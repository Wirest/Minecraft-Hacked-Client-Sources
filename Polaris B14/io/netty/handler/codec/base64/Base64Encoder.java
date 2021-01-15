/*    */ package io.netty.handler.codec.base64;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandler.Sharable;
/*    */ import io.netty.channel.ChannelHandlerContext;
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
/*    */ public class Base64Encoder
/*    */   extends MessageToMessageEncoder<ByteBuf>
/*    */ {
/*    */   private final boolean breakLines;
/*    */   private final Base64Dialect dialect;
/*    */   
/*    */   public Base64Encoder()
/*    */   {
/* 49 */     this(true);
/*    */   }
/*    */   
/*    */   public Base64Encoder(boolean breakLines) {
/* 53 */     this(breakLines, Base64Dialect.STANDARD);
/*    */   }
/*    */   
/*    */   public Base64Encoder(boolean breakLines, Base64Dialect dialect) {
/* 57 */     if (dialect == null) {
/* 58 */       throw new NullPointerException("dialect");
/*    */     }
/*    */     
/* 61 */     this.breakLines = breakLines;
/* 62 */     this.dialect = dialect;
/*    */   }
/*    */   
/*    */   protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception
/*    */   {
/* 67 */     out.add(Base64.encode(msg, msg.readerIndex(), msg.readableBytes(), this.breakLines, this.dialect));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\base64\Base64Encoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */