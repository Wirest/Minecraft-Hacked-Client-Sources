package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentArrowFire extends Enchantment {
   private static final String __OBFID = "CL_00000099";

   public EnchantmentArrowFire(int p_i45777_1_, ResourceLocation p_i45777_2_, int p_i45777_3_) {
      super(p_i45777_1_, p_i45777_2_, p_i45777_3_, EnumEnchantmentType.BOW);
      this.setName("arrowFire");
   }

   public int getMinEnchantability(int p_77321_1_) {
      return 20;
   }

   public int getMaxEnchantability(int p_77317_1_) {
      return 50;
   }

   public int getMaxLevel() {
      return 1;
   }
}
