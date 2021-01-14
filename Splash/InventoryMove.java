package splash.client.modules.movement;

import org.lwjgl.input.Keyboard;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.client.events.player.EventPlayerUpdate;

/**
 * Author: Pulse ( i skid from dort ha ) Created: 22:44, 10-Jun-20 Project:
 * Client
 */
public class InventoryMove extends Module {

	public InventoryMove() {
		super("InventoryMove", "Lets you move in your inventory.", ModuleCategory.MOVEMENT);
	}

	@Collect
	public void onUpdatePlayer(EventPlayerUpdate eventPlayerUpdate) {

		if (this.mc.currentScreen instanceof GuiChat) {
			return;
		}

		KeyBinding keyBindings[] = new KeyBinding[] { mc.gameSettings.keyBindForward, mc.gameSettings.keyBindRight,
				mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint };

		for (KeyBinding keyBinding : keyBindings) {
			keyBinding.pressed = Keyboard.isKeyDown(keyBinding.getKeyCode());
		}

	}

}
