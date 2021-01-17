// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.Reader;
import java.io.InputStream;

public class CharsetMatch implements Comparable<CharsetMatch>
{
    private int fConfidence;
    private byte[] fRawInput;
    private int fRawLength;
    private InputStream fInputStream;
    private String fCharsetName;
    private String fLang;
    
    public Reader getReader() {
        InputStream inputStream = this.fInputStream;
        if (inputStream == null) {
            inputStream = new ByteArrayInputStream(this.fRawInput, 0, this.fRawLength);
        }
        try {
            inputStream.reset();
            return new InputStreamReader(inputStream, this.getName());
        }
        catch (IOException e) {
            return null;
        }
    }
    
    public String getString() throws IOException {
        return this.getString(-1);
    }
    
    public String getString(final int maxLength) throws IOException {
        String result = null;
        if (this.fInputStream != null) {
            final StringBuilder sb = new StringBuilder();
            final char[] buffer = new char[1024];
            final Reader reader = this.getReader();
            for (int max = (maxLength < 0) ? Integer.MAX_VALUE : maxLength, bytesRead = 0; (bytesRead = reader.read(buffer, 0, Math.min(max, 1024))) >= 0; max -= bytesRead) {
                sb.append(buffer, 0, bytesRead);
            }
            reader.close();
            return sb.toString();
        }
        String name = this.getName();
        final int startSuffix = (name.indexOf("_rtl") < 0) ? name.indexOf("_ltr") : name.indexOf("_rtl");
        if (startSuffix > 0) {
            name = name.substring(0, startSuffix);
        }
        result = new String(this.fRawInput, name);
        return result;
    }
    
    public int getConfidence() {
        return this.fConfidence;
    }
    
    public String getName() {
        return this.fCharsetName;
    }
    
    public String getLanguage() {
        return this.fLang;
    }
    
    public int compareTo(final CharsetMatch other) {
        int compareResult = 0;
        if (this.fConfidence > other.fConfidence) {
            compareResult = 1;
        }
        else if (this.fConfidence < other.fConfidence) {
            compareResult = -1;
        }
        return compareResult;
    }
    
    CharsetMatch(final CharsetDetector det, final CharsetRecognizer rec, final int conf) {
        this.fRawInput = null;
        this.fInputStream = null;
        this.fConfidence = conf;
        if (det.fInputStream == null) {
            this.fRawInput = det.fRawInput;
            this.fRawLength = det.fRawLength;
        }
        this.fInputStream = det.fInputStream;
        this.fCharsetName = rec.getName();
        this.fLang = rec.getLanguage();
    }
    
    CharsetMatch(final CharsetDetector det, final CharsetRecognizer rec, final int conf, final String csName, final String lang) {
        this.fRawInput = null;
        this.fInputStream = null;
        this.fConfidence = conf;
        if (det.fInputStream == null) {
            this.fRawInput = det.fRawInput;
            this.fRawLength = det.fRawLength;
        }
        this.fInputStream = det.fInputStream;
        this.fCharsetName = csName;
        this.fLang = lang;
    }
}
