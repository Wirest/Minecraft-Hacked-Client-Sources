// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.dispenser;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

public interface IBlockSource extends ILocatableSource
{
    double getX();
    
    double getY();
    
    double getZ();
    
    BlockPos getBlockPos();
    
    int getBlockMetadata();
    
     <T extends TileEntity> T getBlockTileEntity();
}
