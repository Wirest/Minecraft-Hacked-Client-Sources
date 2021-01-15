package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;

public class AutoParkour extends Module {

	public AutoParkour() {
		super("AutoParkour", Keyboard.KEY_NONE, Category.AUTOMATION,
				"Tries to jump to a block automatically. (Select block by right clicking it)");
	}

}
