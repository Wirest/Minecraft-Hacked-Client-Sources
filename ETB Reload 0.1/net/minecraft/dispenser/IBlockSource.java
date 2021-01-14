package net.minecraft.dispenser;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

public interface IBlockSource extends ILocatableSource
{
    @Override
	double getX();

    @Override
	double getY();

    @Override
	double getZ();

    BlockPos getBlockPos();

    int getBlockMetadata();

    <T extends TileEntity> T getBlockTileEntity();
}
