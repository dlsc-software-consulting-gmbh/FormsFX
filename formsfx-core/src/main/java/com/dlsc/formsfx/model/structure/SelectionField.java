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

import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * {@code SelectionField} holds a list of values. Users can select one or more
 * of these values, depending on the concrete type of the field.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public abstract class SelectionField<V, F extends SelectionField<V, F>> extends Field<F> {

    /**
     * Stores a typed list of available items on this field.
     */
    protected final ListProperty<V> items;

    /**
     * Internal constructor for the {@code SelectionField} class. To create new
     * elements, see the static factory methods in {@code Field}.
     *
     * @see Field::ofMultiSelectionType
     * @see Field::ofSingleSelectionType
     *
     * @param items
     *              The list of available items on the field.
     */
    protected SelectionField(ListProperty<V> items) {
        this.items = items;
    }

    /**
     * Validates that the new field input matches the required condition.
     *
     * @return Returns whether the input matches the required condition.
     */
    protected abstract boolean validateRequired();

    /**
     * Validates a user input based on the field's selection and its validation
     * rules. Also considers the {@code required} flag. This method directly
     * updates the {@code valid} property.
     *
     * This method should not be called directly but instead only be used in
     * concrete subclasses.
     *
     * @param errorMessages
     *              A list of error messages based on the field's validators.
     *
     * @return Returns whether the user selection is a valid value or not.
     */
    protected boolean validate(List<String> errorMessages) {
        if (!validateRequired()) {
            if (isI18N() && requiredErrorKey.get() != null) {
                this.errorMessageKeys.setAll(requiredErrorKey.get());
            } else if (requiredError.get() != null) {
                this.errorMessages.setAll(requiredError.get());
            }

            valid.set(false);
            return false;
        }

        // Update the validation results with the current results. Listeners
        // will handle the translation aspect.

        if (isI18N()) {
            errorMessageKeys.setAll(errorMessages);
        } else {
            this.errorMessages.setAll(errorMessages);
        }

        if (errorMessages.size() > 0) {
            valid.set(false);
            return false;
        }

        // If, and only if all above conditions have succeeded, the user input
        // is considered valid.

        valid.set(true);
        return true;
    }

    public ObservableList getItems() {
        return items.get();
    }

    public ListProperty<V> itemsProperty() {
        return items;
    }

}
