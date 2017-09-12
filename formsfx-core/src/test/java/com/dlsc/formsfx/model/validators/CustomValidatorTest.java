package com.dlsc.formsfx.model.validators;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class CustomValidatorTest {

    @Test
    public void customTest() {
        CustomValidator<String> c = CustomValidator.forPredicate(s -> s.length() % 2 == 0, "test");

        Assert.assertTrue(c.validate("abcd").getResult());
        Assert.assertFalse(c.validate("abc").getResult());
        Assert.assertTrue(c.validate("").getResult());
    }

}
