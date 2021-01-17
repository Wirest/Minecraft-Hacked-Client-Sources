// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import net.minecraft.item.Item;
import java.util.List;
import net.minecraft.item.ItemStack;
import com.google.common.collect.Lists;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;

public class RecipeRepairItem implements IRecipe
{
    @Override
    public boolean matches(final InventoryCrafting inv, final World worldIn) {
        final List<ItemStack> list = (List<ItemStack>)Lists.newArrayList();
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            final ItemStack itemstack = inv.getStackInSlot(i);
            if (itemstack != null) {
                list.add(itemstack);
                if (list.size() > 1) {
                    final ItemStack itemstack2 = list.get(0);
                    if (itemstack.getItem() != itemstack2.getItem() || itemstack2.stackSize != 1 || itemstack.stackSize != 1 || !itemstack2.getItem().isDamageable()) {
                        return false;
                    }
                }
            }
        }
        return list.size() == 2;
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        final List<ItemStack> list = (List<ItemStack>)Lists.newArrayList();
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            final ItemStack itemstack = inv.getStackInSlot(i);
            if (itemstack != null) {
                list.add(itemstack);
                if (list.size() > 1) {
                    final ItemStack itemstack2 = list.get(0);
                    if (itemstack.getItem() != itemstack2.getItem() || itemstack2.stackSize != 1 || itemstack.stackSize != 1 || !itemstack2.getItem().isDamageable()) {
                        return null;
                    }
                }
            }
        }
        if (list.size() == 2) {
            final ItemStack itemstack3 = list.get(0);
            final ItemStack itemstack4 = list.get(1);
            if (itemstack3.getItem() == itemstack4.getItem() && itemstack3.stackSize == 1 && itemstack4.stackSize == 1 && itemstack3.getItem().isDamageable()) {
                final Item item = itemstack3.getItem();
                final int j = item.getMaxDamage() - itemstack3.getItemDamage();
                final int k = item.getMaxDamage() - itemstack4.getItemDamage();
                final int l = j + k + item.getMaxDamage() * 5 / 100;
                int i2 = item.getMaxDamage() - l;
                if (i2 < 0) {
                    i2 = 0;
                }
                return new ItemStack(itemstack3.getItem(), 1, i2);
            }
        }
        return null;
    }
    
    @Override
    public int getRecipeSize() {
        return 4;
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
