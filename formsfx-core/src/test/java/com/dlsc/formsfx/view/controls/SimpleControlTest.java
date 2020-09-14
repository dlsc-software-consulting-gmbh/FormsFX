package com.dlsc.formsfx.view.controls;

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

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.MultiSelectionField;
import com.dlsc.formsfx.model.structure.SingleSelectionField;
import com.dlsc.formsfx.model.structure.StringField;
import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
@Ignore
public class SimpleControlTest {

    @BeforeClass
    public static void before() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ex) {
            // JavaFX may only be initialized once.
        }
    }

    @Test
    public void itemsTest() {
        MultiSelectionField<Integer> mf = Field.ofMultiSelectionType(Arrays.asList(1, 2, 3), Arrays.asList(1, 2));
        SingleSelectionField<Integer> sf = Field.ofSingleSelectionType(Arrays.asList(1, 2, 3), 1);

        SimpleCheckBoxControl<Integer> cb = new SimpleCheckBoxControl<>();
        cb.setField(mf);

        SimpleListViewControl<Integer> lv = new SimpleListViewControl<>();
        lv.setField(mf);

        SimpleRadioButtonControl<Integer> rb = new SimpleRadioButtonControl<>();
        rb.setField(sf);

        SimpleComboBoxControl<Integer> cmb = new SimpleComboBoxControl<>();
        cmb.setField(sf);

        Assert.assertEquals(3, ((VBox) cb.getChildren().get(1)).getChildren().size());
        Assert.assertTrue(((CheckBox) ((VBox) cb.getChildren().get(1)).getChildren().get(1)).isSelected());

        Assert.assertEquals(3, ((ListView) lv.getChildren().get(1)).getItems().size());
        Assert.assertTrue(((ListView) lv.getChildren().get(1)).getSelectionModel().isSelected(1));

        Assert.assertEquals(3, ((VBox) rb.getChildren().get(1)).getChildren().size());
        Assert.assertTrue(((RadioButton) ((VBox) rb.getChildren().get(1)).getChildren().get(1)).isSelected());

        Assert.assertEquals(3, ((ComboBox) ((StackPane) cmb.getChildren().get(1)).getChildren().get(0)).getItems().size());
        Assert.assertTrue(((ComboBox) ((StackPane) cmb.getChildren().get(1)).getChildren().get(0)).getSelectionModel().isSelected(1));

        mf.items(Arrays.asList(1, 2, 3, 4, 5), Arrays.asList(0, 3));
        sf.items(Arrays.asList(1, 2, 3, 4, 5), 3);

        Assert.assertEquals(5, ((VBox) cb.getChildren().get(1)).getChildren().size());
        Assert.assertTrue(((CheckBox) ((VBox) cb.getChildren().get(1)).getChildren().get(0)).isSelected());

        Assert.assertEquals(5, ((ListView) lv.getChildren().get(1)).getItems().size());
        Assert.assertTrue(((ListView) lv.getChildren().get(1)).getSelectionModel().isSelected(0));

        Assert.assertEquals(5, ((VBox) rb.getChildren().get(1)).getChildren().size());
        Assert.assertTrue(((RadioButton) ((VBox) rb.getChildren().get(1)).getChildren().get(3)).isSelected());

        Assert.assertEquals(5, ((ComboBox) ((StackPane) cmb.getChildren().get(1)).getChildren().get(0)).getItems().size());
        Assert.assertTrue(((ComboBox) ((StackPane) cmb.getChildren().get(1)).getChildren().get(0)).getSelectionModel().isSelected(3));
    }

    @Test
    public void styleTest() {
        StringField s = Field.ofStringType("test").styleClass("test");
        SimpleTextControl t = new SimpleTextControl();
        t.setField(s);

        Assert.assertEquals(3, t.getStyleClass().size());

        s.styleClass("hello", "world");
        Assert.assertEquals(4, t.getStyleClass().size());

        s.styleClass("hi", "world");
        Assert.assertEquals(4, t.getStyleClass().size());
        Assert.assertEquals("world", t.getStyleClass().get(3));
    }

}
