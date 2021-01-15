/*    */ package io.netty.handler.codec.socksx.v4;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.DecoderException;
/*    */ import io.netty.handler.codec.DecoderResult;
/*    */ import io.netty.handler.codec.ReplayingDecoder;
/*    */ import io.netty.util.NetUtil;
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
/*    */ public class Socks4ClientDecoder
/*    */   extends ReplayingDecoder<State>
/*    */ {
/*    */   static enum State
/*    */   {
/* 37 */     START, 
/* 38 */     SUCCESS, 
/* 39 */     FAILURE;
/*    */     
/*    */     private State() {} }
/*    */   
/* 43 */   public Socks4ClientDecoder() { super(State.START);
/* 44 */     setSingleDecode(true);
/*    */   }
/*    */   
/*    */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*    */   {
/*    */     try {
/* 50 */       switch ((State)state()) {
/*    */       case START: 
/* 52 */         int version = in.readUnsignedByte();
/* 53 */         if (version != 0) {
/* 54 */           throw new DecoderException("unsupported reply version: " + version + " (expected: 0)");
/*    */         }
/*    */         
/* 57 */         Socks4CommandStatus status = Socks4CommandStatus.valueOf(in.readByte());
/* 58 */         int dstPort = in.readUnsignedShort();
/* 59 */         String dstAddr = NetUtil.intToIpAddress(in.readInt());
/*    */         
/* 61 */         out.add(new DefaultSocks4CommandResponse(status, dstAddr, dstPort));
/* 62 */         checkpoint(State.SUCCESS);
/*    */       
/*    */       case SUCCESS: 
/* 65 */         int readableBytes = actualReadableBytes();
/* 66 */         if (readableBytes > 0) {
/* 67 */           out.add(in.readSlice(readableBytes).retain());
/*    */         }
/*    */         
/*    */         break;
/*    */       case FAILURE: 
/* 72 */         in.skipBytes(actualReadableBytes());
/*    */       }
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 77 */       fail(out, e);
/*    */     }
/*    */   }
/*    */   
/*    */   private void fail(List<Object> out, Throwable cause) {
/* 82 */     if (!(cause instanceof DecoderException)) {
/* 83 */       cause = new DecoderException(cause);
/*    */     }
/*    */     
/* 86 */     Socks4CommandResponse m = new DefaultSocks4CommandResponse(Socks4CommandStatus.REJECTED_OR_FAILED);
/* 87 */     m.setDecoderResult(DecoderResult.failure(cause));
/* 88 */     out.add(m);
/*    */     
/* 90 */     checkpoint(State.FAILURE);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v4\Socks4ClientDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */