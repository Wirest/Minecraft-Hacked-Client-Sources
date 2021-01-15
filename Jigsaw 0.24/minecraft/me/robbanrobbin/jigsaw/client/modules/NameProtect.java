package me.robbanrobbin.jigsaw.client.modules;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;

public class NameProtect extends Module {

	public static HashMap<String, String> replacements = new HashMap<String, String>();

	public NameProtect() {
		super("NameProtect", Keyboard.KEY_NONE, Category.PLAYER,
				"Hides names in the chat for you. Type '.nameprotect <name> <replacement>' to add a name. Make sure this hack "
				+ "is enabled before doing the command!");
	}

	@Override
	public void onClientLoad() {
		replacements.clear();
		super.onClientLoad();
	}

	@Override
	public void onDisable() {
		replacements.clear();
		super.onDisable();
	}
}
