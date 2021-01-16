package org.m0jang.crystal.Mod.Collection.Misc;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Values.Value;

public class Palalyze extends Module {
   public static Value Mode = new Value("Palalyze", String.class, "Mode", "Normal", new String[]{"Normal", "Silent"});

   public Palalyze() {
      super("Palalyze", Category.Misc, false);
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (Minecraft.thePlayer.onGround) {
         int index;
         if (Mode.getSelectedOption().equals("Normal")) {
            for(index = 0; index < 1500; ++index) {
               this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 1.0E-9D, Minecraft.thePlayer.posZ, false));
               this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, false));
            }
         } else {
            for(index = 0; index < 4000; ++index) {
               Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer(false));
            }
         }
      }

   }
}
