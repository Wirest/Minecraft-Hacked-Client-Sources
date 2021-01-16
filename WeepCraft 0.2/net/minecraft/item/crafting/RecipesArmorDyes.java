package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipesArmorDyes implements IRecipe
{
    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        ItemStack itemstack = ItemStack.field_190927_a;
        List<ItemStack> list = Lists.<ItemStack>newArrayList();

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack itemstack1 = inv.getStackInSlot(i);

            if (!itemstack1.func_190926_b())
            {
                if (itemstack1.getItem() instanceof ItemArmor)
                {
                    ItemArmor itemarmor = (ItemArmor)itemstack1.getItem();

                    if (itemarmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER || !itemstack.func_190926_b())
                    {
                        return false;
                    }

                    itemstack = itemstack1;
                }
                else
                {
                    if (itemstack1.getItem() != Items.DYE)
                    {
                        return false;
                    }

                    list.add(itemstack1);
                }
            }
        }

        return !itemstack.func_190926_b() && !list.isEmpty();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack itemstack = ItemStack.field_190927_a;
        int[] aint = new int[3];
        int i = 0;
        int j = 0;
        ItemArmor itemarmor = null;

        for (int k = 0; k < inv.getSizeInventory(); ++k)
        {
            ItemStack itemstack1 = inv.getStackInSlot(k);

            if (!itemstack1.func_190926_b())
            {
                if (itemstack1.getItem() instanceof ItemArmor)
                {
                    itemarmor = (ItemArmor)itemstack1.getItem();

                    if (itemarmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER || !itemstack.func_190926_b())
                    {
                        return ItemStack.field_190927_a;
                    }

                    itemstack = itemstack1.copy();
                    itemstack.func_190920_e(1);

                    if (itemarmor.hasColor(itemstack1))
                    {
                        int l = itemarmor.getColor(itemstack);
                        float f = (float)(l >> 16 & 255) / 255.0F;
                        float f1 = (float)(l >> 8 & 255) / 255.0F;
                        float f2 = (float)(l & 255) / 255.0F;
                        i = (int)((float)i + Math.max(f, Math.max(f1, f2)) * 255.0F);
                        aint[0] = (int)((float)aint[0] + f * 255.0F);
                        aint[1] = (int)((float)aint[1] + f1 * 255.0F);
                        aint[2] = (int)((float)aint[2] + f2 * 255.0F);
                        ++j;
                    }
                }
                else
                {
                    if (itemstack1.getItem() != Items.DYE)
                    {
                        return ItemStack.field_190927_a;
                    }

                    float[] afloat = EnumDyeColor.byDyeDamage(itemstack1.getMetadata()).func_193349_f();
                    int l1 = (int)(afloat[0] * 255.0F);
                    int i2 = (int)(afloat[1] * 255.0F);
                    int j2 = (int)(afloat[2] * 255.0F);
                    i += Math.max(l1, Math.max(i2, j2));
                    aint[0] += l1;
                    aint[1] += i2;
                    aint[2] += j2;
                    ++j;
                }
            }
        }

        if (itemarmor == null)
        {
            return ItemStack.field_190927_a;
        }
        else
        {
            int i1 = aint[0] / j;
            int j1 = aint[1] / j;
            int k1 = aint[2] / j;
            float f3 = (float)i / (float)j;
            float f4 = (float)Math.max(i1, Math.max(j1, k1));
            i1 = (int)((float)i1 * f3 / f4);
            j1 = (int)((float)j1 * f3 / f4);
            k1 = (int)((float)k1 * f3 / f4);
            int k2 = (i1 << 8) + j1;
            k2 = (k2 << 8) + k1;
            itemarmor.setColor(itemstack, k2);
            return itemstack;
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
