package net.minecraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemGlassBottle extends Item {
    private static final String __OBFID = "CL_00001776";

    public ItemGlassBottle() {
        this.setCreativeTab(CreativeTabs.tabBrewing);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        MovingObjectPosition var4 = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, true);

        if (var4 == null) {
            return itemStackIn;
        } else {
            if (var4.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos var5 = var4.func_178782_a();

                if (!worldIn.isBlockModifiable(playerIn, var5)) {
                    return itemStackIn;
                }

                if (!playerIn.func_175151_a(var5.offset(var4.field_178784_b), var4.field_178784_b, itemStackIn)) {
                    return itemStackIn;
                }

                if (worldIn.getBlockState(var5).getBlock().getMaterial() == Material.water) {
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
}
