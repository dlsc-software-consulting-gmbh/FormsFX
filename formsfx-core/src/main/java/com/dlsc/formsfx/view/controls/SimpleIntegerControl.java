package com.dlsc.formsfx.view.controls;

import com.dlsc.formsfx.model.structure.IntegerField;
import javafx.scene.control.SpinnerValueFactory;

/**
 * This class provides a specific implementation to edit integer values.
 *
 * @author Rinesch Murugathas
 * @author Sacha Schmid
 */
public class SimpleIntegerControl extends SimpleNumberControl<IntegerField, Integer> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeParts() {
        super.initializeParts();

        getStyleClass().addAll("simple-integer-control");
        editableSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, field.getValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupValueChangedListeners() {
        super.setupValueChangedListeners();

        field.errorMessagesProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(editableSpinner));
        field.tooltipProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(editableSpinner));
    }

}
