package VenusClient.online.Module.impl.Movement;

import org.lwjgl.input.Keyboard;

import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;

public class TargetStrafe extends Module{

	public TargetStrafe() {
		super("TargetStrafe", "TargetStrafe", Category.MOVEMENT, Keyboard.KEY_NONE);
	}

}
