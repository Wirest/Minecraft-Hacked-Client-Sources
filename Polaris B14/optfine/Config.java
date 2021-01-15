/*      */ package optfine;
/*      */ 
/*      */ import java.awt.Dimension;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.FileDescriptor;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Method;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.Date;
/*      */ import java.util.List;
/*      */ import java.util.Properties;
/*      */ import java.util.StringTokenizer;
/*      */ import javax.imageio.ImageIO;
/*      */ import net.minecraft.client.LoadingScreenRenderer;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.gui.FontRenderer;
/*      */ import net.minecraft.client.gui.ScaledResolution;
/*      */ import net.minecraft.client.renderer.EntityRenderer;
/*      */ import net.minecraft.client.renderer.GlStateManager;
/*      */ import net.minecraft.client.renderer.RenderGlobal;
/*      */ import net.minecraft.client.resources.IResource;
/*      */ import net.minecraft.client.resources.IResourceManager;
/*      */ import net.minecraft.client.resources.IResourcePack;
/*      */ import net.minecraft.client.resources.ResourcePackRepository;
/*      */ import net.minecraft.client.resources.ResourcePackRepository.Entry;
/*      */ import net.minecraft.client.settings.GameSettings;
/*      */ import net.minecraft.client.shader.Framebuffer;
/*      */ import net.minecraft.server.integrated.IntegratedServer;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldProvider;
/*      */ import net.minecraft.world.WorldServer;
/*      */ import org.lwjgl.LWJGLException;
/*      */ import org.lwjgl.Sys;
/*      */ import org.lwjgl.input.Mouse;
/*      */ import org.lwjgl.opengl.ContextCapabilities;
/*      */ import org.lwjgl.opengl.Display;
/*      */ import org.lwjgl.opengl.DisplayMode;
/*      */ import org.lwjgl.opengl.GL11;
/*      */ import org.lwjgl.opengl.GLContext;
/*      */ import org.lwjgl.opengl.PixelFormat;
/*      */ import org.lwjgl.util.glu.GLU;
/*      */ 
/*      */ public class Config
/*      */ {
/*      */   public static final String OF_NAME = "OptiFine";
/*      */   public static final String MC_VERSION = "1.8.8";
/*      */   public static final String OF_EDITION = "HD_U";
/*      */   public static final String OF_RELEASE = "E1";
/*      */   public static final String VERSION = "OptiFine_1.8.8_HD_U_E1";
/*   63 */   private static String newRelease = null;
/*   64 */   private static boolean notify64BitJava = false;
/*   65 */   public static String openGlVersion = null;
/*   66 */   public static String openGlRenderer = null;
/*   67 */   public static String openGlVendor = null;
/*   68 */   public static boolean fancyFogAvailable = false;
/*   69 */   public static boolean occlusionAvailable = false;
/*   70 */   private static GameSettings gameSettings = null;
/*   71 */   private static Minecraft minecraft = null;
/*   72 */   private static boolean initialized = false;
/*   73 */   private static Thread minecraftThread = null;
/*   74 */   private static DisplayMode desktopDisplayMode = null;
/*   75 */   private static int antialiasingLevel = 0;
/*   76 */   private static int availableProcessors = 0;
/*   77 */   public static boolean zoomMode = false;
/*   78 */   private static int texturePackClouds = 0;
/*   79 */   public static boolean waterOpacityChanged = false;
/*   80 */   private static boolean fullscreenModeChecked = false;
/*   81 */   private static boolean desktopModeChecked = false;
/*   82 */   private static PrintStream systemOut = new PrintStream(new FileOutputStream(FileDescriptor.out));
/*   83 */   public static final Boolean DEF_FOG_FANCY = Boolean.valueOf(true);
/*   84 */   public static final Float DEF_FOG_START = Float.valueOf(0.2F);
/*   85 */   public static final Boolean DEF_OPTIMIZE_RENDER_DISTANCE = Boolean.valueOf(false);
/*   86 */   public static final Boolean DEF_OCCLUSION_ENABLED = Boolean.valueOf(false);
/*   87 */   public static final Integer DEF_MIPMAP_LEVEL = Integer.valueOf(0);
/*   88 */   public static final Integer DEF_MIPMAP_TYPE = Integer.valueOf(9984);
/*   89 */   public static final Float DEF_ALPHA_FUNC_LEVEL = Float.valueOf(0.1F);
/*   90 */   public static final Boolean DEF_LOAD_CHUNKS_FAR = Boolean.valueOf(false);
/*   91 */   public static final Integer DEF_PRELOADED_CHUNKS = Integer.valueOf(0);
/*   92 */   public static final Integer DEF_CHUNKS_LIMIT = Integer.valueOf(25);
/*   93 */   public static final Integer DEF_UPDATES_PER_FRAME = Integer.valueOf(3);
/*   94 */   public static final Boolean DEF_DYNAMIC_UPDATES = Boolean.valueOf(false);
/*   95 */   private static long lastActionTime = System.currentTimeMillis();
/*      */   
/*      */   public static String getVersion()
/*      */   {
/*   99 */     return "OptiFine_1.8.8_HD_U_E1";
/*      */   }
/*      */   
/*      */   public static void initGameSettings(GameSettings p_initGameSettings_0_)
/*      */   {
/*  104 */     gameSettings = p_initGameSettings_0_;
/*  105 */     minecraft = Minecraft.getMinecraft();
/*  106 */     desktopDisplayMode = Display.getDesktopDisplayMode();
/*  107 */     updateAvailableProcessors();
/*      */   }
/*      */   
/*      */   public static void initDisplay()
/*      */   {
/*  112 */     checkInitialized();
/*  113 */     antialiasingLevel = gameSettings.ofAaLevel;
/*  114 */     checkDisplaySettings();
/*  115 */     checkDisplayMode();
/*  116 */     minecraftThread = Thread.currentThread();
/*  117 */     updateThreadPriorities();
/*      */   }
/*      */   
/*      */   public static void checkInitialized()
/*      */   {
/*  122 */     if (!initialized)
/*      */     {
/*  124 */       if (Display.isCreated())
/*      */       {
/*  126 */         initialized = true;
/*  127 */         checkOpenGlCaps();
/*  128 */         startVersionCheckThread();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static void checkOpenGlCaps()
/*      */   {
/*  135 */     log("");
/*  136 */     log(getVersion());
/*  137 */     log(new Date());
/*  138 */     log("OS: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version"));
/*  139 */     log("Java: " + System.getProperty("java.version") + ", " + System.getProperty("java.vendor"));
/*  140 */     log("VM: " + System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor"));
/*  141 */     log("LWJGL: " + Sys.getVersion());
/*  142 */     openGlVersion = GL11.glGetString(7938);
/*  143 */     openGlRenderer = GL11.glGetString(7937);
/*  144 */     openGlVendor = GL11.glGetString(7936);
/*  145 */     log("OpenGL: " + openGlRenderer + ", version " + openGlVersion + ", " + openGlVendor);
/*  146 */     log("OpenGL Version: " + getOpenGlVersionString());
/*      */     
/*  148 */     if (!GLContext.getCapabilities().OpenGL12)
/*      */     {
/*  150 */       log("OpenGL Mipmap levels: Not available (GL12.GL_TEXTURE_MAX_LEVEL)");
/*      */     }
/*      */     
/*  153 */     fancyFogAvailable = GLContext.getCapabilities().GL_NV_fog_distance;
/*      */     
/*  155 */     if (!fancyFogAvailable)
/*      */     {
/*  157 */       log("OpenGL Fancy fog: Not available (GL_NV_fog_distance)");
/*      */     }
/*      */     
/*  160 */     occlusionAvailable = GLContext.getCapabilities().GL_ARB_occlusion_query;
/*      */     
/*  162 */     if (!occlusionAvailable)
/*      */     {
/*  164 */       log("OpenGL Occlussion culling: Not available (GL_ARB_occlusion_query)");
/*      */     }
/*      */     
/*  167 */     int i = Minecraft.getGLMaximumTextureSize();
/*  168 */     dbg("Maximum texture size: " + i + "x" + i);
/*      */   }
/*      */   
/*      */   public static boolean isFancyFogAvailable()
/*      */   {
/*  173 */     return fancyFogAvailable;
/*      */   }
/*      */   
/*      */   public static boolean isOcclusionAvailable()
/*      */   {
/*  178 */     return occlusionAvailable;
/*      */   }
/*      */   
/*      */   public static String getOpenGlVersionString()
/*      */   {
/*  183 */     int i = getOpenGlVersion();
/*  184 */     String s = i / 10 + "." + i % 10;
/*  185 */     return s;
/*      */   }
/*      */   
/*      */   private static int getOpenGlVersion()
/*      */   {
/*  190 */     return !GLContext.getCapabilities().OpenGL40 ? 33 : !GLContext.getCapabilities().OpenGL33 ? 32 : !GLContext.getCapabilities().OpenGL32 ? 31 : !GLContext.getCapabilities().OpenGL31 ? 30 : !GLContext.getCapabilities().OpenGL30 ? 21 : !GLContext.getCapabilities().OpenGL21 ? 20 : !GLContext.getCapabilities().OpenGL20 ? 15 : !GLContext.getCapabilities().OpenGL15 ? 14 : !GLContext.getCapabilities().OpenGL14 ? 13 : !GLContext.getCapabilities().OpenGL13 ? 12 : !GLContext.getCapabilities().OpenGL12 ? 11 : !GLContext.getCapabilities().OpenGL11 ? 10 : 40;
/*      */   }
/*      */   
/*      */   public static void updateThreadPriorities()
/*      */   {
/*  195 */     updateAvailableProcessors();
/*  196 */     int i = 8;
/*      */     
/*  198 */     if (isSingleProcessor())
/*      */     {
/*  200 */       if (isSmoothWorld())
/*      */       {
/*  202 */         minecraftThread.setPriority(10);
/*  203 */         setThreadPriority("Server thread", 1);
/*      */       }
/*      */       else
/*      */       {
/*  207 */         minecraftThread.setPriority(5);
/*  208 */         setThreadPriority("Server thread", 5);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  213 */       minecraftThread.setPriority(10);
/*  214 */       setThreadPriority("Server thread", 5);
/*      */     }
/*      */   }
/*      */   
/*      */   private static void setThreadPriority(String p_setThreadPriority_0_, int p_setThreadPriority_1_)
/*      */   {
/*      */     try
/*      */     {
/*  222 */       ThreadGroup threadgroup = Thread.currentThread().getThreadGroup();
/*      */       
/*  224 */       if (threadgroup == null)
/*      */       {
/*  226 */         return;
/*      */       }
/*      */       
/*  229 */       int i = (threadgroup.activeCount() + 10) * 2;
/*  230 */       Thread[] athread = new Thread[i];
/*  231 */       threadgroup.enumerate(athread, false);
/*      */       
/*  233 */       for (int j = 0; j < athread.length; j++)
/*      */       {
/*  235 */         Thread thread = athread[j];
/*      */         
/*  237 */         if ((thread != null) && (thread.getName().startsWith(p_setThreadPriority_0_)))
/*      */         {
/*  239 */           thread.setPriority(p_setThreadPriority_1_);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Throwable throwable)
/*      */     {
/*  245 */       dbg(throwable.getClass().getName() + ": " + throwable.getMessage());
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean isMinecraftThread()
/*      */   {
/*  251 */     return Thread.currentThread() == minecraftThread;
/*      */   }
/*      */   
/*      */   private static void startVersionCheckThread()
/*      */   {
/*  256 */     VersionCheckThread versioncheckthread = new VersionCheckThread();
/*  257 */     versioncheckthread.start();
/*      */   }
/*      */   
/*      */   public static int getMipmapType()
/*      */   {
/*  262 */     if (gameSettings == null)
/*      */     {
/*  264 */       return DEF_MIPMAP_TYPE.intValue();
/*      */     }
/*      */     
/*      */ 
/*  268 */     switch (gameSettings.ofMipmapType)
/*      */     {
/*      */     case 0: 
/*  271 */       return 9986;
/*      */     
/*      */     case 1: 
/*  274 */       return 9986;
/*      */     
/*      */     case 2: 
/*  277 */       if (isMultiTexture())
/*      */       {
/*  279 */         return 9985;
/*      */       }
/*      */       
/*  282 */       return 9986;
/*      */     
/*      */     case 3: 
/*  285 */       if (isMultiTexture())
/*      */       {
/*  287 */         return 9987;
/*      */       }
/*      */       
/*  290 */       return 9986;
/*      */     }
/*      */     
/*  293 */     return 9986;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static boolean isUseAlphaFunc()
/*      */   {
/*  300 */     float f = getAlphaFuncLevel();
/*  301 */     return f > DEF_ALPHA_FUNC_LEVEL.floatValue() + 1.0E-5F;
/*      */   }
/*      */   
/*      */   public static float getAlphaFuncLevel()
/*      */   {
/*  306 */     return DEF_ALPHA_FUNC_LEVEL.floatValue();
/*      */   }
/*      */   
/*      */   public static boolean isFogFancy()
/*      */   {
/*  311 */     return isFancyFogAvailable();
/*      */   }
/*      */   
/*      */   public static boolean isFogFast()
/*      */   {
/*  316 */     return gameSettings.ofFogType == 1;
/*      */   }
/*      */   
/*      */   public static boolean isFogOff()
/*      */   {
/*  321 */     return gameSettings.ofFogType == 3;
/*      */   }
/*      */   
/*      */   public static float getFogStart()
/*      */   {
/*  326 */     return gameSettings.ofFogStart;
/*      */   }
/*      */   
/*      */   public static void dbg(String p_dbg_0_)
/*      */   {
/*  331 */     systemOut.print("[OptiFine] ");
/*  332 */     systemOut.println(p_dbg_0_);
/*      */   }
/*      */   
/*      */   public static void warn(String p_warn_0_)
/*      */   {
/*  337 */     systemOut.print("[OptiFine] [WARN] ");
/*  338 */     systemOut.println(p_warn_0_);
/*      */   }
/*      */   
/*      */   public static void error(String p_error_0_)
/*      */   {
/*  343 */     systemOut.print("[OptiFine] [ERROR] ");
/*  344 */     systemOut.println(p_error_0_);
/*      */   }
/*      */   
/*      */   public static void log(String p_log_0_)
/*      */   {
/*  349 */     dbg(p_log_0_);
/*      */   }
/*      */   
/*      */   public static int getUpdatesPerFrame()
/*      */   {
/*  354 */     return gameSettings.ofChunkUpdates;
/*      */   }
/*      */   
/*      */   public static boolean isDynamicUpdates()
/*      */   {
/*  359 */     return gameSettings.ofChunkUpdatesDynamic;
/*      */   }
/*      */   
/*      */   public static boolean isRainFancy()
/*      */   {
/*  364 */     return gameSettings.ofRain == 2 ? true : gameSettings.ofRain == 0 ? gameSettings.fancyGraphics : false;
/*      */   }
/*      */   
/*      */   public static boolean isRainOff()
/*      */   {
/*  369 */     return gameSettings.ofRain == 3;
/*      */   }
/*      */   
/*      */   public static boolean isCloudsFancy()
/*      */   {
/*  374 */     return texturePackClouds != 0 ? false : texturePackClouds == 2 ? true : gameSettings.ofClouds != 0 ? false : gameSettings.ofClouds == 2 ? true : gameSettings.fancyGraphics;
/*      */   }
/*      */   
/*      */   public static boolean isCloudsOff()
/*      */   {
/*  379 */     return gameSettings.ofClouds == 3;
/*      */   }
/*      */   
/*      */   public static void updateTexturePackClouds()
/*      */   {
/*  384 */     texturePackClouds = 0;
/*  385 */     IResourceManager iresourcemanager = getResourceManager();
/*      */     
/*  387 */     if (iresourcemanager != null)
/*      */     {
/*      */       try
/*      */       {
/*  391 */         InputStream inputstream = iresourcemanager.getResource(new ResourceLocation("mcpatcher/color.properties")).getInputStream();
/*      */         
/*  393 */         if (inputstream == null)
/*      */         {
/*  395 */           return;
/*      */         }
/*      */         
/*  398 */         Properties properties = new Properties();
/*  399 */         properties.load(inputstream);
/*  400 */         inputstream.close();
/*  401 */         String s = properties.getProperty("clouds");
/*      */         
/*  403 */         if (s == null)
/*      */         {
/*  405 */           return;
/*      */         }
/*      */         
/*  408 */         dbg("Texture pack clouds: " + s);
/*  409 */         s = s.toLowerCase();
/*      */         
/*  411 */         if (s.equals("fast"))
/*      */         {
/*  413 */           texturePackClouds = 1;
/*      */         }
/*      */         
/*  416 */         if (s.equals("fancy"))
/*      */         {
/*  418 */           texturePackClouds = 2;
/*      */         }
/*      */       }
/*      */       catch (Exception localException) {}
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isTreesFancy()
/*      */   {
/*  430 */     return gameSettings.ofTrees == 2 ? true : gameSettings.ofTrees == 0 ? gameSettings.fancyGraphics : false;
/*      */   }
/*      */   
/*      */   public static boolean isDroppedItemsFancy()
/*      */   {
/*  435 */     return gameSettings.ofDroppedItems == 2 ? true : gameSettings.ofDroppedItems == 0 ? gameSettings.fancyGraphics : false;
/*      */   }
/*      */   
/*      */   public static int limit(int p_limit_0_, int p_limit_1_, int p_limit_2_)
/*      */   {
/*  440 */     return p_limit_0_ > p_limit_2_ ? p_limit_2_ : p_limit_0_ < p_limit_1_ ? p_limit_1_ : p_limit_0_;
/*      */   }
/*      */   
/*      */   public static float limit(float p_limit_0_, float p_limit_1_, float p_limit_2_)
/*      */   {
/*  445 */     return p_limit_0_ > p_limit_2_ ? p_limit_2_ : p_limit_0_ < p_limit_1_ ? p_limit_1_ : p_limit_0_;
/*      */   }
/*      */   
/*      */   public static float limitTo1(float p_limitTo1_0_)
/*      */   {
/*  450 */     return p_limitTo1_0_ > 1.0F ? 1.0F : p_limitTo1_0_ < 0.0F ? 0.0F : p_limitTo1_0_;
/*      */   }
/*      */   
/*      */   public static boolean isAnimatedWater()
/*      */   {
/*  455 */     return gameSettings.ofAnimatedWater != 2;
/*      */   }
/*      */   
/*      */   public static boolean isGeneratedWater()
/*      */   {
/*  460 */     return gameSettings.ofAnimatedWater == 1;
/*      */   }
/*      */   
/*      */   public static boolean isAnimatedPortal()
/*      */   {
/*  465 */     return gameSettings.ofAnimatedPortal;
/*      */   }
/*      */   
/*      */   public static boolean isAnimatedLava()
/*      */   {
/*  470 */     return gameSettings.ofAnimatedLava != 2;
/*      */   }
/*      */   
/*      */   public static boolean isGeneratedLava()
/*      */   {
/*  475 */     return gameSettings.ofAnimatedLava == 1;
/*      */   }
/*      */   
/*      */   public static boolean isAnimatedFire()
/*      */   {
/*  480 */     return gameSettings.ofAnimatedFire;
/*      */   }
/*      */   
/*      */   public static boolean isAnimatedRedstone()
/*      */   {
/*  485 */     return gameSettings.ofAnimatedRedstone;
/*      */   }
/*      */   
/*      */   public static boolean isAnimatedExplosion()
/*      */   {
/*  490 */     return gameSettings.ofAnimatedExplosion;
/*      */   }
/*      */   
/*      */   public static boolean isAnimatedFlame()
/*      */   {
/*  495 */     return gameSettings.ofAnimatedFlame;
/*      */   }
/*      */   
/*      */   public static boolean isAnimatedSmoke()
/*      */   {
/*  500 */     return gameSettings.ofAnimatedSmoke;
/*      */   }
/*      */   
/*      */   public static boolean isVoidParticles()
/*      */   {
/*  505 */     return gameSettings.ofVoidParticles;
/*      */   }
/*      */   
/*      */   public static boolean isWaterParticles()
/*      */   {
/*  510 */     return gameSettings.ofWaterParticles;
/*      */   }
/*      */   
/*      */   public static boolean isRainSplash()
/*      */   {
/*  515 */     return gameSettings.ofRainSplash;
/*      */   }
/*      */   
/*      */   public static boolean isPortalParticles()
/*      */   {
/*  520 */     return gameSettings.ofPortalParticles;
/*      */   }
/*      */   
/*      */   public static boolean isPotionParticles()
/*      */   {
/*  525 */     return gameSettings.ofPotionParticles;
/*      */   }
/*      */   
/*      */   public static boolean isFireworkParticles()
/*      */   {
/*  530 */     return gameSettings.ofFireworkParticles;
/*      */   }
/*      */   
/*      */   public static float getAmbientOcclusionLevel()
/*      */   {
/*  535 */     return gameSettings.ofAoLevel;
/*      */   }
/*      */   
/*      */   private static Method getMethod(Class p_getMethod_0_, String p_getMethod_1_, Object[] p_getMethod_2_)
/*      */   {
/*  540 */     Method[] amethod = p_getMethod_0_.getMethods();
/*      */     
/*  542 */     for (int i = 0; i < amethod.length; i++)
/*      */     {
/*  544 */       Method method = amethod[i];
/*      */       
/*  546 */       if ((method.getName().equals(p_getMethod_1_)) && (method.getParameterTypes().length == p_getMethod_2_.length))
/*      */       {
/*  548 */         return method;
/*      */       }
/*      */     }
/*      */     
/*  552 */     warn("No method found for: " + p_getMethod_0_.getName() + "." + p_getMethod_1_ + "(" + arrayToString(p_getMethod_2_) + ")");
/*  553 */     return null;
/*      */   }
/*      */   
/*      */   public static String arrayToString(Object[] p_arrayToString_0_)
/*      */   {
/*  558 */     if (p_arrayToString_0_ == null)
/*      */     {
/*  560 */       return "";
/*      */     }
/*      */     
/*      */ 
/*  564 */     StringBuffer stringbuffer = new StringBuffer(p_arrayToString_0_.length * 5);
/*      */     
/*  566 */     for (int i = 0; i < p_arrayToString_0_.length; i++)
/*      */     {
/*  568 */       Object object = p_arrayToString_0_[i];
/*      */       
/*  570 */       if (i > 0)
/*      */       {
/*  572 */         stringbuffer.append(", ");
/*      */       }
/*      */       
/*  575 */       stringbuffer.append(String.valueOf(object));
/*      */     }
/*      */     
/*  578 */     return stringbuffer.toString();
/*      */   }
/*      */   
/*      */ 
/*      */   public static String arrayToString(int[] p_arrayToString_0_)
/*      */   {
/*  584 */     if (p_arrayToString_0_ == null)
/*      */     {
/*  586 */       return "";
/*      */     }
/*      */     
/*      */ 
/*  590 */     StringBuffer stringbuffer = new StringBuffer(p_arrayToString_0_.length * 5);
/*      */     
/*  592 */     for (int i = 0; i < p_arrayToString_0_.length; i++)
/*      */     {
/*  594 */       int j = p_arrayToString_0_[i];
/*      */       
/*  596 */       if (i > 0)
/*      */       {
/*  598 */         stringbuffer.append(", ");
/*      */       }
/*      */       
/*  601 */       stringbuffer.append(String.valueOf(j));
/*      */     }
/*      */     
/*  604 */     return stringbuffer.toString();
/*      */   }
/*      */   
/*      */ 
/*      */   public static Minecraft getMinecraft()
/*      */   {
/*  610 */     return minecraft;
/*      */   }
/*      */   
/*      */   public static net.minecraft.client.renderer.texture.TextureManager getTextureManager()
/*      */   {
/*  615 */     return minecraft.getTextureManager();
/*      */   }
/*      */   
/*      */   public static IResourceManager getResourceManager()
/*      */   {
/*  620 */     return minecraft.getResourceManager();
/*      */   }
/*      */   
/*      */   public static InputStream getResourceStream(ResourceLocation p_getResourceStream_0_) throws IOException
/*      */   {
/*  625 */     return getResourceStream(minecraft.getResourceManager(), p_getResourceStream_0_);
/*      */   }
/*      */   
/*      */   public static InputStream getResourceStream(IResourceManager p_getResourceStream_0_, ResourceLocation p_getResourceStream_1_) throws IOException
/*      */   {
/*  630 */     IResource iresource = p_getResourceStream_0_.getResource(p_getResourceStream_1_);
/*  631 */     return iresource == null ? null : iresource.getInputStream();
/*      */   }
/*      */   
/*      */   public static IResource getResource(ResourceLocation p_getResource_0_) throws IOException
/*      */   {
/*  636 */     return minecraft.getResourceManager().getResource(p_getResource_0_);
/*      */   }
/*      */   
/*      */   public static boolean hasResource(ResourceLocation p_hasResource_0_)
/*      */   {
/*      */     try
/*      */     {
/*  643 */       IResource iresource = getResource(p_hasResource_0_);
/*  644 */       return iresource != null;
/*      */     }
/*      */     catch (IOException var2) {}
/*      */     
/*  648 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public static boolean hasResource(IResourceManager p_hasResource_0_, ResourceLocation p_hasResource_1_)
/*      */   {
/*      */     try
/*      */     {
/*  656 */       IResource iresource = p_hasResource_0_.getResource(p_hasResource_1_);
/*  657 */       return iresource != null;
/*      */     }
/*      */     catch (IOException var3) {}
/*      */     
/*  661 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public static IResourcePack[] getResourcePacks()
/*      */   {
/*  667 */     ResourcePackRepository resourcepackrepository = minecraft.getResourcePackRepository();
/*  668 */     List list = resourcepackrepository.getRepositoryEntries();
/*  669 */     List list1 = new ArrayList();
/*      */     
/*  671 */     for (Object resourcepackrepository$entry : list)
/*      */     {
/*  673 */       list1.add(((ResourcePackRepository.Entry)resourcepackrepository$entry).getResourcePack());
/*      */     }
/*      */     
/*  676 */     IResourcePack[] airesourcepack = (IResourcePack[])list1.toArray(new IResourcePack[list1.size()]);
/*  677 */     return airesourcepack;
/*      */   }
/*      */   
/*      */   public static String getResourcePackNames()
/*      */   {
/*  682 */     if (minecraft == null)
/*      */     {
/*  684 */       return "";
/*      */     }
/*  686 */     if (minecraft.getResourcePackRepository() == null)
/*      */     {
/*  688 */       return "";
/*      */     }
/*      */     
/*      */ 
/*  692 */     IResourcePack[] airesourcepack = getResourcePacks();
/*      */     
/*  694 */     if (airesourcepack.length <= 0)
/*      */     {
/*  696 */       return getDefaultResourcePack().getPackName();
/*      */     }
/*      */     
/*      */ 
/*  700 */     String[] astring = new String[airesourcepack.length];
/*      */     
/*  702 */     for (int i = 0; i < airesourcepack.length; i++)
/*      */     {
/*  704 */       astring[i] = airesourcepack[i].getPackName();
/*      */     }
/*      */     
/*  707 */     String s = arrayToString(astring);
/*  708 */     return s;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static IResourcePack getDefaultResourcePack()
/*      */   {
/*  715 */     return minecraft.getResourcePackRepository().rprDefaultResourcePack;
/*      */   }
/*      */   
/*      */   public static boolean isFromDefaultResourcePack(ResourceLocation p_isFromDefaultResourcePack_0_)
/*      */   {
/*  720 */     IResourcePack iresourcepack = getDefiningResourcePack(p_isFromDefaultResourcePack_0_);
/*  721 */     return iresourcepack == getDefaultResourcePack();
/*      */   }
/*      */   
/*      */   public static IResourcePack getDefiningResourcePack(ResourceLocation p_getDefiningResourcePack_0_)
/*      */   {
/*  726 */     IResourcePack[] airesourcepack = getResourcePacks();
/*      */     
/*  728 */     for (int i = airesourcepack.length - 1; i >= 0; i--)
/*      */     {
/*  730 */       IResourcePack iresourcepack = airesourcepack[i];
/*      */       
/*  732 */       if (iresourcepack.resourceExists(p_getDefiningResourcePack_0_))
/*      */       {
/*  734 */         return iresourcepack;
/*      */       }
/*      */     }
/*      */     
/*  738 */     if (getDefaultResourcePack().resourceExists(p_getDefiningResourcePack_0_))
/*      */     {
/*  740 */       return getDefaultResourcePack();
/*      */     }
/*      */     
/*      */ 
/*  744 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public static RenderGlobal getRenderGlobal()
/*      */   {
/*  750 */     return minecraft == null ? null : minecraft.renderGlobal;
/*      */   }
/*      */   
/*      */   public static int getMaxDynamicTileWidth()
/*      */   {
/*  755 */     return 64;
/*      */   }
/*      */   
/*      */   public static boolean isBetterGrass()
/*      */   {
/*  760 */     return gameSettings.ofBetterGrass != 3;
/*      */   }
/*      */   
/*      */   public static boolean isBetterGrassFancy()
/*      */   {
/*  765 */     return gameSettings.ofBetterGrass == 2;
/*      */   }
/*      */   
/*      */   public static boolean isWeatherEnabled()
/*      */   {
/*  770 */     return gameSettings.ofWeather;
/*      */   }
/*      */   
/*      */   public static boolean isSkyEnabled()
/*      */   {
/*  775 */     return gameSettings.ofSky;
/*      */   }
/*      */   
/*      */   public static boolean isSunMoonEnabled()
/*      */   {
/*  780 */     return gameSettings.ofSunMoon;
/*      */   }
/*      */   
/*      */   public static boolean isVignetteEnabled()
/*      */   {
/*  785 */     return gameSettings.ofVignette == 2 ? true : gameSettings.ofVignette == 0 ? gameSettings.fancyGraphics : false;
/*      */   }
/*      */   
/*      */   public static boolean isStarsEnabled()
/*      */   {
/*  790 */     return gameSettings.ofStars;
/*      */   }
/*      */   
/*      */   public static void sleep(long p_sleep_0_)
/*      */   {
/*      */     try
/*      */     {
/*  797 */       Thread.currentThread();
/*  798 */       Thread.sleep(p_sleep_0_);
/*      */     }
/*      */     catch (InterruptedException interruptedexception)
/*      */     {
/*  802 */       interruptedexception.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean isTimeDayOnly()
/*      */   {
/*  808 */     return gameSettings.ofTime == 1;
/*      */   }
/*      */   
/*      */   public static boolean isTimeDefault()
/*      */   {
/*  813 */     return (gameSettings.ofTime == 0) || (gameSettings.ofTime == 2);
/*      */   }
/*      */   
/*      */   public static boolean isTimeNightOnly()
/*      */   {
/*  818 */     return gameSettings.ofTime == 3;
/*      */   }
/*      */   
/*      */   public static boolean isClearWater()
/*      */   {
/*  823 */     return gameSettings.ofClearWater;
/*      */   }
/*      */   
/*      */   public static int getAnisotropicFilterLevel()
/*      */   {
/*  828 */     return gameSettings.ofAfLevel;
/*      */   }
/*      */   
/*      */   public static int getAntialiasingLevel()
/*      */   {
/*  833 */     return antialiasingLevel;
/*      */   }
/*      */   
/*      */   public static boolean between(int p_between_0_, int p_between_1_, int p_between_2_)
/*      */   {
/*  838 */     return (p_between_0_ >= p_between_1_) && (p_between_0_ <= p_between_2_);
/*      */   }
/*      */   
/*      */   public static boolean isMultiTexture()
/*      */   {
/*  843 */     return getAnisotropicFilterLevel() > 1;
/*      */   }
/*      */   
/*      */   public static boolean isDrippingWaterLava()
/*      */   {
/*  848 */     return gameSettings.ofDrippingWaterLava;
/*      */   }
/*      */   
/*      */   public static boolean isBetterSnow()
/*      */   {
/*  853 */     return gameSettings.ofBetterSnow;
/*      */   }
/*      */   
/*      */   public static Dimension getFullscreenDimension()
/*      */   {
/*  858 */     if (desktopDisplayMode == null)
/*      */     {
/*  860 */       return null;
/*      */     }
/*  862 */     if (gameSettings == null)
/*      */     {
/*  864 */       return new Dimension(desktopDisplayMode.getWidth(), desktopDisplayMode.getHeight());
/*      */     }
/*      */     
/*      */ 
/*  868 */     String s = gameSettings.ofFullscreenMode;
/*      */     
/*  870 */     if (s.equals("Default"))
/*      */     {
/*  872 */       return new Dimension(desktopDisplayMode.getWidth(), desktopDisplayMode.getHeight());
/*      */     }
/*      */     
/*      */ 
/*  876 */     String[] astring = tokenize(s, " x");
/*  877 */     return astring.length < 2 ? new Dimension(desktopDisplayMode.getWidth(), desktopDisplayMode.getHeight()) : new Dimension(parseInt(astring[0], -1), parseInt(astring[1], -1));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int parseInt(String p_parseInt_0_, int p_parseInt_1_)
/*      */   {
/*      */     try
/*      */     {
/*  886 */       return p_parseInt_0_ == null ? p_parseInt_1_ : Integer.parseInt(p_parseInt_0_);
/*      */     }
/*      */     catch (NumberFormatException var3) {}
/*      */     
/*  890 */     return p_parseInt_1_;
/*      */   }
/*      */   
/*      */ 
/*      */   public static float parseFloat(String p_parseFloat_0_, float p_parseFloat_1_)
/*      */   {
/*      */     try
/*      */     {
/*  898 */       return p_parseFloat_0_ == null ? p_parseFloat_1_ : Float.parseFloat(p_parseFloat_0_);
/*      */     }
/*      */     catch (NumberFormatException var3) {}
/*      */     
/*  902 */     return p_parseFloat_1_;
/*      */   }
/*      */   
/*      */ 
/*      */   public static String[] tokenize(String p_tokenize_0_, String p_tokenize_1_)
/*      */   {
/*  908 */     StringTokenizer stringtokenizer = new StringTokenizer(p_tokenize_0_, p_tokenize_1_);
/*  909 */     List list = new ArrayList();
/*      */     
/*  911 */     while (stringtokenizer.hasMoreTokens())
/*      */     {
/*  913 */       String s = stringtokenizer.nextToken();
/*  914 */       list.add(s);
/*      */     }
/*      */     
/*  917 */     String[] astring = (String[])list.toArray(new String[list.size()]);
/*  918 */     return astring;
/*      */   }
/*      */   
/*      */   public static DisplayMode getDesktopDisplayMode()
/*      */   {
/*  923 */     return desktopDisplayMode;
/*      */   }
/*      */   
/*      */   public static DisplayMode[] getFullscreenDisplayModes()
/*      */   {
/*      */     try
/*      */     {
/*  930 */       DisplayMode[] adisplaymode = Display.getAvailableDisplayModes();
/*  931 */       List list = new ArrayList();
/*      */       
/*  933 */       for (int i = 0; i < adisplaymode.length; i++)
/*      */       {
/*  935 */         DisplayMode displaymode = adisplaymode[i];
/*      */         
/*  937 */         if ((desktopDisplayMode == null) || ((displaymode.getBitsPerPixel() == desktopDisplayMode.getBitsPerPixel()) && (displaymode.getFrequency() == desktopDisplayMode.getFrequency())))
/*      */         {
/*  939 */           list.add(displaymode);
/*      */         }
/*      */       }
/*      */       
/*  943 */       DisplayMode[] adisplaymode1 = (DisplayMode[])list.toArray(new DisplayMode[list.size()]);
/*  944 */       Comparator comparator = new Comparator()
/*      */       {
/*      */         public int compare(Object p_compare_1_, Object p_compare_2_)
/*      */         {
/*  948 */           DisplayMode displaymode1 = (DisplayMode)p_compare_1_;
/*  949 */           DisplayMode displaymode2 = (DisplayMode)p_compare_2_;
/*  950 */           return displaymode1.getHeight() != displaymode2.getHeight() ? displaymode2.getHeight() - displaymode1.getHeight() : displaymode1.getWidth() != displaymode2.getWidth() ? displaymode2.getWidth() - displaymode1.getWidth() : 0;
/*      */         }
/*  952 */       };
/*  953 */       Arrays.sort(adisplaymode1, comparator);
/*  954 */       return adisplaymode1;
/*      */     }
/*      */     catch (Exception exception)
/*      */     {
/*  958 */       exception.printStackTrace(); }
/*  959 */     return tmp113_110;
/*      */   }
/*      */   
/*      */ 
/*      */   public static String[] getFullscreenModes()
/*      */   {
/*  965 */     DisplayMode[] adisplaymode = getFullscreenDisplayModes();
/*  966 */     String[] astring = new String[adisplaymode.length];
/*      */     
/*  968 */     for (int i = 0; i < adisplaymode.length; i++)
/*      */     {
/*  970 */       DisplayMode displaymode = adisplaymode[i];
/*  971 */       String s = displaymode.getWidth() + "x" + displaymode.getHeight();
/*  972 */       astring[i] = s;
/*      */     }
/*      */     
/*  975 */     return astring;
/*      */   }
/*      */   
/*      */   public static DisplayMode getDisplayMode(Dimension p_getDisplayMode_0_) throws LWJGLException
/*      */   {
/*  980 */     DisplayMode[] adisplaymode = Display.getAvailableDisplayModes();
/*      */     
/*  982 */     for (int i = 0; i < adisplaymode.length; i++)
/*      */     {
/*  984 */       DisplayMode displaymode = adisplaymode[i];
/*      */       
/*  986 */       if ((displaymode.getWidth() == p_getDisplayMode_0_.width) && (displaymode.getHeight() == p_getDisplayMode_0_.height) && ((desktopDisplayMode == null) || ((displaymode.getBitsPerPixel() == desktopDisplayMode.getBitsPerPixel()) && (displaymode.getFrequency() == desktopDisplayMode.getFrequency()))))
/*      */       {
/*  988 */         return displaymode;
/*      */       }
/*      */     }
/*      */     
/*  992 */     return desktopDisplayMode;
/*      */   }
/*      */   
/*      */   public static boolean isAnimatedTerrain()
/*      */   {
/*  997 */     return gameSettings.ofAnimatedTerrain;
/*      */   }
/*      */   
/*      */   public static boolean isAnimatedTextures()
/*      */   {
/* 1002 */     return gameSettings.ofAnimatedTextures;
/*      */   }
/*      */   
/*      */   public static boolean isSwampColors()
/*      */   {
/* 1007 */     return gameSettings.ofSwampColors;
/*      */   }
/*      */   
/*      */   public static boolean isRandomMobs()
/*      */   {
/* 1012 */     return gameSettings.ofRandomMobs;
/*      */   }
/*      */   
/*      */   public static void checkGlError(String p_checkGlError_0_)
/*      */   {
/* 1017 */     int i = GL11.glGetError();
/*      */     
/* 1019 */     if (i != 0)
/*      */     {
/* 1021 */       String s = GLU.gluErrorString(i);
/* 1022 */       error("OpenGlError: " + i + " (" + s + "), at: " + p_checkGlError_0_);
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean isSmoothBiomes()
/*      */   {
/* 1028 */     return gameSettings.ofSmoothBiomes;
/*      */   }
/*      */   
/*      */   public static boolean isCustomColors()
/*      */   {
/* 1033 */     return gameSettings.ofCustomColors;
/*      */   }
/*      */   
/*      */   public static boolean isCustomSky()
/*      */   {
/* 1038 */     return gameSettings.ofCustomSky;
/*      */   }
/*      */   
/*      */   public static boolean isCustomFonts()
/*      */   {
/* 1043 */     return gameSettings.ofCustomFonts;
/*      */   }
/*      */   
/*      */   public static boolean isShowCapes()
/*      */   {
/* 1048 */     return gameSettings.ofShowCapes;
/*      */   }
/*      */   
/*      */   public static boolean isConnectedTextures()
/*      */   {
/* 1053 */     return gameSettings.ofConnectedTextures != 3;
/*      */   }
/*      */   
/*      */   public static boolean isNaturalTextures()
/*      */   {
/* 1058 */     return gameSettings.ofNaturalTextures;
/*      */   }
/*      */   
/*      */   public static boolean isConnectedTexturesFancy()
/*      */   {
/* 1063 */     return gameSettings.ofConnectedTextures == 2;
/*      */   }
/*      */   
/*      */   public static boolean isFastRender()
/*      */   {
/* 1068 */     return gameSettings.ofFastRender;
/*      */   }
/*      */   
/*      */   public static boolean isTranslucentBlocksFancy()
/*      */   {
/* 1073 */     return gameSettings.ofTranslucentBlocks == 2 ? true : gameSettings.ofTranslucentBlocks == 0 ? gameSettings.fancyGraphics : false;
/*      */   }
/*      */   
/*      */   public static String[] readLines(File p_readLines_0_) throws IOException
/*      */   {
/* 1078 */     List list = new ArrayList();
/* 1079 */     FileInputStream fileinputstream = new FileInputStream(p_readLines_0_);
/* 1080 */     InputStreamReader inputstreamreader = new InputStreamReader(fileinputstream, "ASCII");
/* 1081 */     BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
/*      */     
/*      */     for (;;)
/*      */     {
/* 1085 */       String s = bufferedreader.readLine();
/*      */       
/* 1087 */       if (s == null)
/*      */       {
/* 1089 */         String[] astring = (String[])list.toArray(new String[list.size()]);
/* 1090 */         return astring;
/*      */       }
/*      */       
/* 1093 */       list.add(s);
/*      */     }
/*      */   }
/*      */   
/*      */   public static String readFile(File p_readFile_0_) throws IOException
/*      */   {
/* 1099 */     FileInputStream fileinputstream = new FileInputStream(p_readFile_0_);
/* 1100 */     return readInputStream(fileinputstream, "ASCII");
/*      */   }
/*      */   
/*      */   public static String readInputStream(InputStream p_readInputStream_0_) throws IOException
/*      */   {
/* 1105 */     return readInputStream(p_readInputStream_0_, "ASCII");
/*      */   }
/*      */   
/*      */   public static String readInputStream(InputStream p_readInputStream_0_, String p_readInputStream_1_) throws IOException
/*      */   {
/* 1110 */     InputStreamReader inputstreamreader = new InputStreamReader(p_readInputStream_0_, p_readInputStream_1_);
/* 1111 */     BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
/* 1112 */     StringBuffer stringbuffer = new StringBuffer();
/*      */     
/*      */     for (;;)
/*      */     {
/* 1116 */       String s = bufferedreader.readLine();
/*      */       
/* 1118 */       if (s == null)
/*      */       {
/* 1120 */         return stringbuffer.toString();
/*      */       }
/*      */       
/* 1123 */       stringbuffer.append(s);
/* 1124 */       stringbuffer.append("\n");
/*      */     }
/*      */   }
/*      */   
/*      */   public static GameSettings getGameSettings()
/*      */   {
/* 1130 */     return gameSettings;
/*      */   }
/*      */   
/*      */   public static String getNewRelease()
/*      */   {
/* 1135 */     return newRelease;
/*      */   }
/*      */   
/*      */   public static void setNewRelease(String p_setNewRelease_0_)
/*      */   {
/* 1140 */     newRelease = p_setNewRelease_0_;
/*      */   }
/*      */   
/*      */   public static int compareRelease(String p_compareRelease_0_, String p_compareRelease_1_)
/*      */   {
/* 1145 */     String[] astring = splitRelease(p_compareRelease_0_);
/* 1146 */     String[] astring1 = splitRelease(p_compareRelease_1_);
/* 1147 */     String s = astring[0];
/* 1148 */     String s1 = astring1[0];
/*      */     
/* 1150 */     if (!s.equals(s1))
/*      */     {
/* 1152 */       return s.compareTo(s1);
/*      */     }
/*      */     
/*      */ 
/* 1156 */     int i = parseInt(astring[1], -1);
/* 1157 */     int j = parseInt(astring1[1], -1);
/*      */     
/* 1159 */     if (i != j)
/*      */     {
/* 1161 */       return i - j;
/*      */     }
/*      */     
/*      */ 
/* 1165 */     String s2 = astring[2];
/* 1166 */     String s3 = astring1[2];
/* 1167 */     return s2.compareTo(s3);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static String[] splitRelease(String p_splitRelease_0_)
/*      */   {
/* 1174 */     if ((p_splitRelease_0_ != null) && (p_splitRelease_0_.length() > 0))
/*      */     {
/* 1176 */       String s = p_splitRelease_0_.substring(0, 1);
/*      */       
/* 1178 */       if (p_splitRelease_0_.length() <= 1)
/*      */       {
/* 1180 */         return new String[] { s, "", "" };
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1186 */       for (int i = 1; (i < p_splitRelease_0_.length()) && (Character.isDigit(p_splitRelease_0_.charAt(i))); i++) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1191 */       String s1 = p_splitRelease_0_.substring(1, i);
/*      */       
/* 1193 */       if (i >= p_splitRelease_0_.length())
/*      */       {
/* 1195 */         return new String[] { s, s1, "" };
/*      */       }
/*      */       
/*      */ 
/* 1199 */       String s2 = p_splitRelease_0_.substring(i);
/* 1200 */       return new String[] { s, s1, s2 };
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1206 */     return new String[] { "", "", "" };
/*      */   }
/*      */   
/*      */ 
/*      */   public static int intHash(int p_intHash_0_)
/*      */   {
/* 1212 */     p_intHash_0_ = p_intHash_0_ ^ 0x3D ^ p_intHash_0_ >> 16;
/* 1213 */     p_intHash_0_ += (p_intHash_0_ << 3);
/* 1214 */     p_intHash_0_ ^= p_intHash_0_ >> 4;
/* 1215 */     p_intHash_0_ *= 668265261;
/* 1216 */     p_intHash_0_ ^= p_intHash_0_ >> 15;
/* 1217 */     return p_intHash_0_;
/*      */   }
/*      */   
/*      */   public static int getRandom(BlockPos p_getRandom_0_, int p_getRandom_1_)
/*      */   {
/* 1222 */     int i = intHash(p_getRandom_1_ + 37);
/* 1223 */     i = intHash(i + p_getRandom_0_.getX());
/* 1224 */     i = intHash(i + p_getRandom_0_.getZ());
/* 1225 */     i = intHash(i + p_getRandom_0_.getY());
/* 1226 */     return i;
/*      */   }
/*      */   
/*      */   public static WorldServer getWorldServer()
/*      */   {
/* 1231 */     if (minecraft == null)
/*      */     {
/* 1233 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 1237 */     World world = minecraft.theWorld;
/*      */     
/* 1239 */     if (world == null)
/*      */     {
/* 1241 */       return null;
/*      */     }
/* 1243 */     if (!minecraft.isIntegratedServerRunning())
/*      */     {
/* 1245 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 1249 */     IntegratedServer integratedserver = minecraft.getIntegratedServer();
/*      */     
/* 1251 */     if (integratedserver == null)
/*      */     {
/* 1253 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 1257 */     WorldProvider worldprovider = world.provider;
/*      */     
/* 1259 */     if (worldprovider == null)
/*      */     {
/* 1261 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 1265 */     int i = worldprovider.getDimensionId();
/*      */     
/*      */     try
/*      */     {
/* 1269 */       return integratedserver.worldServerForDimension(i);
/*      */     }
/*      */     catch (NullPointerException var5) {}
/*      */     
/*      */ 
/* 1274 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int getAvailableProcessors()
/*      */   {
/* 1284 */     return availableProcessors;
/*      */   }
/*      */   
/*      */   public static void updateAvailableProcessors()
/*      */   {
/* 1289 */     availableProcessors = Runtime.getRuntime().availableProcessors();
/*      */   }
/*      */   
/*      */   public static boolean isSingleProcessor()
/*      */   {
/* 1294 */     return getAvailableProcessors() <= 1;
/*      */   }
/*      */   
/*      */   public static boolean isSmoothWorld()
/*      */   {
/* 1299 */     return gameSettings.ofSmoothWorld;
/*      */   }
/*      */   
/*      */   public static boolean isLazyChunkLoading()
/*      */   {
/* 1304 */     return !isSingleProcessor() ? false : gameSettings.ofLazyChunkLoading;
/*      */   }
/*      */   
/*      */   public static int getChunkViewDistance()
/*      */   {
/* 1309 */     if (gameSettings == null)
/*      */     {
/* 1311 */       return 10;
/*      */     }
/*      */     
/*      */ 
/* 1315 */     int i = gameSettings.renderDistanceChunks;
/* 1316 */     return i;
/*      */   }
/*      */   
/*      */ 
/*      */   public static boolean equals(Object p_equals_0_, Object p_equals_1_)
/*      */   {
/* 1322 */     return p_equals_0_ == null ? false : p_equals_0_ == p_equals_1_ ? true : p_equals_0_.equals(p_equals_1_);
/*      */   }
/*      */   
/*      */   public static void checkDisplaySettings()
/*      */   {
/* 1327 */     if (getAntialiasingLevel() > 0)
/*      */     {
/* 1329 */       int i = getAntialiasingLevel();
/* 1330 */       DisplayMode displaymode = Display.getDisplayMode();
/* 1331 */       dbg("FSAA Samples: " + i);
/*      */       
/*      */       try
/*      */       {
/* 1335 */         Display.destroy();
/* 1336 */         Display.setDisplayMode(displaymode);
/* 1337 */         Display.create(new PixelFormat().withDepthBits(24).withSamples(i));
/* 1338 */         Display.setResizable(false);
/* 1339 */         Display.setResizable(true);
/*      */       }
/*      */       catch (LWJGLException lwjglexception2)
/*      */       {
/* 1343 */         warn("Error setting FSAA: " + i + "x");
/* 1344 */         lwjglexception2.printStackTrace();
/*      */         
/*      */         try
/*      */         {
/* 1348 */           Display.setDisplayMode(displaymode);
/* 1349 */           Display.create(new PixelFormat().withDepthBits(24));
/* 1350 */           Display.setResizable(false);
/* 1351 */           Display.setResizable(true);
/*      */         }
/*      */         catch (LWJGLException lwjglexception1)
/*      */         {
/* 1355 */           lwjglexception1.printStackTrace();
/*      */           
/*      */           try
/*      */           {
/* 1359 */             Display.setDisplayMode(displaymode);
/* 1360 */             Display.create();
/* 1361 */             Display.setResizable(false);
/* 1362 */             Display.setResizable(true);
/*      */           }
/*      */           catch (LWJGLException lwjglexception)
/*      */           {
/* 1366 */             lwjglexception.printStackTrace();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static ByteBuffer readIconImage(File p_readIconImage_0_) throws IOException
/*      */   {
/* 1375 */     BufferedImage bufferedimage = ImageIO.read(p_readIconImage_0_);
/* 1376 */     int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0, bufferedimage.getWidth());
/* 1377 */     ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
/*      */     int[] arrayOfInt1;
/* 1379 */     int j = (arrayOfInt1 = aint).length; for (int i = 0; i < j; i++) { int i = arrayOfInt1[i];
/*      */       
/* 1381 */       bytebuffer.putInt(i << 8 | i >> 24 & 0xFF);
/*      */     }
/*      */     
/* 1384 */     bytebuffer.flip();
/* 1385 */     return bytebuffer;
/*      */   }
/*      */   
/*      */   public static void checkDisplayMode()
/*      */   {
/*      */     try
/*      */     {
/* 1392 */       if (minecraft.isFullScreen())
/*      */       {
/* 1394 */         if (fullscreenModeChecked)
/*      */         {
/* 1396 */           return;
/*      */         }
/*      */         
/* 1399 */         fullscreenModeChecked = true;
/* 1400 */         desktopModeChecked = false;
/* 1401 */         DisplayMode displaymode = Display.getDisplayMode();
/* 1402 */         Dimension dimension = getFullscreenDimension();
/*      */         
/* 1404 */         if (dimension == null)
/*      */         {
/* 1406 */           return;
/*      */         }
/*      */         
/* 1409 */         if ((displaymode.getWidth() == dimension.width) && (displaymode.getHeight() == dimension.height))
/*      */         {
/* 1411 */           return;
/*      */         }
/*      */         
/* 1414 */         DisplayMode displaymode1 = getDisplayMode(dimension);
/*      */         
/* 1416 */         if (displaymode1 == null)
/*      */         {
/* 1418 */           return;
/*      */         }
/*      */         
/* 1421 */         Display.setDisplayMode(displaymode1);
/* 1422 */         minecraft.displayWidth = Display.getDisplayMode().getWidth();
/* 1423 */         minecraft.displayHeight = Display.getDisplayMode().getHeight();
/*      */         
/* 1425 */         if (minecraft.displayWidth <= 0)
/*      */         {
/* 1427 */           minecraft.displayWidth = 1;
/*      */         }
/*      */         
/* 1430 */         if (minecraft.displayHeight <= 0)
/*      */         {
/* 1432 */           minecraft.displayHeight = 1;
/*      */         }
/*      */         
/* 1435 */         if (minecraft.currentScreen != null)
/*      */         {
/* 1437 */           ScaledResolution scaledresolution = new ScaledResolution(minecraft, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
/* 1438 */           int i = ScaledResolution.getScaledWidth();
/* 1439 */           int j = ScaledResolution.getScaledHeight();
/* 1440 */           minecraft.currentScreen.setWorldAndResolution(minecraft, i, j);
/*      */         }
/*      */         
/* 1443 */         minecraft.loadingScreen = new LoadingScreenRenderer(minecraft);
/* 1444 */         updateFramebufferSize();
/* 1445 */         Display.setFullscreen(true);
/* 1446 */         minecraft.gameSettings.updateVSync();
/* 1447 */         GlStateManager.enableTexture2D();
/*      */       }
/*      */       else
/*      */       {
/* 1451 */         if (desktopModeChecked)
/*      */         {
/* 1453 */           return;
/*      */         }
/*      */         
/* 1456 */         desktopModeChecked = true;
/* 1457 */         fullscreenModeChecked = false;
/* 1458 */         minecraft.gameSettings.updateVSync();
/* 1459 */         Display.update();
/* 1460 */         GlStateManager.enableTexture2D();
/* 1461 */         Display.setResizable(false);
/* 1462 */         Display.setResizable(true);
/*      */       }
/*      */     }
/*      */     catch (Exception exception)
/*      */     {
/* 1467 */       exception.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void updateFramebufferSize()
/*      */   {
/* 1473 */     minecraft.getFramebuffer().createBindFramebuffer(minecraft.displayWidth, minecraft.displayHeight);
/*      */     
/* 1475 */     if (minecraft.entityRenderer != null)
/*      */     {
/* 1477 */       minecraft.entityRenderer.updateShaderGroupSize(minecraft.displayWidth, minecraft.displayHeight);
/*      */     }
/*      */   }
/*      */   
/*      */   public static Object[] addObjectToArray(Object[] p_addObjectToArray_0_, Object p_addObjectToArray_1_)
/*      */   {
/* 1483 */     if (p_addObjectToArray_0_ == null)
/*      */     {
/* 1485 */       throw new NullPointerException("The given array is NULL");
/*      */     }
/*      */     
/*      */ 
/* 1489 */     int i = p_addObjectToArray_0_.length;
/* 1490 */     int j = i + 1;
/* 1491 */     Object[] aobject = (Object[])Array.newInstance(p_addObjectToArray_0_.getClass().getComponentType(), j);
/* 1492 */     System.arraycopy(p_addObjectToArray_0_, 0, aobject, 0, i);
/* 1493 */     aobject[i] = p_addObjectToArray_1_;
/* 1494 */     return aobject;
/*      */   }
/*      */   
/*      */ 
/*      */   public static Object[] addObjectsToArray(Object[] p_addObjectsToArray_0_, Object[] p_addObjectsToArray_1_)
/*      */   {
/* 1500 */     if (p_addObjectsToArray_0_ == null)
/*      */     {
/* 1502 */       throw new NullPointerException("The given array is NULL");
/*      */     }
/* 1504 */     if (p_addObjectsToArray_1_.length == 0)
/*      */     {
/* 1506 */       return p_addObjectsToArray_0_;
/*      */     }
/*      */     
/*      */ 
/* 1510 */     int i = p_addObjectsToArray_0_.length;
/* 1511 */     int j = i + p_addObjectsToArray_1_.length;
/* 1512 */     Object[] aobject = (Object[])Array.newInstance(p_addObjectsToArray_0_.getClass().getComponentType(), j);
/* 1513 */     System.arraycopy(p_addObjectsToArray_0_, 0, aobject, 0, i);
/* 1514 */     System.arraycopy(p_addObjectsToArray_1_, 0, aobject, i, p_addObjectsToArray_1_.length);
/* 1515 */     return aobject;
/*      */   }
/*      */   
/*      */ 
/*      */   public static boolean isCustomItems()
/*      */   {
/* 1521 */     return false;
/*      */   }
/*      */   
/*      */   public static boolean isActing()
/*      */   {
/* 1526 */     boolean flag = isActingNow();
/* 1527 */     long i = System.currentTimeMillis();
/*      */     
/* 1529 */     if (flag)
/*      */     {
/* 1531 */       lastActionTime = i;
/* 1532 */       return true;
/*      */     }
/*      */     
/*      */ 
/* 1536 */     return i - lastActionTime < 100L;
/*      */   }
/*      */   
/*      */ 
/*      */   private static boolean isActingNow()
/*      */   {
/* 1542 */     return Mouse.isButtonDown(0) ? true : Mouse.isButtonDown(1);
/*      */   }
/*      */   
/*      */   public static void drawFps()
/*      */   {
/* 1547 */     Minecraft minecraftx = minecraft;
/* 1548 */     int i = Minecraft.getDebugFPS();
/* 1549 */     String s = getUpdates(minecraft.debug);
/* 1550 */     int j = minecraft.renderGlobal.getCountActiveRenderers();
/* 1551 */     int k = minecraft.renderGlobal.getCountEntitiesRendered();
/* 1552 */     int l = minecraft.renderGlobal.getCountTileEntitiesRendered();
/* 1553 */     String s1 = i + " fps, C: " + j + ", E: " + k + "+" + l + ", U: " + s;
/* 1554 */     minecraft.fontRendererObj.drawString(s1, 2.0D, 2.0D, -2039584);
/*      */   }
/*      */   
/*      */   private static String getUpdates(String p_getUpdates_0_)
/*      */   {
/* 1559 */     int i = p_getUpdates_0_.indexOf('(');
/*      */     
/* 1561 */     if (i < 0)
/*      */     {
/* 1563 */       return "";
/*      */     }
/*      */     
/*      */ 
/* 1567 */     int j = p_getUpdates_0_.indexOf(' ', i);
/* 1568 */     return j < 0 ? "" : p_getUpdates_0_.substring(i + 1, j);
/*      */   }
/*      */   
/*      */ 
/*      */   public static int getBitsOs()
/*      */   {
/* 1574 */     String s = System.getenv("ProgramFiles(X86)");
/* 1575 */     return s != null ? 64 : 32;
/*      */   }
/*      */   
/*      */   public static int getBitsJre()
/*      */   {
/* 1580 */     String[] astring = { "sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch" };
/*      */     
/* 1582 */     for (int i = 0; i < astring.length; i++)
/*      */     {
/* 1584 */       String s = astring[i];
/* 1585 */       String s1 = System.getProperty(s);
/*      */       
/* 1587 */       if ((s1 != null) && (s1.contains("64")))
/*      */       {
/* 1589 */         return 64;
/*      */       }
/*      */     }
/*      */     
/* 1593 */     return 32;
/*      */   }
/*      */   
/*      */   public static boolean isNotify64BitJava()
/*      */   {
/* 1598 */     return notify64BitJava;
/*      */   }
/*      */   
/*      */   public static void setNotify64BitJava(boolean p_setNotify64BitJava_0_)
/*      */   {
/* 1603 */     notify64BitJava = p_setNotify64BitJava_0_;
/*      */   }
/*      */   
/*      */   public static boolean isConnectedModels()
/*      */   {
/* 1608 */     return false;
/*      */   }
/*      */   
/*      */   public static String fillLeft(String p_fillLeft_0_, int p_fillLeft_1_, char p_fillLeft_2_)
/*      */   {
/* 1613 */     if (p_fillLeft_0_ == null)
/*      */     {
/* 1615 */       p_fillLeft_0_ = "";
/*      */     }
/*      */     
/* 1618 */     if (p_fillLeft_0_.length() >= p_fillLeft_1_)
/*      */     {
/* 1620 */       return p_fillLeft_0_;
/*      */     }
/*      */     
/*      */ 
/* 1624 */     StringBuffer stringbuffer = new StringBuffer(p_fillLeft_0_);
/*      */     
/* 1626 */     while (stringbuffer.length() < p_fillLeft_1_ - p_fillLeft_0_.length())
/*      */     {
/* 1628 */       stringbuffer.append(p_fillLeft_2_);
/*      */     }
/*      */     
/* 1631 */     return stringbuffer.toString() + p_fillLeft_0_;
/*      */   }
/*      */   
/*      */ 
/*      */   public static String fillRight(String p_fillRight_0_, int p_fillRight_1_, char p_fillRight_2_)
/*      */   {
/* 1637 */     if (p_fillRight_0_ == null)
/*      */     {
/* 1639 */       p_fillRight_0_ = "";
/*      */     }
/*      */     
/* 1642 */     if (p_fillRight_0_.length() >= p_fillRight_1_)
/*      */     {
/* 1644 */       return p_fillRight_0_;
/*      */     }
/*      */     
/*      */ 
/* 1648 */     StringBuffer stringbuffer = new StringBuffer(p_fillRight_0_);
/*      */     
/* 1650 */     while (stringbuffer.length() < p_fillRight_1_)
/*      */     {
/* 1652 */       stringbuffer.append(p_fillRight_2_);
/*      */     }
/*      */     
/* 1655 */     return stringbuffer.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\Config.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */