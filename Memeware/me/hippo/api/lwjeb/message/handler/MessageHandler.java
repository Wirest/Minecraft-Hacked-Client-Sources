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

package me.hippo.api.lwjeb.message.handler;

import me.hippo.api.lwjeb.filter.MessageFilter;

/**
 * @author Hippo
 * @version 5.0.0, 10/27/19
 * @since 5.0.0
 * <p>
 * A message handler handles what happens whenever a topic is posed, typically done thorough method invocation.
 * May or may not have filters.
 */
public interface MessageHandler<T> {

    /**
     * Handles the topic.
     * <p>
     * Invoked whenever {@code topic} is posted.
     * </p>
     *
     * @param topic The topic.
     */
    void handle(T topic);

    /**
     * Gets the topic.
     *
     * @return The topic.
     */
    Class<T> getTopic();

    /**
     * Gets the filters.
     *
     * @return The filters.
     */
    MessageFilter<T>[] filters();

    /**
     * Checks if {@code topic} passes all the filters.
     *
     * @param topic The topic.
     * @return weather if it passes all the filters.
     */
    default boolean passesFilters(T topic) {
        for (MessageFilter<T> filter : filters()) {
            if (!filter.passes(topic)) {
                return false;
            }
        }
        return true;
    }


}
