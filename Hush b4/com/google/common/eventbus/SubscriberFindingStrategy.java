// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.eventbus;

import com.google.common.collect.Multimap;

interface SubscriberFindingStrategy
{
    Multimap<Class<?>, EventSubscriber> findAllSubscribers(final Object p0);
}
