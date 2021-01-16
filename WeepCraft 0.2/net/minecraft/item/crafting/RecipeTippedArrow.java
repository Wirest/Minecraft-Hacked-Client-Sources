package net.minecraft.item.crafting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipeTippedArrow implements IRecipe
{
    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        if (inv.getWidth() == 3 && inv.getHeight() == 3)
        {
            for (int i = 0; i < inv.getWidth(); ++i)
            {
                for (int j = 0; j < inv.getHeight(); ++j)
                {
                    ItemStack itemstack = inv.getStackInRowAndColumn(i, j);

                    if (itemstack.func_190926_b())
                    {
                        return false;
                    }

                    Item item = itemstack.getItem();

                    if (i == 1 && j == 1)
                    {
                        if (item != Items.LINGERING_POTION)
                        {
                            return false;
                        }
                    }
                    else if (item != Items.ARROW)
                    {
                        return false;
                    }
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack itemstack = inv.getStackInRowAndColumn(1, 1);

        if (itemstack.getItem() != Items.LINGERING_POTION)
        {
            return ItemStack.field_190927_a;
        }
        else
        {
            ItemStack itemstack1 = new ItemStack(Items.TIPPED_ARROW, 8);
            PotionUtils.addPotionToItemStack(itemstack1, PotionUtils.getPotionFromItem(itemstack));
            PotionUtils.appendEffects(itemstack1, PotionUtils.getFullEffectsFromItem(itemstack));
            return itemstack1;
        }
    }

    public ItemStack getRecipeOutput()
    {
        return ItemStack.field_190927_a;
    }

    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
    {
        return NonNullList.<ItemStack>func_191197_a(inv.getSizeInventory(), ItemStack.field_190927_a);
    }

    public boolean func_192399_d()
    {
        return true;
    }

    public boolean func_194133_a(int p_194133_1_, int p_194133_2_)
    {
        return p_194133_1_ >= 2 && p_194133_2_ >= 2;
    }
}
