/*    */ package ch.qos.logback.core.net.ssl;
/*    */ 
/*    */ import javax.net.ssl.SSLSocket;
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
/*    */ public class SSLConfigurableSocket
/*    */   implements SSLConfigurable
/*    */ {
/*    */   private final SSLSocket delegate;
/*    */   
/*    */   public SSLConfigurableSocket(SSLSocket delegate)
/*    */   {
/* 28 */     this.delegate = delegate;
/*    */   }
/*    */   
/*    */   public String[] getDefaultProtocols() {
/* 32 */     return this.delegate.getEnabledProtocols();
/*    */   }
/*    */   
/*    */   public String[] getSupportedProtocols() {
/* 36 */     return this.delegate.getSupportedProtocols();
/*    */   }
/*    */   
/*    */   public void setEnabledProtocols(String[] protocols) {
/* 40 */     this.delegate.setEnabledProtocols(protocols);
/*    */   }
/*    */   
/*    */   public String[] getDefaultCipherSuites() {
/* 44 */     return this.delegate.getEnabledCipherSuites();
/*    */   }
/*    */   
/*    */   public String[] getSupportedCipherSuites() {
/* 48 */     return this.delegate.getSupportedCipherSuites();
/*    */   }
/*    */   
/*    */   public void setEnabledCipherSuites(String[] suites) {
/* 52 */     this.delegate.setEnabledCipherSuites(suites);
/*    */   }
/*    */   
/*    */   public void setNeedClientAuth(boolean state) {
/* 56 */     this.delegate.setNeedClientAuth(state);
/*    */   }
/*    */   
/*    */   public void setWantClientAuth(boolean state) {
/* 60 */     this.delegate.setWantClientAuth(state);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\ssl\SSLConfigurableSocket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */