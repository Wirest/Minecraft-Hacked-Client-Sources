package VenusClient.online.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public enum MathUtils {
    INSTANCE;
	
    public static double square(double motionX) {
        motionX *= motionX;
        return motionX;
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
    public static float secRanFloat(float min, float max) {

		SecureRandom rand = new SecureRandom();

		return rand.nextFloat() * (max - min) + min;
	}

	public static int secRanInt(int min, int max) {

		SecureRandom rand = new SecureRandom();

		return rand.nextInt() * (max - min) + min;
	}

	public static double secRanDouble(double min, double max) {

		SecureRandom rand = new SecureRandom();

		return rand.nextDouble() * (max - min) + min;
	}
    public static float getRandomInRange(float min, float max) {
        Random random = new Random();
        float range = max - min;
        float scaled = random.nextFloat() * range;
        return scaled + min;
    }
    public static int getRandomInRange(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
    public static double getIncre(double val, double inc) {
        double one = 1 / inc;
        return Math.round(val * one) / one;
    }
    public static int getRandomInteger(int maximum, int minimum) {
        return ((int) (Math.random() * (maximum - minimum))) + minimum;
    }
    public static double newRound(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
    public static double roundPlace(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public static double defaultSpeed() {
        double baseSpeed = 0.2873D;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
        }
        return baseSpeed;
    }
    public static int getJumpEffect() {
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump))
            return Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        else
            return 0;
    }
    public static double setRandom(final double min, final double max) {
        final Random random = new Random();
        return min + random.nextDouble() * (max - min);
    }

    public static float setRandom(final float min, final float max) {
        Random random = new Random();
        return min + random.nextFloat() * (max - min);
    }
    public static double roundToPlace(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public static double preciseRound(double value, double precision) {
        double scale = Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public static float round(float value, float offset) {
        return value = value % offset;
    }
    public static float round(float value, float value2, boolean random) {
        if (random) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                value -= value % value2;
            } else {
                value += value % value2;
            }
        } else {
            value -= value % value2;
        }
        return value;
    }
    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd2 = new BigDecimal(value);
        bd2 = bd2.setScale(places, RoundingMode.HALF_UP);
        return bd2.doubleValue();
    }
    public static double map(double val, double mx, double from, double to) {
        return Math.min(Math.max(from + (val / mx) * (to - from), from), to);
    }
}