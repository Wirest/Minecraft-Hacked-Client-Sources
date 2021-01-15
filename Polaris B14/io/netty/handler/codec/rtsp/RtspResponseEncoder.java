/*    */ package io.netty.handler.codec.rtsp;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.handler.codec.AsciiString;
/*    */ import io.netty.handler.codec.http.FullHttpResponse;
/*    */ import io.netty.handler.codec.http.HttpResponse;
/*    */ import io.netty.handler.codec.http.HttpResponseStatus;
/*    */ import io.netty.handler.codec.http.HttpVersion;
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
/*    */ public class RtspResponseEncoder
/*    */   extends RtspObjectEncoder<HttpResponse>
/*    */ {
/* 30 */   private static final byte[] CRLF = { 13, 10 };
/*    */   
/*    */   public boolean acceptOutboundMessage(Object msg) throws Exception
/*    */   {
/* 34 */     return msg instanceof FullHttpResponse;
/*    */   }
/*    */   
/*    */   protected void encodeInitialLine(ByteBuf buf, HttpResponse response) throws Exception
/*    */   {
/* 39 */     AsciiString version = response.protocolVersion().text();
/* 40 */     buf.writeBytes(version.array(), version.arrayOffset(), version.length());
/* 41 */     buf.writeByte(32);
/*    */     
/* 43 */     AsciiString code = response.status().codeAsText();
/* 44 */     buf.writeBytes(code.array(), code.arrayOffset(), code.length());
/* 45 */     buf.writeByte(32);
/*    */     
/* 47 */     AsciiString reasonPhrase = response.status().reasonPhrase();
/* 48 */     buf.writeBytes(reasonPhrase.array(), reasonPhrase.arrayOffset(), reasonPhrase.length());
/* 49 */     buf.writeBytes(CRLF);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\rtsp\RtspResponseEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */