/*    */ package io.netty.handler.codec.sctp;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.sctp.SctpMessage;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SctpOutboundByteStreamHandler
/*    */   extends MessageToMessageEncoder<ByteBuf>
/*    */ {
/*    */   private final int streamIdentifier;
/*    */   private final int protocolIdentifier;
/*    */   
/*    */   public SctpOutboundByteStreamHandler(int streamIdentifier, int protocolIdentifier)
/*    */   {
/* 39 */     this.streamIdentifier = streamIdentifier;
/* 40 */     this.protocolIdentifier = protocolIdentifier;
/*    */   }
/*    */   
/*    */   protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception
/*    */   {
/* 45 */     out.add(new SctpMessage(this.streamIdentifier, this.protocolIdentifier, msg.retain()));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\sctp\SctpOutboundByteStreamHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */