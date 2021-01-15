/*    */ package net.minecraft.scoreboard;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Team
/*    */ {
/*    */   public boolean isSameTeam(Team other)
/*    */   {
/* 14 */     return other != null;
/*    */   }
/*    */   
/*    */ 
/*    */   public abstract String getRegisteredName();
/*    */   
/*    */ 
/*    */   public abstract String formatString(String paramString);
/*    */   
/*    */ 
/*    */   public abstract boolean getSeeFriendlyInvisiblesEnabled();
/*    */   
/*    */   public abstract boolean getAllowFriendlyFire();
/*    */   
/*    */   public abstract EnumVisible getNameTagVisibility();
/*    */   
/*    */   public abstract Collection<String> getMembershipCollection();
/*    */   
/*    */   public abstract EnumVisible getDeathMessageVisibility();
/*    */   
/*    */   public static enum EnumVisible
/*    */   {
/* 36 */     ALWAYS("always", 0), 
/* 37 */     NEVER("never", 1), 
/* 38 */     HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2), 
/* 39 */     HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3);
/*    */     
/*    */     private static Map<String, EnumVisible> field_178828_g;
/*    */     public final String field_178830_e;
/*    */     public final int field_178827_f;
/*    */     
/*    */     public static String[] func_178825_a()
/*    */     {
/* 47 */       return (String[])field_178828_g.keySet().toArray(new String[field_178828_g.size()]);
/*    */     }
/*    */     
/*    */     public static EnumVisible func_178824_a(String p_178824_0_)
/*    */     {
/* 52 */       return (EnumVisible)field_178828_g.get(p_178824_0_);
/*    */     }
/*    */     
/*    */     private EnumVisible(String p_i45550_3_, int p_i45550_4_)
/*    */     {
/* 57 */       this.field_178830_e = p_i45550_3_;
/* 58 */       this.field_178827_f = p_i45550_4_;
/*    */     }
/*    */     
/*    */     static
/*    */     {
/* 41 */       field_178828_g = Maps.newHashMap();
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
/*    */       EnumVisible[] arrayOfEnumVisible;
/*    */       
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 62 */       int j = (arrayOfEnumVisible = values()).length; for (int i = 0; i < j; i++) { EnumVisible team$enumvisible = arrayOfEnumVisible[i];
/*    */         
/* 64 */         field_178828_g.put(team$enumvisible.field_178830_e, team$enumvisible);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\scoreboard\Team.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */