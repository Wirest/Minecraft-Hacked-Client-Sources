package net.minecraft.client.renderer;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonSyntaxException;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.ListChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.chunk.VboChunkFactory;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemRecord;
import net.minecraft.src.ChunkUtils;
import net.minecraft.src.CloudRenderer;
import net.minecraft.src.Config;
import net.minecraft.src.CustomColorizer;
import net.minecraft.src.CustomSky;
import net.minecraft.src.Lagometer;
import net.minecraft.src.RandomMobs;
import net.minecraft.src.Reflector;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;

public class RenderGlobal implements IWorldAccess, IResourceManagerReloadListener
{
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation locationMoonPhasesPng = new ResourceLocation("textures/environment/moon_phases.png");
    private static final ResourceLocation locationSunPng = new ResourceLocation("textures/environment/sun.png");
    private static final ResourceLocation locationCloudsPng = new ResourceLocation("textures/environment/clouds.png");
    private static final ResourceLocation locationEndSkyPng = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation locationForcefieldPng = new ResourceLocation("textures/misc/forcefield.png");
    
    /** A reference to the Minecraft object. */
    public final Minecraft mc;
    
    /** The RenderEngine instance used by RenderGlobal */
    private final TextureManager renderEngine;
    private final RenderManager renderManager;
    private WorldClient theWorld;
    private Set chunksToUpdate = Sets.newLinkedHashSet();
    
    /** List of OpenGL lists for the current render pass */
    private List renderInfos = Lists.newArrayListWithCapacity(69696);
    private ViewFrustum viewFrustum;
    
    /** The star GL Call list */
    private int starGLCallList = -1;
    
    /** OpenGL sky list */
    private int glSkyList = -1;
    
    /** OpenGL sky list 2 */
    private int glSkyList2 = -1;
    private VertexFormat vertexBufferFormat;
    private VertexBuffer starVBO;
    private VertexBuffer skyVBO;
    private VertexBuffer sky2VBO;
    
    /**
     * counts the cloud render updates. Used with mod to stagger some updates
     */
    private int cloudTickCounter;
    
    /**
     * Stores blocks currently being broken. Key is entity ID of the thing doing the breaking. Value is a
     * DestroyBlockProgress
     */
    public final Map damagedBlocks = Maps.newHashMap();
    
    /** Currently playing sounds.  Type:  HashMap<ChunkCoordinates, ISound> */
    private final Map mapSoundPositions = Maps.newHashMap();
    private final TextureAtlasSprite[] destroyBlockIcons = new TextureAtlasSprite[10];
    private Framebuffer entityOutlineFramebuffer;
    
    /** Stores the shader group for the entity_outline shader */
    private ShaderGroup entityOutlineShader;
    private double frustumUpdatePosX = Double.MIN_VALUE;
    private double frustumUpdatePosY = Double.MIN_VALUE;
    private double frustumUpdatePosZ = Double.MIN_VALUE;
    private int frustumUpdatePosChunkX = Integer.MIN_VALUE;
    private int frustumUpdatePosChunkY = Integer.MIN_VALUE;
    private int frustumUpdatePosChunkZ = Integer.MIN_VALUE;
    private double lastViewEntityX = Double.MIN_VALUE;
    private double lastViewEntityY = Double.MIN_VALUE;
    private double lastViewEntityZ = Double.MIN_VALUE;
    private double lastViewEntityPitch = Double.MIN_VALUE;
    private double lastViewEntityYaw = Double.MIN_VALUE;
    private final ChunkRenderDispatcher renderDispatcher = new ChunkRenderDispatcher();
    private ChunkRenderContainer renderContainer;
    private int renderDistanceChunks = -1;
    
    /** Render entities startup counter (init value=2) */
    private int renderEntitiesStartupCounter = 2;
    
    /** Count entities total */
    private int countEntitiesTotal;
    
    /** Count entities rendered */
    private int countEntitiesRendered;
    
    /** Count entities hidden */
    private int countEntitiesHidden;
    private boolean debugFixTerrainFrustum = false;
    private ClippingHelper debugFixedClippingHelper;
    private final Vector4f[] debugTerrainMatrix = new Vector4f[8];
    private final Vector3d debugTerrainFrustumPosition = new Vector3d();
    private boolean vboEnabled = false;
    IRenderChunkFactory renderChunkFactory;
    private double prevRenderSortX;
    private double prevRenderSortY;
    private double prevRenderSortZ;
    public boolean displayListEntitiesDirty = true;
    private CloudRenderer cloudRenderer;
    public Entity renderedEntity;
    public Set chunksToResortTransparency = new LinkedHashSet();
    public Set chunksToUpdateForced = new LinkedHashSet();
    private Deque visibilityDeque = new ArrayDeque();
    private List renderInfosEntities = new ArrayList(1024);
    private List renderInfosTileEntities = new ArrayList(1024);
    private int renderDistance = 0;
    private int renderDistanceSq = 0;
    private static final Set SET_ALL_FACINGS = Collections.unmodifiableSet(new HashSet(Arrays.asList(EnumFacing.VALUES)));
    
