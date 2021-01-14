
package me.memewaredevs.client.module.movement;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.MovementEvent;
import me.memewaredevs.client.module.Module;

import java.util.function.Consumer;

public class HighJump extends Module {
    private boolean groundLastTick;

    public HighJump() {
        super("High Jump", 44, Module.Category.MOVEMENT);
        this.addDouble("Motion", 2.0, 0.4, 8.5);
    }

    @Handler
    public Consumer<MovementEvent> eventConsumer0 = event -> {
        if (mc.thePlayer.isCollidedVertically && mc.thePlayer.movementInput.isJumping()) {
            event.setY(mc.thePlayer.motionY = getDouble("Motion"));
        }
    };

}
