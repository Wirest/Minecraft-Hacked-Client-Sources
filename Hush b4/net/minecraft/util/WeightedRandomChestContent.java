// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.util.Collections;
import com.google.common.collect.Lists;
import net.minecraft.tileentity.TileEntityDispenser;
import java.util.Collection;
import net.minecraft.inventory.IInventory;
import java.util.List;
import java.util.Random;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class WeightedRandomChestContent extends WeightedRandom.Item
{
    private ItemStack theItemId;
    private int minStackSize;
    private int maxStackSize;
    
    public WeightedRandomChestContent(final net.minecraft.item.Item p_i45311_1_, final int p_i45311_2_, final int minimumChance, final int maximumChance, final int itemWeightIn) {
        super(itemWeightIn);
        this.theItemId = new ItemStack(p_i45311_1_, 1, p_i45311_2_);
        this.minStackSize = minimumChance;
        this.maxStackSize = maximumChance;
    }
    
    public WeightedRandomChestContent(final ItemStack stack, final int minimumChance, final int maximumChance, final int itemWeightIn) {
        super(itemWeightIn);
        this.theItemId = stack;
        this.minStackSize = minimumChance;
        this.maxStackSize = maximumChance;
    }
    
    public static void generateChestContents(final Random random, final List<WeightedRandomChestContent> listIn, final IInventory inv, final int max) {
        for (int i = 0; i < max; ++i) {
            final WeightedRandomChestContent weightedrandomchestcontent = WeightedRandom.getRandomItem(random, listIn);
            final int j = weightedrandomchestcontent.minStackSize + random.nextInt(weightedrandomchestcontent.maxStackSize - weightedrandomchestcontent.minStackSize + 1);
            if (weightedrandomchestcontent.theItemId.getMaxStackSize() >= j) {
                final ItemStack itemstack1 = weightedrandomchestcontent.theItemId.copy();
                itemstack1.stackSize = j;
                inv.setInventorySlotContents(random.nextInt(inv.getSizeInventory()), itemstack1);
            }
            else {
                for (int k = 0; k < j; ++k) {
                    final ItemStack itemstack2 = weightedrandomchestcontent.theItemId.copy();
                    itemstack2.stackSize = 1;
                    inv.setInventorySlotContents(random.nextInt(inv.getSizeInventory()), itemstack2);
                }
            }
        }
    }
    
    public static void generateDispenserContents(final Random random, final List<WeightedRandomChestContent> listIn, final TileEntityDispenser dispenser, final int max) {
        for (int i = 0; i < max; ++i) {
            final WeightedRandomChestContent weightedrandomchestcontent = WeightedRandom.getRandomItem(random, listIn);
            final int j = weightedrandomchestcontent.minStackSize + random.nextInt(weightedrandomchestcontent.maxStackSize - weightedrandomchestcontent.minStackSize + 1);
            if (weightedrandomchestcontent.theItemId.getMaxStackSize() >= j) {
                final ItemStack itemstack1 = weightedrandomchestcontent.theItemId.copy();
                itemstack1.stackSize = j;
                dispenser.setInventorySlotContents(random.nextInt(dispenser.getSizeInventory()), itemstack1);
            }
            else {
                for (int k = 0; k < j; ++k) {
                    final ItemStack itemstack2 = weightedrandomchestcontent.theItemId.copy();
                    itemstack2.stackSize = 1;
                    dispenser.setInventorySlotContents(random.nextInt(dispenser.getSizeInventory()), itemstack2);
                }
            }
        }
    }
    
    public static List<WeightedRandomChestContent> func_177629_a(final List<WeightedRandomChestContent> p_177629_0_, final WeightedRandomChestContent... p_177629_1_) {
        final List<WeightedRandomChestContent> list = (List<WeightedRandomChestContent>)Lists.newArrayList((Iterable<?>)p_177629_0_);
        Collections.addAll(list, p_177629_1_);
        return list;
    }
}
