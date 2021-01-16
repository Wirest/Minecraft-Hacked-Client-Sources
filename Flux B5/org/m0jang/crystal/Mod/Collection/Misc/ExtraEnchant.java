package org.m0jang.crystal.Mod.Collection.Misc;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.ChatUtils;

public class ExtraEnchant extends Module {
   public ExtraEnchant() {
      super("ExtraEnchant", Category.Misc, false);
   }

   public void onEnable() {
      if (!Minecraft.thePlayer.capabilities.isCreativeMode) {
         ChatUtils.sendMessageToPlayer("Creative mode only.");
         this.setEnabled(false);
      } else if (Minecraft.thePlayer.inventory.getStackInSlot(0) != null) {
         ChatUtils.sendMessageToPlayer("Please clear the first slot in your hotbar.");
         this.setEnabled(false);
      } else {
         ItemStack item = new ItemStack(Items.skull, 1, 3);
         item.addEnchantment(Enchantment.efficiency, 32767);
         Minecraft.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, item));
         this.setEnabled(false);
      }
   }
}
