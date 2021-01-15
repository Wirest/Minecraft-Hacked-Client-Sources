package net.minecraft.scoreboard;

import java.util.Collection;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldSavedData;

public class ScoreboardSaveData extends WorldSavedData
{
    private static final Logger logger = LogManager.getLogger();
    private Scoreboard theScoreboard;
    private NBTTagCompound delayedInitNbt;

    public ScoreboardSaveData()
    {
        this("scoreboard");
    }

    public ScoreboardSaveData(String name)
    {
        super(name);
    }

    public void setScoreboard(Scoreboard scoreboardIn)
    {
        this.theScoreboard = scoreboardIn;

        if (this.delayedInitNbt != null)
        {
            this.readFromNBT(this.delayedInitNbt);
        }
    }

    /**
     * reads in data from the NBTTagCompound into this MapDataBase
     */
    @Override
	public void readFromNBT(NBTTagCompound nbt)
    {
        if (this.theScoreboard == null)
        {
            this.delayedInitNbt = nbt;
        }
        else
        {
            this.readObjectives(nbt.getTagList("Objectives", 10));
            this.readScores(nbt.getTagList("PlayerScores", 10));

            if (nbt.hasKey("DisplaySlots", 10))
            {
                this.readDisplayConfig(nbt.getCompoundTag("DisplaySlots"));
            }

            if (nbt.hasKey("Teams", 9))
            {
                this.readTeams(nbt.getTagList("Teams", 10));
            }
        }
    }

    protected void readTeams(NBTTagList p_96498_1_)
    {
        for (int var2 = 0; var2 < p_96498_1_.tagCount(); ++var2)
        {
            NBTTagCompound var3 = p_96498_1_.getCompoundTagAt(var2);
            ScorePlayerTeam var4 = this.theScoreboard.createTeam(var3.getString("Name"));
            var4.setTeamName(var3.getString("DisplayName"));

            if (var3.hasKey("TeamColor", 8))
            {
                var4.setChatFormat(EnumChatFormatting.getValueByName(var3.getString("TeamColor")));
            }

            var4.setNamePrefix(var3.getString("Prefix"));
            var4.setNameSuffix(var3.getString("Suffix"));

            if (var3.hasKey("AllowFriendlyFire", 99))
            {
                var4.setAllowFriendlyFire(var3.getBoolean("AllowFriendlyFire"));
            }

            if (var3.hasKey("SeeFriendlyInvisibles", 99))
            {
                var4.setSeeFriendlyInvisiblesEnabled(var3.getBoolean("SeeFriendlyInvisibles"));
            }

            Team.EnumVisible var5;

            if (var3.hasKey("NameTagVisibility", 8))
            {
                var5 = Team.EnumVisible.func_178824_a(var3.getString("NameTagVisibility"));

                if (var5 != null)
                {
                    var4.func_178772_a(var5);
                }
            }

            if (var3.hasKey("DeathMessageVisibility", 8))
            {
                var5 = Team.EnumVisible.func_178824_a(var3.getString("DeathMessageVisibility"));

                if (var5 != null)
                {
                    var4.func_178773_b(var5);
                }
            }

            this.func_96502_a(var4, var3.getTagList("Players", 8));
        }
    }

    protected void func_96502_a(ScorePlayerTeam p_96502_1_, NBTTagList p_96502_2_)
    {
        for (int var3 = 0; var3 < p_96502_2_.tagCount(); ++var3)
        {
            this.theScoreboard.addPlayerToTeam(p_96502_2_.getStringTagAt(var3), p_96502_1_.getRegisteredName());
        }
    }

    protected void readDisplayConfig(NBTTagCompound p_96504_1_)
    {
        for (int var2 = 0; var2 < 19; ++var2)
        {
            if (p_96504_1_.hasKey("slot_" + var2, 8))
            {
                String var3 = p_96504_1_.getString("slot_" + var2);
                ScoreObjective var4 = this.theScoreboard.getObjective(var3);
                this.theScoreboard.setObjectiveInDisplaySlot(var2, var4);
            }
        }
    }

    protected void readObjectives(NBTTagList nbt)
    {
        for (int var2 = 0; var2 < nbt.tagCount(); ++var2)
        {
            NBTTagCompound var3 = nbt.getCompoundTagAt(var2);
            IScoreObjectiveCriteria var4 = (IScoreObjectiveCriteria)IScoreObjectiveCriteria.INSTANCES.get(var3.getString("CriteriaName"));

            if (var4 != null)
            {
                ScoreObjective var5 = this.theScoreboard.addScoreObjective(var3.getString("Name"), var4);
                var5.setDisplayName(var3.getString("DisplayName"));
                var5.setRenderType(IScoreObjectiveCriteria.EnumRenderType.func_178795_a(var3.getString("RenderType")));
            }
        }
    }

    protected void readScores(NBTTagList nbt)
    {
        for (int var2 = 0; var2 < nbt.tagCount(); ++var2)
        {
            NBTTagCompound var3 = nbt.getCompoundTagAt(var2);
            ScoreObjective var4 = this.theScoreboard.getObjective(var3.getString("Objective"));
            Score var5 = this.theScoreboard.getValueFromObjective(var3.getString("Name"), var4);
            var5.setScorePoints(var3.getInteger("Score"));

            if (var3.hasKey("Locked"))
            {
                var5.setLocked(var3.getBoolean("Locked"));
            }
        }
    }

    /**
     * write data to NBTTagCompound from this MapDataBase, similar to Entities and TileEntities
     */
    @Override
	public void writeToNBT(NBTTagCompound nbt)
    {
        if (this.theScoreboard == null)
        {
            logger.warn("Tried to save scoreboard without having a scoreboard...");
        }
        else
        {
            nbt.setTag("Objectives", this.objectivesToNbt());
            nbt.setTag("PlayerScores", this.scoresToNbt());
            nbt.setTag("Teams", this.func_96496_a());
            this.func_96497_d(nbt);
        }
    }

    protected NBTTagList func_96496_a()
    {
        NBTTagList var1 = new NBTTagList();
        Collection var2 = this.theScoreboard.getTeams();
        Iterator var3 = var2.iterator();

        while (var3.hasNext())
        {
            ScorePlayerTeam var4 = (ScorePlayerTeam)var3.next();
            NBTTagCompound var5 = new NBTTagCompound();
            var5.setString("Name", var4.getRegisteredName());
            var5.setString("DisplayName", var4.func_96669_c());

            if (var4.getChatFormat().getColorIndex() >= 0)
            {
                var5.setString("TeamColor", var4.getChatFormat().getFriendlyName());
            }

            var5.setString("Prefix", var4.getColorPrefix());
            var5.setString("Suffix", var4.getColorSuffix());
            var5.setBoolean("AllowFriendlyFire", var4.getAllowFriendlyFire());
            var5.setBoolean("SeeFriendlyInvisibles", var4.func_98297_h());
            var5.setString("NameTagVisibility", var4.func_178770_i().field_178830_e);
            var5.setString("DeathMessageVisibility", var4.func_178771_j().field_178830_e);
            NBTTagList var6 = new NBTTagList();
            Iterator var7 = var4.getMembershipCollection().iterator();

            while (var7.hasNext())
            {
                String var8 = (String)var7.next();
                var6.appendTag(new NBTTagString(var8));
            }

            var5.setTag("Players", var6);
            var1.appendTag(var5);
        }

        return var1;
    }

    protected void func_96497_d(NBTTagCompound p_96497_1_)
    {
        NBTTagCompound var2 = new NBTTagCompound();
        boolean var3 = false;

        for (int var4 = 0; var4 < 19; ++var4)
        {
            ScoreObjective var5 = this.theScoreboard.getObjectiveInDisplaySlot(var4);

            if (var5 != null)
            {
                var2.setString("slot_" + var4, var5.getName());
                var3 = true;
            }
        }

        if (var3)
        {
            p_96497_1_.setTag("DisplaySlots", var2);
        }
    }

    protected NBTTagList objectivesToNbt()
    {
        NBTTagList var1 = new NBTTagList();
        Collection var2 = this.theScoreboard.getScoreObjectives();
        Iterator var3 = var2.iterator();

        while (var3.hasNext())
        {
            ScoreObjective var4 = (ScoreObjective)var3.next();

            if (var4.getCriteria() != null)
            {
                NBTTagCompound var5 = new NBTTagCompound();
                var5.setString("Name", var4.getName());
                var5.setString("CriteriaName", var4.getCriteria().getName());
                var5.setString("DisplayName", var4.getDisplayName());
                var5.setString("RenderType", var4.getRenderType().func_178796_a());
                var1.appendTag(var5);
            }
        }

        return var1;
    }

    protected NBTTagList scoresToNbt()
    {
        NBTTagList var1 = new NBTTagList();
        Collection var2 = this.theScoreboard.getScores();
        Iterator var3 = var2.iterator();

        while (var3.hasNext())
        {
            Score var4 = (Score)var3.next();

            if (var4.getObjective() != null)
            {
                NBTTagCompound var5 = new NBTTagCompound();
                var5.setString("Name", var4.getPlayerName());
                var5.setString("Objective", var4.getObjective().getName());
                var5.setInteger("Score", var4.getScorePoints());
                var5.setBoolean("Locked", var4.isLocked());
                var1.appendTag(var5);
            }
        }

        return var1;
    }
}
