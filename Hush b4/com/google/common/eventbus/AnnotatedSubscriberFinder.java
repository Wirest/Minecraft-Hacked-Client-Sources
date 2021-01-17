// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.eventbus;

import javax.annotation.Nullable;
import com.google.common.base.Objects;
import java.util.Arrays;
import java.util.List;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheBuilder;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.lang.annotation.Annotation;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.common.base.Throwables;
import java.util.Iterator;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.lang.reflect.Method;
import com.google.common.collect.ImmutableList;
import com.google.common.cache.LoadingCache;

class AnnotatedSubscriberFinder implements SubscriberFindingStrategy
{
    private static final LoadingCache<Class<?>, ImmutableList<Method>> subscriberMethodsCache;
    
    @Override
    public Multimap<Class<?>, EventSubscriber> findAllSubscribers(final Object listener) {
        final Multimap<Class<?>, EventSubscriber> methodsInListener = (Multimap<Class<?>, EventSubscriber>)HashMultimap.create();
        final Class<?> clazz = listener.getClass();
        for (final Method method : getAnnotatedMethods(clazz)) {
            final Class<?>[] parameterTypes = method.getParameterTypes();
            final Class<?> eventType = parameterTypes[0];
            final EventSubscriber subscriber = makeSubscriber(listener, method);
            methodsInListener.put(eventType, subscriber);
        }
        return methodsInListener;
    }
    
    private static ImmutableList<Method> getAnnotatedMethods(final Class<?> clazz) {
        try {
            return AnnotatedSubscriberFinder.subscriberMethodsCache.getUnchecked(clazz);
        }
        catch (UncheckedExecutionException e) {
            throw Throwables.propagate(e.getCause());
        }
    }
    
    private static ImmutableList<Method> getAnnotatedMethodsInternal(final Class<?> clazz) {
        final Set<? extends Class<?>> supers = TypeToken.of(clazz).getTypes().rawTypes();
        final Map<MethodIdentifier, Method> identifiers = (Map<MethodIdentifier, Method>)Maps.newHashMap();
        for (final Class<?> superClazz : supers) {
            for (final Method superClazzMethod : superClazz.getMethods()) {
                if (superClazzMethod.isAnnotationPresent(Subscribe.class)) {
                    final Class<?>[] parameterTypes = superClazzMethod.getParameterTypes();
                    if (parameterTypes.length != 1) {
                        throw new IllegalArgumentException("Method " + superClazzMethod + " has @Subscribe annotation, but requires " + parameterTypes.length + " arguments.  Event subscriber methods must require a single argument.");
                    }
                    final MethodIdentifier ident = new MethodIdentifier(superClazzMethod);
                    if (!identifiers.containsKey(ident)) {
                        identifiers.put(ident, superClazzMethod);
                    }
                }
            }
        }
        return ImmutableList.copyOf((Collection<? extends Method>)identifiers.values());
    }
    
    private static EventSubscriber makeSubscriber(final Object listener, final Method method) {
        EventSubscriber wrapper;
        if (methodIsDeclaredThreadSafe(method)) {
            wrapper = new EventSubscriber(listener, method);
        }
        else {
            wrapper = new SynchronizedEventSubscriber(listener, method);
        }
        return wrapper;
    }
    
    private static boolean methodIsDeclaredThreadSafe(final Method method) {
        return method.getAnnotation(AllowConcurrentEvents.class) != null;
    }
    
    static {
        subscriberMethodsCache = CacheBuilder.newBuilder().weakKeys().build((CacheLoader<? super Class<?>, ImmutableList<Method>>)new CacheLoader<Class<?>, ImmutableList<Method>>() {
            @Override
            public ImmutableList<Method> load(final Class<?> concreteClass) throws Exception {
                return getAnnotatedMethodsInternal(concreteClass);
            }
        });
    }
    
    private static final class MethodIdentifier
    {
        private final String name;
        private final List<Class<?>> parameterTypes;
        
        MethodIdentifier(final Method method) {
            this.name = method.getName();
            this.parameterTypes = Arrays.asList(method.getParameterTypes());
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(this.name, this.parameterTypes);
        }
        
        @Override
        public boolean equals(@Nullable final Object o) {
            if (o instanceof MethodIdentifier) {
                final MethodIdentifier ident = (MethodIdentifier)o;
                return this.name.equals(ident.name) && this.parameterTypes.equals(ident.parameterTypes);
            }
            return false;
        }
    }
}
