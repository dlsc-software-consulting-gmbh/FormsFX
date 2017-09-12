package com.dlsc.formsfx.model.validators;

import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.fail;

/**
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class StringLengthValidatorTest {

    @Test
    public void betweenTest() {
        StringLengthValidator s = StringLengthValidator.between(10, 20, "test");

        Assert.assertTrue(s.validate("abcdefghijklmno").getResult());
        Assert.assertFalse(s.validate("abcde").getResult());
        Assert.assertTrue(s.validate("                ").getResult());
        Assert.assertTrue(s.validate("æ¢¢äöä1ö3ä±æ#¢æ±“{").getResult());

        try {
            StringLengthValidator s2 = StringLengthValidator.between(-10, 2, "test");
            fail();
        } catch (IllegalArgumentException ignored) {}

        try {
            StringLengthValidator s3 = StringLengthValidator.between(0, 0, "test");
        } catch (IllegalArgumentException e) {
            fail();
        }

        try {
            StringLengthValidator s4 = StringLengthValidator.between(10, 1, "test");
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void atLeastTest() {
        StringLengthValidator s = StringLengthValidator.atLeast(5, "test");

        Assert.assertTrue(s.validate("gosjrgohgsr").getResult());
        Assert.assertFalse(s.validate(" ").getResult());
        Assert.assertFalse(s.validate("ae").getResult());
        Assert.assertTrue(s.validate("¶æ¢¶ππ§±#").getResult());

        try {
            StringLengthValidator s2 = StringLengthValidator.atLeast(-10, "test");
            fail();
        } catch (IllegalArgumentException ignored) {}

        try {
            StringLengthValidator s3 = StringLengthValidator.atLeast(0, "test");
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void upToTest() {
        StringLengthValidator s = StringLengthValidator.upTo(5, "test");

        Assert.assertFalse(s.validate("gosjrgohgsr").getResult());
        Assert.assertTrue(s.validate(" ").getResult());
        Assert.assertTrue(s.validate("ae").getResult());
        Assert.assertFalse(s.validate("¶æ¢¶ππ§±#").getResult());
    }

    @Test
    public void exactlyTest() {
        StringLengthValidator s = StringLengthValidator.exactly(3, "test");

        Assert.assertFalse(s.validate("gfyf").getResult());
        Assert.assertTrue(s.validate("   ").getResult());
        Assert.assertTrue(s.validate("aee").getResult());
        Assert.assertFalse(s.validate("ee").getResult());
    }

}
