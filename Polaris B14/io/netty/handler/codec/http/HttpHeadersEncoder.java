/*    */ package io.netty.handler.codec.http;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.handler.codec.AsciiString;
/*    */ import io.netty.handler.codec.TextHeaders.EntryVisitor;
/*    */ import java.util.Map.Entry;
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
/*    */ final class HttpHeadersEncoder
/*    */   implements TextHeaders.EntryVisitor
/*    */ {
/*    */   private final ByteBuf buf;
/*    */   
/*    */   HttpHeadersEncoder(ByteBuf buf)
/*    */   {
/* 30 */     this.buf = buf;
/*    */   }
/*    */   
/*    */   public boolean visit(Map.Entry<CharSequence, CharSequence> entry) throws Exception
/*    */   {
/* 35 */     CharSequence name = (CharSequence)entry.getKey();
/* 36 */     CharSequence value = (CharSequence)entry.getValue();
/* 37 */     ByteBuf buf = this.buf;
/* 38 */     int nameLen = name.length();
/* 39 */     int valueLen = value.length();
/* 40 */     int entryLen = nameLen + valueLen + 4;
/* 41 */     int offset = buf.writerIndex();
/* 42 */     buf.ensureWritable(entryLen);
/* 43 */     writeAscii(buf, offset, name, nameLen);
/* 44 */     offset += nameLen;
/* 45 */     buf.setByte(offset++, 58);
/* 46 */     buf.setByte(offset++, 32);
/* 47 */     writeAscii(buf, offset, value, valueLen);
/* 48 */     offset += valueLen;
/* 49 */     buf.setByte(offset++, 13);
/* 50 */     buf.setByte(offset++, 10);
/* 51 */     buf.writerIndex(offset);
/* 52 */     return true;
/*    */   }
/*    */   
/*    */   private static void writeAscii(ByteBuf buf, int offset, CharSequence value, int valueLen) {
/* 56 */     if ((value instanceof AsciiString)) {
/* 57 */       writeAsciiString(buf, offset, (AsciiString)value, valueLen);
/*    */     } else {
/* 59 */       writeCharSequence(buf, offset, value, valueLen);
/*    */     }
/*    */   }
/*    */   
/*    */   private static void writeAsciiString(ByteBuf buf, int offset, AsciiString value, int valueLen) {
/* 64 */     value.copy(0, buf, offset, valueLen);
/*    */   }
/*    */   
/*    */   private static void writeCharSequence(ByteBuf buf, int offset, CharSequence value, int valueLen) {
/* 68 */     for (int i = 0; i < valueLen; i++) {
/* 69 */       buf.setByte(offset++, c2b(value.charAt(i)));
/*    */     }
/*    */   }
/*    */   
/*    */   private static int c2b(char ch) {
/* 74 */     return ch < 'Ā' ? (byte)ch : 63;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpHeadersEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */