/*    */ package io.netty.channel;
/*    */ 
/*    */ import io.netty.util.concurrent.ExecutorServiceFactory;
/*    */ import io.netty.util.concurrent.MultithreadEventExecutorGroup;
/*    */ import io.netty.util.internal.SystemPropertyUtil;
/*    */ import io.netty.util.internal.logging.InternalLogger;
/*    */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*    */ import java.util.concurrent.Executor;
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
/*    */ public abstract class MultithreadEventLoopGroup
/*    */   extends MultithreadEventExecutorGroup
/*    */   implements EventLoopGroup
/*    */ {
/* 32 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(MultithreadEventLoopGroup.class);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 37 */   private static final int DEFAULT_EVENT_LOOP_THREADS = Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", Runtime.getRuntime().availableProcessors() * 2));
/*    */   
/*    */   static {
/* 40 */     if (logger.isDebugEnabled()) {
/* 41 */       logger.debug("-Dio.netty.eventLoopThreads: {}", Integer.valueOf(DEFAULT_EVENT_LOOP_THREADS));
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected MultithreadEventLoopGroup(int nEventLoops, Executor executor, Object... args)
/*    */   {
/* 49 */     super(nEventLoops == 0 ? DEFAULT_EVENT_LOOP_THREADS : nEventLoops, executor, args);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected MultithreadEventLoopGroup(int nEventLoops, ExecutorServiceFactory executorServiceFactory, Object... args)
/*    */   {
/* 58 */     super(nEventLoops == 0 ? DEFAULT_EVENT_LOOP_THREADS : nEventLoops, executorServiceFactory, args);
/*    */   }
/*    */   
/*    */   public EventLoop next()
/*    */   {
/* 63 */     return (EventLoop)super.next();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ChannelFuture register(Channel channel)
/*    */   {
/* 71 */     return next().register(channel);
/*    */   }
/*    */   
/*    */   public ChannelFuture register(Channel channel, ChannelPromise promise)
/*    */   {
/* 76 */     return next().register(channel, promise);
/*    */   }
/*    */   
/*    */   protected abstract EventLoop newChild(Executor paramExecutor, Object... paramVarArgs)
/*    */     throws Exception;
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\MultithreadEventLoopGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */