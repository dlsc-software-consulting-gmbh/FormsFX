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
import com.dlsc.formsfx.view.controls.SimpleListViewControl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class provides an implementation of a {@link MultiSelectionField}
 * allowing for multi-selection.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class MultiSelectionField<V> extends SelectionField<V, MultiSelectionField<V>> {

    /**
     * A {@code MultiSelectionField} can have multiple items selected. These
     * items are stored in a {@code ListProperty}.
     */
    protected final ListProperty<V> persistentSelection = new SimpleListProperty<>(FXCollections.observableArrayList());
    protected final ListProperty<V> selection = new SimpleListProperty<>(FXCollections.observableArrayList());

    /**
     * Every field contains a list of validators. The validators are limited to
     * the ones that correspond to the field's type.
     */
    protected final List<Validator<ObservableList<V>>> validators = new ArrayList<>();

    /**
     * The constructor of {@code MultiSelectionField}.
     *
     * @param items
     *              The property that is used to store the items of the field.
     * @param selection
     *              The list of indices of items that are to be selected.
     */
    protected MultiSelectionField(ListProperty<V> items, List<Integer> selection) {
        super(items);

        // Add items to the selection, based on their indices. This also
        // determines the persistent selection.

        selection.forEach(i -> {
            if (i < this.items.size() && i >= 0) {
                this.selection.add(this.items.get(i));
            }
        });

        persistentSelection.addAll(this.selection.getValue());

        // The changed property is a binding that compares the persistent
        // selection with the current selection. This means that a field is
        // marked as changed until Field::persist or Field::reset are called
        // or the selection is back to the persistent selection.

        changed.bind(Bindings.createBooleanBinding(() -> !persistentSelection.equals(this.selection), this.selection, persistentSelection));

        // Changes to the user input are reflected in the value only if the new
        // user input is valid.

        this.selection.addListener((observable, oldValue, newValue) -> validate());

        // Clear the current selection and persistent selection whenever new
        // items are added. The selection is built back up if it is passed along
        // with the new list of items.

        items.addListener((observable, oldValue, newValue) -> {
            this.selection.clear();
            persistentSelection.clear();
        });

        rendererSupplier = () -> new SimpleListViewControl<>();
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
    public MultiSelectionField<V> items(List<V> newValue, List<Integer> newSelection) {
        items.setAll(newValue);

        newSelection.forEach(i -> selection.add(items.get(i)));
        persistentSelection.setAll(selection.getValue());

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
    public MultiSelectionField<V> items(List<V> newValue) {
        return this.items(newValue, new ArrayList<>());
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
    public final MultiSelectionField<V> validate(Validator<ObservableList<V>>... newValue) {
        validators.clear();
        Collections.addAll(validators, newValue);
        validate();

        return this;
    }

    /**
     * Adds the element at the given index to the current selection.
     *
     * @param index
     *              The index of the element to be selected.
     *
     * @return Returns the current field to allow for chaining.
     */
    public MultiSelectionField<V> select(int index) {
        if (index < items.size() && index > -1 && !selection.contains(items.get(index))) {
            selection.add(items.get(index));
        }

        return this;
    }

    /**
     * Removes the element at the given index from the current selection.
     *
     * @param index
     *              The index of the element to be removed.
     *
     * @return Returns the current field to allow for chaining.
     */
    public MultiSelectionField<V> deselect(int index) {
        if (index < items.size() && selection.contains(items.get(index))) {
            selection.remove(items.get(index));
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
    public MultiSelectionField<V> bind(ListProperty<V> itemsBinding, ListProperty<V> selectionBinding) {
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
    public MultiSelectionField<V> unbind(ListProperty<V> itemsBinding, ListProperty<V> selectionBinding) {
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
     * Stores the field's current selection in its persistent selection. This
     * stores the user's changes in the model.
     */
    public void persist() {
        if (!isValid()) {
            return;
        }

        persistentSelection.setAll(selection.getValue());

        fireEvent(FieldEvent.fieldPersistedEvent(this));
    }

    /**
     * Sets the field's current selection to its persistent selection, thus
     * resetting any changes made by the user.
     */
    public void reset() {
        if (!hasChanged()) {
            return;
        }

        selection.setAll(persistentSelection.getValue());

        fireEvent(FieldEvent.fieldResetEvent(this));
    }

    /**
     * {@inheritDoc}
     */
    protected boolean validateRequired() {
        return !isRequired() || (isRequired() && selection.size() > 0);
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

    public ObservableList<V> getSelection() {
        return selection.get();
    }

    public ListProperty<V> selectionProperty() {
        return selection;
    }

}
