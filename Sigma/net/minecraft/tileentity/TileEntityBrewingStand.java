package net.minecraft.tileentity;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.BlockBrewingStand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionHelper;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.EnumFacing;

public class TileEntityBrewingStand extends TileEntityLockable implements IUpdatePlayerListBox, ISidedInventory {
    /**
     * an array of the input slot indices
     */
    private static final int[] inputSlots = new int[]{3};

    /**
     * an array of the output slot indices
     */
    private static final int[] outputSlots = new int[]{0, 1, 2};

    /**
     * The ItemStacks currently placed in the slots of the brewing stand
     */
    private ItemStack[] brewingItemStacks = new ItemStack[4];
    private int brewTime;

    /**
     * an integer with each bit specifying whether that slot of the stand
     * contains a potion
     */
    private boolean[] filledSlots;

    /**
     * used to check if the current ingredient has been removed from the brewing
     * stand during brewing
     */
    private Item ingredientID;
    private String field_145942_n;
    private static final String __OBFID = "CL_00000345";

    /**
     * Gets the name of this command sender (usually username, but possibly
     * "Rcon")
     */
    @Override
    public String getName() {
        return hasCustomName() ? field_145942_n : "container.brewing";
    }

    /**
     * Returns true if this thing is named
     */
    @Override
    public boolean hasCustomName() {
        return field_145942_n != null && field_145942_n.length() > 0;
    }

    public void func_145937_a(String p_145937_1_) {
        field_145942_n = p_145937_1_;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory() {
        return brewingItemStacks.length;
    }

    /**
     * Updates the JList with a new model.
     */
    @Override
    public void update() {
        if (brewTime > 0) {
            --brewTime;

            if (brewTime == 0) {
                brewPotions();
                markDirty();
            } else if (!canBrew()) {
                brewTime = 0;
                markDirty();
            } else if (ingredientID != brewingItemStacks[3].getItem()) {
                brewTime = 0;
                markDirty();
            }
        } else if (canBrew()) {
            brewTime = 400;
            ingredientID = brewingItemStacks[3].getItem();
        }

        if (!worldObj.isRemote) {
            boolean[] var1 = func_174902_m();

            if (!Arrays.equals(var1, filledSlots)) {
                filledSlots = var1;
                IBlockState var2 = worldObj.getBlockState(getPos());

                if (!(var2.getBlock() instanceof BlockBrewingStand)) {
                    return;
                }

                for (int var3 = 0; var3 < BlockBrewingStand.BOTTLE_PROPS.length; ++var3) {
                    var2 = var2.withProperty(BlockBrewingStand.BOTTLE_PROPS[var3], Boolean.valueOf(var1[var3]));
                }

                worldObj.setBlockState(pos, var2, 2);
            }
        }
    }

    private boolean canBrew() {
        if (brewingItemStacks[3] != null && brewingItemStacks[3].stackSize > 0) {
            ItemStack var1 = brewingItemStacks[3];

            if (!var1.getItem().isPotionIngredient(var1)) {
                return false;
            } else {
                boolean var2 = false;

                for (int var3 = 0; var3 < 3; ++var3) {
                    if (brewingItemStacks[var3] != null && brewingItemStacks[var3].getItem() == Items.potionitem) {
                        int var4 = brewingItemStacks[var3].getMetadata();
                        int var5 = func_145936_c(var4, var1);

                        if (!ItemPotion.isSplash(var4) && ItemPotion.isSplash(var5)) {
                            var2 = true;
                            break;
                        }

                        List var6 = Items.potionitem.getEffects(var4);
                        List var7 = Items.potionitem.getEffects(var5);

                        if ((var4 <= 0 || var6 != var7) && (var6 == null || !var6.equals(var7) && var7 != null) && var4 != var5) {
                            var2 = true;
                            break;
                        }
                    }
                }

                return var2;
            }
        } else {
            return false;
        }
    }

    private void brewPotions() {
        if (canBrew()) {
            ItemStack var1 = brewingItemStacks[3];

            for (int var2 = 0; var2 < 3; ++var2) {
                if (brewingItemStacks[var2] != null && brewingItemStacks[var2].getItem() == Items.potionitem) {
                    int var3 = brewingItemStacks[var2].getMetadata();
                    int var4 = func_145936_c(var3, var1);
                    List var5 = Items.potionitem.getEffects(var3);
                    List var6 = Items.potionitem.getEffects(var4);

                    if ((var3 <= 0 || var5 != var6) && (var5 == null || !var5.equals(var6) && var6 != null)) {
                        if (var3 != var4) {
                            brewingItemStacks[var2].setItemDamage(var4);
                        }
                    } else if (!ItemPotion.isSplash(var3) && ItemPotion.isSplash(var4)) {
                        brewingItemStacks[var2].setItemDamage(var4);
                    }
                }
            }

            if (var1.getItem().hasContainerItem()) {
                brewingItemStacks[3] = new ItemStack(var1.getItem().getContainerItem());
            } else {
                --brewingItemStacks[3].stackSize;

                if (brewingItemStacks[3].stackSize <= 0) {
                    brewingItemStacks[3] = null;
                }
            }
        }
    }

    private int func_145936_c(int p_145936_1_, ItemStack p_145936_2_) {
        return p_145936_2_ == null ? p_145936_1_ : (p_145936_2_.getItem().isPotionIngredient(p_145936_2_) ? PotionHelper.applyIngredient(p_145936_1_, p_145936_2_.getItem().getPotionEffect(p_145936_2_)) : p_145936_1_);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList var2 = compound.getTagList("Items", 10);
        brewingItemStacks = new ItemStack[getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < brewingItemStacks.length) {
                brewingItemStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        brewTime = compound.getShort("BrewTime");

        if (compound.hasKey("CustomName", 8)) {
            field_145942_n = compound.getString("CustomName");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setShort("BrewTime", (short) brewTime);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < brewingItemStacks.length; ++var3) {
            if (brewingItemStacks[var3] != null) {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                brewingItemStacks[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        compound.setTag("Items", var2);

        if (hasCustomName()) {
            compound.setString("CustomName", field_145942_n);
        }
    }

    /**
     * Returns the stack in slot i
     */
    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return slotIn >= 0 && slotIn < brewingItemStacks.length ? brewingItemStacks[slotIn] : null;
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number
     * (second arg) of items and returns them in a new stack.
     */
    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (index >= 0 && index < brewingItemStacks.length) {
            ItemStack var3 = brewingItemStacks[index];
            brewingItemStacks[index] = null;
            return var3;
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
        if (index >= 0 && index < brewingItemStacks.length) {
            ItemStack var2 = brewingItemStacks[index];
            brewingItemStacks[index] = null;
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
        if (index >= 0 && index < brewingItemStacks.length) {
            brewingItemStacks[index] = stack;
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
    public void openInventory(EntityPlayer playerIn) {
    }

    @Override
    public void closeInventory(EntityPlayer playerIn) {
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring
     * stack size) into the given slot.
     */
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index == 3 ? stack.getItem().isPotionIngredient(stack) : stack.getItem() == Items.potionitem || stack.getItem() == Items.glass_bottle;
    }

    public boolean[] func_174902_m() {
        boolean[] var1 = new boolean[3];

        for (int var2 = 0; var2 < 3; ++var2) {
            if (brewingItemStacks[var2] != null) {
                var1[var2] = true;
            }
        }

        return var1;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return side == EnumFacing.UP ? TileEntityBrewingStand.inputSlots : TileEntityBrewingStand.outputSlots;
    }

    /**
     * Returns true if automation can insert the given item in the given slot
     * from the given side. Args: slot, item, side
     */
    @Override
    public boolean canInsertItem(int slotIn, ItemStack itemStackIn, EnumFacing direction) {
        return isItemValidForSlot(slotIn, itemStackIn);
    }

    /**
     * Returns true if automation can extract the given item in the given slot
     * from the given side. Args: slot, item, side
     */
    @Override
    public boolean canExtractItem(int slotId, ItemStack stack, EnumFacing direction) {
        return true;
    }

    @Override
    public String getGuiID() {
        return "minecraft:brewing_stand";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerBrewingStand(playerInventory, this);
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return brewTime;

            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                brewTime = value;

            default:
        }
    }

    @Override
    public int getFieldCount() {
        return 1;
    }

    @Override
    public void clearInventory() {
        for (int var1 = 0; var1 < brewingItemStacks.length; ++var1) {
            brewingItemStacks[var1] = null;
        }
    }
}
