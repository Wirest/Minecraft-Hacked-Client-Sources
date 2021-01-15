/*    */ package io.netty.handler.codec.http.websocketx;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.ChannelFuture;
/*    */ import io.netty.channel.ChannelFutureListener;
/*    */ import io.netty.channel.ChannelHandlerAdapter;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.CorruptedFrameException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Utf8FrameValidator
/*    */   extends ChannelHandlerAdapter
/*    */ {
/*    */   private int fragmentedFramesCount;
/*    */   private Utf8Validator utf8Validator;
/*    */   
/*    */   public void channelRead(ChannelHandlerContext ctx, Object msg)
/*    */     throws Exception
/*    */   {
/* 44 */     if ((msg instanceof WebSocketFrame)) {
/* 45 */       WebSocketFrame frame = (WebSocketFrame)msg;
/*    */       
/*    */ 
/*    */ 
/* 49 */       if (((WebSocketFrame)msg).isFinalFragment())
/*    */       {
/*    */ 
/* 52 */         if (!(frame instanceof PingWebSocketFrame)) {
/* 53 */           this.fragmentedFramesCount = 0;
/*    */           
/*    */ 
/* 56 */           if (((frame instanceof TextWebSocketFrame)) || ((this.utf8Validator != null) && (this.utf8Validator.isChecking())))
/*    */           {
/*    */ 
/* 59 */             checkUTF8String(ctx, frame.content());
/*    */             
/*    */ 
/*    */ 
/* 63 */             this.utf8Validator.finish();
/*    */           }
/*    */         }
/*    */       }
/*    */       else
/*    */       {
/* 69 */         if (this.fragmentedFramesCount == 0)
/*    */         {
/* 71 */           if ((frame instanceof TextWebSocketFrame)) {
/* 72 */             checkUTF8String(ctx, frame.content());
/*    */           }
/*    */           
/*    */         }
/* 76 */         else if ((this.utf8Validator != null) && (this.utf8Validator.isChecking())) {
/* 77 */           checkUTF8String(ctx, frame.content());
/*    */         }
/*    */         
/*    */ 
/*    */ 
/* 82 */         this.fragmentedFramesCount += 1;
/*    */       }
/*    */     }
/*    */     
/* 86 */     super.channelRead(ctx, msg);
/*    */   }
/*    */   
/*    */   private void checkUTF8String(ChannelHandlerContext ctx, ByteBuf buffer) {
/*    */     try {
/* 91 */       if (this.utf8Validator == null) {
/* 92 */         this.utf8Validator = new Utf8Validator();
/*    */       }
/* 94 */       this.utf8Validator.check(buffer);
/*    */     } catch (CorruptedFrameException ex) {
/* 96 */       if (ctx.channel().isActive()) {
/* 97 */         ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\Utf8FrameValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */