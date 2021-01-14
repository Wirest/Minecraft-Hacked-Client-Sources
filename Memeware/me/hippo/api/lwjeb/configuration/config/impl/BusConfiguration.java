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

package me.hippo.api.lwjeb.configuration.config.impl;

import me.hippo.api.lwjeb.configuration.config.Configuration;
import me.hippo.api.lwjeb.message.result.MessagePublicationResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hippo
 * @version 5.0.0, 10/27/19
 * @since 5.0.0
 */
public final class BusConfiguration implements Configuration<BusConfiguration> {

    private String identifier;

    public static BusConfiguration getDefault() {
        return new BusConfiguration().provideDefault();
    }

    @Override
    public BusConfiguration provideDefault() {
        BusConfiguration configuration = new BusConfiguration();
        configuration.setIdentifier("LWJEB");
        return configuration;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

}
