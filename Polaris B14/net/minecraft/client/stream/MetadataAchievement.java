/*    */ package net.minecraft.client.stream;
/*    */ 
/*    */ import net.minecraft.stats.Achievement;
/*    */ 
/*    */ public class MetadataAchievement extends Metadata
/*    */ {
/*    */   public MetadataAchievement(Achievement p_i1032_1_)
/*    */   {
/*  9 */     super("achievement");
/* 10 */     func_152808_a("achievement_id", p_i1032_1_.statId);
/* 11 */     func_152808_a("achievement_name", p_i1032_1_.getStatName().getUnformattedText());
/* 12 */     func_152808_a("achievement_description", p_i1032_1_.getDescription());
/* 13 */     func_152807_a("Achievement '" + p_i1032_1_.getStatName().getUnformattedText() + "' obtained!");
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\stream\MetadataAchievement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */