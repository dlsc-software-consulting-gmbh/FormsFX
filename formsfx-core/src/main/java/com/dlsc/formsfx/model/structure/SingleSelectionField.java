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

import com.dlsc.formsfx.model.event.FieldEvent;
import com.dlsc.formsfx.model.util.BindingMode;
import com.dlsc.formsfx.model.validators.ValidationResult;
import com.dlsc.formsfx.model.validators.Validator;
import com.dlsc.formsfx.view.controls.SimpleComboBoxControl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * This class provides an implementation of a {@link SelectionField} allowing
 * only for single selection.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class SingleSelectionField<V> extends SelectionField<V, SingleSelectionField<V>> {

    /**
     * A {@code SingleSelectionField} can only ever have one item selected.
     * This item is stored in an {@code ObjectProperty}.
     */
    protected final ObjectProperty<V> persistentSelection = new SimpleObjectProperty<>();
    protected final ObjectProperty<V> selection = new SimpleObjectProperty<>();

    /**
     * Every field contains a list of validators. The validators are limited to
     * the ones that correspond to the field's type.
     */
    protected final List<Validator<V>> validators = new ArrayList<>();

    /**
     * The constructor of {@code SingleSelectionField}.
     *
     * @param items
     *              The property that is used to store the items of the field.
     * @param selection
     *              The index of the item that is to be selected.
     */
    protected SingleSelectionField(ListProperty<V> items, int selection) {
        super(items);

        // Sets the initial selection, based on an index. This also determines
        // the persistent selection.

        if (selection < items.size() && selection >= 0) {
            this.selection.set(this.items.get(selection));
            persistentSelection.setValue(this.selection.getValue());
        }

        // The changed property is a binding that compares the persistent
        // selection with the current selection. This means that a field is
        // marked as changed until Field::persist or Field::reset are called
        // or the selection is back to the persistent selection.

        changed.bind(Bindings.createBooleanBinding(() -> persistentSelection.get() == null ? this.selection.get() != null : !persistentSelection.get().equals(this.selection.get()), this.selection, persistentSelection));

        // Changes to the user input are reflected in the value only if the new
        // user input is valid.

        this.selection.addListener((observable, oldValue, newValue) -> validate());

        // Clear the current selection and persistent selection whenever new
        // items are added. The selection is built back up if it is passed along
        // with the new list of items.

        items.addListener((observable, oldValue, newValue) -> {
            this.selection.setValue(null);
            persistentSelection.setValue(null);
        });

        rendererSupplier = () -> new SimpleComboBoxControl<>();
    }

    /**
     * Updates the list of available items to a new list, along with a
     * pre-defined selection.
     *
     * @param newValue
     *              The new list of items.
     * @param newSelection
     *              The new pre-defined selection.
     *
     * @return Returns the current field to allow for chaining.
     */
    public SingleSelectionField<V> items(List<V> newValue, int newSelection) {
        items.setAll(newValue);

        if (newSelection != -1) {
            this.selection.setValue(items.get(newSelection));
            this.persistentSelection.setValue(this.selection.getValue());
        }

        return this;
    }

    /**
     * Updates the list of available items to a new list, without a
     * pre-defined selection.
     *
     * @param newValue
     *              The new list of items.
     *
     * @return Returns the current field to allow for chaining.
     */
    public SingleSelectionField<V> items(List<V> newValue) {
        return this.items(newValue, -1);
    }

    /**
     * Sets the list of validators for the current field. This overrides all
     * validators that have previously been added.
     *
     * @param newValue
     *              The validators that are to be used for validating this
     *              field.
     *
     * @return Returns the current field to allow for chaining.
     */
    @SafeVarargs
    public final SingleSelectionField<V> validate(Validator<V>... newValue) {
        validators.clear();
        Collections.addAll(validators, newValue);
        validate();

        return this;
    }

    /**
     * Sets the selection to the element at the given index.
     *
     * @param index
     *              The index of the element to be selected.
     *
     * @return Returns the current field to allow for chaining.
     */
    public SingleSelectionField<V> select(int index) {
        if (index == -1) {
            selection.setValue(null);
        } else if (index < items.size() && index > -1 && (selection.get() == null || (selection.get() != null && !selection.get().equals(items.get(index))))) {
            selection.setValue(items.get(index));
        }

        return this;
    }

    /**
     * Removes the selection on the current field.
     *
     * @return Returns the current field to allow for chaining.
     */
    public SingleSelectionField<V> deselect() {
        if (selection.get() != null) {
            selection.setValue(null);
        }

        return this;
    }

    /**
     * Binds the given items and selection property with the corresponding
     * elements.
     *
     * @param itemsBinding
     *          The items property to be bound with.
     *
     * @param selectionBinding
     *          The selection property to be bound with.
     *
     * @return Returns the current field to allow for chaining.
     */
    public SingleSelectionField<V> bind(ListProperty<V> itemsBinding, ObjectProperty<V> selectionBinding) {
        items.bindBidirectional(itemsBinding);
        selection.bindBidirectional(selectionBinding);

        return this;
    }

    /**
     * Unbinds the given items and selection property with the corresponding
     * elements.
     *
     * @param itemsBinding
     *          The items property to be unbound with.
     *
     * @param selectionBinding
     *          The selection property to be unbound with.
     *
     * @return Returns the current field to allow for chaining.
     */
    public SingleSelectionField<V> unbind(ListProperty<V> itemsBinding, ObjectProperty<V> selectionBinding) {
        items.unbindBidirectional(itemsBinding);
        selection.unbindBidirectional(selectionBinding);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void setBindingMode(BindingMode newValue) {
        if (BindingMode.CONTINUOUS.equals(newValue)) {
            selection.addListener(bindingModeListener);
        } else {
            selection.removeListener(bindingModeListener);
        }
    }

    /**
     * Stores the field's current value in its persistent value. This stores
     * the user's changes in the model.
     */
    public void persist() {
        if (!isValid()) {
            return;
        }

        persistentSelection.setValue(selection.getValue());

        fireEvent(FieldEvent.fieldPersistedEvent(this));
    }

    /**
     * Sets the field's current value to its persistent value, thus resetting
     * any changes made by the user.
     */
    public void reset() {
        if (!hasChanged()) {
            return;
        }

        selection.setValue(persistentSelection.getValue());

        fireEvent(FieldEvent.fieldResetEvent(this));
    }

    /**
     * {@inheritDoc}
     */
    protected boolean validateRequired() {
        return !isRequired() || (isRequired() && selection.get() != null);
    }

    /**
     * Validates a user input based on the field's selection and its validation
     * rules. Also considers the {@code required} flag. This method directly
     * updates the {@code valid} property.
     *
     * @return Returns whether the user selection is a valid value or not.
     */
    public boolean validate() {

        // Check all validation rules and collect any error messages.

        List<String> errorMessages = validators.stream()
                .map(v -> v.validate(selection.getValue()))
                .filter(r -> !r.getResult())
                .map(ValidationResult::getErrorMessage)
                .collect(Collectors.toList());

        return super.validate(errorMessages);
    }

    public V getSelection() {
        return selection.get();
    }

    public ObjectProperty<V> selectionProperty() {
        return selection;
    }

}
