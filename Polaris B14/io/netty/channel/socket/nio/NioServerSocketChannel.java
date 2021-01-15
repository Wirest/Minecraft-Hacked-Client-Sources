/*     */ package io.netty.channel.socket.nio;
/*     */ 
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.nio.AbstractNioMessageChannel;
/*     */ import io.netty.channel.socket.DefaultServerSocketChannelConfig;
/*     */ import io.netty.channel.socket.ServerSocketChannelConfig;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.util.List;
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
/*     */ public class NioServerSocketChannel
/*     */   extends AbstractNioMessageChannel
/*     */   implements io.netty.channel.socket.ServerSocketChannel
/*     */ {
/*  44 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*  45 */   private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();
/*     */   
/*  47 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioServerSocketChannel.class);
/*     */   
/*     */ 
/*     */   private final ServerSocketChannelConfig config;
/*     */   
/*     */ 
/*     */   private static java.nio.channels.ServerSocketChannel newSocket(SelectorProvider provider)
/*     */   {
/*     */     try
/*     */     {
/*  57 */       return provider.openServerSocketChannel();
/*     */     } catch (IOException e) {
/*  59 */       throw new ChannelException("Failed to open a server socket.", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NioServerSocketChannel()
/*     */   {
/*  70 */     this(newSocket(DEFAULT_SELECTOR_PROVIDER));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public NioServerSocketChannel(SelectorProvider provider)
/*     */   {
/*  77 */     this(newSocket(provider));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public NioServerSocketChannel(java.nio.channels.ServerSocketChannel channel)
/*     */   {
/*  84 */     super(null, channel, 16);
/*  85 */     this.config = new NioServerSocketChannelConfig(this, javaChannel().socket(), null);
/*     */   }
/*     */   
/*     */   public InetSocketAddress localAddress()
/*     */   {
/*  90 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/*  95 */     return METADATA;
/*     */   }
/*     */   
/*     */   public ServerSocketChannelConfig config()
/*     */   {
/* 100 */     return this.config;
/*     */   }
/*     */   
/*     */   public boolean isActive()
/*     */   {
/* 105 */     return javaChannel().socket().isBound();
/*     */   }
/*     */   
/*     */   public InetSocketAddress remoteAddress()
/*     */   {
/* 110 */     return null;
/*     */   }
/*     */   
/*     */   protected java.nio.channels.ServerSocketChannel javaChannel()
/*     */   {
/* 115 */     return (java.nio.channels.ServerSocketChannel)super.javaChannel();
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress0()
/*     */   {
/* 120 */     return javaChannel().socket().getLocalSocketAddress();
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception
/*     */   {
/* 125 */     javaChannel().socket().bind(localAddress, this.config.getBacklog());
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception
/*     */   {
/* 130 */     javaChannel().close();
/*     */   }
/*     */   
/*     */   protected int doReadMessages(List<Object> buf) throws Exception
/*     */   {
/* 135 */     SocketChannel ch = javaChannel().accept();
/*     */     try
/*     */     {
/* 138 */       if (ch != null) {
/* 139 */         buf.add(new NioSocketChannel(this, ch));
/* 140 */         return 1;
/*     */       }
/*     */     } catch (Throwable t) {
/* 143 */       logger.warn("Failed to create a new channel from an accepted socket.", t);
/*     */       try
/*     */       {
/* 146 */         ch.close();
/*     */       } catch (Throwable t2) {
/* 148 */         logger.warn("Failed to close a socket.", t2);
/*     */       }
/*     */     }
/*     */     
/* 152 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress)
/*     */     throws Exception
/*     */   {
/* 159 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected void doFinishConnect() throws Exception
/*     */   {
/* 164 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected SocketAddress remoteAddress0()
/*     */   {
/* 169 */     return null;
/*     */   }
/*     */   
/*     */   protected void doDisconnect() throws Exception
/*     */   {
/* 174 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected boolean doWriteMessage(Object msg, ChannelOutboundBuffer in) throws Exception
/*     */   {
/* 179 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected final Object filterOutboundMessage(Object msg) throws Exception
/*     */   {
/* 184 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   private final class NioServerSocketChannelConfig extends DefaultServerSocketChannelConfig {
/*     */     private NioServerSocketChannelConfig(NioServerSocketChannel channel, ServerSocket javaSocket) {
/* 189 */       super(javaSocket);
/*     */     }
/*     */     
/*     */     protected void autoReadCleared()
/*     */     {
/* 194 */       NioServerSocketChannel.this.setReadPending(false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\socket\nio\NioServerSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */