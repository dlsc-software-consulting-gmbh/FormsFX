package com.dlsc.formsfx.model.util;

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

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static junit.framework.TestCase.fail;

/**
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class ResourceBundleServiceTest {

    private final ResourceBundle rbEN = ResourceBundle.getBundle("testbundle", new Locale("en", "UK"));
    private final ResourceBundle rbDE = ResourceBundle.getBundle("testbundle", new Locale("de", "CH"));

    @Test
    public void translateTest() {
        ResourceBundleService rbs = new ResourceBundleService(rbEN);
        Assert.assertEquals("Test Form", rbs.translate("form_title"));

        try {
            rbs.translate("non_existing");
            fail();
        } catch (MissingResourceException ignored) {}
    }

    @Test
    public void changeLocaleTest() {
        ResourceBundleService rbs = new ResourceBundleService(rbEN);
        final int[] calls = new int[] { 0 };

        Runnable r = () -> calls[0] += 1;

        rbs.addListener(r);

        rbs.changeLocale(rbDE);
        Assert.assertEquals(1, calls[0]);

        rbs.changeLocale(rbDE);
        Assert.assertEquals(1, calls[0]);

        rbs.removeListener(r);
    }

}
