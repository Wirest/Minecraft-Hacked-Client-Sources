/*    */ package net.minecraft.scoreboard;
/*    */ 
/*    */ 
/*    */ public class ScoreObjective
/*    */ {
/*    */   private final Scoreboard theScoreboard;
/*    */   
/*    */   private final String name;
/*    */   private final IScoreObjectiveCriteria objectiveCriteria;
/*    */   private IScoreObjectiveCriteria.EnumRenderType renderType;
/*    */   private String displayName;
/*    */   
/*    */   public ScoreObjective(Scoreboard theScoreboardIn, String nameIn, IScoreObjectiveCriteria objectiveCriteriaIn)
/*    */   {
/* 15 */     this.theScoreboard = theScoreboardIn;
/* 16 */     this.name = nameIn;
/* 17 */     this.objectiveCriteria = objectiveCriteriaIn;
/* 18 */     this.displayName = nameIn;
/* 19 */     this.renderType = objectiveCriteriaIn.getRenderType();
/*    */   }
/*    */   
/*    */   public Scoreboard getScoreboard()
/*    */   {
/* 24 */     return this.theScoreboard;
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 29 */     return this.name;
/*    */   }
/*    */   
/*    */   public IScoreObjectiveCriteria getCriteria()
/*    */   {
/* 34 */     return this.objectiveCriteria;
/*    */   }
/*    */   
/*    */   public String getDisplayName()
/*    */   {
/* 39 */     return this.displayName;
/*    */   }
/*    */   
/*    */   public void setDisplayName(String nameIn)
/*    */   {
/* 44 */     this.displayName = nameIn;
/* 45 */     this.theScoreboard.func_96532_b(this);
/*    */   }
/*    */   
/*    */   public IScoreObjectiveCriteria.EnumRenderType getRenderType()
/*    */   {
/* 50 */     return this.renderType;
/*    */   }
/*    */   
/*    */   public void setRenderType(IScoreObjectiveCriteria.EnumRenderType type)
/*    */   {
/* 55 */     this.renderType = type;
/* 56 */     this.theScoreboard.func_96532_b(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\scoreboard\ScoreObjective.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */