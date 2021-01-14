package net.minecraft.scoreboard;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

public class ScoreDummyCriteria implements IScoreObjectiveCriteria
{
    private final String dummyName;

    public ScoreDummyCriteria(String name)
    {
        this.dummyName = name;
        IScoreObjectiveCriteria.INSTANCES.put(name, this);
    }

    @Override
	public String getName()
    {
        return this.dummyName;
    }

    @Override
	public int func_96635_a(List<EntityPlayer> p_96635_1_)
    {
        return 0;
    }

    @Override
	public boolean isReadOnly()
    {
        return false;
    }

    @Override
	public IScoreObjectiveCriteria.EnumRenderType getRenderType()
    {
        return IScoreObjectiveCriteria.EnumRenderType.INTEGER;
    }
}
