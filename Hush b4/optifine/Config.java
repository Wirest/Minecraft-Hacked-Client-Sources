// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.renderer.texture.TextureMap;
import java.io.FileOutputStream;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.gui.GuiScreen;
import java.util.Collection;
import java.lang.reflect.Array;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.gui.ScaledResolution;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.PixelFormat;
import net.minecraft.world.WorldProvider;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.util.BlockPos;
import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.LWJGLException;
import java.util.HashSet;
import java.util.Set;
import java.util.Comparator;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.awt.Dimension;
import net.minecraft.client.renderer.RenderGlobal;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.resources.ResourcePackRepository;
import java.util.ArrayList;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.IResource;
import java.io.IOException;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.IResourceManager;
import java.util.Properties;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL30;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.InputStream;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.GL11;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import shadersmod.client.Shaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.resources.DefaultResourcePack;
import org.lwjgl.opengl.DisplayMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class Config
{
    public static final String OF_NAME = "OptiFine";
    public static final String MC_VERSION = "1.8.8";
    public static final String OF_EDITION = "HD_U";
    public static final String OF_RELEASE = "H8";
    public static final String VERSION = "OptiFine_1.8.8_HD_U_H8";
    private static String newRelease;
    private static boolean notify64BitJava;
    public static String openGlVersion;
    public static String openGlRenderer;
    public static String openGlVendor;
    public static String[] openGlExtensions;
    public static GlVersion glVersion;
    public static GlVersion glslVersion;
    public static int minecraftVersionInt;
    public static boolean fancyFogAvailable;
    public static boolean occlusionAvailable;
    private static GameSettings gameSettings;
    private static Minecraft minecraft;
    private static boolean initialized;
    private static Thread minecraftThread;
    private static DisplayMode desktopDisplayMode;
    private static DisplayMode[] displayModes;
    private static int antialiasingLevel;
    private static int availableProcessors;
    public static boolean zoomMode;
    private static int texturePackClouds;
    public static boolean waterOpacityChanged;
    private static boolean fullscreenModeChecked;
    private static boolean desktopModeChecked;
    private static DefaultResourcePack defaultResourcePackLazy;
    public static final Float DEF_ALPHA_FUNC_LEVEL;
    private static final Logger LOGGER;
    
    static {
        Config.newRelease = null;
        Config.notify64BitJava = false;
        Config.openGlVersion = null;
        Config.openGlRenderer = null;
        Config.openGlVendor = null;
        Config.openGlExtensions = null;
        Config.glVersion = null;
        Config.glslVersion = null;
        Config.minecraftVersionInt = -1;
        Config.fancyFogAvailable = false;
        Config.occlusionAvailable = false;
        Config.gameSettings = null;
        Config.minecraft = Minecraft.getMinecraft();
        Config.initialized = false;
        Config.minecraftThread = null;
        Config.desktopDisplayMode = null;
        Config.displayModes = null;
        Config.antialiasingLevel = 0;
        Config.availableProcessors = 0;
        Config.zoomMode = false;
        Config.texturePackClouds = 0;
        Config.waterOpacityChanged = false;
        Config.fullscreenModeChecked = false;
        Config.desktopModeChecked = false;
        Config.defaultResourcePackLazy = null;
        DEF_ALPHA_FUNC_LEVEL = 0.1f;
        LOGGER = LogManager.getLogger();
    }
    
    public static String getVersion() {
        return "OptiFine_1.8.8_HD_U_H8";
    }
    
    public static String getVersionDebug() {
        final StringBuffer stringbuffer = new StringBuffer(32);
        if (isDynamicLights()) {
            stringbuffer.append("DL: ");
            stringbuffer.append(String.valueOf(DynamicLights.getCount()));
            stringbuffer.append(", ");
        }
        stringbuffer.append("OptiFine_1.8.8_HD_U_H8");
        final String s = Shaders.getShaderPackName();
        if (s != null) {
            stringbuffer.append(", ");
            stringbuffer.append(s);
        }
        return stringbuffer.toString();
    }
    
    public static void initGameSettings(final GameSettings p_initGameSettings_0_) {
        if (Config.gameSettings == null) {
            Config.gameSettings = p_initGameSettings_0_;
            Config.desktopDisplayMode = Display.getDesktopDisplayMode();
            updateAvailableProcessors();
            ReflectorForge.putLaunchBlackboard("optifine.ForgeSplashCompatible", Boolean.TRUE);
        }
    }
    
    public static void initDisplay() {
        checkInitialized();
        Config.antialiasingLevel = Config.gameSettings.ofAaLevel;
        checkDisplaySettings();
        checkDisplayMode();
        Config.minecraftThread = Thread.currentThread();
        updateThreadPriorities();
        Shaders.startup(Minecraft.getMinecraft());
    }
    
    public static void checkInitialized() {
        if (!Config.initialized && Display.isCreated()) {
            Config.initialized = true;
            checkOpenGlCaps();
            startVersionCheckThread();
        }
    }
    
    private static void checkOpenGlCaps() {
        log("");
        log(getVersion());
        log("Build: " + getBuild());
        log("OS: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version"));
        log("Java: " + System.getProperty("java.version") + ", " + System.getProperty("java.vendor"));
        log("VM: " + System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor"));
        log("LWJGL: " + Sys.getVersion());
        Config.openGlVersion = GL11.glGetString(7938);
        Config.openGlRenderer = GL11.glGetString(7937);
        Config.openGlVendor = GL11.glGetString(7936);
        log("OpenGL: " + Config.openGlRenderer + ", version " + Config.openGlVersion + ", " + Config.openGlVendor);
        log("OpenGL Version: " + getOpenGlVersionString());
        if (!GLContext.getCapabilities().OpenGL12) {
            log("OpenGL Mipmap levels: Not available (GL12.GL_TEXTURE_MAX_LEVEL)");
        }
        if (!(Config.fancyFogAvailable = GLContext.getCapabilities().GL_NV_fog_distance)) {
            log("OpenGL Fancy fog: Not available (GL_NV_fog_distance)");
        }
        if (!(Config.occlusionAvailable = GLContext.getCapabilities().GL_ARB_occlusion_query)) {
            log("OpenGL Occlussion culling: Not available (GL_ARB_occlusion_query)");
        }
        final int i = TextureUtils.getGLMaximumTextureSize();
        dbg("Maximum texture size: " + i + "x" + i);
    }
    
    private static String getBuild() {
        try {
            final InputStream inputstream = Config.class.getResourceAsStream("/buildof.txt");
            if (inputstream == null) {
                return null;
            }
            final String s = readLines(inputstream)[0];
            return s;
        }
        catch (Exception exception) {
            warn(exception.getClass().getName() + ": " + exception.getMessage());
            return null;
        }
    }
    
    public static boolean isFancyFogAvailable() {
        return Config.fancyFogAvailable;
    }
    
    public static boolean isOcclusionAvailable() {
        return Config.occlusionAvailable;
    }
    
    public static int getMinecraftVersionInt() {
        if (Config.minecraftVersionInt < 0) {
            final String[] astring = tokenize("1.8.8", ".");
            int i = 0;
            if (astring.length > 0) {
                i += 10000 * parseInt(astring[0], 0);
            }
            if (astring.length > 1) {
                i += 100 * parseInt(astring[1], 0);
            }
            if (astring.length > 2) {
                i += 1 * parseInt(astring[2], 0);
            }
            Config.minecraftVersionInt = i;
        }
        return Config.minecraftVersionInt;
    }
    
    public static String getOpenGlVersionString() {
        final GlVersion glversion = getGlVersion();
        final String s = glversion.getMajor() + "." + glversion.getMinor() + "." + glversion.getRelease();
        return s;
    }
    
    private static GlVersion getGlVersionLwjgl() {
        return GLContext.getCapabilities().OpenGL44 ? new GlVersion(4, 4) : (GLContext.getCapabilities().OpenGL43 ? new GlVersion(4, 3) : (GLContext.getCapabilities().OpenGL42 ? new GlVersion(4, 2) : (GLContext.getCapabilities().OpenGL41 ? new GlVersion(4, 1) : (GLContext.getCapabilities().OpenGL40 ? new GlVersion(4, 0) : (GLContext.getCapabilities().OpenGL33 ? new GlVersion(3, 3) : (GLContext.getCapabilities().OpenGL32 ? new GlVersion(3, 2) : (GLContext.getCapabilities().OpenGL31 ? new GlVersion(3, 1) : (GLContext.getCapabilities().OpenGL30 ? new GlVersion(3, 0) : (GLContext.getCapabilities().OpenGL21 ? new GlVersion(2, 1) : (GLContext.getCapabilities().OpenGL20 ? new GlVersion(2, 0) : (GLContext.getCapabilities().OpenGL15 ? new GlVersion(1, 5) : (GLContext.getCapabilities().OpenGL14 ? new GlVersion(1, 4) : (GLContext.getCapabilities().OpenGL13 ? new GlVersion(1, 3) : (GLContext.getCapabilities().OpenGL12 ? new GlVersion(1, 2) : (GLContext.getCapabilities().OpenGL11 ? new GlVersion(1, 1) : new GlVersion(1, 0))))))))))))))));
    }
    
    public static GlVersion getGlVersion() {
        if (Config.glVersion == null) {
            final String s = GL11.glGetString(7938);
            Config.glVersion = parseGlVersion(s, null);
            if (Config.glVersion == null) {
                Config.glVersion = getGlVersionLwjgl();
            }
            if (Config.glVersion == null) {
                Config.glVersion = new GlVersion(1, 0);
            }
        }
        return Config.glVersion;
    }
    
    public static GlVersion getGlslVersion() {
        if (Config.glslVersion == null) {
            final String s = GL11.glGetString(35724);
            Config.glslVersion = parseGlVersion(s, null);
            if (Config.glslVersion == null) {
                Config.glslVersion = new GlVersion(1, 10);
            }
        }
        return Config.glslVersion;
    }
    
    public static GlVersion parseGlVersion(final String p_parseGlVersion_0_, final GlVersion p_parseGlVersion_1_) {
        try {
            if (p_parseGlVersion_0_ == null) {
                return p_parseGlVersion_1_;
            }
            final Pattern pattern = Pattern.compile("([0-9]+)\\.([0-9]+)(\\.([0-9]+))?(.+)?");
            final Matcher matcher = pattern.matcher(p_parseGlVersion_0_);
            if (!matcher.matches()) {
                return p_parseGlVersion_1_;
            }
            final int i = Integer.parseInt(matcher.group(1));
            final int j = Integer.parseInt(matcher.group(2));
            final int k = (matcher.group(4) != null) ? Integer.parseInt(matcher.group(4)) : 0;
            final String s = matcher.group(5);
            return new GlVersion(i, j, k, s);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return p_parseGlVersion_1_;
        }
    }
    
    public static String[] getOpenGlExtensions() {
        if (Config.openGlExtensions == null) {
            Config.openGlExtensions = detectOpenGlExtensions();
        }
        return Config.openGlExtensions;
    }
    
    private static String[] detectOpenGlExtensions() {
        try {
            final GlVersion glversion = getGlVersion();
            if (glversion.getMajor() >= 3) {
                final int i = GL11.glGetInteger(33309);
                if (i > 0) {
                    final String[] astring = new String[i];
                    for (int j = 0; j < i; ++j) {
                        astring[j] = GL30.glGetStringi(7939, j);
                    }
                    return astring;
                }
            }
        }
        catch (Exception exception1) {
            exception1.printStackTrace();
        }
        try {
            final String s = GL11.glGetString(7939);
            final String[] astring2 = s.split(" ");
            return astring2;
        }
        catch (Exception exception2) {
            exception2.printStackTrace();
            return new String[0];
        }
    }
    
    public static void updateThreadPriorities() {
        updateAvailableProcessors();
        final int i = 8;
        if (isSingleProcessor()) {
            if (isSmoothWorld()) {
                Config.minecraftThread.setPriority(10);
                setThreadPriority("Server thread", 1);
            }
            else {
                Config.minecraftThread.setPriority(5);
                setThreadPriority("Server thread", 5);
            }
        }
        else {
            Config.minecraftThread.setPriority(10);
            setThreadPriority("Server thread", 5);
        }
    }
    
    private static void setThreadPriority(final String p_setThreadPriority_0_, final int p_setThreadPriority_1_) {
        try {
            final ThreadGroup threadgroup = Thread.currentThread().getThreadGroup();
            if (threadgroup == null) {
                return;
            }
            final int i = (threadgroup.activeCount() + 10) * 2;
            final Thread[] athread = new Thread[i];
            threadgroup.enumerate(athread, false);
            for (int j = 0; j < athread.length; ++j) {
                final Thread thread = athread[j];
                if (thread != null && thread.getName().startsWith(p_setThreadPriority_0_)) {
                    thread.setPriority(p_setThreadPriority_1_);
                }
            }
        }
        catch (Throwable throwable) {
            warn(String.valueOf(throwable.getClass().getName()) + ": " + throwable.getMessage());
        }
    }
    
    public static boolean isMinecraftThread() {
        return Thread.currentThread() == Config.minecraftThread;
    }
    
    private static void startVersionCheckThread() {
        final VersionCheckThread versioncheckthread = new VersionCheckThread();
        versioncheckthread.start();
    }
    
    public static boolean isMipmaps() {
        return Config.gameSettings.mipmapLevels > 0;
    }
    
    public static int getMipmapLevels() {
        return Config.gameSettings.mipmapLevels;
    }
    
    public static int getMipmapType() {
        switch (Config.gameSettings.ofMipmapType) {
            case 0: {
                return 9986;
            }
            case 1: {
                return 9986;
            }
            case 2: {
                if (isMultiTexture()) {
                    return 9985;
                }
                return 9986;
            }
            case 3: {
                if (isMultiTexture()) {
                    return 9987;
                }
                return 9986;
            }
            default: {
                return 9986;
            }
        }
    }
    
    public static boolean isUseAlphaFunc() {
        final float f = getAlphaFuncLevel();
        return f > Config.DEF_ALPHA_FUNC_LEVEL + 1.0E-5f;
    }
    
    public static float getAlphaFuncLevel() {
        return Config.DEF_ALPHA_FUNC_LEVEL;
    }
    
    public static boolean isFogFancy() {
        return isFancyFogAvailable() && Config.gameSettings.ofFogType == 2;
    }
    
    public static boolean isFogFast() {
        return Config.gameSettings.ofFogType == 1;
    }
    
    public static boolean isFogOff() {
        return Config.gameSettings.ofFogType == 3;
    }
    
    public static float getFogStart() {
        return Config.gameSettings.ofFogStart;
    }
    
    public static void dbg(final String p_dbg_0_) {
        Config.LOGGER.info("[OptiFine] " + p_dbg_0_);
    }
    
    public static void warn(final String p_warn_0_) {
        Config.LOGGER.warn("[OptiFine] " + p_warn_0_);
    }
    
    public static void error(final String p_error_0_) {
        Config.LOGGER.error("[OptiFine] " + p_error_0_);
    }
    
    public static void log(final String p_log_0_) {
        dbg(p_log_0_);
    }
    
    public static int getUpdatesPerFrame() {
        return Config.gameSettings.ofChunkUpdates;
    }
    
    public static boolean isDynamicUpdates() {
        return Config.gameSettings.ofChunkUpdatesDynamic;
    }
    
    public static boolean isRainFancy() {
        return (Config.gameSettings.ofRain == 0) ? Config.gameSettings.fancyGraphics : (Config.gameSettings.ofRain == 2);
    }
    
    public static boolean isRainOff() {
        return Config.gameSettings.ofRain == 3;
    }
    
    public static boolean isCloudsFancy() {
        return (Config.gameSettings.ofClouds != 0) ? (Config.gameSettings.ofClouds == 2) : ((isShaders() && !Shaders.shaderPackClouds.isDefault()) ? Shaders.shaderPackClouds.isFancy() : ((Config.texturePackClouds != 0) ? (Config.texturePackClouds == 2) : Config.gameSettings.fancyGraphics));
    }
    
    public static boolean isCloudsOff() {
        return (Config.gameSettings.ofClouds != 0) ? (Config.gameSettings.ofClouds == 3) : ((isShaders() && !Shaders.shaderPackClouds.isDefault()) ? Shaders.shaderPackClouds.isOff() : (Config.texturePackClouds != 0 && Config.texturePackClouds == 3));
    }
    
    public static void updateTexturePackClouds() {
        Config.texturePackClouds = 0;
        final IResourceManager iresourcemanager = getResourceManager();
        if (iresourcemanager != null) {
            try {
                final InputStream inputstream = iresourcemanager.getResource(new ResourceLocation("mcpatcher/color.properties")).getInputStream();
                if (inputstream == null) {
                    return;
                }
                final Properties properties = new Properties();
                properties.load(inputstream);
                inputstream.close();
                String s = properties.getProperty("clouds");
                if (s == null) {
                    return;
                }
                dbg("Texture pack clouds: " + s);
                s = s.toLowerCase();
                if (s.equals("fast")) {
                    Config.texturePackClouds = 1;
                }
                if (s.equals("fancy")) {
                    Config.texturePackClouds = 2;
                }
                if (s.equals("off")) {
                    Config.texturePackClouds = 3;
                }
            }
            catch (Exception ex) {}
        }
    }
    
    public static ModelManager getModelManager() {
        return Config.minecraft.getRenderItem().modelManager;
    }
    
    public static boolean isTreesFancy() {
        return (Config.gameSettings.ofTrees == 0) ? Config.gameSettings.fancyGraphics : (Config.gameSettings.ofTrees != 1);
    }
    
    public static boolean isTreesSmart() {
        return Config.gameSettings.ofTrees == 4;
    }
    
    public static boolean isCullFacesLeaves() {
        return (Config.gameSettings.ofTrees == 0) ? (!Config.gameSettings.fancyGraphics) : (Config.gameSettings.ofTrees == 4);
    }
    
    public static boolean isDroppedItemsFancy() {
        return (Config.gameSettings.ofDroppedItems == 0) ? Config.gameSettings.fancyGraphics : (Config.gameSettings.ofDroppedItems == 2);
    }
    
    public static int limit(final int p_limit_0_, final int p_limit_1_, final int p_limit_2_) {
        return (p_limit_0_ < p_limit_1_) ? p_limit_1_ : ((p_limit_0_ > p_limit_2_) ? p_limit_2_ : p_limit_0_);
    }
    
    public static float limit(final float p_limit_0_, final float p_limit_1_, final float p_limit_2_) {
        return (p_limit_0_ < p_limit_1_) ? p_limit_1_ : ((p_limit_0_ > p_limit_2_) ? p_limit_2_ : p_limit_0_);
    }
    
    public static double limit(final double p_limit_0_, final double p_limit_2_, final double p_limit_4_) {
        return (p_limit_0_ < p_limit_2_) ? p_limit_2_ : ((p_limit_0_ > p_limit_4_) ? p_limit_4_ : p_limit_0_);
    }
    
    public static float limitTo1(final float p_limitTo1_0_) {
        return (p_limitTo1_0_ < 0.0f) ? 0.0f : ((p_limitTo1_0_ > 1.0f) ? 1.0f : p_limitTo1_0_);
    }
    
    public static boolean isAnimatedWater() {
        return Config.gameSettings.ofAnimatedWater != 2;
    }
    
    public static boolean isGeneratedWater() {
        return Config.gameSettings.ofAnimatedWater == 1;
    }
    
    public static boolean isAnimatedPortal() {
        return Config.gameSettings.ofAnimatedPortal;
    }
    
    public static boolean isAnimatedLava() {
        return Config.gameSettings.ofAnimatedLava != 2;
    }
    
    public static boolean isGeneratedLava() {
        return Config.gameSettings.ofAnimatedLava == 1;
    }
    
    public static boolean isAnimatedFire() {
        return Config.gameSettings.ofAnimatedFire;
    }
    
    public static boolean isAnimatedRedstone() {
        return Config.gameSettings.ofAnimatedRedstone;
    }
    
    public static boolean isAnimatedExplosion() {
        return Config.gameSettings.ofAnimatedExplosion;
    }
    
    public static boolean isAnimatedFlame() {
        return Config.gameSettings.ofAnimatedFlame;
    }
    
    public static boolean isAnimatedSmoke() {
        return Config.gameSettings.ofAnimatedSmoke;
    }
    
    public static boolean isVoidParticles() {
        return Config.gameSettings.ofVoidParticles;
    }
    
    public static boolean isWaterParticles() {
        return Config.gameSettings.ofWaterParticles;
    }
    
    public static boolean isRainSplash() {
        return Config.gameSettings.ofRainSplash;
    }
    
    public static boolean isPortalParticles() {
        return Config.gameSettings.ofPortalParticles;
    }
    
    public static boolean isPotionParticles() {
        return Config.gameSettings.ofPotionParticles;
    }
    
    public static boolean isFireworkParticles() {
        return Config.gameSettings.ofFireworkParticles;
    }
    
    public static float getAmbientOcclusionLevel() {
        return (isShaders() && Shaders.aoLevel >= 0.0f) ? Shaders.aoLevel : Config.gameSettings.ofAoLevel;
    }
    
    public static String arrayToString(final Object[] p_arrayToString_0_) {
        if (p_arrayToString_0_ == null) {
            return "";
        }
        final StringBuffer stringbuffer = new StringBuffer(p_arrayToString_0_.length * 5);
        for (int i = 0; i < p_arrayToString_0_.length; ++i) {
            final Object object = p_arrayToString_0_[i];
            if (i > 0) {
                stringbuffer.append(", ");
            }
            stringbuffer.append(String.valueOf(object));
        }
        return stringbuffer.toString();
    }
    
    public static String arrayToString(final int[] p_arrayToString_0_) {
        if (p_arrayToString_0_ == null) {
            return "";
        }
        final StringBuffer stringbuffer = new StringBuffer(p_arrayToString_0_.length * 5);
        for (int i = 0; i < p_arrayToString_0_.length; ++i) {
            final int j = p_arrayToString_0_[i];
            if (i > 0) {
                stringbuffer.append(", ");
            }
            stringbuffer.append(String.valueOf(j));
        }
        return stringbuffer.toString();
    }
    
    public static Minecraft getMinecraft() {
        return Config.minecraft;
    }
    
    public static TextureManager getTextureManager() {
        return Config.minecraft.getTextureManager();
    }
    
    public static IResourceManager getResourceManager() {
        return Config.minecraft.getResourceManager();
    }
    
    public static InputStream getResourceStream(final ResourceLocation p_getResourceStream_0_) throws IOException {
        return getResourceStream(Config.minecraft.getResourceManager(), p_getResourceStream_0_);
    }
    
    public static InputStream getResourceStream(final IResourceManager p_getResourceStream_0_, final ResourceLocation p_getResourceStream_1_) throws IOException {
        final IResource iresource = p_getResourceStream_0_.getResource(p_getResourceStream_1_);
        return (iresource == null) ? null : iresource.getInputStream();
    }
    
    public static IResource getResource(final ResourceLocation p_getResource_0_) throws IOException {
        return Config.minecraft.getResourceManager().getResource(p_getResource_0_);
    }
    
    public static boolean hasResource(final ResourceLocation p_hasResource_0_) {
        final IResourcePack iresourcepack = getDefiningResourcePack(p_hasResource_0_);
        return iresourcepack != null;
    }
    
    public static boolean hasResource(final IResourceManager p_hasResource_0_, final ResourceLocation p_hasResource_1_) {
        try {
            final IResource iresource = p_hasResource_0_.getResource(p_hasResource_1_);
            return iresource != null;
        }
        catch (IOException var3) {
            return false;
        }
    }
    
    public static IResourcePack[] getResourcePacks() {
        final ResourcePackRepository resourcepackrepository = Config.minecraft.getResourcePackRepository();
        final List list = resourcepackrepository.getRepositoryEntries();
        final List list2 = new ArrayList();
        for (final Object resourcepackrepository$entry : list) {
            list2.add(((ResourcePackRepository.Entry)resourcepackrepository$entry).getResourcePack());
        }
        if (resourcepackrepository.getResourcePackInstance() != null) {
            list2.add(resourcepackrepository.getResourcePackInstance());
        }
        final IResourcePack[] airesourcepack = list2.toArray(new IResourcePack[list2.size()]);
        return airesourcepack;
    }
    
    public static String getResourcePackNames() {
        if (Config.minecraft.getResourcePackRepository() == null) {
            return "";
        }
        final IResourcePack[] airesourcepack = getResourcePacks();
        if (airesourcepack.length <= 0) {
            return getDefaultResourcePack().getPackName();
        }
        final String[] astring = new String[airesourcepack.length];
        for (int i = 0; i < airesourcepack.length; ++i) {
            astring[i] = airesourcepack[i].getPackName();
        }
        final String s = arrayToString(astring);
        return s;
    }
    
    public static DefaultResourcePack getDefaultResourcePack() {
        if (Config.defaultResourcePackLazy == null) {
            final Minecraft minecraft = Minecraft.getMinecraft();
            Config.defaultResourcePackLazy = (DefaultResourcePack)Reflector.getFieldValue(minecraft, Reflector.Minecraft_defaultResourcePack);
            if (Config.defaultResourcePackLazy == null) {
                final ResourcePackRepository resourcepackrepository = minecraft.getResourcePackRepository();
                if (resourcepackrepository != null) {
                    Config.defaultResourcePackLazy = (DefaultResourcePack)resourcepackrepository.rprDefaultResourcePack;
                }
            }
        }
        return Config.defaultResourcePackLazy;
    }
    
    public static boolean isFromDefaultResourcePack(final ResourceLocation p_isFromDefaultResourcePack_0_) {
        final IResourcePack iresourcepack = getDefiningResourcePack(p_isFromDefaultResourcePack_0_);
        return iresourcepack == getDefaultResourcePack();
    }
    
    public static IResourcePack getDefiningResourcePack(final ResourceLocation p_getDefiningResourcePack_0_) {
        final ResourcePackRepository resourcepackrepository = Config.minecraft.getResourcePackRepository();
        final IResourcePack iresourcepack = resourcepackrepository.getResourcePackInstance();
        if (iresourcepack != null && iresourcepack.resourceExists(p_getDefiningResourcePack_0_)) {
            return iresourcepack;
        }
        final List<ResourcePackRepository.Entry> list = (List<ResourcePackRepository.Entry>)Reflector.getFieldValue(resourcepackrepository, Reflector.ResourcePackRepository_repositoryEntries);
        if (list != null) {
            for (int i = list.size() - 1; i >= 0; --i) {
                final ResourcePackRepository.Entry resourcepackrepository$entry = list.get(i);
                final IResourcePack iresourcepack2 = resourcepackrepository$entry.getResourcePack();
                if (iresourcepack2.resourceExists(p_getDefiningResourcePack_0_)) {
                    return iresourcepack2;
                }
            }
        }
        return getDefaultResourcePack().resourceExists(p_getDefiningResourcePack_0_) ? getDefaultResourcePack() : null;
    }
    
    public static RenderGlobal getRenderGlobal() {
        return Config.minecraft.renderGlobal;
    }
    
    public static boolean isBetterGrass() {
        return Config.gameSettings.ofBetterGrass != 3;
    }
    
    public static boolean isBetterGrassFancy() {
        return Config.gameSettings.ofBetterGrass == 2;
    }
    
    public static boolean isWeatherEnabled() {
        return Config.gameSettings.ofWeather;
    }
    
    public static boolean isSkyEnabled() {
        return Config.gameSettings.ofSky;
    }
    
    public static boolean isSunMoonEnabled() {
        return Config.gameSettings.ofSunMoon;
    }
    
    public static boolean isSunTexture() {
        return isSunMoonEnabled() && (!isShaders() || Shaders.isSun());
    }
    
    public static boolean isMoonTexture() {
        return isSunMoonEnabled() && (!isShaders() || Shaders.isMoon());
    }
    
    public static boolean isVignetteEnabled() {
        return (!isShaders() || Shaders.isVignette()) && ((Config.gameSettings.ofVignette == 0) ? Config.gameSettings.fancyGraphics : (Config.gameSettings.ofVignette == 2));
    }
    
    public static boolean isStarsEnabled() {
        return Config.gameSettings.ofStars;
    }
    
    public static void sleep(final long p_sleep_0_) {
        try {
            Thread.sleep(p_sleep_0_);
        }
        catch (InterruptedException interruptedexception) {
            interruptedexception.printStackTrace();
        }
    }
    
    public static boolean isTimeDayOnly() {
        return Config.gameSettings.ofTime == 1;
    }
    
    public static boolean isTimeDefault() {
        return Config.gameSettings.ofTime == 0;
    }
    
    public static boolean isTimeNightOnly() {
        return Config.gameSettings.ofTime == 2;
    }
    
    public static boolean isClearWater() {
        return Config.gameSettings.ofClearWater;
    }
    
    public static int getAnisotropicFilterLevel() {
        return Config.gameSettings.ofAfLevel;
    }
    
    public static boolean isAnisotropicFiltering() {
        return getAnisotropicFilterLevel() > 1;
    }
    
    public static int getAntialiasingLevel() {
        return Config.antialiasingLevel;
    }
    
    public static boolean isAntialiasing() {
        return getAntialiasingLevel() > 0;
    }
    
    public static boolean isAntialiasingConfigured() {
        return getGameSettings().ofAaLevel > 0;
    }
    
    public static boolean isMultiTexture() {
        return getAnisotropicFilterLevel() > 1 || getAntialiasingLevel() > 0;
    }
    
    public static boolean between(final int p_between_0_, final int p_between_1_, final int p_between_2_) {
        return p_between_0_ >= p_between_1_ && p_between_0_ <= p_between_2_;
    }
    
    public static boolean isDrippingWaterLava() {
        return Config.gameSettings.ofDrippingWaterLava;
    }
    
    public static boolean isBetterSnow() {
        return Config.gameSettings.ofBetterSnow;
    }
    
    public static Dimension getFullscreenDimension() {
        if (Config.desktopDisplayMode == null) {
            return null;
        }
        if (Config.gameSettings == null) {
            return new Dimension(Config.desktopDisplayMode.getWidth(), Config.desktopDisplayMode.getHeight());
        }
        final String s = Config.gameSettings.ofFullscreenMode;
        if (s.equals("Default")) {
            return new Dimension(Config.desktopDisplayMode.getWidth(), Config.desktopDisplayMode.getHeight());
        }
        final String[] astring = tokenize(s, " x");
        return (astring.length < 2) ? new Dimension(Config.desktopDisplayMode.getWidth(), Config.desktopDisplayMode.getHeight()) : new Dimension(parseInt(astring[0], -1), parseInt(astring[1], -1));
    }
    
    public static int parseInt(String p_parseInt_0_, final int p_parseInt_1_) {
        try {
            if (p_parseInt_0_ == null) {
                return p_parseInt_1_;
            }
            p_parseInt_0_ = p_parseInt_0_.trim();
            return Integer.parseInt(p_parseInt_0_);
        }
        catch (NumberFormatException var3) {
            return p_parseInt_1_;
        }
    }
    
    public static float parseFloat(String p_parseFloat_0_, final float p_parseFloat_1_) {
        try {
            if (p_parseFloat_0_ == null) {
                return p_parseFloat_1_;
            }
            p_parseFloat_0_ = p_parseFloat_0_.trim();
            return Float.parseFloat(p_parseFloat_0_);
        }
        catch (NumberFormatException var3) {
            return p_parseFloat_1_;
        }
    }
    
    public static boolean parseBoolean(String p_parseBoolean_0_, final boolean p_parseBoolean_1_) {
        try {
            if (p_parseBoolean_0_ == null) {
                return p_parseBoolean_1_;
            }
            p_parseBoolean_0_ = p_parseBoolean_0_.trim();
            return Boolean.parseBoolean(p_parseBoolean_0_);
        }
        catch (NumberFormatException var3) {
            return p_parseBoolean_1_;
        }
    }
    
    public static String[] tokenize(final String p_tokenize_0_, final String p_tokenize_1_) {
        final StringTokenizer stringtokenizer = new StringTokenizer(p_tokenize_0_, p_tokenize_1_);
        final List list = new ArrayList();
        while (stringtokenizer.hasMoreTokens()) {
            final String s = stringtokenizer.nextToken();
            list.add(s);
        }
        final String[] astring = list.toArray(new String[list.size()]);
        return astring;
    }
    
    public static DisplayMode getDesktopDisplayMode() {
        return Config.desktopDisplayMode;
    }
    
    public static DisplayMode[] getDisplayModes() {
        if (Config.displayModes == null) {
            try {
                final DisplayMode[] adisplaymode = Display.getAvailableDisplayModes();
                final Set<Dimension> set = getDisplayModeDimensions(adisplaymode);
                final List list = new ArrayList();
                for (final Dimension dimension : set) {
                    final DisplayMode[] adisplaymode2 = getDisplayModes(adisplaymode, dimension);
                    final DisplayMode displaymode = getDisplayMode(adisplaymode2, Config.desktopDisplayMode);
                    if (displaymode != null) {
                        list.add(displaymode);
                    }
                }
                final DisplayMode[] adisplaymode3 = list.toArray(new DisplayMode[list.size()]);
                Arrays.sort(adisplaymode3, new DisplayModeComparator());
                return adisplaymode3;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                Config.displayModes = new DisplayMode[] { Config.desktopDisplayMode };
            }
        }
        return Config.displayModes;
    }
    
    public static DisplayMode getLargestDisplayMode() {
        final DisplayMode[] adisplaymode = getDisplayModes();
        if (adisplaymode != null && adisplaymode.length >= 1) {
            final DisplayMode displaymode = adisplaymode[adisplaymode.length - 1];
            return (Config.desktopDisplayMode.getWidth() > displaymode.getWidth()) ? Config.desktopDisplayMode : ((Config.desktopDisplayMode.getWidth() == displaymode.getWidth() && Config.desktopDisplayMode.getHeight() > displaymode.getHeight()) ? Config.desktopDisplayMode : displaymode);
        }
        return Config.desktopDisplayMode;
    }
    
    private static Set<Dimension> getDisplayModeDimensions(final DisplayMode[] p_getDisplayModeDimensions_0_) {
        final Set<Dimension> set = new HashSet<Dimension>();
        for (int i = 0; i < p_getDisplayModeDimensions_0_.length; ++i) {
            final DisplayMode displaymode = p_getDisplayModeDimensions_0_[i];
            final Dimension dimension = new Dimension(displaymode.getWidth(), displaymode.getHeight());
            set.add(dimension);
        }
        return set;
    }
    
    private static DisplayMode[] getDisplayModes(final DisplayMode[] p_getDisplayModes_0_, final Dimension p_getDisplayModes_1_) {
        final List list = new ArrayList();
        for (int i = 0; i < p_getDisplayModes_0_.length; ++i) {
            final DisplayMode displaymode = p_getDisplayModes_0_[i];
            if (displaymode.getWidth() == p_getDisplayModes_1_.getWidth() && displaymode.getHeight() == p_getDisplayModes_1_.getHeight()) {
                list.add(displaymode);
            }
        }
        final DisplayMode[] adisplaymode = list.toArray(new DisplayMode[list.size()]);
        return adisplaymode;
    }
    
    private static DisplayMode getDisplayMode(final DisplayMode[] p_getDisplayMode_0_, final DisplayMode p_getDisplayMode_1_) {
        if (p_getDisplayMode_1_ != null) {
            for (int i = 0; i < p_getDisplayMode_0_.length; ++i) {
                final DisplayMode displaymode = p_getDisplayMode_0_[i];
                if (displaymode.getBitsPerPixel() == p_getDisplayMode_1_.getBitsPerPixel() && displaymode.getFrequency() == p_getDisplayMode_1_.getFrequency()) {
                    return displaymode;
                }
            }
        }
        if (p_getDisplayMode_0_.length <= 0) {
            return null;
        }
        Arrays.sort(p_getDisplayMode_0_, new DisplayModeComparator());
        return p_getDisplayMode_0_[p_getDisplayMode_0_.length - 1];
    }
    
    public static String[] getDisplayModeNames() {
        final DisplayMode[] adisplaymode = getDisplayModes();
        final String[] astring = new String[adisplaymode.length];
        for (int i = 0; i < adisplaymode.length; ++i) {
            final DisplayMode displaymode = adisplaymode[i];
            final String s = displaymode.getWidth() + "x" + displaymode.getHeight();
            astring[i] = s;
        }
        return astring;
    }
    
    public static DisplayMode getDisplayMode(final Dimension p_getDisplayMode_0_) throws LWJGLException {
        final DisplayMode[] adisplaymode = getDisplayModes();
        for (int i = 0; i < adisplaymode.length; ++i) {
            final DisplayMode displaymode = adisplaymode[i];
            if (displaymode.getWidth() == p_getDisplayMode_0_.width && displaymode.getHeight() == p_getDisplayMode_0_.height) {
                return displaymode;
            }
        }
        return Config.desktopDisplayMode;
    }
    
    public static boolean isAnimatedTerrain() {
        return Config.gameSettings.ofAnimatedTerrain;
    }
    
    public static boolean isAnimatedTextures() {
        return Config.gameSettings.ofAnimatedTextures;
    }
    
    public static boolean isSwampColors() {
        return Config.gameSettings.ofSwampColors;
    }
    
    public static boolean isRandomMobs() {
        return Config.gameSettings.ofRandomMobs;
    }
    
    public static void checkGlError(final String p_checkGlError_0_) {
        final int i = GL11.glGetError();
        if (i != 0) {
            final String s = GLU.gluErrorString(i);
            error("OpenGlError: " + i + " (" + s + "), at: " + p_checkGlError_0_);
        }
    }
    
    public static boolean isSmoothBiomes() {
        return Config.gameSettings.ofSmoothBiomes;
    }
    
    public static boolean isCustomColors() {
        return Config.gameSettings.ofCustomColors;
    }
    
    public static boolean isCustomSky() {
        return Config.gameSettings.ofCustomSky;
    }
    
    public static boolean isCustomFonts() {
        return Config.gameSettings.ofCustomFonts;
    }
    
    public static boolean isShowCapes() {
        return Config.gameSettings.ofShowCapes;
    }
    
    public static boolean isConnectedTextures() {
        return Config.gameSettings.ofConnectedTextures != 3;
    }
    
    public static boolean isNaturalTextures() {
        return Config.gameSettings.ofNaturalTextures;
    }
    
    public static boolean isConnectedTexturesFancy() {
        return Config.gameSettings.ofConnectedTextures == 2;
    }
    
    public static boolean isFastRender() {
        return Config.gameSettings.ofFastRender;
    }
    
    public static boolean isTranslucentBlocksFancy() {
        return (Config.gameSettings.ofTranslucentBlocks == 0) ? Config.gameSettings.fancyGraphics : (Config.gameSettings.ofTranslucentBlocks == 2);
    }
    
    public static boolean isShaders() {
        return Shaders.shaderPackLoaded;
    }
    
    public static String[] readLines(final File p_readLines_0_) throws IOException {
        final FileInputStream fileinputstream = new FileInputStream(p_readLines_0_);
        return readLines(fileinputstream);
    }
    
    public static String[] readLines(final InputStream p_readLines_0_) throws IOException {
        final List list = new ArrayList();
        final InputStreamReader inputstreamreader = new InputStreamReader(p_readLines_0_, "ASCII");
        final BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
        while (true) {
            final String s = bufferedreader.readLine();
            if (s == null) {
                break;
            }
            list.add(s);
        }
        final String[] astring = list.toArray(new String[list.size()]);
        return astring;
    }
    
    public static String readFile(final File p_readFile_0_) throws IOException {
        final FileInputStream fileinputstream = new FileInputStream(p_readFile_0_);
        return readInputStream(fileinputstream, "ASCII");
    }
    
    public static String readInputStream(final InputStream p_readInputStream_0_) throws IOException {
        return readInputStream(p_readInputStream_0_, "ASCII");
    }
    
    public static String readInputStream(final InputStream p_readInputStream_0_, final String p_readInputStream_1_) throws IOException {
        final InputStreamReader inputstreamreader = new InputStreamReader(p_readInputStream_0_, p_readInputStream_1_);
        final BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
        final StringBuffer stringbuffer = new StringBuffer();
        while (true) {
            final String s = bufferedreader.readLine();
            if (s == null) {
                break;
            }
            stringbuffer.append(s);
            stringbuffer.append("\n");
        }
        return stringbuffer.toString();
    }
    
    public static byte[] readAll(final InputStream p_readAll_0_) throws IOException {
        final ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        final byte[] abyte = new byte[1024];
        while (true) {
            final int i = p_readAll_0_.read(abyte);
            if (i < 0) {
                break;
            }
            bytearrayoutputstream.write(abyte, 0, i);
        }
        p_readAll_0_.close();
        final byte[] abyte2 = bytearrayoutputstream.toByteArray();
        return abyte2;
    }
    
    public static GameSettings getGameSettings() {
        return Config.gameSettings;
    }
    
    public static String getNewRelease() {
        return Config.newRelease;
    }
    
    public static void setNewRelease(final String p_setNewRelease_0_) {
        Config.newRelease = p_setNewRelease_0_;
    }
    
    public static int compareRelease(final String p_compareRelease_0_, final String p_compareRelease_1_) {
        final String[] astring = splitRelease(p_compareRelease_0_);
        final String[] astring2 = splitRelease(p_compareRelease_1_);
        final String s = astring[0];
        final String s2 = astring2[0];
        if (!s.equals(s2)) {
            return s.compareTo(s2);
        }
        final int i = parseInt(astring[1], -1);
        final int j = parseInt(astring2[1], -1);
        if (i != j) {
            return i - j;
        }
        final String s3 = astring[2];
        final String s4 = astring2[2];
        if (!s3.equals(s4)) {
            if (s3.isEmpty()) {
                return 1;
            }
            if (s4.isEmpty()) {
                return -1;
            }
        }
        return s3.compareTo(s4);
    }
    
    private static String[] splitRelease(final String p_splitRelease_0_) {
        if (p_splitRelease_0_ == null || p_splitRelease_0_.length() <= 0) {
            return new String[] { "", "", "" };
        }
        final Pattern pattern = Pattern.compile("([A-Z])([0-9]+)(.*)");
        final Matcher matcher = pattern.matcher(p_splitRelease_0_);
        if (!matcher.matches()) {
            return new String[] { "", "", "" };
        }
        final String s = normalize(matcher.group(1));
        final String s2 = normalize(matcher.group(2));
        final String s3 = normalize(matcher.group(3));
        return new String[] { s, s2, s3 };
    }
    
    public static int intHash(int p_intHash_0_) {
        p_intHash_0_ = (p_intHash_0_ ^ 0x3D ^ p_intHash_0_ >> 16);
        p_intHash_0_ += p_intHash_0_ << 3;
        p_intHash_0_ ^= p_intHash_0_ >> 4;
        p_intHash_0_ *= 668265261;
        p_intHash_0_ ^= p_intHash_0_ >> 15;
        return p_intHash_0_;
    }
    
    public static int getRandom(final BlockPos p_getRandom_0_, final int p_getRandom_1_) {
        int i = intHash(p_getRandom_1_ + 37);
        i = intHash(i + p_getRandom_0_.getX());
        i = intHash(i + p_getRandom_0_.getZ());
        i = intHash(i + p_getRandom_0_.getY());
        return i;
    }
    
    public static WorldServer getWorldServer() {
        final World world = Config.minecraft.theWorld;
        if (world == null) {
            return null;
        }
        if (!Config.minecraft.isIntegratedServerRunning()) {
            return null;
        }
        final IntegratedServer integratedserver = Config.minecraft.getIntegratedServer();
        if (integratedserver == null) {
            return null;
        }
        final WorldProvider worldprovider = world.provider;
        if (worldprovider == null) {
            return null;
        }
        final int i = worldprovider.getDimensionId();
        try {
            final WorldServer worldserver = integratedserver.worldServerForDimension(i);
            return worldserver;
        }
        catch (NullPointerException var5) {
            return null;
        }
    }
    
    public static int getAvailableProcessors() {
        return Config.availableProcessors;
    }
    
    public static void updateAvailableProcessors() {
        Config.availableProcessors = Runtime.getRuntime().availableProcessors();
    }
    
    public static boolean isSingleProcessor() {
        return getAvailableProcessors() <= 1;
    }
    
    public static boolean isSmoothWorld() {
        return Config.gameSettings.ofSmoothWorld;
    }
    
    public static boolean isLazyChunkLoading() {
        return isSingleProcessor() && Config.gameSettings.ofLazyChunkLoading;
    }
    
    public static boolean isDynamicFov() {
        return Config.gameSettings.ofDynamicFov;
    }
    
    public static boolean isAlternateBlocks() {
        return Config.gameSettings.allowBlockAlternatives;
    }
    
    public static int getChunkViewDistance() {
        if (Config.gameSettings == null) {
            return 10;
        }
        final int i = Config.gameSettings.renderDistanceChunks;
        return i;
    }
    
    public static boolean equals(final Object p_equals_0_, final Object p_equals_1_) {
        return p_equals_0_ == p_equals_1_ || (p_equals_0_ != null && p_equals_0_.equals(p_equals_1_));
    }
    
    public static boolean equalsOne(final Object p_equalsOne_0_, final Object[] p_equalsOne_1_) {
        if (p_equalsOne_1_ == null) {
            return false;
        }
        for (int i = 0; i < p_equalsOne_1_.length; ++i) {
            final Object object = p_equalsOne_1_[i];
            if (equals(p_equalsOne_0_, object)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isSameOne(final Object p_isSameOne_0_, final Object[] p_isSameOne_1_) {
        if (p_isSameOne_1_ == null) {
            return false;
        }
        for (int i = 0; i < p_isSameOne_1_.length; ++i) {
            final Object object = p_isSameOne_1_[i];
            if (p_isSameOne_0_ == object) {
                return true;
            }
        }
        return false;
    }
    
    public static String normalize(final String p_normalize_0_) {
        return (p_normalize_0_ == null) ? "" : p_normalize_0_;
    }
    
    public static void checkDisplaySettings() {
        final int i = getAntialiasingLevel();
        if (i > 0) {
            final DisplayMode displaymode = Display.getDisplayMode();
            dbg("FSAA Samples: " + i);
            try {
                Display.destroy();
                Display.setDisplayMode(displaymode);
                Display.create(new PixelFormat().withDepthBits(24).withSamples(i));
                Display.setResizable(false);
                Display.setResizable(true);
            }
            catch (LWJGLException lwjglexception2) {
                warn("Error setting FSAA: " + i + "x");
                lwjglexception2.printStackTrace();
                try {
                    Display.setDisplayMode(displaymode);
                    Display.create(new PixelFormat().withDepthBits(24));
                    Display.setResizable(false);
                    Display.setResizable(true);
                }
                catch (LWJGLException lwjglexception3) {
                    lwjglexception3.printStackTrace();
                    try {
                        Display.setDisplayMode(displaymode);
                        Display.create();
                        Display.setResizable(false);
                        Display.setResizable(true);
                    }
                    catch (LWJGLException lwjglexception4) {
                        lwjglexception4.printStackTrace();
                    }
                }
            }
            if (!Minecraft.isRunningOnMac && getDefaultResourcePack() != null) {
                InputStream inputstream = null;
                InputStream inputstream2 = null;
                try {
                    inputstream = getDefaultResourcePack().getInputStreamAssets(new ResourceLocation("icons/icon_16x16.png"));
                    inputstream2 = getDefaultResourcePack().getInputStreamAssets(new ResourceLocation("icons/icon_32x32.png"));
                    if (inputstream != null && inputstream2 != null) {
                        Display.setIcon(new ByteBuffer[] { readIconImage(inputstream), readIconImage(inputstream2) });
                    }
                }
                catch (IOException ioexception) {
                    warn("Error setting window icon: " + ioexception.getClass().getName() + ": " + ioexception.getMessage());
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
    }
    
    private static ByteBuffer readIconImage(final InputStream p_readIconImage_0_) throws IOException {
        final BufferedImage bufferedimage = ImageIO.read(p_readIconImage_0_);
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
    
    public static void checkDisplayMode() {
        try {
            if (Config.minecraft.isFullScreen()) {
                if (Config.fullscreenModeChecked) {
                    return;
                }
                Config.fullscreenModeChecked = true;
                Config.desktopModeChecked = false;
                final DisplayMode displaymode = Display.getDisplayMode();
                final Dimension dimension = getFullscreenDimension();
                if (dimension == null) {
                    return;
                }
                if (displaymode.getWidth() == dimension.width && displaymode.getHeight() == dimension.height) {
                    return;
                }
                final DisplayMode displaymode2 = getDisplayMode(dimension);
                if (displaymode2 == null) {
                    return;
                }
                Display.setDisplayMode(displaymode2);
                Minecraft.displayWidth = Display.getDisplayMode().getWidth();
                Minecraft.displayHeight = Display.getDisplayMode().getHeight();
                if (Minecraft.displayWidth <= 0) {
                    Minecraft.displayWidth = 1;
                }
                if (Minecraft.displayHeight <= 0) {
                    Minecraft.displayHeight = 1;
                }
                if (Config.minecraft.currentScreen != null) {
                    final ScaledResolution scaledresolution = new ScaledResolution(Config.minecraft);
                    final int i = scaledresolution.getScaledWidth();
                    final int j = scaledresolution.getScaledHeight();
                    Config.minecraft.currentScreen.setWorldAndResolution(Config.minecraft, i, j);
                }
                Config.minecraft.loadingScreen = new LoadingScreenRenderer(Config.minecraft);
                updateFramebufferSize();
                Display.setFullscreen(true);
                Config.minecraft.gameSettings.updateVSync();
                GlStateManager.enableTexture2D();
            }
            else {
                if (Config.desktopModeChecked) {
                    return;
                }
                Config.desktopModeChecked = true;
                Config.fullscreenModeChecked = false;
                Config.minecraft.gameSettings.updateVSync();
                Display.update();
                GlStateManager.enableTexture2D();
                Display.setResizable(false);
                Display.setResizable(true);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            Config.gameSettings.ofFullscreenMode = "Default";
            Config.gameSettings.saveOfOptions();
        }
    }
    
    public static void updateFramebufferSize() {
        Config.minecraft.getFramebuffer().createBindFramebuffer(Minecraft.displayWidth, Minecraft.displayHeight);
        if (Config.minecraft.entityRenderer != null) {
            Config.minecraft.entityRenderer.updateShaderGroupSize(Minecraft.displayWidth, Minecraft.displayHeight);
        }
    }
    
    public static Object[] addObjectToArray(final Object[] p_addObjectToArray_0_, final Object p_addObjectToArray_1_) {
        if (p_addObjectToArray_0_ == null) {
            throw new NullPointerException("The given array is NULL");
        }
        final int i = p_addObjectToArray_0_.length;
        final int j = i + 1;
        final Object[] aobject = (Object[])Array.newInstance(p_addObjectToArray_0_.getClass().getComponentType(), j);
        System.arraycopy(p_addObjectToArray_0_, 0, aobject, 0, i);
        aobject[i] = p_addObjectToArray_1_;
        return aobject;
    }
    
    public static Object[] addObjectToArray(final Object[] p_addObjectToArray_0_, final Object p_addObjectToArray_1_, final int p_addObjectToArray_2_) {
        final List list = new ArrayList(Arrays.asList(p_addObjectToArray_0_));
        list.add(p_addObjectToArray_2_, p_addObjectToArray_1_);
        final Object[] aobject = (Object[])Array.newInstance(p_addObjectToArray_0_.getClass().getComponentType(), list.size());
        return list.toArray(aobject);
    }
    
    public static Object[] addObjectsToArray(final Object[] p_addObjectsToArray_0_, final Object[] p_addObjectsToArray_1_) {
        if (p_addObjectsToArray_0_ == null) {
            throw new NullPointerException("The given array is NULL");
        }
        if (p_addObjectsToArray_1_.length == 0) {
            return p_addObjectsToArray_0_;
        }
        final int i = p_addObjectsToArray_0_.length;
        final int j = i + p_addObjectsToArray_1_.length;
        final Object[] aobject = (Object[])Array.newInstance(p_addObjectsToArray_0_.getClass().getComponentType(), j);
        System.arraycopy(p_addObjectsToArray_0_, 0, aobject, 0, i);
        System.arraycopy(p_addObjectsToArray_1_, 0, aobject, i, p_addObjectsToArray_1_.length);
        return aobject;
    }
    
    public static boolean isCustomItems() {
        return Config.gameSettings.ofCustomItems;
    }
    
    public static void drawFps() {
        final int i = Minecraft.getDebugFPS();
        final String s = getUpdates(Config.minecraft.debug);
        final int j = Config.minecraft.renderGlobal.getCountActiveRenderers();
        final int k = Config.minecraft.renderGlobal.getCountEntitiesRendered();
        final int l = Config.minecraft.renderGlobal.getCountTileEntitiesRendered();
        final String s2 = i + " fps, C: " + j + ", E: " + k + "+" + l + ", U: " + s;
        Config.minecraft.fontRendererObj.drawString(s2, 2.0, 2.0, -2039584);
    }
    
    private static String getUpdates(final String p_getUpdates_0_) {
        final int i = p_getUpdates_0_.indexOf(40);
        if (i < 0) {
            return "";
        }
        final int j = p_getUpdates_0_.indexOf(32, i);
        return (j < 0) ? "" : p_getUpdates_0_.substring(i + 1, j);
    }
    
    public static int getBitsOs() {
        final String s = System.getenv("ProgramFiles(X86)");
        return (s != null) ? 64 : 32;
    }
    
    public static int getBitsJre() {
        final String[] astring = { "sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch" };
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final String s2 = System.getProperty(s);
            if (s2 != null && s2.contains("64")) {
                return 64;
            }
        }
        return 32;
    }
    
    public static boolean isNotify64BitJava() {
        return Config.notify64BitJava;
    }
    
    public static void setNotify64BitJava(final boolean p_setNotify64BitJava_0_) {
        Config.notify64BitJava = p_setNotify64BitJava_0_;
    }
    
    public static boolean isConnectedModels() {
        return false;
    }
    
    public static void showGuiMessage(final String p_showGuiMessage_0_, final String p_showGuiMessage_1_) {
        final GuiMessage guimessage = new GuiMessage(Config.minecraft.currentScreen, p_showGuiMessage_0_, p_showGuiMessage_1_);
        Config.minecraft.displayGuiScreen(guimessage);
    }
    
    public static int[] addIntToArray(final int[] p_addIntToArray_0_, final int p_addIntToArray_1_) {
        return addIntsToArray(p_addIntToArray_0_, new int[] { p_addIntToArray_1_ });
    }
    
    public static int[] addIntsToArray(final int[] p_addIntsToArray_0_, final int[] p_addIntsToArray_1_) {
        if (p_addIntsToArray_0_ != null && p_addIntsToArray_1_ != null) {
            final int i = p_addIntsToArray_0_.length;
            final int j = i + p_addIntsToArray_1_.length;
            final int[] aint = new int[j];
            System.arraycopy(p_addIntsToArray_0_, 0, aint, 0, i);
            for (int k = 0; k < p_addIntsToArray_1_.length; ++k) {
                aint[k + i] = p_addIntsToArray_1_[k];
            }
            return aint;
        }
        throw new NullPointerException("The given array is NULL");
    }
    
    public static DynamicTexture getMojangLogoTexture(final DynamicTexture p_getMojangLogoTexture_0_) {
        try {
            final ResourceLocation resourcelocation = new ResourceLocation("textures/gui/title/mojang.png");
            final InputStream inputstream = getResourceStream(resourcelocation);
            if (inputstream == null) {
                return p_getMojangLogoTexture_0_;
            }
            final BufferedImage bufferedimage = ImageIO.read(inputstream);
            if (bufferedimage == null) {
                return p_getMojangLogoTexture_0_;
            }
            final DynamicTexture dynamictexture = new DynamicTexture(bufferedimage);
            return dynamictexture;
        }
        catch (Exception exception) {
            warn(String.valueOf(exception.getClass().getName()) + ": " + exception.getMessage());
            return p_getMojangLogoTexture_0_;
        }
    }
    
    public static void writeFile(final File p_writeFile_0_, final String p_writeFile_1_) throws IOException {
        final FileOutputStream fileoutputstream = new FileOutputStream(p_writeFile_0_);
        final byte[] abyte = p_writeFile_1_.getBytes("ASCII");
        fileoutputstream.write(abyte);
        fileoutputstream.close();
    }
    
    public static TextureMap getTextureMap() {
        return getMinecraft().getTextureMapBlocks();
    }
    
    public static boolean isDynamicLights() {
        return Config.gameSettings.ofDynamicLights != 3;
    }
    
    public static boolean isDynamicLightsFast() {
        return Config.gameSettings.ofDynamicLights == 1;
    }
    
    public static boolean isDynamicHandLight() {
        return isDynamicLights() && (!isShaders() || Shaders.isDynamicHandLight());
    }
}
