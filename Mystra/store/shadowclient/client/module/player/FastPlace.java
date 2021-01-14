package store.shadowclient.client.module.player;

import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;

public class FastPlace extends Module {
	public FastPlace() {
		super("Fastplace", 0, Category.PLAYER);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (this.isToggled()) {
			mc.rightClickDelayTimer = 0;
		}
	}

	public void onDisable() {
		mc.rightClickDelayTimer = 6;
			super.onDisable();
		}

	@Override
    public void onEnable() {
    	super.onEnable();
    }
}