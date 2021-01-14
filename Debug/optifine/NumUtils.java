package optifine;

public class NumUtils
{
    public static float limit(float p_limit_0_, float p_limit_1_, float p_limit_2_)
    {
        return p_limit_0_ < p_limit_1_ ? p_limit_1_ : (p_limit_0_ > p_limit_2_ ? p_limit_2_ : p_limit_0_);
    }
}
