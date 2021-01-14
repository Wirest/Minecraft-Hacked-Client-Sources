package net.optifine.util;

public class ArrayUtils
{
    public static boolean contains(Object[] arr, Object val)
    {
        if (arr == null)
        {
            return false;
        }
        else
        {
            for (Object object : arr) {
                if (object == val) {
                    return true;
                }
            }

            return false;
        }
    }
}
