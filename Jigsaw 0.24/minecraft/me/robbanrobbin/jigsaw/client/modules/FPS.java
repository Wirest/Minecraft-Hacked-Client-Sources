package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.gui.ScreenPos;
import me.robbanrobbin.jigsaw.module.Module;

public class FPS extends Module {

	public FPS() {
		super("FPS", Keyboard.KEY_NONE, Category.PLAYER, "Shows your FPS.");
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

		Jigsaw.getUIRenderer().addToQueue(String.valueOf("§6FPS: §r" + mc.getDebugFPS()), ScreenPos.LEFTUP);

		super.onRender();
	}

}
