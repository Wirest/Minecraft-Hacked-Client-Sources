package net.minecraft.enchantment;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public abstract class Enchantment {
   private static final Enchantment[] enchantmentsList = new Enchantment[256];
   public static final Enchantment[] enchantmentsBookList;
   private static final Map locationEnchantments = Maps.newHashMap();
   public static final Enchantment protection = new EnchantmentProtection(0, new ResourceLocation("protection"), 10, 0);
   public static final Enchantment fireProtection = new EnchantmentProtection(1, new ResourceLocation("fire_protection"), 5, 1);
   public static final Enchantment featherFalling = new EnchantmentProtection(2, new ResourceLocation("feather_falling"), 5, 2);
   public static final Enchantment blastProtection = new EnchantmentProtection(3, new ResourceLocation("blast_protection"), 2, 3);
   public static final Enchantment projectileProtection = new EnchantmentProtection(4, new ResourceLocation("projectile_protection"), 5, 4);
   public static final Enchantment respiration = new EnchantmentOxygen(5, new ResourceLocation("respiration"), 2);
   public static final Enchantment aquaAffinity = new EnchantmentWaterWorker(6, new ResourceLocation("aqua_affinity"), 2);
   public static final Enchantment thorns = new EnchantmentThorns(7, new ResourceLocation("thorns"), 1);
   public static final Enchantment depthStrider = new EnchantmentWaterWalker(8, new ResourceLocation("depth_strider"), 2);
   public static final Enchantment sharpness = new EnchantmentDamage(16, new ResourceLocation("sharpness"), 10, 0);
   public static final Enchantment smite = new EnchantmentDamage(17, new ResourceLocation("smite"), 5, 1);
   public static final Enchantment baneOfArthropods = new EnchantmentDamage(18, new ResourceLocation("bane_of_arthropods"), 5, 2);
   public static final Enchantment knockback = new EnchantmentKnockback(19, new ResourceLocation("knockback"), 5);
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

   public static Enchantment getEnchantmentById(int enchID) {
      return enchID >= 0 && enchID < enchantmentsList.length ? enchantmentsList[enchID] : null;
   }

   protected Enchantment(int enchID, ResourceLocation enchName, int enchWeight, EnumEnchantmentType enchType) {
      this.effectId = enchID;
      this.weight = enchWeight;
      this.type = enchType;
      if (enchantmentsList[enchID] != null) {
         throw new IllegalArgumentException("Duplicate enchantment id!");
      } else {
         enchantmentsList[enchID] = this;
         locationEnchantments.put(enchName, this);
      }
   }

   public static Enchantment getEnchantmentByLocation(String location) {
      return (Enchantment)locationEnchantments.get(new ResourceLocation(location));
   }

   public static Set func_181077_c() {
      return locationEnchantments.keySet();
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

   public int getMinEnchantability(int enchantmentLevel) {
      return 1 + enchantmentLevel * 10;
   }

   public int getMaxEnchantability(int enchantmentLevel) {
      return this.getMinEnchantability(enchantmentLevel) + 5;
   }

   public int calcModifierDamage(int level, DamageSource source) {
      return 0;
   }

   public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType) {
      return 0.0F;
   }

   public boolean canApplyTogether(Enchantment ench) {
      return this != ench;
   }

   public Enchantment setName(String enchName) {
      this.name = enchName;
      return this;
   }

   public String getName() {
      return "enchantment." + this.name;
   }

   public String getTranslatedName(int level) {
      String s = StatCollector.translateToLocal(this.getName());
      return s + " " + StatCollector.translateToLocal("enchantment.level." + level);
   }

   public boolean canApply(ItemStack stack) {
      return this.type.canEnchantItem(stack.getItem());
   }

   public void onEntityDamaged(EntityLivingBase user, Entity target, int level) {
   }

   public void onUserHurt(EntityLivingBase user, Entity attacker, int level) {
   }

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
      List list = Lists.newArrayList();
      Enchantment[] var1 = enchantmentsList;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Enchantment enchantment = var1[var3];
         if (enchantment != null) {
            list.add(enchantment);
         }
      }

      enchantmentsBookList = (Enchantment[])((Enchantment[])list.toArray(new Enchantment[list.size()]));
   }
}
