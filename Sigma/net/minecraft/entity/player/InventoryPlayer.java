package net.minecraft.entity.player;

import java.util.concurrent.Callable;

import net.minecraft.block.Block;
import net.minecraft.command.server.CommandTestForBlock;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ReportedException;

public class InventoryPlayer implements IInventory {
    /**
     * An array of 36 item stacks indicating the main player inventory (including the visible bar).
     */
    public ItemStack[] mainInventory = new ItemStack[36];

    /**
     * An array of 4 item stacks containing the currently worn armor pieces.
     */
    public ItemStack[] armorInventory = new ItemStack[4];

    /**
     * The index of the currently held item (0-8).
     */
    public int currentItem;

    /**
     * The player whose inventory this is.
     */
    public EntityPlayer player;
    private ItemStack itemStack;

    /**
     * Set true whenever the inventory changes. Nothing sets it false so you will have to write your own code to check
     * it and reset the value.
     */
    public boolean inventoryChanged;
    private static final String __OBFID = "CL_00001709";

    public InventoryPlayer(EntityPlayer p_i1750_1_) {
        this.player = p_i1750_1_;
    }

    /**
     * Returns the item stack currently held by the player.
     */
    public ItemStack getCurrentItem() {
        return this.currentItem < 9 && this.currentItem >= 0 ? this.mainInventory[this.currentItem] : null;
    }

    /**
     * Get the size of the player hotbar inventory
     */
    public static int getHotbarSize() {
        return 9;
    }

    private int getInventorySlotContainItem(Item itemIn) {
        for (int var2 = 0; var2 < this.mainInventory.length; ++var2) {
            if (this.mainInventory[var2] != null && this.mainInventory[var2].getItem() == itemIn) {
                return var2;
            }
        }

        return -1;
    }

    private int getInventorySlotContainItemAndDamage(Item p_146024_1_, int p_146024_2_) {
        for (int var3 = 0; var3 < this.mainInventory.length; ++var3) {
            if (this.mainInventory[var3] != null && this.mainInventory[var3].getItem() == p_146024_1_ && this.mainInventory[var3].getMetadata() == p_146024_2_) {
                return var3;
            }
        }

        return -1;
    }

    /**
     * stores an itemstack in the users inventory
     */
    private int storeItemStack(ItemStack p_70432_1_) {
        for (int var2 = 0; var2 < this.mainInventory.length; ++var2) {
            if (this.mainInventory[var2] != null && this.mainInventory[var2].getItem() == p_70432_1_.getItem() && this.mainInventory[var2].isStackable() && this.mainInventory[var2].stackSize < this.mainInventory[var2].getMaxStackSize() && this.mainInventory[var2].stackSize < this.getInventoryStackLimit() && (!this.mainInventory[var2].getHasSubtypes() || this.mainInventory[var2].getMetadata() == p_70432_1_.getMetadata()) && ItemStack.areItemStackTagsEqual(this.mainInventory[var2], p_70432_1_)) {
                return var2;
            }
        }

        return -1;
    }

    /**
     * Returns the first item stack that is empty.
     */
    public int getFirstEmptyStack() {
        for (int var1 = 0; var1 < this.mainInventory.length; ++var1) {
            if (this.mainInventory[var1] == null) {
                return var1;
            }
        }

        return -1;
    }

    public void setCurrentItem(Item p_146030_1_, int p_146030_2_, boolean p_146030_3_, boolean p_146030_4_) {
        ItemStack var5 = this.getCurrentItem();
        int var6 = p_146030_3_ ? this.getInventorySlotContainItemAndDamage(p_146030_1_, p_146030_2_) : this.getInventorySlotContainItem(p_146030_1_);

        if (var6 >= 0 && var6 < 9) {
            this.currentItem = var6;
        } else if (p_146030_4_ && p_146030_1_ != null) {
            int var7 = this.getFirstEmptyStack();

            if (var7 >= 0 && var7 < 9) {
                this.currentItem = var7;
            }

            if (var5 == null || !var5.isItemEnchantable() || this.getInventorySlotContainItemAndDamage(var5.getItem(), var5.getItemDamage()) != this.currentItem) {
                int var8 = this.getInventorySlotContainItemAndDamage(p_146030_1_, p_146030_2_);
                int var9;

                if (var8 >= 0) {
                    var9 = this.mainInventory[var8].stackSize;
                    this.mainInventory[var8] = this.mainInventory[this.currentItem];
                } else {
                    var9 = 1;
                }

                this.mainInventory[this.currentItem] = new ItemStack(p_146030_1_, var9, p_146030_2_);
            }
        }
    }

    /**
     * Switch the current item to the next one or the previous one
     */
    public void changeCurrentItem(int p_70453_1_) {
        if (p_70453_1_ > 0) {
            p_70453_1_ = 1;
        }

        if (p_70453_1_ < 0) {
            p_70453_1_ = -1;
        }

        for (this.currentItem -= p_70453_1_; this.currentItem < 0; this.currentItem += 9) {
            ;
        }

        while (this.currentItem >= 9) {
            this.currentItem -= 9;
        }
    }

    public int func_174925_a(Item p_174925_1_, int p_174925_2_, int p_174925_3_, NBTTagCompound p_174925_4_) {
        int var5 = 0;
        int var6;
        ItemStack var7;
        int var8;

        for (var6 = 0; var6 < this.mainInventory.length; ++var6) {
            var7 = this.mainInventory[var6];

            if (var7 != null && (p_174925_1_ == null || var7.getItem() == p_174925_1_) && (p_174925_2_ <= -1 || var7.getMetadata() == p_174925_2_) && (p_174925_4_ == null || CommandTestForBlock.func_175775_a(p_174925_4_, var7.getTagCompound(), true))) {
                var8 = p_174925_3_ <= 0 ? var7.stackSize : Math.min(p_174925_3_ - var5, var7.stackSize);
                var5 += var8;

                if (p_174925_3_ != 0) {
                    this.mainInventory[var6].stackSize -= var8;

                    if (this.mainInventory[var6].stackSize == 0) {
                        this.mainInventory[var6] = null;
                    }

                    if (p_174925_3_ > 0 && var5 >= p_174925_3_) {
                        return var5;
                    }
                }
            }
        }

        for (var6 = 0; var6 < this.armorInventory.length; ++var6) {
            var7 = this.armorInventory[var6];

            if (var7 != null && (p_174925_1_ == null || var7.getItem() == p_174925_1_) && (p_174925_2_ <= -1 || var7.getMetadata() == p_174925_2_) && (p_174925_4_ == null || CommandTestForBlock.func_175775_a(p_174925_4_, var7.getTagCompound(), false))) {
                var8 = p_174925_3_ <= 0 ? var7.stackSize : Math.min(p_174925_3_ - var5, var7.stackSize);
                var5 += var8;

                if (p_174925_3_ != 0) {
                    this.armorInventory[var6].stackSize -= var8;

                    if (this.armorInventory[var6].stackSize == 0) {
                        this.armorInventory[var6] = null;
                    }

                    if (p_174925_3_ > 0 && var5 >= p_174925_3_) {
                        return var5;
                    }
                }
            }
        }

        if (this.itemStack != null) {
            if (p_174925_1_ != null && this.itemStack.getItem() != p_174925_1_) {
                return var5;
            }

            if (p_174925_2_ > -1 && this.itemStack.getMetadata() != p_174925_2_) {
                return var5;
            }

            if (p_174925_4_ != null && !CommandTestForBlock.func_175775_a(p_174925_4_, this.itemStack.getTagCompound(), false)) {
                return var5;
            }

            var6 = p_174925_3_ <= 0 ? this.itemStack.stackSize : Math.min(p_174925_3_ - var5, this.itemStack.stackSize);
            var5 += var6;

            if (p_174925_3_ != 0) {
                this.itemStack.stackSize -= var6;

                if (this.itemStack.stackSize == 0) {
                    this.itemStack = null;
                }

                if (p_174925_3_ > 0 && var5 >= p_174925_3_) {
                    return var5;
                }
            }
        }

        return var5;
    }

    /**
     * This function stores as many items of an ItemStack as possible in a matching slot and returns the quantity of
     * left over items.
     */
    private int storePartialItemStack(ItemStack p_70452_1_) {
        Item var2 = p_70452_1_.getItem();
        int var3 = p_70452_1_.stackSize;
        int var4 = this.storeItemStack(p_70452_1_);

        if (var4 < 0) {
            var4 = this.getFirstEmptyStack();
        }

        if (var4 < 0) {
            return var3;
        } else {
            if (this.mainInventory[var4] == null) {
                this.mainInventory[var4] = new ItemStack(var2, 0, p_70452_1_.getMetadata());

                if (p_70452_1_.hasTagCompound()) {
                    this.mainInventory[var4].setTagCompound((NBTTagCompound) p_70452_1_.getTagCompound().copy());
                }
            }

            int var5 = var3;

            if (var3 > this.mainInventory[var4].getMaxStackSize() - this.mainInventory[var4].stackSize) {
                var5 = this.mainInventory[var4].getMaxStackSize() - this.mainInventory[var4].stackSize;
            }

            if (var5 > this.getInventoryStackLimit() - this.mainInventory[var4].stackSize) {
                var5 = this.getInventoryStackLimit() - this.mainInventory[var4].stackSize;
            }

            if (var5 == 0) {
                return var3;
            } else {
                var3 -= var5;
                this.mainInventory[var4].stackSize += var5;
                this.mainInventory[var4].animationsToGo = 5;
                return var3;
            }
        }
    }

    /**
     * Decrement the number of animations remaining. Only called on client side. This is used to handle the animation of
     * receiving a block.
     */
    public void decrementAnimations() {
        for (int var1 = 0; var1 < this.mainInventory.length; ++var1) {
            if (this.mainInventory[var1] != null) {
                this.mainInventory[var1].updateAnimation(this.player.worldObj, this.player, var1, this.currentItem == var1);
            }
        }
    }

    /**
     * removed one item of specified Item from inventory (if it is in a stack, the stack size will reduce with 1)
     */
    public boolean consumeInventoryItem(Item p_146026_1_) {
        int var2 = this.getInventorySlotContainItem(p_146026_1_);

        if (var2 < 0) {
            return false;
        } else {
            if (--this.mainInventory[var2].stackSize <= 0) {
                this.mainInventory[var2] = null;
            }

            return true;
        }
    }

    /**
     * Checks if a specified Item is inside the inventory
     */
    public boolean hasItem(Item p_146028_1_) {
        int var2 = this.getInventorySlotContainItem(p_146028_1_);
        return var2 >= 0;
    }

    /**
     * Adds the item stack to the inventory, returns false if it is impossible.
     */
    public boolean addItemStackToInventory(final ItemStack p_70441_1_) {
        if (p_70441_1_ != null && p_70441_1_.stackSize != 0 && p_70441_1_.getItem() != null) {
            try {
                int var2;

                if (p_70441_1_.isItemDamaged()) {
                    var2 = this.getFirstEmptyStack();

                    if (var2 >= 0) {
                        this.mainInventory[var2] = ItemStack.copyItemStack(p_70441_1_);
                        this.mainInventory[var2].animationsToGo = 5;
                        p_70441_1_.stackSize = 0;
                        return true;
                    } else if (this.player.capabilities.isCreativeMode) {
                        p_70441_1_.stackSize = 0;
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    do {
                        var2 = p_70441_1_.stackSize;
                        p_70441_1_.stackSize = this.storePartialItemStack(p_70441_1_);
                    }
                    while (p_70441_1_.stackSize > 0 && p_70441_1_.stackSize < var2);

                    if (p_70441_1_.stackSize == var2 && this.player.capabilities.isCreativeMode) {
                        p_70441_1_.stackSize = 0;
                        return true;
                    } else {
                        return p_70441_1_.stackSize < var2;
                    }
                }
            } catch (Throwable var5) {
                CrashReport var3 = CrashReport.makeCrashReport(var5, "Adding item to inventory");
                CrashReportCategory var4 = var3.makeCategory("Item being added");
                var4.addCrashSection("Item ID", Integer.valueOf(Item.getIdFromItem(p_70441_1_.getItem())));
                var4.addCrashSection("Item data", Integer.valueOf(p_70441_1_.getMetadata()));
                var4.addCrashSectionCallable("Item name", new Callable() {
                    private static final String __OBFID = "CL_00001710";

                    public String call() {
                        return p_70441_1_.getDisplayName();
                    }
                });
                throw new ReportedException(var3);
            }
        } else {
            return false;
        }
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int index, int count) {
        ItemStack[] var3 = this.mainInventory;

        if (index >= this.mainInventory.length) {
            var3 = this.armorInventory;
            index -= this.mainInventory.length;
        }

        if (var3[index] != null) {
            ItemStack var4;

            if (var3[index].stackSize <= count) {
                var4 = var3[index];
                var3[index] = null;
                return var4;
            } else {
                var4 = var3[index].splitStack(count);

                if (var3[index].stackSize == 0) {
                    var3[index] = null;
                }

                return var4;
            }
        } else {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int index) {
        ItemStack[] var2 = this.mainInventory;

        if (index >= this.mainInventory.length) {
            var2 = this.armorInventory;
            index -= this.mainInventory.length;
        }

        if (var2[index] != null) {
            ItemStack var3 = var2[index];
            var2[index] = null;
            return var3;
        } else {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack[] var3 = this.mainInventory;

        if (index >= var3.length) {
            index -= var3.length;
            var3 = this.armorInventory;
        }

        var3[index] = stack;
    }

    public float getStrVsBlock(Block p_146023_1_) {
        float var2 = 1.0F;

        if (this.mainInventory[this.currentItem] != null) {
            var2 *= this.mainInventory[this.currentItem].getStrVsBlock(p_146023_1_);
        }

        return var2;
    }

    /**
     * Writes the inventory out as a list of compound tags. This is where the slot indices are used (+100 for armor, +80
     * for crafting).
     */
    public NBTTagList writeToNBT(NBTTagList p_70442_1_) {
        int var2;
        NBTTagCompound var3;

        for (var2 = 0; var2 < this.mainInventory.length; ++var2) {
            if (this.mainInventory[var2] != null) {
                var3 = new NBTTagCompound();
                var3.setByte("Slot", (byte) var2);
                this.mainInventory[var2].writeToNBT(var3);
                p_70442_1_.appendTag(var3);
            }
        }

        for (var2 = 0; var2 < this.armorInventory.length; ++var2) {
            if (this.armorInventory[var2] != null) {
                var3 = new NBTTagCompound();
                var3.setByte("Slot", (byte) (var2 + 100));
                this.armorInventory[var2].writeToNBT(var3);
                p_70442_1_.appendTag(var3);
            }
        }

        return p_70442_1_;
    }

    /**
     * Reads from the given tag list and fills the slots in the inventory with the correct items.
     */
    public void readFromNBT(NBTTagList p_70443_1_) {
        this.mainInventory = new ItemStack[36];
        this.armorInventory = new ItemStack[4];

        for (int var2 = 0; var2 < p_70443_1_.tagCount(); ++var2) {
            NBTTagCompound var3 = p_70443_1_.getCompoundTagAt(var2);
            int var4 = var3.getByte("Slot") & 255;
            ItemStack var5 = ItemStack.loadItemStackFromNBT(var3);

            if (var5 != null) {
                if (var4 >= 0 && var4 < this.mainInventory.length) {
                    this.mainInventory[var4] = var5;
                }

                if (var4 >= 100 && var4 < this.armorInventory.length + 100) {
                    this.armorInventory[var4 - 100] = var5;
                }
            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory() {
        return this.mainInventory.length + 4;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int slotIn) {
        ItemStack[] var2 = this.mainInventory;

        if (slotIn >= var2.length) {
            slotIn -= var2.length;
            var2 = this.armorInventory;
        }

        return var2[slotIn];
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getName() {
        return "container.inventory";
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName() {
        return false;
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

    public boolean func_146025_b(Block p_146025_1_) {
        if (p_146025_1_.getMaterial().isToolNotRequired()) {
            return true;
        } else {
            ItemStack var2 = this.getStackInSlot(this.currentItem);
            return var2 != null ? var2.canHarvestBlock(p_146025_1_) : false;
        }
    }

    /**
     * returns a player armor item (as itemstack) contained in specified armor slot.
     */
    public ItemStack armorItemInSlot(int p_70440_1_) {
        return this.armorInventory[p_70440_1_];
    }

    /**
     * Based on the damage values and maximum damage values of each armor item, returns the current armor value.
     */
    public int getTotalArmorValue() {
        int var1 = 0;

        for (int var2 = 0; var2 < this.armorInventory.length; ++var2) {
            if (this.armorInventory[var2] != null && this.armorInventory[var2].getItem() instanceof ItemArmor) {
                int var3 = ((ItemArmor) this.armorInventory[var2].getItem()).damageReduceAmount;
                var1 += var3;
            }
        }

        return var1;
    }

    /**
     * Damages armor in each slot by the specified amount.
     */
    public void damageArmor(float p_70449_1_) {
        p_70449_1_ /= 4.0F;

        if (p_70449_1_ < 1.0F) {
            p_70449_1_ = 1.0F;
        }

        for (int var2 = 0; var2 < this.armorInventory.length; ++var2) {
            if (this.armorInventory[var2] != null && this.armorInventory[var2].getItem() instanceof ItemArmor) {
                this.armorInventory[var2].damageItem((int) p_70449_1_, this.player);

                if (this.armorInventory[var2].stackSize == 0) {
                    this.armorInventory[var2] = null;
                }
            }
        }
    }

    /**
     * Drop all armor and main inventory items.
     */
    public void dropAllItems() {
        int var1;

        for (var1 = 0; var1 < this.mainInventory.length; ++var1) {
            if (this.mainInventory[var1] != null) {
                this.player.func_146097_a(this.mainInventory[var1], true, false);
                this.mainInventory[var1] = null;
            }
        }

        for (var1 = 0; var1 < this.armorInventory.length; ++var1) {
            if (this.armorInventory[var1] != null) {
                this.player.func_146097_a(this.armorInventory[var1], true, false);
                this.armorInventory[var1] = null;
            }
        }
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty() {
        this.inventoryChanged = true;
    }

    /**
     * Set the stack helds by mouse, used in GUI/Container
     */
    public void setItemStack(ItemStack p_70437_1_) {
        this.itemStack = p_70437_1_;
    }

    /**
     * Stack helds by mouse, used in GUI and Containers
     */
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer playerIn) {
        return this.player.isDead ? false : playerIn.getDistanceSqToEntity(this.player) <= 64.0D;
    }

    /**
     * Returns true if the specified ItemStack exists in the inventory.
     */
    public boolean hasItemStack(ItemStack p_70431_1_) {
        int var2;

        for (var2 = 0; var2 < this.armorInventory.length; ++var2) {
            if (this.armorInventory[var2] != null && this.armorInventory[var2].isItemEqual(p_70431_1_)) {
                return true;
            }
        }

        for (var2 = 0; var2 < this.mainInventory.length; ++var2) {
            if (this.mainInventory[var2] != null && this.mainInventory[var2].isItemEqual(p_70431_1_)) {
                return true;
            }
        }

        return false;
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

    /**
     * Copy the ItemStack contents from another InventoryPlayer instance
     */
    public void copyInventory(InventoryPlayer p_70455_1_) {
        int var2;

        for (var2 = 0; var2 < this.mainInventory.length; ++var2) {
            this.mainInventory[var2] = ItemStack.copyItemStack(p_70455_1_.mainInventory[var2]);
        }

        for (var2 = 0; var2 < this.armorInventory.length; ++var2) {
            this.armorInventory[var2] = ItemStack.copyItemStack(p_70455_1_.armorInventory[var2]);
        }

        this.currentItem = p_70455_1_.currentItem;
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
        int var1;

        for (var1 = 0; var1 < this.mainInventory.length; ++var1) {
            this.mainInventory[var1] = null;
        }

        for (var1 = 0; var1 < this.armorInventory.length; ++var1) {
            this.armorInventory[var1] = null;
        }
    }
}
