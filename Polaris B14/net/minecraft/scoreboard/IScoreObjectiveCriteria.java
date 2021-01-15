/*    */ package net.minecraft.scoreboard;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ 
/*    */ public abstract interface IScoreObjectiveCriteria
/*    */ {
/* 11 */   public static final Map<String, IScoreObjectiveCriteria> INSTANCES = ;
/* 12 */   public static final IScoreObjectiveCriteria DUMMY = new ScoreDummyCriteria("dummy");
/* 13 */   public static final IScoreObjectiveCriteria TRIGGER = new ScoreDummyCriteria("trigger");
/* 14 */   public static final IScoreObjectiveCriteria deathCount = new ScoreDummyCriteria("deathCount");
/* 15 */   public static final IScoreObjectiveCriteria playerKillCount = new ScoreDummyCriteria("playerKillCount");
/* 16 */   public static final IScoreObjectiveCriteria totalKillCount = new ScoreDummyCriteria("totalKillCount");
/* 17 */   public static final IScoreObjectiveCriteria health = new ScoreHealthCriteria("health");
/* 18 */   public static final IScoreObjectiveCriteria[] field_178792_h = { new GoalColor("teamkill.", EnumChatFormatting.BLACK), new GoalColor("teamkill.", EnumChatFormatting.DARK_BLUE), new GoalColor("teamkill.", EnumChatFormatting.DARK_GREEN), new GoalColor("teamkill.", EnumChatFormatting.DARK_AQUA), new GoalColor("teamkill.", EnumChatFormatting.DARK_RED), new GoalColor("teamkill.", EnumChatFormatting.DARK_PURPLE), new GoalColor("teamkill.", EnumChatFormatting.GOLD), new GoalColor("teamkill.", EnumChatFormatting.GRAY), new GoalColor("teamkill.", EnumChatFormatting.DARK_GRAY), new GoalColor("teamkill.", EnumChatFormatting.BLUE), new GoalColor("teamkill.", EnumChatFormatting.GREEN), new GoalColor("teamkill.", EnumChatFormatting.AQUA), new GoalColor("teamkill.", EnumChatFormatting.RED), new GoalColor("teamkill.", EnumChatFormatting.LIGHT_PURPLE), new GoalColor("teamkill.", EnumChatFormatting.YELLOW), new GoalColor("teamkill.", EnumChatFormatting.WHITE) };
/* 19 */   public static final IScoreObjectiveCriteria[] field_178793_i = { new GoalColor("killedByTeam.", EnumChatFormatting.BLACK), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_BLUE), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_GREEN), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_AQUA), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_RED), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_PURPLE), new GoalColor("killedByTeam.", EnumChatFormatting.GOLD), new GoalColor("killedByTeam.", EnumChatFormatting.GRAY), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_GRAY), new GoalColor("killedByTeam.", EnumChatFormatting.BLUE), new GoalColor("killedByTeam.", EnumChatFormatting.GREEN), new GoalColor("killedByTeam.", EnumChatFormatting.AQUA), new GoalColor("killedByTeam.", EnumChatFormatting.RED), new GoalColor("killedByTeam.", EnumChatFormatting.LIGHT_PURPLE), new GoalColor("killedByTeam.", EnumChatFormatting.YELLOW), new GoalColor("killedByTeam.", EnumChatFormatting.WHITE) };
/*    */   
/*    */   public abstract String getName();
/*    */   
/*    */   public abstract int func_96635_a(List<EntityPlayer> paramList);
/*    */   
/*    */   public abstract boolean isReadOnly();
/*    */   
/*    */   public abstract EnumRenderType getRenderType();
/*    */   
/*    */   public static enum EnumRenderType
/*    */   {
/* 31 */     INTEGER("integer"), 
/* 32 */     HEARTS("hearts");
/*    */     
/*    */     private static final Map<String, EnumRenderType> field_178801_c;
/*    */     private final String field_178798_d;
/*    */     
/*    */     private EnumRenderType(String p_i45548_3_)
/*    */     {
/* 39 */       this.field_178798_d = p_i45548_3_;
/*    */     }
/*    */     
/*    */     public String func_178796_a()
/*    */     {
/* 44 */       return this.field_178798_d;
/*    */     }
/*    */     
/*    */     public static EnumRenderType func_178795_a(String p_178795_0_)
/*    */     {
/* 49 */       EnumRenderType iscoreobjectivecriteria$enumrendertype = (EnumRenderType)field_178801_c.get(p_178795_0_);
/* 50 */       return iscoreobjectivecriteria$enumrendertype == null ? INTEGER : iscoreobjectivecriteria$enumrendertype;
/*    */     }
/*    */     
/*    */     static
/*    */     {
/* 34 */       field_178801_c = Maps.newHashMap();
/*    */       
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       EnumRenderType[] arrayOfEnumRenderType;
/*    */       
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 54 */       int j = (arrayOfEnumRenderType = values()).length; for (int i = 0; i < j; i++) { EnumRenderType iscoreobjectivecriteria$enumrendertype = arrayOfEnumRenderType[i];
/*    */         
/* 56 */         field_178801_c.put(iscoreobjectivecriteria$enumrendertype.func_178796_a(), iscoreobjectivecriteria$enumrendertype);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\scoreboard\IScoreObjectiveCriteria.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */