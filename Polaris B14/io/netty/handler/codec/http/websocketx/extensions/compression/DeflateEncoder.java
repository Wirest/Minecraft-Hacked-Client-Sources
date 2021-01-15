/*     */ package io.netty.handler.codec.http.websocketx.extensions.compression;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.CompositeByteBuf;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.embedded.EmbeddedChannel;
/*     */ import io.netty.handler.codec.CodecException;
/*     */ import io.netty.handler.codec.compression.ZlibCodecFactory;
/*     */ import io.netty.handler.codec.compression.ZlibWrapper;
/*     */ import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
/*     */ import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
/*     */ import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
/*     */ import io.netty.handler.codec.http.websocketx.WebSocketFrame;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionEncoder;
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
/*     */ abstract class DeflateEncoder
/*     */   extends WebSocketExtensionEncoder
/*     */ {
/*     */   private final int compressionLevel;
/*     */   private final int windowSize;
/*     */   private final boolean noContext;
/*     */   private EmbeddedChannel encoder;
/*     */   
/*     */   public DeflateEncoder(int compressionLevel, int windowSize, boolean noContext)
/*     */   {
/*  54 */     this.compressionLevel = compressionLevel;
/*  55 */     this.windowSize = windowSize;
/*  56 */     this.noContext = noContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract int rsv(WebSocketFrame paramWebSocketFrame);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean removeFrameTail(WebSocketFrame paramWebSocketFrame);
/*     */   
/*     */ 
/*     */ 
/*     */   protected void encode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out)
/*     */     throws Exception
/*     */   {
/*  74 */     if (this.encoder == null) {
/*  75 */       this.encoder = new EmbeddedChannel(new ChannelHandler[] { ZlibCodecFactory.newZlibEncoder(ZlibWrapper.NONE, this.compressionLevel, this.windowSize, 8) });
/*     */     }
/*     */     
/*     */ 
/*  79 */     this.encoder.writeOutbound(new Object[] { msg.content().retain() });
/*     */     
/*  81 */     CompositeByteBuf fullCompressedContent = ctx.alloc().compositeBuffer();
/*     */     for (;;) {
/*  83 */       ByteBuf partCompressedContent = (ByteBuf)this.encoder.readOutbound();
/*  84 */       if (partCompressedContent == null) {
/*     */         break;
/*     */       }
/*  87 */       if (!partCompressedContent.isReadable()) {
/*  88 */         partCompressedContent.release();
/*     */       }
/*     */       else {
/*  91 */         fullCompressedContent.addComponent(partCompressedContent);
/*  92 */         fullCompressedContent.writerIndex(fullCompressedContent.writerIndex() + partCompressedContent.readableBytes());
/*     */       }
/*     */     }
/*  95 */     if (fullCompressedContent.numComponents() <= 0) {
/*  96 */       fullCompressedContent.release();
/*  97 */       throw new CodecException("cannot read compressed buffer");
/*     */     }
/*     */     
/* 100 */     if ((msg.isFinalFragment()) && (this.noContext)) {
/* 101 */       cleanup();
/*     */     }
/*     */     ByteBuf compressedContent;
/*     */     ByteBuf compressedContent;
/* 105 */     if (removeFrameTail(msg)) {
/* 106 */       int realLength = fullCompressedContent.readableBytes() - PerMessageDeflateDecoder.FRAME_TAIL.length;
/* 107 */       compressedContent = fullCompressedContent.slice(0, realLength);
/*     */     } else {
/* 109 */       compressedContent = fullCompressedContent;
/*     */     }
/*     */     
/*     */     WebSocketFrame outMsg;
/* 113 */     if ((msg instanceof TextWebSocketFrame)) {
/* 114 */       outMsg = new TextWebSocketFrame(msg.isFinalFragment(), rsv(msg), compressedContent); } else { WebSocketFrame outMsg;
/* 115 */       if ((msg instanceof BinaryWebSocketFrame)) {
/* 116 */         outMsg = new BinaryWebSocketFrame(msg.isFinalFragment(), rsv(msg), compressedContent); } else { WebSocketFrame outMsg;
/* 117 */         if ((msg instanceof ContinuationWebSocketFrame)) {
/* 118 */           outMsg = new ContinuationWebSocketFrame(msg.isFinalFragment(), rsv(msg), compressedContent);
/*     */         } else
/* 120 */           throw new CodecException("unexpected frame type: " + msg.getClass().getName()); } }
/*     */     WebSocketFrame outMsg;
/* 122 */     out.add(outMsg);
/*     */   }
/*     */   
/*     */   public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 127 */     cleanup();
/* 128 */     super.handlerRemoved(ctx);
/*     */   }
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 133 */     cleanup();
/* 134 */     super.channelInactive(ctx);
/*     */   }
/*     */   
/*     */   private void cleanup() {
/* 138 */     if (this.encoder != null)
/*     */     {
/* 140 */       if (this.encoder.finish()) {
/*     */         for (;;) {
/* 142 */           ByteBuf buf = (ByteBuf)this.encoder.readOutbound();
/* 143 */           if (buf == null) {
/*     */             break;
/*     */           }
/*     */           
/* 147 */           buf.release();
/*     */         }
/*     */       }
/* 150 */       this.encoder = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\extensions\compression\DeflateEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */