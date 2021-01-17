// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import com.google.common.collect.Lists;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;

public class RecipesArmorDyes implements IRecipe
{
    @Override
    public boolean matches(final InventoryCrafting inv, final World worldIn) {
        ItemStack itemstack = null;
        final List<ItemStack> list = (List<ItemStack>)Lists.newArrayList();
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            final ItemStack itemstack2 = inv.getStackInSlot(i);
            if (itemstack2 != null) {
                if (itemstack2.getItem() instanceof ItemArmor) {
                    final ItemArmor itemarmor = (ItemArmor)itemstack2.getItem();
                    if (itemarmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER || itemstack != null) {
                        return false;
                    }
                    itemstack = itemstack2;
                }
                else {
                    if (itemstack2.getItem() != Items.dye) {
                        return false;
                    }
                    list.add(itemstack2);
                }
            }
        }
        return itemstack != null && !list.isEmpty();
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        ItemStack itemstack = null;
        final int[] aint = new int[3];
        int i = 0;
        int j = 0;
        ItemArmor itemarmor = null;
        for (int k = 0; k < inv.getSizeInventory(); ++k) {
            final ItemStack itemstack2 = inv.getStackInSlot(k);
            if (itemstack2 != null) {
                if (itemstack2.getItem() instanceof ItemArmor) {
                    itemarmor = (ItemArmor)itemstack2.getItem();
                    if (itemarmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER || itemstack != null) {
                        return null;
                    }
                    itemstack = itemstack2.copy();
                    itemstack.stackSize = 1;
                    if (itemarmor.hasColor(itemstack2)) {
                        final int l = itemarmor.getColor(itemstack);
                        final float f = (l >> 16 & 0xFF) / 255.0f;
                        final float f2 = (l >> 8 & 0xFF) / 255.0f;
                        final float f3 = (l & 0xFF) / 255.0f;
                        i += (int)(Math.max(f, Math.max(f2, f3)) * 255.0f);
                        aint[0] += (int)(f * 255.0f);
                        aint[1] += (int)(f2 * 255.0f);
                        aint[2] += (int)(f3 * 255.0f);
                        ++j;
                    }
                }
                else {
                    if (itemstack2.getItem() != Items.dye) {
                        return null;
                    }
                    final float[] afloat = EntitySheep.func_175513_a(EnumDyeColor.byDyeDamage(itemstack2.getMetadata()));
                    final int l2 = (int)(afloat[0] * 255.0f);
                    final int i2 = (int)(afloat[1] * 255.0f);
                    final int j2 = (int)(afloat[2] * 255.0f);
                    i += Math.max(l2, Math.max(i2, j2));
                    final int[] array = aint;
                    final int n = 0;
                    array[n] += l2;
                    final int[] array2 = aint;
                    final int n2 = 1;
                    array2[n2] += i2;
                    final int[] array3 = aint;
                    final int n3 = 2;
                    array3[n3] += j2;
                    ++j;
                }
            }
        }
        if (itemarmor == null) {
            return null;
        }
        int i3 = aint[0] / j;
        int j3 = aint[1] / j;
        int k2 = aint[2] / j;
        final float f4 = i / (float)j;
        final float f5 = (float)Math.max(i3, Math.max(j3, k2));
        i3 = (int)(i3 * f4 / f5);
        j3 = (int)(j3 * f4 / f5);
        k2 = (int)(k2 * f4 / f5);
        int lvt_12_3_ = (i3 << 8) + j3;
        lvt_12_3_ = (lvt_12_3_ << 8) + k2;
        itemarmor.setColor(itemstack, lvt_12_3_);
        return itemstack;
    }
    
    @Override
    public int getRecipeSize() {
        return 10;
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
