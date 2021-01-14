package me.memewaredevs.client.module.movement;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.MovementEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.movement.MovementUtils;

import java.util.function.Consumer;

public class CustomSpeed extends Module {

	private double airSpeed;

	public CustomSpeed() {
        super("Custom Speed", 44, Module.Category.MOVEMENT);
        addDouble("MotionY", 0.376, 0, 1);
        addDouble("Ground Speed", 1.61, 0.1, 10);
        addDouble("Up Multiplier", 1.03, 0, 5);
		addDouble("Down Multiplier", 1, 0, 5);
		addDouble("Friction", 0.99, 0.2, 1);

    }
    
    @Handler
    public Consumer<MovementEvent> onUpdate = (event) -> {
		double y = mc.thePlayer.motionY;
		if (mc.thePlayer.isMovingOnGround()) {
			y = getDouble("MotionY");
			airSpeed = getDouble("Ground Speed");
		}
		if (y > 0.0) {
			y *= getDouble("Up Multiplier");
		} else {
			y *= getDouble("Down Multiplier");
		}

		event.setY(mc.thePlayer.motionY = y);
		airSpeed *= getDouble("Friction");
		airSpeed = Math.max(airSpeed, MovementUtils.getBaseMoveSpeed());
		MovementUtils.setMoveSpeed(event, airSpeed);
	};

}