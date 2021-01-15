/*     */ package net.minecraft.network;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import io.netty.bootstrap.ServerBootstrap;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.epoll.Epoll;
/*     */ import io.netty.channel.epoll.EpollEventLoopGroup;
/*     */ import io.netty.channel.epoll.EpollServerSocketChannel;
/*     */ import io.netty.channel.local.LocalAddress;
/*     */ import io.netty.channel.local.LocalEventLoopGroup;
/*     */ import io.netty.channel.local.LocalServerChannel;
/*     */ import io.netty.channel.nio.NioEventLoopGroup;
/*     */ import io.netty.channel.socket.ServerSocketChannel;
/*     */ import io.netty.channel.socket.nio.NioServerSocketChannel;
/*     */ import io.netty.handler.timeout.ReadTimeoutHandler;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.client.network.NetHandlerHandshakeMemory;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.network.play.server.S40PacketDisconnect;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.network.NetHandlerHandshakeTCP;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.LazyLoadBase;
/*     */ import net.minecraft.util.MessageDeserializer;
/*     */ import net.minecraft.util.MessageDeserializer2;
/*     */ import net.minecraft.util.MessageSerializer;
/*     */ import net.minecraft.util.MessageSerializer2;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class NetworkSystem
/*     */ {
/*  50 */   private static final Logger logger = ;
/*  51 */   public static final LazyLoadBase<NioEventLoopGroup> eventLoops = new LazyLoadBase()
/*     */   {
/*     */     protected NioEventLoopGroup load()
/*     */     {
/*  55 */       return new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Server IO #%d").setDaemon(true).build());
/*     */     }
/*     */   };
/*  58 */   public static final LazyLoadBase<EpollEventLoopGroup> field_181141_b = new LazyLoadBase()
/*     */   {
/*     */     protected EpollEventLoopGroup load()
/*     */     {
/*  62 */       return new EpollEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Epoll Server IO #%d").setDaemon(true).build());
/*     */     }
/*     */   };
/*  65 */   public static final LazyLoadBase<LocalEventLoopGroup> SERVER_LOCAL_EVENTLOOP = new LazyLoadBase()
/*     */   {
/*     */     protected LocalEventLoopGroup load()
/*     */     {
/*  69 */       return new LocalEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Local Server IO #%d").setDaemon(true).build());
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */   private final MinecraftServer mcServer;
/*     */   
/*     */   public volatile boolean isAlive;
/*     */   
/*  78 */   private final List<ChannelFuture> endpoints = Collections.synchronizedList(Lists.newArrayList());
/*  79 */   private final List<NetworkManager> networkManagers = Collections.synchronizedList(Lists.newArrayList());
/*     */   
/*     */   public NetworkSystem(MinecraftServer server)
/*     */   {
/*  83 */     this.mcServer = server;
/*  84 */     this.isAlive = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addLanEndpoint(InetAddress address, int port)
/*     */     throws IOException
/*     */   {
/*  92 */     synchronized (this.endpoints)
/*     */     {
/*     */       Class<? extends ServerSocketChannel> oclass;
/*     */       
/*     */       LazyLoadBase<? extends EventLoopGroup> lazyloadbase;
/*  97 */       if ((Epoll.isAvailable()) && (this.mcServer.func_181035_ah()))
/*     */       {
/*  99 */         Class<? extends ServerSocketChannel> oclass = EpollServerSocketChannel.class;
/* 100 */         LazyLoadBase<? extends EventLoopGroup> lazyloadbase = field_181141_b;
/* 101 */         logger.info("Using epoll channel type");
/*     */       }
/*     */       else
/*     */       {
/* 105 */         oclass = NioServerSocketChannel.class;
/* 106 */         lazyloadbase = eventLoops;
/* 107 */         logger.info("Using default channel type");
/*     */       }
/*     */       
/* 110 */       this.endpoints.add(
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
/* 129 */         ((ServerBootstrap)((ServerBootstrap)new ServerBootstrap().channel(oclass)).childHandler(new ChannelInitializer()
/*     */         {
/*     */           protected void initChannel(Channel p_initChannel_1_) throws Exception
/*     */           {
/*     */             try
/*     */             {
/* 116 */               p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
/*     */             }
/*     */             catch (ChannelException localChannelException) {}
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 123 */             p_initChannel_1_.pipeline().addLast("timeout", new ReadTimeoutHandler(30)).addLast("legacy_query", new PingResponseHandler(NetworkSystem.this)).addLast("splitter", new MessageDeserializer2()).addLast("decoder", new MessageDeserializer(EnumPacketDirection.SERVERBOUND)).addLast("prepender", new MessageSerializer2()).addLast("encoder", new MessageSerializer(EnumPacketDirection.CLIENTBOUND));
/* 124 */             NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.SERVERBOUND);
/* 125 */             NetworkSystem.this.networkManagers.add(networkmanager);
/* 126 */             p_initChannel_1_.pipeline().addLast("packet_handler", networkmanager);
/* 127 */             networkmanager.setNetHandler(new NetHandlerHandshakeTCP(NetworkSystem.this.mcServer, networkmanager));
/*     */           }
/* 129 */         }).group((EventLoopGroup)lazyloadbase.getValue()).localAddress(address, port)).bind().syncUninterruptibly());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SocketAddress addLocalEndpoint()
/*     */   {
/* 140 */     synchronized (this.endpoints)
/*     */     {
/* 142 */       ChannelFuture channelfuture = 
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 151 */         ((ServerBootstrap)((ServerBootstrap)new ServerBootstrap().channel(LocalServerChannel.class)).childHandler(new ChannelInitializer()
/*     */         {
/*     */           protected void initChannel(Channel p_initChannel_1_) throws Exception
/*     */           {
/* 146 */             NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.SERVERBOUND);
/* 147 */             networkmanager.setNetHandler(new NetHandlerHandshakeMemory(NetworkSystem.this.mcServer, networkmanager));
/* 148 */             NetworkSystem.this.networkManagers.add(networkmanager);
/* 149 */             p_initChannel_1_.pipeline().addLast("packet_handler", networkmanager);
/*     */           }
/* 151 */         }).group((EventLoopGroup)eventLoops.getValue()).localAddress(LocalAddress.ANY)).bind().syncUninterruptibly();
/* 152 */       this.endpoints.add(channelfuture);
/*     */     }
/*     */     ChannelFuture channelfuture;
/* 155 */     return channelfuture.channel().localAddress();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void terminateEndpoints()
/*     */   {
/* 163 */     this.isAlive = false;
/*     */     
/* 165 */     for (ChannelFuture channelfuture : this.endpoints)
/*     */     {
/*     */       try
/*     */       {
/* 169 */         channelfuture.channel().close().sync();
/*     */       }
/*     */       catch (InterruptedException var4)
/*     */       {
/* 173 */         logger.error("Interrupted whilst closing channel");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void networkTick()
/*     */   {
/* 184 */     synchronized (this.networkManagers)
/*     */     {
/* 186 */       Iterator<NetworkManager> iterator = this.networkManagers.iterator();
/*     */       
/* 188 */       while (iterator.hasNext())
/*     */       {
/* 190 */         final NetworkManager networkmanager = (NetworkManager)iterator.next();
/*     */         
/* 192 */         if (!networkmanager.hasNoChannel())
/*     */         {
/* 194 */           if (!networkmanager.isChannelOpen())
/*     */           {
/* 196 */             iterator.remove();
/* 197 */             networkmanager.checkDisconnected();
/*     */           }
/*     */           else
/*     */           {
/*     */             try
/*     */             {
/* 203 */               networkmanager.processReceivedPackets();
/*     */             }
/*     */             catch (Exception exception)
/*     */             {
/* 207 */               if (networkmanager.isLocalChannel())
/*     */               {
/* 209 */                 CrashReport crashreport = CrashReport.makeCrashReport(exception, "Ticking memory connection");
/* 210 */                 CrashReportCategory crashreportcategory = crashreport.makeCategory("Ticking connection");
/* 211 */                 crashreportcategory.addCrashSectionCallable("Connection", new Callable()
/*     */                 {
/*     */                   public String call() throws Exception
/*     */                   {
/* 215 */                     return networkmanager.toString();
/*     */                   }
/* 217 */                 });
/* 218 */                 throw new net.minecraft.util.ReportedException(crashreport);
/*     */               }
/*     */               
/* 221 */               logger.warn("Failed to handle packet for " + networkmanager.getRemoteAddress(), exception);
/* 222 */               final ChatComponentText chatcomponenttext = new ChatComponentText("Internal server error");
/* 223 */               networkmanager.sendPacket(new S40PacketDisconnect(chatcomponenttext), new GenericFutureListener()
/*     */               {
/*     */                 public void operationComplete(Future<? super Void> p_operationComplete_1_) throws Exception
/*     */                 {
/* 227 */                   networkmanager.closeChannel(chatcomponenttext);
/*     */                 }
/* 229 */               }, new GenericFutureListener[0]);
/* 230 */               networkmanager.disableAutoRead();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public MinecraftServer getServer()
/*     */   {
/* 240 */     return this.mcServer;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\NetworkSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */