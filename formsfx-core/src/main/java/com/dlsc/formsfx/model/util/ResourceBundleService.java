package com.dlsc.formsfx.model.util;

import java.util.ResourceBundle;

/**
 * The ResourceBundleService is a concrete implementation of a
 * {@link TranslationService} and uses ResourceBundles to perform translations.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class ResourceBundleService extends TranslationService {

    private ResourceBundle rb;

    public ResourceBundleService(ResourceBundle rb) {
        this.rb = rb;
    }

    /**
     * Change the resource bundle to use for this service. Notifies all
     * listeners of the locale change.
     *
     * @param newValue
     *              The new resource bundle to use for translations.
     */
    public void changeLocale(ResourceBundle newValue) {
        if (newValue.equals(rb)) {
            return;
        }

        rb = newValue;
        notifyListeners();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String translate(String key) {
        return rb.getString(key);
    }

}
