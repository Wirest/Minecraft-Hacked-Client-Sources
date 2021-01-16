package net.minecraft.item.crafting;

import javax.annotation.Nullable;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipesBanners
{
    public static class RecipeAddPattern implements IRecipe
    {
        public boolean matches(InventoryCrafting inv, World worldIn)
        {
            boolean flag = false;

            for (int i = 0; i < inv.getSizeInventory(); ++i)
            {
                ItemStack itemstack = inv.getStackInSlot(i);

                if (itemstack.getItem() == Items.BANNER)
                {
                    if (flag)
                    {
                        return false;
                    }

                    if (TileEntityBanner.getPatterns(itemstack) >= 6)
                    {
                        return false;
                    }

                    flag = true;
                }
            }

            if (!flag)
            {
                return false;
            }
            else
            {
                return this.func_190933_c(inv) != null;
            }
        }

        public ItemStack getCraftingResult(InventoryCrafting inv)
        {
            ItemStack itemstack = ItemStack.field_190927_a;

            for (int i = 0; i < inv.getSizeInventory(); ++i)
            {
                ItemStack itemstack1 = inv.getStackInSlot(i);

                if (!itemstack1.func_190926_b() && itemstack1.getItem() == Items.BANNER)
                {
                    itemstack = itemstack1.copy();
                    itemstack.func_190920_e(1);
                    break;
                }
            }

            BannerPattern bannerpattern = this.func_190933_c(inv);

            if (bannerpattern != null)
            {
                int k = 0;

                for (int j = 0; j < inv.getSizeInventory(); ++j)
                {
                    ItemStack itemstack2 = inv.getStackInSlot(j);

                    if (itemstack2.getItem() == Items.DYE)
                    {
                        k = itemstack2.getMetadata();
                        break;
                    }
                }

                NBTTagCompound nbttagcompound1 = itemstack.func_190925_c("BlockEntityTag");
                NBTTagList nbttaglist;

                if (nbttagcompound1.hasKey("Patterns", 9))
                {
                    nbttaglist = nbttagcompound1.getTagList("Patterns", 10);
                }
                else
                {
                    nbttaglist = new NBTTagList();
                    nbttagcompound1.setTag("Patterns", nbttaglist);
                }

                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setString("Pattern", bannerpattern.func_190993_b());
                nbttagcompound.setInteger("Color", k);
                nbttaglist.appendTag(nbttagcompound);
            }

            return itemstack;
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

        @Nullable
        private BannerPattern func_190933_c(InventoryCrafting p_190933_1_)
        {
            for (BannerPattern bannerpattern : BannerPattern.values())
            {
                if (bannerpattern.func_191000_d())
                {
                    boolean flag = true;

                    if (bannerpattern.func_190999_e())
                    {
                        boolean flag1 = false;
                        boolean flag2 = false;

                        for (int i = 0; i < p_190933_1_.getSizeInventory() && flag; ++i)
                        {
                            ItemStack itemstack = p_190933_1_.getStackInSlot(i);

                            if (!itemstack.func_190926_b() && itemstack.getItem() != Items.BANNER)
                            {
                                if (itemstack.getItem() == Items.DYE)
                                {
                                    if (flag2)
                                    {
                                        flag = false;
                                        break;
                                    }

                                    flag2 = true;
                                }
                                else
                                {
                                    if (flag1 || !itemstack.isItemEqual(bannerpattern.func_190998_f()))
                                    {
                                        flag = false;
                                        break;
                                    }

                                    flag1 = true;
                                }
                            }
                        }

                        if (!flag1 || !flag2)
                        {
                            flag = false;
                        }
                    }
                    else if (p_190933_1_.getSizeInventory() == bannerpattern.func_190996_c().length * bannerpattern.func_190996_c()[0].length())
                    {
                        int j = -1;

                        for (int k = 0; k < p_190933_1_.getSizeInventory() && flag; ++k)
                        {
                            int l = k / 3;
                            int i1 = k % 3;
                            ItemStack itemstack1 = p_190933_1_.getStackInSlot(k);

                            if (!itemstack1.func_190926_b() && itemstack1.getItem() != Items.BANNER)
                            {
                                if (itemstack1.getItem() != Items.DYE)
                                {
                                    flag = false;
                                    break;
                                }

                                if (j != -1 && j != itemstack1.getMetadata())
                                {
                                    flag = false;
                                    break;
                                }

                                if (bannerpattern.func_190996_c()[l].charAt(i1) == ' ')
                                {
                                    flag = false;
                                    break;
                                }

                                j = itemstack1.getMetadata();
                            }
                            else if (bannerpattern.func_190996_c()[l].charAt(i1) != ' ')
                            {
                                flag = false;
                                break;
                            }
                        }
                    }
                    else
                    {
                        flag = false;
                    }

                    if (flag)
                    {
                        return bannerpattern;
                    }
                }
            }

            return null;
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

    public static class RecipeDuplicatePattern implements IRecipe
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
                    if (itemstack2.getItem() != Items.BANNER)
                    {
                        return false;
                    }

                    if (!itemstack.func_190926_b() && !itemstack1.func_190926_b())
                    {
                        return false;
                    }

                    EnumDyeColor enumdyecolor = ItemBanner.getBaseColor(itemstack2);
                    boolean flag = TileEntityBanner.getPatterns(itemstack2) > 0;

                    if (!itemstack.func_190926_b())
                    {
                        if (flag)
                        {
                            return false;
                        }

                        if (enumdyecolor != ItemBanner.getBaseColor(itemstack))
                        {
                            return false;
                        }

                        itemstack1 = itemstack2;
                    }
                    else if (!itemstack1.func_190926_b())
                    {
                        if (!flag)
                        {
                            return false;
                        }

                        if (enumdyecolor != ItemBanner.getBaseColor(itemstack1))
                        {
                            return false;
                        }

                        itemstack = itemstack2;
                    }
                    else if (flag)
                    {
                        itemstack = itemstack2;
                    }
                    else
                    {
                        itemstack1 = itemstack2;
                    }
                }
            }

            return !itemstack.func_190926_b() && !itemstack1.func_190926_b();
        }

        public ItemStack getCraftingResult(InventoryCrafting inv)
        {
            for (int i = 0; i < inv.getSizeInventory(); ++i)
            {
                ItemStack itemstack = inv.getStackInSlot(i);

                if (!itemstack.func_190926_b() && TileEntityBanner.getPatterns(itemstack) > 0)
                {
                    ItemStack itemstack1 = itemstack.copy();
                    itemstack1.func_190920_e(1);
                    return itemstack1;
                }
            }

            return ItemStack.field_190927_a;
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

                if (!itemstack.func_190926_b())
                {
                    if (itemstack.getItem().hasContainerItem())
                    {
                        nonnulllist.set(i, new ItemStack(itemstack.getItem().getContainerItem()));
                    }
                    else if (itemstack.hasTagCompound() && TileEntityBanner.getPatterns(itemstack) > 0)
                    {
                        ItemStack itemstack1 = itemstack.copy();
                        itemstack1.func_190920_e(1);
                        nonnulllist.set(i, itemstack1);
                    }
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
