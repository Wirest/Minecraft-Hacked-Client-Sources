/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.util.internal.InternalThreadLocalMap;
/*     */ import java.net.SocketAddress;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChannelHandlerAdapter
/*     */   implements ChannelHandler
/*     */ {
/*     */   boolean added;
/*     */   
/*     */   public boolean isSharable()
/*     */   {
/*  46 */     Class<?> clazz = getClass();
/*  47 */     Map<Class<?>, Boolean> cache = InternalThreadLocalMap.get().handlerSharableCache();
/*  48 */     Boolean sharable = (Boolean)cache.get(clazz);
/*  49 */     if (sharable == null) {
/*  50 */       sharable = Boolean.valueOf(clazz.isAnnotationPresent(ChannelHandler.Sharable.class));
/*  51 */       cache.put(clazz, sharable);
/*     */     }
/*  53 */     return sharable.booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ChannelHandler.Skip
/*     */   public void handlerAdded(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ChannelHandler.Skip
/*     */   public void handlerRemoved(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ChannelHandler.Skip
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
/*     */     throws Exception
/*     */   {
/*  83 */     ctx.fireExceptionCaught(cause);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ChannelHandler.Skip
/*     */   public void channelRegistered(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/*  95 */     ctx.fireChannelRegistered();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ChannelHandler.Skip
/*     */   public void channelUnregistered(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 107 */     ctx.fireChannelUnregistered();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ChannelHandler.Skip
/*     */   public void channelActive(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 119 */     ctx.fireChannelActive();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ChannelHandler.Skip
/*     */   public void channelInactive(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 131 */     ctx.fireChannelInactive();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ChannelHandler.Skip
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg)
/*     */     throws Exception
/*     */   {
/* 143 */     ctx.fireChannelRead(msg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ChannelHandler.Skip
/*     */   public void channelReadComplete(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 155 */     ctx.fireChannelReadComplete();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ChannelHandler.Skip
/*     */   public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
/*     */     throws Exception
/*     */   {
/* 167 */     ctx.fireUserEventTriggered(evt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ChannelHandler.Skip
/*     */   public void channelWritabilityChanged(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 179 */     ctx.fireChannelWritabilityChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ChannelHandler.Skip
/*     */   public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 191 */     ctx.bind(localAddress, promise);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ChannelHandler.Skip
/*     */   public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 205 */     ctx.connect(remoteAddress, localAddress, promise);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ChannelHandler.Skip
/*     */   public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 217 */     ctx.disconnect(promise);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ChannelHandler.Skip
/*     */   public void close(ChannelHandlerContext ctx, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 229 */     ctx.close(promise);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ChannelHandler.Skip
/*     */   public void deregister(ChannelHandlerContext ctx, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 241 */     ctx.deregister(promise);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ChannelHandler.Skip
/*     */   public void read(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 253 */     ctx.read();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ChannelHandler.Skip
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 265 */     ctx.write(msg, promise);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ChannelHandler.Skip
/*     */   public void flush(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 277 */     ctx.flush();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\ChannelHandlerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */