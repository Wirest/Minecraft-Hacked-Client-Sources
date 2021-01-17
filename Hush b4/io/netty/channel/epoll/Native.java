// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.epoll;

import io.netty.util.internal.NativeLibraryLoader;
import io.netty.util.internal.PlatformDependent;
import java.util.Locale;
import io.netty.util.internal.SystemPropertyUtil;
import java.net.InetSocketAddress;
import io.netty.channel.ChannelException;
import java.net.Inet6Address;
import java.net.InetAddress;
import io.netty.channel.DefaultFileRegion;
import java.nio.ByteBuffer;
import java.io.IOException;

final class Native
{
    private static final byte[] IPV4_MAPPED_IPV6_PREFIX;
    public static final int EPOLLIN = 1;
    public static final int EPOLLOUT = 2;
    public static final int EPOLLACCEPT = 4;
    public static final int EPOLLRDHUP = 8;
    public static final int IOV_MAX;
    
    public static native int eventFd();
    
    public static native void eventFdWrite(final int p0, final long p1);
    
    public static native void eventFdRead(final int p0);
    
    public static native int epollCreate();
    
    public static native int epollWait(final int p0, final long[] p1, final int p2);
    
    public static native void epollCtlAdd(final int p0, final int p1, final int p2, final int p3);
    
    public static native void epollCtlMod(final int p0, final int p1, final int p2, final int p3);
    
    public static native void epollCtlDel(final int p0, final int p1);
    
    public static native void close(final int p0) throws IOException;
    
    public static native int write(final int p0, final ByteBuffer p1, final int p2, final int p3) throws IOException;
    
    public static native int writeAddress(final int p0, final long p1, final int p2, final int p3) throws IOException;
    
    public static native long writev(final int p0, final ByteBuffer[] p1, final int p2, final int p3) throws IOException;
    
    public static native long writevAddresses(final int p0, final long p1, final int p2) throws IOException;
    
    public static native int read(final int p0, final ByteBuffer p1, final int p2, final int p3) throws IOException;
    
    public static native int readAddress(final int p0, final long p1, final int p2, final int p3) throws IOException;
    
    public static native long sendfile(final int p0, final DefaultFileRegion p1, final long p2, final long p3, final long p4) throws IOException;
    
    public static int sendTo(final int fd, final ByteBuffer buf, final int pos, final int limit, final InetAddress addr, final int port) throws IOException {
        byte[] address;
        int scopeId;
        if (addr instanceof Inet6Address) {
            address = addr.getAddress();
            scopeId = ((Inet6Address)addr).getScopeId();
        }
        else {
            scopeId = 0;
            address = ipv4MappedIpv6Address(addr.getAddress());
        }
        return sendTo(fd, buf, pos, limit, address, scopeId, port);
    }
    
    private static native int sendTo(final int p0, final ByteBuffer p1, final int p2, final int p3, final byte[] p4, final int p5, final int p6) throws IOException;
    
    public static int sendToAddress(final int fd, final long memoryAddress, final int pos, final int limit, final InetAddress addr, final int port) throws IOException {
        byte[] address;
        int scopeId;
        if (addr instanceof Inet6Address) {
            address = addr.getAddress();
            scopeId = ((Inet6Address)addr).getScopeId();
        }
        else {
            scopeId = 0;
            address = ipv4MappedIpv6Address(addr.getAddress());
        }
        return sendToAddress(fd, memoryAddress, pos, limit, address, scopeId, port);
    }
    
    private static native int sendToAddress(final int p0, final long p1, final int p2, final int p3, final byte[] p4, final int p5, final int p6) throws IOException;
    
    public static native EpollDatagramChannel.DatagramSocketAddress recvFrom(final int p0, final ByteBuffer p1, final int p2, final int p3) throws IOException;
    
    public static native EpollDatagramChannel.DatagramSocketAddress recvFromAddress(final int p0, final long p1, final int p2, final int p3) throws IOException;
    
