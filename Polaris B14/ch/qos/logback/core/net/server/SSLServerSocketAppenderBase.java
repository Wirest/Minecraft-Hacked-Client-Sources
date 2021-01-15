/*    */ package ch.qos.logback.core.net.server;
/*    */ 
/*    */ import ch.qos.logback.core.net.ssl.ConfigurableSSLServerSocketFactory;
/*    */ import ch.qos.logback.core.net.ssl.SSLComponent;
/*    */ import ch.qos.logback.core.net.ssl.SSLConfiguration;
/*    */ import ch.qos.logback.core.net.ssl.SSLParametersConfiguration;
/*    */ import javax.net.ServerSocketFactory;
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
/*    */ public abstract class SSLServerSocketAppenderBase<E>
/*    */   extends AbstractServerSocketAppender<E>
/*    */   implements SSLComponent
/*    */ {
/*    */   private SSLConfiguration ssl;
/*    */   private ServerSocketFactory socketFactory;
/*    */   
/*    */   protected ServerSocketFactory getServerSocketFactory()
/*    */   {
/* 39 */     return this.socketFactory;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void start()
/*    */   {
/*    */     try
/*    */     {
/* 48 */       SSLContext sslContext = getSsl().createContext(this);
/* 49 */       SSLParametersConfiguration parameters = getSsl().getParameters();
/* 50 */       parameters.setContext(getContext());
/* 51 */       this.socketFactory = new ConfigurableSSLServerSocketFactory(parameters, sslContext.getServerSocketFactory());
/*    */       
/* 53 */       super.start();
/*    */     }
/*    */     catch (Exception ex) {
/* 56 */       addError(ex.getMessage(), ex);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SSLConfiguration getSsl()
/*    */   {
/* 66 */     if (this.ssl == null) {
/* 67 */       this.ssl = new SSLConfiguration();
/*    */     }
/* 69 */     return this.ssl;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setSsl(SSLConfiguration ssl)
/*    */   {
/* 77 */     this.ssl = ssl;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\server\SSLServerSocketAppenderBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */