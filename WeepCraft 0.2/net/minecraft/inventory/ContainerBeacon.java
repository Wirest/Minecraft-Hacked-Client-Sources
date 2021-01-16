package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerBeacon extends Container
{
    private final IInventory tileBeacon;

    /**
     * This beacon's slot where you put in Emerald, Diamond, Gold or Iron Ingot.
     */
    private final ContainerBeacon.BeaconSlot beaconSlot;

    public ContainerBeacon(IInventory playerInventory, IInventory tileBeaconIn)
    {
        this.tileBeacon = tileBeaconIn;
        this.beaconSlot = new ContainerBeacon.BeaconSlot(tileBeaconIn, 0, 136, 110);
        this.addSlotToContainer(this.beaconSlot);
        int i = 36;
        int j = 137;

        for (int k = 0; k < 3; ++k)
        {
            for (int l = 0; l < 9; ++l)
            {
                this.addSlotToContainer(new Slot(playerInventory, l + k * 9 + 9, 36 + l * 18, 137 + k * 18));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1)
        {
            this.addSlotToContainer(new Slot(playerInventory, i1, 36 + i1 * 18, 195));
        }
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileBeacon);
    }

    public void updateProgressBar(int id, int data)
    {
        this.tileBeacon.setField(id, data);
    }

    public IInventory getTileEntity()
    {
        return this.tileBeacon;
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);

        if (!playerIn.world.isRemote)
        {
            ItemStack itemstack = this.beaconSlot.decrStackSize(this.beaconSlot.getSlotStackLimit());

            if (!itemstack.func_190926_b())
            {
                playerIn.dropItem(itemstack, false);
            }
        }
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileBeacon.isUsableByPlayer(playerIn);
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

            if (index == 0)
            {
                if (!this.mergeItemStack(itemstack1, 1, 37, true))
                {
                    return ItemStack.field_190927_a;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (!this.beaconSlot.getHasStack() && this.beaconSlot.isItemValid(itemstack1) && itemstack1.func_190916_E() == 1)
            {
                if (!this.mergeItemStack(itemstack1, 0, 1, false))
                {
                    return ItemStack.field_190927_a;
                }
            }
            else if (index >= 1 && index < 28)
            {
                if (!this.mergeItemStack(itemstack1, 28, 37, false))
                {
                    return ItemStack.field_190927_a;
                }
            }
            else if (index >= 28 && index < 37)
            {
                if (!this.mergeItemStack(itemstack1, 1, 28, false))
                {
                    return ItemStack.field_190927_a;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 1, 37, false))
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

            if (itemstack1.func_190916_E() == itemstack.func_190916_E())
            {
                return ItemStack.field_190927_a;
            }

            slot.func_190901_a(playerIn, itemstack1);
        }

        return itemstack;
    }

    class BeaconSlot extends Slot
    {
        public BeaconSlot(IInventory inventoryIn, int index, int xIn, int yIn)
        {
            super(inventoryIn, index, xIn, yIn);
        }

        public boolean isItemValid(ItemStack stack)
        {
            Item item = stack.getItem();
            return item == Items.EMERALD || item == Items.DIAMOND || item == Items.GOLD_INGOT || item == Items.IRON_INGOT;
        }

        public int getSlotStackLimit()
        {
            return 1;
        }
    }
}
