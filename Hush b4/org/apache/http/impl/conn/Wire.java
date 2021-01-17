// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn;

import java.io.ByteArrayInputStream;
import org.apache.http.util.Args;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.Immutable;

@Immutable
public class Wire
{
    private final Log log;
    private final String id;
    
    public Wire(final Log log, final String id) {
        this.log = log;
        this.id = id;
    }
    
    public Wire(final Log log) {
        this(log, "");
    }
    
    private void wire(final String header, final InputStream instream) throws IOException {
        final StringBuilder buffer = new StringBuilder();
        int ch;
        while ((ch = instream.read()) != -1) {
            if (ch == 13) {
                buffer.append("[\\r]");
            }
            else if (ch == 10) {
                buffer.append("[\\n]\"");
                buffer.insert(0, "\"");
                buffer.insert(0, header);
                this.log.debug(this.id + " " + buffer.toString());
                buffer.setLength(0);
            }
            else if (ch < 32 || ch > 127) {
                buffer.append("[0x");
                buffer.append(Integer.toHexString(ch));
                buffer.append("]");
            }
            else {
                buffer.append((char)ch);
            }
        }
        if (buffer.length() > 0) {
            buffer.append('\"');
            buffer.insert(0, '\"');
            buffer.insert(0, header);
            this.log.debug(this.id + " " + buffer.toString());
        }
    }
    
    public boolean enabled() {
        return this.log.isDebugEnabled();
    }
    
    public void output(final InputStream outstream) throws IOException {
        Args.notNull(outstream, "Output");
        this.wire(">> ", outstream);
    }
    
    public void input(final InputStream instream) throws IOException {
        Args.notNull(instream, "Input");
        this.wire("<< ", instream);
    }
    
    public void output(final byte[] b, final int off, final int len) throws IOException {
        Args.notNull(b, "Output");
        this.wire(">> ", new ByteArrayInputStream(b, off, len));
    }
    
    public void input(final byte[] b, final int off, final int len) throws IOException {
        Args.notNull(b, "Input");
        this.wire("<< ", new ByteArrayInputStream(b, off, len));
    }
    
    public void output(final byte[] b) throws IOException {
        Args.notNull(b, "Output");
        this.wire(">> ", new ByteArrayInputStream(b));
    }
    
    public void input(final byte[] b) throws IOException {
        Args.notNull(b, "Input");
        this.wire("<< ", new ByteArrayInputStream(b));
    }
    
    public void output(final int b) throws IOException {
        this.output(new byte[] { (byte)b });
    }
    
    public void input(final int b) throws IOException {
        this.input(new byte[] { (byte)b });
    }
    
    public void output(final String s) throws IOException {
        Args.notNull(s, "Output");
        this.output(s.getBytes());
    }
    
    public void input(final String s) throws IOException {
        Args.notNull(s, "Input");
        this.input(s.getBytes());
    }
}
