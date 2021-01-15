package dev.astroclient.client.util.render.animation;

public class AnimationUtil {

    // IDK where this is from but I cant remember making it so it's probably skidded

    public static float calculateCompensation(float target, float current, long delta, int speed) {
        float diff = current - target;
        if (delta < 1L) {
            delta = 1L;
        }

        double xD;
        if (diff > (float)speed) {
            xD = (double)((long)speed * delta / 16L) < 0.25D ? 0.5D : (double)((long)speed * delta / 16L);
            current = (float)((double)current - xD);
            if (current < target) {
                current = target;
            }
        } else if (diff < (float)(-speed)) {
            xD = (double)((long)speed * delta / 16L) < 0.25D ? 0.5D : (double)((long)speed * delta / 16L);
            current = (float)((double)current + xD);
            if (current > target) {
                current = target;
            }
        } else {
            current = target;
        }

        return current;
    }
}
