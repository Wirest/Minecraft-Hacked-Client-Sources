// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.eventbus;

import com.google.common.base.Preconditions;
import java.lang.reflect.Method;

public class SubscriberExceptionContext
{
    private final EventBus eventBus;
    private final Object event;
    private final Object subscriber;
    private final Method subscriberMethod;
    
    SubscriberExceptionContext(final EventBus eventBus, final Object event, final Object subscriber, final Method subscriberMethod) {
        this.eventBus = Preconditions.checkNotNull(eventBus);
        this.event = Preconditions.checkNotNull(event);
        this.subscriber = Preconditions.checkNotNull(subscriber);
        this.subscriberMethod = Preconditions.checkNotNull(subscriberMethod);
    }
    
    public EventBus getEventBus() {
        return this.eventBus;
    }
    
    public Object getEvent() {
        return this.event;
    }
    
    public Object getSubscriber() {
        return this.subscriber;
    }
    
    public Method getSubscriberMethod() {
        return this.subscriberMethod;
    }
}
