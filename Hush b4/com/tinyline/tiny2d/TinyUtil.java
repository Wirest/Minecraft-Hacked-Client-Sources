// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.tiny2d;

public class TinyUtil
{
    public static final int MIN_VALUE = Integer.MIN_VALUE;
    public static final int MAX_VALUE = Integer.MAX_VALUE;
    public static final int FIX_BITS = 8;
    public static final int FIX = 256;
    public static final int DFIX_BITS = 16;
    public static final int DFIX = 65536;
    private static final int int = 92160;
    private static final int for = 46080;
    private static final int try = 69120;
    private static final int do = 23040;
    private static final int[] new;
    private static final int[] if;
    private static final int[] a;
    
    public static final int abs(final int n) {
        return (n < 0) ? (-n) : n;
    }
    
    public static final int max(final int n, final int n2) {
        return (n >= n2) ? n : n2;
    }
    
    public static final int min(final int n, final int n2) {
        return (n <= n2) ? n : n2;
    }
    
    public static final int mul(final int n, final int n2) {
        return (int)(n * (long)n2 + 32768L >> 16);
    }
    
    public static final int fastDistance(int abs, int abs2) {
        abs = abs(abs);
        abs2 = abs(abs2);
        return abs + abs2 - (min(abs, abs2) >> 1);
    }
    
    static final int a(final int n, final int n2) {
        int abs = abs(n);
        int abs2 = abs(n2);
        if (abs > abs2) {
            final int n3 = abs;
            abs = abs2;
            abs2 = n3;
        }
        if (abs2 == 0) {
            return 0;
        }
        final int div = div(abs, abs2);
        final int n4 = div >> 10;
        final int n5 = (div & 0x3FF) << 6;
        return mul(abs2, mul(65536 - n5, TinyUtil.a[n4]) + mul(n5, TinyUtil.a[n4 + 1]) >> 14);
    }
    
    public static final int div(int n, int n2) {
        int n3 = 1;
        if (n < 0) {
            n = -n;
            n3 = -1;
        }
        if (n2 < 0) {
            n2 = -n2;
            n3 = -n3;
        }
        int n4;
        if (n2 == 0) {
            n4 = Integer.MAX_VALUE;
        }
        else {
            n4 = (int)(((long)n << 16) / n2);
        }
        return (n3 < 0) ? (-n4) : n4;
    }
    
    public static final int round(final int n) {
        if (n < 0) {
            return (n - 32768) / 65536;
        }
        return (n + 32768) / 65536;
    }
    
    public static int sin(int i) {
        boolean b = false;
        if (i < 0) {
            i = -i;
            b = !b;
        }
        if (i > 92160) {
            while (i > 92160) {
                i -= 92160;
            }
        }
        if (i > 46080) {
            i = 92160 - i;
            b = !b;
        }
        if (i > 23040) {
            i = 46080 - i;
        }
        final int n = TinyUtil.new[i >> 8];
        return b ? (-n) : n;
    }
    
    public static int cos(final int n) {
        return sin(23040 - n);
    }
    
    public static int tan(final int n) {
        return div(sin(n), cos(n));
    }
    
    public static int atan2(int n, int n2) {
        int n3;
        if (n2 > 0 && n >= 0) {
            n3 = 0;
        }
        else if (n2 <= 0 && n > 0) {
            final int n4 = -n2;
            n2 = n;
            n = n4;
            n3 = 23040;
        }
        else if (n2 < 0 && n <= 0) {
            n2 = -n2;
            n = -n;
            n3 = 46080;
        }
        else {
            if (n2 < 0 || n >= 0) {
                return 0;
            }
            final int n5 = n2;
            n2 = -n;
            n = n5;
            n3 = 69120;
        }
        int n6;
        if (n2 >= n) {
            n6 = n3 + TinyUtil.if[div(n, n2) >> 10];
        }
        else {
            n6 = n3 + (23040 - TinyUtil.if[div(n2, n) >> 10]);
        }
        return n6;
    }
    
    static {
        new = new int[] { 0, 1143, 2287, 3429, 4571, 5711, 6850, 7986, 9120, 10252, 11380, 12504, 13625, 14742, 15854, 16961, 18064, 19160, 20251, 21336, 22414, 23486, 24550, 25606, 26655, 27696, 28729, 29752, 30767, 31772, 32767, 33753, 34728, 35693, 36647, 37589, 38521, 39440, 40347, 41243, 42125, 42995, 43852, 44695, 45525, 46340, 47142, 47929, 48702, 49460, 50203, 50931, 51643, 52339, 53019, 53683, 54331, 54963, 55577, 56175, 56755, 57319, 57864, 58393, 58903, 59395, 59870, 60326, 60763, 61183, 61583, 61965, 62328, 62672, 62997, 63302, 63589, 63856, 64103, 64331, 64540, 64729, 64898, 65047, 65176, 65286, 65376, 65446, 65496, 65526, 65536 };
        if = new int[] { 0, 0, 256, 512, 768, 1024, 1280, 1536, 1792, 2048, 2048, 2304, 2560, 2816, 3072, 3328, 3584, 3584, 3840, 4096, 4352, 4608, 4608, 4864, 5120, 5376, 5632, 5632, 5888, 6144, 6400, 6400, 6656, 6912, 6912, 7168, 7424, 7680, 7680, 7936, 8192, 8192, 8448, 8448, 8704, 8960, 8960, 9216, 9216, 9472, 9472, 9728, 9984, 9984, 10240, 10240, 10496, 10496, 10752, 10752, 11008, 11008, 11264, 11264, 11264 };
        a = new int[] { 1073741824, 1073872888, 1074265984, 1074920825, 1075836932, 1077013639, 1078450093, 1080145258, 1082097918, 1084306681, 1086769986, 1089486107, 1092453157, 1095669100, 1099131748, 1102838780, 1106787739, 1110976045, 1115401003, 1120059807, 1124949552, 1130067241, 1135409791, 1140974043, 1146756771, 1152754686, 1158964447, 1165382668, 1172005924, 1178830760, 1185853694, 1193071229, 1200479854, 1208076055, 1215856315, 1223817123, 1231954981, 1240266402, 1248747921, 1257396097, 1266207514, 1275178788, 1284306569, 1293587545, 1303018442, 1312596028, 1322317116, 1332178565, 1342177280, 1352310217, 1362574382, 1372966831, 1383484673, 1394125071, 1404885240, 1415762448, 1426754019, 1437857331, 1449069814, 1460388955, 1471812291, 1483337417, 1494961978, 1506683672, 1518500250, 1518500250 };
    }
}
