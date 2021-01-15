/*     */ package net.minecraft.client.network;
/*     */ 
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import io.netty.bootstrap.Bootstrap;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.SimpleChannelInboundHandler;
/*     */ import io.netty.channel.socket.nio.NioSocketChannel;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ServerAddress;
/*     */ import net.minecraft.client.multiplayer.ServerData;
/*     */ import net.minecraft.network.EnumConnectionState;
/*     */ import net.minecraft.network.NetworkManager;
/*     */ import net.minecraft.network.ServerStatusResponse;
/*     */ import net.minecraft.network.ServerStatusResponse.MinecraftProtocolVersionIdentifier;
/*     */ import net.minecraft.network.ServerStatusResponse.PlayerCountData;
/*     */ import net.minecraft.network.status.INetHandlerStatusClient;
/*     */ import net.minecraft.network.status.client.C00PacketServerQuery;
/*     */ import net.minecraft.network.status.client.C01PacketPing;
/*     */ import net.minecraft.network.status.server.S00PacketServerInfo;
/*     */ import net.minecraft.network.status.server.S01PacketPong;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.LazyLoadBase;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class OldServerPinger
/*     */ {
/*  48 */   private static final Splitter PING_RESPONSE_SPLITTER = Splitter.on('\000').limit(6);
/*  49 */   private static final Logger logger = LogManager.getLogger();
/*  50 */   private final List<NetworkManager> pingDestinations = Collections.synchronizedList(Lists.newArrayList());
/*     */   
/*     */   public void ping(final ServerData server) throws UnknownHostException
/*     */   {
/*  54 */     ServerAddress serveraddress = ServerAddress.func_78860_a(server.serverIP);
/*  55 */     final NetworkManager networkmanager = NetworkManager.func_181124_a(InetAddress.getByName(serveraddress.getIP()), serveraddress.getPort(), false);
/*  56 */     this.pingDestinations.add(networkmanager);
/*  57 */     server.serverMOTD = "Nigger...";
/*  58 */     server.pingToServer = -1L;
/*  59 */     server.playerList = null;
/*  60 */     networkmanager.setNetHandler(new INetHandlerStatusClient()
/*     */     {
/*  62 */       private boolean field_147403_d = false;
/*  63 */       private boolean field_183009_e = false;
/*  64 */       private long field_175092_e = 0L;
/*     */       
/*     */       public void handleServerInfo(S00PacketServerInfo packetIn) {
/*  67 */         if (this.field_183009_e)
/*     */         {
/*  69 */           networkmanager.closeChannel(new ChatComponentText("Received unrequested status"));
/*     */         }
/*     */         else
/*     */         {
/*  73 */           this.field_183009_e = true;
/*  74 */           ServerStatusResponse serverstatusresponse = packetIn.getResponse();
/*     */           
/*  76 */           if (serverstatusresponse.getServerDescription() != null)
/*     */           {
/*  78 */             server.serverMOTD = serverstatusresponse.getServerDescription().getFormattedText();
/*     */           }
/*     */           else
/*     */           {
/*  82 */             server.serverMOTD = "";
/*     */           }
/*     */           
/*  85 */           if (serverstatusresponse.getProtocolVersionInfo() != null)
/*     */           {
/*  87 */             server.gameVersion = serverstatusresponse.getProtocolVersionInfo().getName();
/*  88 */             server.version = serverstatusresponse.getProtocolVersionInfo().getProtocol();
/*     */           }
/*     */           else
/*     */           {
/*  92 */             server.gameVersion = "Old";
/*  93 */             server.version = 0;
/*     */           }
/*     */           
/*  96 */           if (serverstatusresponse.getPlayerCountData() != null)
/*     */           {
/*  98 */             server.populationInfo = (EnumChatFormatting.GRAY + serverstatusresponse.getPlayerCountData().getOnlinePlayerCount() + EnumChatFormatting.DARK_GRAY + "/" + EnumChatFormatting.GRAY + serverstatusresponse.getPlayerCountData().getMaxPlayers());
/*     */             
/* 100 */             if (org.apache.commons.lang3.ArrayUtils.isNotEmpty(serverstatusresponse.getPlayerCountData().getPlayers()))
/*     */             {
/* 102 */               StringBuilder stringbuilder = new StringBuilder();
/*     */               GameProfile[] arrayOfGameProfile;
/* 104 */               int j = (arrayOfGameProfile = serverstatusresponse.getPlayerCountData().getPlayers()).length; for (int i = 0; i < j; i++) { GameProfile gameprofile = arrayOfGameProfile[i];
/*     */                 
/* 106 */                 if (stringbuilder.length() > 0)
/*     */                 {
/* 108 */                   stringbuilder.append("\n");
/*     */                 }
/*     */                 
/* 111 */                 stringbuilder.append(gameprofile.getName());
/*     */               }
/*     */               
/* 114 */               if (serverstatusresponse.getPlayerCountData().getPlayers().length < serverstatusresponse.getPlayerCountData().getOnlinePlayerCount())
/*     */               {
/* 116 */                 if (stringbuilder.length() > 0)
/*     */                 {
/* 118 */                   stringbuilder.append("\n");
/*     */                 }
/*     */                 
/* 121 */                 stringbuilder.append("... and ").append(serverstatusresponse.getPlayerCountData().getOnlinePlayerCount() - serverstatusresponse.getPlayerCountData().getPlayers().length).append(" more ...");
/*     */               }
/*     */               
/* 124 */               server.playerList = stringbuilder.toString();
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 129 */             server.populationInfo = (EnumChatFormatting.DARK_GRAY + "???");
/*     */           }
/*     */           
/* 132 */           if (serverstatusresponse.getFavicon() != null)
/*     */           {
/* 134 */             String s = serverstatusresponse.getFavicon();
/*     */             
/* 136 */             if (s.startsWith("data:image/png;base64,"))
/*     */             {
/* 138 */               server.setBase64EncodedIconData(s.substring("data:image/png;base64,".length()));
/*     */             }
/*     */             else
/*     */             {
/* 142 */               OldServerPinger.logger.error("Invalid server icon (unknown format)");
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 147 */             server.setBase64EncodedIconData(null);
/*     */           }
/*     */           
/* 150 */           this.field_175092_e = Minecraft.getSystemTime();
/* 151 */           networkmanager.sendPacket(new C01PacketPing(this.field_175092_e));
/* 152 */           this.field_147403_d = true;
/*     */         }
/*     */       }
/*     */       
/*     */       public void handlePong(S01PacketPong packetIn) {
/* 157 */         long i = this.field_175092_e;
/* 158 */         long j = Minecraft.getSystemTime();
/* 159 */         server.pingToServer = (j - i);
/* 160 */         networkmanager.closeChannel(new ChatComponentText("Finished"));
/*     */       }
/*     */       
/*     */       public void onDisconnect(IChatComponent reason) {
/* 164 */         if (!this.field_147403_d)
/*     */         {
/* 166 */           OldServerPinger.logger.error("Can't ping " + server.serverIP + ": " + reason.getUnformattedText());
/* 167 */           server.serverMOTD = (EnumChatFormatting.DARK_RED + "Can't connect to server.");
/* 168 */           server.populationInfo = "";
/* 169 */           OldServerPinger.this.tryCompatibilityPing(server);
/*     */         }
/*     */       }
/*     */     });
/*     */     
/*     */     try
/*     */     {
/* 176 */       networkmanager.sendPacket(new net.minecraft.network.handshake.client.C00Handshake(47, serveraddress.getIP(), serveraddress.getPort(), EnumConnectionState.STATUS));
/* 177 */       networkmanager.sendPacket(new C00PacketServerQuery());
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/* 181 */       logger.error(throwable);
/*     */     }
/*     */   }
/*     */   
/*     */   private void tryCompatibilityPing(final ServerData server)
/*     */   {
/* 187 */     final ServerAddress serveraddress = ServerAddress.func_78860_a(server.serverIP);
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
/* 271 */     ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group((EventLoopGroup)NetworkManager.CLIENT_NIO_EVENTLOOP.getValue())).handler(new ChannelInitializer()
/*     */     {
/*     */       protected void initChannel(Channel p_initChannel_1_) throws Exception
/*     */       {
/*     */         try
/*     */         {
/* 194 */           p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
/*     */         }
/*     */         catch (ChannelException localChannelException) {}
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 201 */         p_initChannel_1_.pipeline().addLast(new ChannelHandler[] { new SimpleChannelInboundHandler()
/*     */         {
/*     */           /* Error */
/*     */           public void channelActive(ChannelHandlerContext p_channelActive_1_)
/*     */             throws Exception
/*     */           {
/*     */             // Byte code:
/*     */             //   0: aload_0
/*     */             //   1: aload_1
/*     */             //   2: invokespecial 36	io/netty/channel/SimpleChannelInboundHandler:channelActive	(Lio/netty/channel/ChannelHandlerContext;)V
/*     */             //   5: invokestatic 42	io/netty/buffer/Unpooled:buffer	()Lio/netty/buffer/ByteBuf;
/*     */             //   8: astore_2
/*     */             //   9: aload_2
/*     */             //   10: sipush 254
/*     */             //   13: invokevirtual 48	io/netty/buffer/ByteBuf:writeByte	(I)Lio/netty/buffer/ByteBuf;
/*     */             //   16: pop
/*     */             //   17: aload_2
/*     */             //   18: iconst_1
/*     */             //   19: invokevirtual 48	io/netty/buffer/ByteBuf:writeByte	(I)Lio/netty/buffer/ByteBuf;
/*     */             //   22: pop
/*     */             //   23: aload_2
/*     */             //   24: sipush 250
/*     */             //   27: invokevirtual 48	io/netty/buffer/ByteBuf:writeByte	(I)Lio/netty/buffer/ByteBuf;
/*     */             //   30: pop
/*     */             //   31: ldc 50
/*     */             //   33: invokevirtual 56	java/lang/String:toCharArray	()[C
/*     */             //   36: astore_3
/*     */             //   37: aload_2
/*     */             //   38: aload_3
/*     */             //   39: arraylength
/*     */             //   40: invokevirtual 59	io/netty/buffer/ByteBuf:writeShort	(I)Lio/netty/buffer/ByteBuf;
/*     */             //   43: pop
/*     */             //   44: aload_3
/*     */             //   45: dup
/*     */             //   46: astore 7
/*     */             //   48: arraylength
/*     */             //   49: istore 6
/*     */             //   51: iconst_0
/*     */             //   52: istore 5
/*     */             //   54: goto +20 -> 74
/*     */             //   57: aload 7
/*     */             //   59: iload 5
/*     */             //   61: caload
/*     */             //   62: istore 4
/*     */             //   64: aload_2
/*     */             //   65: iload 4
/*     */             //   67: invokevirtual 66	io/netty/buffer/ByteBuf:writeChar	(I)Lio/netty/buffer/ByteBuf;
/*     */             //   70: pop
/*     */             //   71: iinc 5 1
/*     */             //   74: iload 5
/*     */             //   76: iload 6
/*     */             //   78: if_icmplt -21 -> 57
/*     */             //   81: aload_2
/*     */             //   82: bipush 7
/*     */             //   84: iconst_2
/*     */             //   85: aload_0
/*     */             //   86: getfield 23	net/minecraft/client/network/OldServerPinger$2$1:val$serveraddress	Lnet/minecraft/client/multiplayer/ServerAddress;
/*     */             //   89: invokevirtual 72	net/minecraft/client/multiplayer/ServerAddress:getIP	()Ljava/lang/String;
/*     */             //   92: invokevirtual 76	java/lang/String:length	()I
/*     */             //   95: imul
/*     */             //   96: iadd
/*     */             //   97: invokevirtual 59	io/netty/buffer/ByteBuf:writeShort	(I)Lio/netty/buffer/ByteBuf;
/*     */             //   100: pop
/*     */             //   101: aload_2
/*     */             //   102: bipush 127
/*     */             //   104: invokevirtual 48	io/netty/buffer/ByteBuf:writeByte	(I)Lio/netty/buffer/ByteBuf;
/*     */             //   107: pop
/*     */             //   108: aload_0
/*     */             //   109: getfield 23	net/minecraft/client/network/OldServerPinger$2$1:val$serveraddress	Lnet/minecraft/client/multiplayer/ServerAddress;
/*     */             //   112: invokevirtual 72	net/minecraft/client/multiplayer/ServerAddress:getIP	()Ljava/lang/String;
/*     */             //   115: invokevirtual 56	java/lang/String:toCharArray	()[C
/*     */             //   118: astore_3
/*     */             //   119: aload_2
/*     */             //   120: aload_3
/*     */             //   121: arraylength
/*     */             //   122: invokevirtual 59	io/netty/buffer/ByteBuf:writeShort	(I)Lio/netty/buffer/ByteBuf;
/*     */             //   125: pop
/*     */             //   126: aload_3
/*     */             //   127: dup
/*     */             //   128: astore 7
/*     */             //   130: arraylength
/*     */             //   131: istore 6
/*     */             //   133: iconst_0
/*     */             //   134: istore 5
/*     */             //   136: goto +20 -> 156
/*     */             //   139: aload 7
/*     */             //   141: iload 5
/*     */             //   143: caload
/*     */             //   144: istore 4
/*     */             //   146: aload_2
/*     */             //   147: iload 4
/*     */             //   149: invokevirtual 66	io/netty/buffer/ByteBuf:writeChar	(I)Lio/netty/buffer/ByteBuf;
/*     */             //   152: pop
/*     */             //   153: iinc 5 1
/*     */             //   156: iload 5
/*     */             //   158: iload 6
/*     */             //   160: if_icmplt -21 -> 139
/*     */             //   163: aload_2
/*     */             //   164: aload_0
/*     */             //   165: getfield 23	net/minecraft/client/network/OldServerPinger$2$1:val$serveraddress	Lnet/minecraft/client/multiplayer/ServerAddress;
/*     */             //   168: invokevirtual 79	net/minecraft/client/multiplayer/ServerAddress:getPort	()I
/*     */             //   171: invokevirtual 82	io/netty/buffer/ByteBuf:writeInt	(I)Lio/netty/buffer/ByteBuf;
/*     */             //   174: pop
/*     */             //   175: aload_1
/*     */             //   176: invokeinterface 86 1 0
/*     */             //   181: aload_2
/*     */             //   182: invokeinterface 92 2 0
/*     */             //   187: getstatic 98	io/netty/channel/ChannelFutureListener:CLOSE_ON_FAILURE	Lio/netty/channel/ChannelFutureListener;
/*     */             //   190: invokeinterface 104 2 0
/*     */             //   195: pop
/*     */             //   196: goto +13 -> 209
/*     */             //   199: astore 8
/*     */             //   201: aload_2
/*     */             //   202: invokevirtual 110	io/netty/buffer/ByteBuf:release	()Z
/*     */             //   205: pop
/*     */             //   206: aload 8
/*     */             //   208: athrow
/*     */             //   209: aload_2
/*     */             //   210: invokevirtual 110	io/netty/buffer/ByteBuf:release	()Z
/*     */             //   213: pop
/*     */             //   214: return
/*     */             // Line number table:
/*     */             //   Java source line #205	-> byte code offset #0
/*     */             //   Java source line #206	-> byte code offset #5
/*     */             //   Java source line #210	-> byte code offset #9
/*     */             //   Java source line #211	-> byte code offset #17
/*     */             //   Java source line #212	-> byte code offset #23
/*     */             //   Java source line #213	-> byte code offset #31
/*     */             //   Java source line #214	-> byte code offset #37
/*     */             //   Java source line #216	-> byte code offset #44
/*     */             //   Java source line #218	-> byte code offset #64
/*     */             //   Java source line #216	-> byte code offset #71
/*     */             //   Java source line #221	-> byte code offset #81
/*     */             //   Java source line #222	-> byte code offset #101
/*     */             //   Java source line #223	-> byte code offset #108
/*     */             //   Java source line #224	-> byte code offset #119
/*     */             //   Java source line #226	-> byte code offset #126
/*     */             //   Java source line #228	-> byte code offset #146
/*     */             //   Java source line #226	-> byte code offset #153
/*     */             //   Java source line #231	-> byte code offset #163
/*     */             //   Java source line #232	-> byte code offset #175
/*     */             //   Java source line #233	-> byte code offset #196
/*     */             //   Java source line #235	-> byte code offset #199
/*     */             //   Java source line #236	-> byte code offset #201
/*     */             //   Java source line #237	-> byte code offset #206
/*     */             //   Java source line #236	-> byte code offset #209
/*     */             //   Java source line #238	-> byte code offset #214
/*     */             // Local variable table:
/*     */             //   start	length	slot	name	signature
/*     */             //   0	215	0	this	1
/*     */             //   0	215	1	p_channelActive_1_	ChannelHandlerContext
/*     */             //   8	202	2	bytebuf	ByteBuf
/*     */             //   36	91	3	achar	char[]
/*     */             //   62	4	4	c0	char
/*     */             //   144	4	4	c1	char
/*     */             //   52	109	5	i	int
/*     */             //   49	112	6	j	int
/*     */             //   46	94	7	arrayOfChar1	char[]
/*     */             //   199	8	8	localObject	Object
/*     */             // Exception table:
/*     */             //   from	to	target	type
/*     */             //   9	199	199	finally
/*     */           }
/*     */           
/*     */           protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, ByteBuf p_channelRead0_2_)
/*     */             throws Exception
/*     */           {
/* 241 */             short short1 = p_channelRead0_2_.readUnsignedByte();
/*     */             
/* 243 */             if (short1 == 255)
/*     */             {
/* 245 */               String s = new String(p_channelRead0_2_.readBytes(p_channelRead0_2_.readShort() * 2).array(), com.google.common.base.Charsets.UTF_16BE);
/* 246 */               String[] astring = (String[])Iterables.toArray(OldServerPinger.PING_RESPONSE_SPLITTER.split(s), String.class);
/*     */               
/* 248 */               if ("§1".equals(astring[0]))
/*     */               {
/* 250 */                 int i = MathHelper.parseIntWithDefault(astring[1], 0);
/* 251 */                 String s1 = astring[2];
/* 252 */                 String s2 = astring[3];
/* 253 */                 int j = MathHelper.parseIntWithDefault(astring[4], -1);
/* 254 */                 int k = MathHelper.parseIntWithDefault(astring[5], -1);
/* 255 */                 this.val$server.version = -1;
/* 256 */                 this.val$server.gameVersion = s1;
/* 257 */                 this.val$server.serverMOTD = s2;
/* 258 */                 this.val$server.populationInfo = (EnumChatFormatting.GRAY + j + EnumChatFormatting.DARK_GRAY + "/" + EnumChatFormatting.GRAY + k);
/*     */               }
/*     */             }
/*     */             
/* 262 */             p_channelRead0_1_.close();
/*     */           }
/*     */           
/*     */           public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_) throws Exception {
/* 266 */             p_exceptionCaught_1_.close();
/*     */           }
/*     */           
/*     */         } });
/*     */       }
/* 271 */     })).channel(NioSocketChannel.class)).connect(serveraddress.getIP(), serveraddress.getPort());
/*     */   }
/*     */   
/*     */   public void pingPendingNetworks()
/*     */   {
/* 276 */     synchronized (this.pingDestinations)
/*     */     {
/* 278 */       Iterator<NetworkManager> iterator = this.pingDestinations.iterator();
/*     */       
/* 280 */       while (iterator.hasNext())
/*     */       {
/* 282 */         NetworkManager networkmanager = (NetworkManager)iterator.next();
/*     */         
/* 284 */         if (networkmanager.isChannelOpen())
/*     */         {
/* 286 */           networkmanager.processReceivedPackets();
/*     */         }
/*     */         else
/*     */         {
/* 290 */           iterator.remove();
/* 291 */           networkmanager.checkDisconnected();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void clearPendingNetworks()
/*     */   {
/* 299 */     synchronized (this.pingDestinations)
/*     */     {
/* 301 */       Iterator<NetworkManager> iterator = this.pingDestinations.iterator();
/*     */       
/* 303 */       while (iterator.hasNext())
/*     */       {
/* 305 */         NetworkManager networkmanager = (NetworkManager)iterator.next();
/*     */         
/* 307 */         if (networkmanager.isChannelOpen())
/*     */         {
/* 309 */           iterator.remove();
/* 310 */           networkmanager.closeChannel(new ChatComponentText("Cancelled"));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\network\OldServerPinger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */