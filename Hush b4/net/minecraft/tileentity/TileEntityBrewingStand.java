// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionEffect;
import java.util.List;
import net.minecraft.item.ItemPotion;
import net.minecraft.init.Items;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockBrewingStand;
import java.util.Arrays;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.util.ITickable;

public class TileEntityBrewingStand extends TileEntityLockable implements ITickable, ISidedInventory
{
    private static final int[] inputSlots;
    private static final int[] outputSlots;
    private ItemStack[] brewingItemStacks;
    private int brewTime;
    private boolean[] filledSlots;
    private Item ingredientID;
    private String customName;
    
    static {
        inputSlots = new int[] { 3 };
        outputSlots = new int[] { 0, 1, 2 };
    }
    
    public TileEntityBrewingStand() {
        this.brewingItemStacks = new ItemStack[4];
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.brewing";
    }
    
    @Override
    public boolean hasCustomName() {
        return this.customName != null && this.customName.length() > 0;
    }
    
    public void setName(final String name) {
        this.customName = name;
    }
    
    @Override
    public int getSizeInventory() {
        return this.brewingItemStacks.length;
    }
    
    @Override
    public void update() {
        if (this.brewTime > 0) {
            --this.brewTime;
            if (this.brewTime == 0) {
                this.brewPotions();
                this.markDirty();
            }
            else if (!this.canBrew()) {
                this.brewTime = 0;
                this.markDirty();
            }
            else if (this.ingredientID != this.brewingItemStacks[3].getItem()) {
                this.brewTime = 0;
                this.markDirty();
            }
        }
        else if (this.canBrew()) {
            this.brewTime = 400;
            this.ingredientID = this.brewingItemStacks[3].getItem();
        }
        if (!this.worldObj.isRemote) {
            final boolean[] aboolean = this.func_174902_m();
            if (!Arrays.equals(aboolean, this.filledSlots)) {
                this.filledSlots = aboolean;
                IBlockState iblockstate = this.worldObj.getBlockState(this.getPos());
                if (!(iblockstate.getBlock() instanceof BlockBrewingStand)) {
                    return;
                }
                for (int i = 0; i < BlockBrewingStand.HAS_BOTTLE.length; ++i) {
                    iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockBrewingStand.HAS_BOTTLE[i], aboolean[i]);
                }
                this.worldObj.setBlockState(this.pos, iblockstate, 2);
            }
        }
    }
    
    private boolean canBrew() {
        if (this.brewingItemStacks[3] == null || this.brewingItemStacks[3].stackSize <= 0) {
            return false;
        }
        final ItemStack itemstack = this.brewingItemStacks[3];
        if (!itemstack.getItem().isPotionIngredient(itemstack)) {
            return false;
        }
        boolean flag = false;
        for (int i = 0; i < 3; ++i) {
            if (this.brewingItemStacks[i] != null && this.brewingItemStacks[i].getItem() == Items.potionitem) {
                final int j = this.brewingItemStacks[i].getMetadata();
                final int k = this.getPotionResult(j, itemstack);
                if (!ItemPotion.isSplash(j) && ItemPotion.isSplash(k)) {
                    flag = true;
                    break;
                }
                final List<PotionEffect> list = Items.potionitem.getEffects(j);
                final List<PotionEffect> list2 = Items.potionitem.getEffects(k);
                if ((j <= 0 || list != list2) && (list == null || (!list.equals(list2) && list2 != null)) && j != k) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }
    
    private void brewPotions() {
        if (this.canBrew()) {
            final ItemStack itemstack = this.brewingItemStacks[3];
            for (int i = 0; i < 3; ++i) {
                if (this.brewingItemStacks[i] != null && this.brewingItemStacks[i].getItem() == Items.potionitem) {
                    final int j = this.brewingItemStacks[i].getMetadata();
                    final int k = this.getPotionResult(j, itemstack);
                    final List<PotionEffect> list = Items.potionitem.getEffects(j);
                    final List<PotionEffect> list2 = Items.potionitem.getEffects(k);
                    if ((j > 0 && list == list2) || (list != null && (list.equals(list2) || list2 == null))) {
                        if (!ItemPotion.isSplash(j) && ItemPotion.isSplash(k)) {
                            this.brewingItemStacks[i].setItemDamage(k);
                        }
                    }
                    else if (j != k) {
                        this.brewingItemStacks[i].setItemDamage(k);
                    }
                }
            }
            if (itemstack.getItem().hasContainerItem()) {
                this.brewingItemStacks[3] = new ItemStack(itemstack.getItem().getContainerItem());
            }
            else {
                final ItemStack itemStack = this.brewingItemStacks[3];
                --itemStack.stackSize;
                if (this.brewingItemStacks[3].stackSize <= 0) {
                    this.brewingItemStacks[3] = null;
                }
            }
        }
    }
    
    private int getPotionResult(final int meta, final ItemStack stack) {
        return (stack == null) ? meta : (stack.getItem().isPotionIngredient(stack) ? PotionHelper.applyIngredient(meta, stack.getItem().getPotionEffect(stack)) : meta);
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        final NBTTagList nbttaglist = compound.getTagList("Items", 10);
        this.brewingItemStacks = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            final int j = nbttagcompound.getByte("Slot");
            if (j >= 0 && j < this.brewingItemStacks.length) {
                this.brewingItemStacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
        }
        this.brewTime = compound.getShort("BrewTime");
        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setShort("BrewTime", (short)this.brewTime);
        final NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.brewingItemStacks.length; ++i) {
            if (this.brewingItemStacks[i] != null) {
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                this.brewingItemStacks[i].writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }
        compound.setTag("Items", nbttaglist);
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
        return (index >= 0 && index < this.brewingItemStacks.length) ? this.brewingItemStacks[index] : null;
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        if (index >= 0 && index < this.brewingItemStacks.length) {
            final ItemStack itemstack = this.brewingItemStacks[index];
            this.brewingItemStacks[index] = null;
            return itemstack;
        }
        return null;
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        if (index >= 0 && index < this.brewingItemStacks.length) {
            final ItemStack itemstack = this.brewingItemStacks[index];
            this.brewingItemStacks[index] = null;
            return itemstack;
        }
        return null;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        if (index >= 0 && index < this.brewingItemStacks.length) {
            this.brewingItemStacks[index] = stack;
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
        return (index == 3) ? stack.getItem().isPotionIngredient(stack) : (stack.getItem() == Items.potionitem || stack.getItem() == Items.glass_bottle);
    }
    
    public boolean[] func_174902_m() {
        final boolean[] aboolean = new boolean[3];
        for (int i = 0; i < 3; ++i) {
            if (this.brewingItemStacks[i] != null) {
                aboolean[i] = true;
            }
        }
        return aboolean;
    }
    
    @Override
    public int[] getSlotsForFace(final EnumFacing side) {
        return (side == EnumFacing.UP) ? TileEntityBrewingStand.inputSlots : TileEntityBrewingStand.outputSlots;
    }
    
    @Override
    public boolean canInsertItem(final int index, final ItemStack itemStackIn, final EnumFacing direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }
    
    @Override
    public boolean canExtractItem(final int index, final ItemStack stack, final EnumFacing direction) {
        return true;
    }
    
    @Override
    public String getGuiID() {
        return "minecraft:brewing_stand";
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        return new ContainerBrewingStand(playerInventory, this);
    }
    
    @Override
    public int getField(final int id) {
        switch (id) {
            case 0: {
                return this.brewTime;
            }
            default: {
                return 0;
            }
        }
    }
    
    @Override
    public void setField(final int id, final int value) {
        switch (id) {
            case 0: {
                this.brewTime = value;
                break;
            }
        }
    }
    
    @Override
    public int getFieldCount() {
        return 1;
    }
    
    @Override
    public void clear() {
        for (int i = 0; i < this.brewingItemStacks.length; ++i) {
            this.brewingItemStacks[i] = null;
        }
    }
}
