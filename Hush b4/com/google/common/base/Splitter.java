// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import java.util.LinkedHashMap;
import java.util.Map;
import com.google.common.annotations.Beta;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.CheckReturnValue;
import com.google.common.annotations.GwtIncompatible;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Splitter
{
    private final CharMatcher trimmer;
    private final boolean omitEmptyStrings;
    private final Strategy strategy;
    private final int limit;
    
    private Splitter(final Strategy strategy) {
        this(strategy, false, CharMatcher.NONE, Integer.MAX_VALUE);
    }
    
    private Splitter(final Strategy strategy, final boolean omitEmptyStrings, final CharMatcher trimmer, final int limit) {
        this.strategy = strategy;
        this.omitEmptyStrings = omitEmptyStrings;
        this.trimmer = trimmer;
        this.limit = limit;
    }
    
    public static Splitter on(final char separator) {
        return on(CharMatcher.is(separator));
    }
    
    public static Splitter on(final CharMatcher separatorMatcher) {
        Preconditions.checkNotNull(separatorMatcher);
        return new Splitter(new Strategy() {
            @Override
            public SplittingIterator iterator(final Splitter splitter, final CharSequence toSplit) {
                return new SplittingIterator(splitter, toSplit) {
                    @Override
                    int separatorStart(final int start) {
                        return separatorMatcher.indexIn(this.toSplit, start);
                    }
                    
                    @Override
                    int separatorEnd(final int separatorPosition) {
                        return separatorPosition + 1;
                    }
                };
            }
        });
    }
    
    public static Splitter on(final String separator) {
        Preconditions.checkArgument(separator.length() != 0, (Object)"The separator may not be the empty string.");
        return new Splitter(new Strategy() {
            @Override
            public SplittingIterator iterator(final Splitter splitter, final CharSequence toSplit) {
                return new SplittingIterator(splitter, toSplit) {
                    public int separatorStart(final int start) {
                        final int separatorLength = separator.length();
                        int p = start;
                        final int last = this.toSplit.length() - separatorLength;
                    Label_0026:
                        while (p <= last) {
                            for (int i = 0; i < separatorLength; ++i) {
                                if (this.toSplit.charAt(i + p) != separator.charAt(i)) {
                                    ++p;
                                    continue Label_0026;
                                }
                            }
                            return p;
                        }
                        return -1;
                    }
                    
                    public int separatorEnd(final int separatorPosition) {
                        return separatorPosition + separator.length();
                    }
                };
            }
        });
    }
    
    @GwtIncompatible("java.util.regex")
    public static Splitter on(final Pattern separatorPattern) {
        Preconditions.checkNotNull(separatorPattern);
        Preconditions.checkArgument(!separatorPattern.matcher("").matches(), "The pattern may not match the empty string: %s", separatorPattern);
        return new Splitter(new Strategy() {
            @Override
            public SplittingIterator iterator(final Splitter splitter, final CharSequence toSplit) {
                final Matcher matcher = separatorPattern.matcher(toSplit);
                return new SplittingIterator(splitter, toSplit) {
                    public int separatorStart(final int start) {
                        return matcher.find(start) ? matcher.start() : -1;
                    }
                    
                    public int separatorEnd(final int separatorPosition) {
                        return matcher.end();
                    }
                };
            }
        });
    }
    
    @GwtIncompatible("java.util.regex")
    public static Splitter onPattern(final String separatorPattern) {
        return on(Pattern.compile(separatorPattern));
    }
    
    public static Splitter fixedLength(final int length) {
        Preconditions.checkArgument(length > 0, (Object)"The length may not be less than 1");
        return new Splitter(new Strategy() {
            @Override
            public SplittingIterator iterator(final Splitter splitter, final CharSequence toSplit) {
                return new SplittingIterator(splitter, toSplit) {
                    public int separatorStart(final int start) {
                        final int nextChunkStart = start + length;
                        return (nextChunkStart < this.toSplit.length()) ? nextChunkStart : -1;
                    }
                    
                    public int separatorEnd(final int separatorPosition) {
                        return separatorPosition;
                    }
                };
            }
        });
    }
    
    @CheckReturnValue
    public Splitter omitEmptyStrings() {
        return new Splitter(this.strategy, true, this.trimmer, this.limit);
    }
    
    @CheckReturnValue
    public Splitter limit(final int limit) {
        Preconditions.checkArgument(limit > 0, "must be greater than zero: %s", limit);
        return new Splitter(this.strategy, this.omitEmptyStrings, this.trimmer, limit);
    }
    
    @CheckReturnValue
    public Splitter trimResults() {
        return this.trimResults(CharMatcher.WHITESPACE);
    }
    
    @CheckReturnValue
    public Splitter trimResults(final CharMatcher trimmer) {
        Preconditions.checkNotNull(trimmer);
        return new Splitter(this.strategy, this.omitEmptyStrings, trimmer, this.limit);
    }
    
    public Iterable<String> split(final CharSequence sequence) {
        Preconditions.checkNotNull(sequence);
        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return Splitter.this.splittingIterator(sequence);
            }
            
            @Override
            public String toString() {
                return Joiner.on(", ").appendTo(new StringBuilder().append('['), (Iterable<?>)this).append(']').toString();
            }
        };
    }
    
    private Iterator<String> splittingIterator(final CharSequence sequence) {
        return this.strategy.iterator(this, sequence);
    }
    
    @Beta
    public List<String> splitToList(final CharSequence sequence) {
        Preconditions.checkNotNull(sequence);
        final Iterator<String> iterator = this.splittingIterator(sequence);
        final List<String> result = new ArrayList<String>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return Collections.unmodifiableList((List<? extends String>)result);
    }
    
    @CheckReturnValue
    @Beta
    public MapSplitter withKeyValueSeparator(final String separator) {
        return this.withKeyValueSeparator(on(separator));
    }
    
    @CheckReturnValue
    @Beta
    public MapSplitter withKeyValueSeparator(final char separator) {
        return this.withKeyValueSeparator(on(separator));
    }
    
    @CheckReturnValue
    @Beta
    public MapSplitter withKeyValueSeparator(final Splitter keyValueSplitter) {
        return new MapSplitter(this, keyValueSplitter);
    }
    
    @Beta
    public static final class MapSplitter
    {
        private static final String INVALID_ENTRY_MESSAGE = "Chunk [%s] is not a valid entry";
        private final Splitter outerSplitter;
        private final Splitter entrySplitter;
        
        private MapSplitter(final Splitter outerSplitter, final Splitter entrySplitter) {
            this.outerSplitter = outerSplitter;
            this.entrySplitter = Preconditions.checkNotNull(entrySplitter);
        }
        
        public Map<String, String> split(final CharSequence sequence) {
            final Map<String, String> map = new LinkedHashMap<String, String>();
            for (final String entry : this.outerSplitter.split(sequence)) {
                final Iterator<String> entryFields = this.entrySplitter.splittingIterator(entry);
                Preconditions.checkArgument(entryFields.hasNext(), "Chunk [%s] is not a valid entry", entry);
                final String key = entryFields.next();
                Preconditions.checkArgument(!map.containsKey(key), "Duplicate key [%s] found.", key);
                Preconditions.checkArgument(entryFields.hasNext(), "Chunk [%s] is not a valid entry", entry);
                final String value = entryFields.next();
                map.put(key, value);
                Preconditions.checkArgument(!entryFields.hasNext(), "Chunk [%s] is not a valid entry", entry);
            }
            return Collections.unmodifiableMap((Map<? extends String, ? extends String>)map);
        }
    }
    
    private abstract static class SplittingIterator extends AbstractIterator<String>
    {
        final CharSequence toSplit;
        final CharMatcher trimmer;
        final boolean omitEmptyStrings;
        int offset;
        int limit;
        
        abstract int separatorStart(final int p0);
        
        abstract int separatorEnd(final int p0);
        
        protected SplittingIterator(final Splitter splitter, final CharSequence toSplit) {
            this.offset = 0;
            this.trimmer = splitter.trimmer;
            this.omitEmptyStrings = splitter.omitEmptyStrings;
            this.limit = splitter.limit;
            this.toSplit = toSplit;
        }
        
        @Override
        protected String computeNext() {
            int nextStart = this.offset;
            while (this.offset != -1) {
                int start = nextStart;
                final int separatorPosition = this.separatorStart(this.offset);
                int end;
                if (separatorPosition == -1) {
                    end = this.toSplit.length();
                    this.offset = -1;
                }
                else {
                    end = separatorPosition;
                    this.offset = this.separatorEnd(separatorPosition);
                }
                if (this.offset == nextStart) {
                    ++this.offset;
                    if (this.offset < this.toSplit.length()) {
                        continue;
                    }
                    this.offset = -1;
                }
                else {
                    while (start < end && this.trimmer.matches(this.toSplit.charAt(start))) {
                        ++start;
                    }
                    while (end > start && this.trimmer.matches(this.toSplit.charAt(end - 1))) {
                        --end;
                    }
                    if (!this.omitEmptyStrings || start != end) {
                        if (this.limit == 1) {
                            end = this.toSplit.length();
                            this.offset = -1;
                            while (end > start && this.trimmer.matches(this.toSplit.charAt(end - 1))) {
                                --end;
                            }
                        }
                        else {
                            --this.limit;
                        }
                        return this.toSplit.subSequence(start, end).toString();
                    }
                    nextStart = this.offset;
                }
            }
            return this.endOfData();
        }
    }
    
    private interface Strategy
    {
        Iterator<String> iterator(final Splitter p0, final CharSequence p1);
    }
}
