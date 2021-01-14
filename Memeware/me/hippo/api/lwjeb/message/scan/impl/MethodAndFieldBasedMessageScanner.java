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

package me.hippo.api.lwjeb.message.scan.impl;

import me.hippo.api.lwjeb.bus.subscribe.SubscribeMessageBus;
import me.hippo.api.lwjeb.message.handler.MessageHandler;
import me.hippo.api.lwjeb.message.scan.MessageScanner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hippo
 * @version 5.0.0, 1/13/20
 * @since 5.0.0
 * <p>
 * This is a method and field based implementation of the message scanner, this will search for both method and fields.
 */
public final class MethodAndFieldBasedMessageScanner<T> implements MessageScanner<T> {

    /**
     * @inheritDoc
     */
    @Override
    public List<MessageHandler<T>> scan(Object parent, SubscribeMessageBus messageBus) {
        List<MessageHandler<T>> messageHandlers = new ArrayList<>();
        messageHandlers.addAll(new MethodBasedMessageScanner<T>().scan(parent, messageBus));
        messageHandlers.addAll(new FieldBasedMessageScanner<T>().scan(parent, messageBus));
        return messageHandlers;
    }
}
