package net.minecraft.client.renderer;

import java.util.ArrayDeque;
import java.util.Arrays;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import optifine.Config;
import optifine.DynamicLights;

public class RegionRenderCache extends ChunkCache
{
    private static final IBlockState DEFAULT_STATE = Blocks.air.getDefaultState();
    private final BlockPos position;
    private int[] combinedLights;
    private IBlockState[] blockStates;
    private static final String __OBFID = "CL_00002565";
    private static ArrayDeque<int[]> cacheLights = new ArrayDeque();
    private static ArrayDeque<IBlockState[]> cacheStates = new ArrayDeque();
    private static int maxCacheSize = Config.limit(Runtime.getRuntime().availableProcessors(), 1, 32);

    public RegionRenderCache(World worldIn, BlockPos posFromIn, BlockPos posToIn, int subIn)
    {
        super(worldIn, posFromIn, posToIn, subIn);
        this.position = posFromIn.subtract(new Vec3i(subIn, subIn, subIn));
        boolean flag = true;
        this.combinedLights = allocateLights(8000);
        Arrays.fill((int[])this.combinedLights, (int) - 1);
        this.blockStates = allocateStates(8000);
    }

    public TileEntity getTileEntity(BlockPos pos)
    {
        int i = (pos.getX() >> 4) - this.chunkX;
        int j = (pos.getZ() >> 4) - this.chunkZ;

        if (i >= 0 && i < this.chunkArray.length)
        {
            Chunk[] achunk = this.chunkArray[i];

            if (j >= 0 && j < achunk.length)
            {
                Chunk chunk = achunk[j];
                return chunk == null ? null : chunk.getTileEntity(pos, Chunk.EnumCreateEntityType.QUEUED);
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    public int getCombinedLight(BlockPos pos, int lightValue)
    {
        int i = this.getPositionIndex(pos);

        if (i >= 0 && i < this.combinedLights.length)
        {
            int j = this.combinedLights[i];

            if (j == -1)
            {
                j = this.getCombinedLightRaw(pos, lightValue);
                this.combinedLights[i] = j;
            }

            return j;
        }
        else
        {
            return this.getCombinedLightRaw(pos, lightValue);
        }
    }

    private int getCombinedLightRaw(BlockPos p_getCombinedLightRaw_1_, int p_getCombinedLightRaw_2_)
    {
        int i = super.getCombinedLight(p_getCombinedLightRaw_1_, p_getCombinedLightRaw_2_);

        if (Config.isDynamicLights() && !this.getBlockState(p_getCombinedLightRaw_1_).getBlock().isOpaqueCube())
        {
            i = DynamicLights.getCombinedLight(p_getCombinedLightRaw_1_, i);
        }

        return i;
    }

    public IBlockState getBlockState(BlockPos pos)
    {
        int i = this.getPositionIndex(pos);

        if (i >= 0 && i < this.blockStates.length)
        {
            IBlockState iblockstate = this.blockStates[i];

            if (iblockstate == null)
            {
                iblockstate = this.getBlockStateRaw(pos);
                this.blockStates[i] = iblockstate;
            }

            return iblockstate;
        }
        else
        {
            return this.getBlockStateRaw(pos);
        }
    }

    private IBlockState getBlockStateRaw(BlockPos pos)
    {
        return super.getBlockState(pos);
    }

    private int getPositionIndex(BlockPos p_175630_1_)
    {
        int i = p_175630_1_.getX() - this.position.getX();
        int j = p_175630_1_.getY() - this.position.getY();
        int k = p_175630_1_.getZ() - this.position.getZ();
        return i >= 0 && j >= 0 && k >= 0 && i < 20 && j < 20 && k < 20 ? i * 400 + k * 20 + j : -1;
    }

    public void freeBuffers()
    {
        freeLights(this.combinedLights);
        freeStates(this.blockStates);
    }

    private static int[] allocateLights(int p_allocateLights_0_)
    {
        synchronized (cacheLights)
        {
            int[] aint = (int[])cacheLights.pollLast();

            if (aint == null || aint.length < p_allocateLights_0_)
            {
                aint = new int[p_allocateLights_0_];
            }

            return aint;
        }
    }

    public static void freeLights(int[] p_freeLights_0_)
    {
        synchronized (cacheLights)
        {
            if (cacheLights.size() < maxCacheSize)
            {
                cacheLights.add(p_freeLights_0_);
            }
        }
    }

    private static IBlockState[] allocateStates(int p_allocateStates_0_)
    {
        synchronized (cacheStates)
        {
            IBlockState[] aiblockstate = (IBlockState[])cacheStates.pollLast();

            if (aiblockstate != null && aiblockstate.length >= p_allocateStates_0_)
            {
                Arrays.fill(aiblockstate, (Object)null);
            }
            else
            {
                aiblockstate = new IBlockState[p_allocateStates_0_];
            }

            return aiblockstate;
        }
    }

    public static void freeStates(IBlockState[] p_freeStates_0_)
    {
        synchronized (cacheStates)
        {
            if (cacheStates.size() < maxCacheSize)
            {
                cacheStates.add(p_freeStates_0_);
            }
        }
    }
}
