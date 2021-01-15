package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;

public class Parkour extends Module {

	boolean jumped = false;
	
	public Parkour() {
		super("Parkour", Keyboard.KEY_NONE, Category.MOVEMENT, "Jumps when you reach the end of a block.");
	}

	@Override
	public void onUpdate() {
		if(jumped) {
			if(mc.thePlayer.onGround) {
				jumped = false;
			}
		}
		else {
			if(mc.gameSettings.keyBindJump.pressed) {
				jumped = true;
			}
			else {
				if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
						mc.thePlayer.boundingBox.addCoord(mc.thePlayer.motionX, mc.thePlayer.motionY, mc.thePlayer.motionZ))
						.isEmpty()) {
					mc.thePlayer.jump();
					jumped = true;
				}
			}
		}
		super.onUpdate();
	}
}
