package org.m0jang.crystal.Mod.Collection.Render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import org.m0jang.crystal.Events.EventPacketReceive;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Values.Value;

public class Ambience extends Module {
   public static Value time;

   static {
      time = new Value("Ambience", Float.TYPE, "Time", 16000.0F, 1.0F, 16000.0F, 1.0F);
   }

   public Ambience() {
      super("Ambience", Category.Render, false);
   }

   @EventTarget
   public void onRecieve(EventPacketReceive e) {
      if (e.getPacket() instanceof S03PacketTimeUpdate) {
         e.setCancelled(true);
      }

   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      Minecraft.theWorld.setWorldTime((long)time.getIntValue());
   }
}
