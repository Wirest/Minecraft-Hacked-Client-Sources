package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

public class InventoryLargeChest implements ILockableContainer {
    /**
     * Name of the chest.
     */
    private String name;

    /**
     * Inventory object corresponding to double chest upper part
     */
    private ILockableContainer upperChest;

    /**
     * Inventory object corresponding to double chest lower part
     */
    private ILockableContainer lowerChest;
    private static final String __OBFID = "CL_00001507";

    public InventoryLargeChest(String p_i45905_1_, ILockableContainer p_i45905_2_, ILockableContainer p_i45905_3_) {
        this.name = p_i45905_1_;

        if (p_i45905_2_ == null) {
            p_i45905_2_ = p_i45905_3_;
        }

        if (p_i45905_3_ == null) {
            p_i45905_3_ = p_i45905_2_;
        }

        this.upperChest = p_i45905_2_;
        this.lowerChest = p_i45905_3_;

        if (p_i45905_2_.isLocked()) {
            p_i45905_3_.setLockCode(p_i45905_2_.getLockCode());
        } else if (p_i45905_3_.isLocked()) {
            p_i45905_2_.setLockCode(p_i45905_3_.getLockCode());
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory() {
        return this.upperChest.getSizeInventory() + this.lowerChest.getSizeInventory();
    }

    /**
     * Return whether the given inventory is part of this large chest.
     */
    public boolean isPartOfLargeChest(IInventory p_90010_1_) {
        return this.upperChest == p_90010_1_ || this.lowerChest == p_90010_1_;
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getName() {
        return this.upperChest.hasCustomName() ? this.upperChest.getName() : (this.lowerChest.hasCustomName() ? this.lowerChest.getName() : this.name);
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName() {
        return this.upperChest.hasCustomName() || this.lowerChest.hasCustomName();
    }

    public IChatComponent getDisplayName() {
        return (IChatComponent) (this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]));
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int slotIn) {
        return slotIn >= this.upperChest.getSizeInventory() ? this.lowerChest.getStackInSlot(slotIn - this.upperChest.getSizeInventory()) : this.upperChest.getStackInSlot(slotIn);
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int index, int count) {
        return index >= this.upperChest.getSizeInventory() ? this.lowerChest.decrStackSize(index - this.upperChest.getSizeInventory(), count) : this.upperChest.decrStackSize(index, count);
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int index) {
        return index >= this.upperChest.getSizeInventory() ? this.lowerChest.getStackInSlotOnClosing(index - this.upperChest.getSizeInventory()) : this.upperChest.getStackInSlotOnClosing(index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index >= this.upperChest.getSizeInventory()) {
            this.lowerChest.setInventorySlotContents(index - this.upperChest.getSizeInventory(), stack);
        } else {
            this.upperChest.setInventorySlotContents(index, stack);
        }
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit() {
        return this.upperChest.getInventoryStackLimit();
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty() {
        this.upperChest.markDirty();
        this.lowerChest.markDirty();
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer playerIn) {
        return this.upperChest.isUseableByPlayer(playerIn) && this.lowerChest.isUseableByPlayer(playerIn);
    }

    public void openInventory(EntityPlayer playerIn) {
        this.upperChest.openInventory(playerIn);
        this.lowerChest.openInventory(playerIn);
    }

    public void closeInventory(EntityPlayer playerIn) {
        this.upperChest.closeInventory(playerIn);
        this.lowerChest.closeInventory(playerIn);
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

    public boolean isLocked() {
        return this.upperChest.isLocked() || this.lowerChest.isLocked();
    }

    public void setLockCode(LockCode code) {
        this.upperChest.setLockCode(code);
        this.lowerChest.setLockCode(code);
    }

    public LockCode getLockCode() {
        return this.upperChest.getLockCode();
    }

    public String getGuiID() {
        return this.upperChest.getGuiID();
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerChest(playerInventory, this, playerIn);
    }

    public void clearInventory() {
        this.upperChest.clearInventory();
        this.lowerChest.clearInventory();
    }
}
