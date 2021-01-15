package me.xatzdevelopments.xatz.utils;

import java.util.*;
import java.math.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;
import java.text.*;

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
    
    public static Block getBlockUnderPlayer(final EntityPlayer inPlayer, final double height) {
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ)).getBlock();
    }
    
    public static double getRandomInRange(final double min, final double max) {
        final Random random = new Random();
        final double range = max - min;
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
    
    public static double getHighestOffset(final double max) {
        for (double i = 0.0; i < max; i += 0.01) {
            for (final int offset : new int[] { -2, -1, 0, 1, 2 }) {
                if (Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(Minecraft.getMinecraft().thePlayer.motionX * offset, i, Minecraft.getMinecraft().thePlayer.motionZ * offset)).size() > 0) {
                    return i - 0.01;
                }
            }
        }
        return max;
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
    
    public static float getRandomInRange(final float min, final float max) {
        final Random random = new Random();
        final float range = max - min;
        final float scaled = random.nextFloat() * range;
        final float shifted = scaled + min;
        return shifted;
    }
    
    public static int getRandomInRange(final int min, final int max) {
        final Random rand = new Random();
        final int randomNum = rand.nextInt(max - min + 1) + min;
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
    
    public static double clamp(final double value, final double minimum, final double maximum) {
        return (value > maximum) ? maximum : ((value < minimum) ? minimum : value);
    }
    
    public static double normalizeAngle(final double angle) {
        return (angle + 360.0) % 360.0;
    }
    
    public static float normalizeAngle(final float angle) {
        return (angle + 360.0f) % 360.0f;
    }
    
    public static float clamp(final float input, final float max) {
        return clamp(input, max, -max);
    }
    
    public static float clamp(float input, final float max, final float min) {
        if (input > max) {
            input = max;
        }
        if (input < min) {
            input = min;
        }
        return input;
    }
    
    static {
        MathUtils.rng = new Random();
    }
}

