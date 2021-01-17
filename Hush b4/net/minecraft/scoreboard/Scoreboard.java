// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.scoreboard;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumChatFormatting;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collections;
import java.util.Collection;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

public class Scoreboard
{
    private final Map<String, ScoreObjective> scoreObjectives;
    private final Map<IScoreObjectiveCriteria, List<ScoreObjective>> scoreObjectiveCriterias;
    private final Map<String, Map<ScoreObjective, Score>> entitiesScoreObjectives;
    private final ScoreObjective[] objectiveDisplaySlots;
    private final Map<String, ScorePlayerTeam> teams;
    private final Map<String, ScorePlayerTeam> teamMemberships;
    private static String[] field_178823_g;
    
    static {
        Scoreboard.field_178823_g = null;
    }
    
    public Scoreboard() {
        this.scoreObjectives = (Map<String, ScoreObjective>)Maps.newHashMap();
        this.scoreObjectiveCriterias = (Map<IScoreObjectiveCriteria, List<ScoreObjective>>)Maps.newHashMap();
        this.entitiesScoreObjectives = (Map<String, Map<ScoreObjective, Score>>)Maps.newHashMap();
        this.objectiveDisplaySlots = new ScoreObjective[19];
        this.teams = (Map<String, ScorePlayerTeam>)Maps.newHashMap();
        this.teamMemberships = (Map<String, ScorePlayerTeam>)Maps.newHashMap();
    }
    
    public ScoreObjective getObjective(final String name) {
        return this.scoreObjectives.get(name);
    }
    
    public ScoreObjective addScoreObjective(final String name, final IScoreObjectiveCriteria criteria) {
        if (name.length() > 16) {
            throw new IllegalArgumentException("The objective name '" + name + "' is too long!");
        }
        ScoreObjective scoreobjective = this.getObjective(name);
        if (scoreobjective != null) {
            throw new IllegalArgumentException("An objective with the name '" + name + "' already exists!");
        }
        scoreobjective = new ScoreObjective(this, name, criteria);
        List<ScoreObjective> list = this.scoreObjectiveCriterias.get(criteria);
        if (list == null) {
            list = (List<ScoreObjective>)Lists.newArrayList();
            this.scoreObjectiveCriterias.put(criteria, list);
        }
        list.add(scoreobjective);
        this.scoreObjectives.put(name, scoreobjective);
        this.onScoreObjectiveAdded(scoreobjective);
        return scoreobjective;
    }
    
    public Collection<ScoreObjective> getObjectivesFromCriteria(final IScoreObjectiveCriteria criteria) {
        final Collection<ScoreObjective> collection = this.scoreObjectiveCriterias.get(criteria);
        return (Collection<ScoreObjective>)((collection == null) ? Lists.newArrayList() : Lists.newArrayList((Iterable<?>)collection));
    }
    
    public boolean entityHasObjective(final String name, final ScoreObjective p_178819_2_) {
        final Map<ScoreObjective, Score> map = this.entitiesScoreObjectives.get(name);
        if (map == null) {
            return false;
        }
        final Score score = map.get(p_178819_2_);
        return score != null;
    }
    
    public Score getValueFromObjective(final String name, final ScoreObjective objective) {
        if (name.length() > 40) {
            throw new IllegalArgumentException("The player name '" + name + "' is too long!");
        }
        Map<ScoreObjective, Score> map = this.entitiesScoreObjectives.get(name);
        if (map == null) {
            map = (Map<ScoreObjective, Score>)Maps.newHashMap();
            this.entitiesScoreObjectives.put(name, map);
        }
        Score score = map.get(objective);
        if (score == null) {
            score = new Score(this, objective, name);
            map.put(objective, score);
        }
        return score;
    }
    
    public Collection<Score> getSortedScores(final ScoreObjective objective) {
        final List<Score> list = (List<Score>)Lists.newArrayList();
        for (final Map<ScoreObjective, Score> map : this.entitiesScoreObjectives.values()) {
            final Score score = map.get(objective);
            if (score != null) {
                list.add(score);
            }
        }
        Collections.sort(list, Score.scoreComparator);
        return list;
    }
    
    public Collection<ScoreObjective> getScoreObjectives() {
        return this.scoreObjectives.values();
    }
    
    public Collection<String> getObjectiveNames() {
        return this.entitiesScoreObjectives.keySet();
    }
    
    public void removeObjectiveFromEntity(final String name, final ScoreObjective objective) {
        if (objective == null) {
            final Map<ScoreObjective, Score> map = this.entitiesScoreObjectives.remove(name);
            if (map != null) {
                this.func_96516_a(name);
            }
        }
        else {
            final Map<ScoreObjective, Score> map2 = this.entitiesScoreObjectives.get(name);
            if (map2 != null) {
                final Score score = map2.remove(objective);
                if (map2.size() < 1) {
                    final Map<ScoreObjective, Score> map3 = this.entitiesScoreObjectives.remove(name);
                    if (map3 != null) {
                        this.func_96516_a(name);
                    }
                }
                else if (score != null) {
                    this.func_178820_a(name, objective);
                }
            }
        }
    }
    
    public Collection<Score> getScores() {
        final Collection<Map<ScoreObjective, Score>> collection = this.entitiesScoreObjectives.values();
        final List<Score> list = (List<Score>)Lists.newArrayList();
        for (final Map<ScoreObjective, Score> map : collection) {
            list.addAll(map.values());
        }
        return list;
    }
    
    public Map<ScoreObjective, Score> getObjectivesForEntity(final String name) {
        Map<ScoreObjective, Score> map = this.entitiesScoreObjectives.get(name);
        if (map == null) {
            map = (Map<ScoreObjective, Score>)Maps.newHashMap();
        }
        return map;
    }
    
    public void removeObjective(final ScoreObjective p_96519_1_) {
        this.scoreObjectives.remove(p_96519_1_.getName());
        for (int i = 0; i < 19; ++i) {
            if (this.getObjectiveInDisplaySlot(i) == p_96519_1_) {
                this.setObjectiveInDisplaySlot(i, null);
            }
        }
        final List<ScoreObjective> list = this.scoreObjectiveCriterias.get(p_96519_1_.getCriteria());
        if (list != null) {
            list.remove(p_96519_1_);
        }
        for (final Map<ScoreObjective, Score> map : this.entitiesScoreObjectives.values()) {
            map.remove(p_96519_1_);
        }
        this.func_96533_c(p_96519_1_);
    }
    
    public void setObjectiveInDisplaySlot(final int p_96530_1_, final ScoreObjective p_96530_2_) {
        this.objectiveDisplaySlots[p_96530_1_] = p_96530_2_;
    }
    
    public ScoreObjective getObjectiveInDisplaySlot(final int p_96539_1_) {
        return this.objectiveDisplaySlots[p_96539_1_];
    }
    
    public ScorePlayerTeam getTeam(final String p_96508_1_) {
        return this.teams.get(p_96508_1_);
    }
    
    public ScorePlayerTeam createTeam(final String p_96527_1_) {
        if (p_96527_1_.length() > 16) {
            throw new IllegalArgumentException("The team name '" + p_96527_1_ + "' is too long!");
        }
        ScorePlayerTeam scoreplayerteam = this.getTeam(p_96527_1_);
        if (scoreplayerteam != null) {
            throw new IllegalArgumentException("A team with the name '" + p_96527_1_ + "' already exists!");
        }
        scoreplayerteam = new ScorePlayerTeam(this, p_96527_1_);
        this.teams.put(p_96527_1_, scoreplayerteam);
        this.broadcastTeamCreated(scoreplayerteam);
        return scoreplayerteam;
    }
    
    public void removeTeam(final ScorePlayerTeam p_96511_1_) {
        this.teams.remove(p_96511_1_.getRegisteredName());
        for (final String s : p_96511_1_.getMembershipCollection()) {
            this.teamMemberships.remove(s);
        }
        this.func_96513_c(p_96511_1_);
    }
    
    public boolean addPlayerToTeam(final String player, final String newTeam) {
        if (player.length() > 40) {
            throw new IllegalArgumentException("The player name '" + player + "' is too long!");
        }
        if (!this.teams.containsKey(newTeam)) {
            return false;
        }
        final ScorePlayerTeam scoreplayerteam = this.getTeam(newTeam);
        if (this.getPlayersTeam(player) != null) {
            this.removePlayerFromTeams(player);
        }
        this.teamMemberships.put(player, scoreplayerteam);
        scoreplayerteam.getMembershipCollection().add(player);
        return true;
    }
    
