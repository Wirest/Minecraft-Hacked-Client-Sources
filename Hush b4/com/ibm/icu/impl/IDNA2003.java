// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import com.ibm.icu.text.StringPrepParseException;
import com.ibm.icu.text.UCharacterIterator;
import com.ibm.icu.text.StringPrep;

public final class IDNA2003
{
    private static char[] ACE_PREFIX;
    private static final int MAX_LABEL_LENGTH = 63;
    private static final int HYPHEN = 45;
    private static final int CAPITAL_A = 65;
    private static final int CAPITAL_Z = 90;
    private static final int LOWER_CASE_DELTA = 32;
    private static final int FULL_STOP = 46;
    private static final int MAX_DOMAIN_NAME_LENGTH = 255;
    private static final StringPrep namePrep;
    
    private static boolean startsWithPrefix(final StringBuffer src) {
        boolean startsWithPrefix = true;
        if (src.length() < IDNA2003.ACE_PREFIX.length) {
            return false;
        }
        for (int i = 0; i < IDNA2003.ACE_PREFIX.length; ++i) {
            if (toASCIILower(src.charAt(i)) != IDNA2003.ACE_PREFIX[i]) {
                startsWithPrefix = false;
            }
        }
        return startsWithPrefix;
    }
    
    private static char toASCIILower(final char ch) {
        if ('A' <= ch && ch <= 'Z') {
            return (char)(ch + ' ');
        }
        return ch;
    }
    
    private static StringBuffer toASCIILower(final CharSequence src) {
        final StringBuffer dest = new StringBuffer();
        for (int i = 0; i < src.length(); ++i) {
            dest.append(toASCIILower(src.charAt(i)));
        }
        return dest;
    }
    
    private static int compareCaseInsensitiveASCII(final StringBuffer s1, final StringBuffer s2) {
        for (int i = 0; i != s1.length(); ++i) {
            final char c1 = s1.charAt(i);
            final char c2 = s2.charAt(i);
            if (c1 != c2) {
                final int rc = toASCIILower(c1) - toASCIILower(c2);
                if (rc != 0) {
                    return rc;
                }
            }
        }
        return 0;
    }
    
    private static int getSeparatorIndex(final char[] src, int start, final int limit) {
        while (start < limit) {
            if (isLabelSeparator(src[start])) {
                return start;
            }
            ++start;
        }
        return start;
    }
    
    private static boolean isLDHChar(final int ch) {
        return ch <= 122 && (ch == 45 || (48 <= ch && ch <= 57) || (65 <= ch && ch <= 90) || (97 <= ch && ch <= 122));
    }
    
    private static boolean isLabelSeparator(final int ch) {
        switch (ch) {
            case 46:
            case 12290:
            case 65294:
            case 65377: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static StringBuffer convertToASCII(final UCharacterIterator src, final int options) throws StringPrepParseException {
        boolean[] caseFlags = null;
        boolean srcIsASCII = true;
        boolean srcIsLDH = true;
        final boolean useSTD3ASCIIRules = (options & 0x2) != 0x0;
        int ch;
        while ((ch = src.next()) != -1) {
            if (ch > 127) {
                srcIsASCII = false;
            }
        }
        int failPos = -1;
        src.setToStart();
        StringBuffer processOut = null;
        if (!srcIsASCII) {
            processOut = IDNA2003.namePrep.prepare(src, options);
        }
        else {
            processOut = new StringBuffer(src.getText());
        }
        final int poLen = processOut.length();
        if (poLen == 0) {
            throw new StringPrepParseException("Found zero length lable after NamePrep.", 10);
        }
        StringBuffer dest = new StringBuffer();
        srcIsASCII = true;
        for (int j = 0; j < poLen; ++j) {
            ch = processOut.charAt(j);
            if (ch > 127) {
                srcIsASCII = false;
            }
            else if (!isLDHChar(ch)) {
                srcIsLDH = false;
                failPos = j;
            }
        }
        if (useSTD3ASCIIRules && (!srcIsLDH || processOut.charAt(0) == '-' || processOut.charAt(processOut.length() - 1) == '-')) {
            if (!srcIsLDH) {
                throw new StringPrepParseException("The input does not conform to the STD 3 ASCII rules", 5, processOut.toString(), (failPos > 0) ? (failPos - 1) : failPos);
            }
            if (processOut.charAt(0) == '-') {
                throw new StringPrepParseException("The input does not conform to the STD 3 ASCII rules", 5, processOut.toString(), 0);
            }
            throw new StringPrepParseException("The input does not conform to the STD 3 ASCII rules", 5, processOut.toString(), (poLen > 0) ? (poLen - 1) : poLen);
        }
        else {
            if (srcIsASCII) {
                dest = processOut;
            }
            else {
                if (startsWithPrefix(processOut)) {
                    throw new StringPrepParseException("The input does not start with the ACE Prefix.", 6, processOut.toString(), 0);
                }
                caseFlags = new boolean[poLen];
                final StringBuilder punyout = Punycode.encode(processOut, caseFlags);
                final StringBuffer lowerOut = toASCIILower(punyout);
                dest.append(IDNA2003.ACE_PREFIX, 0, IDNA2003.ACE_PREFIX.length);
                dest.append(lowerOut);
            }
            if (dest.length() > 63) {
                throw new StringPrepParseException("The labels in the input are too long. Length > 63.", 8, dest.toString(), 0);
            }
            return dest;
        }
    }
    
    public static StringBuffer convertIDNToASCII(final String src, final int options) throws StringPrepParseException {
        final char[] srcArr = src.toCharArray();
        final StringBuffer result = new StringBuffer();
        int sepIndex = 0;
        int oldSepIndex = 0;
        while (true) {
            sepIndex = getSeparatorIndex(srcArr, sepIndex, srcArr.length);
            final String label = new String(srcArr, oldSepIndex, sepIndex - oldSepIndex);
            if (label.length() != 0 || sepIndex != srcArr.length) {
                final UCharacterIterator iter = UCharacterIterator.getInstance(label);
                result.append(convertToASCII(iter, options));
            }
            if (sepIndex == srcArr.length) {
                break;
            }
            oldSepIndex = ++sepIndex;
            result.append('.');
        }
        if (result.length() > 255) {
            throw new StringPrepParseException("The output exceed the max allowed length.", 11);
        }
        return result;
    }
    
    public static StringBuffer convertToUnicode(final UCharacterIterator src, final int options) throws StringPrepParseException {
        final boolean[] caseFlags = null;
        boolean srcIsASCII = true;
        final int saveIndex = src.getIndex();
        int ch;
        while ((ch = src.next()) != -1) {
            if (ch > 127) {
                srcIsASCII = false;
            }
        }
        StringBuffer processOut = null;
        Label_0083: {
            if (!srcIsASCII) {
                try {
                    src.setIndex(saveIndex);
                    processOut = IDNA2003.namePrep.prepare(src, options);
                    break Label_0083;
                }
                catch (StringPrepParseException ex) {
                    return new StringBuffer(src.getText());
                }
            }
            processOut = new StringBuffer(src.getText());
        }
        if (startsWithPrefix(processOut)) {
            StringBuffer decodeOut = null;
            final String temp = processOut.substring(IDNA2003.ACE_PREFIX.length, processOut.length());
            try {
                decodeOut = new StringBuffer(Punycode.decode(temp, caseFlags));
            }
            catch (StringPrepParseException e) {
                decodeOut = null;
            }
            if (decodeOut != null) {
                final StringBuffer toASCIIOut = convertToASCII(UCharacterIterator.getInstance(decodeOut), options);
                if (compareCaseInsensitiveASCII(processOut, toASCIIOut) != 0) {
                    decodeOut = null;
                }
            }
            if (decodeOut != null) {
                return decodeOut;
            }
        }
        return new StringBuffer(src.getText());
    }
    
    public static StringBuffer convertIDNToUnicode(final String src, final int options) throws StringPrepParseException {
        final char[] srcArr = src.toCharArray();
        final StringBuffer result = new StringBuffer();
        int sepIndex = 0;
        int oldSepIndex = 0;
        while (true) {
            sepIndex = getSeparatorIndex(srcArr, sepIndex, srcArr.length);
            final String label = new String(srcArr, oldSepIndex, sepIndex - oldSepIndex);
            if (label.length() == 0 && sepIndex != srcArr.length) {
                throw new StringPrepParseException("Found zero length lable after NamePrep.", 10);
            }
            final UCharacterIterator iter = UCharacterIterator.getInstance(label);
            result.append(convertToUnicode(iter, options));
            if (sepIndex == srcArr.length) {
                if (result.length() > 255) {
                    throw new StringPrepParseException("The output exceed the max allowed length.", 11);
                }
                return result;
            }
            else {
                result.append(srcArr[sepIndex]);
                oldSepIndex = ++sepIndex;
            }
        }
    }
    
    public static int compare(final String s1, final String s2, final int options) throws StringPrepParseException {
        final StringBuffer s1Out = convertIDNToASCII(s1, options);
        final StringBuffer s2Out = convertIDNToASCII(s2, options);
        return compareCaseInsensitiveASCII(s1Out, s2Out);
    }
    
    static {
        IDNA2003.ACE_PREFIX = new char[] { 'x', 'n', '-', '-' };
        namePrep = StringPrep.getInstance(0);
    }
}
