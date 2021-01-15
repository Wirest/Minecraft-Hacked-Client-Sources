/*    */ package net.minecraft.item.crafting;
/*    */ 
/*    */ import net.minecraft.block.BlockDirt.DirtType;
/*    */ import net.minecraft.block.BlockPrismarine;
/*    */ import net.minecraft.block.BlockQuartz.EnumType;
/*    */ import net.minecraft.block.BlockRedSandstone.EnumType;
/*    */ import net.minecraft.block.BlockSand.EnumType;
/*    */ import net.minecraft.block.BlockSandStone.EnumType;
/*    */ import net.minecraft.block.BlockStone.EnumType;
/*    */ import net.minecraft.block.BlockStoneBrick;
/*    */ import net.minecraft.block.BlockStoneSlab.EnumType;
/*    */ import net.minecraft.block.BlockStoneSlabNew.EnumType;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.EnumDyeColor;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RecipesCrafting
/*    */ {
/*    */   public void addRecipes(CraftingManager p_77589_1_)
/*    */   {
/* 25 */     p_77589_1_.addRecipe(new ItemStack(Blocks.chest), new Object[] { "###", "# #", "###", Character.valueOf('#'), Blocks.planks });
/* 26 */     p_77589_1_.addRecipe(new ItemStack(Blocks.trapped_chest), new Object[] { "#-", Character.valueOf('#'), Blocks.chest, Character.valueOf('-'), Blocks.tripwire_hook });
/* 27 */     p_77589_1_.addRecipe(new ItemStack(Blocks.ender_chest), new Object[] { "###", "#E#", "###", Character.valueOf('#'), Blocks.obsidian, Character.valueOf('E'), Items.ender_eye });
/* 28 */     p_77589_1_.addRecipe(new ItemStack(Blocks.furnace), new Object[] { "###", "# #", "###", Character.valueOf('#'), Blocks.cobblestone });
/* 29 */     p_77589_1_.addRecipe(new ItemStack(Blocks.crafting_table), new Object[] { "##", "##", Character.valueOf('#'), Blocks.planks });
/* 30 */     p_77589_1_.addRecipe(new ItemStack(Blocks.sandstone), new Object[] { "##", "##", Character.valueOf('#'), new ItemStack(Blocks.sand, 1, BlockSand.EnumType.SAND.getMetadata()) });
/* 31 */     p_77589_1_.addRecipe(new ItemStack(Blocks.red_sandstone), new Object[] { "##", "##", Character.valueOf('#'), new ItemStack(Blocks.sand, 1, BlockSand.EnumType.RED_SAND.getMetadata()) });
/* 32 */     p_77589_1_.addRecipe(new ItemStack(Blocks.sandstone, 4, BlockSandStone.EnumType.SMOOTH.getMetadata()), new Object[] { "##", "##", Character.valueOf('#'), new ItemStack(Blocks.sandstone, 1, BlockSandStone.EnumType.DEFAULT.getMetadata()) });
/* 33 */     p_77589_1_.addRecipe(new ItemStack(Blocks.red_sandstone, 4, BlockRedSandstone.EnumType.SMOOTH.getMetadata()), new Object[] { "##", "##", Character.valueOf('#'), new ItemStack(Blocks.red_sandstone, 1, BlockRedSandstone.EnumType.DEFAULT.getMetadata()) });
/* 34 */     p_77589_1_.addRecipe(new ItemStack(Blocks.sandstone, 1, BlockSandStone.EnumType.CHISELED.getMetadata()), new Object[] { "#", "#", Character.valueOf('#'), new ItemStack(Blocks.stone_slab, 1, BlockStoneSlab.EnumType.SAND.getMetadata()) });
/* 35 */     p_77589_1_.addRecipe(new ItemStack(Blocks.red_sandstone, 1, BlockRedSandstone.EnumType.CHISELED.getMetadata()), new Object[] { "#", "#", Character.valueOf('#'), new ItemStack(Blocks.stone_slab2, 1, BlockStoneSlabNew.EnumType.RED_SANDSTONE.getMetadata()) });
/* 36 */     p_77589_1_.addRecipe(new ItemStack(Blocks.quartz_block, 1, BlockQuartz.EnumType.CHISELED.getMetadata()), new Object[] { "#", "#", Character.valueOf('#'), new ItemStack(Blocks.stone_slab, 1, BlockStoneSlab.EnumType.QUARTZ.getMetadata()) });
/* 37 */     p_77589_1_.addRecipe(new ItemStack(Blocks.quartz_block, 2, BlockQuartz.EnumType.LINES_Y.getMetadata()), new Object[] { "#", "#", Character.valueOf('#'), new ItemStack(Blocks.quartz_block, 1, BlockQuartz.EnumType.DEFAULT.getMetadata()) });
/* 38 */     p_77589_1_.addRecipe(new ItemStack(Blocks.stonebrick, 4), new Object[] { "##", "##", Character.valueOf('#'), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.STONE.getMetadata()) });
/* 39 */     p_77589_1_.addRecipe(new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.CHISELED_META), new Object[] { "#", "#", Character.valueOf('#'), new ItemStack(Blocks.stone_slab, 1, BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()) });
/* 40 */     p_77589_1_.addShapelessRecipe(new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.MOSSY_META), new Object[] { Blocks.stonebrick, Blocks.vine });
/* 41 */     p_77589_1_.addShapelessRecipe(new ItemStack(Blocks.mossy_cobblestone, 1), new Object[] { Blocks.cobblestone, Blocks.vine });
/* 42 */     p_77589_1_.addRecipe(new ItemStack(Blocks.iron_bars, 16), new Object[] { "###", "###", Character.valueOf('#'), Items.iron_ingot });
/* 43 */     p_77589_1_.addRecipe(new ItemStack(Blocks.glass_pane, 16), new Object[] { "###", "###", Character.valueOf('#'), Blocks.glass });
/* 44 */     p_77589_1_.addRecipe(new ItemStack(Blocks.redstone_lamp, 1), new Object[] { " R ", "RGR", " R ", Character.valueOf('R'), Items.redstone, Character.valueOf('G'), Blocks.glowstone });
/* 45 */     p_77589_1_.addRecipe(new ItemStack(Blocks.beacon, 1), new Object[] { "GGG", "GSG", "OOO", Character.valueOf('G'), Blocks.glass, Character.valueOf('S'), Items.nether_star, Character.valueOf('O'), Blocks.obsidian });
/* 46 */     p_77589_1_.addRecipe(new ItemStack(Blocks.nether_brick, 1), new Object[] { "NN", "NN", Character.valueOf('N'), Items.netherbrick });
/* 47 */     p_77589_1_.addRecipe(new ItemStack(Blocks.stone, 2, BlockStone.EnumType.DIORITE.getMetadata()), new Object[] { "CQ", "QC", Character.valueOf('C'), Blocks.cobblestone, Character.valueOf('Q'), Items.quartz });
/* 48 */     p_77589_1_.addShapelessRecipe(new ItemStack(Blocks.stone, 1, BlockStone.EnumType.GRANITE.getMetadata()), new Object[] { new ItemStack(Blocks.stone, 1, BlockStone.EnumType.DIORITE.getMetadata()), Items.quartz });
/* 49 */     p_77589_1_.addShapelessRecipe(new ItemStack(Blocks.stone, 2, BlockStone.EnumType.ANDESITE.getMetadata()), new Object[] { new ItemStack(Blocks.stone, 1, BlockStone.EnumType.DIORITE.getMetadata()), Blocks.cobblestone });
/* 50 */     p_77589_1_.addRecipe(new ItemStack(Blocks.dirt, 4, BlockDirt.DirtType.COARSE_DIRT.getMetadata()), new Object[] { "DG", "GD", Character.valueOf('D'), new ItemStack(Blocks.dirt, 1, BlockDirt.DirtType.DIRT.getMetadata()), Character.valueOf('G'), Blocks.gravel });
/* 51 */     p_77589_1_.addRecipe(new ItemStack(Blocks.stone, 4, BlockStone.EnumType.DIORITE_SMOOTH.getMetadata()), new Object[] { "SS", "SS", Character.valueOf('S'), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.DIORITE.getMetadata()) });
/* 52 */     p_77589_1_.addRecipe(new ItemStack(Blocks.stone, 4, BlockStone.EnumType.GRANITE_SMOOTH.getMetadata()), new Object[] { "SS", "SS", Character.valueOf('S'), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.GRANITE.getMetadata()) });
/* 53 */     p_77589_1_.addRecipe(new ItemStack(Blocks.stone, 4, BlockStone.EnumType.ANDESITE_SMOOTH.getMetadata()), new Object[] { "SS", "SS", Character.valueOf('S'), new ItemStack(Blocks.stone, 1, BlockStone.EnumType.ANDESITE.getMetadata()) });
/* 54 */     p_77589_1_.addRecipe(new ItemStack(Blocks.prismarine, 1, BlockPrismarine.ROUGH_META), new Object[] { "SS", "SS", Character.valueOf('S'), Items.prismarine_shard });
/* 55 */     p_77589_1_.addRecipe(new ItemStack(Blocks.prismarine, 1, BlockPrismarine.BRICKS_META), new Object[] { "SSS", "SSS", "SSS", Character.valueOf('S'), Items.prismarine_shard });
/* 56 */     p_77589_1_.addRecipe(new ItemStack(Blocks.prismarine, 1, BlockPrismarine.DARK_META), new Object[] { "SSS", "SIS", "SSS", Character.valueOf('S'), Items.prismarine_shard, Character.valueOf('I'), new ItemStack(Items.dye, 1, EnumDyeColor.BLACK.getDyeDamage()) });
/* 57 */     p_77589_1_.addRecipe(new ItemStack(Blocks.sea_lantern, 1, 0), new Object[] { "SCS", "CCC", "SCS", Character.valueOf('S'), Items.prismarine_shard, Character.valueOf('C'), Items.prismarine_crystals });
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\crafting\RecipesCrafting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */