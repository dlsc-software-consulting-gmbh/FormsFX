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

import com.dlsc.formsfx.view.controls.SimpleBooleanControl;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.StringConverter;

/**
 * This class provides an implementation of a {@link Field} containing a
 * {@code boolean} value.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class BooleanField extends DataField<BooleanProperty, Boolean, BooleanField> {

    /**
     * The constructor of {@code BooleanField}.
     *
     * @param valueProperty
     *              The property that is used to store the current valid value
     *              of the field.
     * @param persistentValueProperty
     *              The property that is used to store the latest persisted
     *              value of the field.
     */
    protected BooleanField(SimpleBooleanProperty valueProperty, SimpleBooleanProperty persistentValueProperty) {
        super(valueProperty, persistentValueProperty);

        stringConverter = new AbstractStringConverter<Boolean>() {
            @Override
            public Boolean fromString(String string) {
                return Boolean.parseBoolean(string);
            }
        };
        rendererSupplier = () -> new SimpleBooleanControl();

        userInput.set(stringConverter.toString(value.getValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean validateRequired(String newValue) {
        return !isRequired() || (isRequired() && newValue.equals("true"));
    }

}
