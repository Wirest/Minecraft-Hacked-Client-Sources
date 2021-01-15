/*    */ package rip.jutting.polaris.ui.particles.Particle.Impl;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import rip.jutting.polaris.ui.particles.Particle.BorderingSide;
/*    */ import rip.jutting.polaris.ui.particles.Particle.Particle;
/*    */ import rip.jutting.polaris.ui.particles.Utilities.RenderUtil;
/*    */ 
/*    */ public class AutismParticle
/*    */   extends Particle
/*    */ {
/* 13 */   private final Random random = new Random();
/*    */   
/*    */   private static final int MOVE_SPEED = 2;
/*    */   
/*    */   private static final int RAINBOW_SPEED = 6000;
/*    */   
/*    */   private static final int RAINBOW_MULTIPLIER = -15;
/*    */   
/*    */   public AutismParticle(float radius)
/*    */   {
/* 23 */     super(radius);
/*    */     
/* 25 */     setX(-1);
/* 26 */     setY(-1);
/*    */   }
/*    */   
/*    */   public void deploy(BorderingSide borderingSide) {
/* 30 */     ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
/*    */     
/*    */ 
/* 33 */     switch (borderingSide) {
/*    */     case BOTTOM: 
/* 35 */       setY(0);
/* 36 */       setXIncrease(this.random.nextBoolean() ? this.random.nextInt(2) + 1 : -(this.random.nextInt(2) + 1));
/* 37 */       setYIncrease(this.random.nextInt(2) + 1);
/* 38 */       break;
/*    */     case RIGHT: 
/* 40 */       setX(0);
/* 41 */       setXIncrease(this.random.nextInt(2) + 1);
/* 42 */       setYIncrease(this.random.nextBoolean() ? this.random.nextInt(2) + 1 : -(this.random.nextInt(2) + 1));
/* 43 */       break;
/*    */     case LEFT: 
/* 45 */       setY(ScaledResolution.getScaledHeight());
/* 46 */       setXIncrease(this.random.nextBoolean() ? this.random.nextInt(2) + 1 : -(this.random.nextInt(2) + 1));
/* 47 */       setYIncrease(-(this.random.nextInt(2) + 1));
/* 48 */       break;
/*    */     case TOP: 
/* 50 */       setX(ScaledResolution.getScaledWidth());
/* 51 */       setXIncrease(-(this.random.nextInt(2) + 1));
/* 52 */       setYIncrease(this.random.nextBoolean() ? this.random.nextInt(2) + 1 : -(this.random.nextInt(2) + 1));
/*    */     }
/*    */     
/*    */   }
/*    */   
/*    */   public void connect(int x, int y)
/*    */   {
/* 59 */     RenderUtil.connectPoints(getX(), getY(), x, y);
/*    */   }
/*    */   
/*    */   public void draw()
/*    */   {
/* 64 */     RenderUtil.drawCircle(getX(), getY(), getRadius(), RenderUtil.getRainbow(6000, -15 * getY()));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\particles\Particle\Impl\AutismParticle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */