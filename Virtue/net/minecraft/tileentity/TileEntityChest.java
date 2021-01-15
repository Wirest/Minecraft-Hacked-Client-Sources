package net.minecraft.tileentity;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class TileEntityChest extends TileEntityLockable implements IUpdatePlayerListBox, IInventory
{
    private ItemStack[] chestContents = new ItemStack[27];

    /** Determines if the check for adjacent chests has taken place. */
    public boolean adjacentChestChecked;

    /** Contains the chest tile located adjacent to this one (if any) */
    public TileEntityChest adjacentChestZNeg;

    /** Contains the chest tile located adjacent to this one (if any) */
    public TileEntityChest adjacentChestXPos;

    /** Contains the chest tile located adjacent to this one (if any) */
    public TileEntityChest adjacentChestXNeg;

    /** Contains the chest tile located adjacent to this one (if any) */
    public TileEntityChest adjacentChestZPos;

    /** The current angle of the lid (between 0 and 1) */
    public float lidAngle;

    /** The angle of the lid last tick */
    public float prevLidAngle;

    /** The number of players currently using this chest */
    public int numPlayersUsing;

    /** Server sync counter (once per 20 ticks) */
    private int ticksSinceSync;
    private int cachedChestType;
    private String customName;
    private static final String __OBFID = "CL_00000346";

    public TileEntityChest()
    {
        this.cachedChestType = -1;
    }

    public TileEntityChest(int p_i2350_1_)
    {
        this.cachedChestType = p_i2350_1_;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 27;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int slotIn)
    {
        return this.chestContents[slotIn];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int index, int count)
    {
        if (this.chestContents[index] != null)
        {
            ItemStack var3;

            if (this.chestContents[index].stackSize <= count)
            {
                var3 = this.chestContents[index];
                this.chestContents[index] = null;
                this.markDirty();
                return var3;
            }
            else
            {
                var3 = this.chestContents[index].splitStack(count);

                if (this.chestContents[index].stackSize == 0)
                {
                    this.chestContents[index] = null;
                }

                this.markDirty();
                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int index)
    {
        if (this.chestContents[index] != null)
        {
            ItemStack var2 = this.chestContents[index];
            this.chestContents[index] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.chestContents[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getName()
    {
        return this.hasCustomName() ? this.customName : "container.chest";
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
        return this.customName != null && this.customName.length() > 0;
    }

    public void setCustomName(String p_145976_1_)
    {
        this.customName = p_145976_1_;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        NBTTagList var2 = compound.getTagList("Items", 10);
        this.chestContents = new ItemStack[this.getSizeInventory()];

        if (compound.hasKey("CustomName", 8))
        {
            this.customName = compound.getString("CustomName");
        }

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            int var5 = var4.getByte("Slot") & 255;

            if (var5 >= 0 && var5 < this.chestContents.length)
            {
                this.chestContents[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.chestContents.length; ++var3)
        {
            if (this.chestContents[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.chestContents[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        compound.setTag("Items", var2);

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.customName);
        }
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer playerIn)
    {
        return this.worldObj.getTileEntity(this.pos) != this ? false : playerIn.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public void updateContainingBlockInfo()
    {
        super.updateContainingBlockInfo();
        this.adjacentChestChecked = false;
    }

    private void func_174910_a(TileEntityChest p_174910_1_, EnumFacing p_174910_2_)
    {
        if (p_174910_1_.isInvalid())
        {
            this.adjacentChestChecked = false;
        }
        else if (this.adjacentChestChecked)
        {
            switch (TileEntityChest.SwitchEnumFacing.field_177366_a[p_174910_2_.ordinal()])
            {
                case 1:
                    if (this.adjacentChestZNeg != p_174910_1_)
                    {
                        this.adjacentChestChecked = false;
                    }

                    break;

                case 2:
                    if (this.adjacentChestZPos != p_174910_1_)
                    {
                        this.adjacentChestChecked = false;
                    }

                    break;

                case 3:
                    if (this.adjacentChestXPos != p_174910_1_)
                    {
                        this.adjacentChestChecked = false;
                    }

                    break;

                case 4:
                    if (this.adjacentChestXNeg != p_174910_1_)
                    {
                        this.adjacentChestChecked = false;
                    }
            }
        }
    }

    /**
     * Performs the check for adjacent chests to determine if this chest is double or not.
     */
    public void checkForAdjacentChests()
    {
        if (!this.adjacentChestChecked)
        {
            this.adjacentChestChecked = true;
            this.adjacentChestXNeg = this.func_174911_a(EnumFacing.WEST);
            this.adjacentChestXPos = this.func_174911_a(EnumFacing.EAST);
            this.adjacentChestZNeg = this.func_174911_a(EnumFacing.NORTH);
            this.adjacentChestZPos = this.func_174911_a(EnumFacing.SOUTH);
        }
    }

    protected TileEntityChest func_174911_a(EnumFacing p_174911_1_)
    {
        BlockPos var2 = this.pos.offset(p_174911_1_);

        if (this.func_174912_b(var2))
        {
            TileEntity var3 = this.worldObj.getTileEntity(var2);

            if (var3 instanceof TileEntityChest)
            {
                TileEntityChest var4 = (TileEntityChest)var3;
                var4.func_174910_a(this, p_174911_1_.getOpposite());
                return var4;
            }
        }

        return null;
    }

    private boolean func_174912_b(BlockPos p_174912_1_)
    {
        if (this.worldObj == null)
        {
            return false;
        }
        else
        {
            Block var2 = this.worldObj.getBlockState(p_174912_1_).getBlock();
            return var2 instanceof BlockChest && ((BlockChest)var2).chestType == this.getChestType();
        }
    }

    /**
     * Updates the JList with a new model.
     */
    public void update()
    {
        this.checkForAdjacentChests();
        int var1 = this.pos.getX();
        int var2 = this.pos.getY();
        int var3 = this.pos.getZ();
        ++this.ticksSinceSync;
        float var4;

        if (!this.worldObj.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + var1 + var2 + var3) % 200 == 0)
        {
            this.numPlayersUsing = 0;
            var4 = 5.0F;
            List var5 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((double)((float)var1 - var4), (double)((float)var2 - var4), (double)((float)var3 - var4), (double)((float)(var1 + 1) + var4), (double)((float)(var2 + 1) + var4), (double)((float)(var3 + 1) + var4)));
            Iterator var6 = var5.iterator();

            while (var6.hasNext())
            {
                EntityPlayer var7 = (EntityPlayer)var6.next();

                if (var7.openContainer instanceof ContainerChest)
                {
                    IInventory var8 = ((ContainerChest)var7.openContainer).getLowerChestInventory();

                    if (var8 == this || var8 instanceof InventoryLargeChest && ((InventoryLargeChest)var8).isPartOfLargeChest(this))
                    {
                        ++this.numPlayersUsing;
                    }
                }
            }
        }

        this.prevLidAngle = this.lidAngle;
        var4 = 0.1F;
        double var14;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null)
        {
            double var11 = (double)var1 + 0.5D;
            var14 = (double)var3 + 0.5D;

            if (this.adjacentChestZPos != null)
            {
                var14 += 0.5D;
            }

            if (this.adjacentChestXPos != null)
            {
                var11 += 0.5D;
            }

            this.worldObj.playSoundEffect(var11, (double)var2 + 0.5D, var14, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
        {
            float var12 = this.lidAngle;

            if (this.numPlayersUsing > 0)
            {
                this.lidAngle += var4;
            }
            else
            {
                this.lidAngle -= var4;
            }

            if (this.lidAngle > 1.0F)
            {
                this.lidAngle = 1.0F;
            }

            float var13 = 0.5F;

            if (this.lidAngle < var13 && var12 >= var13 && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null)
            {
                var14 = (double)var1 + 0.5D;
                double var9 = (double)var3 + 0.5D;

                if (this.adjacentChestZPos != null)
                {
                    var9 += 0.5D;
                }

                if (this.adjacentChestXPos != null)
                {
                    var14 += 0.5D;
                }

                this.worldObj.playSoundEffect(var14, (double)var2 + 0.5D, var9, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F)
            {
                this.lidAngle = 0.0F;
            }
        }
    }

    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            this.numPlayersUsing = type;
            return true;
        }
        else
        {
            return super.receiveClientEvent(id, type);
        }
    }

    public void openInventory(EntityPlayer playerIn)
    {
        if (!playerIn.func_175149_v())
        {
            if (this.numPlayersUsing < 0)
            {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.worldObj.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
            this.worldObj.notifyNeighborsOfStateChange(this.pos.offsetDown(), this.getBlockType());
        }
    }

    public void closeInventory(EntityPlayer playerIn)
    {
        if (!playerIn.func_175149_v() && this.getBlockType() instanceof BlockChest)
        {
            --this.numPlayersUsing;
            this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.worldObj.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
            this.worldObj.notifyNeighborsOfStateChange(this.pos.offsetDown(), this.getBlockType());
        }
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    /**
     * invalidates a tile entity
     */
    public void invalidate()
    {
        super.invalidate();
        this.updateContainingBlockInfo();
        this.checkForAdjacentChests();
    }

    public int getChestType()
    {
        if (this.cachedChestType == -1)
        {
            if (this.worldObj == null || !(this.getBlockType() instanceof BlockChest))
            {
                return 0;
            }

            this.cachedChestType = ((BlockChest)this.getBlockType()).chestType;
        }

        return this.cachedChestType;
    }

    public String getGuiID()
    {
        return "minecraft:chest";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerChest(playerInventory, this, playerIn);
    }

    public int getField(int id)
    {
        return 0;
    }

    public void setField(int id, int value) {}

    public int getFieldCount()
    {
        return 0;
    }

    public void clearInventory()
    {
        for (int var1 = 0; var1 < this.chestContents.length; ++var1)
        {
            this.chestContents[var1] = null;
        }
    }

    static final class SwitchEnumFacing
    {
        static final int[] field_177366_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002041";

        static
        {
            try
            {
                field_177366_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                field_177366_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                field_177366_a[EnumFacing.EAST.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_177366_a[EnumFacing.WEST.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
