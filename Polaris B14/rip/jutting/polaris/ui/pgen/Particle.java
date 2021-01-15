/*    */ package rip.jutting.polaris.ui.pgen;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.Random;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.ui.click.settings.Setting;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ 
/*    */ public class Particle
/*    */ {
/*    */   public int x;
/*    */   public int y;
/*    */   public int k;
/*    */   public ParticleGen pg;
/*    */   public boolean reset;
/*    */   public float size;
/* 17 */   private Random random = new Random();
/*    */   
/*    */   public Particle(int x, int y) {
/* 20 */     this.x = x;
/* 21 */     this.y = y;
/* 22 */     this.size = genRandom(0.1F, 0.1F);
/*    */   }
/*    */   
/*    */   public void draw()
/*    */   {
/* 27 */     if (this.x == -1) {
/* 28 */       this.x = ParticleGen.meow;
/* 29 */       this.reset = true;
/*    */     }
/*    */     
/* 32 */     if (this.y == -1) {
/* 33 */       this.y = ParticleGen.xd;
/* 34 */       this.reset = true;
/*    */     }
/*    */     
/* 37 */     this.x -= this.random.nextInt(2);
/* 38 */     this.y -= this.random.nextInt(2);
/*    */     
/* 40 */     int xx = (int)(net.minecraft.util.MathHelper.cos(0.1F * (this.x + this.k)) * 10.0F);
/* 41 */     RenderUtils.drawBorderedCircle(this.x + xx, this.y, this.size, 
/* 42 */       new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 43 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 44 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).brighter()
/* 45 */       .getRGB(), 
/* 46 */       new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 47 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 48 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).darker()
/* 49 */       .getRGB());
/*    */   }
/*    */   
/*    */   public float genRandom(float min, float max) {
/* 53 */     return (float)(min + Math.random() * (max - min + 1.0F));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\pgen\Particle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */