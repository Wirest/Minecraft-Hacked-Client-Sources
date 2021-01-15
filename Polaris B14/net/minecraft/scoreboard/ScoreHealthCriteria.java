/*    */ package net.minecraft.scoreboard;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class ScoreHealthCriteria extends ScoreDummyCriteria
/*    */ {
/*    */   public ScoreHealthCriteria(String name)
/*    */   {
/* 11 */     super(name);
/*    */   }
/*    */   
/*    */   public int func_96635_a(List<EntityPlayer> p_96635_1_)
/*    */   {
/* 16 */     float f = 0.0F;
/*    */     
/* 18 */     for (EntityPlayer entityplayer : p_96635_1_)
/*    */     {
/* 20 */       f += entityplayer.getHealth() + entityplayer.getAbsorptionAmount();
/*    */     }
/*    */     
/* 23 */     if (p_96635_1_.size() > 0)
/*    */     {
/* 25 */       f /= p_96635_1_.size();
/*    */     }
/*    */     
/* 28 */     return MathHelper.ceiling_float_int(f);
/*    */   }
/*    */   
/*    */   public boolean isReadOnly()
/*    */   {
/* 33 */     return true;
/*    */   }
/*    */   
/*    */   public IScoreObjectiveCriteria.EnumRenderType getRenderType()
/*    */   {
/* 38 */     return IScoreObjectiveCriteria.EnumRenderType.HEARTS;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\scoreboard\ScoreHealthCriteria.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */