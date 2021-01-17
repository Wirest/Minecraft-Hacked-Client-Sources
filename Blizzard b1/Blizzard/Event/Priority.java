/*
 * Decompiled with CFR 0_122.
 */
package Blizzard.Event;

public class Priority {
    public static final byte FIRST = 0;
    public static final byte SECOND = 1;
    public static final byte THIRD = 2;
    public static final byte FOURTH = 3;
    public static final byte FIFTH = 4;
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

