/*     */ package io.netty.channel.udt.nio;
/*     */ 
/*     */ import com.barchart.udt.SocketUDT;
/*     */ import com.barchart.udt.TypeUDT;
/*     */ import com.barchart.udt.nio.ChannelUDT;
/*     */ import com.barchart.udt.nio.KindUDT;
/*     */ import com.barchart.udt.nio.RendezvousChannelUDT;
/*     */ import com.barchart.udt.nio.SelectorProviderUDT;
/*     */ import com.barchart.udt.nio.ServerSocketChannelUDT;
/*     */ import com.barchart.udt.nio.SocketChannelUDT;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFactory;
/*     */ import io.netty.channel.udt.UdtChannel;
/*     */ import io.netty.channel.udt.UdtServerChannel;
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.spi.SelectorProvider;
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
/*     */ public final class NioUdtProvider<T extends UdtChannel>
/*     */   implements ChannelFactory<T>
/*     */ {
/*  48 */   public static final ChannelFactory<UdtServerChannel> BYTE_ACCEPTOR = new NioUdtProvider(TypeUDT.STREAM, KindUDT.ACCEPTOR);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  55 */   public static final ChannelFactory<UdtChannel> BYTE_CONNECTOR = new NioUdtProvider(TypeUDT.STREAM, KindUDT.CONNECTOR);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  62 */   public static final SelectorProvider BYTE_PROVIDER = SelectorProviderUDT.STREAM;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  68 */   public static final ChannelFactory<UdtChannel> BYTE_RENDEZVOUS = new NioUdtProvider(TypeUDT.STREAM, KindUDT.RENDEZVOUS);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  75 */   public static final ChannelFactory<UdtServerChannel> MESSAGE_ACCEPTOR = new NioUdtProvider(TypeUDT.DATAGRAM, KindUDT.ACCEPTOR);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  82 */   public static final ChannelFactory<UdtChannel> MESSAGE_CONNECTOR = new NioUdtProvider(TypeUDT.DATAGRAM, KindUDT.CONNECTOR);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  89 */   public static final SelectorProvider MESSAGE_PROVIDER = SelectorProviderUDT.DATAGRAM;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  95 */   public static final ChannelFactory<UdtChannel> MESSAGE_RENDEZVOUS = new NioUdtProvider(TypeUDT.DATAGRAM, KindUDT.RENDEZVOUS);
/*     */   
/*     */ 
/*     */   private final KindUDT kind;
/*     */   
/*     */ 
/*     */   private final TypeUDT type;
/*     */   
/*     */ 
/*     */   public static ChannelUDT channelUDT(Channel channel)
/*     */   {
/* 106 */     if ((channel instanceof NioUdtByteAcceptorChannel)) {
/* 107 */       return ((NioUdtByteAcceptorChannel)channel).javaChannel();
/*     */     }
/* 109 */     if ((channel instanceof NioUdtByteConnectorChannel)) {
/* 110 */       return ((NioUdtByteConnectorChannel)channel).javaChannel();
/*     */     }
/* 112 */     if ((channel instanceof NioUdtByteRendezvousChannel)) {
/* 113 */       return ((NioUdtByteRendezvousChannel)channel).javaChannel();
/*     */     }
/*     */     
/* 116 */     if ((channel instanceof NioUdtMessageAcceptorChannel)) {
/* 117 */       return ((NioUdtMessageAcceptorChannel)channel).javaChannel();
/*     */     }
/* 119 */     if ((channel instanceof NioUdtMessageConnectorChannel)) {
/* 120 */       return ((NioUdtMessageConnectorChannel)channel).javaChannel();
/*     */     }
/* 122 */     if ((channel instanceof NioUdtMessageRendezvousChannel)) {
/* 123 */       return ((NioUdtMessageRendezvousChannel)channel).javaChannel();
/*     */     }
/* 125 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static ServerSocketChannelUDT newAcceptorChannelUDT(TypeUDT type)
/*     */   {
/*     */     try
/*     */     {
/* 134 */       return SelectorProviderUDT.from(type).openServerSocketChannel();
/*     */     } catch (IOException e) {
/* 136 */       throw new ChannelException("failed to open a server socket channel", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static SocketChannelUDT newConnectorChannelUDT(TypeUDT type)
/*     */   {
/*     */     try
/*     */     {
/* 145 */       return SelectorProviderUDT.from(type).openSocketChannel();
/*     */     } catch (IOException e) {
/* 147 */       throw new ChannelException("failed to open a socket channel", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static RendezvousChannelUDT newRendezvousChannelUDT(TypeUDT type)
/*     */   {
/*     */     try
/*     */     {
/* 157 */       return SelectorProviderUDT.from(type).openRendezvousChannel();
/*     */     } catch (IOException e) {
/* 159 */       throw new ChannelException("failed to open a rendezvous channel", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SocketUDT socketUDT(Channel channel)
/*     */   {
/* 170 */     ChannelUDT channelUDT = channelUDT(channel);
/* 171 */     if (channelUDT == null) {
/* 172 */       return null;
/*     */     }
/* 174 */     return channelUDT.socketUDT();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private NioUdtProvider(TypeUDT type, KindUDT kind)
/*     */   {
/* 185 */     this.type = type;
/* 186 */     this.kind = kind;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public KindUDT kind()
/*     */   {
/* 193 */     return this.kind;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T newChannel()
/*     */   {
/* 203 */     switch (this.kind) {
/*     */     case ACCEPTOR: 
/* 205 */       switch (this.type) {
/*     */       case DATAGRAM: 
/* 207 */         return new NioUdtMessageAcceptorChannel();
/*     */       case STREAM: 
/* 209 */         return new NioUdtByteAcceptorChannel();
/*     */       }
/* 211 */       throw new IllegalStateException("wrong type=" + this.type);
/*     */     
/*     */     case CONNECTOR: 
/* 214 */       switch (this.type) {
/*     */       case DATAGRAM: 
/* 216 */         return new NioUdtMessageConnectorChannel();
/*     */       case STREAM: 
/* 218 */         return new NioUdtByteConnectorChannel();
/*     */       }
/* 220 */       throw new IllegalStateException("wrong type=" + this.type);
/*     */     
/*     */     case RENDEZVOUS: 
/* 223 */       switch (this.type) {
/*     */       case DATAGRAM: 
/* 225 */         return new NioUdtMessageRendezvousChannel();
/*     */       case STREAM: 
/* 227 */         return new NioUdtByteRendezvousChannel();
/*     */       }
/* 229 */       throw new IllegalStateException("wrong type=" + this.type);
/*     */     }
/*     */     
/* 232 */     throw new IllegalStateException("wrong kind=" + this.kind);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TypeUDT type()
/*     */   {
/* 240 */     return this.type;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\udt\nio\NioUdtProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */