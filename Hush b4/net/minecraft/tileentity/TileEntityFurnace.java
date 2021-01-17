// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.Item;
import net.minecraft.block.BlockFurnace;
import net.minecraft.util.MathHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.util.ITickable;

public class TileEntityFurnace extends TileEntityLockable implements ITickable, ISidedInventory
{
    private static final int[] slotsTop;
    private static final int[] slotsBottom;
    private static final int[] slotsSides;
    private ItemStack[] furnaceItemStacks;
    private int furnaceBurnTime;
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;
    private String furnaceCustomName;
    
    static {
        slotsTop = new int[1];
        slotsBottom = new int[] { 2, 1 };
        slotsSides = new int[] { 1 };
    }
    
    public TileEntityFurnace() {
        this.furnaceItemStacks = new ItemStack[3];
    }
    
    @Override
    public int getSizeInventory() {
        return this.furnaceItemStacks.length;
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
        return this.furnaceItemStacks[index];
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        if (this.furnaceItemStacks[index] == null) {
            return null;
        }
        if (this.furnaceItemStacks[index].stackSize <= count) {
            final ItemStack itemstack1 = this.furnaceItemStacks[index];
            this.furnaceItemStacks[index] = null;
            return itemstack1;
        }
        final ItemStack itemstack2 = this.furnaceItemStacks[index].splitStack(count);
        if (this.furnaceItemStacks[index].stackSize == 0) {
            this.furnaceItemStacks[index] = null;
        }
        return itemstack2;
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        if (this.furnaceItemStacks[index] != null) {
            final ItemStack itemstack = this.furnaceItemStacks[index];
            this.furnaceItemStacks[index] = null;
            return itemstack;
        }
        return null;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        final boolean flag = stack != null && stack.isItemEqual(this.furnaceItemStacks[index]) && ItemStack.areItemStackTagsEqual(stack, this.furnaceItemStacks[index]);
        this.furnaceItemStacks[index] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
        if (index == 0 && !flag) {
            this.totalCookTime = this.getCookTime(stack);
            this.cookTime = 0;
            this.markDirty();
        }
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.furnaceCustomName : "container.furnace";
    }
    
    @Override
    public boolean hasCustomName() {
        return this.furnaceCustomName != null && this.furnaceCustomName.length() > 0;
    }
    
