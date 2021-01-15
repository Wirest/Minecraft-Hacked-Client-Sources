package saint.eventstuff.events;

import java.util.Iterator;
import saint.Saint;
import saint.comandstuff.commands.Ghost;
import saint.eventstuff.Event;
import saint.modstuff.Module;
import saint.modstuff.modules.HUD;
import saint.tabgui.TabGui;

public class PressedKey extends Event {
   private final int key;

   public PressedKey(int key) {
      this.key = key;
   }

   public void checkKey() {
      if (this.key != 0) {
         if (this.key == ((Boolean)HUD.tabguikeys.getValueState() ? 200 : 72)) {
            TabGui.parseKeyUp();
         }

         if (this.key == ((Boolean)HUD.tabguikeys.getValueState() ? 208 : 80)) {
            TabGui.parseKeyDown();
         }

         if (this.key == ((Boolean)HUD.tabguikeys.getValueState() ? 203 : 75)) {
            TabGui.parseKeyLeft();
         }

         if (this.key == ((Boolean)HUD.tabguikeys.getValueState() ? 205 : 77)) {
            TabGui.parseKeyRight();
         }

         if (this.key == 28) {
            TabGui.parseKeyToggle();
         }

         Iterator var2 = Saint.getModuleManager().getContentList().iterator();

         while(var2.hasNext()) {
            Module mod = (Module)var2.next();
            if (mod.getKeybind() != 0 && this.key == mod.getKeybind() && !Ghost.shouldGhost) {
               mod.toggle();
            }
         }

      }
   }

   public int getKey() {
      return this.key;
   }
}
