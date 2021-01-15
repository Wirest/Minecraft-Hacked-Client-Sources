package me.xatzdevelopments.xatz.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

/**
 * Created by Jonathan H. (Niada)
 *  -------------------------------
 * ethereal.rip @ 3:50PM - 3/18/2019
 *  -------------------------------
 *  
 *  Well not really created by, more like took a bunch of public math utils i got from stackoverflow and shit
 *  and put them into one fat ass class, they really do come in handly tho I'll say that much, now some of these I did actually make.
 **/
public class Mafs {

	private static final Random rng = new Random();

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch(NumberFormatException e) {
			return false;
		} catch(NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}
   
    public static Double clamp(double number, double min, double max) {
        if (number < min)
            return min;
        else if (number > max)
            return max;
        else
            return number;
    }

    public static Double getDifference(double num1, double num2) {
        if (num1 > num2) {
            double tempNum = num1;
            num1 = num2;
            num2 = tempNum;
        }
        return num2 - num1;
    }
	
	public static float randomSeed(long seed) {
        seed = System.currentTimeMillis() + seed;
        return 0.4F + (float)new Random(seed).nextInt(80000000) / 1.0E9F + 1.45E-9F;
    }
	public static float secRanFloat(float min, float max) {

		SecureRandom rand = new SecureRandom();

		return rand.nextFloat() * (max - min) + min;
	}

	public static int randInt(int min, int max) {

		SecureRandom rand = new SecureRandom();

		return rand.nextInt() * (max - min) + min;
	}

	public static double secRanDouble(double min, double max) {

		SecureRandom rand = new SecureRandom();

		return rand.nextDouble() * (max - min) + min;
	}

	public static float getAngleDifference(float direction, float rotationYaw) {
		float phi = Math.abs(rotationYaw - direction) % 360;
		float distance = phi > 180 ? 360 - phi : phi;
		return distance;
	}

	public static double getMiddle(double d, double e) {
		return (d + e) / 2;
	}

	public static float getMiddle(float i, float i1) {
		return (i + i1) / 2;
	}

	public static double getMiddleint(double d, double e) {
		return (d + e) / 2;
	}

	public static int getRandom(final int floor, final int cap) {
		return floor + Mafs.rng.nextInt(cap - floor + 1);
	}

	public static double getRandom(final double floor, final double cap) {
		return floor + Mafs.rng.nextInt((int) (cap - floor + 1));
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

	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
    public static double[] getExpandCoords(double x, double z, float forward, float strafe,double exp, int pos, boolean aircheck) {
    	BlockPos underPos = new BlockPos(x, Minecraft.getMinecraft().thePlayer.posY + pos, z);
        Block underBlock = Minecraft.getMinecraft().theWorld.getBlockState(underPos).getBlock();
        double xCalc = -999, zCalc = -999;
        double dist = 0;
        double expandDist = exp;
        while(!(underBlock.getMaterial().isReplaceable()) || !aircheck){
        	xCalc = x;
        	zCalc = z;
        	dist ++;
        	if(dist > expandDist){
        		dist = expandDist;
        	}
        	xCalc += (forward * 0.45 * Math.cos(Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw + 90.0f)) + strafe * 0.45 * Math.sin(Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw  + 90.0f))) * dist;
        	zCalc += (forward * 0.45 * Math.sin(Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw  + 90.0f)) - strafe * 0.45 * Math.cos(Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw  + 90.0f))) * dist;
        	if(dist == expandDist){
        		break;
        	}
        	underPos = new BlockPos(xCalc, Minecraft.getMinecraft().thePlayer.posY + pos, zCalc);
        	underBlock = Minecraft.getMinecraft().theWorld.getBlockState(underPos).getBlock();
        }
    	return new double[]{xCalc,zCalc};
    }

}
