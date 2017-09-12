package com.dlsc.formsfx.view.renderer;

import com.dlsc.formsfx.model.structure.Group;
import javafx.geometry.Insets;

/**
 * This class renders a group for a form.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class GroupRenderer extends GroupRendererBase {

    /**
     * This is the constructor to pass over data.
     *
     * @param group
     *              The section which gets rendered.
     */
    GroupRenderer(Group group) {
        this.element = group;
        init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeParts() {
        super.initializeParts();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutParts() {
        super.layoutParts();

        getStyleClass().add("formsfx-group");

        setFocusTraversable(false);
        setPadding(new Insets(SPACING * 2));
        getChildren().add(grid);
    }

}
