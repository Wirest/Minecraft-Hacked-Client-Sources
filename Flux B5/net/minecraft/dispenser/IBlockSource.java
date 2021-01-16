package net.minecraft.dispenser;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

public interface IBlockSource extends ILocatableSource
{
    double getX();

    double getY();

    double getZ();

    BlockPos getBlockPos();

    Block getBlock();

    int getBlockMetadata();

    TileEntity getBlockTileEntity();
}
