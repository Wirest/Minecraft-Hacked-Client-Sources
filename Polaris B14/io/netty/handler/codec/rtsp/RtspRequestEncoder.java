/*    */ package io.netty.handler.codec.rtsp;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.handler.codec.AsciiString;
/*    */ import io.netty.handler.codec.http.FullHttpRequest;
/*    */ import io.netty.handler.codec.http.HttpMethod;
/*    */ import io.netty.handler.codec.http.HttpRequest;
/*    */ import io.netty.handler.codec.http.HttpVersion;
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
/*    */ public class RtspRequestEncoder
/*    */   extends RtspObjectEncoder<HttpRequest>
/*    */ {
/* 32 */   private static final byte[] CRLF = { 13, 10 };
/*    */   
/*    */   public boolean acceptOutboundMessage(Object msg) throws Exception
/*    */   {
/* 36 */     return msg instanceof FullHttpRequest;
/*    */   }
/*    */   
/*    */   protected void encodeInitialLine(ByteBuf buf, HttpRequest request) throws Exception
/*    */   {
/* 41 */     AsciiString method = request.method().name();
/* 42 */     buf.writeBytes(method.array(), method.arrayOffset(), method.length());
/* 43 */     buf.writeByte(32);
/*    */     
/* 45 */     buf.writeBytes(request.uri().getBytes(CharsetUtil.UTF_8));
/* 46 */     buf.writeByte(32);
/*    */     
/* 48 */     AsciiString version = request.protocolVersion().text();
/* 49 */     buf.writeBytes(version.array(), version.arrayOffset(), version.length());
/* 50 */     buf.writeBytes(CRLF);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\rtsp\RtspRequestEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */