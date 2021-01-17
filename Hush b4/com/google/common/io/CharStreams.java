// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import java.io.Writer;
import java.io.EOFException;
import java.util.Arrays;
import com.google.common.collect.Iterables;
import com.google.common.base.Function;
import java.io.Reader;
import java.util.ArrayList;
import java.io.Closeable;
import java.util.List;
import java.nio.CharBuffer;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.io.InputStream;
import java.io.StringReader;
import com.google.common.annotations.Beta;

@Beta
public final class CharStreams
{
    private static final int BUF_SIZE = 2048;
    
    private CharStreams() {
    }
    
    @Deprecated
    public static InputSupplier<StringReader> newReaderSupplier(final String value) {
        return asInputSupplier(CharSource.wrap(value));
    }
    
    @Deprecated
    public static InputSupplier<InputStreamReader> newReaderSupplier(final InputSupplier<? extends InputStream> in, final Charset charset) {
        return asInputSupplier(ByteStreams.asByteSource(in).asCharSource(charset));
    }
    
    @Deprecated
    public static OutputSupplier<OutputStreamWriter> newWriterSupplier(final OutputSupplier<? extends OutputStream> out, final Charset charset) {
        return asOutputSupplier(ByteStreams.asByteSink(out).asCharSink(charset));
    }
    
    @Deprecated
    public static <W extends Appendable> void write(final CharSequence from, final OutputSupplier<W> to) throws IOException {
        asCharSink((OutputSupplier<? extends Appendable>)to).write(from);
    }
    
    @Deprecated
    public static <R extends Readable, W extends java.lang.Appendable> long copy(final InputSupplier<R> from, final OutputSupplier<W> to) throws IOException {
        return asCharSource((InputSupplier<? extends Readable>)from).copyTo(asCharSink((OutputSupplier<? extends Appendable>)to));
    }
    
    @Deprecated
    public static <R extends java.lang.Readable> long copy(final InputSupplier<R> from, final Appendable to) throws IOException {
        return asCharSource((InputSupplier<? extends Readable>)from).copyTo(to);
    }
    
    public static long copy(final Readable from, final Appendable to) throws IOException {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        final CharBuffer buf = CharBuffer.allocate(2048);
        long total = 0L;
        while (from.read(buf) != -1) {
            buf.flip();
            to.append(buf);
            total += buf.remaining();
            buf.clear();
        }
        return total;
    }
    
    public static String toString(final Readable r) throws IOException {
        return toStringBuilder(r).toString();
    }
    
    @Deprecated
    public static <R extends java.lang.Readable> String toString(final InputSupplier<R> supplier) throws IOException {
        return asCharSource((InputSupplier<? extends Readable>)supplier).read();
    }
    
    private static StringBuilder toStringBuilder(final Readable r) throws IOException {
        final StringBuilder sb = new StringBuilder();
        copy(r, sb);
        return sb;
    }
    
    @Deprecated
    public static <R extends java.lang.Readable> String readFirstLine(final InputSupplier<R> supplier) throws IOException {
        return asCharSource((InputSupplier<? extends Readable>)supplier).readFirstLine();
    }
    
