/*     */ package io.netty.handler.codec.http.websocketx.extensions.compression;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.CompositeByteBuf;
/*     */ import io.netty.buffer.Unpooled;
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
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionDecoder;
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
/*     */ abstract class DeflateDecoder
/*     */   extends WebSocketExtensionDecoder
/*     */ {
/*  44 */   static final byte[] FRAME_TAIL = { 0, 0, -1, -1 };
/*     */   
/*     */ 
/*     */   private final boolean noContext;
/*     */   
/*     */ 
/*     */   private EmbeddedChannel decoder;
/*     */   
/*     */ 
/*     */   public DeflateDecoder(boolean noContext)
/*     */   {
/*  55 */     this.noContext = noContext;
/*     */   }
/*     */   
/*     */   protected abstract boolean appendFrameTail(WebSocketFrame paramWebSocketFrame);
/*     */   
/*     */   protected abstract int newRsv(WebSocketFrame paramWebSocketFrame);
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception
/*     */   {
/*  64 */     if (this.decoder == null) {
/*  65 */       if ((!(msg instanceof TextWebSocketFrame)) && (!(msg instanceof BinaryWebSocketFrame))) {
/*  66 */         throw new CodecException("unexpected initial frame type: " + msg.getClass().getName());
/*     */       }
/*  68 */       this.decoder = new EmbeddedChannel(new ChannelHandler[] { ZlibCodecFactory.newZlibDecoder(ZlibWrapper.NONE) });
/*     */     }
/*     */     
/*  71 */     this.decoder.writeInbound(new Object[] { msg.content().retain() });
/*  72 */     if (appendFrameTail(msg)) {
/*  73 */       this.decoder.writeInbound(new Object[] { Unpooled.wrappedBuffer(FRAME_TAIL) });
/*     */     }
/*     */     
/*  76 */     CompositeByteBuf compositeUncompressedContent = ctx.alloc().compositeBuffer();
/*     */     for (;;) {
/*  78 */       ByteBuf partUncompressedContent = (ByteBuf)this.decoder.readInbound();
/*  79 */       if (partUncompressedContent == null) {
/*     */         break;
/*     */       }
/*  82 */       if (!partUncompressedContent.isReadable()) {
/*  83 */         partUncompressedContent.release();
/*     */       }
/*     */       else {
/*  86 */         compositeUncompressedContent.addComponent(partUncompressedContent);
/*  87 */         compositeUncompressedContent.writerIndex(compositeUncompressedContent.writerIndex() + partUncompressedContent.readableBytes());
/*     */       }
/*     */     }
/*  90 */     if (compositeUncompressedContent.numComponents() <= 0) {
/*  91 */       compositeUncompressedContent.release();
/*  92 */       throw new CodecException("cannot read uncompressed buffer");
/*     */     }
/*     */     
/*  95 */     if ((msg.isFinalFragment()) && (this.noContext)) {
/*  96 */       cleanup();
/*     */     }
/*     */     
/*     */     WebSocketFrame outMsg;
/* 100 */     if ((msg instanceof TextWebSocketFrame)) {
/* 101 */       outMsg = new TextWebSocketFrame(msg.isFinalFragment(), newRsv(msg), compositeUncompressedContent); } else { WebSocketFrame outMsg;
/* 102 */       if ((msg instanceof BinaryWebSocketFrame)) {
/* 103 */         outMsg = new BinaryWebSocketFrame(msg.isFinalFragment(), newRsv(msg), compositeUncompressedContent); } else { WebSocketFrame outMsg;
/* 104 */         if ((msg instanceof ContinuationWebSocketFrame)) {
/* 105 */           outMsg = new ContinuationWebSocketFrame(msg.isFinalFragment(), newRsv(msg), compositeUncompressedContent);
/*     */         }
/*     */         else
/* 108 */           throw new CodecException("unexpected frame type: " + msg.getClass().getName()); } }
/*     */     WebSocketFrame outMsg;
/* 110 */     out.add(outMsg);
/*     */   }
/*     */   
/*     */   public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 115 */     cleanup();
/* 116 */     super.handlerRemoved(ctx);
/*     */   }
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 121 */     cleanup();
/* 122 */     super.channelInactive(ctx);
/*     */   }
/*     */   
/*     */   private void cleanup() {
/* 126 */     if (this.decoder != null)
/*     */     {
/* 128 */       if (this.decoder.finish()) {
/*     */         for (;;) {
/* 130 */           ByteBuf buf = (ByteBuf)this.decoder.readOutbound();
/* 131 */           if (buf == null) {
/*     */             break;
/*     */           }
/*     */           
/* 135 */           buf.release();
/*     */         }
/*     */       }
/* 138 */       this.decoder = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\extensions\compression\DeflateDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */