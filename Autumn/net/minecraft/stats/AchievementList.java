package net.minecraft.stats;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonSerializableSet;

public class AchievementList {
   public static int minDisplayColumn;
   public static int minDisplayRow;
   public static int maxDisplayColumn;
   public static int maxDisplayRow;
   public static List achievementList = Lists.newArrayList();
   public static Achievement openInventory;
   public static Achievement mineWood;
   public static Achievement buildWorkBench;
   public static Achievement buildPickaxe;
   public static Achievement buildFurnace;
   public static Achievement acquireIron;
   public static Achievement buildHoe;
   public static Achievement makeBread;
   public static Achievement bakeCake;
   public static Achievement buildBetterPickaxe;
   public static Achievement cookFish;
   public static Achievement onARail;
   public static Achievement buildSword;
   public static Achievement killEnemy;
   public static Achievement killCow;
   public static Achievement flyPig;
   public static Achievement snipeSkeleton;
   public static Achievement diamonds;
   public static Achievement diamondsToYou;
   public static Achievement portal;
   public static Achievement ghast;
   public static Achievement blazeRod;
   public static Achievement potion;
   public static Achievement theEnd;
   public static Achievement theEnd2;
   public static Achievement enchantments;
   public static Achievement overkill;
   public static Achievement bookcase;
   public static Achievement breedCow;
   public static Achievement spawnWither;
   public static Achievement killWither;
   public static Achievement fullBeacon;
   public static Achievement exploreAllBiomes;
   public static Achievement overpowered;

   public static void init() {
   }

   static {
      openInventory = (new Achievement("achievement.openInventory", "openInventory", 0, 0, Items.book, (Achievement)null)).initIndependentStat().registerStat();
      mineWood = (new Achievement("achievement.mineWood", "mineWood", 2, 1, Blocks.log, openInventory)).registerStat();
      buildWorkBench = (new Achievement("achievement.buildWorkBench", "buildWorkBench", 4, -1, Blocks.crafting_table, mineWood)).registerStat();
      buildPickaxe = (new Achievement("achievement.buildPickaxe", "buildPickaxe", 4, 2, Items.wooden_pickaxe, buildWorkBench)).registerStat();
      buildFurnace = (new Achievement("achievement.buildFurnace", "buildFurnace", 3, 4, Blocks.furnace, buildPickaxe)).registerStat();
      acquireIron = (new Achievement("achievement.acquireIron", "acquireIron", 1, 4, Items.iron_ingot, buildFurnace)).registerStat();
      buildHoe = (new Achievement("achievement.buildHoe", "buildHoe", 2, -3, Items.wooden_hoe, buildWorkBench)).registerStat();
      makeBread = (new Achievement("achievement.makeBread", "makeBread", -1, -3, Items.bread, buildHoe)).registerStat();
      bakeCake = (new Achievement("achievement.bakeCake", "bakeCake", 0, -5, Items.cake, buildHoe)).registerStat();
      buildBetterPickaxe = (new Achievement("achievement.buildBetterPickaxe", "buildBetterPickaxe", 6, 2, Items.stone_pickaxe, buildPickaxe)).registerStat();
      cookFish = (new Achievement("achievement.cookFish", "cookFish", 2, 6, Items.cooked_fish, buildFurnace)).registerStat();
      onARail = (new Achievement("achievement.onARail", "onARail", 2, 3, Blocks.rail, acquireIron)).setSpecial().registerStat();
      buildSword = (new Achievement("achievement.buildSword", "buildSword", 6, -1, Items.wooden_sword, buildWorkBench)).registerStat();
      killEnemy = (new Achievement("achievement.killEnemy", "killEnemy", 8, -1, Items.bone, buildSword)).registerStat();
      killCow = (new Achievement("achievement.killCow", "killCow", 7, -3, Items.leather, buildSword)).registerStat();
      flyPig = (new Achievement("achievement.flyPig", "flyPig", 9, -3, Items.saddle, killCow)).setSpecial().registerStat();
      snipeSkeleton = (new Achievement("achievement.snipeSkeleton", "snipeSkeleton", 7, 0, Items.bow, killEnemy)).setSpecial().registerStat();
      diamonds = (new Achievement("achievement.diamonds", "diamonds", -1, 5, Blocks.diamond_ore, acquireIron)).registerStat();
      diamondsToYou = (new Achievement("achievement.diamondsToYou", "diamondsToYou", -1, 2, Items.diamond, diamonds)).registerStat();
      portal = (new Achievement("achievement.portal", "portal", -1, 7, Blocks.obsidian, diamonds)).registerStat();
      ghast = (new Achievement("achievement.ghast", "ghast", -4, 8, Items.ghast_tear, portal)).setSpecial().registerStat();
      blazeRod = (new Achievement("achievement.blazeRod", "blazeRod", 0, 9, Items.blaze_rod, portal)).registerStat();
      potion = (new Achievement("achievement.potion", "potion", 2, 8, Items.potionitem, blazeRod)).registerStat();
      theEnd = (new Achievement("achievement.theEnd", "theEnd", 3, 10, Items.ender_eye, blazeRod)).setSpecial().registerStat();
      theEnd2 = (new Achievement("achievement.theEnd2", "theEnd2", 4, 13, Blocks.dragon_egg, theEnd)).setSpecial().registerStat();
      enchantments = (new Achievement("achievement.enchantments", "enchantments", -4, 4, Blocks.enchanting_table, diamonds)).registerStat();
      overkill = (new Achievement("achievement.overkill", "overkill", -4, 1, Items.diamond_sword, enchantments)).setSpecial().registerStat();
      bookcase = (new Achievement("achievement.bookcase", "bookcase", -3, 6, Blocks.bookshelf, enchantments)).registerStat();
      breedCow = (new Achievement("achievement.breedCow", "breedCow", 7, -5, Items.wheat, killCow)).registerStat();
      spawnWither = (new Achievement("achievement.spawnWither", "spawnWither", 7, 12, new ItemStack(Items.skull, 1, 1), theEnd2)).registerStat();
      killWither = (new Achievement("achievement.killWither", "killWither", 7, 10, Items.nether_star, spawnWither)).registerStat();
      fullBeacon = (new Achievement("achievement.fullBeacon", "fullBeacon", 7, 8, Blocks.beacon, killWither)).setSpecial().registerStat();
      exploreAllBiomes = (new Achievement("achievement.exploreAllBiomes", "exploreAllBiomes", 4, 8, Items.diamond_boots, theEnd)).func_150953_b(JsonSerializableSet.class).setSpecial().registerStat();
      overpowered = (new Achievement("achievement.overpowered", "overpowered", 6, 4, new ItemStack(Items.golden_apple, 1, 1), buildBetterPickaxe)).setSpecial().registerStat();
   }
}
