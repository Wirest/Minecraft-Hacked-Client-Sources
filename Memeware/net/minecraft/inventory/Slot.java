package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class Slot {
    /**
     * The index of the slot in the inventory.
     */
    private final int slotIndex;

    /**
     * The inventory we want to extract a slot from.
     */
    public final IInventory inventory;

    /**
     * the id of the slot(also the index in the inventory arraylist)
     */
    public int slotNumber;

    /**
     * display position of the inventory slot on the screen x axis
     */
    public int xDisplayPosition;

    /**
     * display position of the inventory slot on the screen y axis
     */
    public int yDisplayPosition;
    private static final String __OBFID = "CL_00001762";

    public Slot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        this.inventory = p_i1824_1_;
        this.slotIndex = p_i1824_2_;
        this.xDisplayPosition = p_i1824_3_;
        this.yDisplayPosition = p_i1824_4_;
    }

    /**
     * if par2 has more items than par1, onCrafting(item,countIncrease) is called
     */
    public void onSlotChange(ItemStack p_75220_1_, ItemStack p_75220_2_) {
        if (p_75220_1_ != null && p_75220_2_ != null) {
            if (p_75220_1_.getItem() == p_75220_2_.getItem()) {
                int var3 = p_75220_2_.stackSize - p_75220_1_.stackSize;

                if (var3 > 0) {
                    this.onCrafting(p_75220_1_, var3);
                }
            }
        }
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
     * internal count then calls onCrafting(item).
     */
    protected void onCrafting(ItemStack p_75210_1_, int p_75210_2_) {
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    protected void onCrafting(ItemStack p_75208_1_) {
    }

    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
        this.onSlotChanged();
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack stack) {
        return true;
    }

    /**
     * Helper fnct to get the stack in the slot.
     */
    public ItemStack getStack() {
        return this.inventory.getStackInSlot(this.slotIndex);
    }

    /**
     * Returns if this slot contains a stack.
     */
    public boolean getHasStack() {
        return this.getStack() != null;
    }

    /**
     * Helper method to put a stack in the slot.
     */
    public void putStack(ItemStack p_75215_1_) {
        this.inventory.setInventorySlotContents(this.slotIndex, p_75215_1_);
        this.onSlotChanged();
    }

    /**
     * Called when the stack in a Slot changes
     */
    public void onSlotChanged() {
        this.inventory.markDirty();
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    public int getSlotStackLimit() {
        return this.inventory.getInventoryStackLimit();
    }

    public int func_178170_b(ItemStack p_178170_1_) {
        return this.getSlotStackLimit();
    }

    public String func_178171_c() {
        return null;
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public ItemStack decrStackSize(int p_75209_1_) {
        return this.inventory.decrStackSize(this.slotIndex, p_75209_1_);
    }

    /**
     * returns true if the slot exists in the given inventory and location
     */
    public boolean isHere(IInventory p_75217_1_, int p_75217_2_) {
        return p_75217_1_ == this.inventory && p_75217_2_ == this.slotIndex;
    }

    /**
     * Return whether this slot's stack can be taken from this slot.
     */
    public boolean canTakeStack(EntityPlayer p_82869_1_) {
        return true;
    }

    /**
     * Actualy only call when we want to render the white square effect over the slots. Return always True, except for
     * the armor slot of the Donkey/Mule (we can't interact with the Undead and Skeleton horses)
     */
    public boolean canBeHovered() {
        return true;
    }
}
