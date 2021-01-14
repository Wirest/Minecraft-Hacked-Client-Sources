package cedo.util.movement;

import cedo.Wrapper;
import net.minecraft.client.Minecraft;

public class SpeedUtil {
    static Minecraft mc = Minecraft.getMinecraft();

    public static void setSpeed(float speed) {
        mc.thePlayer.motionX = -Math.sin(getDirection()) * (double) speed;
        mc.thePlayer.motionZ = Math.cos(getDirection()) * (double) speed;
    }

    public static float getDirection() {
        float var1 = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.moveForward < 0.0f) {
            var1 += 180.0f;
        }
        float forward;
        forward = mc.thePlayer.moveForward < 0.0f ? -0.5f : (mc.thePlayer.moveForward > 0.0f ? 0.5f : 1.0f);
        if (mc.thePlayer.moveStrafing > 0.0f) {
            var1 -= 90.0f * forward;
        }
        if (mc.thePlayer.moveStrafing < 0.0f) {
            var1 += 90.0f * forward;
        }
        return (float) (var1 * (Math.PI / 180.0F));
    }

    public void setWalkSpeed(double speed) {
        Wrapper.mc.thePlayer.capabilities.setPlayerWalkSpeed((float) speed);
    }

    public void setFlySpeed(double speed) {
        Wrapper.mc.thePlayer.capabilities.setFlySpeed((float) speed);
    }

    // TODO: Test this. If it works, make a PlayerTime module with a time NumberSetting
    public void daytimeSpeed(double speed) {
        Wrapper.mc.theWorld.setWorldTime((long) speed);
    }

}
