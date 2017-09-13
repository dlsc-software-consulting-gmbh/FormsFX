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
