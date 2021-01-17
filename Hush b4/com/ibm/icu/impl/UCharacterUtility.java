// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

public final class UCharacterUtility
{
    private static final int NON_CHARACTER_SUFFIX_MIN_3_0_ = 65534;
    private static final int NON_CHARACTER_MIN_3_1_ = 64976;
    private static final int NON_CHARACTER_MAX_3_1_ = 65007;
    
    public static boolean isNonCharacter(final int ch) {
        return (ch & 0xFFFE) == 0xFFFE || (ch >= 64976 && ch <= 65007);
    }
    
    static int toInt(final char msc, final char lsc) {
        return msc << 16 | lsc;
    }
    
    static int getNullTermByteSubString(final StringBuffer str, final byte[] array, int index) {
        byte b = 1;
        while (b != 0) {
            b = array[index];
            if (b != 0) {
                str.append((char)(b & 0xFF));
            }
            ++index;
        }
        return index;
    }
    
    static int compareNullTermByteSubString(final String str, final byte[] array, int strindex, int aindex) {
        byte b = 1;
        final int length = str.length();
        while (b != 0) {
            b = array[aindex];
            ++aindex;
            if (b == 0) {
                break;
            }
            if (strindex == length || str.charAt(strindex) != (char)(b & 0xFF)) {
                return -1;
            }
            ++strindex;
        }
        return strindex;
    }
    
    static int skipNullTermByteSubString(final byte[] array, int index, final int skipcount) {
        for (int i = 0; i < skipcount; ++i) {
            for (byte b = 1; b != 0; b = array[index], ++index) {}
        }
        return index;
    }
    
    static int skipByteSubString(final byte[] array, final int index, final int length, final byte skipend) {
        int result;
        for (result = 0; result < length; ++result) {
            final byte b = array[index + result];
            if (b == skipend) {
                ++result;
                break;
            }
        }
        return result;
    }
    
    private UCharacterUtility() {
    }
}
