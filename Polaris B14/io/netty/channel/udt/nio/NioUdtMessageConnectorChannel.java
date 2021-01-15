/*     */ package io.netty.channel.udt.nio;
/*     */ 
/*     */ import com.barchart.udt.TypeUDT;
/*     */ import com.barchart.udt.nio.NioSocketUDT;
/*     */ import com.barchart.udt.nio.SocketChannelUDT;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.nio.AbstractNioMessageChannel;
/*     */ import io.netty.channel.udt.DefaultUdtChannelConfig;
/*     */ import io.netty.channel.udt.UdtChannel;
/*     */ import io.netty.channel.udt.UdtChannelConfig;
/*     */ import io.netty.channel.udt.UdtMessage;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.SelectionKey;
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
/*     */ public class NioUdtMessageConnectorChannel
/*     */   extends AbstractNioMessageChannel
/*     */   implements UdtChannel
/*     */ {
/*  46 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioUdtMessageConnectorChannel.class);
/*     */   
/*     */ 
/*  49 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*     */   private final UdtChannelConfig config;
/*     */   
/*     */   public NioUdtMessageConnectorChannel()
/*     */   {
/*  54 */     this(TypeUDT.DATAGRAM);
/*     */   }
/*     */   
/*     */   public NioUdtMessageConnectorChannel(Channel parent, SocketChannelUDT channelUDT) {
/*  58 */     super(parent, channelUDT, 1);
/*     */     try {
/*  60 */       channelUDT.configureBlocking(false);
/*  61 */       switch (channelUDT.socketUDT().status()) {
/*     */       case INIT: 
/*     */       case OPENED: 
/*  64 */         this.config = new DefaultUdtChannelConfig(this, channelUDT, true);
/*  65 */         break;
/*     */       default: 
/*  67 */         this.config = new DefaultUdtChannelConfig(this, channelUDT, false);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*     */       try {
/*  72 */         channelUDT.close();
/*     */       } catch (Exception e2) {
/*  74 */         if (logger.isWarnEnabled()) {
/*  75 */           logger.warn("Failed to close channel.", e2);
/*     */         }
/*     */       }
/*  78 */       throw new ChannelException("Failed to configure channel.", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public NioUdtMessageConnectorChannel(SocketChannelUDT channelUDT) {
/*  83 */     this(null, channelUDT);
/*     */   }
/*     */   
/*     */   public NioUdtMessageConnectorChannel(TypeUDT type) {
/*  87 */     this(NioUdtProvider.newConnectorChannelUDT(type));
/*     */   }
/*     */   
/*     */   public UdtChannelConfig config()
/*     */   {
/*  92 */     return this.config;
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception
/*     */   {
/*  97 */     javaChannel().bind(localAddress);
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception
/*     */   {
/* 102 */     javaChannel().close();
/*     */   }
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress)
/*     */     throws Exception
/*     */   {
/* 108 */     doBind(localAddress != null ? localAddress : new InetSocketAddress(0));
/* 109 */     boolean success = false;
/*     */     try {
/* 111 */       boolean connected = javaChannel().connect(remoteAddress);
/* 112 */       if (!connected) {
/* 113 */         selectionKey().interestOps(selectionKey().interestOps() | 0x8);
/*     */       }
/*     */       
/* 116 */       success = true;
/* 117 */       return connected;
/*     */     } finally {
/* 119 */       if (!success) {
/* 120 */         doClose();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doDisconnect() throws Exception
/*     */   {
/* 127 */     doClose();
/*     */   }
/*     */   
/*     */   protected void doFinishConnect() throws Exception
/*     */   {
/* 132 */     if (javaChannel().finishConnect()) {
/* 133 */       selectionKey().interestOps(selectionKey().interestOps() & 0xFFFFFFF7);
/*     */     }
/*     */     else {
/* 136 */       throw new Error("Provider error: failed to finish connect. Provider library should be upgraded.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected int doReadMessages(List<Object> buf)
/*     */     throws Exception
/*     */   {
/* 144 */     int maximumMessageSize = this.config.getReceiveBufferSize();
/*     */     
/* 146 */     ByteBuf byteBuf = this.config.getAllocator().directBuffer(maximumMessageSize);
/*     */     
/*     */ 
/* 149 */     int receivedMessageSize = byteBuf.writeBytes(javaChannel(), maximumMessageSize);
/*     */     
/*     */ 
/* 152 */     if (receivedMessageSize <= 0) {
/* 153 */       byteBuf.release();
/* 154 */       return 0;
/*     */     }
/*     */     
/* 157 */     if (receivedMessageSize >= maximumMessageSize) {
/* 158 */       javaChannel().close();
/* 159 */       throw new ChannelException("Invalid config : increase receive buffer size to avoid message truncation");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 164 */     buf.add(new UdtMessage(byteBuf));
/*     */     
/* 166 */     return 1;
/*     */   }
/*     */   
/*     */   protected boolean doWriteMessage(Object msg, ChannelOutboundBuffer in)
/*     */     throws Exception
/*     */   {
/* 172 */     UdtMessage message = (UdtMessage)msg;
/*     */     
/* 174 */     ByteBuf byteBuf = message.content();
/*     */     
/* 176 */     int messageSize = byteBuf.readableBytes();
/*     */     long writtenBytes;
/*     */     long writtenBytes;
/* 179 */     if (byteBuf.nioBufferCount() == 1) {
/* 180 */       writtenBytes = javaChannel().write(byteBuf.nioBuffer());
/*     */     } else {
/* 182 */       writtenBytes = javaChannel().write(byteBuf.nioBuffers());
/*     */     }
/*     */     
/*     */ 
/* 186 */     if ((writtenBytes <= 0L) && (messageSize > 0)) {
/* 187 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 191 */     if (writtenBytes != messageSize) {
/* 192 */       throw new Error("Provider error: failed to write message. Provider library should be upgraded.");
/*     */     }
/*     */     
/*     */ 
/* 196 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isActive()
/*     */   {
/* 201 */     SocketChannelUDT channelUDT = javaChannel();
/* 202 */     return (channelUDT.isOpen()) && (channelUDT.isConnectFinished());
/*     */   }
/*     */   
/*     */   protected SocketChannelUDT javaChannel()
/*     */   {
/* 207 */     return (SocketChannelUDT)super.javaChannel();
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress0()
/*     */   {
/* 212 */     return javaChannel().socket().getLocalSocketAddress();
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/* 217 */     return METADATA;
/*     */   }
/*     */   
/*     */   protected SocketAddress remoteAddress0()
/*     */   {
/* 222 */     return javaChannel().socket().getRemoteSocketAddress();
/*     */   }
/*     */   
/*     */   public InetSocketAddress localAddress()
/*     */   {
/* 227 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   public InetSocketAddress remoteAddress()
/*     */   {
/* 232 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\udt\nio\NioUdtMessageConnectorChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */