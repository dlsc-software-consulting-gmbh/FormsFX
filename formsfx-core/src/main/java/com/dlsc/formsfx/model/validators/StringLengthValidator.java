package com.dlsc.formsfx.model.validators;

/*-
 * ========================LICENSE_START=================================
 * FormsFX
 * %%
 * Copyright (C) 2017 DLSC Software & Consulting
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */

/**
 * A StringLengthValidator checks if a string value's length is between a
 * minimum and a maximum value.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class StringLengthValidator extends CustomValidator<String> {

    private StringLengthValidator(int min, int max, String errorMessage) {
        super(input -> input.length() >= min && input.length() <= max, errorMessage);
    }

    /**
     * Creates an StringLengthValidator with given lower and upper bounds.
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
     * @return Returns a new StringLengthValidator.
     */
    public static StringLengthValidator between(int min, int max, String errorMessage) {
        if (min < 0) {
            throw new IllegalArgumentException("Minimum string length cannot be negative.");
        } else if (min > max) {
            throw new IllegalArgumentException("Minimum must not be larger than maximum.");
        }

        return new StringLengthValidator(min, max, errorMessage);
    }

    /**
     * Creates an StringLengthValidator with a given lower bound.
     *
     * @param min
     *              The lower bound for the validation.
     * @param errorMessage
     *              The error message that is returned if the validation fails.
     *
     * @throws IllegalArgumentException
     *              Thrown if the minimum is a negative number.
     *
     * @return Returns a new StringLengthValidator.
     */
    public static StringLengthValidator atLeast(int min, String errorMessage) {
        if (min < 0) {
            throw new IllegalArgumentException("Minimum string length cannot be negative.");
        }

        return new StringLengthValidator(min, Integer.MAX_VALUE, errorMessage);
    }

    /**
     * Creates an StringLengthValidator with a given upper bound.
     *
     * @param max
     *              The upper bound for the validation.
     * @param errorMessage
     *              The error message that is returned if the validation fails.
     *
     * @return Returns a new StringLengthValidator.
     */
    public static StringLengthValidator upTo(int max, String errorMessage) {
        return new StringLengthValidator(0, max, errorMessage);
    }

    /**
     * Creates a StringLengthValidator with a given lower and upper bound, which
     * are equal.
     *
     * @param value
     *              The lower and upper bound for the validation.
     * @param errorMessage
     *              The error message that is returned if the validation fails.
     *
     * @return Returns a new StringLengthValidator.
     */
    public static StringLengthValidator exactly(int value, String errorMessage) {
        return new StringLengthValidator(value, value, errorMessage);
    }

}
