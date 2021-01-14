package net.minecraft.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.BiomeGenBase;

public interface IBlockAccess
{
    TileEntity getTileEntity(BlockPos var1);

    int getCombinedLight(BlockPos var1, int var2);

    IBlockState getBlockState(BlockPos var1);

    boolean isAirBlock(BlockPos var1);

    BiomeGenBase getBiomeGenForCoords(BlockPos var1);

    /**
     * set by !chunk.getAreLevelsEmpty
     */
    boolean extendedLevelsInChunkCache();

    int getStrongPower(BlockPos var1, EnumFacing var2);

    WorldType getWorldType();
}
