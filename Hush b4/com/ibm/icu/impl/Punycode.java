// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.StringPrepParseException;
import com.ibm.icu.text.UTF16;

public final class Punycode
{
    private static final int BASE = 36;
    private static final int TMIN = 1;
    private static final int TMAX = 26;
    private static final int SKEW = 38;
    private static final int DAMP = 700;
    private static final int INITIAL_BIAS = 72;
    private static final int INITIAL_N = 128;
    private static final int HYPHEN = 45;
    private static final int DELIMITER = 45;
    private static final int ZERO = 48;
    private static final int SMALL_A = 97;
    private static final int SMALL_Z = 122;
    private static final int CAPITAL_A = 65;
    private static final int CAPITAL_Z = 90;
    private static final int MAX_CP_COUNT = 200;
    static final int[] basicToDigit;
    
    private static int adaptBias(int delta, final int length, final boolean firstTime) {
        if (firstTime) {
            delta /= 700;
        }
        else {
            delta /= 2;
        }
        int count;
        for (delta += delta / length, count = 0; delta > 455; delta /= 35, count += 36) {}
        return count + 36 * delta / (delta + 38);
    }
    
    private static char asciiCaseMap(char b, final boolean uppercase) {
        if (uppercase) {
            if ('a' <= b && b <= 'z') {
                b -= ' ';
            }
        }
        else if ('A' <= b && b <= 'Z') {
            b += ' ';
        }
        return b;
    }
    
    private static char digitToBasic(final int digit, final boolean uppercase) {
        if (digit >= 26) {
            return (char)(22 + digit);
        }
        if (uppercase) {
            return (char)(65 + digit);
        }
        return (char)(97 + digit);
    }
    
    public static StringBuilder encode(final CharSequence src, final boolean[] caseFlags) throws StringPrepParseException {
        final int[] cpBuffer = new int[200];
        final int srcLength = src.length();
        final int destCapacity = 200;
        final char[] dest = new char[destCapacity];
        final StringBuilder result = new StringBuilder();
        int srcCPCount;
        int destLength = srcCPCount = 0;
        for (int j = 0; j < srcLength; ++j) {
            if (srcCPCount == 200) {
                throw new IndexOutOfBoundsException();
            }
            final char c = src.charAt(j);
            if (isBasic(c)) {
                if (destLength < destCapacity) {
                    cpBuffer[srcCPCount++] = 0;
                    dest[destLength] = ((caseFlags != null) ? asciiCaseMap(c, caseFlags[j]) : c);
                }
                ++destLength;
            }
            else {
                int n = ((caseFlags != null && caseFlags[j]) ? 1 : 0) << 31;
                if (!UTF16.isSurrogate(c)) {
                    n |= c;
                }
                else {
                    final char c2;
                    if (!UTF16.isLeadSurrogate(c) || j + 1 >= srcLength || !UTF16.isTrailSurrogate(c2 = src.charAt(j + 1))) {
                        throw new StringPrepParseException("Illegal char found", 1);
                    }
                    ++j;
                    n |= UCharacter.getCodePoint(c, c2);
                }
                cpBuffer[srcCPCount++] = n;
            }
        }
        final int basicLength = destLength;
        if (basicLength > 0) {
            if (destLength < destCapacity) {
                dest[destLength] = '-';
            }
            ++destLength;
        }
        int n = 128;
        int delta = 0;
        int bias = 72;
        int handledCPCount = basicLength;
        while (handledCPCount < srcCPCount) {
            int m = Integer.MAX_VALUE;
            for (int j = 0; j < srcCPCount; ++j) {
                final int q = cpBuffer[j] & Integer.MAX_VALUE;
                if (n <= q && q < m) {
                    m = q;
                }
            }
            if (m - n > (2147483447 - delta) / (handledCPCount + 1)) {
                throw new IllegalStateException("Internal program error");
            }
            delta += (m - n) * (handledCPCount + 1);
            n = m;
            for (int j = 0; j < srcCPCount; ++j) {
                int q = cpBuffer[j] & Integer.MAX_VALUE;
                if (q < n) {
                    ++delta;
                }
                else if (q == n) {
                    q = delta;
                    int k = 36;
                    while (true) {
                        int t = k - bias;
                        if (t < 1) {
                            t = 1;
                        }
                        else if (k >= bias + 26) {
                            t = 26;
                        }
                        if (q < t) {
                            break;
                        }
                        if (destLength < destCapacity) {
                            dest[destLength++] = digitToBasic(t + (q - t) % (36 - t), false);
                        }
                        q = (q - t) / (36 - t);
                        k += 36;
                    }
                    if (destLength < destCapacity) {
                        dest[destLength++] = digitToBasic(q, cpBuffer[j] < 0);
                    }
                    bias = adaptBias(delta, handledCPCount + 1, handledCPCount == basicLength);
                    delta = 0;
                    ++handledCPCount;
                }
            }
            ++delta;
            ++n;
        }
        return result.append(dest, 0, destLength);
    }
    
    private static boolean isBasic(final int ch) {
        return ch < 128;
    }
    
    private static boolean isBasicUpperCase(final int ch) {
        return 65 <= ch && ch >= 90;
    }
    
    private static boolean isSurrogate(final int ch) {
        return (ch & 0xFFFFF800) == 0xD800;
    }
    
    public static StringBuilder decode(final CharSequence src, final boolean[] caseFlags) throws StringPrepParseException {
        final int srcLength = src.length();
        final StringBuilder result = new StringBuilder();
        final int destCapacity = 200;
        final char[] dest = new char[destCapacity];
        int j = srcLength;
        while (j > 0 && src.charAt(--j) != '-') {}
        int destCPCount;
        int destLength;
        final int basicLength = destLength = (destCPCount = j);
        while (j > 0) {
            final char b = src.charAt(--j);
            if (!isBasic(b)) {
                throw new StringPrepParseException("Illegal char found", 0);
            }
            if (j >= destCapacity) {
                continue;
            }
            dest[j] = b;
            if (caseFlags == null) {
                continue;
            }
            caseFlags[j] = isBasicUpperCase(b);
        }
        int n = 128;
        int i = 0;
        int bias = 72;
        int firstSupplementaryIndex = 1000000000;
        int in = (basicLength > 0) ? (basicLength + 1) : 0;
    Label_0161:
        while (in < srcLength) {
            final int oldi = i;
            int w = 1;
            int k = 36;
            while (in < srcLength) {
                final int digit = Punycode.basicToDigit[src.charAt(in++) & '\u00ff'];
                if (digit < 0) {
                    throw new StringPrepParseException("Invalid char found", 0);
                }
                if (digit > (Integer.MAX_VALUE - i) / w) {
                    throw new StringPrepParseException("Illegal char found", 1);
                }
                i += digit * w;
                int t = k - bias;
                if (t < 1) {
                    t = 1;
                }
                else if (k >= bias + 26) {
                    t = 26;
                }
                if (digit < t) {
                    ++destCPCount;
                    bias = adaptBias(i - oldi, destCPCount, oldi == 0);
                    if (i / destCPCount > Integer.MAX_VALUE - n) {
                        throw new StringPrepParseException("Illegal char found", 1);
                    }
                    n += i / destCPCount;
                    i %= destCPCount;
                    if (n > 1114111 || isSurrogate(n)) {
                        throw new StringPrepParseException("Illegal char found", 1);
                    }
                    final int cpLength = UTF16.getCharCount(n);
                    if (destLength + cpLength < destCapacity) {
                        int codeUnitIndex;
                        if (i <= firstSupplementaryIndex) {
                            codeUnitIndex = i;
                            if (cpLength > 1) {
                                firstSupplementaryIndex = codeUnitIndex;
                            }
                            else {
                                ++firstSupplementaryIndex;
                            }
                        }
                        else {
                            codeUnitIndex = firstSupplementaryIndex;
                            codeUnitIndex = UTF16.moveCodePointOffset(dest, 0, destLength, codeUnitIndex, i - codeUnitIndex);
                        }
                        if (codeUnitIndex < destLength) {
                            System.arraycopy(dest, codeUnitIndex, dest, codeUnitIndex + cpLength, destLength - codeUnitIndex);
                            if (caseFlags != null) {
                                System.arraycopy(caseFlags, codeUnitIndex, caseFlags, codeUnitIndex + cpLength, destLength - codeUnitIndex);
                            }
                        }
                        if (cpLength == 1) {
                            dest[codeUnitIndex] = (char)n;
                        }
                        else {
                            dest[codeUnitIndex] = UTF16.getLeadSurrogate(n);
                            dest[codeUnitIndex + 1] = UTF16.getTrailSurrogate(n);
                        }
                        if (caseFlags != null) {
                            caseFlags[codeUnitIndex] = isBasicUpperCase(src.charAt(in - 1));
                            if (cpLength == 2) {
                                caseFlags[codeUnitIndex + 1] = false;
                            }
                        }
                    }
                    destLength += cpLength;
                    ++i;
                    continue Label_0161;
                }
                else {
                    if (w > Integer.MAX_VALUE / (36 - t)) {
                        throw new StringPrepParseException("Illegal char found", 1);
                    }
                    w *= 36 - t;
                    k += 36;
                }
            }
            throw new StringPrepParseException("Illegal char found", 1);
        }
        result.append(dest, 0, destLength);
        return result;
    }
    
    static {
        basicToDigit = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
    }
}
