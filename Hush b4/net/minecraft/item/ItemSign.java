// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.util.MathHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemSign extends Item
{
    public ItemSign() {
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (side == EnumFacing.DOWN) {
            return false;
        }
        if (!worldIn.getBlockState(pos).getBlock().getMaterial().isSolid()) {
            return false;
        }
        pos = pos.offset(side);
        if (!playerIn.canPlayerEdit(pos, side, stack)) {
            return false;
        }
        if (!Blocks.standing_sign.canPlaceBlockAt(worldIn, pos)) {
            return false;
        }
        if (worldIn.isRemote) {
            return true;
        }
        if (side == EnumFacing.UP) {
            final int i = MathHelper.floor_double((playerIn.rotationYaw + 180.0f) * 16.0f / 360.0f + 0.5) & 0xF;
            worldIn.setBlockState(pos, Blocks.standing_sign.getDefaultState().withProperty((IProperty<Comparable>)BlockStandingSign.ROTATION, i), 3);
        }
        else {
            worldIn.setBlockState(pos, Blocks.wall_sign.getDefaultState().withProperty((IProperty<Comparable>)BlockWallSign.FACING, side), 3);
        }
        --stack.stackSize;
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntitySign && !ItemBlock.setTileEntityNBT(worldIn, playerIn, pos, stack)) {
            playerIn.openEditSign((TileEntitySign)tileentity);
        }
        return true;
    }
}
