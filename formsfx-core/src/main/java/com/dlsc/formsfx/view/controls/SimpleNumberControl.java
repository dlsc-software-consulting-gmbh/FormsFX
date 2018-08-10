package com.dlsc.formsfx.view.controls;

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

import com.dlsc.formsfx.model.structure.DataField;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
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
    protected StackPane stack;

    /**
     * - The fieldLabel is the container that displays the label property of
     *   the field.
     * - The editableSpinner is a Spinner for setting numerical values.
     * - The readOnlyLabel is the label to put over editableSpinner.
     */
    protected Label fieldLabel;
    protected Spinner<D> editableSpinner;
    protected Label readOnlyLabel;

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

        Node labelDescription = field.getLabelDescription();
        Node valueDescription = field.getValueDescription();

        int columns = field.getSpan();

        if (columns < 3) {
            int rowIndex = 0;
            add(fieldLabel, 0, rowIndex++, columns, 1);
            if (labelDescription != null) {
                GridPane.setValignment(labelDescription, VPos.TOP);
                add(labelDescription, 0, rowIndex++, columns, 1);
            }
            add(stack, 0, rowIndex++, columns, 1);
            if (valueDescription != null) {
                GridPane.setValignment(valueDescription, VPos.TOP);
                add(valueDescription, 0, rowIndex, columns, 1);
            }
        } else {
            add(fieldLabel, 0, 0, 2, 1);
            if (labelDescription != null) {
                GridPane.setValignment(labelDescription, VPos.TOP);
                add(labelDescription, 0, 1, 2, 1);
            }
            add(stack, 2, 0, columns - 2, 1);
            if (valueDescription != null) {
                GridPane.setValignment(valueDescription, VPos.TOP);
                add(valueDescription, 2, 1, columns - 2, 1);
            }
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
