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

package me.hippo.api.lwjeb.bus;

import me.hippo.api.lwjeb.bus.publish.AsynchronousPublishMessageBus;
import me.hippo.api.lwjeb.bus.subscribe.SubscribeMessageBus;
import me.hippo.api.lwjeb.configuration.BusConfigurations;
import me.hippo.api.lwjeb.configuration.config.impl.AsynchronousPublicationConfiguration;
import me.hippo.api.lwjeb.configuration.config.impl.BusConfiguration;
import me.hippo.api.lwjeb.configuration.config.impl.ExceptionHandlingConfiguration;
import me.hippo.api.lwjeb.message.result.MessagePublicationResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author Hippo
 * @version 5.0.1, 10/27/19
 * @since 5.0.0
 * <p>
 * An abstract implementation of an asynchronous publish bus and a message bus, this implements methods that typically wont change.
 */
public abstract class AbstractAsynchronousPubSubMessageBus<T> implements AsynchronousPublishMessageBus<T>, SubscribeMessageBus<T> {

    /**
     * The dispatchers.
     */
    private final List<Thread> dispatchers;

    /**
     * The result queue.
     */
    private final BlockingQueue<MessagePublicationResult<T>> resultQueue;

    /**
     * All the configurations.
     */
    private final BusConfigurations busConfigurations;

    /**
     * The publication configuration.
     */
    private final AsynchronousPublicationConfiguration asynchronousPublicationConfiguration;

    /**
     * The exception handling configuration.
     */
    private final ExceptionHandlingConfiguration exceptionHandlingConfiguration;

    /**
     * The bus configuration.
     */
    private final BusConfiguration busConfiguration;

    /**
     * Weather the bus is shutdown or not.
     */
    private volatile boolean shutdown;

    /**
     * Creates a new {@link AbstractAsynchronousPubSubMessageBus} with the desired configurations.
     *
     * @param busConfigurations The configurations.
     */
    public AbstractAsynchronousPubSubMessageBus(BusConfigurations busConfigurations) {
        this.dispatchers = new ArrayList<>();
        this.resultQueue = new LinkedBlockingQueue<>();
        this.busConfigurations = busConfigurations;


        this.asynchronousPublicationConfiguration = busConfigurations.get(AsynchronousPublicationConfiguration.class);
        this.exceptionHandlingConfiguration = busConfigurations.get(ExceptionHandlingConfiguration.class);
        this.busConfiguration = busConfigurations.get(BusConfiguration.class);

    }

    /**
     * @inheritDoc
     */
    @Override
    public void setupDispatchers() {
        for (int i = 0; i < asynchronousPublicationConfiguration.getDispatcherCount(); i++) {
            Thread thread = new Thread(() -> {
                while (!shutdown) {
                    try {
                        if (!resultQueue.isEmpty()) {
                            MessagePublicationResult<T> result = resultQueue.take();
                            result.dispatch();
                        }
                    } catch (Throwable t) {
                        if (!(asynchronousPublicationConfiguration.isSuppressDispatcherInterrupt() && t instanceof InterruptedException)) {
                            exceptionHandlingConfiguration.getExceptionHandler().handleException(t);
                        }
                    }
                }
            });
            thread.setName(String.format("(%s) Dispatch - %d", getIdentifier(), i));
            thread.start();
            dispatchers.add(thread);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void addMessage(MessagePublicationResult<T> result) {
        resultQueue.offer(result);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void addMessage(MessagePublicationResult<T> result, long timeout, TimeUnit timeUnit) {
        try {
            resultQueue.offer(result, timeout, timeUnit);
        } catch (InterruptedException e) {
            exceptionHandlingConfiguration.getExceptionHandler().handleException(e);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void shutdown(int delay, TimeUnit timeUnit) {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(() -> {
            shutdown = true;
            new Thread(() -> {
                while (!resultQueue.isEmpty()) ;

                for (Thread dispatcher : dispatchers) {
                    dispatcher.interrupt();
                }
            }, String.format("(%s) Shutdown", getIdentifier())).start();
        }, delay, timeUnit);
        scheduledExecutorService.shutdown();
    }

    @Override
    public void shutdown() {
        shutdown(250, TimeUnit.MILLISECONDS);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void forceShutdown() {
        shutdown = true;
        for (Thread dispatcher : dispatchers) {
            dispatcher.interrupt();
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public BusConfigurations getConfigurations() {
        return busConfigurations;
    }
}
