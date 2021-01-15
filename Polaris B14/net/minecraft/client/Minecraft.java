/*      */ package net.minecraft.client;
/*      */ 
/*      */ import com.google.common.collect.Iterables;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Queues;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.google.common.util.concurrent.Futures;
/*      */ import com.google.common.util.concurrent.ListenableFuture;
/*      */ import com.google.common.util.concurrent.ListenableFutureTask;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*      */ import com.mojang.authlib.properties.Property;
/*      */ import com.mojang.authlib.properties.PropertyMap;
/*      */ import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.net.Proxy;
/*      */ import java.net.SocketAddress;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.text.DecimalFormat;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.Executors;
/*      */ import java.util.concurrent.FutureTask;
/*      */ import javax.imageio.ImageIO;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.client.audio.MusicTicker;
/*      */ import net.minecraft.client.audio.MusicTicker.MusicType;
/*      */ import net.minecraft.client.audio.SoundHandler;
/*      */ import net.minecraft.client.entity.EntityPlayerSP;
/*      */ import net.minecraft.client.gui.FontRenderer;
/*      */ import net.minecraft.client.gui.GuiChat;
/*      */ import net.minecraft.client.gui.GuiControls;
/*      */ import net.minecraft.client.gui.GuiGameOver;
/*      */ import net.minecraft.client.gui.GuiIngame;
/*      */ import net.minecraft.client.gui.GuiMainMenu;
/*      */ import net.minecraft.client.gui.GuiNewChat;
/*      */ import net.minecraft.client.gui.GuiScreen;
/*      */ import net.minecraft.client.gui.GuiSleepMP;
/*      */ import net.minecraft.client.gui.GuiSpectator;
/*      */ import net.minecraft.client.gui.GuiYesNo;
/*      */ import net.minecraft.client.gui.GuiYesNoCallback;
/*      */ import net.minecraft.client.gui.MapItemRenderer;
/*      */ import net.minecraft.client.gui.ScaledResolution;
/*      */ import net.minecraft.client.gui.achievement.GuiAchievement;
/*      */ import net.minecraft.client.gui.inventory.GuiInventory;
/*      */ import net.minecraft.client.gui.stream.GuiStreamUnavailable;
/*      */ import net.minecraft.client.main.GameConfiguration;
/*      */ import net.minecraft.client.main.GameConfiguration.DisplayInformation;
/*      */ import net.minecraft.client.main.GameConfiguration.FolderInformation;
/*      */ import net.minecraft.client.main.GameConfiguration.GameInformation;
/*      */ import net.minecraft.client.main.GameConfiguration.ServerInformation;
/*      */ import net.minecraft.client.main.GameConfiguration.UserInformation;
/*      */ import net.minecraft.client.multiplayer.GuiConnecting;
/*      */ import net.minecraft.client.multiplayer.PlayerControllerMP;
/*      */ import net.minecraft.client.multiplayer.ServerData;
/*      */ import net.minecraft.client.multiplayer.WorldClient;
/*      */ import net.minecraft.client.network.NetHandlerLoginClient;
/*      */ import net.minecraft.client.network.NetHandlerPlayClient;
/*      */ import net.minecraft.client.particle.EffectRenderer;
/*      */ import net.minecraft.client.renderer.BlockRendererDispatcher;
/*      */ import net.minecraft.client.renderer.EntityRenderer;
/*      */ import net.minecraft.client.renderer.GlStateManager;
/*      */ import net.minecraft.client.renderer.ItemRenderer;
/*      */ import net.minecraft.client.renderer.OpenGlHelper;
/*      */ import net.minecraft.client.renderer.RenderGlobal;
/*      */ import net.minecraft.client.renderer.Tessellator;
/*      */ import net.minecraft.client.renderer.WorldRenderer;
/*      */ import net.minecraft.client.renderer.chunk.RenderChunk;
/*      */ import net.minecraft.client.renderer.entity.RenderItem;
/*      */ import net.minecraft.client.renderer.entity.RenderManager;
/*      */ import net.minecraft.client.renderer.texture.TextureManager;
/*      */ import net.minecraft.client.renderer.texture.TextureMap;
/*      */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*      */ import net.minecraft.client.resources.DefaultResourcePack;
/*      */ import net.minecraft.client.resources.FoliageColorReloadListener;
/*      */ import net.minecraft.client.resources.GrassColorReloadListener;
/*      */ import net.minecraft.client.resources.I18n;
/*      */ import net.minecraft.client.resources.IReloadableResourceManager;
/*      */ import net.minecraft.client.resources.IResourceManager;
/*      */ import net.minecraft.client.resources.IResourcePack;
/*      */ import net.minecraft.client.resources.Language;
/*      */ import net.minecraft.client.resources.LanguageManager;
/*      */ import net.minecraft.client.resources.ResourceIndex;
/*      */ import net.minecraft.client.resources.ResourcePackRepository;
/*      */ import net.minecraft.client.resources.ResourcePackRepository.Entry;
/*      */ import net.minecraft.client.resources.SkinManager;
/*      */ import net.minecraft.client.resources.data.AnimationMetadataSection;
/*      */ import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
/*      */ import net.minecraft.client.resources.data.FontMetadataSection;
/*      */ import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
/*      */ import net.minecraft.client.resources.data.IMetadataSerializer;
/*      */ import net.minecraft.client.resources.data.LanguageMetadataSection;
/*      */ import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
/*      */ import net.minecraft.client.resources.data.PackMetadataSection;
/*      */ import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
/*      */ import net.minecraft.client.resources.data.TextureMetadataSection;
/*      */ import net.minecraft.client.resources.model.ModelManager;
/*      */ import net.minecraft.client.settings.GameSettings;
/*      */ import net.minecraft.client.settings.GameSettings.Options;
/*      */ import net.minecraft.client.settings.KeyBinding;
/*      */ import net.minecraft.client.shader.Framebuffer;
/*      */ import net.minecraft.client.stream.IStream;
/*      */ import net.minecraft.client.stream.NullStream;
/*      */ import net.minecraft.client.stream.TwitchStream;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.entity.DataWatcher;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityLeashKnot;
/*      */ import net.minecraft.entity.EntityList;
/*      */ import net.minecraft.entity.boss.BossStatus;
/*      */ import net.minecraft.entity.item.EntityArmorStand;
/*      */ import net.minecraft.entity.item.EntityItemFrame;
/*      */ import net.minecraft.entity.item.EntityMinecart;
/*      */ import net.minecraft.entity.item.EntityPainting;
/*      */ import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
/*      */ import net.minecraft.entity.player.InventoryPlayer;
/*      */ import net.minecraft.entity.player.PlayerCapabilities;
/*      */ import net.minecraft.init.Bootstrap;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.inventory.Container;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemBlock;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.nbt.NBTTagString;
/*      */ import net.minecraft.network.NetworkManager;
/*      */ import net.minecraft.network.NetworkSystem;
/*      */ import net.minecraft.network.handshake.client.C00Handshake;
/*      */ import net.minecraft.network.login.client.C00PacketLoginStart;
/*      */ import net.minecraft.network.play.client.C16PacketClientStatus;
/*      */ import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
/*      */ import net.minecraft.profiler.IPlayerUsage;
/*      */ import net.minecraft.profiler.PlayerUsageSnooper;
/*      */ import net.minecraft.profiler.Profiler;
/*      */ import net.minecraft.profiler.Profiler.Result;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.integrated.IntegratedServer;
/*      */ import net.minecraft.stats.IStatStringFormat;
/*      */ import net.minecraft.stats.StatFileWriter;
/*      */ import net.minecraft.tileentity.TileEntity;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ChatComponentText;
/*      */ import net.minecraft.util.FrameTimer;
/*      */ import net.minecraft.util.IThreadListener;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.MouseHelper;
/*      */ import net.minecraft.util.MovementInputFromOptions;
/*      */ import net.minecraft.util.MovingObjectPosition;
/*      */ import net.minecraft.util.MovingObjectPosition.MovingObjectType;
/*      */ import net.minecraft.util.ReportedException;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.util.ScreenShotHelper;
/*      */ import net.minecraft.util.Session;
/*      */ import net.minecraft.util.Timer;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.Util.EnumOS;
/*      */ import net.minecraft.world.EnumDifficulty;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldProviderEnd;
/*      */ import net.minecraft.world.WorldProviderHell;
/*      */ import net.minecraft.world.WorldSettings;
/*      */ import net.minecraft.world.chunk.storage.AnvilSaveConverter;
/*      */ import net.minecraft.world.storage.ISaveFormat;
/*      */ import net.minecraft.world.storage.ISaveHandler;
/*      */ import net.minecraft.world.storage.WorldInfo;
/*      */ import org.apache.commons.lang3.Validate;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ import org.lwjgl.LWJGLException;
/*      */ import org.lwjgl.Sys;
/*      */ import org.lwjgl.input.Keyboard;
/*      */ import org.lwjgl.input.Mouse;
/*      */ import org.lwjgl.opengl.ContextCapabilities;
/*      */ import org.lwjgl.opengl.Display;
/*      */ import org.lwjgl.opengl.DisplayMode;
/*      */ import org.lwjgl.opengl.GL11;
/*      */ import org.lwjgl.opengl.GLContext;
/*      */ import org.lwjgl.opengl.OpenGLException;
/*      */ import org.lwjgl.opengl.PixelFormat;
/*      */ import org.lwjgl.util.glu.GLU;
/*      */ import rip.jutting.polaris.Polaris;
/*      */ import rip.jutting.polaris.event.events.EventKey;
/*      */ import rip.jutting.polaris.event.events.EventMouse;
/*      */ 
/*      */ public class Minecraft implements IThreadListener, IPlayerUsage
/*      */ {
/*  204 */   private static final Logger logger = ;
/*  205 */   private static final ResourceLocation locationMojangPng = new ResourceLocation("textures/gui/title/mojang.png");
/*  206 */   public static final boolean isRunningOnMac = Util.getOSType() == Util.EnumOS.OSX;
/*      */   
/*      */ 
/*  209 */   public static byte[] memoryReserve = new byte[10485760];
/*  210 */   private static final List<DisplayMode> macDisplayModes = Lists.newArrayList(new DisplayMode[] { new DisplayMode(2560, 1600), new DisplayMode(2880, 1800) });
/*      */   
/*      */   private final File fileResourcepacks;
/*      */   
/*      */   private final PropertyMap twitchDetails;
/*      */   
/*      */   private final PropertyMap field_181038_N;
/*      */   
/*      */   private ServerData currentServerData;
/*      */   
/*      */   private TextureManager renderEngine;
/*      */   
/*      */   private static Minecraft theMinecraft;
/*      */   public PlayerControllerMP playerController;
/*      */   private boolean fullscreen;
/*  225 */   private boolean enableGLErrorChecking = true;
/*      */   
/*      */   private boolean hasCrashed;
/*      */   
/*      */   private CrashReport crashReporter;
/*      */   public int displayWidth;
/*      */   public int displayHeight;
/*  232 */   private boolean field_181541_X = false;
/*  233 */   public Timer timer = new Timer(20.0F);
/*      */   
/*      */ 
/*  236 */   private PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper("client", this, MinecraftServer.getCurrentTimeMillis());
/*      */   
/*      */   public WorldClient theWorld;
/*      */   
/*      */   public RenderGlobal renderGlobal;
/*      */   
/*      */   public RenderManager renderManager;
/*      */   
/*      */   private RenderItem renderItem;
/*      */   
/*      */   private ItemRenderer itemRenderer;
/*      */   
/*      */   public EntityPlayerSP thePlayer;
/*      */   
/*      */   private Entity renderViewEntity;
/*      */   
/*      */   public Entity pointedEntity;
/*      */   
/*      */   public EffectRenderer effectRenderer;
/*      */   
/*      */   public Session session;
/*      */   
/*      */   private boolean isGamePaused;
/*      */   
/*      */   public FontRenderer fontRendererObj;
/*      */   
/*      */   public FontRenderer standardGalacticFontRenderer;
/*      */   
/*      */   public GuiScreen currentScreen;
/*      */   
/*      */   public LoadingScreenRenderer loadingScreen;
/*      */   
/*      */   public EntityRenderer entityRenderer;
/*      */   
/*      */   private int leftClickCounter;
/*      */   
/*      */   private int tempDisplayWidth;
/*      */   
/*      */   private int tempDisplayHeight;
/*      */   
/*      */   private IntegratedServer theIntegratedServer;
/*      */   
/*      */   public GuiAchievement guiAchievement;
/*      */   
/*      */   public GuiIngame ingameGUI;
/*      */   
/*      */   public boolean skipRenderWorld;
/*      */   
/*      */   public MovingObjectPosition objectMouseOver;
/*      */   
/*      */   public GameSettings gameSettings;
/*      */   
/*      */   public MouseHelper mouseHelper;
/*      */   
/*      */   public final File mcDataDir;
/*      */   
/*      */   private final File fileAssets;
/*      */   
/*      */   private final String launchedVersion;
/*      */   
/*      */   private final Proxy proxy;
/*      */   
/*      */   private ISaveFormat saveLoader;
/*      */   
/*      */   public static int debugFPS;
/*      */   
/*      */   public int rightClickDelayTimer;
/*      */   
/*      */   private String serverName;
/*      */   
/*      */   private int serverPort;
/*      */   public boolean inGameHasFocus;
/*  308 */   long systemTime = getSystemTime();
/*      */   
/*      */   private int joinPlayerCounter;
/*      */   
/*  312 */   public final FrameTimer field_181542_y = new FrameTimer();
/*  313 */   long field_181543_z = System.nanoTime();
/*      */   
/*      */   private final boolean jvm64bit;
/*      */   
/*      */   private final boolean isDemo;
/*      */   private NetworkManager myNetworkManager;
/*      */   private boolean integratedServerIsRunning;
/*  320 */   public final Profiler mcProfiler = new Profiler();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  325 */   private long debugCrashKeyPressTime = -1L;
/*      */   private IReloadableResourceManager mcResourceManager;
/*  327 */   private final IMetadataSerializer metadataSerializer_ = new IMetadataSerializer();
/*  328 */   private final List<IResourcePack> defaultResourcePacks = Lists.newArrayList();
/*      */   private final DefaultResourcePack mcDefaultResourcePack;
/*      */   private ResourcePackRepository mcResourcePackRepository;
/*      */   private LanguageManager mcLanguageManager;
/*      */   private IStream stream;
/*      */   private Framebuffer framebufferMc;
/*      */   private TextureMap textureMapBlocks;
/*      */   private SoundHandler mcSoundHandler;
/*      */   private MusicTicker mcMusicTicker;
/*      */   private ResourceLocation mojangLogo;
/*      */   private final MinecraftSessionService sessionService;
/*      */   private SkinManager skinManager;
/*  340 */   private final Queue<FutureTask<?>> scheduledTasks = Queues.newArrayDeque();
/*  341 */   private long field_175615_aJ = 0L;
/*  342 */   private final Thread mcThread = Thread.currentThread();
/*      */   
/*      */ 
/*      */ 
/*      */   private ModelManager modelManager;
/*      */   
/*      */ 
/*      */ 
/*      */   private BlockRendererDispatcher blockRenderDispatcher;
/*      */   
/*      */ 
/*  353 */   volatile boolean running = true;
/*      */   
/*      */ 
/*  356 */   public String debug = "";
/*  357 */   public boolean field_175613_B = false;
/*  358 */   public boolean field_175614_C = false;
/*  359 */   public boolean field_175611_D = false;
/*  360 */   public boolean renderChunksMany = true;
/*      */   
/*      */ 
/*  363 */   long debugUpdateTime = getSystemTime();
/*      */   
/*      */   int fpsCounter;
/*      */   
/*  367 */   long prevFrameTime = -1L;
/*      */   
/*      */ 
/*  370 */   private String debugProfilerName = "root";
/*      */   
/*      */   public Minecraft(GameConfiguration gameConfig)
/*      */   {
/*  374 */     theMinecraft = this;
/*  375 */     this.mcDataDir = gameConfig.folderInfo.mcDataDir;
/*  376 */     this.fileAssets = gameConfig.folderInfo.assetsDir;
/*  377 */     this.fileResourcepacks = gameConfig.folderInfo.resourcePacksDir;
/*  378 */     this.launchedVersion = gameConfig.gameInfo.version;
/*  379 */     this.twitchDetails = gameConfig.userInfo.userProperties;
/*  380 */     this.field_181038_N = gameConfig.userInfo.field_181172_c;
/*  381 */     this.mcDefaultResourcePack = new DefaultResourcePack(new ResourceIndex(gameConfig.folderInfo.assetsDir, gameConfig.folderInfo.assetIndex).getResourceMap());
/*  382 */     this.proxy = (gameConfig.userInfo.proxy == null ? Proxy.NO_PROXY : gameConfig.userInfo.proxy);
/*  383 */     this.sessionService = new YggdrasilAuthenticationService(gameConfig.userInfo.proxy, UUID.randomUUID().toString()).createMinecraftSessionService();
/*  384 */     this.session = gameConfig.userInfo.session;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  401 */     logger.info("Setting user: " + this.session.getUsername());
/*  402 */     logger.info("(Session ID is " + this.session.getSessionID() + ")");
/*  403 */     this.isDemo = gameConfig.gameInfo.isDemo;
/*  404 */     this.displayWidth = (gameConfig.displayInfo.width > 0 ? gameConfig.displayInfo.width : 1);
/*  405 */     this.displayHeight = (gameConfig.displayInfo.height > 0 ? gameConfig.displayInfo.height : 1);
/*  406 */     this.tempDisplayWidth = gameConfig.displayInfo.width;
/*  407 */     this.tempDisplayHeight = gameConfig.displayInfo.height;
/*  408 */     this.fullscreen = gameConfig.displayInfo.fullscreen;
/*  409 */     this.jvm64bit = isJvm64bit();
/*  410 */     this.theIntegratedServer = new IntegratedServer(this);
/*      */     
/*  412 */     if (gameConfig.serverInfo.serverName != null)
/*      */     {
/*  414 */       this.serverName = gameConfig.serverInfo.serverName;
/*  415 */       this.serverPort = gameConfig.serverInfo.serverPort;
/*      */     }
/*      */     
/*  418 */     ImageIO.setUseCache(false);
/*  419 */     Bootstrap.register();
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public void run()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: iconst_1
/*      */     //   2: putfield 399	net/minecraft/client/Minecraft:running	Z
/*      */     //   5: aload_0
/*      */     //   6: invokespecial 612	net/minecraft/client/Minecraft:startGame	()V
/*      */     //   9: goto +81 -> 90
/*      */     //   12: astore_1
/*      */     //   13: aload_1
/*      */     //   14: ldc_w 614
/*      */     //   17: invokestatic 620	net/minecraft/crash/CrashReport:makeCrashReport	(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/crash/CrashReport;
/*      */     //   20: astore_2
/*      */     //   21: aload_2
/*      */     //   22: ldc_w 622
/*      */     //   25: invokevirtual 626	net/minecraft/crash/CrashReport:makeCategory	(Ljava/lang/String;)Lnet/minecraft/crash/CrashReportCategory;
/*      */     //   28: pop
/*      */     //   29: aload_0
/*      */     //   30: aload_0
/*      */     //   31: aload_2
/*      */     //   32: invokevirtual 630	net/minecraft/client/Minecraft:addGraphicsAndWorldToCrashReport	(Lnet/minecraft/crash/CrashReport;)Lnet/minecraft/crash/CrashReport;
/*      */     //   35: invokevirtual 634	net/minecraft/client/Minecraft:displayCrashReport	(Lnet/minecraft/crash/CrashReport;)V
/*      */     //   38: return
/*      */     //   39: aload_0
/*      */     //   40: getfield 636	net/minecraft/client/Minecraft:hasCrashed	Z
/*      */     //   43: ifeq +10 -> 53
/*      */     //   46: aload_0
/*      */     //   47: getfield 638	net/minecraft/client/Minecraft:crashReporter	Lnet/minecraft/crash/CrashReport;
/*      */     //   50: ifnonnull +32 -> 82
/*      */     //   53: aload_0
/*      */     //   54: invokespecial 641	net/minecraft/client/Minecraft:runGameLoop	()V
/*      */     //   57: goto +33 -> 90
/*      */     //   60: astore_1
/*      */     //   61: aload_0
/*      */     //   62: invokevirtual 644	net/minecraft/client/Minecraft:freeMemory	()V
/*      */     //   65: aload_0
/*      */     //   66: new 646	net/minecraft/client/gui/GuiMemoryErrorScreen
/*      */     //   69: dup
/*      */     //   70: invokespecial 647	net/minecraft/client/gui/GuiMemoryErrorScreen:<init>	()V
/*      */     //   73: invokevirtual 651	net/minecraft/client/Minecraft:displayGuiScreen	(Lnet/minecraft/client/gui/GuiScreen;)V
/*      */     //   76: invokestatic 654	java/lang/System:gc	()V
/*      */     //   79: goto +11 -> 90
/*      */     //   82: aload_0
/*      */     //   83: aload_0
/*      */     //   84: getfield 638	net/minecraft/client/Minecraft:crashReporter	Lnet/minecraft/crash/CrashReport;
/*      */     //   87: invokevirtual 634	net/minecraft/client/Minecraft:displayCrashReport	(Lnet/minecraft/crash/CrashReport;)V
/*      */     //   90: aload_0
/*      */     //   91: getfield 399	net/minecraft/client/Minecraft:running	Z
/*      */     //   94: ifne -55 -> 39
/*      */     //   97: goto +104 -> 201
/*      */     //   100: astore_1
/*      */     //   101: aload_0
/*      */     //   102: invokevirtual 657	net/minecraft/client/Minecraft:shutdownMinecraftApplet	()V
/*      */     //   105: goto +101 -> 206
/*      */     //   108: astore_1
/*      */     //   109: aload_0
/*      */     //   110: aload_1
/*      */     //   111: invokevirtual 661	net/minecraft/util/ReportedException:getCrashReport	()Lnet/minecraft/crash/CrashReport;
/*      */     //   114: invokevirtual 630	net/minecraft/client/Minecraft:addGraphicsAndWorldToCrashReport	(Lnet/minecraft/crash/CrashReport;)Lnet/minecraft/crash/CrashReport;
/*      */     //   117: pop
/*      */     //   118: aload_0
/*      */     //   119: invokevirtual 644	net/minecraft/client/Minecraft:freeMemory	()V
/*      */     //   122: getstatic 279	net/minecraft/client/Minecraft:logger	Lorg/apache/logging/log4j/Logger;
/*      */     //   125: ldc_w 663
/*      */     //   128: aload_1
/*      */     //   129: invokeinterface 667 3 0
/*      */     //   134: aload_0
/*      */     //   135: aload_1
/*      */     //   136: invokevirtual 661	net/minecraft/util/ReportedException:getCrashReport	()Lnet/minecraft/crash/CrashReport;
/*      */     //   139: invokevirtual 634	net/minecraft/client/Minecraft:displayCrashReport	(Lnet/minecraft/crash/CrashReport;)V
/*      */     //   142: aload_0
/*      */     //   143: invokevirtual 657	net/minecraft/client/Minecraft:shutdownMinecraftApplet	()V
/*      */     //   146: goto +60 -> 206
/*      */     //   149: astore_1
/*      */     //   150: aload_0
/*      */     //   151: new 616	net/minecraft/crash/CrashReport
/*      */     //   154: dup
/*      */     //   155: ldc_w 669
/*      */     //   158: aload_1
/*      */     //   159: invokespecial 671	net/minecraft/crash/CrashReport:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
/*      */     //   162: invokevirtual 630	net/minecraft/client/Minecraft:addGraphicsAndWorldToCrashReport	(Lnet/minecraft/crash/CrashReport;)Lnet/minecraft/crash/CrashReport;
/*      */     //   165: astore_2
/*      */     //   166: aload_0
/*      */     //   167: invokevirtual 644	net/minecraft/client/Minecraft:freeMemory	()V
/*      */     //   170: getstatic 279	net/minecraft/client/Minecraft:logger	Lorg/apache/logging/log4j/Logger;
/*      */     //   173: ldc_w 673
/*      */     //   176: aload_1
/*      */     //   177: invokeinterface 667 3 0
/*      */     //   182: aload_0
/*      */     //   183: aload_2
/*      */     //   184: invokevirtual 634	net/minecraft/client/Minecraft:displayCrashReport	(Lnet/minecraft/crash/CrashReport;)V
/*      */     //   187: aload_0
/*      */     //   188: invokevirtual 657	net/minecraft/client/Minecraft:shutdownMinecraftApplet	()V
/*      */     //   191: goto +15 -> 206
/*      */     //   194: astore_3
/*      */     //   195: aload_0
/*      */     //   196: invokevirtual 657	net/minecraft/client/Minecraft:shutdownMinecraftApplet	()V
/*      */     //   199: aload_3
/*      */     //   200: athrow
/*      */     //   201: aload_0
/*      */     //   202: invokevirtual 657	net/minecraft/client/Minecraft:shutdownMinecraftApplet	()V
/*      */     //   205: return
/*      */     //   206: return
/*      */     // Line number table:
/*      */     //   Java source line #424	-> byte code offset #0
/*      */     //   Java source line #428	-> byte code offset #5
/*      */     //   Java source line #431	-> byte code offset #9
/*      */     //   Java source line #432	-> byte code offset #12
/*      */     //   Java source line #434	-> byte code offset #13
/*      */     //   Java source line #435	-> byte code offset #21
/*      */     //   Java source line #436	-> byte code offset #29
/*      */     //   Java source line #437	-> byte code offset #38
/*      */     //   Java source line #446	-> byte code offset #39
/*      */     //   Java source line #450	-> byte code offset #53
/*      */     //   Java source line #451	-> byte code offset #57
/*      */     //   Java source line #452	-> byte code offset #60
/*      */     //   Java source line #454	-> byte code offset #61
/*      */     //   Java source line #455	-> byte code offset #65
/*      */     //   Java source line #456	-> byte code offset #76
/*      */     //   Java source line #458	-> byte code offset #79
/*      */     //   Java source line #461	-> byte code offset #82
/*      */     //   Java source line #444	-> byte code offset #90
/*      */     //   Java source line #464	-> byte code offset #97
/*      */     //   Java source line #465	-> byte code offset #100
/*      */     //   Java source line #487	-> byte code offset #101
/*      */     //   Java source line #467	-> byte code offset #105
/*      */     //   Java source line #469	-> byte code offset #108
/*      */     //   Java source line #471	-> byte code offset #109
/*      */     //   Java source line #472	-> byte code offset #118
/*      */     //   Java source line #473	-> byte code offset #122
/*      */     //   Java source line #474	-> byte code offset #134
/*      */     //   Java source line #487	-> byte code offset #142
/*      */     //   Java source line #475	-> byte code offset #146
/*      */     //   Java source line #477	-> byte code offset #149
/*      */     //   Java source line #479	-> byte code offset #150
/*      */     //   Java source line #480	-> byte code offset #166
/*      */     //   Java source line #481	-> byte code offset #170
/*      */     //   Java source line #482	-> byte code offset #182
/*      */     //   Java source line #487	-> byte code offset #187
/*      */     //   Java source line #483	-> byte code offset #191
/*      */     //   Java source line #486	-> byte code offset #194
/*      */     //   Java source line #487	-> byte code offset #195
/*      */     //   Java source line #488	-> byte code offset #199
/*      */     //   Java source line #487	-> byte code offset #201
/*      */     //   Java source line #490	-> byte code offset #205
/*      */     //   Java source line #492	-> byte code offset #206
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	207	0	this	Minecraft
/*      */     //   12	2	1	throwable	Throwable
/*      */     //   60	2	1	var10	OutOfMemoryError
/*      */     //   100	2	1	var12	net.minecraft.util.MinecraftError
/*      */     //   108	28	1	reportedexception	ReportedException
/*      */     //   149	28	1	throwable1	Throwable
/*      */     //   20	12	2	crashreport	CrashReport
/*      */     //   165	19	2	crashreport1	CrashReport
/*      */     //   194	6	3	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   5	9	12	java/lang/Throwable
/*      */     //   53	57	60	java/lang/OutOfMemoryError
/*      */     //   39	97	100	net/minecraft/util/MinecraftError
/*      */     //   39	97	108	net/minecraft/util/ReportedException
/*      */     //   39	97	149	java/lang/Throwable
/*      */     //   39	101	194	finally
/*      */     //   108	142	194	finally
/*      */     //   149	187	194	finally
/*      */   }
/*      */   
/*      */   private void startGame()
/*      */     throws LWJGLException, IOException
/*      */   {
/*  500 */     this.gameSettings = new GameSettings(this, this.mcDataDir);
/*  501 */     this.defaultResourcePacks.add(this.mcDefaultResourcePack);
/*  502 */     startTimerHackThread();
/*      */     
/*  504 */     if ((this.gameSettings.overrideHeight > 0) && (this.gameSettings.overrideWidth > 0))
/*      */     {
/*  506 */       this.displayWidth = this.gameSettings.overrideWidth;
/*  507 */       this.displayHeight = this.gameSettings.overrideHeight;
/*      */     }
/*      */     
/*  510 */     logger.info("LWJGL Version: " + Sys.getVersion());
/*  511 */     setWindowIcon();
/*  512 */     setInitialDisplayMode();
/*  513 */     createDisplay();
/*  514 */     OpenGlHelper.initializeTextures();
/*  515 */     this.framebufferMc = new Framebuffer(this.displayWidth, this.displayHeight, true);
/*  516 */     this.framebufferMc.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
/*  517 */     registerMetadataSerializers();
/*  518 */     this.mcResourcePackRepository = new ResourcePackRepository(this.fileResourcepacks, new File(this.mcDataDir, "server-resource-packs"), this.mcDefaultResourcePack, this.metadataSerializer_, this.gameSettings);
/*  519 */     this.mcResourceManager = new net.minecraft.client.resources.SimpleReloadableResourceManager(this.metadataSerializer_);
/*  520 */     this.mcLanguageManager = new LanguageManager(this.metadataSerializer_, this.gameSettings.language);
/*  521 */     this.mcResourceManager.registerReloadListener(this.mcLanguageManager);
/*  522 */     refreshResources();
/*  523 */     this.renderEngine = new TextureManager(this.mcResourceManager);
/*  524 */     this.mcResourceManager.registerReloadListener(this.renderEngine);
/*  525 */     drawSplashScreen(this.renderEngine);
/*  526 */     initStream();
/*  527 */     this.skinManager = new SkinManager(this.renderEngine, new File(this.fileAssets, "skins"), this.sessionService);
/*  528 */     this.saveLoader = new AnvilSaveConverter(new File(this.mcDataDir, "saves"));
/*  529 */     this.mcSoundHandler = new SoundHandler(this.mcResourceManager, this.gameSettings);
/*  530 */     this.mcResourceManager.registerReloadListener(this.mcSoundHandler);
/*  531 */     this.mcMusicTicker = new MusicTicker(this);
/*  532 */     this.fontRendererObj = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii.png"), this.renderEngine, false);
/*      */     
/*  534 */     if (this.gameSettings.language != null)
/*      */     {
/*  536 */       this.fontRendererObj.setUnicodeFlag(isUnicode());
/*  537 */       this.fontRendererObj.setBidiFlag(this.mcLanguageManager.isCurrentLanguageBidirectional());
/*      */     }
/*      */     
/*  540 */     this.standardGalacticFontRenderer = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii_sga.png"), this.renderEngine, false);
/*  541 */     this.mcResourceManager.registerReloadListener(this.fontRendererObj);
/*  542 */     this.mcResourceManager.registerReloadListener(this.standardGalacticFontRenderer);
/*  543 */     this.mcResourceManager.registerReloadListener(new GrassColorReloadListener());
/*  544 */     this.mcResourceManager.registerReloadListener(new FoliageColorReloadListener());
/*  545 */     net.minecraft.stats.AchievementList.openInventory.setStatStringFormatter(new IStatStringFormat()
/*      */     {
/*      */       public String formatString(String p_74535_1_)
/*      */       {
/*      */         try
/*      */         {
/*  551 */           return String.format(p_74535_1_, new Object[] { GameSettings.getKeyDisplayString(Minecraft.this.gameSettings.keyBindInventory.getKeyCode()) });
/*      */         }
/*      */         catch (Exception exception)
/*      */         {
/*  555 */           return "Error: " + exception.getLocalizedMessage();
/*      */         }
/*      */       }
/*  558 */     });
/*  559 */     this.mouseHelper = new MouseHelper();
/*  560 */     checkGLError("Pre startup");
/*  561 */     GlStateManager.enableTexture2D();
/*  562 */     GlStateManager.shadeModel(7425);
/*  563 */     GlStateManager.clearDepth(1.0D);
/*  564 */     GlStateManager.enableDepth();
/*  565 */     GlStateManager.depthFunc(515);
/*  566 */     GlStateManager.enableAlpha();
/*  567 */     GlStateManager.alphaFunc(516, 0.1F);
/*  568 */     GlStateManager.cullFace(1029);
/*  569 */     GlStateManager.matrixMode(5889);
/*  570 */     GlStateManager.loadIdentity();
/*  571 */     GlStateManager.matrixMode(5888);
/*  572 */     checkGLError("Startup");
/*  573 */     this.textureMapBlocks = new TextureMap("textures");
/*  574 */     this.textureMapBlocks.setMipmapLevels(this.gameSettings.mipmapLevels);
/*  575 */     this.renderEngine.loadTickableTexture(TextureMap.locationBlocksTexture, this.textureMapBlocks);
/*  576 */     this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
/*  577 */     this.textureMapBlocks.setBlurMipmapDirect(false, this.gameSettings.mipmapLevels > 0);
/*  578 */     this.modelManager = new ModelManager(this.textureMapBlocks);
/*  579 */     this.mcResourceManager.registerReloadListener(this.modelManager);
/*  580 */     this.renderItem = new RenderItem(this.renderEngine, this.modelManager);
/*  581 */     this.renderManager = new RenderManager(this.renderEngine, this.renderItem);
/*  582 */     this.itemRenderer = new ItemRenderer(this);
/*  583 */     this.mcResourceManager.registerReloadListener(this.renderItem);
/*  584 */     this.entityRenderer = new EntityRenderer(this, this.mcResourceManager);
/*  585 */     this.mcResourceManager.registerReloadListener(this.entityRenderer);
/*  586 */     this.blockRenderDispatcher = new BlockRendererDispatcher(this.modelManager.getBlockModelShapes(), this.gameSettings);
/*  587 */     this.mcResourceManager.registerReloadListener(this.blockRenderDispatcher);
/*  588 */     this.renderGlobal = new RenderGlobal(this);
/*  589 */     this.mcResourceManager.registerReloadListener(this.renderGlobal);
/*  590 */     this.guiAchievement = new GuiAchievement(this);
/*  591 */     GlStateManager.viewport(0, 0, this.displayWidth, this.displayHeight);
/*  592 */     this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);
/*  593 */     checkGLError("Post startup");
/*  594 */     this.ingameGUI = new GuiIngame(this);
/*      */     
/*  596 */     if (this.serverName != null)
/*      */     {
/*  598 */       displayGuiScreen(new GuiConnecting(new GuiMainMenu(), this, this.serverName, this.serverPort));
/*      */     }
/*      */     else
/*      */     {
/*  602 */       displayGuiScreen(new GuiMainMenu());
/*      */     }
/*      */     
/*  605 */     this.renderEngine.deleteTexture(this.mojangLogo);
/*  606 */     this.mojangLogo = null;
/*  607 */     this.loadingScreen = new LoadingScreenRenderer(this);
/*      */     
/*  609 */     if ((this.gameSettings.fullScreen) && (!this.fullscreen))
/*      */     {
/*  611 */       toggleFullscreen();
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  616 */       Display.setVSyncEnabled(this.gameSettings.enableVsync);
/*      */     }
/*      */     catch (OpenGLException var2)
/*      */     {
/*  620 */       this.gameSettings.enableVsync = false;
/*  621 */       this.gameSettings.saveOptions();
/*      */     }
/*  623 */     Polaris.instance.startClient();
/*      */     
/*  625 */     this.renderGlobal.makeEntityOutlineShader();
/*      */   }
/*      */   
/*      */   private void registerMetadataSerializers()
/*      */   {
/*  630 */     this.metadataSerializer_.registerMetadataSectionType(new net.minecraft.client.resources.data.TextureMetadataSectionSerializer(), TextureMetadataSection.class);
/*  631 */     this.metadataSerializer_.registerMetadataSectionType(new FontMetadataSectionSerializer(), FontMetadataSection.class);
/*  632 */     this.metadataSerializer_.registerMetadataSectionType(new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
/*  633 */     this.metadataSerializer_.registerMetadataSectionType(new PackMetadataSectionSerializer(), PackMetadataSection.class);
/*  634 */     this.metadataSerializer_.registerMetadataSectionType(new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
/*      */   }
/*      */   
/*      */   private void initStream()
/*      */   {
/*      */     try
/*      */     {
/*  641 */       this.stream = new TwitchStream(this, (Property)Iterables.getFirst(this.twitchDetails.get("twitch_access_token"), null));
/*      */     }
/*      */     catch (Throwable throwable)
/*      */     {
/*  645 */       this.stream = new NullStream(throwable);
/*  646 */       logger.error("Couldn't initialize twitch stream");
/*      */     }
/*      */   }
/*      */   
/*      */   private void createDisplay() throws LWJGLException
/*      */   {
/*  652 */     Display.setResizable(true);
/*  653 */     Display.setTitle("Loading " + Polaris.instance.name);
/*      */     
/*      */     try
/*      */     {
/*  657 */       Display.create(new PixelFormat().withDepthBits(24));
/*      */     }
/*      */     catch (LWJGLException lwjglexception)
/*      */     {
/*  661 */       logger.error("Couldn't set pixel format", lwjglexception);
/*      */       
/*      */       try
/*      */       {
/*  665 */         Thread.sleep(1000L);
/*      */       }
/*      */       catch (InterruptedException localInterruptedException) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  672 */       if (this.fullscreen)
/*      */       {
/*  674 */         updateDisplayMode();
/*      */       }
/*      */       
/*  677 */       Display.create();
/*      */     }
/*      */   }
/*      */   
/*      */   private void setInitialDisplayMode() throws LWJGLException
/*      */   {
/*  683 */     if (this.fullscreen)
/*      */     {
/*  685 */       Display.setFullscreen(true);
/*  686 */       DisplayMode displaymode = Display.getDisplayMode();
/*  687 */       this.displayWidth = Math.max(1, displaymode.getWidth());
/*  688 */       this.displayHeight = Math.max(1, displaymode.getHeight());
/*      */     }
/*      */     else
/*      */     {
/*  692 */       Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private void setWindowIcon()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: invokestatic 293	net/minecraft/util/Util:getOSType	()Lnet/minecraft/util/Util$EnumOS;
/*      */     //   3: astore_1
/*      */     //   4: aload_1
/*      */     //   5: getstatic 297	net/minecraft/util/Util$EnumOS:OSX	Lnet/minecraft/util/Util$EnumOS;
/*      */     //   8: if_acmpeq +125 -> 133
/*      */     //   11: aconst_null
/*      */     //   12: astore_2
/*      */     //   13: aconst_null
/*      */     //   14: astore_3
/*      */     //   15: aload_0
/*      */     //   16: getfield 480	net/minecraft/client/Minecraft:mcDefaultResourcePack	Lnet/minecraft/client/resources/DefaultResourcePack;
/*      */     //   19: new 281	net/minecraft/util/ResourceLocation
/*      */     //   22: dup
/*      */     //   23: ldc_w 1197
/*      */     //   26: invokespecial 287	net/minecraft/util/ResourceLocation:<init>	(Ljava/lang/String;)V
/*      */     //   29: invokevirtual 1201	net/minecraft/client/resources/DefaultResourcePack:getInputStreamAssets	(Lnet/minecraft/util/ResourceLocation;)Ljava/io/InputStream;
/*      */     //   32: astore_2
/*      */     //   33: aload_0
/*      */     //   34: getfield 480	net/minecraft/client/Minecraft:mcDefaultResourcePack	Lnet/minecraft/client/resources/DefaultResourcePack;
/*      */     //   37: new 281	net/minecraft/util/ResourceLocation
/*      */     //   40: dup
/*      */     //   41: ldc_w 1203
/*      */     //   44: invokespecial 287	net/minecraft/util/ResourceLocation:<init>	(Ljava/lang/String;)V
/*      */     //   47: invokevirtual 1201	net/minecraft/client/resources/DefaultResourcePack:getInputStreamAssets	(Lnet/minecraft/util/ResourceLocation;)Ljava/io/InputStream;
/*      */     //   50: astore_3
/*      */     //   51: aload_2
/*      */     //   52: ifnull +73 -> 125
/*      */     //   55: aload_3
/*      */     //   56: ifnull +69 -> 125
/*      */     //   59: iconst_2
/*      */     //   60: anewarray 1205	java/nio/ByteBuffer
/*      */     //   63: dup
/*      */     //   64: iconst_0
/*      */     //   65: aload_0
/*      */     //   66: aload_2
/*      */     //   67: invokespecial 1209	net/minecraft/client/Minecraft:readImageToBuffer	(Ljava/io/InputStream;)Ljava/nio/ByteBuffer;
/*      */     //   70: aastore
/*      */     //   71: dup
/*      */     //   72: iconst_1
/*      */     //   73: aload_0
/*      */     //   74: aload_3
/*      */     //   75: invokespecial 1209	net/minecraft/client/Minecraft:readImageToBuffer	(Ljava/io/InputStream;)Ljava/nio/ByteBuffer;
/*      */     //   78: aastore
/*      */     //   79: invokestatic 1213	org/lwjgl/opengl/Display:setIcon	([Ljava/nio/ByteBuffer;)I
/*      */     //   82: pop
/*      */     //   83: goto +42 -> 125
/*      */     //   86: astore 4
/*      */     //   88: getstatic 279	net/minecraft/client/Minecraft:logger	Lorg/apache/logging/log4j/Logger;
/*      */     //   91: ldc_w 1217
/*      */     //   94: aload 4
/*      */     //   96: invokeinterface 1156 3 0
/*      */     //   101: aload_2
/*      */     //   102: invokestatic 1223	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*      */     //   105: aload_3
/*      */     //   106: invokestatic 1223	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*      */     //   109: goto +24 -> 133
/*      */     //   112: astore 5
/*      */     //   114: aload_2
/*      */     //   115: invokestatic 1223	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*      */     //   118: aload_3
/*      */     //   119: invokestatic 1223	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*      */     //   122: aload 5
/*      */     //   124: athrow
/*      */     //   125: aload_2
/*      */     //   126: invokestatic 1223	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*      */     //   129: aload_3
/*      */     //   130: invokestatic 1223	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*      */     //   133: return
/*      */     // Line number table:
/*      */     //   Java source line #698	-> byte code offset #0
/*      */     //   Java source line #700	-> byte code offset #4
/*      */     //   Java source line #702	-> byte code offset #11
/*      */     //   Java source line #703	-> byte code offset #13
/*      */     //   Java source line #707	-> byte code offset #15
/*      */     //   Java source line #708	-> byte code offset #33
/*      */     //   Java source line #710	-> byte code offset #51
/*      */     //   Java source line #712	-> byte code offset #59
/*      */     //   Java source line #714	-> byte code offset #83
/*      */     //   Java source line #715	-> byte code offset #86
/*      */     //   Java source line #717	-> byte code offset #88
/*      */     //   Java source line #721	-> byte code offset #101
/*      */     //   Java source line #722	-> byte code offset #105
/*      */     //   Java source line #720	-> byte code offset #112
/*      */     //   Java source line #721	-> byte code offset #114
/*      */     //   Java source line #722	-> byte code offset #118
/*      */     //   Java source line #723	-> byte code offset #122
/*      */     //   Java source line #721	-> byte code offset #125
/*      */     //   Java source line #722	-> byte code offset #129
/*      */     //   Java source line #725	-> byte code offset #133
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	134	0	this	Minecraft
/*      */     //   3	2	1	util$enumos	Util.EnumOS
/*      */     //   12	114	2	inputstream	InputStream
/*      */     //   14	116	3	inputstream1	InputStream
/*      */     //   86	9	4	ioexception	IOException
/*      */     //   112	11	5	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   15	83	86	java/io/IOException
/*      */     //   15	101	112	finally
/*      */   }
/*      */   
/*      */   private static boolean isJvm64bit()
/*      */   {
/*  729 */     String[] astring = { "sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch" };
/*      */     
/*  731 */     String[] arrayOfString1 = astring;int j = astring.length; for (int i = 0; i < j; i++) { String s = arrayOfString1[i];
/*      */       
/*  733 */       String s1 = System.getProperty(s);
/*      */       
/*  735 */       if ((s1 != null) && (s1.contains("64")))
/*      */       {
/*  737 */         return true;
/*      */       }
/*      */     }
/*      */     
/*  741 */     return false;
/*      */   }
/*      */   
/*      */   public Framebuffer getFramebuffer()
/*      */   {
/*  746 */     return this.framebufferMc;
/*      */   }
/*      */   
/*      */   public String getVersion()
/*      */   {
/*  751 */     return this.launchedVersion;
/*      */   }
/*      */   
/*      */   private void startTimerHackThread()
/*      */   {
/*  756 */     Thread thread = new Thread("Timer hack thread")
/*      */     {
/*      */       public void run()
/*      */       {
/*  760 */         while (Minecraft.this.running)
/*      */         {
/*      */           try
/*      */           {
/*  764 */             Thread.sleep(2147483647L);
/*      */ 
/*      */           }
/*      */           catch (InterruptedException localInterruptedException) {}
/*      */         }
/*      */         
/*      */       }
/*      */       
/*  772 */     };
/*  773 */     thread.setDaemon(true);
/*  774 */     thread.start();
/*      */   }
/*      */   
/*      */   public void crashed(CrashReport crash)
/*      */   {
/*  779 */     this.hasCrashed = true;
/*  780 */     this.crashReporter = crash;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void displayCrashReport(CrashReport crashReportIn)
/*      */   {
/*  788 */     File file1 = new File(getMinecraft().mcDataDir, "crash-reports");
/*  789 */     File file2 = new File(file1, "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-client.txt");
/*  790 */     Bootstrap.printToSYSOUT(crashReportIn.getCompleteReport());
/*      */     
/*  792 */     if (crashReportIn.getFile() != null)
/*      */     {
/*  794 */       Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + crashReportIn.getFile());
/*  795 */       System.exit(-1);
/*      */     }
/*  797 */     else if (crashReportIn.saveToFile(file2))
/*      */     {
/*  799 */       Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + file2.getAbsolutePath());
/*  800 */       System.exit(-1);
/*      */     }
/*      */     else
/*      */     {
/*  804 */       Bootstrap.printToSYSOUT("#@?@# Game crashed! Crash report could not be saved. #@?@#");
/*  805 */       System.exit(-2);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isUnicode()
/*      */   {
/*  811 */     return (this.mcLanguageManager.isCurrentLocaleUnicode()) || (this.gameSettings.forceUnicodeFont);
/*      */   }
/*      */   
/*      */   public void refreshResources()
/*      */   {
/*  816 */     List<IResourcePack> list = Lists.newArrayList(this.defaultResourcePacks);
/*      */     
/*  818 */     for (ResourcePackRepository.Entry resourcepackrepository$entry : this.mcResourcePackRepository.getRepositoryEntries())
/*      */     {
/*  820 */       list.add(resourcepackrepository$entry.getResourcePack());
/*      */     }
/*      */     
/*  823 */     if (this.mcResourcePackRepository.getResourcePackInstance() != null)
/*      */     {
/*  825 */       list.add(this.mcResourcePackRepository.getResourcePackInstance());
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  830 */       this.mcResourceManager.reloadResources(list);
/*      */     }
/*      */     catch (RuntimeException runtimeexception)
/*      */     {
/*  834 */       logger.info("Caught error stitching, removing all assigned resourcepacks", runtimeexception);
/*  835 */       list.clear();
/*  836 */       list.addAll(this.defaultResourcePacks);
/*  837 */       this.mcResourcePackRepository.setRepositories(Collections.emptyList());
/*  838 */       this.mcResourceManager.reloadResources(list);
/*  839 */       this.gameSettings.resourcePacks.clear();
/*  840 */       this.gameSettings.field_183018_l.clear();
/*  841 */       this.gameSettings.saveOptions();
/*      */     }
/*      */     
/*  844 */     this.mcLanguageManager.parseLanguageMetadata(list);
/*      */     
/*  846 */     if (this.renderGlobal != null)
/*      */     {
/*  848 */       this.renderGlobal.loadRenderers();
/*      */     }
/*      */   }
/*      */   
/*      */   private ByteBuffer readImageToBuffer(InputStream imageStream) throws IOException
/*      */   {
/*  854 */     BufferedImage bufferedimage = ImageIO.read(imageStream);
/*  855 */     int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0, bufferedimage.getWidth());
/*  856 */     ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
/*      */     int[] arrayOfInt1;
/*  858 */     int j = (arrayOfInt1 = aint).length; for (int i = 0; i < j; i++) { int i = arrayOfInt1[i];
/*      */       
/*  860 */       bytebuffer.putInt(i << 8 | i >> 24 & 0xFF);
/*      */     }
/*      */     
/*  863 */     bytebuffer.flip();
/*  864 */     return bytebuffer;
/*      */   }
/*      */   
/*      */   private void updateDisplayMode() throws LWJGLException
/*      */   {
/*  869 */     Set<DisplayMode> set = Sets.newHashSet();
/*  870 */     Collections.addAll(set, Display.getAvailableDisplayModes());
/*  871 */     DisplayMode displaymode = Display.getDesktopDisplayMode();
/*      */     
/*  873 */     if ((!set.contains(displaymode)) && (Util.getOSType() == Util.EnumOS.OSX))
/*      */     {
/*      */ 
/*      */ 
/*  877 */       for (DisplayMode displaymode1 : macDisplayModes)
/*      */       {
/*  879 */         boolean flag = true;
/*      */         
/*  881 */         for (DisplayMode displaymode2 : set)
/*      */         {
/*  883 */           if ((displaymode2.getBitsPerPixel() == 32) && (displaymode2.getWidth() == displaymode1.getWidth()) && (displaymode2.getHeight() == displaymode1.getHeight()))
/*      */           {
/*  885 */             flag = false;
/*  886 */             break;
/*      */           }
/*      */         }
/*      */         
/*  890 */         if (!flag)
/*      */         {
/*  892 */           Iterator iterator = set.iterator();
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  897 */           while (iterator.hasNext())
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*  902 */             DisplayMode displaymode3 = (DisplayMode)iterator.next();
/*      */             
/*  904 */             if ((displaymode3.getBitsPerPixel() == 32) && (displaymode3.getWidth() == displaymode1.getWidth() / 2) && (displaymode3.getHeight() == displaymode1.getHeight() / 2))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  910 */               displaymode = displaymode3; }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  915 */     Display.setDisplayMode(displaymode);
/*  916 */     this.displayWidth = displaymode.getWidth();
/*  917 */     this.displayHeight = displaymode.getHeight();
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private void drawSplashScreen(TextureManager textureManagerInstance)
/*      */     throws LWJGLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: new 1460	net/minecraft/client/gui/ScaledResolution
/*      */     //   3: dup
/*      */     //   4: aload_0
/*      */     //   5: invokestatic 1272	net/minecraft/client/Minecraft:getMinecraft	()Lnet/minecraft/client/Minecraft;
/*      */     //   8: getfield 551	net/minecraft/client/Minecraft:displayWidth	I
/*      */     //   11: invokestatic 1272	net/minecraft/client/Minecraft:getMinecraft	()Lnet/minecraft/client/Minecraft;
/*      */     //   14: getfield 556	net/minecraft/client/Minecraft:displayHeight	I
/*      */     //   17: invokespecial 1463	net/minecraft/client/gui/ScaledResolution:<init>	(Lnet/minecraft/client/Minecraft;II)V
/*      */     //   20: astore_2
/*      */     //   21: aload_2
/*      */     //   22: invokevirtual 1466	net/minecraft/client/gui/ScaledResolution:getScaleFactor	()I
/*      */     //   25: istore_3
/*      */     //   26: new 733	net/minecraft/client/shader/Framebuffer
/*      */     //   29: dup
/*      */     //   30: invokestatic 1469	net/minecraft/client/gui/ScaledResolution:getScaledWidth	()I
/*      */     //   33: iload_3
/*      */     //   34: imul
/*      */     //   35: invokestatic 1472	net/minecraft/client/gui/ScaledResolution:getScaledHeight	()I
/*      */     //   38: iload_3
/*      */     //   39: imul
/*      */     //   40: iconst_1
/*      */     //   41: invokespecial 736	net/minecraft/client/shader/Framebuffer:<init>	(IIZ)V
/*      */     //   44: astore 4
/*      */     //   46: aload 4
/*      */     //   48: iconst_0
/*      */     //   49: invokevirtual 1475	net/minecraft/client/shader/Framebuffer:bindFramebuffer	(Z)V
/*      */     //   52: sipush 5889
/*      */     //   55: invokestatic 910	net/minecraft/client/renderer/GlStateManager:matrixMode	(I)V
/*      */     //   58: invokestatic 913	net/minecraft/client/renderer/GlStateManager:loadIdentity	()V
/*      */     //   61: dconst_0
/*      */     //   62: invokestatic 1469	net/minecraft/client/gui/ScaledResolution:getScaledWidth	()I
/*      */     //   65: i2d
/*      */     //   66: invokestatic 1472	net/minecraft/client/gui/ScaledResolution:getScaledHeight	()I
/*      */     //   69: i2d
/*      */     //   70: dconst_0
/*      */     //   71: ldc2_w 1476
/*      */     //   74: ldc2_w 1478
/*      */     //   77: invokestatic 1483	net/minecraft/client/renderer/GlStateManager:ortho	(DDDDDD)V
/*      */     //   80: sipush 5888
/*      */     //   83: invokestatic 910	net/minecraft/client/renderer/GlStateManager:matrixMode	(I)V
/*      */     //   86: invokestatic 913	net/minecraft/client/renderer/GlStateManager:loadIdentity	()V
/*      */     //   89: fconst_0
/*      */     //   90: fconst_0
/*      */     //   91: ldc_w 1484
/*      */     //   94: invokestatic 1488	net/minecraft/client/renderer/GlStateManager:translate	(FFF)V
/*      */     //   97: invokestatic 1491	net/minecraft/client/renderer/GlStateManager:disableLighting	()V
/*      */     //   100: invokestatic 1494	net/minecraft/client/renderer/GlStateManager:disableFog	()V
/*      */     //   103: invokestatic 1497	net/minecraft/client/renderer/GlStateManager:disableDepth	()V
/*      */     //   106: invokestatic 882	net/minecraft/client/renderer/GlStateManager:enableTexture2D	()V
/*      */     //   109: aconst_null
/*      */     //   110: astore 5
/*      */     //   112: aload_0
/*      */     //   113: getfield 480	net/minecraft/client/Minecraft:mcDefaultResourcePack	Lnet/minecraft/client/resources/DefaultResourcePack;
/*      */     //   116: getstatic 289	net/minecraft/client/Minecraft:locationMojangPng	Lnet/minecraft/util/ResourceLocation;
/*      */     //   119: invokevirtual 1500	net/minecraft/client/resources/DefaultResourcePack:getInputStream	(Lnet/minecraft/util/ResourceLocation;)Ljava/io/InputStream;
/*      */     //   122: astore 5
/*      */     //   124: aload_0
/*      */     //   125: aload_1
/*      */     //   126: ldc_w 1502
/*      */     //   129: new 1504	net/minecraft/client/renderer/texture/DynamicTexture
/*      */     //   132: dup
/*      */     //   133: aload 5
/*      */     //   135: invokestatic 1399	javax/imageio/ImageIO:read	(Ljava/io/InputStream;)Ljava/awt/image/BufferedImage;
/*      */     //   138: invokespecial 1507	net/minecraft/client/renderer/texture/DynamicTexture:<init>	(Ljava/awt/image/BufferedImage;)V
/*      */     //   141: invokevirtual 1511	net/minecraft/client/renderer/texture/TextureManager:getDynamicTextureLocation	(Ljava/lang/String;Lnet/minecraft/client/renderer/texture/DynamicTexture;)Lnet/minecraft/util/ResourceLocation;
/*      */     //   144: putfield 1027	net/minecraft/client/Minecraft:mojangLogo	Lnet/minecraft/util/ResourceLocation;
/*      */     //   147: aload_1
/*      */     //   148: aload_0
/*      */     //   149: getfield 1027	net/minecraft/client/Minecraft:mojangLogo	Lnet/minecraft/util/ResourceLocation;
/*      */     //   152: invokevirtual 939	net/minecraft/client/renderer/texture/TextureManager:bindTexture	(Lnet/minecraft/util/ResourceLocation;)V
/*      */     //   155: goto +52 -> 207
/*      */     //   158: astore 6
/*      */     //   160: getstatic 279	net/minecraft/client/Minecraft:logger	Lorg/apache/logging/log4j/Logger;
/*      */     //   163: new 514	java/lang/StringBuilder
/*      */     //   166: dup
/*      */     //   167: ldc_w 1513
/*      */     //   170: invokespecial 517	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   173: getstatic 289	net/minecraft/client/Minecraft:locationMojangPng	Lnet/minecraft/util/ResourceLocation;
/*      */     //   176: invokevirtual 1305	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */     //   179: invokevirtual 527	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   182: aload 6
/*      */     //   184: invokeinterface 1156 3 0
/*      */     //   189: aload 5
/*      */     //   191: invokestatic 1223	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*      */     //   194: goto +18 -> 212
/*      */     //   197: astore 7
/*      */     //   199: aload 5
/*      */     //   201: invokestatic 1223	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*      */     //   204: aload 7
/*      */     //   206: athrow
/*      */     //   207: aload 5
/*      */     //   209: invokestatic 1223	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*      */     //   212: invokestatic 1519	net/minecraft/client/renderer/Tessellator:getInstance	()Lnet/minecraft/client/renderer/Tessellator;
/*      */     //   215: astore 6
/*      */     //   217: aload 6
/*      */     //   219: invokevirtual 1523	net/minecraft/client/renderer/Tessellator:getWorldRenderer	()Lnet/minecraft/client/renderer/WorldRenderer;
/*      */     //   222: astore 7
/*      */     //   224: aload 7
/*      */     //   226: bipush 7
/*      */     //   228: getstatic 1529	net/minecraft/client/renderer/vertex/DefaultVertexFormats:POSITION_TEX_COLOR	Lnet/minecraft/client/renderer/vertex/VertexFormat;
/*      */     //   231: invokevirtual 1535	net/minecraft/client/renderer/WorldRenderer:begin	(ILnet/minecraft/client/renderer/vertex/VertexFormat;)V
/*      */     //   234: aload 7
/*      */     //   236: dconst_0
/*      */     //   237: aload_0
/*      */     //   238: getfield 556	net/minecraft/client/Minecraft:displayHeight	I
/*      */     //   241: i2d
/*      */     //   242: dconst_0
/*      */     //   243: invokevirtual 1539	net/minecraft/client/renderer/WorldRenderer:pos	(DDD)Lnet/minecraft/client/renderer/WorldRenderer;
/*      */     //   246: dconst_0
/*      */     //   247: dconst_0
/*      */     //   248: invokevirtual 1543	net/minecraft/client/renderer/WorldRenderer:tex	(DD)Lnet/minecraft/client/renderer/WorldRenderer;
/*      */     //   251: sipush 255
/*      */     //   254: sipush 255
/*      */     //   257: sipush 255
/*      */     //   260: sipush 255
/*      */     //   263: invokevirtual 1547	net/minecraft/client/renderer/WorldRenderer:color	(IIII)Lnet/minecraft/client/renderer/WorldRenderer;
/*      */     //   266: invokevirtual 1550	net/minecraft/client/renderer/WorldRenderer:endVertex	()V
/*      */     //   269: aload 7
/*      */     //   271: aload_0
/*      */     //   272: getfield 551	net/minecraft/client/Minecraft:displayWidth	I
/*      */     //   275: i2d
/*      */     //   276: aload_0
/*      */     //   277: getfield 556	net/minecraft/client/Minecraft:displayHeight	I
/*      */     //   280: i2d
/*      */     //   281: dconst_0
/*      */     //   282: invokevirtual 1539	net/minecraft/client/renderer/WorldRenderer:pos	(DDD)Lnet/minecraft/client/renderer/WorldRenderer;
/*      */     //   285: dconst_0
/*      */     //   286: dconst_0
/*      */     //   287: invokevirtual 1543	net/minecraft/client/renderer/WorldRenderer:tex	(DD)Lnet/minecraft/client/renderer/WorldRenderer;
/*      */     //   290: sipush 255
/*      */     //   293: sipush 255
/*      */     //   296: sipush 255
/*      */     //   299: sipush 255
/*      */     //   302: invokevirtual 1547	net/minecraft/client/renderer/WorldRenderer:color	(IIII)Lnet/minecraft/client/renderer/WorldRenderer;
/*      */     //   305: invokevirtual 1550	net/minecraft/client/renderer/WorldRenderer:endVertex	()V
/*      */     //   308: aload 7
/*      */     //   310: aload_0
/*      */     //   311: getfield 551	net/minecraft/client/Minecraft:displayWidth	I
/*      */     //   314: i2d
/*      */     //   315: dconst_0
/*      */     //   316: dconst_0
/*      */     //   317: invokevirtual 1539	net/minecraft/client/renderer/WorldRenderer:pos	(DDD)Lnet/minecraft/client/renderer/WorldRenderer;
/*      */     //   320: dconst_0
/*      */     //   321: dconst_0
/*      */     //   322: invokevirtual 1543	net/minecraft/client/renderer/WorldRenderer:tex	(DD)Lnet/minecraft/client/renderer/WorldRenderer;
/*      */     //   325: sipush 255
/*      */     //   328: sipush 255
/*      */     //   331: sipush 255
/*      */     //   334: sipush 255
/*      */     //   337: invokevirtual 1547	net/minecraft/client/renderer/WorldRenderer:color	(IIII)Lnet/minecraft/client/renderer/WorldRenderer;
/*      */     //   340: invokevirtual 1550	net/minecraft/client/renderer/WorldRenderer:endVertex	()V
/*      */     //   343: aload 7
/*      */     //   345: dconst_0
/*      */     //   346: dconst_0
/*      */     //   347: dconst_0
/*      */     //   348: invokevirtual 1539	net/minecraft/client/renderer/WorldRenderer:pos	(DDD)Lnet/minecraft/client/renderer/WorldRenderer;
/*      */     //   351: dconst_0
/*      */     //   352: dconst_0
/*      */     //   353: invokevirtual 1543	net/minecraft/client/renderer/WorldRenderer:tex	(DD)Lnet/minecraft/client/renderer/WorldRenderer;
/*      */     //   356: sipush 255
/*      */     //   359: sipush 255
/*      */     //   362: sipush 255
/*      */     //   365: sipush 255
/*      */     //   368: invokevirtual 1547	net/minecraft/client/renderer/WorldRenderer:color	(IIII)Lnet/minecraft/client/renderer/WorldRenderer;
/*      */     //   371: invokevirtual 1550	net/minecraft/client/renderer/WorldRenderer:endVertex	()V
/*      */     //   374: aload 6
/*      */     //   376: invokevirtual 1553	net/minecraft/client/renderer/Tessellator:draw	()V
/*      */     //   379: fconst_1
/*      */     //   380: fconst_1
/*      */     //   381: fconst_1
/*      */     //   382: fconst_1
/*      */     //   383: invokestatic 1555	net/minecraft/client/renderer/GlStateManager:color	(FFFF)V
/*      */     //   386: sipush 256
/*      */     //   389: istore 8
/*      */     //   391: sipush 256
/*      */     //   394: istore 9
/*      */     //   396: aload_0
/*      */     //   397: invokestatic 1469	net/minecraft/client/gui/ScaledResolution:getScaledWidth	()I
/*      */     //   400: iload 8
/*      */     //   402: isub
/*      */     //   403: iconst_2
/*      */     //   404: idiv
/*      */     //   405: invokestatic 1472	net/minecraft/client/gui/ScaledResolution:getScaledHeight	()I
/*      */     //   408: iload 9
/*      */     //   410: isub
/*      */     //   411: iconst_2
/*      */     //   412: idiv
/*      */     //   413: iconst_0
/*      */     //   414: iconst_0
/*      */     //   415: iload 8
/*      */     //   417: iload 9
/*      */     //   419: sipush 255
/*      */     //   422: sipush 255
/*      */     //   425: sipush 255
/*      */     //   428: sipush 255
/*      */     //   431: invokevirtual 1559	net/minecraft/client/Minecraft:func_181536_a	(IIIIIIIIII)V
/*      */     //   434: invokestatic 1491	net/minecraft/client/renderer/GlStateManager:disableLighting	()V
/*      */     //   437: invokestatic 1494	net/minecraft/client/renderer/GlStateManager:disableFog	()V
/*      */     //   440: aload 4
/*      */     //   442: invokevirtual 1562	net/minecraft/client/shader/Framebuffer:unbindFramebuffer	()V
/*      */     //   445: aload 4
/*      */     //   447: invokestatic 1469	net/minecraft/client/gui/ScaledResolution:getScaledWidth	()I
/*      */     //   450: iload_3
/*      */     //   451: imul
/*      */     //   452: invokestatic 1472	net/minecraft/client/gui/ScaledResolution:getScaledHeight	()I
/*      */     //   455: iload_3
/*      */     //   456: imul
/*      */     //   457: invokevirtual 1565	net/minecraft/client/shader/Framebuffer:framebufferRender	(II)V
/*      */     //   460: invokestatic 899	net/minecraft/client/renderer/GlStateManager:enableAlpha	()V
/*      */     //   463: sipush 516
/*      */     //   466: ldc_w 900
/*      */     //   469: invokestatic 904	net/minecraft/client/renderer/GlStateManager:alphaFunc	(IF)V
/*      */     //   472: aload_0
/*      */     //   473: invokevirtual 1568	net/minecraft/client/Minecraft:updateDisplay	()V
/*      */     //   476: return
/*      */     // Line number table:
/*      */     //   Java source line #922	-> byte code offset #0
/*      */     //   Java source line #923	-> byte code offset #21
/*      */     //   Java source line #924	-> byte code offset #26
/*      */     //   Java source line #925	-> byte code offset #46
/*      */     //   Java source line #926	-> byte code offset #52
/*      */     //   Java source line #927	-> byte code offset #58
/*      */     //   Java source line #928	-> byte code offset #61
/*      */     //   Java source line #929	-> byte code offset #80
/*      */     //   Java source line #930	-> byte code offset #86
/*      */     //   Java source line #931	-> byte code offset #89
/*      */     //   Java source line #932	-> byte code offset #97
/*      */     //   Java source line #933	-> byte code offset #100
/*      */     //   Java source line #934	-> byte code offset #103
/*      */     //   Java source line #935	-> byte code offset #106
/*      */     //   Java source line #936	-> byte code offset #109
/*      */     //   Java source line #940	-> byte code offset #112
/*      */     //   Java source line #941	-> byte code offset #124
/*      */     //   Java source line #942	-> byte code offset #147
/*      */     //   Java source line #943	-> byte code offset #155
/*      */     //   Java source line #944	-> byte code offset #158
/*      */     //   Java source line #946	-> byte code offset #160
/*      */     //   Java source line #950	-> byte code offset #189
/*      */     //   Java source line #949	-> byte code offset #197
/*      */     //   Java source line #950	-> byte code offset #199
/*      */     //   Java source line #951	-> byte code offset #204
/*      */     //   Java source line #950	-> byte code offset #207
/*      */     //   Java source line #953	-> byte code offset #212
/*      */     //   Java source line #954	-> byte code offset #217
/*      */     //   Java source line #955	-> byte code offset #224
/*      */     //   Java source line #956	-> byte code offset #234
/*      */     //   Java source line #957	-> byte code offset #269
/*      */     //   Java source line #958	-> byte code offset #308
/*      */     //   Java source line #959	-> byte code offset #343
/*      */     //   Java source line #960	-> byte code offset #374
/*      */     //   Java source line #961	-> byte code offset #379
/*      */     //   Java source line #962	-> byte code offset #386
/*      */     //   Java source line #963	-> byte code offset #391
/*      */     //   Java source line #964	-> byte code offset #396
/*      */     //   Java source line #965	-> byte code offset #434
/*      */     //   Java source line #966	-> byte code offset #437
/*      */     //   Java source line #967	-> byte code offset #440
/*      */     //   Java source line #968	-> byte code offset #445
/*      */     //   Java source line #969	-> byte code offset #460
/*      */     //   Java source line #970	-> byte code offset #463
/*      */     //   Java source line #971	-> byte code offset #472
/*      */     //   Java source line #972	-> byte code offset #476
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	477	0	this	Minecraft
/*      */     //   0	477	1	textureManagerInstance	TextureManager
/*      */     //   20	2	2	scaledresolution	ScaledResolution
/*      */     //   25	431	3	i	int
/*      */     //   44	402	4	framebuffer	Framebuffer
/*      */     //   110	98	5	inputstream	InputStream
/*      */     //   158	25	6	ioexception	IOException
/*      */     //   215	160	6	tessellator	Tessellator
/*      */     //   197	8	7	localObject	Object
/*      */     //   222	122	7	worldrenderer	WorldRenderer
/*      */     //   389	27	8	j	int
/*      */     //   394	24	9	k	int
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   112	155	158	java/io/IOException
/*      */     //   112	189	197	finally
/*      */   }
/*      */   
/*      */   public void func_181536_a(int p_181536_1_, int p_181536_2_, int p_181536_3_, int p_181536_4_, int p_181536_5_, int p_181536_6_, int p_181536_7_, int p_181536_8_, int p_181536_9_, int p_181536_10_)
/*      */   {
/*  976 */     float f = 0.00390625F;
/*  977 */     float f1 = 0.00390625F;
/*  978 */     WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();
/*  979 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/*  980 */     worldrenderer.pos(p_181536_1_, p_181536_2_ + p_181536_6_, 0.0D).tex(p_181536_3_ * f, (p_181536_4_ + p_181536_6_) * f1).color(p_181536_7_, p_181536_8_, p_181536_9_, p_181536_10_).endVertex();
/*  981 */     worldrenderer.pos(p_181536_1_ + p_181536_5_, p_181536_2_ + p_181536_6_, 0.0D).tex((p_181536_3_ + p_181536_5_) * f, (p_181536_4_ + p_181536_6_) * f1).color(p_181536_7_, p_181536_8_, p_181536_9_, p_181536_10_).endVertex();
/*  982 */     worldrenderer.pos(p_181536_1_ + p_181536_5_, p_181536_2_, 0.0D).tex((p_181536_3_ + p_181536_5_) * f, p_181536_4_ * f1).color(p_181536_7_, p_181536_8_, p_181536_9_, p_181536_10_).endVertex();
/*  983 */     worldrenderer.pos(p_181536_1_, p_181536_2_, 0.0D).tex(p_181536_3_ * f, p_181536_4_ * f1).color(p_181536_7_, p_181536_8_, p_181536_9_, p_181536_10_).endVertex();
/*  984 */     Tessellator.getInstance().draw();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ISaveFormat getSaveLoader()
/*      */   {
/*  992 */     return this.saveLoader;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void displayGuiScreen(GuiScreen guiScreenIn)
/*      */   {
/* 1000 */     if (this.currentScreen != null)
/*      */     {
/* 1002 */       this.currentScreen.onGuiClosed();
/*      */     }
/*      */     
/* 1005 */     if ((guiScreenIn == null) && (this.theWorld == null))
/*      */     {
/* 1007 */       guiScreenIn = new GuiMainMenu();
/*      */     }
/* 1009 */     else if ((guiScreenIn == null) && (this.thePlayer.getHealth() <= 0.0F))
/*      */     {
/* 1011 */       guiScreenIn = new GuiGameOver();
/*      */     }
/*      */     
/* 1014 */     if ((guiScreenIn instanceof GuiMainMenu))
/*      */     {
/* 1016 */       this.gameSettings.showDebugInfo = false;
/* 1017 */       this.ingameGUI.getChatGUI().clearChatMessages();
/*      */     }
/*      */     
/* 1020 */     this.currentScreen = guiScreenIn;
/*      */     
/* 1022 */     if (guiScreenIn != null)
/*      */     {
/* 1024 */       setIngameNotInFocus();
/* 1025 */       ScaledResolution scaledresolution = new ScaledResolution(this, getMinecraft().displayWidth, getMinecraft().displayHeight);
/* 1026 */       int i = ScaledResolution.getScaledWidth();
/* 1027 */       int j = ScaledResolution.getScaledHeight();
/* 1028 */       guiScreenIn.setWorldAndResolution(this, i, j);
/* 1029 */       this.skipRenderWorld = false;
/*      */     }
/*      */     else
/*      */     {
/* 1033 */       this.mcSoundHandler.resumeSounds();
/* 1034 */       setIngameFocus();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void checkGLError(String message)
/*      */   {
/* 1043 */     if (this.enableGLErrorChecking)
/*      */     {
/* 1045 */       int i = GL11.glGetError();
/*      */       
/* 1047 */       if (i != 0)
/*      */       {
/* 1049 */         String s = GLU.gluErrorString(i);
/* 1050 */         logger.error("########## GL ERROR ##########");
/* 1051 */         logger.error("@ " + message);
/* 1052 */         logger.error(i + ": " + s);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public void shutdownMinecraftApplet()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: getstatic 1058	rip/jutting/polaris/Polaris:instance	Lrip/jutting/polaris/Polaris;
/*      */     //   3: invokevirtual 1663	rip/jutting/polaris/Polaris:stopClient	()V
/*      */     //   6: aload_0
/*      */     //   7: getfield 1118	net/minecraft/client/Minecraft:stream	Lnet/minecraft/client/stream/IStream;
/*      */     //   10: invokeinterface 1668 1 0
/*      */     //   15: getstatic 279	net/minecraft/client/Minecraft:logger	Lorg/apache/logging/log4j/Logger;
/*      */     //   18: ldc_w 1670
/*      */     //   21: invokeinterface 532 2 0
/*      */     //   26: aload_0
/*      */     //   27: aconst_null
/*      */     //   28: invokevirtual 1674	net/minecraft/client/Minecraft:loadWorld	(Lnet/minecraft/client/multiplayer/WorldClient;)V
/*      */     //   31: goto +4 -> 35
/*      */     //   34: astore_1
/*      */     //   35: aload_0
/*      */     //   36: getfield 820	net/minecraft/client/Minecraft:mcSoundHandler	Lnet/minecraft/client/audio/SoundHandler;
/*      */     //   39: invokevirtual 1677	net/minecraft/client/audio/SoundHandler:unloadSounds	()V
/*      */     //   42: goto +20 -> 62
/*      */     //   45: astore_2
/*      */     //   46: invokestatic 1680	org/lwjgl/opengl/Display:destroy	()V
/*      */     //   49: aload_0
/*      */     //   50: getfield 636	net/minecraft/client/Minecraft:hasCrashed	Z
/*      */     //   53: ifne +7 -> 60
/*      */     //   56: iconst_0
/*      */     //   57: invokestatic 1308	java/lang/System:exit	(I)V
/*      */     //   60: aload_2
/*      */     //   61: athrow
/*      */     //   62: invokestatic 1680	org/lwjgl/opengl/Display:destroy	()V
/*      */     //   65: aload_0
/*      */     //   66: getfield 636	net/minecraft/client/Minecraft:hasCrashed	Z
/*      */     //   69: ifne +7 -> 76
/*      */     //   72: iconst_0
/*      */     //   73: invokestatic 1308	java/lang/System:exit	(I)V
/*      */     //   76: invokestatic 654	java/lang/System:gc	()V
/*      */     //   79: return
/*      */     // Line number table:
/*      */     //   Java source line #1065	-> byte code offset #0
/*      */     //   Java source line #1067	-> byte code offset #6
/*      */     //   Java source line #1068	-> byte code offset #15
/*      */     //   Java source line #1072	-> byte code offset #26
/*      */     //   Java source line #1073	-> byte code offset #31
/*      */     //   Java source line #1074	-> byte code offset #34
/*      */     //   Java source line #1079	-> byte code offset #35
/*      */     //   Java source line #1080	-> byte code offset #42
/*      */     //   Java source line #1082	-> byte code offset #45
/*      */     //   Java source line #1083	-> byte code offset #46
/*      */     //   Java source line #1085	-> byte code offset #49
/*      */     //   Java source line #1087	-> byte code offset #56
/*      */     //   Java source line #1089	-> byte code offset #60
/*      */     //   Java source line #1083	-> byte code offset #62
/*      */     //   Java source line #1085	-> byte code offset #65
/*      */     //   Java source line #1087	-> byte code offset #72
/*      */     //   Java source line #1091	-> byte code offset #76
/*      */     //   Java source line #1092	-> byte code offset #79
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	80	0	this	Minecraft
/*      */     //   34	1	1	localThrowable	Throwable
/*      */     //   45	16	2	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   26	31	34	java/lang/Throwable
/*      */     //   0	45	45	finally
/*      */   }
/*      */   
/*      */   private void runGameLoop()
/*      */     throws IOException
/*      */   {
/* 1099 */     long i = System.nanoTime();
/* 1100 */     this.mcProfiler.startSection("root");
/*      */     
/* 1102 */     if ((Display.isCreated()) && (Display.isCloseRequested()))
/*      */     {
/* 1104 */       shutdown();
/*      */     }
/*      */     
/* 1107 */     if ((this.isGamePaused) && (this.theWorld != null))
/*      */     {
/* 1109 */       float f = this.timer.renderPartialTicks;
/* 1110 */       this.timer.updateTimer();
/* 1111 */       this.timer.renderPartialTicks = f;
/*      */     }
/*      */     else
/*      */     {
/* 1115 */       this.timer.updateTimer();
/*      */     }
/*      */     
/* 1118 */     this.mcProfiler.startSection("scheduledExecutables");
/*      */     
/* 1120 */     synchronized (this.scheduledTasks)
/*      */     {
/* 1122 */       while (!this.scheduledTasks.isEmpty())
/*      */       {
/* 1124 */         Util.func_181617_a((FutureTask)this.scheduledTasks.poll(), logger);
/*      */       }
/*      */     }
/*      */     
/* 1128 */     this.mcProfiler.endSection();
/* 1129 */     long l = System.nanoTime();
/* 1130 */     this.mcProfiler.startSection("tick");
/*      */     
/* 1132 */     for (int j = 0; j < this.timer.elapsedTicks; j++)
/*      */     {
/* 1134 */       runTick();
/*      */     }
/*      */     
/* 1137 */     this.mcProfiler.endStartSection("preRenderErrors");
/* 1138 */     long i1 = System.nanoTime() - l;
/* 1139 */     checkGLError("Pre render");
/* 1140 */     this.mcProfiler.endStartSection("sound");
/* 1141 */     this.mcSoundHandler.setListener(this.thePlayer, this.timer.renderPartialTicks);
/* 1142 */     this.mcProfiler.endSection();
/* 1143 */     this.mcProfiler.startSection("render");
/* 1144 */     GlStateManager.pushMatrix();
/* 1145 */     GlStateManager.clear(16640);
/* 1146 */     this.framebufferMc.bindFramebuffer(true);
/* 1147 */     this.mcProfiler.startSection("display");
/* 1148 */     GlStateManager.enableTexture2D();
/*      */     
/* 1150 */     if ((this.thePlayer != null) && (this.thePlayer.isEntityInsideOpaqueBlock()))
/*      */     {
/* 1152 */       this.gameSettings.thirdPersonView = 0;
/*      */     }
/*      */     
/* 1155 */     this.mcProfiler.endSection();
/*      */     
/* 1157 */     if (!this.skipRenderWorld)
/*      */     {
/* 1159 */       this.mcProfiler.endStartSection("gameRenderer");
/* 1160 */       this.entityRenderer.func_181560_a(this.timer.renderPartialTicks, i);
/* 1161 */       this.mcProfiler.endSection();
/*      */     }
/*      */     
/* 1164 */     this.mcProfiler.endSection();
/*      */     
/* 1166 */     if ((this.gameSettings.showDebugInfo) && (this.gameSettings.showDebugProfilerChart) && (!this.gameSettings.hideGUI))
/*      */     {
/* 1168 */       if (!this.mcProfiler.profilingEnabled)
/*      */       {
/* 1170 */         this.mcProfiler.clearProfiling();
/*      */       }
/*      */       
/* 1173 */       this.mcProfiler.profilingEnabled = true;
/* 1174 */       displayDebugInfo(i1);
/*      */     }
/*      */     else
/*      */     {
/* 1178 */       this.mcProfiler.profilingEnabled = false;
/* 1179 */       this.prevFrameTime = System.nanoTime();
/*      */     }
/*      */     
/* 1182 */     this.guiAchievement.updateAchievementWindow();
/* 1183 */     this.framebufferMc.unbindFramebuffer();
/* 1184 */     GlStateManager.popMatrix();
/* 1185 */     GlStateManager.pushMatrix();
/* 1186 */     this.framebufferMc.framebufferRender(this.displayWidth, this.displayHeight);
/* 1187 */     GlStateManager.popMatrix();
/* 1188 */     GlStateManager.pushMatrix();
/* 1189 */     this.entityRenderer.renderStreamIndicator(this.timer.renderPartialTicks);
/* 1190 */     GlStateManager.popMatrix();
/* 1191 */     this.mcProfiler.startSection("root");
/* 1192 */     updateDisplay();
/* 1193 */     Thread.yield();
/* 1194 */     this.mcProfiler.startSection("stream");
/* 1195 */     this.mcProfiler.startSection("update");
/* 1196 */     this.stream.func_152935_j();
/* 1197 */     this.mcProfiler.endStartSection("submit");
/* 1198 */     this.stream.func_152922_k();
/* 1199 */     this.mcProfiler.endSection();
/* 1200 */     this.mcProfiler.endSection();
/* 1201 */     checkGLError("Post render");
/* 1202 */     this.fpsCounter += 1;
/* 1203 */     this.isGamePaused = ((isSingleplayer()) && (this.currentScreen != null) && (this.currentScreen.doesGuiPauseGame()) && (!this.theIntegratedServer.getPublic()));
/* 1204 */     long k = System.nanoTime();
/* 1205 */     this.field_181542_y.func_181747_a(k - this.field_181543_z);
/* 1206 */     this.field_181543_z = k;
/*      */     
/* 1208 */     while (getSystemTime() >= this.debugUpdateTime + 1000L)
/*      */     {
/* 1210 */       debugFPS = this.fpsCounter;
/* 1211 */       this.debug = String.format("%d fps (%d chunk update%s) T: %s%s%s%s%s", new Object[] { Integer.valueOf(debugFPS), Integer.valueOf(RenderChunk.renderChunksUpdated), RenderChunk.renderChunksUpdated != 1 ? "s" : "", this.gameSettings.limitFramerate == GameSettings.Options.FRAMERATE_LIMIT.getValueMax() ? "inf" : Integer.valueOf(this.gameSettings.limitFramerate), this.gameSettings.enableVsync ? " vsync" : "", this.gameSettings.fancyGraphics ? "" : " fast", this.gameSettings.clouds == 1 ? " fast-clouds" : this.gameSettings.clouds == 0 ? "" : " fancy-clouds", OpenGlHelper.useVbo() ? " vbo" : "" });
/* 1212 */       RenderChunk.renderChunksUpdated = 0;
/* 1213 */       this.debugUpdateTime += 1000L;
/* 1214 */       this.fpsCounter = 0;
/* 1215 */       this.usageSnooper.addMemoryStatsToSnooper();
/*      */       
/* 1217 */       if (!this.usageSnooper.isSnooperRunning())
/*      */       {
/* 1219 */         this.usageSnooper.startSnooper();
/*      */       }
/*      */     }
/*      */     
/* 1223 */     if (isFramerateLimitBelowMax())
/*      */     {
/* 1225 */       this.mcProfiler.startSection("fpslimit_wait");
/* 1226 */       Display.sync(getLimitFramerate());
/* 1227 */       this.mcProfiler.endSection();
/*      */     }
/*      */     
/* 1230 */     this.mcProfiler.endSection();
/*      */   }
/*      */   
/*      */   public void updateDisplay()
/*      */   {
/* 1235 */     this.mcProfiler.startSection("display_update");
/* 1236 */     Display.update();
/* 1237 */     this.mcProfiler.endSection();
/* 1238 */     checkWindowResize();
/*      */   }
/*      */   
/*      */   protected void checkWindowResize()
/*      */   {
/* 1243 */     if ((!this.fullscreen) && (Display.wasResized()))
/*      */     {
/* 1245 */       int i = this.displayWidth;
/* 1246 */       int j = this.displayHeight;
/* 1247 */       this.displayWidth = Display.getWidth();
/* 1248 */       this.displayHeight = Display.getHeight();
/*      */       
/* 1250 */       if ((this.displayWidth != i) || (this.displayHeight != j))
/*      */       {
/* 1252 */         if (this.displayWidth <= 0)
/*      */         {
/* 1254 */           this.displayWidth = 1;
/*      */         }
/*      */         
/* 1257 */         if (this.displayHeight <= 0)
/*      */         {
/* 1259 */           this.displayHeight = 1;
/*      */         }
/*      */         
/* 1262 */         resize(this.displayWidth, this.displayHeight);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public int getLimitFramerate()
/*      */   {
/* 1269 */     return (this.theWorld == null) && (this.currentScreen != null) ? 30 : this.gameSettings.limitFramerate;
/*      */   }
/*      */   
/*      */   public boolean isFramerateLimitBelowMax()
/*      */   {
/* 1274 */     return getLimitFramerate() < GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
/*      */   }
/*      */   
/*      */   public void freeMemory()
/*      */   {
/*      */     try
/*      */     {
/* 1281 */       memoryReserve = new byte[0];
/* 1282 */       this.renderGlobal.deleteAllDisplayLists();
/*      */     }
/*      */     catch (Throwable localThrowable) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 1291 */       System.gc();
/* 1292 */       loadWorld(null);
/*      */     }
/*      */     catch (Throwable localThrowable1) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1299 */     System.gc();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateDebugProfilerName(int keyCount)
/*      */   {
/* 1307 */     List<Profiler.Result> list = this.mcProfiler.getProfilingData(this.debugProfilerName);
/*      */     
/* 1309 */     if ((list != null) && (!list.isEmpty()))
/*      */     {
/* 1311 */       Profiler.Result profiler$result = (Profiler.Result)list.remove(0);
/*      */       
/* 1313 */       if (keyCount == 0)
/*      */       {
/* 1315 */         if (profiler$result.field_76331_c.length() > 0)
/*      */         {
/* 1317 */           int i = this.debugProfilerName.lastIndexOf(".");
/*      */           
/* 1319 */           if (i >= 0)
/*      */           {
/* 1321 */             this.debugProfilerName = this.debugProfilerName.substring(0, i);
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1327 */         keyCount--;
/*      */         
/* 1329 */         if ((keyCount < list.size()) && (!((Profiler.Result)list.get(keyCount)).field_76331_c.equals("unspecified")))
/*      */         {
/* 1331 */           if (this.debugProfilerName.length() > 0)
/*      */           {
/* 1333 */             this.debugProfilerName += ".";
/*      */           }
/*      */           
/* 1336 */           this.debugProfilerName += ((Profiler.Result)list.get(keyCount)).field_76331_c;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void displayDebugInfo(long elapsedTicksTime)
/*      */   {
/* 1347 */     if (this.mcProfiler.profilingEnabled)
/*      */     {
/* 1349 */       List<Profiler.Result> list = this.mcProfiler.getProfilingData(this.debugProfilerName);
/* 1350 */       Profiler.Result profiler$result = (Profiler.Result)list.remove(0);
/* 1351 */       GlStateManager.clear(256);
/* 1352 */       GlStateManager.matrixMode(5889);
/* 1353 */       GlStateManager.enableColorMaterial();
/* 1354 */       GlStateManager.loadIdentity();
/* 1355 */       GlStateManager.ortho(0.0D, this.displayWidth, this.displayHeight, 0.0D, 1000.0D, 3000.0D);
/* 1356 */       GlStateManager.matrixMode(5888);
/* 1357 */       GlStateManager.loadIdentity();
/* 1358 */       GlStateManager.translate(0.0F, 0.0F, -2000.0F);
/* 1359 */       GL11.glLineWidth(1.0F);
/* 1360 */       GlStateManager.disableTexture2D();
/* 1361 */       Tessellator tessellator = Tessellator.getInstance();
/* 1362 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 1363 */       int i = 160;
/* 1364 */       int j = this.displayWidth - i - 10;
/* 1365 */       int k = this.displayHeight - i * 2;
/* 1366 */       GlStateManager.enableBlend();
/* 1367 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 1368 */       worldrenderer.pos(j - i * 1.1F, k - i * 0.6F - 16.0F, 0.0D).color(200, 0, 0, 0).endVertex();
/* 1369 */       worldrenderer.pos(j - i * 1.1F, k + i * 2, 0.0D).color(200, 0, 0, 0).endVertex();
/* 1370 */       worldrenderer.pos(j + i * 1.1F, k + i * 2, 0.0D).color(200, 0, 0, 0).endVertex();
/* 1371 */       worldrenderer.pos(j + i * 1.1F, k - i * 0.6F - 16.0F, 0.0D).color(200, 0, 0, 0).endVertex();
/* 1372 */       tessellator.draw();
/* 1373 */       GlStateManager.disableBlend();
/* 1374 */       double d0 = 0.0D;
/*      */       
/* 1376 */       for (int l = 0; l < list.size(); l++)
/*      */       {
/* 1378 */         Profiler.Result profiler$result1 = (Profiler.Result)list.get(l);
/* 1379 */         int i1 = MathHelper.floor_double(profiler$result1.field_76332_a / 4.0D) + 1;
/* 1380 */         worldrenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
/* 1381 */         int j1 = profiler$result1.func_76329_a();
/* 1382 */         int k1 = j1 >> 16 & 0xFF;
/* 1383 */         int l1 = j1 >> 8 & 0xFF;
/* 1384 */         int i2 = j1 & 0xFF;
/* 1385 */         worldrenderer.pos(j, k, 0.0D).color(k1, l1, i2, 255).endVertex();
/*      */         
/* 1387 */         for (int j2 = i1; j2 >= 0; j2--)
/*      */         {
/* 1389 */           float f = (float)((d0 + profiler$result1.field_76332_a * j2 / i1) * 3.141592653589793D * 2.0D / 100.0D);
/* 1390 */           float f1 = MathHelper.sin(f) * i;
/* 1391 */           float f2 = MathHelper.cos(f) * i * 0.5F;
/* 1392 */           worldrenderer.pos(j + f1, k - f2, 0.0D).color(k1, l1, i2, 255).endVertex();
/*      */         }
/*      */         
/* 1395 */         tessellator.draw();
/* 1396 */         worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);
/*      */         
/* 1398 */         for (int i3 = i1; i3 >= 0; i3--)
/*      */         {
/* 1400 */           float f3 = (float)((d0 + profiler$result1.field_76332_a * i3 / i1) * 3.141592653589793D * 2.0D / 100.0D);
/* 1401 */           float f4 = MathHelper.sin(f3) * i;
/* 1402 */           float f5 = MathHelper.cos(f3) * i * 0.5F;
/* 1403 */           worldrenderer.pos(j + f4, k - f5, 0.0D).color(k1 >> 1, l1 >> 1, i2 >> 1, 255).endVertex();
/* 1404 */           worldrenderer.pos(j + f4, k - f5 + 10.0F, 0.0D).color(k1 >> 1, l1 >> 1, i2 >> 1, 255).endVertex();
/*      */         }
/*      */         
/* 1407 */         tessellator.draw();
/* 1408 */         d0 += profiler$result1.field_76332_a;
/*      */       }
/*      */       
/* 1411 */       DecimalFormat decimalformat = new DecimalFormat("##0.00");
/* 1412 */       GlStateManager.enableTexture2D();
/* 1413 */       String s = "";
/*      */       
/* 1415 */       if (!profiler$result.field_76331_c.equals("unspecified"))
/*      */       {
/* 1417 */         s = s + "[0] ";
/*      */       }
/*      */       
/* 1420 */       if (profiler$result.field_76331_c.length() == 0)
/*      */       {
/* 1422 */         s = s + "ROOT ";
/*      */       }
/*      */       else
/*      */       {
/* 1426 */         s = s + profiler$result.field_76331_c + " ";
/*      */       }
/*      */       
/* 1429 */       int l2 = 16777215;
/* 1430 */       this.fontRendererObj.drawStringWithShadow(s, j - i, k - i / 2 - 16, l2);
/* 1431 */       this.fontRendererObj.drawStringWithShadow(s = decimalformat.format(profiler$result.field_76330_b) + "%", j + i - this.fontRendererObj.getStringWidth(s), k - i / 2 - 16, l2);
/*      */       
/* 1433 */       for (int k2 = 0; k2 < list.size(); k2++)
/*      */       {
/* 1435 */         Profiler.Result profiler$result2 = (Profiler.Result)list.get(k2);
/* 1436 */         String s1 = "";
/*      */         
/* 1438 */         if (profiler$result2.field_76331_c.equals("unspecified"))
/*      */         {
/* 1440 */           s1 = s1 + "[?] ";
/*      */         }
/*      */         else
/*      */         {
/* 1444 */           s1 = s1 + "[" + (k2 + 1) + "] ";
/*      */         }
/*      */         
/* 1447 */         s1 = s1 + profiler$result2.field_76331_c;
/* 1448 */         this.fontRendererObj.drawStringWithShadow(s1, j - i, k + i / 2 + k2 * 8 + 20, profiler$result2.func_76329_a());
/* 1449 */         this.fontRendererObj.drawStringWithShadow(s1 = decimalformat.format(profiler$result2.field_76332_a) + "%", j + i - 50 - this.fontRendererObj.getStringWidth(s1), k + i / 2 + k2 * 8 + 20, profiler$result2.func_76329_a());
/* 1450 */         this.fontRendererObj.drawStringWithShadow(s1 = decimalformat.format(profiler$result2.field_76330_b) + "%", j + i - this.fontRendererObj.getStringWidth(s1), k + i / 2 + k2 * 8 + 20, profiler$result2.func_76329_a());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void shutdown()
/*      */   {
/* 1460 */     this.running = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setIngameFocus()
/*      */   {
/* 1469 */     if (Display.isActive())
/*      */     {
/* 1471 */       if (!this.inGameHasFocus)
/*      */       {
/* 1473 */         this.inGameHasFocus = true;
/* 1474 */         this.mouseHelper.grabMouseCursor();
/* 1475 */         displayGuiScreen(null);
/* 1476 */         this.leftClickCounter = 10000;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setIngameNotInFocus()
/*      */   {
/* 1486 */     if (this.inGameHasFocus)
/*      */     {
/* 1488 */       KeyBinding.unPressAllKeys();
/* 1489 */       this.inGameHasFocus = false;
/* 1490 */       this.mouseHelper.ungrabMouseCursor();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void displayInGameMenu()
/*      */   {
/* 1499 */     if (this.currentScreen == null)
/*      */     {
/* 1501 */       displayGuiScreen(new net.minecraft.client.gui.GuiIngameMenu());
/*      */       
/* 1503 */       if ((isSingleplayer()) && (!this.theIntegratedServer.getPublic()))
/*      */       {
/* 1505 */         this.mcSoundHandler.pauseSounds();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void sendClickBlockToController(boolean leftClick)
/*      */   {
/* 1512 */     if (!leftClick)
/*      */     {
/* 1514 */       this.leftClickCounter = 0;
/*      */     }
/*      */     
/* 1517 */     if ((this.leftClickCounter <= 0) && (!this.thePlayer.isUsingItem()))
/*      */     {
/* 1519 */       if ((leftClick) && (this.objectMouseOver != null) && (this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK))
/*      */       {
/* 1521 */         BlockPos blockpos = this.objectMouseOver.getBlockPos();
/*      */         
/* 1523 */         if ((this.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) && (this.playerController.onPlayerDamageBlock(blockpos, this.objectMouseOver.sideHit)))
/*      */         {
/* 1525 */           this.effectRenderer.addBlockHitEffects(blockpos, this.objectMouseOver.sideHit);
/* 1526 */           this.thePlayer.swingItem();
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1531 */         this.playerController.resetBlockRemoving();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void clickMouse()
/*      */   {
/* 1538 */     if (this.leftClickCounter <= 0)
/*      */     {
/* 1540 */       this.thePlayer.swingItem();
/*      */       
/* 1542 */       if (this.objectMouseOver == null)
/*      */       {
/* 1544 */         logger.error("Null returned as 'hitResult', this shouldn't happen!");
/*      */         
/* 1546 */         if (this.playerController.isNotCreative())
/*      */         {
/* 1548 */           this.leftClickCounter = 10;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1553 */         switch (this.objectMouseOver.typeOfHit)
/*      */         {
/*      */         case MISS: 
/* 1556 */           this.playerController.attackEntity(this.thePlayer, this.objectMouseOver.entityHit);
/* 1557 */           break;
/*      */         
/*      */         case ENTITY: 
/* 1560 */           BlockPos blockpos = this.objectMouseOver.getBlockPos();
/*      */           
/* 1562 */           if (this.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air)
/*      */           {
/* 1564 */             this.playerController.clickBlock(blockpos, this.objectMouseOver.sideHit); }
/* 1565 */           break;
/*      */         }
/*      */         
/*      */         
/*      */ 
/* 1570 */         if (this.playerController.isNotCreative())
/*      */         {
/* 1572 */           this.leftClickCounter = 10;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void rightClickMouse()
/*      */   {
/* 1586 */     if (!this.playerController.func_181040_m())
/*      */     {
/* 1588 */       this.rightClickDelayTimer = 4;
/* 1589 */       boolean flag = true;
/* 1590 */       ItemStack itemstack = this.thePlayer.inventory.getCurrentItem();
/*      */       
/* 1592 */       if (this.objectMouseOver == null)
/*      */       {
/* 1594 */         logger.warn("Null returned as 'hitResult', this shouldn't happen!");
/*      */       }
/*      */       else
/*      */       {
/* 1598 */         switch (this.objectMouseOver.typeOfHit)
/*      */         {
/*      */         case MISS: 
/* 1601 */           if (this.playerController.func_178894_a(this.thePlayer, this.objectMouseOver.entityHit, this.objectMouseOver))
/*      */           {
/* 1603 */             flag = false;
/*      */           }
/* 1605 */           else if (this.playerController.interactWithEntitySendPacket(this.thePlayer, this.objectMouseOver.entityHit))
/*      */           {
/* 1607 */             flag = false;
/*      */           }
/*      */           
/* 1610 */           break;
/*      */         
/*      */         case ENTITY: 
/* 1613 */           BlockPos blockpos = this.objectMouseOver.getBlockPos();
/*      */           
/* 1615 */           if (this.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air)
/*      */           {
/* 1617 */             int i = itemstack != null ? itemstack.stackSize : 0;
/*      */             
/* 1619 */             if (this.playerController.onPlayerRightClick(this.thePlayer, this.theWorld, itemstack, blockpos, this.objectMouseOver.sideHit, this.objectMouseOver.hitVec))
/*      */             {
/* 1621 */               flag = false;
/* 1622 */               this.thePlayer.swingItem();
/*      */             }
/*      */             
/* 1625 */             if (itemstack == null)
/*      */             {
/* 1627 */               return;
/*      */             }
/*      */             
/* 1630 */             if (itemstack.stackSize == 0)
/*      */             {
/* 1632 */               this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
/*      */             }
/* 1634 */             else if ((itemstack.stackSize != i) || (this.playerController.isInCreativeMode()))
/*      */             {
/* 1636 */               this.entityRenderer.itemRenderer.resetEquippedProgress();
/*      */             }
/*      */           }
/*      */           break;
/*      */         }
/*      */       }
/* 1642 */       if (flag)
/*      */       {
/* 1644 */         ItemStack itemstack1 = this.thePlayer.inventory.getCurrentItem();
/*      */         
/* 1646 */         if ((itemstack1 != null) && (this.playerController.sendUseItem(this.thePlayer, this.theWorld, itemstack1)))
/*      */         {
/* 1648 */           this.entityRenderer.itemRenderer.resetEquippedProgress2();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void toggleFullscreen()
/*      */   {
/*      */     try
/*      */     {
/* 1661 */       this.fullscreen = (!this.fullscreen);
/* 1662 */       this.gameSettings.fullScreen = this.fullscreen;
/*      */       
/* 1664 */       if (this.fullscreen)
/*      */       {
/* 1666 */         updateDisplayMode();
/* 1667 */         this.displayWidth = Display.getDisplayMode().getWidth();
/* 1668 */         this.displayHeight = Display.getDisplayMode().getHeight();
/*      */         
/* 1670 */         if (this.displayWidth <= 0)
/*      */         {
/* 1672 */           this.displayWidth = 1;
/*      */         }
/*      */         
/* 1675 */         if (this.displayHeight <= 0)
/*      */         {
/* 1677 */           this.displayHeight = 1;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1682 */         Display.setDisplayMode(new DisplayMode(this.tempDisplayWidth, this.tempDisplayHeight));
/* 1683 */         this.displayWidth = this.tempDisplayWidth;
/* 1684 */         this.displayHeight = this.tempDisplayHeight;
/*      */         
/* 1686 */         if (this.displayWidth <= 0)
/*      */         {
/* 1688 */           this.displayWidth = 1;
/*      */         }
/*      */         
/* 1691 */         if (this.displayHeight <= 0)
/*      */         {
/* 1693 */           this.displayHeight = 1;
/*      */         }
/*      */       }
/*      */       
/* 1697 */       if (this.currentScreen != null)
/*      */       {
/* 1699 */         resize(this.displayWidth, this.displayHeight);
/*      */       }
/*      */       else
/*      */       {
/* 1703 */         updateFramebufferSize();
/*      */       }
/*      */       
/* 1706 */       Display.setFullscreen(this.fullscreen);
/* 1707 */       Display.setVSyncEnabled(this.gameSettings.enableVsync);
/* 1708 */       updateDisplay();
/*      */     }
/*      */     catch (Exception exception)
/*      */     {
/* 1712 */       logger.error("Couldn't toggle fullscreen", exception);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void resize(int width, int height)
/*      */   {
/* 1721 */     this.displayWidth = Math.max(1, width);
/* 1722 */     this.displayHeight = Math.max(1, height);
/*      */     
/* 1724 */     if (this.currentScreen != null)
/*      */     {
/* 1726 */       ScaledResolution scaledresolution = new ScaledResolution(this, getMinecraft().displayWidth, getMinecraft().displayHeight);
/* 1727 */       this.currentScreen.onResize(this, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight());
/*      */     }
/*      */     
/* 1730 */     this.loadingScreen = new LoadingScreenRenderer(this);
/* 1731 */     updateFramebufferSize();
/*      */   }
/*      */   
/*      */   private void updateFramebufferSize()
/*      */   {
/* 1736 */     this.framebufferMc.createBindFramebuffer(this.displayWidth, this.displayHeight);
/*      */     
/* 1738 */     if (this.entityRenderer != null)
/*      */     {
/* 1740 */       this.entityRenderer.updateShaderGroupSize(this.displayWidth, this.displayHeight);
/*      */     }
/*      */   }
/*      */   
/*      */   public MusicTicker func_181535_r()
/*      */   {
/* 1746 */     return this.mcMusicTicker;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void runTick()
/*      */     throws IOException
/*      */   {
/* 1754 */     if (this.rightClickDelayTimer > 0)
/*      */     {
/* 1756 */       this.rightClickDelayTimer -= 1;
/*      */     }
/*      */     
/* 1759 */     this.mcProfiler.startSection("gui");
/*      */     
/* 1761 */     if (!this.isGamePaused)
/*      */     {
/* 1763 */       this.ingameGUI.updateTick();
/*      */     }
/*      */     
/* 1766 */     this.mcProfiler.endSection();
/* 1767 */     this.entityRenderer.getMouseOver(1.0F);
/* 1768 */     this.mcProfiler.startSection("gameMode");
/*      */     
/* 1770 */     if ((!this.isGamePaused) && (this.theWorld != null))
/*      */     {
/* 1772 */       this.playerController.updateController();
/*      */     }
/*      */     
/* 1775 */     this.mcProfiler.endStartSection("textures");
/*      */     
/* 1777 */     if (!this.isGamePaused)
/*      */     {
/* 1779 */       this.renderEngine.tick();
/*      */     }
/*      */     
/* 1782 */     if ((this.currentScreen == null) && (this.thePlayer != null))
/*      */     {
/* 1784 */       if (this.thePlayer.getHealth() <= 0.0F)
/*      */       {
/* 1786 */         displayGuiScreen(null);
/*      */       }
/* 1788 */       else if ((this.thePlayer.isPlayerSleeping()) && (this.theWorld != null))
/*      */       {
/* 1790 */         displayGuiScreen(new GuiSleepMP());
/*      */       }
/*      */     }
/* 1793 */     else if ((this.currentScreen != null) && ((this.currentScreen instanceof GuiSleepMP)) && (!this.thePlayer.isPlayerSleeping()))
/*      */     {
/* 1795 */       displayGuiScreen(null);
/*      */     }
/*      */     
/* 1798 */     if (this.currentScreen != null)
/*      */     {
/* 1800 */       this.leftClickCounter = 10000;
/*      */     }
/*      */     
/* 1803 */     if (this.currentScreen != null)
/*      */     {
/*      */       try
/*      */       {
/* 1807 */         this.currentScreen.handleInput();
/*      */       }
/*      */       catch (Throwable throwable1)
/*      */       {
/* 1811 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Updating screen events");
/* 1812 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Affected screen");
/* 1813 */         crashreportcategory.addCrashSectionCallable("Screen name", new Callable()
/*      */         {
/*      */           public String call() throws Exception
/*      */           {
/* 1817 */             return Minecraft.this.currentScreen.getClass().getCanonicalName();
/*      */           }
/* 1819 */         });
/* 1820 */         throw new ReportedException(crashreport);
/*      */       }
/*      */       
/* 1823 */       if (this.currentScreen != null)
/*      */       {
/*      */         try
/*      */         {
/* 1827 */           this.currentScreen.updateScreen();
/*      */         }
/*      */         catch (Throwable throwable)
/*      */         {
/* 1831 */           CrashReport crashreport1 = CrashReport.makeCrashReport(throwable, "Ticking screen");
/* 1832 */           CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Affected screen");
/* 1833 */           crashreportcategory1.addCrashSectionCallable("Screen name", new Callable()
/*      */           {
/*      */             public String call() throws Exception
/*      */             {
/* 1837 */               return Minecraft.this.currentScreen.getClass().getCanonicalName();
/*      */             }
/* 1839 */           });
/* 1840 */           throw new ReportedException(crashreport1);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1845 */     if ((this.currentScreen == null) || (this.currentScreen.allowUserInput))
/*      */     {
/* 1847 */       this.mcProfiler.endStartSection("mouse");
/*      */       
/* 1849 */       while (Mouse.next())
/*      */       {
/* 1851 */         int i = Mouse.getEventButton();
/* 1852 */         KeyBinding.setKeyBindState(i - 100, Mouse.getEventButtonState());
/*      */         
/* 1854 */         if (Mouse.getEventButtonState())
/*      */         {
/* 1856 */           EventMouse e = new EventMouse(i);
/* 1857 */           e.call();
/* 1858 */           if ((this.thePlayer.isSpectator()) && (i == 2))
/*      */           {
/* 1860 */             this.ingameGUI.getSpectatorGui().func_175261_b();
/*      */           }
/*      */           else
/*      */           {
/* 1864 */             KeyBinding.onTick(i - 100);
/*      */           }
/*      */         }
/*      */         
/* 1868 */         long i1 = getSystemTime() - this.systemTime;
/*      */         
/* 1870 */         if (i1 <= 200L)
/*      */         {
/* 1872 */           int j = Mouse.getEventDWheel();
/*      */           
/* 1874 */           if (j != 0)
/*      */           {
/* 1876 */             if (this.thePlayer.isSpectator())
/*      */             {
/* 1878 */               j = j < 0 ? -1 : 1;
/*      */               
/* 1880 */               if (this.ingameGUI.getSpectatorGui().func_175262_a())
/*      */               {
/* 1882 */                 this.ingameGUI.getSpectatorGui().func_175259_b(-j);
/*      */               }
/*      */               else
/*      */               {
/* 1886 */                 float f = MathHelper.clamp_float(this.thePlayer.capabilities.getFlySpeed() + j * 0.005F, 0.0F, 0.2F);
/* 1887 */                 this.thePlayer.capabilities.setFlySpeed(f);
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/* 1892 */               this.thePlayer.inventory.changeCurrentItem(j);
/*      */             }
/*      */           }
/*      */           
/* 1896 */           if (this.currentScreen == null)
/*      */           {
/* 1898 */             if ((!this.inGameHasFocus) && (Mouse.getEventButtonState()))
/*      */             {
/* 1900 */               setIngameFocus();
/*      */             }
/*      */           }
/* 1903 */           else if (this.currentScreen != null)
/*      */           {
/* 1905 */             this.currentScreen.handleMouseInput();
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1910 */       if (this.leftClickCounter > 0)
/*      */       {
/* 1912 */         this.leftClickCounter -= 1;
/*      */       }
/*      */       
/* 1915 */       this.mcProfiler.endStartSection("keyboard");
/*      */       
/* 1917 */       while (Keyboard.next())
/*      */       {
/* 1919 */         int k = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 'Ā' : Keyboard.getEventKey();
/* 1920 */         KeyBinding.setKeyBindState(k, Keyboard.getEventKeyState());
/*      */         
/* 1922 */         if (Keyboard.getEventKeyState())
/*      */         {
/* 1924 */           KeyBinding.onTick(k);
/*      */         }
/*      */         
/* 1927 */         if (this.debugCrashKeyPressTime > 0L)
/*      */         {
/* 1929 */           if (getSystemTime() - this.debugCrashKeyPressTime >= 6000L)
/*      */           {
/* 1931 */             throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
/*      */           }
/*      */           
/* 1934 */           if ((!Keyboard.isKeyDown(46)) || (!Keyboard.isKeyDown(61)))
/*      */           {
/* 1936 */             this.debugCrashKeyPressTime = -1L;
/*      */           }
/*      */         }
/* 1939 */         else if ((Keyboard.isKeyDown(46)) && (Keyboard.isKeyDown(61)))
/*      */         {
/* 1941 */           this.debugCrashKeyPressTime = getSystemTime();
/*      */         }
/*      */         
/* 1944 */         dispatchKeypresses();
/*      */         
/* 1946 */         if (Keyboard.getEventKeyState())
/*      */         {
/* 1948 */           if ((k == 62) && (this.entityRenderer != null))
/*      */           {
/* 1950 */             this.entityRenderer.switchUseShader();
/*      */           }
/*      */           
/* 1953 */           if (this.currentScreen != null)
/*      */           {
/* 1955 */             this.currentScreen.handleKeyboardInput();
/*      */           }
/*      */           else
/*      */           {
/* 1959 */             EventKey eventKey = new EventKey(k);
/* 1960 */             eventKey.call();
/*      */             
/* 1962 */             if (k == 1)
/*      */             {
/* 1964 */               displayInGameMenu();
/*      */             }
/*      */             
/* 1967 */             if ((k == 32) && (Keyboard.isKeyDown(61)) && (this.ingameGUI != null))
/*      */             {
/* 1969 */               this.ingameGUI.getChatGUI().clearChatMessages();
/*      */             }
/*      */             
/* 1972 */             if ((k == 31) && (Keyboard.isKeyDown(61)))
/*      */             {
/* 1974 */               refreshResources();
/*      */             }
/*      */             
/* 1977 */             if (((k != 17) || (!Keyboard.isKeyDown(61))) || (
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 1982 */               ((k != 18) || (!Keyboard.isKeyDown(61))) || (
/*      */               
/*      */ 
/*      */ 
/*      */ 
/* 1987 */               ((k != 47) || (!Keyboard.isKeyDown(61))) || (
/*      */               
/*      */ 
/*      */ 
/*      */ 
/* 1992 */               ((k != 38) || (!Keyboard.isKeyDown(61))) || (
/*      */               
/*      */ 
/*      */ 
/*      */ 
/* 1997 */               ((k != 22) || (!Keyboard.isKeyDown(61))) || (
/*      */               
/*      */ 
/*      */ 
/*      */ 
/* 2002 */               (k == 20) && (Keyboard.isKeyDown(61))))))))
/*      */             {
/* 2004 */               refreshResources();
/*      */             }
/*      */             
/* 2007 */             if ((k == 33) && (Keyboard.isKeyDown(61)))
/*      */             {
/* 2009 */               this.gameSettings.setOptionValue(GameSettings.Options.RENDER_DISTANCE, GuiScreen.isShiftKeyDown() ? -1 : 1);
/*      */             }
/*      */             
/* 2012 */             if ((k == 30) && (Keyboard.isKeyDown(61)))
/*      */             {
/* 2014 */               this.renderGlobal.loadRenderers();
/*      */             }
/*      */             
/* 2017 */             if ((k == 35) && (Keyboard.isKeyDown(61)))
/*      */             {
/* 2019 */               this.gameSettings.advancedItemTooltips = (!this.gameSettings.advancedItemTooltips);
/* 2020 */               this.gameSettings.saveOptions();
/*      */             }
/*      */             
/* 2023 */             if ((k == 48) && (Keyboard.isKeyDown(61)))
/*      */             {
/* 2025 */               this.renderManager.setDebugBoundingBox(!this.renderManager.isDebugBoundingBox());
/*      */             }
/*      */             
/* 2028 */             if ((k == 25) && (Keyboard.isKeyDown(61)))
/*      */             {
/* 2030 */               this.gameSettings.pauseOnLostFocus = (!this.gameSettings.pauseOnLostFocus);
/* 2031 */               this.gameSettings.saveOptions();
/*      */             }
/*      */             
/* 2034 */             if (k == 59)
/*      */             {
/* 2036 */               this.gameSettings.hideGUI = (!this.gameSettings.hideGUI);
/*      */             }
/*      */             
/* 2039 */             if (k == 61)
/*      */             {
/* 2041 */               this.gameSettings.showDebugInfo = (!this.gameSettings.showDebugInfo);
/* 2042 */               this.gameSettings.showDebugProfilerChart = GuiScreen.isShiftKeyDown();
/* 2043 */               this.gameSettings.field_181657_aC = GuiScreen.isAltKeyDown();
/*      */             }
/*      */             
/* 2046 */             if (this.gameSettings.keyBindTogglePerspective.isPressed())
/*      */             {
/* 2048 */               this.gameSettings.thirdPersonView += 1;
/*      */               
/* 2050 */               if (this.gameSettings.thirdPersonView > 2)
/*      */               {
/* 2052 */                 this.gameSettings.thirdPersonView = 0;
/*      */               }
/*      */               
/* 2055 */               if (this.gameSettings.thirdPersonView == 0)
/*      */               {
/* 2057 */                 this.entityRenderer.loadEntityShader(getRenderViewEntity());
/*      */               }
/* 2059 */               else if (this.gameSettings.thirdPersonView == 1)
/*      */               {
/* 2061 */                 this.entityRenderer.loadEntityShader(null);
/*      */               }
/*      */               
/* 2064 */               this.renderGlobal.setDisplayListEntitiesDirty();
/*      */             }
/*      */             
/* 2067 */             if (this.gameSettings.keyBindSmoothCamera.isPressed())
/*      */             {
/* 2069 */               this.gameSettings.smoothCamera = (!this.gameSettings.smoothCamera);
/*      */             }
/*      */           }
/*      */           
/* 2073 */           if ((this.gameSettings.showDebugInfo) && (this.gameSettings.showDebugProfilerChart))
/*      */           {
/* 2075 */             if (k == 11)
/*      */             {
/* 2077 */               updateDebugProfilerName(0);
/*      */             }
/*      */             
/* 2080 */             for (int j1 = 0; j1 < 9; j1++)
/*      */             {
/* 2082 */               if (k == 2 + j1)
/*      */               {
/* 2084 */                 updateDebugProfilerName(j1 + 1);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2091 */       for (int l = 0; l < 9; l++)
/*      */       {
/* 2093 */         if (this.gameSettings.keyBindsHotbar[l].isPressed())
/*      */         {
/* 2095 */           if (this.thePlayer.isSpectator())
/*      */           {
/* 2097 */             this.ingameGUI.getSpectatorGui().func_175260_a(l);
/*      */           }
/*      */           else
/*      */           {
/* 2101 */             this.thePlayer.inventory.currentItem = l;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2106 */       boolean flag = this.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN;
/*      */       
/* 2108 */       while (this.gameSettings.keyBindInventory.isPressed())
/*      */       {
/* 2110 */         if (this.playerController.isRidingHorse())
/*      */         {
/* 2112 */           this.thePlayer.sendHorseInventory();
/*      */         }
/*      */         else
/*      */         {
/* 2116 */           getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
/* 2117 */           displayGuiScreen(new GuiInventory(this.thePlayer));
/*      */         }
/*      */       }
/*      */       
/* 2121 */       while (this.gameSettings.keyBindDrop.isPressed())
/*      */       {
/* 2123 */         if (!this.thePlayer.isSpectator())
/*      */         {
/* 2125 */           this.thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
/*      */         }
/*      */       }
/*      */       
/* 2129 */       while ((this.gameSettings.keyBindChat.isPressed()) && (flag))
/*      */       {
/* 2131 */         displayGuiScreen(new GuiChat());
/*      */       }
/*      */       
/* 2134 */       if ((this.currentScreen == null) && (this.gameSettings.keyBindCommand.isPressed()) && (flag))
/*      */       {
/* 2136 */         displayGuiScreen(new GuiChat("/"));
/*      */       }
/*      */       
/* 2139 */       if (this.thePlayer.isUsingItem())
/*      */       {
/* 2141 */         if (!this.gameSettings.keyBindUseItem.isKeyDown())
/*      */         {
/* 2143 */           this.playerController.onStoppedUsingItem(this.thePlayer);
/*      */         }
/*      */         
/* 2146 */         while (this.gameSettings.keyBindAttack.isPressed()) {}
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 2151 */         while (this.gameSettings.keyBindUseItem.isPressed()) {}
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 2156 */         while (this.gameSettings.keyBindPickBlock.isPressed()) {}
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/* 2163 */         while (this.gameSettings.keyBindAttack.isPressed())
/*      */         {
/* 2165 */           clickMouse();
/*      */         }
/*      */         
/* 2168 */         while (this.gameSettings.keyBindUseItem.isPressed())
/*      */         {
/* 2170 */           rightClickMouse();
/*      */         }
/*      */         
/* 2173 */         while (this.gameSettings.keyBindPickBlock.isPressed())
/*      */         {
/* 2175 */           middleClickMouse();
/*      */         }
/*      */       }
/*      */       
/* 2179 */       if ((this.gameSettings.keyBindUseItem.isKeyDown()) && (this.rightClickDelayTimer == 0) && (!this.thePlayer.isUsingItem()))
/*      */       {
/* 2181 */         rightClickMouse();
/*      */       }
/*      */       
/* 2184 */       sendClickBlockToController((this.currentScreen == null) && (this.gameSettings.keyBindAttack.isKeyDown()) && (this.inGameHasFocus));
/*      */     }
/*      */     
/* 2187 */     if (this.theWorld != null)
/*      */     {
/* 2189 */       if (this.thePlayer != null)
/*      */       {
/* 2191 */         this.joinPlayerCounter += 1;
/*      */         
/* 2193 */         if (this.joinPlayerCounter == 30)
/*      */         {
/* 2195 */           this.joinPlayerCounter = 0;
/* 2196 */           this.theWorld.joinEntityInSurroundings(this.thePlayer);
/*      */         }
/*      */       }
/*      */       
/* 2200 */       this.mcProfiler.endStartSection("gameRenderer");
/*      */       
/* 2202 */       if (!this.isGamePaused)
/*      */       {
/* 2204 */         this.entityRenderer.updateRenderer();
/*      */       }
/*      */       
/* 2207 */       this.mcProfiler.endStartSection("levelRenderer");
/*      */       
/* 2209 */       if (!this.isGamePaused)
/*      */       {
/* 2211 */         this.renderGlobal.updateClouds();
/*      */       }
/*      */       
/* 2214 */       this.mcProfiler.endStartSection("level");
/*      */       
/* 2216 */       if (!this.isGamePaused)
/*      */       {
/* 2218 */         if (this.theWorld.getLastLightningBolt() > 0)
/*      */         {
/* 2220 */           this.theWorld.setLastLightningBolt(this.theWorld.getLastLightningBolt() - 1);
/*      */         }
/*      */         
/* 2223 */         this.theWorld.updateEntities();
/*      */       }
/*      */     }
/* 2226 */     else if (this.entityRenderer.isShaderActive())
/*      */     {
/* 2228 */       this.entityRenderer.func_181022_b();
/*      */     }
/*      */     
/* 2231 */     if (!this.isGamePaused)
/*      */     {
/* 2233 */       this.mcMusicTicker.update();
/* 2234 */       this.mcSoundHandler.update();
/*      */     }
/*      */     
/* 2237 */     if (this.theWorld != null)
/*      */     {
/* 2239 */       if (!this.isGamePaused)
/*      */       {
/* 2241 */         this.theWorld.setAllowedSpawnTypes(this.theWorld.getDifficulty() != EnumDifficulty.PEACEFUL, true);
/*      */         
/*      */         try
/*      */         {
/* 2245 */           this.theWorld.tick();
/*      */         }
/*      */         catch (Throwable throwable2)
/*      */         {
/* 2249 */           CrashReport crashreport2 = CrashReport.makeCrashReport(throwable2, "Exception in world tick");
/*      */           
/* 2251 */           if (this.theWorld == null)
/*      */           {
/* 2253 */             CrashReportCategory crashreportcategory2 = crashreport2.makeCategory("Affected level");
/* 2254 */             crashreportcategory2.addCrashSection("Problem", "Level is null!");
/*      */           }
/*      */           else
/*      */           {
/* 2258 */             this.theWorld.addWorldInfoToCrashReport(crashreport2);
/*      */           }
/*      */           
/* 2261 */           throw new ReportedException(crashreport2);
/*      */         }
/*      */       }
/*      */       
/* 2265 */       this.mcProfiler.endStartSection("animateTick");
/*      */       
/* 2267 */       if ((!this.isGamePaused) && (this.theWorld != null))
/*      */       {
/* 2269 */         this.theWorld.doVoidFogParticles(MathHelper.floor_double(this.thePlayer.posX), MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ));
/*      */       }
/*      */       
/* 2272 */       this.mcProfiler.endStartSection("particles");
/*      */       
/* 2274 */       if (!this.isGamePaused)
/*      */       {
/* 2276 */         this.effectRenderer.updateEffects();
/*      */       }
/*      */     }
/* 2279 */     else if (this.myNetworkManager != null)
/*      */     {
/* 2281 */       this.mcProfiler.endStartSection("pendingConnection");
/* 2282 */       this.myNetworkManager.processReceivedPackets();
/*      */     }
/*      */     
/* 2285 */     this.mcProfiler.endSection();
/* 2286 */     this.systemTime = getSystemTime();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void launchIntegratedServer(String folderName, String worldName, WorldSettings worldSettingsIn)
/*      */   {
/* 2294 */     loadWorld(null);
/* 2295 */     System.gc();
/* 2296 */     ISaveHandler isavehandler = this.saveLoader.getSaveLoader(folderName, false);
/* 2297 */     WorldInfo worldinfo = isavehandler.loadWorldInfo();
/*      */     
/* 2299 */     if ((worldinfo == null) && (worldSettingsIn != null))
/*      */     {
/* 2301 */       worldinfo = new WorldInfo(worldSettingsIn, folderName);
/* 2302 */       isavehandler.saveWorldInfo(worldinfo);
/*      */     }
/*      */     
/* 2305 */     if (worldSettingsIn == null)
/*      */     {
/* 2307 */       worldSettingsIn = new WorldSettings(worldinfo);
/*      */     }
/*      */     
/*      */     try
/*      */     {
/* 2312 */       this.theIntegratedServer = new IntegratedServer(this, folderName, worldName, worldSettingsIn);
/* 2313 */       this.theIntegratedServer.startServerThread();
/* 2314 */       this.integratedServerIsRunning = true;
/*      */     }
/*      */     catch (Throwable throwable)
/*      */     {
/* 2318 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Starting integrated server");
/* 2319 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Starting integrated server");
/* 2320 */       crashreportcategory.addCrashSection("Level ID", folderName);
/* 2321 */       crashreportcategory.addCrashSection("Level Name", worldName);
/* 2322 */       throw new ReportedException(crashreport);
/*      */     }
/*      */     
/* 2325 */     this.loadingScreen.displaySavingString(I18n.format("menu.loadingLevel", new Object[0]));
/*      */     
/* 2327 */     while (!this.theIntegratedServer.serverIsInRunLoop())
/*      */     {
/* 2329 */       String s = this.theIntegratedServer.getUserMessage();
/*      */       
/* 2331 */       if (s != null)
/*      */       {
/* 2333 */         this.loadingScreen.displayLoadingString(I18n.format(s, new Object[0]));
/*      */       }
/*      */       else
/*      */       {
/* 2337 */         this.loadingScreen.displayLoadingString("");
/*      */       }
/*      */       
/*      */       try
/*      */       {
/* 2342 */         Thread.sleep(200L);
/*      */       }
/*      */       catch (InterruptedException localInterruptedException) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2350 */     displayGuiScreen(null);
/* 2351 */     SocketAddress socketaddress = this.theIntegratedServer.getNetworkSystem().addLocalEndpoint();
/* 2352 */     NetworkManager networkmanager = NetworkManager.provideLocalClient(socketaddress);
/* 2353 */     networkmanager.setNetHandler(new NetHandlerLoginClient(networkmanager, this, null));
/* 2354 */     networkmanager.sendPacket(new C00Handshake(47, socketaddress.toString(), 0, net.minecraft.network.EnumConnectionState.LOGIN));
/* 2355 */     networkmanager.sendPacket(new C00PacketLoginStart(getSession().getProfile()));
/* 2356 */     this.myNetworkManager = networkmanager;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void loadWorld(WorldClient worldClientIn)
/*      */   {
/* 2364 */     loadWorld(worldClientIn, "");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void loadWorld(WorldClient worldClientIn, String loadingMessage)
/*      */   {
/* 2372 */     if (worldClientIn == null)
/*      */     {
/* 2374 */       NetHandlerPlayClient nethandlerplayclient = getNetHandler();
/*      */       
/* 2376 */       if (nethandlerplayclient != null)
/*      */       {
/* 2378 */         nethandlerplayclient.cleanup();
/*      */       }
/*      */       
/* 2381 */       if ((this.theIntegratedServer != null) && (this.theIntegratedServer.isAnvilFileSet()))
/*      */       {
/* 2383 */         this.theIntegratedServer.initiateShutdown();
/* 2384 */         this.theIntegratedServer.setStaticInstance();
/*      */       }
/*      */       
/* 2387 */       this.theIntegratedServer = null;
/* 2388 */       this.guiAchievement.clearAchievements();
/* 2389 */       this.entityRenderer.getMapItemRenderer().clearLoadedMaps();
/*      */     }
/*      */     
/* 2392 */     this.renderViewEntity = null;
/* 2393 */     this.myNetworkManager = null;
/*      */     
/* 2395 */     if (this.loadingScreen != null)
/*      */     {
/* 2397 */       this.loadingScreen.resetProgressAndMessage(loadingMessage);
/* 2398 */       this.loadingScreen.displayLoadingString("");
/*      */     }
/*      */     
/* 2401 */     if ((worldClientIn == null) && (this.theWorld != null))
/*      */     {
/* 2403 */       this.mcResourcePackRepository.func_148529_f();
/* 2404 */       this.ingameGUI.func_181029_i();
/* 2405 */       setServerData(null);
/* 2406 */       this.integratedServerIsRunning = false;
/*      */     }
/*      */     
/* 2409 */     this.mcSoundHandler.stopSounds();
/* 2410 */     this.theWorld = worldClientIn;
/*      */     
/* 2412 */     if (worldClientIn != null)
/*      */     {
/* 2414 */       if (this.renderGlobal != null)
/*      */       {
/* 2416 */         this.renderGlobal.setWorldAndLoadRenderers(worldClientIn);
/*      */       }
/*      */       
/* 2419 */       if (this.effectRenderer != null)
/*      */       {
/* 2421 */         this.effectRenderer.clearEffects(worldClientIn);
/*      */       }
/*      */       
/* 2424 */       if (this.thePlayer == null)
/*      */       {
/* 2426 */         this.thePlayer = this.playerController.func_178892_a(worldClientIn, new StatFileWriter());
/* 2427 */         this.playerController.flipPlayer(this.thePlayer);
/*      */       }
/*      */       
/* 2430 */       this.thePlayer.preparePlayerToSpawn();
/* 2431 */       worldClientIn.spawnEntityInWorld(this.thePlayer);
/* 2432 */       this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
/* 2433 */       this.playerController.setPlayerCapabilities(this.thePlayer);
/* 2434 */       this.renderViewEntity = this.thePlayer;
/*      */     }
/*      */     else
/*      */     {
/* 2438 */       this.saveLoader.flushCache();
/* 2439 */       this.thePlayer = null;
/*      */     }
/*      */     
/* 2442 */     System.gc();
/* 2443 */     this.systemTime = 0L;
/*      */   }
/*      */   
/*      */   public void setDimensionAndSpawnPlayer(int dimension)
/*      */   {
/* 2448 */     this.theWorld.setInitialSpawnLocation();
/* 2449 */     this.theWorld.removeAllEntities();
/* 2450 */     int i = 0;
/* 2451 */     String s = null;
/*      */     
/* 2453 */     if (this.thePlayer != null)
/*      */     {
/* 2455 */       i = this.thePlayer.getEntityId();
/* 2456 */       this.theWorld.removeEntity(this.thePlayer);
/* 2457 */       s = this.thePlayer.getClientBrand();
/*      */     }
/*      */     
/* 2460 */     this.renderViewEntity = null;
/* 2461 */     EntityPlayerSP entityplayersp = this.thePlayer;
/* 2462 */     this.thePlayer = this.playerController.func_178892_a(this.theWorld, this.thePlayer == null ? new StatFileWriter() : this.thePlayer.getStatFileWriter());
/* 2463 */     this.thePlayer.getDataWatcher().updateWatchedObjectsFromList(entityplayersp.getDataWatcher().getAllWatched());
/* 2464 */     this.thePlayer.dimension = dimension;
/* 2465 */     this.renderViewEntity = this.thePlayer;
/* 2466 */     this.thePlayer.preparePlayerToSpawn();
/* 2467 */     this.thePlayer.setClientBrand(s);
/* 2468 */     this.theWorld.spawnEntityInWorld(this.thePlayer);
/* 2469 */     this.playerController.flipPlayer(this.thePlayer);
/* 2470 */     this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
/* 2471 */     this.thePlayer.setEntityId(i);
/* 2472 */     this.playerController.setPlayerCapabilities(this.thePlayer);
/* 2473 */     this.thePlayer.setReducedDebug(entityplayersp.hasReducedDebug());
/*      */     
/* 2475 */     if ((this.currentScreen instanceof GuiGameOver))
/*      */     {
/* 2477 */       displayGuiScreen(null);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean isDemo()
/*      */   {
/* 2486 */     return this.isDemo;
/*      */   }
/*      */   
/*      */   public NetHandlerPlayClient getNetHandler()
/*      */   {
/* 2491 */     return this.thePlayer != null ? this.thePlayer.sendQueue : null;
/*      */   }
/*      */   
/*      */   public static boolean isGuiEnabled()
/*      */   {
/* 2496 */     return (theMinecraft == null) || (!theMinecraft.gameSettings.hideGUI);
/*      */   }
/*      */   
/*      */   public static boolean isFancyGraphicsEnabled()
/*      */   {
/* 2501 */     return (theMinecraft != null) && (theMinecraft.gameSettings.fancyGraphics);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isAmbientOcclusionEnabled()
/*      */   {
/* 2509 */     return (theMinecraft != null) && (theMinecraft.gameSettings.ambientOcclusion != 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void middleClickMouse()
/*      */   {
/* 2517 */     if (this.objectMouseOver != null)
/*      */     {
/* 2519 */       boolean flag = this.thePlayer.capabilities.isCreativeMode;
/* 2520 */       int i = 0;
/* 2521 */       boolean flag1 = false;
/* 2522 */       TileEntity tileentity = null;
/*      */       
/*      */       Item item;
/* 2525 */       if (this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
/*      */       {
/* 2527 */         BlockPos blockpos = this.objectMouseOver.getBlockPos();
/* 2528 */         Block block = this.theWorld.getBlockState(blockpos).getBlock();
/*      */         
/* 2530 */         if (block.getMaterial() == Material.air)
/*      */         {
/* 2532 */           return;
/*      */         }
/*      */         
/* 2535 */         Item item = block.getItem(this.theWorld, blockpos);
/*      */         
/* 2537 */         if (item == null)
/*      */         {
/* 2539 */           return;
/*      */         }
/*      */         
/* 2542 */         if ((flag) && (GuiScreen.isCtrlKeyDown()))
/*      */         {
/* 2544 */           tileentity = this.theWorld.getTileEntity(blockpos);
/*      */         }
/*      */         
/* 2547 */         Block block1 = ((item instanceof ItemBlock)) && (!block.isFlowerPot()) ? Block.getBlockFromItem(item) : block;
/* 2548 */         i = block1.getDamageValue(this.theWorld, blockpos);
/* 2549 */         flag1 = item.getHasSubtypes();
/*      */       }
/*      */       else
/*      */       {
/* 2553 */         if ((this.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) || (this.objectMouseOver.entityHit == null) || (!flag)) {
/*      */           return;
/*      */         }
/*      */         
/*      */         Item item;
/* 2558 */         if ((this.objectMouseOver.entityHit instanceof EntityPainting))
/*      */         {
/* 2560 */           item = Items.painting;
/*      */         } else { Item item;
/* 2562 */           if ((this.objectMouseOver.entityHit instanceof EntityLeashKnot))
/*      */           {
/* 2564 */             item = Items.lead;
/*      */           }
/* 2566 */           else if ((this.objectMouseOver.entityHit instanceof EntityItemFrame))
/*      */           {
/* 2568 */             EntityItemFrame entityitemframe = (EntityItemFrame)this.objectMouseOver.entityHit;
/* 2569 */             ItemStack itemstack = entityitemframe.getDisplayedItem();
/*      */             Item item;
/* 2571 */             if (itemstack == null)
/*      */             {
/* 2573 */               item = Items.item_frame;
/*      */             }
/*      */             else
/*      */             {
/* 2577 */               Item item = itemstack.getItem();
/* 2578 */               i = itemstack.getMetadata();
/* 2579 */               flag1 = true;
/*      */             }
/*      */           }
/* 2582 */           else if ((this.objectMouseOver.entityHit instanceof EntityMinecart))
/*      */           {
/* 2584 */             EntityMinecart entityminecart = (EntityMinecart)this.objectMouseOver.entityHit;
/*      */             Item item;
/* 2586 */             Item item; Item item; Item item; Item item; Item item; switch (entityminecart.getMinecartType())
/*      */             {
/*      */             case FURNACE: 
/* 2589 */               item = Items.furnace_minecart;
/* 2590 */               break;
/*      */             
/*      */             case COMMAND_BLOCK: 
/* 2593 */               item = Items.chest_minecart;
/* 2594 */               break;
/*      */             
/*      */             case HOPPER: 
/* 2597 */               item = Items.tnt_minecart;
/* 2598 */               break;
/*      */             
/*      */             case SPAWNER: 
/* 2601 */               item = Items.hopper_minecart;
/* 2602 */               break;
/*      */             
/*      */             case TNT: 
/* 2605 */               item = Items.command_block_minecart;
/* 2606 */               break;
/*      */             case RIDEABLE: 
/*      */             default: 
/* 2609 */               item = Items.minecart;
/*      */               
/* 2611 */               break; } } else { Item item;
/* 2612 */             if ((this.objectMouseOver.entityHit instanceof net.minecraft.entity.item.EntityBoat))
/*      */             {
/* 2614 */               item = Items.boat;
/*      */             } else { Item item;
/* 2616 */               if ((this.objectMouseOver.entityHit instanceof EntityArmorStand))
/*      */               {
/* 2618 */                 item = Items.armor_stand;
/*      */               }
/*      */               else
/*      */               {
/* 2622 */                 item = Items.spawn_egg;
/* 2623 */                 i = EntityList.getEntityID(this.objectMouseOver.entityHit);
/* 2624 */                 flag1 = true;
/*      */                 
/* 2626 */                 if (!EntityList.entityEggs.containsKey(Integer.valueOf(i)))
/*      */                 {
/* 2628 */                   return; }
/*      */               }
/*      */             }
/*      */           }
/*      */         } }
/* 2633 */       InventoryPlayer inventoryplayer = this.thePlayer.inventory;
/*      */       
/* 2635 */       if (tileentity == null)
/*      */       {
/* 2637 */         inventoryplayer.setCurrentItem(item, i, flag1, flag);
/*      */       }
/*      */       else
/*      */       {
/* 2641 */         ItemStack itemstack1 = func_181036_a(item, i, tileentity);
/* 2642 */         inventoryplayer.setInventorySlotContents(inventoryplayer.currentItem, itemstack1);
/*      */       }
/*      */       
/* 2645 */       if (flag)
/*      */       {
/* 2647 */         int j = this.thePlayer.inventoryContainer.inventorySlots.size() - 9 + inventoryplayer.currentItem;
/* 2648 */         this.playerController.sendSlotPacket(inventoryplayer.getStackInSlot(inventoryplayer.currentItem), j);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private ItemStack func_181036_a(Item p_181036_1_, int p_181036_2_, TileEntity p_181036_3_)
/*      */   {
/* 2655 */     ItemStack itemstack = new ItemStack(p_181036_1_, 1, p_181036_2_);
/* 2656 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 2657 */     p_181036_3_.writeToNBT(nbttagcompound);
/*      */     
/* 2659 */     if ((p_181036_1_ == Items.skull) && (nbttagcompound.hasKey("Owner")))
/*      */     {
/* 2661 */       NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Owner");
/* 2662 */       NBTTagCompound nbttagcompound3 = new NBTTagCompound();
/* 2663 */       nbttagcompound3.setTag("SkullOwner", nbttagcompound2);
/* 2664 */       itemstack.setTagCompound(nbttagcompound3);
/* 2665 */       return itemstack;
/*      */     }
/*      */     
/*      */ 
/* 2669 */     itemstack.setTagInfo("BlockEntityTag", nbttagcompound);
/* 2670 */     NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 2671 */     NBTTagList nbttaglist = new NBTTagList();
/* 2672 */     nbttaglist.appendTag(new NBTTagString("(+NBT)"));
/* 2673 */     nbttagcompound1.setTag("Lore", nbttaglist);
/* 2674 */     itemstack.setTagInfo("display", nbttagcompound1);
/* 2675 */     return itemstack;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CrashReport addGraphicsAndWorldToCrashReport(CrashReport theCrash)
/*      */   {
/* 2684 */     theCrash.getCategory().addCrashSectionCallable("Launched Version", new Callable()
/*      */     {
/*      */       public String call() throws Exception
/*      */       {
/* 2688 */         return Minecraft.this.launchedVersion;
/*      */       }
/* 2690 */     });
/* 2691 */     theCrash.getCategory().addCrashSectionCallable("LWJGL", new Callable()
/*      */     {
/*      */       public String call()
/*      */       {
/* 2695 */         return Sys.getVersion();
/*      */       }
/* 2697 */     });
/* 2698 */     theCrash.getCategory().addCrashSectionCallable("OpenGL", new Callable()
/*      */     {
/*      */       public String call()
/*      */       {
/* 2702 */         return GL11.glGetString(7937) + " GL version " + GL11.glGetString(7938) + ", " + GL11.glGetString(7936);
/*      */       }
/* 2704 */     });
/* 2705 */     theCrash.getCategory().addCrashSectionCallable("GL Caps", new Callable()
/*      */     {
/*      */       public String call()
/*      */       {
/* 2709 */         return OpenGlHelper.getLogText();
/*      */       }
/* 2711 */     });
/* 2712 */     theCrash.getCategory().addCrashSectionCallable("Using VBOs", new Callable()
/*      */     {
/*      */       public String call()
/*      */       {
/* 2716 */         return Minecraft.this.gameSettings.useVbo ? "Yes" : "No";
/*      */       }
/* 2718 */     });
/* 2719 */     theCrash.getCategory().addCrashSectionCallable("Is Modded", new Callable()
/*      */     {
/*      */       public String call() throws Exception
/*      */       {
/* 2723 */         String s = ClientBrandRetriever.getClientModName();
/* 2724 */         return Minecraft.class.getSigners() == null ? "Very likely; Jar signature invalidated" : !s.equals("vanilla") ? "Definitely; Client brand changed to '" + s + "'" : "Probably not. Jar signature remains and client brand is untouched.";
/*      */       }
/* 2726 */     });
/* 2727 */     theCrash.getCategory().addCrashSectionCallable("Type", new Callable()
/*      */     {
/*      */       public String call() throws Exception
/*      */       {
/* 2731 */         return "Client (map_client.txt)";
/*      */       }
/* 2733 */     });
/* 2734 */     theCrash.getCategory().addCrashSectionCallable("Resource Packs", new Callable()
/*      */     {
/*      */       public String call() throws Exception
/*      */       {
/* 2738 */         StringBuilder stringbuilder = new StringBuilder();
/*      */         
/* 2740 */         for (Object s : Minecraft.this.gameSettings.resourcePacks)
/*      */         {
/* 2742 */           if (stringbuilder.length() > 0)
/*      */           {
/* 2744 */             stringbuilder.append(", ");
/*      */           }
/*      */           
/* 2747 */           stringbuilder.append(s);
/*      */           
/* 2749 */           if (Minecraft.this.gameSettings.field_183018_l.contains(s))
/*      */           {
/* 2751 */             stringbuilder.append(" (incompatible)");
/*      */           }
/*      */         }
/*      */         
/* 2755 */         return stringbuilder.toString();
/*      */       }
/* 2757 */     });
/* 2758 */     theCrash.getCategory().addCrashSectionCallable("Current Language", new Callable()
/*      */     {
/*      */       public String call() throws Exception
/*      */       {
/* 2762 */         return Minecraft.this.mcLanguageManager.getCurrentLanguage().toString();
/*      */       }
/* 2764 */     });
/* 2765 */     theCrash.getCategory().addCrashSectionCallable("Profiler Position", new Callable()
/*      */     {
/*      */       public String call() throws Exception
/*      */       {
/* 2769 */         return Minecraft.this.mcProfiler.profilingEnabled ? Minecraft.this.mcProfiler.getNameOfLastSection() : "N/A (disabled)";
/*      */       }
/* 2771 */     });
/* 2772 */     theCrash.getCategory().addCrashSectionCallable("CPU", new Callable()
/*      */     {
/*      */       public String call()
/*      */       {
/* 2776 */         return OpenGlHelper.func_183029_j();
/*      */       }
/*      */     });
/*      */     
/* 2780 */     if (this.theWorld != null)
/*      */     {
/* 2782 */       this.theWorld.addWorldInfoToCrashReport(theCrash);
/*      */     }
/*      */     
/* 2785 */     return theCrash;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Minecraft getMinecraft()
/*      */   {
/* 2793 */     return theMinecraft;
/*      */   }
/*      */   
/*      */   public ListenableFuture<Object> scheduleResourcesRefresh()
/*      */   {
/* 2798 */     addScheduledTask(new Runnable()
/*      */     {
/*      */       public void run()
/*      */       {
/* 2802 */         Minecraft.this.refreshResources();
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   public void addServerStatsToSnooper(PlayerUsageSnooper playerSnooper)
/*      */   {
/* 2809 */     playerSnooper.addClientStat("fps", Integer.valueOf(debugFPS));
/* 2810 */     playerSnooper.addClientStat("vsync_enabled", Boolean.valueOf(this.gameSettings.enableVsync));
/* 2811 */     playerSnooper.addClientStat("display_frequency", Integer.valueOf(Display.getDisplayMode().getFrequency()));
/* 2812 */     playerSnooper.addClientStat("display_type", this.fullscreen ? "fullscreen" : "windowed");
/* 2813 */     playerSnooper.addClientStat("run_time", Long.valueOf((MinecraftServer.getCurrentTimeMillis() - playerSnooper.getMinecraftStartTimeMillis()) / 60L * 1000L));
/* 2814 */     playerSnooper.addClientStat("current_action", func_181538_aA());
/* 2815 */     String s = ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN ? "little" : "big";
/* 2816 */     playerSnooper.addClientStat("endianness", s);
/* 2817 */     playerSnooper.addClientStat("resource_packs", Integer.valueOf(this.mcResourcePackRepository.getRepositoryEntries().size()));
/* 2818 */     int i = 0;
/*      */     
/* 2820 */     for (ResourcePackRepository.Entry resourcepackrepository$entry : this.mcResourcePackRepository.getRepositoryEntries())
/*      */     {
/* 2822 */       playerSnooper.addClientStat("resource_pack[" + i++ + "]", resourcepackrepository$entry.getResourcePackName());
/*      */     }
/*      */     
/* 2825 */     if ((this.theIntegratedServer != null) && (this.theIntegratedServer.getPlayerUsageSnooper() != null))
/*      */     {
/* 2827 */       playerSnooper.addClientStat("snooper_partner", this.theIntegratedServer.getPlayerUsageSnooper().getUniqueID());
/*      */     }
/*      */   }
/*      */   
/*      */   private String func_181538_aA()
/*      */   {
/* 2833 */     return this.currentServerData != null ? "multiplayer" : this.currentServerData.func_181041_d() ? "playing_lan" : this.theIntegratedServer != null ? "singleplayer" : this.theIntegratedServer.getPublic() ? "hosting_lan" : "out_of_game";
/*      */   }
/*      */   
/*      */   public void addServerTypeToSnooper(PlayerUsageSnooper playerSnooper)
/*      */   {
/* 2838 */     playerSnooper.addStatToSnooper("opengl_version", GL11.glGetString(7938));
/* 2839 */     playerSnooper.addStatToSnooper("opengl_vendor", GL11.glGetString(7936));
/* 2840 */     playerSnooper.addStatToSnooper("client_brand", ClientBrandRetriever.getClientModName());
/* 2841 */     playerSnooper.addStatToSnooper("launched_version", this.launchedVersion);
/* 2842 */     ContextCapabilities contextcapabilities = GLContext.getCapabilities();
/* 2843 */     playerSnooper.addStatToSnooper("gl_caps[ARB_arrays_of_arrays]", Boolean.valueOf(contextcapabilities.GL_ARB_arrays_of_arrays));
/* 2844 */     playerSnooper.addStatToSnooper("gl_caps[ARB_base_instance]", Boolean.valueOf(contextcapabilities.GL_ARB_base_instance));
/* 2845 */     playerSnooper.addStatToSnooper("gl_caps[ARB_blend_func_extended]", Boolean.valueOf(contextcapabilities.GL_ARB_blend_func_extended));
/* 2846 */     playerSnooper.addStatToSnooper("gl_caps[ARB_clear_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_clear_buffer_object));
/* 2847 */     playerSnooper.addStatToSnooper("gl_caps[ARB_color_buffer_float]", Boolean.valueOf(contextcapabilities.GL_ARB_color_buffer_float));
/* 2848 */     playerSnooper.addStatToSnooper("gl_caps[ARB_compatibility]", Boolean.valueOf(contextcapabilities.GL_ARB_compatibility));
/* 2849 */     playerSnooper.addStatToSnooper("gl_caps[ARB_compressed_texture_pixel_storage]", Boolean.valueOf(contextcapabilities.GL_ARB_compressed_texture_pixel_storage));
/* 2850 */     playerSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_compute_shader));
/* 2851 */     playerSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", Boolean.valueOf(contextcapabilities.GL_ARB_copy_buffer));
/* 2852 */     playerSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", Boolean.valueOf(contextcapabilities.GL_ARB_copy_image));
/* 2853 */     playerSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", Boolean.valueOf(contextcapabilities.GL_ARB_depth_buffer_float));
/* 2854 */     playerSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_compute_shader));
/* 2855 */     playerSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", Boolean.valueOf(contextcapabilities.GL_ARB_copy_buffer));
/* 2856 */     playerSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", Boolean.valueOf(contextcapabilities.GL_ARB_copy_image));
/* 2857 */     playerSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", Boolean.valueOf(contextcapabilities.GL_ARB_depth_buffer_float));
/* 2858 */     playerSnooper.addStatToSnooper("gl_caps[ARB_depth_clamp]", Boolean.valueOf(contextcapabilities.GL_ARB_depth_clamp));
/* 2859 */     playerSnooper.addStatToSnooper("gl_caps[ARB_depth_texture]", Boolean.valueOf(contextcapabilities.GL_ARB_depth_texture));
/* 2860 */     playerSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers]", Boolean.valueOf(contextcapabilities.GL_ARB_draw_buffers));
/* 2861 */     playerSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers_blend]", Boolean.valueOf(contextcapabilities.GL_ARB_draw_buffers_blend));
/* 2862 */     playerSnooper.addStatToSnooper("gl_caps[ARB_draw_elements_base_vertex]", Boolean.valueOf(contextcapabilities.GL_ARB_draw_elements_base_vertex));
/* 2863 */     playerSnooper.addStatToSnooper("gl_caps[ARB_draw_indirect]", Boolean.valueOf(contextcapabilities.GL_ARB_draw_indirect));
/* 2864 */     playerSnooper.addStatToSnooper("gl_caps[ARB_draw_instanced]", Boolean.valueOf(contextcapabilities.GL_ARB_draw_instanced));
/* 2865 */     playerSnooper.addStatToSnooper("gl_caps[ARB_explicit_attrib_location]", Boolean.valueOf(contextcapabilities.GL_ARB_explicit_attrib_location));
/* 2866 */     playerSnooper.addStatToSnooper("gl_caps[ARB_explicit_uniform_location]", Boolean.valueOf(contextcapabilities.GL_ARB_explicit_uniform_location));
/* 2867 */     playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_layer_viewport]", Boolean.valueOf(contextcapabilities.GL_ARB_fragment_layer_viewport));
/* 2868 */     playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_program]", Boolean.valueOf(contextcapabilities.GL_ARB_fragment_program));
/* 2869 */     playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_fragment_shader));
/* 2870 */     playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_program_shadow]", Boolean.valueOf(contextcapabilities.GL_ARB_fragment_program_shadow));
/* 2871 */     playerSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_framebuffer_object));
/* 2872 */     playerSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_sRGB]", Boolean.valueOf(contextcapabilities.GL_ARB_framebuffer_sRGB));
/* 2873 */     playerSnooper.addStatToSnooper("gl_caps[ARB_geometry_shader4]", Boolean.valueOf(contextcapabilities.GL_ARB_geometry_shader4));
/* 2874 */     playerSnooper.addStatToSnooper("gl_caps[ARB_gpu_shader5]", Boolean.valueOf(contextcapabilities.GL_ARB_gpu_shader5));
/* 2875 */     playerSnooper.addStatToSnooper("gl_caps[ARB_half_float_pixel]", Boolean.valueOf(contextcapabilities.GL_ARB_half_float_pixel));
/* 2876 */     playerSnooper.addStatToSnooper("gl_caps[ARB_half_float_vertex]", Boolean.valueOf(contextcapabilities.GL_ARB_half_float_vertex));
/* 2877 */     playerSnooper.addStatToSnooper("gl_caps[ARB_instanced_arrays]", Boolean.valueOf(contextcapabilities.GL_ARB_instanced_arrays));
/* 2878 */     playerSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_alignment]", Boolean.valueOf(contextcapabilities.GL_ARB_map_buffer_alignment));
/* 2879 */     playerSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_range]", Boolean.valueOf(contextcapabilities.GL_ARB_map_buffer_range));
/* 2880 */     playerSnooper.addStatToSnooper("gl_caps[ARB_multisample]", Boolean.valueOf(contextcapabilities.GL_ARB_multisample));
/* 2881 */     playerSnooper.addStatToSnooper("gl_caps[ARB_multitexture]", Boolean.valueOf(contextcapabilities.GL_ARB_multitexture));
/* 2882 */     playerSnooper.addStatToSnooper("gl_caps[ARB_occlusion_query2]", Boolean.valueOf(contextcapabilities.GL_ARB_occlusion_query2));
/* 2883 */     playerSnooper.addStatToSnooper("gl_caps[ARB_pixel_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_pixel_buffer_object));
/* 2884 */     playerSnooper.addStatToSnooper("gl_caps[ARB_seamless_cube_map]", Boolean.valueOf(contextcapabilities.GL_ARB_seamless_cube_map));
/* 2885 */     playerSnooper.addStatToSnooper("gl_caps[ARB_shader_objects]", Boolean.valueOf(contextcapabilities.GL_ARB_shader_objects));
/* 2886 */     playerSnooper.addStatToSnooper("gl_caps[ARB_shader_stencil_export]", Boolean.valueOf(contextcapabilities.GL_ARB_shader_stencil_export));
/* 2887 */     playerSnooper.addStatToSnooper("gl_caps[ARB_shader_texture_lod]", Boolean.valueOf(contextcapabilities.GL_ARB_shader_texture_lod));
/* 2888 */     playerSnooper.addStatToSnooper("gl_caps[ARB_shadow]", Boolean.valueOf(contextcapabilities.GL_ARB_shadow));
/* 2889 */     playerSnooper.addStatToSnooper("gl_caps[ARB_shadow_ambient]", Boolean.valueOf(contextcapabilities.GL_ARB_shadow_ambient));
/* 2890 */     playerSnooper.addStatToSnooper("gl_caps[ARB_stencil_texturing]", Boolean.valueOf(contextcapabilities.GL_ARB_stencil_texturing));
/* 2891 */     playerSnooper.addStatToSnooper("gl_caps[ARB_sync]", Boolean.valueOf(contextcapabilities.GL_ARB_sync));
/* 2892 */     playerSnooper.addStatToSnooper("gl_caps[ARB_tessellation_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_tessellation_shader));
/* 2893 */     playerSnooper.addStatToSnooper("gl_caps[ARB_texture_border_clamp]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_border_clamp));
/* 2894 */     playerSnooper.addStatToSnooper("gl_caps[ARB_texture_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_buffer_object));
/* 2895 */     playerSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_cube_map));
/* 2896 */     playerSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map_array]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_cube_map_array));
/* 2897 */     playerSnooper.addStatToSnooper("gl_caps[ARB_texture_non_power_of_two]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_non_power_of_two));
/* 2898 */     playerSnooper.addStatToSnooper("gl_caps[ARB_uniform_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_uniform_buffer_object));
/* 2899 */     playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_blend]", Boolean.valueOf(contextcapabilities.GL_ARB_vertex_blend));
/* 2900 */     playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_vertex_buffer_object));
/* 2901 */     playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_program]", Boolean.valueOf(contextcapabilities.GL_ARB_vertex_program));
/* 2902 */     playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_vertex_shader));
/* 2903 */     playerSnooper.addStatToSnooper("gl_caps[EXT_bindable_uniform]", Boolean.valueOf(contextcapabilities.GL_EXT_bindable_uniform));
/* 2904 */     playerSnooper.addStatToSnooper("gl_caps[EXT_blend_equation_separate]", Boolean.valueOf(contextcapabilities.GL_EXT_blend_equation_separate));
/* 2905 */     playerSnooper.addStatToSnooper("gl_caps[EXT_blend_func_separate]", Boolean.valueOf(contextcapabilities.GL_EXT_blend_func_separate));
/* 2906 */     playerSnooper.addStatToSnooper("gl_caps[EXT_blend_minmax]", Boolean.valueOf(contextcapabilities.GL_EXT_blend_minmax));
/* 2907 */     playerSnooper.addStatToSnooper("gl_caps[EXT_blend_subtract]", Boolean.valueOf(contextcapabilities.GL_EXT_blend_subtract));
/* 2908 */     playerSnooper.addStatToSnooper("gl_caps[EXT_draw_instanced]", Boolean.valueOf(contextcapabilities.GL_EXT_draw_instanced));
/* 2909 */     playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_multisample]", Boolean.valueOf(contextcapabilities.GL_EXT_framebuffer_multisample));
/* 2910 */     playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_object]", Boolean.valueOf(contextcapabilities.GL_EXT_framebuffer_object));
/* 2911 */     playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_sRGB]", Boolean.valueOf(contextcapabilities.GL_EXT_framebuffer_sRGB));
/* 2912 */     playerSnooper.addStatToSnooper("gl_caps[EXT_geometry_shader4]", Boolean.valueOf(contextcapabilities.GL_EXT_geometry_shader4));
/* 2913 */     playerSnooper.addStatToSnooper("gl_caps[EXT_gpu_program_parameters]", Boolean.valueOf(contextcapabilities.GL_EXT_gpu_program_parameters));
/* 2914 */     playerSnooper.addStatToSnooper("gl_caps[EXT_gpu_shader4]", Boolean.valueOf(contextcapabilities.GL_EXT_gpu_shader4));
/* 2915 */     playerSnooper.addStatToSnooper("gl_caps[EXT_multi_draw_arrays]", Boolean.valueOf(contextcapabilities.GL_EXT_multi_draw_arrays));
/* 2916 */     playerSnooper.addStatToSnooper("gl_caps[EXT_packed_depth_stencil]", Boolean.valueOf(contextcapabilities.GL_EXT_packed_depth_stencil));
/* 2917 */     playerSnooper.addStatToSnooper("gl_caps[EXT_paletted_texture]", Boolean.valueOf(contextcapabilities.GL_EXT_paletted_texture));
/* 2918 */     playerSnooper.addStatToSnooper("gl_caps[EXT_rescale_normal]", Boolean.valueOf(contextcapabilities.GL_EXT_rescale_normal));
/* 2919 */     playerSnooper.addStatToSnooper("gl_caps[EXT_separate_shader_objects]", Boolean.valueOf(contextcapabilities.GL_EXT_separate_shader_objects));
/* 2920 */     playerSnooper.addStatToSnooper("gl_caps[EXT_shader_image_load_store]", Boolean.valueOf(contextcapabilities.GL_EXT_shader_image_load_store));
/* 2921 */     playerSnooper.addStatToSnooper("gl_caps[EXT_shadow_funcs]", Boolean.valueOf(contextcapabilities.GL_EXT_shadow_funcs));
/* 2922 */     playerSnooper.addStatToSnooper("gl_caps[EXT_shared_texture_palette]", Boolean.valueOf(contextcapabilities.GL_EXT_shared_texture_palette));
/* 2923 */     playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_clear_tag]", Boolean.valueOf(contextcapabilities.GL_EXT_stencil_clear_tag));
/* 2924 */     playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_two_side]", Boolean.valueOf(contextcapabilities.GL_EXT_stencil_two_side));
/* 2925 */     playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_wrap]", Boolean.valueOf(contextcapabilities.GL_EXT_stencil_wrap));
/* 2926 */     playerSnooper.addStatToSnooper("gl_caps[EXT_texture_3d]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_3d));
/* 2927 */     playerSnooper.addStatToSnooper("gl_caps[EXT_texture_array]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_array));
/* 2928 */     playerSnooper.addStatToSnooper("gl_caps[EXT_texture_buffer_object]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_buffer_object));
/* 2929 */     playerSnooper.addStatToSnooper("gl_caps[EXT_texture_integer]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_integer));
/* 2930 */     playerSnooper.addStatToSnooper("gl_caps[EXT_texture_lod_bias]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_lod_bias));
/* 2931 */     playerSnooper.addStatToSnooper("gl_caps[EXT_texture_sRGB]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_sRGB));
/* 2932 */     playerSnooper.addStatToSnooper("gl_caps[EXT_vertex_shader]", Boolean.valueOf(contextcapabilities.GL_EXT_vertex_shader));
/* 2933 */     playerSnooper.addStatToSnooper("gl_caps[EXT_vertex_weighting]", Boolean.valueOf(contextcapabilities.GL_EXT_vertex_weighting));
/* 2934 */     playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_uniforms]", Integer.valueOf(GL11.glGetInteger(35658)));
/* 2935 */     GL11.glGetError();
/* 2936 */     playerSnooper.addStatToSnooper("gl_caps[gl_max_fragment_uniforms]", Integer.valueOf(GL11.glGetInteger(35657)));
/* 2937 */     GL11.glGetError();
/* 2938 */     playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_attribs]", Integer.valueOf(GL11.glGetInteger(34921)));
/* 2939 */     GL11.glGetError();
/* 2940 */     playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_texture_image_units]", Integer.valueOf(GL11.glGetInteger(35660)));
/* 2941 */     GL11.glGetError();
/* 2942 */     playerSnooper.addStatToSnooper("gl_caps[gl_max_texture_image_units]", Integer.valueOf(GL11.glGetInteger(34930)));
/* 2943 */     GL11.glGetError();
/* 2944 */     playerSnooper.addStatToSnooper("gl_caps[gl_max_texture_image_units]", Integer.valueOf(GL11.glGetInteger(35071)));
/* 2945 */     GL11.glGetError();
/* 2946 */     playerSnooper.addStatToSnooper("gl_max_texture_size", Integer.valueOf(getGLMaximumTextureSize()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int getGLMaximumTextureSize()
/*      */   {
/* 2954 */     for (int i = 16384; i > 0; i >>= 1)
/*      */     {
/* 2956 */       GL11.glTexImage2D(32868, 0, 6408, i, i, 0, 6408, 5121, null);
/* 2957 */       int j = GL11.glGetTexLevelParameteri(32868, 0, 4096);
/*      */       
/* 2959 */       if (j != 0)
/*      */       {
/* 2961 */         return i;
/*      */       }
/*      */     }
/*      */     
/* 2965 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSnooperEnabled()
/*      */   {
/* 2973 */     return this.gameSettings.snooperEnabled;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setServerData(ServerData serverDataIn)
/*      */   {
/* 2981 */     this.currentServerData = serverDataIn;
/*      */   }
/*      */   
/*      */   public ServerData getCurrentServerData()
/*      */   {
/* 2986 */     return this.currentServerData;
/*      */   }
/*      */   
/*      */   public boolean isIntegratedServerRunning()
/*      */   {
/* 2991 */     return this.integratedServerIsRunning;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSingleplayer()
/*      */   {
/* 2999 */     return (this.integratedServerIsRunning) && (this.theIntegratedServer != null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public IntegratedServer getIntegratedServer()
/*      */   {
/* 3007 */     return this.theIntegratedServer;
/*      */   }
/*      */   
/*      */   public static void stopIntegratedServer()
/*      */   {
/* 3012 */     if (theMinecraft != null)
/*      */     {
/* 3014 */       IntegratedServer integratedserver = theMinecraft.getIntegratedServer();
/*      */       
/* 3016 */       if (integratedserver != null)
/*      */       {
/* 3018 */         integratedserver.stopServer();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public PlayerUsageSnooper getPlayerUsageSnooper()
/*      */   {
/* 3028 */     return this.usageSnooper;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static long getSystemTime()
/*      */   {
/* 3036 */     return Sys.getTime() * 1000L / Sys.getTimerResolution();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isFullScreen()
/*      */   {
/* 3044 */     return this.fullscreen;
/*      */   }
/*      */   
/*      */   public Session getSession()
/*      */   {
/* 3049 */     return this.session;
/*      */   }
/*      */   
/*      */   public PropertyMap getTwitchDetails()
/*      */   {
/* 3054 */     return this.twitchDetails;
/*      */   }
/*      */   
/*      */   public PropertyMap func_181037_M()
/*      */   {
/* 3059 */     if (this.field_181038_N.isEmpty())
/*      */     {
/* 3061 */       GameProfile gameprofile = getSessionService().fillProfileProperties(this.session.getProfile(), false);
/* 3062 */       this.field_181038_N.putAll(gameprofile.getProperties());
/*      */     }
/*      */     
/* 3065 */     return this.field_181038_N;
/*      */   }
/*      */   
/*      */   public Proxy getProxy()
/*      */   {
/* 3070 */     return this.proxy;
/*      */   }
/*      */   
/*      */   public TextureManager getTextureManager()
/*      */   {
/* 3075 */     return this.renderEngine;
/*      */   }
/*      */   
/*      */   public IResourceManager getResourceManager()
/*      */   {
/* 3080 */     return this.mcResourceManager;
/*      */   }
/*      */   
/*      */   public ResourcePackRepository getResourcePackRepository()
/*      */   {
/* 3085 */     return this.mcResourcePackRepository;
/*      */   }
/*      */   
/*      */   public LanguageManager getLanguageManager()
/*      */   {
/* 3090 */     return this.mcLanguageManager;
/*      */   }
/*      */   
/*      */   public TextureMap getTextureMapBlocks()
/*      */   {
/* 3095 */     return this.textureMapBlocks;
/*      */   }
/*      */   
/*      */   public boolean isJava64bit()
/*      */   {
/* 3100 */     return this.jvm64bit;
/*      */   }
/*      */   
/*      */   public boolean isGamePaused()
/*      */   {
/* 3105 */     return this.isGamePaused;
/*      */   }
/*      */   
/*      */   public SoundHandler getSoundHandler()
/*      */   {
/* 3110 */     return this.mcSoundHandler;
/*      */   }
/*      */   
/*      */   public MusicTicker.MusicType getAmbientMusicType()
/*      */   {
/* 3115 */     return this.thePlayer != null ? MusicTicker.MusicType.GAME : (this.thePlayer.capabilities.isCreativeMode) && (this.thePlayer.capabilities.allowFlying) ? MusicTicker.MusicType.CREATIVE : (this.thePlayer.worldObj.provider instanceof WorldProviderEnd) ? MusicTicker.MusicType.END : (BossStatus.bossName != null) && (BossStatus.statusBarTime > 0) ? MusicTicker.MusicType.END_BOSS : (this.thePlayer.worldObj.provider instanceof WorldProviderHell) ? MusicTicker.MusicType.NETHER : MusicTicker.MusicType.MENU;
/*      */   }
/*      */   
/*      */   public IStream getTwitchStream()
/*      */   {
/* 3120 */     return this.stream;
/*      */   }
/*      */   
/*      */   public void dispatchKeypresses()
/*      */   {
/* 3125 */     int i = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() : Keyboard.getEventKey();
/*      */     
/* 3127 */     if ((i != 0) && (!Keyboard.isRepeatEvent()))
/*      */     {
/* 3129 */       if ((!(this.currentScreen instanceof GuiControls)) || (((GuiControls)this.currentScreen).time <= getSystemTime() - 20L))
/*      */       {
/* 3131 */         if (Keyboard.getEventKeyState())
/*      */         {
/* 3133 */           if (i == this.gameSettings.keyBindStreamStartStop.getKeyCode())
/*      */           {
/* 3135 */             if (getTwitchStream().isBroadcasting())
/*      */             {
/* 3137 */               getTwitchStream().stopBroadcasting();
/*      */             }
/* 3139 */             else if (getTwitchStream().isReadyToBroadcast())
/*      */             {
/* 3141 */               displayGuiScreen(new GuiYesNo(new GuiYesNoCallback()
/*      */               {
/*      */                 public void confirmClicked(boolean result, int id)
/*      */                 {
/* 3145 */                   if (result)
/*      */                   {
/* 3147 */                     Minecraft.this.getTwitchStream().func_152930_t();
/*      */                   }
/*      */                   
/* 3150 */                   Minecraft.this.displayGuiScreen(null);
/*      */                 }
/* 3152 */               }, I18n.format("stream.confirm_start", new Object[0]), "", 0));
/*      */             }
/* 3154 */             else if ((getTwitchStream().func_152928_D()) && (getTwitchStream().func_152936_l()))
/*      */             {
/* 3156 */               if (this.theWorld != null)
/*      */               {
/* 3158 */                 this.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("Not ready to start streaming yet!"));
/*      */               }
/*      */               
/*      */             }
/*      */             else {
/* 3163 */               GuiStreamUnavailable.func_152321_a(this.currentScreen);
/*      */             }
/*      */           }
/* 3166 */           else if (i == this.gameSettings.keyBindStreamPauseUnpause.getKeyCode())
/*      */           {
/* 3168 */             if (getTwitchStream().isBroadcasting())
/*      */             {
/* 3170 */               if (getTwitchStream().isPaused())
/*      */               {
/* 3172 */                 getTwitchStream().unpause();
/*      */               }
/*      */               else
/*      */               {
/* 3176 */                 getTwitchStream().pause();
/*      */               }
/*      */             }
/*      */           }
/* 3180 */           else if (i == this.gameSettings.keyBindStreamCommercials.getKeyCode())
/*      */           {
/* 3182 */             if (getTwitchStream().isBroadcasting())
/*      */             {
/* 3184 */               getTwitchStream().requestCommercial();
/*      */             }
/*      */           }
/* 3187 */           else if (i == this.gameSettings.keyBindStreamToggleMic.getKeyCode())
/*      */           {
/* 3189 */             this.stream.muteMicrophone(true);
/*      */           }
/* 3191 */           else if (i == this.gameSettings.keyBindFullscreen.getKeyCode())
/*      */           {
/* 3193 */             toggleFullscreen();
/*      */           }
/* 3195 */           else if (i == this.gameSettings.keyBindScreenshot.getKeyCode())
/*      */           {
/* 3197 */             this.ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot(this.mcDataDir, this.displayWidth, this.displayHeight, this.framebufferMc));
/*      */           }
/*      */         }
/* 3200 */         else if (i == this.gameSettings.keyBindStreamToggleMic.getKeyCode())
/*      */         {
/* 3202 */           this.stream.muteMicrophone(false);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public MinecraftSessionService getSessionService()
/*      */   {
/* 3210 */     return this.sessionService;
/*      */   }
/*      */   
/*      */   public SkinManager getSkinManager()
/*      */   {
/* 3215 */     return this.skinManager;
/*      */   }
/*      */   
/*      */   public Entity getRenderViewEntity()
/*      */   {
/* 3220 */     return this.renderViewEntity;
/*      */   }
/*      */   
/*      */   public void setRenderViewEntity(Entity viewingEntity)
/*      */   {
/* 3225 */     this.renderViewEntity = viewingEntity;
/* 3226 */     this.entityRenderer.loadEntityShader(viewingEntity);
/*      */   }
/*      */   
/*      */   public <V> ListenableFuture<V> addScheduledTask(Callable<V> callableToSchedule)
/*      */   {
/* 3231 */     Validate.notNull(callableToSchedule);
/*      */     
/* 3233 */     if (!isCallingFromMinecraftThread())
/*      */     {
/* 3235 */       ListenableFutureTask<V> listenablefuturetask = ListenableFutureTask.create(callableToSchedule);
/*      */       
/* 3237 */       synchronized (this.scheduledTasks)
/*      */       {
/* 3239 */         this.scheduledTasks.add(listenablefuturetask);
/* 3240 */         return listenablefuturetask;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 3247 */       return Futures.immediateFuture(callableToSchedule.call());
/*      */     }
/*      */     catch (Exception exception)
/*      */     {
/* 3251 */       return Futures.immediateFailedCheckedFuture(exception);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule)
/*      */   {
/* 3258 */     Validate.notNull(runnableToSchedule);
/* 3259 */     return addScheduledTask(Executors.callable(runnableToSchedule));
/*      */   }
/*      */   
/*      */   public boolean isCallingFromMinecraftThread()
/*      */   {
/* 3264 */     return Thread.currentThread() == this.mcThread;
/*      */   }
/*      */   
/*      */   public BlockRendererDispatcher getBlockRendererDispatcher()
/*      */   {
/* 3269 */     return this.blockRenderDispatcher;
/*      */   }
/*      */   
/*      */   public RenderManager getRenderManager()
/*      */   {
/* 3274 */     return this.renderManager;
/*      */   }
/*      */   
/*      */   public RenderItem getRenderItem()
/*      */   {
/* 3279 */     return this.renderItem;
/*      */   }
/*      */   
/*      */   public ItemRenderer getItemRenderer()
/*      */   {
/* 3284 */     return this.itemRenderer;
/*      */   }
/*      */   
/*      */   public static int getDebugFPS()
/*      */   {
/* 3289 */     return debugFPS;
/*      */   }
/*      */   
/*      */   public FrameTimer func_181539_aj()
/*      */   {
/* 3294 */     return this.field_181542_y;
/*      */   }
/*      */   
/*      */   public static Map<String, String> getSessionInfo()
/*      */   {
/* 3299 */     Map<String, String> map = Maps.newHashMap();
/* 3300 */     map.put("X-Minecraft-Username", getMinecraft().getSession().getUsername());
/* 3301 */     map.put("X-Minecraft-UUID", getMinecraft().getSession().getPlayerID());
/* 3302 */     map.put("X-Minecraft-Version", "1.8.8");
/* 3303 */     return map;
/*      */   }
/*      */   
/*      */   public boolean func_181540_al()
/*      */   {
/* 3308 */     return this.field_181541_X;
/*      */   }
/*      */   
/*      */   public void func_181537_a(boolean p_181537_1_)
/*      */   {
/* 3313 */     this.field_181541_X = p_181537_1_;
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\Minecraft.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */