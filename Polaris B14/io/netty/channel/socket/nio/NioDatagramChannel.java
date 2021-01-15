/*     */ package io.netty.channel.socket.nio;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.AddressedEnvelope;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.DefaultAddressedEnvelope;
/*     */ import io.netty.channel.RecvByteBufAllocator.Handle;
/*     */ import io.netty.channel.nio.AbstractNioChannel.NioUnsafe;
/*     */ import io.netty.channel.nio.AbstractNioMessageChannel;
/*     */ import io.netty.channel.socket.DatagramChannelConfig;
/*     */ import io.netty.channel.socket.DatagramPacket;
/*     */ import io.netty.channel.socket.InternetProtocolFamily;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.MembershipKey;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public final class NioDatagramChannel
/*     */   extends AbstractNioMessageChannel
/*     */   implements io.netty.channel.socket.DatagramChannel
/*     */ {
/*  63 */   private static final ChannelMetadata METADATA = new ChannelMetadata(true);
/*  64 */   private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();
/*  65 */   private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(DatagramPacket.class) + ", " + StringUtil.simpleClassName(AddressedEnvelope.class) + '<' + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(SocketAddress.class) + ">, " + StringUtil.simpleClassName(ByteBuf.class) + ')';
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final DatagramChannelConfig config;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Map<InetAddress, List<MembershipKey>> memberships;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static java.nio.channels.DatagramChannel newSocket(SelectorProvider provider)
/*     */   {
/*     */     try
/*     */     {
/*  84 */       return provider.openDatagramChannel();
/*     */     } catch (IOException e) {
/*  86 */       throw new ChannelException("Failed to open a socket.", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private static java.nio.channels.DatagramChannel newSocket(SelectorProvider provider, InternetProtocolFamily ipFamily) {
/*  91 */     if (ipFamily == null) {
/*  92 */       return newSocket(provider);
/*     */     }
/*     */     
/*  95 */     checkJavaVersion();
/*     */     try
/*     */     {
/*  98 */       return provider.openDatagramChannel(ProtocolFamilyConverter.convert(ipFamily));
/*     */     } catch (IOException e) {
/* 100 */       throw new ChannelException("Failed to open a socket.", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void checkJavaVersion() {
/* 105 */     if (PlatformDependent.javaVersion() < 7) {
/* 106 */       throw new UnsupportedOperationException("Only supported on java 7+.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public NioDatagramChannel()
/*     */   {
/* 114 */     this(newSocket(DEFAULT_SELECTOR_PROVIDER));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NioDatagramChannel(SelectorProvider provider)
/*     */   {
/* 122 */     this(newSocket(provider));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NioDatagramChannel(InternetProtocolFamily ipFamily)
/*     */   {
/* 130 */     this(newSocket(DEFAULT_SELECTOR_PROVIDER, ipFamily));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NioDatagramChannel(SelectorProvider provider, InternetProtocolFamily ipFamily)
/*     */   {
/* 139 */     this(newSocket(provider, ipFamily));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public NioDatagramChannel(java.nio.channels.DatagramChannel socket)
/*     */   {
/* 146 */     super(null, socket, 1);
/* 147 */     this.config = new NioDatagramChannelConfig(this, socket);
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/* 152 */     return METADATA;
/*     */   }
/*     */   
/*     */   public DatagramChannelConfig config()
/*     */   {
/* 157 */     return this.config;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isActive()
/*     */   {
/* 163 */     java.nio.channels.DatagramChannel ch = javaChannel();
/* 164 */     return (ch.isOpen()) && (((((Boolean)this.config.getOption(ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION)).booleanValue()) && (isRegistered())) || (ch.socket().isBound()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isConnected()
/*     */   {
/* 171 */     return javaChannel().isConnected();
/*     */   }
/*     */   
/*     */   protected java.nio.channels.DatagramChannel javaChannel()
/*     */   {
/* 176 */     return (java.nio.channels.DatagramChannel)super.javaChannel();
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress0()
/*     */   {
/* 181 */     return javaChannel().socket().getLocalSocketAddress();
/*     */   }
/*     */   
/*     */   protected SocketAddress remoteAddress0()
/*     */   {
/* 186 */     return javaChannel().socket().getRemoteSocketAddress();
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception
/*     */   {
/* 191 */     javaChannel().socket().bind(localAddress);
/*     */   }
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress)
/*     */     throws Exception
/*     */   {
/* 197 */     if (localAddress != null) {
/* 198 */       javaChannel().socket().bind(localAddress);
/*     */     }
/*     */     
/* 201 */     boolean success = false;
/*     */     try {
/* 203 */       javaChannel().connect(remoteAddress);
/* 204 */       success = true;
/* 205 */       return true;
/*     */     } finally {
/* 207 */       if (!success) {
/* 208 */         doClose();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doFinishConnect() throws Exception
/*     */   {
/* 215 */     throw new Error();
/*     */   }
/*     */   
/*     */   protected void doDisconnect() throws Exception
/*     */   {
/* 220 */     javaChannel().disconnect();
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception
/*     */   {
/* 225 */     javaChannel().close();
/*     */   }
/*     */   
/*     */   protected int doReadMessages(List<Object> buf) throws Exception
/*     */   {
/* 230 */     java.nio.channels.DatagramChannel ch = javaChannel();
/* 231 */     DatagramChannelConfig config = config();
/* 232 */     RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
/*     */     
/* 234 */     ByteBuf data = allocHandle.allocate(config.getAllocator());
/* 235 */     boolean free = true;
/*     */     try {
/* 237 */       ByteBuffer nioData = data.internalNioBuffer(data.writerIndex(), data.writableBytes());
/* 238 */       pos = nioData.position();
/* 239 */       InetSocketAddress remoteAddress = (InetSocketAddress)ch.receive(nioData);
/* 240 */       if (remoteAddress == null) {
/* 241 */         return 0;
/*     */       }
/*     */       
/* 244 */       int readBytes = nioData.position() - pos;
/* 245 */       data.writerIndex(data.writerIndex() + readBytes);
/* 246 */       allocHandle.record(readBytes);
/*     */       
/* 248 */       buf.add(new DatagramPacket(data, localAddress(), remoteAddress));
/* 249 */       free = false;
/* 250 */       return 1;
/*     */     } catch (Throwable cause) { int pos;
/* 252 */       PlatformDependent.throwException(cause);
/* 253 */       return -1;
/*     */     } finally {
/* 255 */       if (free) {
/* 256 */         data.release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean doWriteMessage(Object msg, ChannelOutboundBuffer in) throws Exception {
/*     */     ByteBuf data;
/*     */     ByteBuf data;
/*     */     SocketAddress remoteAddress;
/* 265 */     if ((msg instanceof AddressedEnvelope))
/*     */     {
/* 267 */       AddressedEnvelope<ByteBuf, SocketAddress> envelope = (AddressedEnvelope)msg;
/* 268 */       SocketAddress remoteAddress = envelope.recipient();
/* 269 */       data = (ByteBuf)envelope.content();
/*     */     } else {
/* 271 */       data = (ByteBuf)msg;
/* 272 */       remoteAddress = null;
/*     */     }
/*     */     
/* 275 */     int dataLen = data.readableBytes();
/* 276 */     if (dataLen == 0) {
/* 277 */       return true;
/*     */     }
/*     */     
/* 280 */     ByteBuffer nioData = data.internalNioBuffer(data.readerIndex(), dataLen);
/*     */     int writtenBytes;
/* 282 */     int writtenBytes; if (remoteAddress != null) {
/* 283 */       writtenBytes = javaChannel().send(nioData, remoteAddress);
/*     */     } else {
/* 285 */       writtenBytes = javaChannel().write(nioData);
/*     */     }
/* 287 */     return writtenBytes > 0;
/*     */   }
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg)
/*     */   {
/* 292 */     if ((msg instanceof DatagramPacket)) {
/* 293 */       DatagramPacket p = (DatagramPacket)msg;
/* 294 */       ByteBuf content = (ByteBuf)p.content();
/* 295 */       if (isSingleDirectBuffer(content)) {
/* 296 */         return p;
/*     */       }
/* 298 */       return new DatagramPacket(newDirectBuffer(p, content), (InetSocketAddress)p.recipient());
/*     */     }
/*     */     
/* 301 */     if ((msg instanceof ByteBuf)) {
/* 302 */       ByteBuf buf = (ByteBuf)msg;
/* 303 */       if (isSingleDirectBuffer(buf)) {
/* 304 */         return buf;
/*     */       }
/* 306 */       return newDirectBuffer(buf);
/*     */     }
/*     */     
/* 309 */     if ((msg instanceof AddressedEnvelope))
/*     */     {
/* 311 */       AddressedEnvelope<Object, SocketAddress> e = (AddressedEnvelope)msg;
/* 312 */       if ((e.content() instanceof ByteBuf)) {
/* 313 */         ByteBuf content = (ByteBuf)e.content();
/* 314 */         if (isSingleDirectBuffer(content)) {
/* 315 */           return e;
/*     */         }
/* 317 */         return new DefaultAddressedEnvelope(newDirectBuffer(e, content), e.recipient());
/*     */       }
/*     */     }
/*     */     
/* 321 */     throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isSingleDirectBuffer(ByteBuf buf)
/*     */   {
/* 330 */     return (buf.isDirect()) && (buf.nioBufferCount() == 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean continueOnWriteError()
/*     */   {
/* 338 */     return true;
/*     */   }
/*     */   
/*     */   public InetSocketAddress localAddress()
/*     */   {
/* 343 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   public InetSocketAddress remoteAddress()
/*     */   {
/* 348 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */   
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress)
/*     */   {
/* 353 */     return joinGroup(multicastAddress, newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, ChannelPromise promise)
/*     */   {
/*     */     try {
/* 359 */       return joinGroup(multicastAddress, NetworkInterface.getByInetAddress(localAddress().getAddress()), null, promise);
/*     */ 
/*     */     }
/*     */     catch (SocketException e)
/*     */     {
/* 364 */       promise.setFailure(e);
/*     */     }
/* 366 */     return promise;
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface)
/*     */   {
/* 372 */     return joinGroup(multicastAddress, networkInterface, newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise)
/*     */   {
/* 379 */     return joinGroup(multicastAddress.getAddress(), networkInterface, null, promise);
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source)
/*     */   {
/* 385 */     return joinGroup(multicastAddress, networkInterface, source, newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise)
/*     */   {
/*     */     
/*     */     
/*     */ 
/* 395 */     if (multicastAddress == null) {
/* 396 */       throw new NullPointerException("multicastAddress");
/*     */     }
/*     */     
/* 399 */     if (networkInterface == null) {
/* 400 */       throw new NullPointerException("networkInterface");
/*     */     }
/*     */     try {
/*     */       MembershipKey key;
/*     */       MembershipKey key;
/* 405 */       if (source == null) {
/* 406 */         key = javaChannel().join(multicastAddress, networkInterface);
/*     */       } else {
/* 408 */         key = javaChannel().join(multicastAddress, networkInterface, source);
/*     */       }
/*     */       
/* 411 */       synchronized (this) {
/* 412 */         List<MembershipKey> keys = null;
/* 413 */         if (this.memberships == null) {
/* 414 */           this.memberships = new HashMap();
/*     */         } else {
/* 416 */           keys = (List)this.memberships.get(multicastAddress);
/*     */         }
/* 418 */         if (keys == null) {
/* 419 */           keys = new ArrayList();
/* 420 */           this.memberships.put(multicastAddress, keys);
/*     */         }
/* 422 */         keys.add(key);
/*     */       }
/*     */       
/* 425 */       promise.setSuccess();
/*     */     } catch (Throwable e) {
/* 427 */       promise.setFailure(e);
/*     */     }
/*     */     
/* 430 */     return promise;
/*     */   }
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress)
/*     */   {
/* 435 */     return leaveGroup(multicastAddress, newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, ChannelPromise promise)
/*     */   {
/*     */     try {
/* 441 */       return leaveGroup(multicastAddress, NetworkInterface.getByInetAddress(localAddress().getAddress()), null, promise);
/*     */     }
/*     */     catch (SocketException e) {
/* 444 */       promise.setFailure(e);
/*     */     }
/* 446 */     return promise;
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface)
/*     */   {
/* 452 */     return leaveGroup(multicastAddress, networkInterface, newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise)
/*     */   {
/* 459 */     return leaveGroup(multicastAddress.getAddress(), networkInterface, null, promise);
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source)
/*     */   {
/* 465 */     return leaveGroup(multicastAddress, networkInterface, source, newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise)
/*     */   {
/*     */     
/*     */     
/* 474 */     if (multicastAddress == null) {
/* 475 */       throw new NullPointerException("multicastAddress");
/*     */     }
/* 477 */     if (networkInterface == null) {
/* 478 */       throw new NullPointerException("networkInterface");
/*     */     }
/*     */     
/* 481 */     synchronized (this) {
/* 482 */       if (this.memberships != null) {
/* 483 */         List<MembershipKey> keys = (List)this.memberships.get(multicastAddress);
/* 484 */         if (keys != null) {
/* 485 */           Iterator<MembershipKey> keyIt = keys.iterator();
/*     */           
/* 487 */           while (keyIt.hasNext()) {
/* 488 */             MembershipKey key = (MembershipKey)keyIt.next();
/* 489 */             if ((networkInterface.equals(key.networkInterface())) && (
/* 490 */               ((source == null) && (key.sourceAddress() == null)) || ((source != null) && (source.equals(key.sourceAddress())))))
/*     */             {
/* 492 */               key.drop();
/* 493 */               keyIt.remove();
/*     */             }
/*     */           }
/*     */           
/* 497 */           if (keys.isEmpty()) {
/* 498 */             this.memberships.remove(multicastAddress);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 504 */     promise.setSuccess();
/* 505 */     return promise;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock)
/*     */   {
/* 515 */     return block(multicastAddress, networkInterface, sourceToBlock, newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock, ChannelPromise promise)
/*     */   {
/*     */     
/*     */     
/*     */ 
/*     */ 
/* 527 */     if (multicastAddress == null) {
/* 528 */       throw new NullPointerException("multicastAddress");
/*     */     }
/* 530 */     if (sourceToBlock == null) {
/* 531 */       throw new NullPointerException("sourceToBlock");
/*     */     }
/*     */     
/* 534 */     if (networkInterface == null) {
/* 535 */       throw new NullPointerException("networkInterface");
/*     */     }
/* 537 */     synchronized (this) {
/* 538 */       if (this.memberships != null) {
/* 539 */         List<MembershipKey> keys = (List)this.memberships.get(multicastAddress);
/* 540 */         for (MembershipKey key : keys) {
/* 541 */           if (networkInterface.equals(key.networkInterface())) {
/*     */             try {
/* 543 */               key.block(sourceToBlock);
/*     */             } catch (IOException e) {
/* 545 */               promise.setFailure(e);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 551 */     promise.setSuccess();
/* 552 */     return promise;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock)
/*     */   {
/* 561 */     return block(multicastAddress, sourceToBlock, newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock, ChannelPromise promise)
/*     */   {
/*     */     try
/*     */     {
/* 572 */       return block(multicastAddress, NetworkInterface.getByInetAddress(localAddress().getAddress()), sourceToBlock, promise);
/*     */ 
/*     */     }
/*     */     catch (SocketException e)
/*     */     {
/* 577 */       promise.setFailure(e);
/*     */     }
/* 579 */     return promise;
/*     */   }
/*     */   
/*     */   protected void setReadPending(boolean readPending)
/*     */   {
/* 584 */     super.setReadPending(readPending);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\socket\nio\NioDatagramChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */