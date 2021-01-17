// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.ItemSword;
import net.minecraft.init.Items;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.AchievementList;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;

public class SlotCrafting extends Slot
{
    private final InventoryCrafting craftMatrix;
    private final EntityPlayer thePlayer;
    private int amountCrafted;
    
    public SlotCrafting(final EntityPlayer player, final InventoryCrafting craftingInventory, final IInventory p_i45790_3_, final int slotIndex, final int xPosition, final int yPosition) {
        super(p_i45790_3_, slotIndex, xPosition, yPosition);
        this.thePlayer = player;
        this.craftMatrix = craftingInventory;
    }
    
    @Override
    public boolean isItemValid(final ItemStack stack) {
        return false;
    }
    
    @Override
    public ItemStack decrStackSize(final int amount) {
        if (this.getHasStack()) {
            this.amountCrafted += Math.min(amount, this.getStack().stackSize);
        }
        return super.decrStackSize(amount);
    }
    
    @Override
    protected void onCrafting(final ItemStack stack, final int amount) {
        this.amountCrafted += amount;
        this.onCrafting(stack);
    }
    
    @Override
    protected void onCrafting(final ItemStack stack) {
        if (this.amountCrafted > 0) {
            stack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.amountCrafted);
        }
        this.amountCrafted = 0;
        if (stack.getItem() == Item.getItemFromBlock(Blocks.crafting_table)) {
            this.thePlayer.triggerAchievement(AchievementList.buildWorkBench);
        }
        if (stack.getItem() instanceof ItemPickaxe) {
            this.thePlayer.triggerAchievement(AchievementList.buildPickaxe);
        }
        if (stack.getItem() == Item.getItemFromBlock(Blocks.furnace)) {
            this.thePlayer.triggerAchievement(AchievementList.buildFurnace);
        }
        if (stack.getItem() instanceof ItemHoe) {
            this.thePlayer.triggerAchievement(AchievementList.buildHoe);
        }
        if (stack.getItem() == Items.bread) {
            this.thePlayer.triggerAchievement(AchievementList.makeBread);
        }
        if (stack.getItem() == Items.cake) {
            this.thePlayer.triggerAchievement(AchievementList.bakeCake);
        }
        if (stack.getItem() instanceof ItemPickaxe && ((ItemPickaxe)stack.getItem()).getToolMaterial() != Item.ToolMaterial.WOOD) {
            this.thePlayer.triggerAchievement(AchievementList.buildBetterPickaxe);
        }
        if (stack.getItem() instanceof ItemSword) {
            this.thePlayer.triggerAchievement(AchievementList.buildSword);
        }
        if (stack.getItem() == Item.getItemFromBlock(Blocks.enchanting_table)) {
            this.thePlayer.triggerAchievement(AchievementList.enchantments);
        }
        if (stack.getItem() == Item.getItemFromBlock(Blocks.bookshelf)) {
            this.thePlayer.triggerAchievement(AchievementList.bookcase);
        }
        if (stack.getItem() == Items.golden_apple && stack.getMetadata() == 1) {
            this.thePlayer.triggerAchievement(AchievementList.overpowered);
        }
    }
    
    @Override
    public void onPickupFromSlot(final EntityPlayer playerIn, final ItemStack stack) {
        this.onCrafting(stack);
        final ItemStack[] aitemstack = CraftingManager.getInstance().func_180303_b(this.craftMatrix, playerIn.worldObj);
        for (int i = 0; i < aitemstack.length; ++i) {
            final ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
            final ItemStack itemstack2 = aitemstack[i];
            if (itemstack != null) {
                this.craftMatrix.decrStackSize(i, 1);
            }
            if (itemstack2 != null) {
                if (this.craftMatrix.getStackInSlot(i) == null) {
                    this.craftMatrix.setInventorySlotContents(i, itemstack2);
                }
                else if (!this.thePlayer.inventory.addItemStackToInventory(itemstack2)) {
                    this.thePlayer.dropPlayerItemWithRandomChoice(itemstack2, false);
                }
            }
        }
    }
}
