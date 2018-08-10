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

import com.dlsc.formsfx.model.util.ResourceBundleService;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class FormTest {

    private ResourceBundleService service;
    private ResourceBundle rbDE = ResourceBundle.getBundle("testbundle", new Locale("de", "CH"));
    private ResourceBundle rbEN = ResourceBundle.getBundle("testbundle", new Locale("en", "UK"));

    @Before
    public void beforeSuite() {
        service = new ResourceBundleService(rbEN);
    }

    @Test
    public void generateFormTest() {
        Form f = Form.of(
                Group.of(
                        Field.ofDoubleType(2.0)
                ),
                Section.of(
                        Field.ofStringType("test"),
                        Field.ofBooleanType(false)
                ),
                Section.of(
                        Field.ofIntegerType(1),
                        Field.ofDoubleType(1.0)
                ),
                Section.of(
                        Field.ofPasswordType("")
                )
        );

        Assert.assertEquals(6, f.getElements().size());
        Assert.assertEquals(4, f.getGroups().size());
    }

    @Test
    public void translationTest() {
        Form f = Form.of(
                Section.of(
                        Field.ofStringType("test")
                                .label("string_label")
                ).title("section_1_title")
        ).title("form_title")
                .i18n(service);

        Assert.assertEquals("Test Form", f.getTitle());

        service.changeLocale(rbDE);

        Assert.assertEquals("Testformular", f.getTitle());
        Assert.assertEquals("Erste Gruppe", ((Section) f.getGroups().get(0)).getTitle());
    }

    @Test
    public void changeTest() {
        StringField s = Field.ofStringType("test");
        DoubleField d = Field.ofDoubleType(2.0);
        Section sec = Section.of(s, d);

        Form f = Form.of(sec);

        s.userInputProperty().setValue("testttt");
        d.userInputProperty().setValue("3.0");

        Assert.assertEquals(true, s.hasChanged());
        Assert.assertEquals(true, sec.hasChanged());
        Assert.assertEquals(true, f.hasChanged());

        s.reset();

        Assert.assertEquals(false, s.hasChanged());
        Assert.assertEquals(true, sec.hasChanged());
        Assert.assertEquals(true, f.hasChanged());

        d.persist();

        Assert.assertEquals(false, s.hasChanged());
        Assert.assertEquals(false, sec.hasChanged());
        Assert.assertEquals(false, f.hasChanged());
    }

    @Test
    public void validationTest() {
        StringField s = Field.ofStringType("Testing")
                .validate(
                        StringLengthValidator.atLeast(5, "String not long enough."),
                        RegexValidator.forPattern("[a-z ]*", "String is not all lowercase.")
                );
        Section sec = Section.of(s);

        Form f = Form.of(sec);

        Assert.assertEquals(false, s.isValid());
        Assert.assertEquals(false, sec.isValid());
        Assert.assertEquals(false, f.isValid());
        Assert.assertEquals(1, s.getErrorMessages().size());

        s.userInputProperty().setValue("Test");

        Assert.assertEquals(false, s.isValid());
        Assert.assertEquals(false, sec.isValid());
        Assert.assertEquals(false, f.isValid());
        Assert.assertEquals(2, s.getErrorMessages().size());

        s.userInputProperty().setValue("Testing this");

        Assert.assertEquals(false, s.isValid());
        Assert.assertEquals(false, sec.isValid());
        Assert.assertEquals(false, f.isValid());
        Assert.assertEquals(1, s.getErrorMessages().size());

        s.userInputProperty().setValue("testing this properly");

        Assert.assertEquals(true, s.isValid());
        Assert.assertEquals(true, sec.isValid());
        Assert.assertEquals(true, f.isValid());
        Assert.assertTrue(s.getErrorMessages().isEmpty());
    }

    @Test
    public void persistableTest() {
        StringField s = Field.ofStringType("test");
        DoubleField d = Field.ofDoubleType(2.0);
        IntegerField i = Field.ofIntegerType(10);

        Form f = Form.of(Group.of(i), Section.of(s), Section.of(d));

        s.userInputProperty().setValue("testttt");

        Assert.assertEquals(true, s.hasChanged());
        Assert.assertEquals(true, f.hasChanged());
        Assert.assertEquals(true, f.isPersistable());

        s.reset();

        Assert.assertEquals(false, s.hasChanged());
        Assert.assertEquals(false, f.hasChanged());
        Assert.assertEquals(false, f.isPersistable());

        i.userInputProperty().setValue("testttt");

        Assert.assertEquals(true, i.hasChanged());
        Assert.assertEquals(true, f.hasChanged());
        Assert.assertEquals(false, f.isPersistable());

        f.persist();

        Assert.assertEquals(true, i.hasChanged());
        Assert.assertEquals(true, f.hasChanged());
        Assert.assertEquals(false, f.isPersistable());

        i.userInputProperty().setValue("50");

        Assert.assertEquals(true, f.isPersistable());

        f.reset();

        Assert.assertEquals(false, i.hasChanged());
        Assert.assertEquals(false, f.hasChanged());
        Assert.assertEquals(false, f.isPersistable());

        i.userInputProperty().setValue("50");
        f.persist();

        Assert.assertEquals(false, i.hasChanged());
        Assert.assertEquals(false, f.hasChanged());
        Assert.assertEquals(false, f.isPersistable());
    }

}
