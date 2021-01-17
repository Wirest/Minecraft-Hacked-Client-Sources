package net.minecraft.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.util.EnumChatFormatting;

public class Scoreboard
{
    /** Map of objective names to ScoreObjective objects. */
    private final Map scoreObjectives = Maps.newHashMap();
    private final Map scoreObjectiveCriterias = Maps.newHashMap();
    private final Map field_96544_c = Maps.newHashMap();

    /** Index 0 is tab menu, 1 is sidebar, and 2 is below name */
    private final ScoreObjective[] objectiveDisplaySlots = new ScoreObjective[19];

    /** Map of teamnames to ScorePlayerTeam instances */
    private final Map teams = Maps.newHashMap();

    /** Map of usernames to ScorePlayerTeam objects. */
    private final Map teamMemberships = Maps.newHashMap();
    private static String[] field_178823_g = null;
    private static final String __OBFID = "CL_00000619";

    /**
     * Returns a ScoreObjective for the objective name
     */
    public ScoreObjective getObjective(String p_96518_1_)
    {
        return (ScoreObjective)this.scoreObjectives.get(p_96518_1_);
    }

    public ScoreObjective addScoreObjective(String p_96535_1_, IScoreObjectiveCriteria p_96535_2_)
    {
        ScoreObjective var3 = this.getObjective(p_96535_1_);

        if (var3 != null)
        {
            throw new IllegalArgumentException("An objective with the name \'" + p_96535_1_ + "\' already exists!");
        }
        else
        {
            var3 = new ScoreObjective(this, p_96535_1_, p_96535_2_);
            Object var4 = (List)this.scoreObjectiveCriterias.get(p_96535_2_);

            if (var4 == null)
            {
                var4 = Lists.newArrayList();
                this.scoreObjectiveCriterias.put(p_96535_2_, var4);
            }

            ((List)var4).add(var3);
            this.scoreObjectives.put(p_96535_1_, var3);
            this.func_96522_a(var3);
            return var3;
        }
    }

    public Collection func_96520_a(IScoreObjectiveCriteria p_96520_1_)
    {
        Collection var2 = (Collection)this.scoreObjectiveCriterias.get(p_96520_1_);
        return var2 == null ? Lists.newArrayList() : Lists.newArrayList(var2);
    }

    public boolean func_178819_b(String p_178819_1_, ScoreObjective p_178819_2_)
    {
        Map var3 = (Map)this.field_96544_c.get(p_178819_1_);

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

    public Score getValueFromObjective(String p_96529_1_, ScoreObjective p_96529_2_)
    {
        Object var3 = (Map)this.field_96544_c.get(p_96529_1_);

        if (var3 == null)
        {
            var3 = Maps.newHashMap();
            this.field_96544_c.put(p_96529_1_, var3);
        }

        Score var4 = (Score)((Map)var3).get(p_96529_2_);

        if (var4 == null)
        {
            var4 = new Score(this, p_96529_2_, p_96529_1_);
            ((Map)var3).put(p_96529_2_, var4);
        }

        return var4;
    }

    /**
     * Returns an array of Score objects, sorting by Score.getScorePoints()
     */
    public Collection getSortedScores(ScoreObjective p_96534_1_)
    {
        ArrayList var2 = Lists.newArrayList();
        Iterator var3 = this.field_96544_c.values().iterator();

        while (var3.hasNext())
        {
            Map var4 = (Map)var3.next();
            Score var5 = (Score)var4.get(p_96534_1_);

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
        return this.field_96544_c.keySet();
    }

    public void func_178822_d(String p_178822_1_, ScoreObjective p_178822_2_)
    {
        Map var3;

        if (p_178822_2_ == null)
        {
            var3 = (Map)this.field_96544_c.remove(p_178822_1_);

            if (var3 != null)
            {
                this.func_96516_a(p_178822_1_);
            }
        }
        else
        {
            var3 = (Map)this.field_96544_c.get(p_178822_1_);

            if (var3 != null)
            {
                Score var4 = (Score)var3.remove(p_178822_2_);

                if (var3.size() < 1)
                {
                    Map var5 = (Map)this.field_96544_c.remove(p_178822_1_);

                    if (var5 != null)
                    {
                        this.func_96516_a(p_178822_1_);
                    }
                }
                else if (var4 != null)
                {
                    this.func_178820_a(p_178822_1_, p_178822_2_);
                }
            }
        }
    }

    public Collection func_96528_e()
    {
        Collection var1 = this.field_96544_c.values();
        ArrayList var2 = Lists.newArrayList();
        Iterator var3 = var1.iterator();

        while (var3.hasNext())
        {
            Map var4 = (Map)var3.next();
            var2.addAll(var4.values());
        }

        return var2;
    }

    public Map func_96510_d(String p_96510_1_)
    {
        Object var2 = (Map)this.field_96544_c.get(p_96510_1_);

        if (var2 == null)
        {
            var2 = Maps.newHashMap();
        }

        return (Map)var2;
    }

    public void func_96519_k(ScoreObjective p_96519_1_)
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

        Iterator var3 = this.field_96544_c.values().iterator();

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

    public boolean func_151392_a(String p_151392_1_, String p_151392_2_)
    {
        if (!this.teams.containsKey(p_151392_2_))
        {
            return false;
        }
        else
        {
            ScorePlayerTeam var3 = this.getTeam(p_151392_2_);

            if (this.getPlayersTeam(p_151392_1_) != null)
            {
                this.removePlayerFromTeams(p_151392_1_);
            }

            this.teamMemberships.put(p_151392_1_, var3);
            var3.getMembershipCollection().add(p_151392_1_);
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
    public void broadcastTeamCreated(ScorePlayerTeam p_96523_1_) {}

    /**
     * This packet will notify the players that this team is removed
     */
    public void broadcastTeamRemoved(ScorePlayerTeam p_96538_1_) {}

    public void func_96513_c(ScorePlayerTeam p_96513_1_) {}

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

                if (var2 != null && var2.func_175746_b() >= 0)
                {
                    return var2.func_175746_b() + 3;
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
