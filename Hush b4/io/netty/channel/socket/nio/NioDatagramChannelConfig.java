// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.socket.nio;

import java.nio.channels.NetworkChannel;
import io.netty.channel.ChannelConfig;
import io.netty.util.internal.PlatformDependent;
import java.net.SocketException;
import io.netty.channel.ChannelException;
import java.util.Enumeration;
import java.net.NetworkInterface;
import java.net.InetAddress;
import io.netty.channel.socket.DatagramChannelConfig;
import java.nio.channels.DatagramChannel;
import java.lang.reflect.Method;
import io.netty.channel.socket.DefaultDatagramChannelConfig;

class NioDatagramChannelConfig extends DefaultDatagramChannelConfig
{
    private static final Object IP_MULTICAST_TTL;
    private static final Object IP_MULTICAST_IF;
    private static final Object IP_MULTICAST_LOOP;
    private static final Method GET_OPTION;
    private static final Method SET_OPTION;
    private final DatagramChannel javaChannel;
    
    NioDatagramChannelConfig(final NioDatagramChannel channel, final DatagramChannel javaChannel) {
        super(channel, javaChannel.socket());
        this.javaChannel = javaChannel;
    }
    
    @Override
    public int getTimeToLive() {
        return (int)this.getOption0(NioDatagramChannelConfig.IP_MULTICAST_TTL);
    }
    
    @Override
    public DatagramChannelConfig setTimeToLive(final int ttl) {
        this.setOption0(NioDatagramChannelConfig.IP_MULTICAST_TTL, ttl);
        return this;
    }
    
    @Override
    public InetAddress getInterface() {
        final NetworkInterface inf = this.getNetworkInterface();
        if (inf == null) {
            return null;
        }
        final Enumeration<InetAddress> addresses = inf.getInetAddresses();
        if (addresses.hasMoreElements()) {
            return addresses.nextElement();
        }
        return null;
    }
    
    @Override
    public DatagramChannelConfig setInterface(final InetAddress interfaceAddress) {
        try {
            this.setNetworkInterface(NetworkInterface.getByInetAddress(interfaceAddress));
        }
        catch (SocketException e) {
            throw new ChannelException(e);
        }
        return this;
    }
    
    @Override
    public NetworkInterface getNetworkInterface() {
        return (NetworkInterface)this.getOption0(NioDatagramChannelConfig.IP_MULTICAST_IF);
    }
    
    @Override
    public DatagramChannelConfig setNetworkInterface(final NetworkInterface networkInterface) {
        this.setOption0(NioDatagramChannelConfig.IP_MULTICAST_IF, networkInterface);
        return this;
    }
    
    @Override
    public boolean isLoopbackModeDisabled() {
        return (boolean)this.getOption0(NioDatagramChannelConfig.IP_MULTICAST_LOOP);
    }
    
    @Override
    public DatagramChannelConfig setLoopbackModeDisabled(final boolean loopbackModeDisabled) {
        this.setOption0(NioDatagramChannelConfig.IP_MULTICAST_LOOP, loopbackModeDisabled);
        return this;
    }
    
    @Override
    public DatagramChannelConfig setAutoRead(final boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }
    
    @Override
    protected void autoReadCleared() {
        ((NioDatagramChannel)this.channel).setReadPending(false);
    }
    
    private Object getOption0(final Object option) {
        if (PlatformDependent.javaVersion() < 7) {
            throw new UnsupportedOperationException();
        }
        try {
            return NioDatagramChannelConfig.GET_OPTION.invoke(this.javaChannel, option);
        }
        catch (Exception e) {
            throw new ChannelException(e);
        }
    }
    
    private void setOption0(final Object option, final Object value) {
        if (PlatformDependent.javaVersion() < 7) {
            throw new UnsupportedOperationException();
        }
        try {
            NioDatagramChannelConfig.SET_OPTION.invoke(this.javaChannel, option, value);
        }
        catch (Exception e) {
            throw new ChannelException(e);
        }
    }
    
    static {
        final ClassLoader classLoader = PlatformDependent.getClassLoader(DatagramChannel.class);
        Class<?> socketOptionType = null;
        try {
            socketOptionType = Class.forName("java.net.SocketOption", true, classLoader);
        }
        catch (Exception ex) {}
        Class<?> stdSocketOptionType = null;
        try {
            stdSocketOptionType = Class.forName("java.net.StandardSocketOptions", true, classLoader);
        }
        catch (Exception ex2) {}
        Object ipMulticastTtl = null;
        Object ipMulticastIf = null;
        Object ipMulticastLoop = null;
        Method getOption = null;
        Method setOption = null;
        if (socketOptionType != null) {
            try {
                ipMulticastTtl = stdSocketOptionType.getDeclaredField("IP_MULTICAST_TTL").get(null);
            }
            catch (Exception e) {
                throw new Error("cannot locate the IP_MULTICAST_TTL field", e);
            }
            try {
                ipMulticastIf = stdSocketOptionType.getDeclaredField("IP_MULTICAST_IF").get(null);
            }
            catch (Exception e) {
                throw new Error("cannot locate the IP_MULTICAST_IF field", e);
            }
            try {
                ipMulticastLoop = stdSocketOptionType.getDeclaredField("IP_MULTICAST_LOOP").get(null);
            }
            catch (Exception e) {
                throw new Error("cannot locate the IP_MULTICAST_LOOP field", e);
            }
            try {
                getOption = NetworkChannel.class.getDeclaredMethod("getOption", socketOptionType);
            }
            catch (Exception e) {
                throw new Error("cannot locate the getOption() method", e);
            }
            try {
                setOption = NetworkChannel.class.getDeclaredMethod("setOption", socketOptionType, Object.class);
            }
            catch (Exception e) {
                throw new Error("cannot locate the setOption() method", e);
            }
        }
        IP_MULTICAST_TTL = ipMulticastTtl;
        IP_MULTICAST_IF = ipMulticastIf;
        IP_MULTICAST_LOOP = ipMulticastLoop;
        GET_OPTION = getOption;
        SET_OPTION = setOption;
    }
}
