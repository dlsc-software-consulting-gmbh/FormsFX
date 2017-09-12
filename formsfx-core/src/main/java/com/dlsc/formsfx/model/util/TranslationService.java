package com.dlsc.formsfx.model.util;

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
