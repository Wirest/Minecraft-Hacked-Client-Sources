// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.concurrent.ConcurrentHashMap;

public final class VersionInfo implements Comparable<VersionInfo>
{
    public static final VersionInfo UNICODE_1_0;
    public static final VersionInfo UNICODE_1_0_1;
    public static final VersionInfo UNICODE_1_1_0;
    public static final VersionInfo UNICODE_1_1_5;
    public static final VersionInfo UNICODE_2_0;
    public static final VersionInfo UNICODE_2_1_2;
    public static final VersionInfo UNICODE_2_1_5;
    public static final VersionInfo UNICODE_2_1_8;
    public static final VersionInfo UNICODE_2_1_9;
    public static final VersionInfo UNICODE_3_0;
    public static final VersionInfo UNICODE_3_0_1;
    public static final VersionInfo UNICODE_3_1_0;
    public static final VersionInfo UNICODE_3_1_1;
    public static final VersionInfo UNICODE_3_2;
    public static final VersionInfo UNICODE_4_0;
    public static final VersionInfo UNICODE_4_0_1;
    public static final VersionInfo UNICODE_4_1;
    public static final VersionInfo UNICODE_5_0;
    public static final VersionInfo UNICODE_5_1;
    public static final VersionInfo UNICODE_5_2;
    public static final VersionInfo UNICODE_6_0;
    public static final VersionInfo UNICODE_6_1;
    public static final VersionInfo UNICODE_6_2;
    public static final VersionInfo ICU_VERSION;
    @Deprecated
    public static final String ICU_DATA_VERSION_PATH = "51b";
    @Deprecated
    public static final VersionInfo ICU_DATA_VERSION;
    public static final VersionInfo UCOL_RUNTIME_VERSION;
    public static final VersionInfo UCOL_BUILDER_VERSION;
    public static final VersionInfo UCOL_TAILORINGS_VERSION;
    private static volatile VersionInfo javaVersion;
    private static final VersionInfo UNICODE_VERSION;
    private int m_version_;
    private static final ConcurrentHashMap<Integer, VersionInfo> MAP_;
    private static final int LAST_BYTE_MASK_ = 255;
    private static final String INVALID_VERSION_NUMBER_ = "Invalid version number: Version number may be negative or greater than 255";
    
    public static VersionInfo getInstance(final String version) {
        final int length = version.length();
        final int[] array = { 0, 0, 0, 0 };
        int count;
        int index;
        for (count = 0, index = 0; count < 4 && index < length; ++index) {
            char c = version.charAt(index);
            if (c == '.') {
                ++count;
            }
            else {
                c -= '0';
                if (c < '\0' || c > '\t') {
                    throw new IllegalArgumentException("Invalid version number: Version number may be negative or greater than 255");
                }
                final int[] array2 = array;
                final int n = count;
                array2[n] *= 10;
                final int[] array3 = array;
                final int n2 = count;
                array3[n2] += c;
            }
        }
        if (index != length) {
            throw new IllegalArgumentException("Invalid version number: String '" + version + "' exceeds version format");
        }
        for (int i = 0; i < 4; ++i) {
            if (array[i] < 0 || array[i] > 255) {
                throw new IllegalArgumentException("Invalid version number: Version number may be negative or greater than 255");
            }
        }
        return getInstance(array[0], array[1], array[2], array[3]);
    }
    
    public static VersionInfo getInstance(final int major, final int minor, final int milli, final int micro) {
        if (major < 0 || major > 255 || minor < 0 || minor > 255 || milli < 0 || milli > 255 || micro < 0 || micro > 255) {
            throw new IllegalArgumentException("Invalid version number: Version number may be negative or greater than 255");
        }
        final int version = getInt(major, minor, milli, micro);
        final Integer key = version;
        VersionInfo result = VersionInfo.MAP_.get(key);
        if (result == null) {
            result = new VersionInfo(version);
            final VersionInfo tmpvi = VersionInfo.MAP_.putIfAbsent(key, result);
            if (tmpvi != null) {
                result = tmpvi;
            }
        }
        return result;
    }
    
    public static VersionInfo getInstance(final int major, final int minor, final int milli) {
        return getInstance(major, minor, milli, 0);
    }
    
    public static VersionInfo getInstance(final int major, final int minor) {
        return getInstance(major, minor, 0, 0);
    }
    
    public static VersionInfo getInstance(final int major) {
        return getInstance(major, 0, 0, 0);
    }
    
