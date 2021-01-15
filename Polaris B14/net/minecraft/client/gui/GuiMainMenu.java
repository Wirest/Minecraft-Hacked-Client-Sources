/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.client.shader.Framebuffer;
/*     */ import net.minecraft.realms.RealmsBridge;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Session;
/*     */ import net.minecraft.world.storage.ISaveFormat;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.util.glu.Project;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.command.commands.NameCommand;
/*     */ import rip.jutting.polaris.ui.altmanager.GuiAltManager;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ import rip.jutting.polaris.ui.fonth.CFontRenderer;
/*     */ import rip.jutting.polaris.ui.fonth.FontLoaders;
/*     */ import rip.jutting.polaris.ui.login.AltLoginThread;
/*     */ import rip.jutting.polaris.ui.pgen.Particle;
/*     */ import rip.jutting.polaris.ui.pgen.ParticleGen;
/*     */ import rip.jutting.polaris.ui.protection.GuiAuth;
/*     */ 
/*     */ public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback
/*     */ {
/*  52 */   private static final AtomicInteger field_175373_f = new AtomicInteger(0);
/*  53 */   private static final Logger logger = LogManager.getLogger();
/*  54 */   private static final Random RANDOM = new Random();
/*     */   
/*  56 */   public static boolean oldMenu = false;
/*     */   
/*     */ 
/*     */   private ParticleGen particles;
/*     */   
/*     */ 
/*     */   private Random random;
/*     */   
/*     */ 
/*     */   private AltLoginThread thread;
/*     */   
/*     */ 
/*     */   private float updateCounter;
/*     */   
/*     */ 
/*     */   private String splashText;
/*     */   
/*     */ 
/*     */   private GuiButton buttonResetDemo;
/*     */   
/*     */ 
/*     */   private int panoramaTimer;
/*     */   
/*     */ 
/*     */   private DynamicTexture viewportTexture;
/*     */   
/*     */ 
/*     */   private boolean field_175375_v;
/*     */   
/*     */   private final Object threadLock;
/*     */   
/*     */   private String openGLWarning1;
/*     */   
/*     */   private String openGLWarning2;
/*     */   
/*     */   private String openGLWarningLink;
/*     */   
/*  93 */   private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
/*  94 */   private static final ResourceLocation minecraftTitleTextures = new ResourceLocation(
/*  95 */     "textures/gui/title/minecraft.png");
/*     */   
/*     */ 
/*  98 */   private static final ResourceLocation[] titlePanoramaPaths = {
/*  99 */     new ResourceLocation("textures/gui/title/background/panorama_0.png"), 
/* 100 */     new ResourceLocation("textures/gui/title/background/panorama_1.png"), 
/* 101 */     new ResourceLocation("textures/gui/title/background/panorama_2.png"), 
/* 102 */     new ResourceLocation("textures/gui/title/background/panorama_3.png"), 
/* 103 */     new ResourceLocation("textures/gui/title/background/panorama_4.png"), 
/* 104 */     new ResourceLocation("textures/gui/title/background/panorama_5.png") };
/* 105 */   public static final String field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + 
/* 106 */     EnumChatFormatting.RESET + " for more information.";
/*     */   private int field_92024_r;
/*     */   private int field_92023_s;
/*     */   private int field_92022_t;
/*     */   private int field_92021_u;
/*     */   private int field_92020_v;
/*     */   private int field_92019_w;
/*     */   private ResourceLocation backgroundTexture;
/*     */   private GuiButton realmsButton;
/*     */   
/*     */   /* Error */
/*     */   public GuiMainMenu()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokespecial 137	net/minecraft/client/gui/GuiScreen:<init>	()V
/*     */     //   4: aload_0
/*     */     //   5: new 70	java/util/Random
/*     */     //   8: dup
/*     */     //   9: invokespecial 72	java/util/Random:<init>	()V
/*     */     //   12: putfield 139	net/minecraft/client/gui/GuiMainMenu:random	Ljava/util/Random;
/*     */     //   15: aload_0
/*     */     //   16: iconst_1
/*     */     //   17: putfield 141	net/minecraft/client/gui/GuiMainMenu:field_175375_v	Z
/*     */     //   20: aload_0
/*     */     //   21: new 143	java/lang/Object
/*     */     //   24: dup
/*     */     //   25: invokespecial 144	java/lang/Object:<init>	()V
/*     */     //   28: putfield 146	net/minecraft/client/gui/GuiMainMenu:threadLock	Ljava/lang/Object;
/*     */     //   31: aload_0
/*     */     //   32: getstatic 134	net/minecraft/client/gui/GuiMainMenu:field_96138_a	Ljava/lang/String;
/*     */     //   35: putfield 148	net/minecraft/client/gui/GuiMainMenu:openGLWarning2	Ljava/lang/String;
/*     */     //   38: aload_0
/*     */     //   39: ldc -106
/*     */     //   41: putfield 152	net/minecraft/client/gui/GuiMainMenu:splashText	Ljava/lang/String;
/*     */     //   44: aconst_null
/*     */     //   45: astore_1
/*     */     //   46: invokestatic 158	com/google/common/collect/Lists:newArrayList	()Ljava/util/ArrayList;
/*     */     //   49: astore_2
/*     */     //   50: new 160	java/io/BufferedReader
/*     */     //   53: dup
/*     */     //   54: new 162	java/io/InputStreamReader
/*     */     //   57: dup
/*     */     //   58: invokestatic 168	net/minecraft/client/Minecraft:getMinecraft	()Lnet/minecraft/client/Minecraft;
/*     */     //   61: invokevirtual 172	net/minecraft/client/Minecraft:getResourceManager	()Lnet/minecraft/client/resources/IResourceManager;
/*     */     //   64: getstatic 85	net/minecraft/client/gui/GuiMainMenu:splashTexts	Lnet/minecraft/util/ResourceLocation;
/*     */     //   67: invokeinterface 178 2 0
/*     */     //   72: invokeinterface 184 1 0
/*     */     //   77: getstatic 190	org/apache/commons/io/Charsets:UTF_8	Ljava/nio/charset/Charset;
/*     */     //   80: invokespecial 193	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/nio/charset/Charset;)V
/*     */     //   83: invokespecial 196	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
/*     */     //   86: astore_1
/*     */     //   87: goto +23 -> 110
/*     */     //   90: aload_3
/*     */     //   91: invokevirtual 203	java/lang/String:trim	()Ljava/lang/String;
/*     */     //   94: astore_3
/*     */     //   95: aload_3
/*     */     //   96: invokevirtual 207	java/lang/String:isEmpty	()Z
/*     */     //   99: ifne +11 -> 110
/*     */     //   102: aload_2
/*     */     //   103: aload_3
/*     */     //   104: invokeinterface 211 2 0
/*     */     //   109: pop
/*     */     //   110: aload_1
/*     */     //   111: invokevirtual 214	java/io/BufferedReader:readLine	()Ljava/lang/String;
/*     */     //   114: dup
/*     */     //   115: astore_3
/*     */     //   116: ifnonnull -26 -> 90
/*     */     //   119: aload_2
/*     */     //   120: invokeinterface 215 1 0
/*     */     //   125: ifne +78 -> 203
/*     */     //   128: aload_0
/*     */     //   129: aload_2
/*     */     //   130: getstatic 74	net/minecraft/client/gui/GuiMainMenu:RANDOM	Ljava/util/Random;
/*     */     //   133: aload_2
/*     */     //   134: invokeinterface 219 1 0
/*     */     //   139: invokevirtual 223	java/util/Random:nextInt	(I)I
/*     */     //   142: invokeinterface 227 2 0
/*     */     //   147: checkcast 200	java/lang/String
/*     */     //   150: putfield 152	net/minecraft/client/gui/GuiMainMenu:splashText	Ljava/lang/String;
/*     */     //   153: aload_0
/*     */     //   154: getfield 152	net/minecraft/client/gui/GuiMainMenu:splashText	Ljava/lang/String;
/*     */     //   157: invokevirtual 230	java/lang/String:hashCode	()I
/*     */     //   160: ldc -25
/*     */     //   162: if_icmpeq -34 -> 128
/*     */     //   165: goto +38 -> 203
/*     */     //   168: astore_2
/*     */     //   169: aload_1
/*     */     //   170: ifnull +46 -> 216
/*     */     //   173: aload_1
/*     */     //   174: invokevirtual 234	java/io/BufferedReader:close	()V
/*     */     //   177: goto +39 -> 216
/*     */     //   180: astore 5
/*     */     //   182: goto +34 -> 216
/*     */     //   185: astore 4
/*     */     //   187: aload_1
/*     */     //   188: ifnull +12 -> 200
/*     */     //   191: aload_1
/*     */     //   192: invokevirtual 234	java/io/BufferedReader:close	()V
/*     */     //   195: goto +5 -> 200
/*     */     //   198: astore 5
/*     */     //   200: aload 4
/*     */     //   202: athrow
/*     */     //   203: aload_1
/*     */     //   204: ifnull +12 -> 216
/*     */     //   207: aload_1
/*     */     //   208: invokevirtual 234	java/io/BufferedReader:close	()V
/*     */     //   211: goto +5 -> 216
/*     */     //   214: astore 5
/*     */     //   216: aload_0
/*     */     //   217: getstatic 74	net/minecraft/client/gui/GuiMainMenu:RANDOM	Ljava/util/Random;
/*     */     //   220: invokevirtual 240	java/util/Random:nextFloat	()F
/*     */     //   223: putfield 242	net/minecraft/client/gui/GuiMainMenu:updateCounter	F
/*     */     //   226: aload_0
/*     */     //   227: ldc -12
/*     */     //   229: putfield 246	net/minecraft/client/gui/GuiMainMenu:openGLWarning1	Ljava/lang/String;
/*     */     //   232: invokestatic 252	org/lwjgl/opengl/GLContext:getCapabilities	()Lorg/lwjgl/opengl/ContextCapabilities;
/*     */     //   235: getfield 257	org/lwjgl/opengl/ContextCapabilities:OpenGL20	Z
/*     */     //   238: ifne +44 -> 282
/*     */     //   241: invokestatic 262	net/minecraft/client/renderer/OpenGlHelper:areShadersSupported	()Z
/*     */     //   244: ifne +38 -> 282
/*     */     //   247: aload_0
/*     */     //   248: ldc_w 264
/*     */     //   251: iconst_0
/*     */     //   252: anewarray 143	java/lang/Object
/*     */     //   255: invokestatic 270	net/minecraft/client/resources/I18n:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   258: putfield 246	net/minecraft/client/gui/GuiMainMenu:openGLWarning1	Ljava/lang/String;
/*     */     //   261: aload_0
/*     */     //   262: ldc_w 272
/*     */     //   265: iconst_0
/*     */     //   266: anewarray 143	java/lang/Object
/*     */     //   269: invokestatic 270	net/minecraft/client/resources/I18n:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   272: putfield 148	net/minecraft/client/gui/GuiMainMenu:openGLWarning2	Ljava/lang/String;
/*     */     //   275: aload_0
/*     */     //   276: ldc_w 274
/*     */     //   279: putfield 276	net/minecraft/client/gui/GuiMainMenu:openGLWarningLink	Ljava/lang/String;
/*     */     //   282: return
/*     */     // Line number table:
/*     */     //   Java source line #118	-> byte code offset #0
/*     */     //   Java source line #59	-> byte code offset #4
/*     */     //   Java source line #77	-> byte code offset #15
/*     */     //   Java source line #83	-> byte code offset #20
/*     */     //   Java source line #119	-> byte code offset #31
/*     */     //   Java source line #120	-> byte code offset #38
/*     */     //   Java source line #121	-> byte code offset #44
/*     */     //   Java source line #124	-> byte code offset #46
/*     */     //   Java source line #125	-> byte code offset #50
/*     */     //   Java source line #126	-> byte code offset #58
/*     */     //   Java source line #127	-> byte code offset #77
/*     */     //   Java source line #125	-> byte code offset #83
/*     */     //   Java source line #130	-> byte code offset #87
/*     */     //   Java source line #131	-> byte code offset #90
/*     */     //   Java source line #133	-> byte code offset #95
/*     */     //   Java source line #134	-> byte code offset #102
/*     */     //   Java source line #130	-> byte code offset #110
/*     */     //   Java source line #137	-> byte code offset #119
/*     */     //   Java source line #139	-> byte code offset #128
/*     */     //   Java source line #141	-> byte code offset #153
/*     */     //   Java source line #146	-> byte code offset #165
/*     */     //   Java source line #149	-> byte code offset #169
/*     */     //   Java source line #151	-> byte code offset #173
/*     */     //   Java source line #152	-> byte code offset #177
/*     */     //   Java source line #148	-> byte code offset #185
/*     */     //   Java source line #149	-> byte code offset #187
/*     */     //   Java source line #151	-> byte code offset #191
/*     */     //   Java source line #152	-> byte code offset #195
/*     */     //   Java source line #156	-> byte code offset #200
/*     */     //   Java source line #149	-> byte code offset #203
/*     */     //   Java source line #151	-> byte code offset #207
/*     */     //   Java source line #152	-> byte code offset #211
/*     */     //   Java source line #158	-> byte code offset #216
/*     */     //   Java source line #159	-> byte code offset #226
/*     */     //   Java source line #161	-> byte code offset #232
/*     */     //   Java source line #162	-> byte code offset #247
/*     */     //   Java source line #163	-> byte code offset #261
/*     */     //   Java source line #164	-> byte code offset #275
/*     */     //   Java source line #166	-> byte code offset #282
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	283	0	this	GuiMainMenu
/*     */     //   45	163	1	bufferedreader	BufferedReader
/*     */     //   49	85	2	list	List<String>
/*     */     //   168	1	2	localIOException	IOException
/*     */     //   90	14	3	s	String
/*     */     //   115	2	3	s	String
/*     */     //   185	16	4	localObject	Object
/*     */     //   180	1	5	localIOException1	IOException
/*     */     //   198	1	5	localIOException2	IOException
/*     */     //   214	1	5	localIOException3	IOException
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   46	165	168	java/io/IOException
/*     */     //   173	177	180	java/io/IOException
/*     */     //   46	169	185	finally
/*     */     //   191	195	198	java/io/IOException
/*     */     //   207	211	214	java/io/IOException
/*     */   }
/*     */   
/*     */   public void updateScreen()
/*     */   {
/* 172 */     this.panoramaTimer += 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean doesGuiPauseGame()
/*     */   {
/* 180 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void keyTyped(char typedChar, int keyCode)
/*     */     throws IOException
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/* 197 */     this.viewportTexture = new DynamicTexture(256, 256);
/* 198 */     this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", 
/* 199 */       this.viewportTexture);
/* 200 */     Calendar calendar = Calendar.getInstance();
/* 201 */     calendar.setTime(new Date());
/*     */     
/* 203 */     if ((calendar.get(2) + 1 == 12) && (calendar.get(5) == 24)) {
/* 204 */       this.splashText = "Merry X-mas!";
/* 205 */     } else if ((calendar.get(2) + 1 == 1) && (calendar.get(5) == 1)) {
/* 206 */       this.splashText = "Happy new year!";
/* 207 */     } else if ((calendar.get(2) + 1 == 10) && (calendar.get(5) == 31)) {
/* 208 */       this.splashText = "OOoooOOOoooo! Spooky!";
/*     */     }
/*     */     
/* 211 */     int i = 24;
/* 212 */     int j = height / 4 + 48;
/*     */     
/* 214 */     if (this.mc.isDemo()) {
/* 215 */       addDemoButtons(j, 24);
/*     */     } else {
/* 217 */       addSingleplayerMultiplayerButtons(j, 24);
/*     */     }
/* 219 */     if (oldMenu) {
/* 220 */       this.buttonList.add(new GuiButton(0, width / 2 - 100, j + 72 + 12, 98, 20, 
/* 221 */         I18n.format("menu.options", new Object[0])));
/* 222 */       this.buttonList.add(
/* 223 */         new GuiButton(4, width / 2 + 2, j + 72 + 12, 98, 20, I18n.format("menu.quit", new Object[0])));
/*     */     } else {
/* 225 */       this.buttonList.add(new GuiButton(0, width / 2 - 68, j + 52 + 12, 50, 50, 
/* 226 */         I18n.format("Options", new Object[0])));
/* 227 */       this.buttonList.add(new GuiButton(4, width / 2 + 20, j + 52 + 12, 50, 50, 
/* 228 */         I18n.format("Quit", new Object[0])));
/*     */     }
/*     */     
/* 231 */     synchronized (this.threadLock) {
/* 232 */       this.field_92023_s = this.fontRendererObj.getStringWidth(this.openGLWarning1);
/* 233 */       this.field_92024_r = this.fontRendererObj.getStringWidth(this.openGLWarning2);
/* 234 */       int k = Math.max(this.field_92023_s, this.field_92024_r);
/* 235 */       this.field_92022_t = ((width - k) / 2);
/* 236 */       this.field_92021_u = (((GuiButton)this.buttonList.get(0)).yPosition - 24);
/* 237 */       this.field_92020_v = (this.field_92022_t + k);
/* 238 */       this.field_92019_w = (this.field_92021_u + 24);
/*     */     }
/*     */     
/* 241 */     this.mc.func_181537_a(false);
/* 242 */     this.particles = new ParticleGen(100, width, height);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_)
/*     */   {
/* 250 */     if (oldMenu) {
/* 251 */       this.buttonList.add(new GuiButton(1, width / 2 - 100, p_73969_1_ - 15, 
/* 252 */         I18n.format("menu.singleplayer", new Object[0])));
/* 253 */       this.buttonList.add(new GuiButton(2, width / 2 - 100, p_73969_1_ + p_73969_2_ * 1 - 15, 
/* 254 */         I18n.format("menu.multiplayer", new Object[0])));
/* 255 */       this.buttonList.add(new GuiButton(500, width / 2 - 100, height / 4 + 48 + 48 - 15, "Login"));
/* 256 */       this.buttonList
/* 257 */         .add(new GuiButton(1337, width / 2 - 100, height / 4 + 73 + 48 - 15, "Quick Login"));
/*     */     } else {
/* 259 */       this.buttonList.add(
/* 260 */         new GuiButton(1, width / 2 - 100, p_73969_1_, 50, 50, I18n.format("S", new Object[0])));
/* 261 */       this.buttonList.add(
/* 262 */         new GuiButton(2, width / 2 - 25, p_73969_1_, 50, 50, I18n.format("M", new Object[0])));
/* 263 */       this.buttonList.add(new GuiButton(500, width / 2 + 50, p_73969_1_, 50, 50, 
/* 264 */         I18n.format("A", new Object[0])));
/*     */     }
/* 266 */     this.buttonList.add(new GuiButton(33, ScaledResolution.getScaledWidth() - 70, height - 26, 64, 20, 
/* 267 */       "Change Menu"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addDemoButtons(int p_73972_1_, int p_73972_2_)
/*     */   {
/* 275 */     this.buttonList.add(new GuiButton(11, width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo", new Object[0])));
/* 276 */     this.buttonList.add(this.buttonResetDemo = new GuiButton(12, width / 2 - 100, p_73972_1_ + p_73972_2_ * 1, 
/* 277 */       I18n.format("menu.resetdemo", new Object[0])));
/* 278 */     ISaveFormat isaveformat = this.mc.getSaveLoader();
/* 279 */     WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
/*     */     
/* 281 */     if (worldinfo == null) {
/* 282 */       this.buttonResetDemo.enabled = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void actionPerformed(GuiButton button)
/*     */     throws IOException
/*     */   {
/* 291 */     if (button.id == 0) {
/* 292 */       this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
/*     */     }
/*     */     
/* 295 */     if (button.id == 1337)
/*     */     {
/* 297 */       URLConnection openConnection = new URL("http://51.38.119.240/altz/meme.php").openConnection();
/* 298 */       openConnection.addRequestProperty("User-Agent", 
/* 299 */         "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
/*     */       
/*     */ 
/* 302 */       BufferedReader in = new BufferedReader(new InputStreamReader(openConnection.getInputStream()));
/*     */       String str;
/* 304 */       while ((str = in.readLine()) != null) {
/* 305 */         String str = in.readLine().toString();
/* 306 */         String[] user = str.split(":");
/* 307 */         String username = user[0];
/* 308 */         String password = user[1];
/* 309 */         this.thread = new AltLoginThread(username, password);
/* 310 */         this.thread.start();
/*     */       }
/* 312 */       in.close();
/*     */     }
/*     */     
/* 315 */     if (button.id == 33) {
/* 316 */       oldMenu = !oldMenu;
/* 317 */       this.mc.displayGuiScreen(new GuiMainMenu());
/*     */     }
/*     */     
/* 320 */     if (button.id == 500) {
/* 321 */       this.mc.displayGuiScreen(new GuiAltManager());
/*     */     }
/*     */     
/* 324 */     if (button.id == 5) {
/* 325 */       this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
/*     */     }
/*     */     
/* 328 */     if (button.id == 1) {
/* 329 */       this.mc.displayGuiScreen(new GuiSelectWorld(this));
/*     */     }
/*     */     
/* 332 */     if (button.id == 2) {
/* 333 */       this.mc.displayGuiScreen(new GuiMultiplayer(this));
/*     */     }
/*     */     
/* 336 */     if ((button.id == 14) && (this.realmsButton.visible)) {
/* 337 */       switchToRealms();
/*     */     }
/*     */     
/* 340 */     if (button.id == 4) {
/* 341 */       this.mc.shutdown();
/*     */     }
/*     */     
/* 344 */     if (button.id == 11) {
/* 345 */       this.mc.launchIntegratedServer("Demo_World", "Demo_World", net.minecraft.world.demo.DemoWorldServer.demoWorldSettings);
/*     */     }
/*     */     
/* 348 */     if (button.id == 12) {
/* 349 */       ISaveFormat isaveformat = this.mc.getSaveLoader();
/* 350 */       WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
/*     */       
/* 352 */       if (worldinfo != null) {
/* 353 */         GuiYesNo guiyesno = GuiSelectWorld.func_152129_a(this, worldinfo.getWorldName(), 12);
/* 354 */         this.mc.displayGuiScreen(guiyesno);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void switchToRealms() {
/* 360 */     RealmsBridge realmsbridge = new RealmsBridge();
/* 361 */     realmsbridge.switchToRealms(this);
/*     */   }
/*     */   
/*     */   public void confirmClicked(boolean result, int id) {
/* 365 */     if ((result) && (id == 12)) {
/* 366 */       ISaveFormat isaveformat = this.mc.getSaveLoader();
/* 367 */       isaveformat.flushCache();
/* 368 */       isaveformat.deleteWorldDirectory("Demo_World");
/* 369 */       this.mc.displayGuiScreen(this);
/* 370 */     } else if (id == 13) {
/* 371 */       if (result) {
/*     */         try {
/* 373 */           Class<?> oclass = Class.forName("java.awt.Desktop");
/* 374 */           Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
/* 375 */           oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, 
/* 376 */             new Object[] { new URI(this.openGLWarningLink) });
/*     */         } catch (Throwable throwable) {
/* 378 */           logger.error("Couldn't open link", throwable);
/*     */         }
/*     */       }
/*     */       
/* 382 */       this.mc.displayGuiScreen(this);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_)
/*     */   {
/* 390 */     Tessellator tessellator = Tessellator.getInstance();
/* 391 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 392 */     GlStateManager.matrixMode(5889);
/* 393 */     GlStateManager.pushMatrix();
/* 394 */     GlStateManager.loadIdentity();
/* 395 */     Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
/* 396 */     GlStateManager.matrixMode(5888);
/* 397 */     GlStateManager.pushMatrix();
/* 398 */     GlStateManager.loadIdentity();
/* 399 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 400 */     GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
/* 401 */     GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
/* 402 */     GlStateManager.enableBlend();
/* 403 */     GlStateManager.disableAlpha();
/* 404 */     GlStateManager.disableCull();
/* 405 */     GlStateManager.depthMask(false);
/* 406 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 407 */     int i = 8;
/*     */     
/* 409 */     for (int j = 0; j < i * i; j++) {
/* 410 */       GlStateManager.pushMatrix();
/* 411 */       float f = (j % i / i - 0.5F) / 64.0F;
/* 412 */       float f1 = (j / i / i - 0.5F) / 64.0F;
/* 413 */       float f2 = 0.0F;
/* 414 */       GlStateManager.translate(f, f1, f2);
/* 415 */       GlStateManager.rotate(MathHelper.sin((this.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 
/* 416 */         1.0F, 0.0F, 0.0F);
/* 417 */       GlStateManager.rotate(-(this.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);
/*     */       
/* 419 */       for (int k = 0; k < 6; k++) {
/* 420 */         GlStateManager.pushMatrix();
/*     */         
/* 422 */         if (k == 1) {
/* 423 */           GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
/*     */         }
/*     */         
/* 426 */         if (k == 2) {
/* 427 */           GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
/*     */         }
/*     */         
/* 430 */         if (k == 3) {
/* 431 */           GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
/*     */         }
/*     */         
/* 434 */         if (k == 4) {
/* 435 */           GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
/*     */         }
/*     */         
/* 438 */         if (k == 5) {
/* 439 */           GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
/*     */         }
/*     */         
/* 442 */         this.mc.getTextureManager().bindTexture(titlePanoramaPaths[k]);
/* 443 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 444 */         int l = 255 / (j + 1);
/* 445 */         float f3 = 0.0F;
/* 446 */         worldrenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, l).endVertex();
/* 447 */         worldrenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, l).endVertex();
/* 448 */         worldrenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, l).endVertex();
/* 449 */         worldrenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, l).endVertex();
/* 450 */         tessellator.draw();
/* 451 */         GlStateManager.popMatrix();
/*     */       }
/*     */       
/* 454 */       GlStateManager.popMatrix();
/* 455 */       GlStateManager.colorMask(true, true, true, false);
/*     */     }
/*     */     
/* 458 */     worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
/* 459 */     GlStateManager.colorMask(true, true, true, true);
/* 460 */     GlStateManager.matrixMode(5889);
/* 461 */     GlStateManager.popMatrix();
/* 462 */     GlStateManager.matrixMode(5888);
/* 463 */     GlStateManager.popMatrix();
/* 464 */     GlStateManager.depthMask(true);
/* 465 */     GlStateManager.enableCull();
/* 466 */     GlStateManager.enableDepth();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void rotateAndBlurSkybox(float p_73968_1_)
/*     */   {
/* 473 */     this.mc.getTextureManager().bindTexture(this.backgroundTexture);
/* 474 */     GL11.glTexParameteri(3553, 10241, 9729);
/* 475 */     GL11.glTexParameteri(3553, 10240, 9729);
/* 476 */     GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
/* 477 */     GlStateManager.enableBlend();
/* 478 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 479 */     GlStateManager.colorMask(true, true, true, false);
/* 480 */     Tessellator tessellator = Tessellator.getInstance();
/* 481 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 482 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 483 */     GlStateManager.disableAlpha();
/* 484 */     int i = 3;
/*     */     
/* 486 */     for (int j = 0; j < i; j++) {
/* 487 */       float f = 1.0F / (j + 1);
/* 488 */       int k = width;
/* 489 */       int l = height;
/* 490 */       float f1 = (j - i / 2) / 256.0F;
/* 491 */       worldrenderer.pos(k, l, this.zLevel).tex(0.0F + f1, 1.0D)
/* 492 */         .color(1.0F, 1.0F, 1.0F, f).endVertex();
/* 493 */       worldrenderer.pos(k, 0.0D, this.zLevel).tex(1.0F + f1, 1.0D)
/* 494 */         .color(1.0F, 1.0F, 1.0F, f).endVertex();
/* 495 */       worldrenderer.pos(0.0D, 0.0D, this.zLevel).tex(1.0F + f1, 0.0D)
/* 496 */         .color(1.0F, 1.0F, 1.0F, f).endVertex();
/* 497 */       worldrenderer.pos(0.0D, l, this.zLevel).tex(0.0F + f1, 0.0D)
/* 498 */         .color(1.0F, 1.0F, 1.0F, f).endVertex();
/*     */     }
/*     */     
/* 501 */     tessellator.draw();
/* 502 */     GlStateManager.enableAlpha();
/* 503 */     GlStateManager.colorMask(true, true, true, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_)
/*     */   {
/* 510 */     this.mc.getFramebuffer().unbindFramebuffer();
/* 511 */     GlStateManager.viewport(0, 0, 256, 256);
/* 512 */     drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
/* 513 */     rotateAndBlurSkybox(p_73971_3_);
/* 514 */     rotateAndBlurSkybox(p_73971_3_);
/* 515 */     rotateAndBlurSkybox(p_73971_3_);
/* 516 */     rotateAndBlurSkybox(p_73971_3_);
/* 517 */     rotateAndBlurSkybox(p_73971_3_);
/* 518 */     rotateAndBlurSkybox(p_73971_3_);
/* 519 */     rotateAndBlurSkybox(p_73971_3_);
/* 520 */     this.mc.getFramebuffer().bindFramebuffer(true);
/* 521 */     GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
/* 522 */     float f = width > height ? 120.0F / width : 120.0F / height;
/* 523 */     float f1 = height * f / 256.0F;
/* 524 */     float f2 = width * f / 256.0F;
/* 525 */     int i = width;
/* 526 */     int j = height;
/* 527 */     Tessellator tessellator = Tessellator.getInstance();
/* 528 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 529 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 530 */     worldrenderer.pos(0.0D, j, this.zLevel).tex(0.5F - f1, 0.5F + f2)
/* 531 */       .color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
/* 532 */     worldrenderer.pos(i, j, this.zLevel).tex(0.5F - f1, 0.5F - f2)
/* 533 */       .color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
/* 534 */     worldrenderer.pos(i, 0.0D, this.zLevel).tex(0.5F + f1, 0.5F - f2)
/* 535 */       .color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
/* 536 */     worldrenderer.pos(0.0D, 0.0D, this.zLevel).tex(0.5F + f1, 0.5F + f2)
/* 537 */       .color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
/* 538 */     tessellator.draw();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 546 */     if (!Polaris.valid) {
/* 547 */       this.mc.displayGuiScreen(new GuiAuth());
/*     */     }
/* 549 */     ScaledResolution scaledRes = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
/* 550 */     this.mc.getTextureManager().bindTexture(new ResourceLocation("polaris/background.jpg"));
/* 551 */     Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), 
/* 552 */       ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), ScaledResolution.getScaledWidth(), 
/* 553 */       ScaledResolution.getScaledHeight());
/* 554 */     CFontRenderer fontx = FontLoaders.mainmenu;
/* 555 */     float scale = 1.0F;
/* 556 */     GL11.glScalef(1.0F, 1.0F, 1.0F);
/* 557 */     fontx.drawCenteredStringWithShadow(NameCommand.name, 
/* 558 */       width / 1.95D - fontx.getStringWidth(NameCommand.name) / 5, height / 6, 
/* 559 */       new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 560 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 561 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB());
/* 562 */     GlStateManager.popMatrix();
/* 563 */     GlStateManager.pushMatrix();
/* 564 */     String xd = "Welcome §f" + GuiAuth.username.getText() + "!";
/* 565 */     String name = "Logged in as §f" + this.mc.session.getUsername();
/* 566 */     CFontRenderer font = FontLoaders.vardana12;
/* 567 */     font.drawStringWithShadow(xd, 2.0D, height - 20, 
/* 568 */       new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 569 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 570 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB());
/* 571 */     font.drawStringWithShadow(name, 2.0D, height - 10, 
/* 572 */       new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 573 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 574 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB());
/* 575 */     Iterator localIterator2; for (Iterator localIterator1 = this.particles.particles.iterator(); localIterator1.hasNext(); 
/* 576 */         localIterator2.hasNext())
/*     */     {
/* 575 */       Particle p = (Particle)localIterator1.next();
/* 576 */       localIterator2 = this.particles.particles.iterator(); continue;Particle p2 = (Particle)localIterator2.next();
/* 577 */       int xx = (int)(MathHelper.cos(0.1F * (p.x + p.k)) * 10.0F);
/* 578 */       int xx2 = (int)(MathHelper.cos(0.1F * (p2.x + p2.k)) * 10.0F);
/*     */       
/* 580 */       int i = (mouseX >= p.x + xx - 95) && (mouseY >= p.y - 90) && (mouseX <= p.x) && (
/* 581 */         mouseY <= p.y) ? 1 : 0;
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
/* 636 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
/*     */     throws IOException
/*     */   {
/* 643 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */     
/* 645 */     synchronized (this.threadLock) {
/* 646 */       if ((this.openGLWarning1.length() > 0) && (mouseX >= this.field_92022_t) && (mouseX <= this.field_92020_v) && 
/* 647 */         (mouseY >= this.field_92021_u) && (mouseY <= this.field_92019_w)) {
/* 648 */         GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
/* 649 */         guiconfirmopenlink.disableSecurityWarning();
/* 650 */         this.mc.displayGuiScreen(guiconfirmopenlink);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiMainMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */