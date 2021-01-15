/*    */ package net.minecraft.item.crafting;
/*    */ 
/*    */ import net.minecraft.block.BlockDoublePlant.EnumPlantType;
/*    */ import net.minecraft.block.BlockFlower.EnumFlowerType;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.EnumDyeColor;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RecipesDyes
/*    */ {
/*    */   public void addRecipes(CraftingManager p_77607_1_)
/*    */   {
/* 18 */     for (int i = 0; i < 16; i++)
/*    */     {
/* 20 */       p_77607_1_.addShapelessRecipe(new ItemStack(Blocks.wool, 1, i), new Object[] { new ItemStack(Items.dye, 1, 15 - i), new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 0) });
/* 21 */       p_77607_1_.addRecipe(new ItemStack(Blocks.stained_hardened_clay, 8, 15 - i), new Object[] { "###", "#X#", "###", Character.valueOf('#'), new ItemStack(Blocks.hardened_clay), Character.valueOf('X'), new ItemStack(Items.dye, 1, i) });
/* 22 */       p_77607_1_.addRecipe(new ItemStack(Blocks.stained_glass, 8, 15 - i), new Object[] { "###", "#X#", "###", Character.valueOf('#'), new ItemStack(Blocks.glass), Character.valueOf('X'), new ItemStack(Items.dye, 1, i) });
/* 23 */       p_77607_1_.addRecipe(new ItemStack(Blocks.stained_glass_pane, 16, i), new Object[] { "###", "###", Character.valueOf('#'), new ItemStack(Blocks.stained_glass, 1, i) });
/*    */     }
/*    */     
/* 26 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 1, EnumDyeColor.YELLOW.getDyeDamage()), new Object[] { new ItemStack(Blocks.yellow_flower, 1, BlockFlower.EnumFlowerType.DANDELION.getMeta()) });
/* 27 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 1, EnumDyeColor.RED.getDyeDamage()), new Object[] { new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.POPPY.getMeta()) });
/* 28 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 3, EnumDyeColor.WHITE.getDyeDamage()), new Object[] { Items.bone });
/* 29 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.PINK.getDyeDamage()), new Object[] { new ItemStack(Items.dye, 1, EnumDyeColor.RED.getDyeDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.WHITE.getDyeDamage()) });
/* 30 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.ORANGE.getDyeDamage()), new Object[] { new ItemStack(Items.dye, 1, EnumDyeColor.RED.getDyeDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.YELLOW.getDyeDamage()) });
/* 31 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.LIME.getDyeDamage()), new Object[] { new ItemStack(Items.dye, 1, EnumDyeColor.GREEN.getDyeDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.WHITE.getDyeDamage()) });
/* 32 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.GRAY.getDyeDamage()), new Object[] { new ItemStack(Items.dye, 1, EnumDyeColor.BLACK.getDyeDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.WHITE.getDyeDamage()) });
/* 33 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.SILVER.getDyeDamage()), new Object[] { new ItemStack(Items.dye, 1, EnumDyeColor.GRAY.getDyeDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.WHITE.getDyeDamage()) });
/* 34 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 3, EnumDyeColor.SILVER.getDyeDamage()), new Object[] { new ItemStack(Items.dye, 1, EnumDyeColor.BLACK.getDyeDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.WHITE.getDyeDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.WHITE.getDyeDamage()) });
/* 35 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.LIGHT_BLUE.getDyeDamage()), new Object[] { new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.WHITE.getDyeDamage()) });
/* 36 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.CYAN.getDyeDamage()), new Object[] { new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.GREEN.getDyeDamage()) });
/* 37 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.PURPLE.getDyeDamage()), new Object[] { new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.RED.getDyeDamage()) });
/* 38 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.MAGENTA.getDyeDamage()), new Object[] { new ItemStack(Items.dye, 1, EnumDyeColor.PURPLE.getDyeDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.PINK.getDyeDamage()) });
/* 39 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 3, EnumDyeColor.MAGENTA.getDyeDamage()), new Object[] { new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.RED.getDyeDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.PINK.getDyeDamage()) });
/* 40 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 4, EnumDyeColor.MAGENTA.getDyeDamage()), new Object[] { new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.RED.getDyeDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.RED.getDyeDamage()), new ItemStack(Items.dye, 1, EnumDyeColor.WHITE.getDyeDamage()) });
/* 41 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 1, EnumDyeColor.LIGHT_BLUE.getDyeDamage()), new Object[] { new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.BLUE_ORCHID.getMeta()) });
/* 42 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 1, EnumDyeColor.MAGENTA.getDyeDamage()), new Object[] { new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.ALLIUM.getMeta()) });
/* 43 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 1, EnumDyeColor.SILVER.getDyeDamage()), new Object[] { new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.HOUSTONIA.getMeta()) });
/* 44 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 1, EnumDyeColor.RED.getDyeDamage()), new Object[] { new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.RED_TULIP.getMeta()) });
/* 45 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 1, EnumDyeColor.ORANGE.getDyeDamage()), new Object[] { new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.ORANGE_TULIP.getMeta()) });
/* 46 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 1, EnumDyeColor.SILVER.getDyeDamage()), new Object[] { new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.WHITE_TULIP.getMeta()) });
/* 47 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 1, EnumDyeColor.PINK.getDyeDamage()), new Object[] { new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.PINK_TULIP.getMeta()) });
/* 48 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 1, EnumDyeColor.SILVER.getDyeDamage()), new Object[] { new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.OXEYE_DAISY.getMeta()) });
/* 49 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.YELLOW.getDyeDamage()), new Object[] { new ItemStack(Blocks.double_plant, 1, BlockDoublePlant.EnumPlantType.SUNFLOWER.getMeta()) });
/* 50 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.MAGENTA.getDyeDamage()), new Object[] { new ItemStack(Blocks.double_plant, 1, BlockDoublePlant.EnumPlantType.SYRINGA.getMeta()) });
/* 51 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.RED.getDyeDamage()), new Object[] { new ItemStack(Blocks.double_plant, 1, BlockDoublePlant.EnumPlantType.ROSE.getMeta()) });
/* 52 */     p_77607_1_.addShapelessRecipe(new ItemStack(Items.dye, 2, EnumDyeColor.PINK.getDyeDamage()), new Object[] { new ItemStack(Blocks.double_plant, 1, BlockDoublePlant.EnumPlantType.PAEONIA.getMeta()) });
/*    */     
/* 54 */     for (int j = 0; j < 16; j++)
/*    */     {
/* 56 */       p_77607_1_.addRecipe(new ItemStack(Blocks.carpet, 3, j), new Object[] { "##", Character.valueOf('#'), new ItemStack(Blocks.wool, 1, j) });
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\crafting\RecipesDyes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */