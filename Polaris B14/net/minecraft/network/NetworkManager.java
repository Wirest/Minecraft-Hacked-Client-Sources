/*     */ package net.minecraft.network;
/*     */ 
/*     */ import com.google.common.collect.Queues;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import io.netty.bootstrap.Bootstrap;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.SimpleChannelInboundHandler;
/*     */ import io.netty.channel.epoll.Epoll;
/*     */ import io.netty.channel.epoll.EpollEventLoopGroup;
/*     */ import io.netty.channel.epoll.EpollSocketChannel;
/*     */ import io.netty.channel.local.LocalChannel;
/*     */ import io.netty.channel.local.LocalEventLoopGroup;
/*     */ import io.netty.channel.local.LocalServerChannel;
/*     */ import io.netty.channel.nio.NioEventLoopGroup;
/*     */ import io.netty.channel.socket.SocketChannel;
/*     */ import io.netty.channel.socket.nio.NioSocketChannel;
/*     */ import io.netty.handler.timeout.ReadTimeoutHandler;
/*     */ import io.netty.handler.timeout.TimeoutException;
/*     */ import io.netty.util.Attribute;
/*     */ import io.netty.util.AttributeKey;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import javax.crypto.SecretKey;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.CryptManager;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ITickable;
/*     */ import net.minecraft.util.LazyLoadBase;
/*     */ import net.minecraft.util.MessageDeserializer;
/*     */ import net.minecraft.util.MessageDeserializer2;
/*     */ import net.minecraft.util.MessageSerializer;
/*     */ import net.minecraft.util.MessageSerializer2;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.MarkerManager;
/*     */ import rip.jutting.polaris.event.events.EventReceivePacket;
/*     */ 
/*     */ public class NetworkManager
/*     */   extends SimpleChannelInboundHandler<Packet>
/*     */ {
/*  58 */   private static final Logger logger = ;
/*  59 */   public static final Marker logMarkerNetwork = MarkerManager.getMarker("NETWORK");
/*  60 */   public static final Marker logMarkerPackets = MarkerManager.getMarker("NETWORK_PACKETS", logMarkerNetwork);
/*  61 */   public static final AttributeKey<EnumConnectionState> attrKeyConnectionState = AttributeKey.valueOf("protocol");
/*  62 */   public static final LazyLoadBase<NioEventLoopGroup> CLIENT_NIO_EVENTLOOP = new LazyLoadBase()
/*     */   {
/*     */     protected NioEventLoopGroup load()
/*     */     {
/*  66 */       return new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Client IO #%d").setDaemon(true).build());
/*     */     }
/*     */   };
/*  69 */   public static final LazyLoadBase<EpollEventLoopGroup> field_181125_e = new LazyLoadBase()
/*     */   {
/*     */     protected EpollEventLoopGroup load()
/*     */     {
/*  73 */       return new EpollEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build());
/*     */     }
/*     */   };
/*  76 */   public static final LazyLoadBase<LocalEventLoopGroup> CLIENT_LOCAL_EVENTLOOP = new LazyLoadBase()
/*     */   {
/*     */     protected LocalEventLoopGroup load()
/*     */     {
/*  80 */       return new LocalEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Local Client IO #%d").setDaemon(true).build());
/*     */     }
/*     */   };
/*     */   private final EnumPacketDirection direction;
/*  84 */   private final Queue<InboundHandlerTuplePacketListener> outboundPacketsQueue = Queues.newConcurrentLinkedQueue();
/*  85 */   private final ReentrantReadWriteLock field_181680_j = new ReentrantReadWriteLock();
/*     */   
/*     */ 
/*     */   private Channel channel;
/*     */   
/*     */   private SocketAddress socketAddress;
/*     */   
/*     */   private INetHandler packetListener;
/*     */   
/*     */   private IChatComponent terminationReason;
/*     */   
/*     */   private boolean isEncrypted;
/*     */   
/*     */   private boolean disconnected;
/*     */   
/*     */ 
/*     */   public NetworkManager(EnumPacketDirection packetDirection)
/*     */   {
/* 103 */     this.direction = packetDirection;
/*     */   }
/*     */   
/*     */   public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception
/*     */   {
/* 108 */     super.channelActive(p_channelActive_1_);
/* 109 */     this.channel = p_channelActive_1_.channel();
/* 110 */     this.socketAddress = this.channel.remoteAddress();
/*     */     
/*     */     try
/*     */     {
/* 114 */       setConnectionState(EnumConnectionState.HANDSHAKING);
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/* 118 */       logger.fatal(throwable);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConnectionState(EnumConnectionState newState)
/*     */   {
/* 127 */     this.channel.attr(attrKeyConnectionState).set(newState);
/* 128 */     this.channel.config().setAutoRead(true);
/* 129 */     logger.debug("Enabled auto read");
/*     */   }
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext p_channelInactive_1_) throws Exception
/*     */   {
/* 134 */     closeChannel(new ChatComponentTranslation("disconnect.endOfStream", new Object[0]));
/*     */   }
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_) throws Exception
/*     */   {
/*     */     ChatComponentTranslation chatcomponenttranslation;
/*     */     ChatComponentTranslation chatcomponenttranslation;
/* 141 */     if ((p_exceptionCaught_2_ instanceof TimeoutException))
/*     */     {
/* 143 */       chatcomponenttranslation = new ChatComponentTranslation("disconnect.timeout", new Object[0]);
/*     */     }
/*     */     else
/*     */     {
/* 147 */       chatcomponenttranslation = new ChatComponentTranslation("disconnect.genericReason", new Object[] { "Internal Exception: " + p_exceptionCaught_2_ });
/*     */     }
/*     */     
/* 150 */     closeChannel(chatcomponenttranslation);
/*     */   }
/*     */   
/*     */   protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet p_channelRead0_2_) throws Exception
/*     */   {
/* 155 */     if (this.channel.isOpen())
/*     */     {
/*     */       try
/*     */       {
/* 159 */         EventReceivePacket eventReceivePacket = new EventReceivePacket(p_channelRead0_2_);
/* 160 */         eventReceivePacket.call();
/*     */         
/* 162 */         if (eventReceivePacket.isCancelled()) {
/* 163 */           return;
/*     */         }
/* 165 */         p_channelRead0_2_.processPacket(this.packetListener);
/*     */       }
/*     */       catch (ThreadQuickExitException localThreadQuickExitException) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setNetHandler(INetHandler handler)
/*     */   {
/* 180 */     Validate.notNull(handler, "packetListener", new Object[0]);
/* 181 */     logger.debug("Set listener of {} to {}", new Object[] { this, handler });
/* 182 */     this.packetListener = handler;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void sendPacket(Packet packetIn)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 287	rip/jutting/polaris/event/events/EventSendPacket
/*     */     //   3: dup
/*     */     //   4: aload_1
/*     */     //   5: invokespecial 288	rip/jutting/polaris/event/events/EventSendPacket:<init>	(Lnet/minecraft/network/Packet;)V
/*     */     //   8: astore_2
/*     */     //   9: aload_2
/*     */     //   10: invokevirtual 289	rip/jutting/polaris/event/events/EventSendPacket:call	()Lrip/jutting/polaris/event/Event;
/*     */     //   13: pop
/*     */     //   14: aload_2
/*     */     //   15: invokevirtual 290	rip/jutting/polaris/event/events/EventSendPacket:isCancelled	()Z
/*     */     //   18: ifeq +4 -> 22
/*     */     //   21: return
/*     */     //   22: aload_0
/*     */     //   23: invokevirtual 293	net/minecraft/network/NetworkManager:isChannelOpen	()Z
/*     */     //   26: ifeq +16 -> 42
/*     */     //   29: aload_0
/*     */     //   30: invokespecial 296	net/minecraft/network/NetworkManager:flushOutboundQueue	()V
/*     */     //   33: aload_0
/*     */     //   34: aload_1
/*     */     //   35: aconst_null
/*     */     //   36: invokespecial 300	net/minecraft/network/NetworkManager:dispatchPacket	(Lnet/minecraft/network/Packet;[Lio/netty/util/concurrent/GenericFutureListener;)V
/*     */     //   39: goto +58 -> 97
/*     */     //   42: aload_0
/*     */     //   43: getfield 123	net/minecraft/network/NetworkManager:field_181680_j	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*     */     //   46: invokevirtual 304	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*     */     //   49: invokevirtual 307	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lock	()V
/*     */     //   52: aload_0
/*     */     //   53: getfield 120	net/minecraft/network/NetworkManager:outboundPacketsQueue	Ljava/util/Queue;
/*     */     //   56: new 28	net/minecraft/network/NetworkManager$InboundHandlerTuplePacketListener
/*     */     //   59: dup
/*     */     //   60: aload_1
/*     */     //   61: aconst_null
/*     */     //   62: invokespecial 309	net/minecraft/network/NetworkManager$InboundHandlerTuplePacketListener:<init>	(Lnet/minecraft/network/Packet;[Lio/netty/util/concurrent/GenericFutureListener;)V
/*     */     //   65: invokeinterface 315 2 0
/*     */     //   70: pop
/*     */     //   71: goto +16 -> 87
/*     */     //   74: astore_3
/*     */     //   75: aload_0
/*     */     //   76: getfield 123	net/minecraft/network/NetworkManager:field_181680_j	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*     */     //   79: invokevirtual 304	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*     */     //   82: invokevirtual 318	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
/*     */     //   85: aload_3
/*     */     //   86: athrow
/*     */     //   87: aload_0
/*     */     //   88: getfield 123	net/minecraft/network/NetworkManager:field_181680_j	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*     */     //   91: invokevirtual 304	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*     */     //   94: invokevirtual 318	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
/*     */     //   97: return
/*     */     // Line number table:
/*     */     //   Java source line #187	-> byte code offset #0
/*     */     //   Java source line #188	-> byte code offset #9
/*     */     //   Java source line #190	-> byte code offset #14
/*     */     //   Java source line #191	-> byte code offset #21
/*     */     //   Java source line #193	-> byte code offset #22
/*     */     //   Java source line #195	-> byte code offset #29
/*     */     //   Java source line #196	-> byte code offset #33
/*     */     //   Java source line #197	-> byte code offset #39
/*     */     //   Java source line #200	-> byte code offset #42
/*     */     //   Java source line #204	-> byte code offset #52
/*     */     //   Java source line #205	-> byte code offset #71
/*     */     //   Java source line #207	-> byte code offset #74
/*     */     //   Java source line #208	-> byte code offset #75
/*     */     //   Java source line #209	-> byte code offset #85
/*     */     //   Java source line #208	-> byte code offset #87
/*     */     //   Java source line #211	-> byte code offset #97
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	98	0	this	NetworkManager
/*     */     //   0	98	1	packetIn	Packet
/*     */     //   8	7	2	eventSendPacket	rip.jutting.polaris.event.events.EventSendPacket
/*     */     //   74	12	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   52	74	74	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void sendPacket(Packet packetIn, GenericFutureListener<? extends Future<? super Void>> listener, GenericFutureListener<? extends Future<? super Void>>... listeners)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 293	net/minecraft/network/NetworkManager:isChannelOpen	()Z
/*     */     //   4: ifeq +24 -> 28
/*     */     //   7: aload_0
/*     */     //   8: invokespecial 296	net/minecraft/network/NetworkManager:flushOutboundQueue	()V
/*     */     //   11: aload_0
/*     */     //   12: aload_1
/*     */     //   13: aload_3
/*     */     //   14: iconst_0
/*     */     //   15: aload_2
/*     */     //   16: invokestatic 327	org/apache/commons/lang3/ArrayUtils:add	([Ljava/lang/Object;ILjava/lang/Object;)[Ljava/lang/Object;
/*     */     //   19: checkcast 329	[Lio/netty/util/concurrent/GenericFutureListener;
/*     */     //   22: invokespecial 300	net/minecraft/network/NetworkManager:dispatchPacket	(Lnet/minecraft/network/Packet;[Lio/netty/util/concurrent/GenericFutureListener;)V
/*     */     //   25: goto +68 -> 93
/*     */     //   28: aload_0
/*     */     //   29: getfield 123	net/minecraft/network/NetworkManager:field_181680_j	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*     */     //   32: invokevirtual 304	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*     */     //   35: invokevirtual 307	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lock	()V
/*     */     //   38: aload_0
/*     */     //   39: getfield 120	net/minecraft/network/NetworkManager:outboundPacketsQueue	Ljava/util/Queue;
/*     */     //   42: new 28	net/minecraft/network/NetworkManager$InboundHandlerTuplePacketListener
/*     */     //   45: dup
/*     */     //   46: aload_1
/*     */     //   47: aload_3
/*     */     //   48: iconst_0
/*     */     //   49: aload_2
/*     */     //   50: invokestatic 327	org/apache/commons/lang3/ArrayUtils:add	([Ljava/lang/Object;ILjava/lang/Object;)[Ljava/lang/Object;
/*     */     //   53: checkcast 329	[Lio/netty/util/concurrent/GenericFutureListener;
/*     */     //   56: invokespecial 309	net/minecraft/network/NetworkManager$InboundHandlerTuplePacketListener:<init>	(Lnet/minecraft/network/Packet;[Lio/netty/util/concurrent/GenericFutureListener;)V
/*     */     //   59: invokeinterface 315 2 0
/*     */     //   64: pop
/*     */     //   65: goto +18 -> 83
/*     */     //   68: astore 4
/*     */     //   70: aload_0
/*     */     //   71: getfield 123	net/minecraft/network/NetworkManager:field_181680_j	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*     */     //   74: invokevirtual 304	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*     */     //   77: invokevirtual 318	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
/*     */     //   80: aload 4
/*     */     //   82: athrow
/*     */     //   83: aload_0
/*     */     //   84: getfield 123	net/minecraft/network/NetworkManager:field_181680_j	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*     */     //   87: invokevirtual 304	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*     */     //   90: invokevirtual 318	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
/*     */     //   93: return
/*     */     // Line number table:
/*     */     //   Java source line #215	-> byte code offset #0
/*     */     //   Java source line #217	-> byte code offset #7
/*     */     //   Java source line #218	-> byte code offset #11
/*     */     //   Java source line #219	-> byte code offset #25
/*     */     //   Java source line #222	-> byte code offset #28
/*     */     //   Java source line #226	-> byte code offset #38
/*     */     //   Java source line #227	-> byte code offset #65
/*     */     //   Java source line #229	-> byte code offset #68
/*     */     //   Java source line #230	-> byte code offset #70
/*     */     //   Java source line #231	-> byte code offset #80
/*     */     //   Java source line #230	-> byte code offset #83
/*     */     //   Java source line #233	-> byte code offset #93
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	94	0	this	NetworkManager
/*     */     //   0	94	1	packetIn	Packet
/*     */     //   0	94	2	listener	GenericFutureListener<? extends Future<? super Void>>
/*     */     //   0	94	3	listeners	GenericFutureListener[]
/*     */     //   68	13	4	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   38	68	68	finally
/*     */   }
/*     */   
/*     */   private void dispatchPacket(final Packet inPacket, final GenericFutureListener<? extends Future<? super Void>>[] futureListeners)
/*     */   {
/* 241 */     final EnumConnectionState enumconnectionstate = EnumConnectionState.getFromPacket(inPacket);
/* 242 */     final EnumConnectionState enumconnectionstate1 = (EnumConnectionState)this.channel.attr(attrKeyConnectionState).get();
/*     */     
/* 244 */     if (enumconnectionstate1 != enumconnectionstate)
/*     */     {
/* 246 */       logger.debug("Disabled auto read");
/* 247 */       this.channel.config().setAutoRead(false);
/*     */     }
/*     */     
/* 250 */     if (this.channel.eventLoop().inEventLoop())
/*     */     {
/* 252 */       if (enumconnectionstate != enumconnectionstate1)
/*     */       {
/* 254 */         setConnectionState(enumconnectionstate);
/*     */       }
/*     */       
/* 257 */       ChannelFuture channelfuture = this.channel.writeAndFlush(inPacket);
/*     */       
/* 259 */       if (futureListeners != null)
/*     */       {
/* 261 */         channelfuture.addListeners(futureListeners);
/*     */       }
/*     */       
/* 264 */       channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
/*     */     }
/*     */     else
/*     */     {
/* 268 */       this.channel.eventLoop().execute(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 272 */           if (enumconnectionstate != enumconnectionstate1)
/*     */           {
/* 274 */             NetworkManager.this.setConnectionState(enumconnectionstate);
/*     */           }
/*     */           
/* 277 */           ChannelFuture channelfuture1 = NetworkManager.this.channel.writeAndFlush(inPacket);
/*     */           
/* 279 */           if (futureListeners != null)
/*     */           {
/* 281 */             channelfuture1.addListeners(futureListeners);
/*     */           }
/*     */           
/* 284 */           channelfuture1.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private void flushOutboundQueue()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 143	net/minecraft/network/NetworkManager:channel	Lio/netty/channel/Channel;
/*     */     //   4: ifnull +91 -> 95
/*     */     //   7: aload_0
/*     */     //   8: getfield 143	net/minecraft/network/NetworkManager:channel	Lio/netty/channel/Channel;
/*     */     //   11: invokeinterface 246 1 0
/*     */     //   16: ifeq +79 -> 95
/*     */     //   19: aload_0
/*     */     //   20: getfield 123	net/minecraft/network/NetworkManager:field_181680_j	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*     */     //   23: invokevirtual 389	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
/*     */     //   26: invokevirtual 390	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
/*     */     //   29: goto +28 -> 57
/*     */     //   32: aload_0
/*     */     //   33: getfield 120	net/minecraft/network/NetworkManager:outboundPacketsQueue	Ljava/util/Queue;
/*     */     //   36: invokeinterface 393 1 0
/*     */     //   41: checkcast 28	net/minecraft/network/NetworkManager$InboundHandlerTuplePacketListener
/*     */     //   44: astore_1
/*     */     //   45: aload_0
/*     */     //   46: aload_1
/*     */     //   47: invokestatic 397	net/minecraft/network/NetworkManager$InboundHandlerTuplePacketListener:access$0	(Lnet/minecraft/network/NetworkManager$InboundHandlerTuplePacketListener;)Lnet/minecraft/network/Packet;
/*     */     //   50: aload_1
/*     */     //   51: invokestatic 401	net/minecraft/network/NetworkManager$InboundHandlerTuplePacketListener:access$1	(Lnet/minecraft/network/NetworkManager$InboundHandlerTuplePacketListener;)[Lio/netty/util/concurrent/GenericFutureListener;
/*     */     //   54: invokespecial 300	net/minecraft/network/NetworkManager:dispatchPacket	(Lnet/minecraft/network/Packet;[Lio/netty/util/concurrent/GenericFutureListener;)V
/*     */     //   57: aload_0
/*     */     //   58: getfield 120	net/minecraft/network/NetworkManager:outboundPacketsQueue	Ljava/util/Queue;
/*     */     //   61: invokeinterface 404 1 0
/*     */     //   66: ifeq -34 -> 32
/*     */     //   69: goto +16 -> 85
/*     */     //   72: astore_2
/*     */     //   73: aload_0
/*     */     //   74: getfield 123	net/minecraft/network/NetworkManager:field_181680_j	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*     */     //   77: invokevirtual 389	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
/*     */     //   80: invokevirtual 405	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
/*     */     //   83: aload_2
/*     */     //   84: athrow
/*     */     //   85: aload_0
/*     */     //   86: getfield 123	net/minecraft/network/NetworkManager:field_181680_j	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*     */     //   89: invokevirtual 389	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
/*     */     //   92: invokevirtual 405	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
/*     */     //   95: return
/*     */     // Line number table:
/*     */     //   Java source line #295	-> byte code offset #0
/*     */     //   Java source line #297	-> byte code offset #19
/*     */     //   Java source line #301	-> byte code offset #29
/*     */     //   Java source line #303	-> byte code offset #32
/*     */     //   Java source line #304	-> byte code offset #45
/*     */     //   Java source line #301	-> byte code offset #57
/*     */     //   Java source line #306	-> byte code offset #69
/*     */     //   Java source line #308	-> byte code offset #72
/*     */     //   Java source line #309	-> byte code offset #73
/*     */     //   Java source line #310	-> byte code offset #83
/*     */     //   Java source line #309	-> byte code offset #85
/*     */     //   Java source line #312	-> byte code offset #95
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	96	0	this	NetworkManager
/*     */     //   44	7	1	networkmanager$inboundhandlertuplepacketlistener	InboundHandlerTuplePacketListener
/*     */     //   72	12	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   29	72	72	finally
/*     */   }
/*     */   
/*     */   public void processReceivedPackets()
/*     */   {
/* 319 */     flushOutboundQueue();
/*     */     
/* 321 */     if ((this.packetListener instanceof ITickable))
/*     */     {
/* 323 */       ((ITickable)this.packetListener).update();
/*     */     }
/*     */     
/* 326 */     this.channel.flush();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SocketAddress getRemoteAddress()
/*     */   {
/* 334 */     return this.socketAddress;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void closeChannel(IChatComponent message)
/*     */   {
/* 342 */     if (this.channel.isOpen())
/*     */     {
/* 344 */       this.channel.close().awaitUninterruptibly();
/* 345 */       this.terminationReason = message;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLocalChannel()
/*     */   {
/* 355 */     return ((this.channel instanceof LocalChannel)) || ((this.channel instanceof LocalServerChannel));
/*     */   }
/*     */   
/*     */   public static NetworkManager func_181124_a(InetAddress p_181124_0_, int p_181124_1_, boolean p_181124_2_)
/*     */   {
/* 360 */     NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
/*     */     LazyLoadBase<? extends EventLoopGroup> lazyloadbase;
/*     */     Class<? extends SocketChannel> oclass;
/*     */     LazyLoadBase<? extends EventLoopGroup> lazyloadbase;
/* 364 */     if ((Epoll.isAvailable()) && (p_181124_2_))
/*     */     {
/* 366 */       Class<? extends SocketChannel> oclass = EpollSocketChannel.class;
/* 367 */       lazyloadbase = field_181125_e;
/*     */     }
/*     */     else
/*     */     {
/* 371 */       oclass = NioSocketChannel.class;
/* 372 */       lazyloadbase = CLIENT_NIO_EVENTLOOP;
/*     */     }
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
/* 390 */     ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group((EventLoopGroup)lazyloadbase.getValue())).handler(new ChannelInitializer()
/*     */     {
/*     */       protected void initChannel(Channel p_initChannel_1_)
/*     */         throws Exception
/*     */       {
/*     */         try
/*     */         {
/* 381 */           p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
/*     */         }
/*     */         catch (ChannelException localChannelException) {}
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 388 */         p_initChannel_1_.pipeline().addLast("timeout", new ReadTimeoutHandler(30)).addLast("splitter", new MessageDeserializer2()).addLast("decoder", new MessageDeserializer(EnumPacketDirection.CLIENTBOUND)).addLast("prepender", new MessageSerializer2()).addLast("encoder", new MessageSerializer(EnumPacketDirection.SERVERBOUND)).addLast("packet_handler", NetworkManager.this);
/*     */       }
/* 390 */     })).channel(oclass)).connect(p_181124_0_, p_181124_1_).syncUninterruptibly();
/* 391 */     return networkmanager;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static NetworkManager provideLocalClient(SocketAddress address)
/*     */   {
/* 400 */     NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 407 */     ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group((EventLoopGroup)CLIENT_LOCAL_EVENTLOOP.getValue())).handler(new ChannelInitializer()
/*     */     {
/*     */       protected void initChannel(Channel p_initChannel_1_) throws Exception
/*     */       {
/* 405 */         p_initChannel_1_.pipeline().addLast("packet_handler", NetworkManager.this);
/*     */       }
/* 407 */     })).channel(LocalChannel.class)).connect(address).syncUninterruptibly();
/* 408 */     return networkmanager;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void enableEncryption(SecretKey key)
/*     */   {
/* 416 */     this.isEncrypted = true;
/* 417 */     this.channel.pipeline().addBefore("splitter", "decrypt", new NettyEncryptingDecoder(CryptManager.createNetCipherInstance(2, key)));
/* 418 */     this.channel.pipeline().addBefore("prepender", "encrypt", new NettyEncryptingEncoder(CryptManager.createNetCipherInstance(1, key)));
/*     */   }
/*     */   
/*     */   public boolean getIsencrypted()
/*     */   {
/* 423 */     return this.isEncrypted;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isChannelOpen()
/*     */   {
/* 431 */     return (this.channel != null) && (this.channel.isOpen());
/*     */   }
/*     */   
/*     */   public boolean hasNoChannel()
/*     */   {
/* 436 */     return this.channel == null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public INetHandler getNetHandler()
/*     */   {
/* 444 */     return this.packetListener;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IChatComponent getExitMessage()
/*     */   {
/* 452 */     return this.terminationReason;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void disableAutoRead()
/*     */   {
/* 460 */     this.channel.config().setAutoRead(false);
/*     */   }
/*     */   
/*     */   public void setCompressionTreshold(int treshold)
/*     */   {
/* 465 */     if (treshold >= 0)
/*     */     {
/* 467 */       if ((this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder))
/*     */       {
/* 469 */         ((NettyCompressionDecoder)this.channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
/*     */       }
/*     */       else
/*     */       {
/* 473 */         this.channel.pipeline().addBefore("decoder", "decompress", new NettyCompressionDecoder(treshold));
/*     */       }
/*     */       
/* 476 */       if ((this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder))
/*     */       {
/* 478 */         ((NettyCompressionEncoder)this.channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
/*     */       }
/*     */       else
/*     */       {
/* 482 */         this.channel.pipeline().addBefore("encoder", "compress", new NettyCompressionEncoder(treshold));
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 487 */       if ((this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder))
/*     */       {
/* 489 */         this.channel.pipeline().remove("decompress");
/*     */       }
/*     */       
/* 492 */       if ((this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder))
/*     */       {
/* 494 */         this.channel.pipeline().remove("compress");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void checkDisconnected()
/*     */   {
/* 501 */     if ((this.channel != null) && (!this.channel.isOpen()))
/*     */     {
/* 503 */       if (!this.disconnected)
/*     */       {
/* 505 */         this.disconnected = true;
/*     */         
/* 507 */         if (getExitMessage() != null)
/*     */         {
/* 509 */           getNetHandler().onDisconnect(getExitMessage());
/*     */         }
/* 511 */         else if (getNetHandler() != null)
/*     */         {
/* 513 */           getNetHandler().onDisconnect(new ChatComponentText("Disconnected"));
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 518 */         logger.warn("handleDisconnection() called twice");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static class InboundHandlerTuplePacketListener
/*     */   {
/*     */     private final Packet packet;
/*     */     private final GenericFutureListener<? extends Future<? super Void>>[] futureListeners;
/*     */     
/*     */     public InboundHandlerTuplePacketListener(Packet inPacket, GenericFutureListener<? extends Future<? super Void>>... inFutureListeners)
/*     */     {
/* 530 */       this.packet = inPacket;
/* 531 */       this.futureListeners = inFutureListeners;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\NetworkManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */