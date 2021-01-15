package saint.modstuff.modules;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import saint.eventstuff.Event;
import saint.eventstuff.events.PacketSent;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.TimeHelper;

public class XCarry extends Module {
   private final TimeHelper time = new TimeHelper();
   private boolean inInventory;

   public XCarry() {
      super("XCarry", -6724045, ModManager.Category.PLAYER);
      this.setTag("XCarry");
   }

   public void onEvent(Event event) {
      if (event instanceof PacketSent) {
         PacketSent sent = (PacketSent)event;
         if (sent.getPacket() instanceof C0DPacketCloseWindow) {
            sent.setCancelled(true);
         }
      } else if (event instanceof PreMotion) {
         if (mc.currentScreen instanceof GuiInventory) {
            this.inInventory = true;
         } else {
            this.inInventory = false;
         }

         if (mc.currentScreen instanceof GuiInventory) {
            mc.playerController.updateController();
         }
      }

   }
}
