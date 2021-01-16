package net.minecraft.item.crafting;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipesDyes
{
    private static final String __OBFID = "CL_00000082";

    /**
     * Adds the dye recipes to the CraftingManager.
     */
    public void addRecipes(CraftingManager p_77607_1_)
    {
        int var2;

        for (var2 = 0; var2 < 16; ++var2)
        {
            p_77607_1_.addShapelessRecipe(new ItemStack(Blocks.wool, 1, var2), new Object[] {new ItemStack(Items.dye, 1, 15 - var2), new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 0)});
            p_77607_1_.addRecipe(new ItemStack(Blocks.stained_hardened_clay, 8, 15 - var2), new Object[] {"###", "#X#", "###", '#', new ItemStack(Blocks.hardened_clay), 'X', new ItemStack(Items.dye, 1, var2)});
            p_77607_1_.addRecipe(new ItemStack(Blocks.stained_glass, 8, 15 - var2), new Object[] {"###", "#X#", "###", '#', new ItemStack(Blocks.glass), 'X', new ItemStack(Items.dye, 1, var2)});
            p_77607_1_.addRecipe(new ItemStack(Blocks.stained_glass_pane, 16, var2), new Object[] {"###", "###", '#', new ItemStack(Blocks.stained_glass, 1, var2)});
        }

        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 1, EnumDyeColor.YELLOW.getDyeColorDamage()), new Object[] {new ItemStack(Blocks.yellow_flower, 1, BlockFlower.EnumFlowerType.DANDELION.func_176968_b())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 1, EnumDyeColor.RED.getDyeColorDamage()), new Object[] {new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.POPPY.func_176968_b())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 3, EnumDyeColor.WHITE.getDyeColorDamage()), new Object[] {Items.bone});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.PINK.getDyeColorDamage()), new Object[] {new ItemStack(Items.dye, 1, EnumDyeColor.RED.getDyeColorDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.WHITE.getDyeColorDamage())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.ORANGE.getDyeColorDamage()), new Object[] {new ItemStack(Items.dye, 1, EnumDyeColor.RED.getDyeColorDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.YELLOW.getDyeColorDamage())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.LIME.getDyeColorDamage()), new Object[] {new ItemStack(Items.dye, 1, EnumDyeColor.GREEN.getDyeColorDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.WHITE.getDyeColorDamage())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.GRAY.getDyeColorDamage()), new Object[] {new ItemStack(Items.dye, 1, EnumDyeColor.BLACK.getDyeColorDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.WHITE.getDyeColorDamage())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.SILVER.getDyeColorDamage()), new Object[] {new ItemStack(Items.dye, 1, EnumDyeColor.GRAY.getDyeColorDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.WHITE.getDyeColorDamage())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 3, EnumDyeColor.SILVER.getDyeColorDamage()), new Object[] {new ItemStack(Items.dye, 1, EnumDyeColor.BLACK.getDyeColorDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.WHITE.getDyeColorDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.WHITE.getDyeColorDamage())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.LIGHT_BLUE.getDyeColorDamage()), new Object[] {new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeColorDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.WHITE.getDyeColorDamage())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.CYAN.getDyeColorDamage()), new Object[] {new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeColorDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.GREEN.getDyeColorDamage())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.PURPLE.getDyeColorDamage()), new Object[] {new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeColorDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.RED.getDyeColorDamage())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.MAGENTA.getDyeColorDamage()), new Object[] {new ItemStack(Items.dye, 1, EnumDyeColor.PURPLE.getDyeColorDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.PINK.getDyeColorDamage())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 3, EnumDyeColor.MAGENTA.getDyeColorDamage()), new Object[] {new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeColorDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.RED.getDyeColorDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.PINK.getDyeColorDamage())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 4, EnumDyeColor.MAGENTA.getDyeColorDamage()), new Object[] {new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeColorDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.RED.getDyeColorDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.RED.getDyeColorDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.WHITE.getDyeColorDamage())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 1, EnumDyeColor.LIGHT_BLUE.getDyeColorDamage()), new Object[] {new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.BLUE_ORCHID.func_176968_b())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 1, EnumDyeColor.MAGENTA.getDyeColorDamage()), new Object[] {new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.ALLIUM.func_176968_b())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 1, EnumDyeColor.SILVER.getDyeColorDamage()), new Object[] {new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.HOUSTONIA.func_176968_b())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 1, EnumDyeColor.RED.getDyeColorDamage()), new Object[] {new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.RED_TULIP.func_176968_b())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 1, EnumDyeColor.ORANGE.getDyeColorDamage()), new Object[] {new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.ORANGE_TULIP.func_176968_b())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 1, EnumDyeColor.SILVER.getDyeColorDamage()), new Object[] {new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.WHITE_TULIP.func_176968_b())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 1, EnumDyeColor.PINK.getDyeColorDamage()), new Object[] {new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.PINK_TULIP.func_176968_b())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 1, EnumDyeColor.SILVER.getDyeColorDamage()), new Object[] {new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.OXEYE_DAISY.func_176968_b())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.YELLOW.getDyeColorDamage()), new Object[] {new ItemStack(Blocks.double_plant, 1, BlockDoublePlant.EnumPlantType.SUNFLOWER.func_176936_a())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.MAGENTA.getDyeColorDamage()), new Object[] {new ItemStack(Blocks.double_plant, 1, BlockDoublePlant.EnumPlantType.SYRINGA.func_176936_a())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.RED.getDyeColorDamage()), new Object[] {new ItemStack(Blocks.double_plant, 1, BlockDoublePlant.EnumPlantType.ROSE.func_176936_a())});
        p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.PINK.getDyeColorDamage()), new Object[] {new ItemStack(Blocks.double_plant, 1, BlockDoublePlant.EnumPlantType.PAEONIA.func_176936_a())});

        for (var2 = 0; var2 < 16; ++var2)
        {
            p_77607_1_.addRecipe(new ItemStack(Blocks.carpet, 3, var2), new Object[] {"##", '#', new ItemStack(Blocks.wool, 1, var2)});
        }
    }
}
