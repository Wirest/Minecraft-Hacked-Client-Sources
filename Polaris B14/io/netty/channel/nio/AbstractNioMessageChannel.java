/*     */ package io.netty.channel.nio;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.ServerChannel;
/*     */ import java.io.IOException;
/*     */ import java.net.PortUnreachableException;
/*     */ import java.nio.channels.SelectableChannel;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public abstract class AbstractNioMessageChannel
/*     */   extends AbstractNioChannel
/*     */ {
/*     */   protected AbstractNioMessageChannel(Channel parent, SelectableChannel ch, int readInterestOp)
/*     */   {
/*  40 */     super(parent, ch, readInterestOp);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  45 */   protected AbstractNioChannel.AbstractNioUnsafe newUnsafe() { return new NioMessageUnsafe(null); }
/*     */   
/*     */   private final class NioMessageUnsafe extends AbstractNioChannel.AbstractNioUnsafe {
/*  48 */     private NioMessageUnsafe() { super(); }
/*     */     
/*  50 */     private final List<Object> readBuf = new ArrayList();
/*     */     
/*     */     public void read()
/*     */     {
/*  54 */       assert (AbstractNioMessageChannel.this.eventLoop().inEventLoop());
/*  55 */       ChannelConfig config = AbstractNioMessageChannel.this.config();
/*  56 */       if ((!config.isAutoRead()) && (!AbstractNioMessageChannel.this.isReadPending()))
/*     */       {
/*  58 */         removeReadOp();
/*  59 */         return;
/*     */       }
/*     */       
/*  62 */       int maxMessagesPerRead = config.getMaxMessagesPerRead();
/*  63 */       ChannelPipeline pipeline = AbstractNioMessageChannel.this.pipeline();
/*  64 */       boolean closed = false;
/*  65 */       Throwable exception = null;
/*     */       try {
/*     */         try {
/*     */           for (;;) {
/*  69 */             int localRead = AbstractNioMessageChannel.this.doReadMessages(this.readBuf);
/*  70 */             if (localRead == 0) {
/*     */               break;
/*     */             }
/*  73 */             if (localRead < 0) {
/*  74 */               closed = true;
/*     */ 
/*     */ 
/*     */ 
/*     */             }
/*  79 */             else if (config.isAutoRead())
/*     */             {
/*     */ 
/*     */ 
/*  83 */               if (this.readBuf.size() >= maxMessagesPerRead)
/*     */                 break;
/*     */             }
/*     */           }
/*     */         } catch (Throwable t) {
/*  88 */           exception = t;
/*     */         }
/*  90 */         AbstractNioMessageChannel.this.setReadPending(false);
/*  91 */         int size = this.readBuf.size();
/*  92 */         for (int i = 0; i < size; i++) {
/*  93 */           pipeline.fireChannelRead(this.readBuf.get(i));
/*     */         }
/*     */         
/*  96 */         this.readBuf.clear();
/*  97 */         pipeline.fireChannelReadComplete();
/*     */         
/*  99 */         if (exception != null) {
/* 100 */           if (((exception instanceof IOException)) && (!(exception instanceof PortUnreachableException)))
/*     */           {
/*     */ 
/* 103 */             closed = !(AbstractNioMessageChannel.this instanceof ServerChannel);
/*     */           }
/*     */           
/* 106 */           pipeline.fireExceptionCaught(exception);
/*     */         }
/*     */         
/* 109 */         if ((closed) && 
/* 110 */           (AbstractNioMessageChannel.this.isOpen())) {
/* 111 */           close(voidPromise());
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/* 121 */         if ((!config.isAutoRead()) && (!AbstractNioMessageChannel.this.isReadPending())) {
/* 122 */           removeReadOp();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception
/*     */   {
/* 130 */     SelectionKey key = selectionKey();
/* 131 */     int interestOps = key.interestOps();
/*     */     for (;;)
/*     */     {
/* 134 */       Object msg = in.current();
/* 135 */       if (msg == null)
/*     */       {
/* 137 */         if ((interestOps & 0x4) == 0) break;
/* 138 */         key.interestOps(interestOps & 0xFFFFFFFB); break;
/*     */       }
/*     */       
/*     */       try
/*     */       {
/* 143 */         boolean done = false;
/* 144 */         for (int i = config().getWriteSpinCount() - 1; i >= 0; i--) {
/* 145 */           if (doWriteMessage(msg, in)) {
/* 146 */             done = true;
/* 147 */             break;
/*     */           }
/*     */         }
/*     */         
/* 151 */         if (done) {
/* 152 */           in.remove();
/*     */         }
/*     */         else {
/* 155 */           if ((interestOps & 0x4) == 0) {
/* 156 */             key.interestOps(interestOps | 0x4);
/*     */           }
/* 158 */           break;
/*     */         }
/*     */       } catch (IOException e) {
/* 161 */         if (continueOnWriteError()) {
/* 162 */           in.remove(e);
/*     */         } else {
/* 164 */           throw e;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean continueOnWriteError()
/*     */   {
/* 174 */     return false;
/*     */   }
/*     */   
/*     */   protected abstract int doReadMessages(List<Object> paramList)
/*     */     throws Exception;
/*     */   
/*     */   protected abstract boolean doWriteMessage(Object paramObject, ChannelOutboundBuffer paramChannelOutboundBuffer)
/*     */     throws Exception;
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\nio\AbstractNioMessageChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */