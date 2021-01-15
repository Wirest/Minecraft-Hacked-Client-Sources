/*     */ package io.netty.channel.sctp.nio;
/*     */ 
/*     */ import com.sun.nio.sctp.Association;
/*     */ import com.sun.nio.sctp.MessageInfo;
/*     */ import com.sun.nio.sctp.NotificationHandler;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.RecvByteBufAllocator.Handle;
/*     */ import io.netty.channel.nio.AbstractNioChannel.NioUnsafe;
/*     */ import io.netty.channel.nio.AbstractNioMessageChannel;
/*     */ import io.netty.channel.sctp.DefaultSctpChannelConfig;
/*     */ import io.netty.channel.sctp.SctpChannelConfig;
/*     */ import io.netty.channel.sctp.SctpMessage;
/*     */ import io.netty.channel.sctp.SctpNotificationHandler;
/*     */ import io.netty.channel.sctp.SctpServerChannel;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public class NioSctpChannel
/*     */   extends AbstractNioMessageChannel
/*     */   implements io.netty.channel.sctp.SctpChannel
/*     */ {
/*  63 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*     */   
/*  65 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioSctpChannel.class);
/*     */   private final SctpChannelConfig config;
/*     */   private final NotificationHandler<?> notificationHandler;
/*     */   
/*     */   private static com.sun.nio.sctp.SctpChannel newSctpChannel()
/*     */   {
/*     */     try
/*     */     {
/*  73 */       return com.sun.nio.sctp.SctpChannel.open();
/*     */     } catch (IOException e) {
/*  75 */       throw new ChannelException("Failed to open a sctp channel.", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public NioSctpChannel()
/*     */   {
/*  83 */     this(newSctpChannel());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public NioSctpChannel(com.sun.nio.sctp.SctpChannel sctpChannel)
/*     */   {
/*  90 */     this(null, sctpChannel);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NioSctpChannel(Channel parent, com.sun.nio.sctp.SctpChannel sctpChannel)
/*     */   {
/* 101 */     super(parent, sctpChannel, 1);
/*     */     try {
/* 103 */       sctpChannel.configureBlocking(false);
/* 104 */       this.config = new NioSctpChannelConfig(this, sctpChannel, null);
/* 105 */       this.notificationHandler = new SctpNotificationHandler(this);
/*     */     } catch (IOException e) {
/*     */       try {
/* 108 */         sctpChannel.close();
/*     */       } catch (IOException e2) {
/* 110 */         if (logger.isWarnEnabled()) {
/* 111 */           logger.warn("Failed to close a partially initialized sctp channel.", e2);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 116 */       throw new ChannelException("Failed to enter non-blocking mode.", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public InetSocketAddress localAddress()
/*     */   {
/* 122 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   public InetSocketAddress remoteAddress()
/*     */   {
/* 127 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */   
/*     */   public SctpServerChannel parent()
/*     */   {
/* 132 */     return (SctpServerChannel)super.parent();
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/* 137 */     return METADATA;
/*     */   }
/*     */   
/*     */   public Association association()
/*     */   {
/*     */     try {
/* 143 */       return javaChannel().association();
/*     */     } catch (IOException ignored) {}
/* 145 */     return null;
/*     */   }
/*     */   
/*     */   public Set<InetSocketAddress> allLocalAddresses()
/*     */   {
/*     */     try
/*     */     {
/* 152 */       Set<SocketAddress> allLocalAddresses = javaChannel().getAllLocalAddresses();
/* 153 */       Set<InetSocketAddress> addresses = new LinkedHashSet(allLocalAddresses.size());
/* 154 */       for (SocketAddress socketAddress : allLocalAddresses) {
/* 155 */         addresses.add((InetSocketAddress)socketAddress);
/*     */       }
/* 157 */       return addresses;
/*     */     } catch (Throwable ignored) {}
/* 159 */     return Collections.emptySet();
/*     */   }
/*     */   
/*     */ 
/*     */   public SctpChannelConfig config()
/*     */   {
/* 165 */     return this.config;
/*     */   }
/*     */   
/*     */   public Set<InetSocketAddress> allRemoteAddresses()
/*     */   {
/*     */     try {
/* 171 */       Set<SocketAddress> allLocalAddresses = javaChannel().getRemoteAddresses();
/* 172 */       Set<InetSocketAddress> addresses = new HashSet(allLocalAddresses.size());
/* 173 */       for (SocketAddress socketAddress : allLocalAddresses) {
/* 174 */         addresses.add((InetSocketAddress)socketAddress);
/*     */       }
/* 176 */       return addresses;
/*     */     } catch (Throwable ignored) {}
/* 178 */     return Collections.emptySet();
/*     */   }
/*     */   
/*     */ 
/*     */   protected com.sun.nio.sctp.SctpChannel javaChannel()
/*     */   {
/* 184 */     return (com.sun.nio.sctp.SctpChannel)super.javaChannel();
/*     */   }
/*     */   
/*     */   public boolean isActive()
/*     */   {
/* 189 */     com.sun.nio.sctp.SctpChannel ch = javaChannel();
/* 190 */     return (ch.isOpen()) && (association() != null);
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress0()
/*     */   {
/*     */     try {
/* 196 */       Iterator<SocketAddress> i = javaChannel().getAllLocalAddresses().iterator();
/* 197 */       if (i.hasNext()) {
/* 198 */         return (SocketAddress)i.next();
/*     */       }
/*     */     }
/*     */     catch (IOException e) {}
/*     */     
/* 203 */     return null;
/*     */   }
/*     */   
/*     */   protected SocketAddress remoteAddress0()
/*     */   {
/*     */     try {
/* 209 */       Iterator<SocketAddress> i = javaChannel().getRemoteAddresses().iterator();
/* 210 */       if (i.hasNext()) {
/* 211 */         return (SocketAddress)i.next();
/*     */       }
/*     */     }
/*     */     catch (IOException e) {}
/*     */     
/* 216 */     return null;
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception
/*     */   {
/* 221 */     javaChannel().bind(localAddress);
/*     */   }
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception
/*     */   {
/* 226 */     if (localAddress != null) {
/* 227 */       javaChannel().bind(localAddress);
/*     */     }
/*     */     
/* 230 */     boolean success = false;
/*     */     try {
/* 232 */       boolean connected = javaChannel().connect(remoteAddress);
/* 233 */       if (!connected) {
/* 234 */         selectionKey().interestOps(8);
/*     */       }
/* 236 */       success = true;
/* 237 */       return connected;
/*     */     } finally {
/* 239 */       if (!success) {
/* 240 */         doClose();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doFinishConnect() throws Exception
/*     */   {
/* 247 */     if (!javaChannel().finishConnect()) {
/* 248 */       throw new Error();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doDisconnect() throws Exception
/*     */   {
/* 254 */     doClose();
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception
/*     */   {
/* 259 */     javaChannel().close();
/*     */   }
/*     */   
/*     */   protected int doReadMessages(List<Object> buf) throws Exception
/*     */   {
/* 264 */     com.sun.nio.sctp.SctpChannel ch = javaChannel();
/*     */     
/* 266 */     RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
/* 267 */     ByteBuf buffer = allocHandle.allocate(config().getAllocator());
/* 268 */     boolean free = true;
/*     */     try {
/* 270 */       ByteBuffer data = buffer.internalNioBuffer(buffer.writerIndex(), buffer.writableBytes());
/* 271 */       pos = data.position();
/*     */       
/* 273 */       MessageInfo messageInfo = ch.receive(data, null, this.notificationHandler);
/* 274 */       int i; if (messageInfo == null) { int bytesRead;
/* 275 */         return 0;
/*     */       }
/* 277 */       buf.add(new SctpMessage(messageInfo, buffer.writerIndex(buffer.writerIndex() + data.position() - pos)));
/* 278 */       free = false;
/* 279 */       int bytesRead; return 1;
/*     */     } catch (Throwable cause) { int pos;
/* 281 */       PlatformDependent.throwException(cause);
/* 282 */       int bytesRead; return -1;
/*     */     } finally {
/* 284 */       int bytesRead = buffer.readableBytes();
/* 285 */       allocHandle.record(bytesRead);
/* 286 */       if (free) {
/* 287 */         buffer.release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean doWriteMessage(Object msg, ChannelOutboundBuffer in) throws Exception
/*     */   {
/* 294 */     SctpMessage packet = (SctpMessage)msg;
/* 295 */     ByteBuf data = packet.content();
/* 296 */     int dataLen = data.readableBytes();
/* 297 */     if (dataLen == 0) {
/* 298 */       return true;
/*     */     }
/*     */     
/* 301 */     ByteBufAllocator alloc = alloc();
/* 302 */     boolean needsCopy = data.nioBufferCount() != 1;
/* 303 */     if ((!needsCopy) && 
/* 304 */       (!data.isDirect()) && (alloc.isDirectBufferPooled())) {
/* 305 */       needsCopy = true;
/*     */     }
/*     */     ByteBuffer nioData;
/*     */     ByteBuffer nioData;
/* 309 */     if (!needsCopy) {
/* 310 */       nioData = data.nioBuffer();
/*     */     } else {
/* 312 */       data = alloc.directBuffer(dataLen).writeBytes(data);
/* 313 */       nioData = data.nioBuffer();
/*     */     }
/* 315 */     MessageInfo mi = MessageInfo.createOutgoing(association(), null, packet.streamIdentifier());
/* 316 */     mi.payloadProtocolID(packet.protocolIdentifier());
/* 317 */     mi.streamNumber(packet.streamIdentifier());
/*     */     
/* 319 */     int writtenBytes = javaChannel().send(nioData, mi);
/* 320 */     return writtenBytes > 0;
/*     */   }
/*     */   
/*     */   protected final Object filterOutboundMessage(Object msg) throws Exception
/*     */   {
/* 325 */     if ((msg instanceof SctpMessage)) {
/* 326 */       SctpMessage m = (SctpMessage)msg;
/* 327 */       ByteBuf buf = m.content();
/* 328 */       if ((buf.isDirect()) && (buf.nioBufferCount() == 1)) {
/* 329 */         return m;
/*     */       }
/*     */       
/* 332 */       return new SctpMessage(m.protocolIdentifier(), m.streamIdentifier(), newDirectBuffer(m, buf));
/*     */     }
/*     */     
/* 335 */     throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + " (expected: " + StringUtil.simpleClassName(SctpMessage.class));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture bindAddress(InetAddress localAddress)
/*     */   {
/* 342 */     return bindAddress(localAddress, newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture bindAddress(final InetAddress localAddress, final ChannelPromise promise)
/*     */   {
/* 347 */     if (eventLoop().inEventLoop()) {
/*     */       try {
/* 349 */         javaChannel().bindAddress(localAddress);
/* 350 */         promise.setSuccess();
/*     */       } catch (Throwable t) {
/* 352 */         promise.setFailure(t);
/*     */       }
/*     */     } else {
/* 355 */       eventLoop().execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 358 */           NioSctpChannel.this.bindAddress(localAddress, promise);
/*     */         }
/*     */       });
/*     */     }
/* 362 */     return promise;
/*     */   }
/*     */   
/*     */   public ChannelFuture unbindAddress(InetAddress localAddress)
/*     */   {
/* 367 */     return unbindAddress(localAddress, newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture unbindAddress(final InetAddress localAddress, final ChannelPromise promise)
/*     */   {
/* 372 */     if (eventLoop().inEventLoop()) {
/*     */       try {
/* 374 */         javaChannel().unbindAddress(localAddress);
/* 375 */         promise.setSuccess();
/*     */       } catch (Throwable t) {
/* 377 */         promise.setFailure(t);
/*     */       }
/*     */     } else {
/* 380 */       eventLoop().execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 383 */           NioSctpChannel.this.unbindAddress(localAddress, promise);
/*     */         }
/*     */       });
/*     */     }
/* 387 */     return promise;
/*     */   }
/*     */   
/*     */   private final class NioSctpChannelConfig extends DefaultSctpChannelConfig {
/*     */     private NioSctpChannelConfig(NioSctpChannel channel, com.sun.nio.sctp.SctpChannel javaChannel) {
/* 392 */       super(javaChannel);
/*     */     }
/*     */     
/*     */     protected void autoReadCleared()
/*     */     {
/* 397 */       NioSctpChannel.this.setReadPending(false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\sctp\nio\NioSctpChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */