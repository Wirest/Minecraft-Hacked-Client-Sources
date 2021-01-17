// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client;

import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.Maps;
import java.util.concurrent.Executors;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFutureTask;
import org.apache.commons.lang3.Validate;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.client.gui.stream.GuiStreamUnavailable;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import com.mojang.authlib.GameProfile;
import com.google.common.collect.Multimap;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GLContext;
import java.nio.ByteOrder;
import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.init.Items;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.stats.StatFileWriter;
import java.net.SocketAddress;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.WorldSettings;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C16PacketClientStatus;
import me.nico.hush.modules.Module;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import java.util.concurrent.Callable;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.settings.KeyBinding;
import java.text.DecimalFormat;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.gui.ScaledResolution;
import java.util.Set;
import com.google.common.collect.Sets;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.PixelFormat;
import net.minecraft.client.stream.NullStream;
import net.minecraft.client.stream.TwitchStream;
import com.google.common.collect.Iterables;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
import net.minecraft.client.resources.data.IMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import java.io.IOException;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.Display;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.stats.IStatStringFormat;
import net.minecraft.stats.AchievementList;
import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.GrassColorReloadListener;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.Sys;
import net.minecraft.util.ReportedException;
import net.minecraft.util.MinecraftError;
import net.minecraft.client.gui.GuiMemoryErrorScreen;
import net.minecraft.init.Bootstrap;
import javax.imageio.ImageIO;
import com.darkmagician6.eventapi.EventManager;
import me.nico.hush.Client;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.util.UUID;
import java.util.Map;
import net.minecraft.client.resources.ResourceIndex;
import com.google.common.collect.Queues;
import net.minecraft.server.MinecraftServer;
import net.minecraft.client.main.GameConfiguration;
import com.google.common.collect.Lists;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.resources.model.ModelManager;
import java.util.concurrent.FutureTask;
import java.util.Queue;
import net.minecraft.client.resources.SkinManager;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.stream.IStream;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.profiler.Profiler;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.FrameTimer;
import net.minecraft.world.storage.ISaveFormat;
import java.net.Proxy;
import net.minecraft.util.MouseHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.Session;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.util.Timer;
import net.minecraft.crash.CrashReport;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.multiplayer.ServerData;
import com.mojang.authlib.properties.PropertyMap;
import java.io.File;
import org.lwjgl.opengl.DisplayMode;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;
import net.minecraft.profiler.IPlayerUsage;
import net.minecraft.util.IThreadListener;

public class Minecraft implements IThreadListener, IPlayerUsage
{
    private static final Logger logger;
    private static final ResourceLocation locationMojangPng;
    public static final boolean isRunningOnMac;
    public static byte[] memoryReserve;
    private static final List<DisplayMode> macDisplayModes;
    private final File fileResourcepacks;
    private final PropertyMap twitchDetails;
    private final PropertyMap field_181038_N;
    private ServerData currentServerData;
    private TextureManager renderEngine;
    private static Minecraft theMinecraft;
    public PlayerControllerMP playerController;
    private boolean fullscreen;
    private boolean enableGLErrorChecking;
    private boolean hasCrashed;
    private CrashReport crashReporter;
    public static int displayWidth;
    public static int displayHeight;
    private boolean field_181541_X;
    public Timer timer;
    private PlayerUsageSnooper usageSnooper;
    public WorldClient theWorld;
    public RenderGlobal renderGlobal;
    public RenderManager renderManager;
    private RenderItem renderItem;
    private ItemRenderer itemRenderer;
    public static EntityPlayerSP thePlayer;
    private Entity renderViewEntity;
    public Entity pointedEntity;
    public EffectRenderer effectRenderer;
    public Session session;
    private boolean isGamePaused;
    public FontRenderer fontRendererObj;
    public FontRenderer standardGalacticFontRenderer;
    public GuiScreen currentScreen;
    public LoadingScreenRenderer loadingScreen;
    public EntityRenderer entityRenderer;
    private int leftClickCounter;
    private int tempDisplayWidth;
    private int tempDisplayHeight;
    private IntegratedServer theIntegratedServer;
    public GuiAchievement guiAchievement;
    public GuiIngame ingameGUI;
    public boolean skipRenderWorld;
    public MovingObjectPosition objectMouseOver;
    public GameSettings gameSettings;
    public MouseHelper mouseHelper;
    public final File mcDataDir;
    private final File fileAssets;
    private final String launchedVersion;
    private final Proxy proxy;
    private ISaveFormat saveLoader;
    private static int debugFPS;
    public int rightClickDelayTimer;
    private String serverName;
    private int serverPort;
    public boolean inGameHasFocus;
    long systemTime;
    private int joinPlayerCounter;
    public final FrameTimer field_181542_y;
    long field_181543_z;
    private final boolean jvm64bit;
    private final boolean isDemo;
    private NetworkManager myNetworkManager;
    private boolean integratedServerIsRunning;
    public final Profiler mcProfiler;
    private long debugCrashKeyPressTime;
    private IReloadableResourceManager mcResourceManager;
    private final IMetadataSerializer metadataSerializer_;
    private final List<IResourcePack> defaultResourcePacks;
    private final DefaultResourcePack mcDefaultResourcePack;
    private ResourcePackRepository mcResourcePackRepository;
    public LanguageManager mcLanguageManager;
    private IStream stream;
    private Framebuffer framebufferMc;
    private TextureMap textureMapBlocks;
    private SoundHandler mcSoundHandler;
    private MusicTicker mcMusicTicker;
    private ResourceLocation mojangLogo;
    private final MinecraftSessionService sessionService;
    private SkinManager skinManager;
    private final Queue<FutureTask<?>> scheduledTasks;
    private long field_175615_aJ;
    private final Thread mcThread;
    private ModelManager modelManager;
    private BlockRendererDispatcher blockRenderDispatcher;
    volatile boolean running;
    public String debug;
    public boolean field_175613_B;
    public boolean field_175614_C;
    public boolean field_175611_D;
    public boolean renderChunksMany;
    long debugUpdateTime;
    int fpsCounter;
    long prevFrameTime;
    private String debugProfilerName;
    
    static {
        logger = LogManager.getLogger();
        locationMojangPng = new ResourceLocation("textures/gui/title/mojang.png");
        isRunningOnMac = (Util.getOSType() == Util.EnumOS.OSX);
        Minecraft.memoryReserve = new byte[10485760];
        macDisplayModes = Lists.newArrayList(new DisplayMode(2560, 1600), new DisplayMode(2880, 1800));
    }
    
    public Minecraft(final GameConfiguration gameConfig) {
        this.enableGLErrorChecking = true;
        this.field_181541_X = false;
        this.timer = new Timer(20.0f);
        this.usageSnooper = new PlayerUsageSnooper("client", this, MinecraftServer.getCurrentTimeMillis());
        this.systemTime = getSystemTime();
        this.field_181542_y = new FrameTimer();
        this.field_181543_z = System.nanoTime();
        this.mcProfiler = new Profiler();
        this.debugCrashKeyPressTime = -1L;
        this.metadataSerializer_ = new IMetadataSerializer();
        this.defaultResourcePacks = (List<IResourcePack>)Lists.newArrayList();
        this.scheduledTasks = (Queue<FutureTask<?>>)Queues.newArrayDeque();
        this.field_175615_aJ = 0L;
        this.mcThread = Thread.currentThread();
        this.running = true;
        this.debug = "";
        this.field_175613_B = false;
        this.field_175614_C = false;
        this.field_175611_D = false;
        this.renderChunksMany = true;
        this.debugUpdateTime = getSystemTime();
        this.prevFrameTime = -1L;
        this.debugProfilerName = "root";
        Minecraft.theMinecraft = this;
        this.mcDataDir = gameConfig.folderInfo.mcDataDir;
        this.fileAssets = gameConfig.folderInfo.assetsDir;
        this.fileResourcepacks = gameConfig.folderInfo.resourcePacksDir;
        this.launchedVersion = gameConfig.gameInfo.version;
        this.twitchDetails = gameConfig.userInfo.userProperties;
        this.field_181038_N = gameConfig.userInfo.field_181172_c;
        this.mcDefaultResourcePack = new DefaultResourcePack(new ResourceIndex(gameConfig.folderInfo.assetsDir, gameConfig.folderInfo.assetIndex).getResourceMap());
        this.proxy = ((gameConfig.userInfo.proxy == null) ? Proxy.NO_PROXY : gameConfig.userInfo.proxy);
        this.sessionService = new YggdrasilAuthenticationService(gameConfig.userInfo.proxy, UUID.randomUUID().toString()).createMinecraftSessionService();
        this.session = gameConfig.userInfo.session;
        Minecraft.logger.info("Setting user: " + this.session.getUsername());
        Minecraft.logger.info("(Session ID is " + this.session.getSessionID() + ")");
        this.isDemo = gameConfig.gameInfo.isDemo;
        Minecraft.displayWidth = ((gameConfig.displayInfo.width > 0) ? gameConfig.displayInfo.width : 1);
        Minecraft.displayHeight = ((gameConfig.displayInfo.height > 0) ? gameConfig.displayInfo.height : 1);
        this.tempDisplayWidth = gameConfig.displayInfo.width;
        this.tempDisplayHeight = gameConfig.displayInfo.height;
        this.fullscreen = gameConfig.displayInfo.fullscreen;
        this.jvm64bit = isJvm64bit();
        this.theIntegratedServer = new IntegratedServer(this);
        if (gameConfig.serverInfo.serverName != null) {
            this.serverName = gameConfig.serverInfo.serverName;
            this.serverPort = gameConfig.serverInfo.serverPort;
            EventManager.register(new Client());
        }
        ImageIO.setUseCache(false);
        Bootstrap.register();
    }
    
    public void run() {
        this.running = true;
        try {
            this.startGame();
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Initializing game");
            crashreport.makeCategory("Initialization");
            this.displayCrashReport(this.addGraphicsAndWorldToCrashReport(crashreport));
            return;
        }
        try {
            do {
                if (this.hasCrashed) {
                    if (this.crashReporter != null) {
                        this.displayCrashReport(this.crashReporter);
                        continue;
                    }
                }
                try {
                    this.runGameLoop();
                }
                catch (OutOfMemoryError var10) {
                    this.freeMemory();
                    this.displayGuiScreen(new GuiMemoryErrorScreen());
                    System.gc();
                }
            } while (this.running);
        }
        catch (MinecraftError var11) {}
        catch (ReportedException reportedexception) {
            this.addGraphicsAndWorldToCrashReport(reportedexception.getCrashReport());
            this.freeMemory();
            Minecraft.logger.fatal("Reported exception thrown!", reportedexception);
            this.displayCrashReport(reportedexception.getCrashReport());
        }
        catch (Throwable throwable2) {
            final CrashReport crashreport2 = this.addGraphicsAndWorldToCrashReport(new CrashReport("Unexpected error", throwable2));
            this.freeMemory();
            Minecraft.logger.fatal("Unreported exception thrown!", throwable2);
            this.displayCrashReport(crashreport2);
        }
        finally {
            this.shutdownMinecraftApplet();
        }
        this.shutdownMinecraftApplet();
    }
    
    private void startGame() throws LWJGLException, IOException {
        this.gameSettings = new GameSettings(this, this.mcDataDir);
        this.defaultResourcePacks.add(this.mcDefaultResourcePack);
        this.startTimerHackThread();
        if (this.gameSettings.overrideHeight > 0 && this.gameSettings.overrideWidth > 0) {
            Minecraft.displayWidth = this.gameSettings.overrideWidth;
            Minecraft.displayHeight = this.gameSettings.overrideHeight;
        }
        Minecraft.logger.info("LWJGL Version: " + Sys.getVersion());
        this.setWindowIcon();
        this.setInitialDisplayMode();
        this.createDisplay();
        OpenGlHelper.initializeTextures();
        (this.framebufferMc = new Framebuffer(Minecraft.displayWidth, Minecraft.displayHeight, true)).setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);
        this.registerMetadataSerializers();
        this.mcResourcePackRepository = new ResourcePackRepository(this.fileResourcepacks, new File(this.mcDataDir, "server-resource-packs"), this.mcDefaultResourcePack, this.metadataSerializer_, this.gameSettings);
        this.mcResourceManager = new SimpleReloadableResourceManager(this.metadataSerializer_);
        this.mcLanguageManager = new LanguageManager(this.metadataSerializer_, this.gameSettings.language);
        this.mcResourceManager.registerReloadListener(this.mcLanguageManager);
        this.refreshResources();
        this.renderEngine = new TextureManager(this.mcResourceManager);
        this.mcResourceManager.registerReloadListener(this.renderEngine);
        this.drawSplashScreen(this.renderEngine);
        this.initStream();
        this.skinManager = new SkinManager(this.renderEngine, new File(this.fileAssets, "skins"), this.sessionService);
        this.saveLoader = new AnvilSaveConverter(new File(this.mcDataDir, "saves"));
        this.mcSoundHandler = new SoundHandler(this.mcResourceManager, this.gameSettings);
        this.mcResourceManager.registerReloadListener(this.mcSoundHandler);
        this.mcMusicTicker = new MusicTicker(this);
        this.fontRendererObj = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii.png"), this.renderEngine, false);
        if (this.gameSettings.language != null) {
            this.fontRendererObj.setUnicodeFlag(this.isUnicode());
            this.fontRendererObj.setBidiFlag(this.mcLanguageManager.isCurrentLanguageBidirectional());
        }
        this.standardGalacticFontRenderer = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii_sga.png"), this.renderEngine, false);
        this.mcResourceManager.registerReloadListener(this.fontRendererObj);
        this.mcResourceManager.registerReloadListener(this.standardGalacticFontRenderer);
        this.mcResourceManager.registerReloadListener(new GrassColorReloadListener());
        this.mcResourceManager.registerReloadListener(new FoliageColorReloadListener());
        AchievementList.openInventory.setStatStringFormatter(new IStatStringFormat() {
            @Override
            public String formatString(final String p_74535_1_) {
                try {
                    return String.format(p_74535_1_, GameSettings.getKeyDisplayString(Minecraft.this.gameSettings.keyBindInventory.getKeyCode()));
                }
                catch (Exception exception) {
                    return "Error: " + exception.getLocalizedMessage();
                }
            }
        });
        this.mouseHelper = new MouseHelper();
        this.checkGLError("Pre startup");
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(7425);
        GlStateManager.clearDepth(1.0);
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.cullFace(1029);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        this.checkGLError("Startup");
        (this.textureMapBlocks = new TextureMap("textures")).setMipmapLevels(this.gameSettings.mipmapLevels);
        this.renderEngine.loadTickableTexture(TextureMap.locationBlocksTexture, this.textureMapBlocks);
        this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        this.textureMapBlocks.setBlurMipmapDirect(false, this.gameSettings.mipmapLevels > 0);
        this.modelManager = new ModelManager(this.textureMapBlocks);
        this.mcResourceManager.registerReloadListener(this.modelManager);
        this.renderItem = new RenderItem(this.renderEngine, this.modelManager);
        this.renderManager = new RenderManager(this.renderEngine, this.renderItem);
        this.itemRenderer = new ItemRenderer(this);
        this.mcResourceManager.registerReloadListener(this.renderItem);
        this.entityRenderer = new EntityRenderer(this, this.mcResourceManager);
        this.mcResourceManager.registerReloadListener(this.entityRenderer);
        this.blockRenderDispatcher = new BlockRendererDispatcher(this.modelManager.getBlockModelShapes(), this.gameSettings);
        this.mcResourceManager.registerReloadListener(this.blockRenderDispatcher);
        this.renderGlobal = new RenderGlobal(this);
        this.mcResourceManager.registerReloadListener(this.renderGlobal);
        this.guiAchievement = new GuiAchievement(this);
        GlStateManager.viewport(0, 0, Minecraft.displayWidth, Minecraft.displayHeight);
        this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);
        this.checkGLError("Post startup");
        this.ingameGUI = new GuiIngame(this);
        new Client().startClient();
        if (this.serverName != null) {
            this.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), this, this.serverName, this.serverPort));
        }
        else {
            this.displayGuiScreen(new GuiMainMenu());
        }
        this.renderEngine.deleteTexture(this.mojangLogo);
        this.mojangLogo = null;
        this.loadingScreen = new LoadingScreenRenderer(this);
        if (this.gameSettings.fullScreen && !this.fullscreen) {
            this.toggleFullscreen();
        }
        try {
            Display.setVSyncEnabled(this.gameSettings.enableVsync);
        }
        catch (OpenGLException var2) {
            this.gameSettings.enableVsync = false;
            this.gameSettings.saveOptions();
        }
        this.renderGlobal.makeEntityOutlineShader();
    }
    
    private void registerMetadataSerializers() {
        this.metadataSerializer_.registerMetadataSectionType(new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType(new FontMetadataSectionSerializer(), FontMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType(new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType(new PackMetadataSectionSerializer(), PackMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType(new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
    }
    
    private void initStream() {
        try {
            this.stream = new TwitchStream(this, Iterables.getFirst((Iterable<? extends Property>)((ForwardingMultimap<String, Object>)this.twitchDetails).get("twitch_access_token"), (Property)null));
        }
        catch (Throwable throwable) {
            this.stream = new NullStream(throwable);
            Minecraft.logger.error("Couldn't initialize twitch stream");
        }
    }
    
    private void createDisplay() throws LWJGLException {
        Display.setResizable(true);
        Display.setTitle("Zodiac is preparing to launch...");
        try {
            Display.create(new PixelFormat().withDepthBits(24));
        }
        catch (LWJGLException lwjglexception) {
            Minecraft.logger.error("Couldn't set pixel format", lwjglexception);
            try {
                Thread.sleep(1000L);
            }
            catch (InterruptedException ex) {}
            if (this.fullscreen) {
                this.updateDisplayMode();
            }
            Display.create();
        }
    }
    
    private void setInitialDisplayMode() throws LWJGLException {
        if (this.fullscreen) {
            Display.setFullscreen(true);
            final DisplayMode displaymode = Display.getDisplayMode();
            Minecraft.displayWidth = Math.max(1, displaymode.getWidth());
            Minecraft.displayHeight = Math.max(1, displaymode.getHeight());
        }
        else {
            Display.setDisplayMode(new DisplayMode(Minecraft.displayWidth, Minecraft.displayHeight));
        }
    }
    
    private void setWindowIcon() {
        final Util.EnumOS util$enumos = Util.getOSType();
        if (util$enumos != Util.EnumOS.OSX) {
            InputStream inputstream = null;
            InputStream inputstream2 = null;
            try {
                inputstream = this.mcDefaultResourcePack.getInputStreamAssets(new ResourceLocation("icons/icon_16x16.png"));
                inputstream2 = this.mcDefaultResourcePack.getInputStreamAssets(new ResourceLocation("icons/icon_32x32.png"));
                if (inputstream != null && inputstream2 != null) {
                    Display.setIcon(new ByteBuffer[] { this.readImageToBuffer(inputstream), this.readImageToBuffer(inputstream2) });
                }
            }
            catch (IOException ioexception) {
                Minecraft.logger.error("Couldn't set icon", ioexception);
                return;
            }
            finally {
                IOUtils.closeQuietly(inputstream);
                IOUtils.closeQuietly(inputstream2);
            }
            IOUtils.closeQuietly(inputstream);
            IOUtils.closeQuietly(inputstream2);
        }
    }
    
    private static boolean isJvm64bit() {
        final String[] astring = { "sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch" };
        String[] array;
        for (int length = (array = astring).length, i = 0; i < length; ++i) {
            final String s = array[i];
            final String s2 = System.getProperty(s);
            if (s2 != null && s2.contains("64")) {
                return true;
            }
        }
        return false;
    }
    
    public Framebuffer getFramebuffer() {
        return this.framebufferMc;
    }
    
    public String getVersion() {
        return this.launchedVersion;
    }
    
    private void startTimerHackThread() {
        final Thread thread = new Thread("Timer hack thread") {
            @Override
            public void run() {
                while (Minecraft.this.running) {
                    try {
                        Thread.sleep(2147483647L);
                    }
                    catch (InterruptedException ex) {}
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
    
    public void crashed(final CrashReport crash) {
        this.hasCrashed = true;
        this.crashReporter = crash;
    }
    
    public void displayCrashReport(final CrashReport crashReportIn) {
        final File file1 = new File(getMinecraft().mcDataDir, "crash-reports");
        final File file2 = new File(file1, "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-client.txt");
        Bootstrap.printToSYSOUT(crashReportIn.getCompleteReport());
        if (crashReportIn.getFile() != null) {
            Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + crashReportIn.getFile());
            System.exit(-1);
        }
        else if (crashReportIn.saveToFile(file2)) {
            Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + file2.getAbsolutePath());
            System.exit(-1);
        }
        else {
            Bootstrap.printToSYSOUT("#@?@# Game crashed! Crash report could not be saved. #@?@#");
            System.exit(-2);
        }
    }
    
    public boolean isUnicode() {
        return this.mcLanguageManager.isCurrentLocaleUnicode() || this.gameSettings.forceUnicodeFont;
    }
    
    public void refreshResources() {
        final List<IResourcePack> list = (List<IResourcePack>)Lists.newArrayList((Iterable<?>)this.defaultResourcePacks);
        for (final ResourcePackRepository.Entry resourcepackrepository$entry : this.mcResourcePackRepository.getRepositoryEntries()) {
            list.add(resourcepackrepository$entry.getResourcePack());
        }
        if (this.mcResourcePackRepository.getResourcePackInstance() != null) {
            list.add(this.mcResourcePackRepository.getResourcePackInstance());
        }
        try {
            this.mcResourceManager.reloadResources(list);
        }
        catch (RuntimeException runtimeexception) {
            Minecraft.logger.info("Caught error stitching, removing all assigned resourcepacks", runtimeexception);
            list.clear();
            list.addAll(this.defaultResourcePacks);
            this.mcResourcePackRepository.setRepositories(Collections.emptyList());
            this.mcResourceManager.reloadResources(list);
            this.gameSettings.resourcePacks.clear();
            this.gameSettings.field_183018_l.clear();
            this.gameSettings.saveOptions();
        }
        this.mcLanguageManager.parseLanguageMetadata(list);
        if (this.renderGlobal != null) {
            this.renderGlobal.loadRenderers();
        }
    }
    
    private ByteBuffer readImageToBuffer(final InputStream imageStream) throws IOException {
        final BufferedImage bufferedimage = ImageIO.read(imageStream);
        final int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0, bufferedimage.getWidth());
        final ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
        int[] array;
        for (int length = (array = aint).length, j = 0; j < length; ++j) {
            final int i = array[j];
            bytebuffer.putInt(i << 8 | (i >> 24 & 0xFF));
        }
        bytebuffer.flip();
        return bytebuffer;
    }
    
    private void updateDisplayMode() throws LWJGLException {
        final Set<DisplayMode> set = (Set<DisplayMode>)Sets.newHashSet();
        Collections.addAll(set, Display.getAvailableDisplayModes());
        DisplayMode displaymode = Display.getDesktopDisplayMode();
        if (!set.contains(displaymode) && Util.getOSType() == Util.EnumOS.OSX) {
            for (final DisplayMode displaymode2 : Minecraft.macDisplayModes) {
                boolean flag = true;
                for (final DisplayMode displaymode3 : set) {
                    if (displaymode3.getBitsPerPixel() == 32 && displaymode3.getWidth() == displaymode2.getWidth() && displaymode3.getHeight() == displaymode2.getHeight()) {
                        flag = false;
                        break;
                    }
                }
                if (!flag) {
                    for (final DisplayMode displaymode4 : set) {
                        if (displaymode4.getBitsPerPixel() == 32 && displaymode4.getWidth() == displaymode2.getWidth() / 2 && displaymode4.getHeight() == displaymode2.getHeight() / 2) {
                            displaymode = displaymode4;
                            break;
                        }
                    }
                }
            }
        }
        Display.setDisplayMode(displaymode);
        Minecraft.displayWidth = displaymode.getWidth();
        Minecraft.displayHeight = displaymode.getHeight();
    }
    
    private void drawSplashScreen(final TextureManager textureManagerInstance) throws LWJGLException {
        final ScaledResolution scaledresolution = new ScaledResolution(this);
        final int i = scaledresolution.getScaleFactor();
        final Framebuffer framebuffer = new Framebuffer(scaledresolution.getScaledWidth() * i, scaledresolution.getScaledHeight() * i, true);
        framebuffer.bindFramebuffer(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        InputStream inputstream = null;
        Label_0204: {
            try {
                inputstream = this.mcDefaultResourcePack.getInputStream(Minecraft.locationMojangPng);
                textureManagerInstance.bindTexture(this.mojangLogo = textureManagerInstance.getDynamicTextureLocation("logo", new DynamicTexture(ImageIO.read(inputstream))));
            }
            catch (IOException ioexception) {
                Minecraft.logger.error("Unable to load logo: " + Minecraft.locationMojangPng, ioexception);
                break Label_0204;
            }
            finally {
                IOUtils.closeQuietly(inputstream);
            }
            IOUtils.closeQuietly(inputstream);
        }
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0, Minecraft.displayHeight, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(Minecraft.displayWidth, Minecraft.displayHeight, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(Minecraft.displayWidth, 0.0, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final int j = 256;
        final int k = 256;
        this.func_181536_a((scaledresolution.getScaledWidth() - j) / 2, (scaledresolution.getScaledHeight() - k) / 2, 0, 0, j, k, 255, 255, 255, 255);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(scaledresolution.getScaledWidth() * i, scaledresolution.getScaledHeight() * i);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        this.updateDisplay();
    }
    
    public void func_181536_a(final int p_181536_1_, final int p_181536_2_, final int p_181536_3_, final int p_181536_4_, final int p_181536_5_, final int p_181536_6_, final int p_181536_7_, final int p_181536_8_, final int p_181536_9_, final int p_181536_10_) {
        final float f = 0.00390625f;
        final float f2 = 0.00390625f;
        final WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(p_181536_1_, p_181536_2_ + p_181536_6_, 0.0).tex(p_181536_3_ * f, (p_181536_4_ + p_181536_6_) * f2).color(p_181536_7_, p_181536_8_, p_181536_9_, p_181536_10_).endVertex();
        worldrenderer.pos(p_181536_1_ + p_181536_5_, p_181536_2_ + p_181536_6_, 0.0).tex((p_181536_3_ + p_181536_5_) * f, (p_181536_4_ + p_181536_6_) * f2).color(p_181536_7_, p_181536_8_, p_181536_9_, p_181536_10_).endVertex();
        worldrenderer.pos(p_181536_1_ + p_181536_5_, p_181536_2_, 0.0).tex((p_181536_3_ + p_181536_5_) * f, p_181536_4_ * f2).color(p_181536_7_, p_181536_8_, p_181536_9_, p_181536_10_).endVertex();
        worldrenderer.pos(p_181536_1_, p_181536_2_, 0.0).tex(p_181536_3_ * f, p_181536_4_ * f2).color(p_181536_7_, p_181536_8_, p_181536_9_, p_181536_10_).endVertex();
        Tessellator.getInstance().draw();
    }
    
    public ISaveFormat getSaveLoader() {
        return this.saveLoader;
    }
    
    public void displayGuiScreen(GuiScreen guiScreenIn) {
        if (this.currentScreen != null) {
            this.currentScreen.onGuiClosed();
        }
        if (guiScreenIn == null && this.theWorld == null) {
            guiScreenIn = new GuiMainMenu();
        }
        else if (guiScreenIn == null && Minecraft.thePlayer.getHealth() <= 0.0f) {
            guiScreenIn = new GuiGameOver();
        }
        if (guiScreenIn instanceof GuiMainMenu) {
            this.gameSettings.showDebugInfo = false;
            this.ingameGUI.getChatGUI().clearChatMessages();
        }
        if ((this.currentScreen = guiScreenIn) != null) {
            this.setIngameNotInFocus();
            final ScaledResolution scaledresolution = new ScaledResolution(this);
            final int i = scaledresolution.getScaledWidth();
            final int j = scaledresolution.getScaledHeight();
            guiScreenIn.setWorldAndResolution(this, i, j);
            this.skipRenderWorld = false;
        }
        else {
            this.mcSoundHandler.resumeSounds();
            this.setIngameFocus();
        }
    }
    
    private void checkGLError(final String message) {
        if (this.enableGLErrorChecking) {
            final int i = GL11.glGetError();
            if (i != 0) {
                final String s = GLU.gluErrorString(i);
                Minecraft.logger.error("########## GL ERROR ##########");
                Minecraft.logger.error("@ " + message);
                Minecraft.logger.error(String.valueOf(i) + ": " + s);
            }
        }
    }
    
    public void shutdownMinecraftApplet() {
        try {
            this.stream.shutdownStream();
            Minecraft.logger.info("Stopping!");
            try {
                this.loadWorld(null);
            }
            catch (Throwable t) {}
            this.mcSoundHandler.unloadSounds();
        }
        finally {
            Display.destroy();
            if (!this.hasCrashed) {
                System.exit(0);
            }
        }
        Display.destroy();
        if (!this.hasCrashed) {
            System.exit(0);
        }
        System.gc();
    }
    
    private void runGameLoop() throws IOException {
        final long i = System.nanoTime();
        this.mcProfiler.startSection("root");
        if (Display.isCreated() && Display.isCloseRequested()) {
            this.shutdown();
        }
        if (this.isGamePaused && this.theWorld != null) {
            final float f = this.timer.renderPartialTicks;
            this.timer.updateTimer();
            this.timer.renderPartialTicks = f;
        }
        else {
            this.timer.updateTimer();
        }
        this.mcProfiler.startSection("scheduledExecutables");
        synchronized (this.scheduledTasks) {
            while (!this.scheduledTasks.isEmpty()) {
                Util.func_181617_a(this.scheduledTasks.poll(), Minecraft.logger);
            }
        }
        // monitorexit(this.scheduledTasks)
        this.mcProfiler.endSection();
        final long l = System.nanoTime();
        this.mcProfiler.startSection("tick");
        for (int j = 0; j < this.timer.elapsedTicks; ++j) {
            this.runTick();
        }
        this.mcProfiler.endStartSection("preRenderErrors");
        final long i2 = System.nanoTime() - l;
        this.checkGLError("Pre render");
        this.mcProfiler.endStartSection("sound");
        this.mcSoundHandler.setListener(Minecraft.thePlayer, this.timer.renderPartialTicks);
        this.mcProfiler.endSection();
        this.mcProfiler.startSection("render");
        GlStateManager.pushMatrix();
        GlStateManager.clear(16640);
        this.framebufferMc.bindFramebuffer(true);
        this.mcProfiler.startSection("display");
        GlStateManager.enableTexture2D();
        if (Minecraft.thePlayer != null && Minecraft.thePlayer.isEntityInsideOpaqueBlock()) {
            this.gameSettings.thirdPersonView = 0;
        }
        this.mcProfiler.endSection();
        if (!this.skipRenderWorld) {
            this.mcProfiler.endStartSection("gameRenderer");
            this.entityRenderer.func_181560_a(this.timer.renderPartialTicks, i);
            this.mcProfiler.endSection();
        }
        this.mcProfiler.endSection();
        if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart && !this.gameSettings.hideGUI) {
            if (!this.mcProfiler.profilingEnabled) {
                this.mcProfiler.clearProfiling();
            }
            this.mcProfiler.profilingEnabled = true;
            this.displayDebugInfo(i2);
        }
        else {
            this.mcProfiler.profilingEnabled = false;
            this.prevFrameTime = System.nanoTime();
        }
        this.guiAchievement.updateAchievementWindow();
        this.framebufferMc.unbindFramebuffer();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        this.framebufferMc.framebufferRender(Minecraft.displayWidth, Minecraft.displayHeight);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        this.entityRenderer.renderStreamIndicator(this.timer.renderPartialTicks);
        GlStateManager.popMatrix();
        this.mcProfiler.startSection("root");
        this.updateDisplay();
        Thread.yield();
        this.mcProfiler.startSection("stream");
        this.mcProfiler.startSection("update");
        this.stream.func_152935_j();
        this.mcProfiler.endStartSection("submit");
        this.stream.func_152922_k();
        this.mcProfiler.endSection();
        this.mcProfiler.endSection();
        this.checkGLError("Post render");
        ++this.fpsCounter;
        this.isGamePaused = (this.isSingleplayer() && this.currentScreen != null && this.currentScreen.doesGuiPauseGame() && !this.theIntegratedServer.getPublic());
        final long k = System.nanoTime();
        this.field_181542_y.func_181747_a(k - this.field_181543_z);
        this.field_181543_z = k;
        while (getSystemTime() >= this.debugUpdateTime + 1000L) {
            Minecraft.debugFPS = this.fpsCounter;
            this.debug = String.format("%d fps (%d chunk update%s) T: %s%s%s%s%s", Minecraft.debugFPS, RenderChunk.renderChunksUpdated, (RenderChunk.renderChunksUpdated != 1) ? "s" : "", (this.gameSettings.limitFramerate == GameSettings.Options.FRAMERATE_LIMIT.getValueMax()) ? "inf" : Integer.valueOf(this.gameSettings.limitFramerate), this.gameSettings.enableVsync ? " vsync" : "", this.gameSettings.fancyGraphics ? "" : " fast", (this.gameSettings.clouds == 0) ? "" : ((this.gameSettings.clouds == 1) ? " fast-clouds" : " fancy-clouds"), OpenGlHelper.useVbo() ? " vbo" : "");
            RenderChunk.renderChunksUpdated = 0;
            this.debugUpdateTime += 1000L;
            this.fpsCounter = 0;
            this.usageSnooper.addMemoryStatsToSnooper();
            if (!this.usageSnooper.isSnooperRunning()) {
                this.usageSnooper.startSnooper();
            }
        }
        if (this.isFramerateLimitBelowMax()) {
            this.mcProfiler.startSection("fpslimit_wait");
            Display.sync(this.getLimitFramerate());
            this.mcProfiler.endSection();
        }
        this.mcProfiler.endSection();
    }
    
    public void updateDisplay() {
        this.mcProfiler.startSection("display_update");
        Display.update();
        this.mcProfiler.endSection();
        this.checkWindowResize();
    }
    
    protected void checkWindowResize() {
        if (!this.fullscreen && Display.wasResized()) {
            final int i = Minecraft.displayWidth;
            final int j = Minecraft.displayHeight;
            Minecraft.displayWidth = Display.getWidth();
            Minecraft.displayHeight = Display.getHeight();
            if (Minecraft.displayWidth != i || Minecraft.displayHeight != j) {
                if (Minecraft.displayWidth <= 0) {
                    Minecraft.displayWidth = 1;
                }
                if (Minecraft.displayHeight <= 0) {
                    Minecraft.displayHeight = 1;
                }
                this.resize(Minecraft.displayWidth, Minecraft.displayHeight);
            }
        }
    }
    
    public int getLimitFramerate() {
        return (this.theWorld == null && this.currentScreen != null) ? 30 : this.gameSettings.limitFramerate;
    }
    
    public boolean isFramerateLimitBelowMax() {
        return this.getLimitFramerate() < GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
    }
    
    public void freeMemory() {
        try {
            Minecraft.memoryReserve = new byte[0];
            this.renderGlobal.deleteAllDisplayLists();
        }
        catch (Throwable t) {}
        try {
            System.gc();
            this.loadWorld(null);
        }
        catch (Throwable t2) {}
        System.gc();
    }
    
    private void updateDebugProfilerName(int keyCount) {
        final List<Profiler.Result> list = (List<Profiler.Result>)this.mcProfiler.getProfilingData(this.debugProfilerName);
        if (list != null && !list.isEmpty()) {
            final Profiler.Result profiler$result = list.remove(0);
            if (keyCount == 0) {
                if (profiler$result.field_76331_c.length() > 0) {
                    final int i = this.debugProfilerName.lastIndexOf(".");
                    if (i >= 0) {
                        this.debugProfilerName = this.debugProfilerName.substring(0, i);
                    }
                }
            }
            else if (--keyCount < list.size() && !list.get(keyCount).field_76331_c.equals("unspecified")) {
                if (this.debugProfilerName.length() > 0) {
                    this.debugProfilerName = String.valueOf(this.debugProfilerName) + ".";
                }
                this.debugProfilerName = String.valueOf(this.debugProfilerName) + list.get(keyCount).field_76331_c;
            }
        }
    }
    
    private void displayDebugInfo(final long elapsedTicksTime) {
        if (this.mcProfiler.profilingEnabled) {
            final List<Profiler.Result> list = (List<Profiler.Result>)this.mcProfiler.getProfilingData(this.debugProfilerName);
            final Profiler.Result profiler$result = list.remove(0);
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.enableColorMaterial();
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0, Minecraft.displayWidth, Minecraft.displayHeight, 0.0, 1000.0, 3000.0);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0f, 0.0f, -2000.0f);
            GL11.glLineWidth(1.0f);
            GlStateManager.disableTexture2D();
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            final int i = 160;
            final int j = Minecraft.displayWidth - i - 10;
            final int k = Minecraft.displayHeight - i * 2;
            GlStateManager.enableBlend();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos(j - i * 1.1f, k - i * 0.6f - 16.0f, 0.0).color(200, 0, 0, 0).endVertex();
            worldrenderer.pos(j - i * 1.1f, k + i * 2, 0.0).color(200, 0, 0, 0).endVertex();
            worldrenderer.pos(j + i * 1.1f, k + i * 2, 0.0).color(200, 0, 0, 0).endVertex();
            worldrenderer.pos(j + i * 1.1f, k - i * 0.6f - 16.0f, 0.0).color(200, 0, 0, 0).endVertex();
            tessellator.draw();
            GlStateManager.disableBlend();
            double d0 = 0.0;
            for (int l = 0; l < list.size(); ++l) {
                final Profiler.Result profiler$result2 = list.get(l);
                final int i2 = MathHelper.floor_double(profiler$result2.field_76332_a / 4.0) + 1;
                worldrenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
                final int j2 = profiler$result2.func_76329_a();
                final int k2 = j2 >> 16 & 0xFF;
                final int l2 = j2 >> 8 & 0xFF;
                final int i3 = j2 & 0xFF;
                worldrenderer.pos(j, k, 0.0).color(k2, l2, i3, 255).endVertex();
                for (int j3 = i2; j3 >= 0; --j3) {
                    final float f = (float)((d0 + profiler$result2.field_76332_a * j3 / i2) * 3.141592653589793 * 2.0 / 100.0);
                    final float f2 = MathHelper.sin(f) * i;
                    final float f3 = MathHelper.cos(f) * i * 0.5f;
                    worldrenderer.pos(j + f2, k - f3, 0.0).color(k2, l2, i3, 255).endVertex();
                }
                tessellator.draw();
                worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);
                for (int i4 = i2; i4 >= 0; --i4) {
                    final float f4 = (float)((d0 + profiler$result2.field_76332_a * i4 / i2) * 3.141592653589793 * 2.0 / 100.0);
                    final float f5 = MathHelper.sin(f4) * i;
                    final float f6 = MathHelper.cos(f4) * i * 0.5f;
                    worldrenderer.pos(j + f5, k - f6, 0.0).color(k2 >> 1, l2 >> 1, i3 >> 1, 255).endVertex();
                    worldrenderer.pos(j + f5, k - f6 + 10.0f, 0.0).color(k2 >> 1, l2 >> 1, i3 >> 1, 255).endVertex();
                }
                tessellator.draw();
                d0 += profiler$result2.field_76332_a;
            }
            final DecimalFormat decimalformat = new DecimalFormat("##0.00");
            GlStateManager.enableTexture2D();
            String s = "";
            if (!profiler$result.field_76331_c.equals("unspecified")) {
                s = String.valueOf(s) + "[0] ";
            }
            if (profiler$result.field_76331_c.length() == 0) {
                s = String.valueOf(s) + "ROOT ";
            }
            else {
                s = String.valueOf(s) + profiler$result.field_76331_c + " ";
            }
            final int l3 = 16777215;
            this.fontRendererObj.drawStringWithShadow(s, (float)(j - i), (float)(k - i / 2 - 16), l3);
            this.fontRendererObj.drawStringWithShadow(s = String.valueOf(decimalformat.format(profiler$result.field_76330_b)) + "%", (float)(j + i - this.fontRendererObj.getStringWidth(s)), (float)(k - i / 2 - 16), l3);
            for (int k3 = 0; k3 < list.size(); ++k3) {
                final Profiler.Result profiler$result3 = list.get(k3);
                String s2 = "";
                if (profiler$result3.field_76331_c.equals("unspecified")) {
                    s2 = String.valueOf(s2) + "[?] ";
                }
                else {
                    s2 = String.valueOf(s2) + "[" + (k3 + 1) + "] ";
                }
                s2 = String.valueOf(s2) + profiler$result3.field_76331_c;
                this.fontRendererObj.drawStringWithShadow(s2, (float)(j - i), (float)(k + i / 2 + k3 * 8 + 20), profiler$result3.func_76329_a());
                this.fontRendererObj.drawStringWithShadow(s2 = String.valueOf(decimalformat.format(profiler$result3.field_76332_a)) + "%", (float)(j + i - 50 - this.fontRendererObj.getStringWidth(s2)), (float)(k + i / 2 + k3 * 8 + 20), profiler$result3.func_76329_a());
                this.fontRendererObj.drawStringWithShadow(s2 = String.valueOf(decimalformat.format(profiler$result3.field_76330_b)) + "%", (float)(j + i - this.fontRendererObj.getStringWidth(s2)), (float)(k + i / 2 + k3 * 8 + 20), profiler$result3.func_76329_a());
            }
        }
    }
    
    public void shutdown() {
        this.running = false;
    }
    
    public void setIngameFocus() {
        if (Display.isActive() && !this.inGameHasFocus) {
            this.inGameHasFocus = true;
            this.mouseHelper.grabMouseCursor();
            this.displayGuiScreen(null);
            this.leftClickCounter = 10000;
        }
    }
    
    public void setIngameNotInFocus() {
        if (this.inGameHasFocus) {
            KeyBinding.unPressAllKeys();
            this.inGameHasFocus = false;
            this.mouseHelper.ungrabMouseCursor();
        }
    }
    
    public void displayInGameMenu() {
        if (this.currentScreen == null) {
            this.displayGuiScreen(new GuiIngameMenu());
            if (this.isSingleplayer() && !this.theIntegratedServer.getPublic()) {
                this.mcSoundHandler.pauseSounds();
            }
        }
    }
    
    private void sendClickBlockToController(final boolean leftClick) {
        if (!leftClick) {
            this.leftClickCounter = 0;
        }
        if (this.leftClickCounter <= 0 && !Minecraft.thePlayer.isUsingItem()) {
            if (leftClick && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                final BlockPos blockpos = this.objectMouseOver.getBlockPos();
                if (this.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air && this.playerController.onPlayerDamageBlock(blockpos, this.objectMouseOver.sideHit)) {
                    this.effectRenderer.addBlockHitEffects(blockpos, this.objectMouseOver.sideHit);
                    Minecraft.thePlayer.swingItem();
                }
            }
            else {
                this.playerController.resetBlockRemoving();
            }
        }
    }
    
    private void clickMouse() {
        if (this.leftClickCounter <= 0) {
            Minecraft.thePlayer.swingItem();
            if (this.objectMouseOver == null) {
                Minecraft.logger.error("Null returned as 'hitResult', this shouldn't happen!");
                if (this.playerController.isNotCreative()) {
                    this.leftClickCounter = 10;
                }
            }
            else {
                switch (this.objectMouseOver.typeOfHit) {
                    case ENTITY: {
                        this.playerController.attackEntity(Minecraft.thePlayer, this.objectMouseOver.entityHit);
                        return;
                    }
                    case BLOCK: {
                        final BlockPos blockpos = this.objectMouseOver.getBlockPos();
                        if (this.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
                            this.playerController.clickBlock(blockpos, this.objectMouseOver.sideHit);
                            return;
                        }
                        break;
                    }
                }
                if (this.playerController.isNotCreative()) {
                    this.leftClickCounter = 10;
                }
            }
        }
    }
    
    private void rightClickMouse() {
        if (!this.playerController.func_181040_m()) {
            this.rightClickDelayTimer = 4;
            boolean flag = true;
            final ItemStack itemstack = Minecraft.thePlayer.inventory.getCurrentItem();
            if (this.objectMouseOver == null) {
                Minecraft.logger.warn("Null returned as 'hitResult', this shouldn't happen!");
            }
            else {
                switch (this.objectMouseOver.typeOfHit) {
                    case ENTITY: {
                        if (this.playerController.func_178894_a(Minecraft.thePlayer, this.objectMouseOver.entityHit, this.objectMouseOver)) {
                            flag = false;
                            break;
                        }
                        if (this.playerController.interactWithEntitySendPacket(Minecraft.thePlayer, this.objectMouseOver.entityHit)) {
                            flag = false;
                            break;
                        }
                        break;
                    }
                    case BLOCK: {
                        final BlockPos blockpos = this.objectMouseOver.getBlockPos();
                        if (this.theWorld.getBlockState(blockpos).getBlock().getMaterial() == Material.air) {
                            break;
                        }
                        final int i = (itemstack != null) ? itemstack.stackSize : 0;
                        if (this.playerController.onPlayerRightClick(Minecraft.thePlayer, this.theWorld, itemstack, blockpos, this.objectMouseOver.sideHit, this.objectMouseOver.hitVec)) {
                            flag = false;
                            Minecraft.thePlayer.swingItem();
                        }
                        if (itemstack == null) {
                            return;
                        }
                        if (itemstack.stackSize == 0) {
                            Minecraft.thePlayer.inventory.mainInventory[Minecraft.thePlayer.inventory.currentItem] = null;
                            break;
                        }
                        if (itemstack.stackSize != i || this.playerController.isInCreativeMode()) {
                            this.entityRenderer.itemRenderer.resetEquippedProgress();
                            break;
                        }
                        break;
                    }
                }
            }
            if (flag) {
                final ItemStack itemstack2 = Minecraft.thePlayer.inventory.getCurrentItem();
                if (itemstack2 != null && this.playerController.sendUseItem(Minecraft.thePlayer, this.theWorld, itemstack2)) {
                    this.entityRenderer.itemRenderer.resetEquippedProgress2();
                }
            }
        }
    }
    
    public void toggleFullscreen() {
        try {
            this.fullscreen = !this.fullscreen;
            this.gameSettings.fullScreen = this.fullscreen;
            if (this.fullscreen) {
                this.updateDisplayMode();
                Minecraft.displayWidth = Display.getDisplayMode().getWidth();
                Minecraft.displayHeight = Display.getDisplayMode().getHeight();
                if (Minecraft.displayWidth <= 0) {
                    Minecraft.displayWidth = 1;
                }
                if (Minecraft.displayHeight <= 0) {
                    Minecraft.displayHeight = 1;
                }
            }
            else {
                Display.setDisplayMode(new DisplayMode(this.tempDisplayWidth, this.tempDisplayHeight));
                Minecraft.displayWidth = this.tempDisplayWidth;
                Minecraft.displayHeight = this.tempDisplayHeight;
                if (Minecraft.displayWidth <= 0) {
                    Minecraft.displayWidth = 1;
                }
                if (Minecraft.displayHeight <= 0) {
                    Minecraft.displayHeight = 1;
                }
            }
            if (this.currentScreen != null) {
                this.resize(Minecraft.displayWidth, Minecraft.displayHeight);
            }
            else {
                this.updateFramebufferSize();
            }
            Display.setFullscreen(this.fullscreen);
            Display.setVSyncEnabled(this.gameSettings.enableVsync);
            this.updateDisplay();
        }
        catch (Exception exception) {
            Minecraft.logger.error("Couldn't toggle fullscreen", exception);
        }
    }
    
    private void resize(final int width, final int height) {
        Minecraft.displayWidth = Math.max(1, width);
        Minecraft.displayHeight = Math.max(1, height);
        if (this.currentScreen != null) {
            final ScaledResolution scaledresolution = new ScaledResolution(this);
            this.currentScreen.onResize(this, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
        }
        this.loadingScreen = new LoadingScreenRenderer(this);
        this.updateFramebufferSize();
    }
    
    private void updateFramebufferSize() {
        this.framebufferMc.createBindFramebuffer(Minecraft.displayWidth, Minecraft.displayHeight);
        if (this.entityRenderer != null) {
            this.entityRenderer.updateShaderGroupSize(Minecraft.displayWidth, Minecraft.displayHeight);
        }
    }
    
    public MusicTicker func_181535_r() {
        return this.mcMusicTicker;
    }
    
    public void runTick() throws IOException {
        if (this.rightClickDelayTimer > 0) {
            --this.rightClickDelayTimer;
        }
        this.mcProfiler.startSection("gui");
        if (!this.isGamePaused) {
            this.ingameGUI.updateTick();
        }
        this.mcProfiler.endSection();
        this.entityRenderer.getMouseOver(1.0f);
        this.mcProfiler.startSection("gameMode");
        if (!this.isGamePaused && this.theWorld != null) {
            this.playerController.updateController();
        }
        this.mcProfiler.endStartSection("textures");
        if (!this.isGamePaused) {
            this.renderEngine.tick();
        }
        if (this.currentScreen == null && Minecraft.thePlayer != null) {
            if (Minecraft.thePlayer.getHealth() <= 0.0f) {
                this.displayGuiScreen(null);
            }
            else if (Minecraft.thePlayer.isPlayerSleeping() && this.theWorld != null) {
                this.displayGuiScreen(new GuiSleepMP());
            }
        }
        else if (this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !Minecraft.thePlayer.isPlayerSleeping()) {
            this.displayGuiScreen(null);
        }
        if (this.currentScreen != null) {
            this.leftClickCounter = 10000;
        }
        if (this.currentScreen != null) {
            try {
                this.currentScreen.handleInput();
            }
            catch (Throwable throwable1) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Updating screen events");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Affected screen");
                crashreportcategory.addCrashSectionCallable("Screen name", new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return Minecraft.this.currentScreen.getClass().getCanonicalName();
                    }
                });
                throw new ReportedException(crashreport);
            }
            if (this.currentScreen != null) {
                try {
                    this.currentScreen.updateScreen();
                }
                catch (Throwable throwable2) {
                    final CrashReport crashreport2 = CrashReport.makeCrashReport(throwable2, "Ticking screen");
                    final CrashReportCategory crashreportcategory2 = crashreport2.makeCategory("Affected screen");
                    crashreportcategory2.addCrashSectionCallable("Screen name", new Callable<String>() {
                        @Override
                        public String call() throws Exception {
                            return Minecraft.this.currentScreen.getClass().getCanonicalName();
                        }
                    });
                    throw new ReportedException(crashreport2);
                }
            }
        }
        if (this.currentScreen == null || this.currentScreen.allowUserInput) {
            this.mcProfiler.endStartSection("mouse");
            while (Mouse.next()) {
                final int i = Mouse.getEventButton();
                KeyBinding.setKeyBindState(i - 100, Mouse.getEventButtonState());
                if (Mouse.getEventButtonState()) {
                    if (Minecraft.thePlayer.isSpectator() && i == 2) {
                        this.ingameGUI.getSpectatorGui().func_175261_b();
                    }
                    else {
                        KeyBinding.onTick(i - 100);
                    }
                }
                final long i2 = getSystemTime() - this.systemTime;
                if (i2 <= 200L) {
                    int j = Mouse.getEventDWheel();
                    if (j != 0) {
                        if (Minecraft.thePlayer.isSpectator()) {
                            j = ((j < 0) ? -1 : 1);
                            if (this.ingameGUI.getSpectatorGui().func_175262_a()) {
                                this.ingameGUI.getSpectatorGui().func_175259_b(-j);
                            }
                            else {
                                final float f = MathHelper.clamp_float(Minecraft.thePlayer.capabilities.getFlySpeed() + j * 0.005f, 0.0f, 0.2f);
                                Minecraft.thePlayer.capabilities.setFlySpeed(f);
                            }
                        }
                        else {
                            Minecraft.thePlayer.inventory.changeCurrentItem(j);
                        }
                    }
                    if (this.currentScreen == null) {
                        if (this.inGameHasFocus || !Mouse.getEventButtonState()) {
                            continue;
                        }
                        this.setIngameFocus();
                    }
                    else {
                        if (this.currentScreen == null) {
                            continue;
                        }
                        this.currentScreen.handleMouseInput();
                    }
                }
            }
            if (this.leftClickCounter > 0) {
                --this.leftClickCounter;
            }
            this.mcProfiler.endStartSection("keyboard");
            while (Keyboard.next()) {
                final int k = (Keyboard.getEventKey() == 0) ? (Keyboard.getEventCharacter() + '\u0100') : Keyboard.getEventKey();
                KeyBinding.setKeyBindState(k, Keyboard.getEventKeyState());
                if (Keyboard.getEventKeyState()) {
                    KeyBinding.onTick(k);
                }
                if (this.debugCrashKeyPressTime > 0L) {
                    if (getSystemTime() - this.debugCrashKeyPressTime >= 6000L) {
                        throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
                    }
                    if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61)) {
                        this.debugCrashKeyPressTime = -1L;
                    }
                }
                else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61)) {
                    this.debugCrashKeyPressTime = getSystemTime();
                }
                this.dispatchKeypresses();
                if (Keyboard.getEventKeyState()) {
                    if (k == 62 && this.entityRenderer != null) {
                        this.entityRenderer.switchUseShader();
                    }
                    if (this.currentScreen != null) {
                        this.currentScreen.handleKeyboardInput();
                    }
                    else {
                        for (final Module m : Client.instance.moduleManager.getModules()) {
                            if (k == m.getKeyBind()) {
                                m.toggle();
                            }
                        }
                        if (k == 1) {
                            this.displayInGameMenu();
                        }
                        if (k == 32 && Keyboard.isKeyDown(61) && this.ingameGUI != null) {
                            this.ingameGUI.getChatGUI().clearChatMessages();
                        }
                        if (k == 31 && Keyboard.isKeyDown(61)) {
                            this.refreshResources();
                        }
                        if (k != 17 || Keyboard.isKeyDown(61)) {}
                        if (k != 18 || Keyboard.isKeyDown(61)) {}
                        if (k != 47 || Keyboard.isKeyDown(61)) {}
                        if (k != 38 || Keyboard.isKeyDown(61)) {}
                        if (k != 22 || Keyboard.isKeyDown(61)) {}
                        if (k == 20 && Keyboard.isKeyDown(61)) {
                            this.refreshResources();
                        }
                        if (k == 33 && Keyboard.isKeyDown(61)) {
                            this.gameSettings.setOptionValue(GameSettings.Options.RENDER_DISTANCE, GuiScreen.isShiftKeyDown() ? -1 : 1);
                        }
                        if (k == 30 && Keyboard.isKeyDown(61)) {
                            this.renderGlobal.loadRenderers();
                        }
                        if (k == 35 && Keyboard.isKeyDown(61)) {
                            this.gameSettings.advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
                            this.gameSettings.saveOptions();
                        }
                        if (k == 48 && Keyboard.isKeyDown(61)) {
                            this.renderManager.setDebugBoundingBox(!this.renderManager.isDebugBoundingBox());
                        }
                        if (k == 25 && Keyboard.isKeyDown(61)) {
                            this.gameSettings.pauseOnLostFocus = !this.gameSettings.pauseOnLostFocus;
                            this.gameSettings.saveOptions();
                        }
                        if (k == 59) {
                            this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
                        }
                        if (k == 61) {
                            this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
                            this.gameSettings.showDebugProfilerChart = GuiScreen.isShiftKeyDown();
                            this.gameSettings.field_181657_aC = GuiScreen.isAltKeyDown();
                        }
                        if (this.gameSettings.keyBindTogglePerspective.isPressed()) {
                            final GameSettings gameSettings = this.gameSettings;
                            ++gameSettings.thirdPersonView;
                            if (this.gameSettings.thirdPersonView > 2) {
                                this.gameSettings.thirdPersonView = 0;
                            }
                            if (this.gameSettings.thirdPersonView == 0) {
                                this.entityRenderer.loadEntityShader(this.getRenderViewEntity());
                            }
                            else if (this.gameSettings.thirdPersonView == 1) {
                                this.entityRenderer.loadEntityShader(null);
                            }
                            this.renderGlobal.setDisplayListEntitiesDirty();
                        }
                        if (this.gameSettings.keyBindSmoothCamera.isPressed()) {
                            this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
                        }
                    }
                    if (!this.gameSettings.showDebugInfo || !this.gameSettings.showDebugProfilerChart) {
                        continue;
                    }
                    if (k == 11) {
                        this.updateDebugProfilerName(0);
                    }
                    for (int j2 = 0; j2 < 9; ++j2) {
                        if (k == 2 + j2) {
                            this.updateDebugProfilerName(j2 + 1);
                        }
                    }
                }
            }
            for (int l = 0; l < 9; ++l) {
                if (this.gameSettings.keyBindsHotbar[l].isPressed()) {
                    if (Minecraft.thePlayer.isSpectator()) {
                        this.ingameGUI.getSpectatorGui().func_175260_a(l);
                    }
                    else {
                        Minecraft.thePlayer.inventory.currentItem = l;
                    }
                }
            }
            final boolean flag = this.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN;
            while (this.gameSettings.keyBindInventory.isPressed()) {
                if (this.playerController.isRidingHorse()) {
                    Minecraft.thePlayer.sendHorseInventory();
                }
                else {
                    this.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                    this.displayGuiScreen(new GuiInventory(Minecraft.thePlayer));
                }
            }
            while (this.gameSettings.keyBindDrop.isPressed()) {
                if (!Minecraft.thePlayer.isSpectator()) {
                    Minecraft.thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
                }
            }
            while (this.gameSettings.keyBindChat.isPressed() && flag) {
                this.displayGuiScreen(new GuiChat());
            }
            if (this.currentScreen == null && this.gameSettings.keyBindCommand.isPressed() && flag) {
                this.displayGuiScreen(new GuiChat("/"));
            }
            if (Minecraft.thePlayer.isUsingItem()) {
                if (!this.gameSettings.keyBindUseItem.isKeyDown()) {
                    this.playerController.onStoppedUsingItem(Minecraft.thePlayer);
                }
                while (this.gameSettings.keyBindAttack.isPressed()) {}
                while (this.gameSettings.keyBindUseItem.isPressed()) {}
                while (this.gameSettings.keyBindPickBlock.isPressed()) {}
            }
            else {
                while (this.gameSettings.keyBindAttack.isPressed()) {
                    this.clickMouse();
                }
                while (this.gameSettings.keyBindUseItem.isPressed()) {
                    this.rightClickMouse();
                }
                while (this.gameSettings.keyBindPickBlock.isPressed()) {
                    this.middleClickMouse();
                }
            }
            if (this.gameSettings.keyBindUseItem.isKeyDown() && this.rightClickDelayTimer == 0 && !Minecraft.thePlayer.isUsingItem()) {
                this.rightClickMouse();
            }
            this.sendClickBlockToController(this.currentScreen == null && this.gameSettings.keyBindAttack.isKeyDown() && this.inGameHasFocus);
        }
        if (this.theWorld != null) {
            if (Minecraft.thePlayer != null) {
                ++this.joinPlayerCounter;
                if (this.joinPlayerCounter == 30) {
                    this.joinPlayerCounter = 0;
                    this.theWorld.joinEntityInSurroundings(Minecraft.thePlayer);
                }
            }
            this.mcProfiler.endStartSection("gameRenderer");
            if (!this.isGamePaused) {
                this.entityRenderer.updateRenderer();
            }
            this.mcProfiler.endStartSection("levelRenderer");
            if (!this.isGamePaused) {
                this.renderGlobal.updateClouds();
            }
            this.mcProfiler.endStartSection("level");
            if (!this.isGamePaused) {
                if (this.theWorld.getLastLightningBolt() > 0) {
                    this.theWorld.setLastLightningBolt(this.theWorld.getLastLightningBolt() - 1);
                }
                this.theWorld.updateEntities();
            }
        }
        else if (this.entityRenderer.isShaderActive()) {
            this.entityRenderer.func_181022_b();
        }
        if (!this.isGamePaused) {
            this.mcMusicTicker.update();
            this.mcSoundHandler.update();
        }
        if (this.theWorld != null) {
            if (!this.isGamePaused) {
                this.theWorld.setAllowedSpawnTypes(this.theWorld.getDifficulty() != EnumDifficulty.PEACEFUL, true);
                try {
                    this.theWorld.tick();
                }
                catch (Throwable throwable3) {
                    final CrashReport crashreport3 = CrashReport.makeCrashReport(throwable3, "Exception in world tick");
                    if (this.theWorld == null) {
                        final CrashReportCategory crashreportcategory3 = crashreport3.makeCategory("Affected level");
                        crashreportcategory3.addCrashSection("Problem", "Level is null!");
                    }
                    else {
                        this.theWorld.addWorldInfoToCrashReport(crashreport3);
                    }
                    throw new ReportedException(crashreport3);
                }
            }
            this.mcProfiler.endStartSection("animateTick");
            if (!this.isGamePaused && this.theWorld != null) {
                this.theWorld.doVoidFogParticles(MathHelper.floor_double(Minecraft.thePlayer.posX), MathHelper.floor_double(Minecraft.thePlayer.posY), MathHelper.floor_double(Minecraft.thePlayer.posZ));
            }
            this.mcProfiler.endStartSection("particles");
            if (!this.isGamePaused) {
                this.effectRenderer.updateEffects();
            }
        }
        else if (this.myNetworkManager != null) {
            this.mcProfiler.endStartSection("pendingConnection");
            this.myNetworkManager.processReceivedPackets();
        }
        this.mcProfiler.endSection();
        this.systemTime = getSystemTime();
    }
    
    public void launchIntegratedServer(final String folderName, final String worldName, WorldSettings worldSettingsIn) {
        this.loadWorld(null);
        System.gc();
        final ISaveHandler isavehandler = this.saveLoader.getSaveLoader(folderName, false);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        if (worldinfo == null && worldSettingsIn != null) {
            worldinfo = new WorldInfo(worldSettingsIn, folderName);
            isavehandler.saveWorldInfo(worldinfo);
        }
        if (worldSettingsIn == null) {
            worldSettingsIn = new WorldSettings(worldinfo);
        }
        try {
            (this.theIntegratedServer = new IntegratedServer(this, folderName, worldName, worldSettingsIn)).startServerThread();
            this.integratedServerIsRunning = true;
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Starting integrated server");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Starting integrated server");
            crashreportcategory.addCrashSection("Level ID", folderName);
            crashreportcategory.addCrashSection("Level Name", worldName);
            throw new ReportedException(crashreport);
        }
        this.loadingScreen.displaySavingString(I18n.format("menu.loadingLevel", new Object[0]));
        while (!this.theIntegratedServer.serverIsInRunLoop()) {
            final String s = this.theIntegratedServer.getUserMessage();
            if (s != null) {
                this.loadingScreen.displayLoadingString(I18n.format(s, new Object[0]));
            }
            else {
                this.loadingScreen.displayLoadingString("");
            }
            try {
                Thread.sleep(200L);
            }
            catch (InterruptedException ex) {}
        }
        this.displayGuiScreen(null);
        final SocketAddress socketaddress = this.theIntegratedServer.getNetworkSystem().addLocalEndpoint();
        final NetworkManager networkmanager = NetworkManager.provideLocalClient(socketaddress);
        networkmanager.setNetHandler(new NetHandlerLoginClient(networkmanager, this, null));
        networkmanager.sendPacket(new C00Handshake(47, socketaddress.toString(), 0, EnumConnectionState.LOGIN));
        networkmanager.sendPacket(new C00PacketLoginStart(this.getSession().getProfile()));
        this.myNetworkManager = networkmanager;
    }
    
    public void loadWorld(final WorldClient worldClientIn) {
        this.loadWorld(worldClientIn, "");
    }
    
    public void loadWorld(final WorldClient worldClientIn, final String loadingMessage) {
        if (worldClientIn == null) {
            final NetHandlerPlayClient nethandlerplayclient = this.getNetHandler();
            if (nethandlerplayclient != null) {
                nethandlerplayclient.cleanup();
            }
            if (this.theIntegratedServer != null && this.theIntegratedServer.isAnvilFileSet()) {
                this.theIntegratedServer.initiateShutdown();
                this.theIntegratedServer.setStaticInstance();
            }
            this.theIntegratedServer = null;
            this.guiAchievement.clearAchievements();
            this.entityRenderer.getMapItemRenderer().clearLoadedMaps();
        }
        this.renderViewEntity = null;
        this.myNetworkManager = null;
        if (this.loadingScreen != null) {
            this.loadingScreen.resetProgressAndMessage(loadingMessage);
            this.loadingScreen.displayLoadingString("");
        }
        if (worldClientIn == null && this.theWorld != null) {
            this.mcResourcePackRepository.func_148529_f();
            this.ingameGUI.func_181029_i();
            this.setServerData(null);
            this.integratedServerIsRunning = false;
        }
        this.mcSoundHandler.stopSounds();
        if ((this.theWorld = worldClientIn) != null) {
            if (this.renderGlobal != null) {
                this.renderGlobal.setWorldAndLoadRenderers(worldClientIn);
            }
            if (this.effectRenderer != null) {
                this.effectRenderer.clearEffects(worldClientIn);
            }
            if (Minecraft.thePlayer == null) {
                Minecraft.thePlayer = this.playerController.func_178892_a(worldClientIn, new StatFileWriter());
                this.playerController.flipPlayer(Minecraft.thePlayer);
            }
            Minecraft.thePlayer.preparePlayerToSpawn();
            worldClientIn.spawnEntityInWorld(Minecraft.thePlayer);
            Minecraft.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
            this.playerController.setPlayerCapabilities(Minecraft.thePlayer);
            this.renderViewEntity = Minecraft.thePlayer;
        }
        else {
            this.saveLoader.flushCache();
            Minecraft.thePlayer = null;
        }
        System.gc();
        this.systemTime = 0L;
    }
    
    public void setDimensionAndSpawnPlayer(final int dimension) {
        this.theWorld.setInitialSpawnLocation();
        this.theWorld.removeAllEntities();
        int i = 0;
        String s = null;
        if (Minecraft.thePlayer != null) {
            i = Minecraft.thePlayer.getEntityId();
            this.theWorld.removeEntity(Minecraft.thePlayer);
            s = Minecraft.thePlayer.getClientBrand();
        }
        this.renderViewEntity = null;
        final EntityPlayerSP entityplayersp = Minecraft.thePlayer;
        Minecraft.thePlayer = this.playerController.func_178892_a(this.theWorld, (Minecraft.thePlayer == null) ? new StatFileWriter() : Minecraft.thePlayer.getStatFileWriter());
        Minecraft.thePlayer.getDataWatcher().updateWatchedObjectsFromList(entityplayersp.getDataWatcher().getAllWatched());
        Minecraft.thePlayer.dimension = dimension;
        this.renderViewEntity = Minecraft.thePlayer;
        Minecraft.thePlayer.preparePlayerToSpawn();
        Minecraft.thePlayer.setClientBrand(s);
        this.theWorld.spawnEntityInWorld(Minecraft.thePlayer);
        this.playerController.flipPlayer(Minecraft.thePlayer);
        Minecraft.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
        Minecraft.thePlayer.setEntityId(i);
        this.playerController.setPlayerCapabilities(Minecraft.thePlayer);
        Minecraft.thePlayer.setReducedDebug(entityplayersp.hasReducedDebug());
        if (this.currentScreen instanceof GuiGameOver) {
            this.displayGuiScreen(null);
        }
    }
    
    public final boolean isDemo() {
        return this.isDemo;
    }
    
    public NetHandlerPlayClient getNetHandler() {
        return (Minecraft.thePlayer != null) ? Minecraft.thePlayer.sendQueue : null;
    }
    
    public static boolean isGuiEnabled() {
        return Minecraft.theMinecraft == null || !Minecraft.theMinecraft.gameSettings.hideGUI;
    }
    
    public static boolean isFancyGraphicsEnabled() {
        return Minecraft.theMinecraft != null && Minecraft.theMinecraft.gameSettings.fancyGraphics;
    }
    
    public static boolean isAmbientOcclusionEnabled() {
        return Minecraft.theMinecraft != null && Minecraft.theMinecraft.gameSettings.ambientOcclusion != 0;
    }
    
    private void middleClickMouse() {
        if (this.objectMouseOver != null) {
            final boolean flag = Minecraft.thePlayer.capabilities.isCreativeMode;
            int i = 0;
            boolean flag2 = false;
            TileEntity tileentity = null;
            Item item;
            if (this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                final BlockPos blockpos = this.objectMouseOver.getBlockPos();
                final Block block = this.theWorld.getBlockState(blockpos).getBlock();
                if (block.getMaterial() == Material.air) {
                    return;
                }
                item = block.getItem(this.theWorld, blockpos);
                if (item == null) {
                    return;
                }
                if (flag && GuiScreen.isCtrlKeyDown()) {
                    tileentity = this.theWorld.getTileEntity(blockpos);
                }
                final Block block2 = (item instanceof ItemBlock && !block.isFlowerPot()) ? Block.getBlockFromItem(item) : block;
                i = block2.getDamageValue(this.theWorld, blockpos);
                flag2 = item.getHasSubtypes();
            }
            else {
                if (this.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY || this.objectMouseOver.entityHit == null || !flag) {
                    return;
                }
                if (this.objectMouseOver.entityHit instanceof EntityPainting) {
                    item = Items.painting;
                }
                else if (this.objectMouseOver.entityHit instanceof EntityLeashKnot) {
                    item = Items.lead;
                }
                else if (this.objectMouseOver.entityHit instanceof EntityItemFrame) {
                    final EntityItemFrame entityitemframe = (EntityItemFrame)this.objectMouseOver.entityHit;
                    final ItemStack itemstack = entityitemframe.getDisplayedItem();
                    if (itemstack == null) {
                        item = Items.item_frame;
                    }
                    else {
                        item = itemstack.getItem();
                        i = itemstack.getMetadata();
                        flag2 = true;
                    }
                }
                else if (this.objectMouseOver.entityHit instanceof EntityMinecart) {
                    final EntityMinecart entityminecart = (EntityMinecart)this.objectMouseOver.entityHit;
                    switch (entityminecart.getMinecartType()) {
                        case FURNACE: {
                            item = Items.furnace_minecart;
                            break;
                        }
                        case CHEST: {
                            item = Items.chest_minecart;
                            break;
                        }
                        case TNT: {
                            item = Items.tnt_minecart;
                            break;
                        }
                        case HOPPER: {
                            item = Items.hopper_minecart;
                            break;
                        }
                        case COMMAND_BLOCK: {
                            item = Items.command_block_minecart;
                            break;
                        }
                        default: {
                            item = Items.minecart;
                            break;
                        }
                    }
                }
                else if (this.objectMouseOver.entityHit instanceof EntityBoat) {
                    item = Items.boat;
                }
                else if (this.objectMouseOver.entityHit instanceof EntityArmorStand) {
                    item = Items.armor_stand;
                }
                else {
                    item = Items.spawn_egg;
                    i = EntityList.getEntityID(this.objectMouseOver.entityHit);
                    flag2 = true;
                    if (!EntityList.entityEggs.containsKey(i)) {
                        return;
                    }
                }
            }
            final InventoryPlayer inventoryplayer = Minecraft.thePlayer.inventory;
            if (tileentity == null) {
                inventoryplayer.setCurrentItem(item, i, flag2, flag);
            }
            else {
                final ItemStack itemstack2 = this.func_181036_a(item, i, tileentity);
                inventoryplayer.setInventorySlotContents(inventoryplayer.currentItem, itemstack2);
            }
            if (flag) {
                final int j = Minecraft.thePlayer.inventoryContainer.inventorySlots.size() - 9 + inventoryplayer.currentItem;
                this.playerController.sendSlotPacket(inventoryplayer.getStackInSlot(inventoryplayer.currentItem), j);
            }
        }
    }
    
    private ItemStack func_181036_a(final Item p_181036_1_, final int p_181036_2_, final TileEntity p_181036_3_) {
        final ItemStack itemstack = new ItemStack(p_181036_1_, 1, p_181036_2_);
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        p_181036_3_.writeToNBT(nbttagcompound);
        if (p_181036_1_ == Items.skull && nbttagcompound.hasKey("Owner")) {
            final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Owner");
            final NBTTagCompound nbttagcompound3 = new NBTTagCompound();
            nbttagcompound3.setTag("SkullOwner", nbttagcompound2);
            itemstack.setTagCompound(nbttagcompound3);
            return itemstack;
        }
        itemstack.setTagInfo("BlockEntityTag", nbttagcompound);
        final NBTTagCompound nbttagcompound4 = new NBTTagCompound();
        final NBTTagList nbttaglist = new NBTTagList();
        nbttaglist.appendTag(new NBTTagString("(+NBT)"));
        nbttagcompound4.setTag("Lore", nbttaglist);
        itemstack.setTagInfo("display", nbttagcompound4);
        return itemstack;
    }
    
    public CrashReport addGraphicsAndWorldToCrashReport(final CrashReport theCrash) {
        theCrash.getCategory().addCrashSectionCallable("Launched Version", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return Minecraft.this.launchedVersion;
            }
        });
        theCrash.getCategory().addCrashSectionCallable("LWJGL", new Callable<String>() {
            @Override
            public String call() {
                return Sys.getVersion();
            }
        });
        theCrash.getCategory().addCrashSectionCallable("OpenGL", new Callable<String>() {
            @Override
            public String call() {
                return String.valueOf(GL11.glGetString(7937)) + " GL version " + GL11.glGetString(7938) + ", " + GL11.glGetString(7936);
            }
        });
        theCrash.getCategory().addCrashSectionCallable("GL Caps", new Callable<String>() {
            @Override
            public String call() {
                return OpenGlHelper.getLogText();
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Using VBOs", new Callable<String>() {
            @Override
            public String call() {
                return Minecraft.this.gameSettings.useVbo ? "Yes" : "No";
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Is Modded", new Callable<String>() {
            @Override
            public String call() throws Exception {
                final String s = ClientBrandRetriever.getClientModName();
                return s.equals("vanilla") ? ((Minecraft.class.getSigners() == null) ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and client brand is untouched.") : ("Definitely; Client brand changed to '" + s + "'");
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Type", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Client (map_client.txt)";
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Resource Packs", new Callable<String>() {
            @Override
            public String call() throws Exception {
                final StringBuilder stringbuilder = new StringBuilder();
                for (final Object s : Minecraft.this.gameSettings.resourcePacks) {
                    if (stringbuilder.length() > 0) {
                        stringbuilder.append(", ");
                    }
                    stringbuilder.append(s);
                    if (Minecraft.this.gameSettings.field_183018_l.contains(s)) {
                        stringbuilder.append(" (incompatible)");
                    }
                }
                return stringbuilder.toString();
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Current Language", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return Minecraft.this.mcLanguageManager.getCurrentLanguage().toString();
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Profiler Position", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return Minecraft.this.mcProfiler.profilingEnabled ? Minecraft.this.mcProfiler.getNameOfLastSection() : "N/A (disabled)";
            }
        });
        theCrash.getCategory().addCrashSectionCallable("CPU", new Callable<String>() {
            @Override
            public String call() {
                return OpenGlHelper.func_183029_j();
            }
        });
        if (this.theWorld != null) {
            this.theWorld.addWorldInfoToCrashReport(theCrash);
        }
        return theCrash;
    }
    
    public static Minecraft getMinecraft() {
        return Minecraft.theMinecraft;
    }
    
    public ListenableFuture<Object> scheduleResourcesRefresh() {
        return this.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                Minecraft.this.refreshResources();
            }
        });
    }
    
    @Override
    public void addServerStatsToSnooper(final PlayerUsageSnooper playerSnooper) {
        playerSnooper.addClientStat("fps", Minecraft.debugFPS);
        playerSnooper.addClientStat("vsync_enabled", this.gameSettings.enableVsync);
        playerSnooper.addClientStat("display_frequency", Display.getDisplayMode().getFrequency());
        playerSnooper.addClientStat("display_type", this.fullscreen ? "fullscreen" : "windowed");
        playerSnooper.addClientStat("run_time", (MinecraftServer.getCurrentTimeMillis() - playerSnooper.getMinecraftStartTimeMillis()) / 60L * 1000L);
        playerSnooper.addClientStat("current_action", this.func_181538_aA());
        final String s = (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) ? "little" : "big";
        playerSnooper.addClientStat("endianness", s);
        playerSnooper.addClientStat("resource_packs", this.mcResourcePackRepository.getRepositoryEntries().size());
        int i = 0;
        for (final ResourcePackRepository.Entry resourcepackrepository$entry : this.mcResourcePackRepository.getRepositoryEntries()) {
            playerSnooper.addClientStat("resource_pack[" + i++ + "]", resourcepackrepository$entry.getResourcePackName());
        }
        if (this.theIntegratedServer != null && this.theIntegratedServer.getPlayerUsageSnooper() != null) {
            playerSnooper.addClientStat("snooper_partner", this.theIntegratedServer.getPlayerUsageSnooper().getUniqueID());
        }
    }
    
    private String func_181538_aA() {
        return (this.theIntegratedServer != null) ? (this.theIntegratedServer.getPublic() ? "hosting_lan" : "singleplayer") : ((this.currentServerData != null) ? (this.currentServerData.func_181041_d() ? "playing_lan" : "multiplayer") : "out_of_game");
    }
    
    @Override
    public void addServerTypeToSnooper(final PlayerUsageSnooper playerSnooper) {
        playerSnooper.addStatToSnooper("opengl_version", GL11.glGetString(7938));
        playerSnooper.addStatToSnooper("opengl_vendor", GL11.glGetString(7936));
        playerSnooper.addStatToSnooper("client_brand", ClientBrandRetriever.getClientModName());
        playerSnooper.addStatToSnooper("launched_version", this.launchedVersion);
        final ContextCapabilities contextcapabilities = GLContext.getCapabilities();
        playerSnooper.addStatToSnooper("gl_caps[ARB_arrays_of_arrays]", contextcapabilities.GL_ARB_arrays_of_arrays);
        playerSnooper.addStatToSnooper("gl_caps[ARB_base_instance]", contextcapabilities.GL_ARB_base_instance);
        playerSnooper.addStatToSnooper("gl_caps[ARB_blend_func_extended]", contextcapabilities.GL_ARB_blend_func_extended);
        playerSnooper.addStatToSnooper("gl_caps[ARB_clear_buffer_object]", contextcapabilities.GL_ARB_clear_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_color_buffer_float]", contextcapabilities.GL_ARB_color_buffer_float);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compatibility]", contextcapabilities.GL_ARB_compatibility);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compressed_texture_pixel_storage]", contextcapabilities.GL_ARB_compressed_texture_pixel_storage);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", contextcapabilities.GL_ARB_compute_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", contextcapabilities.GL_ARB_copy_buffer);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", contextcapabilities.GL_ARB_copy_image);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", contextcapabilities.GL_ARB_depth_buffer_float);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", contextcapabilities.GL_ARB_compute_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", contextcapabilities.GL_ARB_copy_buffer);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", contextcapabilities.GL_ARB_copy_image);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", contextcapabilities.GL_ARB_depth_buffer_float);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_clamp]", contextcapabilities.GL_ARB_depth_clamp);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_texture]", contextcapabilities.GL_ARB_depth_texture);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers]", contextcapabilities.GL_ARB_draw_buffers);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers_blend]", contextcapabilities.GL_ARB_draw_buffers_blend);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_elements_base_vertex]", contextcapabilities.GL_ARB_draw_elements_base_vertex);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_indirect]", contextcapabilities.GL_ARB_draw_indirect);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_instanced]", contextcapabilities.GL_ARB_draw_instanced);
        playerSnooper.addStatToSnooper("gl_caps[ARB_explicit_attrib_location]", contextcapabilities.GL_ARB_explicit_attrib_location);
        playerSnooper.addStatToSnooper("gl_caps[ARB_explicit_uniform_location]", contextcapabilities.GL_ARB_explicit_uniform_location);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_layer_viewport]", contextcapabilities.GL_ARB_fragment_layer_viewport);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_program]", contextcapabilities.GL_ARB_fragment_program);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_shader]", contextcapabilities.GL_ARB_fragment_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_program_shadow]", contextcapabilities.GL_ARB_fragment_program_shadow);
        playerSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_object]", contextcapabilities.GL_ARB_framebuffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_sRGB]", contextcapabilities.GL_ARB_framebuffer_sRGB);
        playerSnooper.addStatToSnooper("gl_caps[ARB_geometry_shader4]", contextcapabilities.GL_ARB_geometry_shader4);
        playerSnooper.addStatToSnooper("gl_caps[ARB_gpu_shader5]", contextcapabilities.GL_ARB_gpu_shader5);
        playerSnooper.addStatToSnooper("gl_caps[ARB_half_float_pixel]", contextcapabilities.GL_ARB_half_float_pixel);
        playerSnooper.addStatToSnooper("gl_caps[ARB_half_float_vertex]", contextcapabilities.GL_ARB_half_float_vertex);
        playerSnooper.addStatToSnooper("gl_caps[ARB_instanced_arrays]", contextcapabilities.GL_ARB_instanced_arrays);
        playerSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_alignment]", contextcapabilities.GL_ARB_map_buffer_alignment);
        playerSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_range]", contextcapabilities.GL_ARB_map_buffer_range);
        playerSnooper.addStatToSnooper("gl_caps[ARB_multisample]", contextcapabilities.GL_ARB_multisample);
        playerSnooper.addStatToSnooper("gl_caps[ARB_multitexture]", contextcapabilities.GL_ARB_multitexture);
        playerSnooper.addStatToSnooper("gl_caps[ARB_occlusion_query2]", contextcapabilities.GL_ARB_occlusion_query2);
        playerSnooper.addStatToSnooper("gl_caps[ARB_pixel_buffer_object]", contextcapabilities.GL_ARB_pixel_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_seamless_cube_map]", contextcapabilities.GL_ARB_seamless_cube_map);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_objects]", contextcapabilities.GL_ARB_shader_objects);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_stencil_export]", contextcapabilities.GL_ARB_shader_stencil_export);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_texture_lod]", contextcapabilities.GL_ARB_shader_texture_lod);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shadow]", contextcapabilities.GL_ARB_shadow);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shadow_ambient]", contextcapabilities.GL_ARB_shadow_ambient);
        playerSnooper.addStatToSnooper("gl_caps[ARB_stencil_texturing]", contextcapabilities.GL_ARB_stencil_texturing);
        playerSnooper.addStatToSnooper("gl_caps[ARB_sync]", contextcapabilities.GL_ARB_sync);
        playerSnooper.addStatToSnooper("gl_caps[ARB_tessellation_shader]", contextcapabilities.GL_ARB_tessellation_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_border_clamp]", contextcapabilities.GL_ARB_texture_border_clamp);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_buffer_object]", contextcapabilities.GL_ARB_texture_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map]", contextcapabilities.GL_ARB_texture_cube_map);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map_array]", contextcapabilities.GL_ARB_texture_cube_map_array);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_non_power_of_two]", contextcapabilities.GL_ARB_texture_non_power_of_two);
        playerSnooper.addStatToSnooper("gl_caps[ARB_uniform_buffer_object]", contextcapabilities.GL_ARB_uniform_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_blend]", contextcapabilities.GL_ARB_vertex_blend);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_buffer_object]", contextcapabilities.GL_ARB_vertex_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_program]", contextcapabilities.GL_ARB_vertex_program);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_shader]", contextcapabilities.GL_ARB_vertex_shader);
        playerSnooper.addStatToSnooper("gl_caps[EXT_bindable_uniform]", contextcapabilities.GL_EXT_bindable_uniform);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_equation_separate]", contextcapabilities.GL_EXT_blend_equation_separate);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_func_separate]", contextcapabilities.GL_EXT_blend_func_separate);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_minmax]", contextcapabilities.GL_EXT_blend_minmax);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_subtract]", contextcapabilities.GL_EXT_blend_subtract);
        playerSnooper.addStatToSnooper("gl_caps[EXT_draw_instanced]", contextcapabilities.GL_EXT_draw_instanced);
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_multisample]", contextcapabilities.GL_EXT_framebuffer_multisample);
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_object]", contextcapabilities.GL_EXT_framebuffer_object);
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_sRGB]", contextcapabilities.GL_EXT_framebuffer_sRGB);
        playerSnooper.addStatToSnooper("gl_caps[EXT_geometry_shader4]", contextcapabilities.GL_EXT_geometry_shader4);
        playerSnooper.addStatToSnooper("gl_caps[EXT_gpu_program_parameters]", contextcapabilities.GL_EXT_gpu_program_parameters);
        playerSnooper.addStatToSnooper("gl_caps[EXT_gpu_shader4]", contextcapabilities.GL_EXT_gpu_shader4);
        playerSnooper.addStatToSnooper("gl_caps[EXT_multi_draw_arrays]", contextcapabilities.GL_EXT_multi_draw_arrays);
        playerSnooper.addStatToSnooper("gl_caps[EXT_packed_depth_stencil]", contextcapabilities.GL_EXT_packed_depth_stencil);
        playerSnooper.addStatToSnooper("gl_caps[EXT_paletted_texture]", contextcapabilities.GL_EXT_paletted_texture);
        playerSnooper.addStatToSnooper("gl_caps[EXT_rescale_normal]", contextcapabilities.GL_EXT_rescale_normal);
        playerSnooper.addStatToSnooper("gl_caps[EXT_separate_shader_objects]", contextcapabilities.GL_EXT_separate_shader_objects);
        playerSnooper.addStatToSnooper("gl_caps[EXT_shader_image_load_store]", contextcapabilities.GL_EXT_shader_image_load_store);
        playerSnooper.addStatToSnooper("gl_caps[EXT_shadow_funcs]", contextcapabilities.GL_EXT_shadow_funcs);
        playerSnooper.addStatToSnooper("gl_caps[EXT_shared_texture_palette]", contextcapabilities.GL_EXT_shared_texture_palette);
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_clear_tag]", contextcapabilities.GL_EXT_stencil_clear_tag);
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_two_side]", contextcapabilities.GL_EXT_stencil_two_side);
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_wrap]", contextcapabilities.GL_EXT_stencil_wrap);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_3d]", contextcapabilities.GL_EXT_texture_3d);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_array]", contextcapabilities.GL_EXT_texture_array);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_buffer_object]", contextcapabilities.GL_EXT_texture_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_integer]", contextcapabilities.GL_EXT_texture_integer);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_lod_bias]", contextcapabilities.GL_EXT_texture_lod_bias);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_sRGB]", contextcapabilities.GL_EXT_texture_sRGB);
        playerSnooper.addStatToSnooper("gl_caps[EXT_vertex_shader]", contextcapabilities.GL_EXT_vertex_shader);
        playerSnooper.addStatToSnooper("gl_caps[EXT_vertex_weighting]", contextcapabilities.GL_EXT_vertex_weighting);
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_uniforms]", GL11.glGetInteger(35658));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_fragment_uniforms]", GL11.glGetInteger(35657));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_attribs]", GL11.glGetInteger(34921));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_texture_image_units]", GL11.glGetInteger(35660));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_texture_image_units]", GL11.glGetInteger(34930));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_texture_image_units]", GL11.glGetInteger(35071));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_max_texture_size", getGLMaximumTextureSize());
    }
    
    public static int getGLMaximumTextureSize() {
        for (int i = 16384; i > 0; i >>= 1) {
            GL11.glTexImage2D(32868, 0, 6408, i, i, 0, 6408, 5121, (ByteBuffer)null);
            final int j = GL11.glGetTexLevelParameteri(32868, 0, 4096);
            if (j != 0) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public boolean isSnooperEnabled() {
        return this.gameSettings.snooperEnabled;
    }
    
    public void setServerData(final ServerData serverDataIn) {
        this.currentServerData = serverDataIn;
    }
    
    public ServerData getCurrentServerData() {
        return this.currentServerData;
    }
    
    public boolean isIntegratedServerRunning() {
        return this.integratedServerIsRunning;
    }
    
    public boolean isSingleplayer() {
        return this.integratedServerIsRunning && this.theIntegratedServer != null;
    }
    
    public IntegratedServer getIntegratedServer() {
        return this.theIntegratedServer;
    }
    
    public static void stopIntegratedServer() {
        if (Minecraft.theMinecraft != null) {
            final IntegratedServer integratedserver = Minecraft.theMinecraft.getIntegratedServer();
            if (integratedserver != null) {
                integratedserver.stopServer();
            }
        }
    }
    
    public PlayerUsageSnooper getPlayerUsageSnooper() {
        return this.usageSnooper;
    }
    
    public static long getSystemTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }
    
    public boolean isFullScreen() {
        return this.fullscreen;
    }
    
    public Session getSession() {
        return this.session;
    }
    
    public PropertyMap getTwitchDetails() {
        return this.twitchDetails;
    }
    
    public PropertyMap func_181037_M() {
        if (this.field_181038_N.isEmpty()) {
            final GameProfile gameprofile = this.getSessionService().fillProfileProperties(this.session.getProfile(), false);
            this.field_181038_N.putAll(gameprofile.getProperties());
        }
        return this.field_181038_N;
    }
    
    public Proxy getProxy() {
        return this.proxy;
    }
    
    public TextureManager getTextureManager() {
        return this.renderEngine;
    }
    
    public IResourceManager getResourceManager() {
        return this.mcResourceManager;
    }
    
    public ResourcePackRepository getResourcePackRepository() {
        return this.mcResourcePackRepository;
    }
    
    public LanguageManager getLanguageManager() {
        return this.mcLanguageManager;
    }
    
    public TextureMap getTextureMapBlocks() {
        return this.textureMapBlocks;
    }
    
    public boolean isJava64bit() {
        return this.jvm64bit;
    }
    
    public boolean isGamePaused() {
        return this.isGamePaused;
    }
    
    public SoundHandler getSoundHandler() {
        return this.mcSoundHandler;
    }
    
    public MusicTicker.MusicType getAmbientMusicType() {
        return (Minecraft.thePlayer != null) ? ((Minecraft.thePlayer.worldObj.provider instanceof WorldProviderHell) ? MusicTicker.MusicType.NETHER : ((Minecraft.thePlayer.worldObj.provider instanceof WorldProviderEnd) ? ((BossStatus.bossName != null && BossStatus.statusBarTime > 0) ? MusicTicker.MusicType.END_BOSS : MusicTicker.MusicType.END) : ((Minecraft.thePlayer.capabilities.isCreativeMode && Minecraft.thePlayer.capabilities.allowFlying) ? MusicTicker.MusicType.CREATIVE : MusicTicker.MusicType.GAME))) : MusicTicker.MusicType.MENU;
    }
    
    public IStream getTwitchStream() {
        return this.stream;
    }
    
    public void dispatchKeypresses() {
        final int i = (Keyboard.getEventKey() == 0) ? Keyboard.getEventCharacter() : Keyboard.getEventKey();
        if (i != 0 && !Keyboard.isRepeatEvent() && (!(this.currentScreen instanceof GuiControls) || ((GuiControls)this.currentScreen).time <= getSystemTime() - 20L)) {
            if (Keyboard.getEventKeyState()) {
                if (i == this.gameSettings.keyBindStreamStartStop.getKeyCode()) {
                    if (this.getTwitchStream().isBroadcasting()) {
                        this.getTwitchStream().stopBroadcasting();
                    }
                    else if (this.getTwitchStream().isReadyToBroadcast()) {
                        this.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
                            @Override
                            public void confirmClicked(final boolean result, final int id) {
                                if (result) {
                                    Minecraft.this.getTwitchStream().func_152930_t();
                                }
                                Minecraft.this.displayGuiScreen(null);
                            }
                        }, I18n.format("stream.confirm_start", new Object[0]), "", 0));
                    }
                    else if (this.getTwitchStream().func_152928_D() && this.getTwitchStream().func_152936_l()) {
                        if (this.theWorld != null) {
                            this.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("Not ready to start streaming yet!"));
                        }
                    }
                    else {
                        GuiStreamUnavailable.func_152321_a(this.currentScreen);
                    }
                }
                else if (i == this.gameSettings.keyBindStreamPauseUnpause.getKeyCode()) {
                    if (this.getTwitchStream().isBroadcasting()) {
                        if (this.getTwitchStream().isPaused()) {
                            this.getTwitchStream().unpause();
                        }
                        else {
                            this.getTwitchStream().pause();
                        }
                    }
                }
                else if (i == this.gameSettings.keyBindStreamCommercials.getKeyCode()) {
                    if (this.getTwitchStream().isBroadcasting()) {
                        this.getTwitchStream().requestCommercial();
                    }
                }
                else if (i == this.gameSettings.keyBindStreamToggleMic.getKeyCode()) {
                    this.stream.muteMicrophone(true);
                }
                else if (i == this.gameSettings.keyBindFullscreen.getKeyCode()) {
                    this.toggleFullscreen();
                }
                else if (i == this.gameSettings.keyBindScreenshot.getKeyCode()) {
                    this.ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot(this.mcDataDir, Minecraft.displayWidth, Minecraft.displayHeight, this.framebufferMc));
                }
            }
            else if (i == this.gameSettings.keyBindStreamToggleMic.getKeyCode()) {
                this.stream.muteMicrophone(false);
            }
        }
    }
    
    public MinecraftSessionService getSessionService() {
        return this.sessionService;
    }
    
    public SkinManager getSkinManager() {
        return this.skinManager;
    }
    
    public Entity getRenderViewEntity() {
        return this.renderViewEntity;
    }
    
    public void setRenderViewEntity(final Entity viewingEntity) {
        this.renderViewEntity = viewingEntity;
        this.entityRenderer.loadEntityShader(viewingEntity);
    }
    
    public <V> ListenableFuture<V> addScheduledTask(final Callable<V> callableToSchedule) {
        Validate.notNull(callableToSchedule);
        if (!this.isCallingFromMinecraftThread()) {
            final ListenableFutureTask<V> listenablefuturetask = ListenableFutureTask.create(callableToSchedule);
            synchronized (this.scheduledTasks) {
                this.scheduledTasks.add(listenablefuturetask);
                // monitorexit(this.scheduledTasks)
                return listenablefuturetask;
            }
        }
        try {
            return Futures.immediateFuture(callableToSchedule.call());
        }
        catch (Exception exception) {
            return (ListenableFuture<V>)Futures.immediateFailedCheckedFuture(exception);
        }
    }
    
    @Override
    public ListenableFuture<Object> addScheduledTask(final Runnable runnableToSchedule) {
        Validate.notNull(runnableToSchedule);
        return this.addScheduledTask(Executors.callable(runnableToSchedule));
    }
    
    @Override
    public boolean isCallingFromMinecraftThread() {
        return Thread.currentThread() == this.mcThread;
    }
    
    public BlockRendererDispatcher getBlockRendererDispatcher() {
        return this.blockRenderDispatcher;
    }
    
    public RenderManager getRenderManager() {
        return this.renderManager;
    }
    
    public RenderItem getRenderItem() {
        return this.renderItem;
    }
    
    public ItemRenderer getItemRenderer() {
        return this.itemRenderer;
    }
    
    public static int getDebugFPS() {
        return Minecraft.debugFPS;
    }
    
    public FrameTimer func_181539_aj() {
        return this.field_181542_y;
    }
    
    public static Map<String, String> getSessionInfo() {
        final Map<String, String> map = (Map<String, String>)Maps.newHashMap();
        map.put("X-Minecraft-Username", getMinecraft().getSession().getUsername());
        map.put("X-Minecraft-UUID", getMinecraft().getSession().getPlayerID());
        map.put("X-Minecraft-Version", "1.8.8");
        return map;
    }
    
    public boolean func_181540_al() {
        return this.field_181541_X;
    }
    
    public void func_181537_a(final boolean p_181537_1_) {
        this.field_181541_X = p_181537_1_;
    }
}
