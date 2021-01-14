package net.minecraft.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

public class Scoreboard {
   private final Map scoreObjectives = Maps.newHashMap();
   private final Map scoreObjectiveCriterias = Maps.newHashMap();
   private final Map entitiesScoreObjectives = Maps.newHashMap();
   private final ScoreObjective[] objectiveDisplaySlots = new ScoreObjective[19];
   private final Map teams = Maps.newHashMap();
   private final Map teamMemberships = Maps.newHashMap();
   private static String[] field_178823_g = null;

   public ScoreObjective getObjective(String name) {
      return (ScoreObjective)this.scoreObjectives.get(name);
   }

   public ScoreObjective addScoreObjective(String name, IScoreObjectiveCriteria criteria) {
      if (name.length() > 16) {
         throw new IllegalArgumentException("The objective name '" + name + "' is too long!");
      } else {
         ScoreObjective scoreobjective = this.getObjective(name);
         if (scoreobjective != null) {
            throw new IllegalArgumentException("An objective with the name '" + name + "' already exists!");
         } else {
            scoreobjective = new ScoreObjective(this, name, criteria);
            List list = (List)this.scoreObjectiveCriterias.get(criteria);
            if (list == null) {
               list = Lists.newArrayList();
               this.scoreObjectiveCriterias.put(criteria, list);
            }

            ((List)list).add(scoreobjective);
            this.scoreObjectives.put(name, scoreobjective);
            this.onScoreObjectiveAdded(scoreobjective);
            return scoreobjective;
         }
      }
   }

   public Collection getObjectivesFromCriteria(IScoreObjectiveCriteria criteria) {
      Collection collection = (Collection)this.scoreObjectiveCriterias.get(criteria);
      return collection == null ? Lists.newArrayList() : Lists.newArrayList(collection);
   }

   public boolean entityHasObjective(String name, ScoreObjective p_178819_2_) {
      Map map = (Map)this.entitiesScoreObjectives.get(name);
      if (map == null) {
         return false;
      } else {
         Score score = (Score)map.get(p_178819_2_);
         return score != null;
      }
   }

   public Score getValueFromObjective(String name, ScoreObjective objective) {
      if (name.length() > 40) {
         throw new IllegalArgumentException("The player name '" + name + "' is too long!");
      } else {
         Map map = (Map)this.entitiesScoreObjectives.get(name);
         if (map == null) {
            map = Maps.newHashMap();
            this.entitiesScoreObjectives.put(name, map);
         }

         Score score = (Score)((Map)map).get(objective);
         if (score == null) {
            score = new Score(this, objective, name);
            ((Map)map).put(objective, score);
         }

         return score;
      }
   }

   public Collection getSortedScores(ScoreObjective objective) {
      List list = Lists.newArrayList();
      Iterator var3 = this.entitiesScoreObjectives.values().iterator();

      while(var3.hasNext()) {
         Map map = (Map)var3.next();
         Score score = (Score)map.get(objective);
         if (score != null) {
            list.add(score);
         }
      }

      Collections.sort(list, Score.scoreComparator);
      return list;
   }

   public Collection getScoreObjectives() {
      return this.scoreObjectives.values();
   }

   public Collection getObjectiveNames() {
      return this.entitiesScoreObjectives.keySet();
   }

   public void removeObjectiveFromEntity(String name, ScoreObjective objective) {
      Map map2;
      if (objective == null) {
         map2 = (Map)this.entitiesScoreObjectives.remove(name);
         if (map2 != null) {
            this.func_96516_a(name);
         }
      } else {
         map2 = (Map)this.entitiesScoreObjectives.get(name);
         if (map2 != null) {
            Score score = (Score)map2.remove(objective);
            if (map2.size() < 1) {
               Map map1 = (Map)this.entitiesScoreObjectives.remove(name);
               if (map1 != null) {
                  this.func_96516_a(name);
               }
            } else if (score != null) {
               this.func_178820_a(name, objective);
            }
         }
      }

   }

   public Collection getScores() {
      Collection collection = this.entitiesScoreObjectives.values();
      List list = Lists.newArrayList();
      Iterator var3 = collection.iterator();

      while(var3.hasNext()) {
         Map map = (Map)var3.next();
         list.addAll(map.values());
      }

      return list;
   }

   public Map getObjectivesForEntity(String name) {
      Map map = (Map)this.entitiesScoreObjectives.get(name);
      if (map == null) {
         map = Maps.newHashMap();
      }

      return (Map)map;
   }

   public void removeObjective(ScoreObjective p_96519_1_) {
      this.scoreObjectives.remove(p_96519_1_.getName());

      for(int i = 0; i < 19; ++i) {
         if (this.getObjectiveInDisplaySlot(i) == p_96519_1_) {
            this.setObjectiveInDisplaySlot(i, (ScoreObjective)null);
         }
      }

      List list = (List)this.scoreObjectiveCriterias.get(p_96519_1_.getCriteria());
      if (list != null) {
         list.remove(p_96519_1_);
      }

      Iterator var3 = this.entitiesScoreObjectives.values().iterator();

      while(var3.hasNext()) {
         Map map = (Map)var3.next();
         map.remove(p_96519_1_);
      }

      this.func_96533_c(p_96519_1_);
   }

   public void setObjectiveInDisplaySlot(int p_96530_1_, ScoreObjective p_96530_2_) {
      this.objectiveDisplaySlots[p_96530_1_] = p_96530_2_;
   }

   public ScoreObjective getObjectiveInDisplaySlot(int p_96539_1_) {
      return this.objectiveDisplaySlots[p_96539_1_];
   }

   public ScorePlayerTeam getTeam(String p_96508_1_) {
      return (ScorePlayerTeam)this.teams.get(p_96508_1_);
   }

   public ScorePlayerTeam createTeam(String p_96527_1_) {
      if (p_96527_1_.length() > 16) {
         throw new IllegalArgumentException("The team name '" + p_96527_1_ + "' is too long!");
      } else {
         ScorePlayerTeam scoreplayerteam = this.getTeam(p_96527_1_);
         if (scoreplayerteam != null) {
            throw new IllegalArgumentException("A team with the name '" + p_96527_1_ + "' already exists!");
         } else {
            scoreplayerteam = new ScorePlayerTeam(this, p_96527_1_);
            this.teams.put(p_96527_1_, scoreplayerteam);
            this.broadcastTeamCreated(scoreplayerteam);
            return scoreplayerteam;
         }
      }
   }

   public void removeTeam(ScorePlayerTeam p_96511_1_) {
      this.teams.remove(p_96511_1_.getRegisteredName());
      Iterator var2 = p_96511_1_.getMembershipCollection().iterator();

      while(var2.hasNext()) {
         String s = (String)var2.next();
         this.teamMemberships.remove(s);
      }

      this.func_96513_c(p_96511_1_);
   }

   public boolean addPlayerToTeam(String player, String newTeam) {
      if (player.length() > 40) {
         throw new IllegalArgumentException("The player name '" + player + "' is too long!");
      } else if (!this.teams.containsKey(newTeam)) {
         return false;
      } else {
         ScorePlayerTeam scoreplayerteam = this.getTeam(newTeam);
         if (this.getPlayersTeam(player) != null) {
            this.removePlayerFromTeams(player);
         }

         this.teamMemberships.put(player, scoreplayerteam);
         scoreplayerteam.getMembershipCollection().add(player);
         return true;
      }
   }

   public boolean removePlayerFromTeams(String p_96524_1_) {
      ScorePlayerTeam scoreplayerteam = this.getPlayersTeam(p_96524_1_);
      if (scoreplayerteam != null) {
         this.removePlayerFromTeam(p_96524_1_, scoreplayerteam);
         return true;
      } else {
         return false;
      }
   }

   public void removePlayerFromTeam(String p_96512_1_, ScorePlayerTeam p_96512_2_) {
      if (this.getPlayersTeam(p_96512_1_) != p_96512_2_) {
         throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team '" + p_96512_2_.getRegisteredName() + "'.");
      } else {
         this.teamMemberships.remove(p_96512_1_);
         p_96512_2_.getMembershipCollection().remove(p_96512_1_);
      }
   }

   public Collection getTeamNames() {
      return this.teams.keySet();
   }

   public Collection getTeams() {
      return this.teams.values();
   }

   public ScorePlayerTeam getPlayersTeam(String p_96509_1_) {
      return (ScorePlayerTeam)this.teamMemberships.get(p_96509_1_);
   }

   public void onScoreObjectiveAdded(ScoreObjective scoreObjectiveIn) {
   }

   public void func_96532_b(ScoreObjective p_96532_1_) {
   }

   public void func_96533_c(ScoreObjective p_96533_1_) {
   }

   public void func_96536_a(Score p_96536_1_) {
   }

   public void func_96516_a(String p_96516_1_) {
   }

   public void func_178820_a(String p_178820_1_, ScoreObjective p_178820_2_) {
   }

   public void broadcastTeamCreated(ScorePlayerTeam playerTeam) {
   }

   public void sendTeamUpdate(ScorePlayerTeam playerTeam) {
   }

   public void func_96513_c(ScorePlayerTeam playerTeam) {
   }

   public static String getObjectiveDisplaySlot(int p_96517_0_) {
      switch(p_96517_0_) {
      case 0:
         return "list";
      case 1:
         return "sidebar";
      case 2:
         return "belowName";
      default:
         if (p_96517_0_ >= 3 && p_96517_0_ <= 18) {
            EnumChatFormatting enumchatformatting = EnumChatFormatting.func_175744_a(p_96517_0_ - 3);
            if (enumchatformatting != null && enumchatformatting != EnumChatFormatting.RESET) {
               return "sidebar.team." + enumchatformatting.getFriendlyName();
            }
         }

         return null;
      }
   }

   public static int getObjectiveDisplaySlotNumber(String p_96537_0_) {
      if (p_96537_0_.equalsIgnoreCase("list")) {
         return 0;
      } else if (p_96537_0_.equalsIgnoreCase("sidebar")) {
         return 1;
      } else if (p_96537_0_.equalsIgnoreCase("belowName")) {
         return 2;
      } else {
         if (p_96537_0_.startsWith("sidebar.team.")) {
            String s = p_96537_0_.substring("sidebar.team.".length());
            EnumChatFormatting enumchatformatting = EnumChatFormatting.getValueByName(s);
            if (enumchatformatting != null && enumchatformatting.getColorIndex() >= 0) {
               return enumchatformatting.getColorIndex() + 3;
            }
         }

         return -1;
      }
   }

   public static String[] getDisplaySlotStrings() {
      if (field_178823_g == null) {
         field_178823_g = new String[19];

         for(int i = 0; i < 19; ++i) {
            field_178823_g[i] = getObjectiveDisplaySlot(i);
         }
      }

      return field_178823_g;
   }

   public void func_181140_a(Entity p_181140_1_) {
      if (p_181140_1_ != null && !(p_181140_1_ instanceof EntityPlayer) && !p_181140_1_.isEntityAlive()) {
         String s = p_181140_1_.getUniqueID().toString();
         this.removeObjectiveFromEntity(s, (ScoreObjective)null);
         this.removePlayerFromTeams(s);
      }

   }
}
