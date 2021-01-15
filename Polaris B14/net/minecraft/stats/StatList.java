/*     */ package net.minecraft.stats;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.EntityList;
/*     */ import net.minecraft.entity.EntityList.EntityEggInfo;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.crafting.CraftingManager;
/*     */ import net.minecraft.item.crafting.FurnaceRecipes;
/*     */ import net.minecraft.item.crafting.IRecipe;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.RegistryNamespaced;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class StatList
/*     */ {
/*  23 */   protected static Map<String, StatBase> oneShotStats = ;
/*  24 */   public static List<StatBase> allStats = Lists.newArrayList();
/*  25 */   public static List<StatBase> generalStats = Lists.newArrayList();
/*  26 */   public static List<StatCrafting> itemStats = Lists.newArrayList();
/*  27 */   public static List<StatCrafting> objectMineStats = Lists.newArrayList();
/*     */   
/*     */ 
/*  30 */   public static StatBase leaveGameStat = new StatBasic("stat.leaveGame", new ChatComponentTranslation("stat.leaveGame", new Object[0])).initIndependentStat().registerStat();
/*     */   
/*     */ 
/*  33 */   public static StatBase minutesPlayedStat = new StatBasic("stat.playOneMinute", new ChatComponentTranslation("stat.playOneMinute", new Object[0]), StatBase.timeStatType).initIndependentStat().registerStat();
/*  34 */   public static StatBase timeSinceDeathStat = new StatBasic("stat.timeSinceDeath", new ChatComponentTranslation("stat.timeSinceDeath", new Object[0]), StatBase.timeStatType).initIndependentStat().registerStat();
/*     */   
/*     */ 
/*  37 */   public static StatBase distanceWalkedStat = new StatBasic("stat.walkOneCm", new ChatComponentTranslation("stat.walkOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
/*  38 */   public static StatBase distanceCrouchedStat = new StatBasic("stat.crouchOneCm", new ChatComponentTranslation("stat.crouchOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
/*  39 */   public static StatBase distanceSprintedStat = new StatBasic("stat.sprintOneCm", new ChatComponentTranslation("stat.sprintOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
/*     */   
/*     */ 
/*  42 */   public static StatBase distanceSwumStat = new StatBasic("stat.swimOneCm", new ChatComponentTranslation("stat.swimOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
/*     */   
/*     */ 
/*  45 */   public static StatBase distanceFallenStat = new StatBasic("stat.fallOneCm", new ChatComponentTranslation("stat.fallOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
/*     */   
/*     */ 
/*  48 */   public static StatBase distanceClimbedStat = new StatBasic("stat.climbOneCm", new ChatComponentTranslation("stat.climbOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
/*     */   
/*     */ 
/*  51 */   public static StatBase distanceFlownStat = new StatBasic("stat.flyOneCm", new ChatComponentTranslation("stat.flyOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
/*     */   
/*     */ 
/*  54 */   public static StatBase distanceDoveStat = new StatBasic("stat.diveOneCm", new ChatComponentTranslation("stat.diveOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
/*     */   
/*     */ 
/*  57 */   public static StatBase distanceByMinecartStat = new StatBasic("stat.minecartOneCm", new ChatComponentTranslation("stat.minecartOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
/*     */   
/*     */ 
/*  60 */   public static StatBase distanceByBoatStat = new StatBasic("stat.boatOneCm", new ChatComponentTranslation("stat.boatOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
/*     */   
/*     */ 
/*  63 */   public static StatBase distanceByPigStat = new StatBasic("stat.pigOneCm", new ChatComponentTranslation("stat.pigOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
/*  64 */   public static StatBase distanceByHorseStat = new StatBasic("stat.horseOneCm", new ChatComponentTranslation("stat.horseOneCm", new Object[0]), StatBase.distanceStatType).initIndependentStat().registerStat();
/*     */   
/*     */ 
/*  67 */   public static StatBase jumpStat = new StatBasic("stat.jump", new ChatComponentTranslation("stat.jump", new Object[0])).initIndependentStat().registerStat();
/*     */   
/*     */ 
/*  70 */   public static StatBase dropStat = new StatBasic("stat.drop", new ChatComponentTranslation("stat.drop", new Object[0])).initIndependentStat().registerStat();
/*     */   
/*     */ 
/*  73 */   public static StatBase damageDealtStat = new StatBasic("stat.damageDealt", new ChatComponentTranslation("stat.damageDealt", new Object[0]), StatBase.field_111202_k).registerStat();
/*     */   
/*     */ 
/*  76 */   public static StatBase damageTakenStat = new StatBasic("stat.damageTaken", new ChatComponentTranslation("stat.damageTaken", new Object[0]), StatBase.field_111202_k).registerStat();
/*     */   
/*     */ 
/*  79 */   public static StatBase deathsStat = new StatBasic("stat.deaths", new ChatComponentTranslation("stat.deaths", new Object[0])).registerStat();
/*     */   
/*     */ 
/*  82 */   public static StatBase mobKillsStat = new StatBasic("stat.mobKills", new ChatComponentTranslation("stat.mobKills", new Object[0])).registerStat();
/*     */   
/*     */ 
/*  85 */   public static StatBase animalsBredStat = new StatBasic("stat.animalsBred", new ChatComponentTranslation("stat.animalsBred", new Object[0])).registerStat();
/*     */   
/*     */ 
/*  88 */   public static StatBase playerKillsStat = new StatBasic("stat.playerKills", new ChatComponentTranslation("stat.playerKills", new Object[0])).registerStat();
/*  89 */   public static StatBase fishCaughtStat = new StatBasic("stat.fishCaught", new ChatComponentTranslation("stat.fishCaught", new Object[0])).registerStat();
/*  90 */   public static StatBase junkFishedStat = new StatBasic("stat.junkFished", new ChatComponentTranslation("stat.junkFished", new Object[0])).registerStat();
/*  91 */   public static StatBase treasureFishedStat = new StatBasic("stat.treasureFished", new ChatComponentTranslation("stat.treasureFished", new Object[0])).registerStat();
/*  92 */   public static StatBase timesTalkedToVillagerStat = new StatBasic("stat.talkedToVillager", new ChatComponentTranslation("stat.talkedToVillager", new Object[0])).registerStat();
/*  93 */   public static StatBase timesTradedWithVillagerStat = new StatBasic("stat.tradedWithVillager", new ChatComponentTranslation("stat.tradedWithVillager", new Object[0])).registerStat();
/*  94 */   public static StatBase field_181724_H = new StatBasic("stat.cakeSlicesEaten", new ChatComponentTranslation("stat.cakeSlicesEaten", new Object[0])).registerStat();
/*  95 */   public static StatBase field_181725_I = new StatBasic("stat.cauldronFilled", new ChatComponentTranslation("stat.cauldronFilled", new Object[0])).registerStat();
/*  96 */   public static StatBase field_181726_J = new StatBasic("stat.cauldronUsed", new ChatComponentTranslation("stat.cauldronUsed", new Object[0])).registerStat();
/*  97 */   public static StatBase field_181727_K = new StatBasic("stat.armorCleaned", new ChatComponentTranslation("stat.armorCleaned", new Object[0])).registerStat();
/*  98 */   public static StatBase field_181728_L = new StatBasic("stat.bannerCleaned", new ChatComponentTranslation("stat.bannerCleaned", new Object[0])).registerStat();
/*  99 */   public static StatBase field_181729_M = new StatBasic("stat.brewingstandInteraction", new ChatComponentTranslation("stat.brewingstandInteraction", new Object[0])).registerStat();
/* 100 */   public static StatBase field_181730_N = new StatBasic("stat.beaconInteraction", new ChatComponentTranslation("stat.beaconInteraction", new Object[0])).registerStat();
/* 101 */   public static StatBase field_181731_O = new StatBasic("stat.dropperInspected", new ChatComponentTranslation("stat.dropperInspected", new Object[0])).registerStat();
/* 102 */   public static StatBase field_181732_P = new StatBasic("stat.hopperInspected", new ChatComponentTranslation("stat.hopperInspected", new Object[0])).registerStat();
/* 103 */   public static StatBase field_181733_Q = new StatBasic("stat.dispenserInspected", new ChatComponentTranslation("stat.dispenserInspected", new Object[0])).registerStat();
/* 104 */   public static StatBase field_181734_R = new StatBasic("stat.noteblockPlayed", new ChatComponentTranslation("stat.noteblockPlayed", new Object[0])).registerStat();
/* 105 */   public static StatBase field_181735_S = new StatBasic("stat.noteblockTuned", new ChatComponentTranslation("stat.noteblockTuned", new Object[0])).registerStat();
/* 106 */   public static StatBase field_181736_T = new StatBasic("stat.flowerPotted", new ChatComponentTranslation("stat.flowerPotted", new Object[0])).registerStat();
/* 107 */   public static StatBase field_181737_U = new StatBasic("stat.trappedChestTriggered", new ChatComponentTranslation("stat.trappedChestTriggered", new Object[0])).registerStat();
/* 108 */   public static StatBase field_181738_V = new StatBasic("stat.enderchestOpened", new ChatComponentTranslation("stat.enderchestOpened", new Object[0])).registerStat();
/* 109 */   public static StatBase field_181739_W = new StatBasic("stat.itemEnchanted", new ChatComponentTranslation("stat.itemEnchanted", new Object[0])).registerStat();
/* 110 */   public static StatBase field_181740_X = new StatBasic("stat.recordPlayed", new ChatComponentTranslation("stat.recordPlayed", new Object[0])).registerStat();
/* 111 */   public static StatBase field_181741_Y = new StatBasic("stat.furnaceInteraction", new ChatComponentTranslation("stat.furnaceInteraction", new Object[0])).registerStat();
/* 112 */   public static StatBase field_181742_Z = new StatBasic("stat.craftingTableInteraction", new ChatComponentTranslation("stat.workbenchInteraction", new Object[0])).registerStat();
/* 113 */   public static StatBase field_181723_aa = new StatBasic("stat.chestOpened", new ChatComponentTranslation("stat.chestOpened", new Object[0])).registerStat();
/* 114 */   public static final StatBase[] mineBlockStatArray = new StatBase['က'];
/*     */   
/*     */ 
/* 117 */   public static final StatBase[] objectCraftStats = new StatBase['紀'];
/*     */   
/*     */ 
/* 120 */   public static final StatBase[] objectUseStats = new StatBase['紀'];
/*     */   
/*     */ 
/* 123 */   public static final StatBase[] objectBreakStats = new StatBase['紀'];
/*     */   
/*     */   public static void init()
/*     */   {
/* 127 */     initMiningStats();
/* 128 */     initStats();
/* 129 */     initItemDepleteStats();
/* 130 */     initCraftableStats();
/* 131 */     AchievementList.init();
/* 132 */     EntityList.func_151514_a();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void initCraftableStats()
/*     */   {
/* 141 */     Set<Item> set = com.google.common.collect.Sets.newHashSet();
/*     */     
/* 143 */     for (IRecipe irecipe : CraftingManager.getInstance().getRecipeList())
/*     */     {
/* 145 */       if (irecipe.getRecipeOutput() != null)
/*     */       {
/* 147 */         set.add(irecipe.getRecipeOutput().getItem());
/*     */       }
/*     */     }
/*     */     
/* 151 */     for (ItemStack itemstack : FurnaceRecipes.instance().getSmeltingList().values())
/*     */     {
/* 153 */       set.add(itemstack.getItem());
/*     */     }
/*     */     
/* 156 */     for (Item item : set)
/*     */     {
/* 158 */       if (item != null)
/*     */       {
/* 160 */         int i = Item.getIdFromItem(item);
/* 161 */         String s = func_180204_a(item);
/*     */         
/* 163 */         if (s != null)
/*     */         {
/* 165 */           objectCraftStats[i] = new StatCrafting("stat.craftItem.", s, new ChatComponentTranslation("stat.craftItem", new Object[] { new ItemStack(item).getChatComponent() }), item).registerStat();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 170 */     replaceAllSimilarBlocks(objectCraftStats);
/*     */   }
/*     */   
/*     */   private static void initMiningStats()
/*     */   {
/* 175 */     for (Block block : Block.blockRegistry)
/*     */     {
/* 177 */       Item item = Item.getItemFromBlock(block);
/*     */       
/* 179 */       if (item != null)
/*     */       {
/* 181 */         int i = Block.getIdFromBlock(block);
/* 182 */         String s = func_180204_a(item);
/*     */         
/* 184 */         if ((s != null) && (block.getEnableStats()))
/*     */         {
/* 186 */           mineBlockStatArray[i] = new StatCrafting("stat.mineBlock.", s, new ChatComponentTranslation("stat.mineBlock", new Object[] { new ItemStack(block).getChatComponent() }), item).registerStat();
/* 187 */           objectMineStats.add((StatCrafting)mineBlockStatArray[i]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 192 */     replaceAllSimilarBlocks(mineBlockStatArray);
/*     */   }
/*     */   
/*     */   private static void initStats()
/*     */   {
/* 197 */     for (Item item : Item.itemRegistry)
/*     */     {
/* 199 */       if (item != null)
/*     */       {
/* 201 */         int i = Item.getIdFromItem(item);
/* 202 */         String s = func_180204_a(item);
/*     */         
/* 204 */         if (s != null)
/*     */         {
/* 206 */           objectUseStats[i] = new StatCrafting("stat.useItem.", s, new ChatComponentTranslation("stat.useItem", new Object[] { new ItemStack(item).getChatComponent() }), item).registerStat();
/*     */           
/* 208 */           if (!(item instanceof net.minecraft.item.ItemBlock))
/*     */           {
/* 210 */             itemStats.add((StatCrafting)objectUseStats[i]);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 216 */     replaceAllSimilarBlocks(objectUseStats);
/*     */   }
/*     */   
/*     */   private static void initItemDepleteStats()
/*     */   {
/* 221 */     for (Item item : Item.itemRegistry)
/*     */     {
/* 223 */       if (item != null)
/*     */       {
/* 225 */         int i = Item.getIdFromItem(item);
/* 226 */         String s = func_180204_a(item);
/*     */         
/* 228 */         if ((s != null) && (item.isDamageable()))
/*     */         {
/* 230 */           objectBreakStats[i] = new StatCrafting("stat.breakItem.", s, new ChatComponentTranslation("stat.breakItem", new Object[] { new ItemStack(item).getChatComponent() }), item).registerStat();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 235 */     replaceAllSimilarBlocks(objectBreakStats);
/*     */   }
/*     */   
/*     */   private static String func_180204_a(Item p_180204_0_)
/*     */   {
/* 240 */     ResourceLocation resourcelocation = (ResourceLocation)Item.itemRegistry.getNameForObject(p_180204_0_);
/* 241 */     return resourcelocation != null ? resourcelocation.toString().replace(':', '.') : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void replaceAllSimilarBlocks(StatBase[] p_75924_0_)
/*     */   {
/* 249 */     mergeStatBases(p_75924_0_, Blocks.water, Blocks.flowing_water);
/* 250 */     mergeStatBases(p_75924_0_, Blocks.lava, Blocks.flowing_lava);
/* 251 */     mergeStatBases(p_75924_0_, Blocks.lit_pumpkin, Blocks.pumpkin);
/* 252 */     mergeStatBases(p_75924_0_, Blocks.lit_furnace, Blocks.furnace);
/* 253 */     mergeStatBases(p_75924_0_, Blocks.lit_redstone_ore, Blocks.redstone_ore);
/* 254 */     mergeStatBases(p_75924_0_, Blocks.powered_repeater, Blocks.unpowered_repeater);
/* 255 */     mergeStatBases(p_75924_0_, Blocks.powered_comparator, Blocks.unpowered_comparator);
/* 256 */     mergeStatBases(p_75924_0_, Blocks.redstone_torch, Blocks.unlit_redstone_torch);
/* 257 */     mergeStatBases(p_75924_0_, Blocks.lit_redstone_lamp, Blocks.redstone_lamp);
/* 258 */     mergeStatBases(p_75924_0_, Blocks.double_stone_slab, Blocks.stone_slab);
/* 259 */     mergeStatBases(p_75924_0_, Blocks.double_wooden_slab, Blocks.wooden_slab);
/* 260 */     mergeStatBases(p_75924_0_, Blocks.double_stone_slab2, Blocks.stone_slab2);
/* 261 */     mergeStatBases(p_75924_0_, Blocks.grass, Blocks.dirt);
/* 262 */     mergeStatBases(p_75924_0_, Blocks.farmland, Blocks.dirt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void mergeStatBases(StatBase[] statBaseIn, Block p_151180_1_, Block p_151180_2_)
/*     */   {
/* 270 */     int i = Block.getIdFromBlock(p_151180_1_);
/* 271 */     int j = Block.getIdFromBlock(p_151180_2_);
/*     */     
/* 273 */     if ((statBaseIn[i] != null) && (statBaseIn[j] == null))
/*     */     {
/* 275 */       statBaseIn[j] = statBaseIn[i];
/*     */     }
/*     */     else
/*     */     {
/* 279 */       allStats.remove(statBaseIn[i]);
/* 280 */       objectMineStats.remove(statBaseIn[i]);
/* 281 */       generalStats.remove(statBaseIn[i]);
/* 282 */       statBaseIn[i] = statBaseIn[j];
/*     */     }
/*     */   }
/*     */   
/*     */   public static StatBase getStatKillEntity(EntityList.EntityEggInfo eggInfo)
/*     */   {
/* 288 */     String s = EntityList.getStringFromID(eggInfo.spawnedID);
/* 289 */     return s == null ? null : new StatBase("stat.killEntity." + s, new ChatComponentTranslation("stat.entityKill", new Object[] { new ChatComponentTranslation("entity." + s + ".name", new Object[0]) })).registerStat();
/*     */   }
/*     */   
/*     */   public static StatBase getStatEntityKilledBy(EntityList.EntityEggInfo eggInfo)
/*     */   {
/* 294 */     String s = EntityList.getStringFromID(eggInfo.spawnedID);
/* 295 */     return s == null ? null : new StatBase("stat.entityKilledBy." + s, new ChatComponentTranslation("stat.entityKilledBy", new Object[] { new ChatComponentTranslation("entity." + s + ".name", new Object[0]) })).registerStat();
/*     */   }
/*     */   
/*     */   public static StatBase getOneShotStat(String p_151177_0_)
/*     */   {
/* 300 */     return (StatBase)oneShotStats.get(p_151177_0_);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\stats\StatList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */