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

import com.dlsc.formsfx.model.util.BindingMode;
import com.dlsc.formsfx.model.util.TranslationService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A form is the containing unit for sections and elements and is used to bring
 * structure to form data. It also acts as a proxy to some properties of the
 * contained data, such as validity or changes.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class Form {

    private final List<Group> groups = new ArrayList<>();

    /**
     * The title acts as a description for the form.
     *
     * This property is translatable if a {@link TranslationService} is set.
     */
    private final StringProperty title = new SimpleStringProperty("");
    private final StringProperty titleKey = new SimpleStringProperty("");

    /**
     * The form acts as a proxy for its contained sections' {@code changed}
     * and {@code valid} properties.
     */
    private final BooleanProperty valid = new SimpleBooleanProperty(true);
    private final BooleanProperty changed = new SimpleBooleanProperty(false);
    private final BooleanProperty persistable = new SimpleBooleanProperty(false);

    /**
     * A form can optionally have a translation service. This service is used to
     * translate displayed values into multiple locales. The form registers
     * itself as a listener on the translation service to handle locale changes.
     *
     * @see TranslationService
     */
    private final ObjectProperty<TranslationService> translationService = new SimpleObjectProperty<>();
    private final Runnable localeChangeListener = this::translate;

    /**
     * Internal constructor for the {@code Form} class. To create new
     * forms, see the static factory method in this class.
     *
     * @see Form::of
     *
     * @param groups
     *              A varargs list of groups that are contained in this
     *              form.
     */
    private Form(Group... groups) {
        Collections.addAll(this.groups, groups);

        // If any of the groups are marked as changed, the section is updated
        // accordingly.

        this.groups.forEach(s -> s.changedProperty().addListener((observable, oldValue, newValue) -> setChangedProperty()));

        // If any of the groups are marked as invalid, the section is updated
        // accordingly.

        this.groups.forEach(s -> s.validProperty().addListener((observable, oldValue, newValue) -> setValidProperty()));

        setValidProperty();
        setChangedProperty();
        setPersistableProperty();

        // Whenever the title's key changes, update the displayed value based
        // on the new translation.

        titleKey.addListener((observable, oldValue, newValue) -> title.setValue(translationService.get().translate(newValue)));

        // Whenever the underlying translation service changes, update all
        // translation on the form and its contained elements.

        translationService.addListener((observable, oldValue, newValue) -> translate());
    }

    /**
     * Creates a new form containing the given sections.
     *
     * @param sections
     *              The sections to be included in the form.
     *
     * @return Returns a new {@code Form}.
     */
    public static Form of(Group... sections) {
        return new Form(sections);
    }

    /**
     * Sets the title property of the current form.
     *
     * @param newValue
     *              The new value for the title property. This can be the title
     *              itself or a key that is then used for translation.
     *
     * @see TranslationService
     *
     * @return Returns the current form to allow for chaining.
     */
    public Form title(String newValue) {
        if (isI18N()) {
            titleKey.set(newValue);
        } else {
            title.set(newValue);
        }

        return this;
    }

    /**
     * Sets the translation service property of the current form.
     *
     * @param newValue
     *              The new value for the translation service property.
     *
     * @return Returns the current form to allow for chaining.
     */
    public Form i18n(TranslationService newValue) {
        if (translationService.get() != null) {
            translationService.get().removeListener(localeChangeListener);
        }

        translationService.setValue(newValue);
        translationService.get().addListener(localeChangeListener);

        return this;
    }

    /**
     * Changes the way field values are bound to external properties.
     *
     * @see BindingMode
     *
     * @param newValue
     *              The new mode for handling external bindings.
     *
     * @return Returns the current form to allow for chaining.
     */
    public Form binding(BindingMode newValue) {
        getFields().forEach(f -> f.setBindingMode(newValue));
        return this;
    }

    /**
     * This internal method is used as a callback for when the translation
     * service or its locale changes. Also applies the translation to all
     * contained sections.
     *
     * @see Group::translate
     */
    private void translate() {
        TranslationService tr = translationService.get();

        if (!isI18N()) {
            return;
        }

        if (titleKey.get() == null || titleKey.get().isEmpty()) {
            titleKey.setValue(title.get());
        } else {
            title.setValue(tr.translate(titleKey.get()));
        }

        groups.forEach(s -> s.translate(tr));
    }

    /**
     * Persists the values for all elements contained in this form's groups.
     *
     * @see Field::reset
     */
    public void persist() {
        if (!isPersistable()) {
            return;
        }

        groups.forEach(Group::persist);
    }

    /**
     * Resets the values for all elements contained in this form's groups.
     *
     * @see Field::reset
     */
    public void reset() {
        if (!hasChanged()) {
            return;
        }

        groups.forEach(Group::reset);
    }

    /**
     * Sets this form's {@code changed} property based on its contained
     * groups' changed properties.
     */
    private void setChangedProperty() {
        changed.setValue(groups.stream().anyMatch(Group::hasChanged));
        setPersistableProperty();
    }

    /**
     * Sets this form's {@code valid} property based on its contained groups'
     * changed properties.
     */
    private void setValidProperty() {
        valid.setValue(groups.stream().allMatch(Group::isValid));
        setPersistableProperty();
    }

    /**
     * Sets this form's {@code persistable} property based on its contained
     * groups' persistable properties.
     */
    private void setPersistableProperty() {
        persistable.setValue(groups.stream().anyMatch(Group::hasChanged) && groups.stream().allMatch(Group::isValid));
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Element> getElements() {
        return groups.stream()
                .map(Group::getElements)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<Field> getFields() {
        return groups.stream()
            .map(Group::getElements)
            .flatMap(List::stream)
            .filter(e -> e instanceof Field)
            .map(e -> (Field) e)
            .collect(Collectors.toList());
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

    public boolean isPersistable() {
        return persistable.get();
    }

    public BooleanProperty persistableProperty() {
        return persistable;
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public boolean isI18N() {
        return translationService.get() != null;
    }

}
