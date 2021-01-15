/*    */ package io.netty.handler.codec.http;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class DefaultHttpMessage
/*    */   extends DefaultHttpObject
/*    */   implements HttpMessage
/*    */ {
/*    */   private static final int HASH_CODE_PRIME = 31;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private HttpVersion version;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private final HttpHeaders headers;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected DefaultHttpMessage(HttpVersion version)
/*    */   {
/* 30 */     this(version, true, false);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected DefaultHttpMessage(HttpVersion version, boolean validateHeaders, boolean singleHeaderFields)
/*    */   {
/* 37 */     if (version == null) {
/* 38 */       throw new NullPointerException("version");
/*    */     }
/* 40 */     this.version = version;
/* 41 */     this.headers = new DefaultHttpHeaders(validateHeaders, singleHeaderFields);
/*    */   }
/*    */   
/*    */   public HttpHeaders headers()
/*    */   {
/* 46 */     return this.headers;
/*    */   }
/*    */   
/*    */   public HttpVersion protocolVersion()
/*    */   {
/* 51 */     return this.version;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 56 */     int result = 1;
/* 57 */     result = 31 * result + this.headers.hashCode();
/* 58 */     result = 31 * result + this.version.hashCode();
/* 59 */     result = 31 * result + super.hashCode();
/* 60 */     return result;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 65 */     if (!(o instanceof DefaultHttpMessage)) {
/* 66 */       return false;
/*    */     }
/*    */     
/* 69 */     DefaultHttpMessage other = (DefaultHttpMessage)o;
/*    */     
/* 71 */     return (headers().equals(other.headers())) && (protocolVersion().equals(other.protocolVersion())) && (super.equals(o));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public HttpMessage setProtocolVersion(HttpVersion version)
/*    */   {
/* 78 */     if (version == null) {
/* 79 */       throw new NullPointerException("version");
/*    */     }
/* 81 */     this.version = version;
/* 82 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\DefaultHttpMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */