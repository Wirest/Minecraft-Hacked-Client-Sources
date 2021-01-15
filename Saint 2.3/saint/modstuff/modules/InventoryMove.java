package saint.modstuff.modules;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import saint.eventstuff.Event;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;

public class InventoryMove extends Module {
   public InventoryMove() {
      super("InventoryMove", -3308225, ModManager.Category.MOVEMENT);
      this.setTag("Inventory Move");
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion && mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) {
         KeyBinding[] moveKeys = new KeyBinding[]{mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump};
         KeyBinding[] array = moveKeys;
         int length = moveKeys.length;

         for(int i = 0; i < length; ++i) {
            KeyBinding bind = array[i];
            KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
         }
      }

   }
}