    public boolean removePlayerFromTeams(final String p_96524_1_) {
        final ScorePlayerTeam scoreplayerteam = this.getPlayersTeam(p_96524_1_);
        if (scoreplayerteam != null) {
            this.removePlayerFromTeam(p_96524_1_, scoreplayerteam);
            return true;
        }
        return false;
    }
    
    public void removePlayerFromTeam(final String p_96512_1_, final ScorePlayerTeam p_96512_2_) {
        if (this.getPlayersTeam(p_96512_1_) != p_96512_2_) {
            throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team '" + p_96512_2_.getRegisteredName() + "'.");
        }
        this.teamMemberships.remove(p_96512_1_);
        p_96512_2_.getMembershipCollection().remove(p_96512_1_);
    }
    
    public Collection<String> getTeamNames() {
        return this.teams.keySet();
    }
    
    public Collection<ScorePlayerTeam> getTeams() {
        return this.teams.values();
    }
    
    public ScorePlayerTeam getPlayersTeam(final String p_96509_1_) {
        return this.teamMemberships.get(p_96509_1_);
    }
    
    public void onScoreObjectiveAdded(final ScoreObjective scoreObjectiveIn) {
    }
    
    public void func_96532_b(final ScoreObjective p_96532_1_) {
    }
    
    public void func_96533_c(final ScoreObjective p_96533_1_) {
    }
    
    public void func_96536_a(final Score p_96536_1_) {
    }
    
    public void func_96516_a(final String p_96516_1_) {
    }
    
    public void func_178820_a(final String p_178820_1_, final ScoreObjective p_178820_2_) {
    }
    
    public void broadcastTeamCreated(final ScorePlayerTeam playerTeam) {
    }
    
    public void sendTeamUpdate(final ScorePlayerTeam playerTeam) {
    }
    
    public void func_96513_c(final ScorePlayerTeam playerTeam) {
    }
    
    public static String getObjectiveDisplaySlot(final int p_96517_0_) {
        switch (p_96517_0_) {
            case 0: {
                return "list";
            }
            case 1: {
                return "sidebar";
            }
            case 2: {
                return "belowName";
            }
            default: {
                if (p_96517_0_ >= 3 && p_96517_0_ <= 18) {
                    final EnumChatFormatting enumchatformatting = EnumChatFormatting.func_175744_a(p_96517_0_ - 3);
                    if (enumchatformatting != null && enumchatformatting != EnumChatFormatting.RESET) {
                        return "sidebar.team." + enumchatformatting.getFriendlyName();
                    }
                }
                return null;
            }
        }
    }
    
    public static int getObjectiveDisplaySlotNumber(final String p_96537_0_) {
        if (p_96537_0_.equalsIgnoreCase("list")) {
            return 0;
        }
        if (p_96537_0_.equalsIgnoreCase("sidebar")) {
            return 1;
        }
        if (p_96537_0_.equalsIgnoreCase("belowName")) {
            return 2;
        }
        if (p_96537_0_.startsWith("sidebar.team.")) {
            final String s = p_96537_0_.substring("sidebar.team.".length());
            final EnumChatFormatting enumchatformatting = EnumChatFormatting.getValueByName(s);
            if (enumchatformatting != null && enumchatformatting.getColorIndex() >= 0) {
                return enumchatformatting.getColorIndex() + 3;
            }
        }
        return -1;
    }
    
    public static String[] getDisplaySlotStrings() {
        if (Scoreboard.field_178823_g == null) {
            Scoreboard.field_178823_g = new String[19];
            for (int i = 0; i < 19; ++i) {
                Scoreboard.field_178823_g[i] = getObjectiveDisplaySlot(i);
            }
        }
        return Scoreboard.field_178823_g;
    }
    
    public void func_181140_a(final Entity p_181140_1_) {
        if (p_181140_1_ != null && !(p_181140_1_ instanceof EntityPlayer) && !p_181140_1_.isEntityAlive()) {
            final String s = p_181140_1_.getUniqueID().toString();
            this.removeObjectiveFromEntity(s, null);
            this.removePlayerFromTeams(s);
        }
    }
}
