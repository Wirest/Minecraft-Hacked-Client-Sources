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

public class TileEntityChest extends TileEntityLockable implements IUpdatePlayerListBox, IInventory {
    private ItemStack[] chestContents = new ItemStack[27];

    public boolean isEmpty;

    /**
     * Determines if the check for adjacent chests has taken place.
     */
    public boolean adjacentChestChecked;

    /**
     * Contains the chest tile located adjacent to this one (if any)
     */
    public TileEntityChest adjacentChestZNeg;

    /**
     * Contains the chest tile located adjacent to this one (if any)
     */
    public TileEntityChest adjacentChestXPos;

    /**
     * Contains the chest tile located adjacent to this one (if any)
     */
    public TileEntityChest adjacentChestXNeg;

    /**
     * Contains the chest tile located adjacent to this one (if any)
     */
    public TileEntityChest adjacentChestZPos;

    /**
     * The current angle of the lid (between 0 and 1)
     */
    public float lidAngle;

    /**
     * The angle of the lid last tick
     */
    public float prevLidAngle;

    /**
     * The number of players currently using this chest
     */
    public int numPlayersUsing;

    /**
     * Server sync counter (once per 20 ticks)
     */
    private int ticksSinceSync;
    private int cachedChestType;
    private String customName;
    private static final String __OBFID = "CL_00000346";

    public TileEntityChest() {
        cachedChestType = -1;
    }

