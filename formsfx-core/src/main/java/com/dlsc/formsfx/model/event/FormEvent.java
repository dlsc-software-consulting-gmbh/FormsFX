package com.dlsc.formsfx.model.event;

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

import com.dlsc.formsfx.model.structure.Form;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * Identifies events triggered by a {@code Form}.
 *
 * @author Andres Almiray
 */
public final class FormEvent extends Event {
    /**
     * When a {@code Form} is persisted.
     */
    public static final EventType<FormEvent> EVENT_FORM_PERSISTED = new EventType<>(ANY, "EVENT_FORM_PERSISTED");

    /**
     * When a {@code Form} is reset.
     */
    public static final EventType<FormEvent> EVENT_FORM_RESET = new EventType<>(ANY, "EVENT_FORM_RESET");

    /**
     * Creates a new instance of {@code FormEvent} with event type set to {@code EVENT_FORM_PERSISTED}.
     */
    public static FormEvent formPersistedEvent(Form form) {
        return new FormEvent(EVENT_FORM_PERSISTED, form);
    }

    /**
     * Creates a new instance of {@code FormEvent} with event type set to {@code EVENT_FORM_RESET}.
     */
    public static FormEvent formResetEvent(Form form) {
        return new FormEvent(EVENT_FORM_RESET, form);
    }

    private final Form form;

    private FormEvent(EventType<? extends Event> eventType, Form form) {
        super(eventType);
        this.form = form;
    }

    public final Form getForm() {
        return form;
    }
}
