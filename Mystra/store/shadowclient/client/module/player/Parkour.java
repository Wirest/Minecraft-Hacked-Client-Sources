package store.shadowclient.client.module.player;

import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;

public class Parkour extends Module {

	public Parkour() {
		super("Parkour", 0, Category.PLAYER);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
	    if ((mc.thePlayer.onGround) && (!mc.thePlayer.isSneaking()) && 
	    		(!mc.gameSettings.keyBindSneak.pressed)) {
	    	    if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -0.5D, 0.0D).expand(-0.001D, 0.0D, -0.001D)).isEmpty()) {
	    	    mc.thePlayer.jump();
	    	 }
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
