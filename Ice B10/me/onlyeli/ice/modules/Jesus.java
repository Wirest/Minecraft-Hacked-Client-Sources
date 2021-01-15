package me.onlyeli.ice.modules;

import org.lwjgl.input.Keyboard;

import me.onlyeli.eventapi.EventManager;
import me.onlyeli.eventapi.EventTarget;
import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.events.EventUpdate;
import me.onlyeli.ice.utils.BlockUtils;
import me.onlyeli.ice.utils.Wrapper;

public class Jesus extends Module {

	public Jesus() {
		super("Jesus", Keyboard.KEY_DOWN, Category.MOVEMENT);
	}

	public void onEnable() {
		EventManager.register(this);
	}

	@Override
	public void onDisable() {
		EventManager.unregister(this);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (BlockUtils.isInLiquid() && !Wrapper.mc.thePlayer.isSneaking()) {
			Wrapper.mc.thePlayer.motionY = 0.0;
			Wrapper.mc.thePlayer.jumpMovementFactor *= 1.12F;
		}
	}

}
