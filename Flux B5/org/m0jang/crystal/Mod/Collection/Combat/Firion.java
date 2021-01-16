package org.m0jang.crystal.Mod.Collection.Combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import org.m0jang.crystal.Events.EventTick;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.BlockUtils;

public class Firion extends Module {
   public Firion() {
      super("Firion", Category.Combat, false);
   }

   @EventTarget
   public void onTick(EventTick event) {
      if (Minecraft.thePlayer.isPotionActive(Potion.blindness.getId())) {
         Minecraft.thePlayer.removePotionEffect(Potion.blindness.getId());
      }

      if (Minecraft.thePlayer.isPotionActive(Potion.confusion.getId())) {
         Minecraft.thePlayer.removePotionEffect(Potion.confusion.getId());
      }

      if (Minecraft.thePlayer.isPotionActive(Potion.digSlowdown.getId())) {
         Minecraft.thePlayer.removePotionEffect(Potion.digSlowdown.getId());
      }

      if (!BlockUtils.isOnLiquid()) {
         if (Minecraft.thePlayer.isBurning()) {
            for(int i = 0; i < 10; ++i) {
               this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, true));
            }
         }

      }
   }
}
