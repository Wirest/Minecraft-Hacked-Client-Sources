// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.stats.StatList;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapData;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;

public class ItemEmptyMap extends ItemMapBase
{
    protected ItemEmptyMap() {
        this.setCreativeTab(CreativeTabs.tabMisc);
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        final ItemStack itemstack = new ItemStack(Items.filled_map, 1, worldIn.getUniqueDataId("map"));
        final String s = "map_" + itemstack.getMetadata();
        final MapData mapdata = new MapData(s);
        worldIn.setItemData(s, mapdata);
        mapdata.scale = 0;
        mapdata.calculateMapCenter(playerIn.posX, playerIn.posZ, mapdata.scale);
        mapdata.dimension = (byte)worldIn.provider.getDimensionId();
        mapdata.markDirty();
        --itemStackIn.stackSize;
        if (itemStackIn.stackSize <= 0) {
            return itemstack;
        }
        if (!playerIn.inventory.addItemStackToInventory(itemstack.copy())) {
            playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
        }
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStackIn;
    }
}
