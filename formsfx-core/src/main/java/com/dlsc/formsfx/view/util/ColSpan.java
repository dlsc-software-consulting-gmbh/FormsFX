package com.dlsc.formsfx.view.util;

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
 * An enum to set the span of columns in the form more easily.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public enum ColSpan {

    FIVE_SIXTH(10),
    TWO_THIRD(8),
    HALF(6),
    THIRD(4),
    QUARTER(3),
    SIXTH(2),
    TWELFTH(1);

    private int span;

    ColSpan(int span) {
        this.span = span;
    }

    public int valueOf() {
        return span;
    }

}
