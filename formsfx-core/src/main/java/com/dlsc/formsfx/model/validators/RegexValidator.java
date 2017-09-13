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

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * A RegexValidator checks if a given input matches a regular expression.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class RegexValidator extends RootValidator<String> {

    private Pattern pattern;

    private RegexValidator(String pattern, String errorMessage) throws PatternSyntaxException {
        super(errorMessage);
        this.pattern = Pattern.compile(pattern);
    }

    /**
     * Creates a RegexValidator with a custom pattern.
     *
     * @param pattern
     *              The pattern to use for the validation. Must be a valid
     *              RegEx.
     * @param errorMessage
     *              The error message that is returned if the validation fails.
     *
     * @throws PatternSyntaxException
     *              Thrown if the given pattern is not a valid RegEx.
     *
     * @return Returns a new RegexValidator.
     */
    public static RegexValidator forPattern(String pattern, String errorMessage) {
        return new RegexValidator(pattern, errorMessage);
    }

    /**
     * Creates a RegexValidator for email addresses.
     *
     * @param errorMessage
     *              The error message that is returned if the validation fails.
     *
     * @throws PatternSyntaxException
     *              Thrown if the given pattern is not a valid RegEx.
     *
     * @return Returns a new RegexValidator.
     */
    public static RegexValidator forEmail(String errorMessage) {
        return new RegexValidator("^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$", errorMessage);
    }

    /**
     * Creates a RegexValidator for URLs.
     *
     * @param errorMessage
     *              The error message that is returned if the validation fails.
     *
     * @throws PatternSyntaxException
     *              Thrown if the given pattern is not a valid RegEx.
     *
     * @return Returns a new RegexValidator.
     */
    public static RegexValidator forURL(String errorMessage) {
        return new RegexValidator("(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]\\.[^\\s]{2,})", errorMessage);
    }

    /**
     * Creates a RegexValidator for alphanumeric inputs.
     *
     * @param errorMessage
     *              The error message that is returned if the validation fails.
     *
     * @throws PatternSyntaxException
     *              Thrown if the given pattern is not a valid RegEx.
     *
     * @return Returns a new RegexValidator.
     */
    public static RegexValidator forAlphaNumeric(String errorMessage) {
        return new RegexValidator("^[a-zA-Z0-9]*$", errorMessage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidationResult validate(String input) {
        return createResult(pattern.matcher(input).matches());
    }

}
