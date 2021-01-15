package net.minecraft.enchantment;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class EnchantmentProtection extends Enchantment {
   private static final String[] protectionName = new String[]{"all", "fire", "fall", "explosion", "projectile"};
   private static final int[] baseEnchantability = new int[]{1, 10, 5, 5, 3};
   private static final int[] levelEnchantability = new int[]{11, 8, 6, 8, 6};
   private static final int[] thresholdEnchantability = new int[]{20, 12, 10, 12, 15};
   public final int protectionType;
   private static final String __OBFID = "CL_00000121";

   public EnchantmentProtection(int p_i45765_1_, ResourceLocation p_i45765_2_, int p_i45765_3_, int p_i45765_4_) {
      super(p_i45765_1_, p_i45765_2_, p_i45765_3_, EnumEnchantmentType.ARMOR);
      this.protectionType = p_i45765_4_;
      if (p_i45765_4_ == 2) {
         this.type = EnumEnchantmentType.ARMOR_FEET;
      }

   }

   public int getMinEnchantability(int p_77321_1_) {
      return baseEnchantability[this.protectionType] + (p_77321_1_ - 1) * levelEnchantability[this.protectionType];
   }

   public int getMaxEnchantability(int p_77317_1_) {
      return this.getMinEnchantability(p_77317_1_) + thresholdEnchantability[this.protectionType];
   }

   public int getMaxLevel() {
      return 4;
   }

   public int calcModifierDamage(int p_77318_1_, DamageSource p_77318_2_) {
      if (p_77318_2_.canHarmInCreative()) {
         return 0;
      } else {
         float var3 = (float)(6 + p_77318_1_ * p_77318_1_) / 3.0F;
         return this.protectionType == 0 ? MathHelper.floor_float(var3 * 0.75F) : (this.protectionType == 1 && p_77318_2_.isFireDamage() ? MathHelper.floor_float(var3 * 1.25F) : (this.protectionType == 2 && p_77318_2_ == DamageSource.fall ? MathHelper.floor_float(var3 * 2.5F) : (this.protectionType == 3 && p_77318_2_.isExplosion() ? MathHelper.floor_float(var3 * 1.5F) : (this.protectionType == 4 && p_77318_2_.isProjectile() ? MathHelper.floor_float(var3 * 1.5F) : 0))));
      }
   }

   public String getName() {
      return "enchantment.protect." + protectionName[this.protectionType];
   }

   public boolean canApplyTogether(Enchantment p_77326_1_) {
      if (!(p_77326_1_ instanceof EnchantmentProtection)) {
         return super.canApplyTogether(p_77326_1_);
      } else {
         EnchantmentProtection var2 = (EnchantmentProtection)p_77326_1_;
         return var2.protectionType == this.protectionType ? false : this.protectionType == 2 || var2.protectionType == 2;
      }
   }

   public static int getFireTimeForEntity(Entity p_92093_0_, int p_92093_1_) {
      int var2 = EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.fireProtection.effectId, p_92093_0_.getInventory());
      if (var2 > 0) {
         p_92093_1_ -= MathHelper.floor_float((float)p_92093_1_ * (float)var2 * 0.15F);
      }

      return p_92093_1_;
   }

   public static double func_92092_a(Entity p_92092_0_, double p_92092_1_) {
      int var3 = EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.blastProtection.effectId, p_92092_0_.getInventory());
      if (var3 > 0) {
         p_92092_1_ -= (double)MathHelper.floor_double(p_92092_1_ * (double)((float)var3 * 0.15F));
      }

      return p_92092_1_;
   }
}
