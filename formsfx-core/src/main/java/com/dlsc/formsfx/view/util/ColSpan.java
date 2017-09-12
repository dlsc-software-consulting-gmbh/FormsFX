package com.dlsc.formsfx.view.util;

/**
 * An enum to set the span of columns in the form more easily.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public enum ColSpan {

    FIVE_SIXTH(10),
    TWO_THIRD(8),
    HALF(6),
    THIRD(4),
    QUARTER(3),
    SIXTH(2),
    TWELFTH(1);

    private int span;

    ColSpan(int span) {
        this.span = span;
    }

    public int valueOf() {
        return span;
    }

}
