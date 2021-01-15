package net.minecraft.item;

public class ItemBook extends Item {
   private static final String __OBFID = "CL_00001775";

   public boolean isItemTool(ItemStack stack) {
      return stack.stackSize == 1;
   }

   public int getItemEnchantability() {
      return 1;
   }
}
