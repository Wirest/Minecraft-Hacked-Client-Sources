package optifine;

import net.minecraft.world.World;

public class LightMap
{
    private CustomColormap lightMapRgb = null;
    private float[][] sunRgbs = new float[16][3];
    private float[][] torchRgbs = new float[16][3];

    public LightMap(CustomColormap p_i69_1_)
    {
        this.lightMapRgb = p_i69_1_;
    }

    public CustomColormap getColormap()
    {
        return this.lightMapRgb;
    }

    public boolean updateLightmap(World p_updateLightmap_1_, float p_updateLightmap_2_, int[] p_updateLightmap_3_, boolean p_updateLightmap_4_)
    {
        if (this.lightMapRgb == null)
        {
            return false;
        }
        else
        {
            int i = this.lightMapRgb.getHeight();

            if (p_updateLightmap_4_ && i < 64)
            {
                return false;
            }
            else
            {
                int j = this.lightMapRgb.getWidth();

                if (j < 16)
                {
                    warn("Invalid lightmap width: " + j);
                    this.lightMapRgb = null;
                    return false;
                }
                else
                {
                    int k = 0;

                    if (p_updateLightmap_4_)
                    {
                        k = j * 16 * 2;
                    }

                    float f = 1.1666666F * (p_updateLightmap_1_.getSunBrightness(1.0F) - 0.2F);

                    if (p_updateLightmap_1_.getLastLightningBolt() > 0)
                    {
                        f = 1.0F;
                    }

                    f = Config.limitTo1(f);
                    float f1 = f * (float)(j - 1);
                    float f2 = Config.limitTo1(p_updateLightmap_2_ + 0.5F) * (float)(j - 1);
                    float f3 = Config.limitTo1(Config.getGameSettings().gammaSetting);
                    boolean flag = f3 > 1.0E-4F;
                    float[][] afloat = this.lightMapRgb.getColorsRgb();
                    this.getLightMapColumn(afloat, f1, k, j, this.sunRgbs);
                    this.getLightMapColumn(afloat, f2, k + 16 * j, j, this.torchRgbs);
                    float[] afloat1 = new float[3];

                    for (int l = 0; l < 16; ++l)
                    {
                        for (int i1 = 0; i1 < 16; ++i1)
                        {
                            for (int j1 = 0; j1 < 3; ++j1)
                            {
                                float f4 = Config.limitTo1(this.sunRgbs[l][j1] + this.torchRgbs[i1][j1]);

                                if (flag)
                                {
                                    float f5 = 1.0F - f4;
                                    f5 = 1.0F - f5 * f5 * f5 * f5;
                                    f4 = f3 * f5 + (1.0F - f3) * f4;
                                }

                                afloat1[j1] = f4;
                            }

                            int k1 = (int)(afloat1[0] * 255.0F);
                            int l1 = (int)(afloat1[1] * 255.0F);
                            int i2 = (int)(afloat1[2] * 255.0F);
                            p_updateLightmap_3_[l * 16 + i1] = -16777216 | k1 << 16 | l1 << 8 | i2;
                        }
                    }

                    return true;
                }
            }
        }
    }

    private void getLightMapColumn(float[][] p_getLightMapColumn_1_, float p_getLightMapColumn_2_, int p_getLightMapColumn_3_, int p_getLightMapColumn_4_, float[][] p_getLightMapColumn_5_)
    {
        int i = (int)Math.floor((double)p_getLightMapColumn_2_);
        int j = (int)Math.ceil((double)p_getLightMapColumn_2_);

        if (i == j)
        {
            for (int i1 = 0; i1 < 16; ++i1)
            {
                float[] afloat3 = p_getLightMapColumn_1_[p_getLightMapColumn_3_ + i1 * p_getLightMapColumn_4_ + i];
                float[] afloat4 = p_getLightMapColumn_5_[i1];

                for (int j1 = 0; j1 < 3; ++j1)
                {
                    afloat4[j1] = afloat3[j1];
                }
            }
        }
        else
        {
            float f = 1.0F - (p_getLightMapColumn_2_ - (float)i);
            float f1 = 1.0F - ((float)j - p_getLightMapColumn_2_);

            for (int k = 0; k < 16; ++k)
            {
                float[] afloat = p_getLightMapColumn_1_[p_getLightMapColumn_3_ + k * p_getLightMapColumn_4_ + i];
                float[] afloat1 = p_getLightMapColumn_1_[p_getLightMapColumn_3_ + k * p_getLightMapColumn_4_ + j];
                float[] afloat2 = p_getLightMapColumn_5_[k];

                for (int l = 0; l < 3; ++l)
                {
                    afloat2[l] = afloat[l] * f + afloat1[l] * f1;
                }
            }
        }
    }

    private static void dbg(String p_dbg_0_)
    {
        Config.dbg("CustomColors: " + p_dbg_0_);
    }

    private static void warn(String p_warn_0_)
    {
        Config.warn("CustomColors: " + p_warn_0_);
    }
}
