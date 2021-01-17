// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import optifine.DynamicLights;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.tileentity.TileEntity;
import java.util.Arrays;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import optifine.Config;
import net.minecraft.init.Blocks;
import java.util.ArrayDeque;
import net.minecraft.util.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.ChunkCache;

public class RegionRenderCache extends ChunkCache
{
    private static final IBlockState DEFAULT_STATE;
    private final BlockPos position;
    private int[] combinedLights;
    private IBlockState[] blockStates;
    private static final String __OBFID = "CL_00002565";
    private static ArrayDeque<int[]> cacheLights;
    private static ArrayDeque<IBlockState[]> cacheStates;
    private static int maxCacheSize;
    
    static {
        DEFAULT_STATE = Blocks.air.getDefaultState();
        RegionRenderCache.cacheLights = new ArrayDeque<int[]>();
        RegionRenderCache.cacheStates = new ArrayDeque<IBlockState[]>();
        RegionRenderCache.maxCacheSize = Config.limit(Runtime.getRuntime().availableProcessors(), 1, 32);
    }
    
    public RegionRenderCache(final World worldIn, final BlockPos posFromIn, final BlockPos posToIn, final int subIn) {
        super(worldIn, posFromIn, posToIn, subIn);
        this.position = posFromIn.subtract(new Vec3i(subIn, subIn, subIn));
        final boolean flag = true;
        Arrays.fill(this.combinedLights = allocateLights(8000), -1);
        this.blockStates = allocateStates(8000);
    }
    
    @Override
    public TileEntity getTileEntity(final BlockPos pos) {
        final int i = (pos.getX() >> 4) - this.chunkX;
        final int j = (pos.getZ() >> 4) - this.chunkZ;
        return this.chunkArray[i][j].getTileEntity(pos, Chunk.EnumCreateEntityType.QUEUED);
    }
    
    @Override
    public int getCombinedLight(final BlockPos pos, final int lightValue) {
        final int i = this.getPositionIndex(pos);
        int j = this.combinedLights[i];
        if (j == -1) {
            j = super.getCombinedLight(pos, lightValue);
            if (Config.isDynamicLights() && !this.getBlockState(pos).getBlock().isOpaqueCube()) {
                j = DynamicLights.getCombinedLight(pos, j);
            }
            this.combinedLights[i] = j;
        }
        return j;
    }
    
    @Override
    public IBlockState getBlockState(final BlockPos pos) {
        final int i = this.getPositionIndex(pos);
        IBlockState iblockstate = this.blockStates[i];
        if (iblockstate == null) {
            iblockstate = this.getBlockStateRaw(pos);
            this.blockStates[i] = iblockstate;
        }
        return iblockstate;
    }
    
    private IBlockState getBlockStateRaw(final BlockPos pos) {
        if (pos.getY() >= 0 && pos.getY() < 256) {
            final int i = (pos.getX() >> 4) - this.chunkX;
            final int j = (pos.getZ() >> 4) - this.chunkZ;
            return this.chunkArray[i][j].getBlockState(pos);
        }
        return RegionRenderCache.DEFAULT_STATE;
    }
    
    private int getPositionIndex(final BlockPos p_175630_1_) {
        final int i = p_175630_1_.getX() - this.position.getX();
        final int j = p_175630_1_.getY() - this.position.getY();
        final int k = p_175630_1_.getZ() - this.position.getZ();
        return i * 400 + k * 20 + j;
    }
    
    public void freeBuffers() {
        freeLights(this.combinedLights);
        freeStates(this.blockStates);
    }
    
    private static int[] allocateLights(final int p_allocateLights_0_) {
        synchronized (RegionRenderCache.cacheLights) {
            int[] aint = RegionRenderCache.cacheLights.pollLast();
            if (aint == null || aint.length < p_allocateLights_0_) {
                aint = new int[p_allocateLights_0_];
            }
            // monitorexit(RegionRenderCache.cacheLights)
            return aint;
        }
    }
    
    public static void freeLights(final int[] p_freeLights_0_) {
        synchronized (RegionRenderCache.cacheLights) {
            if (RegionRenderCache.cacheLights.size() < RegionRenderCache.maxCacheSize) {
                RegionRenderCache.cacheLights.add(p_freeLights_0_);
            }
        }
        // monitorexit(RegionRenderCache.cacheLights)
    }
    
    private static IBlockState[] allocateStates(final int p_allocateStates_0_) {
        synchronized (RegionRenderCache.cacheStates) {
            IBlockState[] aiblockstate = RegionRenderCache.cacheStates.pollLast();
            if (aiblockstate != null && aiblockstate.length >= p_allocateStates_0_) {
                Arrays.fill(aiblockstate, null);
            }
            else {
                aiblockstate = new IBlockState[p_allocateStates_0_];
            }
            // monitorexit(RegionRenderCache.cacheStates)
            return aiblockstate;
        }
    }
    
    public static void freeStates(final IBlockState[] p_freeStates_0_) {
        synchronized (RegionRenderCache.cacheStates) {
            if (RegionRenderCache.cacheStates.size() < RegionRenderCache.maxCacheSize) {
                RegionRenderCache.cacheStates.add(p_freeStates_0_);
            }
        }
        // monitorexit(RegionRenderCache.cacheStates)
    }
}
