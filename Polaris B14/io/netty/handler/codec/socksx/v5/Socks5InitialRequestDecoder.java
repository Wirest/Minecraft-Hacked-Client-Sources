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
/*    */ public class Socks5InitialRequestDecoder
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
/* 44 */   public Socks5InitialRequestDecoder() { super(State.INIT); }
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
/* 58 */         int authMethodCnt = in.readUnsignedByte();
/* 59 */         if (actualReadableBytes() >= authMethodCnt)
/*    */         {
/*    */ 
/*    */ 
/* 63 */           Socks5AuthMethod[] authMethods = new Socks5AuthMethod[authMethodCnt];
/* 64 */           for (int i = 0; i < authMethodCnt; i++) {
/* 65 */             authMethods[i] = Socks5AuthMethod.valueOf(in.readByte());
/*    */           }
/*    */           
/* 68 */           out.add(new DefaultSocks5InitialRequest(authMethods));
/* 69 */           checkpoint(State.SUCCESS);
/*    */         }
/*    */         break;
/* 72 */       case SUCCESS:  int readableBytes = actualReadableBytes();
/* 73 */         if (readableBytes > 0) {
/* 74 */           out.add(in.readSlice(readableBytes).retain());
/*    */         }
/*    */         
/*    */         break;
/*    */       case FAILURE: 
/* 79 */         in.skipBytes(actualReadableBytes());
/*    */       }
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 84 */       fail(out, e);
/*    */     }
/*    */   }
/*    */   
/*    */   private void fail(List<Object> out, Throwable cause) {
/* 89 */     if (!(cause instanceof DecoderException)) {
/* 90 */       cause = new DecoderException(cause);
/*    */     }
/*    */     
/* 93 */     checkpoint(State.FAILURE);
/*    */     
/* 95 */     Socks5Message m = new DefaultSocks5InitialRequest(new Socks5AuthMethod[] { Socks5AuthMethod.NO_AUTH });
/* 96 */     m.setDecoderResult(DecoderResult.failure(cause));
/* 97 */     out.add(m);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\Socks5InitialRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */