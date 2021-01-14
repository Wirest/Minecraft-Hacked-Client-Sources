package net.minecraft.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockSourceImpl implements IBlockSource {
    private final World worldObj;
    private final BlockPos pos;
    private static final String __OBFID = "CL_00001194";

    public BlockSourceImpl(World worldIn, BlockPos p_i46023_2_) {
        worldObj = worldIn;
        pos = p_i46023_2_;
    }

    @Override
    public World getWorld() {
        return worldObj;
    }

    @Override
    public double getX() {
        return pos.getX() + 0.5D;
    }

    @Override
    public double getY() {
        return pos.getY() + 0.5D;
    }

    @Override
    public double getZ() {
        return pos.getZ() + 0.5D;
    }

    @Override
    public BlockPos getBlockPos() {
        return pos;
    }

    @Override
    public Block getBlock() {
        return worldObj.getBlockState(pos).getBlock();
    }

    @Override
    public int getBlockMetadata() {
        IBlockState var1 = worldObj.getBlockState(pos);
        return var1.getBlock().getMetaFromState(var1);
    }

    @Override
    public TileEntity getBlockTileEntity() {
        return worldObj.getTileEntity(pos);
    }
}
