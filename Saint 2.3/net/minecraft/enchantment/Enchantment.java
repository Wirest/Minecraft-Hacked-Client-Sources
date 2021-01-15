package net.minecraft.enchantment;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public abstract class Enchantment {
   public static final Enchantment[] field_180311_a = new Enchantment[256];
   public static final Enchantment[] enchantmentsList;
   private static final Map field_180307_E = Maps.newHashMap();
   public static final Enchantment field_180310_c = new EnchantmentProtection(0, new ResourceLocation("protection"), 10, 0);
   public static final Enchantment fireProtection = new EnchantmentProtection(1, new ResourceLocation("fire_protection"), 5, 1);
   public static final Enchantment field_180309_e = new EnchantmentProtection(2, new ResourceLocation("feather_falling"), 5, 2);
   public static final Enchantment blastProtection = new EnchantmentProtection(3, new ResourceLocation("blast_protection"), 2, 3);
   public static final Enchantment field_180308_g = new EnchantmentProtection(4, new ResourceLocation("projectile_protection"), 5, 4);
   public static final Enchantment field_180317_h = new EnchantmentOxygen(5, new ResourceLocation("respiration"), 2);
   public static final Enchantment aquaAffinity = new EnchantmentWaterWorker(6, new ResourceLocation("aqua_affinity"), 2);
   public static final Enchantment thorns = new EnchantmentThorns(7, new ResourceLocation("thorns"), 1);
   public static final Enchantment field_180316_k = new EnchantmentWaterWalker(8, new ResourceLocation("depth_strider"), 2);
   public static final Enchantment field_180314_l = new EnchantmentDamage(16, new ResourceLocation("sharpness"), 10, 0);
   public static final Enchantment field_180315_m = new EnchantmentDamage(17, new ResourceLocation("smite"), 5, 1);
   public static final Enchantment field_180312_n = new EnchantmentDamage(18, new ResourceLocation("bane_of_arthropods"), 5, 2);
   public static final Enchantment field_180313_o = new EnchantmentKnockback(19, new ResourceLocation("knockback"), 5);
   public static final Enchantment fireAspect = new EnchantmentFireAspect(20, new ResourceLocation("fire_aspect"), 2);
   public static final Enchantment looting;
   public static final Enchantment efficiency;
   public static final Enchantment silkTouch;
   public static final Enchantment unbreaking;
   public static final Enchantment fortune;
   public static final Enchantment power;
   public static final Enchantment punch;
   public static final Enchantment flame;
   public static final Enchantment infinity;
   public static final Enchantment luckOfTheSea;
   public static final Enchantment lure;
   public final int effectId;
   private final int weight;
   public EnumEnchantmentType type;
   protected String name;
   private static final String __OBFID = "CL_00000105";

   static {
      looting = new EnchantmentLootBonus(21, new ResourceLocation("looting"), 2, EnumEnchantmentType.WEAPON);
      efficiency = new EnchantmentDigging(32, new ResourceLocation("efficiency"), 10);
      silkTouch = new EnchantmentUntouching(33, new ResourceLocation("silk_touch"), 1);
      unbreaking = new EnchantmentDurability(34, new ResourceLocation("unbreaking"), 5);
      fortune = new EnchantmentLootBonus(35, new ResourceLocation("fortune"), 2, EnumEnchantmentType.DIGGER);
      power = new EnchantmentArrowDamage(48, new ResourceLocation("power"), 10);
      punch = new EnchantmentArrowKnockback(49, new ResourceLocation("punch"), 2);
      flame = new EnchantmentArrowFire(50, new ResourceLocation("flame"), 2);
      infinity = new EnchantmentArrowInfinite(51, new ResourceLocation("infinity"), 1);
      luckOfTheSea = new EnchantmentLootBonus(61, new ResourceLocation("luck_of_the_sea"), 2, EnumEnchantmentType.FISHING_ROD);
      lure = new EnchantmentFishingSpeed(62, new ResourceLocation("lure"), 2, EnumEnchantmentType.FISHING_ROD);
      ArrayList var0 = Lists.newArrayList();
      Enchantment[] var1 = field_180311_a;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Enchantment var4 = var1[var3];
         if (var4 != null) {
            var0.add(var4);
         }
      }

      enchantmentsList = (Enchantment[])var0.toArray(new Enchantment[var0.size()]);
   }

   public static Enchantment func_180306_c(int p_180306_0_) {
      return p_180306_0_ >= 0 && p_180306_0_ < field_180311_a.length ? field_180311_a[p_180306_0_] : null;
   }

   protected Enchantment(int p_i45771_1_, ResourceLocation p_i45771_2_, int p_i45771_3_, EnumEnchantmentType p_i45771_4_) {
      this.effectId = p_i45771_1_;
      this.weight = p_i45771_3_;
      this.type = p_i45771_4_;
      if (field_180311_a[p_i45771_1_] != null) {
         throw new IllegalArgumentException("Duplicate enchantment id!");
      } else {
         field_180311_a[p_i45771_1_] = this;
         field_180307_E.put(p_i45771_2_, this);
      }
   }

   public static Enchantment func_180305_b(String p_180305_0_) {
      return (Enchantment)field_180307_E.get(new ResourceLocation(p_180305_0_));
   }

   public static String[] func_180304_c() {
      String[] var0 = new String[field_180307_E.size()];
      int var1 = 0;

      ResourceLocation var3;
      for(Iterator var2 = field_180307_E.keySet().iterator(); var2.hasNext(); var0[var1++] = var3.toString()) {
         var3 = (ResourceLocation)var2.next();
      }

      return var0;
   }

   public int getWeight() {
      return this.weight;
   }

   public int getMinLevel() {
      return 1;
   }

   public int getMaxLevel() {
      return 1;
   }

   public int getMinEnchantability(int p_77321_1_) {
      return 1 + p_77321_1_ * 10;
   }

   public int getMaxEnchantability(int p_77317_1_) {
      return this.getMinEnchantability(p_77317_1_) + 5;
   }

   public int calcModifierDamage(int p_77318_1_, DamageSource p_77318_2_) {
      return 0;
   }

   public float func_152376_a(int p_152376_1_, EnumCreatureAttribute p_152376_2_) {
      return 0.0F;
   }

   public boolean canApplyTogether(Enchantment p_77326_1_) {
      return this != p_77326_1_;
   }

   public Enchantment setName(String p_77322_1_) {
      this.name = p_77322_1_;
      return this;
   }

   public String getName() {
      return "enchantment." + this.name;
   }

   public String getTranslatedName(int p_77316_1_) {
      String var2 = StatCollector.translateToLocal(this.getName());
      return var2 + " " + StatCollector.translateToLocal("enchantment.level." + p_77316_1_);
   }

   public boolean canApply(ItemStack p_92089_1_) {
      return this.type.canEnchantItem(p_92089_1_.getItem());
   }

   public void func_151368_a(EntityLivingBase p_151368_1_, Entity p_151368_2_, int p_151368_3_) {
   }

   public void func_151367_b(EntityLivingBase p_151367_1_, Entity p_151367_2_, int p_151367_3_) {
   }
}
