package net.minecraft.item.crafting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipeBookCloning implements IRecipe
{
    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        int i = 0;
        ItemStack itemstack = ItemStack.field_190927_a;

        for (int j = 0; j < inv.getSizeInventory(); ++j)
        {
            ItemStack itemstack1 = inv.getStackInSlot(j);

            if (!itemstack1.func_190926_b())
            {
                if (itemstack1.getItem() == Items.WRITTEN_BOOK)
                {
                    if (!itemstack.func_190926_b())
                    {
                        return false;
                    }

                    itemstack = itemstack1;
                }
                else
                {
                    if (itemstack1.getItem() != Items.WRITABLE_BOOK)
                    {
                        return false;
                    }

                    ++i;
                }
            }
        }

        return !itemstack.func_190926_b() && itemstack.hasTagCompound() && i > 0;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        int i = 0;
        ItemStack itemstack = ItemStack.field_190927_a;

        for (int j = 0; j < inv.getSizeInventory(); ++j)
        {
            ItemStack itemstack1 = inv.getStackInSlot(j);

            if (!itemstack1.func_190926_b())
            {
                if (itemstack1.getItem() == Items.WRITTEN_BOOK)
                {
                    if (!itemstack.func_190926_b())
                    {
                        return ItemStack.field_190927_a;
                    }

                    itemstack = itemstack1;
                }
                else
                {
                    if (itemstack1.getItem() != Items.WRITABLE_BOOK)
                    {
                        return ItemStack.field_190927_a;
                    }

                    ++i;
                }
            }
        }

        if (!itemstack.func_190926_b() && itemstack.hasTagCompound() && i >= 1 && ItemWrittenBook.getGeneration(itemstack) < 2)
        {
            ItemStack itemstack2 = new ItemStack(Items.WRITTEN_BOOK, i);
            itemstack2.setTagCompound(itemstack.getTagCompound().copy());
            itemstack2.getTagCompound().setInteger("generation", ItemWrittenBook.getGeneration(itemstack) + 1);

            if (itemstack.hasDisplayName())
            {
                itemstack2.setStackDisplayName(itemstack.getDisplayName());
            }

            return itemstack2;
        }
        else
        {
            return ItemStack.field_190927_a;
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

            if (itemstack.getItem() instanceof ItemWrittenBook)
            {
                ItemStack itemstack1 = itemstack.copy();
                itemstack1.func_190920_e(1);
                nonnulllist.set(i, itemstack1);
                break;
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
        return p_194133_1_ >= 3 && p_194133_2_ >= 3;
    }
}
