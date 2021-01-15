/*     */ package io.netty.handler.codec.marshalling;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.ReplayingDecoder;
/*     */ import io.netty.handler.codec.TooLongFrameException;
/*     */ import java.util.List;
/*     */ import org.jboss.marshalling.ByteInput;
/*     */ import org.jboss.marshalling.Unmarshaller;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompatibleMarshallingDecoder
/*     */   extends ReplayingDecoder<Void>
/*     */ {
/*     */   protected final UnmarshallerProvider provider;
/*     */   protected final int maxObjectSize;
/*     */   private boolean discardingTooLongFrame;
/*     */   
/*     */   public CompatibleMarshallingDecoder(UnmarshallerProvider provider, int maxObjectSize)
/*     */   {
/*  53 */     this.provider = provider;
/*  54 */     this.maxObjectSize = maxObjectSize;
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception
/*     */   {
/*  59 */     if (this.discardingTooLongFrame) {
/*  60 */       buffer.skipBytes(actualReadableBytes());
/*  61 */       checkpoint();
/*  62 */       return;
/*     */     }
/*     */     
/*  65 */     Unmarshaller unmarshaller = this.provider.getUnmarshaller(ctx);
/*  66 */     ByteInput input = new ChannelBufferByteInput(buffer);
/*  67 */     if (this.maxObjectSize != Integer.MAX_VALUE) {
/*  68 */       input = new LimitingByteInput(input, this.maxObjectSize);
/*     */     }
/*     */     try {
/*  71 */       unmarshaller.start(input);
/*  72 */       Object obj = unmarshaller.readObject();
/*  73 */       unmarshaller.finish();
/*  74 */       out.add(obj);
/*     */     } catch (LimitingByteInput.TooBigObjectException ignored) {
/*  76 */       this.discardingTooLongFrame = true;
/*  77 */       throw new TooLongFrameException();
/*     */     }
/*     */     finally
/*     */     {
/*  81 */       unmarshaller.close();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void decodeLast(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception
/*     */   {
/*  87 */     switch (buffer.readableBytes()) {
/*     */     case 0: 
/*  89 */       return;
/*     */     
/*     */     case 1: 
/*  92 */       if (buffer.getByte(buffer.readerIndex()) == 121) {
/*  93 */         buffer.skipBytes(1); return;
/*     */       }
/*     */       break;
/*     */     }
/*     */     
/*  98 */     decode(ctx, buffer, out);
/*     */   }
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
/*     */   {
/* 103 */     if ((cause instanceof TooLongFrameException)) {
/* 104 */       ctx.close();
/*     */     } else {
/* 106 */       super.exceptionCaught(ctx, cause);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\marshalling\CompatibleMarshallingDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */