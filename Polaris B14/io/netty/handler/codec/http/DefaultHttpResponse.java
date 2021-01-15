/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultHttpResponse
/*     */   extends DefaultHttpMessage
/*     */   implements HttpResponse
/*     */ {
/*     */   private static final int HASH_CODE_PRIME = 31;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private HttpResponseStatus status;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultHttpResponse(HttpVersion version, HttpResponseStatus status)
/*     */   {
/*  32 */     this(version, status, true, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultHttpResponse(HttpVersion version, HttpResponseStatus status, boolean validateHeaders)
/*     */   {
/*  43 */     this(version, status, validateHeaders, false);
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
/*     */   public DefaultHttpResponse(HttpVersion version, HttpResponseStatus status, boolean validateHeaders, boolean singleHeaderFields)
/*     */   {
/*  57 */     super(version, validateHeaders, singleHeaderFields);
/*  58 */     if (status == null) {
/*  59 */       throw new NullPointerException("status");
/*     */     }
/*  61 */     this.status = status;
/*     */   }
/*     */   
/*     */   public HttpResponseStatus status()
/*     */   {
/*  66 */     return this.status;
/*     */   }
/*     */   
/*     */   public HttpResponse setStatus(HttpResponseStatus status)
/*     */   {
/*  71 */     if (status == null) {
/*  72 */       throw new NullPointerException("status");
/*     */     }
/*  74 */     this.status = status;
/*  75 */     return this;
/*     */   }
/*     */   
/*     */   public HttpResponse setProtocolVersion(HttpVersion version)
/*     */   {
/*  80 */     super.setProtocolVersion(version);
/*  81 */     return this;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  86 */     int result = 1;
/*  87 */     result = 31 * result + this.status.hashCode();
/*  88 */     result = 31 * result + super.hashCode();
/*  89 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/*  94 */     if (!(o instanceof DefaultHttpResponse)) {
/*  95 */       return false;
/*     */     }
/*     */     
/*  98 */     DefaultHttpResponse other = (DefaultHttpResponse)o;
/*     */     
/* 100 */     return (status().equals(other.status())) && (super.equals(o));
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 105 */     return HttpMessageUtil.appendResponse(new StringBuilder(256), this).toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\DefaultHttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */