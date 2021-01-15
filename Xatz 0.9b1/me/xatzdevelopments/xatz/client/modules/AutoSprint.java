package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;

public class AutoSprint extends Module {

	public AutoSprint() {
		super("AutoSprint", Keyboard.KEY_V, Category.MOVEMENT, "Sprints everytime you walk.");
	}

	@Override
	public void onDisable() {
		mc.thePlayer.setSprinting(false);
		super.onDisable();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
	}
	
	@Override
	public void onLivingUpdate() {
		if (currentMode.equals("Forwards")) {
			if(Xatz.getModuleByName("NoSlowdown").isToggled()) {
				if (mc.gameSettings.keyBindForward.isKeyDown()) {
					mc.thePlayer.setSprinting(true);
				} else {
					mc.thePlayer.setSprinting(false);
				}
			}
			else {
				if (mc.gameSettings.keyBindForward.isKeyDown() && !mc.thePlayer.isUsingItem() && !mc.thePlayer.isSneaking()) {
					mc.thePlayer.setSprinting(true);
				} else {
					mc.thePlayer.setSprinting(false);
				}
			}
		} else {
			if(Xatz.getModuleByName("NoSlowdown").isToggled()) {
				if ((mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown()
						|| mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown())) {
					mc.thePlayer.setSprinting(true);
				} else {
					mc.thePlayer.setSprinting(false);
				}
			}
			else {
				if ((mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown()
						|| mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown()) && !mc.thePlayer.isUsingItem() && !mc.thePlayer.isSneaking()) {
					mc.thePlayer.setSprinting(true);
				} else {
					mc.thePlayer.setSprinting(false);
				}
			}
			
		}
		super.onLivingUpdate();
	}

	@Override
	public void onLateUpdate() {
		
		super.onLateUpdate();
	}

	@Override
	public String[] getModes() {
		return new String[] { "Forwards", "MultiDir" };
	}
	
	public String getModeName() {
		return "Mode: ";
	}

}
