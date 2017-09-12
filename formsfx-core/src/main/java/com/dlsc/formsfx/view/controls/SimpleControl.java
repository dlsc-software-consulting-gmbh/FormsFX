package com.dlsc.formsfx.view.controls;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.view.util.ViewMixin;
import javafx.collections.ListChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 * This class provides a base for general purpose FormsFX controls.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public abstract class SimpleControl<F extends Field> extends GridPane implements ViewMixin {

    /**
     * This is the Field that is used for binding and update styling changes.
     */
    F field;

    /**
     * Tooltip to hold the error message.
     */
    Tooltip tooltip;

    /**
     * Pseudo classes for state changes.
     */
    protected static final PseudoClass REQUIRED_CLASS = PseudoClass.getPseudoClass("required");
    protected static final PseudoClass INVALID_CLASS = PseudoClass.getPseudoClass("invalid");
    protected static final PseudoClass CHANGED_CLASS = PseudoClass.getPseudoClass("changed");
    protected static final PseudoClass DISABLED_CLASS = PseudoClass.getPseudoClass("disabled");

    public void setField(F field) {
        if (this.field != null) {
            throw new IllegalStateException("Cannot change a control's field once set.");
        }

        this.field = field;
        init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeParts() {
        getStyleClass().add("simple-control");

        tooltip = new Tooltip();
        tooltip.getStyleClass().add("simple-tooltip");

        getStyleClass().addAll(field.getStyleClass());

        updateStyle(INVALID_CLASS, !field.isValid());
        updateStyle(REQUIRED_CLASS, field.isRequired());
        updateStyle(CHANGED_CLASS, field.hasChanged());
        updateStyle(DISABLED_CLASS, !field.isEditable());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutParts() {
        setAlignment(Pos.CENTER_LEFT);

        int columns = field.getSpan();

        for (int i = 0; i < columns; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / columns);
            getColumnConstraints().add(colConst);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setupBindings() {
        idProperty().bind(field.idProperty());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupValueChangedListeners() {
        field.validProperty().addListener((observable, oldValue, newValue) -> updateStyle(INVALID_CLASS, !newValue));
        field.requiredProperty().addListener((observable, oldValue, newValue) -> updateStyle(REQUIRED_CLASS, newValue));
        field.changedProperty().addListener((observable, oldValue, newValue) -> updateStyle(CHANGED_CLASS, newValue));
        field.editableProperty().addListener((observable, oldValue, newValue) -> updateStyle(DISABLED_CLASS, !newValue));

        field.getStyleClass().addListener((ListChangeListener<String>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    getStyleClass().removeAll(c.getRemoved());
                }

                if (c.wasAdded()) {
                    getStyleClass().addAll(c.getAddedSubList());
                }
            }
        });
    }

    /**
     * Sets the error message as tooltip for the matching control and shows
     * them below the same control.
     *
     * @param reference
     *          The control which gets the tooltip.
     */
    protected void toggleTooltip(Node reference) {
        this.toggleTooltip(reference, (Control) reference);
    }

    /**
     * Sets the error message as tooltip for the matching control.
     *
     * @param below
     *          The control needed for positioning the tooltip.
     * @param reference
     *          The control which gets the tooltip.
     */
    protected void toggleTooltip(Node reference, Control below) {
        String fieldTooltip = field.getTooltip();

        if ((reference.isFocused() || reference.isHover()) && (!fieldTooltip.equals("") || field.getErrorMessages().size() > 0)) {
            tooltip.setText((!fieldTooltip.equals("") ? fieldTooltip + "\n" : "") + String.join("\n", field.getErrorMessages()));

            if (tooltip.isShowing()) {
                return;
            }

            Point2D p = below.localToScene(0.0, 0.0);

            tooltip.show(
                    getScene().getWindow(),
                    p.getX() + getScene().getX() + getScene().getWindow().getX(),
                    p.getY() + getScene().getY() + getScene().getWindow().getY() + below.getHeight() + 5
            );
        } else {
            tooltip.hide();
        }
    }

    /**
     * Sets the css style for the defined properties.
     *
     * @param pseudo
     *              The CSS pseudo class to toggle.
     * @param newValue
     *              Determines whether the CSS class should be applied.
     */
    protected void updateStyle(PseudoClass pseudo, boolean newValue) {
        pseudoClassStateChanged(pseudo, newValue);
    }

}
