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

import com.dlsc.formsfx.view.util.ColSpan;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.UUID;

/**
 * @author Andres Almiray
 */
public abstract class Element<E extends Element<E>> {
    /**
     * Fields can be styled using CSS through ID or class hooks.
     */
    protected final StringProperty id = new SimpleStringProperty(UUID.randomUUID().toString());
    protected final ListProperty<String> styleClass = new SimpleListProperty<>(FXCollections.observableArrayList());
    protected final IntegerProperty span = new SimpleIntegerProperty(12);

    /**
     * Sets the id property of the current field.
     *
     * @param newValue
     *              The new value for the id property.
     *
     * @return Returns the current field to allow for chaining.
     */
    public E id(String newValue) {
        id.set(newValue);
        return (E) this;
    }

    /**
     * Sets the style classes for the current field.
     *
     * @param newValue
     *              The new style classes.
     *
     * @return Returns the current field to allow for chaining.
     */
    public E styleClass(String... newValue) {
        styleClass.setAll(newValue);
        return (E) this;
    }

    /**
     * Sets the amount of columns the field takes up inside the section grid.
     *
     * @param newValue
     *              The new number of columns.
     *
     * @return Returns the current field to allow for chaining.
     */
    public E span(int newValue) {
        span.setValue(newValue);
        return (E) this;
    }

    /**
     * Sets the amount of columns the field takes up inside the section grid.
     *
     * @param newValue
     *              The new span fraction.
     *
     * @return Returns the current field to allow for chaining.
     */
    public E span(ColSpan newValue) {
        span.setValue(newValue.valueOf());
        return (E) this;
    }

    public int getSpan() {
        return span.get();
    }

    public IntegerProperty spanProperty() {
        return span;
    }

    public String getID() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public ObservableList<String> getStyleClass() {
        return styleClass.get();
    }

    public ListProperty<String> styleClassProperty() {
        return styleClass;
    }
}
