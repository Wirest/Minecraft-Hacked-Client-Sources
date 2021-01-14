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

import me.hippo.api.lwjeb.annotation.Filter;
import me.hippo.api.lwjeb.annotation.Handler;
import me.hippo.api.lwjeb.annotation.Wrapped;
import me.hippo.api.lwjeb.bus.subscribe.SubscribeMessageBus;
import me.hippo.api.lwjeb.configuration.config.impl.ExceptionHandlingConfiguration;
import me.hippo.api.lwjeb.filter.MessageFilter;
import me.hippo.api.lwjeb.message.handler.MessageHandler;
import me.hippo.api.lwjeb.message.handler.impl.MethodBasedMessageHandler;
import me.hippo.api.lwjeb.message.scan.MessageScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hippo
 * @version 5.0.0, 11/2/19
 * @since 5.0.0
 * <p>
 * This is a method based implementation of the message scanner, it just searches for methods.
 */
public final class MethodBasedMessageScanner<T> implements MessageScanner<T> {

    /**
     * @inheritDoc
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<MessageHandler<T>> scan(Object parent, SubscribeMessageBus messageBus) {
        List<MessageHandler<T>> handlers = new ArrayList<>();

        ExceptionHandlingConfiguration exceptionHandlingConfiguration = messageBus.getConfigurations().get(ExceptionHandlingConfiguration.class);

        for (Method method : parent.getClass().getDeclaredMethods()) {
            try {
                if (method.isAnnotationPresent(Handler.class) && method.getParameterCount() == 1) {
                    Class<?> type = method.getParameters()[0].getType();
                    Filter handlerFilter = method.getDeclaredAnnotation(Filter.class);
                    Wrapped wrappedType = method.getDeclaredAnnotation(Wrapped.class);
                    MessageFilter[] filter = new MessageFilter[0];


                    if (handlerFilter != null) {
                        filter = new MessageFilter[handlerFilter.value().length];
                        for (int i = 0; i < filter.length; i++) {

                            Constructor<? extends MessageFilter> constructor = handlerFilter.value()[i].getDeclaredConstructor();
                            constructor.setAccessible(true);
                            filter[i] = constructor.newInstance();
                        }
                    }
                    if (wrappedType != null) {
                        for (Class<?> acceptedType : wrappedType.value()) {

                            handlers.add(new MethodBasedMessageHandler<>(parent, acceptedType, method, filter, exceptionHandlingConfiguration, true));
                        }
                    } else {
                        handlers.add(new MethodBasedMessageHandler(parent, type, method, filter, exceptionHandlingConfiguration, false));
                    }
                }
            } catch (ReflectiveOperationException e) {
                exceptionHandlingConfiguration.getExceptionHandler().handleException(e);
            }
        }
        return handlers;
    }
}
