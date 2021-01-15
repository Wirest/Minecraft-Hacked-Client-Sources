/*    */ package io.netty.handler.codec.marshalling;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
/*    */ import org.jboss.marshalling.ByteInput;
/*    */ import org.jboss.marshalling.Unmarshaller;
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
/*    */ public class MarshallingDecoder
/*    */   extends LengthFieldBasedFrameDecoder
/*    */ {
/*    */   private final UnmarshallerProvider provider;
/*    */   
/*    */   public MarshallingDecoder(UnmarshallerProvider provider)
/*    */   {
/* 45 */     this(provider, 1048576);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public MarshallingDecoder(UnmarshallerProvider provider, int maxObjectSize)
/*    */   {
/* 57 */     super(maxObjectSize, 0, 4, 0, 4);
/* 58 */     this.provider = provider;
/*    */   }
/*    */   
/*    */   protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception
/*    */   {
/* 63 */     ByteBuf frame = (ByteBuf)super.decode(ctx, in);
/* 64 */     if (frame == null) {
/* 65 */       return null;
/*    */     }
/*    */     
/* 68 */     Unmarshaller unmarshaller = this.provider.getUnmarshaller(ctx);
/* 69 */     ByteInput input = new ChannelBufferByteInput(frame);
/*    */     try
/*    */     {
/* 72 */       unmarshaller.start(input);
/* 73 */       Object obj = unmarshaller.readObject();
/* 74 */       unmarshaller.finish();
/* 75 */       return obj;
/*    */     }
/*    */     finally
/*    */     {
/* 79 */       unmarshaller.close();
/*    */     }
/*    */   }
/*    */   
/*    */   protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length)
/*    */   {
/* 85 */     return buffer.slice(index, length);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\marshalling\MarshallingDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */