package com.dlsc.formsfx.model.structure;

import com.dlsc.formsfx.view.controls.SimpleDoubleControl;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * This class provides an implementation of a {@link Field} containing a
 * {@code double} value.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class DoubleField extends DataField<DoubleProperty, Double, DoubleField> {

    /**
     * The constructor of {@code DoubleField}.
     *
     * @param valueProperty
     *              The property that is used to store the current valid value
     *              of the field.
     * @param persistentValueProperty
     *              The property that is used to store the latest persisted
     *              value of the field.
     */
    DoubleField(SimpleDoubleProperty valueProperty, SimpleDoubleProperty persistentValueProperty) {
        super(valueProperty, persistentValueProperty);

        valueTransformer = Double::parseDouble;
        renderer = new SimpleDoubleControl();

        userInput.set(String.valueOf(value.getValue()));
    }

}
