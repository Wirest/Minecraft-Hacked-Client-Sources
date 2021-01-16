package org.m0jang.crystal.Mod.Collection.World;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.ChatUtils;

public class TrollPot extends Module {
   public TrollPot() {
      super("TrollPot", Category.World, false);
   }

   public void onEnable() {
      if (Minecraft.thePlayer.inventory.getStackInSlot(0) != null) {
         ChatUtils.sendMessageToPlayer("Please clear the first slot in your hotbar.");
         this.setEnabled(false);
      } else if (!Minecraft.thePlayer.capabilities.isCreativeMode) {
         ChatUtils.sendMessageToPlayer("Creative mode only.");
         this.setEnabled(false);
      } else {
         ItemStack stack = new ItemStack(Items.potionitem, 64);
         stack.setItemDamage(16384);
         NBTTagList effects = new NBTTagList();

         for(int i = 1; i <= 23; ++i) {
            NBTTagCompound effect = new NBTTagCompound();
            effect.setInteger("Amplifier", Integer.MAX_VALUE);
            effect.setInteger("Duration", Integer.MAX_VALUE);
            effect.setInteger("Id", i);
            effects.appendTag(effect);
         }

         stack.setTagInfo("CustomPotionEffects", effects);
         stack.setStackDisplayName("Â\2474Troll Â\247cPot.");
         Minecraft.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, stack));
         ChatUtils.sendMessageToPlayer("Potion created. Trololo!");
         this.setEnabled(false);
      }
   }
}
