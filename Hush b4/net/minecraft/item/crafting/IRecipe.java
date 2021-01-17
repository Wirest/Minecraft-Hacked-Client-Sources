// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;

public interface IRecipe
{
    boolean matches(final InventoryCrafting p0, final World p1);
    
    ItemStack getCraftingResult(final InventoryCrafting p0);
    
    int getRecipeSize();
    
    ItemStack getRecipeOutput();
    
    ItemStack[] getRemainingItems(final InventoryCrafting p0);
}
