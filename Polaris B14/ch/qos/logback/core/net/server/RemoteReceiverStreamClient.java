/*     */ package ch.qos.logback.core.net.server;
/*     */ 
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.util.CloseUtil;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class RemoteReceiverStreamClient
/*     */   extends ContextAwareBase
/*     */   implements RemoteReceiverClient
/*     */ {
/*     */   private final String clientId;
/*     */   private final Socket socket;
/*     */   private final OutputStream outputStream;
/*     */   private BlockingQueue<Serializable> queue;
/*     */   
/*     */   public RemoteReceiverStreamClient(String id, Socket socket)
/*     */   {
/*  49 */     this.clientId = ("client " + id + ": ");
/*  50 */     this.socket = socket;
/*  51 */     this.outputStream = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   RemoteReceiverStreamClient(String id, OutputStream outputStream)
/*     */   {
/*  64 */     this.clientId = ("client " + id + ": ");
/*  65 */     this.socket = null;
/*  66 */     this.outputStream = outputStream;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setQueue(BlockingQueue<Serializable> queue)
/*     */   {
/*  73 */     this.queue = queue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean offer(Serializable event)
/*     */   {
/*  80 */     if (this.queue == null) {
/*  81 */       throw new IllegalStateException("client has no event queue");
/*     */     }
/*  83 */     return this.queue.offer(event);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/*  90 */     if (this.socket == null) return;
/*  91 */     CloseUtil.closeQuietly(this.socket);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/*  98 */     addInfo(this.clientId + "connected");
/*     */     
/* 100 */     ObjectOutputStream oos = null;
/*     */     try {
/* 102 */       int counter = 0;
/* 103 */       oos = createObjectOutputStream();
/* 104 */       while (!Thread.currentThread().isInterrupted()) {
/*     */         try {
/* 106 */           Serializable event = (Serializable)this.queue.take();
/* 107 */           oos.writeObject(event);
/* 108 */           oos.flush();
/* 109 */           counter++; if (counter >= 70)
/*     */           {
/*     */ 
/* 112 */             counter = 0;
/* 113 */             oos.reset();
/*     */           }
/*     */         }
/*     */         catch (InterruptedException ex) {
/* 117 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (SocketException ex) {
/* 122 */       addInfo(this.clientId + ex);
/*     */     }
/*     */     catch (IOException ex) {
/* 125 */       addError(this.clientId + ex);
/*     */     }
/*     */     catch (RuntimeException ex) {
/* 128 */       addError(this.clientId + ex);
/*     */     }
/*     */     finally {
/* 131 */       if (oos != null) {
/* 132 */         CloseUtil.closeQuietly(oos);
/*     */       }
/* 134 */       close();
/* 135 */       addInfo(this.clientId + "connection closed");
/*     */     }
/*     */   }
/*     */   
/*     */   private ObjectOutputStream createObjectOutputStream() throws IOException {
/* 140 */     if (this.socket == null) {
/* 141 */       return new ObjectOutputStream(this.outputStream);
/*     */     }
/* 143 */     return new ObjectOutputStream(this.socket.getOutputStream());
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\server\RemoteReceiverStreamClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */