/*    */ package ch.qos.logback.core.net;
/*    */ 
/*    */ import ch.qos.logback.core.net.ssl.ConfigurableSSLSocketFactory;
/*    */ import ch.qos.logback.core.net.ssl.SSLComponent;
/*    */ import ch.qos.logback.core.net.ssl.SSLConfiguration;
/*    */ import ch.qos.logback.core.net.ssl.SSLParametersConfiguration;
/*    */ import javax.net.SocketFactory;
/*    */ import javax.net.ssl.SSLContext;
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
/*    */ public abstract class AbstractSSLSocketAppender<E>
/*    */   extends AbstractSocketAppender<E>
/*    */   implements SSLComponent
/*    */ {
/*    */   private SSLConfiguration ssl;
/*    */   private SocketFactory socketFactory;
/*    */   
/*    */   protected SocketFactory getSocketFactory()
/*    */   {
/* 49 */     return this.socketFactory;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void start()
/*    */   {
/*    */     try
/*    */     {
/* 58 */       SSLContext sslContext = getSsl().createContext(this);
/* 59 */       SSLParametersConfiguration parameters = getSsl().getParameters();
/* 60 */       parameters.setContext(getContext());
/* 61 */       this.socketFactory = new ConfigurableSSLSocketFactory(parameters, sslContext.getSocketFactory());
/*    */       
/* 63 */       super.start();
/*    */     }
/*    */     catch (Exception ex) {
/* 66 */       addError(ex.getMessage(), ex);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SSLConfiguration getSsl()
/*    */   {
/* 76 */     if (this.ssl == null) {
/* 77 */       this.ssl = new SSLConfiguration();
/*    */     }
/* 79 */     return this.ssl;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setSsl(SSLConfiguration ssl)
/*    */   {
/* 87 */     this.ssl = ssl;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\AbstractSSLSocketAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */