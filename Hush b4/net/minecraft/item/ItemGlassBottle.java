// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.BlockPos;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.block.material.Material;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;

public class ItemGlassBottle extends Item
{
    public ItemGlassBottle() {
        this.setCreativeTab(CreativeTabs.tabBrewing);
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        final MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, true);
        if (movingobjectposition == null) {
            return itemStackIn;
        }
        if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            final BlockPos blockpos = movingobjectposition.getBlockPos();
            if (!worldIn.isBlockModifiable(playerIn, blockpos)) {
                return itemStackIn;
            }
            if (!playerIn.canPlayerEdit(blockpos.offset(movingobjectposition.sideHit), movingobjectposition.sideHit, itemStackIn)) {
                return itemStackIn;
            }
            if (worldIn.getBlockState(blockpos).getBlock().getMaterial() == Material.water) {
                --itemStackIn.stackSize;
                playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                if (itemStackIn.stackSize <= 0) {
                    return new ItemStack(Items.potionitem);
                }
                if (!playerIn.inventory.addItemStackToInventory(new ItemStack(Items.potionitem))) {
                    playerIn.dropPlayerItemWithRandomChoice(new ItemStack(Items.potionitem, 1, 0), false);
                }
            }
        }
        return itemStackIn;
    }
}
