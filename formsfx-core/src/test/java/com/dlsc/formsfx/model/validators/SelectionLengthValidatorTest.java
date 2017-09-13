package com.dlsc.formsfx.model.validators;

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

import javafx.collections.FXCollections;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.fail;

/**
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class SelectionLengthValidatorTest {

    @Test
    public void betweenTest() {
        SelectionLengthValidator<Integer> s = SelectionLengthValidator.between(1, 3, "test");

        Assert.assertTrue(s.validate(FXCollections.observableArrayList(1, 2, 3)).getResult());
        Assert.assertFalse(s.validate(FXCollections.observableArrayList()).getResult());
        Assert.assertFalse(s.validate(FXCollections.observableArrayList(1, 2, 3, 4, 5)).getResult());

        try {
            SelectionLengthValidator s2 = SelectionLengthValidator.between(-10, 2, "test");
            fail();
        } catch (IllegalArgumentException ignored) {}

        try {
            SelectionLengthValidator s3 = SelectionLengthValidator.between(0, 0, "test");
        } catch (IllegalArgumentException e) {
            fail();
        }

        try {
            SelectionLengthValidator s4 = SelectionLengthValidator.between(10, 1, "test");
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void atLeastTest() {
        SelectionLengthValidator<Integer> s = SelectionLengthValidator.atLeast(2, "test");

        Assert.assertTrue(s.validate(FXCollections.observableArrayList(1, 4)).getResult());
        Assert.assertFalse(s.validate(FXCollections.observableArrayList(1)).getResult());
        Assert.assertFalse(s.validate(FXCollections.observableArrayList()).getResult());
        Assert.assertTrue(s.validate(FXCollections.observableArrayList(1, 2, 3)).getResult());

        try {
            SelectionLengthValidator s2 = SelectionLengthValidator.atLeast(-10, "test");
            fail();
        } catch (IllegalArgumentException ignored) {}

        try {
            SelectionLengthValidator s3 = SelectionLengthValidator.atLeast(0, "test");
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void upToTest() {
        SelectionLengthValidator<Integer> s = SelectionLengthValidator.upTo(2, "test");

        Assert.assertFalse(s.validate(FXCollections.observableArrayList(3, 5, 1)).getResult());
        Assert.assertTrue(s.validate(FXCollections.observableArrayList(1, 2)).getResult());
        Assert.assertTrue(s.validate(FXCollections.observableArrayList()).getResult());
        Assert.assertFalse(s.validate(FXCollections.observableArrayList(1, 2, 3, 5, 14)).getResult());
    }

    @Test
    public void exactlyTest() {
        SelectionLengthValidator<Integer> s = SelectionLengthValidator.exactly(2, "test");

        Assert.assertFalse(s.validate(FXCollections.observableArrayList(1, 2, 3)).getResult());
        Assert.assertTrue(s.validate(FXCollections.observableArrayList(1, 2)).getResult());
        Assert.assertFalse(s.validate(FXCollections.observableArrayList()).getResult());
        Assert.assertFalse(s.validate(FXCollections.observableArrayList(1)).getResult());
    }

}
