/*     */ package io.netty.channel.nio;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.FileRegion;
/*     */ import io.netty.channel.RecvByteBufAllocator.Handle;
/*     */ import io.netty.channel.socket.ChannelInputShutdownEvent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.SelectableChannel;
/*     */ import java.nio.channels.SelectionKey;
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
/*     */ public abstract class AbstractNioByteChannel
/*     */   extends AbstractNioChannel
/*     */ {
/*  39 */   private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(FileRegion.class) + ')';
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Runnable flushTask;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractNioByteChannel(Channel parent, SelectableChannel ch)
/*     */   {
/*  52 */     super(parent, ch, 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  57 */   protected AbstractNioChannel.AbstractNioUnsafe newUnsafe() { return new NioByteUnsafe(); }
/*     */   
/*     */   protected class NioByteUnsafe extends AbstractNioChannel.AbstractNioUnsafe {
/*  60 */     protected NioByteUnsafe() { super(); }
/*     */     
/*     */     private void closeOnRead(ChannelPipeline pipeline) {
/*  63 */       SelectionKey key = AbstractNioByteChannel.this.selectionKey();
/*  64 */       AbstractNioByteChannel.this.setInputShutdown();
/*  65 */       if (AbstractNioByteChannel.this.isOpen()) {
/*  66 */         if (Boolean.TRUE.equals(AbstractNioByteChannel.this.config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
/*  67 */           key.interestOps(key.interestOps() & (AbstractNioByteChannel.this.readInterestOp ^ 0xFFFFFFFF));
/*  68 */           pipeline.fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
/*     */         } else {
/*  70 */           close(voidPromise());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void handleReadException(ChannelPipeline pipeline, ByteBuf byteBuf, Throwable cause, boolean close)
/*     */     {
/*  77 */       if (byteBuf != null) {
/*  78 */         if (byteBuf.isReadable()) {
/*  79 */           AbstractNioByteChannel.this.setReadPending(false);
/*  80 */           pipeline.fireChannelRead(byteBuf);
/*     */         } else {
/*  82 */           byteBuf.release();
/*     */         }
/*     */       }
/*  85 */       pipeline.fireChannelReadComplete();
/*  86 */       pipeline.fireExceptionCaught(cause);
/*  87 */       if ((close) || ((cause instanceof IOException))) {
/*  88 */         closeOnRead(pipeline);
/*     */       }
/*     */     }
/*     */     
/*     */     public final void read()
/*     */     {
/*  94 */       ChannelConfig config = AbstractNioByteChannel.this.config();
/*  95 */       if ((!config.isAutoRead()) && (!AbstractNioByteChannel.this.isReadPending()))
/*     */       {
/*  97 */         removeReadOp();
/*  98 */         return;
/*     */       }
/*     */       
/* 101 */       ChannelPipeline pipeline = AbstractNioByteChannel.this.pipeline();
/* 102 */       ByteBufAllocator allocator = config.getAllocator();
/* 103 */       int maxMessagesPerRead = config.getMaxMessagesPerRead();
/* 104 */       RecvByteBufAllocator.Handle allocHandle = recvBufAllocHandle();
/*     */       
/* 106 */       ByteBuf byteBuf = null;
/* 107 */       int messages = 0;
/* 108 */       boolean close = false;
/*     */       try {
/* 110 */         int totalReadAmount = 0;
/* 111 */         boolean readPendingReset = false;
/*     */         do {
/* 113 */           byteBuf = allocHandle.allocate(allocator);
/* 114 */           int writable = byteBuf.writableBytes();
/* 115 */           int localReadAmount = AbstractNioByteChannel.this.doReadBytes(byteBuf);
/* 116 */           if (localReadAmount <= 0)
/*     */           {
/* 118 */             byteBuf.release();
/* 119 */             byteBuf = null;
/* 120 */             close = localReadAmount < 0;
/* 121 */             break;
/*     */           }
/* 123 */           if (!readPendingReset) {
/* 124 */             readPendingReset = true;
/* 125 */             AbstractNioByteChannel.this.setReadPending(false);
/*     */           }
/* 127 */           pipeline.fireChannelRead(byteBuf);
/* 128 */           byteBuf = null;
/*     */           
/* 130 */           if (totalReadAmount >= Integer.MAX_VALUE - localReadAmount)
/*     */           {
/* 132 */             totalReadAmount = Integer.MAX_VALUE;
/* 133 */             break;
/*     */           }
/*     */           
/* 136 */           totalReadAmount += localReadAmount;
/*     */           
/*     */ 
/* 139 */           if (!config.isAutoRead()) {
/*     */             break;
/*     */           }
/*     */           
/* 143 */           if (localReadAmount < writable) {
/*     */             break;
/*     */           }
/*     */           
/*     */ 
/* 148 */           messages++; } while (messages < maxMessagesPerRead);
/*     */         
/* 150 */         pipeline.fireChannelReadComplete();
/* 151 */         allocHandle.record(totalReadAmount);
/*     */         
/* 153 */         if (close) {
/* 154 */           closeOnRead(pipeline);
/* 155 */           close = false;
/*     */         }
/*     */       } catch (Throwable t) {
/* 158 */         handleReadException(pipeline, byteBuf, t, close);
/*     */ 
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/*     */ 
/* 166 */         if ((!config.isAutoRead()) && (!AbstractNioByteChannel.this.isReadPending())) {
/* 167 */           removeReadOp();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception
/*     */   {
/* 175 */     int writeSpinCount = -1;
/*     */     for (;;)
/*     */     {
/* 178 */       Object msg = in.current();
/* 179 */       if (msg == null)
/*     */       {
/* 181 */         clearOpWrite();
/* 182 */         break;
/*     */       }
/*     */       
/* 185 */       if ((msg instanceof ByteBuf)) {
/* 186 */         ByteBuf buf = (ByteBuf)msg;
/* 187 */         int readableBytes = buf.readableBytes();
/* 188 */         if (readableBytes == 0) {
/* 189 */           in.remove();
/*     */         }
/*     */         else
/*     */         {
/* 193 */           boolean setOpWrite = false;
/* 194 */           boolean done = false;
/* 195 */           long flushedAmount = 0L;
/* 196 */           if (writeSpinCount == -1) {
/* 197 */             writeSpinCount = config().getWriteSpinCount();
/*     */           }
/* 199 */           for (int i = writeSpinCount - 1; i >= 0; i--) {
/* 200 */             int localFlushedAmount = doWriteBytes(buf);
/* 201 */             if (localFlushedAmount == 0) {
/* 202 */               setOpWrite = true;
/* 203 */               break;
/*     */             }
/*     */             
/* 206 */             flushedAmount += localFlushedAmount;
/* 207 */             if (!buf.isReadable()) {
/* 208 */               done = true;
/* 209 */               break;
/*     */             }
/*     */           }
/*     */           
/* 213 */           in.progress(flushedAmount);
/*     */           
/* 215 */           if (done) {
/* 216 */             in.remove();
/*     */           } else {
/* 218 */             incompleteWrite(setOpWrite);
/* 219 */             break;
/*     */           }
/* 221 */         } } else if ((msg instanceof FileRegion)) {
/* 222 */         FileRegion region = (FileRegion)msg;
/* 223 */         boolean done = region.transfered() >= region.count();
/* 224 */         boolean setOpWrite = false;
/*     */         
/* 226 */         if (!done) {
/* 227 */           long flushedAmount = 0L;
/* 228 */           if (writeSpinCount == -1) {
/* 229 */             writeSpinCount = config().getWriteSpinCount();
/*     */           }
/*     */           
/* 232 */           for (int i = writeSpinCount - 1; i >= 0; i--) {
/* 233 */             long localFlushedAmount = doWriteFileRegion(region);
/* 234 */             if (localFlushedAmount == 0L) {
/* 235 */               setOpWrite = true;
/* 236 */               break;
/*     */             }
/*     */             
/* 239 */             flushedAmount += localFlushedAmount;
/* 240 */             if (region.transfered() >= region.count()) {
/* 241 */               done = true;
/* 242 */               break;
/*     */             }
/*     */           }
/*     */           
/* 246 */           in.progress(flushedAmount);
/*     */         }
/*     */         
/* 249 */         if (done) {
/* 250 */           in.remove();
/*     */         } else {
/* 252 */           incompleteWrite(setOpWrite);
/* 253 */           break;
/*     */         }
/*     */       }
/*     */       else {
/* 257 */         throw new Error();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected final Object filterOutboundMessage(Object msg)
/*     */   {
/* 264 */     if ((msg instanceof ByteBuf)) {
/* 265 */       ByteBuf buf = (ByteBuf)msg;
/* 266 */       if (buf.isDirect()) {
/* 267 */         return msg;
/*     */       }
/*     */       
/* 270 */       return newDirectBuffer(buf);
/*     */     }
/*     */     
/* 273 */     if ((msg instanceof FileRegion)) {
/* 274 */       return msg;
/*     */     }
/*     */     
/* 277 */     throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
/*     */   }
/*     */   
/*     */ 
/*     */   protected final void incompleteWrite(boolean setOpWrite)
/*     */   {
/* 283 */     if (setOpWrite) {
/* 284 */       setOpWrite();
/*     */     }
/*     */     else {
/* 287 */       Runnable flushTask = this.flushTask;
/* 288 */       if (flushTask == null) {
/* 289 */         flushTask = this. = new Runnable()
/*     */         {
/*     */           public void run() {
/* 292 */             AbstractNioByteChannel.this.flush();
/*     */           }
/*     */         };
/*     */       }
/* 296 */       eventLoop().execute(flushTask);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract long doWriteFileRegion(FileRegion paramFileRegion)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract int doReadBytes(ByteBuf paramByteBuf)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract int doWriteBytes(ByteBuf paramByteBuf)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */   protected final void setOpWrite()
/*     */   {
/* 321 */     SelectionKey key = selectionKey();
/*     */     
/*     */ 
/*     */ 
/* 325 */     if (!key.isValid()) {
/* 326 */       return;
/*     */     }
/* 328 */     int interestOps = key.interestOps();
/* 329 */     if ((interestOps & 0x4) == 0) {
/* 330 */       key.interestOps(interestOps | 0x4);
/*     */     }
/*     */   }
/*     */   
/*     */   protected final void clearOpWrite() {
/* 335 */     SelectionKey key = selectionKey();
/*     */     
/*     */ 
/*     */ 
/* 339 */     if (!key.isValid()) {
/* 340 */       return;
/*     */     }
/* 342 */     int interestOps = key.interestOps();
/* 343 */     if ((interestOps & 0x4) != 0) {
/* 344 */       key.interestOps(interestOps & 0xFFFFFFFB);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\nio\AbstractNioByteChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */