package store.shadowclient.client.module.player;

import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;

public class FastWeb extends Module {

	public FastWeb() {
		super("FastWeb", 0, Category.PLAYER);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		 if ((mc.thePlayer.fallDistance > 3.0F) && (!mc.thePlayer.onGround)) {
			mc.thePlayer.motionY = -4.17F;
		}
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onEnable() {
		onDisable();
	}
}
