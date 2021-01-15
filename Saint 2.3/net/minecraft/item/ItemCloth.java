package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemCloth extends ItemBlock {
   private static final String __OBFID = "CL_00000075";

   public ItemCloth(Block p_i45358_1_) {
      super(p_i45358_1_);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public int getMetadata(int damage) {
      return damage;
   }

   public String getUnlocalizedName(ItemStack stack) {
      return super.getUnlocalizedName() + "." + EnumDyeColor.func_176764_b(stack.getMetadata()).func_176762_d();
   }
}