    public RenderGlobal(Minecraft mcIn)
    {
	this.cloudRenderer = new CloudRenderer(mcIn);
	this.mc = mcIn;
	this.renderManager = mcIn.getRenderManager();
	this.renderEngine = mcIn.getTextureManager();
	this.renderEngine.bindTexture(locationForcefieldPng);
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
	GlStateManager.bindTexture(0);
	this.updateDestroyBlockIcons();
	this.vboEnabled = OpenGlHelper.useVbo();
	
	if (this.vboEnabled)
	{
	    this.renderContainer = new VboRenderList();
	    this.renderChunkFactory = new VboChunkFactory();
	}
	else
	{
	    this.renderContainer = new RenderList();
	    this.renderChunkFactory = new ListChunkFactory();
	}
	
	this.vertexBufferFormat = new VertexFormat();
	this.vertexBufferFormat.setElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUseage.POSITION, 3));
	this.generateStars();
	this.generateSky();
	this.generateSky2();
    }
    
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
	this.updateDestroyBlockIcons();
    }
    
    private void updateDestroyBlockIcons()
    {
	TextureMap var1 = this.mc.getTextureMapBlocks();
	
	for (int var2 = 0; var2 < this.destroyBlockIcons.length; ++var2)
	{
	    this.destroyBlockIcons[var2] = var1.getAtlasSprite("minecraft:blocks/destroy_stage_" + var2);
	}
    }
    
    /**
     * Creates the entity outline shader to be stored in RenderGlobal.entityOutlineShader
     */
    public void makeEntityOutlineShader()
    {
	if (OpenGlHelper.shadersSupported)
	{
	    if (ShaderLinkHelper.getStaticShaderLinkHelper() == null)
	    {
		ShaderLinkHelper.setNewStaticShaderLinkHelper();
	    }
	    
	    ResourceLocation var1 = new ResourceLocation("shaders/post/entity_outline.json");
	    
	    try
	    {
		this.entityOutlineShader = new ShaderGroup(this.mc.getTextureManager(), this.mc.getResourceManager(), this.mc.getFramebuffer(), var1);
		this.entityOutlineShader.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
		this.entityOutlineFramebuffer = this.entityOutlineShader.getFramebufferRaw("final");
	    }
	    catch (IOException var3)
	    {
		logger.warn("Failed to load shader: " + var1, var3);
		this.entityOutlineShader = null;
		this.entityOutlineFramebuffer = null;
	    }
	    catch (JsonSyntaxException var4)
	    {
		logger.warn("Failed to load shader: " + var1, var4);
		this.entityOutlineShader = null;
		this.entityOutlineFramebuffer = null;
	    }
	}
	else
	{
	    this.entityOutlineShader = null;
	    this.entityOutlineFramebuffer = null;
	}
    }
    
    public void renderEntityOutlineFramebuffer()
    {
	if (this.isRenderEntityOutlines())
	{
	    GlStateManager.enableBlend();
	    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
	    this.entityOutlineFramebuffer.framebufferRenderExt(this.mc.displayWidth, this.mc.displayHeight, false);
	    GlStateManager.disableBlend();
	}
    }
    
    protected boolean isRenderEntityOutlines()
    {
	return this.entityOutlineFramebuffer != null && this.entityOutlineShader != null && this.mc.thePlayer != null && this.mc.thePlayer.isSpectator() && this.mc.gameSettings.keyBindSpectatorOutlines.isKeyDown();
    }
    
    private void generateSky2()
    {
	Tessellator var1 = Tessellator.getInstance();
	WorldRenderer var2 = var1.getWorldRenderer();
	
	if (this.sky2VBO != null)
	{
	    this.sky2VBO.deleteGlBuffers();
	}
	
	if (this.glSkyList2 >= 0)
	{
	    GLAllocation.deleteDisplayLists(this.glSkyList2);
	    this.glSkyList2 = -1;
	}
	
	if (this.vboEnabled)
	{
	    this.sky2VBO = new VertexBuffer(this.vertexBufferFormat);
	    this.renderSky(var2, -16.0F, true);
	    var2.finishDrawing();
	    var2.reset();
	    this.sky2VBO.bufferData(var2.getByteBuffer(), var2.getByteIndex());
	}
	else
	{
	    this.glSkyList2 = GLAllocation.generateDisplayLists(1);
	    GL11.glNewList(this.glSkyList2, GL11.GL_COMPILE);
	    this.renderSky(var2, -16.0F, true);
	    var1.draw();
	    GL11.glEndList();
	}
    }
    
    private void generateSky()
    {
	Tessellator var1 = Tessellator.getInstance();
	WorldRenderer var2 = var1.getWorldRenderer();
	
	if (this.skyVBO != null)
	{
	    this.skyVBO.deleteGlBuffers();
	}
	
	if (this.glSkyList >= 0)
	{
	    GLAllocation.deleteDisplayLists(this.glSkyList);
	    this.glSkyList = -1;
	}
	
	if (this.vboEnabled)
	{
	    this.skyVBO = new VertexBuffer(this.vertexBufferFormat);
	    this.renderSky(var2, 16.0F, false);
	    var2.finishDrawing();
	    var2.reset();
	    this.skyVBO.bufferData(var2.getByteBuffer(), var2.getByteIndex());
	}
	else
	{
	    this.glSkyList = GLAllocation.generateDisplayLists(1);
	    GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
	    this.renderSky(var2, 16.0F, false);
	    var1.draw();
	    GL11.glEndList();
	}
    }
    
    private void renderSky(WorldRenderer worldRendererIn, float p_174968_2_, boolean p_174968_3_)
    {
	worldRendererIn.startDrawingQuads();
	
	for (int var6 = -384; var6 <= 384; var6 += 64)
	{
	    for (int var7 = -384; var7 <= 384; var7 += 64)
	    {
		float var8 = var6;
		float var9 = var6 + 64;
		
		if (p_174968_3_)
		{
		    var9 = var6;
		    var8 = var6 + 64;
		}
		
		worldRendererIn.addVertex(var8, p_174968_2_, var7);
		worldRendererIn.addVertex(var9, p_174968_2_, var7);
		worldRendererIn.addVertex(var9, p_174968_2_, var7 + 64);
		worldRendererIn.addVertex(var8, p_174968_2_, var7 + 64);
	    }
	}
    }
    
    private void generateStars()
    {
	Tessellator var1 = Tessellator.getInstance();
	WorldRenderer var2 = var1.getWorldRenderer();
	
	if (this.starVBO != null)
	{
	    this.starVBO.deleteGlBuffers();
	}
	
	if (this.starGLCallList >= 0)
	{
	    GLAllocation.deleteDisplayLists(this.starGLCallList);
	    this.starGLCallList = -1;
	}
	
	if (this.vboEnabled)
	{
	    this.starVBO = new VertexBuffer(this.vertexBufferFormat);
	    this.renderStars(var2);
	    var2.finishDrawing();
	    var2.reset();
	    this.starVBO.bufferData(var2.getByteBuffer(), var2.getByteIndex());
	}
	else
	{
	    this.starGLCallList = GLAllocation.generateDisplayLists(1);
	    GlStateManager.pushMatrix();
	    GL11.glNewList(this.starGLCallList, GL11.GL_COMPILE);
	    this.renderStars(var2);
	    var1.draw();
	    GL11.glEndList();
	    GlStateManager.popMatrix();
	}
    }
    
    private void renderStars(WorldRenderer worldRendererIn)
    {
	Random var2 = new Random(10842L);
	worldRendererIn.startDrawingQuads();
	
	for (int var3 = 0; var3 < 1500; ++var3)
	{
	    double var4 = var2.nextFloat() * 2.0F - 1.0F;
	    double var6 = var2.nextFloat() * 2.0F - 1.0F;
	    double var8 = var2.nextFloat() * 2.0F - 1.0F;
	    double var10 = 0.15F + var2.nextFloat() * 0.1F;
	    double var12 = var4 * var4 + var6 * var6 + var8 * var8;
	    
	    if (var12 < 1.0D && var12 > 0.01D)
	    {
		var12 = 1.0D / Math.sqrt(var12);
		var4 *= var12;
		var6 *= var12;
		var8 *= var12;
		double var14 = var4 * 100.0D;
		double var16 = var6 * 100.0D;
		double var18 = var8 * 100.0D;
		double var20 = Math.atan2(var4, var8);
		double var22 = Math.sin(var20);
		double var24 = Math.cos(var20);
		double var26 = Math.atan2(Math.sqrt(var4 * var4 + var8 * var8), var6);
		double var28 = Math.sin(var26);
		double var30 = Math.cos(var26);
		double var32 = var2.nextDouble() * Math.PI * 2.0D;
		double var34 = Math.sin(var32);
		double var36 = Math.cos(var32);
		
		for (int var38 = 0; var38 < 4; ++var38)
		{
		    double var41 = ((var38 & 2) - 1) * var10;
		    double var43 = ((var38 + 1 & 2) - 1) * var10;
		    double var47 = var41 * var36 - var43 * var34;
		    double var49 = var43 * var36 + var41 * var34;
		    double var53 = var47 * var28 + 0.0D * var30;
		    double var55 = 0.0D * var28 - var47 * var30;
		    double var57 = var55 * var22 - var49 * var24;
		    double var61 = var49 * var22 + var55 * var24;
		    worldRendererIn.addVertex(var14 + var57, var16 + var53, var18 + var61);
		}
	    }
	}
    }
    
    /**
     * set null to clear
     */
    public void setWorldAndLoadRenderers(WorldClient worldClientIn)
    {
	if (this.theWorld != null)
	{
	    this.theWorld.removeWorldAccess(this);
	}
	
	this.frustumUpdatePosX = Double.MIN_VALUE;
	this.frustumUpdatePosY = Double.MIN_VALUE;
	this.frustumUpdatePosZ = Double.MIN_VALUE;
	this.frustumUpdatePosChunkX = Integer.MIN_VALUE;
	this.frustumUpdatePosChunkY = Integer.MIN_VALUE;
	this.frustumUpdatePosChunkZ = Integer.MIN_VALUE;
	this.renderManager.set(worldClientIn);
	this.theWorld = worldClientIn;
	
	if (worldClientIn != null)
	{
	    worldClientIn.addWorldAccess(this);
	    this.loadRenderers();
	}
    }
    
    /**
     * Loads all the renderers and sets up the basic settings usage
     */
    public void loadRenderers()
    {
	if (this.theWorld != null)
	{
	    this.displayListEntitiesDirty = true;
	    Blocks.leaves.setGraphicsLevel(Config.isTreesFancy());
	    Blocks.leaves2.setGraphicsLevel(Config.isTreesFancy());
	    BlockModelRenderer.updateAoLightValue();
	    this.renderDistanceChunks = this.mc.gameSettings.renderDistanceChunks;
	    this.renderDistance = this.renderDistanceChunks * 16;
	    this.renderDistanceSq = this.renderDistance * this.renderDistance;
	    boolean var1 = this.vboEnabled;
	    this.vboEnabled = OpenGlHelper.useVbo();
	    
	    if (var1 && !this.vboEnabled)
	    {
		this.renderContainer = new RenderList();
		this.renderChunkFactory = new ListChunkFactory();
	    }
	    else if (!var1 && this.vboEnabled)
	    {
		this.renderContainer = new VboRenderList();
		this.renderChunkFactory = new VboChunkFactory();
	    }
	    
	    if (var1 != this.vboEnabled)
	    {
		this.generateStars();
		this.generateSky();
		this.generateSky2();
	    }
	    
	    if (this.viewFrustum != null)
	    {
		this.viewFrustum.deleteGlResources();
	    }
	    
	    this.stopChunkUpdates();
	    this.viewFrustum = new ViewFrustum(this.theWorld, this.mc.gameSettings.renderDistanceChunks, this, this.renderChunkFactory);
	    
	    if (this.theWorld != null)
	    {
		Entity var2 = this.mc.getRenderViewEntity();
		
		if (var2 != null)
		{
		    this.viewFrustum.updateChunkPositions(var2.posX, var2.posZ);
		}
	    }
	    
	    this.renderEntitiesStartupCounter = 2;
	}
    }
    
    protected void stopChunkUpdates()
    {
	this.chunksToUpdate.clear();
	this.renderDispatcher.stopChunkUpdates();
    }
    
    public void createBindEntityOutlineFbs(int p_72720_1_, int p_72720_2_)
    {
	if (OpenGlHelper.shadersSupported && this.entityOutlineShader != null)
	{
	    this.entityOutlineShader.createBindFramebuffers(p_72720_1_, p_72720_2_);
	}
    }
    
    public void renderEntities(Entity renderViewEntity, ICamera camera, float partialTicks)
    {
	int pass = 0;
	
	if (Reflector.MinecraftForgeClient_getRenderPass.exists())
	{
	    pass = Reflector.callInt(Reflector.MinecraftForgeClient_getRenderPass, new Object[0]);
	}
	
	if (this.renderEntitiesStartupCounter > 0)
	{
	    if (pass > 0)
	    {
		return;
	    }
	    
	    --this.renderEntitiesStartupCounter;
	}
	else
	{
	    double var4 = renderViewEntity.prevPosX + (renderViewEntity.posX - renderViewEntity.prevPosX) * partialTicks;
	    double var6 = renderViewEntity.prevPosY + (renderViewEntity.posY - renderViewEntity.prevPosY) * partialTicks;
	    double var8 = renderViewEntity.prevPosZ + (renderViewEntity.posZ - renderViewEntity.prevPosZ) * partialTicks;
	    this.theWorld.theProfiler.startSection("prepare");
	    TileEntityRendererDispatcher.instance.cacheActiveRenderInfo(this.theWorld, this.mc.getTextureManager(), this.mc.fontRendererObj, this.mc.getRenderViewEntity(), partialTicks);
	    this.renderManager.cacheActiveRenderInfo(this.theWorld, this.mc.fontRendererObj, this.mc.getRenderViewEntity(), this.mc.pointedEntity, this.mc.gameSettings, partialTicks);
	    
	    if (pass == 0)
	    {
		this.countEntitiesTotal = 0;
		this.countEntitiesRendered = 0;
		this.countEntitiesHidden = 0;
	    }
	    
	    Entity var10 = this.mc.getRenderViewEntity();
	    double var11 = var10.lastTickPosX + (var10.posX - var10.lastTickPosX) * partialTicks;
	    double var13 = var10.lastTickPosY + (var10.posY - var10.lastTickPosY) * partialTicks;
	    double var15 = var10.lastTickPosZ + (var10.posZ - var10.lastTickPosZ) * partialTicks;
	    TileEntityRendererDispatcher.staticPlayerX = var11;
	    TileEntityRendererDispatcher.staticPlayerY = var13;
	    TileEntityRendererDispatcher.staticPlayerZ = var15;
	    this.renderManager.setRenderPosition(var11, var13, var15);
	    this.mc.entityRenderer.enableLightmap();
	    this.theWorld.theProfiler.endStartSection("global");
	    List var17 = this.theWorld.getLoadedEntityList();
	    
	    if (pass == 0)
	    {
		this.countEntitiesTotal = var17.size();
	    }
	    
	    if (Config.isFogOff() && this.mc.entityRenderer.fogStandard)
	    {
		GlStateManager.disableFog();
	    }
	    
	    boolean forgeEntityPass = Reflector.ForgeEntity_shouldRenderInPass.exists();
	    boolean forgeTileEntityPass = Reflector.ForgeTileEntity_shouldRenderInPass.exists();
	    int var18;
	    Entity var19;
	    
	    for (var18 = 0; var18 < this.theWorld.weatherEffects.size(); ++var18)
	    {
		var19 = (Entity) this.theWorld.weatherEffects.get(var18);
		
		if (!forgeEntityPass || Reflector.callBoolean(var19, Reflector.ForgeEntity_shouldRenderInPass, new Object[] { Integer.valueOf(pass) }))
		{
		    ++this.countEntitiesRendered;
		    
		    if (var19.isInRangeToRender3d(var4, var6, var8))
		    {
			this.renderManager.renderEntitySimple(var19, partialTicks);
		    }
		}
	    }
	    
	    if (this.isRenderEntityOutlines())
	    {
		GlStateManager.depthFunc(519);
		GlStateManager.disableFog();
		this.entityOutlineFramebuffer.framebufferClear();
		this.entityOutlineFramebuffer.bindFramebuffer(false);
		this.theWorld.theProfiler.endStartSection("entityOutlines");
		RenderHelper.disableStandardItemLighting();
		this.renderManager.setRenderOutlines(true);
		
		for (var18 = 0; var18 < var17.size(); ++var18)
		{
		    var19 = (Entity) var17.get(var18);
		    
		    if (!forgeEntityPass || Reflector.callBoolean(var19, Reflector.ForgeEntity_shouldRenderInPass, new Object[] { Integer.valueOf(pass) }))
		    {
			boolean var25 = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase) this.mc.getRenderViewEntity()).isPlayerSleeping();
			boolean var26 = var19.isInRangeToRender3d(var4, var6, var8) && (var19.ignoreFrustumCheck || camera.isBoundingBoxInFrustum(var19.getEntityBoundingBox()) || var19.riddenByEntity == this.mc.thePlayer) && var19 instanceof EntityPlayer;
			
			if ((var19 != this.mc.getRenderViewEntity() || this.mc.gameSettings.thirdPersonView != 0 || var25) && var26)
			{
			    this.renderManager.renderEntitySimple(var19, partialTicks);
			}
		    }
		}
		
		this.renderManager.setRenderOutlines(false);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.depthMask(false);
		this.entityOutlineShader.loadShaderGroup(partialTicks);
		GlStateManager.depthMask(true);
		this.mc.getFramebuffer().bindFramebuffer(false);
		GlStateManager.enableFog();
		GlStateManager.depthFunc(515);
		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
	    }
	    
	    this.theWorld.theProfiler.endStartSection("entities");
	    Iterator var341 = this.renderInfosEntities.iterator();
	    boolean oldFancyGraphics = this.mc.gameSettings.fancyGraphics;
	    this.mc.gameSettings.fancyGraphics = Config.isDroppedItemsFancy();
	    RenderGlobal.ContainerLocalRenderInformation var35;
	    
	    while (var341.hasNext())
	    {
		var35 = (RenderGlobal.ContainerLocalRenderInformation) var341.next();
		Chunk fontRenderer = this.theWorld.getChunkFromBlockCoords(var35.renderChunk.getPosition());
		Iterator var32 = fontRenderer.getEntityLists()[var35.renderChunk.getPosition().getY() / 16].iterator();
		
		while (var32.hasNext())
		{
		    Entity var27 = (Entity) var32.next();
		    
		    if (!forgeEntityPass || Reflector.callBoolean(var27, Reflector.ForgeEntity_shouldRenderInPass, new Object[] { Integer.valueOf(pass) }))
		    {
			boolean var30 = this.renderManager.shouldRender(var27, camera, var4, var6, var8) || var27.riddenByEntity == this.mc.thePlayer;
			
			if (var30)
			{
			    boolean var34 = this.mc.getRenderViewEntity() instanceof EntityLivingBase ? ((EntityLivingBase) this.mc.getRenderViewEntity()).isPlayerSleeping() : false;
			    
			    if (var27 == this.mc.getRenderViewEntity() && this.mc.gameSettings.thirdPersonView == 0 && !var34 || var27.posY >= 0.0D && var27.posY < 256.0D && !this.theWorld.isBlockLoaded(new BlockPos(var27)))
			    {
				continue;
			    }
			    
			    ++this.countEntitiesRendered;
			    
			    if (var27.getClass() == EntityItemFrame.class)
			    {
				var27.renderDistanceWeight = 0.06D;
			    }
			    
			    this.renderedEntity = var27;
			    this.renderManager.renderEntitySimple(var27, partialTicks);
			    this.renderedEntity = null;
			}
			
			if (!var30 && var27 instanceof EntityWitherSkull)
			{
			    this.mc.getRenderManager().renderWitherSkull(var27, partialTicks);
			}
		    }
		}
	    }
	    
	    this.mc.gameSettings.fancyGraphics = oldFancyGraphics;
	    FontRenderer var36 = TileEntityRendererDispatcher.instance.getFontRenderer();
	    this.theWorld.theProfiler.endStartSection("blockentities");
	    RenderHelper.enableStandardItemLighting();
	    var341 = this.renderInfosTileEntities.iterator();
	    TileEntity var37;
	    
	    while (var341.hasNext())
	    {
		var35 = (RenderGlobal.ContainerLocalRenderInformation) var341.next();
		Iterator var38 = var35.renderChunk.getCompiledChunk().getTileEntities().iterator();
		
		while (var38.hasNext())
		{
		    var37 = (TileEntity) var38.next();
		    
		    if (forgeTileEntityPass)
		    {
			if (!Reflector.callBoolean(var37, Reflector.ForgeTileEntity_shouldRenderInPass, new Object[] { Integer.valueOf(pass) }))
			{
			    continue;
			}
			AxisAlignedBB var40 = (AxisAlignedBB) Reflector.call(var37, Reflector.ForgeTileEntity_getRenderBoundingBox, new Object[0]);
			
			if (var40 != null && !camera.isBoundingBoxInFrustum(var40))
			{
			    continue;
			}
		    }
		    
		    Class var42 = var37.getClass();
		    
		    if (var42 == TileEntitySign.class && !Config.zoomMode)
		    {
			EntityPlayerSP shouldRender = this.mc.thePlayer;
			double tileEntity = var37.getDistanceSq(shouldRender.posX, shouldRender.posY, shouldRender.posZ);
			
			if (tileEntity > 256.0D)
			{
			    var36.enabled = false;
			}
		    }
		    
		    TileEntityRendererDispatcher.instance.renderTileEntity(var37, partialTicks, -1);
		    var36.enabled = true;
		}
	    }
	    
	    this.preRenderDamagedBlocks();
	    var341 = this.damagedBlocks.values().iterator();
	    
	    while (var341.hasNext())
	    {
		DestroyBlockProgress var39 = (DestroyBlockProgress) var341.next();
		BlockPos var41 = var39.getPosition();
		var37 = this.theWorld.getTileEntity(var41);
		
		if (var37 instanceof TileEntityChest)
		{
		    TileEntityChest var43 = (TileEntityChest) var37;
		    
		    if (var43.adjacentChestXNeg != null)
		    {
			var41 = var41.offset(EnumFacing.WEST);
			var37 = this.theWorld.getTileEntity(var41);
		    }
		    else if (var43.adjacentChestZNeg != null)
		    {
			var41 = var41.offset(EnumFacing.NORTH);
			var37 = this.theWorld.getTileEntity(var41);
		    }
		}
		
		Block var44 = this.theWorld.getBlockState(var41).getBlock();
		boolean var45;
		
		if (forgeTileEntityPass)
		{
		    var45 = false;
		    
		    if (var37 != null && Reflector.callBoolean(var37, Reflector.ForgeTileEntity_shouldRenderInPass, new Object[] { Integer.valueOf(pass) }) && Reflector.callBoolean(var37, Reflector.ForgeTileEntity_canRenderBreaking, new Object[0]))
		    {
			AxisAlignedBB aabb = (AxisAlignedBB) Reflector.call(var37, Reflector.ForgeTileEntity_getRenderBoundingBox, new Object[0]);
			
			if (aabb != null)
			{
			    var45 = camera.isBoundingBoxInFrustum(aabb);
			}
		    }
		}
		else
		{
		    var45 = var37 != null && (var44 instanceof BlockChest || var44 instanceof BlockEnderChest || var44 instanceof BlockSign || var44 instanceof BlockSkull);
		}
		
		if (var45)
		{
		    TileEntityRendererDispatcher.instance.renderTileEntity(var37, partialTicks, var39.getPartialBlockDamage());
		}
	    }
	    
	    this.postRenderDamagedBlocks();
	    this.mc.entityRenderer.disableLightmap();
	    this.mc.mcProfiler.endSection();
	}
    }
    
    /**
     * Gets the render info for use on the Debug screen
     */
    public String getDebugInfoRenders()
    {
	int var1 = this.viewFrustum.renderChunks.length;
	int var2 = 0;
	Iterator var3 = this.renderInfos.iterator();
	
	while (var3.hasNext())
	{
	    RenderGlobal.ContainerLocalRenderInformation var4 = (RenderGlobal.ContainerLocalRenderInformation) var3.next();
	    CompiledChunk var5 = var4.renderChunk.compiledChunk;
	    
	    if (var5 != CompiledChunk.DUMMY && !var5.isEmpty())
	    {
		++var2;
	    }
	}
	
	return String.format("C: %d/%d %sD: %d, %s", new Object[] { Integer.valueOf(var2), Integer.valueOf(var1), this.mc.renderChunksMany ? "(s) " : "", Integer.valueOf(this.renderDistanceChunks), this.renderDispatcher.getDebugInfo() });
    }
    
    /**
     * Gets the entities info for use on the Debug screen
     */
    public String getDebugInfoEntities()
    {
	return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ", B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered) + ", " + Config.getVersion();
    }
    
    public void setupTerrain(Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator)
    {
	if (this.mc.gameSettings.renderDistanceChunks != this.renderDistanceChunks)
	{
	    this.loadRenderers();
	}
	
	this.theWorld.theProfiler.startSection("camera");
	double var7 = viewEntity.posX - this.frustumUpdatePosX;
	double var9 = viewEntity.posY - this.frustumUpdatePosY;
	double var11 = viewEntity.posZ - this.frustumUpdatePosZ;
	
	if (this.frustumUpdatePosChunkX != viewEntity.chunkCoordX || this.frustumUpdatePosChunkY != viewEntity.chunkCoordY || this.frustumUpdatePosChunkZ != viewEntity.chunkCoordZ || var7 * var7 + var9 * var9 + var11 * var11 > 16.0D)
	{
	    this.frustumUpdatePosX = viewEntity.posX;
	    this.frustumUpdatePosY = viewEntity.posY;
	    this.frustumUpdatePosZ = viewEntity.posZ;
	    this.frustumUpdatePosChunkX = viewEntity.chunkCoordX;
	    this.frustumUpdatePosChunkY = viewEntity.chunkCoordY;
	    this.frustumUpdatePosChunkZ = viewEntity.chunkCoordZ;
	    this.viewFrustum.updateChunkPositions(viewEntity.posX, viewEntity.posZ);
	}
	
	this.theWorld.theProfiler.endStartSection("renderlistcamera");
	double var13 = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * partialTicks;
	double var15 = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * partialTicks;
	double var17 = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * partialTicks;
	this.renderContainer.initialize(var13, var15, var17);
	this.theWorld.theProfiler.endStartSection("cull");
	
	if (this.debugFixedClippingHelper != null)
	{
	    Frustrum var35 = new Frustrum(this.debugFixedClippingHelper);
	    var35.setPosition(this.debugTerrainFrustumPosition.x, this.debugTerrainFrustumPosition.y, this.debugTerrainFrustumPosition.z);
	    camera = var35;
	}
	
	this.mc.mcProfiler.endStartSection("culling");
	BlockPos var351 = new BlockPos(var13, var15 + viewEntity.getEyeHeight(), var17);
	RenderChunk var20 = this.viewFrustum.getRenderChunk(var351);
	BlockPos var21 = new BlockPos(MathHelper.floor_double(var13) / 16 * 16, MathHelper.floor_double(var15) / 16 * 16, MathHelper.floor_double(var17) / 16 * 16);
	this.displayListEntitiesDirty = this.displayListEntitiesDirty || !this.chunksToUpdate.isEmpty() || viewEntity.posX != this.lastViewEntityX || viewEntity.posY != this.lastViewEntityY || viewEntity.posZ != this.lastViewEntityZ || viewEntity.rotationPitch != this.lastViewEntityPitch || viewEntity.rotationYaw != this.lastViewEntityYaw;
	this.lastViewEntityX = viewEntity.posX;
	this.lastViewEntityY = viewEntity.posY;
	this.lastViewEntityZ = viewEntity.posZ;
	this.lastViewEntityPitch = viewEntity.rotationPitch;
	this.lastViewEntityYaw = viewEntity.rotationYaw;
	boolean var22 = this.debugFixedClippingHelper != null;
	Lagometer.timerVisibility.start();
	RenderGlobal.ContainerLocalRenderInformation var39;
	RenderChunk var41;
	
	if (!var22 && this.displayListEntitiesDirty)
	{
	    this.displayListEntitiesDirty = false;
	    this.renderInfos.clear();
	    this.renderInfosEntities.clear();
	    this.renderInfosTileEntities.clear();
	    this.visibilityDeque.clear();
	    Deque var36 = this.visibilityDeque;
	    boolean var37 = this.mc.renderChunksMany;
	    int var30;
	    
	    if (var20 == null)
	    {
		int var46 = var351.getY() > 0 ? 248 : 8;
		
		for (var30 = -this.renderDistanceChunks; var30 <= this.renderDistanceChunks; ++var30)
		{
		    for (int var43 = -this.renderDistanceChunks; var43 <= this.renderDistanceChunks; ++var43)
		    {
			RenderChunk var45 = this.viewFrustum.getRenderChunk(new BlockPos((var30 << 4) + 8, var46, (var43 << 4) + 8));
			
			if (var45 != null && camera.isBoundingBoxInFrustum(var45.boundingBox))
			{
			    var45.setFrameIndex(frameCount);
			    var36.add(new RenderGlobal.ContainerLocalRenderInformation(var45, (EnumFacing) null, 0, (Object) null));
			}
		    }
		}
	    }
	    else
	    {
		boolean var38 = false;
		RenderGlobal.ContainerLocalRenderInformation var40 = new RenderGlobal.ContainerLocalRenderInformation(var20, (EnumFacing) null, 0, (Object) null);
		Set var411 = SET_ALL_FACINGS;
		
		if (!var411.isEmpty() && var411.size() == 1)
		{
		    Vector3f var431 = this.getViewVector(viewEntity, partialTicks);
		    EnumFacing var31 = EnumFacing.getFacingFromVector(var431.x, var431.y, var431.z).getOpposite();
		    var411.remove(var31);
		}
		
		if (var411.isEmpty())
		{
		    var38 = true;
		}
		
		if (var38 && !playerSpectator)
		{
		    this.renderInfos.add(var40);
		}
		else
		{
		    if (playerSpectator && this.theWorld.getBlockState(var351).getBlock().isOpaqueCube())
		    {
			var37 = false;
		    }
		    
		    var20.setFrameIndex(frameCount);
		    var36.add(var40);
		}
	    }
	    
	    EnumFacing[] var391 = EnumFacing.VALUES;
	    var30 = var391.length;
	    
	    while (!var36.isEmpty())
	    {
		var39 = (RenderGlobal.ContainerLocalRenderInformation) var36.poll();
		var41 = var39.renderChunk;
		EnumFacing var42 = var39.facing;
		BlockPos var44 = var41.getPosition();
		
		if (!var41.compiledChunk.isEmpty() || var41.isNeedsUpdate())
		{
		    this.renderInfos.add(var39);
		}
		
		if (ChunkUtils.hasEntities(this.theWorld.getChunkFromBlockCoords(var44)))
		{
		    this.renderInfosEntities.add(var39);
		}
		
		if (var41.getCompiledChunk().getTileEntities().size() > 0)
		{
		    this.renderInfosTileEntities.add(var39);
		}
		
		for (int var451 = 0; var451 < var30; ++var451)
		{
		    EnumFacing var32 = var391[var451];
		    
		    if ((!var37 || !var39.setFacing.contains(var32.getOpposite())) && (!var37 || var42 == null || var41.getCompiledChunk().isVisible(var42.getOpposite(), var32)))
		    {
			RenderChunk var33 = this.getRenderChunkOffset(var351, var41, var32);
			
			if (var33 != null && var33.setFrameIndex(frameCount) && camera.isBoundingBoxInFrustum(var33.boundingBox))
			{
			    RenderGlobal.ContainerLocalRenderInformation var34 = new RenderGlobal.ContainerLocalRenderInformation(var33, var32, var39.counter + 1, (Object) null);
			    var34.setFacing.addAll(var39.setFacing);
			    var34.setFacing.add(var32);
			    var36.add(var34);
			}
		    }
		}
	    }
	}
	
	if (this.debugFixTerrainFrustum)
	{
	    this.fixTerrainFrustum(var13, var15, var17);
	    this.debugFixTerrainFrustum = false;
	}
	
	Lagometer.timerVisibility.end();
	this.renderDispatcher.clearChunkUpdates();
	Set var361 = this.chunksToUpdate;
	this.chunksToUpdate = Sets.newLinkedHashSet();
	Iterator var371 = this.renderInfos.iterator();
	Lagometer.timerChunkUpdate.start();
	
	while (var371.hasNext())
	{
	    var39 = (RenderGlobal.ContainerLocalRenderInformation) var371.next();
	    var41 = var39.renderChunk;
	    
	    if (var41.isNeedsUpdate() || var361.contains(var41))
	    {
		this.displayListEntitiesDirty = true;
		
		if (this.isPositionInRenderChunk(var21, var39.renderChunk))
		{
		    if (!Config.isActing())
		    {
			this.chunksToUpdateForced.add(var41);
		    }
		    else
		    {
			this.mc.mcProfiler.startSection("build near");
			this.renderDispatcher.updateChunkNow(var41);
			var41.setNeedsUpdate(false);
			this.mc.mcProfiler.endSection();
		    }
		}
		else
		{
		    this.chunksToUpdate.add(var41);
		}
	    }
	}
	
	Lagometer.timerChunkUpdate.end();
	this.chunksToUpdate.addAll(var361);
	this.mc.mcProfiler.endSection();
    }
    
    private boolean isPositionInRenderChunk(BlockPos pos, RenderChunk renderChunkIn)
    {
	BlockPos var3 = renderChunkIn.getPosition();
	return MathHelper.abs_int(pos.getX() - var3.getX()) > 16 ? false : (MathHelper.abs_int(pos.getY() - var3.getY()) > 16 ? false : MathHelper.abs_int(pos.getZ() - var3.getZ()) <= 16);
    }
    
    private RenderChunk getRenderChunkOffset(BlockPos posEye, RenderChunk renderChunk, EnumFacing side)
    {
	BlockPos var4 = renderChunk.getPositionOffset16(side);
	
	if (var4.getY() >= 0 && var4.getY() < 256)
	{
	    int dx = MathHelper.abs_int(posEye.getX() - var4.getX());
	    int dz = MathHelper.abs_int(posEye.getZ() - var4.getZ());
	    
	    if (Config.isFogOff())
	    {
		if (dx > this.renderDistance || dz > this.renderDistance)
		{
		    return null;
		}
	    }
	    else
	    {
		int distSq = dx * dx + dz * dz;
		
		if (distSq > this.renderDistanceSq)
		{
		    return null;
		}
	    }
	    
	    return this.viewFrustum.getRenderChunk(var4);
	}
	else
	{
	    return null;
	}
    }
    
    private void fixTerrainFrustum(double x, double y, double z)
    {
	this.debugFixedClippingHelper = new ClippingHelperImpl();
	((ClippingHelperImpl) this.debugFixedClippingHelper).init();
	Matrix4f var7 = new Matrix4f(this.debugFixedClippingHelper.modelviewMatrix);
	var7.transpose();
	Matrix4f var8 = new Matrix4f(this.debugFixedClippingHelper.projectionMatrix);
	var8.transpose();
	Matrix4f var9 = new Matrix4f();
	var9.mul(var8, var7);
	var9.invert();
	this.debugTerrainFrustumPosition.x = x;
	this.debugTerrainFrustumPosition.y = y;
	this.debugTerrainFrustumPosition.z = z;
	this.debugTerrainMatrix[0] = new Vector4f(-1.0F, -1.0F, -1.0F, 1.0F);
	this.debugTerrainMatrix[1] = new Vector4f(1.0F, -1.0F, -1.0F, 1.0F);
	this.debugTerrainMatrix[2] = new Vector4f(1.0F, 1.0F, -1.0F, 1.0F);
	this.debugTerrainMatrix[3] = new Vector4f(-1.0F, 1.0F, -1.0F, 1.0F);
	this.debugTerrainMatrix[4] = new Vector4f(-1.0F, -1.0F, 1.0F, 1.0F);
	this.debugTerrainMatrix[5] = new Vector4f(1.0F, -1.0F, 1.0F, 1.0F);
	this.debugTerrainMatrix[6] = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
	this.debugTerrainMatrix[7] = new Vector4f(-1.0F, 1.0F, 1.0F, 1.0F);
	
	for (int var10 = 0; var10 < 8; ++var10)
	{
	    var9.transform(this.debugTerrainMatrix[var10]);
	    this.debugTerrainMatrix[var10].x /= this.debugTerrainMatrix[var10].w;
	    this.debugTerrainMatrix[var10].y /= this.debugTerrainMatrix[var10].w;
	    this.debugTerrainMatrix[var10].z /= this.debugTerrainMatrix[var10].w;
	    this.debugTerrainMatrix[var10].w = 1.0F;
	}
    }
    
    protected Vector3f getViewVector(Entity entityIn, double partialTicks)
    {
	float var4 = (float) (entityIn.prevRotationPitch + (entityIn.rotationPitch - entityIn.prevRotationPitch) * partialTicks);
	float var5 = (float) (entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks);
	
	if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2)
	{
	    var4 += 180.0F;
	}
	
	float var6 = MathHelper.cos(-var5 * 0.017453292F - (float) Math.PI);
	float var7 = MathHelper.sin(-var5 * 0.017453292F - (float) Math.PI);
	float var8 = -MathHelper.cos(-var4 * 0.017453292F);
	float var9 = MathHelper.sin(-var4 * 0.017453292F);
	return new Vector3f(var7 * var8, var9, var6 * var8);
    }
    
    public int renderBlockLayer(EnumWorldBlockLayer blockLayerIn, double partialTicks, int pass, Entity entityIn)
    {
	RenderHelper.disableStandardItemLighting();
	
	if (blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT)
	{
	    this.mc.mcProfiler.startSection("translucent_sort");
	    double var15 = entityIn.posX - this.prevRenderSortX;
	    double var16 = entityIn.posY - this.prevRenderSortY;
	    double var17 = entityIn.posZ - this.prevRenderSortZ;
	    
	    if (var15 * var15 + var16 * var16 + var17 * var17 > 1.0D)
	    {
		this.prevRenderSortX = entityIn.posX;
		this.prevRenderSortY = entityIn.posY;
		this.prevRenderSortZ = entityIn.posZ;
		int var18 = 0;
		Iterator var13 = this.renderInfos.iterator();
		this.chunksToResortTransparency.clear();
		
		while (var13.hasNext())
		{
		    RenderGlobal.ContainerLocalRenderInformation var14 = (RenderGlobal.ContainerLocalRenderInformation) var13.next();
		    
		    if (var14.renderChunk.compiledChunk.isLayerStarted(blockLayerIn) && var18++ < 15)
		    {
			this.chunksToResortTransparency.add(var14.renderChunk);
		    }
		}
	    }
	    
	    this.mc.mcProfiler.endSection();
	}
	
	this.mc.mcProfiler.startSection("filterempty");
	int var151 = 0;
	boolean var7 = blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT;
	int var161 = var7 ? this.renderInfos.size() - 1 : 0;
	int var9 = var7 ? -1 : this.renderInfos.size();
	int var171 = var7 ? -1 : 1;
	
	for (int var11 = var161; var11 != var9; var11 += var171)
	{
	    RenderChunk var181 = ((RenderGlobal.ContainerLocalRenderInformation) this.renderInfos.get(var11)).renderChunk;
	    
	    if (!var181.getCompiledChunk().isLayerEmpty(blockLayerIn))
	    {
		++var151;
		this.renderContainer.addRenderChunk(var181, blockLayerIn);
	    }
	}
	
	if (Config.isFogOff() && this.mc.entityRenderer.fogStandard)
	{
	    GlStateManager.disableFog();
	}
	
	this.mc.mcProfiler.endStartSection("render_" + blockLayerIn);
	this.renderBlockLayer(blockLayerIn);
	this.mc.mcProfiler.endSection();
	return var151;
    }
    
    private void renderBlockLayer(EnumWorldBlockLayer blockLayerIn)
    {
	this.mc.entityRenderer.enableLightmap();
	
	if (OpenGlHelper.useVbo())
	{
	    GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
	    OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
	    GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	    OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
	    GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	    OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
	    GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
	}
	
	this.renderContainer.renderChunkLayer(blockLayerIn);
	
	if (OpenGlHelper.useVbo())
	{
	    List var2 = DefaultVertexFormats.BLOCK.getElements();
	    Iterator var3 = var2.iterator();
	    
	    while (var3.hasNext())
	    {
		VertexFormatElement var4 = (VertexFormatElement) var3.next();
		VertexFormatElement.EnumUseage var5 = var4.getUsage();
		int var6 = var4.getIndex();
		
		switch (RenderGlobal.SwitchEnumUseage.VALUES[var5.ordinal()])
		{
		case 1:
		    GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		    break;
		    
		case 2:
		    OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + var6);
		    GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		    OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
		    break;
		    
		case 3:
		    GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		    GlStateManager.resetColor();
		}
	    }
	}
	
	this.mc.entityRenderer.disableLightmap();
    }
    
    private void cleanupDamagedBlocks(Iterator iteratorIn)
    {
	while (iteratorIn.hasNext())
	{
	    DestroyBlockProgress var2 = (DestroyBlockProgress) iteratorIn.next();
	    int var3 = var2.getCreationCloudUpdateTick();
	    
	    if (this.cloudTickCounter - var3 > 400)
	    {
		iteratorIn.remove();
	    }
	}
    }
    
    public void updateClouds()
    {
	++this.cloudTickCounter;
	
	if (this.cloudTickCounter % 20 == 0)
	{
	    this.cleanupDamagedBlocks(this.damagedBlocks.values().iterator());
	}
    }
    
    private void renderSkyEnd()
    {
	if (Config.isSkyEnabled())
	{
	    GlStateManager.disableFog();
	    GlStateManager.disableAlpha();
	    GlStateManager.enableBlend();
	    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	    RenderHelper.disableStandardItemLighting();
	    GlStateManager.depthMask(false);
	    this.renderEngine.bindTexture(locationEndSkyPng);
	    Tessellator var1 = Tessellator.getInstance();
	    WorldRenderer var2 = var1.getWorldRenderer();
	    
	    for (int var3 = 0; var3 < 6; ++var3)
	    {
		GlStateManager.pushMatrix();
		
		if (var3 == 1)
		{
		    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
		}
		
		if (var3 == 2)
		{
		    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
		}
		
		if (var3 == 3)
		{
		    GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		}
		
		if (var3 == 4)
		{
		    GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
		}
		
		if (var3 == 5)
		{
		    GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
		}
		
		var2.startDrawingQuads();
		var2.setColorOpaque_I(2631720);
		var2.addVertexWithUV(-100.0D, -100.0D, -100.0D, 0.0D, 0.0D);
		var2.addVertexWithUV(-100.0D, -100.0D, 100.0D, 0.0D, 16.0D);
		var2.addVertexWithUV(100.0D, -100.0D, 100.0D, 16.0D, 16.0D);
		var2.addVertexWithUV(100.0D, -100.0D, -100.0D, 16.0D, 0.0D);
		var1.draw();
		GlStateManager.popMatrix();
	    }
	    
	    GlStateManager.depthMask(true);
	    GlStateManager.enableTexture2D();
	    GlStateManager.enableAlpha();
	}
    }
    
    public void renderSky(float partialTicks, int pass)
    {
	if (Reflector.ForgeWorldProvider_getSkyRenderer.exists())
	{
	    WorldProvider var3 = this.mc.theWorld.provider;
	    Object var4 = Reflector.call(var3, Reflector.ForgeWorldProvider_getSkyRenderer, new Object[0]);
	    
	    if (var4 != null)
	    {
		Reflector.callVoid(var4, Reflector.IRenderHandler_render, new Object[] { Float.valueOf(partialTicks), this.theWorld, this.mc });
		return;
	    }
	}
	
	if (this.mc.theWorld.provider.getDimensionId() == 1)
	{
	    this.renderSkyEnd();
	}
	else if (this.mc.theWorld.provider.isSurfaceWorld())
	{
	    GlStateManager.disableTexture2D();
	    Vec3 var221 = this.theWorld.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
	    var221 = CustomColorizer.getSkyColor(var221, this.mc.theWorld, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0D, this.mc.getRenderViewEntity().posZ);
	    float var231 = (float) var221.xCoord;
	    float var5 = (float) var221.yCoord;
	    float var6 = (float) var221.zCoord;
	    
	    if (pass != 2)
	    {
		float var23 = (var231 * 30.0F + var5 * 59.0F + var6 * 11.0F) / 100.0F;
		float var24 = (var231 * 30.0F + var5 * 70.0F) / 100.0F;
		float var25 = (var231 * 30.0F + var6 * 70.0F) / 100.0F;
		var231 = var23;
		var5 = var24;
		var6 = var25;
	    }
	    
	    GlStateManager.color(var231, var5, var6);
	    Tessellator var241 = Tessellator.getInstance();
	    WorldRenderer var251 = var241.getWorldRenderer();
	    GlStateManager.depthMask(false);
	    GlStateManager.enableFog();
	    GlStateManager.color(var231, var5, var6);
	    
	    if (Config.isSkyEnabled())
	    {
		if (this.vboEnabled)
		{
		    this.skyVBO.bindBuffer();
		    GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		    GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
		    this.skyVBO.drawArrays(7);
		    this.skyVBO.unbindBuffer();
		    GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		}
		else
		{
		    GlStateManager.callList(this.glSkyList);
		}
	    }
	    
	    GlStateManager.disableFog();
	    GlStateManager.disableAlpha();
	    GlStateManager.enableBlend();
	    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	    RenderHelper.disableStandardItemLighting();
	    float[] var261 = this.theWorld.provider.calcSunriseSunsetColors(this.theWorld.getCelestialAngle(partialTicks), partialTicks);
	    float var10;
	    float var11;
	    float var12;
	    float var13;
	    float var14;
	    float var22;
	    int var31;
	    float var18;
	    float var19;
	    
	    if (var261 != null && Config.isSunMoonEnabled())
	    {
		GlStateManager.disableTexture2D();
		GlStateManager.shadeModel(7425);
		GlStateManager.pushMatrix();
		GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(MathHelper.sin(this.theWorld.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
		var10 = var261[0];
		var11 = var261[1];
		var12 = var261[2];
		
		if (pass != 2)
		{
		    var13 = (var10 * 30.0F + var11 * 59.0F + var12 * 11.0F) / 100.0F;
		    var14 = (var10 * 30.0F + var11 * 70.0F) / 100.0F;
		    var22 = (var10 * 30.0F + var12 * 70.0F) / 100.0F;
		    var10 = var13;
		    var11 = var14;
		    var12 = var22;
		}
		
		var251.startDrawing(6);
		var251.setColorRGBA_F(var10, var11, var12, var261[3]);
		var251.addVertex(0.0D, 100.0D, 0.0D);
		var251.setColorRGBA_F(var261[0], var261[1], var261[2], 0.0F);
		
		for (var31 = 0; var31 <= 16; ++var31)
		{
		    var22 = var31 * (float) Math.PI * 2.0F / 16.0F;
		    var18 = MathHelper.sin(var22);
		    var19 = MathHelper.cos(var22);
		    var251.addVertex(var18 * 120.0F, var19 * 120.0F, -var19 * 40.0F * var261[3]);
		}
		
		var241.draw();
		GlStateManager.popMatrix();
		GlStateManager.shadeModel(7424);
	    }
	    
	    GlStateManager.enableTexture2D();
	    GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
	    GlStateManager.pushMatrix();
	    var10 = 1.0F - this.theWorld.getRainStrength(partialTicks);
	    var11 = 0.0F;
	    var12 = 0.0F;
	    var13 = 0.0F;
	    GlStateManager.color(1.0F, 1.0F, 1.0F, var10);
	    GlStateManager.translate(0.0F, 0.0F, 0.0F);
	    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
	    CustomSky.renderSky(this.theWorld, this.renderEngine, this.theWorld.getCelestialAngle(partialTicks), var10);
	    GlStateManager.rotate(this.theWorld.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
	    
	    if (Config.isSunMoonEnabled())
	    {
		var14 = 30.0F;
		this.renderEngine.bindTexture(locationSunPng);
		var251.startDrawingQuads();
		var251.addVertexWithUV((-var14), 100.0D, (-var14), 0.0D, 0.0D);
		var251.addVertexWithUV(var14, 100.0D, (-var14), 1.0D, 0.0D);
		var251.addVertexWithUV(var14, 100.0D, var14, 1.0D, 1.0D);
		var251.addVertexWithUV((-var14), 100.0D, var14, 0.0D, 1.0D);
		var241.draw();
		var14 = 20.0F;
		this.renderEngine.bindTexture(locationMoonPhasesPng);
		int var27 = this.theWorld.getMoonPhase();
		int var28 = var27 % 4;
		var31 = var27 / 4 % 2;
		var18 = (var28 + 0) / 4.0F;
		var19 = (var31 + 0) / 2.0F;
		float var20 = (var28 + 1) / 4.0F;
		float var21 = (var31 + 1) / 2.0F;
		var251.startDrawingQuads();
		var251.addVertexWithUV((-var14), -100.0D, var14, var20, var21);
		var251.addVertexWithUV(var14, -100.0D, var14, var18, var21);
		var251.addVertexWithUV(var14, -100.0D, (-var14), var18, var19);
		var251.addVertexWithUV((-var14), -100.0D, (-var14), var20, var19);
		var241.draw();
	    }
	    
	    GlStateManager.disableTexture2D();
	    var22 = this.theWorld.getStarBrightness(partialTicks) * var10;
	    
	    if (var22 > 0.0F && Config.isStarsEnabled() && !CustomSky.hasSkyLayers(this.theWorld))
	    {
		GlStateManager.color(var22, var22, var22, var22);
		
		if (this.vboEnabled)
		{
		    this.starVBO.bindBuffer();
		    GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		    GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
		    this.starVBO.drawArrays(7);
		    this.starVBO.unbindBuffer();
		    GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		}
		else
		{
		    GlStateManager.callList(this.starGLCallList);
		}
	    }
	    
	    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	    GlStateManager.disableBlend();
	    GlStateManager.enableAlpha();
	    GlStateManager.enableFog();
	    GlStateManager.popMatrix();
	    GlStateManager.disableTexture2D();
	    GlStateManager.color(0.0F, 0.0F, 0.0F);
	    double var29 = this.mc.thePlayer.getPositionEyes(partialTicks).yCoord - this.theWorld.getHorizon();
	    
	    if (var29 < 0.0D)
	    {
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, 12.0F, 0.0F);
		
		if (this.vboEnabled)
		{
		    this.sky2VBO.bindBuffer();
		    GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		    GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
		    this.sky2VBO.drawArrays(7);
		    this.sky2VBO.unbindBuffer();
		    GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		}
		else
		{
		    GlStateManager.callList(this.glSkyList2);
		}
		
		GlStateManager.popMatrix();
		var12 = 1.0F;
		var13 = -((float) (var29 + 65.0D));
		var14 = -1.0F;
		var251.startDrawingQuads();
		var251.setColorRGBA_I(0, 255);
		var251.addVertex(-1.0D, var13, 1.0D);
		var251.addVertex(1.0D, var13, 1.0D);
		var251.addVertex(1.0D, -1.0D, 1.0D);
		var251.addVertex(-1.0D, -1.0D, 1.0D);
		var251.addVertex(-1.0D, -1.0D, -1.0D);
		var251.addVertex(1.0D, -1.0D, -1.0D);
		var251.addVertex(1.0D, var13, -1.0D);
		var251.addVertex(-1.0D, var13, -1.0D);
		var251.addVertex(1.0D, -1.0D, -1.0D);
		var251.addVertex(1.0D, -1.0D, 1.0D);
		var251.addVertex(1.0D, var13, 1.0D);
		var251.addVertex(1.0D, var13, -1.0D);
		var251.addVertex(-1.0D, var13, -1.0D);
		var251.addVertex(-1.0D, var13, 1.0D);
		var251.addVertex(-1.0D, -1.0D, 1.0D);
		var251.addVertex(-1.0D, -1.0D, -1.0D);
		var251.addVertex(-1.0D, -1.0D, -1.0D);
		var251.addVertex(-1.0D, -1.0D, 1.0D);
		var251.addVertex(1.0D, -1.0D, 1.0D);
		var251.addVertex(1.0D, -1.0D, -1.0D);
		var241.draw();
	    }
	    
	    if (this.theWorld.provider.isSkyColored())
	    {
		GlStateManager.color(var231 * 0.2F + 0.04F, var5 * 0.2F + 0.04F, var6 * 0.6F + 0.1F);
	    }
	    else
	    {
		GlStateManager.color(var231, var5, var6);
	    }
	    
	    if (this.mc.gameSettings.renderDistanceChunks <= 4)
	    {
		GlStateManager.color(this.mc.entityRenderer.fogColorRed, this.mc.entityRenderer.fogColorGreen, this.mc.entityRenderer.fogColorBlue);
	    }
	    
	    GlStateManager.pushMatrix();
	    GlStateManager.translate(0.0F, -((float) (var29 - 16.0D)), 0.0F);
	    
	    if (Config.isSkyEnabled())
	    {
		GlStateManager.callList(this.glSkyList2);
	    }
	    
	    GlStateManager.popMatrix();
	    GlStateManager.enableTexture2D();
	    GlStateManager.depthMask(true);
	}
    }
    
    public void renderClouds(float partialTicks, int pass)
    {
	if (!Config.isCloudsOff())
	{
	    if (Reflector.ForgeWorldProvider_getCloudRenderer.exists())
	    {
		WorldProvider partialTicks1 = this.mc.theWorld.provider;
		Object var3 = Reflector.call(partialTicks1, Reflector.ForgeWorldProvider_getCloudRenderer, new Object[0]);
		
		if (var3 != null)
		{
		    Reflector.callVoid(var3, Reflector.IRenderHandler_render, new Object[] { Float.valueOf(partialTicks), this.theWorld, this.mc });
		    return;
		}
	    }
	    
	    if (this.mc.theWorld.provider.isSurfaceWorld())
	    {
		if (Config.isCloudsFancy())
		{
		    this.renderCloudsFancy(partialTicks, pass);
		}
		else
		{
		    this.cloudRenderer.prepareToRender(false, this.cloudTickCounter, partialTicks);
		    partialTicks = 0.0F;
		    GlStateManager.disableCull();
		    float var31 = (float) (this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().posY - this.mc.getRenderViewEntity().lastTickPosY) * partialTicks);
		    Tessellator var6 = Tessellator.getInstance();
		    WorldRenderer var7 = var6.getWorldRenderer();
		    this.renderEngine.bindTexture(locationCloudsPng);
		    GlStateManager.enableBlend();
		    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		    
		    if (this.cloudRenderer.shouldUpdateGlList())
		    {
			this.cloudRenderer.startUpdateGlList();
			Vec3 var8 = this.theWorld.getCloudColour(partialTicks);
			float var9 = (float) var8.xCoord;
			float var10 = (float) var8.yCoord;
			float var11 = (float) var8.zCoord;
			float var12;
			
			if (pass != 2)
			{
			    var12 = (var9 * 30.0F + var10 * 59.0F + var11 * 11.0F) / 100.0F;
			    float var26 = (var9 * 30.0F + var10 * 70.0F) / 100.0F;
			    float var14 = (var9 * 30.0F + var11 * 70.0F) / 100.0F;
			    var9 = var12;
			    var10 = var26;
			    var11 = var14;
			}
			
			var12 = 4.8828125E-4F;
			double var261 = this.cloudTickCounter + partialTicks;
			double var15 = this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().posX - this.mc.getRenderViewEntity().prevPosX) * partialTicks + var261 * 0.029999999329447746D;
			double var17 = this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().posZ - this.mc.getRenderViewEntity().prevPosZ) * partialTicks;
			int var19 = MathHelper.floor_double(var15 / 2048.0D);
			int var20 = MathHelper.floor_double(var17 / 2048.0D);
			var15 -= var19 * 2048;
			var17 -= var20 * 2048;
			float var21 = this.theWorld.provider.getCloudHeight() - var31 + 0.33F;
			var21 += this.mc.gameSettings.ofCloudsHeight * 128.0F;
			float var22 = (float) (var15 * 4.8828125E-4D);
			float var23 = (float) (var17 * 4.8828125E-4D);
			var7.startDrawingQuads();
			var7.setColorRGBA_F(var9, var10, var11, 0.8F);
			
			for (int var24 = -256; var24 < 256; var24 += 32)
			{
			    for (int var25 = -256; var25 < 256; var25 += 32)
			    {
				var7.addVertexWithUV(var24 + 0, var21, var25 + 32, (var24 + 0) * 4.8828125E-4F + var22, (var25 + 32) * 4.8828125E-4F + var23);
				var7.addVertexWithUV(var24 + 32, var21, var25 + 32, (var24 + 32) * 4.8828125E-4F + var22, (var25 + 32) * 4.8828125E-4F + var23);
				var7.addVertexWithUV(var24 + 32, var21, var25 + 0, (var24 + 32) * 4.8828125E-4F + var22, (var25 + 0) * 4.8828125E-4F + var23);
				var7.addVertexWithUV(var24 + 0, var21, var25 + 0, (var24 + 0) * 4.8828125E-4F + var22, (var25 + 0) * 4.8828125E-4F + var23);
			    }
			}
			
			var6.draw();
			this.cloudRenderer.endUpdateGlList();
		    }
		    
		    this.cloudRenderer.renderGlList();
		    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		    GlStateManager.disableBlend();
		    GlStateManager.enableCull();
		}
	    }
	}
    }
    
    /**
     * Checks if the given position is to be rendered with cloud fog
     */
    public boolean hasCloudFog(double x, double y, double z, float partialTicks)
    {
	return false;
    }
    
    private void renderCloudsFancy(float partialTicks, int pass)
    {
	this.cloudRenderer.prepareToRender(true, this.cloudTickCounter, partialTicks);
	partialTicks = 0.0F;
	GlStateManager.disableCull();
	float var3 = (float) (this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().posY - this.mc.getRenderViewEntity().lastTickPosY) * partialTicks);
	Tessellator var4 = Tessellator.getInstance();
	WorldRenderer var5 = var4.getWorldRenderer();
	double var8 = this.cloudTickCounter + partialTicks;
	double var10 = (this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().posX - this.mc.getRenderViewEntity().prevPosX) * partialTicks + var8 * 0.029999999329447746D) / 12.0D;
	double var12 = (this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().posZ - this.mc.getRenderViewEntity().prevPosZ) * partialTicks) / 12.0D + 0.33000001311302185D;
	float var14 = this.theWorld.provider.getCloudHeight() - var3 + 0.33F;
	var14 += this.mc.gameSettings.ofCloudsHeight * 128.0F;
	int var15 = MathHelper.floor_double(var10 / 2048.0D);
	int var16 = MathHelper.floor_double(var12 / 2048.0D);
	var10 -= var15 * 2048;
	var12 -= var16 * 2048;
	this.renderEngine.bindTexture(locationCloudsPng);
	GlStateManager.enableBlend();
	GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	Vec3 var17 = this.theWorld.getCloudColour(partialTicks);
	float var18 = (float) var17.xCoord;
	float var19 = (float) var17.yCoord;
	float var20 = (float) var17.zCoord;
	float var21;
	float var22;
	float var23;
	
	if (pass != 2)
	{
	    var21 = (var18 * 30.0F + var19 * 59.0F + var20 * 11.0F) / 100.0F;
	    var22 = (var18 * 30.0F + var19 * 70.0F) / 100.0F;
	    var23 = (var18 * 30.0F + var20 * 70.0F) / 100.0F;
	    var18 = var21;
	    var19 = var22;
	    var20 = var23;
	}
	
	var21 = 0.00390625F;
	var22 = MathHelper.floor_double(var10) * 0.00390625F;
	var23 = MathHelper.floor_double(var12) * 0.00390625F;
	float var24 = (float) (var10 - MathHelper.floor_double(var10));
	float var25 = (float) (var12 - MathHelper.floor_double(var12));
	GlStateManager.scale(12.0F, 1.0F, 12.0F);
	int var30;
	
	for (var30 = 0; var30 < 2; ++var30)
	{
	    if (var30 == 0)
	    {
		GlStateManager.colorMask(false, false, false, false);
	    }
	    else
	    {
		switch (pass)
		{
		case 0:
		    GlStateManager.colorMask(false, true, true, true);
		    break;
		    
		case 1:
		    GlStateManager.colorMask(true, false, false, true);
		    break;
		    
		case 2:
		    GlStateManager.colorMask(true, true, true, true);
		}
	    }
	    
	    this.cloudRenderer.renderGlList();
	}
	
	if (this.cloudRenderer.shouldUpdateGlList())
	{
	    this.cloudRenderer.startUpdateGlList();
	    
	    for (var30 = -3; var30 <= 4; ++var30)
	    {
		for (int var31 = -3; var31 <= 4; ++var31)
		{
		    var5.startDrawingQuads();
		    float var32 = var30 * 8;
		    float var33 = var31 * 8;
		    float var34 = var32 - var24;
		    float var35 = var33 - var25;
		    
		    if (var14 > -5.0F)
		    {
			var5.setColorRGBA_F(var18 * 0.7F, var19 * 0.7F, var20 * 0.7F, 0.8F);
			var5.setNormal(0.0F, -1.0F, 0.0F);
			var5.addVertexWithUV(var34 + 0.0F, var14 + 0.0F, var35 + 8.0F, (var32 + 0.0F) * 0.00390625F + var22, (var33 + 8.0F) * 0.00390625F + var23);
			var5.addVertexWithUV(var34 + 8.0F, var14 + 0.0F, var35 + 8.0F, (var32 + 8.0F) * 0.00390625F + var22, (var33 + 8.0F) * 0.00390625F + var23);
			var5.addVertexWithUV(var34 + 8.0F, var14 + 0.0F, var35 + 0.0F, (var32 + 8.0F) * 0.00390625F + var22, (var33 + 0.0F) * 0.00390625F + var23);
			var5.addVertexWithUV(var34 + 0.0F, var14 + 0.0F, var35 + 0.0F, (var32 + 0.0F) * 0.00390625F + var22, (var33 + 0.0F) * 0.00390625F + var23);
		    }
		    
		    if (var14 <= 5.0F)
		    {
			var5.setColorRGBA_F(var18, var19, var20, 0.8F);
			var5.setNormal(0.0F, 1.0F, 0.0F);
			var5.addVertexWithUV(var34 + 0.0F, var14 + 4.0F - 9.765625E-4F, var35 + 8.0F, (var32 + 0.0F) * 0.00390625F + var22, (var33 + 8.0F) * 0.00390625F + var23);
			var5.addVertexWithUV(var34 + 8.0F, var14 + 4.0F - 9.765625E-4F, var35 + 8.0F, (var32 + 8.0F) * 0.00390625F + var22, (var33 + 8.0F) * 0.00390625F + var23);
			var5.addVertexWithUV(var34 + 8.0F, var14 + 4.0F - 9.765625E-4F, var35 + 0.0F, (var32 + 8.0F) * 0.00390625F + var22, (var33 + 0.0F) * 0.00390625F + var23);
			var5.addVertexWithUV(var34 + 0.0F, var14 + 4.0F - 9.765625E-4F, var35 + 0.0F, (var32 + 0.0F) * 0.00390625F + var22, (var33 + 0.0F) * 0.00390625F + var23);
		    }
		    
		    var5.setColorRGBA_F(var18 * 0.9F, var19 * 0.9F, var20 * 0.9F, 0.8F);
		    int var36;
		    
		    if (var30 > -1)
		    {
			var5.setNormal(-1.0F, 0.0F, 0.0F);
			
			for (var36 = 0; var36 < 8; ++var36)
			{
			    var5.addVertexWithUV(var34 + var36 + 0.0F, var14 + 0.0F, var35 + 8.0F, (var32 + var36 + 0.5F) * 0.00390625F + var22, (var33 + 8.0F) * 0.00390625F + var23);
			    var5.addVertexWithUV(var34 + var36 + 0.0F, var14 + 4.0F, var35 + 8.0F, (var32 + var36 + 0.5F) * 0.00390625F + var22, (var33 + 8.0F) * 0.00390625F + var23);
			    var5.addVertexWithUV(var34 + var36 + 0.0F, var14 + 4.0F, var35 + 0.0F, (var32 + var36 + 0.5F) * 0.00390625F + var22, (var33 + 0.0F) * 0.00390625F + var23);
			    var5.addVertexWithUV(var34 + var36 + 0.0F, var14 + 0.0F, var35 + 0.0F, (var32 + var36 + 0.5F) * 0.00390625F + var22, (var33 + 0.0F) * 0.00390625F + var23);
			}
		    }
		    
		    if (var30 <= 1)
		    {
			var5.setNormal(1.0F, 0.0F, 0.0F);
			
			for (var36 = 0; var36 < 8; ++var36)
			{
			    var5.addVertexWithUV(var34 + var36 + 1.0F - 9.765625E-4F, var14 + 0.0F, var35 + 8.0F, (var32 + var36 + 0.5F) * 0.00390625F + var22, (var33 + 8.0F) * 0.00390625F + var23);
			    var5.addVertexWithUV(var34 + var36 + 1.0F - 9.765625E-4F, var14 + 4.0F, var35 + 8.0F, (var32 + var36 + 0.5F) * 0.00390625F + var22, (var33 + 8.0F) * 0.00390625F + var23);
			    var5.addVertexWithUV(var34 + var36 + 1.0F - 9.765625E-4F, var14 + 4.0F, var35 + 0.0F, (var32 + var36 + 0.5F) * 0.00390625F + var22, (var33 + 0.0F) * 0.00390625F + var23);
			    var5.addVertexWithUV(var34 + var36 + 1.0F - 9.765625E-4F, var14 + 0.0F, var35 + 0.0F, (var32 + var36 + 0.5F) * 0.00390625F + var22, (var33 + 0.0F) * 0.00390625F + var23);
			}
		    }
		    
		    var5.setColorRGBA_F(var18 * 0.8F, var19 * 0.8F, var20 * 0.8F, 0.8F);
		    
		    if (var31 > -1)
		    {
			var5.setNormal(0.0F, 0.0F, -1.0F);
			
			for (var36 = 0; var36 < 8; ++var36)
			{
			    var5.addVertexWithUV(var34 + 0.0F, var14 + 4.0F, var35 + var36 + 0.0F, (var32 + 0.0F) * 0.00390625F + var22, (var33 + var36 + 0.5F) * 0.00390625F + var23);
			    var5.addVertexWithUV(var34 + 8.0F, var14 + 4.0F, var35 + var36 + 0.0F, (var32 + 8.0F) * 0.00390625F + var22, (var33 + var36 + 0.5F) * 0.00390625F + var23);
			    var5.addVertexWithUV(var34 + 8.0F, var14 + 0.0F, var35 + var36 + 0.0F, (var32 + 8.0F) * 0.00390625F + var22, (var33 + var36 + 0.5F) * 0.00390625F + var23);
			    var5.addVertexWithUV(var34 + 0.0F, var14 + 0.0F, var35 + var36 + 0.0F, (var32 + 0.0F) * 0.00390625F + var22, (var33 + var36 + 0.5F) * 0.00390625F + var23);
			}
		    }
		    
		    if (var31 <= 1)
		    {
			var5.setNormal(0.0F, 0.0F, 1.0F);
			
			for (var36 = 0; var36 < 8; ++var36)
			{
			    var5.addVertexWithUV(var34 + 0.0F, var14 + 4.0F, var35 + var36 + 1.0F - 9.765625E-4F, (var32 + 0.0F) * 0.00390625F + var22, (var33 + var36 + 0.5F) * 0.00390625F + var23);
			    var5.addVertexWithUV(var34 + 8.0F, var14 + 4.0F, var35 + var36 + 1.0F - 9.765625E-4F, (var32 + 8.0F) * 0.00390625F + var22, (var33 + var36 + 0.5F) * 0.00390625F + var23);
			    var5.addVertexWithUV(var34 + 8.0F, var14 + 0.0F, var35 + var36 + 1.0F - 9.765625E-4F, (var32 + 8.0F) * 0.00390625F + var22, (var33 + var36 + 0.5F) * 0.00390625F + var23);
			    var5.addVertexWithUV(var34 + 0.0F, var14 + 0.0F, var35 + var36 + 1.0F - 9.765625E-4F, (var32 + 0.0F) * 0.00390625F + var22, (var33 + var36 + 0.5F) * 0.00390625F + var23);
			}
		    }
		    
		    var4.draw();
		}
	    }
	    
	    this.cloudRenderer.endUpdateGlList();
	}
	
	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	GlStateManager.disableBlend();
	GlStateManager.enableCull();
    }
    
    public void updateChunks(long finishTimeNano)
    {
	this.displayListEntitiesDirty |= this.renderDispatcher.runChunkUploads(finishTimeNano);
	Iterator countUpdated;
	RenderChunk updatesPerFrame;
	
	if (this.chunksToUpdateForced.size() > 0)
	{
	    countUpdated = this.chunksToUpdateForced.iterator();
	    
	    while (countUpdated.hasNext())
	    {
		updatesPerFrame = (RenderChunk) countUpdated.next();
		
		if (!this.renderDispatcher.updateChunkLater(updatesPerFrame))
		{
		    break;
		}
		
		updatesPerFrame.setNeedsUpdate(false);
		countUpdated.remove();
		this.chunksToUpdate.remove(updatesPerFrame);
		this.chunksToResortTransparency.remove(updatesPerFrame);
	    }
	}
	
	if (this.chunksToResortTransparency.size() > 0)
	{
	    countUpdated = this.chunksToResortTransparency.iterator();
	    
	    if (countUpdated.hasNext())
	    {
		updatesPerFrame = (RenderChunk) countUpdated.next();
		
		if (this.renderDispatcher.updateTransparencyLater(updatesPerFrame))
		{
		    countUpdated.remove();
		}
	    }
	}
	
	int var8 = 0;
	int var9 = Config.getUpdatesPerFrame();
	int maxUpdatesPerFrame = var9 * 2;
	Iterator var3 = this.chunksToUpdate.iterator();
	
	while (var3.hasNext())
	{
	    RenderChunk var4 = (RenderChunk) var3.next();
	    
	    if (!this.renderDispatcher.updateChunkLater(var4))
	    {
		break;
	    }
	    
	    var4.setNeedsUpdate(false);
	    var3.remove();
	    
	    if (var4.getCompiledChunk().isEmpty() && var9 < maxUpdatesPerFrame)
	    {
		++var9;
	    }
	    
	    ++var8;
	    
	    if (var8 >= var9)
	    {
		break;
	    }
	}
    }
    
    public void renderWorldBorder(Entity p_180449_1_, float partialTicks)
    {
	Tessellator var3 = Tessellator.getInstance();
	WorldRenderer var4 = var3.getWorldRenderer();
	WorldBorder var5 = this.theWorld.getWorldBorder();
	double var6 = this.mc.gameSettings.renderDistanceChunks * 16;
	
	if (p_180449_1_.posX >= var5.maxX() - var6 || p_180449_1_.posX <= var5.minX() + var6 || p_180449_1_.posZ >= var5.maxZ() - var6 || p_180449_1_.posZ <= var5.minZ() + var6)
	{
	    double var8 = 1.0D - var5.getClosestDistance(p_180449_1_) / var6;
	    var8 = Math.pow(var8, 4.0D);
	    double var10 = p_180449_1_.lastTickPosX + (p_180449_1_.posX - p_180449_1_.lastTickPosX) * partialTicks;
	    double var12 = p_180449_1_.lastTickPosY + (p_180449_1_.posY - p_180449_1_.lastTickPosY) * partialTicks;
	    double var14 = p_180449_1_.lastTickPosZ + (p_180449_1_.posZ - p_180449_1_.lastTickPosZ) * partialTicks;
	    GlStateManager.enableBlend();
	    GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
	    this.renderEngine.bindTexture(locationForcefieldPng);
	    GlStateManager.depthMask(false);
	    GlStateManager.pushMatrix();
	    int var16 = var5.getStatus().getID();
	    float var17 = (var16 >> 16 & 255) / 255.0F;
	    float var18 = (var16 >> 8 & 255) / 255.0F;
	    float var19 = (var16 & 255) / 255.0F;
	    GlStateManager.color(var17, var18, var19, (float) var8);
	    GlStateManager.doPolygonOffset(-3.0F, -3.0F);
	    GlStateManager.enablePolygonOffset();
	    GlStateManager.alphaFunc(516, 0.1F);
	    GlStateManager.enableAlpha();
	    GlStateManager.disableCull();
	    float var20 = Minecraft.getSystemTime() % 3000L / 3000.0F;
	    var4.startDrawingQuads();
	    var4.setTranslation(-var10, -var12, -var14);
	    var4.markDirty();
	    double var24 = Math.max(MathHelper.floor_double(var14 - var6), var5.minZ());
	    double var26 = Math.min(MathHelper.ceiling_double_int(var14 + var6), var5.maxZ());
	    float var28;
	    double var29;
	    double var31;
	    float var33;
	    
	    if (var10 > var5.maxX() - var6)
	    {
		var28 = 0.0F;
		
		for (var29 = var24; var29 < var26; var28 += 0.5F)
		{
		    var31 = Math.min(1.0D, var26 - var29);
		    var33 = (float) var31 * 0.5F;
		    var4.addVertexWithUV(var5.maxX(), 256.0D, var29, var20 + var28, var20 + 0.0F);
		    var4.addVertexWithUV(var5.maxX(), 256.0D, var29 + var31, var20 + var33 + var28, var20 + 0.0F);
		    var4.addVertexWithUV(var5.maxX(), 0.0D, var29 + var31, var20 + var33 + var28, var20 + 128.0F);
		    var4.addVertexWithUV(var5.maxX(), 0.0D, var29, var20 + var28, var20 + 128.0F);
		    ++var29;
		}
	    }
	    
	    if (var10 < var5.minX() + var6)
	    {
		var28 = 0.0F;
		
		for (var29 = var24; var29 < var26; var28 += 0.5F)
		{
		    var31 = Math.min(1.0D, var26 - var29);
		    var33 = (float) var31 * 0.5F;
		    var4.addVertexWithUV(var5.minX(), 256.0D, var29, var20 + var28, var20 + 0.0F);
		    var4.addVertexWithUV(var5.minX(), 256.0D, var29 + var31, var20 + var33 + var28, var20 + 0.0F);
		    var4.addVertexWithUV(var5.minX(), 0.0D, var29 + var31, var20 + var33 + var28, var20 + 128.0F);
		    var4.addVertexWithUV(var5.minX(), 0.0D, var29, var20 + var28, var20 + 128.0F);
		    ++var29;
		}
	    }
	    
	    var24 = Math.max(MathHelper.floor_double(var10 - var6), var5.minX());
	    var26 = Math.min(MathHelper.ceiling_double_int(var10 + var6), var5.maxX());
	    
	    if (var14 > var5.maxZ() - var6)
	    {
		var28 = 0.0F;
		
		for (var29 = var24; var29 < var26; var28 += 0.5F)
		{
		    var31 = Math.min(1.0D, var26 - var29);
		    var33 = (float) var31 * 0.5F;
		    var4.addVertexWithUV(var29, 256.0D, var5.maxZ(), var20 + var28, var20 + 0.0F);
		    var4.addVertexWithUV(var29 + var31, 256.0D, var5.maxZ(), var20 + var33 + var28, var20 + 0.0F);
		    var4.addVertexWithUV(var29 + var31, 0.0D, var5.maxZ(), var20 + var33 + var28, var20 + 128.0F);
		    var4.addVertexWithUV(var29, 0.0D, var5.maxZ(), var20 + var28, var20 + 128.0F);
		    ++var29;
		}
	    }
	    
	    if (var14 < var5.minZ() + var6)
	    {
		var28 = 0.0F;
		
		for (var29 = var24; var29 < var26; var28 += 0.5F)
		{
		    var31 = Math.min(1.0D, var26 - var29);
		    var33 = (float) var31 * 0.5F;
		    var4.addVertexWithUV(var29, 256.0D, var5.minZ(), var20 + var28, var20 + 0.0F);
		    var4.addVertexWithUV(var29 + var31, 256.0D, var5.minZ(), var20 + var33 + var28, var20 + 0.0F);
		    var4.addVertexWithUV(var29 + var31, 0.0D, var5.minZ(), var20 + var33 + var28, var20 + 128.0F);
		    var4.addVertexWithUV(var29, 0.0D, var5.minZ(), var20 + var28, var20 + 128.0F);
		    ++var29;
		}
	    }
	    
	    var3.draw();
	    var4.setTranslation(0.0D, 0.0D, 0.0D);
	    GlStateManager.enableCull();
	    GlStateManager.disableAlpha();
	    GlStateManager.doPolygonOffset(0.0F, 0.0F);
	    GlStateManager.disablePolygonOffset();
	    GlStateManager.enableAlpha();
	    GlStateManager.disableBlend();
	    GlStateManager.popMatrix();
	    GlStateManager.depthMask(true);
	}
    }
    
    private void preRenderDamagedBlocks()
    {
	GlStateManager.tryBlendFuncSeparate(774, 768, 1, 0);
	GlStateManager.enableBlend();
	GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
	GlStateManager.doPolygonOffset(-3.0F, -3.0F);
	GlStateManager.enablePolygonOffset();
	GlStateManager.alphaFunc(516, 0.1F);
	GlStateManager.enableAlpha();
	GlStateManager.pushMatrix();
    }
    
    private void postRenderDamagedBlocks()
    {
	GlStateManager.disableAlpha();
	GlStateManager.doPolygonOffset(0.0F, 0.0F);
	GlStateManager.disablePolygonOffset();
	GlStateManager.enableAlpha();
	GlStateManager.depthMask(true);
	GlStateManager.popMatrix();
    }
    
    public void drawBlockDamageTexture(Tessellator tessellatorIn, WorldRenderer worldRendererIn, Entity entityIn, float partialTicks)
    {
	double var5 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
	double var7 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
	double var9 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
	
	if (!this.damagedBlocks.isEmpty())
	{
	    this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
	    this.preRenderDamagedBlocks();
	    worldRendererIn.startDrawingQuads();
	    worldRendererIn.setVertexFormat(DefaultVertexFormats.BLOCK);
	    worldRendererIn.setTranslation(-var5, -var7, -var9);
	    worldRendererIn.markDirty();
	    Iterator var11 = this.damagedBlocks.values().iterator();
	    
	    while (var11.hasNext())
	    {
		DestroyBlockProgress var12 = (DestroyBlockProgress) var11.next();
		BlockPos var13 = var12.getPosition();
		double var14 = var13.getX() - var5;
		double var16 = var13.getY() - var7;
		double var18 = var13.getZ() - var9;
		Block var20 = this.theWorld.getBlockState(var13).getBlock();
		boolean renderBreaking;
		
		if (Reflector.ForgeTileEntity_canRenderBreaking.exists())
		{
		    boolean var22 = var20 instanceof BlockChest || var20 instanceof BlockEnderChest || var20 instanceof BlockSign || var20 instanceof BlockSkull;
		    
		    if (!var22)
		    {
			TileEntity var23 = this.theWorld.getTileEntity(var13);
			
			if (var23 != null)
			{
			    var22 = Reflector.callBoolean(var23, Reflector.ForgeTileEntity_canRenderBreaking, new Object[0]);
			}
		    }
		    
		    renderBreaking = !var22;
		}
		else
		{
		    renderBreaking = !(var20 instanceof BlockChest) && !(var20 instanceof BlockEnderChest) && !(var20 instanceof BlockSign) && !(var20 instanceof BlockSkull);
		}
		
		if (renderBreaking)
		{
		    if (var14 * var14 + var16 * var16 + var18 * var18 > 1024.0D)
		    {
			var11.remove();
		    }
		    else
		    {
			IBlockState var21 = this.theWorld.getBlockState(var13);
			
			if (var21.getBlock().getMaterial() != Material.air)
			{
			    int var221 = var12.getPartialBlockDamage();
			    TextureAtlasSprite var231 = this.destroyBlockIcons[var221];
			    BlockRendererDispatcher var24 = this.mc.getBlockRendererDispatcher();
			    var24.renderBlockDamage(var21, var13, var231, this.theWorld);
			}
		    }
		}
	    }
	    
	    tessellatorIn.draw();
	    worldRendererIn.setTranslation(0.0D, 0.0D, 0.0D);
	    this.postRenderDamagedBlocks();
	}
    }
    
    /**
     * Draws the selection box for the player. Args: entityPlayer, rayTraceHit, i, itemStack, partialTickTime
     */
    public void drawSelectionBox(EntityPlayer player, MovingObjectPosition movingObjectPositionIn, int p_72731_3_, float partialTicks)
    {
	if (p_72731_3_ == 0 && movingObjectPositionIn.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
	{
	    GlStateManager.enableBlend();
	    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	    GlStateManager.color(0.0F, 0.0F, 0.0F, 0.4F);
	    GL11.glLineWidth(2.0F);
	    GlStateManager.disableTexture2D();
	    GlStateManager.depthMask(false);
	    BlockPos var6 = movingObjectPositionIn.getBlockPos();
	    Block var7 = this.theWorld.getBlockState(var6).getBlock();
	    
	    if (var7.getMaterial() != Material.air && this.theWorld.getWorldBorder().contains(var6))
	    {
		var7.setBlockBoundsBasedOnState(this.theWorld, var6);
		double var8 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
		double var10 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
		double var12 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
		drawOutlinedBoundingBox(var7.getSelectedBoundingBox(this.theWorld, var6).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-var8, -var10, -var12), -1);
	    }
	    
	    GlStateManager.depthMask(true);
	    GlStateManager.enableTexture2D();
	    GlStateManager.disableBlend();
	}
    }
    
    /**
     * Draws lines for the edges of the bounding box.
     *  
     * @param boundingBox The bounding box to draw.
     */
    public static void drawOutlinedBoundingBox(AxisAlignedBB boundingBox, int color)
    {
	Tessellator var2 = Tessellator.getInstance();
	WorldRenderer var3 = var2.getWorldRenderer();
	var3.startDrawing(3);
	
	if (color != -1)
	{
	    var3.setColorOpaque_I(color);
	}
	
	var3.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
	var3.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
	var3.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
	var3.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
	var3.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
	var2.draw();
	var3.startDrawing(3);
	
	if (color != -1)
	{
	    var3.setColorOpaque_I(color);
	}
	
	var3.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
	var3.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
	var3.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
	var3.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
	var3.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
	var2.draw();
	var3.startDrawing(1);
	
	if (color != -1)
	{
	    var3.setColorOpaque_I(color);
	}
	
	var3.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
	var3.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
	var3.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
	var3.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
	var3.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
	var3.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
	var3.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
	var3.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
	var2.draw();
    }
    
    public static void drawOutlinedBoundingBox(AxisAlignedBB boundingBox, int color, boolean shouldColor)
    {
	Tessellator var2 = Tessellator.getInstance();
	WorldRenderer var3 = var2.getWorldRenderer();
	var3.startDrawing(3);
	
	if (color != -1)
	{
	    if (shouldColor) var3.setColorOpaque_I(color);
	}
	
	var3.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
	var3.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
	var3.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
	var3.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
	var3.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
	var2.draw();
	var3.startDrawing(3);
	
	if (color != -1)
	{
	    if (shouldColor) var3.setColorOpaque_I(color);
	}
	
	var3.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
	var3.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
	var3.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
	var3.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
	var3.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
	var2.draw();
	var3.startDrawing(1);
	
	if (color != -1)
	{
	    if (shouldColor) var3.setColorOpaque_I(color);
	}
	
	var3.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
	var3.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
	var3.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
	var3.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
	var3.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
	var3.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
	var3.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
	var3.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
	var2.draw();
    }
    
    /**
     * Marks the blocks in the given range for update
     */
    private void markBlocksForUpdate(int x1, int y1, int z1, int x2, int y2, int z2)
    {
	this.viewFrustum.markBlocksForUpdate(x1, y1, z1, x2, y2, z2);
    }
    
    @Override
    public void markBlockForUpdate(BlockPos pos)
    {
	int var2 = pos.getX();
	int var3 = pos.getY();
	int var4 = pos.getZ();
	this.markBlocksForUpdate(var2 - 1, var3 - 1, var4 - 1, var2 + 1, var3 + 1, var4 + 1);
    }
    
    @Override
    public void notifyLightSet(BlockPos pos)
    {
	int var2 = pos.getX();
	int var3 = pos.getY();
	int var4 = pos.getZ();
	this.markBlocksForUpdate(var2 - 1, var3 - 1, var4 - 1, var2 + 1, var3 + 1, var4 + 1);
    }
    
    /**
     * On the client, re-renders all blocks in this range, inclusive. On the server, does nothing. Args: min x, min y,
     * min z, max x, max y, max z
     */
    @Override
    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2)
    {
	this.markBlocksForUpdate(x1 - 1, y1 - 1, z1 - 1, x2 + 1, y2 + 1, z2 + 1);
    }
    
    @Override
    public void playRecord(String recordName, BlockPos blockPosIn)
    {
	ISound var3 = (ISound) this.mapSoundPositions.get(blockPosIn);
	
	if (var3 != null)
	{
	    this.mc.getSoundHandler().stopSound(var3);
	    this.mapSoundPositions.remove(blockPosIn);
	}
	
	if (recordName != null)
	{
	    ItemRecord var4 = ItemRecord.getRecord(recordName);
	    
	    if (var4 != null)
	    {
		this.mc.ingameGUI.setRecordPlayingMessage(var4.getRecordNameLocal());
	    }
	    
	    ResourceLocation resource = null;
	    
	    if (Reflector.ForgeItemRecord_getRecordResource.exists() && var4 != null)
	    {
		resource = (ResourceLocation) Reflector.call(var4, Reflector.ForgeItemRecord_getRecordResource, new Object[] { recordName });
	    }
	    
	    if (resource == null)
	    {
		resource = new ResourceLocation(recordName);
	    }
	    
	    PositionedSoundRecord var5 = PositionedSoundRecord.create(resource, blockPosIn.getX(), blockPosIn.getY(), blockPosIn.getZ());
	    this.mapSoundPositions.put(blockPosIn, var5);
	    this.mc.getSoundHandler().playSound(var5);
	}
    }
    
    /**
     * Plays the specified sound. Arg: soundName, x, y, z, volume, pitch
     */
    @Override
    public void playSound(String soundName, double x, double y, double z, float volume, float pitch)
    {
    }
    
    /**
     * Plays sound to all near players except the player reference given
     */
    @Override
    public void playSoundToNearExcept(EntityPlayer except, String soundName, double x, double y, double z, float volume, float pitch)
    {
    }
    
    @Override
    public void spawnParticle(int particleID, boolean p_180442_2_, final double xCoord, final double yCoord, final double zCoord, double xOffset, double yOffset, double zOffset, int... p_180442_15_)
    {
	try
	{
	    this.spawnEntityFX(particleID, p_180442_2_, xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, p_180442_15_);
	}
	catch (Throwable var19)
	{
	    CrashReport var17 = CrashReport.makeCrashReport(var19, "Exception while adding particle");
	    CrashReportCategory var18 = var17.makeCategory("Particle being added");
	    var18.addCrashSection("ID", Integer.valueOf(particleID));
	    
	    if (p_180442_15_ != null)
	    {
		var18.addCrashSection("Parameters", p_180442_15_);
	    }
	    
	    var18.addCrashSectionCallable("Position", new Callable()
	    {
		@Override
		public String call()
		{
		    return CrashReportCategory.getCoordinateInfo(xCoord, yCoord, zCoord);
		}
	    });
	    throw new ReportedException(var17);
	}
    }
    
    private void spawnParticle(EnumParticleTypes particleIn, double p_174972_2_, double p_174972_4_, double p_174972_6_, double p_174972_8_, double p_174972_10_, double p_174972_12_, int... p_174972_14_)
    {
	this.spawnParticle(particleIn.getParticleID(), particleIn.func_179344_e(), p_174972_2_, p_174972_4_, p_174972_6_, p_174972_8_, p_174972_10_, p_174972_12_, p_174972_14_);
    }
    
    private EntityFX spawnEntityFX(int p_174974_1_, boolean p_174974_2_, double p_174974_3_, double p_174974_5_, double p_174974_7_, double p_174974_9_, double p_174974_11_, double p_174974_13_, int... p_174974_15_)
    {
	if (this.mc != null && this.mc.getRenderViewEntity() != null && this.mc.effectRenderer != null)
	{
	    int var16 = this.mc.gameSettings.particleSetting;
	    
	    if (var16 == 1 && this.theWorld.rand.nextInt(3) == 0)
	    {
		var16 = 2;
	    }
	    
	    double var17 = this.mc.getRenderViewEntity().posX - p_174974_3_;
	    double var19 = this.mc.getRenderViewEntity().posY - p_174974_5_;
	    double var21 = this.mc.getRenderViewEntity().posZ - p_174974_7_;
	    
	    if (p_174974_1_ == EnumParticleTypes.EXPLOSION_HUGE.getParticleID() && !Config.isAnimatedExplosion())
	    {
		return null;
	    }
	    else if (p_174974_1_ == EnumParticleTypes.EXPLOSION_LARGE.getParticleID() && !Config.isAnimatedExplosion())
	    {
		return null;
	    }
	    else if (p_174974_1_ == EnumParticleTypes.EXPLOSION_NORMAL.getParticleID() && !Config.isAnimatedExplosion())
	    {
		return null;
	    }
	    else if (p_174974_1_ == EnumParticleTypes.SUSPENDED.getParticleID() && !Config.isWaterParticles())
	    {
		return null;
	    }
	    else if (p_174974_1_ == EnumParticleTypes.SUSPENDED_DEPTH.getParticleID() && !Config.isVoidParticles())
	    {
		return null;
	    }
	    else if (p_174974_1_ == EnumParticleTypes.SMOKE_NORMAL.getParticleID() && !Config.isAnimatedSmoke())
	    {
		return null;
	    }
	    else if (p_174974_1_ == EnumParticleTypes.SMOKE_LARGE.getParticleID() && !Config.isAnimatedSmoke())
	    {
		return null;
	    }
	    else if (p_174974_1_ == EnumParticleTypes.SPELL_MOB.getParticleID() && !Config.isPotionParticles())
	    {
		return null;
	    }
	    else if (p_174974_1_ == EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID() && !Config.isPotionParticles())
	    {
		return null;
	    }
	    else if (p_174974_1_ == EnumParticleTypes.SPELL.getParticleID() && !Config.isPotionParticles())
	    {
		return null;
	    }
	    else if (p_174974_1_ == EnumParticleTypes.SPELL_INSTANT.getParticleID() && !Config.isPotionParticles())
	    {
		return null;
	    }
	    else if (p_174974_1_ == EnumParticleTypes.SPELL_WITCH.getParticleID() && !Config.isPotionParticles())
	    {
		return null;
	    }
	    else if (p_174974_1_ == EnumParticleTypes.PORTAL.getParticleID() && !Config.isAnimatedPortal())
	    {
		return null;
	    }
	    else if (p_174974_1_ == EnumParticleTypes.FLAME.getParticleID() && !Config.isAnimatedFlame())
	    {
		return null;
	    }
	    else if (p_174974_1_ == EnumParticleTypes.REDSTONE.getParticleID() && !Config.isAnimatedRedstone())
	    {
		return null;
	    }
	    else if (p_174974_1_ == EnumParticleTypes.DRIP_WATER.getParticleID() && !Config.isDrippingWaterLava())
	    {
		return null;
	    }
	    else if (p_174974_1_ == EnumParticleTypes.DRIP_LAVA.getParticleID() && !Config.isDrippingWaterLava())
	    {
		return null;
	    }
	    else if (p_174974_1_ == EnumParticleTypes.FIREWORKS_SPARK.getParticleID() && !Config.isFireworkParticles())
	    {
		return null;
	    }
	    else if (p_174974_2_)
	    {
		return this.mc.effectRenderer.spawnEffectParticle(p_174974_1_, p_174974_3_, p_174974_5_, p_174974_7_, p_174974_9_, p_174974_11_, p_174974_13_, p_174974_15_);
	    }
	    else
	    {
		double maxDistSq = 256.0D;
		
		if (p_174974_1_ == EnumParticleTypes.CRIT.getParticleID())
		{
		    maxDistSq = 38416.0D;
		}
		
		if (var17 * var17 + var19 * var19 + var21 * var21 > maxDistSq)
		{
		    return null;
		}
		else if (var16 > 1)
		{
		    return null;
		}
		else
		{
		    EntityFX entityFx = this.mc.effectRenderer.spawnEffectParticle(p_174974_1_, p_174974_3_, p_174974_5_, p_174974_7_, p_174974_9_, p_174974_11_, p_174974_13_, p_174974_15_);
		    
		    if (p_174974_1_ == EnumParticleTypes.WATER_BUBBLE.getParticleID())
		    {
			CustomColorizer.updateWaterFX(entityFx, this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_);
		    }
		    
		    if (p_174974_1_ == EnumParticleTypes.WATER_SPLASH.getParticleID())
		    {
			CustomColorizer.updateWaterFX(entityFx, this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_);
		    }
		    
		    if (p_174974_1_ == EnumParticleTypes.WATER_DROP.getParticleID())
		    {
			CustomColorizer.updateWaterFX(entityFx, this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_);
		    }
		    
		    if (p_174974_1_ == EnumParticleTypes.TOWN_AURA.getParticleID())
		    {
			CustomColorizer.updateMyceliumFX(entityFx);
		    }
		    
		    if (p_174974_1_ == EnumParticleTypes.PORTAL.getParticleID())
		    {
			CustomColorizer.updatePortalFX(entityFx);
		    }
		    
		    if (p_174974_1_ == EnumParticleTypes.REDSTONE.getParticleID())
		    {
			CustomColorizer.updateReddustFX(entityFx, this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_);
		    }
		    
		    return entityFx;
		}
	    }
	}
	else
	{
	    return null;
	}
    }
    
    /**
     * Called on all IWorldAccesses when an entity is created or loaded. On client worlds, starts downloading any
     * necessary textures. On server worlds, adds the entity to the entity tracker.
     */
    @Override
    public void onEntityAdded(Entity entityIn)
    {
	RandomMobs.entityLoaded(entityIn);
    }
    
    /**
     * Called on all IWorldAccesses when an entity is unloaded or destroyed. On client worlds, releases any downloaded
     * textures. On server worlds, removes the entity from the entity tracker.
     */
    @Override
    public void onEntityRemoved(Entity entityIn)
    {
    }
    
    /**
     * Deletes all display lists
     */
    public void deleteAllDisplayLists()
    {
    }
    
    @Override
    public void broadcastSound(int p_180440_1_, BlockPos p_180440_2_, int p_180440_3_)
    {
	switch (p_180440_1_)
	{
	case 1013:
	case 1018:
	    if (this.mc.getRenderViewEntity() != null)
	    {
		double var4 = p_180440_2_.getX() - this.mc.getRenderViewEntity().posX;
		double var6 = p_180440_2_.getY() - this.mc.getRenderViewEntity().posY;
		double var8 = p_180440_2_.getZ() - this.mc.getRenderViewEntity().posZ;
		double var10 = Math.sqrt(var4 * var4 + var6 * var6 + var8 * var8);
		double var12 = this.mc.getRenderViewEntity().posX;
		double var14 = this.mc.getRenderViewEntity().posY;
		double var16 = this.mc.getRenderViewEntity().posZ;
		
		if (var10 > 0.0D)
		{
		    var12 += var4 / var10 * 2.0D;
		    var14 += var6 / var10 * 2.0D;
		    var16 += var8 / var10 * 2.0D;
		}
		
		if (p_180440_1_ == 1013)
		{
		    this.theWorld.playSound(var12, var14, var16, "mob.wither.spawn", 1.0F, 1.0F, false);
		}
		else
		{
		    this.theWorld.playSound(var12, var14, var16, "mob.enderdragon.end", 5.0F, 1.0F, false);
		}
	    }
	    
	default:
	}
    }
    
    @Override
    public void playAusSFX(EntityPlayer player, int sfxType, BlockPos blockPosIn, int p_180439_4_)
    {
	Random var5 = this.theWorld.rand;
	double var7;
	double var9;
	double var11;
	int var13;
	int var18;
	double var19;
	double var21;
	double var23;
	double var32;
	double var25;
	double var27;
	
	switch (sfxType)
	{
	case 1000:
	    this.theWorld.playSoundAtPos(blockPosIn, "random.click", 1.0F, 1.0F, false);
	    break;
	    
	case 1001:
	    this.theWorld.playSoundAtPos(blockPosIn, "random.click", 1.0F, 1.2F, false);
	    break;
	    
	case 1002:
	    this.theWorld.playSoundAtPos(blockPosIn, "random.bow", 1.0F, 1.2F, false);
	    break;
	    
	case 1003:
	    this.theWorld.playSoundAtPos(blockPosIn, "random.door_open", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
	    break;
	    
	case 1004:
	    this.theWorld.playSoundAtPos(blockPosIn, "random.fizz", 0.5F, 2.6F + (var5.nextFloat() - var5.nextFloat()) * 0.8F, false);
	    break;
	    
	case 1005:
	    if (Item.getItemById(p_180439_4_) instanceof ItemRecord)
	    {
		this.theWorld.playRecord(blockPosIn, "records." + ((ItemRecord) Item.getItemById(p_180439_4_)).recordName);
	    }
	    else
	    {
		this.theWorld.playRecord(blockPosIn, (String) null);
	    }
	    
	    break;
	    
	case 1006:
	    this.theWorld.playSoundAtPos(blockPosIn, "random.door_close", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
	    break;
	    
	case 1007:
	    this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.charge", 10.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
	    break;
	    
	case 1008:
	    this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.fireball", 10.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
	    break;
	    
	case 1009:
	    this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.fireball", 2.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
	    break;
	    
	case 1010:
	    this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.wood", 2.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
	    break;
	    
	case 1011:
	    this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.metal", 2.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
	    break;
	    
	case 1012:
	    this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.woodbreak", 2.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
	    break;
	    
	case 1014:
	    this.theWorld.playSoundAtPos(blockPosIn, "mob.wither.shoot", 2.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
	    break;
	    
	case 1015:
	    this.theWorld.playSoundAtPos(blockPosIn, "mob.bat.takeoff", 0.05F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
	    break;
	    
	case 1016:
	    this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.infect", 2.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
	    break;
	    
	case 1017:
	    this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.unfect", 2.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
	    break;
	    
	case 1020:
	    this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_break", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
	    break;
	    
	case 1021:
	    this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_use", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
	    break;
	    
	case 1022:
	    this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_land", 0.3F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
	    break;
	    
	case 2000:
	    int var31 = p_180439_4_ % 3 - 1;
	    int var8 = p_180439_4_ / 3 % 3 - 1;
	    var9 = blockPosIn.getX() + var31 * 0.6D + 0.5D;
	    var11 = blockPosIn.getY() + 0.5D;
	    var32 = blockPosIn.getZ() + var8 * 0.6D + 0.5D;
	    
	    for (int var39 = 0; var39 < 10; ++var39)
	    {
		double var40 = var5.nextDouble() * 0.2D + 0.01D;
		double var41 = var9 + var31 * 0.01D + (var5.nextDouble() - 0.5D) * var8 * 0.5D;
		var25 = var11 + (var5.nextDouble() - 0.5D) * 0.5D;
		var27 = var32 + var8 * 0.01D + (var5.nextDouble() - 0.5D) * var31 * 0.5D;
		double var42 = var31 * var40 + var5.nextGaussian() * 0.01D;
		double var26 = -0.03D + var5.nextGaussian() * 0.01D;
		double var28 = var8 * var40 + var5.nextGaussian() * 0.01D;
		this.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var41, var25, var27, var42, var26, var28, new int[0]);
	    }
	    
	    return;
	    
	case 2001:
	    Block var6 = Block.getBlockById(p_180439_4_ & 4095);
	    
	    if (var6.getMaterial() != Material.air)
	    {
		this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(var6.stepSound.getBreakSound()), (var6.stepSound.getVolume() + 1.0F) / 2.0F, var6.stepSound.getFrequency() * 0.8F, blockPosIn.getX() + 0.5F, blockPosIn.getY() + 0.5F, blockPosIn.getZ() + 0.5F));
	    }
	    
	    this.mc.effectRenderer.addBlockDestroyEffects(blockPosIn, var6.getStateFromMeta(p_180439_4_ >> 12 & 255));
	    break;
	    
	case 2002:
	    var7 = blockPosIn.getX();
	    var9 = blockPosIn.getY();
	    var11 = blockPosIn.getZ();
	    
	    for (var13 = 0; var13 < 8; ++var13)
	    {
		this.spawnParticle(EnumParticleTypes.ITEM_CRACK, var7, var9, var11, var5.nextGaussian() * 0.15D, var5.nextDouble() * 0.2D, var5.nextGaussian() * 0.15D, new int[] { Item.getIdFromItem(Items.potionitem), p_180439_4_ });
	    }
	    
	    var13 = Items.potionitem.getColorFromDamage(p_180439_4_);
	    float var14 = (var13 >> 16 & 255) / 255.0F;
	    float var15 = (var13 >> 8 & 255) / 255.0F;
	    float var16 = (var13 >> 0 & 255) / 255.0F;
	    EnumParticleTypes var17 = EnumParticleTypes.SPELL;
	    
	    if (Items.potionitem.isEffectInstant(p_180439_4_))
	    {
		var17 = EnumParticleTypes.SPELL_INSTANT;
	    }
	    
	    for (var18 = 0; var18 < 100; ++var18)
	    {
		var19 = var5.nextDouble() * 4.0D;
		var21 = var5.nextDouble() * Math.PI * 2.0D;
		var23 = Math.cos(var21) * var19;
		var25 = 0.01D + var5.nextDouble() * 0.5D;
		var27 = Math.sin(var21) * var19;
		EntityFX var29 = this.spawnEntityFX(var17.getParticleID(), var17.func_179344_e(), var7 + var23 * 0.1D, var9 + 0.3D, var11 + var27 * 0.1D, var23, var25, var27, new int[0]);
		
		if (var29 != null)
		{
		    float var30 = 0.75F + var5.nextFloat() * 0.25F;
		    var29.setRBGColorF(var14 * var30, var15 * var30, var16 * var30);
		    var29.multiplyVelocity((float) var19);
		}
	    }
	    
	    this.theWorld.playSoundAtPos(blockPosIn, "game.potion.smash", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
	    break;
	    
	case 2003:
	    var7 = blockPosIn.getX() + 0.5D;
	    var9 = blockPosIn.getY();
	    var11 = blockPosIn.getZ() + 0.5D;
	    
	    for (var13 = 0; var13 < 8; ++var13)
	    {
		this.spawnParticle(EnumParticleTypes.ITEM_CRACK, var7, var9, var11, var5.nextGaussian() * 0.15D, var5.nextDouble() * 0.2D, var5.nextGaussian() * 0.15D, new int[] { Item.getIdFromItem(Items.ender_eye) });
	    }
	    
	    for (var32 = 0.0D; var32 < (Math.PI * 2D); var32 += 0.15707963267948966D)
	    {
		this.spawnParticle(EnumParticleTypes.PORTAL, var7 + Math.cos(var32) * 5.0D, var9 - 0.4D, var11 + Math.sin(var32) * 5.0D, Math.cos(var32) * -5.0D, 0.0D, Math.sin(var32) * -5.0D, new int[0]);
		this.spawnParticle(EnumParticleTypes.PORTAL, var7 + Math.cos(var32) * 5.0D, var9 - 0.4D, var11 + Math.sin(var32) * 5.0D, Math.cos(var32) * -7.0D, 0.0D, Math.sin(var32) * -7.0D, new int[0]);
	    }
	    
	    return;
	    
	case 2004:
	    for (var18 = 0; var18 < 20; ++var18)
	    {
		var19 = blockPosIn.getX() + 0.5D + (this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
		var21 = blockPosIn.getY() + 0.5D + (this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
		var23 = blockPosIn.getZ() + 0.5D + (this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
		this.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var19, var21, var23, 0.0D, 0.0D, 0.0D, new int[0]);
		this.theWorld.spawnParticle(EnumParticleTypes.FLAME, var19, var21, var23, 0.0D, 0.0D, 0.0D, new int[0]);
	    }
	    
	    return;
	    
	case 2005:
	    ItemDye.spawnBonemealParticles(this.theWorld, blockPosIn, p_180439_4_);
	}
    }
    
    @Override
    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress)
    {
	if (progress >= 0 && progress < 10)
	{
	    DestroyBlockProgress var4 = (DestroyBlockProgress) this.damagedBlocks.get(Integer.valueOf(breakerId));
	    
	    if (var4 == null || var4.getPosition().getX() != pos.getX() || var4.getPosition().getY() != pos.getY() || var4.getPosition().getZ() != pos.getZ())
	    {
		var4 = new DestroyBlockProgress(breakerId, pos);
		this.damagedBlocks.put(Integer.valueOf(breakerId), var4);
	    }
	    
	    var4.setPartialBlockDamage(progress);
	    var4.setCloudUpdateTick(this.cloudTickCounter);
	}
	else
	{
	    this.damagedBlocks.remove(Integer.valueOf(breakerId));
	}
    }
    
    public void setDisplayListEntitiesDirty()
    {
	this.displayListEntitiesDirty = true;
    }
    
    public void resetClouds()
    {
	this.cloudRenderer.reset();
    }
    
    class ContainerLocalRenderInformation
    {
	final RenderChunk renderChunk;
	final EnumFacing facing;
	final Set setFacing;
	final int counter;
	
	private ContainerLocalRenderInformation(RenderChunk renderChunkIn, EnumFacing facingIn, int counterIn)
	{
	    this.setFacing = EnumSet.noneOf(EnumFacing.class);
	    this.renderChunk = renderChunkIn;
	    this.facing = facingIn;
	    this.counter = counterIn;
	}
	
	ContainerLocalRenderInformation(RenderChunk p_i46249_2_, EnumFacing p_i46249_3_, int p_i46249_4_, Object p_i46249_5_)
	{
	    this(p_i46249_2_, p_i46249_3_, p_i46249_4_);
	}
    }
    
    static final class SwitchEnumUseage
    {
	static final int[] VALUES = new int[VertexFormatElement.EnumUseage.values().length];
	
	static
	{
	    try
	    {
		VALUES[VertexFormatElement.EnumUseage.POSITION.ordinal()] = 1;
	    }
	    catch (NoSuchFieldError var3)
	    {
		;
	    }
	    
	    try
	    {
		VALUES[VertexFormatElement.EnumUseage.UV.ordinal()] = 2;
	    }
	    catch (NoSuchFieldError var2)
	    {
		;
	    }
	    
	    try
	    {
		VALUES[VertexFormatElement.EnumUseage.COLOR.ordinal()] = 3;
	    }
	    catch (NoSuchFieldError var1)
	    {
		;
	    }
	}
    }
}
