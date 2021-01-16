package net.minecraft.inventory;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;

public class ContainerBrewingStand extends Container
{
    private final IInventory tileBrewingStand;

    /** Instance of Slot. */
    private final Slot theSlot;

    /**
     * Used to cache the brewing time to send changes to ICrafting listeners.
     */
    private int prevBrewTime;

    /**
     * Used to cache the fuel remaining in the brewing stand to send changes to ICrafting listeners.
     */
    private int prevFuel;

    public ContainerBrewingStand(InventoryPlayer playerInventory, IInventory tileBrewingStandIn)
    {
        this.tileBrewingStand = tileBrewingStandIn;
        this.addSlotToContainer(new ContainerBrewingStand.Potion(tileBrewingStandIn, 0, 56, 51));
        this.addSlotToContainer(new ContainerBrewingStand.Potion(tileBrewingStandIn, 1, 79, 58));
        this.addSlotToContainer(new ContainerBrewingStand.Potion(tileBrewingStandIn, 2, 102, 51));
        this.theSlot = this.addSlotToContainer(new ContainerBrewingStand.Ingredient(tileBrewingStandIn, 3, 79, 17));
        this.addSlotToContainer(new ContainerBrewingStand.Fuel(tileBrewingStandIn, 4, 17, 17));

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileBrewingStand);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);

            if (this.prevBrewTime != this.tileBrewingStand.getField(0))
            {
                icontainerlistener.sendProgressBarUpdate(this, 0, this.tileBrewingStand.getField(0));
            }

            if (this.prevFuel != this.tileBrewingStand.getField(1))
            {
                icontainerlistener.sendProgressBarUpdate(this, 1, this.tileBrewingStand.getField(1));
            }
        }

        this.prevBrewTime = this.tileBrewingStand.getField(0);
        this.prevFuel = this.tileBrewingStand.getField(1);
    }

    public void updateProgressBar(int id, int data)
    {
        this.tileBrewingStand.setField(id, data);
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileBrewingStand.isUsableByPlayer(playerIn);
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

            if ((index < 0 || index > 2) && index != 3 && index != 4)
            {
                if (this.theSlot.isItemValid(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 3, 4, false))
                    {
                        return ItemStack.field_190927_a;
                    }
                }
                else if (ContainerBrewingStand.Potion.canHoldPotion(itemstack) && itemstack.func_190916_E() == 1)
                {
                    if (!this.mergeItemStack(itemstack1, 0, 3, false))
                    {
                        return ItemStack.field_190927_a;
                    }
                }
                else if (ContainerBrewingStand.Fuel.isValidBrewingFuel(itemstack))
                {
                    if (!this.mergeItemStack(itemstack1, 4, 5, false))
                    {
                        return ItemStack.field_190927_a;
                    }
                }
                else if (index >= 5 && index < 32)
                {
                    if (!this.mergeItemStack(itemstack1, 32, 41, false))
                    {
                        return ItemStack.field_190927_a;
                    }
                }
                else if (index >= 32 && index < 41)
                {
                    if (!this.mergeItemStack(itemstack1, 5, 32, false))
                    {
                        return ItemStack.field_190927_a;
                    }
                }
                else if (!this.mergeItemStack(itemstack1, 5, 41, false))
                {
                    return ItemStack.field_190927_a;
                }
            }
            else
            {
                if (!this.mergeItemStack(itemstack1, 5, 41, true))
                {
                    return ItemStack.field_190927_a;
                }

                slot.onSlotChange(itemstack1, itemstack);
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

    static class Fuel extends Slot
    {
        public Fuel(IInventory iInventoryIn, int index, int xPosition, int yPosition)
        {
            super(iInventoryIn, index, xPosition, yPosition);
        }

        public boolean isItemValid(ItemStack stack)
        {
            return isValidBrewingFuel(stack);
        }

        public static boolean isValidBrewingFuel(ItemStack itemStackIn)
        {
            return itemStackIn.getItem() == Items.BLAZE_POWDER;
        }

        public int getSlotStackLimit()
        {
            return 64;
        }
    }

    static class Ingredient extends Slot
    {
        public Ingredient(IInventory iInventoryIn, int index, int xPosition, int yPosition)
        {
            super(iInventoryIn, index, xPosition, yPosition);
        }

        public boolean isItemValid(ItemStack stack)
        {
            return PotionHelper.isReagent(stack);
        }

        public int getSlotStackLimit()
        {
            return 64;
        }
    }

    static class Potion extends Slot
    {
        public Potion(IInventory p_i47598_1_, int p_i47598_2_, int p_i47598_3_, int p_i47598_4_)
        {
            super(p_i47598_1_, p_i47598_2_, p_i47598_3_, p_i47598_4_);
        }

        public boolean isItemValid(ItemStack stack)
        {
            return canHoldPotion(stack);
        }

        public int getSlotStackLimit()
        {
            return 1;
        }

        public ItemStack func_190901_a(EntityPlayer p_190901_1_, ItemStack p_190901_2_)
        {
            PotionType potiontype = PotionUtils.getPotionFromItem(p_190901_2_);

            if (p_190901_1_ instanceof EntityPlayerMP)
            {
                CriteriaTriggers.field_192130_j.func_192173_a((EntityPlayerMP)p_190901_1_, potiontype);
            }

            super.func_190901_a(p_190901_1_, p_190901_2_);
            return p_190901_2_;
        }

        public static boolean canHoldPotion(ItemStack stack)
        {
            Item item = stack.getItem();
            return item == Items.POTIONITEM || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION || item == Items.GLASS_BOTTLE;
        }
    }
}
