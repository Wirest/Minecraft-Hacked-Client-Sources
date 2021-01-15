/*      */ package net.minecraft.client.renderer;
/*      */ 
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.google.gson.JsonSyntaxException;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Deque;
/*      */ import java.util.EnumSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.Callable;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.Block.SoundType;
/*      */ import net.minecraft.block.BlockChest;
/*      */ import net.minecraft.block.BlockEnderChest;
/*      */ import net.minecraft.block.BlockLeaves;
/*      */ import net.minecraft.block.BlockSign;
/*      */ import net.minecraft.block.BlockSkull;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.audio.ISound;
/*      */ import net.minecraft.client.audio.PositionedSoundRecord;
/*      */ import net.minecraft.client.audio.SoundHandler;
/*      */ import net.minecraft.client.entity.EntityPlayerSP;
/*      */ import net.minecraft.client.multiplayer.WorldClient;
/*      */ import net.minecraft.client.particle.EffectRenderer;
/*      */ import net.minecraft.client.particle.EntityFX;
/*      */ import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
/*      */ import net.minecraft.client.renderer.chunk.CompiledChunk;
/*      */ import net.minecraft.client.renderer.chunk.ListChunkFactory;
/*      */ import net.minecraft.client.renderer.chunk.RenderChunk;
/*      */ import net.minecraft.client.renderer.chunk.VboChunkFactory;
/*      */ import net.minecraft.client.renderer.chunk.VisGraph;
/*      */ import net.minecraft.client.renderer.culling.ClippingHelper;
/*      */ import net.minecraft.client.renderer.culling.ClippingHelperImpl;
/*      */ import net.minecraft.client.renderer.culling.Frustum;
/*      */ import net.minecraft.client.renderer.culling.ICamera;
/*      */ import net.minecraft.client.renderer.entity.RenderManager;
/*      */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*      */ import net.minecraft.client.renderer.texture.TextureManager;
/*      */ import net.minecraft.client.renderer.texture.TextureMap;
/*      */ import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
/*      */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*      */ import net.minecraft.client.renderer.vertex.VertexBuffer;
/*      */ import net.minecraft.client.renderer.vertex.VertexFormat;
/*      */ import net.minecraft.client.renderer.vertex.VertexFormatElement;
/*      */ import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
/*      */ import net.minecraft.client.resources.IResourceManager;
/*      */ import net.minecraft.client.resources.IResourceManagerReloadListener;
/*      */ import net.minecraft.client.settings.GameSettings;
/*      */ import net.minecraft.client.shader.Framebuffer;
/*      */ import net.minecraft.client.shader.ShaderGroup;
/*      */ import net.minecraft.client.shader.ShaderLinkHelper;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemPotion;
/*      */ import net.minecraft.item.ItemRecord;
/*      */ import net.minecraft.profiler.Profiler;
/*      */ import net.minecraft.tileentity.TileEntity;
/*      */ import net.minecraft.tileentity.TileEntityChest;
/*      */ import net.minecraft.tileentity.TileEntitySign;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*      */ import net.minecraft.util.ClassInheritanceMultiMap;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.EnumWorldBlockLayer;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.Matrix4f;
/*      */ import net.minecraft.util.MovingObjectPosition;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.util.Vector3d;
/*      */ import net.minecraft.world.WorldProvider;
/*      */ import net.minecraft.world.border.EnumBorderStatus;
/*      */ import net.minecraft.world.border.WorldBorder;
/*      */ import net.minecraft.world.chunk.Chunk;
/*      */ import optfine.ChunkUtils;
/*      */ import optfine.CloudRenderer;
/*      */ import optfine.Config;
/*      */ import optfine.CustomColorizer;
/*      */ import optfine.CustomSky;
/*      */ import optfine.Lagometer;
/*      */ import optfine.Lagometer.TimerNano;
/*      */ import optfine.Reflector;
/*      */ import optfine.ReflectorMethod;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ import org.lwjgl.opengl.GL11;
/*      */ import org.lwjgl.util.vector.Vector3f;
/*      */ import org.lwjgl.util.vector.Vector4f;
/*      */ 
/*      */ public class RenderGlobal implements net.minecraft.world.IWorldAccess, IResourceManagerReloadListener
/*      */ {
/*  110 */   private static final Logger logger = ;
/*  111 */   private static final ResourceLocation locationMoonPhasesPng = new ResourceLocation("textures/environment/moon_phases.png");
/*  112 */   private static final ResourceLocation locationSunPng = new ResourceLocation("textures/environment/sun.png");
/*  113 */   private static final ResourceLocation locationCloudsPng = new ResourceLocation("textures/environment/clouds.png");
/*  114 */   private static final ResourceLocation locationEndSkyPng = new ResourceLocation("textures/environment/end_sky.png");
/*  115 */   private static final ResourceLocation locationForcefieldPng = new ResourceLocation("textures/misc/forcefield.png");
/*      */   
/*      */   public final Minecraft mc;
/*      */   
/*      */   private final TextureManager renderEngine;
/*      */   
/*      */   private final RenderManager renderManager;
/*      */   
/*      */   private WorldClient theWorld;
/*  124 */   private Set chunksToUpdate = Sets.newLinkedHashSet();
/*      */   
/*      */ 
/*  127 */   private List renderInfos = com.google.common.collect.Lists.newArrayListWithCapacity(69696);
/*  128 */   private final Set field_181024_n = Sets.newHashSet();
/*      */   
/*      */   private ViewFrustum viewFrustum;
/*      */   
/*  132 */   private int starGLCallList = -1;
/*      */   
/*      */ 
/*  135 */   private int glSkyList = -1;
/*      */   
/*      */ 
/*  138 */   private int glSkyList2 = -1;
/*      */   
/*      */ 
/*      */   private VertexFormat vertexBufferFormat;
/*      */   
/*      */ 
/*      */   private VertexBuffer starVBO;
/*      */   
/*      */ 
/*      */   private VertexBuffer skyVBO;
/*      */   
/*      */   private VertexBuffer sky2VBO;
/*      */   
/*      */   private int cloudTickCounter;
/*      */   
/*  153 */   public final Map damagedBlocks = Maps.newHashMap();
/*      */   
/*      */ 
/*  156 */   private final Map mapSoundPositions = Maps.newHashMap();
/*  157 */   private final TextureAtlasSprite[] destroyBlockIcons = new TextureAtlasSprite[10];
/*      */   
/*      */   private Framebuffer entityOutlineFramebuffer;
/*      */   
/*      */   private ShaderGroup entityOutlineShader;
/*  162 */   private double frustumUpdatePosX = Double.MIN_VALUE;
/*  163 */   private double frustumUpdatePosY = Double.MIN_VALUE;
/*  164 */   private double frustumUpdatePosZ = Double.MIN_VALUE;
/*  165 */   private int frustumUpdatePosChunkX = Integer.MIN_VALUE;
/*  166 */   private int frustumUpdatePosChunkY = Integer.MIN_VALUE;
/*  167 */   private int frustumUpdatePosChunkZ = Integer.MIN_VALUE;
/*  168 */   private double lastViewEntityX = Double.MIN_VALUE;
/*  169 */   private double lastViewEntityY = Double.MIN_VALUE;
/*  170 */   private double lastViewEntityZ = Double.MIN_VALUE;
/*  171 */   private double lastViewEntityPitch = Double.MIN_VALUE;
/*  172 */   private double lastViewEntityYaw = Double.MIN_VALUE;
/*  173 */   private final ChunkRenderDispatcher renderDispatcher = new ChunkRenderDispatcher();
/*      */   private ChunkRenderContainer renderContainer;
/*  175 */   private int renderDistanceChunks = -1;
/*      */   
/*      */ 
/*  178 */   private int renderEntitiesStartupCounter = 2;
/*      */   
/*      */ 
/*      */   private int countEntitiesTotal;
/*      */   
/*      */ 
/*      */   private int countEntitiesRendered;
/*      */   
/*      */   private int countEntitiesHidden;
/*      */   
/*  188 */   private boolean debugFixTerrainFrustum = false;
/*      */   private ClippingHelper debugFixedClippingHelper;
/*  190 */   private final Vector4f[] debugTerrainMatrix = new Vector4f[8];
/*  191 */   private final Vector3d debugTerrainFrustumPosition = new Vector3d();
/*  192 */   private boolean vboEnabled = false;
/*      */   net.minecraft.client.renderer.chunk.IRenderChunkFactory renderChunkFactory;
/*      */   private double prevRenderSortX;
/*      */   private double prevRenderSortY;
/*      */   private double prevRenderSortZ;
/*  197 */   public boolean displayListEntitiesDirty = true;
/*      */   private static final String __OBFID = "CL_00000954";
/*      */   private CloudRenderer cloudRenderer;
/*      */   public Entity renderedEntity;
/*  201 */   public Set chunksToResortTransparency = new LinkedHashSet();
/*  202 */   public Set chunksToUpdateForced = new LinkedHashSet();
/*  203 */   private Deque visibilityDeque = new java.util.ArrayDeque();
/*  204 */   private List renderInfosEntities = new ArrayList(1024);
/*  205 */   private List renderInfosTileEntities = new ArrayList(1024);
/*  206 */   private int renderDistance = 0;
/*  207 */   private int renderDistanceSq = 0;
/*  208 */   private static final Set SET_ALL_FACINGS = java.util.Collections.unmodifiableSet(new java.util.HashSet(Arrays.asList(EnumFacing.VALUES)));
/*      */   private int countTileEntitiesRendered;
/*      */   
/*      */   public RenderGlobal(Minecraft mcIn)
/*      */   {
/*  213 */     this.cloudRenderer = new CloudRenderer(mcIn);
/*  214 */     this.mc = mcIn;
/*  215 */     this.renderManager = mcIn.getRenderManager();
/*  216 */     this.renderEngine = mcIn.getTextureManager();
/*  217 */     this.renderEngine.bindTexture(locationForcefieldPng);
/*  218 */     GL11.glTexParameteri(3553, 10242, 10497);
/*  219 */     GL11.glTexParameteri(3553, 10243, 10497);
/*  220 */     GlStateManager.bindTexture(0);
/*  221 */     updateDestroyBlockIcons();
/*  222 */     this.vboEnabled = OpenGlHelper.useVbo();
/*      */     
/*  224 */     if (this.vboEnabled)
/*      */     {
/*  226 */       this.renderContainer = new VboRenderList();
/*  227 */       this.renderChunkFactory = new VboChunkFactory();
/*      */     }
/*      */     else
/*      */     {
/*  231 */       this.renderContainer = new RenderList();
/*  232 */       this.renderChunkFactory = new ListChunkFactory();
/*      */     }
/*      */     
/*  235 */     this.vertexBufferFormat = new VertexFormat();
/*  236 */     this.vertexBufferFormat.func_181721_a(new VertexFormatElement(0, net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
/*  237 */     generateStars();
/*  238 */     generateSky();
/*  239 */     generateSky2();
/*      */   }
/*      */   
/*      */   public void onResourceManagerReload(IResourceManager resourceManager)
/*      */   {
/*  244 */     updateDestroyBlockIcons();
/*      */   }
/*      */   
/*      */   private void updateDestroyBlockIcons()
/*      */   {
/*  249 */     TextureMap texturemap = this.mc.getTextureMapBlocks();
/*      */     
/*  251 */     for (int i = 0; i < this.destroyBlockIcons.length; i++)
/*      */     {
/*  253 */       this.destroyBlockIcons[i] = texturemap.getAtlasSprite("minecraft:blocks/destroy_stage_" + i);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void makeEntityOutlineShader()
/*      */   {
/*  262 */     if (OpenGlHelper.shadersSupported)
/*      */     {
/*  264 */       if (ShaderLinkHelper.getStaticShaderLinkHelper() == null)
/*      */       {
/*  266 */         ShaderLinkHelper.setNewStaticShaderLinkHelper();
/*      */       }
/*      */       
/*  269 */       ResourceLocation resourcelocation = new ResourceLocation("shaders/post/entity_outline.json");
/*      */       
/*      */       try
/*      */       {
/*  273 */         this.entityOutlineShader = new ShaderGroup(this.mc.getTextureManager(), this.mc.getResourceManager(), this.mc.getFramebuffer(), resourcelocation);
/*  274 */         this.entityOutlineShader.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
/*  275 */         this.entityOutlineFramebuffer = this.entityOutlineShader.getFramebufferRaw("final");
/*      */       }
/*      */       catch (IOException ioexception)
/*      */       {
/*  279 */         logger.warn("Failed to load shader: " + resourcelocation, ioexception);
/*  280 */         this.entityOutlineShader = null;
/*  281 */         this.entityOutlineFramebuffer = null;
/*      */       }
/*      */       catch (JsonSyntaxException jsonsyntaxexception)
/*      */       {
/*  285 */         logger.warn("Failed to load shader: " + resourcelocation, jsonsyntaxexception);
/*  286 */         this.entityOutlineShader = null;
/*  287 */         this.entityOutlineFramebuffer = null;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  292 */       this.entityOutlineShader = null;
/*  293 */       this.entityOutlineFramebuffer = null;
/*      */     }
/*      */   }
/*      */   
/*      */   public void renderEntityOutlineFramebuffer()
/*      */   {
/*  299 */     if (isRenderEntityOutlines())
/*      */     {
/*  301 */       GlStateManager.enableBlend();
/*  302 */       GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
/*  303 */       this.entityOutlineFramebuffer.framebufferRenderExt(this.mc.displayWidth, this.mc.displayHeight, false);
/*  304 */       GlStateManager.disableBlend();
/*      */     }
/*      */   }
/*      */   
/*      */   protected boolean isRenderEntityOutlines()
/*      */   {
/*  310 */     return (this.entityOutlineFramebuffer != null) && (this.entityOutlineShader != null) && (this.mc.thePlayer != null) && (this.mc.thePlayer.isSpectator()) && (this.mc.gameSettings.keyBindSpectatorOutlines.isKeyDown());
/*      */   }
/*      */   
/*      */   private void generateSky2()
/*      */   {
/*  315 */     Tessellator tessellator = Tessellator.getInstance();
/*  316 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*      */     
/*  318 */     if (this.sky2VBO != null)
/*      */     {
/*  320 */       this.sky2VBO.deleteGlBuffers();
/*      */     }
/*      */     
/*  323 */     if (this.glSkyList2 >= 0)
/*      */     {
/*  325 */       GLAllocation.deleteDisplayLists(this.glSkyList2);
/*  326 */       this.glSkyList2 = -1;
/*      */     }
/*      */     
/*  329 */     if (this.vboEnabled)
/*      */     {
/*  331 */       this.sky2VBO = new VertexBuffer(this.vertexBufferFormat);
/*  332 */       renderSky(worldrenderer, -16.0F, true);
/*  333 */       worldrenderer.finishDrawing();
/*  334 */       worldrenderer.reset();
/*  335 */       this.sky2VBO.func_181722_a(worldrenderer.getByteBuffer());
/*      */     }
/*      */     else
/*      */     {
/*  339 */       this.glSkyList2 = GLAllocation.generateDisplayLists(1);
/*  340 */       GL11.glNewList(this.glSkyList2, 4864);
/*  341 */       renderSky(worldrenderer, -16.0F, true);
/*  342 */       tessellator.draw();
/*  343 */       GL11.glEndList();
/*      */     }
/*      */   }
/*      */   
/*      */   private void generateSky()
/*      */   {
/*  349 */     Tessellator tessellator = Tessellator.getInstance();
/*  350 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*      */     
/*  352 */     if (this.skyVBO != null)
/*      */     {
/*  354 */       this.skyVBO.deleteGlBuffers();
/*      */     }
/*      */     
/*  357 */     if (this.glSkyList >= 0)
/*      */     {
/*  359 */       GLAllocation.deleteDisplayLists(this.glSkyList);
/*  360 */       this.glSkyList = -1;
/*      */     }
/*      */     
/*  363 */     if (this.vboEnabled)
/*      */     {
/*  365 */       this.skyVBO = new VertexBuffer(this.vertexBufferFormat);
/*  366 */       renderSky(worldrenderer, 16.0F, false);
/*  367 */       worldrenderer.finishDrawing();
/*  368 */       worldrenderer.reset();
/*  369 */       this.skyVBO.func_181722_a(worldrenderer.getByteBuffer());
/*      */     }
/*      */     else
/*      */     {
/*  373 */       this.glSkyList = GLAllocation.generateDisplayLists(1);
/*  374 */       GL11.glNewList(this.glSkyList, 4864);
/*  375 */       renderSky(worldrenderer, 16.0F, false);
/*  376 */       tessellator.draw();
/*  377 */       GL11.glEndList();
/*      */     }
/*      */   }
/*      */   
/*      */   private void renderSky(WorldRenderer worldRendererIn, float p_174968_2_, boolean p_174968_3_)
/*      */   {
/*  383 */     boolean flag = true;
/*  384 */     boolean flag1 = true;
/*  385 */     worldRendererIn.begin(7, DefaultVertexFormats.POSITION);
/*      */     
/*  387 */     for (int i = 65152; i <= 384; i += 64)
/*      */     {
/*  389 */       for (int j = 65152; j <= 384; j += 64)
/*      */       {
/*  391 */         float f = i;
/*  392 */         float f1 = i + 64;
/*      */         
/*  394 */         if (p_174968_3_)
/*      */         {
/*  396 */           f1 = i;
/*  397 */           f = i + 64;
/*      */         }
/*      */         
/*  400 */         worldRendererIn.pos(f, p_174968_2_, j).endVertex();
/*  401 */         worldRendererIn.pos(f1, p_174968_2_, j).endVertex();
/*  402 */         worldRendererIn.pos(f1, p_174968_2_, j + 64).endVertex();
/*  403 */         worldRendererIn.pos(f, p_174968_2_, j + 64).endVertex();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void generateStars()
/*      */   {
/*  410 */     Tessellator tessellator = Tessellator.getInstance();
/*  411 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*      */     
/*  413 */     if (this.starVBO != null)
/*      */     {
/*  415 */       this.starVBO.deleteGlBuffers();
/*      */     }
/*      */     
/*  418 */     if (this.starGLCallList >= 0)
/*      */     {
/*  420 */       GLAllocation.deleteDisplayLists(this.starGLCallList);
/*  421 */       this.starGLCallList = -1;
/*      */     }
/*      */     
/*  424 */     if (this.vboEnabled)
/*      */     {
/*  426 */       this.starVBO = new VertexBuffer(this.vertexBufferFormat);
/*  427 */       renderStars(worldrenderer);
/*  428 */       worldrenderer.finishDrawing();
/*  429 */       worldrenderer.reset();
/*  430 */       this.starVBO.func_181722_a(worldrenderer.getByteBuffer());
/*      */     }
/*      */     else
/*      */     {
/*  434 */       this.starGLCallList = GLAllocation.generateDisplayLists(1);
/*  435 */       GlStateManager.pushMatrix();
/*  436 */       GL11.glNewList(this.starGLCallList, 4864);
/*  437 */       renderStars(worldrenderer);
/*  438 */       tessellator.draw();
/*  439 */       GL11.glEndList();
/*  440 */       GlStateManager.popMatrix();
/*      */     }
/*      */   }
/*      */   
/*      */   private void renderStars(WorldRenderer worldRendererIn)
/*      */   {
/*  446 */     Random random = new Random(10842L);
/*  447 */     worldRendererIn.begin(7, DefaultVertexFormats.POSITION);
/*      */     
/*  449 */     for (int i = 0; i < 1500; i++)
/*      */     {
/*  451 */       double d0 = random.nextFloat() * 2.0F - 1.0F;
/*  452 */       double d1 = random.nextFloat() * 2.0F - 1.0F;
/*  453 */       double d2 = random.nextFloat() * 2.0F - 1.0F;
/*  454 */       double d3 = 0.15F + random.nextFloat() * 0.1F;
/*  455 */       double d4 = d0 * d0 + d1 * d1 + d2 * d2;
/*      */       
/*  457 */       if ((d4 < 1.0D) && (d4 > 0.01D))
/*      */       {
/*  459 */         d4 = 1.0D / Math.sqrt(d4);
/*  460 */         d0 *= d4;
/*  461 */         d1 *= d4;
/*  462 */         d2 *= d4;
/*  463 */         double d5 = d0 * 100.0D;
/*  464 */         double d6 = d1 * 100.0D;
/*  465 */         double d7 = d2 * 100.0D;
/*  466 */         double d8 = Math.atan2(d0, d2);
/*  467 */         double d9 = Math.sin(d8);
/*  468 */         double d10 = Math.cos(d8);
/*  469 */         double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
/*  470 */         double d12 = Math.sin(d11);
/*  471 */         double d13 = Math.cos(d11);
/*  472 */         double d14 = random.nextDouble() * 3.141592653589793D * 2.0D;
/*  473 */         double d15 = Math.sin(d14);
/*  474 */         double d16 = Math.cos(d14);
/*      */         
/*  476 */         for (int j = 0; j < 4; j++)
/*      */         {
/*  478 */           double d17 = 0.0D;
/*  479 */           double d18 = ((j & 0x2) - 1) * d3;
/*  480 */           double d19 = ((j + 1 & 0x2) - 1) * d3;
/*  481 */           double d20 = 0.0D;
/*  482 */           double d21 = d18 * d16 - d19 * d15;
/*  483 */           double d22 = d19 * d16 + d18 * d15;
/*  484 */           double d23 = d21 * d12 + 0.0D * d13;
/*  485 */           double d24 = 0.0D * d12 - d21 * d13;
/*  486 */           double d25 = d24 * d9 - d22 * d10;
/*  487 */           double d26 = d22 * d9 + d24 * d10;
/*  488 */           worldRendererIn.pos(d5 + d25, d6 + d23, d7 + d26).endVertex();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setWorldAndLoadRenderers(WorldClient worldClientIn)
/*      */   {
/*  499 */     if (this.theWorld != null)
/*      */     {
/*  501 */       this.theWorld.removeWorldAccess(this);
/*      */     }
/*      */     
/*  504 */     this.frustumUpdatePosX = Double.MIN_VALUE;
/*  505 */     this.frustumUpdatePosY = Double.MIN_VALUE;
/*  506 */     this.frustumUpdatePosZ = Double.MIN_VALUE;
/*  507 */     this.frustumUpdatePosChunkX = Integer.MIN_VALUE;
/*  508 */     this.frustumUpdatePosChunkY = Integer.MIN_VALUE;
/*  509 */     this.frustumUpdatePosChunkZ = Integer.MIN_VALUE;
/*  510 */     this.renderManager.set(worldClientIn);
/*  511 */     this.theWorld = worldClientIn;
/*      */     
/*  513 */     if (worldClientIn != null)
/*      */     {
/*  515 */       worldClientIn.addWorldAccess(this);
/*  516 */       loadRenderers();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void loadRenderers()
/*      */   {
/*  525 */     if (this.theWorld != null)
/*      */     {
/*  527 */       this.displayListEntitiesDirty = true;
/*  528 */       Blocks.leaves.setGraphicsLevel(Config.isTreesFancy());
/*  529 */       Blocks.leaves2.setGraphicsLevel(Config.isTreesFancy());
/*  530 */       BlockModelRenderer.updateAoLightValue();
/*  531 */       this.renderDistanceChunks = this.mc.gameSettings.renderDistanceChunks;
/*  532 */       this.renderDistance = (this.renderDistanceChunks * 16);
/*  533 */       this.renderDistanceSq = (this.renderDistance * this.renderDistance);
/*  534 */       boolean flag = this.vboEnabled;
/*  535 */       this.vboEnabled = OpenGlHelper.useVbo();
/*      */       
/*  537 */       if ((flag) && (!this.vboEnabled))
/*      */       {
/*  539 */         this.renderContainer = new RenderList();
/*  540 */         this.renderChunkFactory = new ListChunkFactory();
/*      */       }
/*  542 */       else if ((!flag) && (this.vboEnabled))
/*      */       {
/*  544 */         this.renderContainer = new VboRenderList();
/*  545 */         this.renderChunkFactory = new VboChunkFactory();
/*      */       }
/*      */       
/*  548 */       if (flag != this.vboEnabled)
/*      */       {
/*  550 */         generateStars();
/*  551 */         generateSky();
/*  552 */         generateSky2();
/*      */       }
/*      */       
/*  555 */       if (this.viewFrustum != null)
/*      */       {
/*  557 */         this.viewFrustum.deleteGlResources();
/*      */       }
/*      */       
/*  560 */       stopChunkUpdates();
/*  561 */       Set var5 = this.field_181024_n;
/*      */       
/*  563 */       synchronized (this.field_181024_n)
/*      */       {
/*  565 */         this.field_181024_n.clear();
/*      */       }
/*      */       
/*  568 */       this.viewFrustum = new ViewFrustum(this.theWorld, this.mc.gameSettings.renderDistanceChunks, this, this.renderChunkFactory);
/*      */       
/*  570 */       if (this.theWorld != null)
/*      */       {
/*  572 */         Entity entity = this.mc.getRenderViewEntity();
/*      */         
/*  574 */         if (entity != null)
/*      */         {
/*  576 */           this.viewFrustum.updateChunkPositions(entity.posX, entity.posZ);
/*      */         }
/*      */       }
/*      */       
/*  580 */       this.renderEntitiesStartupCounter = 2;
/*      */     }
/*      */   }
/*      */   
/*      */   protected void stopChunkUpdates()
/*      */   {
/*  586 */     this.chunksToUpdate.clear();
/*  587 */     this.renderDispatcher.stopChunkUpdates();
/*      */   }
/*      */   
/*      */   public void createBindEntityOutlineFbs(int p_72720_1_, int p_72720_2_)
/*      */   {
/*  592 */     if ((OpenGlHelper.shadersSupported) && (this.entityOutlineShader != null))
/*      */     {
/*  594 */       this.entityOutlineShader.createBindFramebuffers(p_72720_1_, p_72720_2_);
/*      */     }
/*      */   }
/*      */   
/*      */   public void renderEntities(Entity renderViewEntity, ICamera camera, float partialTicks)
/*      */   {
/*  600 */     int i = 0;
/*      */     
/*  602 */     if (Reflector.MinecraftForgeClient_getRenderPass.exists())
/*      */     {
/*  604 */       i = Reflector.callInt(Reflector.MinecraftForgeClient_getRenderPass, new Object[0]);
/*      */     }
/*      */     
/*  607 */     if (this.renderEntitiesStartupCounter > 0)
/*      */     {
/*  609 */       if (i > 0)
/*      */       {
/*  611 */         return;
/*      */       }
/*      */       
/*  614 */       this.renderEntitiesStartupCounter -= 1;
/*      */     }
/*      */     else
/*      */     {
/*  618 */       double d0 = renderViewEntity.prevPosX + (renderViewEntity.posX - renderViewEntity.prevPosX) * partialTicks;
/*  619 */       double d1 = renderViewEntity.prevPosY + (renderViewEntity.posY - renderViewEntity.prevPosY) * partialTicks;
/*  620 */       double d2 = renderViewEntity.prevPosZ + (renderViewEntity.posZ - renderViewEntity.prevPosZ) * partialTicks;
/*  621 */       this.theWorld.theProfiler.startSection("prepare");
/*  622 */       TileEntityRendererDispatcher.instance.cacheActiveRenderInfo(this.theWorld, this.mc.getTextureManager(), this.mc.fontRendererObj, this.mc.getRenderViewEntity(), partialTicks);
/*  623 */       this.renderManager.cacheActiveRenderInfo(this.theWorld, this.mc.fontRendererObj, this.mc.getRenderViewEntity(), this.mc.pointedEntity, this.mc.gameSettings, partialTicks);
/*      */       
/*  625 */       if (i == 0)
/*      */       {
/*  627 */         this.countEntitiesTotal = 0;
/*  628 */         this.countEntitiesRendered = 0;
/*  629 */         this.countEntitiesHidden = 0;
/*  630 */         this.countTileEntitiesRendered = 0;
/*      */       }
/*      */       
/*  633 */       Entity entity = this.mc.getRenderViewEntity();
/*  634 */       double d3 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
/*  635 */       double d4 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
/*  636 */       double d5 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
/*  637 */       TileEntityRendererDispatcher.staticPlayerX = d3;
/*  638 */       TileEntityRendererDispatcher.staticPlayerY = d4;
/*  639 */       TileEntityRendererDispatcher.staticPlayerZ = d5;
/*  640 */       this.renderManager.setRenderPosition(d3, d4, d5);
/*  641 */       this.mc.entityRenderer.enableLightmap();
/*  642 */       this.theWorld.theProfiler.endStartSection("global");
/*  643 */       List list = this.theWorld.getLoadedEntityList();
/*      */       
/*  645 */       if (i == 0)
/*      */       {
/*  647 */         this.countEntitiesTotal = list.size();
/*      */       }
/*      */       
/*  650 */       if ((Config.isFogOff()) && (this.mc.entityRenderer.fogStandard))
/*      */       {
/*  652 */         GlStateManager.disableFog();
/*      */       }
/*      */       
/*  655 */       boolean flag = Reflector.ForgeEntity_shouldRenderInPass.exists();
/*  656 */       boolean flag1 = Reflector.ForgeTileEntity_shouldRenderInPass.exists();
/*      */       
/*  658 */       for (int j = 0; j < this.theWorld.weatherEffects.size(); j++)
/*      */       {
/*  660 */         Entity entity1 = (Entity)this.theWorld.weatherEffects.get(j);
/*      */         
/*  662 */         if (flag) { if (!Reflector.callBoolean(entity1, Reflector.ForgeEntity_shouldRenderInPass, new Object[] { Integer.valueOf(i) })) {}
/*      */         } else {
/*  664 */           this.countEntitiesRendered += 1;
/*      */           
/*  666 */           if (entity1.isInRangeToRender3d(d0, d1, d2))
/*      */           {
/*  668 */             this.renderManager.renderEntitySimple(entity1, partialTicks);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  673 */       if (isRenderEntityOutlines())
/*      */       {
/*  675 */         GlStateManager.depthFunc(519);
/*  676 */         GlStateManager.disableFog();
/*  677 */         this.entityOutlineFramebuffer.framebufferClear();
/*  678 */         this.entityOutlineFramebuffer.bindFramebuffer(false);
/*  679 */         this.theWorld.theProfiler.endStartSection("entityOutlines");
/*  680 */         RenderHelper.disableStandardItemLighting();
/*  681 */         this.renderManager.setRenderOutlines(true);
/*      */         
/*  683 */         for (int k = 0; k < list.size(); k++)
/*      */         {
/*  685 */           Entity entity3 = (Entity)list.get(k);
/*      */           
/*  687 */           if (flag) { if (!Reflector.callBoolean(entity3, Reflector.ForgeEntity_shouldRenderInPass, new Object[] { Integer.valueOf(i) })) {}
/*      */           } else {
/*  689 */             boolean flag2 = ((this.mc.getRenderViewEntity() instanceof EntityLivingBase)) && (((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping());
/*  690 */             boolean flag3 = (entity3.isInRangeToRender3d(d0, d1, d2)) && ((entity3.ignoreFrustumCheck) || (camera.isBoundingBoxInFrustum(entity3.getEntityBoundingBox())) || (entity3.riddenByEntity == this.mc.thePlayer)) && ((entity3 instanceof EntityPlayer));
/*      */             
/*  692 */             if (((entity3 != this.mc.getRenderViewEntity()) || (this.mc.gameSettings.thirdPersonView != 0) || (flag2)) && (flag3))
/*      */             {
/*  694 */               this.renderManager.renderEntitySimple(entity3, partialTicks);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  699 */         this.renderManager.setRenderOutlines(false);
/*  700 */         RenderHelper.enableStandardItemLighting();
/*  701 */         GlStateManager.depthMask(false);
/*  702 */         this.entityOutlineShader.loadShaderGroup(partialTicks);
/*  703 */         GlStateManager.enableLighting();
/*  704 */         GlStateManager.depthMask(true);
/*  705 */         this.mc.getFramebuffer().bindFramebuffer(false);
/*  706 */         GlStateManager.enableFog();
/*  707 */         GlStateManager.enableBlend();
/*  708 */         GlStateManager.enableColorMaterial();
/*  709 */         GlStateManager.depthFunc(515);
/*  710 */         GlStateManager.enableDepth();
/*  711 */         GlStateManager.enableAlpha();
/*      */       }
/*      */       
/*  714 */       this.theWorld.theProfiler.endStartSection("entities");
/*  715 */       Iterator iterator1 = this.renderInfosEntities.iterator();
/*  716 */       boolean flag4 = this.mc.gameSettings.fancyGraphics;
/*  717 */       this.mc.gameSettings.fancyGraphics = Config.isDroppedItemsFancy();
/*      */       
/*      */       ClassInheritanceMultiMap classinheritancemultimap;
/*  720 */       while (iterator1.hasNext())
/*      */       {
/*  722 */         ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation = (ContainerLocalRenderInformation)iterator1.next();
/*  723 */         Chunk chunk = this.theWorld.getChunkFromBlockCoords(renderglobal$containerlocalrenderinformation.renderChunk.getPosition());
/*  724 */         classinheritancemultimap = chunk.getEntityLists()[(renderglobal$containerlocalrenderinformation.renderChunk.getPosition().getY() / 16)];
/*      */         
/*  726 */         if (!classinheritancemultimap.isEmpty())
/*      */         {
/*  728 */           Iterator iterator = classinheritancemultimap.iterator();
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  737 */           while (iterator.hasNext())
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*  742 */             Entity entity2 = (Entity)iterator.next();
/*      */             
/*  744 */             if (flag) { if (!Reflector.callBoolean(entity2, Reflector.ForgeEntity_shouldRenderInPass, new Object[] { Integer.valueOf(i) })) {}
/*      */             } else {
/*  746 */               boolean flag5 = (this.renderManager.shouldRender(entity2, camera, d0, d1, d2)) || (entity2.riddenByEntity == this.mc.thePlayer);
/*      */               
/*  748 */               if (flag5)
/*      */               {
/*      */ 
/*      */ 
/*      */ 
/*  753 */                 boolean flag6 = (this.mc.getRenderViewEntity() instanceof EntityLivingBase) ? ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping() : false;
/*      */                 
/*  755 */                 if (((entity2 != this.mc.getRenderViewEntity()) || (this.mc.gameSettings.thirdPersonView != 0) || (flag6)) && ((entity2.posY < 0.0D) || (entity2.posY >= 256.0D) || (this.theWorld.isBlockLoaded(new BlockPos(entity2)))))
/*      */                 {
/*  757 */                   this.countEntitiesRendered += 1;
/*      */                   
/*  759 */                   if (entity2.getClass() == net.minecraft.entity.item.EntityItemFrame.class)
/*      */                   {
/*  761 */                     entity2.renderDistanceWeight = 0.06D;
/*      */                   }
/*      */                   
/*  764 */                   this.renderedEntity = entity2;
/*  765 */                   this.renderManager.renderEntitySimple(entity2, partialTicks);
/*  766 */                   this.renderedEntity = null;
/*      */ 
/*      */                 }
/*      */                 
/*      */ 
/*      */               }
/*  772 */               else if ((!flag5) && ((entity2 instanceof net.minecraft.entity.projectile.EntityWitherSkull)))
/*      */               {
/*  774 */                 this.mc.getRenderManager().renderWitherSkull(entity2, partialTicks);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  780 */       this.mc.gameSettings.fancyGraphics = flag4;
/*  781 */       net.minecraft.client.gui.FontRenderer fontrenderer = TileEntityRendererDispatcher.instance.getFontRenderer();
/*  782 */       this.theWorld.theProfiler.endStartSection("blockentities");
/*  783 */       RenderHelper.enableStandardItemLighting();
/*      */       
/*      */       List list1;
/*  786 */       for (Object renderglobal$containerlocalrenderinformation10 : this.renderInfosTileEntities)
/*      */       {
/*  788 */         ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation1 = (ContainerLocalRenderInformation)renderglobal$containerlocalrenderinformation10;
/*  789 */         list1 = renderglobal$containerlocalrenderinformation1.renderChunk.getCompiledChunk().getTileEntities();
/*      */         
/*  791 */         if (!list1.isEmpty())
/*      */         {
/*  793 */           Iterator iterator2 = list1.iterator();
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  801 */           while (iterator2.hasNext())
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*  806 */             TileEntity tileentity = (TileEntity)iterator2.next();
/*      */             
/*  808 */             if (flag1)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*  813 */               if (Reflector.callBoolean(tileentity, Reflector.ForgeTileEntity_shouldRenderInPass, new Object[] { Integer.valueOf(i) }))
/*      */               {
/*  815 */                 AxisAlignedBB axisalignedbb = (AxisAlignedBB)Reflector.call(tileentity, Reflector.ForgeTileEntity_getRenderBoundingBox, new Object[0]);
/*      */                 
/*  817 */                 if ((axisalignedbb != null) && (!camera.isBoundingBoxInFrustum(axisalignedbb))) {}
/*      */               }
/*      */               
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*  824 */               Class oclass = tileentity.getClass();
/*      */               
/*  826 */               if ((oclass == TileEntitySign.class) && (!Config.zoomMode))
/*      */               {
/*  828 */                 EntityPlayer entityplayer = this.mc.thePlayer;
/*  829 */                 double d6 = tileentity.getDistanceSq(entityplayer.posX, entityplayer.posY, entityplayer.posZ);
/*      */                 
/*  831 */                 if (d6 > 256.0D)
/*      */                 {
/*  833 */                   fontrenderer.enabled = false;
/*      */                 }
/*      */               }
/*      */               
/*  837 */               TileEntityRendererDispatcher.instance.renderTileEntity(tileentity, partialTicks, -1);
/*  838 */               this.countTileEntitiesRendered += 1;
/*  839 */               fontrenderer.enabled = true;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  844 */       Set var32 = this.field_181024_n;
/*      */       
/*  846 */       synchronized (this.field_181024_n)
/*      */       {
/*  848 */         for (list1 = this.field_181024_n.iterator(); list1.hasNext();) { tileentity1 = list1.next();
/*      */           
/*  850 */           if (flag1)
/*      */           {
/*  852 */             if (Reflector.callBoolean(tileentity1, Reflector.ForgeTileEntity_shouldRenderInPass, new Object[] { Integer.valueOf(i) }))
/*      */             {
/*      */ 
/*      */ 
/*  856 */               AxisAlignedBB axisalignedbb1 = (AxisAlignedBB)Reflector.call(tileentity1, Reflector.ForgeTileEntity_getRenderBoundingBox, new Object[0]);
/*      */               
/*  858 */               if ((axisalignedbb1 != null) && (!camera.isBoundingBoxInFrustum(axisalignedbb1))) {}
/*      */             }
/*      */             
/*      */           }
/*      */           else
/*      */           {
/*  864 */             Class oclass1 = tileentity1.getClass();
/*      */             
/*  866 */             if ((oclass1 == TileEntitySign.class) && (!Config.zoomMode))
/*      */             {
/*  868 */               EntityPlayer entityplayer1 = this.mc.thePlayer;
/*  869 */               double d7 = ((TileEntity)tileentity1).getDistanceSq(entityplayer1.posX, entityplayer1.posY, entityplayer1.posZ);
/*      */               
/*  871 */               if (d7 > 256.0D)
/*      */               {
/*  873 */                 fontrenderer.enabled = false;
/*      */               }
/*      */             }
/*      */             
/*  877 */             TileEntityRendererDispatcher.instance.renderTileEntity((TileEntity)tileentity1, partialTicks, -1);
/*  878 */             fontrenderer.enabled = true;
/*      */           }
/*      */         }
/*      */       }
/*  882 */       preRenderDamagedBlocks();
/*      */       
/*  884 */       for (Object tileentity1 = this.damagedBlocks.values().iterator(); ((Iterator)tileentity1).hasNext();) { Object destroyblockprogress = ((Iterator)tileentity1).next();
/*      */         
/*  886 */         BlockPos blockpos = ((DestroyBlockProgress)destroyblockprogress).getPosition();
/*  887 */         TileEntity tileentity2 = this.theWorld.getTileEntity(blockpos);
/*      */         
/*  889 */         if ((tileentity2 instanceof TileEntityChest))
/*      */         {
/*  891 */           TileEntityChest tileentitychest = (TileEntityChest)tileentity2;
/*      */           
/*  893 */           if (tileentitychest.adjacentChestXNeg != null)
/*      */           {
/*  895 */             blockpos = blockpos.offset(EnumFacing.WEST);
/*  896 */             tileentity2 = this.theWorld.getTileEntity(blockpos);
/*      */           }
/*  898 */           else if (tileentitychest.adjacentChestZNeg != null)
/*      */           {
/*  900 */             blockpos = blockpos.offset(EnumFacing.NORTH);
/*  901 */             tileentity2 = this.theWorld.getTileEntity(blockpos);
/*      */           }
/*      */         }
/*      */         
/*  905 */         Block block = this.theWorld.getBlockState(blockpos).getBlock();
/*      */         
/*      */         boolean flag7;
/*  908 */         if (flag1)
/*      */         {
/*  910 */           boolean flag7 = false;
/*      */           
/*  912 */           if (tileentity2 != null) if ((Reflector.callBoolean(tileentity2, Reflector.ForgeTileEntity_shouldRenderInPass, new Object[] { Integer.valueOf(i) })) && (Reflector.callBoolean(tileentity2, Reflector.ForgeTileEntity_canRenderBreaking, new Object[0])))
/*      */             {
/*  914 */               AxisAlignedBB axisalignedbb2 = (AxisAlignedBB)Reflector.call(tileentity2, Reflector.ForgeTileEntity_getRenderBoundingBox, new Object[0]);
/*      */               
/*  916 */               if (axisalignedbb2 != null)
/*      */               {
/*  918 */                 flag7 = camera.isBoundingBoxInFrustum(axisalignedbb2);
/*      */               }
/*      */             }
/*      */         }
/*      */         else
/*      */         {
/*  924 */           flag7 = (tileentity2 != null) && (((block instanceof BlockChest)) || ((block instanceof BlockEnderChest)) || ((block instanceof BlockSign)) || ((block instanceof BlockSkull)));
/*      */         }
/*      */         
/*  927 */         if (flag7)
/*      */         {
/*  929 */           TileEntityRendererDispatcher.instance.renderTileEntity(tileentity2, partialTicks, ((DestroyBlockProgress)destroyblockprogress).getPartialBlockDamage());
/*      */         }
/*      */       }
/*      */       
/*  933 */       postRenderDamagedBlocks();
/*  934 */       this.mc.entityRenderer.disableLightmap();
/*  935 */       this.mc.mcProfiler.endSection();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDebugInfoRenders()
/*      */   {
/*  944 */     int i = this.viewFrustum.renderChunks.length;
/*  945 */     int j = 0;
/*      */     
/*  947 */     for (Object renderglobal$containerlocalrenderinformation0 : this.renderInfos)
/*      */     {
/*  949 */       ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation = (ContainerLocalRenderInformation)renderglobal$containerlocalrenderinformation0;
/*  950 */       CompiledChunk compiledchunk = renderglobal$containerlocalrenderinformation.renderChunk.compiledChunk;
/*      */       
/*  952 */       if ((compiledchunk != CompiledChunk.DUMMY) && (!compiledchunk.isEmpty()))
/*      */       {
/*  954 */         j++;
/*      */       }
/*      */     }
/*      */     
/*  958 */     return String.format("C: %d/%d %sD: %d, %s", new Object[] { Integer.valueOf(j), Integer.valueOf(i), this.mc.renderChunksMany ? "(s) " : "", Integer.valueOf(this.renderDistanceChunks), this.renderDispatcher.getDebugInfo() });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDebugInfoEntities()
/*      */   {
/*  966 */     return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ", B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered) + ", " + Config.getVersion();
/*      */   }
/*      */   
/*      */   public void setupTerrain(Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator)
/*      */   {
/*  971 */     if (this.mc.gameSettings.renderDistanceChunks != this.renderDistanceChunks)
/*      */     {
/*  973 */       loadRenderers();
/*      */     }
/*      */     
/*  976 */     this.theWorld.theProfiler.startSection("camera");
/*  977 */     double d0 = viewEntity.posX - this.frustumUpdatePosX;
/*  978 */     double d1 = viewEntity.posY - this.frustumUpdatePosY;
/*  979 */     double d2 = viewEntity.posZ - this.frustumUpdatePosZ;
/*      */     
/*  981 */     if ((this.frustumUpdatePosChunkX != viewEntity.chunkCoordX) || (this.frustumUpdatePosChunkY != viewEntity.chunkCoordY) || (this.frustumUpdatePosChunkZ != viewEntity.chunkCoordZ) || (d0 * d0 + d1 * d1 + d2 * d2 > 16.0D))
/*      */     {
/*  983 */       this.frustumUpdatePosX = viewEntity.posX;
/*  984 */       this.frustumUpdatePosY = viewEntity.posY;
/*  985 */       this.frustumUpdatePosZ = viewEntity.posZ;
/*  986 */       this.frustumUpdatePosChunkX = viewEntity.chunkCoordX;
/*  987 */       this.frustumUpdatePosChunkY = viewEntity.chunkCoordY;
/*  988 */       this.frustumUpdatePosChunkZ = viewEntity.chunkCoordZ;
/*  989 */       this.viewFrustum.updateChunkPositions(viewEntity.posX, viewEntity.posZ);
/*      */     }
/*      */     
/*  992 */     this.theWorld.theProfiler.endStartSection("renderlistcamera");
/*  993 */     double d3 = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * partialTicks;
/*  994 */     double d4 = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * partialTicks;
/*  995 */     double d5 = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * partialTicks;
/*  996 */     this.renderContainer.initialize(d3, d4, d5);
/*  997 */     this.theWorld.theProfiler.endStartSection("cull");
/*      */     
/*  999 */     if (this.debugFixedClippingHelper != null)
/*      */     {
/* 1001 */       Frustum frustum = new Frustum(this.debugFixedClippingHelper);
/* 1002 */       frustum.setPosition(this.debugTerrainFrustumPosition.field_181059_a, this.debugTerrainFrustumPosition.field_181060_b, this.debugTerrainFrustumPosition.field_181061_c);
/* 1003 */       camera = frustum;
/*      */     }
/*      */     
/* 1006 */     this.mc.mcProfiler.endStartSection("culling");
/* 1007 */     BlockPos blockpos1 = new BlockPos(d3, d4 + viewEntity.getEyeHeight(), d5);
/* 1008 */     RenderChunk renderchunk = this.viewFrustum.getRenderChunk(blockpos1);
/* 1009 */     BlockPos blockpos = new BlockPos(MathHelper.floor_double(d3 / 16.0D) * 16, MathHelper.floor_double(d4 / 16.0D) * 16, MathHelper.floor_double(d5 / 16.0D) * 16);
/* 1010 */     this.displayListEntitiesDirty = ((this.displayListEntitiesDirty) || (!this.chunksToUpdate.isEmpty()) || (viewEntity.posX != this.lastViewEntityX) || (viewEntity.posY != this.lastViewEntityY) || (viewEntity.posZ != this.lastViewEntityZ) || (viewEntity.rotationPitch != this.lastViewEntityPitch) || (viewEntity.rotationYaw != this.lastViewEntityYaw));
/* 1011 */     this.lastViewEntityX = viewEntity.posX;
/* 1012 */     this.lastViewEntityY = viewEntity.posY;
/* 1013 */     this.lastViewEntityZ = viewEntity.posZ;
/* 1014 */     this.lastViewEntityPitch = viewEntity.rotationPitch;
/* 1015 */     this.lastViewEntityYaw = viewEntity.rotationYaw;
/* 1016 */     boolean flag = this.debugFixedClippingHelper != null;
/* 1017 */     Lagometer.timerVisibility.start();
/*      */     
/* 1019 */     if ((!flag) && (this.displayListEntitiesDirty))
/*      */     {
/* 1021 */       this.displayListEntitiesDirty = false;
/* 1022 */       this.renderInfos.clear();
/* 1023 */       this.renderInfosEntities.clear();
/* 1024 */       this.renderInfosTileEntities.clear();
/* 1025 */       this.visibilityDeque.clear();
/* 1026 */       Deque deque = this.visibilityDeque;
/* 1027 */       boolean flag1 = this.mc.renderChunksMany;
/*      */       
/* 1029 */       if (renderchunk != null)
/*      */       {
/* 1031 */         boolean flag2 = false;
/* 1032 */         ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation3 = new ContainerLocalRenderInformation(renderchunk, null, 0, null);
/* 1033 */         Set set1 = SET_ALL_FACINGS;
/*      */         
/* 1035 */         if (set1.size() == 1)
/*      */         {
/* 1037 */           Vector3f vector3f = getViewVector(viewEntity, partialTicks);
/* 1038 */           EnumFacing enumfacing = EnumFacing.getFacingFromVector(vector3f.x, vector3f.y, vector3f.z).getOpposite();
/* 1039 */           set1.remove(enumfacing);
/*      */         }
/*      */         
/* 1042 */         if (set1.isEmpty())
/*      */         {
/* 1044 */           flag2 = true;
/*      */         }
/*      */         
/* 1047 */         if ((flag2) && (!playerSpectator))
/*      */         {
/* 1049 */           this.renderInfos.add(renderglobal$containerlocalrenderinformation3);
/*      */         }
/*      */         else
/*      */         {
/* 1053 */           if ((playerSpectator) && (this.theWorld.getBlockState(blockpos1).getBlock().isOpaqueCube()))
/*      */           {
/* 1055 */             flag1 = false;
/*      */           }
/*      */           
/* 1058 */           renderchunk.setFrameIndex(frameCount);
/* 1059 */           deque.add(renderglobal$containerlocalrenderinformation3);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1064 */         int i = blockpos1.getY() > 0 ? 248 : 8;
/*      */         
/* 1066 */         for (int j = -this.renderDistanceChunks; j <= this.renderDistanceChunks; j++)
/*      */         {
/* 1068 */           for (int k = -this.renderDistanceChunks; k <= this.renderDistanceChunks; k++)
/*      */           {
/* 1070 */             RenderChunk renderchunk2 = this.viewFrustum.getRenderChunk(new BlockPos((j << 4) + 8, i, (k << 4) + 8));
/*      */             
/* 1072 */             if ((renderchunk2 != null) && (camera.isBoundingBoxInFrustum(renderchunk2.boundingBox)))
/*      */             {
/* 1074 */               renderchunk2.setFrameIndex(frameCount);
/* 1075 */               deque.add(new ContainerLocalRenderInformation(renderchunk2, null, 0, null));
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1081 */       EnumFacing[] aenumfacing = EnumFacing.VALUES;
/* 1082 */       int l = aenumfacing.length;
/*      */       int i1;
/* 1084 */       for (; !deque.isEmpty(); 
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
/* 1106 */           i1 < l)
/*      */       {
/* 1086 */         ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation = (ContainerLocalRenderInformation)deque.poll();
/* 1087 */         RenderChunk renderchunk1 = renderglobal$containerlocalrenderinformation.renderChunk;
/* 1088 */         EnumFacing enumfacing2 = renderglobal$containerlocalrenderinformation.facing;
/* 1089 */         BlockPos blockpos2 = renderchunk1.getPosition();
/*      */         
/* 1091 */         if ((!renderchunk1.compiledChunk.isEmpty()) || (renderchunk1.isNeedsUpdate()))
/*      */         {
/* 1093 */           this.renderInfos.add(renderglobal$containerlocalrenderinformation);
/*      */         }
/*      */         
/* 1096 */         if (ChunkUtils.hasEntities(this.theWorld.getChunkFromBlockCoords(blockpos2)))
/*      */         {
/* 1098 */           this.renderInfosEntities.add(renderglobal$containerlocalrenderinformation);
/*      */         }
/*      */         
/* 1101 */         if (renderchunk1.getCompiledChunk().getTileEntities().size() > 0)
/*      */         {
/* 1103 */           this.renderInfosTileEntities.add(renderglobal$containerlocalrenderinformation);
/*      */         }
/*      */         
/* 1106 */         i1 = 0; continue;
/*      */         
/* 1108 */         EnumFacing enumfacing1 = aenumfacing[i1];
/*      */         
/* 1110 */         if (((!flag1) || (!renderglobal$containerlocalrenderinformation.setFacing.contains(enumfacing1.getOpposite()))) && ((!flag1) || (enumfacing2 == null) || (renderchunk1.getCompiledChunk().isVisible(enumfacing2.getOpposite(), enumfacing1))))
/*      */         {
/* 1112 */           RenderChunk renderchunk3 = func_181562_a(blockpos1, renderchunk1, enumfacing1);
/*      */           
/* 1114 */           if ((renderchunk3 != null) && (renderchunk3.setFrameIndex(frameCount)) && (camera.isBoundingBoxInFrustum(renderchunk3.boundingBox)))
/*      */           {
/* 1116 */             ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation1 = new ContainerLocalRenderInformation(renderchunk3, enumfacing1, renderglobal$containerlocalrenderinformation.counter + 1, null);
/* 1117 */             renderglobal$containerlocalrenderinformation1.setFacing.addAll(renderglobal$containerlocalrenderinformation.setFacing);
/* 1118 */             renderglobal$containerlocalrenderinformation1.setFacing.add(enumfacing1);
/* 1119 */             deque.add(renderglobal$containerlocalrenderinformation1);
/*      */           }
/*      */         }
/* 1106 */         i1++;
/*      */       }
/*      */     }
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
/* 1126 */     if (this.debugFixTerrainFrustum)
/*      */     {
/* 1128 */       fixTerrainFrustum(d3, d4, d5);
/* 1129 */       this.debugFixTerrainFrustum = false;
/*      */     }
/*      */     
/* 1132 */     Lagometer.timerVisibility.end();
/* 1133 */     this.renderDispatcher.clearChunkUpdates();
/* 1134 */     Set set = this.chunksToUpdate;
/* 1135 */     this.chunksToUpdate = Sets.newLinkedHashSet();
/* 1136 */     Iterator iterator = this.renderInfos.iterator();
/* 1137 */     Lagometer.timerChunkUpdate.start();
/*      */     
/* 1139 */     while (iterator.hasNext())
/*      */     {
/* 1141 */       ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation2 = (ContainerLocalRenderInformation)iterator.next();
/* 1142 */       RenderChunk renderchunk4 = renderglobal$containerlocalrenderinformation2.renderChunk;
/*      */       
/* 1144 */       if ((renderchunk4.isNeedsUpdate()) || (set.contains(renderchunk4)))
/*      */       {
/* 1146 */         this.displayListEntitiesDirty = true;
/*      */         
/* 1148 */         if (isPositionInRenderChunk(blockpos, renderglobal$containerlocalrenderinformation2.renderChunk))
/*      */         {
/* 1150 */           if (!Config.isActing())
/*      */           {
/* 1152 */             this.chunksToUpdateForced.add(renderchunk4);
/*      */           }
/*      */           else
/*      */           {
/* 1156 */             this.mc.mcProfiler.startSection("build near");
/* 1157 */             this.renderDispatcher.updateChunkNow(renderchunk4);
/* 1158 */             renderchunk4.setNeedsUpdate(false);
/* 1159 */             this.mc.mcProfiler.endSection();
/*      */           }
/*      */           
/*      */         }
/*      */         else {
/* 1164 */           this.chunksToUpdate.add(renderchunk4);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1169 */     Lagometer.timerChunkUpdate.end();
/* 1170 */     this.chunksToUpdate.addAll(set);
/* 1171 */     this.mc.mcProfiler.endSection();
/*      */   }
/*      */   
/*      */   private boolean isPositionInRenderChunk(BlockPos pos, RenderChunk renderChunkIn)
/*      */   {
/* 1176 */     BlockPos blockpos = renderChunkIn.getPosition();
/* 1177 */     return MathHelper.abs_int(pos.getX() - blockpos.getX()) <= 16;
/*      */   }
/*      */   
/*      */   private Set getVisibleFacings(BlockPos pos)
/*      */   {
/* 1182 */     VisGraph visgraph = new VisGraph();
/* 1183 */     BlockPos blockpos = new BlockPos(pos.getX() >> 4 << 4, pos.getY() >> 4 << 4, pos.getZ() >> 4 << 4);
/* 1184 */     Chunk chunk = this.theWorld.getChunkFromBlockCoords(blockpos);
/*      */     
/* 1186 */     for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(blockpos, blockpos.add(15, 15, 15)))
/*      */     {
/* 1188 */       if (chunk.getBlock(blockpos$mutableblockpos).isOpaqueCube())
/*      */       {
/* 1190 */         visgraph.func_178606_a(blockpos$mutableblockpos);
/*      */       }
/*      */     }
/* 1193 */     return visgraph.func_178609_b(pos);
/*      */   }
/*      */   
/*      */   private RenderChunk func_181562_a(BlockPos p_181562_1_, RenderChunk p_181562_2_, EnumFacing p_181562_3_)
/*      */   {
/* 1198 */     BlockPos blockpos = p_181562_2_.getPositionOffset16(p_181562_3_);
/*      */     
/* 1200 */     if ((blockpos.getY() >= 0) && (blockpos.getY() < 256))
/*      */     {
/* 1202 */       int i = MathHelper.abs_int(p_181562_1_.getX() - blockpos.getX());
/* 1203 */       int j = MathHelper.abs_int(p_181562_1_.getZ() - blockpos.getZ());
/*      */       
/* 1205 */       if (Config.isFogOff())
/*      */       {
/* 1207 */         if ((i > this.renderDistance) || (j > this.renderDistance))
/*      */         {
/* 1209 */           return null;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1214 */         int k = i * i + j * j;
/*      */         
/* 1216 */         if (k > this.renderDistanceSq)
/*      */         {
/* 1218 */           return null;
/*      */         }
/*      */       }
/*      */       
/* 1222 */       return this.viewFrustum.getRenderChunk(blockpos);
/*      */     }
/*      */     
/*      */ 
/* 1226 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   private void fixTerrainFrustum(double x, double y, double z)
/*      */   {
/* 1232 */     this.debugFixedClippingHelper = new ClippingHelperImpl();
/* 1233 */     ((ClippingHelperImpl)this.debugFixedClippingHelper).init();
/* 1234 */     Matrix4f matrix4f = new Matrix4f(this.debugFixedClippingHelper.modelviewMatrix);
/* 1235 */     matrix4f.transpose();
/* 1236 */     Matrix4f matrix4f1 = new Matrix4f(this.debugFixedClippingHelper.projectionMatrix);
/* 1237 */     matrix4f1.transpose();
/* 1238 */     Matrix4f matrix4f2 = new Matrix4f();
/* 1239 */     Matrix4f.mul(matrix4f1, matrix4f, matrix4f2);
/* 1240 */     matrix4f2.invert();
/* 1241 */     this.debugTerrainFrustumPosition.field_181059_a = x;
/* 1242 */     this.debugTerrainFrustumPosition.field_181060_b = y;
/* 1243 */     this.debugTerrainFrustumPosition.field_181061_c = z;
/* 1244 */     this.debugTerrainMatrix[0] = new Vector4f(-1.0F, -1.0F, -1.0F, 1.0F);
/* 1245 */     this.debugTerrainMatrix[1] = new Vector4f(1.0F, -1.0F, -1.0F, 1.0F);
/* 1246 */     this.debugTerrainMatrix[2] = new Vector4f(1.0F, 1.0F, -1.0F, 1.0F);
/* 1247 */     this.debugTerrainMatrix[3] = new Vector4f(-1.0F, 1.0F, -1.0F, 1.0F);
/* 1248 */     this.debugTerrainMatrix[4] = new Vector4f(-1.0F, -1.0F, 1.0F, 1.0F);
/* 1249 */     this.debugTerrainMatrix[5] = new Vector4f(1.0F, -1.0F, 1.0F, 1.0F);
/* 1250 */     this.debugTerrainMatrix[6] = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 1251 */     this.debugTerrainMatrix[7] = new Vector4f(-1.0F, 1.0F, 1.0F, 1.0F);
/*      */     
/* 1253 */     for (int i = 0; i < 8; i++)
/*      */     {
/* 1255 */       Matrix4f.transform(matrix4f2, this.debugTerrainMatrix[i], this.debugTerrainMatrix[i]);
/* 1256 */       this.debugTerrainMatrix[i].x /= this.debugTerrainMatrix[i].w;
/* 1257 */       this.debugTerrainMatrix[i].y /= this.debugTerrainMatrix[i].w;
/* 1258 */       this.debugTerrainMatrix[i].z /= this.debugTerrainMatrix[i].w;
/* 1259 */       this.debugTerrainMatrix[i].w = 1.0F;
/*      */     }
/*      */   }
/*      */   
/*      */   protected Vector3f getViewVector(Entity entityIn, double partialTicks)
/*      */   {
/* 1265 */     float f = (float)(entityIn.prevRotationPitch + (entityIn.rotationPitch - entityIn.prevRotationPitch) * partialTicks);
/* 1266 */     float f1 = (float)(entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks);
/*      */     
/* 1268 */     if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2)
/*      */     {
/* 1270 */       f += 180.0F;
/*      */     }
/*      */     
/* 1273 */     float f2 = MathHelper.cos(-f1 * 0.017453292F - 3.1415927F);
/* 1274 */     float f3 = MathHelper.sin(-f1 * 0.017453292F - 3.1415927F);
/* 1275 */     float f4 = -MathHelper.cos(-f * 0.017453292F);
/* 1276 */     float f5 = MathHelper.sin(-f * 0.017453292F);
/* 1277 */     return new Vector3f(f3 * f4, f5, f2 * f4);
/*      */   }
/*      */   
/*      */ 
/*      */   public int renderBlockLayer(EnumWorldBlockLayer blockLayerIn, double partialTicks, int pass, Entity entityIn)
/*      */   {
/*      */     
/* 1284 */     if (blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT)
/*      */     {
/* 1286 */       this.mc.mcProfiler.startSection("translucent_sort");
/* 1287 */       double d0 = entityIn.posX - this.prevRenderSortX;
/* 1288 */       double d1 = entityIn.posY - this.prevRenderSortY;
/* 1289 */       double d2 = entityIn.posZ - this.prevRenderSortZ;
/*      */       
/* 1291 */       if (d0 * d0 + d1 * d1 + d2 * d2 > 1.0D)
/*      */       {
/* 1293 */         this.prevRenderSortX = entityIn.posX;
/* 1294 */         this.prevRenderSortY = entityIn.posY;
/* 1295 */         this.prevRenderSortZ = entityIn.posZ;
/* 1296 */         int k = 0;
/* 1297 */         Iterator iterator = this.renderInfos.iterator();
/* 1298 */         this.chunksToResortTransparency.clear();
/*      */         
/* 1300 */         while (iterator.hasNext())
/*      */         {
/* 1302 */           ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation = (ContainerLocalRenderInformation)iterator.next();
/*      */           
/* 1304 */           if ((renderglobal$containerlocalrenderinformation.renderChunk.compiledChunk.isLayerStarted(blockLayerIn)) && (k++ < 15))
/*      */           {
/* 1306 */             this.chunksToResortTransparency.add(renderglobal$containerlocalrenderinformation.renderChunk);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1311 */       this.mc.mcProfiler.endSection();
/*      */     }
/*      */     
/* 1314 */     this.mc.mcProfiler.startSection("filterempty");
/* 1315 */     int l = 0;
/* 1316 */     boolean flag = blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT;
/* 1317 */     int i1 = flag ? this.renderInfos.size() - 1 : 0;
/* 1318 */     int i = flag ? -1 : this.renderInfos.size();
/* 1319 */     int j1 = flag ? -1 : 1;
/*      */     
/* 1321 */     for (int j = i1; j != i; j += j1)
/*      */     {
/* 1323 */       RenderChunk renderchunk = ((ContainerLocalRenderInformation)this.renderInfos.get(j)).renderChunk;
/*      */       
/* 1325 */       if (!renderchunk.getCompiledChunk().isLayerEmpty(blockLayerIn))
/*      */       {
/* 1327 */         l++;
/* 1328 */         this.renderContainer.addRenderChunk(renderchunk, blockLayerIn);
/*      */       }
/*      */     }
/*      */     
/* 1332 */     if (l == 0)
/*      */     {
/* 1334 */       return l;
/*      */     }
/*      */     
/*      */ 
/* 1338 */     if ((Config.isFogOff()) && (this.mc.entityRenderer.fogStandard))
/*      */     {
/* 1340 */       GlStateManager.disableFog();
/*      */     }
/*      */     
/* 1343 */     this.mc.mcProfiler.endStartSection("render_" + blockLayerIn);
/* 1344 */     renderBlockLayer(blockLayerIn);
/* 1345 */     this.mc.mcProfiler.endSection();
/* 1346 */     return l;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void renderBlockLayer(EnumWorldBlockLayer blockLayerIn)
/*      */   {
/* 1353 */     this.mc.entityRenderer.enableLightmap();
/*      */     
/* 1355 */     if (OpenGlHelper.useVbo())
/*      */     {
/* 1357 */       GL11.glEnableClientState(32884);
/* 1358 */       OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
/* 1359 */       GL11.glEnableClientState(32888);
/* 1360 */       OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
/* 1361 */       GL11.glEnableClientState(32888);
/* 1362 */       OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
/* 1363 */       GL11.glEnableClientState(32886);
/*      */     }
/*      */     
/* 1366 */     this.renderContainer.renderChunkLayer(blockLayerIn);
/*      */     
/* 1368 */     if (OpenGlHelper.useVbo())
/*      */     {
/* 1370 */       for (VertexFormatElement vertexformatelement : DefaultVertexFormats.BLOCK.getElements())
/*      */       {
/* 1372 */         VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
/* 1373 */         int i = vertexformatelement.getIndex();
/*      */         
/* 1375 */         switch (RenderGlobal.2.field_178037_a[vertexformatelement$enumusage.ordinal()])
/*      */         {
/*      */         case 1: 
/* 1378 */           GL11.glDisableClientState(32884);
/* 1379 */           break;
/*      */         
/*      */         case 2: 
/* 1382 */           OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + i);
/* 1383 */           GL11.glDisableClientState(32888);
/* 1384 */           OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
/* 1385 */           break;
/*      */         
/*      */         case 3: 
/* 1388 */           GL11.glDisableClientState(32886);
/* 1389 */           GlStateManager.resetColor();
/*      */         }
/*      */         
/*      */       }
/*      */     }
/* 1394 */     this.mc.entityRenderer.disableLightmap();
/*      */   }
/*      */   
/*      */   private void cleanupDamagedBlocks(Iterator iteratorIn)
/*      */   {
/* 1399 */     while (iteratorIn.hasNext())
/*      */     {
/* 1401 */       DestroyBlockProgress destroyblockprogress = (DestroyBlockProgress)iteratorIn.next();
/* 1402 */       int i = destroyblockprogress.getCreationCloudUpdateTick();
/*      */       
/* 1404 */       if (this.cloudTickCounter - i > 400)
/*      */       {
/* 1406 */         iteratorIn.remove();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void updateClouds()
/*      */   {
/* 1413 */     this.cloudTickCounter += 1;
/*      */     
/* 1415 */     if (this.cloudTickCounter % 20 == 0)
/*      */     {
/* 1417 */       cleanupDamagedBlocks(this.damagedBlocks.values().iterator());
/*      */     }
/*      */   }
/*      */   
/*      */   private void renderSkyEnd()
/*      */   {
/* 1423 */     if (Config.isSkyEnabled())
/*      */     {
/* 1425 */       GlStateManager.disableFog();
/* 1426 */       GlStateManager.disableAlpha();
/* 1427 */       GlStateManager.enableBlend();
/* 1428 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 1429 */       RenderHelper.disableStandardItemLighting();
/* 1430 */       GlStateManager.depthMask(false);
/* 1431 */       this.renderEngine.bindTexture(locationEndSkyPng);
/* 1432 */       Tessellator tessellator = Tessellator.getInstance();
/* 1433 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*      */       
/* 1435 */       for (int i = 0; i < 6; i++)
/*      */       {
/* 1437 */         GlStateManager.pushMatrix();
/*      */         
/* 1439 */         if (i == 1)
/*      */         {
/* 1441 */           GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
/*      */         }
/*      */         
/* 1444 */         if (i == 2)
/*      */         {
/* 1446 */           GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
/*      */         }
/*      */         
/* 1449 */         if (i == 3)
/*      */         {
/* 1451 */           GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
/*      */         }
/*      */         
/* 1454 */         if (i == 4)
/*      */         {
/* 1456 */           GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
/*      */         }
/*      */         
/* 1459 */         if (i == 5)
/*      */         {
/* 1461 */           GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
/*      */         }
/*      */         
/* 1464 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 1465 */         worldrenderer.pos(-100.0D, -100.0D, -100.0D).tex(0.0D, 0.0D).color(40, 40, 40, 255).endVertex();
/* 1466 */         worldrenderer.pos(-100.0D, -100.0D, 100.0D).tex(0.0D, 16.0D).color(40, 40, 40, 255).endVertex();
/* 1467 */         worldrenderer.pos(100.0D, -100.0D, 100.0D).tex(16.0D, 16.0D).color(40, 40, 40, 255).endVertex();
/* 1468 */         worldrenderer.pos(100.0D, -100.0D, -100.0D).tex(16.0D, 0.0D).color(40, 40, 40, 255).endVertex();
/* 1469 */         tessellator.draw();
/* 1470 */         GlStateManager.popMatrix();
/*      */       }
/*      */       
/* 1473 */       GlStateManager.depthMask(true);
/* 1474 */       GlStateManager.enableTexture2D();
/* 1475 */       GlStateManager.enableAlpha();
/*      */     }
/*      */   }
/*      */   
/*      */   public void renderSky(float partialTicks, int pass)
/*      */   {
/* 1481 */     if (Reflector.ForgeWorldProvider_getSkyRenderer.exists())
/*      */     {
/* 1483 */       WorldProvider worldprovider = this.mc.theWorld.provider;
/* 1484 */       Object object = Reflector.call(worldprovider, Reflector.ForgeWorldProvider_getSkyRenderer, new Object[0]);
/*      */       
/* 1486 */       if (object != null)
/*      */       {
/* 1488 */         Reflector.callVoid(object, Reflector.IRenderHandler_render, new Object[] { Float.valueOf(partialTicks), this.theWorld, this.mc });
/* 1489 */         return;
/*      */       }
/*      */     }
/*      */     
/* 1493 */     if (this.mc.theWorld.provider.getDimensionId() == 1)
/*      */     {
/* 1495 */       renderSkyEnd();
/*      */     }
/* 1497 */     else if (this.mc.theWorld.provider.isSurfaceWorld())
/*      */     {
/* 1499 */       GlStateManager.disableTexture2D();
/* 1500 */       Vec3 vec3 = this.theWorld.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
/* 1501 */       vec3 = CustomColorizer.getSkyColor(vec3, this.mc.theWorld, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0D, this.mc.getRenderViewEntity().posZ);
/* 1502 */       float f14 = (float)vec3.xCoord;
/* 1503 */       float f = (float)vec3.yCoord;
/* 1504 */       float f1 = (float)vec3.zCoord;
/*      */       
/* 1506 */       if (pass != 2)
/*      */       {
/* 1508 */         float f2 = (f14 * 30.0F + f * 59.0F + f1 * 11.0F) / 100.0F;
/* 1509 */         float f3 = (f14 * 30.0F + f * 70.0F) / 100.0F;
/* 1510 */         float f4 = (f14 * 30.0F + f1 * 70.0F) / 100.0F;
/* 1511 */         f14 = f2;
/* 1512 */         f = f3;
/* 1513 */         f1 = f4;
/*      */       }
/*      */       
/* 1516 */       GlStateManager.color(f14, f, f1);
/* 1517 */       Tessellator tessellator = Tessellator.getInstance();
/* 1518 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 1519 */       GlStateManager.depthMask(false);
/* 1520 */       GlStateManager.enableFog();
/* 1521 */       GlStateManager.color(f14, f, f1);
/*      */       
/* 1523 */       if (Config.isSkyEnabled())
/*      */       {
/* 1525 */         if (this.vboEnabled)
/*      */         {
/* 1527 */           this.skyVBO.bindBuffer();
/* 1528 */           GL11.glEnableClientState(32884);
/* 1529 */           GL11.glVertexPointer(3, 5126, 12, 0L);
/* 1530 */           this.skyVBO.drawArrays(7);
/* 1531 */           this.skyVBO.unbindBuffer();
/* 1532 */           GL11.glDisableClientState(32884);
/*      */         }
/*      */         else
/*      */         {
/* 1536 */           GlStateManager.callList(this.glSkyList);
/*      */         }
/*      */       }
/*      */       
/* 1540 */       GlStateManager.disableFog();
/* 1541 */       GlStateManager.disableAlpha();
/* 1542 */       GlStateManager.enableBlend();
/* 1543 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 1544 */       RenderHelper.disableStandardItemLighting();
/* 1545 */       float[] afloat = this.theWorld.provider.calcSunriseSunsetColors(this.theWorld.getCelestialAngle(partialTicks), partialTicks);
/*      */       
/* 1547 */       if ((afloat != null) && (Config.isSunMoonEnabled()))
/*      */       {
/* 1549 */         GlStateManager.disableTexture2D();
/* 1550 */         GlStateManager.shadeModel(7425);
/* 1551 */         GlStateManager.pushMatrix();
/* 1552 */         GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
/* 1553 */         GlStateManager.rotate(MathHelper.sin(this.theWorld.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
/* 1554 */         GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
/* 1555 */         float f5 = afloat[0];
/* 1556 */         float f6 = afloat[1];
/* 1557 */         float f7 = afloat[2];
/*      */         
/* 1559 */         if (pass != 2)
/*      */         {
/* 1561 */           float f8 = (f5 * 30.0F + f6 * 59.0F + f7 * 11.0F) / 100.0F;
/* 1562 */           float f9 = (f5 * 30.0F + f6 * 70.0F) / 100.0F;
/* 1563 */           float f10 = (f5 * 30.0F + f7 * 70.0F) / 100.0F;
/* 1564 */           f5 = f8;
/* 1565 */           f6 = f9;
/* 1566 */           f7 = f10;
/*      */         }
/*      */         
/* 1569 */         worldrenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
/* 1570 */         worldrenderer.pos(0.0D, 100.0D, 0.0D).color(f5, f6, f7, afloat[3]).endVertex();
/* 1571 */         boolean flag = true;
/*      */         
/* 1573 */         for (int i = 0; i <= 16; i++)
/*      */         {
/* 1575 */           float f20 = i * 3.1415927F * 2.0F / 16.0F;
/* 1576 */           float f11 = MathHelper.sin(f20);
/* 1577 */           float f12 = MathHelper.cos(f20);
/* 1578 */           worldrenderer.pos(f11 * 120.0F, f12 * 120.0F, -f12 * 40.0F * afloat[3]).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
/*      */         }
/*      */         
/* 1581 */         tessellator.draw();
/* 1582 */         GlStateManager.popMatrix();
/* 1583 */         GlStateManager.shadeModel(7424);
/*      */       }
/*      */       
/* 1586 */       GlStateManager.enableTexture2D();
/* 1587 */       GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
/* 1588 */       GlStateManager.pushMatrix();
/* 1589 */       float f15 = 1.0F - this.theWorld.getRainStrength(partialTicks);
/* 1590 */       GlStateManager.color(1.0F, 1.0F, 1.0F, f15);
/* 1591 */       GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
/* 1592 */       CustomSky.renderSky(this.theWorld, this.renderEngine, this.theWorld.getCelestialAngle(partialTicks), f15);
/* 1593 */       GlStateManager.rotate(this.theWorld.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
/*      */       
/* 1595 */       if (Config.isSunMoonEnabled())
/*      */       {
/* 1597 */         float f16 = 30.0F;
/* 1598 */         this.renderEngine.bindTexture(locationSunPng);
/* 1599 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 1600 */         worldrenderer.pos(-f16, 100.0D, -f16).tex(0.0D, 0.0D).endVertex();
/* 1601 */         worldrenderer.pos(f16, 100.0D, -f16).tex(1.0D, 0.0D).endVertex();
/* 1602 */         worldrenderer.pos(f16, 100.0D, f16).tex(1.0D, 1.0D).endVertex();
/* 1603 */         worldrenderer.pos(-f16, 100.0D, f16).tex(0.0D, 1.0D).endVertex();
/* 1604 */         tessellator.draw();
/* 1605 */         f16 = 20.0F;
/* 1606 */         this.renderEngine.bindTexture(locationMoonPhasesPng);
/* 1607 */         int l = this.theWorld.getMoonPhase();
/* 1608 */         int j = l % 4;
/* 1609 */         int k = l / 4 % 2;
/* 1610 */         float f21 = (j + 0) / 4.0F;
/* 1611 */         float f22 = (k + 0) / 2.0F;
/* 1612 */         float f23 = (j + 1) / 4.0F;
/* 1613 */         float f13 = (k + 1) / 2.0F;
/* 1614 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 1615 */         worldrenderer.pos(-f16, -100.0D, f16).tex(f23, f13).endVertex();
/* 1616 */         worldrenderer.pos(f16, -100.0D, f16).tex(f21, f13).endVertex();
/* 1617 */         worldrenderer.pos(f16, -100.0D, -f16).tex(f21, f22).endVertex();
/* 1618 */         worldrenderer.pos(-f16, -100.0D, -f16).tex(f23, f22).endVertex();
/* 1619 */         tessellator.draw();
/*      */       }
/*      */       
/* 1622 */       GlStateManager.disableTexture2D();
/* 1623 */       float f24 = this.theWorld.getStarBrightness(partialTicks) * f15;
/*      */       
/* 1625 */       if ((f24 > 0.0F) && (Config.isStarsEnabled()) && (!CustomSky.hasSkyLayers(this.theWorld)))
/*      */       {
/* 1627 */         GlStateManager.color(f24, f24, f24, f24);
/*      */         
/* 1629 */         if (this.vboEnabled)
/*      */         {
/* 1631 */           this.starVBO.bindBuffer();
/* 1632 */           GL11.glEnableClientState(32884);
/* 1633 */           GL11.glVertexPointer(3, 5126, 12, 0L);
/* 1634 */           this.starVBO.drawArrays(7);
/* 1635 */           this.starVBO.unbindBuffer();
/* 1636 */           GL11.glDisableClientState(32884);
/*      */         }
/*      */         else
/*      */         {
/* 1640 */           GlStateManager.callList(this.starGLCallList);
/*      */         }
/*      */       }
/*      */       
/* 1644 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 1645 */       GlStateManager.disableBlend();
/* 1646 */       GlStateManager.enableAlpha();
/* 1647 */       GlStateManager.enableFog();
/* 1648 */       GlStateManager.popMatrix();
/* 1649 */       GlStateManager.disableTexture2D();
/* 1650 */       GlStateManager.color(0.0F, 0.0F, 0.0F);
/* 1651 */       double d0 = this.mc.thePlayer.getPositionEyes(partialTicks).yCoord - this.theWorld.getHorizon();
/*      */       
/* 1653 */       if (d0 < 0.0D)
/*      */       {
/* 1655 */         GlStateManager.pushMatrix();
/* 1656 */         GlStateManager.translate(0.0F, 12.0F, 0.0F);
/*      */         
/* 1658 */         if (this.vboEnabled)
/*      */         {
/* 1660 */           this.sky2VBO.bindBuffer();
/* 1661 */           GL11.glEnableClientState(32884);
/* 1662 */           GL11.glVertexPointer(3, 5126, 12, 0L);
/* 1663 */           this.sky2VBO.drawArrays(7);
/* 1664 */           this.sky2VBO.unbindBuffer();
/* 1665 */           GL11.glDisableClientState(32884);
/*      */         }
/*      */         else
/*      */         {
/* 1669 */           GlStateManager.callList(this.glSkyList2);
/*      */         }
/*      */         
/* 1672 */         GlStateManager.popMatrix();
/* 1673 */         float f17 = 1.0F;
/* 1674 */         float f18 = -(float)(d0 + 65.0D);
/* 1675 */         float f19 = -1.0F;
/* 1676 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 1677 */         worldrenderer.pos(-1.0D, f18, 1.0D).color(0, 0, 0, 255).endVertex();
/* 1678 */         worldrenderer.pos(1.0D, f18, 1.0D).color(0, 0, 0, 255).endVertex();
/* 1679 */         worldrenderer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 1680 */         worldrenderer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 1681 */         worldrenderer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
/* 1682 */         worldrenderer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
/* 1683 */         worldrenderer.pos(1.0D, f18, -1.0D).color(0, 0, 0, 255).endVertex();
/* 1684 */         worldrenderer.pos(-1.0D, f18, -1.0D).color(0, 0, 0, 255).endVertex();
/* 1685 */         worldrenderer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
/* 1686 */         worldrenderer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 1687 */         worldrenderer.pos(1.0D, f18, 1.0D).color(0, 0, 0, 255).endVertex();
/* 1688 */         worldrenderer.pos(1.0D, f18, -1.0D).color(0, 0, 0, 255).endVertex();
/* 1689 */         worldrenderer.pos(-1.0D, f18, -1.0D).color(0, 0, 0, 255).endVertex();
/* 1690 */         worldrenderer.pos(-1.0D, f18, 1.0D).color(0, 0, 0, 255).endVertex();
/* 1691 */         worldrenderer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 1692 */         worldrenderer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
/* 1693 */         worldrenderer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
/* 1694 */         worldrenderer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 1695 */         worldrenderer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 1696 */         worldrenderer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
/* 1697 */         tessellator.draw();
/*      */       }
/*      */       
/* 1700 */       if (this.theWorld.provider.isSkyColored())
/*      */       {
/* 1702 */         GlStateManager.color(f14 * 0.2F + 0.04F, f * 0.2F + 0.04F, f1 * 0.6F + 0.1F);
/*      */       }
/*      */       else
/*      */       {
/* 1706 */         GlStateManager.color(f14, f, f1);
/*      */       }
/*      */       
/* 1709 */       if (this.mc.gameSettings.renderDistanceChunks <= 4)
/*      */       {
/* 1711 */         GlStateManager.color(this.mc.entityRenderer.fogColorRed, this.mc.entityRenderer.fogColorGreen, this.mc.entityRenderer.fogColorBlue);
/*      */       }
/*      */       
/* 1714 */       GlStateManager.pushMatrix();
/* 1715 */       GlStateManager.translate(0.0F, -(float)(d0 - 16.0D), 0.0F);
/*      */       
/* 1717 */       if (Config.isSkyEnabled())
/*      */       {
/* 1719 */         GlStateManager.callList(this.glSkyList2);
/*      */       }
/*      */       
/* 1722 */       GlStateManager.popMatrix();
/* 1723 */       GlStateManager.enableTexture2D();
/* 1724 */       GlStateManager.depthMask(true);
/*      */     }
/*      */   }
/*      */   
/*      */   public void renderClouds(float partialTicks, int pass)
/*      */   {
/* 1730 */     if (!Config.isCloudsOff())
/*      */     {
/* 1732 */       if (Reflector.ForgeWorldProvider_getCloudRenderer.exists())
/*      */       {
/* 1734 */         WorldProvider worldprovider = this.mc.theWorld.provider;
/* 1735 */         Object object = Reflector.call(worldprovider, Reflector.ForgeWorldProvider_getCloudRenderer, new Object[0]);
/*      */         
/* 1737 */         if (object != null)
/*      */         {
/* 1739 */           Reflector.callVoid(object, Reflector.IRenderHandler_render, new Object[] { Float.valueOf(partialTicks), this.theWorld, this.mc });
/* 1740 */           return;
/*      */         }
/*      */       }
/*      */       
/* 1744 */       if (this.mc.theWorld.provider.isSurfaceWorld())
/*      */       {
/* 1746 */         if (Config.isCloudsFancy())
/*      */         {
/* 1748 */           renderCloudsFancy(partialTicks, pass);
/*      */         }
/*      */         else
/*      */         {
/* 1752 */           this.cloudRenderer.prepareToRender(false, this.cloudTickCounter, partialTicks);
/* 1753 */           partialTicks = 0.0F;
/* 1754 */           GlStateManager.disableCull();
/* 1755 */           float f9 = (float)(this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().posY - this.mc.getRenderViewEntity().lastTickPosY) * partialTicks);
/* 1756 */           boolean flag = true;
/* 1757 */           boolean flag1 = true;
/* 1758 */           Tessellator tessellator = Tessellator.getInstance();
/* 1759 */           WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 1760 */           this.renderEngine.bindTexture(locationCloudsPng);
/* 1761 */           GlStateManager.enableBlend();
/* 1762 */           GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*      */           
/* 1764 */           if (this.cloudRenderer.shouldUpdateGlList())
/*      */           {
/* 1766 */             this.cloudRenderer.startUpdateGlList();
/* 1767 */             Vec3 vec3 = this.theWorld.getCloudColour(partialTicks);
/* 1768 */             float f = (float)vec3.xCoord;
/* 1769 */             float f1 = (float)vec3.yCoord;
/* 1770 */             float f2 = (float)vec3.zCoord;
/*      */             
/* 1772 */             if (pass != 2)
/*      */             {
/* 1774 */               float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
/* 1775 */               float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
/* 1776 */               float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
/* 1777 */               f = f3;
/* 1778 */               f1 = f4;
/* 1779 */               f2 = f5;
/*      */             }
/*      */             
/* 1782 */             float f10 = 4.8828125E-4F;
/* 1783 */             double d2 = this.cloudTickCounter + partialTicks;
/* 1784 */             double d0 = this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().posX - this.mc.getRenderViewEntity().prevPosX) * partialTicks + d2 * 0.029999999329447746D;
/* 1785 */             double d1 = this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().posZ - this.mc.getRenderViewEntity().prevPosZ) * partialTicks;
/* 1786 */             int i = MathHelper.floor_double(d0 / 2048.0D);
/* 1787 */             int j = MathHelper.floor_double(d1 / 2048.0D);
/* 1788 */             d0 -= i * 2048;
/* 1789 */             d1 -= j * 2048;
/* 1790 */             float f6 = this.theWorld.provider.getCloudHeight() - f9 + 0.33F;
/* 1791 */             f6 += this.mc.gameSettings.ofCloudsHeight * 128.0F;
/* 1792 */             float f7 = (float)(d0 * 4.8828125E-4D);
/* 1793 */             float f8 = (float)(d1 * 4.8828125E-4D);
/* 1794 */             worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/*      */             
/* 1796 */             for (int k = 65280; k < 256; k += 32)
/*      */             {
/* 1798 */               for (int l = 65280; l < 256; l += 32)
/*      */               {
/* 1800 */                 worldrenderer.pos(k + 0, f6, l + 32).tex((k + 0) * 4.8828125E-4F + f7, (l + 32) * 4.8828125E-4F + f8).color(f, f1, f2, 0.8F).endVertex();
/* 1801 */                 worldrenderer.pos(k + 32, f6, l + 32).tex((k + 32) * 4.8828125E-4F + f7, (l + 32) * 4.8828125E-4F + f8).color(f, f1, f2, 0.8F).endVertex();
/* 1802 */                 worldrenderer.pos(k + 32, f6, l + 0).tex((k + 32) * 4.8828125E-4F + f7, (l + 0) * 4.8828125E-4F + f8).color(f, f1, f2, 0.8F).endVertex();
/* 1803 */                 worldrenderer.pos(k + 0, f6, l + 0).tex((k + 0) * 4.8828125E-4F + f7, (l + 0) * 4.8828125E-4F + f8).color(f, f1, f2, 0.8F).endVertex();
/*      */               }
/*      */             }
/*      */             
/* 1807 */             tessellator.draw();
/* 1808 */             this.cloudRenderer.endUpdateGlList();
/*      */           }
/*      */           
/* 1811 */           this.cloudRenderer.renderGlList();
/* 1812 */           GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 1813 */           GlStateManager.disableBlend();
/* 1814 */           GlStateManager.enableCull();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasCloudFog(double x, double y, double z, float partialTicks)
/*      */   {
/* 1825 */     return false;
/*      */   }
/*      */   
/*      */   private void renderCloudsFancy(float partialTicks, int pass)
/*      */   {
/* 1830 */     this.cloudRenderer.prepareToRender(true, this.cloudTickCounter, partialTicks);
/* 1831 */     partialTicks = 0.0F;
/* 1832 */     GlStateManager.disableCull();
/* 1833 */     float f = (float)(this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().posY - this.mc.getRenderViewEntity().lastTickPosY) * partialTicks);
/* 1834 */     Tessellator tessellator = Tessellator.getInstance();
/* 1835 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 1836 */     float f1 = 12.0F;
/* 1837 */     float f2 = 4.0F;
/* 1838 */     double d0 = this.cloudTickCounter + partialTicks;
/* 1839 */     double d1 = (this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().posX - this.mc.getRenderViewEntity().prevPosX) * partialTicks + d0 * 0.029999999329447746D) / 12.0D;
/* 1840 */     double d2 = (this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().posZ - this.mc.getRenderViewEntity().prevPosZ) * partialTicks) / 12.0D + 0.33000001311302185D;
/* 1841 */     float f3 = this.theWorld.provider.getCloudHeight() - f + 0.33F;
/* 1842 */     f3 += this.mc.gameSettings.ofCloudsHeight * 128.0F;
/* 1843 */     int i = MathHelper.floor_double(d1 / 2048.0D);
/* 1844 */     int j = MathHelper.floor_double(d2 / 2048.0D);
/* 1845 */     d1 -= i * 2048;
/* 1846 */     d2 -= j * 2048;
/* 1847 */     this.renderEngine.bindTexture(locationCloudsPng);
/* 1848 */     GlStateManager.enableBlend();
/* 1849 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 1850 */     Vec3 vec3 = this.theWorld.getCloudColour(partialTicks);
/* 1851 */     float f4 = (float)vec3.xCoord;
/* 1852 */     float f5 = (float)vec3.yCoord;
/* 1853 */     float f6 = (float)vec3.zCoord;
/*      */     
/* 1855 */     if (pass != 2)
/*      */     {
/* 1857 */       float f7 = (f4 * 30.0F + f5 * 59.0F + f6 * 11.0F) / 100.0F;
/* 1858 */       float f8 = (f4 * 30.0F + f5 * 70.0F) / 100.0F;
/* 1859 */       float f9 = (f4 * 30.0F + f6 * 70.0F) / 100.0F;
/* 1860 */       f4 = f7;
/* 1861 */       f5 = f8;
/* 1862 */       f6 = f9;
/*      */     }
/*      */     
/* 1865 */     float f26 = f4 * 0.9F;
/* 1866 */     float f27 = f5 * 0.9F;
/* 1867 */     float f28 = f6 * 0.9F;
/* 1868 */     float f10 = f4 * 0.7F;
/* 1869 */     float f11 = f5 * 0.7F;
/* 1870 */     float f12 = f6 * 0.7F;
/* 1871 */     float f13 = f4 * 0.8F;
/* 1872 */     float f14 = f5 * 0.8F;
/* 1873 */     float f15 = f6 * 0.8F;
/* 1874 */     float f16 = 0.00390625F;
/* 1875 */     float f17 = MathHelper.floor_double(d1) * 0.00390625F;
/* 1876 */     float f18 = MathHelper.floor_double(d2) * 0.00390625F;
/* 1877 */     float f19 = (float)(d1 - MathHelper.floor_double(d1));
/* 1878 */     float f20 = (float)(d2 - MathHelper.floor_double(d2));
/* 1879 */     boolean flag = true;
/* 1880 */     boolean flag1 = true;
/* 1881 */     float f21 = 9.765625E-4F;
/* 1882 */     GlStateManager.scale(12.0F, 1.0F, 12.0F);
/*      */     
/* 1884 */     for (int k = 0; k < 2; k++)
/*      */     {
/* 1886 */       if (k == 0)
/*      */       {
/* 1888 */         GlStateManager.colorMask(false, false, false, false);
/*      */       }
/*      */       else
/*      */       {
/* 1892 */         switch (pass)
/*      */         {
/*      */         case 0: 
/* 1895 */           GlStateManager.colorMask(false, true, true, true);
/* 1896 */           break;
/*      */         
/*      */         case 1: 
/* 1899 */           GlStateManager.colorMask(true, false, false, true);
/* 1900 */           break;
/*      */         
/*      */         case 2: 
/* 1903 */           GlStateManager.colorMask(true, true, true, true);
/*      */         }
/*      */         
/*      */       }
/* 1907 */       this.cloudRenderer.renderGlList();
/*      */     }
/*      */     
/* 1910 */     if (this.cloudRenderer.shouldUpdateGlList())
/*      */     {
/* 1912 */       this.cloudRenderer.startUpdateGlList();
/*      */       
/* 1914 */       for (int j1 = -3; j1 <= 4; j1++)
/*      */       {
/* 1916 */         for (int l = -3; l <= 4; l++)
/*      */         {
/* 1918 */           worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
/* 1919 */           float f22 = j1 * 8;
/* 1920 */           float f23 = l * 8;
/* 1921 */           float f24 = f22 - f19;
/* 1922 */           float f25 = f23 - f20;
/*      */           
/* 1924 */           if (f3 > -5.0F)
/*      */           {
/* 1926 */             worldrenderer.pos(f24 + 0.0F, f3 + 0.0F, f25 + 8.0F).tex((f22 + 0.0F) * 0.00390625F + f17, (f23 + 8.0F) * 0.00390625F + f18).color(f10, f11, f12, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
/* 1927 */             worldrenderer.pos(f24 + 8.0F, f3 + 0.0F, f25 + 8.0F).tex((f22 + 8.0F) * 0.00390625F + f17, (f23 + 8.0F) * 0.00390625F + f18).color(f10, f11, f12, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
/* 1928 */             worldrenderer.pos(f24 + 8.0F, f3 + 0.0F, f25 + 0.0F).tex((f22 + 8.0F) * 0.00390625F + f17, (f23 + 0.0F) * 0.00390625F + f18).color(f10, f11, f12, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
/* 1929 */             worldrenderer.pos(f24 + 0.0F, f3 + 0.0F, f25 + 0.0F).tex((f22 + 0.0F) * 0.00390625F + f17, (f23 + 0.0F) * 0.00390625F + f18).color(f10, f11, f12, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
/*      */           }
/*      */           
/* 1932 */           if (f3 <= 5.0F)
/*      */           {
/* 1934 */             worldrenderer.pos(f24 + 0.0F, f3 + 4.0F - 9.765625E-4F, f25 + 8.0F).tex((f22 + 0.0F) * 0.00390625F + f17, (f23 + 8.0F) * 0.00390625F + f18).color(f4, f5, f6, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 1935 */             worldrenderer.pos(f24 + 8.0F, f3 + 4.0F - 9.765625E-4F, f25 + 8.0F).tex((f22 + 8.0F) * 0.00390625F + f17, (f23 + 8.0F) * 0.00390625F + f18).color(f4, f5, f6, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 1936 */             worldrenderer.pos(f24 + 8.0F, f3 + 4.0F - 9.765625E-4F, f25 + 0.0F).tex((f22 + 8.0F) * 0.00390625F + f17, (f23 + 0.0F) * 0.00390625F + f18).color(f4, f5, f6, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 1937 */             worldrenderer.pos(f24 + 0.0F, f3 + 4.0F - 9.765625E-4F, f25 + 0.0F).tex((f22 + 0.0F) * 0.00390625F + f17, (f23 + 0.0F) * 0.00390625F + f18).color(f4, f5, f6, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
/*      */           }
/*      */           
/* 1940 */           if (j1 > -1)
/*      */           {
/* 1942 */             for (int i1 = 0; i1 < 8; i1++)
/*      */             {
/* 1944 */               worldrenderer.pos(f24 + i1 + 0.0F, f3 + 0.0F, f25 + 8.0F).tex((f22 + i1 + 0.5F) * 0.00390625F + f17, (f23 + 8.0F) * 0.00390625F + f18).color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
/* 1945 */               worldrenderer.pos(f24 + i1 + 0.0F, f3 + 4.0F, f25 + 8.0F).tex((f22 + i1 + 0.5F) * 0.00390625F + f17, (f23 + 8.0F) * 0.00390625F + f18).color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
/* 1946 */               worldrenderer.pos(f24 + i1 + 0.0F, f3 + 4.0F, f25 + 0.0F).tex((f22 + i1 + 0.5F) * 0.00390625F + f17, (f23 + 0.0F) * 0.00390625F + f18).color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
/* 1947 */               worldrenderer.pos(f24 + i1 + 0.0F, f3 + 0.0F, f25 + 0.0F).tex((f22 + i1 + 0.5F) * 0.00390625F + f17, (f23 + 0.0F) * 0.00390625F + f18).color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
/*      */             }
/*      */           }
/*      */           
/* 1951 */           if (j1 <= 1)
/*      */           {
/* 1953 */             for (int k1 = 0; k1 < 8; k1++)
/*      */             {
/* 1955 */               worldrenderer.pos(f24 + k1 + 1.0F - 9.765625E-4F, f3 + 0.0F, f25 + 8.0F).tex((f22 + k1 + 0.5F) * 0.00390625F + f17, (f23 + 8.0F) * 0.00390625F + f18).color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
/* 1956 */               worldrenderer.pos(f24 + k1 + 1.0F - 9.765625E-4F, f3 + 4.0F, f25 + 8.0F).tex((f22 + k1 + 0.5F) * 0.00390625F + f17, (f23 + 8.0F) * 0.00390625F + f18).color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
/* 1957 */               worldrenderer.pos(f24 + k1 + 1.0F - 9.765625E-4F, f3 + 4.0F, f25 + 0.0F).tex((f22 + k1 + 0.5F) * 0.00390625F + f17, (f23 + 0.0F) * 0.00390625F + f18).color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
/* 1958 */               worldrenderer.pos(f24 + k1 + 1.0F - 9.765625E-4F, f3 + 0.0F, f25 + 0.0F).tex((f22 + k1 + 0.5F) * 0.00390625F + f17, (f23 + 0.0F) * 0.00390625F + f18).color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
/*      */             }
/*      */           }
/*      */           
/* 1962 */           if (l > -1)
/*      */           {
/* 1964 */             for (int l1 = 0; l1 < 8; l1++)
/*      */             {
/* 1966 */               worldrenderer.pos(f24 + 0.0F, f3 + 4.0F, f25 + l1 + 0.0F).tex((f22 + 0.0F) * 0.00390625F + f17, (f23 + l1 + 0.5F) * 0.00390625F + f18).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
/* 1967 */               worldrenderer.pos(f24 + 8.0F, f3 + 4.0F, f25 + l1 + 0.0F).tex((f22 + 8.0F) * 0.00390625F + f17, (f23 + l1 + 0.5F) * 0.00390625F + f18).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
/* 1968 */               worldrenderer.pos(f24 + 8.0F, f3 + 0.0F, f25 + l1 + 0.0F).tex((f22 + 8.0F) * 0.00390625F + f17, (f23 + l1 + 0.5F) * 0.00390625F + f18).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
/* 1969 */               worldrenderer.pos(f24 + 0.0F, f3 + 0.0F, f25 + l1 + 0.0F).tex((f22 + 0.0F) * 0.00390625F + f17, (f23 + l1 + 0.5F) * 0.00390625F + f18).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
/*      */             }
/*      */           }
/*      */           
/* 1973 */           if (l <= 1)
/*      */           {
/* 1975 */             for (int i2 = 0; i2 < 8; i2++)
/*      */             {
/* 1977 */               worldrenderer.pos(f24 + 0.0F, f3 + 4.0F, f25 + i2 + 1.0F - 9.765625E-4F).tex((f22 + 0.0F) * 0.00390625F + f17, (f23 + i2 + 0.5F) * 0.00390625F + f18).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
/* 1978 */               worldrenderer.pos(f24 + 8.0F, f3 + 4.0F, f25 + i2 + 1.0F - 9.765625E-4F).tex((f22 + 8.0F) * 0.00390625F + f17, (f23 + i2 + 0.5F) * 0.00390625F + f18).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
/* 1979 */               worldrenderer.pos(f24 + 8.0F, f3 + 0.0F, f25 + i2 + 1.0F - 9.765625E-4F).tex((f22 + 8.0F) * 0.00390625F + f17, (f23 + i2 + 0.5F) * 0.00390625F + f18).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
/* 1980 */               worldrenderer.pos(f24 + 0.0F, f3 + 0.0F, f25 + i2 + 1.0F - 9.765625E-4F).tex((f22 + 0.0F) * 0.00390625F + f17, (f23 + i2 + 0.5F) * 0.00390625F + f18).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
/*      */             }
/*      */           }
/*      */           
/* 1984 */           tessellator.draw();
/*      */         }
/*      */       }
/*      */       
/* 1988 */       this.cloudRenderer.endUpdateGlList();
/*      */     }
/*      */     
/* 1991 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 1992 */     GlStateManager.disableBlend();
/* 1993 */     GlStateManager.enableCull();
/*      */   }
/*      */   
/*      */   public void updateChunks(long finishTimeNano)
/*      */   {
/* 1998 */     this.displayListEntitiesDirty |= this.renderDispatcher.runChunkUploads(finishTimeNano);
/*      */     
/* 2000 */     if (this.chunksToUpdateForced.size() > 0)
/*      */     {
/* 2002 */       Iterator iterator = this.chunksToUpdateForced.iterator();
/*      */       
/* 2004 */       while (iterator.hasNext())
/*      */       {
/* 2006 */         RenderChunk renderchunk = (RenderChunk)iterator.next();
/*      */         
/* 2008 */         if (!this.renderDispatcher.updateChunkLater(renderchunk)) {
/*      */           break;
/*      */         }
/*      */         
/*      */ 
/* 2013 */         renderchunk.setNeedsUpdate(false);
/* 2014 */         iterator.remove();
/* 2015 */         this.chunksToUpdate.remove(renderchunk);
/* 2016 */         this.chunksToResortTransparency.remove(renderchunk);
/*      */       }
/*      */     }
/*      */     
/* 2020 */     if (this.chunksToResortTransparency.size() > 0)
/*      */     {
/* 2022 */       Iterator iterator2 = this.chunksToResortTransparency.iterator();
/*      */       
/* 2024 */       if (iterator2.hasNext())
/*      */       {
/* 2026 */         RenderChunk renderchunk2 = (RenderChunk)iterator2.next();
/*      */         
/* 2028 */         if (this.renderDispatcher.updateTransparencyLater(renderchunk2))
/*      */         {
/* 2030 */           iterator2.remove();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2035 */     int j = 0;
/* 2036 */     int k = Config.getUpdatesPerFrame();
/* 2037 */     int i = k * 2;
/* 2038 */     Iterator iterator1 = this.chunksToUpdate.iterator();
/*      */     
/* 2040 */     while (iterator1.hasNext())
/*      */     {
/* 2042 */       RenderChunk renderchunk1 = (RenderChunk)iterator1.next();
/*      */       
/* 2044 */       if (!this.renderDispatcher.updateChunkLater(renderchunk1)) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/* 2049 */       renderchunk1.setNeedsUpdate(false);
/* 2050 */       iterator1.remove();
/*      */       
/* 2052 */       if ((renderchunk1.getCompiledChunk().isEmpty()) && (k < i))
/*      */       {
/* 2054 */         k++;
/*      */       }
/*      */       
/* 2057 */       j++;
/*      */       
/* 2059 */       if (j >= k) {
/*      */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void renderWorldBorder(Entity p_180449_1_, float partialTicks)
/*      */   {
/* 2068 */     Tessellator tessellator = Tessellator.getInstance();
/* 2069 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 2070 */     WorldBorder worldborder = this.theWorld.getWorldBorder();
/* 2071 */     double d0 = this.mc.gameSettings.renderDistanceChunks * 16;
/*      */     
/* 2073 */     if ((p_180449_1_.posX >= worldborder.maxX() - d0) || (p_180449_1_.posX <= worldborder.minX() + d0) || (p_180449_1_.posZ >= worldborder.maxZ() - d0) || (p_180449_1_.posZ <= worldborder.minZ() + d0))
/*      */     {
/* 2075 */       double d1 = 1.0D - worldborder.getClosestDistance(p_180449_1_) / d0;
/* 2076 */       d1 = Math.pow(d1, 4.0D);
/* 2077 */       double d2 = p_180449_1_.lastTickPosX + (p_180449_1_.posX - p_180449_1_.lastTickPosX) * partialTicks;
/* 2078 */       double d3 = p_180449_1_.lastTickPosY + (p_180449_1_.posY - p_180449_1_.lastTickPosY) * partialTicks;
/* 2079 */       double d4 = p_180449_1_.lastTickPosZ + (p_180449_1_.posZ - p_180449_1_.lastTickPosZ) * partialTicks;
/* 2080 */       GlStateManager.enableBlend();
/* 2081 */       GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
/* 2082 */       this.renderEngine.bindTexture(locationForcefieldPng);
/* 2083 */       GlStateManager.depthMask(false);
/* 2084 */       GlStateManager.pushMatrix();
/* 2085 */       int i = worldborder.getStatus().getID();
/* 2086 */       float f = (i >> 16 & 0xFF) / 255.0F;
/* 2087 */       float f1 = (i >> 8 & 0xFF) / 255.0F;
/* 2088 */       float f2 = (i & 0xFF) / 255.0F;
/* 2089 */       GlStateManager.color(f, f1, f2, (float)d1);
/* 2090 */       GlStateManager.doPolygonOffset(-3.0F, -3.0F);
/* 2091 */       GlStateManager.enablePolygonOffset();
/* 2092 */       GlStateManager.alphaFunc(516, 0.1F);
/* 2093 */       GlStateManager.enableAlpha();
/* 2094 */       GlStateManager.disableCull();
/* 2095 */       float f3 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F;
/* 2096 */       float f4 = 0.0F;
/* 2097 */       float f5 = 0.0F;
/* 2098 */       float f6 = 128.0F;
/* 2099 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 2100 */       worldrenderer.setTranslation(-d2, -d3, -d4);
/* 2101 */       double d5 = Math.max(MathHelper.floor_double(d4 - d0), worldborder.minZ());
/* 2102 */       double d6 = Math.min(MathHelper.ceiling_double_int(d4 + d0), worldborder.maxZ());
/*      */       
/* 2104 */       if (d2 > worldborder.maxX() - d0)
/*      */       {
/* 2106 */         float f7 = 0.0F;
/*      */         
/* 2108 */         for (double d7 = d5; d7 < d6; f7 += 0.5F)
/*      */         {
/* 2110 */           double d8 = Math.min(1.0D, d6 - d7);
/* 2111 */           float f8 = (float)d8 * 0.5F;
/* 2112 */           worldrenderer.pos(worldborder.maxX(), 256.0D, d7).tex(f3 + f7, f3 + 0.0F).endVertex();
/* 2113 */           worldrenderer.pos(worldborder.maxX(), 256.0D, d7 + d8).tex(f3 + f8 + f7, f3 + 0.0F).endVertex();
/* 2114 */           worldrenderer.pos(worldborder.maxX(), 0.0D, d7 + d8).tex(f3 + f8 + f7, f3 + 128.0F).endVertex();
/* 2115 */           worldrenderer.pos(worldborder.maxX(), 0.0D, d7).tex(f3 + f7, f3 + 128.0F).endVertex();
/* 2116 */           d7 += 1.0D;
/*      */         }
/*      */       }
/*      */       
/* 2120 */       if (d2 < worldborder.minX() + d0)
/*      */       {
/* 2122 */         float f9 = 0.0F;
/*      */         
/* 2124 */         for (double d9 = d5; d9 < d6; f9 += 0.5F)
/*      */         {
/* 2126 */           double d12 = Math.min(1.0D, d6 - d9);
/* 2127 */           float f12 = (float)d12 * 0.5F;
/* 2128 */           worldrenderer.pos(worldborder.minX(), 256.0D, d9).tex(f3 + f9, f3 + 0.0F).endVertex();
/* 2129 */           worldrenderer.pos(worldborder.minX(), 256.0D, d9 + d12).tex(f3 + f12 + f9, f3 + 0.0F).endVertex();
/* 2130 */           worldrenderer.pos(worldborder.minX(), 0.0D, d9 + d12).tex(f3 + f12 + f9, f3 + 128.0F).endVertex();
/* 2131 */           worldrenderer.pos(worldborder.minX(), 0.0D, d9).tex(f3 + f9, f3 + 128.0F).endVertex();
/* 2132 */           d9 += 1.0D;
/*      */         }
/*      */       }
/*      */       
/* 2136 */       d5 = Math.max(MathHelper.floor_double(d2 - d0), worldborder.minX());
/* 2137 */       d6 = Math.min(MathHelper.ceiling_double_int(d2 + d0), worldborder.maxX());
/*      */       
/* 2139 */       if (d4 > worldborder.maxZ() - d0)
/*      */       {
/* 2141 */         float f10 = 0.0F;
/*      */         
/* 2143 */         for (double d10 = d5; d10 < d6; f10 += 0.5F)
/*      */         {
/* 2145 */           double d13 = Math.min(1.0D, d6 - d10);
/* 2146 */           float f13 = (float)d13 * 0.5F;
/* 2147 */           worldrenderer.pos(d10, 256.0D, worldborder.maxZ()).tex(f3 + f10, f3 + 0.0F).endVertex();
/* 2148 */           worldrenderer.pos(d10 + d13, 256.0D, worldborder.maxZ()).tex(f3 + f13 + f10, f3 + 0.0F).endVertex();
/* 2149 */           worldrenderer.pos(d10 + d13, 0.0D, worldborder.maxZ()).tex(f3 + f13 + f10, f3 + 128.0F).endVertex();
/* 2150 */           worldrenderer.pos(d10, 0.0D, worldborder.maxZ()).tex(f3 + f10, f3 + 128.0F).endVertex();
/* 2151 */           d10 += 1.0D;
/*      */         }
/*      */       }
/*      */       
/* 2155 */       if (d4 < worldborder.minZ() + d0)
/*      */       {
/* 2157 */         float f11 = 0.0F;
/*      */         
/* 2159 */         for (double d11 = d5; d11 < d6; f11 += 0.5F)
/*      */         {
/* 2161 */           double d14 = Math.min(1.0D, d6 - d11);
/* 2162 */           float f14 = (float)d14 * 0.5F;
/* 2163 */           worldrenderer.pos(d11, 256.0D, worldborder.minZ()).tex(f3 + f11, f3 + 0.0F).endVertex();
/* 2164 */           worldrenderer.pos(d11 + d14, 256.0D, worldborder.minZ()).tex(f3 + f14 + f11, f3 + 0.0F).endVertex();
/* 2165 */           worldrenderer.pos(d11 + d14, 0.0D, worldborder.minZ()).tex(f3 + f14 + f11, f3 + 128.0F).endVertex();
/* 2166 */           worldrenderer.pos(d11, 0.0D, worldborder.minZ()).tex(f3 + f11, f3 + 128.0F).endVertex();
/* 2167 */           d11 += 1.0D;
/*      */         }
/*      */       }
/*      */       
/* 2171 */       tessellator.draw();
/* 2172 */       worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
/* 2173 */       GlStateManager.enableCull();
/* 2174 */       GlStateManager.disableAlpha();
/* 2175 */       GlStateManager.doPolygonOffset(0.0F, 0.0F);
/* 2176 */       GlStateManager.disablePolygonOffset();
/* 2177 */       GlStateManager.enableAlpha();
/* 2178 */       GlStateManager.disableBlend();
/* 2179 */       GlStateManager.popMatrix();
/* 2180 */       GlStateManager.depthMask(true);
/*      */     }
/*      */   }
/*      */   
/*      */   private void preRenderDamagedBlocks()
/*      */   {
/* 2186 */     GlStateManager.tryBlendFuncSeparate(774, 768, 1, 0);
/* 2187 */     GlStateManager.enableBlend();
/* 2188 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
/* 2189 */     GlStateManager.doPolygonOffset(-3.0F, -3.0F);
/* 2190 */     GlStateManager.enablePolygonOffset();
/* 2191 */     GlStateManager.alphaFunc(516, 0.1F);
/* 2192 */     GlStateManager.enableAlpha();
/* 2193 */     GlStateManager.pushMatrix();
/*      */   }
/*      */   
/*      */   private void postRenderDamagedBlocks()
/*      */   {
/* 2198 */     GlStateManager.disableAlpha();
/* 2199 */     GlStateManager.doPolygonOffset(0.0F, 0.0F);
/* 2200 */     GlStateManager.disablePolygonOffset();
/* 2201 */     GlStateManager.enableAlpha();
/* 2202 */     GlStateManager.depthMask(true);
/* 2203 */     GlStateManager.popMatrix();
/*      */   }
/*      */   
/*      */   public void drawBlockDamageTexture(Tessellator tessellatorIn, WorldRenderer worldRendererIn, Entity entityIn, float partialTicks)
/*      */   {
/* 2208 */     double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
/* 2209 */     double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
/* 2210 */     double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
/*      */     
/* 2212 */     if (!this.damagedBlocks.isEmpty())
/*      */     {
/* 2214 */       this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
/* 2215 */       preRenderDamagedBlocks();
/* 2216 */       worldRendererIn.begin(7, DefaultVertexFormats.BLOCK);
/* 2217 */       worldRendererIn.setTranslation(-d0, -d1, -d2);
/* 2218 */       worldRendererIn.markDirty();
/* 2219 */       Iterator iterator = this.damagedBlocks.values().iterator();
/*      */       
/* 2221 */       while (iterator.hasNext())
/*      */       {
/* 2223 */         DestroyBlockProgress destroyblockprogress = (DestroyBlockProgress)iterator.next();
/* 2224 */         BlockPos blockpos = destroyblockprogress.getPosition();
/* 2225 */         double d3 = blockpos.getX() - d0;
/* 2226 */         double d4 = blockpos.getY() - d1;
/* 2227 */         double d5 = blockpos.getZ() - d2;
/* 2228 */         Block block = this.theWorld.getBlockState(blockpos).getBlock();
/*      */         boolean flag;
/*      */         boolean flag;
/* 2231 */         if (Reflector.ForgeTileEntity_canRenderBreaking.exists())
/*      */         {
/* 2233 */           boolean flag1 = ((block instanceof BlockChest)) || ((block instanceof BlockEnderChest)) || ((block instanceof BlockSign)) || ((block instanceof BlockSkull));
/*      */           
/* 2235 */           if (!flag1)
/*      */           {
/* 2237 */             TileEntity tileentity = this.theWorld.getTileEntity(blockpos);
/*      */             
/* 2239 */             if (tileentity != null)
/*      */             {
/* 2241 */               flag1 = Reflector.callBoolean(tileentity, Reflector.ForgeTileEntity_canRenderBreaking, new Object[0]);
/*      */             }
/*      */           }
/*      */           
/* 2245 */           flag = !flag1;
/*      */         }
/*      */         else
/*      */         {
/* 2249 */           flag = (!(block instanceof BlockChest)) && (!(block instanceof BlockEnderChest)) && (!(block instanceof BlockSign)) && (!(block instanceof BlockSkull));
/*      */         }
/*      */         
/* 2252 */         if (flag)
/*      */         {
/* 2254 */           if (d3 * d3 + d4 * d4 + d5 * d5 > 1024.0D)
/*      */           {
/* 2256 */             iterator.remove();
/*      */           }
/*      */           else
/*      */           {
/* 2260 */             IBlockState iblockstate = this.theWorld.getBlockState(blockpos);
/*      */             
/* 2262 */             if (iblockstate.getBlock().getMaterial() != Material.air)
/*      */             {
/* 2264 */               int i = destroyblockprogress.getPartialBlockDamage();
/* 2265 */               TextureAtlasSprite textureatlassprite = this.destroyBlockIcons[i];
/* 2266 */               BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
/* 2267 */               blockrendererdispatcher.renderBlockDamage(iblockstate, blockpos, textureatlassprite, this.theWorld);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2273 */       tessellatorIn.draw();
/* 2274 */       worldRendererIn.setTranslation(0.0D, 0.0D, 0.0D);
/* 2275 */       postRenderDamagedBlocks();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void drawSelectionBox(EntityPlayer player, MovingObjectPosition movingObjectPositionIn, int p_72731_3_, float partialTicks)
/*      */   {
/* 2284 */     if ((p_72731_3_ == 0) && (movingObjectPositionIn.typeOfHit == net.minecraft.util.MovingObjectPosition.MovingObjectType.BLOCK))
/*      */     {
/* 2286 */       GlStateManager.enableBlend();
/* 2287 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 2288 */       GlStateManager.color(0.0F, 0.0F, 0.0F, 0.4F);
/* 2289 */       GL11.glLineWidth(2.0F);
/* 2290 */       GlStateManager.disableTexture2D();
/* 2291 */       GlStateManager.depthMask(false);
/* 2292 */       float f = 0.002F;
/* 2293 */       BlockPos blockpos = movingObjectPositionIn.getBlockPos();
/* 2294 */       Block block = this.theWorld.getBlockState(blockpos).getBlock();
/*      */       
/* 2296 */       if ((block.getMaterial() != Material.air) && (this.theWorld.getWorldBorder().contains(blockpos)))
/*      */       {
/* 2298 */         block.setBlockBoundsBasedOnState(this.theWorld, blockpos);
/* 2299 */         double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
/* 2300 */         double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
/* 2301 */         double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
/* 2302 */         func_181561_a(block.getSelectedBoundingBox(this.theWorld, blockpos).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-d0, -d1, -d2));
/*      */       }
/*      */       
/* 2305 */       GlStateManager.depthMask(true);
/* 2306 */       GlStateManager.enableTexture2D();
/* 2307 */       GlStateManager.disableBlend();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void func_181561_a(AxisAlignedBB p_181561_0_)
/*      */   {
/* 2313 */     Tessellator tessellator = Tessellator.getInstance();
/* 2314 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 2315 */     worldrenderer.begin(3, DefaultVertexFormats.POSITION);
/* 2316 */     worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
/* 2317 */     worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
/* 2318 */     worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
/* 2319 */     worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
/* 2320 */     worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
/* 2321 */     tessellator.draw();
/* 2322 */     worldrenderer.begin(3, DefaultVertexFormats.POSITION);
/* 2323 */     worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
/* 2324 */     worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
/* 2325 */     worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
/* 2326 */     worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
/* 2327 */     worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
/* 2328 */     tessellator.draw();
/* 2329 */     worldrenderer.begin(1, DefaultVertexFormats.POSITION);
/* 2330 */     worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
/* 2331 */     worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
/* 2332 */     worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
/* 2333 */     worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
/* 2334 */     worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
/* 2335 */     worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
/* 2336 */     worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
/* 2337 */     worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
/* 2338 */     tessellator.draw();
/*      */   }
/*      */   
/*      */   public static void func_181563_a(AxisAlignedBB p_181563_0_, int p_181563_1_, int p_181563_2_, int p_181563_3_, int p_181563_4_)
/*      */   {
/* 2343 */     Tessellator tessellator = Tessellator.getInstance();
/* 2344 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 2345 */     worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
/* 2346 */     worldrenderer.pos(p_181563_0_.minX, p_181563_0_.minY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
/* 2347 */     worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.minY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
/* 2348 */     worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.minY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
/* 2349 */     worldrenderer.pos(p_181563_0_.minX, p_181563_0_.minY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
/* 2350 */     worldrenderer.pos(p_181563_0_.minX, p_181563_0_.minY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
/* 2351 */     tessellator.draw();
/* 2352 */     worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
/* 2353 */     worldrenderer.pos(p_181563_0_.minX, p_181563_0_.maxY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
/* 2354 */     worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.maxY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
/* 2355 */     worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.maxY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
/* 2356 */     worldrenderer.pos(p_181563_0_.minX, p_181563_0_.maxY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
/* 2357 */     worldrenderer.pos(p_181563_0_.minX, p_181563_0_.maxY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
/* 2358 */     tessellator.draw();
/* 2359 */     worldrenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);
/* 2360 */     worldrenderer.pos(p_181563_0_.minX, p_181563_0_.minY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
/* 2361 */     worldrenderer.pos(p_181563_0_.minX, p_181563_0_.maxY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
/* 2362 */     worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.minY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
/* 2363 */     worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.maxY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
/* 2364 */     worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.minY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
/* 2365 */     worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.maxY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
/* 2366 */     worldrenderer.pos(p_181563_0_.minX, p_181563_0_.minY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
/* 2367 */     worldrenderer.pos(p_181563_0_.minX, p_181563_0_.maxY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
/* 2368 */     tessellator.draw();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void markBlocksForUpdate(int x1, int y1, int z1, int x2, int y2, int z2)
/*      */   {
/* 2376 */     this.viewFrustum.markBlocksForUpdate(x1, y1, z1, x2, y2, z2);
/*      */   }
/*      */   
/*      */   public void markBlockForUpdate(BlockPos pos)
/*      */   {
/* 2381 */     int i = pos.getX();
/* 2382 */     int j = pos.getY();
/* 2383 */     int k = pos.getZ();
/* 2384 */     markBlocksForUpdate(i - 1, j - 1, k - 1, i + 1, j + 1, k + 1);
/*      */   }
/*      */   
/*      */   public void notifyLightSet(BlockPos pos)
/*      */   {
/* 2389 */     int i = pos.getX();
/* 2390 */     int j = pos.getY();
/* 2391 */     int k = pos.getZ();
/* 2392 */     markBlocksForUpdate(i - 1, j - 1, k - 1, i + 1, j + 1, k + 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2)
/*      */   {
/* 2401 */     markBlocksForUpdate(x1 - 1, y1 - 1, z1 - 1, x2 + 1, y2 + 1, z2 + 1);
/*      */   }
/*      */   
/*      */   public void playRecord(String recordName, BlockPos blockPosIn)
/*      */   {
/* 2406 */     ISound isound = (ISound)this.mapSoundPositions.get(blockPosIn);
/*      */     
/* 2408 */     if (isound != null)
/*      */     {
/* 2410 */       this.mc.getSoundHandler().stopSound(isound);
/* 2411 */       this.mapSoundPositions.remove(blockPosIn);
/*      */     }
/*      */     
/* 2414 */     if (recordName != null)
/*      */     {
/* 2416 */       ItemRecord itemrecord = ItemRecord.getRecord(recordName);
/*      */       
/* 2418 */       if (itemrecord != null)
/*      */       {
/* 2420 */         this.mc.ingameGUI.setRecordPlayingMessage(itemrecord.getRecordNameLocal());
/*      */       }
/*      */       
/* 2423 */       ResourceLocation resourcelocation = null;
/*      */       
/* 2425 */       if ((Reflector.ForgeItemRecord_getRecordResource.exists()) && (itemrecord != null))
/*      */       {
/* 2427 */         resourcelocation = (ResourceLocation)Reflector.call(itemrecord, Reflector.ForgeItemRecord_getRecordResource, new Object[] { recordName });
/*      */       }
/*      */       
/* 2430 */       if (resourcelocation == null)
/*      */       {
/* 2432 */         resourcelocation = new ResourceLocation(recordName);
/*      */       }
/*      */       
/* 2435 */       PositionedSoundRecord positionedsoundrecord = PositionedSoundRecord.create(resourcelocation, blockPosIn.getX(), blockPosIn.getY(), blockPosIn.getZ());
/* 2436 */       this.mapSoundPositions.put(blockPosIn, positionedsoundrecord);
/* 2437 */       this.mc.getSoundHandler().playSound(positionedsoundrecord);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void playSound(String soundName, double x, double y, double z, float volume, float pitch) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void playSoundToNearExcept(EntityPlayer except, String soundName, double x, double y, double z, float volume, float pitch) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void spawnParticle(int particleID, boolean ignoreRange, final double xCoord, double yCoord, final double zCoord, double xOffset, double yOffset, double zOffset, int... p_180442_15_)
/*      */   {
/*      */     try
/*      */     {
/* 2459 */       spawnEntityFX(particleID, ignoreRange, xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, p_180442_15_);
/*      */     }
/*      */     catch (Throwable throwable)
/*      */     {
/* 2463 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while adding particle");
/* 2464 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being added");
/* 2465 */       crashreportcategory.addCrashSection("ID", Integer.valueOf(particleID));
/*      */       
/* 2467 */       if (p_180442_15_ != null)
/*      */       {
/* 2469 */         crashreportcategory.addCrashSection("Parameters", p_180442_15_);
/*      */       }
/*      */       
/* 2472 */       crashreportcategory.addCrashSectionCallable("Position", new Callable()
/*      */       {
/*      */         private static final String __OBFID = "CL_00000955";
/*      */         
/*      */         public String call() throws Exception {
/* 2477 */           return CrashReportCategory.getCoordinateInfo(xCoord, zCoord, this.val$zCoord);
/*      */         }
/* 2479 */       });
/* 2480 */       throw new net.minecraft.util.ReportedException(crashreport);
/*      */     }
/*      */   }
/*      */   
/*      */   private void spawnParticle(EnumParticleTypes particleIn, double p_174972_2_, double p_174972_4_, double p_174972_6_, double p_174972_8_, double p_174972_10_, double p_174972_12_, int... p_174972_14_)
/*      */   {
/* 2486 */     spawnParticle(particleIn.getParticleID(), particleIn.getShouldIgnoreRange(), p_174972_2_, p_174972_4_, p_174972_6_, p_174972_8_, p_174972_10_, p_174972_12_, p_174972_14_);
/*      */   }
/*      */   
/*      */   private EntityFX spawnEntityFX(int p_174974_1_, boolean ignoreRange, double p_174974_3_, double p_174974_5_, double p_174974_7_, double p_174974_9_, double p_174974_11_, double p_174974_13_, int... p_174974_15_)
/*      */   {
/* 2491 */     if ((this.mc != null) && (this.mc.getRenderViewEntity() != null) && (this.mc.effectRenderer != null))
/*      */     {
/* 2493 */       int i = this.mc.gameSettings.particleSetting;
/*      */       
/* 2495 */       if ((i == 1) && (this.theWorld.rand.nextInt(3) == 0))
/*      */       {
/* 2497 */         i = 2;
/*      */       }
/*      */       
/* 2500 */       double d0 = this.mc.getRenderViewEntity().posX - p_174974_3_;
/* 2501 */       double d1 = this.mc.getRenderViewEntity().posY - p_174974_5_;
/* 2502 */       double d2 = this.mc.getRenderViewEntity().posZ - p_174974_7_;
/*      */       
/* 2504 */       if ((p_174974_1_ == EnumParticleTypes.EXPLOSION_HUGE.getParticleID()) && (!Config.isAnimatedExplosion()))
/*      */       {
/* 2506 */         return null;
/*      */       }
/* 2508 */       if ((p_174974_1_ == EnumParticleTypes.EXPLOSION_LARGE.getParticleID()) && (!Config.isAnimatedExplosion()))
/*      */       {
/* 2510 */         return null;
/*      */       }
/* 2512 */       if ((p_174974_1_ == EnumParticleTypes.EXPLOSION_NORMAL.getParticleID()) && (!Config.isAnimatedExplosion()))
/*      */       {
/* 2514 */         return null;
/*      */       }
/* 2516 */       if ((p_174974_1_ == EnumParticleTypes.SUSPENDED.getParticleID()) && (!Config.isWaterParticles()))
/*      */       {
/* 2518 */         return null;
/*      */       }
/* 2520 */       if ((p_174974_1_ == EnumParticleTypes.SUSPENDED_DEPTH.getParticleID()) && (!Config.isVoidParticles()))
/*      */       {
/* 2522 */         return null;
/*      */       }
/* 2524 */       if ((p_174974_1_ == EnumParticleTypes.SMOKE_NORMAL.getParticleID()) && (!Config.isAnimatedSmoke()))
/*      */       {
/* 2526 */         return null;
/*      */       }
/* 2528 */       if ((p_174974_1_ == EnumParticleTypes.SMOKE_LARGE.getParticleID()) && (!Config.isAnimatedSmoke()))
/*      */       {
/* 2530 */         return null;
/*      */       }
/* 2532 */       if ((p_174974_1_ == EnumParticleTypes.SPELL_MOB.getParticleID()) && (!Config.isPotionParticles()))
/*      */       {
/* 2534 */         return null;
/*      */       }
/* 2536 */       if ((p_174974_1_ == EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID()) && (!Config.isPotionParticles()))
/*      */       {
/* 2538 */         return null;
/*      */       }
/* 2540 */       if ((p_174974_1_ == EnumParticleTypes.SPELL.getParticleID()) && (!Config.isPotionParticles()))
/*      */       {
/* 2542 */         return null;
/*      */       }
/* 2544 */       if ((p_174974_1_ == EnumParticleTypes.SPELL_INSTANT.getParticleID()) && (!Config.isPotionParticles()))
/*      */       {
/* 2546 */         return null;
/*      */       }
/* 2548 */       if ((p_174974_1_ == EnumParticleTypes.SPELL_WITCH.getParticleID()) && (!Config.isPotionParticles()))
/*      */       {
/* 2550 */         return null;
/*      */       }
/* 2552 */       if ((p_174974_1_ == EnumParticleTypes.PORTAL.getParticleID()) && (!Config.isAnimatedPortal()))
/*      */       {
/* 2554 */         return null;
/*      */       }
/* 2556 */       if ((p_174974_1_ == EnumParticleTypes.FLAME.getParticleID()) && (!Config.isAnimatedFlame()))
/*      */       {
/* 2558 */         return null;
/*      */       }
/* 2560 */       if ((p_174974_1_ == EnumParticleTypes.REDSTONE.getParticleID()) && (!Config.isAnimatedRedstone()))
/*      */       {
/* 2562 */         return null;
/*      */       }
/* 2564 */       if ((p_174974_1_ == EnumParticleTypes.DRIP_WATER.getParticleID()) && (!Config.isDrippingWaterLava()))
/*      */       {
/* 2566 */         return null;
/*      */       }
/* 2568 */       if ((p_174974_1_ == EnumParticleTypes.DRIP_LAVA.getParticleID()) && (!Config.isDrippingWaterLava()))
/*      */       {
/* 2570 */         return null;
/*      */       }
/* 2572 */       if ((p_174974_1_ == EnumParticleTypes.FIREWORKS_SPARK.getParticleID()) && (!Config.isFireworkParticles()))
/*      */       {
/* 2574 */         return null;
/*      */       }
/* 2576 */       if (ignoreRange)
/*      */       {
/* 2578 */         return this.mc.effectRenderer.spawnEffectParticle(p_174974_1_, p_174974_3_, p_174974_5_, p_174974_7_, p_174974_9_, p_174974_11_, p_174974_13_, p_174974_15_);
/*      */       }
/*      */       
/*      */ 
/* 2582 */       double d3 = 16.0D;
/* 2583 */       double d4 = 256.0D;
/*      */       
/* 2585 */       if (p_174974_1_ == EnumParticleTypes.CRIT.getParticleID())
/*      */       {
/* 2587 */         d4 = 38416.0D;
/*      */       }
/*      */       
/* 2590 */       if (d0 * d0 + d1 * d1 + d2 * d2 > d4)
/*      */       {
/* 2592 */         return null;
/*      */       }
/* 2594 */       if (i > 1)
/*      */       {
/* 2596 */         return null;
/*      */       }
/*      */       
/*      */ 
/* 2600 */       EntityFX entityfx = this.mc.effectRenderer.spawnEffectParticle(p_174974_1_, p_174974_3_, p_174974_5_, p_174974_7_, p_174974_9_, p_174974_11_, p_174974_13_, p_174974_15_);
/*      */       
/* 2602 */       if (p_174974_1_ == EnumParticleTypes.WATER_BUBBLE.getParticleID())
/*      */       {
/* 2604 */         CustomColorizer.updateWaterFX(entityfx, this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_);
/*      */       }
/*      */       
/* 2607 */       if (p_174974_1_ == EnumParticleTypes.WATER_SPLASH.getParticleID())
/*      */       {
/* 2609 */         CustomColorizer.updateWaterFX(entityfx, this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_);
/*      */       }
/*      */       
/* 2612 */       if (p_174974_1_ == EnumParticleTypes.WATER_DROP.getParticleID())
/*      */       {
/* 2614 */         CustomColorizer.updateWaterFX(entityfx, this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_);
/*      */       }
/*      */       
/* 2617 */       if (p_174974_1_ == EnumParticleTypes.TOWN_AURA.getParticleID())
/*      */       {
/* 2619 */         CustomColorizer.updateMyceliumFX(entityfx);
/*      */       }
/*      */       
/* 2622 */       if (p_174974_1_ == EnumParticleTypes.PORTAL.getParticleID())
/*      */       {
/* 2624 */         CustomColorizer.updatePortalFX(entityfx);
/*      */       }
/*      */       
/* 2627 */       if (p_174974_1_ == EnumParticleTypes.REDSTONE.getParticleID())
/*      */       {
/* 2629 */         CustomColorizer.updateReddustFX(entityfx, this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_);
/*      */       }
/*      */       
/* 2632 */       return entityfx;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2638 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onEntityAdded(Entity entityIn)
/*      */   {
/* 2648 */     optfine.RandomMobs.entityLoaded(entityIn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onEntityRemoved(Entity entityIn) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void deleteAllDisplayLists() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void broadcastSound(int p_180440_1_, BlockPos p_180440_2_, int p_180440_3_)
/*      */   {
/* 2668 */     switch (p_180440_1_)
/*      */     {
/*      */     case 1013: 
/*      */     case 1018: 
/* 2672 */       if (this.mc.getRenderViewEntity() != null)
/*      */       {
/* 2674 */         double d0 = p_180440_2_.getX() - this.mc.getRenderViewEntity().posX;
/* 2675 */         double d1 = p_180440_2_.getY() - this.mc.getRenderViewEntity().posY;
/* 2676 */         double d2 = p_180440_2_.getZ() - this.mc.getRenderViewEntity().posZ;
/* 2677 */         double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
/* 2678 */         double d4 = this.mc.getRenderViewEntity().posX;
/* 2679 */         double d5 = this.mc.getRenderViewEntity().posY;
/* 2680 */         double d6 = this.mc.getRenderViewEntity().posZ;
/*      */         
/* 2682 */         if (d3 > 0.0D)
/*      */         {
/* 2684 */           d4 += d0 / d3 * 2.0D;
/* 2685 */           d5 += d1 / d3 * 2.0D;
/* 2686 */           d6 += d2 / d3 * 2.0D;
/*      */         }
/*      */         
/* 2689 */         if (p_180440_1_ == 1013)
/*      */         {
/* 2691 */           this.theWorld.playSound(d4, d5, d6, "mob.wither.spawn", 1.0F, 1.0F, false);
/*      */         }
/*      */         else
/*      */         {
/* 2695 */           this.theWorld.playSound(d4, d5, d6, "mob.enderdragon.end", 5.0F, 1.0F, false);
/*      */         }
/*      */       }
/*      */       break;
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */   public void playAuxSFX(EntityPlayer player, int sfxType, BlockPos blockPosIn, int p_180439_4_)
/*      */   {
/* 2705 */     Random random = this.theWorld.rand;
/*      */     
/* 2707 */     switch (sfxType)
/*      */     {
/*      */     case 1000: 
/* 2710 */       this.theWorld.playSoundAtPos(blockPosIn, "random.click", 1.0F, 1.0F, false);
/* 2711 */       break;
/*      */     
/*      */     case 1001: 
/* 2714 */       this.theWorld.playSoundAtPos(blockPosIn, "random.click", 1.0F, 1.2F, false);
/* 2715 */       break;
/*      */     
/*      */     case 1002: 
/* 2718 */       this.theWorld.playSoundAtPos(blockPosIn, "random.bow", 1.0F, 1.2F, false);
/* 2719 */       break;
/*      */     
/*      */     case 1003: 
/* 2722 */       this.theWorld.playSoundAtPos(blockPosIn, "random.door_open", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
/* 2723 */       break;
/*      */     
/*      */     case 1004: 
/* 2726 */       this.theWorld.playSoundAtPos(blockPosIn, "random.fizz", 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F, false);
/* 2727 */       break;
/*      */     
/*      */     case 1005: 
/* 2730 */       if ((Item.getItemById(p_180439_4_) instanceof ItemRecord))
/*      */       {
/* 2732 */         this.theWorld.playRecord(blockPosIn, "records." + ((ItemRecord)Item.getItemById(p_180439_4_)).recordName);
/*      */       }
/*      */       else
/*      */       {
/* 2736 */         this.theWorld.playRecord(blockPosIn, null);
/*      */       }
/*      */       
/* 2739 */       break;
/*      */     
/*      */     case 1006: 
/* 2742 */       this.theWorld.playSoundAtPos(blockPosIn, "random.door_close", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
/* 2743 */       break;
/*      */     
/*      */     case 1007: 
/* 2746 */       this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.charge", 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/* 2747 */       break;
/*      */     
/*      */     case 1008: 
/* 2750 */       this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.fireball", 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/* 2751 */       break;
/*      */     
/*      */     case 1009: 
/* 2754 */       this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.fireball", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/* 2755 */       break;
/*      */     
/*      */     case 1010: 
/* 2758 */       this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.wood", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/* 2759 */       break;
/*      */     
/*      */     case 1011: 
/* 2762 */       this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.metal", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/* 2763 */       break;
/*      */     
/*      */     case 1012: 
/* 2766 */       this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.woodbreak", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/* 2767 */       break;
/*      */     
/*      */     case 1014: 
/* 2770 */       this.theWorld.playSoundAtPos(blockPosIn, "mob.wither.shoot", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/* 2771 */       break;
/*      */     
/*      */     case 1015: 
/* 2774 */       this.theWorld.playSoundAtPos(blockPosIn, "mob.bat.takeoff", 0.05F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/* 2775 */       break;
/*      */     
/*      */     case 1016: 
/* 2778 */       this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.infect", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/* 2779 */       break;
/*      */     
/*      */     case 1017: 
/* 2782 */       this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.unfect", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/* 2783 */       break;
/*      */     
/*      */     case 1020: 
/* 2786 */       this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_break", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
/* 2787 */       break;
/*      */     
/*      */     case 1021: 
/* 2790 */       this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_use", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
/* 2791 */       break;
/*      */     
/*      */     case 1022: 
/* 2794 */       this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_land", 0.3F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
/* 2795 */       break;
/*      */     
/*      */     case 2000: 
/* 2798 */       int k = p_180439_4_ % 3 - 1;
/* 2799 */       int l = p_180439_4_ / 3 % 3 - 1;
/* 2800 */       double d13 = blockPosIn.getX() + k * 0.6D + 0.5D;
/* 2801 */       double d15 = blockPosIn.getY() + 0.5D;
/* 2802 */       double d19 = blockPosIn.getZ() + l * 0.6D + 0.5D;
/*      */       
/* 2804 */       for (int l1 = 0; l1 < 10; l1++)
/*      */       {
/* 2806 */         double d20 = random.nextDouble() * 0.2D + 0.01D;
/* 2807 */         double d21 = d13 + k * 0.01D + (random.nextDouble() - 0.5D) * l * 0.5D;
/* 2808 */         double d22 = d15 + (random.nextDouble() - 0.5D) * 0.5D;
/* 2809 */         double d23 = d19 + l * 0.01D + (random.nextDouble() - 0.5D) * k * 0.5D;
/* 2810 */         double d24 = k * d20 + random.nextGaussian() * 0.01D;
/* 2811 */         double d9 = -0.03D + random.nextGaussian() * 0.01D;
/* 2812 */         double d10 = l * d20 + random.nextGaussian() * 0.01D;
/* 2813 */         spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d21, d22, d23, d24, d9, d10, new int[0]);
/*      */       }
/*      */       
/* 2816 */       return;
/*      */     
/*      */     case 2001: 
/* 2819 */       Block block = Block.getBlockById(p_180439_4_ & 0xFFF);
/*      */       
/* 2821 */       if (block.getMaterial() != Material.air)
/*      */       {
/* 2823 */         this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(block.stepSound.getBreakSound()), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getFrequency() * 0.8F, blockPosIn.getX() + 0.5F, blockPosIn.getY() + 0.5F, blockPosIn.getZ() + 0.5F));
/*      */       }
/*      */       
/* 2826 */       this.mc.effectRenderer.addBlockDestroyEffects(blockPosIn, block.getStateFromMeta(p_180439_4_ >> 12 & 0xFF));
/* 2827 */       break;
/*      */     
/*      */     case 2002: 
/* 2830 */       double d11 = blockPosIn.getX();
/* 2831 */       double d12 = blockPosIn.getY();
/* 2832 */       double d14 = blockPosIn.getZ();
/*      */       
/* 2834 */       for (int i1 = 0; i1 < 8; i1++)
/*      */       {
/* 2836 */         spawnParticle(EnumParticleTypes.ITEM_CRACK, d11, d12, d14, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D, new int[] { Item.getIdFromItem(Items.potionitem), p_180439_4_ });
/*      */       }
/*      */       
/* 2839 */       int j1 = Items.potionitem.getColorFromDamage(p_180439_4_);
/* 2840 */       float f = (j1 >> 16 & 0xFF) / 255.0F;
/* 2841 */       float f1 = (j1 >> 8 & 0xFF) / 255.0F;
/* 2842 */       float f2 = (j1 >> 0 & 0xFF) / 255.0F;
/* 2843 */       EnumParticleTypes enumparticletypes = EnumParticleTypes.SPELL;
/*      */       
/* 2845 */       if (Items.potionitem.isEffectInstant(p_180439_4_))
/*      */       {
/* 2847 */         enumparticletypes = EnumParticleTypes.SPELL_INSTANT;
/*      */       }
/*      */       
/* 2850 */       for (int k1 = 0; k1 < 100; k1++)
/*      */       {
/* 2852 */         double d16 = random.nextDouble() * 4.0D;
/* 2853 */         double d17 = random.nextDouble() * 3.141592653589793D * 2.0D;
/* 2854 */         double d18 = Math.cos(d17) * d16;
/* 2855 */         double d7 = 0.01D + random.nextDouble() * 0.5D;
/* 2856 */         double d8 = Math.sin(d17) * d16;
/* 2857 */         EntityFX entityfx = spawnEntityFX(enumparticletypes.getParticleID(), enumparticletypes.getShouldIgnoreRange(), d11 + d18 * 0.1D, d12 + 0.3D, d14 + d8 * 0.1D, d18, d7, d8, new int[0]);
/*      */         
/* 2859 */         if (entityfx != null)
/*      */         {
/* 2861 */           float f3 = 0.75F + random.nextFloat() * 0.25F;
/* 2862 */           entityfx.setRBGColorF(f * f3, f1 * f3, f2 * f3);
/* 2863 */           entityfx.multiplyVelocity((float)d16);
/*      */         }
/*      */       }
/*      */       
/* 2867 */       this.theWorld.playSoundAtPos(blockPosIn, "game.potion.smash", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
/* 2868 */       break;
/*      */     
/*      */     case 2003: 
/* 2871 */       double var7 = blockPosIn.getX() + 0.5D;
/* 2872 */       double var9 = blockPosIn.getY();
/* 2873 */       double var11 = blockPosIn.getZ() + 0.5D;
/*      */       
/* 2875 */       for (int var13 = 0; var13 < 8; var13++)
/*      */       {
/* 2877 */         spawnParticle(EnumParticleTypes.ITEM_CRACK, var7, var9, var11, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D, new int[] { Item.getIdFromItem(Items.ender_eye) });
/*      */       }
/*      */       
/* 2880 */       for (double var32 = 0.0D; var32 < 6.283185307179586D; var32 += 0.15707963267948966D)
/*      */       {
/* 2882 */         spawnParticle(EnumParticleTypes.PORTAL, var7 + Math.cos(var32) * 5.0D, var9 - 0.4D, var11 + Math.sin(var32) * 5.0D, Math.cos(var32) * -5.0D, 0.0D, Math.sin(var32) * -5.0D, new int[0]);
/* 2883 */         spawnParticle(EnumParticleTypes.PORTAL, var7 + Math.cos(var32) * 5.0D, var9 - 0.4D, var11 + Math.sin(var32) * 5.0D, Math.cos(var32) * -7.0D, 0.0D, Math.sin(var32) * -7.0D, new int[0]);
/*      */       }
/*      */       
/* 2886 */       return;
/*      */     
/*      */     case 2004: 
/* 2889 */       for (int var18 = 0; var18 < 20; var18++)
/*      */       {
/* 2891 */         double d3 = blockPosIn.getX() + 0.5D + (this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
/* 2892 */         double d4 = blockPosIn.getY() + 0.5D + (this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
/* 2893 */         double d5 = blockPosIn.getZ() + 0.5D + (this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
/* 2894 */         this.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);
/* 2895 */         this.theWorld.spawnParticle(EnumParticleTypes.FLAME, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);
/*      */       }
/*      */       
/* 2898 */       return;
/*      */     
/*      */     case 2005: 
/* 2901 */       net.minecraft.item.ItemDye.spawnBonemealParticles(this.theWorld, blockPosIn, p_180439_4_);
/*      */     }
/*      */   }
/*      */   
/*      */   public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress)
/*      */   {
/* 2907 */     if ((progress >= 0) && (progress < 10))
/*      */     {
/* 2909 */       DestroyBlockProgress destroyblockprogress = (DestroyBlockProgress)this.damagedBlocks.get(Integer.valueOf(breakerId));
/*      */       
/* 2911 */       if ((destroyblockprogress == null) || (destroyblockprogress.getPosition().getX() != pos.getX()) || (destroyblockprogress.getPosition().getY() != pos.getY()) || (destroyblockprogress.getPosition().getZ() != pos.getZ()))
/*      */       {
/* 2913 */         destroyblockprogress = new DestroyBlockProgress(breakerId, pos);
/* 2914 */         this.damagedBlocks.put(Integer.valueOf(breakerId), destroyblockprogress);
/*      */       }
/*      */       
/* 2917 */       destroyblockprogress.setPartialBlockDamage(progress);
/* 2918 */       destroyblockprogress.setCloudUpdateTick(this.cloudTickCounter);
/*      */     }
/*      */     else
/*      */     {
/* 2922 */       this.damagedBlocks.remove(Integer.valueOf(breakerId));
/*      */     }
/*      */   }
/*      */   
/*      */   public void setDisplayListEntitiesDirty()
/*      */   {
/* 2928 */     this.displayListEntitiesDirty = true;
/*      */   }
/*      */   
/*      */   public void resetClouds()
/*      */   {
/* 2933 */     this.cloudRenderer.reset();
/*      */   }
/*      */   
/*      */   public int getCountRenderers()
/*      */   {
/* 2938 */     return this.viewFrustum.renderChunks.length;
/*      */   }
/*      */   
/*      */   public int getCountActiveRenderers()
/*      */   {
/* 2943 */     return this.renderInfos.size();
/*      */   }
/*      */   
/*      */   public int getCountEntitiesRendered()
/*      */   {
/* 2948 */     return this.countEntitiesRendered;
/*      */   }
/*      */   
/*      */   public int getCountTileEntitiesRendered()
/*      */   {
/* 2953 */     return this.countTileEntitiesRendered;
/*      */   }
/*      */   
/*      */   public void func_181023_a(Collection p_181023_1_, Collection p_181023_2_)
/*      */   {
/* 2958 */     Set set = this.field_181024_n;
/*      */     
/* 2960 */     synchronized (this.field_181024_n)
/*      */     {
/* 2962 */       this.field_181024_n.removeAll(p_181023_1_);
/* 2963 */       this.field_181024_n.addAll(p_181023_2_);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   class ContainerLocalRenderInformation
/*      */   {
/*      */     final RenderChunk renderChunk;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     final EnumFacing facing;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     final Set setFacing;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     final int counter;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private static final String __OBFID = "CL_00002534";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private ContainerLocalRenderInformation(RenderChunk renderChunkIn, EnumFacing facingIn, int counterIn)
/*      */     {
/* 3013 */       this.setFacing = EnumSet.noneOf(EnumFacing.class);
/* 3014 */       this.renderChunk = renderChunkIn;
/* 3015 */       this.facing = facingIn;
/* 3016 */       this.counter = counterIn;
/*      */     }
/*      */     
/*      */     ContainerLocalRenderInformation(RenderChunk p_i4_2_, EnumFacing p_i4_3_, int p_i4_4_, Object p_i4_5_)
/*      */     {
/* 3021 */       this(p_i4_2_, p_i4_3_, p_i4_4_);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\RenderGlobal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */