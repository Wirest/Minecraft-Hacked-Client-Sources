package net.minecraft.client.renderer.chunk;

import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RegionRenderCache;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.src.BlockPosM;
import net.minecraft.src.Reflector;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class RenderChunk
{
    private World world;
    public static int renderChunksUpdated;
    private BlockPos position;
    public CompiledChunk compiledChunk;
    private final ReentrantLock lockCompileTask;
    private final ReentrantLock lockCompiledChunk;
    private ChunkCompileTaskGenerator compileTask;
    private final FloatBuffer modelviewMatrix;
    private final VertexBuffer[] vertexBuffers;
    public AxisAlignedBB boundingBox;
    private int frameIndex;
    private boolean needsUpdate;
    private BlockPos[] positionOffsets16;
    private static EnumWorldBlockLayer[] ENUM_WORLD_BLOCK_LAYERS = EnumWorldBlockLayer.values();
    private EnumWorldBlockLayer[] blockLayersSingle;

    public RenderChunk(World worldIn, RenderGlobal renderGlobalIn, BlockPos blockPosIn, int indexIn)
    {
        this.positionOffsets16 = new BlockPos[EnumFacing.VALUES.length];
        this.blockLayersSingle = new EnumWorldBlockLayer[1];
        this.compiledChunk = CompiledChunk.DUMMY;
        this.lockCompileTask = new ReentrantLock();
        this.lockCompiledChunk = new ReentrantLock();
        this.compileTask = null;
        this.modelviewMatrix = GLAllocation.createDirectFloatBuffer(16);
        this.vertexBuffers = new VertexBuffer[EnumWorldBlockLayer.values().length];
        this.frameIndex = -1;
        this.needsUpdate = true;
        this.world = worldIn;
        if (!blockPosIn.equals(this.getPosition()))
        {
            this.setPosition(blockPosIn);
        }

        if (OpenGlHelper.useVbo())
        {
            for (int var5 = 0; var5 < EnumWorldBlockLayer.values().length; ++var5)
            {
                this.vertexBuffers[var5] = new VertexBuffer(DefaultVertexFormats.BLOCK);
            }
        }
    }

    public boolean setFrameIndex(int frameIndexIn)
    {
        if (this.frameIndex == frameIndexIn)
        {
            return false;
        }
        else
        {
            this.frameIndex = frameIndexIn;
            return true;
        }
    }

    public VertexBuffer getVertexBufferByLayer(int layer)
    {
        return this.vertexBuffers[layer];
    }

    public void setPosition(BlockPos pos)
    {
        this.stopCompileTask();
        this.position = pos;
        this.boundingBox = new AxisAlignedBB(pos, pos.add(16, 16, 16));
        this.initModelviewMatrix();

        for (int i = 0; i < this.positionOffsets16.length; ++i)
        {
            this.positionOffsets16[i] = null;
        }
    }

    public void resortTransparency(float x, float y, float z, ChunkCompileTaskGenerator generator)
    {
        CompiledChunk var5 = generator.getCompiledChunk();

        if (var5.getState() != null && !var5.isLayerEmpty(EnumWorldBlockLayer.TRANSLUCENT))
        {
            WorldRenderer worldRenderer = generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT);
            this.preRenderBlocks(worldRenderer, this.position);
            worldRenderer.setVertexState(var5.getState());
            this.postRenderBlocks(EnumWorldBlockLayer.TRANSLUCENT, x, y, z, worldRenderer, var5);
        }
    }

    public void rebuildChunk(float x, float y, float z, ChunkCompileTaskGenerator generator)
    {
        CompiledChunk var5 = new CompiledChunk();
        BlockPos var7 = this.position;
        BlockPos var8 = var7.add(15, 15, 15);
        generator.getLock().lock();
        RegionRenderCache var9;

        try
        {
            if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING)
            {
                return;
            }

            if (this.world == null)
            {
                return;
            }

            var9 = new RegionRenderCache(this.world, var7.add(-1, -1, -1), var8.add(1, 1, 1), 1);
            generator.setCompiledChunk(var5);
        }
        finally
        {
            generator.getLock().unlock();
        }

        VisGraph var10 = new VisGraph();

        if (!var9.extendedLevelsInChunkCache())
        {
            ++renderChunksUpdated;
            Iterator var11 = BlockPosM.getAllInBoxMutable(var7, var8).iterator();
            boolean forgeHasTileEntityExists = Reflector.ForgeBlock_hasTileEntity.exists();
            boolean forgeBlockCanRenderInLayerExists = Reflector.ForgeBlock_canRenderInLayer.exists();
            boolean forgeHooksSetRenderLayerExists = Reflector.ForgeHooksClient_setRenderLayer.exists();

            while (var11.hasNext())
            {
                BlockPosM var20 = (BlockPosM)var11.next();
                IBlockState var21 = var9.getBlockState(var20);
                Block var22 = var21.getBlock();

                if (var22.isOpaqueCube())
                {
                    var10.func_178606_a(var20);
                }

                boolean var23;

                if (forgeHasTileEntityExists)
                {
                    var23 = Reflector.callBoolean(var22, Reflector.ForgeBlock_hasTileEntity, new Object[] {var21});
                }
                else
                {
                    var23 = var22.hasTileEntity();
                }

                if (var23)
                {
                    TileEntity blockLayers = var9.getTileEntity(new BlockPos(var20));

                    if (blockLayers != null && TileEntityRendererDispatcher.instance.hasSpecialRenderer(blockLayers))
                    {
                        var5.addTileEntity(blockLayers);
                    }
                }

                EnumWorldBlockLayer[] var30;

                if (forgeBlockCanRenderInLayerExists)
                {
                    var30 = ENUM_WORLD_BLOCK_LAYERS;
                }
                else
                {
                    var30 = this.blockLayersSingle;
                    var30[0] = var22.getBlockLayer();
                }

                for (int i = 0; i < var30.length; ++i)
                {
                    EnumWorldBlockLayer var24 = var30[i];

                    if (forgeBlockCanRenderInLayerExists)
                    {
                        boolean var16 = Reflector.callBoolean(var22, Reflector.ForgeBlock_canRenderInLayer, new Object[] {var24});

                        if (!var16)
                        {
                            continue;
                        }

                        if (forgeHooksSetRenderLayerExists)
                        {
                            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, new Object[] {var24});
                        }
                    }

                    int var31 = var24.ordinal();

                    if (var22.getRenderType() != -1)
                    {
                        WorldRenderer var17 = generator.getRegionRenderCacheBuilder().getWorldRendererByLayerId(var31);
                        var17.setBlockLayer(var24);

                        if (!var5.isLayerStarted(var24))
                        {
                            var5.setLayerStarted(var24);
                            this.preRenderBlocks(var17, var7);
                        }

                        if (Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(var21, var20, var9, var17))
                        {
                            var5.setLayerUsed(var24);
                        }
                    }
                }
            }

            EnumWorldBlockLayer[] var26 = EnumWorldBlockLayer.values();
            int var27 = var26.length;

            for (int var28 = 0; var28 < var27; ++var28)
            {
                EnumWorldBlockLayer var29 = var26[var28];

                if (var5.isLayerStarted(var29))
                {
                    this.postRenderBlocks(var29, x, y, z, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(var29), var5);
                }
            }
        }

        var5.setVisibility(var10.computeVisibility());
    }

    protected void finishCompileTask()
    {
        this.lockCompileTask.lock();

        try
        {
            if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE)
            {
                this.compileTask.finish();
                this.compileTask = null;
            }
        }
        finally
        {
            this.lockCompileTask.unlock();
        }
    }

    public ReentrantLock getLockCompileTask()
    {
        return this.lockCompileTask;
    }

    public ChunkCompileTaskGenerator makeCompileTaskChunk()
    {
        this.lockCompileTask.lock();
        ChunkCompileTaskGenerator var1;

        try
        {
            this.finishCompileTask();
            this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.REBUILD_CHUNK);
            var1 = this.compileTask;
        }
        finally
        {
            this.lockCompileTask.unlock();
        }

        return var1;
    }

    public ChunkCompileTaskGenerator makeCompileTaskTransparency()
    {
        this.lockCompileTask.lock();
        ChunkCompileTaskGenerator var1;

        try
        {
            if (this.compileTask == null || this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.PENDING)
            {
                if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE)
                {
                    this.compileTask.finish();
                    this.compileTask = null;
                }

                this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY);
                this.compileTask.setCompiledChunk(this.compiledChunk);
                var1 = this.compileTask;
                ChunkCompileTaskGenerator var2 = var1;
                return var2;
            }

            var1 = null;
        }
        finally
        {
            this.lockCompileTask.unlock();
        }

        return var1;
    }

    private void preRenderBlocks(WorldRenderer worldRendererIn, BlockPos pos)
    {
        worldRendererIn.startDrawing(7);
        worldRendererIn.setVertexFormat(DefaultVertexFormats.BLOCK);
        worldRendererIn.setTranslation((-pos.getX()), (-pos.getY()), (-pos.getZ()));
    }

    private void postRenderBlocks(EnumWorldBlockLayer layer, float x, float y, float z, WorldRenderer worldRendererIn, CompiledChunk compiledChunkIn)
    {
        if (layer == EnumWorldBlockLayer.TRANSLUCENT && !compiledChunkIn.isLayerEmpty(layer))
        {
            compiledChunkIn.setState(worldRendererIn.getVertexState(x, y, z));
        }

        worldRendererIn.finishDrawing();
    }

    private void initModelviewMatrix()
    {
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        float var1 = 1.000001F;
        GlStateManager.translate(-8.0F, -8.0F, -8.0F);
        GlStateManager.scale(var1, var1, var1);
        GlStateManager.translate(8.0F, 8.0F, 8.0F);
        GlStateManager.getFloat(2982, this.modelviewMatrix);
        GlStateManager.popMatrix();
    }

    public void multModelviewMatrix()
    {
        GlStateManager.multMatrix(this.modelviewMatrix);
    }

    public CompiledChunk getCompiledChunk()
    {
        return this.compiledChunk;
    }

    public void setCompiledChunk(CompiledChunk compiledChunkIn)
    {
        this.lockCompiledChunk.lock();

        try
        {
            this.compiledChunk = compiledChunkIn;
        }
        finally
        {
            this.lockCompiledChunk.unlock();
        }
    }

    public void stopCompileTask()
    {
        this.finishCompileTask();
        this.compiledChunk = CompiledChunk.DUMMY;
    }

    public void deleteGlResources()
    {
        this.stopCompileTask();
        this.world = null;

        for (int var1 = 0; var1 < EnumWorldBlockLayer.values().length; ++var1)
        {
            if (this.vertexBuffers[var1] != null)
            {
                this.vertexBuffers[var1].deleteGlBuffers();
            }
        }
    }

    public BlockPos getPosition()
    {
        return this.position;
    }

    public boolean isCompileTaskPending()
    {
        this.lockCompileTask.lock();
        boolean var1;

        try
        {
            var1 = this.compileTask == null || this.compileTask.getStatus() == ChunkCompileTaskGenerator.Status.PENDING;
        }
        finally
        {
            this.lockCompileTask.unlock();
        }

        return var1;
    }

    public void setNeedsUpdate(boolean needsUpdateIn)
    {
        this.needsUpdate = needsUpdateIn;
    }

    public boolean isNeedsUpdate()
    {
        return this.needsUpdate;
    }

    public BlockPos getPositionOffset16(EnumFacing facing)
    {
        int index = facing.getIndex();
        BlockPos posOffset = this.positionOffsets16[index];

        if (posOffset == null)
        {
            posOffset = this.getPosition().offset(facing, 16);
            this.positionOffsets16[index] = posOffset;
        }

        return posOffset;
    }
}
