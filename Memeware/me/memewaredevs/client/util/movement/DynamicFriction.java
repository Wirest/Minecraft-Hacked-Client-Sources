package me.memewaredevs.client.util.movement;

import me.memewaredevs.client.event.events.MovementEvent;
import net.minecraft.client.Minecraft;

public class DynamicFriction {

    protected static final Minecraft mc = Minecraft.getMinecraft();
    private double speed;
    private boolean prevOnGround;

    public void updateFriction(MovementEvent event, double jumpMotY, double mult, double friction, double distanceDiff) {
        if(mc.thePlayer.isMovingOnGround()) {
            event.setY(mc.thePlayer.motionY = jumpMotY);
            speed = Math.max(MovementUtils.getBaseMoveSpeed() * mult, speed * mult);
            prevOnGround = true;
        }
        else {
            if (prevOnGround) {
                speed -= distanceDiff * (speed - MovementUtils.getBaseMoveSpeed());
                prevOnGround = false;
            }
            else {
                speed -= speed / friction;
            }
        }
        MovementUtils.setMoveSpeed(event, speed);
    }
}
