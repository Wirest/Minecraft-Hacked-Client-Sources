package org.m0jang.crystal.Mod.Collection.Misc;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.m0jang.crystal.Events.EventPacketReceive;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class FreeRotate extends Module {
   public FreeRotate() {
      super("FreeRotate", Category.Misc, false);
   }

   public void onEnable() {
      this.setRenderName("FreeRotate");
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onPacketTake(EventPacketReceive event) {
      if (event.packet instanceof S08PacketPlayerPosLook) {
         S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.packet;
         packet.setYaw(Minecraft.thePlayer.rotationYaw);
         packet.setPitch(Minecraft.thePlayer.rotationPitch);
      }

   }
}
