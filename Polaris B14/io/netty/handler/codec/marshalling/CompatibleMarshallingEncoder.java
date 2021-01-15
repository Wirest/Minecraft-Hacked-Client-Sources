/*    */ package io.netty.handler.codec.marshalling;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandler.Sharable;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
/*    */ import org.jboss.marshalling.Marshaller;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ChannelHandler.Sharable
/*    */ public class CompatibleMarshallingEncoder
/*    */   extends MessageToByteEncoder<Object>
/*    */ {
/*    */   private final MarshallerProvider provider;
/*    */   
/*    */   public CompatibleMarshallingEncoder(MarshallerProvider provider)
/*    */   {
/* 47 */     this.provider = provider;
/*    */   }
/*    */   
/*    */   protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception
/*    */   {
/* 52 */     Marshaller marshaller = this.provider.getMarshaller(ctx);
/* 53 */     marshaller.start(new ChannelBufferByteOutput(out));
/* 54 */     marshaller.writeObject(msg);
/* 55 */     marshaller.finish();
/* 56 */     marshaller.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\marshalling\CompatibleMarshallingEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */