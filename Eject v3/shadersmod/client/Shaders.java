package shadersmod.client;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import optifine.*;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;
import shadersmod.common.SMCLog;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Shaders {
    public static final int ProgramNone = 0;
    public static final int ProgramBasic = 1;
    public static final int ProgramTextured = 2;
    public static final int ProgramTexturedLit = 3;
    public static final int ProgramSkyBasic = 4;
    public static final int ProgramSkyTextured = 5;
    public static final int ProgramClouds = 6;
    public static final int ProgramTerrain = 7;
    public static final int ProgramTerrainSolid = 8;
    public static final int ProgramTerrainCutoutMip = 9;
    public static final int ProgramTerrainCutout = 10;
    public static final int ProgramDamagedBlock = 11;
    public static final int ProgramWater = 12;
    public static final int ProgramBlock = 13;
    public static final int ProgramBeaconBeam = 14;
    public static final int ProgramItem = 15;
    public static final int ProgramEntities = 16;
    public static final int ProgramArmorGlint = 17;
    public static final int ProgramSpiderEyes = 18;
    public static final int ProgramHand = 19;
    public static final int ProgramWeather = 20;
    public static final int ProgramComposite = 21;
    public static final int ProgramComposite1 = 22;
    public static final int ProgramComposite2 = 23;
    public static final int ProgramComposite3 = 24;
    public static final int ProgramComposite4 = 25;
    public static final int ProgramComposite5 = 26;
    public static final int ProgramComposite6 = 27;
    public static final int ProgramComposite7 = 28;
    public static final int ProgramFinal = 29;
    public static final int ProgramShadow = 30;
    public static final int ProgramShadowSolid = 31;
    public static final int ProgramShadowCutout = 32;
    public static final int ProgramCount = 33;
    public static final int MaxCompositePasses = 8;
    public static final int texMinFilRange = 3;
    public static final int texMagFilRange = 2;
    public static final String[] texMinFilDesc = {"Nearest", "Nearest-Nearest", "Nearest-Linear"};
    public static final String[] texMagFilDesc = {"Nearest", "Linear"};
    public static final int[] texMinFilValue = {9728, 9984, 9986};
    public static final int[] texMagFilValue = {9728, 9729};
    public static final boolean enableShadersOption = true;
    static final int MaxDrawBuffers = 8;
    static final int MaxColorBuffers = 8;
    static final int MaxDepthBuffers = 3;
    static final int MaxShadowColorBuffers = 8;
    static final int MaxShadowDepthBuffers = 2;
    static final int[] dfbColorTexturesA = new int[16];
    static final int[] colorTexturesToggle = new int[8];
    static final int[] colorTextureTextureImageUnit = {0, 1, 2, 3, 7, 8, 9, 10};
    static final boolean[][] programsToggleColorTextures = new boolean[33][8];
    static final float[] faProjection = new float[16];
    static final float[] faProjectionInverse = new float[16];
    static final float[] faModelView = new float[16];
    static final float[] faModelViewInverse = new float[16];
    static final float[] faShadowProjection = new float[16];
    static final float[] faShadowProjectionInverse = new float[16];
    static final float[] faShadowModelView = new float[16];
    static final float[] faShadowModelViewInverse = new float[16];
    private static final String[] programNames = {"", "gbuffers_basic", "gbuffers_textured", "gbuffers_textured_lit", "gbuffers_skybasic", "gbuffers_skytextured", "gbuffers_clouds", "gbuffers_terrain", "gbuffers_terrain_solid", "gbuffers_terrain_cutout_mip", "gbuffers_terrain_cutout", "gbuffers_damagedblock", "gbuffers_water", "gbuffers_block", "gbuffers_beaconbeam", "gbuffers_item", "gbuffers_entities", "gbuffers_armor_glint", "gbuffers_spidereyes", "gbuffers_hand", "gbuffers_weather", "composite", "composite1", "composite2", "composite3", "composite4", "composite5", "composite6", "composite7", "final", "shadow", "shadow_solid", "shadow_cutout"};
    private static final int[] programBackups = {0, 0, 1, 2, 1, 2, 2, 3, 7, 7, 7, 7, 7, 7, 2, 3, 3, 2, 2, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 30};
    private static final int STAGE_GBUFFERS = 0;
    private static final int STAGE_COMPOSITE = 1;
    private static final String[] STAGE_NAMES = {"gbuffers", "composite"};
    private static final boolean enableShadersDebug = true;
    private static final boolean saveFinalShaders = System.getProperty("shaders.debug.save", "false").equals("true");
    private static final int bigBufferSize = 2196;
    private static final ByteBuffer bigBuffer = (ByteBuffer) BufferUtils.createByteBuffer(2196).limit(0);
    static final FloatBuffer projection = nextFloatBuffer(16);
    static final FloatBuffer projectionInverse = nextFloatBuffer(16);
    static final FloatBuffer modelView = nextFloatBuffer(16);
    static final FloatBuffer modelViewInverse = nextFloatBuffer(16);
    static final FloatBuffer shadowProjection = nextFloatBuffer(16);
    static final FloatBuffer shadowProjectionInverse = nextFloatBuffer(16);
    static final FloatBuffer shadowModelView = nextFloatBuffer(16);
    static final FloatBuffer shadowModelViewInverse = nextFloatBuffer(16);
    static final FloatBuffer previousProjection = nextFloatBuffer(16);
    static final FloatBuffer previousModelView = nextFloatBuffer(16);
    static final FloatBuffer tempMatrixDirectBuffer = nextFloatBuffer(16);
    static final FloatBuffer tempDirectFloatBuffer = nextFloatBuffer(16);
    static final IntBuffer dfbColorTextures = nextIntBuffer(16);
    static final IntBuffer dfbDepthTextures = nextIntBuffer(3);
    static final IntBuffer sfbColorTextures = nextIntBuffer(8);
    static final IntBuffer sfbDepthTextures = nextIntBuffer(2);
    static final IntBuffer dfbDrawBuffers = nextIntBuffer(8);
    static final IntBuffer sfbDrawBuffers = nextIntBuffer(8);
    static final IntBuffer drawBuffersNone = nextIntBuffer(8);
    static final IntBuffer drawBuffersAll = nextIntBuffer(8);
    static final IntBuffer drawBuffersClear0 = nextIntBuffer(8);
    static final IntBuffer drawBuffersClear1 = nextIntBuffer(8);
    static final IntBuffer drawBuffersClearColor = nextIntBuffer(8);
    static final IntBuffer drawBuffersColorAtt0 = nextIntBuffer(8);
    static final IntBuffer[] drawBuffersBuffer = nextIntBufferArray(33, 8);
    private static final Pattern gbufferFormatPattern = Pattern.compile("[ \t]*const[ \t]*int[ \t]*(\\w+)Format[ \t]*=[ \t]*([RGBA0123456789FUI_SNORM]*)[ \t]*;.*");
    private static final Pattern gbufferClearPattern = Pattern.compile("[ \t]*const[ \t]*bool[ \t]*(\\w+)Clear[ \t]*=[ \t]*false[ \t]*;.*");
    private static final Pattern gbufferMipmapEnabledPattern = Pattern.compile("[ \t]*const[ \t]*bool[ \t]*(\\w+)MipmapEnabled[ \t]*=[ \t]*true[ \t]*;.*");
    private static final String[] formatNames = {"R8", "RG8", "RGB8", "RGBA8", "R8_SNORM", "RG8_SNORM", "RGB8_SNORM", "RGBA8_SNORM", "R16", "RG16", "RGB16", "RGBA16", "R16_SNORM", "RG16_SNORM", "RGB16_SNORM", "RGBA16_SNORM", "R32F", "RG32F", "RGB32F", "RGBA32F", "R32I", "RG32I", "RGB32I", "RGBA32I", "R32UI", "RG32UI", "RGB32UI", "RGBA32UI", "R3_G3_B2", "RGB5_A1", "RGB10_A2", "R11F_G11F_B10F"};
    private static final int[] formatIds = {33321, 33323, 32849, 32856, 36756, 36757, 36758, 36759, 33322, 33324, 32852, 32859, 36760, 36761, 36762, 36763, 33326, 33328, 34837, 34836, 33333, 33339, 36227, 36226, 33334, 33340, 36209, 36208, 10768, 32855, 32857, 35898};
    private static final Pattern patternLoadEntityDataMap = Pattern.compile("\\s*([\\w:]+)\\s*=\\s*([-]?\\d+)\\s*");
    public static boolean isInitializedOnce = false;
    public static boolean isShaderPackInitialized = false;
    public static ContextCapabilities capabilities;
    public static String glVersionString;
    public static String glVendorString;
    public static String glRendererString;
    public static boolean hasGlGenMipmap = false;
    public static boolean hasForge = false;
    public static int numberResetDisplayList = 0;
    public static int renderWidth = 0;
    public static int renderHeight = 0;
    public static boolean isRenderingWorld = false;
    public static boolean isRenderingSky = false;
    public static boolean isCompositeRendered = false;
    public static boolean isRenderingDfb = false;
    public static boolean isShadowPass = false;
    public static boolean isSleeping;
    public static boolean renderItemKeepDepthMask = false;
    public static boolean itemToRenderMainTranslucent = false;
    public static float wetnessHalfLife = 600.0F;
    public static float drynessHalfLife = 200.0F;
    public static float eyeBrightnessHalflife = 10.0F;
    public static int entityAttrib = 10;
    public static int midTexCoordAttrib = 11;
    public static int tangentAttrib = 12;
    public static boolean useEntityAttrib = false;
    public static boolean useMidTexCoordAttrib = false;
    public static boolean useMultiTexCoord3Attrib = false;
    public static boolean useTangentAttrib = false;
    public static boolean progUseEntityAttrib = false;
    public static boolean progUseMidTexCoordAttrib = false;
    public static boolean progUseTangentAttrib = false;
    public static int atlasSizeX = 0;
    public static int atlasSizeY = 0;
    public static ShaderUniformFloat4 uniformEntityColor = new ShaderUniformFloat4("entityColor");
    public static ShaderUniformInt uniformEntityId = new ShaderUniformInt("entityId");
    public static ShaderUniformInt uniformBlockEntityId = new ShaderUniformInt("blockEntityId");
    public static boolean needResizeShadow = false;
    public static boolean shouldSkipDefaultShadow = false;
    public static int activeProgram = 0;
    public static Properties loadedShaders = null;
    public static Properties shadersConfig = null;
    public static ITextureObject defaultTexture = null;
    public static boolean normalMapEnabled = false;
    public static boolean[] shadowHardwareFilteringEnabled = new boolean[2];
    public static boolean[] shadowMipmapEnabled = new boolean[2];
    public static boolean[] shadowFilterNearest = new boolean[2];
    public static boolean[] shadowColorMipmapEnabled = new boolean[8];
    public static boolean[] shadowColorFilterNearest = new boolean[8];
    public static boolean configTweakBlockDamage = true;
    public static boolean configCloudShadow = true;
    public static float configHandDepthMul = 0.125F;
    public static float configRenderResMul = 1.0F;
    public static float configShadowResMul = 1.0F;
    public static int configTexMinFilB = 0;
    public static int configTexMinFilN = 0;
    public static int configTexMinFilS = 0;
    public static int configTexMagFilB = 0;
    public static int configTexMagFilN = 0;
    public static int configTexMagFilS = 0;
    public static boolean configShadowClipFrustrum = true;
    public static boolean configNormalMap = true;
    public static boolean configSpecularMap = true;
    public static PropertyDefaultTrueFalse configOldLighting = new PropertyDefaultTrueFalse("oldLighting", "Classic Lighting", 0);
    public static PropertyDefaultTrueFalse configOldHandLight = new PropertyDefaultTrueFalse("oldHandLight", "Old Hand Light", 0);
    public static int configAntialiasingLevel = 0;
    public static boolean shaderPackLoaded = false;
    public static String packNameNone = "OFF";
    public static PropertyDefaultFastFancyOff shaderPackClouds = new PropertyDefaultFastFancyOff("clouds", "Clouds", 0);
    public static PropertyDefaultTrueFalse shaderPackOldLighting = new PropertyDefaultTrueFalse("oldLighting", "Classic Lighting", 0);
    public static PropertyDefaultTrueFalse shaderPackOldHandLight = new PropertyDefaultTrueFalse("oldHandLight", "Old Hand Light", 0);
    public static PropertyDefaultTrueFalse shaderPackDynamicHandLight = new PropertyDefaultTrueFalse("dynamicHandLight", "Dynamic Hand Light", 0);
    public static PropertyDefaultTrueFalse shaderPackShadowTranslucent = new PropertyDefaultTrueFalse("shadowTranslucent", "Shadow Translucent", 0);
    public static PropertyDefaultTrueFalse shaderPackUnderwaterOverlay = new PropertyDefaultTrueFalse("underwaterOverlay", "Underwater Overlay", 0);
    public static PropertyDefaultTrueFalse shaderPackSun = new PropertyDefaultTrueFalse("sun", "Sun", 0);
    public static PropertyDefaultTrueFalse shaderPackMoon = new PropertyDefaultTrueFalse("moon", "Moon", 0);
    public static PropertyDefaultTrueFalse shaderPackVignette = new PropertyDefaultTrueFalse("vignette", "Vignette", 0);
    public static PropertyDefaultTrueFalse shaderPackBackFaceSolid = new PropertyDefaultTrueFalse("backFace.solid", "Back-face Solid", 0);
    public static PropertyDefaultTrueFalse shaderPackBackFaceCutout = new PropertyDefaultTrueFalse("backFace.cutout", "Back-face Cutout", 0);
    public static PropertyDefaultTrueFalse shaderPackBackFaceCutoutMipped = new PropertyDefaultTrueFalse("backFace.cutoutMipped", "Back-face Cutout Mipped", 0);
    public static PropertyDefaultTrueFalse shaderPackBackFaceTranslucent = new PropertyDefaultTrueFalse("backFace.translucent", "Back-face Translucent", 0);
    public static float blockLightLevel05 = 0.5F;
    public static float blockLightLevel06 = 0.6F;
    public static float blockLightLevel08 = 0.8F;
    public static float aoLevel = -1.0F;
    public static float sunPathRotation = 0.0F;
    public static float shadowAngleInterval = 0.0F;
    public static int fogMode = 0;
    public static float fogColorR;
    public static float fogColorG;
    public static float fogColorB;
    public static float shadowIntervalSize = 2.0F;
    public static int terrainIconSize = 16;
    public static int[] terrainTextureSize = new int[2];
    public static int[] entityData = new int[32];
    public static int entityDataIndex = 0;
    static Minecraft mc;
    static EntityRenderer entityRenderer;
    static boolean needResetModels = false;
    static float[] sunPosition = new float[4];
    static float[] moonPosition = new float[4];
    static float[] shadowLightPosition = new float[4];
    static float[] upPosition = new float[4];
    static float[] shadowLightPositionVector = new float[4];
    static float[] upPosModelView = {0.0F, 100.0F, 0.0F, 0.0F};
    static float[] sunPosModelView = {0.0F, 100.0F, 0.0F, 0.0F};
    static float[] moonPosModelView = {0.0F, -100.0F, 0.0F, 0.0F};
    static float clearColorR;
    static float clearColorG;
    static float clearColorB;
    static float skyColorR;
    static float skyColorG;
    static float skyColorB;
    static long worldTime = 0L;
    static long lastWorldTime = 0L;
    static long diffWorldTime = 0L;
    static float celestialAngle = 0.0F;
    static float sunAngle = 0.0F;
    static float shadowAngle = 0.0F;
    static int moonPhase = 0;
    static long systemTime = 0L;
    static long lastSystemTime = 0L;
    static long diffSystemTime = 0L;
    static int frameCounter = 0;
    static float frameTime = 0.0F;
    static float frameTimeCounter = 0.0F;
    static int systemTimeInt32 = 0;
    static float rainStrength = 0.0F;
    static float wetness = 0.0F;
    static boolean usewetness = false;
    static int isEyeInWater = 0;
    static int eyeBrightness = 0;
    static float eyeBrightnessFadeX = 0.0F;
    static float eyeBrightnessFadeY = 0.0F;
    static float eyePosY = 0.0F;
    static float centerDepth = 0.0F;
    static float centerDepthSmooth = 0.0F;
    static float centerDepthSmoothHalflife = 1.0F;
    static boolean centerDepthSmoothEnabled = false;
    static int superSamplingLevel = 1;
    static float nightVision = 0.0F;
    static float blindness = 0.0F;
    static boolean updateChunksErrorRecorded = false;
    static boolean lightmapEnabled = false;
    static boolean fogEnabled = true;
    static double previousCameraPositionX;
    static double previousCameraPositionY;
    static double previousCameraPositionZ;
    static double cameraPositionX;
    static double cameraPositionY;
    static double cameraPositionZ;
    static int shadowPassInterval = 0;
    static int shadowMapWidth = 1024;
    static int shadowMapHeight = 1024;
    static int spShadowMapWidth = 1024;
    static int spShadowMapHeight = 1024;
    static float shadowMapFOV = 90.0F;
    static float shadowMapHalfPlane = 160.0F;
    static boolean shadowMapIsOrtho = true;
    static float shadowDistanceRenderMul = -1.0F;
    static int shadowPassCounter = 0;
    static int preShadowPassThirdPersonView;
    static boolean waterShadowEnabled = false;
    static int usedColorBuffers = 0;
    static int usedDepthBuffers = 0;
    static int usedShadowColorBuffers = 0;
    static int usedShadowDepthBuffers = 0;
    static int usedColorAttachs = 0;
    static int usedDrawBuffers = 0;
    static int dfb = 0;
    static int sfb = 0;
    static int[] programsID = new int[33];
    static IntBuffer[] programsDrawBuffers = new IntBuffer[33];
    static IntBuffer activeDrawBuffers = null;
    static IShaderPack shaderPack = null;
    static File currentshader;
    static String currentshadername;
    static String packNameDefault = "(internal)";
    static String shaderpacksdirname = "shaderpacks";
    static String optionsfilename = "optionsshaders.txt";
    static File shadersdir;
    static File shaderpacksdir;
    static File configFile;
    static ShaderOption[] shaderPackOptions = null;
    static ShaderProfile[] shaderPackProfiles = null;
    static Map<String, ShaderOption[]> shaderPackGuiScreens = null;
    static Map<Block, Integer> mapBlockToEntityData;
    private static int renderDisplayWidth = 0;
    private static int renderDisplayHeight = 0;
    private static boolean isHandRenderedMain;
    private static float[] tempMat = new float[16];
    private static int[] gbuffersFormat = new int[8];
    private static boolean[] gbuffersClear = new boolean[8];
    private static int[] programsRef = new int[33];
    private static int programIDCopyDepth = 0;
    private static String[] programsDrawBufSettings = new String[33];
    private static String newDrawBufSetting = null;
    private static String[] programsColorAtmSettings = new String[33];
    private static String newColorAtmSetting = null;
    private static String activeColorAtmSettings = null;
    private static int[] programsCompositeMipmapSetting = new int[33];
    private static int newCompositeMipmapSetting = 0;
    private static int activeCompositeMipmapSetting = 0;
    private static Map<String, String> shaderPackResources = new HashMap();
    private static World currentWorld = null;
    private static List<Integer> shaderPackDimensions = new ArrayList();
    private static CustomTexture[] customTexturesGbuffers = null;
    private static CustomTexture[] customTexturesComposite = null;
    private static HFNoiseTexture noiseTexture;
    private static boolean noiseTextureEnabled = false;
    private static int noiseTextureResolution = 256;

    static {
        shadersdir = new File(Minecraft.getMinecraft().mcDataDir, "shaders");
        shaderpacksdir = new File(Minecraft.getMinecraft().mcDataDir, shaderpacksdirname);
        configFile = new File(Minecraft.getMinecraft().mcDataDir, optionsfilename);
        drawBuffersNone.limit(0);
        drawBuffersColorAtt0.put(36064).position(0).limit(1);
    }

    private static ByteBuffer nextByteBuffer(int paramInt) {
        ByteBuffer localByteBuffer = bigBuffer;
        int i = localByteBuffer.limit();
        localByteBuffer.position(i).limit(i | paramInt);
        return localByteBuffer.slice();
    }

    private static IntBuffer nextIntBuffer(int paramInt) {
        ByteBuffer localByteBuffer = bigBuffer;
        int i = localByteBuffer.limit();
        localByteBuffer.position(i).limit(i | paramInt * 4);
        return localByteBuffer.asIntBuffer();
    }

    private static FloatBuffer nextFloatBuffer(int paramInt) {
        ByteBuffer localByteBuffer = bigBuffer;
        int i = localByteBuffer.limit();
        localByteBuffer.position(i).limit(i | paramInt * 4);
        return localByteBuffer.asFloatBuffer();
    }

    private static IntBuffer[] nextIntBufferArray(int paramInt1, int paramInt2) {
        IntBuffer[] arrayOfIntBuffer = new IntBuffer[paramInt1];
        for (int i = 0; i < paramInt1; i++) {
            arrayOfIntBuffer[i] = nextIntBuffer(paramInt2);
        }
        return arrayOfIntBuffer;
    }

    public static void loadConfig() {
        SMCLog.info("Load ShadersMod configuration.");
        try {
            if (!shaderpacksdir.exists()) {
                shaderpacksdir.mkdir();
            }
        } catch (Exception localException1) {
            SMCLog.severe("Failed to open the shaderpacks directory: " + shaderpacksdir);
        }
        shadersConfig = new PropertiesOrdered();
        shadersConfig.setProperty(EnumShaderOption.SHADER_PACK.getPropertyKey(), "");
        if (configFile.exists()) {
            try {
                FileReader localFileReader = new FileReader(configFile);
                shadersConfig.load(localFileReader);
                localFileReader.close();
            } catch (Exception localException2) {
            }
        }
        if (!configFile.exists()) {
            try {
                storeConfig();
            } catch (Exception localException3) {
            }
        }
        EnumShaderOption[] arrayOfEnumShaderOption = EnumShaderOption.values();
        for (int i = 0; i < arrayOfEnumShaderOption.length; i++) {
            EnumShaderOption localEnumShaderOption = arrayOfEnumShaderOption[i];
            String str1 = localEnumShaderOption.getPropertyKey();
            String str2 = localEnumShaderOption.getValueDefault();
            String str3 = shadersConfig.getProperty(str1, str2);
            setEnumShaderOption(localEnumShaderOption, str3);
        }
        loadShaderPack();
    }

    private static void setEnumShaderOption(EnumShaderOption paramEnumShaderOption, String paramString) {
        if (paramString == null) {
            paramString = paramEnumShaderOption.getValueDefault();
        }
        switch (paramEnumShaderOption) {
            case ANTIALIASING:
                configAntialiasingLevel = Config.parseInt(paramString, 0);
                break;
            case NORMAL_MAP:
                configNormalMap = Config.parseBoolean(paramString, true);
                break;
            case SPECULAR_MAP:
                configSpecularMap = Config.parseBoolean(paramString, true);
                break;
            case RENDER_RES_MUL:
                configRenderResMul = Config.parseFloat(paramString, 1.0F);
                break;
            case SHADOW_RES_MUL:
                configShadowResMul = Config.parseFloat(paramString, 1.0F);
                break;
            case HAND_DEPTH_MUL:
                configHandDepthMul = Config.parseFloat(paramString, 0.125F);
                break;
            case CLOUD_SHADOW:
                configCloudShadow = Config.parseBoolean(paramString, true);
                break;
            case OLD_HAND_LIGHT:
                configOldHandLight.setPropertyValue(paramString);
                break;
            case OLD_LIGHTING:
                configOldLighting.setPropertyValue(paramString);
                break;
            case SHADER_PACK:
                currentshadername = paramString;
                break;
            case TWEAK_BLOCK_DAMAGE:
                configTweakBlockDamage = Config.parseBoolean(paramString, true);
                break;
            case SHADOW_CLIP_FRUSTRUM:
                configShadowClipFrustrum = Config.parseBoolean(paramString, true);
                break;
            case TEX_MIN_FIL_B:
                configTexMinFilB = Config.parseInt(paramString, 0);
                break;
            case TEX_MIN_FIL_N:
                configTexMinFilN = Config.parseInt(paramString, 0);
                break;
            case TEX_MIN_FIL_S:
                configTexMinFilS = Config.parseInt(paramString, 0);
                break;
            case TEX_MAG_FIL_B:
                configTexMagFilB = Config.parseInt(paramString, 0);
                break;
            case TEX_MAG_FIL_N:
                configTexMagFilB = Config.parseInt(paramString, 0);
                break;
            case TEX_MAG_FIL_S:
                configTexMagFilB = Config.parseInt(paramString, 0);
                break;
            default:
                throw new IllegalArgumentException("Unknown option: " + paramEnumShaderOption);
        }
    }

    public static void storeConfig() {
        SMCLog.info("Save ShadersMod configuration.");
        if (shadersConfig == null) {
            shadersConfig = new PropertiesOrdered();
        }
        EnumShaderOption[] arrayOfEnumShaderOption = EnumShaderOption.values();
        for (int i = 0; i < arrayOfEnumShaderOption.length; i++) {
            EnumShaderOption localEnumShaderOption = arrayOfEnumShaderOption[i];
            String str1 = localEnumShaderOption.getPropertyKey();
            String str2 = getEnumShaderOption(localEnumShaderOption);
            shadersConfig.setProperty(str1, str2);
        }
        try {
            FileWriter localFileWriter = new FileWriter(configFile);
            shadersConfig.store(localFileWriter, (String) null);
            localFileWriter.close();
        } catch (Exception localException) {
            SMCLog.severe("Error saving configuration: " + localException.getClass().getName() + ": " + localException.getMessage());
        }
    }

    public static String getEnumShaderOption(EnumShaderOption paramEnumShaderOption) {
        switch (paramEnumShaderOption) {
            case ANTIALIASING:
                return Integer.toString(configAntialiasingLevel);
            case NORMAL_MAP:
                return Boolean.toString(configNormalMap);
            case SPECULAR_MAP:
                return Boolean.toString(configSpecularMap);
            case RENDER_RES_MUL:
                return Float.toString(configRenderResMul);
            case SHADOW_RES_MUL:
                return Float.toString(configShadowResMul);
            case HAND_DEPTH_MUL:
                return Float.toString(configHandDepthMul);
            case CLOUD_SHADOW:
                return Boolean.toString(configCloudShadow);
            case OLD_HAND_LIGHT:
                return configOldHandLight.getPropertyValue();
            case OLD_LIGHTING:
                return configOldLighting.getPropertyValue();
            case SHADER_PACK:
                return currentshadername;
            case TWEAK_BLOCK_DAMAGE:
                return Boolean.toString(configTweakBlockDamage);
            case SHADOW_CLIP_FRUSTRUM:
                return Boolean.toString(configShadowClipFrustrum);
            case TEX_MIN_FIL_B:
                return Integer.toString(configTexMinFilB);
            case TEX_MIN_FIL_N:
                return Integer.toString(configTexMinFilN);
            case TEX_MIN_FIL_S:
                return Integer.toString(configTexMinFilS);
            case TEX_MAG_FIL_B:
                return Integer.toString(configTexMagFilB);
            case TEX_MAG_FIL_N:
                return Integer.toString(configTexMagFilB);
            case TEX_MAG_FIL_S:
                return Integer.toString(configTexMagFilB);
        }
        throw new IllegalArgumentException("Unknown option: " + paramEnumShaderOption);
    }

    public static void setShaderPack(String paramString) {
        currentshadername = paramString;
        shadersConfig.setProperty(EnumShaderOption.SHADER_PACK.getPropertyKey(), paramString);
        loadShaderPack();
    }

    public static void loadShaderPack() {
        boolean bool1 = shaderPackLoaded;
        boolean bool2 = isOldLighting();
        shaderPackLoaded = false;
        if (shaderPack != null) {
            shaderPack.close();
            shaderPack = null;
            shaderPackResources.clear();
            shaderPackDimensions.clear();
            shaderPackOptions = null;
            shaderPackProfiles = null;
            shaderPackGuiScreens = null;
            shaderPackClouds.resetValue();
            shaderPackOldHandLight.resetValue();
            shaderPackDynamicHandLight.resetValue();
            shaderPackOldLighting.resetValue();
            resetCustomTextures();
        }
        int i = 0;
        if (Config.isAntialiasing()) {
            SMCLog.info("Shaders can not be loaded, Antialiasing is enabled: " + Config.getAntialiasingLevel() + "x");
            i = 1;
        }
        if (Config.isAnisotropicFiltering()) {
            SMCLog.info("Shaders can not be loaded, Anisotropic Filtering is enabled: " + Config.getAnisotropicFilterLevel() + "x");
            i = 1;
        }
        if (Config.isFastRender()) {
            SMCLog.info("Shaders can not be loaded, Fast Render is enabled.");
            i = 1;
        }
        String str = shadersConfig.getProperty(EnumShaderOption.SHADER_PACK.getPropertyKey(), packNameDefault);
        if ((!str.isEmpty()) && (!str.equals(packNameNone)) && (i == 0)) {
            if (str.equals(packNameDefault)) {
                shaderPack = new ShaderPackDefault();
                shaderPackLoaded = true;
            } else {
                try {
                    File localFile = new File(shaderpacksdir, str);
                    if (localFile.isDirectory()) {
                        shaderPack = new ShaderPackFolder(str, localFile);
                        shaderPackLoaded = true;
                    } else if ((localFile.isFile()) && (str.toLowerCase().endsWith(".zip"))) {
                        shaderPack = new ShaderPackZip(str, localFile);
                        shaderPackLoaded = true;
                    }
                } catch (Exception localException) {
                }
            }
        }
        if (shaderPack != null) {
            SMCLog.info("Loaded shaderpack: " + getShaderPackName());
        } else {
            SMCLog.info("No shaderpack loaded.");
            shaderPack = new ShaderPackNone();
        }
        loadShaderPackResources();
        loadShaderPackDimensions();
        shaderPackOptions = loadShaderPackOptions();
        loadShaderPackProperties();
        int j = shaderPackLoaded != bool1 ? 1 : 0;
        int k = isOldLighting() != bool2 ? 1 : 0;
        if ((j != 0) || (k != 0)) {
            DefaultVertexFormats.updateVertexFormats();
            if (Reflector.LightUtil.exists()) {
                Reflector.LightUtil_itemConsumer.setValue(null);
                Reflector.LightUtil_tessellator.setValue(null);
            }
            updateBlockLightLevel();
            mc.scheduleResourcesRefresh();
        }
    }

    private static void loadShaderPackDimensions() {
        shaderPackDimensions.clear();
        for (int i = -128; i <= 128; i++) {
            String str = "/shaders/world" + i;
            if (shaderPack.hasDirectory(str)) {
                shaderPackDimensions.add(Integer.valueOf(i));
            }
        }
        if (shaderPackDimensions.size() > 0) {
            Integer[] arrayOfInteger = (Integer[]) (Integer[]) shaderPackDimensions.toArray(new Integer[shaderPackDimensions.size()]);
            Config.dbg("[Shaders] Worlds: " + Config.arrayToString((Object[]) arrayOfInteger));
        }
    }

    private static void loadShaderPackProperties() {
        shaderPackClouds.resetValue();
        shaderPackOldHandLight.resetValue();
        shaderPackDynamicHandLight.resetValue();
        shaderPackOldLighting.resetValue();
        shaderPackShadowTranslucent.resetValue();
        shaderPackUnderwaterOverlay.resetValue();
        shaderPackSun.resetValue();
        shaderPackMoon.resetValue();
        shaderPackVignette.resetValue();
        shaderPackBackFaceSolid.resetValue();
        shaderPackBackFaceCutout.resetValue();
        shaderPackBackFaceCutoutMipped.resetValue();
        shaderPackBackFaceTranslucent.resetValue();
        BlockAliases.reset();
        if (shaderPack != null) {
            BlockAliases.update(shaderPack);
            String str = "/shaders/shaders.properties";
            try {
                InputStream localInputStream = shaderPack.getResourceAsStream(str);
                if (localInputStream == null) {
                    return;
                }
                PropertiesOrdered localPropertiesOrdered = new PropertiesOrdered();
                localPropertiesOrdered.load(localInputStream);
                localInputStream.close();
                shaderPackClouds.loadFrom(localPropertiesOrdered);
                shaderPackOldHandLight.loadFrom(localPropertiesOrdered);
                shaderPackDynamicHandLight.loadFrom(localPropertiesOrdered);
                shaderPackOldLighting.loadFrom(localPropertiesOrdered);
                shaderPackShadowTranslucent.loadFrom(localPropertiesOrdered);
                shaderPackUnderwaterOverlay.loadFrom(localPropertiesOrdered);
                shaderPackSun.loadFrom(localPropertiesOrdered);
                shaderPackVignette.loadFrom(localPropertiesOrdered);
                shaderPackMoon.loadFrom(localPropertiesOrdered);
                shaderPackBackFaceSolid.loadFrom(localPropertiesOrdered);
                shaderPackBackFaceCutout.loadFrom(localPropertiesOrdered);
                shaderPackBackFaceCutoutMipped.loadFrom(localPropertiesOrdered);
                shaderPackBackFaceTranslucent.loadFrom(localPropertiesOrdered);
                shaderPackProfiles = ShaderPackParser.parseProfiles(localPropertiesOrdered, shaderPackOptions);
                shaderPackGuiScreens = ShaderPackParser.parseGuiScreens(localPropertiesOrdered, shaderPackProfiles, shaderPackOptions);
                customTexturesGbuffers = loadCustomTextures(localPropertiesOrdered, 0);
                customTexturesComposite = loadCustomTextures(localPropertiesOrdered, 1);
            } catch (IOException localIOException) {
                Config.warn("[Shaders] Error reading: " + str);
            }
        }
    }

    private static CustomTexture[] loadCustomTextures(Properties paramProperties, int paramInt) {
        String str1 = "texture." + STAGE_NAMES[paramInt] + ".";
        Set localSet = paramProperties.keySet();
        ArrayList localArrayList = new ArrayList();
        Object localObject1 = localSet.iterator();
        while (((Iterator) localObject1).hasNext()) {
            Object localObject2 = ((Iterator) localObject1).next();
            String str2 = (String) localObject2;
            if (str2.startsWith(str1)) {
                String str3 = str2.substring(str1.length());
                String str4 = paramProperties.getProperty(str2).trim();
                int i = getTextureIndex(paramInt, str3);
                if (i < 0) {
                    SMCLog.warning("Invalid texture name: " + str2);
                } else {
                    try {
                        String str5 = "shaders/" + StrUtils.removePrefix(str4, "/");
                        InputStream localInputStream = shaderPack.getResourceAsStream(str5);
                        if (localInputStream == null) {
                            SMCLog.warning("Texture not found: " + str4);
                        } else {
                            IOUtils.closeQuietly(localInputStream);
                            SimpleShaderTexture localSimpleShaderTexture = new SimpleShaderTexture(str5);
                            localSimpleShaderTexture.loadTexture(mc.getResourceManager());
                            CustomTexture localCustomTexture = new CustomTexture(i, str5, localSimpleShaderTexture);
                            localArrayList.add(localCustomTexture);
                        }
                    } catch (IOException localIOException) {
                        SMCLog.warning("Error loading texture: " + str4);
                        SMCLog.warning("" + localIOException.getClass().getName() + ": " + localIOException.getMessage());
                    }
                }
            }
        }
        if (localArrayList.size() <= 0) {
            return null;
        }
        localObject1 = (CustomTexture[]) (CustomTexture[]) localArrayList.toArray(new CustomTexture[localArrayList.size()]);
        return (CustomTexture[]) localObject1;
    }

    private static int getTextureIndex(int paramInt, String paramString) {
        if (paramInt == 0) {
            if (paramString.equals("texture")) {
                return 0;
            }
            if (paramString.equals("lightmap")) {
                return 1;
            }
            if (paramString.equals("normals")) {
                return 2;
            }
            if (paramString.equals("specular")) {
                return 3;
            }
            if ((paramString.equals("shadowtex0")) || (paramString.equals("watershadow"))) {
                return 4;
            }
            if (paramString.equals("shadow")) {
                return waterShadowEnabled ? 5 : 4;
            }
            if (paramString.equals("shadowtex1")) {
                return 5;
            }
            if (paramString.equals("depthtex0")) {
                return 6;
            }
            if (paramString.equals("gaux1")) {
                return 7;
            }
            if (paramString.equals("gaux2")) {
                return 8;
            }
            if (paramString.equals("gaux3")) {
                return 9;
            }
            if (paramString.equals("gaux4")) {
                return 10;
            }
            if (paramString.equals("depthtex1")) {
                return 12;
            }
            if ((paramString.equals("shadowcolor0")) || (paramString.equals("shadowcolor"))) {
                return 13;
            }
            if (paramString.equals("shadowcolor1")) {
                return 14;
            }
            if (paramString.equals("noisetex")) {
                return 15;
            }
        }
        if (paramInt == 1) {
            if ((paramString.equals("colortex0")) || (paramString.equals("colortex0"))) {
                return 0;
            }
            if ((paramString.equals("colortex1")) || (paramString.equals("gdepth"))) {
                return 1;
            }
            if ((paramString.equals("colortex2")) || (paramString.equals("gnormal"))) {
                return 2;
            }
            if ((paramString.equals("colortex3")) || (paramString.equals("composite"))) {
                return 3;
            }
            if ((paramString.equals("shadowtex0")) || (paramString.equals("watershadow"))) {
                return 4;
            }
            if (paramString.equals("shadow")) {
                return waterShadowEnabled ? 5 : 4;
            }
            if (paramString.equals("shadowtex1")) {
                return 5;
            }
            if ((paramString.equals("depthtex0")) || (paramString.equals("gdepthtex"))) {
                return 6;
            }
            if ((paramString.equals("colortex4")) || (paramString.equals("gaux1"))) {
                return 7;
            }
            if ((paramString.equals("colortex5")) || (paramString.equals("gaux2"))) {
                return 8;
            }
            if ((paramString.equals("colortex6")) || (paramString.equals("gaux3"))) {
                return 9;
            }
            if ((paramString.equals("colortex7")) || (paramString.equals("gaux4"))) {
                return 10;
            }
            if (paramString.equals("depthtex1")) {
                return 11;
            }
            if (paramString.equals("depthtex2")) {
                return 12;
            }
            if ((paramString.equals("shadowcolor0")) || (paramString.equals("shadowcolor"))) {
                return 13;
            }
            if (paramString.equals("shadowcolor1")) {
                return 14;
            }
            if (paramString.equals("noisetex")) {
                return 15;
            }
        }
        return -1;
    }

    private static void bindCustomTextures(CustomTexture[] paramArrayOfCustomTexture) {
        if (paramArrayOfCustomTexture != null) {
            for (int i = 0; i < paramArrayOfCustomTexture.length; i++) {
                CustomTexture localCustomTexture = paramArrayOfCustomTexture[i];
                GlStateManager.setActiveTexture(0x84C0 | localCustomTexture.getTextureUnit());
                ITextureObject localITextureObject = localCustomTexture.getTexture();
                GlStateManager.bindTexture(localITextureObject.getGlTextureId());
            }
        }
    }

    private static void resetCustomTextures() {
        deleteCustomTextures(customTexturesGbuffers);
        deleteCustomTextures(customTexturesComposite);
        customTexturesGbuffers = null;
        customTexturesComposite = null;
    }

    private static void deleteCustomTextures(CustomTexture[] paramArrayOfCustomTexture) {
        if (paramArrayOfCustomTexture != null) {
            for (int i = 0; i < paramArrayOfCustomTexture.length; i++) {
                CustomTexture localCustomTexture = paramArrayOfCustomTexture[i];
                ITextureObject localITextureObject = localCustomTexture.getTexture();
                TextureUtil.deleteTexture(localITextureObject.getGlTextureId());
            }
        }
    }

    public static ShaderOption[] getShaderPackOptions(String paramString) {
        ShaderOption[] arrayOfShaderOption1 = (ShaderOption[]) shaderPackOptions.clone();
        if (shaderPackGuiScreens == null) {
            if (shaderPackProfiles != null) {
                localObject = new ShaderOptionProfile(shaderPackProfiles, arrayOfShaderOption1);
                arrayOfShaderOption1 = (ShaderOption[]) (ShaderOption[]) Config.addObjectToArray(arrayOfShaderOption1, localObject, 0);
            }
            arrayOfShaderOption1 = getVisibleOptions(arrayOfShaderOption1);
            return arrayOfShaderOption1;
        }
        Object localObject = paramString != null ? "screen." + paramString : "screen";
        ShaderOption[] arrayOfShaderOption2 = (ShaderOption[]) shaderPackGuiScreens.get(localObject);
        if (arrayOfShaderOption2 == null) {
            return new ShaderOption[0];
        }
        ArrayList localArrayList = new ArrayList();
        for (int i = 0; i < arrayOfShaderOption2.length; i++) {
            ShaderOption localShaderOption = arrayOfShaderOption2[i];
            if (localShaderOption == null) {
                localArrayList.add((ShaderOption) null);
            } else if ((localShaderOption instanceof ShaderOptionRest)) {
                ShaderOption[] arrayOfShaderOption4 = getShaderOptionsRest(shaderPackGuiScreens, arrayOfShaderOption1);
                localArrayList.addAll(Arrays.asList(arrayOfShaderOption4));
            } else {
                localArrayList.add(localShaderOption);
            }
        }
        ShaderOption[] arrayOfShaderOption3 = (ShaderOption[]) (ShaderOption[]) localArrayList.toArray(new ShaderOption[localArrayList.size()]);
        return arrayOfShaderOption3;
    }

    private static ShaderOption[] getShaderOptionsRest(Map<String, ShaderOption[]> paramMap, ShaderOption[] paramArrayOfShaderOption) {
        HashSet localHashSet = new HashSet();
        Object localObject1 = paramMap.keySet().iterator();
        Object localObject2;
        while (((Iterator) localObject1).hasNext()) {
            String str1 = (String) ((Iterator) localObject1).next();
            localObject2 = (ShaderOption[]) paramMap.get(str1);
            for (int j = 0; j < localObject2.length; j++) {
                Object localObject3 = localObject2[j];
                if (localObject3 != null) {
                    localHashSet.add(((ShaderOption) localObject3).getName());
                }
            }
        }
        localObject1 = new ArrayList();
        for (int i = 0; i < paramArrayOfShaderOption.length; i++) {
            localObject2 = paramArrayOfShaderOption[i];
            if (((ShaderOption) localObject2).isVisible()) {
                String str2 = ((ShaderOption) localObject2).getName();
                if (!localHashSet.contains(str2)) {
                    ((List) localObject1).add(localObject2);
                }
            }
        }
        ShaderOption[] arrayOfShaderOption = (ShaderOption[]) (ShaderOption[]) ((List) localObject1).toArray(new ShaderOption[((List) localObject1).size()]);
        return arrayOfShaderOption;
    }

    public static ShaderOption getShaderOption(String paramString) {
        return ShaderUtils.getShaderOption(paramString, shaderPackOptions);
    }

    public static ShaderOption[] getShaderPackOptions() {
        return shaderPackOptions;
    }

    private static ShaderOption[] getVisibleOptions(ShaderOption[] paramArrayOfShaderOption) {
        ArrayList localArrayList = new ArrayList();
        for (int i = 0; i < paramArrayOfShaderOption.length; i++) {
            ShaderOption localShaderOption = paramArrayOfShaderOption[i];
            if (localShaderOption.isVisible()) {
                localArrayList.add(localShaderOption);
            }
        }
        ShaderOption[] arrayOfShaderOption = (ShaderOption[]) (ShaderOption[]) localArrayList.toArray(new ShaderOption[localArrayList.size()]);
        return arrayOfShaderOption;
    }

    public static void saveShaderPackOptions() {
        saveShaderPackOptions(shaderPackOptions, shaderPack);
    }

    private static void saveShaderPackOptions(ShaderOption[] paramArrayOfShaderOption, IShaderPack paramIShaderPack) {
        Properties localProperties = new Properties();
        if (shaderPackOptions != null) {
            for (int i = 0; i < paramArrayOfShaderOption.length; i++) {
                ShaderOption localShaderOption = paramArrayOfShaderOption[i];
                if ((localShaderOption.isChanged()) && (localShaderOption.isEnabled())) {
                    localProperties.setProperty(localShaderOption.getName(), localShaderOption.getValue());
                }
            }
        }
        try {
            saveOptionProperties(paramIShaderPack, localProperties);
        } catch (IOException localIOException) {
            Config.warn("[Shaders] Error saving configuration for " + shaderPack.getName());
            localIOException.printStackTrace();
        }
    }

    private static void saveOptionProperties(IShaderPack paramIShaderPack, Properties paramProperties)
            throws IOException {
        String str = shaderpacksdirname + "/" + paramIShaderPack.getName() + ".txt";
        File localFile = new File(Minecraft.getMinecraft().mcDataDir, str);
        if (paramProperties.isEmpty()) {
            localFile.delete();
        } else {
            FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
            paramProperties.store(localFileOutputStream, (String) null);
            localFileOutputStream.flush();
            localFileOutputStream.close();
        }
    }

    private static ShaderOption[] loadShaderPackOptions() {
        try {
            ShaderOption[] arrayOfShaderOption = ShaderPackParser.parseShaderPackOptions(shaderPack, programNames, shaderPackDimensions);
            Properties localProperties = loadOptionProperties(shaderPack);
            for (int i = 0; i < arrayOfShaderOption.length; i++) {
                ShaderOption localShaderOption = arrayOfShaderOption[i];
                String str = localProperties.getProperty(localShaderOption.getName());
                if (str != null) {
                    localShaderOption.resetValue();
                    if (!localShaderOption.setValue(str)) {
                        Config.warn("[Shaders] Invalid value, option: " + localShaderOption.getName() + ", value: " + str);
                    }
                }
            }
            return arrayOfShaderOption;
        } catch (IOException localIOException) {
            Config.warn("[Shaders] Error reading configuration for " + shaderPack.getName());
            localIOException.printStackTrace();
        }
        return null;
    }

    private static Properties loadOptionProperties(IShaderPack paramIShaderPack)
            throws IOException {
        Properties localProperties = new Properties();
        String str = shaderpacksdirname + "/" + paramIShaderPack.getName() + ".txt";
        File localFile = new File(Minecraft.getMinecraft().mcDataDir, str);
        if ((localFile.exists()) && (localFile.isFile()) && (localFile.canRead())) {
            FileInputStream localFileInputStream = new FileInputStream(localFile);
            localProperties.load(localFileInputStream);
            localFileInputStream.close();
            return localProperties;
        }
        return localProperties;
    }

    public static ShaderOption[] getChangedOptions(ShaderOption[] paramArrayOfShaderOption) {
        ArrayList localArrayList = new ArrayList();
        for (int i = 0; i < paramArrayOfShaderOption.length; i++) {
            ShaderOption localShaderOption = paramArrayOfShaderOption[i];
            if ((localShaderOption.isEnabled()) && (localShaderOption.isChanged())) {
                localArrayList.add(localShaderOption);
            }
        }
        ShaderOption[] arrayOfShaderOption = (ShaderOption[]) (ShaderOption[]) localArrayList.toArray(new ShaderOption[localArrayList.size()]);
        return arrayOfShaderOption;
    }

    private static String applyOptions(String paramString, ShaderOption[] paramArrayOfShaderOption) {
        if ((paramArrayOfShaderOption != null) && (paramArrayOfShaderOption.length > 0)) {
            for (int i = 0; i < paramArrayOfShaderOption.length; i++) {
                ShaderOption localShaderOption = paramArrayOfShaderOption[i];
                String str = localShaderOption.getName();
                if (localShaderOption.matchesLine(paramString)) {
                    paramString = localShaderOption.getSourceLine();
                    break;
                }
            }
            return paramString;
        }
        return paramString;
    }

    static ArrayList listOfShaders() {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(packNameNone);
        localArrayList.add(packNameDefault);
        try {
            if (!shaderpacksdir.exists()) {
                shaderpacksdir.mkdir();
            }
            File[] arrayOfFile = shaderpacksdir.listFiles();
            for (int i = 0; i < arrayOfFile.length; i++) {
                File localFile1 = arrayOfFile[i];
                String str = localFile1.getName();
                if (localFile1.isDirectory()) {
                    File localFile2 = new File(localFile1, "shaders");
                    if ((localFile2.exists()) && (localFile2.isDirectory())) {
                        localArrayList.add(str);
                    }
                } else if ((localFile1.isFile()) && (str.toLowerCase().endsWith(".zip"))) {
                    localArrayList.add(str);
                }
            }
        } catch (Exception localException) {
        }
        return localArrayList;
    }

    static String versiontostring(int paramInt) {
        String str = Integer.toString(paramInt);
        return Integer.toString(Integer.parseInt(str.substring(1, 3))) + "." + Integer.toString(Integer.parseInt(str.substring(3, 5))) + "." + Integer.toString(Integer.parseInt(str.substring(5)));
    }

    static void checkOptifine() {
    }

    public static int checkFramebufferStatus(String paramString) {
        int i = EXTFramebufferObject.glCheckFramebufferStatusEXT(36160);
        if (i != 36053) {
            System.err.format("FramebufferStatus 0x%04X at %s\n", new Object[]{Integer.valueOf(i), paramString});
        }
        return i;
    }

    public static int checkGLError(String paramString) {
        int i = GL11.glGetError();
        if (i != 0) {
            int j = 0;
            if (j == 0) {
                if (i == 1286) {
                    int k = EXTFramebufferObject.glCheckFramebufferStatusEXT(36160);
                    System.err.format("GL error 0x%04X: %s (Fb status 0x%04X) at %s\n", new Object[]{Integer.valueOf(i), GLU.gluErrorString(i), Integer.valueOf(k), paramString});
                } else {
                    System.err.format("GL error 0x%04X: %s at %s\n", new Object[]{Integer.valueOf(i), GLU.gluErrorString(i), paramString});
                }
            }
        }
        return i;
    }

    public static int checkGLError(String paramString1, String paramString2) {
        int i = GL11.glGetError();
        if (i != 0) {
            System.err.format("GL error 0x%04x: %s at %s %s\n", new Object[]{Integer.valueOf(i), GLU.gluErrorString(i), paramString1, paramString2});
        }
        return i;
    }

    public static int checkGLError(String paramString1, String paramString2, String paramString3) {
        int i = GL11.glGetError();
        if (i != 0) {
            System.err.format("GL error 0x%04x: %s at %s %s %s\n", new Object[]{Integer.valueOf(i), GLU.gluErrorString(i), paramString1, paramString2, paramString3});
        }
        return i;
    }

    private static void printChat(String paramString) {
        mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(paramString));
    }

    private static void printChatAndLogError(String paramString) {
        SMCLog.severe(paramString);
        mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(paramString));
    }

    public static void printIntBuffer(String paramString, IntBuffer paramIntBuffer) {
        StringBuilder localStringBuilder = new StringBuilder(128);
        localStringBuilder.append(paramString).append(" [pos ").append(paramIntBuffer.position()).append(" lim ").append(paramIntBuffer.limit()).append(" cap ").append(paramIntBuffer.capacity()).append(" :");
        int i = paramIntBuffer.limit();
        for (int j = 0; j < i; j++) {
            localStringBuilder.append(" ").append(paramIntBuffer.get(j));
        }
        localStringBuilder.append("]");
        SMCLog.info(localStringBuilder.toString());
    }

    public static void startup(Minecraft paramMinecraft) {
        checkShadersModInstalled();
        mc = paramMinecraft;
        paramMinecraft = Minecraft.getMinecraft();
        capabilities = GLContext.getCapabilities();
        glVersionString = GL11.glGetString(7938);
        glVendorString = GL11.glGetString(7936);
        glRendererString = GL11.glGetString(7937);
        SMCLog.info("ShadersMod version: 2.4.12");
        SMCLog.info("OpenGL Version: " + glVersionString);
        SMCLog.info("Vendor:  " + glVendorString);
        SMCLog.info("Renderer: " + glRendererString);
        SMCLog.info("Capabilities: " + (capabilities.OpenGL20 ? " 2.0 " : " - ") + (capabilities.OpenGL21 ? " 2.1 " : " - ") + (capabilities.OpenGL30 ? " 3.0 " : " - ") + (capabilities.OpenGL32 ? " 3.2 " : " - ") + (capabilities.OpenGL40 ? " 4.0 " : " - "));
        SMCLog.info("GL_MAX_DRAW_BUFFERS: " + GL11.glGetInteger(34852));
        SMCLog.info("GL_MAX_COLOR_ATTACHMENTS_EXT: " + GL11.glGetInteger(36063));
        SMCLog.info("GL_MAX_TEXTURE_IMAGE_UNITS: " + GL11.glGetInteger(34930));
        hasGlGenMipmap = capabilities.OpenGL30;
        loadConfig();
    }

    private static String toStringYN(boolean paramBoolean) {
        return paramBoolean ? "Y" : "N";
    }

    public static void updateBlockLightLevel() {
        if (isOldLighting()) {
            blockLightLevel05 = 0.5F;
            blockLightLevel06 = 0.6F;
            blockLightLevel08 = 0.8F;
        } else {
            blockLightLevel05 = 1.0F;
            blockLightLevel06 = 1.0F;
            blockLightLevel08 = 1.0F;
        }
    }

    public static boolean isOldHandLight() {
        return !shaderPackOldHandLight.isDefault() ? shaderPackOldHandLight.isTrue() : !configOldHandLight.isDefault() ? configOldHandLight.isTrue() : true;
    }

    public static boolean isDynamicHandLight() {
        return !shaderPackDynamicHandLight.isDefault() ? shaderPackDynamicHandLight.isTrue() : true;
    }

    public static boolean isOldLighting() {
        return !shaderPackOldLighting.isDefault() ? shaderPackOldLighting.isTrue() : !configOldLighting.isDefault() ? configOldLighting.isTrue() : true;
    }

    public static boolean isRenderShadowTranslucent() {
        return !shaderPackShadowTranslucent.isFalse();
    }

    public static boolean isUnderwaterOverlay() {
        return !shaderPackUnderwaterOverlay.isFalse();
    }

    public static boolean isSun() {
        return !shaderPackSun.isFalse();
    }

    public static boolean isMoon() {
        return !shaderPackMoon.isFalse();
    }

    public static boolean isVignette() {
        return !shaderPackVignette.isFalse();
    }

    public static boolean isRenderBackFace(EnumWorldBlockLayer paramEnumWorldBlockLayer) {
        switch (paramEnumWorldBlockLayer) {
            case SOLID:
                return shaderPackBackFaceSolid.isTrue();
            case CUTOUT:
                return shaderPackBackFaceCutout.isTrue();
            case CUTOUT_MIPPED:
                return shaderPackBackFaceCutoutMipped.isTrue();
            case TRANSLUCENT:
                return shaderPackBackFaceTranslucent.isTrue();
        }
        return false;
    }

    public static void init() {
        int i;
        if (!isInitializedOnce) {
            isInitializedOnce = true;
            i = 1;
        } else {
            i = 0;
        }
        if (!isShaderPackInitialized) {
            checkGLError("Shaders.init pre");
            if ((getShaderPackName() == null) || (!capabilities.OpenGL20)) {
                printChatAndLogError("No OpenGL 2.0");
            }
            if (!capabilities.GL_EXT_framebuffer_object) {
                printChatAndLogError("No EXT_framebuffer_object");
            }
            dfbDrawBuffers.position(0).limit(8);
            dfbColorTextures.position(0).limit(16);
            dfbDepthTextures.position(0).limit(3);
            sfbDrawBuffers.position(0).limit(8);
            sfbDepthTextures.position(0).limit(2);
            sfbColorTextures.position(0).limit(8);
            usedColorBuffers = 4;
            usedDepthBuffers = 1;
            usedShadowColorBuffers = 0;
            usedShadowDepthBuffers = 0;
            usedColorAttachs = 1;
            usedDrawBuffers = 1;
            Arrays.fill((int[]) gbuffersFormat, 6408);
            Arrays.fill(gbuffersClear, true);
            Arrays.fill(shadowHardwareFilteringEnabled, false);
            Arrays.fill(shadowMipmapEnabled, false);
            Arrays.fill(shadowFilterNearest, false);
            Arrays.fill(shadowColorMipmapEnabled, false);
            Arrays.fill(shadowColorFilterNearest, false);
            centerDepthSmoothEnabled = false;
            noiseTextureEnabled = false;
            sunPathRotation = 0.0F;
            shadowIntervalSize = 2.0F;
            shadowDistanceRenderMul = -1.0F;
            aoLevel = -1.0F;
            useEntityAttrib = false;
            useMidTexCoordAttrib = false;
            useMultiTexCoord3Attrib = false;
            useTangentAttrib = false;
            waterShadowEnabled = false;
            updateChunksErrorRecorded = false;
            updateBlockLightLevel();
            ShaderProfile localShaderProfile = ShaderUtils.detectProfile(shaderPackProfiles, shaderPackOptions, false);
            String str1 = "";
            if (currentWorld != null) {
                j = currentWorld.provider.getDimensionId();
                if (shaderPackDimensions.contains(Integer.valueOf(j))) {
                    str1 = "world" + j + "/";
                }
            }
            if (saveFinalShaders) {
                clearDirectory(new File(shaderpacksdir, "debug"));
            }
            String str4;
            Object localObject;
            int m;
            for (int j = 0; j < 33; j++) {
                String str2 = programNames[j];
                if (str2.equals("")) {
                    programsID[j] = (programsRef[j] = 0);
                    programsDrawBufSettings[j] = null;
                    programsColorAtmSettings[j] = null;
                    programsCompositeMipmapSetting[j] = 0;
                } else {
                    newDrawBufSetting = null;
                    newColorAtmSetting = null;
                    newCompositeMipmapSetting = 0;
                    str4 = str1 + str2;
                    if ((localShaderProfile != null) && (localShaderProfile.isProgramDisabled(str4))) {
                        SMCLog.info("Program disabled: " + str4);
                        str2 = "<disabled>";
                        str4 = str1 + str2;
                    }
                    localObject = "/shaders/" + str4;
                    m = setupProgram(j, (String) localObject + ".vsh", (String) localObject + ".fsh");
                    if (m > 0) {
                        SMCLog.info("Program loaded: " + str4);
                    }
                    programsID[j] = (programsRef[j] = m);
                    programsDrawBufSettings[j] = (m != 0 ? newDrawBufSetting : null);
                    programsColorAtmSettings[j] = (m != 0 ? newColorAtmSetting : null);
                    programsCompositeMipmapSetting[j] = (m != 0 ? newCompositeMipmapSetting : 0);
                }
            }
            j = GL11.glGetInteger(34852);
            new HashMap();
            for (String str3 = 0; str3 < 33; str3++) {
                Arrays.fill(programsToggleColorTextures[str3], false);
                if (str3 == 29) {
                    programsDrawBuffers[str3] = null;
                } else if (programsID[str3] == 0) {
                    if (str3 == 30) {
                        programsDrawBuffers[str3] = drawBuffersNone;
                    } else {
                        programsDrawBuffers[str3] = drawBuffersColorAtt0;
                    }
                } else {
                    str4 = programsDrawBufSettings[str3];
                    if (str4 != null) {
                        localObject = drawBuffersBuffer[str3];
                        m = str4.length();
                        if (m > usedDrawBuffers) {
                            usedDrawBuffers = m;
                        }
                        if (m > j) {
                            m = j;
                        }
                        programsDrawBuffers[str3] = localObject;
                        ((IntBuffer) localObject).limit(m);
                        for (int n = 0; n < m; n++) {
                            int i1 = 0;
                            if (str4.length() > n) {
                                int i2 = str4.charAt(n) - '0';
                                if (str3 != 30) {
                                    if ((i2 >= 0) && (i2 <= 7)) {
                                        programsToggleColorTextures[str3][i2] = 1;
                                        i1 = i2 | 0x8CE0;
                                        if (i2 > usedColorAttachs) {
                                            usedColorAttachs = i2;
                                        }
                                        if (i2 > usedColorBuffers) {
                                            usedColorBuffers = i2;
                                        }
                                    }
                                } else if ((i2 >= 0) && (i2 <= 1)) {
                                    i1 = i2 | 0x8CE0;
                                    if (i2 > usedShadowColorBuffers) {
                                        usedShadowColorBuffers = i2;
                                    }
                                }
                            }
                            ((IntBuffer) localObject).put(n, i1);
                        }
                    } else if ((str3 != 30) && (str3 != 31) && (str3 != 32)) {
                        programsDrawBuffers[str3] = dfbDrawBuffers;
                        usedDrawBuffers = usedColorBuffers;
                        Arrays.fill(programsToggleColorTextures[str3], 0, usedColorBuffers, true);
                    } else {
                        programsDrawBuffers[str3] = sfbDrawBuffers;
                    }
                }
            }
            usedColorAttachs = usedColorBuffers;
            shadowPassInterval = usedShadowDepthBuffers > 0 ? 1 : 0;
            shouldSkipDefaultShadow = usedShadowDepthBuffers > 0;
            SMCLog.info("usedColorBuffers: " + usedColorBuffers);
            SMCLog.info("usedDepthBuffers: " + usedDepthBuffers);
            SMCLog.info("usedShadowColorBuffers: " + usedShadowColorBuffers);
            SMCLog.info("usedShadowDepthBuffers: " + usedShadowDepthBuffers);
            SMCLog.info("usedColorAttachs: " + usedColorAttachs);
            SMCLog.info("usedDrawBuffers: " + usedDrawBuffers);
            dfbDrawBuffers.position(0).limit(usedDrawBuffers);
            dfbColorTextures.position(0).limit(usedColorBuffers * 2);
            for (str3 = 0; str3 < usedDrawBuffers; str3++) {
                dfbDrawBuffers.put(str3, 0x8CE0 | str3);
            }
            if (usedDrawBuffers > j) {
                printChatAndLogError("[Shaders] Error: Not enough draw buffers, needed: " + usedDrawBuffers + ", available: " + j);
            }
            sfbDrawBuffers.position(0).limit(usedShadowColorBuffers);
            for (str3 = 0; str3 < usedShadowColorBuffers; str3++) {
                sfbDrawBuffers.put(str3, 0x8CE0 | str3);
            }
            for (str3 = 0; str3 < 33; str3++) {
                int k;
                for (str4 = str3; (programsID[str4] == 0) && (programBackups[str4] != str4); k = programBackups[str4]) {
                }
                if ((k != str3) && (str3 != 30)) {
                    programsID[str3] = programsID[k];
                    programsDrawBufSettings[str3] = programsDrawBufSettings[k];
                    programsDrawBuffers[str3] = programsDrawBuffers[k];
                }
            }
            resize();
            resizeShadow();
            if (noiseTextureEnabled) {
                setupNoiseTexture();
            }
            if (defaultTexture == null) {
                defaultTexture = ShadersTex.createDefaultTexture();
            }
            GlStateManager.pushMatrix();
            GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
            preCelestialRotate();
            postCelestialRotate();
            GlStateManager.popMatrix();
            isShaderPackInitialized = true;
            loadEntityDataMap();
            resetDisplayList();
            if (i == 0) {
            }
            checkGLError("Shaders.init");
        }
    }

    public static void resetDisplayList() {
        numberResetDisplayList |= 0x1;
        needResetModels = true;
        SMCLog.info("Reset world renderers");
        mc.renderGlobal.loadRenderers();
    }

    public static void resetDisplayListModels() {
        if (needResetModels) {
            needResetModels = false;
            SMCLog.info("Reset model renderers");
            Iterator localIterator = mc.getRenderManager().getEntityRenderMap().values().iterator();
            while (localIterator.hasNext()) {
                Object localObject = localIterator.next();
                if ((localObject instanceof RendererLivingEntity)) {
                    RendererLivingEntity localRendererLivingEntity = (RendererLivingEntity) localObject;
                    resetDisplayListModel(localRendererLivingEntity.getMainModel());
                }
            }
        }
    }

    public static void resetDisplayListModel(ModelBase paramModelBase) {
        if (paramModelBase != null) {
            Iterator localIterator = paramModelBase.boxList.iterator();
            while (localIterator.hasNext()) {
                Object localObject = localIterator.next();
                if ((localObject instanceof ModelRenderer)) {
                    resetDisplayListModelRenderer((ModelRenderer) localObject);
                }
            }
        }
    }

    public static void resetDisplayListModelRenderer(ModelRenderer paramModelRenderer) {
        paramModelRenderer.resetDisplayList();
        if (paramModelRenderer.childModels != null) {
            int i = 0;
            int j = paramModelRenderer.childModels.size();
            while (i < j) {
                resetDisplayListModelRenderer((ModelRenderer) paramModelRenderer.childModels.get(i));
                i++;
            }
        }
    }

    private static int setupProgram(int paramInt, String paramString1, String paramString2) {
        checkGLError("pre setupProgram");
        int i = ARBShaderObjects.glCreateProgramObjectARB();
        checkGLError("create");
        if (i != 0) {
            progUseEntityAttrib = false;
            progUseMidTexCoordAttrib = false;
            progUseTangentAttrib = false;
            int j = createVertShader(paramString1);
            int k = createFragShader(paramString2);
            checkGLError("create");
            if ((j == 0) && (k == 0)) {
                ARBShaderObjects.glDeleteObjectARB(i);
                i = 0;
            } else {
                if (j != 0) {
                    ARBShaderObjects.glAttachObjectARB(i, j);
                    checkGLError("attach");
                }
                if (k != 0) {
                    ARBShaderObjects.glAttachObjectARB(i, k);
                    checkGLError("attach");
                }
                if (progUseEntityAttrib) {
                    ARBVertexShader.glBindAttribLocationARB(i, entityAttrib, "mc_Entity");
                    checkGLError("mc_Entity");
                }
                if (progUseMidTexCoordAttrib) {
                    ARBVertexShader.glBindAttribLocationARB(i, midTexCoordAttrib, "mc_midTexCoord");
                    checkGLError("mc_midTexCoord");
                }
                if (progUseTangentAttrib) {
                    ARBVertexShader.glBindAttribLocationARB(i, tangentAttrib, "at_tangent");
                    checkGLError("at_tangent");
                }
                ARBShaderObjects.glLinkProgramARB(i);
                if (GL20.glGetProgrami(i, 35714) != 1) {
                    SMCLog.severe("Error linking program: " + i);
                }
                printLogInfo(i, paramString1 + ", " + paramString2);
                if (j != 0) {
                    ARBShaderObjects.glDetachObjectARB(i, j);
                    ARBShaderObjects.glDeleteObjectARB(j);
                }
                if (k != 0) {
                    ARBShaderObjects.glDetachObjectARB(i, k);
                    ARBShaderObjects.glDeleteObjectARB(k);
                }
                programsID[paramInt] = i;
                useProgram(paramInt);
                ARBShaderObjects.glValidateProgramARB(i);
                useProgram(0);
                printLogInfo(i, paramString1 + ", " + paramString2);
                int m = GL20.glGetProgrami(i, 35715);
                if (m != 1) {
                    String str = "\"";
                    printChatAndLogError("[Shaders] Error: Invalid program " + str + programNames[paramInt] + str);
                    ARBShaderObjects.glDeleteObjectARB(i);
                    i = 0;
                }
            }
        }
        return i;
    }

    private static int createVertShader(String paramString) {
        int i = ARBShaderObjects.glCreateShaderObjectARB(35633);
        if (i == 0) {
            return 0;
        }
        StringBuilder localStringBuilder = new StringBuilder(131072);
        BufferedReader localBufferedReader = null;
        try {
            localBufferedReader = new BufferedReader(new InputStreamReader(shaderPack.getResourceAsStream(paramString)));
        } catch (Exception localException1) {
            try {
                localBufferedReader = new BufferedReader(new FileReader(new File(paramString)));
            } catch (Exception localException2) {
                ARBShaderObjects.glDeleteObjectARB(i);
                return 0;
            }
        }
        ShaderOption[] arrayOfShaderOption = getChangedOptions(shaderPackOptions);
        ArrayList localArrayList = new ArrayList();
        if (localBufferedReader != null) {
            try {
                localBufferedReader = ShaderPackParser.resolveIncludes(localBufferedReader, paramString, shaderPack, 0, localArrayList, 0);
                for (; ; ) {
                    String str = localBufferedReader.readLine();
                    if (str == null) {
                        localBufferedReader.close();
                        break;
                    }
                    str = applyOptions(str, arrayOfShaderOption);
                    localStringBuilder.append(str).append('\n');
                    if (str.matches("attribute [_a-zA-Z0-9]+ mc_Entity.*")) {
                        useEntityAttrib = true;
                        progUseEntityAttrib = true;
                    } else if (str.matches("attribute [_a-zA-Z0-9]+ mc_midTexCoord.*")) {
                        useMidTexCoordAttrib = true;
                        progUseMidTexCoordAttrib = true;
                    } else if (str.matches(".*gl_MultiTexCoord3.*")) {
                        useMultiTexCoord3Attrib = true;
                    } else if (str.matches("attribute [_a-zA-Z0-9]+ at_tangent.*")) {
                        useTangentAttrib = true;
                        progUseTangentAttrib = true;
                    }
                }
            } catch (Exception localException3) {
                SMCLog.severe("Couldn't read " + paramString + "!");
                localException3.printStackTrace();
                ARBShaderObjects.glDeleteObjectARB(i);
                return 0;
            }
        }
        if (saveFinalShaders) {
            saveShader(paramString, localStringBuilder.toString());
        }
        ARBShaderObjects.glShaderSourceARB(i, localStringBuilder);
        ARBShaderObjects.glCompileShaderARB(i);
        if (GL20.glGetShaderi(i, 35713) != 1) {
            SMCLog.severe("Error compiling vertex shader: " + paramString);
        }
        printShaderLogInfo(i, paramString, localArrayList);
        return i;
    }

    private static int createFragShader(String paramString) {
        int i = ARBShaderObjects.glCreateShaderObjectARB(35632);
        if (i == 0) {
            return 0;
        }
        StringBuilder localStringBuilder = new StringBuilder(131072);
        BufferedReader localBufferedReader = null;
        try {
            localBufferedReader = new BufferedReader(new InputStreamReader(shaderPack.getResourceAsStream(paramString)));
        } catch (Exception localException1) {
            try {
                localBufferedReader = new BufferedReader(new FileReader(new File(paramString)));
            } catch (Exception localException2) {
                ARBShaderObjects.glDeleteObjectARB(i);
                return 0;
            }
        }
        ShaderOption[] arrayOfShaderOption = getChangedOptions(shaderPackOptions);
        ArrayList localArrayList = new ArrayList();
        if (localBufferedReader != null) {
            try {
                localBufferedReader = ShaderPackParser.resolveIncludes(localBufferedReader, paramString, shaderPack, 0, localArrayList, 0);
                for (; ; ) {
                    String str1 = localBufferedReader.readLine();
                    if (str1 == null) {
                        localBufferedReader.close();
                        break;
                    }
                    str1 = applyOptions(str1, arrayOfShaderOption);
                    localStringBuilder.append(str1).append('\n');
                    if (!str1.matches("#version .*")) {
                        if (str1.matches("uniform [ _a-zA-Z0-9]+ shadow;.*")) {
                            if (usedShadowDepthBuffers < 1) {
                                usedShadowDepthBuffers = 1;
                            }
                        } else if (str1.matches("uniform [ _a-zA-Z0-9]+ watershadow;.*")) {
                            waterShadowEnabled = true;
                            if (usedShadowDepthBuffers < 2) {
                                usedShadowDepthBuffers = 2;
                            }
                        } else if (str1.matches("uniform [ _a-zA-Z0-9]+ shadowtex0;.*")) {
                            if (usedShadowDepthBuffers < 1) {
                                usedShadowDepthBuffers = 1;
                            }
                        } else if (str1.matches("uniform [ _a-zA-Z0-9]+ shadowtex1;.*")) {
                            if (usedShadowDepthBuffers < 2) {
                                usedShadowDepthBuffers = 2;
                            }
                        } else if (str1.matches("uniform [ _a-zA-Z0-9]+ shadowcolor;.*")) {
                            if (usedShadowColorBuffers < 1) {
                                usedShadowColorBuffers = 1;
                            }
                        } else if (str1.matches("uniform [ _a-zA-Z0-9]+ shadowcolor0;.*")) {
                            if (usedShadowColorBuffers < 1) {
                                usedShadowColorBuffers = 1;
                            }
                        } else if (str1.matches("uniform [ _a-zA-Z0-9]+ shadowcolor1;.*")) {
                            if (usedShadowColorBuffers < 2) {
                                usedShadowColorBuffers = 2;
                            }
                        } else if (str1.matches("uniform [ _a-zA-Z0-9]+ depthtex0;.*")) {
                            if (usedDepthBuffers < 1) {
                                usedDepthBuffers = 1;
                            }
                        } else if (str1.matches("uniform [ _a-zA-Z0-9]+ depthtex1;.*")) {
                            if (usedDepthBuffers < 2) {
                                usedDepthBuffers = 2;
                            }
                        } else if (str1.matches("uniform [ _a-zA-Z0-9]+ depthtex2;.*")) {
                            if (usedDepthBuffers < 3) {
                                usedDepthBuffers = 3;
                            }
                        } else if (str1.matches("uniform [ _a-zA-Z0-9]+ gdepth;.*")) {
                            if (gbuffersFormat[1] == 6408) {
                                gbuffersFormat[1] = 34836;
                            }
                        } else if ((usedColorBuffers < 5) && (str1.matches("uniform [ _a-zA-Z0-9]+ gaux1;.*"))) {
                            usedColorBuffers = 5;
                        } else if ((usedColorBuffers < 6) && (str1.matches("uniform [ _a-zA-Z0-9]+ gaux2;.*"))) {
                            usedColorBuffers = 6;
                        } else if ((usedColorBuffers < 7) && (str1.matches("uniform [ _a-zA-Z0-9]+ gaux3;.*"))) {
                            usedColorBuffers = 7;
                        } else if ((usedColorBuffers < 8) && (str1.matches("uniform [ _a-zA-Z0-9]+ gaux4;.*"))) {
                            usedColorBuffers = 8;
                        } else if ((usedColorBuffers < 5) && (str1.matches("uniform [ _a-zA-Z0-9]+ colortex4;.*"))) {
                            usedColorBuffers = 5;
                        } else if ((usedColorBuffers < 6) && (str1.matches("uniform [ _a-zA-Z0-9]+ colortex5;.*"))) {
                            usedColorBuffers = 6;
                        } else if ((usedColorBuffers < 7) && (str1.matches("uniform [ _a-zA-Z0-9]+ colortex6;.*"))) {
                            usedColorBuffers = 7;
                        } else if ((usedColorBuffers < 8) && (str1.matches("uniform [ _a-zA-Z0-9]+ colortex7;.*"))) {
                            usedColorBuffers = 8;
                        } else if (str1.matches("uniform [ _a-zA-Z0-9]+ centerDepthSmooth;.*")) {
                            centerDepthSmoothEnabled = true;
                        } else {
                            Object localObject;
                            if (str1.matches("/\\* SHADOWRES:[0-9]+ \\*/.*")) {
                                localObject = str1.split("(:| )", 4);
                                SMCLog.info("Shadow map resolution: " + localObject[2]);
                                spShadowMapWidth = spShadowMapHeight = Integer.parseInt(localObject[2]);
                                shadowMapWidth = shadowMapHeight = Math.round(spShadowMapWidth * configShadowResMul);
                            } else if (str1.matches("[ \t]*const[ \t]*int[ \t]*shadowMapResolution[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                                localObject = str1.split("(=[ \t]*|;)");
                                SMCLog.info("Shadow map resolution: " + localObject[1]);
                                spShadowMapWidth = spShadowMapHeight = Integer.parseInt(localObject[1]);
                                shadowMapWidth = shadowMapHeight = Math.round(spShadowMapWidth * configShadowResMul);
                            } else if (str1.matches("/\\* SHADOWFOV:[0-9\\.]+ \\*/.*")) {
                                localObject = str1.split("(:| )", 4);
                                SMCLog.info("Shadow map field of view: " + localObject[2]);
                                shadowMapFOV = Float.parseFloat(localObject[2]);
                                shadowMapIsOrtho = false;
                            } else if (str1.matches("/\\* SHADOWHPL:[0-9\\.]+ \\*/.*")) {
                                localObject = str1.split("(:| )", 4);
                                SMCLog.info("Shadow map half-plane: " + localObject[2]);
                                shadowMapHalfPlane = Float.parseFloat(localObject[2]);
                                shadowMapIsOrtho = true;
                            } else if (str1.matches("[ \t]*const[ \t]*float[ \t]*shadowDistance[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                                localObject = str1.split("(=[ \t]*|;)");
                                SMCLog.info("Shadow map distance: " + localObject[1]);
                                shadowMapHalfPlane = Float.parseFloat(localObject[1]);
                                shadowMapIsOrtho = true;
                            } else if (str1.matches("[ \t]*const[ \t]*float[ \t]*shadowDistanceRenderMul[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                                localObject = str1.split("(=[ \t]*|;)");
                                SMCLog.info("Shadow distance render mul: " + localObject[1]);
                                shadowDistanceRenderMul = Float.parseFloat(localObject[1]);
                            } else if (str1.matches("[ \t]*const[ \t]*float[ \t]*shadowIntervalSize[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                                localObject = str1.split("(=[ \t]*|;)");
                                SMCLog.info("Shadow map interval size: " + localObject[1]);
                                shadowIntervalSize = Float.parseFloat(localObject[1]);
                            } else if (str1.matches("[ \t]*const[ \t]*bool[ \t]*generateShadowMipmap[ \t]*=[ \t]*true[ \t]*;.*")) {
                                SMCLog.info("Generate shadow mipmap");
                                Arrays.fill(shadowMipmapEnabled, true);
                            } else if (str1.matches("[ \t]*const[ \t]*bool[ \t]*generateShadowColorMipmap[ \t]*=[ \t]*true[ \t]*;.*")) {
                                SMCLog.info("Generate shadow color mipmap");
                                Arrays.fill(shadowColorMipmapEnabled, true);
                            } else if (str1.matches("[ \t]*const[ \t]*bool[ \t]*shadowHardwareFiltering[ \t]*=[ \t]*true[ \t]*;.*")) {
                                SMCLog.info("Hardware shadow filtering enabled.");
                                Arrays.fill(shadowHardwareFilteringEnabled, true);
                            } else if (str1.matches("[ \t]*const[ \t]*bool[ \t]*shadowHardwareFiltering0[ \t]*=[ \t]*true[ \t]*;.*")) {
                                SMCLog.info("shadowHardwareFiltering0");
                                shadowHardwareFilteringEnabled[0] = true;
                            } else if (str1.matches("[ \t]*const[ \t]*bool[ \t]*shadowHardwareFiltering1[ \t]*=[ \t]*true[ \t]*;.*")) {
                                SMCLog.info("shadowHardwareFiltering1");
                                shadowHardwareFilteringEnabled[1] = true;
                            } else if (str1.matches("[ \t]*const[ \t]*bool[ \t]*(shadowtex0Mipmap|shadowtexMipmap)[ \t]*=[ \t]*true[ \t]*;.*")) {
                                SMCLog.info("shadowtex0Mipmap");
                                shadowMipmapEnabled[0] = true;
                            } else if (str1.matches("[ \t]*const[ \t]*bool[ \t]*(shadowtex1Mipmap)[ \t]*=[ \t]*true[ \t]*;.*")) {
                                SMCLog.info("shadowtex1Mipmap");
                                shadowMipmapEnabled[1] = true;
                            } else if (str1.matches("[ \t]*const[ \t]*bool[ \t]*(shadowcolor0Mipmap|shadowColor0Mipmap)[ \t]*=[ \t]*true[ \t]*;.*")) {
                                SMCLog.info("shadowcolor0Mipmap");
                                shadowColorMipmapEnabled[0] = true;
                            } else if (str1.matches("[ \t]*const[ \t]*bool[ \t]*(shadowcolor1Mipmap|shadowColor1Mipmap)[ \t]*=[ \t]*true[ \t]*;.*")) {
                                SMCLog.info("shadowcolor1Mipmap");
                                shadowColorMipmapEnabled[1] = true;
                            } else if (str1.matches("[ \t]*const[ \t]*bool[ \t]*(shadowtex0Nearest|shadowtexNearest|shadow0MinMagNearest)[ \t]*=[ \t]*true[ \t]*;.*")) {
                                SMCLog.info("shadowtex0Nearest");
                                shadowFilterNearest[0] = true;
                            } else if (str1.matches("[ \t]*const[ \t]*bool[ \t]*(shadowtex1Nearest|shadow1MinMagNearest)[ \t]*=[ \t]*true[ \t]*;.*")) {
                                SMCLog.info("shadowtex1Nearest");
                                shadowFilterNearest[1] = true;
                            } else if (str1.matches("[ \t]*const[ \t]*bool[ \t]*(shadowcolor0Nearest|shadowColor0Nearest|shadowColor0MinMagNearest)[ \t]*=[ \t]*true[ \t]*;.*")) {
                                SMCLog.info("shadowcolor0Nearest");
                                shadowColorFilterNearest[0] = true;
                            } else if (str1.matches("[ \t]*const[ \t]*bool[ \t]*(shadowcolor1Nearest|shadowColor1Nearest|shadowColor1MinMagNearest)[ \t]*=[ \t]*true[ \t]*;.*")) {
                                SMCLog.info("shadowcolor1Nearest");
                                shadowColorFilterNearest[1] = true;
                            } else if (str1.matches("/\\* WETNESSHL:[0-9\\.]+ \\*/.*")) {
                                localObject = str1.split("(:| )", 4);
                                SMCLog.info("Wetness halflife: " + localObject[2]);
                                wetnessHalfLife = Float.parseFloat(localObject[2]);
                            } else if (str1.matches("[ \t]*const[ \t]*float[ \t]*wetnessHalflife[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                                localObject = str1.split("(=[ \t]*|;)");
                                SMCLog.info("Wetness halflife: " + localObject[1]);
                                wetnessHalfLife = Float.parseFloat(localObject[1]);
                            } else if (str1.matches("/\\* DRYNESSHL:[0-9\\.]+ \\*/.*")) {
                                localObject = str1.split("(:| )", 4);
                                SMCLog.info("Dryness halflife: " + localObject[2]);
                                drynessHalfLife = Float.parseFloat(localObject[2]);
                            } else if (str1.matches("[ \t]*const[ \t]*float[ \t]*drynessHalflife[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                                localObject = str1.split("(=[ \t]*|;)");
                                SMCLog.info("Dryness halflife: " + localObject[1]);
                                drynessHalfLife = Float.parseFloat(localObject[1]);
                            } else if (str1.matches("[ \t]*const[ \t]*float[ \t]*eyeBrightnessHalflife[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                                localObject = str1.split("(=[ \t]*|;)");
                                SMCLog.info("Eye brightness halflife: " + localObject[1]);
                                eyeBrightnessHalflife = Float.parseFloat(localObject[1]);
                            } else if (str1.matches("[ \t]*const[ \t]*float[ \t]*centerDepthHalflife[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                                localObject = str1.split("(=[ \t]*|;)");
                                SMCLog.info("Center depth halflife: " + localObject[1]);
                                centerDepthSmoothHalflife = Float.parseFloat(localObject[1]);
                            } else if (str1.matches("[ \t]*const[ \t]*float[ \t]*sunPathRotation[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                                localObject = str1.split("(=[ \t]*|;)");
                                SMCLog.info("Sun path rotation: " + localObject[1]);
                                sunPathRotation = Float.parseFloat(localObject[1]);
                            } else if (str1.matches("[ \t]*const[ \t]*float[ \t]*ambientOcclusionLevel[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                                localObject = str1.split("(=[ \t]*|;)");
                                SMCLog.info("AO Level: " + localObject[1]);
                                aoLevel = Config.limit(Float.parseFloat(localObject[1]), 0.0F, 1.0F);
                            } else if (str1.matches("[ \t]*const[ \t]*int[ \t]*superSamplingLevel[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                                localObject = str1.split("(=[ \t]*|;)");
                                int j = Integer.parseInt(localObject[1]);
                                if (j > 1) {
                                    SMCLog.info("Super sampling level: " + j + "x");
                                    superSamplingLevel = j;
                                } else {
                                    superSamplingLevel = 1;
                                }
                            } else if (str1.matches("[ \t]*const[ \t]*int[ \t]*noiseTextureResolution[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                                localObject = str1.split("(=[ \t]*|;)");
                                SMCLog.info("Noise texture enabled");
                                SMCLog.info("Noise texture resolution: " + localObject[1]);
                                noiseTextureResolution = Integer.parseInt(localObject[1]);
                                noiseTextureEnabled = true;
                            } else {
                                String str2;
                                if (str1.matches("[ \t]*const[ \t]*int[ \t]*\\w+Format[ \t]*=[ \t]*[RGBA0123456789FUI_SNORM]*[ \t]*;.*")) {
                                    localObject = gbufferFormatPattern.matcher(str1);
                                    ((Matcher) localObject).matches();
                                    str2 = ((Matcher) localObject).group(1);
                                    String str3 = ((Matcher) localObject).group(2);
                                    int m = getBufferIndexFromString(str2);
                                    int n = getTextureFormatFromString(str3);
                                    if ((m >= 0) && (n != 0)) {
                                        gbuffersFormat[m] = n;
                                        SMCLog.info("%s format: %s", new Object[]{str2, str3});
                                    }
                                } else {
                                    int k;
                                    if (str1.matches("[ \t]*const[ \t]*bool[ \t]*\\w+Clear[ \t]*=[ \t]*false[ \t]*;.*")) {
                                        if (paramString.matches(".*composite[0-9]?.fsh")) {
                                            localObject = gbufferClearPattern.matcher(str1);
                                            ((Matcher) localObject).matches();
                                            str2 = ((Matcher) localObject).group(1);
                                            k = getBufferIndexFromString(str2);
                                            if (k >= 0) {
                                                gbuffersClear[k] = false;
                                                SMCLog.info("%s clear disabled", new Object[]{str2});
                                            }
                                        }
                                    } else if (str1.matches("/\\* GAUX4FORMAT:RGBA32F \\*/.*")) {
                                        SMCLog.info("gaux4 format : RGB32AF");
                                        gbuffersFormat[7] = 34836;
                                    } else if (str1.matches("/\\* GAUX4FORMAT:RGB32F \\*/.*")) {
                                        SMCLog.info("gaux4 format : RGB32F");
                                        gbuffersFormat[7] = 34837;
                                    } else if (str1.matches("/\\* GAUX4FORMAT:RGB16 \\*/.*")) {
                                        SMCLog.info("gaux4 format : RGB16");
                                        gbuffersFormat[7] = 32852;
                                    } else if (str1.matches("[ \t]*const[ \t]*bool[ \t]*\\w+MipmapEnabled[ \t]*=[ \t]*true[ \t]*;.*")) {
                                        if ((paramString.matches(".*composite[0-9]?.fsh")) || (paramString.matches(".*final.fsh"))) {
                                            localObject = gbufferMipmapEnabledPattern.matcher(str1);
                                            ((Matcher) localObject).matches();
                                            str2 = ((Matcher) localObject).group(1);
                                            k = getBufferIndexFromString(str2);
                                            if (k >= 0) {
                                                newCompositeMipmapSetting ^= 1 >>> k;
                                                SMCLog.info("%s mipmap enabled", new Object[]{str2});
                                            }
                                        }
                                    } else if (str1.matches("/\\* DRAWBUFFERS:[0-7N]* \\*/.*")) {
                                        localObject = str1.split("(:| )", 4);
                                        newDrawBufSetting = localObject[2];
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception localException3) {
                SMCLog.severe("Couldn't read " + paramString + "!");
                localException3.printStackTrace();
                ARBShaderObjects.glDeleteObjectARB(i);
                return 0;
            }
        }
        if (saveFinalShaders) {
            saveShader(paramString, localStringBuilder.toString());
        }
        ARBShaderObjects.glShaderSourceARB(i, localStringBuilder);
        ARBShaderObjects.glCompileShaderARB(i);
        if (GL20.glGetShaderi(i, 35713) != 1) {
            SMCLog.severe("Error compiling fragment shader: " + paramString);
        }
        printShaderLogInfo(i, paramString, localArrayList);
        return i;
    }

    private static void saveShader(String paramString1, String paramString2) {
        try {
            File localFile = new File(shaderpacksdir, "debug/" + paramString1);
            localFile.getParentFile().mkdirs();
            Config.writeFile(localFile, paramString2);
        } catch (IOException localIOException) {
            Config.warn("Error saving: " + paramString1);
            localIOException.printStackTrace();
        }
    }

    private static void clearDirectory(File paramFile) {
        if ((paramFile.exists()) && (paramFile.isDirectory())) {
            File[] arrayOfFile = paramFile.listFiles();
            if (arrayOfFile != null) {
                for (int i = 0; i < arrayOfFile.length; i++) {
                    File localFile = arrayOfFile[i];
                    if (localFile.isDirectory()) {
                        clearDirectory(localFile);
                    }
                    localFile.delete();
                }
            }
        }
    }

    private static boolean printLogInfo(int paramInt, String paramString) {
        IntBuffer localIntBuffer = BufferUtils.createIntBuffer(1);
        ARBShaderObjects.glGetObjectParameterARB(paramInt, 35716, localIntBuffer);
        int i = localIntBuffer.get();
        if (i > 1) {
            ByteBuffer localByteBuffer = BufferUtils.createByteBuffer(i);
            localIntBuffer.flip();
            ARBShaderObjects.glGetInfoLogARB(paramInt, localIntBuffer, localByteBuffer);
            byte[] arrayOfByte = new byte[i];
            localByteBuffer.get(arrayOfByte);
            if (arrayOfByte[(i - 1)] == 0) {
                arrayOfByte[(i - 1)] = 10;
            }
            String str = new String(arrayOfByte);
            SMCLog.info("Info log: " + paramString + "\n" + str);
            return false;
        }
        return true;
    }

    private static boolean printShaderLogInfo(int paramInt, String paramString, List<String> paramList) {
        IntBuffer localIntBuffer = BufferUtils.createIntBuffer(1);
        int i = GL20.glGetShaderi(paramInt, 35716);
        if (i <= 1) {
            return true;
        }
        for (int j = 0; j < paramList.size(); j++) {
            String str2 = (String) paramList.get(j);
            SMCLog.info("File: " + (j | 0x1) + " = " + str2);
        }
        String str1 = GL20.glGetShaderInfoLog(paramInt, i);
        SMCLog.info("Shader info log: " + paramString + "\n" + str1);
        return false;
    }

    public static void setDrawBuffers(IntBuffer paramIntBuffer) {
        if (paramIntBuffer == null) {
            paramIntBuffer = drawBuffersNone;
        }
        if (activeDrawBuffers != paramIntBuffer) {
            activeDrawBuffers = paramIntBuffer;
            GL20.glDrawBuffers(paramIntBuffer);
        }
    }

    public static void useProgram(int paramInt) {
        checkGLError("pre-useProgram");
        if (isShadowPass) {
            paramInt = 30;
            if (programsID[30] == 0) {
                normalMapEnabled = false;
                return;
            }
        }
        if (activeProgram != paramInt) {
            activeProgram = paramInt;
            ARBShaderObjects.glUseProgramObjectARB(programsID[paramInt]);
            if (programsID[paramInt] == 0) {
                normalMapEnabled = false;
            } else {
                if (checkGLError("useProgram ", programNames[paramInt]) != 0) {
                    programsID[paramInt] = 0;
                }
                IntBuffer localIntBuffer = programsDrawBuffers[paramInt];
                if (isRenderingDfb) {
                    setDrawBuffers(localIntBuffer);
                    checkGLError(programNames[paramInt], " draw buffers = ", programsDrawBufSettings[paramInt]);
                }
                activeCompositeMipmapSetting = programsCompositeMipmapSetting[paramInt];
                uniformEntityColor.setProgram(programsID[activeProgram]);
                uniformEntityId.setProgram(programsID[activeProgram]);
                uniformBlockEntityId.setProgram(programsID[activeProgram]);
                switch (paramInt) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 16:
                    case 18:
                    case 19:
                    case 20:
                        normalMapEnabled = true;
                        setProgramUniform1i("texture", 0);
                        setProgramUniform1i("lightmap", 1);
                        setProgramUniform1i("normals", 2);
                        setProgramUniform1i("specular", 3);
                        setProgramUniform1i("shadow", waterShadowEnabled ? 5 : 4);
                        setProgramUniform1i("watershadow", 4);
                        setProgramUniform1i("shadowtex0", 4);
                        setProgramUniform1i("shadowtex1", 5);
                        setProgramUniform1i("depthtex0", 6);
                        if (customTexturesGbuffers != null) {
                            setProgramUniform1i("gaux1", 7);
                            setProgramUniform1i("gaux2", 8);
                            setProgramUniform1i("gaux3", 9);
                            setProgramUniform1i("gaux4", 10);
                        }
                        setProgramUniform1i("depthtex1", 12);
                        setProgramUniform1i("shadowcolor", 13);
                        setProgramUniform1i("shadowcolor0", 13);
                        setProgramUniform1i("shadowcolor1", 14);
                        setProgramUniform1i("noisetex", 15);
                        break;
                    case 14:
                    case 15:
                    case 17:
                    default:
                        normalMapEnabled = false;
                        break;
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                        normalMapEnabled = false;
                        setProgramUniform1i("gcolor", 0);
                        setProgramUniform1i("gdepth", 1);
                        setProgramUniform1i("gnormal", 2);
                        setProgramUniform1i("composite", 3);
                        setProgramUniform1i("gaux1", 7);
                        setProgramUniform1i("gaux2", 8);
                        setProgramUniform1i("gaux3", 9);
                        setProgramUniform1i("gaux4", 10);
                        setProgramUniform1i("colortex0", 0);
                        setProgramUniform1i("colortex1", 1);
                        setProgramUniform1i("colortex2", 2);
                        setProgramUniform1i("colortex3", 3);
                        setProgramUniform1i("colortex4", 7);
                        setProgramUniform1i("colortex5", 8);
                        setProgramUniform1i("colortex6", 9);
                        setProgramUniform1i("colortex7", 10);
                        setProgramUniform1i("shadow", waterShadowEnabled ? 5 : 4);
                        setProgramUniform1i("watershadow", 4);
                        setProgramUniform1i("shadowtex0", 4);
                        setProgramUniform1i("shadowtex1", 5);
                        setProgramUniform1i("gdepthtex", 6);
                        setProgramUniform1i("depthtex0", 6);
                        setProgramUniform1i("depthtex1", 11);
                        setProgramUniform1i("depthtex2", 12);
                        setProgramUniform1i("shadowcolor", 13);
                        setProgramUniform1i("shadowcolor0", 13);
                        setProgramUniform1i("shadowcolor1", 14);
                        setProgramUniform1i("noisetex", 15);
                        break;
                    case 30:
                    case 31:
                    case 32:
                        setProgramUniform1i("tex", 0);
                        setProgramUniform1i("texture", 0);
                        setProgramUniform1i("lightmap", 1);
                        setProgramUniform1i("normals", 2);
                        setProgramUniform1i("specular", 3);
                        setProgramUniform1i("shadow", waterShadowEnabled ? 5 : 4);
                        setProgramUniform1i("watershadow", 4);
                        setProgramUniform1i("shadowtex0", 4);
                        setProgramUniform1i("shadowtex1", 5);
                        if (customTexturesGbuffers != null) {
                            setProgramUniform1i("gaux1", 7);
                            setProgramUniform1i("gaux2", 8);
                            setProgramUniform1i("gaux3", 9);
                            setProgramUniform1i("gaux4", 10);
                        }
                        setProgramUniform1i("shadowcolor", 13);
                        setProgramUniform1i("shadowcolor0", 13);
                        setProgramUniform1i("shadowcolor1", 14);
                        setProgramUniform1i("noisetex", 15);
                }
                Object localObject1 = mc.thePlayer != null ? mc.thePlayer.getHeldItem() : null;
                Object localObject2 = localObject1 != null ? ((ItemStack) localObject1).getItem() : null;
                int i = -1;
                Block localBlock = null;
                if (localObject2 != null) {
                    i = Item.itemRegistry.getIDForObject(localObject2);
                    localBlock = (Block) Block.blockRegistry.getObjectById(i);
                }
                int j = localBlock != null ? localBlock.getLightValue() : 0;
                setProgramUniform1i("heldItemId", i);
                setProgramUniform1i("heldBlockLightValue", j);
                setProgramUniform1i("fogMode", fogEnabled ? fogMode : 0);
                setProgramUniform3f("fogColor", fogColorR, fogColorG, fogColorB);
                setProgramUniform3f("skyColor", skyColorR, skyColorG, skyColorB);
                setProgramUniform1i("worldTime", (int) (worldTime % 24000L));
                setProgramUniform1i("worldDay", (int) (worldTime / 24000L));
                setProgramUniform1i("moonPhase", moonPhase);
                setProgramUniform1i("frameCounter", frameCounter);
                setProgramUniform1f("frameTime", frameTime);
                setProgramUniform1f("frameTimeCounter", frameTimeCounter);
                setProgramUniform1f("sunAngle", sunAngle);
                setProgramUniform1f("shadowAngle", shadowAngle);
                setProgramUniform1f("rainStrength", rainStrength);
                setProgramUniform1f("aspectRatio", renderWidth / renderHeight);
                setProgramUniform1f("viewWidth", renderWidth);
                setProgramUniform1f("viewHeight", renderHeight);
                setProgramUniform1f("near", 0.05F);
                setProgramUniform1f("far", mc.gameSettings.renderDistanceChunks * 16);
                setProgramUniform3f("sunPosition", sunPosition[0], sunPosition[1], sunPosition[2]);
                setProgramUniform3f("moonPosition", moonPosition[0], moonPosition[1], moonPosition[2]);
                setProgramUniform3f("shadowLightPosition", shadowLightPosition[0], shadowLightPosition[1], shadowLightPosition[2]);
                setProgramUniform3f("upPosition", upPosition[0], upPosition[1], upPosition[2]);
                setProgramUniform3f("previousCameraPosition", (float) previousCameraPositionX, (float) previousCameraPositionY, (float) previousCameraPositionZ);
                setProgramUniform3f("cameraPosition", (float) cameraPositionX, (float) cameraPositionY, (float) cameraPositionZ);
                setProgramUniformMatrix4ARB("gbufferModelView", false, modelView);
                setProgramUniformMatrix4ARB("gbufferModelViewInverse", false, modelViewInverse);
                setProgramUniformMatrix4ARB("gbufferPreviousProjection", false, previousProjection);
                setProgramUniformMatrix4ARB("gbufferProjection", false, projection);
                setProgramUniformMatrix4ARB("gbufferProjectionInverse", false, projectionInverse);
                setProgramUniformMatrix4ARB("gbufferPreviousModelView", false, previousModelView);
                if (usedShadowDepthBuffers > 0) {
                    setProgramUniformMatrix4ARB("shadowProjection", false, shadowProjection);
                    setProgramUniformMatrix4ARB("shadowProjectionInverse", false, shadowProjectionInverse);
                    setProgramUniformMatrix4ARB("shadowModelView", false, shadowModelView);
                    setProgramUniformMatrix4ARB("shadowModelViewInverse", false, shadowModelViewInverse);
                }
                setProgramUniform1f("wetness", wetness);
                setProgramUniform1f("eyeAltitude", eyePosY);
                setProgramUniform2i("eyeBrightness", eyeBrightness >> 65535, eyeBrightness & 0x10);
                setProgramUniform2i("eyeBrightnessSmooth", Math.round(eyeBrightnessFadeX), Math.round(eyeBrightnessFadeY));
                setProgramUniform2i("terrainTextureSize", terrainTextureSize[0], terrainTextureSize[1]);
                setProgramUniform1i("terrainIconSize", terrainIconSize);
                setProgramUniform1i("isEyeInWater", isEyeInWater);
                setProgramUniform1f("nightVision", nightVision);
                setProgramUniform1f("blindness", blindness);
                setProgramUniform1f("screenBrightness", mc.gameSettings.gammaSetting);
                setProgramUniform1i("hideGUI", mc.gameSettings.hideGUI ? 1 : 0);
                setProgramUniform1f("centerDepthSmooth", centerDepthSmooth);
                setProgramUniform2i("atlasSize", atlasSizeX, atlasSizeY);
                checkGLError("useProgram ", programNames[paramInt]);
            }
        }
    }

    public static void setProgramUniform1i(String paramString, int paramInt) {
        int i = programsID[activeProgram];
        if (i != 0) {
            int j = ARBShaderObjects.glGetUniformLocationARB(i, paramString);
            ARBShaderObjects.glUniform1iARB(j, paramInt);
            checkGLError(programNames[activeProgram], paramString);
        }
    }

    public static void setProgramUniform2i(String paramString, int paramInt1, int paramInt2) {
        int i = programsID[activeProgram];
        if (i != 0) {
            int j = ARBShaderObjects.glGetUniformLocationARB(i, paramString);
            ARBShaderObjects.glUniform2iARB(j, paramInt1, paramInt2);
            checkGLError(programNames[activeProgram], paramString);
        }
    }

    public static void setProgramUniform1f(String paramString, float paramFloat) {
        int i = programsID[activeProgram];
        if (i != 0) {
            int j = ARBShaderObjects.glGetUniformLocationARB(i, paramString);
            ARBShaderObjects.glUniform1fARB(j, paramFloat);
            checkGLError(programNames[activeProgram], paramString);
        }
    }

    public static void setProgramUniform3f(String paramString, float paramFloat1, float paramFloat2, float paramFloat3) {
        int i = programsID[activeProgram];
        if (i != 0) {
            int j = ARBShaderObjects.glGetUniformLocationARB(i, paramString);
            ARBShaderObjects.glUniform3fARB(j, paramFloat1, paramFloat2, paramFloat3);
            checkGLError(programNames[activeProgram], paramString);
        }
    }

    public static void setProgramUniformMatrix4ARB(String paramString, boolean paramBoolean, FloatBuffer paramFloatBuffer) {
        int i = programsID[activeProgram];
        if ((i != 0) && (paramFloatBuffer != null)) {
            int j = ARBShaderObjects.glGetUniformLocationARB(i, paramString);
            ARBShaderObjects.glUniformMatrix4ARB(j, paramBoolean, paramFloatBuffer);
            checkGLError(programNames[activeProgram], paramString);
        }
    }

    private static int getBufferIndexFromString(String paramString) {
        return (!paramString.equals("colortex0")) && (!paramString.equals("gcolor")) ? 1 : (!paramString.equals("colortex1")) && (!paramString.equals("gdepth")) ? 2 : (!paramString.equals("colortex2")) && (!paramString.equals("gnormal")) ? 3 : (!paramString.equals("colortex3")) && (!paramString.equals("composite")) ? 4 : (!paramString.equals("colortex4")) && (!paramString.equals("gaux1")) ? 5 : (!paramString.equals("colortex5")) && (!paramString.equals("gaux2")) ? 6 : (!paramString.equals("colortex6")) && (!paramString.equals("gaux3")) ? 7 : (!paramString.equals("colortex7")) && (!paramString.equals("gaux4")) ? -1 : 0;
    }

    private static int getTextureFormatFromString(String paramString) {
        paramString = paramString.trim();
        for (int i = 0; i < formatNames.length; i++) {
            String str = formatNames[i];
            if (paramString.equals(str)) {
                return formatIds[i];
            }
        }
        return 0;
    }

    private static void setupNoiseTexture() {
        if (noiseTexture == null) {
            noiseTexture = new HFNoiseTexture(noiseTextureResolution, noiseTextureResolution);
        }
    }

    private static void loadEntityDataMap() {
        mapBlockToEntityData = new IdentityHashMap(300);
        Object localObject2;
        if (mapBlockToEntityData.isEmpty()) {
            localObject1 = Block.blockRegistry.getKeys().iterator();
            while (((Iterator) localObject1).hasNext()) {
                ResourceLocation localResourceLocation = (ResourceLocation) ((Iterator) localObject1).next();
                localObject2 = (Block) Block.blockRegistry.getObject(localResourceLocation);
                int i = Block.blockRegistry.getIDForObject(localObject2);
                mapBlockToEntityData.put(localObject2, Integer.valueOf(i));
            }
        }
        Object localObject1 = null;
        try {
            localObject1 = new BufferedReader(new InputStreamReader(shaderPack.getResourceAsStream("/mc_Entity_x.txt")));
        } catch (Exception localException1) {
        }
        if (localObject1 != null) {
            try {
                String str1;
                while ((str1 = ((BufferedReader) localObject1).readLine()) != null) {
                    localObject2 = patternLoadEntityDataMap.matcher(str1);
                    if (((Matcher) localObject2).matches()) {
                        String str2 = ((Matcher) localObject2).group(1);
                        String str3 = ((Matcher) localObject2).group(2);
                        int j = Integer.parseInt(str3);
                        Block localBlock = Block.getBlockFromName(str2);
                        if (localBlock != null) {
                            mapBlockToEntityData.put(localBlock, Integer.valueOf(j));
                        } else {
                            SMCLog.warning("Unknown block name %s", new Object[]{str2});
                        }
                    } else {
                        SMCLog.warning("unmatched %s\n", new Object[]{str1});
                    }
                }
            } catch (Exception localException3) {
                SMCLog.warning("Error parsing mc_Entity_x.txt");
            }
        }
        if (localObject1 != null) {
            try {
                ((BufferedReader) localObject1).close();
            } catch (Exception localException2) {
            }
        }
    }

    private static IntBuffer fillIntBufferZero(IntBuffer paramIntBuffer) {
        int i = paramIntBuffer.limit();
        for (int j = paramIntBuffer.position(); j < i; j++) {
            paramIntBuffer.put(j, 0);
        }
        return paramIntBuffer;
    }

    public static void uninit() {
        if (isShaderPackInitialized) {
            checkGLError("Shaders.uninit pre");
            for (int i = 0; i < 33; i++) {
                if (programsRef[i] != 0) {
                    ARBShaderObjects.glDeleteObjectARB(programsRef[i]);
                    checkGLError("del programRef");
                }
                programsRef[i] = 0;
                programsID[i] = 0;
                programsDrawBufSettings[i] = null;
                programsDrawBuffers[i] = null;
                programsCompositeMipmapSetting[i] = 0;
            }
            if (dfb != 0) {
                EXTFramebufferObject.glDeleteFramebuffersEXT(dfb);
                dfb = 0;
                checkGLError("del dfb");
            }
            if (sfb != 0) {
                EXTFramebufferObject.glDeleteFramebuffersEXT(sfb);
                sfb = 0;
                checkGLError("del sfb");
            }
            if (dfbDepthTextures != null) {
                GlStateManager.deleteTextures(dfbDepthTextures);
                fillIntBufferZero(dfbDepthTextures);
                checkGLError("del dfbDepthTextures");
            }
            if (dfbColorTextures != null) {
                GlStateManager.deleteTextures(dfbColorTextures);
                fillIntBufferZero(dfbColorTextures);
                checkGLError("del dfbTextures");
            }
            if (sfbDepthTextures != null) {
                GlStateManager.deleteTextures(sfbDepthTextures);
                fillIntBufferZero(sfbDepthTextures);
                checkGLError("del shadow depth");
            }
            if (sfbColorTextures != null) {
                GlStateManager.deleteTextures(sfbColorTextures);
                fillIntBufferZero(sfbColorTextures);
                checkGLError("del shadow color");
            }
            if (dfbDrawBuffers != null) {
                fillIntBufferZero(dfbDrawBuffers);
            }
            if (noiseTexture != null) {
                noiseTexture.destroy();
                noiseTexture = null;
            }
            SMCLog.info("Uninit");
            shadowPassInterval = 0;
            shouldSkipDefaultShadow = false;
            isShaderPackInitialized = false;
            checkGLError("Shaders.uninit");
        }
    }

    public static void scheduleResize() {
        renderDisplayHeight = 0;
    }

    public static void scheduleResizeShadow() {
        needResizeShadow = true;
    }

    private static void resize() {
        renderDisplayWidth = mc.displayWidth;
        renderDisplayHeight = mc.displayHeight;
        renderWidth = Math.round(renderDisplayWidth * configRenderResMul);
        renderHeight = Math.round(renderDisplayHeight * configRenderResMul);
        setupFrameBuffer();
    }

    private static void resizeShadow() {
        needResizeShadow = false;
        shadowMapWidth = Math.round(spShadowMapWidth * configShadowResMul);
        shadowMapHeight = Math.round(spShadowMapHeight * configShadowResMul);
        setupShadowFrameBuffer();
    }

    private static void setupFrameBuffer() {
        if (dfb != 0) {
            EXTFramebufferObject.glDeleteFramebuffersEXT(dfb);
            GlStateManager.deleteTextures(dfbDepthTextures);
            GlStateManager.deleteTextures(dfbColorTextures);
        }
        dfb = EXTFramebufferObject.glGenFramebuffersEXT();
        GL11.glGenTextures((IntBuffer) dfbDepthTextures.clear().limit(usedDepthBuffers));
        GL11.glGenTextures((IntBuffer) dfbColorTextures.clear().limit(16));
        dfbDepthTextures.position(0);
        dfbColorTextures.position(0);
        dfbColorTextures.get(dfbColorTexturesA).position(0);
        EXTFramebufferObject.glBindFramebufferEXT(36160, dfb);
        GL20.glDrawBuffers(0);
        GL11.glReadBuffer(0);
        for (int i = 0; i < usedDepthBuffers; i++) {
            GlStateManager.bindTexture(dfbDepthTextures.get(i));
            GL11.glTexParameteri(3553, 10242, 10496);
            GL11.glTexParameteri(3553, 10243, 10496);
            GL11.glTexParameteri(3553, 10241, 9728);
            GL11.glTexParameteri(3553, 10240, 9728);
            GL11.glTexParameteri(3553, 34891, 6409);
            GL11.glTexImage2D(3553, 0, 6402, renderWidth, renderHeight, 0, 6402, 5126, (FloatBuffer) null);
        }
        EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36096, 3553, dfbDepthTextures.get(0), 0);
        GL20.glDrawBuffers(dfbDrawBuffers);
        GL11.glReadBuffer(0);
        checkGLError("FT d");
        for (i = 0; i < usedColorBuffers; i++) {
            GlStateManager.bindTexture(dfbColorTexturesA[i]);
            GL11.glTexParameteri(3553, 10242, 10496);
            GL11.glTexParameteri(3553, 10243, 10496);
            GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
            GL11.glTexImage2D(3553, 0, gbuffersFormat[i], renderWidth, renderHeight, 0, 32993, 33639, (ByteBuffer) null);
            EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 0x8CE0 | i, 3553, dfbColorTexturesA[i], 0);
            checkGLError("FT c");
        }
        for (i = 0; i < usedColorBuffers; i++) {
            GlStateManager.bindTexture(dfbColorTexturesA[(0x8 | i)]);
            GL11.glTexParameteri(3553, 10242, 10496);
            GL11.glTexParameteri(3553, 10243, 10496);
            GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
            GL11.glTexImage2D(3553, 0, gbuffersFormat[i], renderWidth, renderHeight, 0, 32993, 33639, (ByteBuffer) null);
            checkGLError("FT ca");
        }
        i = EXTFramebufferObject.glCheckFramebufferStatusEXT(36160);
        if (i == 36058) {
            printChatAndLogError("[Shaders] Error: Failed framebuffer incomplete formats");
            for (int j = 0; j < usedColorBuffers; j++) {
                GlStateManager.bindTexture(dfbColorTextures.get(j));
                GL11.glTexImage2D(3553, 0, 6408, renderWidth, renderHeight, 0, 32993, 33639, (ByteBuffer) null);
                EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 0x8CE0 | j, 3553, dfbColorTextures.get(j), 0);
                checkGLError("FT c");
            }
            i = EXTFramebufferObject.glCheckFramebufferStatusEXT(36160);
            if (i == 36053) {
                SMCLog.info("complete");
            }
        }
        GlStateManager.bindTexture(0);
        if (i != 36053) {
            printChatAndLogError("[Shaders] Error: Failed creating framebuffer! (Status " + i + ")");
        } else {
            SMCLog.info("Framebuffer created.");
        }
    }

    private static void setupShadowFrameBuffer() {
        if (usedShadowDepthBuffers != 0) {
            if (sfb != 0) {
                EXTFramebufferObject.glDeleteFramebuffersEXT(sfb);
                GlStateManager.deleteTextures(sfbDepthTextures);
                GlStateManager.deleteTextures(sfbColorTextures);
            }
            sfb = EXTFramebufferObject.glGenFramebuffersEXT();
            EXTFramebufferObject.glBindFramebufferEXT(36160, sfb);
            GL11.glDrawBuffer(0);
            GL11.glReadBuffer(0);
            GL11.glGenTextures((IntBuffer) sfbDepthTextures.clear().limit(usedShadowDepthBuffers));
            GL11.glGenTextures((IntBuffer) sfbColorTextures.clear().limit(usedShadowColorBuffers));
            sfbDepthTextures.position(0);
            sfbColorTextures.position(0);
            int j;
            for (int i = 0; i < usedShadowDepthBuffers; i++) {
                GlStateManager.bindTexture(sfbDepthTextures.get(i));
                GL11.glTexParameterf(3553, 10242, 10496.0F);
                GL11.glTexParameterf(3553, 10243, 10496.0F);
                j = shadowFilterNearest[i] != 0 ? 9728 : 9729;
                GL11.glTexParameteri(3553, 10241, j);
                GL11.glTexParameteri(3553, 10240, j);
                if (shadowHardwareFilteringEnabled[i] != 0) {
                    GL11.glTexParameteri(3553, 34892, 34894);
                }
                GL11.glTexImage2D(3553, 0, 6402, shadowMapWidth, shadowMapHeight, 0, 6402, 5126, (FloatBuffer) null);
            }
            EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36096, 3553, sfbDepthTextures.get(0), 0);
            checkGLError("FT sd");
            for (i = 0; i < usedShadowColorBuffers; i++) {
                GlStateManager.bindTexture(sfbColorTextures.get(i));
                GL11.glTexParameterf(3553, 10242, 10496.0F);
                GL11.glTexParameterf(3553, 10243, 10496.0F);
                j = shadowColorFilterNearest[i] != 0 ? 9728 : 9729;
                GL11.glTexParameteri(3553, 10241, j);
                GL11.glTexParameteri(3553, 10240, j);
                GL11.glTexImage2D(3553, 0, 6408, shadowMapWidth, shadowMapHeight, 0, 32993, 33639, (ByteBuffer) null);
                EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 0x8CE0 | i, 3553, sfbColorTextures.get(i), 0);
                checkGLError("FT sc");
            }
            GlStateManager.bindTexture(0);
            if (usedShadowColorBuffers > 0) {
                GL20.glDrawBuffers(sfbDrawBuffers);
            }
            i = EXTFramebufferObject.glCheckFramebufferStatusEXT(36160);
            if (i != 36053) {
                printChatAndLogError("[Shaders] Error: Failed creating shadow framebuffer! (Status " + i + ")");
            } else {
                SMCLog.info("Shadow framebuffer created.");
            }
        }
    }

    public static void beginRender(Minecraft paramMinecraft, float paramFloat, long paramLong) {
        checkGLError("pre beginRender");
        checkWorldChanged(mc.theWorld);
        mc = paramMinecraft;
        mc.mcProfiler.startSection("init");
        entityRenderer = mc.entityRenderer;
        if (!isShaderPackInitialized) {
            try {
                init();
            } catch (IllegalStateException localIllegalStateException) {
                if (Config.normalize(localIllegalStateException.getMessage()).equals("Function is not supported")) {
                    printChatAndLogError("[Shaders] Error: " + localIllegalStateException.getMessage());
                    localIllegalStateException.printStackTrace();
                    setShaderPack(packNameNone);
                    return;
                }
            }
        }
        if ((mc.displayWidth != renderDisplayWidth) || (mc.displayHeight != renderDisplayHeight)) {
            resize();
        }
        if (needResizeShadow) {
            resizeShadow();
        }
        worldTime = mc.theWorld.getWorldTime();
        diffWorldTime = (worldTime - lastWorldTime) % 24000L;
        if (diffWorldTime < 0L) {
            diffWorldTime += 24000L;
        }
        lastWorldTime = worldTime;
        moonPhase = mc.theWorld.getMoonPhase();
        frameCounter |= 0x1;
        if (frameCounter >= 720720) {
            frameCounter = 0;
        }
        systemTime = System.currentTimeMillis();
        if (lastSystemTime == 0L) {
            lastSystemTime = systemTime;
        }
        diffSystemTime = systemTime - lastSystemTime;
        lastSystemTime = systemTime;
        frameTime = (float) diffSystemTime / 1000.0F;
        frameTimeCounter += frameTime;
        frameTimeCounter %= 3600.0F;
        rainStrength = paramMinecraft.theWorld.getRainStrength(paramFloat);
        float f1 = (float) diffSystemTime * 0.01F;
        float f2 = (float) Math.exp(Math.log(0.5D) * f1 / (wetness < rainStrength ? drynessHalfLife : wetnessHalfLife));
        wetness = wetness * f2 + rainStrength * (1.0F - f2);
        Entity localEntity = mc.getRenderViewEntity();
        if (localEntity != null) {
            isSleeping = ((localEntity instanceof EntityLivingBase)) && (((EntityLivingBase) localEntity).isPlayerSleeping());
            eyePosY = (float) localEntity.posY * paramFloat + (float) localEntity.lastTickPosY * (1.0F - paramFloat);
            eyeBrightness = localEntity.getBrightnessForRender(paramFloat);
            f2 = (float) diffSystemTime * 0.01F;
            float f3 = (float) Math.exp(Math.log(0.5D) * f2 / eyeBrightnessHalflife);
            eyeBrightnessFadeX = eyeBrightnessFadeX * f3 + (eyeBrightness >> 65535) * (1.0F - f3);
            eyeBrightnessFadeY = eyeBrightnessFadeY * f3 + (eyeBrightness & 0x10) * (1.0F - f3);
            isEyeInWater = (mc.gameSettings.thirdPersonView == 0) && (!isSleeping) && (mc.thePlayer.isInsideOfMaterial(Material.water)) ? 1 : 0;
            if (mc.thePlayer != null) {
                nightVision = 0.0F;
                if (mc.thePlayer.isPotionActive(Potion.nightVision)) {
                    nightVision = Config.getMinecraft().entityRenderer.getNightVisionBrightness(mc.thePlayer, paramFloat);
                }
                blindness = 0.0F;
                if (mc.thePlayer.isPotionActive(Potion.blindness)) {
                    int j = mc.thePlayer.getActivePotionEffect(Potion.blindness).getDuration();
                    blindness = Config.limit(j / 20.0F, 0.0F, 1.0F);
                }
            }
            Vec3 localVec3 = mc.theWorld.getSkyColor(localEntity, paramFloat);
            localVec3 = CustomColors.getWorldSkyColor(localVec3, currentWorld, localEntity, paramFloat);
            skyColorR = (float) localVec3.xCoord;
            skyColorG = (float) localVec3.yCoord;
            skyColorB = (float) localVec3.zCoord;
        }
        isRenderingWorld = true;
        isCompositeRendered = false;
        isHandRenderedMain = false;
        if (usedShadowDepthBuffers >= 1) {
            GlStateManager.setActiveTexture(33988);
            GlStateManager.bindTexture(sfbDepthTextures.get(0));
            if (usedShadowDepthBuffers >= 2) {
                GlStateManager.setActiveTexture(33989);
                GlStateManager.bindTexture(sfbDepthTextures.get(1));
            }
        }
        GlStateManager.setActiveTexture(33984);
        for (int i = 0; i < usedColorBuffers; i++) {
            GlStateManager.bindTexture(dfbColorTexturesA[i]);
            GL11.glTexParameteri(3553, 10240, 9729);
            GL11.glTexParameteri(3553, 10241, 9729);
            GlStateManager.bindTexture(dfbColorTexturesA[(0x8 | i)]);
            GL11.glTexParameteri(3553, 10240, 9729);
            GL11.glTexParameteri(3553, 10241, 9729);
        }
        GlStateManager.bindTexture(0);
        for (i = 0; (i < 4) && ((0x4 | i) < usedColorBuffers); i++) {
            GlStateManager.setActiveTexture(0x84C7 | i);
            GlStateManager.bindTexture(dfbColorTextures.get(0x4 | i));
        }
        GlStateManager.setActiveTexture(33990);
        GlStateManager.bindTexture(dfbDepthTextures.get(0));
        if (usedDepthBuffers >= 2) {
            GlStateManager.setActiveTexture(33995);
            GlStateManager.bindTexture(dfbDepthTextures.get(1));
            if (usedDepthBuffers >= 3) {
                GlStateManager.setActiveTexture(33996);
                GlStateManager.bindTexture(dfbDepthTextures.get(2));
            }
        }
        for (i = 0; i < usedShadowColorBuffers; i++) {
            GlStateManager.setActiveTexture(0x84CD | i);
            GlStateManager.bindTexture(sfbColorTextures.get(i));
        }
        if (noiseTextureEnabled) {
            GlStateManager.setActiveTexture(0x84C0 | noiseTexture.textureUnit);
            GlStateManager.bindTexture(noiseTexture.getID());
            GL11.glTexParameteri(3553, 10242, 10497);
            GL11.glTexParameteri(3553, 10243, 10497);
            GL11.glTexParameteri(3553, 10240, 9729);
            GL11.glTexParameteri(3553, 10241, 9729);
        }
        bindCustomTextures(customTexturesGbuffers);
        GlStateManager.setActiveTexture(33984);
        previousCameraPositionX = cameraPositionX;
        previousCameraPositionY = cameraPositionY;
        previousCameraPositionZ = cameraPositionZ;
        previousProjection.position(0);
        projection.position(0);
        previousProjection.put(projection);
        previousProjection.position(0);
        projection.position(0);
        previousModelView.position(0);
        modelView.position(0);
        previousModelView.put(modelView);
        previousModelView.position(0);
        modelView.position(0);
        checkGLError("beginRender");
        ShadersRender.renderShadowMap(entityRenderer, 0, paramFloat, paramLong);
        mc.mcProfiler.endSection();
        EXTFramebufferObject.glBindFramebufferEXT(36160, dfb);
        for (i = 0; i < usedColorBuffers; i++) {
            colorTexturesToggle[i] = 0;
            EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 0x8CE0 | i, 3553, dfbColorTexturesA[i], 0);
        }
        checkGLError("end beginRender");
    }

    private static void checkWorldChanged(World paramWorld) {
        if (currentWorld != paramWorld) {
            World localWorld = currentWorld;
            currentWorld = paramWorld;
            if ((localWorld != null) && (paramWorld != null)) {
                int i = localWorld.provider.getDimensionId();
                int j = paramWorld.provider.getDimensionId();
                boolean bool1 = shaderPackDimensions.contains(Integer.valueOf(i));
                boolean bool2 = shaderPackDimensions.contains(Integer.valueOf(j));
                if ((bool1) || (bool2)) {
                    uninit();
                }
            }
        }
    }

    public static void beginRenderPass(int paramInt, float paramFloat, long paramLong) {
        if (!isShadowPass) {
            EXTFramebufferObject.glBindFramebufferEXT(36160, dfb);
            GL11.glViewport(0, 0, renderWidth, renderHeight);
            activeDrawBuffers = null;
            ShadersTex.bindNSTextures(defaultTexture.getMultiTexID());
            useProgram(2);
            checkGLError("end beginRenderPass");
        }
    }

    public static void setViewport(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        GlStateManager.colorMask(true, true, true, true);
        if (isShadowPass) {
            GL11.glViewport(0, 0, shadowMapWidth, shadowMapHeight);
        } else {
            GL11.glViewport(0, 0, renderWidth, renderHeight);
            EXTFramebufferObject.glBindFramebufferEXT(36160, dfb);
            isRenderingDfb = true;
            GlStateManager.enableCull();
            GlStateManager.enableDepth();
            setDrawBuffers(drawBuffersNone);
            useProgram(2);
            checkGLError("beginRenderPass");
        }
    }

    public static int setFogMode(int paramInt) {
        fogMode = paramInt;
        return paramInt;
    }

    public static void setFogColor(float paramFloat1, float paramFloat2, float paramFloat3) {
        fogColorR = paramFloat1;
        fogColorG = paramFloat2;
        fogColorB = paramFloat3;
    }

    public static void setClearColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        GlStateManager.clearColor(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
        clearColorR = paramFloat1;
        clearColorG = paramFloat2;
        clearColorB = paramFloat3;
    }

    public static void clearRenderBuffer() {
        if (isShadowPass) {
            checkGLError("shadow clear pre");
            EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36096, 3553, sfbDepthTextures.get(0), 0);
            GL11.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
            GL20.glDrawBuffers(programsDrawBuffers[30]);
            checkFramebufferStatus("shadow clear");
            GL11.glClear(16640);
            checkGLError("shadow clear");
        } else {
            checkGLError("clear pre");
            if (gbuffersClear[0] != 0) {
                GL20.glDrawBuffers(36064);
                GL11.glClear(16384);
            }
            if (gbuffersClear[1] != 0) {
                GL20.glDrawBuffers(36065);
                GL11.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glClear(16384);
            }
            for (int i = 2; i < usedColorBuffers; i++) {
                if (gbuffersClear[i] != 0) {
                    GL20.glDrawBuffers(0x8CE0 | i);
                    GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                    GL11.glClear(16384);
                }
            }
            setDrawBuffers(dfbDrawBuffers);
            checkFramebufferStatus("clear");
            checkGLError("clear");
        }
    }

    public static void setCamera(float paramFloat) {
        Entity localEntity = mc.getRenderViewEntity();
        double d1 = localEntity.lastTickPosX + (localEntity.posX - localEntity.lastTickPosX) * paramFloat;
        double d2 = localEntity.lastTickPosY + (localEntity.posY - localEntity.lastTickPosY) * paramFloat;
        double d3 = localEntity.lastTickPosZ + (localEntity.posZ - localEntity.lastTickPosZ) * paramFloat;
        cameraPositionX = d1;
        cameraPositionY = d2;
        cameraPositionZ = d3;
        GL11.glGetFloat(2983, (FloatBuffer) projection.position(0));
        SMath.invertMat4FBFA((FloatBuffer) projectionInverse.position(0), (FloatBuffer) projection.position(0), faProjectionInverse, faProjection);
        projection.position(0);
        projectionInverse.position(0);
        GL11.glGetFloat(2982, (FloatBuffer) modelView.position(0));
        SMath.invertMat4FBFA((FloatBuffer) modelViewInverse.position(0), (FloatBuffer) modelView.position(0), faModelViewInverse, faModelView);
        modelView.position(0);
        modelViewInverse.position(0);
        checkGLError("setCamera");
    }

    public static void setCameraShadow(float paramFloat) {
        Entity localEntity = mc.getRenderViewEntity();
        double d1 = localEntity.lastTickPosX + (localEntity.posX - localEntity.lastTickPosX) * paramFloat;
        double d2 = localEntity.lastTickPosY + (localEntity.posY - localEntity.lastTickPosY) * paramFloat;
        double d3 = localEntity.lastTickPosZ + (localEntity.posZ - localEntity.lastTickPosZ) * paramFloat;
        cameraPositionX = d1;
        cameraPositionY = d2;
        cameraPositionZ = d3;
        GL11.glGetFloat(2983, (FloatBuffer) projection.position(0));
        SMath.invertMat4FBFA((FloatBuffer) projectionInverse.position(0), (FloatBuffer) projection.position(0), faProjectionInverse, faProjection);
        projection.position(0);
        projectionInverse.position(0);
        GL11.glGetFloat(2982, (FloatBuffer) modelView.position(0));
        SMath.invertMat4FBFA((FloatBuffer) modelViewInverse.position(0), (FloatBuffer) modelView.position(0), faModelViewInverse, faModelView);
        modelView.position(0);
        modelViewInverse.position(0);
        GL11.glViewport(0, 0, shadowMapWidth, shadowMapHeight);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        if (shadowMapIsOrtho) {
            GL11.glOrtho(-shadowMapHalfPlane, shadowMapHalfPlane, -shadowMapHalfPlane, shadowMapHalfPlane, 0.05000000074505806D, 256.0D);
        } else {
            GLU.gluPerspective(shadowMapFOV, shadowMapWidth / shadowMapHeight, 0.05F, 256.0F);
        }
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -100.0F);
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        celestialAngle = mc.theWorld.getCelestialAngle(paramFloat);
        sunAngle = celestialAngle < 0.75F ? celestialAngle + 0.25F : celestialAngle - 0.75F;
        float f1 = celestialAngle * -360.0F;
        float f2 = shadowAngleInterval > 0.0F ? f1 % shadowAngleInterval - shadowAngleInterval * 0.5F : 0.0F;
        if (sunAngle <= 0.5D) {
            GL11.glRotatef(f1 - f2, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(sunPathRotation, 1.0F, 0.0F, 0.0F);
            shadowAngle = sunAngle;
        } else {
            GL11.glRotatef(f1 + 180.0F - f2, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(sunPathRotation, 1.0F, 0.0F, 0.0F);
            shadowAngle = sunAngle - 0.5F;
        }
        if (shadowMapIsOrtho) {
            f3 = shadowIntervalSize;
            f4 = f3 / 2.0F;
            GL11.glTranslatef((float) d1 % f3 - f4, (float) d2 % f3 - f4, (float) d3 % f3 - f4);
        }
        float f3 = sunAngle * 6.2831855F;
        float f4 = (float) Math.cos(f3);
        float f5 = (float) Math.sin(f3);
        float f6 = sunPathRotation * 6.2831855F;
        float f7 = f4;
        float f8 = f5 * (float) Math.cos(f6);
        float f9 = f5 * (float) Math.sin(f6);
        if (sunAngle > 0.5D) {
            f7 = -f4;
            f8 = -f8;
            f9 = -f9;
        }
        shadowLightPositionVector[0] = f7;
        shadowLightPositionVector[1] = f8;
        shadowLightPositionVector[2] = f9;
        shadowLightPositionVector[3] = 0.0F;
        GL11.glGetFloat(2983, (FloatBuffer) shadowProjection.position(0));
        SMath.invertMat4FBFA((FloatBuffer) shadowProjectionInverse.position(0), (FloatBuffer) shadowProjection.position(0), faShadowProjectionInverse, faShadowProjection);
        shadowProjection.position(0);
        shadowProjectionInverse.position(0);
        GL11.glGetFloat(2982, (FloatBuffer) shadowModelView.position(0));
        SMath.invertMat4FBFA((FloatBuffer) shadowModelViewInverse.position(0), (FloatBuffer) shadowModelView.position(0), faShadowModelViewInverse, faShadowModelView);
        shadowModelView.position(0);
        shadowModelViewInverse.position(0);
        setProgramUniformMatrix4ARB("gbufferProjection", false, projection);
        setProgramUniformMatrix4ARB("gbufferProjectionInverse", false, projectionInverse);
        setProgramUniformMatrix4ARB("gbufferPreviousProjection", false, previousProjection);
        setProgramUniformMatrix4ARB("gbufferModelView", false, modelView);
        setProgramUniformMatrix4ARB("gbufferModelViewInverse", false, modelViewInverse);
        setProgramUniformMatrix4ARB("gbufferPreviousModelView", false, previousModelView);
        setProgramUniformMatrix4ARB("shadowProjection", false, shadowProjection);
        setProgramUniformMatrix4ARB("shadowProjectionInverse", false, shadowProjectionInverse);
        setProgramUniformMatrix4ARB("shadowModelView", false, shadowModelView);
        setProgramUniformMatrix4ARB("shadowModelViewInverse", false, shadowModelViewInverse);
        mc.gameSettings.thirdPersonView = 1;
        checkGLError("setCamera");
    }

    public static void preCelestialRotate() {
        GL11.glRotatef(sunPathRotation * 1.0F, 0.0F, 0.0F, 1.0F);
        checkGLError("preCelestialRotate");
    }

    public static void postCelestialRotate() {
        FloatBuffer localFloatBuffer = tempMatrixDirectBuffer;
        localFloatBuffer.clear();
        GL11.glGetFloat(2982, localFloatBuffer);
        localFloatBuffer.get(tempMat, 0, 16);
        SMath.multiplyMat4xVec4(sunPosition, tempMat, sunPosModelView);
        SMath.multiplyMat4xVec4(moonPosition, tempMat, moonPosModelView);
        System.arraycopy(shadowAngle == sunAngle ? sunPosition : moonPosition, 0, shadowLightPosition, 0, 3);
        setProgramUniform3f("sunPosition", sunPosition[0], sunPosition[1], sunPosition[2]);
        setProgramUniform3f("moonPosition", moonPosition[0], moonPosition[1], moonPosition[2]);
        setProgramUniform3f("shadowLightPosition", shadowLightPosition[0], shadowLightPosition[1], shadowLightPosition[2]);
        checkGLError("postCelestialRotate");
    }

    public static void setUpPosition() {
        FloatBuffer localFloatBuffer = tempMatrixDirectBuffer;
        localFloatBuffer.clear();
        GL11.glGetFloat(2982, localFloatBuffer);
        localFloatBuffer.get(tempMat, 0, 16);
        SMath.multiplyMat4xVec4(upPosition, tempMat, upPosModelView);
        setProgramUniform3f("upPosition", upPosition[0], upPosition[1], upPosition[2]);
    }

    public static void genCompositeMipmap() {
        if (hasGlGenMipmap) {
            for (int i = 0; i < usedColorBuffers; i++) {
                if (activeCompositeMipmapSetting >> (1 >>> i) != 0) {
                    GlStateManager.setActiveTexture(0x84C0 | colorTextureTextureImageUnit[i]);
                    GL11.glTexParameteri(3553, 10241, 9987);
                    GL30.glGenerateMipmap(3553);
                }
            }
            GlStateManager.setActiveTexture(33984);
        }
    }

    public static void drawComposite() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0F, 0.0F);
        GL11.glVertex3f(0.0F, 0.0F, 0.0F);
        GL11.glTexCoord2f(1.0F, 0.0F);
        GL11.glVertex3f(1.0F, 0.0F, 0.0F);
        GL11.glTexCoord2f(1.0F, 1.0F);
        GL11.glVertex3f(1.0F, 1.0F, 0.0F);
        GL11.glTexCoord2f(0.0F, 1.0F);
        GL11.glVertex3f(0.0F, 1.0F, 0.0F);
        GL11.glEnd();
    }

    public static void renderCompositeFinal() {
        if (!isShadowPass) {
            checkGLError("pre-renderCompositeFinal");
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glMatrixMode(5889);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0D, 1.0D, 0.0D, 1.0D, 0.0D, 1.0D);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableTexture2D();
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableDepth();
            GlStateManager.depthFunc(519);
            GlStateManager.depthMask(false);
            GlStateManager.disableLighting();
            if (usedShadowDepthBuffers >= 1) {
                GlStateManager.setActiveTexture(33988);
                GlStateManager.bindTexture(sfbDepthTextures.get(0));
                if (usedShadowDepthBuffers >= 2) {
                    GlStateManager.setActiveTexture(33989);
                    GlStateManager.bindTexture(sfbDepthTextures.get(1));
                }
            }
            for (int i = 0; i < usedColorBuffers; i++) {
                GlStateManager.setActiveTexture(0x84C0 | colorTextureTextureImageUnit[i]);
                GlStateManager.bindTexture(dfbColorTexturesA[i]);
            }
            GlStateManager.setActiveTexture(33990);
            GlStateManager.bindTexture(dfbDepthTextures.get(0));
            if (usedDepthBuffers >= 2) {
                GlStateManager.setActiveTexture(33995);
                GlStateManager.bindTexture(dfbDepthTextures.get(1));
                if (usedDepthBuffers >= 3) {
                    GlStateManager.setActiveTexture(33996);
                    GlStateManager.bindTexture(dfbDepthTextures.get(2));
                }
            }
            for (i = 0; i < usedShadowColorBuffers; i++) {
                GlStateManager.setActiveTexture(0x84CD | i);
                GlStateManager.bindTexture(sfbColorTextures.get(i));
            }
            if (noiseTextureEnabled) {
                GlStateManager.setActiveTexture(0x84C0 | noiseTexture.textureUnit);
                GlStateManager.bindTexture(noiseTexture.getID());
                GL11.glTexParameteri(3553, 10242, 10497);
                GL11.glTexParameteri(3553, 10243, 10497);
                GL11.glTexParameteri(3553, 10240, 9729);
                GL11.glTexParameteri(3553, 10241, 9729);
            }
            bindCustomTextures(customTexturesComposite);
            GlStateManager.setActiveTexture(33984);
            i = 1;
            for (int j = 0; j < usedColorBuffers; j++) {
                EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 0x8CE0 | j, 3553, dfbColorTexturesA[(0x8 | j)], 0);
            }
            EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36096, 3553, dfbDepthTextures.get(0), 0);
            GL20.glDrawBuffers(dfbDrawBuffers);
            checkGLError("pre-composite");
            for (j = 0; j < 8; j++) {
                if (programsID[(0x15 | j)] != 0) {
                    useProgram(0x15 | j);
                    checkGLError(programNames[(0x15 | j)]);
                    if (activeCompositeMipmapSetting != 0) {
                        genCompositeMipmap();
                    }
                    drawComposite();
                    for (int k = 0; k < usedColorBuffers; k++) {
                        if (programsToggleColorTextures[(0x15 | j)][k] != 0) {
                            int m = colorTexturesToggle[k];
                            int n = colorTexturesToggle[k] = 8 - m;
                            GlStateManager.setActiveTexture(0x84C0 | colorTextureTextureImageUnit[k]);
                            GlStateManager.bindTexture(dfbColorTexturesA[(n | k)]);
                            EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 0x8CE0 | k, 3553, dfbColorTexturesA[(m | k)], 0);
                        }
                    }
                    GlStateManager.setActiveTexture(33984);
                }
            }
            checkGLError("composite");
            isRenderingDfb = false;
            mc.getFramebuffer().bindFramebuffer(true);
            OpenGlHelper.glFramebufferTexture2D(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_COLOR_ATTACHMENT0, 3553, mc.getFramebuffer().framebufferTexture, 0);
            GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
            if (EntityRenderer.anaglyphEnable) {
                j = EntityRenderer.anaglyphField != 0 ? 1 : 0;
                GlStateManager.colorMask(j, j == 0, j == 0, true);
            }
            GlStateManager.depthMask(true);
            GL11.glClearColor(clearColorR, clearColorG, clearColorB, 1.0F);
            GL11.glClear(16640);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableTexture2D();
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableDepth();
            GlStateManager.depthFunc(519);
            GlStateManager.depthMask(false);
            checkGLError("pre-final");
            useProgram(29);
            checkGLError("final");
            if (activeCompositeMipmapSetting != 0) {
                genCompositeMipmap();
            }
            drawComposite();
            checkGLError("renderCompositeFinal");
            isCompositeRendered = true;
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.depthFunc(515);
            GlStateManager.depthMask(true);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
            GL11.glPopMatrix();
            useProgram(0);
        }
    }

    public static void endRender() {
        if (isShadowPass) {
            checkGLError("shadow endRender");
        } else {
            if (!isCompositeRendered) {
                renderCompositeFinal();
            }
            isRenderingWorld = false;
            GlStateManager.colorMask(true, true, true, true);
            useProgram(0);
            RenderHelper.disableStandardItemLighting();
            checkGLError("endRender end");
        }
    }

    public static void beginSky() {
        isRenderingSky = true;
        fogEnabled = true;
        setDrawBuffers(dfbDrawBuffers);
        useProgram(5);
        pushEntity(-2, 0);
    }

    public static void setSkyColor(Vec3 paramVec3) {
        skyColorR = (float) paramVec3.xCoord;
        skyColorG = (float) paramVec3.yCoord;
        skyColorB = (float) paramVec3.zCoord;
        setProgramUniform3f("skyColor", skyColorR, skyColorG, skyColorB);
    }

    public static void drawHorizon() {
        WorldRenderer localWorldRenderer = Tessellator.getInstance().getWorldRenderer();
        float f = mc.gameSettings.renderDistanceChunks * 16;
        double d1 = f * 0.9238D;
        double d2 = f * 0.3826D;
        double d3 = -d2;
        double d4 = -d1;
        double d5 = 16.0D;
        double d6 = -cameraPositionY;
        localWorldRenderer.begin(7, DefaultVertexFormats.POSITION);
        localWorldRenderer.pos(d3, d6, d4).endVertex();
        localWorldRenderer.pos(d3, d5, d4).endVertex();
        localWorldRenderer.pos(d4, d5, d3).endVertex();
        localWorldRenderer.pos(d4, d6, d3).endVertex();
        localWorldRenderer.pos(d4, d6, d3).endVertex();
        localWorldRenderer.pos(d4, d5, d3).endVertex();
        localWorldRenderer.pos(d4, d5, d2).endVertex();
        localWorldRenderer.pos(d4, d6, d2).endVertex();
        localWorldRenderer.pos(d4, d6, d2).endVertex();
        localWorldRenderer.pos(d4, d5, d2).endVertex();
        localWorldRenderer.pos(d3, d5, d2).endVertex();
        localWorldRenderer.pos(d3, d6, d2).endVertex();
        localWorldRenderer.pos(d3, d6, d2).endVertex();
        localWorldRenderer.pos(d3, d5, d2).endVertex();
        localWorldRenderer.pos(d2, d5, d1).endVertex();
        localWorldRenderer.pos(d2, d6, d1).endVertex();
        localWorldRenderer.pos(d2, d6, d1).endVertex();
        localWorldRenderer.pos(d2, d5, d1).endVertex();
        localWorldRenderer.pos(d1, d5, d2).endVertex();
        localWorldRenderer.pos(d1, d6, d2).endVertex();
        localWorldRenderer.pos(d1, d6, d2).endVertex();
        localWorldRenderer.pos(d1, d5, d2).endVertex();
        localWorldRenderer.pos(d1, d5, d3).endVertex();
        localWorldRenderer.pos(d1, d6, d3).endVertex();
        localWorldRenderer.pos(d1, d6, d3).endVertex();
        localWorldRenderer.pos(d1, d5, d3).endVertex();
        localWorldRenderer.pos(d2, d5, d4).endVertex();
        localWorldRenderer.pos(d2, d6, d4).endVertex();
        localWorldRenderer.pos(d2, d6, d4).endVertex();
        localWorldRenderer.pos(d2, d5, d4).endVertex();
        localWorldRenderer.pos(d3, d5, d4).endVertex();
        localWorldRenderer.pos(d3, d6, d4).endVertex();
        Tessellator.getInstance().draw();
    }

    public static void preSkyList() {
        setUpPosition();
        GL11.glColor3f(fogColorR, fogColorG, fogColorB);
        drawHorizon();
        GL11.glColor3f(skyColorR, skyColorG, skyColorB);
    }

    public static void endSky() {
        isRenderingSky = false;
        setDrawBuffers(dfbDrawBuffers);
        useProgram(lightmapEnabled ? 3 : 2);
        popEntity();
    }

    public static void beginUpdateChunks() {
        checkGLError("beginUpdateChunks1");
        checkFramebufferStatus("beginUpdateChunks1");
        if (!isShadowPass) {
            useProgram(7);
        }
        checkGLError("beginUpdateChunks2");
        checkFramebufferStatus("beginUpdateChunks2");
    }

    public static void endUpdateChunks() {
        checkGLError("endUpdateChunks1");
        checkFramebufferStatus("endUpdateChunks1");
        if (!isShadowPass) {
            useProgram(7);
        }
        checkGLError("endUpdateChunks2");
        checkFramebufferStatus("endUpdateChunks2");
    }

    public static boolean shouldRenderClouds(GameSettings paramGameSettings) {
        if (!shaderPackLoaded) {
            return true;
        }
        checkGLError("shouldRenderClouds");
        return paramGameSettings.clouds > 0 ? true : isShadowPass ? configCloudShadow : false;
    }

    public static void beginClouds() {
        fogEnabled = true;
        pushEntity(-3, 0);
        useProgram(6);
    }

    public static void endClouds() {
        disableFog();
        popEntity();
        useProgram(lightmapEnabled ? 3 : 2);
    }

    public static void beginEntities() {
        if (isRenderingWorld) {
            useProgram(16);
            resetDisplayListModels();
        }
    }

    public static void nextEntity(Entity paramEntity) {
        if (isRenderingWorld) {
            useProgram(16);
            setEntityId(paramEntity);
        }
    }

    public static void setEntityId(Entity paramEntity) {
        if ((isRenderingWorld) && (!isShadowPass) && (uniformEntityId.isDefined())) {
            int i = EntityList.getEntityID(paramEntity);
            uniformEntityId.setValue(i);
        }
    }

    public static void beginSpiderEyes() {
        if ((isRenderingWorld) && (programsID[18] != programsID[0])) {
            useProgram(18);
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.0F);
            GlStateManager.blendFunc(770, 771);
        }
    }

    public static void endEntities() {
        if (isRenderingWorld) {
            useProgram(lightmapEnabled ? 3 : 2);
        }
    }

    public static void setEntityColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        if ((isRenderingWorld) && (!isShadowPass)) {
            uniformEntityColor.setValue(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
        }
    }

    public static void beginLivingDamage() {
        if (isRenderingWorld) {
            ShadersTex.bindTexture(defaultTexture);
            if (!isShadowPass) {
                setDrawBuffers(drawBuffersColorAtt0);
            }
        }
    }

    public static void endLivingDamage() {
        if ((isRenderingWorld) && (!isShadowPass)) {
            setDrawBuffers(programsDrawBuffers[16]);
        }
    }

    public static void beginBlockEntities() {
        if (isRenderingWorld) {
            checkGLError("beginBlockEntities");
            useProgram(13);
        }
    }

    public static void nextBlockEntity(TileEntity paramTileEntity) {
        if (isRenderingWorld) {
            checkGLError("nextBlockEntity");
            useProgram(13);
            setBlockEntityId(paramTileEntity);
        }
    }

    public static void setBlockEntityId(TileEntity paramTileEntity) {
        if ((isRenderingWorld) && (!isShadowPass) && (uniformBlockEntityId.isDefined())) {
            Block localBlock = paramTileEntity.getBlockType();
            int i = Block.getIdFromBlock(localBlock);
            uniformBlockEntityId.setValue(i);
        }
    }

    public static void endBlockEntities() {
        if (isRenderingWorld) {
            checkGLError("endBlockEntities");
            useProgram(lightmapEnabled ? 3 : 2);
            ShadersTex.bindNSTextures(defaultTexture.getMultiTexID());
        }
    }

    public static void beginLitParticles() {
        useProgram(3);
    }

    public static void beginParticles() {
        useProgram(2);
    }

    public static void endParticles() {
        useProgram(3);
    }

    public static void readCenterDepth() {
        if ((!isShadowPass) && (centerDepthSmoothEnabled)) {
            tempDirectFloatBuffer.clear();
            GL11.glReadPixels(renderHeight, -2, 1, 1, 6402, 5126, tempDirectFloatBuffer);
            centerDepth = tempDirectFloatBuffer.get(0);
            float f1 = (float) diffSystemTime * 0.01F;
            float f2 = (float) Math.exp(Math.log(0.5D) * f1 / centerDepthSmoothHalflife);
            centerDepthSmooth = centerDepthSmooth * f2 + centerDepth * (1.0F - f2);
        }
    }

    public static void beginWeather() {
        if (!isShadowPass) {
            if (usedDepthBuffers >= 3) {
                GlStateManager.setActiveTexture(33996);
                GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, renderWidth, renderHeight);
                GlStateManager.setActiveTexture(33984);
            }
            GlStateManager.enableDepth();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.enableAlpha();
            useProgram(20);
        }
    }

    public static void endWeather() {
        GlStateManager.disableBlend();
        useProgram(3);
    }

    public static void preWater() {
        if (usedDepthBuffers >= 2) {
            GlStateManager.setActiveTexture(33995);
            checkGLError("pre copy depth");
            GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, renderWidth, renderHeight);
            checkGLError("copy depth");
            GlStateManager.setActiveTexture(33984);
        }
        ShadersTex.bindNSTextures(defaultTexture.getMultiTexID());
    }

    public static void beginWater() {
        if (isRenderingWorld) {
            if (!isShadowPass) {
                useProgram(12);
                GlStateManager.enableBlend();
                GlStateManager.depthMask(true);
            } else {
                GlStateManager.depthMask(true);
            }
        }
    }

    public static void endWater() {
        if (isRenderingWorld) {
            useProgram((!isShadowPass) || (lightmapEnabled) ? 3 : 2);
        }
    }

    public static void beginProjectRedHalo() {
        if (isRenderingWorld) {
            useProgram(1);
        }
    }

    public static void endProjectRedHalo() {
        if (isRenderingWorld) {
            useProgram(3);
        }
    }

    public static void applyHandDepth() {
        if (configHandDepthMul != 1.0D) {
            GL11.glScaled(1.0D, 1.0D, configHandDepthMul);
        }
    }

    public static void beginHand() {
        GL11.glMatrixMode(5888);
        GL11.glPushMatrix();
        GL11.glMatrixMode(5889);
        GL11.glPushMatrix();
        GL11.glMatrixMode(5888);
        useProgram(19);
        checkGLError("beginHand");
        checkFramebufferStatus("beginHand");
    }

    public static void endHand() {
        checkGLError("pre endHand");
        checkFramebufferStatus("pre endHand");
        GL11.glMatrixMode(5889);
        GL11.glPopMatrix();
        GL11.glMatrixMode(5888);
        GL11.glPopMatrix();
        GlStateManager.blendFunc(770, 771);
        checkGLError("endHand");
    }

    public static void beginFPOverlay() {
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
    }

    public static void endFPOverlay() {
    }

    public static void glEnableWrapper(int paramInt) {
        GL11.glEnable(paramInt);
        if (paramInt == 3553) {
            enableTexture2D();
        } else if (paramInt == 2912) {
            enableFog();
        }
    }

    public static void glDisableWrapper(int paramInt) {
        GL11.glDisable(paramInt);
        if (paramInt == 3553) {
            disableTexture2D();
        } else if (paramInt == 2912) {
            disableFog();
        }
    }

    public static void sglEnableT2D(int paramInt) {
        GL11.glEnable(paramInt);
        enableTexture2D();
    }

    public static void sglDisableT2D(int paramInt) {
        GL11.glDisable(paramInt);
        disableTexture2D();
    }

    public static void sglEnableFog(int paramInt) {
        GL11.glEnable(paramInt);
        enableFog();
    }

    public static void sglDisableFog(int paramInt) {
        GL11.glDisable(paramInt);
        disableFog();
    }

    public static void enableTexture2D() {
        if (isRenderingSky) {
            useProgram(5);
        } else if (activeProgram == 1) {
            useProgram(lightmapEnabled ? 3 : 2);
        }
    }

    public static void disableTexture2D() {
        if (isRenderingSky) {
            useProgram(4);
        } else if ((activeProgram == 2) || (activeProgram == 3)) {
            useProgram(1);
        }
    }

    public static void beginLeash() {
        useProgram(1);
    }

    public static void endLeash() {
        useProgram(16);
    }

    public static void enableFog() {
        fogEnabled = true;
        setProgramUniform1i("fogMode", fogMode);
    }

    public static void disableFog() {
        fogEnabled = false;
        setProgramUniform1i("fogMode", 0);
    }

    public static void setFog(int paramInt) {
        GlStateManager.setFog(paramInt);
        paramInt = paramInt;
        if (fogEnabled) {
            setProgramUniform1i("fogMode", paramInt);
        }
    }

    public static void sglFogi(int paramInt1, int paramInt2) {
        GL11.glFogi(paramInt1, paramInt2);
        if (paramInt1 == 2917) {
            fogMode = paramInt2;
            if (fogEnabled) {
                setProgramUniform1i("fogMode", fogMode);
            }
        }
    }

    public static void enableLightmap() {
        lightmapEnabled = true;
        if (activeProgram == 2) {
            useProgram(3);
        }
    }

    public static void disableLightmap() {
        lightmapEnabled = false;
        if (activeProgram == 3) {
            useProgram(2);
        }
    }

    public static int getEntityData() {
        return entityData[(entityDataIndex * 2)];
    }

    public static int getEntityData2() {
        return entityData[(entityDataIndex * 2 | 0x1)];
    }

    public static int setEntityData1(int paramInt) {
        entityData[(entityDataIndex * 2)] = (entityData[(entityDataIndex * 2)] >> 65535 ^ paramInt >>> 16);
        return paramInt;
    }

    public static int setEntityData2(int paramInt) {
        entityData[(entityDataIndex * 2 | 0x1)] = (entityData[(entityDataIndex * 2 | 0x1)] >> -65536 ^ paramInt >> 65535);
        return paramInt;
    }

    public static void pushEntity(int paramInt1, int paramInt2) {
        entityDataIndex |= 0x1;
        entityData[(entityDataIndex * 2)] = (paramInt1 >> 65535 ^ paramInt2 >>> 16);
        entityData[(entityDataIndex * 2 | 0x1)] = 0;
    }

    public static void pushEntity(int paramInt) {
        entityDataIndex |= 0x1;
        entityData[(entityDataIndex * 2)] = (paramInt >> 65535);
        entityData[(entityDataIndex * 2 | 0x1)] = 0;
    }

    public static void pushEntity(Block paramBlock) {
        entityDataIndex |= 0x1;
        entityData[(entityDataIndex * 2)] = (Block.blockRegistry.getIDForObject(paramBlock) >> 65535 ^ paramBlock.getRenderType() >>> 16);
        entityData[(entityDataIndex * 2 | 0x1)] = 0;
    }

    public static void popEntity() {
        entityData[(entityDataIndex * 2)] = 0;
        entityData[(entityDataIndex * 2 | 0x1)] = 0;
        entityDataIndex -= 1;
    }

    public static void mcProfilerEndSection() {
        mc.mcProfiler.endSection();
    }

    public static String getShaderPackName() {
        return (shaderPack instanceof ShaderPackNone) ? null : shaderPack == null ? null : shaderPack.getName();
    }

    public static InputStream getShaderPackResourceStream(String paramString) {
        return shaderPack == null ? null : shaderPack.getResourceAsStream(paramString);
    }

    public static void nextAntialiasingLevel() {
        configAntialiasingLevel |= 0x2;
        configAntialiasingLevel = -2 * 2;
        if (configAntialiasingLevel > 4) {
            configAntialiasingLevel = 0;
        }
        configAntialiasingLevel = Config.limit(configAntialiasingLevel, 0, 4);
    }

    public static void checkShadersModInstalled() {
        try {
            Class localClass = Class.forName("shadersmod.transform.SMCClassTransformer");
        } catch (Throwable localThrowable) {
            return;
        }
        throw new RuntimeException("Shaders Mod detected. Please remove it, OptiFine has built-in support for shaders.");
    }

    public static void resourcesReloaded() {
    }

    private static void loadShaderPackResources() {
        shaderPackResources = new HashMap();
        if (shaderPackLoaded) {
            ArrayList localArrayList = new ArrayList();
            String str1 = "/shaders/lang/";
            String str2 = "en_US";
            String str3 = ".lang";
            localArrayList.add(str1 + str2 + str3);
            if (!Config.getGameSettings().language.equals(str2)) {
                localArrayList.add(str1 + Config.getGameSettings().language + str3);
            }
            try {
                Iterator localIterator1 = localArrayList.iterator();
                while (localIterator1.hasNext()) {
                    String str4 = (String) localIterator1.next();
                    InputStream localInputStream = shaderPack.getResourceAsStream(str4);
                    if (localInputStream != null) {
                        Properties localProperties = new Properties();
                        Lang.loadLocaleData(localInputStream, localProperties);
                        localInputStream.close();
                        Iterator localIterator2 = localProperties.keySet().iterator();
                        while (localIterator2.hasNext()) {
                            Object localObject = localIterator2.next();
                            String str5 = localProperties.getProperty((String) localObject);
                            shaderPackResources.put((String) localObject, str5);
                        }
                    }
                }
            } catch (IOException localIOException) {
                localIOException.printStackTrace();
            }
        }
    }

    public static String translate(String paramString1, String paramString2) {
        String str = (String) shaderPackResources.get(paramString1);
        return str == null ? paramString2 : str;
    }

    public static boolean isProgramPath(String paramString) {
        if (paramString == null) {
            return false;
        }
        if (paramString.length() <= 0) {
            return false;
        }
        int i = paramString.lastIndexOf("/");
        if (i >= 0) {
            paramString = paramString.substring(i | 0x1);
        }
        return Arrays.asList(programNames).contains(paramString);
    }

    public static void setItemToRenderMain(ItemStack paramItemStack) {
        itemToRenderMainTranslucent = isTranslucentBlock(paramItemStack);
    }

    public static boolean isItemToRenderMainTranslucent() {
        return itemToRenderMainTranslucent;
    }

    private static boolean isTranslucentBlock(ItemStack paramItemStack) {
        if (paramItemStack == null) {
            return false;
        }
        Item localItem = paramItemStack.getItem();
        if (localItem == null) {
            return false;
        }
        if (!(localItem instanceof ItemBlock)) {
            return false;
        }
        ItemBlock localItemBlock = (ItemBlock) localItem;
        Block localBlock = localItemBlock.getBlock();
        if (localBlock == null) {
            return false;
        }
        EnumWorldBlockLayer localEnumWorldBlockLayer = localBlock.getBlockLayer();
        return localEnumWorldBlockLayer == EnumWorldBlockLayer.TRANSLUCENT;
    }

    public static boolean isRenderBothHands() {
        return false;
    }

    public static boolean isHandRenderedMain() {
        return isHandRenderedMain;
    }

    public static void setHandRenderedMain(boolean paramBoolean) {
        paramBoolean = paramBoolean;
    }

    public static float getShadowRenderDistance() {
        return shadowDistanceRenderMul < 0.0F ? -1.0F : shadowMapHalfPlane * shadowDistanceRenderMul;
    }
}




