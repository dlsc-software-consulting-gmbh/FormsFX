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
import com.dlsc.formsfx.view.controls.SimpleControl;
import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import java.util.stream.Collectors;

/**
 * This class provides the base implementation for a FormsFX field. It is not
 * meant to be used directly, but instead acts as a base for concrete
 * implementations.
 *
 * A field is the smallest unit of the form. It contains only the value and
 * relevant information.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public abstract class Field<F extends Field<F>> extends Element<F> implements FormElement {

    /**
     * The label acts as a description for the field. It is always visible to
     * the user and tells them what should be entered into the field.
     *
     * This property is translatable if a {@link TranslationService} is set on
     * the containing form.
     */
    protected final StringProperty label = new SimpleStringProperty("");
    protected final StringProperty labelKey = new SimpleStringProperty("");

    /**
     * The tooltip is an extension of the label. It contains additional
     * information about the contained data that is only contextually visible.
     *
     * This property is translatable if a {@link TranslationService} is set on
     * the containing form.
     */
    protected final StringProperty tooltip = new SimpleStringProperty("");
    protected final StringProperty tooltipKey = new SimpleStringProperty("");

    /**
     * The placeholder is only visible in an empty field. It provides a hint to
     * the user about what should be entered into the field.
     *
     * This property is translatable if a {@link TranslationService} is set on
     * the containing form.
     */
    protected final StringProperty placeholder = new SimpleStringProperty("");
    protected final StringProperty placeholderKey = new SimpleStringProperty("");

    /**
     * Every field can be marked as {@code required} and {@code editable}. These
     * properties can change the field's behaviour.
     */
    protected final StringProperty requiredErrorKey = new SimpleStringProperty("");
    protected final StringProperty requiredError = new SimpleStringProperty("");
    protected final BooleanProperty required = new SimpleBooleanProperty(false);
    protected final BooleanProperty editable = new SimpleBooleanProperty(true);

    /**
     * The field's current state is represented by the value properties, as
     * well as by the {@code valid} and {@code changed} flags.
     */
    protected final BooleanProperty valid = new SimpleBooleanProperty(true);
    protected final BooleanProperty changed = new SimpleBooleanProperty(false);

    /**
     * Fields can be styled using CSS through ID or class hooks.
     */
    protected final StringProperty id = new SimpleStringProperty(UUID.randomUUID().toString());
    protected final ListProperty<String> styleClass = new SimpleListProperty<>(FXCollections.observableArrayList());
    protected final IntegerProperty span = new SimpleIntegerProperty(12);

    /**
     * The results of the field's validation is stored in this property. After
     * every validation, the results are updated and reflected in this property.
     *
     * This property is translatable if a {@link TranslationService} is set on
     * the containing form.
     */
    protected final ListProperty<String> errorMessages = new SimpleListProperty<>(FXCollections.observableArrayList());
    protected final ListProperty<String> errorMessageKeys = new SimpleListProperty<>(FXCollections.observableArrayList());

    /**
     * Additional descriptions for the field's label and value are stored in these properties.
     *
     * These properties are translatable if a {@link TranslationService} is set on
     * the containing form.
     */
    private Node labelDescription;
    private Node valueDescription;
    private final StringProperty labelDescriptionKey = new SimpleStringProperty("");
    private final StringProperty valueDescriptionKey = new SimpleStringProperty("");

    private static final String LABEL_DESCRIPTION_STYLE_CLASS = "field-label-description";
    private static final String VALUE_DESCRIPTION_STYLE_CLASS = "field-value-description";

    /**
     * The translation service is passed down from the containing section. It
     * is used to translate all translatable values of the field.
     */
    protected TranslationService translationService;

    protected SimpleControl<F> renderer;

    protected final Map<EventType<FieldEvent>,List<EventHandler<? super FieldEvent>>> eventHandlers = new ConcurrentHashMap<>();

    /**
     * With the continuous binding mode, values are always directly persisted
     * upon any changes.
     */
    protected final InvalidationListener bindingModeListener = (observable) -> {
        if (validate()) {
            persist();
        }
    };

    /**
     * Internal constructor for the {@code Field} class. To create new elements,
     * see the static factory methods in this class.
     *
     * @see Field::ofStringType
     * @see Field::ofIntegerType
     * @see Field::ofDoubleType
     * @see Field::ofBooleanType
     * @see Field::ofMultiSelectionType
     * @see Field::ofSingleSelectionType
     */
    protected Field() {

        // Whenever one of the translatable elements' keys change, update the
        // displayed value based on the new translation.

        labelKey.addListener((observable, oldValue, newValue) -> label.setValue(translationService.translate(newValue)));

        tooltipKey.addListener((observable, oldValue, newValue) -> tooltip.setValue(translationService.translate(newValue)));

        placeholderKey.addListener((observable, oldValue, newValue) -> placeholder.setValue(translationService.translate(newValue)));

        labelDescriptionKey.addListener((observable, oldValue, newValue) -> labelDescription = asLabel(translationService.translate(newValue), LABEL_DESCRIPTION_STYLE_CLASS));

        valueDescriptionKey.addListener((observable, oldValue, newValue) -> valueDescription = asLabel(translationService.translate(newValue), VALUE_DESCRIPTION_STYLE_CLASS));

        requiredErrorKey.addListener((observable, oldValue, newValue) -> validate());

        // Whenever the errorMessageKeys change, update the displayed
        // label to the new translation. This maps the keys to their translated
        // representation.

        errorMessageKeys.addListener((observable, oldValue, newValue) -> errorMessages.setAll(errorMessageKeys.stream()
                .map(s -> translationService.translate(s))
                .collect(Collectors.toList())));
    }

    /**
     * Creates a new {@link PasswordField} with the given default value.
     *
     * @param defaultValue
     *              The initial value and persistent value of the field.
     *
     * @return Returns a new {@link PasswordField}.
     */
    public static PasswordField ofPasswordType(String defaultValue) {
        return new PasswordField(new SimpleStringProperty(defaultValue), new SimpleStringProperty(defaultValue));
    }

    /**
     * Creates a new {@link PasswordField} with the given property.
     *
     * @param binding
     *          The property from the model to be bound with.
     *
     * @return Returns a new {@link PasswordField}.
     */
    public static PasswordField ofPasswordType(StringProperty binding) {
        return new PasswordField(new SimpleStringProperty(binding.getValue()), new SimpleStringProperty(binding.getValue())).bind(binding);
    }

    /**
     * Creates a new {@link StringField} with the given default value.
     *
     * @param defaultValue
     *              The initial value and persistent value of the field.
     *
     * @return Returns a new {@link StringField}.
     */
    public static StringField ofStringType(String defaultValue) {
        return new StringField(new SimpleStringProperty(defaultValue), new SimpleStringProperty(defaultValue));
    }

    /**
     * Creates a new {@link StringField} with the given property.
     *
     * @param binding
     *          The property from the model to be bound with.
     *
     * @return Returns a new {@link StringField}.
     */
    public static StringField ofStringType(StringProperty binding) {
        return new StringField(new SimpleStringProperty(binding.getValue()), new SimpleStringProperty(binding.getValue())).bind(binding);
    }

    /**
     * Creates a new {@link DoubleField} with the given default value.
     *
     * @param defaultValue
     *              The initial value and persistent value of the field.
     *
     * @return Returns a new {@link DoubleField}.
     */
    public static DoubleField ofDoubleType(double defaultValue) {
        return new DoubleField(new SimpleDoubleProperty(defaultValue), new SimpleDoubleProperty(defaultValue));
    }

    /**
     * Creates a new {@link DoubleField} with the given property.
     *
     * @param binding
     *          The property from the model to be bound with.
     *
     * @return Returns a new {@link DoubleField}.
     */
    public static DoubleField ofDoubleType(DoubleProperty binding) {
        return new DoubleField(new SimpleDoubleProperty(binding.getValue()), new SimpleDoubleProperty(binding.getValue())).bind(binding);
    }

    /**
     * Creates a new {@link IntegerField} with the given default value.
     *
     * @param defaultValue
     *              The initial value and persistent value of the field.
     *
     * @return Returns a new {@link IntegerField}.
     */
    public static IntegerField ofIntegerType(int defaultValue) {
        return new IntegerField(new SimpleIntegerProperty(defaultValue), new SimpleIntegerProperty(defaultValue));
    }

    /**
     * Creates a new {@link IntegerField} with the given property.
     *
     * @param binding
     *          The property from the model to be bound with.
     *
     * @return Returns a new {@link IntegerField}.
     */
    public static IntegerField ofIntegerType(IntegerProperty binding) {
        return new IntegerField(new SimpleIntegerProperty(binding.getValue()), new SimpleIntegerProperty(binding.getValue())).bind(binding);
    }

    /**
     * Creates a new {@link BooleanField} with the given default value.
     *
     * @param defaultValue
     *              The initial value and persistent value of the field.
     *
     * @return Returns a new {@link BooleanField}.
     */
    public static BooleanField ofBooleanType(boolean defaultValue) {
        return new BooleanField(new SimpleBooleanProperty(defaultValue), new SimpleBooleanProperty(defaultValue));
    }

    /**
     * Creates a new {@link BooleanField} with the given property.
     *
     * @param binding
     *          The property from the model to be bound with.
     *
     * @return Returns a new {@link BooleanField}.
     */
    public static BooleanField ofBooleanType(BooleanProperty binding) {
        return new BooleanField(new SimpleBooleanProperty(binding.getValue()), new SimpleBooleanProperty(binding.getValue())).bind(binding);
    }

    /**
     * Creates a new {@link MultiSelectionField} with the given items and a
     * pre-defined selection.
     *
     * @param items
     *              The list of available items on the field.
     * @param selection
     *              The pre-defined indices of the selected items.
     *
     * @return Returns a new {@link MultiSelectionField}.
     */
    public static <T> MultiSelectionField<T> ofMultiSelectionType(List<T> items, List<Integer> selection) {
        return new MultiSelectionField<>(new SimpleListProperty<>(FXCollections.observableArrayList(items)), selection);
    }

    /**
     * Creates a new {@link MultiSelectionField} with the given items and no
     * pre-defined selection.
     *
     * @param items
     *              The list of available items on the field.
     *
     * @return Returns a new {@link MultiSelectionField}.
     */
    public static <T> MultiSelectionField<T> ofMultiSelectionType(List<T> items) {
        return new MultiSelectionField<>(new SimpleListProperty<>(FXCollections.observableArrayList(items)), new ArrayList<>());
    }

    /**
     * Creates a new {@link MultiSelectionField} with the given properties for
     * items and selection.
     *
     * @param itemsBinding
     *          The items property to be bound with.
     *
     * @param selectionBinding
     *          The selection property to be bound with.
     *
     * @return Returns a new {@link MultiSelectionField}.
     */
    public static <T> MultiSelectionField<T> ofMultiSelectionType(ListProperty<T> itemsBinding, ListProperty<T> selectionBinding) {
        return new MultiSelectionField<>(new SimpleListProperty<>(itemsBinding.getValue()), new ArrayList<>(selectionBinding.getValue().stream().map(t -> itemsBinding.getValue().indexOf(t)).collect(Collectors.toList()))).bind(itemsBinding, selectionBinding);
    }

    /**
     * Creates a new {@link SingleSelectionField} with the given items and a
     * pre-defined selection.
     *
     * @param items
     *              The list of available items on the field.
     * @param selection
     *              The pre-defined index of the selected item.
     *
     * @return Returns a new {@link SingleSelectionField}.
     */
    public static <T> SingleSelectionField<T> ofSingleSelectionType(List<T> items, int selection) {
        return new SingleSelectionField<>(new SimpleListProperty<>(FXCollections.observableArrayList(items)), selection);
    }

    /**
     * Creates a new {@link SingleSelectionField} with the given items and no
     * pre-defined selection.
     *
     * @param items
     *              The list of available items on the field.
     *
     * @return Returns a new {@link SingleSelectionField}.
     */
    public static <T> SingleSelectionField<T> ofSingleSelectionType(List<T> items) {
        return new SingleSelectionField<>(new SimpleListProperty<>(FXCollections.observableArrayList(items)), -1);
    }

    /**
     * Creates a new {@link SingleSelectionField} with the given properties for
     * items and selection.
     *
     * @param itemsBinding
     *          The items property to be bound with.
     *
     * @param selectionBinding
     *          The selection property to be bound with.
     *
     * @return Returns a new {@link SingleSelectionField}.
     */
    public static <T> SingleSelectionField<T> ofSingleSelectionType(ListProperty<T> itemsBinding, ObjectProperty<T> selectionBinding) {
        return new SingleSelectionField<>(new SimpleListProperty<>(itemsBinding.getValue()), itemsBinding.indexOf(selectionBinding.getValue())).bind(itemsBinding, selectionBinding);
    }

    /**
     * Creates a new {@link DateField} with given default value
     *
     * @param defaultValue The initial value and persistent value of the field.
     * @return Returns a new {@link DateField}.
     */
    public static DateField ofDate(LocalDate defaultValue) {
        return new DateField(new SimpleObjectProperty<>(defaultValue), new SimpleObjectProperty<>(defaultValue));
    }

    /**
     * Creates a new {@link DateField} with given property
     *
     * @param binding The property from the model to be bound with.
     * @return Returns a new {@link DateField}.
     */
    public static DateField ofDate(ObjectProperty<LocalDate> binding) {
        return new DateField(new SimpleObjectProperty<>(binding.getValue()), new SimpleObjectProperty<>(binding.getValue())).bind(binding);
    }

    /**
     * Sets the required property to for the current field without providing an
     * error message.
     *
     * @param newValue
     *              The new state of the required property.
     *
     * @return Returns the current field to allow for chaining.
     */
    public F required(boolean newValue) {
        required.set(newValue);
        validate();

        return (F) this;
    }

    /**
     * Sets the required property to true for the current field.
     *
     * @param errorMessage
     *              The error message if the field is not filled in.
     *
     * @return Returns the current field to allow for chaining.
     */
    public F required(String errorMessage) {
        required.set(true);

        if (isI18N()) {
            requiredErrorKey.set(errorMessage);
        } else {
            requiredError.set(errorMessage);
        }

        validate();

        return (F) this;
    }

    /**
     * Sets the editable property of the current field.
     *
     * @param newValue
     *              The new value for the editable property.
     *
     * @return Returns the current field to allow for chaining.
     */
    public F editable(boolean newValue) {
        editable.set(newValue);
        return (F) this;
    }

    /**
     * Sets the label property of the current field.
     *
     * @param newValue
     *              The new value for the label property. This can be the label
     *              itself or a key that is then used for translation.
     *
     * @see TranslationService
     *
     * @return Returns the current field to allow for chaining.
     */
    public F label(String newValue) {
        if (isI18N()) {
            labelKey.set(newValue);
        } else {
            label.set(newValue);
        }

        return (F) this;
    }

    /**
     * Sets the label description property of the current field.
     *
     * @param newValue
     *              The new value for the label description property.
     *
     *
     * @return Returns the current field to allow for chaining.
     */
    public F labelDescription(Node newValue) {
        labelDescription = newValue;
        if (labelDescription != null) {
            labelDescription.getStyleClass().add(LABEL_DESCRIPTION_STYLE_CLASS);
        }

        return (F) this;
    }

    /**
     * Sets the label description property of the current field.
     *
     * @param newValue
     *              The new value for the label description property,
     *              wrapped with a {@code Text}.
     *
     *
     * @return Returns the current field to allow for chaining.
     */
    public F labelDescription(String newValue) {
        if(isI18N()) {
            labelDescriptionKey.set(newValue);
        } else if (newValue != null) {
            labelDescription = asLabel(newValue, LABEL_DESCRIPTION_STYLE_CLASS);
        }

        return (F) this;
    }

    /**
     * Sets the value description property of the current field.
     *
     * @param newValue
     *              The new value for the field description property.
     *
     *
     * @return Returns the current field to allow for chaining.
     */
    public F valueDescription(Node newValue) {
        valueDescription = newValue;
        if (valueDescription != null) {
            valueDescription.getStyleClass().add(VALUE_DESCRIPTION_STYLE_CLASS);
        }

        return (F) this;
    }

    /**
     * Sets the value description property of the current field.
     *
     * @param newValue
     *              The new value for the field description property,
     *              wrapped with a {@code Text}.
     *
     *
     * @return Returns the current field to allow for chaining.
     */
    public F valueDescription(String newValue) {
        if(isI18N()) {
            valueDescriptionKey.set(newValue);
        } else if (newValue != null) {
            valueDescription = asLabel(newValue, VALUE_DESCRIPTION_STYLE_CLASS);
        }

        return (F) this;
    }

    private Label asLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.getStyleClass().add(styleClass);
        return label;
    }

    /**
     * Sets the tooltip property of the current field.
     *
     * @param newValue
     *              The new value for the tooltip property. This can be the
     *              label itself or a key that is then used for translation.
     *
     * @see TranslationService
     *
     * @return Returns the current field to allow for chaining.
     */
    public F tooltip(String newValue) {
        if (isI18N()) {
            tooltipKey.set(newValue);
        } else {
            tooltip.set(newValue);
        }

        return (F) this;
    }

    /**
     * Sets the placeholder property of the current field.
     *
     * @param newValue
     *              The new value for the placeholder property. This can be the
     *              label itself or a key that is then used for translation.
     *
     * @see TranslationService
     *
     * @return Returns the current field to allow for chaining.
     */
    public F placeholder(String newValue) {
        if (isI18N()) {
            placeholderKey.set(newValue);
        } else {
            placeholder.set(newValue);
        }

        return (F) this;
    }

    /**
     * Sets the control that renders this field.
     *
     * @param newValue
     *              The new control to render the field.
     *
     * @return Returns the current field to allow for chaining.
     */
    public F render(SimpleControl<F> newValue) {
        renderer = newValue;
        return (F) this;
    }

    /**
     * Activates or deactivates the {@code bindingModeListener} based on the
     * given {@code BindingMode}.
     *
     * @param newValue
     *              The new binding mode for the current field.
     */
    public abstract void setBindingMode(BindingMode newValue);

    // abstract void persist();

    // abstract void reset();

    /**
     * This internal method is called by the containing section when a new
     * translation has been added to the form.
     *
     * @param newValue
     *              The new service to use for translating translatable values.
     */
    public void translate(TranslationService newValue) {
        translationService = newValue;

        if (!isI18N()) {
            return;
        }

        updateElement(label, labelKey);
        updateElement(tooltip, tooltipKey);
        updateElement(placeholder, placeholderKey);
        updateElement(requiredError, requiredErrorKey);
        updateElement(labelDescription, labelDescriptionKey);
        updateElement(valueDescription, valueDescriptionKey);

        // Validation results are handled separately as they use a somewhat
        // more complex structure.

        validate();
    }

    /**
     * Updates a displayable field property to include translated text.
     *
     * @param displayProperty
     *              The property that is displayed to the user.
     * @param keyProperty
     *              The internal property that holds the translation key.
     */
    protected void updateElement(StringProperty displayProperty, StringProperty keyProperty) {

        // If the key has not yet been set that means that the translation
        // service was added for the first time. We can simply set the key
        // to the value stored in the display property, the listener will
        // then take care of the translation.

        if ((keyProperty.get() == null || keyProperty.get().isEmpty()) && !displayProperty.get().isEmpty()) {
            keyProperty.setValue(displayProperty.get());
        } else if (!keyProperty.get().isEmpty()) {
            displayProperty.setValue(translationService.translate(keyProperty.get()));
        }
    }

    /**
     * Updates a displayable field property to include translated text.
     *
     * @param node
     *              The property that is displayed to the user.
     * @param keyProperty
     *              The internal property that holds the translation key.
     */
    void updateElement(Node node, StringProperty keyProperty) {

        // If the key has not yet been set that means that the translation
        // service was added for the first time. We can simply set the key
        // to the value stored in the display property, the listener will
        // then take care of the translation.

        if (!(node instanceof Labeled)) {
            // no automatic update
            return;
        }

        Labeled labeled = (Labeled) node;

        if ((keyProperty.get() == null || keyProperty.get().isEmpty()) && !labeled.getText().isEmpty()) {
            keyProperty.setValue(labeled.getText());
        } else if (!keyProperty.get().isEmpty()) {
            labeled.setText(translationService.translate(keyProperty.get()));
        }
    }

    /**
     * Validates a user input based on the field's value transformer and its
     * validation rules. Also considers the {@code required} flag. This method
     * directly updates the {@code valid} property.
     *
     * @return Returns whether the user input is a valid value or not.
     */
    protected abstract boolean validate();

    public String getPlaceholder() {
        return placeholder.get();
    }

    public StringProperty placeholderProperty() {
        return placeholder;
    }

    public String getLabel() {
        return label.get();
    }

    public StringProperty labelProperty() {
        return label;
    }

    public String getTooltip() {
        return tooltip.get();
    }

    public StringProperty tooltipProperty() {
        return tooltip;
    }

    public boolean isValid() {
        return valid.get();
    }

    public BooleanProperty validProperty() {
        return valid;
    }

    public boolean hasChanged() {
        return changed.get();
    }

    public BooleanProperty changedProperty() {
        return changed;
    }

    public boolean isRequired() {
        return required.get();
    }

    public BooleanProperty requiredProperty() {
        return required;
    }

    public boolean isEditable() {
        return editable.get();
    }

    public BooleanProperty editableProperty() {
        return editable;
    }

    public boolean isI18N() {
        return translationService != null;
    }

    public SimpleControl<F> getRenderer() {
        return renderer;
    }

    public List<String> getErrorMessages() {
        return errorMessages.get();
    }

    public ListProperty<String> errorMessagesProperty() {
        return errorMessages;
    }

    /**
     * Registers an event handler to this field. The handler is called when the
     * field receives an {@code Event} of the specified type during the bubbling
     * phase of event delivery.
     *
     * @param eventType    the type of the events to receive by the handler
     * @param eventHandler the handler to register
     *
     * @throws NullPointerException if either event type or handler are {@code null}.
     */
    public Field addEventHandler(EventType<FieldEvent> eventType, EventHandler<? super FieldEvent> eventHandler) {
        if (eventType == null) {
            throw new NullPointerException("Argument eventType must not be null");
        }
        if (eventHandler == null) {
            throw new NullPointerException("Argument eventHandler must not be null");
        }

        this.eventHandlers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(eventHandler);

        return this;
    }

    /**
     * Unregisters a previously registered event handler from this field. One
     * handler might have been registered for different event types, so the
     * caller needs to specify the particular event type from which to
     * unregister the handler.
     *
     * @param eventType    the event type from which to unregister
     * @param eventHandler the handler to unregister
     *
     * @throws NullPointerException if either event type or handler are {@code null}.
     */
    public Field removeEventHandler(EventType<FieldEvent> eventType, EventHandler<? super FieldEvent> eventHandler) {
        if (eventType == null) {
            throw new NullPointerException("Argument eventType must not be null");
        }
        if (eventHandler == null) {
            throw new NullPointerException("Argument eventHandler must not be null");
        }

        List<EventHandler<? super FieldEvent>> list = this.eventHandlers.get(eventType);
        if (list != null) {
            list.remove(eventHandler);
        }

        return this;
    }

    protected void fireEvent(FieldEvent event) {
        List<EventHandler<? super FieldEvent>> list = this.eventHandlers.get(event.getEventType());
        if (list == null) {
            return;
        }
        for (EventHandler<? super FieldEvent> eventHandler : list) {
            if (!event.isConsumed()) {
                eventHandler.handle(event);
            }
        }
    }
  
    public Node getLabelDescription() {
        return labelDescription;
    }

    public Node getValueDescription() {
        return valueDescription;
    }
}
