/*     */ package ch.qos.logback.classic.net.server;
/*     */ 
/*     */ import ch.qos.logback.classic.Logger;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.core.util.CloseUtil;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.net.Socket;
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
/*     */ class RemoteAppenderStreamClient
/*     */   implements RemoteAppenderClient
/*     */ {
/*     */   private final String id;
/*     */   private final Socket socket;
/*     */   private final InputStream inputStream;
/*     */   private LoggerContext lc;
/*     */   private Logger logger;
/*     */   
/*     */   public RemoteAppenderStreamClient(String id, Socket socket)
/*     */   {
/*  48 */     this.id = id;
/*  49 */     this.socket = socket;
/*  50 */     this.inputStream = null;
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
/*     */   public RemoteAppenderStreamClient(String id, InputStream inputStream)
/*     */   {
/*  63 */     this.id = id;
/*  64 */     this.socket = null;
/*  65 */     this.inputStream = inputStream;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setLoggerContext(LoggerContext lc)
/*     */   {
/*  72 */     this.lc = lc;
/*  73 */     this.logger = lc.getLogger(getClass().getPackage().getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/*  80 */     if (this.socket == null) return;
/*  81 */     CloseUtil.closeQuietly(this.socket);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/*  88 */     this.logger.info(this + ": connected");
/*  89 */     ObjectInputStream ois = null;
/*     */     try {
/*  91 */       ois = createObjectInputStream();
/*     */       for (;;)
/*     */       {
/*  94 */         ILoggingEvent event = (ILoggingEvent)ois.readObject();
/*     */         
/*     */ 
/*  97 */         Logger remoteLogger = this.lc.getLogger(event.getLoggerName());
/*     */         
/*  99 */         if (remoteLogger.isEnabledFor(event.getLevel()))
/*     */         {
/* 101 */           remoteLogger.callAppenders(event);
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     catch (EOFException ex) {}catch (IOException ex)
/*     */     {
/* 110 */       this.logger.info(this + ": " + ex);
/*     */     }
/*     */     catch (ClassNotFoundException ex) {
/* 113 */       this.logger.error(this + ": unknown event class");
/*     */     }
/*     */     catch (RuntimeException ex) {
/* 116 */       this.logger.error(this + ": " + ex);
/*     */     }
/*     */     finally {
/* 119 */       if (ois != null) {
/* 120 */         CloseUtil.closeQuietly(ois);
/*     */       }
/* 122 */       close();
/* 123 */       this.logger.info(this + ": connection closed");
/*     */     }
/*     */   }
/*     */   
/*     */   private ObjectInputStream createObjectInputStream() throws IOException {
/* 128 */     if (this.inputStream != null) {
/* 129 */       return new ObjectInputStream(this.inputStream);
/*     */     }
/* 131 */     return new ObjectInputStream(this.socket.getInputStream());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 139 */     return "client " + this.id;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\net\server\RemoteAppenderStreamClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */