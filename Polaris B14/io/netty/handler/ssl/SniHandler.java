/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.DomainNameMapping;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.IDN;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
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
/*     */ public class SniHandler
/*     */   extends ByteToMessageDecoder
/*     */ {
/*  40 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(SniHandler.class);
/*     */   
/*     */ 
/*     */   private final DomainNameMapping<SslContext> mapping;
/*     */   
/*     */ 
/*     */   private boolean handshaken;
/*     */   
/*     */ 
/*     */   private volatile String hostname;
/*     */   
/*     */ 
/*     */   private volatile SslContext selectedContext;
/*     */   
/*     */ 
/*     */   public SniHandler(DomainNameMapping<? extends SslContext> mapping)
/*     */   {
/*  57 */     if (mapping == null) {
/*  58 */       throw new NullPointerException("mapping");
/*     */     }
/*     */     
/*  61 */     this.mapping = mapping;
/*  62 */     this.handshaken = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String hostname()
/*     */   {
/*  69 */     return this.hostname;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SslContext sslContext()
/*     */   {
/*  76 */     return this.selectedContext;
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/*  81 */     if ((!this.handshaken) && (in.readableBytes() >= 5)) {
/*  82 */       String hostname = sniHostNameFromHandshakeInfo(in);
/*  83 */       if (hostname != null) {
/*  84 */         hostname = IDN.toASCII(hostname, 1).toLowerCase(Locale.US);
/*     */       }
/*  86 */       this.hostname = hostname;
/*     */       
/*     */ 
/*  89 */       this.selectedContext = ((SslContext)this.mapping.map(hostname));
/*     */     }
/*     */     
/*  92 */     if (this.handshaken) {
/*  93 */       SslHandler sslHandler = this.selectedContext.newHandler(ctx.alloc());
/*  94 */       ctx.pipeline().replace(this, SslHandler.class.getName(), sslHandler);
/*     */     }
/*     */   }
/*     */   
/*     */   private String sniHostNameFromHandshakeInfo(ByteBuf in) {
/*  99 */     int readerIndex = in.readerIndex();
/*     */     try {
/* 101 */       int command = in.getUnsignedByte(readerIndex);
/*     */       
/*     */ 
/* 104 */       switch (command) {
/*     */       case 20: 
/*     */       case 21: 
/*     */       case 23: 
/* 108 */         return null;
/*     */       case 22: 
/*     */         break;
/*     */       
/*     */       default: 
/* 113 */         this.handshaken = true;
/* 114 */         return null;
/*     */       }
/*     */       
/* 117 */       int majorVersion = in.getUnsignedByte(readerIndex + 1);
/*     */       
/*     */ 
/* 120 */       if (majorVersion == 3)
/*     */       {
/* 122 */         int packetLength = in.getUnsignedShort(readerIndex + 3) + 5;
/*     */         
/* 124 */         if (in.readableBytes() >= packetLength)
/*     */         {
/*     */ 
/* 127 */           int offset = readerIndex + 43;
/*     */           
/* 129 */           int sessionIdLength = in.getUnsignedByte(offset);
/* 130 */           offset += sessionIdLength + 1;
/*     */           
/* 132 */           int cipherSuitesLength = in.getUnsignedShort(offset);
/* 133 */           offset += cipherSuitesLength + 2;
/*     */           
/* 135 */           int compressionMethodLength = in.getUnsignedByte(offset);
/* 136 */           offset += compressionMethodLength + 1;
/*     */           
/* 138 */           int extensionsLength = in.getUnsignedShort(offset);
/* 139 */           offset += 2;
/* 140 */           int extensionsLimit = offset + extensionsLength;
/*     */           
/* 142 */           while (offset < extensionsLimit) {
/* 143 */             int extensionType = in.getUnsignedShort(offset);
/* 144 */             offset += 2;
/*     */             
/* 146 */             int extensionLength = in.getUnsignedShort(offset);
/* 147 */             offset += 2;
/*     */             
/*     */ 
/* 150 */             if (extensionType == 0) {
/* 151 */               this.handshaken = true;
/* 152 */               int serverNameType = in.getUnsignedByte(offset + 2);
/* 153 */               if (serverNameType == 0) {
/* 154 */                 int serverNameLength = in.getUnsignedShort(offset + 3);
/* 155 */                 return in.toString(offset + 5, serverNameLength, CharsetUtil.UTF_8);
/*     */               }
/*     */               
/*     */ 
/* 159 */               return null;
/*     */             }
/*     */             
/*     */ 
/* 163 */             offset += extensionLength;
/*     */           }
/*     */           
/* 166 */           this.handshaken = true;
/* 167 */           return null;
/*     */         }
/*     */         
/* 170 */         return null;
/*     */       }
/*     */       
/* 173 */       this.handshaken = true;
/* 174 */       return null;
/*     */     }
/*     */     catch (Throwable e)
/*     */     {
/* 178 */       if (logger.isDebugEnabled()) {
/* 179 */         logger.debug("Unexpected client hello packet: " + ByteBufUtil.hexDump(in), e);
/*     */       }
/* 181 */       this.handshaken = true; }
/* 182 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\SniHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */