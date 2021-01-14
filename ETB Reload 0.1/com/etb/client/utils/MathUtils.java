package com.etb.client.utils;

import net.minecraft.client.Minecraft;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

public class MathUtils
{
    private static Random rng;

    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public static double square(double in) {
        return in * in;
    }
    public static double getRandomInRange(double min, double max) {
        Random random = new Random();
        double range = max - min;
        double scaled = random.nextDouble() * range;
        if (scaled > max) {
            scaled = max;
        }
        double shifted = scaled + min;

        if (shifted > max) {
            shifted = max;
        }
        return shifted;
    }
    public static double roundDouble(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public static float roundFloat(final float value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
    public static double getHighestOffset(double max)
    {
        for (double i = 0.0D; i < max; i += 0.01D) {
            for (int offset : new int[] { -2, -1, 0, 1, 2 })
            {
                if (Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(Minecraft.getMinecraft().thePlayer.motionX * offset, i, Minecraft.getMinecraft().thePlayer.motionZ * offset)).size() > 0) {
                    return i - 0.01D;
                }
            }
        }
        return max;
    }
    public static float[] constrainAngle(float[] vector) {

        vector[0] = (vector[0] % 360F);
        vector[1] = (vector[1] % 360F);

        while (vector[0] <= -180) {
            vector[0] = (vector[0] + 360);
        }

        while (vector[1] <= -180) {
            vector[1] = (vector[1] + 360);
        }

        while (vector[0] > 180) {
            vector[0] =(vector[0] - 360);
        }

        while (vector[1]> 180) {
            vector[1] = (vector[1] - 360);
        }

        return vector;
    }
    public static float getRandomInRange(float min, float max) {
        Random random = new Random();
        float range = max - min;
        float scaled = random.nextFloat() * range;
        float shifted = scaled + min;
        return shifted;
    }

    public static int getRandomInRange(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public double doRound(final double d, final int r) {
        String round = "#";
        for (int i = 0; i < r; ++i) {
            round = String.valueOf(round) + ".#";
        }
        final DecimalFormat twoDForm = new DecimalFormat(round);
        return Double.valueOf(twoDForm.format(d));
    }

    public static int getMiddle(final int i, final int i2) {
        return (i + i2) / 2;
    }

    public static Random getRng() {
        return MathUtils.rng;
    }

    public static int getNumberFor(final int start, final int end) {
        if (end >= start) {
            return 0;
        }
        if (end - start < 0) {
            return 0;
        }
        return end - start;
    }

    public static double roundToPlace(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static float getRandom() {
        return MathUtils.rng.nextFloat();
    }

    public static int getRandom(final int cap) {
        return MathUtils.rng.nextInt(cap);
    }

    public static int getRandom(final int floor, final int cap) {
        return floor + MathUtils.rng.nextInt(cap - floor + 1);
    }
    public static double getRandomf(final double min, final double max) {
        return min + MathUtils.rng.nextDouble() * (max - min + 1.0);
    }

    public static double getMiddleDouble(final int i, final int i2) {
        return (i + i2) / 2.0;
    }

    public static double clamp(double value, double minimum, double maximum) {
        return value > maximum ? maximum : value < minimum ? minimum : value;
    }
    public static double normalizeAngle(double angle) {
        return (angle + 360) % 360;
    }

    public static float normalizeAngle(float angle) {
        return (angle + 360) % 360;
    }
    public static float clamp(float input, float max) {
        return clamp(input, max, -max);
    }
    public static float clamp(float input, float max, float min) {
        if (input > max)
            input = max;
        if (input < min)
            input = min;
        return input;
    }
    public static class NumberType {
        public static final byte SHORT = 0, BYTE = 1, INT = 2, FLOAT = 3, DOUBLE = 4, LONG = 5;

        /**
         * Get the NumberType by a number class
         */
        public static byte getByType(Class cls) {
            if (cls == Short.class) {
                return SHORT;
            } else if (cls == Byte.class) {
                return BYTE;
            } else if (cls == Integer.class) {
                return INT;
            } else if (cls == Float.class) {
                return FLOAT;
            } else if (cls == Double.class) {
                return DOUBLE;
            } else if (cls == Long.class) {
                return LONG;
            } else {
                return -1;
            }
        }
    }
    public static boolean parsable(String s, byte type) {
        try {
            switch (type) {
                case NumberType.SHORT:
                    Short.parseShort(s);
                    break;
                case NumberType.BYTE:
                    Byte.parseByte(s);
                    break;
                case NumberType.INT:
                    Integer.parseInt(s);
                    break;
                case NumberType.FLOAT:
                    Float.parseFloat(s);
                    break;
                case NumberType.DOUBLE:
                    Double.parseDouble(s);
                    break;
                case NumberType.LONG:
                    Long.parseLong(s);
                    break;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static int wrapAngleToDirection(final float yaw, final int zones) {
        int angle = (int) (yaw + 360 / (2 * zones) + 0.5) % 360;
        if (angle < 0) {
            angle += 360;
        }
        return angle / (360 / zones);
    }
    static {
        MathUtils.rng = new Random();
    }
}
