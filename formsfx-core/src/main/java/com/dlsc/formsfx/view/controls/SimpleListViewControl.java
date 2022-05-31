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
import javafx.collections.ListChangeListener;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.GridPane;

/**
 * This class provides the base implementation for a simple control to edit
 * listview values.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class SimpleListViewControl<V> extends SimpleControl<MultiSelectionField<V>> {

    /**
     * - The fieldLabel is the container that displays the label property of
     *   the field.
     * - The listView is the container that displays list values.
     */
    protected Label fieldLabel;
    protected ListView<V> listView = new ListView<>();

    /**
     * The flag used for setting the selection properly.
     */
    protected boolean preventUpdate;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeParts() {
        super.initializeParts();

        getStyleClass().add("simple-listview-control");

        fieldLabel = new Label(field.labelProperty().getValue());

        listView.setItems(field.getItems());
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        for (int i = 0; i < field.getItems().size(); i++) {
            if (field.getSelection().contains(field.getItems().get(i))) {
                listView.getSelectionModel().select(i);
            } else {
                listView.getSelectionModel().clearSelection(i);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutParts() {
        super.layoutParts();

        int columns = field.getSpan();

        listView.setPrefHeight(200);

        Node labelDescription = field.getLabelDescription();
        Node valueDescription = field.getValueDescription();

        add(fieldLabel, 0, 0, 2, 1);
        if (labelDescription != null) {
            GridPane.setValignment(labelDescription, VPos.TOP);
            add(labelDescription, 0, 1, 2, 1);
        }
        add(listView, 2, 0, columns - 2, 1);
        if (valueDescription != null) {
            GridPane.setValignment(valueDescription, VPos.TOP);
            add(valueDescription, 2, 1, columns - 2, 1);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupBindings() {
        super.setupBindings();

        fieldLabel.textProperty().bind(field.labelProperty());
        listView.disableProperty().bind(field.editableProperty().not());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupValueChangedListeners() {
        super.setupValueChangedListeners();

        field.itemsProperty().addListener((observable, oldValue, newValue) -> listView.setItems(field.getItems()));

        field.selectionProperty().addListener((observable, oldValue, newValue) -> {
            if (preventUpdate) {
                return;
            }

            preventUpdate = true;

            for (int i = 0; i < field.getItems().size(); i++) {
                if (field.getSelection().contains(field.getItems().get(i))) {
                    listView.getSelectionModel().select(i);
                } else {
                    listView.getSelectionModel().clearSelection(i);
                }
            }

            preventUpdate = false;
        });

        field.errorMessagesProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(listView));
        field.tooltipProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(listView));
        listView.focusedProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(listView));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupEventHandlers() {
        listView.setOnMouseEntered(event -> toggleTooltip(listView));
        listView.setOnMouseExited(event -> toggleTooltip(listView));

        listView.getSelectionModel().getSelectedIndices().addListener((ListChangeListener<Integer>) c -> {
            if (preventUpdate) {
                return;
            }

            preventUpdate = true;

            for (int i = 0; i < listView.getItems().size(); i++) {
                if (listView.getSelectionModel().getSelectedIndices().contains(i)) {
                    field.select(i);
                } else {
                    field.deselect(i);
                }
            }

            preventUpdate = false;
        });
    }

}
