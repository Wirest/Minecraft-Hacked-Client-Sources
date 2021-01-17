package me.ihaq.iClient.modules.Player;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventUpdate;
import me.ihaq.iClient.modules.Module;

public class Respawn extends Module {
	public Respawn() {
		super("Respawn", Keyboard.KEY_NONE, Category.PLAYER, "");
	}

	@EventTarget
	public void onUpdate(EventUpdate e) {

		if (!this.isToggled())
			return;

		if (!mc.thePlayer.isEntityAlive()) {
			mc.thePlayer.respawnPlayer();
		}
	}
}