    @Deprecated
    public static VersionInfo javaVersion() {
        if (VersionInfo.javaVersion == null) {
            synchronized (VersionInfo.class) {
                if (VersionInfo.javaVersion == null) {
                    final String s = System.getProperty("java.version");
                    final char[] chars = s.toCharArray();
                    int r = 0;
                    int w = 0;
                    int count = 0;
                    boolean numeric = false;
                    while (r < chars.length) {
                        final char c = chars[r++];
                        if (c < '0' || c > '9') {
                            if (!numeric) {
                                continue;
                            }
                            if (count == 3) {
                                break;
                            }
                            numeric = false;
                            chars[w++] = '.';
                            ++count;
                        }
                        else {
                            numeric = true;
                            chars[w++] = c;
                        }
                    }
                    while (w > 0 && chars[w - 1] == '.') {
                        --w;
                    }
                    final String vs = new String(chars, 0, w);
                    VersionInfo.javaVersion = getInstance(vs);
                }
            }
        }
        return VersionInfo.javaVersion;
    }
    
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder(7);
        result.append(this.getMajor());
        result.append('.');
        result.append(this.getMinor());
        result.append('.');
        result.append(this.getMilli());
        result.append('.');
        result.append(this.getMicro());
        return result.toString();
    }
    
    public int getMajor() {
        return this.m_version_ >> 24 & 0xFF;
    }
    
    public int getMinor() {
        return this.m_version_ >> 16 & 0xFF;
    }
    
    public int getMilli() {
        return this.m_version_ >> 8 & 0xFF;
    }
    
    public int getMicro() {
        return this.m_version_ & 0xFF;
    }
    
    @Override
    public boolean equals(final Object other) {
        return other == this;
    }
    
    public int compareTo(final VersionInfo other) {
        return this.m_version_ - other.m_version_;
    }
    
    private VersionInfo(final int compactversion) {
        this.m_version_ = compactversion;
    }
    
    private static int getInt(final int major, final int minor, final int milli, final int micro) {
        return major << 24 | minor << 16 | milli << 8 | micro;
    }
    
    public static void main(final String[] args) {
        String icuApiVer;
        if (VersionInfo.ICU_VERSION.getMajor() <= 4) {
            if (VersionInfo.ICU_VERSION.getMinor() % 2 != 0) {
                int major = VersionInfo.ICU_VERSION.getMajor();
                int minor = VersionInfo.ICU_VERSION.getMinor() + 1;
                if (minor >= 10) {
                    minor -= 10;
                    ++major;
                }
                icuApiVer = "" + major + "." + minor + "M" + VersionInfo.ICU_VERSION.getMilli();
            }
            else {
                icuApiVer = VersionInfo.ICU_VERSION.getVersionString(2, 2);
            }
        }
        else if (VersionInfo.ICU_VERSION.getMinor() == 0) {
            icuApiVer = "" + VersionInfo.ICU_VERSION.getMajor() + "M" + VersionInfo.ICU_VERSION.getMilli();
        }
        else {
            icuApiVer = VersionInfo.ICU_VERSION.getVersionString(2, 2);
        }
        System.out.println("International Components for Unicode for Java " + icuApiVer);
        System.out.println("");
        System.out.println("Implementation Version: " + VersionInfo.ICU_VERSION.getVersionString(2, 4));
        System.out.println("Unicode Data Version:   " + VersionInfo.UNICODE_VERSION.getVersionString(2, 4));
        System.out.println("CLDR Data Version:      " + LocaleData.getCLDRVersion().getVersionString(2, 4));
        System.out.println("Time Zone Data Version: " + TimeZone.getTZDataVersion());
    }
    
    private String getVersionString(final int minDigits, final int maxDigits) {
        if (minDigits < 1 || maxDigits < 1 || minDigits > 4 || maxDigits > 4 || minDigits > maxDigits) {
            throw new IllegalArgumentException("Invalid min/maxDigits range");
        }
        int[] digits;
        int numDigits;
        for (digits = new int[] { this.getMajor(), this.getMinor(), this.getMilli(), this.getMicro() }, numDigits = maxDigits; numDigits > minDigits && digits[numDigits - 1] == 0; --numDigits) {}
        final StringBuilder verStr = new StringBuilder(7);
        verStr.append(digits[0]);
        for (int i = 1; i < numDigits; ++i) {
            verStr.append(".");
            verStr.append(digits[i]);
        }
        return verStr.toString();
    }
    
    static {
        MAP_ = new ConcurrentHashMap<Integer, VersionInfo>();
        UNICODE_1_0 = getInstance(1, 0, 0, 0);
        UNICODE_1_0_1 = getInstance(1, 0, 1, 0);
        UNICODE_1_1_0 = getInstance(1, 1, 0, 0);
        UNICODE_1_1_5 = getInstance(1, 1, 5, 0);
        UNICODE_2_0 = getInstance(2, 0, 0, 0);
        UNICODE_2_1_2 = getInstance(2, 1, 2, 0);
        UNICODE_2_1_5 = getInstance(2, 1, 5, 0);
        UNICODE_2_1_8 = getInstance(2, 1, 8, 0);
        UNICODE_2_1_9 = getInstance(2, 1, 9, 0);
        UNICODE_3_0 = getInstance(3, 0, 0, 0);
        UNICODE_3_0_1 = getInstance(3, 0, 1, 0);
        UNICODE_3_1_0 = getInstance(3, 1, 0, 0);
        UNICODE_3_1_1 = getInstance(3, 1, 1, 0);
        UNICODE_3_2 = getInstance(3, 2, 0, 0);
        UNICODE_4_0 = getInstance(4, 0, 0, 0);
        UNICODE_4_0_1 = getInstance(4, 0, 1, 0);
        UNICODE_4_1 = getInstance(4, 1, 0, 0);
        UNICODE_5_0 = getInstance(5, 0, 0, 0);
        UNICODE_5_1 = getInstance(5, 1, 0, 0);
        UNICODE_5_2 = getInstance(5, 2, 0, 0);
        UNICODE_6_0 = getInstance(6, 0, 0, 0);
        UNICODE_6_1 = getInstance(6, 1, 0, 0);
        UNICODE_6_2 = getInstance(6, 2, 0, 0);
        ICU_VERSION = getInstance(51, 2, 0, 0);
        ICU_DATA_VERSION = getInstance(51, 2, 0, 0);
        UNICODE_VERSION = VersionInfo.UNICODE_6_2;
        UCOL_RUNTIME_VERSION = getInstance(7);
        UCOL_BUILDER_VERSION = getInstance(8);
        UCOL_TAILORINGS_VERSION = getInstance(1);
    }
}
