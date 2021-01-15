/*    */ package net.minecraft.world;
/*    */ 
/*    */ public enum EnumDifficulty
/*    */ {
/*  5 */   PEACEFUL(0, "options.difficulty.peaceful"), 
/*  6 */   EASY(1, "options.difficulty.easy"), 
/*  7 */   NORMAL(2, "options.difficulty.normal"), 
/*  8 */   HARD(3, "options.difficulty.hard");
/*    */   
/*    */   private static final EnumDifficulty[] difficultyEnums;
/*    */   private final int difficultyId;
/*    */   private final String difficultyResourceKey;
/*    */   
/*    */   private EnumDifficulty(int difficultyIdIn, String difficultyResourceKeyIn)
/*    */   {
/* 16 */     this.difficultyId = difficultyIdIn;
/* 17 */     this.difficultyResourceKey = difficultyResourceKeyIn;
/*    */   }
/*    */   
/*    */   public int getDifficultyId()
/*    */   {
/* 22 */     return this.difficultyId;
/*    */   }
/*    */   
/*    */   public static EnumDifficulty getDifficultyEnum(int p_151523_0_)
/*    */   {
/* 27 */     return difficultyEnums[(p_151523_0_ % difficultyEnums.length)];
/*    */   }
/*    */   
/*    */   public String getDifficultyResourceKey()
/*    */   {
/* 32 */     return this.difficultyResourceKey;
/*    */   }
/*    */   
/*    */   static
/*    */   {
/* 10 */     difficultyEnums = new EnumDifficulty[values().length];
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     EnumDifficulty[] arrayOfEnumDifficulty;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 36 */     int j = (arrayOfEnumDifficulty = values()).length; for (int i = 0; i < j; i++) { EnumDifficulty enumdifficulty = arrayOfEnumDifficulty[i];
/*    */       
/* 38 */       difficultyEnums[enumdifficulty.difficultyId] = enumdifficulty;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\EnumDifficulty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */