/*
 * Decompiled with CFR 0.150.
 */
package delta;

public class Class110 {
    public static float _hosting(float f, float f2, long l, double d) {
        float f3 = f2 - f;
        if (l < 244L - 337L + 197L + -103L) {
            l = 202L - 316L + 13L + 102L;
        }
        if (l > 94L - 156L + 56L + 1006L) {
            l = 252L - 342L + 237L - 120L + -11L;
        }
        if ((double)f3 > d) {
            double d2 = d * (double)l / 16.0 < 0.5 ? 0.5 : d * (double)l / 16.0;
            if ((f2 = (float)((double)f2 - d2)) < f) {
                f2 = f;
            }
        } else if ((double)f3 < -d) {
            double d3 = d * (double)l / 16.0 < 0.5 ? 0.5 : d * (double)l / 16.0;
            if ((f2 = (float)((double)f2 + d3)) > f) {
                f2 = f;
            }
        } else {
            f2 = f;
        }
        return f2;
    }
}

