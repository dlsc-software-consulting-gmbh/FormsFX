package com.dlsc.formsfx.model.validators;

import javafx.collections.ObservableList;

/**
 * A SelectionLengthValidator checks if a selection list's length is between a
 * minimum and a maximum value.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class SelectionLengthValidator<E> extends CustomValidator<ObservableList<E>> {

    private SelectionLengthValidator(int min, int max, String errorMessage) {
        super(input -> input.size() >= min && input.size() <= max, errorMessage);
    }

    /**
     * Creates an SelectionLengthValidator with given lower and upper bounds.
     *
     * @param min
     *              The lower bound for the validation.
     * @param max
     *              The upper bound for the validation.
     * @param errorMessage
     *              The error message that is returned if the validation fails.
     *
     * @throws IllegalArgumentException
     *              Thrown if the minimum is a negative number.
     *
     * @return Returns a new SelectionLengthValidator.
     */
    public static <T> SelectionLengthValidator<T> between(int min, int max, String errorMessage) {
        if (min < 0) {
            throw new IllegalArgumentException("Minimum string length cannot be negative.");
        } else if (min > max) {
            throw new IllegalArgumentException("Minimum must not be larger than maximum.");
        }

        return new SelectionLengthValidator<>(min, max, errorMessage);
    }

    /**
     * Creates an SelectionLengthValidator with a given lower bound.
     *
     * @param min
     *              The lower bound for the validation.
     * @param errorMessage
     *              The error message that is returned if the validation fails.
     *
     * @throws IllegalArgumentException
     *              Thrown if the minimum is a negative number.
     *
     * @return Returns a new SelectionLengthValidator.
     */
    public static <T> SelectionLengthValidator<T> atLeast(int min, String errorMessage) {
        if (min < 0) {
            throw new IllegalArgumentException("Minimum string length cannot be negative.");
        }

        return new SelectionLengthValidator<>(min, Integer.MAX_VALUE, errorMessage);
    }

    /**
     * Creates an SelectionLengthValidator with a given upper bound.
     *
     * @param max
     *              The upper bound for the validation.
     * @param errorMessage
     *              The error message that is returned if the validation fails.
     *
     * @return Returns a new SelectionLengthValidator.
     */
    public static <T> SelectionLengthValidator<T> upTo(int max, String errorMessage) {
        return new SelectionLengthValidator<>(0, max, errorMessage);
    }

    /**
     * Creates a SelectionLengthValidator with a given lower and upper bound,
     * which are equal.
     *
     * @param value
     *              The lower and upper bound for the validation.
     * @param errorMessage
     *              The error message that is returned if the validation fails.
     *
     * @return Returns a new SelectionLengthValidator.
     */
    public static <T> SelectionLengthValidator<T> exactly(int value, String errorMessage) {
        return new SelectionLengthValidator<>(value, value, errorMessage);
    }

}
