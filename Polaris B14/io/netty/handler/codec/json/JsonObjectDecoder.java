/*     */ package io.netty.handler.codec.json;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import io.netty.handler.codec.CorruptedFrameException;
/*     */ import io.netty.handler.codec.TooLongFrameException;
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
/*     */ public class JsonObjectDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private static final int ST_CORRUPTED = -1;
/*     */   private static final int ST_INIT = 0;
/*     */   private static final int ST_DECODING_NORMAL = 1;
/*     */   private static final int ST_DECODING_ARRAY_STREAM = 2;
/*     */   private int openBraces;
/*     */   private int idx;
/*     */   private int state;
/*     */   private boolean insideString;
/*     */   private final int maxObjectLength;
/*     */   private final boolean streamArrayElements;
/*     */   
/*     */   public JsonObjectDecoder()
/*     */   {
/*  56 */     this(1048576);
/*     */   }
/*     */   
/*     */   public JsonObjectDecoder(int maxObjectLength) {
/*  60 */     this(maxObjectLength, false);
/*     */   }
/*     */   
/*     */   public JsonObjectDecoder(boolean streamArrayElements) {
/*  64 */     this(1048576, streamArrayElements);
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
/*     */   public JsonObjectDecoder(int maxObjectLength, boolean streamArrayElements)
/*     */   {
/*  77 */     if (maxObjectLength < 1) {
/*  78 */       throw new IllegalArgumentException("maxObjectLength must be a positive int");
/*     */     }
/*  80 */     this.maxObjectLength = maxObjectLength;
/*  81 */     this.streamArrayElements = streamArrayElements;
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/*  86 */     if (this.state == -1) {
/*  87 */       in.skipBytes(in.readableBytes());
/*  88 */       return;
/*     */     }
/*     */     
/*     */ 
/*  92 */     int idx = this.idx;
/*  93 */     int wrtIdx = in.writerIndex();
/*     */     
/*  95 */     if (wrtIdx > this.maxObjectLength)
/*     */     {
/*  97 */       in.skipBytes(in.readableBytes());
/*  98 */       reset();
/*  99 */       throw new TooLongFrameException("object length exceeds " + this.maxObjectLength + ": " + wrtIdx + " bytes discarded");
/*     */     }
/* 103 */     for (; 
/*     */         
/* 103 */         idx < wrtIdx; idx++) {
/* 104 */       byte c = in.getByte(idx);
/* 105 */       if (this.state == 1) {
/* 106 */         decodeByte(c, in, idx);
/*     */         
/*     */ 
/*     */ 
/* 110 */         if (this.openBraces == 0) {
/* 111 */           ByteBuf json = extractObject(ctx, in, in.readerIndex(), idx + 1 - in.readerIndex());
/* 112 */           if (json != null) {
/* 113 */             out.add(json);
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 118 */           in.readerIndex(idx + 1);
/*     */           
/*     */ 
/* 121 */           reset();
/*     */         }
/* 123 */       } else if (this.state == 2) {
/* 124 */         decodeByte(c, in, idx);
/*     */         
/* 126 */         if ((!this.insideString) && (((this.openBraces == 1) && (c == 44)) || ((this.openBraces == 0) && (c == 93))))
/*     */         {
/*     */ 
/* 129 */           for (int i = in.readerIndex(); Character.isWhitespace(in.getByte(i)); i++) {
/* 130 */             in.skipBytes(1);
/*     */           }
/*     */           
/*     */ 
/* 134 */           int idxNoSpaces = idx - 1;
/* 135 */           while ((idxNoSpaces >= in.readerIndex()) && (Character.isWhitespace(in.getByte(idxNoSpaces)))) {
/* 136 */             idxNoSpaces--;
/*     */           }
/*     */           
/* 139 */           ByteBuf json = extractObject(ctx, in, in.readerIndex(), idxNoSpaces + 1 - in.readerIndex());
/* 140 */           if (json != null) {
/* 141 */             out.add(json);
/*     */           }
/*     */           
/* 144 */           in.readerIndex(idx + 1);
/*     */           
/* 146 */           if (c == 93) {
/* 147 */             reset();
/*     */           }
/*     */         }
/*     */       }
/* 151 */       else if ((c == 123) || (c == 91)) {
/* 152 */         initDecoding(c);
/*     */         
/* 154 */         if (this.state == 2)
/*     */         {
/* 156 */           in.skipBytes(1);
/*     */         }
/*     */       }
/* 159 */       else if (Character.isWhitespace(c)) {
/* 160 */         in.skipBytes(1);
/*     */       } else {
/* 162 */         this.state = -1;
/* 163 */         throw new CorruptedFrameException("invalid JSON received at byte position " + idx + ": " + ByteBufUtil.hexDump(in));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 168 */     if (in.readableBytes() == 0) {
/* 169 */       this.idx = 0;
/*     */     } else {
/* 171 */       this.idx = idx;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ByteBuf extractObject(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length)
/*     */   {
/* 180 */     return buffer.slice(index, length).retain();
/*     */   }
/*     */   
/*     */   private void decodeByte(byte c, ByteBuf in, int idx) {
/* 184 */     if (((c == 123) || (c == 91)) && (!this.insideString)) {
/* 185 */       this.openBraces += 1;
/* 186 */     } else if (((c == 125) || (c == 93)) && (!this.insideString)) {
/* 187 */       this.openBraces -= 1;
/* 188 */     } else if (c == 34)
/*     */     {
/*     */ 
/* 191 */       if (!this.insideString) {
/* 192 */         this.insideString = true;
/*     */       }
/* 194 */       else if (in.getByte(idx - 1) != 92) {
/* 195 */         this.insideString = false;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void initDecoding(byte openingBrace) {
/* 201 */     this.openBraces = 1;
/* 202 */     if ((openingBrace == 91) && (this.streamArrayElements)) {
/* 203 */       this.state = 2;
/*     */     } else {
/* 205 */       this.state = 1;
/*     */     }
/*     */   }
/*     */   
/*     */   private void reset() {
/* 210 */     this.insideString = false;
/* 211 */     this.state = 0;
/* 212 */     this.openBraces = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\json\JsonObjectDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */