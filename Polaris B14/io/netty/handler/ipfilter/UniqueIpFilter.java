/*    */ package io.netty.handler.ipfilter;
/*    */ 
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.ChannelFuture;
/*    */ import io.netty.channel.ChannelFutureListener;
/*    */ import io.netty.channel.ChannelHandler.Sharable;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.util.internal.ConcurrentSet;
/*    */ import java.net.InetAddress;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ChannelHandler.Sharable
/*    */ public class UniqueIpFilter
/*    */   extends AbstractRemoteAddressFilter<InetSocketAddress>
/*    */ {
/* 36 */   private final Set<InetAddress> connected = new ConcurrentSet();
/*    */   
/*    */   protected boolean accept(ChannelHandlerContext ctx, InetSocketAddress remoteAddress) throws Exception
/*    */   {
/* 40 */     final InetAddress remoteIp = remoteAddress.getAddress();
/* 41 */     if (this.connected.contains(remoteIp)) {
/* 42 */       return false;
/*    */     }
/* 44 */     this.connected.add(remoteIp);
/* 45 */     ctx.channel().closeFuture().addListener(new ChannelFutureListener()
/*    */     {
/*    */       public void operationComplete(ChannelFuture future) throws Exception {
/* 48 */         UniqueIpFilter.this.connected.remove(remoteIp);
/*    */       }
/* 50 */     });
/* 51 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ipfilter\UniqueIpFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */