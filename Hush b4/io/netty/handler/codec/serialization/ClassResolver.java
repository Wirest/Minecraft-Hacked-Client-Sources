// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.serialization;

public interface ClassResolver
{
    Class<?> resolve(final String p0) throws ClassNotFoundException;
}
