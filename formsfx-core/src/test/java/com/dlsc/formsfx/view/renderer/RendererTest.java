package com.dlsc.formsfx.view.renderer;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.Section;
import com.dlsc.formsfx.view.controls.SimpleCheckBoxControl;
import com.dlsc.formsfx.view.controls.SimpleRadioButtonControl;
import com.dlsc.formsfx.view.controls.SimpleTextControl;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class RendererTest {

    @BeforeClass
    public static void before() {
        com.sun.javafx.application.PlatformImpl.startup(() -> {});
    }

    @Test
    public void formTest() {
        Form f = Form.of(Group.of(), Section.of(), Group.of(), Group.of(), Section.of());
        FormRenderer r = new FormRenderer(f);

        Assert.assertTrue(r.getChildren().get(0) instanceof GroupRenderer);
        Assert.assertEquals(5, r.getChildren().size());
        Assert.assertFalse(r.getChildren().get(0) instanceof SectionRenderer);
        Assert.assertTrue(r.getChildren().get(1) instanceof SectionRenderer);
    }

    @Test
    public void groupTest() {
        Group g = Group.of(
                Field.ofStringType("").span(7),
                Field.ofBooleanType(false).span(8),
                Field.ofMultiSelectionType(Arrays.asList(1, 2, 3), Collections.singletonList(1)),
                Field.ofSingleSelectionType(Arrays.asList(1, 2, 3), 1).render(new SimpleRadioButtonControl<>())
        );
        GroupRenderer r = new GroupRenderer(g);

        Assert.assertTrue(r.getChildren().get(0) instanceof GridPane);
        Assert.assertTrue(((GridPane) r.getChildren().get(0)).getChildren().get(0) instanceof SimpleTextControl);
    }

    @Test
    public void sectionTest() {
        Section s = Section.of(
                Field.ofDoubleType(2.0),
                Field.ofIntegerType(1),
                Field.ofMultiSelectionType(Arrays.asList(1, 2, 3), Collections.singletonList(1)).render(new SimpleCheckBoxControl<>()),
                Field.ofSingleSelectionType(Arrays.asList(1, 2, 3), 1)
        );
        SectionRenderer r = new SectionRenderer(s);

        Assert.assertTrue(r.getChildren().get(0) instanceof TitledPane);
        Assert.assertTrue(((TitledPane) r.getChildren().get(0)).getContent() instanceof GridPane);
        Assert.assertTrue(((TitledPane) r.getChildren().get(0)).isExpanded());

        s.collapse(true);

        Assert.assertFalse(((TitledPane) r.getChildren().get(0)).isExpanded());
    }

}
