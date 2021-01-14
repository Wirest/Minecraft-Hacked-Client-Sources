package net.minecraft.inventory;

import com.google.common.collect.Lists;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class InventoryBasic implements IInventory {
    private String inventoryTitle;
    private int slotsCount;
    private ItemStack[] inventoryContents;
    private List field_70480_d;
    private boolean hasCustomName;
    private static final String __OBFID = "CL_00001514";

    public InventoryBasic(String p_i1561_1_, boolean p_i1561_2_, int p_i1561_3_) {
        this.inventoryTitle = p_i1561_1_;
        this.hasCustomName = p_i1561_2_;
        this.slotsCount = p_i1561_3_;
        this.inventoryContents = new ItemStack[p_i1561_3_];
    }

    public InventoryBasic(IChatComponent p_i45902_1_, int p_i45902_2_) {
        this(p_i45902_1_.getUnformattedText(), true, p_i45902_2_);
    }

    public void func_110134_a(IInvBasic p_110134_1_) {
        if (this.field_70480_d == null) {
            this.field_70480_d = Lists.newArrayList();
        }

        this.field_70480_d.add(p_110134_1_);
    }

    public void func_110132_b(IInvBasic p_110132_1_) {
        this.field_70480_d.remove(p_110132_1_);
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int slotIn) {
        return slotIn >= 0 && slotIn < this.inventoryContents.length ? this.inventoryContents[slotIn] : null;
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int index, int count) {
        if (this.inventoryContents[index] != null) {
            ItemStack var3;

            if (this.inventoryContents[index].stackSize <= count) {
                var3 = this.inventoryContents[index];
                this.inventoryContents[index] = null;
                this.markDirty();
                return var3;
            } else {
                var3 = this.inventoryContents[index].splitStack(count);

                if (this.inventoryContents[index].stackSize == 0) {
                    this.inventoryContents[index] = null;
                }

                this.markDirty();
                return var3;
            }
        } else {
            return null;
        }
    }

    public ItemStack func_174894_a(ItemStack p_174894_1_) {
        ItemStack var2 = p_174894_1_.copy();

        for (int var3 = 0; var3 < this.slotsCount; ++var3) {
            ItemStack var4 = this.getStackInSlot(var3);

            if (var4 == null) {
                this.setInventorySlotContents(var3, var2);
                this.markDirty();
                return null;
            }

            if (ItemStack.areItemsEqual(var4, var2)) {
                int var5 = Math.min(this.getInventoryStackLimit(), var4.getMaxStackSize());
                int var6 = Math.min(var2.stackSize, var5 - var4.stackSize);

                if (var6 > 0) {
                    var4.stackSize += var6;
                    var2.stackSize -= var6;

                    if (var2.stackSize <= 0) {
                        this.markDirty();
                        return null;
                    }
                }
            }
        }

        if (var2.stackSize != p_174894_1_.stackSize) {
            this.markDirty();
        }

        return var2;
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int index) {
        if (this.inventoryContents[index] != null) {
            ItemStack var2 = this.inventoryContents[index];
            this.inventoryContents[index] = null;
            return var2;
        } else {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.inventoryContents[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory() {
        return this.slotsCount;
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getName() {
        return this.inventoryTitle;
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName() {
        return this.hasCustomName;
    }

    public void func_110133_a(String p_110133_1_) {
        this.hasCustomName = true;
        this.inventoryTitle = p_110133_1_;
    }

    public IChatComponent getDisplayName() {
        return (IChatComponent) (this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]));
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit() {
        return 64;
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty() {
        if (this.field_70480_d != null) {
            for (int var1 = 0; var1 < this.field_70480_d.size(); ++var1) {
                ((IInvBasic) this.field_70480_d.get(var1)).onInventoryChanged(this);
            }
        }
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer playerIn) {
        return true;
    }

    public void openInventory(EntityPlayer playerIn) {
    }

    public void closeInventory(EntityPlayer playerIn) {
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    public int getField(int id) {
        return 0;
    }

    public void setField(int id, int value) {
    }

    public int getFieldCount() {
        return 0;
    }

    public void clearInventory() {
        for (int var1 = 0; var1 < this.inventoryContents.length; ++var1) {
            this.inventoryContents[var1] = null;
        }
    }
}
