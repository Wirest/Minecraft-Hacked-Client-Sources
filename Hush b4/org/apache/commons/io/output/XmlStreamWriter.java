// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.output;

import org.apache.commons.io.input.XmlStreamReader;
import java.util.regex.Matcher;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.regex.Pattern;
import java.io.StringWriter;
import java.io.OutputStream;
import java.io.Writer;

public class XmlStreamWriter extends Writer
{
    private static final int BUFFER_SIZE = 4096;
    private final OutputStream out;
    private final String defaultEncoding;
    private StringWriter xmlPrologWriter;
    private Writer writer;
    private String encoding;
    static final Pattern ENCODING_PATTERN;
    
    public XmlStreamWriter(final OutputStream out) {
        this(out, null);
    }
    
    public XmlStreamWriter(final OutputStream out, final String defaultEncoding) {
        this.xmlPrologWriter = new StringWriter(4096);
        this.out = out;
        this.defaultEncoding = ((defaultEncoding != null) ? defaultEncoding : "UTF-8");
    }
    
    public XmlStreamWriter(final File file) throws FileNotFoundException {
        this(file, null);
    }
    
    public XmlStreamWriter(final File file, final String defaultEncoding) throws FileNotFoundException {
        this(new FileOutputStream(file), defaultEncoding);
    }
    
    public String getEncoding() {
        return this.encoding;
    }
    
    public String getDefaultEncoding() {
        return this.defaultEncoding;
    }
    
    @Override
    public void close() throws IOException {
        if (this.writer == null) {
            this.encoding = this.defaultEncoding;
            (this.writer = new OutputStreamWriter(this.out, this.encoding)).write(this.xmlPrologWriter.toString());
        }
        this.writer.close();
    }
    
    @Override
    public void flush() throws IOException {
        if (this.writer != null) {
            this.writer.flush();
        }
    }
    
    private void detectEncoding(final char[] cbuf, final int off, final int len) throws IOException {
        int size = len;
        final StringBuffer xmlProlog = this.xmlPrologWriter.getBuffer();
        if (xmlProlog.length() + len > 4096) {
            size = 4096 - xmlProlog.length();
        }
        this.xmlPrologWriter.write(cbuf, off, size);
        if (xmlProlog.length() >= 5) {
            if (xmlProlog.substring(0, 5).equals("<?xml")) {
                final int xmlPrologEnd = xmlProlog.indexOf("?>");
                if (xmlPrologEnd > 0) {
                    final Matcher m = XmlStreamWriter.ENCODING_PATTERN.matcher(xmlProlog.substring(0, xmlPrologEnd));
                    if (m.find()) {
                        this.encoding = m.group(1).toUpperCase();
                        this.encoding = this.encoding.substring(1, this.encoding.length() - 1);
                    }
                    else {
                        this.encoding = this.defaultEncoding;
                    }
                }
                else if (xmlProlog.length() >= 4096) {
                    this.encoding = this.defaultEncoding;
                }
            }
            else {
                this.encoding = this.defaultEncoding;
            }
            if (this.encoding != null) {
                this.xmlPrologWriter = null;
                (this.writer = new OutputStreamWriter(this.out, this.encoding)).write(xmlProlog.toString());
                if (len > size) {
                    this.writer.write(cbuf, off + size, len - size);
                }
            }
        }
    }
    
    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        if (this.xmlPrologWriter != null) {
            this.detectEncoding(cbuf, off, len);
        }
        else {
            this.writer.write(cbuf, off, len);
        }
    }
    
    static {
        ENCODING_PATTERN = XmlStreamReader.ENCODING_PATTERN;
    }
}
