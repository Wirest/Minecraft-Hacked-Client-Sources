/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.FixedRecvByteBufAllocator;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.socket.DatagramChannelConfig;
/*     */ import io.netty.channel.unix.FileDescriptor;
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class EpollDatagramChannelConfig
/*     */   extends EpollChannelConfig
/*     */   implements DatagramChannelConfig
/*     */ {
/*  30 */   private static final RecvByteBufAllocator DEFAULT_RCVBUF_ALLOCATOR = new FixedRecvByteBufAllocator(2048);
/*     */   private final EpollDatagramChannel datagramChannel;
/*     */   private boolean activeOnOpen;
/*     */   
/*     */   EpollDatagramChannelConfig(EpollDatagramChannel channel) {
/*  35 */     super(channel);
/*  36 */     this.datagramChannel = channel;
/*  37 */     setRecvByteBufAllocator(DEFAULT_RCVBUF_ALLOCATOR);
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<ChannelOption<?>, Object> getOptions()
/*     */   {
/*  43 */     return getOptions(super.getOptions(), new ChannelOption[] { ChannelOption.SO_BROADCAST, ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.SO_REUSEADDR, ChannelOption.IP_MULTICAST_LOOP_DISABLED, ChannelOption.IP_MULTICAST_ADDR, ChannelOption.IP_MULTICAST_IF, ChannelOption.IP_MULTICAST_TTL, ChannelOption.IP_TOS, ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION, EpollChannelOption.SO_REUSEPORT });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T> T getOption(ChannelOption<T> option)
/*     */   {
/*  55 */     if (option == ChannelOption.SO_BROADCAST) {
/*  56 */       return Boolean.valueOf(isBroadcast());
/*     */     }
/*  58 */     if (option == ChannelOption.SO_RCVBUF) {
/*  59 */       return Integer.valueOf(getReceiveBufferSize());
/*     */     }
/*  61 */     if (option == ChannelOption.SO_SNDBUF) {
/*  62 */       return Integer.valueOf(getSendBufferSize());
/*     */     }
/*  64 */     if (option == ChannelOption.SO_REUSEADDR) {
/*  65 */       return Boolean.valueOf(isReuseAddress());
/*     */     }
/*  67 */     if (option == ChannelOption.IP_MULTICAST_LOOP_DISABLED) {
/*  68 */       return Boolean.valueOf(isLoopbackModeDisabled());
/*     */     }
/*  70 */     if (option == ChannelOption.IP_MULTICAST_ADDR) {
/*  71 */       return getInterface();
/*     */     }
/*  73 */     if (option == ChannelOption.IP_MULTICAST_IF) {
/*  74 */       return getNetworkInterface();
/*     */     }
/*  76 */     if (option == ChannelOption.IP_MULTICAST_TTL) {
/*  77 */       return Integer.valueOf(getTimeToLive());
/*     */     }
/*  79 */     if (option == ChannelOption.IP_TOS) {
/*  80 */       return Integer.valueOf(getTrafficClass());
/*     */     }
/*  82 */     if (option == ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) {
/*  83 */       return Boolean.valueOf(this.activeOnOpen);
/*     */     }
/*  85 */     if (option == EpollChannelOption.SO_REUSEPORT) {
/*  86 */       return Boolean.valueOf(isReusePort());
/*     */     }
/*  88 */     return (T)super.getOption(option);
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value)
/*     */   {
/*  94 */     validate(option, value);
/*     */     
/*  96 */     if (option == ChannelOption.SO_BROADCAST) {
/*  97 */       setBroadcast(((Boolean)value).booleanValue());
/*  98 */     } else if (option == ChannelOption.SO_RCVBUF) {
/*  99 */       setReceiveBufferSize(((Integer)value).intValue());
/* 100 */     } else if (option == ChannelOption.SO_SNDBUF) {
/* 101 */       setSendBufferSize(((Integer)value).intValue());
/* 102 */     } else if (option == ChannelOption.SO_REUSEADDR) {
/* 103 */       setReuseAddress(((Boolean)value).booleanValue());
/* 104 */     } else if (option == ChannelOption.IP_MULTICAST_LOOP_DISABLED) {
/* 105 */       setLoopbackModeDisabled(((Boolean)value).booleanValue());
/* 106 */     } else if (option == ChannelOption.IP_MULTICAST_ADDR) {
/* 107 */       setInterface((InetAddress)value);
/* 108 */     } else if (option == ChannelOption.IP_MULTICAST_IF) {
/* 109 */       setNetworkInterface((NetworkInterface)value);
/* 110 */     } else if (option == ChannelOption.IP_MULTICAST_TTL) {
/* 111 */       setTimeToLive(((Integer)value).intValue());
/* 112 */     } else if (option == ChannelOption.IP_TOS) {
/* 113 */       setTrafficClass(((Integer)value).intValue());
/* 114 */     } else if (option == ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) {
/* 115 */       setActiveOnOpen(((Boolean)value).booleanValue());
/* 116 */     } else if (option == EpollChannelOption.SO_REUSEPORT) {
/* 117 */       setReusePort(((Boolean)value).booleanValue());
/*     */     } else {
/* 119 */       return super.setOption(option, value);
/*     */     }
/*     */     
/* 122 */     return true;
/*     */   }
/*     */   
/*     */   private void setActiveOnOpen(boolean activeOnOpen) {
/* 126 */     if (this.channel.isRegistered()) {
/* 127 */       throw new IllegalStateException("Can only changed before channel was registered");
/*     */     }
/* 129 */     this.activeOnOpen = activeOnOpen;
/*     */   }
/*     */   
/*     */   public EpollDatagramChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator)
/*     */   {
/* 134 */     super.setMessageSizeEstimator(estimator);
/* 135 */     return this;
/*     */   }
/*     */   
/*     */   public EpollDatagramChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark)
/*     */   {
/* 140 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 141 */     return this;
/*     */   }
/*     */   
/*     */   public EpollDatagramChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark)
/*     */   {
/* 146 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 147 */     return this;
/*     */   }
/*     */   
/*     */   public EpollDatagramChannelConfig setAutoRead(boolean autoRead)
/*     */   {
/* 152 */     super.setAutoRead(autoRead);
/* 153 */     return this;
/*     */   }
/*     */   
/*     */   public EpollDatagramChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator)
/*     */   {
/* 158 */     super.setRecvByteBufAllocator(allocator);
/* 159 */     return this;
/*     */   }
/*     */   
/*     */   public EpollDatagramChannelConfig setWriteSpinCount(int writeSpinCount)
/*     */   {
/* 164 */     super.setWriteSpinCount(writeSpinCount);
/* 165 */     return this;
/*     */   }
/*     */   
/*     */   public EpollDatagramChannelConfig setAllocator(ByteBufAllocator allocator)
/*     */   {
/* 170 */     super.setAllocator(allocator);
/* 171 */     return this;
/*     */   }
/*     */   
/*     */   public EpollDatagramChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis)
/*     */   {
/* 176 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 177 */     return this;
/*     */   }
/*     */   
/*     */   public EpollDatagramChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead)
/*     */   {
/* 182 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 183 */     return this;
/*     */   }
/*     */   
/*     */   public int getSendBufferSize()
/*     */   {
/* 188 */     return Native.getSendBufferSize(this.datagramChannel.fd().intValue());
/*     */   }
/*     */   
/*     */   public EpollDatagramChannelConfig setSendBufferSize(int sendBufferSize)
/*     */   {
/* 193 */     Native.setSendBufferSize(this.datagramChannel.fd().intValue(), sendBufferSize);
/* 194 */     return this;
/*     */   }
/*     */   
/*     */   public int getReceiveBufferSize()
/*     */   {
/* 199 */     return Native.getReceiveBufferSize(this.datagramChannel.fd().intValue());
/*     */   }
/*     */   
/*     */   public EpollDatagramChannelConfig setReceiveBufferSize(int receiveBufferSize)
/*     */   {
/* 204 */     Native.setReceiveBufferSize(this.datagramChannel.fd().intValue(), receiveBufferSize);
/* 205 */     return this;
/*     */   }
/*     */   
/*     */   public int getTrafficClass()
/*     */   {
/* 210 */     return Native.getTrafficClass(this.datagramChannel.fd().intValue());
/*     */   }
/*     */   
/*     */   public EpollDatagramChannelConfig setTrafficClass(int trafficClass)
/*     */   {
/* 215 */     Native.setTrafficClass(this.datagramChannel.fd().intValue(), trafficClass);
/* 216 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isReuseAddress()
/*     */   {
/* 221 */     return Native.isReuseAddress(this.datagramChannel.fd().intValue()) == 1;
/*     */   }
/*     */   
/*     */   public EpollDatagramChannelConfig setReuseAddress(boolean reuseAddress)
/*     */   {
/* 226 */     Native.setReuseAddress(this.datagramChannel.fd().intValue(), reuseAddress ? 1 : 0);
/* 227 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isBroadcast()
/*     */   {
/* 232 */     return Native.isBroadcast(this.datagramChannel.fd().intValue()) == 1;
/*     */   }
/*     */   
/*     */   public EpollDatagramChannelConfig setBroadcast(boolean broadcast)
/*     */   {
/* 237 */     Native.setBroadcast(this.datagramChannel.fd().intValue(), broadcast ? 1 : 0);
/* 238 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isLoopbackModeDisabled()
/*     */   {
/* 243 */     return false;
/*     */   }
/*     */   
/*     */   public DatagramChannelConfig setLoopbackModeDisabled(boolean loopbackModeDisabled)
/*     */   {
/* 248 */     throw new UnsupportedOperationException("Multicast not supported");
/*     */   }
/*     */   
/*     */   public int getTimeToLive()
/*     */   {
/* 253 */     return -1;
/*     */   }
/*     */   
/*     */   public EpollDatagramChannelConfig setTimeToLive(int ttl)
/*     */   {
/* 258 */     throw new UnsupportedOperationException("Multicast not supported");
/*     */   }
/*     */   
/*     */   public InetAddress getInterface()
/*     */   {
/* 263 */     return null;
/*     */   }
/*     */   
/*     */   public EpollDatagramChannelConfig setInterface(InetAddress interfaceAddress)
/*     */   {
/* 268 */     throw new UnsupportedOperationException("Multicast not supported");
/*     */   }
/*     */   
/*     */   public NetworkInterface getNetworkInterface()
/*     */   {
/* 273 */     return null;
/*     */   }
/*     */   
/*     */   public EpollDatagramChannelConfig setNetworkInterface(NetworkInterface networkInterface)
/*     */   {
/* 278 */     throw new UnsupportedOperationException("Multicast not supported");
/*     */   }
/*     */   
/*     */   public EpollDatagramChannelConfig setEpollMode(EpollMode mode)
/*     */   {
/* 283 */     super.setEpollMode(mode);
/* 284 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isReusePort()
/*     */   {
/* 291 */     return Native.isReusePort(this.datagramChannel.fd().intValue()) == 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EpollDatagramChannelConfig setReusePort(boolean reusePort)
/*     */   {
/* 302 */     Native.setReusePort(this.datagramChannel.fd().intValue(), reusePort ? 1 : 0);
/* 303 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\EpollDatagramChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */