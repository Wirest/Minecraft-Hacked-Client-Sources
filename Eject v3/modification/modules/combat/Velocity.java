package modification.modules.combat;

import modification.enummerates.Category;
import modification.events.*;
import modification.extenders.Module;
import modification.extenders.Value;
import modification.interfaces.Event;
import modification.utilities.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovementInput;

public final class Velocity
        extends Module {
    public final Value<String> mode = new Value("Mode", "Reverse", new String[]{"Reverse", "None", "Push", "Legit", "New", "Other"}, this, new String[0]);
    private boolean shouldReduce;
    private float lastTimer;

    public Velocity(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    public static void setSpeed(double paramDouble, float paramFloat) {
        Minecraft localMinecraft = Minecraft.getMinecraft();
        MovementInput localMovementInput = localMinecraft.thePlayer.movementInput;
        float f1 = localMovementInput.moveForward;
        float f2 = localMovementInput.moveStrafe;
        if ((f1 == 0.0F) && (f2 == 0.0F)) {
            localMinecraft.thePlayer.motionX = 0.0D;
            localMinecraft.thePlayer.motionZ = 0.0D;
        } else if (f1 != 0.0F) {
            if (f2 >= 1.0F) {
                paramFloat += (f1 > 0.0F ? -45 : 45);
                f2 = 0.0F;
            } else if (f2 <= -1.0F) {
                paramFloat += (f1 > 0.0F ? 45 : -45);
                f2 = 0.0F;
            }
            if (f1 > 0.0F) {
                f1 = 1.0F;
            } else if (f1 < 0.0F) {
                f1 = -1.0F;
            }
        }
        double d1 = Math.cos(Math.toRadians(paramFloat + 90.0F));
        double d2 = Math.sin(Math.toRadians(paramFloat + 90.0F));
        double d3 = f1 * paramDouble * d1 + f2 * paramDouble * d2;
        double d4 = f1 * paramDouble * d2 - f2 * paramDouble * d1;
        localMinecraft.thePlayer.motionX = d3;
        localMinecraft.thePlayer.motionZ = d4;
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventVelocity)) {
            if ((((String) this.mode.value).equals("New")) && (this.shouldReduce)) {
                EventVelocity tmp36_33 = ((EventVelocity) paramEvent);
                tmp36_33.value = ((float) (tmp36_33.value * -1.15D));
            }
            ((EventVelocity) paramEvent).canceled = ((String) this.mode.value).equals("None");
        }
        if ((paramEvent instanceof EventTick)) {
            this.tag = ((String) this.mode.value);
        }
        if (((paramEvent instanceof EventPreMotion)) && (((String) this.mode.value).equals("Push")) && (MC.thePlayer.hurtTime > 0) && (MC.thePlayer.hurtTime < 10)) {
            MC.thePlayer.motionX *= 0.6D;
            MC.thePlayer.motionZ *= 0.6D;
            EntityPlayerSP tmp183_180 = MC.thePlayer;
            tmp183_180.jumpMovementFactor = ((float) (tmp183_180.jumpMovementFactor * 10.15D));
        }
        if (((paramEvent instanceof EventPostMotion)) && (((String) this.mode.value).equals("Legit"))) {
            if (MC.thePlayer.hurtTime == 9) {
                MC.thePlayer.motionX *= 0.5D;
                MC.thePlayer.motionZ *= 0.5D;
            }
            if (MC.thePlayer.hurtTime == 8) {
                MC.thePlayer.motionX *= 0.5D;
                MC.thePlayer.motionZ *= 0.5D;
            }
            if (MC.thePlayer.hurtTime == 7) {
                MC.thePlayer.motionX *= 0.4D;
                MC.thePlayer.motionZ *= 0.4D;
            }
        }
        if (((paramEvent instanceof EventUpdate)) && (MC.thePlayer.hurtTime == 10)) {
            this.lastTimer = MC.timer.timerSpeed;
            this.shouldReduce = (!this.shouldReduce);
        }
        float f;
        if ((paramEvent instanceof EventPostMotion)) {
            f = (RotationUtil.currentRotation != null) && (RotationUtil.moveToRotation) ? RotationUtil.currentRotation.yaw : MC.thePlayer.rotationYaw;
            if ("Reverse".equals(this.mode.value)) {
                if (MC.thePlayer.hurtTime == 9) {
                    MC.thePlayer.motionX *= 0.9D;
                    MC.thePlayer.motionZ *= 0.9D;
                }
                if (MC.thePlayer.hurtTime == 8) {
                    MC.thePlayer.motionX *= 0.8D;
                    MC.thePlayer.motionZ *= 0.8D;
                }
                if (MC.thePlayer.hurtTime == 5) {
                    MC.thePlayer.motionX = (-Math.sin(Math.toRadians(f)) * 0.1D);
                    MC.thePlayer.motionZ = (Math.cos(Math.toRadians(f)) * 0.1D);
                }
            }
        }
        if (((paramEvent instanceof EventPostMotion)) && (((String) this.mode.value).equals("Other")) && (this.shouldReduce) && (MC.thePlayer.hurtResistantTime == 13)) {
            f = (RotationUtil.currentRotation != null) && (RotationUtil.moveToRotation) ? RotationUtil.currentRotation.yaw : MC.thePlayer.rotationYaw;
            MC.thePlayer.motionX *= -0.5D;
            MC.thePlayer.motionZ *= -0.5D;
        }
    }

    protected void onDeactivated() {
    }
}




