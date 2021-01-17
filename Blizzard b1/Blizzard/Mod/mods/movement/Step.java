package Blizzard.Mod.mods.movement;

import org.lwjgl.input.Keyboard;

import Blizzard.Category.Category;
import Blizzard.Event.EventTarget;
import Blizzard.Event.events.EventUpdate;
import Blizzard.Mod.Mod;

public class Step extends Mod {
	public Step() {
		super("Step", "Step", Keyboard.KEY_NONE, Category.MOVEMENT);

	}

	@EventTarget
	public void onUpdate(EventUpdate e) {
		if (mc.thePlayer.isCollidedHorizontally) {
			mc.thePlayer.motionY = 0.42;
		}
	}
}