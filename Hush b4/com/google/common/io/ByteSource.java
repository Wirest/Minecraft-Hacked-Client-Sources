// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import com.google.common.base.Ascii;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.Arrays;
import com.google.common.hash.Hasher;
import com.google.common.hash.PrimitiveSink;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.io.InputStream;

public abstract class ByteSource implements InputSupplier<InputStream>
{
    private static final int BUF_SIZE = 4096;
    private static final byte[] countBuffer;
    
    protected ByteSource() {
    }
    
    public CharSource asCharSource(final Charset charset) {
        return new AsCharSource(charset);
    }
    
    public abstract InputStream openStream() throws IOException;
    
    @Deprecated
    @Override
    public final InputStream getInput() throws IOException {
        return this.openStream();
    }
    
    public InputStream openBufferedStream() throws IOException {
        final InputStream in = this.openStream();
        return (in instanceof BufferedInputStream) ? in : new BufferedInputStream(in);
    }
    
    public ByteSource slice(final long offset, final long length) {
        return new SlicedByteSource(offset, length);
    }
    
    public boolean isEmpty() throws IOException {
        final Closer closer = Closer.create();
        try {
            final InputStream in = closer.register(this.openStream());
            return in.read() == -1;
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public long size() throws IOException {
        Closer closer = Closer.create();
        try {
            final InputStream in = closer.register(this.openStream());
            return this.countBySkipping(in);
        }
        catch (IOException e) {
            closer.close();
            final Closer create;
            closer = (create = Closer.create());
            final ByteSource byteSource = this;
            final InputStream inputStream = byteSource.openStream();
            final InputStream inputStream2 = create.register(inputStream);
            final InputStream in = inputStream2;
            final ByteSource byteSource2 = this;
            final InputStream inputStream3 = in;
            final long countByReading = byteSource2.countByReading(inputStream3);
            return countByReading;
        }
        try {
            final Closer create = closer;
            final ByteSource byteSource = this;
            final InputStream inputStream = byteSource.openStream();
            final InputStream inputStream2 = create.register(inputStream);
            final InputStream in = inputStream2;
            final ByteSource byteSource2 = this;
            final InputStream inputStream3 = in;
            final long countByReading2;
            final long countByReading = countByReading2 = byteSource2.countByReading(inputStream3);
            return countByReading2;
        }
        catch (Throwable t) {}
    }
    
    private long countBySkipping(final InputStream in) throws IOException {
        long count = 0L;
        while (true) {
            final long skipped = in.skip(Math.min(in.available(), Integer.MAX_VALUE));
            if (skipped <= 0L) {
                if (in.read() == -1) {
                    return count;
                }
                if (count == 0L && in.available() == 0) {
                    throw new IOException();
                }
                ++count;
            }
            else {
                count += skipped;
            }
        }
    }
    
    private long countByReading(final InputStream in) throws IOException {
        long count = 0L;
        long read;
        while ((read = in.read(ByteSource.countBuffer)) != -1L) {
            count += read;
        }
        return count;
    }
    
    public long copyTo(final OutputStream output) throws IOException {
        Preconditions.checkNotNull(output);
        final Closer closer = Closer.create();
        try {
            final InputStream in = closer.register(this.openStream());
            return ByteStreams.copy(in, output);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public long copyTo(final ByteSink sink) throws IOException {
        Preconditions.checkNotNull(sink);
        final Closer closer = Closer.create();
        try {
            final InputStream in = closer.register(this.openStream());
            final OutputStream out = closer.register(sink.openStream());
            return ByteStreams.copy(in, out);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public byte[] read() throws IOException {
        final Closer closer = Closer.create();
        try {
            final InputStream in = closer.register(this.openStream());
            return ByteStreams.toByteArray(in);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    @Beta
    public <T> T read(final ByteProcessor<T> processor) throws IOException {
        Preconditions.checkNotNull(processor);
        final Closer closer = Closer.create();
        try {
            final InputStream in = closer.register(this.openStream());
            return ByteStreams.readBytes(in, processor);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public HashCode hash(final HashFunction hashFunction) throws IOException {
        final Hasher hasher = hashFunction.newHasher();
        this.copyTo(Funnels.asOutputStream(hasher));
        return hasher.hash();
    }
    
    public boolean contentEquals(final ByteSource other) throws IOException {
        Preconditions.checkNotNull(other);
        final byte[] buf1 = new byte[4096];
        final byte[] buf2 = new byte[4096];
        final Closer closer = Closer.create();
        try {
            final InputStream in1 = closer.register(this.openStream());
            final InputStream in2 = closer.register(other.openStream());
            while (true) {
                final int read1 = ByteStreams.read(in1, buf1, 0, 4096);
                final int read2 = ByteStreams.read(in2, buf2, 0, 4096);
                if (read1 != read2 || !Arrays.equals(buf1, buf2)) {
                    return false;
                }
                if (read1 != 4096) {
                    return true;
                }
            }
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public static ByteSource concat(final Iterable<? extends ByteSource> sources) {
        return new ConcatenatedByteSource(sources);
    }
    
    public static ByteSource concat(final Iterator<? extends ByteSource> sources) {
        return concat((Iterable<? extends ByteSource>)ImmutableList.copyOf((Iterator<?>)sources));
    }
    
    public static ByteSource concat(final ByteSource... sources) {
        return concat(ImmutableList.copyOf(sources));
    }
    
    public static ByteSource wrap(final byte[] b) {
        return new ByteArrayByteSource(b);
    }
    
    public static ByteSource empty() {
        return EmptyByteSource.INSTANCE;
    }
    
    static {
        countBuffer = new byte[4096];
    }
    
    private final class AsCharSource extends CharSource
    {
        private final Charset charset;
        
        private AsCharSource(final Charset charset) {
            this.charset = Preconditions.checkNotNull(charset);
        }
        
        @Override
        public Reader openStream() throws IOException {
            return new InputStreamReader(ByteSource.this.openStream(), this.charset);
        }
        
        @Override
        public String toString() {
            return ByteSource.this.toString() + ".asCharSource(" + this.charset + ")";
        }
    }
    
    private final class SlicedByteSource extends ByteSource
    {
        private final long offset;
        private final long length;
        
        private SlicedByteSource(final long offset, final long length) {
            Preconditions.checkArgument(offset >= 0L, "offset (%s) may not be negative", offset);
            Preconditions.checkArgument(length >= 0L, "length (%s) may not be negative", length);
            this.offset = offset;
            this.length = length;
        }
        
        @Override
        public InputStream openStream() throws IOException {
            return this.sliceStream(ByteSource.this.openStream());
        }
        
        @Override
        public InputStream openBufferedStream() throws IOException {
            return this.sliceStream(ByteSource.this.openBufferedStream());
        }
        
        private InputStream sliceStream(final InputStream in) throws IOException {
            if (this.offset > 0L) {
                try {
                    ByteStreams.skipFully(in, this.offset);
                }
                catch (Throwable e) {
                    final Closer closer = Closer.create();
                    closer.register(in);
                    try {
                        throw closer.rethrow(e);
                    }
                    finally {
                        closer.close();
                    }
                }
            }
            return ByteStreams.limit(in, this.length);
        }
        
        @Override
        public ByteSource slice(final long offset, final long length) {
            Preconditions.checkArgument(offset >= 0L, "offset (%s) may not be negative", offset);
            Preconditions.checkArgument(length >= 0L, "length (%s) may not be negative", length);
            final long maxLength = this.length - offset;
            return ByteSource.this.slice(this.offset + offset, Math.min(length, maxLength));
        }
        
        @Override
        public boolean isEmpty() throws IOException {
            return this.length == 0L || super.isEmpty();
        }
        
        @Override
        public String toString() {
            return ByteSource.this.toString() + ".slice(" + this.offset + ", " + this.length + ")";
        }
    }
    
    private static class ByteArrayByteSource extends ByteSource
    {
        protected final byte[] bytes;
        
        protected ByteArrayByteSource(final byte[] bytes) {
            this.bytes = Preconditions.checkNotNull(bytes);
        }
        
        @Override
        public InputStream openStream() {
            return new ByteArrayInputStream(this.bytes);
        }
        
        @Override
        public InputStream openBufferedStream() throws IOException {
            return this.openStream();
        }
        
        @Override
        public boolean isEmpty() {
            return this.bytes.length == 0;
        }
        
        @Override
        public long size() {
            return this.bytes.length;
        }
        
        @Override
        public byte[] read() {
            return this.bytes.clone();
        }
        
        @Override
        public long copyTo(final OutputStream output) throws IOException {
            output.write(this.bytes);
            return this.bytes.length;
        }
        
        @Override
        public <T> T read(final ByteProcessor<T> processor) throws IOException {
            processor.processBytes(this.bytes, 0, this.bytes.length);
            return processor.getResult();
        }
        
        @Override
        public HashCode hash(final HashFunction hashFunction) throws IOException {
            return hashFunction.hashBytes(this.bytes);
        }
        
        @Override
        public String toString() {
            return "ByteSource.wrap(" + Ascii.truncate(BaseEncoding.base16().encode(this.bytes), 30, "...") + ")";
        }
    }
    
    private static final class EmptyByteSource extends ByteArrayByteSource
    {
        private static final EmptyByteSource INSTANCE;
        
        private EmptyByteSource() {
            super(new byte[0]);
        }
        
        @Override
        public CharSource asCharSource(final Charset charset) {
            Preconditions.checkNotNull(charset);
            return CharSource.empty();
        }
        
        @Override
        public byte[] read() {
            return this.bytes;
        }
        
        @Override
        public String toString() {
            return "ByteSource.empty()";
        }
        
        static {
            INSTANCE = new EmptyByteSource();
        }
    }
    
    private static final class ConcatenatedByteSource extends ByteSource
    {
        private final Iterable<? extends ByteSource> sources;
        
        ConcatenatedByteSource(final Iterable<? extends ByteSource> sources) {
            this.sources = Preconditions.checkNotNull(sources);
        }
        
        @Override
        public InputStream openStream() throws IOException {
            return new MultiInputStream(this.sources.iterator());
        }
        
        @Override
        public boolean isEmpty() throws IOException {
            for (final ByteSource source : this.sources) {
                if (!source.isEmpty()) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public long size() throws IOException {
            long result = 0L;
            for (final ByteSource source : this.sources) {
                result += source.size();
            }
            return result;
        }
        
        @Override
        public String toString() {
            return "ByteSource.concat(" + this.sources + ")";
        }
    }
}
