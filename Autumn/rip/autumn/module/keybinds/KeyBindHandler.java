package rip.autumn.module.keybinds;

import java.util.List;
import me.zane.basicbus.api.annotations.Listener;
import rip.autumn.events.game.KeyPressEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleManager;

public final class KeyBindHandler {
   private final ModuleManager moduleManager;

   public KeyBindHandler(ModuleManager moduleManager) {
      this.moduleManager = moduleManager;
   }

   @Listener(KeyPressEvent.class)
   public final void onKeyPress(KeyPressEvent event) {
      List modules = this.moduleManager.getModules();
      int i = 0;

      for(int keysSize = modules.size(); i < keysSize; ++i) {
         Module module = (Module)modules.get(i);
         if (event.getKeyCode() == module.getKeyBind().getKeyCode()) {
            module.toggle();
         }
      }

   }
}
