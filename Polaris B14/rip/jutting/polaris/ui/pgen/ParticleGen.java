/*    */ package rip.jutting.polaris.ui.pgen;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Random;
/*    */ import java.util.Timer;
/*    */ 
/*    */ 
/*    */ public class ParticleGen
/*    */ {
/*    */   public static int nigerian;
/*    */   public static int meow;
/*    */   public static int xd;
/* 13 */   public ArrayList<Particle> particles = new ArrayList();
/* 14 */   private Random random = new Random();
/* 15 */   private Timer timer = new Timer();
/*    */   
/*    */   public ParticleGen(int nigerian, int meow, int xd) {
/* 18 */     nigerian = nigerian;
/* 19 */     meow = meow;
/* 20 */     xd = xd;
/* 21 */     for (int i = 0; i < nigerian; i++) {
/* 22 */       this.particles.add(new Particle(this.random.nextInt(meow), this.random.nextInt(xd)));
/*    */     }
/*    */   }
/*    */   
/*    */   public void drawParticles() {
/* 27 */     for (Particle p : this.particles) {
/* 28 */       p.draw();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\pgen\ParticleGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */