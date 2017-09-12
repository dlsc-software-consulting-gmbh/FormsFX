package com.dlsc.formsfx.model.util;

/**
 * A value transformer takes a string as an input and returns a parsed type.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public interface ValueTransformer<T> {

    T transform(String input);

}
