/*    */ package io.netty.handler.codec.socksx.v5;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.DecoderException;
/*    */ import io.netty.handler.codec.DecoderResult;
/*    */ import io.netty.handler.codec.ReplayingDecoder;
/*    */ import io.netty.handler.codec.socksx.SocksVersion;
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
/*    */ public class Socks5InitialResponseDecoder
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
/* 44 */   public Socks5InitialResponseDecoder() { super(State.INIT); }
/*    */   
/*    */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*    */   {
/*    */     try
/*    */     {
/* 50 */       switch ((State)state()) {
/*    */       case INIT: 
/* 52 */         byte version = in.readByte();
/* 53 */         if (version != SocksVersion.SOCKS5.byteValue()) {
/* 54 */           throw new DecoderException("unsupported version: " + version + " (expected: " + SocksVersion.SOCKS5.byteValue() + ')');
/*    */         }
/*    */         
/*    */ 
/* 58 */         Socks5AuthMethod authMethod = Socks5AuthMethod.valueOf(in.readByte());
/* 59 */         out.add(new DefaultSocks5InitialResponse(authMethod));
/* 60 */         checkpoint(State.SUCCESS);
/*    */       
/*    */       case SUCCESS: 
/* 63 */         int readableBytes = actualReadableBytes();
/* 64 */         if (readableBytes > 0) {
/* 65 */           out.add(in.readSlice(readableBytes).retain());
/*    */         }
/*    */         
/*    */         break;
/*    */       case FAILURE: 
/* 70 */         in.skipBytes(actualReadableBytes());
/*    */       }
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 75 */       fail(out, e);
/*    */     }
/*    */   }
/*    */   
/*    */   private void fail(List<Object> out, Throwable cause) {
/* 80 */     if (!(cause instanceof DecoderException)) {
/* 81 */       cause = new DecoderException(cause);
/*    */     }
/*    */     
/* 84 */     checkpoint(State.FAILURE);
/*    */     
/* 86 */     Socks5Message m = new DefaultSocks5InitialResponse(Socks5AuthMethod.UNACCEPTED);
/* 87 */     m.setDecoderResult(DecoderResult.failure(cause));
/* 88 */     out.add(m);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\Socks5InitialResponseDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */