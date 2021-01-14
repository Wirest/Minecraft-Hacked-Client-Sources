package optifine;

import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.*;
import net.minecraft.client.resources.ResourcePackRepository.Entry;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;
import shadersmod.client.Shaders;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {
    public static final String OF_NAME = "OptiFine";
    public static final String MC_VERSION = "1.8.8";
    public static final String OF_EDITION = "HD_U";
    public static final String OF_RELEASE = "H8";
    public static final String VERSION = "OptiFine_1.8.8_HD_U_H8";
    public static final Float DEF_ALPHA_FUNC_LEVEL = Float.valueOf(0.1F);
    private static final Logger LOGGER = LogManager.getLogger();
    public static String openGlVersion = null;
    public static String openGlRenderer = null;
    public static String openGlVendor = null;
    public static String[] openGlExtensions = null;
    public static GlVersion glVersion = null;
    public static GlVersion glslVersion = null;
    public static int minecraftVersionInt = -1;
    public static boolean fancyFogAvailable = false;
    public static boolean occlusionAvailable = false;
    public static boolean zoomMode = false;
    public static boolean waterOpacityChanged = false;
    private static String newRelease = null;
    private static boolean notify64BitJava = false;
    private static GameSettings gameSettings = null;
    private static Minecraft minecraft = Minecraft.getMinecraft();
    private static boolean initialized = false;
    private static Thread minecraftThread = null;
    private static DisplayMode desktopDisplayMode = null;
    private static DisplayMode[] displayModes = null;
    private static int antialiasingLevel = 0;
    private static int availableProcessors = 0;
    private static int texturePackClouds = 0;
    private static boolean fullscreenModeChecked = false;
    private static boolean desktopModeChecked = false;
    private static DefaultResourcePack defaultResourcePackLazy = null;

    public static String getVersion() {
        return "OptiFine_1.8.8_HD_U_H8";
    }

    public static String getVersionDebug() {
        StringBuffer localStringBuffer = new StringBuffer(32);
        if (isDynamicLights()) {
            localStringBuffer.append("DL: ");
            localStringBuffer.append(String.valueOf(DynamicLights.getCount()));
            localStringBuffer.append(", ");
        }
        localStringBuffer.append("OptiFine_1.8.8_HD_U_H8");
        String str = Shaders.getShaderPackName();
        if (str != null) {
            localStringBuffer.append(", ");
            localStringBuffer.append(str);
        }
        return localStringBuffer.toString();
    }

    public static void initGameSettings(GameSettings paramGameSettings) {
        if (gameSettings == null) {
            gameSettings = paramGameSettings;
            desktopDisplayMode = Display.getDesktopDisplayMode();
            updateAvailableProcessors();
            ReflectorForge.putLaunchBlackboard("optifine.ForgeSplashCompatible", Boolean.TRUE);
        }
    }

    public static void initDisplay() {
        checkInitialized();
        antialiasingLevel = gameSettings.ofAaLevel;
        checkDisplaySettings();
        checkDisplayMode();
        minecraftThread = Thread.currentThread();
        updateThreadPriorities();
        Shaders.startup(Minecraft.getMinecraft());
    }

    public static void checkInitialized() {
        if ((!initialized) && (Display.isCreated())) {
            initialized = true;
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
        openGlVersion = GL11.glGetString(7938);
        openGlRenderer = GL11.glGetString(7937);
        openGlVendor = GL11.glGetString(7936);
        log("OpenGL: " + openGlRenderer + ", version " + openGlVersion + ", " + openGlVendor);
        log("OpenGL Version: " + getOpenGlVersionString());
        if (!GLContext.getCapabilities().OpenGL12) {
            log("OpenGL Mipmap levels: Not available (GL12.GL_TEXTURE_MAX_LEVEL)");
        }
        fancyFogAvailable = GLContext.getCapabilities().GL_NV_fog_distance;
        if (!fancyFogAvailable) {
            log("OpenGL Fancy fog: Not available (GL_NV_fog_distance)");
        }
        occlusionAvailable = GLContext.getCapabilities().GL_ARB_occlusion_query;
        if (!occlusionAvailable) {
            log("OpenGL Occlussion culling: Not available (GL_ARB_occlusion_query)");
        }
        int i = TextureUtils.getGLMaximumTextureSize();
        dbg("Maximum texture size: " + i + "x" + i);
    }

    private static String getBuild() {
        try {
            InputStream localInputStream = Config.class.getResourceAsStream("/buildof.txt");
            if (localInputStream == null) {
                return null;
            }
            String str = readLines(localInputStream)[0];
            return str;
        } catch (Exception localException) {
            warn("" + localException.getClass().getName() + ": " + localException.getMessage());
        }
        return null;
    }

    public static boolean isFancyFogAvailable() {
        return fancyFogAvailable;
    }

    public static boolean isOcclusionAvailable() {
        return occlusionAvailable;
    }

    public static int getMinecraftVersionInt() {
        if (minecraftVersionInt < 0) {
            String[] arrayOfString = tokenize("1.8.8", ".");
            int i = 0;
            if (arrayOfString.length > 0) {
                i |= 10000 * parseInt(arrayOfString[0], 0);
            }
            if (arrayOfString.length > 1) {
                i |= 100 * parseInt(arrayOfString[1], 0);
            }
            if (arrayOfString.length > 2) {
                i |= 1 * parseInt(arrayOfString[2], 0);
            }
            minecraftVersionInt = i;
        }
        return minecraftVersionInt;
    }

    public static String getOpenGlVersionString() {
        GlVersion localGlVersion = getGlVersion();
        String str = "" + localGlVersion.getMajor() + "." + localGlVersion.getMinor() + "." + localGlVersion.getRelease();
        return str;
    }

    private static GlVersion getGlVersionLwjgl() {
        return GLContext.getCapabilities().OpenGL11 ? new GlVersion(1, 1) : GLContext.getCapabilities().OpenGL12 ? new GlVersion(1, 2) : GLContext.getCapabilities().OpenGL13 ? new GlVersion(1, 3) : GLContext.getCapabilities().OpenGL14 ? new GlVersion(1, 4) : GLContext.getCapabilities().OpenGL15 ? new GlVersion(1, 5) : GLContext.getCapabilities().OpenGL20 ? new GlVersion(2, 0) : GLContext.getCapabilities().OpenGL21 ? new GlVersion(2, 1) : GLContext.getCapabilities().OpenGL30 ? new GlVersion(3, 0) : GLContext.getCapabilities().OpenGL31 ? new GlVersion(3, 1) : GLContext.getCapabilities().OpenGL32 ? new GlVersion(3, 2) : GLContext.getCapabilities().OpenGL33 ? new GlVersion(3, 3) : GLContext.getCapabilities().OpenGL40 ? new GlVersion(4, 0) : GLContext.getCapabilities().OpenGL41 ? new GlVersion(4, 1) : GLContext.getCapabilities().OpenGL42 ? new GlVersion(4, 2) : GLContext.getCapabilities().OpenGL43 ? new GlVersion(4, 3) : GLContext.getCapabilities().OpenGL44 ? new GlVersion(4, 4) : new GlVersion(1, 0);
    }

    public static GlVersion getGlVersion() {
        if (glVersion == null) {
            String str = GL11.glGetString(7938);
            glVersion = parseGlVersion(str, (GlVersion) null);
            if (glVersion == null) {
                glVersion = getGlVersionLwjgl();
            }
            if (glVersion == null) {
                glVersion = new GlVersion(1, 0);
            }
        }
        return glVersion;
    }

    public static GlVersion getGlslVersion() {
        if (glslVersion == null) {
            String str = GL11.glGetString(35724);
            glslVersion = parseGlVersion(str, (GlVersion) null);
            if (glslVersion == null) {
                glslVersion = new GlVersion(1, 10);
            }
        }
        return glslVersion;
    }

    public static GlVersion parseGlVersion(String paramString, GlVersion paramGlVersion) {
        try {
            if (paramString == null) {
                return paramGlVersion;
            }
            Pattern localPattern = Pattern.compile("([0-9]+)\\.([0-9]+)(\\.([0-9]+))?(.+)?");
            Matcher localMatcher = localPattern.matcher(paramString);
            if (!localMatcher.matches()) {
                return paramGlVersion;
            }
            int i = Integer.parseInt(localMatcher.group(1));
            int j = Integer.parseInt(localMatcher.group(2));
            int k = localMatcher.group(4) != null ? Integer.parseInt(localMatcher.group(4)) : 0;
            String str = localMatcher.group(5);
            return new GlVersion(i, j, k, str);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return paramGlVersion;
    }

    public static String[] getOpenGlExtensions() {
        if (openGlExtensions == null) {
            openGlExtensions = detectOpenGlExtensions();
        }
        return openGlExtensions;
    }

    private static String[] detectOpenGlExtensions() {
        try {
            GlVersion localGlVersion = getGlVersion();
            if (localGlVersion.getMajor() >= 3) {
                int i = GL11.glGetInteger(33309);
                if (i > 0) {
                    String[] arrayOfString2 = new String[i];
                    for (int j = 0; j < i; j++) {
                        arrayOfString2[j] = GL30.glGetStringi(7939, j);
                    }
                    return arrayOfString2;
                }
            }
        } catch (Exception localException1) {
            localException1.printStackTrace();
        }
        try {
            String str = GL11.glGetString(7939);
            String[] arrayOfString1 = str.split(" ");
            return arrayOfString1;
        } catch (Exception localException2) {
            localException2.printStackTrace();
        }
        return new String[0];
    }

    public static void updateThreadPriorities() {
        updateAvailableProcessors();
        int i = 8;
        if (isSingleProcessor()) {
            if (isSmoothWorld()) {
                minecraftThread.setPriority(10);
                setThreadPriority("Server thread", 1);
            } else {
                minecraftThread.setPriority(5);
                setThreadPriority("Server thread", 5);
            }
        } else {
            minecraftThread.setPriority(10);
            setThreadPriority("Server thread", 5);
        }
    }

    private static void setThreadPriority(String paramString, int paramInt) {
        try {
            ThreadGroup localThreadGroup = Thread.currentThread().getThreadGroup();
            if (localThreadGroup == null) {
                return;
            }
            int i = (localThreadGroup.activeCount() | 0xA) * 2;
            Thread[] arrayOfThread = new Thread[i];
            localThreadGroup.enumerate(arrayOfThread, false);
            for (int j = 0; j < arrayOfThread.length; j++) {
                Thread localThread = arrayOfThread[j];
                if ((localThread != null) && (localThread.getName().startsWith(paramString))) {
                    localThread.setPriority(paramInt);
                }
            }
        } catch (Throwable localThrowable) {
            warn(localThrowable.getClass().getName() + ": " + localThrowable.getMessage());
        }
    }

    public static boolean isMinecraftThread() {
        return Thread.currentThread() == minecraftThread;
    }

    private static void startVersionCheckThread() {
        VersionCheckThread localVersionCheckThread = new VersionCheckThread();
        localVersionCheckThread.start();
    }

    public static boolean isMipmaps() {
        return gameSettings.mipmapLevels > 0;
    }

    public static int getMipmapLevels() {
        return gameSettings.mipmapLevels;
    }

    public static int getMipmapType() {
        switch (gameSettings.ofMipmapType) {
            case 0:
                return 9986;
            case 1:
                return 9986;
            case 2:
                if (isMultiTexture()) {
                    return 9985;
                }
                return 9986;
            case 3:
                if (isMultiTexture()) {
                    return 9987;
                }
                return 9986;
        }
        return 9986;
    }

    public static boolean isUseAlphaFunc() {
        float f = getAlphaFuncLevel();
        return f > DEF_ALPHA_FUNC_LEVEL.floatValue() + 1.0E-5F;
    }

    public static float getAlphaFuncLevel() {
        return DEF_ALPHA_FUNC_LEVEL.floatValue();
    }

    public static boolean isFogFancy() {
        return isFancyFogAvailable();
    }

    public static boolean isFogFast() {
        return gameSettings.ofFogType == 1;
    }

    public static boolean isFogOff() {
        return gameSettings.ofFogType == 3;
    }

    public static float getFogStart() {
        return gameSettings.ofFogStart;
    }

    public static void dbg(String paramString) {
        LOGGER.info("[OptiFine] " + paramString);
    }

    public static void warn(String paramString) {
        LOGGER.warn("[OptiFine] " + paramString);
    }

    public static void error(String paramString) {
        LOGGER.error("[OptiFine] " + paramString);
    }

    public static void log(String paramString) {
        dbg(paramString);
    }

    public static int getUpdatesPerFrame() {
        return gameSettings.ofChunkUpdates;
    }

    public static boolean isDynamicUpdates() {
        return gameSettings.ofChunkUpdatesDynamic;
    }

    public static boolean isRainFancy() {
        return gameSettings.ofRain == 2 ? true : gameSettings.ofRain == 0 ? gameSettings.fancyGraphics : false;
    }

    public static boolean isRainOff() {
        return gameSettings.ofRain == 3;
    }

    public static boolean isCloudsFancy() {
        return texturePackClouds != 0 ? false : texturePackClouds == 2 ? true : (isShaders()) && (!Shaders.shaderPackClouds.isDefault()) ? Shaders.shaderPackClouds.isFancy() : gameSettings.ofClouds != 0 ? false : gameSettings.ofClouds == 2 ? true : gameSettings.fancyGraphics;
    }

    public static boolean isCloudsOff() {
        return gameSettings.ofClouds == 3;
    }

    public static void updateTexturePackClouds() {
        texturePackClouds = 0;
        IResourceManager localIResourceManager = getResourceManager();
        if (localIResourceManager != null) {
            try {
                InputStream localInputStream = localIResourceManager.getResource(new ResourceLocation("mcpatcher/color.properties")).getInputStream();
                if (localInputStream == null) {
                    return;
                }
                Properties localProperties = new Properties();
                localProperties.load(localInputStream);
                localInputStream.close();
                String str = localProperties.getProperty("clouds");
                if (str == null) {
                    return;
                }
                dbg("Texture pack clouds: " + str);
                str = str.toLowerCase();
                if (str.equals("fast")) {
                    texturePackClouds = 1;
                }
                if (str.equals("fancy")) {
                    texturePackClouds = 2;
                }
                if (str.equals("off")) {
                    texturePackClouds = 3;
                }
            } catch (Exception localException) {
            }
        }
    }

    public static ModelManager getModelManager() {
        return minecraft.getRenderItem().modelManager;
    }

    public static boolean isTreesFancy() {
        return gameSettings.ofTrees != 1 ? true : gameSettings.ofTrees == 0 ? gameSettings.fancyGraphics : false;
    }

    public static boolean isTreesSmart() {
        return gameSettings.ofTrees == 4;
    }

    public static boolean isCullFacesLeaves() {
        return !gameSettings.fancyGraphics;
    }

    public static boolean isDroppedItemsFancy() {
        return gameSettings.ofDroppedItems == 2 ? true : gameSettings.ofDroppedItems == 0 ? gameSettings.fancyGraphics : false;
    }

    public static int limit(int paramInt1, int paramInt2, int paramInt3) {
        return paramInt1 > paramInt3 ? paramInt3 : paramInt1 < paramInt2 ? paramInt2 : paramInt1;
    }

    public static float limit(float paramFloat1, float paramFloat2, float paramFloat3) {
        return paramFloat1 > paramFloat3 ? paramFloat3 : paramFloat1 < paramFloat2 ? paramFloat2 : paramFloat1;
    }

    public static double limit(double paramDouble1, double paramDouble2, double paramDouble3) {
        return paramDouble1 > paramDouble3 ? paramDouble3 : paramDouble1 < paramDouble2 ? paramDouble2 : paramDouble1;
    }

    public static float limitTo1(float paramFloat) {
        return paramFloat > 1.0F ? 1.0F : paramFloat < 0.0F ? 0.0F : paramFloat;
    }

    public static boolean isAnimatedWater() {
        return gameSettings.ofAnimatedWater != 2;
    }

    public static boolean isGeneratedWater() {
        return gameSettings.ofAnimatedWater == 1;
    }

    public static boolean isAnimatedPortal() {
        return gameSettings.ofAnimatedPortal;
    }

    public static boolean isAnimatedLava() {
        return gameSettings.ofAnimatedLava != 2;
    }

    public static boolean isGeneratedLava() {
        return gameSettings.ofAnimatedLava == 1;
    }

    public static boolean isAnimatedFire() {
        return gameSettings.ofAnimatedFire;
    }

    public static boolean isAnimatedRedstone() {
        return gameSettings.ofAnimatedRedstone;
    }

    public static boolean isAnimatedExplosion() {
        return gameSettings.ofAnimatedExplosion;
    }

    public static boolean isAnimatedFlame() {
        return gameSettings.ofAnimatedFlame;
    }

    public static boolean isAnimatedSmoke() {
        return gameSettings.ofAnimatedSmoke;
    }

    public static boolean isVoidParticles() {
        return gameSettings.ofVoidParticles;
    }

    public static boolean isWaterParticles() {
        return gameSettings.ofWaterParticles;
    }

    public static boolean isRainSplash() {
        return gameSettings.ofRainSplash;
    }

    public static boolean isPortalParticles() {
        return gameSettings.ofPortalParticles;
    }

    public static boolean isPotionParticles() {
        return gameSettings.ofPotionParticles;
    }

    public static boolean isFireworkParticles() {
        return gameSettings.ofFireworkParticles;
    }

    public static float getAmbientOcclusionLevel() {
        return (isShaders()) && (Shaders.aoLevel >= 0.0F) ? Shaders.aoLevel : gameSettings.ofAoLevel;
    }

    public static String arrayToString(Object[] paramArrayOfObject) {
        if (paramArrayOfObject == null) {
            return "";
        }
        StringBuffer localStringBuffer = new StringBuffer(paramArrayOfObject.length * 5);
        for (int i = 0; i < paramArrayOfObject.length; i++) {
            Object localObject = paramArrayOfObject[i];
            if (i > 0) {
                localStringBuffer.append(", ");
            }
            localStringBuffer.append(String.valueOf(localObject));
        }
        return localStringBuffer.toString();
    }

    public static String arrayToString(int[] paramArrayOfInt) {
        if (paramArrayOfInt == null) {
            return "";
        }
        StringBuffer localStringBuffer = new StringBuffer(paramArrayOfInt.length * 5);
        for (int i = 0; i < paramArrayOfInt.length; i++) {
            int j = paramArrayOfInt[i];
            if (i > 0) {
                localStringBuffer.append(", ");
            }
            localStringBuffer.append(String.valueOf(j));
        }
        return localStringBuffer.toString();
    }

    public static Minecraft getMinecraft() {
        return minecraft;
    }

    public static TextureManager getTextureManager() {
        return minecraft.getTextureManager();
    }

    public static IResourceManager getResourceManager() {
        return minecraft.getResourceManager();
    }

    public static InputStream getResourceStream(ResourceLocation paramResourceLocation)
            throws IOException {
        return getResourceStream(minecraft.getResourceManager(), paramResourceLocation);
    }

    public static InputStream getResourceStream(IResourceManager paramIResourceManager, ResourceLocation paramResourceLocation)
            throws IOException {
        IResource localIResource = paramIResourceManager.getResource(paramResourceLocation);
        return localIResource == null ? null : localIResource.getInputStream();
    }

    public static IResource getResource(ResourceLocation paramResourceLocation)
            throws IOException {
        return minecraft.getResourceManager().getResource(paramResourceLocation);
    }

    public static boolean hasResource(ResourceLocation paramResourceLocation) {
        IResourcePack localIResourcePack = getDefiningResourcePack(paramResourceLocation);
        return localIResourcePack != null;
    }

    public static boolean hasResource(IResourceManager paramIResourceManager, ResourceLocation paramResourceLocation) {
        try {
            IResource localIResource = paramIResourceManager.getResource(paramResourceLocation);
            return localIResource != null;
        } catch (IOException localIOException) {
        }
        return false;
    }

    public static IResourcePack[] getResourcePacks() {
        ResourcePackRepository localResourcePackRepository = minecraft.getResourcePackRepository();
        List localList = localResourcePackRepository.getRepositoryEntries();
        ArrayList localArrayList = new ArrayList();
        Object localObject1 = localList.iterator();
        while (((Iterator) localObject1).hasNext()) {
            Object localObject2 = ((Iterator) localObject1).next();
            localArrayList.add(((ResourcePackRepository.Entry) localObject2).getResourcePack());
        }
        if (localResourcePackRepository.getResourcePackInstance() != null) {
            localArrayList.add(localResourcePackRepository.getResourcePackInstance());
        }
        localObject1 = (IResourcePack[]) (IResourcePack[]) localArrayList.toArray(new IResourcePack[localArrayList.size()]);
        return (IResourcePack[]) localObject1;
    }

    public static String getResourcePackNames() {
        if (minecraft.getResourcePackRepository() == null) {
            return "";
        }
        IResourcePack[] arrayOfIResourcePack = getResourcePacks();
        if (arrayOfIResourcePack.length <= 0) {
            return getDefaultResourcePack().getPackName();
        }
        String[] arrayOfString = new String[arrayOfIResourcePack.length];
        for (int i = 0; i < arrayOfIResourcePack.length; i++) {
            arrayOfString[i] = arrayOfIResourcePack[i].getPackName();
        }
        String str = arrayToString((Object[]) arrayOfString);
        return str;
    }

    public static DefaultResourcePack getDefaultResourcePack() {
        if (defaultResourcePackLazy == null) {
            Minecraft localMinecraft = Minecraft.getMinecraft();
            defaultResourcePackLazy = (DefaultResourcePack) Reflector.getFieldValue(localMinecraft, Reflector.Minecraft_defaultResourcePack);
            if (defaultResourcePackLazy == null) {
                ResourcePackRepository localResourcePackRepository = localMinecraft.getResourcePackRepository();
                if (localResourcePackRepository != null) {
                    defaultResourcePackLazy = (DefaultResourcePack) localResourcePackRepository.rprDefaultResourcePack;
                }
            }
        }
        return defaultResourcePackLazy;
    }

    public static boolean isFromDefaultResourcePack(ResourceLocation paramResourceLocation) {
        IResourcePack localIResourcePack = getDefiningResourcePack(paramResourceLocation);
        return localIResourcePack == getDefaultResourcePack();
    }

    public static IResourcePack getDefiningResourcePack(ResourceLocation paramResourceLocation) {
        ResourcePackRepository localResourcePackRepository = minecraft.getResourcePackRepository();
        IResourcePack localIResourcePack1 = localResourcePackRepository.getResourcePackInstance();
        if ((localIResourcePack1 != null) && (localIResourcePack1.resourceExists(paramResourceLocation))) {
            return localIResourcePack1;
        }
        List localList = (List) Reflector.getFieldValue(localResourcePackRepository, Reflector.ResourcePackRepository_repositoryEntries);
        if (localList != null) {
            for (int i = localList.size() - 1; i >= 0; i--) {
                ResourcePackRepository.Entry localEntry = (ResourcePackRepository.Entry) localList.get(i);
                IResourcePack localIResourcePack2 = localEntry.getResourcePack();
                if (localIResourcePack2.resourceExists(paramResourceLocation)) {
                    return localIResourcePack2;
                }
            }
        }
        return getDefaultResourcePack().resourceExists(paramResourceLocation) ? getDefaultResourcePack() : null;
    }

    public static RenderGlobal getRenderGlobal() {
        return minecraft.renderGlobal;
    }

    public static boolean isBetterGrass() {
        return gameSettings.ofBetterGrass != 3;
    }

    public static boolean isBetterGrassFancy() {
        return gameSettings.ofBetterGrass == 2;
    }

    public static boolean isWeatherEnabled() {
        return gameSettings.ofWeather;
    }

    public static boolean isSkyEnabled() {
        return gameSettings.ofSky;
    }

    public static boolean isSunMoonEnabled() {
        return gameSettings.ofSunMoon;
    }

    public static boolean isSunTexture() {
        return isSunMoonEnabled();
    }

    public static boolean isMoonTexture() {
        return isSunMoonEnabled();
    }

    public static boolean isVignetteEnabled() {
        return (!isShaders()) || (Shaders.isVignette());
    }

    public static boolean isStarsEnabled() {
        return gameSettings.ofStars;
    }

    public static void sleep(long paramLong) {
        try {
            Thread.sleep(paramLong);
        } catch (InterruptedException localInterruptedException) {
            localInterruptedException.printStackTrace();
        }
    }

    public static boolean isTimeDayOnly() {
        return gameSettings.ofTime == 1;
    }

    public static boolean isTimeDefault() {
        return gameSettings.ofTime == 0;
    }

    public static boolean isTimeNightOnly() {
        return gameSettings.ofTime == 2;
    }

    public static boolean isClearWater() {
        return gameSettings.ofClearWater;
    }

    public static int getAnisotropicFilterLevel() {
        return gameSettings.ofAfLevel;
    }

    public static boolean isAnisotropicFiltering() {
        return getAnisotropicFilterLevel() > 1;
    }

    public static int getAntialiasingLevel() {
        return antialiasingLevel;
    }

    public static boolean isAntialiasing() {
        return getAntialiasingLevel() > 0;
    }

    public static boolean isAntialiasingConfigured() {
        return getGameSettings().ofAaLevel > 0;
    }

    public static boolean isMultiTexture() {
        return getAnisotropicFilterLevel() > 1;
    }

    public static boolean between(int paramInt1, int paramInt2, int paramInt3) {
        return (paramInt1 >= paramInt2) && (paramInt1 <= paramInt3);
    }

    public static boolean isDrippingWaterLava() {
        return gameSettings.ofDrippingWaterLava;
    }

    public static boolean isBetterSnow() {
        return gameSettings.ofBetterSnow;
    }

    public static Dimension getFullscreenDimension() {
        if (desktopDisplayMode == null) {
            return null;
        }
        if (gameSettings == null) {
            return new Dimension(desktopDisplayMode.getWidth(), desktopDisplayMode.getHeight());
        }
        String str = gameSettings.ofFullscreenMode;
        if (str.equals("Default")) {
            return new Dimension(desktopDisplayMode.getWidth(), desktopDisplayMode.getHeight());
        }
        String[] arrayOfString = tokenize(str, " x");
        return arrayOfString.length < 2 ? new Dimension(desktopDisplayMode.getWidth(), desktopDisplayMode.getHeight()) : new Dimension(parseInt(arrayOfString[0], -1), parseInt(arrayOfString[1], -1));
    }

    public static int parseInt(String paramString, int paramInt) {
        try {
            if (paramString == null) {
                return paramInt;
            }
            paramString = paramString.trim();
            return Integer.parseInt(paramString);
        } catch (NumberFormatException localNumberFormatException) {
        }
        return paramInt;
    }

    public static float parseFloat(String paramString, float paramFloat) {
        try {
            if (paramString == null) {
                return paramFloat;
            }
            paramString = paramString.trim();
            return Float.parseFloat(paramString);
        } catch (NumberFormatException localNumberFormatException) {
        }
        return paramFloat;
    }

    public static boolean parseBoolean(String paramString, boolean paramBoolean) {
        try {
            if (paramString == null) {
                return paramBoolean;
            }
            paramString = paramString.trim();
            return Boolean.parseBoolean(paramString);
        } catch (NumberFormatException localNumberFormatException) {
        }
        return paramBoolean;
    }

    public static String[] tokenize(String paramString1, String paramString2) {
        StringTokenizer localStringTokenizer = new StringTokenizer(paramString1, paramString2);
        ArrayList localArrayList = new ArrayList();
        while (localStringTokenizer.hasMoreTokens()) {
            localObject = localStringTokenizer.nextToken();
            localArrayList.add(localObject);
        }
        Object localObject = (String[]) (String[]) localArrayList.toArray(new String[localArrayList.size()]);
        return (String[]) localObject;
    }

    public static DisplayMode getDesktopDisplayMode() {
        return desktopDisplayMode;
    }

    public static DisplayMode[] getDisplayModes() {
        if (displayModes == null) {
            try {
                DisplayMode[] arrayOfDisplayMode1 = Display.getAvailableDisplayModes();
                Set localSet = getDisplayModeDimensions(arrayOfDisplayMode1);
                ArrayList localArrayList = new ArrayList();
                Object localObject = localSet.iterator();
                while (((Iterator) localObject).hasNext()) {
                    Dimension localDimension = (Dimension) ((Iterator) localObject).next();
                    DisplayMode[] arrayOfDisplayMode2 = getDisplayModes(arrayOfDisplayMode1, localDimension);
                    DisplayMode localDisplayMode = getDisplayMode(arrayOfDisplayMode2, desktopDisplayMode);
                    if (localDisplayMode != null) {
                        localArrayList.add(localDisplayMode);
                    }
                }
                localObject = (DisplayMode[]) (DisplayMode[]) localArrayList.toArray(new DisplayMode[localArrayList.size()]);
                Arrays.sort((Object[]) localObject, new DisplayModeComparator());
                return (DisplayMode[]) localObject;
            } catch (Exception localException) {
                localException.printStackTrace();
                displayModes = new DisplayMode[]{desktopDisplayMode};
            }
        }
        return displayModes;
    }

    public static DisplayMode getLargestDisplayMode() {
        DisplayMode[] arrayOfDisplayMode = getDisplayModes();
        if ((arrayOfDisplayMode != null) && (arrayOfDisplayMode.length >= 1)) {
            DisplayMode localDisplayMode = arrayOfDisplayMode[(arrayOfDisplayMode.length - 1)];
            return (desktopDisplayMode.getWidth() == localDisplayMode.getWidth()) && (desktopDisplayMode.getHeight() > localDisplayMode.getHeight()) ? desktopDisplayMode : desktopDisplayMode.getWidth() > localDisplayMode.getWidth() ? desktopDisplayMode : localDisplayMode;
        }
        return desktopDisplayMode;
    }

    private static Set<Dimension> getDisplayModeDimensions(DisplayMode[] paramArrayOfDisplayMode) {
        HashSet localHashSet = new HashSet();
        for (int i = 0; i < paramArrayOfDisplayMode.length; i++) {
            DisplayMode localDisplayMode = paramArrayOfDisplayMode[i];
            Dimension localDimension = new Dimension(localDisplayMode.getWidth(), localDisplayMode.getHeight());
            localHashSet.add(localDimension);
        }
        return localHashSet;
    }

    private static DisplayMode[] getDisplayModes(DisplayMode[] paramArrayOfDisplayMode, Dimension paramDimension) {
        ArrayList localArrayList = new ArrayList();
        for (int i = 0; i < paramArrayOfDisplayMode.length; i++) {
            DisplayMode localDisplayMode = paramArrayOfDisplayMode[i];
            if ((localDisplayMode.getWidth() == paramDimension.getWidth()) && (localDisplayMode.getHeight() == paramDimension.getHeight())) {
                localArrayList.add(localDisplayMode);
            }
        }
        DisplayMode[] arrayOfDisplayMode = (DisplayMode[]) (DisplayMode[]) localArrayList.toArray(new DisplayMode[localArrayList.size()]);
        return arrayOfDisplayMode;
    }

    private static DisplayMode getDisplayMode(DisplayMode[] paramArrayOfDisplayMode, DisplayMode paramDisplayMode) {
        if (paramDisplayMode != null) {
            for (int i = 0; i < paramArrayOfDisplayMode.length; i++) {
                DisplayMode localDisplayMode = paramArrayOfDisplayMode[i];
                if ((localDisplayMode.getBitsPerPixel() == paramDisplayMode.getBitsPerPixel()) && (localDisplayMode.getFrequency() == paramDisplayMode.getFrequency())) {
                    return localDisplayMode;
                }
            }
        }
        if (paramArrayOfDisplayMode.length <= 0) {
            return null;
        }
        Arrays.sort(paramArrayOfDisplayMode, new DisplayModeComparator());
        return paramArrayOfDisplayMode[(paramArrayOfDisplayMode.length - 1)];
    }

    public static String[] getDisplayModeNames() {
        DisplayMode[] arrayOfDisplayMode = getDisplayModes();
        String[] arrayOfString = new String[arrayOfDisplayMode.length];
        for (int i = 0; i < arrayOfDisplayMode.length; i++) {
            DisplayMode localDisplayMode = arrayOfDisplayMode[i];
            String str = "" + localDisplayMode.getWidth() + "x" + localDisplayMode.getHeight();
            arrayOfString[i] = str;
        }
        return arrayOfString;
    }

    public static DisplayMode getDisplayMode(Dimension paramDimension)
            throws LWJGLException {
        DisplayMode[] arrayOfDisplayMode = getDisplayModes();
        for (int i = 0; i < arrayOfDisplayMode.length; i++) {
            DisplayMode localDisplayMode = arrayOfDisplayMode[i];
            if ((localDisplayMode.getWidth() == paramDimension.width) && (localDisplayMode.getHeight() == paramDimension.height)) {
                return localDisplayMode;
            }
        }
        return desktopDisplayMode;
    }

    public static boolean isAnimatedTerrain() {
        return gameSettings.ofAnimatedTerrain;
    }

    public static boolean isAnimatedTextures() {
        return gameSettings.ofAnimatedTextures;
    }

    public static boolean isSwampColors() {
        return gameSettings.ofSwampColors;
    }

    public static boolean isRandomMobs() {
        return gameSettings.ofRandomMobs;
    }

    public static void checkGlError(String paramString) {
        int i = GL11.glGetError();
        if (i != 0) {
            String str = GLU.gluErrorString(i);
            error("OpenGlError: " + i + " (" + str + "), at: " + paramString);
        }
    }

    public static boolean isSmoothBiomes() {
        return gameSettings.ofSmoothBiomes;
    }

    public static boolean isCustomColors() {
        return gameSettings.ofCustomColors;
    }

    public static boolean isCustomSky() {
        return gameSettings.ofCustomSky;
    }

    public static boolean isCustomFonts() {
        return gameSettings.ofCustomFonts;
    }

    public static boolean isShowCapes() {
        return gameSettings.ofShowCapes;
    }

    public static boolean isConnectedTextures() {
        return gameSettings.ofConnectedTextures != 3;
    }

    public static boolean isNaturalTextures() {
        return gameSettings.ofNaturalTextures;
    }

    public static boolean isConnectedTexturesFancy() {
        return gameSettings.ofConnectedTextures == 2;
    }

    public static boolean isFastRender() {
        return gameSettings.ofFastRender;
    }

    public static boolean isTranslucentBlocksFancy() {
        return gameSettings.ofTranslucentBlocks == 2 ? true : gameSettings.ofTranslucentBlocks == 0 ? gameSettings.fancyGraphics : false;
    }

    public static boolean isShaders() {
        return Shaders.shaderPackLoaded;
    }

    public static String[] readLines(File paramFile)
            throws IOException {
        FileInputStream localFileInputStream = new FileInputStream(paramFile);
        return readLines(localFileInputStream);
    }

    public static String[] readLines(InputStream paramInputStream)
            throws IOException {
        ArrayList localArrayList = new ArrayList();
        InputStreamReader localInputStreamReader = new InputStreamReader(paramInputStream, "ASCII");
        BufferedReader localBufferedReader = new BufferedReader(localInputStreamReader);
        for (; ; ) {
            String str = localBufferedReader.readLine();
            if (str == null) {
                String[] arrayOfString = (String[]) (String[]) localArrayList.toArray(new String[localArrayList.size()]);
                return arrayOfString;
            }
            localArrayList.add(str);
        }
    }

    public static String readFile(File paramFile)
            throws IOException {
        FileInputStream localFileInputStream = new FileInputStream(paramFile);
        return readInputStream(localFileInputStream, "ASCII");
    }

    public static String readInputStream(InputStream paramInputStream)
            throws IOException {
        return readInputStream(paramInputStream, "ASCII");
    }

    public static String readInputStream(InputStream paramInputStream, String paramString)
            throws IOException {
        InputStreamReader localInputStreamReader = new InputStreamReader(paramInputStream, paramString);
        BufferedReader localBufferedReader = new BufferedReader(localInputStreamReader);
        StringBuffer localStringBuffer = new StringBuffer();
        for (; ; ) {
            String str = localBufferedReader.readLine();
            if (str == null) {
                return localStringBuffer.toString();
            }
            localStringBuffer.append(str);
            localStringBuffer.append("\n");
        }
    }

    public static byte[] readAll(InputStream paramInputStream)
            throws IOException {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        byte[] arrayOfByte1 = new byte['Ѐ'];
        for (; ; ) {
            int i = paramInputStream.read(arrayOfByte1);
            if (i < 0) {
                paramInputStream.close();
                byte[] arrayOfByte2 = localByteArrayOutputStream.toByteArray();
                return arrayOfByte2;
            }
            localByteArrayOutputStream.write(arrayOfByte1, 0, i);
        }
    }

    public static GameSettings getGameSettings() {
        return gameSettings;
    }

    public static String getNewRelease() {
        return newRelease;
    }

    public static void setNewRelease(String paramString) {
        newRelease = paramString;
    }

    public static int compareRelease(String paramString1, String paramString2) {
        String[] arrayOfString1 = splitRelease(paramString1);
        String[] arrayOfString2 = splitRelease(paramString2);
        String str1 = arrayOfString1[0];
        String str2 = arrayOfString2[0];
        if (!str1.equals(str2)) {
            return str1.compareTo(str2);
        }
        int i = parseInt(arrayOfString1[1], -1);
        int j = parseInt(arrayOfString2[1], -1);
        if (i != j) {
            return i - j;
        }
        String str3 = arrayOfString1[2];
        String str4 = arrayOfString2[2];
        if (!str3.equals(str4)) {
            if (str3.isEmpty()) {
                return 1;
            }
            if (str4.isEmpty()) {
                return -1;
            }
        }
        return str3.compareTo(str4);
    }

    private static String[] splitRelease(String paramString) {
        if ((paramString != null) && (paramString.length() > 0)) {
            Pattern localPattern = Pattern.compile("([A-Z])([0-9]+)(.*)");
            Matcher localMatcher = localPattern.matcher(paramString);
            if (!localMatcher.matches()) {
                return new String[]{"", "", ""};
            }
            String str1 = normalize(localMatcher.group(1));
            String str2 = normalize(localMatcher.group(2));
            String str3 = normalize(localMatcher.group(3));
            return new String[]{str1, str2, str3};
        }
        return new String[]{"", "", ""};
    }

    public static int intHash(int paramInt) {
        paramInt = paramInt + 61 + (paramInt & 0x10);
        paramInt |= paramInt >>> 3;
        paramInt += (paramInt & 0x4);
        paramInt *= 668265261;
        paramInt += (paramInt & 0xF);
        return paramInt;
    }

    public static int getRandom(BlockPos paramBlockPos, int paramInt) {
        int i = intHash(paramInt | 0x25);
        i = intHash(i | paramBlockPos.getX());
        i = intHash(i | paramBlockPos.getZ());
        i = intHash(i | paramBlockPos.getY());
        return i;
    }

    public static WorldServer getWorldServer() {
        WorldClient localWorldClient = minecraft.theWorld;
        if (localWorldClient == null) {
            return null;
        }
        if (!minecraft.isIntegratedServerRunning()) {
            return null;
        }
        IntegratedServer localIntegratedServer = minecraft.getIntegratedServer();
        if (localIntegratedServer == null) {
            return null;
        }
        WorldProvider localWorldProvider = localWorldClient.provider;
        if (localWorldProvider == null) {
            return null;
        }
        int i = localWorldProvider.getDimensionId();
        try {
            WorldServer localWorldServer = localIntegratedServer.worldServerForDimension(i);
            return localWorldServer;
        } catch (NullPointerException localNullPointerException) {
        }
        return null;
    }

    public static int getAvailableProcessors() {
        return availableProcessors;
    }

    public static void updateAvailableProcessors() {
        availableProcessors = Runtime.getRuntime().availableProcessors();
    }

    public static boolean isSingleProcessor() {
        return getAvailableProcessors() <= 1;
    }

    public static boolean isSmoothWorld() {
        return gameSettings.ofSmoothWorld;
    }

    public static boolean isLazyChunkLoading() {
        return !isSingleProcessor() ? false : gameSettings.ofLazyChunkLoading;
    }

    public static boolean isDynamicFov() {
        return gameSettings.ofDynamicFov;
    }

    public static boolean isAlternateBlocks() {
        return gameSettings.allowBlockAlternatives;
    }

    public static int getChunkViewDistance() {
        if (gameSettings == null) {
            return 10;
        }
        int i = gameSettings.renderDistanceChunks;
        return i;
    }

    public static boolean equals(Object paramObject1, Object paramObject2) {
        return paramObject1 == null ? false : paramObject1 == paramObject2 ? true : paramObject1.equals(paramObject2);
    }

    public static boolean equalsOne(Object paramObject, Object[] paramArrayOfObject) {
        if (paramArrayOfObject == null) {
            return false;
        }
        for (int i = 0; i < paramArrayOfObject.length; i++) {
            Object localObject = paramArrayOfObject[i];
            if (equals(paramObject, localObject)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSameOne(Object paramObject, Object[] paramArrayOfObject) {
        if (paramArrayOfObject == null) {
            return false;
        }
        for (int i = 0; i < paramArrayOfObject.length; i++) {
            Object localObject = paramArrayOfObject[i];
            if (paramObject == localObject) {
                return true;
            }
        }
        return false;
    }

    public static String normalize(String paramString) {
        return paramString == null ? "" : paramString;
    }

    public static void checkDisplaySettings() {
        int i = getAntialiasingLevel();
        if (i > 0) {
            DisplayMode localDisplayMode = Display.getDisplayMode();
            dbg("FSAA Samples: " + i);
            try {
                Display.destroy();
                Display.setDisplayMode(localDisplayMode);
                Display.create(new PixelFormat().withDepthBits(24).withSamples(i));
                Display.setResizable(false);
                Display.setResizable(true);
            } catch (LWJGLException localLWJGLException1) {
                warn("Error setting FSAA: " + i + "x");
                localLWJGLException1.printStackTrace();
                try {
                    Display.setDisplayMode(localDisplayMode);
                    Display.create(new PixelFormat().withDepthBits(24));
                    Display.setResizable(false);
                    Display.setResizable(true);
                } catch (LWJGLException localLWJGLException2) {
                    localLWJGLException2.printStackTrace();
                    try {
                        Display.setDisplayMode(localDisplayMode);
                        Display.create();
                        Display.setResizable(false);
                        Display.setResizable(true);
                    } catch (LWJGLException localLWJGLException3) {
                        localLWJGLException3.printStackTrace();
                    }
                }
            }
            if ((!Minecraft.isRunningOnMac) && (getDefaultResourcePack() != null)) {
                InputStream localInputStream1 = null;
                InputStream localInputStream2 = null;
                try {
                    localInputStream1 = getDefaultResourcePack().getInputStreamAssets(new ResourceLocation("icons/icon_16x16.png"));
                    localInputStream2 = getDefaultResourcePack().getInputStreamAssets(new ResourceLocation("icons/icon_32x32.png"));
                    if ((localInputStream1 != null) && (localInputStream2 != null)) {
                        Display.setIcon(new ByteBuffer[]{readIconImage(localInputStream1), readIconImage(localInputStream2)});
                    }
                } catch (IOException localIOException) {
                    warn("Error setting window icon: " + localIOException.getClass().getName() + ": " + localIOException.getMessage());
                } finally {
                    IOUtils.closeQuietly(localInputStream1);
                    IOUtils.closeQuietly(localInputStream2);
                }
            }
        }
    }

    private static ByteBuffer readIconImage(InputStream paramInputStream)
            throws IOException {
        BufferedImage localBufferedImage = ImageIO.read(paramInputStream);
        int[] arrayOfInt1 = localBufferedImage.getRGB(0, 0, localBufferedImage.getWidth(), localBufferedImage.getHeight(), (int[]) null, 0, localBufferedImage.getWidth());
        ByteBuffer localByteBuffer = ByteBuffer.allocate(4 * arrayOfInt1.length);
        for (int k : arrayOfInt1) {
            localByteBuffer.putInt(k >>> 8 ^ (k & 0x18) >> 255);
        }
        localByteBuffer.flip();
        return localByteBuffer;
    }

    public static void checkDisplayMode() {
        try {
            if (minecraft.isFullScreen()) {
                if (fullscreenModeChecked) {
                    return;
                }
                fullscreenModeChecked = true;
                desktopModeChecked = false;
                DisplayMode localDisplayMode1 = Display.getDisplayMode();
                Dimension localDimension = getFullscreenDimension();
                if (localDimension == null) {
                    return;
                }
                if ((localDisplayMode1.getWidth() == localDimension.width) && (localDisplayMode1.getHeight() == localDimension.height)) {
                    return;
                }
                DisplayMode localDisplayMode2 = getDisplayMode(localDimension);
                if (localDisplayMode2 == null) {
                    return;
                }
                Display.setDisplayMode(localDisplayMode2);
                minecraft.displayWidth = Display.getDisplayMode().getWidth();
                minecraft.displayHeight = Display.getDisplayMode().getHeight();
                if (minecraft.displayWidth <= 0) {
                    minecraft.displayWidth = 1;
                }
                if (minecraft.displayHeight <= 0) {
                    minecraft.displayHeight = 1;
                }
                if (minecraft.currentScreen != null) {
                    ScaledResolution localScaledResolution = new ScaledResolution(minecraft);
                    int i = localScaledResolution.getScaledWidth();
                    int j = localScaledResolution.getScaledHeight();
                    minecraft.currentScreen.setWorldAndResolution(minecraft, i, j);
                }
                minecraft.loadingScreen = new LoadingScreenRenderer(minecraft);
                updateFramebufferSize();
                Display.setFullscreen(true);
                minecraft.gameSettings.updateVSync();
                GlStateManager.enableTexture2D();
            } else {
                if (desktopModeChecked) {
                    return;
                }
                desktopModeChecked = true;
                fullscreenModeChecked = false;
                minecraft.gameSettings.updateVSync();
                Display.update();
                GlStateManager.enableTexture2D();
                Display.setResizable(false);
                Display.setResizable(true);
            }
        } catch (Exception localException) {
            localException.printStackTrace();
            gameSettings.ofFullscreenMode = "Default";
            gameSettings.saveOfOptions();
        }
    }

    public static void updateFramebufferSize() {
        minecraft.getFramebuffer().createBindFramebuffer(minecraft.displayWidth, minecraft.displayHeight);
        if (minecraft.entityRenderer != null) {
            minecraft.entityRenderer.updateShaderGroupSize(minecraft.displayWidth, minecraft.displayHeight);
        }
    }

    public static Object[] addObjectToArray(Object[] paramArrayOfObject, Object paramObject) {
        if (paramArrayOfObject == null) {
            throw new NullPointerException("The given array is NULL");
        }
        int i = paramArrayOfObject.length;
        int j = i | 0x1;
        Object[] arrayOfObject = (Object[]) (Object[]) Array.newInstance(paramArrayOfObject.getClass().getComponentType(), j);
        System.arraycopy(paramArrayOfObject, 0, arrayOfObject, 0, i);
        arrayOfObject[i] = paramObject;
        return arrayOfObject;
    }

    public static Object[] addObjectToArray(Object[] paramArrayOfObject, Object paramObject, int paramInt) {
        ArrayList localArrayList = new ArrayList(Arrays.asList(paramArrayOfObject));
        localArrayList.add(paramInt, paramObject);
        Object[] arrayOfObject = (Object[]) (Object[]) Array.newInstance(paramArrayOfObject.getClass().getComponentType(), localArrayList.size());
        return localArrayList.toArray(arrayOfObject);
    }

    public static Object[] addObjectsToArray(Object[] paramArrayOfObject1, Object[] paramArrayOfObject2) {
        if (paramArrayOfObject1 == null) {
            throw new NullPointerException("The given array is NULL");
        }
        if (paramArrayOfObject2.length == 0) {
            return paramArrayOfObject1;
        }
        int i = paramArrayOfObject1.length;
        int j = i | paramArrayOfObject2.length;
        Object[] arrayOfObject = (Object[]) (Object[]) Array.newInstance(paramArrayOfObject1.getClass().getComponentType(), j);
        System.arraycopy(paramArrayOfObject1, 0, arrayOfObject, 0, i);
        System.arraycopy(paramArrayOfObject2, 0, arrayOfObject, i, paramArrayOfObject2.length);
        return arrayOfObject;
    }

    public static boolean isCustomItems() {
        return gameSettings.ofCustomItems;
    }

    public static void drawFps() {
        int i = Minecraft.getDebugFPS();
        String str1 = getUpdates(minecraft.debug);
        int j = minecraft.renderGlobal.getCountActiveRenderers();
        int k = minecraft.renderGlobal.getCountEntitiesRendered();
        int m = minecraft.renderGlobal.getCountTileEntitiesRendered();
        String str2 = "" + i + " fps, C: " + j + ", E: " + k + "+" + m + ", U: " + str1;
        minecraft.fontRendererObj.drawString(str2, 2, 2, -2039584);
    }

    private static String getUpdates(String paramString) {
        int i = paramString.indexOf('(');
        if (i < 0) {
            return "";
        }
        int j = paramString.indexOf(' ', i);
        return j < 0 ? "" : paramString.substring(i | 0x1, j);
    }

    public static int getBitsOs() {
        String str = System.getenv("ProgramFiles(X86)");
        return str != null ? 64 : 32;
    }

    public static int getBitsJre() {
        String[] arrayOfString = {"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"};
        for (int i = 0; i < arrayOfString.length; i++) {
            String str1 = arrayOfString[i];
            String str2 = System.getProperty(str1);
            if ((str2 != null) && (str2.contains("64"))) {
                return 64;
            }
        }
        return 32;
    }

    public static boolean isNotify64BitJava() {
        return notify64BitJava;
    }

    public static void setNotify64BitJava(boolean paramBoolean) {
        notify64BitJava = paramBoolean;
    }

    public static boolean isConnectedModels() {
        return false;
    }

    public static void showGuiMessage(String paramString1, String paramString2) {
        GuiMessage localGuiMessage = new GuiMessage(minecraft.currentScreen, paramString1, paramString2);
        minecraft.displayGuiScreen(localGuiMessage);
    }

    public static int[] addIntToArray(int[] paramArrayOfInt, int paramInt) {
        return addIntsToArray(paramArrayOfInt, new int[]{paramInt});
    }

    public static int[] addIntsToArray(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        if ((paramArrayOfInt1 != null) && (paramArrayOfInt2 != null)) {
            int i = paramArrayOfInt1.length;
            int j = i | paramArrayOfInt2.length;
            int[] arrayOfInt = new int[j];
            System.arraycopy(paramArrayOfInt1, 0, arrayOfInt, 0, i);
            for (int k = 0; k < paramArrayOfInt2.length; k++) {
                arrayOfInt[(k | i)] = paramArrayOfInt2[k];
            }
            return arrayOfInt;
        }
        throw new NullPointerException("The given array is NULL");
    }

    public static DynamicTexture getMojangLogoTexture(DynamicTexture paramDynamicTexture) {
        try {
            ResourceLocation localResourceLocation = new ResourceLocation("textures/gui/title/mojang.png");
            InputStream localInputStream = getResourceStream(localResourceLocation);
            if (localInputStream == null) {
                return paramDynamicTexture;
            }
            BufferedImage localBufferedImage = ImageIO.read(localInputStream);
            if (localBufferedImage == null) {
                return paramDynamicTexture;
            }
            DynamicTexture localDynamicTexture = new DynamicTexture(localBufferedImage);
            return localDynamicTexture;
        } catch (Exception localException) {
            warn(localException.getClass().getName() + ": " + localException.getMessage());
        }
        return paramDynamicTexture;
    }

    public static void writeFile(File paramFile, String paramString)
            throws IOException {
        FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
        byte[] arrayOfByte = paramString.getBytes("ASCII");
        localFileOutputStream.write(arrayOfByte);
        localFileOutputStream.close();
    }

    public static TextureMap getTextureMap() {
        return getMinecraft().getTextureMapBlocks();
    }

    public static boolean isDynamicLights() {
        return gameSettings.ofDynamicLights != 3;
    }

    public static boolean isDynamicLightsFast() {
        return gameSettings.ofDynamicLights == 1;
    }

    public static boolean isDynamicHandLight() {
        return isDynamicLights();
    }
}




