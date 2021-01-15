package saint.modstuff.modules;

import net.minecraft.client.gui.GuiNewChat;
import saint.eventstuff.Event;
import saint.modstuff.Module;
import saint.modstuff.modules.addons.SaintNewChat;

public class SaintChat extends Module {
   public SaintChat() {
      super("SaintChat");
      this.setEnabled(true);
   }

   public void onDisabled() {
      mc.ingameGUI.persistantChatGUI = new GuiNewChat(mc);
   }

   public void onEnabled() {
      mc.ingameGUI.persistantChatGUI = new SaintNewChat(mc);
   }

   public void onEvent(Event event) {
   }
}
