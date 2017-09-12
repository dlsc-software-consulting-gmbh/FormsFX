package com.dlsc.formsfx.model.validators;

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
