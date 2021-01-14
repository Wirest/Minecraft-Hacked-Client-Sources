
package me.memewaredevs.client.module.movement;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;

import java.util.function.Consumer;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", 0, Module.Category.MOVEMENT);
        this.addBoolean("No Direction Check", true);
    }

    @Handler
    public Consumer<UpdateEvent> eventConsumer0 = (event) -> {
        if (this.getBool("No Direction Check")) {
            if (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0) {
                mc.thePlayer.setSprinting(true);
            }
        } else if (mc.thePlayer.moveForward > 0) {
            mc.thePlayer.setSprinting(true);
        }
    };

}
