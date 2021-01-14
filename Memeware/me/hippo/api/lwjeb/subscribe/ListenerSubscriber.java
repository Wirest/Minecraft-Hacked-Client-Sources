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

import java.util.List;
import java.util.Map;

/**
 * @author Hippo
 * @version 5.0.0, 11/1/19
 * @since 5.0.0
 * <p>
 * A listener subscriber is made for subscribing objects to the bus.
 * This makes the object scannable therefore its handlers can be collected.
 * Any subscribed objects handlers will be invoked whenever its topic gets published.
 */
public interface ListenerSubscriber<T> {

    /**
     * Subscribes an object to the bus.
     *
     * @param parent       The object to subscribe.
     * @param scanner      The scanner that will collect the objects handers.
     * @param subscribeBus The bus.
     */
    void subscribe(Object parent, MessageScanner<T> scanner, SubscribeMessageBus<T> subscribeBus);

    /**
     * Un-subscribes an object to the bus.
     *
     * @param parent       The object to subscribe.
     * @param scanner      The scanner that will collect the objects handers.
     * @param subscribeBus The bus.
     */
    void unsubscribe(Object parent, MessageScanner<T> scanner, SubscribeMessageBus<T> subscribeBus);

    /**
     * Gets the subscriber map.
     * <p>
     * Maps a class to a list of handlers.
     * </p>
     *
     * @param <S> The list of handlers.
     * @param <U> The map.
     * @return The map.
     */
    <S extends List<MessageHandler<T>>, U extends Map<Class<T>, S>> U subscriberMap();

    /**
     * Gets the the message handlers that the subscriber cached.
     *
     * @param parent       The object.
     * @param scanner      The scanner.
     * @param subscribeBus The bus.
     * @return The cached handlers.
     */
    List<MessageHandler<T>> getCachedHandlers(Object parent, MessageScanner<T> scanner, SubscribeMessageBus<T> subscribeBus);

}
