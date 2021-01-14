package info.sigmaclient.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MathUtils {

    public static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int randomize(int max, int min) {
        return -min + (int) (Math.random() * ((max - (-min)) + 1));
    }

    public static double getIncremental(double val, double inc) {
        double one = 1 / inc;
        return Math.round(val * one) / one;
    }

    public static boolean isInteger(Double variable) {
        return (variable == Math.floor(variable)) && !Double.isInfinite(variable);
    }

}
