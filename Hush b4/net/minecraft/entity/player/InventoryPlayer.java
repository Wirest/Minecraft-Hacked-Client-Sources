// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.player;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.block.Block;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.IInventory;

public class InventoryPlayer implements IInventory
{
    public ItemStack[] mainInventory;
    public ItemStack[] armorInventory;
    public int currentItem;
    public EntityPlayer player;
    private ItemStack itemStack;
    public boolean inventoryChanged;
    
    public InventoryPlayer(final EntityPlayer playerIn) {
        this.mainInventory = new ItemStack[36];
        this.armorInventory = new ItemStack[4];
        this.player = playerIn;
    }
    
    public ItemStack getCurrentItem() {
        return (this.currentItem < 9 && this.currentItem >= 0) ? this.mainInventory[this.currentItem] : null;
    }
    
    public static int getHotbarSize() {
        return 9;
    }
    
    private int getInventorySlotContainItem(final Item itemIn) {
        for (int i = 0; i < this.mainInventory.length; ++i) {
            if (this.mainInventory[i] != null && this.mainInventory[i].getItem() == itemIn) {
                return i;
            }
        }
        return -1;
    }
    
    private int getInventorySlotContainItemAndDamage(final Item itemIn, final int p_146024_2_) {
        for (int i = 0; i < this.mainInventory.length; ++i) {
            if (this.mainInventory[i] != null && this.mainInventory[i].getItem() == itemIn && this.mainInventory[i].getMetadata() == p_146024_2_) {
                return i;
            }
        }
        return -1;
    }
    
    private int storeItemStack(final ItemStack itemStackIn) {
        for (int i = 0; i < this.mainInventory.length; ++i) {
            if (this.mainInventory[i] != null && this.mainInventory[i].getItem() == itemStackIn.getItem() && this.mainInventory[i].isStackable() && this.mainInventory[i].stackSize < this.mainInventory[i].getMaxStackSize() && this.mainInventory[i].stackSize < this.getInventoryStackLimit() && (!this.mainInventory[i].getHasSubtypes() || this.mainInventory[i].getMetadata() == itemStackIn.getMetadata()) && ItemStack.areItemStackTagsEqual(this.mainInventory[i], itemStackIn)) {
                return i;
            }
        }
        return -1;
    }
    
    public int getFirstEmptyStack() {
        for (int i = 0; i < this.mainInventory.length; ++i) {
            if (this.mainInventory[i] == null) {
                return i;
            }
        }
        return -1;
    }
    
    public void setCurrentItem(final Item itemIn, final int p_146030_2_, final boolean p_146030_3_, final boolean p_146030_4_) {
        final ItemStack itemstack = this.getCurrentItem();
        final int i = p_146030_3_ ? this.getInventorySlotContainItemAndDamage(itemIn, p_146030_2_) : this.getInventorySlotContainItem(itemIn);
        if (i >= 0 && i < 9) {
            this.currentItem = i;
        }
        else if (p_146030_4_ && itemIn != null) {
            final int j = this.getFirstEmptyStack();
            if (j >= 0 && j < 9) {
                this.currentItem = j;
            }
            if (itemstack == null || !itemstack.isItemEnchantable() || this.getInventorySlotContainItemAndDamage(itemstack.getItem(), itemstack.getItemDamage()) != this.currentItem) {
                final int k = this.getInventorySlotContainItemAndDamage(itemIn, p_146030_2_);
                int l;
                if (k >= 0) {
                    l = this.mainInventory[k].stackSize;
                    this.mainInventory[k] = this.mainInventory[this.currentItem];
                }
                else {
                    l = 1;
                }
                this.mainInventory[this.currentItem] = new ItemStack(itemIn, l, p_146030_2_);
            }
        }
    }
    
    public void changeCurrentItem(int p_70453_1_) {
        if (p_70453_1_ > 0) {
            p_70453_1_ = 1;
        }
        if (p_70453_1_ < 0) {
            p_70453_1_ = -1;
        }
        this.currentItem -= p_70453_1_;
        while (this.currentItem < 0) {
            this.currentItem += 9;
        }
        while (this.currentItem >= 9) {
            this.currentItem -= 9;
        }
    }
    
    public int clearMatchingItems(final Item itemIn, final int metadataIn, final int removeCount, final NBTTagCompound itemNBT) {
        int i = 0;
        for (int j = 0; j < this.mainInventory.length; ++j) {
            final ItemStack itemstack = this.mainInventory[j];
            if (itemstack != null && (itemIn == null || itemstack.getItem() == itemIn) && (metadataIn <= -1 || itemstack.getMetadata() == metadataIn) && (itemNBT == null || NBTUtil.func_181123_a(itemNBT, itemstack.getTagCompound(), true))) {
                final int k = (removeCount <= 0) ? itemstack.stackSize : Math.min(removeCount - i, itemstack.stackSize);
                i += k;
                if (removeCount != 0) {
                    final ItemStack itemStack = this.mainInventory[j];
                    itemStack.stackSize -= k;
                    if (this.mainInventory[j].stackSize == 0) {
                        this.mainInventory[j] = null;
                    }
                    if (removeCount > 0 && i >= removeCount) {
                        return i;
                    }
                }
            }
        }
        for (int l = 0; l < this.armorInventory.length; ++l) {
            final ItemStack itemstack2 = this.armorInventory[l];
            if (itemstack2 != null && (itemIn == null || itemstack2.getItem() == itemIn) && (metadataIn <= -1 || itemstack2.getMetadata() == metadataIn) && (itemNBT == null || NBTUtil.func_181123_a(itemNBT, itemstack2.getTagCompound(), false))) {
                final int j2 = (removeCount <= 0) ? itemstack2.stackSize : Math.min(removeCount - i, itemstack2.stackSize);
                i += j2;
                if (removeCount != 0) {
                    final ItemStack itemStack2 = this.armorInventory[l];
                    itemStack2.stackSize -= j2;
                    if (this.armorInventory[l].stackSize == 0) {
                        this.armorInventory[l] = null;
                    }
                    if (removeCount > 0 && i >= removeCount) {
                        return i;
                    }
                }
            }
        }
        if (this.itemStack != null) {
            if (itemIn != null && this.itemStack.getItem() != itemIn) {
                return i;
            }
            if (metadataIn > -1 && this.itemStack.getMetadata() != metadataIn) {
                return i;
            }
            if (itemNBT != null && !NBTUtil.func_181123_a(itemNBT, this.itemStack.getTagCompound(), false)) {
                return i;
            }
            final int i2 = (removeCount <= 0) ? this.itemStack.stackSize : Math.min(removeCount - i, this.itemStack.stackSize);
            i += i2;
            if (removeCount != 0) {
                final ItemStack itemStack3 = this.itemStack;
                itemStack3.stackSize -= i2;
                if (this.itemStack.stackSize == 0) {
                    this.itemStack = null;
                }
                if (removeCount > 0 && i >= removeCount) {
                    return i;
                }
            }
        }
        return i;
    }
    
    private int storePartialItemStack(final ItemStack itemStackIn) {
        final Item item = itemStackIn.getItem();
        int i = itemStackIn.stackSize;
        int j = this.storeItemStack(itemStackIn);
        if (j < 0) {
            j = this.getFirstEmptyStack();
        }
        if (j < 0) {
            return i;
        }
        if (this.mainInventory[j] == null) {
            this.mainInventory[j] = new ItemStack(item, 0, itemStackIn.getMetadata());
            if (itemStackIn.hasTagCompound()) {
                this.mainInventory[j].setTagCompound((NBTTagCompound)itemStackIn.getTagCompound().copy());
            }
        }
        int k;
        if ((k = i) > this.mainInventory[j].getMaxStackSize() - this.mainInventory[j].stackSize) {
            k = this.mainInventory[j].getMaxStackSize() - this.mainInventory[j].stackSize;
        }
        if (k > this.getInventoryStackLimit() - this.mainInventory[j].stackSize) {
            k = this.getInventoryStackLimit() - this.mainInventory[j].stackSize;
        }
        if (k == 0) {
            return i;
        }
        i -= k;
        final ItemStack itemStack = this.mainInventory[j];
        itemStack.stackSize += k;
        this.mainInventory[j].animationsToGo = 5;
        return i;
    }
    
    public void decrementAnimations() {
        for (int i = 0; i < this.mainInventory.length; ++i) {
            if (this.mainInventory[i] != null) {
                this.mainInventory[i].updateAnimation(this.player.worldObj, this.player, i, this.currentItem == i);
            }
        }
    }
    
    public boolean consumeInventoryItem(final Item itemIn) {
        final int i = this.getInventorySlotContainItem(itemIn);
        if (i < 0) {
            return false;
        }
        final ItemStack itemStack = this.mainInventory[i];
        if (--itemStack.stackSize <= 0) {
            this.mainInventory[i] = null;
        }
        return true;
    }
    
    public boolean hasItem(final Item itemIn) {
        final int i = this.getInventorySlotContainItem(itemIn);
        return i >= 0;
    }
    
    public boolean addItemStackToInventory(final ItemStack itemStackIn) {
        if (itemStackIn != null && itemStackIn.stackSize != 0 && itemStackIn.getItem() != null) {
            try {
                if (itemStackIn.isItemDamaged()) {
                    final int j = this.getFirstEmptyStack();
                    if (j >= 0) {
                        this.mainInventory[j] = ItemStack.copyItemStack(itemStackIn);
                        this.mainInventory[j].animationsToGo = 5;
                        itemStackIn.stackSize = 0;
                        return true;
                    }
                    if (this.player.capabilities.isCreativeMode) {
                        itemStackIn.stackSize = 0;
                        return true;
                    }
                    return false;
                }
                else {
                    int i;
                    do {
                        i = itemStackIn.stackSize;
                        itemStackIn.stackSize = this.storePartialItemStack(itemStackIn);
                    } while (itemStackIn.stackSize > 0 && itemStackIn.stackSize < i);
                    if (itemStackIn.stackSize == i && this.player.capabilities.isCreativeMode) {
                        itemStackIn.stackSize = 0;
                        return true;
                    }
                    return itemStackIn.stackSize < i;
                }
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding item to inventory");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being added");
                crashreportcategory.addCrashSection("Item ID", Item.getIdFromItem(itemStackIn.getItem()));
                crashreportcategory.addCrashSection("Item data", itemStackIn.getMetadata());
                crashreportcategory.addCrashSectionCallable("Item name", new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return itemStackIn.getDisplayName();
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
        return false;
    }
    
    @Override
    public ItemStack decrStackSize(int index, final int count) {
        ItemStack[] aitemstack = this.mainInventory;
        if (index >= this.mainInventory.length) {
            aitemstack = this.armorInventory;
            index -= this.mainInventory.length;
        }
        if (aitemstack[index] == null) {
            return null;
        }
        if (aitemstack[index].stackSize <= count) {
            final ItemStack itemstack1 = aitemstack[index];
            aitemstack[index] = null;
            return itemstack1;
        }
        final ItemStack itemstack2 = aitemstack[index].splitStack(count);
        if (aitemstack[index].stackSize == 0) {
            aitemstack[index] = null;
        }
        return itemstack2;
    }
    
    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack[] aitemstack = this.mainInventory;
        if (index >= this.mainInventory.length) {
            aitemstack = this.armorInventory;
            index -= this.mainInventory.length;
        }
        if (aitemstack[index] != null) {
            final ItemStack itemstack = aitemstack[index];
            aitemstack[index] = null;
            return itemstack;
        }
        return null;
    }
    
    @Override
    public void setInventorySlotContents(int index, final ItemStack stack) {
        ItemStack[] aitemstack = this.mainInventory;
        if (index >= aitemstack.length) {
            index -= aitemstack.length;
            aitemstack = this.armorInventory;
        }
        aitemstack[index] = stack;
    }
    
    public float getStrVsBlock(final Block blockIn) {
        float f = 1.0f;
        if (this.mainInventory[this.currentItem] != null) {
            f *= this.mainInventory[this.currentItem].getStrVsBlock(blockIn);
        }
        return f;
    }
    
    public NBTTagList writeToNBT(final NBTTagList p_70442_1_) {
        for (int i = 0; i < this.mainInventory.length; ++i) {
            if (this.mainInventory[i] != null) {
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                this.mainInventory[i].writeToNBT(nbttagcompound);
                p_70442_1_.appendTag(nbttagcompound);
            }
        }
        for (int j = 0; j < this.armorInventory.length; ++j) {
            if (this.armorInventory[j] != null) {
                final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                nbttagcompound2.setByte("Slot", (byte)(j + 100));
                this.armorInventory[j].writeToNBT(nbttagcompound2);
                p_70442_1_.appendTag(nbttagcompound2);
            }
        }
        return p_70442_1_;
    }
    
    public void readFromNBT(final NBTTagList p_70443_1_) {
        this.mainInventory = new ItemStack[36];
        this.armorInventory = new ItemStack[4];
        for (int i = 0; i < p_70443_1_.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = p_70443_1_.getCompoundTagAt(i);
            final int j = nbttagcompound.getByte("Slot") & 0xFF;
            final ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);
            if (itemstack != null) {
                if (j >= 0 && j < this.mainInventory.length) {
                    this.mainInventory[j] = itemstack;
                }
                if (j >= 100 && j < this.armorInventory.length + 100) {
                    this.armorInventory[j - 100] = itemstack;
                }
            }
        }
    }
    
