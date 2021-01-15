/*    */ package ch.qos.logback.classic.net;
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
/*    */ public class SSLSocketReceiver
/*    */   extends SocketReceiver
/*    */   implements SSLComponent
/*    */ {
/*    */   private SSLConfiguration ssl;
/*    */   private SocketFactory socketFactory;
/*    */   
/*    */   protected SocketFactory getSocketFactory()
/*    */   {
/* 42 */     return this.socketFactory;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected boolean shouldStart()
/*    */   {
/*    */     try
/*    */     {
/* 51 */       SSLContext sslContext = getSsl().createContext(this);
/* 52 */       SSLParametersConfiguration parameters = getSsl().getParameters();
/* 53 */       parameters.setContext(getContext());
/* 54 */       this.socketFactory = new ConfigurableSSLSocketFactory(parameters, sslContext.getSocketFactory());
/*    */       
/* 56 */       return super.shouldStart();
/*    */     }
/*    */     catch (Exception ex) {
/* 59 */       addError(ex.getMessage(), ex); }
/* 60 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SSLConfiguration getSsl()
/*    */   {
/* 70 */     if (this.ssl == null) {
/* 71 */       this.ssl = new SSLConfiguration();
/*    */     }
/* 73 */     return this.ssl;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setSsl(SSLConfiguration ssl)
/*    */   {
/* 81 */     this.ssl = ssl;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\net\SSLSocketReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */