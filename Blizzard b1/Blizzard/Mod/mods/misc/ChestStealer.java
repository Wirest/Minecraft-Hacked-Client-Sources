package Blizzard.Mod.mods.misc;

import org.lwjgl.input.Keyboard;

import Blizzard.Category.Category;
import Blizzard.Event.EventTarget;
import Blizzard.Event.events.EventUpdate;
import Blizzard.Mod.Mod;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;

public class ChestStealer extends Mod {
	public ChestStealer() {
		super("ChestStealer", "ChestStealer", Keyboard.KEY_C, Category.MISC);
	}

	int slot = 0;
	int delay;

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mc.currentScreen instanceof GuiChest) {

			ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;

			delay++;
			slot++;

			if (slot > chest.getLowerChestInventory().getSizeInventory()) {
				slot = 0;
			}

			if (delay > 1 && (chest.getLowerChestInventory().getStackInSlot(slot) != null)) {

				mc.playerController.windowClick(chest.windowId, slot, 0, 1, mc.thePlayer);

				delay = 0;

			}
		}
	}
}
