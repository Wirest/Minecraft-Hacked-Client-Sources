
package me.memewaredevs.client.module.player;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.module.combat.AutoPotion;

import java.util.function.Consumer;

public class Derp extends Module {
    private float yaw;

    public Derp(final String name, final int key, final Module.Category category) {
        super(name, key, category);
    }

    @Handler
    public Consumer<UpdateEvent> onUpdate = (event) -> {
        if (AutoPotion.isPotting()) {
            return;
        }
        if (event.isPre()) {
            this.yaw -= 22.0f;
            if (this.yaw <= -180.0f) {
                this.yaw = 180.0f;
            }
            this.mc.thePlayer.renderYawOffset = this.yaw;
            this.mc.thePlayer.rotationYawHead = this.yaw;
            event.setYaw(this.yaw);
//			event.setPitch(180);
        }
    };
}
