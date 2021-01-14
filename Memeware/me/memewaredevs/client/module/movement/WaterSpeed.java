
package me.memewaredevs.client.module.movement;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.MovementEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.movement.MovementUtils;

import java.util.function.Consumer;

public class WaterSpeed extends Module {
    public static double waterSpeed;
    public WaterSpeed() {
        super("Water Speed", 0, Module.Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        waterSpeed = 0.1;
    }

    @Handler
    public Consumer<MovementEvent> moveListener = event -> {
        if (mc.thePlayer.isInWater()) {
            event.setY(mc.thePlayer.motionY = 0.42F);
            waterSpeed = 0.5D;
            MovementUtils.setMoveSpeed(event, waterSpeed);
        }
        else {
            if (waterSpeed > 0.3) {
                MovementUtils.setMoveSpeed(event, waterSpeed *= 0.99);
            }
        }
    };

}