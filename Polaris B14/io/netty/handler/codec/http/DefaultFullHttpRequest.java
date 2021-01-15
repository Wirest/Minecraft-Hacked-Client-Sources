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
/*     */ public class DefaultFullHttpRequest
/*     */   extends DefaultHttpRequest
/*     */   implements FullHttpRequest
/*     */ {
/*     */   private static final int HASH_CODE_PRIME = 31;
/*     */   private final ByteBuf content;
/*     */   private final HttpHeaders trailingHeader;
/*     */   private final boolean validateHeaders;
/*     */   
/*     */   public DefaultFullHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri)
/*     */   {
/*  31 */     this(httpVersion, method, uri, Unpooled.buffer(0));
/*     */   }
/*     */   
/*     */   public DefaultFullHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri, ByteBuf content) {
/*  35 */     this(httpVersion, method, uri, content, true);
/*     */   }
/*     */   
/*     */   public DefaultFullHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri, boolean validateHeaders) {
/*  39 */     this(httpVersion, method, uri, Unpooled.buffer(0), true);
/*     */   }
/*     */   
/*     */   public DefaultFullHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri, ByteBuf content, boolean validateHeaders)
/*     */   {
/*  44 */     super(httpVersion, method, uri, validateHeaders);
/*  45 */     if (content == null) {
/*  46 */       throw new NullPointerException("content");
/*     */     }
/*  48 */     this.content = content;
/*  49 */     this.trailingHeader = new DefaultHttpHeaders(validateHeaders);
/*  50 */     this.validateHeaders = validateHeaders;
/*     */   }
/*     */   
/*     */   public HttpHeaders trailingHeaders()
/*     */   {
/*  55 */     return this.trailingHeader;
/*     */   }
/*     */   
/*     */   public ByteBuf content()
/*     */   {
/*  60 */     return this.content;
/*     */   }
/*     */   
/*     */   public int refCnt()
/*     */   {
/*  65 */     return this.content.refCnt();
/*     */   }
/*     */   
/*     */   public FullHttpRequest retain()
/*     */   {
/*  70 */     this.content.retain();
/*  71 */     return this;
/*     */   }
/*     */   
/*     */   public FullHttpRequest retain(int increment)
/*     */   {
/*  76 */     this.content.retain(increment);
/*  77 */     return this;
/*     */   }
/*     */   
/*     */   public FullHttpRequest touch()
/*     */   {
/*  82 */     this.content.touch();
/*  83 */     return this;
/*     */   }
/*     */   
/*     */   public FullHttpRequest touch(Object hint)
/*     */   {
/*  88 */     this.content.touch(hint);
/*  89 */     return this;
/*     */   }
/*     */   
/*     */   public boolean release()
/*     */   {
/*  94 */     return this.content.release();
/*     */   }
/*     */   
/*     */   public boolean release(int decrement)
/*     */   {
/*  99 */     return this.content.release(decrement);
/*     */   }
/*     */   
/*     */   public FullHttpRequest setProtocolVersion(HttpVersion version)
/*     */   {
/* 104 */     super.setProtocolVersion(version);
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   public FullHttpRequest setMethod(HttpMethod method)
/*     */   {
/* 110 */     super.setMethod(method);
/* 111 */     return this;
/*     */   }
/*     */   
/*     */   public FullHttpRequest setUri(String uri)
/*     */   {
/* 116 */     super.setUri(uri);
/* 117 */     return this;
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
/*     */   private FullHttpRequest copy(boolean copyContent, ByteBuf newContent)
/*     */   {
/* 136 */     DefaultFullHttpRequest copy = new DefaultFullHttpRequest(protocolVersion(), method(), uri(), newContent == null ? Unpooled.buffer(0) : copyContent ? content().copy() : newContent);
/*     */     
/*     */ 
/*     */ 
/* 140 */     copy.headers().set(headers());
/* 141 */     copy.trailingHeaders().set(trailingHeaders());
/* 142 */     return copy;
/*     */   }
/*     */   
/*     */   public FullHttpRequest copy(ByteBuf newContent)
/*     */   {
/* 147 */     return copy(false, newContent);
/*     */   }
/*     */   
/*     */   public FullHttpRequest copy()
/*     */   {
/* 152 */     return copy(true, null);
/*     */   }
/*     */   
/*     */   public FullHttpRequest duplicate()
/*     */   {
/* 157 */     DefaultFullHttpRequest duplicate = new DefaultFullHttpRequest(protocolVersion(), method(), uri(), content().duplicate(), this.validateHeaders);
/*     */     
/* 159 */     duplicate.headers().set(headers());
/* 160 */     duplicate.trailingHeaders().set(trailingHeaders());
/* 161 */     return duplicate;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 166 */     int result = 1;
/* 167 */     result = 31 * result + content().hashCode();
/* 168 */     result = 31 * result + trailingHeaders().hashCode();
/* 169 */     result = 31 * result + super.hashCode();
/* 170 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 175 */     if (!(o instanceof DefaultFullHttpRequest)) {
/* 176 */       return false;
/*     */     }
/*     */     
/* 179 */     DefaultFullHttpRequest other = (DefaultFullHttpRequest)o;
/*     */     
/* 181 */     return (super.equals(other)) && (content().equals(other.content())) && (trailingHeaders().equals(other.trailingHeaders()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 188 */     return HttpMessageUtil.appendFullRequest(new StringBuilder(256), this).toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\DefaultFullHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */