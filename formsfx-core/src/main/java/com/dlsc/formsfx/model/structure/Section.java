package com.dlsc.formsfx.model.structure;

import com.dlsc.formsfx.model.util.TranslationService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * A section is a kind of group with more options. It can have a title and can
 * be collapsed by the user. Sections represent a more semantically heavy
 * grouping of fields, compared to groups.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class Section extends Group {

    /**
     * The title acts as a description for the group. It is always visible to
     * the user and tells them how the contained fields are grouped.
     *
     * This property is translatable if a {@link TranslationService} is set on
     * the containing form.
     */
    private final StringProperty titleKey = new SimpleStringProperty("");
    private final StringProperty title = new SimpleStringProperty("");

    /**
     * A group can optionally be collapsed.
     */
    private final BooleanProperty collapsed = new SimpleBooleanProperty(false);

    /**
     * {@inheritDoc}
     */
    private Section(Field... fields) {
        super(fields);

        // Whenever the title's key changes, update the displayed value based
        // on the new translation.

        titleKey.addListener((observable, oldValue, newValue) -> title.setValue(translationService.translate(newValue)));
    }

    /**
     * Creates a new section containing the given fields.
     *
     * @param fields
     *              The fields to be included in the section.
     *
     * @return Returns a new {@code Section}.
     */
    public static Section of(Field... fields) {
        return new Section(fields);
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
    public Group title(String newValue) {
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
    void translate(TranslationService newValue) {
        translationService = newValue;

        if (!isI18N()) {
            return;
        }

        if (titleKey.get() == null || titleKey.get().isEmpty()) {
            titleKey.setValue(title.get());
        } else {
            title.setValue(translationService.translate(titleKey.get()));
        }

        fields.forEach(f -> f.translate(translationService));
    }

    /**
     * Changes the collapsed state on a section.
     *
     * @param newValue
     *              The new value for the collapsed state.
     */
    public void collapse(boolean newValue) {
        collapsed.setValue(newValue);
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

}
