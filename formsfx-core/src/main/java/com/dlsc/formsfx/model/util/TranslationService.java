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

import java.util.ArrayList;
import java.util.List;

/**
 * A general purpose translation service that is used to translate values into
 * multiple locales based on keys. A concrete sample implementation is provided
 * in the {@link ResourceBundleService}.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public abstract class TranslationService {

    private List<Runnable> listeners = new ArrayList<>();

    /**
     * Looks up a key in the translation service and returns the translate string.
     *
     * @param key
     *              The key to use for the lookup.
     *
     * @return The translated string.
     */
    public abstract String translate(String key);

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    public void removeListener(Runnable listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all listeners of a locale change. Concrete implementations must
     * call this method after every locale change.
     */
    protected void notifyListeners() {
        listeners.forEach(Runnable::run);
    }

}
