// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.util.Arrays;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.io.IOException;
import java.util.ArrayList;
import java.io.InputStream;

public class CharsetDetector
{
    private static final int kBufSize = 8000;
    byte[] fInputBytes;
    int fInputLen;
    short[] fByteStats;
    boolean fC1Bytes;
    String fDeclaredEncoding;
    byte[] fRawInput;
    int fRawLength;
    InputStream fInputStream;
    boolean fStripTags;
    private static ArrayList<CharsetRecognizer> fCSRecognizers;
    private static String[] fCharsetNames;
    
    public CharsetDetector() {
        this.fInputBytes = new byte[8000];
        this.fByteStats = new short[256];
        this.fC1Bytes = false;
        this.fStripTags = false;
    }
    
    public CharsetDetector setDeclaredEncoding(final String encoding) {
        this.fDeclaredEncoding = encoding;
        return this;
    }
    
    public CharsetDetector setText(final byte[] in) {
        this.fRawInput = in;
        this.fRawLength = in.length;
        return this;
    }
    
    public CharsetDetector setText(final InputStream in) throws IOException {
        (this.fInputStream = in).mark(8000);
        this.fRawInput = new byte[8000];
        this.fRawLength = 0;
        int bytesRead;
        for (int remainingLength = 8000; remainingLength > 0; remainingLength -= bytesRead) {
            bytesRead = this.fInputStream.read(this.fRawInput, this.fRawLength, remainingLength);
            if (bytesRead <= 0) {
                break;
            }
            this.fRawLength += bytesRead;
        }
        this.fInputStream.reset();
        return this;
    }
    
    public CharsetMatch detect() {
        final CharsetMatch[] matches = this.detectAll();
        if (matches == null || matches.length == 0) {
            return null;
        }
        return matches[0];
    }
    
    public CharsetMatch[] detectAll() {
        final ArrayList<CharsetMatch> matches = new ArrayList<CharsetMatch>();
        this.MungeInput();
        for (final CharsetRecognizer csr : CharsetDetector.fCSRecognizers) {
            final CharsetMatch m = csr.match(this);
            if (m != null) {
                matches.add(m);
            }
        }
        Collections.sort(matches);
        Collections.reverse(matches);
        CharsetMatch[] resultArray = new CharsetMatch[matches.size()];
        resultArray = matches.toArray(resultArray);
        return resultArray;
    }
    
    public Reader getReader(final InputStream in, final String declaredEncoding) {
        this.fDeclaredEncoding = declaredEncoding;
        try {
            this.setText(in);
            final CharsetMatch match = this.detect();
            if (match == null) {
                return null;
            }
            return match.getReader();
        }
        catch (IOException e) {
            return null;
        }
    }
    
    public String getString(final byte[] in, final String declaredEncoding) {
        this.fDeclaredEncoding = declaredEncoding;
        try {
            this.setText(in);
            final CharsetMatch match = this.detect();
            if (match == null) {
                return null;
            }
            return match.getString(-1);
        }
        catch (IOException e) {
            return null;
        }
    }
    
    public static String[] getAllDetectableCharsets() {
        return CharsetDetector.fCharsetNames;
    }
    
    public boolean inputFilterEnabled() {
        return this.fStripTags;
    }
    
    public boolean enableInputFilter(final boolean filter) {
        final boolean previous = this.fStripTags;
        this.fStripTags = filter;
        return previous;
    }
    
    private void MungeInput() {
        int srci = 0;
        int dsti = 0;
        boolean inMarkup = false;
        int openTags = 0;
        int badTags = 0;
        if (this.fStripTags) {
            for (srci = 0; srci < this.fRawLength && dsti < this.fInputBytes.length; ++srci) {
                final byte b = this.fRawInput[srci];
                if (b == 60) {
                    if (inMarkup) {
                        ++badTags;
                    }
                    inMarkup = true;
                    ++openTags;
                }
                if (!inMarkup) {
                    this.fInputBytes[dsti++] = b;
                }
                if (b == 62) {
                    inMarkup = false;
                }
            }
            this.fInputLen = dsti;
        }
        if (openTags < 5 || openTags / 5 < badTags || (this.fInputLen < 100 && this.fRawLength > 600)) {
            int limit = this.fRawLength;
            if (limit > 8000) {
                limit = 8000;
            }
            for (srci = 0; srci < limit; ++srci) {
                this.fInputBytes[srci] = this.fRawInput[srci];
            }
            this.fInputLen = srci;
        }
        Arrays.fill(this.fByteStats, (short)0);
        for (srci = 0; srci < this.fInputLen; ++srci) {
            final int val = this.fInputBytes[srci] & 0xFF;
            final short[] fByteStats = this.fByteStats;
            final int n = val;
            ++fByteStats[n];
        }
        this.fC1Bytes = false;
        for (int i = 128; i <= 159; ++i) {
            if (this.fByteStats[i] != 0) {
                this.fC1Bytes = true;
                break;
            }
        }
    }
    
    private static ArrayList<CharsetRecognizer> createRecognizers() {
        final ArrayList<CharsetRecognizer> recognizers = new ArrayList<CharsetRecognizer>();
        recognizers.add(new CharsetRecog_UTF8());
        recognizers.add(new CharsetRecog_Unicode.CharsetRecog_UTF_16_BE());
        recognizers.add(new CharsetRecog_Unicode.CharsetRecog_UTF_16_LE());
        recognizers.add(new CharsetRecog_Unicode.CharsetRecog_UTF_32_BE());
        recognizers.add(new CharsetRecog_Unicode.CharsetRecog_UTF_32_LE());
        recognizers.add(new CharsetRecog_mbcs.CharsetRecog_sjis());
        recognizers.add(new CharsetRecog_2022.CharsetRecog_2022JP());
        recognizers.add(new CharsetRecog_2022.CharsetRecog_2022CN());
        recognizers.add(new CharsetRecog_2022.CharsetRecog_2022KR());
        recognizers.add(new CharsetRecog_mbcs.CharsetRecog_gb_18030());
        recognizers.add(new CharsetRecog_mbcs.CharsetRecog_euc.CharsetRecog_euc_jp());
        recognizers.add(new CharsetRecog_mbcs.CharsetRecog_euc.CharsetRecog_euc_kr());
        recognizers.add(new CharsetRecog_mbcs.CharsetRecog_big5());
        recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_1());
        recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_2());
        recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_5_ru());
        recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_6_ar());
        recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_7_el());
        recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_8_I_he());
        recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_8_he());
        recognizers.add(new CharsetRecog_sbcs.CharsetRecog_windows_1251());
        recognizers.add(new CharsetRecog_sbcs.CharsetRecog_windows_1256());
        recognizers.add(new CharsetRecog_sbcs.CharsetRecog_KOI8_R());
        recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_9_tr());
        recognizers.add(new CharsetRecog_sbcs.CharsetRecog_IBM424_he_rtl());
        recognizers.add(new CharsetRecog_sbcs.CharsetRecog_IBM424_he_ltr());
        recognizers.add(new CharsetRecog_sbcs.CharsetRecog_IBM420_ar_rtl());
        recognizers.add(new CharsetRecog_sbcs.CharsetRecog_IBM420_ar_ltr());
        final String[] charsetNames = new String[recognizers.size()];
        int out = 0;
        for (int i = 0; i < recognizers.size(); ++i) {
            final String name = recognizers.get(i).getName();
            if (out == 0 || !name.equals(charsetNames[out - 1])) {
                charsetNames[out++] = name;
            }
        }
        System.arraycopy(charsetNames, 0, CharsetDetector.fCharsetNames = new String[out], 0, out);
        return recognizers;
    }
    
    static {
        CharsetDetector.fCSRecognizers = createRecognizers();
    }
}
