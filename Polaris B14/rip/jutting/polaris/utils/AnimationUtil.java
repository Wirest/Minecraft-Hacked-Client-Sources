/*    */ package rip.jutting.polaris.utils;
/*    */ 
/*    */ public class AnimationUtil {
/*    */   public static float calculateCompensation(float target, float current, long delta, int speed) {
/*  5 */     float diff = current - target;
/*  6 */     if (delta < 9L) {
/*  7 */       delta = 9L;
/*    */     }
/*  9 */     if (diff > speed) {
/* 10 */       double delta2 = speed * delta / 16L < 0.05D ? 0.05D : 
/* 11 */         speed * delta / 16L;
/* 12 */       if ((current = (float)(current - delta2)) < target) {
/* 13 */         current = target;
/*    */       }
/* 15 */     } else if (diff < -speed) {
/* 16 */       double delta2 = speed * delta / 16L < 0.05D ? 0.05D : 
/* 17 */         speed * delta / 16L;
/* 18 */       if ((current = (float)(current + delta2)) > target) {
/* 19 */         current = target;
/*    */       }
/*    */     } else {
/* 22 */       current = target;
/*    */     }
/* 24 */     return current;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\AnimationUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */