package saint.modstuff.modules;

import net.minecraft.network.play.client.C0BPacketEntityAction;
import saint.eventstuff.Event;
import saint.eventstuff.events.PostMotion;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;

public class Sneak extends Module {
   public Sneak() {
      super("Sneak", -16332525, ModManager.Category.PLAYER);
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         if (mc.thePlayer.motionX != 0.0D && mc.thePlayer.motionY != 0.0D && mc.thePlayer.motionZ != 0.0D && !mc.thePlayer.isSneaking()) {
            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
         }
      } else if (event instanceof PostMotion && !mc.thePlayer.isSneaking()) {
         mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
         mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
      }

   }

   public void onDisabled() {
      super.onDisabled();
      if (mc.thePlayer != null && !mc.thePlayer.isSneaking()) {
         mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
      }

   }
}
