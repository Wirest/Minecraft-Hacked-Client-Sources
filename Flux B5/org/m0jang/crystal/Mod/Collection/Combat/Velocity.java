package org.m0jang.crystal.Mod.Collection.Combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.m0jang.crystal.Events.EventPacketReceive;
import org.m0jang.crystal.Events.EventTick;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Values.Value;

public class Velocity extends Module {
   public static Value hypixel;
   public static Value amplifier;

   static {
      hypixel = new Value("Velocity", Boolean.TYPE, "Hypixel", false);
      amplifier = new Value("Velocity", Float.TYPE, "Amount (%)", 0.0F, -200.0F, 200.0F, 10.0F);
   }

   public Velocity() {
      super("Velocity", Category.Combat, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onTick(EventTick event) {
      this.setRenderName(this.getName() + " \247f" + amplifier.getFloatValue() + "%");
   }

   @EventTarget
   public void onPacketTake(EventPacketReceive event) {
      if (event.packet instanceof S12PacketEntityVelocity) {
         S12PacketEntityVelocity packet = (S12PacketEntityVelocity)event.packet;
         if (Minecraft.theWorld.getEntityByID(packet.func_149412_c()) == Minecraft.thePlayer) {
            packet.x = (int)((double)packet.x * ((double)amplifier.getFloatValue() / 100.0D));
            packet.y = (int)((double)packet.y * ((double)amplifier.getFloatValue() / 100.0D));
            packet.z = (int)((double)packet.z * ((double)amplifier.getFloatValue() / 100.0D));
         }
      }

      if (Minecraft.theWorld != null && hypixel.getBooleanValue() && event.getPacket() instanceof S27PacketExplosion) {
         event.setCancelled(true);
      }

   }
}
