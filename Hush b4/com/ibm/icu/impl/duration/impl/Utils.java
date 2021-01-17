// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration.impl;

import java.util.Locale;

public class Utils
{
    public static final Locale localeFromString(final String s) {
        String language = s;
        String region = "";
        String variant = "";
        int x = language.indexOf("_");
        if (x != -1) {
            region = language.substring(x + 1);
            language = language.substring(0, x);
        }
        x = region.indexOf("_");
        if (x != -1) {
            variant = region.substring(x + 1);
            region = region.substring(0, x);
        }
        return new Locale(language, region, variant);
    }
    
    public static String chineseNumber(long n, final ChineseDigits zh) {
        if (n < 0L) {
            n = -n;
        }
        if (n > 10L) {
            final char[] buf = new char[40];
            final char[] digits = String.valueOf(n).toCharArray();
            boolean inZero = true;
            boolean forcedZero = false;
            int x = buf.length;
            int i = digits.length;
            int u = -1;
            int l = -1;
            while (--i >= 0) {
                if (u == -1) {
                    if (l != -1) {
                        buf[--x] = zh.levels[l];
                        inZero = true;
                        forcedZero = false;
                    }
                    ++u;
                }
                else {
                    buf[--x] = zh.units[u++];
                    if (u == 3) {
                        u = -1;
                        ++l;
                    }
                }
                final int d = digits[i] - '0';
                if (d == 0) {
                    if (x < buf.length - 1 && u != 0) {
                        buf[x] = '*';
                    }
                    if (inZero || forcedZero) {
                        buf[--x] = '*';
                    }
                    else {
                        buf[--x] = zh.digits[0];
                        inZero = true;
                        forcedZero = (u == 1);
                    }
                }
                else {
                    inZero = false;
                    buf[--x] = zh.digits[d];
                }
            }
            Label_0493: {
                if (n > 1000000L) {
                    boolean last = true;
                    int j = buf.length - 3;
                    while (true) {
                        while (buf[j] != '0') {
                            j -= 8;
                            last = !last;
                            if (j <= x) {
                                j = buf.length - 7;
                                do {
                                    if (buf[j] == zh.digits[0] && !last) {
                                        buf[j] = '*';
                                    }
                                    j -= 8;
                                    last = !last;
                                } while (j > x);
                                if (n >= 100000000L) {
                                    j = buf.length - 8;
                                    do {
                                        boolean empty = true;
                                        for (int k = j - 1, e = Math.max(x - 1, j - 8); k > e; --k) {
                                            if (buf[k] != '*') {
                                                empty = false;
                                                break;
                                            }
                                        }
                                        if (empty) {
                                            if (buf[j + 1] != '*' && buf[j + 1] != zh.digits[0]) {
                                                buf[j] = zh.digits[0];
                                            }
                                            else {
                                                buf[j] = '*';
                                            }
                                        }
                                        j -= 8;
                                    } while (j > x);
                                }
                                break Label_0493;
                            }
                        }
                        continue;
                    }
                }
            }
            for (i = x; i < buf.length; ++i) {
                if (buf[i] == zh.digits[2]) {
                    if (i >= buf.length - 1 || buf[i + 1] != zh.units[0]) {
                        if (i > x) {
                            if (buf[i - 1] == zh.units[0] || buf[i - 1] == zh.digits[0]) {
                                continue;
                            }
                            if (buf[i - 1] == '*') {
                                continue;
                            }
                        }
                        buf[i] = zh.liang;
                    }
                }
            }
            if (buf[x] == zh.digits[1] && (zh.ko || buf[x + 1] == zh.units[0])) {
                ++x;
            }
            int w = x;
            for (int r = x; r < buf.length; ++r) {
                if (buf[r] != '*') {
                    buf[w++] = buf[r];
                }
            }
            return new String(buf, x, w - x);
        }
        if (n == 2L) {
            return String.valueOf(zh.liang);
        }
        return String.valueOf(zh.digits[(int)n]);
    }
    
    public static class ChineseDigits
    {
        final char[] digits;
        final char[] units;
        final char[] levels;
        final char liang;
        final boolean ko;
        public static final ChineseDigits DEBUG;
        public static final ChineseDigits TRADITIONAL;
        public static final ChineseDigits SIMPLIFIED;
        public static final ChineseDigits KOREAN;
        
        ChineseDigits(final String digits, final String units, final String levels, final char liang, final boolean ko) {
            this.digits = digits.toCharArray();
            this.units = units.toCharArray();
            this.levels = levels.toCharArray();
            this.liang = liang;
            this.ko = ko;
        }
        
        static {
            DEBUG = new ChineseDigits("0123456789s", "sbq", "WYZ", 'L', false);
            TRADITIONAL = new ChineseDigits("\u96f6\u4e00\u4e8c\u4e09\u56db\u4e94\u516d\u4e03\u516b\u4e5d\u5341", "\u5341\u767e\u5343", "\u842c\u5104\u5146", '\u5169', false);
            SIMPLIFIED = new ChineseDigits("\u96f6\u4e00\u4e8c\u4e09\u56db\u4e94\u516d\u4e03\u516b\u4e5d\u5341", "\u5341\u767e\u5343", "\u4e07\u4ebf\u5146", '\u4e24', false);
            KOREAN = new ChineseDigits("\uc601\uc77c\uc774\uc0bc\uc0ac\uc624\uc721\uce60\ud314\uad6c\uc2ed", "\uc2ed\ubc31\ucc9c", "\ub9cc\uc5b5?", '\uc774', true);
        }
    }
}
