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
