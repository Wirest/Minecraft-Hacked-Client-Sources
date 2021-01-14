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

package me.hippo.api.lwjeb.message.result.impl;

import me.hippo.api.lwjeb.message.handler.MessageHandler;
import me.hippo.api.lwjeb.message.result.MessagePublicationResult;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Hippo
 * @version 5.0.0, 11/2/19
 * @since 5.0.0
 * <p>
 * This is a dead result, you will only get this if you post a topic and the bus can't find any handlers.
 */
public final class DeadMessagePublicationResult<T> implements MessagePublicationResult<T> {

    /**
     * @inheritDoc
     */
    @Override
    public void async() {
    }

    /**
     * @inheritDoc
     */
    @Override
    public void async(long timeout, TimeUnit timeUnit) {
    }

    /**
     * @inheritDoc
     */
    @Override
    public void dispatch() {
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<MessageHandler<T>> getHandlers() {
        return Collections.emptyList();
    }
}
