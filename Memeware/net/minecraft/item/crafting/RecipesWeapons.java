package net.minecraft.item.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipesWeapons {
    private String[][] recipePatterns = new String[][]{{"X", "X", "#"}};
    private Object[][] recipeItems;
    private static final String __OBFID = "CL_00000097";

    public RecipesWeapons() {
        this.recipeItems = new Object[][]{{Blocks.planks, Blocks.cobblestone, Items.iron_ingot, Items.diamond, Items.gold_ingot}, {Items.wooden_sword, Items.stone_sword, Items.iron_sword, Items.diamond_sword, Items.golden_sword}};
    }

    /**
     * Adds the weapon recipes to the CraftingManager.
     */
    public void addRecipes(CraftingManager p_77583_1_) {
        for (int var2 = 0; var2 < this.recipeItems[0].length; ++var2) {
            Object var3 = this.recipeItems[0][var2];

            for (int var4 = 0; var4 < this.recipeItems.length - 1; ++var4) {
                Item var5 = (Item) this.recipeItems[var4 + 1][var2];
                p_77583_1_.addRecipe(new ItemStack(var5), new Object[]{this.recipePatterns[var4], '#', Items.stick, 'X', var3});
            }
        }

        p_77583_1_.addRecipe(new ItemStack(Items.bow, 1), new Object[]{" #X", "# X", " #X", 'X', Items.string, '#', Items.stick});
        p_77583_1_.addRecipe(new ItemStack(Items.arrow, 4), new Object[]{"X", "#", "Y", 'Y', Items.feather, 'X', Items.flint, '#', Items.stick});
    }
}
