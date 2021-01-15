/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ServerData;
/*     */ import net.minecraft.client.multiplayer.ServerList;
/*     */ import net.minecraft.client.network.OldServerPinger;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerListEntryNormal
/*     */   implements GuiListExtended.IGuiListEntry
/*     */ {
/*  26 */   private static final Logger logger = ;
/*  27 */   private static final ThreadPoolExecutor field_148302_b = new ScheduledThreadPoolExecutor(5, new ThreadFactoryBuilder().setNameFormat("Server Pinger #%d").setDaemon(true).build());
/*  28 */   private static final ResourceLocation UNKNOWN_SERVER = new ResourceLocation("textures/misc/unknown_server.png");
/*  29 */   private static final ResourceLocation SERVER_SELECTION_BUTTONS = new ResourceLocation("textures/gui/server_selection.png");
/*     */   private final GuiMultiplayer field_148303_c;
/*     */   private final Minecraft mc;
/*     */   private final ServerData field_148301_e;
/*     */   private final ResourceLocation field_148306_i;
/*     */   private String field_148299_g;
/*     */   private DynamicTexture field_148305_h;
/*     */   private long field_148298_f;
/*     */   
/*     */   protected ServerListEntryNormal(GuiMultiplayer p_i45048_1_, ServerData p_i45048_2_) {
/*  39 */     this.field_148303_c = p_i45048_1_;
/*  40 */     this.field_148301_e = p_i45048_2_;
/*  41 */     this.mc = Minecraft.getMinecraft();
/*  42 */     this.field_148306_i = new ResourceLocation("servers/" + p_i45048_2_.serverIP + "/icon");
/*  43 */     this.field_148305_h = ((DynamicTexture)this.mc.getTextureManager().getTexture(this.field_148306_i));
/*     */   }
/*     */   
/*     */   public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
/*  47 */     if (!this.field_148301_e.field_78841_f) {
/*  48 */       this.field_148301_e.field_78841_f = true;
/*  49 */       this.field_148301_e.pingToServer = -2L;
/*  50 */       this.field_148301_e.serverMOTD = "";
/*  51 */       this.field_148301_e.populationInfo = "";
/*  52 */       field_148302_b.submit(new Runnable() {
/*     */         public void run() {
/*     */           try {
/*  55 */             ServerListEntryNormal.this.field_148303_c.getOldServerPinger().ping(ServerListEntryNormal.this.field_148301_e);
/*     */           } catch (UnknownHostException var2) {
/*  57 */             ServerListEntryNormal.this.field_148301_e.pingToServer = -1L;
/*  58 */             ServerListEntryNormal.this.field_148301_e.serverMOTD = (EnumChatFormatting.DARK_RED + "Can't resolve hostname");
/*     */           } catch (Exception var3) {
/*  60 */             ServerListEntryNormal.this.field_148301_e.pingToServer = -1L;
/*  61 */             ServerListEntryNormal.this.field_148301_e.serverMOTD = (EnumChatFormatting.DARK_RED + "Can't connect to server.");
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*  67 */     boolean flag = this.field_148301_e.version > 47;
/*  68 */     boolean flag1 = this.field_148301_e.version < 47;
/*  69 */     boolean flag2 = (flag) || (flag1);
/*  70 */     this.mc.fontRendererObj.drawString(this.field_148301_e.serverName, x + 32 + 3, y + 1, 16777215);
/*  71 */     List<String> list = this.mc.fontRendererObj.listFormattedStringToWidth(this.field_148301_e.serverMOTD, listWidth - 32 - 2);
/*     */     
/*  73 */     for (int i = 0; i < Math.min(list.size(), 2); i++) {
/*  74 */       this.mc.fontRendererObj.drawString((String)list.get(i), x + 32 + 3, y + 12 + this.mc.fontRendererObj.FONT_HEIGHT * i, 8421504);
/*     */     }
/*     */     
/*  77 */     String s2 = flag2 ? EnumChatFormatting.DARK_RED + this.field_148301_e.gameVersion : this.field_148301_e.populationInfo;
/*  78 */     int j = this.mc.fontRendererObj.getStringWidth(s2);
/*  79 */     this.mc.fontRendererObj.drawString(s2, x + listWidth - j - 15 - 2, y + 1, 8421504);
/*  80 */     int k = 0;
/*  81 */     String s = null;
/*     */     int l;
/*     */     String s1;
/*  84 */     if (flag2) {
/*  85 */       int l = 5;
/*  86 */       String s1 = flag ? "Client out of date!" : "Server out of date!";
/*  87 */       s = this.field_148301_e.playerList;
/*  88 */     } else if ((this.field_148301_e.field_78841_f) && (this.field_148301_e.pingToServer != -2L)) { int l;
/*  89 */       int l; if (this.field_148301_e.pingToServer < 0L) {
/*  90 */         l = 5; } else { int l;
/*  91 */         if (this.field_148301_e.pingToServer < 150L) {
/*  92 */           l = 0; } else { int l;
/*  93 */           if (this.field_148301_e.pingToServer < 300L) {
/*  94 */             l = 1; } else { int l;
/*  95 */             if (this.field_148301_e.pingToServer < 600L) {
/*  96 */               l = 2; } else { int l;
/*  97 */               if (this.field_148301_e.pingToServer < 1000L) {
/*  98 */                 l = 3;
/*     */               } else
/* 100 */                 l = 4;
/*     */             } } } }
/*     */       String s1;
/* 103 */       if (this.field_148301_e.pingToServer < 0L) {
/* 104 */         s1 = "(no connection)";
/*     */       } else {
/* 106 */         String s1 = this.field_148301_e.pingToServer + "ms";
/* 107 */         s = this.field_148301_e.playerList;
/*     */       }
/*     */     } else {
/* 110 */       k = 1;
/* 111 */       l = (int)(Minecraft.getSystemTime() / 100L + slotIndex * 2 & 0x7);
/* 112 */       if (l > 4) {
/* 113 */         l = 8 - l;
/*     */       }
/*     */       
/* 116 */       s1 = "Pinging...";
/*     */     }
/*     */     
/* 119 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 120 */     this.mc.getTextureManager().bindTexture(Gui.icons);
/* 121 */     Gui.drawModalRectWithCustomSizedTexture(x + listWidth - 15, y, k * 10, 176 + l * 8, 10, 8, 256.0F, 256.0F);
/* 122 */     if ((this.field_148301_e.getBase64EncodedIconData() != null) && (!this.field_148301_e.getBase64EncodedIconData().equals(this.field_148299_g))) {
/* 123 */       this.field_148299_g = this.field_148301_e.getBase64EncodedIconData();
/* 124 */       prepareServerIcon();
/* 125 */       this.field_148303_c.getServerList().saveServerList();
/*     */     }
/*     */     
/* 128 */     if (this.field_148305_h != null) {
/* 129 */       func_178012_a(x, y, this.field_148306_i);
/*     */     } else {
/* 131 */       func_178012_a(x, y, UNKNOWN_SERVER);
/*     */     }
/*     */     
/* 134 */     int i1 = mouseX - x;
/* 135 */     int j1 = mouseY - y;
/* 136 */     if ((i1 >= listWidth - 15) && (i1 <= listWidth - 5) && (j1 >= 0) && (j1 <= 8)) {
/* 137 */       this.field_148303_c.setHoveringText(s1);
/* 138 */     } else if ((i1 >= listWidth - j - 15 - 2) && (i1 <= listWidth - 15 - 2) && (j1 >= 0) && (j1 <= 8)) {
/* 139 */       this.field_148303_c.setHoveringText(s);
/*     */     }
/*     */     
/* 142 */     if ((this.mc.gameSettings.touchscreen) || (isSelected)) {
/* 143 */       this.mc.getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
/* 144 */       Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
/* 145 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 146 */       int k1 = mouseX - x;
/* 147 */       int l1 = mouseY - y;
/* 148 */       if (func_178013_b()) {
/* 149 */         if ((k1 < 32) && (k1 > 16)) {
/* 150 */           Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
/*     */         } else {
/* 152 */           Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
/*     */         }
/*     */       }
/*     */       
/* 156 */       if (this.field_148303_c.func_175392_a(this, slotIndex)) {
/* 157 */         if ((k1 < 16) && (l1 < 16)) {
/* 158 */           Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
/*     */         } else {
/* 160 */           Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
/*     */         }
/*     */       }
/*     */       
/* 164 */       if (this.field_148303_c.func_175394_b(this, slotIndex)) {
/* 165 */         if ((k1 < 16) && (l1 > 16)) {
/* 166 */           Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
/*     */         } else {
/* 168 */           Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_178012_a(int p_178012_1_, int p_178012_2_, ResourceLocation p_178012_3_) {
/* 175 */     this.mc.getTextureManager().bindTexture(p_178012_3_);
/* 176 */     GlStateManager.enableBlend();
/* 177 */     Gui.drawModalRectWithCustomSizedTexture(p_178012_1_, p_178012_2_, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
/* 178 */     GlStateManager.disableBlend();
/*     */   }
/*     */   
/*     */   private boolean func_178013_b() {
/* 182 */     return true;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private void prepareServerIcon()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 87	net/minecraft/client/gui/ServerListEntryNormal:field_148301_e	Lnet/minecraft/client/multiplayer/ServerData;
/*     */     //   4: invokevirtual 280	net/minecraft/client/multiplayer/ServerData:getBase64EncodedIconData	()Ljava/lang/String;
/*     */     //   7: ifnonnull +25 -> 32
/*     */     //   10: aload_0
/*     */     //   11: getfield 95	net/minecraft/client/gui/ServerListEntryNormal:mc	Lnet/minecraft/client/Minecraft;
/*     */     //   14: invokevirtual 121	net/minecraft/client/Minecraft:getTextureManager	()Lnet/minecraft/client/renderer/texture/TextureManager;
/*     */     //   17: aload_0
/*     */     //   18: getfield 117	net/minecraft/client/gui/ServerListEntryNormal:field_148306_i	Lnet/minecraft/util/ResourceLocation;
/*     */     //   21: invokevirtual 374	net/minecraft/client/renderer/texture/TextureManager:deleteTexture	(Lnet/minecraft/util/ResourceLocation;)V
/*     */     //   24: aload_0
/*     */     //   25: aconst_null
/*     */     //   26: putfield 131	net/minecraft/client/gui/ServerListEntryNormal:field_148305_h	Lnet/minecraft/client/renderer/texture/DynamicTexture;
/*     */     //   29: goto +268 -> 297
/*     */     //   32: aload_0
/*     */     //   33: getfield 87	net/minecraft/client/gui/ServerListEntryNormal:field_148301_e	Lnet/minecraft/client/multiplayer/ServerData;
/*     */     //   36: invokevirtual 280	net/minecraft/client/multiplayer/ServerData:getBase64EncodedIconData	()Ljava/lang/String;
/*     */     //   39: getstatic 380	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
/*     */     //   42: invokestatic 386	io/netty/buffer/Unpooled:copiedBuffer	(Ljava/lang/CharSequence;Ljava/nio/charset/Charset;)Lio/netty/buffer/ByteBuf;
/*     */     //   45: astore_1
/*     */     //   46: aload_1
/*     */     //   47: invokestatic 392	io/netty/handler/codec/base64/Base64:decode	(Lio/netty/buffer/ByteBuf;)Lio/netty/buffer/ByteBuf;
/*     */     //   50: astore_2
/*     */     //   51: new 394	io/netty/buffer/ByteBufInputStream
/*     */     //   54: dup
/*     */     //   55: aload_2
/*     */     //   56: invokespecial 397	io/netty/buffer/ByteBufInputStream:<init>	(Lio/netty/buffer/ByteBuf;)V
/*     */     //   59: invokestatic 403	net/minecraft/client/renderer/texture/TextureUtil:readBufferedImage	(Ljava/io/InputStream;)Ljava/awt/image/BufferedImage;
/*     */     //   62: astore_3
/*     */     //   63: aload_3
/*     */     //   64: invokevirtual 408	java/awt/image/BufferedImage:getWidth	()I
/*     */     //   67: bipush 64
/*     */     //   69: if_icmpne +7 -> 76
/*     */     //   72: iconst_1
/*     */     //   73: goto +4 -> 77
/*     */     //   76: iconst_0
/*     */     //   77: ldc_w 412
/*     */     //   80: iconst_0
/*     */     //   81: anewarray 4	java/lang/Object
/*     */     //   84: invokestatic 418	org/apache/commons/lang3/Validate:validState	(ZLjava/lang/String;[Ljava/lang/Object;)V
/*     */     //   87: aload_3
/*     */     //   88: invokevirtual 421	java/awt/image/BufferedImage:getHeight	()I
/*     */     //   91: bipush 64
/*     */     //   93: if_icmpne +7 -> 100
/*     */     //   96: iconst_1
/*     */     //   97: goto +4 -> 101
/*     */     //   100: iconst_0
/*     */     //   101: ldc_w 423
/*     */     //   104: iconst_0
/*     */     //   105: anewarray 4	java/lang/Object
/*     */     //   108: invokestatic 418	org/apache/commons/lang3/Validate:validState	(ZLjava/lang/String;[Ljava/lang/Object;)V
/*     */     //   111: aload_1
/*     */     //   112: invokevirtual 426	io/netty/buffer/ByteBuf:release	()Z
/*     */     //   115: pop
/*     */     //   116: aload_2
/*     */     //   117: invokevirtual 426	io/netty/buffer/ByteBuf:release	()Z
/*     */     //   120: pop
/*     */     //   121: goto +97 -> 218
/*     */     //   124: astore 4
/*     */     //   126: getstatic 42	net/minecraft/client/gui/ServerListEntryNormal:logger	Lorg/apache/logging/log4j/Logger;
/*     */     //   129: new 97	java/lang/StringBuilder
/*     */     //   132: dup
/*     */     //   133: ldc_w 428
/*     */     //   136: invokespecial 100	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   139: aload_0
/*     */     //   140: getfield 87	net/minecraft/client/gui/ServerListEntryNormal:field_148301_e	Lnet/minecraft/client/multiplayer/ServerData;
/*     */     //   143: getfield 174	net/minecraft/client/multiplayer/ServerData:serverName	Ljava/lang/String;
/*     */     //   146: invokevirtual 109	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   149: ldc_w 430
/*     */     //   152: invokevirtual 109	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   155: aload_0
/*     */     //   156: getfield 87	net/minecraft/client/gui/ServerListEntryNormal:field_148301_e	Lnet/minecraft/client/multiplayer/ServerData;
/*     */     //   159: getfield 105	net/minecraft/client/multiplayer/ServerData:serverIP	Ljava/lang/String;
/*     */     //   162: invokevirtual 109	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   165: ldc_w 432
/*     */     //   168: invokevirtual 109	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   171: invokevirtual 115	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   174: aload 4
/*     */     //   176: invokeinterface 438 3 0
/*     */     //   181: aload_0
/*     */     //   182: getfield 87	net/minecraft/client/gui/ServerListEntryNormal:field_148301_e	Lnet/minecraft/client/multiplayer/ServerData;
/*     */     //   185: aconst_null
/*     */     //   186: invokevirtual 441	net/minecraft/client/multiplayer/ServerData:setBase64EncodedIconData	(Ljava/lang/String;)V
/*     */     //   189: aload_1
/*     */     //   190: invokevirtual 426	io/netty/buffer/ByteBuf:release	()Z
/*     */     //   193: pop
/*     */     //   194: aload_2
/*     */     //   195: invokevirtual 426	io/netty/buffer/ByteBuf:release	()Z
/*     */     //   198: pop
/*     */     //   199: goto +18 -> 217
/*     */     //   202: astore 5
/*     */     //   204: aload_1
/*     */     //   205: invokevirtual 426	io/netty/buffer/ByteBuf:release	()Z
/*     */     //   208: pop
/*     */     //   209: aload_2
/*     */     //   210: invokevirtual 426	io/netty/buffer/ByteBuf:release	()Z
/*     */     //   213: pop
/*     */     //   214: aload 5
/*     */     //   216: athrow
/*     */     //   217: return
/*     */     //   218: aload_0
/*     */     //   219: getfield 131	net/minecraft/client/gui/ServerListEntryNormal:field_148305_h	Lnet/minecraft/client/renderer/texture/DynamicTexture;
/*     */     //   222: ifnonnull +41 -> 263
/*     */     //   225: aload_0
/*     */     //   226: new 129	net/minecraft/client/renderer/texture/DynamicTexture
/*     */     //   229: dup
/*     */     //   230: aload_3
/*     */     //   231: invokevirtual 408	java/awt/image/BufferedImage:getWidth	()I
/*     */     //   234: aload_3
/*     */     //   235: invokevirtual 421	java/awt/image/BufferedImage:getHeight	()I
/*     */     //   238: invokespecial 444	net/minecraft/client/renderer/texture/DynamicTexture:<init>	(II)V
/*     */     //   241: putfield 131	net/minecraft/client/gui/ServerListEntryNormal:field_148305_h	Lnet/minecraft/client/renderer/texture/DynamicTexture;
/*     */     //   244: aload_0
/*     */     //   245: getfield 95	net/minecraft/client/gui/ServerListEntryNormal:mc	Lnet/minecraft/client/Minecraft;
/*     */     //   248: invokevirtual 121	net/minecraft/client/Minecraft:getTextureManager	()Lnet/minecraft/client/renderer/texture/TextureManager;
/*     */     //   251: aload_0
/*     */     //   252: getfield 117	net/minecraft/client/gui/ServerListEntryNormal:field_148306_i	Lnet/minecraft/util/ResourceLocation;
/*     */     //   255: aload_0
/*     */     //   256: getfield 131	net/minecraft/client/gui/ServerListEntryNormal:field_148305_h	Lnet/minecraft/client/renderer/texture/DynamicTexture;
/*     */     //   259: invokevirtual 448	net/minecraft/client/renderer/texture/TextureManager:loadTexture	(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/client/renderer/texture/ITextureObject;)Z
/*     */     //   262: pop
/*     */     //   263: aload_3
/*     */     //   264: iconst_0
/*     */     //   265: iconst_0
/*     */     //   266: aload_3
/*     */     //   267: invokevirtual 408	java/awt/image/BufferedImage:getWidth	()I
/*     */     //   270: aload_3
/*     */     //   271: invokevirtual 421	java/awt/image/BufferedImage:getHeight	()I
/*     */     //   274: aload_0
/*     */     //   275: getfield 131	net/minecraft/client/gui/ServerListEntryNormal:field_148305_h	Lnet/minecraft/client/renderer/texture/DynamicTexture;
/*     */     //   278: invokevirtual 452	net/minecraft/client/renderer/texture/DynamicTexture:getTextureData	()[I
/*     */     //   281: iconst_0
/*     */     //   282: aload_3
/*     */     //   283: invokevirtual 408	java/awt/image/BufferedImage:getWidth	()I
/*     */     //   286: invokevirtual 456	java/awt/image/BufferedImage:getRGB	(IIII[III)[I
/*     */     //   289: pop
/*     */     //   290: aload_0
/*     */     //   291: getfield 131	net/minecraft/client/gui/ServerListEntryNormal:field_148305_h	Lnet/minecraft/client/renderer/texture/DynamicTexture;
/*     */     //   294: invokevirtual 459	net/minecraft/client/renderer/texture/DynamicTexture:updateDynamicTexture	()V
/*     */     //   297: return
/*     */     // Line number table:
/*     */     //   Java source line #186	-> byte code offset #0
/*     */     //   Java source line #187	-> byte code offset #10
/*     */     //   Java source line #188	-> byte code offset #24
/*     */     //   Java source line #189	-> byte code offset #29
/*     */     //   Java source line #190	-> byte code offset #32
/*     */     //   Java source line #191	-> byte code offset #46
/*     */     //   Java source line #196	-> byte code offset #51
/*     */     //   Java source line #197	-> byte code offset #63
/*     */     //   Java source line #198	-> byte code offset #87
/*     */     //   Java source line #204	-> byte code offset #111
/*     */     //   Java source line #205	-> byte code offset #116
/*     */     //   Java source line #199	-> byte code offset #121
/*     */     //   Java source line #200	-> byte code offset #124
/*     */     //   Java source line #201	-> byte code offset #126
/*     */     //   Java source line #202	-> byte code offset #181
/*     */     //   Java source line #204	-> byte code offset #189
/*     */     //   Java source line #205	-> byte code offset #194
/*     */     //   Java source line #203	-> byte code offset #202
/*     */     //   Java source line #204	-> byte code offset #204
/*     */     //   Java source line #205	-> byte code offset #209
/*     */     //   Java source line #206	-> byte code offset #214
/*     */     //   Java source line #208	-> byte code offset #217
/*     */     //   Java source line #211	-> byte code offset #218
/*     */     //   Java source line #212	-> byte code offset #225
/*     */     //   Java source line #213	-> byte code offset #244
/*     */     //   Java source line #216	-> byte code offset #263
/*     */     //   Java source line #217	-> byte code offset #290
/*     */     //   Java source line #219	-> byte code offset #297
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	298	0	this	ServerListEntryNormal
/*     */     //   45	160	1	bytebuf	io.netty.buffer.ByteBuf
/*     */     //   50	160	2	bytebuf1	io.netty.buffer.ByteBuf
/*     */     //   62	26	3	bufferedimage	java.awt.image.BufferedImage
/*     */     //   218	65	3	bufferedimage	java.awt.image.BufferedImage
/*     */     //   124	51	4	throwable	Throwable
/*     */     //   202	13	5	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   51	111	124	java/lang/Throwable
/*     */     //   51	111	202	finally
/*     */     //   124	189	202	finally
/*     */   }
/*     */   
/*     */   public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
/*     */   {
/* 222 */     if (p_148278_5_ <= 32) {
/* 223 */       if ((p_148278_5_ < 32) && (p_148278_5_ > 16) && (func_178013_b())) {
/* 224 */         this.field_148303_c.selectServer(slotIndex);
/* 225 */         this.field_148303_c.connectToSelected();
/* 226 */         return true;
/*     */       }
/*     */       
/* 229 */       if ((p_148278_5_ < 16) && (p_148278_6_ < 16) && (this.field_148303_c.func_175392_a(this, slotIndex))) {
/* 230 */         this.field_148303_c.func_175391_a(this, slotIndex, GuiScreen.isShiftKeyDown());
/* 231 */         return true;
/*     */       }
/*     */       
/* 234 */       if ((p_148278_5_ < 16) && (p_148278_6_ > 16) && (this.field_148303_c.func_175394_b(this, slotIndex))) {
/* 235 */         this.field_148303_c.func_175393_b(this, slotIndex, GuiScreen.isShiftKeyDown());
/* 236 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 240 */     this.field_148303_c.selectServer(slotIndex);
/* 241 */     if (Minecraft.getSystemTime() - this.field_148298_f < 250L) {
/* 242 */       this.field_148303_c.connectToSelected();
/*     */     }
/*     */     
/* 245 */     this.field_148298_f = Minecraft.getSystemTime();
/* 246 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {}
/*     */   
/*     */   public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {}
/*     */   
/*     */   public ServerData getServerData()
/*     */   {
/* 256 */     return this.field_148301_e;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\ServerListEntryNormal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */