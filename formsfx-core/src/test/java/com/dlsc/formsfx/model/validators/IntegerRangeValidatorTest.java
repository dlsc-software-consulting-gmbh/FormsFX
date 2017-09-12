package com.dlsc.formsfx.model.validators;

import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.fail;

/**
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class IntegerRangeValidatorTest {

    @Test
    public void betweenTest() {
        IntegerRangeValidator i = IntegerRangeValidator.between(10, 20, "test");

        Assert.assertTrue(i.validate(14).getResult());
        Assert.assertFalse(i.validate(21).getResult());
        Assert.assertTrue(i.validate(20).getResult());
        Assert.assertTrue(i.validate(10).getResult());

        try {
            IntegerRangeValidator i2 = IntegerRangeValidator.between(20, 10, "test");
            fail();
        } catch (IllegalArgumentException ignored) {}

        try {
            IntegerRangeValidator i2 = IntegerRangeValidator.between(10, 10, "test");
        } catch (IllegalArgumentException ignored) {
            fail();
        }
    }

    @Test
    public void atLeastTest() {
        IntegerRangeValidator i = IntegerRangeValidator.atLeast(10, "test");

        Assert.assertTrue(i.validate(14).getResult());
        Assert.assertFalse(i.validate(-139).getResult());
        Assert.assertTrue(i.validate(10).getResult());
        Assert.assertFalse(i.validate(9).getResult());
        Assert.assertTrue(i.validate(Integer.MAX_VALUE).getResult());
    }

    @Test
    public void upToTest() {
        IntegerRangeValidator i = IntegerRangeValidator.upTo(10, "test");

        Assert.assertFalse(i.validate(14).getResult());
        Assert.assertFalse(i.validate(21).getResult());
        Assert.assertTrue(i.validate(10).getResult());
        Assert.assertFalse(i.validate(11).getResult());
        Assert.assertTrue(i.validate(Integer.MIN_VALUE).getResult());
    }

    @Test
    public void exactlyTest() {
        IntegerRangeValidator i = IntegerRangeValidator.exactly(10, "test");

        Assert.assertFalse(i.validate(11).getResult());
        Assert.assertFalse(i.validate(9).getResult());
        Assert.assertTrue(i.validate(10).getResult());
        Assert.assertFalse(i.validate(Integer.MIN_VALUE).getResult());
    }

}
