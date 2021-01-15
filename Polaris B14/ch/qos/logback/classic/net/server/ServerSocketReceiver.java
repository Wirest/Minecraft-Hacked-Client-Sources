/*     */ package ch.qos.logback.classic.net.server;
/*     */ 
/*     */ import ch.qos.logback.classic.net.ReceiverBase;
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.net.server.ServerListener;
/*     */ import ch.qos.logback.core.net.server.ServerRunner;
/*     */ import ch.qos.logback.core.util.CloseUtil;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.concurrent.Executor;
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
/*     */ public class ServerSocketReceiver
/*     */   extends ReceiverBase
/*     */ {
/*     */   public static final int DEFAULT_BACKLOG = 50;
/*  42 */   private int port = 4560;
/*  43 */   private int backlog = 50;
/*     */   
/*     */   private String address;
/*     */   
/*     */   private ServerSocket serverSocket;
/*     */   
/*     */   private ServerRunner runner;
/*     */   
/*     */   protected boolean shouldStart()
/*     */   {
/*     */     try
/*     */     {
/*  55 */       ServerSocket serverSocket = getServerSocketFactory().createServerSocket(getPort(), getBacklog(), getInetAddress());
/*     */       
/*     */ 
/*  58 */       ServerListener<RemoteAppenderClient> listener = createServerListener(serverSocket);
/*     */       
/*     */ 
/*  61 */       this.runner = createServerRunner(listener, getContext().getExecutorService());
/*  62 */       this.runner.setContext(getContext());
/*  63 */       return true;
/*     */     }
/*     */     catch (Exception ex) {
/*  66 */       addError("server startup error: " + ex, ex);
/*  67 */       CloseUtil.closeQuietly(this.serverSocket); }
/*  68 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   protected ServerListener<RemoteAppenderClient> createServerListener(ServerSocket socket)
/*     */   {
/*  74 */     return new RemoteAppenderServerListener(socket);
/*     */   }
/*     */   
/*     */ 
/*     */   protected ServerRunner createServerRunner(ServerListener<RemoteAppenderClient> listener, Executor executor)
/*     */   {
/*  80 */     return new RemoteAppenderServerRunner(listener, executor);
/*     */   }
/*     */   
/*     */   protected Runnable getRunnableTask()
/*     */   {
/*  85 */     return this.runner;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onStop()
/*     */   {
/*     */     try
/*     */     {
/*  93 */       if (this.runner == null) return;
/*  94 */       this.runner.stop();
/*     */     }
/*     */     catch (IOException ex) {
/*  97 */       addError("server shutdown error: " + ex, ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ServerSocketFactory getServerSocketFactory()
/*     */     throws Exception
/*     */   {
/* 109 */     return ServerSocketFactory.getDefault();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected InetAddress getInetAddress()
/*     */     throws UnknownHostException
/*     */   {
/* 118 */     if (getAddress() == null) return null;
/* 119 */     return InetAddress.getByName(getAddress());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 127 */     return this.port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPort(int port)
/*     */   {
/* 135 */     this.port = port;
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
/* 147 */     return this.backlog;
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
/* 159 */     this.backlog = backlog;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getAddress()
/*     */   {
/* 167 */     return this.address;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAddress(String address)
/*     */   {
/* 175 */     this.address = address;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\net\server\ServerSocketReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */