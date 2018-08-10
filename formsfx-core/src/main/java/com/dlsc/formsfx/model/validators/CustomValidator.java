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

    protected CustomValidator(Predicate<T> callback, String errorMessage) {
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
