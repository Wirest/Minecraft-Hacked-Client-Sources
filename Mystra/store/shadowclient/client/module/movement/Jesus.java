package store.shadowclient.client.module.movement;

import org.lwjgl.input.Keyboard;

import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;

public class Jesus extends Module{
	public Jesus() {
		super("Jesus", Keyboard.KEY_L, Category.MOVEMENT);
	}
}
