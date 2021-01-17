// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RecipesMapExtending extends ShapedRecipes
{
    public RecipesMapExtending() {
        super(3, 3, new ItemStack[] { new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack(Items.filled_map, 0, 32767), new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack(Items.paper), new ItemStack(Items.paper) }, new ItemStack(Items.map, 0, 0));
    }
    
    @Override
    public boolean matches(final InventoryCrafting inv, final World worldIn) {
        if (!super.matches(inv, worldIn)) {
            return false;
        }
        ItemStack itemstack = null;
        for (int i = 0; i < inv.getSizeInventory() && itemstack == null; ++i) {
            final ItemStack itemstack2 = inv.getStackInSlot(i);
            if (itemstack2 != null && itemstack2.getItem() == Items.filled_map) {
                itemstack = itemstack2;
            }
        }
        if (itemstack == null) {
            return false;
        }
        final MapData mapdata = Items.filled_map.getMapData(itemstack, worldIn);
        return mapdata != null && mapdata.scale < 4;
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        ItemStack itemstack = null;
        for (int i = 0; i < inv.getSizeInventory() && itemstack == null; ++i) {
            final ItemStack itemstack2 = inv.getStackInSlot(i);
            if (itemstack2 != null && itemstack2.getItem() == Items.filled_map) {
                itemstack = itemstack2;
            }
        }
        itemstack = itemstack.copy();
        itemstack.stackSize = 1;
        if (itemstack.getTagCompound() == null) {
            itemstack.setTagCompound(new NBTTagCompound());
        }
        itemstack.getTagCompound().setBoolean("map_is_scaling", true);
        return itemstack;
    }
}
