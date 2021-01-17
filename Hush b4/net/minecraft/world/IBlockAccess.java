// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

public interface IBlockAccess
{
    TileEntity getTileEntity(final BlockPos p0);
    
    int getCombinedLight(final BlockPos p0, final int p1);
    
    IBlockState getBlockState(final BlockPos p0);
    
    boolean isAirBlock(final BlockPos p0);
    
    BiomeGenBase getBiomeGenForCoords(final BlockPos p0);
    
    boolean extendedLevelsInChunkCache();
    
    int getStrongPower(final BlockPos p0, final EnumFacing p1);
    
    WorldType getWorldType();
}
