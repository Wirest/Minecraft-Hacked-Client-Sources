package saint.modstuff.modules;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import saint.Saint;
import saint.eventstuff.Event;
import saint.eventstuff.events.RecievePacket;
import saint.modstuff.ModManager;
import saint.modstuff.Module;

public class NoRotationSet extends Module {
   public NoRotationSet() {
      super("NoRotationSet", -2448096, ModManager.Category.COMBAT);
      this.setTag("No Rotation Set");
   }

   public void onEvent(Event event) {
      if (event instanceof RecievePacket) {
         RecievePacket receive = (RecievePacket)event;
         if (receive.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook poslook = (S08PacketPlayerPosLook)receive.getPacket();
            if (mc.thePlayer != null && mc.thePlayer.rotationYaw != -180.0F && mc.thePlayer.rotationPitch != 0.0F && !Saint.getModuleManager().getModuleUsingName("phase").isEnabled()) {
               poslook.field_148936_d = mc.thePlayer.rotationYaw;
               poslook.field_148937_e = mc.thePlayer.rotationPitch;
            }
         }
      }

   }
}
