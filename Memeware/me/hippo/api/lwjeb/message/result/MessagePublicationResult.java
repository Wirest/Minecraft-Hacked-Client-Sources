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

package me.hippo.api.lwjeb.message.result;

import me.hippo.api.lwjeb.message.handler.MessageHandler;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Hippo
 * @version 5.0.0, 10/30/19
 * @since 5.0.0
 * <p>
 * A publication result is what you get whenever you post a topic, this will give you more control on how you handle your publications.
 */
public interface MessagePublicationResult<T> {

    /**
     * This will immediately invoke all the handlers on the main thread.
     */
    void dispatch();

    /**
     * This will add the result to the asynchronous queue then it will be dispatched as soon as it can.
     */
    void async();

    /**
     * This will add the result to the asynchronous queue if {@code timeout} has been reached then it will be removed.
     *
     * @param timeout  The time out.
     * @param timeUnit The time unit.
     */
    void async(long timeout, TimeUnit timeUnit);

    /**
     * Gets all the handlers in the result.
     *
     * @return The handlers.
     */
    List<MessageHandler<T>> getHandlers();

}
