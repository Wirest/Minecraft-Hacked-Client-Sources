/*     */ package io.netty.bootstrap;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.resolver.DefaultNameResolverGroup;
/*     */ import io.netty.resolver.NameResolver;
/*     */ import io.netty.resolver.NameResolverGroup;
/*     */ import io.netty.util.Attribute;
/*     */ import io.netty.util.AttributeKey;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.FutureListener;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ public class Bootstrap
/*     */   extends AbstractBootstrap<Bootstrap, Channel>
/*     */ {
/*  49 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(Bootstrap.class);
/*     */   
/*  51 */   private static final NameResolverGroup<?> DEFAULT_RESOLVER = DefaultNameResolverGroup.INSTANCE;
/*     */   
/*  53 */   private volatile NameResolverGroup<SocketAddress> resolver = DEFAULT_RESOLVER;
/*     */   private volatile SocketAddress remoteAddress;
/*     */   
/*     */   public Bootstrap() {}
/*     */   
/*     */   private Bootstrap(Bootstrap bootstrap)
/*     */   {
/*  60 */     super(bootstrap);
/*  61 */     this.resolver = bootstrap.resolver;
/*  62 */     this.remoteAddress = bootstrap.remoteAddress;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Bootstrap resolver(NameResolverGroup<?> resolver)
/*     */   {
/*  70 */     if (resolver == null) {
/*  71 */       throw new NullPointerException("resolver");
/*     */     }
/*  73 */     this.resolver = resolver;
/*  74 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Bootstrap remoteAddress(SocketAddress remoteAddress)
/*     */   {
/*  82 */     this.remoteAddress = remoteAddress;
/*  83 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Bootstrap remoteAddress(String inetHost, int inetPort)
/*     */   {
/*  90 */     this.remoteAddress = InetSocketAddress.createUnresolved(inetHost, inetPort);
/*  91 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Bootstrap remoteAddress(InetAddress inetHost, int inetPort)
/*     */   {
/*  98 */     this.remoteAddress = new InetSocketAddress(inetHost, inetPort);
/*  99 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture connect()
/*     */   {
/* 106 */     validate();
/* 107 */     SocketAddress remoteAddress = this.remoteAddress;
/* 108 */     if (remoteAddress == null) {
/* 109 */       throw new IllegalStateException("remoteAddress not set");
/*     */     }
/*     */     
/* 112 */     return doResolveAndConnect(remoteAddress, localAddress());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture connect(String inetHost, int inetPort)
/*     */   {
/* 119 */     return connect(InetSocketAddress.createUnresolved(inetHost, inetPort));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture connect(InetAddress inetHost, int inetPort)
/*     */   {
/* 126 */     return connect(new InetSocketAddress(inetHost, inetPort));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture connect(SocketAddress remoteAddress)
/*     */   {
/* 133 */     if (remoteAddress == null) {
/* 134 */       throw new NullPointerException("remoteAddress");
/*     */     }
/*     */     
/* 137 */     validate();
/* 138 */     return doResolveAndConnect(remoteAddress, localAddress());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress)
/*     */   {
/* 145 */     if (remoteAddress == null) {
/* 146 */       throw new NullPointerException("remoteAddress");
/*     */     }
/* 148 */     validate();
/* 149 */     return doResolveAndConnect(remoteAddress, localAddress);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private ChannelFuture doResolveAndConnect(SocketAddress remoteAddress, final SocketAddress localAddress)
/*     */   {
/* 156 */     final ChannelFuture regFuture = initAndRegister();
/* 157 */     if (regFuture.cause() != null) {
/* 158 */       return regFuture;
/*     */     }
/*     */     
/* 161 */     final Channel channel = regFuture.channel();
/* 162 */     EventLoop eventLoop = channel.eventLoop();
/* 163 */     NameResolver<SocketAddress> resolver = this.resolver.getResolver(eventLoop);
/*     */     
/* 165 */     if ((!resolver.isSupported(remoteAddress)) || (resolver.isResolved(remoteAddress)))
/*     */     {
/* 167 */       return doConnect(remoteAddress, localAddress, regFuture, channel.newPromise());
/*     */     }
/*     */     
/* 170 */     Future<SocketAddress> resolveFuture = resolver.resolve(remoteAddress);
/* 171 */     Throwable resolveFailureCause = resolveFuture.cause();
/*     */     
/* 173 */     if (resolveFailureCause != null)
/*     */     {
/* 175 */       channel.close();
/* 176 */       return channel.newFailedFuture(resolveFailureCause);
/*     */     }
/*     */     
/* 179 */     if (resolveFuture.isDone())
/*     */     {
/* 181 */       return doConnect((SocketAddress)resolveFuture.getNow(), localAddress, regFuture, channel.newPromise());
/*     */     }
/*     */     
/*     */ 
/* 185 */     final ChannelPromise connectPromise = channel.newPromise();
/* 186 */     resolveFuture.addListener(new FutureListener()
/*     */     {
/*     */       public void operationComplete(Future<SocketAddress> future) throws Exception {
/* 189 */         if (future.cause() != null) {
/* 190 */           channel.close();
/* 191 */           connectPromise.setFailure(future.cause());
/*     */         } else {
/* 193 */           Bootstrap.doConnect((SocketAddress)future.getNow(), localAddress, regFuture, connectPromise);
/*     */         }
/*     */         
/*     */       }
/* 197 */     });
/* 198 */     return connectPromise;
/*     */   }
/*     */   
/*     */ 
/*     */   private static ChannelFuture doConnect(SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelFuture regFuture, final ChannelPromise connectPromise)
/*     */   {
/* 204 */     if (regFuture.isDone()) {
/* 205 */       doConnect0(remoteAddress, localAddress, regFuture, connectPromise);
/*     */     } else {
/* 207 */       regFuture.addListener(new ChannelFutureListener()
/*     */       {
/*     */         public void operationComplete(ChannelFuture future) throws Exception {
/* 210 */           Bootstrap.doConnect0(this.val$remoteAddress, localAddress, regFuture, connectPromise);
/*     */         }
/*     */       });
/*     */     }
/*     */     
/* 215 */     return connectPromise;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void doConnect0(final SocketAddress remoteAddress, final SocketAddress localAddress, ChannelFuture regFuture, final ChannelPromise connectPromise)
/*     */   {
/* 224 */     final Channel channel = connectPromise.channel();
/* 225 */     channel.eventLoop().execute(new Runnable()
/*     */     {
/*     */       public void run() {
/* 228 */         if (this.val$regFuture.isSuccess()) {
/* 229 */           if (localAddress == null) {
/* 230 */             channel.connect(remoteAddress, connectPromise);
/*     */           } else {
/* 232 */             channel.connect(remoteAddress, localAddress, connectPromise);
/*     */           }
/* 234 */           connectPromise.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
/*     */         } else {
/* 236 */           connectPromise.setFailure(this.val$regFuture.cause());
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   void init(Channel channel)
/*     */     throws Exception
/*     */   {
/* 245 */     ChannelPipeline p = channel.pipeline();
/* 246 */     p.addLast(new ChannelHandler[] { handler() });
/*     */     
/* 248 */     Map<ChannelOption<?>, Object> options = options();
/* 249 */     synchronized (options) {
/* 250 */       for (Map.Entry<ChannelOption<?>, Object> e : options.entrySet()) {
/*     */         try {
/* 252 */           if (!channel.config().setOption((ChannelOption)e.getKey(), e.getValue())) {
/* 253 */             logger.warn("Unknown channel option: " + e);
/*     */           }
/*     */         } catch (Throwable t) {
/* 256 */           logger.warn("Failed to set a channel option: " + channel, t);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 261 */     Map<AttributeKey<?>, Object> attrs = attrs();
/* 262 */     synchronized (attrs) {
/* 263 */       for (Map.Entry<AttributeKey<?>, Object> e : attrs.entrySet()) {
/* 264 */         channel.attr((AttributeKey)e.getKey()).set(e.getValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Bootstrap validate()
/*     */   {
/* 271 */     super.validate();
/* 272 */     if (handler() == null) {
/* 273 */       throw new IllegalStateException("handler not set");
/*     */     }
/* 275 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public Bootstrap clone()
/*     */   {
/* 281 */     return new Bootstrap(this);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 286 */     if (this.remoteAddress == null) {
/* 287 */       return super.toString();
/*     */     }
/*     */     
/* 290 */     StringBuilder buf = new StringBuilder(super.toString());
/* 291 */     buf.setLength(buf.length() - 1);
/*     */     
/* 293 */     return ", remoteAddress: " + this.remoteAddress + ')';
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\bootstrap\Bootstrap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */