// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.scoreboard;

import java.util.Collection;
import com.google.common.collect.Sets;
import net.minecraft.util.EnumChatFormatting;
import java.util.Set;

public class ScorePlayerTeam extends Team
{
    private final Scoreboard theScoreboard;
    private final String registeredName;
    private final Set<String> membershipSet;
    private String teamNameSPT;
    private String namePrefixSPT;
    private String colorSuffix;
    private boolean allowFriendlyFire;
    private boolean canSeeFriendlyInvisibles;
    private EnumVisible nameTagVisibility;
    private EnumVisible deathMessageVisibility;
    private EnumChatFormatting chatFormat;
    
    public ScorePlayerTeam(final Scoreboard theScoreboardIn, final String name) {
        this.membershipSet = (Set<String>)Sets.newHashSet();
        this.namePrefixSPT = "";
        this.colorSuffix = "";
        this.allowFriendlyFire = true;
        this.canSeeFriendlyInvisibles = true;
        this.nameTagVisibility = EnumVisible.ALWAYS;
        this.deathMessageVisibility = EnumVisible.ALWAYS;
        this.chatFormat = EnumChatFormatting.RESET;
        this.theScoreboard = theScoreboardIn;
        this.registeredName = name;
        this.teamNameSPT = name;
    }
    
    @Override
    public String getRegisteredName() {
        return this.registeredName;
    }
    
    public String getTeamName() {
        return this.teamNameSPT;
    }
    
    public void setTeamName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        this.teamNameSPT = name;
        this.theScoreboard.sendTeamUpdate(this);
    }
    
    @Override
    public Collection<String> getMembershipCollection() {
        return this.membershipSet;
    }
    
    public String getColorPrefix() {
        return this.namePrefixSPT;
    }
    
    public void setNamePrefix(final String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Prefix cannot be null");
        }
        this.namePrefixSPT = prefix;
        this.theScoreboard.sendTeamUpdate(this);
    }
    
    public String getColorSuffix() {
        return this.colorSuffix;
    }
    
    public void setNameSuffix(final String suffix) {
        this.colorSuffix = suffix;
        this.theScoreboard.sendTeamUpdate(this);
    }
    
    @Override
    public String formatString(final String input) {
        return String.valueOf(this.getColorPrefix()) + input + this.getColorSuffix();
    }
    
    public static String formatPlayerName(final Team p_96667_0_, final String p_96667_1_) {
        return (p_96667_0_ == null) ? p_96667_1_ : p_96667_0_.formatString(p_96667_1_);
    }
    
    @Override
    public boolean getAllowFriendlyFire() {
        return this.allowFriendlyFire;
    }
    
    public void setAllowFriendlyFire(final boolean friendlyFire) {
        this.allowFriendlyFire = friendlyFire;
        this.theScoreboard.sendTeamUpdate(this);
    }
    
    @Override
    public boolean getSeeFriendlyInvisiblesEnabled() {
        return this.canSeeFriendlyInvisibles;
    }
    
    public void setSeeFriendlyInvisiblesEnabled(final boolean friendlyInvisibles) {
        this.canSeeFriendlyInvisibles = friendlyInvisibles;
        this.theScoreboard.sendTeamUpdate(this);
    }
    
    @Override
    public EnumVisible getNameTagVisibility() {
        return this.nameTagVisibility;
    }
    
    @Override
    public EnumVisible getDeathMessageVisibility() {
        return this.deathMessageVisibility;
    }
    
    public void setNameTagVisibility(final EnumVisible p_178772_1_) {
        this.nameTagVisibility = p_178772_1_;
        this.theScoreboard.sendTeamUpdate(this);
    }
    
    public void setDeathMessageVisibility(final EnumVisible p_178773_1_) {
        this.deathMessageVisibility = p_178773_1_;
        this.theScoreboard.sendTeamUpdate(this);
    }
    
    public int func_98299_i() {
        int i = 0;
        if (this.getAllowFriendlyFire()) {
            i |= 0x1;
        }
        if (this.getSeeFriendlyInvisiblesEnabled()) {
            i |= 0x2;
        }
        return i;
    }
    
    public void func_98298_a(final int p_98298_1_) {
        this.setAllowFriendlyFire((p_98298_1_ & 0x1) > 0);
        this.setSeeFriendlyInvisiblesEnabled((p_98298_1_ & 0x2) > 0);
    }
    
    public void setChatFormat(final EnumChatFormatting p_178774_1_) {
        this.chatFormat = p_178774_1_;
    }
    
    public EnumChatFormatting getChatFormat() {
        return this.chatFormat;
    }
}
