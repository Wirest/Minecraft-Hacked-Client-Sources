/*
 * Decompiled with CFR 0.150.
 */
package delta;

public final class Class11
extends Enum<Class11> {
    public static final /* enum */ Class11 captain$;
    private static String[] ratio$;
    public static final /* enum */ Class11 alarm$;
    public static final /* enum */ Class11 failing$;
    public static final /* enum */ Class11 motel$;
    private static final Class11[] operator$;

    static {
        Class11._august();
        motel$ = new Class11();
        alarm$ = new Class11();
        captain$ = new Class11();
        failing$ = new Class11();
        Class11[] arrclass11 = new Class11[124 - 247 + 132 + -5];
        arrclass11[31 - 54 + 25 + -2] = motel$;
        arrclass11[55 - 60 + 24 - 14 + -4] = alarm$;
        arrclass11[22 - 35 + 35 - 9 + -11] = captain$;
        arrclass11[207 - 353 + 190 - 67 + 26] = failing$;
        operator$ = arrclass11;
    }

    public static Class11 _alumni(String string) {
        return Enum.valueOf(Class11.class, string);
    }

    public static Class11[] _baskets() {
        return (Class11[])operator$.clone();
    }

    private static void _august() {
        ratio$ = new String[]{"\u2b1e\u2b05\u2b1a", "\u20d0\u20dd\u20c6\u20c6\u20dd\u20df", "\u9ed1\u9ed8\u9edb\u9ec9", "\u8ea2\u8eb9\u8eb7\u8eb8\u8ea4"};
    }
}

