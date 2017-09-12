package com.dlsc.formsfx.view.renderer;

import com.dlsc.formsfx.model.structure.Section;
import javafx.scene.control.TitledPane;

/**
 * This class renders a section for a form.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class SectionRenderer extends GroupRendererBase<Section> {

    private TitledPane titledPane;

    /**
     * This is the constructor to pass over data.
     *
     * @param section
     *              The section which gets rendered.
     */
    SectionRenderer(Section section) {
        this.element = section;
        init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeParts() {
        super.initializeParts();
        titledPane = new TitledPane();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutParts() {
        super.layoutParts();
        titledPane.setContent(grid);
        getChildren().add(titledPane);

        titledPane.setFocusTraversable(false);
        titledPane.setExpanded(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupValueChangedListeners() {
        titledPane.expandedProperty().addListener((observable, oldValue, newValue) -> element.collapsedProperty().setValue(!newValue));
        element.collapsedProperty().addListener((observable, oldValue, newValue) -> titledPane.expandedProperty().setValue(!newValue));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupBindings() {
        titledPane.textProperty().bind(element.titleProperty());
    }

}
