/*    */ package io.netty.handler.codec.sctp;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.sctp.SctpMessage;
/*    */ import io.netty.handler.codec.CodecException;
/*    */ import io.netty.handler.codec.MessageToMessageDecoder;
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
/*    */ public class SctpInboundByteStreamHandler
/*    */   extends MessageToMessageDecoder<SctpMessage>
/*    */ {
/*    */   private final int protocolIdentifier;
/*    */   private final int streamIdentifier;
/*    */   
/*    */   public SctpInboundByteStreamHandler(int protocolIdentifier, int streamIdentifier)
/*    */   {
/* 40 */     this.protocolIdentifier = protocolIdentifier;
/* 41 */     this.streamIdentifier = streamIdentifier;
/*    */   }
/*    */   
/*    */   public final boolean acceptInboundMessage(Object msg) throws Exception
/*    */   {
/* 46 */     if (super.acceptInboundMessage(msg)) {
/* 47 */       return acceptInboundMessage((SctpMessage)msg);
/*    */     }
/* 49 */     return false;
/*    */   }
/*    */   
/*    */   protected boolean acceptInboundMessage(SctpMessage msg) {
/* 53 */     return (msg.protocolIdentifier() == this.protocolIdentifier) && (msg.streamIdentifier() == this.streamIdentifier);
/*    */   }
/*    */   
/*    */   protected void decode(ChannelHandlerContext ctx, SctpMessage msg, List<Object> out) throws Exception
/*    */   {
/* 58 */     if (!msg.isComplete()) {
/* 59 */       throw new CodecException(String.format("Received SctpMessage is not complete, please add %s in the pipeline before this handler", new Object[] { SctpMessageCompletionHandler.class.getSimpleName() }));
/*    */     }
/*    */     
/* 62 */     out.add(msg.content().retain());
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\sctp\SctpInboundByteStreamHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */