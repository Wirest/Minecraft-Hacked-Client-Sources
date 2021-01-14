package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class InventoryMerchant implements IInventory {
    private final IMerchant theMerchant;
    private ItemStack[] theInventory = new ItemStack[3];
    private final EntityPlayer thePlayer;
    private MerchantRecipe currentRecipe;
    private int currentRecipeIndex;
    private static final String __OBFID = "CL_00001756";

    public InventoryMerchant(EntityPlayer p_i1820_1_, IMerchant p_i1820_2_) {
        thePlayer = p_i1820_1_;
        theMerchant = p_i1820_2_;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory() {
        return theInventory.length;
    }

    /**
     * Returns the stack in slot i
     */
    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return theInventory[slotIn];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number
     * (second arg) of items and returns them in a new stack.
     */
    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (theInventory[index] != null) {
            ItemStack var3;

            if (index == 2) {
                var3 = theInventory[index];
                theInventory[index] = null;
                return var3;
            } else if (theInventory[index].stackSize <= count) {
                var3 = theInventory[index];
                theInventory[index] = null;

                if (inventoryResetNeededOnSlotChange(index)) {
                    resetRecipeAndSlots();
                }

                return var3;
            } else {
                var3 = theInventory[index].splitStack(count);

                if (theInventory[index].stackSize == 0) {
                    theInventory[index] = null;
                }

                if (inventoryResetNeededOnSlotChange(index)) {
                    resetRecipeAndSlots();
                }

                return var3;
            }
        } else {
            return null;
        }
    }

    /**
     * if par1 slot has changed, does resetRecipeAndSlots need to be called?
     */
    private boolean inventoryResetNeededOnSlotChange(int p_70469_1_) {
        return p_70469_1_ == 0 || p_70469_1_ == 1;
    }

    /**
     * When some containers are closed they call this on each slot, then drop
     * whatever it returns as an EntityItem - like when you close a workbench
     * GUI.
     */
    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        if (theInventory[index] != null) {
            ItemStack var2 = theInventory[index];
            theInventory[index] = null;
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
        theInventory[index] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }

        if (inventoryResetNeededOnSlotChange(index)) {
            resetRecipeAndSlots();
        }
    }

    /**
     * Gets the name of this command sender (usually username, but possibly
     * "Rcon")
     */
    @Override
    public String getName() {
        return "mob.villager";
    }

    /**
     * Returns true if this thing is named
     */
    @Override
    public boolean hasCustomName() {
        return false;
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
     * Do not make give this method the name canInteractWith because it clashes
     * with Container
     */
    @Override
    public boolean isUseableByPlayer(EntityPlayer playerIn) {
        return theMerchant.getCustomer() == playerIn;
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

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved
     * to disk later - the game won't think it hasn't changed and skip it.
     */
    @Override
    public void markDirty() {
        resetRecipeAndSlots();
    }

    public void resetRecipeAndSlots() {
        currentRecipe = null;
        ItemStack var1 = theInventory[0];
        ItemStack var2 = theInventory[1];

        if (var1 == null) {
            var1 = var2;
            var2 = null;
        }

        if (var1 == null) {
            setInventorySlotContents(2, (ItemStack) null);
        } else {
            MerchantRecipeList var3 = theMerchant.getRecipes(thePlayer);

            if (var3 != null) {
                MerchantRecipe var4 = var3.canRecipeBeUsed(var1, var2, currentRecipeIndex);

                if (var4 != null && !var4.isRecipeDisabled()) {
                    currentRecipe = var4;
                    setInventorySlotContents(2, var4.getItemToSell().copy());
                } else if (var2 != null) {
                    var4 = var3.canRecipeBeUsed(var2, var1, currentRecipeIndex);

                    if (var4 != null && !var4.isRecipeDisabled()) {
                        currentRecipe = var4;
                        setInventorySlotContents(2, var4.getItemToSell().copy());
                    } else {
                        setInventorySlotContents(2, (ItemStack) null);
                    }
                } else {
                    setInventorySlotContents(2, (ItemStack) null);
                }
            }
        }

        theMerchant.verifySellingItem(getStackInSlot(2));
    }

    public MerchantRecipe getCurrentRecipe() {
        return currentRecipe;
    }

    public void setCurrentRecipeIndex(int p_70471_1_) {
        currentRecipeIndex = p_70471_1_;
        resetRecipeAndSlots();
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
        for (int var1 = 0; var1 < theInventory.length; ++var1) {
            theInventory[var1] = null;
        }
    }
}
