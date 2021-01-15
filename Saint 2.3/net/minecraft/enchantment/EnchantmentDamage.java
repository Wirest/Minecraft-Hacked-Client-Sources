package net.minecraft.enchantment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class EnchantmentDamage extends Enchantment {
   private static final String[] protectionName = new String[]{"all", "undead", "arthropods"};
   private static final int[] baseEnchantability = new int[]{1, 5, 5};
   private static final int[] levelEnchantability = new int[]{11, 8, 8};
   private static final int[] thresholdEnchantability = new int[]{20, 20, 20};
   public final int damageType;
   private static final String __OBFID = "CL_00000102";

   public EnchantmentDamage(int p_i45774_1_, ResourceLocation p_i45774_2_, int p_i45774_3_, int p_i45774_4_) {
      super(p_i45774_1_, p_i45774_2_, p_i45774_3_, EnumEnchantmentType.WEAPON);
      this.damageType = p_i45774_4_;
   }

   public int getMinEnchantability(int p_77321_1_) {
      return baseEnchantability[this.damageType] + (p_77321_1_ - 1) * levelEnchantability[this.damageType];
   }

   public int getMaxEnchantability(int p_77317_1_) {
      return this.getMinEnchantability(p_77317_1_) + thresholdEnchantability[this.damageType];
   }

   public int getMaxLevel() {
      return 5;
   }

   public float func_152376_a(int p_152376_1_, EnumCreatureAttribute p_152376_2_) {
      return this.damageType == 0 ? (float)p_152376_1_ * 1.25F : (this.damageType == 1 && p_152376_2_ == EnumCreatureAttribute.UNDEAD ? (float)p_152376_1_ * 2.5F : (this.damageType == 2 && p_152376_2_ == EnumCreatureAttribute.ARTHROPOD ? (float)p_152376_1_ * 2.5F : 0.0F));
   }

   public String getName() {
      return "enchantment.damage." + protectionName[this.damageType];
   }

   public boolean canApplyTogether(Enchantment p_77326_1_) {
      return !(p_77326_1_ instanceof EnchantmentDamage);
   }

   public boolean canApply(ItemStack p_92089_1_) {
      return p_92089_1_.getItem() instanceof ItemAxe ? true : super.canApply(p_92089_1_);
   }

   public void func_151368_a(EntityLivingBase p_151368_1_, Entity p_151368_2_, int p_151368_3_) {
      if (p_151368_2_ instanceof EntityLivingBase) {
         EntityLivingBase var4 = (EntityLivingBase)p_151368_2_;
         if (this.damageType == 2 && var4.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD) {
            int var5 = 20 + p_151368_1_.getRNG().nextInt(10 * p_151368_3_);
            var4.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, var5, 3));
         }
      }

   }
}
