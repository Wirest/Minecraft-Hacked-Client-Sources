// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.stats;

import net.minecraft.util.ChatComponentText;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.EnumChatFormatting;
import java.util.Locale;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.util.IChatComponent;

public class StatBase
{
    public final String statId;
    private final IChatComponent statName;
    public boolean isIndependent;
    private final IStatType type;
    private final IScoreObjectiveCriteria field_150957_c;
    private Class<? extends IJsonSerializable> field_150956_d;
    private static NumberFormat numberFormat;
    public static IStatType simpleStatType;
    private static DecimalFormat decimalFormat;
    public static IStatType timeStatType;
    public static IStatType distanceStatType;
    public static IStatType field_111202_k;
    
    static {
        StatBase.numberFormat = NumberFormat.getIntegerInstance(Locale.US);
        StatBase.simpleStatType = new IStatType() {
            @Override
            public String format(final int p_75843_1_) {
                return StatBase.numberFormat.format(p_75843_1_);
            }
        };
        StatBase.decimalFormat = new DecimalFormat("########0.00");
        StatBase.timeStatType = new IStatType() {
            @Override
            public String format(final int p_75843_1_) {
                final double d0 = p_75843_1_ / 20.0;
                final double d2 = d0 / 60.0;
                final double d3 = d2 / 60.0;
                final double d4 = d3 / 24.0;
                final double d5 = d4 / 365.0;
                return (d5 > 0.5) ? (String.valueOf(StatBase.decimalFormat.format(d5)) + " y") : ((d4 > 0.5) ? (String.valueOf(StatBase.decimalFormat.format(d4)) + " d") : ((d3 > 0.5) ? (String.valueOf(StatBase.decimalFormat.format(d3)) + " h") : ((d2 > 0.5) ? (String.valueOf(StatBase.decimalFormat.format(d2)) + " m") : (String.valueOf(d0) + " s"))));
            }
        };
        StatBase.distanceStatType = new IStatType() {
            @Override
            public String format(final int p_75843_1_) {
                final double d0 = p_75843_1_ / 100.0;
                final double d2 = d0 / 1000.0;
                return (d2 > 0.5) ? (String.valueOf(StatBase.decimalFormat.format(d2)) + " km") : ((d0 > 0.5) ? (String.valueOf(StatBase.decimalFormat.format(d0)) + " m") : (String.valueOf(p_75843_1_) + " cm"));
            }
        };
        StatBase.field_111202_k = new IStatType() {
            @Override
            public String format(final int p_75843_1_) {
                return StatBase.decimalFormat.format(p_75843_1_ * 0.1);
            }
        };
    }
    
    public StatBase(final String statIdIn, final IChatComponent statNameIn, final IStatType typeIn) {
        this.statId = statIdIn;
        this.statName = statNameIn;
        this.type = typeIn;
        this.field_150957_c = new ObjectiveStat(this);
        IScoreObjectiveCriteria.INSTANCES.put(this.field_150957_c.getName(), this.field_150957_c);
    }
    
    public StatBase(final String statIdIn, final IChatComponent statNameIn) {
        this(statIdIn, statNameIn, StatBase.simpleStatType);
    }
    
    public StatBase initIndependentStat() {
        this.isIndependent = true;
        return this;
    }
    
    public StatBase registerStat() {
        if (StatList.oneShotStats.containsKey(this.statId)) {
            throw new RuntimeException("Duplicate stat id: \"" + StatList.oneShotStats.get(this.statId).statName + "\" and \"" + this.statName + "\" at id " + this.statId);
        }
        StatList.allStats.add(this);
        StatList.oneShotStats.put(this.statId, this);
        return this;
    }
    
    public boolean isAchievement() {
        return false;
    }
    
    public String format(final int p_75968_1_) {
        return this.type.format(p_75968_1_);
    }
    
    public IChatComponent getStatName() {
        final IChatComponent ichatcomponent = this.statName.createCopy();
        ichatcomponent.getChatStyle().setColor(EnumChatFormatting.GRAY);
        ichatcomponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ACHIEVEMENT, new ChatComponentText(this.statId)));
        return ichatcomponent;
    }
    
    public IChatComponent func_150955_j() {
        final IChatComponent ichatcomponent = this.getStatName();
        final IChatComponent ichatcomponent2 = new ChatComponentText("[").appendSibling(ichatcomponent).appendText("]");
        ichatcomponent2.setChatStyle(ichatcomponent.getChatStyle());
        return ichatcomponent2;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            final StatBase statbase = (StatBase)p_equals_1_;
            return this.statId.equals(statbase.statId);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.statId.hashCode();
    }
    
    @Override
    public String toString() {
        return "Stat{id=" + this.statId + ", nameId=" + this.statName + ", awardLocallyOnly=" + this.isIndependent + ", formatter=" + this.type + ", objectiveCriteria=" + this.field_150957_c + '}';
    }
    
    public IScoreObjectiveCriteria func_150952_k() {
        return this.field_150957_c;
    }
    
    public Class<? extends IJsonSerializable> func_150954_l() {
        return this.field_150956_d;
    }
    
    public StatBase func_150953_b(final Class<? extends IJsonSerializable> p_150953_1_) {
        this.field_150956_d = p_150953_1_;
        return this;
    }
}
