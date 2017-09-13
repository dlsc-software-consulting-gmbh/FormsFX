package com.dlsc.formsfx.model.structure;

/*-
 * ========================LICENSE_START=================================
 * FormsFX
 * %%
 * Copyright (C) 2017 DLSC Software & Consulting
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */


import com.dlsc.formsfx.model.util.TranslationService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A group is the intermediate unit in a form. It is used to group form
 * fields to a larger unit. It also acts as a proxy to some properties of
 * the contained data, such as validity or changes.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class Group {

    final List<Field> fields = new ArrayList<>();

    /**
     * The group acts as a proxy for its contained fields' {@code changed}
     * and {@code valid} properties.
     */
    private final BooleanProperty valid = new SimpleBooleanProperty(true);
    private final BooleanProperty changed = new SimpleBooleanProperty(false);

    /**
     * The translation service is passed down from the containing form. It
     * is used to translate all translatable values of the field.
     */
    TranslationService translationService;

    /**
     * Internal constructor for the {@code Group} class. To create new
     * groups, see the static factory method in this class.
     *
     * @see Group::of
     *
     * @param fields
     *              A varargs list of fields that are contained in this
     *              group.
     */
    Group(Field... fields) {
        Collections.addAll(this.fields, fields);

        // If any of the fields are marked as changed, the group is updated
        // accordingly.

        this.fields.forEach(f -> f.changedProperty().addListener((observable, oldValue, newValue) -> setChangedProperty()));

        // If any of the fields are marked as invalid, the group is updated
        // accordingly.

        this.fields.forEach(f -> f.validProperty().addListener((observable, oldValue, newValue) -> setValidProperty()));

        setValidProperty();
        setChangedProperty();
    }

    /**
     * Creates a new group containing the given fields.
     *
     * @param fields
     *              The fields to be included in the group.
     *
     * @return Returns a new {@code Group}.
     */
    public static Group of(Field... fields) {
        return new Group(fields);
    }

    /**
     * This internal method is called by the containing form when a new
     * translation has been added to the form. Also applies the translation
     * to all contained fields.
     *
     * @see Field::translate
     *
     * @param newValue
     *              The new service to use for translating translatable values.
     */
    void translate(TranslationService newValue) {
        translationService = newValue;

        if (!isI18N()) {
            return;
        }

        fields.forEach(f -> f.translate(translationService));
    }

    /**
     * Persists the values for all contained fields.
     * @see Field::persist
     */
    void persist() {
        if (!isValid()) {
            return;
        }

        fields.forEach(Field::persist);
    }

    /**
     * Resets the values for all contained fields.
     * @see Field::reset
     */
    void reset() {
        if (!hasChanged()) {
            return;
        }

        fields.forEach(Field::reset);
    }

    /**
     * Sets this group's {@code changed} property based on its contained
     * fields' changed properties.
     */
    private void setChangedProperty() {
        changed.setValue(fields.stream().anyMatch(Field::hasChanged));
    }

    /**
     * Sets this group's {@code valid} property based on its contained fields'
     * changed properties.
     */
    private void setValidProperty() {
        valid.setValue(fields.stream().allMatch(Field::isValid));
    }

    public List<Field> getFields() {
        return fields;
    }

    public boolean hasChanged() {
        return changed.get();
    }

    public BooleanProperty changedProperty() {
        return changed;
    }

    public boolean isValid() {
        return valid.get();
    }

    public BooleanProperty validProperty() {
        return valid;
    }

    public boolean isI18N() {
        return translationService != null;
    }

}
