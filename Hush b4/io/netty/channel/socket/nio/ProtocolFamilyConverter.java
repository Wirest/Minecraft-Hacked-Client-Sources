// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.socket.nio;

import java.net.StandardProtocolFamily;
import java.net.ProtocolFamily;
import io.netty.channel.socket.InternetProtocolFamily;

final class ProtocolFamilyConverter
{
    private ProtocolFamilyConverter() {
    }
    
    public static ProtocolFamily convert(final InternetProtocolFamily family) {
        switch (family) {
            case IPv4: {
                return StandardProtocolFamily.INET;
            }
            case IPv6: {
                return StandardProtocolFamily.INET6;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
}
