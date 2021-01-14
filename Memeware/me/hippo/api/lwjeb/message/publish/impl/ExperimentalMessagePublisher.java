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

package me.hippo.api.lwjeb.message.publish.impl;

import me.hippo.api.lwjeb.bus.AbstractAsynchronousPubSubMessageBus;
import me.hippo.api.lwjeb.message.handler.MessageHandler;
import me.hippo.api.lwjeb.message.publish.MessagePublisher;
import me.hippo.api.lwjeb.message.result.MessagePublicationResult;
import me.hippo.api.lwjeb.message.result.impl.DeadMessagePublicationResult;
import me.hippo.api.lwjeb.message.result.impl.ExperimentalMessagePublicationResult;
import me.hippo.api.lwjeb.message.result.impl.StandardMessagePublicationResult;
import me.hippo.api.lwjeb.subscribe.ListenerSubscriber;

import java.util.List;
import java.util.Map;

/**
 * @author Hippo
 * @version 5.0.0, 11/2/19
 * @since 5.0.0
 * <p>
 * This is the experiential implementation if a message publisher, it returns experiential results.
 */
public final class ExperimentalMessagePublisher<T> implements MessagePublisher<T> {

    /**
     * @inheritDoc
     */
    @Override
    public MessagePublicationResult<T> publish(T topic, AbstractAsynchronousPubSubMessageBus<T> messageBus) {
        List<MessageHandler<T>> messageHandlers = messageBus.getSubscriber().subscriberMap().get(topic.getClass());
        return messageHandlers == null ? new DeadMessagePublicationResult<>() : new ExperimentalMessagePublicationResult<>(messageBus, messageHandlers, topic);
    }
}
