// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import java.util.Iterator;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ITickable;

public class TileEntityChest extends TileEntityLockable implements ITickable, IInventory
{
    private ItemStack[] chestContents;
    public boolean adjacentChestChecked;
    public TileEntityChest adjacentChestZNeg;
    public TileEntityChest adjacentChestXPos;
    public TileEntityChest adjacentChestXNeg;
    public TileEntityChest adjacentChestZPos;
    public float lidAngle;
    public float prevLidAngle;
    public int numPlayersUsing;
    private int ticksSinceSync;
    private int cachedChestType;
    private String customName;
    
    public TileEntityChest() {
        this.chestContents = new ItemStack[27];
        this.cachedChestType = -1;
    }
    
    public TileEntityChest(final int chestType) {
        this.chestContents = new ItemStack[27];
        this.cachedChestType = chestType;
    }
    
    @Override
    public int getSizeInventory() {
        return 27;
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
        return this.chestContents[index];
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        if (this.chestContents[index] == null) {
            return null;
        }
        if (this.chestContents[index].stackSize <= count) {
            final ItemStack itemstack1 = this.chestContents[index];
            this.chestContents[index] = null;
            this.markDirty();
            return itemstack1;
        }
        final ItemStack itemstack2 = this.chestContents[index].splitStack(count);
        if (this.chestContents[index].stackSize == 0) {
            this.chestContents[index] = null;
        }
        this.markDirty();
        return itemstack2;
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        if (this.chestContents[index] != null) {
            final ItemStack itemstack = this.chestContents[index];
            this.chestContents[index] = null;
            return itemstack;
        }
        return null;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        this.chestContents[index] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.chest";
    }
    
    @Override
    public boolean hasCustomName() {
        return this.customName != null && this.customName.length() > 0;
    }
    
    public void setCustomName(final String name) {
        this.customName = name;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        final NBTTagList nbttaglist = compound.getTagList("Items", 10);
        this.chestContents = new ItemStack[this.getSizeInventory()];
        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            final int j = nbttagcompound.getByte("Slot") & 0xFF;
            if (j >= 0 && j < this.chestContents.length) {
                this.chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
        }
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        final NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.chestContents.length; ++i) {
            if (this.chestContents[i] != null) {
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                this.chestContents[i].writeToNBT(nbttagcompound);
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
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        this.adjacentChestChecked = false;
    }
    
    private void func_174910_a(final TileEntityChest chestTe, final EnumFacing side) {
        if (chestTe.isInvalid()) {
            this.adjacentChestChecked = false;
        }
        else if (this.adjacentChestChecked) {
            switch (side) {
                case NORTH: {
                    if (this.adjacentChestZNeg != chestTe) {
                        this.adjacentChestChecked = false;
                        break;
                    }
                    break;
                }
                case SOUTH: {
                    if (this.adjacentChestZPos != chestTe) {
                        this.adjacentChestChecked = false;
                        break;
                    }
                    break;
                }
                case EAST: {
                    if (this.adjacentChestXPos != chestTe) {
                        this.adjacentChestChecked = false;
                        break;
                    }
                    break;
                }
                case WEST: {
                    if (this.adjacentChestXNeg != chestTe) {
                        this.adjacentChestChecked = false;
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    public void checkForAdjacentChests() {
        if (!this.adjacentChestChecked) {
            this.adjacentChestChecked = true;
            this.adjacentChestXNeg = this.getAdjacentChest(EnumFacing.WEST);
            this.adjacentChestXPos = this.getAdjacentChest(EnumFacing.EAST);
            this.adjacentChestZNeg = this.getAdjacentChest(EnumFacing.NORTH);
            this.adjacentChestZPos = this.getAdjacentChest(EnumFacing.SOUTH);
        }
    }
    
    protected TileEntityChest getAdjacentChest(final EnumFacing side) {
        final BlockPos blockpos = this.pos.offset(side);
        if (this.isChestAt(blockpos)) {
            final TileEntity tileentity = this.worldObj.getTileEntity(blockpos);
            if (tileentity instanceof TileEntityChest) {
                final TileEntityChest tileentitychest = (TileEntityChest)tileentity;
                tileentitychest.func_174910_a(this, side.getOpposite());
                return tileentitychest;
            }
        }
        return null;
    }
    
    private boolean isChestAt(final BlockPos posIn) {
        if (this.worldObj == null) {
            return false;
        }
        final Block block = this.worldObj.getBlockState(posIn).getBlock();
        return block instanceof BlockChest && ((BlockChest)block).chestType == this.getChestType();
    }
    
    @Override
    public void update() {
        this.checkForAdjacentChests();
        final int i = this.pos.getX();
        final int j = this.pos.getY();
        final int k = this.pos.getZ();
        ++this.ticksSinceSync;
        if (!this.worldObj.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0) {
            this.numPlayersUsing = 0;
            final float f = 5.0f;
            for (final EntityPlayer entityplayer : this.worldObj.getEntitiesWithinAABB((Class<? extends EntityPlayer>)EntityPlayer.class, new AxisAlignedBB(i - f, j - f, k - f, i + 1 + f, j + 1 + f, k + 1 + f))) {
                if (entityplayer.openContainer instanceof ContainerChest) {
                    final IInventory iinventory = ((ContainerChest)entityplayer.openContainer).getLowerChestInventory();
                    if (iinventory != this && (!(iinventory instanceof InventoryLargeChest) || !((InventoryLargeChest)iinventory).isPartOfLargeChest(this))) {
                        continue;
                    }
                    ++this.numPlayersUsing;
                }
            }
        }
        this.prevLidAngle = this.lidAngle;
        final float f2 = 0.1f;
        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0f && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
            double d1 = i + 0.5;
            double d2 = k + 0.5;
            if (this.adjacentChestZPos != null) {
                d2 += 0.5;
            }
            if (this.adjacentChestXPos != null) {
                d1 += 0.5;
            }
            this.worldObj.playSoundEffect(d1, j + 0.5, d2, "random.chestopen", 0.5f, this.worldObj.rand.nextFloat() * 0.1f + 0.9f);
        }
        if ((this.numPlayersUsing == 0 && this.lidAngle > 0.0f) || (this.numPlayersUsing > 0 && this.lidAngle < 1.0f)) {
            final float f3 = this.lidAngle;
            if (this.numPlayersUsing > 0) {
                this.lidAngle += f2;
            }
            else {
                this.lidAngle -= f2;
            }
            if (this.lidAngle > 1.0f) {
                this.lidAngle = 1.0f;
            }
            final float f4 = 0.5f;
            if (this.lidAngle < f4 && f3 >= f4 && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
                double d3 = i + 0.5;
                double d4 = k + 0.5;
                if (this.adjacentChestZPos != null) {
                    d4 += 0.5;
                }
                if (this.adjacentChestXPos != null) {
                    d3 += 0.5;
                }
                this.worldObj.playSoundEffect(d3, j + 0.5, d4, "random.chestclosed", 0.5f, this.worldObj.rand.nextFloat() * 0.1f + 0.9f);
            }
            if (this.lidAngle < 0.0f) {
                this.lidAngle = 0.0f;
            }
        }
    }
    
    @Override
    public boolean receiveClientEvent(final int id, final int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        }
        return super.receiveClientEvent(id, type);
    }
    
    @Override
    public void openInventory(final EntityPlayer player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }
            ++this.numPlayersUsing;
            this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.worldObj.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
            this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType());
        }
    }
    
    @Override
    public void closeInventory(final EntityPlayer player) {
        if (!player.isSpectator() && this.getBlockType() instanceof BlockChest) {
            --this.numPlayersUsing;
            this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.worldObj.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
            this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType());
        }
    }
    
    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
        return true;
    }
    
    @Override
    public void invalidate() {
        super.invalidate();
        this.updateContainingBlockInfo();
        this.checkForAdjacentChests();
    }
    
    public int getChestType() {
        if (this.cachedChestType == -1) {
            if (this.worldObj == null || !(this.getBlockType() instanceof BlockChest)) {
                return 0;
            }
            this.cachedChestType = ((BlockChest)this.getBlockType()).chestType;
        }
        return this.cachedChestType;
    }
    
    @Override
    public String getGuiID() {
        return "minecraft:chest";
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        return new ContainerChest(playerInventory, this, playerIn);
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
        for (int i = 0; i < this.chestContents.length; ++i) {
            this.chestContents[i] = null;
        }
    }
}
