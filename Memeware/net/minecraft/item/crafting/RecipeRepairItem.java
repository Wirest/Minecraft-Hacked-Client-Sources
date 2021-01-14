package net.minecraft.item.crafting;

import com.google.common.collect.Lists;

import java.util.ArrayList;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RecipeRepairItem implements IRecipe {
    private static final String __OBFID = "CL_00002156";

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting p_77569_1_, World worldIn) {
        ArrayList var3 = Lists.newArrayList();

        for (int var4 = 0; var4 < p_77569_1_.getSizeInventory(); ++var4) {
            ItemStack var5 = p_77569_1_.getStackInSlot(var4);

            if (var5 != null) {
                var3.add(var5);

                if (var3.size() > 1) {
                    ItemStack var6 = (ItemStack) var3.get(0);

                    if (var5.getItem() != var6.getItem() || var6.stackSize != 1 || var5.stackSize != 1 || !var6.getItem().isDamageable()) {
                        return false;
                    }
                }
            }
        }

        return var3.size() == 2;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
        ArrayList var2 = Lists.newArrayList();
        ItemStack var4;

        for (int var3 = 0; var3 < p_77572_1_.getSizeInventory(); ++var3) {
            var4 = p_77572_1_.getStackInSlot(var3);

            if (var4 != null) {
                var2.add(var4);

                if (var2.size() > 1) {
                    ItemStack var5 = (ItemStack) var2.get(0);

                    if (var4.getItem() != var5.getItem() || var5.stackSize != 1 || var4.stackSize != 1 || !var5.getItem().isDamageable()) {
                        return null;
                    }
                }
            }
        }

        if (var2.size() == 2) {
            ItemStack var10 = (ItemStack) var2.get(0);
            var4 = (ItemStack) var2.get(1);

            if (var10.getItem() == var4.getItem() && var10.stackSize == 1 && var4.stackSize == 1 && var10.getItem().isDamageable()) {
                Item var11 = var10.getItem();
                int var6 = var11.getMaxDamage() - var10.getItemDamage();
                int var7 = var11.getMaxDamage() - var4.getItemDamage();
                int var8 = var6 + var7 + var11.getMaxDamage() * 5 / 100;
                int var9 = var11.getMaxDamage() - var8;

                if (var9 < 0) {
                    var9 = 0;
                }

                return new ItemStack(var10.getItem(), 1, var9);
            }
        }

        return null;
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize() {
        return 4;
    }

    public ItemStack getRecipeOutput() {
        return null;
    }

    public ItemStack[] func_179532_b(InventoryCrafting p_179532_1_) {
        ItemStack[] var2 = new ItemStack[p_179532_1_.getSizeInventory()];

        for (int var3 = 0; var3 < var2.length; ++var3) {
            ItemStack var4 = p_179532_1_.getStackInSlot(var3);

            if (var4 != null && var4.getItem().hasContainerItem()) {
                var2[var3] = new ItemStack(var4.getItem().getContainerItem());
            }
        }

        return var2;
    }
}
