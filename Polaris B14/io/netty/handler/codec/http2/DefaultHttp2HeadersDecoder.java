/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import com.twitter.hpack.Decoder;
/*     */ import com.twitter.hpack.HeaderListener;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufInputStream;
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ public class DefaultHttp2HeadersDecoder
/*     */   implements Http2HeadersDecoder, Http2HeadersDecoder.Configuration
/*     */ {
/*     */   private final Decoder decoder;
/*     */   private final Http2HeaderTable headerTable;
/*     */   
/*     */   public DefaultHttp2HeadersDecoder()
/*     */   {
/*  39 */     this(8192, 4096);
/*     */   }
/*     */   
/*     */   public DefaultHttp2HeadersDecoder(int maxHeaderSize, int maxHeaderTableSize) {
/*  43 */     this.decoder = new Decoder(maxHeaderSize, maxHeaderTableSize);
/*  44 */     this.headerTable = new Http2HeaderTableDecoder(null);
/*     */   }
/*     */   
/*     */   public Http2HeaderTable headerTable()
/*     */   {
/*  49 */     return this.headerTable;
/*     */   }
/*     */   
/*     */   public Http2HeadersDecoder.Configuration configuration()
/*     */   {
/*  54 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers decodeHeaders(ByteBuf headerBlock) throws Http2Exception
/*     */   {
/*  59 */     InputStream in = new ByteBufInputStream(headerBlock);
/*     */     try {
/*  61 */       final Http2Headers headers = new DefaultHttp2Headers();
/*  62 */       HeaderListener listener = new HeaderListener()
/*     */       {
/*     */         public void addHeader(byte[] key, byte[] value, boolean sensitive) {
/*  65 */           headers.add(new AsciiString(key, false), new AsciiString(value, false));
/*     */         }
/*     */         
/*  68 */       };
/*  69 */       this.decoder.decode(in, listener);
/*  70 */       boolean truncated = this.decoder.endHeaderBlock();
/*  71 */       if ((!truncated) || 
/*     */       
/*     */ 
/*     */ 
/*  75 */         (headers.size() > this.headerTable.maxHeaderListSize())) {
/*  76 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Number of headers (%d) exceeds maxHeaderListSize (%d)", new Object[] { Integer.valueOf(headers.size()), Integer.valueOf(this.headerTable.maxHeaderListSize()) });
/*     */       }
/*     */       
/*     */ 
/*  80 */       return headers;
/*     */     } catch (IOException e) {
/*  82 */       throw Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, e, e.getMessage(), new Object[0]);
/*     */     } catch (Http2Exception e) {
/*  84 */       throw e;
/*     */ 
/*     */     }
/*     */     catch (Throwable e)
/*     */     {
/*  89 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, e, e.getMessage(), new Object[0]);
/*     */     } finally {
/*     */       try {
/*  92 */         in.close();
/*     */       } catch (IOException e) {
/*  94 */         throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, e, e.getMessage(), new Object[0]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Http2HeaderTableDecoder extends DefaultHttp2HeaderTableListSize implements Http2HeaderTable
/*     */   {
/*     */     private Http2HeaderTableDecoder() {}
/*     */     
/*     */     public void maxHeaderTableSize(int max) throws Http2Exception
/*     */     {
/* 105 */       if (max < 0) {
/* 106 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Header Table Size must be non-negative but was %d", new Object[] { Integer.valueOf(max) });
/*     */       }
/*     */       try {
/* 109 */         DefaultHttp2HeadersDecoder.this.decoder.setMaxHeaderTableSize(max);
/*     */       } catch (Throwable t) {
/* 111 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, t.getMessage(), new Object[] { t });
/*     */       }
/*     */     }
/*     */     
/*     */     public int maxHeaderTableSize()
/*     */     {
/* 117 */       return DefaultHttp2HeadersDecoder.this.decoder.getMaxHeaderTableSize();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\DefaultHttp2HeadersDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */