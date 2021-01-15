package saint.modstuff.modules;

import saint.Saint;
import saint.eventstuff.Event;
import saint.modstuff.Module;

public class GUI extends Module {
   public GUI() {
      super("GUI");
   }

   public void onEnabled() {
      if (mc.theWorld != null) {
         mc.displayGuiScreen(Saint.getClickGui());
         this.setEnabled(false);
      }

   }

   public void onEvent(Event event) {
   }
}
