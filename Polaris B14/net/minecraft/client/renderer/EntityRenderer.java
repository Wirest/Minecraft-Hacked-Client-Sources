/*      */ package net.minecraft.client.renderer;
/*      */ 
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.gson.JsonSyntaxException;
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.Field;
/*      */ import java.nio.FloatBuffer;
/*      */ import java.util.Calendar;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import java.util.concurrent.Callable;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockBed;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.entity.AbstractClientPlayer;
/*      */ import net.minecraft.client.entity.EntityPlayerSP;
/*      */ import net.minecraft.client.gui.GuiDownloadTerrain;
/*      */ import net.minecraft.client.gui.GuiIngame;
/*      */ import net.minecraft.client.gui.GuiMainMenu;
/*      */ import net.minecraft.client.gui.GuiNewChat;
/*      */ import net.minecraft.client.gui.MapItemRenderer;
/*      */ import net.minecraft.client.gui.ScaledResolution;
/*      */ import net.minecraft.client.multiplayer.PlayerControllerMP;
/*      */ import net.minecraft.client.multiplayer.WorldClient;
/*      */ import net.minecraft.client.particle.EffectRenderer;
/*      */ import net.minecraft.client.renderer.culling.Frustum;
/*      */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*      */ import net.minecraft.client.renderer.texture.ITextureObject;
/*      */ import net.minecraft.client.renderer.texture.TextureManager;
/*      */ import net.minecraft.client.renderer.texture.TextureMap;
/*      */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*      */ import net.minecraft.client.resources.IResourceManager;
/*      */ import net.minecraft.client.resources.IResourceManagerReloadListener;
/*      */ import net.minecraft.client.settings.GameSettings;
/*      */ import net.minecraft.client.shader.ShaderGroup;
/*      */ import net.minecraft.client.shader.ShaderLinkHelper;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.monster.EntityCreeper;
/*      */ import net.minecraft.entity.monster.EntitySpider;
/*      */ import net.minecraft.entity.passive.EntityAnimal;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.PlayerCapabilities;
/*      */ import net.minecraft.inventory.IInventory;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.potion.Potion;
/*      */ import net.minecraft.potion.PotionEffect;
/*      */ import net.minecraft.profiler.Profiler;
/*      */ import net.minecraft.server.integrated.IntegratedServer;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*      */ import net.minecraft.util.ChatComponentText;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.EnumWorldBlockLayer;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.MouseFilter;
/*      */ import net.minecraft.util.MouseHelper;
/*      */ import net.minecraft.util.MovingObjectPosition;
/*      */ import net.minecraft.util.MovingObjectPosition.MovingObjectType;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldProvider;
/*      */ import net.minecraft.world.biome.BiomeGenBase;
/*      */ import optfine.Config;
/*      */ import optfine.CustomColorizer;
/*      */ import optfine.Lagometer;
/*      */ import optfine.Lagometer.TimerNano;
/*      */ import optfine.Reflector;
/*      */ import optfine.ReflectorMethod;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ import org.lwjgl.input.Mouse;
/*      */ import org.lwjgl.opengl.ContextCapabilities;
/*      */ import org.lwjgl.opengl.Display;
/*      */ import org.lwjgl.opengl.GL11;
/*      */ import org.lwjgl.opengl.GLContext;
/*      */ import org.lwjgl.util.glu.Project;
/*      */ import rip.jutting.polaris.event.events.Event3D;
/*      */ 
/*      */ public class EntityRenderer implements IResourceManagerReloadListener
/*      */ {
/*   90 */   private static final Logger logger = ;
/*   91 */   private static final ResourceLocation locationRainPng = new ResourceLocation("textures/environment/rain.png");
/*   92 */   private static final ResourceLocation locationSnowPng = new ResourceLocation("textures/environment/snow.png");
/*      */   
/*      */   public static boolean anaglyphEnable;
/*      */   
/*      */   public static int anaglyphField;
/*      */   
/*      */   private Minecraft mc;
/*      */   
/*      */   private final IResourceManager resourceManager;
/*  101 */   private Random random = new Random();
/*      */   
/*      */   private float farPlaneDistance;
/*      */   
/*      */   public ItemRenderer itemRenderer;
/*      */   
/*      */   private final MapItemRenderer theMapItemRenderer;
/*      */   
/*      */   private int rendererUpdateCount;
/*      */   private Entity pointedEntity;
/*  111 */   private MouseFilter mouseFilterXAxis = new MouseFilter();
/*  112 */   private MouseFilter mouseFilterYAxis = new MouseFilter();
/*  113 */   private float thirdPersonDistance = 4.0F;
/*      */   
/*      */ 
/*  116 */   private float thirdPersonDistanceTemp = 4.0F;
/*      */   
/*      */ 
/*      */   private float smoothCamYaw;
/*      */   
/*      */ 
/*      */   private float smoothCamPitch;
/*      */   
/*      */ 
/*      */   private float smoothCamFilterX;
/*      */   
/*      */ 
/*      */   private float smoothCamFilterY;
/*      */   
/*      */ 
/*      */   private float smoothCamPartialTicks;
/*      */   
/*      */   private float fovModifierHand;
/*      */   
/*      */   private float fovModifierHandPrev;
/*      */   
/*      */   private float bossColorModifier;
/*      */   
/*      */   private float bossColorModifierPrev;
/*      */   
/*      */   private boolean cloudFog;
/*      */   
/*  143 */   private boolean renderHand = true;
/*  144 */   private boolean drawBlockOutline = true;
/*      */   
/*      */ 
/*  147 */   private long prevFrameTime = Minecraft.getSystemTime();
/*      */   
/*      */ 
/*      */   private long renderEndNanoTime;
/*      */   
/*      */ 
/*      */   private final DynamicTexture lightmapTexture;
/*      */   
/*      */ 
/*      */   private final int[] lightmapColors;
/*      */   
/*      */ 
/*      */   private final ResourceLocation locationLightMap;
/*      */   
/*      */ 
/*      */   private boolean lightmapUpdateNeeded;
/*      */   
/*      */ 
/*      */   private float torchFlickerX;
/*      */   
/*      */ 
/*      */   private float torchFlickerDX;
/*      */   
/*      */ 
/*      */   private int rainSoundCounter;
/*      */   
/*      */ 
/*  174 */   private float[] rainXCoords = new float['Ѐ'];
/*  175 */   private float[] rainYCoords = new float['Ѐ'];
/*      */   
/*      */ 
/*  178 */   private FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
/*      */   
/*      */   public float fogColorRed;
/*      */   
/*      */   public float fogColorGreen;
/*      */   
/*      */   public float fogColorBlue;
/*      */   
/*      */   private float fogColor2;
/*      */   private float fogColor1;
/*  188 */   private int debugViewDirection = 0;
/*  189 */   private boolean debugView = false;
/*  190 */   private double cameraZoom = 1.0D;
/*      */   private double cameraYaw;
/*      */   private double cameraPitch;
/*      */   public ShaderGroup theShaderGroup;
/*  194 */   private static final ResourceLocation[] shaderResourceLocations = { new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"), new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"), new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"), new ResourceLocation("shaders/post/color_convolve.json"), new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"), new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"), new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"), new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"), new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"), new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"), new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"), new ResourceLocation("shaders/post/antialias.json"), new ResourceLocation("shaders/post/creeper.json"), new ResourceLocation("shaders/post/spider.json") };
/*  195 */   public static final int shaderCount = shaderResourceLocations.length;
/*      */   private int shaderIndex;
/*      */   private boolean useShader;
/*      */   private int frameCount;
/*      */   private static final String __OBFID = "CL_00000947";
/*  200 */   private boolean initialized = false;
/*  201 */   private World updatedWorld = null;
/*  202 */   private boolean showDebugInfo = false;
/*  203 */   public boolean fogStandard = false;
/*  204 */   private float clipDistance = 128.0F;
/*  205 */   private long lastServerTime = 0L;
/*  206 */   private int lastServerTicks = 0;
/*  207 */   private int serverWaitTime = 0;
/*  208 */   private int serverWaitTimeCurrent = 0;
/*  209 */   private float avgServerTimeDiff = 0.0F;
/*  210 */   private float avgServerTickDiff = 0.0F;
/*  211 */   private long lastErrorCheckTimeMs = 0L;
/*      */   
/*      */   public EntityRenderer(Minecraft mcIn, IResourceManager resourceManagerIn)
/*      */   {
/*  215 */     this.shaderIndex = shaderCount;
/*  216 */     this.useShader = false;
/*  217 */     this.frameCount = 0;
/*  218 */     this.mc = mcIn;
/*  219 */     this.resourceManager = resourceManagerIn;
/*  220 */     this.itemRenderer = mcIn.getItemRenderer();
/*  221 */     this.theMapItemRenderer = new MapItemRenderer(mcIn.getTextureManager());
/*  222 */     this.lightmapTexture = new DynamicTexture(16, 16);
/*  223 */     this.locationLightMap = mcIn.getTextureManager().getDynamicTextureLocation("lightMap", this.lightmapTexture);
/*  224 */     this.lightmapColors = this.lightmapTexture.getTextureData();
/*  225 */     this.theShaderGroup = null;
/*      */     
/*  227 */     for (int i = 0; i < 32; i++)
/*      */     {
/*  229 */       for (int j = 0; j < 32; j++)
/*      */       {
/*  231 */         float f = j - 16;
/*  232 */         float f1 = i - 16;
/*  233 */         float f2 = MathHelper.sqrt_float(f * f + f1 * f1);
/*  234 */         this.rainXCoords[(i << 5 | j)] = (-f1 / f2);
/*  235 */         this.rainYCoords[(i << 5 | j)] = (f / f2);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isShaderActive()
/*      */   {
/*  242 */     return (OpenGlHelper.shadersSupported) && (this.theShaderGroup != null);
/*      */   }
/*      */   
/*      */   public void func_181022_b()
/*      */   {
/*  247 */     if (this.theShaderGroup != null)
/*      */     {
/*  249 */       this.theShaderGroup.deleteShaderGroup();
/*      */     }
/*      */     
/*  252 */     this.theShaderGroup = null;
/*  253 */     this.shaderIndex = shaderCount;
/*      */   }
/*      */   
/*      */   public void switchUseShader()
/*      */   {
/*  258 */     this.useShader = (!this.useShader);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void loadEntityShader(Entity entityIn)
/*      */   {
/*  266 */     if (OpenGlHelper.shadersSupported)
/*      */     {
/*  268 */       if (this.theShaderGroup != null)
/*      */       {
/*  270 */         this.theShaderGroup.deleteShaderGroup();
/*      */       }
/*      */       
/*  273 */       this.theShaderGroup = null;
/*      */       
/*  275 */       if ((entityIn instanceof EntityCreeper))
/*      */       {
/*  277 */         loadShader(new ResourceLocation("shaders/post/creeper.json"));
/*      */       }
/*  279 */       else if ((entityIn instanceof EntitySpider))
/*      */       {
/*  281 */         loadShader(new ResourceLocation("shaders/post/spider.json"));
/*      */       }
/*  283 */       else if ((entityIn instanceof net.minecraft.entity.monster.EntityEnderman))
/*      */       {
/*  285 */         loadShader(new ResourceLocation("shaders/post/invert.json"));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void activateNextShader()
/*      */   {
/*  292 */     if ((OpenGlHelper.shadersSupported) && ((this.mc.getRenderViewEntity() instanceof EntityPlayer)))
/*      */     {
/*  294 */       if (this.theShaderGroup != null)
/*      */       {
/*  296 */         this.theShaderGroup.deleteShaderGroup();
/*      */       }
/*      */       
/*  299 */       this.shaderIndex = ((this.shaderIndex + 1) % (shaderResourceLocations.length + 1));
/*      */       
/*  301 */       if (this.shaderIndex != shaderCount)
/*      */       {
/*  303 */         loadShader(shaderResourceLocations[this.shaderIndex]);
/*      */       }
/*      */       else
/*      */       {
/*  307 */         this.theShaderGroup = null;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void loadShader(ResourceLocation resourceLocationIn)
/*      */   {
/*      */     try
/*      */     {
/*  316 */       this.theShaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.resourceManager, this.mc.getFramebuffer(), resourceLocationIn);
/*  317 */       this.theShaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
/*  318 */       this.useShader = true;
/*      */     }
/*      */     catch (IOException ioexception)
/*      */     {
/*  322 */       logger.warn("Failed to load shader: " + resourceLocationIn, ioexception);
/*  323 */       this.shaderIndex = shaderCount;
/*  324 */       this.useShader = false;
/*      */     }
/*      */     catch (JsonSyntaxException jsonsyntaxexception)
/*      */     {
/*  328 */       logger.warn("Failed to load shader: " + resourceLocationIn, jsonsyntaxexception);
/*  329 */       this.shaderIndex = shaderCount;
/*  330 */       this.useShader = false;
/*      */     }
/*      */   }
/*      */   
/*      */   public void onResourceManagerReload(IResourceManager resourceManager)
/*      */   {
/*  336 */     if (this.theShaderGroup != null)
/*      */     {
/*  338 */       this.theShaderGroup.deleteShaderGroup();
/*      */     }
/*      */     
/*  341 */     this.theShaderGroup = null;
/*      */     
/*  343 */     if (this.shaderIndex != shaderCount)
/*      */     {
/*  345 */       loadShader(shaderResourceLocations[this.shaderIndex]);
/*      */     }
/*      */     else
/*      */     {
/*  349 */       loadEntityShader(this.mc.getRenderViewEntity());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateRenderer()
/*      */   {
/*  358 */     if ((OpenGlHelper.shadersSupported) && (ShaderLinkHelper.getStaticShaderLinkHelper() == null))
/*      */     {
/*  360 */       ShaderLinkHelper.setNewStaticShaderLinkHelper();
/*      */     }
/*      */     
/*  363 */     updateFovModifierHand();
/*  364 */     updateTorchFlicker();
/*  365 */     this.fogColor2 = this.fogColor1;
/*  366 */     this.thirdPersonDistanceTemp = this.thirdPersonDistance;
/*      */     
/*  368 */     if (this.mc.gameSettings.smoothCamera)
/*      */     {
/*  370 */       float f = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
/*  371 */       float f1 = f * f * f * 8.0F;
/*  372 */       this.smoothCamFilterX = this.mouseFilterXAxis.smooth(this.smoothCamYaw, 0.05F * f1);
/*  373 */       this.smoothCamFilterY = this.mouseFilterYAxis.smooth(this.smoothCamPitch, 0.05F * f1);
/*  374 */       this.smoothCamPartialTicks = 0.0F;
/*  375 */       this.smoothCamYaw = 0.0F;
/*  376 */       this.smoothCamPitch = 0.0F;
/*      */     }
/*      */     else
/*      */     {
/*  380 */       this.smoothCamFilterX = 0.0F;
/*  381 */       this.smoothCamFilterY = 0.0F;
/*  382 */       this.mouseFilterXAxis.reset();
/*  383 */       this.mouseFilterYAxis.reset();
/*      */     }
/*      */     
/*  386 */     if (this.mc.getRenderViewEntity() == null)
/*      */     {
/*  388 */       this.mc.setRenderViewEntity(this.mc.thePlayer);
/*      */     }
/*      */     
/*  391 */     float f3 = this.mc.theWorld.getLightBrightness(new BlockPos(this.mc.getRenderViewEntity()));
/*  392 */     float f4 = this.mc.gameSettings.renderDistanceChunks / 32.0F;
/*  393 */     float f2 = f3 * (1.0F - f4) + f4;
/*  394 */     this.fogColor1 += (f2 - this.fogColor1) * 0.1F;
/*  395 */     this.rendererUpdateCount += 1;
/*  396 */     this.itemRenderer.updateEquippedItem();
/*  397 */     addRainParticles();
/*  398 */     this.bossColorModifierPrev = this.bossColorModifier;
/*      */     
/*  400 */     if (net.minecraft.entity.boss.BossStatus.hasColorModifier)
/*      */     {
/*  402 */       this.bossColorModifier += 0.05F;
/*      */       
/*  404 */       if (this.bossColorModifier > 1.0F)
/*      */       {
/*  406 */         this.bossColorModifier = 1.0F;
/*      */       }
/*      */       
/*  409 */       net.minecraft.entity.boss.BossStatus.hasColorModifier = false;
/*      */     }
/*  411 */     else if (this.bossColorModifier > 0.0F)
/*      */     {
/*  413 */       this.bossColorModifier -= 0.0125F;
/*      */     }
/*      */   }
/*      */   
/*      */   public ShaderGroup getShaderGroup()
/*      */   {
/*  419 */     return this.theShaderGroup;
/*      */   }
/*      */   
/*      */   public void updateShaderGroupSize(int width, int height)
/*      */   {
/*  424 */     if (OpenGlHelper.shadersSupported)
/*      */     {
/*  426 */       if (this.theShaderGroup != null)
/*      */       {
/*  428 */         this.theShaderGroup.createBindFramebuffers(width, height);
/*      */       }
/*      */       
/*  431 */       this.mc.renderGlobal.createBindEntityOutlineFbs(width, height);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void getMouseOver(float partialTicks)
/*      */   {
/*  440 */     Entity entity = this.mc.getRenderViewEntity();
/*      */     
/*  442 */     if ((entity != null) && (this.mc.theWorld != null))
/*      */     {
/*  444 */       this.mc.mcProfiler.startSection("pick");
/*  445 */       this.mc.pointedEntity = null;
/*  446 */       double d0 = this.mc.playerController.getBlockReachDistance();
/*  447 */       this.mc.objectMouseOver = entity.rayTrace(d0, partialTicks);
/*  448 */       double d1 = d0;
/*  449 */       Vec3 vec3 = entity.getPositionEyes(partialTicks);
/*  450 */       boolean flag = false;
/*  451 */       boolean flag1 = true;
/*      */       
/*  453 */       if (this.mc.playerController.extendedReach())
/*      */       {
/*  455 */         d0 = 6.0D;
/*  456 */         d1 = 6.0D;
/*      */       }
/*      */       else
/*      */       {
/*  460 */         if (d0 > 3.0D)
/*      */         {
/*  462 */           flag = true;
/*      */         }
/*      */         
/*  465 */         d0 = d0;
/*      */       }
/*      */       
/*  468 */       if (this.mc.objectMouseOver != null)
/*      */       {
/*  470 */         d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
/*      */       }
/*      */       
/*  473 */       Vec3 vec31 = entity.getLook(partialTicks);
/*  474 */       Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
/*  475 */       this.pointedEntity = null;
/*  476 */       Vec3 vec33 = null;
/*  477 */       float f = 1.0F;
/*  478 */       List list = this.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand(f, f, f), Predicates.and(net.minecraft.util.EntitySelectors.NOT_SPECTATING, new EntityRenderer1(this)));
/*  479 */       double d2 = d1;
/*      */       
/*  481 */       for (int i = 0; i < list.size(); i++)
/*      */       {
/*  483 */         Entity entity1 = (Entity)list.get(i);
/*  484 */         float f1 = entity1.getCollisionBorderSize();
/*  485 */         AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
/*  486 */         MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
/*      */         
/*  488 */         if (axisalignedbb.isVecInside(vec3))
/*      */         {
/*  490 */           if (d2 >= 0.0D)
/*      */           {
/*  492 */             this.pointedEntity = entity1;
/*  493 */             vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
/*  494 */             d2 = 0.0D;
/*      */           }
/*      */         }
/*  497 */         else if (movingobjectposition != null)
/*      */         {
/*  499 */           double d3 = vec3.distanceTo(movingobjectposition.hitVec);
/*      */           
/*  501 */           if ((d3 < d2) || (d2 == 0.0D))
/*      */           {
/*  503 */             boolean flag2 = false;
/*      */             
/*  505 */             if (Reflector.ForgeEntity_canRiderInteract.exists())
/*      */             {
/*  507 */               flag2 = Reflector.callBoolean(entity1, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
/*      */             }
/*      */             
/*  510 */             if ((entity1 == entity.ridingEntity) && (!flag2))
/*      */             {
/*  512 */               if (d2 == 0.0D)
/*      */               {
/*  514 */                 this.pointedEntity = entity1;
/*  515 */                 vec33 = movingobjectposition.hitVec;
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/*  520 */               this.pointedEntity = entity1;
/*  521 */               vec33 = movingobjectposition.hitVec;
/*  522 */               d2 = d3;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  528 */       if ((this.pointedEntity != null) && (flag) && (vec3.distanceTo(vec33) > 3.0D))
/*      */       {
/*  530 */         this.pointedEntity = null;
/*  531 */         this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, null, new BlockPos(vec33));
/*      */       }
/*      */       
/*  534 */       if ((this.pointedEntity != null) && ((d2 < d1) || (this.mc.objectMouseOver == null)))
/*      */       {
/*  536 */         this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, vec33);
/*      */         
/*  538 */         if (((this.pointedEntity instanceof EntityLivingBase)) || ((this.pointedEntity instanceof net.minecraft.entity.item.EntityItemFrame)))
/*      */         {
/*  540 */           this.mc.pointedEntity = this.pointedEntity;
/*      */         }
/*      */       }
/*      */       
/*  544 */       this.mc.mcProfiler.endSection();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateFovModifierHand()
/*      */   {
/*  553 */     float f = 1.0F;
/*      */     
/*  555 */     if ((this.mc.getRenderViewEntity() instanceof AbstractClientPlayer))
/*      */     {
/*  557 */       AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)this.mc.getRenderViewEntity();
/*  558 */       f = abstractclientplayer.getFovModifier();
/*      */     }
/*      */     
/*  561 */     this.fovModifierHandPrev = this.fovModifierHand;
/*  562 */     this.fovModifierHand += (f - this.fovModifierHand) * 0.5F;
/*      */     
/*  564 */     if (this.fovModifierHand > 1.5F)
/*      */     {
/*  566 */       this.fovModifierHand = 1.5F;
/*      */     }
/*      */     
/*  569 */     if (this.fovModifierHand < 0.1F)
/*      */     {
/*  571 */       this.fovModifierHand = 0.1F;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private float getFOVModifier(float partialTicks, boolean p_78481_2_)
/*      */   {
/*  580 */     if (this.debugView)
/*      */     {
/*  582 */       return 90.0F;
/*      */     }
/*      */     
/*      */ 
/*  586 */     Entity entity = this.mc.getRenderViewEntity();
/*  587 */     float f = 70.0F;
/*      */     
/*  589 */     if (p_78481_2_)
/*      */     {
/*  591 */       f = this.mc.gameSettings.fovSetting;
/*  592 */       f *= (this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * partialTicks);
/*      */     }
/*      */     
/*  595 */     boolean flag = false;
/*      */     
/*  597 */     if (this.mc.currentScreen == null)
/*      */     {
/*  599 */       GameSettings gamesettings = this.mc.gameSettings;
/*  600 */       flag = GameSettings.isKeyDown(this.mc.gameSettings.ofKeyBindZoom);
/*      */     }
/*      */     
/*  603 */     if (flag)
/*      */     {
/*  605 */       if (!Config.zoomMode)
/*      */       {
/*  607 */         Config.zoomMode = true;
/*  608 */         this.mc.gameSettings.smoothCamera = true;
/*      */       }
/*      */       
/*  611 */       if (Config.zoomMode)
/*      */       {
/*  613 */         f /= 4.0F;
/*      */       }
/*      */     }
/*  616 */     else if (Config.zoomMode)
/*      */     {
/*  618 */       Config.zoomMode = false;
/*  619 */       this.mc.gameSettings.smoothCamera = false;
/*  620 */       this.mouseFilterXAxis = new MouseFilter();
/*  621 */       this.mouseFilterYAxis = new MouseFilter();
/*  622 */       this.mc.renderGlobal.displayListEntitiesDirty = true;
/*      */     }
/*      */     
/*  625 */     if (((entity instanceof EntityLivingBase)) && (((EntityLivingBase)entity).getHealth() <= 0.0F))
/*      */     {
/*  627 */       float f1 = ((EntityLivingBase)entity).deathTime + partialTicks;
/*  628 */       f /= ((1.0F - 500.0F / (f1 + 500.0F)) * 2.0F + 1.0F);
/*      */     }
/*      */     
/*  631 */     Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);
/*      */     
/*  633 */     if (block.getMaterial() == Material.water)
/*      */     {
/*  635 */       f = f * 60.0F / 70.0F;
/*      */     }
/*      */     
/*  638 */     return f;
/*      */   }
/*      */   
/*      */ 
/*      */   private void hurtCameraEffect(float partialTicks)
/*      */   {
/*  644 */     if ((this.mc.getRenderViewEntity() instanceof EntityLivingBase))
/*      */     {
/*  646 */       EntityLivingBase entitylivingbase = (EntityLivingBase)this.mc.getRenderViewEntity();
/*  647 */       float f = entitylivingbase.hurtTime - partialTicks;
/*      */       
/*  649 */       if (entitylivingbase.getHealth() <= 0.0F)
/*      */       {
/*  651 */         float f1 = entitylivingbase.deathTime + partialTicks;
/*  652 */         GlStateManager.rotate(40.0F - 8000.0F / (f1 + 200.0F), 0.0F, 0.0F, 1.0F);
/*      */       }
/*      */       
/*  655 */       if (f < 0.0F)
/*      */       {
/*  657 */         return;
/*      */       }
/*      */       
/*  660 */       f /= entitylivingbase.maxHurtTime;
/*  661 */       f = MathHelper.sin(f * f * f * f * 3.1415927F);
/*  662 */       float f2 = entitylivingbase.attackedAtYaw;
/*  663 */       GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
/*  664 */       GlStateManager.rotate(-f * 14.0F, 0.0F, 0.0F, 1.0F);
/*  665 */       GlStateManager.rotate(f2, 0.0F, 1.0F, 0.0F);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setupViewBobbing(float partialTicks)
/*      */   {
/*  674 */     if ((this.mc.getRenderViewEntity() instanceof EntityPlayer))
/*      */     {
/*  676 */       EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
/*  677 */       float f = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
/*  678 */       float f1 = -(entityplayer.distanceWalkedModified + f * partialTicks);
/*  679 */       float f2 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * partialTicks;
/*  680 */       float f3 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * partialTicks;
/*  681 */       GlStateManager.translate(MathHelper.sin(f1 * 3.1415927F) * f2 * 0.5F, -Math.abs(MathHelper.cos(f1 * 3.1415927F) * f2), 0.0F);
/*  682 */       GlStateManager.rotate(MathHelper.sin(f1 * 3.1415927F) * f2 * 3.0F, 0.0F, 0.0F, 1.0F);
/*  683 */       GlStateManager.rotate(Math.abs(MathHelper.cos(f1 * 3.1415927F - 0.2F) * f2) * 5.0F, 1.0F, 0.0F, 0.0F);
/*  684 */       GlStateManager.rotate(f3, 1.0F, 0.0F, 0.0F);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void orientCamera(float partialTicks)
/*      */   {
/*  693 */     Entity entity = this.mc.getRenderViewEntity();
/*  694 */     float f = entity.getEyeHeight();
/*  695 */     double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
/*  696 */     double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + f;
/*  697 */     double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
/*      */     
/*  699 */     if (((entity instanceof EntityLivingBase)) && (((EntityLivingBase)entity).isPlayerSleeping()))
/*      */     {
/*  701 */       f = (float)(f + 1.0D);
/*  702 */       GlStateManager.translate(0.0F, 0.3F, 0.0F);
/*      */       
/*  704 */       if (!this.mc.gameSettings.debugCamEnable)
/*      */       {
/*  706 */         BlockPos blockpos = new BlockPos(entity);
/*  707 */         IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
/*  708 */         Block block = iblockstate.getBlock();
/*      */         
/*  710 */         if (Reflector.ForgeHooksClient_orientBedCamera.exists())
/*      */         {
/*  712 */           Reflector.callVoid(Reflector.ForgeHooksClient_orientBedCamera, new Object[] { this.mc.theWorld, blockpos, iblockstate, entity });
/*      */         }
/*  714 */         else if (block == net.minecraft.init.Blocks.bed)
/*      */         {
/*  716 */           int j = ((EnumFacing)iblockstate.getValue(BlockBed.FACING)).getHorizontalIndex();
/*  717 */           GlStateManager.rotate(j * 90, 0.0F, 1.0F, 0.0F);
/*      */         }
/*      */         
/*  720 */         GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F, 0.0F, -1.0F, 0.0F);
/*  721 */         GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1.0F, 0.0F, 0.0F);
/*      */       }
/*      */     }
/*  724 */     else if (this.mc.gameSettings.thirdPersonView > 0)
/*      */     {
/*  726 */       double d3 = this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * partialTicks;
/*      */       
/*  728 */       if (this.mc.gameSettings.debugCamEnable)
/*      */       {
/*  730 */         GlStateManager.translate(0.0F, 0.0F, (float)-d3);
/*      */       }
/*      */       else
/*      */       {
/*  734 */         float f1 = entity.rotationYaw;
/*  735 */         float f2 = entity.rotationPitch;
/*      */         
/*  737 */         if (this.mc.gameSettings.thirdPersonView == 2)
/*      */         {
/*  739 */           f2 += 180.0F;
/*      */         }
/*      */         
/*  742 */         double d4 = -MathHelper.sin(f1 / 180.0F * 3.1415927F) * MathHelper.cos(f2 / 180.0F * 3.1415927F) * d3;
/*  743 */         double d5 = MathHelper.cos(f1 / 180.0F * 3.1415927F) * MathHelper.cos(f2 / 180.0F * 3.1415927F) * d3;
/*  744 */         double d6 = -MathHelper.sin(f2 / 180.0F * 3.1415927F) * d3;
/*      */         
/*  746 */         for (int i = 0; i < 8; i++)
/*      */         {
/*  748 */           float f3 = (i & 0x1) * 2 - 1;
/*  749 */           float f4 = (i >> 1 & 0x1) * 2 - 1;
/*  750 */           float f5 = (i >> 2 & 0x1) * 2 - 1;
/*  751 */           f3 *= 0.1F;
/*  752 */           f4 *= 0.1F;
/*  753 */           f5 *= 0.1F;
/*  754 */           MovingObjectPosition movingobjectposition = this.mc.theWorld.rayTraceBlocks(new Vec3(d0 + f3, d1 + f4, d2 + f5), new Vec3(d0 - d4 + f3 + f5, d1 - d6 + f4, d2 - d5 + f5));
/*      */           
/*  756 */           if (movingobjectposition != null)
/*      */           {
/*  758 */             double d7 = movingobjectposition.hitVec.distanceTo(new Vec3(d0, d1, d2));
/*      */             
/*  760 */             if (d7 < d3)
/*      */             {
/*  762 */               d3 = d7;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  767 */         if (this.mc.gameSettings.thirdPersonView == 2)
/*      */         {
/*  769 */           GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
/*      */         }
/*      */         
/*  772 */         GlStateManager.rotate(entity.rotationPitch - f2, 1.0F, 0.0F, 0.0F);
/*  773 */         GlStateManager.rotate(entity.rotationYaw - f1, 0.0F, 1.0F, 0.0F);
/*  774 */         GlStateManager.translate(0.0F, 0.0F, (float)-d3);
/*  775 */         GlStateManager.rotate(f1 - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
/*  776 */         GlStateManager.rotate(f2 - entity.rotationPitch, 1.0F, 0.0F, 0.0F);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  781 */       GlStateManager.translate(0.0F, 0.0F, -0.1F);
/*      */     }
/*      */     
/*  784 */     if (!this.mc.gameSettings.debugCamEnable)
/*      */     {
/*  786 */       GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 1.0F, 0.0F, 0.0F);
/*      */       
/*  788 */       if ((entity instanceof EntityAnimal))
/*      */       {
/*  790 */         EntityAnimal entityanimal = (EntityAnimal)entity;
/*  791 */         GlStateManager.rotate(entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0F, 0.0F, 1.0F, 0.0F);
/*      */       }
/*      */       else
/*      */       {
/*  795 */         GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F, 0.0F, 1.0F, 0.0F);
/*      */       }
/*      */     }
/*      */     
/*  799 */     GlStateManager.translate(0.0F, -f, 0.0F);
/*  800 */     d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
/*  801 */     d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + f;
/*  802 */     d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
/*  803 */     this.cloudFog = this.mc.renderGlobal.hasCloudFog(d0, d1, d2, partialTicks);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setupCameraTransform(float partialTicks, int pass)
/*      */   {
/*  811 */     this.farPlaneDistance = (this.mc.gameSettings.renderDistanceChunks * 16);
/*      */     
/*  813 */     if (Config.isFogFancy())
/*      */     {
/*  815 */       this.farPlaneDistance *= 0.95F;
/*      */     }
/*      */     
/*  818 */     if (Config.isFogFast())
/*      */     {
/*  820 */       this.farPlaneDistance *= 0.83F;
/*      */     }
/*      */     
/*  823 */     GlStateManager.matrixMode(5889);
/*  824 */     GlStateManager.loadIdentity();
/*  825 */     float f = 0.07F;
/*      */     
/*  827 */     if (this.mc.gameSettings.anaglyph)
/*      */     {
/*  829 */       GlStateManager.translate(-(pass * 2 - 1) * f, 0.0F, 0.0F);
/*      */     }
/*      */     
/*  832 */     this.clipDistance = (this.farPlaneDistance * 2.0F);
/*      */     
/*  834 */     if (this.clipDistance < 173.0F)
/*      */     {
/*  836 */       this.clipDistance = 173.0F;
/*      */     }
/*      */     
/*  839 */     if (this.mc.theWorld.provider.getDimensionId() == 1)
/*      */     {
/*  841 */       this.clipDistance = 256.0F;
/*      */     }
/*      */     
/*  844 */     if (this.cameraZoom != 1.0D)
/*      */     {
/*  846 */       GlStateManager.translate((float)this.cameraYaw, (float)-this.cameraPitch, 0.0F);
/*  847 */       GlStateManager.scale(this.cameraZoom, this.cameraZoom, 1.0D);
/*      */     }
/*      */     
/*  850 */     Project.gluPerspective(getFOVModifier(partialTicks, true), this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
/*  851 */     GlStateManager.matrixMode(5888);
/*  852 */     GlStateManager.loadIdentity();
/*      */     
/*  854 */     if (this.mc.gameSettings.anaglyph)
/*      */     {
/*  856 */       GlStateManager.translate((pass * 2 - 1) * 0.1F, 0.0F, 0.0F);
/*      */     }
/*      */     
/*  859 */     hurtCameraEffect(partialTicks);
/*      */     
/*  861 */     if (this.mc.gameSettings.viewBobbing)
/*      */     {
/*  863 */       setupViewBobbing(partialTicks);
/*      */     }
/*      */     
/*  866 */     float f1 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * partialTicks;
/*      */     
/*  868 */     if (f1 > 0.0F)
/*      */     {
/*  870 */       byte b0 = 20;
/*      */       
/*  872 */       if (this.mc.thePlayer.isPotionActive(Potion.confusion))
/*      */       {
/*  874 */         b0 = 7;
/*      */       }
/*      */       
/*  877 */       float f2 = 5.0F / (f1 * f1 + 5.0F) - f1 * 0.04F;
/*  878 */       f2 *= f2;
/*  879 */       GlStateManager.rotate((this.rendererUpdateCount + partialTicks) * b0, 0.0F, 1.0F, 1.0F);
/*  880 */       GlStateManager.scale(1.0F / f2, 1.0F, 1.0F);
/*  881 */       GlStateManager.rotate(-(this.rendererUpdateCount + partialTicks) * b0, 0.0F, 1.0F, 1.0F);
/*      */     }
/*      */     
/*  884 */     orientCamera(partialTicks);
/*      */     
/*  886 */     if (this.debugView)
/*      */     {
/*  888 */       switch (this.debugViewDirection)
/*      */       {
/*      */       case 0: 
/*  891 */         GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
/*  892 */         break;
/*      */       
/*      */       case 1: 
/*  895 */         GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
/*  896 */         break;
/*      */       
/*      */       case 2: 
/*  899 */         GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
/*  900 */         break;
/*      */       
/*      */       case 3: 
/*  903 */         GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
/*  904 */         break;
/*      */       
/*      */       case 4: 
/*  907 */         GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
/*      */       }
/*      */       
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void renderHand(float partialTicks, int xOffset)
/*      */   {
/*  917 */     if (!this.debugView)
/*      */     {
/*  919 */       GlStateManager.matrixMode(5889);
/*  920 */       GlStateManager.loadIdentity();
/*  921 */       float f = 0.07F;
/*      */       
/*  923 */       if (this.mc.gameSettings.anaglyph)
/*      */       {
/*  925 */         GlStateManager.translate(-(xOffset * 2 - 1) * f, 0.0F, 0.0F);
/*      */       }
/*      */       
/*  928 */       Project.gluPerspective(getFOVModifier(partialTicks, false), this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
/*  929 */       GlStateManager.matrixMode(5888);
/*  930 */       GlStateManager.loadIdentity();
/*      */       
/*  932 */       if (this.mc.gameSettings.anaglyph)
/*      */       {
/*  934 */         GlStateManager.translate((xOffset * 2 - 1) * 0.1F, 0.0F, 0.0F);
/*      */       }
/*      */       
/*  937 */       GlStateManager.pushMatrix();
/*  938 */       hurtCameraEffect(partialTicks);
/*      */       
/*  940 */       if (this.mc.gameSettings.viewBobbing)
/*      */       {
/*  942 */         setupViewBobbing(partialTicks);
/*      */       }
/*      */       
/*  945 */       boolean flag = ((this.mc.getRenderViewEntity() instanceof EntityLivingBase)) && (((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping());
/*      */       
/*  947 */       if ((this.mc.gameSettings.thirdPersonView == 0) && (!flag) && (!this.mc.gameSettings.hideGUI) && (!this.mc.playerController.isSpectator()))
/*      */       {
/*  949 */         enableLightmap();
/*  950 */         this.itemRenderer.renderItemInFirstPerson(partialTicks);
/*  951 */         disableLightmap();
/*      */       }
/*      */       
/*  954 */       GlStateManager.popMatrix();
/*      */       
/*  956 */       if ((this.mc.gameSettings.thirdPersonView == 0) && (!flag))
/*      */       {
/*  958 */         this.itemRenderer.renderOverlays(partialTicks);
/*  959 */         hurtCameraEffect(partialTicks);
/*      */       }
/*      */       
/*  962 */       if (this.mc.gameSettings.viewBobbing)
/*      */       {
/*  964 */         setupViewBobbing(partialTicks);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void disableLightmap()
/*      */   {
/*  971 */     GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
/*  972 */     GlStateManager.disableTexture2D();
/*  973 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/*      */   }
/*      */   
/*      */   public void enableLightmap()
/*      */   {
/*  978 */     GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
/*  979 */     GlStateManager.matrixMode(5890);
/*  980 */     GlStateManager.loadIdentity();
/*  981 */     float f = 0.00390625F;
/*  982 */     GlStateManager.scale(f, f, f);
/*  983 */     GlStateManager.translate(8.0F, 8.0F, 8.0F);
/*  984 */     GlStateManager.matrixMode(5888);
/*  985 */     this.mc.getTextureManager().bindTexture(this.locationLightMap);
/*  986 */     GL11.glTexParameteri(3553, 10241, 9729);
/*  987 */     GL11.glTexParameteri(3553, 10240, 9729);
/*  988 */     GL11.glTexParameteri(3553, 10242, 10496);
/*  989 */     GL11.glTexParameteri(3553, 10243, 10496);
/*  990 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  991 */     GlStateManager.enableTexture2D();
/*  992 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateTorchFlicker()
/*      */   {
/* 1000 */     this.torchFlickerDX = ((float)(this.torchFlickerDX + (Math.random() - Math.random()) * Math.random() * Math.random()));
/* 1001 */     this.torchFlickerDX = ((float)(this.torchFlickerDX * 0.9D));
/* 1002 */     this.torchFlickerX += (this.torchFlickerDX - this.torchFlickerX) * 1.0F;
/* 1003 */     this.lightmapUpdateNeeded = true;
/*      */   }
/*      */   
/*      */   private void updateLightmap(float partialTicks)
/*      */   {
/* 1008 */     if (this.lightmapUpdateNeeded)
/*      */     {
/* 1010 */       this.mc.mcProfiler.startSection("lightTex");
/* 1011 */       WorldClient worldclient = this.mc.theWorld;
/*      */       
/* 1013 */       if (worldclient != null)
/*      */       {
/* 1015 */         if (CustomColorizer.updateLightmap(worldclient, this.torchFlickerX, this.lightmapColors, this.mc.thePlayer.isPotionActive(Potion.nightVision)))
/*      */         {
/* 1017 */           this.lightmapTexture.updateDynamicTexture();
/* 1018 */           this.lightmapUpdateNeeded = false;
/* 1019 */           this.mc.mcProfiler.endSection();
/* 1020 */           return;
/*      */         }
/*      */         
/* 1023 */         float f = worldclient.getSunBrightness(1.0F);
/* 1024 */         float f1 = f * 0.95F + 0.05F;
/*      */         
/* 1026 */         for (int i = 0; i < 256; i++)
/*      */         {
/* 1028 */           float f2 = worldclient.provider.getLightBrightnessTable()[(i / 16)] * f1;
/* 1029 */           float f3 = worldclient.provider.getLightBrightnessTable()[(i % 16)] * (this.torchFlickerX * 0.1F + 1.5F);
/*      */           
/* 1031 */           if (worldclient.getLastLightningBolt() > 0)
/*      */           {
/* 1033 */             f2 = worldclient.provider.getLightBrightnessTable()[(i / 16)];
/*      */           }
/*      */           
/* 1036 */           float f4 = f2 * (f * 0.65F + 0.35F);
/* 1037 */           float f5 = f2 * (f * 0.65F + 0.35F);
/* 1038 */           float f6 = f3 * ((f3 * 0.6F + 0.4F) * 0.6F + 0.4F);
/* 1039 */           float f7 = f3 * (f3 * f3 * 0.6F + 0.4F);
/* 1040 */           float f8 = f4 + f3;
/* 1041 */           float f9 = f5 + f6;
/* 1042 */           float f10 = f2 + f7;
/* 1043 */           f8 = f8 * 0.96F + 0.03F;
/* 1044 */           f9 = f9 * 0.96F + 0.03F;
/* 1045 */           f10 = f10 * 0.96F + 0.03F;
/*      */           
/* 1047 */           if (this.bossColorModifier > 0.0F)
/*      */           {
/* 1049 */             float f11 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
/* 1050 */             f8 = f8 * (1.0F - f11) + f8 * 0.7F * f11;
/* 1051 */             f9 = f9 * (1.0F - f11) + f9 * 0.6F * f11;
/* 1052 */             f10 = f10 * (1.0F - f11) + f10 * 0.6F * f11;
/*      */           }
/*      */           
/* 1055 */           if (worldclient.provider.getDimensionId() == 1)
/*      */           {
/* 1057 */             f8 = 0.22F + f3 * 0.75F;
/* 1058 */             f9 = 0.28F + f6 * 0.75F;
/* 1059 */             f10 = 0.25F + f7 * 0.75F;
/*      */           }
/*      */           
/* 1062 */           if (this.mc.thePlayer.isPotionActive(Potion.nightVision))
/*      */           {
/* 1064 */             float f15 = getNightVisionBrightness(this.mc.thePlayer, partialTicks);
/* 1065 */             float f12 = 1.0F / f8;
/*      */             
/* 1067 */             if (f12 > 1.0F / f9)
/*      */             {
/* 1069 */               f12 = 1.0F / f9;
/*      */             }
/*      */             
/* 1072 */             if (f12 > 1.0F / f10)
/*      */             {
/* 1074 */               f12 = 1.0F / f10;
/*      */             }
/*      */             
/* 1077 */             f8 = f8 * (1.0F - f15) + f8 * f12 * f15;
/* 1078 */             f9 = f9 * (1.0F - f15) + f9 * f12 * f15;
/* 1079 */             f10 = f10 * (1.0F - f15) + f10 * f12 * f15;
/*      */           }
/*      */           
/* 1082 */           if (f8 > 1.0F)
/*      */           {
/* 1084 */             f8 = 1.0F;
/*      */           }
/*      */           
/* 1087 */           if (f9 > 1.0F)
/*      */           {
/* 1089 */             f9 = 1.0F;
/*      */           }
/*      */           
/* 1092 */           if (f10 > 1.0F)
/*      */           {
/* 1094 */             f10 = 1.0F;
/*      */           }
/*      */           
/* 1097 */           float f16 = this.mc.gameSettings.gammaSetting;
/* 1098 */           float f17 = 1.0F - f8;
/* 1099 */           float f13 = 1.0F - f9;
/* 1100 */           float f14 = 1.0F - f10;
/* 1101 */           f17 = 1.0F - f17 * f17 * f17 * f17;
/* 1102 */           f13 = 1.0F - f13 * f13 * f13 * f13;
/* 1103 */           f14 = 1.0F - f14 * f14 * f14 * f14;
/* 1104 */           f8 = f8 * (1.0F - f16) + f17 * f16;
/* 1105 */           f9 = f9 * (1.0F - f16) + f13 * f16;
/* 1106 */           f10 = f10 * (1.0F - f16) + f14 * f16;
/* 1107 */           f8 = f8 * 0.96F + 0.03F;
/* 1108 */           f9 = f9 * 0.96F + 0.03F;
/* 1109 */           f10 = f10 * 0.96F + 0.03F;
/*      */           
/* 1111 */           if (f8 > 1.0F)
/*      */           {
/* 1113 */             f8 = 1.0F;
/*      */           }
/*      */           
/* 1116 */           if (f9 > 1.0F)
/*      */           {
/* 1118 */             f9 = 1.0F;
/*      */           }
/*      */           
/* 1121 */           if (f10 > 1.0F)
/*      */           {
/* 1123 */             f10 = 1.0F;
/*      */           }
/*      */           
/* 1126 */           if (f8 < 0.0F)
/*      */           {
/* 1128 */             f8 = 0.0F;
/*      */           }
/*      */           
/* 1131 */           if (f9 < 0.0F)
/*      */           {
/* 1133 */             f9 = 0.0F;
/*      */           }
/*      */           
/* 1136 */           if (f10 < 0.0F)
/*      */           {
/* 1138 */             f10 = 0.0F;
/*      */           }
/*      */           
/* 1141 */           short short1 = 255;
/* 1142 */           int j = (int)(f8 * 255.0F);
/* 1143 */           int k = (int)(f9 * 255.0F);
/* 1144 */           int l = (int)(f10 * 255.0F);
/* 1145 */           this.lightmapColors[i] = (short1 << 24 | j << 16 | k << 8 | l);
/*      */         }
/*      */         
/* 1148 */         this.lightmapTexture.updateDynamicTexture();
/* 1149 */         this.lightmapUpdateNeeded = false;
/* 1150 */         this.mc.mcProfiler.endSection();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private float getNightVisionBrightness(EntityLivingBase entitylivingbaseIn, float partialTicks)
/*      */   {
/* 1157 */     int i = entitylivingbaseIn.getActivePotionEffect(Potion.nightVision).getDuration();
/* 1158 */     return i > 200 ? 1.0F : 0.7F + MathHelper.sin((i - partialTicks) * 3.1415927F * 0.2F) * 0.3F;
/*      */   }
/*      */   
/*      */   public void func_181560_a(float p_181560_1_, long p_181560_2_)
/*      */   {
/* 1163 */     frameInit();
/* 1164 */     boolean flag = Display.isActive();
/*      */     
/* 1166 */     if ((!flag) && (this.mc.gameSettings.pauseOnLostFocus) && ((!this.mc.gameSettings.touchscreen) || (!Mouse.isButtonDown(1))))
/*      */     {
/* 1168 */       if (Minecraft.getSystemTime() - this.prevFrameTime > 500L)
/*      */       {
/* 1170 */         this.mc.displayInGameMenu();
/*      */       }
/*      */       
/*      */     }
/*      */     else {
/* 1175 */       this.prevFrameTime = Minecraft.getSystemTime();
/*      */     }
/*      */     
/* 1178 */     this.mc.mcProfiler.startSection("mouse");
/*      */     
/* 1180 */     if ((flag) && (Minecraft.isRunningOnMac) && (this.mc.inGameHasFocus) && (!Mouse.isInsideWindow()))
/*      */     {
/* 1182 */       Mouse.setGrabbed(false);
/* 1183 */       Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
/* 1184 */       Mouse.setGrabbed(true);
/*      */     }
/*      */     
/* 1187 */     if ((this.mc.inGameHasFocus) && (flag))
/*      */     {
/* 1189 */       this.mc.mouseHelper.mouseXYChange();
/* 1190 */       float f = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
/* 1191 */       float f1 = f * f * f * 8.0F;
/* 1192 */       float f2 = this.mc.mouseHelper.deltaX * f1;
/* 1193 */       float f3 = this.mc.mouseHelper.deltaY * f1;
/* 1194 */       byte b0 = 1;
/*      */       
/* 1196 */       if (this.mc.gameSettings.invertMouse)
/*      */       {
/* 1198 */         b0 = -1;
/*      */       }
/*      */       
/* 1201 */       if (this.mc.gameSettings.smoothCamera)
/*      */       {
/* 1203 */         this.smoothCamYaw += f2;
/* 1204 */         this.smoothCamPitch += f3;
/* 1205 */         float f4 = p_181560_1_ - this.smoothCamPartialTicks;
/* 1206 */         this.smoothCamPartialTicks = p_181560_1_;
/* 1207 */         f2 = this.smoothCamFilterX * f4;
/* 1208 */         f3 = this.smoothCamFilterY * f4;
/* 1209 */         this.mc.thePlayer.setAngles(f2, f3 * b0);
/*      */       }
/*      */       else
/*      */       {
/* 1213 */         this.smoothCamYaw = 0.0F;
/* 1214 */         this.smoothCamPitch = 0.0F;
/* 1215 */         this.mc.thePlayer.setAngles(f2, f3 * b0);
/*      */       }
/*      */     }
/*      */     
/* 1219 */     this.mc.mcProfiler.endSection();
/*      */     
/* 1221 */     if (!this.mc.skipRenderWorld)
/*      */     {
/* 1223 */       anaglyphEnable = this.mc.gameSettings.anaglyph;
/* 1224 */       final ScaledResolution scaledresolution = new ScaledResolution(this.mc, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
/* 1225 */       int l = ScaledResolution.getScaledWidth();
/* 1226 */       int i1 = ScaledResolution.getScaledHeight();
/* 1227 */       final int j1 = Mouse.getX() * l / this.mc.displayWidth;
/* 1228 */       final int k1 = i1 - Mouse.getY() * i1 / this.mc.displayHeight - 1;
/* 1229 */       int l1 = this.mc.gameSettings.limitFramerate;
/*      */       
/* 1231 */       if (this.mc.theWorld != null)
/*      */       {
/* 1233 */         this.mc.mcProfiler.startSection("level");
/* 1234 */         int i = Math.min(Minecraft.getDebugFPS(), l1);
/* 1235 */         i = Math.max(i, 60);
/* 1236 */         long j = System.nanoTime() - p_181560_2_;
/* 1237 */         long k = Math.max(1000000000 / i / 4 - j, 0L);
/* 1238 */         renderWorld(p_181560_1_, System.nanoTime() + k);
/*      */         
/* 1240 */         if (OpenGlHelper.shadersSupported)
/*      */         {
/* 1242 */           this.mc.renderGlobal.renderEntityOutlineFramebuffer();
/*      */           
/* 1244 */           if ((this.theShaderGroup != null) && (this.useShader))
/*      */           {
/* 1246 */             GlStateManager.matrixMode(5890);
/* 1247 */             GlStateManager.pushMatrix();
/* 1248 */             GlStateManager.loadIdentity();
/* 1249 */             this.theShaderGroup.loadShaderGroup(p_181560_1_);
/* 1250 */             GlStateManager.popMatrix();
/*      */           }
/*      */           
/* 1253 */           this.mc.getFramebuffer().bindFramebuffer(true);
/*      */         }
/*      */         
/* 1256 */         this.renderEndNanoTime = System.nanoTime();
/* 1257 */         this.mc.mcProfiler.endStartSection("gui");
/*      */         
/* 1259 */         if ((!this.mc.gameSettings.hideGUI) || (this.mc.currentScreen != null))
/*      */         {
/* 1261 */           GlStateManager.alphaFunc(516, 0.1F);
/* 1262 */           this.mc.ingameGUI.renderGameOverlay(p_181560_1_);
/*      */           
/* 1264 */           if ((this.mc.gameSettings.ofShowFps) && (!this.mc.gameSettings.showDebugInfo))
/*      */           {
/* 1266 */             Config.drawFps();
/*      */           }
/*      */           
/* 1269 */           if (this.mc.gameSettings.showDebugInfo)
/*      */           {
/* 1271 */             Lagometer.showLagometer(scaledresolution);
/*      */           }
/*      */         }
/*      */         
/* 1275 */         this.mc.mcProfiler.endSection();
/*      */       }
/*      */       else
/*      */       {
/* 1279 */         GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
/* 1280 */         GlStateManager.matrixMode(5889);
/* 1281 */         GlStateManager.loadIdentity();
/* 1282 */         GlStateManager.matrixMode(5888);
/* 1283 */         GlStateManager.loadIdentity();
/* 1284 */         setupOverlayRendering();
/* 1285 */         this.renderEndNanoTime = System.nanoTime();
/*      */       }
/*      */       
/* 1288 */       if (this.mc.currentScreen != null)
/*      */       {
/* 1290 */         GlStateManager.clear(256);
/*      */         
/*      */         try
/*      */         {
/* 1294 */           if (Reflector.ForgeHooksClient_drawScreen.exists())
/*      */           {
/* 1296 */             Reflector.callVoid(Reflector.ForgeHooksClient_drawScreen, new Object[] { this.mc.currentScreen, Integer.valueOf(j1), Integer.valueOf(k1), Float.valueOf(p_181560_1_) });
/*      */           }
/*      */           else
/*      */           {
/* 1300 */             this.mc.currentScreen.drawScreen(j1, k1, p_181560_1_);
/*      */           }
/*      */         }
/*      */         catch (Throwable throwable)
/*      */         {
/* 1305 */           CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering screen");
/* 1306 */           CrashReportCategory crashreportcategory = crashreport.makeCategory("Screen render details");
/* 1307 */           crashreportcategory.addCrashSectionCallable("Screen name", new EntityRenderer2(this));
/* 1308 */           crashreportcategory.addCrashSectionCallable("Mouse location", new Callable()
/*      */           {
/*      */             private static final String __OBFID = "CL_00000950";
/*      */             
/*      */             public String call() throws Exception {
/* 1313 */               return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", new Object[] { Integer.valueOf(j1), Integer.valueOf(k1), Integer.valueOf(Mouse.getX()), Integer.valueOf(Mouse.getY()) });
/*      */             }
/* 1315 */           });
/* 1316 */           crashreportcategory.addCrashSectionCallable("Screen size", new Callable()
/*      */           {
/*      */             private static final String __OBFID = "CL_00000951";
/*      */             
/*      */             public String call() throws Exception {
/* 1321 */               return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", new Object[] { Integer.valueOf(ScaledResolution.getScaledWidth()), Integer.valueOf(ScaledResolution.getScaledHeight()), Integer.valueOf(EntityRenderer.this.mc.displayWidth), Integer.valueOf(EntityRenderer.this.mc.displayHeight), Integer.valueOf(scaledresolution.getScaleFactor()) });
/*      */             }
/* 1323 */           });
/* 1324 */           throw new net.minecraft.util.ReportedException(crashreport);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1329 */     frameFinish();
/* 1330 */     waitForServerThread();
/* 1331 */     Lagometer.updateLagometer();
/*      */     
/* 1333 */     if (this.mc.gameSettings.ofProfiler)
/*      */     {
/* 1335 */       this.mc.gameSettings.showDebugProfilerChart = true;
/*      */     }
/*      */   }
/*      */   
/*      */   public void renderStreamIndicator(float partialTicks)
/*      */   {
/* 1341 */     setupOverlayRendering();
/* 1342 */     this.mc.ingameGUI.renderStreamIndicator(new ScaledResolution(this.mc, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight));
/*      */   }
/*      */   
/*      */   private boolean isDrawBlockOutline()
/*      */   {
/* 1347 */     if (!this.drawBlockOutline)
/*      */     {
/* 1349 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1353 */     Entity entity = this.mc.getRenderViewEntity();
/* 1354 */     boolean flag = ((entity instanceof EntityPlayer)) && (!this.mc.gameSettings.hideGUI);
/*      */     
/* 1356 */     if ((flag) && (!((EntityPlayer)entity).capabilities.allowEdit))
/*      */     {
/* 1358 */       ItemStack itemstack = ((EntityPlayer)entity).getCurrentEquippedItem();
/*      */       
/* 1360 */       if ((this.mc.objectMouseOver != null) && (this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK))
/*      */       {
/* 1362 */         BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
/* 1363 */         Block block = this.mc.theWorld.getBlockState(blockpos).getBlock();
/*      */         
/* 1365 */         if (this.mc.playerController.getCurrentGameType() == net.minecraft.world.WorldSettings.GameType.SPECTATOR)
/*      */         {
/*      */           boolean flag1;
/*      */           boolean flag1;
/* 1369 */           if (Reflector.ForgeBlock_hasTileEntity.exists())
/*      */           {
/* 1371 */             IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
/* 1372 */             flag1 = Reflector.callBoolean(block, Reflector.ForgeBlock_hasTileEntity, new Object[] { iblockstate });
/*      */           }
/*      */           else
/*      */           {
/* 1376 */             flag1 = block.hasTileEntity();
/*      */           }
/*      */           
/* 1379 */           flag = (flag1) && ((this.mc.theWorld.getTileEntity(blockpos) instanceof IInventory));
/*      */         }
/*      */         else
/*      */         {
/* 1383 */           flag = (itemstack != null) && ((itemstack.canDestroy(block)) || (itemstack.canPlaceOn(block)));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1388 */     return flag;
/*      */   }
/*      */   
/*      */ 
/*      */   private void renderWorldDirections(float partialTicks)
/*      */   {
/* 1394 */     if ((this.mc.gameSettings.showDebugInfo) && (!this.mc.gameSettings.hideGUI) && (!this.mc.thePlayer.hasReducedDebug()) && (!this.mc.gameSettings.reducedDebugInfo))
/*      */     {
/* 1396 */       Entity entity = this.mc.getRenderViewEntity();
/* 1397 */       GlStateManager.enableBlend();
/* 1398 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 1399 */       GL11.glLineWidth(1.0F);
/* 1400 */       GlStateManager.disableTexture2D();
/* 1401 */       GlStateManager.depthMask(false);
/* 1402 */       GlStateManager.pushMatrix();
/* 1403 */       GlStateManager.matrixMode(5888);
/* 1404 */       GlStateManager.loadIdentity();
/* 1405 */       orientCamera(partialTicks);
/* 1406 */       GlStateManager.translate(0.0F, entity.getEyeHeight(), 0.0F);
/* 1407 */       RenderGlobal.func_181563_a(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.005D, 1.0E-4D, 1.0E-4D), 255, 0, 0, 255);
/* 1408 */       RenderGlobal.func_181563_a(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0E-4D, 1.0E-4D, 0.005D), 0, 0, 255, 255);
/* 1409 */       RenderGlobal.func_181563_a(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0E-4D, 0.0033D, 1.0E-4D), 0, 255, 0, 255);
/* 1410 */       GlStateManager.popMatrix();
/* 1411 */       GlStateManager.depthMask(true);
/* 1412 */       GlStateManager.enableTexture2D();
/* 1413 */       GlStateManager.disableBlend();
/*      */     }
/*      */   }
/*      */   
/*      */   public void renderWorld(float partialTicks, long finishTimeNano)
/*      */   {
/* 1419 */     updateLightmap(partialTicks);
/*      */     
/* 1421 */     if (this.mc.getRenderViewEntity() == null)
/*      */     {
/* 1423 */       this.mc.setRenderViewEntity(this.mc.thePlayer);
/*      */     }
/*      */     
/* 1426 */     getMouseOver(partialTicks);
/* 1427 */     GlStateManager.enableDepth();
/* 1428 */     GlStateManager.enableAlpha();
/* 1429 */     GlStateManager.alphaFunc(516, 0.1F);
/* 1430 */     this.mc.mcProfiler.startSection("center");
/*      */     
/* 1432 */     if (this.mc.gameSettings.anaglyph)
/*      */     {
/* 1434 */       anaglyphField = 0;
/* 1435 */       GlStateManager.colorMask(false, true, true, false);
/* 1436 */       renderWorldPass(0, partialTicks, finishTimeNano);
/* 1437 */       anaglyphField = 1;
/* 1438 */       GlStateManager.colorMask(true, false, false, false);
/* 1439 */       renderWorldPass(1, partialTicks, finishTimeNano);
/* 1440 */       GlStateManager.colorMask(true, true, true, false);
/*      */     }
/*      */     else
/*      */     {
/* 1444 */       renderWorldPass(2, partialTicks, finishTimeNano);
/*      */     }
/*      */     
/* 1447 */     this.mc.mcProfiler.endSection();
/*      */   }
/*      */   
/*      */   private void renderWorldPass(int pass, float partialTicks, long finishTimeNano)
/*      */   {
/* 1452 */     RenderGlobal renderglobal = this.mc.renderGlobal;
/* 1453 */     EffectRenderer effectrenderer = this.mc.effectRenderer;
/* 1454 */     boolean flag = isDrawBlockOutline();
/* 1455 */     GlStateManager.enableCull();
/* 1456 */     this.mc.mcProfiler.endStartSection("clear");
/* 1457 */     GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
/* 1458 */     updateFogColor(partialTicks);
/* 1459 */     GlStateManager.clear(16640);
/* 1460 */     this.mc.mcProfiler.endStartSection("camera");
/* 1461 */     setupCameraTransform(partialTicks, pass);
/* 1462 */     ActiveRenderInfo.updateRenderInfo(this.mc.thePlayer, this.mc.gameSettings.thirdPersonView == 2);
/* 1463 */     this.mc.mcProfiler.endStartSection("frustum");
/* 1464 */     net.minecraft.client.renderer.culling.ClippingHelperImpl.getInstance();
/* 1465 */     this.mc.mcProfiler.endStartSection("culling");
/* 1466 */     Frustum frustum = new Frustum();
/* 1467 */     Entity entity = this.mc.getRenderViewEntity();
/* 1468 */     double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
/* 1469 */     double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
/* 1470 */     double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
/* 1471 */     frustum.setPosition(d0, d1, d2);
/*      */     
/* 1473 */     if ((!Config.isSkyEnabled()) && (!Config.isSunMoonEnabled()) && (!Config.isStarsEnabled()))
/*      */     {
/* 1475 */       GlStateManager.disableBlend();
/*      */     }
/*      */     else
/*      */     {
/* 1479 */       setupFog(-1, partialTicks);
/* 1480 */       this.mc.mcProfiler.endStartSection("sky");
/* 1481 */       GlStateManager.matrixMode(5889);
/* 1482 */       GlStateManager.loadIdentity();
/* 1483 */       Project.gluPerspective(getFOVModifier(partialTicks, true), this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
/* 1484 */       GlStateManager.matrixMode(5888);
/* 1485 */       renderglobal.renderSky(partialTicks, pass);
/* 1486 */       GlStateManager.matrixMode(5889);
/* 1487 */       GlStateManager.loadIdentity();
/* 1488 */       Project.gluPerspective(getFOVModifier(partialTicks, true), this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
/* 1489 */       GlStateManager.matrixMode(5888);
/*      */     }
/*      */     
/* 1492 */     setupFog(0, partialTicks);
/* 1493 */     GlStateManager.shadeModel(7425);
/*      */     
/* 1495 */     if (entity.posY + entity.getEyeHeight() < 128.0D + this.mc.gameSettings.ofCloudsHeight * 128.0F)
/*      */     {
/* 1497 */       renderCloudsCheck(renderglobal, partialTicks, pass);
/*      */     }
/*      */     
/* 1500 */     this.mc.mcProfiler.endStartSection("prepareterrain");
/* 1501 */     setupFog(0, partialTicks);
/* 1502 */     this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
/* 1503 */     RenderHelper.disableStandardItemLighting();
/* 1504 */     this.mc.mcProfiler.endStartSection("terrain_setup");
/* 1505 */     renderglobal.setupTerrain(entity, partialTicks, frustum, this.frameCount++, this.mc.thePlayer.isSpectator());
/*      */     
/* 1507 */     if ((pass == 0) || (pass == 2))
/*      */     {
/* 1509 */       this.mc.mcProfiler.endStartSection("updatechunks");
/* 1510 */       Lagometer.timerChunkUpload.start();
/* 1511 */       this.mc.renderGlobal.updateChunks(finishTimeNano);
/* 1512 */       Lagometer.timerChunkUpload.end();
/*      */     }
/*      */     
/* 1515 */     this.mc.mcProfiler.endStartSection("terrain");
/* 1516 */     Lagometer.timerTerrain.start();
/*      */     
/* 1518 */     if ((this.mc.gameSettings.ofSmoothFps) && (pass > 0))
/*      */     {
/* 1520 */       GL11.glFinish();
/*      */     }
/*      */     
/* 1523 */     GlStateManager.matrixMode(5888);
/* 1524 */     GlStateManager.pushMatrix();
/* 1525 */     GlStateManager.disableAlpha();
/* 1526 */     renderglobal.renderBlockLayer(EnumWorldBlockLayer.SOLID, partialTicks, pass, entity);
/* 1527 */     GlStateManager.enableAlpha();
/* 1528 */     renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, partialTicks, pass, entity);
/* 1529 */     this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
/* 1530 */     renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT, partialTicks, pass, entity);
/* 1531 */     this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
/* 1532 */     Lagometer.timerTerrain.end();
/* 1533 */     GlStateManager.shadeModel(7424);
/* 1534 */     GlStateManager.alphaFunc(516, 0.1F);
/*      */     
/* 1536 */     if (!this.debugView)
/*      */     {
/* 1538 */       GlStateManager.matrixMode(5888);
/* 1539 */       GlStateManager.popMatrix();
/* 1540 */       GlStateManager.pushMatrix();
/* 1541 */       RenderHelper.enableStandardItemLighting();
/* 1542 */       this.mc.mcProfiler.endStartSection("entities");
/*      */       
/* 1544 */       if (Reflector.ForgeHooksClient_setRenderPass.exists())
/*      */       {
/* 1546 */         Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[] { Integer.valueOf(0) });
/*      */       }
/*      */       
/* 1549 */       renderglobal.renderEntities(entity, frustum, partialTicks);
/*      */       
/* 1551 */       if (Reflector.ForgeHooksClient_setRenderPass.exists())
/*      */       {
/* 1553 */         Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[] { Integer.valueOf(-1) });
/*      */       }
/*      */       
/* 1556 */       RenderHelper.disableStandardItemLighting();
/* 1557 */       disableLightmap();
/* 1558 */       GlStateManager.matrixMode(5888);
/* 1559 */       GlStateManager.popMatrix();
/* 1560 */       GlStateManager.pushMatrix();
/*      */       
/* 1562 */       if ((this.mc.objectMouseOver != null) && (entity.isInsideOfMaterial(Material.water)) && (flag))
/*      */       {
/* 1564 */         EntityPlayer entityplayer = (EntityPlayer)entity;
/* 1565 */         GlStateManager.disableAlpha();
/* 1566 */         this.mc.mcProfiler.endStartSection("outline");
/* 1567 */         boolean flag1 = Reflector.ForgeHooksClient_onDrawBlockHighlight.exists();
/*      */         
/* 1569 */         if (flag1) { if (Reflector.callBoolean(Reflector.ForgeHooksClient_onDrawBlockHighlight, new Object[] { renderglobal, entityplayer, this.mc.objectMouseOver, Integer.valueOf(0), entityplayer.getHeldItem(), Float.valueOf(partialTicks) })) {} } else if (!this.mc.gameSettings.hideGUI)
/*      */         {
/* 1571 */           renderglobal.drawSelectionBox(entityplayer, this.mc.objectMouseOver, 0, partialTicks);
/*      */         }
/* 1573 */         GlStateManager.enableAlpha();
/*      */       }
/*      */     }
/*      */     
/* 1577 */     GlStateManager.matrixMode(5888);
/* 1578 */     GlStateManager.popMatrix();
/*      */     
/* 1580 */     if ((flag) && (this.mc.objectMouseOver != null) && (!entity.isInsideOfMaterial(Material.water)))
/*      */     {
/* 1582 */       EntityPlayer entityplayer1 = (EntityPlayer)entity;
/* 1583 */       GlStateManager.disableAlpha();
/* 1584 */       this.mc.mcProfiler.endStartSection("outline");
/* 1585 */       boolean flag2 = Reflector.ForgeHooksClient_onDrawBlockHighlight.exists();
/*      */       
/* 1587 */       if (flag2) { if (Reflector.callBoolean(Reflector.ForgeHooksClient_onDrawBlockHighlight, new Object[] { renderglobal, entityplayer1, this.mc.objectMouseOver, Integer.valueOf(0), entityplayer1.getHeldItem(), Float.valueOf(partialTicks) })) {} } else if (!this.mc.gameSettings.hideGUI)
/*      */       {
/* 1589 */         renderglobal.drawSelectionBox(entityplayer1, this.mc.objectMouseOver, 0, partialTicks);
/*      */       }
/* 1591 */       GlStateManager.enableAlpha();
/*      */     }
/*      */     
/* 1594 */     this.mc.mcProfiler.endStartSection("destroyProgress");
/* 1595 */     GlStateManager.enableBlend();
/* 1596 */     GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
/* 1597 */     this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
/* 1598 */     renderglobal.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getWorldRenderer(), entity, partialTicks);
/* 1599 */     this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
/* 1600 */     GlStateManager.disableBlend();
/*      */     
/* 1602 */     if (!this.debugView)
/*      */     {
/* 1604 */       enableLightmap();
/* 1605 */       this.mc.mcProfiler.endStartSection("litParticles");
/* 1606 */       effectrenderer.renderLitParticles(entity, partialTicks);
/* 1607 */       RenderHelper.disableStandardItemLighting();
/* 1608 */       setupFog(0, partialTicks);
/* 1609 */       this.mc.mcProfiler.endStartSection("particles");
/* 1610 */       effectrenderer.renderParticles(entity, partialTicks);
/* 1611 */       disableLightmap();
/*      */     }
/*      */     
/* 1614 */     GlStateManager.depthMask(false);
/* 1615 */     GlStateManager.enableCull();
/* 1616 */     this.mc.mcProfiler.endStartSection("weather");
/* 1617 */     renderRainSnow(partialTicks);
/* 1618 */     GlStateManager.depthMask(true);
/* 1619 */     renderglobal.renderWorldBorder(entity, partialTicks);
/* 1620 */     GlStateManager.disableBlend();
/* 1621 */     GlStateManager.enableCull();
/* 1622 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 1623 */     GlStateManager.alphaFunc(516, 0.1F);
/* 1624 */     setupFog(0, partialTicks);
/* 1625 */     GlStateManager.enableBlend();
/* 1626 */     GlStateManager.depthMask(false);
/* 1627 */     this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
/* 1628 */     GlStateManager.shadeModel(7425);
/* 1629 */     this.mc.mcProfiler.endStartSection("translucent");
/* 1630 */     renderglobal.renderBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, partialTicks, pass, entity);
/*      */     
/* 1632 */     if ((Reflector.ForgeHooksClient_setRenderPass.exists()) && (!this.debugView))
/*      */     {
/* 1634 */       RenderHelper.enableStandardItemLighting();
/* 1635 */       this.mc.mcProfiler.endStartSection("entities");
/* 1636 */       Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[] { Integer.valueOf(1) });
/* 1637 */       this.mc.renderGlobal.renderEntities(entity, frustum, partialTicks);
/* 1638 */       Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[] { Integer.valueOf(-1) });
/* 1639 */       RenderHelper.disableStandardItemLighting();
/*      */     }
/*      */     
/* 1642 */     GlStateManager.shadeModel(7424);
/* 1643 */     GlStateManager.depthMask(true);
/* 1644 */     GlStateManager.enableCull();
/* 1645 */     GlStateManager.disableBlend();
/* 1646 */     GlStateManager.disableFog();
/*      */     
/* 1648 */     if (entity.posY + entity.getEyeHeight() >= 128.0D + this.mc.gameSettings.ofCloudsHeight * 128.0F)
/*      */     {
/* 1650 */       this.mc.mcProfiler.endStartSection("aboveClouds");
/* 1651 */       renderCloudsCheck(renderglobal, partialTicks, pass);
/*      */     }
/*      */     
/* 1654 */     if (Reflector.ForgeHooksClient_dispatchRenderLast.exists())
/*      */     {
/* 1656 */       this.mc.mcProfiler.endStartSection("forge_render_last");
/* 1657 */       Reflector.callVoid(Reflector.ForgeHooksClient_dispatchRenderLast, new Object[] { renderglobal, Float.valueOf(partialTicks) });
/*      */     }
/*      */     
/* 1660 */     Event3D event3D = new Event3D(partialTicks);
/* 1661 */     event3D.call();
/*      */     
/* 1663 */     this.mc.mcProfiler.endStartSection("hand");
/* 1664 */     boolean flag3 = Reflector.callBoolean(Reflector.ForgeHooksClient_renderFirstPersonHand, new Object[] { this.mc.renderGlobal, Float.valueOf(partialTicks), Integer.valueOf(pass) });
/*      */     
/* 1666 */     if ((!flag3) && (this.renderHand))
/*      */     {
/* 1668 */       GlStateManager.clear(256);
/* 1669 */       renderHand(partialTicks, pass);
/* 1670 */       renderWorldDirections(partialTicks);
/*      */     }
/*      */   }
/*      */   
/*      */   private void renderCloudsCheck(RenderGlobal renderGlobalIn, float partialTicks, int pass)
/*      */   {
/* 1676 */     if ((this.mc.gameSettings.renderDistanceChunks >= 4) && (!Config.isCloudsOff()))
/*      */     {
/* 1678 */       this.mc.mcProfiler.endStartSection("clouds");
/* 1679 */       GlStateManager.matrixMode(5889);
/* 1680 */       GlStateManager.loadIdentity();
/* 1681 */       Project.gluPerspective(getFOVModifier(partialTicks, true), this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance * 4.0F);
/* 1682 */       GlStateManager.matrixMode(5888);
/* 1683 */       GlStateManager.pushMatrix();
/* 1684 */       setupFog(0, partialTicks);
/* 1685 */       renderGlobalIn.renderClouds(partialTicks, pass);
/* 1686 */       GlStateManager.disableFog();
/* 1687 */       GlStateManager.popMatrix();
/* 1688 */       GlStateManager.matrixMode(5889);
/* 1689 */       GlStateManager.loadIdentity();
/* 1690 */       Project.gluPerspective(getFOVModifier(partialTicks, true), this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
/* 1691 */       GlStateManager.matrixMode(5888);
/*      */     }
/*      */   }
/*      */   
/*      */   private void addRainParticles()
/*      */   {
/* 1697 */     float f = this.mc.theWorld.getRainStrength(1.0F);
/*      */     
/* 1699 */     if (!Config.isRainFancy())
/*      */     {
/* 1701 */       f /= 2.0F;
/*      */     }
/*      */     
/* 1704 */     if ((f != 0.0F) && (Config.isRainSplash()))
/*      */     {
/* 1706 */       this.random.setSeed(this.rendererUpdateCount * 312987231L);
/* 1707 */       Entity entity = this.mc.getRenderViewEntity();
/* 1708 */       WorldClient worldclient = this.mc.theWorld;
/* 1709 */       BlockPos blockpos = new BlockPos(entity);
/* 1710 */       byte b0 = 10;
/* 1711 */       double d0 = 0.0D;
/* 1712 */       double d1 = 0.0D;
/* 1713 */       double d2 = 0.0D;
/* 1714 */       int i = 0;
/* 1715 */       int j = (int)(100.0F * f * f);
/*      */       
/* 1717 */       if (this.mc.gameSettings.particleSetting == 1)
/*      */       {
/* 1719 */         j >>= 1;
/*      */       }
/* 1721 */       else if (this.mc.gameSettings.particleSetting == 2)
/*      */       {
/* 1723 */         j = 0;
/*      */       }
/*      */       
/* 1726 */       for (int k = 0; k < j; k++)
/*      */       {
/* 1728 */         BlockPos blockpos1 = worldclient.getPrecipitationHeight(blockpos.add(this.random.nextInt(b0) - this.random.nextInt(b0), 0, this.random.nextInt(b0) - this.random.nextInt(b0)));
/* 1729 */         BiomeGenBase biomegenbase = worldclient.getBiomeGenForCoords(blockpos1);
/* 1730 */         BlockPos blockpos2 = blockpos1.down();
/* 1731 */         Block block = worldclient.getBlockState(blockpos2).getBlock();
/*      */         
/* 1733 */         if ((blockpos1.getY() <= blockpos.getY() + b0) && (blockpos1.getY() >= blockpos.getY() - b0) && (biomegenbase.canSpawnLightningBolt()) && (biomegenbase.getFloatTemperature(blockpos1) >= 0.15F))
/*      */         {
/* 1735 */           double d3 = this.random.nextDouble();
/* 1736 */           double d4 = this.random.nextDouble();
/*      */           
/* 1738 */           if (block.getMaterial() == Material.lava)
/*      */           {
/* 1740 */             this.mc.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, blockpos1.getX() + d3, blockpos1.getY() + 0.1F - block.getBlockBoundsMinY(), blockpos1.getZ() + d4, 0.0D, 0.0D, 0.0D, new int[0]);
/*      */           }
/* 1742 */           else if (block.getMaterial() != Material.air)
/*      */           {
/* 1744 */             block.setBlockBoundsBasedOnState(worldclient, blockpos2);
/* 1745 */             i++;
/*      */             
/* 1747 */             if (this.random.nextInt(i) == 0)
/*      */             {
/* 1749 */               d0 = blockpos2.getX() + d3;
/* 1750 */               d1 = blockpos2.getY() + 0.1F + block.getBlockBoundsMaxY() - 1.0D;
/* 1751 */               d2 = blockpos2.getZ() + d4;
/*      */             }
/*      */             
/* 1754 */             this.mc.theWorld.spawnParticle(EnumParticleTypes.WATER_DROP, blockpos2.getX() + d3, blockpos2.getY() + 0.1F + block.getBlockBoundsMaxY(), blockpos2.getZ() + d4, 0.0D, 0.0D, 0.0D, new int[0]);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1759 */       if ((i > 0) && (this.random.nextInt(3) < this.rainSoundCounter++))
/*      */       {
/* 1761 */         this.rainSoundCounter = 0;
/*      */         
/* 1763 */         if ((d1 > blockpos.getY() + 1) && (worldclient.getPrecipitationHeight(blockpos).getY() > MathHelper.floor_float(blockpos.getY())))
/*      */         {
/* 1765 */           this.mc.theWorld.playSound(d0, d1, d2, "ambient.weather.rain", 0.1F, 0.5F, false);
/*      */         }
/*      */         else
/*      */         {
/* 1769 */           this.mc.theWorld.playSound(d0, d1, d2, "ambient.weather.rain", 0.2F, 1.0F, false);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void renderRainSnow(float partialTicks)
/*      */   {
/* 1780 */     if (Reflector.ForgeWorldProvider_getWeatherRenderer.exists())
/*      */     {
/* 1782 */       WorldProvider worldprovider = this.mc.theWorld.provider;
/* 1783 */       Object object = Reflector.call(worldprovider, Reflector.ForgeWorldProvider_getWeatherRenderer, new Object[0]);
/*      */       
/* 1785 */       if (object != null)
/*      */       {
/* 1787 */         Reflector.callVoid(object, Reflector.IRenderHandler_render, new Object[] { Float.valueOf(partialTicks), this.mc.theWorld, this.mc });
/* 1788 */         return;
/*      */       }
/*      */     }
/*      */     
/* 1792 */     float f5 = this.mc.theWorld.getRainStrength(partialTicks);
/*      */     
/* 1794 */     if (f5 > 0.0F)
/*      */     {
/* 1796 */       if (Config.isRainOff())
/*      */       {
/* 1798 */         return;
/*      */       }
/*      */       
/* 1801 */       enableLightmap();
/* 1802 */       Entity entity = this.mc.getRenderViewEntity();
/* 1803 */       WorldClient worldclient = this.mc.theWorld;
/* 1804 */       int i = MathHelper.floor_double(entity.posX);
/* 1805 */       int j = MathHelper.floor_double(entity.posY);
/* 1806 */       int k = MathHelper.floor_double(entity.posZ);
/* 1807 */       Tessellator tessellator = Tessellator.getInstance();
/* 1808 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 1809 */       GlStateManager.disableCull();
/* 1810 */       GL11.glNormal3f(0.0F, 1.0F, 0.0F);
/* 1811 */       GlStateManager.enableBlend();
/* 1812 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 1813 */       GlStateManager.alphaFunc(516, 0.1F);
/* 1814 */       double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
/* 1815 */       double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
/* 1816 */       double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
/* 1817 */       int l = MathHelper.floor_double(d1);
/* 1818 */       byte b0 = 5;
/*      */       
/* 1820 */       if (Config.isRainFancy())
/*      */       {
/* 1822 */         b0 = 10;
/*      */       }
/*      */       
/* 1825 */       byte b1 = -1;
/* 1826 */       float f = this.rendererUpdateCount + partialTicks;
/* 1827 */       worldrenderer.setTranslation(-d0, -d1, -d2);
/*      */       
/* 1829 */       if (Config.isRainFancy())
/*      */       {
/* 1831 */         b0 = 10;
/*      */       }
/*      */       
/* 1834 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 1835 */       BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*      */       
/* 1837 */       for (int i1 = k - b0; i1 <= k + b0; i1++)
/*      */       {
/* 1839 */         for (int j1 = i - b0; j1 <= i + b0; j1++)
/*      */         {
/* 1841 */           int k1 = (i1 - k + 16) * 32 + j1 - i + 16;
/* 1842 */           double d3 = this.rainXCoords[k1] * 0.5D;
/* 1843 */           double d4 = this.rainYCoords[k1] * 0.5D;
/* 1844 */           blockpos$mutableblockpos.func_181079_c(j1, 0, i1);
/* 1845 */           BiomeGenBase biomegenbase = worldclient.getBiomeGenForCoords(blockpos$mutableblockpos);
/*      */           
/* 1847 */           if ((biomegenbase.canSpawnLightningBolt()) || (biomegenbase.getEnableSnow()))
/*      */           {
/* 1849 */             int l1 = worldclient.getPrecipitationHeight(blockpos$mutableblockpos).getY();
/* 1850 */             int i2 = j - b0;
/* 1851 */             int j2 = j + b0;
/*      */             
/* 1853 */             if (i2 < l1)
/*      */             {
/* 1855 */               i2 = l1;
/*      */             }
/*      */             
/* 1858 */             if (j2 < l1)
/*      */             {
/* 1860 */               j2 = l1;
/*      */             }
/*      */             
/* 1863 */             int k2 = l1;
/*      */             
/* 1865 */             if (l1 < l)
/*      */             {
/* 1867 */               k2 = l;
/*      */             }
/*      */             
/* 1870 */             if (i2 != j2)
/*      */             {
/* 1872 */               this.random.setSeed(j1 * j1 * 3121 + j1 * 45238971 ^ i1 * i1 * 418711 + i1 * 13761);
/* 1873 */               blockpos$mutableblockpos.func_181079_c(j1, i2, i1);
/* 1874 */               float f1 = biomegenbase.getFloatTemperature(blockpos$mutableblockpos);
/*      */               
/* 1876 */               if (worldclient.getWorldChunkManager().getTemperatureAtHeight(f1, l1) >= 0.15F)
/*      */               {
/* 1878 */                 if (b1 != 0)
/*      */                 {
/* 1880 */                   if (b1 >= 0)
/*      */                   {
/* 1882 */                     tessellator.draw();
/*      */                   }
/*      */                   
/* 1885 */                   b1 = 0;
/* 1886 */                   this.mc.getTextureManager().bindTexture(locationRainPng);
/* 1887 */                   worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
/*      */                 }
/*      */                 
/* 1890 */                 double d5 = ((this.rendererUpdateCount + j1 * j1 * 3121 + j1 * 45238971 + i1 * i1 * 418711 + i1 * 13761 & 0x1F) + partialTicks) / 32.0D * (3.0D + this.random.nextDouble());
/* 1891 */                 double d6 = j1 + 0.5F - entity.posX;
/* 1892 */                 double d7 = i1 + 0.5F - entity.posZ;
/* 1893 */                 float f2 = MathHelper.sqrt_double(d6 * d6 + d7 * d7) / b0;
/* 1894 */                 float f3 = ((1.0F - f2 * f2) * 0.5F + 0.5F) * f5;
/* 1895 */                 blockpos$mutableblockpos.func_181079_c(j1, k2, i1);
/* 1896 */                 int l2 = worldclient.getCombinedLight(blockpos$mutableblockpos, 0);
/* 1897 */                 int i3 = l2 >> 16 & 0xFFFF;
/* 1898 */                 int j3 = l2 & 0xFFFF;
/* 1899 */                 worldrenderer.pos(j1 - d3 + 0.5D, i2, i1 - d4 + 0.5D).tex(0.0D, i2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f3).lightmap(i3, j3).endVertex();
/* 1900 */                 worldrenderer.pos(j1 + d3 + 0.5D, i2, i1 + d4 + 0.5D).tex(1.0D, i2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f3).lightmap(i3, j3).endVertex();
/* 1901 */                 worldrenderer.pos(j1 + d3 + 0.5D, j2, i1 + d4 + 0.5D).tex(1.0D, j2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f3).lightmap(i3, j3).endVertex();
/* 1902 */                 worldrenderer.pos(j1 - d3 + 0.5D, j2, i1 - d4 + 0.5D).tex(0.0D, j2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f3).lightmap(i3, j3).endVertex();
/*      */               }
/*      */               else
/*      */               {
/* 1906 */                 if (b1 != 1)
/*      */                 {
/* 1908 */                   if (b1 >= 0)
/*      */                   {
/* 1910 */                     tessellator.draw();
/*      */                   }
/*      */                   
/* 1913 */                   b1 = 1;
/* 1914 */                   this.mc.getTextureManager().bindTexture(locationSnowPng);
/* 1915 */                   worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
/*      */                 }
/*      */                 
/* 1918 */                 double d8 = ((this.rendererUpdateCount & 0x1FF) + partialTicks) / 512.0F;
/* 1919 */                 double d9 = this.random.nextDouble() + f * 0.01D * (float)this.random.nextGaussian();
/* 1920 */                 double d10 = this.random.nextDouble() + f * (float)this.random.nextGaussian() * 0.001D;
/* 1921 */                 double d11 = j1 + 0.5F - entity.posX;
/* 1922 */                 double d12 = i1 + 0.5F - entity.posZ;
/* 1923 */                 float f6 = MathHelper.sqrt_double(d11 * d11 + d12 * d12) / b0;
/* 1924 */                 float f4 = ((1.0F - f6 * f6) * 0.3F + 0.5F) * f5;
/* 1925 */                 blockpos$mutableblockpos.func_181079_c(j1, k2, i1);
/* 1926 */                 int k3 = (worldclient.getCombinedLight(blockpos$mutableblockpos, 0) * 3 + 15728880) / 4;
/* 1927 */                 int l3 = k3 >> 16 & 0xFFFF;
/* 1928 */                 int i4 = k3 & 0xFFFF;
/* 1929 */                 worldrenderer.pos(j1 - d3 + 0.5D, i2, i1 - d4 + 0.5D).tex(0.0D + d9, i2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f4).lightmap(l3, i4).endVertex();
/* 1930 */                 worldrenderer.pos(j1 + d3 + 0.5D, i2, i1 + d4 + 0.5D).tex(1.0D + d9, i2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f4).lightmap(l3, i4).endVertex();
/* 1931 */                 worldrenderer.pos(j1 + d3 + 0.5D, j2, i1 + d4 + 0.5D).tex(1.0D + d9, j2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f4).lightmap(l3, i4).endVertex();
/* 1932 */                 worldrenderer.pos(j1 - d3 + 0.5D, j2, i1 - d4 + 0.5D).tex(0.0D + d9, j2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f4).lightmap(l3, i4).endVertex();
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1939 */       if (b1 >= 0)
/*      */       {
/* 1941 */         tessellator.draw();
/*      */       }
/*      */       
/* 1944 */       worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
/* 1945 */       GlStateManager.enableCull();
/* 1946 */       GlStateManager.disableBlend();
/* 1947 */       GlStateManager.alphaFunc(516, 0.1F);
/* 1948 */       disableLightmap();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setupOverlayRendering()
/*      */   {
/* 1957 */     ScaledResolution scaledresolution = new ScaledResolution(this.mc, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
/* 1958 */     GlStateManager.clear(256);
/* 1959 */     GlStateManager.matrixMode(5889);
/* 1960 */     GlStateManager.loadIdentity();
/* 1961 */     GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
/* 1962 */     GlStateManager.matrixMode(5888);
/* 1963 */     GlStateManager.loadIdentity();
/* 1964 */     GlStateManager.translate(0.0F, 0.0F, -2000.0F);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateFogColor(float partialTicks)
/*      */   {
/* 1972 */     WorldClient worldclient = this.mc.theWorld;
/* 1973 */     Entity entity = this.mc.getRenderViewEntity();
/* 1974 */     float f = 0.25F + 0.75F * this.mc.gameSettings.renderDistanceChunks / 32.0F;
/* 1975 */     f = 1.0F - (float)Math.pow(f, 0.25D);
/* 1976 */     Vec3 vec3 = worldclient.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
/* 1977 */     vec3 = CustomColorizer.getWorldSkyColor(vec3, worldclient, this.mc.getRenderViewEntity(), partialTicks);
/* 1978 */     float f1 = (float)vec3.xCoord;
/* 1979 */     float f2 = (float)vec3.yCoord;
/* 1980 */     float f3 = (float)vec3.zCoord;
/* 1981 */     Vec3 vec31 = worldclient.getFogColor(partialTicks);
/* 1982 */     vec31 = CustomColorizer.getWorldFogColor(vec31, worldclient, this.mc.getRenderViewEntity(), partialTicks);
/* 1983 */     this.fogColorRed = ((float)vec31.xCoord);
/* 1984 */     this.fogColorGreen = ((float)vec31.yCoord);
/* 1985 */     this.fogColorBlue = ((float)vec31.zCoord);
/*      */     
/* 1987 */     if (this.mc.gameSettings.renderDistanceChunks >= 4)
/*      */     {
/* 1989 */       double d0 = -1.0D;
/* 1990 */       Vec3 vec32 = MathHelper.sin(worldclient.getCelestialAngleRadians(partialTicks)) > 0.0F ? new Vec3(d0, 0.0D, 0.0D) : new Vec3(1.0D, 0.0D, 0.0D);
/* 1991 */       float f4 = (float)entity.getLook(partialTicks).dotProduct(vec32);
/*      */       
/* 1993 */       if (f4 < 0.0F)
/*      */       {
/* 1995 */         f4 = 0.0F;
/*      */       }
/*      */       
/* 1998 */       if (f4 > 0.0F)
/*      */       {
/* 2000 */         float[] afloat = worldclient.provider.calcSunriseSunsetColors(worldclient.getCelestialAngle(partialTicks), partialTicks);
/*      */         
/* 2002 */         if (afloat != null)
/*      */         {
/* 2004 */           f4 *= afloat[3];
/* 2005 */           this.fogColorRed = (this.fogColorRed * (1.0F - f4) + afloat[0] * f4);
/* 2006 */           this.fogColorGreen = (this.fogColorGreen * (1.0F - f4) + afloat[1] * f4);
/* 2007 */           this.fogColorBlue = (this.fogColorBlue * (1.0F - f4) + afloat[2] * f4);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2012 */     this.fogColorRed += (f1 - this.fogColorRed) * f;
/* 2013 */     this.fogColorGreen += (f2 - this.fogColorGreen) * f;
/* 2014 */     this.fogColorBlue += (f3 - this.fogColorBlue) * f;
/* 2015 */     float f10 = worldclient.getRainStrength(partialTicks);
/*      */     
/* 2017 */     if (f10 > 0.0F)
/*      */     {
/* 2019 */       float f5 = 1.0F - f10 * 0.5F;
/* 2020 */       float f12 = 1.0F - f10 * 0.4F;
/* 2021 */       this.fogColorRed *= f5;
/* 2022 */       this.fogColorGreen *= f5;
/* 2023 */       this.fogColorBlue *= f12;
/*      */     }
/*      */     
/* 2026 */     float f11 = worldclient.getThunderStrength(partialTicks);
/*      */     
/* 2028 */     if (f11 > 0.0F)
/*      */     {
/* 2030 */       float f13 = 1.0F - f11 * 0.5F;
/* 2031 */       this.fogColorRed *= f13;
/* 2032 */       this.fogColorGreen *= f13;
/* 2033 */       this.fogColorBlue *= f13;
/*      */     }
/*      */     
/* 2036 */     Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);
/*      */     
/* 2038 */     if (this.cloudFog)
/*      */     {
/* 2040 */       Vec3 vec33 = worldclient.getCloudColour(partialTicks);
/* 2041 */       this.fogColorRed = ((float)vec33.xCoord);
/* 2042 */       this.fogColorGreen = ((float)vec33.yCoord);
/* 2043 */       this.fogColorBlue = ((float)vec33.zCoord);
/*      */     }
/* 2045 */     else if (block.getMaterial() == Material.water)
/*      */     {
/* 2047 */       float f8 = EnchantmentHelper.getRespiration(entity) * 0.2F;
/*      */       
/* 2049 */       if (((entity instanceof EntityLivingBase)) && (((EntityLivingBase)entity).isPotionActive(Potion.waterBreathing)))
/*      */       {
/* 2051 */         f8 = f8 * 0.3F + 0.6F;
/*      */       }
/*      */       
/* 2054 */       this.fogColorRed = (0.02F + f8);
/* 2055 */       this.fogColorGreen = (0.02F + f8);
/* 2056 */       this.fogColorBlue = (0.2F + f8);
/* 2057 */       Vec3 vec34 = CustomColorizer.getUnderwaterColor(this.mc.theWorld, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0D, this.mc.getRenderViewEntity().posZ);
/*      */       
/* 2059 */       if (vec34 != null)
/*      */       {
/* 2061 */         this.fogColorRed = ((float)vec34.xCoord);
/* 2062 */         this.fogColorGreen = ((float)vec34.yCoord);
/* 2063 */         this.fogColorBlue = ((float)vec34.zCoord);
/*      */       }
/*      */     }
/* 2066 */     else if (block.getMaterial() == Material.lava)
/*      */     {
/* 2068 */       this.fogColorRed = 0.6F;
/* 2069 */       this.fogColorGreen = 0.1F;
/* 2070 */       this.fogColorBlue = 0.0F;
/*      */     }
/*      */     
/* 2073 */     float f9 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * partialTicks;
/* 2074 */     this.fogColorRed *= f9;
/* 2075 */     this.fogColorGreen *= f9;
/* 2076 */     this.fogColorBlue *= f9;
/* 2077 */     double d2 = worldclient.provider.getVoidFogYFactor();
/* 2078 */     double d1 = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks) * d2;
/*      */     
/* 2080 */     if (((entity instanceof EntityLivingBase)) && (((EntityLivingBase)entity).isPotionActive(Potion.blindness)))
/*      */     {
/* 2082 */       int i = ((EntityLivingBase)entity).getActivePotionEffect(Potion.blindness).getDuration();
/*      */       
/* 2084 */       if (i < 20)
/*      */       {
/* 2086 */         d1 *= (1.0F - i / 20.0F);
/*      */       }
/*      */       else
/*      */       {
/* 2090 */         d1 = 0.0D;
/*      */       }
/*      */     }
/*      */     
/* 2094 */     if (d1 < 1.0D)
/*      */     {
/* 2096 */       if (d1 < 0.0D)
/*      */       {
/* 2098 */         d1 = 0.0D;
/*      */       }
/*      */       
/* 2101 */       d1 *= d1;
/* 2102 */       this.fogColorRed = ((float)(this.fogColorRed * d1));
/* 2103 */       this.fogColorGreen = ((float)(this.fogColorGreen * d1));
/* 2104 */       this.fogColorBlue = ((float)(this.fogColorBlue * d1));
/*      */     }
/*      */     
/* 2107 */     if (this.bossColorModifier > 0.0F)
/*      */     {
/* 2109 */       float f14 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
/* 2110 */       this.fogColorRed = (this.fogColorRed * (1.0F - f14) + this.fogColorRed * 0.7F * f14);
/* 2111 */       this.fogColorGreen = (this.fogColorGreen * (1.0F - f14) + this.fogColorGreen * 0.6F * f14);
/* 2112 */       this.fogColorBlue = (this.fogColorBlue * (1.0F - f14) + this.fogColorBlue * 0.6F * f14);
/*      */     }
/*      */     
/* 2115 */     if (((entity instanceof EntityLivingBase)) && (((EntityLivingBase)entity).isPotionActive(Potion.nightVision)))
/*      */     {
/* 2117 */       float f15 = getNightVisionBrightness((EntityLivingBase)entity, partialTicks);
/* 2118 */       float f6 = 1.0F / this.fogColorRed;
/*      */       
/* 2120 */       if (f6 > 1.0F / this.fogColorGreen)
/*      */       {
/* 2122 */         f6 = 1.0F / this.fogColorGreen;
/*      */       }
/*      */       
/* 2125 */       if (f6 > 1.0F / this.fogColorBlue)
/*      */       {
/* 2127 */         f6 = 1.0F / this.fogColorBlue;
/*      */       }
/*      */       
/* 2130 */       this.fogColorRed = (this.fogColorRed * (1.0F - f15) + this.fogColorRed * f6 * f15);
/* 2131 */       this.fogColorGreen = (this.fogColorGreen * (1.0F - f15) + this.fogColorGreen * f6 * f15);
/* 2132 */       this.fogColorBlue = (this.fogColorBlue * (1.0F - f15) + this.fogColorBlue * f6 * f15);
/*      */     }
/*      */     
/* 2135 */     if (this.mc.gameSettings.anaglyph)
/*      */     {
/* 2137 */       float f16 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
/* 2138 */       float f17 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
/* 2139 */       float f7 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
/* 2140 */       this.fogColorRed = f16;
/* 2141 */       this.fogColorGreen = f17;
/* 2142 */       this.fogColorBlue = f7;
/*      */     }
/*      */     
/* 2145 */     if (Reflector.EntityViewRenderEvent_FogColors_Constructor.exists())
/*      */     {
/* 2147 */       Object object = Reflector.newInstance(Reflector.EntityViewRenderEvent_FogColors_Constructor, new Object[] { this, entity, block, Float.valueOf(partialTicks), Float.valueOf(this.fogColorRed), Float.valueOf(this.fogColorGreen), Float.valueOf(this.fogColorBlue) });
/* 2148 */       Reflector.postForgeBusEvent(object);
/* 2149 */       this.fogColorRed = Reflector.getFieldValueFloat(object, Reflector.EntityViewRenderEvent_FogColors_red, this.fogColorRed);
/* 2150 */       this.fogColorGreen = Reflector.getFieldValueFloat(object, Reflector.EntityViewRenderEvent_FogColors_green, this.fogColorGreen);
/* 2151 */       this.fogColorBlue = Reflector.getFieldValueFloat(object, Reflector.EntityViewRenderEvent_FogColors_blue, this.fogColorBlue);
/*      */     }
/*      */     
/* 2154 */     GlStateManager.clearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setupFog(int p_78468_1_, float partialTicks)
/*      */   {
/* 2163 */     Entity entity = this.mc.getRenderViewEntity();
/* 2164 */     boolean flag = false;
/* 2165 */     this.fogStandard = false;
/*      */     
/* 2167 */     if ((entity instanceof EntityPlayer))
/*      */     {
/* 2169 */       flag = ((EntityPlayer)entity).capabilities.isCreativeMode;
/*      */     }
/*      */     
/* 2172 */     GL11.glFog(2918, setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
/* 2173 */     GL11.glNormal3f(0.0F, -1.0F, 0.0F);
/* 2174 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 2175 */     Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);
/* 2176 */     Object object = Reflector.newInstance(Reflector.EntityViewRenderEvent_FogDensity_Constructor, new Object[] { this, entity, block, Float.valueOf(partialTicks), Float.valueOf(0.1F) });
/*      */     
/* 2178 */     if (Reflector.postForgeBusEvent(object))
/*      */     {
/* 2180 */       float f1 = Reflector.getFieldValueFloat(object, Reflector.EntityViewRenderEvent_FogDensity_density, 0.0F);
/* 2181 */       GL11.glFogf(2914, f1);
/*      */     }
/* 2183 */     else if (((entity instanceof EntityLivingBase)) && (((EntityLivingBase)entity).isPotionActive(Potion.blindness)))
/*      */     {
/* 2185 */       float f2 = 5.0F;
/* 2186 */       int i = ((EntityLivingBase)entity).getActivePotionEffect(Potion.blindness).getDuration();
/*      */       
/* 2188 */       if (i < 20)
/*      */       {
/* 2190 */         f2 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - i / 20.0F);
/*      */       }
/*      */       
/* 2193 */       GlStateManager.setFog(9729);
/*      */       
/* 2195 */       if (p_78468_1_ == -1)
/*      */       {
/* 2197 */         GlStateManager.setFogStart(0.0F);
/* 2198 */         GlStateManager.setFogEnd(f2 * 0.8F);
/*      */       }
/*      */       else
/*      */       {
/* 2202 */         GlStateManager.setFogStart(f2 * 0.25F);
/* 2203 */         GlStateManager.setFogEnd(f2);
/*      */       }
/*      */       
/* 2206 */       if ((GLContext.getCapabilities().GL_NV_fog_distance) && (Config.isFogFancy()))
/*      */       {
/* 2208 */         GL11.glFogi(34138, 34139);
/*      */       }
/*      */     }
/* 2211 */     else if (this.cloudFog)
/*      */     {
/* 2213 */       GlStateManager.setFog(2048);
/* 2214 */       GlStateManager.setFogDensity(0.1F);
/*      */     }
/* 2216 */     else if (block.getMaterial() == Material.water)
/*      */     {
/* 2218 */       GlStateManager.setFog(2048);
/*      */       
/* 2220 */       if (((entity instanceof EntityLivingBase)) && (((EntityLivingBase)entity).isPotionActive(Potion.waterBreathing)))
/*      */       {
/* 2222 */         GlStateManager.setFogDensity(0.01F);
/*      */       }
/*      */       else
/*      */       {
/* 2226 */         GlStateManager.setFogDensity(0.1F - EnchantmentHelper.getRespiration(entity) * 0.03F);
/*      */       }
/*      */       
/* 2229 */       if (Config.isClearWater())
/*      */       {
/* 2231 */         GL11.glFogf(2914, 0.02F);
/*      */       }
/*      */     }
/* 2234 */     else if (block.getMaterial() == Material.lava)
/*      */     {
/* 2236 */       GlStateManager.setFog(2048);
/* 2237 */       GlStateManager.setFogDensity(2.0F);
/*      */     }
/*      */     else
/*      */     {
/* 2241 */       float f = this.farPlaneDistance;
/* 2242 */       this.fogStandard = true;
/* 2243 */       GlStateManager.setFog(9729);
/*      */       
/* 2245 */       if (p_78468_1_ == -1)
/*      */       {
/* 2247 */         GlStateManager.setFogStart(0.0F);
/* 2248 */         GlStateManager.setFogEnd(f);
/*      */       }
/*      */       else
/*      */       {
/* 2252 */         GlStateManager.setFogStart(f * Config.getFogStart());
/* 2253 */         GlStateManager.setFogEnd(f);
/*      */       }
/*      */       
/* 2256 */       if (GLContext.getCapabilities().GL_NV_fog_distance)
/*      */       {
/* 2258 */         if (Config.isFogFancy())
/*      */         {
/* 2260 */           GL11.glFogi(34138, 34139);
/*      */         }
/*      */         
/* 2263 */         if (Config.isFogFast())
/*      */         {
/* 2265 */           GL11.glFogi(34138, 34140);
/*      */         }
/*      */       }
/*      */       
/* 2269 */       if (this.mc.theWorld.provider.doesXZShowFog((int)entity.posX, (int)entity.posZ))
/*      */       {
/* 2271 */         GlStateManager.setFogStart(f * 0.05F);
/* 2272 */         GlStateManager.setFogEnd(f);
/*      */       }
/*      */       
/* 2275 */       if (Reflector.ForgeHooksClient_onFogRender.exists())
/*      */       {
/* 2277 */         Reflector.callVoid(Reflector.ForgeHooksClient_onFogRender, new Object[] { this, entity, block, Float.valueOf(partialTicks), Integer.valueOf(p_78468_1_), Float.valueOf(f) });
/*      */       }
/*      */     }
/*      */     
/* 2281 */     GlStateManager.enableColorMaterial();
/* 2282 */     GlStateManager.enableFog();
/* 2283 */     GlStateManager.colorMaterial(1028, 4608);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private FloatBuffer setFogColorBuffer(float red, float green, float blue, float alpha)
/*      */   {
/* 2291 */     this.fogColorBuffer.clear();
/* 2292 */     this.fogColorBuffer.put(red).put(green).put(blue).put(alpha);
/* 2293 */     this.fogColorBuffer.flip();
/* 2294 */     return this.fogColorBuffer;
/*      */   }
/*      */   
/*      */   public MapItemRenderer getMapItemRenderer()
/*      */   {
/* 2299 */     return this.theMapItemRenderer;
/*      */   }
/*      */   
/*      */   private void waitForServerThread()
/*      */   {
/* 2304 */     this.serverWaitTimeCurrent = 0;
/*      */     
/* 2306 */     if ((Config.isSmoothWorld()) && (Config.isSingleProcessor()))
/*      */     {
/* 2308 */       if (this.mc.isIntegratedServerRunning())
/*      */       {
/* 2310 */         IntegratedServer integratedserver = this.mc.getIntegratedServer();
/*      */         
/* 2312 */         if (integratedserver != null)
/*      */         {
/* 2314 */           boolean flag = this.mc.isGamePaused();
/*      */           
/* 2316 */           if ((!flag) && (!(this.mc.currentScreen instanceof GuiDownloadTerrain)))
/*      */           {
/* 2318 */             if (this.serverWaitTime > 0)
/*      */             {
/* 2320 */               Lagometer.timerServer.start();
/* 2321 */               Config.sleep(this.serverWaitTime);
/* 2322 */               Lagometer.timerServer.end();
/* 2323 */               this.serverWaitTimeCurrent = this.serverWaitTime;
/*      */             }
/*      */             
/* 2326 */             long i = System.nanoTime() / 1000000L;
/*      */             
/* 2328 */             if ((this.lastServerTime != 0L) && (this.lastServerTicks != 0))
/*      */             {
/* 2330 */               long j = i - this.lastServerTime;
/*      */               
/* 2332 */               if (j < 0L)
/*      */               {
/* 2334 */                 this.lastServerTime = i;
/* 2335 */                 j = 0L;
/*      */               }
/*      */               
/* 2338 */               if (j >= 50L)
/*      */               {
/* 2340 */                 this.lastServerTime = i;
/* 2341 */                 int k = integratedserver.getTickCounter();
/* 2342 */                 int l = k - this.lastServerTicks;
/*      */                 
/* 2344 */                 if (l < 0)
/*      */                 {
/* 2346 */                   this.lastServerTicks = k;
/* 2347 */                   l = 0;
/*      */                 }
/*      */                 
/* 2350 */                 if ((l < 1) && (this.serverWaitTime < 100))
/*      */                 {
/* 2352 */                   this.serverWaitTime += 2;
/*      */                 }
/*      */                 
/* 2355 */                 if ((l > 1) && (this.serverWaitTime > 0))
/*      */                 {
/* 2357 */                   this.serverWaitTime -= 1;
/*      */                 }
/*      */                 
/* 2360 */                 this.lastServerTicks = k;
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/* 2365 */               this.lastServerTime = i;
/* 2366 */               this.lastServerTicks = integratedserver.getTickCounter();
/* 2367 */               this.avgServerTickDiff = 1.0F;
/* 2368 */               this.avgServerTimeDiff = 50.0F;
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 2373 */             if ((this.mc.currentScreen instanceof GuiDownloadTerrain))
/*      */             {
/* 2375 */               Config.sleep(20L);
/*      */             }
/*      */             
/* 2378 */             this.lastServerTime = 0L;
/* 2379 */             this.lastServerTicks = 0;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 2386 */       this.lastServerTime = 0L;
/* 2387 */       this.lastServerTicks = 0;
/*      */     }
/*      */   }
/*      */   
/*      */   private void frameInit()
/*      */   {
/* 2393 */     if (!this.initialized)
/*      */     {
/* 2395 */       optfine.TextureUtils.registerResourceListener();
/* 2396 */       optfine.RenderPlayerOF.register();
/*      */       
/* 2398 */       if ((Config.getBitsOs() == 64) && (Config.getBitsJre() == 32))
/*      */       {
/* 2400 */         Config.setNotify64BitJava(true);
/*      */       }
/*      */       
/* 2403 */       this.initialized = true;
/*      */     }
/*      */     
/* 2406 */     Config.isActing();
/* 2407 */     Config.checkDisplayMode();
/* 2408 */     World world = this.mc.theWorld;
/*      */     
/* 2410 */     if (world != null)
/*      */     {
/* 2412 */       if (Config.getNewRelease() != null)
/*      */       {
/* 2414 */         String s = "HD_U".replace("HD_U", "HD Ultra").replace("L", "Light");
/* 2415 */         String s1 = s + " " + Config.getNewRelease();
/* 2416 */         ChatComponentText chatcomponenttext = new ChatComponentText("A new §eOptiFine§f version is available: §e" + s1 + "§f");
/* 2417 */         this.mc.ingameGUI.getChatGUI().printChatMessage(chatcomponenttext);
/* 2418 */         Config.setNewRelease(null);
/*      */       }
/*      */       
/* 2421 */       if (Config.isNotify64BitJava())
/*      */       {
/* 2423 */         Config.setNotify64BitJava(false);
/* 2424 */         ChatComponentText chatcomponenttext1 = new ChatComponentText("You can install §e64-bit Java§f to increase performance");
/* 2425 */         this.mc.ingameGUI.getChatGUI().printChatMessage(chatcomponenttext1);
/*      */       }
/*      */     }
/*      */     
/* 2429 */     if ((this.mc.currentScreen instanceof GuiMainMenu))
/*      */     {
/* 2431 */       updateMainMenu((GuiMainMenu)this.mc.currentScreen);
/*      */     }
/*      */     
/* 2434 */     if (this.updatedWorld != world)
/*      */     {
/* 2436 */       optfine.RandomMobs.worldChanged(this.updatedWorld, world);
/* 2437 */       Config.updateThreadPriorities();
/* 2438 */       this.lastServerTime = 0L;
/* 2439 */       this.lastServerTicks = 0;
/* 2440 */       this.updatedWorld = world;
/*      */     }
/*      */   }
/*      */   
/*      */   private void frameFinish()
/*      */   {
/* 2446 */     if (this.mc.theWorld != null)
/*      */     {
/* 2448 */       long i = System.currentTimeMillis();
/*      */       
/* 2450 */       if (i > this.lastErrorCheckTimeMs + 10000L)
/*      */       {
/* 2452 */         this.lastErrorCheckTimeMs = i;
/* 2453 */         int j = GL11.glGetError();
/*      */         
/* 2455 */         if (j != 0)
/*      */         {
/* 2457 */           String s = org.lwjgl.util.glu.GLU.gluErrorString(j);
/* 2458 */           ChatComponentText chatcomponenttext = new ChatComponentText("§eOpenGL Error§f: " + j + " (" + s + ")");
/* 2459 */           this.mc.ingameGUI.getChatGUI().printChatMessage(chatcomponenttext);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateMainMenu(GuiMainMenu p_updateMainMenu_1_)
/*      */   {
/*      */     try
/*      */     {
/* 2469 */       String s = null;
/* 2470 */       Calendar calendar = Calendar.getInstance();
/* 2471 */       calendar.setTime(new java.util.Date());
/* 2472 */       int i = calendar.get(5);
/* 2473 */       int j = calendar.get(2) + 1;
/*      */       
/* 2475 */       if ((i == 8) && (j == 4))
/*      */       {
/* 2477 */         s = "Happy birthday, OptiFine!";
/*      */       }
/*      */       
/* 2480 */       if ((i == 14) && (j == 8))
/*      */       {
/* 2482 */         s = "Happy birthday, sp614x!";
/*      */       }
/*      */       
/* 2485 */       if (s == null)
/*      */       {
/* 2487 */         return;
/*      */       }
/*      */       
/* 2490 */       Field[] afield = GuiMainMenu.class.getDeclaredFields();
/*      */       
/* 2492 */       for (int k = 0; k < afield.length; k++)
/*      */       {
/* 2494 */         if (afield[k].getType() == String.class)
/*      */         {
/* 2496 */           afield[k].setAccessible(true);
/* 2497 */           afield[k].set(p_updateMainMenu_1_, s);
/* 2498 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Throwable localThrowable) {}
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\EntityRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */