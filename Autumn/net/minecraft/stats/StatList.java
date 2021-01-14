package net.minecraft.stats;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ResourceLocation;

public class StatList {
   protected static Map oneShotStats = Maps.newHashMap();
   public static List allStats = Lists.newArrayList();
   public static List generalStats = Lists.newArrayList();
   public static List itemStats = Lists.newArrayList();
   public static List objectMineStats = Lists.newArrayList();
   public static StatBase leaveGameStat = (new StatBasic("stat.leaveGame", new ChatComponentTranslation("stat.leaveGame", new Object[0]))).initIndependentStat().registerStat();
   public static StatBase minutesPlayedStat;
   public static StatBase timeSinceDeathStat;
   public static StatBase distanceWalkedStat;
   public static StatBase distanceCrouchedStat;
   public static StatBase distanceSprintedStat;
   public static StatBase distanceSwumStat;
   public static StatBase distanceFallenStat;
   public static StatBase distanceClimbedStat;
   public static StatBase distanceFlownStat;
   public static StatBase distanceDoveStat;
   public static StatBase distanceByMinecartStat;
   public static StatBase distanceByBoatStat;
   public static StatBase distanceByPigStat;
   public static StatBase distanceByHorseStat;
   public static StatBase jumpStat;
   public static StatBase dropStat;
   public static StatBase damageDealtStat;
   public static StatBase damageTakenStat;
   public static StatBase deathsStat;
   public static StatBase mobKillsStat;
   public static StatBase animalsBredStat;
   public static StatBase playerKillsStat;
   public static StatBase fishCaughtStat;
   public static StatBase junkFishedStat;
   public static StatBase treasureFishedStat;
   public static StatBase timesTalkedToVillagerStat;
   public static StatBase timesTradedWithVillagerStat;
   public static StatBase field_181724_H;
   public static StatBase field_181725_I;
   public static StatBase field_181726_J;
   public static StatBase field_181727_K;
   public static StatBase field_181728_L;
   public static StatBase field_181729_M;
   public static StatBase field_181730_N;
   public static StatBase field_181731_O;
   public static StatBase field_181732_P;
   public static StatBase field_181733_Q;
   public static StatBase field_181734_R;
   public static StatBase field_181735_S;
   public static StatBase field_181736_T;
   public static StatBase field_181737_U;
   public static StatBase field_181738_V;
   public static StatBase field_181739_W;
   public static StatBase field_181740_X;
   public static StatBase field_181741_Y;
   public static StatBase field_181742_Z;
   public static StatBase field_181723_aa;
   public static final StatBase[] mineBlockStatArray;
   public static final StatBase[] objectCraftStats;
   public static final StatBase[] objectUseStats;
   public static final StatBase[] objectBreakStats;

   public static void init() {
      initMiningStats();
      initStats();
      initItemDepleteStats();
      initCraftableStats();
      AchievementList.init();
      EntityList.func_151514_a();
   }

   private static void initCraftableStats() {
      Set set = Sets.newHashSet();
      Iterator var1 = CraftingManager.getInstance().getRecipeList().iterator();

      while(var1.hasNext()) {
         IRecipe irecipe = (IRecipe)var1.next();
         if (irecipe.getRecipeOutput() != null) {
            set.add(irecipe.getRecipeOutput().getItem());
         }
      }

      var1 = FurnaceRecipes.instance().getSmeltingList().values().iterator();

      while(var1.hasNext()) {
         ItemStack itemstack = (ItemStack)var1.next();
         set.add(itemstack.getItem());
      }

      var1 = set.iterator();

      while(var1.hasNext()) {
         Item item = (Item)var1.next();
         if (item != null) {
            int i = Item.getIdFromItem(item);
            String s = func_180204_a(item);
            if (s != null) {
               objectCraftStats[i] = (new StatCrafting("stat.craftItem.", s, new ChatComponentTranslation("stat.craftItem", new Object[]{(new ItemStack(item)).getChatComponent()}), item)).registerStat();
            }
         }
      }

      replaceAllSimilarBlocks(objectCraftStats);
   }

   private static void initMiningStats() {
      Iterator var0 = Block.blockRegistry.iterator();

      while(var0.hasNext()) {
         Block block = (Block)var0.next();
         Item item = Item.getItemFromBlock(block);
         if (item != null) {
            int i = Block.getIdFromBlock(block);
            String s = func_180204_a(item);
            if (s != null && block.getEnableStats()) {
               mineBlockStatArray[i] = (new StatCrafting("stat.mineBlock.", s, new ChatComponentTranslation("stat.mineBlock", new Object[]{(new ItemStack(block)).getChatComponent()}), item)).registerStat();
               objectMineStats.add((StatCrafting)mineBlockStatArray[i]);
            }
         }
      }

      replaceAllSimilarBlocks(mineBlockStatArray);
   }

   private static void initStats() {
      Iterator var0 = Item.itemRegistry.iterator();

      while(var0.hasNext()) {
         Item item = (Item)var0.next();
         if (item != null) {
            int i = Item.getIdFromItem(item);
            String s = func_180204_a(item);
            if (s != null) {
               objectUseStats[i] = (new StatCrafting("stat.useItem.", s, new ChatComponentTranslation("stat.useItem", new Object[]{(new ItemStack(item)).getChatComponent()}), item)).registerStat();
               if (!(item instanceof ItemBlock)) {
                  itemStats.add((StatCrafting)objectUseStats[i]);
               }
            }
         }
      }

      replaceAllSimilarBlocks(objectUseStats);
   }

   private static void initItemDepleteStats() {
      Iterator var0 = Item.itemRegistry.iterator();

      while(var0.hasNext()) {
         Item item = (Item)var0.next();
         if (item != null) {
            int i = Item.getIdFromItem(item);
            String s = func_180204_a(item);
            if (s != null && item.isDamageable()) {
               objectBreakStats[i] = (new StatCrafting("stat.breakItem.", s, new ChatComponentTranslation("stat.breakItem", new Object[]{(new ItemStack(item)).getChatComponent()}), item)).registerStat();
            }
         }
      }

      replaceAllSimilarBlocks(objectBreakStats);
   }

   private static String func_180204_a(Item p_180204_0_) {
      ResourceLocation resourcelocation = (ResourceLocation)Item.itemRegistry.getNameForObject(p_180204_0_);
      return resourcelocation != null ? resourcelocation.toString().replace(':', '.') : null;
   }

   private static void replaceAllSimilarBlocks(StatBase[] p_75924_0_) {
      mergeStatBases(p_75924_0_, Blocks.water, Blocks.flowing_water);
      mergeStatBases(p_75924_0_, Blocks.lava, Blocks.flowing_lava);
      mergeStatBases(p_75924_0_, Blocks.lit_pumpkin, Blocks.pumpkin);
      mergeStatBases(p_75924_0_, Blocks.lit_furnace, Blocks.furnace);
      mergeStatBases(p_75924_0_, Blocks.lit_redstone_ore, Blocks.redstone_ore);
      mergeStatBases(p_75924_0_, Blocks.powered_repeater, Blocks.unpowered_repeater);
      mergeStatBases(p_75924_0_, Blocks.powered_comparator, Blocks.unpowered_comparator);
      mergeStatBases(p_75924_0_, Blocks.redstone_torch, Blocks.unlit_redstone_torch);
      mergeStatBases(p_75924_0_, Blocks.lit_redstone_lamp, Blocks.redstone_lamp);
      mergeStatBases(p_75924_0_, Blocks.double_stone_slab, Blocks.stone_slab);
      mergeStatBases(p_75924_0_, Blocks.double_wooden_slab, Blocks.wooden_slab);
      mergeStatBases(p_75924_0_, Blocks.double_stone_slab2, Blocks.stone_slab2);
      mergeStatBases(p_75924_0_, Blocks.grass, Blocks.dirt);
      mergeStatBases(p_75924_0_, Blocks.farmland, Blocks.dirt);
   }

   private static void mergeStatBases(StatBase[] statBaseIn, Block p_151180_1_, Block p_151180_2_) {
      int i = Block.getIdFromBlock(p_151180_1_);
      int j = Block.getIdFromBlock(p_151180_2_);
      if (statBaseIn[i] != null && statBaseIn[j] == null) {
         statBaseIn[j] = statBaseIn[i];
      } else {
         allStats.remove(statBaseIn[i]);
         objectMineStats.remove(statBaseIn[i]);
         generalStats.remove(statBaseIn[i]);
         statBaseIn[i] = statBaseIn[j];
      }

   }

