/*    */ package net.minecraft.item.crafting;
/*    */ 
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ public class RecipesArmor
/*    */ {
/*  9 */   private String[][] recipePatterns = { { "XXX", "X X" }, { "X X", "XXX", "XXX" }, { "XXX", "X X", "X X" }, { "X X", "X X" } };
/* 10 */   private Item[][] recipeItems = { { Items.leather, Items.iron_ingot, Items.diamond, Items.gold_ingot }, { Items.leather_helmet, Items.iron_helmet, Items.diamond_helmet, Items.golden_helmet }, { Items.leather_chestplate, Items.iron_chestplate, Items.diamond_chestplate, Items.golden_chestplate }, { Items.leather_leggings, Items.iron_leggings, Items.diamond_leggings, Items.golden_leggings }, { Items.leather_boots, Items.iron_boots, Items.diamond_boots, Items.golden_boots } };
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void addRecipes(CraftingManager craftManager)
/*    */   {
/* 17 */     for (int i = 0; i < this.recipeItems[0].length; i++)
/*    */     {
/* 19 */       Item item = this.recipeItems[0][i];
/*    */       
/* 21 */       for (int j = 0; j < this.recipeItems.length - 1; j++)
/*    */       {
/* 23 */         Item item1 = this.recipeItems[(j + 1)][i];
/* 24 */         craftManager.addRecipe(new ItemStack(item1), new Object[] { this.recipePatterns[j], Character.valueOf('X'), item });
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\crafting\RecipesArmor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */