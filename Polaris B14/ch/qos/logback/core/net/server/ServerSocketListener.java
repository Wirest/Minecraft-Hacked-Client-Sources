/*    */ package ch.qos.logback.core.net.server;
/*    */ 
/*    */ import ch.qos.logback.core.util.CloseUtil;
/*    */ import java.io.IOException;
/*    */ import java.net.ServerSocket;
/*    */ import java.net.Socket;
/*    */ import java.net.SocketAddress;
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
/*    */ public abstract class ServerSocketListener<T extends Client>
/*    */   implements ServerListener<T>
/*    */ {
/*    */   private final ServerSocket serverSocket;
/*    */   
/*    */   public ServerSocketListener(ServerSocket serverSocket)
/*    */   {
/* 38 */     this.serverSocket = serverSocket;
/*    */   }
/*    */   
/*    */ 
/*    */   public T acceptClient()
/*    */     throws IOException
/*    */   {
/* 45 */     Socket socket = this.serverSocket.accept();
/* 46 */     return createClient(socketAddressToString(socket.getRemoteSocketAddress()), socket);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected abstract T createClient(String paramString, Socket paramSocket)
/*    */     throws IOException;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void close()
/*    */   {
/* 64 */     CloseUtil.closeQuietly(this.serverSocket);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 72 */     return socketAddressToString(this.serverSocket.getLocalSocketAddress());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private String socketAddressToString(SocketAddress address)
/*    */   {
/* 81 */     String addr = address.toString();
/* 82 */     int i = addr.indexOf("/");
/* 83 */     if (i >= 0) {
/* 84 */       addr = addr.substring(i + 1);
/*    */     }
/* 86 */     return addr;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\server\ServerSocketListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */