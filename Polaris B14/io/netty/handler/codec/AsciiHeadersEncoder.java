/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Map.Entry;
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
/*     */ public final class AsciiHeadersEncoder
/*     */   implements TextHeaders.EntryVisitor
/*     */ {
/*     */   private final ByteBuf buf;
/*     */   private final SeparatorType separatorType;
/*     */   private final NewlineType newlineType;
/*     */   
/*     */   public static enum SeparatorType
/*     */   {
/*  34 */     COLON, 
/*     */     
/*     */ 
/*     */ 
/*  38 */     COLON_SPACE;
/*     */     
/*     */ 
/*     */ 
/*     */     private SeparatorType() {}
/*     */   }
/*     */   
/*     */ 
/*     */   public static enum NewlineType
/*     */   {
/*  48 */     LF, 
/*     */     
/*     */ 
/*     */ 
/*  52 */     CRLF;
/*     */     
/*     */ 
/*     */     private NewlineType() {}
/*     */   }
/*     */   
/*     */   public AsciiHeadersEncoder(ByteBuf buf)
/*     */   {
/*  60 */     this(buf, SeparatorType.COLON_SPACE, NewlineType.CRLF);
/*     */   }
/*     */   
/*     */   public AsciiHeadersEncoder(ByteBuf buf, SeparatorType separatorType, NewlineType newlineType) {
/*  64 */     if (buf == null) {
/*  65 */       throw new NullPointerException("buf");
/*     */     }
/*  67 */     if (separatorType == null) {
/*  68 */       throw new NullPointerException("separatorType");
/*     */     }
/*  70 */     if (newlineType == null) {
/*  71 */       throw new NullPointerException("newlineType");
/*     */     }
/*     */     
/*  74 */     this.buf = buf;
/*  75 */     this.separatorType = separatorType;
/*  76 */     this.newlineType = newlineType;
/*     */   }
/*     */   
/*     */   public boolean visit(Map.Entry<CharSequence, CharSequence> entry) throws Exception
/*     */   {
/*  81 */     CharSequence name = (CharSequence)entry.getKey();
/*  82 */     CharSequence value = (CharSequence)entry.getValue();
/*  83 */     ByteBuf buf = this.buf;
/*  84 */     int nameLen = name.length();
/*  85 */     int valueLen = value.length();
/*  86 */     int entryLen = nameLen + valueLen + 4;
/*  87 */     int offset = buf.writerIndex();
/*  88 */     buf.ensureWritable(entryLen);
/*  89 */     writeAscii(buf, offset, name, nameLen);
/*  90 */     offset += nameLen;
/*     */     
/*  92 */     switch (this.separatorType) {
/*     */     case COLON: 
/*  94 */       buf.setByte(offset++, 58);
/*  95 */       break;
/*     */     case COLON_SPACE: 
/*  97 */       buf.setByte(offset++, 58);
/*  98 */       buf.setByte(offset++, 32);
/*  99 */       break;
/*     */     default: 
/* 101 */       throw new Error();
/*     */     }
/*     */     
/* 104 */     writeAscii(buf, offset, value, valueLen);
/* 105 */     offset += valueLen;
/*     */     
/* 107 */     switch (this.newlineType) {
/*     */     case LF: 
/* 109 */       buf.setByte(offset++, 10);
/* 110 */       break;
/*     */     case CRLF: 
/* 112 */       buf.setByte(offset++, 13);
/* 113 */       buf.setByte(offset++, 10);
/* 114 */       break;
/*     */     default: 
/* 116 */       throw new Error();
/*     */     }
/*     */     
/* 119 */     buf.writerIndex(offset);
/* 120 */     return true;
/*     */   }
/*     */   
/*     */   private static void writeAscii(ByteBuf buf, int offset, CharSequence value, int valueLen) {
/* 124 */     if ((value instanceof AsciiString)) {
/* 125 */       writeAsciiString(buf, offset, (AsciiString)value, valueLen);
/*     */     } else {
/* 127 */       writeCharSequence(buf, offset, value, valueLen);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void writeAsciiString(ByteBuf buf, int offset, AsciiString value, int valueLen) {
/* 132 */     value.copy(0, buf, offset, valueLen);
/*     */   }
/*     */   
/*     */   private static void writeCharSequence(ByteBuf buf, int offset, CharSequence value, int valueLen) {
/* 136 */     for (int i = 0; i < valueLen; i++) {
/* 137 */       buf.setByte(offset++, c2b(value.charAt(i)));
/*     */     }
/*     */   }
/*     */   
/*     */   private static int c2b(char ch) {
/* 142 */     return ch < 'Ā' ? (byte)ch : 63;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\AsciiHeadersEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */