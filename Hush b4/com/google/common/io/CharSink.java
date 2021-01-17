// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import java.util.Iterator;
import com.google.common.base.Preconditions;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

public abstract class CharSink implements OutputSupplier<Writer>
{
    protected CharSink() {
    }
    
    public abstract Writer openStream() throws IOException;
    
    @Deprecated
    @Override
    public final Writer getOutput() throws IOException {
        return this.openStream();
    }
    
    public Writer openBufferedStream() throws IOException {
        final Writer writer = this.openStream();
        return (writer instanceof BufferedWriter) ? writer : new BufferedWriter(writer);
    }
    
    public void write(final CharSequence charSequence) throws IOException {
        Preconditions.checkNotNull(charSequence);
        final Closer closer = Closer.create();
        try {
            final Writer out = closer.register(this.openStream());
            out.append(charSequence);
            out.flush();
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public void writeLines(final Iterable<? extends CharSequence> lines) throws IOException {
        this.writeLines(lines, System.getProperty("line.separator"));
    }
    
    public void writeLines(final Iterable<? extends CharSequence> lines, final String lineSeparator) throws IOException {
        Preconditions.checkNotNull(lines);
        Preconditions.checkNotNull(lineSeparator);
        final Closer closer = Closer.create();
        try {
            final Writer out = closer.register(this.openBufferedStream());
            for (final CharSequence line : lines) {
                out.append(line).append((CharSequence)lineSeparator);
            }
            out.flush();
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public long writeFrom(final Readable readable) throws IOException {
        Preconditions.checkNotNull(readable);
        final Closer closer = Closer.create();
        try {
            final Writer out = closer.register(this.openStream());
            final long written = CharStreams.copy(readable, out);
            out.flush();
            return written;
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
}
