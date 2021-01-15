package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.gui.ScreenPos;
import me.xatzdevelopments.xatz.module.Module;

public class Coords extends Module {

	public Coords() {
		super("Coords", Keyboard.KEY_NONE, Category.RENDER, "Displays your coordinates.");
	}

	@Override
	public void onRender() {
		if(!Xatz.getModuleByName("ModernHotbar").isToggled()) {
		Xatz.getUIRenderer().addToQueue(String.valueOf("§dX: §r" + (int) mc.thePlayer.posX), ScreenPos.LEFTUP);
		Xatz.getUIRenderer().addToQueue(String.valueOf("§dY: §r" + (int) mc.thePlayer.posY), ScreenPos.LEFTUP);
		Xatz.getUIRenderer().addToQueue(String.valueOf("§dZ: §r" + (int) mc.thePlayer.posZ), ScreenPos.LEFTUP);
		}
		super.onRender();
	}

}
