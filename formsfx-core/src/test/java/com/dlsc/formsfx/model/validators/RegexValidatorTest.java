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

import java.util.regex.PatternSyntaxException;

import static junit.framework.TestCase.fail;

/**
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class RegexValidatorTest {

    @Test
    public void regexTest() {
        RegexValidator r = RegexValidator.forPattern("[a-z]*", "test");

        Assert.assertTrue(r.validate("abc").getResult());
        Assert.assertTrue(r.validate("aeihafpiaheypfhapfhpa").getResult());
        Assert.assertFalse(r.validate("Ajj").getResult());
        Assert.assertTrue(r.validate("").getResult());
        Assert.assertFalse(r.validate("hlhlhL").getResult());

        try {
            RegexValidator r1 = RegexValidator.forPattern("a[aa[", "test");
            fail();
        } catch (PatternSyntaxException ignored) {}
    }

    @Test
    public void emailTest() {
        RegexValidator r = RegexValidator.forEmail("test");

        Assert.assertTrue(r.validate("test@test.com").getResult());
        Assert.assertFalse(r.validate("test@test").getResult());
        Assert.assertFalse(r.validate("test.com@test@").getResult());
        Assert.assertTrue(r.validate("test+ea@test.com").getResult());
    }

    @Test
    public void urlTest() {
        RegexValidator r = RegexValidator.forURL("test");

        Assert.assertTrue(r.validate("http://test.com").getResult());
        Assert.assertFalse(r.validate("http:/test.com").getResult());
        Assert.assertTrue(r.validate("www.test.com").getResult());
        Assert.assertTrue(r.validate("https://www.test.com").getResult());
    }

    @Test
    public void alphaNumericTest() {
        RegexValidator r= RegexValidator.forAlphaNumeric("test");

        Assert.assertTrue(r.validate("afaehofh3r1ohr1o3hro1h3A13OIHho").getResult());
        Assert.assertFalse(r.validate("aefih 391ur fj ").getResult());
        Assert.assertFalse(r.validate("¢æ±#").getResult());
    }

}
