// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class RecipesArmor
{
    private String[][] recipePatterns;
    private Item[][] recipeItems;
    
    public RecipesArmor() {
        this.recipePatterns = new String[][] { { "XXX", "X X" }, { "X X", "XXX", "XXX" }, { "XXX", "X X", "X X" }, { "X X", "X X" } };
        this.recipeItems = new Item[][] { { Items.leather, Items.iron_ingot, Items.diamond, Items.gold_ingot }, { Items.leather_helmet, Items.iron_helmet, Items.diamond_helmet, Items.golden_helmet }, { Items.leather_chestplate, Items.iron_chestplate, Items.diamond_chestplate, Items.golden_chestplate }, { Items.leather_leggings, Items.iron_leggings, Items.diamond_leggings, Items.golden_leggings }, { Items.leather_boots, Items.iron_boots, Items.diamond_boots, Items.golden_boots } };
    }
    
    public void addRecipes(final CraftingManager craftManager) {
        for (int i = 0; i < this.recipeItems[0].length; ++i) {
            final Item item = this.recipeItems[0][i];
            for (int j = 0; j < this.recipeItems.length - 1; ++j) {
                final Item item2 = this.recipeItems[j + 1][i];
                craftManager.addRecipe(new ItemStack(item2), this.recipePatterns[j], 'X', item);
            }
        }
    }
}
