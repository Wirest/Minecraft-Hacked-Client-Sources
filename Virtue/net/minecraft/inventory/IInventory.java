package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorldNameable;

public interface IInventory extends IWorldNameable
{
    /**
     * Returns the number of slots in the inventory.
     */
    int getSizeInventory();

    /**
     * Returns the stack in slot i
     */
    ItemStack getStackInSlot(int slotIn);

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    ItemStack decrStackSize(int index, int count);

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    ItemStack getStackInSlotOnClosing(int index);

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    void setInventorySlotContents(int index, ItemStack stack);

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    int getInventoryStackLimit();

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    void markDirty();

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    boolean isUseableByPlayer(EntityPlayer playerIn);

    void openInventory(EntityPlayer playerIn);

    void closeInventory(EntityPlayer playerIn);

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    boolean isItemValidForSlot(int index, ItemStack stack);

    int getField(int id);

    void setField(int id, int value);

    int getFieldCount();

    void clearInventory();
}
