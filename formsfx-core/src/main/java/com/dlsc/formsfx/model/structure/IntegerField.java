package com.dlsc.formsfx.model.structure;


import com.dlsc.formsfx.view.controls.SimpleIntegerControl;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * This class provides an implementation of a {@link Field} containing a
 * {@code integer} value.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class IntegerField extends DataField<IntegerProperty, Integer, IntegerField> {

    /**
     * The constructor of {@code IntegerField}.
     *
     * @param valueProperty
     *              The property that is used to store the current valid value
     *              of the field.
     * @param persistentValueProperty
     *              The property that is used to store the latest persisted
     *              value of the field.
     */
    IntegerField(SimpleIntegerProperty valueProperty, SimpleIntegerProperty persistentValueProperty) {
        super(valueProperty, persistentValueProperty);

        valueTransformer = Integer::parseInt;
        renderer = new SimpleIntegerControl();

        userInput.set(String.valueOf(value.getValue()));
    }

}
