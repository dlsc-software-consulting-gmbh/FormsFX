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
import com.dlsc.formsfx.model.util.TranslationService;
import com.dlsc.formsfx.model.util.ValueTransformer;
import com.dlsc.formsfx.model.validators.ValidationResult;
import com.dlsc.formsfx.model.validators.Validator;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@code DataField} holds a single value. This value can be represented and
 * manipulated as a {@code String}. It is stored as a concrete type.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public abstract class DataField<P extends Property, V, F extends Field<F>> extends Field<F> {
  
    /**
     * Every field tracks its value in multiple ways.
     *
     * - The user input is bound to a specific control's input value and is a
     *   1-to-1 representation of what the user enters.
     * - The value is the last valid value entered by the user. This means that
     *   the value passes the type transformation of the concrete field and all
     *   user-defined validations.
     * - The persistent value is the value that was last saved on the field. It
     *   is the responsibility of the form creator to persist the field values
     *   at the correct time.
     */
    protected final P value;
    protected final P persistentValue;
    protected final StringProperty userInput = new SimpleStringProperty("");

    /**
     * Every field contains a list of validators. The validators are limited to
     * the ones that correspond to the field's type.
     */
    protected final List<Validator<V>> validators = new ArrayList<>();

    /**
     * The value transformer is responsible for transforming the user input
     * string to the specific type of the field's value.
     * @deprecated Use DataField#stringConverter instead.
     */
    @Deprecated
    ValueTransformer<V> valueTransformer;

    protected StringConverter<V> stringConverter = new AbstractStringConverter<V>() {
        @Override
        public V fromString(String string) {
            return null;
        }
    };

    /**
     * The format error is displayed when the value transformation fails.
     *
     * This property is translatable if a {@link TranslationService} is set on
     * the containing form.
     */
    protected final StringProperty formatErrorKey = new SimpleStringProperty("");
    protected final StringProperty formatError = new SimpleStringProperty("");

    /**
     * This listener updates the field when the external binding changes.
     */
    private final InvalidationListener externalBindingListener = (observable) -> userInput.setValue(stringConverter.toString((V) ((P) observable).getValue()));

    /**
     * Internal constructor for the {@code DataField} class. To create new
     * elements, see the static factory methods in {@code Field}.
     *
     * @see Field::ofStringType
     * @see Field::ofIntegerType
     * @see Field::ofDoubleType
     * @see Field::ofBooleanType
     *
     * @param valueProperty
     *              The property that is used to store the current valid value
     *              of the field.
     * @param persistentValueProperty
     *              The property that is used to store the latest persisted
     *              value of the field.
     */
    protected DataField(P valueProperty, P persistentValueProperty) {
        value = valueProperty;
        persistentValue = persistentValueProperty;

        // The changed property is a binding that compares the persistent value
        // with the current value. This means that a field is marked as changed
        // until Field::persist or Field::reset are called or the value is back
        // to the persistent value.

        changed.bind(Bindings.createBooleanBinding(() -> !stringConverter.toString((V) persistentValue.getValue()).equals(userInput.getValue()), userInput, persistentValue));

        // Whenever one of the translatable elements' keys change, update the
        // displayed value based on the new translation.

        formatErrorKey.addListener((observable, oldValue, newValue) -> formatError.setValue(translationService.translate(newValue)));

        // Changes to the user input are reflected in the value only if the new
        // user input is valid.

        userInput.addListener((observable, oldValue, newValue) -> {
            if (validate()) {
                value.setValue(stringConverter.fromString(newValue));
            }
        });
    }

    protected static abstract class AbstractStringConverter<V> extends StringConverter<V> {
        @Override
        public String toString(V object) {
            return String.valueOf(object);
        }
    }

    private static class StringConverterAdapter<V> extends AbstractStringConverter<V> {
        private final ValueTransformer<V> valueTransformer;

        public StringConverterAdapter(ValueTransformer<V> valueTransformer) {
            this.valueTransformer = valueTransformer;
        }

        @Override
        public V fromString(String string) {
            return valueTransformer.transform(string);
        }
    }

    /**
     * Sets the string converter for the current field.
     *
     * @param newValue
     *              The string converter that transforms the user input string to
     *              the field's underlying value and back.
     *
     * @return Returns the current field to allow for chaining.
     */
    public F format(StringConverter<V> newValue) {
        stringConverter = newValue;
        validate();
        return (F) this;
    }

    /**
     * Applies a new string converter that converts the entered string input
     * to a concrete value.
     *
     * @param newValue
     *              The string converter that transforms the user input string to
     *              the field's underlying value and back.
     * @param errorMessage
     *              The error message to display if the transformation was
     *              unsuccessful.
     *
     * @return Returns the current field to allow for chaining.
     */
    public F format(StringConverter<V> newValue, String errorMessage) {
        stringConverter = newValue;

        if (isI18N()) {
            formatErrorKey.set(errorMessage);
        } else {
            formatError.set(errorMessage);
        }

        validate();
        return (F) this;
    }

    /**
     * Sets the value transformer for the current field.
     *
     * @param newValue
     *              The value transformer that parses the user input string to
     *              the field's underlying value.
     *
     * @return Returns the current field to allow for chaining.
     * @deprecated Use format(StringConverter) instead
     */
    @Deprecated
    public F format(ValueTransformer<V> newValue) {
        stringConverter = new StringConverterAdapter<>(newValue);
        validate();
        return (F) this;
    }

    /**
     * Applies a new value transformer that converts the entered string input
     * to a concrete value.
     *
     * @param newValue
     *              The new value transformer. Takes a string as an input and
     *              returns the concrete type.
     * @param errorMessage
     *              The error message to display if the transformation was
     *              unsuccessful.
     *
     * @return Returns the current field to allow for chaining.
     * @deprecated Use format(StringConverter, errorMessage) instead
     */
    @Deprecated
    public F format(ValueTransformer<V> newValue, String errorMessage) {
        stringConverter = new StringConverterAdapter<>(newValue);

        if (isI18N()) {
            formatErrorKey.set(errorMessage);
        } else {
            formatError.set(errorMessage);
        }

        validate();
        return (F) this;
    }

    /**
     * Adds an error message to handle formatting errors with the default
     * value transformers.
     *
     * @param errorMessage
     *              The error message to display if the transformation was
     *              unsuccessful.
     *
     * @return Returns the current field to allow for chaining.
     */
    public F format(String errorMessage) {
        if (isI18N()) {
            formatErrorKey.set(errorMessage);
        } else {
            formatError.set(errorMessage);
        }

        validate();
        return (F) this;
    }

    /**
     * Sets the list of validators for the current field. This overrides all
     * validators that have previously been added.
     *
     * @param newValue
     *              The validators that are to be used for validating this
     *              field. Limited to validators that are able to handle the
     *              field's underlying type.
     *
     * @return Returns the current field to allow for chaining.
     */
    @SafeVarargs
    public final F validate(Validator<V>... newValue) {
        validators.clear();
        Collections.addAll(validators, newValue);
        validate();

        return (F) this;
    }

    /**
     * Binds the given property with the field.
     *
     * @param binding
     *          The property to be bound with.
     *
     * @return Returns the current field to allow for chaining.
     */
    public F bind(P binding) {
        persistentValue.bindBidirectional(binding);
        binding.addListener(externalBindingListener);

        return (F) this;
    }

    /**
     * Unbinds the given property with the field.
     *
     * @param binding
     *          The property to be unbound with.
     *
     * @return Returns the current field to allow for chaining.
     */
    public F unbind(P binding) {
        persistentValue.unbindBidirectional(binding);
        binding.removeListener(externalBindingListener);

        return (F) this;
    }

    /**
     * {@inheritDoc}
     */
    public void setBindingMode(BindingMode newValue) {
        if (BindingMode.CONTINUOUS.equals(newValue)) {
            value.addListener(bindingModeListener);
        } else {
            value.removeListener(bindingModeListener);
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

        persistentValue.setValue(value.getValue());

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

        userInput.setValue(stringConverter.toString((V) persistentValue.getValue()));
    }

    /**
     * Validates that the new field input matches the required condition.
     *
     * @param newValue
     *              The new value to check for the required state.
     *
     * @return Returns whether the input matches the required condition.
     */
    protected boolean validateRequired(String newValue) {
        return !isRequired() || (isRequired() && !newValue.isEmpty());
    }

    /**
     * Validates a user input based on the field's value transformer and its
     * validation rules. Also considers the {@code required} flag. This method
     * directly updates the {@code valid} property.
     *
     * @return Returns whether the user input is a valid value or not.
     */
    public boolean validate() {
        String newValue = userInput.getValue();

        if (!validateRequired(newValue)) {
            if (isI18N() && !requiredErrorKey.get().isEmpty()) {
                errorMessageKeys.setAll(requiredErrorKey.get());
            } else if (!requiredError.get().isEmpty()) {
                errorMessages.setAll(requiredError.get());
            }

            valid.set(false);
            return false;
        }

        V transformedValue;

        // Attempt a transformation from String to the field's underlying type.

        try {
            transformedValue = stringConverter.fromString(newValue);
        } catch (Exception e) {
            if (isI18N() && !formatErrorKey.get().isEmpty()) {
                errorMessageKeys.setAll(formatErrorKey.get());
            } else if (!formatError.get().isEmpty()) {
                errorMessages.setAll(formatError.get());
            }

            valid.set(false);
            return false;
        }

        // Check all validation rules and collect any error messages.

        List<String> errorMessages = validators.stream()
                .map(v -> v.validate(transformedValue))
                .filter(r -> !r.getResult())
                .map(ValidationResult::getErrorMessage)
                .collect(Collectors.toList());

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

        // If all above conditions have succeeded, the user input is
        // considered valid.

        valid.set(true);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void translate(TranslationService service) {
        super.translate(service);

        updateElement(formatError, formatErrorKey);
        validate();
    }

    public String getUserInput() {
        return userInput.get();
    }

    public StringProperty userInputProperty() {
        return userInput;
    }

    public V getValue() {
        return (V) value.getValue();
    }

    public P valueProperty() {
        return value;
    }

}
