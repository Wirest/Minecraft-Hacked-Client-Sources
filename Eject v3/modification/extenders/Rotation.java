package modification.extenders;

import modification.interfaces.MCHook;

public final class Rotation
        implements MCHook {
    public float yaw;
    public float pitch;
    public float lastYaw;
    public float lastPitch;

    public Rotation(float paramFloat1, float paramFloat2) {
        this.yaw = paramFloat1;
        this.pitch = paramFloat2;
    }

    public final void checkSensitivity() {
        float f1 = MC.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float f2 = f1 * f1 * f1 * 8.0F;
        this.yaw -= this.yaw % f2;
        this.pitch -= this.pitch % f2;
    }
}




