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
