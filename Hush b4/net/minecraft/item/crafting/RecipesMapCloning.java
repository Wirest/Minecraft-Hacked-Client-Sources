// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;

public class RecipesMapCloning implements IRecipe
{
    @Override
    public boolean matches(final InventoryCrafting inv, final World worldIn) {
        int i = 0;
        ItemStack itemstack = null;
        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            final ItemStack itemstack2 = inv.getStackInSlot(j);
            if (itemstack2 != null) {
                if (itemstack2.getItem() == Items.filled_map) {
                    if (itemstack != null) {
                        return false;
                    }
                    itemstack = itemstack2;
                }
                else {
                    if (itemstack2.getItem() != Items.map) {
                        return false;
                    }
                    ++i;
                }
            }
        }
        return itemstack != null && i > 0;
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        int i = 0;
        ItemStack itemstack = null;
        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            final ItemStack itemstack2 = inv.getStackInSlot(j);
            if (itemstack2 != null) {
                if (itemstack2.getItem() == Items.filled_map) {
                    if (itemstack != null) {
                        return null;
                    }
                    itemstack = itemstack2;
                }
                else {
                    if (itemstack2.getItem() != Items.map) {
                        return null;
                    }
                    ++i;
                }
            }
        }
        if (itemstack != null && i >= 1) {
            final ItemStack itemstack3 = new ItemStack(Items.filled_map, i + 1, itemstack.getMetadata());
            if (itemstack.hasDisplayName()) {
                itemstack3.setStackDisplayName(itemstack.getDisplayName());
            }
            return itemstack3;
        }
        return null;
    }
    
    @Override
    public int getRecipeSize() {
        return 9;
    }
    
    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }
    
    @Override
    public ItemStack[] getRemainingItems(final InventoryCrafting inv) {
        final ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
        for (int i = 0; i < aitemstack.length; ++i) {
            final ItemStack itemstack = inv.getStackInSlot(i);
            if (itemstack != null && itemstack.getItem().hasContainerItem()) {
                aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
            }
        }
        return aitemstack;
    }
}
