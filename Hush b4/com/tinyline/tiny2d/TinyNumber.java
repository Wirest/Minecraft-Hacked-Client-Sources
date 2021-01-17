// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.tiny2d;

public class TinyNumber
{
    public int val;
    public static final int NUMBER_ERR_OK = 0;
    public static final int NUMBER_ERR_FORMAT = 1;
    public static final int INHERIT = Integer.MIN_VALUE;
    public static int error;
    private static final int[] a;
    
    public TinyNumber(final int val) {
        this.val = val;
    }
    
    public static final int parseFix(final TinyString tinyString) {
        return parseFix(tinyString.data, 0, tinyString.count);
    }
    
    public static final int parseDoubleFix(final TinyString tinyString) {
        return parseDoubleFix(tinyString.data, 0, tinyString.count);
    }
    
    public static final int parseInt(final TinyString tinyString) {
        return parseInt(tinyString.data, 0, tinyString.count, 10);
    }
    
    public static final int parseFix(final char[] array, final int n, final int n2) {
        return parseDoubleFix(array, n, n2) / 256;
    }
    
    public static final int parseDoubleFix(final char[] array, int n, final int n2) {
        TinyNumber.error = 0;
        int n3 = 0;
        int n4 = 0;
        boolean b = true;
        boolean b2 = false;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        boolean b3 = true;
        int n8 = array[n++];
        switch (n8) {
            case 45: {
                b = false;
            }
            case 43: {
                if (n == n2) {
                    n8 = -1;
                    break;
                }
                n8 = array[n++];
                break;
            }
        }
        Label_0311: {
            switch (n8) {
                default: {
                    TinyNumber.error = 1;
                    return 0;
                }
                case 46: {
                    break;
                }
                case 48: {
                    b2 = true;
                    while (true) {
                        if (n == n2) {
                            n8 = -1;
                        }
                        else {
                            n8 = array[n++];
                        }
                        switch (n8) {
                            case 49:
                            case 50:
                            case 51:
                            case 52:
                            case 53:
                            case 54:
                            case 55:
                            case 56:
                            case 57: {
                                break Label_0311;
                            }
                            case 46:
                            case 69:
                            case 101: {
                                break Label_0311;
                            }
                            default: {
                                return 0;
                            }
                            case 48: {
                                continue;
                            }
                        }
                    }
                    break;
                }
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57: {
                    b2 = true;
                    while (true) {
                        if (n4 < 9) {
                            ++n4;
                            n3 = n3 * 10 + (n8 - 48);
                        }
                        else {
                            ++n7;
                        }
                        if (n == n2) {
                            n8 = -1;
                        }
                        else {
                            n8 = array[n++];
                        }
                        switch (n8) {
                            default: {
                                break Label_0311;
                            }
                            case 48:
                            case 49:
                            case 50:
                            case 51:
                            case 52:
                            case 53:
                            case 54:
                            case 55:
                            case 56:
                            case 57: {
                                continue;
                            }
                        }
                    }
                    break;
                }
            }
        }
        Label_0782: {
            if (n8 == 46) {
                if (n == n2) {
                    n8 = -1;
                }
                else {
                    n8 = array[n++];
                }
                Label_0676: {
                    switch (n8) {
                        case 69:
                        case 101: {
                            if (!b2) {
                                TinyNumber.error = 1;
                                return Integer.MIN_VALUE;
                            }
                            break;
                        }
                        case 48: {
                            if (n4 != 0) {
                                break Label_0676;
                            }
                            while (true) {
                                if (n == n2) {
                                    n8 = -1;
                                }
                                else {
                                    n8 = array[n++];
                                }
                                --n7;
                                switch (n8) {
                                    case 49:
                                    case 50:
                                    case 51:
                                    case 52:
                                    case 53:
                                    case 54:
                                    case 55:
                                    case 56:
                                    case 57: {
                                        break Label_0676;
                                    }
                                    default: {
                                        if (!b2) {
                                            return 0;
                                        }
                                        break Label_0782;
                                    }
                                    case 48: {
                                        continue;
                                    }
                                }
                            }
                            break;
                        }
                        case 49:
                        case 50:
                        case 51:
                        case 52:
                        case 53:
                        case 54:
                        case 55:
                        case 56:
                        case 57: {
                            while (true) {
                                if (n4 < 9) {
                                    ++n4;
                                    n3 = n3 * 10 + (n8 - 48);
                                    --n7;
                                }
                                if (n == n2) {
                                    n8 = -1;
                                }
                                else {
                                    n8 = array[n++];
                                }
                                switch (n8) {
                                    default: {
                                        break Label_0782;
                                    }
                                    case 48:
                                    case 49:
                                    case 50:
                                    case 51:
                                    case 52:
                                    case 53:
                                    case 54:
                                    case 55:
                                    case 56:
                                    case 57: {
                                        continue;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        Label_1246: {
            switch (n8) {
                case 69:
                case 101: {
                    int n9;
                    if (n == n2) {
                        n9 = -1;
                    }
                    else {
                        n9 = array[n++];
                    }
                    Label_0999: {
                        switch (n9) {
                            default: {
                                TinyNumber.error = 1;
                                return 0;
                            }
                            case 45: {
                                b3 = false;
                            }
                            case 43: {
                                if (n == n2) {
                                    n9 = -1;
                                }
                                else {
                                    n9 = array[n++];
                                }
                                switch (n9) {
                                    default: {
                                        TinyNumber.error = 1;
                                        return Integer.MIN_VALUE;
                                    }
                                    case 48:
                                    case 49:
                                    case 50:
                                    case 51:
                                    case 52:
                                    case 53:
                                    case 54:
                                    case 55:
                                    case 56:
                                    case 57: {
                                        break Label_0999;
                                    }
                                }
                                break;
                            }
                            case 48:
                            case 49:
                            case 50:
                            case 51:
                            case 52:
                            case 53:
                            case 54:
                            case 55:
                            case 56:
                            case 57: {
                                Label_1141: {
                                    switch (n9) {
                                        case 48: {
                                            while (true) {
                                                if (n == n2) {
                                                    n9 = -1;
                                                }
                                                else {
                                                    n9 = array[n++];
                                                }
                                                switch (n9) {
                                                    case 49:
                                                    case 50:
                                                    case 51:
                                                    case 52:
                                                    case 53:
                                                    case 54:
                                                    case 55:
                                                    case 56:
                                                    case 57: {
                                                        break Label_1141;
                                                    }
                                                    default: {
                                                        break Label_1246;
                                                    }
                                                    case 48: {
                                                        continue;
                                                    }
                                                }
                                            }
                                            break;
                                        }
                                        case 49:
                                        case 50:
                                        case 51:
                                        case 52:
                                        case 53:
                                        case 54:
                                        case 55:
                                        case 56:
                                        case 57: {
                                            while (true) {
                                                if (n6 < 3) {
                                                    ++n6;
                                                    n5 = n5 * 10 + (n9 - 48);
                                                }
                                                if (n == n2) {
                                                    n9 = -1;
                                                }
                                                else {
                                                    n9 = array[n++];
                                                }
                                                switch (n9) {
                                                    default: {
                                                        break Label_1246;
                                                    }
                                                    case 48:
                                                    case 49:
                                                    case 50:
                                                    case 51:
                                                    case 52:
                                                    case 53:
                                                    case 54:
                                                    case 55:
                                                    case 56:
                                                    case 57: {
                                                        continue;
                                                    }
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
        if (!b3) {
            n5 = -n5;
        }
        int n10 = n5 + n7;
        if (!b) {
            n3 = -n3;
        }
        long n11 = n3 * (long)65536;
        if (n10 < -125 || n3 == 0) {
            return 0;
        }
        if (n10 > 128) {
            return (n3 > 0) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        if (n10 == 0) {
            return (int)n11;
        }
        if (n11 >= 67108864) {
            ++n11;
        }
        if (n10 > 0) {
            if (n10 > 10) {
                n10 = 10;
            }
            return (int)(n11 * TinyNumber.a[n10]);
        }
        int n12 = -n10;
        if (n12 > 10) {
            n12 = 10;
        }
        return (int)(n11 / TinyNumber.a[n12]);
    }
    
    public static int parseInt(final char[] array, int i, final int n, final int n2) {
        TinyNumber.error = 0;
        int n3 = 0;
        boolean b = false;
        if (n <= 0) {
            TinyNumber.error = 1;
            return 0;
        }
        int n4;
        if (array[i] == '-') {
            b = true;
            n4 = Integer.MIN_VALUE;
            ++i;
        }
        else {
            n4 = -2147483647;
        }
        final int n5 = n4 / n2;
        if (i < n) {
            final int digit = Character.digit(array[i++], n2);
            if (digit < 0) {
                TinyNumber.error = 1;
                return 0;
            }
            n3 = -digit;
        }
        while (i < n) {
            final int digit2 = Character.digit(array[i++], n2);
            if (digit2 < 0) {
                TinyNumber.error = 1;
                return 0;
            }
            if (n3 < n5) {
                TinyNumber.error = 1;
                return 0;
            }
            final int n6 = n3 * n2;
            if (n6 < n4 + digit2) {
                TinyNumber.error = 1;
                return 0;
            }
            n3 = n6 - digit2;
        }
        if (!b) {
            return -n3;
        }
        if (i > 1) {
            return n3;
        }
        TinyNumber.error = 1;
        return 0;
    }
    
    static {
        a = new int[] { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000, Integer.MAX_VALUE };
    }
}
