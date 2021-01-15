/*      */ package io.netty.handler.ssl;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.buffer.ByteBufUtil;
/*      */ import io.netty.buffer.Unpooled;
/*      */ import io.netty.channel.Channel;
/*      */ import io.netty.channel.ChannelConfig;
/*      */ import io.netty.channel.ChannelException;
/*      */ import io.netty.channel.ChannelFuture;
/*      */ import io.netty.channel.ChannelFutureListener;
/*      */ import io.netty.channel.ChannelHandlerContext;
/*      */ import io.netty.channel.ChannelPromise;
/*      */ import io.netty.channel.PendingWriteQueue;
/*      */ import io.netty.handler.codec.ByteToMessageDecoder;
/*      */ import io.netty.util.concurrent.DefaultPromise;
/*      */ import io.netty.util.concurrent.EventExecutor;
/*      */ import io.netty.util.concurrent.Future;
/*      */ import io.netty.util.concurrent.FutureListener;
/*      */ import io.netty.util.concurrent.Promise;
/*      */ import io.netty.util.internal.EmptyArrays;
/*      */ import io.netty.util.internal.OneTimeTask;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import java.io.IOException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.ClosedChannelException;
/*      */ import java.nio.channels.DatagramChannel;
/*      */ import java.nio.channels.SocketChannel;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.ScheduledFuture;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.net.ssl.SSLEngine;
/*      */ import javax.net.ssl.SSLEngineResult;
/*      */ import javax.net.ssl.SSLEngineResult.HandshakeStatus;
/*      */ import javax.net.ssl.SSLEngineResult.Status;
/*      */ import javax.net.ssl.SSLException;
/*      */ import javax.net.ssl.SSLSession;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class SslHandler
/*      */   extends ByteToMessageDecoder
/*      */ {
/*      */   private static final InternalLogger logger;
/*      */   private static final Pattern IGNORABLE_CLASS_IN_STACK;
/*      */   private static final Pattern IGNORABLE_ERROR_MESSAGE;
/*      */   private static final SSLException SSLENGINE_CLOSED;
/*      */   private static final SSLException HANDSHAKE_TIMED_OUT;
/*      */   private static final ClosedChannelException CHANNEL_CLOSED;
/*      */   private volatile ChannelHandlerContext ctx;
/*      */   private final SSLEngine engine;
/*      */   private final int maxPacketBufferSize;
/*      */   
/*      */   static
/*      */   {
/*  157 */     logger = InternalLoggerFactory.getInstance(SslHandler.class);
/*      */     
/*      */ 
/*  160 */     IGNORABLE_CLASS_IN_STACK = Pattern.compile("^.*(?:Socket|Datagram|Sctp|Udt)Channel.*$");
/*      */     
/*  162 */     IGNORABLE_ERROR_MESSAGE = Pattern.compile("^.*(?:connection.*(?:reset|closed|abort|broken)|broken.*pipe).*$", 2);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  170 */     SSLENGINE_CLOSED = new SSLException("SSLEngine closed already");
/*  171 */     HANDSHAKE_TIMED_OUT = new SSLException("handshake timed out");
/*  172 */     CHANNEL_CLOSED = new ClosedChannelException();
/*      */     
/*      */ 
/*  175 */     SSLENGINE_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
/*  176 */     HANDSHAKE_TIMED_OUT.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
/*  177 */     CHANNEL_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  189 */   private final ByteBuffer[] singleBuffer = new ByteBuffer[1];
/*      */   
/*      */ 
/*      */ 
/*      */   private final boolean wantsDirectBuffer;
/*      */   
/*      */ 
/*      */ 
/*      */   private final boolean wantsLargeOutboundNetworkBuffer;
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean wantsInboundHeapBuffer;
/*      */   
/*      */ 
/*      */ 
/*      */   private final boolean startTls;
/*      */   
/*      */ 
/*      */   private boolean sentFirstMessage;
/*      */   
/*      */ 
/*      */   private boolean flushedBeforeHandshake;
/*      */   
/*      */ 
/*      */   private boolean readDuringHandshake;
/*      */   
/*      */ 
/*      */   private PendingWriteQueue pendingUnencryptedWrites;
/*      */   
/*      */ 
/*  220 */   private Promise<Channel> handshakePromise = new LazyChannelPromise(null);
/*  221 */   private final LazyChannelPromise sslCloseFuture = new LazyChannelPromise(null);
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean needsFlush;
/*      */   
/*      */ 
/*      */   private int packetLength;
/*      */   
/*      */ 
/*  231 */   private volatile long handshakeTimeoutMillis = 10000L;
/*  232 */   private volatile long closeNotifyTimeoutMillis = 3000L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SslHandler(SSLEngine engine)
/*      */   {
/*  240 */     this(engine, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SslHandler(SSLEngine engine, boolean startTls)
/*      */   {
/*  251 */     if (engine == null) {
/*  252 */       throw new NullPointerException("engine");
/*      */     }
/*  254 */     this.engine = engine;
/*  255 */     this.startTls = startTls;
/*  256 */     this.maxPacketBufferSize = engine.getSession().getPacketBufferSize();
/*      */     
/*  258 */     boolean opensslEngine = engine instanceof OpenSslEngine;
/*  259 */     this.wantsDirectBuffer = opensslEngine;
/*  260 */     this.wantsLargeOutboundNetworkBuffer = (!opensslEngine);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  270 */     setCumulator(opensslEngine ? COMPOSITE_CUMULATOR : MERGE_CUMULATOR);
/*      */   }
/*      */   
/*      */   public long getHandshakeTimeoutMillis() {
/*  274 */     return this.handshakeTimeoutMillis;
/*      */   }
/*      */   
/*      */   public void setHandshakeTimeout(long handshakeTimeout, TimeUnit unit) {
/*  278 */     if (unit == null) {
/*  279 */       throw new NullPointerException("unit");
/*      */     }
/*      */     
/*  282 */     setHandshakeTimeoutMillis(unit.toMillis(handshakeTimeout));
/*      */   }
/*      */   
/*      */   public void setHandshakeTimeoutMillis(long handshakeTimeoutMillis) {
/*  286 */     if (handshakeTimeoutMillis < 0L) {
/*  287 */       throw new IllegalArgumentException("handshakeTimeoutMillis: " + handshakeTimeoutMillis + " (expected: >= 0)");
/*      */     }
/*      */     
/*  290 */     this.handshakeTimeoutMillis = handshakeTimeoutMillis;
/*      */   }
/*      */   
/*      */   public long getCloseNotifyTimeoutMillis() {
/*  294 */     return this.closeNotifyTimeoutMillis;
/*      */   }
/*      */   
/*      */   public void setCloseNotifyTimeout(long closeNotifyTimeout, TimeUnit unit) {
/*  298 */     if (unit == null) {
/*  299 */       throw new NullPointerException("unit");
/*      */     }
/*      */     
/*  302 */     setCloseNotifyTimeoutMillis(unit.toMillis(closeNotifyTimeout));
/*      */   }
/*      */   
/*      */   public void setCloseNotifyTimeoutMillis(long closeNotifyTimeoutMillis) {
/*  306 */     if (closeNotifyTimeoutMillis < 0L) {
/*  307 */       throw new IllegalArgumentException("closeNotifyTimeoutMillis: " + closeNotifyTimeoutMillis + " (expected: >= 0)");
/*      */     }
/*      */     
/*  310 */     this.closeNotifyTimeoutMillis = closeNotifyTimeoutMillis;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public SSLEngine engine()
/*      */   {
/*  317 */     return this.engine;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Future<Channel> handshakeFuture()
/*      */   {
/*  327 */     return this.handshakePromise;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChannelFuture close()
/*      */   {
/*  335 */     return close(this.ctx.newPromise());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ChannelFuture close(final ChannelPromise future)
/*      */   {
/*  342 */     final ChannelHandlerContext ctx = this.ctx;
/*  343 */     ctx.executor().execute(new Runnable()
/*      */     {
/*      */       public void run() {
/*  346 */         SslHandler.this.engine.closeOutbound();
/*      */         try {
/*  348 */           SslHandler.this.write(ctx, Unpooled.EMPTY_BUFFER, future);
/*  349 */           SslHandler.this.flush(ctx);
/*      */         } catch (Exception e) {
/*  351 */           if (!future.tryFailure(e)) {
/*  352 */             SslHandler.logger.warn("{} flush() raised a masked exception.", ctx.channel(), e);
/*      */           }
/*      */           
/*      */         }
/*      */       }
/*  357 */     });
/*  358 */     return future;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Future<Channel> sslCloseFuture()
/*      */   {
/*  369 */     return this.sslCloseFuture;
/*      */   }
/*      */   
/*      */   public void handlerRemoved0(ChannelHandlerContext ctx) throws Exception
/*      */   {
/*  374 */     if (!this.pendingUnencryptedWrites.isEmpty())
/*      */     {
/*  376 */       this.pendingUnencryptedWrites.removeAndFailAll(new ChannelException("Pending write on removal of SslHandler"));
/*      */     }
/*      */   }
/*      */   
/*      */   public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise)
/*      */     throws Exception
/*      */   {
/*  383 */     closeOutboundAndChannel(ctx, promise, true);
/*      */   }
/*      */   
/*      */   public void close(ChannelHandlerContext ctx, ChannelPromise promise)
/*      */     throws Exception
/*      */   {
/*  389 */     closeOutboundAndChannel(ctx, promise, false);
/*      */   }
/*      */   
/*      */   public void read(ChannelHandlerContext ctx) throws Exception
/*      */   {
/*  394 */     if (!this.handshakePromise.isDone()) {
/*  395 */       this.readDuringHandshake = true;
/*      */     }
/*      */     
/*  398 */     ctx.read();
/*      */   }
/*      */   
/*      */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*      */   {
/*  403 */     this.pendingUnencryptedWrites.add(msg, promise);
/*      */   }
/*      */   
/*      */ 
/*      */   public void flush(ChannelHandlerContext ctx)
/*      */     throws Exception
/*      */   {
/*  410 */     if ((this.startTls) && (!this.sentFirstMessage)) {
/*  411 */       this.sentFirstMessage = true;
/*  412 */       this.pendingUnencryptedWrites.removeAndWriteAll();
/*  413 */       ctx.flush();
/*  414 */       return;
/*      */     }
/*  416 */     if (this.pendingUnencryptedWrites.isEmpty())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  421 */       this.pendingUnencryptedWrites.add(Unpooled.EMPTY_BUFFER, ctx.newPromise());
/*      */     }
/*  423 */     if (!this.handshakePromise.isDone()) {
/*  424 */       this.flushedBeforeHandshake = true;
/*      */     }
/*  426 */     wrap(ctx, false);
/*  427 */     ctx.flush();
/*      */   }
/*      */   
/*      */   private void wrap(ChannelHandlerContext ctx, boolean inUnwrap) throws SSLException {
/*  431 */     ByteBuf out = null;
/*  432 */     ChannelPromise promise = null;
/*  433 */     ByteBufAllocator alloc = ctx.alloc();
/*      */     try {
/*      */       for (;;) {
/*  436 */         Object msg = this.pendingUnencryptedWrites.current();
/*  437 */         if (msg == null) {
/*      */           break;
/*      */         }
/*      */         
/*  441 */         if (!(msg instanceof ByteBuf)) {
/*  442 */           this.pendingUnencryptedWrites.removeAndWrite();
/*      */         }
/*      */         else
/*      */         {
/*  446 */           ByteBuf buf = (ByteBuf)msg;
/*  447 */           if (out == null) {
/*  448 */             out = allocateOutNetBuf(ctx, buf.readableBytes());
/*      */           }
/*      */           
/*  451 */           SSLEngineResult result = wrap(alloc, this.engine, buf, out);
/*  452 */           if (!buf.isReadable()) {
/*  453 */             promise = this.pendingUnencryptedWrites.remove();
/*      */           } else {
/*  455 */             promise = null;
/*      */           }
/*      */           
/*  458 */           if (result.getStatus() == SSLEngineResult.Status.CLOSED)
/*      */           {
/*      */ 
/*  461 */             this.pendingUnencryptedWrites.removeAndFailAll(SSLENGINE_CLOSED); return;
/*      */           }
/*      */           
/*  464 */           switch (result.getHandshakeStatus()) {
/*      */           case NEED_TASK: 
/*  466 */             runDelegatedTasks();
/*  467 */             break;
/*      */           case FINISHED: 
/*  469 */             setHandshakeSuccess();
/*      */           
/*      */           case NOT_HANDSHAKING: 
/*  472 */             setHandshakeSuccessIfStillHandshaking();
/*      */           
/*      */           case NEED_WRAP: 
/*  475 */             finishWrap(ctx, out, promise, inUnwrap);
/*  476 */             promise = null;
/*  477 */             out = null;
/*  478 */             break;
/*      */           case NEED_UNWRAP: 
/*      */             return;
/*      */           default: 
/*  482 */             throw new IllegalStateException("Unknown handshake status: " + result.getHandshakeStatus());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (SSLException e) {
/*  488 */       setHandshakeFailure(ctx, e);
/*  489 */       throw e;
/*      */     } finally {
/*  491 */       finishWrap(ctx, out, promise, inUnwrap);
/*      */     }
/*      */   }
/*      */   
/*      */   private void finishWrap(ChannelHandlerContext ctx, ByteBuf out, ChannelPromise promise, boolean inUnwrap) {
/*  496 */     if (out == null) {
/*  497 */       out = Unpooled.EMPTY_BUFFER;
/*  498 */     } else if (!out.isReadable()) {
/*  499 */       out.release();
/*  500 */       out = Unpooled.EMPTY_BUFFER;
/*      */     }
/*      */     
/*  503 */     if (promise != null) {
/*  504 */       ctx.write(out, promise);
/*      */     } else {
/*  506 */       ctx.write(out);
/*      */     }
/*      */     
/*  509 */     if (inUnwrap) {
/*  510 */       this.needsFlush = true;
/*      */     }
/*      */   }
/*      */   
/*      */   private void wrapNonAppData(ChannelHandlerContext ctx, boolean inUnwrap) throws SSLException {
/*  515 */     ByteBuf out = null;
/*  516 */     ByteBufAllocator alloc = ctx.alloc();
/*      */     try {
/*      */       for (;;) {
/*  519 */         if (out == null) {
/*  520 */           out = allocateOutNetBuf(ctx, 0);
/*      */         }
/*  522 */         SSLEngineResult result = wrap(alloc, this.engine, Unpooled.EMPTY_BUFFER, out);
/*      */         
/*  524 */         if (result.bytesProduced() > 0) {
/*  525 */           ctx.write(out);
/*  526 */           if (inUnwrap) {
/*  527 */             this.needsFlush = true;
/*      */           }
/*  529 */           out = null;
/*      */         }
/*      */         
/*  532 */         switch (result.getHandshakeStatus()) {
/*      */         case FINISHED: 
/*  534 */           setHandshakeSuccess();
/*  535 */           break;
/*      */         case NEED_TASK: 
/*  537 */           runDelegatedTasks();
/*  538 */           break;
/*      */         case NEED_UNWRAP: 
/*  540 */           if (!inUnwrap) {
/*  541 */             unwrapNonAppData(ctx);
/*      */           }
/*      */           break;
/*      */         case NEED_WRAP: 
/*      */           break;
/*      */         case NOT_HANDSHAKING: 
/*  547 */           setHandshakeSuccessIfStillHandshaking();
/*      */           
/*      */ 
/*  550 */           if (!inUnwrap) {
/*  551 */             unwrapNonAppData(ctx);
/*      */           }
/*      */           break;
/*      */         default: 
/*  555 */           throw new IllegalStateException("Unknown handshake status: " + result.getHandshakeStatus());
/*      */         }
/*      */         
/*  558 */         if (result.bytesProduced() == 0) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     } catch (SSLException e) {
/*  563 */       setHandshakeFailure(ctx, e);
/*  564 */       throw e;
/*      */     } finally {
/*  566 */       if (out != null) {
/*  567 */         out.release();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void channelInactive(ChannelHandlerContext ctx)
/*      */     throws Exception
/*      */   {
/*  633 */     setHandshakeFailure(ctx, CHANNEL_CLOSED);
/*  634 */     super.channelInactive(ctx);
/*      */   }
/*      */   
/*      */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
/*      */   {
/*  639 */     if (ignoreException(cause))
/*      */     {
/*      */ 
/*  642 */       if (logger.isDebugEnabled()) {
/*  643 */         logger.debug("{} Swallowing a harmless 'connection reset by peer / broken pipe' error that occurred while writing close_notify in response to the peer's close_notify", ctx.channel(), cause);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  650 */       if (ctx.channel().isActive()) {
/*  651 */         ctx.close();
/*      */       }
/*      */     } else {
/*  654 */       ctx.fireExceptionCaught(cause);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean ignoreException(Throwable t)
/*      */   {
/*  668 */     if ((!(t instanceof SSLException)) && ((t instanceof IOException)) && (this.sslCloseFuture.isDone())) {
/*  669 */       String message = String.valueOf(t.getMessage()).toLowerCase();
/*      */       
/*      */ 
/*      */ 
/*  673 */       if (IGNORABLE_ERROR_MESSAGE.matcher(message).matches()) {
/*  674 */         return true;
/*      */       }
/*      */       
/*      */ 
/*  678 */       StackTraceElement[] elements = t.getStackTrace();
/*  679 */       for (StackTraceElement element : elements) {
/*  680 */         String classname = element.getClassName();
/*  681 */         String methodname = element.getMethodName();
/*      */         
/*      */ 
/*  684 */         if (!classname.startsWith("io.netty."))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*  689 */           if ("read".equals(methodname))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  695 */             if (IGNORABLE_CLASS_IN_STACK.matcher(classname).matches()) {
/*  696 */               return true;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */             try
/*      */             {
/*  703 */               Class<?> clazz = PlatformDependent.getClassLoader(getClass()).loadClass(classname);
/*      */               
/*  705 */               if ((SocketChannel.class.isAssignableFrom(clazz)) || (DatagramChannel.class.isAssignableFrom(clazz)))
/*      */               {
/*  707 */                 return true;
/*      */               }
/*      */               
/*      */ 
/*  711 */               if ((PlatformDependent.javaVersion() >= 7) && ("com.sun.nio.sctp.SctpChannel".equals(clazz.getSuperclass().getName())))
/*      */               {
/*  713 */                 return true;
/*      */               }
/*      */             }
/*      */             catch (ClassNotFoundException e) {}
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  721 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isEncrypted(ByteBuf buffer)
/*      */   {
/*  737 */     if (buffer.readableBytes() < 5) {
/*  738 */       throw new IllegalArgumentException("buffer must have at least 5 readable bytes");
/*      */     }
/*  740 */     return getEncryptedPacketLength(buffer, buffer.readerIndex()) != -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int getEncryptedPacketLength(ByteBuf buffer, int offset)
/*      */   {
/*  757 */     int packetLength = 0;
/*      */     
/*      */     boolean tls;
/*      */     
/*  761 */     switch (buffer.getUnsignedByte(offset)) {
/*      */     case 20: 
/*      */     case 21: 
/*      */     case 22: 
/*      */     case 23: 
/*  766 */       tls = true;
/*  767 */       break;
/*      */     
/*      */     default: 
/*  770 */       tls = false;
/*      */     }
/*      */     
/*  773 */     if (tls)
/*      */     {
/*  775 */       int majorVersion = buffer.getUnsignedByte(offset + 1);
/*  776 */       if (majorVersion == 3)
/*      */       {
/*  778 */         packetLength = buffer.getUnsignedShort(offset + 3) + 5;
/*  779 */         if (packetLength <= 5)
/*      */         {
/*  781 */           tls = false;
/*      */         }
/*      */       }
/*      */       else {
/*  785 */         tls = false;
/*      */       }
/*      */     }
/*      */     
/*  789 */     if (!tls)
/*      */     {
/*  791 */       boolean sslv2 = true;
/*  792 */       int headerLength = (buffer.getUnsignedByte(offset) & 0x80) != 0 ? 2 : 3;
/*  793 */       int majorVersion = buffer.getUnsignedByte(offset + headerLength + 1);
/*  794 */       if ((majorVersion == 2) || (majorVersion == 3))
/*      */       {
/*  796 */         if (headerLength == 2) {
/*  797 */           packetLength = (buffer.getShort(offset) & 0x7FFF) + 2;
/*      */         } else {
/*  799 */           packetLength = (buffer.getShort(offset) & 0x3FFF) + 3;
/*      */         }
/*  801 */         if (packetLength <= headerLength) {
/*  802 */           sslv2 = false;
/*      */         }
/*      */       } else {
/*  805 */         sslv2 = false;
/*      */       }
/*      */       
/*  808 */       if (!sslv2) {
/*  809 */         return -1;
/*      */       }
/*      */     }
/*  812 */     return packetLength;
/*      */   }
/*      */   
/*      */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws SSLException
/*      */   {
/*  817 */     int startOffset = in.readerIndex();
/*  818 */     int endOffset = in.writerIndex();
/*  819 */     int offset = startOffset;
/*  820 */     int totalLength = 0;
/*      */     
/*      */ 
/*  823 */     if (this.packetLength > 0) {
/*  824 */       if (endOffset - startOffset < this.packetLength) {
/*  825 */         return;
/*      */       }
/*  827 */       offset += this.packetLength;
/*  828 */       totalLength = this.packetLength;
/*  829 */       this.packetLength = 0;
/*      */     }
/*      */     
/*      */ 
/*  833 */     boolean nonSslRecord = false;
/*      */     
/*  835 */     while (totalLength < 18713) {
/*  836 */       int readableBytes = endOffset - offset;
/*  837 */       if (readableBytes < 5) {
/*      */         break;
/*      */       }
/*      */       
/*  841 */       int packetLength = getEncryptedPacketLength(in, offset);
/*  842 */       if (packetLength == -1) {
/*  843 */         nonSslRecord = true;
/*  844 */         break;
/*      */       }
/*      */       
/*  847 */       assert (packetLength > 0);
/*      */       
/*  849 */       if (packetLength > readableBytes)
/*      */       {
/*  851 */         this.packetLength = packetLength;
/*  852 */         break;
/*      */       }
/*      */       
/*  855 */       int newTotalLength = totalLength + packetLength;
/*  856 */       if (newTotalLength > 18713) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  863 */       offset += packetLength;
/*  864 */       totalLength = newTotalLength;
/*      */     }
/*      */     
/*  867 */     if (totalLength > 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  879 */       in.skipBytes(totalLength);
/*      */       
/*      */ 
/*  882 */       if ((in.isDirect()) && (this.wantsInboundHeapBuffer)) {
/*  883 */         ByteBuf copy = ctx.alloc().heapBuffer(totalLength);
/*      */         try {
/*  885 */           copy.writeBytes(in, startOffset, totalLength);
/*  886 */           unwrap(ctx, copy, 0, totalLength);
/*      */         } finally {
/*  888 */           copy.release();
/*      */         }
/*      */       } else {
/*  891 */         unwrap(ctx, in, startOffset, totalLength);
/*      */       }
/*      */     }
/*      */     
/*  895 */     if (nonSslRecord)
/*      */     {
/*  897 */       NotSslRecordException e = new NotSslRecordException("not an SSL/TLS record: " + ByteBufUtil.hexDump(in));
/*      */       
/*  899 */       in.skipBytes(in.readableBytes());
/*  900 */       ctx.fireExceptionCaught(e);
/*  901 */       setHandshakeFailure(ctx, e);
/*      */     }
/*      */   }
/*      */   
/*      */   public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
/*      */   {
/*  907 */     if (this.needsFlush) {
/*  908 */       this.needsFlush = false;
/*  909 */       ctx.flush();
/*      */     }
/*      */     
/*      */ 
/*  913 */     if ((!this.handshakePromise.isDone()) && (!ctx.channel().config().isAutoRead())) {
/*  914 */       ctx.read();
/*      */     }
/*      */     
/*  917 */     ctx.fireChannelReadComplete();
/*      */   }
/*      */   
/*      */ 
/*      */   private void unwrapNonAppData(ChannelHandlerContext ctx)
/*      */     throws SSLException
/*      */   {
/*  924 */     unwrap(ctx, Unpooled.EMPTY_BUFFER, 0, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void unwrap(ChannelHandlerContext ctx, ByteBuf packet, int offset, int length)
/*      */     throws SSLException
/*      */   {
/*  933 */     boolean wrapLater = false;
/*  934 */     boolean notifyClosure = false;
/*  935 */     ByteBuf decodeOut = allocate(ctx, length);
/*      */     try {
/*      */       for (;;) {
/*  938 */         SSLEngineResult result = unwrap(this.engine, packet, offset, length, decodeOut);
/*  939 */         SSLEngineResult.Status status = result.getStatus();
/*  940 */         SSLEngineResult.HandshakeStatus handshakeStatus = result.getHandshakeStatus();
/*  941 */         int produced = result.bytesProduced();
/*  942 */         int consumed = result.bytesConsumed();
/*      */         
/*      */ 
/*  945 */         offset += consumed;
/*  946 */         length -= consumed;
/*      */         
/*  948 */         if (status == SSLEngineResult.Status.CLOSED)
/*      */         {
/*  950 */           notifyClosure = true;
/*      */         }
/*      */         
/*  953 */         switch (handshakeStatus) {
/*      */         case NEED_UNWRAP: 
/*      */           break;
/*      */         case NEED_WRAP: 
/*  957 */           wrapNonAppData(ctx, true);
/*  958 */           break;
/*      */         case NEED_TASK: 
/*  960 */           runDelegatedTasks();
/*  961 */           break;
/*      */         case FINISHED: 
/*  963 */           setHandshakeSuccess();
/*  964 */           wrapLater = true;
/*  965 */           break;
/*      */         case NOT_HANDSHAKING: 
/*  967 */           if (setHandshakeSuccessIfStillHandshaking()) {
/*  968 */             wrapLater = true;
/*      */ 
/*      */           }
/*  971 */           else if (this.flushedBeforeHandshake)
/*      */           {
/*      */ 
/*      */ 
/*  975 */             this.flushedBeforeHandshake = false;
/*  976 */             wrapLater = true;
/*      */           }
/*      */           
/*      */           break;
/*      */         default: 
/*  981 */           throw new IllegalStateException("unknown handshake status: " + handshakeStatus);
/*      */           
/*      */ 
/*  984 */           if ((status == SSLEngineResult.Status.BUFFER_UNDERFLOW) || ((consumed == 0) && (produced == 0)))
/*      */             break label237;
/*      */         }
/*      */       }
/*      */       label237:
/*  989 */       if (wrapLater) {
/*  990 */         wrap(ctx, true);
/*      */       }
/*      */       
/*  993 */       if (notifyClosure) {
/*  994 */         this.sslCloseFuture.trySuccess(ctx.channel());
/*      */       }
/*      */     } catch (SSLException e) {
/*  997 */       setHandshakeFailure(ctx, e);
/*  998 */       throw e;
/*      */     } finally {
/* 1000 */       if (decodeOut.isReadable()) {
/* 1001 */         ctx.fireChannelRead(decodeOut);
/*      */       } else {
/* 1003 */         decodeOut.release();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void runDelegatedTasks()
/*      */   {
/*      */     for (;;)
/*      */     {
/* 1096 */       Runnable task = this.engine.getDelegatedTask();
/* 1097 */       if (task == null) {
/*      */         break;
/*      */       }
/*      */       
/* 1101 */       task.run();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean setHandshakeSuccessIfStillHandshaking()
/*      */   {
/* 1113 */     if (!this.handshakePromise.isDone()) {
/* 1114 */       setHandshakeSuccess();
/* 1115 */       return true;
/*      */     }
/* 1117 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setHandshakeSuccess()
/*      */   {
/* 1125 */     String cipherSuite = String.valueOf(this.engine.getSession().getCipherSuite());
/* 1126 */     if ((!this.wantsDirectBuffer) && ((cipherSuite.contains("_GCM_")) || (cipherSuite.contains("-GCM-")))) {
/* 1127 */       this.wantsInboundHeapBuffer = true;
/*      */     }
/*      */     
/* 1130 */     this.handshakePromise.trySuccess(this.ctx.channel());
/*      */     
/* 1132 */     if (logger.isDebugEnabled()) {
/* 1133 */       logger.debug("{} HANDSHAKEN: {}", this.ctx.channel(), this.engine.getSession().getCipherSuite());
/*      */     }
/* 1135 */     this.ctx.fireUserEventTriggered(SslHandshakeCompletionEvent.SUCCESS);
/*      */     
/* 1137 */     if ((this.readDuringHandshake) && (!this.ctx.channel().config().isAutoRead())) {
/* 1138 */       this.readDuringHandshake = false;
/* 1139 */       this.ctx.read();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setHandshakeFailure(ChannelHandlerContext ctx, Throwable cause)
/*      */   {
/* 1149 */     this.engine.closeOutbound();
/*      */     try
/*      */     {
/* 1152 */       this.engine.closeInbound();
/*      */ 
/*      */     }
/*      */     catch (SSLException e)
/*      */     {
/*      */ 
/* 1158 */       String msg = e.getMessage();
/* 1159 */       if ((msg == null) || (!msg.contains("possible truncation attack"))) {
/* 1160 */         logger.debug("{} SSLEngine.closeInbound() raised an exception.", ctx.channel(), e);
/*      */       }
/*      */     }
/* 1163 */     notifyHandshakeFailure(cause);
/* 1164 */     this.pendingUnencryptedWrites.removeAndFailAll(cause);
/*      */   }
/*      */   
/*      */   private void notifyHandshakeFailure(Throwable cause) {
/* 1168 */     if (this.handshakePromise.tryFailure(cause)) {
/* 1169 */       this.ctx.fireUserEventTriggered(new SslHandshakeCompletionEvent(cause));
/* 1170 */       this.ctx.close();
/*      */     }
/*      */   }
/*      */   
/*      */   private void closeOutboundAndChannel(ChannelHandlerContext ctx, ChannelPromise promise, boolean disconnect) throws Exception
/*      */   {
/* 1176 */     if (!ctx.channel().isActive()) {
/* 1177 */       if (disconnect) {
/* 1178 */         ctx.disconnect(promise);
/*      */       } else {
/* 1180 */         ctx.close(promise);
/*      */       }
/* 1182 */       return;
/*      */     }
/*      */     
/* 1185 */     this.engine.closeOutbound();
/*      */     
/* 1187 */     ChannelPromise closeNotifyFuture = ctx.newPromise();
/* 1188 */     write(ctx, Unpooled.EMPTY_BUFFER, closeNotifyFuture);
/* 1189 */     flush(ctx);
/* 1190 */     safeClose(ctx, closeNotifyFuture, promise);
/*      */   }
/*      */   
/*      */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception
/*      */   {
/* 1195 */     this.ctx = ctx;
/* 1196 */     this.pendingUnencryptedWrites = new PendingWriteQueue(ctx);
/*      */     
/* 1198 */     if ((ctx.channel().isActive()) && (this.engine.getUseClientMode()))
/*      */     {
/*      */ 
/*      */ 
/* 1202 */       handshake(null);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Future<Channel> renegotiate()
/*      */   {
/* 1213 */     ChannelHandlerContext ctx = this.ctx;
/* 1214 */     if (ctx == null) {
/* 1215 */       throw new IllegalStateException();
/*      */     }
/*      */     
/* 1218 */     return renegotiate(ctx.executor().newPromise());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Future<Channel> renegotiate(final Promise<Channel> promise)
/*      */   {
/* 1225 */     if (promise == null) {
/* 1226 */       throw new NullPointerException("promise");
/*      */     }
/*      */     
/* 1229 */     ChannelHandlerContext ctx = this.ctx;
/* 1230 */     if (ctx == null) {
/* 1231 */       throw new IllegalStateException();
/*      */     }
/*      */     
/* 1234 */     EventExecutor executor = ctx.executor();
/* 1235 */     if (!executor.inEventLoop()) {
/* 1236 */       executor.execute(new OneTimeTask()
/*      */       {
/*      */         public void run() {
/* 1239 */           SslHandler.this.handshake(promise);
/*      */         }
/* 1241 */       });
/* 1242 */       return promise;
/*      */     }
/*      */     
/* 1245 */     handshake(promise);
/* 1246 */     return promise;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void handshake(final Promise<Channel> newHandshakePromise)
/*      */   {
/*      */     final Promise<Channel> p;
/*      */     
/*      */ 
/*      */ 
/* 1258 */     if (newHandshakePromise != null) {
/* 1259 */       Promise<Channel> oldHandshakePromise = this.handshakePromise;
/* 1260 */       if (!oldHandshakePromise.isDone())
/*      */       {
/*      */ 
/* 1263 */         oldHandshakePromise.addListener(new FutureListener()
/*      */         {
/*      */           public void operationComplete(Future<Channel> future) throws Exception {
/* 1266 */             if (future.isSuccess()) {
/* 1267 */               newHandshakePromise.setSuccess(future.getNow());
/*      */             } else {
/* 1269 */               newHandshakePromise.setFailure(future.cause());
/*      */             }
/*      */           }
/*      */         }); return;
/*      */       }
/*      */       
/*      */       Promise<Channel> p;
/* 1276 */       this.handshakePromise = (p = newHandshakePromise);
/*      */     }
/*      */     else {
/* 1279 */       p = this.handshakePromise;
/* 1280 */       assert (!p.isDone());
/*      */     }
/*      */     
/*      */ 
/* 1284 */     ChannelHandlerContext ctx = this.ctx;
/*      */     try {
/* 1286 */       this.engine.beginHandshake();
/* 1287 */       wrapNonAppData(ctx, false);
/* 1288 */       ctx.flush();
/*      */     } catch (Exception e) {
/* 1290 */       notifyHandshakeFailure(e);
/*      */     }
/*      */     
/*      */ 
/* 1294 */     long handshakeTimeoutMillis = this.handshakeTimeoutMillis;
/* 1295 */     if ((handshakeTimeoutMillis <= 0L) || (p.isDone())) {
/* 1296 */       return;
/*      */     }
/*      */     
/* 1299 */     final ScheduledFuture<?> timeoutFuture = ctx.executor().schedule(new Runnable()
/*      */     {
/*      */       public void run() {
/* 1302 */         if (p.isDone()) {
/* 1303 */           return;
/*      */         }
/* 1305 */         SslHandler.this.notifyHandshakeFailure(SslHandler.HANDSHAKE_TIMED_OUT); } }, handshakeTimeoutMillis, TimeUnit.MILLISECONDS);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1310 */     p.addListener(new FutureListener()
/*      */     {
/*      */       public void operationComplete(Future<Channel> f) throws Exception {
/* 1313 */         timeoutFuture.cancel(false);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void channelActive(ChannelHandlerContext ctx)
/*      */     throws Exception
/*      */   {
/* 1323 */     if ((!this.startTls) && (this.engine.getUseClientMode()))
/*      */     {
/* 1325 */       handshake(null);
/*      */     }
/* 1327 */     ctx.fireChannelActive();
/*      */   }
/*      */   
/*      */ 
/*      */   private void safeClose(final ChannelHandlerContext ctx, ChannelFuture flushFuture, final ChannelPromise promise)
/*      */   {
/* 1333 */     if (!ctx.channel().isActive()) {
/* 1334 */       ctx.close(promise); return;
/*      */     }
/*      */     
/*      */     ScheduledFuture<?> timeoutFuture;
/*      */     final ScheduledFuture<?> timeoutFuture;
/* 1339 */     if (this.closeNotifyTimeoutMillis > 0L)
/*      */     {
/* 1341 */       timeoutFuture = ctx.executor().schedule(new Runnable()
/*      */       {
/*      */         public void run() {
/* 1344 */           SslHandler.logger.warn("{} Last write attempt timed out; force-closing the connection.", ctx.channel());
/* 1345 */           ctx.close(promise); } }, this.closeNotifyTimeoutMillis, TimeUnit.MILLISECONDS);
/*      */     }
/*      */     else
/*      */     {
/* 1349 */       timeoutFuture = null;
/*      */     }
/*      */     
/*      */ 
/* 1353 */     flushFuture.addListener(new ChannelFutureListener()
/*      */     {
/*      */       public void operationComplete(ChannelFuture f) throws Exception
/*      */       {
/* 1357 */         if (timeoutFuture != null) {
/* 1358 */           timeoutFuture.cancel(false);
/*      */         }
/*      */         
/*      */ 
/* 1362 */         ctx.close(promise);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private ByteBuf allocate(ChannelHandlerContext ctx, int capacity)
/*      */   {
/* 1372 */     ByteBufAllocator alloc = ctx.alloc();
/* 1373 */     if (this.wantsDirectBuffer) {
/* 1374 */       return alloc.directBuffer(capacity);
/*      */     }
/* 1376 */     return alloc.buffer(capacity);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private ByteBuf allocateOutNetBuf(ChannelHandlerContext ctx, int pendingBytes)
/*      */   {
/* 1385 */     if (this.wantsLargeOutboundNetworkBuffer) {
/* 1386 */       return allocate(ctx, this.maxPacketBufferSize);
/*      */     }
/* 1388 */     return allocate(ctx, Math.min(pendingBytes + 2329, this.maxPacketBufferSize));
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private SSLEngineResult wrap(ByteBufAllocator alloc, SSLEngine engine, ByteBuf in, ByteBuf out)
/*      */     throws SSLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore 5
/*      */     //   3: aload_3
/*      */     //   4: invokevirtual 403	io/netty/buffer/ByteBuf:readerIndex	()I
/*      */     //   7: istore 6
/*      */     //   9: aload_3
/*      */     //   10: invokevirtual 314	io/netty/buffer/ByteBuf:readableBytes	()I
/*      */     //   13: istore 7
/*      */     //   15: aload_3
/*      */     //   16: invokevirtual 406	io/netty/buffer/ByteBuf:isDirect	()Z
/*      */     //   19: ifne +10 -> 29
/*      */     //   22: aload_0
/*      */     //   23: getfield 131	io/netty/handler/ssl/SslHandler:wantsDirectBuffer	Z
/*      */     //   26: ifne +48 -> 74
/*      */     //   29: aload_3
/*      */     //   30: instanceof 408
/*      */     //   33: ifne +32 -> 65
/*      */     //   36: aload_3
/*      */     //   37: invokevirtual 411	io/netty/buffer/ByteBuf:nioBufferCount	()I
/*      */     //   40: iconst_1
/*      */     //   41: if_icmpne +24 -> 65
/*      */     //   44: aload_0
/*      */     //   45: getfield 88	io/netty/handler/ssl/SslHandler:singleBuffer	[Ljava/nio/ByteBuffer;
/*      */     //   48: astore 8
/*      */     //   50: aload 8
/*      */     //   52: iconst_0
/*      */     //   53: aload_3
/*      */     //   54: iload 6
/*      */     //   56: iload 7
/*      */     //   58: invokevirtual 415	io/netty/buffer/ByteBuf:internalNioBuffer	(II)Ljava/nio/ByteBuffer;
/*      */     //   61: aastore
/*      */     //   62: goto +51 -> 113
/*      */     //   65: aload_3
/*      */     //   66: invokevirtual 419	io/netty/buffer/ByteBuf:nioBuffers	()[Ljava/nio/ByteBuffer;
/*      */     //   69: astore 8
/*      */     //   71: goto +42 -> 113
/*      */     //   74: aload_1
/*      */     //   75: iload 7
/*      */     //   77: invokeinterface 423 2 0
/*      */     //   82: astore 5
/*      */     //   84: aload 5
/*      */     //   86: aload_3
/*      */     //   87: iload 6
/*      */     //   89: iload 7
/*      */     //   91: invokevirtual 427	io/netty/buffer/ByteBuf:writeBytes	(Lio/netty/buffer/ByteBuf;II)Lio/netty/buffer/ByteBuf;
/*      */     //   94: pop
/*      */     //   95: aload_0
/*      */     //   96: getfield 88	io/netty/handler/ssl/SslHandler:singleBuffer	[Ljava/nio/ByteBuffer;
/*      */     //   99: astore 8
/*      */     //   101: aload 8
/*      */     //   103: iconst_0
/*      */     //   104: aload 5
/*      */     //   106: iconst_0
/*      */     //   107: iload 7
/*      */     //   109: invokevirtual 415	io/netty/buffer/ByteBuf:internalNioBuffer	(II)Ljava/nio/ByteBuffer;
/*      */     //   112: aastore
/*      */     //   113: aload 4
/*      */     //   115: aload 4
/*      */     //   117: invokevirtual 431	io/netty/buffer/ByteBuf:writerIndex	()I
/*      */     //   120: aload 4
/*      */     //   122: invokevirtual 434	io/netty/buffer/ByteBuf:writableBytes	()I
/*      */     //   125: invokevirtual 437	io/netty/buffer/ByteBuf:nioBuffer	(II)Ljava/nio/ByteBuffer;
/*      */     //   128: astore 9
/*      */     //   130: aload_2
/*      */     //   131: aload 8
/*      */     //   133: aload 9
/*      */     //   135: invokevirtual 440	javax/net/ssl/SSLEngine:wrap	([Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)Ljavax/net/ssl/SSLEngineResult;
/*      */     //   138: astore 10
/*      */     //   140: aload_3
/*      */     //   141: aload 10
/*      */     //   143: invokevirtual 443	javax/net/ssl/SSLEngineResult:bytesConsumed	()I
/*      */     //   146: invokevirtual 446	io/netty/buffer/ByteBuf:skipBytes	(I)Lio/netty/buffer/ByteBuf;
/*      */     //   149: pop
/*      */     //   150: aload 4
/*      */     //   152: aload 4
/*      */     //   154: invokevirtual 431	io/netty/buffer/ByteBuf:writerIndex	()I
/*      */     //   157: aload 10
/*      */     //   159: invokevirtual 397	javax/net/ssl/SSLEngineResult:bytesProduced	()I
/*      */     //   162: iadd
/*      */     //   163: invokevirtual 448	io/netty/buffer/ByteBuf:writerIndex	(I)Lio/netty/buffer/ByteBuf;
/*      */     //   166: pop
/*      */     //   167: getstatic 451	io/netty/handler/ssl/SslHandler$8:$SwitchMap$javax$net$ssl$SSLEngineResult$Status	[I
/*      */     //   170: aload 10
/*      */     //   172: invokevirtual 331	javax/net/ssl/SSLEngineResult:getStatus	()Ljavax/net/ssl/SSLEngineResult$Status;
/*      */     //   175: invokevirtual 452	javax/net/ssl/SSLEngineResult$Status:ordinal	()I
/*      */     //   178: iaload
/*      */     //   179: lookupswitch	default:+30->209, 1:+17->196
/*      */     //   196: aload 4
/*      */     //   198: aload_0
/*      */     //   199: getfield 127	io/netty/handler/ssl/SslHandler:maxPacketBufferSize	I
/*      */     //   202: invokevirtual 455	io/netty/buffer/ByteBuf:ensureWritable	(I)Lio/netty/buffer/ByteBuf;
/*      */     //   205: pop
/*      */     //   206: goto +28 -> 234
/*      */     //   209: aload 10
/*      */     //   211: astore 11
/*      */     //   213: aload_0
/*      */     //   214: getfield 88	io/netty/handler/ssl/SslHandler:singleBuffer	[Ljava/nio/ByteBuffer;
/*      */     //   217: iconst_0
/*      */     //   218: aconst_null
/*      */     //   219: aastore
/*      */     //   220: aload 5
/*      */     //   222: ifnull +9 -> 231
/*      */     //   225: aload 5
/*      */     //   227: invokevirtual 385	io/netty/buffer/ByteBuf:release	()Z
/*      */     //   230: pop
/*      */     //   231: aload 11
/*      */     //   233: areturn
/*      */     //   234: goto -121 -> 113
/*      */     //   237: astore 12
/*      */     //   239: aload_0
/*      */     //   240: getfield 88	io/netty/handler/ssl/SslHandler:singleBuffer	[Ljava/nio/ByteBuffer;
/*      */     //   243: iconst_0
/*      */     //   244: aconst_null
/*      */     //   245: aastore
/*      */     //   246: aload 5
/*      */     //   248: ifnull +9 -> 257
/*      */     //   251: aload 5
/*      */     //   253: invokevirtual 385	io/netty/buffer/ByteBuf:release	()Z
/*      */     //   256: pop
/*      */     //   257: aload 12
/*      */     //   259: athrow
/*      */     // Line number table:
/*      */     //   Java source line #574	-> byte code offset #0
/*      */     //   Java source line #576	-> byte code offset #3
/*      */     //   Java source line #577	-> byte code offset #9
/*      */     //   Java source line #582	-> byte code offset #15
/*      */     //   Java source line #587	-> byte code offset #29
/*      */     //   Java source line #588	-> byte code offset #44
/*      */     //   Java source line #591	-> byte code offset #50
/*      */     //   Java source line #593	-> byte code offset #65
/*      */     //   Java source line #599	-> byte code offset #74
/*      */     //   Java source line #600	-> byte code offset #84
/*      */     //   Java source line #601	-> byte code offset #95
/*      */     //   Java source line #602	-> byte code offset #101
/*      */     //   Java source line #606	-> byte code offset #113
/*      */     //   Java source line #607	-> byte code offset #130
/*      */     //   Java source line #608	-> byte code offset #140
/*      */     //   Java source line #609	-> byte code offset #150
/*      */     //   Java source line #611	-> byte code offset #167
/*      */     //   Java source line #613	-> byte code offset #196
/*      */     //   Java source line #614	-> byte code offset #206
/*      */     //   Java source line #616	-> byte code offset #209
/*      */     //   Java source line #621	-> byte code offset #213
/*      */     //   Java source line #623	-> byte code offset #220
/*      */     //   Java source line #624	-> byte code offset #225
/*      */     //   Java source line #618	-> byte code offset #234
/*      */     //   Java source line #621	-> byte code offset #237
/*      */     //   Java source line #623	-> byte code offset #246
/*      */     //   Java source line #624	-> byte code offset #251
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	260	0	this	SslHandler
/*      */     //   0	260	1	alloc	ByteBufAllocator
/*      */     //   0	260	2	engine	SSLEngine
/*      */     //   0	260	3	in	ByteBuf
/*      */     //   0	260	4	out	ByteBuf
/*      */     //   1	251	5	newDirectIn	ByteBuf
/*      */     //   7	81	6	readerIndex	int
/*      */     //   13	95	7	readableBytes	int
/*      */     //   48	3	8	in0	ByteBuffer[]
/*      */     //   69	3	8	in0	ByteBuffer[]
/*      */     //   99	33	8	in0	ByteBuffer[]
/*      */     //   128	6	9	out0	ByteBuffer
/*      */     //   138	72	10	result	SSLEngineResult
/*      */     //   211	21	11	localSSLEngineResult1	SSLEngineResult
/*      */     //   237	21	12	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   3	213	237	finally
/*      */     //   234	239	237	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private SSLEngineResult unwrap(SSLEngine engine, ByteBuf in, int readerIndex, int len, ByteBuf out)
/*      */     throws SSLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_2
/*      */     //   1: invokevirtual 411	io/netty/buffer/ByteBuf:nioBufferCount	()I
/*      */     //   4: istore 6
/*      */     //   6: aload_1
/*      */     //   7: instanceof 129
/*      */     //   10: ifeq +236 -> 246
/*      */     //   13: iload 6
/*      */     //   15: iconst_1
/*      */     //   16: if_icmple +230 -> 246
/*      */     //   19: aload_1
/*      */     //   20: checkcast 129	io/netty/handler/ssl/OpenSslEngine
/*      */     //   23: astore 7
/*      */     //   25: iconst_0
/*      */     //   26: istore 8
/*      */     //   28: aload_2
/*      */     //   29: iload_3
/*      */     //   30: iload 4
/*      */     //   32: invokevirtual 720	io/netty/buffer/ByteBuf:nioBuffers	(II)[Ljava/nio/ByteBuffer;
/*      */     //   35: astore 9
/*      */     //   37: aload 5
/*      */     //   39: invokevirtual 431	io/netty/buffer/ByteBuf:writerIndex	()I
/*      */     //   42: istore 10
/*      */     //   44: aload 5
/*      */     //   46: invokevirtual 434	io/netty/buffer/ByteBuf:writableBytes	()I
/*      */     //   49: istore 11
/*      */     //   51: aload 5
/*      */     //   53: invokevirtual 411	io/netty/buffer/ByteBuf:nioBufferCount	()I
/*      */     //   56: iconst_1
/*      */     //   57: if_icmpne +17 -> 74
/*      */     //   60: aload 5
/*      */     //   62: iload 10
/*      */     //   64: iload 11
/*      */     //   66: invokevirtual 415	io/netty/buffer/ByteBuf:internalNioBuffer	(II)Ljava/nio/ByteBuffer;
/*      */     //   69: astore 12
/*      */     //   71: goto +14 -> 85
/*      */     //   74: aload 5
/*      */     //   76: iload 10
/*      */     //   78: iload 11
/*      */     //   80: invokevirtual 437	io/netty/buffer/ByteBuf:nioBuffer	(II)Ljava/nio/ByteBuffer;
/*      */     //   83: astore 12
/*      */     //   85: aload_0
/*      */     //   86: getfield 88	io/netty/handler/ssl/SslHandler:singleBuffer	[Ljava/nio/ByteBuffer;
/*      */     //   89: iconst_0
/*      */     //   90: aload 12
/*      */     //   92: aastore
/*      */     //   93: aload 7
/*      */     //   95: aload 9
/*      */     //   97: aload_0
/*      */     //   98: getfield 88	io/netty/handler/ssl/SslHandler:singleBuffer	[Ljava/nio/ByteBuffer;
/*      */     //   101: invokevirtual 723	io/netty/handler/ssl/OpenSslEngine:unwrap	([Ljava/nio/ByteBuffer;[Ljava/nio/ByteBuffer;)Ljavax/net/ssl/SSLEngineResult;
/*      */     //   104: astore 13
/*      */     //   106: aload 5
/*      */     //   108: aload 5
/*      */     //   110: invokevirtual 431	io/netty/buffer/ByteBuf:writerIndex	()I
/*      */     //   113: aload 13
/*      */     //   115: invokevirtual 397	javax/net/ssl/SSLEngineResult:bytesProduced	()I
/*      */     //   118: iadd
/*      */     //   119: invokevirtual 448	io/netty/buffer/ByteBuf:writerIndex	(I)Lio/netty/buffer/ByteBuf;
/*      */     //   122: pop
/*      */     //   123: getstatic 451	io/netty/handler/ssl/SslHandler$8:$SwitchMap$javax$net$ssl$SSLEngineResult$Status	[I
/*      */     //   126: aload 13
/*      */     //   128: invokevirtual 331	javax/net/ssl/SSLEngineResult:getStatus	()Ljavax/net/ssl/SSLEngineResult$Status;
/*      */     //   131: invokevirtual 452	javax/net/ssl/SSLEngineResult$Status:ordinal	()I
/*      */     //   134: iaload
/*      */     //   135: lookupswitch	default:+82->217, 1:+17->152
/*      */     //   152: aload_1
/*      */     //   153: invokevirtual 119	javax/net/ssl/SSLEngine:getSession	()Ljavax/net/ssl/SSLSession;
/*      */     //   156: invokeinterface 726 1 0
/*      */     //   161: istore 14
/*      */     //   163: iload 8
/*      */     //   165: iinc 8 1
/*      */     //   168: lookupswitch	default:+38->206, 0:+20->188
/*      */     //   188: aload 5
/*      */     //   190: iload 14
/*      */     //   192: aload_2
/*      */     //   193: invokevirtual 314	io/netty/buffer/ByteBuf:readableBytes	()I
/*      */     //   196: invokestatic 732	java/lang/Math:min	(II)I
/*      */     //   199: invokevirtual 455	io/netty/buffer/ByteBuf:ensureWritable	(I)Lio/netty/buffer/ByteBuf;
/*      */     //   202: pop
/*      */     //   203: goto +28 -> 231
/*      */     //   206: aload 5
/*      */     //   208: iload 14
/*      */     //   210: invokevirtual 455	io/netty/buffer/ByteBuf:ensureWritable	(I)Lio/netty/buffer/ByteBuf;
/*      */     //   213: pop
/*      */     //   214: goto +17 -> 231
/*      */     //   217: aload 13
/*      */     //   219: astore 15
/*      */     //   221: aload_0
/*      */     //   222: getfield 88	io/netty/handler/ssl/SslHandler:singleBuffer	[Ljava/nio/ByteBuffer;
/*      */     //   225: iconst_0
/*      */     //   226: aconst_null
/*      */     //   227: aastore
/*      */     //   228: aload 15
/*      */     //   230: areturn
/*      */     //   231: goto -194 -> 37
/*      */     //   234: astore 16
/*      */     //   236: aload_0
/*      */     //   237: getfield 88	io/netty/handler/ssl/SslHandler:singleBuffer	[Ljava/nio/ByteBuffer;
/*      */     //   240: iconst_0
/*      */     //   241: aconst_null
/*      */     //   242: aastore
/*      */     //   243: aload 16
/*      */     //   245: athrow
/*      */     //   246: iconst_0
/*      */     //   247: istore 7
/*      */     //   249: iload 6
/*      */     //   251: iconst_1
/*      */     //   252: if_icmpne +15 -> 267
/*      */     //   255: aload_2
/*      */     //   256: iload_3
/*      */     //   257: iload 4
/*      */     //   259: invokevirtual 415	io/netty/buffer/ByteBuf:internalNioBuffer	(II)Ljava/nio/ByteBuffer;
/*      */     //   262: astore 8
/*      */     //   264: goto +12 -> 276
/*      */     //   267: aload_2
/*      */     //   268: iload_3
/*      */     //   269: iload 4
/*      */     //   271: invokevirtual 437	io/netty/buffer/ByteBuf:nioBuffer	(II)Ljava/nio/ByteBuffer;
/*      */     //   274: astore 8
/*      */     //   276: aload 5
/*      */     //   278: invokevirtual 431	io/netty/buffer/ByteBuf:writerIndex	()I
/*      */     //   281: istore 9
/*      */     //   283: aload 5
/*      */     //   285: invokevirtual 434	io/netty/buffer/ByteBuf:writableBytes	()I
/*      */     //   288: istore 10
/*      */     //   290: aload 5
/*      */     //   292: invokevirtual 411	io/netty/buffer/ByteBuf:nioBufferCount	()I
/*      */     //   295: iconst_1
/*      */     //   296: if_icmpne +17 -> 313
/*      */     //   299: aload 5
/*      */     //   301: iload 9
/*      */     //   303: iload 10
/*      */     //   305: invokevirtual 415	io/netty/buffer/ByteBuf:internalNioBuffer	(II)Ljava/nio/ByteBuffer;
/*      */     //   308: astore 11
/*      */     //   310: goto +14 -> 324
/*      */     //   313: aload 5
/*      */     //   315: iload 9
/*      */     //   317: iload 10
/*      */     //   319: invokevirtual 437	io/netty/buffer/ByteBuf:nioBuffer	(II)Ljava/nio/ByteBuffer;
/*      */     //   322: astore 11
/*      */     //   324: aload_1
/*      */     //   325: aload 8
/*      */     //   327: aload 11
/*      */     //   329: invokevirtual 735	javax/net/ssl/SSLEngine:unwrap	(Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)Ljavax/net/ssl/SSLEngineResult;
/*      */     //   332: astore 12
/*      */     //   334: aload 5
/*      */     //   336: aload 5
/*      */     //   338: invokevirtual 431	io/netty/buffer/ByteBuf:writerIndex	()I
/*      */     //   341: aload 12
/*      */     //   343: invokevirtual 397	javax/net/ssl/SSLEngineResult:bytesProduced	()I
/*      */     //   346: iadd
/*      */     //   347: invokevirtual 448	io/netty/buffer/ByteBuf:writerIndex	(I)Lio/netty/buffer/ByteBuf;
/*      */     //   350: pop
/*      */     //   351: getstatic 451	io/netty/handler/ssl/SslHandler$8:$SwitchMap$javax$net$ssl$SSLEngineResult$Status	[I
/*      */     //   354: aload 12
/*      */     //   356: invokevirtual 331	javax/net/ssl/SSLEngineResult:getStatus	()Ljavax/net/ssl/SSLEngineResult$Status;
/*      */     //   359: invokevirtual 452	javax/net/ssl/SSLEngineResult$Status:ordinal	()I
/*      */     //   362: iaload
/*      */     //   363: lookupswitch	default:+82->445, 1:+17->380
/*      */     //   380: aload_1
/*      */     //   381: invokevirtual 119	javax/net/ssl/SSLEngine:getSession	()Ljavax/net/ssl/SSLSession;
/*      */     //   384: invokeinterface 726 1 0
/*      */     //   389: istore 13
/*      */     //   391: iload 7
/*      */     //   393: iinc 7 1
/*      */     //   396: lookupswitch	default:+38->434, 0:+20->416
/*      */     //   416: aload 5
/*      */     //   418: iload 13
/*      */     //   420: aload_2
/*      */     //   421: invokevirtual 314	io/netty/buffer/ByteBuf:readableBytes	()I
/*      */     //   424: invokestatic 732	java/lang/Math:min	(II)I
/*      */     //   427: invokevirtual 455	io/netty/buffer/ByteBuf:ensureWritable	(I)Lio/netty/buffer/ByteBuf;
/*      */     //   430: pop
/*      */     //   431: goto +17 -> 448
/*      */     //   434: aload 5
/*      */     //   436: iload 13
/*      */     //   438: invokevirtual 455	io/netty/buffer/ByteBuf:ensureWritable	(I)Lio/netty/buffer/ByteBuf;
/*      */     //   441: pop
/*      */     //   442: goto +6 -> 448
/*      */     //   445: aload 12
/*      */     //   447: areturn
/*      */     //   448: goto -172 -> 276
/*      */     // Line number table:
/*      */     //   Java source line #1010	-> byte code offset #0
/*      */     //   Java source line #1011	-> byte code offset #6
/*      */     //   Java source line #1017	-> byte code offset #19
/*      */     //   Java source line #1018	-> byte code offset #25
/*      */     //   Java source line #1019	-> byte code offset #28
/*      */     //   Java source line #1022	-> byte code offset #37
/*      */     //   Java source line #1023	-> byte code offset #44
/*      */     //   Java source line #1025	-> byte code offset #51
/*      */     //   Java source line #1026	-> byte code offset #60
/*      */     //   Java source line #1028	-> byte code offset #74
/*      */     //   Java source line #1030	-> byte code offset #85
/*      */     //   Java source line #1031	-> byte code offset #93
/*      */     //   Java source line #1032	-> byte code offset #106
/*      */     //   Java source line #1033	-> byte code offset #123
/*      */     //   Java source line #1035	-> byte code offset #152
/*      */     //   Java source line #1036	-> byte code offset #163
/*      */     //   Java source line #1038	-> byte code offset #188
/*      */     //   Java source line #1039	-> byte code offset #203
/*      */     //   Java source line #1041	-> byte code offset #206
/*      */     //   Java source line #1043	-> byte code offset #214
/*      */     //   Java source line #1045	-> byte code offset #217
/*      */     //   Java source line #1049	-> byte code offset #221
/*      */     //   Java source line #1047	-> byte code offset #231
/*      */     //   Java source line #1049	-> byte code offset #234
/*      */     //   Java source line #1052	-> byte code offset #246
/*      */     //   Java source line #1054	-> byte code offset #249
/*      */     //   Java source line #1056	-> byte code offset #255
/*      */     //   Java source line #1060	-> byte code offset #267
/*      */     //   Java source line #1063	-> byte code offset #276
/*      */     //   Java source line #1064	-> byte code offset #283
/*      */     //   Java source line #1066	-> byte code offset #290
/*      */     //   Java source line #1067	-> byte code offset #299
/*      */     //   Java source line #1069	-> byte code offset #313
/*      */     //   Java source line #1071	-> byte code offset #324
/*      */     //   Java source line #1072	-> byte code offset #334
/*      */     //   Java source line #1073	-> byte code offset #351
/*      */     //   Java source line #1075	-> byte code offset #380
/*      */     //   Java source line #1076	-> byte code offset #391
/*      */     //   Java source line #1078	-> byte code offset #416
/*      */     //   Java source line #1079	-> byte code offset #431
/*      */     //   Java source line #1081	-> byte code offset #434
/*      */     //   Java source line #1083	-> byte code offset #442
/*      */     //   Java source line #1085	-> byte code offset #445
/*      */     //   Java source line #1087	-> byte code offset #448
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	451	0	this	SslHandler
/*      */     //   0	451	1	engine	SSLEngine
/*      */     //   0	451	2	in	ByteBuf
/*      */     //   0	451	3	readerIndex	int
/*      */     //   0	451	4	len	int
/*      */     //   0	451	5	out	ByteBuf
/*      */     //   4	246	6	nioBufferCount	int
/*      */     //   23	71	7	opensslEngine	OpenSslEngine
/*      */     //   247	145	7	overflows	int
/*      */     //   26	138	8	overflows	int
/*      */     //   262	3	8	in0	ByteBuffer
/*      */     //   274	52	8	in0	ByteBuffer
/*      */     //   35	61	9	in0	ByteBuffer[]
/*      */     //   281	35	9	writerIndex	int
/*      */     //   42	35	10	writerIndex	int
/*      */     //   288	30	10	writableBytes	int
/*      */     //   49	30	11	writableBytes	int
/*      */     //   308	3	11	out0	ByteBuffer
/*      */     //   322	6	11	out0	ByteBuffer
/*      */     //   69	3	12	out0	ByteBuffer
/*      */     //   83	8	12	out0	ByteBuffer
/*      */     //   332	114	12	result	SSLEngineResult
/*      */     //   104	114	13	result	SSLEngineResult
/*      */     //   389	48	13	max	int
/*      */     //   161	48	14	max	int
/*      */     //   219	10	15	localSSLEngineResult1	SSLEngineResult
/*      */     //   234	10	16	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   37	221	234	finally
/*      */     //   231	236	234	finally
/*      */   }
/*      */   
/*      */   private final class LazyChannelPromise
/*      */     extends DefaultPromise<Channel>
/*      */   {
/*      */     private LazyChannelPromise() {}
/*      */     
/*      */     protected EventExecutor executor()
/*      */     {
/* 1398 */       if (SslHandler.this.ctx == null) {
/* 1399 */         throw new IllegalStateException();
/*      */       }
/* 1401 */       return SslHandler.this.ctx.executor();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\SslHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */