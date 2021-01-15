package net.minecraft.scoreboard;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.util.EnumChatFormatting;

public class ScorePlayerTeam extends Team
{
    private final Scoreboard theScoreboard;
    private final String registeredName;

    /** A set of all team member usernames. */
    private final Set membershipSet = Sets.newHashSet();
    private String teamNameSPT;
    private String namePrefixSPT = "";
    private String colorSuffix = "";
    private boolean allowFriendlyFire = true;
    private boolean canSeeFriendlyInvisibles = true;
    private Team.EnumVisible field_178778_i;
    private Team.EnumVisible field_178776_j;
    private EnumChatFormatting chatFormat;

    public ScorePlayerTeam(Scoreboard theScoreboardIn, String name)
    {
        this.field_178778_i = Team.EnumVisible.ALWAYS;
        this.field_178776_j = Team.EnumVisible.ALWAYS;
        this.chatFormat = EnumChatFormatting.RESET;
        this.theScoreboard = theScoreboardIn;
        this.registeredName = name;
        this.teamNameSPT = name;
    }

    /**
     * Retrieve the name by which this team is registered in the scoreboard
     */
    @Override
	public String getRegisteredName()
    {
        return this.registeredName;
    }

    public String func_96669_c()
    {
        return this.teamNameSPT;
    }

    public void setTeamName(String name)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Name cannot be null");
        }
        else
        {
            this.teamNameSPT = name;
            this.theScoreboard.sendTeamUpdate(this);
        }
    }

    @Override
	public Collection getMembershipCollection()
    {
        return this.membershipSet;
    }

    /**
     * Returns the color prefix for the player's team name
     */
    public String getColorPrefix()
    {
        return this.namePrefixSPT;
    }

    public void setNamePrefix(String prefix)
    {
        if (prefix == null)
        {
            throw new IllegalArgumentException("Prefix cannot be null");
        }
        else
        {
            this.namePrefixSPT = prefix;
            this.theScoreboard.sendTeamUpdate(this);
        }
    }

    /**
     * Returns the color suffix for the player's team name
     */
    public String getColorSuffix()
    {
        return this.colorSuffix;
    }

    public void setNameSuffix(String suffix)
    {
        this.colorSuffix = suffix;
        this.theScoreboard.sendTeamUpdate(this);
    }

    @Override
	public String formatString(String input)
    {
        return this.getColorPrefix() + input + this.getColorSuffix();
    }

    /**
     * Returns the player name including the color prefixes and suffixes
     */
    public static String formatPlayerName(Team p_96667_0_, String p_96667_1_)
    {
        return p_96667_0_ == null ? p_96667_1_ : p_96667_0_.formatString(p_96667_1_);
    }

    @Override
	public boolean getAllowFriendlyFire()
    {
        return this.allowFriendlyFire;
    }

    public void setAllowFriendlyFire(boolean friendlyFire)
    {
        this.allowFriendlyFire = friendlyFire;
        this.theScoreboard.sendTeamUpdate(this);
    }

    @Override
	public boolean func_98297_h()
    {
        return this.canSeeFriendlyInvisibles;
    }

    public void setSeeFriendlyInvisiblesEnabled(boolean friendlyInvisibles)
    {
        this.canSeeFriendlyInvisibles = friendlyInvisibles;
        this.theScoreboard.sendTeamUpdate(this);
    }

    @Override
	public Team.EnumVisible func_178770_i()
    {
        return this.field_178778_i;
    }

    @Override
	public Team.EnumVisible func_178771_j()
    {
        return this.field_178776_j;
    }

    public void func_178772_a(Team.EnumVisible p_178772_1_)
    {
        this.field_178778_i = p_178772_1_;
        this.theScoreboard.sendTeamUpdate(this);
    }

    public void func_178773_b(Team.EnumVisible p_178773_1_)
    {
        this.field_178776_j = p_178773_1_;
        this.theScoreboard.sendTeamUpdate(this);
    }

    public int func_98299_i()
    {
        int var1 = 0;

        if (this.getAllowFriendlyFire())
        {
            var1 |= 1;
        }

        if (this.func_98297_h())
        {
            var1 |= 2;
        }

        return var1;
    }

    public void func_98298_a(int p_98298_1_)
    {
        this.setAllowFriendlyFire((p_98298_1_ & 1) > 0);
        this.setSeeFriendlyInvisiblesEnabled((p_98298_1_ & 2) > 0);
    }

    public void setChatFormat(EnumChatFormatting p_178774_1_)
    {
        this.chatFormat = p_178774_1_;
    }

    public EnumChatFormatting getChatFormat()
    {
        return this.chatFormat;
    }
}
