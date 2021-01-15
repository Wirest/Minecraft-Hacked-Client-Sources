/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.FileRegion;
/*     */ import io.netty.handler.codec.MessageToMessageEncoder;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ public abstract class HttpObjectEncoder<H extends HttpMessage>
/*     */   extends MessageToMessageEncoder<Object>
/*     */ {
/*  45 */   private static final byte[] CRLF = { 13, 10 };
/*  46 */   private static final byte[] ZERO_CRLF = { 48, 13, 10 };
/*  47 */   private static final byte[] ZERO_CRLF_CRLF = { 48, 13, 10, 13, 10 };
/*  48 */   private static final ByteBuf CRLF_BUF = Unpooled.unreleasableBuffer(Unpooled.directBuffer(CRLF.length).writeBytes(CRLF));
/*  49 */   private static final ByteBuf ZERO_CRLF_CRLF_BUF = Unpooled.unreleasableBuffer(Unpooled.directBuffer(ZERO_CRLF_CRLF.length).writeBytes(ZERO_CRLF_CRLF));
/*     */   
/*     */   private static final int ST_INIT = 0;
/*     */   
/*     */   private static final int ST_CONTENT_NON_CHUNK = 1;
/*     */   
/*     */   private static final int ST_CONTENT_CHUNK = 2;
/*  56 */   private int state = 0;
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out)
/*     */     throws Exception
/*     */   {
/*  61 */     ByteBuf buf = null;
/*  62 */     if ((msg instanceof HttpMessage)) {
/*  63 */       if (this.state != 0) {
/*  64 */         throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
/*     */       }
/*     */       
/*     */ 
/*  68 */       H m = (HttpMessage)msg;
/*     */       
/*  70 */       buf = ctx.alloc().buffer();
/*     */       
/*  72 */       encodeInitialLine(buf, m);
/*  73 */       encodeHeaders(m.headers(), buf);
/*  74 */       buf.writeBytes(CRLF);
/*  75 */       this.state = (HttpHeaderUtil.isTransferEncodingChunked(m) ? 2 : 1);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  84 */     if (((msg instanceof ByteBuf)) && (!((ByteBuf)msg).isReadable())) {
/*  85 */       out.add(Unpooled.EMPTY_BUFFER);
/*  86 */       return;
/*     */     }
/*     */     
/*  89 */     if (((msg instanceof HttpContent)) || ((msg instanceof ByteBuf)) || ((msg instanceof FileRegion)))
/*     */     {
/*  91 */       if (this.state == 0) {
/*  92 */         throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
/*     */       }
/*     */       
/*  95 */       long contentLength = contentLength(msg);
/*  96 */       if (this.state == 1) {
/*  97 */         if (contentLength > 0L) {
/*  98 */           if ((buf != null) && (buf.writableBytes() >= contentLength) && ((msg instanceof HttpContent)))
/*     */           {
/* 100 */             buf.writeBytes(((HttpContent)msg).content());
/* 101 */             out.add(buf);
/*     */           } else {
/* 103 */             if (buf != null) {
/* 104 */               out.add(buf);
/*     */             }
/* 106 */             out.add(encodeAndRetain(msg));
/*     */           }
/*     */         }
/* 109 */         else if (buf != null) {
/* 110 */           out.add(buf);
/*     */         }
/*     */         else
/*     */         {
/* 114 */           out.add(Unpooled.EMPTY_BUFFER);
/*     */         }
/*     */         
/*     */ 
/* 118 */         if ((msg instanceof LastHttpContent)) {
/* 119 */           this.state = 0;
/*     */         }
/* 121 */       } else if (this.state == 2) {
/* 122 */         if (buf != null) {
/* 123 */           out.add(buf);
/*     */         }
/* 125 */         encodeChunkedContent(ctx, msg, contentLength, out);
/*     */       } else {
/* 127 */         throw new Error();
/*     */       }
/*     */     }
/* 130 */     else if (buf != null) {
/* 131 */       out.add(buf);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void encodeHeaders(HttpHeaders headers, ByteBuf buf)
/*     */     throws Exception
/*     */   {
/* 140 */     headers.forEachEntry(new HttpHeadersEncoder(buf));
/*     */   }
/*     */   
/*     */   private void encodeChunkedContent(ChannelHandlerContext ctx, Object msg, long contentLength, List<Object> out) {
/* 144 */     if (contentLength > 0L) {
/* 145 */       byte[] length = Long.toHexString(contentLength).getBytes(CharsetUtil.US_ASCII);
/* 146 */       ByteBuf buf = ctx.alloc().buffer(length.length + 2);
/* 147 */       buf.writeBytes(length);
/* 148 */       buf.writeBytes(CRLF);
/* 149 */       out.add(buf);
/* 150 */       out.add(encodeAndRetain(msg));
/* 151 */       out.add(CRLF_BUF.duplicate());
/*     */     }
/*     */     
/* 154 */     if ((msg instanceof LastHttpContent)) {
/* 155 */       HttpHeaders headers = ((LastHttpContent)msg).trailingHeaders();
/* 156 */       if (headers.isEmpty()) {
/* 157 */         out.add(ZERO_CRLF_CRLF_BUF.duplicate());
/*     */       } else {
/* 159 */         ByteBuf buf = ctx.alloc().buffer();
/* 160 */         buf.writeBytes(ZERO_CRLF);
/*     */         try {
/* 162 */           encodeHeaders(headers, buf);
/*     */         } catch (Exception ex) {
/* 164 */           buf.release();
/* 165 */           PlatformDependent.throwException(ex);
/*     */         }
/* 167 */         buf.writeBytes(CRLF);
/* 168 */         out.add(buf);
/*     */       }
/*     */       
/* 171 */       this.state = 0;
/*     */     }
/* 173 */     else if (contentLength == 0L)
/*     */     {
/*     */ 
/* 176 */       out.add(Unpooled.EMPTY_BUFFER);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean acceptOutboundMessage(Object msg)
/*     */     throws Exception
/*     */   {
/* 183 */     return ((msg instanceof HttpObject)) || ((msg instanceof ByteBuf)) || ((msg instanceof FileRegion));
/*     */   }
/*     */   
/*     */   private static Object encodeAndRetain(Object msg) {
/* 187 */     if ((msg instanceof ByteBuf)) {
/* 188 */       return ((ByteBuf)msg).retain();
/*     */     }
/* 190 */     if ((msg instanceof HttpContent)) {
/* 191 */       return ((HttpContent)msg).content().retain();
/*     */     }
/* 193 */     if ((msg instanceof FileRegion)) {
/* 194 */       return ((FileRegion)msg).retain();
/*     */     }
/* 196 */     throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
/*     */   }
/*     */   
/*     */   private static long contentLength(Object msg) {
/* 200 */     if ((msg instanceof HttpContent)) {
/* 201 */       return ((HttpContent)msg).content().readableBytes();
/*     */     }
/* 203 */     if ((msg instanceof ByteBuf)) {
/* 204 */       return ((ByteBuf)msg).readableBytes();
/*     */     }
/* 206 */     if ((msg instanceof FileRegion)) {
/* 207 */       return ((FileRegion)msg).count();
/*     */     }
/* 209 */     throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
/*     */   }
/*     */   
/*     */   protected abstract void encodeInitialLine(ByteBuf paramByteBuf, H paramH)
/*     */     throws Exception;
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpObjectEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */