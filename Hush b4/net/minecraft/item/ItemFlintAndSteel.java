// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemFlintAndSteel extends Item
{
    public ItemFlintAndSteel() {
        this.maxStackSize = 1;
        this.setMaxDamage(64);
        this.setCreativeTab(CreativeTabs.tabTools);
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        pos = pos.offset(side);
        if (!playerIn.canPlayerEdit(pos, side, stack)) {
            return false;
        }
        if (worldIn.getBlockState(pos).getBlock().getMaterial() == Material.air) {
            worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "fire.ignite", 1.0f, ItemFlintAndSteel.itemRand.nextFloat() * 0.4f + 0.8f);
            worldIn.setBlockState(pos, Blocks.fire.getDefaultState());
        }
        stack.damageItem(1, playerIn);
        return true;
    }
}
