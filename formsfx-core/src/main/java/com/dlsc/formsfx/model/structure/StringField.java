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

import com.dlsc.formsfx.view.controls.SimpleTextControl;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class provides an implementation of a {@link Field} containing a
 * {@code string} value.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class StringField extends DataField<StringProperty, String, StringField> {

    protected final BooleanProperty multiline = new SimpleBooleanProperty(false);

    /**
     * The constructor of {@code StringField}.
     *
     * @param valueProperty
     *              The property that is used to store the current valid value
     *              of the field.
     * @param persistentValueProperty
     *              The property that is used to store the latest persisted
     *              value of the field.
     */
    protected StringField(SimpleStringProperty valueProperty, SimpleStringProperty persistentValueProperty) {
        super(valueProperty, persistentValueProperty);

        stringConverter = new AbstractStringConverter<String>() {
            @Override
            public String fromString(String string) {
                return string;
            }
        };
        rendererSupplier = () -> new SimpleTextControl();

        userInput.set(stringConverter.toString(value.getValue()));
    }

    /**
     * Sets whether the field is considered to be multiline or not.
     *
     * @param newValue
     *              The new value for the multiline property.
     *
     * @return Returns the current field to allow for chaining.
     */
    public StringField multiline(boolean newValue) {
        multiline.setValue(newValue);
        return this;
    }

    public boolean isMultiline() {
        return multiline.get();
    }

    public BooleanProperty multilineProperty() {
        return multiline;
    }

}
