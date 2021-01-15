/*    */ package io.netty.channel.oio;
/*    */ 
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.Channel.Unsafe;
/*    */ import io.netty.channel.ChannelConfig;
/*    */ import io.netty.channel.ChannelPipeline;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public abstract class AbstractOioMessageChannel
/*    */   extends AbstractOioChannel
/*    */ {
/* 31 */   private final List<Object> readBuf = new ArrayList();
/*    */   
/*    */   protected AbstractOioMessageChannel(Channel parent) {
/* 34 */     super(parent);
/*    */   }
/*    */   
/*    */   protected void doRead()
/*    */   {
/* 39 */     ChannelConfig config = config();
/* 40 */     ChannelPipeline pipeline = pipeline();
/* 41 */     boolean closed = false;
/* 42 */     int maxMessagesPerRead = config.getMaxMessagesPerRead();
/*    */     
/* 44 */     Throwable exception = null;
/* 45 */     int localRead = 0;
/* 46 */     int totalRead = 0;
/*    */     try
/*    */     {
/*    */       for (;;) {
/* 50 */         localRead = doReadMessages(this.readBuf);
/* 51 */         if (localRead == 0) {
/*    */           break;
/*    */         }
/* 54 */         if (localRead < 0) {
/* 55 */           closed = true;
/*    */ 
/*    */         }
/*    */         else
/*    */         {
/* 60 */           int size = this.readBuf.size();
/* 61 */           for (int i = 0; i < size; i++) {
/* 62 */             pipeline.fireChannelRead(this.readBuf.get(i));
/*    */           }
/* 64 */           this.readBuf.clear();
/*    */           
/*    */ 
/*    */ 
/* 68 */           totalRead += localRead;
/* 69 */           if ((totalRead >= maxMessagesPerRead) || (!config.isAutoRead()))
/*    */             break;
/*    */         }
/*    */       }
/*    */     } catch (Throwable t) {
/* 74 */       exception = t;
/*    */     }
/*    */     
/* 77 */     pipeline.fireChannelReadComplete();
/*    */     
/* 79 */     if (exception != null) {
/* 80 */       if ((exception instanceof IOException)) {
/* 81 */         closed = true;
/*    */       }
/*    */       
/* 84 */       pipeline().fireExceptionCaught(exception);
/*    */     }
/*    */     
/* 87 */     if (closed) {
/* 88 */       if (isOpen()) {
/* 89 */         unsafe().close(unsafe().voidPromise());
/*    */       }
/* 91 */     } else if ((localRead == 0) && (isActive()))
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 98 */       read();
/*    */     }
/*    */   }
/*    */   
/*    */   protected abstract int doReadMessages(List<Object> paramList)
/*    */     throws Exception;
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\oio\AbstractOioMessageChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */