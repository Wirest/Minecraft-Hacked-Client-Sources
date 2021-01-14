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

package me.hippo.api.lwjeb.subscribe;

import me.hippo.api.lwjeb.bus.subscribe.SubscribeMessageBus;
import me.hippo.api.lwjeb.message.handler.MessageHandler;
import me.hippo.api.lwjeb.message.scan.MessageScanner;
import me.hippo.api.lwjeb.subscribe.ListenerSubscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hippo
 * @version 5.0.0, 11/2/19
 * @since 5.0.0
 * <p>
 * This is an abstract implementation of {@link ListenerSubscriber}, this just implements methods that typically wouldn't change per concrete implementation.
 */
public abstract class AbstractListenerSubscriber<T> implements ListenerSubscriber<T> {

    /**
     * The cache map.
     * <p>
     * Once an object is subscribed it will be added to the cache map, this makes second run subscribing much faster.
     * </p>
     */
    private final Map<Object, List<MessageHandler<T>>> cacheMap;

    protected AbstractListenerSubscriber() {
        cacheMap = new HashMap<>();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void unsubscribe(Object parent, MessageScanner<T> scanner, SubscribeMessageBus<T> subscribeBus) {
        List<MessageHandler<T>> cached = getCachedHandlers(parent, scanner, subscribeBus);
        for (MessageHandler<T> messageHandler : cached) {
            List<MessageHandler<T>> messageHandlers = subscriberMap().get(messageHandler.getTopic());
            if (messageHandlers != null) {
                messageHandlers.remove(messageHandler);
            }
        }
    }


    /**
     * @inheritDoc
     */
    @Override
    public List<MessageHandler<T>> getCachedHandlers(Object parent, MessageScanner<T> scanner, SubscribeMessageBus<T> subscribeBus) {
        return cacheMap.computeIfAbsent(parent, ignored -> scanner.scan(parent, subscribeBus));
    }
}
