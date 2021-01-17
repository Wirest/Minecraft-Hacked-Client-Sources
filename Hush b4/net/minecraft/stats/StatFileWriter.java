// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.stats;

import net.minecraft.util.IJsonSerializable;
import net.minecraft.entity.player.EntityPlayer;
import com.google.common.collect.Maps;
import net.minecraft.util.TupleIntJsonSerializable;
import java.util.Map;

public class StatFileWriter
{
    protected final Map<StatBase, TupleIntJsonSerializable> statsData;
    
    public StatFileWriter() {
        this.statsData = (Map<StatBase, TupleIntJsonSerializable>)Maps.newConcurrentMap();
    }
    
    public boolean hasAchievementUnlocked(final Achievement achievementIn) {
        return this.readStat(achievementIn) > 0;
    }
    
    public boolean canUnlockAchievement(final Achievement achievementIn) {
        return achievementIn.parentAchievement == null || this.hasAchievementUnlocked(achievementIn.parentAchievement);
    }
    
    public int func_150874_c(final Achievement p_150874_1_) {
        if (this.hasAchievementUnlocked(p_150874_1_)) {
            return 0;
        }
        int i = 0;
        for (Achievement achievement = p_150874_1_.parentAchievement; achievement != null && !this.hasAchievementUnlocked(achievement); achievement = achievement.parentAchievement, ++i) {}
        return i;
    }
    
    public void increaseStat(final EntityPlayer player, final StatBase stat, final int amount) {
        if (!stat.isAchievement() || this.canUnlockAchievement((Achievement)stat)) {
            this.unlockAchievement(player, stat, this.readStat(stat) + amount);
        }
    }
    
    public void unlockAchievement(final EntityPlayer playerIn, final StatBase statIn, final int p_150873_3_) {
        TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(statIn);
        if (tupleintjsonserializable == null) {
            tupleintjsonserializable = new TupleIntJsonSerializable();
            this.statsData.put(statIn, tupleintjsonserializable);
        }
        tupleintjsonserializable.setIntegerValue(p_150873_3_);
    }
    
    public int readStat(final StatBase stat) {
        final TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(stat);
        return (tupleintjsonserializable == null) ? 0 : tupleintjsonserializable.getIntegerValue();
    }
    
    public <T extends IJsonSerializable> T func_150870_b(final StatBase p_150870_1_) {
        final TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(p_150870_1_);
        return (T)((tupleintjsonserializable != null) ? tupleintjsonserializable.getJsonSerializableValue() : null);
    }
    
    public <T extends IJsonSerializable> T func_150872_a(final StatBase p_150872_1_, final T p_150872_2_) {
        TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(p_150872_1_);
        if (tupleintjsonserializable == null) {
            tupleintjsonserializable = new TupleIntJsonSerializable();
            this.statsData.put(p_150872_1_, tupleintjsonserializable);
        }
        tupleintjsonserializable.setJsonSerializableValue(p_150872_2_);
        return p_150872_2_;
    }
}
