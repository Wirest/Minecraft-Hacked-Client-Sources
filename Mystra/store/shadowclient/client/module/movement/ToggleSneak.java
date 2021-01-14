package store.shadowclient.client.module.movement;

import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;

public class ToggleSneak extends Module {
	public ToggleSneak() {
		super("ToggleSneak", 0, Category.MOVEMENT);
	}

    @Override
    public void onEnable(){
    	mc.gameSettings.keyBindSneak.pressed = true;
    	}
    @Override
    public void onDisable(){
    	mc.gameSettings.keyBindSneak.pressed = false;
    }
}
