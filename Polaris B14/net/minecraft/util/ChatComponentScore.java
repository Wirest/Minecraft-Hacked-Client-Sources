/*     */ package net.minecraft.util;
/*     */ 
/*     */ import net.minecraft.scoreboard.Score;
/*     */ import net.minecraft.scoreboard.ScoreObjective;
/*     */ import net.minecraft.scoreboard.Scoreboard;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.world.WorldServer;
/*     */ 
/*     */ public class ChatComponentScore
/*     */   extends ChatComponentStyle
/*     */ {
/*     */   private final String name;
/*     */   private final String objective;
/*  14 */   private String value = "";
/*     */   
/*     */   public ChatComponentScore(String nameIn, String objectiveIn)
/*     */   {
/*  18 */     this.name = nameIn;
/*  19 */     this.objective = objectiveIn;
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/*  24 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getObjective()
/*     */   {
/*  29 */     return this.objective;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValue(String valueIn)
/*     */   {
/*  37 */     this.value = valueIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUnformattedTextForChat()
/*     */   {
/*  46 */     MinecraftServer minecraftserver = MinecraftServer.getServer();
/*     */     
/*  48 */     if ((minecraftserver != null) && (minecraftserver.isAnvilFileSet()) && (StringUtils.isNullOrEmpty(this.value)))
/*     */     {
/*  50 */       Scoreboard scoreboard = minecraftserver.worldServerForDimension(0).getScoreboard();
/*  51 */       ScoreObjective scoreobjective = scoreboard.getObjective(this.objective);
/*     */       
/*  53 */       if (scoreboard.entityHasObjective(this.name, scoreobjective))
/*     */       {
/*  55 */         Score score = scoreboard.getValueFromObjective(this.name, scoreobjective);
/*  56 */         setValue(String.format("%d", new Object[] { Integer.valueOf(score.getScorePoints()) }));
/*     */       }
/*     */       else
/*     */       {
/*  60 */         this.value = "";
/*     */       }
/*     */     }
/*     */     
/*  64 */     return this.value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChatComponentScore createCopy()
/*     */   {
/*  72 */     ChatComponentScore chatcomponentscore = new ChatComponentScore(this.name, this.objective);
/*  73 */     chatcomponentscore.setValue(this.value);
/*  74 */     chatcomponentscore.setChatStyle(getChatStyle().createShallowCopy());
/*     */     
/*  76 */     for (IChatComponent ichatcomponent : getSiblings())
/*     */     {
/*  78 */       chatcomponentscore.appendSibling(ichatcomponent.createCopy());
/*     */     }
/*     */     
/*  81 */     return chatcomponentscore;
/*     */   }
/*     */   
/*     */   public boolean equals(Object p_equals_1_)
/*     */   {
/*  86 */     if (this == p_equals_1_)
/*     */     {
/*  88 */       return true;
/*     */     }
/*  90 */     if (!(p_equals_1_ instanceof ChatComponentScore))
/*     */     {
/*  92 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  96 */     ChatComponentScore chatcomponentscore = (ChatComponentScore)p_equals_1_;
/*  97 */     return (this.name.equals(chatcomponentscore.name)) && (this.objective.equals(chatcomponentscore.objective)) && (super.equals(p_equals_1_));
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 103 */     return "ScoreComponent{name='" + this.name + '\'' + "objective='" + this.objective + '\'' + ", siblings=" + this.siblings + ", style=" + getChatStyle() + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\ChatComponentScore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */