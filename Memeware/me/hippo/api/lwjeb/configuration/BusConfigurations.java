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

package me.hippo.api.lwjeb.configuration;


import me.hippo.api.lwjeb.configuration.config.Configuration;
import me.hippo.api.lwjeb.configuration.config.impl.AsynchronousPublicationConfiguration;
import me.hippo.api.lwjeb.configuration.config.impl.BusConfiguration;
import me.hippo.api.lwjeb.configuration.config.impl.BusPubSubConfiguration;
import me.hippo.api.lwjeb.configuration.config.impl.ExceptionHandlingConfiguration;
import me.hippo.api.lwjeb.configuration.exception.BusConfigurationException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Hippo
 * @version 5.0.0, 10/26/19
 * @since 5.0.0
 * <p>
 * The bus configurations hold all the configuration instances.
 */
public final class BusConfigurations {

    /**
     * The configuration map.
     */
    private final Map<Class<? extends Configuration<?>>, Configuration<?>> configurationMap;


    private BusConfigurations(Builder builder) {
        this.configurationMap = builder == null ? new HashMap<>() : builder.configurationMap;
    }

    /**
     * Gets the default configuration.
     * <p>
     * This configuration will be good for most non-concurrent cases.
     * </p>
     *
     * @return The default configuration.
     */
    public static BusConfigurations getDefault() {
        BusConfigurations configurations = new BusConfigurations(null);
        configurations.configurationMap.put(AsynchronousPublicationConfiguration.class, AsynchronousPublicationConfiguration.getDefault());
        configurations.configurationMap.put(BusConfiguration.class, BusConfiguration.getDefault());
        configurations.configurationMap.put(ExceptionHandlingConfiguration.class, ExceptionHandlingConfiguration.getDefault());
        configurations.configurationMap.put(BusPubSubConfiguration.class, BusPubSubConfiguration.getDefault());
        return configurations;
    }

    /**
     * Gets the configuration by class.
     *
     * @param configuration The configuration class.
     * @param <T>           The configuration.
     * @return The configuration.
     */
    public <T extends Configuration<?>> T get(Class<T> configuration) {

        T config = configuration.cast(configurationMap.get(configuration));
        if (config == null) {
            throw new BusConfigurationException("Could not find configuration for \"" + configuration.getName() + "\".");
        }
        return config;
    }

    /**
     * The builder that will help build configurations.
     */
    public static final class Builder {

        /**
         * The configuration map.
         */
        private final Map<Class<? extends Configuration<?>>, Configuration<?>> configurationMap = new HashMap<>();

        /**
         * Sets a configuration.
         *
         * @param configurationClass    The configuration class.
         * @param configurationSupplier The supplier that will supply the configuration.
         * @return This.
         */
        public Builder setConfiguration(Class<? extends Configuration<?>> configurationClass, Supplier<Configuration<?>> configurationSupplier) {
            configurationMap.put(configurationClass, configurationSupplier.get());
            return this;
        }

        /**
         * Builds the new configuration.
         *
         * @return The configuration.
         */
        public BusConfigurations build() {
            BusConfigurations reference = BusConfigurations.getDefault();
            for (Class<? extends Configuration<?>> configuration : reference.configurationMap.keySet()) {
                configurationMap.computeIfAbsent(configuration, reference.configurationMap::get);
            }

            return new BusConfigurations(this);
        }
    }
}
