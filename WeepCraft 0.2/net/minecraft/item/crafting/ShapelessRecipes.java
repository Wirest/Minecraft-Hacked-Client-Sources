package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.List;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ShapelessRecipes implements IRecipe
{
    /** Is the ItemStack that you get when craft the recipe. */
    private final ItemStack recipeOutput;
    private final NonNullList<Ingredient> recipeItems;
    private final String field_194138_c;

    public ShapelessRecipes(String p_i47500_1_, ItemStack p_i47500_2_, NonNullList<Ingredient> p_i47500_3_)
    {
        this.field_194138_c = p_i47500_1_;
        this.recipeOutput = p_i47500_2_;
        this.recipeItems = p_i47500_3_;
    }

    public String func_193358_e()
    {
        return this.field_194138_c;
    }

    public ItemStack getRecipeOutput()
    {
        return this.recipeOutput;
    }

    public NonNullList<Ingredient> func_192400_c()
    {
        return this.recipeItems;
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

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        List<Ingredient> list = Lists.newArrayList(this.recipeItems);

        for (int i = 0; i < inv.getHeight(); ++i)
        {
            for (int j = 0; j < inv.getWidth(); ++j)
            {
                ItemStack itemstack = inv.getStackInRowAndColumn(j, i);

                if (!itemstack.func_190926_b())
                {
                    boolean flag = false;

                    for (Ingredient ingredient : list)
                    {
                        if (ingredient.apply(itemstack))
                        {
                            flag = true;
                            list.remove(ingredient);
                            break;
                        }
                    }

                    if (!flag)
                    {
                        return false;
                    }
                }
            }
        }

        return list.isEmpty();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        return this.recipeOutput.copy();
    }

    public static ShapelessRecipes func_193363_a(JsonObject p_193363_0_)
    {
        String s = JsonUtils.getString(p_193363_0_, "group", "");
        NonNullList<Ingredient> nonnulllist = func_193364_a(JsonUtils.getJsonArray(p_193363_0_, "ingredients"));

        if (nonnulllist.isEmpty())
        {
            throw new JsonParseException("No ingredients for shapeless recipe");
        }
        else if (nonnulllist.size() > 9)
        {
            throw new JsonParseException("Too many ingredients for shapeless recipe");
        }
        else
        {
            ItemStack itemstack = ShapedRecipes.func_192405_a(JsonUtils.getJsonObject(p_193363_0_, "result"), true);
            return new ShapelessRecipes(s, itemstack, nonnulllist);
        }
    }

    private static NonNullList<Ingredient> func_193364_a(JsonArray p_193364_0_)
    {
        NonNullList<Ingredient> nonnulllist = NonNullList.<Ingredient>func_191196_a();

        for (int i = 0; i < p_193364_0_.size(); ++i)
        {
            Ingredient ingredient = ShapedRecipes.func_193361_a(p_193364_0_.get(i));

            if (ingredient != Ingredient.field_193370_a)
            {
                nonnulllist.add(ingredient);
            }
        }

        return nonnulllist;
    }

    public boolean func_194133_a(int p_194133_1_, int p_194133_2_)
    {
        return p_194133_1_ * p_194133_2_ >= this.recipeItems.size();
    }
}
