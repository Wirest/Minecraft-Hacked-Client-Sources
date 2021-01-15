/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.CompositeByteBuf;
/*     */ import io.netty.channel.AddressedEnvelope;
/*     */ import io.netty.channel.Channel.Unsafe;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.DefaultAddressedEnvelope;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.RecvByteBufAllocator.Handle;
/*     */ import io.netty.channel.socket.DatagramChannel;
/*     */ import io.netty.channel.socket.DatagramChannelConfig;
/*     */ import io.netty.channel.socket.DatagramPacket;
/*     */ import io.netty.channel.unix.FileDescriptor;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.NotYetConnectedException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public final class EpollDatagramChannel
/*     */   extends AbstractEpollChannel
/*     */   implements DatagramChannel
/*     */ {
/*  52 */   private static final ChannelMetadata METADATA = new ChannelMetadata(true);
/*  53 */   private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(DatagramPacket.class) + ", " + StringUtil.simpleClassName(AddressedEnvelope.class) + '<' + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(InetSocketAddress.class) + ">, " + StringUtil.simpleClassName(ByteBuf.class) + ')';
/*     */   
/*     */   private volatile InetSocketAddress local;
/*     */   
/*     */   private volatile InetSocketAddress remote;
/*     */   
/*     */   private volatile boolean connected;
/*     */   
/*     */   private final EpollDatagramChannelConfig config;
/*     */   
/*     */ 
/*     */   public EpollDatagramChannel()
/*     */   {
/*  66 */     super(Native.socketDgramFd(), Native.EPOLLIN);
/*  67 */     this.config = new EpollDatagramChannelConfig(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public EpollDatagramChannel(FileDescriptor fd)
/*     */   {
/*  74 */     super(null, fd, Native.EPOLLIN, true);
/*  75 */     this.config = new EpollDatagramChannelConfig(this);
/*     */     
/*     */ 
/*     */ 
/*  79 */     this.local = Native.localAddress(fd.intValue());
/*     */   }
/*     */   
/*     */   public InetSocketAddress remoteAddress()
/*     */   {
/*  84 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */   
/*     */   public InetSocketAddress localAddress()
/*     */   {
/*  89 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/*  94 */     return METADATA;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isActive()
/*     */   {
/* 100 */     return (fd().isOpen()) && (((((Boolean)this.config.getOption(ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION)).booleanValue()) && (isRegistered())) || (this.active));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isConnected()
/*     */   {
/* 107 */     return this.connected;
/*     */   }
/*     */   
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress)
/*     */   {
/* 112 */     return joinGroup(multicastAddress, newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, ChannelPromise promise)
/*     */   {
/*     */     try {
/* 118 */       return joinGroup(multicastAddress, NetworkInterface.getByInetAddress(localAddress().getAddress()), null, promise);
/*     */ 
/*     */     }
/*     */     catch (SocketException e)
/*     */     {
/* 123 */       promise.setFailure(e);
/*     */     }
/* 125 */     return promise;
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface)
/*     */   {
/* 131 */     return joinGroup(multicastAddress, networkInterface, newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise)
/*     */   {
/* 138 */     return joinGroup(multicastAddress.getAddress(), networkInterface, null, promise);
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source)
/*     */   {
/* 144 */     return joinGroup(multicastAddress, networkInterface, source, newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise)
/*     */   {
/* 152 */     if (multicastAddress == null) {
/* 153 */       throw new NullPointerException("multicastAddress");
/*     */     }
/*     */     
/* 156 */     if (networkInterface == null) {
/* 157 */       throw new NullPointerException("networkInterface");
/*     */     }
/*     */     
/* 160 */     promise.setFailure(new UnsupportedOperationException("Multicast not supported"));
/* 161 */     return promise;
/*     */   }
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress)
/*     */   {
/* 166 */     return leaveGroup(multicastAddress, newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, ChannelPromise promise)
/*     */   {
/*     */     try {
/* 172 */       return leaveGroup(multicastAddress, NetworkInterface.getByInetAddress(localAddress().getAddress()), null, promise);
/*     */     }
/*     */     catch (SocketException e) {
/* 175 */       promise.setFailure(e);
/*     */     }
/* 177 */     return promise;
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface)
/*     */   {
/* 183 */     return leaveGroup(multicastAddress, networkInterface, newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise)
/*     */   {
/* 190 */     return leaveGroup(multicastAddress.getAddress(), networkInterface, null, promise);
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source)
/*     */   {
/* 196 */     return leaveGroup(multicastAddress, networkInterface, source, newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise)
/*     */   {
/* 203 */     if (multicastAddress == null) {
/* 204 */       throw new NullPointerException("multicastAddress");
/*     */     }
/* 206 */     if (networkInterface == null) {
/* 207 */       throw new NullPointerException("networkInterface");
/*     */     }
/*     */     
/* 210 */     promise.setFailure(new UnsupportedOperationException("Multicast not supported"));
/*     */     
/* 212 */     return promise;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock)
/*     */   {
/* 219 */     return block(multicastAddress, networkInterface, sourceToBlock, newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock, ChannelPromise promise)
/*     */   {
/* 226 */     if (multicastAddress == null) {
/* 227 */       throw new NullPointerException("multicastAddress");
/*     */     }
/* 229 */     if (sourceToBlock == null) {
/* 230 */       throw new NullPointerException("sourceToBlock");
/*     */     }
/*     */     
/* 233 */     if (networkInterface == null) {
/* 234 */       throw new NullPointerException("networkInterface");
/*     */     }
/* 236 */     promise.setFailure(new UnsupportedOperationException("Multicast not supported"));
/* 237 */     return promise;
/*     */   }
/*     */   
/*     */   public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock)
/*     */   {
/* 242 */     return block(multicastAddress, sourceToBlock, newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock, ChannelPromise promise)
/*     */   {
/*     */     try
/*     */     {
/* 249 */       return block(multicastAddress, NetworkInterface.getByInetAddress(localAddress().getAddress()), sourceToBlock, promise);
/*     */ 
/*     */     }
/*     */     catch (Throwable e)
/*     */     {
/* 254 */       promise.setFailure(e);
/*     */     }
/* 256 */     return promise;
/*     */   }
/*     */   
/*     */   protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe()
/*     */   {
/* 261 */     return new EpollDatagramChannelUnsafe();
/*     */   }
/*     */   
/*     */   protected InetSocketAddress localAddress0()
/*     */   {
/* 266 */     return this.local;
/*     */   }
/*     */   
/*     */   protected InetSocketAddress remoteAddress0()
/*     */   {
/* 271 */     return this.remote;
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception
/*     */   {
/* 276 */     InetSocketAddress addr = (InetSocketAddress)localAddress;
/* 277 */     checkResolvable(addr);
/* 278 */     int fd = fd().intValue();
/* 279 */     Native.bind(fd, addr);
/* 280 */     this.local = Native.localAddress(fd);
/* 281 */     this.active = true;
/*     */   }
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception
/*     */   {
/*     */     for (;;) {
/* 287 */       Object msg = in.current();
/* 288 */       if (msg == null)
/*     */       {
/* 290 */         clearFlag(Native.EPOLLOUT);
/* 291 */         break;
/*     */       }
/*     */       
/*     */       try
/*     */       {
/* 296 */         if ((Native.IS_SUPPORTING_SENDMMSG) && (in.size() > 1)) {
/* 297 */           NativeDatagramPacketArray array = NativeDatagramPacketArray.getInstance(in);
/* 298 */           int cnt = array.count();
/*     */           
/* 300 */           if (cnt >= 1)
/*     */           {
/* 302 */             int offset = 0;
/* 303 */             NativeDatagramPacketArray.NativeDatagramPacket[] packets = array.packets();
/*     */             
/* 305 */             while (cnt > 0) {
/* 306 */               int send = Native.sendmmsg(fd().intValue(), packets, offset, cnt);
/* 307 */               if (send == 0)
/*     */               {
/* 309 */                 setFlag(Native.EPOLLOUT);
/* 310 */                 return;
/*     */               }
/* 312 */               for (int i = 0; i < send; i++) {
/* 313 */                 in.remove();
/*     */               }
/* 315 */               cnt -= send;
/* 316 */               offset += send;
/*     */             }
/* 318 */             continue;
/*     */           }
/*     */         }
/* 321 */         boolean done = false;
/* 322 */         for (int i = config().getWriteSpinCount() - 1; i >= 0; i--) {
/* 323 */           if (doWriteMessage(msg)) {
/* 324 */             done = true;
/* 325 */             break;
/*     */           }
/*     */         }
/*     */         
/* 329 */         if (done) {
/* 330 */           in.remove();
/*     */         }
/*     */         else {
/* 333 */           setFlag(Native.EPOLLOUT);
/* 334 */           break;
/*     */         }
/*     */         
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 340 */         in.remove(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean doWriteMessage(Object msg) throws Exception { InetSocketAddress remoteAddress;
/*     */     ByteBuf data;
/*     */     InetSocketAddress remoteAddress;
/* 348 */     if ((msg instanceof AddressedEnvelope))
/*     */     {
/* 350 */       AddressedEnvelope<ByteBuf, InetSocketAddress> envelope = (AddressedEnvelope)msg;
/*     */       
/* 352 */       ByteBuf data = (ByteBuf)envelope.content();
/* 353 */       remoteAddress = (InetSocketAddress)envelope.recipient();
/*     */     } else {
/* 355 */       data = (ByteBuf)msg;
/* 356 */       remoteAddress = null;
/*     */     }
/*     */     
/* 359 */     int dataLen = data.readableBytes();
/* 360 */     if (dataLen == 0) {
/* 361 */       return true;
/*     */     }
/*     */     
/* 364 */     if (remoteAddress == null) {
/* 365 */       remoteAddress = this.remote;
/* 366 */       if (remoteAddress == null) {
/* 367 */         throw new NotYetConnectedException();
/*     */       }
/*     */     }
/*     */     int writtenBytes;
/*     */     int writtenBytes;
/* 372 */     if (data.hasMemoryAddress()) {
/* 373 */       long memoryAddress = data.memoryAddress();
/* 374 */       writtenBytes = Native.sendToAddress(fd().intValue(), memoryAddress, data.readerIndex(), data.writerIndex(), remoteAddress.getAddress(), remoteAddress.getPort());
/*     */     } else { int writtenBytes;
/* 376 */       if ((data instanceof CompositeByteBuf)) {
/* 377 */         IovArray array = IovArrayThreadLocal.get((CompositeByteBuf)data);
/* 378 */         int cnt = array.count();
/* 379 */         assert (cnt != 0);
/*     */         
/* 381 */         writtenBytes = Native.sendToAddresses(fd().intValue(), array.memoryAddress(0), cnt, remoteAddress.getAddress(), remoteAddress.getPort());
/*     */       }
/*     */       else {
/* 384 */         ByteBuffer nioData = data.internalNioBuffer(data.readerIndex(), data.readableBytes());
/* 385 */         writtenBytes = Native.sendTo(fd().intValue(), nioData, nioData.position(), nioData.limit(), remoteAddress.getAddress(), remoteAddress.getPort());
/*     */       }
/*     */     }
/*     */     
/* 389 */     return writtenBytes > 0;
/*     */   }
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg)
/*     */   {
/* 394 */     if ((msg instanceof DatagramPacket)) {
/* 395 */       DatagramPacket packet = (DatagramPacket)msg;
/* 396 */       ByteBuf content = (ByteBuf)packet.content();
/* 397 */       if (content.hasMemoryAddress()) {
/* 398 */         return msg;
/*     */       }
/*     */       
/* 401 */       if ((content.isDirect()) && ((content instanceof CompositeByteBuf)))
/*     */       {
/*     */ 
/* 404 */         CompositeByteBuf comp = (CompositeByteBuf)content;
/* 405 */         if ((comp.isDirect()) && (comp.nioBufferCount() <= Native.IOV_MAX)) {
/* 406 */           return msg;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 411 */       return new DatagramPacket(newDirectBuffer(packet, content), (InetSocketAddress)packet.recipient());
/*     */     }
/*     */     
/* 414 */     if ((msg instanceof ByteBuf)) {
/* 415 */       ByteBuf buf = (ByteBuf)msg;
/* 416 */       if ((!buf.hasMemoryAddress()) && ((PlatformDependent.hasUnsafe()) || (!buf.isDirect()))) {
/* 417 */         if ((buf instanceof CompositeByteBuf))
/*     */         {
/*     */ 
/* 420 */           CompositeByteBuf comp = (CompositeByteBuf)buf;
/* 421 */           if ((!comp.isDirect()) || (comp.nioBufferCount() > Native.IOV_MAX))
/*     */           {
/* 423 */             buf = newDirectBuffer(buf);
/* 424 */             assert (buf.hasMemoryAddress());
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 429 */           buf = newDirectBuffer(buf);
/* 430 */           assert (buf.hasMemoryAddress());
/*     */         }
/*     */       }
/* 433 */       return buf;
/*     */     }
/*     */     
/* 436 */     if ((msg instanceof AddressedEnvelope))
/*     */     {
/* 438 */       AddressedEnvelope<Object, SocketAddress> e = (AddressedEnvelope)msg;
/* 439 */       if (((e.content() instanceof ByteBuf)) && ((e.recipient() == null) || ((e.recipient() instanceof InetSocketAddress))))
/*     */       {
/*     */ 
/* 442 */         ByteBuf content = (ByteBuf)e.content();
/* 443 */         if (content.hasMemoryAddress()) {
/* 444 */           return e;
/*     */         }
/* 446 */         if ((content instanceof CompositeByteBuf))
/*     */         {
/*     */ 
/* 449 */           CompositeByteBuf comp = (CompositeByteBuf)content;
/* 450 */           if ((comp.isDirect()) && (comp.nioBufferCount() <= Native.IOV_MAX)) {
/* 451 */             return e;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 456 */         return new DefaultAddressedEnvelope(newDirectBuffer(e, content), (InetSocketAddress)e.recipient());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 461 */     throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
/*     */   }
/*     */   
/*     */ 
/*     */   public EpollDatagramChannelConfig config()
/*     */   {
/* 467 */     return this.config;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 472 */   protected void doDisconnect() throws Exception { this.connected = false; }
/*     */   
/*     */   final class EpollDatagramChannelUnsafe extends AbstractEpollChannel.AbstractEpollUnsafe {
/* 475 */     EpollDatagramChannelUnsafe() { super(); }
/*     */     
/* 477 */     private final List<Object> readBuf = new ArrayList();
/*     */     
/*     */     public void connect(SocketAddress remote, SocketAddress local, ChannelPromise channelPromise)
/*     */     {
/* 481 */       boolean success = false;
/*     */       try {
/*     */         try {
/* 484 */           boolean wasActive = EpollDatagramChannel.this.isActive();
/* 485 */           InetSocketAddress remoteAddress = (InetSocketAddress)remote;
/* 486 */           if (local != null) {
/* 487 */             InetSocketAddress localAddress = (InetSocketAddress)local;
/* 488 */             EpollDatagramChannel.this.doBind(localAddress);
/*     */           }
/*     */           
/* 491 */           AbstractEpollChannel.checkResolvable(remoteAddress);
/* 492 */           EpollDatagramChannel.this.remote = remoteAddress;
/* 493 */           EpollDatagramChannel.this.local = Native.localAddress(EpollDatagramChannel.this.fd().intValue());
/* 494 */           success = true;
/*     */           
/*     */ 
/*     */ 
/* 498 */           if ((!wasActive) && (EpollDatagramChannel.this.isActive())) {
/* 499 */             EpollDatagramChannel.this.pipeline().fireChannelActive();
/*     */           }
/*     */         } finally {
/* 502 */           if (!success) {
/* 503 */             EpollDatagramChannel.this.doClose();
/*     */           } else {
/* 505 */             channelPromise.setSuccess();
/* 506 */             EpollDatagramChannel.this.connected = true;
/*     */           }
/*     */         }
/*     */       } catch (Throwable cause) {
/* 510 */         channelPromise.setFailure(cause);
/*     */       }
/*     */     }
/*     */     
/*     */     void epollInReady()
/*     */     {
/* 516 */       assert (EpollDatagramChannel.this.eventLoop().inEventLoop());
/* 517 */       DatagramChannelConfig config = EpollDatagramChannel.this.config();
/* 518 */       boolean edgeTriggered = EpollDatagramChannel.this.isFlagSet(Native.EPOLLET);
/*     */       
/* 520 */       if ((!this.readPending) && (!edgeTriggered) && (!config.isAutoRead()))
/*     */       {
/* 522 */         clearEpollIn0();
/* 523 */         return;
/*     */       }
/*     */       
/* 526 */       RecvByteBufAllocator.Handle allocHandle = EpollDatagramChannel.this.unsafe().recvBufAllocHandle();
/*     */       
/* 528 */       ChannelPipeline pipeline = EpollDatagramChannel.this.pipeline();
/* 529 */       Throwable exception = null;
/*     */       try
/*     */       {
/* 532 */         int maxMessagesPerRead = edgeTriggered ? Integer.MAX_VALUE : config.getMaxMessagesPerRead();
/*     */         
/* 534 */         int messages = 0;
/*     */         label118:
/* 536 */         ByteBuf data = null;
/*     */         try {
/* 538 */           data = allocHandle.allocate(config.getAllocator());
/* 539 */           int writerIndex = data.writerIndex();
/*     */           EpollDatagramChannel.DatagramSocketAddress remoteAddress;
/* 541 */           EpollDatagramChannel.DatagramSocketAddress remoteAddress; if (data.hasMemoryAddress())
/*     */           {
/* 543 */             remoteAddress = Native.recvFromAddress(EpollDatagramChannel.this.fd().intValue(), data.memoryAddress(), writerIndex, data.capacity());
/*     */           }
/*     */           else {
/* 546 */             ByteBuffer nioData = data.internalNioBuffer(writerIndex, data.writableBytes());
/* 547 */             remoteAddress = Native.recvFrom(EpollDatagramChannel.this.fd().intValue(), nioData, nioData.position(), nioData.limit());
/*     */           }
/*     */           
/*     */ 
/* 551 */           if (remoteAddress == null)
/*     */           {
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
/* 567 */             if (data != null) {
/* 568 */               data.release();
/*     */             }
/* 570 */             if ((edgeTriggered) || (config.isAutoRead())) {}
/*     */           }
/*     */           else
/*     */           {
/* 555 */             int readBytes = remoteAddress.receivedAmount;
/* 556 */             data.writerIndex(data.writerIndex() + readBytes);
/* 557 */             allocHandle.record(readBytes);
/* 558 */             this.readPending = false;
/*     */             
/* 560 */             this.readBuf.add(new DatagramPacket(data, (InetSocketAddress)localAddress(), remoteAddress));
/* 561 */             data = null;
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 567 */             if (data != null) {
/* 568 */               data.release();
/*     */             }
/* 570 */             if ((edgeTriggered) || (config.isAutoRead())) {}
/*     */           }
/*     */         }
/*     */         catch (Throwable t)
/*     */         {
/* 565 */           exception = t;
/*     */           
/* 567 */           if (data != null) {
/* 568 */             data.release();
/*     */           }
/* 570 */           if ((edgeTriggered) || (config.isAutoRead())) {}
/*     */         }
/*     */         finally
/*     */         {
/* 567 */           if (data != null) {
/* 568 */             data.release();
/*     */           }
/* 570 */           if ((edgeTriggered) || (config.isAutoRead()))
/*     */           {
/*     */ 
/*     */ 
/* 574 */             throw ((Throwable)localObject1);
/*     */             
/*     */ 
/* 577 */             messages++; if (messages < maxMessagesPerRead) break label118;
/*     */           } }
/* 579 */         int size = this.readBuf.size();
/* 580 */         for (int i = 0; i < size; i++) {
/* 581 */           pipeline.fireChannelRead(this.readBuf.get(i));
/*     */         }
/*     */         
/* 584 */         this.readBuf.clear();
/* 585 */         pipeline.fireChannelReadComplete();
/*     */         
/* 587 */         if (exception != null) {
/* 588 */           pipeline.fireExceptionCaught(exception);
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/* 597 */         if ((!this.readPending) && (!config.isAutoRead())) {
/* 598 */           EpollDatagramChannel.this.clearEpollIn();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static final class DatagramSocketAddress
/*     */     extends InetSocketAddress
/*     */   {
/*     */     private static final long serialVersionUID = 1348596211215015739L;
/*     */     
/*     */     final int receivedAmount;
/*     */     
/*     */ 
/*     */     DatagramSocketAddress(String addr, int port, int receivedAmount)
/*     */     {
/* 616 */       super(port);
/* 617 */       this.receivedAmount = receivedAmount;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\EpollDatagramChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */