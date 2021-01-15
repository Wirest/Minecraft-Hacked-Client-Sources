/*     */ package io.netty.channel.nio;
/*     */ 
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.MultithreadEventLoopGroup;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.ExecutorServiceFactory;
/*     */ import java.nio.channels.spi.SelectorProvider;
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
/*     */ public class NioEventLoopGroup
/*     */   extends MultithreadEventLoopGroup
/*     */ {
/*     */   public NioEventLoopGroup()
/*     */   {
/*  41 */     this(0);
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
/*     */   public NioEventLoopGroup(int nEventLoops)
/*     */   {
/*  56 */     this(nEventLoops, (Executor)null);
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
/*     */   public NioEventLoopGroup(int nEventLoops, Executor executor)
/*     */   {
/*  71 */     this(nEventLoops, executor, SelectorProvider.provider());
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
/*     */   public NioEventLoopGroup(int nEventLoops, ExecutorServiceFactory executorServiceFactory)
/*     */   {
/*  87 */     this(nEventLoops, executorServiceFactory, SelectorProvider.provider());
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
/*     */   public NioEventLoopGroup(int nEventLoops, Executor executor, SelectorProvider selectorProvider)
/*     */   {
/* 100 */     super(nEventLoops, executor, new Object[] { selectorProvider });
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
/*     */   public NioEventLoopGroup(int nEventLoops, ExecutorServiceFactory executorServiceFactory, SelectorProvider selectorProvider)
/*     */   {
/* 115 */     super(nEventLoops, executorServiceFactory, new Object[] { selectorProvider });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIoRatio(int ioRatio)
/*     */   {
/* 123 */     for (EventExecutor e : children()) {
/* 124 */       ((NioEventLoop)e).setIoRatio(ioRatio);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void rebuildSelectors()
/*     */   {
/* 133 */     for (EventExecutor e : children()) {
/* 134 */       ((NioEventLoop)e).rebuildSelector();
/*     */     }
/*     */   }
/*     */   
/*     */   protected EventLoop newChild(Executor executor, Object... args) throws Exception
/*     */   {
/* 140 */     return new NioEventLoop(this, executor, (SelectorProvider)args[0]);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\nio\NioEventLoopGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */