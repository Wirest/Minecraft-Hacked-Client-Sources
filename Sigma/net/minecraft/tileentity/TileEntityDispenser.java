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
    @Override
    public int getSizeInventory() {
        return 9;
    }

    /**
     * Returns the stack in slot i
     */
    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return field_146022_i[slotIn];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number
     * (second arg) of items and returns them in a new stack.
     */
    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (field_146022_i[index] != null) {
            ItemStack var3;

            if (field_146022_i[index].stackSize <= count) {
                var3 = field_146022_i[index];
                field_146022_i[index] = null;
                markDirty();
                return var3;
            } else {
                var3 = field_146022_i[index].splitStack(count);

                if (field_146022_i[index].stackSize == 0) {
                    field_146022_i[index] = null;
                }

                markDirty();
                return var3;
            }
        } else {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop
     * whatever it returns as an EntityItem - like when you close a workbench
     * GUI.
     */
    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        if (field_146022_i[index] != null) {
            ItemStack var2 = field_146022_i[index];
            field_146022_i[index] = null;
            return var2;
        } else {
            return null;
        }
    }

    public int func_146017_i() {
        int var1 = -1;
        int var2 = 1;

        for (int var3 = 0; var3 < field_146022_i.length; ++var3) {
            if (field_146022_i[var3] != null && TileEntityDispenser.field_174913_f.nextInt(var2++) == 0) {
                var1 = var3;
            }
        }

        return var1;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be
     * crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        field_146022_i[index] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }

        markDirty();
    }

    public int func_146019_a(ItemStack p_146019_1_) {
        for (int var2 = 0; var2 < field_146022_i.length; ++var2) {
            if (field_146022_i[var2] == null || field_146022_i[var2].getItem() == null) {
                setInventorySlotContents(var2, p_146019_1_);
                return var2;
            }
        }

        return -1;
    }

    /**
     * Gets the name of this command sender (usually username, but possibly
     * "Rcon")
     */
    @Override
    public String getName() {
        return hasCustomName() ? field_146020_a : "container.dispenser";
    }

    public void func_146018_a(String p_146018_1_) {
        field_146020_a = p_146018_1_;
    }

    /**
     * Returns true if this thing is named
     */
    @Override
    public boolean hasCustomName() {
        return field_146020_a != null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList var2 = compound.getTagList("Items", 10);
        field_146022_i = new ItemStack[getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            int var5 = var4.getByte("Slot") & 255;

            if (var5 >= 0 && var5 < field_146022_i.length) {
                field_146022_i[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        if (compound.hasKey("CustomName", 8)) {
            field_146020_a = compound.getString("CustomName");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < field_146022_i.length; ++var3) {
            if (field_146022_i[var3] != null) {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                field_146022_i[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        compound.setTag("Items", var2);

        if (hasCustomName()) {
            compound.setString("CustomName", field_146020_a);
        }
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
        return worldObj.getTileEntity(pos) != this ? false : playerIn.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
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
    public String getGuiID() {
        return "minecraft:dispenser";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerDispenser(playerInventory, this);
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
        for (int var1 = 0; var1 < field_146022_i.length; ++var1) {
            field_146022_i[var1] = null;
        }
    }
}
