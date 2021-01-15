/*     */ package io.netty.channel.socket.oio;
/*     */ 
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.oio.AbstractOioMessageChannel;
/*     */ import io.netty.channel.socket.ServerSocketChannel;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.util.List;
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
/*     */ public class OioServerSocketChannel
/*     */   extends AbstractOioMessageChannel
/*     */   implements ServerSocketChannel
/*     */ {
/*  44 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(OioServerSocketChannel.class);
/*     */   
/*     */ 
/*  47 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*     */   final ServerSocket socket;
/*     */   
/*     */   private static ServerSocket newServerSocket() {
/*  51 */     try { return new ServerSocket();
/*     */     } catch (IOException e) {
/*  53 */       throw new ChannelException("failed to create a server socket", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*  58 */   final Lock shutdownLock = new ReentrantLock();
/*     */   
/*     */   private final OioServerSocketChannelConfig config;
/*     */   
/*     */ 
/*     */   public OioServerSocketChannel()
/*     */   {
/*  65 */     this(newServerSocket());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OioServerSocketChannel(ServerSocket socket)
/*     */   {
/*  74 */     super(null);
/*  75 */     if (socket == null) {
/*  76 */       throw new NullPointerException("socket");
/*     */     }
/*     */     
/*  79 */     boolean success = false;
/*     */     try {
/*  81 */       socket.setSoTimeout(1000);
/*  82 */       success = true;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  87 */       if (!success) {
/*     */         try {
/*  89 */           socket.close();
/*     */         } catch (IOException e) {
/*  91 */           if (logger.isWarnEnabled()) {
/*  92 */             logger.warn("Failed to close a partially initialized socket.", e);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*  98 */       this.socket = socket;
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*  84 */       throw new ChannelException("Failed to set the server socket timeout.", e);
/*     */     }
/*     */     finally {
/*  87 */       if (!success) {
/*     */         try {
/*  89 */           socket.close();
/*     */         } catch (IOException e) {
/*  91 */           if (logger.isWarnEnabled()) {
/*  92 */             logger.warn("Failed to close a partially initialized socket.", e);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  99 */     this.config = new DefaultOioServerSocketChannelConfig(this, socket);
/*     */   }
/*     */   
/*     */   public InetSocketAddress localAddress()
/*     */   {
/* 104 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/* 109 */     return METADATA;
/*     */   }
/*     */   
/*     */   public OioServerSocketChannelConfig config()
/*     */   {
/* 114 */     return this.config;
/*     */   }
/*     */   
/*     */   public InetSocketAddress remoteAddress()
/*     */   {
/* 119 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isOpen()
/*     */   {
/* 124 */     return !this.socket.isClosed();
/*     */   }
/*     */   
/*     */   public boolean isActive()
/*     */   {
/* 129 */     return (isOpen()) && (this.socket.isBound());
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress0()
/*     */   {
/* 134 */     return this.socket.getLocalSocketAddress();
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception
/*     */   {
/* 139 */     this.socket.bind(localAddress, this.config.getBacklog());
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception
/*     */   {
/* 144 */     this.socket.close();
/*     */   }
/*     */   
/*     */   protected int doReadMessages(List<Object> buf) throws Exception
/*     */   {
/* 149 */     if (this.socket.isClosed()) {
/* 150 */       return -1;
/*     */     }
/*     */     try
/*     */     {
/* 154 */       Socket s = this.socket.accept();
/*     */       try {
/* 156 */         buf.add(new OioSocketChannel(this, s));
/* 157 */         return 1;
/*     */       } catch (Throwable t) {
/* 159 */         logger.warn("Failed to create a new channel from an accepted socket.", t);
/*     */         try {
/* 161 */           s.close();
/*     */         } catch (Throwable t2) {
/* 163 */           logger.warn("Failed to close a socket.", t2);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 169 */       return 0;
/*     */     } catch (SocketTimeoutException e) {}
/*     */   }
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception {
/* 174 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg) throws Exception
/*     */   {
/* 179 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected void doConnect(SocketAddress remoteAddress, SocketAddress localAddress)
/*     */     throws Exception
/*     */   {
/* 185 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected SocketAddress remoteAddress0()
/*     */   {
/* 190 */     return null;
/*     */   }
/*     */   
/*     */   protected void doDisconnect() throws Exception
/*     */   {
/* 195 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected void setReadPending(boolean readPending)
/*     */   {
/* 200 */     super.setReadPending(readPending);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\socket\oio\OioServerSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */