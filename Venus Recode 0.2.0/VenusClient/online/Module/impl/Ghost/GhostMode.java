package VenusClient.online.Module.impl.Ghost;

import org.lwjgl.input.Keyboard;

import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;

public class GhostMode extends Module{

	public GhostMode() {
		super("GhostMode", "GhostMode", Category.GHOST, Keyboard.KEY_NONE);
		toggle();
	}

}
