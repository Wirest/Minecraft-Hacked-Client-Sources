package rip.autumn.module.impl.player;

import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import rip.autumn.annotations.Label;
import rip.autumn.events.packet.ReceivePacketEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Category;

@Label("Anti Desync")
@Category(ModuleCategory.PLAYER)
@Aliases({"antidesync", "norotations"})
public final class AntiDesyncMod extends Module {
   @Listener(ReceivePacketEvent.class)
   public void onEvent(ReceivePacketEvent event) {
      if (event.getPacket() instanceof S08PacketPlayerPosLook) {
         S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.getPacket();
         packet.setYaw(mc.thePlayer.rotationYaw);
         packet.setPitch(mc.thePlayer.rotationPitch);
      }

   }
}
