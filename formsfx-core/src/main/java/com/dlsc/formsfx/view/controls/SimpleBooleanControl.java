package com.dlsc.formsfx.view.controls;

import com.dlsc.formsfx.model.structure.BooleanField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * This class provides the base implementation for a simple control to edit
 * boolean values.
 *
 * @author Rinesch Murugathas
 * @author Sacha Schmid
 */
public class SimpleBooleanControl extends SimpleControl<BooleanField> {

    /**
     * - fieldLabel is the container that displays the label property of the
     *   field.
     * - checkBox is the editable checkbox to set user input.
     * - container holds the checkbox so that it can be styled properly.
     */
    private Label fieldLabel;
    private CheckBox checkBox;
    private VBox container;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeParts() {
        super.initializeParts();

        getStyleClass().add("simple-boolean-control");

        fieldLabel = new Label(field.labelProperty().getValue());
        checkBox = new CheckBox();
        container = new VBox();
        checkBox.setSelected(field.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutParts() {
        super.layoutParts();

        container.getChildren().add(checkBox);

        add(fieldLabel, 0,0,2,1);
        add(container, 2, 0, field.getSpan() - 2,1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupBindings() {
        super.setupBindings();

        checkBox.disableProperty().bind(field.editableProperty().not());
        fieldLabel.textProperty().bind(field.labelProperty());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupValueChangedListeners() {
        super.setupValueChangedListeners();
        field.userInputProperty().addListener((observable, oldValue, newValue) -> checkBox.setSelected(Boolean.parseBoolean(field.getUserInput())));

        field.errorMessagesProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(checkBox));
        field.tooltipProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(checkBox));

        checkBox.focusedProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(checkBox));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupEventHandlers() {
        setOnMouseEntered(event -> toggleTooltip(checkBox));
        setOnMouseExited(event -> toggleTooltip(checkBox));

        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> field.userInputProperty().setValue(String.valueOf(newValue)));
    }

}
