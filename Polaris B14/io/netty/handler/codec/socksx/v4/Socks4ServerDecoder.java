/*     */ package io.netty.handler.codec.socksx.v4;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.DecoderResult;
/*     */ import io.netty.handler.codec.ReplayingDecoder;
/*     */ import io.netty.handler.codec.socksx.SocksVersion;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.NetUtil;
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
/*     */ public class Socks4ServerDecoder
/*     */   extends ReplayingDecoder<State>
/*     */ {
/*     */   private static final int MAX_FIELD_LENGTH = 255;
/*     */   private Socks4CommandType type;
/*     */   private String dstAddr;
/*     */   private int dstPort;
/*     */   private String userId;
/*     */   
/*     */   static enum State
/*     */   {
/*  41 */     START, 
/*  42 */     READ_USERID, 
/*  43 */     READ_DOMAIN, 
/*  44 */     SUCCESS, 
/*  45 */     FAILURE;
/*     */     
/*     */ 
/*     */     private State() {}
/*     */   }
/*     */   
/*     */ 
/*     */   public Socks4ServerDecoder()
/*     */   {
/*  54 */     super(State.START);
/*  55 */     setSingleDecode(true);
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/*     */     try {
/*  61 */       switch ((State)state()) {
/*     */       case START: 
/*  63 */         int version = in.readUnsignedByte();
/*  64 */         if (version != SocksVersion.SOCKS4a.byteValue()) {
/*  65 */           throw new DecoderException("unsupported protocol version: " + version);
/*     */         }
/*     */         
/*  68 */         this.type = Socks4CommandType.valueOf(in.readByte());
/*  69 */         this.dstPort = in.readUnsignedShort();
/*  70 */         this.dstAddr = NetUtil.intToIpAddress(in.readInt());
/*  71 */         checkpoint(State.READ_USERID);
/*     */       
/*     */       case READ_USERID: 
/*  74 */         this.userId = readString("userid", in);
/*  75 */         checkpoint(State.READ_DOMAIN);
/*     */       
/*     */ 
/*     */       case READ_DOMAIN: 
/*  79 */         if ((!"0.0.0.0".equals(this.dstAddr)) && (this.dstAddr.startsWith("0.0.0."))) {
/*  80 */           this.dstAddr = readString("dstAddr", in);
/*     */         }
/*  82 */         out.add(new DefaultSocks4CommandRequest(this.type, this.dstAddr, this.dstPort, this.userId));
/*  83 */         checkpoint(State.SUCCESS);
/*     */       
/*     */       case SUCCESS: 
/*  86 */         int readableBytes = actualReadableBytes();
/*  87 */         if (readableBytes > 0) {
/*  88 */           out.add(in.readSlice(readableBytes).retain());
/*     */         }
/*     */         
/*     */         break;
/*     */       case FAILURE: 
/*  93 */         in.skipBytes(actualReadableBytes());
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  98 */       fail(out, e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void fail(List<Object> out, Throwable cause) {
/* 103 */     if (!(cause instanceof DecoderException)) {
/* 104 */       cause = new DecoderException(cause);
/*     */     }
/*     */     
/* 107 */     Socks4CommandRequest m = new DefaultSocks4CommandRequest(this.type != null ? this.type : Socks4CommandType.CONNECT, this.dstAddr != null ? this.dstAddr : "", this.dstPort != 0 ? this.dstPort : 65535, this.userId != null ? this.userId : "");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 113 */     m.setDecoderResult(DecoderResult.failure(cause));
/* 114 */     out.add(m);
/*     */     
/* 116 */     checkpoint(State.FAILURE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static String readString(String fieldName, ByteBuf in)
/*     */   {
/* 123 */     int length = in.bytesBefore(256, (byte)0);
/* 124 */     if (length < 0) {
/* 125 */       throw new DecoderException("field '" + fieldName + "' longer than " + 255 + " chars");
/*     */     }
/*     */     
/* 128 */     String value = in.readSlice(length).toString(CharsetUtil.US_ASCII);
/* 129 */     in.skipBytes(1);
/*     */     
/* 131 */     return value;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v4\Socks4ServerDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */