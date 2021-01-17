/*
 */
package Blizzard.Mod.mods.movement;

import org.lwjgl.input.Keyboard;

import Blizzard.Category.Category;
import Blizzard.Event.EventTarget;
import Blizzard.Event.events.EventUpdate;
import Blizzard.Mod.Mod;
import Blizzard.Utils.Time;

public class Fly extends Mod {
	public Fly() {
		super("Fly §7AAC", "Fly", Keyboard.KEY_F, Category.MOVEMENT);
	}

	Time timer = new Time();

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mc.thePlayer.motionY <= -0.8f) {
			mc.thePlayer.motionY = 0.0f;
			mc.thePlayer.onGround = true;
		} else {
			mc.timer.timerSpeed = 1.0f;
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();
		mc.timer.timerSpeed = 1.0f;
	}
}
