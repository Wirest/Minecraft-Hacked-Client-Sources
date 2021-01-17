/*
 * Decompiled with CFR 0.150.
 */
package delta;

import java.util.Random;

public class Class91 {
    public static float _turns(float f, float f2) {
        Random random = new Random();
        float f3 = f2 - f;
        float f4 = random.nextFloat() * f3;
        float f5 = f4 + f;
        return f5;
    }

    public static double _adults(float f, float f2, float f3, float f4) {
        return Math.sqrt((f - f3) * (f - f3) + (f2 - f4) * (f2 - f4));
    }
}

