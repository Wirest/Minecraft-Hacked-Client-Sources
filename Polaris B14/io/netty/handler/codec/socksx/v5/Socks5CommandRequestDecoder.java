/*     */ package io.netty.handler.codec.socksx.v5;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.DecoderResult;
/*     */ import io.netty.handler.codec.ReplayingDecoder;
/*     */ import io.netty.handler.codec.socksx.SocksVersion;
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
/*     */ public class Socks5CommandRequestDecoder
/*     */   extends ReplayingDecoder<State>
/*     */ {
/*     */   private final Socks5AddressDecoder addressDecoder;
/*     */   
/*     */   static enum State
/*     */   {
/*  38 */     INIT, 
/*  39 */     SUCCESS, 
/*  40 */     FAILURE;
/*     */     
/*     */     private State() {}
/*     */   }
/*     */   
/*     */   public Socks5CommandRequestDecoder() {
/*  46 */     this(Socks5AddressDecoder.DEFAULT);
/*     */   }
/*     */   
/*     */   public Socks5CommandRequestDecoder(Socks5AddressDecoder addressDecoder) {
/*  50 */     super(State.INIT);
/*  51 */     if (addressDecoder == null) {
/*  52 */       throw new NullPointerException("addressDecoder");
/*     */     }
/*     */     
/*  55 */     this.addressDecoder = addressDecoder;
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/*     */     try {
/*  61 */       switch ((State)state()) {
/*     */       case INIT: 
/*  63 */         byte version = in.readByte();
/*  64 */         if (version != SocksVersion.SOCKS5.byteValue()) {
/*  65 */           throw new DecoderException("unsupported version: " + version + " (expected: " + SocksVersion.SOCKS5.byteValue() + ')');
/*     */         }
/*     */         
/*     */ 
/*  69 */         Socks5CommandType type = Socks5CommandType.valueOf(in.readByte());
/*  70 */         in.skipBytes(1);
/*  71 */         Socks5AddressType dstAddrType = Socks5AddressType.valueOf(in.readByte());
/*  72 */         String dstAddr = this.addressDecoder.decodeAddress(dstAddrType, in);
/*  73 */         int dstPort = in.readUnsignedShort();
/*     */         
/*  75 */         out.add(new DefaultSocks5CommandRequest(type, dstAddrType, dstAddr, dstPort));
/*  76 */         checkpoint(State.SUCCESS);
/*     */       
/*     */       case SUCCESS: 
/*  79 */         int readableBytes = actualReadableBytes();
/*  80 */         if (readableBytes > 0) {
/*  81 */           out.add(in.readSlice(readableBytes).retain());
/*     */         }
/*     */         
/*     */         break;
/*     */       case FAILURE: 
/*  86 */         in.skipBytes(actualReadableBytes());
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  91 */       fail(out, e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void fail(List<Object> out, Throwable cause) {
/*  96 */     if (!(cause instanceof DecoderException)) {
/*  97 */       cause = new DecoderException(cause);
/*     */     }
/*     */     
/* 100 */     checkpoint(State.FAILURE);
/*     */     
/* 102 */     Socks5Message m = new DefaultSocks5CommandRequest(Socks5CommandType.CONNECT, Socks5AddressType.IPv4, "0.0.0.0", 1);
/*     */     
/* 104 */     m.setDecoderResult(DecoderResult.failure(cause));
/* 105 */     out.add(m);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\Socks5CommandRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */