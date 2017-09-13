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
