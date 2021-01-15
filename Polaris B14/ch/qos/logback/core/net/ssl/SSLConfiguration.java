/*    */ package ch.qos.logback.core.net.ssl;
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
/*    */ public class SSLConfiguration
/*    */   extends SSLContextFactoryBean
/*    */ {
/*    */   private SSLParametersConfiguration parameters;
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
/*    */   public SSLParametersConfiguration getParameters()
/*    */   {
/* 34 */     if (this.parameters == null) {
/* 35 */       this.parameters = new SSLParametersConfiguration();
/*    */     }
/* 37 */     return this.parameters;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setParameters(SSLParametersConfiguration parameters)
/*    */   {
/* 45 */     this.parameters = parameters;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\ssl\SSLConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */