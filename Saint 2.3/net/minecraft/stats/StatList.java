package net.minecraft.stats;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
   public static final StatBase[] mineBlockStatArray;
   public static final StatBase[] objectCraftStats;
   public static final StatBase[] objectUseStats;
   public static final StatBase[] objectBreakStats;
   private static final String __OBFID = "CL_00001480";

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
      mineBlockStatArray = new StatBase[4096];
      objectCraftStats = new StatBase[32000];
      objectUseStats = new StatBase[32000];
      objectBreakStats = new StatBase[32000];
   }

   public static void func_151178_a() {
      func_151181_c();
      initStats();
      func_151179_e();
      initCraftableStats();
      AchievementList.init();
      EntityList.func_151514_a();
   }

   private static void initCraftableStats() {
      HashSet var0 = Sets.newHashSet();
      Iterator var1 = CraftingManager.getInstance().getRecipeList().iterator();

      while(var1.hasNext()) {
         IRecipe var2 = (IRecipe)var1.next();
         if (var2.getRecipeOutput() != null) {
            var0.add(var2.getRecipeOutput().getItem());
         }
      }

      var1 = FurnaceRecipes.instance().getSmeltingList().values().iterator();

      while(var1.hasNext()) {
         ItemStack var5 = (ItemStack)var1.next();
         var0.add(var5.getItem());
      }

      var1 = var0.iterator();

      while(var1.hasNext()) {
         Item var6 = (Item)var1.next();
         if (var6 != null) {
            int var3 = Item.getIdFromItem(var6);
            String var4 = func_180204_a(var6);
            if (var4 != null) {
               objectCraftStats[var3] = (new StatCrafting("stat.craftItem.", var4, new ChatComponentTranslation("stat.craftItem", new Object[]{(new ItemStack(var6)).getChatComponent()}), var6)).registerStat();
            }
         }
      }

      replaceAllSimilarBlocks(objectCraftStats);
   }

   private static void func_151181_c() {
      Iterator var0 = Block.blockRegistry.iterator();

      while(var0.hasNext()) {
         Block var1 = (Block)var0.next();
         Item var2 = Item.getItemFromBlock(var1);
         if (var2 != null) {
            int var3 = Block.getIdFromBlock(var1);
            String var4 = func_180204_a(var2);
            if (var4 != null && var1.getEnableStats()) {
               mineBlockStatArray[var3] = (new StatCrafting("stat.mineBlock.", var4, new ChatComponentTranslation("stat.mineBlock", new Object[]{(new ItemStack(var1)).getChatComponent()}), var2)).registerStat();
               objectMineStats.add((StatCrafting)mineBlockStatArray[var3]);
            }
         }
      }

      replaceAllSimilarBlocks(mineBlockStatArray);
   }

   private static void initStats() {
      Iterator var0 = Item.itemRegistry.iterator();

      while(var0.hasNext()) {
         Item var1 = (Item)var0.next();
         if (var1 != null) {
            int var2 = Item.getIdFromItem(var1);
            String var3 = func_180204_a(var1);
            if (var3 != null) {
               objectUseStats[var2] = (new StatCrafting("stat.useItem.", var3, new ChatComponentTranslation("stat.useItem", new Object[]{(new ItemStack(var1)).getChatComponent()}), var1)).registerStat();
               if (!(var1 instanceof ItemBlock)) {
                  itemStats.add((StatCrafting)objectUseStats[var2]);
               }
            }
         }
      }

      replaceAllSimilarBlocks(objectUseStats);
   }

   private static void func_151179_e() {
      Iterator var0 = Item.itemRegistry.iterator();

      while(var0.hasNext()) {
         Item var1 = (Item)var0.next();
         if (var1 != null) {
            int var2 = Item.getIdFromItem(var1);
            String var3 = func_180204_a(var1);
            if (var3 != null && var1.isDamageable()) {
               objectBreakStats[var2] = (new StatCrafting("stat.breakItem.", var3, new ChatComponentTranslation("stat.breakItem", new Object[]{(new ItemStack(var1)).getChatComponent()}), var1)).registerStat();
            }
         }
      }

      replaceAllSimilarBlocks(objectBreakStats);
   }

   private static String func_180204_a(Item p_180204_0_) {
      ResourceLocation var1 = (ResourceLocation)Item.itemRegistry.getNameForObject(p_180204_0_);
      return var1 != null ? var1.toString().replace(':', '.') : null;
   }

   private static void replaceAllSimilarBlocks(StatBase[] p_75924_0_) {
      func_151180_a(p_75924_0_, Blocks.water, Blocks.flowing_water);
      func_151180_a(p_75924_0_, Blocks.lava, Blocks.flowing_lava);
      func_151180_a(p_75924_0_, Blocks.lit_pumpkin, Blocks.pumpkin);
      func_151180_a(p_75924_0_, Blocks.lit_furnace, Blocks.furnace);
      func_151180_a(p_75924_0_, Blocks.lit_redstone_ore, Blocks.redstone_ore);
      func_151180_a(p_75924_0_, Blocks.powered_repeater, Blocks.unpowered_repeater);
      func_151180_a(p_75924_0_, Blocks.powered_comparator, Blocks.unpowered_comparator);
      func_151180_a(p_75924_0_, Blocks.redstone_torch, Blocks.unlit_redstone_torch);
      func_151180_a(p_75924_0_, Blocks.lit_redstone_lamp, Blocks.redstone_lamp);
      func_151180_a(p_75924_0_, Blocks.double_stone_slab, Blocks.stone_slab);
      func_151180_a(p_75924_0_, Blocks.double_wooden_slab, Blocks.wooden_slab);
      func_151180_a(p_75924_0_, Blocks.double_stone_slab2, Blocks.stone_slab2);
      func_151180_a(p_75924_0_, Blocks.grass, Blocks.dirt);
      func_151180_a(p_75924_0_, Blocks.farmland, Blocks.dirt);
   }

   private static void func_151180_a(StatBase[] p_151180_0_, Block p_151180_1_, Block p_151180_2_) {
      int var3 = Block.getIdFromBlock(p_151180_1_);
      int var4 = Block.getIdFromBlock(p_151180_2_);
      if (p_151180_0_[var3] != null && p_151180_0_[var4] == null) {
         p_151180_0_[var4] = p_151180_0_[var3];
      } else {
         allStats.remove(p_151180_0_[var3]);
         objectMineStats.remove(p_151180_0_[var3]);
         generalStats.remove(p_151180_0_[var3]);
         p_151180_0_[var3] = p_151180_0_[var4];
      }

   }

   public static StatBase func_151182_a(EntityList.EntityEggInfo p_151182_0_) {
      String var1 = EntityList.getStringFromID(p_151182_0_.spawnedID);
      return var1 == null ? null : (new StatBase("stat.killEntity." + var1, new ChatComponentTranslation("stat.entityKill", new Object[]{new ChatComponentTranslation("entity." + var1 + ".name", new Object[0])}))).registerStat();
   }

   public static StatBase func_151176_b(EntityList.EntityEggInfo p_151176_0_) {
      String var1 = EntityList.getStringFromID(p_151176_0_.spawnedID);
      return var1 == null ? null : (new StatBase("stat.entityKilledBy." + var1, new ChatComponentTranslation("stat.entityKilledBy", new Object[]{new ChatComponentTranslation("entity." + var1 + ".name", new Object[0])}))).registerStat();
   }

   public static StatBase getOneShotStat(String p_151177_0_) {
      return (StatBase)oneShotStats.get(p_151177_0_);
   }
}
