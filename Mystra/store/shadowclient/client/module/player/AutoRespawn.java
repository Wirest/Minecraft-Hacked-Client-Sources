package store.shadowclient.client.module.player;

import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.ClientTickEvent;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;

public class AutoRespawn extends Module {
	public AutoRespawn() {
		super("AutoRespawn", 0, Category.PLAYER);
	}
	@EventTarget
	public void onTick(ClientTickEvent event) {
		if(mc.thePlayer == null) {
			return;
		}
        if (mc.thePlayer.isDead) {
        	mc.thePlayer.respawnPlayer();
        }
    }

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}
}