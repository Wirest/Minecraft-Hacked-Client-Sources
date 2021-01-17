// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import java.util.EnumSet;
import net.minecraft.item.ItemDye;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import optifine.RandomMobs;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReportCategory;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.item.ItemRecord;
import net.minecraft.client.audio.ISound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import optifine.CustomSky;
import net.minecraft.world.IBlockAccess;
import optifine.CustomColors;
import org.lwjgl.input.Keyboard;
import shadersmod.client.ShadersRender;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.Matrix4f;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.chunk.VisGraph;
import org.lwjgl.util.vector.Vector3f;
import optifine.ChunkUtils;
import net.minecraft.client.renderer.chunk.RenderChunk;
import shadersmod.client.ShadowUtils;
import optifine.RenderInfoLazy;
import optifine.Lagometer;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.world.chunk.Chunk;
import java.util.Iterator;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockChest;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.BlockPos;
import shadersmod.client.Shaders;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import optifine.Reflector;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.init.Blocks;
import optifine.DynamicLights;
import net.minecraft.world.World;
import java.util.Random;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import optifine.Config;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.chunk.ListChunkFactory;
import net.minecraft.client.renderer.chunk.VboChunkFactory;
import org.lwjgl.opengl.GL11;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import net.minecraft.util.EnumFacing;
import org.apache.logging.log4j.LogManager;
import java.util.Deque;
import net.minecraft.entity.Entity;
import optifine.CloudRenderer;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.util.Vector3d;
import org.lwjgl.util.vector.Vector4f;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import java.util.Map;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import java.util.List;
import java.util.Set;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.world.IWorldAccess;

public class RenderGlobal implements IWorldAccess, IResourceManagerReloadListener
{
    private static final Logger logger;
    private static final ResourceLocation locationMoonPhasesPng;
    private static final ResourceLocation locationSunPng;
    private static final ResourceLocation locationCloudsPng;
    private static final ResourceLocation locationEndSkyPng;
    private static final ResourceLocation locationForcefieldPng;
    public final Minecraft mc;
    private final TextureManager renderEngine;
    private final RenderManager renderManager;
    private WorldClient theWorld;
    private Set chunksToUpdate;
    private List renderInfos;
    private final Set field_181024_n;
    private ViewFrustum viewFrustum;
    private int starGLCallList;
    private int glSkyList;
    private int glSkyList2;
    private VertexFormat vertexBufferFormat;
    private VertexBuffer starVBO;
    private VertexBuffer skyVBO;
    private VertexBuffer sky2VBO;
    private int cloudTickCounter;
    public final Map damagedBlocks;
    private final Map mapSoundPositions;
    private final TextureAtlasSprite[] destroyBlockIcons;
    private Framebuffer entityOutlineFramebuffer;
    private ShaderGroup entityOutlineShader;
    private double frustumUpdatePosX;
    private double frustumUpdatePosY;
    private double frustumUpdatePosZ;
    private int frustumUpdatePosChunkX;
    private int frustumUpdatePosChunkY;
    private int frustumUpdatePosChunkZ;
    private double lastViewEntityX;
    private double lastViewEntityY;
    private double lastViewEntityZ;
    private double lastViewEntityPitch;
    private double lastViewEntityYaw;
    private final ChunkRenderDispatcher renderDispatcher;
    private ChunkRenderContainer renderContainer;
    private int renderDistanceChunks;
    private int renderEntitiesStartupCounter;
    private int countEntitiesTotal;
    private int countEntitiesRendered;
    private int countEntitiesHidden;
    private boolean debugFixTerrainFrustum;
    private ClippingHelper debugFixedClippingHelper;
    private final Vector4f[] debugTerrainMatrix;
    private final Vector3d debugTerrainFrustumPosition;
    private boolean vboEnabled;
    IRenderChunkFactory renderChunkFactory;
    private double prevRenderSortX;
    private double prevRenderSortY;
    private double prevRenderSortZ;
    public boolean displayListEntitiesDirty;
    private static final String __OBFID = "CL_00000954";
    private CloudRenderer cloudRenderer;
    public Entity renderedEntity;
    public Set chunksToResortTransparency;
    public Set chunksToUpdateForced;
    private Deque visibilityDeque;
    private List renderInfosEntities;
    private List renderInfosTileEntities;
    private List renderInfosNormal;
    private List renderInfosEntitiesNormal;
    private List renderInfosTileEntitiesNormal;
    private List renderInfosShadow;
    private List renderInfosEntitiesShadow;
    private List renderInfosTileEntitiesShadow;
    private int renderDistance;
    private int renderDistanceSq;
    private static final Set SET_ALL_FACINGS;
    private int countTileEntitiesRendered;
    
    static {
        logger = LogManager.getLogger();
        locationMoonPhasesPng = new ResourceLocation("textures/environment/moon_phases.png");
        locationSunPng = new ResourceLocation("textures/environment/sun.png");
        locationCloudsPng = new ResourceLocation("textures/environment/clouds.png");
        locationEndSkyPng = new ResourceLocation("textures/environment/end_sky.png");
        locationForcefieldPng = new ResourceLocation("textures/misc/forcefield.png");
        SET_ALL_FACINGS = Collections.unmodifiableSet((Set<?>)new HashSet<Object>(Arrays.asList(EnumFacing.VALUES)));
    }
    
    public RenderGlobal(final Minecraft mcIn) {
        this.chunksToUpdate = Sets.newLinkedHashSet();
        this.renderInfos = Lists.newArrayListWithCapacity(69696);
        this.field_181024_n = Sets.newHashSet();
        this.starGLCallList = -1;
        this.glSkyList = -1;
        this.glSkyList2 = -1;
        this.damagedBlocks = Maps.newHashMap();
        this.mapSoundPositions = Maps.newHashMap();
        this.destroyBlockIcons = new TextureAtlasSprite[10];
        this.frustumUpdatePosX = Double.MIN_VALUE;
        this.frustumUpdatePosY = Double.MIN_VALUE;
        this.frustumUpdatePosZ = Double.MIN_VALUE;
        this.frustumUpdatePosChunkX = Integer.MIN_VALUE;
        this.frustumUpdatePosChunkY = Integer.MIN_VALUE;
        this.frustumUpdatePosChunkZ = Integer.MIN_VALUE;
        this.lastViewEntityX = Double.MIN_VALUE;
        this.lastViewEntityY = Double.MIN_VALUE;
        this.lastViewEntityZ = Double.MIN_VALUE;
        this.lastViewEntityPitch = Double.MIN_VALUE;
        this.lastViewEntityYaw = Double.MIN_VALUE;
        this.renderDispatcher = new ChunkRenderDispatcher();
        this.renderDistanceChunks = -1;
        this.renderEntitiesStartupCounter = 2;
        this.debugFixTerrainFrustum = false;
        this.debugTerrainMatrix = new Vector4f[8];
        this.debugTerrainFrustumPosition = new Vector3d();
        this.vboEnabled = false;
        this.displayListEntitiesDirty = true;
        this.chunksToResortTransparency = new LinkedHashSet();
        this.chunksToUpdateForced = new LinkedHashSet();
        this.visibilityDeque = new ArrayDeque();
        this.renderInfosEntities = new ArrayList(1024);
        this.renderInfosTileEntities = new ArrayList(1024);
        this.renderInfosNormal = new ArrayList(1024);
        this.renderInfosEntitiesNormal = new ArrayList(1024);
        this.renderInfosTileEntitiesNormal = new ArrayList(1024);
        this.renderInfosShadow = new ArrayList(1024);
        this.renderInfosEntitiesShadow = new ArrayList(1024);
        this.renderInfosTileEntitiesShadow = new ArrayList(1024);
        this.renderDistance = 0;
        this.renderDistanceSq = 0;
        this.cloudRenderer = new CloudRenderer(mcIn);
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        (this.renderEngine = mcIn.getTextureManager()).bindTexture(RenderGlobal.locationForcefieldPng);
        GL11.glTexParameteri(3553, 10242, 10497);
        GL11.glTexParameteri(3553, 10243, 10497);
        GlStateManager.bindTexture(0);
        this.updateDestroyBlockIcons();
        this.vboEnabled = OpenGlHelper.useVbo();
        if (this.vboEnabled) {
            this.renderContainer = new VboRenderList();
            this.renderChunkFactory = new VboChunkFactory();
        }
        else {
            this.renderContainer = new RenderList();
            this.renderChunkFactory = new ListChunkFactory();
        }
        (this.vertexBufferFormat = new VertexFormat()).func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
        this.generateStars();
        this.generateSky();
        this.generateSky2();
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
        this.updateDestroyBlockIcons();
    }
    
    private void updateDestroyBlockIcons() {
        final TextureMap texturemap = this.mc.getTextureMapBlocks();
        for (int i = 0; i < this.destroyBlockIcons.length; ++i) {
            this.destroyBlockIcons[i] = texturemap.getAtlasSprite("minecraft:blocks/destroy_stage_" + i);
        }
    }
    
    public void makeEntityOutlineShader() {
        if (OpenGlHelper.shadersSupported) {
            if (ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
                ShaderLinkHelper.setNewStaticShaderLinkHelper();
            }
            final ResourceLocation resourcelocation = new ResourceLocation("shaders/post/entity_outline.json");
            try {
                (this.entityOutlineShader = new ShaderGroup(this.mc.getTextureManager(), this.mc.getResourceManager(), this.mc.getFramebuffer(), resourcelocation)).createBindFramebuffers(Minecraft.displayWidth, Minecraft.displayHeight);
                this.entityOutlineFramebuffer = this.entityOutlineShader.getFramebufferRaw("final");
            }
            catch (IOException ioexception) {
                RenderGlobal.logger.warn("Failed to load shader: " + resourcelocation, ioexception);
                this.entityOutlineShader = null;
                this.entityOutlineFramebuffer = null;
            }
            catch (JsonSyntaxException jsonsyntaxexception) {
                RenderGlobal.logger.warn("Failed to load shader: " + resourcelocation, jsonsyntaxexception);
                this.entityOutlineShader = null;
                this.entityOutlineFramebuffer = null;
            }
        }
        else {
            this.entityOutlineShader = null;
            this.entityOutlineFramebuffer = null;
        }
    }
    
