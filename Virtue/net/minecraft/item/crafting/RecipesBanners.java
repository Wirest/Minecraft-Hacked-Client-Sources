package net.minecraft.item.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.world.World;

public class RecipesBanners
{
    private static final String __OBFID = "CL_00002160";

    void func_179534_a(CraftingManager p_179534_1_)
    {
        EnumDyeColor[] var2 = EnumDyeColor.values();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            EnumDyeColor var5 = var2[var4];
            p_179534_1_.addRecipe(new ItemStack(Items.banner, 1, var5.getDyeColorDamage()), new Object[] {"###", "###", " | ", '#', new ItemStack(Blocks.wool, 1, var5.func_176765_a()), '|', Items.stick});
        }

        p_179534_1_.func_180302_a(new RecipesBanners.RecipeDuplicatePattern(null));
        p_179534_1_.func_180302_a(new RecipesBanners.RecipeAddPattern(null));
    }

    static class RecipeAddPattern implements IRecipe
    {
        private static final String __OBFID = "CL_00002158";

        private RecipeAddPattern() {}

        public boolean matches(InventoryCrafting p_77569_1_, World worldIn)
        {
            boolean var3 = false;

            for (int var4 = 0; var4 < p_77569_1_.getSizeInventory(); ++var4)
            {
                ItemStack var5 = p_77569_1_.getStackInSlot(var4);

                if (var5 != null && var5.getItem() == Items.banner)
                {
                    if (var3)
                    {
                        return false;
                    }

                    if (TileEntityBanner.func_175113_c(var5) >= 6)
                    {
                        return false;
                    }

                    var3 = true;
                }
            }

            if (!var3)
            {
                return false;
            }
            else
            {
                return this.func_179533_c(p_77569_1_) != null;
            }
        }

        public ItemStack getCraftingResult(InventoryCrafting p_77572_1_)
        {
            ItemStack var2 = null;

            for (int var3 = 0; var3 < p_77572_1_.getSizeInventory(); ++var3)
            {
                ItemStack var4 = p_77572_1_.getStackInSlot(var3);

                if (var4 != null && var4.getItem() == Items.banner)
                {
                    var2 = var4.copy();
                    var2.stackSize = 1;
                    break;
                }
            }

            TileEntityBanner.EnumBannerPattern var8 = this.func_179533_c(p_77572_1_);

            if (var8 != null)
            {
                int var9 = 0;
                ItemStack var6;

                for (int var5 = 0; var5 < p_77572_1_.getSizeInventory(); ++var5)
                {
                    var6 = p_77572_1_.getStackInSlot(var5);

                    if (var6 != null && var6.getItem() == Items.dye)
                    {
                        var9 = var6.getMetadata();
                        break;
                    }
                }

                NBTTagCompound var10 = var2.getSubCompound("BlockEntityTag", true);
                var6 = null;
                NBTTagList var11;

                if (var10.hasKey("Patterns", 9))
                {
                    var11 = var10.getTagList("Patterns", 10);
                }
                else
                {
                    var11 = new NBTTagList();
                    var10.setTag("Patterns", var11);
                }

                NBTTagCompound var7 = new NBTTagCompound();
                var7.setString("Pattern", var8.func_177273_b());
                var7.setInteger("Color", var9);
                var11.appendTag(var7);
            }

            return var2;
        }

        public int getRecipeSize()
        {
            return 10;
        }

        public ItemStack getRecipeOutput()
        {
            return null;
        }

        public ItemStack[] func_179532_b(InventoryCrafting p_179532_1_)
        {
            ItemStack[] var2 = new ItemStack[p_179532_1_.getSizeInventory()];

            for (int var3 = 0; var3 < var2.length; ++var3)
            {
                ItemStack var4 = p_179532_1_.getStackInSlot(var3);

                if (var4 != null && var4.getItem().hasContainerItem())
                {
                    var2[var3] = new ItemStack(var4.getItem().getContainerItem());
                }
            }

            return var2;
        }

        private TileEntityBanner.EnumBannerPattern func_179533_c(InventoryCrafting p_179533_1_)
        {
            TileEntityBanner.EnumBannerPattern[] var2 = TileEntityBanner.EnumBannerPattern.values();
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4)
            {
                TileEntityBanner.EnumBannerPattern var5 = var2[var4];

                if (var5.func_177270_d())
                {
                    boolean var6 = true;
                    int var9;

                    if (var5.func_177269_e())
                    {
                        boolean var12 = false;
                        boolean var13 = false;

                        for (var9 = 0; var9 < p_179533_1_.getSizeInventory() && var6; ++var9)
                        {
                            ItemStack var14 = p_179533_1_.getStackInSlot(var9);

                            if (var14 != null && var14.getItem() != Items.banner)
                            {
                                if (var14.getItem() == Items.dye)
                                {
                                    if (var13)
                                    {
                                        var6 = false;
                                        break;
                                    }

                                    var13 = true;
                                }
                                else
                                {
                                    if (var12 || !var14.isItemEqual(var5.func_177272_f()))
                                    {
                                        var6 = false;
                                        break;
                                    }

                                    var12 = true;
                                }
                            }
                        }

                        if (!var12)
                        {
                            var6 = false;
                        }
                    }
                    else if (p_179533_1_.getSizeInventory() != var5.func_177267_c().length * var5.func_177267_c()[0].length())
                    {
                        var6 = false;
                    }
                    else
                    {
                        int var7 = -1;

                        for (int var8 = 0; var8 < p_179533_1_.getSizeInventory() && var6; ++var8)
                        {
                            var9 = var8 / 3;
                            int var10 = var8 % 3;
                            ItemStack var11 = p_179533_1_.getStackInSlot(var8);

                            if (var11 != null && var11.getItem() != Items.banner)
                            {
                                if (var11.getItem() != Items.dye)
                                {
                                    var6 = false;
                                    break;
                                }

                                if (var7 != -1 && var7 != var11.getMetadata())
                                {
                                    var6 = false;
                                    break;
                                }

                                if (var5.func_177267_c()[var9].charAt(var10) == 32)
                                {
                                    var6 = false;
                                    break;
                                }

                                var7 = var11.getMetadata();
                            }
                            else if (var5.func_177267_c()[var9].charAt(var10) != 32)
                            {
                                var6 = false;
                                break;
                            }
                        }
                    }

                    if (var6)
                    {
                        return var5;
                    }
                }
            }

            return null;
        }

        RecipeAddPattern(Object p_i45780_1_)
        {
            this();
        }
    }

    static class RecipeDuplicatePattern implements IRecipe
    {
        private static final String __OBFID = "CL_00002157";

        private RecipeDuplicatePattern() {}

        public boolean matches(InventoryCrafting p_77569_1_, World worldIn)
        {
            ItemStack var3 = null;
            ItemStack var4 = null;

            for (int var5 = 0; var5 < p_77569_1_.getSizeInventory(); ++var5)
            {
                ItemStack var6 = p_77569_1_.getStackInSlot(var5);

                if (var6 != null)
                {
                    if (var6.getItem() != Items.banner)
                    {
                        return false;
                    }

                    if (var3 != null && var4 != null)
                    {
                        return false;
                    }

                    int var7 = TileEntityBanner.getBaseColor(var6);
                    boolean var8 = TileEntityBanner.func_175113_c(var6) > 0;

                    if (var3 != null)
                    {
                        if (var8)
                        {
                            return false;
                        }

                        if (var7 != TileEntityBanner.getBaseColor(var3))
                        {
                            return false;
                        }

                        var4 = var6;
                    }
                    else if (var4 != null)
                    {
                        if (!var8)
                        {
                            return false;
                        }

                        if (var7 != TileEntityBanner.getBaseColor(var4))
                        {
                            return false;
                        }

                        var3 = var6;
                    }
                    else if (var8)
                    {
                        var3 = var6;
                    }
                    else
                    {
                        var4 = var6;
                    }
                }
            }

            return var3 != null && var4 != null;
        }

        public ItemStack getCraftingResult(InventoryCrafting p_77572_1_)
        {
            for (int var2 = 0; var2 < p_77572_1_.getSizeInventory(); ++var2)
            {
                ItemStack var3 = p_77572_1_.getStackInSlot(var2);

                if (var3 != null && TileEntityBanner.func_175113_c(var3) > 0)
                {
                    ItemStack var4 = var3.copy();
                    var4.stackSize = 1;
                    return var4;
                }
            }

            return null;
        }

        public int getRecipeSize()
        {
            return 2;
        }

        public ItemStack getRecipeOutput()
        {
            return null;
        }

        public ItemStack[] func_179532_b(InventoryCrafting p_179532_1_)
        {
            ItemStack[] var2 = new ItemStack[p_179532_1_.getSizeInventory()];

            for (int var3 = 0; var3 < var2.length; ++var3)
            {
                ItemStack var4 = p_179532_1_.getStackInSlot(var3);

                if (var4 != null)
                {
                    if (var4.getItem().hasContainerItem())
                    {
                        var2[var3] = new ItemStack(var4.getItem().getContainerItem());
                    }
                    else if (var4.hasTagCompound() && TileEntityBanner.func_175113_c(var4) > 0)
                    {
                        var2[var3] = var4.copy();
                        var2[var3].stackSize = 1;
                    }
                }
            }

            return var2;
        }

        RecipeDuplicatePattern(Object p_i45779_1_)
        {
            this();
        }
    }
}
