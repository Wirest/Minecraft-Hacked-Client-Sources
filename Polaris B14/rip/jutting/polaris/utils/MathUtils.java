/*    */ package rip.jutting.polaris.utils;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ import java.text.DecimalFormat;
/*    */ import java.util.Random;
/*    */ 
/*    */ public class MathUtils
/*    */ {
/*  9 */   private static Random rng = new Random();
/*    */   
/*    */   public static double round(double value, int places)
/*    */   {
/* 13 */     if (places < 0) {
/* 14 */       throw new IllegalArgumentException();
/*    */     }
/* 16 */     BigDecimal bd = new BigDecimal(value);
/* 17 */     bd = bd.setScale(places, java.math.RoundingMode.HALF_UP);
/* 18 */     return bd.doubleValue();
/*    */   }
/*    */   
/* 21 */   public double doRound(double d, int r) { String round = "#";
/* 22 */     for (int i = 0; i < r; i++) {
/* 23 */       round = String.valueOf(round) + ".#";
/*    */     }
/* 25 */     DecimalFormat twoDForm = new DecimalFormat(round);
/* 26 */     return Double.valueOf(twoDForm.format(d)).doubleValue();
/*    */   }
/*    */   
/*    */   public static int getMiddle(int i, int i2) {
/* 30 */     return (i + i2) / 2;
/*    */   }
/*    */   
/* 33 */   public static Random getRng() { return rng; }
/*    */   
/*    */   public static int getNumberFor(int start, int end) {
/* 36 */     if (end >= start) return 0;
/* 37 */     if (end - start < 0) return 0;
/* 38 */     return end - start;
/*    */   }
/*    */   
/* 41 */   public static double roundToPlace(double value, int places) { if (places < 0) {
/* 42 */       throw new IllegalArgumentException();
/*    */     }
/* 44 */     BigDecimal bd = new BigDecimal(value);
/* 45 */     bd = bd.setScale(places, java.math.RoundingMode.HALF_UP);
/* 46 */     return bd.doubleValue();
/*    */   }
/*    */   
/* 49 */   public static float getRandom() { return rng.nextFloat(); }
/*    */   
/*    */   public static int getRandom(int cap)
/*    */   {
/* 53 */     return rng.nextInt(cap);
/*    */   }
/*    */   
/*    */   public static int getRandom(int floor, int cap) {
/* 57 */     return floor + rng.nextInt(cap - floor + 1);
/*    */   }
/*    */   
/* 60 */   public static double getMiddleDouble(int i, int i2) { return (i + i2) / 2.0D; }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\MathUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */