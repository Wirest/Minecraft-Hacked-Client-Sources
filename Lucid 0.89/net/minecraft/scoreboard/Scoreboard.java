package net.minecraft.scoreboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.util.EnumChatFormatting;

public class Scoreboard
{
    /** Map of objective names to ScoreObjective objects. */
    private final Map scoreObjectives = Maps.newHashMap();

    /** Map of IScoreObjectiveCriteria objects to ScoreObjective objects. */
    private final Map scoreObjectiveCriterias = Maps.newHashMap();

    /** Map of entities name to ScoreObjective objects. */
    private final Map entitiesScoreObjectives = Maps.newHashMap();

    /** Index 0 is tab menu, 1 is sidebar, and 2 is below name */
    private final ScoreObjective[] objectiveDisplaySlots = new ScoreObjective[19];

    /** Map of teamnames to ScorePlayerTeam instances */
    private final Map teams = Maps.newHashMap();

    /** Map of usernames to ScorePlayerTeam objects. */
    private final Map teamMemberships = Maps.newHashMap();
    private static String[] field_178823_g = null;

    /**
     * Returns a ScoreObjective for the objective name
     *  
     * @param name The objective name
     */
    public ScoreObjective getObjective(String name)
    {
        return (ScoreObjective)this.scoreObjectives.get(name);
    }

    /**
     * Create and returns the score objective for the given name and ScoreCriteria
     *  
     * @param name The ScoreObjective Name
     * @param criteria The ScoreObjective Criteria
     */
    public ScoreObjective addScoreObjective(String name, IScoreObjectiveCriteria criteria)
    {
        ScoreObjective var3 = this.getObjective(name);

        if (var3 != null)
        {
            throw new IllegalArgumentException("An objective with the name \'" + name + "\' already exists!");
        }
        else
        {
            var3 = new ScoreObjective(this, name, criteria);
            Object var4 = this.scoreObjectiveCriterias.get(criteria);

            if (var4 == null)
            {
                var4 = Lists.newArrayList();
                this.scoreObjectiveCriterias.put(criteria, var4);
            }

            ((List)var4).add(var3);
            this.scoreObjectives.put(name, var3);
            this.func_96522_a(var3);
            return var3;
        }
    }

    /**
     * Returns all the objectives for the given criteria
     *  
     * @param criteria The objective criteria
     */
    public Collection getObjectivesFromCriteria(IScoreObjectiveCriteria criteria)
    {
        Collection var2 = (Collection)this.scoreObjectiveCriterias.get(criteria);
        return var2 == null ? Lists.newArrayList() : Lists.newArrayList(var2);
    }

    /**
     * Returns if the entity has the given ScoreObjective
     *  
     * @param name The Entity name
     */
    public boolean entityHasObjective(String name, ScoreObjective p_178819_2_)
    {
        Map var3 = (Map)this.entitiesScoreObjectives.get(name);

        if (var3 == null)
        {
            return false;
        }
        else
        {
            Score var4 = (Score)var3.get(p_178819_2_);
            return var4 != null;
        }
    }

    /**
     * Returns the value of the given objective for the given entity name
     *  
     * @param name The entity name
     * @param objective The ScoreObjective to get the value from
     */
    public Score getValueFromObjective(String name, ScoreObjective objective)
    {
        Object var3 = this.entitiesScoreObjectives.get(name);

        if (var3 == null)
        {
            var3 = Maps.newHashMap();
            this.entitiesScoreObjectives.put(name, var3);
        }

        Score var4 = (Score)((Map)var3).get(objective);

        if (var4 == null)
        {
            var4 = new Score(this, objective, name);
            ((Map)var3).put(objective, var4);
        }

        return var4;
    }

    /**
     * Returns an array of Score objects, sorting by Score.getScorePoints()
     */
    public Collection getSortedScores(ScoreObjective objective)
    {
        ArrayList var2 = Lists.newArrayList();
        Iterator var3 = this.entitiesScoreObjectives.values().iterator();

        while (var3.hasNext())
        {
            Map var4 = (Map)var3.next();
            Score var5 = (Score)var4.get(objective);

            if (var5 != null)
            {
                var2.add(var5);
            }
        }

        Collections.sort(var2, Score.scoreComparator);
        return var2;
    }

    public Collection getScoreObjectives()
    {
        return this.scoreObjectives.values();
    }

    public Collection getObjectiveNames()
    {
        return this.entitiesScoreObjectives.keySet();
    }

    /**
     * Remove the given ScoreObjective for the given Entity name.
     *  
     * @param name The entity Name
     * @param objective The ScoreObjective
     */
    public void removeObjectiveFromEntity(String name, ScoreObjective objective)
    {
        Map var3;

        if (objective == null)
        {
            var3 = (Map)this.entitiesScoreObjectives.remove(name);

            if (var3 != null)
            {
                this.func_96516_a(name);
            }
        }
        else
        {
            var3 = (Map)this.entitiesScoreObjectives.get(name);

            if (var3 != null)
            {
                Score var4 = (Score)var3.remove(objective);

                if (var3.size() < 1)
                {
                    Map var5 = (Map)this.entitiesScoreObjectives.remove(name);

                    if (var5 != null)
                    {
                        this.func_96516_a(name);
                    }
                }
                else if (var4 != null)
                {
                    this.func_178820_a(name, objective);
                }
            }
        }
    }

    public Collection getScores()
    {
        Collection var1 = this.entitiesScoreObjectives.values();
        ArrayList var2 = Lists.newArrayList();
        Iterator var3 = var1.iterator();

        while (var3.hasNext())
        {
            Map var4 = (Map)var3.next();
            var2.addAll(var4.values());
        }

        return var2;
    }

    /**
     * Returns all the objectives for the given entity
     *  
     * @param name The entity Name
     */
    public Map getObjectivesForEntity(String name)
    {
        Object var2 = this.entitiesScoreObjectives.get(name);

        if (var2 == null)
        {
            var2 = Maps.newHashMap();
        }

        return (Map)var2;
    }

    public void removeObjective(ScoreObjective p_96519_1_)
    {
        this.scoreObjectives.remove(p_96519_1_.getName());

        for (int var2 = 0; var2 < 19; ++var2)
        {
            if (this.getObjectiveInDisplaySlot(var2) == p_96519_1_)
            {
                this.setObjectiveInDisplaySlot(var2, (ScoreObjective)null);
            }
        }

        List var5 = (List)this.scoreObjectiveCriterias.get(p_96519_1_.getCriteria());

        if (var5 != null)
        {
            var5.remove(p_96519_1_);
        }

        Iterator var3 = this.entitiesScoreObjectives.values().iterator();

        while (var3.hasNext())
        {
            Map var4 = (Map)var3.next();
            var4.remove(p_96519_1_);
        }

        this.func_96533_c(p_96519_1_);
    }

    /**
     * 0 is tab menu, 1 is sidebar, 2 is below name
     */
    public void setObjectiveInDisplaySlot(int p_96530_1_, ScoreObjective p_96530_2_)
    {
        this.objectiveDisplaySlots[p_96530_1_] = p_96530_2_;
    }

    /**
     * 0 is tab menu, 1 is sidebar, 2 is below name
     */
    public ScoreObjective getObjectiveInDisplaySlot(int p_96539_1_)
    {
        return this.objectiveDisplaySlots[p_96539_1_];
    }

    /**
     * Retrieve the ScorePlayerTeam instance identified by the passed team name
     */
    public ScorePlayerTeam getTeam(String p_96508_1_)
    {
        return (ScorePlayerTeam)this.teams.get(p_96508_1_);
    }

    public ScorePlayerTeam createTeam(String p_96527_1_)
    {
        ScorePlayerTeam var2 = this.getTeam(p_96527_1_);

        if (var2 != null)
        {
            throw new IllegalArgumentException("A team with the name \'" + p_96527_1_ + "\' already exists!");
        }
        else
        {
            var2 = new ScorePlayerTeam(this, p_96527_1_);
            this.teams.put(p_96527_1_, var2);
            this.broadcastTeamCreated(var2);
            return var2;
        }
    }

    /**
     * Removes the team from the scoreboard, updates all player memberships and broadcasts the deletion to all players
     */
    public void removeTeam(ScorePlayerTeam p_96511_1_)
    {
        this.teams.remove(p_96511_1_.getRegisteredName());
        Iterator var2 = p_96511_1_.getMembershipCollection().iterator();

        while (var2.hasNext())
        {
            String var3 = (String)var2.next();
            this.teamMemberships.remove(var3);
        }

        this.func_96513_c(p_96511_1_);
    }

