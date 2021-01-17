package Blizzard.Mod.mods.movement;

import Blizzard.Category.Category;
import Blizzard.Event.EventTarget;
import Blizzard.Event.events.EventUpdate;
import Blizzard.Mod.Mod;

public class NoFall extends Mod {
	public NoFall() {
		super("NoFall", "NoFall", 0, Category.PLAYER);
	}

	@EventTarget
	public void onUpdate(EventUpdate e) {
		if (NoFall.mc.thePlayer.fallDistance >= 2.0f) {
			NoFall.mc.thePlayer.motionY = -9.9;
		}
	}
}
