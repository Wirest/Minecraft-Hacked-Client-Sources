package net.minecraft.stats;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonSerializableSet;

public class AchievementList
{
    /** Is the smallest column used to display a achievement on the GUI. */
    public static int minDisplayColumn;

    /** Is the smallest row used to display a achievement on the GUI. */
    public static int minDisplayRow;

    /** Is the biggest column used to display a achievement on the GUI. */
    public static int maxDisplayColumn;

    /** Is the biggest row used to display a achievement on the GUI. */
    public static int maxDisplayRow;

    /** The list holding all achievements */
    public static List achievementList = Lists.newArrayList();

    /** Is the 'open inventory' achievement. */
    public static Achievement openInventory = (new Achievement("achievement.openInventory", "openInventory", 0, 0, Items.book, (Achievement)null)).setIndependent().registerAchievement();

    /** Is the 'getting wood' achievement. */
    public static Achievement mineWood = (new Achievement("achievement.mineWood", "mineWood", 2, 1, Blocks.log, openInventory)).registerAchievement();

    /** Is the 'benchmarking' achievement. */
    public static Achievement buildWorkBench = (new Achievement("achievement.buildWorkBench", "buildWorkBench", 4, -1, Blocks.crafting_table, mineWood)).registerAchievement();

    /** Is the 'time to mine' achievement. */
    public static Achievement buildPickaxe = (new Achievement("achievement.buildPickaxe", "buildPickaxe", 4, 2, Items.wooden_pickaxe, buildWorkBench)).registerAchievement();

    /** Is the 'hot topic' achievement. */
    public static Achievement buildFurnace = (new Achievement("achievement.buildFurnace", "buildFurnace", 3, 4, Blocks.furnace, buildPickaxe)).registerAchievement();

    /** Is the 'acquire hardware' achievement. */
    public static Achievement acquireIron = (new Achievement("achievement.acquireIron", "acquireIron", 1, 4, Items.iron_ingot, buildFurnace)).registerAchievement();

    /** Is the 'time to farm' achievement. */
    public static Achievement buildHoe = (new Achievement("achievement.buildHoe", "buildHoe", 2, -3, Items.wooden_hoe, buildWorkBench)).registerAchievement();

    /** Is the 'bake bread' achievement. */
    public static Achievement makeBread = (new Achievement("achievement.makeBread", "makeBread", -1, -3, Items.bread, buildHoe)).registerAchievement();

    /** Is the 'the lie' achievement. */
    public static Achievement bakeCake = (new Achievement("achievement.bakeCake", "bakeCake", 0, -5, Items.cake, buildHoe)).registerAchievement();

    /** Is the 'getting a upgrade' achievement. */
    public static Achievement buildBetterPickaxe = (new Achievement("achievement.buildBetterPickaxe", "buildBetterPickaxe", 6, 2, Items.stone_pickaxe, buildPickaxe)).registerAchievement();

    /** Is the 'delicious fish' achievement. */
    public static Achievement cookFish = (new Achievement("achievement.cookFish", "cookFish", 2, 6, Items.cooked_fish, buildFurnace)).registerAchievement();

    /** Is the 'on a rail' achievement */
    public static Achievement onARail = (new Achievement("achievement.onARail", "onARail", 2, 3, Blocks.rail, acquireIron)).setSpecial().registerAchievement();

    /** Is the 'time to strike' achievement. */
    public static Achievement buildSword = (new Achievement("achievement.buildSword", "buildSword", 6, -1, Items.wooden_sword, buildWorkBench)).registerAchievement();

    /** Is the 'monster hunter' achievement. */
    public static Achievement killEnemy = (new Achievement("achievement.killEnemy", "killEnemy", 8, -1, Items.bone, buildSword)).registerAchievement();

    /** is the 'cow tipper' achievement. */
    public static Achievement killCow = (new Achievement("achievement.killCow", "killCow", 7, -3, Items.leather, buildSword)).registerAchievement();

    /** Is the 'when pig fly' achievement. */
    public static Achievement flyPig = (new Achievement("achievement.flyPig", "flyPig", 9, -3, Items.saddle, killCow)).setSpecial().registerAchievement();

    /** The achievement for killing a Skeleton from 50 meters aways. */
    public static Achievement snipeSkeleton = (new Achievement("achievement.snipeSkeleton", "snipeSkeleton", 7, 0, Items.bow, killEnemy)).setSpecial().registerAchievement();

    /** Is the 'DIAMONDS!' achievement */
    public static Achievement diamonds = (new Achievement("achievement.diamonds", "diamonds", -1, 5, Blocks.diamond_ore, acquireIron)).registerAchievement();
    public static Achievement diamondsToYou = (new Achievement("achievement.diamondsToYou", "diamondsToYou", -1, 2, Items.diamond, diamonds)).registerAchievement();

    /** Is the 'We Need to Go Deeper' achievement */
    public static Achievement portal = (new Achievement("achievement.portal", "portal", -1, 7, Blocks.obsidian, diamonds)).registerAchievement();

    /** Is the 'Return to Sender' achievement */
    public static Achievement ghast = (new Achievement("achievement.ghast", "ghast", -4, 8, Items.ghast_tear, portal)).setSpecial().registerAchievement();

    /** Is the 'Into Fire' achievement */
    public static Achievement blazeRod = (new Achievement("achievement.blazeRod", "blazeRod", 0, 9, Items.blaze_rod, portal)).registerAchievement();

    /** Is the 'Local Brewery' achievement */
    public static Achievement potion = (new Achievement("achievement.potion", "potion", 2, 8, Items.potionitem, blazeRod)).registerAchievement();

    /** Is the 'The End?' achievement */
    public static Achievement theEnd = (new Achievement("achievement.theEnd", "theEnd", 3, 10, Items.ender_eye, blazeRod)).setSpecial().registerAchievement();

    /** Is the 'The End.' achievement */
    public static Achievement theEnd2 = (new Achievement("achievement.theEnd2", "theEnd2", 4, 13, Blocks.dragon_egg, theEnd)).setSpecial().registerAchievement();

    /** Is the 'Enchanter' achievement */
    public static Achievement enchantments = (new Achievement("achievement.enchantments", "enchantments", -4, 4, Blocks.enchanting_table, diamonds)).registerAchievement();
    public static Achievement overkill = (new Achievement("achievement.overkill", "overkill", -4, 1, Items.diamond_sword, enchantments)).setSpecial().registerAchievement();

    /** Is the 'Librarian' achievement */
    public static Achievement bookcase = (new Achievement("achievement.bookcase", "bookcase", -3, 6, Blocks.bookshelf, enchantments)).registerAchievement();

    /** Is the 'Repopulation' achievement */
    public static Achievement breedCow = (new Achievement("achievement.breedCow", "breedCow", 7, -5, Items.wheat, killCow)).registerAchievement();

    /** Is the 'The Beginning?' achievement */
    public static Achievement spawnWither = (new Achievement("achievement.spawnWither", "spawnWither", 7, 12, new ItemStack(Items.skull, 1, 1), theEnd2)).registerAchievement();

    /** Is the 'The Beginning.' achievement */
    public static Achievement killWither = (new Achievement("achievement.killWither", "killWither", 7, 10, Items.nether_star, spawnWither)).registerAchievement();

    /** Is the 'Beaconator' achievement */
    public static Achievement fullBeacon = (new Achievement("achievement.fullBeacon", "fullBeacon", 7, 8, Blocks.beacon, killWither)).setSpecial().registerAchievement();

    /** Is the 'Adventuring Time' achievement */
    public static Achievement exploreAllBiomes = (new Achievement("achievement.exploreAllBiomes", "exploreAllBiomes", 4, 8, Items.diamond_boots, theEnd)).func_180787_a(JsonSerializableSet.class).setSpecial().registerAchievement();
    public static Achievement overpowered = (new Achievement("achievement.overpowered", "overpowered", 6, 4, Items.golden_apple, buildBetterPickaxe)).setSpecial().registerAchievement();

    /**
     * A stub functions called to make the static initializer for this class run.
     */
    public static void init() {}
}
