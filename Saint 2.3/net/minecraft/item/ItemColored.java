package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemColored extends ItemBlock {
   private final Block field_150944_b;
   private String[] field_150945_c;
   private static final String __OBFID = "CL_00000003";

   public ItemColored(Block p_i45332_1_, boolean p_i45332_2_) {
      super(p_i45332_1_);
      this.field_150944_b = p_i45332_1_;
      if (p_i45332_2_) {
         this.setMaxDamage(0);
         this.setHasSubtypes(true);
      }

   }

   public int getColorFromItemStack(ItemStack stack, int renderPass) {
      return this.field_150944_b.getRenderColor(this.field_150944_b.getStateFromMeta(stack.getMetadata()));
   }

   public int getMetadata(int damage) {
      return damage;
   }

   public ItemColored func_150943_a(String[] p_150943_1_) {
      this.field_150945_c = p_150943_1_;
      return this;
   }

   public String getUnlocalizedName(ItemStack stack) {
      if (this.field_150945_c == null) {
         return super.getUnlocalizedName(stack);
      } else {
         int var2 = stack.getMetadata();
         return var2 >= 0 && var2 < this.field_150945_c.length ? super.getUnlocalizedName(stack) + "." + this.field_150945_c[var2] : super.getUnlocalizedName(stack);
      }
   }
}
