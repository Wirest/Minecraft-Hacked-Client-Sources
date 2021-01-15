/*    */ package rip.jutting.polaris.ui.particles.Particle;
/*    */ 
/*    */ 
/*    */ public abstract class Particle
/*    */   implements IParticle
/*    */ {
/*    */   private int x;
/*    */   
/*    */   private int y;
/*    */   private final float radius;
/*    */   private int xIncrease;
/*    */   private int yIncrease;
/*    */   
/*    */   public Particle(float radius)
/*    */   {
/* 16 */     this.radius = radius;
/*    */     
/* 18 */     this.x = -1;
/* 19 */     this.x = -1;
/*    */   }
/*    */   
/*    */   public abstract void deploy(BorderingSide paramBorderingSide);
/*    */   
/*    */   public int getX() {
/* 25 */     return this.x;
/*    */   }
/*    */   
/*    */   public void setX(int x) {
/* 29 */     this.x = x;
/*    */   }
/*    */   
/*    */   public int getY() {
/* 33 */     return this.y;
/*    */   }
/*    */   
/*    */   public void setY(int y) {
/* 37 */     this.y = y;
/*    */   }
/*    */   
/*    */   public float getRadius() {
/* 41 */     return this.radius;
/*    */   }
/*    */   
/*    */   int getXIncrease() {
/* 45 */     return this.xIncrease;
/*    */   }
/*    */   
/*    */   protected void setXIncrease(int xIncrease) {
/* 49 */     this.xIncrease = xIncrease;
/*    */   }
/*    */   
/*    */   int getYIncrease() {
/* 53 */     return this.yIncrease;
/*    */   }
/*    */   
/*    */   protected void setYIncrease(int yIncrease) {
/* 57 */     this.yIncrease = yIncrease;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\particles\Particle\Particle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */