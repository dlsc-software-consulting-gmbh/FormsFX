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
