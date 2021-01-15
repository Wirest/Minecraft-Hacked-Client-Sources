/*    */ package io.netty.channel.oio;
/*    */ 
/*    */ import io.netty.channel.ThreadPerChannelEventLoopGroup;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.concurrent.Executors;
/*    */ import java.util.concurrent.ThreadFactory;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OioEventLoopGroup
/*    */   extends ThreadPerChannelEventLoopGroup
/*    */ {
/*    */   public OioEventLoopGroup()
/*    */   {
/* 39 */     this(0);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public OioEventLoopGroup(int maxChannels)
/*    */   {
/* 50 */     this(maxChannels, Executors.defaultThreadFactory());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public OioEventLoopGroup(int maxChannels, Executor executor)
/*    */   {
/* 63 */     super(maxChannels, executor, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public OioEventLoopGroup(int maxChannels, ThreadFactory threadFactory)
/*    */   {
/* 76 */     super(maxChannels, threadFactory, new Object[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\oio\OioEventLoopGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */