/*    */ package ch.qos.logback.core.net.server;
/*    */ 
/*    */ import java.util.concurrent.ArrayBlockingQueue;
/*    */ import java.util.concurrent.Executor;
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
/*    */ class RemoteReceiverServerRunner
/*    */   extends ConcurrentServerRunner<RemoteReceiverClient>
/*    */ {
/*    */   private final int clientQueueSize;
/*    */   
/*    */   public RemoteReceiverServerRunner(ServerListener<RemoteReceiverClient> listener, Executor executor, int clientQueueSize)
/*    */   {
/* 43 */     super(listener, executor);
/* 44 */     this.clientQueueSize = clientQueueSize;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected boolean configureClient(RemoteReceiverClient client)
/*    */   {
/* 52 */     client.setContext(getContext());
/* 53 */     client.setQueue(new ArrayBlockingQueue(this.clientQueueSize));
/* 54 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\server\RemoteReceiverServerRunner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */