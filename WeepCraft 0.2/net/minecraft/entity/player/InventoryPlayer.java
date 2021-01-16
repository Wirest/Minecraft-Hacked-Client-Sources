package net.minecraft.entity.player;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class InventoryPlayer implements IInventory
{
    public final NonNullList<ItemStack> mainInventory = NonNullList.<ItemStack>func_191197_a(36, ItemStack.field_190927_a);
    public final NonNullList<ItemStack> armorInventory = NonNullList.<ItemStack>func_191197_a(4, ItemStack.field_190927_a);
    public final NonNullList<ItemStack> offHandInventory = NonNullList.<ItemStack>func_191197_a(1, ItemStack.field_190927_a);
    private final List<NonNullList<ItemStack>> allInventories;

    /** The index of the currently held item (0-8). */
    public int currentItem;

    /** The player whose inventory this is. */
    public EntityPlayer player;
    private ItemStack itemStack;
    private int field_194017_h;

    public InventoryPlayer(EntityPlayer playerIn)
    {
        this.allInventories = Arrays.<NonNullList<ItemStack>>asList(this.mainInventory, this.armorInventory, this.offHandInventory);
        this.itemStack = ItemStack.field_190927_a;
        this.player = playerIn;
    }

    /**
     * Returns the item stack currently held by the player.
     */
    public ItemStack getCurrentItem()
    {
        return isHotbar(this.currentItem) ? (ItemStack)this.mainInventory.get(this.currentItem) : ItemStack.field_190927_a;
    }

    /**
     * Get the size of the player hotbar inventory
     */
    public static int getHotbarSize()
    {
        return 9;
    }

    private boolean canMergeStacks(ItemStack stack1, ItemStack stack2)
    {
        return !stack1.func_190926_b() && this.stackEqualExact(stack1, stack2) && stack1.isStackable() && stack1.func_190916_E() < stack1.getMaxStackSize() && stack1.func_190916_E() < this.getInventoryStackLimit();
    }

    /**
     * Checks item, NBT, and meta if the item is not damageable
     */
    private boolean stackEqualExact(ItemStack stack1, ItemStack stack2)
    {
        return stack1.getItem() == stack2.getItem() && (!stack1.getHasSubtypes() || stack1.getMetadata() == stack2.getMetadata()) && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }

    /**
     * Returns the first item stack that is empty.
     */
    public int getFirstEmptyStack()
    {
        for (int i = 0; i < this.mainInventory.size(); ++i)
        {
            if (((ItemStack)this.mainInventory.get(i)).func_190926_b())
            {
                return i;
            }
        }

        return -1;
    }

    public void setPickedItemStack(ItemStack stack)
    {
        int i = this.getSlotFor(stack);

        if (isHotbar(i))
        {
            this.currentItem = i;
        }
        else
        {
            if (i == -1)
            {
                this.currentItem = this.getBestHotbarSlot();

                if (!((ItemStack)this.mainInventory.get(this.currentItem)).func_190926_b())
                {
                    int j = this.getFirstEmptyStack();

                    if (j != -1)
                    {
                        this.mainInventory.set(j, this.mainInventory.get(this.currentItem));
                    }
                }

                this.mainInventory.set(this.currentItem, stack);
            }
            else
            {
                this.pickItem(i);
            }
        }
    }

    public void pickItem(int index)
    {
        this.currentItem = this.getBestHotbarSlot();
        ItemStack itemstack = this.mainInventory.get(this.currentItem);
        this.mainInventory.set(this.currentItem, this.mainInventory.get(index));
        this.mainInventory.set(index, itemstack);
    }

    public static boolean isHotbar(int index)
    {
        return index >= 0 && index < 9;
    }

    /**
     * Finds the stack or an equivalent one in the main inventory
     */
    public int getSlotFor(ItemStack stack)
    {
        for (int i = 0; i < this.mainInventory.size(); ++i)
        {
            if (!((ItemStack)this.mainInventory.get(i)).func_190926_b() && this.stackEqualExact(stack, this.mainInventory.get(i)))
            {
                return i;
            }
        }

        return -1;
    }

    public int func_194014_c(ItemStack p_194014_1_)
    {
        for (int i = 0; i < this.mainInventory.size(); ++i)
        {
            ItemStack itemstack = this.mainInventory.get(i);

            if (!((ItemStack)this.mainInventory.get(i)).func_190926_b() && this.stackEqualExact(p_194014_1_, this.mainInventory.get(i)) && !((ItemStack)this.mainInventory.get(i)).isItemDamaged() && !itemstack.isItemEnchanted() && !itemstack.hasDisplayName())
            {
                return i;
            }
        }

        return -1;
    }

    public int getBestHotbarSlot()
    {
        for (int i = 0; i < 9; ++i)
        {
            int j = (this.currentItem + i) % 9;

            if (((ItemStack)this.mainInventory.get(j)).func_190926_b())
            {
                return j;
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            int l = (this.currentItem + k) % 9;

            if (!((ItemStack)this.mainInventory.get(l)).isItemEnchanted())
            {
                return l;
            }
        }

        return this.currentItem;
    }

    /**
     * Switch the current item to the next one or the previous one
     */
    public void changeCurrentItem(int direction)
    {
        if (direction > 0)
        {
            direction = 1;
        }

        if (direction < 0)
        {
            direction = -1;
        }

        for (this.currentItem -= direction; this.currentItem < 0; this.currentItem += 9)
        {
            ;
        }

        while (this.currentItem >= 9)
        {
            this.currentItem -= 9;
        }
    }

    /**
     * Removes matching items from the inventory.
     * @param itemIn The item to match, null ignores.
     * @param metadataIn The metadata to match, -1 ignores.
     * @param removeCount The number of items to remove. If less than 1, removes all matching items.
     * @param itemNBT The NBT data to match, null ignores.
     * @return The number of items removed from the inventory.
     */
    public int clearMatchingItems(@Nullable Item itemIn, int metadataIn, int removeCount, @Nullable NBTTagCompound itemNBT)
    {
        int i = 0;

        for (int j = 0; j < this.getSizeInventory(); ++j)
        {
            ItemStack itemstack = this.getStackInSlot(j);

            if (!itemstack.func_190926_b() && (itemIn == null || itemstack.getItem() == itemIn) && (metadataIn <= -1 || itemstack.getMetadata() == metadataIn) && (itemNBT == null || NBTUtil.areNBTEquals(itemNBT, itemstack.getTagCompound(), true)))
            {
                int k = removeCount <= 0 ? itemstack.func_190916_E() : Math.min(removeCount - i, itemstack.func_190916_E());
                i += k;

                if (removeCount != 0)
                {
                    itemstack.func_190918_g(k);

                    if (itemstack.func_190926_b())
                    {
                        this.setInventorySlotContents(j, ItemStack.field_190927_a);
                    }

                    if (removeCount > 0 && i >= removeCount)
                    {
                        return i;
                    }
                }
            }
        }

        if (!this.itemStack.func_190926_b())
        {
            if (itemIn != null && this.itemStack.getItem() != itemIn)
            {
                return i;
            }

            if (metadataIn > -1 && this.itemStack.getMetadata() != metadataIn)
            {
                return i;
            }

            if (itemNBT != null && !NBTUtil.areNBTEquals(itemNBT, this.itemStack.getTagCompound(), true))
            {
                return i;
            }

            int l = removeCount <= 0 ? this.itemStack.func_190916_E() : Math.min(removeCount - i, this.itemStack.func_190916_E());
            i += l;

            if (removeCount != 0)
            {
                this.itemStack.func_190918_g(l);

                if (this.itemStack.func_190926_b())
                {
                    this.itemStack = ItemStack.field_190927_a;
                }

                if (removeCount > 0 && i >= removeCount)
                {
                    return i;
                }
            }
        }

        return i;
    }

    /**
     * This function stores as many items of an ItemStack as possible in a matching slot and returns the quantity of
     * left over items.
     */
    private int storePartialItemStack(ItemStack itemStackIn)
    {
        int i = this.storeItemStack(itemStackIn);

        if (i == -1)
        {
            i = this.getFirstEmptyStack();
        }

        return i == -1 ? itemStackIn.func_190916_E() : this.func_191973_d(i, itemStackIn);
    }

    private int func_191973_d(int p_191973_1_, ItemStack p_191973_2_)
    {
        Item item = p_191973_2_.getItem();
        int i = p_191973_2_.func_190916_E();
        ItemStack itemstack = this.getStackInSlot(p_191973_1_);

        if (itemstack.func_190926_b())
        {
            itemstack = new ItemStack(item, 0, p_191973_2_.getMetadata());

            if (p_191973_2_.hasTagCompound())
            {
                itemstack.setTagCompound(p_191973_2_.getTagCompound().copy());
            }

            this.setInventorySlotContents(p_191973_1_, itemstack);
        }

        int j = i;

        if (i > itemstack.getMaxStackSize() - itemstack.func_190916_E())
        {
            j = itemstack.getMaxStackSize() - itemstack.func_190916_E();
        }

        if (j > this.getInventoryStackLimit() - itemstack.func_190916_E())
        {
            j = this.getInventoryStackLimit() - itemstack.func_190916_E();
        }

        if (j == 0)
        {
            return i;
        }
        else
        {
            i = i - j;
            itemstack.func_190917_f(j);
            itemstack.func_190915_d(5);
            return i;
        }
    }

    /**
     * stores an itemstack in the users inventory
     */
    public int storeItemStack(ItemStack itemStackIn)
    {
        if (this.canMergeStacks(this.getStackInSlot(this.currentItem), itemStackIn))
        {
            return this.currentItem;
        }
        else if (this.canMergeStacks(this.getStackInSlot(40), itemStackIn))
        {
            return 40;
        }
        else
        {
            for (int i = 0; i < this.mainInventory.size(); ++i)
            {
                if (this.canMergeStacks(this.mainInventory.get(i), itemStackIn))
                {
                    return i;
                }
            }

            return -1;
        }
    }

    /**
     * Decrement the number of animations remaining. Only called on client side. This is used to handle the animation of
     * receiving a block.
     */
    public void decrementAnimations()
    {
        for (NonNullList<ItemStack> nonnulllist : this.allInventories)
        {
            for (int i = 0; i < nonnulllist.size(); ++i)
            {
                if (!((ItemStack)nonnulllist.get(i)).func_190926_b())
                {
                    ((ItemStack)nonnulllist.get(i)).updateAnimation(this.player.world, this.player, i, this.currentItem == i);
                }
            }
        }
    }

    /**
     * Adds the item stack to the inventory, returns false if it is impossible.
     */
    public boolean addItemStackToInventory(ItemStack itemStackIn)
    {
        return this.func_191971_c(-1, itemStackIn);
    }

    public boolean func_191971_c(int p_191971_1_, final ItemStack p_191971_2_)
    {
        if (p_191971_2_.func_190926_b())
        {
            return false;
        }
        else
        {
            try
            {
                if (p_191971_2_.isItemDamaged())
                {
                    if (p_191971_1_ == -1)
                    {
                        p_191971_1_ = this.getFirstEmptyStack();
                    }

                    if (p_191971_1_ >= 0)
                    {
                        this.mainInventory.set(p_191971_1_, p_191971_2_.copy());
                        ((ItemStack)this.mainInventory.get(p_191971_1_)).func_190915_d(5);
                        p_191971_2_.func_190920_e(0);
                        return true;
                    }
                    else if (this.player.capabilities.isCreativeMode)
                    {
                        p_191971_2_.func_190920_e(0);
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    int i;

                    while (true)
                    {
                        i = p_191971_2_.func_190916_E();

                        if (p_191971_1_ == -1)
                        {
                            p_191971_2_.func_190920_e(this.storePartialItemStack(p_191971_2_));
                        }
                        else
                        {
                            p_191971_2_.func_190920_e(this.func_191973_d(p_191971_1_, p_191971_2_));
                        }

                        if (p_191971_2_.func_190926_b() || p_191971_2_.func_190916_E() >= i)
                        {
                            break;
                        }
                    }

                    if (p_191971_2_.func_190916_E() == i && this.player.capabilities.isCreativeMode)
                    {
                        p_191971_2_.func_190920_e(0);
                        return true;
                    }
                    else
                    {
                        return p_191971_2_.func_190916_E() < i;
                    }
                }
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding item to inventory");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being added");
                crashreportcategory.addCrashSection("Item ID", Integer.valueOf(Item.getIdFromItem(p_191971_2_.getItem())));
                crashreportcategory.addCrashSection("Item data", Integer.valueOf(p_191971_2_.getMetadata()));
                crashreportcategory.setDetail("Item name", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        return p_191971_2_.getDisplayName();
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
    }

    public void func_191975_a(World p_191975_1_, ItemStack p_191975_2_)
    {
        if (!p_191975_1_.isRemote)
        {
            while (!p_191975_2_.func_190926_b())
            {
                int i = this.storeItemStack(p_191975_2_);

                if (i == -1)
                {
                    i = this.getFirstEmptyStack();
                }

                if (i == -1)
                {
                    this.player.dropItem(p_191975_2_, false);
                    break;
                }

                int j = p_191975_2_.getMaxStackSize() - this.getStackInSlot(i).func_190916_E();

                if (this.func_191971_c(i, p_191975_2_.splitStack(j)))
                {
                    ((EntityPlayerMP)this.player).connection.sendPacket(new SPacketSetSlot(-2, i, this.getStackInSlot(i)));
                }
            }
        }
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack decrStackSize(int index, int count)
    {
        List<ItemStack> list = null;

        for (NonNullList<ItemStack> nonnulllist : this.allInventories)
        {
            if (index < nonnulllist.size())
            {
                list = nonnulllist;
                break;
            }

            index -= nonnulllist.size();
        }

        return list != null && !((ItemStack)list.get(index)).func_190926_b() ? ItemStackHelper.getAndSplit(list, index, count) : ItemStack.field_190927_a;
    }

    public void deleteStack(ItemStack stack)
    {
        for (NonNullList<ItemStack> nonnulllist : this.allInventories)
        {
            for (int i = 0; i < nonnulllist.size(); ++i)
            {
                if (nonnulllist.get(i) == stack)
                {
                    nonnulllist.set(i, ItemStack.field_190927_a);
                    break;
                }
            }
        }
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeStackFromSlot(int index)
    {
        NonNullList<ItemStack> nonnulllist = null;

        for (NonNullList<ItemStack> nonnulllist1 : this.allInventories)
        {
            if (index < nonnulllist1.size())
            {
                nonnulllist = nonnulllist1;
                break;
            }

            index -= nonnulllist1.size();
        }

        if (nonnulllist != null && !((ItemStack)nonnulllist.get(index)).func_190926_b())
        {
            ItemStack itemstack = nonnulllist.get(index);
            nonnulllist.set(index, ItemStack.field_190927_a);
            return itemstack;
        }
        else
        {
            return ItemStack.field_190927_a;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        NonNullList<ItemStack> nonnulllist = null;

        for (NonNullList<ItemStack> nonnulllist1 : this.allInventories)
        {
            if (index < nonnulllist1.size())
            {
                nonnulllist = nonnulllist1;
                break;
            }

            index -= nonnulllist1.size();
        }

        if (nonnulllist != null)
        {
            nonnulllist.set(index, stack);
        }
    }

    public float getStrVsBlock(IBlockState state)
    {
        float f = 1.0F;

        if (!((ItemStack)this.mainInventory.get(this.currentItem)).func_190926_b())
        {
            f *= ((ItemStack)this.mainInventory.get(this.currentItem)).getStrVsBlock(state);
        }

        return f;
    }

    /**
     * Writes the inventory out as a list of compound tags. This is where the slot indices are used (+100 for armor, +80
     * for crafting).
     */
    public NBTTagList writeToNBT(NBTTagList nbtTagListIn)
    {
        for (int i = 0; i < this.mainInventory.size(); ++i)
        {
            if (!((ItemStack)this.mainInventory.get(i)).func_190926_b())
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                ((ItemStack)this.mainInventory.get(i)).writeToNBT(nbttagcompound);
                nbtTagListIn.appendTag(nbttagcompound);
            }
        }

        for (int j = 0; j < this.armorInventory.size(); ++j)
        {
            if (!((ItemStack)this.armorInventory.get(j)).func_190926_b())
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)(j + 100));
                ((ItemStack)this.armorInventory.get(j)).writeToNBT(nbttagcompound1);
                nbtTagListIn.appendTag(nbttagcompound1);
            }
        }

        for (int k = 0; k < this.offHandInventory.size(); ++k)
        {
            if (!((ItemStack)this.offHandInventory.get(k)).func_190926_b())
            {
                NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                nbttagcompound2.setByte("Slot", (byte)(k + 150));
                ((ItemStack)this.offHandInventory.get(k)).writeToNBT(nbttagcompound2);
                nbtTagListIn.appendTag(nbttagcompound2);
            }
        }

        return nbtTagListIn;
    }

    /**
     * Reads from the given tag list and fills the slots in the inventory with the correct items.
     */
    public void readFromNBT(NBTTagList nbtTagListIn)
    {
        this.mainInventory.clear();
        this.armorInventory.clear();
        this.offHandInventory.clear();

        for (int i = 0; i < nbtTagListIn.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbtTagListIn.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;
            ItemStack itemstack = new ItemStack(nbttagcompound);

            if (!itemstack.func_190926_b())
            {
                if (j >= 0 && j < this.mainInventory.size())
                {
                    this.mainInventory.set(j, itemstack);
                }
                else if (j >= 100 && j < this.armorInventory.size() + 100)
                {
                    this.armorInventory.set(j - 100, itemstack);
                }
                else if (j >= 150 && j < this.offHandInventory.size() + 150)
                {
                    this.offHandInventory.set(j - 150, itemstack);
                }
            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.mainInventory.size() + this.armorInventory.size() + this.offHandInventory.size();
    }

    public boolean func_191420_l()
    {
        for (ItemStack itemstack : this.mainInventory)
        {
            if (!itemstack.func_190926_b())
            {
                return false;
            }
        }

        for (ItemStack itemstack1 : this.armorInventory)
        {
            if (!itemstack1.func_190926_b())
            {
                return false;
            }
        }

        for (ItemStack itemstack2 : this.offHandInventory)
        {
            if (!itemstack2.func_190926_b())
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the stack in the given slot.
     */
    public ItemStack getStackInSlot(int index)
    {
        List<ItemStack> list = null;

        for (NonNullList<ItemStack> nonnulllist : this.allInventories)
        {
            if (index < nonnulllist.size())
            {
                list = nonnulllist;
                break;
            }

            index -= nonnulllist.size();
        }

        return list == null ? ItemStack.field_190927_a : (ItemStack)list.get(index);
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return "container.inventory";
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
        return false;
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    public ITextComponent getDisplayName()
    {
        return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean canHarvestBlock(IBlockState state)
    {
        if (state.getMaterial().isToolNotRequired())
        {
            return true;
        }
        else
        {
            ItemStack itemstack = this.getStackInSlot(this.currentItem);
            return !itemstack.func_190926_b() ? itemstack.canHarvestBlock(state) : false;
        }
    }

    /**
     * returns a player armor item (as itemstack) contained in specified armor slot.
     */
    public ItemStack armorItemInSlot(int slotIn)
    {
        return this.armorInventory.get(slotIn);
    }

    /**
     * Damages armor in each slot by the specified amount.
     */
    public void damageArmor(float damage)
    {
        damage = damage / 4.0F;

        if (damage < 1.0F)
        {
            damage = 1.0F;
        }

        for (int i = 0; i < this.armorInventory.size(); ++i)
        {
            ItemStack itemstack = this.armorInventory.get(i);

            if (itemstack.getItem() instanceof ItemArmor)
            {
                itemstack.damageItem((int)damage, this.player);
            }
        }
    }

    /**
     * Drop all armor and main inventory items.
     */
    public void dropAllItems()
    {
        for (List<ItemStack> list : this.allInventories)
        {
            for (int i = 0; i < list.size(); ++i)
            {
                ItemStack itemstack = list.get(i);

                if (!itemstack.func_190926_b())
                {
                    this.player.dropItem(itemstack, true, false);
                    list.set(i, ItemStack.field_190927_a);
                }
            }
        }
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty()
    {
        ++this.field_194017_h;
    }

    public int func_194015_p()
    {
        return this.field_194017_h;
    }

    /**
     * Set the stack helds by mouse, used in GUI/Container
     */
    public void setItemStack(ItemStack itemStackIn)
    {
        this.itemStack = itemStackIn;
    }

    /**
     * Stack helds by mouse, used in GUI and Containers
     */
    public ItemStack getItemStack()
    {
        return this.itemStack;
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (this.player.isDead)
        {
            return false;
        }
        else
        {
            return player.getDistanceSqToEntity(this.player) <= 64.0D;
        }
    }

    /**
     * Returns true if the specified ItemStack exists in the inventory.
     */
    public boolean hasItemStack(ItemStack itemStackIn)
    {
        label23:

        for (List<ItemStack> list : this.allInventories)
        {
            Iterator iterator = list.iterator();

            while (true)
            {
                if (!iterator.hasNext())
                {
                    continue label23;
                }

                ItemStack itemstack = (ItemStack)iterator.next();

                if (!itemstack.func_190926_b() && itemstack.isItemEqual(itemStackIn))
                {
                    break;
                }
            }

            return true;
        }

        return false;
    }

    public void openInventory(EntityPlayer player)
    {
    }

    public void closeInventory(EntityPlayer player)
    {
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    /**
     * Copy the ItemStack contents from another InventoryPlayer instance
     */
    public void copyInventory(InventoryPlayer playerInventory)
    {
        for (int i = 0; i < this.getSizeInventory(); ++i)
        {
            this.setInventorySlotContents(i, playerInventory.getStackInSlot(i));
        }

        this.currentItem = playerInventory.currentItem;
    }

    public int getField(int id)
    {
        return 0;
    }

    public void setField(int id, int value)
    {
    }

    public int getFieldCount()
    {
        return 0;
    }

    public void clear()
    {
        for (List<ItemStack> list : this.allInventories)
        {
            list.clear();
        }
    }

    public void func_194016_a(RecipeItemHelper p_194016_1_, boolean p_194016_2_)
    {
        for (ItemStack itemstack : this.mainInventory)
        {
            p_194016_1_.func_194112_a(itemstack);
        }

        if (p_194016_2_)
        {
            p_194016_1_.func_194112_a(this.offHandInventory.get(0));
        }
    }
}
