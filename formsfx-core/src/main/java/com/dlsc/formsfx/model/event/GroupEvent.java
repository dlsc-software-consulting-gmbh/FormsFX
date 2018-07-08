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

import com.dlsc.formsfx.model.structure.Group;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * Identifies events triggered by a {@code Group}.
 *
 * @author Andres Almiray
 */
public final class GroupEvent extends Event {
    /**
     * When a {@code Group} is persisted.
     */
    public static final EventType<GroupEvent> EVENT_GROUP_PERSISTED = new EventType<>(ANY, "EVENT_GROUP_PERSISTED");

    /**
     * When a {@code Group} is reset.
     */
    public static final EventType<GroupEvent> EVENT_GROUP_RESET = new EventType<>(ANY, "EVENT_GROUP_RESET");

    /**
     * Creates a new instance of {@code GroupEvent} with event type set to {@code EVENT_GROUP_PERSISTED}.
     */
    public static GroupEvent groupPersistedEvent(Group group) {
        return new GroupEvent(EVENT_GROUP_PERSISTED, group);
    }

    /**
     * Creates a new instance of {@code GroupEvent} with event type set to {@code EVENT_GROUP_RESET}.
     */
    public static GroupEvent groupResetEvent(Group group) {
        return new GroupEvent(EVENT_GROUP_RESET, group);
    }

    private final Group group;

    private GroupEvent(EventType<? extends Event> eventType, Group group) {
        super(eventType);
        this.group = group;
    }

    public final Group getGroup() {
        return group;
    }
}
