/*     */ package ch.qos.logback.core.net;
/*     */ 
/*     */ import ch.qos.logback.core.util.DelayStrategy;
/*     */ import ch.qos.logback.core.util.FixedDelay;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultSocketConnector
/*     */   implements SocketConnector
/*     */ {
/*     */   private final InetAddress address;
/*     */   private final int port;
/*     */   private final DelayStrategy delayStrategy;
/*     */   private SocketConnector.ExceptionHandler exceptionHandler;
/*     */   private SocketFactory socketFactory;
/*     */   
/*     */   public DefaultSocketConnector(InetAddress address, int port, long initialDelay, long retryDelay)
/*     */   {
/*  50 */     this(address, port, new FixedDelay(initialDelay, retryDelay));
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
/*     */   public DefaultSocketConnector(InetAddress address, int port, DelayStrategy delayStrategy)
/*     */   {
/*  63 */     this.address = address;
/*  64 */     this.port = port;
/*  65 */     this.delayStrategy = delayStrategy;
/*     */   }
/*     */   
/*     */ 
/*     */   public Socket call()
/*     */     throws InterruptedException
/*     */   {
/*  72 */     useDefaultsForMissingFields();
/*  73 */     Socket socket = createSocket();
/*  74 */     while ((socket == null) && (!Thread.currentThread().isInterrupted())) {
/*  75 */       Thread.sleep(this.delayStrategy.nextDelay());
/*  76 */       socket = createSocket();
/*     */     }
/*  78 */     return socket;
/*     */   }
/*     */   
/*     */   private Socket createSocket() {
/*  82 */     Socket newSocket = null;
/*     */     try {
/*  84 */       newSocket = this.socketFactory.createSocket(this.address, this.port);
/*     */     } catch (IOException ioex) {
/*  86 */       this.exceptionHandler.connectionFailed(this, ioex);
/*     */     }
/*  88 */     return newSocket;
/*     */   }
/*     */   
/*     */   private void useDefaultsForMissingFields() {
/*  92 */     if (this.exceptionHandler == null) {
/*  93 */       this.exceptionHandler = new ConsoleExceptionHandler(null);
/*     */     }
/*  95 */     if (this.socketFactory == null) {
/*  96 */       this.socketFactory = SocketFactory.getDefault();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setExceptionHandler(SocketConnector.ExceptionHandler exceptionHandler)
/*     */   {
/* 104 */     this.exceptionHandler = exceptionHandler;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setSocketFactory(SocketFactory socketFactory)
/*     */   {
/* 111 */     this.socketFactory = socketFactory;
/*     */   }
/*     */   
/*     */ 
/*     */   private static class ConsoleExceptionHandler
/*     */     implements SocketConnector.ExceptionHandler
/*     */   {
/*     */     public void connectionFailed(SocketConnector connector, Exception ex)
/*     */     {
/* 120 */       System.out.println(ex);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\DefaultSocketConnector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */