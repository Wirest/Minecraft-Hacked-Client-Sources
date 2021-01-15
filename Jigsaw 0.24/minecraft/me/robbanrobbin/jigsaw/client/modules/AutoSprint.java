package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;

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
			if(Jigsaw.getModuleByName("NoSlowdown").isToggled()) {
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
			if(Jigsaw.getModuleByName("NoSlowdown").isToggled()) {
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

}
