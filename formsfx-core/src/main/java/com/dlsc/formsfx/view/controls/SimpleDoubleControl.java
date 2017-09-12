package com.dlsc.formsfx.view.controls;

import com.dlsc.formsfx.model.structure.DoubleField;
import javafx.scene.control.SpinnerValueFactory;

/**
 * This class provides a specific implementation to edit double values.
 *
 * @author Rinesch Murugathas
 * @author Sacha Schmid
 */
public class SimpleDoubleControl extends SimpleNumberControl<DoubleField, Double> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeParts() {
        super.initializeParts();

        getStyleClass().add("simple-double-control");
        editableSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-Double.MAX_VALUE, Double.MAX_VALUE, field.getValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupValueChangedListeners() {
        super.setupValueChangedListeners();

        field.tooltipProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(editableSpinner));
        field.errorMessagesProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(editableSpinner));
    }
}
