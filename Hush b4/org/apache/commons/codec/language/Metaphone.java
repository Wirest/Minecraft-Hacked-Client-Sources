// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import java.util.Locale;
import org.apache.commons.codec.StringEncoder;

public class Metaphone implements StringEncoder
{
    private static final String VOWELS = "AEIOU";
    private static final String FRONTV = "EIY";
    private static final String VARSON = "CSPTG";
    private int maxCodeLen;
    
    public Metaphone() {
        this.maxCodeLen = 4;
    }
    
    public String metaphone(final String txt) {
        boolean hard = false;
        if (txt == null || txt.length() == 0) {
            return "";
        }
        if (txt.length() == 1) {
            return txt.toUpperCase(Locale.ENGLISH);
        }
        final char[] inwd = txt.toUpperCase(Locale.ENGLISH).toCharArray();
        final StringBuilder local = new StringBuilder(40);
        final StringBuilder code = new StringBuilder(10);
        switch (inwd[0]) {
            case 'G':
            case 'K':
            case 'P': {
                if (inwd[1] == 'N') {
                    local.append(inwd, 1, inwd.length - 1);
                    break;
                }
                local.append(inwd);
                break;
            }
            case 'A': {
                if (inwd[1] == 'E') {
                    local.append(inwd, 1, inwd.length - 1);
                    break;
                }
                local.append(inwd);
                break;
            }
            case 'W': {
                if (inwd[1] == 'R') {
                    local.append(inwd, 1, inwd.length - 1);
                    break;
                }
                if (inwd[1] == 'H') {
                    local.append(inwd, 1, inwd.length - 1);
                    local.setCharAt(0, 'W');
                    break;
                }
                local.append(inwd);
                break;
            }
            case 'X': {
                inwd[0] = 'S';
                local.append(inwd);
                break;
            }
            default: {
                local.append(inwd);
                break;
            }
        }
        final int wdsz = local.length();
        int n = 0;
        while (code.length() < this.getMaxCodeLen() && n < wdsz) {
            final char symb = local.charAt(n);
            if (symb != 'C' && this.isPreviousChar(local, n, symb)) {
                ++n;
            }
            else {
                switch (symb) {
                    case 'A':
                    case 'E':
                    case 'I':
                    case 'O':
                    case 'U': {
                        if (n == 0) {
                            code.append(symb);
                            break;
                        }
                        break;
                    }
                    case 'B': {
                        if (this.isPreviousChar(local, n, 'M') && this.isLastChar(wdsz, n)) {
                            break;
                        }
                        code.append(symb);
                        break;
                    }
                    case 'C': {
                        if (this.isPreviousChar(local, n, 'S') && !this.isLastChar(wdsz, n) && "EIY".indexOf(local.charAt(n + 1)) >= 0) {
                            break;
                        }
                        if (this.regionMatch(local, n, "CIA")) {
                            code.append('X');
                            break;
                        }
                        if (!this.isLastChar(wdsz, n) && "EIY".indexOf(local.charAt(n + 1)) >= 0) {
                            code.append('S');
                            break;
                        }
                        if (this.isPreviousChar(local, n, 'S') && this.isNextChar(local, n, 'H')) {
                            code.append('K');
                            break;
                        }
                        if (!this.isNextChar(local, n, 'H')) {
                            code.append('K');
                            break;
                        }
                        if (n == 0 && wdsz >= 3 && this.isVowel(local, 2)) {
                            code.append('K');
                            break;
                        }
                        code.append('X');
                        break;
                    }
                    case 'D': {
                        if (!this.isLastChar(wdsz, n + 1) && this.isNextChar(local, n, 'G') && "EIY".indexOf(local.charAt(n + 2)) >= 0) {
                            code.append('J');
                            n += 2;
                            break;
                        }
                        code.append('T');
                        break;
                    }
                    case 'G': {
                        if (this.isLastChar(wdsz, n + 1) && this.isNextChar(local, n, 'H')) {
                            break;
                        }
                        if (!this.isLastChar(wdsz, n + 1) && this.isNextChar(local, n, 'H') && !this.isVowel(local, n + 2)) {
                            break;
                        }
                        if (n > 0) {
                            if (this.regionMatch(local, n, "GN")) {
                                break;
                            }
                            if (this.regionMatch(local, n, "GNED")) {
                                break;
                            }
                        }
                        hard = this.isPreviousChar(local, n, 'G');
                        if (!this.isLastChar(wdsz, n) && "EIY".indexOf(local.charAt(n + 1)) >= 0 && !hard) {
                            code.append('J');
                            break;
                        }
                        code.append('K');
                        break;
                    }
                    case 'H': {
                        if (this.isLastChar(wdsz, n)) {
                            break;
                        }
                        if (n > 0 && "CSPTG".indexOf(local.charAt(n - 1)) >= 0) {
                            break;
                        }
                        if (this.isVowel(local, n + 1)) {
                            code.append('H');
                            break;
                        }
                        break;
                    }
                    case 'F':
                    case 'J':
                    case 'L':
                    case 'M':
                    case 'N':
                    case 'R': {
                        code.append(symb);
                        break;
                    }
                    case 'K': {
                        if (n <= 0) {
                            code.append(symb);
                            break;
                        }
                        if (!this.isPreviousChar(local, n, 'C')) {
                            code.append(symb);
                            break;
                        }
                        break;
                    }
                    case 'P': {
                        if (this.isNextChar(local, n, 'H')) {
                            code.append('F');
                            break;
                        }
                        code.append(symb);
                        break;
                    }
                    case 'Q': {
                        code.append('K');
                        break;
                    }
                    case 'S': {
                        if (this.regionMatch(local, n, "SH") || this.regionMatch(local, n, "SIO") || this.regionMatch(local, n, "SIA")) {
                            code.append('X');
                            break;
                        }
                        code.append('S');
                        break;
                    }
                    case 'T': {
                        if (this.regionMatch(local, n, "TIA") || this.regionMatch(local, n, "TIO")) {
                            code.append('X');
                            break;
                        }
                        if (this.regionMatch(local, n, "TCH")) {
                            break;
                        }
                        if (this.regionMatch(local, n, "TH")) {
                            code.append('0');
                            break;
                        }
                        code.append('T');
                        break;
                    }
                    case 'V': {
                        code.append('F');
                        break;
                    }
                    case 'W':
                    case 'Y': {
                        if (!this.isLastChar(wdsz, n) && this.isVowel(local, n + 1)) {
                            code.append(symb);
                            break;
                        }
                        break;
                    }
                    case 'X': {
                        code.append('K');
                        code.append('S');
                        break;
                    }
                    case 'Z': {
                        code.append('S');
                        break;
                    }
                }
                ++n;
            }
            if (code.length() > this.getMaxCodeLen()) {
                code.setLength(this.getMaxCodeLen());
            }
        }
        return code.toString();
    }
    
    private boolean isVowel(final StringBuilder string, final int index) {
        return "AEIOU".indexOf(string.charAt(index)) >= 0;
    }
    
    private boolean isPreviousChar(final StringBuilder string, final int index, final char c) {
        boolean matches = false;
        if (index > 0 && index < string.length()) {
            matches = (string.charAt(index - 1) == c);
        }
        return matches;
    }
    
    private boolean isNextChar(final StringBuilder string, final int index, final char c) {
        boolean matches = false;
        if (index >= 0 && index < string.length() - 1) {
            matches = (string.charAt(index + 1) == c);
        }
        return matches;
    }
    
    private boolean regionMatch(final StringBuilder string, final int index, final String test) {
        boolean matches = false;
        if (index >= 0 && index + test.length() - 1 < string.length()) {
            final String substring = string.substring(index, index + test.length());
            matches = substring.equals(test);
        }
        return matches;
    }
    
    private boolean isLastChar(final int wdsz, final int n) {
        return n + 1 == wdsz;
    }
    
    @Override
    public Object encode(final Object obj) throws EncoderException {
        if (!(obj instanceof String)) {
            throw new EncoderException("Parameter supplied to Metaphone encode is not of type java.lang.String");
        }
        return this.metaphone((String)obj);
    }
    
    @Override
    public String encode(final String str) {
        return this.metaphone(str);
    }
    
    public boolean isMetaphoneEqual(final String str1, final String str2) {
        return this.metaphone(str1).equals(this.metaphone(str2));
    }
    
    public int getMaxCodeLen() {
        return this.maxCodeLen;
    }
    
    public void setMaxCodeLen(final int maxCodeLen) {
        this.maxCodeLen = maxCodeLen;
    }
}
