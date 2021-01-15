/*    */ package io.netty.handler.ssl;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import java.util.NoSuchElementException;
/*    */ import javax.net.ssl.SSLSession;
/*    */ import javax.net.ssl.SSLSessionContext;
/*    */ import org.apache.tomcat.jni.SSLContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class OpenSslSessionContext
/*    */   implements SSLSessionContext
/*    */ {
/* 29 */   private static final Enumeration<byte[]> EMPTY = new EmptyEnumeration(null);
/*    */   private final OpenSslSessionStats stats;
/*    */   final long context;
/*    */   
/*    */   OpenSslSessionContext(long context)
/*    */   {
/* 35 */     this.context = context;
/* 36 */     this.stats = new OpenSslSessionStats(context);
/*    */   }
/*    */   
/*    */   public SSLSession getSession(byte[] bytes)
/*    */   {
/* 41 */     if (bytes == null) {
/* 42 */       throw new NullPointerException("bytes");
/*    */     }
/* 44 */     return null;
/*    */   }
/*    */   
/*    */   public Enumeration<byte[]> getIds()
/*    */   {
/* 49 */     return EMPTY;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void setTicketKeys(byte[] keys)
/*    */   {
/* 56 */     if (keys == null) {
/* 57 */       throw new NullPointerException("keys");
/*    */     }
/* 59 */     SSLContext.setSessionTicketKeys(this.context, keys);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract void setSessionCacheEnabled(boolean paramBoolean);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract boolean isSessionCacheEnabled();
/*    */   
/*    */ 
/*    */ 
/*    */   public OpenSslSessionStats stats()
/*    */   {
/* 76 */     return this.stats;
/*    */   }
/*    */   
/*    */   private static final class EmptyEnumeration implements Enumeration<byte[]>
/*    */   {
/*    */     public boolean hasMoreElements() {
/* 82 */       return false;
/*    */     }
/*    */     
/*    */     public byte[] nextElement()
/*    */     {
/* 87 */       throw new NoSuchElementException();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\OpenSslSessionContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */