// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import net.minecraft.item.ItemBlock;
import optifine.Lang;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.EntityList;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL30;
import net.minecraft.util.Vec3;
import net.minecraft.entity.Entity;
import optifine.CustomColors;
import net.minecraft.potion.Potion;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import java.util.IdentityHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import java.util.regex.Matcher;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.ARBShaderObjects;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.util.EnumWorldBlockLayer;
import org.lwjgl.opengl.GLContext;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.EXTFramebufferObject;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Collection;
import java.util.Arrays;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import optifine.StrUtils;
import java.io.InputStream;
import java.io.IOException;
import optifine.Reflector;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import java.io.Writer;
import java.io.FileWriter;
import optifine.Config;
import java.io.Reader;
import java.io.FileReader;
import optifine.PropertiesOrdered;
import shadersmod.common.SMCLog;
import org.lwjgl.BufferUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import net.minecraft.block.Block;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.util.List;
import net.minecraft.world.World;
import java.util.Map;
import java.io.File;
import net.minecraft.client.renderer.texture.ITextureObject;
import java.util.Properties;
import java.nio.IntBuffer;
import org.lwjgl.opengl.ContextCapabilities;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.Minecraft;

public class Shaders
{
    static Minecraft mc;
    static EntityRenderer entityRenderer;
    public static boolean isInitializedOnce;
    public static boolean isShaderPackInitialized;
    public static ContextCapabilities capabilities;
    public static String glVersionString;
    public static String glVendorString;
    public static String glRendererString;
    public static boolean hasGlGenMipmap;
    public static boolean hasForge;
    public static int numberResetDisplayList;
    static boolean needResetModels;
    private static int renderDisplayWidth;
    private static int renderDisplayHeight;
    public static int renderWidth;
    public static int renderHeight;
    public static boolean isRenderingWorld;
    public static boolean isRenderingSky;
    public static boolean isCompositeRendered;
    public static boolean isRenderingDfb;
    public static boolean isShadowPass;
    public static boolean isSleeping;
    private static boolean isHandRenderedMain;
    public static boolean renderItemKeepDepthMask;
    public static boolean itemToRenderMainTranslucent;
    static float[] sunPosition;
    static float[] moonPosition;
    static float[] shadowLightPosition;
    static float[] upPosition;
    static float[] shadowLightPositionVector;
    static float[] upPosModelView;
    static float[] sunPosModelView;
    static float[] moonPosModelView;
    private static float[] tempMat;
    static float clearColorR;
    static float clearColorG;
    static float clearColorB;
    static float skyColorR;
    static float skyColorG;
    static float skyColorB;
    static long worldTime;
    static long lastWorldTime;
    static long diffWorldTime;
    static float celestialAngle;
    static float sunAngle;
    static float shadowAngle;
    static int moonPhase;
    static long systemTime;
    static long lastSystemTime;
    static long diffSystemTime;
    static int frameCounter;
    static float frameTime;
    static float frameTimeCounter;
    static int systemTimeInt32;
    static float rainStrength;
    static float wetness;
    public static float wetnessHalfLife;
    public static float drynessHalfLife;
    public static float eyeBrightnessHalflife;
    static boolean usewetness;
    static int isEyeInWater;
    static int eyeBrightness;
    static float eyeBrightnessFadeX;
    static float eyeBrightnessFadeY;
    static float eyePosY;
    static float centerDepth;
    static float centerDepthSmooth;
    static float centerDepthSmoothHalflife;
    static boolean centerDepthSmoothEnabled;
    static int superSamplingLevel;
    static float nightVision;
    static float blindness;
    static boolean updateChunksErrorRecorded;
    static boolean lightmapEnabled;
    static boolean fogEnabled;
    public static int entityAttrib;
    public static int midTexCoordAttrib;
    public static int tangentAttrib;
    public static boolean useEntityAttrib;
    public static boolean useMidTexCoordAttrib;
    public static boolean useMultiTexCoord3Attrib;
    public static boolean useTangentAttrib;
    public static boolean progUseEntityAttrib;
    public static boolean progUseMidTexCoordAttrib;
    public static boolean progUseTangentAttrib;
    public static int atlasSizeX;
    public static int atlasSizeY;
    public static ShaderUniformFloat4 uniformEntityColor;
    public static ShaderUniformInt uniformEntityId;
    public static ShaderUniformInt uniformBlockEntityId;
    static double previousCameraPositionX;
    static double previousCameraPositionY;
    static double previousCameraPositionZ;
    static double cameraPositionX;
    static double cameraPositionY;
    static double cameraPositionZ;
    static int shadowPassInterval;
    public static boolean needResizeShadow;
    static int shadowMapWidth;
    static int shadowMapHeight;
    static int spShadowMapWidth;
    static int spShadowMapHeight;
    static float shadowMapFOV;
    static float shadowMapHalfPlane;
    static boolean shadowMapIsOrtho;
    static float shadowDistanceRenderMul;
    static int shadowPassCounter;
    static int preShadowPassThirdPersonView;
    public static boolean shouldSkipDefaultShadow;
    static boolean waterShadowEnabled;
    static final int MaxDrawBuffers = 8;
    static final int MaxColorBuffers = 8;
    static final int MaxDepthBuffers = 3;
    static final int MaxShadowColorBuffers = 8;
    static final int MaxShadowDepthBuffers = 2;
    static int usedColorBuffers;
    static int usedDepthBuffers;
    static int usedShadowColorBuffers;
    static int usedShadowDepthBuffers;
    static int usedColorAttachs;
    static int usedDrawBuffers;
    static int dfb;
    static int sfb;
    private static int[] gbuffersFormat;
    private static boolean[] gbuffersClear;
    public static int activeProgram;
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
    private static final String[] programNames;
    private static final int[] programBackups;
    static int[] programsID;
    private static int[] programsRef;
    private static int programIDCopyDepth;
    private static String[] programsDrawBufSettings;
    private static String newDrawBufSetting;
    static IntBuffer[] programsDrawBuffers;
    static IntBuffer activeDrawBuffers;
    private static String[] programsColorAtmSettings;
    private static String newColorAtmSetting;
    private static String activeColorAtmSettings;
    private static int[] programsCompositeMipmapSetting;
    private static int newCompositeMipmapSetting;
    private static int activeCompositeMipmapSetting;
    public static Properties loadedShaders;
    public static Properties shadersConfig;
    public static ITextureObject defaultTexture;
    public static boolean normalMapEnabled;
    public static boolean[] shadowHardwareFilteringEnabled;
    public static boolean[] shadowMipmapEnabled;
    public static boolean[] shadowFilterNearest;
    public static boolean[] shadowColorMipmapEnabled;
    public static boolean[] shadowColorFilterNearest;
    public static boolean configTweakBlockDamage;
    public static boolean configCloudShadow;
    public static float configHandDepthMul;
    public static float configRenderResMul;
    public static float configShadowResMul;
    public static int configTexMinFilB;
    public static int configTexMinFilN;
    public static int configTexMinFilS;
    public static int configTexMagFilB;
    public static int configTexMagFilN;
    public static int configTexMagFilS;
    public static boolean configShadowClipFrustrum;
    public static boolean configNormalMap;
    public static boolean configSpecularMap;
    public static PropertyDefaultTrueFalse configOldLighting;
    public static PropertyDefaultTrueFalse configOldHandLight;
    public static int configAntialiasingLevel;
    public static final int texMinFilRange = 3;
    public static final int texMagFilRange = 2;
    public static final String[] texMinFilDesc;
    public static final String[] texMagFilDesc;
    public static final int[] texMinFilValue;
    public static final int[] texMagFilValue;
    static IShaderPack shaderPack;
    public static boolean shaderPackLoaded;
    static File currentshader;
    static String currentshadername;
    public static String packNameNone;
    static String packNameDefault;
    static String shaderpacksdirname;
    static String optionsfilename;
    static File shadersdir;
    static File shaderpacksdir;
    static File configFile;
    static ShaderOption[] shaderPackOptions;
    static ShaderProfile[] shaderPackProfiles;
    static Map<String, ShaderOption[]> shaderPackGuiScreens;
    public static PropertyDefaultFastFancyOff shaderPackClouds;
    public static PropertyDefaultTrueFalse shaderPackOldLighting;
    public static PropertyDefaultTrueFalse shaderPackOldHandLight;
    public static PropertyDefaultTrueFalse shaderPackDynamicHandLight;
    public static PropertyDefaultTrueFalse shaderPackShadowTranslucent;
    public static PropertyDefaultTrueFalse shaderPackUnderwaterOverlay;
    public static PropertyDefaultTrueFalse shaderPackSun;
    public static PropertyDefaultTrueFalse shaderPackMoon;
    public static PropertyDefaultTrueFalse shaderPackVignette;
    public static PropertyDefaultTrueFalse shaderPackBackFaceSolid;
    public static PropertyDefaultTrueFalse shaderPackBackFaceCutout;
    public static PropertyDefaultTrueFalse shaderPackBackFaceCutoutMipped;
    public static PropertyDefaultTrueFalse shaderPackBackFaceTranslucent;
    private static Map<String, String> shaderPackResources;
    private static World currentWorld;
    private static List<Integer> shaderPackDimensions;
    private static CustomTexture[] customTexturesGbuffers;
    private static CustomTexture[] customTexturesComposite;
    private static final int STAGE_GBUFFERS = 0;
    private static final int STAGE_COMPOSITE = 1;
    private static final String[] STAGE_NAMES;
    public static final boolean enableShadersOption = true;
    private static final boolean enableShadersDebug = true;
    private static final boolean saveFinalShaders;
    public static float blockLightLevel05;
    public static float blockLightLevel06;
    public static float blockLightLevel08;
    public static float aoLevel;
    public static float sunPathRotation;
    public static float shadowAngleInterval;
    public static int fogMode;
    public static float fogColorR;
    public static float fogColorG;
    public static float fogColorB;
    public static float shadowIntervalSize;
    public static int terrainIconSize;
    public static int[] terrainTextureSize;
    private static HFNoiseTexture noiseTexture;
    private static boolean noiseTextureEnabled;
    private static int noiseTextureResolution;
    static final int[] dfbColorTexturesA;
    static final int[] colorTexturesToggle;
    static final int[] colorTextureTextureImageUnit;
    static final boolean[][] programsToggleColorTextures;
    private static final int bigBufferSize = 2196;
    private static final ByteBuffer bigBuffer;
    static final float[] faProjection;
    static final float[] faProjectionInverse;
    static final float[] faModelView;
    static final float[] faModelViewInverse;
    static final float[] faShadowProjection;
    static final float[] faShadowProjectionInverse;
    static final float[] faShadowModelView;
    static final float[] faShadowModelViewInverse;
    static final FloatBuffer projection;
    static final FloatBuffer projectionInverse;
    static final FloatBuffer modelView;
    static final FloatBuffer modelViewInverse;
    static final FloatBuffer shadowProjection;
    static final FloatBuffer shadowProjectionInverse;
    static final FloatBuffer shadowModelView;
    static final FloatBuffer shadowModelViewInverse;
    static final FloatBuffer previousProjection;
    static final FloatBuffer previousModelView;
    static final FloatBuffer tempMatrixDirectBuffer;
    static final FloatBuffer tempDirectFloatBuffer;
    static final IntBuffer dfbColorTextures;
    static final IntBuffer dfbDepthTextures;
    static final IntBuffer sfbColorTextures;
    static final IntBuffer sfbDepthTextures;
    static final IntBuffer dfbDrawBuffers;
    static final IntBuffer sfbDrawBuffers;
    static final IntBuffer drawBuffersNone;
    static final IntBuffer drawBuffersAll;
    static final IntBuffer drawBuffersClear0;
    static final IntBuffer drawBuffersClear1;
    static final IntBuffer drawBuffersClearColor;
    static final IntBuffer drawBuffersColorAtt0;
    static final IntBuffer[] drawBuffersBuffer;
    static Map<Block, Integer> mapBlockToEntityData;
    private static final Pattern gbufferFormatPattern;
    private static final Pattern gbufferClearPattern;
    private static final Pattern gbufferMipmapEnabledPattern;
    private static final String[] formatNames;
    private static final int[] formatIds;
    private static final Pattern patternLoadEntityDataMap;
    public static int[] entityData;
    public static int entityDataIndex;
    
    static {
        Shaders.isInitializedOnce = false;
        Shaders.isShaderPackInitialized = false;
        Shaders.hasGlGenMipmap = false;
        Shaders.hasForge = false;
        Shaders.numberResetDisplayList = 0;
        Shaders.needResetModels = false;
        Shaders.renderDisplayWidth = 0;
        Shaders.renderDisplayHeight = 0;
        Shaders.renderWidth = 0;
        Shaders.renderHeight = 0;
        Shaders.isRenderingWorld = false;
        Shaders.isRenderingSky = false;
        Shaders.isCompositeRendered = false;
        Shaders.isRenderingDfb = false;
        Shaders.isShadowPass = false;
        Shaders.renderItemKeepDepthMask = false;
        Shaders.itemToRenderMainTranslucent = false;
        Shaders.sunPosition = new float[4];
        Shaders.moonPosition = new float[4];
        Shaders.shadowLightPosition = new float[4];
        Shaders.upPosition = new float[4];
        Shaders.shadowLightPositionVector = new float[4];
        Shaders.upPosModelView = new float[] { 0.0f, 100.0f, 0.0f, 0.0f };
        Shaders.sunPosModelView = new float[] { 0.0f, 100.0f, 0.0f, 0.0f };
        Shaders.moonPosModelView = new float[] { 0.0f, -100.0f, 0.0f, 0.0f };
        Shaders.tempMat = new float[16];
        Shaders.worldTime = 0L;
        Shaders.lastWorldTime = 0L;
        Shaders.diffWorldTime = 0L;
        Shaders.celestialAngle = 0.0f;
        Shaders.sunAngle = 0.0f;
        Shaders.shadowAngle = 0.0f;
        Shaders.moonPhase = 0;
        Shaders.systemTime = 0L;
        Shaders.lastSystemTime = 0L;
        Shaders.diffSystemTime = 0L;
        Shaders.frameCounter = 0;
        Shaders.frameTime = 0.0f;
        Shaders.frameTimeCounter = 0.0f;
        Shaders.systemTimeInt32 = 0;
        Shaders.rainStrength = 0.0f;
        Shaders.wetness = 0.0f;
        Shaders.wetnessHalfLife = 600.0f;
        Shaders.drynessHalfLife = 200.0f;
        Shaders.eyeBrightnessHalflife = 10.0f;
        Shaders.usewetness = false;
        Shaders.isEyeInWater = 0;
        Shaders.eyeBrightness = 0;
        Shaders.eyeBrightnessFadeX = 0.0f;
        Shaders.eyeBrightnessFadeY = 0.0f;
        Shaders.eyePosY = 0.0f;
        Shaders.centerDepth = 0.0f;
        Shaders.centerDepthSmooth = 0.0f;
        Shaders.centerDepthSmoothHalflife = 1.0f;
        Shaders.centerDepthSmoothEnabled = false;
        Shaders.superSamplingLevel = 1;
        Shaders.nightVision = 0.0f;
        Shaders.blindness = 0.0f;
        Shaders.updateChunksErrorRecorded = false;
        Shaders.lightmapEnabled = false;
        Shaders.fogEnabled = true;
        Shaders.entityAttrib = 10;
        Shaders.midTexCoordAttrib = 11;
        Shaders.tangentAttrib = 12;
        Shaders.useEntityAttrib = false;
        Shaders.useMidTexCoordAttrib = false;
        Shaders.useMultiTexCoord3Attrib = false;
        Shaders.useTangentAttrib = false;
        Shaders.progUseEntityAttrib = false;
        Shaders.progUseMidTexCoordAttrib = false;
        Shaders.progUseTangentAttrib = false;
        Shaders.atlasSizeX = 0;
        Shaders.atlasSizeY = 0;
        Shaders.uniformEntityColor = new ShaderUniformFloat4("entityColor");
        Shaders.uniformEntityId = new ShaderUniformInt("entityId");
        Shaders.uniformBlockEntityId = new ShaderUniformInt("blockEntityId");
        Shaders.shadowPassInterval = 0;
        Shaders.needResizeShadow = false;
        Shaders.shadowMapWidth = 1024;
        Shaders.shadowMapHeight = 1024;
        Shaders.spShadowMapWidth = 1024;
        Shaders.spShadowMapHeight = 1024;
        Shaders.shadowMapFOV = 90.0f;
        Shaders.shadowMapHalfPlane = 160.0f;
        Shaders.shadowMapIsOrtho = true;
        Shaders.shadowDistanceRenderMul = -1.0f;
        Shaders.shadowPassCounter = 0;
        Shaders.shouldSkipDefaultShadow = false;
        Shaders.waterShadowEnabled = false;
        Shaders.usedColorBuffers = 0;
        Shaders.usedDepthBuffers = 0;
        Shaders.usedShadowColorBuffers = 0;
        Shaders.usedShadowDepthBuffers = 0;
        Shaders.usedColorAttachs = 0;
        Shaders.usedDrawBuffers = 0;
        Shaders.dfb = 0;
        Shaders.sfb = 0;
        Shaders.gbuffersFormat = new int[8];
        Shaders.gbuffersClear = new boolean[8];
        Shaders.activeProgram = 0;
        programNames = new String[] { "", "gbuffers_basic", "gbuffers_textured", "gbuffers_textured_lit", "gbuffers_skybasic", "gbuffers_skytextured", "gbuffers_clouds", "gbuffers_terrain", "gbuffers_terrain_solid", "gbuffers_terrain_cutout_mip", "gbuffers_terrain_cutout", "gbuffers_damagedblock", "gbuffers_water", "gbuffers_block", "gbuffers_beaconbeam", "gbuffers_item", "gbuffers_entities", "gbuffers_armor_glint", "gbuffers_spidereyes", "gbuffers_hand", "gbuffers_weather", "composite", "composite1", "composite2", "composite3", "composite4", "composite5", "composite6", "composite7", "final", "shadow", "shadow_solid", "shadow_cutout" };
        programBackups = new int[] { 0, 0, 1, 2, 1, 2, 2, 3, 7, 7, 7, 7, 7, 7, 2, 3, 3, 2, 2, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 30 };
        Shaders.programsID = new int[33];
        Shaders.programsRef = new int[33];
        Shaders.programIDCopyDepth = 0;
        Shaders.programsDrawBufSettings = new String[33];
        Shaders.newDrawBufSetting = null;
        Shaders.programsDrawBuffers = new IntBuffer[33];
        Shaders.activeDrawBuffers = null;
        Shaders.programsColorAtmSettings = new String[33];
        Shaders.newColorAtmSetting = null;
        Shaders.activeColorAtmSettings = null;
        Shaders.programsCompositeMipmapSetting = new int[33];
        Shaders.newCompositeMipmapSetting = 0;
        Shaders.activeCompositeMipmapSetting = 0;
        Shaders.loadedShaders = null;
        Shaders.shadersConfig = null;
        Shaders.defaultTexture = null;
        Shaders.normalMapEnabled = false;
        Shaders.shadowHardwareFilteringEnabled = new boolean[2];
        Shaders.shadowMipmapEnabled = new boolean[2];
        Shaders.shadowFilterNearest = new boolean[2];
        Shaders.shadowColorMipmapEnabled = new boolean[8];
        Shaders.shadowColorFilterNearest = new boolean[8];
        Shaders.configTweakBlockDamage = true;
        Shaders.configCloudShadow = true;
        Shaders.configHandDepthMul = 0.125f;
        Shaders.configRenderResMul = 1.0f;
        Shaders.configShadowResMul = 1.0f;
        Shaders.configTexMinFilB = 0;
        Shaders.configTexMinFilN = 0;
        Shaders.configTexMinFilS = 0;
        Shaders.configTexMagFilB = 0;
        Shaders.configTexMagFilN = 0;
        Shaders.configTexMagFilS = 0;
        Shaders.configShadowClipFrustrum = true;
        Shaders.configNormalMap = true;
        Shaders.configSpecularMap = true;
        Shaders.configOldLighting = new PropertyDefaultTrueFalse("oldLighting", "Classic Lighting", 0);
        Shaders.configOldHandLight = new PropertyDefaultTrueFalse("oldHandLight", "Old Hand Light", 0);
        Shaders.configAntialiasingLevel = 0;
        texMinFilDesc = new String[] { "Nearest", "Nearest-Nearest", "Nearest-Linear" };
        texMagFilDesc = new String[] { "Nearest", "Linear" };
        texMinFilValue = new int[] { 9728, 9984, 9986 };
        texMagFilValue = new int[] { 9728, 9729 };
        Shaders.shaderPack = null;
        Shaders.shaderPackLoaded = false;
        Shaders.packNameNone = "OFF";
        Shaders.packNameDefault = "(internal)";
        Shaders.shaderpacksdirname = "shaderpacks";
        Shaders.optionsfilename = "optionsshaders.txt";
        Shaders.shaderPackOptions = null;
        Shaders.shaderPackProfiles = null;
        Shaders.shaderPackGuiScreens = null;
        Shaders.shaderPackClouds = new PropertyDefaultFastFancyOff("clouds", "Clouds", 0);
        Shaders.shaderPackOldLighting = new PropertyDefaultTrueFalse("oldLighting", "Classic Lighting", 0);
        Shaders.shaderPackOldHandLight = new PropertyDefaultTrueFalse("oldHandLight", "Old Hand Light", 0);
        Shaders.shaderPackDynamicHandLight = new PropertyDefaultTrueFalse("dynamicHandLight", "Dynamic Hand Light", 0);
        Shaders.shaderPackShadowTranslucent = new PropertyDefaultTrueFalse("shadowTranslucent", "Shadow Translucent", 0);
        Shaders.shaderPackUnderwaterOverlay = new PropertyDefaultTrueFalse("underwaterOverlay", "Underwater Overlay", 0);
        Shaders.shaderPackSun = new PropertyDefaultTrueFalse("sun", "Sun", 0);
        Shaders.shaderPackMoon = new PropertyDefaultTrueFalse("moon", "Moon", 0);
        Shaders.shaderPackVignette = new PropertyDefaultTrueFalse("vignette", "Vignette", 0);
        Shaders.shaderPackBackFaceSolid = new PropertyDefaultTrueFalse("backFace.solid", "Back-face Solid", 0);
        Shaders.shaderPackBackFaceCutout = new PropertyDefaultTrueFalse("backFace.cutout", "Back-face Cutout", 0);
        Shaders.shaderPackBackFaceCutoutMipped = new PropertyDefaultTrueFalse("backFace.cutoutMipped", "Back-face Cutout Mipped", 0);
        Shaders.shaderPackBackFaceTranslucent = new PropertyDefaultTrueFalse("backFace.translucent", "Back-face Translucent", 0);
        Shaders.shaderPackResources = new HashMap<String, String>();
        Shaders.currentWorld = null;
        Shaders.shaderPackDimensions = new ArrayList<Integer>();
        Shaders.customTexturesGbuffers = null;
        Shaders.customTexturesComposite = null;
        STAGE_NAMES = new String[] { "gbuffers", "composite" };
        saveFinalShaders = System.getProperty("shaders.debug.save", "false").equals("true");
        Shaders.blockLightLevel05 = 0.5f;
        Shaders.blockLightLevel06 = 0.6f;
        Shaders.blockLightLevel08 = 0.8f;
        Shaders.aoLevel = -1.0f;
        Shaders.sunPathRotation = 0.0f;
        Shaders.shadowAngleInterval = 0.0f;
        Shaders.fogMode = 0;
        Shaders.shadowIntervalSize = 2.0f;
        Shaders.terrainIconSize = 16;
        Shaders.terrainTextureSize = new int[2];
        Shaders.noiseTextureEnabled = false;
        Shaders.noiseTextureResolution = 256;
        dfbColorTexturesA = new int[16];
        colorTexturesToggle = new int[8];
        colorTextureTextureImageUnit = new int[] { 0, 1, 2, 3, 7, 8, 9, 10 };
        programsToggleColorTextures = new boolean[33][8];
        bigBuffer = (ByteBuffer)BufferUtils.createByteBuffer(2196).limit(0);
        faProjection = new float[16];
        faProjectionInverse = new float[16];
        faModelView = new float[16];
        faModelViewInverse = new float[16];
        faShadowProjection = new float[16];
        faShadowProjectionInverse = new float[16];
        faShadowModelView = new float[16];
        faShadowModelViewInverse = new float[16];
        projection = nextFloatBuffer(16);
        projectionInverse = nextFloatBuffer(16);
        modelView = nextFloatBuffer(16);
        modelViewInverse = nextFloatBuffer(16);
        shadowProjection = nextFloatBuffer(16);
        shadowProjectionInverse = nextFloatBuffer(16);
        shadowModelView = nextFloatBuffer(16);
        shadowModelViewInverse = nextFloatBuffer(16);
        previousProjection = nextFloatBuffer(16);
        previousModelView = nextFloatBuffer(16);
        tempMatrixDirectBuffer = nextFloatBuffer(16);
        tempDirectFloatBuffer = nextFloatBuffer(16);
        dfbColorTextures = nextIntBuffer(16);
        dfbDepthTextures = nextIntBuffer(3);
        sfbColorTextures = nextIntBuffer(8);
        sfbDepthTextures = nextIntBuffer(2);
        dfbDrawBuffers = nextIntBuffer(8);
        sfbDrawBuffers = nextIntBuffer(8);
        drawBuffersNone = nextIntBuffer(8);
        drawBuffersAll = nextIntBuffer(8);
        drawBuffersClear0 = nextIntBuffer(8);
        drawBuffersClear1 = nextIntBuffer(8);
        drawBuffersClearColor = nextIntBuffer(8);
        drawBuffersColorAtt0 = nextIntBuffer(8);
        drawBuffersBuffer = nextIntBufferArray(33, 8);
        gbufferFormatPattern = Pattern.compile("[ \t]*const[ \t]*int[ \t]*(\\w+)Format[ \t]*=[ \t]*([RGBA0123456789FUI_SNORM]*)[ \t]*;.*");
        gbufferClearPattern = Pattern.compile("[ \t]*const[ \t]*bool[ \t]*(\\w+)Clear[ \t]*=[ \t]*false[ \t]*;.*");
        gbufferMipmapEnabledPattern = Pattern.compile("[ \t]*const[ \t]*bool[ \t]*(\\w+)MipmapEnabled[ \t]*=[ \t]*true[ \t]*;.*");
        formatNames = new String[] { "R8", "RG8", "RGB8", "RGBA8", "R8_SNORM", "RG8_SNORM", "RGB8_SNORM", "RGBA8_SNORM", "R16", "RG16", "RGB16", "RGBA16", "R16_SNORM", "RG16_SNORM", "RGB16_SNORM", "RGBA16_SNORM", "R32F", "RG32F", "RGB32F", "RGBA32F", "R32I", "RG32I", "RGB32I", "RGBA32I", "R32UI", "RG32UI", "RGB32UI", "RGBA32UI", "R3_G3_B2", "RGB5_A1", "RGB10_A2", "R11F_G11F_B10F" };
        formatIds = new int[] { 33321, 33323, 32849, 32856, 36756, 36757, 36758, 36759, 33322, 33324, 32852, 32859, 36760, 36761, 36762, 36763, 33326, 33328, 34837, 34836, 33333, 33339, 36227, 36226, 33334, 33340, 36209, 36208, 10768, 32855, 32857, 35898 };
        patternLoadEntityDataMap = Pattern.compile("\\s*([\\w:]+)\\s*=\\s*([-]?\\d+)\\s*");
        Shaders.entityData = new int[32];
        Shaders.entityDataIndex = 0;
        Shaders.shadersdir = new File(Minecraft.getMinecraft().mcDataDir, "shaders");
        Shaders.shaderpacksdir = new File(Minecraft.getMinecraft().mcDataDir, Shaders.shaderpacksdirname);
        Shaders.configFile = new File(Minecraft.getMinecraft().mcDataDir, Shaders.optionsfilename);
        Shaders.drawBuffersNone.limit(0);
        Shaders.drawBuffersColorAtt0.put(36064).position(0).limit(1);
    }
    
