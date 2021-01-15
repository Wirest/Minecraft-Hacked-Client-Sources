/*     */ package io.netty.channel.embedded;
/*     */ 
/*     */ import io.netty.channel.AbstractChannel;
/*     */ import io.netty.channel.AbstractChannel.AbstractUnsafe;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.DefaultChannelConfig;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.RecyclableArrayList;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Queue;
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
/*     */ public class EmbeddedChannel
/*     */   extends AbstractChannel
/*     */ {
/*  47 */   private static final SocketAddress LOCAL_ADDRESS = new EmbeddedSocketAddress();
/*  48 */   private static final SocketAddress REMOTE_ADDRESS = new EmbeddedSocketAddress();
/*     */   
/*  50 */   private static final ChannelHandler[] EMPTY_HANDLERS = new ChannelHandler[0];
/*  51 */   private static enum State { OPEN,  ACTIVE,  CLOSED;
/*     */     private State() {} }
/*  53 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(EmbeddedChannel.class);
/*     */   
/*  55 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*     */   
/*  57 */   private final EmbeddedEventLoop loop = new EmbeddedEventLoop();
/*  58 */   private final ChannelConfig config = new DefaultChannelConfig(this);
/*  59 */   private final Queue<Object> inboundMessages = new ArrayDeque();
/*  60 */   private final Queue<Object> outboundMessages = new ArrayDeque();
/*     */   
/*     */   private Throwable lastException;
/*     */   
/*     */   private State state;
/*     */   
/*     */   public EmbeddedChannel()
/*     */   {
/*  68 */     this(EMPTY_HANDLERS);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EmbeddedChannel(ChannelHandler... handlers)
/*     */   {
/*  77 */     super(null, EmbeddedChannelId.INSTANCE);
/*     */     
/*  79 */     if (handlers == null) {
/*  80 */       throw new NullPointerException("handlers");
/*     */     }
/*     */     
/*  83 */     ChannelPipeline p = pipeline();
/*  84 */     for (ChannelHandler h : handlers) {
/*  85 */       if (h == null) {
/*     */         break;
/*     */       }
/*  88 */       p.addLast(new ChannelHandler[] { h });
/*     */     }
/*     */     
/*  91 */     this.loop.register(this);
/*  92 */     p.addLast(new ChannelHandler[] { new LastInboundHandler(null) });
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/*  97 */     return METADATA;
/*     */   }
/*     */   
/*     */   public ChannelConfig config()
/*     */   {
/* 102 */     return this.config;
/*     */   }
/*     */   
/*     */   public boolean isOpen()
/*     */   {
/* 107 */     return this.state != State.CLOSED;
/*     */   }
/*     */   
/*     */   public boolean isActive()
/*     */   {
/* 112 */     return this.state == State.ACTIVE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Queue<Object> inboundMessages()
/*     */   {
/* 119 */     return this.inboundMessages;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Queue<Object> lastInboundBuffer()
/*     */   {
/* 127 */     return inboundMessages();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Queue<Object> outboundMessages()
/*     */   {
/* 134 */     return this.outboundMessages;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Queue<Object> lastOutboundBuffer()
/*     */   {
/* 142 */     return outboundMessages();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T> T readInbound()
/*     */   {
/* 150 */     return (T)this.inboundMessages.poll();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T> T readOutbound()
/*     */   {
/* 158 */     return (T)this.outboundMessages.poll();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean writeInbound(Object... msgs)
/*     */   {
/* 169 */     ensureOpen();
/* 170 */     if (msgs.length == 0) {
/* 171 */       return !this.inboundMessages.isEmpty();
/*     */     }
/*     */     
/* 174 */     ChannelPipeline p = pipeline();
/* 175 */     for (Object m : msgs) {
/* 176 */       p.fireChannelRead(m);
/*     */     }
/* 178 */     p.fireChannelReadComplete();
/* 179 */     runPendingTasks();
/* 180 */     checkException();
/* 181 */     return !this.inboundMessages.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean writeOutbound(Object... msgs)
/*     */   {
/* 191 */     ensureOpen();
/* 192 */     if (msgs.length == 0) {
/* 193 */       return !this.outboundMessages.isEmpty();
/*     */     }
/*     */     
/* 196 */     RecyclableArrayList futures = RecyclableArrayList.newInstance(msgs.length);
/*     */     try {
/* 198 */       for (Object m : msgs) {
/* 199 */         if (m == null) {
/*     */           break;
/*     */         }
/* 202 */         futures.add(write(m));
/*     */       }
/*     */       
/* 205 */       flush();
/*     */       
/* 207 */       int size = futures.size();
/* 208 */       for (int i = 0; i < size; i++) {
/* 209 */         ChannelFuture future = (ChannelFuture)futures.get(i);
/* 210 */         assert (future.isDone());
/* 211 */         if (future.cause() != null) {
/* 212 */           recordException(future.cause());
/*     */         }
/*     */       }
/*     */       
/* 216 */       runPendingTasks();
/* 217 */       checkException();
/* 218 */       return !this.outboundMessages.isEmpty() ? 1 : 0;
/*     */     } finally {
/* 220 */       futures.recycle();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean finish()
/*     */   {
/* 230 */     close();
/* 231 */     runPendingTasks();
/*     */     
/*     */ 
/* 234 */     this.loop.cancelScheduledTasks();
/*     */     
/* 236 */     checkException();
/*     */     
/* 238 */     return (!this.inboundMessages.isEmpty()) || (!this.outboundMessages.isEmpty());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void runPendingTasks()
/*     */   {
/*     */     try
/*     */     {
/* 247 */       this.loop.runTasks();
/*     */     } catch (Exception e) {
/* 249 */       recordException(e);
/*     */     }
/*     */     try
/*     */     {
/* 253 */       this.loop.runScheduledTasks();
/*     */     } catch (Exception e) {
/* 255 */       recordException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long runScheduledPendingTasks()
/*     */   {
/*     */     try
/*     */     {
/* 266 */       return this.loop.runScheduledTasks();
/*     */     } catch (Exception e) {
/* 268 */       recordException(e); }
/* 269 */     return this.loop.nextScheduledTask();
/*     */   }
/*     */   
/*     */   private void recordException(Throwable cause)
/*     */   {
/* 274 */     if (this.lastException == null) {
/* 275 */       this.lastException = cause;
/*     */     } else {
/* 277 */       logger.warn("More than one exception was raised. Will report only the first one and log others.", cause);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void checkException()
/*     */   {
/* 287 */     Throwable t = this.lastException;
/* 288 */     if (t == null) {
/* 289 */       return;
/*     */     }
/*     */     
/* 292 */     this.lastException = null;
/*     */     
/* 294 */     PlatformDependent.throwException(t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final void ensureOpen()
/*     */   {
/* 301 */     if (!isOpen()) {
/* 302 */       recordException(new ClosedChannelException());
/* 303 */       checkException();
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean isCompatible(EventLoop loop)
/*     */   {
/* 309 */     return loop instanceof EmbeddedEventLoop;
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress0()
/*     */   {
/* 314 */     return isActive() ? LOCAL_ADDRESS : null;
/*     */   }
/*     */   
/*     */   protected SocketAddress remoteAddress0()
/*     */   {
/* 319 */     return isActive() ? REMOTE_ADDRESS : null;
/*     */   }
/*     */   
/*     */   protected void doRegister() throws Exception
/*     */   {
/* 324 */     this.state = State.ACTIVE;
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress)
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */   protected void doDisconnect()
/*     */     throws Exception
/*     */   {
/* 334 */     doClose();
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception
/*     */   {
/* 339 */     this.state = State.CLOSED;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void doBeginRead()
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */   protected AbstractChannel.AbstractUnsafe newUnsafe()
/*     */   {
/* 349 */     return new DefaultUnsafe(null);
/*     */   }
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception
/*     */   {
/*     */     for (;;) {
/* 355 */       Object msg = in.current();
/* 356 */       if (msg == null) {
/*     */         break;
/*     */       }
/*     */       
/* 360 */       ReferenceCountUtil.retain(msg);
/* 361 */       this.outboundMessages.add(msg);
/* 362 */       in.remove();
/*     */     }
/*     */   }
/*     */   
/* 366 */   private class DefaultUnsafe extends AbstractChannel.AbstractUnsafe { private DefaultUnsafe() { super(); }
/*     */     
/*     */     public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
/* 369 */       safeSetSuccess(promise);
/*     */     }
/*     */   }
/*     */   
/*     */   private final class LastInboundHandler extends ChannelHandlerAdapter {
/*     */     private LastInboundHandler() {}
/*     */     
/* 376 */     public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception { EmbeddedChannel.this.inboundMessages.add(msg); }
/*     */     
/*     */     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
/*     */       throws Exception
/*     */     {
/* 381 */       EmbeddedChannel.this.recordException(cause);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\embedded\EmbeddedChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */