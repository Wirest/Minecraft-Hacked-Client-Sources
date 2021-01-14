package optifine.xdelta;

public class SimplePrime
{
    public static long belowOrEqual(long number)
    {
        if (number < 2L)
        {
            return 0L;
        }
        else if (number == 2L)
        {
            return 2L;
        }
        else
        {
            if ((number & 1L) == 0L)
            {
                --number;
            }

            while (!testPrime(number))
            {
                number -= 2L;

                if (number <= 2L)
                {
                    return 2L;
                }
            }

            return number;
        }
    }

    public static long aboveOrEqual(long number)
    {
        if (number <= 2L)
        {
            return 2L;
        }
        else
        {
            if ((number & 1L) == 0L)
            {
                ++number;
            }

            while (!testPrime(number))
            {
                number += 2L;

                if (number < 0L)
                {
                    return 0L;
                }
            }

            return number;
        }
    }

    public static boolean testPrime(long number)
    {
        if (number == 2L)
        {
            return true;
        }
        else if (number < 2L)
        {
            return false;
        }
        else if ((number & 1L) == 0L)
        {
            return false;
        }
        else
        {
            long i = (long)Math.floor(Math.sqrt((double)number));

            for (long j = 3L; j <= i; j += 2L)
            {
                if (number % j == 0L)
                {
                    return false;
                }
            }

            return true;
        }
    }
}
