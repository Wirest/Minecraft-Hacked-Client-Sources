/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import com.twitter.hpack.Encoder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufOutputStream;
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.handler.codec.BinaryHeaders.EntryVisitor;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Collections;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
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
/*     */ public class DefaultHttp2HeadersEncoder
/*     */   implements Http2HeadersEncoder, Http2HeadersEncoder.Configuration
/*     */ {
/*     */   private final Encoder encoder;
/*  40 */   private final ByteArrayOutputStream tableSizeChangeOutput = new ByteArrayOutputStream();
/*  41 */   private final Set<String> sensitiveHeaders = new TreeSet(String.CASE_INSENSITIVE_ORDER);
/*     */   private final Http2HeaderTable headerTable;
/*     */   
/*     */   public DefaultHttp2HeadersEncoder() {
/*  45 */     this(4096, Collections.emptySet());
/*     */   }
/*     */   
/*     */   public DefaultHttp2HeadersEncoder(int maxHeaderTableSize, Set<String> sensitiveHeaders) {
/*  49 */     this.encoder = new Encoder(maxHeaderTableSize);
/*  50 */     this.sensitiveHeaders.addAll(sensitiveHeaders);
/*  51 */     this.headerTable = new Http2HeaderTableEncoder(null);
/*     */   }
/*     */   
/*     */   public void encodeHeaders(Http2Headers headers, ByteBuf buffer) throws Http2Exception
/*     */   {
/*  56 */     final OutputStream stream = new ByteBufOutputStream(buffer);
/*     */     try {
/*  58 */       if (headers.size() > this.headerTable.maxHeaderListSize()) {
/*  59 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Number of headers (%d) exceeds maxHeaderListSize (%d)", new Object[] { Integer.valueOf(headers.size()), Integer.valueOf(this.headerTable.maxHeaderListSize()) });
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  65 */       if (this.tableSizeChangeOutput.size() > 0) {
/*  66 */         buffer.writeBytes(this.tableSizeChangeOutput.toByteArray());
/*  67 */         this.tableSizeChangeOutput.reset();
/*     */       }
/*     */       
/*     */ 
/*  71 */       for (Http2Headers.PseudoHeaderName pseudoHeader : Http2Headers.PseudoHeaderName.values()) {
/*  72 */         AsciiString name = pseudoHeader.value();
/*  73 */         AsciiString value = (AsciiString)headers.get(name);
/*  74 */         if (value != null) {
/*  75 */           encodeHeader(name, value, stream);
/*     */         }
/*     */       }
/*     */       
/*  79 */       headers.forEachEntry(new BinaryHeaders.EntryVisitor()
/*     */       {
/*     */         public boolean visit(Map.Entry<AsciiString, AsciiString> entry) throws Exception {
/*  82 */           AsciiString name = (AsciiString)entry.getKey();
/*  83 */           AsciiString value = (AsciiString)entry.getValue();
/*  84 */           if (!Http2Headers.PseudoHeaderName.isPseudoHeader(name)) {
/*  85 */             DefaultHttp2HeadersEncoder.this.encodeHeader(name, value, stream);
/*     */           }
/*  87 */           return true;
/*     */         }
/*     */       }); return;
/*     */     } catch (Http2Exception e) {
/*  91 */       throw e;
/*     */     } catch (Throwable t) {
/*  93 */       throw Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, t, "Failed encoding headers block: %s", new Object[] { t.getMessage() });
/*     */     } finally {
/*     */       try {
/*  96 */         stream.close();
/*     */       } catch (IOException e) {
/*  98 */         throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, e, e.getMessage(), new Object[0]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Http2HeaderTable headerTable()
/*     */   {
/* 105 */     return this.headerTable;
/*     */   }
/*     */   
/*     */   public Http2HeadersEncoder.Configuration configuration()
/*     */   {
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   private void encodeHeader(AsciiString key, AsciiString value, OutputStream stream) throws IOException {
/* 114 */     boolean sensitive = this.sensitiveHeaders.contains(key.toString());
/* 115 */     this.encoder.encodeHeader(stream, key.array(), value.array(), sensitive);
/*     */   }
/*     */   
/*     */   private final class Http2HeaderTableEncoder extends DefaultHttp2HeaderTableListSize implements Http2HeaderTable
/*     */   {
/*     */     private Http2HeaderTableEncoder() {}
/*     */     
/*     */     public void maxHeaderTableSize(int max) throws Http2Exception
/*     */     {
/* 124 */       if (max < 0) {
/* 125 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Header Table Size must be non-negative but was %d", new Object[] { Integer.valueOf(max) });
/*     */       }
/*     */       try
/*     */       {
/* 129 */         DefaultHttp2HeadersEncoder.this.encoder.setMaxHeaderTableSize(DefaultHttp2HeadersEncoder.this.tableSizeChangeOutput, max);
/*     */       } catch (IOException e) {
/* 131 */         throw new Http2Exception(Http2Error.COMPRESSION_ERROR, e.getMessage(), e);
/*     */       } catch (Throwable t) {
/* 133 */         throw new Http2Exception(Http2Error.PROTOCOL_ERROR, t.getMessage(), t);
/*     */       }
/*     */     }
/*     */     
/*     */     public int maxHeaderTableSize()
/*     */     {
/* 139 */       return DefaultHttp2HeadersEncoder.this.encoder.getMaxHeaderTableSize();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\DefaultHttp2HeadersEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */