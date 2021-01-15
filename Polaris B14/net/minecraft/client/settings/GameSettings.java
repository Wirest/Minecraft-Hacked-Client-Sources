/*      */ package net.minecraft.client.settings;
/*      */ 
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.gson.Gson;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.FileReader;
/*      */ import java.io.FileWriter;
/*      */ import java.io.PrintWriter;
/*      */ import java.lang.reflect.ParameterizedType;
/*      */ import java.lang.reflect.Type;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.audio.SoundCategory;
/*      */ import net.minecraft.client.audio.SoundHandler;
/*      */ import net.minecraft.client.entity.EntityPlayerSP;
/*      */ import net.minecraft.client.gui.FontRenderer;
/*      */ import net.minecraft.client.gui.GuiIngame;
/*      */ import net.minecraft.client.gui.GuiNewChat;
/*      */ import net.minecraft.client.renderer.RenderGlobal;
/*      */ import net.minecraft.client.renderer.texture.TextureMap;
/*      */ import net.minecraft.client.resources.I18n;
/*      */ import net.minecraft.client.resources.LanguageManager;
/*      */ import net.minecraft.client.stream.IStream;
/*      */ import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
/*      */ import net.minecraft.entity.player.EnumPlayerModelParts;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.world.EnumDifficulty;
/*      */ import optfine.ClearWater;
/*      */ import optfine.Config;
/*      */ import optfine.CustomColorizer;
/*      */ import optfine.CustomSky;
/*      */ import optfine.Reflector;
/*      */ import optfine.ReflectorClass;
/*      */ import org.apache.commons.lang3.ArrayUtils;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ import org.lwjgl.opengl.Display;
/*      */ 
/*      */ public class GameSettings
/*      */ {
/*   47 */   private static final Logger logger = ;
/*   48 */   private static final Gson gson = new Gson();
/*   49 */   private static final ParameterizedType typeListString = new ParameterizedType()
/*      */   {
/*      */     private static final String __OBFID = "CL_00000651";
/*      */     
/*      */     public Type[] getActualTypeArguments() {
/*   54 */       return new Type[] { String.class };
/*      */     }
/*      */     
/*      */     public Type getRawType() {
/*   58 */       return List.class;
/*      */     }
/*      */     
/*      */     public Type getOwnerType() {
/*   62 */       return null;
/*      */     }
/*      */   };
/*      */   
/*      */ 
/*   67 */   private static final String[] GUISCALES = { "options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large" };
/*   68 */   private static final String[] PARTICLES = { "options.particles.all", "options.particles.decreased", "options.particles.minimal" };
/*   69 */   private static final String[] AMBIENT_OCCLUSIONS = { "options.ao.off", "options.ao.min", "options.ao.max" };
/*   70 */   private static final String[] STREAM_COMPRESSIONS = { "options.stream.compression.low", "options.stream.compression.medium", "options.stream.compression.high" };
/*   71 */   private static final String[] STREAM_CHAT_MODES = { "options.stream.chat.enabled.streaming", "options.stream.chat.enabled.always", "options.stream.chat.enabled.never" };
/*   72 */   private static final String[] STREAM_CHAT_FILTER_MODES = { "options.stream.chat.userFilter.all", "options.stream.chat.userFilter.subs", "options.stream.chat.userFilter.mods" };
/*   73 */   private static final String[] STREAM_MIC_MODES = { "options.stream.mic_toggle.mute", "options.stream.mic_toggle.talk" };
/*   74 */   private static final String[] field_181149_aW = { "options.off", "options.graphics.fast", "options.graphics.fancy" };
/*   75 */   public float mouseSensitivity = 0.5F;
/*      */   public boolean invertMouse;
/*   77 */   public int renderDistanceChunks = -1;
/*   78 */   public boolean viewBobbing = true;
/*      */   public boolean anaglyph;
/*   80 */   public boolean fboEnable = true;
/*   81 */   public int limitFramerate = 120;
/*      */   
/*      */ 
/*   84 */   public int clouds = 2;
/*   85 */   public boolean fancyGraphics = true;
/*      */   
/*      */ 
/*   88 */   public int ambientOcclusion = 2;
/*   89 */   public List resourcePacks = Lists.newArrayList();
/*   90 */   public List field_183018_l = Lists.newArrayList();
/*   91 */   public EntityPlayer.EnumChatVisibility chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
/*   92 */   public boolean chatColours = true;
/*   93 */   public boolean chatLinks = true;
/*   94 */   public boolean chatLinksPrompt = true;
/*   95 */   public float chatOpacity = 1.0F;
/*   96 */   public boolean snooperEnabled = true;
/*      */   public boolean fullScreen;
/*   98 */   public boolean enableVsync = true;
/*   99 */   public boolean useVbo = false;
/*  100 */   public boolean allowBlockAlternatives = true;
/*  101 */   public boolean reducedDebugInfo = false;
/*      */   
/*      */ 
/*      */   public boolean hideServerAddress;
/*      */   
/*      */ 
/*      */   public boolean advancedItemTooltips;
/*      */   
/*      */ 
/*  110 */   public boolean pauseOnLostFocus = true;
/*  111 */   private final Set setModelParts = com.google.common.collect.Sets.newHashSet(EnumPlayerModelParts.values());
/*      */   public boolean touchscreen;
/*      */   public int overrideWidth;
/*      */   public int overrideHeight;
/*  115 */   public boolean heldItemTooltips = true;
/*  116 */   public float chatScale = 1.0F;
/*  117 */   public float chatWidth = 1.0F;
/*  118 */   public float chatHeightUnfocused = 0.44366196F;
/*  119 */   public float chatHeightFocused = 1.0F;
/*  120 */   public boolean showInventoryAchievementHint = true;
/*  121 */   public int mipmapLevels = 4;
/*  122 */   private Map mapSoundLevels = Maps.newEnumMap(SoundCategory.class);
/*  123 */   public float streamBytesPerPixel = 0.5F;
/*  124 */   public float streamMicVolume = 1.0F;
/*  125 */   public float streamGameVolume = 1.0F;
/*  126 */   public float streamKbps = 0.5412844F;
/*  127 */   public float streamFps = 0.31690142F;
/*  128 */   public int streamCompression = 1;
/*  129 */   public boolean streamSendMetadata = true;
/*  130 */   public String streamPreferredServer = "";
/*  131 */   public int streamChatEnabled = 0;
/*  132 */   public int streamChatUserFilter = 0;
/*  133 */   public int streamMicToggleBehavior = 0;
/*  134 */   public boolean field_181150_U = true;
/*  135 */   public boolean field_181151_V = true;
/*  136 */   public KeyBinding keyBindForward = new KeyBinding("key.forward", 17, "key.categories.movement");
/*  137 */   public KeyBinding keyBindLeft = new KeyBinding("key.left", 30, "key.categories.movement");
/*  138 */   public KeyBinding keyBindBack = new KeyBinding("key.back", 31, "key.categories.movement");
/*  139 */   public KeyBinding keyBindRight = new KeyBinding("key.right", 32, "key.categories.movement");
/*  140 */   public KeyBinding keyBindJump = new KeyBinding("key.jump", 57, "key.categories.movement");
/*  141 */   public KeyBinding keyBindSneak = new KeyBinding("key.sneak", 42, "key.categories.movement");
/*  142 */   public KeyBinding keyBindSprint = new KeyBinding("key.sprint", 29, "key.categories.movement");
/*  143 */   public KeyBinding keyBindInventory = new KeyBinding("key.inventory", 18, "key.categories.inventory");
/*  144 */   public KeyBinding keyBindUseItem = new KeyBinding("key.use", -99, "key.categories.gameplay");
/*  145 */   public KeyBinding keyBindDrop = new KeyBinding("key.drop", 16, "key.categories.gameplay");
/*  146 */   public KeyBinding keyBindAttack = new KeyBinding("key.attack", -100, "key.categories.gameplay");
/*  147 */   public KeyBinding keyBindPickBlock = new KeyBinding("key.pickItem", -98, "key.categories.gameplay");
/*  148 */   public KeyBinding keyBindChat = new KeyBinding("key.chat", 20, "key.categories.multiplayer");
/*  149 */   public KeyBinding keyBindPlayerList = new KeyBinding("key.playerlist", 15, "key.categories.multiplayer");
/*  150 */   public KeyBinding keyBindCommand = new KeyBinding("key.command", 53, "key.categories.multiplayer");
/*  151 */   public KeyBinding keyBindScreenshot = new KeyBinding("key.screenshot", 60, "key.categories.misc");
/*  152 */   public KeyBinding keyBindTogglePerspective = new KeyBinding("key.togglePerspective", 63, "key.categories.misc");
/*  153 */   public KeyBinding keyBindSmoothCamera = new KeyBinding("key.smoothCamera", 0, "key.categories.misc");
/*  154 */   public KeyBinding keyBindFullscreen = new KeyBinding("key.fullscreen", 87, "key.categories.misc");
/*  155 */   public KeyBinding keyBindSpectatorOutlines = new KeyBinding("key.spectatorOutlines", 0, "key.categories.misc");
/*  156 */   public KeyBinding keyBindStreamStartStop = new KeyBinding("key.streamStartStop", 64, "key.categories.stream");
/*  157 */   public KeyBinding keyBindStreamPauseUnpause = new KeyBinding("key.streamPauseUnpause", 65, "key.categories.stream");
/*  158 */   public KeyBinding keyBindStreamCommercials = new KeyBinding("key.streamCommercial", 0, "key.categories.stream");
/*  159 */   public KeyBinding keyBindStreamToggleMic = new KeyBinding("key.streamToggleMic", 0, "key.categories.stream");
/*  160 */   public KeyBinding[] keyBindsHotbar = { new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"), new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"), new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"), new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"), new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"), new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"), new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"), new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"), new KeyBinding("key.hotbar.9", 10, "key.categories.inventory") };
/*      */   
/*      */   public KeyBinding[] keyBindings;
/*      */   
/*      */   protected Minecraft mc;
/*      */   
/*      */   private File optionsFile;
/*      */   
/*      */   public EnumDifficulty difficulty;
/*      */   
/*      */   public boolean hideGUI;
/*      */   
/*      */   public int thirdPersonView;
/*      */   
/*      */   public boolean showDebugInfo;
/*      */   
/*      */   public boolean showDebugProfilerChart;
/*      */   
/*      */   public boolean field_181657_aC;
/*      */   
/*      */   public String lastServer;
/*      */   
/*      */   public boolean smoothCamera;
/*      */   
/*      */   public boolean debugCamEnable;
/*      */   public float fovSetting;
/*      */   public float gammaSetting;
/*      */   public float saturation;
/*      */   public int guiScale;
/*      */   public int particleSetting;
/*      */   public String language;
/*      */   public boolean forceUnicodeFont;
/*      */   private static final String __OBFID = "CL_00000650";
/*  193 */   public int ofFogType = 1;
/*  194 */   public float ofFogStart = 0.8F;
/*  195 */   public int ofMipmapType = 0;
/*  196 */   public boolean ofOcclusionFancy = false;
/*  197 */   public boolean ofSmoothFps = false;
/*  198 */   public boolean ofSmoothWorld = Config.isSingleProcessor();
/*  199 */   public boolean ofLazyChunkLoading = Config.isSingleProcessor();
/*  200 */   public float ofAoLevel = 1.0F;
/*  201 */   public int ofAaLevel = 0;
/*  202 */   public int ofAfLevel = 1;
/*  203 */   public int ofClouds = 0;
/*  204 */   public float ofCloudsHeight = 0.0F;
/*  205 */   public int ofTrees = 0;
/*  206 */   public int ofRain = 0;
/*  207 */   public int ofDroppedItems = 0;
/*  208 */   public int ofBetterGrass = 3;
/*  209 */   public int ofAutoSaveTicks = 4000;
/*  210 */   public boolean ofLagometer = false;
/*  211 */   public boolean ofProfiler = false;
/*  212 */   public boolean ofShowFps = false;
/*  213 */   public boolean ofWeather = true;
/*  214 */   public boolean ofSky = true;
/*  215 */   public boolean ofStars = true;
/*  216 */   public boolean ofSunMoon = true;
/*  217 */   public int ofVignette = 0;
/*  218 */   public int ofChunkUpdates = 1;
/*  219 */   public int ofChunkLoading = 0;
/*  220 */   public boolean ofChunkUpdatesDynamic = false;
/*  221 */   public int ofTime = 0;
/*  222 */   public boolean ofClearWater = false;
/*  223 */   public boolean ofBetterSnow = false;
/*  224 */   public String ofFullscreenMode = "Default";
/*  225 */   public boolean ofSwampColors = true;
/*  226 */   public boolean ofRandomMobs = true;
/*  227 */   public boolean ofSmoothBiomes = true;
/*  228 */   public boolean ofCustomFonts = true;
/*  229 */   public boolean ofCustomColors = true;
/*  230 */   public boolean ofCustomSky = true;
/*  231 */   public boolean ofShowCapes = true;
/*  232 */   public int ofConnectedTextures = 2;
/*  233 */   public boolean ofNaturalTextures = false;
/*  234 */   public boolean ofFastMath = false;
/*  235 */   public boolean ofFastRender = true;
/*  236 */   public int ofTranslucentBlocks = 0;
/*  237 */   public int ofAnimatedWater = 0;
/*  238 */   public int ofAnimatedLava = 0;
/*  239 */   public boolean ofAnimatedFire = true;
/*  240 */   public boolean ofAnimatedPortal = true;
/*  241 */   public boolean ofAnimatedRedstone = true;
/*  242 */   public boolean ofAnimatedExplosion = true;
/*  243 */   public boolean ofAnimatedFlame = true;
/*  244 */   public boolean ofAnimatedSmoke = true;
/*  245 */   public boolean ofVoidParticles = true;
/*  246 */   public boolean ofWaterParticles = true;
/*  247 */   public boolean ofRainSplash = true;
/*  248 */   public boolean ofPortalParticles = true;
/*  249 */   public boolean ofPotionParticles = true;
/*  250 */   public boolean ofFireworkParticles = true;
/*  251 */   public boolean ofDrippingWaterLava = true;
/*  252 */   public boolean ofAnimatedTerrain = true;
/*  253 */   public boolean ofAnimatedTextures = true;
/*      */   public static final int DEFAULT = 0;
/*      */   public static final int FAST = 1;
/*      */   public static final int FANCY = 2;
/*      */   public static final int OFF = 3;
/*      */   public static final int ANIM_ON = 0;
/*      */   public static final int ANIM_GENERATED = 1;
/*      */   public static final int ANIM_OFF = 2;
/*      */   public static final int CL_DEFAULT = 0;
/*      */   public static final int CL_SMOOTH = 1;
/*      */   public static final int CL_THREADED = 2;
/*      */   public static final String DEFAULT_STR = "Default";
/*      */   public KeyBinding ofKeyBindZoom;
/*      */   private File optionsFileOF;
/*      */   
/*      */   public GameSettings(Minecraft mcIn, File p_i46326_2_)
/*      */   {
/*  270 */     this.keyBindings = ((KeyBinding[])ArrayUtils.addAll(new KeyBinding[] { this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.keyBindSpectatorOutlines }, this.keyBindsHotbar));
/*  271 */     this.difficulty = EnumDifficulty.NORMAL;
/*  272 */     this.lastServer = "";
/*  273 */     this.fovSetting = 70.0F;
/*  274 */     this.language = "en_US";
/*  275 */     this.forceUnicodeFont = false;
/*  276 */     this.mc = mcIn;
/*  277 */     this.optionsFile = new File(p_i46326_2_, "options.txt");
/*  278 */     this.optionsFileOF = new File(p_i46326_2_, "optionsof.txt");
/*  279 */     this.limitFramerate = ((int)Options.FRAMERATE_LIMIT.getValueMax());
/*  280 */     this.ofKeyBindZoom = new KeyBinding("Zoom", 46, "key.categories.misc");
/*  281 */     this.keyBindings = ((KeyBinding[])ArrayUtils.add(this.keyBindings, this.ofKeyBindZoom));
/*  282 */     Options.RENDER_DISTANCE.setValueMax(32.0F);
/*  283 */     this.renderDistanceChunks = 8;
/*  284 */     loadOptions();
/*  285 */     Config.initGameSettings(this);
/*      */   }
/*      */   
/*      */   public GameSettings()
/*      */   {
/*  290 */     this.keyBindings = ((KeyBinding[])ArrayUtils.addAll(new KeyBinding[] { this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.keyBindSpectatorOutlines }, this.keyBindsHotbar));
/*  291 */     this.difficulty = EnumDifficulty.NORMAL;
/*  292 */     this.lastServer = "";
/*  293 */     this.fovSetting = 70.0F;
/*  294 */     this.language = "en_US";
/*  295 */     this.forceUnicodeFont = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getKeyDisplayString(int p_74298_0_)
/*      */   {
/*  303 */     return p_74298_0_ < 256 ? org.lwjgl.input.Keyboard.getKeyName(p_74298_0_) : p_74298_0_ < 0 ? I18n.format("key.mouseButton", new Object[] { Integer.valueOf(p_74298_0_ + 101) }) : String.format("%c", new Object[] { Character.valueOf((char)(p_74298_0_ - 256)) }).toUpperCase();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isKeyDown(KeyBinding p_100015_0_)
/*      */   {
/*  311 */     int i = p_100015_0_.getKeyCode();
/*  312 */     return p_100015_0_.getKeyCode() != 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setOptionKeyBinding(KeyBinding p_151440_1_, int p_151440_2_)
/*      */   {
/*  320 */     p_151440_1_.setKeyCode(p_151440_2_);
/*  321 */     saveOptions();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setOptionFloatValue(Options p_74304_1_, float p_74304_2_)
/*      */   {
/*  329 */     setOptionFloatValueOF(p_74304_1_, p_74304_2_);
/*      */     
/*  331 */     if (p_74304_1_ == Options.SENSITIVITY)
/*      */     {
/*  333 */       this.mouseSensitivity = p_74304_2_;
/*      */     }
/*      */     
/*  336 */     if (p_74304_1_ == Options.FOV)
/*      */     {
/*  338 */       this.fovSetting = p_74304_2_;
/*      */     }
/*      */     
/*  341 */     if (p_74304_1_ == Options.GAMMA)
/*      */     {
/*  343 */       this.gammaSetting = p_74304_2_;
/*      */     }
/*      */     
/*  346 */     if (p_74304_1_ == Options.FRAMERATE_LIMIT)
/*      */     {
/*  348 */       this.limitFramerate = ((int)p_74304_2_);
/*  349 */       this.enableVsync = false;
/*      */       
/*  351 */       if (this.limitFramerate <= 0)
/*      */       {
/*  353 */         this.limitFramerate = ((int)Options.FRAMERATE_LIMIT.getValueMax());
/*  354 */         this.enableVsync = true;
/*      */       }
/*      */       
/*  357 */       updateVSync();
/*      */     }
/*      */     
/*  360 */     if (p_74304_1_ == Options.CHAT_OPACITY)
/*      */     {
/*  362 */       this.chatOpacity = p_74304_2_;
/*  363 */       this.mc.ingameGUI.getChatGUI().refreshChat();
/*      */     }
/*      */     
/*  366 */     if (p_74304_1_ == Options.CHAT_HEIGHT_FOCUSED)
/*      */     {
/*  368 */       this.chatHeightFocused = p_74304_2_;
/*  369 */       this.mc.ingameGUI.getChatGUI().refreshChat();
/*      */     }
/*      */     
/*  372 */     if (p_74304_1_ == Options.CHAT_HEIGHT_UNFOCUSED)
/*      */     {
/*  374 */       this.chatHeightUnfocused = p_74304_2_;
/*  375 */       this.mc.ingameGUI.getChatGUI().refreshChat();
/*      */     }
/*      */     
/*  378 */     if (p_74304_1_ == Options.CHAT_WIDTH)
/*      */     {
/*  380 */       this.chatWidth = p_74304_2_;
/*  381 */       this.mc.ingameGUI.getChatGUI().refreshChat();
/*      */     }
/*      */     
/*  384 */     if (p_74304_1_ == Options.CHAT_SCALE)
/*      */     {
/*  386 */       this.chatScale = p_74304_2_;
/*  387 */       this.mc.ingameGUI.getChatGUI().refreshChat();
/*      */     }
/*      */     
/*  390 */     if (p_74304_1_ == Options.MIPMAP_LEVELS)
/*      */     {
/*  392 */       int i = this.mipmapLevels;
/*  393 */       this.mipmapLevels = ((int)p_74304_2_);
/*      */       
/*  395 */       if (i != p_74304_2_)
/*      */       {
/*  397 */         this.mc.getTextureMapBlocks().setMipmapLevels(this.mipmapLevels);
/*  398 */         this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
/*  399 */         this.mc.getTextureMapBlocks().setBlurMipmapDirect(false, this.mipmapLevels > 0);
/*  400 */         this.mc.scheduleResourcesRefresh();
/*      */       }
/*      */     }
/*      */     
/*  404 */     if (p_74304_1_ == Options.BLOCK_ALTERNATIVES)
/*      */     {
/*  406 */       this.allowBlockAlternatives = (!this.allowBlockAlternatives);
/*  407 */       this.mc.renderGlobal.loadRenderers();
/*      */     }
/*      */     
/*  410 */     if (p_74304_1_ == Options.RENDER_DISTANCE)
/*      */     {
/*  412 */       this.renderDistanceChunks = ((int)p_74304_2_);
/*  413 */       this.mc.renderGlobal.setDisplayListEntitiesDirty();
/*      */     }
/*      */     
/*  416 */     if (p_74304_1_ == Options.STREAM_BYTES_PER_PIXEL)
/*      */     {
/*  418 */       this.streamBytesPerPixel = p_74304_2_;
/*      */     }
/*      */     
/*  421 */     if (p_74304_1_ == Options.STREAM_VOLUME_MIC)
/*      */     {
/*  423 */       this.streamMicVolume = p_74304_2_;
/*  424 */       this.mc.getTwitchStream().updateStreamVolume();
/*      */     }
/*      */     
/*  427 */     if (p_74304_1_ == Options.STREAM_VOLUME_SYSTEM)
/*      */     {
/*  429 */       this.streamGameVolume = p_74304_2_;
/*  430 */       this.mc.getTwitchStream().updateStreamVolume();
/*      */     }
/*      */     
/*  433 */     if (p_74304_1_ == Options.STREAM_KBPS)
/*      */     {
/*  435 */       this.streamKbps = p_74304_2_;
/*      */     }
/*      */     
/*  438 */     if (p_74304_1_ == Options.STREAM_FPS)
/*      */     {
/*  440 */       this.streamFps = p_74304_2_;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setOptionValue(Options p_74306_1_, int p_74306_2_)
/*      */   {
/*  449 */     setOptionValueOF(p_74306_1_, p_74306_2_);
/*      */     
/*  451 */     if (p_74306_1_ == Options.INVERT_MOUSE)
/*      */     {
/*  453 */       this.invertMouse = (!this.invertMouse);
/*      */     }
/*      */     
/*  456 */     if (p_74306_1_ == Options.GUI_SCALE)
/*      */     {
/*  458 */       this.guiScale = (this.guiScale + p_74306_2_ & 0x3);
/*      */     }
/*      */     
/*  461 */     if (p_74306_1_ == Options.PARTICLES)
/*      */     {
/*  463 */       this.particleSetting = ((this.particleSetting + p_74306_2_) % 3);
/*      */     }
/*      */     
/*  466 */     if (p_74306_1_ == Options.VIEW_BOBBING)
/*      */     {
/*  468 */       this.viewBobbing = (!this.viewBobbing);
/*      */     }
/*      */     
/*  471 */     if (p_74306_1_ == Options.RENDER_CLOUDS)
/*      */     {
/*  473 */       this.clouds = ((this.clouds + p_74306_2_) % 3);
/*      */     }
/*      */     
/*  476 */     if (p_74306_1_ == Options.FORCE_UNICODE_FONT)
/*      */     {
/*  478 */       this.forceUnicodeFont = (!this.forceUnicodeFont);
/*  479 */       this.mc.fontRendererObj.setUnicodeFlag((this.mc.getLanguageManager().isCurrentLocaleUnicode()) || (this.forceUnicodeFont));
/*      */     }
/*      */     
/*  482 */     if (p_74306_1_ == Options.FBO_ENABLE)
/*      */     {
/*  484 */       this.fboEnable = (!this.fboEnable);
/*      */     }
/*      */     
/*  487 */     if (p_74306_1_ == Options.ANAGLYPH)
/*      */     {
/*  489 */       this.anaglyph = (!this.anaglyph);
/*  490 */       this.mc.refreshResources();
/*      */     }
/*      */     
/*  493 */     if (p_74306_1_ == Options.GRAPHICS)
/*      */     {
/*  495 */       this.fancyGraphics = (!this.fancyGraphics);
/*  496 */       updateRenderClouds();
/*  497 */       this.mc.renderGlobal.loadRenderers();
/*      */     }
/*      */     
/*  500 */     if (p_74306_1_ == Options.AMBIENT_OCCLUSION)
/*      */     {
/*  502 */       this.ambientOcclusion = ((this.ambientOcclusion + p_74306_2_) % 3);
/*  503 */       this.mc.renderGlobal.loadRenderers();
/*      */     }
/*      */     
/*  506 */     if (p_74306_1_ == Options.CHAT_VISIBILITY)
/*      */     {
/*  508 */       this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility((this.chatVisibility.getChatVisibility() + p_74306_2_) % 3);
/*      */     }
/*      */     
/*  511 */     if (p_74306_1_ == Options.STREAM_COMPRESSION)
/*      */     {
/*  513 */       this.streamCompression = ((this.streamCompression + p_74306_2_) % 3);
/*      */     }
/*      */     
/*  516 */     if (p_74306_1_ == Options.STREAM_SEND_METADATA)
/*      */     {
/*  518 */       this.streamSendMetadata = (!this.streamSendMetadata);
/*      */     }
/*      */     
/*  521 */     if (p_74306_1_ == Options.STREAM_CHAT_ENABLED)
/*      */     {
/*  523 */       this.streamChatEnabled = ((this.streamChatEnabled + p_74306_2_) % 3);
/*      */     }
/*      */     
/*  526 */     if (p_74306_1_ == Options.STREAM_CHAT_USER_FILTER)
/*      */     {
/*  528 */       this.streamChatUserFilter = ((this.streamChatUserFilter + p_74306_2_) % 3);
/*      */     }
/*      */     
/*  531 */     if (p_74306_1_ == Options.STREAM_MIC_TOGGLE_BEHAVIOR)
/*      */     {
/*  533 */       this.streamMicToggleBehavior = ((this.streamMicToggleBehavior + p_74306_2_) % 2);
/*      */     }
/*      */     
/*  536 */     if (p_74306_1_ == Options.CHAT_COLOR)
/*      */     {
/*  538 */       this.chatColours = (!this.chatColours);
/*      */     }
/*      */     
/*  541 */     if (p_74306_1_ == Options.CHAT_LINKS)
/*      */     {
/*  543 */       this.chatLinks = (!this.chatLinks);
/*      */     }
/*      */     
/*  546 */     if (p_74306_1_ == Options.CHAT_LINKS_PROMPT)
/*      */     {
/*  548 */       this.chatLinksPrompt = (!this.chatLinksPrompt);
/*      */     }
/*      */     
/*  551 */     if (p_74306_1_ == Options.SNOOPER_ENABLED)
/*      */     {
/*  553 */       this.snooperEnabled = (!this.snooperEnabled);
/*      */     }
/*      */     
/*  556 */     if (p_74306_1_ == Options.TOUCHSCREEN)
/*      */     {
/*  558 */       this.touchscreen = (!this.touchscreen);
/*      */     }
/*      */     
/*  561 */     if (p_74306_1_ == Options.USE_FULLSCREEN)
/*      */     {
/*  563 */       this.fullScreen = (!this.fullScreen);
/*      */       
/*  565 */       if (this.mc.isFullScreen() != this.fullScreen)
/*      */       {
/*  567 */         this.mc.toggleFullscreen();
/*      */       }
/*      */     }
/*      */     
/*  571 */     if (p_74306_1_ == Options.ENABLE_VSYNC)
/*      */     {
/*  573 */       this.enableVsync = (!this.enableVsync);
/*  574 */       Display.setVSyncEnabled(this.enableVsync);
/*      */     }
/*      */     
/*  577 */     if (p_74306_1_ == Options.USE_VBO)
/*      */     {
/*  579 */       this.useVbo = (!this.useVbo);
/*  580 */       this.mc.renderGlobal.loadRenderers();
/*      */     }
/*      */     
/*  583 */     if (p_74306_1_ == Options.BLOCK_ALTERNATIVES)
/*      */     {
/*  585 */       this.allowBlockAlternatives = (!this.allowBlockAlternatives);
/*  586 */       this.mc.renderGlobal.loadRenderers();
/*      */     }
/*      */     
/*  589 */     if (p_74306_1_ == Options.REDUCED_DEBUG_INFO)
/*      */     {
/*  591 */       this.reducedDebugInfo = (!this.reducedDebugInfo);
/*      */     }
/*      */     
/*  594 */     if (p_74306_1_ == Options.ENTITY_SHADOWS)
/*      */     {
/*  596 */       this.field_181151_V = (!this.field_181151_V);
/*      */     }
/*      */     
/*  599 */     saveOptions();
/*      */   }
/*      */   
/*      */   public float getOptionFloatValue(Options p_74296_1_)
/*      */   {
/*  604 */     return p_74296_1_ == Options.STREAM_FPS ? this.streamFps : p_74296_1_ == Options.STREAM_KBPS ? this.streamKbps : p_74296_1_ == Options.STREAM_VOLUME_SYSTEM ? this.streamGameVolume : p_74296_1_ == Options.STREAM_VOLUME_MIC ? this.streamMicVolume : p_74296_1_ == Options.STREAM_BYTES_PER_PIXEL ? this.streamBytesPerPixel : p_74296_1_ == Options.RENDER_DISTANCE ? this.renderDistanceChunks : p_74296_1_ == Options.MIPMAP_LEVELS ? this.mipmapLevels : p_74296_1_ == Options.FRAMERATE_LIMIT ? this.limitFramerate : p_74296_1_ == Options.CHAT_WIDTH ? this.chatWidth : p_74296_1_ == Options.CHAT_SCALE ? this.chatScale : p_74296_1_ == Options.CHAT_HEIGHT_UNFOCUSED ? this.chatHeightUnfocused : p_74296_1_ == Options.CHAT_HEIGHT_FOCUSED ? this.chatHeightFocused : p_74296_1_ == Options.CHAT_OPACITY ? this.chatOpacity : p_74296_1_ == Options.SENSITIVITY ? this.mouseSensitivity : p_74296_1_ == Options.SATURATION ? this.saturation : p_74296_1_ == Options.GAMMA ? this.gammaSetting : p_74296_1_ == Options.FOV ? this.fovSetting : p_74296_1_ == Options.FRAMERATE_LIMIT ? this.limitFramerate : (this.limitFramerate == Options.FRAMERATE_LIMIT.getValueMax()) && (this.enableVsync) ? 0.0F : p_74296_1_ == Options.MIPMAP_TYPE ? this.ofMipmapType : p_74296_1_ == Options.AF_LEVEL ? this.ofAfLevel : p_74296_1_ == Options.AA_LEVEL ? this.ofAaLevel : p_74296_1_ == Options.AO_LEVEL ? this.ofAoLevel : p_74296_1_ == Options.CLOUD_HEIGHT ? this.ofCloudsHeight : 0.0F;
/*      */   }
/*      */   
/*      */   public boolean getOptionOrdinalValue(Options p_74308_1_)
/*      */   {
/*  609 */     switch (GameSettings.2.field_151477_a[p_74308_1_.ordinal()])
/*      */     {
/*      */     case 1: 
/*  612 */       return this.invertMouse;
/*      */     
/*      */     case 2: 
/*  615 */       return this.viewBobbing;
/*      */     
/*      */     case 3: 
/*  618 */       return this.anaglyph;
/*      */     
/*      */     case 4: 
/*  621 */       return this.fboEnable;
/*      */     
/*      */     case 5: 
/*  624 */       return this.chatColours;
/*      */     
/*      */     case 6: 
/*  627 */       return this.chatLinks;
/*      */     
/*      */     case 7: 
/*  630 */       return this.chatLinksPrompt;
/*      */     
/*      */     case 8: 
/*  633 */       return this.snooperEnabled;
/*      */     
/*      */     case 9: 
/*  636 */       return this.fullScreen;
/*      */     
/*      */     case 10: 
/*  639 */       return this.enableVsync;
/*      */     
/*      */     case 11: 
/*  642 */       return this.useVbo;
/*      */     
/*      */     case 12: 
/*  645 */       return this.touchscreen;
/*      */     
/*      */     case 13: 
/*  648 */       return this.streamSendMetadata;
/*      */     
/*      */     case 14: 
/*  651 */       return this.forceUnicodeFont;
/*      */     
/*      */     case 15: 
/*  654 */       return this.allowBlockAlternatives;
/*      */     
/*      */     case 16: 
/*  657 */       return this.reducedDebugInfo;
/*      */     
/*      */     case 17: 
/*  660 */       return this.field_181151_V;
/*      */     }
/*      */     
/*  663 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static String getTranslation(String[] p_74299_0_, int p_74299_1_)
/*      */   {
/*  673 */     if ((p_74299_1_ < 0) || (p_74299_1_ >= p_74299_0_.length))
/*      */     {
/*  675 */       p_74299_1_ = 0;
/*      */     }
/*      */     
/*  678 */     return I18n.format(p_74299_0_[p_74299_1_], new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getKeyBinding(Options p_74297_1_)
/*      */   {
/*  686 */     String s = getKeyBindingOF(p_74297_1_);
/*      */     
/*  688 */     if (s != null)
/*      */     {
/*  690 */       return s;
/*      */     }
/*      */     
/*      */ 
/*  694 */     String s1 = I18n.format(p_74297_1_.getEnumString(), new Object[0]) + ": ";
/*      */     
/*  696 */     if (p_74297_1_.getEnumFloat())
/*      */     {
/*  698 */       float f1 = getOptionFloatValue(p_74297_1_);
/*  699 */       float f = p_74297_1_.normalizeValue(f1);
/*  700 */       return s1 + (int)(f * 100.0F) + "%";
/*      */     }
/*  702 */     if (p_74297_1_.getEnumBoolean())
/*      */     {
/*  704 */       boolean flag = getOptionOrdinalValue(p_74297_1_);
/*  705 */       return s1 + I18n.format("options.off", new Object[0]);
/*      */     }
/*  707 */     if (p_74297_1_ == Options.GUI_SCALE)
/*      */     {
/*  709 */       return s1 + getTranslation(GUISCALES, this.guiScale);
/*      */     }
/*  711 */     if (p_74297_1_ == Options.CHAT_VISIBILITY)
/*      */     {
/*  713 */       return s1 + I18n.format(this.chatVisibility.getResourceKey(), new Object[0]);
/*      */     }
/*  715 */     if (p_74297_1_ == Options.PARTICLES)
/*      */     {
/*  717 */       return s1 + getTranslation(PARTICLES, this.particleSetting);
/*      */     }
/*  719 */     if (p_74297_1_ == Options.AMBIENT_OCCLUSION)
/*      */     {
/*  721 */       return s1 + getTranslation(AMBIENT_OCCLUSIONS, this.ambientOcclusion);
/*      */     }
/*  723 */     if (p_74297_1_ == Options.STREAM_COMPRESSION)
/*      */     {
/*  725 */       return s1 + getTranslation(STREAM_COMPRESSIONS, this.streamCompression);
/*      */     }
/*  727 */     if (p_74297_1_ == Options.STREAM_CHAT_ENABLED)
/*      */     {
/*  729 */       return s1 + getTranslation(STREAM_CHAT_MODES, this.streamChatEnabled);
/*      */     }
/*  731 */     if (p_74297_1_ == Options.STREAM_CHAT_USER_FILTER)
/*      */     {
/*  733 */       return s1 + getTranslation(STREAM_CHAT_FILTER_MODES, this.streamChatUserFilter);
/*      */     }
/*  735 */     if (p_74297_1_ == Options.STREAM_MIC_TOGGLE_BEHAVIOR)
/*      */     {
/*  737 */       return s1 + getTranslation(STREAM_MIC_MODES, this.streamMicToggleBehavior);
/*      */     }
/*  739 */     if (p_74297_1_ == Options.RENDER_CLOUDS)
/*      */     {
/*  741 */       return s1 + getTranslation(field_181149_aW, this.clouds);
/*      */     }
/*  743 */     if (p_74297_1_ == Options.GRAPHICS)
/*      */     {
/*  745 */       if (this.fancyGraphics)
/*      */       {
/*  747 */         return s1 + I18n.format("options.graphics.fancy", new Object[0]);
/*      */       }
/*      */       
/*      */ 
/*  751 */       String s2 = "options.graphics.fast";
/*  752 */       return s1 + I18n.format("options.graphics.fast", new Object[0]);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  757 */     return s1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void loadOptions()
/*      */   {
/*      */     try
/*      */     {
/*  769 */       if (!this.optionsFile.exists())
/*      */       {
/*  771 */         return;
/*      */       }
/*      */       
/*  774 */       BufferedReader bufferedreader = new BufferedReader(new FileReader(this.optionsFile));
/*  775 */       String s = "";
/*  776 */       this.mapSoundLevels.clear();
/*      */       
/*  778 */       while ((s = bufferedreader.readLine()) != null)
/*      */       {
/*      */         try
/*      */         {
/*  782 */           String[] astring = s.split(":");
/*      */           
/*  784 */           if (astring[0].equals("mouseSensitivity"))
/*      */           {
/*  786 */             this.mouseSensitivity = parseFloat(astring[1]);
/*      */           }
/*      */           
/*  789 */           if (astring[0].equals("fov"))
/*      */           {
/*  791 */             this.fovSetting = (parseFloat(astring[1]) * 40.0F + 70.0F);
/*      */           }
/*      */           
/*  794 */           if (astring[0].equals("gamma"))
/*      */           {
/*  796 */             this.gammaSetting = parseFloat(astring[1]);
/*      */           }
/*      */           
/*  799 */           if (astring[0].equals("saturation"))
/*      */           {
/*  801 */             this.saturation = parseFloat(astring[1]);
/*      */           }
/*      */           
/*  804 */           if (astring[0].equals("invertYMouse"))
/*      */           {
/*  806 */             this.invertMouse = astring[1].equals("true");
/*      */           }
/*      */           
/*  809 */           if (astring[0].equals("renderDistance"))
/*      */           {
/*  811 */             this.renderDistanceChunks = Integer.parseInt(astring[1]);
/*      */           }
/*      */           
/*  814 */           if (astring[0].equals("guiScale"))
/*      */           {
/*  816 */             this.guiScale = Integer.parseInt(astring[1]);
/*      */           }
/*      */           
/*  819 */           if (astring[0].equals("particles"))
/*      */           {
/*  821 */             this.particleSetting = Integer.parseInt(astring[1]);
/*      */           }
/*      */           
/*  824 */           if (astring[0].equals("bobView"))
/*      */           {
/*  826 */             this.viewBobbing = astring[1].equals("true");
/*      */           }
/*      */           
/*  829 */           if (astring[0].equals("anaglyph3d"))
/*      */           {
/*  831 */             this.anaglyph = astring[1].equals("true");
/*      */           }
/*      */           
/*  834 */           if (astring[0].equals("maxFps"))
/*      */           {
/*  836 */             this.limitFramerate = Integer.parseInt(astring[1]);
/*  837 */             this.enableVsync = false;
/*      */             
/*  839 */             if (this.limitFramerate <= 0)
/*      */             {
/*  841 */               this.limitFramerate = ((int)Options.FRAMERATE_LIMIT.getValueMax());
/*  842 */               this.enableVsync = true;
/*      */             }
/*      */             
/*  845 */             updateVSync();
/*      */           }
/*      */           
/*  848 */           if (astring[0].equals("fboEnable"))
/*      */           {
/*  850 */             this.fboEnable = astring[1].equals("true");
/*      */           }
/*      */           
/*  853 */           if (astring[0].equals("difficulty"))
/*      */           {
/*  855 */             this.difficulty = EnumDifficulty.getDifficultyEnum(Integer.parseInt(astring[1]));
/*      */           }
/*      */           
/*  858 */           if (astring[0].equals("fancyGraphics"))
/*      */           {
/*  860 */             this.fancyGraphics = astring[1].equals("true");
/*  861 */             updateRenderClouds();
/*      */           }
/*      */           
/*  864 */           if (astring[0].equals("ao"))
/*      */           {
/*  866 */             if (astring[1].equals("true"))
/*      */             {
/*  868 */               this.ambientOcclusion = 2;
/*      */             }
/*  870 */             else if (astring[1].equals("false"))
/*      */             {
/*  872 */               this.ambientOcclusion = 0;
/*      */             }
/*      */             else
/*      */             {
/*  876 */               this.ambientOcclusion = Integer.parseInt(astring[1]);
/*      */             }
/*      */           }
/*      */           
/*  880 */           if (astring[0].equals("renderClouds"))
/*      */           {
/*  882 */             if (astring[1].equals("true"))
/*      */             {
/*  884 */               this.clouds = 2;
/*      */             }
/*  886 */             else if (astring[1].equals("false"))
/*      */             {
/*  888 */               this.clouds = 0;
/*      */             }
/*  890 */             else if (astring[1].equals("fast"))
/*      */             {
/*  892 */               this.clouds = 1;
/*      */             }
/*      */           }
/*      */           
/*  896 */           if (astring[0].equals("resourcePacks"))
/*      */           {
/*  898 */             this.resourcePacks = ((List)gson.fromJson(s.substring(s.indexOf(':') + 1), typeListString));
/*      */             
/*  900 */             if (this.resourcePacks == null)
/*      */             {
/*  902 */               this.resourcePacks = Lists.newArrayList();
/*      */             }
/*      */           }
/*      */           
/*  906 */           if (astring[0].equals("incompatibleResourcePacks"))
/*      */           {
/*  908 */             this.field_183018_l = ((List)gson.fromJson(s.substring(s.indexOf(':') + 1), typeListString));
/*      */             
/*  910 */             if (this.field_183018_l == null)
/*      */             {
/*  912 */               this.field_183018_l = Lists.newArrayList();
/*      */             }
/*      */           }
/*      */           
/*  916 */           if ((astring[0].equals("lastServer")) && (astring.length >= 2))
/*      */           {
/*  918 */             this.lastServer = s.substring(s.indexOf(':') + 1);
/*      */           }
/*      */           
/*  921 */           if ((astring[0].equals("lang")) && (astring.length >= 2))
/*      */           {
/*  923 */             this.language = astring[1];
/*      */           }
/*      */           
/*  926 */           if (astring[0].equals("chatVisibility"))
/*      */           {
/*  928 */             this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility(Integer.parseInt(astring[1]));
/*      */           }
/*      */           
/*  931 */           if (astring[0].equals("chatColors"))
/*      */           {
/*  933 */             this.chatColours = astring[1].equals("true");
/*      */           }
/*      */           
/*  936 */           if (astring[0].equals("chatLinks"))
/*      */           {
/*  938 */             this.chatLinks = astring[1].equals("true");
/*      */           }
/*      */           
/*  941 */           if (astring[0].equals("chatLinksPrompt"))
/*      */           {
/*  943 */             this.chatLinksPrompt = astring[1].equals("true");
/*      */           }
/*      */           
/*  946 */           if (astring[0].equals("chatOpacity"))
/*      */           {
/*  948 */             this.chatOpacity = parseFloat(astring[1]);
/*      */           }
/*      */           
/*  951 */           if (astring[0].equals("snooperEnabled"))
/*      */           {
/*  953 */             this.snooperEnabled = astring[1].equals("true");
/*      */           }
/*      */           
/*  956 */           if (astring[0].equals("fullscreen"))
/*      */           {
/*  958 */             this.fullScreen = astring[1].equals("true");
/*      */           }
/*      */           
/*  961 */           if (astring[0].equals("enableVsync"))
/*      */           {
/*  963 */             this.enableVsync = astring[1].equals("true");
/*  964 */             updateVSync();
/*      */           }
/*      */           
/*  967 */           if (astring[0].equals("useVbo"))
/*      */           {
/*  969 */             this.useVbo = astring[1].equals("true");
/*      */           }
/*      */           
/*  972 */           if (astring[0].equals("hideServerAddress"))
/*      */           {
/*  974 */             this.hideServerAddress = astring[1].equals("true");
/*      */           }
/*      */           
/*  977 */           if (astring[0].equals("advancedItemTooltips"))
/*      */           {
/*  979 */             this.advancedItemTooltips = astring[1].equals("true");
/*      */           }
/*      */           
/*  982 */           if (astring[0].equals("pauseOnLostFocus"))
/*      */           {
/*  984 */             this.pauseOnLostFocus = astring[1].equals("true");
/*      */           }
/*      */           
/*  987 */           if (astring[0].equals("touchscreen"))
/*      */           {
/*  989 */             this.touchscreen = astring[1].equals("true");
/*      */           }
/*      */           
/*  992 */           if (astring[0].equals("overrideHeight"))
/*      */           {
/*  994 */             this.overrideHeight = Integer.parseInt(astring[1]);
/*      */           }
/*      */           
/*  997 */           if (astring[0].equals("overrideWidth"))
/*      */           {
/*  999 */             this.overrideWidth = Integer.parseInt(astring[1]);
/*      */           }
/*      */           
/* 1002 */           if (astring[0].equals("heldItemTooltips"))
/*      */           {
/* 1004 */             this.heldItemTooltips = astring[1].equals("true");
/*      */           }
/*      */           
/* 1007 */           if (astring[0].equals("chatHeightFocused"))
/*      */           {
/* 1009 */             this.chatHeightFocused = parseFloat(astring[1]);
/*      */           }
/*      */           
/* 1012 */           if (astring[0].equals("chatHeightUnfocused"))
/*      */           {
/* 1014 */             this.chatHeightUnfocused = parseFloat(astring[1]);
/*      */           }
/*      */           
/* 1017 */           if (astring[0].equals("chatScale"))
/*      */           {
/* 1019 */             this.chatScale = parseFloat(astring[1]);
/*      */           }
/*      */           
/* 1022 */           if (astring[0].equals("chatWidth"))
/*      */           {
/* 1024 */             this.chatWidth = parseFloat(astring[1]);
/*      */           }
/*      */           
/* 1027 */           if (astring[0].equals("showInventoryAchievementHint"))
/*      */           {
/* 1029 */             this.showInventoryAchievementHint = astring[1].equals("true");
/*      */           }
/*      */           
/* 1032 */           if (astring[0].equals("mipmapLevels"))
/*      */           {
/* 1034 */             this.mipmapLevels = Integer.parseInt(astring[1]);
/*      */           }
/*      */           
/* 1037 */           if (astring[0].equals("streamBytesPerPixel"))
/*      */           {
/* 1039 */             this.streamBytesPerPixel = parseFloat(astring[1]);
/*      */           }
/*      */           
/* 1042 */           if (astring[0].equals("streamMicVolume"))
/*      */           {
/* 1044 */             this.streamMicVolume = parseFloat(astring[1]);
/*      */           }
/*      */           
/* 1047 */           if (astring[0].equals("streamSystemVolume"))
/*      */           {
/* 1049 */             this.streamGameVolume = parseFloat(astring[1]);
/*      */           }
/*      */           
/* 1052 */           if (astring[0].equals("streamKbps"))
/*      */           {
/* 1054 */             this.streamKbps = parseFloat(astring[1]);
/*      */           }
/*      */           
/* 1057 */           if (astring[0].equals("streamFps"))
/*      */           {
/* 1059 */             this.streamFps = parseFloat(astring[1]);
/*      */           }
/*      */           
/* 1062 */           if (astring[0].equals("streamCompression"))
/*      */           {
/* 1064 */             this.streamCompression = Integer.parseInt(astring[1]);
/*      */           }
/*      */           
/* 1067 */           if (astring[0].equals("streamSendMetadata"))
/*      */           {
/* 1069 */             this.streamSendMetadata = astring[1].equals("true");
/*      */           }
/*      */           
/* 1072 */           if ((astring[0].equals("streamPreferredServer")) && (astring.length >= 2))
/*      */           {
/* 1074 */             this.streamPreferredServer = s.substring(s.indexOf(':') + 1);
/*      */           }
/*      */           
/* 1077 */           if (astring[0].equals("streamChatEnabled"))
/*      */           {
/* 1079 */             this.streamChatEnabled = Integer.parseInt(astring[1]);
/*      */           }
/*      */           
/* 1082 */           if (astring[0].equals("streamChatUserFilter"))
/*      */           {
/* 1084 */             this.streamChatUserFilter = Integer.parseInt(astring[1]);
/*      */           }
/*      */           
/* 1087 */           if (astring[0].equals("streamMicToggleBehavior"))
/*      */           {
/* 1089 */             this.streamMicToggleBehavior = Integer.parseInt(astring[1]);
/*      */           }
/*      */           
/* 1092 */           if (astring[0].equals("forceUnicodeFont"))
/*      */           {
/* 1094 */             this.forceUnicodeFont = astring[1].equals("true");
/*      */           }
/*      */           
/* 1097 */           if (astring[0].equals("allowBlockAlternatives"))
/*      */           {
/* 1099 */             this.allowBlockAlternatives = astring[1].equals("true");
/*      */           }
/*      */           
/* 1102 */           if (astring[0].equals("reducedDebugInfo"))
/*      */           {
/* 1104 */             this.reducedDebugInfo = astring[1].equals("true");
/*      */           }
/*      */           
/* 1107 */           if (astring[0].equals("useNativeTransport"))
/*      */           {
/* 1109 */             this.field_181150_U = astring[1].equals("true");
/*      */           }
/*      */           
/* 1112 */           if (astring[0].equals("entityShadows"))
/*      */           {
/* 1114 */             this.field_181151_V = astring[1].equals("true");
/*      */           }
/*      */           Object localObject;
/* 1117 */           int j = (localObject = this.keyBindings).length; for (int i = 0; i < j; i++) { KeyBinding keybinding = localObject[i];
/*      */             
/* 1119 */             if (astring[0].equals("key_" + keybinding.getKeyDescription()))
/*      */             {
/* 1121 */               keybinding.setKeyCode(Integer.parseInt(astring[1]));
/*      */             }
/*      */           }
/*      */           
/* 1125 */           j = (localObject = SoundCategory.values()).length; for (i = 0; i < j; i++) { SoundCategory soundcategory = localObject[i];
/*      */             
/* 1127 */             if (astring[0].equals("soundCategory_" + soundcategory.getCategoryName()))
/*      */             {
/* 1129 */               this.mapSoundLevels.put(soundcategory, Float.valueOf(parseFloat(astring[1])));
/*      */             }
/*      */           }
/*      */           
/* 1133 */           j = (localObject = EnumPlayerModelParts.values()).length; for (i = 0; i < j; i++) { EnumPlayerModelParts enumplayermodelparts = localObject[i];
/*      */             
/* 1135 */             if (astring[0].equals("modelPart_" + enumplayermodelparts.getPartName()))
/*      */             {
/* 1137 */               setModelPartEnabled(enumplayermodelparts, astring[1].equals("true"));
/*      */             }
/*      */           }
/*      */         }
/*      */         catch (Exception exception)
/*      */         {
/* 1143 */           logger.warn("Skipping bad option: " + s);
/* 1144 */           exception.printStackTrace();
/*      */         }
/*      */       }
/*      */       
/* 1148 */       KeyBinding.resetKeyBindingArrayAndHash();
/* 1149 */       bufferedreader.close();
/*      */     }
/*      */     catch (Exception exception1)
/*      */     {
/* 1153 */       logger.error("Failed to load options", exception1);
/*      */     }
/*      */     
/* 1156 */     loadOfOptions();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private float parseFloat(String p_74305_1_)
/*      */   {
/* 1164 */     return p_74305_1_.equals("false") ? 0.0F : p_74305_1_.equals("true") ? 1.0F : Float.parseFloat(p_74305_1_);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void saveOptions()
/*      */   {
/* 1172 */     if (Reflector.FMLClientHandler.exists())
/*      */     {
/* 1174 */       Object object = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
/*      */       
/* 1176 */       if ((object != null) && (Reflector.callBoolean(object, Reflector.FMLClientHandler_isLoading, new Object[0])))
/*      */       {
/* 1178 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */     try
/*      */     {
/* 1184 */       PrintWriter printwriter = new PrintWriter(new FileWriter(this.optionsFile));
/* 1185 */       printwriter.println("invertYMouse:" + this.invertMouse);
/* 1186 */       printwriter.println("mouseSensitivity:" + this.mouseSensitivity);
/* 1187 */       printwriter.println("fov:" + (this.fovSetting - 70.0F) / 40.0F);
/* 1188 */       printwriter.println("gamma:" + this.gammaSetting);
/* 1189 */       printwriter.println("saturation:" + this.saturation);
/* 1190 */       printwriter.println("renderDistance:" + this.renderDistanceChunks);
/* 1191 */       printwriter.println("guiScale:" + this.guiScale);
/* 1192 */       printwriter.println("particles:" + this.particleSetting);
/* 1193 */       printwriter.println("bobView:" + this.viewBobbing);
/* 1194 */       printwriter.println("anaglyph3d:" + this.anaglyph);
/* 1195 */       printwriter.println("maxFps:" + this.limitFramerate);
/* 1196 */       printwriter.println("fboEnable:" + this.fboEnable);
/* 1197 */       printwriter.println("difficulty:" + this.difficulty.getDifficultyId());
/* 1198 */       printwriter.println("fancyGraphics:" + this.fancyGraphics);
/* 1199 */       printwriter.println("ao:" + this.ambientOcclusion);
/*      */       
/* 1201 */       switch (this.clouds)
/*      */       {
/*      */       case 0: 
/* 1204 */         printwriter.println("renderClouds:false");
/* 1205 */         break;
/*      */       
/*      */       case 1: 
/* 1208 */         printwriter.println("renderClouds:fast");
/* 1209 */         break;
/*      */       
/*      */       case 2: 
/* 1212 */         printwriter.println("renderClouds:true");
/*      */       }
/*      */       
/* 1215 */       printwriter.println("resourcePacks:" + gson.toJson(this.resourcePacks));
/* 1216 */       printwriter.println("incompatibleResourcePacks:" + gson.toJson(this.field_183018_l));
/* 1217 */       printwriter.println("lastServer:" + this.lastServer);
/* 1218 */       printwriter.println("lang:" + this.language);
/* 1219 */       printwriter.println("chatVisibility:" + this.chatVisibility.getChatVisibility());
/* 1220 */       printwriter.println("chatColors:" + this.chatColours);
/* 1221 */       printwriter.println("chatLinks:" + this.chatLinks);
/* 1222 */       printwriter.println("chatLinksPrompt:" + this.chatLinksPrompt);
/* 1223 */       printwriter.println("chatOpacity:" + this.chatOpacity);
/* 1224 */       printwriter.println("snooperEnabled:" + this.snooperEnabled);
/* 1225 */       printwriter.println("fullscreen:" + this.fullScreen);
/* 1226 */       printwriter.println("enableVsync:" + this.enableVsync);
/* 1227 */       printwriter.println("useVbo:" + this.useVbo);
/* 1228 */       printwriter.println("hideServerAddress:" + this.hideServerAddress);
/* 1229 */       printwriter.println("advancedItemTooltips:" + this.advancedItemTooltips);
/* 1230 */       printwriter.println("pauseOnLostFocus:" + this.pauseOnLostFocus);
/* 1231 */       printwriter.println("touchscreen:" + this.touchscreen);
/* 1232 */       printwriter.println("overrideWidth:" + this.overrideWidth);
/* 1233 */       printwriter.println("overrideHeight:" + this.overrideHeight);
/* 1234 */       printwriter.println("heldItemTooltips:" + this.heldItemTooltips);
/* 1235 */       printwriter.println("chatHeightFocused:" + this.chatHeightFocused);
/* 1236 */       printwriter.println("chatHeightUnfocused:" + this.chatHeightUnfocused);
/* 1237 */       printwriter.println("chatScale:" + this.chatScale);
/* 1238 */       printwriter.println("chatWidth:" + this.chatWidth);
/* 1239 */       printwriter.println("showInventoryAchievementHint:" + this.showInventoryAchievementHint);
/* 1240 */       printwriter.println("mipmapLevels:" + this.mipmapLevels);
/* 1241 */       printwriter.println("streamBytesPerPixel:" + this.streamBytesPerPixel);
/* 1242 */       printwriter.println("streamMicVolume:" + this.streamMicVolume);
/* 1243 */       printwriter.println("streamSystemVolume:" + this.streamGameVolume);
/* 1244 */       printwriter.println("streamKbps:" + this.streamKbps);
/* 1245 */       printwriter.println("streamFps:" + this.streamFps);
/* 1246 */       printwriter.println("streamCompression:" + this.streamCompression);
/* 1247 */       printwriter.println("streamSendMetadata:" + this.streamSendMetadata);
/* 1248 */       printwriter.println("streamPreferredServer:" + this.streamPreferredServer);
/* 1249 */       printwriter.println("streamChatEnabled:" + this.streamChatEnabled);
/* 1250 */       printwriter.println("streamChatUserFilter:" + this.streamChatUserFilter);
/* 1251 */       printwriter.println("streamMicToggleBehavior:" + this.streamMicToggleBehavior);
/* 1252 */       printwriter.println("forceUnicodeFont:" + this.forceUnicodeFont);
/* 1253 */       printwriter.println("allowBlockAlternatives:" + this.allowBlockAlternatives);
/* 1254 */       printwriter.println("reducedDebugInfo:" + this.reducedDebugInfo);
/* 1255 */       printwriter.println("useNativeTransport:" + this.field_181150_U);
/* 1256 */       printwriter.println("entityShadows:" + this.field_181151_V);
/*      */       Object localObject1;
/* 1258 */       int j = (localObject1 = this.keyBindings).length; for (int i = 0; i < j; i++) { KeyBinding keybinding = localObject1[i];
/*      */         
/* 1260 */         printwriter.println("key_" + keybinding.getKeyDescription() + ":" + keybinding.getKeyCode());
/*      */       }
/*      */       
/* 1263 */       j = (localObject1 = SoundCategory.values()).length; for (i = 0; i < j; i++) { SoundCategory soundcategory = localObject1[i];
/*      */         
/* 1265 */         printwriter.println("soundCategory_" + soundcategory.getCategoryName() + ":" + getSoundLevel(soundcategory));
/*      */       }
/*      */       
/* 1268 */       j = (localObject1 = EnumPlayerModelParts.values()).length; for (i = 0; i < j; i++) { EnumPlayerModelParts enumplayermodelparts = localObject1[i];
/*      */         
/* 1270 */         printwriter.println("modelPart_" + enumplayermodelparts.getPartName() + ":" + this.setModelParts.contains(enumplayermodelparts));
/*      */       }
/*      */       
/* 1273 */       printwriter.close();
/*      */     }
/*      */     catch (Exception exception)
/*      */     {
/* 1277 */       logger.error("Failed to save options", exception);
/*      */     }
/*      */     
/* 1280 */     saveOfOptions();
/* 1281 */     sendSettingsToServer();
/*      */   }
/*      */   
/*      */   public float getSoundLevel(SoundCategory p_151438_1_)
/*      */   {
/* 1286 */     return this.mapSoundLevels.containsKey(p_151438_1_) ? ((Float)this.mapSoundLevels.get(p_151438_1_)).floatValue() : 1.0F;
/*      */   }
/*      */   
/*      */   public void setSoundLevel(SoundCategory p_151439_1_, float p_151439_2_)
/*      */   {
/* 1291 */     this.mc.getSoundHandler().setSoundLevel(p_151439_1_, p_151439_2_);
/* 1292 */     this.mapSoundLevels.put(p_151439_1_, Float.valueOf(p_151439_2_));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void sendSettingsToServer()
/*      */   {
/* 1300 */     if (this.mc.thePlayer != null)
/*      */     {
/* 1302 */       int i = 0;
/*      */       
/* 1304 */       for (Object enumplayermodelparts : this.setModelParts)
/*      */       {
/* 1306 */         i |= ((EnumPlayerModelParts)enumplayermodelparts).getPartMask();
/*      */       }
/*      */       
/* 1309 */       this.mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C15PacketClientSettings(this.language, this.renderDistanceChunks, this.chatVisibility, this.chatColours, i));
/*      */     }
/*      */   }
/*      */   
/*      */   public Set getModelParts()
/*      */   {
/* 1315 */     return ImmutableSet.copyOf(this.setModelParts);
/*      */   }
/*      */   
/*      */   public void setModelPartEnabled(EnumPlayerModelParts p_178878_1_, boolean p_178878_2_)
/*      */   {
/* 1320 */     if (p_178878_2_)
/*      */     {
/* 1322 */       this.setModelParts.add(p_178878_1_);
/*      */     }
/*      */     else
/*      */     {
/* 1326 */       this.setModelParts.remove(p_178878_1_);
/*      */     }
/*      */     
/* 1329 */     sendSettingsToServer();
/*      */   }
/*      */   
/*      */   public void switchModelPartEnabled(EnumPlayerModelParts p_178877_1_)
/*      */   {
/* 1334 */     if (!getModelParts().contains(p_178877_1_))
/*      */     {
/* 1336 */       this.setModelParts.add(p_178877_1_);
/*      */     }
/*      */     else
/*      */     {
/* 1340 */       this.setModelParts.remove(p_178877_1_);
/*      */     }
/*      */     
/* 1343 */     sendSettingsToServer();
/*      */   }
/*      */   
/*      */   public int func_181147_e()
/*      */   {
/* 1348 */     return this.renderDistanceChunks >= 4 ? this.clouds : 0;
/*      */   }
/*      */   
/*      */   public boolean func_181148_f()
/*      */   {
/* 1353 */     return this.field_181150_U;
/*      */   }
/*      */   
/*      */   private void setOptionFloatValueOF(Options p_setOptionFloatValueOF_1_, float p_setOptionFloatValueOF_2_)
/*      */   {
/* 1358 */     if (p_setOptionFloatValueOF_1_ == Options.CLOUD_HEIGHT)
/*      */     {
/* 1360 */       this.ofCloudsHeight = p_setOptionFloatValueOF_2_;
/* 1361 */       this.mc.renderGlobal.resetClouds();
/*      */     }
/*      */     
/* 1364 */     if (p_setOptionFloatValueOF_1_ == Options.AO_LEVEL)
/*      */     {
/* 1366 */       this.ofAoLevel = p_setOptionFloatValueOF_2_;
/* 1367 */       this.mc.renderGlobal.loadRenderers();
/*      */     }
/*      */     
/* 1370 */     if (p_setOptionFloatValueOF_1_ == Options.AA_LEVEL)
/*      */     {
/* 1372 */       int[] aint = { 0, 2, 4, 6, 8, 12, 16 };
/* 1373 */       this.ofAaLevel = 0;
/* 1374 */       int i = (int)p_setOptionFloatValueOF_2_;
/*      */       
/* 1376 */       for (int j = 0; j < aint.length; j++)
/*      */       {
/* 1378 */         if (i >= aint[j])
/*      */         {
/* 1380 */           this.ofAaLevel = aint[j];
/*      */         }
/*      */       }
/*      */       
/* 1384 */       this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
/*      */     }
/*      */     
/* 1387 */     if (p_setOptionFloatValueOF_1_ == Options.AF_LEVEL)
/*      */     {
/* 1389 */       int k = (int)p_setOptionFloatValueOF_2_;
/*      */       
/* 1391 */       for (this.ofAfLevel = 1; this.ofAfLevel * 2 <= k; this.ofAfLevel *= 2) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1396 */       this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
/* 1397 */       this.mc.refreshResources();
/*      */     }
/*      */     
/* 1400 */     if (p_setOptionFloatValueOF_1_ == Options.MIPMAP_TYPE)
/*      */     {
/* 1402 */       int l = (int)p_setOptionFloatValueOF_2_;
/* 1403 */       this.ofMipmapType = Config.limit(l, 0, 3);
/* 1404 */       this.mc.refreshResources();
/*      */     }
/*      */   }
/*      */   
/*      */   private void setOptionValueOF(Options p_setOptionValueOF_1_, int p_setOptionValueOF_2_)
/*      */   {
/* 1410 */     if (p_setOptionValueOF_1_ == Options.FOG_FANCY)
/*      */     {
/* 1412 */       switch (this.ofFogType)
/*      */       {
/*      */       case 1: 
/* 1415 */         this.ofFogType = 2;
/*      */         
/* 1417 */         if (!Config.isFancyFogAvailable())
/*      */         {
/* 1419 */           this.ofFogType = 3;
/*      */         }
/*      */         
/* 1422 */         break;
/*      */       
/*      */       case 2: 
/* 1425 */         this.ofFogType = 3;
/* 1426 */         break;
/*      */       
/*      */       case 3: 
/* 1429 */         this.ofFogType = 1;
/* 1430 */         break;
/*      */       
/*      */       default: 
/* 1433 */         this.ofFogType = 1;
/*      */       }
/*      */       
/*      */     }
/* 1437 */     if (p_setOptionValueOF_1_ == Options.FOG_START)
/*      */     {
/* 1439 */       this.ofFogStart += 0.2F;
/*      */       
/* 1441 */       if (this.ofFogStart > 0.81F)
/*      */       {
/* 1443 */         this.ofFogStart = 0.2F;
/*      */       }
/*      */     }
/*      */     
/* 1447 */     if (p_setOptionValueOF_1_ == Options.SMOOTH_FPS)
/*      */     {
/* 1449 */       this.ofSmoothFps = (!this.ofSmoothFps);
/*      */     }
/*      */     
/* 1452 */     if (p_setOptionValueOF_1_ == Options.SMOOTH_WORLD)
/*      */     {
/* 1454 */       this.ofSmoothWorld = (!this.ofSmoothWorld);
/* 1455 */       Config.updateThreadPriorities();
/*      */     }
/*      */     
/* 1458 */     if (p_setOptionValueOF_1_ == Options.CLOUDS)
/*      */     {
/* 1460 */       this.ofClouds += 1;
/*      */       
/* 1462 */       if (this.ofClouds > 3)
/*      */       {
/* 1464 */         this.ofClouds = 0;
/*      */       }
/*      */       
/* 1467 */       updateRenderClouds();
/* 1468 */       this.mc.renderGlobal.resetClouds();
/*      */     }
/*      */     
/* 1471 */     if (p_setOptionValueOF_1_ == Options.TREES)
/*      */     {
/* 1473 */       this.ofTrees += 1;
/*      */       
/* 1475 */       if (this.ofTrees > 2)
/*      */       {
/* 1477 */         this.ofTrees = 0;
/*      */       }
/*      */       
/* 1480 */       this.mc.renderGlobal.loadRenderers();
/*      */     }
/*      */     
/* 1483 */     if (p_setOptionValueOF_1_ == Options.DROPPED_ITEMS)
/*      */     {
/* 1485 */       this.ofDroppedItems += 1;
/*      */       
/* 1487 */       if (this.ofDroppedItems > 2)
/*      */       {
/* 1489 */         this.ofDroppedItems = 0;
/*      */       }
/*      */     }
/*      */     
/* 1493 */     if (p_setOptionValueOF_1_ == Options.RAIN)
/*      */     {
/* 1495 */       this.ofRain += 1;
/*      */       
/* 1497 */       if (this.ofRain > 3)
/*      */       {
/* 1499 */         this.ofRain = 0;
/*      */       }
/*      */     }
/*      */     
/* 1503 */     if (p_setOptionValueOF_1_ == Options.ANIMATED_WATER)
/*      */     {
/* 1505 */       this.ofAnimatedWater += 1;
/*      */       
/* 1507 */       if (this.ofAnimatedWater > 2)
/*      */       {
/* 1509 */         this.ofAnimatedWater = 0;
/*      */       }
/*      */     }
/*      */     
/* 1513 */     if (p_setOptionValueOF_1_ == Options.ANIMATED_LAVA)
/*      */     {
/* 1515 */       this.ofAnimatedLava += 1;
/*      */       
/* 1517 */       if (this.ofAnimatedLava > 2)
/*      */       {
/* 1519 */         this.ofAnimatedLava = 0;
/*      */       }
/*      */     }
/*      */     
/* 1523 */     if (p_setOptionValueOF_1_ == Options.ANIMATED_FIRE)
/*      */     {
/* 1525 */       this.ofAnimatedFire = (!this.ofAnimatedFire);
/*      */     }
/*      */     
/* 1528 */     if (p_setOptionValueOF_1_ == Options.ANIMATED_PORTAL)
/*      */     {
/* 1530 */       this.ofAnimatedPortal = (!this.ofAnimatedPortal);
/*      */     }
/*      */     
/* 1533 */     if (p_setOptionValueOF_1_ == Options.ANIMATED_REDSTONE)
/*      */     {
/* 1535 */       this.ofAnimatedRedstone = (!this.ofAnimatedRedstone);
/*      */     }
/*      */     
/* 1538 */     if (p_setOptionValueOF_1_ == Options.ANIMATED_EXPLOSION)
/*      */     {
/* 1540 */       this.ofAnimatedExplosion = (!this.ofAnimatedExplosion);
/*      */     }
/*      */     
/* 1543 */     if (p_setOptionValueOF_1_ == Options.ANIMATED_FLAME)
/*      */     {
/* 1545 */       this.ofAnimatedFlame = (!this.ofAnimatedFlame);
/*      */     }
/*      */     
/* 1548 */     if (p_setOptionValueOF_1_ == Options.ANIMATED_SMOKE)
/*      */     {
/* 1550 */       this.ofAnimatedSmoke = (!this.ofAnimatedSmoke);
/*      */     }
/*      */     
/* 1553 */     if (p_setOptionValueOF_1_ == Options.VOID_PARTICLES)
/*      */     {
/* 1555 */       this.ofVoidParticles = (!this.ofVoidParticles);
/*      */     }
/*      */     
/* 1558 */     if (p_setOptionValueOF_1_ == Options.WATER_PARTICLES)
/*      */     {
/* 1560 */       this.ofWaterParticles = (!this.ofWaterParticles);
/*      */     }
/*      */     
/* 1563 */     if (p_setOptionValueOF_1_ == Options.PORTAL_PARTICLES)
/*      */     {
/* 1565 */       this.ofPortalParticles = (!this.ofPortalParticles);
/*      */     }
/*      */     
/* 1568 */     if (p_setOptionValueOF_1_ == Options.POTION_PARTICLES)
/*      */     {
/* 1570 */       this.ofPotionParticles = (!this.ofPotionParticles);
/*      */     }
/*      */     
/* 1573 */     if (p_setOptionValueOF_1_ == Options.FIREWORK_PARTICLES)
/*      */     {
/* 1575 */       this.ofFireworkParticles = (!this.ofFireworkParticles);
/*      */     }
/*      */     
/* 1578 */     if (p_setOptionValueOF_1_ == Options.DRIPPING_WATER_LAVA)
/*      */     {
/* 1580 */       this.ofDrippingWaterLava = (!this.ofDrippingWaterLava);
/*      */     }
/*      */     
/* 1583 */     if (p_setOptionValueOF_1_ == Options.ANIMATED_TERRAIN)
/*      */     {
/* 1585 */       this.ofAnimatedTerrain = (!this.ofAnimatedTerrain);
/*      */     }
/*      */     
/* 1588 */     if (p_setOptionValueOF_1_ == Options.ANIMATED_TEXTURES)
/*      */     {
/* 1590 */       this.ofAnimatedTextures = (!this.ofAnimatedTextures);
/*      */     }
/*      */     
/* 1593 */     if (p_setOptionValueOF_1_ == Options.RAIN_SPLASH)
/*      */     {
/* 1595 */       this.ofRainSplash = (!this.ofRainSplash);
/*      */     }
/*      */     
/* 1598 */     if (p_setOptionValueOF_1_ == Options.LAGOMETER)
/*      */     {
/* 1600 */       this.ofLagometer = (!this.ofLagometer);
/*      */     }
/*      */     
/* 1603 */     if (p_setOptionValueOF_1_ == Options.SHOW_FPS)
/*      */     {
/* 1605 */       this.ofShowFps = (!this.ofShowFps);
/*      */     }
/*      */     
/* 1608 */     if (p_setOptionValueOF_1_ == Options.AUTOSAVE_TICKS)
/*      */     {
/* 1610 */       this.ofAutoSaveTicks *= 10;
/*      */       
/* 1612 */       if (this.ofAutoSaveTicks > 40000)
/*      */       {
/* 1614 */         this.ofAutoSaveTicks = 40;
/*      */       }
/*      */     }
/*      */     
/* 1618 */     if (p_setOptionValueOF_1_ == Options.BETTER_GRASS)
/*      */     {
/* 1620 */       this.ofBetterGrass += 1;
/*      */       
/* 1622 */       if (this.ofBetterGrass > 3)
/*      */       {
/* 1624 */         this.ofBetterGrass = 1;
/*      */       }
/*      */       
/* 1627 */       this.mc.renderGlobal.loadRenderers();
/*      */     }
/*      */     
/* 1630 */     if (p_setOptionValueOF_1_ == Options.CONNECTED_TEXTURES)
/*      */     {
/* 1632 */       this.ofConnectedTextures += 1;
/*      */       
/* 1634 */       if (this.ofConnectedTextures > 3)
/*      */       {
/* 1636 */         this.ofConnectedTextures = 1;
/*      */       }
/*      */       
/* 1639 */       if (this.ofConnectedTextures != 2)
/*      */       {
/* 1641 */         this.mc.refreshResources();
/*      */       }
/*      */     }
/*      */     
/* 1645 */     if (p_setOptionValueOF_1_ == Options.WEATHER)
/*      */     {
/* 1647 */       this.ofWeather = (!this.ofWeather);
/*      */     }
/*      */     
/* 1650 */     if (p_setOptionValueOF_1_ == Options.SKY)
/*      */     {
/* 1652 */       this.ofSky = (!this.ofSky);
/*      */     }
/*      */     
/* 1655 */     if (p_setOptionValueOF_1_ == Options.STARS)
/*      */     {
/* 1657 */       this.ofStars = (!this.ofStars);
/*      */     }
/*      */     
/* 1660 */     if (p_setOptionValueOF_1_ == Options.SUN_MOON)
/*      */     {
/* 1662 */       this.ofSunMoon = (!this.ofSunMoon);
/*      */     }
/*      */     
/* 1665 */     if (p_setOptionValueOF_1_ == Options.VIGNETTE)
/*      */     {
/* 1667 */       this.ofVignette += 1;
/*      */       
/* 1669 */       if (this.ofVignette > 2)
/*      */       {
/* 1671 */         this.ofVignette = 0;
/*      */       }
/*      */     }
/*      */     
/* 1675 */     if (p_setOptionValueOF_1_ == Options.CHUNK_UPDATES)
/*      */     {
/* 1677 */       this.ofChunkUpdates += 1;
/*      */       
/* 1679 */       if (this.ofChunkUpdates > 5)
/*      */       {
/* 1681 */         this.ofChunkUpdates = 1;
/*      */       }
/*      */     }
/*      */     
/* 1685 */     if (p_setOptionValueOF_1_ == Options.CHUNK_LOADING)
/*      */     {
/* 1687 */       this.ofChunkLoading += 1;
/*      */       
/* 1689 */       if (this.ofChunkLoading > 2)
/*      */       {
/* 1691 */         this.ofChunkLoading = 0;
/*      */       }
/*      */       
/* 1694 */       updateChunkLoading();
/*      */     }
/*      */     
/* 1697 */     if (p_setOptionValueOF_1_ == Options.CHUNK_UPDATES_DYNAMIC)
/*      */     {
/* 1699 */       this.ofChunkUpdatesDynamic = (!this.ofChunkUpdatesDynamic);
/*      */     }
/*      */     
/* 1702 */     if (p_setOptionValueOF_1_ == Options.TIME)
/*      */     {
/* 1704 */       this.ofTime += 1;
/*      */       
/* 1706 */       if (this.ofTime > 3)
/*      */       {
/* 1708 */         this.ofTime = 0;
/*      */       }
/*      */     }
/*      */     
/* 1712 */     if (p_setOptionValueOF_1_ == Options.CLEAR_WATER)
/*      */     {
/* 1714 */       this.ofClearWater = (!this.ofClearWater);
/* 1715 */       updateWaterOpacity();
/*      */     }
/*      */     
/* 1718 */     if (p_setOptionValueOF_1_ == Options.PROFILER)
/*      */     {
/* 1720 */       this.ofProfiler = (!this.ofProfiler);
/*      */     }
/*      */     
/* 1723 */     if (p_setOptionValueOF_1_ == Options.BETTER_SNOW)
/*      */     {
/* 1725 */       this.ofBetterSnow = (!this.ofBetterSnow);
/* 1726 */       this.mc.renderGlobal.loadRenderers();
/*      */     }
/*      */     
/* 1729 */     if (p_setOptionValueOF_1_ == Options.SWAMP_COLORS)
/*      */     {
/* 1731 */       this.ofSwampColors = (!this.ofSwampColors);
/* 1732 */       CustomColorizer.updateUseDefaultColorMultiplier();
/* 1733 */       this.mc.renderGlobal.loadRenderers();
/*      */     }
/*      */     
/* 1736 */     if (p_setOptionValueOF_1_ == Options.RANDOM_MOBS)
/*      */     {
/* 1738 */       this.ofRandomMobs = (!this.ofRandomMobs);
/* 1739 */       optfine.RandomMobs.resetTextures();
/*      */     }
/*      */     
/* 1742 */     if (p_setOptionValueOF_1_ == Options.SMOOTH_BIOMES)
/*      */     {
/* 1744 */       this.ofSmoothBiomes = (!this.ofSmoothBiomes);
/* 1745 */       CustomColorizer.updateUseDefaultColorMultiplier();
/* 1746 */       this.mc.renderGlobal.loadRenderers();
/*      */     }
/*      */     
/* 1749 */     if (p_setOptionValueOF_1_ == Options.CUSTOM_FONTS)
/*      */     {
/* 1751 */       this.ofCustomFonts = (!this.ofCustomFonts);
/* 1752 */       this.mc.fontRendererObj.onResourceManagerReload(Config.getResourceManager());
/* 1753 */       this.mc.standardGalacticFontRenderer.onResourceManagerReload(Config.getResourceManager());
/*      */     }
/*      */     
/* 1756 */     if (p_setOptionValueOF_1_ == Options.CUSTOM_COLORS)
/*      */     {
/* 1758 */       this.ofCustomColors = (!this.ofCustomColors);
/* 1759 */       CustomColorizer.update();
/* 1760 */       this.mc.renderGlobal.loadRenderers();
/*      */     }
/*      */     
/* 1763 */     if (p_setOptionValueOF_1_ == Options.CUSTOM_SKY)
/*      */     {
/* 1765 */       this.ofCustomSky = (!this.ofCustomSky);
/* 1766 */       CustomSky.update();
/*      */     }
/*      */     
/* 1769 */     if (p_setOptionValueOF_1_ == Options.SHOW_CAPES)
/*      */     {
/* 1771 */       this.ofShowCapes = (!this.ofShowCapes);
/*      */     }
/*      */     
/* 1774 */     if (p_setOptionValueOF_1_ == Options.NATURAL_TEXTURES)
/*      */     {
/* 1776 */       this.ofNaturalTextures = (!this.ofNaturalTextures);
/* 1777 */       optfine.NaturalTextures.update();
/* 1778 */       this.mc.renderGlobal.loadRenderers();
/*      */     }
/*      */     
/* 1781 */     if (p_setOptionValueOF_1_ == Options.FAST_MATH)
/*      */     {
/* 1783 */       this.ofFastMath = (!this.ofFastMath);
/* 1784 */       MathHelper.fastMath = this.ofFastMath;
/*      */     }
/*      */     
/* 1787 */     if (p_setOptionValueOF_1_ == Options.FAST_RENDER)
/*      */     {
/* 1789 */       this.ofFastRender = (!this.ofFastRender);
/* 1790 */       Config.updateFramebufferSize();
/*      */     }
/*      */     
/* 1793 */     if (p_setOptionValueOF_1_ == Options.TRANSLUCENT_BLOCKS)
/*      */     {
/* 1795 */       if (this.ofTranslucentBlocks == 0)
/*      */       {
/* 1797 */         this.ofTranslucentBlocks = 1;
/*      */       }
/* 1799 */       else if (this.ofTranslucentBlocks == 1)
/*      */       {
/* 1801 */         this.ofTranslucentBlocks = 2;
/*      */       }
/* 1803 */       else if (this.ofTranslucentBlocks == 2)
/*      */       {
/* 1805 */         this.ofTranslucentBlocks = 0;
/*      */       }
/*      */       else
/*      */       {
/* 1809 */         this.ofTranslucentBlocks = 0;
/*      */       }
/*      */       
/* 1812 */       this.mc.renderGlobal.loadRenderers();
/*      */     }
/*      */     
/* 1815 */     if (p_setOptionValueOF_1_ == Options.LAZY_CHUNK_LOADING)
/*      */     {
/* 1817 */       this.ofLazyChunkLoading = (!this.ofLazyChunkLoading);
/* 1818 */       Config.updateAvailableProcessors();
/*      */       
/* 1820 */       if (!Config.isSingleProcessor())
/*      */       {
/* 1822 */         this.ofLazyChunkLoading = false;
/*      */       }
/*      */       
/* 1825 */       this.mc.renderGlobal.loadRenderers();
/*      */     }
/*      */     
/* 1828 */     if (p_setOptionValueOF_1_ == Options.FULLSCREEN_MODE)
/*      */     {
/* 1830 */       List list = Arrays.asList(Config.getFullscreenModes());
/*      */       
/* 1832 */       if (this.ofFullscreenMode.equals("Default"))
/*      */       {
/* 1834 */         this.ofFullscreenMode = ((String)list.get(0));
/*      */       }
/*      */       else
/*      */       {
/* 1838 */         int i = list.indexOf(this.ofFullscreenMode);
/*      */         
/* 1840 */         if (i < 0)
/*      */         {
/* 1842 */           this.ofFullscreenMode = "Default";
/*      */         }
/*      */         else
/*      */         {
/* 1846 */           i++;
/*      */           
/* 1848 */           if (i >= list.size())
/*      */           {
/* 1850 */             this.ofFullscreenMode = "Default";
/*      */           }
/*      */           else
/*      */           {
/* 1854 */             this.ofFullscreenMode = ((String)list.get(i));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1860 */     if (p_setOptionValueOF_1_ == Options.HELD_ITEM_TOOLTIPS)
/*      */     {
/* 1862 */       this.heldItemTooltips = (!this.heldItemTooltips);
/*      */     }
/*      */   }
/*      */   
/*      */   private String getKeyBindingOF(Options p_getKeyBindingOF_1_)
/*      */   {
/* 1868 */     String s = I18n.format(p_getKeyBindingOF_1_.getEnumString(), new Object[0]) + ": ";
/*      */     
/* 1870 */     if (s == null)
/*      */     {
/* 1872 */       s = p_getKeyBindingOF_1_.getEnumString();
/*      */     }
/*      */     
/* 1875 */     if (p_getKeyBindingOF_1_ == Options.RENDER_DISTANCE)
/*      */     {
/* 1877 */       int k = (int)getOptionFloatValue(p_getKeyBindingOF_1_);
/* 1878 */       String s1 = "Tiny";
/* 1879 */       int i = 2;
/*      */       
/* 1881 */       if (k >= 4)
/*      */       {
/* 1883 */         s1 = "Short";
/* 1884 */         i = 4;
/*      */       }
/*      */       
/* 1887 */       if (k >= 8)
/*      */       {
/* 1889 */         s1 = "Normal";
/* 1890 */         i = 8;
/*      */       }
/*      */       
/* 1893 */       if (k >= 16)
/*      */       {
/* 1895 */         s1 = "Far";
/* 1896 */         i = 16;
/*      */       }
/*      */       
/* 1899 */       if (k >= 32)
/*      */       {
/* 1901 */         s1 = "Extreme";
/* 1902 */         i = 32;
/*      */       }
/*      */       
/* 1905 */       int j = this.renderDistanceChunks - i;
/* 1906 */       String s2 = s1;
/*      */       
/* 1908 */       if (j > 0)
/*      */       {
/* 1910 */         s2 = s1 + "+";
/*      */       }
/*      */       
/* 1913 */       return s + k + " " + s2;
/*      */     }
/* 1915 */     if (p_getKeyBindingOF_1_ == Options.FOG_FANCY)
/*      */     {
/* 1917 */       switch (this.ofFogType)
/*      */       {
/*      */       case 1: 
/* 1920 */         return s + "Fast";
/*      */       
/*      */       case 2: 
/* 1923 */         return s + "Fancy";
/*      */       
/*      */       case 3: 
/* 1926 */         return s + "OFF";
/*      */       }
/*      */       
/* 1929 */       return s + "OFF";
/*      */     }
/*      */     
/* 1932 */     if (p_getKeyBindingOF_1_ == Options.FOG_START)
/*      */     {
/* 1934 */       return s + this.ofFogStart;
/*      */     }
/* 1936 */     if (p_getKeyBindingOF_1_ == Options.MIPMAP_TYPE)
/*      */     {
/* 1938 */       switch (this.ofMipmapType)
/*      */       {
/*      */       case 0: 
/* 1941 */         return s + "Nearest";
/*      */       
/*      */       case 1: 
/* 1944 */         return s + "Linear";
/*      */       
/*      */       case 2: 
/* 1947 */         return s + "Bilinear";
/*      */       
/*      */       case 3: 
/* 1950 */         return s + "Trilinear";
/*      */       }
/*      */       
/* 1953 */       return s + "Nearest";
/*      */     }
/*      */     
/* 1956 */     if (p_getKeyBindingOF_1_ == Options.SMOOTH_FPS)
/*      */     {
/* 1958 */       return s + "OFF";
/*      */     }
/* 1960 */     if (p_getKeyBindingOF_1_ == Options.SMOOTH_WORLD)
/*      */     {
/* 1962 */       return s + "OFF";
/*      */     }
/* 1964 */     if (p_getKeyBindingOF_1_ == Options.CLOUDS)
/*      */     {
/* 1966 */       switch (this.ofClouds)
/*      */       {
/*      */       case 1: 
/* 1969 */         return s + "Fast";
/*      */       
/*      */       case 2: 
/* 1972 */         return s + "Fancy";
/*      */       
/*      */       case 3: 
/* 1975 */         return s + "OFF";
/*      */       }
/*      */       
/* 1978 */       return s + "Default";
/*      */     }
/*      */     
/* 1981 */     if (p_getKeyBindingOF_1_ == Options.TREES)
/*      */     {
/* 1983 */       switch (this.ofTrees)
/*      */       {
/*      */       case 1: 
/* 1986 */         return s + "Fast";
/*      */       
/*      */       case 2: 
/* 1989 */         return s + "Fancy";
/*      */       }
/*      */       
/* 1992 */       return s + "Default";
/*      */     }
/*      */     
/* 1995 */     if (p_getKeyBindingOF_1_ == Options.DROPPED_ITEMS)
/*      */     {
/* 1997 */       switch (this.ofDroppedItems)
/*      */       {
/*      */       case 1: 
/* 2000 */         return s + "Fast";
/*      */       
/*      */       case 2: 
/* 2003 */         return s + "Fancy";
/*      */       }
/*      */       
/* 2006 */       return s + "Default";
/*      */     }
/*      */     
/* 2009 */     if (p_getKeyBindingOF_1_ == Options.RAIN)
/*      */     {
/* 2011 */       switch (this.ofRain)
/*      */       {
/*      */       case 1: 
/* 2014 */         return s + "Fast";
/*      */       
/*      */       case 2: 
/* 2017 */         return s + "Fancy";
/*      */       
/*      */       case 3: 
/* 2020 */         return s + "OFF";
/*      */       }
/*      */       
/* 2023 */       return s + "Default";
/*      */     }
/*      */     
/* 2026 */     if (p_getKeyBindingOF_1_ == Options.ANIMATED_WATER)
/*      */     {
/* 2028 */       switch (this.ofAnimatedWater)
/*      */       {
/*      */       case 1: 
/* 2031 */         return s + "Dynamic";
/*      */       
/*      */       case 2: 
/* 2034 */         return s + "OFF";
/*      */       }
/*      */       
/* 2037 */       return s + "ON";
/*      */     }
/*      */     
/* 2040 */     if (p_getKeyBindingOF_1_ == Options.ANIMATED_LAVA)
/*      */     {
/* 2042 */       switch (this.ofAnimatedLava)
/*      */       {
/*      */       case 1: 
/* 2045 */         return s + "Dynamic";
/*      */       
/*      */       case 2: 
/* 2048 */         return s + "OFF";
/*      */       }
/*      */       
/* 2051 */       return s + "ON";
/*      */     }
/*      */     
/* 2054 */     if (p_getKeyBindingOF_1_ == Options.ANIMATED_FIRE)
/*      */     {
/* 2056 */       return s + "OFF";
/*      */     }
/* 2058 */     if (p_getKeyBindingOF_1_ == Options.ANIMATED_PORTAL)
/*      */     {
/* 2060 */       return s + "OFF";
/*      */     }
/* 2062 */     if (p_getKeyBindingOF_1_ == Options.ANIMATED_REDSTONE)
/*      */     {
/* 2064 */       return s + "OFF";
/*      */     }
/* 2066 */     if (p_getKeyBindingOF_1_ == Options.ANIMATED_EXPLOSION)
/*      */     {
/* 2068 */       return s + "OFF";
/*      */     }
/* 2070 */     if (p_getKeyBindingOF_1_ == Options.ANIMATED_FLAME)
/*      */     {
/* 2072 */       return s + "OFF";
/*      */     }
/* 2074 */     if (p_getKeyBindingOF_1_ == Options.ANIMATED_SMOKE)
/*      */     {
/* 2076 */       return s + "OFF";
/*      */     }
/* 2078 */     if (p_getKeyBindingOF_1_ == Options.VOID_PARTICLES)
/*      */     {
/* 2080 */       return s + "OFF";
/*      */     }
/* 2082 */     if (p_getKeyBindingOF_1_ == Options.WATER_PARTICLES)
/*      */     {
/* 2084 */       return s + "OFF";
/*      */     }
/* 2086 */     if (p_getKeyBindingOF_1_ == Options.PORTAL_PARTICLES)
/*      */     {
/* 2088 */       return s + "OFF";
/*      */     }
/* 2090 */     if (p_getKeyBindingOF_1_ == Options.POTION_PARTICLES)
/*      */     {
/* 2092 */       return s + "OFF";
/*      */     }
/* 2094 */     if (p_getKeyBindingOF_1_ == Options.FIREWORK_PARTICLES)
/*      */     {
/* 2096 */       return s + "OFF";
/*      */     }
/* 2098 */     if (p_getKeyBindingOF_1_ == Options.DRIPPING_WATER_LAVA)
/*      */     {
/* 2100 */       return s + "OFF";
/*      */     }
/* 2102 */     if (p_getKeyBindingOF_1_ == Options.ANIMATED_TERRAIN)
/*      */     {
/* 2104 */       return s + "OFF";
/*      */     }
/* 2106 */     if (p_getKeyBindingOF_1_ == Options.ANIMATED_TEXTURES)
/*      */     {
/* 2108 */       return s + "OFF";
/*      */     }
/* 2110 */     if (p_getKeyBindingOF_1_ == Options.RAIN_SPLASH)
/*      */     {
/* 2112 */       return s + "OFF";
/*      */     }
/* 2114 */     if (p_getKeyBindingOF_1_ == Options.LAGOMETER)
/*      */     {
/* 2116 */       return s + "OFF";
/*      */     }
/* 2118 */     if (p_getKeyBindingOF_1_ == Options.SHOW_FPS)
/*      */     {
/* 2120 */       return s + "OFF";
/*      */     }
/* 2122 */     if (p_getKeyBindingOF_1_ == Options.AUTOSAVE_TICKS)
/*      */     {
/* 2124 */       return s + "30min";
/*      */     }
/* 2126 */     if (p_getKeyBindingOF_1_ == Options.BETTER_GRASS)
/*      */     {
/* 2128 */       switch (this.ofBetterGrass)
/*      */       {
/*      */       case 1: 
/* 2131 */         return s + "Fast";
/*      */       
/*      */       case 2: 
/* 2134 */         return s + "Fancy";
/*      */       }
/*      */       
/* 2137 */       return s + "OFF";
/*      */     }
/*      */     
/* 2140 */     if (p_getKeyBindingOF_1_ == Options.CONNECTED_TEXTURES)
/*      */     {
/* 2142 */       switch (this.ofConnectedTextures)
/*      */       {
/*      */       case 1: 
/* 2145 */         return s + "Fast";
/*      */       
/*      */       case 2: 
/* 2148 */         return s + "Fancy";
/*      */       }
/*      */       
/* 2151 */       return s + "OFF";
/*      */     }
/*      */     
/* 2154 */     if (p_getKeyBindingOF_1_ == Options.WEATHER)
/*      */     {
/* 2156 */       return s + "OFF";
/*      */     }
/* 2158 */     if (p_getKeyBindingOF_1_ == Options.SKY)
/*      */     {
/* 2160 */       return s + "OFF";
/*      */     }
/* 2162 */     if (p_getKeyBindingOF_1_ == Options.STARS)
/*      */     {
/* 2164 */       return s + "OFF";
/*      */     }
/* 2166 */     if (p_getKeyBindingOF_1_ == Options.SUN_MOON)
/*      */     {
/* 2168 */       return s + "OFF";
/*      */     }
/* 2170 */     if (p_getKeyBindingOF_1_ == Options.VIGNETTE)
/*      */     {
/* 2172 */       switch (this.ofVignette)
/*      */       {
/*      */       case 1: 
/* 2175 */         return s + "Fast";
/*      */       
/*      */       case 2: 
/* 2178 */         return s + "Fancy";
/*      */       }
/*      */       
/* 2181 */       return s + "Default";
/*      */     }
/*      */     
/* 2184 */     if (p_getKeyBindingOF_1_ == Options.CHUNK_UPDATES)
/*      */     {
/* 2186 */       return s + this.ofChunkUpdates;
/*      */     }
/* 2188 */     if (p_getKeyBindingOF_1_ == Options.CHUNK_LOADING)
/*      */     {
/* 2190 */       return s + "Default";
/*      */     }
/* 2192 */     if (p_getKeyBindingOF_1_ == Options.CHUNK_UPDATES_DYNAMIC)
/*      */     {
/* 2194 */       return s + "OFF";
/*      */     }
/* 2196 */     if (p_getKeyBindingOF_1_ == Options.TIME)
/*      */     {
/* 2198 */       return s + "Default";
/*      */     }
/* 2200 */     if (p_getKeyBindingOF_1_ == Options.CLEAR_WATER)
/*      */     {
/* 2202 */       return s + "OFF";
/*      */     }
/* 2204 */     if (p_getKeyBindingOF_1_ == Options.AA_LEVEL)
/*      */     {
/* 2206 */       String s3 = "";
/*      */       
/* 2208 */       if (this.ofAaLevel != Config.getAntialiasingLevel())
/*      */       {
/* 2210 */         s3 = " (restart)";
/*      */       }
/*      */       
/* 2213 */       return s + this.ofAaLevel + s3;
/*      */     }
/* 2215 */     if (p_getKeyBindingOF_1_ == Options.AF_LEVEL)
/*      */     {
/* 2217 */       return s + this.ofAfLevel;
/*      */     }
/* 2219 */     if (p_getKeyBindingOF_1_ == Options.PROFILER)
/*      */     {
/* 2221 */       return s + "OFF";
/*      */     }
/* 2223 */     if (p_getKeyBindingOF_1_ == Options.BETTER_SNOW)
/*      */     {
/* 2225 */       return s + "OFF";
/*      */     }
/* 2227 */     if (p_getKeyBindingOF_1_ == Options.SWAMP_COLORS)
/*      */     {
/* 2229 */       return s + "OFF";
/*      */     }
/* 2231 */     if (p_getKeyBindingOF_1_ == Options.RANDOM_MOBS)
/*      */     {
/* 2233 */       return s + "OFF";
/*      */     }
/* 2235 */     if (p_getKeyBindingOF_1_ == Options.SMOOTH_BIOMES)
/*      */     {
/* 2237 */       return s + "OFF";
/*      */     }
/* 2239 */     if (p_getKeyBindingOF_1_ == Options.CUSTOM_FONTS)
/*      */     {
/* 2241 */       return s + "OFF";
/*      */     }
/* 2243 */     if (p_getKeyBindingOF_1_ == Options.CUSTOM_COLORS)
/*      */     {
/* 2245 */       return s + "OFF";
/*      */     }
/* 2247 */     if (p_getKeyBindingOF_1_ == Options.CUSTOM_SKY)
/*      */     {
/* 2249 */       return s + "OFF";
/*      */     }
/* 2251 */     if (p_getKeyBindingOF_1_ == Options.SHOW_CAPES)
/*      */     {
/* 2253 */       return s + "OFF";
/*      */     }
/* 2255 */     if (p_getKeyBindingOF_1_ == Options.NATURAL_TEXTURES)
/*      */     {
/* 2257 */       return s + "OFF";
/*      */     }
/* 2259 */     if (p_getKeyBindingOF_1_ == Options.FAST_MATH)
/*      */     {
/* 2261 */       return s + "OFF";
/*      */     }
/* 2263 */     if (p_getKeyBindingOF_1_ == Options.FAST_RENDER)
/*      */     {
/* 2265 */       return s + "OFF";
/*      */     }
/* 2267 */     if (p_getKeyBindingOF_1_ == Options.TRANSLUCENT_BLOCKS)
/*      */     {
/* 2269 */       return s + "Default";
/*      */     }
/* 2271 */     if (p_getKeyBindingOF_1_ == Options.LAZY_CHUNK_LOADING)
/*      */     {
/* 2273 */       return s + "OFF";
/*      */     }
/* 2275 */     if (p_getKeyBindingOF_1_ == Options.FULLSCREEN_MODE)
/*      */     {
/* 2277 */       return s + this.ofFullscreenMode;
/*      */     }
/* 2279 */     if (p_getKeyBindingOF_1_ == Options.HELD_ITEM_TOOLTIPS)
/*      */     {
/* 2281 */       return s + "OFF";
/*      */     }
/* 2283 */     if (p_getKeyBindingOF_1_ == Options.FRAMERATE_LIMIT)
/*      */     {
/* 2285 */       float f = getOptionFloatValue(p_getKeyBindingOF_1_);
/* 2286 */       return s + (int)f + " fps";
/*      */     }
/*      */     
/*      */ 
/* 2290 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public void loadOfOptions()
/*      */   {
/*      */     try
/*      */     {
/* 2298 */       File file1 = this.optionsFileOF;
/*      */       
/* 2300 */       if (!file1.exists())
/*      */       {
/* 2302 */         file1 = this.optionsFile;
/*      */       }
/*      */       
/* 2305 */       if (!file1.exists())
/*      */       {
/* 2307 */         return;
/*      */       }
/*      */       
/* 2310 */       BufferedReader bufferedreader = new BufferedReader(new FileReader(file1));
/* 2311 */       String s = "";
/*      */       
/* 2313 */       while ((s = bufferedreader.readLine()) != null)
/*      */       {
/*      */         try
/*      */         {
/* 2317 */           String[] astring = s.split(":");
/*      */           
/* 2319 */           if ((astring[0].equals("ofRenderDistanceChunks")) && (astring.length >= 2))
/*      */           {
/* 2321 */             this.renderDistanceChunks = Integer.valueOf(astring[1]).intValue();
/* 2322 */             this.renderDistanceChunks = Config.limit(this.renderDistanceChunks, 2, 32);
/*      */           }
/*      */           
/* 2325 */           if ((astring[0].equals("ofFogType")) && (astring.length >= 2))
/*      */           {
/* 2327 */             this.ofFogType = Integer.valueOf(astring[1]).intValue();
/* 2328 */             this.ofFogType = Config.limit(this.ofFogType, 1, 3);
/*      */           }
/*      */           
/* 2331 */           if ((astring[0].equals("ofFogStart")) && (astring.length >= 2))
/*      */           {
/* 2333 */             this.ofFogStart = Float.valueOf(astring[1]).floatValue();
/*      */             
/* 2335 */             if (this.ofFogStart < 0.2F)
/*      */             {
/* 2337 */               this.ofFogStart = 0.2F;
/*      */             }
/*      */             
/* 2340 */             if (this.ofFogStart > 0.81F)
/*      */             {
/* 2342 */               this.ofFogStart = 0.8F;
/*      */             }
/*      */           }
/*      */           
/* 2346 */           if ((astring[0].equals("ofMipmapType")) && (astring.length >= 2))
/*      */           {
/* 2348 */             this.ofMipmapType = Integer.valueOf(astring[1]).intValue();
/* 2349 */             this.ofMipmapType = Config.limit(this.ofMipmapType, 0, 3);
/*      */           }
/*      */           
/* 2352 */           if ((astring[0].equals("ofOcclusionFancy")) && (astring.length >= 2))
/*      */           {
/* 2354 */             this.ofOcclusionFancy = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2357 */           if ((astring[0].equals("ofSmoothFps")) && (astring.length >= 2))
/*      */           {
/* 2359 */             this.ofSmoothFps = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2362 */           if ((astring[0].equals("ofSmoothWorld")) && (astring.length >= 2))
/*      */           {
/* 2364 */             this.ofSmoothWorld = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2367 */           if ((astring[0].equals("ofAoLevel")) && (astring.length >= 2))
/*      */           {
/* 2369 */             this.ofAoLevel = Float.valueOf(astring[1]).floatValue();
/* 2370 */             this.ofAoLevel = Config.limit(this.ofAoLevel, 0.0F, 1.0F);
/*      */           }
/*      */           
/* 2373 */           if ((astring[0].equals("ofClouds")) && (astring.length >= 2))
/*      */           {
/* 2375 */             this.ofClouds = Integer.valueOf(astring[1]).intValue();
/* 2376 */             this.ofClouds = Config.limit(this.ofClouds, 0, 3);
/* 2377 */             updateRenderClouds();
/*      */           }
/*      */           
/* 2380 */           if ((astring[0].equals("ofCloudsHeight")) && (astring.length >= 2))
/*      */           {
/* 2382 */             this.ofCloudsHeight = Float.valueOf(astring[1]).floatValue();
/* 2383 */             this.ofCloudsHeight = Config.limit(this.ofCloudsHeight, 0.0F, 1.0F);
/*      */           }
/*      */           
/* 2386 */           if ((astring[0].equals("ofTrees")) && (astring.length >= 2))
/*      */           {
/* 2388 */             this.ofTrees = Integer.valueOf(astring[1]).intValue();
/* 2389 */             this.ofTrees = Config.limit(this.ofTrees, 0, 2);
/*      */           }
/*      */           
/* 2392 */           if ((astring[0].equals("ofDroppedItems")) && (astring.length >= 2))
/*      */           {
/* 2394 */             this.ofDroppedItems = Integer.valueOf(astring[1]).intValue();
/* 2395 */             this.ofDroppedItems = Config.limit(this.ofDroppedItems, 0, 2);
/*      */           }
/*      */           
/* 2398 */           if ((astring[0].equals("ofRain")) && (astring.length >= 2))
/*      */           {
/* 2400 */             this.ofRain = Integer.valueOf(astring[1]).intValue();
/* 2401 */             this.ofRain = Config.limit(this.ofRain, 0, 3);
/*      */           }
/*      */           
/* 2404 */           if ((astring[0].equals("ofAnimatedWater")) && (astring.length >= 2))
/*      */           {
/* 2406 */             this.ofAnimatedWater = Integer.valueOf(astring[1]).intValue();
/* 2407 */             this.ofAnimatedWater = Config.limit(this.ofAnimatedWater, 0, 2);
/*      */           }
/*      */           
/* 2410 */           if ((astring[0].equals("ofAnimatedLava")) && (astring.length >= 2))
/*      */           {
/* 2412 */             this.ofAnimatedLava = Integer.valueOf(astring[1]).intValue();
/* 2413 */             this.ofAnimatedLava = Config.limit(this.ofAnimatedLava, 0, 2);
/*      */           }
/*      */           
/* 2416 */           if ((astring[0].equals("ofAnimatedFire")) && (astring.length >= 2))
/*      */           {
/* 2418 */             this.ofAnimatedFire = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2421 */           if ((astring[0].equals("ofAnimatedPortal")) && (astring.length >= 2))
/*      */           {
/* 2423 */             this.ofAnimatedPortal = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2426 */           if ((astring[0].equals("ofAnimatedRedstone")) && (astring.length >= 2))
/*      */           {
/* 2428 */             this.ofAnimatedRedstone = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2431 */           if ((astring[0].equals("ofAnimatedExplosion")) && (astring.length >= 2))
/*      */           {
/* 2433 */             this.ofAnimatedExplosion = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2436 */           if ((astring[0].equals("ofAnimatedFlame")) && (astring.length >= 2))
/*      */           {
/* 2438 */             this.ofAnimatedFlame = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2441 */           if ((astring[0].equals("ofAnimatedSmoke")) && (astring.length >= 2))
/*      */           {
/* 2443 */             this.ofAnimatedSmoke = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2446 */           if ((astring[0].equals("ofVoidParticles")) && (astring.length >= 2))
/*      */           {
/* 2448 */             this.ofVoidParticles = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2451 */           if ((astring[0].equals("ofWaterParticles")) && (astring.length >= 2))
/*      */           {
/* 2453 */             this.ofWaterParticles = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2456 */           if ((astring[0].equals("ofPortalParticles")) && (astring.length >= 2))
/*      */           {
/* 2458 */             this.ofPortalParticles = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2461 */           if ((astring[0].equals("ofPotionParticles")) && (astring.length >= 2))
/*      */           {
/* 2463 */             this.ofPotionParticles = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2466 */           if ((astring[0].equals("ofFireworkParticles")) && (astring.length >= 2))
/*      */           {
/* 2468 */             this.ofFireworkParticles = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2471 */           if ((astring[0].equals("ofDrippingWaterLava")) && (astring.length >= 2))
/*      */           {
/* 2473 */             this.ofDrippingWaterLava = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2476 */           if ((astring[0].equals("ofAnimatedTerrain")) && (astring.length >= 2))
/*      */           {
/* 2478 */             this.ofAnimatedTerrain = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2481 */           if ((astring[0].equals("ofAnimatedTextures")) && (astring.length >= 2))
/*      */           {
/* 2483 */             this.ofAnimatedTextures = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2486 */           if ((astring[0].equals("ofRainSplash")) && (astring.length >= 2))
/*      */           {
/* 2488 */             this.ofRainSplash = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2491 */           if ((astring[0].equals("ofLagometer")) && (astring.length >= 2))
/*      */           {
/* 2493 */             this.ofLagometer = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2496 */           if ((astring[0].equals("ofShowFps")) && (astring.length >= 2))
/*      */           {
/* 2498 */             this.ofShowFps = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2501 */           if ((astring[0].equals("ofAutoSaveTicks")) && (astring.length >= 2))
/*      */           {
/* 2503 */             this.ofAutoSaveTicks = Integer.valueOf(astring[1]).intValue();
/* 2504 */             this.ofAutoSaveTicks = Config.limit(this.ofAutoSaveTicks, 40, 40000);
/*      */           }
/*      */           
/* 2507 */           if ((astring[0].equals("ofBetterGrass")) && (astring.length >= 2))
/*      */           {
/* 2509 */             this.ofBetterGrass = Integer.valueOf(astring[1]).intValue();
/* 2510 */             this.ofBetterGrass = Config.limit(this.ofBetterGrass, 1, 3);
/*      */           }
/*      */           
/* 2513 */           if ((astring[0].equals("ofConnectedTextures")) && (astring.length >= 2))
/*      */           {
/* 2515 */             this.ofConnectedTextures = Integer.valueOf(astring[1]).intValue();
/* 2516 */             this.ofConnectedTextures = Config.limit(this.ofConnectedTextures, 1, 3);
/*      */           }
/*      */           
/* 2519 */           if ((astring[0].equals("ofWeather")) && (astring.length >= 2))
/*      */           {
/* 2521 */             this.ofWeather = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2524 */           if ((astring[0].equals("ofSky")) && (astring.length >= 2))
/*      */           {
/* 2526 */             this.ofSky = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2529 */           if ((astring[0].equals("ofStars")) && (astring.length >= 2))
/*      */           {
/* 2531 */             this.ofStars = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2534 */           if ((astring[0].equals("ofSunMoon")) && (astring.length >= 2))
/*      */           {
/* 2536 */             this.ofSunMoon = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2539 */           if ((astring[0].equals("ofVignette")) && (astring.length >= 2))
/*      */           {
/* 2541 */             this.ofVignette = Integer.valueOf(astring[1]).intValue();
/* 2542 */             this.ofVignette = Config.limit(this.ofVignette, 0, 2);
/*      */           }
/*      */           
/* 2545 */           if ((astring[0].equals("ofChunkUpdates")) && (astring.length >= 2))
/*      */           {
/* 2547 */             this.ofChunkUpdates = Integer.valueOf(astring[1]).intValue();
/* 2548 */             this.ofChunkUpdates = Config.limit(this.ofChunkUpdates, 1, 5);
/*      */           }
/*      */           
/* 2551 */           if ((astring[0].equals("ofChunkLoading")) && (astring.length >= 2))
/*      */           {
/* 2553 */             this.ofChunkLoading = Integer.valueOf(astring[1]).intValue();
/* 2554 */             this.ofChunkLoading = Config.limit(this.ofChunkLoading, 0, 2);
/* 2555 */             updateChunkLoading();
/*      */           }
/*      */           
/* 2558 */           if ((astring[0].equals("ofChunkUpdatesDynamic")) && (astring.length >= 2))
/*      */           {
/* 2560 */             this.ofChunkUpdatesDynamic = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2563 */           if ((astring[0].equals("ofTime")) && (astring.length >= 2))
/*      */           {
/* 2565 */             this.ofTime = Integer.valueOf(astring[1]).intValue();
/* 2566 */             this.ofTime = Config.limit(this.ofTime, 0, 3);
/*      */           }
/*      */           
/* 2569 */           if ((astring[0].equals("ofClearWater")) && (astring.length >= 2))
/*      */           {
/* 2571 */             this.ofClearWater = Boolean.valueOf(astring[1]).booleanValue();
/* 2572 */             updateWaterOpacity();
/*      */           }
/*      */           
/* 2575 */           if ((astring[0].equals("ofAaLevel")) && (astring.length >= 2))
/*      */           {
/* 2577 */             this.ofAaLevel = Integer.valueOf(astring[1]).intValue();
/* 2578 */             this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
/*      */           }
/*      */           
/* 2581 */           if ((astring[0].equals("ofAfLevel")) && (astring.length >= 2))
/*      */           {
/* 2583 */             this.ofAfLevel = Integer.valueOf(astring[1]).intValue();
/* 2584 */             this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
/*      */           }
/*      */           
/* 2587 */           if ((astring[0].equals("ofProfiler")) && (astring.length >= 2))
/*      */           {
/* 2589 */             this.ofProfiler = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2592 */           if ((astring[0].equals("ofBetterSnow")) && (astring.length >= 2))
/*      */           {
/* 2594 */             this.ofBetterSnow = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2597 */           if ((astring[0].equals("ofSwampColors")) && (astring.length >= 2))
/*      */           {
/* 2599 */             this.ofSwampColors = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2602 */           if ((astring[0].equals("ofRandomMobs")) && (astring.length >= 2))
/*      */           {
/* 2604 */             this.ofRandomMobs = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2607 */           if ((astring[0].equals("ofSmoothBiomes")) && (astring.length >= 2))
/*      */           {
/* 2609 */             this.ofSmoothBiomes = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2612 */           if ((astring[0].equals("ofCustomFonts")) && (astring.length >= 2))
/*      */           {
/* 2614 */             this.ofCustomFonts = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2617 */           if ((astring[0].equals("ofCustomColors")) && (astring.length >= 2))
/*      */           {
/* 2619 */             this.ofCustomColors = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2622 */           if ((astring[0].equals("ofCustomSky")) && (astring.length >= 2))
/*      */           {
/* 2624 */             this.ofCustomSky = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2627 */           if ((astring[0].equals("ofShowCapes")) && (astring.length >= 2))
/*      */           {
/* 2629 */             this.ofShowCapes = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2632 */           if ((astring[0].equals("ofNaturalTextures")) && (astring.length >= 2))
/*      */           {
/* 2634 */             this.ofNaturalTextures = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2637 */           if ((astring[0].equals("ofLazyChunkLoading")) && (astring.length >= 2))
/*      */           {
/* 2639 */             this.ofLazyChunkLoading = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2642 */           if ((astring[0].equals("ofFullscreenMode")) && (astring.length >= 2))
/*      */           {
/* 2644 */             this.ofFullscreenMode = astring[1];
/*      */           }
/*      */           
/* 2647 */           if ((astring[0].equals("ofFastMath")) && (astring.length >= 2))
/*      */           {
/* 2649 */             this.ofFastMath = Boolean.valueOf(astring[1]).booleanValue();
/* 2650 */             MathHelper.fastMath = this.ofFastMath;
/*      */           }
/*      */           
/* 2653 */           if ((astring[0].equals("ofFastRender")) && (astring.length >= 2))
/*      */           {
/* 2655 */             this.ofFastRender = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2658 */           if ((astring[0].equals("ofTranslucentBlocks")) && (astring.length >= 2))
/*      */           {
/* 2660 */             this.ofTranslucentBlocks = Integer.valueOf(astring[1]).intValue();
/* 2661 */             this.ofTranslucentBlocks = Config.limit(this.ofTranslucentBlocks, 0, 2);
/*      */           }
/*      */           
/* 2664 */           if (astring[0].equals("key_" + this.ofKeyBindZoom.getKeyDescription()))
/*      */           {
/* 2666 */             this.ofKeyBindZoom.setKeyCode(Integer.parseInt(astring[1]));
/*      */           }
/*      */         }
/*      */         catch (Exception exception)
/*      */         {
/* 2671 */           Config.dbg("Skipping bad option: " + s);
/* 2672 */           exception.printStackTrace();
/*      */         }
/*      */       }
/*      */       
/* 2676 */       KeyBinding.resetKeyBindingArrayAndHash();
/* 2677 */       bufferedreader.close();
/*      */     }
/*      */     catch (Exception exception1)
/*      */     {
/* 2681 */       Config.warn("Failed to load options");
/* 2682 */       exception1.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   public void saveOfOptions()
/*      */   {
/*      */     try
/*      */     {
/* 2690 */       PrintWriter printwriter = new PrintWriter(new FileWriter(this.optionsFileOF));
/* 2691 */       printwriter.println("ofRenderDistanceChunks:" + this.renderDistanceChunks);
/* 2692 */       printwriter.println("ofFogType:" + this.ofFogType);
/* 2693 */       printwriter.println("ofFogStart:" + this.ofFogStart);
/* 2694 */       printwriter.println("ofMipmapType:" + this.ofMipmapType);
/* 2695 */       printwriter.println("ofOcclusionFancy:" + this.ofOcclusionFancy);
/* 2696 */       printwriter.println("ofSmoothFps:" + this.ofSmoothFps);
/* 2697 */       printwriter.println("ofSmoothWorld:" + this.ofSmoothWorld);
/* 2698 */       printwriter.println("ofAoLevel:" + this.ofAoLevel);
/* 2699 */       printwriter.println("ofClouds:" + this.ofClouds);
/* 2700 */       printwriter.println("ofCloudsHeight:" + this.ofCloudsHeight);
/* 2701 */       printwriter.println("ofTrees:" + this.ofTrees);
/* 2702 */       printwriter.println("ofDroppedItems:" + this.ofDroppedItems);
/* 2703 */       printwriter.println("ofRain:" + this.ofRain);
/* 2704 */       printwriter.println("ofAnimatedWater:" + this.ofAnimatedWater);
/* 2705 */       printwriter.println("ofAnimatedLava:" + this.ofAnimatedLava);
/* 2706 */       printwriter.println("ofAnimatedFire:" + this.ofAnimatedFire);
/* 2707 */       printwriter.println("ofAnimatedPortal:" + this.ofAnimatedPortal);
/* 2708 */       printwriter.println("ofAnimatedRedstone:" + this.ofAnimatedRedstone);
/* 2709 */       printwriter.println("ofAnimatedExplosion:" + this.ofAnimatedExplosion);
/* 2710 */       printwriter.println("ofAnimatedFlame:" + this.ofAnimatedFlame);
/* 2711 */       printwriter.println("ofAnimatedSmoke:" + this.ofAnimatedSmoke);
/* 2712 */       printwriter.println("ofVoidParticles:" + this.ofVoidParticles);
/* 2713 */       printwriter.println("ofWaterParticles:" + this.ofWaterParticles);
/* 2714 */       printwriter.println("ofPortalParticles:" + this.ofPortalParticles);
/* 2715 */       printwriter.println("ofPotionParticles:" + this.ofPotionParticles);
/* 2716 */       printwriter.println("ofFireworkParticles:" + this.ofFireworkParticles);
/* 2717 */       printwriter.println("ofDrippingWaterLava:" + this.ofDrippingWaterLava);
/* 2718 */       printwriter.println("ofAnimatedTerrain:" + this.ofAnimatedTerrain);
/* 2719 */       printwriter.println("ofAnimatedTextures:" + this.ofAnimatedTextures);
/* 2720 */       printwriter.println("ofRainSplash:" + this.ofRainSplash);
/* 2721 */       printwriter.println("ofLagometer:" + this.ofLagometer);
/* 2722 */       printwriter.println("ofShowFps:" + this.ofShowFps);
/* 2723 */       printwriter.println("ofAutoSaveTicks:" + this.ofAutoSaveTicks);
/* 2724 */       printwriter.println("ofBetterGrass:" + this.ofBetterGrass);
/* 2725 */       printwriter.println("ofConnectedTextures:" + this.ofConnectedTextures);
/* 2726 */       printwriter.println("ofWeather:" + this.ofWeather);
/* 2727 */       printwriter.println("ofSky:" + this.ofSky);
/* 2728 */       printwriter.println("ofStars:" + this.ofStars);
/* 2729 */       printwriter.println("ofSunMoon:" + this.ofSunMoon);
/* 2730 */       printwriter.println("ofVignette:" + this.ofVignette);
/* 2731 */       printwriter.println("ofChunkUpdates:" + this.ofChunkUpdates);
/* 2732 */       printwriter.println("ofChunkLoading:" + this.ofChunkLoading);
/* 2733 */       printwriter.println("ofChunkUpdatesDynamic:" + this.ofChunkUpdatesDynamic);
/* 2734 */       printwriter.println("ofTime:" + this.ofTime);
/* 2735 */       printwriter.println("ofClearWater:" + this.ofClearWater);
/* 2736 */       printwriter.println("ofAaLevel:" + this.ofAaLevel);
/* 2737 */       printwriter.println("ofAfLevel:" + this.ofAfLevel);
/* 2738 */       printwriter.println("ofProfiler:" + this.ofProfiler);
/* 2739 */       printwriter.println("ofBetterSnow:" + this.ofBetterSnow);
/* 2740 */       printwriter.println("ofSwampColors:" + this.ofSwampColors);
/* 2741 */       printwriter.println("ofRandomMobs:" + this.ofRandomMobs);
/* 2742 */       printwriter.println("ofSmoothBiomes:" + this.ofSmoothBiomes);
/* 2743 */       printwriter.println("ofCustomFonts:" + this.ofCustomFonts);
/* 2744 */       printwriter.println("ofCustomColors:" + this.ofCustomColors);
/* 2745 */       printwriter.println("ofCustomSky:" + this.ofCustomSky);
/* 2746 */       printwriter.println("ofShowCapes:" + this.ofShowCapes);
/* 2747 */       printwriter.println("ofNaturalTextures:" + this.ofNaturalTextures);
/* 2748 */       printwriter.println("ofLazyChunkLoading:" + this.ofLazyChunkLoading);
/* 2749 */       printwriter.println("ofFullscreenMode:" + this.ofFullscreenMode);
/* 2750 */       printwriter.println("ofFastMath:" + this.ofFastMath);
/* 2751 */       printwriter.println("ofFastRender:" + this.ofFastRender);
/* 2752 */       printwriter.println("ofTranslucentBlocks:" + this.ofTranslucentBlocks);
/* 2753 */       printwriter.println("key_" + this.ofKeyBindZoom.getKeyDescription() + ":" + this.ofKeyBindZoom.getKeyCode());
/* 2754 */       printwriter.close();
/*      */     }
/*      */     catch (Exception exception)
/*      */     {
/* 2758 */       Config.warn("Failed to save options");
/* 2759 */       exception.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateRenderClouds()
/*      */   {
/* 2765 */     switch (this.ofClouds)
/*      */     {
/*      */     case 1: 
/* 2768 */       this.clouds = 1;
/* 2769 */       break;
/*      */     
/*      */     case 2: 
/* 2772 */       this.clouds = 2;
/* 2773 */       break;
/*      */     
/*      */     case 3: 
/* 2776 */       this.clouds = 0;
/* 2777 */       break;
/*      */     
/*      */     default: 
/* 2780 */       if (this.fancyGraphics)
/*      */       {
/* 2782 */         this.clouds = 2;
/*      */       }
/*      */       else
/*      */       {
/* 2786 */         this.clouds = 1;
/*      */       }
/*      */       break;
/*      */     }
/*      */   }
/*      */   
/*      */   public void resetSettings() {
/* 2793 */     this.renderDistanceChunks = 8;
/* 2794 */     this.viewBobbing = true;
/* 2795 */     this.anaglyph = false;
/* 2796 */     this.limitFramerate = ((int)Options.FRAMERATE_LIMIT.getValueMax());
/* 2797 */     this.enableVsync = false;
/* 2798 */     updateVSync();
/* 2799 */     this.mipmapLevels = 4;
/* 2800 */     this.fancyGraphics = true;
/* 2801 */     this.ambientOcclusion = 2;
/* 2802 */     this.clouds = 2;
/* 2803 */     this.fovSetting = 70.0F;
/* 2804 */     this.gammaSetting = 0.0F;
/* 2805 */     this.guiScale = 0;
/* 2806 */     this.particleSetting = 0;
/* 2807 */     this.heldItemTooltips = true;
/* 2808 */     this.useVbo = false;
/* 2809 */     this.allowBlockAlternatives = true;
/* 2810 */     this.forceUnicodeFont = false;
/* 2811 */     this.ofFogType = 1;
/* 2812 */     this.ofFogStart = 0.8F;
/* 2813 */     this.ofMipmapType = 0;
/* 2814 */     this.ofOcclusionFancy = false;
/* 2815 */     this.ofSmoothFps = false;
/* 2816 */     Config.updateAvailableProcessors();
/* 2817 */     this.ofSmoothWorld = Config.isSingleProcessor();
/* 2818 */     this.ofLazyChunkLoading = Config.isSingleProcessor();
/* 2819 */     this.ofFastMath = false;
/* 2820 */     this.ofFastRender = true;
/* 2821 */     this.ofTranslucentBlocks = 0;
/* 2822 */     this.ofAoLevel = 1.0F;
/* 2823 */     this.ofAaLevel = 0;
/* 2824 */     this.ofAfLevel = 1;
/* 2825 */     this.ofClouds = 0;
/* 2826 */     this.ofCloudsHeight = 0.0F;
/* 2827 */     this.ofTrees = 0;
/* 2828 */     this.ofRain = 0;
/* 2829 */     this.ofBetterGrass = 3;
/* 2830 */     this.ofAutoSaveTicks = 4000;
/* 2831 */     this.ofLagometer = false;
/* 2832 */     this.ofShowFps = false;
/* 2833 */     this.ofProfiler = false;
/* 2834 */     this.ofWeather = true;
/* 2835 */     this.ofSky = true;
/* 2836 */     this.ofStars = true;
/* 2837 */     this.ofSunMoon = true;
/* 2838 */     this.ofVignette = 0;
/* 2839 */     this.ofChunkUpdates = 1;
/* 2840 */     this.ofChunkLoading = 0;
/* 2841 */     this.ofChunkUpdatesDynamic = false;
/* 2842 */     this.ofTime = 0;
/* 2843 */     this.ofClearWater = false;
/* 2844 */     this.ofBetterSnow = false;
/* 2845 */     this.ofFullscreenMode = "Default";
/* 2846 */     this.ofSwampColors = true;
/* 2847 */     this.ofRandomMobs = true;
/* 2848 */     this.ofSmoothBiomes = true;
/* 2849 */     this.ofCustomFonts = true;
/* 2850 */     this.ofCustomColors = true;
/* 2851 */     this.ofCustomSky = true;
/* 2852 */     this.ofShowCapes = true;
/* 2853 */     this.ofConnectedTextures = 2;
/* 2854 */     this.ofNaturalTextures = false;
/* 2855 */     this.ofAnimatedWater = 0;
/* 2856 */     this.ofAnimatedLava = 0;
/* 2857 */     this.ofAnimatedFire = true;
/* 2858 */     this.ofAnimatedPortal = true;
/* 2859 */     this.ofAnimatedRedstone = true;
/* 2860 */     this.ofAnimatedExplosion = true;
/* 2861 */     this.ofAnimatedFlame = true;
/* 2862 */     this.ofAnimatedSmoke = true;
/* 2863 */     this.ofVoidParticles = true;
/* 2864 */     this.ofWaterParticles = true;
/* 2865 */     this.ofRainSplash = true;
/* 2866 */     this.ofPortalParticles = true;
/* 2867 */     this.ofPotionParticles = true;
/* 2868 */     this.ofFireworkParticles = true;
/* 2869 */     this.ofDrippingWaterLava = true;
/* 2870 */     this.ofAnimatedTerrain = true;
/* 2871 */     this.ofAnimatedTextures = true;
/* 2872 */     updateWaterOpacity();
/* 2873 */     this.mc.refreshResources();
/* 2874 */     saveOptions();
/*      */   }
/*      */   
/*      */   public void updateVSync()
/*      */   {
/* 2879 */     Display.setVSyncEnabled(this.enableVsync);
/*      */   }
/*      */   
/*      */   private void updateWaterOpacity()
/*      */   {
/* 2884 */     if ((this.mc.isIntegratedServerRunning()) && (this.mc.getIntegratedServer() != null))
/*      */     {
/* 2886 */       Config.waterOpacityChanged = true;
/*      */     }
/*      */     
/* 2889 */     ClearWater.updateWaterOpacity(this, this.mc.theWorld);
/*      */   }
/*      */   
/*      */   public void updateChunkLoading()
/*      */   {
/* 2894 */     if (this.mc.renderGlobal != null)
/*      */     {
/* 2896 */       this.mc.renderGlobal.loadRenderers();
/*      */     }
/*      */   }
/*      */   
/*      */   public void setAllAnimations(boolean p_setAllAnimations_1_)
/*      */   {
/* 2902 */     int i = p_setAllAnimations_1_ ? 0 : 2;
/* 2903 */     this.ofAnimatedWater = i;
/* 2904 */     this.ofAnimatedLava = i;
/* 2905 */     this.ofAnimatedFire = p_setAllAnimations_1_;
/* 2906 */     this.ofAnimatedPortal = p_setAllAnimations_1_;
/* 2907 */     this.ofAnimatedRedstone = p_setAllAnimations_1_;
/* 2908 */     this.ofAnimatedExplosion = p_setAllAnimations_1_;
/* 2909 */     this.ofAnimatedFlame = p_setAllAnimations_1_;
/* 2910 */     this.ofAnimatedSmoke = p_setAllAnimations_1_;
/* 2911 */     this.ofVoidParticles = p_setAllAnimations_1_;
/* 2912 */     this.ofWaterParticles = p_setAllAnimations_1_;
/* 2913 */     this.ofRainSplash = p_setAllAnimations_1_;
/* 2914 */     this.ofPortalParticles = p_setAllAnimations_1_;
/* 2915 */     this.ofPotionParticles = p_setAllAnimations_1_;
/* 2916 */     this.ofFireworkParticles = p_setAllAnimations_1_;
/* 2917 */     this.particleSetting = (p_setAllAnimations_1_ ? 0 : 2);
/* 2918 */     this.ofDrippingWaterLava = p_setAllAnimations_1_;
/* 2919 */     this.ofAnimatedTerrain = p_setAllAnimations_1_;
/* 2920 */     this.ofAnimatedTextures = p_setAllAnimations_1_;
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */   public static enum Options
/*      */   {
/* 3087 */     INVERT_MOUSE("INVERT_MOUSE", 0, "options.invertMouse", false, true), 
/* 3088 */     SENSITIVITY("SENSITIVITY", 1, "options.sensitivity", true, false), 
/* 3089 */     FOV("FOV", 2, "options.fov", true, false, 30.0F, 110.0F, 1.0F), 
/* 3090 */     GAMMA("GAMMA", 3, "options.gamma", true, false), 
/* 3091 */     SATURATION("SATURATION", 4, "options.saturation", true, false), 
/* 3092 */     RENDER_DISTANCE("RENDER_DISTANCE", 5, "options.renderDistance", true, false, 2.0F, 16.0F, 1.0F), 
/* 3093 */     VIEW_BOBBING("VIEW_BOBBING", 6, "options.viewBobbing", false, true), 
/* 3094 */     ANAGLYPH("ANAGLYPH", 7, "options.anaglyph", false, true), 
/* 3095 */     FRAMERATE_LIMIT("FRAMERATE_LIMIT", 8, "options.framerateLimit", true, false, 0.0F, 260.0F, 5.0F), 
/* 3096 */     FBO_ENABLE("FBO_ENABLE", 9, "options.fboEnable", false, true), 
/* 3097 */     RENDER_CLOUDS("RENDER_CLOUDS", 10, "options.renderClouds", false, false), 
/* 3098 */     GRAPHICS("GRAPHICS", 11, "options.graphics", false, false), 
/* 3099 */     AMBIENT_OCCLUSION("AMBIENT_OCCLUSION", 12, "options.ao", false, false), 
/* 3100 */     GUI_SCALE("GUI_SCALE", 13, "options.guiScale", false, false), 
/* 3101 */     PARTICLES("PARTICLES", 14, "options.particles", false, false), 
/* 3102 */     CHAT_VISIBILITY("CHAT_VISIBILITY", 15, "options.chat.visibility", false, false), 
/* 3103 */     CHAT_COLOR("CHAT_COLOR", 16, "options.chat.color", false, true), 
/* 3104 */     CHAT_LINKS("CHAT_LINKS", 17, "options.chat.links", false, true), 
/* 3105 */     CHAT_OPACITY("CHAT_OPACITY", 18, "options.chat.opacity", true, false), 
/* 3106 */     CHAT_LINKS_PROMPT("CHAT_LINKS_PROMPT", 19, "options.chat.links.prompt", false, true), 
/* 3107 */     SNOOPER_ENABLED("SNOOPER_ENABLED", 20, "options.snooper", false, true), 
/* 3108 */     USE_FULLSCREEN("USE_FULLSCREEN", 21, "options.fullscreen", false, true), 
/* 3109 */     ENABLE_VSYNC("ENABLE_VSYNC", 22, "options.vsync", false, true), 
/* 3110 */     USE_VBO("USE_VBO", 23, "options.vbo", false, true), 
/* 3111 */     TOUCHSCREEN("TOUCHSCREEN", 24, "options.touchscreen", false, true), 
/* 3112 */     CHAT_SCALE("CHAT_SCALE", 25, "options.chat.scale", true, false), 
/* 3113 */     CHAT_WIDTH("CHAT_WIDTH", 26, "options.chat.width", true, false), 
/* 3114 */     CHAT_HEIGHT_FOCUSED("CHAT_HEIGHT_FOCUSED", 27, "options.chat.height.focused", true, false), 
/* 3115 */     CHAT_HEIGHT_UNFOCUSED("CHAT_HEIGHT_UNFOCUSED", 28, "options.chat.height.unfocused", true, false), 
/* 3116 */     MIPMAP_LEVELS("MIPMAP_LEVELS", 29, "options.mipmapLevels", true, false, 0.0F, 4.0F, 1.0F), 
/* 3117 */     FORCE_UNICODE_FONT("FORCE_UNICODE_FONT", 30, "options.forceUnicodeFont", false, true), 
/* 3118 */     STREAM_BYTES_PER_PIXEL("STREAM_BYTES_PER_PIXEL", 31, "options.stream.bytesPerPixel", true, false), 
/* 3119 */     STREAM_VOLUME_MIC("STREAM_VOLUME_MIC", 32, "options.stream.micVolumne", true, false), 
/* 3120 */     STREAM_VOLUME_SYSTEM("STREAM_VOLUME_SYSTEM", 33, "options.stream.systemVolume", true, false), 
/* 3121 */     STREAM_KBPS("STREAM_KBPS", 34, "options.stream.kbps", true, false), 
/* 3122 */     STREAM_FPS("STREAM_FPS", 35, "options.stream.fps", true, false), 
/* 3123 */     STREAM_COMPRESSION("STREAM_COMPRESSION", 36, "options.stream.compression", false, false), 
/* 3124 */     STREAM_SEND_METADATA("STREAM_SEND_METADATA", 37, "options.stream.sendMetadata", false, true), 
/* 3125 */     STREAM_CHAT_ENABLED("STREAM_CHAT_ENABLED", 38, "options.stream.chat.enabled", false, false), 
/* 3126 */     STREAM_CHAT_USER_FILTER("STREAM_CHAT_USER_FILTER", 39, "options.stream.chat.userFilter", false, false), 
/* 3127 */     STREAM_MIC_TOGGLE_BEHAVIOR("STREAM_MIC_TOGGLE_BEHAVIOR", 40, "options.stream.micToggleBehavior", false, false), 
/* 3128 */     BLOCK_ALTERNATIVES("BLOCK_ALTERNATIVES", 41, "options.blockAlternatives", false, true), 
/* 3129 */     REDUCED_DEBUG_INFO("REDUCED_DEBUG_INFO", 42, "options.reducedDebugInfo", false, true), 
/* 3130 */     ENTITY_SHADOWS("ENTITY_SHADOWS", 43, "options.entityShadows", false, true), 
/* 3131 */     FOG_FANCY("FOG", 999, "Fog", false, false), 
/* 3132 */     FOG_START("", 999, "Fog Start", false, false), 
/* 3133 */     MIPMAP_TYPE("", 999, "Mipmap Type", true, false, 0.0F, 3.0F, 1.0F), 
/* 3134 */     SMOOTH_FPS("", 999, "Smooth FPS", false, false), 
/* 3135 */     CLOUDS("", 999, "Clouds", false, false), 
/* 3136 */     CLOUD_HEIGHT("", 999, "Cloud Height", true, false), 
/* 3137 */     TREES("", 999, "Trees", false, false), 
/* 3138 */     RAIN("", 999, "Rain & Snow", false, false), 
/* 3139 */     ANIMATED_WATER("", 999, "Water Animated", false, false), 
/* 3140 */     ANIMATED_LAVA("", 999, "Lava Animated", false, false), 
/* 3141 */     ANIMATED_FIRE("", 999, "Fire Animated", false, false), 
/* 3142 */     ANIMATED_PORTAL("", 999, "Portal Animated", false, false), 
/* 3143 */     AO_LEVEL("", 999, "Smooth Lighting Level", true, false), 
/* 3144 */     LAGOMETER("", 999, "Lagometer", false, false), 
/* 3145 */     SHOW_FPS("", 999, "Show FPS", false, false), 
/* 3146 */     AUTOSAVE_TICKS("", 999, "Autosave", false, false), 
/* 3147 */     BETTER_GRASS("", 999, "Better Grass", false, false), 
/* 3148 */     ANIMATED_REDSTONE("", 999, "Redstone Animated", false, false), 
/* 3149 */     ANIMATED_EXPLOSION("", 999, "Explosion Animated", false, false), 
/* 3150 */     ANIMATED_FLAME("", 999, "Flame Animated", false, false), 
/* 3151 */     ANIMATED_SMOKE("", 999, "Smoke Animated", false, false), 
/* 3152 */     WEATHER("", 999, "Weather", false, false), 
/* 3153 */     SKY("", 999, "Sky", false, false), 
/* 3154 */     STARS("", 999, "Stars", false, false), 
/* 3155 */     SUN_MOON("", 999, "Sun & Moon", false, false), 
/* 3156 */     VIGNETTE("", 999, "Vignette", false, false), 
/* 3157 */     CHUNK_UPDATES("", 999, "Chunk Updates", false, false), 
/* 3158 */     CHUNK_UPDATES_DYNAMIC("", 999, "Dynamic Updates", false, false), 
/* 3159 */     TIME("", 999, "Time", false, false), 
/* 3160 */     CLEAR_WATER("", 999, "Clear Water", false, false), 
/* 3161 */     SMOOTH_WORLD("", 999, "Smooth World", false, false), 
/* 3162 */     VOID_PARTICLES("", 999, "Void Particles", false, false), 
/* 3163 */     WATER_PARTICLES("", 999, "Water Particles", false, false), 
/* 3164 */     RAIN_SPLASH("", 999, "Rain Splash", false, false), 
/* 3165 */     PORTAL_PARTICLES("", 999, "Portal Particles", false, false), 
/* 3166 */     POTION_PARTICLES("", 999, "Potion Particles", false, false), 
/* 3167 */     FIREWORK_PARTICLES("", 999, "Firework Particles", false, false), 
/* 3168 */     PROFILER("", 999, "Debug Profiler", false, false), 
/* 3169 */     DRIPPING_WATER_LAVA("", 999, "Dripping Water/Lava", false, false), 
/* 3170 */     BETTER_SNOW("", 999, "Better Snow", false, false), 
/* 3171 */     FULLSCREEN_MODE("", 999, "Fullscreen Mode", false, false), 
/* 3172 */     ANIMATED_TERRAIN("", 999, "Terrain Animated", false, false), 
/* 3173 */     SWAMP_COLORS("", 999, "Swamp Colors", false, false), 
/* 3174 */     RANDOM_MOBS("", 999, "Random Mobs", false, false), 
/* 3175 */     SMOOTH_BIOMES("", 999, "Smooth Biomes", false, false), 
/* 3176 */     CUSTOM_FONTS("", 999, "Custom Fonts", false, false), 
/* 3177 */     CUSTOM_COLORS("", 999, "Custom Colors", false, false), 
/* 3178 */     SHOW_CAPES("", 999, "Show Capes", false, false), 
/* 3179 */     CONNECTED_TEXTURES("", 999, "Connected Textures", false, false), 
/* 3180 */     AA_LEVEL("", 999, "Antialiasing", true, false, 0.0F, 16.0F, 1.0F), 
/* 3181 */     AF_LEVEL("", 999, "Anisotropic Filtering", true, false, 1.0F, 16.0F, 1.0F), 
/* 3182 */     ANIMATED_TEXTURES("", 999, "Textures Animated", false, false), 
/* 3183 */     NATURAL_TEXTURES("", 999, "Natural Textures", false, false), 
/* 3184 */     CHUNK_LOADING("", 999, "Chunk Loading", false, false), 
/* 3185 */     HELD_ITEM_TOOLTIPS("", 999, "Held Item Tooltips", false, false), 
/* 3186 */     DROPPED_ITEMS("", 999, "Dropped Items", false, false), 
/* 3187 */     LAZY_CHUNK_LOADING("", 999, "Lazy Chunk Loading", false, false), 
/* 3188 */     CUSTOM_SKY("", 999, "Custom Sky", false, false), 
/* 3189 */     FAST_MATH("", 999, "Fast Math", false, false), 
/* 3190 */     FAST_RENDER("", 999, "Fast Render", false, false), 
/* 3191 */     TRANSLUCENT_BLOCKS("", 999, "Translucent Blocks", false, false);
/*      */     
/*      */     private final boolean enumFloat;
/*      */     private final boolean enumBoolean;
/*      */     private final String enumString;
/*      */     private final float valueStep;
/*      */     private float valueMin;
/*      */     private float valueMax;
/* 3199 */     private static final Options[] $VALUES = { INVERT_MOUSE, SENSITIVITY, FOV, GAMMA, SATURATION, RENDER_DISTANCE, VIEW_BOBBING, ANAGLYPH, FRAMERATE_LIMIT, FBO_ENABLE, RENDER_CLOUDS, GRAPHICS, AMBIENT_OCCLUSION, GUI_SCALE, PARTICLES, CHAT_VISIBILITY, CHAT_COLOR, CHAT_LINKS, CHAT_OPACITY, CHAT_LINKS_PROMPT, SNOOPER_ENABLED, USE_FULLSCREEN, ENABLE_VSYNC, USE_VBO, TOUCHSCREEN, CHAT_SCALE, CHAT_WIDTH, CHAT_HEIGHT_FOCUSED, CHAT_HEIGHT_UNFOCUSED, MIPMAP_LEVELS, FORCE_UNICODE_FONT, STREAM_BYTES_PER_PIXEL, STREAM_VOLUME_MIC, STREAM_VOLUME_SYSTEM, STREAM_KBPS, STREAM_FPS, STREAM_COMPRESSION, STREAM_SEND_METADATA, STREAM_CHAT_ENABLED, STREAM_CHAT_USER_FILTER, STREAM_MIC_TOGGLE_BEHAVIOR, BLOCK_ALTERNATIVES, REDUCED_DEBUG_INFO, ENTITY_SHADOWS };
/*      */     private static final String __OBFID = "CL_00000653";
/*      */     
/*      */     public static Options getEnumOptions(int p_74379_0_) {
/*      */       Options[] arrayOfOptions;
/* 3204 */       int j = (arrayOfOptions = values()).length; for (int i = 0; i < j; i++) { Options gamesettings$options = arrayOfOptions[i];
/*      */         
/* 3206 */         if (gamesettings$options.returnEnumOrdinal() == p_74379_0_)
/*      */         {
/* 3208 */           return gamesettings$options;
/*      */         }
/*      */       }
/*      */       
/* 3212 */       return null;
/*      */     }
/*      */     
/*      */     private Options(String p_i0_3_, int p_i0_4_, String p_i0_5_, boolean p_i0_6_, boolean p_i0_7_)
/*      */     {
/* 3217 */       this(p_i0_3_, p_i0_4_, p_i0_5_, p_i0_6_, p_i0_7_, 0.0F, 1.0F, 0.0F);
/*      */     }
/*      */     
/*      */     private Options(String p_i1_3_, int p_i1_4_, String p_i1_5_, boolean p_i1_6_, boolean p_i1_7_, float p_i1_8_, float p_i1_9_, float p_i1_10_)
/*      */     {
/* 3222 */       this.enumString = p_i1_5_;
/* 3223 */       this.enumFloat = p_i1_6_;
/* 3224 */       this.enumBoolean = p_i1_7_;
/* 3225 */       this.valueMin = p_i1_8_;
/* 3226 */       this.valueMax = p_i1_9_;
/* 3227 */       this.valueStep = p_i1_10_;
/*      */     }
/*      */     
/*      */     public boolean getEnumFloat()
/*      */     {
/* 3232 */       return this.enumFloat;
/*      */     }
/*      */     
/*      */     public boolean getEnumBoolean()
/*      */     {
/* 3237 */       return this.enumBoolean;
/*      */     }
/*      */     
/*      */     public int returnEnumOrdinal()
/*      */     {
/* 3242 */       return ordinal();
/*      */     }
/*      */     
/*      */     public String getEnumString()
/*      */     {
/* 3247 */       return this.enumString;
/*      */     }
/*      */     
/*      */     public float getValueMax()
/*      */     {
/* 3252 */       return this.valueMax;
/*      */     }
/*      */     
/*      */     public void setValueMax(float p_148263_1_)
/*      */     {
/* 3257 */       this.valueMax = p_148263_1_;
/*      */     }
/*      */     
/*      */     public float normalizeValue(float p_148266_1_)
/*      */     {
/* 3262 */       return MathHelper.clamp_float((snapToStepClamp(p_148266_1_) - this.valueMin) / (this.valueMax - this.valueMin), 0.0F, 1.0F);
/*      */     }
/*      */     
/*      */     public float denormalizeValue(float p_148262_1_)
/*      */     {
/* 3267 */       return snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp_float(p_148262_1_, 0.0F, 1.0F));
/*      */     }
/*      */     
/*      */     public float snapToStepClamp(float p_148268_1_)
/*      */     {
/* 3272 */       p_148268_1_ = snapToStep(p_148268_1_);
/* 3273 */       return MathHelper.clamp_float(p_148268_1_, this.valueMin, this.valueMax);
/*      */     }
/*      */     
/*      */     protected float snapToStep(float p_148264_1_)
/*      */     {
/* 3278 */       if (this.valueStep > 0.0F)
/*      */       {
/* 3280 */         p_148264_1_ = this.valueStep * Math.round(p_148264_1_ / this.valueStep);
/*      */       }
/*      */       
/* 3283 */       return p_148264_1_;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\settings\GameSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */