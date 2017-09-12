package com.dlsc.formsfx.view.renderer;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Section;
import com.dlsc.formsfx.view.util.ViewMixin;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used to render a form.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class FormRenderer extends VBox implements ViewMixin {

    private Form form;
    private List<GroupRendererBase> sections = new ArrayList<>();

    /**
     * This is the constructor to pass over data.
     * @param form
     *              The form which gets rendered.
     */
    public FormRenderer(Form form) {
        this.form = form;

        init();
    }

    @Override
    public String getUserAgentStylesheet() {
        return FormRenderer.class.getResource("style.css").toExternalForm();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeParts() {
        sections = form.getGroups().stream()
                .map(s -> {
                    if (s instanceof Section) {
                        return new SectionRenderer((Section) s);
                    } else {
                        return new GroupRenderer(s);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutParts() {
        setPadding(new Insets(10));
        getChildren().addAll(sections);
    }

}
