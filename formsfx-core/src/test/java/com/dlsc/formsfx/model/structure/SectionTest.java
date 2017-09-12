package com.dlsc.formsfx.model.structure;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class SectionTest {

    @Test
    public void collapseTest() {
        Section s = Section.of();

        final int[] changes = { 0 };

        s.collapsedProperty().addListener((observable, oldValue, newValue) -> changes[0] += 1);

        s.collapse(true);
        s.collapse(false);

        Assert.assertEquals(2, changes[0]);
        Assert.assertFalse(s.isCollapsed());
    }

}
