package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.gui.ScreenPos;
import me.xatzdevelopments.xatz.module.Module;

public class TPS extends Module {

	public TPS() {
		super("TPS", Keyboard.KEY_NONE, Category.RENDER,
				"Shows the server's TPS (Ticks per second). Lower TPS means more lag!");
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
		Xatz.getUIRenderer().addToQueue(String.valueOf("§dTPS: §r" + Math.round(Xatz.lastTps * 10.0) / 10.0), ScreenPos.LEFTUP);
		}
		super.onRender();
	}

}
