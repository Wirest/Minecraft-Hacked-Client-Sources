// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import java.util.regex.Pattern;
import com.google.common.base.Ascii;
import com.google.common.collect.AbstractIterator;
import com.google.common.base.Splitter;
import java.util.Iterator;
import com.google.common.annotations.Beta;
import java.util.List;
import java.util.Collection;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList;
import javax.annotation.Nullable;
import java.io.Writer;
import com.google.common.base.Preconditions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public abstract class CharSource implements InputSupplier<Reader>
{
    protected CharSource() {
    }
    
    public abstract Reader openStream() throws IOException;
    
    @Deprecated
    @Override
    public final Reader getInput() throws IOException {
        return this.openStream();
    }
    
    public BufferedReader openBufferedStream() throws IOException {
        final Reader reader = this.openStream();
        return (BufferedReader)((reader instanceof BufferedReader) ? reader : new BufferedReader(reader));
    }
    
    public long copyTo(final Appendable appendable) throws IOException {
        Preconditions.checkNotNull(appendable);
        final Closer closer = Closer.create();
        try {
            final Reader reader = closer.register(this.openStream());
            return CharStreams.copy(reader, appendable);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public long copyTo(final CharSink sink) throws IOException {
        Preconditions.checkNotNull(sink);
        final Closer closer = Closer.create();
        try {
            final Reader reader = closer.register(this.openStream());
            final Writer writer = closer.register(sink.openStream());
            return CharStreams.copy(reader, writer);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public String read() throws IOException {
        final Closer closer = Closer.create();
        try {
            final Reader reader = closer.register(this.openStream());
            return CharStreams.toString(reader);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    @Nullable
    public String readFirstLine() throws IOException {
        final Closer closer = Closer.create();
        try {
            final BufferedReader reader = closer.register(this.openBufferedStream());
            return reader.readLine();
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public ImmutableList<String> readLines() throws IOException {
        final Closer closer = Closer.create();
        try {
            final BufferedReader reader = closer.register(this.openBufferedStream());
            final List<String> result = (List<String>)Lists.newArrayList();
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
            return ImmutableList.copyOf((Collection<? extends String>)result);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    @Beta
    public <T> T readLines(final LineProcessor<T> processor) throws IOException {
        Preconditions.checkNotNull(processor);
        final Closer closer = Closer.create();
        try {
            final Reader reader = closer.register(this.openStream());
            return CharStreams.readLines(reader, processor);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public boolean isEmpty() throws IOException {
        final Closer closer = Closer.create();
        try {
            final Reader reader = closer.register(this.openStream());
            return reader.read() == -1;
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public static CharSource concat(final Iterable<? extends CharSource> sources) {
        return new ConcatenatedCharSource(sources);
    }
    
    public static CharSource concat(final Iterator<? extends CharSource> sources) {
        return concat((Iterable<? extends CharSource>)ImmutableList.copyOf((Iterator<?>)sources));
    }
    
    public static CharSource concat(final CharSource... sources) {
        return concat(ImmutableList.copyOf(sources));
    }
    
    public static CharSource wrap(final CharSequence charSequence) {
        return new CharSequenceCharSource(charSequence);
    }
    
    public static CharSource empty() {
        return EmptyCharSource.INSTANCE;
    }
    
    private static class CharSequenceCharSource extends CharSource
    {
        private static final Splitter LINE_SPLITTER;
        private final CharSequence seq;
        
        protected CharSequenceCharSource(final CharSequence seq) {
            this.seq = Preconditions.checkNotNull(seq);
        }
        
        @Override
        public Reader openStream() {
            return new CharSequenceReader(this.seq);
        }
        
        @Override
        public String read() {
            return this.seq.toString();
        }
        
        @Override
        public boolean isEmpty() {
            return this.seq.length() == 0;
        }
        
        private Iterable<String> lines() {
            return new Iterable<String>() {
                @Override
                public Iterator<String> iterator() {
                    return new AbstractIterator<String>() {
                        Iterator<String> lines = CharSequenceCharSource.LINE_SPLITTER.split(CharSequenceCharSource.this.seq).iterator();
                        
                        @Override
                        protected String computeNext() {
                            if (this.lines.hasNext()) {
                                final String next = this.lines.next();
                                if (this.lines.hasNext() || !next.isEmpty()) {
                                    return next;
                                }
                            }
                            return this.endOfData();
                        }
                    };
                }
            };
        }
        
        @Override
        public String readFirstLine() {
            final Iterator<String> lines = this.lines().iterator();
            return lines.hasNext() ? lines.next() : null;
        }
        
        @Override
        public ImmutableList<String> readLines() {
            return ImmutableList.copyOf((Iterable<? extends String>)this.lines());
        }
        
        @Override
        public <T> T readLines(final LineProcessor<T> processor) throws IOException {
            for (final String line : this.lines()) {
                if (!processor.processLine(line)) {
                    break;
                }
            }
            return processor.getResult();
        }
        
        @Override
        public String toString() {
            return "CharSource.wrap(" + Ascii.truncate(this.seq, 30, "...") + ")";
        }
        
        static {
            LINE_SPLITTER = Splitter.on(Pattern.compile("\r\n|\n|\r"));
        }
    }
    
    private static final class EmptyCharSource extends CharSequenceCharSource
    {
        private static final EmptyCharSource INSTANCE;
        
        private EmptyCharSource() {
            super("");
        }
        
        @Override
        public String toString() {
            return "CharSource.empty()";
        }
        
        static {
            INSTANCE = new EmptyCharSource();
        }
    }
    
    private static final class ConcatenatedCharSource extends CharSource
    {
        private final Iterable<? extends CharSource> sources;
        
        ConcatenatedCharSource(final Iterable<? extends CharSource> sources) {
            this.sources = Preconditions.checkNotNull(sources);
        }
        
        @Override
        public Reader openStream() throws IOException {
            return new MultiReader(this.sources.iterator());
        }
        
        @Override
        public boolean isEmpty() throws IOException {
            for (final CharSource source : this.sources) {
                if (!source.isEmpty()) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public String toString() {
            return "CharSource.concat(" + this.sources + ")";
        }
    }
}