    /**
     * Adds a player to the given team
     *  
     * @param player The name of the player to add
     * @param newTeam The name of the team
     */
    public boolean addPlayerToTeam(String player, String newTeam)
    {
        if (!this.teams.containsKey(newTeam))
        {
            return false;
        }
        else
        {
            ScorePlayerTeam var3 = this.getTeam(newTeam);

            if (this.getPlayersTeam(player) != null)
            {
                this.removePlayerFromTeams(player);
            }

            this.teamMemberships.put(player, var3);
            var3.getMembershipCollection().add(player);
            return true;
        }
    }

    public boolean removePlayerFromTeams(String p_96524_1_)
    {
        ScorePlayerTeam var2 = this.getPlayersTeam(p_96524_1_);

        if (var2 != null)
        {
            this.removePlayerFromTeam(p_96524_1_, var2);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Removes the given username from the given ScorePlayerTeam. If the player is not on the team then an
     * IllegalStateException is thrown.
     */
    public void removePlayerFromTeam(String p_96512_1_, ScorePlayerTeam p_96512_2_)
    {
        if (this.getPlayersTeam(p_96512_1_) != p_96512_2_)
        {
            throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team \'" + p_96512_2_.getRegisteredName() + "\'.");
        }
        else
        {
            this.teamMemberships.remove(p_96512_1_);
            p_96512_2_.getMembershipCollection().remove(p_96512_1_);
        }
    }

    /**
     * Retrieve all registered ScorePlayerTeam names
     */
    public Collection getTeamNames()
    {
        return this.teams.keySet();
    }

    /**
     * Retrieve all registered ScorePlayerTeam instances
     */
    public Collection getTeams()
    {
        return this.teams.values();
    }

    /**
     * Gets the ScorePlayerTeam object for the given username.
     */
    public ScorePlayerTeam getPlayersTeam(String p_96509_1_)
    {
        return (ScorePlayerTeam)this.teamMemberships.get(p_96509_1_);
    }

    public void func_96522_a(ScoreObjective p_96522_1_) {}

    public void func_96532_b(ScoreObjective p_96532_1_) {}

    public void func_96533_c(ScoreObjective p_96533_1_) {}

    public void func_96536_a(Score p_96536_1_) {}

    public void func_96516_a(String p_96516_1_) {}

    public void func_178820_a(String p_178820_1_, ScoreObjective p_178820_2_) {}

    /**
     * This packet will notify the players that this team is created, and that will register it on the client
     */
    public void broadcastTeamCreated(ScorePlayerTeam playerTeam) {}

    /**
     * This packet will notify the players that this team is updated
     */
    public void sendTeamUpdate(ScorePlayerTeam playerTeam) {}

    public void func_96513_c(ScorePlayerTeam playerTeam) {}

    /**
     * Returns 'list' for 0, 'sidebar' for 1, 'belowName for 2, otherwise null.
     */
    public static String getObjectiveDisplaySlot(int p_96517_0_)
    {
        switch (p_96517_0_)
        {
            case 0:
                return "list";

            case 1:
                return "sidebar";

            case 2:
                return "belowName";

            default:
                if (p_96517_0_ >= 3 && p_96517_0_ <= 18)
                {
                    EnumChatFormatting var1 = EnumChatFormatting.func_175744_a(p_96517_0_ - 3);

                    if (var1 != null && var1 != EnumChatFormatting.RESET)
                    {
                        return "sidebar.team." + var1.getFriendlyName();
                    }
                }

                return null;
        }
    }

    /**
     * Returns 0 for (case-insensitive) 'list', 1 for 'sidebar', 2 for 'belowName', otherwise -1.
     */
    public static int getObjectiveDisplaySlotNumber(String p_96537_0_)
    {
        if (p_96537_0_.equalsIgnoreCase("list"))
        {
            return 0;
        }
        else if (p_96537_0_.equalsIgnoreCase("sidebar"))
        {
            return 1;
        }
        else if (p_96537_0_.equalsIgnoreCase("belowName"))
        {
            return 2;
        }
        else
        {
            if (p_96537_0_.startsWith("sidebar.team."))
            {
                String var1 = p_96537_0_.substring("sidebar.team.".length());
                EnumChatFormatting var2 = EnumChatFormatting.getValueByName(var1);

                if (var2 != null && var2.getColorIndex() >= 0)
                {
                    return var2.getColorIndex() + 3;
                }
            }

            return -1;
        }
    }

    public static String[] func_178821_h()
    {
        if (field_178823_g == null)
        {
            field_178823_g = new String[19];

            for (int var0 = 0; var0 < 19; ++var0)
            {
                field_178823_g[var0] = getObjectiveDisplaySlot(var0);
            }
        }

        return field_178823_g;
    }
}
