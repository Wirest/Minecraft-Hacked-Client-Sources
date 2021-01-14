package net.minecraft.scoreboard;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class ScoreHealthCriteria extends ScoreDummyCriteria
{
    public ScoreHealthCriteria(String name)
    {
        super(name);
    }

    @Override
	public int func_96635_a(List<EntityPlayer> p_96635_1_)
    {
        float f = 0.0F;

        for (EntityPlayer entityplayer : p_96635_1_)
        {
            f += entityplayer.getHealth() + entityplayer.getAbsorptionAmount();
        }

        if (p_96635_1_.size() > 0)
        {
            f /= p_96635_1_.size();
        }

        return MathHelper.ceiling_float_int(f);
    }

    @Override
	public boolean isReadOnly()
    {
        return true;
    }

    @Override
	public IScoreObjectiveCriteria.EnumRenderType getRenderType()
    {
        return IScoreObjectiveCriteria.EnumRenderType.HEARTS;
    }
}
