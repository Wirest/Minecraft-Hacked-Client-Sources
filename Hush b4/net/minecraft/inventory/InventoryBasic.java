// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatComponentText;
import com.google.common.collect.Lists;
import net.minecraft.util.IChatComponent;
import java.util.List;
import net.minecraft.item.ItemStack;

public class InventoryBasic implements IInventory
{
    private String inventoryTitle;
    private int slotsCount;
    private ItemStack[] inventoryContents;
    private List<IInvBasic> field_70480_d;
    private boolean hasCustomName;
    
    public InventoryBasic(final String title, final boolean customName, final int slotCount) {
        this.inventoryTitle = title;
        this.hasCustomName = customName;
        this.slotsCount = slotCount;
        this.inventoryContents = new ItemStack[slotCount];
    }
    
    public InventoryBasic(final IChatComponent title, final int slotCount) {
        this(title.getUnformattedText(), true, slotCount);
    }
    
    public void func_110134_a(final IInvBasic p_110134_1_) {
        if (this.field_70480_d == null) {
            this.field_70480_d = (List<IInvBasic>)Lists.newArrayList();
        }
        this.field_70480_d.add(p_110134_1_);
    }
    
    public void func_110132_b(final IInvBasic p_110132_1_) {
        this.field_70480_d.remove(p_110132_1_);
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
        return (index >= 0 && index < this.inventoryContents.length) ? this.inventoryContents[index] : null;
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        if (this.inventoryContents[index] == null) {
            return null;
        }
        if (this.inventoryContents[index].stackSize <= count) {
            final ItemStack itemstack1 = this.inventoryContents[index];
            this.inventoryContents[index] = null;
            this.markDirty();
            return itemstack1;
        }
        final ItemStack itemstack2 = this.inventoryContents[index].splitStack(count);
        if (this.inventoryContents[index].stackSize == 0) {
            this.inventoryContents[index] = null;
        }
        this.markDirty();
        return itemstack2;
    }
    
    public ItemStack func_174894_a(final ItemStack stack) {
        final ItemStack itemstack = stack.copy();
        for (int i = 0; i < this.slotsCount; ++i) {
            final ItemStack itemstack2 = this.getStackInSlot(i);
            if (itemstack2 == null) {
                this.setInventorySlotContents(i, itemstack);
                this.markDirty();
                return null;
            }
            if (ItemStack.areItemsEqual(itemstack2, itemstack)) {
                final int j = Math.min(this.getInventoryStackLimit(), itemstack2.getMaxStackSize());
                final int k = Math.min(itemstack.stackSize, j - itemstack2.stackSize);
                if (k > 0) {
                    final ItemStack itemStack = itemstack2;
                    itemStack.stackSize += k;
                    final ItemStack itemStack2 = itemstack;
                    itemStack2.stackSize -= k;
                    if (itemstack.stackSize <= 0) {
                        this.markDirty();
                        return null;
                    }
                }
            }
        }
        if (itemstack.stackSize != stack.stackSize) {
            this.markDirty();
        }
        return itemstack;
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        if (this.inventoryContents[index] != null) {
            final ItemStack itemstack = this.inventoryContents[index];
            this.inventoryContents[index] = null;
            return itemstack;
        }
        return null;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        this.inventoryContents[index] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }
    
    @Override
    public int getSizeInventory() {
        return this.slotsCount;
    }
    
    @Override
    public String getName() {
        return this.inventoryTitle;
    }
    
    @Override
    public boolean hasCustomName() {
        return this.hasCustomName;
    }
    
    public void setCustomName(final String inventoryTitleIn) {
        this.hasCustomName = true;
        this.inventoryTitle = inventoryTitleIn;
    }
    
    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public void markDirty() {
        if (this.field_70480_d != null) {
            for (int i = 0; i < this.field_70480_d.size(); ++i) {
                this.field_70480_d.get(i).onInventoryChanged(this);
            }
        }
    }
    
    @Override
    public boolean isUseableByPlayer(final EntityPlayer player) {
        return true;
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
        for (int i = 0; i < this.inventoryContents.length; ++i) {
            this.inventoryContents[i] = null;
        }
    }
}
