package saint.modstuff.modules;

import saint.Saint;
import saint.eventstuff.Event;
import saint.eventstuff.events.EveryTick;
import saint.modstuff.ModManager;
import saint.modstuff.Module;

public class Sprint extends Module {
   public Sprint() {
      super("Sprint", -2894893, ModManager.Category.MOVEMENT);
      this.setEnabled(true);
   }

   public void onEvent(Event event) {
      if (event instanceof EveryTick) {
         KillAura aura = (KillAura)Saint.getModuleManager().getModuleUsingName("killaura");
         if (mc.thePlayer != null && mc.thePlayer.getFoodStats().getFoodLevel() > 6 && !mc.gameSettings.keyBindSneak.pressed && (mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed || mc.gameSettings.keyBindBack.pressed)) {
            mc.thePlayer.setSprinting(true);
         }
      }

   }
}
