package com.dlsc.formsfx.model.validators;

/**
 * A ValidationResult is the description of the result of a validation. It
 * contains the actual result, as well as possibly an error message.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class ValidationResult {

    private boolean result;
    private String errorMessage;

    ValidationResult(boolean result, String errorMessage) {
        this.result = result;

        // The error message is only included in the result if the validation
        // failed.

        this.errorMessage = !result ? errorMessage : null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean getResult() {
        return result;
    }

}
