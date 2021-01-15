/*     */ package io.netty.channel.oio;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.Channel.Unsafe;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.FileRegion;
/*     */ import io.netty.channel.RecvByteBufAllocator.Handle;
/*     */ import io.netty.channel.socket.ChannelInputShutdownEvent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.io.IOException;
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
/*     */ public abstract class AbstractOioByteChannel
/*     */   extends AbstractOioChannel
/*     */ {
/*  37 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*  38 */   private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(FileRegion.class) + ')';
/*     */   
/*     */ 
/*     */ 
/*     */   private volatile boolean inputShutdown;
/*     */   
/*     */ 
/*     */ 
/*     */   protected AbstractOioByteChannel(Channel parent)
/*     */   {
/*  48 */     super(parent);
/*     */   }
/*     */   
/*     */   protected boolean isInputShutdown() {
/*  52 */     return this.inputShutdown;
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/*  57 */     return METADATA;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean checkInputShutdown()
/*     */   {
/*  65 */     if (this.inputShutdown) {
/*     */       try {
/*  67 */         Thread.sleep(1000L);
/*     */       }
/*     */       catch (InterruptedException e) {}
/*     */       
/*  71 */       return true;
/*     */     }
/*  73 */     return false;
/*     */   }
/*     */   
/*     */   protected void doRead()
/*     */   {
/*  78 */     if (checkInputShutdown()) {
/*  79 */       return;
/*     */     }
/*  81 */     ChannelConfig config = config();
/*  82 */     ChannelPipeline pipeline = pipeline();
/*     */     
/*  84 */     RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
/*     */     
/*  86 */     ByteBuf byteBuf = allocHandle.allocate(alloc());
/*     */     
/*  88 */     boolean closed = false;
/*  89 */     boolean read = false;
/*  90 */     Throwable exception = null;
/*  91 */     int localReadAmount = 0;
/*     */     try {
/*  93 */       int totalReadAmount = 0;
/*     */       for (;;)
/*     */       {
/*  96 */         localReadAmount = doReadBytes(byteBuf);
/*  97 */         if (localReadAmount > 0) {
/*  98 */           read = true;
/*  99 */         } else if (localReadAmount < 0) {
/* 100 */           closed = true;
/*     */         }
/*     */         
/* 103 */         int available = available();
/* 104 */         if (available <= 0) {
/*     */           break;
/*     */         }
/*     */         
/* 108 */         if (!byteBuf.isWritable()) {
/* 109 */           int capacity = byteBuf.capacity();
/* 110 */           int maxCapacity = byteBuf.maxCapacity();
/* 111 */           if (capacity == maxCapacity) {
/* 112 */             if (read) {
/* 113 */               read = false;
/* 114 */               pipeline.fireChannelRead(byteBuf);
/* 115 */               byteBuf = alloc().buffer();
/*     */             }
/*     */           } else {
/* 118 */             int writerIndex = byteBuf.writerIndex();
/* 119 */             if (writerIndex + available > maxCapacity) {
/* 120 */               byteBuf.capacity(maxCapacity);
/*     */             } else {
/* 122 */               byteBuf.ensureWritable(available);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 127 */         if (totalReadAmount >= Integer.MAX_VALUE - localReadAmount)
/*     */         {
/* 129 */           totalReadAmount = Integer.MAX_VALUE;
/*     */         }
/*     */         else
/*     */         {
/* 133 */           totalReadAmount += localReadAmount;
/*     */           
/* 135 */           if (!config.isAutoRead()) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 141 */       allocHandle.record(totalReadAmount);
/*     */     }
/*     */     catch (Throwable t) {
/* 144 */       exception = t;
/*     */     } finally {
/* 146 */       if (read) {
/* 147 */         pipeline.fireChannelRead(byteBuf);
/*     */       }
/*     */       else {
/* 150 */         byteBuf.release();
/*     */       }
/*     */       
/* 153 */       pipeline.fireChannelReadComplete();
/* 154 */       if (exception != null) {
/* 155 */         if ((exception instanceof IOException)) {
/* 156 */           closed = true;
/* 157 */           pipeline().fireExceptionCaught(exception);
/*     */         } else {
/* 159 */           pipeline.fireExceptionCaught(exception);
/* 160 */           unsafe().close(voidPromise());
/*     */         }
/*     */       }
/*     */       
/* 164 */       if (closed) {
/* 165 */         this.inputShutdown = true;
/* 166 */         if (isOpen()) {
/* 167 */           if (Boolean.TRUE.equals(config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
/* 168 */             pipeline.fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
/*     */           } else {
/* 170 */             unsafe().close(unsafe().voidPromise());
/*     */           }
/*     */         }
/*     */       }
/* 174 */       if ((localReadAmount == 0) && (isActive()))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 181 */         read();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception
/*     */   {
/*     */     for (;;) {
/* 189 */       Object msg = in.current();
/* 190 */       if (msg == null) {
/*     */         break;
/*     */       }
/*     */       
/* 194 */       if ((msg instanceof ByteBuf)) {
/* 195 */         ByteBuf buf = (ByteBuf)msg;
/* 196 */         int readableBytes = buf.readableBytes();
/* 197 */         while (readableBytes > 0) {
/* 198 */           doWriteBytes(buf);
/* 199 */           int newReadableBytes = buf.readableBytes();
/* 200 */           in.progress(readableBytes - newReadableBytes);
/* 201 */           readableBytes = newReadableBytes;
/*     */         }
/* 203 */         in.remove();
/* 204 */       } else if ((msg instanceof FileRegion)) {
/* 205 */         FileRegion region = (FileRegion)msg;
/* 206 */         long transfered = region.transfered();
/* 207 */         doWriteFileRegion(region);
/* 208 */         in.progress(region.transfered() - transfered);
/* 209 */         in.remove();
/*     */       } else {
/* 211 */         in.remove(new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg)));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected final Object filterOutboundMessage(Object msg)
/*     */     throws Exception
/*     */   {
/* 219 */     if (((msg instanceof ByteBuf)) || ((msg instanceof FileRegion))) {
/* 220 */       return msg;
/*     */     }
/*     */     
/* 223 */     throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
/*     */   }
/*     */   
/*     */   protected abstract int available();
/*     */   
/*     */   protected abstract int doReadBytes(ByteBuf paramByteBuf)
/*     */     throws Exception;
/*     */   
/*     */   protected abstract void doWriteBytes(ByteBuf paramByteBuf)
/*     */     throws Exception;
/*     */   
/*     */   protected abstract void doWriteFileRegion(FileRegion paramFileRegion)
/*     */     throws Exception;
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\oio\AbstractOioByteChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */