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
import me.hippo.api.lwjeb.configuration.exception.handle.ExceptionHandler;
import me.hippo.api.lwjeb.configuration.exception.handle.impl.StandardExceptionHandler;

/**
 * @author Hippo
 * @version 5.0.0, 10/30/19
 * @since 5.0.0
 * <p>
 * The exception handling configuration allows you to configure how your errors are handled.
 */
public final class ExceptionHandlingConfiguration implements Configuration<ExceptionHandlingConfiguration> {

    /**
     * The exception handler.
     */
    private ExceptionHandler exceptionHandler;

    /**
     * Gets the default config.
     *
     * @return The default config.
     */
    public static ExceptionHandlingConfiguration getDefault() {
        return new ExceptionHandlingConfiguration().provideDefault();
    }

    /**
     * @inheritDoc
     */
    @Override
    public ExceptionHandlingConfiguration provideDefault() {
        ExceptionHandlingConfiguration configuration = new ExceptionHandlingConfiguration();
        configuration.setExceptionHandler(StandardExceptionHandler.INSTANCE);
        return configuration;
    }

    /**
     * Gets the exception handler.
     *
     * @return The exception handler.
     */
    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    /**
     * Sets the exception handler.
     *
     * @param exceptionHandler The exception handler.
     */
    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }
}
