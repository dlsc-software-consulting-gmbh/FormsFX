package com.dlsc.formsfx.model.validators;

import java.util.function.Predicate;

/**
 * A custom validator implementation of the root validator. This validator
 * takes a generic object as an input parameter and allows users to perform
 * any kind of validation.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class CustomValidator<T> extends RootValidator<T> {

    private Predicate<T> callback;

    CustomValidator(Predicate<T> callback, String errorMessage) {
        super(errorMessage);
        this.callback = callback;
    }

    public static <E> CustomValidator<E> forPredicate(Predicate<E> callback, String errorMessage) {
        return new CustomValidator<>(callback, errorMessage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidationResult validate(T input) {
        return createResult(callback.test(input));
    }

}
