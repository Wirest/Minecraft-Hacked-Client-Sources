package store.shadowclient.client.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public final class MathUtils {
    private static Random random = new Random();

    public static double getRandomInRange(double max, double min) {
        return min + (max - min) * random.nextDouble();
    }

    public static int getRandomInRange(int max, int min) {
        return (int) (min + (max - min) * random.nextDouble());
    }

    public static boolean isEven(int number) {
        return number % 2 == 0;
    }

    public static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
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

    public static float[] constrainAngle(final float[] vector) {
        vector[0] %= 360.0f;
        vector[1] %= 360.0f;
        while (vector[0] <= -180.0f) {
            vector[0] += 360.0f;
        }
        while (vector[1] <= -180.0f) {
            vector[1] += 360.0f;
        }
        while (vector[0] > 180.0f) {
            vector[0] -= 360.0f;
        }
        while (vector[1] > 180.0f) {
            vector[1] -= 360.0f;
        }
        return vector;
    }
}