/*    */ package io.netty.handler.codec.http;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.handler.codec.AsciiString;
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
/*    */ public class HttpResponseEncoder
/*    */   extends HttpObjectEncoder<HttpResponse>
/*    */ {
/* 28 */   private static final byte[] CRLF = { 13, 10 };
/*    */   
/*    */   public boolean acceptOutboundMessage(Object msg) throws Exception
/*    */   {
/* 32 */     return (super.acceptOutboundMessage(msg)) && (!(msg instanceof HttpRequest));
/*    */   }
/*    */   
/*    */   protected void encodeInitialLine(ByteBuf buf, HttpResponse response) throws Exception
/*    */   {
/* 37 */     AsciiString version = response.protocolVersion().text();
/* 38 */     buf.writeBytes(version.array(), version.arrayOffset(), version.length());
/* 39 */     buf.writeByte(32);
/*    */     
/* 41 */     AsciiString code = response.status().codeAsText();
/* 42 */     buf.writeBytes(code.array(), code.arrayOffset(), code.length());
/* 43 */     buf.writeByte(32);
/*    */     
/* 45 */     AsciiString reasonPhrase = response.status().reasonPhrase();
/* 46 */     buf.writeBytes(reasonPhrase.array(), reasonPhrase.arrayOffset(), reasonPhrase.length());
/* 47 */     buf.writeBytes(CRLF);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpResponseEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */