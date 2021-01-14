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
import me.hippo.api.lwjeb.message.handler.impl.FieldBasedMessageHandler;
import me.hippo.api.lwjeb.message.scan.MessageScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Hippo
 * @version 5.0.0, 1/12/20
 * @since 5.0.0
 * <p>
 * This is a field based implementation of the message scanner, it just searches for fields.
 */
public final class FieldBasedMessageScanner<T> implements MessageScanner<T> {

    /**
     * @inheritDoc
     */
    @SuppressWarnings("all")
    @Override
    public List<MessageHandler<T>> scan(Object parent, SubscribeMessageBus messageBus) {
        List<MessageHandler<T>> messageHandlers = new ArrayList<>();
        ExceptionHandlingConfiguration exceptionHandlingConfiguration = messageBus.getConfigurations().get(ExceptionHandlingConfiguration.class);

        for (Field field : parent.getClass().getDeclaredFields()) {
            try {
                if (Consumer.class.isAssignableFrom(field.getType()) && field.isAnnotationPresent(Handler.class)) {
                    Consumer listenerConsumer = (Consumer) field.get(parent);
                    Class<?> type = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    Filter handlerFilter = field.getDeclaredAnnotation(Filter.class);
                    Wrapped wrappedType = field.getDeclaredAnnotation(Wrapped.class);
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
                            messageHandlers.add(new FieldBasedMessageHandler<>(acceptedType, filter, listenerConsumer, true));
                        }
                    } else {
                        messageHandlers.add(new FieldBasedMessageHandler(type, filter, listenerConsumer, false));
                    }
                }
            } catch (ReflectiveOperationException e) {
                exceptionHandlingConfiguration.getExceptionHandler().handleException(e);
            }
        }

        return messageHandlers;
    }
}
