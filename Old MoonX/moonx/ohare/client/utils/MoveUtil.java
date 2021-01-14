package moonx.ohare.client.utils;

import moonx.ohare.client.event.impl.player.MotionEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;

public class MoveUtil {

    private static Minecraft mc = Minecraft.getMinecraft();

    public static void setJumpSpeed(float multiplier) {
        if (mc.thePlayer.isSprinting()) {
            float f = mc.thePlayer.rotationYaw * 0.017453292F;
            float speed = 0.2F * multiplier;
            mc.thePlayer.motionX -= (double)(MathHelper.sin(f) * speed);
            mc.thePlayer.motionZ += (double)(MathHelper.cos(f) * speed);
        }
        mc.thePlayer.isAirBorne = true;
    }

    public static void setMoveSpeed(final MotionEvent event, final double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setX(forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw)));
            event.setZ(forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw)));
        }
    }

    public static void TP(MotionEvent event, double speed, double y) {
        float yaw = mc.thePlayer.rotationYaw;
        final float forward = mc.thePlayer.moveForward;
        final float strafe = mc.thePlayer.moveStrafing;
        yaw += ((forward < 0.0f) ? 180 : 0);
        if (strafe < 0.0f) {
            yaw += ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        if (strafe > 0.0f) {
            yaw -= ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        float direction =  yaw * 0.017453292f;

        final double posX = mc.thePlayer.posX;
        final double posY = mc.thePlayer.posY;
        final double posZ = mc.thePlayer.posZ;
        final double raycastFirstX = -Math.sin(direction);
        final double raycastFirstZ = Math.cos(direction);
        final double raycastFinalX = raycastFirstX * speed;
        final double raycastFinalZ = raycastFirstZ * speed;
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX + raycastFinalX, posY + y, posZ + raycastFinalZ, mc.thePlayer.onGround));
        mc.thePlayer.setPosition(posX + raycastFinalX, posY + y, posZ + raycastFinalZ);
    }

    public static float getDirection() {
        float yaw = mc.thePlayer.rotationYaw;
        final float forward = mc.thePlayer.moveForward;
        final float strafe = mc.thePlayer.moveStrafing;
        yaw += ((forward < 0.0f) ? 180 : 0);
        if (strafe < 0.0f) {
            yaw += ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        if (strafe > 0.0f) {
            yaw -= ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        return yaw * 0.017453292f;
    }

    public static double square(final double in) {
        return in * in;
    }

    public static double getSpeed() {
        return Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ);
    }

    public static void setSpeed(final double speed) {
        mc.thePlayer.motionX = -MathHelper.sin(getDirection()) * speed;
        mc.thePlayer.motionZ = MathHelper.cos(getDirection()) * speed;
    }

}
