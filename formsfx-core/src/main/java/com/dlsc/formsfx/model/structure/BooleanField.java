package com.dlsc.formsfx.model.structure;

import com.dlsc.formsfx.view.controls.SimpleBooleanControl;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * This class provides an implementation of a {@link Field} containing a
 * {@code boolean} value.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class BooleanField extends DataField<BooleanProperty, Boolean, BooleanField> {

    /**
     * The constructor of {@code BooleanField}.
     *
     * @param valueProperty
     *              The property that is used to store the current valid value
     *              of the field.
     * @param persistentValueProperty
     *              The property that is used to store the latest persisted
     *              value of the field.
     */
    BooleanField(SimpleBooleanProperty valueProperty, SimpleBooleanProperty persistentValueProperty) {
        super(valueProperty, persistentValueProperty);

        valueTransformer = Boolean::parseBoolean;
        renderer = new SimpleBooleanControl();

        userInput.set(String.valueOf(value.getValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean validateRequired(String newValue) {
        return !isRequired() || (isRequired() && newValue.equals("true"));
    }

}
