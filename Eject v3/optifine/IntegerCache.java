package optifine;

public class IntegerCache {
    private static final int CACHE_SIZE = 4096;
    private static final Integer[] cache = makeCache(4096);

    private static Integer[] makeCache(int paramInt) {
        Integer[] arrayOfInteger = new Integer[paramInt];
        for (int i = 0; i < paramInt; i++) {
            arrayOfInteger[i] = new Integer(i);
        }
        return arrayOfInteger;
    }

    public static Integer valueOf(int paramInt) {
        return (paramInt >= 0) && (paramInt < 4096) ? cache[paramInt] : new Integer(paramInt);
    }
}




