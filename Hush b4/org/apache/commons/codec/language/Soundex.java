// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class Soundex implements StringEncoder
{
    public static final String US_ENGLISH_MAPPING_STRING = "01230120022455012623010202";
    private static final char[] US_ENGLISH_MAPPING;
    public static final Soundex US_ENGLISH;
    @Deprecated
    private int maxLength;
    private final char[] soundexMapping;
    
    public Soundex() {
        this.maxLength = 4;
        this.soundexMapping = Soundex.US_ENGLISH_MAPPING;
    }
    
    public Soundex(final char[] mapping) {
        this.maxLength = 4;
        System.arraycopy(mapping, 0, this.soundexMapping = new char[mapping.length], 0, mapping.length);
    }
    
    public Soundex(final String mapping) {
        this.maxLength = 4;
        this.soundexMapping = mapping.toCharArray();
    }
    
    public int difference(final String s1, final String s2) throws EncoderException {
        return SoundexUtils.difference(this, s1, s2);
    }
    
    @Override
    public Object encode(final Object obj) throws EncoderException {
        if (!(obj instanceof String)) {
            throw new EncoderException("Parameter supplied to Soundex encode is not of type java.lang.String");
        }
        return this.soundex((String)obj);
    }
    
    @Override
    public String encode(final String str) {
        return this.soundex(str);
    }
    
    private char getMappingCode(final String str, final int index) {
        final char mappedChar = this.map(str.charAt(index));
        if (index > 1 && mappedChar != '0') {
            final char hwChar = str.charAt(index - 1);
            if ('H' == hwChar || 'W' == hwChar) {
                final char preHWChar = str.charAt(index - 2);
                final char firstCode = this.map(preHWChar);
                if (firstCode == mappedChar || 'H' == preHWChar || 'W' == preHWChar) {
                    return '\0';
                }
            }
        }
        return mappedChar;
    }
    
    @Deprecated
    public int getMaxLength() {
        return this.maxLength;
    }
    
    private char[] getSoundexMapping() {
        return this.soundexMapping;
    }
    
    private char map(final char ch) {
        final int index = ch - 'A';
        if (index < 0 || index >= this.getSoundexMapping().length) {
            throw new IllegalArgumentException("The character is not mapped: " + ch);
        }
        return this.getSoundexMapping()[index];
    }
    
    @Deprecated
    public void setMaxLength(final int maxLength) {
        this.maxLength = maxLength;
    }
    
    public String soundex(String str) {
        if (str == null) {
            return null;
        }
        str = SoundexUtils.clean(str);
        if (str.length() == 0) {
            return str;
        }
        final char[] out = { '0', '0', '0', '0' };
        int incount = 1;
        int count = 1;
        out[0] = str.charAt(0);
        char last = this.getMappingCode(str, 0);
        while (incount < str.length() && count < out.length) {
            final char mapped = this.getMappingCode(str, incount++);
            if (mapped != '\0') {
                if (mapped != '0' && mapped != last) {
                    out[count++] = mapped;
                }
                last = mapped;
            }
        }
        return new String(out);
    }
    
    static {
        US_ENGLISH_MAPPING = "01230120022455012623010202".toCharArray();
        US_ENGLISH = new Soundex();
    }
}
