package nivia.utils.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

public class MathUtils {
	private static Random rng = new Random();
	
    public static double round(double value, int places)
    {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public double doRound(final double d, final int r) {
        String round = "#";
        for (int i = 0; i < r; ++i) {
            round = String.valueOf(round) + ".#";
        }
        final DecimalFormat twoDForm = new DecimalFormat(round);
        return Double.valueOf(twoDForm.format(d));
    }

    public static int getMiddle(int i, int i2){
        return (i + i2) / 2;
    }
    public static Random getRng() {
        return MathUtils.rng;
    }
    public static int getNumberFor(int start, int end) {
    	if(end >= start) return 0;
    	if((end - start) < 0) return 0;
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
    public static double getMiddleDouble(int i, int i2){
        return ((double)i + (double)i2) / 2.0;
    }
}
