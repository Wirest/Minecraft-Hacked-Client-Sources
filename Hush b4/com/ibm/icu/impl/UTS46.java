// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.io.InputStream;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UScript;
import com.ibm.icu.text.StringPrepParseException;
import java.util.EnumSet;
import com.ibm.icu.text.Normalizer2;
import com.ibm.icu.text.IDNA;

public final class UTS46 extends IDNA
{
    private static final Normalizer2 uts46Norm2;
    final int options;
    private static final EnumSet<Error> severeErrors;
    private static final byte[] asciiData;
    private static final int L_MASK;
    private static final int R_AL_MASK;
    private static final int L_R_AL_MASK;
    private static final int R_AL_AN_MASK;
    private static final int EN_AN_MASK;
    private static final int R_AL_EN_AN_MASK;
    private static final int L_EN_MASK;
    private static final int ES_CS_ET_ON_BN_NSM_MASK;
    private static final int L_EN_ES_CS_ET_ON_BN_NSM_MASK;
    private static final int R_AL_AN_EN_ES_CS_ET_ON_BN_NSM_MASK;
    private static int U_GC_M_MASK;
    
    public UTS46(final int options) {
        this.options = options;
    }
    
    @Override
    public StringBuilder labelToASCII(final CharSequence label, final StringBuilder dest, final Info info) {
        return this.process(label, true, true, dest, info);
    }
    
    @Override
    public StringBuilder labelToUnicode(final CharSequence label, final StringBuilder dest, final Info info) {
        return this.process(label, true, false, dest, info);
    }
    
    @Override
    public StringBuilder nameToASCII(final CharSequence name, final StringBuilder dest, final Info info) {
        this.process(name, false, true, dest, info);
        if (dest.length() >= 254 && !info.getErrors().contains(Error.DOMAIN_NAME_TOO_LONG) && isASCIIString(dest) && (dest.length() > 254 || dest.charAt(253) != '.')) {
            IDNA.addError(info, Error.DOMAIN_NAME_TOO_LONG);
        }
        return dest;
    }
    
    @Override
    public StringBuilder nameToUnicode(final CharSequence name, final StringBuilder dest, final Info info) {
        return this.process(name, false, false, dest, info);
    }
    
    private static boolean isASCIIString(final CharSequence dest) {
        for (int length = dest.length(), i = 0; i < length; ++i) {
            if (dest.charAt(i) > '\u007f') {
                return false;
            }
        }
        return true;
    }
    
    private StringBuilder process(final CharSequence src, final boolean isLabel, final boolean toASCII, final StringBuilder dest, final Info info) {
        if (dest == src) {
            throw new IllegalArgumentException();
        }
        dest.delete(0, Integer.MAX_VALUE);
        IDNA.resetInfo(info);
        final int srcLength = src.length();
        if (srcLength == 0) {
            if (toASCII) {
                IDNA.addError(info, Error.EMPTY_LABEL);
            }
            return dest;
        }
        final boolean disallowNonLDHDot = (this.options & 0x2) != 0x0;
        int labelStart = 0;
        int i = 0;
        while (i != srcLength) {
            final char c = src.charAt(i);
            Label_0389: {
                if (c <= '\u007f') {
                    final int cData = UTS46.asciiData[c];
                    if (cData > 0) {
                        dest.append((char)(c + ' '));
                    }
                    else {
                        if (cData < 0 && disallowNonLDHDot) {
                            break Label_0389;
                        }
                        dest.append(c);
                        if (c == '-') {
                            if (i == labelStart + 3 && src.charAt(i - 1) == '-') {
                                ++i;
                                break Label_0389;
                            }
                            if (i == labelStart) {
                                IDNA.addLabelError(info, Error.LEADING_HYPHEN);
                            }
                            if (i + 1 == srcLength || src.charAt(i + 1) == '.') {
                                IDNA.addLabelError(info, Error.TRAILING_HYPHEN);
                            }
                        }
                        else if (c == '.') {
                            if (isLabel) {
                                ++i;
                                break Label_0389;
                            }
                            if (toASCII) {
                                if (i == labelStart && i < srcLength - 1) {
                                    IDNA.addLabelError(info, Error.EMPTY_LABEL);
                                }
                                else if (i - labelStart > 63) {
                                    IDNA.addLabelError(info, Error.LABEL_TOO_LONG);
                                }
                            }
                            IDNA.promoteAndResetLabelErrors(info);
                            labelStart = i + 1;
                        }
                    }
                    ++i;
                    continue;
                }
            }
            IDNA.promoteAndResetLabelErrors(info);
            this.processUnicode(src, labelStart, i, isLabel, toASCII, dest, info);
            if (IDNA.isBiDi(info) && !IDNA.hasCertainErrors(info, UTS46.severeErrors) && (!IDNA.isOkBiDi(info) || (labelStart > 0 && !isASCIIOkBiDi(dest, labelStart)))) {
                IDNA.addError(info, Error.BIDI);
            }
            return dest;
        }
        if (toASCII) {
            if (i - labelStart > 63) {
                IDNA.addLabelError(info, Error.LABEL_TOO_LONG);
            }
            if (!isLabel && i >= 254 && (i > 254 || labelStart < i)) {
                IDNA.addError(info, Error.DOMAIN_NAME_TOO_LONG);
            }
        }
        IDNA.promoteAndResetLabelErrors(info);
        return dest;
    }
    
    private StringBuilder processUnicode(final CharSequence src, int labelStart, final int mappingStart, final boolean isLabel, final boolean toASCII, final StringBuilder dest, final Info info) {
        if (mappingStart == 0) {
            UTS46.uts46Norm2.normalize(src, dest);
        }
        else {
            UTS46.uts46Norm2.normalizeSecondAndAppend(dest, src.subSequence(mappingStart, src.length()));
        }
        boolean doMapDevChars = toASCII ? ((this.options & 0x10) == 0x0) : ((this.options & 0x20) == 0x0);
        int destLength = dest.length();
        int labelLimit = labelStart;
        while (labelLimit < destLength) {
            final char c = dest.charAt(labelLimit);
            if (c == '.' && !isLabel) {
                final int labelLength = labelLimit - labelStart;
                final int newLength = this.processLabel(dest, labelStart, labelLength, toASCII, info);
                IDNA.promoteAndResetLabelErrors(info);
                destLength += newLength - labelLength;
                labelStart = (labelLimit = labelStart + (newLength + 1));
            }
            else if ('\u00df' <= c && c <= '\u200d' && (c == '\u00df' || c == '\u03c2' || c >= '\u200c')) {
                IDNA.setTransitionalDifferent(info);
                if (doMapDevChars) {
                    destLength = this.mapDevChars(dest, labelStart, labelLimit);
                    doMapDevChars = false;
                }
                else {
                    ++labelLimit;
                }
            }
            else {
                ++labelLimit;
            }
        }
        if (0 == labelStart || labelStart < labelLimit) {
            this.processLabel(dest, labelStart, labelLimit - labelStart, toASCII, info);
            IDNA.promoteAndResetLabelErrors(info);
        }
        return dest;
    }
    
    private int mapDevChars(final StringBuilder dest, final int labelStart, final int mappingStart) {
        int length = dest.length();
        boolean didMapDevChars = false;
        int i = mappingStart;
        while (i < length) {
            final char c = dest.charAt(i);
            switch (c) {
                case '\u00df': {
                    didMapDevChars = true;
                    dest.setCharAt(i++, 's');
                    dest.insert(i++, 's');
                    ++length;
                    continue;
                }
                case '\u03c2': {
                    didMapDevChars = true;
                    dest.setCharAt(i++, '\u03c3');
                    continue;
                }
                case '\u200c':
                case '\u200d': {
                    didMapDevChars = true;
                    dest.delete(i, i + 1);
                    --length;
                    continue;
                }
                default: {
                    ++i;
                    continue;
                }
            }
        }
        if (didMapDevChars) {
            final String normalized = UTS46.uts46Norm2.normalize(dest.subSequence(labelStart, dest.length()));
            dest.replace(labelStart, Integer.MAX_VALUE, normalized);
            return dest.length();
        }
        return length;
    }
    
    private static boolean isNonASCIIDisallowedSTD3Valid(final int c) {
        return c == 8800 || c == 8814 || c == 8815;
    }
    
    private static int replaceLabel(final StringBuilder dest, final int destLabelStart, final int destLabelLength, final CharSequence label, final int labelLength) {
        if (label != dest) {
            dest.delete(destLabelStart, destLabelStart + destLabelLength).insert(destLabelStart, label);
        }
        return labelLength;
    }
    
    private int processLabel(final StringBuilder dest, int labelStart, int labelLength, final boolean toASCII, final Info info) {
        final int destLabelStart = labelStart;
        int destLabelLength = labelLength;
        boolean wasPunycode;
        StringBuilder labelString;
        if (labelLength >= 4 && dest.charAt(labelStart) == 'x' && dest.charAt(labelStart + 1) == 'n' && dest.charAt(labelStart + 2) == '-' && dest.charAt(labelStart + 3) == '-') {
            wasPunycode = true;
            StringBuilder fromPunycode;
            try {
                fromPunycode = Punycode.decode(dest.subSequence(labelStart + 4, labelStart + labelLength), null);
            }
            catch (StringPrepParseException e2) {
                IDNA.addLabelError(info, Error.PUNYCODE);
                return this.markBadACELabel(dest, labelStart, labelLength, toASCII, info);
            }
            final boolean isValid = UTS46.uts46Norm2.isNormalized(fromPunycode);
            if (!isValid) {
                IDNA.addLabelError(info, Error.INVALID_ACE_LABEL);
                return this.markBadACELabel(dest, labelStart, labelLength, toASCII, info);
            }
            labelString = fromPunycode;
            labelStart = 0;
            labelLength = fromPunycode.length();
        }
        else {
            wasPunycode = false;
            labelString = dest;
        }
        if (labelLength == 0) {
            if (toASCII) {
                IDNA.addLabelError(info, Error.EMPTY_LABEL);
            }
            return replaceLabel(dest, destLabelStart, destLabelLength, labelString, labelLength);
        }
        if (labelLength >= 4 && labelString.charAt(labelStart + 2) == '-' && labelString.charAt(labelStart + 3) == '-') {
            IDNA.addLabelError(info, Error.HYPHEN_3_4);
        }
        if (labelString.charAt(labelStart) == '-') {
            IDNA.addLabelError(info, Error.LEADING_HYPHEN);
        }
        if (labelString.charAt(labelStart + labelLength - 1) == '-') {
            IDNA.addLabelError(info, Error.TRAILING_HYPHEN);
        }
        int i = labelStart;
        final int limit = labelStart + labelLength;
        char oredChars = '\0';
        final boolean disallowNonLDHDot = (this.options & 0x2) != 0x0;
        do {
            final char c = labelString.charAt(i);
            if (c <= '\u007f') {
                if (c == '.') {
                    IDNA.addLabelError(info, Error.LABEL_HAS_DOT);
                    labelString.setCharAt(i, '\ufffd');
                }
                else {
                    if (!disallowNonLDHDot || UTS46.asciiData[c] >= 0) {
                        continue;
                    }
                    IDNA.addLabelError(info, Error.DISALLOWED);
                    labelString.setCharAt(i, '\ufffd');
                }
            }
            else {
                oredChars |= c;
                if (disallowNonLDHDot && isNonASCIIDisallowedSTD3Valid(c)) {
                    IDNA.addLabelError(info, Error.DISALLOWED);
                    labelString.setCharAt(i, '\ufffd');
                }
                else {
                    if (c != '\ufffd') {
                        continue;
                    }
                    IDNA.addLabelError(info, Error.DISALLOWED);
                }
            }
        } while (++i < limit);
        final int c2 = labelString.codePointAt(labelStart);
        if ((U_GET_GC_MASK(c2) & UTS46.U_GC_M_MASK) != 0x0) {
            IDNA.addLabelError(info, Error.LEADING_COMBINING_MARK);
            labelString.setCharAt(labelStart, '\ufffd');
            if (c2 > 65535) {
                labelString.deleteCharAt(labelStart + 1);
                --labelLength;
                if (labelString == dest) {
                    --destLabelLength;
                }
            }
        }
        if (!IDNA.hasCertainLabelErrors(info, UTS46.severeErrors)) {
            if ((this.options & 0x4) != 0x0 && (!IDNA.isBiDi(info) || IDNA.isOkBiDi(info))) {
                this.checkLabelBiDi(labelString, labelStart, labelLength, info);
            }
            if ((this.options & 0x8) != 0x0 && (oredChars & '\u200c') == 0x200C && !this.isLabelOkContextJ(labelString, labelStart, labelLength)) {
                IDNA.addLabelError(info, Error.CONTEXTJ);
            }
            if ((this.options & 0x40) != 0x0 && oredChars >= '·') {
                this.checkLabelContextO(labelString, labelStart, labelLength, info);
            }
            if (toASCII) {
                if (wasPunycode) {
                    if (destLabelLength > 63) {
                        IDNA.addLabelError(info, Error.LABEL_TOO_LONG);
                    }
                    return destLabelLength;
                }
                if (oredChars >= '\u0080') {
                    StringBuilder punycode;
                    try {
                        punycode = Punycode.encode(labelString.subSequence(labelStart, labelStart + labelLength), null);
                    }
                    catch (StringPrepParseException e) {
                        throw new RuntimeException(e);
                    }
                    punycode.insert(0, "xn--");
                    if (punycode.length() > 63) {
                        IDNA.addLabelError(info, Error.LABEL_TOO_LONG);
                    }
                    return replaceLabel(dest, destLabelStart, destLabelLength, punycode, punycode.length());
                }
                if (labelLength > 63) {
                    IDNA.addLabelError(info, Error.LABEL_TOO_LONG);
                }
            }
        }
        else if (wasPunycode) {
            IDNA.addLabelError(info, Error.INVALID_ACE_LABEL);
            return this.markBadACELabel(dest, destLabelStart, destLabelLength, toASCII, info);
        }
        return replaceLabel(dest, destLabelStart, destLabelLength, labelString, labelLength);
    }
    
    private int markBadACELabel(final StringBuilder dest, final int labelStart, int labelLength, final boolean toASCII, final Info info) {
        final boolean disallowNonLDHDot = (this.options & 0x2) != 0x0;
        boolean isASCII = true;
        boolean onlyLDH = true;
        int i = labelStart + 4;
        final int limit = labelStart + labelLength;
        do {
            final char c = dest.charAt(i);
            if (c <= '\u007f') {
                if (c == '.') {
                    IDNA.addLabelError(info, Error.LABEL_HAS_DOT);
                    dest.setCharAt(i, '\ufffd');
                    onlyLDH = (isASCII = false);
                }
                else {
                    if (UTS46.asciiData[c] >= 0) {
                        continue;
                    }
                    onlyLDH = false;
                    if (!disallowNonLDHDot) {
                        continue;
                    }
                    dest.setCharAt(i, '\ufffd');
                    isASCII = false;
                }
            }
            else {
                onlyLDH = (isASCII = false);
            }
        } while (++i < limit);
        if (onlyLDH) {
            dest.insert(labelStart + labelLength, '\ufffd');
            ++labelLength;
        }
        else if (toASCII && isASCII && labelLength > 63) {
            IDNA.addLabelError(info, Error.LABEL_TOO_LONG);
        }
        return labelLength;
    }
    
    private void checkLabelBiDi(final CharSequence label, final int labelStart, final int labelLength, final Info info) {
        int i = labelStart;
        int c = Character.codePointAt(label, i);
        i += Character.charCount(c);
        final int firstMask = U_MASK(UBiDiProps.INSTANCE.getClass(c));
        if ((firstMask & ~UTS46.L_R_AL_MASK) != 0x0) {
            IDNA.setNotOkBiDi(info);
        }
        int labelLimit = labelStart + labelLength;
        while (true) {
            while (i < labelLimit) {
                c = Character.codePointBefore(label, labelLimit);
                labelLimit -= Character.charCount(c);
                final int dir = UBiDiProps.INSTANCE.getClass(c);
                if (dir != 17) {
                    final int lastMask = U_MASK(dir);
                    Label_0156: {
                        if ((firstMask & UTS46.L_MASK) != 0x0) {
                            if ((lastMask & ~UTS46.L_EN_MASK) == 0x0) {
                                break Label_0156;
                            }
                        }
                        else if ((lastMask & ~UTS46.R_AL_EN_AN_MASK) == 0x0) {
                            break Label_0156;
                        }
                        IDNA.setNotOkBiDi(info);
                    }
                    int mask;
                    for (mask = 0; i < labelLimit; i += Character.charCount(c), mask |= U_MASK(UBiDiProps.INSTANCE.getClass(c))) {
                        c = Character.codePointAt(label, i);
                    }
                    if ((firstMask & UTS46.L_MASK) != 0x0) {
                        if ((mask & ~UTS46.L_EN_ES_CS_ET_ON_BN_NSM_MASK) != 0x0) {
                            IDNA.setNotOkBiDi(info);
                        }
                    }
                    else {
                        if ((mask & ~UTS46.R_AL_AN_EN_ES_CS_ET_ON_BN_NSM_MASK) != 0x0) {
                            IDNA.setNotOkBiDi(info);
                        }
                        if ((mask & UTS46.EN_AN_MASK) == UTS46.EN_AN_MASK) {
                            IDNA.setNotOkBiDi(info);
                        }
                    }
                    if (((firstMask | mask | lastMask) & UTS46.R_AL_AN_MASK) != 0x0) {
                        IDNA.setBiDi(info);
                    }
                    return;
                }
            }
            final int lastMask = firstMask;
            continue;
        }
    }
    
    private static boolean isASCIIOkBiDi(final CharSequence s, final int length) {
        int labelStart = 0;
        for (int i = 0; i < length; ++i) {
            char c = s.charAt(i);
            if (c == '.') {
                if (i > labelStart) {
                    c = s.charAt(i - 1);
                    if (('a' > c || c > 'z') && ('0' > c || c > '9')) {
                        return false;
                    }
                }
                labelStart = i + 1;
            }
            else if (i == labelStart) {
                if ('a' > c || c > 'z') {
                    return false;
                }
            }
            else if (c <= ' ' && (c >= '\u001c' || ('\t' <= c && c <= '\r'))) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isLabelOkContextJ(final CharSequence label, final int labelStart, final int labelLength) {
    Label_0259:
        for (int labelLimit = labelStart + labelLength, i = labelStart; i < labelLimit; ++i) {
            if (label.charAt(i) == '\u200c') {
                if (i == labelStart) {
                    return false;
                }
                int j = i;
                int c = Character.codePointBefore(label, j);
                j -= Character.charCount(c);
                if (UTS46.uts46Norm2.getCombiningClass(c) != 9) {
                    while (true) {
                        int type = UBiDiProps.INSTANCE.getJoiningType(c);
                        if (type == 5) {
                            if (j == 0) {
                                return false;
                            }
                            c = Character.codePointBefore(label, j);
                            j -= Character.charCount(c);
                        }
                        else {
                            if (type != 3 && type != 2) {
                                return false;
                            }
                            j = i + 1;
                            while (j != labelLimit) {
                                c = Character.codePointAt(label, j);
                                j += Character.charCount(c);
                                type = UBiDiProps.INSTANCE.getJoiningType(c);
                                if (type == 5) {
                                    continue;
                                }
                                if (type != 4 && type != 2) {
                                    return false;
                                }
                                continue Label_0259;
                            }
                            return false;
                        }
                    }
                }
            }
            else if (label.charAt(i) == '\u200d') {
                if (i == labelStart) {
                    return false;
                }
                final int c = Character.codePointBefore(label, i);
                if (UTS46.uts46Norm2.getCombiningClass(c) != 9) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private void checkLabelContextO(final CharSequence label, final int labelStart, final int labelLength, final Info info) {
        final int labelEnd = labelStart + labelLength - 1;
        int arabicDigits = 0;
    Label_0343:
        for (int i = labelStart; i <= labelEnd; ++i) {
            int c = label.charAt(i);
            if (c >= 183) {
                if (c <= 1785) {
                    if (c == 183) {
                        if (labelStart >= i || label.charAt(i - 1) != 'l' || i >= labelEnd || label.charAt(i + 1) != 'l') {
                            IDNA.addLabelError(info, Error.CONTEXTO_PUNCTUATION);
                        }
                    }
                    else if (c == 885) {
                        if (i >= labelEnd || 14 != UScript.getScript(Character.codePointAt(label, i + 1))) {
                            IDNA.addLabelError(info, Error.CONTEXTO_PUNCTUATION);
                        }
                    }
                    else if (c == 1523 || c == 1524) {
                        if (labelStart >= i || 19 != UScript.getScript(Character.codePointBefore(label, i))) {
                            IDNA.addLabelError(info, Error.CONTEXTO_PUNCTUATION);
                        }
                    }
                    else if (1632 <= c) {
                        if (c <= 1641) {
                            if (arabicDigits > 0) {
                                IDNA.addLabelError(info, Error.CONTEXTO_DIGITS);
                            }
                            arabicDigits = -1;
                        }
                        else if (1776 <= c) {
                            if (arabicDigits < 0) {
                                IDNA.addLabelError(info, Error.CONTEXTO_DIGITS);
                            }
                            arabicDigits = 1;
                        }
                    }
                }
                else if (c == 12539) {
                    for (int j = labelStart; j <= labelEnd; j += Character.charCount(c)) {
                        c = Character.codePointAt(label, j);
                        final int script = UScript.getScript(c);
                        if (script == 20 || script == 22) {
                            continue Label_0343;
                        }
                        if (script == 17) {
                            continue Label_0343;
                        }
                    }
                    IDNA.addLabelError(info, Error.CONTEXTO_PUNCTUATION);
                }
            }
        }
    }
    
    private static int U_MASK(final int x) {
        return 1 << x;
    }
    
    private static int U_GET_GC_MASK(final int c) {
        return 1 << UCharacter.getType(c);
    }
    
    static {
        uts46Norm2 = Normalizer2.getInstance(null, "uts46", Normalizer2.Mode.COMPOSE);
        severeErrors = EnumSet.of(Error.LEADING_COMBINING_MARK, Error.DISALLOWED, Error.PUNYCODE, Error.LABEL_HAS_DOT, Error.INVALID_ACE_LABEL);
        asciiData = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1 };
        L_MASK = U_MASK(0);
        R_AL_MASK = (U_MASK(1) | U_MASK(13));
        L_R_AL_MASK = (UTS46.L_MASK | UTS46.R_AL_MASK);
        R_AL_AN_MASK = (UTS46.R_AL_MASK | U_MASK(5));
        EN_AN_MASK = (U_MASK(2) | U_MASK(5));
        R_AL_EN_AN_MASK = (UTS46.R_AL_MASK | UTS46.EN_AN_MASK);
        L_EN_MASK = (UTS46.L_MASK | U_MASK(2));
        ES_CS_ET_ON_BN_NSM_MASK = (U_MASK(3) | U_MASK(6) | U_MASK(4) | U_MASK(10) | U_MASK(18) | U_MASK(17));
        L_EN_ES_CS_ET_ON_BN_NSM_MASK = (UTS46.L_EN_MASK | UTS46.ES_CS_ET_ON_BN_NSM_MASK);
        R_AL_AN_EN_ES_CS_ET_ON_BN_NSM_MASK = (UTS46.R_AL_MASK | UTS46.EN_AN_MASK | UTS46.ES_CS_ET_ON_BN_NSM_MASK);
        UTS46.U_GC_M_MASK = (U_MASK(6) | U_MASK(7) | U_MASK(8));
    }
}