    @Override
    public int getSizeInventory() {
        return this.mainInventory.length + 4;
    }
    
    @Override
    public ItemStack getStackInSlot(int index) {
        ItemStack[] aitemstack = this.mainInventory;
        if (index >= aitemstack.length) {
            index -= aitemstack.length;
            aitemstack = this.armorInventory;
        }
        return aitemstack[index];
    }
    
    @Override
    public String getName() {
        return "container.inventory";
    }
    
    @Override
    public boolean hasCustomName() {
        return false;
    }
    
    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    public boolean canHeldItemHarvest(final Block blockIn) {
        if (blockIn.getMaterial().isToolNotRequired()) {
            return true;
        }
        final ItemStack itemstack = this.getStackInSlot(this.currentItem);
        return itemstack != null && itemstack.canHarvestBlock(blockIn);
    }
    
    public ItemStack armorItemInSlot(final int p_70440_1_) {
        return this.armorInventory[p_70440_1_];
    }
    
    public int getTotalArmorValue() {
        int i = 0;
        for (int j = 0; j < this.armorInventory.length; ++j) {
            if (this.armorInventory[j] != null && this.armorInventory[j].getItem() instanceof ItemArmor) {
                final int k = ((ItemArmor)this.armorInventory[j].getItem()).damageReduceAmount;
                i += k;
            }
        }
        return i;
    }
    
    public void damageArmor(float damage) {
        damage /= 4.0f;
        if (damage < 1.0f) {
            damage = 1.0f;
        }
        for (int i = 0; i < this.armorInventory.length; ++i) {
            if (this.armorInventory[i] != null && this.armorInventory[i].getItem() instanceof ItemArmor) {
                this.armorInventory[i].damageItem((int)damage, this.player);
                if (this.armorInventory[i].stackSize == 0) {
                    this.armorInventory[i] = null;
                }
            }
        }
    }
    
    public void dropAllItems() {
        for (int i = 0; i < this.mainInventory.length; ++i) {
            if (this.mainInventory[i] != null) {
                this.player.dropItem(this.mainInventory[i], true, false);
                this.mainInventory[i] = null;
            }
        }
        for (int j = 0; j < this.armorInventory.length; ++j) {
            if (this.armorInventory[j] != null) {
                this.player.dropItem(this.armorInventory[j], true, false);
                this.armorInventory[j] = null;
            }
        }
    }
    
    @Override
    public void markDirty() {
        this.inventoryChanged = true;
    }
    
    public void setItemStack(final ItemStack itemStackIn) {
        this.itemStack = itemStackIn;
    }
    
    public ItemStack getItemStack() {
        return this.itemStack;
    }
    
    @Override
    public boolean isUseableByPlayer(final EntityPlayer player) {
        return !this.player.isDead && player.getDistanceSqToEntity(this.player) <= 64.0;
    }
    
    public boolean hasItemStack(final ItemStack itemStackIn) {
        for (int i = 0; i < this.armorInventory.length; ++i) {
            if (this.armorInventory[i] != null && this.armorInventory[i].isItemEqual(itemStackIn)) {
                return true;
            }
        }
        for (int j = 0; j < this.mainInventory.length; ++j) {
            if (this.mainInventory[j] != null && this.mainInventory[j].isItemEqual(itemStackIn)) {
                return true;
            }
        }
        return false;
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
    
    public void copyInventory(final InventoryPlayer playerInventory) {
        for (int i = 0; i < this.mainInventory.length; ++i) {
            this.mainInventory[i] = ItemStack.copyItemStack(playerInventory.mainInventory[i]);
        }
        for (int j = 0; j < this.armorInventory.length; ++j) {
            this.armorInventory[j] = ItemStack.copyItemStack(playerInventory.armorInventory[j]);
        }
        this.currentItem = playerInventory.currentItem;
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
        for (int i = 0; i < this.mainInventory.length; ++i) {
            this.mainInventory[i] = null;
        }
        for (int j = 0; j < this.armorInventory.length; ++j) {
            this.armorInventory[j] = null;
        }
    }
}