    private static ByteBuffer nextByteBuffer(final int size) {
        final ByteBuffer bytebuffer = Shaders.bigBuffer;
        final int i = bytebuffer.limit();
        bytebuffer.position(i).limit(i + size);
        return bytebuffer.slice();
    }
    
    private static IntBuffer nextIntBuffer(final int size) {
        final ByteBuffer bytebuffer = Shaders.bigBuffer;
        final int i = bytebuffer.limit();
        bytebuffer.position(i).limit(i + size * 4);
        return bytebuffer.asIntBuffer();
    }
    
    private static FloatBuffer nextFloatBuffer(final int size) {
        final ByteBuffer bytebuffer = Shaders.bigBuffer;
        final int i = bytebuffer.limit();
        bytebuffer.position(i).limit(i + size * 4);
        return bytebuffer.asFloatBuffer();
    }
    
    private static IntBuffer[] nextIntBufferArray(final int count, final int size) {
        final IntBuffer[] aintbuffer = new IntBuffer[count];
        for (int i = 0; i < count; ++i) {
            aintbuffer[i] = nextIntBuffer(size);
        }
        return aintbuffer;
    }
    
    public static void loadConfig() {
        SMCLog.info("Load ShadersMod configuration.");
        try {
            if (!Shaders.shaderpacksdir.exists()) {
                Shaders.shaderpacksdir.mkdir();
            }
        }
        catch (Exception var8) {
            SMCLog.severe("Failed to open the shaderpacks directory: " + Shaders.shaderpacksdir);
        }
        (Shaders.shadersConfig = new PropertiesOrdered()).setProperty(EnumShaderOption.SHADER_PACK.getPropertyKey(), "");
        if (Shaders.configFile.exists()) {
            try {
                final FileReader filereader = new FileReader(Shaders.configFile);
                Shaders.shadersConfig.load(filereader);
                filereader.close();
            }
            catch (Exception ex) {}
        }
        if (!Shaders.configFile.exists()) {
            try {
                storeConfig();
            }
            catch (Exception ex2) {}
        }
        final EnumShaderOption[] aenumshaderoption = EnumShaderOption.values();
        for (int i = 0; i < aenumshaderoption.length; ++i) {
            final EnumShaderOption enumshaderoption = aenumshaderoption[i];
            final String s = enumshaderoption.getPropertyKey();
            final String s2 = enumshaderoption.getValueDefault();
            final String s3 = Shaders.shadersConfig.getProperty(s, s2);
            setEnumShaderOption(enumshaderoption, s3);
        }
        loadShaderPack();
    }
    
    private static void setEnumShaderOption(final EnumShaderOption eso, String str) {
        if (str == null) {
            str = eso.getValueDefault();
        }
        switch (eso) {
            case ANTIALIASING: {
                Shaders.configAntialiasingLevel = Config.parseInt(str, 0);
                break;
            }
            case NORMAL_MAP: {
                Shaders.configNormalMap = Config.parseBoolean(str, true);
                break;
            }
            case SPECULAR_MAP: {
                Shaders.configSpecularMap = Config.parseBoolean(str, true);
                break;
            }
            case RENDER_RES_MUL: {
                Shaders.configRenderResMul = Config.parseFloat(str, 1.0f);
                break;
            }
            case SHADOW_RES_MUL: {
                Shaders.configShadowResMul = Config.parseFloat(str, 1.0f);
                break;
            }
            case HAND_DEPTH_MUL: {
                Shaders.configHandDepthMul = Config.parseFloat(str, 0.125f);
                break;
            }
            case CLOUD_SHADOW: {
                Shaders.configCloudShadow = Config.parseBoolean(str, true);
                break;
            }
            case OLD_HAND_LIGHT: {
                Shaders.configOldHandLight.setPropertyValue(str);
                break;
            }
            case OLD_LIGHTING: {
                Shaders.configOldLighting.setPropertyValue(str);
                break;
            }
            case SHADER_PACK: {
                Shaders.currentshadername = str;
                break;
            }
            case TWEAK_BLOCK_DAMAGE: {
                Shaders.configTweakBlockDamage = Config.parseBoolean(str, true);
                break;
            }
            case SHADOW_CLIP_FRUSTRUM: {
                Shaders.configShadowClipFrustrum = Config.parseBoolean(str, true);
                break;
            }
            case TEX_MIN_FIL_B: {
                Shaders.configTexMinFilB = Config.parseInt(str, 0);
                break;
            }
            case TEX_MIN_FIL_N: {
                Shaders.configTexMinFilN = Config.parseInt(str, 0);
                break;
            }
            case TEX_MIN_FIL_S: {
                Shaders.configTexMinFilS = Config.parseInt(str, 0);
                break;
            }
            case TEX_MAG_FIL_B: {
                Shaders.configTexMagFilB = Config.parseInt(str, 0);
                break;
            }
            case TEX_MAG_FIL_N: {
                Shaders.configTexMagFilB = Config.parseInt(str, 0);
                break;
            }
            case TEX_MAG_FIL_S: {
                Shaders.configTexMagFilB = Config.parseInt(str, 0);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown option: " + eso);
            }
        }
    }
    
    public static void storeConfig() {
        SMCLog.info("Save ShadersMod configuration.");
        if (Shaders.shadersConfig == null) {
            Shaders.shadersConfig = new PropertiesOrdered();
        }
        final EnumShaderOption[] aenumshaderoption = EnumShaderOption.values();
        for (int i = 0; i < aenumshaderoption.length; ++i) {
            final EnumShaderOption enumshaderoption = aenumshaderoption[i];
            final String s = enumshaderoption.getPropertyKey();
            final String s2 = getEnumShaderOption(enumshaderoption);
            Shaders.shadersConfig.setProperty(s, s2);
        }
        try {
            final FileWriter filewriter = new FileWriter(Shaders.configFile);
            Shaders.shadersConfig.store(filewriter, null);
            filewriter.close();
        }
        catch (Exception exception) {
            SMCLog.severe("Error saving configuration: " + exception.getClass().getName() + ": " + exception.getMessage());
        }
    }
    
    public static String getEnumShaderOption(final EnumShaderOption eso) {
        switch (eso) {
            case ANTIALIASING: {
                return Integer.toString(Shaders.configAntialiasingLevel);
            }
            case NORMAL_MAP: {
                return Boolean.toString(Shaders.configNormalMap);
            }
            case SPECULAR_MAP: {
                return Boolean.toString(Shaders.configSpecularMap);
            }
            case RENDER_RES_MUL: {
                return Float.toString(Shaders.configRenderResMul);
            }
            case SHADOW_RES_MUL: {
                return Float.toString(Shaders.configShadowResMul);
            }
            case HAND_DEPTH_MUL: {
                return Float.toString(Shaders.configHandDepthMul);
            }
            case CLOUD_SHADOW: {
                return Boolean.toString(Shaders.configCloudShadow);
            }
            case OLD_HAND_LIGHT: {
                return Shaders.configOldHandLight.getPropertyValue();
            }
            case OLD_LIGHTING: {
                return Shaders.configOldLighting.getPropertyValue();
            }
            case SHADER_PACK: {
                return Shaders.currentshadername;
            }
            case TWEAK_BLOCK_DAMAGE: {
                return Boolean.toString(Shaders.configTweakBlockDamage);
            }
            case SHADOW_CLIP_FRUSTRUM: {
                return Boolean.toString(Shaders.configShadowClipFrustrum);
            }
            case TEX_MIN_FIL_B: {
                return Integer.toString(Shaders.configTexMinFilB);
            }
            case TEX_MIN_FIL_N: {
                return Integer.toString(Shaders.configTexMinFilN);
            }
            case TEX_MIN_FIL_S: {
                return Integer.toString(Shaders.configTexMinFilS);
            }
            case TEX_MAG_FIL_B: {
                return Integer.toString(Shaders.configTexMagFilB);
            }
            case TEX_MAG_FIL_N: {
                return Integer.toString(Shaders.configTexMagFilB);
            }
            case TEX_MAG_FIL_S: {
                return Integer.toString(Shaders.configTexMagFilB);
            }
            default: {
                throw new IllegalArgumentException("Unknown option: " + eso);
            }
        }
    }
    
    public static void setShaderPack(final String par1name) {
        Shaders.currentshadername = par1name;
        Shaders.shadersConfig.setProperty(EnumShaderOption.SHADER_PACK.getPropertyKey(), par1name);
        loadShaderPack();
    }
    
    public static void loadShaderPack() {
        final boolean flag = Shaders.shaderPackLoaded;
        final boolean flag2 = isOldLighting();
        Shaders.shaderPackLoaded = false;
        if (Shaders.shaderPack != null) {
            Shaders.shaderPack.close();
            Shaders.shaderPack = null;
            Shaders.shaderPackResources.clear();
            Shaders.shaderPackDimensions.clear();
            Shaders.shaderPackOptions = null;
            Shaders.shaderPackProfiles = null;
            Shaders.shaderPackGuiScreens = null;
            Shaders.shaderPackClouds.resetValue();
            Shaders.shaderPackOldHandLight.resetValue();
            Shaders.shaderPackDynamicHandLight.resetValue();
            Shaders.shaderPackOldLighting.resetValue();
            resetCustomTextures();
        }
        boolean flag3 = false;
        if (Config.isAntialiasing()) {
            SMCLog.info("Shaders can not be loaded, Antialiasing is enabled: " + Config.getAntialiasingLevel() + "x");
            flag3 = true;
        }
        if (Config.isAnisotropicFiltering()) {
            SMCLog.info("Shaders can not be loaded, Anisotropic Filtering is enabled: " + Config.getAnisotropicFilterLevel() + "x");
            flag3 = true;
        }
        if (Config.isFastRender()) {
            SMCLog.info("Shaders can not be loaded, Fast Render is enabled.");
            flag3 = true;
        }
        final String s = Shaders.shadersConfig.getProperty(EnumShaderOption.SHADER_PACK.getPropertyKey(), Shaders.packNameDefault);
        if (!s.isEmpty() && !s.equals(Shaders.packNameNone) && !flag3) {
            if (s.equals(Shaders.packNameDefault)) {
                Shaders.shaderPack = new ShaderPackDefault();
                Shaders.shaderPackLoaded = true;
            }
            else {
                try {
                    final File file1 = new File(Shaders.shaderpacksdir, s);
                    if (file1.isDirectory()) {
                        Shaders.shaderPack = new ShaderPackFolder(s, file1);
                        Shaders.shaderPackLoaded = true;
                    }
                    else if (file1.isFile() && s.toLowerCase().endsWith(".zip")) {
                        Shaders.shaderPack = new ShaderPackZip(s, file1);
                        Shaders.shaderPackLoaded = true;
                    }
                }
                catch (Exception ex) {}
            }
        }
        if (Shaders.shaderPack != null) {
            SMCLog.info("Loaded shaderpack: " + getShaderPackName());
        }
        else {
            SMCLog.info("No shaderpack loaded.");
            Shaders.shaderPack = new ShaderPackNone();
        }
        loadShaderPackResources();
        loadShaderPackDimensions();
        Shaders.shaderPackOptions = loadShaderPackOptions();
        loadShaderPackProperties();
        final boolean flag4 = Shaders.shaderPackLoaded ^ flag;
        final boolean flag5 = isOldLighting() ^ flag2;
        if (flag4 || flag5) {
            DefaultVertexFormats.updateVertexFormats();
            if (Reflector.LightUtil.exists()) {
                Reflector.LightUtil_itemConsumer.setValue(null);
                Reflector.LightUtil_tessellator.setValue(null);
            }
            updateBlockLightLevel();
            Shaders.mc.scheduleResourcesRefresh();
        }
    }
    
    private static void loadShaderPackDimensions() {
        Shaders.shaderPackDimensions.clear();
        for (int i = -128; i <= 128; ++i) {
            final String s = "/shaders/world" + i;
            if (Shaders.shaderPack.hasDirectory(s)) {
                Shaders.shaderPackDimensions.add(i);
            }
        }
        if (Shaders.shaderPackDimensions.size() > 0) {
            final Integer[] ainteger = Shaders.shaderPackDimensions.toArray(new Integer[Shaders.shaderPackDimensions.size()]);
            Config.dbg("[Shaders] Worlds: " + Config.arrayToString(ainteger));
        }
    }
    
    private static void loadShaderPackProperties() {
        Shaders.shaderPackClouds.resetValue();
        Shaders.shaderPackOldHandLight.resetValue();
        Shaders.shaderPackDynamicHandLight.resetValue();
        Shaders.shaderPackOldLighting.resetValue();
        Shaders.shaderPackShadowTranslucent.resetValue();
        Shaders.shaderPackUnderwaterOverlay.resetValue();
        Shaders.shaderPackSun.resetValue();
        Shaders.shaderPackMoon.resetValue();
        Shaders.shaderPackVignette.resetValue();
        Shaders.shaderPackBackFaceSolid.resetValue();
        Shaders.shaderPackBackFaceCutout.resetValue();
        Shaders.shaderPackBackFaceCutoutMipped.resetValue();
        Shaders.shaderPackBackFaceTranslucent.resetValue();
        BlockAliases.reset();
        if (Shaders.shaderPack != null) {
            BlockAliases.update(Shaders.shaderPack);
            final String s = "/shaders/shaders.properties";
            try {
                final InputStream inputstream = Shaders.shaderPack.getResourceAsStream(s);
                if (inputstream == null) {
                    return;
                }
                final Properties properties = new PropertiesOrdered();
                properties.load(inputstream);
                inputstream.close();
                Shaders.shaderPackClouds.loadFrom(properties);
                Shaders.shaderPackOldHandLight.loadFrom(properties);
                Shaders.shaderPackDynamicHandLight.loadFrom(properties);
                Shaders.shaderPackOldLighting.loadFrom(properties);
                Shaders.shaderPackShadowTranslucent.loadFrom(properties);
                Shaders.shaderPackUnderwaterOverlay.loadFrom(properties);
                Shaders.shaderPackSun.loadFrom(properties);
                Shaders.shaderPackVignette.loadFrom(properties);
                Shaders.shaderPackMoon.loadFrom(properties);
                Shaders.shaderPackBackFaceSolid.loadFrom(properties);
                Shaders.shaderPackBackFaceCutout.loadFrom(properties);
                Shaders.shaderPackBackFaceCutoutMipped.loadFrom(properties);
                Shaders.shaderPackBackFaceTranslucent.loadFrom(properties);
                Shaders.shaderPackProfiles = ShaderPackParser.parseProfiles(properties, Shaders.shaderPackOptions);
                Shaders.shaderPackGuiScreens = ShaderPackParser.parseGuiScreens(properties, Shaders.shaderPackProfiles, Shaders.shaderPackOptions);
                Shaders.customTexturesGbuffers = loadCustomTextures(properties, 0);
                Shaders.customTexturesComposite = loadCustomTextures(properties, 1);
            }
            catch (IOException var3) {
                Config.warn("[Shaders] Error reading: " + s);
            }
        }
    }
    
    private static CustomTexture[] loadCustomTextures(final Properties props, final int stage) {
        final String s = "texture." + Shaders.STAGE_NAMES[stage] + ".";
        final Set set = props.keySet();
        final List<CustomTexture> list = new ArrayList<CustomTexture>();
        for (final Object s2 : set) {
            final String s3 = (String)s2;
            if (s3.startsWith(s)) {
                final String s4 = s3.substring(s.length());
                final String s5 = props.getProperty(s3).trim();
                final int i = getTextureIndex(stage, s4);
                if (i < 0) {
                    SMCLog.warning("Invalid texture name: " + s3);
                }
                else {
                    try {
                        final String s6 = "shaders/" + StrUtils.removePrefix(s5, "/");
                        final InputStream inputstream = Shaders.shaderPack.getResourceAsStream(s6);
                        if (inputstream == null) {
                            SMCLog.warning("Texture not found: " + s5);
                        }
                        else {
                            IOUtils.closeQuietly(inputstream);
                            final SimpleShaderTexture simpleshadertexture = new SimpleShaderTexture(s6);
                            simpleshadertexture.loadTexture(Shaders.mc.getResourceManager());
                            final CustomTexture customtexture = new CustomTexture(i, s6, simpleshadertexture);
                            list.add(customtexture);
                        }
                    }
                    catch (IOException ioexception) {
                        SMCLog.warning("Error loading texture: " + s5);
                        SMCLog.warning(ioexception.getClass().getName() + ": " + ioexception.getMessage());
                    }
                }
            }
        }
        if (list.size() <= 0) {
            return null;
        }
        final CustomTexture[] acustomtexture = list.toArray(new CustomTexture[list.size()]);
        return acustomtexture;
    }
    
    private static int getTextureIndex(final int stage, final String name) {
        if (stage == 0) {
            if (name.equals("texture")) {
                return 0;
            }
            if (name.equals("lightmap")) {
                return 1;
            }
            if (name.equals("normals")) {
                return 2;
            }
            if (name.equals("specular")) {
                return 3;
            }
            if (name.equals("shadowtex0") || name.equals("watershadow")) {
                return 4;
            }
            if (name.equals("shadow")) {
                return Shaders.waterShadowEnabled ? 5 : 4;
            }
            if (name.equals("shadowtex1")) {
                return 5;
            }
            if (name.equals("depthtex0")) {
                return 6;
            }
            if (name.equals("gaux1")) {
                return 7;
            }
            if (name.equals("gaux2")) {
                return 8;
            }
            if (name.equals("gaux3")) {
                return 9;
            }
            if (name.equals("gaux4")) {
                return 10;
            }
            if (name.equals("depthtex1")) {
                return 12;
            }
            if (name.equals("shadowcolor0") || name.equals("shadowcolor")) {
                return 13;
            }
            if (name.equals("shadowcolor1")) {
                return 14;
            }
            if (name.equals("noisetex")) {
                return 15;
            }
        }
        if (stage == 1) {
            if (name.equals("colortex0") || name.equals("colortex0")) {
                return 0;
            }
            if (name.equals("colortex1") || name.equals("gdepth")) {
                return 1;
            }
            if (name.equals("colortex2") || name.equals("gnormal")) {
                return 2;
            }
            if (name.equals("colortex3") || name.equals("composite")) {
                return 3;
            }
            if (name.equals("shadowtex0") || name.equals("watershadow")) {
                return 4;
            }
            if (name.equals("shadow")) {
                return Shaders.waterShadowEnabled ? 5 : 4;
            }
            if (name.equals("shadowtex1")) {
                return 5;
            }
            if (name.equals("depthtex0") || name.equals("gdepthtex")) {
                return 6;
            }
            if (name.equals("colortex4") || name.equals("gaux1")) {
                return 7;
            }
            if (name.equals("colortex5") || name.equals("gaux2")) {
                return 8;
            }
            if (name.equals("colortex6") || name.equals("gaux3")) {
                return 9;
            }
            if (name.equals("colortex7") || name.equals("gaux4")) {
                return 10;
            }
            if (name.equals("depthtex1")) {
                return 11;
            }
            if (name.equals("depthtex2")) {
                return 12;
            }
            if (name.equals("shadowcolor0") || name.equals("shadowcolor")) {
                return 13;
            }
            if (name.equals("shadowcolor1")) {
                return 14;
            }
            if (name.equals("noisetex")) {
                return 15;
            }
        }
        return -1;
    }
    
    private static void bindCustomTextures(final CustomTexture[] cts) {
        if (cts != null) {
            for (int i = 0; i < cts.length; ++i) {
                final CustomTexture customtexture = cts[i];
                GlStateManager.setActiveTexture(33984 + customtexture.getTextureUnit());
                final ITextureObject itextureobject = customtexture.getTexture();
                GlStateManager.bindTexture(itextureobject.getGlTextureId());
            }
        }
    }
    
    private static void resetCustomTextures() {
        deleteCustomTextures(Shaders.customTexturesGbuffers);
        deleteCustomTextures(Shaders.customTexturesComposite);
        Shaders.customTexturesGbuffers = null;
        Shaders.customTexturesComposite = null;
    }
    
    private static void deleteCustomTextures(final CustomTexture[] cts) {
        if (cts != null) {
            for (int i = 0; i < cts.length; ++i) {
                final CustomTexture customtexture = cts[i];
                final ITextureObject itextureobject = customtexture.getTexture();
                TextureUtil.deleteTexture(itextureobject.getGlTextureId());
            }
        }
    }
    
    public static ShaderOption[] getShaderPackOptions(final String screenName) {
        ShaderOption[] ashaderoption = Shaders.shaderPackOptions.clone();
        if (Shaders.shaderPackGuiScreens == null) {
            if (Shaders.shaderPackProfiles != null) {
                final ShaderOptionProfile shaderoptionprofile = new ShaderOptionProfile(Shaders.shaderPackProfiles, ashaderoption);
                ashaderoption = (ShaderOption[])Config.addObjectToArray(ashaderoption, shaderoptionprofile, 0);
            }
            ashaderoption = getVisibleOptions(ashaderoption);
            return ashaderoption;
        }
        final String s = (screenName != null) ? ("screen." + screenName) : "screen";
        final ShaderOption[] ashaderoption2 = Shaders.shaderPackGuiScreens.get(s);
        if (ashaderoption2 == null) {
            return new ShaderOption[0];
        }
        final List<ShaderOption> list = new ArrayList<ShaderOption>();
        for (int i = 0; i < ashaderoption2.length; ++i) {
            final ShaderOption shaderoption = ashaderoption2[i];
            if (shaderoption == null) {
                list.add(null);
            }
            else if (shaderoption instanceof ShaderOptionRest) {
                final ShaderOption[] ashaderoption3 = getShaderOptionsRest(Shaders.shaderPackGuiScreens, ashaderoption);
                list.addAll(Arrays.asList(ashaderoption3));
            }
            else {
                list.add(shaderoption);
            }
        }
        final ShaderOption[] ashaderoption4 = list.toArray(new ShaderOption[list.size()]);
        return ashaderoption4;
    }
    
    private static ShaderOption[] getShaderOptionsRest(final Map<String, ShaderOption[]> mapScreens, final ShaderOption[] ops) {
        final Set<String> set = new HashSet<String>();
        for (final String s : mapScreens.keySet()) {
            final ShaderOption[] ashaderoption = mapScreens.get(s);
            for (int i = 0; i < ashaderoption.length; ++i) {
                final ShaderOption shaderoption = ashaderoption[i];
                if (shaderoption != null) {
                    set.add(shaderoption.getName());
                }
            }
        }
        final List<ShaderOption> list = new ArrayList<ShaderOption>();
        for (int j = 0; j < ops.length; ++j) {
            final ShaderOption shaderoption2 = ops[j];
            if (shaderoption2.isVisible()) {
                final String s2 = shaderoption2.getName();
                if (!set.contains(s2)) {
                    list.add(shaderoption2);
                }
            }
        }
        final ShaderOption[] ashaderoption2 = list.toArray(new ShaderOption[list.size()]);
        return ashaderoption2;
    }
    
    public static ShaderOption getShaderOption(final String name) {
        return ShaderUtils.getShaderOption(name, Shaders.shaderPackOptions);
    }
    
    public static ShaderOption[] getShaderPackOptions() {
        return Shaders.shaderPackOptions;
    }
    
    private static ShaderOption[] getVisibleOptions(final ShaderOption[] ops) {
        final List<ShaderOption> list = new ArrayList<ShaderOption>();
        for (int i = 0; i < ops.length; ++i) {
            final ShaderOption shaderoption = ops[i];
            if (shaderoption.isVisible()) {
                list.add(shaderoption);
            }
        }
        final ShaderOption[] ashaderoption = list.toArray(new ShaderOption[list.size()]);
        return ashaderoption;
    }
    
    public static void saveShaderPackOptions() {
        saveShaderPackOptions(Shaders.shaderPackOptions, Shaders.shaderPack);
    }
    
    private static void saveShaderPackOptions(final ShaderOption[] sos, final IShaderPack sp) {
        final Properties properties = new Properties();
        if (Shaders.shaderPackOptions != null) {
            for (int i = 0; i < sos.length; ++i) {
                final ShaderOption shaderoption = sos[i];
                if (shaderoption.isChanged() && shaderoption.isEnabled()) {
                    properties.setProperty(shaderoption.getName(), shaderoption.getValue());
                }
            }
        }
        try {
            saveOptionProperties(sp, properties);
        }
        catch (IOException ioexception) {
            Config.warn("[Shaders] Error saving configuration for " + Shaders.shaderPack.getName());
            ioexception.printStackTrace();
        }
    }
    
    private static void saveOptionProperties(final IShaderPack sp, final Properties props) throws IOException {
        final String s = String.valueOf(Shaders.shaderpacksdirname) + "/" + sp.getName() + ".txt";
        final File file1 = new File(Minecraft.getMinecraft().mcDataDir, s);
        if (props.isEmpty()) {
            file1.delete();
        }
        else {
            final FileOutputStream fileoutputstream = new FileOutputStream(file1);
            props.store(fileoutputstream, null);
            fileoutputstream.flush();
            fileoutputstream.close();
        }
    }
    
    private static ShaderOption[] loadShaderPackOptions() {
        try {
            final ShaderOption[] ashaderoption = ShaderPackParser.parseShaderPackOptions(Shaders.shaderPack, Shaders.programNames, Shaders.shaderPackDimensions);
            final Properties properties = loadOptionProperties(Shaders.shaderPack);
            for (int i = 0; i < ashaderoption.length; ++i) {
                final ShaderOption shaderoption = ashaderoption[i];
                final String s = properties.getProperty(shaderoption.getName());
                if (s != null) {
                    shaderoption.resetValue();
                    if (!shaderoption.setValue(s)) {
                        Config.warn("[Shaders] Invalid value, option: " + shaderoption.getName() + ", value: " + s);
                    }
                }
            }
            return ashaderoption;
        }
        catch (IOException ioexception) {
            Config.warn("[Shaders] Error reading configuration for " + Shaders.shaderPack.getName());
            ioexception.printStackTrace();
            return null;
        }
    }
    
    private static Properties loadOptionProperties(final IShaderPack sp) throws IOException {
        final Properties properties = new Properties();
        final String s = String.valueOf(Shaders.shaderpacksdirname) + "/" + sp.getName() + ".txt";
        final File file1 = new File(Minecraft.getMinecraft().mcDataDir, s);
        if (file1.exists() && file1.isFile() && file1.canRead()) {
            final FileInputStream fileinputstream = new FileInputStream(file1);
            properties.load(fileinputstream);
            fileinputstream.close();
            return properties;
        }
        return properties;
    }
    
    public static ShaderOption[] getChangedOptions(final ShaderOption[] ops) {
        final List<ShaderOption> list = new ArrayList<ShaderOption>();
        for (int i = 0; i < ops.length; ++i) {
            final ShaderOption shaderoption = ops[i];
            if (shaderoption.isEnabled() && shaderoption.isChanged()) {
                list.add(shaderoption);
            }
        }
        final ShaderOption[] ashaderoption = list.toArray(new ShaderOption[list.size()]);
        return ashaderoption;
    }
    
    private static String applyOptions(String line, final ShaderOption[] ops) {
        if (ops != null && ops.length > 0) {
            for (int i = 0; i < ops.length; ++i) {
                final ShaderOption shaderoption = ops[i];
                final String s = shaderoption.getName();
                if (shaderoption.matchesLine(line)) {
                    line = shaderoption.getSourceLine();
                    break;
                }
            }
            return line;
        }
        return line;
    }
    
    static ArrayList listOfShaders() {
        final ArrayList<String> arraylist = new ArrayList<String>();
        arraylist.add(Shaders.packNameNone);
        arraylist.add(Shaders.packNameDefault);
        try {
            if (!Shaders.shaderpacksdir.exists()) {
                Shaders.shaderpacksdir.mkdir();
            }
            final File[] afile = Shaders.shaderpacksdir.listFiles();
            for (int i = 0; i < afile.length; ++i) {
                final File file1 = afile[i];
                final String s = file1.getName();
                if (file1.isDirectory()) {
                    final File file2 = new File(file1, "shaders");
                    if (file2.exists() && file2.isDirectory()) {
                        arraylist.add(s);
                    }
                }
                else if (file1.isFile() && s.toLowerCase().endsWith(".zip")) {
                    arraylist.add(s);
                }
            }
        }
        catch (Exception ex) {}
        return arraylist;
    }
    
    static String versiontostring(final int vv) {
        final String s = Integer.toString(vv);
        return String.valueOf(Integer.toString(Integer.parseInt(s.substring(1, 3)))) + "." + Integer.toString(Integer.parseInt(s.substring(3, 5))) + "." + Integer.toString(Integer.parseInt(s.substring(5)));
    }
    
    static void checkOptifine() {
    }
    
    public static int checkFramebufferStatus(final String location) {
        final int i = EXTFramebufferObject.glCheckFramebufferStatusEXT(36160);
        if (i != 36053) {
            System.err.format("FramebufferStatus 0x%04X at %s\n", i, location);
        }
        return i;
    }
    
    public static int checkGLError(final String location) {
        final int i = GL11.glGetError();
        if (i != 0) {
            final boolean flag = false;
            if (!flag) {
                if (i == 1286) {
                    final int j = EXTFramebufferObject.glCheckFramebufferStatusEXT(36160);
                    System.err.format("GL error 0x%04X: %s (Fb status 0x%04X) at %s\n", i, GLU.gluErrorString(i), j, location);
                }
                else {
                    System.err.format("GL error 0x%04X: %s at %s\n", i, GLU.gluErrorString(i), location);
                }
            }
        }
        return i;
    }
    
    public static int checkGLError(final String location, final String info) {
        final int i = GL11.glGetError();
        if (i != 0) {
            System.err.format("GL error 0x%04x: %s at %s %s\n", i, GLU.gluErrorString(i), location, info);
        }
        return i;
    }
    
    public static int checkGLError(final String location, final String info1, final String info2) {
        final int i = GL11.glGetError();
        if (i != 0) {
            System.err.format("GL error 0x%04x: %s at %s %s %s\n", i, GLU.gluErrorString(i), location, info1, info2);
        }
        return i;
    }
    
