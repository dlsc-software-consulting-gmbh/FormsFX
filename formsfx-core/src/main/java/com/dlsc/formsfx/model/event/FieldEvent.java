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

import com.dlsc.formsfx.model.structure.Field;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * Identifies events triggered by a {@code Field}.
 *
 * @author Andres Almiray
 */
public final class FieldEvent extends Event {
    /**
     * When a {@code Field} is persisted.
     */
    public static final EventType<FieldEvent> EVENT_FIELD_PERSISTED = new EventType<>(ANY, "EVENT_FIELD_PERSISTED");

    /**
     * When a {@code Field} is reset.
     */
    public static final EventType<FieldEvent> EVENT_FIELD_RESET = new EventType<>(ANY, "EVENT_FIELD_RESET");

    /**
     * Creates a new instance of {@code FieldEvent} with event type set to {@code EVENT_FIELD_PERSISTED}.
     */
    public static FieldEvent fieldPersistedEvent(Field field) {
        return new FieldEvent(EVENT_FIELD_PERSISTED, field);
    }

    /**
     * Creates a new instance of {@code FieldEvent} with event type set to {@code EVENT_FIELD_RESET}.
     */
    public static FieldEvent fieldResetEvent(Field field) {
        return new FieldEvent(EVENT_FIELD_RESET, field);
    }

    private final Field field;

    private FieldEvent(EventType<? extends Event> eventType, Field field) {
        super(eventType);
        this.field = field;
    }

    public final Field getField() {
        return field;
    }
}
