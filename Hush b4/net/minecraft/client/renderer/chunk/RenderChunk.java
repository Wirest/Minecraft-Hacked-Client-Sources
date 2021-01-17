// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.chunk;

import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import java.util.Iterator;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import java.util.HashSet;
import net.minecraft.client.renderer.RegionRenderCache;
import java.util.Collection;
import shadersmod.client.SVertexBuilder;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Vec3i;
import optifine.ReflectorForge;
import optifine.BlockPosM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.OpenGlHelper;
import optifine.Reflector;
import optifine.Config;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.GLAllocation;
import com.google.common.collect.Sets;
import net.minecraft.util.EnumWorldBlockLayer;
import java.util.EnumMap;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import java.nio.FloatBuffer;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.world.World;

public class RenderChunk
{
    private World world;
    private final RenderGlobal renderGlobal;
    public static int renderChunksUpdated;
    private BlockPos position;
    public CompiledChunk compiledChunk;
    private final ReentrantLock lockCompileTask;
    private final ReentrantLock lockCompiledChunk;
    private ChunkCompileTaskGenerator compileTask;
    private final Set field_181056_j;
    private final int index;
    private final FloatBuffer modelviewMatrix;
    private final VertexBuffer[] vertexBuffers;
    public AxisAlignedBB boundingBox;
    private int frameIndex;
    private boolean needsUpdate;
    private EnumMap field_181702_p;
    private static final String __OBFID = "CL_00002452";
    private BlockPos[] positionOffsets16;
    private static EnumWorldBlockLayer[] ENUM_WORLD_BLOCK_LAYERS;
    private EnumWorldBlockLayer[] blockLayersSingle;
    private boolean isMipmaps;
    private boolean fixBlockLayer;
    private boolean playerUpdate;
    
    static {
        RenderChunk.ENUM_WORLD_BLOCK_LAYERS = EnumWorldBlockLayer.values();
    }
    
    public RenderChunk(final World worldIn, final RenderGlobal renderGlobalIn, final BlockPos blockPosIn, final int indexIn) {
        this.compiledChunk = CompiledChunk.DUMMY;
        this.lockCompileTask = new ReentrantLock();
        this.lockCompiledChunk = new ReentrantLock();
        this.compileTask = null;
        this.field_181056_j = Sets.newHashSet();
        this.modelviewMatrix = GLAllocation.createDirectFloatBuffer(16);
        this.vertexBuffers = new VertexBuffer[EnumWorldBlockLayer.values().length];
        this.frameIndex = -1;
        this.needsUpdate = true;
        this.positionOffsets16 = new BlockPos[EnumFacing.VALUES.length];
        this.blockLayersSingle = new EnumWorldBlockLayer[1];
        this.isMipmaps = Config.isMipmaps();
        this.fixBlockLayer = !Reflector.BetterFoliageClient.exists();
        this.playerUpdate = false;
        this.world = worldIn;
        this.renderGlobal = renderGlobalIn;
        this.index = indexIn;
        if (!blockPosIn.equals(this.getPosition())) {
            this.setPosition(blockPosIn);
        }
        if (OpenGlHelper.useVbo()) {
            for (int i = 0; i < EnumWorldBlockLayer.values().length; ++i) {
                this.vertexBuffers[i] = new VertexBuffer(DefaultVertexFormats.BLOCK);
            }
        }
    }
    
    public boolean setFrameIndex(final int frameIndexIn) {
        if (this.frameIndex == frameIndexIn) {
            return false;
        }
        this.frameIndex = frameIndexIn;
        return true;
    }
    
    public VertexBuffer getVertexBufferByLayer(final int layer) {
        return this.vertexBuffers[layer];
    }
    
    public void setPosition(final BlockPos pos) {
        this.stopCompileTask();
        this.position = pos;
        this.boundingBox = new AxisAlignedBB(pos, pos.add(16, 16, 16));
        this.initModelviewMatrix();
        for (int i = 0; i < this.positionOffsets16.length; ++i) {
            this.positionOffsets16[i] = null;
        }
    }
    
