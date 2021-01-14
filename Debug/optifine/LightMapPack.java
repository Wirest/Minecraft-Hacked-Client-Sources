package optifine;

import net.minecraft.world.World;

public class LightMapPack
{
    private LightMap lightMap;
    private LightMap lightMapRain;
    private LightMap lightMapThunder;
    private int[] colorBuffer1 = new int[0];
    private int[] colorBuffer2 = new int[0];

    public LightMapPack(LightMap p_i70_1_, LightMap p_i70_2_, LightMap p_i70_3_)
    {
        if (p_i70_2_ != null || p_i70_3_ != null)
        {
            if (p_i70_2_ == null)
            {
                p_i70_2_ = p_i70_1_;
            }

            if (p_i70_3_ == null)
            {
                p_i70_3_ = p_i70_2_;
            }
        }

        this.lightMap = p_i70_1_;
        this.lightMapRain = p_i70_2_;
        this.lightMapThunder = p_i70_3_;
    }

    public boolean updateLightmap(World p_updateLightmap_1_, float p_updateLightmap_2_, int[] p_updateLightmap_3_, boolean p_updateLightmap_4_, float p_updateLightmap_5_)
    {
        if (this.lightMapRain == null && this.lightMapThunder == null)
        {
            return this.lightMap.updateLightmap(p_updateLightmap_1_, p_updateLightmap_2_, p_updateLightmap_3_, p_updateLightmap_4_);
        }
        else
        {
            int i = p_updateLightmap_1_.provider.getDimensionId();

            if (i != 1 && i != -1)
            {
                float f = p_updateLightmap_1_.getRainStrength(p_updateLightmap_5_);
                float f1 = p_updateLightmap_1_.getThunderStrength(p_updateLightmap_5_);
                float f2 = 1.0E-4F;
                boolean flag = f > f2;
                boolean flag1 = f1 > f2;

                if (!flag && !flag1)
                {
                    return this.lightMap.updateLightmap(p_updateLightmap_1_, p_updateLightmap_2_, p_updateLightmap_3_, p_updateLightmap_4_);
                }
                else
                {
                    if (f > 0.0F)
                    {
                        f1 /= f;
                    }

                    float f3 = 1.0F - f;
                    float f4 = f - f1;

                    if (this.colorBuffer1.length != p_updateLightmap_3_.length)
                    {
                        this.colorBuffer1 = new int[p_updateLightmap_3_.length];
                        this.colorBuffer2 = new int[p_updateLightmap_3_.length];
                    }

                    int j = 0;
                    int[][] aint = new int[][] {p_updateLightmap_3_, this.colorBuffer1, this.colorBuffer2};
                    float[] afloat = new float[3];

                    if (f3 > f2 && this.lightMap.updateLightmap(p_updateLightmap_1_, p_updateLightmap_2_, aint[j], p_updateLightmap_4_))
                    {
                        afloat[j] = f3;
                        ++j;
                    }

                    if (f4 > f2 && this.lightMapRain != null && this.lightMapRain.updateLightmap(p_updateLightmap_1_, p_updateLightmap_2_, aint[j], p_updateLightmap_4_))
                    {
                        afloat[j] = f4;
                        ++j;
                    }

                    if (f1 > f2 && this.lightMapThunder != null && this.lightMapThunder.updateLightmap(p_updateLightmap_1_, p_updateLightmap_2_, aint[j], p_updateLightmap_4_))
                    {
                        afloat[j] = f1;
                        ++j;
                    }

                    return j == 2 ? this.blend(aint[0], afloat[0], aint[1], afloat[1]) : (j == 3 ? this.blend(aint[0], afloat[0], aint[1], afloat[1], aint[2], afloat[2]) : true);
                }
            }
            else
            {
                return this.lightMap.updateLightmap(p_updateLightmap_1_, p_updateLightmap_2_, p_updateLightmap_3_, p_updateLightmap_4_);
            }
        }
    }

    private boolean blend(int[] p_blend_1_, float p_blend_2_, int[] p_blend_3_, float p_blend_4_)
    {
        if (p_blend_3_.length != p_blend_1_.length)
        {
            return false;
        }
        else
        {
            for (int i = 0; i < p_blend_1_.length; ++i)
            {
                int j = p_blend_1_[i];
                int k = j >> 16 & 255;
                int l = j >> 8 & 255;
                int i1 = j & 255;
                int j1 = p_blend_3_[i];
                int k1 = j1 >> 16 & 255;
                int l1 = j1 >> 8 & 255;
                int i2 = j1 & 255;
                int j2 = (int)((float)k * p_blend_2_ + (float)k1 * p_blend_4_);
                int k2 = (int)((float)l * p_blend_2_ + (float)l1 * p_blend_4_);
                int l2 = (int)((float)i1 * p_blend_2_ + (float)i2 * p_blend_4_);
                p_blend_1_[i] = -16777216 | j2 << 16 | k2 << 8 | l2;
            }

            return true;
        }
    }

    private boolean blend(int[] p_blend_1_, float p_blend_2_, int[] p_blend_3_, float p_blend_4_, int[] p_blend_5_, float p_blend_6_)
    {
        if (p_blend_3_.length == p_blend_1_.length && p_blend_5_.length == p_blend_1_.length)
        {
            for (int i = 0; i < p_blend_1_.length; ++i)
            {
                int j = p_blend_1_[i];
                int k = j >> 16 & 255;
                int l = j >> 8 & 255;
                int i1 = j & 255;
                int j1 = p_blend_3_[i];
                int k1 = j1 >> 16 & 255;
                int l1 = j1 >> 8 & 255;
                int i2 = j1 & 255;
                int j2 = p_blend_5_[i];
                int k2 = j2 >> 16 & 255;
                int l2 = j2 >> 8 & 255;
                int i3 = j2 & 255;
                int j3 = (int)((float)k * p_blend_2_ + (float)k1 * p_blend_4_ + (float)k2 * p_blend_6_);
                int k3 = (int)((float)l * p_blend_2_ + (float)l1 * p_blend_4_ + (float)l2 * p_blend_6_);
                int l3 = (int)((float)i1 * p_blend_2_ + (float)i2 * p_blend_4_ + (float)i3 * p_blend_6_);
                p_blend_1_[i] = -16777216 | j3 << 16 | k3 << 8 | l3;
            }

            return true;
        }
        else
        {
            return false;
        }
    }
}
