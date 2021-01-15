package saint.modstuff.modules;

import net.minecraft.network.play.server.S02PacketChat;
import saint.eventstuff.Event;
import saint.eventstuff.events.DrawScreen;
import saint.eventstuff.events.RecievePacket;
import saint.modstuff.Module;
import saint.utilities.NahrFont;
import saint.utilities.RenderHelper;
import saint.utilities.TimeHelper;

public class LagDetector extends Module {
   private final TimeHelper time = new TimeHelper();

   public TimeHelper getTime() {
      return this.time;
   }

   public LagDetector() {
      super("LagDetector");
      this.setEnabled(true);
   }

   public void onEvent(Event event) {
      if (event instanceof DrawScreen) {
         if (this.time.hasReached(1000L)) {
            RenderHelper.getNahrFont().drawString("Lag: §7" + (this.time.getCurrentMS() - this.time.getLastMS()) + "ms", 2.0F, -2.0F, NahrFont.FontType.SHADOW_THICK, -1, -16777216);
         }
      } else if (event instanceof RecievePacket) {
         RecievePacket receive = (RecievePacket)event;
         if (!(receive.getPacket() instanceof S02PacketChat)) {
            this.time.reset();
         }
      }

   }
}
