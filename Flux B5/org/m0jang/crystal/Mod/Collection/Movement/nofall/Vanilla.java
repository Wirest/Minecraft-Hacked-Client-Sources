package org.m0jang.crystal.Mod.Collection.Movement.nofall;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.m0jang.crystal.Events.EventPacketSend;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.SubModule;
import org.m0jang.crystal.Utils.PlayerUtils;

public class Vanilla extends SubModule {
   private int motionDelay;

   public Vanilla() {
      super("Vanilla", "NoFall");
   }

   @EventTarget
   public void onMove(EventUpdate event) {
      if (Minecraft.thePlayer.fallDistance > 2.0F || PlayerUtils.getDistanceToFall() > 2.0D) {
         event.onGround = true;
      }

   }

   @EventTarget
   private void onSendPacket(EventPacketSend event) {
      if (event.packet instanceof C03PacketPlayer && (Minecraft.thePlayer.fallDistance > 2.0F || PlayerUtils.getDistanceToFall() > 2.0D)) {
         C03PacketPlayer packetPlayer = (C03PacketPlayer)event.packet;
         packetPlayer.onGround = true;
         event.packet = packetPlayer;
      }

   }
}
