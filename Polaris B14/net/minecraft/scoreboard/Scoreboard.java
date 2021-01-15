/*     */ package net.minecraft.scoreboard;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ 
/*     */ public class Scoreboard
/*     */ {
/*  15 */   private final Map<String, ScoreObjective> scoreObjectives = Maps.newHashMap();
/*  16 */   private final Map<IScoreObjectiveCriteria, List<ScoreObjective>> scoreObjectiveCriterias = Maps.newHashMap();
/*  17 */   private final Map<String, Map<ScoreObjective, Score>> entitiesScoreObjectives = Maps.newHashMap();
/*     */   
/*     */ 
/*  20 */   private final ScoreObjective[] objectiveDisplaySlots = new ScoreObjective[19];
/*  21 */   private final Map<String, ScorePlayerTeam> teams = Maps.newHashMap();
/*  22 */   private final Map<String, ScorePlayerTeam> teamMemberships = Maps.newHashMap();
/*  23 */   private static String[] field_178823_g = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ScoreObjective getObjective(String name)
/*     */   {
/*  30 */     return (ScoreObjective)this.scoreObjectives.get(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ScoreObjective addScoreObjective(String name, IScoreObjectiveCriteria criteria)
/*     */   {
/*  38 */     if (name.length() > 16)
/*     */     {
/*  40 */       throw new IllegalArgumentException("The objective name '" + name + "' is too long!");
/*     */     }
/*     */     
/*     */ 
/*  44 */     ScoreObjective scoreobjective = getObjective(name);
/*     */     
/*  46 */     if (scoreobjective != null)
/*     */     {
/*  48 */       throw new IllegalArgumentException("An objective with the name '" + name + "' already exists!");
/*     */     }
/*     */     
/*     */ 
/*  52 */     scoreobjective = new ScoreObjective(this, name, criteria);
/*  53 */     List<ScoreObjective> list = (List)this.scoreObjectiveCriterias.get(criteria);
/*     */     
/*  55 */     if (list == null)
/*     */     {
/*  57 */       list = Lists.newArrayList();
/*  58 */       this.scoreObjectiveCriterias.put(criteria, list);
/*     */     }
/*     */     
/*  61 */     list.add(scoreobjective);
/*  62 */     this.scoreObjectives.put(name, scoreobjective);
/*  63 */     onScoreObjectiveAdded(scoreobjective);
/*  64 */     return scoreobjective;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Collection<ScoreObjective> getObjectivesFromCriteria(IScoreObjectiveCriteria criteria)
/*     */   {
/*  71 */     Collection<ScoreObjective> collection = (Collection)this.scoreObjectiveCriterias.get(criteria);
/*  72 */     return collection == null ? Lists.newArrayList() : Lists.newArrayList(collection);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean entityHasObjective(String name, ScoreObjective p_178819_2_)
/*     */   {
/*  80 */     Map<ScoreObjective, Score> map = (Map)this.entitiesScoreObjectives.get(name);
/*     */     
/*  82 */     if (map == null)
/*     */     {
/*  84 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  88 */     Score score = (Score)map.get(p_178819_2_);
/*  89 */     return score != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Score getValueFromObjective(String name, ScoreObjective objective)
/*     */   {
/*  98 */     if (name.length() > 40)
/*     */     {
/* 100 */       throw new IllegalArgumentException("The player name '" + name + "' is too long!");
/*     */     }
/*     */     
/*     */ 
/* 104 */     Map<ScoreObjective, Score> map = (Map)this.entitiesScoreObjectives.get(name);
/*     */     
/* 106 */     if (map == null)
/*     */     {
/* 108 */       map = Maps.newHashMap();
/* 109 */       this.entitiesScoreObjectives.put(name, map);
/*     */     }
/*     */     
/* 112 */     Score score = (Score)map.get(objective);
/*     */     
/* 114 */     if (score == null)
/*     */     {
/* 116 */       score = new Score(this, objective, name);
/* 117 */       map.put(objective, score);
/*     */     }
/*     */     
/* 120 */     return score;
/*     */   }
/*     */   
/*     */ 
/*     */   public Collection<Score> getSortedScores(ScoreObjective objective)
/*     */   {
/* 126 */     List<Score> list = Lists.newArrayList();
/*     */     
/* 128 */     for (Map<ScoreObjective, Score> map : this.entitiesScoreObjectives.values())
/*     */     {
/* 130 */       Score score = (Score)map.get(objective);
/*     */       
/* 132 */       if (score != null)
/*     */       {
/* 134 */         list.add(score);
/*     */       }
/*     */     }
/*     */     
/* 138 */     Collections.sort(list, Score.scoreComparator);
/* 139 */     return list;
/*     */   }
/*     */   
/*     */   public Collection<ScoreObjective> getScoreObjectives()
/*     */   {
/* 144 */     return this.scoreObjectives.values();
/*     */   }
/*     */   
/*     */   public Collection<String> getObjectiveNames()
/*     */   {
/* 149 */     return this.entitiesScoreObjectives.keySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeObjectiveFromEntity(String name, ScoreObjective objective)
/*     */   {
/* 157 */     if (objective == null)
/*     */     {
/* 159 */       Map<ScoreObjective, Score> map = (Map)this.entitiesScoreObjectives.remove(name);
/*     */       
/* 161 */       if (map != null)
/*     */       {
/* 163 */         func_96516_a(name);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 168 */       Map<ScoreObjective, Score> map2 = (Map)this.entitiesScoreObjectives.get(name);
/*     */       
/* 170 */       if (map2 != null)
/*     */       {
/* 172 */         Score score = (Score)map2.remove(objective);
/*     */         
/* 174 */         if (map2.size() < 1)
/*     */         {
/* 176 */           Map<ScoreObjective, Score> map1 = (Map)this.entitiesScoreObjectives.remove(name);
/*     */           
/* 178 */           if (map1 != null)
/*     */           {
/* 180 */             func_96516_a(name);
/*     */           }
/*     */         }
/* 183 */         else if (score != null)
/*     */         {
/* 185 */           func_178820_a(name, objective);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Collection<Score> getScores()
/*     */   {
/* 193 */     Collection<Map<ScoreObjective, Score>> collection = this.entitiesScoreObjectives.values();
/* 194 */     List<Score> list = Lists.newArrayList();
/*     */     
/* 196 */     for (Map<ScoreObjective, Score> map : collection)
/*     */     {
/* 198 */       list.addAll(map.values());
/*     */     }
/*     */     
/* 201 */     return list;
/*     */   }
/*     */   
/*     */   public Map<ScoreObjective, Score> getObjectivesForEntity(String name)
/*     */   {
/* 206 */     Map<ScoreObjective, Score> map = (Map)this.entitiesScoreObjectives.get(name);
/*     */     
/* 208 */     if (map == null)
/*     */     {
/* 210 */       map = Maps.newHashMap();
/*     */     }
/*     */     
/* 213 */     return map;
/*     */   }
/*     */   
/*     */   public void removeObjective(ScoreObjective p_96519_1_)
/*     */   {
/* 218 */     this.scoreObjectives.remove(p_96519_1_.getName());
/*     */     
/* 220 */     for (int i = 0; i < 19; i++)
/*     */     {
/* 222 */       if (getObjectiveInDisplaySlot(i) == p_96519_1_)
/*     */       {
/* 224 */         setObjectiveInDisplaySlot(i, null);
/*     */       }
/*     */     }
/*     */     
/* 228 */     List<ScoreObjective> list = (List)this.scoreObjectiveCriterias.get(p_96519_1_.getCriteria());
/*     */     
/* 230 */     if (list != null)
/*     */     {
/* 232 */       list.remove(p_96519_1_);
/*     */     }
/*     */     
/* 235 */     for (Map<ScoreObjective, Score> map : this.entitiesScoreObjectives.values())
/*     */     {
/* 237 */       map.remove(p_96519_1_);
/*     */     }
/*     */     
/* 240 */     func_96533_c(p_96519_1_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setObjectiveInDisplaySlot(int p_96530_1_, ScoreObjective p_96530_2_)
/*     */   {
/* 248 */     this.objectiveDisplaySlots[p_96530_1_] = p_96530_2_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ScoreObjective getObjectiveInDisplaySlot(int p_96539_1_)
/*     */   {
/* 256 */     return this.objectiveDisplaySlots[p_96539_1_];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ScorePlayerTeam getTeam(String p_96508_1_)
/*     */   {
/* 264 */     return (ScorePlayerTeam)this.teams.get(p_96508_1_);
/*     */   }
/*     */   
/*     */   public ScorePlayerTeam createTeam(String p_96527_1_)
/*     */   {
/* 269 */     if (p_96527_1_.length() > 16)
/*     */     {
/* 271 */       throw new IllegalArgumentException("The team name '" + p_96527_1_ + "' is too long!");
/*     */     }
/*     */     
/*     */ 
/* 275 */     ScorePlayerTeam scoreplayerteam = getTeam(p_96527_1_);
/*     */     
/* 277 */     if (scoreplayerteam != null)
/*     */     {
/* 279 */       throw new IllegalArgumentException("A team with the name '" + p_96527_1_ + "' already exists!");
/*     */     }
/*     */     
/*     */ 
/* 283 */     scoreplayerteam = new ScorePlayerTeam(this, p_96527_1_);
/* 284 */     this.teams.put(p_96527_1_, scoreplayerteam);
/* 285 */     broadcastTeamCreated(scoreplayerteam);
/* 286 */     return scoreplayerteam;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeTeam(ScorePlayerTeam p_96511_1_)
/*     */   {
/* 296 */     this.teams.remove(p_96511_1_.getRegisteredName());
/*     */     
/* 298 */     for (String s : p_96511_1_.getMembershipCollection())
/*     */     {
/* 300 */       this.teamMemberships.remove(s);
/*     */     }
/*     */     
/* 303 */     func_96513_c(p_96511_1_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean addPlayerToTeam(String player, String newTeam)
/*     */   {
/* 311 */     if (player.length() > 40)
/*     */     {
/* 313 */       throw new IllegalArgumentException("The player name '" + player + "' is too long!");
/*     */     }
/* 315 */     if (!this.teams.containsKey(newTeam))
/*     */     {
/* 317 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 321 */     ScorePlayerTeam scoreplayerteam = getTeam(newTeam);
/*     */     
/* 323 */     if (getPlayersTeam(player) != null)
/*     */     {
/* 325 */       removePlayerFromTeams(player);
/*     */     }
/*     */     
/* 328 */     this.teamMemberships.put(player, scoreplayerteam);
/* 329 */     scoreplayerteam.getMembershipCollection().add(player);
/* 330 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean removePlayerFromTeams(String p_96524_1_)
/*     */   {
/* 336 */     ScorePlayerTeam scoreplayerteam = getPlayersTeam(p_96524_1_);
/*     */     
/* 338 */     if (scoreplayerteam != null)
/*     */     {
/* 340 */       removePlayerFromTeam(p_96524_1_, scoreplayerteam);
/* 341 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 345 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removePlayerFromTeam(String p_96512_1_, ScorePlayerTeam p_96512_2_)
/*     */   {
/* 355 */     if (getPlayersTeam(p_96512_1_) != p_96512_2_)
/*     */     {
/* 357 */       throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team '" + p_96512_2_.getRegisteredName() + "'.");
/*     */     }
/*     */     
/*     */ 
/* 361 */     this.teamMemberships.remove(p_96512_1_);
/* 362 */     p_96512_2_.getMembershipCollection().remove(p_96512_1_);
/*     */   }
/*     */   
/*     */ 
/*     */   public Collection<String> getTeamNames()
/*     */   {
/* 368 */     return this.teams.keySet();
/*     */   }
/*     */   
/*     */   public Collection<ScorePlayerTeam> getTeams()
/*     */   {
/* 373 */     return this.teams.values();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ScorePlayerTeam getPlayersTeam(String p_96509_1_)
/*     */   {
/* 381 */     return (ScorePlayerTeam)this.teamMemberships.get(p_96509_1_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onScoreObjectiveAdded(ScoreObjective scoreObjectiveIn) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void func_96532_b(ScoreObjective p_96532_1_) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void func_96533_c(ScoreObjective p_96533_1_) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void func_96536_a(Score p_96536_1_) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void func_96516_a(String p_96516_1_) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void func_178820_a(String p_178820_1_, ScoreObjective p_178820_2_) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void broadcastTeamCreated(ScorePlayerTeam playerTeam) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendTeamUpdate(ScorePlayerTeam playerTeam) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void func_96513_c(ScorePlayerTeam playerTeam) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getObjectiveDisplaySlot(int p_96517_0_)
/*     */   {
/* 434 */     switch (p_96517_0_)
/*     */     {
/*     */     case 0: 
/* 437 */       return "list";
/*     */     
/*     */     case 1: 
/* 440 */       return "sidebar";
/*     */     
/*     */     case 2: 
/* 443 */       return "belowName";
/*     */     }
/*     */     
/* 446 */     if ((p_96517_0_ >= 3) && (p_96517_0_ <= 18))
/*     */     {
/* 448 */       EnumChatFormatting enumchatformatting = EnumChatFormatting.func_175744_a(p_96517_0_ - 3);
/*     */       
/* 450 */       if ((enumchatformatting != null) && (enumchatformatting != EnumChatFormatting.RESET))
/*     */       {
/* 452 */         return "sidebar.team." + enumchatformatting.getFriendlyName();
/*     */       }
/*     */     }
/*     */     
/* 456 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getObjectiveDisplaySlotNumber(String p_96537_0_)
/*     */   {
/* 465 */     if (p_96537_0_.equalsIgnoreCase("list"))
/*     */     {
/* 467 */       return 0;
/*     */     }
/* 469 */     if (p_96537_0_.equalsIgnoreCase("sidebar"))
/*     */     {
/* 471 */       return 1;
/*     */     }
/* 473 */     if (p_96537_0_.equalsIgnoreCase("belowName"))
/*     */     {
/* 475 */       return 2;
/*     */     }
/*     */     
/*     */ 
/* 479 */     if (p_96537_0_.startsWith("sidebar.team."))
/*     */     {
/* 481 */       String s = p_96537_0_.substring("sidebar.team.".length());
/* 482 */       EnumChatFormatting enumchatformatting = EnumChatFormatting.getValueByName(s);
/*     */       
/* 484 */       if ((enumchatformatting != null) && (enumchatformatting.getColorIndex() >= 0))
/*     */       {
/* 486 */         return enumchatformatting.getColorIndex() + 3;
/*     */       }
/*     */     }
/*     */     
/* 490 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public static String[] getDisplaySlotStrings()
/*     */   {
/* 496 */     if (field_178823_g == null)
/*     */     {
/* 498 */       field_178823_g = new String[19];
/*     */       
/* 500 */       for (int i = 0; i < 19; i++)
/*     */       {
/* 502 */         field_178823_g[i] = getObjectiveDisplaySlot(i);
/*     */       }
/*     */     }
/*     */     
/* 506 */     return field_178823_g;
/*     */   }
/*     */   
/*     */   public void func_181140_a(Entity p_181140_1_)
/*     */   {
/* 511 */     if ((p_181140_1_ != null) && (!(p_181140_1_ instanceof EntityPlayer)) && (!p_181140_1_.isEntityAlive()))
/*     */     {
/* 513 */       String s = p_181140_1_.getUniqueID().toString();
/* 514 */       removeObjectiveFromEntity(s, null);
/* 515 */       removePlayerFromTeams(s);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\scoreboard\Scoreboard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */