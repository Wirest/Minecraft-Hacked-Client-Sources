package optifine;

public class SmoothFloat
{
    private float valueLast;
    private final float timeFadeUpSec;
    private final float timeFadeDownSec;
    private long timeLastMs;

    public SmoothFloat(float p_i108_1_, float p_i108_2_)
    {
        this(p_i108_1_, p_i108_2_, p_i108_2_);
    }

    public SmoothFloat(float p_i109_1_, float p_i109_2_, float p_i109_3_)
    {
        this.valueLast = p_i109_1_;
        this.timeFadeUpSec = p_i109_2_;
        this.timeFadeDownSec = p_i109_3_;
        this.timeLastMs = System.currentTimeMillis();
    }

    public float getValueLast()
    {
        return this.valueLast;
    }

    public float getTimeFadeUpSec()
    {
        return this.timeFadeUpSec;
    }

    public float getTimeFadeDownSec()
    {
        return this.timeFadeDownSec;
    }

    public long getTimeLastMs()
    {
        return this.timeLastMs;
    }

    public float getSmoothValue(float p_getSmoothValue_1_)
    {
        long i = System.currentTimeMillis();
        float f = this.valueLast;
        long j = this.timeLastMs;
        float f1 = (float)(i - j) / 1000.0F;
        float f2 = p_getSmoothValue_1_ >= f ? this.timeFadeUpSec : this.timeFadeDownSec;
        float f3 = getSmoothValue(f, p_getSmoothValue_1_, f1, f2);
        this.valueLast = f3;
        this.timeLastMs = i;
        return f3;
    }

    public static float getSmoothValue(float p_getSmoothValue_0_, float p_getSmoothValue_1_, float p_getSmoothValue_2_, float p_getSmoothValue_3_)
    {
        if (p_getSmoothValue_2_ <= 0.0F)
        {
            return p_getSmoothValue_0_;
        }
        else
        {
            float f = p_getSmoothValue_1_ - p_getSmoothValue_0_;
            float f1;

            if (p_getSmoothValue_3_ > 0.0F && p_getSmoothValue_2_ < p_getSmoothValue_3_ && Math.abs(f) > 1.0E-6F)
            {
                float f2 = p_getSmoothValue_3_ / p_getSmoothValue_2_;
                float f3 = 4.61F;
                float f4 = 0.13F;
                float f5 = 10.0F;
                float f6 = f3 - 1.0F / (f4 + f2 / f5);
                float f7 = p_getSmoothValue_2_ / p_getSmoothValue_3_ * f6;
                f7 = NumUtils.limit(f7, 0.0F, 1.0F);
                f1 = p_getSmoothValue_0_ + f * f7;
            }
            else
            {
                f1 = p_getSmoothValue_1_;
            }

            return f1;
        }
    }
}
