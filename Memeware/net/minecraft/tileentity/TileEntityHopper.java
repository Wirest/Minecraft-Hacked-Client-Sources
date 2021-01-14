package net.minecraft.tileentity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHopper;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileEntityHopper extends TileEntityLockable implements IHopper, IUpdatePlayerListBox {
    private ItemStack[] inventory = new ItemStack[5];
    private String customName;
    private int transferCooldown = -1;
    private static final String __OBFID = "CL_00000359";

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList var2 = compound.getTagList("Items", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];

        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }

        this.transferCooldown = compound.getInteger("TransferCooldown");

        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.inventory.length) {
                this.inventory[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.inventory.length; ++var3) {
            if (this.inventory[var3] != null) {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.inventory[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        compound.setTag("Items", var2);
        compound.setInteger("TransferCooldown", this.transferCooldown);

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty() {
        super.markDirty();
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory() {
        return this.inventory.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int slotIn) {
        return this.inventory[slotIn];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int index, int count) {
        if (this.inventory[index] != null) {
            ItemStack var3;

            if (this.inventory[index].stackSize <= count) {
                var3 = this.inventory[index];
                this.inventory[index] = null;
                return var3;
            } else {
                var3 = this.inventory[index].splitStack(count);

                if (this.inventory[index].stackSize == 0) {
                    this.inventory[index] = null;
                }

                return var3;
            }
        } else {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int index) {
        if (this.inventory[index] != null) {
            ItemStack var2 = this.inventory[index];
            this.inventory[index] = null;
            return var2;
        } else {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.inventory[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.hopper";
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName() {
        return this.customName != null && this.customName.length() > 0;
    }

    public void setCustomName(String customNameIn) {
        this.customName = customNameIn;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit() {
        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer playerIn) {
        return this.worldObj.getTileEntity(this.pos) != this ? false : playerIn.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public void openInventory(EntityPlayer playerIn) {
    }

    public void closeInventory(EntityPlayer playerIn) {
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    /**
     * Updates the JList with a new model.
     */
    public void update() {
        if (this.worldObj != null && !this.worldObj.isRemote) {
            --this.transferCooldown;

            if (!this.isOnTransferCooldown()) {
                this.setTransferCooldown(0);
                this.func_145887_i();
            }
        }
    }

    public boolean func_145887_i() {
        if (this.worldObj != null && !this.worldObj.isRemote) {
            if (!this.isOnTransferCooldown() && BlockHopper.getActiveStateFromMetadata(this.getBlockMetadata())) {
                boolean var1 = false;

                if (!this.func_152104_k()) {
                    var1 = this.func_145883_k();
                }

                if (!this.func_152105_l()) {
                    var1 = func_145891_a(this) || var1;
                }

                if (var1) {
                    this.setTransferCooldown(8);
                    this.markDirty();
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    private boolean func_152104_k() {
        ItemStack[] var1 = this.inventory;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            ItemStack var4 = var1[var3];

            if (var4 != null) {
                return false;
            }
        }

        return true;
    }

    private boolean func_152105_l() {
        ItemStack[] var1 = this.inventory;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            ItemStack var4 = var1[var3];

            if (var4 == null || var4.stackSize != var4.getMaxStackSize()) {
                return false;
            }
        }

        return true;
    }

    private boolean func_145883_k() {
        IInventory var1 = this.func_145895_l();

        if (var1 == null) {
            return false;
        } else {
            EnumFacing var2 = BlockHopper.func_176428_b(this.getBlockMetadata()).getOpposite();

            if (this.func_174919_a(var1, var2)) {
                return false;
            } else {
                for (int var3 = 0; var3 < this.getSizeInventory(); ++var3) {
                    if (this.getStackInSlot(var3) != null) {
                        ItemStack var4 = this.getStackInSlot(var3).copy();
                        ItemStack var5 = func_174918_a(var1, this.decrStackSize(var3, 1), var2);

                        if (var5 == null || var5.stackSize == 0) {
                            var1.markDirty();
                            return true;
                        }

                        this.setInventorySlotContents(var3, var4);
                    }
                }

                return false;
            }
        }
    }

    private boolean func_174919_a(IInventory p_174919_1_, EnumFacing p_174919_2_) {
        if (p_174919_1_ instanceof ISidedInventory) {
            ISidedInventory var3 = (ISidedInventory) p_174919_1_;
            int[] var4 = var3.getSlotsForFace(p_174919_2_);

            for (int var5 = 0; var5 < var4.length; ++var5) {
                ItemStack var6 = var3.getStackInSlot(var4[var5]);

                if (var6 == null || var6.stackSize != var6.getMaxStackSize()) {
                    return false;
                }
            }
        } else {
            int var7 = p_174919_1_.getSizeInventory();

            for (int var8 = 0; var8 < var7; ++var8) {
                ItemStack var9 = p_174919_1_.getStackInSlot(var8);

                if (var9 == null || var9.stackSize != var9.getMaxStackSize()) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean func_174917_b(IInventory p_174917_0_, EnumFacing p_174917_1_) {
        if (p_174917_0_ instanceof ISidedInventory) {
            ISidedInventory var2 = (ISidedInventory) p_174917_0_;
            int[] var3 = var2.getSlotsForFace(p_174917_1_);

            for (int var4 = 0; var4 < var3.length; ++var4) {
                if (var2.getStackInSlot(var3[var4]) != null) {
                    return false;
                }
            }
        } else {
            int var5 = p_174917_0_.getSizeInventory();

            for (int var6 = 0; var6 < var5; ++var6) {
                if (p_174917_0_.getStackInSlot(var6) != null) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean func_145891_a(IHopper p_145891_0_) {
        IInventory var1 = func_145884_b(p_145891_0_);

        if (var1 != null) {
            EnumFacing var2 = EnumFacing.DOWN;

            if (func_174917_b(var1, var2)) {
                return false;
            }

            if (var1 instanceof ISidedInventory) {
                ISidedInventory var3 = (ISidedInventory) var1;
                int[] var4 = var3.getSlotsForFace(var2);

                for (int var5 = 0; var5 < var4.length; ++var5) {
                    if (func_174915_a(p_145891_0_, var1, var4[var5], var2)) {
                        return true;
                    }
                }
            } else {
                int var7 = var1.getSizeInventory();

                for (int var8 = 0; var8 < var7; ++var8) {
                    if (func_174915_a(p_145891_0_, var1, var8, var2)) {
                        return true;
                    }
                }
            }
        } else {
            EntityItem var6 = func_145897_a(p_145891_0_.getWorld(), p_145891_0_.getXPos(), p_145891_0_.getYPos() + 1.0D, p_145891_0_.getZPos());

            if (var6 != null) {
                return func_145898_a(p_145891_0_, var6);
            }
        }

        return false;
    }

    private static boolean func_174915_a(IHopper p_174915_0_, IInventory p_174915_1_, int p_174915_2_, EnumFacing p_174915_3_) {
        ItemStack var4 = p_174915_1_.getStackInSlot(p_174915_2_);

        if (var4 != null && func_174921_b(p_174915_1_, var4, p_174915_2_, p_174915_3_)) {
            ItemStack var5 = var4.copy();
            ItemStack var6 = func_174918_a(p_174915_0_, p_174915_1_.decrStackSize(p_174915_2_, 1), (EnumFacing) null);

            if (var6 == null || var6.stackSize == 0) {
                p_174915_1_.markDirty();
                return true;
            }

            p_174915_1_.setInventorySlotContents(p_174915_2_, var5);
        }

        return false;
    }

    public static boolean func_145898_a(IInventory p_145898_0_, EntityItem p_145898_1_) {
        boolean var2 = false;

        if (p_145898_1_ == null) {
            return false;
        } else {
            ItemStack var3 = p_145898_1_.getEntityItem().copy();
            ItemStack var4 = func_174918_a(p_145898_0_, var3, (EnumFacing) null);

            if (var4 != null && var4.stackSize != 0) {
                p_145898_1_.setEntityItemStack(var4);
            } else {
                var2 = true;
                p_145898_1_.setDead();
            }

            return var2;
        }
    }

    public static ItemStack func_174918_a(IInventory p_174918_0_, ItemStack p_174918_1_, EnumFacing p_174918_2_) {
        if (p_174918_0_ instanceof ISidedInventory && p_174918_2_ != null) {
            ISidedInventory var6 = (ISidedInventory) p_174918_0_;
            int[] var7 = var6.getSlotsForFace(p_174918_2_);

            for (int var5 = 0; var5 < var7.length && p_174918_1_ != null && p_174918_1_.stackSize > 0; ++var5) {
                p_174918_1_ = func_174916_c(p_174918_0_, p_174918_1_, var7[var5], p_174918_2_);
            }
        } else {
            int var3 = p_174918_0_.getSizeInventory();

            for (int var4 = 0; var4 < var3 && p_174918_1_ != null && p_174918_1_.stackSize > 0; ++var4) {
                p_174918_1_ = func_174916_c(p_174918_0_, p_174918_1_, var4, p_174918_2_);
            }
        }

        if (p_174918_1_ != null && p_174918_1_.stackSize == 0) {
            p_174918_1_ = null;
        }

        return p_174918_1_;
    }

    private static boolean func_174920_a(IInventory p_174920_0_, ItemStack p_174920_1_, int p_174920_2_, EnumFacing p_174920_3_) {
        return !p_174920_0_.isItemValidForSlot(p_174920_2_, p_174920_1_) ? false : !(p_174920_0_ instanceof ISidedInventory) || ((ISidedInventory) p_174920_0_).canInsertItem(p_174920_2_, p_174920_1_, p_174920_3_);
    }

    private static boolean func_174921_b(IInventory p_174921_0_, ItemStack p_174921_1_, int p_174921_2_, EnumFacing p_174921_3_) {
        return !(p_174921_0_ instanceof ISidedInventory) || ((ISidedInventory) p_174921_0_).canExtractItem(p_174921_2_, p_174921_1_, p_174921_3_);
    }

    private static ItemStack func_174916_c(IInventory p_174916_0_, ItemStack p_174916_1_, int p_174916_2_, EnumFacing p_174916_3_) {
        ItemStack var4 = p_174916_0_.getStackInSlot(p_174916_2_);

        if (func_174920_a(p_174916_0_, p_174916_1_, p_174916_2_, p_174916_3_)) {
            boolean var5 = false;

            if (var4 == null) {
                p_174916_0_.setInventorySlotContents(p_174916_2_, p_174916_1_);
                p_174916_1_ = null;
                var5 = true;
            } else if (canCombine(var4, p_174916_1_)) {
                int var6 = p_174916_1_.getMaxStackSize() - var4.stackSize;
                int var7 = Math.min(p_174916_1_.stackSize, var6);
                p_174916_1_.stackSize -= var7;
                var4.stackSize += var7;
                var5 = var7 > 0;
            }

            if (var5) {
                if (p_174916_0_ instanceof TileEntityHopper) {
                    TileEntityHopper var8 = (TileEntityHopper) p_174916_0_;

                    if (var8.mayTransfer()) {
                        var8.setTransferCooldown(8);
                    }

                    p_174916_0_.markDirty();
                }

                p_174916_0_.markDirty();
            }
        }

        return p_174916_1_;
    }

    private IInventory func_145895_l() {
        EnumFacing var1 = BlockHopper.func_176428_b(this.getBlockMetadata());
        return func_145893_b(this.getWorld(), (double) (this.pos.getX() + var1.getFrontOffsetX()), (double) (this.pos.getY() + var1.getFrontOffsetY()), (double) (this.pos.getZ() + var1.getFrontOffsetZ()));
    }

    public static IInventory func_145884_b(IHopper p_145884_0_) {
        return func_145893_b(p_145884_0_.getWorld(), p_145884_0_.getXPos(), p_145884_0_.getYPos() + 1.0D, p_145884_0_.getZPos());
    }

    public static EntityItem func_145897_a(World worldIn, double p_145897_1_, double p_145897_3_, double p_145897_5_) {
        List var7 = worldIn.func_175647_a(EntityItem.class, new AxisAlignedBB(p_145897_1_, p_145897_3_, p_145897_5_, p_145897_1_ + 1.0D, p_145897_3_ + 1.0D, p_145897_5_ + 1.0D), IEntitySelector.selectAnything);
        return var7.size() > 0 ? (EntityItem) var7.get(0) : null;
    }

    public static IInventory func_145893_b(World worldIn, double p_145893_1_, double p_145893_3_, double p_145893_5_) {
        Object var7 = null;
        int var8 = MathHelper.floor_double(p_145893_1_);
        int var9 = MathHelper.floor_double(p_145893_3_);
        int var10 = MathHelper.floor_double(p_145893_5_);
        BlockPos var11 = new BlockPos(var8, var9, var10);
        TileEntity var12 = worldIn.getTileEntity(new BlockPos(var8, var9, var10));

        if (var12 instanceof IInventory) {
            var7 = (IInventory) var12;

            if (var7 instanceof TileEntityChest) {
                Block var13 = worldIn.getBlockState(new BlockPos(var8, var9, var10)).getBlock();

                if (var13 instanceof BlockChest) {
                    var7 = ((BlockChest) var13).getLockableContainer(worldIn, var11);
                }
            }
        }

        if (var7 == null) {
            List var14 = worldIn.func_175674_a((Entity) null, new AxisAlignedBB(p_145893_1_, p_145893_3_, p_145893_5_, p_145893_1_ + 1.0D, p_145893_3_ + 1.0D, p_145893_5_ + 1.0D), IEntitySelector.selectInventories);

            if (var14.size() > 0) {
                var7 = (IInventory) var14.get(worldIn.rand.nextInt(var14.size()));
            }
        }

        return (IInventory) var7;
    }

    private static boolean canCombine(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() != stack2.getItem() ? false : (stack1.getMetadata() != stack2.getMetadata() ? false : (stack1.stackSize > stack1.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(stack1, stack2)));
    }

    /**
     * Gets the world X position for this hopper entity.
     */
    public double getXPos() {
        return (double) this.pos.getX();
    }

    /**
     * Gets the world Y position for this hopper entity.
     */
    public double getYPos() {
        return (double) this.pos.getY();
    }

    /**
     * Gets the world Z position for this hopper entity.
     */
    public double getZPos() {
        return (double) this.pos.getZ();
    }

    public void setTransferCooldown(int ticks) {
        this.transferCooldown = ticks;
    }

    public boolean isOnTransferCooldown() {
        return this.transferCooldown > 0;
    }

    public boolean mayTransfer() {
        return this.transferCooldown <= 1;
    }

    public String getGuiID() {
        return "minecraft:hopper";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerHopper(playerInventory, this, playerIn);
    }

    public int getField(int id) {
        return 0;
    }

    public void setField(int id, int value) {
    }

    public int getFieldCount() {
        return 0;
    }

    public void clearInventory() {
        for (int var1 = 0; var1 < this.inventory.length; ++var1) {
            this.inventory[var1] = null;
        }
    }
}
