package com.dlsc.formsfx.model.structure;

/*-
 * ========================LICENSE_START=================================
 * FormsFX
 * %%
 * Copyright (C) 2017 - 2018 DLSC Software & Consulting
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

import com.dlsc.formsfx.view.controls.SimpleDateControl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.converter.LocalDateStringConverter;

import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * This class provides an implementation of a {@link Field} containing a {@code LocalDate} value.
 *
 * @author Tomasz Krzemiński
 */
public class DateField extends DataField<ObjectProperty<LocalDate>, LocalDate, DateField> {
    /**
     * Internal constructor for the {@code DataField} class. To create new
     * elements, see the static factory methods in {@code Field}.
     *
     * @param valueProperty           The property that is used to store the current valid value
     *                                of the field.
     * @param persistentValueProperty The property that is used to store the latest persisted
     * @see Field ::ofStringType
     * @see Field ::ofIntegerType
     * @see Field ::ofDoubleType
     * @see Field ::ofBooleanType
     */
    public DateField(ObjectProperty<LocalDate> valueProperty, ObjectProperty<LocalDate> persistentValueProperty) {
        super(valueProperty, persistentValueProperty);

        Chronology chronology = Chronology.ofLocale(Locale.getDefault(Locale.Category.FORMAT));
        stringConverter = new LocalDateStringConverter(FormatStyle.SHORT, null, chronology);
        rendererSupplier = () -> new SimpleDateControl();
        userInput.setValue(null);
        userInput.setValue(stringConverter.toString((LocalDate) persistentValue.getValue()));
    }

    @Override
    public DateField bind(ObjectProperty<LocalDate> binding) {
        return super.bind(binding);

    }
}
