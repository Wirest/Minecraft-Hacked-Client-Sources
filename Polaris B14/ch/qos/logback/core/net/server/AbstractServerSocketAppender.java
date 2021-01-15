/*     */ package ch.qos.logback.core.net.server;
/*     */ 
/*     */ import ch.qos.logback.core.AppenderBase;
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.spi.PreSerializationTransformer;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import javax.net.ServerSocketFactory;
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
/*     */ public abstract class AbstractServerSocketAppender<E>
/*     */   extends AppenderBase<E>
/*     */ {
/*     */   public static final int DEFAULT_BACKLOG = 50;
/*     */   public static final int DEFAULT_CLIENT_QUEUE_SIZE = 100;
/*  48 */   private int port = 4560;
/*  49 */   private int backlog = 50;
/*  50 */   private int clientQueueSize = 100;
/*     */   
/*     */   private String address;
/*     */   
/*     */   private ServerRunner<RemoteReceiverClient> runner;
/*     */   
/*     */   public void start()
/*     */   {
/*  58 */     if (isStarted()) return;
/*     */     try {
/*  60 */       ServerSocket socket = getServerSocketFactory().createServerSocket(getPort(), getBacklog(), getInetAddress());
/*     */       
/*  62 */       ServerListener<RemoteReceiverClient> listener = createServerListener(socket);
/*     */       
/*  64 */       this.runner = createServerRunner(listener, getContext().getExecutorService());
/*  65 */       this.runner.setContext(getContext());
/*  66 */       getContext().getExecutorService().execute(this.runner);
/*  67 */       super.start();
/*     */     } catch (Exception ex) {
/*  69 */       addError("server startup error: " + ex, ex);
/*     */     }
/*     */   }
/*     */   
/*     */   protected ServerListener<RemoteReceiverClient> createServerListener(ServerSocket socket)
/*     */   {
/*  75 */     return new RemoteReceiverServerListener(socket);
/*     */   }
/*     */   
/*     */ 
/*     */   protected ServerRunner<RemoteReceiverClient> createServerRunner(ServerListener<RemoteReceiverClient> listener, Executor executor)
/*     */   {
/*  81 */     return new RemoteReceiverServerRunner(listener, executor, getClientQueueSize());
/*     */   }
/*     */   
/*     */ 
/*     */   public void stop()
/*     */   {
/*  87 */     if (!isStarted()) return;
/*     */     try {
/*  89 */       this.runner.stop();
/*  90 */       super.stop();
/*     */     }
/*     */     catch (IOException ex) {
/*  93 */       addError("server shutdown error: " + ex, ex);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void append(E event)
/*     */   {
/*  99 */     if (event == null) return;
/* 100 */     postProcessEvent(event);
/* 101 */     final Serializable serEvent = getPST().transform(event);
/* 102 */     this.runner.accept(new ClientVisitor() {
/*     */       public void visit(RemoteReceiverClient client) {
/* 104 */         client.offer(serEvent);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void postProcessEvent(E paramE);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract PreSerializationTransformer<E> getPST();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ServerSocketFactory getServerSocketFactory()
/*     */     throws Exception
/*     */   {
/* 132 */     return ServerSocketFactory.getDefault();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected InetAddress getInetAddress()
/*     */     throws UnknownHostException
/*     */   {
/* 141 */     if (getAddress() == null) return null;
/* 142 */     return InetAddress.getByName(getAddress());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 150 */     return this.port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPort(int port)
/*     */   {
/* 158 */     this.port = port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getBacklog()
/*     */   {
/* 170 */     return this.backlog;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBacklog(int backlog)
/*     */   {
/* 182 */     this.backlog = backlog;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getAddress()
/*     */   {
/* 190 */     return this.address;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAddress(String address)
/*     */   {
/* 198 */     this.address = address;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getClientQueueSize()
/*     */   {
/* 206 */     return this.clientQueueSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setClientQueueSize(int clientQueueSize)
/*     */   {
/* 214 */     this.clientQueueSize = clientQueueSize;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\server\AbstractServerSocketAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */