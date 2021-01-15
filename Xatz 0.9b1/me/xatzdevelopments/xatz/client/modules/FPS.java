package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.gui.ScreenPos;
import me.xatzdevelopments.xatz.module.Module;

public class FPS extends Module {

	public FPS() {
		super("FPS", Keyboard.KEY_NONE, Category.RENDER, "Shows your FPS.");
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onUpdate() {

		super.onUpdate();
	}

	@Override
	public void onRender() {
if(!Xatz.getModuleByName("ModernHotbar").isToggled()) {
	
		Xatz.getUIRenderer().addToQueue(String.valueOf("§dFPS: §r" + mc.getDebugFPS()), ScreenPos.LEFTUP);
}

		super.onRender();
	}

}
