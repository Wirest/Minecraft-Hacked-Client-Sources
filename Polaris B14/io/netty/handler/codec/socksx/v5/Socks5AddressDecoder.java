/*    */ package io.netty.handler.codec.socksx.v5;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.handler.codec.DecoderException;
/*    */ import io.netty.util.CharsetUtil;
/*    */ import io.netty.util.NetUtil;
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
/*    */ public abstract interface Socks5AddressDecoder
/*    */ {
/* 32 */   public static final Socks5AddressDecoder DEFAULT = new Socks5AddressDecoder()
/*    */   {
/*    */     private static final int IPv6_LEN = 16;
/*    */     
/*    */     public String decodeAddress(Socks5AddressType addrType, ByteBuf in) throws Exception
/*    */     {
/* 38 */       if (addrType == Socks5AddressType.IPv4) {
/* 39 */         return NetUtil.intToIpAddress(in.readInt());
/*    */       }
/* 41 */       if (addrType == Socks5AddressType.DOMAIN) {
/* 42 */         int length = in.readUnsignedByte();
/* 43 */         String domain = in.toString(in.readerIndex(), length, CharsetUtil.US_ASCII);
/* 44 */         in.skipBytes(length);
/* 45 */         return domain;
/*    */       }
/* 47 */       if (addrType == Socks5AddressType.IPv6) {
/* 48 */         if (in.hasArray()) {
/* 49 */           int readerIdx = in.readerIndex();
/* 50 */           in.readerIndex(readerIdx + 16);
/* 51 */           return NetUtil.bytesToIpAddress(in.array(), in.arrayOffset() + readerIdx, 16);
/*    */         }
/* 53 */         byte[] tmp = new byte[16];
/* 54 */         in.readBytes(tmp);
/* 55 */         return NetUtil.bytesToIpAddress(tmp, 0, 16);
/*    */       }
/*    */       
/* 58 */       throw new DecoderException("unsupported address type: " + (addrType.byteValue() & 0xFF));
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract String decodeAddress(Socks5AddressType paramSocks5AddressType, ByteBuf paramByteBuf)
/*    */     throws Exception;
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\Socks5AddressDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */