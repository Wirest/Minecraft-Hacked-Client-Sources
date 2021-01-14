package optifine;

public class MathUtils
{
    public static int getAverage(int[] vals)
    {
        if (vals.length <= 0)
        {
            return 0;
        }
        else
        {
            int sum = 0;
            int avg;

            for (avg = 0; avg < vals.length; ++avg)
            {
                int val = vals[avg];
                sum += val;
            }

            avg = sum / vals.length;
            return avg;
        }
    }
}
