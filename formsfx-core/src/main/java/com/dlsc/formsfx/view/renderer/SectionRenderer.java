package com.dlsc.formsfx.view.renderer;

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
    protected SectionRenderer(Section section) {
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

        getStyleClass().add("formsfx-section");

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
        titledPane.collapsibleProperty().bind(element.collapsibleProperty());
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
