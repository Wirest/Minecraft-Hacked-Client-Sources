// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.data;

import com.ibm.icu.impl.PatternProps;
import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import com.ibm.icu.impl.ICUData;
import java.io.BufferedReader;

public class ResourceReader
{
    private BufferedReader reader;
    private String resourceName;
    private String encoding;
    private Class<?> root;
    private int lineNo;
    
    public ResourceReader(final String resourceName, final String encoding) throws UnsupportedEncodingException {
        this(ICUData.class, "data/" + resourceName, encoding);
    }
    
    public ResourceReader(final String resourceName) {
        this(ICUData.class, "data/" + resourceName);
    }
    
    public ResourceReader(final Class<?> rootClass, final String resourceName, final String encoding) throws UnsupportedEncodingException {
        this.root = rootClass;
        this.resourceName = resourceName;
        this.encoding = encoding;
        this.lineNo = -1;
        this._reset();
    }
    
    public ResourceReader(final InputStream is, final String resourceName, final String encoding) {
        this.root = null;
        this.resourceName = resourceName;
        this.encoding = encoding;
        this.lineNo = -1;
        try {
            final InputStreamReader isr = (encoding == null) ? new InputStreamReader(is) : new InputStreamReader(is, encoding);
            this.reader = new BufferedReader(isr);
            this.lineNo = 0;
        }
        catch (UnsupportedEncodingException ex) {}
    }
    
    public ResourceReader(final InputStream is, final String resourceName) {
        this(is, resourceName, null);
    }
    
    public ResourceReader(final Class<?> rootClass, final String resourceName) {
        this.root = rootClass;
        this.resourceName = resourceName;
        this.encoding = null;
        this.lineNo = -1;
        try {
            this._reset();
        }
        catch (UnsupportedEncodingException ex) {}
    }
    
    public String readLine() throws IOException {
        if (this.lineNo == 0) {
            ++this.lineNo;
            String line = this.reader.readLine();
            if (line.charAt(0) == '\uffef' || line.charAt(0) == '\ufeff') {
                line = line.substring(1);
            }
            return line;
        }
        ++this.lineNo;
        return this.reader.readLine();
    }
    
    public String readLineSkippingComments(final boolean trim) throws IOException {
        while (true) {
            String line = this.readLine();
            if (line == null) {
                return line;
            }
            final int pos = PatternProps.skipWhiteSpace(line, 0);
            if (pos == line.length()) {
                continue;
            }
            if (line.charAt(pos) == '#') {
                continue;
            }
            if (trim) {
                line = line.substring(pos);
            }
            return line;
        }
    }
    
    public String readLineSkippingComments() throws IOException {
        return this.readLineSkippingComments(false);
    }
    
    public int getLineNumber() {
        return this.lineNo;
    }
    
    public String describePosition() {
        return this.resourceName + ':' + this.lineNo;
    }
    
    public void reset() {
        try {
            this._reset();
        }
        catch (UnsupportedEncodingException ex) {}
    }
    
    private void _reset() throws UnsupportedEncodingException {
        if (this.lineNo == 0) {
            return;
        }
        final InputStream is = ICUData.getStream(this.root, this.resourceName);
        if (is == null) {
            throw new IllegalArgumentException("Can't open " + this.resourceName);
        }
        final InputStreamReader isr = (this.encoding == null) ? new InputStreamReader(is) : new InputStreamReader(is, this.encoding);
        this.reader = new BufferedReader(isr);
        this.lineNo = 0;
    }
}
