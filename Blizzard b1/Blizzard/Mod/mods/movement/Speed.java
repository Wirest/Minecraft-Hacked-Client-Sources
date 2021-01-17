
package Blizzard.Mod.mods.movement;

import org.lwjgl.input.Keyboard;

import Blizzard.Category.Category;
import Blizzard.Event.EventTarget;
import Blizzard.Event.events.EventUpdate;
import Blizzard.Mod.Mod;
import Blizzard.Utils.MovementUtils;

public class Speed extends Mod {
	public Speed() {
		super("Speed §7Mineplex", "Speed", Keyboard.KEY_D, Category.MOVEMENT);

	}

	@EventTarget
	public void onUpdate(EventUpdate e) {
		if (mc.thePlayer.onGround) {
			mc.thePlayer.setSprinting(true);
			mc.thePlayer.onGround = true;
			setSpeed(0.305);
			if (mc.thePlayer.isCollidedVertically) {
				mc.thePlayer.motionY = 0.4;
			}
		}
	}

	public void setSpeed(Number speed) {
		MovementUtils.setSpeed(speed.doubleValue());
	}
}
