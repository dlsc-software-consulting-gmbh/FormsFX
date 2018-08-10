package com.dlsc.formsfx.model.structure;

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


import com.dlsc.formsfx.model.event.GroupEvent;
import com.dlsc.formsfx.model.util.TranslationService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A group is the intermediate unit in a form. It is used to group form
 * elements to a larger unit. It also acts as a proxy to some properties of
 * the contained data, such as validity or changes.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class Group {

    protected final List<Element> elements = new ArrayList<>();

    /**
     * The group acts as a proxy for its contained elements' {@code changed}
     * and {@code valid} properties.
     */
    protected final BooleanProperty valid = new SimpleBooleanProperty(true);
    protected final BooleanProperty changed = new SimpleBooleanProperty(false);

    /**
     * The translation service is passed down from the containing form. It
     * is used to translate all translatable values of the field.
     */
    protected TranslationService translationService;

    private final Map<EventType<GroupEvent>,List<EventHandler<? super GroupEvent>>> eventHandlers = new ConcurrentHashMap<>();

    /**
     * Internal constructor for the {@code Group} class. To create new
     * groups, see the static factory method in this class.
     *
     * @see Group::of
     *
     * @param elements
     *              A varargs list of elements that are contained in this
     *              group.
     */
    protected Group(Element... elements) {
        Collections.addAll(this.elements, elements);

        // If any of the elements are marked as changed, the group is updated
        // accordingly.

        this.elements.stream()
            .filter(e -> e instanceof Field)
            .map(e -> (Field) e)
            .forEach(f -> f.changedProperty().addListener((observable, oldValue, newValue) -> setChangedProperty()));

        // If any of the elements are marked as invalid, the group is updated
        // accordingly.

        this.elements.stream()
            .filter(e -> e instanceof Field)
            .map(e -> (Field) e)
            .forEach(f -> f.validProperty().addListener((observable, oldValue, newValue) -> setValidProperty()));

        setValidProperty();
        setChangedProperty();
    }

    /**
     * Creates a new group containing the given elements.
     *
     * @param elements
     *              The elements to be included in the group.
     *
     * @return Returns a new {@code Group}.
     */
    public static Group of(Element... elements) {
        return new Group(elements);
    }

    /**
     * This internal method is called by the containing form when a new
     * translation has been added to the form. Also applies the translation
     * to all contained elements.
     *
     * @see Field::translate
     *
     * @param newValue
     *              The new service to use for translating translatable values.
     */
    protected void translate(TranslationService newValue) {
        translationService = newValue;

        if (!isI18N()) {
            return;
        }

        elements.stream()
            .filter(e -> e instanceof Field)
            .map(e -> (Field) e)
            .forEach(f -> f.translate(translationService));
    }

    /**
     * Persists the values for all contained elements.
     * @see Field::persist
     */
    public void persist() {
        if (!isValid()) {
            return;
        }

        elements.stream()
            .filter(e -> e instanceof FormElement)
            .map(e -> (FormElement) e)
            .forEach(FormElement::persist);

        fireEvent(GroupEvent.groupPersistedEvent(this));
    }

    /**
     * Resets the values for all contained elements.
     * @see Field::reset
     */
    public void reset() {
        if (!hasChanged()) {
            return;
        }

        elements.stream()
            .filter(e -> e instanceof FormElement)
            .map(e -> (FormElement) e)
            .forEach(FormElement::reset);
    }

    /**
     * Sets this group's {@code changed} property based on its contained
     * elements' changed properties.
     */
    private void setChangedProperty() {
        changed.setValue(elements.stream()
            .filter(e -> e instanceof Field)
            .map(e -> (Field) e)
            .anyMatch(Field::hasChanged));
    }

    /**
     * Sets this group's {@code valid} property based on its contained elements'
     * changed properties.
     */
    private void setValidProperty() {
        valid.setValue(elements.stream()
            .filter(e -> e instanceof Field)
            .map(e -> (Field) e)
            .allMatch(Field::isValid));
    }

    public List<Element> getElements() {
        return elements;
    }

    public boolean hasChanged() {
        return changed.get();
    }

    public BooleanProperty changedProperty() {
        return changed;
    }

    public boolean isValid() {
        return valid.get();
    }

    public BooleanProperty validProperty() {
        return valid;
    }

    public boolean isI18N() {
        return translationService != null;
    }

    /**
     * Registers an event handler to this group. The handler is called when the
     * group receives an {@code Event} of the specified type during the bubbling
     * phase of event delivery.
     *
     * @param eventType    the type of the events to receive by the handler
     * @param eventHandler the handler to register
     *
     * @throws NullPointerException if either event type or handler are {@code null}.
     */
    public Group addEventHandler(EventType<GroupEvent> eventType, EventHandler<? super GroupEvent> eventHandler) {
        if (eventType == null) {
            throw new NullPointerException("Argument eventType must not be null");
        }
        if (eventHandler == null) {
            throw new NullPointerException("Argument eventHandler must not be null");
        }

        this.eventHandlers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(eventHandler);

        return this;
    }

    /**
     * Unregisters a previously registered event handler from this group. One
     * handler might have been registered for different event types, so the
     * caller needs to specify the particular event type from which to
     * unregister the handler.
     *
     * @param eventType    the event type from which to unregister
     * @param eventHandler the handler to unregister
     *
     * @throws NullPointerException if either event type or handler are {@code null}.
     */
    public Group removeEventHandler(EventType<GroupEvent> eventType, EventHandler<? super GroupEvent> eventHandler) {
        if (eventType == null) {
            throw new NullPointerException("Argument eventType must not be null");
        }
        if (eventHandler == null) {
            throw new NullPointerException("Argument eventHandler must not be null");
        }

        List<EventHandler<? super GroupEvent>> list = this.eventHandlers.get(eventType);
        if (list != null) {
            list.remove(eventHandler);
        }

        return this;
    }

    protected void fireEvent(GroupEvent event) {
        List<EventHandler<? super GroupEvent>> list = this.eventHandlers.get(event.getEventType());
        if (list == null) {
            return;
        }
        for (EventHandler<? super GroupEvent> eventHandler : list) {
            if (!event.isConsumed()) {
                eventHandler.handle(event);
            }
        }
    }
}
