/*     */ package io.netty.handler.codec.http.multipart;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.handler.codec.http.HttpHeaderValues;
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
/*     */ final class HttpPostBodyUtil
/*     */ {
/*     */   public static final int chunkSize = 8096;
/*  31 */   public static final String DEFAULT_BINARY_CONTENT_TYPE = HttpHeaderValues.APPLICATION_OCTET_STREAM.toString();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  36 */   public static final String DEFAULT_TEXT_CONTENT_TYPE = HttpHeaderValues.TEXT_PLAIN.toString();
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
/*     */   public static enum TransferEncodingMechanism
/*     */   {
/*  50 */     BIT7("7bit"), 
/*     */     
/*     */ 
/*     */ 
/*  54 */     BIT8("8bit"), 
/*     */     
/*     */ 
/*     */ 
/*  58 */     BINARY("binary");
/*     */     
/*     */     private final String value;
/*     */     
/*     */     private TransferEncodingMechanism(String value) {
/*  63 */       this.value = value;
/*     */     }
/*     */     
/*     */     private TransferEncodingMechanism() {
/*  67 */       this.value = name();
/*     */     }
/*     */     
/*     */     public String value() {
/*  71 */       return this.value;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/*  76 */       return this.value;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static class SeekAheadNoBackArrayException
/*     */     extends Exception
/*     */   {
/*     */     private static final long serialVersionUID = -630418804938699495L;
/*     */   }
/*     */   
/*     */ 
/*     */   static class SeekAheadOptimize
/*     */   {
/*     */     byte[] bytes;
/*     */     
/*     */     int readerIndex;
/*     */     
/*     */     int pos;
/*     */     
/*     */     int origPos;
/*     */     int limit;
/*     */     ByteBuf buffer;
/*     */     
/*     */     SeekAheadOptimize(ByteBuf buffer)
/*     */       throws HttpPostBodyUtil.SeekAheadNoBackArrayException
/*     */     {
/* 103 */       if (!buffer.hasArray()) {
/* 104 */         throw new HttpPostBodyUtil.SeekAheadNoBackArrayException();
/*     */       }
/* 106 */       this.buffer = buffer;
/* 107 */       this.bytes = buffer.array();
/* 108 */       this.readerIndex = buffer.readerIndex();
/* 109 */       this.origPos = (this.pos = buffer.arrayOffset() + this.readerIndex);
/* 110 */       this.limit = (buffer.arrayOffset() + buffer.writerIndex());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     void setReadPosition(int minus)
/*     */     {
/* 119 */       this.pos -= minus;
/* 120 */       this.readerIndex = getReadPosition(this.pos);
/* 121 */       this.buffer.readerIndex(this.readerIndex);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     int getReadPosition(int index)
/*     */     {
/* 130 */       return index - this.origPos + this.readerIndex;
/*     */     }
/*     */     
/*     */     void clear() {
/* 134 */       this.buffer = null;
/* 135 */       this.bytes = null;
/* 136 */       this.limit = 0;
/* 137 */       this.pos = 0;
/* 138 */       this.readerIndex = 0;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static int findNonWhitespace(String sb, int offset)
/*     */   {
/* 148 */     for (int result = offset; result < sb.length(); result++) {
/* 149 */       if (!Character.isWhitespace(sb.charAt(result))) {
/*     */         break;
/*     */       }
/*     */     }
/* 153 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static int findWhitespace(String sb, int offset)
/*     */   {
/* 162 */     for (int result = offset; result < sb.length(); result++) {
/* 163 */       if (Character.isWhitespace(sb.charAt(result))) {
/*     */         break;
/*     */       }
/*     */     }
/* 167 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static int findEndOfString(String sb)
/*     */   {
/* 176 */     for (int result = sb.length(); result > 0; result--) {
/* 177 */       if (!Character.isWhitespace(sb.charAt(result - 1))) {
/*     */         break;
/*     */       }
/*     */     }
/* 181 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\multipart\HttpPostBodyUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */