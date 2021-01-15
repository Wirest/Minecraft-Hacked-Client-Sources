/*    */ package ch.qos.logback.core.net.ssl;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.InetAddress;
/*    */ import java.net.ServerSocket;
/*    */ import javax.net.ServerSocketFactory;
/*    */ import javax.net.ssl.SSLServerSocket;
/*    */ import javax.net.ssl.SSLServerSocketFactory;
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
/*    */ public class ConfigurableSSLServerSocketFactory
/*    */   extends ServerSocketFactory
/*    */ {
/*    */   private final SSLParametersConfiguration parameters;
/*    */   private final SSLServerSocketFactory delegate;
/*    */   
/*    */   public ConfigurableSSLServerSocketFactory(SSLParametersConfiguration parameters, SSLServerSocketFactory delegate)
/*    */   {
/* 50 */     this.parameters = parameters;
/* 51 */     this.delegate = delegate;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ServerSocket createServerSocket(int port, int backlog, InetAddress ifAddress)
/*    */     throws IOException
/*    */   {
/* 60 */     SSLServerSocket socket = (SSLServerSocket)this.delegate.createServerSocket(port, backlog, ifAddress);
/*    */     
/* 62 */     this.parameters.configure(new SSLConfigurableServerSocket(socket));
/* 63 */     return socket;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ServerSocket createServerSocket(int port, int backlog)
/*    */     throws IOException
/*    */   {
/* 72 */     SSLServerSocket socket = (SSLServerSocket)this.delegate.createServerSocket(port, backlog);
/*    */     
/* 74 */     this.parameters.configure(new SSLConfigurableServerSocket(socket));
/* 75 */     return socket;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public ServerSocket createServerSocket(int port)
/*    */     throws IOException
/*    */   {
/* 83 */     SSLServerSocket socket = (SSLServerSocket)this.delegate.createServerSocket(port);
/*    */     
/* 85 */     this.parameters.configure(new SSLConfigurableServerSocket(socket));
/* 86 */     return socket;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\ssl\ConfigurableSSLServerSocketFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */