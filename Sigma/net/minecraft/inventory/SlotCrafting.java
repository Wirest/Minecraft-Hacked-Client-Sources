package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.stats.AchievementList;

public class SlotCrafting extends Slot {
    /**
     * The craft matrix inventory linked to this result slot.
     */
    private final InventoryCrafting craftMatrix;

    /**
     * The player that is using the GUI where this slot resides.
     */
    private final EntityPlayer thePlayer;

    /**
     * The number of items that have been crafted so far. Gets passed to
     * ItemStack.onCrafting before being reset.
     */
    private int amountCrafted;
    private static final String __OBFID = "CL_00001761";

    public SlotCrafting(EntityPlayer p_i45790_1_, InventoryCrafting p_i45790_2_, IInventory p_i45790_3_, int p_i45790_4_, int p_i45790_5_, int p_i45790_6_) {
        super(p_i45790_3_, p_i45790_4_, p_i45790_5_, p_i45790_6_);
        thePlayer = p_i45790_1_;
        craftMatrix = p_i45790_2_;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for
     * the armor slots.
     */
    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of
     * the second int arg. Returns the new stack.
     */
    @Override
    public ItemStack decrStackSize(int p_75209_1_) {
        if (getHasStack()) {
            amountCrafted += Math.min(p_75209_1_, getStack().stackSize);
        }

        return super.decrStackSize(p_75209_1_);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes,
     * not ore and wood. Typically increases an internal count then calls
     * onCrafting(item).
     */
    @Override
    protected void onCrafting(ItemStack p_75210_1_, int p_75210_2_) {
        amountCrafted += p_75210_2_;
        this.onCrafting(p_75210_1_);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes,
     * not ore and wood.
     */
    @Override
    protected void onCrafting(ItemStack p_75208_1_) {
        if (amountCrafted > 0) {
            p_75208_1_.onCrafting(thePlayer.worldObj, thePlayer, amountCrafted);
        }

        amountCrafted = 0;

        if (p_75208_1_.getItem() == Item.getItemFromBlock(Blocks.crafting_table)) {
            thePlayer.triggerAchievement(AchievementList.buildWorkBench);
        }

        if (p_75208_1_.getItem() instanceof ItemPickaxe) {
            thePlayer.triggerAchievement(AchievementList.buildPickaxe);
        }

        if (p_75208_1_.getItem() == Item.getItemFromBlock(Blocks.furnace)) {
            thePlayer.triggerAchievement(AchievementList.buildFurnace);
        }

        if (p_75208_1_.getItem() instanceof ItemHoe) {
            thePlayer.triggerAchievement(AchievementList.buildHoe);
        }

        if (p_75208_1_.getItem() == Items.bread) {
            thePlayer.triggerAchievement(AchievementList.makeBread);
        }

        if (p_75208_1_.getItem() == Items.cake) {
            thePlayer.triggerAchievement(AchievementList.bakeCake);
        }

        if (p_75208_1_.getItem() instanceof ItemPickaxe && ((ItemPickaxe) p_75208_1_.getItem()).getToolMaterial() != Item.ToolMaterial.WOOD) {
            thePlayer.triggerAchievement(AchievementList.buildBetterPickaxe);
        }

        if (p_75208_1_.getItem() instanceof ItemSword) {
            thePlayer.triggerAchievement(AchievementList.buildSword);
        }

        if (p_75208_1_.getItem() == Item.getItemFromBlock(Blocks.enchanting_table)) {
            thePlayer.triggerAchievement(AchievementList.enchantments);
        }

        if (p_75208_1_.getItem() == Item.getItemFromBlock(Blocks.bookshelf)) {
            thePlayer.triggerAchievement(AchievementList.bookcase);
        }

        if (p_75208_1_.getItem() == Items.golden_apple && p_75208_1_.getMetadata() == 1) {
            thePlayer.triggerAchievement(AchievementList.overpowered);
        }
    }

    @Override
    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
        this.onCrafting(stack);
        ItemStack[] var3 = CraftingManager.getInstance().func_180303_b(craftMatrix, playerIn.worldObj);

        for (int var4 = 0; var4 < var3.length; ++var4) {
            ItemStack var5 = craftMatrix.getStackInSlot(var4);
            ItemStack var6 = var3[var4];

            if (var5 != null) {
                craftMatrix.decrStackSize(var4, 1);
            }

            if (var6 != null) {
                if (craftMatrix.getStackInSlot(var4) == null) {
                    craftMatrix.setInventorySlotContents(var4, var6);
                } else if (!thePlayer.inventory.addItemStackToInventory(var6)) {
                    thePlayer.dropPlayerItemWithRandomChoice(var6, false);
                }
            }
        }
    }
}
