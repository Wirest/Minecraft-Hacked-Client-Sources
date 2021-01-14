package store.shadowclient.client.module.movement;

import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;

public class BoatFly extends Module {

	public BoatFly() {
		super("BoatFly", 0, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mc.thePlayer.isRiding()) {
			mc.thePlayer.ridingEntity.motionY = -0.0;
            if (this.mc.gameSettings.keyBindJump.pressed) {
            	mc.thePlayer.ridingEntity.motionY = 0.3;
            }
            if (this.mc.gameSettings.keyBindSneak.pressed) {
            	mc.thePlayer.ridingEntity.motionY = -0.3;
			}
            if (this.mc.gameSettings.keyBindRight.pressed) {
            	mc.thePlayer.ridingEntity.motionX = -0.3;
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

