// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.udt;

import java.net.InetSocketAddress;
import io.netty.channel.Channel;

public interface UdtChannel extends Channel
{
    UdtChannelConfig config();
    
    InetSocketAddress localAddress();
    
    InetSocketAddress remoteAddress();
}
