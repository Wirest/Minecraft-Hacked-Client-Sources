/*     */ package net.minecraft.scoreboard;
/*     */ 
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.nbt.NBTTagString;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.world.WorldSavedData;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ScoreboardSaveData extends WorldSavedData
/*     */ {
/*  13 */   private static final Logger logger = ;
/*     */   private Scoreboard theScoreboard;
/*     */   private NBTTagCompound delayedInitNbt;
/*     */   
/*     */   public ScoreboardSaveData()
/*     */   {
/*  19 */     this("scoreboard");
/*     */   }
/*     */   
/*     */   public ScoreboardSaveData(String name)
/*     */   {
/*  24 */     super(name);
/*     */   }
/*     */   
/*     */   public void setScoreboard(Scoreboard scoreboardIn)
/*     */   {
/*  29 */     this.theScoreboard = scoreboardIn;
/*     */     
/*  31 */     if (this.delayedInitNbt != null)
/*     */     {
/*  33 */       readFromNBT(this.delayedInitNbt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readFromNBT(NBTTagCompound nbt)
/*     */   {
/*  42 */     if (this.theScoreboard == null)
/*     */     {
/*  44 */       this.delayedInitNbt = nbt;
/*     */     }
/*     */     else
/*     */     {
/*  48 */       readObjectives(nbt.getTagList("Objectives", 10));
/*  49 */       readScores(nbt.getTagList("PlayerScores", 10));
/*     */       
/*  51 */       if (nbt.hasKey("DisplaySlots", 10))
/*     */       {
/*  53 */         readDisplayConfig(nbt.getCompoundTag("DisplaySlots"));
/*     */       }
/*     */       
/*  56 */       if (nbt.hasKey("Teams", 9))
/*     */       {
/*  58 */         readTeams(nbt.getTagList("Teams", 10));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void readTeams(NBTTagList p_96498_1_)
/*     */   {
/*  65 */     for (int i = 0; i < p_96498_1_.tagCount(); i++)
/*     */     {
/*  67 */       NBTTagCompound nbttagcompound = p_96498_1_.getCompoundTagAt(i);
/*  68 */       String s = nbttagcompound.getString("Name");
/*     */       
/*  70 */       if (s.length() > 16)
/*     */       {
/*  72 */         s = s.substring(0, 16);
/*     */       }
/*     */       
/*  75 */       ScorePlayerTeam scoreplayerteam = this.theScoreboard.createTeam(s);
/*  76 */       String s1 = nbttagcompound.getString("DisplayName");
/*     */       
/*  78 */       if (s1.length() > 32)
/*     */       {
/*  80 */         s1 = s1.substring(0, 32);
/*     */       }
/*     */       
/*  83 */       scoreplayerteam.setTeamName(s1);
/*     */       
/*  85 */       if (nbttagcompound.hasKey("TeamColor", 8))
/*     */       {
/*  87 */         scoreplayerteam.setChatFormat(EnumChatFormatting.getValueByName(nbttagcompound.getString("TeamColor")));
/*     */       }
/*     */       
/*  90 */       scoreplayerteam.setNamePrefix(nbttagcompound.getString("Prefix"));
/*  91 */       scoreplayerteam.setNameSuffix(nbttagcompound.getString("Suffix"));
/*     */       
/*  93 */       if (nbttagcompound.hasKey("AllowFriendlyFire", 99))
/*     */       {
/*  95 */         scoreplayerteam.setAllowFriendlyFire(nbttagcompound.getBoolean("AllowFriendlyFire"));
/*     */       }
/*     */       
/*  98 */       if (nbttagcompound.hasKey("SeeFriendlyInvisibles", 99))
/*     */       {
/* 100 */         scoreplayerteam.setSeeFriendlyInvisiblesEnabled(nbttagcompound.getBoolean("SeeFriendlyInvisibles"));
/*     */       }
/*     */       
/* 103 */       if (nbttagcompound.hasKey("NameTagVisibility", 8))
/*     */       {
/* 105 */         Team.EnumVisible team$enumvisible = Team.EnumVisible.func_178824_a(nbttagcompound.getString("NameTagVisibility"));
/*     */         
/* 107 */         if (team$enumvisible != null)
/*     */         {
/* 109 */           scoreplayerteam.setNameTagVisibility(team$enumvisible);
/*     */         }
/*     */       }
/*     */       
/* 113 */       if (nbttagcompound.hasKey("DeathMessageVisibility", 8))
/*     */       {
/* 115 */         Team.EnumVisible team$enumvisible1 = Team.EnumVisible.func_178824_a(nbttagcompound.getString("DeathMessageVisibility"));
/*     */         
/* 117 */         if (team$enumvisible1 != null)
/*     */         {
/* 119 */           scoreplayerteam.setDeathMessageVisibility(team$enumvisible1);
/*     */         }
/*     */       }
/*     */       
/* 123 */       func_96502_a(scoreplayerteam, nbttagcompound.getTagList("Players", 8));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_96502_a(ScorePlayerTeam p_96502_1_, NBTTagList p_96502_2_)
/*     */   {
/* 129 */     for (int i = 0; i < p_96502_2_.tagCount(); i++)
/*     */     {
/* 131 */       this.theScoreboard.addPlayerToTeam(p_96502_2_.getStringTagAt(i), p_96502_1_.getRegisteredName());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void readDisplayConfig(NBTTagCompound p_96504_1_)
/*     */   {
/* 137 */     for (int i = 0; i < 19; i++)
/*     */     {
/* 139 */       if (p_96504_1_.hasKey("slot_" + i, 8))
/*     */       {
/* 141 */         String s = p_96504_1_.getString("slot_" + i);
/* 142 */         ScoreObjective scoreobjective = this.theScoreboard.getObjective(s);
/* 143 */         this.theScoreboard.setObjectiveInDisplaySlot(i, scoreobjective);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void readObjectives(NBTTagList nbt)
/*     */   {
/* 150 */     for (int i = 0; i < nbt.tagCount(); i++)
/*     */     {
/* 152 */       NBTTagCompound nbttagcompound = nbt.getCompoundTagAt(i);
/* 153 */       IScoreObjectiveCriteria iscoreobjectivecriteria = (IScoreObjectiveCriteria)IScoreObjectiveCriteria.INSTANCES.get(nbttagcompound.getString("CriteriaName"));
/*     */       
/* 155 */       if (iscoreobjectivecriteria != null)
/*     */       {
/* 157 */         String s = nbttagcompound.getString("Name");
/*     */         
/* 159 */         if (s.length() > 16)
/*     */         {
/* 161 */           s = s.substring(0, 16);
/*     */         }
/*     */         
/* 164 */         ScoreObjective scoreobjective = this.theScoreboard.addScoreObjective(s, iscoreobjectivecriteria);
/* 165 */         scoreobjective.setDisplayName(nbttagcompound.getString("DisplayName"));
/* 166 */         scoreobjective.setRenderType(IScoreObjectiveCriteria.EnumRenderType.func_178795_a(nbttagcompound.getString("RenderType")));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void readScores(NBTTagList nbt)
/*     */   {
/* 173 */     for (int i = 0; i < nbt.tagCount(); i++)
/*     */     {
/* 175 */       NBTTagCompound nbttagcompound = nbt.getCompoundTagAt(i);
/* 176 */       ScoreObjective scoreobjective = this.theScoreboard.getObjective(nbttagcompound.getString("Objective"));
/* 177 */       String s = nbttagcompound.getString("Name");
/*     */       
/* 179 */       if (s.length() > 40)
/*     */       {
/* 181 */         s = s.substring(0, 40);
/*     */       }
/*     */       
/* 184 */       Score score = this.theScoreboard.getValueFromObjective(s, scoreobjective);
/* 185 */       score.setScorePoints(nbttagcompound.getInteger("Score"));
/*     */       
/* 187 */       if (nbttagcompound.hasKey("Locked"))
/*     */       {
/* 189 */         score.setLocked(nbttagcompound.getBoolean("Locked"));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeToNBT(NBTTagCompound nbt)
/*     */   {
/* 199 */     if (this.theScoreboard == null)
/*     */     {
/* 201 */       logger.warn("Tried to save scoreboard without having a scoreboard...");
/*     */     }
/*     */     else
/*     */     {
/* 205 */       nbt.setTag("Objectives", objectivesToNbt());
/* 206 */       nbt.setTag("PlayerScores", scoresToNbt());
/* 207 */       nbt.setTag("Teams", func_96496_a());
/* 208 */       func_96497_d(nbt);
/*     */     }
/*     */   }
/*     */   
/*     */   protected NBTTagList func_96496_a()
/*     */   {
/* 214 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 216 */     for (ScorePlayerTeam scoreplayerteam : this.theScoreboard.getTeams())
/*     */     {
/* 218 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 219 */       nbttagcompound.setString("Name", scoreplayerteam.getRegisteredName());
/* 220 */       nbttagcompound.setString("DisplayName", scoreplayerteam.getTeamName());
/*     */       
/* 222 */       if (scoreplayerteam.getChatFormat().getColorIndex() >= 0)
/*     */       {
/* 224 */         nbttagcompound.setString("TeamColor", scoreplayerteam.getChatFormat().getFriendlyName());
/*     */       }
/*     */       
/* 227 */       nbttagcompound.setString("Prefix", scoreplayerteam.getColorPrefix());
/* 228 */       nbttagcompound.setString("Suffix", scoreplayerteam.getColorSuffix());
/* 229 */       nbttagcompound.setBoolean("AllowFriendlyFire", scoreplayerteam.getAllowFriendlyFire());
/* 230 */       nbttagcompound.setBoolean("SeeFriendlyInvisibles", scoreplayerteam.getSeeFriendlyInvisiblesEnabled());
/* 231 */       nbttagcompound.setString("NameTagVisibility", scoreplayerteam.getNameTagVisibility().field_178830_e);
/* 232 */       nbttagcompound.setString("DeathMessageVisibility", scoreplayerteam.getDeathMessageVisibility().field_178830_e);
/* 233 */       NBTTagList nbttaglist1 = new NBTTagList();
/*     */       
/* 235 */       for (String s : scoreplayerteam.getMembershipCollection())
/*     */       {
/* 237 */         nbttaglist1.appendTag(new NBTTagString(s));
/*     */       }
/*     */       
/* 240 */       nbttagcompound.setTag("Players", nbttaglist1);
/* 241 */       nbttaglist.appendTag(nbttagcompound);
/*     */     }
/*     */     
/* 244 */     return nbttaglist;
/*     */   }
/*     */   
/*     */   protected void func_96497_d(NBTTagCompound p_96497_1_)
/*     */   {
/* 249 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 250 */     boolean flag = false;
/*     */     
/* 252 */     for (int i = 0; i < 19; i++)
/*     */     {
/* 254 */       ScoreObjective scoreobjective = this.theScoreboard.getObjectiveInDisplaySlot(i);
/*     */       
/* 256 */       if (scoreobjective != null)
/*     */       {
/* 258 */         nbttagcompound.setString("slot_" + i, scoreobjective.getName());
/* 259 */         flag = true;
/*     */       }
/*     */     }
/*     */     
/* 263 */     if (flag)
/*     */     {
/* 265 */       p_96497_1_.setTag("DisplaySlots", nbttagcompound);
/*     */     }
/*     */   }
/*     */   
/*     */   protected NBTTagList objectivesToNbt()
/*     */   {
/* 271 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 273 */     for (ScoreObjective scoreobjective : this.theScoreboard.getScoreObjectives())
/*     */     {
/* 275 */       if (scoreobjective.getCriteria() != null)
/*     */       {
/* 277 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 278 */         nbttagcompound.setString("Name", scoreobjective.getName());
/* 279 */         nbttagcompound.setString("CriteriaName", scoreobjective.getCriteria().getName());
/* 280 */         nbttagcompound.setString("DisplayName", scoreobjective.getDisplayName());
/* 281 */         nbttagcompound.setString("RenderType", scoreobjective.getRenderType().func_178796_a());
/* 282 */         nbttaglist.appendTag(nbttagcompound);
/*     */       }
/*     */     }
/*     */     
/* 286 */     return nbttaglist;
/*     */   }
/*     */   
/*     */   protected NBTTagList scoresToNbt()
/*     */   {
/* 291 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 293 */     for (Score score : this.theScoreboard.getScores())
/*     */     {
/* 295 */       if (score.getObjective() != null)
/*     */       {
/* 297 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 298 */         nbttagcompound.setString("Name", score.getPlayerName());
/* 299 */         nbttagcompound.setString("Objective", score.getObjective().getName());
/* 300 */         nbttagcompound.setInteger("Score", score.getScorePoints());
/* 301 */         nbttagcompound.setBoolean("Locked", score.isLocked());
/* 302 */         nbttaglist.appendTag(nbttagcompound);
/*     */       }
/*     */     }
/*     */     
/* 306 */     return nbttaglist;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\scoreboard\ScoreboardSaveData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */