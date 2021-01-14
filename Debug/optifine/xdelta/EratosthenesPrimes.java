package optifine.xdelta;

public class EratosthenesPrimes
{
    static BitArray sieve;
    static int lastInit = -1;

    public static synchronized void reset()
    {
        sieve = null;
        lastInit = -1;
    }

    public static synchronized void init(int maxNumber)
    {
        if (maxNumber > lastInit)
        {
            int i = (int)Math.ceil(Math.sqrt((double)maxNumber));
            lastInit = maxNumber;
            maxNumber = maxNumber >> 1;
            ++maxNumber;
            i = i >> 1;
            ++i;
            sieve = new BitArray(maxNumber + 1);
            sieve.set(0, true);

            for (int j = 1; j <= i; ++j)
            {
                if (!sieve.get(j))
                {
                    int k = (j << 1) + 1;

                    for (int l = j * ((j << 1) + 2); l <= maxNumber; l += k)
                    {
                        sieve.set(l, true);
                    }
                }
            }
        }
    }

    public static synchronized int[] getPrimes(int maxNumber)
    {
        int i = primesCount(maxNumber);

        if (i <= 0)
        {
            return new int[0];
        }
        else if (maxNumber == 2)
        {
            return new int[] {2};
        }
        else
        {
            init(maxNumber);
            int[] aint = new int[i];
            int j = maxNumber - 1 >> 1;
            int k = 0;
            aint[k++] = 2;

            for (int l = 1; l <= j; ++l)
            {
                if (!sieve.get(l))
                {
                    aint[k++] = (l << 1) + 1;
                }
            }

            return aint;
        }
    }

    public static synchronized int primesCount(int number)
    {
        if (number < 2)
        {
            return 0;
        }
        else
        {
            init(number);
            int i = number - 1 >> 1;
            int j = 1;

            for (int k = 1; k <= i; ++k)
            {
                if (!sieve.get(k))
                {
                    ++j;
                }
            }

            return j;
        }
    }

    public static synchronized int belowOrEqual(int number)
    {
        if (number < 2)
        {
            return -1;
        }
        else if (number == 2)
        {
            return 2;
        }
        else
        {
            init(number);
            int i = number - 1 >> 1;

            for (int j = i; j > 0; --j)
            {
                if (!sieve.get(j))
                {
                    return (j << 1) + 1;
                }
            }

            return -1;
        }
    }

    public static int below(int number)
    {
        return belowOrEqual(number - 1);
    }
}
