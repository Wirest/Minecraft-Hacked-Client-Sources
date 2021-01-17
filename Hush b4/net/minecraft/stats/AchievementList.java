// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.stats;

import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.JsonSerializableSet;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import com.google.common.collect.Lists;
import java.util.List;

public class AchievementList
{
    public static int minDisplayColumn;
    public static int minDisplayRow;
    public static int maxDisplayColumn;
    public static int maxDisplayRow;
    public static List<Achievement> achievementList;
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
    
    static {
        AchievementList.achievementList = (List<Achievement>)Lists.newArrayList();
        AchievementList.openInventory = new Achievement("achievement.openInventory", "openInventory", 0, 0, Items.book, null).initIndependentStat().registerStat();
        AchievementList.mineWood = new Achievement("achievement.mineWood", "mineWood", 2, 1, Blocks.log, AchievementList.openInventory).registerStat();
        AchievementList.buildWorkBench = new Achievement("achievement.buildWorkBench", "buildWorkBench", 4, -1, Blocks.crafting_table, AchievementList.mineWood).registerStat();
        AchievementList.buildPickaxe = new Achievement("achievement.buildPickaxe", "buildPickaxe", 4, 2, Items.wooden_pickaxe, AchievementList.buildWorkBench).registerStat();
        AchievementList.buildFurnace = new Achievement("achievement.buildFurnace", "buildFurnace", 3, 4, Blocks.furnace, AchievementList.buildPickaxe).registerStat();
        AchievementList.acquireIron = new Achievement("achievement.acquireIron", "acquireIron", 1, 4, Items.iron_ingot, AchievementList.buildFurnace).registerStat();
        AchievementList.buildHoe = new Achievement("achievement.buildHoe", "buildHoe", 2, -3, Items.wooden_hoe, AchievementList.buildWorkBench).registerStat();
        AchievementList.makeBread = new Achievement("achievement.makeBread", "makeBread", -1, -3, Items.bread, AchievementList.buildHoe).registerStat();
        AchievementList.bakeCake = new Achievement("achievement.bakeCake", "bakeCake", 0, -5, Items.cake, AchievementList.buildHoe).registerStat();
        AchievementList.buildBetterPickaxe = new Achievement("achievement.buildBetterPickaxe", "buildBetterPickaxe", 6, 2, Items.stone_pickaxe, AchievementList.buildPickaxe).registerStat();
        AchievementList.cookFish = new Achievement("achievement.cookFish", "cookFish", 2, 6, Items.cooked_fish, AchievementList.buildFurnace).registerStat();
        AchievementList.onARail = new Achievement("achievement.onARail", "onARail", 2, 3, Blocks.rail, AchievementList.acquireIron).setSpecial().registerStat();
        AchievementList.buildSword = new Achievement("achievement.buildSword", "buildSword", 6, -1, Items.wooden_sword, AchievementList.buildWorkBench).registerStat();
        AchievementList.killEnemy = new Achievement("achievement.killEnemy", "killEnemy", 8, -1, Items.bone, AchievementList.buildSword).registerStat();
        AchievementList.killCow = new Achievement("achievement.killCow", "killCow", 7, -3, Items.leather, AchievementList.buildSword).registerStat();
        AchievementList.flyPig = new Achievement("achievement.flyPig", "flyPig", 9, -3, Items.saddle, AchievementList.killCow).setSpecial().registerStat();
        AchievementList.snipeSkeleton = new Achievement("achievement.snipeSkeleton", "snipeSkeleton", 7, 0, Items.bow, AchievementList.killEnemy).setSpecial().registerStat();
        AchievementList.diamonds = new Achievement("achievement.diamonds", "diamonds", -1, 5, Blocks.diamond_ore, AchievementList.acquireIron).registerStat();
        AchievementList.diamondsToYou = new Achievement("achievement.diamondsToYou", "diamondsToYou", -1, 2, Items.diamond, AchievementList.diamonds).registerStat();
        AchievementList.portal = new Achievement("achievement.portal", "portal", -1, 7, Blocks.obsidian, AchievementList.diamonds).registerStat();
        AchievementList.ghast = new Achievement("achievement.ghast", "ghast", -4, 8, Items.ghast_tear, AchievementList.portal).setSpecial().registerStat();
        AchievementList.blazeRod = new Achievement("achievement.blazeRod", "blazeRod", 0, 9, Items.blaze_rod, AchievementList.portal).registerStat();
        AchievementList.potion = new Achievement("achievement.potion", "potion", 2, 8, Items.potionitem, AchievementList.blazeRod).registerStat();
        AchievementList.theEnd = new Achievement("achievement.theEnd", "theEnd", 3, 10, Items.ender_eye, AchievementList.blazeRod).setSpecial().registerStat();
        AchievementList.theEnd2 = new Achievement("achievement.theEnd2", "theEnd2", 4, 13, Blocks.dragon_egg, AchievementList.theEnd).setSpecial().registerStat();
        AchievementList.enchantments = new Achievement("achievement.enchantments", "enchantments", -4, 4, Blocks.enchanting_table, AchievementList.diamonds).registerStat();
        AchievementList.overkill = new Achievement("achievement.overkill", "overkill", -4, 1, Items.diamond_sword, AchievementList.enchantments).setSpecial().registerStat();
        AchievementList.bookcase = new Achievement("achievement.bookcase", "bookcase", -3, 6, Blocks.bookshelf, AchievementList.enchantments).registerStat();
        AchievementList.breedCow = new Achievement("achievement.breedCow", "breedCow", 7, -5, Items.wheat, AchievementList.killCow).registerStat();
        AchievementList.spawnWither = new Achievement("achievement.spawnWither", "spawnWither", 7, 12, new ItemStack(Items.skull, 1, 1), AchievementList.theEnd2).registerStat();
        AchievementList.killWither = new Achievement("achievement.killWither", "killWither", 7, 10, Items.nether_star, AchievementList.spawnWither).registerStat();
        AchievementList.fullBeacon = new Achievement("achievement.fullBeacon", "fullBeacon", 7, 8, Blocks.beacon, AchievementList.killWither).setSpecial().registerStat();
        AchievementList.exploreAllBiomes = new Achievement("achievement.exploreAllBiomes", "exploreAllBiomes", 4, 8, Items.diamond_boots, AchievementList.theEnd).func_150953_b(JsonSerializableSet.class).setSpecial().registerStat();
        AchievementList.overpowered = new Achievement("achievement.overpowered", "overpowered", 6, 4, new ItemStack(Items.golden_apple, 1, 1), AchievementList.buildBetterPickaxe).setSpecial().registerStat();
    }
    
    public static void init() {
    }
}
