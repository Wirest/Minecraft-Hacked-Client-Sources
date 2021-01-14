package modification.modules.movement;

import modification.enummerates.Category;
import modification.events.EventUpdate;
import modification.extenders.Module;
import modification.extenders.Value;
import modification.interfaces.Event;
import modification.utilities.RotationUtil;

public final class Strafe
        extends Module {
    private final Value<Boolean> intave = new Value("Intave", Boolean.valueOf(true), this, new String[0]);
    private boolean canceled;
    private boolean rotated;

    public Strafe(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventUpdate)) {
            if ((MC.thePlayer.hurtTime > 0) && (!MC.thePlayer.onGround)) {
                this.canceled = true;
            }
            if (MC.thePlayer.onGround) {
                this.canceled = false;
            }
            if (!this.canceled) {
                if (((MC.thePlayer.moveStrafing != 0.0F) || (MC.thePlayer.moveForward > 0.0F)) && (MC.thePlayer.hurtTime == 0)) {
                    float f = MC.thePlayer.rotationYaw;
                    if (MC.thePlayer.moveStrafing > 0.0F) {
                        f -= 90.0F / (MC.thePlayer.moveForward > 0.0F ? 2.0F : 1.0F);
                    } else if (MC.thePlayer.moveStrafing < 0.0F) {
                        f += 90.0F / (MC.thePlayer.moveForward > 0.0F ? 2.0F : 1.0F);
                    }
                    if (((Boolean) this.intave.value).booleanValue()) {
                        RotationUtil.currentRotation = RotationUtil.fixedRotations(f, MC.thePlayer.rotationPitch);
                        RotationUtil.moveToRotation = true;
                        this.rotated = true;
                    }
                    double d = Math.hypot(MC.thePlayer.motionX, MC.thePlayer.motionZ);
                    MC.thePlayer.motionX = (-Math.sin(Math.toRadians(f)) * d);
                    MC.thePlayer.motionZ = (Math.cos(Math.toRadians(f)) * d);
                }
                return;
            }
        }
        if (this.rotated) {
            this.rotated = false;
        }
    }

    protected void onDeactivated() {
    }
}