    private static void printChat(final String str) {
        Shaders.mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(str));
    }
    
    private static void printChatAndLogError(final String str) {
        SMCLog.severe(str);
        Shaders.mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(str));
    }
    
    public static void printIntBuffer(final String title, final IntBuffer buf) {
        final StringBuilder stringbuilder = new StringBuilder(128);
        stringbuilder.append(title).append(" [pos ").append(buf.position()).append(" lim ").append(buf.limit()).append(" cap ").append(buf.capacity()).append(" :");
        for (int i = buf.limit(), j = 0; j < i; ++j) {
            stringbuilder.append(" ").append(buf.get(j));
        }
        stringbuilder.append("]");
        SMCLog.info(stringbuilder.toString());
    }
    
    public static void startup(Minecraft mc) {
        checkShadersModInstalled();
        Shaders.mc = mc;
        mc = Minecraft.getMinecraft();
        Shaders.capabilities = GLContext.getCapabilities();
        Shaders.glVersionString = GL11.glGetString(7938);
        Shaders.glVendorString = GL11.glGetString(7936);
        Shaders.glRendererString = GL11.glGetString(7937);
        SMCLog.info("ShadersMod version: 2.4.12");
        SMCLog.info("OpenGL Version: " + Shaders.glVersionString);
        SMCLog.info("Vendor:  " + Shaders.glVendorString);
        SMCLog.info("Renderer: " + Shaders.glRendererString);
        SMCLog.info("Capabilities: " + (Shaders.capabilities.OpenGL20 ? " 2.0 " : " - ") + (Shaders.capabilities.OpenGL21 ? " 2.1 " : " - ") + (Shaders.capabilities.OpenGL30 ? " 3.0 " : " - ") + (Shaders.capabilities.OpenGL32 ? " 3.2 " : " - ") + (Shaders.capabilities.OpenGL40 ? " 4.0 " : " - "));
        SMCLog.info("GL_MAX_DRAW_BUFFERS: " + GL11.glGetInteger(34852));
        SMCLog.info("GL_MAX_COLOR_ATTACHMENTS_EXT: " + GL11.glGetInteger(36063));
        SMCLog.info("GL_MAX_TEXTURE_IMAGE_UNITS: " + GL11.glGetInteger(34930));
        Shaders.hasGlGenMipmap = Shaders.capabilities.OpenGL30;
        loadConfig();
    }
    
    private static String toStringYN(final boolean b) {
        return b ? "Y" : "N";
    }
    
    public static void updateBlockLightLevel() {
        if (isOldLighting()) {
            Shaders.blockLightLevel05 = 0.5f;
            Shaders.blockLightLevel06 = 0.6f;
            Shaders.blockLightLevel08 = 0.8f;
        }
        else {
            Shaders.blockLightLevel05 = 1.0f;
            Shaders.blockLightLevel06 = 1.0f;
            Shaders.blockLightLevel08 = 1.0f;
        }
    }
    
    public static boolean isOldHandLight() {
        return Shaders.configOldHandLight.isDefault() ? (Shaders.shaderPackOldHandLight.isDefault() || Shaders.shaderPackOldHandLight.isTrue()) : Shaders.configOldHandLight.isTrue();
    }
    
    public static boolean isDynamicHandLight() {
        return Shaders.shaderPackDynamicHandLight.isDefault() || Shaders.shaderPackDynamicHandLight.isTrue();
    }
    
    public static boolean isOldLighting() {
        return Shaders.configOldLighting.isDefault() ? (Shaders.shaderPackOldLighting.isDefault() || Shaders.shaderPackOldLighting.isTrue()) : Shaders.configOldLighting.isTrue();
    }
    
    public static boolean isRenderShadowTranslucent() {
        return !Shaders.shaderPackShadowTranslucent.isFalse();
    }
    
    public static boolean isUnderwaterOverlay() {
        return !Shaders.shaderPackUnderwaterOverlay.isFalse();
    }
    
    public static boolean isSun() {
        return !Shaders.shaderPackSun.isFalse();
    }
    
    public static boolean isMoon() {
        return !Shaders.shaderPackMoon.isFalse();
    }
    
    public static boolean isVignette() {
        return !Shaders.shaderPackVignette.isFalse();
    }
    
    public static boolean isRenderBackFace(final EnumWorldBlockLayer blockLayerIn) {
        switch (blockLayerIn) {
            case SOLID: {
                return Shaders.shaderPackBackFaceSolid.isTrue();
            }
            case CUTOUT: {
                return Shaders.shaderPackBackFaceCutout.isTrue();
            }
            case CUTOUT_MIPPED: {
                return Shaders.shaderPackBackFaceCutoutMipped.isTrue();
            }
            case TRANSLUCENT: {
                return Shaders.shaderPackBackFaceTranslucent.isTrue();
            }
            default: {
                return false;
            }
        }
    }
    
    public static void init() {
        boolean flag;
        if (!Shaders.isInitializedOnce) {
            Shaders.isInitializedOnce = true;
            flag = true;
        }
        else {
            flag = false;
        }
        if (!Shaders.isShaderPackInitialized) {
            checkGLError("Shaders.init pre");
            if (getShaderPackName() != null) {}
            if (!Shaders.capabilities.OpenGL20) {
                printChatAndLogError("No OpenGL 2.0");
            }
            if (!Shaders.capabilities.GL_EXT_framebuffer_object) {
                printChatAndLogError("No EXT_framebuffer_object");
            }
            Shaders.dfbDrawBuffers.position(0).limit(8);
            Shaders.dfbColorTextures.position(0).limit(16);
            Shaders.dfbDepthTextures.position(0).limit(3);
            Shaders.sfbDrawBuffers.position(0).limit(8);
            Shaders.sfbDepthTextures.position(0).limit(2);
            Shaders.sfbColorTextures.position(0).limit(8);
            Shaders.usedColorBuffers = 4;
            Shaders.usedDepthBuffers = 1;
            Shaders.usedShadowColorBuffers = 0;
            Shaders.usedShadowDepthBuffers = 0;
            Shaders.usedColorAttachs = 1;
            Shaders.usedDrawBuffers = 1;
            Arrays.fill(Shaders.gbuffersFormat, 6408);
            Arrays.fill(Shaders.gbuffersClear, true);
            Arrays.fill(Shaders.shadowHardwareFilteringEnabled, false);
            Arrays.fill(Shaders.shadowMipmapEnabled, false);
            Arrays.fill(Shaders.shadowFilterNearest, false);
            Arrays.fill(Shaders.shadowColorMipmapEnabled, false);
            Arrays.fill(Shaders.shadowColorFilterNearest, false);
            Shaders.centerDepthSmoothEnabled = false;
            Shaders.noiseTextureEnabled = false;
            Shaders.sunPathRotation = 0.0f;
            Shaders.shadowIntervalSize = 2.0f;
            Shaders.shadowDistanceRenderMul = -1.0f;
            Shaders.aoLevel = -1.0f;
            Shaders.useEntityAttrib = false;
            Shaders.useMidTexCoordAttrib = false;
            Shaders.useMultiTexCoord3Attrib = false;
            Shaders.useTangentAttrib = false;
            Shaders.waterShadowEnabled = false;
            Shaders.updateChunksErrorRecorded = false;
            updateBlockLightLevel();
            final ShaderProfile shaderprofile = ShaderUtils.detectProfile(Shaders.shaderPackProfiles, Shaders.shaderPackOptions, false);
            String s = "";
            if (Shaders.currentWorld != null) {
                final int i = Shaders.currentWorld.provider.getDimensionId();
                if (Shaders.shaderPackDimensions.contains(i)) {
                    s = "world" + i + "/";
                }
            }
            if (Shaders.saveFinalShaders) {
                clearDirectory(new File(Shaders.shaderpacksdir, "debug"));
            }
            for (int k1 = 0; k1 < 33; ++k1) {
                String s2 = Shaders.programNames[k1];
                if (s2.equals("")) {
                    Shaders.programsID[k1] = (Shaders.programsRef[k1] = 0);
                    Shaders.programsDrawBufSettings[k1] = null;
                    Shaders.programsColorAtmSettings[k1] = null;
                    Shaders.programsCompositeMipmapSetting[k1] = 0;
                }
                else {
                    Shaders.newDrawBufSetting = null;
                    Shaders.newColorAtmSetting = null;
                    Shaders.newCompositeMipmapSetting = 0;
                    String s3 = String.valueOf(s) + s2;
                    if (shaderprofile != null && shaderprofile.isProgramDisabled(s3)) {
                        SMCLog.info("Program disabled: " + s3);
                        s2 = "<disabled>";
                        s3 = String.valueOf(s) + s2;
                    }
                    final String s4 = "/shaders/" + s3;
                    final int j = setupProgram(k1, String.valueOf(s4) + ".vsh", String.valueOf(s4) + ".fsh");
                    if (j > 0) {
                        SMCLog.info("Program loaded: " + s3);
                    }
                    Shaders.programsID[k1] = (Shaders.programsRef[k1] = j);
                    Shaders.programsDrawBufSettings[k1] = ((j != 0) ? Shaders.newDrawBufSetting : null);
                    Shaders.programsColorAtmSettings[k1] = ((j != 0) ? Shaders.newColorAtmSetting : null);
                    Shaders.programsCompositeMipmapSetting[k1] = ((j != 0) ? Shaders.newCompositeMipmapSetting : 0);
                }
            }
            final int l1 = GL11.glGetInteger(34852);
            new HashMap();
            for (int i2 = 0; i2 < 33; ++i2) {
                Arrays.fill(Shaders.programsToggleColorTextures[i2], false);
                if (i2 == 29) {
                    Shaders.programsDrawBuffers[i2] = null;
                }
                else if (Shaders.programsID[i2] == 0) {
                    if (i2 == 30) {
                        Shaders.programsDrawBuffers[i2] = Shaders.drawBuffersNone;
                    }
                    else {
                        Shaders.programsDrawBuffers[i2] = Shaders.drawBuffersColorAtt0;
                    }
                }
                else {
                    final String s5 = Shaders.programsDrawBufSettings[i2];
                    if (s5 != null) {
                        final IntBuffer intbuffer = Shaders.drawBuffersBuffer[i2];
                        int m = s5.length();
                        if (m > Shaders.usedDrawBuffers) {
                            Shaders.usedDrawBuffers = m;
                        }
                        if (m > l1) {
                            m = l1;
                        }
                        (Shaders.programsDrawBuffers[i2] = intbuffer).limit(m);
                        for (int l2 = 0; l2 < m; ++l2) {
                            int i3 = 0;
                            if (s5.length() > l2) {
                                final int j2 = s5.charAt(l2) - '0';
                                if (i2 != 30) {
                                    if (j2 >= 0 && j2 <= 7) {
                                        Shaders.programsToggleColorTextures[i2][j2] = true;
                                        i3 = j2 + 36064;
                                        if (j2 > Shaders.usedColorAttachs) {
                                            Shaders.usedColorAttachs = j2;
                                        }
                                        if (j2 > Shaders.usedColorBuffers) {
                                            Shaders.usedColorBuffers = j2;
                                        }
                                    }
                                }
                                else if (j2 >= 0 && j2 <= 1) {
                                    i3 = j2 + 36064;
                                    if (j2 > Shaders.usedShadowColorBuffers) {
                                        Shaders.usedShadowColorBuffers = j2;
                                    }
                                }
                            }
                            intbuffer.put(l2, i3);
                        }
                    }
                    else if (i2 != 30 && i2 != 31 && i2 != 32) {
                        Shaders.programsDrawBuffers[i2] = Shaders.dfbDrawBuffers;
                        Shaders.usedDrawBuffers = Shaders.usedColorBuffers;
                        Arrays.fill(Shaders.programsToggleColorTextures[i2], 0, Shaders.usedColorBuffers, true);
                    }
                    else {
                        Shaders.programsDrawBuffers[i2] = Shaders.sfbDrawBuffers;
                    }
                }
            }
            Shaders.usedColorAttachs = Shaders.usedColorBuffers;
            Shaders.shadowPassInterval = ((Shaders.usedShadowDepthBuffers > 0) ? 1 : 0);
            Shaders.shouldSkipDefaultShadow = (Shaders.usedShadowDepthBuffers > 0);
            SMCLog.info("usedColorBuffers: " + Shaders.usedColorBuffers);
            SMCLog.info("usedDepthBuffers: " + Shaders.usedDepthBuffers);
            SMCLog.info("usedShadowColorBuffers: " + Shaders.usedShadowColorBuffers);
            SMCLog.info("usedShadowDepthBuffers: " + Shaders.usedShadowDepthBuffers);
            SMCLog.info("usedColorAttachs: " + Shaders.usedColorAttachs);
            SMCLog.info("usedDrawBuffers: " + Shaders.usedDrawBuffers);
            Shaders.dfbDrawBuffers.position(0).limit(Shaders.usedDrawBuffers);
            Shaders.dfbColorTextures.position(0).limit(Shaders.usedColorBuffers * 2);
            for (int j3 = 0; j3 < Shaders.usedDrawBuffers; ++j3) {
                Shaders.dfbDrawBuffers.put(j3, 36064 + j3);
            }
            if (Shaders.usedDrawBuffers > l1) {
                printChatAndLogError("[Shaders] Error: Not enough draw buffers, needed: " + Shaders.usedDrawBuffers + ", available: " + l1);
            }
            Shaders.sfbDrawBuffers.position(0).limit(Shaders.usedShadowColorBuffers);
            for (int k2 = 0; k2 < Shaders.usedShadowColorBuffers; ++k2) {
                Shaders.sfbDrawBuffers.put(k2, 36064 + k2);
            }
            for (int l3 = 0; l3 < 33; ++l3) {
                int i4;
                for (i4 = l3; Shaders.programsID[i4] == 0 && Shaders.programBackups[i4] != i4; i4 = Shaders.programBackups[i4]) {}
                if (i4 != l3 && l3 != 30) {
                    Shaders.programsID[l3] = Shaders.programsID[i4];
                    Shaders.programsDrawBufSettings[l3] = Shaders.programsDrawBufSettings[i4];
                    Shaders.programsDrawBuffers[l3] = Shaders.programsDrawBuffers[i4];
                }
            }
            resize();
            resizeShadow();
            if (Shaders.noiseTextureEnabled) {
                setupNoiseTexture();
            }
            if (Shaders.defaultTexture == null) {
                Shaders.defaultTexture = ShadersTex.createDefaultTexture();
            }
            GlStateManager.pushMatrix();
            GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
            preCelestialRotate();
            postCelestialRotate();
            GlStateManager.popMatrix();
            Shaders.isShaderPackInitialized = true;
            loadEntityDataMap();
            resetDisplayList();
            if (!flag) {}
            checkGLError("Shaders.init");
        }
    }
    
    public static void resetDisplayList() {
        ++Shaders.numberResetDisplayList;
        Shaders.needResetModels = true;
        SMCLog.info("Reset world renderers");
        Shaders.mc.renderGlobal.loadRenderers();
    }
    
    public static void resetDisplayListModels() {
        if (Shaders.needResetModels) {
            Shaders.needResetModels = false;
            SMCLog.info("Reset model renderers");
            for (final Object render : Shaders.mc.getRenderManager().getEntityRenderMap().values()) {
                if (render instanceof RendererLivingEntity) {
                    final RendererLivingEntity rendererlivingentity = (RendererLivingEntity)render;
                    resetDisplayListModel(rendererlivingentity.getMainModel());
                }
            }
        }
    }
    
    public static void resetDisplayListModel(final ModelBase model) {
        if (model != null) {
            for (final Object object : model.boxList) {
                if (object instanceof ModelRenderer) {
                    resetDisplayListModelRenderer((ModelRenderer)object);
                }
            }
        }
    }
    
    public static void resetDisplayListModelRenderer(final ModelRenderer mrr) {
        mrr.resetDisplayList();
        if (mrr.childModels != null) {
            for (int i = 0, j = mrr.childModels.size(); i < j; ++i) {
                resetDisplayListModelRenderer(mrr.childModels.get(i));
            }
        }
    }
    
    private static int setupProgram(final int program, final String vShaderPath, final String fShaderPath) {
        checkGLError("pre setupProgram");
        int i = ARBShaderObjects.glCreateProgramObjectARB();
        checkGLError("create");
        if (i != 0) {
            Shaders.progUseEntityAttrib = false;
            Shaders.progUseMidTexCoordAttrib = false;
            Shaders.progUseTangentAttrib = false;
            final int j = createVertShader(vShaderPath);
            final int k = createFragShader(fShaderPath);
            checkGLError("create");
            if (j == 0 && k == 0) {
                ARBShaderObjects.glDeleteObjectARB(i);
                i = 0;
            }
            else {
                if (j != 0) {
                    ARBShaderObjects.glAttachObjectARB(i, j);
                    checkGLError("attach");
                }
                if (k != 0) {
                    ARBShaderObjects.glAttachObjectARB(i, k);
                    checkGLError("attach");
                }
                if (Shaders.progUseEntityAttrib) {
                    ARBVertexShader.glBindAttribLocationARB(i, Shaders.entityAttrib, "mc_Entity");
                    checkGLError("mc_Entity");
                }
                if (Shaders.progUseMidTexCoordAttrib) {
                    ARBVertexShader.glBindAttribLocationARB(i, Shaders.midTexCoordAttrib, "mc_midTexCoord");
                    checkGLError("mc_midTexCoord");
                }
                if (Shaders.progUseTangentAttrib) {
                    ARBVertexShader.glBindAttribLocationARB(i, Shaders.tangentAttrib, "at_tangent");
                    checkGLError("at_tangent");
                }
                ARBShaderObjects.glLinkProgramARB(i);
                if (GL20.glGetProgrami(i, 35714) != 1) {
                    SMCLog.severe("Error linking program: " + i);
                }
                printLogInfo(i, String.valueOf(vShaderPath) + ", " + fShaderPath);
                if (j != 0) {
                    ARBShaderObjects.glDetachObjectARB(i, j);
                    ARBShaderObjects.glDeleteObjectARB(j);
                }
                if (k != 0) {
                    ARBShaderObjects.glDetachObjectARB(i, k);
                    ARBShaderObjects.glDeleteObjectARB(k);
                }
                Shaders.programsID[program] = i;
                useProgram(program);
                ARBShaderObjects.glValidateProgramARB(i);
                useProgram(0);
                printLogInfo(i, String.valueOf(vShaderPath) + ", " + fShaderPath);
                final int l = GL20.glGetProgrami(i, 35715);
                if (l != 1) {
                    final String s = "\"";
                    printChatAndLogError("[Shaders] Error: Invalid program " + s + Shaders.programNames[program] + s);
                    ARBShaderObjects.glDeleteObjectARB(i);
                    i = 0;
                }
            }
        }
        return i;
    }
    
    private static int createVertShader(final String filename) {
        final int i = ARBShaderObjects.glCreateShaderObjectARB(35633);
        if (i == 0) {
            return 0;
        }
        final StringBuilder stringbuilder = new StringBuilder(131072);
        BufferedReader bufferedreader = null;
        try {
            bufferedreader = new BufferedReader(new InputStreamReader(Shaders.shaderPack.getResourceAsStream(filename)));
        }
        catch (Exception var8) {
            try {
                bufferedreader = new BufferedReader(new FileReader(new File(filename)));
            }
            catch (Exception var9) {
                ARBShaderObjects.glDeleteObjectARB(i);
                return 0;
            }
        }
        final ShaderOption[] ashaderoption = getChangedOptions(Shaders.shaderPackOptions);
        final List<String> list = new ArrayList<String>();
        if (bufferedreader != null) {
            try {
                bufferedreader = ShaderPackParser.resolveIncludes(bufferedreader, filename, Shaders.shaderPack, 0, list, 0);
                while (true) {
                    String s = bufferedreader.readLine();
                    if (s == null) {
                        break;
                    }
                    s = applyOptions(s, ashaderoption);
                    stringbuilder.append(s).append('\n');
                    if (s.matches("attribute [_a-zA-Z0-9]+ mc_Entity.*")) {
                        Shaders.useEntityAttrib = true;
                        Shaders.progUseEntityAttrib = true;
                    }
                    else if (s.matches("attribute [_a-zA-Z0-9]+ mc_midTexCoord.*")) {
                        Shaders.useMidTexCoordAttrib = true;
                        Shaders.progUseMidTexCoordAttrib = true;
                    }
                    else if (s.matches(".*gl_MultiTexCoord3.*")) {
                        Shaders.useMultiTexCoord3Attrib = true;
                    }
                    else {
                        if (!s.matches("attribute [_a-zA-Z0-9]+ at_tangent.*")) {
                            continue;
                        }
                        Shaders.useTangentAttrib = true;
                        Shaders.progUseTangentAttrib = true;
                    }
                }
                bufferedreader.close();
            }
            catch (Exception exception) {
                SMCLog.severe("Couldn't read " + filename + "!");
                exception.printStackTrace();
                ARBShaderObjects.glDeleteObjectARB(i);
                return 0;
            }
        }
        if (Shaders.saveFinalShaders) {
            saveShader(filename, stringbuilder.toString());
        }
        ARBShaderObjects.glShaderSourceARB(i, stringbuilder);
        ARBShaderObjects.glCompileShaderARB(i);
        if (GL20.glGetShaderi(i, 35713) != 1) {
            SMCLog.severe("Error compiling vertex shader: " + filename);
        }
        printShaderLogInfo(i, filename, list);
        return i;
    }
    
    private static int createFragShader(final String filename) {
        final int i = ARBShaderObjects.glCreateShaderObjectARB(35632);
        if (i == 0) {
            return 0;
        }
        final StringBuilder stringbuilder = new StringBuilder(131072);
        BufferedReader bufferedreader = null;
        try {
            bufferedreader = new BufferedReader(new InputStreamReader(Shaders.shaderPack.getResourceAsStream(filename)));
        }
        catch (Exception var13) {
            try {
                bufferedreader = new BufferedReader(new FileReader(new File(filename)));
            }
            catch (Exception var14) {
                ARBShaderObjects.glDeleteObjectARB(i);
                return 0;
            }
        }
        final ShaderOption[] ashaderoption = getChangedOptions(Shaders.shaderPackOptions);
        final List<String> list = new ArrayList<String>();
        if (bufferedreader != null) {
            try {
                bufferedreader = ShaderPackParser.resolveIncludes(bufferedreader, filename, Shaders.shaderPack, 0, list, 0);
                while (true) {
                    String s = bufferedreader.readLine();
                    if (s == null) {
                        break;
                    }
                    s = applyOptions(s, ashaderoption);
                    stringbuilder.append(s).append('\n');
                    if (s.matches("#version .*")) {
                        continue;
                    }
                    if (s.matches("uniform [ _a-zA-Z0-9]+ shadow;.*")) {
                        if (Shaders.usedShadowDepthBuffers >= 1) {
                            continue;
                        }
                        Shaders.usedShadowDepthBuffers = 1;
                    }
                    else if (s.matches("uniform [ _a-zA-Z0-9]+ watershadow;.*")) {
                        Shaders.waterShadowEnabled = true;
                        if (Shaders.usedShadowDepthBuffers >= 2) {
                            continue;
                        }
                        Shaders.usedShadowDepthBuffers = 2;
                    }
                    else if (s.matches("uniform [ _a-zA-Z0-9]+ shadowtex0;.*")) {
                        if (Shaders.usedShadowDepthBuffers >= 1) {
                            continue;
                        }
                        Shaders.usedShadowDepthBuffers = 1;
                    }
                    else if (s.matches("uniform [ _a-zA-Z0-9]+ shadowtex1;.*")) {
                        if (Shaders.usedShadowDepthBuffers >= 2) {
                            continue;
                        }
                        Shaders.usedShadowDepthBuffers = 2;
                    }
                    else if (s.matches("uniform [ _a-zA-Z0-9]+ shadowcolor;.*")) {
                        if (Shaders.usedShadowColorBuffers >= 1) {
                            continue;
                        }
                        Shaders.usedShadowColorBuffers = 1;
                    }
                    else if (s.matches("uniform [ _a-zA-Z0-9]+ shadowcolor0;.*")) {
                        if (Shaders.usedShadowColorBuffers >= 1) {
                            continue;
                        }
                        Shaders.usedShadowColorBuffers = 1;
                    }
                    else if (s.matches("uniform [ _a-zA-Z0-9]+ shadowcolor1;.*")) {
                        if (Shaders.usedShadowColorBuffers >= 2) {
                            continue;
                        }
                        Shaders.usedShadowColorBuffers = 2;
                    }
                    else if (s.matches("uniform [ _a-zA-Z0-9]+ depthtex0;.*")) {
                        if (Shaders.usedDepthBuffers >= 1) {
                            continue;
                        }
                        Shaders.usedDepthBuffers = 1;
                    }
                    else if (s.matches("uniform [ _a-zA-Z0-9]+ depthtex1;.*")) {
                        if (Shaders.usedDepthBuffers >= 2) {
                            continue;
                        }
                        Shaders.usedDepthBuffers = 2;
                    }
                    else if (s.matches("uniform [ _a-zA-Z0-9]+ depthtex2;.*")) {
                        if (Shaders.usedDepthBuffers >= 3) {
                            continue;
                        }
                        Shaders.usedDepthBuffers = 3;
                    }
                    else if (s.matches("uniform [ _a-zA-Z0-9]+ gdepth;.*")) {
                        if (Shaders.gbuffersFormat[1] != 6408) {
                            continue;
                        }
                        Shaders.gbuffersFormat[1] = 34836;
                    }
                    else if (Shaders.usedColorBuffers < 5 && s.matches("uniform [ _a-zA-Z0-9]+ gaux1;.*")) {
                        Shaders.usedColorBuffers = 5;
                    }
                    else if (Shaders.usedColorBuffers < 6 && s.matches("uniform [ _a-zA-Z0-9]+ gaux2;.*")) {
                        Shaders.usedColorBuffers = 6;
                    }
                    else if (Shaders.usedColorBuffers < 7 && s.matches("uniform [ _a-zA-Z0-9]+ gaux3;.*")) {
                        Shaders.usedColorBuffers = 7;
                    }
                    else if (Shaders.usedColorBuffers < 8 && s.matches("uniform [ _a-zA-Z0-9]+ gaux4;.*")) {
                        Shaders.usedColorBuffers = 8;
                    }
                    else if (Shaders.usedColorBuffers < 5 && s.matches("uniform [ _a-zA-Z0-9]+ colortex4;.*")) {
                        Shaders.usedColorBuffers = 5;
                    }
                    else if (Shaders.usedColorBuffers < 6 && s.matches("uniform [ _a-zA-Z0-9]+ colortex5;.*")) {
                        Shaders.usedColorBuffers = 6;
                    }
                    else if (Shaders.usedColorBuffers < 7 && s.matches("uniform [ _a-zA-Z0-9]+ colortex6;.*")) {
                        Shaders.usedColorBuffers = 7;
                    }
                    else if (Shaders.usedColorBuffers < 8 && s.matches("uniform [ _a-zA-Z0-9]+ colortex7;.*")) {
                        Shaders.usedColorBuffers = 8;
                    }
                    else if (s.matches("uniform [ _a-zA-Z0-9]+ centerDepthSmooth;.*")) {
                        Shaders.centerDepthSmoothEnabled = true;
                    }
                    else if (s.matches("/\\* SHADOWRES:[0-9]+ \\*/.*")) {
                        final String[] astring17 = s.split("(:| )", 4);
                        SMCLog.info("Shadow map resolution: " + astring17[2]);
                        Shaders.spShadowMapWidth = (Shaders.spShadowMapHeight = Integer.parseInt(astring17[2]));
                        Shaders.shadowMapWidth = (Shaders.shadowMapHeight = Math.round(Shaders.spShadowMapWidth * Shaders.configShadowResMul));
                    }
                    else if (s.matches("[ \t]*const[ \t]*int[ \t]*shadowMapResolution[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        final String[] astring18 = s.split("(=[ \t]*|;)");
                        SMCLog.info("Shadow map resolution: " + astring18[1]);
                        Shaders.spShadowMapWidth = (Shaders.spShadowMapHeight = Integer.parseInt(astring18[1]));
                        Shaders.shadowMapWidth = (Shaders.shadowMapHeight = Math.round(Shaders.spShadowMapWidth * Shaders.configShadowResMul));
                    }
                    else if (s.matches("/\\* SHADOWFOV:[0-9\\.]+ \\*/.*")) {
                        final String[] astring19 = s.split("(:| )", 4);
                        SMCLog.info("Shadow map field of view: " + astring19[2]);
                        Shaders.shadowMapFOV = Float.parseFloat(astring19[2]);
                        Shaders.shadowMapIsOrtho = false;
                    }
                    else if (s.matches("/\\* SHADOWHPL:[0-9\\.]+ \\*/.*")) {
                        final String[] astring20 = s.split("(:| )", 4);
                        SMCLog.info("Shadow map half-plane: " + astring20[2]);
                        Shaders.shadowMapHalfPlane = Float.parseFloat(astring20[2]);
                        Shaders.shadowMapIsOrtho = true;
                    }
                    else if (s.matches("[ \t]*const[ \t]*float[ \t]*shadowDistance[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        final String[] astring21 = s.split("(=[ \t]*|;)");
                        SMCLog.info("Shadow map distance: " + astring21[1]);
                        Shaders.shadowMapHalfPlane = Float.parseFloat(astring21[1]);
                        Shaders.shadowMapIsOrtho = true;
                    }
                    else if (s.matches("[ \t]*const[ \t]*float[ \t]*shadowDistanceRenderMul[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        final String[] astring22 = s.split("(=[ \t]*|;)");
                        SMCLog.info("Shadow distance render mul: " + astring22[1]);
                        Shaders.shadowDistanceRenderMul = Float.parseFloat(astring22[1]);
                    }
                    else if (s.matches("[ \t]*const[ \t]*float[ \t]*shadowIntervalSize[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        final String[] astring23 = s.split("(=[ \t]*|;)");
                        SMCLog.info("Shadow map interval size: " + astring23[1]);
                        Shaders.shadowIntervalSize = Float.parseFloat(astring23[1]);
                    }
                    else if (s.matches("[ \t]*const[ \t]*bool[ \t]*generateShadowMipmap[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("Generate shadow mipmap");
                        Arrays.fill(Shaders.shadowMipmapEnabled, true);
                    }
                    else if (s.matches("[ \t]*const[ \t]*bool[ \t]*generateShadowColorMipmap[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("Generate shadow color mipmap");
                        Arrays.fill(Shaders.shadowColorMipmapEnabled, true);
                    }
                    else if (s.matches("[ \t]*const[ \t]*bool[ \t]*shadowHardwareFiltering[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("Hardware shadow filtering enabled.");
                        Arrays.fill(Shaders.shadowHardwareFilteringEnabled, true);
                    }
                    else if (s.matches("[ \t]*const[ \t]*bool[ \t]*shadowHardwareFiltering0[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("shadowHardwareFiltering0");
                        Shaders.shadowHardwareFilteringEnabled[0] = true;
                    }
                    else if (s.matches("[ \t]*const[ \t]*bool[ \t]*shadowHardwareFiltering1[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("shadowHardwareFiltering1");
                        Shaders.shadowHardwareFilteringEnabled[1] = true;
                    }
                    else if (s.matches("[ \t]*const[ \t]*bool[ \t]*(shadowtex0Mipmap|shadowtexMipmap)[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("shadowtex0Mipmap");
                        Shaders.shadowMipmapEnabled[0] = true;
                    }
                    else if (s.matches("[ \t]*const[ \t]*bool[ \t]*(shadowtex1Mipmap)[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("shadowtex1Mipmap");
                        Shaders.shadowMipmapEnabled[1] = true;
                    }
                    else if (s.matches("[ \t]*const[ \t]*bool[ \t]*(shadowcolor0Mipmap|shadowColor0Mipmap)[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("shadowcolor0Mipmap");
                        Shaders.shadowColorMipmapEnabled[0] = true;
                    }
                    else if (s.matches("[ \t]*const[ \t]*bool[ \t]*(shadowcolor1Mipmap|shadowColor1Mipmap)[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("shadowcolor1Mipmap");
                        Shaders.shadowColorMipmapEnabled[1] = true;
                    }
                    else if (s.matches("[ \t]*const[ \t]*bool[ \t]*(shadowtex0Nearest|shadowtexNearest|shadow0MinMagNearest)[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("shadowtex0Nearest");
                        Shaders.shadowFilterNearest[0] = true;
                    }
                    else if (s.matches("[ \t]*const[ \t]*bool[ \t]*(shadowtex1Nearest|shadow1MinMagNearest)[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("shadowtex1Nearest");
                        Shaders.shadowFilterNearest[1] = true;
                    }
                    else if (s.matches("[ \t]*const[ \t]*bool[ \t]*(shadowcolor0Nearest|shadowColor0Nearest|shadowColor0MinMagNearest)[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("shadowcolor0Nearest");
                        Shaders.shadowColorFilterNearest[0] = true;
                    }
                    else if (s.matches("[ \t]*const[ \t]*bool[ \t]*(shadowcolor1Nearest|shadowColor1Nearest|shadowColor1MinMagNearest)[ \t]*=[ \t]*true[ \t]*;.*")) {
                        SMCLog.info("shadowcolor1Nearest");
                        Shaders.shadowColorFilterNearest[1] = true;
                    }
                    else if (s.matches("/\\* WETNESSHL:[0-9\\.]+ \\*/.*")) {
                        final String[] astring24 = s.split("(:| )", 4);
                        SMCLog.info("Wetness halflife: " + astring24[2]);
                        Shaders.wetnessHalfLife = Float.parseFloat(astring24[2]);
                    }
                    else if (s.matches("[ \t]*const[ \t]*float[ \t]*wetnessHalflife[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        final String[] astring25 = s.split("(=[ \t]*|;)");
                        SMCLog.info("Wetness halflife: " + astring25[1]);
                        Shaders.wetnessHalfLife = Float.parseFloat(astring25[1]);
                    }
                    else if (s.matches("/\\* DRYNESSHL:[0-9\\.]+ \\*/.*")) {
                        final String[] astring26 = s.split("(:| )", 4);
                        SMCLog.info("Dryness halflife: " + astring26[2]);
                        Shaders.drynessHalfLife = Float.parseFloat(astring26[2]);
                    }
                    else if (s.matches("[ \t]*const[ \t]*float[ \t]*drynessHalflife[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        final String[] astring27 = s.split("(=[ \t]*|;)");
                        SMCLog.info("Dryness halflife: " + astring27[1]);
                        Shaders.drynessHalfLife = Float.parseFloat(astring27[1]);
                    }
                    else if (s.matches("[ \t]*const[ \t]*float[ \t]*eyeBrightnessHalflife[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        final String[] astring28 = s.split("(=[ \t]*|;)");
                        SMCLog.info("Eye brightness halflife: " + astring28[1]);
                        Shaders.eyeBrightnessHalflife = Float.parseFloat(astring28[1]);
                    }
                    else if (s.matches("[ \t]*const[ \t]*float[ \t]*centerDepthHalflife[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        final String[] astring29 = s.split("(=[ \t]*|;)");
                        SMCLog.info("Center depth halflife: " + astring29[1]);
                        Shaders.centerDepthSmoothHalflife = Float.parseFloat(astring29[1]);
                    }
                    else if (s.matches("[ \t]*const[ \t]*float[ \t]*sunPathRotation[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        final String[] astring30 = s.split("(=[ \t]*|;)");
                        SMCLog.info("Sun path rotation: " + astring30[1]);
                        Shaders.sunPathRotation = Float.parseFloat(astring30[1]);
                    }
                    else if (s.matches("[ \t]*const[ \t]*float[ \t]*ambientOcclusionLevel[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        final String[] astring31 = s.split("(=[ \t]*|;)");
                        SMCLog.info("AO Level: " + astring31[1]);
                        Shaders.aoLevel = Config.limit(Float.parseFloat(astring31[1]), 0.0f, 1.0f);
                    }
                    else if (s.matches("[ \t]*const[ \t]*int[ \t]*superSamplingLevel[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        final String[] astring32 = s.split("(=[ \t]*|;)");
                        final int i2 = Integer.parseInt(astring32[1]);
                        if (i2 > 1) {
                            SMCLog.info("Super sampling level: " + i2 + "x");
                            Shaders.superSamplingLevel = i2;
                        }
                        else {
                            Shaders.superSamplingLevel = 1;
                        }
                    }
                    else if (s.matches("[ \t]*const[ \t]*int[ \t]*noiseTextureResolution[ \t]*=[ \t]*-?[0-9.]+f?;.*")) {
                        final String[] astring33 = s.split("(=[ \t]*|;)");
                        SMCLog.info("Noise texture enabled");
                        SMCLog.info("Noise texture resolution: " + astring33[1]);
                        Shaders.noiseTextureResolution = Integer.parseInt(astring33[1]);
                        Shaders.noiseTextureEnabled = true;
                    }
                    else if (s.matches("[ \t]*const[ \t]*int[ \t]*\\w+Format[ \t]*=[ \t]*[RGBA0123456789FUI_SNORM]*[ \t]*;.*")) {
                        final Matcher matcher2 = Shaders.gbufferFormatPattern.matcher(s);
                        matcher2.matches();
                        final String s2 = matcher2.group(1);
                        final String s3 = matcher2.group(2);
                        final int k = getBufferIndexFromString(s2);
                        final int l = getTextureFormatFromString(s3);
                        if (k < 0 || l == 0) {
                            continue;
                        }
                        Shaders.gbuffersFormat[k] = l;
                        SMCLog.info("%s format: %s", s2, s3);
                    }
                    else if (s.matches("[ \t]*const[ \t]*bool[ \t]*\\w+Clear[ \t]*=[ \t]*false[ \t]*;.*")) {
                        if (!filename.matches(".*composite[0-9]?.fsh")) {
                            continue;
                        }
                        final Matcher matcher3 = Shaders.gbufferClearPattern.matcher(s);
                        matcher3.matches();
                        final String s4 = matcher3.group(1);
                        final int j1 = getBufferIndexFromString(s4);
                        if (j1 < 0) {
                            continue;
                        }
                        Shaders.gbuffersClear[j1] = false;
                        SMCLog.info("%s clear disabled", s4);
                    }
                    else if (s.matches("/\\* GAUX4FORMAT:RGBA32F \\*/.*")) {
                        SMCLog.info("gaux4 format : RGB32AF");
                        Shaders.gbuffersFormat[7] = 34836;
                    }
                    else if (s.matches("/\\* GAUX4FORMAT:RGB32F \\*/.*")) {
                        SMCLog.info("gaux4 format : RGB32F");
                        Shaders.gbuffersFormat[7] = 34837;
                    }
                    else if (s.matches("/\\* GAUX4FORMAT:RGB16 \\*/.*")) {
                        SMCLog.info("gaux4 format : RGB16");
                        Shaders.gbuffersFormat[7] = 32852;
                    }
                    else if (s.matches("[ \t]*const[ \t]*bool[ \t]*\\w+MipmapEnabled[ \t]*=[ \t]*true[ \t]*;.*")) {
                        if (!filename.matches(".*composite[0-9]?.fsh") && !filename.matches(".*final.fsh")) {
                            continue;
                        }
                        final Matcher matcher4 = Shaders.gbufferMipmapEnabledPattern.matcher(s);
                        matcher4.matches();
                        final String s5 = matcher4.group(1);
                        final int m = getBufferIndexFromString(s5);
                        if (m < 0) {
                            continue;
                        }
                        Shaders.newCompositeMipmapSetting |= 1 << m;
                        SMCLog.info("%s mipmap enabled", s5);
                    }
                    else {
                        if (!s.matches("/\\* DRAWBUFFERS:[0-7N]* \\*/.*")) {
                            continue;
                        }
                        final String[] astring34 = s.split("(:| )", 4);
                        Shaders.newDrawBufSetting = astring34[2];
                    }
                }
                bufferedreader.close();
            }
            catch (Exception exception) {
                SMCLog.severe("Couldn't read " + filename + "!");
                exception.printStackTrace();
                ARBShaderObjects.glDeleteObjectARB(i);
                return 0;
            }
        }
        if (Shaders.saveFinalShaders) {
            saveShader(filename, stringbuilder.toString());
        }
        ARBShaderObjects.glShaderSourceARB(i, stringbuilder);
        ARBShaderObjects.glCompileShaderARB(i);
        if (GL20.glGetShaderi(i, 35713) != 1) {
            SMCLog.severe("Error compiling fragment shader: " + filename);
        }
        printShaderLogInfo(i, filename, list);
        return i;
    }
    
    private static void saveShader(final String filename, final String code) {
        try {
            final File file1 = new File(Shaders.shaderpacksdir, "debug/" + filename);
            file1.getParentFile().mkdirs();
            Config.writeFile(file1, code);
        }
        catch (IOException ioexception) {
            Config.warn("Error saving: " + filename);
            ioexception.printStackTrace();
        }
    }
    
    private static void clearDirectory(final File dir) {
        if (dir.exists() && dir.isDirectory()) {
            final File[] afile = dir.listFiles();
            if (afile != null) {
                for (int i = 0; i < afile.length; ++i) {
                    final File file1 = afile[i];
                    if (file1.isDirectory()) {
                        clearDirectory(file1);
                    }
                    file1.delete();
                }
            }
        }
    }
    
    private static boolean printLogInfo(final int obj, final String name) {
        final IntBuffer intbuffer = BufferUtils.createIntBuffer(1);
        ARBShaderObjects.glGetObjectParameterARB(obj, 35716, intbuffer);
        final int i = intbuffer.get();
        if (i > 1) {
            final ByteBuffer bytebuffer = BufferUtils.createByteBuffer(i);
            intbuffer.flip();
            ARBShaderObjects.glGetInfoLogARB(obj, intbuffer, bytebuffer);
            final byte[] abyte = new byte[i];
            bytebuffer.get(abyte);
            if (abyte[i - 1] == 0) {
                abyte[i - 1] = 10;
            }
            final String s = new String(abyte);
            SMCLog.info("Info log: " + name + "\n" + s);
            return false;
        }
        return true;
    }
    
    private static boolean printShaderLogInfo(final int shader, final String name, final List<String> listFiles) {
        final IntBuffer intbuffer = BufferUtils.createIntBuffer(1);
        final int i = GL20.glGetShaderi(shader, 35716);
        if (i <= 1) {
            return true;
        }
        for (int j = 0; j < listFiles.size(); ++j) {
            final String s = listFiles.get(j);
            SMCLog.info("File: " + (j + 1) + " = " + s);
        }
        final String s2 = GL20.glGetShaderInfoLog(shader, i);
        SMCLog.info("Shader info log: " + name + "\n" + s2);
        return false;
    }
    
    public static void setDrawBuffers(IntBuffer drawBuffers) {
        if (drawBuffers == null) {
            drawBuffers = Shaders.drawBuffersNone;
        }
        if (Shaders.activeDrawBuffers != drawBuffers) {
            GL20.glDrawBuffers(Shaders.activeDrawBuffers = drawBuffers);
        }
    }
    
    public static void useProgram(int program) {
        checkGLError("pre-useProgram");
        if (Shaders.isShadowPass) {
            program = 30;
            if (Shaders.programsID[30] == 0) {
                Shaders.normalMapEnabled = false;
                return;
            }
        }
        if (Shaders.activeProgram != program) {
            Shaders.activeProgram = program;
            ARBShaderObjects.glUseProgramObjectARB(Shaders.programsID[program]);
            if (Shaders.programsID[program] == 0) {
                Shaders.normalMapEnabled = false;
            }
            else {
                if (checkGLError("useProgram ", Shaders.programNames[program]) != 0) {
                    Shaders.programsID[program] = 0;
                }
                final IntBuffer intbuffer = Shaders.programsDrawBuffers[program];
                if (Shaders.isRenderingDfb) {
                    setDrawBuffers(intbuffer);
                    checkGLError(Shaders.programNames[program], " draw buffers = ", Shaders.programsDrawBufSettings[program]);
                }
                Shaders.activeCompositeMipmapSetting = Shaders.programsCompositeMipmapSetting[program];
                Shaders.uniformEntityColor.setProgram(Shaders.programsID[Shaders.activeProgram]);
                Shaders.uniformEntityId.setProgram(Shaders.programsID[Shaders.activeProgram]);
                Shaders.uniformBlockEntityId.setProgram(Shaders.programsID[Shaders.activeProgram]);
                switch (program) {
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
                    case 20: {
                        Shaders.normalMapEnabled = true;
                        setProgramUniform1i("texture", 0);
                        setProgramUniform1i("lightmap", 1);
                        setProgramUniform1i("normals", 2);
                        setProgramUniform1i("specular", 3);
                        setProgramUniform1i("shadow", Shaders.waterShadowEnabled ? 5 : 4);
                        setProgramUniform1i("watershadow", 4);
                        setProgramUniform1i("shadowtex0", 4);
                        setProgramUniform1i("shadowtex1", 5);
                        setProgramUniform1i("depthtex0", 6);
                        if (Shaders.customTexturesGbuffers != null) {
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
                    }
                    default: {
                        Shaders.normalMapEnabled = false;
                        break;
                    }
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 29: {
                        Shaders.normalMapEnabled = false;
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
                        setProgramUniform1i("shadow", Shaders.waterShadowEnabled ? 5 : 4);
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
                    }
                    case 30:
                    case 31:
                    case 32: {
                        setProgramUniform1i("tex", 0);
                        setProgramUniform1i("texture", 0);
                        setProgramUniform1i("lightmap", 1);
                        setProgramUniform1i("normals", 2);
                        setProgramUniform1i("specular", 3);
                        setProgramUniform1i("shadow", Shaders.waterShadowEnabled ? 5 : 4);
                        setProgramUniform1i("watershadow", 4);
                        setProgramUniform1i("shadowtex0", 4);
                        setProgramUniform1i("shadowtex1", 5);
                        if (Shaders.customTexturesGbuffers != null) {
                            setProgramUniform1i("gaux1", 7);
                            setProgramUniform1i("gaux2", 8);
                            setProgramUniform1i("gaux3", 9);
                            setProgramUniform1i("gaux4", 10);
                        }
                        setProgramUniform1i("shadowcolor", 13);
                        setProgramUniform1i("shadowcolor0", 13);
                        setProgramUniform1i("shadowcolor1", 14);
                        setProgramUniform1i("noisetex", 15);
                        break;
                    }
                }
                final ItemStack itemstack = (Minecraft.thePlayer != null) ? Minecraft.thePlayer.getHeldItem() : null;
                final Item item = (itemstack != null) ? itemstack.getItem() : null;
                int i = -1;
                Block block = null;
                if (item != null) {
                    i = Item.itemRegistry.getIDForObject(item);
                    block = Block.blockRegistry.getObjectById(i);
                }
                final int j = (block != null) ? block.getLightValue() : 0;
                setProgramUniform1i("heldItemId", i);
                setProgramUniform1i("heldBlockLightValue", j);
                setProgramUniform1i("fogMode", Shaders.fogEnabled ? Shaders.fogMode : 0);
                setProgramUniform3f("fogColor", Shaders.fogColorR, Shaders.fogColorG, Shaders.fogColorB);
                setProgramUniform3f("skyColor", Shaders.skyColorR, Shaders.skyColorG, Shaders.skyColorB);
                setProgramUniform1i("worldTime", (int)(Shaders.worldTime % 24000L));
                setProgramUniform1i("worldDay", (int)(Shaders.worldTime / 24000L));
                setProgramUniform1i("moonPhase", Shaders.moonPhase);
                setProgramUniform1i("frameCounter", Shaders.frameCounter);
                setProgramUniform1f("frameTime", Shaders.frameTime);
                setProgramUniform1f("frameTimeCounter", Shaders.frameTimeCounter);
                setProgramUniform1f("sunAngle", Shaders.sunAngle);
                setProgramUniform1f("shadowAngle", Shaders.shadowAngle);
                setProgramUniform1f("rainStrength", Shaders.rainStrength);
                setProgramUniform1f("aspectRatio", Shaders.renderWidth / (float)Shaders.renderHeight);
                setProgramUniform1f("viewWidth", (float)Shaders.renderWidth);
                setProgramUniform1f("viewHeight", (float)Shaders.renderHeight);
                setProgramUniform1f("near", 0.05f);
                setProgramUniform1f("far", (float)(Shaders.mc.gameSettings.renderDistanceChunks * 16));
                setProgramUniform3f("sunPosition", Shaders.sunPosition[0], Shaders.sunPosition[1], Shaders.sunPosition[2]);
                setProgramUniform3f("moonPosition", Shaders.moonPosition[0], Shaders.moonPosition[1], Shaders.moonPosition[2]);
                setProgramUniform3f("shadowLightPosition", Shaders.shadowLightPosition[0], Shaders.shadowLightPosition[1], Shaders.shadowLightPosition[2]);
                setProgramUniform3f("upPosition", Shaders.upPosition[0], Shaders.upPosition[1], Shaders.upPosition[2]);
                setProgramUniform3f("previousCameraPosition", (float)Shaders.previousCameraPositionX, (float)Shaders.previousCameraPositionY, (float)Shaders.previousCameraPositionZ);
                setProgramUniform3f("cameraPosition", (float)Shaders.cameraPositionX, (float)Shaders.cameraPositionY, (float)Shaders.cameraPositionZ);
                setProgramUniformMatrix4ARB("gbufferModelView", false, Shaders.modelView);
                setProgramUniformMatrix4ARB("gbufferModelViewInverse", false, Shaders.modelViewInverse);
                setProgramUniformMatrix4ARB("gbufferPreviousProjection", false, Shaders.previousProjection);
                setProgramUniformMatrix4ARB("gbufferProjection", false, Shaders.projection);
                setProgramUniformMatrix4ARB("gbufferProjectionInverse", false, Shaders.projectionInverse);
                setProgramUniformMatrix4ARB("gbufferPreviousModelView", false, Shaders.previousModelView);
                if (Shaders.usedShadowDepthBuffers > 0) {
                    setProgramUniformMatrix4ARB("shadowProjection", false, Shaders.shadowProjection);
                    setProgramUniformMatrix4ARB("shadowProjectionInverse", false, Shaders.shadowProjectionInverse);
                    setProgramUniformMatrix4ARB("shadowModelView", false, Shaders.shadowModelView);
                    setProgramUniformMatrix4ARB("shadowModelViewInverse", false, Shaders.shadowModelViewInverse);
                }
                setProgramUniform1f("wetness", Shaders.wetness);
                setProgramUniform1f("eyeAltitude", Shaders.eyePosY);
                setProgramUniform2i("eyeBrightness", Shaders.eyeBrightness & 0xFFFF, Shaders.eyeBrightness >> 16);
                setProgramUniform2i("eyeBrightnessSmooth", Math.round(Shaders.eyeBrightnessFadeX), Math.round(Shaders.eyeBrightnessFadeY));
                setProgramUniform2i("terrainTextureSize", Shaders.terrainTextureSize[0], Shaders.terrainTextureSize[1]);
                setProgramUniform1i("terrainIconSize", Shaders.terrainIconSize);
                setProgramUniform1i("isEyeInWater", Shaders.isEyeInWater);
                setProgramUniform1f("nightVision", Shaders.nightVision);
                setProgramUniform1f("blindness", Shaders.blindness);
                setProgramUniform1f("screenBrightness", Shaders.mc.gameSettings.gammaSetting);
                setProgramUniform1i("hideGUI", Shaders.mc.gameSettings.hideGUI ? 1 : 0);
                setProgramUniform1f("centerDepthSmooth", Shaders.centerDepthSmooth);
                setProgramUniform2i("atlasSize", Shaders.atlasSizeX, Shaders.atlasSizeY);
                checkGLError("useProgram ", Shaders.programNames[program]);
            }
        }
    }
    
    public static void setProgramUniform1i(final String name, final int x) {
        final int i = Shaders.programsID[Shaders.activeProgram];
        if (i != 0) {
            final int j = ARBShaderObjects.glGetUniformLocationARB(i, name);
            ARBShaderObjects.glUniform1iARB(j, x);
            checkGLError(Shaders.programNames[Shaders.activeProgram], name);
        }
    }
    
    public static void setProgramUniform2i(final String name, final int x, final int y) {
        final int i = Shaders.programsID[Shaders.activeProgram];
        if (i != 0) {
            final int j = ARBShaderObjects.glGetUniformLocationARB(i, name);
            ARBShaderObjects.glUniform2iARB(j, x, y);
            checkGLError(Shaders.programNames[Shaders.activeProgram], name);
        }
    }
    
    public static void setProgramUniform1f(final String name, final float x) {
        final int i = Shaders.programsID[Shaders.activeProgram];
        if (i != 0) {
            final int j = ARBShaderObjects.glGetUniformLocationARB(i, name);
            ARBShaderObjects.glUniform1fARB(j, x);
            checkGLError(Shaders.programNames[Shaders.activeProgram], name);
        }
    }
    
    public static void setProgramUniform3f(final String name, final float x, final float y, final float z) {
        final int i = Shaders.programsID[Shaders.activeProgram];
        if (i != 0) {
            final int j = ARBShaderObjects.glGetUniformLocationARB(i, name);
            ARBShaderObjects.glUniform3fARB(j, x, y, z);
            checkGLError(Shaders.programNames[Shaders.activeProgram], name);
        }
    }
    
    public static void setProgramUniformMatrix4ARB(final String name, final boolean transpose, final FloatBuffer matrix) {
        final int i = Shaders.programsID[Shaders.activeProgram];
        if (i != 0 && matrix != null) {
            final int j = ARBShaderObjects.glGetUniformLocationARB(i, name);
            ARBShaderObjects.glUniformMatrix4ARB(j, transpose, matrix);
            checkGLError(Shaders.programNames[Shaders.activeProgram], name);
        }
    }
    
    private static int getBufferIndexFromString(final String name) {
        return (!name.equals("colortex0") && !name.equals("gcolor")) ? ((!name.equals("colortex1") && !name.equals("gdepth")) ? ((!name.equals("colortex2") && !name.equals("gnormal")) ? ((!name.equals("colortex3") && !name.equals("composite")) ? ((!name.equals("colortex4") && !name.equals("gaux1")) ? ((!name.equals("colortex5") && !name.equals("gaux2")) ? ((!name.equals("colortex6") && !name.equals("gaux3")) ? ((!name.equals("colortex7") && !name.equals("gaux4")) ? -1 : 7) : 6) : 5) : 4) : 3) : 2) : 1) : 0;
    }
    
    private static int getTextureFormatFromString(String par) {
        par = par.trim();
        for (int i = 0; i < Shaders.formatNames.length; ++i) {
            final String s = Shaders.formatNames[i];
            if (par.equals(s)) {
                return Shaders.formatIds[i];
            }
        }
        return 0;
    }
    
    private static void setupNoiseTexture() {
        if (Shaders.noiseTexture == null) {
            Shaders.noiseTexture = new HFNoiseTexture(Shaders.noiseTextureResolution, Shaders.noiseTextureResolution);
        }
    }
    
    private static void loadEntityDataMap() {
        Shaders.mapBlockToEntityData = new IdentityHashMap<Block, Integer>(300);
        if (Shaders.mapBlockToEntityData.isEmpty()) {
            for (final ResourceLocation resourcelocation : Block.blockRegistry.getKeys()) {
                final Block block = Block.blockRegistry.getObject(resourcelocation);
                final int i = Block.blockRegistry.getIDForObject(block);
                Shaders.mapBlockToEntityData.put(block, i);
            }
        }
        BufferedReader bufferedreader = null;
        try {
            bufferedreader = new BufferedReader(new InputStreamReader(Shaders.shaderPack.getResourceAsStream("/mc_Entity_x.txt")));
        }
        catch (Exception ex) {}
        if (bufferedreader != null) {
            try {
                String s1;
                while ((s1 = bufferedreader.readLine()) != null) {
                    final Matcher matcher = Shaders.patternLoadEntityDataMap.matcher(s1);
                    if (matcher.matches()) {
                        final String s2 = matcher.group(1);
                        final String s3 = matcher.group(2);
                        final int j = Integer.parseInt(s3);
                        final Block block2 = Block.getBlockFromName(s2);
                        if (block2 != null) {
                            Shaders.mapBlockToEntityData.put(block2, j);
                        }
                        else {
                            SMCLog.warning("Unknown block name %s", s2);
                        }
                    }
                    else {
                        SMCLog.warning("unmatched %s\n", s1);
                    }
                }
            }
            catch (Exception var9) {
                SMCLog.warning("Error parsing mc_Entity_x.txt");
            }
        }
        if (bufferedreader != null) {
            try {
                bufferedreader.close();
            }
            catch (Exception ex2) {}
        }
    }
    
    private static IntBuffer fillIntBufferZero(final IntBuffer buf) {
        for (int i = buf.limit(), j = buf.position(); j < i; ++j) {
            buf.put(j, 0);
        }
        return buf;
    }
    
    public static void uninit() {
        if (Shaders.isShaderPackInitialized) {
            checkGLError("Shaders.uninit pre");
            for (int i = 0; i < 33; ++i) {
                if (Shaders.programsRef[i] != 0) {
                    ARBShaderObjects.glDeleteObjectARB(Shaders.programsRef[i]);
                    checkGLError("del programRef");
                }
                Shaders.programsRef[i] = 0;
                Shaders.programsID[i] = 0;
                Shaders.programsDrawBufSettings[i] = null;
                Shaders.programsDrawBuffers[i] = null;
                Shaders.programsCompositeMipmapSetting[i] = 0;
            }
            if (Shaders.dfb != 0) {
                EXTFramebufferObject.glDeleteFramebuffersEXT(Shaders.dfb);
                Shaders.dfb = 0;
                checkGLError("del dfb");
            }
            if (Shaders.sfb != 0) {
                EXTFramebufferObject.glDeleteFramebuffersEXT(Shaders.sfb);
                Shaders.sfb = 0;
                checkGLError("del sfb");
            }
            if (Shaders.dfbDepthTextures != null) {
                GlStateManager.deleteTextures(Shaders.dfbDepthTextures);
                fillIntBufferZero(Shaders.dfbDepthTextures);
                checkGLError("del dfbDepthTextures");
            }
            if (Shaders.dfbColorTextures != null) {
                GlStateManager.deleteTextures(Shaders.dfbColorTextures);
                fillIntBufferZero(Shaders.dfbColorTextures);
                checkGLError("del dfbTextures");
            }
            if (Shaders.sfbDepthTextures != null) {
                GlStateManager.deleteTextures(Shaders.sfbDepthTextures);
                fillIntBufferZero(Shaders.sfbDepthTextures);
                checkGLError("del shadow depth");
            }
            if (Shaders.sfbColorTextures != null) {
                GlStateManager.deleteTextures(Shaders.sfbColorTextures);
                fillIntBufferZero(Shaders.sfbColorTextures);
                checkGLError("del shadow color");
            }
            if (Shaders.dfbDrawBuffers != null) {
                fillIntBufferZero(Shaders.dfbDrawBuffers);
            }
            if (Shaders.noiseTexture != null) {
                Shaders.noiseTexture.destroy();
                Shaders.noiseTexture = null;
            }
            SMCLog.info("Uninit");
            Shaders.shadowPassInterval = 0;
            Shaders.shouldSkipDefaultShadow = false;
            Shaders.isShaderPackInitialized = false;
            checkGLError("Shaders.uninit");
        }
    }
    
    public static void scheduleResize() {
        Shaders.renderDisplayHeight = 0;
    }
    
    public static void scheduleResizeShadow() {
        Shaders.needResizeShadow = true;
    }
    
    private static void resize() {
        Shaders.renderDisplayWidth = Minecraft.displayWidth;
        Shaders.renderDisplayHeight = Minecraft.displayHeight;
        Shaders.renderWidth = Math.round(Shaders.renderDisplayWidth * Shaders.configRenderResMul);
        Shaders.renderHeight = Math.round(Shaders.renderDisplayHeight * Shaders.configRenderResMul);
        setupFrameBuffer();
    }
    
    private static void resizeShadow() {
        Shaders.needResizeShadow = false;
        Shaders.shadowMapWidth = Math.round(Shaders.spShadowMapWidth * Shaders.configShadowResMul);
        Shaders.shadowMapHeight = Math.round(Shaders.spShadowMapHeight * Shaders.configShadowResMul);
        setupShadowFrameBuffer();
    }
    
    private static void setupFrameBuffer() {
        if (Shaders.dfb != 0) {
            EXTFramebufferObject.glDeleteFramebuffersEXT(Shaders.dfb);
            GlStateManager.deleteTextures(Shaders.dfbDepthTextures);
            GlStateManager.deleteTextures(Shaders.dfbColorTextures);
        }
        Shaders.dfb = EXTFramebufferObject.glGenFramebuffersEXT();
        GL11.glGenTextures((IntBuffer)Shaders.dfbDepthTextures.clear().limit(Shaders.usedDepthBuffers));
        GL11.glGenTextures((IntBuffer)Shaders.dfbColorTextures.clear().limit(16));
        Shaders.dfbDepthTextures.position(0);
        Shaders.dfbColorTextures.position(0);
        Shaders.dfbColorTextures.get(Shaders.dfbColorTexturesA).position(0);
        EXTFramebufferObject.glBindFramebufferEXT(36160, Shaders.dfb);
        GL20.glDrawBuffers(0);
        GL11.glReadBuffer(0);
        for (int i = 0; i < Shaders.usedDepthBuffers; ++i) {
            GlStateManager.bindTexture(Shaders.dfbDepthTextures.get(i));
            GL11.glTexParameteri(3553, 10242, 10496);
            GL11.glTexParameteri(3553, 10243, 10496);
            GL11.glTexParameteri(3553, 10241, 9728);
            GL11.glTexParameteri(3553, 10240, 9728);
            GL11.glTexParameteri(3553, 34891, 6409);
            GL11.glTexImage2D(3553, 0, 6402, Shaders.renderWidth, Shaders.renderHeight, 0, 6402, 5126, (FloatBuffer)null);
        }
        EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36096, 3553, Shaders.dfbDepthTextures.get(0), 0);
        GL20.glDrawBuffers(Shaders.dfbDrawBuffers);
        GL11.glReadBuffer(0);
        checkGLError("FT d");
        for (int k = 0; k < Shaders.usedColorBuffers; ++k) {
            GlStateManager.bindTexture(Shaders.dfbColorTexturesA[k]);
            GL11.glTexParameteri(3553, 10242, 10496);
            GL11.glTexParameteri(3553, 10243, 10496);
            GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
            GL11.glTexImage2D(3553, 0, Shaders.gbuffersFormat[k], Shaders.renderWidth, Shaders.renderHeight, 0, 32993, 33639, (ByteBuffer)null);
            EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064 + k, 3553, Shaders.dfbColorTexturesA[k], 0);
            checkGLError("FT c");
        }
        for (int l = 0; l < Shaders.usedColorBuffers; ++l) {
            GlStateManager.bindTexture(Shaders.dfbColorTexturesA[8 + l]);
            GL11.glTexParameteri(3553, 10242, 10496);
            GL11.glTexParameteri(3553, 10243, 10496);
            GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
            GL11.glTexImage2D(3553, 0, Shaders.gbuffersFormat[l], Shaders.renderWidth, Shaders.renderHeight, 0, 32993, 33639, (ByteBuffer)null);
            checkGLError("FT ca");
        }
        int i2 = EXTFramebufferObject.glCheckFramebufferStatusEXT(36160);
        if (i2 == 36058) {
            printChatAndLogError("[Shaders] Error: Failed framebuffer incomplete formats");
            for (int j = 0; j < Shaders.usedColorBuffers; ++j) {
                GlStateManager.bindTexture(Shaders.dfbColorTextures.get(j));
                GL11.glTexImage2D(3553, 0, 6408, Shaders.renderWidth, Shaders.renderHeight, 0, 32993, 33639, (ByteBuffer)null);
                EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064 + j, 3553, Shaders.dfbColorTextures.get(j), 0);
                checkGLError("FT c");
            }
            i2 = EXTFramebufferObject.glCheckFramebufferStatusEXT(36160);
            if (i2 == 36053) {
                SMCLog.info("complete");
            }
        }
        GlStateManager.bindTexture(0);
        if (i2 != 36053) {
            printChatAndLogError("[Shaders] Error: Failed creating framebuffer! (Status " + i2 + ")");
        }
        else {
            SMCLog.info("Framebuffer created.");
        }
    }
    
    private static void setupShadowFrameBuffer() {
        if (Shaders.usedShadowDepthBuffers != 0) {
            if (Shaders.sfb != 0) {
                EXTFramebufferObject.glDeleteFramebuffersEXT(Shaders.sfb);
                GlStateManager.deleteTextures(Shaders.sfbDepthTextures);
                GlStateManager.deleteTextures(Shaders.sfbColorTextures);
            }
            EXTFramebufferObject.glBindFramebufferEXT(36160, Shaders.sfb = EXTFramebufferObject.glGenFramebuffersEXT());
            GL11.glDrawBuffer(0);
            GL11.glReadBuffer(0);
            GL11.glGenTextures((IntBuffer)Shaders.sfbDepthTextures.clear().limit(Shaders.usedShadowDepthBuffers));
            GL11.glGenTextures((IntBuffer)Shaders.sfbColorTextures.clear().limit(Shaders.usedShadowColorBuffers));
            Shaders.sfbDepthTextures.position(0);
            Shaders.sfbColorTextures.position(0);
            for (int i = 0; i < Shaders.usedShadowDepthBuffers; ++i) {
                GlStateManager.bindTexture(Shaders.sfbDepthTextures.get(i));
                GL11.glTexParameterf(3553, 10242, 10496.0f);
                GL11.glTexParameterf(3553, 10243, 10496.0f);
                final int j = Shaders.shadowFilterNearest[i] ? 9728 : 9729;
                GL11.glTexParameteri(3553, 10241, j);
                GL11.glTexParameteri(3553, 10240, j);
                if (Shaders.shadowHardwareFilteringEnabled[i]) {
                    GL11.glTexParameteri(3553, 34892, 34894);
                }
                GL11.glTexImage2D(3553, 0, 6402, Shaders.shadowMapWidth, Shaders.shadowMapHeight, 0, 6402, 5126, (FloatBuffer)null);
            }
            EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36096, 3553, Shaders.sfbDepthTextures.get(0), 0);
            checkGLError("FT sd");
            for (int k = 0; k < Shaders.usedShadowColorBuffers; ++k) {
                GlStateManager.bindTexture(Shaders.sfbColorTextures.get(k));
                GL11.glTexParameterf(3553, 10242, 10496.0f);
                GL11.glTexParameterf(3553, 10243, 10496.0f);
                final int i2 = Shaders.shadowColorFilterNearest[k] ? 9728 : 9729;
                GL11.glTexParameteri(3553, 10241, i2);
                GL11.glTexParameteri(3553, 10240, i2);
                GL11.glTexImage2D(3553, 0, 6408, Shaders.shadowMapWidth, Shaders.shadowMapHeight, 0, 32993, 33639, (ByteBuffer)null);
                EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064 + k, 3553, Shaders.sfbColorTextures.get(k), 0);
                checkGLError("FT sc");
            }
            GlStateManager.bindTexture(0);
            if (Shaders.usedShadowColorBuffers > 0) {
                GL20.glDrawBuffers(Shaders.sfbDrawBuffers);
            }
            final int l = EXTFramebufferObject.glCheckFramebufferStatusEXT(36160);
            if (l != 36053) {
                printChatAndLogError("[Shaders] Error: Failed creating shadow framebuffer! (Status " + l + ")");
            }
            else {
                SMCLog.info("Shadow framebuffer created.");
            }
        }
    }
    
    public static void beginRender(final Minecraft minecraft, final float partialTicks, final long finishTimeNano) {
        checkGLError("pre beginRender");
        checkWorldChanged(Shaders.mc.theWorld);
        Shaders.mc = minecraft;
        Shaders.mc.mcProfiler.startSection("init");
        Shaders.entityRenderer = Shaders.mc.entityRenderer;
        if (!Shaders.isShaderPackInitialized) {
            try {
                init();
            }
            catch (IllegalStateException illegalstateexception) {
                if (Config.normalize(illegalstateexception.getMessage()).equals("Function is not supported")) {
                    printChatAndLogError("[Shaders] Error: " + illegalstateexception.getMessage());
                    illegalstateexception.printStackTrace();
                    setShaderPack(Shaders.packNameNone);
                    return;
                }
            }
        }
        if (Minecraft.displayWidth != Shaders.renderDisplayWidth || Minecraft.displayHeight != Shaders.renderDisplayHeight) {
            resize();
        }
        if (Shaders.needResizeShadow) {
            resizeShadow();
        }
        Shaders.worldTime = Shaders.mc.theWorld.getWorldTime();
        Shaders.diffWorldTime = (Shaders.worldTime - Shaders.lastWorldTime) % 24000L;
        if (Shaders.diffWorldTime < 0L) {
            Shaders.diffWorldTime += 24000L;
        }
        Shaders.lastWorldTime = Shaders.worldTime;
        Shaders.moonPhase = Shaders.mc.theWorld.getMoonPhase();
        ++Shaders.frameCounter;
        if (Shaders.frameCounter >= 720720) {
            Shaders.frameCounter = 0;
        }
        Shaders.systemTime = System.currentTimeMillis();
        if (Shaders.lastSystemTime == 0L) {
            Shaders.lastSystemTime = Shaders.systemTime;
        }
        Shaders.diffSystemTime = Shaders.systemTime - Shaders.lastSystemTime;
        Shaders.lastSystemTime = Shaders.systemTime;
        Shaders.frameTime = Shaders.diffSystemTime / 1000.0f;
        Shaders.frameTimeCounter += Shaders.frameTime;
        Shaders.frameTimeCounter %= 3600.0f;
        Shaders.rainStrength = minecraft.theWorld.getRainStrength(partialTicks);
        final float f = Shaders.diffSystemTime * 0.01f;
        float f2 = (float)Math.exp(Math.log(0.5) * f / ((Shaders.wetness < Shaders.rainStrength) ? Shaders.drynessHalfLife : Shaders.wetnessHalfLife));
        Shaders.wetness = Shaders.wetness * f2 + Shaders.rainStrength * (1.0f - f2);
        final Entity entity = Shaders.mc.getRenderViewEntity();
        if (entity != null) {
            Shaders.isSleeping = (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPlayerSleeping());
            Shaders.eyePosY = (float)entity.posY * partialTicks + (float)entity.lastTickPosY * (1.0f - partialTicks);
            Shaders.eyeBrightness = entity.getBrightnessForRender(partialTicks);
            f2 = Shaders.diffSystemTime * 0.01f;
            final float f3 = (float)Math.exp(Math.log(0.5) * f2 / Shaders.eyeBrightnessHalflife);
            Shaders.eyeBrightnessFadeX = Shaders.eyeBrightnessFadeX * f3 + (Shaders.eyeBrightness & 0xFFFF) * (1.0f - f3);
            Shaders.eyeBrightnessFadeY = Shaders.eyeBrightnessFadeY * f3 + (Shaders.eyeBrightness >> 16) * (1.0f - f3);
            Shaders.isEyeInWater = ((Shaders.mc.gameSettings.thirdPersonView == 0 && !Shaders.isSleeping && Minecraft.thePlayer.isInsideOfMaterial(Material.water)) ? 1 : 0);
            if (Minecraft.thePlayer != null) {
                Shaders.nightVision = 0.0f;
                if (Minecraft.thePlayer.isPotionActive(Potion.nightVision)) {
                    Shaders.nightVision = Config.getMinecraft().entityRenderer.getNightVisionBrightness(Minecraft.thePlayer, partialTicks);
                }
                Shaders.blindness = 0.0f;
                if (Minecraft.thePlayer.isPotionActive(Potion.blindness)) {
                    final int i = Minecraft.thePlayer.getActivePotionEffect(Potion.blindness).getDuration();
                    Shaders.blindness = Config.limit(i / 20.0f, 0.0f, 1.0f);
                }
            }
            Vec3 vec3 = Shaders.mc.theWorld.getSkyColor(entity, partialTicks);
            vec3 = CustomColors.getWorldSkyColor(vec3, Shaders.currentWorld, entity, partialTicks);
            Shaders.skyColorR = (float)vec3.xCoord;
            Shaders.skyColorG = (float)vec3.yCoord;
            Shaders.skyColorB = (float)vec3.zCoord;
        }
        Shaders.isRenderingWorld = true;
        Shaders.isCompositeRendered = false;
        Shaders.isHandRenderedMain = false;
        if (Shaders.usedShadowDepthBuffers >= 1) {
            GlStateManager.setActiveTexture(33988);
            GlStateManager.bindTexture(Shaders.sfbDepthTextures.get(0));
            if (Shaders.usedShadowDepthBuffers >= 2) {
                GlStateManager.setActiveTexture(33989);
                GlStateManager.bindTexture(Shaders.sfbDepthTextures.get(1));
            }
        }
        GlStateManager.setActiveTexture(33984);
        for (int j = 0; j < Shaders.usedColorBuffers; ++j) {
            GlStateManager.bindTexture(Shaders.dfbColorTexturesA[j]);
            GL11.glTexParameteri(3553, 10240, 9729);
            GL11.glTexParameteri(3553, 10241, 9729);
            GlStateManager.bindTexture(Shaders.dfbColorTexturesA[8 + j]);
            GL11.glTexParameteri(3553, 10240, 9729);
            GL11.glTexParameteri(3553, 10241, 9729);
        }
        GlStateManager.bindTexture(0);
        for (int k = 0; k < 4 && 4 + k < Shaders.usedColorBuffers; ++k) {
            GlStateManager.setActiveTexture(33991 + k);
            GlStateManager.bindTexture(Shaders.dfbColorTextures.get(4 + k));
        }
        GlStateManager.setActiveTexture(33990);
        GlStateManager.bindTexture(Shaders.dfbDepthTextures.get(0));
        if (Shaders.usedDepthBuffers >= 2) {
            GlStateManager.setActiveTexture(33995);
            GlStateManager.bindTexture(Shaders.dfbDepthTextures.get(1));
            if (Shaders.usedDepthBuffers >= 3) {
                GlStateManager.setActiveTexture(33996);
                GlStateManager.bindTexture(Shaders.dfbDepthTextures.get(2));
            }
        }
        for (int l = 0; l < Shaders.usedShadowColorBuffers; ++l) {
            GlStateManager.setActiveTexture(33997 + l);
            GlStateManager.bindTexture(Shaders.sfbColorTextures.get(l));
        }
        if (Shaders.noiseTextureEnabled) {
            GlStateManager.setActiveTexture(33984 + Shaders.noiseTexture.textureUnit);
            GlStateManager.bindTexture(Shaders.noiseTexture.getID());
            GL11.glTexParameteri(3553, 10242, 10497);
            GL11.glTexParameteri(3553, 10243, 10497);
            GL11.glTexParameteri(3553, 10240, 9729);
            GL11.glTexParameteri(3553, 10241, 9729);
        }
        bindCustomTextures(Shaders.customTexturesGbuffers);
        GlStateManager.setActiveTexture(33984);
        Shaders.previousCameraPositionX = Shaders.cameraPositionX;
        Shaders.previousCameraPositionY = Shaders.cameraPositionY;
        Shaders.previousCameraPositionZ = Shaders.cameraPositionZ;
        Shaders.previousProjection.position(0);
        Shaders.projection.position(0);
        Shaders.previousProjection.put(Shaders.projection);
        Shaders.previousProjection.position(0);
        Shaders.projection.position(0);
        Shaders.previousModelView.position(0);
        Shaders.modelView.position(0);
        Shaders.previousModelView.put(Shaders.modelView);
        Shaders.previousModelView.position(0);
        Shaders.modelView.position(0);
        checkGLError("beginRender");
        ShadersRender.renderShadowMap(Shaders.entityRenderer, 0, partialTicks, finishTimeNano);
        Shaders.mc.mcProfiler.endSection();
        EXTFramebufferObject.glBindFramebufferEXT(36160, Shaders.dfb);
        for (int i2 = 0; i2 < Shaders.usedColorBuffers; ++i2) {
            Shaders.colorTexturesToggle[i2] = 0;
            EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064 + i2, 3553, Shaders.dfbColorTexturesA[i2], 0);
        }
        checkGLError("end beginRender");
    }
    
    private static void checkWorldChanged(final World worldd) {
        if (Shaders.currentWorld != worldd) {
            final World world = Shaders.currentWorld;
            Shaders.currentWorld = worldd;
            if (world != null && worldd != null) {
                final int i = world.provider.getDimensionId();
                final int j = worldd.provider.getDimensionId();
                final boolean flag = Shaders.shaderPackDimensions.contains(i);
                final boolean flag2 = Shaders.shaderPackDimensions.contains(j);
                if (flag || flag2) {
                    uninit();
                }
            }
        }
    }
    
    public static void beginRenderPass(final int pass, final float partialTicks, final long finishTimeNano) {
        if (!Shaders.isShadowPass) {
            EXTFramebufferObject.glBindFramebufferEXT(36160, Shaders.dfb);
            GL11.glViewport(0, 0, Shaders.renderWidth, Shaders.renderHeight);
            Shaders.activeDrawBuffers = null;
            ShadersTex.bindNSTextures(Shaders.defaultTexture.getMultiTexID());
            useProgram(2);
            checkGLError("end beginRenderPass");
        }
    }
    
    public static void setViewport(final int vx, final int vy, final int vw, final int vh) {
        GlStateManager.colorMask(true, true, true, true);
        if (Shaders.isShadowPass) {
            GL11.glViewport(0, 0, Shaders.shadowMapWidth, Shaders.shadowMapHeight);
        }
        else {
            GL11.glViewport(0, 0, Shaders.renderWidth, Shaders.renderHeight);
            EXTFramebufferObject.glBindFramebufferEXT(36160, Shaders.dfb);
            Shaders.isRenderingDfb = true;
            GlStateManager.enableCull();
            GlStateManager.enableDepth();
            setDrawBuffers(Shaders.drawBuffersNone);
            useProgram(2);
            checkGLError("beginRenderPass");
        }
    }
    
    public static int setFogMode(final int val) {
        return Shaders.fogMode = val;
    }
    
    public static void setFogColor(final float r, final float g, final float b) {
        Shaders.fogColorR = r;
        Shaders.fogColorG = g;
        Shaders.fogColorB = b;
    }
    
    public static void setClearColor(final float red, final float green, final float blue, final float alpha) {
        GlStateManager.clearColor(red, green, blue, alpha);
        Shaders.clearColorR = red;
        Shaders.clearColorG = green;
        Shaders.clearColorB = blue;
    }
    
    public static void clearRenderBuffer() {
        if (Shaders.isShadowPass) {
            checkGLError("shadow clear pre");
            EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36096, 3553, Shaders.sfbDepthTextures.get(0), 0);
            GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            GL20.glDrawBuffers(Shaders.programsDrawBuffers[30]);
            checkFramebufferStatus("shadow clear");
            GL11.glClear(16640);
            checkGLError("shadow clear");
        }
        else {
            checkGLError("clear pre");
            if (Shaders.gbuffersClear[0]) {
                GL20.glDrawBuffers(36064);
                GL11.glClear(16384);
            }
            if (Shaders.gbuffersClear[1]) {
                GL20.glDrawBuffers(36065);
                GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glClear(16384);
            }
            for (int i = 2; i < Shaders.usedColorBuffers; ++i) {
                if (Shaders.gbuffersClear[i]) {
                    GL20.glDrawBuffers(36064 + i);
                    GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                    GL11.glClear(16384);
                }
            }
            setDrawBuffers(Shaders.dfbDrawBuffers);
            checkFramebufferStatus("clear");
            checkGLError("clear");
        }
    }
    
    public static void setCamera(final float partialTicks) {
        final Entity entity = Shaders.mc.getRenderViewEntity();
        final double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
        final double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
        final double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
        Shaders.cameraPositionX = d0;
        Shaders.cameraPositionY = d2;
        Shaders.cameraPositionZ = d3;
        GL11.glGetFloat(2983, (FloatBuffer)Shaders.projection.position(0));
        SMath.invertMat4FBFA((FloatBuffer)Shaders.projectionInverse.position(0), (FloatBuffer)Shaders.projection.position(0), Shaders.faProjectionInverse, Shaders.faProjection);
        Shaders.projection.position(0);
        Shaders.projectionInverse.position(0);
        GL11.glGetFloat(2982, (FloatBuffer)Shaders.modelView.position(0));
        SMath.invertMat4FBFA((FloatBuffer)Shaders.modelViewInverse.position(0), (FloatBuffer)Shaders.modelView.position(0), Shaders.faModelViewInverse, Shaders.faModelView);
        Shaders.modelView.position(0);
        Shaders.modelViewInverse.position(0);
        checkGLError("setCamera");
    }
    
    public static void setCameraShadow(final float partialTicks) {
        final Entity entity = Shaders.mc.getRenderViewEntity();
        final double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
        final double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
        final double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
        Shaders.cameraPositionX = d0;
        Shaders.cameraPositionY = d2;
        Shaders.cameraPositionZ = d3;
        GL11.glGetFloat(2983, (FloatBuffer)Shaders.projection.position(0));
        SMath.invertMat4FBFA((FloatBuffer)Shaders.projectionInverse.position(0), (FloatBuffer)Shaders.projection.position(0), Shaders.faProjectionInverse, Shaders.faProjection);
        Shaders.projection.position(0);
        Shaders.projectionInverse.position(0);
        GL11.glGetFloat(2982, (FloatBuffer)Shaders.modelView.position(0));
        SMath.invertMat4FBFA((FloatBuffer)Shaders.modelViewInverse.position(0), (FloatBuffer)Shaders.modelView.position(0), Shaders.faModelViewInverse, Shaders.faModelView);
        Shaders.modelView.position(0);
        Shaders.modelViewInverse.position(0);
        GL11.glViewport(0, 0, Shaders.shadowMapWidth, Shaders.shadowMapHeight);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        if (Shaders.shadowMapIsOrtho) {
            GL11.glOrtho(-Shaders.shadowMapHalfPlane, Shaders.shadowMapHalfPlane, -Shaders.shadowMapHalfPlane, Shaders.shadowMapHalfPlane, 0.05000000074505806, 256.0);
        }
        else {
            GLU.gluPerspective(Shaders.shadowMapFOV, Shaders.shadowMapWidth / (float)Shaders.shadowMapHeight, 0.05f, 256.0f);
        }
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -100.0f);
        GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
        Shaders.celestialAngle = Shaders.mc.theWorld.getCelestialAngle(partialTicks);
        Shaders.sunAngle = ((Shaders.celestialAngle < 0.75f) ? (Shaders.celestialAngle + 0.25f) : (Shaders.celestialAngle - 0.75f));
        final float f = Shaders.celestialAngle * -360.0f;
        final float f2 = (Shaders.shadowAngleInterval > 0.0f) ? (f % Shaders.shadowAngleInterval - Shaders.shadowAngleInterval * 0.5f) : 0.0f;
        if (Shaders.sunAngle <= 0.5) {
            GL11.glRotatef(f - f2, 0.0f, 0.0f, 1.0f);
            GL11.glRotatef(Shaders.sunPathRotation, 1.0f, 0.0f, 0.0f);
            Shaders.shadowAngle = Shaders.sunAngle;
        }
        else {
            GL11.glRotatef(f + 180.0f - f2, 0.0f, 0.0f, 1.0f);
            GL11.glRotatef(Shaders.sunPathRotation, 1.0f, 0.0f, 0.0f);
            Shaders.shadowAngle = Shaders.sunAngle - 0.5f;
        }
        if (Shaders.shadowMapIsOrtho) {
            final float f3 = Shaders.shadowIntervalSize;
            final float f4 = f3 / 2.0f;
            GL11.glTranslatef((float)d0 % f3 - f4, (float)d2 % f3 - f4, (float)d3 % f3 - f4);
        }
        final float f5 = Shaders.sunAngle * 6.2831855f;
        final float f6 = (float)Math.cos(f5);
        final float f7 = (float)Math.sin(f5);
        final float f8 = Shaders.sunPathRotation * 6.2831855f;
        float f9 = f6;
        float f10 = f7 * (float)Math.cos(f8);
        float f11 = f7 * (float)Math.sin(f8);
        if (Shaders.sunAngle > 0.5) {
            f9 = -f6;
            f10 = -f10;
            f11 = -f11;
        }
        Shaders.shadowLightPositionVector[0] = f9;
        Shaders.shadowLightPositionVector[1] = f10;
        Shaders.shadowLightPositionVector[2] = f11;
        Shaders.shadowLightPositionVector[3] = 0.0f;
        GL11.glGetFloat(2983, (FloatBuffer)Shaders.shadowProjection.position(0));
        SMath.invertMat4FBFA((FloatBuffer)Shaders.shadowProjectionInverse.position(0), (FloatBuffer)Shaders.shadowProjection.position(0), Shaders.faShadowProjectionInverse, Shaders.faShadowProjection);
        Shaders.shadowProjection.position(0);
        Shaders.shadowProjectionInverse.position(0);
        GL11.glGetFloat(2982, (FloatBuffer)Shaders.shadowModelView.position(0));
        SMath.invertMat4FBFA((FloatBuffer)Shaders.shadowModelViewInverse.position(0), (FloatBuffer)Shaders.shadowModelView.position(0), Shaders.faShadowModelViewInverse, Shaders.faShadowModelView);
        Shaders.shadowModelView.position(0);
        Shaders.shadowModelViewInverse.position(0);
        setProgramUniformMatrix4ARB("gbufferProjection", false, Shaders.projection);
        setProgramUniformMatrix4ARB("gbufferProjectionInverse", false, Shaders.projectionInverse);
        setProgramUniformMatrix4ARB("gbufferPreviousProjection", false, Shaders.previousProjection);
        setProgramUniformMatrix4ARB("gbufferModelView", false, Shaders.modelView);
        setProgramUniformMatrix4ARB("gbufferModelViewInverse", false, Shaders.modelViewInverse);
        setProgramUniformMatrix4ARB("gbufferPreviousModelView", false, Shaders.previousModelView);
        setProgramUniformMatrix4ARB("shadowProjection", false, Shaders.shadowProjection);
        setProgramUniformMatrix4ARB("shadowProjectionInverse", false, Shaders.shadowProjectionInverse);
        setProgramUniformMatrix4ARB("shadowModelView", false, Shaders.shadowModelView);
        setProgramUniformMatrix4ARB("shadowModelViewInverse", false, Shaders.shadowModelViewInverse);
        Shaders.mc.gameSettings.thirdPersonView = 1;
        checkGLError("setCamera");
    }
    
    public static void preCelestialRotate() {
        GL11.glRotatef(Shaders.sunPathRotation * 1.0f, 0.0f, 0.0f, 1.0f);
        checkGLError("preCelestialRotate");
    }
    
    public static void postCelestialRotate() {
        final FloatBuffer floatbuffer = Shaders.tempMatrixDirectBuffer;
        floatbuffer.clear();
        GL11.glGetFloat(2982, floatbuffer);
        floatbuffer.get(Shaders.tempMat, 0, 16);
        SMath.multiplyMat4xVec4(Shaders.sunPosition, Shaders.tempMat, Shaders.sunPosModelView);
        SMath.multiplyMat4xVec4(Shaders.moonPosition, Shaders.tempMat, Shaders.moonPosModelView);
        System.arraycopy((Shaders.shadowAngle == Shaders.sunAngle) ? Shaders.sunPosition : Shaders.moonPosition, 0, Shaders.shadowLightPosition, 0, 3);
        setProgramUniform3f("sunPosition", Shaders.sunPosition[0], Shaders.sunPosition[1], Shaders.sunPosition[2]);
        setProgramUniform3f("moonPosition", Shaders.moonPosition[0], Shaders.moonPosition[1], Shaders.moonPosition[2]);
        setProgramUniform3f("shadowLightPosition", Shaders.shadowLightPosition[0], Shaders.shadowLightPosition[1], Shaders.shadowLightPosition[2]);
        checkGLError("postCelestialRotate");
    }
    
    public static void setUpPosition() {
        final FloatBuffer floatbuffer = Shaders.tempMatrixDirectBuffer;
        floatbuffer.clear();
        GL11.glGetFloat(2982, floatbuffer);
        floatbuffer.get(Shaders.tempMat, 0, 16);
        SMath.multiplyMat4xVec4(Shaders.upPosition, Shaders.tempMat, Shaders.upPosModelView);
        setProgramUniform3f("upPosition", Shaders.upPosition[0], Shaders.upPosition[1], Shaders.upPosition[2]);
    }
    
    public static void genCompositeMipmap() {
        if (Shaders.hasGlGenMipmap) {
            for (int i = 0; i < Shaders.usedColorBuffers; ++i) {
                if ((Shaders.activeCompositeMipmapSetting & 1 << i) != 0x0) {
                    GlStateManager.setActiveTexture(33984 + Shaders.colorTextureTextureImageUnit[i]);
                    GL11.glTexParameteri(3553, 10241, 9987);
                    GL30.glGenerateMipmap(3553);
                }
            }
            GlStateManager.setActiveTexture(33984);
        }
    }
    
    public static void drawComposite() {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex3f(0.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3f(1.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex3f(1.0f, 1.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3f(0.0f, 1.0f, 0.0f);
        GL11.glEnd();
    }
    
    public static void renderCompositeFinal() {
        if (!Shaders.isShadowPass) {
            checkGLError("pre-renderCompositeFinal");
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glMatrixMode(5889);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0, 1.0, 0.0, 1.0, 0.0, 1.0);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableTexture2D();
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableDepth();
            GlStateManager.depthFunc(519);
            GlStateManager.depthMask(false);
            GlStateManager.disableLighting();
            if (Shaders.usedShadowDepthBuffers >= 1) {
                GlStateManager.setActiveTexture(33988);
                GlStateManager.bindTexture(Shaders.sfbDepthTextures.get(0));
                if (Shaders.usedShadowDepthBuffers >= 2) {
                    GlStateManager.setActiveTexture(33989);
                    GlStateManager.bindTexture(Shaders.sfbDepthTextures.get(1));
                }
            }
            for (int i = 0; i < Shaders.usedColorBuffers; ++i) {
                GlStateManager.setActiveTexture(33984 + Shaders.colorTextureTextureImageUnit[i]);
                GlStateManager.bindTexture(Shaders.dfbColorTexturesA[i]);
            }
            GlStateManager.setActiveTexture(33990);
            GlStateManager.bindTexture(Shaders.dfbDepthTextures.get(0));
            if (Shaders.usedDepthBuffers >= 2) {
                GlStateManager.setActiveTexture(33995);
                GlStateManager.bindTexture(Shaders.dfbDepthTextures.get(1));
                if (Shaders.usedDepthBuffers >= 3) {
                    GlStateManager.setActiveTexture(33996);
                    GlStateManager.bindTexture(Shaders.dfbDepthTextures.get(2));
                }
            }
            for (int j1 = 0; j1 < Shaders.usedShadowColorBuffers; ++j1) {
                GlStateManager.setActiveTexture(33997 + j1);
                GlStateManager.bindTexture(Shaders.sfbColorTextures.get(j1));
            }
            if (Shaders.noiseTextureEnabled) {
                GlStateManager.setActiveTexture(33984 + Shaders.noiseTexture.textureUnit);
                GlStateManager.bindTexture(Shaders.noiseTexture.getID());
                GL11.glTexParameteri(3553, 10242, 10497);
                GL11.glTexParameteri(3553, 10243, 10497);
                GL11.glTexParameteri(3553, 10240, 9729);
                GL11.glTexParameteri(3553, 10241, 9729);
            }
            bindCustomTextures(Shaders.customTexturesComposite);
            GlStateManager.setActiveTexture(33984);
            final boolean flag = true;
            for (int k = 0; k < Shaders.usedColorBuffers; ++k) {
                EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064 + k, 3553, Shaders.dfbColorTexturesA[8 + k], 0);
            }
            EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36096, 3553, Shaders.dfbDepthTextures.get(0), 0);
            GL20.glDrawBuffers(Shaders.dfbDrawBuffers);
            checkGLError("pre-composite");
            for (int k2 = 0; k2 < 8; ++k2) {
                if (Shaders.programsID[21 + k2] != 0) {
                    useProgram(21 + k2);
                    checkGLError(Shaders.programNames[21 + k2]);
                    if (Shaders.activeCompositeMipmapSetting != 0) {
                        genCompositeMipmap();
                    }
                    drawComposite();
                    for (int l = 0; l < Shaders.usedColorBuffers; ++l) {
                        if (Shaders.programsToggleColorTextures[21 + k2][l]) {
                            final int m = Shaders.colorTexturesToggle[l];
                            final int[] colorTexturesToggle = Shaders.colorTexturesToggle;
                            final int n = l;
                            final int n2 = 8 - m;
                            colorTexturesToggle[n] = n2;
                            final int i2 = n2;
                            GlStateManager.setActiveTexture(33984 + Shaders.colorTextureTextureImageUnit[l]);
                            GlStateManager.bindTexture(Shaders.dfbColorTexturesA[i2 + l]);
                            EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064 + l, 3553, Shaders.dfbColorTexturesA[m + l], 0);
                        }
                    }
                    GlStateManager.setActiveTexture(33984);
                }
            }
            checkGLError("composite");
            Shaders.isRenderingDfb = false;
            Shaders.mc.getFramebuffer().bindFramebuffer(true);
            OpenGlHelper.glFramebufferTexture2D(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_COLOR_ATTACHMENT0, 3553, Shaders.mc.getFramebuffer().framebufferTexture, 0);
            GL11.glViewport(0, 0, Minecraft.displayWidth, Minecraft.displayHeight);
            if (EntityRenderer.anaglyphEnable) {
                final boolean flag2 = EntityRenderer.anaglyphField != 0;
                GlStateManager.colorMask(flag2, !flag2, !flag2, true);
            }
            GlStateManager.depthMask(true);
            GL11.glClearColor(Shaders.clearColorR, Shaders.clearColorG, Shaders.clearColorB, 1.0f);
            GL11.glClear(16640);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableTexture2D();
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableDepth();
            GlStateManager.depthFunc(519);
            GlStateManager.depthMask(false);
            checkGLError("pre-final");
            useProgram(29);
            checkGLError("final");
            if (Shaders.activeCompositeMipmapSetting != 0) {
                genCompositeMipmap();
            }
            drawComposite();
            checkGLError("renderCompositeFinal");
            Shaders.isCompositeRendered = true;
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
        if (Shaders.isShadowPass) {
            checkGLError("shadow endRender");
        }
        else {
            if (!Shaders.isCompositeRendered) {
                renderCompositeFinal();
            }
            Shaders.isRenderingWorld = false;
            GlStateManager.colorMask(true, true, true, true);
            useProgram(0);
            RenderHelper.disableStandardItemLighting();
            checkGLError("endRender end");
        }
    }
    
    public static void beginSky() {
        Shaders.isRenderingSky = true;
        Shaders.fogEnabled = true;
        setDrawBuffers(Shaders.dfbDrawBuffers);
        useProgram(5);
        pushEntity(-2, 0);
    }
    
    public static void setSkyColor(final Vec3 v3color) {
        Shaders.skyColorR = (float)v3color.xCoord;
        Shaders.skyColorG = (float)v3color.yCoord;
        Shaders.skyColorB = (float)v3color.zCoord;
        setProgramUniform3f("skyColor", Shaders.skyColorR, Shaders.skyColorG, Shaders.skyColorB);
    }
    
    public static void drawHorizon() {
        final WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();
        final float f = (float)(Shaders.mc.gameSettings.renderDistanceChunks * 16);
        final double d0 = f * 0.9238;
        final double d2 = f * 0.3826;
        final double d3 = -d2;
        final double d4 = -d0;
        final double d5 = 16.0;
        final double d6 = -Shaders.cameraPositionY;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(d3, d6, d4).endVertex();
        worldrenderer.pos(d3, d5, d4).endVertex();
        worldrenderer.pos(d4, d5, d3).endVertex();
        worldrenderer.pos(d4, d6, d3).endVertex();
        worldrenderer.pos(d4, d6, d3).endVertex();
        worldrenderer.pos(d4, d5, d3).endVertex();
        worldrenderer.pos(d4, d5, d2).endVertex();
        worldrenderer.pos(d4, d6, d2).endVertex();
        worldrenderer.pos(d4, d6, d2).endVertex();
        worldrenderer.pos(d4, d5, d2).endVertex();
        worldrenderer.pos(d3, d5, d2).endVertex();
        worldrenderer.pos(d3, d6, d2).endVertex();
        worldrenderer.pos(d3, d6, d2).endVertex();
        worldrenderer.pos(d3, d5, d2).endVertex();
        worldrenderer.pos(d2, d5, d0).endVertex();
        worldrenderer.pos(d2, d6, d0).endVertex();
        worldrenderer.pos(d2, d6, d0).endVertex();
        worldrenderer.pos(d2, d5, d0).endVertex();
        worldrenderer.pos(d0, d5, d2).endVertex();
        worldrenderer.pos(d0, d6, d2).endVertex();
        worldrenderer.pos(d0, d6, d2).endVertex();
        worldrenderer.pos(d0, d5, d2).endVertex();
        worldrenderer.pos(d0, d5, d3).endVertex();
        worldrenderer.pos(d0, d6, d3).endVertex();
        worldrenderer.pos(d0, d6, d3).endVertex();
        worldrenderer.pos(d0, d5, d3).endVertex();
        worldrenderer.pos(d2, d5, d4).endVertex();
        worldrenderer.pos(d2, d6, d4).endVertex();
        worldrenderer.pos(d2, d6, d4).endVertex();
        worldrenderer.pos(d2, d5, d4).endVertex();
        worldrenderer.pos(d3, d5, d4).endVertex();
        worldrenderer.pos(d3, d6, d4).endVertex();
        Tessellator.getInstance().draw();
    }
    
    public static void preSkyList() {
        setUpPosition();
        GL11.glColor3f(Shaders.fogColorR, Shaders.fogColorG, Shaders.fogColorB);
        drawHorizon();
        GL11.glColor3f(Shaders.skyColorR, Shaders.skyColorG, Shaders.skyColorB);
    }
    
    public static void endSky() {
        Shaders.isRenderingSky = false;
        setDrawBuffers(Shaders.dfbDrawBuffers);
        useProgram(Shaders.lightmapEnabled ? 3 : 2);
        popEntity();
    }
    
    public static void beginUpdateChunks() {
        checkGLError("beginUpdateChunks1");
        checkFramebufferStatus("beginUpdateChunks1");
        if (!Shaders.isShadowPass) {
            useProgram(7);
        }
        checkGLError("beginUpdateChunks2");
        checkFramebufferStatus("beginUpdateChunks2");
    }
    
    public static void endUpdateChunks() {
        checkGLError("endUpdateChunks1");
        checkFramebufferStatus("endUpdateChunks1");
        if (!Shaders.isShadowPass) {
            useProgram(7);
        }
        checkGLError("endUpdateChunks2");
        checkFramebufferStatus("endUpdateChunks2");
    }
    
    public static boolean shouldRenderClouds(final GameSettings gs) {
        if (!Shaders.shaderPackLoaded) {
            return true;
        }
        checkGLError("shouldRenderClouds");
        return Shaders.isShadowPass ? Shaders.configCloudShadow : (gs.clouds > 0);
    }
    
    public static void beginClouds() {
        Shaders.fogEnabled = true;
        pushEntity(-3, 0);
        useProgram(6);
    }
    
    public static void endClouds() {
        disableFog();
        popEntity();
        useProgram(Shaders.lightmapEnabled ? 3 : 2);
    }
    
    public static void beginEntities() {
        if (Shaders.isRenderingWorld) {
            useProgram(16);
            resetDisplayListModels();
        }
    }
    
    public static void nextEntity(final Entity entity) {
        if (Shaders.isRenderingWorld) {
            useProgram(16);
            setEntityId(entity);
        }
    }
    
    public static void setEntityId(final Entity entity) {
        if (Shaders.isRenderingWorld && !Shaders.isShadowPass && Shaders.uniformEntityId.isDefined()) {
            final int i = EntityList.getEntityID(entity);
            Shaders.uniformEntityId.setValue(i);
        }
    }
    
    public static void beginSpiderEyes() {
        if (Shaders.isRenderingWorld && Shaders.programsID[18] != Shaders.programsID[0]) {
            useProgram(18);
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.0f);
            GlStateManager.blendFunc(770, 771);
        }
    }
    
    public static void endEntities() {
        if (Shaders.isRenderingWorld) {
            useProgram(Shaders.lightmapEnabled ? 3 : 2);
        }
    }
    
    public static void setEntityColor(final float r, final float g, final float b, final float a) {
        if (Shaders.isRenderingWorld && !Shaders.isShadowPass) {
            Shaders.uniformEntityColor.setValue(r, g, b, a);
        }
    }
    
    public static void beginLivingDamage() {
        if (Shaders.isRenderingWorld) {
            ShadersTex.bindTexture(Shaders.defaultTexture);
            if (!Shaders.isShadowPass) {
                setDrawBuffers(Shaders.drawBuffersColorAtt0);
            }
        }
    }
    
    public static void endLivingDamage() {
        if (Shaders.isRenderingWorld && !Shaders.isShadowPass) {
            setDrawBuffers(Shaders.programsDrawBuffers[16]);
        }
    }
    
    public static void beginBlockEntities() {
        if (Shaders.isRenderingWorld) {
            checkGLError("beginBlockEntities");
            useProgram(13);
        }
    }
    
    public static void nextBlockEntity(final TileEntity tileEntity) {
        if (Shaders.isRenderingWorld) {
            checkGLError("nextBlockEntity");
            useProgram(13);
            setBlockEntityId(tileEntity);
        }
    }
    
    public static void setBlockEntityId(final TileEntity tileEntity) {
        if (Shaders.isRenderingWorld && !Shaders.isShadowPass && Shaders.uniformBlockEntityId.isDefined()) {
            final Block block = tileEntity.getBlockType();
            final int i = Block.getIdFromBlock(block);
            Shaders.uniformBlockEntityId.setValue(i);
        }
    }
    
    public static void endBlockEntities() {
        if (Shaders.isRenderingWorld) {
            checkGLError("endBlockEntities");
            useProgram(Shaders.lightmapEnabled ? 3 : 2);
            ShadersTex.bindNSTextures(Shaders.defaultTexture.getMultiTexID());
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
        if (!Shaders.isShadowPass && Shaders.centerDepthSmoothEnabled) {
            Shaders.tempDirectFloatBuffer.clear();
            GL11.glReadPixels(Shaders.renderWidth / 2, Shaders.renderHeight / 2, 1, 1, 6402, 5126, Shaders.tempDirectFloatBuffer);
            Shaders.centerDepth = Shaders.tempDirectFloatBuffer.get(0);
            final float f = Shaders.diffSystemTime * 0.01f;
            final float f2 = (float)Math.exp(Math.log(0.5) * f / Shaders.centerDepthSmoothHalflife);
            Shaders.centerDepthSmooth = Shaders.centerDepthSmooth * f2 + Shaders.centerDepth * (1.0f - f2);
        }
    }
    
    public static void beginWeather() {
        if (!Shaders.isShadowPass) {
            if (Shaders.usedDepthBuffers >= 3) {
                GlStateManager.setActiveTexture(33996);
                GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, Shaders.renderWidth, Shaders.renderHeight);
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
        if (Shaders.usedDepthBuffers >= 2) {
            GlStateManager.setActiveTexture(33995);
            checkGLError("pre copy depth");
            GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, Shaders.renderWidth, Shaders.renderHeight);
            checkGLError("copy depth");
            GlStateManager.setActiveTexture(33984);
        }
        ShadersTex.bindNSTextures(Shaders.defaultTexture.getMultiTexID());
    }
    
    public static void beginWater() {
        if (Shaders.isRenderingWorld) {
            if (!Shaders.isShadowPass) {
                useProgram(12);
                GlStateManager.enableBlend();
                GlStateManager.depthMask(true);
            }
            else {
                GlStateManager.depthMask(true);
            }
        }
    }
    
    public static void endWater() {
        if (Shaders.isRenderingWorld) {
            if (Shaders.isShadowPass) {}
            useProgram(Shaders.lightmapEnabled ? 3 : 2);
        }
    }
    
    public static void beginProjectRedHalo() {
        if (Shaders.isRenderingWorld) {
            useProgram(1);
        }
    }
    
    public static void endProjectRedHalo() {
        if (Shaders.isRenderingWorld) {
            useProgram(3);
        }
    }
    
    public static void applyHandDepth() {
        if (Shaders.configHandDepthMul != 1.0) {
            GL11.glScaled(1.0, 1.0, Shaders.configHandDepthMul);
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
    
    public static void glEnableWrapper(final int cap) {
        GL11.glEnable(cap);
        if (cap == 3553) {
            enableTexture2D();
        }
        else if (cap == 2912) {
            enableFog();
        }
    }
    
    public static void glDisableWrapper(final int cap) {
        GL11.glDisable(cap);
        if (cap == 3553) {
            disableTexture2D();
        }
        else if (cap == 2912) {
            disableFog();
        }
    }
    
    public static void sglEnableT2D(final int cap) {
        GL11.glEnable(cap);
        enableTexture2D();
    }
    
    public static void sglDisableT2D(final int cap) {
        GL11.glDisable(cap);
        disableTexture2D();
    }
    
    public static void sglEnableFog(final int cap) {
        GL11.glEnable(cap);
        enableFog();
    }
    
    public static void sglDisableFog(final int cap) {
        GL11.glDisable(cap);
        disableFog();
    }
    
    public static void enableTexture2D() {
        if (Shaders.isRenderingSky) {
            useProgram(5);
        }
        else if (Shaders.activeProgram == 1) {
            useProgram(Shaders.lightmapEnabled ? 3 : 2);
        }
    }
    
    public static void disableTexture2D() {
        if (Shaders.isRenderingSky) {
            useProgram(4);
        }
        else if (Shaders.activeProgram == 2 || Shaders.activeProgram == 3) {
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
        Shaders.fogEnabled = true;
        setProgramUniform1i("fogMode", Shaders.fogMode);
    }
    
    public static void disableFog() {
        Shaders.fogEnabled = false;
        setProgramUniform1i("fogMode", 0);
    }
    
    public static void setFog(final int fogMode) {
        GlStateManager.setFog(fogMode);
        Shaders.fogMode = fogMode;
        if (Shaders.fogEnabled) {
            setProgramUniform1i("fogMode", fogMode);
        }
    }
    
    public static void sglFogi(final int pname, final int param) {
        GL11.glFogi(pname, param);
        if (pname == 2917) {
            Shaders.fogMode = param;
            if (Shaders.fogEnabled) {
                setProgramUniform1i("fogMode", Shaders.fogMode);
            }
        }
    }
    
    public static void enableLightmap() {
        Shaders.lightmapEnabled = true;
        if (Shaders.activeProgram == 2) {
            useProgram(3);
        }
    }
    
    public static void disableLightmap() {
        Shaders.lightmapEnabled = false;
        if (Shaders.activeProgram == 3) {
            useProgram(2);
        }
    }
    
    public static int getEntityData() {
        return Shaders.entityData[Shaders.entityDataIndex * 2];
    }
    
    public static int getEntityData2() {
        return Shaders.entityData[Shaders.entityDataIndex * 2 + 1];
    }
    
    public static int setEntityData1(final int data1) {
        Shaders.entityData[Shaders.entityDataIndex * 2] = ((Shaders.entityData[Shaders.entityDataIndex * 2] & 0xFFFF) | data1 << 16);
        return data1;
    }
    
    public static int setEntityData2(final int data2) {
        Shaders.entityData[Shaders.entityDataIndex * 2 + 1] = ((Shaders.entityData[Shaders.entityDataIndex * 2 + 1] & 0xFFFF0000) | (data2 & 0xFFFF));
        return data2;
    }
    
    public static void pushEntity(final int data0, final int data1) {
        ++Shaders.entityDataIndex;
        Shaders.entityData[Shaders.entityDataIndex * 2] = ((data0 & 0xFFFF) | data1 << 16);
        Shaders.entityData[Shaders.entityDataIndex * 2 + 1] = 0;
    }
    
    public static void pushEntity(final int data0) {
        ++Shaders.entityDataIndex;
        Shaders.entityData[Shaders.entityDataIndex * 2] = (data0 & 0xFFFF);
        Shaders.entityData[Shaders.entityDataIndex * 2 + 1] = 0;
    }
    
    public static void pushEntity(final Block block) {
        ++Shaders.entityDataIndex;
        Shaders.entityData[Shaders.entityDataIndex * 2] = ((Block.blockRegistry.getIDForObject(block) & 0xFFFF) | block.getRenderType() << 16);
        Shaders.entityData[Shaders.entityDataIndex * 2 + 1] = 0;
    }
    
    public static void popEntity() {
        Shaders.entityData[Shaders.entityDataIndex * 2] = 0;
        Shaders.entityData[Shaders.entityDataIndex * 2 + 1] = 0;
        --Shaders.entityDataIndex;
    }
    
    public static void mcProfilerEndSection() {
        Shaders.mc.mcProfiler.endSection();
    }
    
    public static String getShaderPackName() {
        return (Shaders.shaderPack == null) ? null : ((Shaders.shaderPack instanceof ShaderPackNone) ? null : Shaders.shaderPack.getName());
    }
    
    public static InputStream getShaderPackResourceStream(final String path) {
        return (Shaders.shaderPack == null) ? null : Shaders.shaderPack.getResourceAsStream(path);
    }
    
    public static void nextAntialiasingLevel() {
        Shaders.configAntialiasingLevel += 2;
        Shaders.configAntialiasingLevel = Shaders.configAntialiasingLevel / 2 * 2;
        if (Shaders.configAntialiasingLevel > 4) {
            Shaders.configAntialiasingLevel = 0;
        }
        Shaders.configAntialiasingLevel = Config.limit(Shaders.configAntialiasingLevel, 0, 4);
    }
    
    public static void checkShadersModInstalled() {
        try {
            Class.forName("shadersmod.transform.SMCClassTransformer");
        }
        catch (Throwable var1) {
            return;
        }
        throw new RuntimeException("Shaders Mod detected. Please remove it, OptiFine has built-in support for shaders.");
    }
    
    public static void resourcesReloaded() {
        loadShaderPackResources();
    }
    
    private static void loadShaderPackResources() {
        Shaders.shaderPackResources = new HashMap<String, String>();
        if (Shaders.shaderPackLoaded) {
            final List<String> list = new ArrayList<String>();
            final String s = "/shaders/lang/";
            final String s2 = "en_US";
            final String s3 = ".lang";
            list.add(String.valueOf(s) + s2 + s3);
            if (!Config.getGameSettings().language.equals(s2)) {
                list.add(String.valueOf(s) + Config.getGameSettings().language + s3);
            }
            try {
                for (final String s4 : list) {
                    final InputStream inputstream = Shaders.shaderPack.getResourceAsStream(s4);
                    if (inputstream != null) {
                        final Properties properties = new Properties();
                        Lang.loadLocaleData(inputstream, properties);
                        inputstream.close();
                        for (final Object s5 : properties.keySet()) {
                            final String s6 = properties.getProperty((String)s5);
                            Shaders.shaderPackResources.put((String)s5, s6);
                        }
                    }
                }
            }
            catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        }
    }
    
    public static String translate(final String key, final String def) {
        final String s = Shaders.shaderPackResources.get(key);
        return (s == null) ? def : s;
    }
    
    public static boolean isProgramPath(String program) {
        if (program == null) {
            return false;
        }
        if (program.length() <= 0) {
            return false;
        }
        final int i = program.lastIndexOf("/");
        if (i >= 0) {
            program = program.substring(i + 1);
        }
        return Arrays.asList(Shaders.programNames).contains(program);
    }
    
    public static void setItemToRenderMain(final ItemStack itemToRenderMain) {
        Shaders.itemToRenderMainTranslucent = isTranslucentBlock(itemToRenderMain);
    }
    
    public static boolean isItemToRenderMainTranslucent() {
        return Shaders.itemToRenderMainTranslucent;
    }
    
    private static boolean isTranslucentBlock(final ItemStack stack) {
        if (stack == null) {
            return false;
        }
        final Item item = stack.getItem();
        if (item == null) {
            return false;
        }
        if (!(item instanceof ItemBlock)) {
            return false;
        }
        final ItemBlock itemblock = (ItemBlock)item;
        final Block block = itemblock.getBlock();
        if (block == null) {
            return false;
        }
        final EnumWorldBlockLayer enumworldblocklayer = block.getBlockLayer();
        return enumworldblocklayer == EnumWorldBlockLayer.TRANSLUCENT;
    }
    
    public static boolean isRenderBothHands() {
        return false;
    }
    
    public static boolean isHandRenderedMain() {
        return Shaders.isHandRenderedMain;
    }
    
    public static void setHandRenderedMain(final boolean isHandRenderedMain) {
        Shaders.isHandRenderedMain = isHandRenderedMain;
    }
    
    public static float getShadowRenderDistance() {
        return (Shaders.shadowDistanceRenderMul < 0.0f) ? -1.0f : (Shaders.shadowMapHalfPlane * Shaders.shadowDistanceRenderMul);
    }
}