    public static int socketStreamFd() {
        try {
            return socketStream();
        }
        catch (IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public static int socketDgramFd() {
        try {
            return socketDgram();
        }
        catch (IOException e) {
            throw new ChannelException(e);
        }
    }
    
    private static native int socketStream() throws IOException;
    
    private static native int socketDgram() throws IOException;
    
    public static void bind(final int fd, final InetAddress addr, final int port) throws IOException {
        final NativeInetAddress address = toNativeInetAddress(addr);
        bind(fd, address.address, address.scopeId, port);
    }
    
    private static byte[] ipv4MappedIpv6Address(final byte[] ipv4) {
        final byte[] address = new byte[16];
        System.arraycopy(Native.IPV4_MAPPED_IPV6_PREFIX, 0, address, 0, Native.IPV4_MAPPED_IPV6_PREFIX.length);
        System.arraycopy(ipv4, 0, address, 12, ipv4.length);
        return address;
    }
    
    public static native void bind(final int p0, final byte[] p1, final int p2, final int p3) throws IOException;
    
    public static native void listen(final int p0, final int p1) throws IOException;
    
    public static boolean connect(final int fd, final InetAddress addr, final int port) throws IOException {
        final NativeInetAddress address = toNativeInetAddress(addr);
        return connect(fd, address.address, address.scopeId, port);
    }
    
    public static native boolean connect(final int p0, final byte[] p1, final int p2, final int p3) throws IOException;
    
    public static native boolean finishConnect(final int p0) throws IOException;
    
    public static native InetSocketAddress remoteAddress(final int p0);
    
    public static native InetSocketAddress localAddress(final int p0);
    
    public static native int accept(final int p0) throws IOException;
    
    public static native void shutdown(final int p0, final boolean p1, final boolean p2) throws IOException;
    
    public static native int getReceiveBufferSize(final int p0);
    
    public static native int getSendBufferSize(final int p0);
    
    public static native int isKeepAlive(final int p0);
    
    public static native int isReuseAddress(final int p0);
    
    public static native int isReusePort(final int p0);
    
    public static native int isTcpNoDelay(final int p0);
    
    public static native int isTcpCork(final int p0);
    
    public static native int getSoLinger(final int p0);
    
    public static native int getTrafficClass(final int p0);
    
    public static native int isBroadcast(final int p0);
    
    public static native int getTcpKeepIdle(final int p0);
    
    public static native int getTcpKeepIntvl(final int p0);
    
    public static native int getTcpKeepCnt(final int p0);
    
    public static native void setKeepAlive(final int p0, final int p1);
    
    public static native void setReceiveBufferSize(final int p0, final int p1);
    
    public static native void setReuseAddress(final int p0, final int p1);
    
    public static native void setReusePort(final int p0, final int p1);
    
    public static native void setSendBufferSize(final int p0, final int p1);
    
    public static native void setTcpNoDelay(final int p0, final int p1);
    
    public static native void setTcpCork(final int p0, final int p1);
    
    public static native void setSoLinger(final int p0, final int p1);
    
    public static native void setTrafficClass(final int p0, final int p1);
    
    public static native void setBroadcast(final int p0, final int p1);
    
    public static native void setTcpKeepIdle(final int p0, final int p1);
    
    public static native void setTcpKeepIntvl(final int p0, final int p1);
    
    public static native void setTcpKeepCnt(final int p0, final int p1);
    
    private static NativeInetAddress toNativeInetAddress(final InetAddress addr) {
        final byte[] bytes = addr.getAddress();
        if (addr instanceof Inet6Address) {
            return new NativeInetAddress(bytes, ((Inet6Address)addr).getScopeId());
        }
        return new NativeInetAddress(ipv4MappedIpv6Address(bytes));
    }
    
    public static native String kernelVersion();
    
    private static native int iovMax();
    
    private Native() {
    }
    
    static {
        IPV4_MAPPED_IPV6_PREFIX = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1 };
        final String name = SystemPropertyUtil.get("os.name").toLowerCase(Locale.UK).trim();
        if (!name.startsWith("linux")) {
            throw new IllegalStateException("Only supported on Linux");
        }
        NativeLibraryLoader.load("netty-transport-native-epoll", PlatformDependent.getClassLoader(Native.class));
        IOV_MAX = iovMax();
    }
    
    private static class NativeInetAddress
    {
        final byte[] address;
        final int scopeId;
        
        NativeInetAddress(final byte[] address, final int scopeId) {
            this.address = address;
            this.scopeId = scopeId;
        }
        
        NativeInetAddress(final byte[] address) {
            this(address, 0);
        }
    }
}