    public void renderEntityOutlineFramebuffer() {
        if (this.isRenderEntityOutlines()) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            this.entityOutlineFramebuffer.framebufferRenderExt(Minecraft.displayWidth, Minecraft.displayHeight, false);
            GlStateManager.disableBlend();
        }
    }
    
    protected boolean isRenderEntityOutlines() {
        return !Config.isFastRender() && !Config.isShaders() && !Config.isAntialiasing() && (this.entityOutlineFramebuffer != null && this.entityOutlineShader != null && Minecraft.thePlayer != null && Minecraft.thePlayer.isSpectator() && this.mc.gameSettings.keyBindSpectatorOutlines.isKeyDown());
    }
    
    private void generateSky2() {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        if (this.sky2VBO != null) {
            this.sky2VBO.deleteGlBuffers();
        }
        if (this.glSkyList2 >= 0) {
            GLAllocation.deleteDisplayLists(this.glSkyList2);
            this.glSkyList2 = -1;
        }
        if (this.vboEnabled) {
            this.sky2VBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderSky(worldrenderer, -16.0f, true);
            worldrenderer.finishDrawing();
            worldrenderer.reset();
            this.sky2VBO.func_181722_a(worldrenderer.getByteBuffer());
        }
        else {
            GL11.glNewList(this.glSkyList2 = GLAllocation.generateDisplayLists(1), 4864);
            this.renderSky(worldrenderer, -16.0f, true);
            tessellator.draw();
            GL11.glEndList();
        }
    }
    
    private void generateSky() {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        if (this.skyVBO != null) {
            this.skyVBO.deleteGlBuffers();
        }
        if (this.glSkyList >= 0) {
            GLAllocation.deleteDisplayLists(this.glSkyList);
            this.glSkyList = -1;
        }
        if (this.vboEnabled) {
            this.skyVBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderSky(worldrenderer, 16.0f, false);
            worldrenderer.finishDrawing();
            worldrenderer.reset();
            this.skyVBO.func_181722_a(worldrenderer.getByteBuffer());
        }
        else {
            GL11.glNewList(this.glSkyList = GLAllocation.generateDisplayLists(1), 4864);
            this.renderSky(worldrenderer, 16.0f, false);
            tessellator.draw();
            GL11.glEndList();
        }
    }
    
    private void renderSky(final WorldRenderer worldRendererIn, final float p_174968_2_, final boolean p_174968_3_) {
        final boolean flag = true;
        final boolean flag2 = true;
        worldRendererIn.begin(7, DefaultVertexFormats.POSITION);
        for (int i = -384; i <= 384; i += 64) {
            for (int j = -384; j <= 384; j += 64) {
                float f = (float)i;
                float f2 = (float)(i + 64);
                if (p_174968_3_) {
                    f2 = (float)i;
                    f = (float)(i + 64);
                }
                worldRendererIn.pos(f, p_174968_2_, j).endVertex();
                worldRendererIn.pos(f2, p_174968_2_, j).endVertex();
                worldRendererIn.pos(f2, p_174968_2_, j + 64).endVertex();
                worldRendererIn.pos(f, p_174968_2_, j + 64).endVertex();
            }
        }
    }
    
    private void generateStars() {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        if (this.starVBO != null) {
            this.starVBO.deleteGlBuffers();
        }
        if (this.starGLCallList >= 0) {
            GLAllocation.deleteDisplayLists(this.starGLCallList);
            this.starGLCallList = -1;
        }
        if (this.vboEnabled) {
            this.starVBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderStars(worldrenderer);
            worldrenderer.finishDrawing();
            worldrenderer.reset();
            this.starVBO.func_181722_a(worldrenderer.getByteBuffer());
        }
        else {
            this.starGLCallList = GLAllocation.generateDisplayLists(1);
            GlStateManager.pushMatrix();
            GL11.glNewList(this.starGLCallList, 4864);
            this.renderStars(worldrenderer);
            tessellator.draw();
            GL11.glEndList();
            GlStateManager.popMatrix();
        }
    }
    
    private void renderStars(final WorldRenderer worldRendererIn) {
        final Random random = new Random(10842L);
        worldRendererIn.begin(7, DefaultVertexFormats.POSITION);
        for (int i = 0; i < 1500; ++i) {
            double d0 = random.nextFloat() * 2.0f - 1.0f;
            double d2 = random.nextFloat() * 2.0f - 1.0f;
            double d3 = random.nextFloat() * 2.0f - 1.0f;
            final double d4 = 0.15f + random.nextFloat() * 0.1f;
            double d5 = d0 * d0 + d2 * d2 + d3 * d3;
            if (d5 < 1.0 && d5 > 0.01) {
                d5 = 1.0 / Math.sqrt(d5);
                d0 *= d5;
                d2 *= d5;
                d3 *= d5;
                final double d6 = d0 * 100.0;
                final double d7 = d2 * 100.0;
                final double d8 = d3 * 100.0;
                final double d9 = Math.atan2(d0, d3);
                final double d10 = Math.sin(d9);
                final double d11 = Math.cos(d9);
                final double d12 = Math.atan2(Math.sqrt(d0 * d0 + d3 * d3), d2);
                final double d13 = Math.sin(d12);
                final double d14 = Math.cos(d12);
                final double d15 = random.nextDouble() * 3.141592653589793 * 2.0;
                final double d16 = Math.sin(d15);
                final double d17 = Math.cos(d15);
                for (int j = 0; j < 4; ++j) {
                    final double d18 = 0.0;
                    final double d19 = ((j & 0x2) - 1) * d4;
                    final double d20 = ((j + 1 & 0x2) - 1) * d4;
                    final double d21 = 0.0;
                    final double d22 = d19 * d17 - d20 * d16;
                    final double d23 = d20 * d17 + d19 * d16;
                    final double d24 = d22 * d13 + 0.0 * d14;
                    final double d25 = 0.0 * d13 - d22 * d14;
                    final double d26 = d25 * d10 - d23 * d11;
                    final double d27 = d23 * d10 + d25 * d11;
                    worldRendererIn.pos(d6 + d26, d7 + d24, d8 + d27).endVertex();
                }
            }
        }
    }
    
    public void setWorldAndLoadRenderers(final WorldClient worldClientIn) {
        if (this.theWorld != null) {
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
        if (Config.isDynamicLights()) {
            DynamicLights.clear();
        }
        if (worldClientIn != null) {
            worldClientIn.addWorldAccess(this);
            this.loadRenderers();
        }
    }
    
    public void loadRenderers() {
        if (this.theWorld != null) {
            this.displayListEntitiesDirty = true;
            Blocks.leaves.setGraphicsLevel(Config.isTreesFancy());
            Blocks.leaves2.setGraphicsLevel(Config.isTreesFancy());
            BlockModelRenderer.updateAoLightValue();
            if (Config.isDynamicLights()) {
                DynamicLights.clear();
            }
            this.renderDistanceChunks = this.mc.gameSettings.renderDistanceChunks;
            this.renderDistance = this.renderDistanceChunks * 16;
            this.renderDistanceSq = this.renderDistance * this.renderDistance;
            final boolean flag = this.vboEnabled;
            this.vboEnabled = OpenGlHelper.useVbo();
            if (flag && !this.vboEnabled) {
                this.renderContainer = new RenderList();
                this.renderChunkFactory = new ListChunkFactory();
            }
            else if (!flag && this.vboEnabled) {
                this.renderContainer = new VboRenderList();
                this.renderChunkFactory = new VboChunkFactory();
            }
            if (flag != this.vboEnabled) {
                this.generateStars();
                this.generateSky();
                this.generateSky2();
            }
            if (this.viewFrustum != null) {
                this.viewFrustum.deleteGlResources();
            }
            this.stopChunkUpdates();
            final Set var5 = this.field_181024_n;
            synchronized (this.field_181024_n) {
                this.field_181024_n.clear();
            }
            // monitorexit(this.field_181024_n)
            this.viewFrustum = new ViewFrustum(this.theWorld, this.mc.gameSettings.renderDistanceChunks, this, this.renderChunkFactory);
            if (this.theWorld != null) {
                final Entity entity = this.mc.getRenderViewEntity();
                if (entity != null) {
                    this.viewFrustum.updateChunkPositions(entity.posX, entity.posZ);
                }
            }
            this.renderEntitiesStartupCounter = 2;
        }
    }
    
    protected void stopChunkUpdates() {
        this.chunksToUpdate.clear();
        this.renderDispatcher.stopChunkUpdates();
    }
    
    public void createBindEntityOutlineFbs(final int p_72720_1_, final int p_72720_2_) {
        if (OpenGlHelper.shadersSupported && this.entityOutlineShader != null) {
            this.entityOutlineShader.createBindFramebuffers(p_72720_1_, p_72720_2_);
        }
    }
    
    public void renderEntities(final Entity renderViewEntity, final ICamera camera, final float partialTicks) {
        int i = 0;
        if (Reflector.MinecraftForgeClient_getRenderPass.exists()) {
            i = Reflector.callInt(Reflector.MinecraftForgeClient_getRenderPass, new Object[0]);
        }
        if (this.renderEntitiesStartupCounter > 0) {
            if (i > 0) {
                return;
            }
            --this.renderEntitiesStartupCounter;
        }
        else {
            final double d0 = renderViewEntity.prevPosX + (renderViewEntity.posX - renderViewEntity.prevPosX) * partialTicks;
            final double d2 = renderViewEntity.prevPosY + (renderViewEntity.posY - renderViewEntity.prevPosY) * partialTicks;
            final double d3 = renderViewEntity.prevPosZ + (renderViewEntity.posZ - renderViewEntity.prevPosZ) * partialTicks;
            this.theWorld.theProfiler.startSection("prepare");
            TileEntityRendererDispatcher.instance.cacheActiveRenderInfo(this.theWorld, this.mc.getTextureManager(), this.mc.fontRendererObj, this.mc.getRenderViewEntity(), partialTicks);
            this.renderManager.cacheActiveRenderInfo(this.theWorld, this.mc.fontRendererObj, this.mc.getRenderViewEntity(), this.mc.pointedEntity, this.mc.gameSettings, partialTicks);
            if (i == 0) {
                this.countEntitiesTotal = 0;
                this.countEntitiesRendered = 0;
                this.countEntitiesHidden = 0;
                this.countTileEntitiesRendered = 0;
            }
            final Entity entity = this.mc.getRenderViewEntity();
            final double d4 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
            final double d5 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
            final double d6 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
            TileEntityRendererDispatcher.staticPlayerX = d4;
            TileEntityRendererDispatcher.staticPlayerY = d5;
            TileEntityRendererDispatcher.staticPlayerZ = d6;
            this.renderManager.setRenderPosition(d4, d5, d6);
            this.mc.entityRenderer.enableLightmap();
            this.theWorld.theProfiler.endStartSection("global");
            final List list = this.theWorld.getLoadedEntityList();
            if (i == 0) {
                this.countEntitiesTotal = list.size();
            }
            if (Config.isFogOff() && this.mc.entityRenderer.fogStandard) {
                GlStateManager.disableFog();
            }
            final boolean flag = Reflector.ForgeEntity_shouldRenderInPass.exists();
            final boolean flag2 = Reflector.ForgeTileEntity_shouldRenderInPass.exists();
            for (int j = 0; j < this.theWorld.weatherEffects.size(); ++j) {
                final Entity entity2 = this.theWorld.weatherEffects.get(j);
                if (!flag || Reflector.callBoolean(entity2, Reflector.ForgeEntity_shouldRenderInPass, i)) {
                    ++this.countEntitiesRendered;
                    if (entity2.isInRangeToRender3d(d0, d2, d3)) {
                        this.renderManager.renderEntitySimple(entity2, partialTicks);
                    }
                }
            }
            if (this.isRenderEntityOutlines()) {
                GlStateManager.depthFunc(519);
                GlStateManager.disableFog();
                this.entityOutlineFramebuffer.framebufferClear();
                this.entityOutlineFramebuffer.bindFramebuffer(false);
                this.theWorld.theProfiler.endStartSection("entityOutlines");
                RenderHelper.disableStandardItemLighting();
                this.renderManager.setRenderOutlines(true);
                for (int k = 0; k < list.size(); ++k) {
                    final Entity entity3 = list.get(k);
                    if (!flag || Reflector.callBoolean(entity3, Reflector.ForgeEntity_shouldRenderInPass, i)) {
                        final boolean flag3 = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();
                        final boolean flag4 = entity3.isInRangeToRender3d(d0, d2, d3) && (entity3.ignoreFrustumCheck || camera.isBoundingBoxInFrustum(entity3.getEntityBoundingBox()) || entity3.riddenByEntity == Minecraft.thePlayer) && entity3 instanceof EntityPlayer;
                        if ((entity3 != this.mc.getRenderViewEntity() || this.mc.gameSettings.thirdPersonView != 0 || flag3) && flag4) {
                            this.renderManager.renderEntitySimple(entity3, partialTicks);
                        }
                    }
                }
                this.renderManager.setRenderOutlines(false);
                RenderHelper.enableStandardItemLighting();
                GlStateManager.depthMask(false);
                this.entityOutlineShader.loadShaderGroup(partialTicks);
                GlStateManager.enableLighting();
                GlStateManager.depthMask(true);
                this.mc.getFramebuffer().bindFramebuffer(false);
                GlStateManager.enableFog();
                GlStateManager.enableBlend();
                GlStateManager.enableColorMaterial();
                GlStateManager.depthFunc(515);
                GlStateManager.enableDepth();
                GlStateManager.enableAlpha();
            }
            this.theWorld.theProfiler.endStartSection("entities");
            final boolean flag5 = Config.isShaders();
            if (flag5) {
                Shaders.beginEntities();
            }
            final Iterator iterator1 = this.renderInfosEntities.iterator();
            final boolean flag6 = this.mc.gameSettings.fancyGraphics;
            this.mc.gameSettings.fancyGraphics = Config.isDroppedItemsFancy();
            while (iterator1.hasNext()) {
                final ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation = iterator1.next();
                final Chunk chunk = this.theWorld.getChunkFromBlockCoords(renderglobal$containerlocalrenderinformation.renderChunk.getPosition());
                final ClassInheritanceMultiMap classinheritancemultimap = chunk.getEntityLists()[renderglobal$containerlocalrenderinformation.renderChunk.getPosition().getY() / 16];
                if (!classinheritancemultimap.isEmpty()) {
                    for (final Entity entity4 : classinheritancemultimap) {
                        if (!flag || Reflector.callBoolean(entity4, Reflector.ForgeEntity_shouldRenderInPass, i)) {
                            final boolean flag7 = this.renderManager.shouldRender(entity4, camera, d0, d2, d3) || entity4.riddenByEntity == Minecraft.thePlayer;
                            if (flag7) {
                                final boolean flag8 = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();
                                if ((entity4 == this.mc.getRenderViewEntity() && this.mc.gameSettings.thirdPersonView == 0 && !flag8) || (entity4.posY >= 0.0 && entity4.posY < 256.0 && !this.theWorld.isBlockLoaded(new BlockPos(entity4)))) {
                                    continue;
                                }
                                ++this.countEntitiesRendered;
                                if (entity4.getClass() == EntityItemFrame.class) {
                                    entity4.renderDistanceWeight = 0.06;
                                }
                                this.renderedEntity = entity4;
                                if (flag5) {
                                    Shaders.nextEntity(entity4);
                                }
                                this.renderManager.renderEntitySimple(entity4, partialTicks);
                                this.renderedEntity = null;
                            }
                            if (flag7 || !(entity4 instanceof EntityWitherSkull)) {
                                continue;
                            }
                            if (flag5) {
                                Shaders.nextEntity(entity4);
                            }
                            this.mc.getRenderManager().renderWitherSkull(entity4, partialTicks);
                        }
                    }
                }
            }
            this.mc.gameSettings.fancyGraphics = flag6;
            final FontRenderer fontrenderer = TileEntityRendererDispatcher.instance.getFontRenderer();
            if (flag5) {
                Shaders.endEntities();
                Shaders.beginBlockEntities();
            }
            this.theWorld.theProfiler.endStartSection("blockentities");
            RenderHelper.enableStandardItemLighting();
            if (Reflector.ForgeTileEntityRendererDispatcher_preDrawBatch.exists()) {
                Reflector.call(TileEntityRendererDispatcher.instance, Reflector.ForgeTileEntityRendererDispatcher_preDrawBatch, new Object[0]);
            }
            for (final Object renderglobal$containerlocalrenderinformation2 : this.renderInfosTileEntities) {
                final ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation3 = (ContainerLocalRenderInformation)renderglobal$containerlocalrenderinformation2;
                final List list2 = renderglobal$containerlocalrenderinformation3.renderChunk.getCompiledChunk().getTileEntities();
                if (!list2.isEmpty()) {
                    for (final TileEntity tileentity : list2) {
                        if (flag2) {
                            if (!Reflector.callBoolean(tileentity, Reflector.ForgeTileEntity_shouldRenderInPass, i)) {
                                continue;
                            }
                            final AxisAlignedBB axisalignedbb = (AxisAlignedBB)Reflector.call(tileentity, Reflector.ForgeTileEntity_getRenderBoundingBox, new Object[0]);
                            if (axisalignedbb != null && !camera.isBoundingBoxInFrustum(axisalignedbb)) {
                                continue;
                            }
                        }
                        final Class oclass = tileentity.getClass();
                        if (oclass == TileEntitySign.class && !Config.zoomMode) {
                            final EntityPlayer entityplayer = Minecraft.thePlayer;
                            final double d7 = tileentity.getDistanceSq(entityplayer.posX, entityplayer.posY, entityplayer.posZ);
                            if (d7 > 256.0) {
                                fontrenderer.enabled = false;
                            }
                        }
                        if (flag5) {
                            Shaders.nextBlockEntity(tileentity);
                        }
                        TileEntityRendererDispatcher.instance.renderTileEntity(tileentity, partialTicks, -1);
                        ++this.countTileEntitiesRendered;
                        fontrenderer.enabled = true;
                    }
                }
            }
            final Set var32 = this.field_181024_n;
            synchronized (this.field_181024_n) {
                for (final Object tileentity2 : this.field_181024_n) {
                    if (flag2) {
                        if (!Reflector.callBoolean(tileentity2, Reflector.ForgeTileEntity_shouldRenderInPass, i)) {
                            continue;
                        }
                        final AxisAlignedBB axisalignedbb2 = (AxisAlignedBB)Reflector.call(tileentity2, Reflector.ForgeTileEntity_getRenderBoundingBox, new Object[0]);
                        if (axisalignedbb2 != null && !camera.isBoundingBoxInFrustum(axisalignedbb2)) {
                            continue;
                        }
                    }
                    final Class oclass2 = tileentity2.getClass();
                    if (oclass2 == TileEntitySign.class && !Config.zoomMode) {
                        final EntityPlayer entityplayer2 = Minecraft.thePlayer;
                        final double d8 = ((TileEntity)tileentity2).getDistanceSq(entityplayer2.posX, entityplayer2.posY, entityplayer2.posZ);
                        if (d8 > 256.0) {
                            fontrenderer.enabled = false;
                        }
                    }
                    if (flag5) {
                        Shaders.nextBlockEntity((TileEntity)tileentity2);
                    }
                    TileEntityRendererDispatcher.instance.renderTileEntity((TileEntity)tileentity2, partialTicks, -1);
                    fontrenderer.enabled = true;
                }
            }
            // monitorexit(this.field_181024_n)
            if (Reflector.ForgeTileEntityRendererDispatcher_drawBatch.exists()) {
                Reflector.call(TileEntityRendererDispatcher.instance, Reflector.ForgeTileEntityRendererDispatcher_drawBatch, i);
            }
            this.preRenderDamagedBlocks();
            for (final Object destroyblockprogress : this.damagedBlocks.values()) {
                BlockPos blockpos = ((DestroyBlockProgress)destroyblockprogress).getPosition();
                TileEntity tileentity3 = this.theWorld.getTileEntity(blockpos);
                if (tileentity3 instanceof TileEntityChest) {
                    final TileEntityChest tileentitychest = (TileEntityChest)tileentity3;
                    if (tileentitychest.adjacentChestXNeg != null) {
                        blockpos = blockpos.offset(EnumFacing.WEST);
                        tileentity3 = this.theWorld.getTileEntity(blockpos);
                    }
                    else if (tileentitychest.adjacentChestZNeg != null) {
                        blockpos = blockpos.offset(EnumFacing.NORTH);
                        tileentity3 = this.theWorld.getTileEntity(blockpos);
                    }
                }
                final Block block = this.theWorld.getBlockState(blockpos).getBlock();
                boolean flag9;
                if (flag2) {
                    flag9 = false;
                    if (tileentity3 != null && Reflector.callBoolean(tileentity3, Reflector.ForgeTileEntity_shouldRenderInPass, i) && Reflector.callBoolean(tileentity3, Reflector.ForgeTileEntity_canRenderBreaking, new Object[0])) {
                        final AxisAlignedBB axisalignedbb3 = (AxisAlignedBB)Reflector.call(tileentity3, Reflector.ForgeTileEntity_getRenderBoundingBox, new Object[0]);
                        if (axisalignedbb3 != null) {
                            flag9 = camera.isBoundingBoxInFrustum(axisalignedbb3);
                        }
                    }
                }
                else {
                    flag9 = (tileentity3 != null && (block instanceof BlockChest || block instanceof BlockEnderChest || block instanceof BlockSign || block instanceof BlockSkull));
                }
                if (flag9) {
                    if (flag5) {
                        Shaders.nextBlockEntity(tileentity3);
                    }
                    TileEntityRendererDispatcher.instance.renderTileEntity(tileentity3, partialTicks, ((DestroyBlockProgress)destroyblockprogress).getPartialBlockDamage());
                }
            }
            this.postRenderDamagedBlocks();
            this.mc.entityRenderer.disableLightmap();
            this.mc.mcProfiler.endSection();
        }
    }
    
    public String getDebugInfoRenders() {
        final int i = this.viewFrustum.renderChunks.length;
        int j = 0;
        for (final Object renderglobal$containerlocalrenderinformation0 : this.renderInfos) {
            final ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation2 = (ContainerLocalRenderInformation)renderglobal$containerlocalrenderinformation0;
            final CompiledChunk compiledchunk = renderglobal$containerlocalrenderinformation2.renderChunk.compiledChunk;
            if (compiledchunk != CompiledChunk.DUMMY && !compiledchunk.isEmpty()) {
                ++j;
            }
        }
        return String.format("C: %d/%d %sD: %d, %s", j, i, this.mc.renderChunksMany ? "(s) " : "", this.renderDistanceChunks, this.renderDispatcher.getDebugInfo());
    }
    
    public String getDebugInfoEntities() {
        return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ", B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered) + ", " + Config.getVersionDebug();
    }
    
    public void setupTerrain(final Entity viewEntity, final double partialTicks, ICamera camera, final int frameCount, final boolean playerSpectator) {
        if (this.mc.gameSettings.renderDistanceChunks != this.renderDistanceChunks) {
            this.loadRenderers();
        }
        this.theWorld.theProfiler.startSection("camera");
        final double d0 = viewEntity.posX - this.frustumUpdatePosX;
        final double d2 = viewEntity.posY - this.frustumUpdatePosY;
        final double d3 = viewEntity.posZ - this.frustumUpdatePosZ;
        if (this.frustumUpdatePosChunkX != viewEntity.chunkCoordX || this.frustumUpdatePosChunkY != viewEntity.chunkCoordY || this.frustumUpdatePosChunkZ != viewEntity.chunkCoordZ || d0 * d0 + d2 * d2 + d3 * d3 > 16.0) {
            this.frustumUpdatePosX = viewEntity.posX;
            this.frustumUpdatePosY = viewEntity.posY;
            this.frustumUpdatePosZ = viewEntity.posZ;
            this.frustumUpdatePosChunkX = viewEntity.chunkCoordX;
            this.frustumUpdatePosChunkY = viewEntity.chunkCoordY;
            this.frustumUpdatePosChunkZ = viewEntity.chunkCoordZ;
            this.viewFrustum.updateChunkPositions(viewEntity.posX, viewEntity.posZ);
        }
        if (Config.isDynamicLights()) {
            DynamicLights.update(this);
        }
        this.theWorld.theProfiler.endStartSection("renderlistcamera");
        final double d4 = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * partialTicks;
        final double d5 = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * partialTicks;
        final double d6 = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * partialTicks;
        this.renderContainer.initialize(d4, d5, d6);
        this.theWorld.theProfiler.endStartSection("cull");
        if (this.debugFixedClippingHelper != null) {
            final Frustum frustum = new Frustum(this.debugFixedClippingHelper);
            frustum.setPosition(this.debugTerrainFrustumPosition.field_181059_a, this.debugTerrainFrustumPosition.field_181060_b, this.debugTerrainFrustumPosition.field_181061_c);
            camera = frustum;
        }
        this.mc.mcProfiler.endStartSection("culling");
        final BlockPos blockpos2 = new BlockPos(d4, d5 + viewEntity.getEyeHeight(), d6);
        final RenderChunk renderchunk = this.viewFrustum.getRenderChunk(blockpos2);
        final BlockPos blockpos3 = new BlockPos(MathHelper.floor_double(d4 / 16.0) * 16, MathHelper.floor_double(d5 / 16.0) * 16, MathHelper.floor_double(d6 / 16.0) * 16);
        this.displayListEntitiesDirty = (this.displayListEntitiesDirty || !this.chunksToUpdate.isEmpty() || viewEntity.posX != this.lastViewEntityX || viewEntity.posY != this.lastViewEntityY || viewEntity.posZ != this.lastViewEntityZ || viewEntity.rotationPitch != this.lastViewEntityPitch || viewEntity.rotationYaw != this.lastViewEntityYaw);
        this.lastViewEntityX = viewEntity.posX;
        this.lastViewEntityY = viewEntity.posY;
        this.lastViewEntityZ = viewEntity.posZ;
        this.lastViewEntityPitch = viewEntity.rotationPitch;
        this.lastViewEntityYaw = viewEntity.rotationYaw;
        final boolean flag = this.debugFixedClippingHelper != null;
        Lagometer.timerVisibility.start();
        if (Shaders.isShadowPass) {
            this.renderInfos = this.renderInfosShadow;
            this.renderInfosEntities = this.renderInfosEntitiesShadow;
            this.renderInfosTileEntities = this.renderInfosTileEntitiesShadow;
            if (!flag && this.displayListEntitiesDirty) {
                this.renderInfos.clear();
                this.renderInfosEntities.clear();
                this.renderInfosTileEntities.clear();
                final RenderInfoLazy renderinfolazy = new RenderInfoLazy();
                final Iterator<RenderChunk> iterator = ShadowUtils.makeShadowChunkIterator(this.theWorld, partialTicks, viewEntity, this.renderDistanceChunks, this.viewFrustum);
                while (iterator.hasNext()) {
                    final RenderChunk renderchunk2 = iterator.next();
                    if (renderchunk2 != null) {
                        renderinfolazy.setRenderChunk(renderchunk2);
                        if (!renderchunk2.compiledChunk.isEmpty() || renderchunk2.isNeedsUpdate()) {
                            this.renderInfos.add(renderinfolazy.getRenderInfo());
                        }
                        final BlockPos blockpos4 = renderchunk2.getPosition();
                        if (ChunkUtils.hasEntities(this.theWorld.getChunkFromBlockCoords(blockpos4))) {
                            this.renderInfosEntities.add(renderinfolazy.getRenderInfo());
                        }
                        if (renderchunk2.getCompiledChunk().getTileEntities().size() <= 0) {
                            continue;
                        }
                        this.renderInfosTileEntities.add(renderinfolazy.getRenderInfo());
                    }
                }
            }
        }
        else {
            this.renderInfos = this.renderInfosNormal;
            this.renderInfosEntities = this.renderInfosEntitiesNormal;
            this.renderInfosTileEntities = this.renderInfosTileEntitiesNormal;
        }
        if (!flag && this.displayListEntitiesDirty && !Shaders.isShadowPass) {
            this.displayListEntitiesDirty = false;
            this.renderInfos.clear();
            this.renderInfosEntities.clear();
            this.renderInfosTileEntities.clear();
            this.visibilityDeque.clear();
            final Deque deque = this.visibilityDeque;
            boolean flag2 = this.mc.renderChunksMany;
            if (renderchunk != null) {
                boolean flag3 = false;
                final ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation3 = new ContainerLocalRenderInformation(renderchunk, null, 0, null);
                final Set set1 = RenderGlobal.SET_ALL_FACINGS;
                if (set1.size() == 1) {
                    final Vector3f vector3f = this.getViewVector(viewEntity, partialTicks);
                    final EnumFacing enumfacing = EnumFacing.getFacingFromVector(vector3f.x, vector3f.y, vector3f.z).getOpposite();
                    set1.remove(enumfacing);
                }
                if (set1.isEmpty()) {
                    flag3 = true;
                }
                if (flag3 && !playerSpectator) {
                    this.renderInfos.add(renderglobal$containerlocalrenderinformation3);
                }
                else {
                    if (playerSpectator && this.theWorld.getBlockState(blockpos2).getBlock().isOpaqueCube()) {
                        flag2 = false;
                    }
                    renderchunk.setFrameIndex(frameCount);
                    deque.add(renderglobal$containerlocalrenderinformation3);
                }
            }
            else {
                final int i = (blockpos2.getY() > 0) ? 248 : 8;
                for (int j = -this.renderDistanceChunks; j <= this.renderDistanceChunks; ++j) {
                    for (int k = -this.renderDistanceChunks; k <= this.renderDistanceChunks; ++k) {
                        final RenderChunk renderchunk3 = this.viewFrustum.getRenderChunk(new BlockPos((j << 4) + 8, i, (k << 4) + 8));
                        if (renderchunk3 != null && camera.isBoundingBoxInFrustum(renderchunk3.boundingBox)) {
                            renderchunk3.setFrameIndex(frameCount);
                            deque.add(new ContainerLocalRenderInformation(renderchunk3, null, 0, null));
                        }
                    }
                }
            }
            final EnumFacing[] aenumfacing = EnumFacing.VALUES;
            final int l = aenumfacing.length;
            while (!deque.isEmpty()) {
                final ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation4 = deque.poll();
                final RenderChunk renderchunk4 = renderglobal$containerlocalrenderinformation4.renderChunk;
                final EnumFacing enumfacing2 = renderglobal$containerlocalrenderinformation4.facing;
                final BlockPos blockpos5 = renderchunk4.getPosition();
                if (!renderchunk4.compiledChunk.isEmpty() || renderchunk4.isNeedsUpdate()) {
                    this.renderInfos.add(renderglobal$containerlocalrenderinformation4);
                }
                if (ChunkUtils.hasEntities(this.theWorld.getChunkFromBlockCoords(blockpos5))) {
                    this.renderInfosEntities.add(renderglobal$containerlocalrenderinformation4);
                }
                if (renderchunk4.getCompiledChunk().getTileEntities().size() > 0) {
                    this.renderInfosTileEntities.add(renderglobal$containerlocalrenderinformation4);
                }
                for (final EnumFacing enumfacing3 : aenumfacing) {
                    if ((!flag2 || !renderglobal$containerlocalrenderinformation4.setFacing.contains(enumfacing3.getOpposite())) && (!flag2 || enumfacing2 == null || renderchunk4.getCompiledChunk().isVisible(enumfacing2.getOpposite(), enumfacing3))) {
                        final RenderChunk renderchunk5 = this.func_181562_a(blockpos2, renderchunk4, enumfacing3);
                        if (renderchunk5 != null && renderchunk5.setFrameIndex(frameCount) && camera.isBoundingBoxInFrustum(renderchunk5.boundingBox)) {
                            final ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation5 = new ContainerLocalRenderInformation(renderchunk5, enumfacing3, renderglobal$containerlocalrenderinformation4.counter + 1, null);
                            renderglobal$containerlocalrenderinformation5.setFacing.addAll(renderglobal$containerlocalrenderinformation4.setFacing);
                            renderglobal$containerlocalrenderinformation5.setFacing.add(enumfacing3);
                            deque.add(renderglobal$containerlocalrenderinformation5);
                        }
                    }
                }
            }
        }
        if (this.debugFixTerrainFrustum) {
            this.fixTerrainFrustum(d4, d5, d6);
            this.debugFixTerrainFrustum = false;
        }
        Lagometer.timerVisibility.end();
        if (Shaders.isShadowPass) {
            Shaders.mcProfilerEndSection();
        }
        else {
            this.renderDispatcher.clearChunkUpdates();
            final Set set2 = this.chunksToUpdate;
            this.chunksToUpdate = Sets.newLinkedHashSet();
            final Iterator iterator2 = this.renderInfos.iterator();
            Lagometer.timerChunkUpdate.start();
            while (iterator2.hasNext()) {
                final ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation6 = iterator2.next();
                final RenderChunk renderchunk6 = renderglobal$containerlocalrenderinformation6.renderChunk;
                if (renderchunk6.isNeedsUpdate() || set2.contains(renderchunk6)) {
                    this.displayListEntitiesDirty = true;
                    if (this.isPositionInRenderChunk(blockpos3, renderglobal$containerlocalrenderinformation6.renderChunk)) {
                        if (!renderchunk6.isPlayerUpdate()) {
                            this.chunksToUpdateForced.add(renderchunk6);
                        }
                        else {
                            this.mc.mcProfiler.startSection("build near");
                            this.renderDispatcher.updateChunkNow(renderchunk6);
                            renderchunk6.setNeedsUpdate(false);
                            this.mc.mcProfiler.endSection();
                        }
                    }
                    else {
                        this.chunksToUpdate.add(renderchunk6);
                    }
                }
            }
            Lagometer.timerChunkUpdate.end();
            this.chunksToUpdate.addAll(set2);
            this.mc.mcProfiler.endSection();
        }
    }
    
    private boolean isPositionInRenderChunk(final BlockPos pos, final RenderChunk renderChunkIn) {
        final BlockPos blockpos = renderChunkIn.getPosition();
        return MathHelper.abs_int(pos.getX() - blockpos.getX()) <= 16 && MathHelper.abs_int(pos.getY() - blockpos.getY()) <= 16 && MathHelper.abs_int(pos.getZ() - blockpos.getZ()) <= 16;
    }
    
    private Set getVisibleFacings(final BlockPos pos) {
        final VisGraph visgraph = new VisGraph();
        final BlockPos blockpos = new BlockPos(pos.getX() >> 4 << 4, pos.getY() >> 4 << 4, pos.getZ() >> 4 << 4);
        final Chunk chunk = this.theWorld.getChunkFromBlockCoords(blockpos);
        for (final BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(blockpos, blockpos.add(15, 15, 15))) {
            if (chunk.getBlock(blockpos$mutableblockpos).isOpaqueCube()) {
                visgraph.func_178606_a(blockpos$mutableblockpos);
            }
        }
        return visgraph.func_178609_b(pos);
    }
    
    private RenderChunk func_181562_a(final BlockPos p_181562_1_, final RenderChunk p_181562_2_, final EnumFacing p_181562_3_) {
        final BlockPos blockpos = p_181562_2_.getPositionOffset16(p_181562_3_);
        if (blockpos.getY() >= 0 && blockpos.getY() < 256) {
            final int i = MathHelper.abs_int(p_181562_1_.getX() - blockpos.getX());
            final int j = MathHelper.abs_int(p_181562_1_.getZ() - blockpos.getZ());
            if (Config.isFogOff()) {
                if (i > this.renderDistance || j > this.renderDistance) {
                    return null;
                }
            }
            else {
                final int k = i * i + j * j;
                if (k > this.renderDistanceSq) {
                    return null;
                }
            }
            return this.viewFrustum.getRenderChunk(blockpos);
        }
        return null;
    }
    
    private void fixTerrainFrustum(final double x, final double y, final double z) {
        this.debugFixedClippingHelper = new ClippingHelperImpl();
        ((ClippingHelperImpl)this.debugFixedClippingHelper).init();
        final Matrix4f matrix4f = new Matrix4f(this.debugFixedClippingHelper.modelviewMatrix);
        matrix4f.transpose();
        final Matrix4f matrix4f2 = new Matrix4f(this.debugFixedClippingHelper.projectionMatrix);
        matrix4f2.transpose();
        final Matrix4f matrix4f3 = new Matrix4f();
        org.lwjgl.util.vector.Matrix4f.mul(matrix4f2, matrix4f, matrix4f3);
        matrix4f3.invert();
        this.debugTerrainFrustumPosition.field_181059_a = x;
        this.debugTerrainFrustumPosition.field_181060_b = y;
        this.debugTerrainFrustumPosition.field_181061_c = z;
        this.debugTerrainMatrix[0] = new Vector4f(-1.0f, -1.0f, -1.0f, 1.0f);
        this.debugTerrainMatrix[1] = new Vector4f(1.0f, -1.0f, -1.0f, 1.0f);
        this.debugTerrainMatrix[2] = new Vector4f(1.0f, 1.0f, -1.0f, 1.0f);
        this.debugTerrainMatrix[3] = new Vector4f(-1.0f, 1.0f, -1.0f, 1.0f);
        this.debugTerrainMatrix[4] = new Vector4f(-1.0f, -1.0f, 1.0f, 1.0f);
        this.debugTerrainMatrix[5] = new Vector4f(1.0f, -1.0f, 1.0f, 1.0f);
        this.debugTerrainMatrix[6] = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.debugTerrainMatrix[7] = new Vector4f(-1.0f, 1.0f, 1.0f, 1.0f);
        for (int i = 0; i < 8; ++i) {
            org.lwjgl.util.vector.Matrix4f.transform(matrix4f3, this.debugTerrainMatrix[i], this.debugTerrainMatrix[i]);
            final Vector4f vector4f = this.debugTerrainMatrix[i];
            vector4f.x /= this.debugTerrainMatrix[i].w;
            final Vector4f vector4f2 = this.debugTerrainMatrix[i];
            vector4f2.y /= this.debugTerrainMatrix[i].w;
            final Vector4f vector4f3 = this.debugTerrainMatrix[i];
            vector4f3.z /= this.debugTerrainMatrix[i].w;
            this.debugTerrainMatrix[i].w = 1.0f;
        }
    }
    
    protected Vector3f getViewVector(final Entity entityIn, final double partialTicks) {
        float f = (float)(entityIn.prevRotationPitch + (entityIn.rotationPitch - entityIn.prevRotationPitch) * partialTicks);
        final float f2 = (float)(entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks);
        if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
            f += 180.0f;
        }
        final float f3 = MathHelper.cos(-f2 * 0.017453292f - 3.1415927f);
        final float f4 = MathHelper.sin(-f2 * 0.017453292f - 3.1415927f);
        final float f5 = -MathHelper.cos(-f * 0.017453292f);
        final float f6 = MathHelper.sin(-f * 0.017453292f);
        return new Vector3f(f4 * f5, f6, f3 * f5);
    }
    
    public int renderBlockLayer(final EnumWorldBlockLayer blockLayerIn, final double partialTicks, final int pass, final Entity entityIn) {
        RenderHelper.disableStandardItemLighting();
        if (blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT) {
            this.mc.mcProfiler.startSection("translucent_sort");
            final double d0 = entityIn.posX - this.prevRenderSortX;
            final double d2 = entityIn.posY - this.prevRenderSortY;
            final double d3 = entityIn.posZ - this.prevRenderSortZ;
            if (d0 * d0 + d2 * d2 + d3 * d3 > 1.0) {
                this.prevRenderSortX = entityIn.posX;
                this.prevRenderSortY = entityIn.posY;
                this.prevRenderSortZ = entityIn.posZ;
                int k = 0;
                final Iterator iterator = this.renderInfos.iterator();
                this.chunksToResortTransparency.clear();
                while (iterator.hasNext()) {
                    final ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation = iterator.next();
                    if (renderglobal$containerlocalrenderinformation.renderChunk.compiledChunk.isLayerStarted(blockLayerIn) && k++ < 15) {
                        this.chunksToResortTransparency.add(renderglobal$containerlocalrenderinformation.renderChunk);
                    }
                }
            }
            this.mc.mcProfiler.endSection();
        }
        this.mc.mcProfiler.startSection("filterempty");
        int l = 0;
        final boolean flag = blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT;
        final int i1 = flag ? (this.renderInfos.size() - 1) : 0;
        for (int j = flag ? -1 : this.renderInfos.size(), j2 = flag ? -1 : 1, m = i1; m != j; m += j2) {
            final RenderChunk renderchunk = this.renderInfos.get(m).renderChunk;
            if (!renderchunk.getCompiledChunk().isLayerEmpty(blockLayerIn)) {
                ++l;
                this.renderContainer.addRenderChunk(renderchunk, blockLayerIn);
            }
        }
        if (l == 0) {
            this.mc.mcProfiler.endSection();
            return l;
        }
        if (Config.isFogOff() && this.mc.entityRenderer.fogStandard) {
            GlStateManager.disableFog();
        }
        this.mc.mcProfiler.endStartSection("render_" + blockLayerIn);
        this.renderBlockLayer(blockLayerIn);
        this.mc.mcProfiler.endSection();
        return l;
    }
    
    private void renderBlockLayer(final EnumWorldBlockLayer blockLayerIn) {
        this.mc.entityRenderer.enableLightmap();
        if (OpenGlHelper.useVbo()) {
            GL11.glEnableClientState(32884);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
            GL11.glEnableClientState(32888);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glEnableClientState(32888);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
            GL11.glEnableClientState(32886);
        }
        if (Config.isShaders()) {
            ShadersRender.preRenderChunkLayer(blockLayerIn);
        }
        this.renderContainer.renderChunkLayer(blockLayerIn);
        if (Config.isShaders()) {
            ShadersRender.postRenderChunkLayer(blockLayerIn);
        }
        if (OpenGlHelper.useVbo()) {
            for (final VertexFormatElement vertexformatelement : DefaultVertexFormats.BLOCK.getElements()) {
                final VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
                final int i = vertexformatelement.getIndex();
                switch (RenderGlobal$2.field_178037_a[vertexformatelement$enumusage.ordinal()]) {
                    default: {
                        continue;
                    }
                    case 1: {
                        GL11.glDisableClientState(32884);
                        continue;
                    }
                    case 2: {
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + i);
                        GL11.glDisableClientState(32888);
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                        continue;
                    }
                    case 3: {
                        GL11.glDisableClientState(32886);
                        GlStateManager.resetColor();
                        continue;
                    }
                }
            }
        }
        this.mc.entityRenderer.disableLightmap();
    }
    
    private void cleanupDamagedBlocks(final Iterator iteratorIn) {
        while (iteratorIn.hasNext()) {
            final DestroyBlockProgress destroyblockprogress = iteratorIn.next();
            final int i = destroyblockprogress.getCreationCloudUpdateTick();
            if (this.cloudTickCounter - i > 400) {
                iteratorIn.remove();
            }
        }
    }
    
    public void updateClouds() {
        if (Config.isShaders() && Keyboard.isKeyDown(61) && Keyboard.isKeyDown(19)) {
            Shaders.uninit();
            Shaders.loadShaderPack();
        }
        ++this.cloudTickCounter;
        if (this.cloudTickCounter % 20 == 0) {
            this.cleanupDamagedBlocks(this.damagedBlocks.values().iterator());
        }
    }
    
    private void renderSkyEnd() {
        if (Config.isSkyEnabled()) {
            GlStateManager.disableFog();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.depthMask(false);
            this.renderEngine.bindTexture(RenderGlobal.locationEndSkyPng);
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            for (int i = 0; i < 6; ++i) {
                GlStateManager.pushMatrix();
                if (i == 1) {
                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                }
                if (i == 2) {
                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                }
                if (i == 3) {
                    GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
                }
                if (i == 4) {
                    GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
                }
                if (i == 5) {
                    GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
                }
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldrenderer.pos(-100.0, -100.0, -100.0).tex(0.0, 0.0).color(40, 40, 40, 255).endVertex();
                worldrenderer.pos(-100.0, -100.0, 100.0).tex(0.0, 16.0).color(40, 40, 40, 255).endVertex();
                worldrenderer.pos(100.0, -100.0, 100.0).tex(16.0, 16.0).color(40, 40, 40, 255).endVertex();
                worldrenderer.pos(100.0, -100.0, -100.0).tex(16.0, 0.0).color(40, 40, 40, 255).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.enableAlpha();
        }
    }
    
    public void renderSky(final float partialTicks, final int pass) {
        if (Reflector.ForgeWorldProvider_getSkyRenderer.exists()) {
            final WorldProvider worldprovider = this.mc.theWorld.provider;
            final Object object = Reflector.call(worldprovider, Reflector.ForgeWorldProvider_getSkyRenderer, new Object[0]);
            if (object != null) {
                Reflector.callVoid(object, Reflector.IRenderHandler_render, partialTicks, this.theWorld, this.mc);
                return;
            }
        }
        if (this.mc.theWorld.provider.getDimensionId() == 1) {
            this.renderSkyEnd();
        }
        else if (this.mc.theWorld.provider.isSurfaceWorld()) {
            GlStateManager.disableTexture2D();
            final boolean flag1 = Config.isShaders();
            if (flag1) {
                Shaders.disableTexture2D();
            }
            Vec3 vec3 = this.theWorld.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
            vec3 = CustomColors.getSkyColor(vec3, this.mc.theWorld, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0, this.mc.getRenderViewEntity().posZ);
            if (flag1) {
                Shaders.setSkyColor(vec3);
            }
            float f = (float)vec3.xCoord;
            float f2 = (float)vec3.yCoord;
            float f3 = (float)vec3.zCoord;
            if (pass != 2) {
                final float f4 = (f * 30.0f + f2 * 59.0f + f3 * 11.0f) / 100.0f;
                final float f5 = (f * 30.0f + f2 * 70.0f) / 100.0f;
                final float f6 = (f * 30.0f + f3 * 70.0f) / 100.0f;
                f = f4;
                f2 = f5;
                f3 = f6;
            }
            GlStateManager.color(f, f2, f3);
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            GlStateManager.depthMask(false);
            GlStateManager.enableFog();
            if (flag1) {
                Shaders.enableFog();
            }
            GlStateManager.color(f, f2, f3);
            if (flag1) {
                Shaders.preSkyList();
            }
            if (Config.isSkyEnabled()) {
                if (this.vboEnabled) {
                    this.skyVBO.bindBuffer();
                    GL11.glEnableClientState(32884);
                    GL11.glVertexPointer(3, 5126, 12, 0L);
                    this.skyVBO.drawArrays(7);
                    this.skyVBO.unbindBuffer();
                    GL11.glDisableClientState(32884);
                }
                else {
                    GlStateManager.callList(this.glSkyList);
                }
            }
            GlStateManager.disableFog();
            if (flag1) {
                Shaders.disableFog();
            }
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.disableStandardItemLighting();
            final float[] afloat = this.theWorld.provider.calcSunriseSunsetColors(this.theWorld.getCelestialAngle(partialTicks), partialTicks);
            if (afloat != null && Config.isSunMoonEnabled()) {
                GlStateManager.disableTexture2D();
                if (flag1) {
                    Shaders.disableTexture2D();
                }
                GlStateManager.shadeModel(7425);
                GlStateManager.pushMatrix();
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate((MathHelper.sin(this.theWorld.getCelestialAngleRadians(partialTicks)) < 0.0f) ? 180.0f : 0.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
                float f7 = afloat[0];
                float f8 = afloat[1];
                float f9 = afloat[2];
                if (pass != 2) {
                    final float f10 = (f7 * 30.0f + f8 * 59.0f + f9 * 11.0f) / 100.0f;
                    final float f11 = (f7 * 30.0f + f8 * 70.0f) / 100.0f;
                    final float f12 = (f7 * 30.0f + f9 * 70.0f) / 100.0f;
                    f7 = f10;
                    f8 = f11;
                    f9 = f12;
                }
                worldrenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
                worldrenderer.pos(0.0, 100.0, 0.0).color(f7, f8, f9, afloat[3]).endVertex();
                final boolean flag2 = true;
                for (int i = 0; i <= 16; ++i) {
                    final float f13 = i * 3.1415927f * 2.0f / 16.0f;
                    final float f14 = MathHelper.sin(f13);
                    final float f15 = MathHelper.cos(f13);
                    worldrenderer.pos(f14 * 120.0f, f15 * 120.0f, -f15 * 40.0f * afloat[3]).color(afloat[0], afloat[1], afloat[2], 0.0f).endVertex();
                }
                tessellator.draw();
                GlStateManager.popMatrix();
                GlStateManager.shadeModel(7424);
            }
            GlStateManager.enableTexture2D();
            if (flag1) {
                Shaders.enableTexture2D();
            }
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            GlStateManager.pushMatrix();
            final float f16 = 1.0f - this.theWorld.getRainStrength(partialTicks);
            GlStateManager.color(1.0f, 1.0f, 1.0f, f16);
            GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
            CustomSky.renderSky(this.theWorld, this.renderEngine, this.theWorld.getCelestialAngle(partialTicks), f16);
            if (flag1) {
                Shaders.preCelestialRotate();
            }
            GlStateManager.rotate(this.theWorld.getCelestialAngle(partialTicks) * 360.0f, 1.0f, 0.0f, 0.0f);
            if (flag1) {
                Shaders.postCelestialRotate();
            }
            float f17 = 30.0f;
            if (Config.isSunTexture()) {
                this.renderEngine.bindTexture(RenderGlobal.locationSunPng);
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
                worldrenderer.pos(-f17, 100.0, -f17).tex(0.0, 0.0).endVertex();
                worldrenderer.pos(f17, 100.0, -f17).tex(1.0, 0.0).endVertex();
                worldrenderer.pos(f17, 100.0, f17).tex(1.0, 1.0).endVertex();
                worldrenderer.pos(-f17, 100.0, f17).tex(0.0, 1.0).endVertex();
                tessellator.draw();
            }
            f17 = 20.0f;
            if (Config.isMoonTexture()) {
                this.renderEngine.bindTexture(RenderGlobal.locationMoonPhasesPng);
                final int l = this.theWorld.getMoonPhase();
                final int j = l % 4;
                final int k = l / 4 % 2;
                final float f18 = (j + 0) / 4.0f;
                final float f19 = (k + 0) / 2.0f;
                final float f20 = (j + 1) / 4.0f;
                final float f21 = (k + 1) / 2.0f;
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
                worldrenderer.pos(-f17, -100.0, f17).tex(f20, f21).endVertex();
                worldrenderer.pos(f17, -100.0, f17).tex(f18, f21).endVertex();
                worldrenderer.pos(f17, -100.0, -f17).tex(f18, f19).endVertex();
                worldrenderer.pos(-f17, -100.0, -f17).tex(f20, f19).endVertex();
                tessellator.draw();
            }
            GlStateManager.disableTexture2D();
            if (flag1) {
                Shaders.disableTexture2D();
            }
            final float f22 = this.theWorld.getStarBrightness(partialTicks) * f16;
            if (f22 > 0.0f && Config.isStarsEnabled() && !CustomSky.hasSkyLayers(this.theWorld)) {
                GlStateManager.color(f22, f22, f22, f22);
                if (this.vboEnabled) {
                    this.starVBO.bindBuffer();
                    GL11.glEnableClientState(32884);
                    GL11.glVertexPointer(3, 5126, 12, 0L);
                    this.starVBO.drawArrays(7);
                    this.starVBO.unbindBuffer();
                    GL11.glDisableClientState(32884);
                }
                else {
                    GlStateManager.callList(this.starGLCallList);
                }
            }
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableFog();
            if (flag1) {
                Shaders.enableFog();
            }
            GlStateManager.popMatrix();
            GlStateManager.disableTexture2D();
            if (flag1) {
                Shaders.disableTexture2D();
            }
            GlStateManager.color(0.0f, 0.0f, 0.0f);
            final double d0 = Minecraft.thePlayer.getPositionEyes(partialTicks).yCoord - this.theWorld.getHorizon();
            if (d0 < 0.0) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.0f, 12.0f, 0.0f);
                if (this.vboEnabled) {
                    this.sky2VBO.bindBuffer();
                    GL11.glEnableClientState(32884);
                    GL11.glVertexPointer(3, 5126, 12, 0L);
                    this.sky2VBO.drawArrays(7);
                    this.sky2VBO.unbindBuffer();
                    GL11.glDisableClientState(32884);
                }
                else {
                    GlStateManager.callList(this.glSkyList2);
                }
                GlStateManager.popMatrix();
                final float f23 = 1.0f;
                final float f24 = -(float)(d0 + 65.0);
                final float f25 = -1.0f;
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                worldrenderer.pos(-1.0, f24, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0, f24, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0, f24, -1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0, f24, -1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0, f24, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0, f24, -1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0, f24, -1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0, f24, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                worldrenderer.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
            }
            if (this.theWorld.provider.isSkyColored()) {
                GlStateManager.color(f * 0.2f + 0.04f, f2 * 0.2f + 0.04f, f3 * 0.6f + 0.1f);
            }
            else {
                GlStateManager.color(f, f2, f3);
            }
            if (this.mc.gameSettings.renderDistanceChunks <= 4) {
                GlStateManager.color(this.mc.entityRenderer.fogColorRed, this.mc.entityRenderer.fogColorGreen, this.mc.entityRenderer.fogColorBlue);
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, -(float)(d0 - 16.0), 0.0f);
            if (Config.isSkyEnabled()) {
                GlStateManager.callList(this.glSkyList2);
            }
            GlStateManager.popMatrix();
            GlStateManager.enableTexture2D();
            if (flag1) {
                Shaders.enableTexture2D();
            }
            GlStateManager.depthMask(true);
        }
    }
    
    public void renderClouds(float partialTicks, final int pass) {
        if (!Config.isCloudsOff()) {
            if (Reflector.ForgeWorldProvider_getCloudRenderer.exists()) {
                final WorldProvider worldprovider = this.mc.theWorld.provider;
                final Object object = Reflector.call(worldprovider, Reflector.ForgeWorldProvider_getCloudRenderer, new Object[0]);
                if (object != null) {
                    Reflector.callVoid(object, Reflector.IRenderHandler_render, partialTicks, this.theWorld, this.mc);
                    return;
                }
            }
            if (this.mc.theWorld.provider.isSurfaceWorld()) {
                if (Config.isShaders()) {
                    Shaders.beginClouds();
                }
                if (Config.isCloudsFancy()) {
                    this.renderCloudsFancy(partialTicks, pass);
                }
                else {
                    this.cloudRenderer.prepareToRender(false, this.cloudTickCounter, partialTicks);
                    partialTicks = 0.0f;
                    GlStateManager.disableCull();
                    final float f9 = (float)(this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().posY - this.mc.getRenderViewEntity().lastTickPosY) * partialTicks);
                    final boolean flag = true;
                    final boolean flag2 = true;
                    final Tessellator tessellator = Tessellator.getInstance();
                    final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    this.renderEngine.bindTexture(RenderGlobal.locationCloudsPng);
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    if (this.cloudRenderer.shouldUpdateGlList()) {
                        this.cloudRenderer.startUpdateGlList();
                        final Vec3 vec3 = this.theWorld.getCloudColour(partialTicks);
                        float f10 = (float)vec3.xCoord;
                        float f11 = (float)vec3.yCoord;
                        float f12 = (float)vec3.zCoord;
                        if (pass != 2) {
                            final float f13 = (f10 * 30.0f + f11 * 59.0f + f12 * 11.0f) / 100.0f;
                            final float f14 = (f10 * 30.0f + f11 * 70.0f) / 100.0f;
                            final float f15 = (f10 * 30.0f + f12 * 70.0f) / 100.0f;
                            f10 = f13;
                            f11 = f14;
                            f12 = f15;
                        }
                        final float f16 = 4.8828125E-4f;
                        final double d2 = this.cloudTickCounter + partialTicks;
                        double d3 = this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().posX - this.mc.getRenderViewEntity().prevPosX) * partialTicks + d2 * 0.029999999329447746;
                        double d4 = this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().posZ - this.mc.getRenderViewEntity().prevPosZ) * partialTicks;
                        final int i = MathHelper.floor_double(d3 / 2048.0);
                        final int j = MathHelper.floor_double(d4 / 2048.0);
                        d3 -= i * 2048;
                        d4 -= j * 2048;
                        float f17 = this.theWorld.provider.getCloudHeight() - f9 + 0.33f;
                        f17 += this.mc.gameSettings.ofCloudsHeight * 128.0f;
                        final float f18 = (float)(d3 * 4.8828125E-4);
                        final float f19 = (float)(d4 * 4.8828125E-4);
                        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                        for (int k = -256; k < 256; k += 32) {
                            for (int l = -256; l < 256; l += 32) {
                                worldrenderer.pos(k + 0, f17, l + 32).tex((k + 0) * 4.8828125E-4f + f18, (l + 32) * 4.8828125E-4f + f19).color(f10, f11, f12, 0.8f).endVertex();
                                worldrenderer.pos(k + 32, f17, l + 32).tex((k + 32) * 4.8828125E-4f + f18, (l + 32) * 4.8828125E-4f + f19).color(f10, f11, f12, 0.8f).endVertex();
                                worldrenderer.pos(k + 32, f17, l + 0).tex((k + 32) * 4.8828125E-4f + f18, (l + 0) * 4.8828125E-4f + f19).color(f10, f11, f12, 0.8f).endVertex();
                                worldrenderer.pos(k + 0, f17, l + 0).tex((k + 0) * 4.8828125E-4f + f18, (l + 0) * 4.8828125E-4f + f19).color(f10, f11, f12, 0.8f).endVertex();
                            }
                        }
                        tessellator.draw();
                        this.cloudRenderer.endUpdateGlList();
                    }
                    this.cloudRenderer.renderGlList();
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    GlStateManager.disableBlend();
                    GlStateManager.enableCull();
                }
                if (Config.isShaders()) {
                    Shaders.endClouds();
                }
            }
        }
    }
    
    public boolean hasCloudFog(final double x, final double y, final double z, final float partialTicks) {
        return false;
    }
    
    private void renderCloudsFancy(float partialTicks, final int pass) {
        this.cloudRenderer.prepareToRender(true, this.cloudTickCounter, partialTicks);
        partialTicks = 0.0f;
        GlStateManager.disableCull();
        final float f = (float)(this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().posY - this.mc.getRenderViewEntity().lastTickPosY) * partialTicks);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        final float f2 = 12.0f;
        final float f3 = 4.0f;
        final double d0 = this.cloudTickCounter + partialTicks;
        double d2 = (this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().posX - this.mc.getRenderViewEntity().prevPosX) * partialTicks + d0 * 0.029999999329447746) / 12.0;
        double d3 = (this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().posZ - this.mc.getRenderViewEntity().prevPosZ) * partialTicks) / 12.0 + 0.33000001311302185;
        float f4 = this.theWorld.provider.getCloudHeight() - f + 0.33f;
        f4 += this.mc.gameSettings.ofCloudsHeight * 128.0f;
        final int i = MathHelper.floor_double(d2 / 2048.0);
        final int j = MathHelper.floor_double(d3 / 2048.0);
        d2 -= i * 2048;
        d3 -= j * 2048;
        this.renderEngine.bindTexture(RenderGlobal.locationCloudsPng);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        final Vec3 vec3 = this.theWorld.getCloudColour(partialTicks);
        float f5 = (float)vec3.xCoord;
        float f6 = (float)vec3.yCoord;
        float f7 = (float)vec3.zCoord;
        if (pass != 2) {
            final float f8 = (f5 * 30.0f + f6 * 59.0f + f7 * 11.0f) / 100.0f;
            final float f9 = (f5 * 30.0f + f6 * 70.0f) / 100.0f;
            final float f10 = (f5 * 30.0f + f7 * 70.0f) / 100.0f;
            f5 = f8;
            f6 = f9;
            f7 = f10;
        }
        final float f11 = f5 * 0.9f;
        final float f12 = f6 * 0.9f;
        final float f13 = f7 * 0.9f;
        final float f14 = f5 * 0.7f;
        final float f15 = f6 * 0.7f;
        final float f16 = f7 * 0.7f;
        final float f17 = f5 * 0.8f;
        final float f18 = f6 * 0.8f;
        final float f19 = f7 * 0.8f;
        final float f20 = 0.00390625f;
        final float f21 = MathHelper.floor_double(d2) * 0.00390625f;
        final float f22 = MathHelper.floor_double(d3) * 0.00390625f;
        final float f23 = (float)(d2 - MathHelper.floor_double(d2));
        final float f24 = (float)(d3 - MathHelper.floor_double(d3));
        final boolean flag = true;
        final boolean flag2 = true;
        final float f25 = 9.765625E-4f;
        GlStateManager.scale(12.0f, 1.0f, 12.0f);
        for (int k = 0; k < 2; ++k) {
            if (k == 0) {
                GlStateManager.colorMask(false, false, false, false);
            }
            else {
                switch (pass) {
                    case 0: {
                        GlStateManager.colorMask(false, true, true, true);
                        break;
                    }
                    case 1: {
                        GlStateManager.colorMask(true, false, false, true);
                        break;
                    }
                    case 2: {
                        GlStateManager.colorMask(true, true, true, true);
                        break;
                    }
                }
            }
            this.cloudRenderer.renderGlList();
        }
        if (this.cloudRenderer.shouldUpdateGlList()) {
            this.cloudRenderer.startUpdateGlList();
            for (int j2 = -3; j2 <= 4; ++j2) {
                for (int l = -3; l <= 4; ++l) {
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
                    final float f26 = (float)(j2 * 8);
                    final float f27 = (float)(l * 8);
                    final float f28 = f26 - f23;
                    final float f29 = f27 - f24;
                    if (f4 > -5.0f) {
                        worldrenderer.pos(f28 + 0.0f, f4 + 0.0f, f29 + 8.0f).tex((f26 + 0.0f) * 0.00390625f + f21, (f27 + 8.0f) * 0.00390625f + f22).color(f14, f15, f16, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                        worldrenderer.pos(f28 + 8.0f, f4 + 0.0f, f29 + 8.0f).tex((f26 + 8.0f) * 0.00390625f + f21, (f27 + 8.0f) * 0.00390625f + f22).color(f14, f15, f16, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                        worldrenderer.pos(f28 + 8.0f, f4 + 0.0f, f29 + 0.0f).tex((f26 + 8.0f) * 0.00390625f + f21, (f27 + 0.0f) * 0.00390625f + f22).color(f14, f15, f16, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                        worldrenderer.pos(f28 + 0.0f, f4 + 0.0f, f29 + 0.0f).tex((f26 + 0.0f) * 0.00390625f + f21, (f27 + 0.0f) * 0.00390625f + f22).color(f14, f15, f16, 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
                    }
                    if (f4 <= 5.0f) {
                        worldrenderer.pos(f28 + 0.0f, f4 + 4.0f - 9.765625E-4f, f29 + 8.0f).tex((f26 + 0.0f) * 0.00390625f + f21, (f27 + 8.0f) * 0.00390625f + f22).color(f5, f6, f7, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                        worldrenderer.pos(f28 + 8.0f, f4 + 4.0f - 9.765625E-4f, f29 + 8.0f).tex((f26 + 8.0f) * 0.00390625f + f21, (f27 + 8.0f) * 0.00390625f + f22).color(f5, f6, f7, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                        worldrenderer.pos(f28 + 8.0f, f4 + 4.0f - 9.765625E-4f, f29 + 0.0f).tex((f26 + 8.0f) * 0.00390625f + f21, (f27 + 0.0f) * 0.00390625f + f22).color(f5, f6, f7, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                        worldrenderer.pos(f28 + 0.0f, f4 + 4.0f - 9.765625E-4f, f29 + 0.0f).tex((f26 + 0.0f) * 0.00390625f + f21, (f27 + 0.0f) * 0.00390625f + f22).color(f5, f6, f7, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
                    }
                    if (j2 > -1) {
                        for (int i2 = 0; i2 < 8; ++i2) {
                            worldrenderer.pos(f28 + i2 + 0.0f, f4 + 0.0f, f29 + 8.0f).tex((f26 + i2 + 0.5f) * 0.00390625f + f21, (f27 + 8.0f) * 0.00390625f + f22).color(f11, f12, f13, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                            worldrenderer.pos(f28 + i2 + 0.0f, f4 + 4.0f, f29 + 8.0f).tex((f26 + i2 + 0.5f) * 0.00390625f + f21, (f27 + 8.0f) * 0.00390625f + f22).color(f11, f12, f13, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                            worldrenderer.pos(f28 + i2 + 0.0f, f4 + 4.0f, f29 + 0.0f).tex((f26 + i2 + 0.5f) * 0.00390625f + f21, (f27 + 0.0f) * 0.00390625f + f22).color(f11, f12, f13, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                            worldrenderer.pos(f28 + i2 + 0.0f, f4 + 0.0f, f29 + 0.0f).tex((f26 + i2 + 0.5f) * 0.00390625f + f21, (f27 + 0.0f) * 0.00390625f + f22).color(f11, f12, f13, 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
                        }
                    }
                    if (j2 <= 1) {
                        for (int k2 = 0; k2 < 8; ++k2) {
                            worldrenderer.pos(f28 + k2 + 1.0f - 9.765625E-4f, f4 + 0.0f, f29 + 8.0f).tex((f26 + k2 + 0.5f) * 0.00390625f + f21, (f27 + 8.0f) * 0.00390625f + f22).color(f11, f12, f13, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                            worldrenderer.pos(f28 + k2 + 1.0f - 9.765625E-4f, f4 + 4.0f, f29 + 8.0f).tex((f26 + k2 + 0.5f) * 0.00390625f + f21, (f27 + 8.0f) * 0.00390625f + f22).color(f11, f12, f13, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                            worldrenderer.pos(f28 + k2 + 1.0f - 9.765625E-4f, f4 + 4.0f, f29 + 0.0f).tex((f26 + k2 + 0.5f) * 0.00390625f + f21, (f27 + 0.0f) * 0.00390625f + f22).color(f11, f12, f13, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                            worldrenderer.pos(f28 + k2 + 1.0f - 9.765625E-4f, f4 + 0.0f, f29 + 0.0f).tex((f26 + k2 + 0.5f) * 0.00390625f + f21, (f27 + 0.0f) * 0.00390625f + f22).color(f11, f12, f13, 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
                        }
                    }
                    if (l > -1) {
                        for (int l2 = 0; l2 < 8; ++l2) {
                            worldrenderer.pos(f28 + 0.0f, f4 + 4.0f, f29 + l2 + 0.0f).tex((f26 + 0.0f) * 0.00390625f + f21, (f27 + l2 + 0.5f) * 0.00390625f + f22).color(f17, f18, f19, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                            worldrenderer.pos(f28 + 8.0f, f4 + 4.0f, f29 + l2 + 0.0f).tex((f26 + 8.0f) * 0.00390625f + f21, (f27 + l2 + 0.5f) * 0.00390625f + f22).color(f17, f18, f19, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                            worldrenderer.pos(f28 + 8.0f, f4 + 0.0f, f29 + l2 + 0.0f).tex((f26 + 8.0f) * 0.00390625f + f21, (f27 + l2 + 0.5f) * 0.00390625f + f22).color(f17, f18, f19, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                            worldrenderer.pos(f28 + 0.0f, f4 + 0.0f, f29 + l2 + 0.0f).tex((f26 + 0.0f) * 0.00390625f + f21, (f27 + l2 + 0.5f) * 0.00390625f + f22).color(f17, f18, f19, 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
                        }
                    }
                    if (l <= 1) {
                        for (int i3 = 0; i3 < 8; ++i3) {
                            worldrenderer.pos(f28 + 0.0f, f4 + 4.0f, f29 + i3 + 1.0f - 9.765625E-4f).tex((f26 + 0.0f) * 0.00390625f + f21, (f27 + i3 + 0.5f) * 0.00390625f + f22).color(f17, f18, f19, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                            worldrenderer.pos(f28 + 8.0f, f4 + 4.0f, f29 + i3 + 1.0f - 9.765625E-4f).tex((f26 + 8.0f) * 0.00390625f + f21, (f27 + i3 + 0.5f) * 0.00390625f + f22).color(f17, f18, f19, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                            worldrenderer.pos(f28 + 8.0f, f4 + 0.0f, f29 + i3 + 1.0f - 9.765625E-4f).tex((f26 + 8.0f) * 0.00390625f + f21, (f27 + i3 + 0.5f) * 0.00390625f + f22).color(f17, f18, f19, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                            worldrenderer.pos(f28 + 0.0f, f4 + 0.0f, f29 + i3 + 1.0f - 9.765625E-4f).tex((f26 + 0.0f) * 0.00390625f + f21, (f27 + i3 + 0.5f) * 0.00390625f + f22).color(f17, f18, f19, 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
                        }
                    }
                    tessellator.draw();
                }
            }
            this.cloudRenderer.endUpdateGlList();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
    }
    
    public void updateChunks(long finishTimeNano) {
        finishTimeNano += (long)1.0E8;
        this.displayListEntitiesDirty |= this.renderDispatcher.runChunkUploads(finishTimeNano);
        if (this.chunksToUpdateForced.size() > 0) {
            final Iterator iterator = this.chunksToUpdateForced.iterator();
            while (iterator.hasNext()) {
                final RenderChunk renderchunk = iterator.next();
                if (!this.renderDispatcher.updateChunkLater(renderchunk)) {
                    break;
                }
                renderchunk.setNeedsUpdate(false);
                iterator.remove();
                this.chunksToUpdate.remove(renderchunk);
                this.chunksToResortTransparency.remove(renderchunk);
            }
        }
        if (this.chunksToResortTransparency.size() > 0) {
            final Iterator iterator2 = this.chunksToResortTransparency.iterator();
            if (iterator2.hasNext()) {
                final RenderChunk renderchunk2 = iterator2.next();
                if (this.renderDispatcher.updateTransparencyLater(renderchunk2)) {
                    iterator2.remove();
                }
            }
        }
        int j = 0;
        int k = Config.getUpdatesPerFrame();
        final int i = k * 2;
        final Iterator iterator3 = this.chunksToUpdate.iterator();
        while (iterator3.hasNext()) {
            final RenderChunk renderchunk3 = iterator3.next();
            if (!this.renderDispatcher.updateChunkLater(renderchunk3)) {
                break;
            }
            renderchunk3.setNeedsUpdate(false);
            iterator3.remove();
            if (renderchunk3.getCompiledChunk().isEmpty() && k < i) {
                ++k;
            }
            if (++j >= k) {
                break;
            }
        }
    }
    
    public void renderWorldBorder(final Entity p_180449_1_, final float partialTicks) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        final WorldBorder worldborder = this.theWorld.getWorldBorder();
        final double d0 = this.mc.gameSettings.renderDistanceChunks * 16;
        if (p_180449_1_.posX >= worldborder.maxX() - d0 || p_180449_1_.posX <= worldborder.minX() + d0 || p_180449_1_.posZ >= worldborder.maxZ() - d0 || p_180449_1_.posZ <= worldborder.minZ() + d0) {
            double d2 = 1.0 - worldborder.getClosestDistance(p_180449_1_) / d0;
            d2 = Math.pow(d2, 4.0);
            final double d3 = p_180449_1_.lastTickPosX + (p_180449_1_.posX - p_180449_1_.lastTickPosX) * partialTicks;
            final double d4 = p_180449_1_.lastTickPosY + (p_180449_1_.posY - p_180449_1_.lastTickPosY) * partialTicks;
            final double d5 = p_180449_1_.lastTickPosZ + (p_180449_1_.posZ - p_180449_1_.lastTickPosZ) * partialTicks;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            this.renderEngine.bindTexture(RenderGlobal.locationForcefieldPng);
            GlStateManager.depthMask(false);
            GlStateManager.pushMatrix();
            final int i = worldborder.getStatus().getID();
            final float f = (i >> 16 & 0xFF) / 255.0f;
            final float f2 = (i >> 8 & 0xFF) / 255.0f;
            final float f3 = (i & 0xFF) / 255.0f;
            GlStateManager.color(f, f2, f3, (float)d2);
            GlStateManager.doPolygonOffset(-3.0f, -3.0f);
            GlStateManager.enablePolygonOffset();
            GlStateManager.alphaFunc(516, 0.1f);
            GlStateManager.enableAlpha();
            GlStateManager.disableCull();
            final float f4 = Minecraft.getSystemTime() % 3000L / 3000.0f;
            final float f5 = 0.0f;
            final float f6 = 0.0f;
            final float f7 = 128.0f;
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.setTranslation(-d3, -d4, -d5);
            double d6 = Math.max(MathHelper.floor_double(d5 - d0), worldborder.minZ());
            double d7 = Math.min(MathHelper.ceiling_double_int(d5 + d0), worldborder.maxZ());
            if (d3 > worldborder.maxX() - d0) {
                float f8 = 0.0f;
                for (double d8 = d6; d8 < d7; ++d8, f8 += 0.5f) {
                    final double d9 = Math.min(1.0, d7 - d8);
                    final float f9 = (float)d9 * 0.5f;
                    worldrenderer.pos(worldborder.maxX(), 256.0, d8).tex(f4 + f8, f4 + 0.0f).endVertex();
                    worldrenderer.pos(worldborder.maxX(), 256.0, d8 + d9).tex(f4 + f9 + f8, f4 + 0.0f).endVertex();
                    worldrenderer.pos(worldborder.maxX(), 0.0, d8 + d9).tex(f4 + f9 + f8, f4 + 128.0f).endVertex();
                    worldrenderer.pos(worldborder.maxX(), 0.0, d8).tex(f4 + f8, f4 + 128.0f).endVertex();
                }
            }
            if (d3 < worldborder.minX() + d0) {
                float f10 = 0.0f;
                for (double d10 = d6; d10 < d7; ++d10, f10 += 0.5f) {
                    final double d11 = Math.min(1.0, d7 - d10);
                    final float f11 = (float)d11 * 0.5f;
                    worldrenderer.pos(worldborder.minX(), 256.0, d10).tex(f4 + f10, f4 + 0.0f).endVertex();
                    worldrenderer.pos(worldborder.minX(), 256.0, d10 + d11).tex(f4 + f11 + f10, f4 + 0.0f).endVertex();
                    worldrenderer.pos(worldborder.minX(), 0.0, d10 + d11).tex(f4 + f11 + f10, f4 + 128.0f).endVertex();
                    worldrenderer.pos(worldborder.minX(), 0.0, d10).tex(f4 + f10, f4 + 128.0f).endVertex();
                }
            }
            d6 = Math.max(MathHelper.floor_double(d3 - d0), worldborder.minX());
            d7 = Math.min(MathHelper.ceiling_double_int(d3 + d0), worldborder.maxX());
            if (d5 > worldborder.maxZ() - d0) {
                float f12 = 0.0f;
                for (double d12 = d6; d12 < d7; ++d12, f12 += 0.5f) {
                    final double d13 = Math.min(1.0, d7 - d12);
                    final float f13 = (float)d13 * 0.5f;
                    worldrenderer.pos(d12, 256.0, worldborder.maxZ()).tex(f4 + f12, f4 + 0.0f).endVertex();
                    worldrenderer.pos(d12 + d13, 256.0, worldborder.maxZ()).tex(f4 + f13 + f12, f4 + 0.0f).endVertex();
                    worldrenderer.pos(d12 + d13, 0.0, worldborder.maxZ()).tex(f4 + f13 + f12, f4 + 128.0f).endVertex();
                    worldrenderer.pos(d12, 0.0, worldborder.maxZ()).tex(f4 + f12, f4 + 128.0f).endVertex();
                }
            }
            if (d5 < worldborder.minZ() + d0) {
                float f14 = 0.0f;
                for (double d14 = d6; d14 < d7; ++d14, f14 += 0.5f) {
                    final double d15 = Math.min(1.0, d7 - d14);
                    final float f15 = (float)d15 * 0.5f;
                    worldrenderer.pos(d14, 256.0, worldborder.minZ()).tex(f4 + f14, f4 + 0.0f).endVertex();
                    worldrenderer.pos(d14 + d15, 256.0, worldborder.minZ()).tex(f4 + f15 + f14, f4 + 0.0f).endVertex();
                    worldrenderer.pos(d14 + d15, 0.0, worldborder.minZ()).tex(f4 + f15 + f14, f4 + 128.0f).endVertex();
                    worldrenderer.pos(d14, 0.0, worldborder.minZ()).tex(f4 + f14, f4 + 128.0f).endVertex();
                }
            }
            tessellator.draw();
            worldrenderer.setTranslation(0.0, 0.0, 0.0);
            GlStateManager.enableCull();
            GlStateManager.disableAlpha();
            GlStateManager.doPolygonOffset(0.0f, 0.0f);
            GlStateManager.disablePolygonOffset();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
        }
    }
    
    private void preRenderDamagedBlocks() {
        GlStateManager.tryBlendFuncSeparate(774, 768, 1, 0);
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.5f);
        GlStateManager.doPolygonOffset(-3.0f, -3.0f);
        GlStateManager.enablePolygonOffset();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();
        if (Config.isShaders()) {
            ShadersRender.beginBlockDamage();
        }
    }
    
    private void postRenderDamagedBlocks() {
        GlStateManager.disableAlpha();
        GlStateManager.doPolygonOffset(0.0f, 0.0f);
        GlStateManager.disablePolygonOffset();
        GlStateManager.enableAlpha();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
        if (Config.isShaders()) {
            ShadersRender.endBlockDamage();
        }
    }
    
    public void drawBlockDamageTexture(final Tessellator tessellatorIn, final WorldRenderer worldRendererIn, final Entity entityIn, final float partialTicks) {
        final double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
        final double d2 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
        final double d3 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
        if (!this.damagedBlocks.isEmpty()) {
            this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            this.preRenderDamagedBlocks();
            worldRendererIn.begin(7, DefaultVertexFormats.BLOCK);
            worldRendererIn.setTranslation(-d0, -d2, -d3);
            worldRendererIn.markDirty();
            final Iterator iterator = this.damagedBlocks.values().iterator();
            while (iterator.hasNext()) {
                final DestroyBlockProgress destroyblockprogress = iterator.next();
                final BlockPos blockpos = destroyblockprogress.getPosition();
                final double d4 = blockpos.getX() - d0;
                final double d5 = blockpos.getY() - d2;
                final double d6 = blockpos.getZ() - d3;
                final Block block = this.theWorld.getBlockState(blockpos).getBlock();
                boolean flag2;
                if (Reflector.ForgeTileEntity_canRenderBreaking.exists()) {
                    boolean flag1 = block instanceof BlockChest || block instanceof BlockEnderChest || block instanceof BlockSign || block instanceof BlockSkull;
                    if (!flag1) {
                        final TileEntity tileentity = this.theWorld.getTileEntity(blockpos);
                        if (tileentity != null) {
                            flag1 = Reflector.callBoolean(tileentity, Reflector.ForgeTileEntity_canRenderBreaking, new Object[0]);
                        }
                    }
                    flag2 = !flag1;
                }
                else {
                    flag2 = (!(block instanceof BlockChest) && !(block instanceof BlockEnderChest) && !(block instanceof BlockSign) && !(block instanceof BlockSkull));
                }
                if (flag2) {
                    if (d4 * d4 + d5 * d5 + d6 * d6 > 1024.0) {
                        iterator.remove();
                    }
                    else {
                        final IBlockState iblockstate = this.theWorld.getBlockState(blockpos);
                        if (iblockstate.getBlock().getMaterial() == Material.air) {
                            continue;
                        }
                        final int i = destroyblockprogress.getPartialBlockDamage();
                        final TextureAtlasSprite textureatlassprite = this.destroyBlockIcons[i];
                        final BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
                        blockrendererdispatcher.renderBlockDamage(iblockstate, blockpos, textureatlassprite, this.theWorld);
                    }
                }
            }
            tessellatorIn.draw();
            worldRendererIn.setTranslation(0.0, 0.0, 0.0);
            this.postRenderDamagedBlocks();
        }
    }
    
    public void drawSelectionBox(final EntityPlayer player, final MovingObjectPosition movingObjectPositionIn, final int p_72731_3_, final float partialTicks) {
        if (p_72731_3_ == 0 && movingObjectPositionIn.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(0.0f, 0.0f, 0.0f, 0.4f);
            GL11.glLineWidth(2.0f);
            GlStateManager.disableTexture2D();
            if (Config.isShaders()) {
                Shaders.disableTexture2D();
            }
            GlStateManager.depthMask(false);
            final float f = 0.002f;
            final BlockPos blockpos = movingObjectPositionIn.getBlockPos();
            final Block block = this.theWorld.getBlockState(blockpos).getBlock();
            if (block.getMaterial() != Material.air && this.theWorld.getWorldBorder().contains(blockpos)) {
                block.setBlockBoundsBasedOnState(this.theWorld, blockpos);
                final double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
                final double d2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
                final double d3 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
                func_181561_a(block.getSelectedBoundingBox(this.theWorld, blockpos).expand(0.0020000000949949026, 0.0020000000949949026, 0.0020000000949949026).offset(-d0, -d2, -d3));
            }
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            if (Config.isShaders()) {
                Shaders.enableTexture2D();
            }
            GlStateManager.disableBlend();
        }
    }
    
    public static void func_181561_a(final AxisAlignedBB p_181561_0_) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(1, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        tessellator.draw();
    }
    
    public static void func_181563_a(final AxisAlignedBB p_181563_0_, final int p_181563_1_, final int p_181563_2_, final int p_181563_3_, final int p_181563_4_) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(p_181563_0_.minX, p_181563_0_.minY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.minY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.minY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.minX, p_181563_0_.minY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.minX, p_181563_0_.minY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(p_181563_0_.minX, p_181563_0_.maxY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.maxY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.maxY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.minX, p_181563_0_.maxY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.minX, p_181563_0_.maxY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        tessellator.draw();
        worldrenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(p_181563_0_.minX, p_181563_0_.minY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.minX, p_181563_0_.maxY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.minY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.maxY, p_181563_0_.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.minY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.maxX, p_181563_0_.maxY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.minX, p_181563_0_.minY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(p_181563_0_.minX, p_181563_0_.maxY, p_181563_0_.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        tessellator.draw();
    }
    
    private void markBlocksForUpdate(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        this.viewFrustum.markBlocksForUpdate(x1, y1, z1, x2, y2, z2);
    }
    
    @Override
    public void markBlockForUpdate(final BlockPos pos) {
        final int i = pos.getX();
        final int j = pos.getY();
        final int k = pos.getZ();
        this.markBlocksForUpdate(i - 1, j - 1, k - 1, i + 1, j + 1, k + 1);
    }
    
    @Override
    public void notifyLightSet(final BlockPos pos) {
        final int i = pos.getX();
        final int j = pos.getY();
        final int k = pos.getZ();
        this.markBlocksForUpdate(i - 1, j - 1, k - 1, i + 1, j + 1, k + 1);
    }
    
    @Override
    public void markBlockRangeForRenderUpdate(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        this.markBlocksForUpdate(x1 - 1, y1 - 1, z1 - 1, x2 + 1, y2 + 1, z2 + 1);
    }
    
    @Override
    public void playRecord(final String recordName, final BlockPos blockPosIn) {
        final ISound isound = this.mapSoundPositions.get(blockPosIn);
        if (isound != null) {
            this.mc.getSoundHandler().stopSound(isound);
            this.mapSoundPositions.remove(blockPosIn);
        }
        if (recordName != null) {
            final ItemRecord itemrecord = ItemRecord.getRecord(recordName);
            if (itemrecord != null) {
                this.mc.ingameGUI.setRecordPlayingMessage(itemrecord.getRecordNameLocal());
            }
            ResourceLocation resourcelocation = null;
            if (Reflector.ForgeItemRecord_getRecordResource.exists() && itemrecord != null) {
                resourcelocation = (ResourceLocation)Reflector.call(itemrecord, Reflector.ForgeItemRecord_getRecordResource, recordName);
            }
            if (resourcelocation == null) {
                resourcelocation = new ResourceLocation(recordName);
            }
            final PositionedSoundRecord positionedsoundrecord = PositionedSoundRecord.create(resourcelocation, (float)blockPosIn.getX(), (float)blockPosIn.getY(), (float)blockPosIn.getZ());
            this.mapSoundPositions.put(blockPosIn, positionedsoundrecord);
            this.mc.getSoundHandler().playSound(positionedsoundrecord);
        }
    }
    
    @Override
    public void playSound(final String soundName, final double x, final double y, final double z, final float volume, final float pitch) {
    }
    
    @Override
    public void playSoundToNearExcept(final EntityPlayer except, final String soundName, final double x, final double y, final double z, final float volume, final float pitch) {
    }
    
    @Override
    public void spawnParticle(final int particleID, final boolean ignoreRange, final double xCoord, final double yCoord, final double zCoord, final double xOffset, final double yOffset, final double zOffset, final int... p_180442_15_) {
        try {
            this.spawnEntityFX(particleID, ignoreRange, xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, p_180442_15_);
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while adding particle");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being added");
            crashreportcategory.addCrashSection("ID", particleID);
            if (p_180442_15_ != null) {
                crashreportcategory.addCrashSection("Parameters", p_180442_15_);
            }
            crashreportcategory.addCrashSectionCallable("Position", new Callable() {
                private static final String __OBFID = "CL_00000955";
                
                @Override
                public String call() throws Exception {
                    return CrashReportCategory.getCoordinateInfo(xCoord, yCoord, zCoord);
                }
            });
            throw new ReportedException(crashreport);
        }
    }
    
    private void spawnParticle(final EnumParticleTypes particleIn, final double p_174972_2_, final double p_174972_4_, final double p_174972_6_, final double p_174972_8_, final double p_174972_10_, final double p_174972_12_, final int... p_174972_14_) {
        this.spawnParticle(particleIn.getParticleID(), particleIn.getShouldIgnoreRange(), p_174972_2_, p_174972_4_, p_174972_6_, p_174972_8_, p_174972_10_, p_174972_12_, p_174972_14_);
    }
    
    private EntityFX spawnEntityFX(final int p_174974_1_, final boolean ignoreRange, final double p_174974_3_, final double p_174974_5_, final double p_174974_7_, final double p_174974_9_, final double p_174974_11_, final double p_174974_13_, final int... p_174974_15_) {
        if (this.mc == null || this.mc.getRenderViewEntity() == null || this.mc.effectRenderer == null) {
            return null;
        }
        int i = this.mc.gameSettings.particleSetting;
        if (i == 1 && this.theWorld.rand.nextInt(3) == 0) {
            i = 2;
        }
        final double d0 = this.mc.getRenderViewEntity().posX - p_174974_3_;
        final double d2 = this.mc.getRenderViewEntity().posY - p_174974_5_;
        final double d3 = this.mc.getRenderViewEntity().posZ - p_174974_7_;
        if (p_174974_1_ == EnumParticleTypes.EXPLOSION_HUGE.getParticleID() && !Config.isAnimatedExplosion()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.EXPLOSION_LARGE.getParticleID() && !Config.isAnimatedExplosion()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.EXPLOSION_NORMAL.getParticleID() && !Config.isAnimatedExplosion()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.SUSPENDED.getParticleID() && !Config.isWaterParticles()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.SUSPENDED_DEPTH.getParticleID() && !Config.isVoidParticles()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.SMOKE_NORMAL.getParticleID() && !Config.isAnimatedSmoke()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.SMOKE_LARGE.getParticleID() && !Config.isAnimatedSmoke()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.SPELL_MOB.getParticleID() && !Config.isPotionParticles()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID() && !Config.isPotionParticles()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.SPELL.getParticleID() && !Config.isPotionParticles()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.SPELL_INSTANT.getParticleID() && !Config.isPotionParticles()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.SPELL_WITCH.getParticleID() && !Config.isPotionParticles()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.PORTAL.getParticleID() && !Config.isAnimatedPortal()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.FLAME.getParticleID() && !Config.isAnimatedFlame()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.REDSTONE.getParticleID() && !Config.isAnimatedRedstone()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.DRIP_WATER.getParticleID() && !Config.isDrippingWaterLava()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.DRIP_LAVA.getParticleID() && !Config.isDrippingWaterLava()) {
            return null;
        }
        if (p_174974_1_ == EnumParticleTypes.FIREWORKS_SPARK.getParticleID() && !Config.isFireworkParticles()) {
            return null;
        }
        if (ignoreRange) {
            return this.mc.effectRenderer.spawnEffectParticle(p_174974_1_, p_174974_3_, p_174974_5_, p_174974_7_, p_174974_9_, p_174974_11_, p_174974_13_, p_174974_15_);
        }
        final double d4 = 16.0;
        double d5 = 256.0;
        if (p_174974_1_ == EnumParticleTypes.CRIT.getParticleID()) {
            d5 = 38416.0;
        }
        if (d0 * d0 + d2 * d2 + d3 * d3 > d5) {
            return null;
        }
        if (i > 1) {
            return null;
        }
        final EntityFX entityfx = this.mc.effectRenderer.spawnEffectParticle(p_174974_1_, p_174974_3_, p_174974_5_, p_174974_7_, p_174974_9_, p_174974_11_, p_174974_13_, p_174974_15_);
        if (p_174974_1_ == EnumParticleTypes.WATER_BUBBLE.getParticleID()) {
            CustomColors.updateWaterFX(entityfx, this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_);
        }
        if (p_174974_1_ == EnumParticleTypes.WATER_SPLASH.getParticleID()) {
            CustomColors.updateWaterFX(entityfx, this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_);
        }
        if (p_174974_1_ == EnumParticleTypes.WATER_DROP.getParticleID()) {
            CustomColors.updateWaterFX(entityfx, this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_);
        }
        if (p_174974_1_ == EnumParticleTypes.TOWN_AURA.getParticleID()) {
            CustomColors.updateMyceliumFX(entityfx);
        }
        if (p_174974_1_ == EnumParticleTypes.PORTAL.getParticleID()) {
            CustomColors.updatePortalFX(entityfx);
        }
        if (p_174974_1_ == EnumParticleTypes.REDSTONE.getParticleID()) {
            CustomColors.updateReddustFX(entityfx, this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_);
        }
        return entityfx;
    }
    
    @Override
    public void onEntityAdded(final Entity entityIn) {
        RandomMobs.entityLoaded(entityIn, this.theWorld);
        if (Config.isDynamicLights()) {
            DynamicLights.entityAdded(entityIn, this);
        }
    }
    
    @Override
    public void onEntityRemoved(final Entity entityIn) {
        if (Config.isDynamicLights()) {
            DynamicLights.entityRemoved(entityIn, this);
        }
    }
    
    public void deleteAllDisplayLists() {
    }
    
    @Override
    public void broadcastSound(final int p_180440_1_, final BlockPos p_180440_2_, final int p_180440_3_) {
        switch (p_180440_1_) {
            case 1013:
            case 1018: {
                if (this.mc.getRenderViewEntity() == null) {
                    break;
                }
                final double d0 = p_180440_2_.getX() - this.mc.getRenderViewEntity().posX;
                final double d2 = p_180440_2_.getY() - this.mc.getRenderViewEntity().posY;
                final double d3 = p_180440_2_.getZ() - this.mc.getRenderViewEntity().posZ;
                final double d4 = Math.sqrt(d0 * d0 + d2 * d2 + d3 * d3);
                double d5 = this.mc.getRenderViewEntity().posX;
                double d6 = this.mc.getRenderViewEntity().posY;
                double d7 = this.mc.getRenderViewEntity().posZ;
                if (d4 > 0.0) {
                    d5 += d0 / d4 * 2.0;
                    d6 += d2 / d4 * 2.0;
                    d7 += d3 / d4 * 2.0;
                }
                if (p_180440_1_ == 1013) {
                    this.theWorld.playSound(d5, d6, d7, "mob.wither.spawn", 1.0f, 1.0f, false);
                    break;
                }
                this.theWorld.playSound(d5, d6, d7, "mob.enderdragon.end", 5.0f, 1.0f, false);
                break;
            }
        }
    }
    
    @Override
    public void playAuxSFX(final EntityPlayer player, final int sfxType, final BlockPos blockPosIn, final int p_180439_4_) {
        final Random random = this.theWorld.rand;
        switch (sfxType) {
            case 1000: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.click", 1.0f, 1.0f, false);
                break;
            }
            case 1001: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.click", 1.0f, 1.2f, false);
                break;
            }
            case 1002: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.bow", 1.0f, 1.2f, false);
                break;
            }
            case 1003: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.door_open", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1004: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.fizz", 0.5f, 2.6f + (random.nextFloat() - random.nextFloat()) * 0.8f, false);
                break;
            }
            case 1005: {
                if (Item.getItemById(p_180439_4_) instanceof ItemRecord) {
                    this.theWorld.playRecord(blockPosIn, "records." + ((ItemRecord)Item.getItemById(p_180439_4_)).recordName);
                    break;
                }
                this.theWorld.playRecord(blockPosIn, null);
                break;
            }
            case 1006: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.door_close", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1007: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.charge", 10.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1008: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.fireball", 10.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1009: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.fireball", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1010: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.wood", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1011: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.metal", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1012: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.woodbreak", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1014: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.wither.shoot", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1015: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.bat.takeoff", 0.05f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1016: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.infect", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1017: {
                this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.unfect", 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                break;
            }
            case 1020: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_break", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1021: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_use", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 1022: {
                this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_land", 0.3f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 2000: {
                final int k = p_180439_4_ % 3 - 1;
                final int l = p_180439_4_ / 3 % 3 - 1;
                final double d13 = blockPosIn.getX() + k * 0.6 + 0.5;
                final double d14 = blockPosIn.getY() + 0.5;
                final double d15 = blockPosIn.getZ() + l * 0.6 + 0.5;
                for (int l2 = 0; l2 < 10; ++l2) {
                    final double d16 = random.nextDouble() * 0.2 + 0.01;
                    final double d17 = d13 + k * 0.01 + (random.nextDouble() - 0.5) * l * 0.5;
                    final double d18 = d14 + (random.nextDouble() - 0.5) * 0.5;
                    final double d19 = d15 + l * 0.01 + (random.nextDouble() - 0.5) * k * 0.5;
                    final double d20 = k * d16 + random.nextGaussian() * 0.01;
                    final double d21 = -0.03 + random.nextGaussian() * 0.01;
                    final double d22 = l * d16 + random.nextGaussian() * 0.01;
                    this.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d17, d18, d19, d20, d21, d22, new int[0]);
                }
            }
            case 2001: {
                final Block block = Block.getBlockById(p_180439_4_ & 0xFFF);
                if (block.getMaterial() != Material.air) {
                    this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(block.stepSound.getBreakSound()), (block.stepSound.getVolume() + 1.0f) / 2.0f, block.stepSound.getFrequency() * 0.8f, blockPosIn.getX() + 0.5f, blockPosIn.getY() + 0.5f, blockPosIn.getZ() + 0.5f));
                }
                this.mc.effectRenderer.addBlockDestroyEffects(blockPosIn, block.getStateFromMeta(p_180439_4_ >> 12 & 0xFF));
                break;
            }
            case 2002: {
                final double d23 = blockPosIn.getX();
                final double d24 = blockPosIn.getY();
                final double d25 = blockPosIn.getZ();
                for (int i1 = 0; i1 < 8; ++i1) {
                    this.spawnParticle(EnumParticleTypes.ITEM_CRACK, d23, d24, d25, random.nextGaussian() * 0.15, random.nextDouble() * 0.2, random.nextGaussian() * 0.15, Item.getIdFromItem(Items.potionitem), p_180439_4_);
                }
                final int j1 = Items.potionitem.getColorFromDamage(p_180439_4_);
                final float f = (j1 >> 16 & 0xFF) / 255.0f;
                final float f2 = (j1 >> 8 & 0xFF) / 255.0f;
                final float f3 = (j1 >> 0 & 0xFF) / 255.0f;
                EnumParticleTypes enumparticletypes = EnumParticleTypes.SPELL;
                if (Items.potionitem.isEffectInstant(p_180439_4_)) {
                    enumparticletypes = EnumParticleTypes.SPELL_INSTANT;
                }
                for (int k2 = 0; k2 < 100; ++k2) {
                    final double d26 = random.nextDouble() * 4.0;
                    final double d27 = random.nextDouble() * 3.141592653589793 * 2.0;
                    final double d28 = Math.cos(d27) * d26;
                    final double d29 = 0.01 + random.nextDouble() * 0.5;
                    final double d30 = Math.sin(d27) * d26;
                    final EntityFX entityfx = this.spawnEntityFX(enumparticletypes.getParticleID(), enumparticletypes.getShouldIgnoreRange(), d23 + d28 * 0.1, d24 + 0.3, d25 + d30 * 0.1, d28, d29, d30, new int[0]);
                    if (entityfx != null) {
                        final float f4 = 0.75f + random.nextFloat() * 0.25f;
                        entityfx.setRBGColorF(f * f4, f2 * f4, f3 * f4);
                        entityfx.multiplyVelocity((float)d26);
                    }
                }
                this.theWorld.playSoundAtPos(blockPosIn, "game.potion.smash", 1.0f, this.theWorld.rand.nextFloat() * 0.1f + 0.9f, false);
                break;
            }
            case 2003: {
                final double var7 = blockPosIn.getX() + 0.5;
                final double var8 = blockPosIn.getY();
                final double var9 = blockPosIn.getZ() + 0.5;
                for (int var10 = 0; var10 < 8; ++var10) {
                    this.spawnParticle(EnumParticleTypes.ITEM_CRACK, var7, var8, var9, random.nextGaussian() * 0.15, random.nextDouble() * 0.2, random.nextGaussian() * 0.15, Item.getIdFromItem(Items.ender_eye));
                }
                for (double var11 = 0.0; var11 < 6.283185307179586; var11 += 0.15707963267948966) {
                    this.spawnParticle(EnumParticleTypes.PORTAL, var7 + Math.cos(var11) * 5.0, var8 - 0.4, var9 + Math.sin(var11) * 5.0, Math.cos(var11) * -5.0, 0.0, Math.sin(var11) * -5.0, new int[0]);
                    this.spawnParticle(EnumParticleTypes.PORTAL, var7 + Math.cos(var11) * 5.0, var8 - 0.4, var9 + Math.sin(var11) * 5.0, Math.cos(var11) * -7.0, 0.0, Math.sin(var11) * -7.0, new int[0]);
                }
            }
            case 2004: {
                for (int var12 = 0; var12 < 20; ++var12) {
                    final double d31 = blockPosIn.getX() + 0.5 + (this.theWorld.rand.nextFloat() - 0.5) * 2.0;
                    final double d32 = blockPosIn.getY() + 0.5 + (this.theWorld.rand.nextFloat() - 0.5) * 2.0;
                    final double d33 = blockPosIn.getZ() + 0.5 + (this.theWorld.rand.nextFloat() - 0.5) * 2.0;
                    this.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d31, d32, d33, 0.0, 0.0, 0.0, new int[0]);
                    this.theWorld.spawnParticle(EnumParticleTypes.FLAME, d31, d32, d33, 0.0, 0.0, 0.0, new int[0]);
                }
            }
            case 2005: {
                ItemDye.spawnBonemealParticles(this.theWorld, blockPosIn, p_180439_4_);
                break;
            }
        }
    }
    
    @Override
    public void sendBlockBreakProgress(final int breakerId, final BlockPos pos, final int progress) {
        if (progress >= 0 && progress < 10) {
            DestroyBlockProgress destroyblockprogress = this.damagedBlocks.get(breakerId);
            if (destroyblockprogress == null || destroyblockprogress.getPosition().getX() != pos.getX() || destroyblockprogress.getPosition().getY() != pos.getY() || destroyblockprogress.getPosition().getZ() != pos.getZ()) {
                destroyblockprogress = new DestroyBlockProgress(breakerId, pos);
                this.damagedBlocks.put(breakerId, destroyblockprogress);
            }
            destroyblockprogress.setPartialBlockDamage(progress);
            destroyblockprogress.setCloudUpdateTick(this.cloudTickCounter);
        }
        else {
            this.damagedBlocks.remove(breakerId);
        }
    }
    
    public void setDisplayListEntitiesDirty() {
        this.displayListEntitiesDirty = true;
    }
    
    public void resetClouds() {
        this.cloudRenderer.reset();
    }
    
    public int getCountRenderers() {
        return this.viewFrustum.renderChunks.length;
    }
    
    public int getCountActiveRenderers() {
        return this.renderInfos.size();
    }
    
    public int getCountEntitiesRendered() {
        return this.countEntitiesRendered;
    }
    
    public int getCountTileEntitiesRendered() {
        return this.countTileEntitiesRendered;
    }
    
    public RenderChunk getRenderChunk(final BlockPos p_getRenderChunk_1_) {
        return this.viewFrustum.getRenderChunk(p_getRenderChunk_1_);
    }
    
    public RenderChunk getRenderChunk(final RenderChunk p_getRenderChunk_1_, final EnumFacing p_getRenderChunk_2_) {
        if (p_getRenderChunk_1_ == null) {
            return null;
        }
        final BlockPos blockpos = p_getRenderChunk_1_.func_181701_a(p_getRenderChunk_2_);
        return this.viewFrustum.getRenderChunk(blockpos);
    }
    
    public WorldClient getWorld() {
        return this.theWorld;
    }
    
    public void func_181023_a(final Collection p_181023_1_, final Collection p_181023_2_) {
        final Set set = this.field_181024_n;
        synchronized (this.field_181024_n) {
            this.field_181024_n.removeAll(p_181023_1_);
            this.field_181024_n.addAll(p_181023_2_);
        }
        // monitorexit(this.field_181024_n)
    }
    
    static final class RenderGlobal$2
    {
        static final int[] field_178037_a;
        private static final String __OBFID = "CL_00002535";
        
        static {
            field_178037_a = new int[VertexFormatElement.EnumUsage.values().length];
            try {
                RenderGlobal$2.field_178037_a[VertexFormatElement.EnumUsage.POSITION.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                RenderGlobal$2.field_178037_a[VertexFormatElement.EnumUsage.UV.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                RenderGlobal$2.field_178037_a[VertexFormatElement.EnumUsage.COLOR.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
        }
    }
    
    public static class ContainerLocalRenderInformation
    {
        final RenderChunk renderChunk;
        final EnumFacing facing;
        final Set setFacing;
        final int counter;
        private static final String __OBFID = "CL_00002534";
        
        public ContainerLocalRenderInformation(final RenderChunk p_i4_1_, final EnumFacing p_i4_2_, final int p_i4_3_) {
            this.setFacing = EnumSet.noneOf(EnumFacing.class);
            this.renderChunk = p_i4_1_;
            this.facing = p_i4_2_;
            this.counter = p_i4_3_;
        }
        
        ContainerLocalRenderInformation(final RenderChunk p_i5_1_, final EnumFacing p_i5_2_, final int p_i5_3_, final Object p_i5_4_) {
            this(p_i5_1_, p_i5_2_, p_i5_3_);
        }
    }
}
