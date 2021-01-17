// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;

public class RecipesWeapons
{
    private String[][] recipePatterns;
    private Object[][] recipeItems;
    
    public RecipesWeapons() {
        this.recipePatterns = new String[][] { { "X", "X", "#" } };
        this.recipeItems = new Object[][] { { Blocks.planks, Blocks.cobblestone, Items.iron_ingot, Items.diamond, Items.gold_ingot }, { Items.wooden_sword, Items.stone_sword, Items.iron_sword, Items.diamond_sword, Items.golden_sword } };
    }
    
    public void addRecipes(final CraftingManager p_77583_1_) {
        for (int i = 0; i < this.recipeItems[0].length; ++i) {
            final Object object = this.recipeItems[0][i];
            for (int j = 0; j < this.recipeItems.length - 1; ++j) {
                final Item item = (Item)this.recipeItems[j + 1][i];
                p_77583_1_.addRecipe(new ItemStack(item), this.recipePatterns[j], '#', Items.stick, 'X', object);
            }
        }
        p_77583_1_.addRecipe(new ItemStack(Items.bow, 1), " #X", "# X", " #X", 'X', Items.string, '#', Items.stick);
        p_77583_1_.addRecipe(new ItemStack(Items.arrow, 4), "X", "#", "Y", 'Y', Items.feather, 'X', Items.flint, '#', Items.stick);
    }
}
