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

package me.hippo.api.lwjeb.configuration.exception.handle.impl;

import me.hippo.api.lwjeb.configuration.exception.handle.ExceptionHandler;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.DateFormat;
import java.util.Date;

/**
 * @author Hippo
 * @version 5.0.0, 10/30/19
 * @since 5.0.0
 * <p>
 * The descriptive handler prints the stack trace and some information about the jvm.
 */
public enum DescriptiveExceptionHandler implements ExceptionHandler {
    INSTANCE;

    /**
     * @inheritDoc
     */
    @Override
    public void handleException(Throwable t) {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

        StringBuilder jvmArguments = new StringBuilder();
        for (String jvmArgument : runtimeMXBean.getInputArguments()) {
            jvmArguments.append(jvmArgument).append(' ');
        }

        System.err.println(DateFormat.getInstance().format(new Date()));
        System.err.println("\nAn exception was thrown, " + t.toString());
        System.err.println("Stacktrace:");
        t.printStackTrace();
        System.err.println("\nCurrent java version: " + System.getProperty("java.version"));
        System.err.println("JVM Arguments: " + jvmArguments.toString());
        System.err.println("JVM Information: " + runtimeMXBean.getVmName() + " | " + runtimeMXBean.getVmVendor() + " | " + runtimeMXBean.getVmVersion());
        System.err.println("Memory: " + Runtime.getRuntime().totalMemory() + "/" + Runtime.getRuntime().maxMemory());
    }
}
