package net.minecraft.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLilyPad extends BlockBush {
    private static final String __OBFID = "CL_00000332";

    protected BlockLilyPad() {
        float var1 = 0.5F;
        float var2 = 0.015625F;
        this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, var2, 0.5F + var1);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    /**
     * Add all collision boxes of this Block to the list that intersect with the given mask.
     *
     * @param collidingEntity the Entity colliding with this Block
     */
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
        if (collidingEntity == null || !(collidingEntity instanceof EntityBoat)) {
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return new AxisAlignedBB((double) pos.getX() + this.minX, (double) pos.getY() + this.minY, (double) pos.getZ() + this.minZ, (double) pos.getX() + this.maxX, (double) pos.getY() + this.maxY, (double) pos.getZ() + this.maxZ);
    }

    public int getBlockColor() {
        return 7455580;
    }

    public int getRenderColor(IBlockState state) {
        return 7455580;
    }

    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        return 2129968;
    }

    /**
     * is the block grass, dirt or farmland
     */
    protected boolean canPlaceBlockOn(Block ground) {
        return ground == Blocks.water;
    }

    public boolean canBlockStay(World worldIn, BlockPos p_180671_2_, IBlockState p_180671_3_) {
        if (p_180671_2_.getY() >= 0 && p_180671_2_.getY() < 256) {
            IBlockState var4 = worldIn.getBlockState(p_180671_2_.offsetDown());
            return var4.getBlock().getMaterial() == Material.water && ((Integer) var4.getValue(BlockLiquid.LEVEL)).intValue() == 0;
        } else {
            return false;
        }
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return 0;
    }
}
