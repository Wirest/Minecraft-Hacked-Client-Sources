package org.m0jang.crystal.Mod.Collection.Movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.m0jang.crystal.Events.EventMove;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.PlayerUtils;

public class NoVoid extends Module {
   public NoVoid() {
      super("NoVoid", Category.Movement, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onMove(EventMove event) {
      if (Minecraft.thePlayer.fallDistance > 7.0F && PlayerUtils.getDistanceToFall() == Minecraft.thePlayer.posY - 1.0D) {
         Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.3D, Minecraft.thePlayer.posZ, false));
      }

   }
}
