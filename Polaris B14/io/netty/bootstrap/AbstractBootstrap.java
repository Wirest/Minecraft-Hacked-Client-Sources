/*     */ package io.netty.bootstrap;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.Channel.Unsafe;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.DefaultChannelPromise;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.ReflectiveChannelFactory;
/*     */ import io.netty.util.AttributeKey;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.GlobalEventExecutor;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.LinkedHashMap;
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
/*     */ public abstract class AbstractBootstrap<B extends AbstractBootstrap<B, C>, C extends Channel>
/*     */   implements Cloneable
/*     */ {
/*     */   private volatile EventLoopGroup group;
/*     */   private volatile ChannelFactory<? extends C> channelFactory;
/*     */   private volatile SocketAddress localAddress;
/*  53 */   private final Map<ChannelOption<?>, Object> options = new LinkedHashMap();
/*  54 */   private final Map<AttributeKey<?>, Object> attrs = new LinkedHashMap();
/*     */   
/*     */   private volatile ChannelHandler handler;
/*     */   
/*     */   AbstractBootstrap() {}
/*     */   
/*     */   AbstractBootstrap(AbstractBootstrap<B, C> bootstrap)
/*     */   {
/*  62 */     this.group = bootstrap.group;
/*  63 */     this.channelFactory = bootstrap.channelFactory;
/*  64 */     this.handler = bootstrap.handler;
/*  65 */     this.localAddress = bootstrap.localAddress;
/*  66 */     synchronized (bootstrap.options) {
/*  67 */       this.options.putAll(bootstrap.options);
/*     */     }
/*  69 */     synchronized (bootstrap.attrs) {
/*  70 */       this.attrs.putAll(bootstrap.attrs);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public B group(EventLoopGroup group)
/*     */   {
/*  80 */     if (group == null) {
/*  81 */       throw new NullPointerException("group");
/*     */     }
/*  83 */     if (this.group != null) {
/*  84 */       throw new IllegalStateException("group set already");
/*     */     }
/*  86 */     this.group = group;
/*  87 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public B channel(Class<? extends C> channelClass)
/*     */   {
/*  96 */     if (channelClass == null) {
/*  97 */       throw new NullPointerException("channelClass");
/*     */     }
/*  99 */     return channelFactory(new ReflectiveChannelFactory(channelClass));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public B channelFactory(ChannelFactory<? extends C> channelFactory)
/*     */   {
/* 108 */     if (channelFactory == null) {
/* 109 */       throw new NullPointerException("channelFactory");
/*     */     }
/* 111 */     if (this.channelFactory != null) {
/* 112 */       throw new IllegalStateException("channelFactory set already");
/*     */     }
/*     */     
/* 115 */     this.channelFactory = channelFactory;
/* 116 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public B channelFactory(io.netty.channel.ChannelFactory<? extends C> channelFactory)
/*     */   {
/* 128 */     return channelFactory(channelFactory);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public B localAddress(SocketAddress localAddress)
/*     */   {
/* 136 */     this.localAddress = localAddress;
/* 137 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public B localAddress(int inetPort)
/*     */   {
/* 144 */     return localAddress(new InetSocketAddress(inetPort));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public B localAddress(String inetHost, int inetPort)
/*     */   {
/* 151 */     return localAddress(new InetSocketAddress(inetHost, inetPort));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public B localAddress(InetAddress inetHost, int inetPort)
/*     */   {
/* 158 */     return localAddress(new InetSocketAddress(inetHost, inetPort));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T> B option(ChannelOption<T> option, T value)
/*     */   {
/* 167 */     if (option == null) {
/* 168 */       throw new NullPointerException("option");
/*     */     }
/* 170 */     if (value == null) {
/* 171 */       synchronized (this.options) {
/* 172 */         this.options.remove(option);
/*     */       }
/*     */     } else {
/* 175 */       synchronized (this.options) {
/* 176 */         this.options.put(option, value);
/*     */       }
/*     */     }
/* 179 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T> B attr(AttributeKey<T> key, T value)
/*     */   {
/* 188 */     if (key == null) {
/* 189 */       throw new NullPointerException("key");
/*     */     }
/* 191 */     if (value == null) {
/* 192 */       synchronized (this.attrs) {
/* 193 */         this.attrs.remove(key);
/*     */       }
/*     */     } else {
/* 196 */       synchronized (this.attrs) {
/* 197 */         this.attrs.put(key, value);
/*     */       }
/*     */     }
/* 200 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public B validate()
/*     */   {
/* 209 */     if (this.group == null) {
/* 210 */       throw new IllegalStateException("group not set");
/*     */     }
/* 212 */     if (this.channelFactory == null) {
/* 213 */       throw new IllegalStateException("channel or channelFactory not set");
/*     */     }
/* 215 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract B clone();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelFuture register()
/*     */   {
/* 231 */     validate();
/* 232 */     return initAndRegister();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture bind()
/*     */   {
/* 239 */     validate();
/* 240 */     SocketAddress localAddress = this.localAddress;
/* 241 */     if (localAddress == null) {
/* 242 */       throw new IllegalStateException("localAddress not set");
/*     */     }
/* 244 */     return doBind(localAddress);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture bind(int inetPort)
/*     */   {
/* 251 */     return bind(new InetSocketAddress(inetPort));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture bind(String inetHost, int inetPort)
/*     */   {
/* 258 */     return bind(new InetSocketAddress(inetHost, inetPort));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture bind(InetAddress inetHost, int inetPort)
/*     */   {
/* 265 */     return bind(new InetSocketAddress(inetHost, inetPort));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture bind(SocketAddress localAddress)
/*     */   {
/* 272 */     validate();
/* 273 */     if (localAddress == null) {
/* 274 */       throw new NullPointerException("localAddress");
/*     */     }
/* 276 */     return doBind(localAddress);
/*     */   }
/*     */   
/*     */   private ChannelFuture doBind(final SocketAddress localAddress) {
/* 280 */     final ChannelFuture regFuture = initAndRegister();
/* 281 */     final Channel channel = regFuture.channel();
/* 282 */     if (regFuture.cause() != null) {
/* 283 */       return regFuture;
/*     */     }
/*     */     
/* 286 */     if (regFuture.isDone())
/*     */     {
/* 288 */       ChannelPromise promise = channel.newPromise();
/* 289 */       doBind0(regFuture, channel, localAddress, promise);
/* 290 */       return promise;
/*     */     }
/*     */     
/* 293 */     final PendingRegistrationPromise promise = new PendingRegistrationPromise(channel, null);
/* 294 */     regFuture.addListener(new ChannelFutureListener()
/*     */     {
/*     */       public void operationComplete(ChannelFuture future) throws Exception {
/* 297 */         Throwable cause = future.cause();
/* 298 */         if (cause != null)
/*     */         {
/*     */ 
/* 301 */           promise.setFailure(cause);
/*     */         }
/*     */         else
/*     */         {
/* 305 */           promise.executor = channel.eventLoop();
/*     */         }
/* 307 */         AbstractBootstrap.doBind0(regFuture, channel, localAddress, promise);
/*     */       }
/* 309 */     });
/* 310 */     return promise;
/*     */   }
/*     */   
/*     */   final ChannelFuture initAndRegister()
/*     */   {
/* 315 */     Channel channel = channelFactory().newChannel();
/*     */     try {
/* 317 */       init(channel);
/*     */     } catch (Throwable t) {
/* 319 */       channel.unsafe().closeForcibly();
/*     */       
/* 321 */       return new DefaultChannelPromise(channel, GlobalEventExecutor.INSTANCE).setFailure(t);
/*     */     }
/*     */     
/* 324 */     ChannelFuture regFuture = group().register(channel);
/* 325 */     if (regFuture.cause() != null) {
/* 326 */       if (channel.isRegistered()) {
/* 327 */         channel.close();
/*     */       } else {
/* 329 */         channel.unsafe().closeForcibly();
/*     */       }
/*     */     }
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
/* 342 */     return regFuture;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   abstract void init(Channel paramChannel)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */   private static void doBind0(ChannelFuture regFuture, final Channel channel, final SocketAddress localAddress, final ChannelPromise promise)
/*     */   {
/* 353 */     channel.eventLoop().execute(new Runnable()
/*     */     {
/*     */       public void run() {
/* 356 */         if (this.val$regFuture.isSuccess()) {
/* 357 */           channel.bind(localAddress, promise).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
/*     */         } else {
/* 359 */           promise.setFailure(this.val$regFuture.cause());
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public B handler(ChannelHandler handler)
/*     */   {
/* 370 */     if (handler == null) {
/* 371 */       throw new NullPointerException("handler");
/*     */     }
/* 373 */     this.handler = handler;
/* 374 */     return this;
/*     */   }
/*     */   
/*     */   final SocketAddress localAddress() {
/* 378 */     return this.localAddress;
/*     */   }
/*     */   
/*     */   final ChannelFactory<? extends C> channelFactory()
/*     */   {
/* 383 */     return this.channelFactory;
/*     */   }
/*     */   
/*     */   final ChannelHandler handler() {
/* 387 */     return this.handler;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public EventLoopGroup group()
/*     */   {
/* 394 */     return this.group;
/*     */   }
/*     */   
/*     */   final Map<ChannelOption<?>, Object> options() {
/* 398 */     return this.options;
/*     */   }
/*     */   
/*     */   final Map<AttributeKey<?>, Object> attrs() {
/* 402 */     return this.attrs;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 407 */     StringBuilder buf = new StringBuilder().append(StringUtil.simpleClassName(this)).append('(');
/*     */     
/*     */ 
/* 410 */     if (this.group != null) {
/* 411 */       buf.append("group: ").append(StringUtil.simpleClassName(this.group)).append(", ");
/*     */     }
/*     */     
/*     */ 
/* 415 */     if (this.channelFactory != null) {
/* 416 */       buf.append("channelFactory: ").append(this.channelFactory).append(", ");
/*     */     }
/*     */     
/*     */ 
/* 420 */     if (this.localAddress != null) {
/* 421 */       buf.append("localAddress: ").append(this.localAddress).append(", ");
/*     */     }
/*     */     
/*     */ 
/* 425 */     synchronized (this.options) {
/* 426 */       if (!this.options.isEmpty()) {
/* 427 */         buf.append("options: ").append(this.options).append(", ");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 432 */     synchronized (this.attrs) {
/* 433 */       if (!this.attrs.isEmpty()) {
/* 434 */         buf.append("attrs: ").append(this.attrs).append(", ");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 439 */     if (this.handler != null) {
/* 440 */       buf.append("handler: ").append(this.handler).append(", ");
/*     */     }
/*     */     
/*     */ 
/* 444 */     if (buf.charAt(buf.length() - 1) == '(') {
/* 445 */       buf.append(')');
/*     */     } else {
/* 447 */       buf.setCharAt(buf.length() - 2, ')');
/* 448 */       buf.setLength(buf.length() - 1);
/*     */     }
/* 450 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private static final class PendingRegistrationPromise extends DefaultChannelPromise
/*     */   {
/*     */     private volatile EventExecutor executor;
/*     */     
/*     */     private PendingRegistrationPromise(Channel channel)
/*     */     {
/* 459 */       super();
/*     */     }
/*     */     
/*     */     protected EventExecutor executor()
/*     */     {
/* 464 */       EventExecutor executor = this.executor;
/* 465 */       if (executor != null)
/*     */       {
/*     */ 
/*     */ 
/* 469 */         return executor;
/*     */       }
/*     */       
/* 472 */       return GlobalEventExecutor.INSTANCE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\bootstrap\AbstractBootstrap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */