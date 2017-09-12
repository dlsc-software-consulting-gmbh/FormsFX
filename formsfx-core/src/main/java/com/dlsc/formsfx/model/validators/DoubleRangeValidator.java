package com.dlsc.formsfx.model.validators;

/**
 * A DoubleRangeValidator checks if a double value is between a minimum and a
 * maximum value.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class DoubleRangeValidator extends CustomValidator<Double> {

    private DoubleRangeValidator(double min, double max, String errorMessage) {
        super(input -> input >= min && input <= max, errorMessage);
    }

    /**
     * Creates a DoubleRangeValidator with given lower and upper bounds.
     *
     * @param min
     *              The lower bound for the validation.
     * @param max
     *              The upper bound for the validation.
     * @param errorMessage
     *              The error message that is returned if the validation fails.
     *
     * @throws IllegalArgumentException
     *              Thrown if the maximum is not larger than or equal to the
     *              minimum.
     *
     * @return Returns a new DoubleRangeValidator.
     */
    public static DoubleRangeValidator between(double min, double max, String errorMessage) {
        if (min > max) {
            throw new IllegalArgumentException("Minimum must not be larger than maximum.");
        }

        return new DoubleRangeValidator(min, max, errorMessage);
    }

    /**
     * Creates a DoubleRangeValidator with a given lower bound.
     *
     * @param min
     *              The lower bound for the validation.
     * @param errorMessage
     *              The error message that is returned if the validation fails.
     *
     * @return Returns a new DoubleRangeValidator.
     */
    public static DoubleRangeValidator atLeast(double min, String errorMessage) {
        return new DoubleRangeValidator(min, Double.MAX_VALUE, errorMessage);
    }

    /**
     * Creates a DoubleRangeValidator with a given upper bound.
     *
     * @param max
     *              The upper bound for the validation.
     * @param errorMessage
     *              The error message that is returned if the validation fails.
     *
     * @return Returns a new DoubleRangeValidator.
     */
    public static DoubleRangeValidator upTo(double max, String errorMessage) {
        return new DoubleRangeValidator(Double.MIN_VALUE, max, errorMessage);
    }

    /**
     * Creates a DoubleRangeValidator with a given lower and upper bound, which
     * are equal.
     *
     * @param value
     *              The lower and upper bound for the validation.
     * @param errorMessage
     *              The error message that is returned if the validation fails.
     *
     * @return Returns a new DoubleRangeValidator.
     */
    public static DoubleRangeValidator exactly(double value, String errorMessage) {
        return new DoubleRangeValidator(value, value, errorMessage);
    }

}
