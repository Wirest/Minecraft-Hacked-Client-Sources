/*
 * Copyright 2020 Hippo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package me.hippo.api.lwjeb.message.handler.impl;

import me.hippo.api.lwjeb.filter.MessageFilter;
import me.hippo.api.lwjeb.message.handler.MessageHandler;
import me.hippo.api.lwjeb.wrapped.WrappedType;

import java.util.function.Consumer;

/**
 * @author Hippo
 * @version 5.0.0, 1/13/20
 * @since 5.0.0
 * <p>
 * This is a field based implementation of the message handler.
 */
public final class FieldBasedMessageHandler<T> implements MessageHandler<T> {

    /**
     * The topic.
     */
    private final Class<T> topic;

    /**
     * The filters.
     */
    private final MessageFilter<T>[] filters;

    /**
     * The listener consumer, used for invocation.
     */
    private final Consumer listenerConsumer;

    /**
     * Weather the handler is for {@link WrappedType}s.
     */
    private final boolean wrapped;


    /**
     * Creates a new {@link FieldBasedMessageHandler}.
     *
     * @param topic            The topic.
     * @param filters          The filters.
     * @param listenerConsumer The listener.
     * @param wrapped          Weather its wrapped or not.
     */
    public FieldBasedMessageHandler(Class<T> topic, MessageFilter<T>[] filters, Consumer listenerConsumer, boolean wrapped) {
        this.topic = topic;
        this.filters = filters;
        this.listenerConsumer = listenerConsumer;
        this.wrapped = wrapped;
    }

    /**
     * @inheritDoc
     */
    @SuppressWarnings("unchecked")
    @Override
    public void handle(T topic) {
        listenerConsumer.accept(wrapped ? new WrappedType(topic) : topic);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Class<T> getTopic() {
        return topic;
    }

    /**
     * @inheritDoc
     */
    @Override
    public MessageFilter<T>[] filters() {
        return filters;
    }
}
