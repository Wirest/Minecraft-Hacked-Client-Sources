package net.minecraft.client.gui.inventory;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class CreativeCrafting implements ICrafting {
   private final Minecraft mc;
   private static final String __OBFID = "CL_00000751";

   public CreativeCrafting(Minecraft mc) {
      this.mc = mc;
   }

   public void updateCraftingInventory(Container p_71110_1_, List p_71110_2_) {
   }

   public void sendSlotContents(Container p_71111_1_, int p_71111_2_, ItemStack p_71111_3_) {
      this.mc.playerController.sendSlotPacket(p_71111_3_, p_71111_2_);
   }

   public void sendProgressBarUpdate(Container p_71112_1_, int p_71112_2_, int p_71112_3_) {
   }

   public void func_175173_a(Container p_175173_1_, IInventory p_175173_2_) {
   }
}
