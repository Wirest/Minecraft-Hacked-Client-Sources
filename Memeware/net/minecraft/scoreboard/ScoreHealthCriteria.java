package net.minecraft.scoreboard;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class ScoreHealthCriteria extends ScoreDummyCriteria {
    private static final String __OBFID = "CL_00000623";

    public ScoreHealthCriteria(String p_i2312_1_) {
        super(p_i2312_1_);
    }

    public int func_96635_a(List p_96635_1_) {
        float var2 = 0.0F;
        EntityPlayer var4;

        for (Iterator var3 = p_96635_1_.iterator(); var3.hasNext(); var2 += var4.getHealth() + var4.getAbsorptionAmount()) {
            var4 = (EntityPlayer) var3.next();
        }

        if (p_96635_1_.size() > 0) {
            var2 /= (float) p_96635_1_.size();
        }

        return MathHelper.ceiling_float_int(var2);
    }

    public boolean isReadOnly() {
        return true;
    }

    public IScoreObjectiveCriteria.EnumRenderType func_178790_c() {
        return IScoreObjectiveCriteria.EnumRenderType.HEARTS;
    }
}
