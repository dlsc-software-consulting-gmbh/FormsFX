package com.dlsc.formsfx.view.controls;

import com.dlsc.formsfx.model.structure.DataField;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.StackPane;

/**
 * This class provides the base implementation for a simple control to edit
 * numerical fields.
 *
 * @author Rinesch Murugathas
 * @author Sacha Schmid
 */
public abstract class SimpleNumberControl<F extends DataField, D extends Number> extends SimpleControl<F> {

    /**
     * This StackPane is needed for achieving the readonly effect by putting
     * the {@code readOnlyLabel} over the {@code editableSpinner} on the change
     * of the {@code visibleProperty}.
     */
    private StackPane stack;

    /**
     * - The fieldLabel is the container that displays the label property of
     *   the field.
     * - The editableSpinner is a Spinner for setting numerical values.
     * - The readOnlyLabel is the label to put over editableSpinner.
     */
    private Label fieldLabel;
    Spinner<D> editableSpinner;
    private Label readOnlyLabel;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeParts() {
        super.initializeParts();

        stack = new StackPane();

        fieldLabel = new Label();
        readOnlyLabel = new Label();
        editableSpinner = new Spinner<>();
        editableSpinner.setEditable(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutParts() {
        super.layoutParts();

        readOnlyLabel.getStyleClass().add("read-only-label");
        stack.getChildren().addAll(editableSpinner, readOnlyLabel);
        stack.setAlignment(Pos.CENTER_LEFT);

        editableSpinner.setMaxWidth(Double.MAX_VALUE);

        int columns = field.getSpan();

        if (columns < 3) {
            add(fieldLabel, 0, 0, columns, 0);
            add(stack, 0, 1, columns, 1);
        } else {
            add(fieldLabel, 0, 0, 2, 1);
            add(stack, 2, 0, columns - 2, 1);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupBindings() {
        super.setupBindings();

        editableSpinner.visibleProperty().bind(field.editableProperty());
        readOnlyLabel.visibleProperty().bind(field.editableProperty().not());

        editableSpinner.getEditor().textProperty().bindBidirectional(field.userInputProperty());
        readOnlyLabel.textProperty().bind(field.userInputProperty());
        fieldLabel.textProperty().bind(field.labelProperty());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupEventHandlers() {
        editableSpinner.getEditor().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    editableSpinner.increment(1);
                    break;
                case DOWN:
                    editableSpinner.decrement(1);
                    break;
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupValueChangedListeners() {
        super.setupValueChangedListeners();
        editableSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(editableSpinner));
    }
}
