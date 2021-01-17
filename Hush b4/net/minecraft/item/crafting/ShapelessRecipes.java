// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import java.util.Iterator;
import com.google.common.collect.Lists;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import java.util.List;
import net.minecraft.item.ItemStack;

public class ShapelessRecipes implements IRecipe
{
    private final ItemStack recipeOutput;
    private final List<ItemStack> recipeItems;
    
    public ShapelessRecipes(final ItemStack output, final List<ItemStack> inputList) {
        this.recipeOutput = output;
        this.recipeItems = inputList;
    }
    
    @Override
    public ItemStack getRecipeOutput() {
        return this.recipeOutput;
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
    
    @Override
    public boolean matches(final InventoryCrafting inv, final World worldIn) {
        final List<ItemStack> list = (List<ItemStack>)Lists.newArrayList((Iterable<?>)this.recipeItems);
        for (int i = 0; i < inv.getHeight(); ++i) {
            for (int j = 0; j < inv.getWidth(); ++j) {
                final ItemStack itemstack = inv.getStackInRowAndColumn(j, i);
                if (itemstack != null) {
                    boolean flag = false;
                    for (final ItemStack itemstack2 : list) {
                        if (itemstack.getItem() == itemstack2.getItem() && (itemstack2.getMetadata() == 32767 || itemstack.getMetadata() == itemstack2.getMetadata())) {
                            flag = true;
                            list.remove(itemstack2);
                            break;
                        }
                    }
                    if (!flag) {
                        return false;
                    }
                }
            }
        }
        return list.isEmpty();
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        return this.recipeOutput.copy();
    }
    
    @Override
    public int getRecipeSize() {
        return this.recipeItems.size();
    }
}
