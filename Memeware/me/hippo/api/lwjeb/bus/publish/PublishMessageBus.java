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

package me.hippo.api.lwjeb.bus.publish;

import me.hippo.api.lwjeb.bus.MessageBus;
import me.hippo.api.lwjeb.message.publish.MessagePublisher;

/**
 * @author Hippo
 * @version 5.0.0, 10/26/19
 * @since 5.0.0
 * <p>
 * A publish bus extends the functionality to allow topics to be published.
 */
public interface PublishMessageBus<T> extends MessageBus {

    /**
     * Gets the publisher.
     *
     * @return The publisher.
     */
    MessagePublisher<T> getPublisher();
}
