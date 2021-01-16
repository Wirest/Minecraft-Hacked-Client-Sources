package net.minecraft.item.crafting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ShieldRecipes
{
    public static class Decoration implements IRecipe
    {
        public boolean matches(InventoryCrafting inv, World worldIn)
        {
            ItemStack itemstack = ItemStack.field_190927_a;
            ItemStack itemstack1 = ItemStack.field_190927_a;

            for (int i = 0; i < inv.getSizeInventory(); ++i)
            {
                ItemStack itemstack2 = inv.getStackInSlot(i);

                if (!itemstack2.func_190926_b())
                {
                    if (itemstack2.getItem() == Items.BANNER)
                    {
                        if (!itemstack1.func_190926_b())
                        {
                            return false;
                        }

                        itemstack1 = itemstack2;
                    }
                    else
                    {
                        if (itemstack2.getItem() != Items.SHIELD)
                        {
                            return false;
                        }

                        if (!itemstack.func_190926_b())
                        {
                            return false;
                        }

                        if (itemstack2.getSubCompound("BlockEntityTag") != null)
                        {
                            return false;
                        }

                        itemstack = itemstack2;
                    }
                }
            }

            if (!itemstack.func_190926_b() && !itemstack1.func_190926_b())
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        public ItemStack getCraftingResult(InventoryCrafting inv)
        {
            ItemStack itemstack = ItemStack.field_190927_a;
            ItemStack itemstack1 = ItemStack.field_190927_a;

            for (int i = 0; i < inv.getSizeInventory(); ++i)
            {
                ItemStack itemstack2 = inv.getStackInSlot(i);

                if (!itemstack2.func_190926_b())
                {
                    if (itemstack2.getItem() == Items.BANNER)
                    {
                        itemstack = itemstack2;
                    }
                    else if (itemstack2.getItem() == Items.SHIELD)
                    {
                        itemstack1 = itemstack2.copy();
                    }
                }
            }

            if (itemstack1.func_190926_b())
            {
                return itemstack1;
            }
            else
            {
                NBTTagCompound nbttagcompound = itemstack.getSubCompound("BlockEntityTag");
                NBTTagCompound nbttagcompound1 = nbttagcompound == null ? new NBTTagCompound() : nbttagcompound.copy();
                nbttagcompound1.setInteger("Base", itemstack.getMetadata() & 15);
                itemstack1.setTagInfo("BlockEntityTag", nbttagcompound1);
                return itemstack1;
            }
        }

        public ItemStack getRecipeOutput()
        {
            return ItemStack.field_190927_a;
        }

        public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
        {
            NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>func_191197_a(inv.getSizeInventory(), ItemStack.field_190927_a);

            for (int i = 0; i < nonnulllist.size(); ++i)
            {
                ItemStack itemstack = inv.getStackInSlot(i);

                if (itemstack.getItem().hasContainerItem())
                {
                    nonnulllist.set(i, new ItemStack(itemstack.getItem().getContainerItem()));
                }
            }

            return nonnulllist;
        }

        public boolean func_192399_d()
        {
            return true;
        }

        public boolean func_194133_a(int p_194133_1_, int p_194133_2_)
        {
            return p_194133_1_ * p_194133_2_ >= 2;
        }
    }
}
