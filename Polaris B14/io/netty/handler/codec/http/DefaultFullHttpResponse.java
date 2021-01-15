/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
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
/*     */ public class DefaultFullHttpResponse
/*     */   extends DefaultHttpResponse
/*     */   implements FullHttpResponse
/*     */ {
/*     */   private static final int HASH_CODE_PRIME = 31;
/*     */   private final ByteBuf content;
/*     */   private final HttpHeaders trailingHeaders;
/*     */   private final boolean validateHeaders;
/*     */   
/*     */   public DefaultFullHttpResponse(HttpVersion version, HttpResponseStatus status)
/*     */   {
/*  32 */     this(version, status, Unpooled.buffer(0));
/*     */   }
/*     */   
/*     */   public DefaultFullHttpResponse(HttpVersion version, HttpResponseStatus status, ByteBuf content) {
/*  36 */     this(version, status, content, false);
/*     */   }
/*     */   
/*     */   public DefaultFullHttpResponse(HttpVersion version, HttpResponseStatus status, boolean validateHeaders) {
/*  40 */     this(version, status, Unpooled.buffer(0), validateHeaders, false);
/*     */   }
/*     */   
/*     */   public DefaultFullHttpResponse(HttpVersion version, HttpResponseStatus status, boolean validateHeaders, boolean singleFieldHeaders)
/*     */   {
/*  45 */     this(version, status, Unpooled.buffer(0), validateHeaders, singleFieldHeaders);
/*     */   }
/*     */   
/*     */   public DefaultFullHttpResponse(HttpVersion version, HttpResponseStatus status, ByteBuf content, boolean singleFieldHeaders)
/*     */   {
/*  50 */     this(version, status, content, true, singleFieldHeaders);
/*     */   }
/*     */   
/*     */   public DefaultFullHttpResponse(HttpVersion version, HttpResponseStatus status, ByteBuf content, boolean validateHeaders, boolean singleFieldHeaders)
/*     */   {
/*  55 */     super(version, status, validateHeaders, singleFieldHeaders);
/*  56 */     if (content == null) {
/*  57 */       throw new NullPointerException("content");
/*     */     }
/*  59 */     this.content = content;
/*  60 */     this.trailingHeaders = new DefaultHttpHeaders(validateHeaders, singleFieldHeaders);
/*  61 */     this.validateHeaders = validateHeaders;
/*     */   }
/*     */   
/*     */   public HttpHeaders trailingHeaders()
/*     */   {
/*  66 */     return this.trailingHeaders;
/*     */   }
/*     */   
/*     */   public ByteBuf content()
/*     */   {
/*  71 */     return this.content;
/*     */   }
/*     */   
/*     */   public int refCnt()
/*     */   {
/*  76 */     return this.content.refCnt();
/*     */   }
/*     */   
/*     */   public FullHttpResponse retain()
/*     */   {
/*  81 */     this.content.retain();
/*  82 */     return this;
/*     */   }
/*     */   
/*     */   public FullHttpResponse retain(int increment)
/*     */   {
/*  87 */     this.content.retain(increment);
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public FullHttpResponse touch()
/*     */   {
/*  93 */     this.content.touch();
/*  94 */     return this;
/*     */   }
/*     */   
/*     */   public FullHttpResponse touch(Object hint)
/*     */   {
/*  99 */     this.content.touch(hint);
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public boolean release()
/*     */   {
/* 105 */     return this.content.release();
/*     */   }
/*     */   
/*     */   public boolean release(int decrement)
/*     */   {
/* 110 */     return this.content.release(decrement);
/*     */   }
/*     */   
/*     */   public FullHttpResponse setProtocolVersion(HttpVersion version)
/*     */   {
/* 115 */     super.setProtocolVersion(version);
/* 116 */     return this;
/*     */   }
/*     */   
/*     */   public FullHttpResponse setStatus(HttpResponseStatus status)
/*     */   {
/* 121 */     super.setStatus(status);
/* 122 */     return this;
/*     */   }
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
/*     */   private FullHttpResponse copy(boolean copyContent, ByteBuf newContent)
/*     */   {
/* 141 */     DefaultFullHttpResponse copy = new DefaultFullHttpResponse(protocolVersion(), status(), newContent == null ? Unpooled.buffer(0) : copyContent ? content().copy() : newContent);
/*     */     
/*     */ 
/*     */ 
/* 145 */     copy.headers().set(headers());
/* 146 */     copy.trailingHeaders().set(trailingHeaders());
/* 147 */     return copy;
/*     */   }
/*     */   
/*     */   public FullHttpResponse copy(ByteBuf newContent)
/*     */   {
/* 152 */     return copy(false, newContent);
/*     */   }
/*     */   
/*     */   public FullHttpResponse copy()
/*     */   {
/* 157 */     return copy(true, null);
/*     */   }
/*     */   
/*     */   public FullHttpResponse duplicate()
/*     */   {
/* 162 */     DefaultFullHttpResponse duplicate = new DefaultFullHttpResponse(protocolVersion(), status(), content().duplicate(), this.validateHeaders);
/*     */     
/* 164 */     duplicate.headers().set(headers());
/* 165 */     duplicate.trailingHeaders().set(trailingHeaders());
/* 166 */     return duplicate;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 171 */     int result = 1;
/* 172 */     result = 31 * result + content().hashCode();
/* 173 */     result = 31 * result + trailingHeaders().hashCode();
/* 174 */     result = 31 * result + super.hashCode();
/* 175 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 180 */     if (!(o instanceof DefaultFullHttpResponse)) {
/* 181 */       return false;
/*     */     }
/*     */     
/* 184 */     DefaultFullHttpResponse other = (DefaultFullHttpResponse)o;
/*     */     
/* 186 */     return (super.equals(other)) && (content().equals(other.content())) && (trailingHeaders().equals(other.trailingHeaders()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 193 */     return HttpMessageUtil.appendFullResponse(new StringBuilder(256), this).toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\DefaultFullHttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */