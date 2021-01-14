package net.minecraft.entity.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;

public abstract class EntityMinecartContainer extends EntityMinecart implements ILockableContainer {
    private ItemStack[] minecartContainerItems = new ItemStack[36];

    /**
     * When set to true, the minecart will drop all items when setDead() is called. When false (such as when travelling
     * dimensions) it preserves its contents.
     */
    private boolean dropContentsWhenDead = true;
    private static final String __OBFID = "CL_00001674";

    public EntityMinecartContainer(World worldIn) {
        super(worldIn);
    }

    public EntityMinecartContainer(World worldIn, double p_i1717_2_, double p_i1717_4_, double p_i1717_6_) {
        super(worldIn, p_i1717_2_, p_i1717_4_, p_i1717_6_);
    }

    public void killMinecart(DamageSource p_94095_1_) {
        super.killMinecart(p_94095_1_);
        InventoryHelper.func_180176_a(this.worldObj, this, this);
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int slotIn) {
        return this.minecartContainerItems[slotIn];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int index, int count) {
        if (this.minecartContainerItems[index] != null) {
            ItemStack var3;

            if (this.minecartContainerItems[index].stackSize <= count) {
                var3 = this.minecartContainerItems[index];
                this.minecartContainerItems[index] = null;
                return var3;
            } else {
                var3 = this.minecartContainerItems[index].splitStack(count);

                if (this.minecartContainerItems[index].stackSize == 0) {
                    this.minecartContainerItems[index] = null;
                }

                return var3;
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
        if (this.minecartContainerItems[index] != null) {
            ItemStack var2 = this.minecartContainerItems[index];
            this.minecartContainerItems[index] = null;
            return var2;
        } else {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.minecartContainerItems[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty() {
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer playerIn) {
        return this.isDead ? false : playerIn.getDistanceSqToEntity(this) <= 64.0D;
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
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getName() {
        return this.hasCustomName() ? this.getCustomNameTag() : "container.minecart";
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit() {
        return 64;
    }

    /**
     * Teleports the entity to another dimension. Params: Dimension number to teleport to
     */
    public void travelToDimension(int dimensionId) {
        this.dropContentsWhenDead = false;
        super.travelToDimension(dimensionId);
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead() {
        if (this.dropContentsWhenDead) {
            InventoryHelper.func_180176_a(this.worldObj, this, this);
        }

        super.setDead();
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.minecartContainerItems.length; ++var3) {
            if (this.minecartContainerItems[var3] != null) {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.minecartContainerItems[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        tagCompound.setTag("Items", var2);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        NBTTagList var2 = tagCompund.getTagList("Items", 10);
        this.minecartContainerItems = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            int var5 = var4.getByte("Slot") & 255;

            if (var5 >= 0 && var5 < this.minecartContainerItems.length) {
                this.minecartContainerItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    /**
     * First layer of player interaction
     */
    public boolean interactFirst(EntityPlayer playerIn) {
        if (!this.worldObj.isRemote) {
            playerIn.displayGUIChest(this);
        }

        return true;
    }

    protected void applyDrag() {
        int var1 = 15 - Container.calcRedstoneFromInventory(this);
        float var2 = 0.98F + (float) var1 * 0.001F;
        this.motionX *= (double) var2;
        this.motionY *= 0.0D;
        this.motionZ *= (double) var2;
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
        return false;
    }

    public void setLockCode(LockCode code) {
    }

    public LockCode getLockCode() {
        return LockCode.EMPTY_CODE;
    }

    public void clearInventory() {
        for (int var1 = 0; var1 < this.minecartContainerItems.length; ++var1) {
            this.minecartContainerItems[var1] = null;
        }
    }
}
