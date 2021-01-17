// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockBed;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.MathHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemBed extends Item
{
    public ItemBed() {
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        if (side != EnumFacing.UP) {
            return false;
        }
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        final boolean flag = block.isReplaceable(worldIn, pos);
        if (!flag) {
            pos = pos.up();
        }
        final int i = MathHelper.floor_double(playerIn.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3;
        final EnumFacing enumfacing = EnumFacing.getHorizontal(i);
        final BlockPos blockpos = pos.offset(enumfacing);
        if (!playerIn.canPlayerEdit(pos, side, stack) || !playerIn.canPlayerEdit(blockpos, side, stack)) {
            return false;
        }
        final boolean flag2 = worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
        final boolean flag3 = flag || worldIn.isAirBlock(pos);
        final boolean flag4 = flag2 || worldIn.isAirBlock(blockpos);
        if (flag3 && flag4 && World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && World.doesBlockHaveSolidTopSurface(worldIn, blockpos.down())) {
            final IBlockState iblockstate2 = Blocks.bed.getDefaultState().withProperty((IProperty<Comparable>)BlockBed.OCCUPIED, false).withProperty((IProperty<Comparable>)BlockBed.FACING, enumfacing).withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);
            if (worldIn.setBlockState(pos, iblockstate2, 3)) {
                final IBlockState iblockstate3 = iblockstate2.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD);
                worldIn.setBlockState(blockpos, iblockstate3, 3);
            }
            --stack.stackSize;
            return true;
        }
        return false;
    }
}