   public static StatBase getStatKillEntity(EntityList.EntityEggInfo eggInfo) {
      String s = EntityList.getStringFromID(eggInfo.spawnedID);
      return s == null ? null : (new StatBase("stat.killEntity." + s, new ChatComponentTranslation("stat.entityKill", new Object[]{new ChatComponentTranslation("entity." + s + ".name", new Object[0])}))).registerStat();
   }

   public static StatBase getStatEntityKilledBy(EntityList.EntityEggInfo eggInfo) {
      String s = EntityList.getStringFromID(eggInfo.spawnedID);
      return s == null ? null : (new StatBase("stat.entityKilledBy." + s, new ChatComponentTranslation("stat.entityKilledBy", new Object[]{new ChatComponentTranslation("entity." + s + ".name", new Object[0])}))).registerStat();
   }

   public static StatBase getOneShotStat(String p_151177_0_) {
      return (StatBase)oneShotStats.get(p_151177_0_);
   }

   static {
      minutesPlayedStat = (new StatBasic("stat.playOneMinute", new ChatComponentTranslation("stat.playOneMinute", new Object[0]), StatBase.timeStatType)).initIndependentStat().registerStat();
      timeSinceDeathStat = (new StatBasic("stat.timeSinceDeath", new ChatComponentTranslation("stat.timeSinceDeath", new Object[0]), StatBase.timeStatType)).initIndependentStat().registerStat();
      distanceWalkedStat = (new StatBasic("stat.walkOneCm", new ChatComponentTranslation("stat.walkOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceCrouchedStat = (new StatBasic("stat.crouchOneCm", new ChatComponentTranslation("stat.crouchOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceSprintedStat = (new StatBasic("stat.sprintOneCm", new ChatComponentTranslation("stat.sprintOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceSwumStat = (new StatBasic("stat.swimOneCm", new ChatComponentTranslation("stat.swimOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceFallenStat = (new StatBasic("stat.fallOneCm", new ChatComponentTranslation("stat.fallOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceClimbedStat = (new StatBasic("stat.climbOneCm", new ChatComponentTranslation("stat.climbOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceFlownStat = (new StatBasic("stat.flyOneCm", new ChatComponentTranslation("stat.flyOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceDoveStat = (new StatBasic("stat.diveOneCm", new ChatComponentTranslation("stat.diveOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceByMinecartStat = (new StatBasic("stat.minecartOneCm", new ChatComponentTranslation("stat.minecartOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceByBoatStat = (new StatBasic("stat.boatOneCm", new ChatComponentTranslation("stat.boatOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceByPigStat = (new StatBasic("stat.pigOneCm", new ChatComponentTranslation("stat.pigOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      distanceByHorseStat = (new StatBasic("stat.horseOneCm", new ChatComponentTranslation("stat.horseOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      jumpStat = (new StatBasic("stat.jump", new ChatComponentTranslation("stat.jump", new Object[0]))).initIndependentStat().registerStat();
      dropStat = (new StatBasic("stat.drop", new ChatComponentTranslation("stat.drop", new Object[0]))).initIndependentStat().registerStat();
      damageDealtStat = (new StatBasic("stat.damageDealt", new ChatComponentTranslation("stat.damageDealt", new Object[0]), StatBase.field_111202_k)).registerStat();
      damageTakenStat = (new StatBasic("stat.damageTaken", new ChatComponentTranslation("stat.damageTaken", new Object[0]), StatBase.field_111202_k)).registerStat();
      deathsStat = (new StatBasic("stat.deaths", new ChatComponentTranslation("stat.deaths", new Object[0]))).registerStat();
      mobKillsStat = (new StatBasic("stat.mobKills", new ChatComponentTranslation("stat.mobKills", new Object[0]))).registerStat();
      animalsBredStat = (new StatBasic("stat.animalsBred", new ChatComponentTranslation("stat.animalsBred", new Object[0]))).registerStat();
      playerKillsStat = (new StatBasic("stat.playerKills", new ChatComponentTranslation("stat.playerKills", new Object[0]))).registerStat();
      fishCaughtStat = (new StatBasic("stat.fishCaught", new ChatComponentTranslation("stat.fishCaught", new Object[0]))).registerStat();
      junkFishedStat = (new StatBasic("stat.junkFished", new ChatComponentTranslation("stat.junkFished", new Object[0]))).registerStat();
      treasureFishedStat = (new StatBasic("stat.treasureFished", new ChatComponentTranslation("stat.treasureFished", new Object[0]))).registerStat();
      timesTalkedToVillagerStat = (new StatBasic("stat.talkedToVillager", new ChatComponentTranslation("stat.talkedToVillager", new Object[0]))).registerStat();
      timesTradedWithVillagerStat = (new StatBasic("stat.tradedWithVillager", new ChatComponentTranslation("stat.tradedWithVillager", new Object[0]))).registerStat();
      field_181724_H = (new StatBasic("stat.cakeSlicesEaten", new ChatComponentTranslation("stat.cakeSlicesEaten", new Object[0]))).registerStat();
      field_181725_I = (new StatBasic("stat.cauldronFilled", new ChatComponentTranslation("stat.cauldronFilled", new Object[0]))).registerStat();
      field_181726_J = (new StatBasic("stat.cauldronUsed", new ChatComponentTranslation("stat.cauldronUsed", new Object[0]))).registerStat();
      field_181727_K = (new StatBasic("stat.armorCleaned", new ChatComponentTranslation("stat.armorCleaned", new Object[0]))).registerStat();
      field_181728_L = (new StatBasic("stat.bannerCleaned", new ChatComponentTranslation("stat.bannerCleaned", new Object[0]))).registerStat();
      field_181729_M = (new StatBasic("stat.brewingstandInteraction", new ChatComponentTranslation("stat.brewingstandInteraction", new Object[0]))).registerStat();
      field_181730_N = (new StatBasic("stat.beaconInteraction", new ChatComponentTranslation("stat.beaconInteraction", new Object[0]))).registerStat();
      field_181731_O = (new StatBasic("stat.dropperInspected", new ChatComponentTranslation("stat.dropperInspected", new Object[0]))).registerStat();
      field_181732_P = (new StatBasic("stat.hopperInspected", new ChatComponentTranslation("stat.hopperInspected", new Object[0]))).registerStat();
      field_181733_Q = (new StatBasic("stat.dispenserInspected", new ChatComponentTranslation("stat.dispenserInspected", new Object[0]))).registerStat();
      field_181734_R = (new StatBasic("stat.noteblockPlayed", new ChatComponentTranslation("stat.noteblockPlayed", new Object[0]))).registerStat();
      field_181735_S = (new StatBasic("stat.noteblockTuned", new ChatComponentTranslation("stat.noteblockTuned", new Object[0]))).registerStat();
      field_181736_T = (new StatBasic("stat.flowerPotted", new ChatComponentTranslation("stat.flowerPotted", new Object[0]))).registerStat();
      field_181737_U = (new StatBasic("stat.trappedChestTriggered", new ChatComponentTranslation("stat.trappedChestTriggered", new Object[0]))).registerStat();
      field_181738_V = (new StatBasic("stat.enderchestOpened", new ChatComponentTranslation("stat.enderchestOpened", new Object[0]))).registerStat();
      field_181739_W = (new StatBasic("stat.itemEnchanted", new ChatComponentTranslation("stat.itemEnchanted", new Object[0]))).registerStat();
      field_181740_X = (new StatBasic("stat.recordPlayed", new ChatComponentTranslation("stat.recordPlayed", new Object[0]))).registerStat();
      field_181741_Y = (new StatBasic("stat.furnaceInteraction", new ChatComponentTranslation("stat.furnaceInteraction", new Object[0]))).registerStat();
      field_181742_Z = (new StatBasic("stat.craftingTableInteraction", new ChatComponentTranslation("stat.workbenchInteraction", new Object[0]))).registerStat();
      field_181723_aa = (new StatBasic("stat.chestOpened", new ChatComponentTranslation("stat.chestOpened", new Object[0]))).registerStat();
      mineBlockStatArray = new StatBase[4096];
      objectCraftStats = new StatBase[32000];
      objectUseStats = new StatBase[32000];
      objectBreakStats = new StatBase[32000];
   }
}
