package me.ihaq.iClient.modules.Movement;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventUpdate;
import me.ihaq.iClient.modules.Module;
import me.ihaq.iClient.utils.values.BooleanValue;

public class Sprint extends Module {

	public Sprint() {
		super("Sprint", Keyboard.KEY_NONE, Category.MOVEMENT, "");
	}

	public BooleanValue multiSprint = new BooleanValue(this, "MultiSprint", "multisprint", Boolean.valueOf(true));

	@EventTarget
	public void onUpdate(EventUpdate e) {
		if (!this.isToggled()) {
			return;
		}

		if (multiSprint.getValue()) {
			setMode(": \u00A7fMultiSprint");
			if (mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindBack.pressed
					|| mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed) {
				mc.thePlayer.setSprinting(true);
			}
		} else {
			setMode(": \u00A7fUniSprint");
			if(mc.gameSettings.keyBindForward.pressed) {
				mc.thePlayer.setSprinting(true);
			}
		}

	}

}
