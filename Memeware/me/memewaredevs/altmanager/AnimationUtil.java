
package me.memewaredevs.altmanager;

public class AnimationUtil {
    public static float calculateCompensation(float target, float current, long delta, int speed) {
        float diff = current - target;
        if (delta < 9L) {
            delta = 9L;
        }
        if (diff > (float) speed) {
            double xD = (double) ((long) speed * delta / 16L) < 0.05 ? 0.05 : (double) ((long) speed * delta / 16L);
            current -= (float) xD;
            if (current < target) {
                current = target;
            }
        } else if (diff < (float) (-speed)) {
            double xD = (double) ((long) speed * delta / 16L) < 0.05 ? 0.05 : (double) ((long) speed * delta / 16L);
            current += (float) xD;
            if (current > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }
}

