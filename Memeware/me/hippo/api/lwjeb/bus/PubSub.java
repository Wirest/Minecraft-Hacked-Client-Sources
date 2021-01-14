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

package me.hippo.api.lwjeb.bus;

import me.hippo.api.lwjeb.configuration.BusConfigurations;
import me.hippo.api.lwjeb.configuration.config.impl.BusPubSubConfiguration;
import me.hippo.api.lwjeb.message.publish.MessagePublisher;
import me.hippo.api.lwjeb.message.result.MessagePublicationResult;
import me.hippo.api.lwjeb.subscribe.ListenerSubscriber;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hippo
 * @version 5.0.0, 11/1/19
 * @since 5.0.0
 * <p>
 * A concrete implementation of all the busses, this will fit almost every single case.
 * This bus has a result cache, to speed up publications.
 */
public final class PubSub<T> extends AbstractAsynchronousPubSubMessageBus<T> {

    /**
     * The listener subscriber.
     */
    private final ListenerSubscriber<T> listenerSubscriber;

    /**
     * The message publisher.
     */
    private final MessagePublisher<T> messagePublisher;

    /**
     * The bus configurations.
     */
    private final BusPubSubConfiguration busPubSubConfiguration;

    /**
     * The result cache.
     */
    private final Map<Object, MessagePublicationResult<T>> resultCache;


    /**
     * Creates a new {@link PubSub} with the default configuration.
     */
    public PubSub() {
        this(BusConfigurations.getDefault());
    }

    /**
     * @inheritDoc
     */
    public PubSub(BusConfigurations busConfigurations) {
        super(busConfigurations);
        this.busPubSubConfiguration = busConfigurations.get(BusPubSubConfiguration.class);
        this.listenerSubscriber = busPubSubConfiguration.getSubscriber();
        this.messagePublisher = busPubSubConfiguration.getPublisher();
        this.resultCache = new HashMap<>();
    }


    /**
     * Post {@code topic}.
     *
     * @param topic The topic.
     * @return The result.
     */
    public MessagePublicationResult<T> post(T topic) {
        MessagePublicationResult<T> result = resultCache.get(topic);
        if (result == null) {
            MessagePublicationResult<T> publish = messagePublisher.publish(topic, this);
            resultCache.put(topic, publish);
            return publish;
        }
        return result;
    }

    /**
     * Subscribes {@code parent}.
     *
     * @param parent The parent.
     */
    public void subscribe(Object parent) {
        invalidateCaches();
        listenerSubscriber.subscribe(parent, busPubSubConfiguration.getScanner(), this);
    }

    /**
     * Un-subscribes {@code parent}.
     *
     * @param parent The parent.
     */
    public void unsubscribe(Object parent) {
        invalidateCaches();
        listenerSubscriber.unsubscribe(parent, busPubSubConfiguration.getScanner(), this);
    }

    /**
     * Clears all the result caches.
     */
    public void invalidateCaches() {
        resultCache.clear();
    }

    /**
     * @inheritDoc
     */
    @Override
    public MessagePublisher<T> getPublisher() {
        return messagePublisher;
    }

    /**
     * @inheritDoc
     */
    @Override
    public ListenerSubscriber<T> getSubscriber() {
        return listenerSubscriber;
    }

}