    public void setCustomInventoryName(final String p_145951_1_) {
        this.furnaceCustomName = p_145951_1_;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        final NBTTagList nbttaglist = compound.getTagList("Items", 10);
        this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            final int j = nbttagcompound.getByte("Slot");
            if (j >= 0 && j < this.furnaceItemStacks.length) {
                this.furnaceItemStacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
        }
        this.furnaceBurnTime = compound.getShort("BurnTime");
        this.cookTime = compound.getShort("CookTime");
        this.totalCookTime = compound.getShort("CookTimeTotal");
        this.currentItemBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);
        if (compound.hasKey("CustomName", 8)) {
            this.furnaceCustomName = compound.getString("CustomName");
        }
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setShort("BurnTime", (short)this.furnaceBurnTime);
        compound.setShort("CookTime", (short)this.cookTime);
        compound.setShort("CookTimeTotal", (short)this.totalCookTime);
        final NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.furnaceItemStacks.length; ++i) {
            if (this.furnaceItemStacks[i] != null) {
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                this.furnaceItemStacks[i].writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }
        compound.setTag("Items", nbttaglist);
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.furnaceCustomName);
        }
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    public boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }
    
    public static boolean isBurning(final IInventory p_174903_0_) {
        return p_174903_0_.getField(0) > 0;
    }
    
    @Override
    public void update() {
        final boolean flag = this.isBurning();
        boolean flag2 = false;
        if (this.isBurning()) {
            --this.furnaceBurnTime;
        }
        if (!this.worldObj.isRemote) {
            if (this.isBurning() || (this.furnaceItemStacks[1] != null && this.furnaceItemStacks[0] != null)) {
                if (!this.isBurning() && this.canSmelt()) {
                    final int itemBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);
                    this.furnaceBurnTime = itemBurnTime;
                    this.currentItemBurnTime = itemBurnTime;
                    if (this.isBurning()) {
                        flag2 = true;
                        if (this.furnaceItemStacks[1] != null) {
                            final ItemStack itemStack = this.furnaceItemStacks[1];
                            --itemStack.stackSize;
                            if (this.furnaceItemStacks[1].stackSize == 0) {
                                final Item item = this.furnaceItemStacks[1].getItem().getContainerItem();
                                this.furnaceItemStacks[1] = ((item != null) ? new ItemStack(item) : null);
                            }
                        }
                    }
                }
                if (this.isBurning() && this.canSmelt()) {
                    ++this.cookTime;
                    if (this.cookTime == this.totalCookTime) {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime(this.furnaceItemStacks[0]);
                        this.smeltItem();
                        flag2 = true;
                    }
                }
                else {
                    this.cookTime = 0;
                }
            }
            else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp_int(this.cookTime - 2, 0, this.totalCookTime);
            }
            if (flag != this.isBurning()) {
                flag2 = true;
                BlockFurnace.setState(this.isBurning(), this.worldObj, this.pos);
            }
        }
        if (flag2) {
            this.markDirty();
        }
    }
    
    public int getCookTime(final ItemStack stack) {
        return 200;
    }
    
    private boolean canSmelt() {
        if (this.furnaceItemStacks[0] == null) {
            return false;
        }
        final ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks[0]);
        return itemstack != null && (this.furnaceItemStacks[2] == null || (this.furnaceItemStacks[2].isItemEqual(itemstack) && ((this.furnaceItemStacks[2].stackSize < this.getInventoryStackLimit() && this.furnaceItemStacks[2].stackSize < this.furnaceItemStacks[2].getMaxStackSize()) || this.furnaceItemStacks[2].stackSize < itemstack.getMaxStackSize())));
    }
    
    public void smeltItem() {
        if (this.canSmelt()) {
            final ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks[0]);
            if (this.furnaceItemStacks[2] == null) {
                this.furnaceItemStacks[2] = itemstack.copy();
            }
            else if (this.furnaceItemStacks[2].getItem() == itemstack.getItem()) {
                final ItemStack itemStack = this.furnaceItemStacks[2];
                ++itemStack.stackSize;
            }
            if (this.furnaceItemStacks[0].getItem() == Item.getItemFromBlock(Blocks.sponge) && this.furnaceItemStacks[0].getMetadata() == 1 && this.furnaceItemStacks[1] != null && this.furnaceItemStacks[1].getItem() == Items.bucket) {
                this.furnaceItemStacks[1] = new ItemStack(Items.water_bucket);
            }
            final ItemStack itemStack2 = this.furnaceItemStacks[0];
            --itemStack2.stackSize;
            if (this.furnaceItemStacks[0].stackSize <= 0) {
                this.furnaceItemStacks[0] = null;
            }
        }
    }
    
    public static int getItemBurnTime(final ItemStack p_145952_0_) {
        if (p_145952_0_ == null) {
            return 0;
        }
        final Item item = p_145952_0_.getItem();
        if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
            final Block block = Block.getBlockFromItem(item);
            if (block == Blocks.wooden_slab) {
                return 150;
            }
            if (block.getMaterial() == Material.wood) {
                return 300;
            }
            if (block == Blocks.coal_block) {
                return 16000;
            }
        }
        return (item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD")) ? 200 : ((item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD")) ? 200 : ((item instanceof ItemHoe && ((ItemHoe)item).getMaterialName().equals("WOOD")) ? 200 : ((item == Items.stick) ? 100 : ((item == Items.coal) ? 1600 : ((item == Items.lava_bucket) ? 20000 : ((item == Item.getItemFromBlock(Blocks.sapling)) ? 100 : ((item == Items.blaze_rod) ? 2400 : 0)))))));
    }
    
    public static boolean isItemFuel(final ItemStack p_145954_0_) {
        return getItemBurnTime(p_145954_0_) > 0;
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
        return index != 2 && (index != 1 || isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack));
    }
    
    @Override
    public int[] getSlotsForFace(final EnumFacing side) {
        return (side == EnumFacing.DOWN) ? TileEntityFurnace.slotsBottom : ((side == EnumFacing.UP) ? TileEntityFurnace.slotsTop : TileEntityFurnace.slotsSides);
    }
    
    @Override
    public boolean canInsertItem(final int index, final ItemStack itemStackIn, final EnumFacing direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }
    
    @Override
    public boolean canExtractItem(final int index, final ItemStack stack, final EnumFacing direction) {
        if (direction == EnumFacing.DOWN && index == 1) {
            final Item item = stack.getItem();
            if (item != Items.water_bucket && item != Items.bucket) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String getGuiID() {
        return "minecraft:furnace";
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        return new ContainerFurnace(playerInventory, this);
    }
    
    @Override
    public int getField(final int id) {
        switch (id) {
            case 0: {
                return this.furnaceBurnTime;
            }
            case 1: {
                return this.currentItemBurnTime;
            }
            case 2: {
                return this.cookTime;
            }
            case 3: {
                return this.totalCookTime;
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
                this.furnaceBurnTime = value;
                break;
            }
            case 1: {
                this.currentItemBurnTime = value;
                break;
            }
            case 2: {
                this.cookTime = value;
                break;
            }
            case 3: {
                this.totalCookTime = value;
                break;
            }
        }
    }
    
    @Override
    public int getFieldCount() {
        return 4;
    }
    
    @Override
    public void clear() {
        for (int i = 0; i < this.furnaceItemStacks.length; ++i) {
            this.furnaceItemStacks[i] = null;
        }
    }
}
