// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import java.util.regex.Pattern;
import org.apache.commons.codec.StringEncoder;

public class Nysiis implements StringEncoder
{
    private static final char[] CHARS_A;
    private static final char[] CHARS_AF;
    private static final char[] CHARS_C;
    private static final char[] CHARS_FF;
    private static final char[] CHARS_G;
    private static final char[] CHARS_N;
    private static final char[] CHARS_NN;
    private static final char[] CHARS_S;
    private static final char[] CHARS_SSS;
    private static final Pattern PAT_MAC;
    private static final Pattern PAT_KN;
    private static final Pattern PAT_K;
    private static final Pattern PAT_PH_PF;
    private static final Pattern PAT_SCH;
    private static final Pattern PAT_EE_IE;
    private static final Pattern PAT_DT_ETC;
    private static final char SPACE = ' ';
    private static final int TRUE_LENGTH = 6;
    private final boolean strict;
    
    private static boolean isVowel(final char c) {
        return c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U';
    }
    
    private static char[] transcodeRemaining(final char prev, final char curr, final char next, final char aNext) {
        if (curr == 'E' && next == 'V') {
            return Nysiis.CHARS_AF;
        }
        if (isVowel(curr)) {
            return Nysiis.CHARS_A;
        }
        if (curr == 'Q') {
            return Nysiis.CHARS_G;
        }
        if (curr == 'Z') {
            return Nysiis.CHARS_S;
        }
        if (curr == 'M') {
            return Nysiis.CHARS_N;
        }
        if (curr == 'K') {
            if (next == 'N') {
                return Nysiis.CHARS_NN;
            }
            return Nysiis.CHARS_C;
        }
        else {
            if (curr == 'S' && next == 'C' && aNext == 'H') {
                return Nysiis.CHARS_SSS;
            }
            if (curr == 'P' && next == 'H') {
                return Nysiis.CHARS_FF;
            }
            if (curr == 'H' && (!isVowel(prev) || !isVowel(next))) {
                return new char[] { prev };
            }
            if (curr == 'W' && isVowel(prev)) {
                return new char[] { prev };
            }
            return new char[] { curr };
        }
    }
    
    public Nysiis() {
        this(true);
    }
    
    public Nysiis(final boolean strict) {
        this.strict = strict;
    }
    
    @Override
    public Object encode(final Object obj) throws EncoderException {
        if (!(obj instanceof String)) {
            throw new EncoderException("Parameter supplied to Nysiis encode is not of type java.lang.String");
        }
        return this.nysiis((String)obj);
    }
    
    @Override
    public String encode(final String str) {
        return this.nysiis(str);
    }
    
    public boolean isStrict() {
        return this.strict;
    }
    
    public String nysiis(String str) {
        if (str == null) {
            return null;
        }
        str = SoundexUtils.clean(str);
        if (str.length() == 0) {
            return str;
        }
        str = Nysiis.PAT_MAC.matcher(str).replaceFirst("MCC");
        str = Nysiis.PAT_KN.matcher(str).replaceFirst("NN");
        str = Nysiis.PAT_K.matcher(str).replaceFirst("C");
        str = Nysiis.PAT_PH_PF.matcher(str).replaceFirst("FF");
        str = Nysiis.PAT_SCH.matcher(str).replaceFirst("SSS");
        str = Nysiis.PAT_EE_IE.matcher(str).replaceFirst("Y");
        str = Nysiis.PAT_DT_ETC.matcher(str).replaceFirst("D");
        final StringBuilder key = new StringBuilder(str.length());
        key.append(str.charAt(0));
        final char[] chars = str.toCharArray();
        for (int len = chars.length, i = 1; i < len; ++i) {
            final char next = (i < len - 1) ? chars[i + 1] : ' ';
            final char aNext = (i < len - 2) ? chars[i + 2] : ' ';
            final char[] transcoded = transcodeRemaining(chars[i - 1], chars[i], next, aNext);
            System.arraycopy(transcoded, 0, chars, i, transcoded.length);
            if (chars[i] != chars[i - 1]) {
                key.append(chars[i]);
            }
        }
        if (key.length() > 1) {
            char lastChar = key.charAt(key.length() - 1);
            if (lastChar == 'S') {
                key.deleteCharAt(key.length() - 1);
                lastChar = key.charAt(key.length() - 1);
            }
            if (key.length() > 2) {
                final char last2Char = key.charAt(key.length() - 2);
                if (last2Char == 'A' && lastChar == 'Y') {
                    key.deleteCharAt(key.length() - 2);
                }
            }
            if (lastChar == 'A') {
                key.deleteCharAt(key.length() - 1);
            }
        }
        final String string = key.toString();
        return this.isStrict() ? string.substring(0, Math.min(6, string.length())) : string;
    }
    
    static {
        CHARS_A = new char[] { 'A' };
        CHARS_AF = new char[] { 'A', 'F' };
        CHARS_C = new char[] { 'C' };
        CHARS_FF = new char[] { 'F', 'F' };
        CHARS_G = new char[] { 'G' };
        CHARS_N = new char[] { 'N' };
        CHARS_NN = new char[] { 'N', 'N' };
        CHARS_S = new char[] { 'S' };
        CHARS_SSS = new char[] { 'S', 'S', 'S' };
        PAT_MAC = Pattern.compile("^MAC");
        PAT_KN = Pattern.compile("^KN");
        PAT_K = Pattern.compile("^K");
        PAT_PH_PF = Pattern.compile("^(PH|PF)");
        PAT_SCH = Pattern.compile("^SCH");
        PAT_EE_IE = Pattern.compile("(EE|IE)$");
        PAT_DT_ETC = Pattern.compile("(DT|RT|RD|NT|ND)$");
    }
}
