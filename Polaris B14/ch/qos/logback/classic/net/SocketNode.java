/*     */ package ch.qos.logback.classic.net;
/*     */ 
/*     */ import ch.qos.logback.classic.Logger;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
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
/*     */ public class SocketNode
/*     */   implements Runnable
/*     */ {
/*     */   Socket socket;
/*     */   LoggerContext context;
/*     */   ObjectInputStream ois;
/*     */   SocketAddress remoteSocketAddress;
/*     */   Logger logger;
/*  51 */   boolean closed = false;
/*     */   SimpleSocketServer socketServer;
/*     */   
/*     */   public SocketNode(SimpleSocketServer socketServer, Socket socket, LoggerContext context) {
/*  55 */     this.socketServer = socketServer;
/*  56 */     this.socket = socket;
/*  57 */     this.remoteSocketAddress = socket.getRemoteSocketAddress();
/*  58 */     this.context = context;
/*  59 */     this.logger = context.getLogger(SocketNode.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/*  71 */       this.ois = new ObjectInputStream(new BufferedInputStream(this.socket.getInputStream()));
/*     */     }
/*     */     catch (Exception e) {
/*  74 */       this.logger.error("Could not open ObjectInputStream to " + this.socket, e);
/*  75 */       this.closed = true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/*  82 */       while (!this.closed)
/*     */       {
/*  84 */         ILoggingEvent event = (ILoggingEvent)this.ois.readObject();
/*     */         
/*     */ 
/*  87 */         Logger remoteLogger = this.context.getLogger(event.getLoggerName());
/*     */         
/*  89 */         if (remoteLogger.isEnabledFor(event.getLevel()))
/*     */         {
/*  91 */           remoteLogger.callAppenders(event);
/*     */         }
/*     */       }
/*     */     } catch (EOFException e) {
/*  95 */       this.logger.info("Caught java.io.EOFException closing connection.");
/*     */     } catch (SocketException e) {
/*  97 */       this.logger.info("Caught java.net.SocketException closing connection.");
/*     */     } catch (IOException e) {
/*  99 */       this.logger.info("Caught java.io.IOException: " + e);
/* 100 */       this.logger.info("Closing connection.");
/*     */     } catch (Exception e) {
/* 102 */       this.logger.error("Unexpected exception. Closing connection.", e);
/*     */     }
/*     */     
/* 105 */     this.socketServer.socketNodeClosing(this);
/* 106 */     close();
/*     */   }
/*     */   
/*     */   void close() {
/* 110 */     if (this.closed) {
/* 111 */       return;
/*     */     }
/* 113 */     this.closed = true;
/* 114 */     if (this.ois != null) {
/*     */       try {
/* 116 */         this.ois.close();
/*     */       } catch (IOException e) {
/* 118 */         this.logger.warn("Could not close connection.", e);
/*     */       } finally {
/* 120 */         this.ois = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 127 */     return getClass().getName() + this.remoteSocketAddress.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\net\SocketNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */