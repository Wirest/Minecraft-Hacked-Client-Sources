/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class DifficultyInstance
/*    */ {
/*    */   private final EnumDifficulty worldDifficulty;
/*    */   private final float additionalDifficulty;
/*    */   
/*    */   public DifficultyInstance(EnumDifficulty worldDifficulty, long worldTime, long chunkInhabitedTime, float moonPhaseFactor)
/*    */   {
/* 12 */     this.worldDifficulty = worldDifficulty;
/* 13 */     this.additionalDifficulty = calculateAdditionalDifficulty(worldDifficulty, worldTime, chunkInhabitedTime, moonPhaseFactor);
/*    */   }
/*    */   
/*    */   public float getAdditionalDifficulty()
/*    */   {
/* 18 */     return this.additionalDifficulty;
/*    */   }
/*    */   
/*    */   public float getClampedAdditionalDifficulty()
/*    */   {
/* 23 */     return this.additionalDifficulty > 4.0F ? 1.0F : this.additionalDifficulty < 2.0F ? 0.0F : (this.additionalDifficulty - 2.0F) / 2.0F;
/*    */   }
/*    */   
/*    */   private float calculateAdditionalDifficulty(EnumDifficulty difficulty, long worldTime, long chunkInhabitedTime, float moonPhaseFactor)
/*    */   {
/* 28 */     if (difficulty == EnumDifficulty.PEACEFUL)
/*    */     {
/* 30 */       return 0.0F;
/*    */     }
/*    */     
/*    */ 
/* 34 */     boolean flag = difficulty == EnumDifficulty.HARD;
/* 35 */     float f = 0.75F;
/* 36 */     float f1 = MathHelper.clamp_float(((float)worldTime + -72000.0F) / 1440000.0F, 0.0F, 1.0F) * 0.25F;
/* 37 */     f += f1;
/* 38 */     float f2 = 0.0F;
/* 39 */     f2 += MathHelper.clamp_float((float)chunkInhabitedTime / 3600000.0F, 0.0F, 1.0F) * (flag ? 1.0F : 0.75F);
/* 40 */     f2 += MathHelper.clamp_float(moonPhaseFactor * 0.25F, 0.0F, f1);
/*    */     
/* 42 */     if (difficulty == EnumDifficulty.EASY)
/*    */     {
/* 44 */       f2 *= 0.5F;
/*    */     }
/*    */     
/* 47 */     f += f2;
/* 48 */     return difficulty.getDifficultyId() * f;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\DifficultyInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */