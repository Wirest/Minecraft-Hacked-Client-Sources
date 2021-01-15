/*     */ package io.netty.handler.codec.xml;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
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
/*     */ public class XmlFrameDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private final int maxFrameLength;
/*     */   
/*     */   public XmlFrameDecoder(int maxFrameLength)
/*     */   {
/*  75 */     if (maxFrameLength < 1) {
/*  76 */       throw new IllegalArgumentException("maxFrameLength must be a positive int");
/*     */     }
/*  78 */     this.maxFrameLength = maxFrameLength;
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/*  83 */     boolean openingBracketFound = false;
/*  84 */     boolean atLeastOneXmlElementFound = false;
/*  85 */     boolean inCDATASection = false;
/*  86 */     long openBracketsCount = 0L;
/*  87 */     int length = 0;
/*  88 */     int leadingWhiteSpaceCount = 0;
/*  89 */     int bufferLength = in.writerIndex();
/*     */     
/*  91 */     if (bufferLength > this.maxFrameLength)
/*     */     {
/*  93 */       in.skipBytes(in.readableBytes());
/*  94 */       fail(bufferLength);
/*  95 */       return;
/*     */     }
/*     */     
/*  98 */     for (int i = in.readerIndex(); i < bufferLength; i++) {
/*  99 */       byte readByte = in.getByte(i);
/* 100 */       if ((!openingBracketFound) && (Character.isWhitespace(readByte)))
/*     */       {
/* 102 */         leadingWhiteSpaceCount++;
/* 103 */       } else { if ((!openingBracketFound) && (readByte != 60))
/*     */         {
/* 105 */           fail(ctx);
/* 106 */           in.skipBytes(in.readableBytes());
/* 107 */           return; }
/* 108 */         if ((!inCDATASection) && (readByte == 60)) {
/* 109 */           openingBracketFound = true;
/*     */           
/* 111 */           if (i < bufferLength - 1) {
/* 112 */             byte peekAheadByte = in.getByte(i + 1);
/* 113 */             if (peekAheadByte == 47)
/*     */             {
/* 115 */               openBracketsCount -= 1L;
/* 116 */             } else if (isValidStartCharForXmlElement(peekAheadByte)) {
/* 117 */               atLeastOneXmlElementFound = true;
/*     */               
/*     */ 
/* 120 */               openBracketsCount += 1L;
/* 121 */             } else if (peekAheadByte == 33) {
/* 122 */               if (isCommentBlockStart(in, i))
/*     */               {
/* 124 */                 openBracketsCount += 1L;
/* 125 */               } else if (isCDATABlockStart(in, i))
/*     */               {
/* 127 */                 openBracketsCount += 1L;
/* 128 */                 inCDATASection = true;
/*     */               }
/* 130 */             } else if (peekAheadByte == 63)
/*     */             {
/* 132 */               openBracketsCount += 1L;
/*     */             }
/*     */           }
/* 135 */         } else if ((!inCDATASection) && (readByte == 47)) {
/* 136 */           if ((i < bufferLength - 1) && (in.getByte(i + 1) == 62))
/*     */           {
/* 138 */             openBracketsCount -= 1L;
/*     */           }
/* 140 */         } else if (readByte == 62) {
/* 141 */           length = i + 1;
/*     */           
/* 143 */           if (i - 1 > -1) {
/* 144 */             byte peekBehindByte = in.getByte(i - 1);
/*     */             
/* 146 */             if (!inCDATASection) {
/* 147 */               if (peekBehindByte == 63)
/*     */               {
/* 149 */                 openBracketsCount -= 1L;
/* 150 */               } else if ((peekBehindByte == 45) && (i - 2 > -1) && (in.getByte(i - 2) == 45))
/*     */               {
/* 152 */                 openBracketsCount -= 1L;
/*     */               }
/* 154 */             } else if ((peekBehindByte == 93) && (i - 2 > -1) && (in.getByte(i - 2) == 93))
/*     */             {
/* 156 */               openBracketsCount -= 1L;
/* 157 */               inCDATASection = false;
/*     */             }
/*     */           }
/*     */           
/* 161 */           if ((atLeastOneXmlElementFound) && (openBracketsCount == 0L)) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 168 */     int readerIndex = in.readerIndex();
/*     */     
/* 170 */     if ((openBracketsCount == 0L) && (length > 0)) {
/* 171 */       if (length >= bufferLength) {
/* 172 */         length = in.readableBytes();
/*     */       }
/* 174 */       ByteBuf frame = extractFrame(in, readerIndex + leadingWhiteSpaceCount, length - leadingWhiteSpaceCount);
/*     */       
/* 176 */       in.skipBytes(length);
/* 177 */       out.add(frame);
/*     */     }
/*     */   }
/*     */   
/*     */   private void fail(long frameLength) {
/* 182 */     if (frameLength > 0L) {
/* 183 */       throw new TooLongFrameException("frame length exceeds " + this.maxFrameLength + ": " + frameLength + " - discarded");
/*     */     }
/*     */     
/* 186 */     throw new TooLongFrameException("frame length exceeds " + this.maxFrameLength + " - discarding");
/*     */   }
/*     */   
/*     */ 
/*     */   private static void fail(ChannelHandlerContext ctx)
/*     */   {
/* 192 */     ctx.fireExceptionCaught(new CorruptedFrameException("frame contains content before the xml starts"));
/*     */   }
/*     */   
/*     */   private static ByteBuf extractFrame(ByteBuf buffer, int index, int length) {
/* 196 */     return buffer.copy(index, length);
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
/*     */   private static boolean isValidStartCharForXmlElement(byte b)
/*     */   {
/* 211 */     return ((b >= 97) && (b <= 122)) || ((b >= 65) && (b <= 90)) || (b == 58) || (b == 95);
/*     */   }
/*     */   
/*     */   private static boolean isCommentBlockStart(ByteBuf in, int i) {
/* 215 */     return (i < in.writerIndex() - 3) && (in.getByte(i + 2) == 45) && (in.getByte(i + 3) == 45);
/*     */   }
/*     */   
/*     */ 
/*     */   private static boolean isCDATABlockStart(ByteBuf in, int i)
/*     */   {
/* 221 */     return (i < in.writerIndex() - 8) && (in.getByte(i + 2) == 91) && (in.getByte(i + 3) == 67) && (in.getByte(i + 4) == 68) && (in.getByte(i + 5) == 65) && (in.getByte(i + 6) == 84) && (in.getByte(i + 7) == 65) && (in.getByte(i + 8) == 91);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\xml\XmlFrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */