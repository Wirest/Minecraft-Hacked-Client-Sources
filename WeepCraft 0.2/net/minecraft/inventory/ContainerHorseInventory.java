package net.minecraft.inventory;

import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ContainerHorseInventory extends Container
{
    private final IInventory horseInventory;
    private final AbstractHorse theHorse;

    public ContainerHorseInventory(IInventory playerInventory, IInventory horseInventoryIn, final AbstractHorse horse, EntityPlayer player)
    {
        this.horseInventory = horseInventoryIn;
        this.theHorse = horse;
        int i = 3;
        horseInventoryIn.openInventory(player);
        int j = -18;
        this.addSlotToContainer(new Slot(horseInventoryIn, 0, 8, 18)
        {
            public boolean isItemValid(ItemStack stack)
            {
                return stack.getItem() == Items.SADDLE && !this.getHasStack() && horse.func_190685_dA();
            }
            public boolean canBeHovered()
            {
                return horse.func_190685_dA();
            }
        });
        this.addSlotToContainer(new Slot(horseInventoryIn, 1, 8, 36)
        {
            public boolean isItemValid(ItemStack stack)
            {
                return horse.func_190682_f(stack);
            }
            public boolean canBeHovered()
            {
                return horse.func_190677_dK();
            }
            public int getSlotStackLimit()
            {
                return 1;
            }
        });

        if (horse instanceof AbstractChestHorse && ((AbstractChestHorse)horse).func_190695_dh())
        {
            for (int k = 0; k < 3; ++k)
            {
                for (int l = 0; l < ((AbstractChestHorse)horse).func_190696_dl(); ++l)
                {
                    this.addSlotToContainer(new Slot(horseInventoryIn, 2 + l + k * ((AbstractChestHorse)horse).func_190696_dl(), 80 + l * 18, 18 + k * 18));
                }
            }
        }

        for (int i1 = 0; i1 < 3; ++i1)
        {
            for (int k1 = 0; k1 < 9; ++k1)
            {
                this.addSlotToContainer(new Slot(playerInventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 102 + i1 * 18 + -18));
            }
        }

        for (int j1 = 0; j1 < 9; ++j1)
        {
            this.addSlotToContainer(new Slot(playerInventory, j1, 8 + j1 * 18, 142));
        }
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.horseInventory.isUsableByPlayer(playerIn) && this.theHorse.isEntityAlive() && this.theHorse.getDistanceToEntity(playerIn) < 8.0F;
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.field_190927_a;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < this.horseInventory.getSizeInventory())
            {
                if (!this.mergeItemStack(itemstack1, this.horseInventory.getSizeInventory(), this.inventorySlots.size(), true))
                {
                    return ItemStack.field_190927_a;
                }
            }
            else if (this.getSlot(1).isItemValid(itemstack1) && !this.getSlot(1).getHasStack())
            {
                if (!this.mergeItemStack(itemstack1, 1, 2, false))
                {
                    return ItemStack.field_190927_a;
                }
            }
            else if (this.getSlot(0).isItemValid(itemstack1))
            {
                if (!this.mergeItemStack(itemstack1, 0, 1, false))
                {
                    return ItemStack.field_190927_a;
                }
            }
            else if (this.horseInventory.getSizeInventory() <= 2 || !this.mergeItemStack(itemstack1, 2, this.horseInventory.getSizeInventory(), false))
            {
                return ItemStack.field_190927_a;
            }

            if (itemstack1.func_190926_b())
            {
                slot.putStack(ItemStack.field_190927_a);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        this.horseInventory.closeInventory(playerIn);
    }
}
