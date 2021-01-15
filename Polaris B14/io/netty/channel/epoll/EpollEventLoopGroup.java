/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.MultithreadEventLoopGroup;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.ExecutorServiceFactory;
/*     */ import java.util.concurrent.Executor;
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
/*     */ public final class EpollEventLoopGroup
/*     */   extends MultithreadEventLoopGroup
/*     */ {
/*     */   public EpollEventLoopGroup()
/*     */   {
/*  40 */     this(0);
/*     */   }
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
/*     */   public EpollEventLoopGroup(int nEventLoops)
/*     */   {
/*  54 */     this(nEventLoops, (Executor)null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EpollEventLoopGroup(int nEventLoops, Executor executor)
/*     */   {
/*  67 */     this(nEventLoops, executor, 0);
/*     */   }
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
/*     */   public EpollEventLoopGroup(int nEventLoops, ExecutorServiceFactory executorServiceFactory)
/*     */   {
/*  81 */     this(nEventLoops, executorServiceFactory, 0);
/*     */   }
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
/*     */   @Deprecated
/*     */   public EpollEventLoopGroup(int nEventLoops, Executor executor, int maxEventsAtOnce)
/*     */   {
/*  98 */     super(nEventLoops, executor, new Object[] { Integer.valueOf(maxEventsAtOnce) });
/*     */   }
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
/*     */   @Deprecated
/*     */   public EpollEventLoopGroup(int nEventLoops, ExecutorServiceFactory executorServiceFactory, int maxEventsAtOnce)
/*     */   {
/* 116 */     super(nEventLoops, executorServiceFactory, new Object[] { Integer.valueOf(maxEventsAtOnce) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIoRatio(int ioRatio)
/*     */   {
/* 124 */     for (EventExecutor e : children()) {
/* 125 */       ((EpollEventLoop)e).setIoRatio(ioRatio);
/*     */     }
/*     */   }
/*     */   
/*     */   protected EventLoop newChild(Executor executor, Object... args) throws Exception
/*     */   {
/* 131 */     return new EpollEventLoop(this, executor, ((Integer)args[0]).intValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\EpollEventLoopGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */