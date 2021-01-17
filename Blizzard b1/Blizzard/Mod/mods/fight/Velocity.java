
package Blizzard.Mod.mods.fight;

import Blizzard.Category.Category;
import Blizzard.Event.EventTarget;
import Blizzard.Event.events.EventUpdate;
import Blizzard.Mod.Mod;

public class Velocity extends Mod {

	public Velocity() {
		super("Velocity", "Velocity", 0, Category.PLAYER);
	}

	private double motionX;
	private double motionZ;

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (this.mc.thePlayer.hurtTime == 9) {
			this.motionX = this.mc.thePlayer.motionX;
			this.motionZ = this.mc.thePlayer.motionZ;
		} else if (this.mc.thePlayer.hurtTime == 8) {
			this.mc.thePlayer.motionX = -this.motionX * 0.45D;
			this.mc.thePlayer.motionZ = -this.motionZ * 0.45D;
		}
	}
}
