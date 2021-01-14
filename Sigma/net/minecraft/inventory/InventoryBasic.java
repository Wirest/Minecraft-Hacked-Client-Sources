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
        inventoryTitle = p_i1561_1_;
        hasCustomName = p_i1561_2_;
        slotsCount = p_i1561_3_;
        inventoryContents = new ItemStack[p_i1561_3_];
    }

    public InventoryBasic(IChatComponent p_i45902_1_, int p_i45902_2_) {
        this(p_i45902_1_.getUnformattedText(), true, p_i45902_2_);
    }

    public void func_110134_a(IInvBasic p_110134_1_) {
        if (field_70480_d == null) {
            field_70480_d = Lists.newArrayList();
        }

        field_70480_d.add(p_110134_1_);
    }

    public void func_110132_b(IInvBasic p_110132_1_) {
        field_70480_d.remove(p_110132_1_);
    }

    /**
     * Returns the stack in slot i
     */
    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return slotIn >= 0 && slotIn < inventoryContents.length ? inventoryContents[slotIn] : null;
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number
     * (second arg) of items and returns them in a new stack.
     */
    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (inventoryContents[index] != null) {
            ItemStack var3;

            if (inventoryContents[index].stackSize <= count) {
                var3 = inventoryContents[index];
                inventoryContents[index] = null;
                markDirty();
                return var3;
            } else {
                var3 = inventoryContents[index].splitStack(count);

                if (inventoryContents[index].stackSize == 0) {
                    inventoryContents[index] = null;
                }

                markDirty();
                return var3;
            }
        } else {
            return null;
        }
    }

    public ItemStack func_174894_a(ItemStack p_174894_1_) {
        ItemStack var2 = p_174894_1_.copy();

        for (int var3 = 0; var3 < slotsCount; ++var3) {
            ItemStack var4 = getStackInSlot(var3);

            if (var4 == null) {
                setInventorySlotContents(var3, var2);
                markDirty();
                return null;
            }

            if (ItemStack.areItemsEqual(var4, var2)) {
                int var5 = Math.min(getInventoryStackLimit(), var4.getMaxStackSize());
                int var6 = Math.min(var2.stackSize, var5 - var4.stackSize);

                if (var6 > 0) {
                    var4.stackSize += var6;
                    var2.stackSize -= var6;

                    if (var2.stackSize <= 0) {
                        markDirty();
                        return null;
                    }
                }
            }
        }

        if (var2.stackSize != p_174894_1_.stackSize) {
            markDirty();
        }

        return var2;
    }

    /**
     * When some containers are closed they call this on each slot, then drop
     * whatever it returns as an EntityItem - like when you close a workbench
     * GUI.
     */
    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        if (inventoryContents[index] != null) {
            ItemStack var2 = inventoryContents[index];
            inventoryContents[index] = null;
            return var2;
        } else {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be
     * crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inventoryContents[index] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }

        markDirty();
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory() {
        return slotsCount;
    }

    /**
     * Gets the name of this command sender (usually username, but possibly
     * "Rcon")
     */
    @Override
    public String getName() {
        return inventoryTitle;
    }

    /**
     * Returns true if this thing is named
     */
    @Override
    public boolean hasCustomName() {
        return hasCustomName;
    }

    public void func_110133_a(String p_110133_1_) {
        hasCustomName = true;
        inventoryTitle = p_110133_1_;
    }

    @Override
    public IChatComponent getDisplayName() {
        return hasCustomName() ? new ChatComponentText(getName()) : new ChatComponentTranslation(getName(), new Object[0]);
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be
     * 64, possibly will be extended. *Isn't this more of a set than a get?*
     */
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved
     * to disk later - the game won't think it hasn't changed and skip it.
     */
    @Override
    public void markDirty() {
        if (field_70480_d != null) {
            for (int var1 = 0; var1 < field_70480_d.size(); ++var1) {
                ((IInvBasic) field_70480_d.get(var1)).onInventoryChanged(this);
            }
        }
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes
     * with Container
     */
    @Override
    public boolean isUseableByPlayer(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer playerIn) {
    }

    @Override
    public void closeInventory(EntityPlayer playerIn) {
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
    public void clearInventory() {
        for (int var1 = 0; var1 < inventoryContents.length; ++var1) {
            inventoryContents[var1] = null;
        }
    }
}
