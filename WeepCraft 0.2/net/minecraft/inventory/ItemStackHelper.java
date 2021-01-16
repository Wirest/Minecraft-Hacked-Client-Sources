package net.minecraft.inventory;

import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;

public class ItemStackHelper
{
    public static ItemStack getAndSplit(List<ItemStack> stacks, int index, int amount)
    {
        return index >= 0 && index < stacks.size() && !((ItemStack)stacks.get(index)).func_190926_b() && amount > 0 ? ((ItemStack)stacks.get(index)).splitStack(amount) : ItemStack.field_190927_a;
    }

    public static ItemStack getAndRemove(List<ItemStack> stacks, int index)
    {
        return index >= 0 && index < stacks.size() ? (ItemStack)stacks.set(index, ItemStack.field_190927_a) : ItemStack.field_190927_a;
    }

    public static NBTTagCompound func_191282_a(NBTTagCompound p_191282_0_, NonNullList<ItemStack> p_191282_1_)
    {
        return func_191281_a(p_191282_0_, p_191282_1_, true);
    }

    public static NBTTagCompound func_191281_a(NBTTagCompound p_191281_0_, NonNullList<ItemStack> p_191281_1_, boolean p_191281_2_)
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < p_191281_1_.size(); ++i)
        {
            ItemStack itemstack = p_191281_1_.get(i);

            if (!itemstack.func_190926_b())
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                itemstack.writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        if (!nbttaglist.hasNoTags() || p_191281_2_)
        {
            p_191281_0_.setTag("Items", nbttaglist);
        }

        return p_191281_0_;
    }

    public static void func_191283_b(NBTTagCompound p_191283_0_, NonNullList<ItemStack> p_191283_1_)
    {
        NBTTagList nbttaglist = p_191283_0_.getTagList("Items", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;

            if (j >= 0 && j < p_191283_1_.size())
            {
                p_191283_1_.set(j, new ItemStack(nbttagcompound));
            }
        }
    }
}
