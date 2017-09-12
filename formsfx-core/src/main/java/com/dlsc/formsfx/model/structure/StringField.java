package com.dlsc.formsfx.model.structure;

import com.dlsc.formsfx.view.controls.SimpleTextControl;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class provides an implementation of a {@link Field} containing a
 * {@code string} value.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class StringField extends DataField<StringProperty, String, StringField> {

    private final BooleanProperty multiline = new SimpleBooleanProperty(false);

    /**
     * The constructor of {@code StringField}.
     *
     * @param valueProperty
     *              The property that is used to store the current valid value
     *              of the field.
     * @param persistentValueProperty
     *              The property that is used to store the latest persisted
     *              value of the field.
     */
    StringField(SimpleStringProperty valueProperty, SimpleStringProperty persistentValueProperty) {
        super(valueProperty, persistentValueProperty);

        valueTransformer = String::valueOf;
        renderer = new SimpleTextControl();

        userInput.set(String.valueOf(value.getValue()));
    }

    /**
     * Sets whether the field is considered to be multiline or not.
     *
     * @param newValue
     *              The new value for the multiline property.
     *
     * @return Returns the current field to allow for chaining.
     */
    public StringField multiline(boolean newValue) {
        multiline.setValue(newValue);
        return this;
    }

    public boolean isMultiline() {
        return multiline.get();
    }

    public BooleanProperty multilineProperty() {
        return multiline;
    }

}
