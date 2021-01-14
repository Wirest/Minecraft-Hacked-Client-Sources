package net.minecraft.scoreboard;

import java.util.List;

public class ScoreDummyCriteria implements IScoreObjectiveCriteria {
    private final String field_96644_g;
    private static final String __OBFID = "CL_00000622";

    public ScoreDummyCriteria(String p_i2311_1_) {
        field_96644_g = p_i2311_1_;
        IScoreObjectiveCriteria.INSTANCES.put(p_i2311_1_, this);
    }

    @Override
    public String getName() {
        return field_96644_g;
    }

    @Override
    public int func_96635_a(List p_96635_1_) {
        return 0;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public IScoreObjectiveCriteria.EnumRenderType func_178790_c() {
        return IScoreObjectiveCriteria.EnumRenderType.INTEGER;
    }
}
