// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.eventbus;

import com.google.common.reflect.TypeToken;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheBuilder;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.common.base.Throwables;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator;
import java.util.Collection;
import java.util.Map;
import com.google.common.collect.Multimap;
import com.google.common.base.Preconditions;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.google.common.collect.HashMultimap;
import java.util.Queue;
import java.util.concurrent.locks.ReadWriteLock;
import com.google.common.collect.SetMultimap;
import java.util.Set;
import com.google.common.cache.LoadingCache;
import com.google.common.annotations.Beta;

@Beta
public class EventBus
{
    private static final LoadingCache<Class<?>, Set<Class<?>>> flattenHierarchyCache;
    private final SetMultimap<Class<?>, EventSubscriber> subscribersByType;
    private final ReadWriteLock subscribersByTypeLock;
    private final SubscriberFindingStrategy finder;
    private final ThreadLocal<Queue<EventWithSubscriber>> eventsToDispatch;
    private final ThreadLocal<Boolean> isDispatching;
    private SubscriberExceptionHandler subscriberExceptionHandler;
    
    public EventBus() {
        this("default");
    }
    
    public EventBus(final String identifier) {
        this(new LoggingSubscriberExceptionHandler(identifier));
    }
    
    public EventBus(final SubscriberExceptionHandler subscriberExceptionHandler) {
        this.subscribersByType = (SetMultimap<Class<?>, EventSubscriber>)HashMultimap.create();
        this.subscribersByTypeLock = new ReentrantReadWriteLock();
        this.finder = new AnnotatedSubscriberFinder();
        this.eventsToDispatch = new ThreadLocal<Queue<EventWithSubscriber>>() {
            @Override
            protected Queue<EventWithSubscriber> initialValue() {
                return new LinkedList<EventWithSubscriber>();
            }
        };
        this.isDispatching = new ThreadLocal<Boolean>() {
            @Override
            protected Boolean initialValue() {
                return false;
            }
        };
        this.subscriberExceptionHandler = Preconditions.checkNotNull(subscriberExceptionHandler);
    }
    
    public void register(final Object object) {
        final Multimap<Class<?>, EventSubscriber> methodsInListener = this.finder.findAllSubscribers(object);
        this.subscribersByTypeLock.writeLock().lock();
        try {
            this.subscribersByType.putAll((Multimap<?, ?>)methodsInListener);
        }
        finally {
            this.subscribersByTypeLock.writeLock().unlock();
        }
    }
    
    public void unregister(final Object object) {
        final Multimap<Class<?>, EventSubscriber> methodsInListener = this.finder.findAllSubscribers(object);
        for (final Map.Entry<Class<?>, Collection<EventSubscriber>> entry : methodsInListener.asMap().entrySet()) {
            final Class<?> eventType = entry.getKey();
            final Collection<EventSubscriber> eventMethodsInListener = entry.getValue();
            this.subscribersByTypeLock.writeLock().lock();
            try {
                final Set<EventSubscriber> currentSubscribers = this.subscribersByType.get(eventType);
                if (!currentSubscribers.containsAll(eventMethodsInListener)) {
                    throw new IllegalArgumentException("missing event subscriber for an annotated method. Is " + object + " registered?");
                }
                currentSubscribers.removeAll(eventMethodsInListener);
            }
            finally {
                this.subscribersByTypeLock.writeLock().unlock();
            }
        }
    }
    
    public void post(final Object event) {
        final Set<Class<?>> dispatchTypes = this.flattenHierarchy(event.getClass());
        boolean dispatched = false;
        for (final Class<?> eventType : dispatchTypes) {
            this.subscribersByTypeLock.readLock().lock();
            try {
                final Set<EventSubscriber> wrappers = this.subscribersByType.get(eventType);
                if (wrappers.isEmpty()) {
                    continue;
                }
                dispatched = true;
                for (final EventSubscriber wrapper : wrappers) {
                    this.enqueueEvent(event, wrapper);
                }
            }
            finally {
                this.subscribersByTypeLock.readLock().unlock();
            }
        }
        if (!dispatched && !(event instanceof DeadEvent)) {
            this.post(new DeadEvent(this, event));
        }
        this.dispatchQueuedEvents();
    }
    
    void enqueueEvent(final Object event, final EventSubscriber subscriber) {
        this.eventsToDispatch.get().offer(new EventWithSubscriber(event, subscriber));
    }
    
    void dispatchQueuedEvents() {
        if (this.isDispatching.get()) {
            return;
        }
        this.isDispatching.set(true);
        try {
            final Queue<EventWithSubscriber> events = this.eventsToDispatch.get();
            EventWithSubscriber eventWithSubscriber;
            while ((eventWithSubscriber = events.poll()) != null) {
                this.dispatch(eventWithSubscriber.event, eventWithSubscriber.subscriber);
            }
        }
        finally {
            this.isDispatching.remove();
            this.eventsToDispatch.remove();
        }
    }
    
    void dispatch(final Object event, final EventSubscriber wrapper) {
        try {
            wrapper.handleEvent(event);
        }
        catch (InvocationTargetException e) {
            try {
                this.subscriberExceptionHandler.handleException(e.getCause(), new SubscriberExceptionContext(this, event, wrapper.getSubscriber(), wrapper.getMethod()));
            }
            catch (Throwable t) {
                Logger.getLogger(EventBus.class.getName()).log(Level.SEVERE, String.format("Exception %s thrown while handling exception: %s", t, e.getCause()), t);
            }
        }
    }
    
    @VisibleForTesting
    Set<Class<?>> flattenHierarchy(final Class<?> concreteClass) {
        try {
            return EventBus.flattenHierarchyCache.getUnchecked(concreteClass);
        }
        catch (UncheckedExecutionException e) {
            throw Throwables.propagate(e.getCause());
        }
    }
    
    static {
        flattenHierarchyCache = CacheBuilder.newBuilder().weakKeys().build((CacheLoader<? super Class<?>, Set<Class<?>>>)new CacheLoader<Class<?>, Set<Class<?>>>() {
            @Override
            public Set<Class<?>> load(final Class<?> concreteClass) {
                return (Set<Class<?>>)TypeToken.of(concreteClass).getTypes().rawTypes();
            }
        });
    }
    
    private static final class LoggingSubscriberExceptionHandler implements SubscriberExceptionHandler
    {
        private final Logger logger;
        
        public LoggingSubscriberExceptionHandler(final String identifier) {
            this.logger = Logger.getLogger(EventBus.class.getName() + "." + Preconditions.checkNotNull(identifier));
        }
        
        @Override
        public void handleException(final Throwable exception, final SubscriberExceptionContext context) {
            this.logger.log(Level.SEVERE, "Could not dispatch event: " + context.getSubscriber() + " to " + context.getSubscriberMethod(), exception.getCause());
        }
    }
    
    static class EventWithSubscriber
    {
        final Object event;
        final EventSubscriber subscriber;
        
        public EventWithSubscriber(final Object event, final EventSubscriber subscriber) {
            this.event = Preconditions.checkNotNull(event);
            this.subscriber = Preconditions.checkNotNull(subscriber);
        }
    }
}
