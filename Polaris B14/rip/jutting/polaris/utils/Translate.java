/*    */ package rip.jutting.polaris.utils;
/*    */ 
/*    */ 
/*    */ public class Translate
/*    */ {
/*    */   private float x;
/*    */   private float y;
/*    */   private long lastMS;
/*    */   
/*    */   public Translate(float x, float y)
/*    */   {
/* 12 */     this.x = x;
/* 13 */     this.y = y;
/* 14 */     this.lastMS = System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   public void interpolate(float targetX, float targetY, int xSpeed, int ySpeed) {
/* 18 */     long currentMS = System.currentTimeMillis();
/* 19 */     long delta = currentMS - this.lastMS;
/* 20 */     this.lastMS = currentMS;
/* 21 */     this.x = AnimationUtil.calculateCompensation(targetX, this.x, delta, xSpeed);
/* 22 */     this.y = AnimationUtil.calculateCompensation(targetY, this.y, delta, ySpeed);
/*    */   }
/*    */   
/*    */   public void interpolate(float targetX, float targetY, int speed) {
/* 26 */     long currentMS = System.currentTimeMillis();
/* 27 */     long delta = currentMS - this.lastMS;
/* 28 */     this.lastMS = currentMS;
/* 29 */     this.x = AnimationUtil.calculateCompensation(targetX, this.x, delta, speed);
/* 30 */     this.y = AnimationUtil.calculateCompensation(targetY, this.y, delta, speed);
/*    */   }
/*    */   
/*    */   public float getX() {
/* 34 */     return this.x;
/*    */   }
/*    */   
/*    */   public void setX(float x) {
/* 38 */     this.x = x;
/*    */   }
/*    */   
/*    */   public float getY() {
/* 42 */     return this.y;
/*    */   }
/*    */   
/*    */   public void setY(float y) {
/* 46 */     this.y = y;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\Translate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */