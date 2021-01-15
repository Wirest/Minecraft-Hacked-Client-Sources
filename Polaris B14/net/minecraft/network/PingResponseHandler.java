/*     */ package net.minecraft.network;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class PingResponseHandler
/*     */   extends ChannelInboundHandlerAdapter
/*     */ {
/*  16 */   private static final Logger logger = ;
/*     */   private NetworkSystem networkSystem;
/*     */   
/*     */   public PingResponseHandler(NetworkSystem networkSystemIn)
/*     */   {
/*  21 */     this.networkSystem = networkSystemIn;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void channelRead(ChannelHandlerContext p_channelRead_1_, Object p_channelRead_2_)
/*     */     throws java.lang.Exception
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_2
/*     */     //   1: checkcast 36	io/netty/buffer/ByteBuf
/*     */     //   4: astore_3
/*     */     //   5: aload_3
/*     */     //   6: invokevirtual 40	io/netty/buffer/ByteBuf:markReaderIndex	()Lio/netty/buffer/ByteBuf;
/*     */     //   9: pop
/*     */     //   10: iconst_1
/*     */     //   11: istore 4
/*     */     //   13: aload_3
/*     */     //   14: invokevirtual 44	io/netty/buffer/ByteBuf:readUnsignedByte	()S
/*     */     //   17: sipush 254
/*     */     //   20: if_icmpne +756 -> 776
/*     */     //   23: aload_1
/*     */     //   24: invokeinterface 50 1 0
/*     */     //   29: invokeinterface 56 1 0
/*     */     //   34: checkcast 58	java/net/InetSocketAddress
/*     */     //   37: astore 5
/*     */     //   39: aload_0
/*     */     //   40: getfield 25	net/minecraft/network/PingResponseHandler:networkSystem	Lnet/minecraft/network/NetworkSystem;
/*     */     //   43: invokevirtual 64	net/minecraft/network/NetworkSystem:getServer	()Lnet/minecraft/server/MinecraftServer;
/*     */     //   46: astore 6
/*     */     //   48: aload_3
/*     */     //   49: invokevirtual 68	io/netty/buffer/ByteBuf:readableBytes	()I
/*     */     //   52: istore 7
/*     */     //   54: iload 7
/*     */     //   56: tableswitch	default:+262->318, 0:+24->80, 1:+112->168
/*     */     //   80: getstatic 19	net/minecraft/network/PingResponseHandler:logger	Lorg/apache/logging/log4j/Logger;
/*     */     //   83: ldc 74
/*     */     //   85: iconst_2
/*     */     //   86: anewarray 70	java/lang/Object
/*     */     //   89: dup
/*     */     //   90: iconst_0
/*     */     //   91: aload 5
/*     */     //   93: invokevirtual 78	java/net/InetSocketAddress:getAddress	()Ljava/net/InetAddress;
/*     */     //   96: aastore
/*     */     //   97: dup
/*     */     //   98: iconst_1
/*     */     //   99: aload 5
/*     */     //   101: invokevirtual 81	java/net/InetSocketAddress:getPort	()I
/*     */     //   104: invokestatic 87	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   107: aastore
/*     */     //   108: invokeinterface 93 3 0
/*     */     //   113: ldc 95
/*     */     //   115: iconst_3
/*     */     //   116: anewarray 70	java/lang/Object
/*     */     //   119: dup
/*     */     //   120: iconst_0
/*     */     //   121: aload 6
/*     */     //   123: invokevirtual 99	net/minecraft/server/MinecraftServer:getMOTD	()Ljava/lang/String;
/*     */     //   126: aastore
/*     */     //   127: dup
/*     */     //   128: iconst_1
/*     */     //   129: aload 6
/*     */     //   131: invokevirtual 102	net/minecraft/server/MinecraftServer:getCurrentPlayerCount	()I
/*     */     //   134: invokestatic 87	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   137: aastore
/*     */     //   138: dup
/*     */     //   139: iconst_2
/*     */     //   140: aload 6
/*     */     //   142: invokevirtual 105	net/minecraft/server/MinecraftServer:getMaxPlayers	()I
/*     */     //   145: invokestatic 87	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   148: aastore
/*     */     //   149: invokestatic 111	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   152: astore 8
/*     */     //   154: aload_0
/*     */     //   155: aload_1
/*     */     //   156: aload_0
/*     */     //   157: aload 8
/*     */     //   159: invokespecial 115	net/minecraft/network/PingResponseHandler:getStringBuffer	(Ljava/lang/String;)Lio/netty/buffer/ByteBuf;
/*     */     //   162: invokespecial 119	net/minecraft/network/PingResponseHandler:writeAndFlush	(Lio/netty/channel/ChannelHandlerContext;Lio/netty/buffer/ByteBuf;)V
/*     */     //   165: goto +483 -> 648
/*     */     //   168: aload_3
/*     */     //   169: invokevirtual 44	io/netty/buffer/ByteBuf:readUnsignedByte	()S
/*     */     //   172: iconst_1
/*     */     //   173: if_icmpeq +41 -> 214
/*     */     //   176: iload 4
/*     */     //   178: ifeq +35 -> 213
/*     */     //   181: aload_3
/*     */     //   182: invokevirtual 122	io/netty/buffer/ByteBuf:resetReaderIndex	()Lio/netty/buffer/ByteBuf;
/*     */     //   185: pop
/*     */     //   186: aload_1
/*     */     //   187: invokeinterface 50 1 0
/*     */     //   192: invokeinterface 126 1 0
/*     */     //   197: ldc -128
/*     */     //   199: invokeinterface 134 2 0
/*     */     //   204: pop
/*     */     //   205: aload_1
/*     */     //   206: aload_2
/*     */     //   207: invokeinterface 138 2 0
/*     */     //   212: pop
/*     */     //   213: return
/*     */     //   214: getstatic 19	net/minecraft/network/PingResponseHandler:logger	Lorg/apache/logging/log4j/Logger;
/*     */     //   217: ldc -116
/*     */     //   219: iconst_2
/*     */     //   220: anewarray 70	java/lang/Object
/*     */     //   223: dup
/*     */     //   224: iconst_0
/*     */     //   225: aload 5
/*     */     //   227: invokevirtual 78	java/net/InetSocketAddress:getAddress	()Ljava/net/InetAddress;
/*     */     //   230: aastore
/*     */     //   231: dup
/*     */     //   232: iconst_1
/*     */     //   233: aload 5
/*     */     //   235: invokevirtual 81	java/net/InetSocketAddress:getPort	()I
/*     */     //   238: invokestatic 87	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   241: aastore
/*     */     //   242: invokeinterface 93 3 0
/*     */     //   247: ldc -114
/*     */     //   249: iconst_5
/*     */     //   250: anewarray 70	java/lang/Object
/*     */     //   253: dup
/*     */     //   254: iconst_0
/*     */     //   255: bipush 127
/*     */     //   257: invokestatic 87	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   260: aastore
/*     */     //   261: dup
/*     */     //   262: iconst_1
/*     */     //   263: aload 6
/*     */     //   265: invokevirtual 145	net/minecraft/server/MinecraftServer:getMinecraftVersion	()Ljava/lang/String;
/*     */     //   268: aastore
/*     */     //   269: dup
/*     */     //   270: iconst_2
/*     */     //   271: aload 6
/*     */     //   273: invokevirtual 99	net/minecraft/server/MinecraftServer:getMOTD	()Ljava/lang/String;
/*     */     //   276: aastore
/*     */     //   277: dup
/*     */     //   278: iconst_3
/*     */     //   279: aload 6
/*     */     //   281: invokevirtual 102	net/minecraft/server/MinecraftServer:getCurrentPlayerCount	()I
/*     */     //   284: invokestatic 87	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   287: aastore
/*     */     //   288: dup
/*     */     //   289: iconst_4
/*     */     //   290: aload 6
/*     */     //   292: invokevirtual 105	net/minecraft/server/MinecraftServer:getMaxPlayers	()I
/*     */     //   295: invokestatic 87	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   298: aastore
/*     */     //   299: invokestatic 111	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   302: astore 9
/*     */     //   304: aload_0
/*     */     //   305: aload_1
/*     */     //   306: aload_0
/*     */     //   307: aload 9
/*     */     //   309: invokespecial 115	net/minecraft/network/PingResponseHandler:getStringBuffer	(Ljava/lang/String;)Lio/netty/buffer/ByteBuf;
/*     */     //   312: invokespecial 119	net/minecraft/network/PingResponseHandler:writeAndFlush	(Lio/netty/channel/ChannelHandlerContext;Lio/netty/buffer/ByteBuf;)V
/*     */     //   315: goto +333 -> 648
/*     */     //   318: aload_3
/*     */     //   319: invokevirtual 44	io/netty/buffer/ByteBuf:readUnsignedByte	()S
/*     */     //   322: iconst_1
/*     */     //   323: if_icmpne +7 -> 330
/*     */     //   326: iconst_1
/*     */     //   327: goto +4 -> 331
/*     */     //   330: iconst_0
/*     */     //   331: istore 10
/*     */     //   333: iload 10
/*     */     //   335: aload_3
/*     */     //   336: invokevirtual 44	io/netty/buffer/ByteBuf:readUnsignedByte	()S
/*     */     //   339: sipush 250
/*     */     //   342: if_icmpne +7 -> 349
/*     */     //   345: iconst_1
/*     */     //   346: goto +4 -> 350
/*     */     //   349: iconst_0
/*     */     //   350: iand
/*     */     //   351: istore 10
/*     */     //   353: iload 10
/*     */     //   355: ldc -109
/*     */     //   357: new 107	java/lang/String
/*     */     //   360: dup
/*     */     //   361: aload_3
/*     */     //   362: aload_3
/*     */     //   363: invokevirtual 150	io/netty/buffer/ByteBuf:readShort	()S
/*     */     //   366: iconst_2
/*     */     //   367: imul
/*     */     //   368: invokevirtual 154	io/netty/buffer/ByteBuf:readBytes	(I)Lio/netty/buffer/ByteBuf;
/*     */     //   371: invokevirtual 158	io/netty/buffer/ByteBuf:array	()[B
/*     */     //   374: getstatic 164	com/google/common/base/Charsets:UTF_16BE	Ljava/nio/charset/Charset;
/*     */     //   377: invokespecial 167	java/lang/String:<init>	([BLjava/nio/charset/Charset;)V
/*     */     //   380: invokevirtual 171	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   383: iand
/*     */     //   384: istore 10
/*     */     //   386: aload_3
/*     */     //   387: invokevirtual 174	io/netty/buffer/ByteBuf:readUnsignedShort	()I
/*     */     //   390: istore 11
/*     */     //   392: iload 10
/*     */     //   394: aload_3
/*     */     //   395: invokevirtual 44	io/netty/buffer/ByteBuf:readUnsignedByte	()S
/*     */     //   398: bipush 73
/*     */     //   400: if_icmplt +7 -> 407
/*     */     //   403: iconst_1
/*     */     //   404: goto +4 -> 408
/*     */     //   407: iconst_0
/*     */     //   408: iand
/*     */     //   409: istore 10
/*     */     //   411: iload 10
/*     */     //   413: iconst_3
/*     */     //   414: aload_3
/*     */     //   415: aload_3
/*     */     //   416: invokevirtual 150	io/netty/buffer/ByteBuf:readShort	()S
/*     */     //   419: iconst_2
/*     */     //   420: imul
/*     */     //   421: invokevirtual 154	io/netty/buffer/ByteBuf:readBytes	(I)Lio/netty/buffer/ByteBuf;
/*     */     //   424: invokevirtual 158	io/netty/buffer/ByteBuf:array	()[B
/*     */     //   427: arraylength
/*     */     //   428: iadd
/*     */     //   429: iconst_4
/*     */     //   430: iadd
/*     */     //   431: iload 11
/*     */     //   433: if_icmpne +7 -> 440
/*     */     //   436: iconst_1
/*     */     //   437: goto +4 -> 441
/*     */     //   440: iconst_0
/*     */     //   441: iand
/*     */     //   442: istore 10
/*     */     //   444: iload 10
/*     */     //   446: aload_3
/*     */     //   447: invokevirtual 177	io/netty/buffer/ByteBuf:readInt	()I
/*     */     //   450: ldc -78
/*     */     //   452: if_icmpgt +7 -> 459
/*     */     //   455: iconst_1
/*     */     //   456: goto +4 -> 460
/*     */     //   459: iconst_0
/*     */     //   460: iand
/*     */     //   461: istore 10
/*     */     //   463: iload 10
/*     */     //   465: aload_3
/*     */     //   466: invokevirtual 68	io/netty/buffer/ByteBuf:readableBytes	()I
/*     */     //   469: ifne +7 -> 476
/*     */     //   472: iconst_1
/*     */     //   473: goto +4 -> 477
/*     */     //   476: iconst_0
/*     */     //   477: iand
/*     */     //   478: istore 10
/*     */     //   480: iload 10
/*     */     //   482: ifne +41 -> 523
/*     */     //   485: iload 4
/*     */     //   487: ifeq +35 -> 522
/*     */     //   490: aload_3
/*     */     //   491: invokevirtual 122	io/netty/buffer/ByteBuf:resetReaderIndex	()Lio/netty/buffer/ByteBuf;
/*     */     //   494: pop
/*     */     //   495: aload_1
/*     */     //   496: invokeinterface 50 1 0
/*     */     //   501: invokeinterface 126 1 0
/*     */     //   506: ldc -128
/*     */     //   508: invokeinterface 134 2 0
/*     */     //   513: pop
/*     */     //   514: aload_1
/*     */     //   515: aload_2
/*     */     //   516: invokeinterface 138 2 0
/*     */     //   521: pop
/*     */     //   522: return
/*     */     //   523: getstatic 19	net/minecraft/network/PingResponseHandler:logger	Lorg/apache/logging/log4j/Logger;
/*     */     //   526: ldc -76
/*     */     //   528: iconst_2
/*     */     //   529: anewarray 70	java/lang/Object
/*     */     //   532: dup
/*     */     //   533: iconst_0
/*     */     //   534: aload 5
/*     */     //   536: invokevirtual 78	java/net/InetSocketAddress:getAddress	()Ljava/net/InetAddress;
/*     */     //   539: aastore
/*     */     //   540: dup
/*     */     //   541: iconst_1
/*     */     //   542: aload 5
/*     */     //   544: invokevirtual 81	java/net/InetSocketAddress:getPort	()I
/*     */     //   547: invokestatic 87	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   550: aastore
/*     */     //   551: invokeinterface 93 3 0
/*     */     //   556: ldc -114
/*     */     //   558: iconst_5
/*     */     //   559: anewarray 70	java/lang/Object
/*     */     //   562: dup
/*     */     //   563: iconst_0
/*     */     //   564: bipush 127
/*     */     //   566: invokestatic 87	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   569: aastore
/*     */     //   570: dup
/*     */     //   571: iconst_1
/*     */     //   572: aload 6
/*     */     //   574: invokevirtual 145	net/minecraft/server/MinecraftServer:getMinecraftVersion	()Ljava/lang/String;
/*     */     //   577: aastore
/*     */     //   578: dup
/*     */     //   579: iconst_2
/*     */     //   580: aload 6
/*     */     //   582: invokevirtual 99	net/minecraft/server/MinecraftServer:getMOTD	()Ljava/lang/String;
/*     */     //   585: aastore
/*     */     //   586: dup
/*     */     //   587: iconst_3
/*     */     //   588: aload 6
/*     */     //   590: invokevirtual 102	net/minecraft/server/MinecraftServer:getCurrentPlayerCount	()I
/*     */     //   593: invokestatic 87	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   596: aastore
/*     */     //   597: dup
/*     */     //   598: iconst_4
/*     */     //   599: aload 6
/*     */     //   601: invokevirtual 105	net/minecraft/server/MinecraftServer:getMaxPlayers	()I
/*     */     //   604: invokestatic 87	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   607: aastore
/*     */     //   608: invokestatic 111	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   611: astore 12
/*     */     //   613: aload_0
/*     */     //   614: aload 12
/*     */     //   616: invokespecial 115	net/minecraft/network/PingResponseHandler:getStringBuffer	(Ljava/lang/String;)Lio/netty/buffer/ByteBuf;
/*     */     //   619: astore 13
/*     */     //   621: aload_0
/*     */     //   622: aload_1
/*     */     //   623: aload 13
/*     */     //   625: invokespecial 119	net/minecraft/network/PingResponseHandler:writeAndFlush	(Lio/netty/channel/ChannelHandlerContext;Lio/netty/buffer/ByteBuf;)V
/*     */     //   628: goto +14 -> 642
/*     */     //   631: astore 14
/*     */     //   633: aload 13
/*     */     //   635: invokevirtual 186	io/netty/buffer/ByteBuf:release	()Z
/*     */     //   638: pop
/*     */     //   639: aload 14
/*     */     //   641: athrow
/*     */     //   642: aload 13
/*     */     //   644: invokevirtual 186	io/netty/buffer/ByteBuf:release	()Z
/*     */     //   647: pop
/*     */     //   648: aload_3
/*     */     //   649: invokevirtual 186	io/netty/buffer/ByteBuf:release	()Z
/*     */     //   652: pop
/*     */     //   653: iconst_0
/*     */     //   654: istore 4
/*     */     //   656: iload 4
/*     */     //   658: ifeq +35 -> 693
/*     */     //   661: aload_3
/*     */     //   662: invokevirtual 122	io/netty/buffer/ByteBuf:resetReaderIndex	()Lio/netty/buffer/ByteBuf;
/*     */     //   665: pop
/*     */     //   666: aload_1
/*     */     //   667: invokeinterface 50 1 0
/*     */     //   672: invokeinterface 126 1 0
/*     */     //   677: ldc -128
/*     */     //   679: invokeinterface 134 2 0
/*     */     //   684: pop
/*     */     //   685: aload_1
/*     */     //   686: aload_2
/*     */     //   687: invokeinterface 138 2 0
/*     */     //   692: pop
/*     */     //   693: return
/*     */     //   694: astore 5
/*     */     //   696: iload 4
/*     */     //   698: ifeq +35 -> 733
/*     */     //   701: aload_3
/*     */     //   702: invokevirtual 122	io/netty/buffer/ByteBuf:resetReaderIndex	()Lio/netty/buffer/ByteBuf;
/*     */     //   705: pop
/*     */     //   706: aload_1
/*     */     //   707: invokeinterface 50 1 0
/*     */     //   712: invokeinterface 126 1 0
/*     */     //   717: ldc -128
/*     */     //   719: invokeinterface 134 2 0
/*     */     //   724: pop
/*     */     //   725: aload_1
/*     */     //   726: aload_2
/*     */     //   727: invokeinterface 138 2 0
/*     */     //   732: pop
/*     */     //   733: return
/*     */     //   734: astore 15
/*     */     //   736: iload 4
/*     */     //   738: ifeq +35 -> 773
/*     */     //   741: aload_3
/*     */     //   742: invokevirtual 122	io/netty/buffer/ByteBuf:resetReaderIndex	()Lio/netty/buffer/ByteBuf;
/*     */     //   745: pop
/*     */     //   746: aload_1
/*     */     //   747: invokeinterface 50 1 0
/*     */     //   752: invokeinterface 126 1 0
/*     */     //   757: ldc -128
/*     */     //   759: invokeinterface 134 2 0
/*     */     //   764: pop
/*     */     //   765: aload_1
/*     */     //   766: aload_2
/*     */     //   767: invokeinterface 138 2 0
/*     */     //   772: pop
/*     */     //   773: aload 15
/*     */     //   775: athrow
/*     */     //   776: iload 4
/*     */     //   778: ifeq +35 -> 813
/*     */     //   781: aload_3
/*     */     //   782: invokevirtual 122	io/netty/buffer/ByteBuf:resetReaderIndex	()Lio/netty/buffer/ByteBuf;
/*     */     //   785: pop
/*     */     //   786: aload_1
/*     */     //   787: invokeinterface 50 1 0
/*     */     //   792: invokeinterface 126 1 0
/*     */     //   797: ldc -128
/*     */     //   799: invokeinterface 134 2 0
/*     */     //   804: pop
/*     */     //   805: aload_1
/*     */     //   806: aload_2
/*     */     //   807: invokeinterface 138 2 0
/*     */     //   812: pop
/*     */     //   813: return
/*     */     // Line number table:
/*     */     //   Java source line #26	-> byte code offset #0
/*     */     //   Java source line #27	-> byte code offset #5
/*     */     //   Java source line #28	-> byte code offset #10
/*     */     //   Java source line #32	-> byte code offset #13
/*     */     //   Java source line #34	-> byte code offset #23
/*     */     //   Java source line #35	-> byte code offset #39
/*     */     //   Java source line #36	-> byte code offset #48
/*     */     //   Java source line #38	-> byte code offset #54
/*     */     //   Java source line #41	-> byte code offset #80
/*     */     //   Java source line #42	-> byte code offset #113
/*     */     //   Java source line #43	-> byte code offset #154
/*     */     //   Java source line #44	-> byte code offset #165
/*     */     //   Java source line #47	-> byte code offset #168
/*     */     //   Java source line #97	-> byte code offset #176
/*     */     //   Java source line #99	-> byte code offset #181
/*     */     //   Java source line #100	-> byte code offset #186
/*     */     //   Java source line #101	-> byte code offset #205
/*     */     //   Java source line #49	-> byte code offset #213
/*     */     //   Java source line #52	-> byte code offset #214
/*     */     //   Java source line #53	-> byte code offset #247
/*     */     //   Java source line #54	-> byte code offset #304
/*     */     //   Java source line #55	-> byte code offset #315
/*     */     //   Java source line #58	-> byte code offset #318
/*     */     //   Java source line #59	-> byte code offset #333
/*     */     //   Java source line #60	-> byte code offset #353
/*     */     //   Java source line #61	-> byte code offset #386
/*     */     //   Java source line #62	-> byte code offset #392
/*     */     //   Java source line #63	-> byte code offset #411
/*     */     //   Java source line #64	-> byte code offset #444
/*     */     //   Java source line #65	-> byte code offset #463
/*     */     //   Java source line #67	-> byte code offset #480
/*     */     //   Java source line #97	-> byte code offset #485
/*     */     //   Java source line #99	-> byte code offset #490
/*     */     //   Java source line #100	-> byte code offset #495
/*     */     //   Java source line #101	-> byte code offset #514
/*     */     //   Java source line #69	-> byte code offset #522
/*     */     //   Java source line #72	-> byte code offset #523
/*     */     //   Java source line #73	-> byte code offset #556
/*     */     //   Java source line #74	-> byte code offset #613
/*     */     //   Java source line #78	-> byte code offset #621
/*     */     //   Java source line #79	-> byte code offset #628
/*     */     //   Java source line #81	-> byte code offset #631
/*     */     //   Java source line #82	-> byte code offset #633
/*     */     //   Java source line #83	-> byte code offset #639
/*     */     //   Java source line #82	-> byte code offset #642
/*     */     //   Java source line #86	-> byte code offset #648
/*     */     //   Java source line #87	-> byte code offset #653
/*     */     //   Java source line #97	-> byte code offset #656
/*     */     //   Java source line #99	-> byte code offset #661
/*     */     //   Java source line #100	-> byte code offset #666
/*     */     //   Java source line #101	-> byte code offset #685
/*     */     //   Java source line #88	-> byte code offset #693
/*     */     //   Java source line #91	-> byte code offset #694
/*     */     //   Java source line #97	-> byte code offset #696
/*     */     //   Java source line #99	-> byte code offset #701
/*     */     //   Java source line #100	-> byte code offset #706
/*     */     //   Java source line #101	-> byte code offset #725
/*     */     //   Java source line #93	-> byte code offset #733
/*     */     //   Java source line #96	-> byte code offset #734
/*     */     //   Java source line #97	-> byte code offset #736
/*     */     //   Java source line #99	-> byte code offset #741
/*     */     //   Java source line #100	-> byte code offset #746
/*     */     //   Java source line #101	-> byte code offset #765
/*     */     //   Java source line #103	-> byte code offset #773
/*     */     //   Java source line #97	-> byte code offset #776
/*     */     //   Java source line #99	-> byte code offset #781
/*     */     //   Java source line #100	-> byte code offset #786
/*     */     //   Java source line #101	-> byte code offset #805
/*     */     //   Java source line #104	-> byte code offset #813
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	814	0	this	PingResponseHandler
/*     */     //   0	814	1	p_channelRead_1_	ChannelHandlerContext
/*     */     //   0	814	2	p_channelRead_2_	Object
/*     */     //   4	778	3	bytebuf	ByteBuf
/*     */     //   11	766	4	flag	boolean
/*     */     //   37	506	5	inetsocketaddress	java.net.InetSocketAddress
/*     */     //   694	3	5	var21	RuntimeException
/*     */     //   46	554	6	minecraftserver	net.minecraft.server.MinecraftServer
/*     */     //   52	3	7	i	int
/*     */     //   152	6	8	s2	String
/*     */     //   302	6	9	s	String
/*     */     //   331	150	10	flag1	boolean
/*     */     //   390	42	11	j	int
/*     */     //   611	4	12	s1	String
/*     */     //   619	24	13	bytebuf1	ByteBuf
/*     */     //   631	9	14	localObject1	Object
/*     */     //   734	40	15	localObject2	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   621	631	631	finally
/*     */     //   13	176	694	java/lang/RuntimeException
/*     */     //   214	485	694	java/lang/RuntimeException
/*     */     //   523	656	694	java/lang/RuntimeException
/*     */     //   13	176	734	finally
/*     */     //   214	485	734	finally
/*     */     //   523	656	734	finally
/*     */     //   694	696	734	finally
/*     */   }
/*     */   
/*     */   private void writeAndFlush(ChannelHandlerContext ctx, ByteBuf data)
/*     */   {
/* 108 */     ctx.pipeline().firstContext().writeAndFlush(data).addListener(ChannelFutureListener.CLOSE);
/*     */   }
/*     */   
/*     */   private ByteBuf getStringBuffer(String string)
/*     */   {
/* 113 */     ByteBuf bytebuf = Unpooled.buffer();
/* 114 */     bytebuf.writeByte(255);
/* 115 */     char[] achar = string.toCharArray();
/* 116 */     bytebuf.writeShort(achar.length);
/*     */     char[] arrayOfChar1;
/* 118 */     int j = (arrayOfChar1 = achar).length; for (int i = 0; i < j; i++) { char c0 = arrayOfChar1[i];
/*     */       
/* 120 */       bytebuf.writeChar(c0);
/*     */     }
/*     */     
/* 123 */     return bytebuf;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\PingResponseHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */