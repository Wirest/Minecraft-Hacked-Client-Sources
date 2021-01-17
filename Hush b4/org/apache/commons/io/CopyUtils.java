// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io;

import java.io.StringReader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.Writer;
import java.io.IOException;
import java.io.OutputStream;

@Deprecated
public class CopyUtils
{
    private static final int DEFAULT_BUFFER_SIZE = 4096;
    
    public static void copy(final byte[] input, final OutputStream output) throws IOException {
        output.write(input);
    }
    
    public static void copy(final byte[] input, final Writer output) throws IOException {
        final ByteArrayInputStream in = new ByteArrayInputStream(input);
        copy(in, output);
    }
    
    public static void copy(final byte[] input, final Writer output, final String encoding) throws IOException {
        final ByteArrayInputStream in = new ByteArrayInputStream(input);
        copy(in, output, encoding);
    }
    
    public static int copy(final InputStream input, final OutputStream output) throws IOException {
        final byte[] buffer = new byte[4096];
        int count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
    
    public static int copy(final Reader input, final Writer output) throws IOException {
        final char[] buffer = new char[4096];
        int count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
    
    public static void copy(final InputStream input, final Writer output) throws IOException {
        final InputStreamReader in = new InputStreamReader(input);
        copy(in, output);
    }
    
    public static void copy(final InputStream input, final Writer output, final String encoding) throws IOException {
        final InputStreamReader in = new InputStreamReader(input, encoding);
        copy(in, output);
    }
    
    public static void copy(final Reader input, final OutputStream output) throws IOException {
        final OutputStreamWriter out = new OutputStreamWriter(output);
        copy(input, out);
        out.flush();
    }
    
    public static void copy(final String input, final OutputStream output) throws IOException {
        final StringReader in = new StringReader(input);
        final OutputStreamWriter out = new OutputStreamWriter(output);
        copy(in, out);
        out.flush();
    }
    
    public static void copy(final String input, final Writer output) throws IOException {
        output.write(input);
    }
}
