package store.shadowclient.client.module.player;

import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;

public class FastBreak extends Module {
	public FastBreak() {
		super("FastBreak", 0, Category.PLAYER);
	}

	@EventTarget
	public void onUpdate(EventUpdate event){
	if (mc.playerController.curBlockDamageMP > 0.8F) {
		    mc.playerController.curBlockDamageMP = 1.0F;
		}
		mc.playerController.blockHitDelay = 0;
	}

	@Override
	public void onDisable(){
		super.onDisable();
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
}
