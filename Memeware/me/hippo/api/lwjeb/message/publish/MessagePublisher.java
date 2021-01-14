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

package me.hippo.api.lwjeb.message.publish;


import me.hippo.api.lwjeb.bus.AbstractAsynchronousPubSubMessageBus;
import me.hippo.api.lwjeb.message.result.MessagePublicationResult;

/**
 * @author Hippo
 * @version 5.0.0, 10/27/19
 * @since 5.0.0
 * <p>
 * A message publisher is responsible for posting topics and returning results.
 */
@FunctionalInterface
public interface MessagePublisher<T> {

    /**
     * Post {@code topic} and returns the result.
     *
     * <p>
     * Returns {@link me.hippo.api.lwjeb.message.result.impl.DeadMessagePublicationResult} if it can't find any handlers.
     * </p>
     *
     * @param topic      The topic.
     * @param messageBus The bus.
     * @return The result.
     */
    MessagePublicationResult<T> publish(T topic, AbstractAsynchronousPubSubMessageBus<T> messageBus);
}
