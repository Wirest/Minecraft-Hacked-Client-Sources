// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import java.util.Random;
import net.minecraft.inventory.IInventory;

public class TileEntityDispenser extends TileEntityLockable implements IInventory
{
    private static final Random RNG;
    private ItemStack[] stacks;
    protected String customName;
    
    static {
        RNG = new Random();
    }
    
    public TileEntityDispenser() {
        this.stacks = new ItemStack[9];
    }
    
    @Override
    public int getSizeInventory() {
        return 9;
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
        return this.stacks[index];
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        if (this.stacks[index] == null) {
            return null;
        }
        if (this.stacks[index].stackSize <= count) {
            final ItemStack itemstack1 = this.stacks[index];
            this.stacks[index] = null;
            this.markDirty();
            return itemstack1;
        }
        final ItemStack itemstack2 = this.stacks[index].splitStack(count);
        if (this.stacks[index].stackSize == 0) {
            this.stacks[index] = null;
        }
        this.markDirty();
        return itemstack2;
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        if (this.stacks[index] != null) {
            final ItemStack itemstack = this.stacks[index];
            this.stacks[index] = null;
            return itemstack;
        }
        return null;
    }
    
    public int getDispenseSlot() {
        int i = -1;
        int j = 1;
        for (int k = 0; k < this.stacks.length; ++k) {
            if (this.stacks[k] != null && TileEntityDispenser.RNG.nextInt(j++) == 0) {
                i = k;
            }
        }
        return i;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        this.stacks[index] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }
    
    public int addItemStack(final ItemStack stack) {
        for (int i = 0; i < this.stacks.length; ++i) {
            if (this.stacks[i] == null || this.stacks[i].getItem() == null) {
                this.setInventorySlotContents(i, stack);
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.dispenser";
    }
    
    public void setCustomName(final String customName) {
        this.customName = customName;
    }
    
    @Override
    public boolean hasCustomName() {
        return this.customName != null;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        final NBTTagList nbttaglist = compound.getTagList("Items", 10);
        this.stacks = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            final int j = nbttagcompound.getByte("Slot") & 0xFF;
            if (j >= 0 && j < this.stacks.length) {
                this.stacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
        }
        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        final NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.stacks.length; ++i) {
            if (this.stacks[i] != null) {
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                this.stacks[i].writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }
        compound.setTag("Items", nbttaglist);
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public boolean isUseableByPlayer(final EntityPlayer player) {
        return this.worldObj.getTileEntity(this.pos) == this && player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64.0;
    }
    
    @Override
    public void openInventory(final EntityPlayer player) {
    }
    
    @Override
    public void closeInventory(final EntityPlayer player) {
    }
    
    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
        return true;
    }
    
    @Override
    public String getGuiID() {
        return "minecraft:dispenser";
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        return new ContainerDispenser(playerInventory, this);
    }
    
    @Override
    public int getField(final int id) {
        return 0;
    }
    
    @Override
    public void setField(final int id, final int value) {
    }
    
    @Override
    public int getFieldCount() {
        return 0;
    }
    
    @Override
    public void clear() {
        for (int i = 0; i < this.stacks.length; ++i) {
            this.stacks[i] = null;
        }
    }
}
