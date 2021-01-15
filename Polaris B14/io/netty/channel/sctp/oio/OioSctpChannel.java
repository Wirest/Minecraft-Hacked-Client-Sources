/*     */ package io.netty.channel.sctp.oio;
/*     */ 
/*     */ import com.sun.nio.sctp.Association;
/*     */ import com.sun.nio.sctp.MessageInfo;
/*     */ import com.sun.nio.sctp.NotificationHandler;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.Channel.Unsafe;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.RecvByteBufAllocator.Handle;
/*     */ import io.netty.channel.oio.AbstractOioMessageChannel;
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
/*     */ import java.nio.channels.Selector;
/*     */ import java.util.Collections;
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
/*     */ 
/*     */ 
/*     */ public class OioSctpChannel
/*     */   extends AbstractOioMessageChannel
/*     */   implements io.netty.channel.sctp.SctpChannel
/*     */ {
/*  64 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(OioSctpChannel.class);
/*     */   
/*     */ 
/*  67 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*  68 */   private static final String EXPECTED_TYPE = " (expected: " + StringUtil.simpleClassName(SctpMessage.class) + ')';
/*     */   
/*     */   private final com.sun.nio.sctp.SctpChannel ch;
/*     */   private final SctpChannelConfig config;
/*     */   private final Selector readSelector;
/*     */   private final Selector writeSelector;
/*     */   private final Selector connectSelector;
/*     */   private final NotificationHandler<?> notificationHandler;
/*     */   
/*     */   private static com.sun.nio.sctp.SctpChannel openChannel()
/*     */   {
/*     */     try
/*     */     {
/*  81 */       return com.sun.nio.sctp.SctpChannel.open();
/*     */     } catch (IOException e) {
/*  83 */       throw new ChannelException("Failed to open a sctp channel.", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public OioSctpChannel()
/*     */   {
/*  91 */     this(openChannel());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OioSctpChannel(com.sun.nio.sctp.SctpChannel ch)
/*     */   {
/* 100 */     this(null, ch);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OioSctpChannel(Channel parent, com.sun.nio.sctp.SctpChannel ch)
/*     */   {
/* 111 */     super(parent);
/* 112 */     this.ch = ch;
/* 113 */     boolean success = false;
/*     */     try {
/* 115 */       ch.configureBlocking(false);
/* 116 */       this.readSelector = Selector.open();
/* 117 */       this.writeSelector = Selector.open();
/* 118 */       this.connectSelector = Selector.open();
/*     */       
/* 120 */       ch.register(this.readSelector, 1);
/* 121 */       ch.register(this.writeSelector, 4);
/* 122 */       ch.register(this.connectSelector, 8);
/*     */       
/* 124 */       this.config = new OioSctpChannelConfig(this, ch, null);
/* 125 */       this.notificationHandler = new SctpNotificationHandler(this);
/* 126 */       success = true; return;
/*     */     } catch (Exception e) {
/* 128 */       throw new ChannelException("failed to initialize a sctp channel", e);
/*     */     } finally {
/* 130 */       if (!success) {
/*     */         try {
/* 132 */           ch.close();
/*     */         } catch (IOException e) {
/* 134 */           logger.warn("Failed to close a sctp channel.", e);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public InetSocketAddress localAddress()
/*     */   {
/* 142 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   public InetSocketAddress remoteAddress()
/*     */   {
/* 147 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */   
/*     */   public SctpServerChannel parent()
/*     */   {
/* 152 */     return (SctpServerChannel)super.parent();
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/* 157 */     return METADATA;
/*     */   }
/*     */   
/*     */   public SctpChannelConfig config()
/*     */   {
/* 162 */     return this.config;
/*     */   }
/*     */   
/*     */   public boolean isOpen()
/*     */   {
/* 167 */     return this.ch.isOpen();
/*     */   }
/*     */   
/*     */   protected int doReadMessages(List<Object> msgs) throws Exception
/*     */   {
/* 172 */     if (!this.readSelector.isOpen()) {
/* 173 */       return 0;
/*     */     }
/*     */     
/* 176 */     int readMessages = 0;
/*     */     
/* 178 */     int selectedKeys = this.readSelector.select(1000L);
/* 179 */     boolean keysSelected = selectedKeys > 0;
/*     */     
/* 181 */     if (!keysSelected) {
/* 182 */       return readMessages;
/*     */     }
/*     */     
/* 185 */     Set<SelectionKey> reableKeys = this.readSelector.selectedKeys();
/*     */     try {
/* 187 */       for (SelectionKey ignored : reableKeys) {
/* 188 */         RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
/* 189 */         ByteBuf buffer = allocHandle.allocate(config().getAllocator());
/* 190 */         boolean free = true;
/*     */         try
/*     */         {
/* 193 */           ByteBuffer data = buffer.nioBuffer(buffer.writerIndex(), buffer.writableBytes());
/* 194 */           MessageInfo messageInfo = this.ch.receive(data, null, this.notificationHandler);
/* 195 */           if (messageInfo == null) {
/* 196 */             int i = readMessages;
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 206 */             int bytesRead = buffer.readableBytes();
/* 207 */             allocHandle.record(bytesRead);
/* 208 */             if (free) {
/* 209 */               buffer.release();
/*     */             }
/*     */             
/*     */ 
/*     */ 
/* 214 */             return i;
/*     */           }
/* 199 */           data.flip();
/* 200 */           msgs.add(new SctpMessage(messageInfo, buffer.writerIndex(buffer.writerIndex() + data.remaining())));
/* 201 */           free = false;
/* 202 */           readMessages++;
/*     */         } catch (Throwable cause) { int bytesRead;
/* 204 */           PlatformDependent.throwException(cause);
/*     */         } finally { int bytesRead;
/* 206 */           int bytesRead = buffer.readableBytes();
/* 207 */           allocHandle.record(bytesRead);
/* 208 */           if (free) {
/* 209 */             buffer.release();
/*     */           }
/*     */         }
/*     */       }
/*     */     } finally {
/* 214 */       reableKeys.clear();
/*     */     }
/* 216 */     return readMessages;
/*     */   }
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception
/*     */   {
/* 221 */     if (!this.writeSelector.isOpen()) {
/* 222 */       return;
/*     */     }
/* 224 */     int size = in.size();
/* 225 */     int selectedKeys = this.writeSelector.select(1000L);
/* 226 */     if (selectedKeys > 0) {
/* 227 */       Set<SelectionKey> writableKeys = this.writeSelector.selectedKeys();
/* 228 */       if (writableKeys.isEmpty()) {
/* 229 */         return;
/*     */       }
/* 231 */       Iterator<SelectionKey> writableKeysIt = writableKeys.iterator();
/* 232 */       int written = 0;
/*     */       for (;;) {
/* 234 */         if (written == size)
/*     */         {
/* 236 */           return;
/*     */         }
/* 238 */         writableKeysIt.next();
/* 239 */         writableKeysIt.remove();
/*     */         
/* 241 */         SctpMessage packet = (SctpMessage)in.current();
/* 242 */         if (packet == null) {
/* 243 */           return;
/*     */         }
/*     */         
/* 246 */         ByteBuf data = packet.content();
/* 247 */         int dataLen = data.readableBytes();
/*     */         ByteBuffer nioData;
/*     */         ByteBuffer nioData;
/* 250 */         if (data.nioBufferCount() != -1) {
/* 251 */           nioData = data.nioBuffer();
/*     */         } else {
/* 253 */           nioData = ByteBuffer.allocate(dataLen);
/* 254 */           data.getBytes(data.readerIndex(), nioData);
/* 255 */           nioData.flip();
/*     */         }
/*     */         
/* 258 */         MessageInfo mi = MessageInfo.createOutgoing(association(), null, packet.streamIdentifier());
/* 259 */         mi.payloadProtocolID(packet.protocolIdentifier());
/* 260 */         mi.streamNumber(packet.streamIdentifier());
/*     */         
/* 262 */         this.ch.send(nioData, mi);
/* 263 */         written++;
/* 264 */         in.remove();
/*     */         
/* 266 */         if (!writableKeysIt.hasNext()) {
/* 267 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg) throws Exception
/*     */   {
/* 275 */     if ((msg instanceof SctpMessage)) {
/* 276 */       return msg;
/*     */     }
/*     */     
/* 279 */     throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPE);
/*     */   }
/*     */   
/*     */   public Association association()
/*     */   {
/*     */     try
/*     */     {
/* 286 */       return this.ch.association();
/*     */     } catch (IOException ignored) {}
/* 288 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isActive()
/*     */   {
/* 294 */     return (isOpen()) && (association() != null);
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress0()
/*     */   {
/*     */     try {
/* 300 */       Iterator<SocketAddress> i = this.ch.getAllLocalAddresses().iterator();
/* 301 */       if (i.hasNext()) {
/* 302 */         return (SocketAddress)i.next();
/*     */       }
/*     */     }
/*     */     catch (IOException e) {}
/*     */     
/* 307 */     return null;
/*     */   }
/*     */   
/*     */   public Set<InetSocketAddress> allLocalAddresses()
/*     */   {
/*     */     try {
/* 313 */       Set<SocketAddress> allLocalAddresses = this.ch.getAllLocalAddresses();
/* 314 */       Set<InetSocketAddress> addresses = new LinkedHashSet(allLocalAddresses.size());
/* 315 */       for (SocketAddress socketAddress : allLocalAddresses) {
/* 316 */         addresses.add((InetSocketAddress)socketAddress);
/*     */       }
/* 318 */       return addresses;
/*     */     } catch (Throwable ignored) {}
/* 320 */     return Collections.emptySet();
/*     */   }
/*     */   
/*     */   protected SocketAddress remoteAddress0()
/*     */   {
/*     */     try
/*     */     {
/* 327 */       Iterator<SocketAddress> i = this.ch.getRemoteAddresses().iterator();
/* 328 */       if (i.hasNext()) {
/* 329 */         return (SocketAddress)i.next();
/*     */       }
/*     */     }
/*     */     catch (IOException e) {}
/*     */     
/* 334 */     return null;
/*     */   }
/*     */   
/*     */   public Set<InetSocketAddress> allRemoteAddresses()
/*     */   {
/*     */     try {
/* 340 */       Set<SocketAddress> allLocalAddresses = this.ch.getRemoteAddresses();
/* 341 */       Set<InetSocketAddress> addresses = new LinkedHashSet(allLocalAddresses.size());
/* 342 */       for (SocketAddress socketAddress : allLocalAddresses) {
/* 343 */         addresses.add((InetSocketAddress)socketAddress);
/*     */       }
/* 345 */       return addresses;
/*     */     } catch (Throwable ignored) {}
/* 347 */     return Collections.emptySet();
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress)
/*     */     throws Exception
/*     */   {
/* 353 */     this.ch.bind(localAddress);
/*     */   }
/*     */   
/*     */   protected void doConnect(SocketAddress remoteAddress, SocketAddress localAddress)
/*     */     throws Exception
/*     */   {
/* 359 */     if (localAddress != null) {
/* 360 */       this.ch.bind(localAddress);
/*     */     }
/*     */     
/* 363 */     boolean success = false;
/*     */     try {
/* 365 */       this.ch.connect(remoteAddress);
/* 366 */       boolean finishConnect = false;
/* 367 */       while (!finishConnect) {
/* 368 */         if (this.connectSelector.select(1000L) >= 0) {
/* 369 */           Set<SelectionKey> selectionKeys = this.connectSelector.selectedKeys();
/* 370 */           for (SelectionKey key : selectionKeys) {
/* 371 */             if (key.isConnectable()) {
/* 372 */               selectionKeys.clear();
/* 373 */               finishConnect = true;
/* 374 */               break;
/*     */             }
/*     */           }
/* 377 */           selectionKeys.clear();
/*     */         }
/*     */       }
/* 380 */       success = this.ch.finishConnect();
/*     */     } finally {
/* 382 */       if (!success) {
/* 383 */         doClose();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doDisconnect() throws Exception
/*     */   {
/* 390 */     doClose();
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception
/*     */   {
/* 395 */     closeSelector("read", this.readSelector);
/* 396 */     closeSelector("write", this.writeSelector);
/* 397 */     closeSelector("connect", this.connectSelector);
/* 398 */     this.ch.close();
/*     */   }
/*     */   
/*     */   private static void closeSelector(String selectorName, Selector selector) {
/*     */     try {
/* 403 */       selector.close();
/*     */     } catch (IOException e) {
/* 405 */       logger.warn("Failed to close a " + selectorName + " selector.", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public ChannelFuture bindAddress(InetAddress localAddress)
/*     */   {
/* 411 */     return bindAddress(localAddress, newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture bindAddress(final InetAddress localAddress, final ChannelPromise promise)
/*     */   {
/* 416 */     if (eventLoop().inEventLoop()) {
/*     */       try {
/* 418 */         this.ch.bindAddress(localAddress);
/* 419 */         promise.setSuccess();
/*     */       } catch (Throwable t) {
/* 421 */         promise.setFailure(t);
/*     */       }
/*     */     } else {
/* 424 */       eventLoop().execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 427 */           OioSctpChannel.this.bindAddress(localAddress, promise);
/*     */         }
/*     */       });
/*     */     }
/* 431 */     return promise;
/*     */   }
/*     */   
/*     */   public ChannelFuture unbindAddress(InetAddress localAddress)
/*     */   {
/* 436 */     return unbindAddress(localAddress, newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture unbindAddress(final InetAddress localAddress, final ChannelPromise promise)
/*     */   {
/* 441 */     if (eventLoop().inEventLoop()) {
/*     */       try {
/* 443 */         this.ch.unbindAddress(localAddress);
/* 444 */         promise.setSuccess();
/*     */       } catch (Throwable t) {
/* 446 */         promise.setFailure(t);
/*     */       }
/*     */     } else {
/* 449 */       eventLoop().execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 452 */           OioSctpChannel.this.unbindAddress(localAddress, promise);
/*     */         }
/*     */       });
/*     */     }
/* 456 */     return promise;
/*     */   }
/*     */   
/*     */   private final class OioSctpChannelConfig extends DefaultSctpChannelConfig {
/*     */     private OioSctpChannelConfig(OioSctpChannel channel, com.sun.nio.sctp.SctpChannel javaChannel) {
/* 461 */       super(javaChannel);
/*     */     }
/*     */     
/*     */     protected void autoReadCleared()
/*     */     {
/* 466 */       OioSctpChannel.this.setReadPending(false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\sctp\oio\OioSctpChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */