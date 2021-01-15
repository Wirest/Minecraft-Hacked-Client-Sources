package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentFishingSpeed extends Enchantment {
   private static final String __OBFID = "CL_00000117";

   protected EnchantmentFishingSpeed(int p_i45769_1_, ResourceLocation p_i45769_2_, int p_i45769_3_, EnumEnchantmentType p_i45769_4_) {
      super(p_i45769_1_, p_i45769_2_, p_i45769_3_, p_i45769_4_);
      this.setName("fishingSpeed");
   }

   public int getMinEnchantability(int p_77321_1_) {
      return 15 + (p_77321_1_ - 1) * 9;
   }

   public int getMaxEnchantability(int p_77317_1_) {
      return super.getMinEnchantability(p_77317_1_) + 50;
   }

   public int getMaxLevel() {
      return 3;
   }
}
