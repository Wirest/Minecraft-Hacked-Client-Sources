/*    */ package io.netty.handler.codec.http.websocketx;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufAllocator;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import io.netty.channel.ChannelHandler.Sharable;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToMessageEncoder;
/*    */ import java.util.List;
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
/*    */ @ChannelHandler.Sharable
/*    */ public class WebSocket00FrameEncoder
/*    */   extends MessageToMessageEncoder<WebSocketFrame>
/*    */   implements WebSocketFrameEncoder
/*    */ {
/* 34 */   private static final ByteBuf _0X00 = Unpooled.unreleasableBuffer(Unpooled.directBuffer(1, 1).writeByte(0));
/*    */   
/* 36 */   private static final ByteBuf _0XFF = Unpooled.unreleasableBuffer(Unpooled.directBuffer(1, 1).writeByte(-1));
/*    */   
/* 38 */   private static final ByteBuf _0XFF_0X00 = Unpooled.unreleasableBuffer(Unpooled.directBuffer(2, 2).writeByte(-1).writeByte(0));
/*    */   
/*    */   protected void encode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out)
/*    */     throws Exception
/*    */   {
/* 43 */     if ((msg instanceof TextWebSocketFrame))
/*    */     {
/* 45 */       ByteBuf data = msg.content();
/*    */       
/* 47 */       out.add(_0X00.duplicate());
/* 48 */       out.add(data.retain());
/* 49 */       out.add(_0XFF.duplicate());
/* 50 */     } else if ((msg instanceof CloseWebSocketFrame))
/*    */     {
/*    */ 
/* 53 */       out.add(_0XFF_0X00.duplicate());
/*    */     }
/*    */     else {
/* 56 */       ByteBuf data = msg.content();
/* 57 */       int dataLen = data.readableBytes();
/*    */       
/* 59 */       ByteBuf buf = ctx.alloc().buffer(5);
/* 60 */       boolean release = true;
/*    */       try
/*    */       {
/* 63 */         buf.writeByte(-128);
/*    */         
/*    */ 
/* 66 */         int b1 = dataLen >>> 28 & 0x7F;
/* 67 */         int b2 = dataLen >>> 14 & 0x7F;
/* 68 */         int b3 = dataLen >>> 7 & 0x7F;
/* 69 */         int b4 = dataLen & 0x7F;
/* 70 */         if (b1 == 0) {
/* 71 */           if (b2 == 0) {
/* 72 */             if (b3 == 0) {
/* 73 */               buf.writeByte(b4);
/*    */             } else {
/* 75 */               buf.writeByte(b3 | 0x80);
/* 76 */               buf.writeByte(b4);
/*    */             }
/*    */           } else {
/* 79 */             buf.writeByte(b2 | 0x80);
/* 80 */             buf.writeByte(b3 | 0x80);
/* 81 */             buf.writeByte(b4);
/*    */           }
/*    */         } else {
/* 84 */           buf.writeByte(b1 | 0x80);
/* 85 */           buf.writeByte(b2 | 0x80);
/* 86 */           buf.writeByte(b3 | 0x80);
/* 87 */           buf.writeByte(b4);
/*    */         }
/*    */         
/*    */ 
/* 91 */         out.add(buf);
/* 92 */         out.add(data.retain());
/* 93 */         release = false;
/*    */       } finally {
/* 95 */         if (release) {
/* 96 */           buf.release();
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\WebSocket00FrameEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */