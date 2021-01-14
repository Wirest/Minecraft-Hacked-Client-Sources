package net.minecraft.tileentity;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEntityDispenser extends TileEntityLockable implements IInventory {
    private static final Random field_174913_f = new Random();
    private ItemStack[] field_146022_i = new ItemStack[9];
    protected String field_146020_a;
    private static final String __OBFID = "CL_00000352";

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory() {
        return 9;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int slotIn) {
        return this.field_146022_i[slotIn];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int index, int count) {
        if (this.field_146022_i[index] != null) {
            ItemStack var3;

            if (this.field_146022_i[index].stackSize <= count) {
                var3 = this.field_146022_i[index];
                this.field_146022_i[index] = null;
                this.markDirty();
                return var3;
            } else {
                var3 = this.field_146022_i[index].splitStack(count);

                if (this.field_146022_i[index].stackSize == 0) {
                    this.field_146022_i[index] = null;
                }

                this.markDirty();
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
        if (this.field_146022_i[index] != null) {
            ItemStack var2 = this.field_146022_i[index];
            this.field_146022_i[index] = null;
            return var2;
        } else {
            return null;
        }
    }

    public int func_146017_i() {
        int var1 = -1;
        int var2 = 1;

        for (int var3 = 0; var3 < this.field_146022_i.length; ++var3) {
            if (this.field_146022_i[var3] != null && field_174913_f.nextInt(var2++) == 0) {
                var1 = var3;
            }
        }

        return var1;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.field_146022_i[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    public int func_146019_a(ItemStack p_146019_1_) {
        for (int var2 = 0; var2 < this.field_146022_i.length; ++var2) {
            if (this.field_146022_i[var2] == null || this.field_146022_i[var2].getItem() == null) {
                this.setInventorySlotContents(var2, p_146019_1_);
                return var2;
            }
        }

        return -1;
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getName() {
        return this.hasCustomName() ? this.field_146020_a : "container.dispenser";
    }

    public void func_146018_a(String p_146018_1_) {
        this.field_146020_a = p_146018_1_;
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName() {
        return this.field_146020_a != null;
    }

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList var2 = compound.getTagList("Items", 10);
        this.field_146022_i = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            int var5 = var4.getByte("Slot") & 255;

            if (var5 >= 0 && var5 < this.field_146022_i.length) {
                this.field_146022_i[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        if (compound.hasKey("CustomName", 8)) {
            this.field_146020_a = compound.getString("CustomName");
        }
    }

    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.field_146022_i.length; ++var3) {
            if (this.field_146022_i[var3] != null) {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.field_146022_i[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        compound.setTag("Items", var2);

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.field_146020_a);
        }
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit() {
        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer playerIn) {
        return this.worldObj.getTileEntity(this.pos) != this ? false : playerIn.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
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

    public String getGuiID() {
        return "minecraft:dispenser";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerDispenser(playerInventory, this);
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
        for (int var1 = 0; var1 < this.field_146022_i.length; ++var1) {
            this.field_146022_i[var1] = null;
        }
    }
}
