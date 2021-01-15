package net.minecraft.inventory;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ContainerHorseInventory extends Container
{
    private IInventory horseInventory;
    private EntityHorse theHorse;

    public ContainerHorseInventory(IInventory playerInventory, final IInventory horseInventoryIn, final EntityHorse horse, EntityPlayer player)
    {
        this.horseInventory = horseInventoryIn;
        this.theHorse = horse;
        byte var5 = 3;
        horseInventoryIn.openInventory(player);
        int var6 = (var5 - 4) * 18;
        this.addSlotToContainer(new Slot(horseInventoryIn, 0, 8, 18)
        {
            @Override
			public boolean isItemValid(ItemStack stack)
            {
                return super.isItemValid(stack) && stack.getItem() == Items.saddle && !this.getHasStack();
            }
        });
        this.addSlotToContainer(new Slot(horseInventoryIn, 1, 8, 36)
        {
            @Override
			public boolean isItemValid(ItemStack stack)
            {
                return super.isItemValid(stack) && horse.canWearArmor() && EntityHorse.isArmorItem(stack.getItem());
            }
            @Override
			public boolean canBeHovered()
            {
                return horse.canWearArmor();
            }
        });
        int var7;
        int var8;

        if (horse.isChested())
        {
            for (var7 = 0; var7 < var5; ++var7)
            {
                for (var8 = 0; var8 < 5; ++var8)
                {
                    this.addSlotToContainer(new Slot(horseInventoryIn, 2 + var8 + var7 * 5, 80 + var8 * 18, 18 + var7 * 18));
                }
            }
        }

        for (var7 = 0; var7 < 3; ++var7)
        {
            for (var8 = 0; var8 < 9; ++var8)
            {
                this.addSlotToContainer(new Slot(playerInventory, var8 + var7 * 9 + 9, 8 + var8 * 18, 102 + var7 * 18 + var6));
            }
        }

        for (var7 = 0; var7 < 9; ++var7)
        {
            this.addSlotToContainer(new Slot(playerInventory, var7, 8 + var7 * 18, 160 + var6));
        }
    }

    @Override
	public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.horseInventory.isUseableByPlayer(playerIn) && this.theHorse.isEntityAlive() && this.theHorse.getDistanceToEntity(playerIn) < 8.0F;
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

            if (index < this.horseInventory.getSizeInventory())
            {
                if (!this.mergeItemStack(var5, this.horseInventory.getSizeInventory(), this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (this.getSlot(1).isItemValid(var5) && !this.getSlot(1).getHasStack())
            {
                if (!this.mergeItemStack(var5, 1, 2, false))
                {
                    return null;
                }
            }
            else if (this.getSlot(0).isItemValid(var5))
            {
                if (!this.mergeItemStack(var5, 0, 1, false))
                {
                    return null;
                }
            }
            else if (this.horseInventory.getSizeInventory() <= 2 || !this.mergeItemStack(var5, 2, this.horseInventory.getSizeInventory(), false))
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
        this.horseInventory.closeInventory(playerIn);
    }
}
