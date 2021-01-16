package org.m0jang.crystal.Mod.Collection.Movement.nofall;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.SubModule;

public class Kohi extends SubModule {
   public Kohi() {
      super("Kohi", "NoFall");
   }

   @EventTarget
   private void onUpdate(EventUpdate event) {
      if (Minecraft.thePlayer.fallDistance > 3.0F) {
         Minecraft.thePlayer.isInWeb = true;
         Minecraft.thePlayer.moveEntity(0.0D, -1.0D, 0.0D);
         Minecraft.thePlayer.fallDistance = 0.0F;
      }

      if ((double)Minecraft.thePlayer.fallDistance > 0.3D) {
         for(int i = 0; i < 30; ++i) {
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
         }
      }

   }
}
