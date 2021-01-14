package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerFurnace extends Container {
    private final IInventory tileFurnace;
    private int field_178152_f;
    private int field_178153_g;
    private int field_178154_h;
    private int field_178155_i;
    private static final String __OBFID = "CL_00001748";

    public ContainerFurnace(InventoryPlayer p_i45794_1_, IInventory p_i45794_2_) {
        this.tileFurnace = p_i45794_2_;
        this.addSlotToContainer(new Slot(p_i45794_2_, 0, 56, 17));
        this.addSlotToContainer(new SlotFurnaceFuel(p_i45794_2_, 1, 56, 53));
        this.addSlotToContainer(new SlotFurnaceOutput(p_i45794_1_.player, p_i45794_2_, 2, 116, 35));
        int var3;

        for (var3 = 0; var3 < 3; ++var3) {
            for (int var4 = 0; var4 < 9; ++var4) {
                this.addSlotToContainer(new Slot(p_i45794_1_, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3) {
            this.addSlotToContainer(new Slot(p_i45794_1_, var3, 8 + var3 * 18, 142));
        }
    }

    public void onCraftGuiOpened(ICrafting p_75132_1_) {
        super.onCraftGuiOpened(p_75132_1_);
        p_75132_1_.func_175173_a(this, this.tileFurnace);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int var1 = 0; var1 < this.crafters.size(); ++var1) {
            ICrafting var2 = (ICrafting) this.crafters.get(var1);

            if (this.field_178152_f != this.tileFurnace.getField(2)) {
                var2.sendProgressBarUpdate(this, 2, this.tileFurnace.getField(2));
            }

            if (this.field_178154_h != this.tileFurnace.getField(0)) {
                var2.sendProgressBarUpdate(this, 0, this.tileFurnace.getField(0));
            }

            if (this.field_178155_i != this.tileFurnace.getField(1)) {
                var2.sendProgressBarUpdate(this, 1, this.tileFurnace.getField(1));
            }

            if (this.field_178153_g != this.tileFurnace.getField(3)) {
                var2.sendProgressBarUpdate(this, 3, this.tileFurnace.getField(3));
            }
        }

        this.field_178152_f = this.tileFurnace.getField(2);
        this.field_178154_h = this.tileFurnace.getField(0);
        this.field_178155_i = this.tileFurnace.getField(1);
        this.field_178153_g = this.tileFurnace.getField(3);
    }

    public void updateProgressBar(int p_75137_1_, int p_75137_2_) {
        this.tileFurnace.setField(p_75137_1_, p_75137_2_);
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tileFurnace.isUseableByPlayer(playerIn);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack var3 = null;
        Slot var4 = (Slot) this.inventorySlots.get(index);

        if (var4 != null && var4.getHasStack()) {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (index == 2) {
                if (!this.mergeItemStack(var5, 3, 39, true)) {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            } else if (index != 1 && index != 0) {
                if (FurnaceRecipes.instance().getSmeltingResult(var5) != null) {
                    if (!this.mergeItemStack(var5, 0, 1, false)) {
                        return null;
                    }
                } else if (TileEntityFurnace.isItemFuel(var5)) {
                    if (!this.mergeItemStack(var5, 1, 2, false)) {
                        return null;
                    }
                } else if (index >= 3 && index < 30) {
                    if (!this.mergeItemStack(var5, 30, 39, false)) {
                        return null;
                    }
                } else if (index >= 30 && index < 39 && !this.mergeItemStack(var5, 3, 30, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(var5, 3, 39, false)) {
                return null;
            }

            if (var5.stackSize == 0) {
                var4.putStack((ItemStack) null);
            } else {
                var4.onSlotChanged();
            }

            if (var5.stackSize == var3.stackSize) {
                return null;
            }

            var4.onPickupFromSlot(playerIn, var5);
        }

        return var3;
    }
}