    @Deprecated
    public static <R extends java.lang.Readable> List<String> readLines(final InputSupplier<R> supplier) throws IOException {
        final Closer closer = Closer.create();
        try {
            final R r = closer.register(supplier.getInput());
            return readLines((Readable)r);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public static List<String> readLines(final Readable r) throws IOException {
        final List<String> result = new ArrayList<String>();
        final LineReader lineReader = new LineReader(r);
        String line;
        while ((line = lineReader.readLine()) != null) {
            result.add(line);
        }
        return result;
    }
    
    public static <T> T readLines(final Readable readable, final LineProcessor<T> processor) throws IOException {
        Preconditions.checkNotNull(readable);
        Preconditions.checkNotNull(processor);
        final LineReader lineReader = new LineReader(readable);
        String line;
        while ((line = lineReader.readLine()) != null && processor.processLine(line)) {}
        return processor.getResult();
    }
    
    @Deprecated
    public static <R extends java.lang.Readable, T> T readLines(final InputSupplier<R> supplier, final LineProcessor<T> callback) throws IOException {
        Preconditions.checkNotNull(supplier);
        Preconditions.checkNotNull(callback);
        final Closer closer = Closer.create();
        try {
            final R r = closer.register(supplier.getInput());
            return readLines((Readable)r, callback);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    @Deprecated
    public static InputSupplier<Reader> join(final Iterable<? extends InputSupplier<? extends Reader>> suppliers) {
        Preconditions.checkNotNull(suppliers);
        final Iterable<CharSource> sources = Iterables.transform(suppliers, (Function<? super InputSupplier<? extends Reader>, ? extends CharSource>)new Function<InputSupplier<? extends Reader>, CharSource>() {
            @Override
            public CharSource apply(final InputSupplier<? extends Reader> input) {
                return CharStreams.asCharSource(input);
            }
        });
        return asInputSupplier(CharSource.concat(sources));
    }
    
    @Deprecated
    public static InputSupplier<Reader> join(final InputSupplier<? extends Reader>... suppliers) {
        return join(Arrays.asList(suppliers));
    }
    
    public static void skipFully(final Reader reader, long n) throws IOException {
        Preconditions.checkNotNull(reader);
        while (n > 0L) {
            final long amt = reader.skip(n);
            if (amt == 0L) {
                if (reader.read() == -1) {
                    throw new EOFException();
                }
                --n;
            }
            else {
                n -= amt;
            }
        }
    }
    
    public static Writer nullWriter() {
        return NullWriter.INSTANCE;
    }
    
    public static Writer asWriter(final Appendable target) {
        if (target instanceof Writer) {
            return (Writer)target;
        }
        return new AppendableWriter(target);
    }
    
    static Reader asReader(final Readable readable) {
        Preconditions.checkNotNull(readable);
        if (readable instanceof Reader) {
            return (Reader)readable;
        }
        return new Reader() {
            @Override
            public int read(final char[] cbuf, final int off, final int len) throws IOException {
                return this.read(CharBuffer.wrap(cbuf, off, len));
            }
            
            @Override
            public int read(final CharBuffer target) throws IOException {
                return readable.read(target);
            }
            
            @Override
            public void close() throws IOException {
                if (readable instanceof Closeable) {
                    ((Closeable)readable).close();
                }
            }
        };
    }
    
    @Deprecated
    public static CharSource asCharSource(final InputSupplier<? extends Readable> supplier) {
        Preconditions.checkNotNull(supplier);
        return new CharSource() {
            @Override
            public Reader openStream() throws IOException {
                return CharStreams.asReader(supplier.getInput());
            }
            
            @Override
            public String toString() {
                return "CharStreams.asCharSource(" + supplier + ")";
            }
        };
    }
    
    @Deprecated
    public static CharSink asCharSink(final OutputSupplier<? extends Appendable> supplier) {
        Preconditions.checkNotNull(supplier);
        return new CharSink() {
            @Override
            public Writer openStream() throws IOException {
                return CharStreams.asWriter(supplier.getOutput());
            }
            
            @Override
            public String toString() {
                return "CharStreams.asCharSink(" + supplier + ")";
            }
        };
    }
    
    static <R extends Reader> InputSupplier<R> asInputSupplier(final CharSource source) {
        return Preconditions.checkNotNull((InputSupplier<R>)source);
    }
    
    static <W extends Writer> OutputSupplier<W> asOutputSupplier(final CharSink sink) {
        return Preconditions.checkNotNull((OutputSupplier<W>)sink);
    }
    
    private static final class NullWriter extends Writer
    {
        private static final NullWriter INSTANCE;
        
        @Override
        public void write(final int c) {
        }
        
        @Override
        public void write(final char[] cbuf) {
            Preconditions.checkNotNull(cbuf);
        }
        
        @Override
        public void write(final char[] cbuf, final int off, final int len) {
            Preconditions.checkPositionIndexes(off, off + len, cbuf.length);
        }
        
        @Override
        public void write(final String str) {
            Preconditions.checkNotNull(str);
        }
        
        @Override
        public void write(final String str, final int off, final int len) {
            Preconditions.checkPositionIndexes(off, off + len, str.length());
        }
        
        @Override
        public Writer append(final CharSequence csq) {
            Preconditions.checkNotNull(csq);
            return this;
        }
        
        @Override
        public Writer append(final CharSequence csq, final int start, final int end) {
            Preconditions.checkPositionIndexes(start, end, csq.length());
            return this;
        }
        
        @Override
        public Writer append(final char c) {
            return this;
        }
        
        @Override
        public void flush() {
        }
        
        @Override
        public void close() {
        }
        
        @Override
        public String toString() {
            return "CharStreams.nullWriter()";
        }
        
        static {
            INSTANCE = new NullWriter();
        }
    }
}
