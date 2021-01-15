/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.CompositeByteBuf;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.ConnectTimeoutException;
/*     */ import io.netty.channel.DefaultFileRegion;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.RecvByteBufAllocator.Handle;
/*     */ import io.netty.channel.socket.ChannelInputShutdownEvent;
/*     */ import io.netty.channel.unix.FileDescriptor;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public abstract class AbstractEpollStreamChannel
/*     */   extends AbstractEpollChannel
/*     */ {
/*  45 */   private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(DefaultFileRegion.class) + ')';
/*     */   
/*     */   private volatile boolean inputShutdown;
/*     */   
/*     */   private volatile boolean outputShutdown;
/*     */   
/*     */   protected AbstractEpollStreamChannel(Channel parent, int fd)
/*     */   {
/*  53 */     super(parent, fd, Native.EPOLLIN, true);
/*     */     
/*  55 */     this.flags |= Native.EPOLLRDHUP;
/*     */   }
/*     */   
/*     */   protected AbstractEpollStreamChannel(int fd) {
/*  59 */     super(fd, Native.EPOLLIN);
/*     */     
/*  61 */     this.flags |= Native.EPOLLRDHUP;
/*     */   }
/*     */   
/*     */   protected AbstractEpollStreamChannel(FileDescriptor fd) {
/*  65 */     super(null, fd, Native.EPOLLIN, Native.getSoError(fd.intValue()) == 0);
/*     */   }
/*     */   
/*     */   protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe()
/*     */   {
/*  70 */     return new EpollStreamUnsafe();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean writeBytes(ChannelOutboundBuffer in, ByteBuf buf, int writeSpinCount)
/*     */     throws Exception
/*     */   {
/*  78 */     int readableBytes = buf.readableBytes();
/*  79 */     if (readableBytes == 0) {
/*  80 */       in.remove();
/*  81 */       return true;
/*     */     }
/*     */     
/*  84 */     if ((buf.hasMemoryAddress()) || (buf.nioBufferCount() == 1)) {
/*  85 */       int writtenBytes = doWriteBytes(buf, writeSpinCount);
/*  86 */       in.removeBytes(writtenBytes);
/*  87 */       return writtenBytes == readableBytes;
/*     */     }
/*  89 */     ByteBuffer[] nioBuffers = buf.nioBuffers();
/*  90 */     return writeBytesMultiple(in, nioBuffers, nioBuffers.length, readableBytes, writeSpinCount);
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean writeBytesMultiple(ChannelOutboundBuffer in, IovArray array, int writeSpinCount)
/*     */     throws IOException
/*     */   {
/*  97 */     long expectedWrittenBytes = array.size();
/*  98 */     long initialExpectedWrittenBytes = expectedWrittenBytes;
/*     */     
/* 100 */     int cnt = array.count();
/*     */     
/* 102 */     assert (expectedWrittenBytes != 0L);
/* 103 */     assert (cnt != 0);
/*     */     
/* 105 */     boolean done = false;
/* 106 */     int offset = 0;
/* 107 */     int end = offset + cnt;
/* 108 */     for (int i = writeSpinCount - 1; i >= 0; i--) {
/* 109 */       long localWrittenBytes = Native.writevAddresses(fd().intValue(), array.memoryAddress(offset), cnt);
/* 110 */       if (localWrittenBytes == 0L) {
/*     */         break;
/*     */       }
/* 113 */       expectedWrittenBytes -= localWrittenBytes;
/*     */       
/* 115 */       if (expectedWrittenBytes == 0L)
/*     */       {
/* 117 */         done = true;
/* 118 */         break;
/*     */       }
/*     */       do
/*     */       {
/* 122 */         long bytes = array.processWritten(offset, localWrittenBytes);
/* 123 */         if (bytes == -1L) {
/*     */           break;
/*     */         }
/*     */         
/* 127 */         offset++;
/* 128 */         cnt--;
/* 129 */         localWrittenBytes -= bytes;
/*     */       }
/* 131 */       while ((offset < end) && (localWrittenBytes > 0L));
/*     */     }
/* 133 */     if (!done) {
/* 134 */       setFlag(Native.EPOLLOUT);
/*     */     }
/* 136 */     in.removeBytes(initialExpectedWrittenBytes - expectedWrittenBytes);
/* 137 */     return done;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean writeBytesMultiple(ChannelOutboundBuffer in, ByteBuffer[] nioBuffers, int nioBufferCnt, long expectedWrittenBytes, int writeSpinCount)
/*     */     throws IOException
/*     */   {
/* 144 */     assert (expectedWrittenBytes != 0L);
/* 145 */     long initialExpectedWrittenBytes = expectedWrittenBytes;
/*     */     
/* 147 */     boolean done = false;
/* 148 */     int offset = 0;
/* 149 */     int end = offset + nioBufferCnt;
/* 150 */     for (int i = writeSpinCount - 1; i >= 0; i--) {
/* 151 */       long localWrittenBytes = Native.writev(fd().intValue(), nioBuffers, offset, nioBufferCnt);
/* 152 */       if (localWrittenBytes == 0L) {
/*     */         break;
/*     */       }
/* 155 */       expectedWrittenBytes -= localWrittenBytes;
/*     */       
/* 157 */       if (expectedWrittenBytes == 0L)
/*     */       {
/* 159 */         done = true;
/* 160 */         break;
/*     */       }
/*     */       do {
/* 163 */         ByteBuffer buffer = nioBuffers[offset];
/* 164 */         int pos = buffer.position();
/* 165 */         int bytes = buffer.limit() - pos;
/* 166 */         if (bytes > localWrittenBytes) {
/* 167 */           buffer.position(pos + (int)localWrittenBytes);
/*     */           
/* 169 */           break;
/*     */         }
/* 171 */         offset++;
/* 172 */         nioBufferCnt--;
/* 173 */         localWrittenBytes -= bytes;
/*     */       }
/* 175 */       while ((offset < end) && (localWrittenBytes > 0L));
/*     */     }
/*     */     
/* 178 */     in.removeBytes(initialExpectedWrittenBytes - expectedWrittenBytes);
/* 179 */     if (!done) {
/* 180 */       setFlag(Native.EPOLLOUT);
/*     */     }
/* 182 */     return done;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean writeFileRegion(ChannelOutboundBuffer in, DefaultFileRegion region, int writeSpinCount)
/*     */     throws Exception
/*     */   {
/* 193 */     long regionCount = region.count();
/* 194 */     if (region.transfered() >= regionCount) {
/* 195 */       in.remove();
/* 196 */       return true;
/*     */     }
/*     */     
/* 199 */     long baseOffset = region.position();
/* 200 */     boolean done = false;
/* 201 */     long flushedAmount = 0L;
/*     */     
/* 203 */     for (int i = writeSpinCount - 1; i >= 0; i--) {
/* 204 */       long offset = region.transfered();
/* 205 */       long localFlushedAmount = Native.sendfile(fd().intValue(), region, baseOffset, offset, regionCount - offset);
/*     */       
/* 207 */       if (localFlushedAmount == 0L) {
/*     */         break;
/*     */       }
/*     */       
/* 211 */       flushedAmount += localFlushedAmount;
/* 212 */       if (region.transfered() >= regionCount) {
/* 213 */         done = true;
/* 214 */         break;
/*     */       }
/*     */     }
/*     */     
/* 218 */     if (flushedAmount > 0L) {
/* 219 */       in.progress(flushedAmount);
/*     */     }
/*     */     
/* 222 */     if (done) {
/* 223 */       in.remove();
/*     */     }
/*     */     else {
/* 226 */       setFlag(Native.EPOLLOUT);
/*     */     }
/* 228 */     return done;
/*     */   }
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception
/*     */   {
/* 233 */     int writeSpinCount = config().getWriteSpinCount();
/*     */     for (;;) {
/* 235 */       int msgCount = in.size();
/*     */       
/* 237 */       if (msgCount == 0)
/*     */       {
/* 239 */         clearFlag(Native.EPOLLOUT);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 244 */         if ((msgCount > 1) && ((in.current() instanceof ByteBuf)) ? 
/* 245 */           !doWriteMultiple(in, writeSpinCount) : 
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 253 */           !doWriteSingle(in, writeSpinCount)) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean doWriteSingle(ChannelOutboundBuffer in, int writeSpinCount) throws Exception
/*     */   {
/* 262 */     Object msg = in.current();
/* 263 */     if ((msg instanceof ByteBuf)) {
/* 264 */       ByteBuf buf = (ByteBuf)msg;
/* 265 */       if (!writeBytes(in, buf, writeSpinCount))
/*     */       {
/*     */ 
/* 268 */         return false;
/*     */       }
/* 270 */     } else if ((msg instanceof DefaultFileRegion)) {
/* 271 */       DefaultFileRegion region = (DefaultFileRegion)msg;
/* 272 */       if (!writeFileRegion(in, region, writeSpinCount))
/*     */       {
/*     */ 
/* 275 */         return false;
/*     */       }
/*     */     }
/*     */     else {
/* 279 */       throw new Error();
/*     */     }
/*     */     
/* 282 */     return true;
/*     */   }
/*     */   
/*     */   private boolean doWriteMultiple(ChannelOutboundBuffer in, int writeSpinCount) throws Exception {
/* 286 */     if (PlatformDependent.hasUnsafe())
/*     */     {
/* 288 */       IovArray array = IovArrayThreadLocal.get(in);
/* 289 */       int cnt = array.count();
/* 290 */       if (cnt >= 1)
/*     */       {
/* 292 */         if (!writeBytesMultiple(in, array, writeSpinCount))
/*     */         {
/*     */ 
/* 295 */           return false;
/*     */         }
/*     */       } else {
/* 298 */         in.removeBytes(0L);
/*     */       }
/*     */     } else {
/* 301 */       ByteBuffer[] buffers = in.nioBuffers();
/* 302 */       int cnt = in.nioBufferCount();
/* 303 */       if (cnt >= 1)
/*     */       {
/* 305 */         if (!writeBytesMultiple(in, buffers, cnt, in.nioBufferSize(), writeSpinCount))
/*     */         {
/*     */ 
/* 308 */           return false;
/*     */         }
/*     */       } else {
/* 311 */         in.removeBytes(0L);
/*     */       }
/*     */     }
/*     */     
/* 315 */     return true;
/*     */   }
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg)
/*     */   {
/* 320 */     if ((msg instanceof ByteBuf)) {
/* 321 */       ByteBuf buf = (ByteBuf)msg;
/* 322 */       if ((!buf.hasMemoryAddress()) && ((PlatformDependent.hasUnsafe()) || (!buf.isDirect()))) {
/* 323 */         if ((buf instanceof CompositeByteBuf))
/*     */         {
/*     */ 
/* 326 */           CompositeByteBuf comp = (CompositeByteBuf)buf;
/* 327 */           if ((!comp.isDirect()) || (comp.nioBufferCount() > Native.IOV_MAX))
/*     */           {
/* 329 */             buf = newDirectBuffer(buf);
/* 330 */             assert (buf.hasMemoryAddress());
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 335 */           buf = newDirectBuffer(buf);
/* 336 */           assert (buf.hasMemoryAddress());
/*     */         }
/*     */       }
/* 339 */       return buf;
/*     */     }
/*     */     
/* 342 */     if ((msg instanceof DefaultFileRegion)) {
/* 343 */       return msg;
/*     */     }
/*     */     
/* 346 */     throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
/*     */   }
/*     */   
/*     */   protected boolean isInputShutdown0()
/*     */   {
/* 351 */     return this.inputShutdown;
/*     */   }
/*     */   
/*     */   protected boolean isOutputShutdown0() {
/* 355 */     return (this.outputShutdown) || (!isActive());
/*     */   }
/*     */   
/*     */   protected void shutdownOutput0(ChannelPromise promise) {
/*     */     try {
/* 360 */       Native.shutdown(fd().intValue(), false, true);
/* 361 */       this.outputShutdown = true;
/* 362 */       promise.setSuccess();
/*     */     } catch (Throwable cause) {
/* 364 */       promise.setFailure(cause);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress)
/*     */     throws Exception
/*     */   {
/* 372 */     if (localAddress != null) {
/* 373 */       Native.bind(fd().intValue(), localAddress);
/*     */     }
/*     */     
/* 376 */     boolean success = false;
/*     */     try {
/* 378 */       boolean connected = Native.connect(fd().intValue(), remoteAddress);
/* 379 */       if (!connected) {
/* 380 */         setFlag(Native.EPOLLOUT);
/*     */       }
/* 382 */       success = true;
/* 383 */       return connected;
/*     */     } finally {
/* 385 */       if (!success)
/* 386 */         doClose(); } }
/*     */   
/*     */   class EpollStreamUnsafe extends AbstractEpollChannel.AbstractEpollUnsafe { private ChannelPromise connectPromise;
/*     */     private ScheduledFuture<?> connectTimeoutFuture;
/*     */     
/* 391 */     EpollStreamUnsafe() { super(); }
/*     */     
/*     */ 
/*     */ 
/*     */     private SocketAddress requestedRemoteAddress;
/*     */     
/*     */ 
/*     */     private RecvByteBufAllocator.Handle allocHandle;
/*     */     
/*     */ 
/*     */     private void closeOnRead(ChannelPipeline pipeline)
/*     */     {
/* 403 */       AbstractEpollStreamChannel.this.inputShutdown = true;
/* 404 */       if (AbstractEpollStreamChannel.this.isOpen()) {
/* 405 */         if (Boolean.TRUE.equals(AbstractEpollStreamChannel.this.config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
/* 406 */           clearEpollIn0();
/* 407 */           pipeline.fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
/*     */         } else {
/* 409 */           close(voidPromise());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private boolean handleReadException(ChannelPipeline pipeline, ByteBuf byteBuf, Throwable cause, boolean close) {
/* 415 */       if (byteBuf != null) {
/* 416 */         if (byteBuf.isReadable()) {
/* 417 */           this.readPending = false;
/* 418 */           pipeline.fireChannelRead(byteBuf);
/*     */         } else {
/* 420 */           byteBuf.release();
/*     */         }
/*     */       }
/* 423 */       pipeline.fireChannelReadComplete();
/* 424 */       pipeline.fireExceptionCaught(cause);
/* 425 */       if ((close) || ((cause instanceof IOException))) {
/* 426 */         closeOnRead(pipeline);
/* 427 */         return true;
/*     */       }
/* 429 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public void connect(final SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*     */     {
/* 435 */       if ((!promise.setUncancellable()) || (!ensureOpen(promise))) {
/* 436 */         return;
/*     */       }
/*     */       try
/*     */       {
/* 440 */         if (this.connectPromise != null) {
/* 441 */           throw new IllegalStateException("connection attempt already made");
/*     */         }
/*     */         
/* 444 */         boolean wasActive = AbstractEpollStreamChannel.this.isActive();
/* 445 */         if (AbstractEpollStreamChannel.this.doConnect(remoteAddress, localAddress)) {
/* 446 */           fulfillConnectPromise(promise, wasActive);
/*     */         } else {
/* 448 */           this.connectPromise = promise;
/* 449 */           this.requestedRemoteAddress = remoteAddress;
/*     */           
/*     */ 
/* 452 */           int connectTimeoutMillis = AbstractEpollStreamChannel.this.config().getConnectTimeoutMillis();
/* 453 */           if (connectTimeoutMillis > 0) {
/* 454 */             this.connectTimeoutFuture = AbstractEpollStreamChannel.this.eventLoop().schedule(new Runnable()
/*     */             {
/*     */               public void run() {
/* 457 */                 ChannelPromise connectPromise = AbstractEpollStreamChannel.EpollStreamUnsafe.this.connectPromise;
/* 458 */                 ConnectTimeoutException cause = new ConnectTimeoutException("connection timed out: " + remoteAddress);
/*     */                 
/* 460 */                 if ((connectPromise != null) && (connectPromise.tryFailure(cause)))
/* 461 */                   AbstractEpollStreamChannel.EpollStreamUnsafe.this.close(AbstractEpollStreamChannel.EpollStreamUnsafe.this.voidPromise()); } }, connectTimeoutMillis, TimeUnit.MILLISECONDS);
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 467 */           promise.addListener(new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture future) throws Exception {
/* 470 */               if (future.isCancelled()) {
/* 471 */                 if (AbstractEpollStreamChannel.EpollStreamUnsafe.this.connectTimeoutFuture != null) {
/* 472 */                   AbstractEpollStreamChannel.EpollStreamUnsafe.this.connectTimeoutFuture.cancel(false);
/*     */                 }
/* 474 */                 AbstractEpollStreamChannel.EpollStreamUnsafe.this.connectPromise = null;
/* 475 */                 AbstractEpollStreamChannel.EpollStreamUnsafe.this.close(AbstractEpollStreamChannel.EpollStreamUnsafe.this.voidPromise());
/*     */               }
/*     */             }
/*     */           });
/*     */         }
/*     */       } catch (Throwable t) {
/* 481 */         closeIfClosed();
/* 482 */         promise.tryFailure(annotateConnectException(t, remoteAddress));
/*     */       }
/*     */     }
/*     */     
/*     */     private void fulfillConnectPromise(ChannelPromise promise, boolean wasActive) {
/* 487 */       if (promise == null)
/*     */       {
/* 489 */         return;
/*     */       }
/* 491 */       AbstractEpollStreamChannel.this.active = true;
/*     */       
/*     */ 
/* 494 */       boolean promiseSet = promise.trySuccess();
/*     */       
/*     */ 
/*     */ 
/* 498 */       if ((!wasActive) && (AbstractEpollStreamChannel.this.isActive())) {
/* 499 */         AbstractEpollStreamChannel.this.pipeline().fireChannelActive();
/*     */       }
/*     */       
/*     */ 
/* 503 */       if (!promiseSet) {
/* 504 */         close(voidPromise());
/*     */       }
/*     */     }
/*     */     
/*     */     private void fulfillConnectPromise(ChannelPromise promise, Throwable cause) {
/* 509 */       if (promise == null)
/*     */       {
/* 511 */         return;
/*     */       }
/*     */       
/*     */ 
/* 515 */       promise.tryFailure(cause);
/* 516 */       closeIfClosed();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void finishConnect()
/*     */     {
/* 523 */       assert (AbstractEpollStreamChannel.this.eventLoop().inEventLoop());
/*     */       
/* 525 */       boolean connectStillInProgress = false;
/*     */       try {
/* 527 */         boolean wasActive = AbstractEpollStreamChannel.this.isActive();
/* 528 */         if (!doFinishConnect()) {
/* 529 */           connectStillInProgress = true;
/*     */         }
/*     */         else
/* 532 */           fulfillConnectPromise(this.connectPromise, wasActive);
/*     */       } catch (Throwable t) {
/* 534 */         fulfillConnectPromise(this.connectPromise, annotateConnectException(t, this.requestedRemoteAddress));
/*     */       } finally {
/* 536 */         if (!connectStillInProgress)
/*     */         {
/*     */ 
/* 539 */           if (this.connectTimeoutFuture != null) {
/* 540 */             this.connectTimeoutFuture.cancel(false);
/*     */           }
/* 542 */           this.connectPromise = null;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     void epollOutReady()
/*     */     {
/* 549 */       if (this.connectPromise != null)
/*     */       {
/* 551 */         finishConnect();
/*     */       } else {
/* 553 */         super.epollOutReady();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     private boolean doFinishConnect()
/*     */       throws Exception
/*     */     {
/* 561 */       if (Native.finishConnect(AbstractEpollStreamChannel.this.fd().intValue())) {
/* 562 */         AbstractEpollStreamChannel.this.clearFlag(Native.EPOLLOUT);
/* 563 */         return true;
/*     */       }
/* 565 */       AbstractEpollStreamChannel.this.setFlag(Native.EPOLLOUT);
/* 566 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     void epollRdHupReady()
/*     */     {
/* 572 */       if (AbstractEpollStreamChannel.this.isActive()) {
/* 573 */         epollInReady();
/*     */       } else {
/* 575 */         closeOnRead(AbstractEpollStreamChannel.this.pipeline());
/*     */       }
/*     */     }
/*     */     
/*     */     void epollInReady()
/*     */     {
/* 581 */       ChannelConfig config = AbstractEpollStreamChannel.this.config();
/* 582 */       boolean edgeTriggered = AbstractEpollStreamChannel.this.isFlagSet(Native.EPOLLET);
/*     */       
/* 584 */       if ((!this.readPending) && (!edgeTriggered) && (!config.isAutoRead()))
/*     */       {
/* 586 */         clearEpollIn0();
/* 587 */         return;
/*     */       }
/*     */       
/* 590 */       ChannelPipeline pipeline = AbstractEpollStreamChannel.this.pipeline();
/* 591 */       ByteBufAllocator allocator = config.getAllocator();
/* 592 */       RecvByteBufAllocator.Handle allocHandle = this.allocHandle;
/* 593 */       if (allocHandle == null) {
/* 594 */         this.allocHandle = (allocHandle = config.getRecvByteBufAllocator().newHandle());
/*     */       }
/*     */       
/* 597 */       ByteBuf byteBuf = null;
/* 598 */       boolean close = false;
/*     */       try
/*     */       {
/* 601 */         int maxMessagesPerRead = edgeTriggered ? Integer.MAX_VALUE : config.getMaxMessagesPerRead();
/*     */         
/* 603 */         int messages = 0;
/* 604 */         int totalReadAmount = 0;
/*     */         
/*     */         do
/*     */         {
/* 608 */           byteBuf = allocHandle.allocate(allocator);
/* 609 */           int writable = byteBuf.writableBytes();
/* 610 */           int localReadAmount = AbstractEpollStreamChannel.this.doReadBytes(byteBuf);
/* 611 */           if (localReadAmount <= 0)
/*     */           {
/* 613 */             byteBuf.release();
/* 614 */             close = localReadAmount < 0;
/* 615 */             break;
/*     */           }
/* 617 */           this.readPending = false;
/* 618 */           pipeline.fireChannelRead(byteBuf);
/* 619 */           byteBuf = null;
/*     */           
/* 621 */           if (totalReadAmount >= Integer.MAX_VALUE - localReadAmount) {
/* 622 */             allocHandle.record(totalReadAmount);
/*     */             
/*     */ 
/* 625 */             totalReadAmount = localReadAmount;
/*     */           } else {
/* 627 */             totalReadAmount += localReadAmount;
/*     */           }
/*     */           
/* 630 */           if (localReadAmount < writable) {
/*     */             break;
/*     */           }
/*     */           
/*     */ 
/* 635 */           if ((!edgeTriggered) && (!config.isAutoRead())) {
/*     */             break;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 641 */           messages++; } while (messages < maxMessagesPerRead);
/*     */         
/* 643 */         pipeline.fireChannelReadComplete();
/* 644 */         allocHandle.record(totalReadAmount);
/*     */         
/* 646 */         if (close) {
/* 647 */           closeOnRead(pipeline);
/* 648 */           close = false;
/*     */         }
/*     */       } catch (Throwable t) {
/* 651 */         boolean closed = handleReadException(pipeline, byteBuf, t, close);
/* 652 */         if (!closed)
/*     */         {
/*     */ 
/* 655 */           AbstractEpollStreamChannel.this.eventLoop().execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 658 */               AbstractEpollStreamChannel.EpollStreamUnsafe.this.epollInReady();
/*     */             }
/*     */             
/*     */ 
/*     */           });
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/* 669 */         if ((!this.readPending) && (!config.isAutoRead())) {
/* 670 */           clearEpollIn0();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\AbstractEpollStreamChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */