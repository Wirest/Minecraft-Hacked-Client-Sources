package me.onlyeli.ice.modules;

import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.utils.Wrapper;

import org.lwjgl.input.Keyboard;

public class Sprint extends Module {

	public Sprint() {
		super("Sprint", Keyboard.KEY_R, Category.PLAYER);
	}

	public void onUpdate() {
		//if (!this.getState()) {
		//	return;
		//}
		if (!(Wrapper.mc.thePlayer.isCollidedHorizontally) && Wrapper.mc.thePlayer.moveForward > 0.0f) {
			Wrapper.mc.thePlayer.setSprinting(true);
		} else {
			Wrapper.mc.thePlayer.setSprinting(false);
		}
	}

}
