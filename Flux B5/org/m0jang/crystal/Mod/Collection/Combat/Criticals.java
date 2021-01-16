package org.m0jang.crystal.Mod.Collection.Combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.m0jang.crystal.Events.EventTick;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Values.Value;

public class Criticals extends Module {
   public static Value Mode = new Value("Criticals", String.class, "Mode", "Packet", new String[]{"Packet", "Minijump"});

   public Criticals() {
      super("Criticals", Category.Combat, Mode);
   }

   @EventTarget
   public void onTick(EventTick event) {
      this.setRenderName(this.getName() + " \247f" + Mode.getSelectedOption());
   }

   public static void critical() {
      if (Minecraft.thePlayer.isCollidedVertically) {
         if (Mode.getSelectedOption().equals("Packet")) {
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.0625D, Minecraft.thePlayer.posZ, false));
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, false));
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.001D, Minecraft.thePlayer.posZ, false));
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, false));
         } else if (Mode.getSelectedOption().equals("Minijump")) {
            Minecraft.thePlayer.onGround = false;
            Minecraft.thePlayer.motionY = 0.1D;
            Minecraft.thePlayer.fallDistance = 0.1F;
         }

      }
   }
}
