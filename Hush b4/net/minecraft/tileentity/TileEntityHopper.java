// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.block.BlockChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import com.google.common.base.Predicate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.AxisAlignedBB;
import java.util.List;
import net.minecraft.world.World;
import java.util.Iterator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.inventory.IInventory;
import net.minecraft.block.BlockHopper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;

public class TileEntityHopper extends TileEntityLockable implements IHopper, ITickable
{
    private ItemStack[] inventory;
    private String customName;
    private int transferCooldown;
    
    public TileEntityHopper() {
        this.inventory = new ItemStack[5];
        this.transferCooldown = -1;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        final NBTTagList nbttaglist = compound.getTagList("Items", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];
        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
        this.transferCooldown = compound.getInteger("TransferCooldown");
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            final int j = nbttagcompound.getByte("Slot");
            if (j >= 0 && j < this.inventory.length) {
                this.inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
        }
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        final NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.inventory.length; ++i) {
            if (this.inventory[i] != null) {
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                this.inventory[i].writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }
        compound.setTag("Items", nbttaglist);
        compound.setInteger("TransferCooldown", this.transferCooldown);
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }
    }
    
    @Override
    public void markDirty() {
        super.markDirty();
    }
    
    @Override
    public int getSizeInventory() {
        return this.inventory.length;
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
        return this.inventory[index];
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        if (this.inventory[index] == null) {
            return null;
        }
        if (this.inventory[index].stackSize <= count) {
            final ItemStack itemstack1 = this.inventory[index];
            this.inventory[index] = null;
            return itemstack1;
        }
        final ItemStack itemstack2 = this.inventory[index].splitStack(count);
        if (this.inventory[index].stackSize == 0) {
            this.inventory[index] = null;
        }
        return itemstack2;
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        if (this.inventory[index] != null) {
            final ItemStack itemstack = this.inventory[index];
            this.inventory[index] = null;
            return itemstack;
        }
        return null;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        this.inventory[index] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.hopper";
    }
    
    @Override
    public boolean hasCustomName() {
        return this.customName != null && this.customName.length() > 0;
    }
    
    public void setCustomName(final String customNameIn) {
        this.customName = customNameIn;
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
    public void update() {
        if (this.worldObj != null && !this.worldObj.isRemote) {
            --this.transferCooldown;
            if (!this.isOnTransferCooldown()) {
                this.setTransferCooldown(0);
                this.updateHopper();
            }
        }
    }
    
    public boolean updateHopper() {
        if (this.worldObj != null && !this.worldObj.isRemote) {
            if (!this.isOnTransferCooldown() && BlockHopper.isEnabled(this.getBlockMetadata())) {
                boolean flag = false;
                if (!this.isEmpty()) {
                    flag = this.transferItemsOut();
                }
                if (!this.isFull()) {
                    flag = (captureDroppedItems(this) || flag);
                }
                if (flag) {
                    this.setTransferCooldown(8);
                    this.markDirty();
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    
    private boolean isEmpty() {
        ItemStack[] inventory;
        for (int length = (inventory = this.inventory).length, i = 0; i < length; ++i) {
            final ItemStack itemstack = inventory[i];
            if (itemstack != null) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isFull() {
        ItemStack[] inventory;
        for (int length = (inventory = this.inventory).length, i = 0; i < length; ++i) {
            final ItemStack itemstack = inventory[i];
            if (itemstack == null || itemstack.stackSize != itemstack.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }
    
    private boolean transferItemsOut() {
        final IInventory iinventory = this.getInventoryForHopperTransfer();
        if (iinventory == null) {
            return false;
        }
        final EnumFacing enumfacing = BlockHopper.getFacing(this.getBlockMetadata()).getOpposite();
        if (this.isInventoryFull(iinventory, enumfacing)) {
            return false;
        }
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            if (this.getStackInSlot(i) != null) {
                final ItemStack itemstack = this.getStackInSlot(i).copy();
                final ItemStack itemstack2 = putStackInInventoryAllSlots(iinventory, this.decrStackSize(i, 1), enumfacing);
                if (itemstack2 == null || itemstack2.stackSize == 0) {
                    iinventory.markDirty();
                    return true;
                }
                this.setInventorySlotContents(i, itemstack);
            }
        }
        return false;
    }
    
    private boolean isInventoryFull(final IInventory inventoryIn, final EnumFacing side) {
        if (inventoryIn instanceof ISidedInventory) {
            final ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            final int[] aint = isidedinventory.getSlotsForFace(side);
            for (int k = 0; k < aint.length; ++k) {
                final ItemStack itemstack1 = isidedinventory.getStackInSlot(aint[k]);
                if (itemstack1 == null || itemstack1.stackSize != itemstack1.getMaxStackSize()) {
                    return false;
                }
            }
        }
        else {
            for (int i = inventoryIn.getSizeInventory(), j = 0; j < i; ++j) {
                final ItemStack itemstack2 = inventoryIn.getStackInSlot(j);
                if (itemstack2 == null || itemstack2.stackSize != itemstack2.getMaxStackSize()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private static boolean isInventoryEmpty(final IInventory inventoryIn, final EnumFacing side) {
        if (inventoryIn instanceof ISidedInventory) {
            final ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            final int[] aint = isidedinventory.getSlotsForFace(side);
            for (int i = 0; i < aint.length; ++i) {
                if (isidedinventory.getStackInSlot(aint[i]) != null) {
                    return false;
                }
            }
        }
        else {
            for (int j = inventoryIn.getSizeInventory(), k = 0; k < j; ++k) {
                if (inventoryIn.getStackInSlot(k) != null) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean captureDroppedItems(final IHopper p_145891_0_) {
        final IInventory iinventory = getHopperInventory(p_145891_0_);
        if (iinventory != null) {
            final EnumFacing enumfacing = EnumFacing.DOWN;
            if (isInventoryEmpty(iinventory, enumfacing)) {
                return false;
            }
            if (iinventory instanceof ISidedInventory) {
                final ISidedInventory isidedinventory = (ISidedInventory)iinventory;
                final int[] aint = isidedinventory.getSlotsForFace(enumfacing);
                for (int i = 0; i < aint.length; ++i) {
                    if (pullItemFromSlot(p_145891_0_, iinventory, aint[i], enumfacing)) {
                        return true;
                    }
                }
            }
            else {
                for (int j = iinventory.getSizeInventory(), k = 0; k < j; ++k) {
                    if (pullItemFromSlot(p_145891_0_, iinventory, k, enumfacing)) {
                        return true;
                    }
                }
            }
        }
        else {
            for (final EntityItem entityitem : func_181556_a(p_145891_0_.getWorld(), p_145891_0_.getXPos(), p_145891_0_.getYPos() + 1.0, p_145891_0_.getZPos())) {
                if (putDropInInventoryAllSlots(p_145891_0_, entityitem)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean pullItemFromSlot(final IHopper hopper, final IInventory inventoryIn, final int index, final EnumFacing direction) {
        final ItemStack itemstack = inventoryIn.getStackInSlot(index);
        if (itemstack != null && canExtractItemFromSlot(inventoryIn, itemstack, index, direction)) {
            final ItemStack itemstack2 = itemstack.copy();
            final ItemStack itemstack3 = putStackInInventoryAllSlots(hopper, inventoryIn.decrStackSize(index, 1), null);
            if (itemstack3 == null || itemstack3.stackSize == 0) {
                inventoryIn.markDirty();
                return true;
            }
            inventoryIn.setInventorySlotContents(index, itemstack2);
        }
        return false;
    }
    
    public static boolean putDropInInventoryAllSlots(final IInventory p_145898_0_, final EntityItem itemIn) {
        boolean flag = false;
        if (itemIn == null) {
            return false;
        }
        final ItemStack itemstack = itemIn.getEntityItem().copy();
        final ItemStack itemstack2 = putStackInInventoryAllSlots(p_145898_0_, itemstack, null);
        if (itemstack2 != null && itemstack2.stackSize != 0) {
            itemIn.setEntityItemStack(itemstack2);
        }
        else {
            flag = true;
            itemIn.setDead();
        }
        return flag;
    }
    
    public static ItemStack putStackInInventoryAllSlots(final IInventory inventoryIn, ItemStack stack, final EnumFacing side) {
        if (inventoryIn instanceof ISidedInventory && side != null) {
            final ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            final int[] aint = isidedinventory.getSlotsForFace(side);
            for (int k = 0; k < aint.length && stack != null; stack = insertStack(inventoryIn, stack, aint[k], side), ++k) {
                if (stack.stackSize <= 0) {
                    break;
                }
            }
        }
        else {
            for (int i = inventoryIn.getSizeInventory(), j = 0; j < i && stack != null && stack.stackSize > 0; stack = insertStack(inventoryIn, stack, j, side), ++j) {}
        }
        if (stack != null && stack.stackSize == 0) {
            stack = null;
        }
        return stack;
    }
    
    private static boolean canInsertItemInSlot(final IInventory inventoryIn, final ItemStack stack, final int index, final EnumFacing side) {
        return inventoryIn.isItemValidForSlot(index, stack) && (!(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canInsertItem(index, stack, side));
    }
    
    private static boolean canExtractItemFromSlot(final IInventory inventoryIn, final ItemStack stack, final int index, final EnumFacing side) {
        return !(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canExtractItem(index, stack, side);
    }
    
    private static ItemStack insertStack(final IInventory inventoryIn, ItemStack stack, final int index, final EnumFacing side) {
        final ItemStack itemstack = inventoryIn.getStackInSlot(index);
        if (canInsertItemInSlot(inventoryIn, stack, index, side)) {
            boolean flag = false;
            if (itemstack == null) {
                inventoryIn.setInventorySlotContents(index, stack);
                stack = null;
                flag = true;
            }
            else if (canCombine(itemstack, stack)) {
                final int i = stack.getMaxStackSize() - itemstack.stackSize;
                final int j = Math.min(stack.stackSize, i);
                final ItemStack itemStack = stack;
                itemStack.stackSize -= j;
                final ItemStack itemStack2 = itemstack;
                itemStack2.stackSize += j;
                flag = (j > 0);
            }
            if (flag) {
                if (inventoryIn instanceof TileEntityHopper) {
                    final TileEntityHopper tileentityhopper = (TileEntityHopper)inventoryIn;
                    if (tileentityhopper.mayTransfer()) {
                        tileentityhopper.setTransferCooldown(8);
                    }
                    inventoryIn.markDirty();
                }
                inventoryIn.markDirty();
            }
        }
        return stack;
    }
    
    private IInventory getInventoryForHopperTransfer() {
        final EnumFacing enumfacing = BlockHopper.getFacing(this.getBlockMetadata());
        return getInventoryAtPosition(this.getWorld(), this.pos.getX() + enumfacing.getFrontOffsetX(), this.pos.getY() + enumfacing.getFrontOffsetY(), this.pos.getZ() + enumfacing.getFrontOffsetZ());
    }
    
    public static IInventory getHopperInventory(final IHopper hopper) {
        return getInventoryAtPosition(hopper.getWorld(), hopper.getXPos(), hopper.getYPos() + 1.0, hopper.getZPos());
    }
    
    public static List<EntityItem> func_181556_a(final World p_181556_0_, final double p_181556_1_, final double p_181556_3_, final double p_181556_5_) {
        return p_181556_0_.getEntitiesWithinAABB((Class<? extends EntityItem>)EntityItem.class, new AxisAlignedBB(p_181556_1_ - 0.5, p_181556_3_ - 0.5, p_181556_5_ - 0.5, p_181556_1_ + 0.5, p_181556_3_ + 0.5, p_181556_5_ + 0.5), (Predicate<? super EntityItem>)EntitySelectors.selectAnything);
    }
    
    public static IInventory getInventoryAtPosition(final World worldIn, final double x, final double y, final double z) {
        IInventory iinventory = null;
        final int i = MathHelper.floor_double(x);
        final int j = MathHelper.floor_double(y);
        final int k = MathHelper.floor_double(z);
        final BlockPos blockpos = new BlockPos(i, j, k);
        final Block block = worldIn.getBlockState(blockpos).getBlock();
        if (block.hasTileEntity()) {
            final TileEntity tileentity = worldIn.getTileEntity(blockpos);
            if (tileentity instanceof IInventory) {
                iinventory = (IInventory)tileentity;
                if (iinventory instanceof TileEntityChest && block instanceof BlockChest) {
                    iinventory = ((BlockChest)block).getLockableContainer(worldIn, blockpos);
                }
            }
        }
        if (iinventory == null) {
            final List<Entity> list = worldIn.getEntitiesInAABBexcluding(null, new AxisAlignedBB(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5), EntitySelectors.selectInventories);
            if (list.size() > 0) {
                iinventory = (IInventory)list.get(worldIn.rand.nextInt(list.size()));
            }
        }
        return iinventory;
    }
    
    private static boolean canCombine(final ItemStack stack1, final ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && stack1.getMetadata() == stack2.getMetadata() && stack1.stackSize <= stack1.getMaxStackSize() && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }
    
    @Override
    public double getXPos() {
        return this.pos.getX() + 0.5;
    }
    
    @Override
    public double getYPos() {
        return this.pos.getY() + 0.5;
    }
    
    @Override
    public double getZPos() {
        return this.pos.getZ() + 0.5;
    }
    
    public void setTransferCooldown(final int ticks) {
        this.transferCooldown = ticks;
    }
    
    public boolean isOnTransferCooldown() {
        return this.transferCooldown > 0;
    }
    
    public boolean mayTransfer() {
        return this.transferCooldown <= 1;
    }
    
    @Override
    public String getGuiID() {
        return "minecraft:hopper";
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        return new ContainerHopper(playerInventory, this, playerIn);
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
        for (int i = 0; i < this.inventory.length; ++i) {
            this.inventory[i] = null;
        }
    }
}
