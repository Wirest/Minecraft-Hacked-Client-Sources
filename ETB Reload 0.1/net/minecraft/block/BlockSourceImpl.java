package net.minecraft.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockSourceImpl implements IBlockSource
{
    private final World worldObj;
    private final BlockPos pos;

    public BlockSourceImpl(World worldIn, BlockPos posIn)
    {
        this.worldObj = worldIn;
        this.pos = posIn;
    }

    @Override
	public World getWorld()
    {
        return this.worldObj;
    }

    @Override
	public double getX()
    {
        return this.pos.getX() + 0.5D;
    }

    @Override
	public double getY()
    {
        return this.pos.getY() + 0.5D;
    }

    @Override
	public double getZ()
    {
        return this.pos.getZ() + 0.5D;
    }

    @Override
	public BlockPos getBlockPos()
    {
        return this.pos;
    }

    @Override
	public int getBlockMetadata()
    {
        IBlockState iblockstate = this.worldObj.getBlockState(this.pos);
        return iblockstate.getBlock().getMetaFromState(iblockstate);
    }

    @Override
	public <T extends TileEntity> T getBlockTileEntity()
    {
        return (T)this.worldObj.getTileEntity(this.pos);
    }
}
