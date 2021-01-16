/*
 * Decompiled with CFR 0_122.
 */
package darkmagician6;

public final class Priority {
    public static final byte HIGHEST = 0;
    public static final byte HIGH = 1;
    public static final byte MEDIUM = 2;
    public static final byte LOW = 3;
    public static final byte LOWEST = 4;
    public static final byte[] VALUE_ARRAY;

    static {
        byte[] arrby = new byte[5];
        arrby[1] = 1;
        arrby[2] = 2;
        arrby[3] = 3;
        arrby[4] = 4;
        VALUE_ARRAY = arrby;
    }
}

