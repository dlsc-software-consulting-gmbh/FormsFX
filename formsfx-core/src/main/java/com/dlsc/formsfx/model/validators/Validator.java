package com.dlsc.formsfx.model.validators;

/**
 * A validator is used to validate a generic input for a specific syntax or
 * semantic.
 * 
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public interface Validator<T> {

    ValidationResult validate(T input);

}
