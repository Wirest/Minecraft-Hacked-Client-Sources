package net.minecraft.client.renderer;

import java.util.Arrays;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class RegionRenderCache extends ChunkCache
{
    private static final IBlockState DEFAULT_STATE = Blocks.air.getDefaultState();
    private final BlockPos position;
    private int[] combinedLights;
    private IBlockState[] blockStates;

    public RegionRenderCache(World worldIn, BlockPos posFromIn, BlockPos posToIn, int subIn)
    {
        super(worldIn, posFromIn, posToIn, subIn);
        this.position = posFromIn.subtract(new Vec3i(subIn, subIn, subIn));
        this.combinedLights = new int[8000];
        Arrays.fill(this.combinedLights, -1);
        this.blockStates = new IBlockState[8000];
    }

    @Override
	public TileEntity getTileEntity(BlockPos pos)
    {
        int var2 = (pos.getX() >> 4) - this.chunkX;
        int var3 = (pos.getZ() >> 4) - this.chunkZ;
        return this.chunkArray[var2][var3].getTileEntity(pos, Chunk.EnumCreateEntityType.QUEUED);
    }

    @Override
	public int getCombinedLight(BlockPos pos, int lightValue)
    {
        int var3 = this.getPositionIndex(pos);
        int var4 = this.combinedLights[var3];

        if (var4 == -1)
        {
            var4 = super.getCombinedLight(pos, lightValue);
            this.combinedLights[var3] = var4;
        }

        return var4;
    }

    @Override
	public IBlockState getBlockState(BlockPos pos)
    {
        int var2 = this.getPositionIndex(pos);
        IBlockState var3 = this.blockStates[var2];

        if (var3 == null)
        {
            var3 = this.getBlockStateRaw(pos);
            this.blockStates[var2] = var3;
        }

        return var3;
    }

    private IBlockState getBlockStateRaw(BlockPos pos)
    {
        if (pos.getY() >= 0 && pos.getY() < 256)
        {
            int var2 = (pos.getX() >> 4) - this.chunkX;
            int var3 = (pos.getZ() >> 4) - this.chunkZ;
            return this.chunkArray[var2][var3].getBlockState(pos);
        }
        else
        {
            return DEFAULT_STATE;
        }
    }

    private int getPositionIndex(BlockPos p_175630_1_)
    {
        int var2 = p_175630_1_.getX() - this.position.getX();
        int var3 = p_175630_1_.getY() - this.position.getY();
        int var4 = p_175630_1_.getZ() - this.position.getZ();
        return var2 * 400 + var4 * 20 + var3;
    }
}
