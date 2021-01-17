// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockSnow;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;

public class ItemReed extends Item
{
    private Block block;
    
    public ItemReed(final Block block) {
        this.block = block;
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, BlockPos pos, EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        if (block == Blocks.snow_layer && iblockstate.getValue((IProperty<Integer>)BlockSnow.LAYERS) < 1) {
            side = EnumFacing.UP;
        }
        else if (!block.isReplaceable(worldIn, pos)) {
            pos = pos.offset(side);
        }
        if (!playerIn.canPlayerEdit(pos, side, stack)) {
            return false;
        }
        if (stack.stackSize == 0) {
            return false;
        }
        if (worldIn.canBlockBePlaced(this.block, pos, false, side, null, stack)) {
            IBlockState iblockstate2 = this.block.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, 0, playerIn);
            if (worldIn.setBlockState(pos, iblockstate2, 3)) {
                iblockstate2 = worldIn.getBlockState(pos);
                if (iblockstate2.getBlock() == this.block) {
                    ItemBlock.setTileEntityNBT(worldIn, playerIn, pos, stack);
                    iblockstate2.getBlock().onBlockPlacedBy(worldIn, pos, iblockstate2, playerIn, stack);
                }
                worldIn.playSoundEffect(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0f) / 2.0f, this.block.stepSound.getFrequency() * 0.8f);
                --stack.stackSize;
                return true;
            }
        }
        return false;
    }
}
