package com.dlsc.formsfx.model.structure;

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
    protected DoubleField(SimpleDoubleProperty valueProperty, SimpleDoubleProperty persistentValueProperty) {
        super(valueProperty, persistentValueProperty);

        stringConverter = new AbstractStringConverter<Double>() {
            @Override
            public Double fromString(String string) {
                return Double.parseDouble(string);
            }
        };
        rendererSupplier = () -> new SimpleDoubleControl();

        userInput.set(stringConverter.toString(value.getValue()));
    }

}
