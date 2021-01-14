package optifine;

public class MathUtils
{
    public static int getAverage(int[] p_getAverage_0_)
    {
        if (p_getAverage_0_.length <= 0)
        {
            return 0;
        }
        else
        {
            int i = 0;

            for (int k : p_getAverage_0_) {
                i += k;
            }

            return i / p_getAverage_0_.length;
        }
    }
}
