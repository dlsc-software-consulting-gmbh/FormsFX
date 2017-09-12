package com.dlsc.formsfx.model.util;

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
