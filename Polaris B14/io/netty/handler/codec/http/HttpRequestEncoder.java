/*    */ package io.netty.handler.codec.http;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.handler.codec.AsciiString;
/*    */ import io.netty.util.CharsetUtil;
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
/*    */ 
/*    */ 
/*    */ public class HttpRequestEncoder
/*    */   extends HttpObjectEncoder<HttpRequest>
/*    */ {
/*    */   private static final char SLASH = '/';
/*    */   private static final char QUESTION_MARK = '?';
/* 31 */   private static final byte[] CRLF = { 13, 10 };
/*    */   
/*    */   public boolean acceptOutboundMessage(Object msg) throws Exception
/*    */   {
/* 35 */     return (super.acceptOutboundMessage(msg)) && (!(msg instanceof HttpResponse));
/*    */   }
/*    */   
/*    */   protected void encodeInitialLine(ByteBuf buf, HttpRequest request) throws Exception
/*    */   {
/* 40 */     AsciiString method = request.method().name();
/* 41 */     buf.writeBytes(method.array(), method.arrayOffset(), method.length());
/* 42 */     buf.writeByte(32);
/*    */     
/*    */ 
/*    */ 
/* 46 */     String uri = request.uri();
/*    */     
/* 48 */     if (uri.length() == 0) {
/* 49 */       uri = uri + '/';
/*    */     } else {
/* 51 */       int start = uri.indexOf("://");
/* 52 */       if ((start != -1) && (uri.charAt(0) != '/')) {
/* 53 */         int startIndex = start + 3;
/*    */         
/*    */ 
/* 56 */         int index = uri.indexOf('?', startIndex);
/* 57 */         if (index == -1) {
/* 58 */           if (uri.lastIndexOf('/') <= startIndex) {
/* 59 */             uri = uri + '/';
/*    */           }
/*    */         }
/* 62 */         else if (uri.lastIndexOf('/', index) <= startIndex) {
/* 63 */           int len = uri.length();
/* 64 */           StringBuilder sb = new StringBuilder(len + 1);
/* 65 */           sb.append(uri, 0, index).append('/').append(uri, index, len);
/*    */           
/*    */ 
/* 68 */           uri = sb.toString();
/*    */         }
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 74 */     buf.writeBytes(uri.getBytes(CharsetUtil.UTF_8));
/* 75 */     buf.writeByte(32);
/*    */     
/* 77 */     AsciiString version = request.protocolVersion().text();
/* 78 */     buf.writeBytes(version.array(), version.arrayOffset(), version.length());
/* 79 */     buf.writeBytes(CRLF);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpRequestEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */