// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;

public class BlockLilyPad extends BlockBush
{
    protected BlockLilyPad() {
        final float f = 0.5f;
        final float f2 = 0.015625f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, f2, 0.5f + f);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List<AxisAlignedBB> list, final Entity collidingEntity) {
        if (collidingEntity == null || !(collidingEntity instanceof EntityBoat)) {
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new AxisAlignedBB(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ);
    }
    
    @Override
    public int getBlockColor() {
        return 7455580;
    }
    
    @Override
    public int getRenderColor(final IBlockState state) {
        return 7455580;
    }
    
    @Override
    public int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos, final int renderPass) {
        return 2129968;
    }
    
    @Override
    protected boolean canPlaceBlockOn(final Block ground) {
        return ground == Blocks.water;
    }
    
    @Override
    public boolean canBlockStay(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (pos.getY() >= 0 && pos.getY() < 256) {
            final IBlockState iblockstate = worldIn.getBlockState(pos.down());
            return iblockstate.getBlock().getMaterial() == Material.water && iblockstate.getValue((IProperty<Integer>)BlockLiquid.LEVEL) == 0;
        }
        return false;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return 0;
    }
}