    public void resortTransparency(final float x, final float y, final float z, final ChunkCompileTaskGenerator generator) {
        final CompiledChunk compiledchunk = generator.getCompiledChunk();
        if (compiledchunk.getState() != null && !compiledchunk.isLayerEmpty(EnumWorldBlockLayer.TRANSLUCENT)) {
            final WorldRenderer worldrenderer = generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT);
            this.preRenderBlocks(worldrenderer, this.position);
            worldrenderer.setVertexState(compiledchunk.getState());
            this.postRenderBlocks(EnumWorldBlockLayer.TRANSLUCENT, x, y, z, worldrenderer, compiledchunk);
        }
    }
    
    public void rebuildChunk(final float x, final float y, final float z, final ChunkCompileTaskGenerator generator) {
        final CompiledChunk compiledchunk = new CompiledChunk();
        final boolean flag = true;
        final BlockPos blockpos = this.position;
        final BlockPos blockpos2 = blockpos.add(15, 15, 15);
        generator.getLock().lock();
        RegionRenderCache regionrendercache;
        try {
            if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
                return;
            }
            if (this.world == null) {
                return;
            }
            regionrendercache = this.createRegionRenderCache(this.world, blockpos.add(-1, -1, -1), blockpos2.add(1, 1, 1), 1);
            if (Reflector.MinecraftForgeClient_onRebuildChunk.exists()) {
                Reflector.call(Reflector.MinecraftForgeClient_onRebuildChunk, this.world, this.position, regionrendercache);
            }
            generator.setCompiledChunk(compiledchunk);
        }
        finally {
            generator.getLock().unlock();
        }
        generator.getLock().unlock();
        final VisGraph var10 = new VisGraph();
        final HashSet var11 = Sets.newHashSet();
        if (!regionrendercache.extendedLevelsInChunkCache()) {
            ++RenderChunk.renderChunksUpdated;
            final boolean[] aboolean = new boolean[RenderChunk.ENUM_WORLD_BLOCK_LAYERS.length];
            final BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            final Iterator iterator = BlockPosM.getAllInBoxMutable(blockpos, blockpos2).iterator();
            final boolean flag2 = Reflector.ForgeBlock_hasTileEntity.exists();
            final boolean flag3 = Reflector.ForgeBlock_canRenderInLayer.exists();
            final boolean flag4 = Reflector.ForgeHooksClient_setRenderLayer.exists();
            while (iterator.hasNext()) {
                final BlockPosM blockposm = iterator.next();
                final IBlockState iblockstate = regionrendercache.getBlockState(blockposm);
                final Block block = iblockstate.getBlock();
                if (block.isOpaqueCube()) {
                    var10.func_178606_a(blockposm);
                }
                if (ReflectorForge.blockHasTileEntity(iblockstate)) {
                    final TileEntity tileentity = regionrendercache.getTileEntity(new BlockPos(blockposm));
                    final TileEntitySpecialRenderer tileentityspecialrenderer = TileEntityRendererDispatcher.instance.getSpecialRenderer(tileentity);
                    if (tileentity != null && tileentityspecialrenderer != null) {
                        compiledchunk.addTileEntity(tileentity);
                        if (tileentityspecialrenderer.func_181055_a()) {
                            var11.add(tileentity);
                        }
                    }
                }
                EnumWorldBlockLayer[] aenumworldblocklayer;
                if (flag3) {
                    aenumworldblocklayer = RenderChunk.ENUM_WORLD_BLOCK_LAYERS;
                }
                else {
                    aenumworldblocklayer = this.blockLayersSingle;
                    aenumworldblocklayer[0] = block.getBlockLayer();
                }
                for (int i = 0; i < aenumworldblocklayer.length; ++i) {
                    EnumWorldBlockLayer enumworldblocklayer = aenumworldblocklayer[i];
                    if (flag3) {
                        final boolean flag5 = Reflector.callBoolean(block, Reflector.ForgeBlock_canRenderInLayer, enumworldblocklayer);
                        if (!flag5) {
                            continue;
                        }
                    }
                    if (flag4) {
                        Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, enumworldblocklayer);
                    }
                    if (this.fixBlockLayer) {
                        enumworldblocklayer = this.fixBlockLayer(block, enumworldblocklayer);
                    }
                    final int j = enumworldblocklayer.ordinal();
                    if (block.getRenderType() != -1) {
                        final WorldRenderer worldrenderer = generator.getRegionRenderCacheBuilder().getWorldRendererByLayerId(j);
                        worldrenderer.setBlockLayer(enumworldblocklayer);
                        if (!compiledchunk.isLayerStarted(enumworldblocklayer)) {
                            compiledchunk.setLayerStarted(enumworldblocklayer);
                            this.preRenderBlocks(worldrenderer, blockpos);
                        }
                        final boolean[] array = aboolean;
                        final int n = j;
                        array[n] |= blockrendererdispatcher.renderBlock(iblockstate, blockposm, regionrendercache, worldrenderer);
                    }
                }
            }
            EnumWorldBlockLayer[] enum_WORLD_BLOCK_LAYERS;
            for (int length = (enum_WORLD_BLOCK_LAYERS = RenderChunk.ENUM_WORLD_BLOCK_LAYERS).length, k = 0; k < length; ++k) {
                final EnumWorldBlockLayer enumworldblocklayer2 = enum_WORLD_BLOCK_LAYERS[k];
                if (aboolean[enumworldblocklayer2.ordinal()]) {
                    compiledchunk.setLayerUsed(enumworldblocklayer2);
                }
                if (compiledchunk.isLayerStarted(enumworldblocklayer2)) {
                    if (Config.isShaders()) {
                        SVertexBuilder.calcNormalChunkLayer(generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(enumworldblocklayer2));
                    }
                    this.postRenderBlocks(enumworldblocklayer2, x, y, z, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(enumworldblocklayer2), compiledchunk);
                }
            }
        }
        compiledchunk.setVisibility(var10.computeVisibility());
        this.lockCompileTask.lock();
        try {
            final HashSet hashset1 = Sets.newHashSet((Iterable<?>)var11);
            final HashSet hashset2 = Sets.newHashSet((Iterable<?>)this.field_181056_j);
            hashset1.removeAll(this.field_181056_j);
            hashset2.removeAll(var11);
            this.field_181056_j.clear();
            this.field_181056_j.addAll(var11);
            this.renderGlobal.func_181023_a(hashset2, hashset1);
        }
        finally {
            this.lockCompileTask.unlock();
        }
        this.lockCompileTask.unlock();
    }
    
    protected void finishCompileTask() {
        this.lockCompileTask.lock();
        try {
            if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE) {
                this.compileTask.finish();
                this.compileTask = null;
            }
        }
        finally {
            this.lockCompileTask.unlock();
        }
        this.lockCompileTask.unlock();
    }
    
    public ReentrantLock getLockCompileTask() {
        return this.lockCompileTask;
    }
    
    public ChunkCompileTaskGenerator makeCompileTaskChunk() {
        this.lockCompileTask.lock();
        ChunkCompileTaskGenerator chunkcompiletaskgenerator;
        try {
            this.finishCompileTask();
            this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.REBUILD_CHUNK);
            chunkcompiletaskgenerator = this.compileTask;
        }
        finally {
            this.lockCompileTask.unlock();
        }
        this.lockCompileTask.unlock();
        return chunkcompiletaskgenerator;
    }
    
    public ChunkCompileTaskGenerator makeCompileTaskTransparency() {
        this.lockCompileTask.lock();
        ChunkCompileTaskGenerator chunkcompiletaskgenerator4;
        try {
            if (this.compileTask != null && this.compileTask.getStatus() == ChunkCompileTaskGenerator.Status.PENDING) {
                final ChunkCompileTaskGenerator chunkcompiletaskgenerator2 = null;
                return chunkcompiletaskgenerator2;
            }
            if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE) {
                this.compileTask.finish();
                this.compileTask = null;
            }
            (this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY)).setCompiledChunk(this.compiledChunk);
            final ChunkCompileTaskGenerator chunkcompiletaskgenerator3 = chunkcompiletaskgenerator4 = this.compileTask;
        }
        finally {
            this.lockCompileTask.unlock();
        }
        this.lockCompileTask.unlock();
        return chunkcompiletaskgenerator4;
    }
    
    private void preRenderBlocks(final WorldRenderer worldRendererIn, final BlockPos pos) {
        worldRendererIn.begin(7, DefaultVertexFormats.BLOCK);
        worldRendererIn.setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
    }
    
    private void postRenderBlocks(final EnumWorldBlockLayer layer, final float x, final float y, final float z, final WorldRenderer worldRendererIn, final CompiledChunk compiledChunkIn) {
        if (layer == EnumWorldBlockLayer.TRANSLUCENT && !compiledChunkIn.isLayerEmpty(layer)) {
            worldRendererIn.func_181674_a(x, y, z);
            compiledChunkIn.setState(worldRendererIn.func_181672_a());
        }
        worldRendererIn.finishDrawing();
    }
    
    private void initModelviewMatrix() {
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        final float f = 1.000001f;
        GlStateManager.translate(-8.0f, -8.0f, -8.0f);
        GlStateManager.scale(f, f, f);
        GlStateManager.translate(8.0f, 8.0f, 8.0f);
        GlStateManager.getFloat(2982, this.modelviewMatrix);
        GlStateManager.popMatrix();
    }
    
    public void multModelviewMatrix() {
        GlStateManager.multMatrix(this.modelviewMatrix);
    }
    
    public CompiledChunk getCompiledChunk() {
        return this.compiledChunk;
    }
    
    public void setCompiledChunk(final CompiledChunk compiledChunkIn) {
        this.lockCompiledChunk.lock();
        try {
            this.compiledChunk = compiledChunkIn;
        }
        finally {
            this.lockCompiledChunk.unlock();
        }
        this.lockCompiledChunk.unlock();
    }
    
    public void stopCompileTask() {
        this.finishCompileTask();
        this.compiledChunk = CompiledChunk.DUMMY;
    }
    
    public void deleteGlResources() {
        this.stopCompileTask();
        this.world = null;
        for (int i = 0; i < EnumWorldBlockLayer.values().length; ++i) {
            if (this.vertexBuffers[i] != null) {
                this.vertexBuffers[i].deleteGlBuffers();
            }
        }
    }
    
    public BlockPos getPosition() {
        return this.position;
    }
    
    public void setNeedsUpdate(final boolean needsUpdateIn) {
        this.needsUpdate = needsUpdateIn;
        if (this.needsUpdate) {
            if (this.isWorldPlayerUpdate()) {
                this.playerUpdate = true;
            }
        }
        else {
            this.playerUpdate = false;
        }
    }
    
    public boolean isNeedsUpdate() {
        return this.needsUpdate;
    }
    
    public BlockPos func_181701_a(final EnumFacing p_181701_1_) {
        return this.getPositionOffset16(p_181701_1_);
    }
    
    public BlockPos getPositionOffset16(final EnumFacing p_getPositionOffset16_1_) {
        final int i = p_getPositionOffset16_1_.getIndex();
        BlockPos blockpos = this.positionOffsets16[i];
        if (blockpos == null) {
            blockpos = this.getPosition().offset(p_getPositionOffset16_1_, 16);
            this.positionOffsets16[i] = blockpos;
        }
        return blockpos;
    }
    
    private boolean isWorldPlayerUpdate() {
        if (this.world instanceof WorldClient) {
            final WorldClient worldclient = (WorldClient)this.world;
            return worldclient.isPlayerUpdate();
        }
        return false;
    }
    
    public boolean isPlayerUpdate() {
        return this.playerUpdate;
    }
    
    protected RegionRenderCache createRegionRenderCache(final World p_createRegionRenderCache_1_, final BlockPos p_createRegionRenderCache_2_, final BlockPos p_createRegionRenderCache_3_, final int p_createRegionRenderCache_4_) {
        return new RegionRenderCache(p_createRegionRenderCache_1_, p_createRegionRenderCache_2_, p_createRegionRenderCache_3_, p_createRegionRenderCache_4_);
    }
    
    private EnumWorldBlockLayer fixBlockLayer(final Block p_fixBlockLayer_1_, final EnumWorldBlockLayer p_fixBlockLayer_2_) {
        if (this.isMipmaps) {
            if (p_fixBlockLayer_2_ == EnumWorldBlockLayer.CUTOUT) {
                if (p_fixBlockLayer_1_ instanceof BlockRedstoneWire) {
                    return p_fixBlockLayer_2_;
                }
                if (p_fixBlockLayer_1_ instanceof BlockCactus) {
                    return p_fixBlockLayer_2_;
                }
                return EnumWorldBlockLayer.CUTOUT_MIPPED;
            }
        }
        else if (p_fixBlockLayer_2_ == EnumWorldBlockLayer.CUTOUT_MIPPED) {
            return EnumWorldBlockLayer.CUTOUT;
        }
        return p_fixBlockLayer_2_;
    }
}
