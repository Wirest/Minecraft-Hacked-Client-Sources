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

package me.hippo.api.lwjeb.message.scan;

import me.hippo.api.lwjeb.bus.AbstractAsynchronousPubSubMessageBus;
import me.hippo.api.lwjeb.bus.subscribe.SubscribeMessageBus;
import me.hippo.api.lwjeb.message.handler.MessageHandler;

import java.util.List;

/**
 * @author Hippo
 * @version 5.0.0, 11/2/19
 * @since 5.0.0
 * <p>
 * A message scanner searches through an object and finds message handlers.
 */
@FunctionalInterface
public interface MessageScanner<T> {

    /**
     * Gets all the message handlers in an object.
     *
     * @param parent     The object to scan.
     * @param messageBus The bus.
     * @return The handlers.
     */
    List<MessageHandler<T>> scan(Object parent, SubscribeMessageBus messageBus);
}
