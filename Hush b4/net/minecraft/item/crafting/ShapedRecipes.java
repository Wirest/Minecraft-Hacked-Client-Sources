// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class ShapedRecipes implements IRecipe
{
    private final int recipeWidth;
    private final int recipeHeight;
    private final ItemStack[] recipeItems;
    private final ItemStack recipeOutput;
    private boolean copyIngredientNBT;
    
    public ShapedRecipes(final int width, final int height, final ItemStack[] p_i1917_3_, final ItemStack output) {
        this.recipeWidth = width;
        this.recipeHeight = height;
        this.recipeItems = p_i1917_3_;
        this.recipeOutput = output;
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
        for (int i = 0; i <= 3 - this.recipeWidth; ++i) {
            for (int j = 0; j <= 3 - this.recipeHeight; ++j) {
                if (this.checkMatch(inv, i, j, true)) {
                    return true;
                }
                if (this.checkMatch(inv, i, j, false)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean checkMatch(final InventoryCrafting p_77573_1_, final int p_77573_2_, final int p_77573_3_, final boolean p_77573_4_) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                final int k = i - p_77573_2_;
                final int l = j - p_77573_3_;
                ItemStack itemstack = null;
                if (k >= 0 && l >= 0 && k < this.recipeWidth && l < this.recipeHeight) {
                    if (p_77573_4_) {
                        itemstack = this.recipeItems[this.recipeWidth - k - 1 + l * this.recipeWidth];
                    }
                    else {
                        itemstack = this.recipeItems[k + l * this.recipeWidth];
                    }
                }
                final ItemStack itemstack2 = p_77573_1_.getStackInRowAndColumn(i, j);
                if (itemstack2 != null || itemstack != null) {
                    if ((itemstack2 == null && itemstack != null) || (itemstack2 != null && itemstack == null)) {
                        return false;
                    }
                    if (itemstack.getItem() != itemstack2.getItem()) {
                        return false;
                    }
                    if (itemstack.getMetadata() != 32767 && itemstack.getMetadata() != itemstack2.getMetadata()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        final ItemStack itemstack = this.getRecipeOutput().copy();
        if (this.copyIngredientNBT) {
            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                final ItemStack itemstack2 = inv.getStackInSlot(i);
                if (itemstack2 != null && itemstack2.hasTagCompound()) {
                    itemstack.setTagCompound((NBTTagCompound)itemstack2.getTagCompound().copy());
                }
            }
        }
        return itemstack;
    }
    
    @Override
    public int getRecipeSize() {
        return this.recipeWidth * this.recipeHeight;
    }
}
