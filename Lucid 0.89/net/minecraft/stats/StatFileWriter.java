package net.minecraft.stats;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.TupleIntJsonSerializable;

public class StatFileWriter
{
    protected final Map statsData = Maps.newConcurrentMap();

    /**
     * Returns true if the achievement has been unlocked.
     */
    public boolean hasAchievementUnlocked(Achievement achievementIn)
    {
        return this.readStat(achievementIn) > 0;
    }

    /**
     * Returns true if the parent has been unlocked, or there is no parent
     */
    public boolean canUnlockAchievement(Achievement achievementIn)
    {
        return achievementIn.parentAchievement == null || this.hasAchievementUnlocked(achievementIn.parentAchievement);
    }

    public int func_150874_c(Achievement p_150874_1_)
    {
        if (this.hasAchievementUnlocked(p_150874_1_))
        {
            return 0;
        }
        else
        {
            int var2 = 0;

            for (Achievement var3 = p_150874_1_.parentAchievement; var3 != null && !this.hasAchievementUnlocked(var3); ++var2)
            {
                var3 = var3.parentAchievement;
            }

            return var2;
        }
    }

    public void func_150871_b(EntityPlayer p_150871_1_, StatBase p_150871_2_, int p_150871_3_)
    {
        if (!p_150871_2_.isAchievement() || this.canUnlockAchievement((Achievement)p_150871_2_))
        {
            this.func_150873_a(p_150871_1_, p_150871_2_, this.readStat(p_150871_2_) + p_150871_3_);
        }
    }

    public void func_150873_a(EntityPlayer playerIn, StatBase statIn, int p_150873_3_)
    {
        TupleIntJsonSerializable var4 = (TupleIntJsonSerializable)this.statsData.get(statIn);

        if (var4 == null)
        {
            var4 = new TupleIntJsonSerializable();
            this.statsData.put(statIn, var4);
        }

        var4.setIntegerValue(p_150873_3_);
    }

    /**
     * Reads the given stat and returns its value as an int.
     *  
     * @param stat The StatBase object to lookup
     */
    public int readStat(StatBase stat)
    {
        TupleIntJsonSerializable var2 = (TupleIntJsonSerializable)this.statsData.get(stat);
        return var2 == null ? 0 : var2.getIntegerValue();
    }

    public IJsonSerializable func_150870_b(StatBase p_150870_1_)
    {
        TupleIntJsonSerializable var2 = (TupleIntJsonSerializable)this.statsData.get(p_150870_1_);
        return var2 != null ? var2.getJsonSerializableValue() : null;
    }

    public IJsonSerializable func_150872_a(StatBase p_150872_1_, IJsonSerializable p_150872_2_)
    {
        TupleIntJsonSerializable var3 = (TupleIntJsonSerializable)this.statsData.get(p_150872_1_);

        if (var3 == null)
        {
            var3 = new TupleIntJsonSerializable();
            this.statsData.put(p_150872_1_, var3);
        }

        var3.setJsonSerializableValue(p_150872_2_);
        return p_150872_2_;
    }
}
