/*     */ package io.netty.channel.socket.oio;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.AddressedEnvelope;
/*     */ import io.netty.channel.Channel.Unsafe;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.RecvByteBufAllocator.Handle;
/*     */ import io.netty.channel.oio.AbstractOioMessageChannel;
/*     */ import io.netty.channel.socket.DatagramChannel;
/*     */ import io.netty.channel.socket.DatagramChannelConfig;
/*     */ import io.netty.channel.socket.DefaultDatagramChannelConfig;
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.MulticastSocket;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
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
/*     */ public class OioDatagramChannel
/*     */   extends AbstractOioMessageChannel
/*     */   implements DatagramChannel
/*     */ {
/*  60 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(OioDatagramChannel.class);
/*     */   
/*  62 */   private static final ChannelMetadata METADATA = new ChannelMetadata(true);
/*  63 */   private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(io.netty.channel.socket.DatagramPacket.class) + ", " + StringUtil.simpleClassName(AddressedEnvelope.class) + '<' + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(SocketAddress.class) + ">, " + StringUtil.simpleClassName(ByteBuf.class) + ')';
/*     */   
/*     */ 
/*     */   private final MulticastSocket socket;
/*     */   
/*     */ 
/*     */   private final DatagramChannelConfig config;
/*     */   
/*     */ 
/*  72 */   private final java.net.DatagramPacket tmpPacket = new java.net.DatagramPacket(EmptyArrays.EMPTY_BYTES, 0);
/*     */   
/*     */   private static MulticastSocket newSocket() {
/*     */     try {
/*  76 */       return new MulticastSocket(null);
/*     */     } catch (Exception e) {
/*  78 */       throw new ChannelException("failed to create a new socket", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public OioDatagramChannel()
/*     */   {
/*  86 */     this(newSocket());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OioDatagramChannel(MulticastSocket socket)
/*     */   {
/*  95 */     super(null);
/*     */     
/*  97 */     boolean success = false;
/*     */     try {
/*  99 */       socket.setSoTimeout(1000);
/* 100 */       socket.setBroadcast(false);
/* 101 */       success = true;
/*     */     } catch (SocketException e) {
/* 103 */       throw new ChannelException("Failed to configure the datagram socket timeout.", e);
/*     */     }
/*     */     finally {
/* 106 */       if (!success) {
/* 107 */         socket.close();
/*     */       }
/*     */     }
/*     */     
/* 111 */     this.socket = socket;
/* 112 */     this.config = new DefaultDatagramChannelConfig(this, socket);
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/* 117 */     return METADATA;
/*     */   }
/*     */   
/*     */   public DatagramChannelConfig config()
/*     */   {
/* 122 */     return this.config;
/*     */   }
/*     */   
/*     */   public boolean isOpen()
/*     */   {
/* 127 */     return !this.socket.isClosed();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isActive()
/*     */   {
/* 133 */     return (isOpen()) && (((((Boolean)this.config.getOption(ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION)).booleanValue()) && (isRegistered())) || (this.socket.isBound()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isConnected()
/*     */   {
/* 140 */     return this.socket.isConnected();
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress0()
/*     */   {
/* 145 */     return this.socket.getLocalSocketAddress();
/*     */   }
/*     */   
/*     */   protected SocketAddress remoteAddress0()
/*     */   {
/* 150 */     return this.socket.getRemoteSocketAddress();
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception
/*     */   {
/* 155 */     this.socket.bind(localAddress);
/*     */   }
/*     */   
/*     */   public InetSocketAddress localAddress()
/*     */   {
/* 160 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   public InetSocketAddress remoteAddress()
/*     */   {
/* 165 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */   
/*     */   protected void doConnect(SocketAddress remoteAddress, SocketAddress localAddress)
/*     */     throws Exception
/*     */   {
/* 171 */     if (localAddress != null) {
/* 172 */       this.socket.bind(localAddress);
/*     */     }
/*     */     
/* 175 */     boolean success = false;
/*     */     try {
/* 177 */       this.socket.connect(remoteAddress);
/* 178 */       success = true; return;
/*     */     } finally {
/* 180 */       if (!success) {
/*     */         try {
/* 182 */           this.socket.close();
/*     */         } catch (Throwable t) {
/* 184 */           logger.warn("Failed to close a socket.", t);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doDisconnect() throws Exception
/*     */   {
/* 192 */     this.socket.disconnect();
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception
/*     */   {
/* 197 */     this.socket.close();
/*     */   }
/*     */   
/*     */   protected int doReadMessages(List<Object> buf) throws Exception
/*     */   {
/* 202 */     DatagramChannelConfig config = config();
/* 203 */     RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
/*     */     
/* 205 */     ByteBuf data = config.getAllocator().heapBuffer(allocHandle.guess());
/* 206 */     boolean free = true;
/*     */     try {
/* 208 */       this.tmpPacket.setData(data.array(), data.arrayOffset(), data.capacity());
/* 209 */       this.socket.receive(this.tmpPacket);
/*     */       
/* 211 */       InetSocketAddress remoteAddr = (InetSocketAddress)this.tmpPacket.getSocketAddress();
/*     */       
/* 213 */       readBytes = this.tmpPacket.getLength();
/* 214 */       allocHandle.record(readBytes);
/* 215 */       buf.add(new io.netty.channel.socket.DatagramPacket(data.writerIndex(readBytes), localAddress(), remoteAddr));
/* 216 */       free = false;
/* 217 */       return 1;
/*     */     }
/*     */     catch (SocketTimeoutException e) {
/* 220 */       return 0;
/*     */     } catch (SocketException e) {
/* 222 */       if (!e.getMessage().toLowerCase(Locale.US).contains("socket closed")) {
/* 223 */         throw e;
/*     */       }
/* 225 */       return -1;
/*     */     } catch (Throwable cause) { int readBytes;
/* 227 */       PlatformDependent.throwException(cause);
/* 228 */       return -1;
/*     */     } finally {
/* 230 */       if (free) {
/* 231 */         data.release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception
/*     */   {
/*     */     for (;;) {
/* 239 */       Object o = in.current();
/* 240 */       if (o == null) {
/*     */         break;
/*     */       }
/*     */       ByteBuf data;
/*     */       ByteBuf data;
/*     */       SocketAddress remoteAddress;
/* 246 */       if ((o instanceof AddressedEnvelope))
/*     */       {
/* 248 */         AddressedEnvelope<ByteBuf, SocketAddress> envelope = (AddressedEnvelope)o;
/* 249 */         SocketAddress remoteAddress = envelope.recipient();
/* 250 */         data = (ByteBuf)envelope.content();
/*     */       } else {
/* 252 */         data = (ByteBuf)o;
/* 253 */         remoteAddress = null;
/*     */       }
/*     */       
/* 256 */       int length = data.readableBytes();
/* 257 */       if (remoteAddress != null) {
/* 258 */         this.tmpPacket.setSocketAddress(remoteAddress);
/*     */       }
/* 260 */       if (data.hasArray()) {
/* 261 */         this.tmpPacket.setData(data.array(), data.arrayOffset() + data.readerIndex(), length);
/*     */       } else {
/* 263 */         byte[] tmp = new byte[length];
/* 264 */         data.getBytes(data.readerIndex(), tmp);
/* 265 */         this.tmpPacket.setData(tmp);
/*     */       }
/*     */       try {
/* 268 */         this.socket.send(this.tmpPacket);
/* 269 */         in.remove();
/*     */ 
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 274 */         in.remove(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg)
/*     */   {
/* 281 */     if (((msg instanceof io.netty.channel.socket.DatagramPacket)) || ((msg instanceof ByteBuf))) {
/* 282 */       return msg;
/*     */     }
/*     */     
/* 285 */     if ((msg instanceof AddressedEnvelope))
/*     */     {
/* 287 */       AddressedEnvelope<Object, SocketAddress> e = (AddressedEnvelope)msg;
/* 288 */       if ((e.content() instanceof ByteBuf)) {
/* 289 */         return msg;
/*     */       }
/*     */     }
/*     */     
/* 293 */     throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress)
/*     */   {
/* 299 */     return joinGroup(multicastAddress, newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, ChannelPromise promise)
/*     */   {
/* 304 */     ensureBound();
/*     */     try {
/* 306 */       this.socket.joinGroup(multicastAddress);
/* 307 */       promise.setSuccess();
/*     */     } catch (IOException e) {
/* 309 */       promise.setFailure(e);
/*     */     }
/* 311 */     return promise;
/*     */   }
/*     */   
/*     */   public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface)
/*     */   {
/* 316 */     return joinGroup(multicastAddress, networkInterface, newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise)
/*     */   {
/* 323 */     ensureBound();
/*     */     try {
/* 325 */       this.socket.joinGroup(multicastAddress, networkInterface);
/* 326 */       promise.setSuccess();
/*     */     } catch (IOException e) {
/* 328 */       promise.setFailure(e);
/*     */     }
/* 330 */     return promise;
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source)
/*     */   {
/* 336 */     return newFailedFuture(new UnsupportedOperationException());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise)
/*     */   {
/* 343 */     promise.setFailure(new UnsupportedOperationException());
/* 344 */     return promise;
/*     */   }
/*     */   
/*     */   private void ensureBound() {
/* 348 */     if (!isActive()) {
/* 349 */       throw new IllegalStateException(DatagramChannel.class.getName() + " must be bound to join a group.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress)
/*     */   {
/* 357 */     return leaveGroup(multicastAddress, newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, ChannelPromise promise)
/*     */   {
/*     */     try {
/* 363 */       this.socket.leaveGroup(multicastAddress);
/* 364 */       promise.setSuccess();
/*     */     } catch (IOException e) {
/* 366 */       promise.setFailure(e);
/*     */     }
/* 368 */     return promise;
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface)
/*     */   {
/* 374 */     return leaveGroup(multicastAddress, networkInterface, newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise)
/*     */   {
/*     */     try
/*     */     {
/* 382 */       this.socket.leaveGroup(multicastAddress, networkInterface);
/* 383 */       promise.setSuccess();
/*     */     } catch (IOException e) {
/* 385 */       promise.setFailure(e);
/*     */     }
/* 387 */     return promise;
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source)
/*     */   {
/* 393 */     return newFailedFuture(new UnsupportedOperationException());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise)
/*     */   {
/* 400 */     promise.setFailure(new UnsupportedOperationException());
/* 401 */     return promise;
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock)
/*     */   {
/* 407 */     return newFailedFuture(new UnsupportedOperationException());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock, ChannelPromise promise)
/*     */   {
/* 414 */     promise.setFailure(new UnsupportedOperationException());
/* 415 */     return promise;
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock)
/*     */   {
/* 421 */     return newFailedFuture(new UnsupportedOperationException());
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock, ChannelPromise promise)
/*     */   {
/* 427 */     promise.setFailure(new UnsupportedOperationException());
/* 428 */     return promise;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\socket\oio\OioDatagramChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */