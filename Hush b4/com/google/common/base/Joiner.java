// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import com.google.common.annotations.Beta;
import java.util.Map;
import java.util.AbstractList;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Iterator;
import java.io.IOException;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public class Joiner
{
    private final String separator;
    
    public static Joiner on(final String separator) {
        return new Joiner(separator);
    }
    
    public static Joiner on(final char separator) {
        return new Joiner(String.valueOf(separator));
    }
    
    private Joiner(final String separator) {
        this.separator = Preconditions.checkNotNull(separator);
    }
    
    private Joiner(final Joiner prototype) {
        this.separator = prototype.separator;
    }
    
    public <A extends Appendable> A appendTo(final A appendable, final Iterable<?> parts) throws IOException {
        return this.appendTo(appendable, parts.iterator());
    }
    
    public <A extends Appendable> A appendTo(final A appendable, final Iterator<?> parts) throws IOException {
        Preconditions.checkNotNull(appendable);
        if (parts.hasNext()) {
            appendable.append(this.toString(parts.next()));
            while (parts.hasNext()) {
                appendable.append(this.separator);
                appendable.append(this.toString(parts.next()));
            }
        }
        return appendable;
    }
    
    public final <A extends Appendable> A appendTo(final A appendable, final Object[] parts) throws IOException {
        return this.appendTo(appendable, Arrays.asList(parts));
    }
    
    public final <A extends Appendable> A appendTo(final A appendable, @Nullable final Object first, @Nullable final Object second, final Object... rest) throws IOException {
        return this.appendTo(appendable, iterable(first, second, rest));
    }
    
    public final StringBuilder appendTo(final StringBuilder builder, final Iterable<?> parts) {
        return this.appendTo(builder, parts.iterator());
    }
    
    public final StringBuilder appendTo(final StringBuilder builder, final Iterator<?> parts) {
        try {
            this.appendTo(builder, parts);
        }
        catch (IOException impossible) {
            throw new AssertionError((Object)impossible);
        }
        return builder;
    }
    
    public final StringBuilder appendTo(final StringBuilder builder, final Object[] parts) {
        return this.appendTo(builder, (Iterable<?>)Arrays.asList(parts));
    }
    
    public final StringBuilder appendTo(final StringBuilder builder, @Nullable final Object first, @Nullable final Object second, final Object... rest) {
        return this.appendTo(builder, (Iterable<?>)iterable(first, second, rest));
    }
    
    public final String join(final Iterable<?> parts) {
        return this.join(parts.iterator());
    }
    
    public final String join(final Iterator<?> parts) {
        return this.appendTo(new StringBuilder(), parts).toString();
    }
    
    public final String join(final Object[] parts) {
        return this.join(Arrays.asList(parts));
    }
    
    public final String join(@Nullable final Object first, @Nullable final Object second, final Object... rest) {
        return this.join(iterable(first, second, rest));
    }
    
    @CheckReturnValue
    public Joiner useForNull(final String nullText) {
        Preconditions.checkNotNull(nullText);
        return new Joiner(this) {
            @Override
            CharSequence toString(@Nullable final Object part) {
                return (part == null) ? nullText : Joiner.this.toString(part);
            }
            
            @Override
            public Joiner useForNull(final String nullText) {
                throw new UnsupportedOperationException("already specified useForNull");
            }
            
            @Override
            public Joiner skipNulls() {
                throw new UnsupportedOperationException("already specified useForNull");
            }
        };
    }
    
    @CheckReturnValue
    public Joiner skipNulls() {
        return new Joiner(this) {
            @Override
            public <A extends Appendable> A appendTo(final A appendable, final Iterator<?> parts) throws IOException {
                Preconditions.checkNotNull(appendable, (Object)"appendable");
                Preconditions.checkNotNull(parts, (Object)"parts");
                while (parts.hasNext()) {
                    final Object part = parts.next();
                    if (part != null) {
                        appendable.append(Joiner.this.toString(part));
                        break;
                    }
                }
                while (parts.hasNext()) {
                    final Object part = parts.next();
                    if (part != null) {
                        appendable.append(Joiner.this.separator);
                        appendable.append(Joiner.this.toString(part));
                    }
                }
                return appendable;
            }
            
            @Override
            public Joiner useForNull(final String nullText) {
                throw new UnsupportedOperationException("already specified skipNulls");
            }
            
            @Override
            public MapJoiner withKeyValueSeparator(final String kvs) {
                throw new UnsupportedOperationException("can't use .skipNulls() with maps");
            }
        };
    }
    
    @CheckReturnValue
    public MapJoiner withKeyValueSeparator(final String keyValueSeparator) {
        return new MapJoiner(this, keyValueSeparator);
    }
    
    CharSequence toString(final Object part) {
        Preconditions.checkNotNull(part);
        return (part instanceof CharSequence) ? ((CharSequence)part) : part.toString();
    }
    
    private static Iterable<Object> iterable(final Object first, final Object second, final Object[] rest) {
        Preconditions.checkNotNull(rest);
        return new AbstractList<Object>() {
            @Override
            public int size() {
                return rest.length + 2;
            }
            
            @Override
            public Object get(final int index) {
                switch (index) {
                    case 0: {
                        return first;
                    }
                    case 1: {
                        return second;
                    }
                    default: {
                        return rest[index - 2];
                    }
                }
            }
        };
    }
    
    public static final class MapJoiner
    {
        private final Joiner joiner;
        private final String keyValueSeparator;
        
        private MapJoiner(final Joiner joiner, final String keyValueSeparator) {
            this.joiner = joiner;
            this.keyValueSeparator = Preconditions.checkNotNull(keyValueSeparator);
        }
        
        public <A extends Appendable> A appendTo(final A appendable, final Map<?, ?> map) throws IOException {
            return this.appendTo(appendable, map.entrySet());
        }
        
        public StringBuilder appendTo(final StringBuilder builder, final Map<?, ?> map) {
            return this.appendTo(builder, (Iterable<? extends Map.Entry<?, ?>>)map.entrySet());
        }
        
        public String join(final Map<?, ?> map) {
            return this.join(map.entrySet());
        }
        
        @Beta
        public <A extends Appendable> A appendTo(final A appendable, final Iterable<? extends Map.Entry<?, ?>> entries) throws IOException {
            return this.appendTo(appendable, entries.iterator());
        }
        
        @Beta
        public <A extends Appendable> A appendTo(final A appendable, final Iterator<? extends Map.Entry<?, ?>> parts) throws IOException {
            Preconditions.checkNotNull(appendable);
            if (parts.hasNext()) {
                final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)parts.next();
                appendable.append(this.joiner.toString(entry.getKey()));
                appendable.append(this.keyValueSeparator);
                appendable.append(this.joiner.toString(entry.getValue()));
                while (parts.hasNext()) {
                    appendable.append(this.joiner.separator);
                    final Map.Entry<?, ?> e = (Map.Entry<?, ?>)parts.next();
                    appendable.append(this.joiner.toString(e.getKey()));
                    appendable.append(this.keyValueSeparator);
                    appendable.append(this.joiner.toString(e.getValue()));
                }
            }
            return appendable;
        }
        
        @Beta
        public StringBuilder appendTo(final StringBuilder builder, final Iterable<? extends Map.Entry<?, ?>> entries) {
            return this.appendTo(builder, entries.iterator());
        }
        
        @Beta
        public StringBuilder appendTo(final StringBuilder builder, final Iterator<? extends Map.Entry<?, ?>> entries) {
            try {
                this.appendTo(builder, entries);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
            return builder;
        }
        
        @Beta
        public String join(final Iterable<? extends Map.Entry<?, ?>> entries) {
            return this.join(entries.iterator());
        }
        
        @Beta
        public String join(final Iterator<? extends Map.Entry<?, ?>> entries) {
            return this.appendTo(new StringBuilder(), entries).toString();
        }
        
        @CheckReturnValue
        public MapJoiner useForNull(final String nullText) {
            return new MapJoiner(this.joiner.useForNull(nullText), this.keyValueSeparator);
        }
    }
}
