// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import java.util.List;
import net.minecraft.item.ItemDye;
import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class RecipeFireworks implements IRecipe
{
    private ItemStack field_92102_a;
    
    @Override
    public boolean matches(final InventoryCrafting inv, final World worldIn) {
        this.field_92102_a = null;
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        int i2 = 0;
        int j2 = 0;
        for (int k2 = 0; k2 < inv.getSizeInventory(); ++k2) {
            final ItemStack itemstack = inv.getStackInSlot(k2);
            if (itemstack != null) {
                if (itemstack.getItem() == Items.gunpowder) {
                    ++j;
                }
                else if (itemstack.getItem() == Items.firework_charge) {
                    ++l;
                }
                else if (itemstack.getItem() == Items.dye) {
                    ++k;
                }
                else if (itemstack.getItem() == Items.paper) {
                    ++i;
                }
                else if (itemstack.getItem() == Items.glowstone_dust) {
                    ++i2;
                }
                else if (itemstack.getItem() == Items.diamond) {
                    ++i2;
                }
                else if (itemstack.getItem() == Items.fire_charge) {
                    ++j2;
                }
                else if (itemstack.getItem() == Items.feather) {
                    ++j2;
                }
                else if (itemstack.getItem() == Items.gold_nugget) {
                    ++j2;
                }
                else {
                    if (itemstack.getItem() != Items.skull) {
                        return false;
                    }
                    ++j2;
                }
            }
        }
        i2 = i2 + k + j2;
        if (j > 3 || i > 1) {
            return false;
        }
        if (j >= 1 && i == 1 && i2 == 0) {
            this.field_92102_a = new ItemStack(Items.fireworks);
            if (l > 0) {
                final NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                final NBTTagList nbttaglist = new NBTTagList();
                for (int k3 = 0; k3 < inv.getSizeInventory(); ++k3) {
                    final ItemStack itemstack2 = inv.getStackInSlot(k3);
                    if (itemstack2 != null && itemstack2.getItem() == Items.firework_charge && itemstack2.hasTagCompound() && itemstack2.getTagCompound().hasKey("Explosion", 10)) {
                        nbttaglist.appendTag(itemstack2.getTagCompound().getCompoundTag("Explosion"));
                    }
                }
                nbttagcompound2.setTag("Explosions", nbttaglist);
                nbttagcompound2.setByte("Flight", (byte)j);
                nbttagcompound1.setTag("Fireworks", nbttagcompound2);
                this.field_92102_a.setTagCompound(nbttagcompound1);
            }
            return true;
        }
        if (j == 1 && i == 0 && l == 0 && k > 0 && j2 <= 1) {
            this.field_92102_a = new ItemStack(Items.firework_charge);
            final NBTTagCompound nbttagcompound3 = new NBTTagCompound();
            final NBTTagCompound nbttagcompound4 = new NBTTagCompound();
            byte b0 = 0;
            final List<Integer> list = (List<Integer>)Lists.newArrayList();
            for (int l2 = 0; l2 < inv.getSizeInventory(); ++l2) {
                final ItemStack itemstack3 = inv.getStackInSlot(l2);
                if (itemstack3 != null) {
                    if (itemstack3.getItem() == Items.dye) {
                        list.add(ItemDye.dyeColors[itemstack3.getMetadata() & 0xF]);
                    }
                    else if (itemstack3.getItem() == Items.glowstone_dust) {
                        nbttagcompound4.setBoolean("Flicker", true);
                    }
                    else if (itemstack3.getItem() == Items.diamond) {
                        nbttagcompound4.setBoolean("Trail", true);
                    }
                    else if (itemstack3.getItem() == Items.fire_charge) {
                        b0 = 1;
                    }
                    else if (itemstack3.getItem() == Items.feather) {
                        b0 = 4;
                    }
                    else if (itemstack3.getItem() == Items.gold_nugget) {
                        b0 = 2;
                    }
                    else if (itemstack3.getItem() == Items.skull) {
                        b0 = 3;
                    }
                }
            }
            final int[] aint1 = new int[list.size()];
            for (int l3 = 0; l3 < aint1.length; ++l3) {
                aint1[l3] = list.get(l3);
            }
            nbttagcompound4.setIntArray("Colors", aint1);
            nbttagcompound4.setByte("Type", b0);
            nbttagcompound3.setTag("Explosion", nbttagcompound4);
            this.field_92102_a.setTagCompound(nbttagcompound3);
            return true;
        }
        if (j != 0 || i != 0 || l != 1 || k <= 0 || k != i2) {
            return false;
        }
        final List<Integer> list2 = (List<Integer>)Lists.newArrayList();
        for (int i3 = 0; i3 < inv.getSizeInventory(); ++i3) {
            final ItemStack itemstack4 = inv.getStackInSlot(i3);
            if (itemstack4 != null) {
                if (itemstack4.getItem() == Items.dye) {
                    list2.add(ItemDye.dyeColors[itemstack4.getMetadata() & 0xF]);
                }
                else if (itemstack4.getItem() == Items.firework_charge) {
                    this.field_92102_a = itemstack4.copy();
                    this.field_92102_a.stackSize = 1;
                }
            }
        }
        final int[] aint2 = new int[list2.size()];
        for (int j3 = 0; j3 < aint2.length; ++j3) {
            aint2[j3] = list2.get(j3);
        }
        if (this.field_92102_a == null || !this.field_92102_a.hasTagCompound()) {
            return false;
        }
        final NBTTagCompound nbttagcompound5 = this.field_92102_a.getTagCompound().getCompoundTag("Explosion");
        if (nbttagcompound5 == null) {
            return false;
        }
        nbttagcompound5.setIntArray("FadeColors", aint2);
        return true;
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        return this.field_92102_a.copy();
    }
    
    @Override
    public int getRecipeSize() {
        return 10;
    }
    
    @Override
    public ItemStack getRecipeOutput() {
        return this.field_92102_a;
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
