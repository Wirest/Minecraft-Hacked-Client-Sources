// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.output;

import java.io.OutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.Charset;
import java.io.IOException;
import java.io.File;
import java.io.Writer;

public class FileWriterWithEncoding extends Writer
{
    private final Writer out;
    
    public FileWriterWithEncoding(final String filename, final String encoding) throws IOException {
        this(new File(filename), encoding, false);
    }
    
    public FileWriterWithEncoding(final String filename, final String encoding, final boolean append) throws IOException {
        this(new File(filename), encoding, append);
    }
    
    public FileWriterWithEncoding(final String filename, final Charset encoding) throws IOException {
        this(new File(filename), encoding, false);
    }
    
    public FileWriterWithEncoding(final String filename, final Charset encoding, final boolean append) throws IOException {
        this(new File(filename), encoding, append);
    }
    
    public FileWriterWithEncoding(final String filename, final CharsetEncoder encoding) throws IOException {
        this(new File(filename), encoding, false);
    }
    
    public FileWriterWithEncoding(final String filename, final CharsetEncoder encoding, final boolean append) throws IOException {
        this(new File(filename), encoding, append);
    }
    
    public FileWriterWithEncoding(final File file, final String encoding) throws IOException {
        this(file, encoding, false);
    }
    
    public FileWriterWithEncoding(final File file, final String encoding, final boolean append) throws IOException {
        this.out = initWriter(file, encoding, append);
    }
    
    public FileWriterWithEncoding(final File file, final Charset encoding) throws IOException {
        this(file, encoding, false);
    }
    
    public FileWriterWithEncoding(final File file, final Charset encoding, final boolean append) throws IOException {
        this.out = initWriter(file, encoding, append);
    }
    
    public FileWriterWithEncoding(final File file, final CharsetEncoder encoding) throws IOException {
        this(file, encoding, false);
    }
    
    public FileWriterWithEncoding(final File file, final CharsetEncoder encoding, final boolean append) throws IOException {
        this.out = initWriter(file, encoding, append);
    }
    
    private static Writer initWriter(final File file, final Object encoding, final boolean append) throws IOException {
        if (file == null) {
            throw new NullPointerException("File is missing");
        }
        if (encoding == null) {
            throw new NullPointerException("Encoding is missing");
        }
        final boolean fileExistedAlready = file.exists();
        OutputStream stream = null;
        Writer writer = null;
        try {
            stream = new FileOutputStream(file, append);
            if (encoding instanceof Charset) {
                writer = new OutputStreamWriter(stream, (Charset)encoding);
            }
            else if (encoding instanceof CharsetEncoder) {
                writer = new OutputStreamWriter(stream, (CharsetEncoder)encoding);
            }
            else {
                writer = new OutputStreamWriter(stream, (String)encoding);
            }
        }
        catch (IOException ex) {
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(stream);
            if (!fileExistedAlready) {
                FileUtils.deleteQuietly(file);
            }
            throw ex;
        }
        catch (RuntimeException ex2) {
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(stream);
            if (!fileExistedAlready) {
                FileUtils.deleteQuietly(file);
            }
            throw ex2;
        }
        return writer;
    }
    
    @Override
    public void write(final int idx) throws IOException {
        this.out.write(idx);
    }
    
    @Override
    public void write(final char[] chr) throws IOException {
        this.out.write(chr);
    }
    
    @Override
    public void write(final char[] chr, final int st, final int end) throws IOException {
        this.out.write(chr, st, end);
    }
    
    @Override
    public void write(final String str) throws IOException {
        this.out.write(str);
    }
    
    @Override
    public void write(final String str, final int st, final int end) throws IOException {
        this.out.write(str, st, end);
    }
    
    @Override
    public void flush() throws IOException {
        this.out.flush();
    }
    
    @Override
    public void close() throws IOException {
        this.out.close();
    }
}
