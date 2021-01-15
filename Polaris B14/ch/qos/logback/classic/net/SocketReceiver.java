/*     */ package ch.qos.logback.classic.net;
/*     */ 
/*     */ import ch.qos.logback.classic.Logger;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.net.DefaultSocketConnector;
/*     */ import ch.qos.logback.core.net.SocketConnector;
/*     */ import ch.qos.logback.core.net.SocketConnector.ExceptionHandler;
/*     */ import ch.qos.logback.core.util.CloseUtil;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.net.ConnectException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import javax.net.SocketFactory;
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
/*     */ public class SocketReceiver
/*     */   extends ReceiverBase
/*     */   implements Runnable, SocketConnector.ExceptionHandler
/*     */ {
/*     */   private static final int DEFAULT_ACCEPT_CONNECTION_DELAY = 5000;
/*     */   private String remoteHost;
/*     */   private InetAddress address;
/*     */   private int port;
/*     */   private int reconnectionDelay;
/*  52 */   private int acceptConnectionTimeout = 5000;
/*     */   
/*     */   private String receiverId;
/*     */   
/*     */   private volatile Socket socket;
/*     */   
/*     */   private Future<Socket> connectorTask;
/*     */   
/*     */   protected boolean shouldStart()
/*     */   {
/*  62 */     int errorCount = 0;
/*  63 */     if (this.port == 0) {
/*  64 */       errorCount++;
/*  65 */       addError("No port was configured for receiver. For more information, please visit http://logback.qos.ch/codes.html#receiver_no_port");
/*     */     }
/*     */     
/*     */ 
/*  69 */     if (this.remoteHost == null) {
/*  70 */       errorCount++;
/*  71 */       addError("No host name or address was configured for receiver. For more information, please visit http://logback.qos.ch/codes.html#receiver_no_host");
/*     */     }
/*     */     
/*     */ 
/*  75 */     if (this.reconnectionDelay == 0) {
/*  76 */       this.reconnectionDelay = 30000;
/*     */     }
/*     */     
/*  79 */     if (errorCount == 0) {
/*     */       try {
/*  81 */         this.address = InetAddress.getByName(this.remoteHost);
/*     */       } catch (UnknownHostException ex) {
/*  83 */         addError("unknown host: " + this.remoteHost);
/*  84 */         errorCount++;
/*     */       }
/*     */     }
/*     */     
/*  88 */     if (errorCount == 0) {
/*  89 */       this.receiverId = ("receiver " + this.remoteHost + ":" + this.port + ": ");
/*     */     }
/*     */     
/*  92 */     return errorCount == 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void onStop()
/*     */   {
/*  99 */     if (this.socket != null) {
/* 100 */       CloseUtil.closeQuietly(this.socket);
/*     */     }
/*     */   }
/*     */   
/*     */   protected Runnable getRunnableTask()
/*     */   {
/* 106 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/* 114 */       LoggerContext lc = (LoggerContext)getContext();
/* 115 */       while (!Thread.currentThread().isInterrupted()) {
/* 116 */         SocketConnector connector = createConnector(this.address, this.port, 0, this.reconnectionDelay);
/*     */         
/* 118 */         this.connectorTask = activateConnector(connector);
/* 119 */         if (this.connectorTask == null)
/*     */           break;
/* 121 */         this.socket = waitForConnectorToReturnASocket();
/* 122 */         if (this.socket == null)
/*     */           break;
/* 124 */         dispatchEvents(lc);
/*     */       }
/*     */     }
/*     */     catch (InterruptedException ex) {}
/*     */     
/* 129 */     addInfo("shutting down");
/*     */   }
/*     */   
/*     */   private SocketConnector createConnector(InetAddress address, int port, int initialDelay, int retryDelay)
/*     */   {
/* 134 */     SocketConnector connector = newConnector(address, port, initialDelay, retryDelay);
/*     */     
/* 136 */     connector.setExceptionHandler(this);
/* 137 */     connector.setSocketFactory(getSocketFactory());
/* 138 */     return connector;
/*     */   }
/*     */   
/*     */   private Future<Socket> activateConnector(SocketConnector connector)
/*     */   {
/*     */     try {
/* 144 */       return getContext().getExecutorService().submit(connector);
/*     */     } catch (RejectedExecutionException ex) {}
/* 146 */     return null;
/*     */   }
/*     */   
/*     */   private Socket waitForConnectorToReturnASocket() throws InterruptedException
/*     */   {
/*     */     try {
/* 152 */       Socket s = (Socket)this.connectorTask.get();
/* 153 */       this.connectorTask = null;
/* 154 */       return s;
/*     */     } catch (ExecutionException e) {}
/* 156 */     return null;
/*     */   }
/*     */   
/*     */   private void dispatchEvents(LoggerContext lc)
/*     */   {
/*     */     try {
/* 162 */       this.socket.setSoTimeout(this.acceptConnectionTimeout);
/* 163 */       ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
/* 164 */       this.socket.setSoTimeout(0);
/* 165 */       addInfo(this.receiverId + "connection established");
/*     */       for (;;) {
/* 167 */         ILoggingEvent event = (ILoggingEvent)ois.readObject();
/* 168 */         Logger remoteLogger = lc.getLogger(event.getLoggerName());
/* 169 */         if (remoteLogger.isEnabledFor(event.getLevel())) {
/* 170 */           remoteLogger.callAppenders(event);
/*     */         }
/*     */       }
/*     */     } catch (EOFException ex) {
/* 174 */       addInfo(this.receiverId + "end-of-stream detected");
/*     */     } catch (IOException ex) {
/* 176 */       addInfo(this.receiverId + "connection failed: " + ex);
/*     */     } catch (ClassNotFoundException ex) {
/* 178 */       addInfo(this.receiverId + "unknown event class: " + ex);
/*     */     } finally {
/* 180 */       CloseUtil.closeQuietly(this.socket);
/* 181 */       this.socket = null;
/* 182 */       addInfo(this.receiverId + "connection closed");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void connectionFailed(SocketConnector connector, Exception ex)
/*     */   {
/* 190 */     if ((ex instanceof InterruptedException)) {
/* 191 */       addInfo("connector interrupted");
/* 192 */     } else if ((ex instanceof ConnectException)) {
/* 193 */       addInfo(this.receiverId + "connection refused");
/*     */     } else {
/* 195 */       addInfo(this.receiverId + ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected SocketConnector newConnector(InetAddress address, int port, int initialDelay, int retryDelay)
/*     */   {
/* 202 */     return new DefaultSocketConnector(address, port, initialDelay, retryDelay);
/*     */   }
/*     */   
/*     */   protected SocketFactory getSocketFactory() {
/* 206 */     return SocketFactory.getDefault();
/*     */   }
/*     */   
/*     */   public void setRemoteHost(String remoteHost) {
/* 210 */     this.remoteHost = remoteHost;
/*     */   }
/*     */   
/*     */   public void setPort(int port) {
/* 214 */     this.port = port;
/*     */   }
/*     */   
/*     */   public void setReconnectionDelay(int reconnectionDelay) {
/* 218 */     this.reconnectionDelay = reconnectionDelay;
/*     */   }
/*     */   
/*     */   public void setAcceptConnectionTimeout(int acceptConnectionTimeout) {
/* 222 */     this.acceptConnectionTimeout = acceptConnectionTimeout;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\net\SocketReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */