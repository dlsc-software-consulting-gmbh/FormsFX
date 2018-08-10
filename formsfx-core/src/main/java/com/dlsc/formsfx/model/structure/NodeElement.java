package com.dlsc.formsfx.model.structure;

/*-
 * ========================LICENSE_START=================================
 * FormsFX
 * %%
 * Copyright (C) 2017 - 2018 DLSC Software & Consulting
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

import javafx.scene.Node;

/**
 * @author Andres Almiray
 */
public class NodeElement<N extends Node> extends Element {
    protected N node;

    public static <T extends Node> NodeElement<T> of(T node) {
        return new NodeElement(node);
    }

    protected NodeElement(N node) {
        if (node == null) {
            throw new NullPointerException("Node argument must not be null");
        }
        this.node = node;
    }

    public N getNode() {
        return node;
    }
}
