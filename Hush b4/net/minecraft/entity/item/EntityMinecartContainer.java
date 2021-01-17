// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.world.LockCode;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ILockableContainer;

public abstract class EntityMinecartContainer extends EntityMinecart implements ILockableContainer
{
    private ItemStack[] minecartContainerItems;
    private boolean dropContentsWhenDead;
    
    public EntityMinecartContainer(final World worldIn) {
        super(worldIn);
        this.minecartContainerItems = new ItemStack[36];
        this.dropContentsWhenDead = true;
    }
    
    public EntityMinecartContainer(final World worldIn, final double p_i1717_2_, final double p_i1717_4_, final double p_i1717_6_) {
        super(worldIn, p_i1717_2_, p_i1717_4_, p_i1717_6_);
        this.minecartContainerItems = new ItemStack[36];
        this.dropContentsWhenDead = true;
    }
    
    @Override
    public void killMinecart(final DamageSource p_94095_1_) {
        super.killMinecart(p_94095_1_);
        if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
            InventoryHelper.func_180176_a(this.worldObj, this, this);
        }
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
        return this.minecartContainerItems[index];
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        if (this.minecartContainerItems[index] == null) {
            return null;
        }
        if (this.minecartContainerItems[index].stackSize <= count) {
            final ItemStack itemstack1 = this.minecartContainerItems[index];
            this.minecartContainerItems[index] = null;
            return itemstack1;
        }
        final ItemStack itemstack2 = this.minecartContainerItems[index].splitStack(count);
        if (this.minecartContainerItems[index].stackSize == 0) {
            this.minecartContainerItems[index] = null;
        }
        return itemstack2;
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        if (this.minecartContainerItems[index] != null) {
            final ItemStack itemstack = this.minecartContainerItems[index];
            this.minecartContainerItems[index] = null;
            return itemstack;
        }
        return null;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        this.minecartContainerItems[index] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }
    
    @Override
    public void markDirty() {
    }
    
    @Override
    public boolean isUseableByPlayer(final EntityPlayer player) {
        return !this.isDead && player.getDistanceSqToEntity(this) <= 64.0;
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
    public String getName() {
        return this.hasCustomName() ? this.getCustomNameTag() : "container.minecart";
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public void travelToDimension(final int dimensionId) {
        this.dropContentsWhenDead = false;
        super.travelToDimension(dimensionId);
    }
    
    @Override
    public void setDead() {
        if (this.dropContentsWhenDead) {
            InventoryHelper.func_180176_a(this.worldObj, this, this);
        }
        super.setDead();
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        final NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.minecartContainerItems.length; ++i) {
            if (this.minecartContainerItems[i] != null) {
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                this.minecartContainerItems[i].writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }
        tagCompound.setTag("Items", nbttaglist);
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        final NBTTagList nbttaglist = tagCompund.getTagList("Items", 10);
        this.minecartContainerItems = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            final int j = nbttagcompound.getByte("Slot") & 0xFF;
            if (j >= 0 && j < this.minecartContainerItems.length) {
                this.minecartContainerItems[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
        }
    }
    
    @Override
    public boolean interactFirst(final EntityPlayer playerIn) {
        if (!this.worldObj.isRemote) {
            playerIn.displayGUIChest(this);
        }
        return true;
    }
    
    @Override
    protected void applyDrag() {
        final int i = 15 - Container.calcRedstoneFromInventory(this);
        final float f = 0.98f + i * 0.001f;
        this.motionX *= f;
        this.motionY *= 0.0;
        this.motionZ *= f;
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
    public boolean isLocked() {
        return false;
    }
    
    @Override
    public void setLockCode(final LockCode code) {
    }
    
    @Override
    public LockCode getLockCode() {
        return LockCode.EMPTY_CODE;
    }
    
    @Override
    public void clear() {
        for (int i = 0; i < this.minecartContainerItems.length; ++i) {
            this.minecartContainerItems[i] = null;
        }
    }
}
