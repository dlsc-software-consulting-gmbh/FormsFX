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

import com.dlsc.formsfx.model.structure.MultiSelectionField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides the base implementation for a simple control to edit
 * checkbox values.
 *
 * @author Rinesch Murugathas
 * @author Sacha Schmid
 */
public class SimpleCheckBoxControl<V> extends SimpleControl<MultiSelectionField<V>> {

    /**
     * - The fieldLabel is the container that displays the label property of
     *   the field.
     * - The checkboxes list contains all the checkboxes to display.
     * - The box is a VBox holding all box.
     */
    private Label fieldLabel;
    private final List<CheckBox> checkboxes = new ArrayList<>();
    private VBox box;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeParts() {
        super.initializeParts();

        getStyleClass().add("simple-checkbox-control");

        fieldLabel = new Label(field.labelProperty().getValue());
        box = new VBox();

        createCheckboxes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutParts() {
        super.layoutParts();

        int columns = field.getSpan();

        box.setSpacing(5);

        add(fieldLabel, 0,0,2,1);
        add(box, 2, 0, columns - 2,1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupBindings() {
        super.setupBindings();

        fieldLabel.textProperty().bind(field.labelProperty());
        setupCheckboxBindings();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupValueChangedListeners() {
        super.setupValueChangedListeners();

        field.itemsProperty().addListener((observable, oldValue, newValue) -> {
            createCheckboxes();
            setupCheckboxBindings();
            setupCheckboxEventHandlers();
        });

        field.selectionProperty().addListener((observable, oldValue, newValue) -> {
            for (int i = 0; i < checkboxes.size(); i++) {
                checkboxes.get(i).setSelected(field.getSelection().contains(field.getItems().get(i)));
            }
        });

        field.errorMessagesProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(box, checkboxes.get(checkboxes.size() - 1)));
        field.tooltipProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(box, checkboxes.get(checkboxes.size() - 1)));

        for (int i = 0; i < checkboxes.size(); i++) {
            checkboxes.get(i).focusedProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(box, checkboxes.get(checkboxes.size() - 1)));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupEventHandlers() {
        box.setOnMouseEntered(event -> toggleTooltip(box, checkboxes.get(checkboxes.size() - 1)));
        box.setOnMouseExited(event -> toggleTooltip(box, checkboxes.get(checkboxes.size() - 1)));
        setupCheckboxEventHandlers();
    }

    /**
     * This method creates box and adds them to checkboxes and is
     * used when the itemsProperty on the field changes.
     */
    private void createCheckboxes() {
        box.getChildren().clear();
        checkboxes.clear();

        for (int i = 0; i < field.getItems().size(); i++) {
            CheckBox cb = new CheckBox();

            cb.setText(field.getItems().get(i).toString());
            cb.setSelected(field.getSelection().contains(field.getItems().get(i)));

            checkboxes.add(cb);
        }

        box.getChildren().addAll(checkboxes);
    }

    /**
     * Sets up bindings for all checkboxes.
     */
    private void setupCheckboxBindings() {
        for (CheckBox checkbox : checkboxes) {
            checkbox.disableProperty().bind(field.editableProperty().not());
        }
    }

    /**
     * Sets up event handlers for all checkboxes.
     */
    private void setupCheckboxEventHandlers() {
        for (int i = 0; i < checkboxes.size(); i++) {
            final int j = i;

            checkboxes.get(i).setOnAction(event -> {
                if (checkboxes.get(j).isSelected()) {
                    field.select(j);
                } else {
                    field.deselect(j);
                }
            });
        }
    }

}
