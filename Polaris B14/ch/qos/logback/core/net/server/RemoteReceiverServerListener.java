/*    */ package ch.qos.logback.core.net.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.ServerSocket;
/*    */ import java.net.Socket;
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
/*    */ class RemoteReceiverServerListener
/*    */   extends ServerSocketListener<RemoteReceiverClient>
/*    */ {
/*    */   public RemoteReceiverServerListener(ServerSocket serverSocket)
/*    */   {
/* 35 */     super(serverSocket);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected RemoteReceiverClient createClient(String id, Socket socket)
/*    */     throws IOException
/*    */   {
/* 44 */     return new RemoteReceiverStreamClient(id, socket);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\server\RemoteReceiverServerListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */