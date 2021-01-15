package net.minecraft.world;

import net.minecraft.util.MathHelper;

public class DifficultyInstance
{
    private final EnumDifficulty field_180172_a;
    private final float field_180171_b;
    private static final String __OBFID = "CL_00002261";

    public DifficultyInstance(EnumDifficulty p_i45904_1_, long p_i45904_2_, long p_i45904_4_, float p_i45904_6_)
    {
        this.field_180172_a = p_i45904_1_;
        this.field_180171_b = this.func_180169_a(p_i45904_1_, p_i45904_2_, p_i45904_4_, p_i45904_6_);
    }

    public float func_180168_b()
    {
        return this.field_180171_b;
    }

    public float func_180170_c()
    {
        return this.field_180171_b < 2.0F ? 0.0F : (this.field_180171_b > 4.0F ? 1.0F : (this.field_180171_b - 2.0F) / 2.0F);
    }

    private float func_180169_a(EnumDifficulty p_180169_1_, long p_180169_2_, long p_180169_4_, float p_180169_6_)
    {
        if (p_180169_1_ == EnumDifficulty.PEACEFUL)
        {
            return 0.0F;
        }
        else
        {
            boolean var7 = p_180169_1_ == EnumDifficulty.HARD;
            float var8 = 0.75F;
            float var9 = MathHelper.clamp_float(((float)p_180169_2_ + -72000.0F) / 1440000.0F, 0.0F, 1.0F) * 0.25F;
            var8 += var9;
            float var10 = 0.0F;
            var10 += MathHelper.clamp_float((float)p_180169_4_ / 3600000.0F, 0.0F, 1.0F) * (var7 ? 1.0F : 0.75F);
            var10 += MathHelper.clamp_float(p_180169_6_ * 0.25F, 0.0F, var9);

            if (p_180169_1_ == EnumDifficulty.EASY)
            {
                var10 *= 0.5F;
            }

            var8 += var10;
            return (float)p_180169_1_.getDifficultyId() * var8;
        }
    }
}
