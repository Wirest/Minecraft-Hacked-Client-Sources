package store.shadowclient.client.module.movement;

import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.player.PlayerUtils;

public class Strafe extends Module {

    public Strafe() {
        super("Strafe", 0, Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
    	if (this.mc.thePlayer.hurtTime > 0) {
            return;
        }
        PlayerUtils.setSpeed((float)Math.sqrt(this.mc.thePlayer.motionX * this.mc.thePlayer.motionX + this.mc.thePlayer.motionZ * this.mc.thePlayer.motionZ));
    }
}

