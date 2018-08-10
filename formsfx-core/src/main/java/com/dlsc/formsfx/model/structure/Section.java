package com.dlsc.formsfx.model.structure;

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

import com.dlsc.formsfx.model.util.TranslationService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * A section is a kind of group with more options. It can have a title and can
 * be collapsed by the user. Sections represent a more semantically heavy
 * grouping of elements, compared to groups.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class Section extends Group {

    /**
     * The title acts as a description for the group. It is always visible to
     * the user and tells them how the contained elements are grouped.
     *
     * This property is translatable if a {@link TranslationService} is set on
     * the containing form.
     */
    protected final StringProperty titleKey = new SimpleStringProperty("");
    protected final StringProperty title = new SimpleStringProperty("");

    /**
     * A group can optionally be collapsed.
     */
    protected final BooleanProperty collapsed = new SimpleBooleanProperty(false);
    /**
     * Section is collapsible by default
     */
    protected final BooleanProperty collapsible = new SimpleBooleanProperty(true);

    /**
     * {@inheritDoc}
     */
    private Section(Element... elements) {
        super(elements);

        // Whenever the title's key changes, update the displayed value based
        // on the new translation.

        titleKey.addListener((observable, oldValue, newValue) -> title.setValue(translationService.translate(newValue)));
    }

    /**
     * Creates a new section containing the given elements.
     *
     * @param elements
     *              The elements to be included in the section.
     *
     * @return Returns a new {@code Section}.
     */
    public static Section of(Element... elements) {
        return new Section(elements);
    }

    /**
     * Sets the title property of the current group.
     *
     * @param newValue
     *              The new value for the title property. This can be the title
     *              itself or a key that is then used for translation.
     *
     * @see TranslationService
     *
     * @return Returns the current group to allow for chaining.
     */
    public Section title(String newValue) {
        if (isI18N()) {
            titleKey.set(newValue);
        } else {
            title.set(newValue);
        }

        return this;
    }

    /**
     * {@inheritDoc}
     */
    protected void translate(TranslationService newValue) {
        translationService = newValue;

        if (!isI18N()) {
            return;
        }

        if (titleKey.get() == null || titleKey.get().isEmpty()) {
            titleKey.setValue(title.get());
        } else {
            title.setValue(translationService.translate(titleKey.get()));
        }

        elements.stream()
            .filter(e -> e instanceof Field)
            .map(e -> (Field) e)
            .forEach(f -> f.translate(translationService));
    }

    /**
     * Changes the collapsed state on a section.
     *
     * @param newValue
     *              The new value for the collapsed state.
     */
    public Section collapse(boolean newValue) {
        collapsed.setValue(newValue);
        return this;
    }

    public BooleanProperty collapsedProperty() {
        return collapsed;
    }

    public boolean isCollapsed() {
        return collapsed.get();
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    /**
     * Changes the collapsible state on a section.
     *
     * @param newValue
     *              The new value for the collapsible state.
     */
    public Section collapsible(boolean newValue) {
        this.collapsible.set(newValue);
        return this;
    }

    public boolean isCollapsible() {
        return collapsible.get();
    }

    public BooleanProperty collapsibleProperty() {
        return collapsible;
    }
}
