package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ContainerChest extends Container
{
    private IInventory lowerChestInventory;
    private int numRows;

    public ContainerChest(IInventory playerInventory, IInventory chestInventory, EntityPlayer player)
    {
        this.lowerChestInventory = chestInventory;
        this.numRows = chestInventory.getSizeInventory() / 9;
        chestInventory.openInventory(player);
        int var4 = (this.numRows - 4) * 18;
        int var5;
        int var6;

        for (var5 = 0; var5 < this.numRows; ++var5)
        {
            for (var6 = 0; var6 < 9; ++var6)
            {
                this.addSlotToContainer(new Slot(chestInventory, var6 + var5 * 9, 8 + var6 * 18, 18 + var5 * 18));
            }
        }

        for (var5 = 0; var5 < 3; ++var5)
        {
            for (var6 = 0; var6 < 9; ++var6)
            {
                this.addSlotToContainer(new Slot(playerInventory, var6 + var5 * 9 + 9, 8 + var6 * 18, 103 + var5 * 18 + var4));
            }
        }

        for (var5 = 0; var5 < 9; ++var5)
        {
            this.addSlotToContainer(new Slot(playerInventory, var5, 8 + var5 * 18, 161 + var4));
        }
    }

    @Override
	public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.lowerChestInventory.isUseableByPlayer(playerIn);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    @Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(index);

        if (var4 != null && var4.getHasStack())
        {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (index < this.numRows * 9)
            {
                if (!this.mergeItemStack(var5, this.numRows * 9, this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 0, this.numRows * 9, false))
            {
                return null;
            }

            if (var5.stackSize == 0)
            {
                var4.putStack((ItemStack)null);
            }
            else
            {
                var4.onSlotChanged();
            }
        }

        return var3;
    }

    /**
     * Called when the container is closed.
     */
    @Override
	public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        this.lowerChestInventory.closeInventory(playerIn);
    }

    /**
     * Return this chest container's lower chest inventory.
     */
    public IInventory getLowerChestInventory()
    {
        return this.lowerChestInventory;
    }
}
