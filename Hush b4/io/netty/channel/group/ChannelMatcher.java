// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.group;

import io.netty.channel.Channel;

public interface ChannelMatcher
{
    boolean matches(final Channel p0);
}
