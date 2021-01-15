package nivia.modules.movement;

import nivia.Pandora;
import nivia.events.EventTarget;
import nivia.events.events.EventPostMotionUpdates;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.PropertyManager;
import nivia.modules.Module;
import nivia.utils.Helper;

public class Sprint extends Module {
	public Sprint() {
		super("Sprint", 0, 0x75FF47, Category.MOVEMENT, "Run nigger, run.", new String[] { "run", "spr" }, true);
	}

	public PropertyManager.Property<Boolean> omni = new PropertyManager.Property<Boolean>(this, "Omni Directional", true);

	@EventTarget
	public void onEvent(EventPreMotionUpdates pre) {

		if ((omni.value ? Helper.playerUtils().MovementInput() :  mc.thePlayer.moveForward > 0.0F) && !mc.thePlayer.isSneaking() && !mc.thePlayer.isCollidedHorizontally) {
				if(mc.thePlayer.moveForward <= 0.0F && mc.thePlayer.isCollidedVertically && !Pandora.getModManager().getModState("Speed")){
				mc.thePlayer.motionX *= 1.185;
				mc.thePlayer.motionZ *= 1.185;
			}
			mc.thePlayer.setSprinting(true);
		} else {
			mc.thePlayer.setSprinting(false);
		}
	}

	@EventTarget
	public void onPost(EventPostMotionUpdates post) {

	}
}
