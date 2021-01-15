package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.gui.ScreenPos;
import me.robbanrobbin.jigsaw.module.Module;

public class Coords extends Module {

	public Coords() {
		super("Coords", Keyboard.KEY_NONE, Category.PLAYER, "Displays your coordinates.");
	}

	@Override
	public void onRender() {

		Jigsaw.getUIRenderer().addToQueue(String.valueOf("§6X: §r" + (int) mc.thePlayer.posX), ScreenPos.LEFTUP);
		Jigsaw.getUIRenderer().addToQueue(String.valueOf("§6Y: §r" + (int) mc.thePlayer.posY), ScreenPos.LEFTUP);
		Jigsaw.getUIRenderer().addToQueue(String.valueOf("§6Z: §r" + (int) mc.thePlayer.posZ), ScreenPos.LEFTUP);

		super.onRender();
	}

}
