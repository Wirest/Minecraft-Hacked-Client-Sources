package rip.autumn.module.impl.combat;

import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import rip.autumn.annotations.Label;
import rip.autumn.events.packet.ReceivePacketEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Category;

@Label("Velocity")
@Category(ModuleCategory.COMBAT)
public final class VelocityMod extends Module {
   @Listener(ReceivePacketEvent.class)
   public final void onReceivePacket(ReceivePacketEvent event) {
      Packet packet = event.getPacket();
      if (packet instanceof S12PacketEntityVelocity || packet instanceof S27PacketExplosion) {
         event.setCancelled();
      }

   }
}
