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

package me.hippo.api.lwjeb.subscribe.impl;

import me.hippo.api.lwjeb.bus.subscribe.SubscribeMessageBus;
import me.hippo.api.lwjeb.message.handler.MessageHandler;
import me.hippo.api.lwjeb.message.scan.MessageScanner;
import me.hippo.api.lwjeb.subscribe.AbstractListenerSubscriber;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Hippo
 * @version 5.0.0, 11/2/19
 * @since 5.0.0
 * <p>
 * This is a strong implementation of the listener subscriber, this should only be used for concurrent purposes, anything out side of is a waste.
 */
public final class StrongReferencedListenerSubscriber<T> extends AbstractListenerSubscriber<T> {

    /**
     * The subscriber map.
     * <p>
     * This only contains subscribed objects.
     * </p>
     */
    private final ConcurrentHashMap<Class<T>, CopyOnWriteArrayList<MessageHandler<T>>> subscriberMap;

    public StrongReferencedListenerSubscriber() {
        this.subscriberMap = new ConcurrentHashMap<>();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void subscribe(Object parent, MessageScanner<T> scanner, SubscribeMessageBus<T> subscribeBus) {
        for (MessageHandler<T> cachedHandler : getCachedHandlers(parent, scanner, subscribeBus)) {
            subscriberMap.computeIfAbsent(cachedHandler.getTopic(), ignored -> new CopyOnWriteArrayList<>()).add(cachedHandler);
        }
    }

    /**
     * @inheritDoc
     */
    @SuppressWarnings("unchecked")
    @Override
    public ConcurrentHashMap<Class<T>, CopyOnWriteArrayList<MessageHandler<T>>> subscriberMap() {
        return subscriberMap;
    }

}
