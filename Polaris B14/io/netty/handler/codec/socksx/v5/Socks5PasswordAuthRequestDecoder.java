/*    */ package io.netty.handler.codec.socksx.v5;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.DecoderException;
/*    */ import io.netty.handler.codec.DecoderResult;
/*    */ import io.netty.handler.codec.ReplayingDecoder;
/*    */ import io.netty.util.CharsetUtil;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Socks5PasswordAuthRequestDecoder
/*    */   extends ReplayingDecoder<State>
/*    */ {
/*    */   static enum State
/*    */   {
/* 38 */     INIT, 
/* 39 */     SUCCESS, 
/* 40 */     FAILURE;
/*    */     
/*    */     private State() {} }
/*    */   
/* 44 */   public Socks5PasswordAuthRequestDecoder() { super(State.INIT); }
/*    */   
/*    */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*    */   {
/*    */     try
/*    */     {
/* 50 */       switch ((State)state()) {
/*    */       case INIT: 
/* 52 */         int startOffset = in.readerIndex();
/* 53 */         byte version = in.getByte(startOffset);
/* 54 */         if (version != 1) {
/* 55 */           throw new DecoderException("unsupported subnegotiation version: " + version + " (expected: 1)");
/*    */         }
/*    */         
/* 58 */         int usernameLength = in.getUnsignedByte(startOffset + 1);
/* 59 */         int passwordLength = in.getUnsignedByte(startOffset + 2 + usernameLength);
/* 60 */         int totalLength = usernameLength + passwordLength + 3;
/*    */         
/* 62 */         in.skipBytes(totalLength);
/* 63 */         out.add(new DefaultSocks5PasswordAuthRequest(in.toString(startOffset + 2, usernameLength, CharsetUtil.US_ASCII), in.toString(startOffset + 3 + usernameLength, passwordLength, CharsetUtil.US_ASCII)));
/*    */         
/*    */ 
/*    */ 
/* 67 */         checkpoint(State.SUCCESS);
/*    */       
/*    */       case SUCCESS: 
/* 70 */         int readableBytes = actualReadableBytes();
/* 71 */         if (readableBytes > 0) {
/* 72 */           out.add(in.readSlice(readableBytes).retain());
/*    */         }
/*    */         
/*    */         break;
/*    */       case FAILURE: 
/* 77 */         in.skipBytes(actualReadableBytes());
/*    */       }
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 82 */       fail(out, e);
/*    */     }
/*    */   }
/*    */   
/*    */   private void fail(List<Object> out, Throwable cause) {
/* 87 */     if (!(cause instanceof DecoderException)) {
/* 88 */       cause = new DecoderException(cause);
/*    */     }
/*    */     
/* 91 */     checkpoint(State.FAILURE);
/*    */     
/* 93 */     Socks5Message m = new DefaultSocks5PasswordAuthRequest("", "");
/* 94 */     m.setDecoderResult(DecoderResult.failure(cause));
/* 95 */     out.add(m);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\Socks5PasswordAuthRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */