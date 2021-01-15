package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.village.MerchantRecipe;

public class SlotMerchantResult extends Slot
{
    /** Merchant's inventory. */
    private final InventoryMerchant theMerchantInventory;

    /** The Player whos trying to buy/sell stuff. */
    private EntityPlayer thePlayer;
    private int field_75231_g;

    /** "Instance" of the Merchant. */
    private final IMerchant theMerchant;

    public SlotMerchantResult(EntityPlayer player, IMerchant merchant, InventoryMerchant merchantInventory, int slotIndex, int xPosition, int yPosition)
    {
        super(merchantInventory, slotIndex, xPosition, yPosition);
        this.thePlayer = player;
        this.theMerchant = merchant;
        this.theMerchantInventory = merchantInventory;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    @Override
	public boolean isItemValid(ItemStack stack)
    {
        return false;
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    @Override
	public ItemStack decrStackSize(int amount)
    {
        if (this.getHasStack())
        {
            this.field_75231_g += Math.min(amount, this.getStack().stackSize);
        }

        return super.decrStackSize(amount);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
     * internal count then calls onCrafting(item).
     */
    @Override
	protected void onCrafting(ItemStack stack, int amount)
    {
        this.field_75231_g += amount;
        this.onCrafting(stack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    @Override
	protected void onCrafting(ItemStack stack)
    {
        stack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75231_g);
        this.field_75231_g = 0;
    }

    @Override
	public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
    {
        this.onCrafting(stack);
        MerchantRecipe var3 = this.theMerchantInventory.getCurrentRecipe();

        if (var3 != null)
        {
            ItemStack var4 = this.theMerchantInventory.getStackInSlot(0);
            ItemStack var5 = this.theMerchantInventory.getStackInSlot(1);

            if (this.doTrade(var3, var4, var5) || this.doTrade(var3, var5, var4))
            {
                this.theMerchant.useRecipe(var3);
                playerIn.triggerAchievement(StatList.timesTradedWithVillagerStat);

                if (var4 != null && var4.stackSize <= 0)
                {
                    var4 = null;
                }

                if (var5 != null && var5.stackSize <= 0)
                {
                    var5 = null;
                }

                this.theMerchantInventory.setInventorySlotContents(0, var4);
                this.theMerchantInventory.setInventorySlotContents(1, var5);
            }
        }
    }

    private boolean doTrade(MerchantRecipe trade, ItemStack firstItem, ItemStack secondItem)
    {
        ItemStack var4 = trade.getItemToBuy();
        ItemStack var5 = trade.getSecondItemToBuy();

        if (firstItem != null && firstItem.getItem() == var4.getItem())
        {
            if (var5 != null && secondItem != null && var5.getItem() == secondItem.getItem())
            {
                firstItem.stackSize -= var4.stackSize;
                secondItem.stackSize -= var5.stackSize;
                return true;
            }

            if (var5 == null && secondItem == null)
            {
                firstItem.stackSize -= var4.stackSize;
                return true;
            }
        }

        return false;
    }
}
