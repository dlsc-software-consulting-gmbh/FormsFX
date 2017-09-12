package com.dlsc.formsfx.model.util;

/**
 * Contains constants for the different ways to handle value bindings.
 * {@code CONTINUOUS} persists field values upon any change, while
 * {@code PERSISTENT} only persists values when explicitly requested.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public enum BindingMode {

    CONTINUOUS,
    PERSISTENT

}
