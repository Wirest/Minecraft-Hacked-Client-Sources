package net.minecraft.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;

public class WeightedRandomChestContent extends WeightedRandom.Item
{
    /** The Item/Block ID to generate in the Chest. */
    private ItemStack theItemId;

    /** The minimum chance of item generating. */
    private int theMinimumChanceToGenerateItem;

    /** The maximum chance of item generating. */
    private int theMaximumChanceToGenerateItem;

    public WeightedRandomChestContent(Item p_i45311_1_, int p_i45311_2_, int minimumChance, int maximumChance, int itemWeightIn)
    {
        super(itemWeightIn);
        this.theItemId = new ItemStack(p_i45311_1_, 1, p_i45311_2_);
        this.theMinimumChanceToGenerateItem = minimumChance;
        this.theMaximumChanceToGenerateItem = maximumChance;
    }

    public WeightedRandomChestContent(ItemStack stack, int minimumChance, int maximumChance, int itemWeightIn)
    {
        super(itemWeightIn);
        this.theItemId = stack;
        this.theMinimumChanceToGenerateItem = minimumChance;
        this.theMaximumChanceToGenerateItem = maximumChance;
    }

    public static void generateChestContents(Random random, List listIn, IInventory inv, int max)
    {
        for (int var4 = 0; var4 < max; ++var4)
        {
            WeightedRandomChestContent var5 = (WeightedRandomChestContent)WeightedRandom.getRandomItem(random, listIn);
            int var6 = var5.theMinimumChanceToGenerateItem + random.nextInt(var5.theMaximumChanceToGenerateItem - var5.theMinimumChanceToGenerateItem + 1);

            if (var5.theItemId.getMaxStackSize() >= var6)
            {
                ItemStack var7 = var5.theItemId.copy();
                var7.stackSize = var6;
                inv.setInventorySlotContents(random.nextInt(inv.getSizeInventory()), var7);
            }
            else
            {
                for (int var9 = 0; var9 < var6; ++var9)
                {
                    ItemStack var8 = var5.theItemId.copy();
                    var8.stackSize = 1;
                    inv.setInventorySlotContents(random.nextInt(inv.getSizeInventory()), var8);
                }
            }
        }
    }

    public static void generateDispenserContents(Random random, List listIn, TileEntityDispenser dispenser, int max)
    {
        for (int var4 = 0; var4 < max; ++var4)
        {
            WeightedRandomChestContent var5 = (WeightedRandomChestContent)WeightedRandom.getRandomItem(random, listIn);
            int var6 = var5.theMinimumChanceToGenerateItem + random.nextInt(var5.theMaximumChanceToGenerateItem - var5.theMinimumChanceToGenerateItem + 1);

            if (var5.theItemId.getMaxStackSize() >= var6)
            {
                ItemStack var7 = var5.theItemId.copy();
                var7.stackSize = var6;
                dispenser.setInventorySlotContents(random.nextInt(dispenser.getSizeInventory()), var7);
            }
            else
            {
                for (int var9 = 0; var9 < var6; ++var9)
                {
                    ItemStack var8 = var5.theItemId.copy();
                    var8.stackSize = 1;
                    dispenser.setInventorySlotContents(random.nextInt(dispenser.getSizeInventory()), var8);
                }
            }
        }
    }

    public static List func_177629_a(List p_177629_0_, WeightedRandomChestContent ... p_177629_1_)
    {
        ArrayList var2 = Lists.newArrayList(p_177629_0_);
        Collections.addAll(var2, p_177629_1_);
        return var2;
    }
}
