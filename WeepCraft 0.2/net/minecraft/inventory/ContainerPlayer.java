package net.minecraft.inventory;

import javax.annotation.Nullable;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ContainerPlayer extends Container
{
    private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};

    /** The crafting matrix inventory. */
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
    public InventoryCraftResult craftResult = new InventoryCraftResult();

    /** Determines if inventory manipulation should be handled. */
    public boolean isLocalWorld;
    private final EntityPlayer thePlayer;

    public ContainerPlayer(InventoryPlayer playerInventory, boolean localWorld, EntityPlayer player)
    {
        this.isLocalWorld = localWorld;
        this.thePlayer = player;
        this.addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 154, 28));

        for (int i = 0; i < 2; ++i)
        {
            for (int j = 0; j < 2; ++j)
            {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 2, 98 + j * 18, 18 + i * 18));
            }
        }

        for (int k = 0; k < 4; ++k)
        {
            final EntityEquipmentSlot entityequipmentslot = VALID_EQUIPMENT_SLOTS[k];
            this.addSlotToContainer(new Slot(playerInventory, 36 + (3 - k), 8, 8 + k * 18)
            {
                public int getSlotStackLimit()
                {
                    return 1;
                }
                public boolean isItemValid(ItemStack stack)
                {
                    return entityequipmentslot == EntityLiving.getSlotForItemStack(stack);
                }
                public boolean canTakeStack(EntityPlayer playerIn)
                {
                    ItemStack itemstack = this.getStack();
                    return !itemstack.func_190926_b() && !playerIn.isCreative() && EnchantmentHelper.func_190938_b(itemstack) ? false : super.canTakeStack(playerIn);
                }
                @Nullable
                public String getSlotTexture()
                {
                    return ItemArmor.EMPTY_SLOT_NAMES[entityequipmentslot.getIndex()];
                }
            });
        }

        for (int l = 0; l < 3; ++l)
        {
            for (int j1 = 0; j1 < 9; ++j1)
            {
                this.addSlotToContainer(new Slot(playerInventory, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1)
        {
            this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 142));
        }

        this.addSlotToContainer(new Slot(playerInventory, 40, 77, 62)
        {
            @Nullable
            public String getSlotTexture()
            {
                return "minecraft:items/empty_armor_slot_shield";
            }
        });
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        this.func_192389_a(this.thePlayer.world, this.thePlayer, this.craftMatrix, this.craftResult);
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        this.craftResult.clear();

        if (!playerIn.world.isRemote)
        {
            this.func_193327_a(playerIn, playerIn.world, this.craftMatrix);
        }
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
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
            EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemstack);

            if (index == 0)
            {
                if (!this.mergeItemStack(itemstack1, 9, 45, true))
                {
                    return ItemStack.field_190927_a;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index >= 1 && index < 5)
            {
                if (!this.mergeItemStack(itemstack1, 9, 45, false))
                {
                    return ItemStack.field_190927_a;
                }
            }
            else if (index >= 5 && index < 9)
            {
                if (!this.mergeItemStack(itemstack1, 9, 45, false))
                {
                    return ItemStack.field_190927_a;
                }
            }
            else if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR && !((Slot)this.inventorySlots.get(8 - entityequipmentslot.getIndex())).getHasStack())
            {
                int i = 8 - entityequipmentslot.getIndex();

                if (!this.mergeItemStack(itemstack1, i, i + 1, false))
                {
                    return ItemStack.field_190927_a;
                }
            }
            else if (entityequipmentslot == EntityEquipmentSlot.OFFHAND && !((Slot)this.inventorySlots.get(45)).getHasStack())
            {
                if (!this.mergeItemStack(itemstack1, 45, 46, false))
                {
                    return ItemStack.field_190927_a;
                }
            }
            else if (index >= 9 && index < 36)
            {
                if (!this.mergeItemStack(itemstack1, 36, 45, false))
                {
                    return ItemStack.field_190927_a;
                }
            }
            else if (index >= 36 && index < 45)
            {
                if (!this.mergeItemStack(itemstack1, 9, 36, false))
                {
                    return ItemStack.field_190927_a;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 9, 45, false))
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

            ItemStack itemstack2 = slot.func_190901_a(playerIn, itemstack1);

            if (index == 0)
            {
                playerIn.dropItem(itemstack2, false);
            }
        }

        return itemstack;
    }

    /**
     * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in
     * is null for the initial slot that was double-clicked.
     */
    public boolean canMergeSlot(ItemStack stack, Slot slotIn)
    {
        return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
    }
}
