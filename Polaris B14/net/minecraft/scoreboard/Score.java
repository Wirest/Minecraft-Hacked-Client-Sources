/*     */ package net.minecraft.scoreboard;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ 
/*     */ public class Score
/*     */ {
/*   9 */   public static final Comparator<Score> scoreComparator = new Comparator()
/*     */   {
/*     */     public int compare(Score p_compare_1_, Score p_compare_2_)
/*     */     {
/*  13 */       return p_compare_1_.getScorePoints() < p_compare_2_.getScorePoints() ? -1 : p_compare_1_.getScorePoints() > p_compare_2_.getScorePoints() ? 1 : p_compare_2_.getPlayerName().compareToIgnoreCase(p_compare_1_.getPlayerName());
/*     */     }
/*     */   };
/*     */   private final Scoreboard theScoreboard;
/*     */   private final ScoreObjective theScoreObjective;
/*     */   private final String scorePlayerName;
/*     */   private int scorePoints;
/*     */   private boolean locked;
/*     */   private boolean field_178818_g;
/*     */   
/*     */   public Score(Scoreboard theScoreboardIn, ScoreObjective theScoreObjectiveIn, String scorePlayerNameIn)
/*     */   {
/*  25 */     this.theScoreboard = theScoreboardIn;
/*  26 */     this.theScoreObjective = theScoreObjectiveIn;
/*  27 */     this.scorePlayerName = scorePlayerNameIn;
/*  28 */     this.field_178818_g = true;
/*     */   }
/*     */   
/*     */   public void increseScore(int amount)
/*     */   {
/*  33 */     if (this.theScoreObjective.getCriteria().isReadOnly())
/*     */     {
/*  35 */       throw new IllegalStateException("Cannot modify read-only score");
/*     */     }
/*     */     
/*     */ 
/*  39 */     setScorePoints(getScorePoints() + amount);
/*     */   }
/*     */   
/*     */ 
/*     */   public void decreaseScore(int amount)
/*     */   {
/*  45 */     if (this.theScoreObjective.getCriteria().isReadOnly())
/*     */     {
/*  47 */       throw new IllegalStateException("Cannot modify read-only score");
/*     */     }
/*     */     
/*     */ 
/*  51 */     setScorePoints(getScorePoints() - amount);
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_96648_a()
/*     */   {
/*  57 */     if (this.theScoreObjective.getCriteria().isReadOnly())
/*     */     {
/*  59 */       throw new IllegalStateException("Cannot modify read-only score");
/*     */     }
/*     */     
/*     */ 
/*  63 */     increseScore(1);
/*     */   }
/*     */   
/*     */ 
/*     */   public int getScorePoints()
/*     */   {
/*  69 */     return this.scorePoints;
/*     */   }
/*     */   
/*     */   public void setScorePoints(int points)
/*     */   {
/*  74 */     int i = this.scorePoints;
/*  75 */     this.scorePoints = points;
/*     */     
/*  77 */     if ((i != points) || (this.field_178818_g))
/*     */     {
/*  79 */       this.field_178818_g = false;
/*  80 */       getScoreScoreboard().func_96536_a(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public ScoreObjective getObjective()
/*     */   {
/*  86 */     return this.theScoreObjective;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPlayerName()
/*     */   {
/*  94 */     return this.scorePlayerName;
/*     */   }
/*     */   
/*     */   public Scoreboard getScoreScoreboard()
/*     */   {
/*  99 */     return this.theScoreboard;
/*     */   }
/*     */   
/*     */   public boolean isLocked()
/*     */   {
/* 104 */     return this.locked;
/*     */   }
/*     */   
/*     */   public void setLocked(boolean locked)
/*     */   {
/* 109 */     this.locked = locked;
/*     */   }
/*     */   
/*     */   public void func_96651_a(List<EntityPlayer> p_96651_1_)
/*     */   {
/* 114 */     setScorePoints(this.theScoreObjective.getCriteria().func_96635_a(p_96651_1_));
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\scoreboard\Score.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */