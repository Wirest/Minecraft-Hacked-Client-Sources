// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.lang.UCharacter;

final class BidiWriter
{
    static final char LRM_CHAR = '\u200e';
    static final char RLM_CHAR = '\u200f';
    static final int MASK_R_AL = 8194;
    
    private static boolean IsCombining(final int type) {
        return (1 << type & 0x1C0) != 0x0;
    }
    
    private static String doWriteForward(final String src, final int options) {
        switch (options & 0xA) {
            case 0: {
                return src;
            }
            case 2: {
                final StringBuffer dest = new StringBuffer(src.length());
                int i = 0;
                do {
                    final int c = UTF16.charAt(src, i);
                    i += UTF16.getCharCount(c);
                    UTF16.append(dest, UCharacter.getMirror(c));
                } while (i < src.length());
                return dest.toString();
            }
            case 8: {
                final StringBuilder dest2 = new StringBuilder(src.length());
                int i = 0;
                do {
                    final char c2 = src.charAt(i++);
                    if (!Bidi.IsBidiControlChar(c2)) {
                        dest2.append(c2);
                    }
                } while (i < src.length());
                return dest2.toString();
            }
            default: {
                final StringBuffer dest = new StringBuffer(src.length());
                int i = 0;
                do {
                    final int c = UTF16.charAt(src, i);
                    i += UTF16.getCharCount(c);
                    if (!Bidi.IsBidiControlChar(c)) {
                        UTF16.append(dest, UCharacter.getMirror(c));
                    }
                } while (i < src.length());
                return dest.toString();
            }
        }
    }
    
    private static String doWriteForward(final char[] text, final int start, final int limit, final int options) {
        return doWriteForward(new String(text, start, limit - start), options);
    }
    
    static String writeReverse(final String src, final int options) {
        final StringBuffer dest = new StringBuffer(src.length());
        switch (options & 0xB) {
            case 0: {
                int srcLength = src.length();
                do {
                    final int i = srcLength;
                    srcLength -= UTF16.getCharCount(UTF16.charAt(src, srcLength - 1));
                    dest.append(src.substring(srcLength, i));
                } while (srcLength > 0);
                break;
            }
            case 1: {
                int srcLength = src.length();
                do {
                    final int j = srcLength;
                    int c;
                    do {
                        c = UTF16.charAt(src, srcLength - 1);
                        srcLength -= UTF16.getCharCount(c);
                    } while (srcLength > 0 && IsCombining(UCharacter.getType(c)));
                    dest.append(src.substring(srcLength, j));
                } while (srcLength > 0);
                break;
            }
            default: {
                int srcLength = src.length();
                do {
                    final int i = srcLength;
                    int c2 = UTF16.charAt(src, srcLength - 1);
                    srcLength -= UTF16.getCharCount(c2);
                    if ((options & 0x1) != 0x0) {
                        while (srcLength > 0 && IsCombining(UCharacter.getType(c2))) {
                            c2 = UTF16.charAt(src, srcLength - 1);
                            srcLength -= UTF16.getCharCount(c2);
                        }
                    }
                    if ((options & 0x8) != 0x0 && Bidi.IsBidiControlChar(c2)) {
                        continue;
                    }
                    int k = srcLength;
                    if ((options & 0x2) != 0x0) {
                        c2 = UCharacter.getMirror(c2);
                        UTF16.append(dest, c2);
                        k += UTF16.getCharCount(c2);
                    }
                    dest.append(src.substring(k, i));
                } while (srcLength > 0);
                break;
            }
        }
        return dest.toString();
    }
    
    static String doWriteReverse(final char[] text, final int start, final int limit, final int options) {
        return writeReverse(new String(text, start, limit - start), options);
    }
    
    static String writeReordered(final Bidi bidi, int options) {
        final char[] text = bidi.text;
        final int runCount = bidi.countRuns();
        if ((bidi.reorderingOptions & 0x1) != 0x0) {
            options |= 0x4;
            options &= 0xFFFFFFF7;
        }
        if ((bidi.reorderingOptions & 0x2) != 0x0) {
            options |= 0x8;
            options &= 0xFFFFFFFB;
        }
        if (bidi.reorderingMode != 4 && bidi.reorderingMode != 5 && bidi.reorderingMode != 6 && bidi.reorderingMode != 3) {
            options &= 0xFFFFFFFB;
        }
        final StringBuilder dest = new StringBuilder(((options & 0x4) != 0x0) ? (bidi.length * 2) : bidi.length);
        if ((options & 0x10) == 0x0) {
            if ((options & 0x4) == 0x0) {
                for (int run = 0; run < runCount; ++run) {
                    final BidiRun bidiRun = bidi.getVisualRun(run);
                    if (bidiRun.isEvenRun()) {
                        dest.append(doWriteForward(text, bidiRun.start, bidiRun.limit, options & 0xFFFFFFFD));
                    }
                    else {
                        dest.append(doWriteReverse(text, bidiRun.start, bidiRun.limit, options));
                    }
                }
            }
            else {
                final byte[] dirProps = bidi.dirProps;
                for (int run = 0; run < runCount; ++run) {
                    final BidiRun bidiRun2 = bidi.getVisualRun(run);
                    int markFlag = 0;
                    markFlag = bidi.runs[run].insertRemove;
                    if (markFlag < 0) {
                        markFlag = 0;
                    }
                    if (bidiRun2.isEvenRun()) {
                        if (bidi.isInverse() && dirProps[bidiRun2.start] != 0) {
                            markFlag |= 0x1;
                        }
                        char uc;
                        if ((markFlag & 0x1) != 0x0) {
                            uc = '\u200e';
                        }
                        else if ((markFlag & 0x4) != 0x0) {
                            uc = '\u200f';
                        }
                        else {
                            uc = '\0';
                        }
                        if (uc != '\0') {
                            dest.append(uc);
                        }
                        dest.append(doWriteForward(text, bidiRun2.start, bidiRun2.limit, options & 0xFFFFFFFD));
                        if (bidi.isInverse() && dirProps[bidiRun2.limit - 1] != 0) {
                            markFlag |= 0x2;
                        }
                        if ((markFlag & 0x2) != 0x0) {
                            uc = '\u200e';
                        }
                        else if ((markFlag & 0x8) != 0x0) {
                            uc = '\u200f';
                        }
                        else {
                            uc = '\0';
                        }
                        if (uc != '\0') {
                            dest.append(uc);
                        }
                    }
                    else {
                        if (bidi.isInverse() && !bidi.testDirPropFlagAt(8194, bidiRun2.limit - 1)) {
                            markFlag |= 0x4;
                        }
                        char uc;
                        if ((markFlag & 0x1) != 0x0) {
                            uc = '\u200e';
                        }
                        else if ((markFlag & 0x4) != 0x0) {
                            uc = '\u200f';
                        }
                        else {
                            uc = '\0';
                        }
                        if (uc != '\0') {
                            dest.append(uc);
                        }
                        dest.append(doWriteReverse(text, bidiRun2.start, bidiRun2.limit, options));
                        if (bidi.isInverse() && (0x2002 & Bidi.DirPropFlag(dirProps[bidiRun2.start])) == 0x0) {
                            markFlag |= 0x8;
                        }
                        if ((markFlag & 0x2) != 0x0) {
                            uc = '\u200e';
                        }
                        else if ((markFlag & 0x8) != 0x0) {
                            uc = '\u200f';
                        }
                        else {
                            uc = '\0';
                        }
                        if (uc != '\0') {
                            dest.append(uc);
                        }
                    }
                }
            }
        }
        else if ((options & 0x4) == 0x0) {
            int run = runCount;
            while (--run >= 0) {
                final BidiRun bidiRun = bidi.getVisualRun(run);
                if (bidiRun.isEvenRun()) {
                    dest.append(doWriteReverse(text, bidiRun.start, bidiRun.limit, options & 0xFFFFFFFD));
                }
                else {
                    dest.append(doWriteForward(text, bidiRun.start, bidiRun.limit, options));
                }
            }
        }
        else {
            final byte[] dirProps = bidi.dirProps;
            int run = runCount;
            while (--run >= 0) {
                final BidiRun bidiRun3 = bidi.getVisualRun(run);
                if (bidiRun3.isEvenRun()) {
                    if (dirProps[bidiRun3.limit - 1] != 0) {
                        dest.append('\u200e');
                    }
                    dest.append(doWriteReverse(text, bidiRun3.start, bidiRun3.limit, options & 0xFFFFFFFD));
                    if (dirProps[bidiRun3.start] == 0) {
                        continue;
                    }
                    dest.append('\u200e');
                }
                else {
                    if ((0x2002 & Bidi.DirPropFlag(dirProps[bidiRun3.start])) == 0x0) {
                        dest.append('\u200f');
                    }
                    dest.append(doWriteForward(text, bidiRun3.start, bidiRun3.limit, options));
                    if ((0x2002 & Bidi.DirPropFlag(dirProps[bidiRun3.limit - 1])) != 0x0) {
                        continue;
                    }
                    dest.append('\u200f');
                }
            }
        }
        return dest.toString();
    }
}
