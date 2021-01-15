/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultHttpRequest
/*     */   extends DefaultHttpMessage
/*     */   implements HttpRequest
/*     */ {
/*     */   private static final int HASH_CODE_PRIME = 31;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private HttpMethod method;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String uri;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri)
/*     */   {
/*  34 */     this(httpVersion, method, uri, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri, boolean validateHeaders)
/*     */   {
/*  46 */     super(httpVersion, validateHeaders, false);
/*  47 */     if (method == null) {
/*  48 */       throw new NullPointerException("method");
/*     */     }
/*  50 */     if (uri == null) {
/*  51 */       throw new NullPointerException("uri");
/*     */     }
/*  53 */     this.method = method;
/*  54 */     this.uri = uri;
/*     */   }
/*     */   
/*     */   public HttpMethod method()
/*     */   {
/*  59 */     return this.method;
/*     */   }
/*     */   
/*     */   public String uri()
/*     */   {
/*  64 */     return this.uri;
/*     */   }
/*     */   
/*     */   public HttpRequest setMethod(HttpMethod method)
/*     */   {
/*  69 */     if (method == null) {
/*  70 */       throw new NullPointerException("method");
/*     */     }
/*  72 */     this.method = method;
/*  73 */     return this;
/*     */   }
/*     */   
/*     */   public HttpRequest setUri(String uri)
/*     */   {
/*  78 */     if (uri == null) {
/*  79 */       throw new NullPointerException("uri");
/*     */     }
/*  81 */     this.uri = uri;
/*  82 */     return this;
/*     */   }
/*     */   
/*     */   public HttpRequest setProtocolVersion(HttpVersion version)
/*     */   {
/*  87 */     super.setProtocolVersion(version);
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  93 */     int result = 1;
/*  94 */     result = 31 * result + this.method.hashCode();
/*  95 */     result = 31 * result + this.uri.hashCode();
/*  96 */     result = 31 * result + super.hashCode();
/*  97 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 102 */     if (!(o instanceof DefaultHttpRequest)) {
/* 103 */       return false;
/*     */     }
/*     */     
/* 106 */     DefaultHttpRequest other = (DefaultHttpRequest)o;
/*     */     
/* 108 */     return (method().equals(other.method())) && (uri().equalsIgnoreCase(other.uri())) && (super.equals(o));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 115 */     return HttpMessageUtil.appendRequest(new StringBuilder(256), this).toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\DefaultHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */