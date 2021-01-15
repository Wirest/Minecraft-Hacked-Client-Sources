/*     */ package net.minecraft.stats;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.JsonSerializableSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AchievementList
/*     */ {
/*     */   public static int minDisplayColumn;
/*     */   public static int minDisplayRow;
/*     */   public static int maxDisplayColumn;
/*     */   public static int maxDisplayRow;
/*  23 */   public static List<Achievement> achievementList = ;
/*     */   
/*     */ 
/*  26 */   public static Achievement openInventory = new Achievement("achievement.openInventory", "openInventory", 0, 0, Items.book, null).initIndependentStat().registerStat();
/*     */   
/*     */ 
/*  29 */   public static Achievement mineWood = new Achievement("achievement.mineWood", "mineWood", 2, 1, Blocks.log, openInventory).registerStat();
/*     */   
/*     */ 
/*  32 */   public static Achievement buildWorkBench = new Achievement("achievement.buildWorkBench", "buildWorkBench", 4, -1, Blocks.crafting_table, mineWood).registerStat();
/*     */   
/*     */ 
/*  35 */   public static Achievement buildPickaxe = new Achievement("achievement.buildPickaxe", "buildPickaxe", 4, 2, Items.wooden_pickaxe, buildWorkBench).registerStat();
/*     */   
/*     */ 
/*  38 */   public static Achievement buildFurnace = new Achievement("achievement.buildFurnace", "buildFurnace", 3, 4, Blocks.furnace, buildPickaxe).registerStat();
/*     */   
/*     */ 
/*  41 */   public static Achievement acquireIron = new Achievement("achievement.acquireIron", "acquireIron", 1, 4, Items.iron_ingot, buildFurnace).registerStat();
/*     */   
/*     */ 
/*  44 */   public static Achievement buildHoe = new Achievement("achievement.buildHoe", "buildHoe", 2, -3, Items.wooden_hoe, buildWorkBench).registerStat();
/*     */   
/*     */ 
/*  47 */   public static Achievement makeBread = new Achievement("achievement.makeBread", "makeBread", -1, -3, Items.bread, buildHoe).registerStat();
/*     */   
/*     */ 
/*  50 */   public static Achievement bakeCake = new Achievement("achievement.bakeCake", "bakeCake", 0, -5, Items.cake, buildHoe).registerStat();
/*     */   
/*     */ 
/*  53 */   public static Achievement buildBetterPickaxe = new Achievement("achievement.buildBetterPickaxe", "buildBetterPickaxe", 6, 2, Items.stone_pickaxe, buildPickaxe).registerStat();
/*     */   
/*     */ 
/*  56 */   public static Achievement cookFish = new Achievement("achievement.cookFish", "cookFish", 2, 6, Items.cooked_fish, buildFurnace).registerStat();
/*     */   
/*     */ 
/*  59 */   public static Achievement onARail = new Achievement("achievement.onARail", "onARail", 2, 3, Blocks.rail, acquireIron).setSpecial().registerStat();
/*     */   
/*     */ 
/*  62 */   public static Achievement buildSword = new Achievement("achievement.buildSword", "buildSword", 6, -1, Items.wooden_sword, buildWorkBench).registerStat();
/*     */   
/*     */ 
/*  65 */   public static Achievement killEnemy = new Achievement("achievement.killEnemy", "killEnemy", 8, -1, Items.bone, buildSword).registerStat();
/*     */   
/*     */ 
/*  68 */   public static Achievement killCow = new Achievement("achievement.killCow", "killCow", 7, -3, Items.leather, buildSword).registerStat();
/*     */   
/*     */ 
/*  71 */   public static Achievement flyPig = new Achievement("achievement.flyPig", "flyPig", 9, -3, Items.saddle, killCow).setSpecial().registerStat();
/*     */   
/*     */ 
/*  74 */   public static Achievement snipeSkeleton = new Achievement("achievement.snipeSkeleton", "snipeSkeleton", 7, 0, Items.bow, killEnemy).setSpecial().registerStat();
/*     */   
/*     */ 
/*  77 */   public static Achievement diamonds = new Achievement("achievement.diamonds", "diamonds", -1, 5, Blocks.diamond_ore, acquireIron).registerStat();
/*  78 */   public static Achievement diamondsToYou = new Achievement("achievement.diamondsToYou", "diamondsToYou", -1, 2, Items.diamond, diamonds).registerStat();
/*     */   
/*     */ 
/*  81 */   public static Achievement portal = new Achievement("achievement.portal", "portal", -1, 7, Blocks.obsidian, diamonds).registerStat();
/*     */   
/*     */ 
/*  84 */   public static Achievement ghast = new Achievement("achievement.ghast", "ghast", -4, 8, Items.ghast_tear, portal).setSpecial().registerStat();
/*     */   
/*     */ 
/*  87 */   public static Achievement blazeRod = new Achievement("achievement.blazeRod", "blazeRod", 0, 9, Items.blaze_rod, portal).registerStat();
/*     */   
/*     */ 
/*  90 */   public static Achievement potion = new Achievement("achievement.potion", "potion", 2, 8, Items.potionitem, blazeRod).registerStat();
/*     */   
/*     */ 
/*  93 */   public static Achievement theEnd = new Achievement("achievement.theEnd", "theEnd", 3, 10, Items.ender_eye, blazeRod).setSpecial().registerStat();
/*     */   
/*     */ 
/*  96 */   public static Achievement theEnd2 = new Achievement("achievement.theEnd2", "theEnd2", 4, 13, Blocks.dragon_egg, theEnd).setSpecial().registerStat();
/*     */   
/*     */ 
/*  99 */   public static Achievement enchantments = new Achievement("achievement.enchantments", "enchantments", -4, 4, Blocks.enchanting_table, diamonds).registerStat();
/* 100 */   public static Achievement overkill = new Achievement("achievement.overkill", "overkill", -4, 1, Items.diamond_sword, enchantments).setSpecial().registerStat();
/*     */   
/*     */ 
/* 103 */   public static Achievement bookcase = new Achievement("achievement.bookcase", "bookcase", -3, 6, Blocks.bookshelf, enchantments).registerStat();
/*     */   
/*     */ 
/* 106 */   public static Achievement breedCow = new Achievement("achievement.breedCow", "breedCow", 7, -5, Items.wheat, killCow).registerStat();
/*     */   
/*     */ 
/* 109 */   public static Achievement spawnWither = new Achievement("achievement.spawnWither", "spawnWither", 7, 12, new ItemStack(Items.skull, 1, 1), theEnd2).registerStat();
/*     */   
/*     */ 
/* 112 */   public static Achievement killWither = new Achievement("achievement.killWither", "killWither", 7, 10, Items.nether_star, spawnWither).registerStat();
/*     */   
/*     */ 
/* 115 */   public static Achievement fullBeacon = new Achievement("achievement.fullBeacon", "fullBeacon", 7, 8, Blocks.beacon, killWither).setSpecial().registerStat();
/*     */   
/*     */ 
/* 118 */   public static Achievement exploreAllBiomes = new Achievement("achievement.exploreAllBiomes", "exploreAllBiomes", 4, 8, Items.diamond_boots, theEnd).func_150953_b(JsonSerializableSet.class).setSpecial().registerStat();
/* 119 */   public static Achievement overpowered = new Achievement("achievement.overpowered", "overpowered", 6, 4, new ItemStack(Items.golden_apple, 1, 1), buildBetterPickaxe).setSpecial().registerStat();
/*     */   
/*     */   public static void init() {}
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\stats\AchievementList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */