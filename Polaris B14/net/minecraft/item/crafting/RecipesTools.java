/*    */ package net.minecraft.item.crafting;
/*    */ 
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ public class RecipesTools
/*    */ {
/* 10 */   private String[][] recipePatterns = { { "XXX", " # ", " # " }, { "X", "#", "#" }, { "XX", "X#", " #" }, { "XX", " #", " #" } };
/* 11 */   private Object[][] recipeItems = { { Blocks.planks, Blocks.cobblestone, Items.iron_ingot, Items.diamond, Items.gold_ingot }, { Items.wooden_pickaxe, Items.stone_pickaxe, Items.iron_pickaxe, Items.diamond_pickaxe, Items.golden_pickaxe }, { Items.wooden_shovel, Items.stone_shovel, Items.iron_shovel, Items.diamond_shovel, Items.golden_shovel }, { Items.wooden_axe, Items.stone_axe, Items.iron_axe, Items.diamond_axe, Items.golden_axe }, { Items.wooden_hoe, Items.stone_hoe, Items.iron_hoe, Items.diamond_hoe, Items.golden_hoe } };
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void addRecipes(CraftingManager p_77586_1_)
/*    */   {
/* 18 */     for (int i = 0; i < this.recipeItems[0].length; i++)
/*    */     {
/* 20 */       Object object = this.recipeItems[0][i];
/*    */       
/* 22 */       for (int j = 0; j < this.recipeItems.length - 1; j++)
/*    */       {
/* 24 */         Item item = (Item)this.recipeItems[(j + 1)][i];
/* 25 */         p_77586_1_.addRecipe(new ItemStack(item), new Object[] { this.recipePatterns[j], Character.valueOf('#'), Items.stick, Character.valueOf('X'), object });
/*    */       }
/*    */     }
/*    */     
/* 29 */     p_77586_1_.addRecipe(new ItemStack(Items.shears), new Object[] { " #", "# ", Character.valueOf('#'), Items.iron_ingot });
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\crafting\RecipesTools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */