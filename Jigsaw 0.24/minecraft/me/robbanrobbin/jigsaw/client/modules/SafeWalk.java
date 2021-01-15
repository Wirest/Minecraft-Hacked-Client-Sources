package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;

public class SafeWalk extends Module {

	public SafeWalk() {
		super("SafeWalk", Keyboard.KEY_NONE, Category.MOVEMENT, "Prevents you from falling from blocks, just like if you were sneaking.");
	}

}
