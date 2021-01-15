/*    */ package ch.qos.logback.core.net.ssl;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.InetAddress;
/*    */ import java.net.Socket;
/*    */ import java.net.UnknownHostException;
/*    */ import javax.net.SocketFactory;
/*    */ import javax.net.ssl.SSLSocket;
/*    */ import javax.net.ssl.SSLSocketFactory;
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
/*    */ public class ConfigurableSSLSocketFactory
/*    */   extends SocketFactory
/*    */ {
/*    */   private final SSLParametersConfiguration parameters;
/*    */   private final SSLSocketFactory delegate;
/*    */   
/*    */   public ConfigurableSSLSocketFactory(SSLParametersConfiguration parameters, SSLSocketFactory delegate)
/*    */   {
/* 51 */     this.parameters = parameters;
/* 52 */     this.delegate = delegate;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
/*    */     throws IOException
/*    */   {
/* 61 */     SSLSocket socket = (SSLSocket)this.delegate.createSocket(address, port, localAddress, localPort);
/*    */     
/* 63 */     this.parameters.configure(new SSLConfigurableSocket(socket));
/* 64 */     return socket;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Socket createSocket(InetAddress host, int port)
/*    */     throws IOException
/*    */   {
/* 72 */     SSLSocket socket = (SSLSocket)this.delegate.createSocket(host, port);
/* 73 */     this.parameters.configure(new SSLConfigurableSocket(socket));
/* 74 */     return socket;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
/*    */     throws IOException, UnknownHostException
/*    */   {
/* 83 */     SSLSocket socket = (SSLSocket)this.delegate.createSocket(host, port, localHost, localPort);
/*    */     
/* 85 */     this.parameters.configure(new SSLConfigurableSocket(socket));
/* 86 */     return socket;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Socket createSocket(String host, int port)
/*    */     throws IOException, UnknownHostException
/*    */   {
/* 95 */     SSLSocket socket = (SSLSocket)this.delegate.createSocket(host, port);
/* 96 */     this.parameters.configure(new SSLConfigurableSocket(socket));
/* 97 */     return socket;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\ssl\ConfigurableSSLSocketFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */