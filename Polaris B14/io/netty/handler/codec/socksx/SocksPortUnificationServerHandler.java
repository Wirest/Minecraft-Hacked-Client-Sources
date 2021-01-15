/*     */ package io.netty.handler.codec.socksx;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import io.netty.handler.codec.socksx.v4.Socks4ServerDecoder;
/*     */ import io.netty.handler.codec.socksx.v4.Socks4ServerEncoder;
/*     */ import io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder;
/*     */ import io.netty.handler.codec.socksx.v5.Socks5ServerEncoder;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
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
/*     */ public class SocksPortUnificationServerHandler
/*     */   extends ByteToMessageDecoder
/*     */ {
/*  39 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(SocksPortUnificationServerHandler.class);
/*     */   
/*     */ 
/*     */   private final Socks5ServerEncoder socks5encoder;
/*     */   
/*     */ 
/*     */ 
/*     */   public SocksPortUnificationServerHandler()
/*     */   {
/*  48 */     this(Socks5ServerEncoder.DEFAULT);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SocksPortUnificationServerHandler(Socks5ServerEncoder socks5encoder)
/*     */   {
/*  56 */     if (socks5encoder == null) {
/*  57 */       throw new NullPointerException("socks5encoder");
/*     */     }
/*     */     
/*  60 */     this.socks5encoder = socks5encoder;
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/*  65 */     int readerIndex = in.readerIndex();
/*  66 */     if (in.writerIndex() == readerIndex) {
/*  67 */       return;
/*     */     }
/*     */     
/*  70 */     ChannelPipeline p = ctx.pipeline();
/*  71 */     byte versionVal = in.getByte(readerIndex);
/*  72 */     SocksVersion version = SocksVersion.valueOf(versionVal);
/*     */     
/*  74 */     switch (version) {
/*     */     case SOCKS4a: 
/*  76 */       logKnownVersion(ctx, version);
/*  77 */       p.addAfter(ctx.name(), null, Socks4ServerEncoder.INSTANCE);
/*  78 */       p.addAfter(ctx.name(), null, new Socks4ServerDecoder());
/*  79 */       break;
/*     */     case SOCKS5: 
/*  81 */       logKnownVersion(ctx, version);
/*  82 */       p.addAfter(ctx.name(), null, this.socks5encoder);
/*  83 */       p.addAfter(ctx.name(), null, new Socks5InitialRequestDecoder());
/*  84 */       break;
/*     */     default: 
/*  86 */       logUnknownVersion(ctx, versionVal);
/*  87 */       in.skipBytes(in.readableBytes());
/*  88 */       ctx.close();
/*  89 */       return;
/*     */     }
/*     */     
/*  92 */     p.remove(this);
/*     */   }
/*     */   
/*     */   private static void logKnownVersion(ChannelHandlerContext ctx, SocksVersion version) {
/*  96 */     logger.debug("{} Protocol version: {}({})", ctx.channel(), version);
/*     */   }
/*     */   
/*     */   private static void logUnknownVersion(ChannelHandlerContext ctx, byte versionVal) {
/* 100 */     if (logger.isDebugEnabled()) {
/* 101 */       logger.debug("{} Unknown protocol version: {}", ctx.channel(), Integer.valueOf(versionVal & 0xFF));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\SocksPortUnificationServerHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */