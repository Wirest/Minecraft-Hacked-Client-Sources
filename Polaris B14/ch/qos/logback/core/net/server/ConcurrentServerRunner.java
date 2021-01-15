/*     */ package ch.qos.logback.core.net.server;
/*     */ 
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
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
/*     */ public abstract class ConcurrentServerRunner<T extends Client>
/*     */   extends ContextAwareBase
/*     */   implements Runnable, ServerRunner<T>
/*     */ {
/*  51 */   private final Lock clientsLock = new ReentrantLock();
/*     */   
/*  53 */   private final Collection<T> clients = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */   private final ServerListener<T> listener;
/*     */   
/*     */ 
/*     */ 
/*     */   private final Executor executor;
/*     */   
/*     */ 
/*     */   private boolean running;
/*     */   
/*     */ 
/*     */ 
/*     */   public ConcurrentServerRunner(ServerListener<T> listener, Executor executor)
/*     */   {
/*  70 */     this.listener = listener;
/*  71 */     this.executor = executor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isRunning()
/*     */   {
/*  78 */     return this.running;
/*     */   }
/*     */   
/*     */   protected void setRunning(boolean running) {
/*  82 */     this.running = running;
/*     */   }
/*     */   
/*     */ 
/*     */   public void stop()
/*     */     throws IOException
/*     */   {
/*  89 */     this.listener.close();
/*  90 */     accept(new ClientVisitor() {
/*     */       public void visit(T client) {
/*  92 */         client.close();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void accept(ClientVisitor<T> visitor)
/*     */   {
/* 101 */     Collection<T> clients = copyClients();
/* 102 */     for (T client : clients) {
/*     */       try {
/* 104 */         visitor.visit(client);
/*     */       }
/*     */       catch (RuntimeException ex) {
/* 107 */         addError(client + ": " + ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Collection<T> copyClients()
/*     */   {
/* 118 */     this.clientsLock.lock();
/*     */     try {
/* 120 */       Collection<T> copy = new ArrayList(this.clients);
/* 121 */       return copy;
/*     */     }
/*     */     finally {
/* 124 */       this.clientsLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/* 132 */     setRunning(true);
/*     */     try {
/* 134 */       addInfo("listening on " + this.listener);
/* 135 */       while (!Thread.currentThread().isInterrupted()) {
/* 136 */         T client = this.listener.acceptClient();
/* 137 */         if (!configureClient(client)) {
/* 138 */           addError(client + ": connection dropped");
/* 139 */           client.close();
/*     */         }
/*     */         else {
/*     */           try {
/* 143 */             this.executor.execute(new ClientWrapper(client));
/*     */           }
/*     */           catch (RejectedExecutionException ex) {
/* 146 */             addError(client + ": connection dropped");
/* 147 */             client.close();
/*     */           }
/*     */           
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (InterruptedException ex) {}catch (Exception ex)
/*     */     {
/* 155 */       addError("listener: " + ex);
/*     */     }
/*     */     
/* 158 */     setRunning(false);
/* 159 */     addInfo("shutting down");
/* 160 */     this.listener.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean configureClient(T paramT);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addClient(T client)
/*     */   {
/* 180 */     this.clientsLock.lock();
/*     */     try {
/* 182 */       this.clients.add(client);
/*     */     }
/*     */     finally {
/* 185 */       this.clientsLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void removeClient(T client)
/*     */   {
/* 194 */     this.clientsLock.lock();
/*     */     try {
/* 196 */       this.clients.remove(client);
/*     */     }
/*     */     finally {
/* 199 */       this.clientsLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class ClientWrapper
/*     */     implements Client
/*     */   {
/*     */     private final T delegate;
/*     */     
/*     */ 
/*     */     public ClientWrapper()
/*     */     {
/* 212 */       this.delegate = client;
/*     */     }
/*     */     
/*     */     public void run() {
/* 216 */       ConcurrentServerRunner.this.addClient(this.delegate);
/*     */       try {
/* 218 */         this.delegate.run();
/*     */       }
/*     */       finally {
/* 221 */         ConcurrentServerRunner.this.removeClient(this.delegate);
/*     */       }
/*     */     }
/*     */     
/*     */     public void close() {
/* 226 */       this.delegate.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\server\ConcurrentServerRunner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */