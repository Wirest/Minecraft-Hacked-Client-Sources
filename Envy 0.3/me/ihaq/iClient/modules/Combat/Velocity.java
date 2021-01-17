package me.ihaq.iClient.modules.Combat;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.EventManager;
import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventPlayerVelocity;
import me.ihaq.iClient.modules.Module;
import me.ihaq.iClient.modules.Module.Category;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class Velocity extends Module {

	public Velocity() {
		super("Velocity", Keyboard.KEY_NONE, Category.COMBAT, "");
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@EventTarget
	public void onUpdate(EventPlayerVelocity event) {
		if (!this.isToggled()) {
			return;
		}
		event.setCancelled(true);
	}

}
