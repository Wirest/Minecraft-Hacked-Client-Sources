package store.shadowclient.client.module.render;

import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;

public class Fullbright extends Module{
	public Fullbright() {
		super("Fullbright", 0, Category.RENDER);
	}

	public void onUpdate(EventUpdate e) {
		if(this.isToggled()) {
			mc.gameSettings.gammaSetting = 1000f;
		}else {
			mc.gameSettings.gammaSetting = 0f;
		}
	}

	@Override
    public void onEnable() {
		mc.gameSettings.gammaSetting = 1000f;
    	super.onEnable();
    }

	@Override
	public void onDisable() {
		mc.gameSettings.gammaSetting = 0f;
		super.onDisable();
	}
}
