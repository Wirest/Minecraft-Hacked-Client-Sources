// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.Block;

public class ItemDoor extends Item
{
    private Block block;
    
    public ItemDoor(final Block block) {
        this.block = block;
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (side != EnumFacing.UP) {
            return false;
        }
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        if (!block.isReplaceable(worldIn, pos)) {
            pos = pos.offset(side);
        }
        if (!playerIn.canPlayerEdit(pos, side, stack)) {
            return false;
        }
        if (!this.block.canPlaceBlockAt(worldIn, pos)) {
            return false;
        }
        placeDoor(worldIn, pos, EnumFacing.fromAngle(playerIn.rotationYaw), this.block);
        --stack.stackSize;
        return true;
    }
    
    public static void placeDoor(final World worldIn, final BlockPos pos, final EnumFacing facing, final Block door) {
        final BlockPos blockpos = pos.offset(facing.rotateY());
        final BlockPos blockpos2 = pos.offset(facing.rotateYCCW());
        final int i = (worldIn.getBlockState(blockpos2).getBlock().isNormalCube() + worldIn.getBlockState(blockpos2.up()).getBlock().isNormalCube()) ? 1 : 0;
        final int j = (worldIn.getBlockState(blockpos).getBlock().isNormalCube() + worldIn.getBlockState(blockpos.up()).getBlock().isNormalCube()) ? 1 : 0;
        final boolean flag = worldIn.getBlockState(blockpos2).getBlock() == door || worldIn.getBlockState(blockpos2.up()).getBlock() == door;
        final boolean flag2 = worldIn.getBlockState(blockpos).getBlock() == door || worldIn.getBlockState(blockpos.up()).getBlock() == door;
        boolean flag3 = false;
        if ((flag && !flag2) || j > i) {
            flag3 = true;
        }
        final BlockPos blockpos3 = pos.up();
        final IBlockState iblockstate = door.getDefaultState().withProperty((IProperty<Comparable>)BlockDoor.FACING, facing).withProperty(BlockDoor.HINGE, flag3 ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT);
        worldIn.setBlockState(pos, iblockstate.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER), 2);
        worldIn.setBlockState(blockpos3, iblockstate.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 2);
        worldIn.notifyNeighborsOfStateChange(pos, door);
        worldIn.notifyNeighborsOfStateChange(blockpos3, door);
    }
}
