/*     */ package io.netty.channel.udt.nio;
/*     */ 
/*     */ import com.barchart.udt.TypeUDT;
/*     */ import com.barchart.udt.nio.NioServerSocketUDT;
/*     */ import com.barchart.udt.nio.ServerSocketChannelUDT;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.nio.AbstractNioMessageChannel;
/*     */ import io.netty.channel.udt.DefaultUdtServerChannelConfig;
/*     */ import io.netty.channel.udt.UdtServerChannel;
/*     */ import io.netty.channel.udt.UdtServerChannelConfig;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
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
/*     */ public abstract class NioUdtAcceptorChannel
/*     */   extends AbstractNioMessageChannel
/*     */   implements UdtServerChannel
/*     */ {
/*  39 */   protected static final InternalLogger logger = InternalLoggerFactory.getInstance(NioUdtAcceptorChannel.class);
/*     */   
/*     */   private final UdtServerChannelConfig config;
/*     */   
/*     */   protected NioUdtAcceptorChannel(ServerSocketChannelUDT channelUDT)
/*     */   {
/*  45 */     super(null, channelUDT, 16);
/*     */     try {
/*  47 */       channelUDT.configureBlocking(false);
/*  48 */       this.config = new DefaultUdtServerChannelConfig(this, channelUDT, true);
/*     */     } catch (Exception e) {
/*     */       try {
/*  51 */         channelUDT.close();
/*     */       } catch (Exception e2) {
/*  53 */         if (logger.isWarnEnabled()) {
/*  54 */           logger.warn("Failed to close channel.", e2);
/*     */         }
/*     */       }
/*  57 */       throw new ChannelException("Failed to configure channel.", e);
/*     */     }
/*     */   }
/*     */   
/*     */   protected NioUdtAcceptorChannel(TypeUDT type) {
/*  62 */     this(NioUdtProvider.newAcceptorChannelUDT(type));
/*     */   }
/*     */   
/*     */   public UdtServerChannelConfig config()
/*     */   {
/*  67 */     return this.config;
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception
/*     */   {
/*  72 */     javaChannel().socket().bind(localAddress, this.config.getBacklog());
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception
/*     */   {
/*  77 */     javaChannel().close();
/*     */   }
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress)
/*     */     throws Exception
/*     */   {
/*  83 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected void doDisconnect() throws Exception
/*     */   {
/*  88 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected void doFinishConnect() throws Exception
/*     */   {
/*  93 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected boolean doWriteMessage(Object msg, ChannelOutboundBuffer in) throws Exception
/*     */   {
/*  98 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected final Object filterOutboundMessage(Object msg) throws Exception
/*     */   {
/* 103 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isActive()
/*     */   {
/* 108 */     return javaChannel().socket().isBound();
/*     */   }
/*     */   
/*     */   protected ServerSocketChannelUDT javaChannel()
/*     */   {
/* 113 */     return (ServerSocketChannelUDT)super.javaChannel();
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress0()
/*     */   {
/* 118 */     return javaChannel().socket().getLocalSocketAddress();
/*     */   }
/*     */   
/*     */   public InetSocketAddress localAddress() {
/* 122 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   public InetSocketAddress remoteAddress()
/*     */   {
/* 127 */     return null;
/*     */   }
/*     */   
/*     */   protected SocketAddress remoteAddress0()
/*     */   {
/* 132 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\udt\nio\NioUdtAcceptorChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */