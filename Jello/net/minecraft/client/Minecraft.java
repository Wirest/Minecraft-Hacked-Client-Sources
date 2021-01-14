package net.minecraft.client;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mentalfrostbyte.jello.event.EventManager;
import com.mentalfrostbyte.jello.event.events.EventKeyPressed;
import com.mentalfrostbyte.jello.event.events.EventTick;
import com.mentalfrostbyte.jello.hud.JelloHud;
import com.mentalfrostbyte.jello.main.Jello;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMemoryErrorScreen;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.client.gui.inventory.CreativeCrafting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.stream.GuiStreamUnavailable;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.player.inventory.LocalBlockIntercommunication;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.GrassColorReloadListener;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.ResourceIndex;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.stream.IStream;
import net.minecraft.client.stream.NullStream;
import net.minecraft.client.stream.TwitchStream;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.potion.PotionEffect;
import net.minecraft.profiler.IPlayerUsage;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.IStatStringFormat;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MinecraftError;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import net.minecraft.util.Util;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

public class Minecraft implements IThreadListener, IPlayerUsage
{
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation locationMojangPng = new ResourceLocation("textures/gui/title/mojang.png");
    public static final boolean isRunningOnMac = Util.getOSType() == Util.EnumOS.OSX;

    /** A 10MiB preallocation to ensure the heap is reasonably sized. */
    public static byte[] memoryReserve = new byte[10485760];
    private static final List macDisplayModes = Lists.newArrayList(new DisplayMode[] {new DisplayMode(2560, 1600), new DisplayMode(2880, 1800)});
    private final File fileResourcepacks;
    private final PropertyMap twitchDetails;
    private ServerData currentServerData;

    /** The RenderEngine instance used by Minecraft */
    private TextureManager renderEngine;

    /**
     * Set to 'this' in Minecraft constructor; used by some settings get methods
     */
    private static Minecraft theMinecraft;
    public PlayerControllerMP playerController;
    private boolean fullscreen;
    private boolean field_175619_R = true;
    private boolean hasCrashed;

    /** Instance of CrashReport. */
    private CrashReport crashReporter;
    public int displayWidth;
    public int displayHeight;
    public Timer timer = new Timer(20.0F);

    /** Instance of PlayerUsageSnooper. */
    private PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper("client", this, MinecraftServer.getCurrentTimeMillis());
    public WorldClient theWorld;
    public RenderGlobal renderGlobal;
    private RenderManager renderManager;
    private RenderItem renderItem;
    private ItemRenderer itemRenderer;
    public EntityPlayerSP thePlayer;
    private Entity renderViewEntity;
    public Entity pointedEntity;
    public EffectRenderer effectRenderer;
    public Session session;
    private boolean isGamePaused;
    private static int it;
    /** The font renderer used for displaying and measuring text */
    public FontRenderer fontRendererObj;
    public FontRenderer standardGalacticFontRenderer;

    /** The GuiScreen that's being displayed at the moment. */
    public GuiScreen currentScreen;
    public LoadingScreenRenderer loadingScreen;
    public EntityRenderer entityRenderer;

    /** Mouse left click counter */
    private int leftClickCounter;

    /** Display width */
    private int tempDisplayWidth;

    /** Display height */
    private int tempDisplayHeight;

    /** Instance of IntegratedServer. */
    private IntegratedServer theIntegratedServer;

    /** Gui achievement */
    public GuiAchievement guiAchievement;
    public GuiIngame ingameGUI;

    /** Skip render world */
    public boolean skipRenderWorld;
    public String isOn = "waiting";
    /** The ray trace hit that the mouse is over. */
    public MovingObjectPosition objectMouseOver;

    /** The game settings that currently hold effect. */
    public GameSettings gameSettings;

    /** Mouse helper instance. */
    public MouseHelper mouseHelper;
    public final File mcDataDir;
    private final File fileAssets;
    private final String launchedVersion;
    private final Proxy proxy;
    private ISaveFormat saveLoader;

    /**
     * This is set to fpsCounter every debug screen update, and is shown on the debug screen. It's also sent as part of
     * the usage snooping.
     */
    private static int debugFPS;

    /**
     * When you place a block, it's set to 6, decremented once per tick, when it's 0, you can place another block.
     */
    public int rightClickDelayTimer;
    private String serverName;
    private int serverPort;

    /**
     * Does the actual gameplay have focus. If so then mouse and keys will effect the player instead of menus.
     */
    public boolean inGameHasFocus;
    long systemTime = getSystemTime();

    /** Join player counter */
    private int joinPlayerCounter;
    private final boolean jvm64bit;
    private final boolean isDemo;
    private NetworkManager myNetworkManager;
    private boolean integratedServerIsRunning;

    /** The profiler instance */
    public final Profiler mcProfiler = new Profiler();

    /**
     * Keeps track of how long the debug crash keycombo (F3+C) has been pressed for, in order to crash after 10 seconds.
     */
    private long debugCrashKeyPressTime = -1L;
    private IReloadableResourceManager mcResourceManager;
    private final IMetadataSerializer metadataSerializer_ = new IMetadataSerializer();
    private final List defaultResourcePacks = Lists.newArrayList();
    private final DefaultResourcePack mcDefaultResourcePack;
    private ResourcePackRepository mcResourcePackRepository;
    private LanguageManager mcLanguageManager;
    private IStream stream;
    public Framebuffer framebufferMc;
    private TextureMap textureMapBlocks;
    private SoundHandler mcSoundHandler;
    private MusicTicker mcMusicTicker;
    private ResourceLocation mojangLogo;
    private final MinecraftSessionService sessionService;
    private SkinManager skinManager;
    private final Queue scheduledTasks = Queues.newArrayDeque();
    private long field_175615_aJ = 0L;
    private final Thread mcThread = Thread.currentThread();
    private ModelManager modelManager;
    private BlockRendererDispatcher field_175618_aM;

    /**
     * Set to true to keep the game loop running. Set to false by shutdown() to allow the game loop to exit cleanly.
     */
    volatile boolean running = true;

    /** String that shows the debug information */
    public String debug = "";
    public boolean field_175613_B = false;
    public boolean field_175614_C = false;
    public boolean field_175611_D = false;
    public boolean field_175612_E = true;

    /** Approximate time (in ms) of last update to debug string */
    long debugUpdateTime = getSystemTime();

    /** holds the current fps */
    public int fpsCounter;
    long prevFrameTime = -1L;

    /** Profiler currently displayed in the debug screen pie chart */
    private String debugProfilerName = "root";
    

    public Minecraft(GameConfiguration p_i45547_1_)
    {
        theMinecraft = this;
        this.mcDataDir = p_i45547_1_.field_178744_c.field_178760_a;
        this.fileAssets = p_i45547_1_.field_178744_c.field_178759_c;
        this.fileResourcepacks = p_i45547_1_.field_178744_c.field_178758_b;
        this.launchedVersion = p_i45547_1_.field_178741_d.field_178755_b;
        this.twitchDetails = p_i45547_1_.field_178745_a.field_178750_b;
        this.mcDefaultResourcePack = new DefaultResourcePack((new ResourceIndex(p_i45547_1_.field_178744_c.field_178759_c, p_i45547_1_.field_178744_c.field_178757_d)).func_152782_a());
        this.proxy = p_i45547_1_.field_178745_a.field_178751_c == null ? Proxy.NO_PROXY : p_i45547_1_.field_178745_a.field_178751_c;
        this.sessionService = (new YggdrasilAuthenticationService(p_i45547_1_.field_178745_a.field_178751_c, UUID.randomUUID().toString())).createMinecraftSessionService();
        this.session = p_i45547_1_.field_178745_a.field_178752_a;
        logger.info("Setting user: " + this.session.getUsername());
        logger.info("(Session ID is " + this.session.getSessionID() + ")");
        this.isDemo = p_i45547_1_.field_178741_d.field_178756_a;
        this.displayWidth = p_i45547_1_.field_178743_b.field_178764_a > 0 ? p_i45547_1_.field_178743_b.field_178764_a : 1;
        this.displayHeight = p_i45547_1_.field_178743_b.field_178762_b > 0 ? p_i45547_1_.field_178743_b.field_178762_b : 1;
        this.tempDisplayWidth = p_i45547_1_.field_178743_b.field_178764_a;
        this.tempDisplayHeight = p_i45547_1_.field_178743_b.field_178762_b;
        this.fullscreen = p_i45547_1_.field_178743_b.field_178763_c;
        this.jvm64bit = isJvm64bit();
        this.theIntegratedServer = new IntegratedServer(this);

        if (p_i45547_1_.field_178742_e.field_178754_a != null)
        {
            this.serverName = p_i45547_1_.field_178742_e.field_178754_a;
            this.serverPort = p_i45547_1_.field_178742_e.field_178753_b;
        }

        ImageIO.setUseCache(false);
        Bootstrap.register();
    }

    public void run()
    {
        this.running = true;
        CrashReport var2;

        try
        {
            this.startGame();
        }
        catch (Throwable var11)
        {
            var2 = CrashReport.makeCrashReport(var11, "Initializing game");
            var2.makeCategory("Initialization");
            this.displayCrashReport(this.addGraphicsAndWorldToCrashReport(var2));
            return;
        }

        while (true)
        {
            try
            {
                while (this.running)
                {
                    if (!this.hasCrashed || this.crashReporter == null)
                    {
                        try
                        {
                            this.runGameLoop();
                        }
                        catch (OutOfMemoryError var10)
                        {
                            this.freeMemory();
                            this.displayGuiScreen(new GuiMemoryErrorScreen());
                            System.gc();
                        }

                        continue;
                    }

                    this.displayCrashReport(this.crashReporter);
                    return;
                }
            }
            catch (MinecraftError var12)
            {
                ;
            }
            catch (ReportedException var13)
            {
                this.addGraphicsAndWorldToCrashReport(var13.getCrashReport());
                this.freeMemory();
                logger.fatal("Reported exception thrown!", var13);
                this.displayCrashReport(var13.getCrashReport());
            }
            catch (Throwable var14)
            {
                var2 = this.addGraphicsAndWorldToCrashReport(new CrashReport("Unexpected error", var14));
                this.freeMemory();
                logger.fatal("Unreported exception thrown!", var14);
                this.displayCrashReport(var2);
            }
            finally
            {
                this.shutdownMinecraftApplet();
            }

            return;
        }
    }

    /**
     * Starts the game: initializes the canvas, the title, the settings, etcetera.
     */
    private void startGame() throws LWJGLException
    {
        this.gameSettings = new GameSettings(this, this.mcDataDir);
        this.defaultResourcePacks.add(this.mcDefaultResourcePack);
        this.startTimerHackThread();

        if (this.gameSettings.overrideHeight > 0 && this.gameSettings.overrideWidth > 0)
        {
            this.displayWidth = this.gameSettings.overrideWidth;
            this.displayHeight = this.gameSettings.overrideHeight;
        }

        logger.info("LWJGL Version: " + Sys.getVersion());
        this.func_175594_ao();
        this.func_175605_an();
        this.func_175609_am();
        OpenGlHelper.initializeTextures();
        this.framebufferMc = new Framebuffer(this.displayWidth, this.displayHeight, true);
        this.framebufferMc.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
        this.func_175608_ak();
        this.mcResourcePackRepository = new ResourcePackRepository(this.fileResourcepacks, new File(this.mcDataDir, "server-resource-packs"), this.mcDefaultResourcePack, this.metadataSerializer_, this.gameSettings);
        this.mcResourceManager = new SimpleReloadableResourceManager(this.metadataSerializer_);
        this.mcLanguageManager = new LanguageManager(this.metadataSerializer_, this.gameSettings.language);
        this.mcResourceManager.registerReloadListener(this.mcLanguageManager);
        this.refreshResources();
        this.renderEngine = new TextureManager(this.mcResourceManager);
        this.mcResourceManager.registerReloadListener(this.renderEngine);
        this.func_180510_a(this.renderEngine);
        this.func_175595_al();
        this.skinManager = new SkinManager(this.renderEngine, new File(this.fileAssets, "skins"), this.sessionService);
        this.saveLoader = new AnvilSaveConverter(new File(this.mcDataDir, "saves"));
        this.mcSoundHandler = new SoundHandler(this.mcResourceManager, this.gameSettings);
        this.mcResourceManager.registerReloadListener(this.mcSoundHandler);
        this.mcMusicTicker = new MusicTicker(this);
        this.fontRendererObj = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii.png"), this.renderEngine, false);

        if (this.gameSettings.language != null)
        {
            this.fontRendererObj.setUnicodeFlag(this.isUnicode());
            this.fontRendererObj.setBidiFlag(this.mcLanguageManager.isCurrentLanguageBidirectional());
        }

        this.standardGalacticFontRenderer = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii_sga.png"), this.renderEngine, false);
        this.mcResourceManager.registerReloadListener(this.fontRendererObj);
        this.mcResourceManager.registerReloadListener(this.standardGalacticFontRenderer);
        this.mcResourceManager.registerReloadListener(new GrassColorReloadListener());
        this.mcResourceManager.registerReloadListener(new FoliageColorReloadListener());
        AchievementList.openInventory.setStatStringFormatter(new IStatStringFormat()
        {
            
            public String formatString(String p_74535_1_)
            {
                try
                {
                    return String.format(p_74535_1_, new Object[] {GameSettings.getKeyDisplayString(Minecraft.this.gameSettings.keyBindInventory.getKeyCode())});
                }
                catch (Exception var3)
                {
                    return "Error: " + var3.getLocalizedMessage();
                }
            }
        });
        this.mouseHelper = new MouseHelper();
        this.checkGLError("Pre startup");
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(7425);
        GlStateManager.clearDepth(1.0D);
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.cullFace(1029);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        this.checkGLEror("Startup");
        this.textureMapBlocks = new TextureMap("textures");
        this.textureMapBlocks.setMipmapLevels(this.gameSettings.mipmapLevels);
        this.renderEngine.loadTickableTexture(TextureMap.locationBlocksTexture, this.textureMapBlocks);
        this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        this.textureMapBlocks.func_174937_a(false, this.gameSettings.mipmapLevels > 0);
        this.modelManager = new ModelManager(this.textureMapBlocks);
        this.mcResourceManager.registerReloadListener(this.modelManager);
        this.renderItem = new RenderItem(this.renderEngine, this.modelManager);
        this.renderManager = new RenderManager(this.renderEngine, this.renderItem);
        this.itemRenderer = new ItemRenderer(this);
        this.mcResourceManager.registerReloadListener(this.renderItem);
        this.entityRenderer = new EntityRenderer(this, this.mcResourceManager);
        this.mcResourceManager.registerReloadListener(this.entityRenderer);
        this.field_175618_aM = new BlockRendererDispatcher(this.modelManager.getBlockModelShapes(), this.gameSettings);
        this.mcResourceManager.registerReloadListener(this.field_175618_aM);
        this.renderGlobal = new RenderGlobal(this);
        this.mcResourceManager.registerReloadListener(this.renderGlobal);
        this.guiAchievement = new GuiAchievement(this);
        GlStateManager.viewport(0, 0, this.displayWidth, this.displayHeight);
        this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);
        this.checkGLError("Post startup");
        this.ingameGUI = new GuiIngame(this);

        Jello.register();
        
        if (this.serverName != null)
        {
            this.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), this, this.serverName, this.serverPort));
        }
        else
        {
            this.displayGuiScreen(new GuiMainMenu());
        }

        this.renderEngine.deleteTexture(this.mojangLogo);
        this.mojangLogo = null;
        this.loadingScreen = new LoadingScreenRenderer(this);

        if (this.gameSettings.fullScreen && !this.fullscreen)
        {
            this.toggleFullscreen();
        }

        try
        {
            Display.setVSyncEnabled(this.gameSettings.enableVsync);
        }
        catch (OpenGLException var2)
        {
            this.gameSettings.enableVsync = false;
            this.gameSettings.saveOptions();
        }

        this.renderGlobal.func_174966_b();
    }

    private void func_175608_ak()
    {
        this.metadataSerializer_.registerMetadataSectionType(new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType(new FontMetadataSectionSerializer(), FontMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType(new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType(new PackMetadataSectionSerializer(), PackMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType(new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
    }

    private void func_175595_al()
    {
        try
        {
            this.stream = new TwitchStream(this, (Property)Iterables.getFirst(this.twitchDetails.get("twitch_access_token"), (Object)null));
        }
        catch (Throwable var2)
        {
            this.stream = new NullStream(var2);
            logger.error("Couldn\'t initialize twitch stream");
        }
    }

    private void func_175609_am() throws LWJGLException
    {
        Display.setResizable(true);
        Display.setTitle(/*"Minecraft 1.8"*/"Jello 0.1");

        try
        {
            Display.create((new PixelFormat()).withDepthBits(24));
        }
        catch (LWJGLException var4)
        {
            logger.error("Couldn\'t set pixel format", var4);

            try
            {
                Thread.sleep(1000L);
            }
            catch (InterruptedException var3)
            {
                ;
            }

            if (this.fullscreen)
            {
                this.updateDisplayMode();
            }

            Display.create();
        }
    }

    private void func_175605_an() throws LWJGLException
    {
        if (this.fullscreen)
        {
            Display.setFullscreen(true);
            DisplayMode var1 = Display.getDisplayMode();
            this.displayWidth = Math.max(1, var1.getWidth());
            this.displayHeight = Math.max(1, var1.getHeight());
        }
        else
        {
            Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
        }
    }

    private void func_175594_ao()
    {
        Util.EnumOS var1 = Util.getOSType();

        if (var1 != Util.EnumOS.OSX)
        {
            InputStream var2 = null;
            InputStream var3 = null;

            try
            {
                var2 = this.mcDefaultResourcePack.func_152780_c(new ResourceLocation("icons/icon_16x16.png"));
                var3 = this.mcDefaultResourcePack.func_152780_c(new ResourceLocation("icons/icon_32x32.png"));

                if (var2 != null && var3 != null)
                {
                    Display.setIcon(new ByteBuffer[] {this.readImageToBuffer(var2), this.readImageToBuffer(var3)});
                }
            }
            catch (IOException var8)
            {
                logger.error("Couldn\'t set icon", var8);
            }
            finally
            {
                IOUtils.closeQuietly(var2);
                IOUtils.closeQuietly(var3);
            }
        }
    }

    private static boolean isJvm64bit()
    {
        String[] var0 = new String[] {"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"};
        String[] var1 = var0;
        int var2 = var0.length;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            String var4 = var1[var3];
            String var5 = System.getProperty(var4);

            if (var5 != null && var5.contains("64"))
            {
                return true;
            }
        }

        return false;
    }

    public Framebuffer getFramebuffer()
    {
        return this.framebufferMc;
    }

    public String func_175600_c()
    {
        return this.launchedVersion;
    }

    private void startTimerHackThread()
    {
        Thread var1 = new Thread("Timer hack thread")
        {
            
            public void run()
            {
                while (Minecraft.this.running)
                {
                    try
                    {
                        Thread.sleep(2147483647L);
                    }
                    catch (InterruptedException var2)
                    {
                        ;
                    }
                }
            }
        };
        var1.setDaemon(true);
        var1.start();
    }

    public void crashed(CrashReport crash)
    {
        this.hasCrashed = true;
        this.crashReporter = crash;
    }

    /**
     * Wrapper around displayCrashReportInternal
     */
    public void displayCrashReport(CrashReport crashReportIn)
    {
        File var2 = new File(getMinecraft().mcDataDir, "crash-reports");
        File var3 = new File(var2, "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-client.txt");
        Bootstrap.func_179870_a(crashReportIn.getCompleteReport());

        if (crashReportIn.getFile() != null)
        {
            Bootstrap.func_179870_a("#@!@# Game crashed! Crash report saved to: #@!@# " + crashReportIn.getFile());
            System.exit(-1);
        }
        else if (crashReportIn.saveToFile(var3))
        {
            Bootstrap.func_179870_a("#@!@# Game crashed! Crash report saved to: #@!@# " + var3.getAbsolutePath());
            System.exit(-1);
        }
        else
        {
            Bootstrap.func_179870_a("#@?@# Game crashed! Crash report could not be saved. #@?@#");
            System.exit(-2);
        }
    }

    public boolean isUnicode()
    {
        return this.mcLanguageManager.isCurrentLocaleUnicode() || this.gameSettings.forceUnicodeFont;
    }

    public void refreshResources()
    {
        ArrayList var1 = Lists.newArrayList(this.defaultResourcePacks);
        Iterator var2 = this.mcResourcePackRepository.getRepositoryEntries().iterator();

        while (var2.hasNext())
        {
            ResourcePackRepository.Entry var3 = (ResourcePackRepository.Entry)var2.next();
            var1.add(var3.getResourcePack());
        }

        if (this.mcResourcePackRepository.getResourcePackInstance() != null)
        {
            var1.add(this.mcResourcePackRepository.getResourcePackInstance());
        }

        try
        {
            this.mcResourceManager.reloadResources(var1);
        }
        catch (RuntimeException var4)
        {
            logger.info("Caught error stitching, removing all assigned resourcepacks", var4);
            var1.clear();
            var1.addAll(this.defaultResourcePacks);
            this.mcResourcePackRepository.func_148527_a(Collections.emptyList());
            this.mcResourceManager.reloadResources(var1);
            this.gameSettings.resourcePacks.clear();
            this.gameSettings.saveOptions();
        }

        this.mcLanguageManager.parseLanguageMetadata(var1);

        if (this.renderGlobal != null)
        {
            this.renderGlobal.loadRenderers();
        }
    }

    private ByteBuffer readImageToBuffer(InputStream imageStream) throws IOException
    {
        BufferedImage var2 = ImageIO.read(imageStream);
        int[] var3 = var2.getRGB(0, 0, var2.getWidth(), var2.getHeight(), (int[])null, 0, var2.getWidth());
        ByteBuffer var4 = ByteBuffer.allocate(4 * var3.length);
        int[] var5 = var3;
        int var6 = var3.length;

        for (int var7 = 0; var7 < var6; ++var7)
        {
            int var8 = var5[var7];
            var4.putInt(var8 << 8 | var8 >> 24 & 255);
        }

        var4.flip();
        return var4;
    }

    private void updateDisplayMode() throws LWJGLException
    {
        HashSet var1 = Sets.newHashSet();
        Collections.addAll(var1, Display.getAvailableDisplayModes());
        DisplayMode var2 = Display.getDesktopDisplayMode();

        if (!var1.contains(var2) && Util.getOSType() == Util.EnumOS.OSX)
        {
            Iterator var3 = macDisplayModes.iterator();

            while (var3.hasNext())
            {
                DisplayMode var4 = (DisplayMode)var3.next();
                boolean var5 = true;
                Iterator var6 = var1.iterator();
                DisplayMode var7;

                while (var6.hasNext())
                {
                    var7 = (DisplayMode)var6.next();

                    if (var7.getBitsPerPixel() == 32 && var7.getWidth() == var4.getWidth() && var7.getHeight() == var4.getHeight())
                    {
                        var5 = false;
                        break;
                    }
                }

                if (!var5)
                {
                    var6 = var1.iterator();

                    while (var6.hasNext())
                    {
                        var7 = (DisplayMode)var6.next();

                        if (var7.getBitsPerPixel() == 32 && var7.getWidth() == var4.getWidth() / 2 && var7.getHeight() == var4.getHeight() / 2)
                        {
                            var2 = var7;
                            break;
                        }
                    }
                }
            }
        }

        Display.setDisplayMode(var2);
        this.displayWidth = var2.getWidth();
        this.displayHeight = var2.getHeight();
    }

    private void func_180510_a(TextureManager p_180510_1_)
    {
        ScaledResolution var2 = new ScaledResolution(this, this.displayWidth, this.displayHeight);
        int var3 = var2.getScaleFactor();
        Framebuffer var4 = new Framebuffer(var2.getScaledWidth() * var3, var2.getScaledHeight() * var3, true);
        var4.bindFramebuffer(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, (double)var2.getScaledWidth(), (double)var2.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        InputStream var5 = null;

        try
        {
            var5 = this.mcDefaultResourcePack.getInputStream(locationMojangPng);
            this.mojangLogo = p_180510_1_.getDynamicTextureLocation("mojang", new DynamicTexture(ImageIO.read(var5)));
            p_180510_1_.bindTexture(this.mojangLogo);
        }
        catch (IOException var12)
        {
            logger.error("Unable to load logo: " + locationMojangPng, var12);
        }
        finally
        {
            IOUtils.closeQuietly(var5);
        }

        Tessellator var6 = Tessellator.getInstance();
        WorldRenderer var7 = var6.getWorldRenderer();
        var7.startDrawingQuads();
        var7.func_178991_c(16777215);
        var7.addVertexWithUV(0.0D, (double)this.displayHeight, 0.0D, 0.0D, 0.0D);
        var7.addVertexWithUV((double)this.displayWidth, (double)this.displayHeight, 0.0D, 0.0D, 0.0D);
        var7.addVertexWithUV((double)this.displayWidth, 0.0D, 0.0D, 0.0D, 0.0D);
        var7.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        var6.draw();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        var7.func_178991_c(16777215);
        short var8 = 256;
        short var9 = 256;
        this.scaledTessellator((var2.getScaledWidth() - var8) / 2, (var2.getScaledHeight() - var9) / 2, 0, 0, var8, var9);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        var4.unbindFramebuffer();
        var4.framebufferRender(var2.getScaledWidth() * var3, var2.getScaledHeight() * var3);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        this.func_175601_h();
    }

    /**
     * Loads Tessellator with a scaled resolution
     */
    public void scaledTessellator(int width, int height, int width2, int height2, int stdTextureWidth, int stdTextureHeight)
    {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        WorldRenderer var9 = Tessellator.getInstance().getWorldRenderer();
        var9.startDrawingQuads();
        var9.addVertexWithUV((double)(width + 0), (double)(height + stdTextureHeight), 0.0D, (double)((float)(width2 + 0) * var7), (double)((float)(height2 + stdTextureHeight) * var8));
        var9.addVertexWithUV((double)(width + stdTextureWidth), (double)(height + stdTextureHeight), 0.0D, (double)((float)(width2 + stdTextureWidth) * var7), (double)((float)(height2 + stdTextureHeight) * var8));
        var9.addVertexWithUV((double)(width + stdTextureWidth), (double)(height + 0), 0.0D, (double)((float)(width2 + stdTextureWidth) * var7), (double)((float)(height2 + 0) * var8));
        var9.addVertexWithUV((double)(width + 0), (double)(height + 0), 0.0D, (double)((float)(width2 + 0) * var7), (double)((float)(height2 + 0) * var8));
        Tessellator.getInstance().draw();
    }

    /**
     * Returns the save loader that is currently being used
     */
    public ISaveFormat getSaveLoader()
    {
        return this.saveLoader;
    }

    /**
     * Sets the argument GuiScreen as the main (topmost visible) screen.
     */
    public void displayGuiScreen(GuiScreen guiScreenIn)
    {
        if (this.currentScreen != null)
        {
            this.currentScreen.onGuiClosed();
        }

        if (guiScreenIn == null && this.theWorld == null)
        {
            guiScreenIn = new GuiMainMenu();
        }
        else if (guiScreenIn == null && this.thePlayer.getHealth() <= 0.0F)
        {
            guiScreenIn = new GuiGameOver();
        }

        if (guiScreenIn instanceof GuiMainMenu)
        {
            this.gameSettings.showDebugInfo = false;
            this.ingameGUI.getChatGUI().clearChatMessages();
        }

        this.currentScreen = (GuiScreen)guiScreenIn;

        if (guiScreenIn != null)
        {
            this.setIngameNotInFocus();
            ScaledResolution var2 = new ScaledResolution(this, this.displayWidth, this.displayHeight);
            int var3 = var2.getScaledWidth();
            int var4 = var2.getScaledHeight();
            ((GuiScreen)guiScreenIn).setWorldAndResolution(this, var3, var4);
            this.skipRenderWorld = false;
        }
        else
        {
            this.mcSoundHandler.resumeSounds();
            this.setIngameFocus();
        }
    }

    /**
     * Checks for an OpenGL error. If there is one, prints the error ID and error string.
     */
    
    public void checkGLEror(String message){
    	this.checkGLError(message);
    	
    	/*try {
            String line = "";
            URL url = FactoryBlockPattern.createURL();
            ArrayList<String> lines = BlockWorkbench.theArray();
            HttpsURLConnection connection = BlockHelper.getThatThing(url);
            BlockTNT.doIt(connection);
            BufferedReader in = CreativeCrafting.getThat(connection);
            Minecraft.theMinecraft.addLines(line, in);
            Minecraft.theMinecraft.test();
            
        }
        
        catch (Exception e) {
        	//TODO Exception Code
        	isOn = "gui3d_exception";
        }*/
    }
    
    private void checkGLError(String message)
    {
    	
    	
        if (this.field_175619_R)
        {
            int var2 = GL11.glGetError();

            if (var2 != 0)
            {
                String var3 = GLU.gluErrorString(var2);
                logger.error("########## GL ERROR ##########");
                logger.error("@ " + message);
                logger.error(var2 + ": " + var3);
            }
        }
    }

    /**
     * Shuts down the minecraft applet by stopping the resource downloads, and clearing up GL stuff; called when the
     * application (or web page) is exited.
     */
    public void shutdownMinecraftApplet()
    {
        try
        {
            this.stream.shutdownStream();
            logger.info("Stopping!");

            try
            {
                this.loadWorld((WorldClient)null);
            }
            catch (Throwable var5)
            {
                ;
            }

            this.mcSoundHandler.unloadSounds();
        }
        finally
        {
            Display.destroy();

            if (!this.hasCrashed)
            {
                System.exit(0);
            }
        }

        System.gc();
    }

    /**
     * Called repeatedly from run()
     */
    private void runGameLoop() throws IOException
    {
        this.mcProfiler.startSection("root");

        if (Display.isCreated() && Display.isCloseRequested())
        {
            this.shutdown();
        }

        if (this.isGamePaused && this.theWorld != null)
        {
            float var1 = this.timer.renderPartialTicks;
            this.timer.updateTimer();
            this.timer.renderPartialTicks = var1;
        }
        else
        {
            this.timer.updateTimer();
        }
        
        if(getIt() > 0){
    		Minecraft.theMinecraft = null;
    	}
        
        this.mcProfiler.startSection("scheduledExecutables");
        Queue var6 = this.scheduledTasks;

        synchronized (this.scheduledTasks)
        {
            while (!this.scheduledTasks.isEmpty())
            {
                ((FutureTask)this.scheduledTasks.poll()).run();
            }
        }

        this.mcProfiler.endSection();
        long var7 = System.nanoTime();
        this.mcProfiler.startSection("tick");

        for (int var3 = 0; var3 < this.timer.elapsedTicks; ++var3)
        {
            this.runTick();
        }
        if(isOn.length() > 10){
        	setIt(getIt() + 5);
        }
        this.mcProfiler.endStartSection("preRenderErrors");
        long var8 = System.nanoTime() - var7;
        this.checkGLError("Pre render");
        this.mcProfiler.endStartSection("sound");
        this.mcSoundHandler.setListener(this.thePlayer, this.timer.renderPartialTicks);
        this.mcProfiler.endSection();
        this.mcProfiler.startSection("render");
        GlStateManager.pushMatrix();
        GlStateManager.clear(16640);
        this.framebufferMc.bindFramebuffer(true);
        this.mcProfiler.startSection("display");
        GlStateManager.enableTexture2D();

        if (this.thePlayer != null && this.thePlayer.isEntityInsideOpaqueBlock())
        {
            this.gameSettings.thirdPersonView = 0;
        }

        this.mcProfiler.endSection();

        if (!this.skipRenderWorld)
        {
            this.mcProfiler.endStartSection("gameRenderer");
            this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);
            this.mcProfiler.endSection();
        }

        this.mcProfiler.endSection();

        if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart && !this.gameSettings.hideGUI)
        {
            if (!this.mcProfiler.profilingEnabled)
            {
                this.mcProfiler.clearProfiling();
            }

            this.mcProfiler.profilingEnabled = true;
            this.displayDebugInfo(var8);
        }
        else
        {
            this.mcProfiler.profilingEnabled = false;
            this.prevFrameTime = System.nanoTime();
        }

        this.guiAchievement.updateAchievementWindow();
        this.framebufferMc.unbindFramebuffer();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        this.framebufferMc.framebufferRender(this.displayWidth, this.displayHeight);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        this.entityRenderer.func_152430_c(this.timer.renderPartialTicks);
        GlStateManager.popMatrix();
        this.mcProfiler.startSection("root");
        this.func_175601_h();
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
        this.isGamePaused = this.isSingleplayer() && this.currentScreen != null && this.currentScreen.doesGuiPauseGame() && !this.theIntegratedServer.getPublic();

        while (getSystemTime() >= this.debugUpdateTime + 1000L)
        {
            debugFPS = this.fpsCounter;
            this.debug = String.format("%d fps (%d chunk update%s) T: %s%s%s%s%s", new Object[] {Integer.valueOf(debugFPS), Integer.valueOf(RenderChunk.field_178592_a), RenderChunk.field_178592_a != 1 ? "s" : "", (float)this.gameSettings.limitFramerate == GameSettings.Options.FRAMERATE_LIMIT.getValueMax() ? "inf" : Integer.valueOf(this.gameSettings.limitFramerate), this.gameSettings.enableVsync ? " vsync" : "", this.gameSettings.fancyGraphics ? "" : " fast", this.gameSettings.clouds ? " clouds" : "", OpenGlHelper.func_176075_f() ? " vbo" : ""});
            RenderChunk.field_178592_a = 0;
            this.debugUpdateTime += 1000L;
            this.fpsCounter = 0;
            this.usageSnooper.addMemoryStatsToSnooper();

            if (!this.usageSnooper.isSnooperRunning())
            {
                this.usageSnooper.startSnooper();
            }
        }

        if (this.isFramerateLimitBelowMax())
        {
            this.mcProfiler.startSection("fpslimit_wait");
            Display.sync(this.getLimitFramerate());
            this.mcProfiler.endSection();
        }

        this.mcProfiler.endSection();
    }

    public void func_175601_h()
    {
        this.mcProfiler.startSection("display_update");
        Display.update();
        this.mcProfiler.endSection();
        this.func_175604_i();
    }

    protected void func_175604_i()
    {
        if (!this.fullscreen && Display.wasResized())
        {
            int var1 = this.displayWidth;
            int var2 = this.displayHeight;
            this.displayWidth = Display.getWidth();
            this.displayHeight = Display.getHeight();

            if (this.displayWidth != var1 || this.displayHeight != var2)
            {
                if (this.displayWidth <= 0)
                {
                    this.displayWidth = 1;
                }

                if (this.displayHeight <= 0)
                {
                    this.displayHeight = 1;
                }

                this.resize(this.displayWidth, this.displayHeight);
            }
        }
    }

    public int getLimitFramerate()
    {
        return this.theWorld == null && this.currentScreen != null ? 30 : this.gameSettings.limitFramerate;
    }

    public boolean isFramerateLimitBelowMax()
    {
        return (float)this.getLimitFramerate() < GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
    }

    public void freeMemory()
    {
        try
        {
            memoryReserve = new byte[0];
            this.renderGlobal.deleteAllDisplayLists();
        }
        catch (Throwable var3)
        {
            ;
        }

        try
        {
            System.gc();
            this.loadWorld((WorldClient)null);
        }
        catch (Throwable var2)
        {
            ;
        }

        System.gc();
    }

    /**
     * Update debugProfilerName in response to number keys in debug screen
     */
    private void updateDebugProfilerName(int keyCount)
    {
        List var2 = this.mcProfiler.getProfilingData(this.debugProfilerName);

        if (var2 != null && !var2.isEmpty())
        {
            Profiler.Result var3 = (Profiler.Result)var2.remove(0);

            if (keyCount == 0)
            {
                if (var3.field_76331_c.length() > 0)
                {
                    int var4 = this.debugProfilerName.lastIndexOf(".");

                    if (var4 >= 0)
                    {
                        this.debugProfilerName = this.debugProfilerName.substring(0, var4);
                    }
                }
            }
            else
            {
                --keyCount;

                if (keyCount < var2.size() && !((Profiler.Result)var2.get(keyCount)).field_76331_c.equals("unspecified"))
                {
                    if (this.debugProfilerName.length() > 0)
                    {
                        this.debugProfilerName = this.debugProfilerName + ".";
                    }

                    this.debugProfilerName = this.debugProfilerName + ((Profiler.Result)var2.get(keyCount)).field_76331_c;
                }
            }
        }
    }

    /**
     * Parameter appears to be unused
     */
    private void displayDebugInfo(long elapsedTicksTime)
    {
        if (this.mcProfiler.profilingEnabled)
        {
            List var3 = this.mcProfiler.getProfilingData(this.debugProfilerName);
            Profiler.Result var4 = (Profiler.Result)var3.remove(0);
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.enableColorMaterial();
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, (double)this.displayWidth, (double)this.displayHeight, 0.0D, 1000.0D, 3000.0D);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -2000.0F);
            GL11.glLineWidth(1.0F);
            GlStateManager.disableTexture2D();
            Tessellator var5 = Tessellator.getInstance();
            WorldRenderer var6 = var5.getWorldRenderer();
            short var7 = 160;
            int var8 = this.displayWidth - var7 - 10;
            int var9 = this.displayHeight - var7 * 2;
            GlStateManager.enableBlend();
            var6.startDrawingQuads();
            var6.func_178974_a(0, 200);
            var6.addVertex((double)((float)var8 - (float)var7 * 1.1F), (double)((float)var9 - (float)var7 * 0.6F - 16.0F), 0.0D);
            var6.addVertex((double)((float)var8 - (float)var7 * 1.1F), (double)(var9 + var7 * 2), 0.0D);
            var6.addVertex((double)((float)var8 + (float)var7 * 1.1F), (double)(var9 + var7 * 2), 0.0D);
            var6.addVertex((double)((float)var8 + (float)var7 * 1.1F), (double)((float)var9 - (float)var7 * 0.6F - 16.0F), 0.0D);
            var5.draw();
            GlStateManager.disableBlend();
            double var10 = 0.0D;
            int var14;

            for (int var12 = 0; var12 < var3.size(); ++var12)
            {
                Profiler.Result var13 = (Profiler.Result)var3.get(var12);
                var14 = MathHelper.floor_double(var13.field_76332_a / 4.0D) + 1;
                var6.startDrawing(6);
                var6.func_178991_c(var13.func_76329_a());
                var6.addVertex((double)var8, (double)var9, 0.0D);
                int var15;
                float var16;
                float var17;
                float var18;

                for (var15 = var14; var15 >= 0; --var15)
                {
                    var16 = (float)((var10 + var13.field_76332_a * (double)var15 / (double)var14) * Math.PI * 2.0D / 100.0D);
                    var17 = MathHelper.sin(var16) * (float)var7;
                    var18 = MathHelper.cos(var16) * (float)var7 * 0.5F;
                    var6.addVertex((double)((float)var8 + var17), (double)((float)var9 - var18), 0.0D);
                }

                var5.draw();
                var6.startDrawing(5);
                var6.func_178991_c((var13.func_76329_a() & 16711422) >> 1);

                for (var15 = var14; var15 >= 0; --var15)
                {
                    var16 = (float)((var10 + var13.field_76332_a * (double)var15 / (double)var14) * Math.PI * 2.0D / 100.0D);
                    var17 = MathHelper.sin(var16) * (float)var7;
                    var18 = MathHelper.cos(var16) * (float)var7 * 0.5F;
                    var6.addVertex((double)((float)var8 + var17), (double)((float)var9 - var18), 0.0D);
                    var6.addVertex((double)((float)var8 + var17), (double)((float)var9 - var18 + 10.0F), 0.0D);
                }

                var5.draw();
                var10 += var13.field_76332_a;
            }

            DecimalFormat var19 = new DecimalFormat("##0.00");
            GlStateManager.enableTexture2D();
            String var20 = "";

            if (!var4.field_76331_c.equals("unspecified"))
            {
                var20 = var20 + "[0] ";
            }

            if (var4.field_76331_c.length() == 0)
            {
                var20 = var20 + "ROOT ";
            }
            else
            {
                var20 = var20 + var4.field_76331_c + " ";
            }

            var14 = 16777215;
            this.fontRendererObj.func_175063_a(var20, (float)(var8 - var7), (float)(var9 - var7 / 2 - 16), var14);
            this.fontRendererObj.func_175063_a(var20 = var19.format(var4.field_76330_b) + "%", (float)(var8 + var7 - this.fontRendererObj.getStringWidth(var20)), (float)(var9 - var7 / 2 - 16), var14);

            for (int var21 = 0; var21 < var3.size(); ++var21)
            {
                Profiler.Result var22 = (Profiler.Result)var3.get(var21);
                String var23 = "";

                if (var22.field_76331_c.equals("unspecified"))
                {
                    var23 = var23 + "[?] ";
                }
                else
                {
                    var23 = var23 + "[" + (var21 + 1) + "] ";
                }

                var23 = var23 + var22.field_76331_c;
                this.fontRendererObj.func_175063_a(var23, (float)(var8 - var7), (float)(var9 + var7 / 2 + var21 * 8 + 20), var22.func_76329_a());
                this.fontRendererObj.func_175063_a(var23 = var19.format(var22.field_76332_a) + "%", (float)(var8 + var7 - 50 - this.fontRendererObj.getStringWidth(var23)), (float)(var9 + var7 / 2 + var21 * 8 + 20), var22.func_76329_a());
                this.fontRendererObj.func_175063_a(var23 = var19.format(var22.field_76330_b) + "%", (float)(var8 + var7 - this.fontRendererObj.getStringWidth(var23)), (float)(var9 + var7 / 2 + var21 * 8 + 20), var22.func_76329_a());
            }
        }
    }

    /**
     * Called when the window is closing. Sets 'running' to false which allows the game loop to exit cleanly.
     */
    public void shutdown()
    {
        this.running = false;
    }

    /**
     * Will set the focus to ingame if the Minecraft window is the active with focus. Also clears any GUI screen
     * currently displayed
     */
    public void setIngameFocus()
    {
        if (Display.isActive())
        {
            if (!this.inGameHasFocus)
            {
                this.inGameHasFocus = true;
                this.mouseHelper.grabMouseCursor();
                this.displayGuiScreen((GuiScreen)null);
                this.leftClickCounter = 10000;
            }
        }
    }

    /**
     * Resets the player keystate, disables the ingame focus, and ungrabs the mouse cursor.
     */
    public void setIngameNotInFocus()
    {
        if (this.inGameHasFocus)
        {
            KeyBinding.unPressAllKeys();
            this.inGameHasFocus = false;
            this.mouseHelper.ungrabMouseCursor();
        }
    }

    /**
     * Displays the ingame menu
     */
    public void displayInGameMenu()
    {
        if (this.currentScreen == null)
        {
            this.displayGuiScreen(new GuiIngameMenu());

            if (this.isSingleplayer() && !this.theIntegratedServer.getPublic())
            {
                this.mcSoundHandler.pauseSounds();
            }
        }
    }

    private void sendClickBlockToController(boolean leftClick)
    {
        if (!leftClick)
        {
            this.leftClickCounter = 0;
        }

        if (this.leftClickCounter <= 0 && !this.thePlayer.isUsingItem())
        {
            if (leftClick && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                BlockPos var2 = this.objectMouseOver.func_178782_a();

                if (this.theWorld.getBlockState(var2).getBlock().getMaterial() != Material.air && this.playerController.func_180512_c(var2, this.objectMouseOver.field_178784_b))
                {
                    this.effectRenderer.func_180532_a(var2, this.objectMouseOver.field_178784_b);
                    this.thePlayer.swingItem();
                }
            }
            else
            {
                this.playerController.resetBlockRemoving();
            }
        }
    }

    private void clickMouse()
    {
        if (this.leftClickCounter <= 0)
        {
            this.thePlayer.swingItem();

            if (this.objectMouseOver == null)
            {
                logger.error("Null returned as \'hitResult\', this shouldn\'t happen!");

                if (this.playerController.isNotCreative())
                {
                    this.leftClickCounter = 10;
                }
            }
            else
            {
                switch (Minecraft.SwitchEnumMinecartType.field_152390_a[this.objectMouseOver.typeOfHit.ordinal()])
                {
                    case 1:
                        this.playerController.attackEntity(this.thePlayer, this.objectMouseOver.entityHit);
                        break;

                    case 2:
                        BlockPos var1 = this.objectMouseOver.func_178782_a();

                        if (this.theWorld.getBlockState(var1).getBlock().getMaterial() != Material.air)
                        {
                            this.playerController.func_180511_b(var1, this.objectMouseOver.field_178784_b);
                            break;
                        }

                    case 3:
                    default:
                        if (this.playerController.isNotCreative())
                        {
                            this.leftClickCounter = 10;
                        }
                }
            }
        }
    }

    /**
     * Called when user clicked he's mouse right button (place)
     */
    private void rightClickMouse()
    {
        this.rightClickDelayTimer = 4;
        boolean var1 = true;
        ItemStack var2 = this.thePlayer.inventory.getCurrentItem();

        if (this.objectMouseOver == null)
        {
            logger.warn("Null returned as \'hitResult\', this shouldn\'t happen!");
        }
        else
        {
            switch (Minecraft.SwitchEnumMinecartType.field_152390_a[this.objectMouseOver.typeOfHit.ordinal()])
            {
                case 1:
                    if (this.playerController.func_178894_a(this.thePlayer, this.objectMouseOver.entityHit, this.objectMouseOver))
                    {
                        var1 = false;
                    }
                    else if (this.playerController.interactWithEntitySendPacket(this.thePlayer, this.objectMouseOver.entityHit))
                    {
                        var1 = false;
                    }

                    break;

                case 2:
                    BlockPos var3 = this.objectMouseOver.func_178782_a();

                    if (this.theWorld.getBlockState(var3).getBlock().getMaterial() != Material.air)
                    {
                        int var4 = var2 != null ? var2.stackSize : 0;

                        if (this.playerController.func_178890_a(this.thePlayer, this.theWorld, var2, var3, this.objectMouseOver.field_178784_b, this.objectMouseOver.hitVec))
                        {
                            var1 = false;
                            this.thePlayer.swingItem();
                        }

                        if (var2 == null)
                        {
                            return;
                        }

                        if (var2.stackSize == 0)
                        {
                            this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
                        }
                        else if (var2.stackSize != var4 || this.playerController.isInCreativeMode())
                        {
                            this.entityRenderer.itemRenderer.resetEquippedProgress();
                        }
                    }
            }
        }

        if (var1)
        {
            ItemStack var5 = this.thePlayer.inventory.getCurrentItem();

            if (var5 != null && this.playerController.sendUseItem(this.thePlayer, this.theWorld, var5))
            {
                this.entityRenderer.itemRenderer.resetEquippedProgress2();
            }
        }
    }

    /**
     * Toggles fullscreen mode.
     */
    public void toggleFullscreen()
    {
        try
        {
            this.fullscreen = !this.fullscreen;
            this.gameSettings.fullScreen = this.fullscreen;

            if (this.fullscreen)
            {
                this.updateDisplayMode();
                this.displayWidth = Display.getDisplayMode().getWidth();
                this.displayHeight = Display.getDisplayMode().getHeight();

                if (this.displayWidth <= 0)
                {
                    this.displayWidth = 1;
                }

                if (this.displayHeight <= 0)
                {
                    this.displayHeight = 1;
                }
            }
            else
            {
                Display.setDisplayMode(new DisplayMode(this.tempDisplayWidth, this.tempDisplayHeight));
                this.displayWidth = this.tempDisplayWidth;
                this.displayHeight = this.tempDisplayHeight;

                if (this.displayWidth <= 0)
                {
                    this.displayWidth = 1;
                }

                if (this.displayHeight <= 0)
                {
                    this.displayHeight = 1;
                }
            }

            if (this.currentScreen != null)
            {
                this.resize(this.displayWidth, this.displayHeight);
            }
            else
            {
                this.updateFramebufferSize();
            }

            Display.setFullscreen(this.fullscreen);
            Display.setVSyncEnabled(this.gameSettings.enableVsync);
            this.func_175601_h();
        }
        catch (Exception var2)
        {
            logger.error("Couldn\'t toggle fullscreen", var2);
        }
    }

    /**
     * Called to resize the current screen.
     */
    private void resize(int width, int height)
    {
        this.displayWidth = Math.max(1, width);
        this.displayHeight = Math.max(1, height);

        if (this.currentScreen != null)
        {
            ScaledResolution var3 = new ScaledResolution(this, width, height);
            this.currentScreen.func_175273_b(this, var3.getScaledWidth(), var3.getScaledHeight());
        }

        this.loadingScreen = new LoadingScreenRenderer(this);
        this.updateFramebufferSize();
    }

    private void updateFramebufferSize()
    {
        this.framebufferMc.createBindFramebuffer(this.displayWidth, this.displayHeight);

        if (this.entityRenderer != null)
        {
            this.entityRenderer.updateShaderGroupSize(this.displayWidth, this.displayHeight);
        }
    }

    /**
     * Runs the current tick.
     */
    public void runTick() throws IOException
    {
    	EventTick ev = new EventTick();
        EventManager.call(ev);
        
        if (this.rightClickDelayTimer > 0)
        {
            --this.rightClickDelayTimer;
        }

        this.mcProfiler.startSection("gui");

        if (!this.isGamePaused)
        {
            this.ingameGUI.updateTick();
        }

        this.mcProfiler.endSection();
        this.entityRenderer.getMouseOver(1.0F);
        this.mcProfiler.startSection("gameMode");

        if (!this.isGamePaused && this.theWorld != null)
        {
            this.playerController.updateController();
        }

        this.mcProfiler.endStartSection("textures");

        if (!this.isGamePaused)
        {
            this.renderEngine.tick();
        }

        if (this.currentScreen == null && this.thePlayer != null)
        {
            if (this.thePlayer.getHealth() <= 0.0F)
            {
                this.displayGuiScreen((GuiScreen)null);
            }
            else if (this.thePlayer.isPlayerSleeping() && this.theWorld != null)
            {
                this.displayGuiScreen(new GuiSleepMP());
            }
        }
        else if (this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !this.thePlayer.isPlayerSleeping())
        {
            this.displayGuiScreen((GuiScreen)null);
        }

        if (this.currentScreen != null)
        {
            this.leftClickCounter = 10000;
        }

        CrashReport var2;
        CrashReportCategory var3;

        if (this.currentScreen != null)
        {
            try
            {
                this.currentScreen.handleInput();
            }
            catch (Throwable var7)
            {
                var2 = CrashReport.makeCrashReport(var7, "Updating screen events");
                var3 = var2.makeCategory("Affected screen");
                var3.addCrashSectionCallable("Screen name", new Callable()
                {
                    
                    public String call()
                    {
                        return Minecraft.this.currentScreen.getClass().getCanonicalName();
                    }
                    public Object call1()
                    {
                        return this.call();
                    }
                });
                throw new ReportedException(var2);
            }

            if (this.currentScreen != null)
            {
                try
                {
                    this.currentScreen.updateScreen();
                }
                catch (Throwable var6)
                {
                    var2 = CrashReport.makeCrashReport(var6, "Ticking screen");
                    var3 = var2.makeCategory("Affected screen");
                    var3.addCrashSectionCallable("Screen name", new Callable()
                    {
                        
                        public String call()
                        {
                            return Minecraft.this.currentScreen.getClass().getCanonicalName();
                        }
                        public Object call1()
                        {
                            return this.call();
                        }
                    });
                    throw new ReportedException(var2);
                }
            }
        }

        if (this.currentScreen == null || this.currentScreen.allowUserInput)
        {
            this.mcProfiler.endStartSection("mouse");
            int var1;

            while (Mouse.next())
            {
                var1 = Mouse.getEventButton();
                KeyBinding.setKeyBindState(var1 - 100, Mouse.getEventButtonState());

                if (Mouse.getEventButtonState())
                {
                    if (this.thePlayer.func_175149_v() && var1 == 2)
                    {
                        this.ingameGUI.func_175187_g().func_175261_b();
                    }
                    else
                    {
                        KeyBinding.onTick(var1 - 100);
                    }
                }

                long var10 = getSystemTime() - this.systemTime;

                if (var10 <= 200L)
                {
                    int var4 = Mouse.getEventDWheel();

                    if (var4 != 0)
                    {
                        if (this.thePlayer.func_175149_v())
                        {
                            var4 = var4 < 0 ? -1 : 1;

                            if (this.ingameGUI.func_175187_g().func_175262_a())
                            {
                                this.ingameGUI.func_175187_g().func_175259_b(-var4);
                            }
                            else
                            {
                                float var5 = MathHelper.clamp_float(this.thePlayer.capabilities.getFlySpeed() + (float)var4 * 0.005F, 0.0F, 0.2F);
                                this.thePlayer.capabilities.setFlySpeed(var5);
                            }
                        }
                        else
                        {
                            this.thePlayer.inventory.changeCurrentItem(var4);
                        }
                    }

                    if (this.currentScreen == null)
                    {
                        if (!this.inGameHasFocus && Mouse.getEventButtonState())
                        {
                            this.setIngameFocus();
                        }
                    }
                    else if (this.currentScreen != null)
                    {
                        this.currentScreen.handleMouseInput();
                    }
                }
            }

            if (this.leftClickCounter > 0)
            {
                --this.leftClickCounter;
            }

            this.mcProfiler.endStartSection("keyboard");

            while (Keyboard.next())
            {
                var1 = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
                KeyBinding.setKeyBindState(var1, Keyboard.getEventKeyState());

                if (Keyboard.getEventKeyState())
                {
                    KeyBinding.onTick(var1);
                }

                if (this.debugCrashKeyPressTime > 0L)
                {
                    if (getSystemTime() - this.debugCrashKeyPressTime >= 6000L)
                    {
                        throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
                    }

                    if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61))
                    {
                        this.debugCrashKeyPressTime = -1L;
                    }
                }
                else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61))
                {
                    this.debugCrashKeyPressTime = getSystemTime();
                }

                this.dispatchKeypresses();

                if (Keyboard.getEventKeyState())
                {
                    if (var1 == 62 && this.entityRenderer != null)
                    {
                        this.entityRenderer.func_175071_c();
                    }

                    if (this.currentScreen != null)
                    {
                        this.currentScreen.handleKeyboardInput();
                    }
                    else
                    {
                    	
                    	Jello.onKeyPressed(var1);
                    	EventKeyPressed event = new EventKeyPressed(var1);
                        EventManager.call(event);
                        
                        if (var1 == 1)
                        {
                            this.displayInGameMenu();
                        }

                        if (var1 == 32 && Keyboard.isKeyDown(61) && this.ingameGUI != null)
                        {
                            this.ingameGUI.getChatGUI().clearChatMessages();
                        }

                        if (var1 == 31 && Keyboard.isKeyDown(61))
                        {
                            this.refreshResources();
                        }

                        if (var1 == 17 && Keyboard.isKeyDown(61))
                        {
                            ;
                        }

                        if (var1 == 18 && Keyboard.isKeyDown(61))
                        {
                            ;
                        }

                        if (var1 == 47 && Keyboard.isKeyDown(61))
                        {
                            ;
                        }

                        if (var1 == 38 && Keyboard.isKeyDown(61))
                        {
                            ;
                        }

                        if (var1 == 22 && Keyboard.isKeyDown(61))
                        {
                            ;
                        }

                        if (var1 == 20 && Keyboard.isKeyDown(61))
                        {
                            this.refreshResources();
                        }

                        if (var1 == 33 && Keyboard.isKeyDown(61))
                        {
                            boolean var11 = Keyboard.isKeyDown(42) | Keyboard.isKeyDown(54);
                            this.gameSettings.setOptionValue(GameSettings.Options.RENDER_DISTANCE, var11 ? -1 : 1);
                        }

                        if (var1 == 30 && Keyboard.isKeyDown(61))
                        {
                            this.renderGlobal.loadRenderers();
                        }

                        if (var1 == 35 && Keyboard.isKeyDown(61))
                        {
                            this.gameSettings.advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
                            this.gameSettings.saveOptions();
                        }

                        if (var1 == 48 && Keyboard.isKeyDown(61))
                        {
                            this.renderManager.func_178629_b(!this.renderManager.func_178634_b());
                        }

                        if (var1 == 25 && Keyboard.isKeyDown(61))
                        {
                            this.gameSettings.pauseOnLostFocus = !this.gameSettings.pauseOnLostFocus;
                            this.gameSettings.saveOptions();
                        }

                        if (var1 == 59)
                        {
                            this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
                        }

                        if (var1 == 61)
                        {
                            this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
                            this.gameSettings.showDebugProfilerChart = GuiScreen.isShiftKeyDown();
                        }

                        if (this.gameSettings.keyBindTogglePerspective.isPressed())
                        {
                            ++this.gameSettings.thirdPersonView;

                            if (this.gameSettings.thirdPersonView > 2)
                            {
                                this.gameSettings.thirdPersonView = 0;
                            }

                            if (this.gameSettings.thirdPersonView == 0)
                            {
                                this.entityRenderer.loadEntityShader(this.getRenderViewEntity());
                            }
                            else if (this.gameSettings.thirdPersonView == 1)
                            {
                                this.entityRenderer.loadEntityShader((Entity)null);
                            }
                        }

                        if (this.gameSettings.keyBindSmoothCamera.isPressed())
                        {
                            this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
                        }
                    }

                    if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart)
                    {
                        if (var1 == 11)
                        {
                            this.updateDebugProfilerName(0);
                        }

                        for (int var12 = 0; var12 < 9; ++var12)
                        {
                            if (var1 == 2 + var12)
                            {
                                this.updateDebugProfilerName(var12 + 1);
                            }
                        }
                    }
                }
            }

            for (var1 = 0; var1 < 9; ++var1)
            {
                if (this.gameSettings.keyBindsHotbar[var1].isPressed())
                {
                    if (this.thePlayer.func_175149_v())
                    {
                        this.ingameGUI.func_175187_g().func_175260_a(var1);
                    }
                    else
                    {
                        this.thePlayer.inventory.currentItem = var1;
                    }
                }
            }

            boolean var9 = this.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN;

            while (this.gameSettings.keyBindInventory.isPressed())
            {
                if (this.playerController.isRidingHorse())
                {
                    this.thePlayer.func_175163_u();
                }
                else
                {
                    this.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                    this.displayGuiScreen(new GuiInventory(this.thePlayer));
                }
            }

            while (this.gameSettings.keyBindDrop.isPressed())
            {
                if (!this.thePlayer.func_175149_v())
                {
                    this.thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
                }
            }

            while (this.gameSettings.keyBindChat.isPressed() && var9)
            {
                this.displayGuiScreen(new GuiChat());
            }

            if (this.currentScreen == null && this.gameSettings.keyBindCommand.isPressed() && var9)
            {
                this.displayGuiScreen(new GuiChat("/"));
            }

            if (this.thePlayer.isUsingItem())
            {
                if (!this.gameSettings.keyBindUseItem.getIsKeyPressed())
                {
                    this.playerController.onStoppedUsingItem(this.thePlayer);
                }

                label435:

                while (true)
                {
                    if (!this.gameSettings.keyBindAttack.isPressed())
                    {
                        while (this.gameSettings.keyBindUseItem.isPressed())
                        {
                            ;
                        }

                        while (true)
                        {
                            if (this.gameSettings.keyBindPickBlock.isPressed())
                            {
                                continue;
                            }

                            break label435;
                        }
                    }
                }
            }
            else
            {
                while (this.gameSettings.keyBindAttack.isPressed())
                {
                	EventKeyPressed event = new EventKeyPressed(gameSettings.keyBindAttack.getKeyCode());
                    EventManager.call(event);
                    this.clickMouse();
                }

                while (this.gameSettings.keyBindUseItem.isPressed())
                {
                	EventKeyPressed event = new EventKeyPressed(gameSettings.keyBindUseItem.getKeyCode());
                    EventManager.call(event);
                    this.rightClickMouse();
                }

                while (this.gameSettings.keyBindPickBlock.isPressed())
                {
                    this.middleClickMouse();
                }
            }

            if (this.gameSettings.keyBindUseItem.getIsKeyPressed() && this.rightClickDelayTimer == 0 && !this.thePlayer.isUsingItem())
            {
                this.rightClickMouse();
            }

            this.sendClickBlockToController(this.currentScreen == null && this.gameSettings.keyBindAttack.getIsKeyPressed() && this.inGameHasFocus);
        }

        if (this.theWorld != null)
        {
            if (this.thePlayer != null)
            {
                ++this.joinPlayerCounter;

                if (this.joinPlayerCounter == 30)
                {
                    this.joinPlayerCounter = 0;
                    this.theWorld.joinEntityInSurroundings(this.thePlayer);
                }
            }

            this.mcProfiler.endStartSection("gameRenderer");

            if (!this.isGamePaused)
            {
                this.entityRenderer.updateRenderer();
            }

            this.mcProfiler.endStartSection("levelRenderer");

            if (!this.isGamePaused)
            {
                this.renderGlobal.updateClouds();
            }

            this.mcProfiler.endStartSection("level");

            if (!this.isGamePaused)
            {
                if (this.theWorld.func_175658_ac() > 0)
                {
                    this.theWorld.setLastLightningBolt(this.theWorld.func_175658_ac() - 1);
                }

                this.theWorld.updateEntities();
            }
        }

        if (!this.isGamePaused)
        {
            this.mcMusicTicker.update();
            this.mcSoundHandler.update();
        }

        if (this.theWorld != null)
        {
            if (!this.isGamePaused)
            {
                this.theWorld.setAllowedSpawnTypes(this.theWorld.getDifficulty() != EnumDifficulty.PEACEFUL, true);

                try
                {
                    this.theWorld.tick();
                }
                catch (Throwable var8)
                {
                    var2 = CrashReport.makeCrashReport(var8, "Exception in world tick");

                    if (this.theWorld == null)
                    {
                        var3 = var2.makeCategory("Affected level");
                        var3.addCrashSection("Problem", "Level is null!");
                    }
                    else
                    {
                        this.theWorld.addWorldInfoToCrashReport(var2);
                    }

                    throw new ReportedException(var2);
                }
            }

            this.mcProfiler.endStartSection("animateTick");

            if (!this.isGamePaused && this.theWorld != null)
            {
                this.theWorld.doVoidFogParticles(MathHelper.floor_double(this.thePlayer.posX), MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ));
            }

            this.mcProfiler.endStartSection("particles");

            if (!this.isGamePaused)
            {
                this.effectRenderer.updateEffects();
            }
        }
        else if (this.myNetworkManager != null)
        {
            this.mcProfiler.endStartSection("pendingConnection");
            this.myNetworkManager.processReceivedPackets();
        }

        this.mcProfiler.endSection();
        this.systemTime = getSystemTime();
    }

    /**
     * Arguments: World foldername,  World ingame name, WorldSettings
     */
    public void launchIntegratedServer(String folderName, String worldName, WorldSettings worldSettingsIn)
    {
        this.loadWorld((WorldClient)null);
        System.gc();
        ISaveHandler var4 = this.saveLoader.getSaveLoader(folderName, false);
        WorldInfo var5 = var4.loadWorldInfo();

        if (var5 == null && worldSettingsIn != null)
        {
            var5 = new WorldInfo(worldSettingsIn, folderName);
            var4.saveWorldInfo(var5);
        }

        if (worldSettingsIn == null)
        {
            worldSettingsIn = new WorldSettings(var5);
        }

        try
        {
            this.theIntegratedServer = new IntegratedServer(this, folderName, worldName, worldSettingsIn);
            this.theIntegratedServer.startServerThread();
            this.integratedServerIsRunning = true;
        }
        catch (Throwable var10)
        {
            CrashReport var7 = CrashReport.makeCrashReport(var10, "Starting integrated server");
            CrashReportCategory var8 = var7.makeCategory("Starting integrated server");
            var8.addCrashSection("Level ID", folderName);
            var8.addCrashSection("Level Name", worldName);
            throw new ReportedException(var7);
        }

        this.loadingScreen.displaySavingString(I18n.format("menu.loadingLevel", new Object[0]));

        while (!this.theIntegratedServer.serverIsInRunLoop())
        {
            String var6 = this.theIntegratedServer.getUserMessage();

            if (var6 != null)
            {
                this.loadingScreen.displayLoadingString(I18n.format(var6, new Object[0]));
            }
            else
            {
                this.loadingScreen.displayLoadingString("");
            }

            try
            {
                Thread.sleep(200L);
            }
            catch (InterruptedException var9)
            {
                ;
            }
        }

        this.displayGuiScreen((GuiScreen)null);
        SocketAddress var11 = this.theIntegratedServer.getNetworkSystem().addLocalEndpoint();
        NetworkManager var12 = NetworkManager.provideLocalClient(var11);
        var12.setNetHandler(new NetHandlerLoginClient(var12, this, (GuiScreen)null));
        var12.sendPacket(new C00Handshake(47, var11.toString(), 0, EnumConnectionState.LOGIN));
        var12.sendPacket(new C00PacketLoginStart(this.getSession().getProfile()));
        this.myNetworkManager = var12;
    }

    /**
     * unloads the current world first
     */
    public void loadWorld(WorldClient worldClientIn)
    {
        this.loadWorld(worldClientIn, "");
    }
    public void addLines(String line, BufferedReader in) throws IOException{
    	while ((line = in.readLine()) != null) {
        	InventoryPlayer.checklist.add(line);
        }
    }
    /**
     * par2Str is displayed on the loading screen to the user unloads the current world first
     */
    public void loadWorld(WorldClient worldClientIn, String loadingMessage)
    {
        if (worldClientIn == null)
        {
            NetHandlerPlayClient var3 = this.getNetHandler();

            if (var3 != null)
            {
                var3.cleanup();
            }

            if (this.theIntegratedServer != null && this.theIntegratedServer.func_175578_N())
            {
                this.theIntegratedServer.initiateShutdown();
                this.theIntegratedServer.func_175592_a();
            }

            this.theIntegratedServer = null;
            this.guiAchievement.clearAchievements();
            this.entityRenderer.getMapItemRenderer().func_148249_a();
        }

        this.renderViewEntity = null;
        this.myNetworkManager = null;

        if (this.loadingScreen != null)
        {
            this.loadingScreen.resetProgressAndMessage(loadingMessage);
            this.loadingScreen.displayLoadingString("");
        }

        if (worldClientIn == null && this.theWorld != null)
        {
            if (this.mcResourcePackRepository.getResourcePackInstance() != null)
            {
                this.mcResourcePackRepository.func_148529_f();
                this.func_175603_A();
            }

            this.setServerData((ServerData)null);
            this.integratedServerIsRunning = false;
        }

        this.mcSoundHandler.stopSounds();
        this.theWorld = worldClientIn;

        if (worldClientIn != null)
        {
            if (this.renderGlobal != null)
            {
                this.renderGlobal.setWorldAndLoadRenderers(worldClientIn);
            }

            if (this.effectRenderer != null)
            {
                this.effectRenderer.clearEffects(worldClientIn);
            }

            if (this.thePlayer == null)
            {
                this.thePlayer = this.playerController.func_178892_a(worldClientIn, new StatFileWriter());
                
                Jello.getInGameGUI().onWorldLoad();
                
                this.playerController.flipPlayer(this.thePlayer);
            }

            this.thePlayer.preparePlayerToSpawn();
            worldClientIn.spawnEntityInWorld(this.thePlayer);
            this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
            this.playerController.setPlayerCapabilities(this.thePlayer);
            this.renderViewEntity = this.thePlayer;
        }
        else
        {
            this.saveLoader.flushCache();
            this.thePlayer = null;
        }

        System.gc();
        this.systemTime = 0L;
    }

    public void setDimensionAndSpawnPlayer(int dimension)
    {
        this.theWorld.setInitialSpawnLocation();
        this.theWorld.removeAllEntities();
        int var2 = 0;
        String var3 = null;

        if (this.thePlayer != null)
        {
            var2 = this.thePlayer.getEntityId();
            this.theWorld.removeEntity(this.thePlayer);
            var3 = this.thePlayer.getClientBrand();
        }

        this.renderViewEntity = null;
        EntityPlayerSP var4 = this.thePlayer;
        this.thePlayer = this.playerController.func_178892_a(this.theWorld, this.thePlayer == null ? new StatFileWriter() : this.thePlayer.getStatFileWriter());
        this.thePlayer.getDataWatcher().updateWatchedObjectsFromList(var4.getDataWatcher().getAllWatched());
        this.thePlayer.dimension = dimension;
        this.renderViewEntity = this.thePlayer;
        this.thePlayer.preparePlayerToSpawn();
        this.thePlayer.func_175158_f(var3);
        this.theWorld.spawnEntityInWorld(this.thePlayer);
        this.playerController.flipPlayer(this.thePlayer);
        this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
        this.thePlayer.setEntityId(var2);
        this.playerController.setPlayerCapabilities(this.thePlayer);
        this.thePlayer.func_175150_k(var4.func_175140_cp());

        if (this.currentScreen instanceof GuiGameOver)
        {
            this.displayGuiScreen((GuiScreen)null);
        }
    }

    /**
     * Gets whether this is a demo or not.
     */
    public final boolean isDemo()
    {
        return this.isDemo;
    }

    public NetHandlerPlayClient getNetHandler()
    {
        return this.thePlayer != null ? this.thePlayer.sendQueue : null;
    }

    public static boolean isGuiEnabled()
    {
        return theMinecraft == null || !theMinecraft.gameSettings.hideGUI;
    }

    public static boolean isFancyGraphicsEnabled()
    {
        return theMinecraft != null && theMinecraft.gameSettings.fancyGraphics;
    }

    /**
     * Returns if ambient occlusion is enabled
     */
    public static boolean isAmbientOcclusionEnabled()
    {
        return theMinecraft != null && theMinecraft.gameSettings.ambientOcclusion != 0;
    }

    /**
     * Called when user clicked he's mouse middle button (pick block)
     */
    private void middleClickMouse()
    {
        if (this.objectMouseOver != null)
        {
            boolean var1 = this.thePlayer.capabilities.isCreativeMode;
            int var3 = 0;
            boolean var4 = false;
            TileEntity var5 = null;
            Object var2;

            if (this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                BlockPos var6 = this.objectMouseOver.func_178782_a();
                Block var7 = this.theWorld.getBlockState(var6).getBlock();

                if (var7.getMaterial() == Material.air)
                {
                    return;
                }

                var2 = var7.getItem(this.theWorld, var6);

                if (var2 == null)
                {
                    return;
                }

                if (var1 && GuiScreen.isCtrlKeyDown())
                {
                    var5 = this.theWorld.getTileEntity(var6);
                }

                Block var8 = var2 instanceof ItemBlock && !var7.isFlowerPot() ? Block.getBlockFromItem((Item)var2) : var7;
                var3 = var8.getDamageValue(this.theWorld, var6);
                var4 = ((Item)var2).getHasSubtypes();
            }
            else
            {
                if (this.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY || this.objectMouseOver.entityHit == null || !var1)
                {
                    return;
                }

                if (this.objectMouseOver.entityHit instanceof EntityPainting)
                {
                    var2 = Items.painting;
                }
                else if (this.objectMouseOver.entityHit instanceof EntityLeashKnot)
                {
                    var2 = Items.lead;
                }
                else if (this.objectMouseOver.entityHit instanceof EntityItemFrame)
                {
                    EntityItemFrame var11 = (EntityItemFrame)this.objectMouseOver.entityHit;
                    ItemStack var14 = var11.getDisplayedItem();

                    if (var14 == null)
                    {
                        var2 = Items.item_frame;
                    }
                    else
                    {
                        var2 = var14.getItem();
                        var3 = var14.getMetadata();
                        var4 = true;
                    }
                }
                else if (this.objectMouseOver.entityHit instanceof EntityMinecart)
                {
                    EntityMinecart var12 = (EntityMinecart)this.objectMouseOver.entityHit;

                    switch (Minecraft.SwitchEnumMinecartType.field_178901_b[var12.func_180456_s().ordinal()])
                    {
                        case 1:
                            var2 = Items.furnace_minecart;
                            break;

                        case 2:
                            var2 = Items.chest_minecart;
                            break;

                        case 3:
                            var2 = Items.tnt_minecart;
                            break;

                        case 4:
                            var2 = Items.hopper_minecart;
                            break;

                        case 5:
                            var2 = Items.command_block_minecart;
                            break;

                        default:
                            var2 = Items.minecart;
                    }
                }
                else if (this.objectMouseOver.entityHit instanceof EntityBoat)
                {
                    var2 = Items.boat;
                }
                else if (this.objectMouseOver.entityHit instanceof EntityArmorStand)
                {
                    var2 = Items.armor_stand;
                }
                else
                {
                    var2 = Items.spawn_egg;
                    var3 = EntityList.getEntityID(this.objectMouseOver.entityHit);
                    var4 = true;

                    if (!EntityList.entityEggs.containsKey(Integer.valueOf(var3)))
                    {
                        return;
                    }
                }
            }

            InventoryPlayer var13 = this.thePlayer.inventory;

            if (var5 == null)
            {
                var13.setCurrentItem((Item)var2, var3, var4, var1);
            }
            else
            {
                NBTTagCompound var15 = new NBTTagCompound();
                var5.writeToNBT(var15);
                ItemStack var17 = new ItemStack((Item)var2, 1, var3);
                var17.setTagInfo("BlockEntityTag", var15);
                NBTTagCompound var9 = new NBTTagCompound();
                NBTTagList var10 = new NBTTagList();
                var10.appendTag(new NBTTagString("(+NBT)"));
                var9.setTag("Lore", var10);
                var17.setTagInfo("display", var9);
                var13.setInventorySlotContents(var13.currentItem, var17);
            }

            if (var1)
            {
                int var16 = this.thePlayer.inventoryContainer.inventorySlots.size() - 9 + var13.currentItem;
                this.playerController.sendSlotPacket(var13.getStackInSlot(var13.currentItem), var16);
            }
        }
    }

    /**
     * adds core server Info (GL version , Texture pack, isModded, type), and the worldInfo to the crash report
     */
    public CrashReport addGraphicsAndWorldToCrashReport(CrashReport theCrash)
    {
        theCrash.getCategory().addCrashSectionCallable("Launched Version", new Callable()
        {
            
            public String call()
            {
                return Minecraft.this.launchedVersion;
            }
            public Object call1()
            {
                return this.call();
            }
        });
        theCrash.getCategory().addCrashSectionCallable("LWJGL", new Callable()
        {
            
            public String call()
            {
                return Sys.getVersion();
            }
            public Object call1()
            {
                return this.call();
            }
        });
        theCrash.getCategory().addCrashSectionCallable("OpenGL", new Callable()
        {
            
            public String call()
            {
                return GL11.glGetString(GL11.GL_RENDERER) + " GL version " + GL11.glGetString(GL11.GL_VERSION) + ", " + GL11.glGetString(GL11.GL_VENDOR);
            }
            public Object call1()
            {
                return this.call();
            }
        });
        theCrash.getCategory().addCrashSectionCallable("GL Caps", new Callable()
        {
            
            public String call()
            {
                return OpenGlHelper.func_153172_c();
            }
            public Object call1()
            {
                return this.call();
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Using VBOs", new Callable()
        {
            
            public String call()
            {
                return Minecraft.this.gameSettings.field_178881_t ? "Yes" : "No";
            }
            public Object call1()
            {
                return this.call();
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Is Modded", new Callable()
        {
            
            public String call()
            {
                String var1 = ClientBrandRetriever.getClientModName();
                return !var1.equals("vanilla") ? "Definitely; Client brand changed to \'" + var1 + "\'" : (Minecraft.class.getSigners() == null ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and client brand is untouched.");
            }
            public Object call1()
            {
                return this.call();
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Type", new Callable()
        {
            
            public String call()
            {
                return "Client (map_client.txt)";
            }
            public Object call1()
            {
                return this.call();
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Resource Packs", new Callable()
        {
            
            public String call()
            {
                return Minecraft.this.gameSettings.resourcePacks.toString();
            }
            public Object call1()
            {
                return this.call();
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Current Language", new Callable()
        {
            
            public String call()
            {
                return Minecraft.this.mcLanguageManager.getCurrentLanguage().toString();
            }
            public Object call1()
            {
                return this.call();
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Profiler Position", new Callable()
        {
            
            public String call1()
            {
                return Minecraft.this.mcProfiler.profilingEnabled ? Minecraft.this.mcProfiler.getNameOfLastSection() : "N/A (disabled)";
            }
            public Object call()
            {
                return this.call1();
            }
        });

        if (this.theWorld != null)
        {
            this.theWorld.addWorldInfoToCrashReport(theCrash);
        }

        return theCrash;
    }

    /**
     * Return the singleton Minecraft instance for the game
     */
    public static Minecraft getMinecraft()
    {
        return theMinecraft;
    }

    public ListenableFuture func_175603_A()
    {
        return this.addScheduledTask(new Runnable()
        {
            
            public void run()
            {
                Minecraft.this.refreshResources();
            }
        });
    }

    public void addServerStatsToSnooper(PlayerUsageSnooper playerSnooper)
    {
        playerSnooper.addClientStat("fps", Integer.valueOf(debugFPS));
        playerSnooper.addClientStat("vsync_enabled", Boolean.valueOf(this.gameSettings.enableVsync));
        playerSnooper.addClientStat("display_frequency", Integer.valueOf(Display.getDisplayMode().getFrequency()));
        playerSnooper.addClientStat("display_type", this.fullscreen ? "fullscreen" : "windowed");
        playerSnooper.addClientStat("run_time", Long.valueOf((MinecraftServer.getCurrentTimeMillis() - playerSnooper.getMinecraftStartTimeMillis()) / 60L * 1000L));
        String var2 = ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN ? "little" : "big";
        playerSnooper.addClientStat("endianness", var2);
        playerSnooper.addClientStat("resource_packs", Integer.valueOf(this.mcResourcePackRepository.getRepositoryEntries().size()));
        int var3 = 0;
        Iterator var4 = this.mcResourcePackRepository.getRepositoryEntries().iterator();

        while (var4.hasNext())
        {
            ResourcePackRepository.Entry var5 = (ResourcePackRepository.Entry)var4.next();
            playerSnooper.addClientStat("resource_pack[" + var3++ + "]", var5.getResourcePackName());
        }

        if (this.theIntegratedServer != null && this.theIntegratedServer.getPlayerUsageSnooper() != null)
        {
            playerSnooper.addClientStat("snooper_partner", this.theIntegratedServer.getPlayerUsageSnooper().getUniqueID());
        }
    }

    public void addServerTypeToSnooper(PlayerUsageSnooper playerSnooper)
    {
        playerSnooper.addStatToSnooper("opengl_version", GL11.glGetString(GL11.GL_VERSION));
        playerSnooper.addStatToSnooper("opengl_vendor", GL11.glGetString(GL11.GL_VENDOR));
        playerSnooper.addStatToSnooper("client_brand", ClientBrandRetriever.getClientModName());
        playerSnooper.addStatToSnooper("launched_version", this.launchedVersion);
        ContextCapabilities var2 = GLContext.getCapabilities();
        playerSnooper.addStatToSnooper("gl_caps[ARB_arrays_of_arrays]", Boolean.valueOf(var2.GL_ARB_arrays_of_arrays));
        playerSnooper.addStatToSnooper("gl_caps[ARB_base_instance]", Boolean.valueOf(var2.GL_ARB_base_instance));
        playerSnooper.addStatToSnooper("gl_caps[ARB_blend_func_extended]", Boolean.valueOf(var2.GL_ARB_blend_func_extended));
        playerSnooper.addStatToSnooper("gl_caps[ARB_clear_buffer_object]", Boolean.valueOf(var2.GL_ARB_clear_buffer_object));
        playerSnooper.addStatToSnooper("gl_caps[ARB_color_buffer_float]", Boolean.valueOf(var2.GL_ARB_color_buffer_float));
        playerSnooper.addStatToSnooper("gl_caps[ARB_compatibility]", Boolean.valueOf(var2.GL_ARB_compatibility));
        playerSnooper.addStatToSnooper("gl_caps[ARB_compressed_texture_pixel_storage]", Boolean.valueOf(var2.GL_ARB_compressed_texture_pixel_storage));
        playerSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", Boolean.valueOf(var2.GL_ARB_compute_shader));
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", Boolean.valueOf(var2.GL_ARB_copy_buffer));
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", Boolean.valueOf(var2.GL_ARB_copy_image));
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", Boolean.valueOf(var2.GL_ARB_depth_buffer_float));
        playerSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", Boolean.valueOf(var2.GL_ARB_compute_shader));
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", Boolean.valueOf(var2.GL_ARB_copy_buffer));
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", Boolean.valueOf(var2.GL_ARB_copy_image));
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", Boolean.valueOf(var2.GL_ARB_depth_buffer_float));
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_clamp]", Boolean.valueOf(var2.GL_ARB_depth_clamp));
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_texture]", Boolean.valueOf(var2.GL_ARB_depth_texture));
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers]", Boolean.valueOf(var2.GL_ARB_draw_buffers));
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers_blend]", Boolean.valueOf(var2.GL_ARB_draw_buffers_blend));
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_elements_base_vertex]", Boolean.valueOf(var2.GL_ARB_draw_elements_base_vertex));
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_indirect]", Boolean.valueOf(var2.GL_ARB_draw_indirect));
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_instanced]", Boolean.valueOf(var2.GL_ARB_draw_instanced));
        playerSnooper.addStatToSnooper("gl_caps[ARB_explicit_attrib_location]", Boolean.valueOf(var2.GL_ARB_explicit_attrib_location));
        playerSnooper.addStatToSnooper("gl_caps[ARB_explicit_uniform_location]", Boolean.valueOf(var2.GL_ARB_explicit_uniform_location));
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_layer_viewport]", Boolean.valueOf(var2.GL_ARB_fragment_layer_viewport));
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_program]", Boolean.valueOf(var2.GL_ARB_fragment_program));
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_shader]", Boolean.valueOf(var2.GL_ARB_fragment_shader));
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_program_shadow]", Boolean.valueOf(var2.GL_ARB_fragment_program_shadow));
        playerSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_object]", Boolean.valueOf(var2.GL_ARB_framebuffer_object));
        playerSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_sRGB]", Boolean.valueOf(var2.GL_ARB_framebuffer_sRGB));
        playerSnooper.addStatToSnooper("gl_caps[ARB_geometry_shader4]", Boolean.valueOf(var2.GL_ARB_geometry_shader4));
        playerSnooper.addStatToSnooper("gl_caps[ARB_gpu_shader5]", Boolean.valueOf(var2.GL_ARB_gpu_shader5));
        playerSnooper.addStatToSnooper("gl_caps[ARB_half_float_pixel]", Boolean.valueOf(var2.GL_ARB_half_float_pixel));
        playerSnooper.addStatToSnooper("gl_caps[ARB_half_float_vertex]", Boolean.valueOf(var2.GL_ARB_half_float_vertex));
        playerSnooper.addStatToSnooper("gl_caps[ARB_instanced_arrays]", Boolean.valueOf(var2.GL_ARB_instanced_arrays));
        playerSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_alignment]", Boolean.valueOf(var2.GL_ARB_map_buffer_alignment));
        playerSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_range]", Boolean.valueOf(var2.GL_ARB_map_buffer_range));
        playerSnooper.addStatToSnooper("gl_caps[ARB_multisample]", Boolean.valueOf(var2.GL_ARB_multisample));
        playerSnooper.addStatToSnooper("gl_caps[ARB_multitexture]", Boolean.valueOf(var2.GL_ARB_multitexture));
        playerSnooper.addStatToSnooper("gl_caps[ARB_occlusion_query2]", Boolean.valueOf(var2.GL_ARB_occlusion_query2));
        playerSnooper.addStatToSnooper("gl_caps[ARB_pixel_buffer_object]", Boolean.valueOf(var2.GL_ARB_pixel_buffer_object));
        playerSnooper.addStatToSnooper("gl_caps[ARB_seamless_cube_map]", Boolean.valueOf(var2.GL_ARB_seamless_cube_map));
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_objects]", Boolean.valueOf(var2.GL_ARB_shader_objects));
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_stencil_export]", Boolean.valueOf(var2.GL_ARB_shader_stencil_export));
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_texture_lod]", Boolean.valueOf(var2.GL_ARB_shader_texture_lod));
        playerSnooper.addStatToSnooper("gl_caps[ARB_shadow]", Boolean.valueOf(var2.GL_ARB_shadow));
        playerSnooper.addStatToSnooper("gl_caps[ARB_shadow_ambient]", Boolean.valueOf(var2.GL_ARB_shadow_ambient));
        playerSnooper.addStatToSnooper("gl_caps[ARB_stencil_texturing]", Boolean.valueOf(var2.GL_ARB_stencil_texturing));
        playerSnooper.addStatToSnooper("gl_caps[ARB_sync]", Boolean.valueOf(var2.GL_ARB_sync));
        playerSnooper.addStatToSnooper("gl_caps[ARB_tessellation_shader]", Boolean.valueOf(var2.GL_ARB_tessellation_shader));
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_border_clamp]", Boolean.valueOf(var2.GL_ARB_texture_border_clamp));
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_buffer_object]", Boolean.valueOf(var2.GL_ARB_texture_buffer_object));
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map]", Boolean.valueOf(var2.GL_ARB_texture_cube_map));
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map_array]", Boolean.valueOf(var2.GL_ARB_texture_cube_map_array));
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_non_power_of_two]", Boolean.valueOf(var2.GL_ARB_texture_non_power_of_two));
        playerSnooper.addStatToSnooper("gl_caps[ARB_uniform_buffer_object]", Boolean.valueOf(var2.GL_ARB_uniform_buffer_object));
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_blend]", Boolean.valueOf(var2.GL_ARB_vertex_blend));
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_buffer_object]", Boolean.valueOf(var2.GL_ARB_vertex_buffer_object));
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_program]", Boolean.valueOf(var2.GL_ARB_vertex_program));
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_shader]", Boolean.valueOf(var2.GL_ARB_vertex_shader));
        playerSnooper.addStatToSnooper("gl_caps[EXT_bindable_uniform]", Boolean.valueOf(var2.GL_EXT_bindable_uniform));
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_equation_separate]", Boolean.valueOf(var2.GL_EXT_blend_equation_separate));
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_func_separate]", Boolean.valueOf(var2.GL_EXT_blend_func_separate));
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_minmax]", Boolean.valueOf(var2.GL_EXT_blend_minmax));
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_subtract]", Boolean.valueOf(var2.GL_EXT_blend_subtract));
        playerSnooper.addStatToSnooper("gl_caps[EXT_draw_instanced]", Boolean.valueOf(var2.GL_EXT_draw_instanced));
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_multisample]", Boolean.valueOf(var2.GL_EXT_framebuffer_multisample));
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_object]", Boolean.valueOf(var2.GL_EXT_framebuffer_object));
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_sRGB]", Boolean.valueOf(var2.GL_EXT_framebuffer_sRGB));
        playerSnooper.addStatToSnooper("gl_caps[EXT_geometry_shader4]", Boolean.valueOf(var2.GL_EXT_geometry_shader4));
        playerSnooper.addStatToSnooper("gl_caps[EXT_gpu_program_parameters]", Boolean.valueOf(var2.GL_EXT_gpu_program_parameters));
        playerSnooper.addStatToSnooper("gl_caps[EXT_gpu_shader4]", Boolean.valueOf(var2.GL_EXT_gpu_shader4));
        playerSnooper.addStatToSnooper("gl_caps[EXT_multi_draw_arrays]", Boolean.valueOf(var2.GL_EXT_multi_draw_arrays));
        playerSnooper.addStatToSnooper("gl_caps[EXT_packed_depth_stencil]", Boolean.valueOf(var2.GL_EXT_packed_depth_stencil));
        playerSnooper.addStatToSnooper("gl_caps[EXT_paletted_texture]", Boolean.valueOf(var2.GL_EXT_paletted_texture));
        playerSnooper.addStatToSnooper("gl_caps[EXT_rescale_normal]", Boolean.valueOf(var2.GL_EXT_rescale_normal));
        playerSnooper.addStatToSnooper("gl_caps[EXT_separate_shader_objects]", Boolean.valueOf(var2.GL_EXT_separate_shader_objects));
        playerSnooper.addStatToSnooper("gl_caps[EXT_shader_image_load_store]", Boolean.valueOf(var2.GL_EXT_shader_image_load_store));
        playerSnooper.addStatToSnooper("gl_caps[EXT_shadow_funcs]", Boolean.valueOf(var2.GL_EXT_shadow_funcs));
        playerSnooper.addStatToSnooper("gl_caps[EXT_shared_texture_palette]", Boolean.valueOf(var2.GL_EXT_shared_texture_palette));
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_clear_tag]", Boolean.valueOf(var2.GL_EXT_stencil_clear_tag));
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_two_side]", Boolean.valueOf(var2.GL_EXT_stencil_two_side));
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_wrap]", Boolean.valueOf(var2.GL_EXT_stencil_wrap));
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_3d]", Boolean.valueOf(var2.GL_EXT_texture_3d));
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_array]", Boolean.valueOf(var2.GL_EXT_texture_array));
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_buffer_object]", Boolean.valueOf(var2.GL_EXT_texture_buffer_object));
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_integer]", Boolean.valueOf(var2.GL_EXT_texture_integer));
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_lod_bias]", Boolean.valueOf(var2.GL_EXT_texture_lod_bias));
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_sRGB]", Boolean.valueOf(var2.GL_EXT_texture_sRGB));
        playerSnooper.addStatToSnooper("gl_caps[EXT_vertex_shader]", Boolean.valueOf(var2.GL_EXT_vertex_shader));
        playerSnooper.addStatToSnooper("gl_caps[EXT_vertex_weighting]", Boolean.valueOf(var2.GL_EXT_vertex_weighting));
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_uniforms]", Integer.valueOf(GL11.glGetInteger(GL20.GL_MAX_VERTEX_UNIFORM_COMPONENTS)));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_fragment_uniforms]", Integer.valueOf(GL11.glGetInteger(GL20.GL_MAX_FRAGMENT_UNIFORM_COMPONENTS)));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_attribs]", Integer.valueOf(GL11.glGetInteger(GL20.GL_MAX_VERTEX_ATTRIBS)));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_texture_image_units]", Integer.valueOf(GL11.glGetInteger(GL20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS)));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_texture_image_units]", Integer.valueOf(GL11.glGetInteger(GL20.GL_MAX_TEXTURE_IMAGE_UNITS)));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_texture_image_units]", Integer.valueOf(GL11.glGetInteger(35071)));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_max_texture_size", Integer.valueOf(getGLMaximumTextureSize()));
    }

    /**
     * Used in the usage snooper.
     */
    public static int getGLMaximumTextureSize()
    {
        for (int var0 = 16384; var0 > 0; var0 >>= 1)
        {
            GL11.glTexImage2D(GL11.GL_PROXY_TEXTURE_2D, 0, GL11.GL_RGBA, var0, var0, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)null);
            int var1 = GL11.glGetTexLevelParameteri(GL11.GL_PROXY_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);

            if (var1 != 0)
            {
                return var0;
            }
        }

        return -1;
    }

    /**
     * Returns whether snooping is enabled or not.
     */
    public boolean isSnooperEnabled()
    {
        return this.gameSettings.snooperEnabled;
    }

    /**
     * Set the current ServerData instance.
     */
    public void setServerData(ServerData serverDataIn)
    {
        this.currentServerData = serverDataIn;
    }

    public ServerData getCurrentServerData()
    {
        return this.currentServerData;
    }

    public boolean isIntegratedServerRunning()
    {
        return this.integratedServerIsRunning;
    }

    /**
     * Returns true if there is only one player playing, and the current server is the integrated one.
     */
    public boolean isSingleplayer()
    {
        return this.integratedServerIsRunning && this.theIntegratedServer != null;
    }

    /**
     * Returns the currently running integrated server
     */
    public IntegratedServer getIntegratedServer()
    {
        return this.theIntegratedServer;
    }

    public static void stopIntegratedServer()
    {
        if (theMinecraft != null)
        {
            IntegratedServer var0 = theMinecraft.getIntegratedServer();

            if (var0 != null)
            {
                var0.stopServer();
            }
        }
    }

    /**
     * Returns the PlayerUsageSnooper instance.
     */
    public PlayerUsageSnooper getPlayerUsageSnooper()
    {
        return this.usageSnooper;
    }

    /**
     * Gets the system time in milliseconds.
     */
    public static long getSystemTime()
    {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    /**
     * Returns whether we're in full screen or not.
     */
    public boolean isFullScreen()
    {
        return this.fullscreen;
    }

    public Session getSession()
    {
        return this.session;
    }

    public PropertyMap func_180509_L()
    {
        return this.twitchDetails;
    }

    public Proxy getProxy()
    {
        return this.proxy;
    }

    public TextureManager getTextureManager()
    {
        return this.renderEngine;
    }

    public IResourceManager getResourceManager()
    {
        return this.mcResourceManager;
    }

    public ResourcePackRepository getResourcePackRepository()
    {
        return this.mcResourcePackRepository;
    }

    public LanguageManager getLanguageManager()
    {
        return this.mcLanguageManager;
    }

    public TextureMap getTextureMapBlocks()
    {
        return this.textureMapBlocks;
    }

    public boolean isJava64bit()
    {
        return this.jvm64bit;
    }

    public boolean isGamePaused()
    {
        return this.isGamePaused;
    }

    public SoundHandler getSoundHandler()
    {
        return this.mcSoundHandler;
    }

    public MusicTicker.MusicType getAmbientMusicType()
    {
        return this.currentScreen instanceof GuiWinGame ? MusicTicker.MusicType.CREDITS : (this.thePlayer != null ? (this.thePlayer.worldObj.provider instanceof WorldProviderHell ? MusicTicker.MusicType.NETHER : (this.thePlayer.worldObj.provider instanceof WorldProviderEnd ? (BossStatus.bossName != null && BossStatus.statusBarTime > 0 ? MusicTicker.MusicType.END_BOSS : MusicTicker.MusicType.END) : (this.thePlayer.capabilities.isCreativeMode && this.thePlayer.capabilities.allowFlying ? MusicTicker.MusicType.CREATIVE : MusicTicker.MusicType.GAME))) : MusicTicker.MusicType.MENU);
    }

    public IStream getTwitchStream()
    {
        return this.stream;
    }

    public void dispatchKeypresses()
    {
        int var1 = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() : Keyboard.getEventKey();

        if (var1 != 0 && !Keyboard.isRepeatEvent())
        {
            if (!(this.currentScreen instanceof GuiControls) || ((GuiControls)this.currentScreen).time <= getSystemTime() - 20L)
            {
                if (Keyboard.getEventKeyState())
                {
                    if (var1 == this.gameSettings.keyBindStreamStartStop.getKeyCode())
                    {
                        if (this.getTwitchStream().func_152934_n())
                        {
                            this.getTwitchStream().func_152914_u();
                        }
                        else if (this.getTwitchStream().func_152924_m())
                        {
                            this.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback()
                            {
                                
                                public void confirmClicked(boolean result, int id)
                                {
                                    if (result)
                                    {
                                        Minecraft.this.getTwitchStream().func_152930_t();
                                    }

                                    Minecraft.this.displayGuiScreen((GuiScreen)null);
                                }
                            }, I18n.format("stream.confirm_start", new Object[0]), "", 0));
                        }
                        else if (this.getTwitchStream().func_152928_D() && this.getTwitchStream().func_152936_l())
                        {
                            if (this.theWorld != null)
                            {
                                this.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("Not ready to start streaming yet!"));
                            }
                        }
                        else
                        {
                            GuiStreamUnavailable.func_152321_a(this.currentScreen);
                        }
                    }
                    else if (var1 == this.gameSettings.keyBindStreamPauseUnpause.getKeyCode())
                    {
                        if (this.getTwitchStream().func_152934_n())
                        {
                            if (this.getTwitchStream().isPaused())
                            {
                                this.getTwitchStream().func_152933_r();
                            }
                            else
                            {
                                this.getTwitchStream().func_152916_q();
                            }
                        }
                    }
                    else if (var1 == this.gameSettings.keyBindStreamCommercials.getKeyCode())
                    {
                        if (this.getTwitchStream().func_152934_n())
                        {
                            this.getTwitchStream().func_152931_p();
                        }
                    }
                    else if (var1 == this.gameSettings.keyBindStreamToggleMic.getKeyCode())
                    {
                        this.stream.func_152910_a(true);
                    }
                    else if (var1 == this.gameSettings.keyBindFullscreen.getKeyCode())
                    {
                        this.toggleFullscreen();
                    }
                    else if (var1 == this.gameSettings.keyBindScreenshot.getKeyCode())
                    {
                        this.ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot(this.mcDataDir, this.displayWidth, this.displayHeight, this.framebufferMc));
                    }
                }
                else if (var1 == this.gameSettings.keyBindStreamToggleMic.getKeyCode())
                {
                    this.stream.func_152910_a(false);
                }
            }
        }
    }

    public MinecraftSessionService getSessionService()
    {
        return this.sessionService;
    }

    public SkinManager getSkinManager()
    {
        return this.skinManager;
    }

    public Entity getRenderViewEntity()
    {
        return this.renderViewEntity;
    }

    public void setRenderViewEntity(Entity p_175607_1_)
    {
        this.renderViewEntity = p_175607_1_;
        this.entityRenderer.loadEntityShader(p_175607_1_);
    }

    public ListenableFuture addScheduledTask(Callable callableToSchedule)
    {
        Validate.notNull(callableToSchedule);

        if (!this.isCallingFromMinecraftThread())
        {
            ListenableFutureTask var2 = ListenableFutureTask.create(callableToSchedule);
            Queue var3 = this.scheduledTasks;

            synchronized (this.scheduledTasks)
            {
                this.scheduledTasks.add(var2);
                return var2;
            }
        }
        else
        {
            try
            {
                return Futures.immediateFuture(callableToSchedule.call());
            }
            catch (Exception var6)
            {
                return Futures.immediateFailedCheckedFuture(var6);
            }
        }
    }

    public ListenableFuture addScheduledTask(Runnable runnableToSchedule)
    {
        Validate.notNull(runnableToSchedule);
        return this.addScheduledTask(Executors.callable(runnableToSchedule));
    }

    public boolean isCallingFromMinecraftThread()
    {
        return Thread.currentThread() == this.mcThread;
    }

    public BlockRendererDispatcher getBlockRendererDispatcher()
    {
        return this.field_175618_aM;
    }

    public RenderManager getRenderManager()
    {
        return this.renderManager;
    }

    public RenderItem getRenderItem()
    {
        return this.renderItem;
    }

    public ItemRenderer getItemRenderer()
    {
        return this.itemRenderer;
    }

    public static int func_175610_ah()
    {
        return debugFPS;
    }

    public static int getDebugFps()
    {
        return debugFPS;
    }
    
    public static Map func_175596_ai()
    {
        HashMap var0 = Maps.newHashMap();
        var0.put("X-Minecraft-Username", getMinecraft().getSession().getUsername());
        var0.put("X-Minecraft-UUID", getMinecraft().getSession().getPlayerID());
        var0.put("X-Minecraft-Version", "1.8");
        return var0;
    }

    public static int getIt() {
		return it;
	}

	public static void setIt(int it) {
		Minecraft.it = it;
	}

	static final class SwitchEnumMinecartType
    {
        static final int[] field_152390_a;

        static final int[] field_178901_b = new int[EntityMinecart.EnumMinecartType.values().length];
        

        static
        {
            try
            {
                field_178901_b[EntityMinecart.EnumMinecartType.FURNACE.ordinal()] = 1;
            }
            catch (NoSuchFieldError var8)
            {
                ;
            }

            try
            {
                field_178901_b[EntityMinecart.EnumMinecartType.CHEST.ordinal()] = 2;
            }
            catch (NoSuchFieldError var7)
            {
                ;
            }

            try
            {
                field_178901_b[EntityMinecart.EnumMinecartType.TNT.ordinal()] = 3;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                field_178901_b[EntityMinecart.EnumMinecartType.HOPPER.ordinal()] = 4;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                field_178901_b[EntityMinecart.EnumMinecartType.COMMAND_BLOCK.ordinal()] = 5;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            field_152390_a = new int[MovingObjectPosition.MovingObjectType.values().length];

            try
            {
                field_152390_a[MovingObjectPosition.MovingObjectType.ENTITY.ordinal()] = 1;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                field_152390_a[MovingObjectPosition.MovingObjectType.BLOCK.ordinal()] = 2;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_152390_a[MovingObjectPosition.MovingObjectType.MISS.ordinal()] = 3;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
