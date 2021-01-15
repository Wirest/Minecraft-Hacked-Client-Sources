package net.minecraft.world;

import net.minecraft.util.MathHelper;

public class DifficultyInstance
{
    private final float additionalDifficulty;

    public DifficultyInstance(EnumDifficulty worldDifficulty, long worldTime, long chunkInhabitedTime, float moonPhaseFactor)
    {
        this.additionalDifficulty = this.calculateAdditionalDifficulty(worldDifficulty, worldTime, chunkInhabitedTime, moonPhaseFactor);
    }

    public float getAdditionalDifficulty()
    {
        return this.additionalDifficulty;
    }

    public float getClampedAdditionalDifficulty()
    {
        return this.additionalDifficulty < 2.0F ? 0.0F : (this.additionalDifficulty > 4.0F ? 1.0F : (this.additionalDifficulty - 2.0F) / 2.0F);
    }

    private float calculateAdditionalDifficulty(EnumDifficulty difficulty, long worldTime, long chunkInhabitedTime, float moonPhaseFactor)
    {
        if (difficulty == EnumDifficulty.PEACEFUL)
        {
            return 0.0F;
        }
        else
        {
            boolean var7 = difficulty == EnumDifficulty.HARD;
            float var8 = 0.75F;
            float var9 = MathHelper.clamp_float((worldTime + -72000.0F) / 1440000.0F, 0.0F, 1.0F) * 0.25F;
            var8 += var9;
            float var10 = 0.0F;
            var10 += MathHelper.clamp_float(chunkInhabitedTime / 3600000.0F, 0.0F, 1.0F) * (var7 ? 1.0F : 0.75F);
            var10 += MathHelper.clamp_float(moonPhaseFactor * 0.25F, 0.0F, var9);

            if (difficulty == EnumDifficulty.EASY)
            {
                var10 *= 0.5F;
            }

            var8 += var10;
            return difficulty.getDifficultyId() * var8;
        }
    }
}
