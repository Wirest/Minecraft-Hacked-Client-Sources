package me.ihaq.iClient.modules.Movement;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventSlowDown;
import me.ihaq.iClient.modules.Module;

public class NoSlowDown extends Module {

	public NoSlowDown() {
		super("NoSlowDown", Keyboard.KEY_NONE, Category.MOVEMENT, "");
	}

	@EventTarget
	public void onEvent(EventSlowDown event) {
		if (!this.isToggled()) {
			return;
		}

		event.setCancelled(true);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}
}
