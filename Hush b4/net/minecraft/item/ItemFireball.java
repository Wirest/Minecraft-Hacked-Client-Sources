// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemFireball extends Item
{
    public ItemFireball() {
        this.setCreativeTab(CreativeTabs.tabMisc);
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        pos = pos.offset(side);
        if (!playerIn.canPlayerEdit(pos, side, stack)) {
            return false;
        }
        if (worldIn.getBlockState(pos).getBlock().getMaterial() == Material.air) {
            worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "item.fireCharge.use", 1.0f, (ItemFireball.itemRand.nextFloat() - ItemFireball.itemRand.nextFloat()) * 0.2f + 1.0f);
            worldIn.setBlockState(pos, Blocks.fire.getDefaultState());
        }
        if (!playerIn.capabilities.isCreativeMode) {
            --stack.stackSize;
        }
        return true;
    }
}
