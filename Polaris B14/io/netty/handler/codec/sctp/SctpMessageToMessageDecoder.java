/*    */ package io.netty.handler.codec.sctp;
/*    */ 
/*    */ import io.netty.channel.sctp.SctpMessage;
/*    */ import io.netty.handler.codec.CodecException;
/*    */ import io.netty.handler.codec.MessageToMessageDecoder;
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
/*    */ public abstract class SctpMessageToMessageDecoder
/*    */   extends MessageToMessageDecoder<SctpMessage>
/*    */ {
/*    */   public boolean acceptInboundMessage(Object msg)
/*    */     throws Exception
/*    */   {
/* 27 */     if ((msg instanceof SctpMessage)) {
/* 28 */       SctpMessage sctpMsg = (SctpMessage)msg;
/* 29 */       if (sctpMsg.isComplete()) {
/* 30 */         return true;
/*    */       }
/*    */       
/* 33 */       throw new CodecException(String.format("Received SctpMessage is not complete, please add %s in the pipeline before this handler", new Object[] { SctpMessageCompletionHandler.class.getSimpleName() }));
/*    */     }
/*    */     
/* 36 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\sctp\SctpMessageToMessageDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */