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

import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.util.ColSpan;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
@Ignore
public class FieldTest {

    @BeforeClass
    public static void beforeClass() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ex) {
            // JavaFX may only be initialized once.
        }
    }

    @Test
    public void validTest() {
        StringField s = Field.ofStringType("test");

        final int[] changes = {0};

        s.validProperty().addListener((observable, oldValue, newValue) -> changes[0] += 1);

        s.required("This field is required.").validate(StringLengthValidator.atLeast(6, "test"));

        Assert.assertFalse(s.isValid());

        s.validate(StringLengthValidator.upTo(6, "test"));

        Assert.assertEquals(2, changes[0]);
        Assert.assertTrue(s.isValid());
    }

    @Test
    public void singleSelectionTest() {
        SingleSelectionField<String> s = Field.ofSingleSelectionType(Arrays.asList("Test", "Test 1", "Test 2"), 0);

        Assert.assertEquals("Test", s.getSelection());

        s.select(2);
        Assert.assertEquals("Test 2", s.getSelection());

        s.select(4);
        Assert.assertEquals("Test 2", s.getSelection());

        s.deselect();
        Assert.assertEquals(null, s.getSelection());

        s.select(2);
        Assert.assertEquals("Test 2", s.getSelection());

        s.select(-1);
        Assert.assertEquals(null, s.getSelection());
    }

    @Test
    public void multiSelectionTest() {
        MultiSelectionField<String> s = Field.ofMultiSelectionType(Arrays.asList("Test", "Test 1", "Test 2"), Arrays.asList(0, 3));

        Assert.assertEquals(1, s.getSelection().size());

        s.select(2);
        Assert.assertEquals(2, s.getSelection().size());

        s.select(4);
        Assert.assertEquals(2, s.getSelection().size());

        s.deselect(1);
        Assert.assertEquals(2, s.getSelection().size());

        s.deselect(0);
        Assert.assertEquals(1, s.getSelection().size());
    }

    @Test
    public void multilineTest() {
        StringField s = Field.ofStringType("test").multiline(true);

        Assert.assertTrue(s.isMultiline());

        s.multiline(false);
        Assert.assertFalse(s.multilineProperty().getValue());
    }

    @Test
    public void dataBindingTest() {
        StringProperty s = new SimpleStringProperty("test");
        DoubleProperty d = new SimpleDoubleProperty(1.0);
        IntegerProperty i = new SimpleIntegerProperty(3);
        BooleanProperty b = new SimpleBooleanProperty(false);

        StringField sf = Field.ofStringType(s);
        DoubleField df = Field.ofDoubleType(d);
        IntegerField inf = Field.ofIntegerType(i);
        BooleanField bf = Field.ofBooleanType(b);

        sf.userInputProperty().setValue("test 2");
        Assert.assertEquals("test", s.getValue());
        sf.persist();
        Assert.assertEquals("test 2", s.getValue());

        df.userInputProperty().setValue("2");
        Assert.assertEquals(1.0, d.get(), 0.0001);
        df.persist();
        Assert.assertEquals(2.0, d.get(), 0.0001);

        inf.userInputProperty().setValue("5");
        Assert.assertEquals(3, i.get());
        inf.persist();
        Assert.assertEquals(5, i.get());

        bf.userInputProperty().setValue("true");
        Assert.assertEquals(false, b.get());
        bf.persist();
        Assert.assertEquals(true, b.get());

        s.setValue("test 3");
        Assert.assertEquals("test 3", sf.getValue());
        Assert.assertEquals("test 3", sf.getValue());
    }

    @Ignore
    public void selectionBindingTest() {
        ObjectProperty<String> o1 = new SimpleObjectProperty<>("hello");
        ListProperty<String> l1 = new SimpleListProperty<>(FXCollections.observableArrayList("hello", "world"));
        ListProperty<String> l2 = new SimpleListProperty<>(FXCollections.observableArrayList("hi", "there"));
        ListProperty<String> l3 = new SimpleListProperty<>(FXCollections.observableArrayList("hi"));

        SingleSelectionField<String> sif = Field.ofSingleSelectionType(l1, o1);
        MultiSelectionField<String> mf = Field.ofMultiSelectionType(l2, l3);

        Assert.assertEquals("hello", sif.getSelection());
        Assert.assertEquals(1, mf.getSelection().size());

        l1.setValue(FXCollections.observableArrayList("oh", "wonder"));

        Assert.assertEquals(null, sif.getSelection());

        mf.select(1);

        Assert.assertEquals(2, mf.getSelection().size());
        Assert.assertEquals(1, l3.size());

        mf.persist();

        Assert.assertEquals(2, l3.size());
    }

    @Ignore
    public void propertiesTest() {
        StringProperty sp = new SimpleStringProperty("test 3");

        StringField s = Field.ofStringType("test")
                .multiline(true)
                .editable(true)
                .format(String::toString)
                .styleClass("class", "thing")
                .id("element")
                .placeholder("temp")
                .label("Field")
                .required("error")
                .span(6)
                .span(ColSpan.HALF);

        Assert.assertEquals("test", s.getValue());
        Assert.assertEquals(6, s.getSpan());
        Assert.assertEquals(2, s.getStyleClass().size());
        Assert.assertEquals("Field", s.getLabel());
        Assert.assertEquals("temp", s.getPlaceholder());

        s.bind(sp);

        Assert.assertEquals("test 3", s.getValue());
    }

}
