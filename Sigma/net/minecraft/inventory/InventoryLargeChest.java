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
        name = p_i45905_1_;

        if (p_i45905_2_ == null) {
            p_i45905_2_ = p_i45905_3_;
        }

        if (p_i45905_3_ == null) {
            p_i45905_3_ = p_i45905_2_;
        }

        upperChest = p_i45905_2_;
        lowerChest = p_i45905_3_;

        if (p_i45905_2_.isLocked()) {
            p_i45905_3_.setLockCode(p_i45905_2_.getLockCode());
        } else if (p_i45905_3_.isLocked()) {
            p_i45905_2_.setLockCode(p_i45905_3_.getLockCode());
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory() {
        return upperChest.getSizeInventory() + lowerChest.getSizeInventory();
    }

    /**
     * Return whether the given inventory is part of this large chest.
     */
    public boolean isPartOfLargeChest(IInventory p_90010_1_) {
        return upperChest == p_90010_1_ || lowerChest == p_90010_1_;
    }

    /**
     * Gets the name of this command sender (usually username, but possibly
     * "Rcon")
     */
    @Override
    public String getName() {
        return upperChest.hasCustomName() ? upperChest.getName() : (lowerChest.hasCustomName() ? lowerChest.getName() : name);
    }

    /**
     * Returns true if this thing is named
     */
    @Override
    public boolean hasCustomName() {
        return upperChest.hasCustomName() || lowerChest.hasCustomName();
    }

    @Override
    public IChatComponent getDisplayName() {
        return hasCustomName() ? new ChatComponentText(getName()) : new ChatComponentTranslation(getName(), new Object[0]);
    }

    /**
     * Returns the stack in slot i
     */
    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return slotIn >= upperChest.getSizeInventory() ? lowerChest.getStackInSlot(slotIn - upperChest.getSizeInventory()) : upperChest.getStackInSlot(slotIn);
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number
     * (second arg) of items and returns them in a new stack.
     */
    @Override
    public ItemStack decrStackSize(int index, int count) {
        return index >= upperChest.getSizeInventory() ? lowerChest.decrStackSize(index - upperChest.getSizeInventory(), count) : upperChest.decrStackSize(index, count);
    }

    /**
     * When some containers are closed they call this on each slot, then drop
     * whatever it returns as an EntityItem - like when you close a workbench
     * GUI.
     */
    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        return index >= upperChest.getSizeInventory() ? lowerChest.getStackInSlotOnClosing(index - upperChest.getSizeInventory()) : upperChest.getStackInSlotOnClosing(index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be
     * crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index >= upperChest.getSizeInventory()) {
            lowerChest.setInventorySlotContents(index - upperChest.getSizeInventory(), stack);
        } else {
            upperChest.setInventorySlotContents(index, stack);
        }
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be
     * 64, possibly will be extended. *Isn't this more of a set than a get?*
     */
    @Override
    public int getInventoryStackLimit() {
        return upperChest.getInventoryStackLimit();
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved
     * to disk later - the game won't think it hasn't changed and skip it.
     */
    @Override
    public void markDirty() {
        upperChest.markDirty();
        lowerChest.markDirty();
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes
     * with Container
     */
    @Override
    public boolean isUseableByPlayer(EntityPlayer playerIn) {
        return upperChest.isUseableByPlayer(playerIn) && lowerChest.isUseableByPlayer(playerIn);
    }

    @Override
    public void openInventory(EntityPlayer playerIn) {
        upperChest.openInventory(playerIn);
        lowerChest.openInventory(playerIn);
    }

    @Override
    public void closeInventory(EntityPlayer playerIn) {
        upperChest.closeInventory(playerIn);
        lowerChest.closeInventory(playerIn);
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring
     * stack size) into the given slot.
     */
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public boolean isLocked() {
        return upperChest.isLocked() || lowerChest.isLocked();
    }

    @Override
    public void setLockCode(LockCode code) {
        upperChest.setLockCode(code);
        lowerChest.setLockCode(code);
    }

    @Override
    public LockCode getLockCode() {
        return upperChest.getLockCode();
    }

    @Override
    public String getGuiID() {
        return upperChest.getGuiID();
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerChest(playerInventory, this, playerIn);
    }

    @Override
    public void clearInventory() {
        upperChest.clearInventory();
        lowerChest.clearInventory();
    }
}
