package optifine;

public class IntegerCache
{
    private static final int CACHE_SIZE = 4096;
    private static final Integer[] cache = makeCache(4096);

    private static Integer[] makeCache(int size)
    {
        Integer[] arr = new Integer[size];

        for (int i = 0; i < size; ++i)
        {
            arr[i] = new Integer(i);
        }

        return arr;
    }

    public static Integer valueOf(int value)
    {
        return value >= 0 && value < 4096 ? cache[value] : new Integer(value);
    }
}
