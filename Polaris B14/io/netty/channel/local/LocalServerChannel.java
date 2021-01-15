/*     */ package io.netty.channel.local;
/*     */ 
/*     */ import io.netty.channel.AbstractServerChannel;
/*     */ import io.netty.channel.Channel.Unsafe;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.DefaultChannelConfig;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.SingleThreadEventLoop;
/*     */ import java.net.SocketAddress;
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
/*     */ public class LocalServerChannel
/*     */   extends AbstractServerChannel
/*     */ {
/*  35 */   private final ChannelConfig config = new DefaultChannelConfig(this);
/*  36 */   private final Queue<Object> inboundBuffer = new ArrayDeque();
/*  37 */   private final Runnable shutdownHook = new Runnable()
/*     */   {
/*     */     public void run() {
/*  40 */       LocalServerChannel.this.unsafe().close(LocalServerChannel.this.unsafe().voidPromise());
/*     */     }
/*     */   };
/*     */   
/*     */   private volatile int state;
/*     */   private volatile LocalAddress localAddress;
/*     */   private volatile boolean acceptInProgress;
/*     */   
/*     */   public ChannelConfig config()
/*     */   {
/*  50 */     return this.config;
/*     */   }
/*     */   
/*     */   public LocalAddress localAddress()
/*     */   {
/*  55 */     return (LocalAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   public LocalAddress remoteAddress()
/*     */   {
/*  60 */     return (LocalAddress)super.remoteAddress();
/*     */   }
/*     */   
/*     */   public boolean isOpen()
/*     */   {
/*  65 */     return this.state < 2;
/*     */   }
/*     */   
/*     */   public boolean isActive()
/*     */   {
/*  70 */     return this.state == 1;
/*     */   }
/*     */   
/*     */   protected boolean isCompatible(EventLoop loop)
/*     */   {
/*  75 */     return loop instanceof SingleThreadEventLoop;
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress0()
/*     */   {
/*  80 */     return this.localAddress;
/*     */   }
/*     */   
/*     */   protected void doRegister() throws Exception
/*     */   {
/*  85 */     ((SingleThreadEventLoop)eventLoop().unwrap()).addShutdownHook(this.shutdownHook);
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception
/*     */   {
/*  90 */     this.localAddress = LocalChannelRegistry.register(this, this.localAddress, localAddress);
/*  91 */     this.state = 1;
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception
/*     */   {
/*  96 */     if (this.state <= 1)
/*     */     {
/*  98 */       if (this.localAddress != null) {
/*  99 */         LocalChannelRegistry.unregister(this.localAddress);
/* 100 */         this.localAddress = null;
/*     */       }
/* 102 */       this.state = 2;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doDeregister() throws Exception
/*     */   {
/* 108 */     ((SingleThreadEventLoop)eventLoop().unwrap()).removeShutdownHook(this.shutdownHook);
/*     */   }
/*     */   
/*     */   protected void doBeginRead() throws Exception
/*     */   {
/* 113 */     if (this.acceptInProgress) {
/* 114 */       return;
/*     */     }
/*     */     
/* 117 */     Queue<Object> inboundBuffer = this.inboundBuffer;
/* 118 */     if (inboundBuffer.isEmpty()) {
/* 119 */       this.acceptInProgress = true;
/* 120 */       return;
/*     */     }
/*     */     
/* 123 */     ChannelPipeline pipeline = pipeline();
/*     */     for (;;) {
/* 125 */       Object m = inboundBuffer.poll();
/* 126 */       if (m == null) {
/*     */         break;
/*     */       }
/* 129 */       pipeline.fireChannelRead(m);
/*     */     }
/* 131 */     pipeline.fireChannelReadComplete();
/*     */   }
/*     */   
/*     */   LocalChannel serve(LocalChannel peer) {
/* 135 */     final LocalChannel child = new LocalChannel(this, peer);
/* 136 */     if (eventLoop().inEventLoop()) {
/* 137 */       serve0(child);
/*     */     } else {
/* 139 */       eventLoop().execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 142 */           LocalServerChannel.this.serve0(child);
/*     */         }
/*     */       });
/*     */     }
/* 146 */     return child;
/*     */   }
/*     */   
/*     */   private void serve0(LocalChannel child) {
/* 150 */     this.inboundBuffer.add(child);
/* 151 */     if (this.acceptInProgress) {
/* 152 */       this.acceptInProgress = false;
/* 153 */       ChannelPipeline pipeline = pipeline();
/*     */       for (;;) {
/* 155 */         Object m = this.inboundBuffer.poll();
/* 156 */         if (m == null) {
/*     */           break;
/*     */         }
/* 159 */         pipeline.fireChannelRead(m);
/*     */       }
/* 161 */       pipeline.fireChannelReadComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\local\LocalServerChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */