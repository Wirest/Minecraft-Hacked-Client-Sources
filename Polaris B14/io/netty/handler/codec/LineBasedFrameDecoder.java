/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
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
/*     */ public class LineBasedFrameDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private final int maxLength;
/*     */   private final boolean failFast;
/*     */   private final boolean stripDelimiter;
/*     */   private boolean discarding;
/*     */   private int discardedBytes;
/*     */   
/*     */   public LineBasedFrameDecoder(int maxLength)
/*     */   {
/*  48 */     this(maxLength, true, false);
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
/*     */ 
/*     */ 
/*     */   public LineBasedFrameDecoder(int maxLength, boolean stripDelimiter, boolean failFast)
/*     */   {
/*  67 */     this.maxLength = maxLength;
/*  68 */     this.failFast = failFast;
/*  69 */     this.stripDelimiter = stripDelimiter;
/*     */   }
/*     */   
/*     */   protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/*  74 */     Object decoded = decode(ctx, in);
/*  75 */     if (decoded != null) {
/*  76 */       out.add(decoded);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer)
/*     */     throws Exception
/*     */   {
/*  89 */     int eol = findEndOfLine(buffer);
/*  90 */     if (!this.discarding) {
/*  91 */       if (eol >= 0)
/*     */       {
/*  93 */         int length = eol - buffer.readerIndex();
/*  94 */         int delimLength = buffer.getByte(eol) == 13 ? 2 : 1;
/*     */         
/*  96 */         if (length > this.maxLength) {
/*  97 */           buffer.readerIndex(eol + delimLength);
/*  98 */           fail(ctx, length);
/*  99 */           return null;
/*     */         }
/*     */         ByteBuf frame;
/* 102 */         if (this.stripDelimiter) {
/* 103 */           ByteBuf frame = buffer.readSlice(length);
/* 104 */           buffer.skipBytes(delimLength);
/*     */         } else {
/* 106 */           frame = buffer.readSlice(length + delimLength);
/*     */         }
/*     */         
/* 109 */         return frame.retain();
/*     */       }
/* 111 */       int length = buffer.readableBytes();
/* 112 */       if (length > this.maxLength) {
/* 113 */         this.discardedBytes = length;
/* 114 */         buffer.readerIndex(buffer.writerIndex());
/* 115 */         this.discarding = true;
/* 116 */         if (this.failFast) {
/* 117 */           fail(ctx, "over " + this.discardedBytes);
/*     */         }
/*     */       }
/* 120 */       return null;
/*     */     }
/*     */     
/* 123 */     if (eol >= 0) {
/* 124 */       int length = this.discardedBytes + eol - buffer.readerIndex();
/* 125 */       int delimLength = buffer.getByte(eol) == 13 ? 2 : 1;
/* 126 */       buffer.readerIndex(eol + delimLength);
/* 127 */       this.discardedBytes = 0;
/* 128 */       this.discarding = false;
/* 129 */       if (!this.failFast) {
/* 130 */         fail(ctx, length);
/*     */       }
/*     */     } else {
/* 133 */       this.discardedBytes = buffer.readableBytes();
/* 134 */       buffer.readerIndex(buffer.writerIndex());
/*     */     }
/* 136 */     return null;
/*     */   }
/*     */   
/*     */   private void fail(ChannelHandlerContext ctx, int length)
/*     */   {
/* 141 */     fail(ctx, String.valueOf(length));
/*     */   }
/*     */   
/*     */   private void fail(ChannelHandlerContext ctx, String length) {
/* 145 */     ctx.fireExceptionCaught(new TooLongFrameException("frame length (" + length + ") exceeds the allowed maximum (" + this.maxLength + ')'));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int findEndOfLine(ByteBuf buffer)
/*     */   {
/* 155 */     int n = buffer.writerIndex();
/* 156 */     for (int i = buffer.readerIndex(); i < n; i++) {
/* 157 */       byte b = buffer.getByte(i);
/* 158 */       if (b == 10)
/* 159 */         return i;
/* 160 */       if ((b == 13) && (i < n - 1) && (buffer.getByte(i + 1) == 10)) {
/* 161 */         return i;
/*     */       }
/*     */     }
/* 164 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\LineBasedFrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */