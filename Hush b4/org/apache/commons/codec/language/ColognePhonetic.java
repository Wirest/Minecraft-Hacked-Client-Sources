// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class ColognePhonetic implements StringEncoder
{
    private static final char[] AEIJOUY;
    private static final char[] SCZ;
    private static final char[] WFPV;
    private static final char[] GKQ;
    private static final char[] CKQ;
    private static final char[] AHKLOQRUX;
    private static final char[] SZ;
    private static final char[] AHOUKQX;
    private static final char[] TDX;
    private static final char[][] PREPROCESS_MAP;
    
    private static boolean arrayContains(final char[] arr, final char key) {
        for (final char element : arr) {
            if (element == key) {
                return true;
            }
        }
        return false;
    }
    
    public String colognePhonetic(String text) {
        if (text == null) {
            return null;
        }
        text = this.preprocess(text);
        final CologneOutputBuffer output = new CologneOutputBuffer(text.length() * 2);
        final CologneInputBuffer input = new CologneInputBuffer(text.toCharArray());
        char lastChar = '-';
        char lastCode = '/';
        int rightLength = input.length();
        while (rightLength > 0) {
            final char chr = input.removeNext();
            char nextChar;
            if ((rightLength = input.length()) > 0) {
                nextChar = input.getNextChar();
            }
            else {
                nextChar = '-';
            }
            char code;
            if (arrayContains(ColognePhonetic.AEIJOUY, chr)) {
                code = '0';
            }
            else if (chr == 'H' || chr < 'A' || chr > 'Z') {
                if (lastCode == '/') {
                    continue;
                }
                code = '-';
            }
            else if (chr == 'B' || (chr == 'P' && nextChar != 'H')) {
                code = '1';
            }
            else if ((chr == 'D' || chr == 'T') && !arrayContains(ColognePhonetic.SCZ, nextChar)) {
                code = '2';
            }
            else if (arrayContains(ColognePhonetic.WFPV, chr)) {
                code = '3';
            }
            else if (arrayContains(ColognePhonetic.GKQ, chr)) {
                code = '4';
            }
            else if (chr == 'X' && !arrayContains(ColognePhonetic.CKQ, lastChar)) {
                code = '4';
                input.addLeft('S');
                ++rightLength;
            }
            else if (chr == 'S' || chr == 'Z') {
                code = '8';
            }
            else if (chr == 'C') {
                if (lastCode == '/') {
                    if (arrayContains(ColognePhonetic.AHKLOQRUX, nextChar)) {
                        code = '4';
                    }
                    else {
                        code = '8';
                    }
                }
                else if (arrayContains(ColognePhonetic.SZ, lastChar) || !arrayContains(ColognePhonetic.AHOUKQX, nextChar)) {
                    code = '8';
                }
                else {
                    code = '4';
                }
            }
            else if (arrayContains(ColognePhonetic.TDX, chr)) {
                code = '8';
            }
            else if (chr == 'R') {
                code = '7';
            }
            else if (chr == 'L') {
                code = '5';
            }
            else if (chr == 'M' || chr == 'N') {
                code = '6';
            }
            else {
                code = chr;
            }
            if (code != '-' && ((lastCode != code && (code != '0' || lastCode == '/')) || code < '0' || code > '8')) {
                output.addRight(code);
            }
            lastChar = chr;
            lastCode = code;
        }
        return output.toString();
    }
    
    @Override
    public Object encode(final Object object) throws EncoderException {
        if (!(object instanceof String)) {
            throw new EncoderException("This method's parameter was expected to be of the type " + String.class.getName() + ". But actually it was of the type " + object.getClass().getName() + ".");
        }
        return this.encode((String)object);
    }
    
    @Override
    public String encode(final String text) {
        return this.colognePhonetic(text);
    }
    
    public boolean isEncodeEqual(final String text1, final String text2) {
        return this.colognePhonetic(text1).equals(this.colognePhonetic(text2));
    }
    
    private String preprocess(String text) {
        text = text.toUpperCase(Locale.GERMAN);
        final char[] chrs = text.toCharArray();
        for (int index = 0; index < chrs.length; ++index) {
            if (chrs[index] > 'Z') {
                for (final char[] element : ColognePhonetic.PREPROCESS_MAP) {
                    if (chrs[index] == element[0]) {
                        chrs[index] = element[1];
                        break;
                    }
                }
            }
        }
        return new String(chrs);
    }
    
    static {
        AEIJOUY = new char[] { 'A', 'E', 'I', 'J', 'O', 'U', 'Y' };
        SCZ = new char[] { 'S', 'C', 'Z' };
        WFPV = new char[] { 'W', 'F', 'P', 'V' };
        GKQ = new char[] { 'G', 'K', 'Q' };
        CKQ = new char[] { 'C', 'K', 'Q' };
        AHKLOQRUX = new char[] { 'A', 'H', 'K', 'L', 'O', 'Q', 'R', 'U', 'X' };
        SZ = new char[] { 'S', 'Z' };
        AHOUKQX = new char[] { 'A', 'H', 'O', 'U', 'K', 'Q', 'X' };
        TDX = new char[] { 'T', 'D', 'X' };
        PREPROCESS_MAP = new char[][] { { '\u00c4', 'A' }, { '\u00dc', 'U' }, { '\u00d6', 'O' }, { '\u00df', 'S' } };
    }
    
    private abstract class CologneBuffer
    {
        protected final char[] data;
        protected int length;
        
        public CologneBuffer(final char[] data) {
            this.length = 0;
            this.data = data;
            this.length = data.length;
        }
        
        public CologneBuffer(final int buffSize) {
            this.length = 0;
            this.data = new char[buffSize];
            this.length = 0;
        }
        
        protected abstract char[] copyData(final int p0, final int p1);
        
        public int length() {
            return this.length;
        }
        
        @Override
        public String toString() {
            return new String(this.copyData(0, this.length));
        }
    }
    
    private class CologneOutputBuffer extends CologneBuffer
    {
        public CologneOutputBuffer(final int buffSize) {
            super(buffSize);
        }
        
        public void addRight(final char chr) {
            this.data[this.length] = chr;
            ++this.length;
        }
        
        @Override
        protected char[] copyData(final int start, final int length) {
            final char[] newData = new char[length];
            System.arraycopy(this.data, start, newData, 0, length);
            return newData;
        }
    }
    
    private class CologneInputBuffer extends CologneBuffer
    {
        public CologneInputBuffer(final char[] data) {
            super(data);
        }
        
        public void addLeft(final char ch) {
            ++this.length;
            this.data[this.getNextPos()] = ch;
        }
        
        @Override
        protected char[] copyData(final int start, final int length) {
            final char[] newData = new char[length];
            System.arraycopy(this.data, this.data.length - this.length + start, newData, 0, length);
            return newData;
        }
        
        public char getNextChar() {
            return this.data[this.getNextPos()];
        }
        
        protected int getNextPos() {
            return this.data.length - this.length;
        }
        
        public char removeNext() {
            final char ch = this.getNextChar();
            --this.length;
            return ch;
        }
    }
}