    public TileEntityChest(int p_i2350_1_) {
        cachedChestType = p_i2350_1_;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory() {
        return 27;
    }

    /**
     * Returns the stack in slot i
     */
    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return chestContents[slotIn];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number
     * (second arg) of items and returns them in a new stack.
     */
    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (chestContents[index] != null) {
            ItemStack var3;

            if (chestContents[index].stackSize <= count) {
                var3 = chestContents[index];
                chestContents[index] = null;
                markDirty();
                return var3;
            } else {
                var3 = chestContents[index].splitStack(count);

                if (chestContents[index].stackSize == 0) {
                    chestContents[index] = null;
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
        if (chestContents[index] != null) {
            ItemStack var2 = chestContents[index];
            chestContents[index] = null;
            return var2;
        } else {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be
     * crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        chestContents[index] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }

        markDirty();
    }

    /**
     * Gets the name of this command sender (usually username, but possibly
     * "Rcon")
     */
    @Override
    public String getName() {
        return hasCustomName() ? customName : "container.chest";
    }

    /**
     * Returns true if this thing is named
     */
    @Override
    public boolean hasCustomName() {
        return customName != null && customName.length() > 0;
    }

    public void setCustomName(String p_145976_1_) {
        customName = p_145976_1_;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList var2 = compound.getTagList("Items", 10);
        chestContents = new ItemStack[getSizeInventory()];

        if (compound.hasKey("CustomName", 8)) {
            customName = compound.getString("CustomName");
        }

        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            int var5 = var4.getByte("Slot") & 255;

            if (var5 >= 0 && var5 < chestContents.length) {
                chestContents[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < chestContents.length; ++var3) {
            if (chestContents[var3] != null) {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                chestContents[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        compound.setTag("Items", var2);

        if (hasCustomName()) {
            compound.setString("CustomName", customName);
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
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        adjacentChestChecked = false;
    }

    private void func_174910_a(TileEntityChest p_174910_1_, EnumFacing p_174910_2_) {
        if (p_174910_1_.isInvalid()) {
            adjacentChestChecked = false;
        } else if (adjacentChestChecked) {
            switch (TileEntityChest.SwitchEnumFacing.field_177366_a[p_174910_2_.ordinal()]) {
                case 1:
                    if (adjacentChestZNeg != p_174910_1_) {
                        adjacentChestChecked = false;
                    }

                    break;

                case 2:
                    if (adjacentChestZPos != p_174910_1_) {
                        adjacentChestChecked = false;
                    }

                    break;

                case 3:
                    if (adjacentChestXPos != p_174910_1_) {
                        adjacentChestChecked = false;
                    }

                    break;

                case 4:
                    if (adjacentChestXNeg != p_174910_1_) {
                        adjacentChestChecked = false;
                    }
            }
        }
    }

    /**
     * Performs the check for adjacent chests to determine if this chest is
     * double or not.
     */
    public void checkForAdjacentChests() {
        if (!adjacentChestChecked) {
            adjacentChestChecked = true;
            adjacentChestXNeg = func_174911_a(EnumFacing.WEST);
            adjacentChestXPos = func_174911_a(EnumFacing.EAST);
            adjacentChestZNeg = func_174911_a(EnumFacing.NORTH);
            adjacentChestZPos = func_174911_a(EnumFacing.SOUTH);
        }
    }

    protected TileEntityChest func_174911_a(EnumFacing p_174911_1_) {
        BlockPos var2 = pos.offset(p_174911_1_);

        if (func_174912_b(var2)) {
            TileEntity var3 = worldObj.getTileEntity(var2);

            if (var3 instanceof TileEntityChest) {
                TileEntityChest var4 = (TileEntityChest) var3;
                var4.func_174910_a(this, p_174911_1_.getOpposite());
                return var4;
            }
        }

        return null;
    }

    private boolean func_174912_b(BlockPos p_174912_1_) {
        if (worldObj == null) {
            return false;
        } else {
            Block var2 = worldObj.getBlockState(p_174912_1_).getBlock();
            return var2 instanceof BlockChest && ((BlockChest) var2).chestType == getChestType();
        }
    }

    /**
     * Updates the JList with a new model.
     */
    @Override
    public void update() {
        checkForAdjacentChests();
        int var1 = pos.getX();
        int var2 = pos.getY();
        int var3 = pos.getZ();
        ++ticksSinceSync;
        float var4;

        if (!worldObj.isRemote && numPlayersUsing != 0 && (ticksSinceSync + var1 + var2 + var3) % 200 == 0) {
            numPlayersUsing = 0;
            var4 = 5.0F;
            List var5 = worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(var1 - var4, var2 - var4, var3 - var4, var1 + 1 + var4, var2 + 1 + var4, var3 + 1 + var4));
            Iterator var6 = var5.iterator();

            while (var6.hasNext()) {
                EntityPlayer var7 = (EntityPlayer) var6.next();

                if (var7.openContainer instanceof ContainerChest) {
                    IInventory var8 = ((ContainerChest) var7.openContainer).getLowerChestInventory();

                    if (var8 == this || var8 instanceof InventoryLargeChest && ((InventoryLargeChest) var8).isPartOfLargeChest(this)) {
                        ++numPlayersUsing;
                    }
                }
            }
        }

        prevLidAngle = lidAngle;
        var4 = 0.1F;
        double var14;

        if (numPlayersUsing > 0 && lidAngle == 0.0F && adjacentChestZNeg == null && adjacentChestXNeg == null) {
            double var11 = var1 + 0.5D;
            var14 = var3 + 0.5D;

            if (adjacentChestZPos != null) {
                var14 += 0.5D;
            }

            if (adjacentChestXPos != null) {
                var11 += 0.5D;
            }

            worldObj.playSoundEffect(var11, var2 + 0.5D, var14, "random.chestopen", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (numPlayersUsing == 0 && lidAngle > 0.0F || numPlayersUsing > 0 && lidAngle < 1.0F) {
            float var12 = lidAngle;

            if (numPlayersUsing > 0) {
                lidAngle += var4;
            } else {
                lidAngle -= var4;
            }

            if (lidAngle > 1.0F) {
                lidAngle = 1.0F;
            }

            float var13 = 0.5F;

            if (lidAngle < var13 && var12 >= var13 && adjacentChestZNeg == null && adjacentChestXNeg == null) {
                var14 = var1 + 0.5D;
                double var9 = var3 + 0.5D;

                if (adjacentChestZPos != null) {
                    var9 += 0.5D;
                }

                if (adjacentChestXPos != null) {
                    var14 += 0.5D;
                }

                worldObj.playSoundEffect(var14, var2 + 0.5D, var9, "random.chestclosed", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (lidAngle < 0.0F) {
                lidAngle = 0.0F;
            }
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            numPlayersUsing = type;
            return true;
        } else {
            return super.receiveClientEvent(id, type);
        }
    }

    @Override
    public void openInventory(EntityPlayer playerIn) {
        if (!playerIn.func_175149_v()) {
            if (numPlayersUsing < 0) {
                numPlayersUsing = 0;
            }

            ++numPlayersUsing;
            worldObj.addBlockEvent(pos, getBlockType(), 1, numPlayersUsing);
            worldObj.notifyNeighborsOfStateChange(pos, getBlockType());
            worldObj.notifyNeighborsOfStateChange(pos.offsetDown(), getBlockType());
        }
    }

    @Override
    public void closeInventory(EntityPlayer playerIn) {
        if (!playerIn.func_175149_v() && getBlockType() instanceof BlockChest) {
            --numPlayersUsing;
            worldObj.addBlockEvent(pos, getBlockType(), 1, numPlayersUsing);
            worldObj.notifyNeighborsOfStateChange(pos, getBlockType());
            worldObj.notifyNeighborsOfStateChange(pos.offsetDown(), getBlockType());
        }
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring
     * stack size) into the given slot.
     */
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    /**
     * invalidates a tile entity
     */
    @Override
    public void invalidate() {
        super.invalidate();
        updateContainingBlockInfo();
        checkForAdjacentChests();
    }

    public int getChestType() {
        if (cachedChestType == -1) {
            if (worldObj == null || !(getBlockType() instanceof BlockChest)) {
                return 0;
            }

            cachedChestType = ((BlockChest) getBlockType()).chestType;
        }

        return cachedChestType;
    }

    @Override
    public String getGuiID() {
        return "minecraft:chest";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerChest(playerInventory, this, playerIn);
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
        for (int var1 = 0; var1 < chestContents.length; ++var1) {
            chestContents[var1] = null;
        }
    }

    static final class SwitchEnumFacing {
        static final int[] field_177366_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002041";

        static {
            try {
                SwitchEnumFacing.field_177366_a[EnumFacing.NORTH.ordinal()] = 1;
            } catch (NoSuchFieldError var4) {
                ;
            }

            try {
                SwitchEnumFacing.field_177366_a[EnumFacing.SOUTH.ordinal()] = 2;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                SwitchEnumFacing.field_177366_a[EnumFacing.EAST.ordinal()] = 3;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                SwitchEnumFacing.field_177366_a[EnumFacing.WEST.ordinal()] = 4;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
