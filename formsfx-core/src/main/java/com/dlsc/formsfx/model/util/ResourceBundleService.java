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
