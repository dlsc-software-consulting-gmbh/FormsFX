package com.dlsc.formsfx.model.validators;

/**
 * A IntegerRangeValidator checks if an integer value is between a minimum and a
 * maximum value.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class IntegerRangeValidator extends CustomValidator<Integer> {

    private IntegerRangeValidator(int min, int max, String errorMessage) {
        super(input -> input >= min && input <= max, errorMessage);
    }

    /**
     * Creates an IntegerRangeValidator with given lower and upper bounds.
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
     * @return Returns a new IntegerRangeValidator.
     */
    public static IntegerRangeValidator between(int min, int max, String errorMessage) {
        if (min > max) {
            throw new IllegalArgumentException("Minimum must not be larger than maximum.");
        }

        return new IntegerRangeValidator(min, max, errorMessage);
    }

    /**
     * Creates an IntegerRangeValidator with a given lower bound.
     *
     * @param min
     *              The lower bound for the validation.
     * @param errorMessage
     *              The error message that is returned if the validation fails.
     *
     * @return Returns a new IntegerRangeValidator.
     */
    public static IntegerRangeValidator atLeast(int min, String errorMessage) {
        return new IntegerRangeValidator(min, Integer.MAX_VALUE, errorMessage);
    }

    /**
     * Creates an IntegerRangeValidator with a given upper bound.
     *
     * @param max
     *              The upper bound for the validation.
     * @param errorMessage
     *              The error message that is returned if the validation fails.
     *
     * @return Returns a new IntegerRangeValidator.
     */
    public static IntegerRangeValidator upTo(int max, String errorMessage) {
        return new IntegerRangeValidator(Integer.MIN_VALUE, max, errorMessage);
    }

    /**
     * Creates a IntegerRangeValidator with a given lower and upper bound,
     * which are equal.
     *
     * @param value
     *              The lower and upper bound for the validation.
     * @param errorMessage
     *              The error message that is returned if the validation fails.
     *
     * @return Returns a new IntegerRangeValidator.
     */
    public static IntegerRangeValidator exactly(int value, String errorMessage) {
        return new IntegerRangeValidator(value, value, errorMessage);
    }

}
