/*    */ package ch.qos.logback.classic.net;
/*    */ 
/*    */ import ch.qos.logback.classic.LoggerContext;
/*    */ import ch.qos.logback.core.net.ssl.ConfigurableSSLServerSocketFactory;
/*    */ import ch.qos.logback.core.net.ssl.SSLParametersConfiguration;
/*    */ import java.security.NoSuchAlgorithmException;
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
/*    */ public class SimpleSSLSocketServer
/*    */   extends SimpleSocketServer
/*    */ {
/*    */   private final ServerSocketFactory socketFactory;
/*    */   
/*    */   public static void main(String[] argv)
/*    */     throws Exception
/*    */   {
/* 62 */     doMain(SimpleSSLSocketServer.class, argv);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SimpleSSLSocketServer(LoggerContext lc, int port)
/*    */     throws NoSuchAlgorithmException
/*    */   {
/* 74 */     this(lc, port, SSLContext.getDefault());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SimpleSSLSocketServer(LoggerContext lc, int port, SSLContext sslContext)
/*    */   {
/* 85 */     super(lc, port);
/* 86 */     if (sslContext == null) {
/* 87 */       throw new NullPointerException("SSL context required");
/*    */     }
/* 89 */     SSLParametersConfiguration parameters = new SSLParametersConfiguration();
/*    */     
/* 91 */     parameters.setContext(lc);
/* 92 */     this.socketFactory = new ConfigurableSSLServerSocketFactory(parameters, sslContext.getServerSocketFactory());
/*    */   }
/*    */   
/*    */ 
/*    */   protected ServerSocketFactory getServerSocketFactory()
/*    */   {
/* 98 */     return this.socketFactory;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\net\SimpleSSLSocketServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */