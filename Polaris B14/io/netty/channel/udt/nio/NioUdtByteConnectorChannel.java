/*     */ package io.netty.channel.udt.nio;
/*     */ 
/*     */ import com.barchart.udt.TypeUDT;
/*     */ import com.barchart.udt.nio.NioSocketUDT;
/*     */ import com.barchart.udt.nio.SocketChannelUDT;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.FileRegion;
/*     */ import io.netty.channel.nio.AbstractNioByteChannel;
/*     */ import io.netty.channel.udt.DefaultUdtChannelConfig;
/*     */ import io.netty.channel.udt.UdtChannel;
/*     */ import io.netty.channel.udt.UdtChannelConfig;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.SelectionKey;
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
/*     */ public class NioUdtByteConnectorChannel
/*     */   extends AbstractNioByteChannel
/*     */   implements UdtChannel
/*     */ {
/*  42 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioUdtByteConnectorChannel.class);
/*     */   
/*     */ 
/*  45 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*     */   private final UdtChannelConfig config;
/*     */   
/*     */   public NioUdtByteConnectorChannel()
/*     */   {
/*  50 */     this(TypeUDT.STREAM);
/*     */   }
/*     */   
/*     */   public NioUdtByteConnectorChannel(Channel parent, SocketChannelUDT channelUDT) {
/*  54 */     super(parent, channelUDT);
/*     */     try {
/*  56 */       channelUDT.configureBlocking(false);
/*  57 */       switch (channelUDT.socketUDT().status()) {
/*     */       case INIT: 
/*     */       case OPENED: 
/*  60 */         this.config = new DefaultUdtChannelConfig(this, channelUDT, true);
/*  61 */         break;
/*     */       default: 
/*  63 */         this.config = new DefaultUdtChannelConfig(this, channelUDT, false);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*     */       try {
/*  68 */         channelUDT.close();
/*     */       } catch (Exception e2) {
/*  70 */         if (logger.isWarnEnabled()) {
/*  71 */           logger.warn("Failed to close channel.", e2);
/*     */         }
/*     */       }
/*  74 */       throw new ChannelException("Failed to configure channel.", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public NioUdtByteConnectorChannel(SocketChannelUDT channelUDT) {
/*  79 */     this(null, channelUDT);
/*     */   }
/*     */   
/*     */   public NioUdtByteConnectorChannel(TypeUDT type) {
/*  83 */     this(NioUdtProvider.newConnectorChannelUDT(type));
/*     */   }
/*     */   
/*     */   public UdtChannelConfig config()
/*     */   {
/*  88 */     return this.config;
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception
/*     */   {
/*  93 */     javaChannel().bind(localAddress);
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception
/*     */   {
/*  98 */     javaChannel().close();
/*     */   }
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress)
/*     */     throws Exception
/*     */   {
/* 104 */     doBind(localAddress != null ? localAddress : new InetSocketAddress(0));
/* 105 */     boolean success = false;
/*     */     try {
/* 107 */       boolean connected = javaChannel().connect(remoteAddress);
/* 108 */       if (!connected) {
/* 109 */         selectionKey().interestOps(selectionKey().interestOps() | 0x8);
/*     */       }
/*     */       
/* 112 */       success = true;
/* 113 */       return connected;
/*     */     } finally {
/* 115 */       if (!success) {
/* 116 */         doClose();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doDisconnect() throws Exception
/*     */   {
/* 123 */     doClose();
/*     */   }
/*     */   
/*     */   protected void doFinishConnect() throws Exception
/*     */   {
/* 128 */     if (javaChannel().finishConnect()) {
/* 129 */       selectionKey().interestOps(selectionKey().interestOps() & 0xFFFFFFF7);
/*     */     }
/*     */     else {
/* 132 */       throw new Error("Provider error: failed to finish connect. Provider library should be upgraded.");
/*     */     }
/*     */   }
/*     */   
/*     */   protected int doReadBytes(ByteBuf byteBuf)
/*     */     throws Exception
/*     */   {
/* 139 */     return byteBuf.writeBytes(javaChannel(), byteBuf.writableBytes());
/*     */   }
/*     */   
/*     */   protected int doWriteBytes(ByteBuf byteBuf) throws Exception
/*     */   {
/* 144 */     int expectedWrittenBytes = byteBuf.readableBytes();
/* 145 */     return byteBuf.readBytes(javaChannel(), expectedWrittenBytes);
/*     */   }
/*     */   
/*     */   protected long doWriteFileRegion(FileRegion region) throws Exception
/*     */   {
/* 150 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isActive()
/*     */   {
/* 155 */     SocketChannelUDT channelUDT = javaChannel();
/* 156 */     return (channelUDT.isOpen()) && (channelUDT.isConnectFinished());
/*     */   }
/*     */   
/*     */   protected SocketChannelUDT javaChannel()
/*     */   {
/* 161 */     return (SocketChannelUDT)super.javaChannel();
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress0()
/*     */   {
/* 166 */     return javaChannel().socket().getLocalSocketAddress();
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/* 171 */     return METADATA;
/*     */   }
/*     */   
/*     */   protected SocketAddress remoteAddress0()
/*     */   {
/* 176 */     return javaChannel().socket().getRemoteSocketAddress();
/*     */   }
/*     */   
/*     */   public InetSocketAddress localAddress()
/*     */   {
/* 181 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   public InetSocketAddress remoteAddress()
/*     */   {
/* 186 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\udt\nio\NioUdtByteConnectorChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */