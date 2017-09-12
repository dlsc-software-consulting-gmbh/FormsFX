package com.dlsc.formsfx.model.validators;

/**
 * The RootValidator contains helper methods for concrete validator implementations.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
abstract class RootValidator<T> implements Validator<T> {

    private String errorMessage;

    RootValidator(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Creates a {@link ValidationResult} based on the validation result.
     *
     * @param result
     *              The result of the validation.
     *
     * @return Returns a new ValidationResult containing result and message.
     */
    ValidationResult createResult(boolean result) {
        return new ValidationResult(result, errorMessage);
    }

}
