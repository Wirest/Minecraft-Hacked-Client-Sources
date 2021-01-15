/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandler.Sharable;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import java.util.List;
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
/*     */ @ChannelHandler.Sharable
/*     */ public class LengthFieldPrepender
/*     */   extends MessageToMessageEncoder<ByteBuf>
/*     */ {
/*     */   private final int lengthFieldLength;
/*     */   private final boolean lengthIncludesLengthFieldLength;
/*     */   private final int lengthAdjustment;
/*     */   
/*     */   public LengthFieldPrepender(int lengthFieldLength)
/*     */   {
/*  68 */     this(lengthFieldLength, false);
/*     */   }
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
/*     */   public LengthFieldPrepender(int lengthFieldLength, boolean lengthIncludesLengthFieldLength)
/*     */   {
/*  85 */     this(lengthFieldLength, 0, lengthIncludesLengthFieldLength);
/*     */   }
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
/*     */   public LengthFieldPrepender(int lengthFieldLength, int lengthAdjustment)
/*     */   {
/* 100 */     this(lengthFieldLength, lengthAdjustment, false);
/*     */   }
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
/*     */   public LengthFieldPrepender(int lengthFieldLength, int lengthAdjustment, boolean lengthIncludesLengthFieldLength)
/*     */   {
/* 119 */     if ((lengthFieldLength != 1) && (lengthFieldLength != 2) && (lengthFieldLength != 3) && (lengthFieldLength != 4) && (lengthFieldLength != 8))
/*     */     {
/*     */ 
/* 122 */       throw new IllegalArgumentException("lengthFieldLength must be either 1, 2, 3, 4, or 8: " + lengthFieldLength);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 127 */     this.lengthFieldLength = lengthFieldLength;
/* 128 */     this.lengthIncludesLengthFieldLength = lengthIncludesLengthFieldLength;
/* 129 */     this.lengthAdjustment = lengthAdjustment;
/*     */   }
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception
/*     */   {
/* 134 */     int length = msg.readableBytes() + this.lengthAdjustment;
/* 135 */     if (this.lengthIncludesLengthFieldLength) {
/* 136 */       length += this.lengthFieldLength;
/*     */     }
/*     */     
/* 139 */     if (length < 0) {
/* 140 */       throw new IllegalArgumentException("Adjusted frame length (" + length + ") is less than zero");
/*     */     }
/*     */     
/*     */ 
/* 144 */     switch (this.lengthFieldLength) {
/*     */     case 1: 
/* 146 */       if (length >= 256) {
/* 147 */         throw new IllegalArgumentException("length does not fit into a byte: " + length);
/*     */       }
/*     */       
/* 150 */       out.add(ctx.alloc().buffer(1).writeByte((byte)length));
/* 151 */       break;
/*     */     case 2: 
/* 153 */       if (length >= 65536) {
/* 154 */         throw new IllegalArgumentException("length does not fit into a short integer: " + length);
/*     */       }
/*     */       
/* 157 */       out.add(ctx.alloc().buffer(2).writeShort((short)length));
/* 158 */       break;
/*     */     case 3: 
/* 160 */       if (length >= 16777216) {
/* 161 */         throw new IllegalArgumentException("length does not fit into a medium integer: " + length);
/*     */       }
/*     */       
/* 164 */       out.add(ctx.alloc().buffer(3).writeMedium(length));
/* 165 */       break;
/*     */     case 4: 
/* 167 */       out.add(ctx.alloc().buffer(4).writeInt(length));
/* 168 */       break;
/*     */     case 8: 
/* 170 */       out.add(ctx.alloc().buffer(8).writeLong(length));
/* 171 */       break;
/*     */     case 5: case 6: case 7: default: 
/* 173 */       throw new Error("should not reach here");
/*     */     }
/* 175 */     out.add(msg.retain());
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\LengthFieldPrepender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */