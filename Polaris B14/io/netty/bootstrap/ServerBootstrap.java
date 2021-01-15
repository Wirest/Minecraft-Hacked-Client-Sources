/*     */ package io.netty.bootstrap;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.Channel.Unsafe;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.ServerChannel;
/*     */ import io.netty.util.Attribute;
/*     */ import io.netty.util.AttributeKey;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerBootstrap
/*     */   extends AbstractBootstrap<ServerBootstrap, ServerChannel>
/*     */ {
/*  47 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ServerBootstrap.class);
/*     */   
/*  49 */   private final Map<ChannelOption<?>, Object> childOptions = new LinkedHashMap();
/*  50 */   private final Map<AttributeKey<?>, Object> childAttrs = new LinkedHashMap();
/*     */   private volatile EventLoopGroup childGroup;
/*     */   private volatile ChannelHandler childHandler;
/*     */   
/*     */   public ServerBootstrap() {}
/*     */   
/*     */   private ServerBootstrap(ServerBootstrap bootstrap) {
/*  57 */     super(bootstrap);
/*  58 */     this.childGroup = bootstrap.childGroup;
/*  59 */     this.childHandler = bootstrap.childHandler;
/*  60 */     synchronized (bootstrap.childOptions) {
/*  61 */       this.childOptions.putAll(bootstrap.childOptions);
/*     */     }
/*  63 */     synchronized (bootstrap.childAttrs) {
/*  64 */       this.childAttrs.putAll(bootstrap.childAttrs);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServerBootstrap group(EventLoopGroup group)
/*     */   {
/*  73 */     return group(group, group);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServerBootstrap group(EventLoopGroup parentGroup, EventLoopGroup childGroup)
/*     */   {
/*  82 */     super.group(parentGroup);
/*  83 */     if (childGroup == null) {
/*  84 */       throw new NullPointerException("childGroup");
/*     */     }
/*  86 */     if (this.childGroup != null) {
/*  87 */       throw new IllegalStateException("childGroup set already");
/*     */     }
/*  89 */     this.childGroup = childGroup;
/*  90 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T> ServerBootstrap childOption(ChannelOption<T> childOption, T value)
/*     */   {
/*  99 */     if (childOption == null) {
/* 100 */       throw new NullPointerException("childOption");
/*     */     }
/* 102 */     if (value == null) {
/* 103 */       synchronized (this.childOptions) {
/* 104 */         this.childOptions.remove(childOption);
/*     */       }
/*     */     } else {
/* 107 */       synchronized (this.childOptions) {
/* 108 */         this.childOptions.put(childOption, value);
/*     */       }
/*     */     }
/* 111 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T> ServerBootstrap childAttr(AttributeKey<T> childKey, T value)
/*     */   {
/* 119 */     if (childKey == null) {
/* 120 */       throw new NullPointerException("childKey");
/*     */     }
/* 122 */     if (value == null) {
/* 123 */       this.childAttrs.remove(childKey);
/*     */     } else {
/* 125 */       this.childAttrs.put(childKey, value);
/*     */     }
/* 127 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ServerBootstrap childHandler(ChannelHandler childHandler)
/*     */   {
/* 134 */     if (childHandler == null) {
/* 135 */       throw new NullPointerException("childHandler");
/*     */     }
/* 137 */     this.childHandler = childHandler;
/* 138 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EventLoopGroup childGroup()
/*     */   {
/* 146 */     return this.childGroup;
/*     */   }
/*     */   
/*     */   void init(Channel channel) throws Exception
/*     */   {
/* 151 */     Map<ChannelOption<?>, Object> options = options();
/* 152 */     synchronized (options) {
/* 153 */       channel.config().setOptions(options);
/*     */     }
/*     */     
/* 156 */     Map<AttributeKey<?>, Object> attrs = attrs();
/* 157 */     synchronized (attrs) {
/* 158 */       for (Map.Entry<AttributeKey<?>, Object> e : attrs.entrySet())
/*     */       {
/* 160 */         AttributeKey<Object> key = (AttributeKey)e.getKey();
/* 161 */         channel.attr(key).set(e.getValue());
/*     */       }
/*     */     }
/*     */     
/* 165 */     ChannelPipeline p = channel.pipeline();
/* 166 */     if (handler() != null) {
/* 167 */       p.addLast(new ChannelHandler[] { handler() });
/*     */     }
/*     */     
/* 170 */     final EventLoopGroup currentChildGroup = this.childGroup;
/* 171 */     final ChannelHandler currentChildHandler = this.childHandler;
/*     */     
/*     */     final Map.Entry<ChannelOption<?>, Object>[] currentChildOptions;
/* 174 */     synchronized (this.childOptions) {
/* 175 */       currentChildOptions = (Map.Entry[])this.childOptions.entrySet().toArray(newOptionArray(this.childOptions.size())); }
/*     */     final Object currentChildAttrs;
/* 177 */     synchronized (this.childAttrs) {
/* 178 */       currentChildAttrs = (Map.Entry[])this.childAttrs.entrySet().toArray(newAttrArray(this.childAttrs.size()));
/*     */     }
/*     */     
/* 181 */     p.addLast(new ChannelHandler[] { new ChannelInitializer()
/*     */     {
/*     */       public void initChannel(Channel ch) throws Exception {
/* 184 */         ch.pipeline().addLast(new ChannelHandler[] { new ServerBootstrap.ServerBootstrapAcceptor(currentChildGroup, currentChildHandler, currentChildOptions, currentChildAttrs) });
/*     */       }
/*     */     } });
/*     */   }
/*     */   
/*     */ 
/*     */   public ServerBootstrap validate()
/*     */   {
/* 192 */     super.validate();
/* 193 */     if (this.childHandler == null) {
/* 194 */       throw new IllegalStateException("childHandler not set");
/*     */     }
/* 196 */     if (this.childGroup == null) {
/* 197 */       logger.warn("childGroup is not set. Using parentGroup instead.");
/* 198 */       this.childGroup = group();
/*     */     }
/* 200 */     return this;
/*     */   }
/*     */   
/*     */   private static Map.Entry<ChannelOption<?>, Object>[] newOptionArray(int size)
/*     */   {
/* 205 */     return new Map.Entry[size];
/*     */   }
/*     */   
/*     */   private static Map.Entry<AttributeKey<?>, Object>[] newAttrArray(int size)
/*     */   {
/* 210 */     return new Map.Entry[size];
/*     */   }
/*     */   
/*     */   private static class ServerBootstrapAcceptor
/*     */     extends ChannelHandlerAdapter
/*     */   {
/*     */     private final EventLoopGroup childGroup;
/*     */     private final ChannelHandler childHandler;
/*     */     private final Map.Entry<ChannelOption<?>, Object>[] childOptions;
/*     */     private final Map.Entry<AttributeKey<?>, Object>[] childAttrs;
/*     */     
/*     */     ServerBootstrapAcceptor(EventLoopGroup childGroup, ChannelHandler childHandler, Map.Entry<ChannelOption<?>, Object>[] childOptions, Map.Entry<AttributeKey<?>, Object>[] childAttrs)
/*     */     {
/* 223 */       this.childGroup = childGroup;
/* 224 */       this.childHandler = childHandler;
/* 225 */       this.childOptions = childOptions;
/* 226 */       this.childAttrs = childAttrs;
/*     */     }
/*     */     
/*     */ 
/*     */     public void channelRead(ChannelHandlerContext ctx, Object msg)
/*     */     {
/* 232 */       final Channel child = (Channel)msg;
/*     */       
/* 234 */       child.pipeline().addLast(new ChannelHandler[] { this.childHandler });
/*     */       
/* 236 */       for (Map.Entry<ChannelOption<?>, Object> e : this.childOptions) {
/*     */         try {
/* 238 */           if (!child.config().setOption((ChannelOption)e.getKey(), e.getValue())) {
/* 239 */             ServerBootstrap.logger.warn("Unknown channel option: " + e);
/*     */           }
/*     */         } catch (Throwable t) {
/* 242 */           ServerBootstrap.logger.warn("Failed to set a channel option: " + child, t);
/*     */         }
/*     */       }
/*     */       
/* 246 */       for (Map.Entry<AttributeKey<?>, Object> e : this.childAttrs) {
/* 247 */         child.attr((AttributeKey)e.getKey()).set(e.getValue());
/*     */       }
/*     */       try
/*     */       {
/* 251 */         this.childGroup.register(child).addListener(new ChannelFutureListener()
/*     */         {
/*     */           public void operationComplete(ChannelFuture future) throws Exception {
/* 254 */             if (!future.isSuccess()) {
/* 255 */               ServerBootstrap.ServerBootstrapAcceptor.forceClose(child, future.cause());
/*     */             }
/*     */           }
/*     */         });
/*     */       } catch (Throwable t) {
/* 260 */         forceClose(child, t);
/*     */       }
/*     */     }
/*     */     
/*     */     private static void forceClose(Channel child, Throwable t) {
/* 265 */       child.unsafe().closeForcibly();
/* 266 */       ServerBootstrap.logger.warn("Failed to register an accepted channel: " + child, t);
/*     */     }
/*     */     
/*     */     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
/*     */     {
/* 271 */       final ChannelConfig config = ctx.channel().config();
/* 272 */       if (config.isAutoRead())
/*     */       {
/*     */ 
/* 275 */         config.setAutoRead(false);
/* 276 */         ctx.channel().eventLoop().schedule(new Runnable()
/*     */         {
/*     */ 
/* 279 */           public void run() { config.setAutoRead(true); } }, 1L, TimeUnit.SECONDS);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 285 */       ctx.fireExceptionCaught(cause);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ServerBootstrap clone()
/*     */   {
/* 292 */     return new ServerBootstrap(this);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 297 */     StringBuilder buf = new StringBuilder(super.toString());
/* 298 */     buf.setLength(buf.length() - 1);
/* 299 */     buf.append(", ");
/* 300 */     if (this.childGroup != null) {
/* 301 */       buf.append("childGroup: ");
/* 302 */       buf.append(StringUtil.simpleClassName(this.childGroup));
/* 303 */       buf.append(", ");
/*     */     }
/* 305 */     synchronized (this.childOptions) {
/* 306 */       if (!this.childOptions.isEmpty()) {
/* 307 */         buf.append("childOptions: ");
/* 308 */         buf.append(this.childOptions);
/* 309 */         buf.append(", ");
/*     */       }
/*     */     }
/* 312 */     synchronized (this.childAttrs) {
/* 313 */       if (!this.childAttrs.isEmpty()) {
/* 314 */         buf.append("childAttrs: ");
/* 315 */         buf.append(this.childAttrs);
/* 316 */         buf.append(", ");
/*     */       }
/*     */     }
/* 319 */     if (this.childHandler != null) {
/* 320 */       buf.append("childHandler: ");
/* 321 */       buf.append(this.childHandler);
/* 322 */       buf.append(", ");
/*     */     }
/* 324 */     if (buf.charAt(buf.length() - 1) == '(') {
/* 325 */       buf.append(')');
/*     */     } else {
/* 327 */       buf.setCharAt(buf.length() - 2, ')');
/* 328 */       buf.setLength(buf.length() - 1);
/*     */     }
/*     */     
/* 331 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\bootstrap\ServerBootstrap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */