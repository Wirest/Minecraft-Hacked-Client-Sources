// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import java.util.Iterator;
import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.io.OutputStream;
import java.nio.charset.Charset;
import com.google.common.annotations.Beta;

@Beta
public final class Funnels
{
    private Funnels() {
    }
    
    public static Funnel<byte[]> byteArrayFunnel() {
        return ByteArrayFunnel.INSTANCE;
    }
    
    public static Funnel<CharSequence> unencodedCharsFunnel() {
        return UnencodedCharsFunnel.INSTANCE;
    }
    
    public static Funnel<CharSequence> stringFunnel(final Charset charset) {
        return new StringCharsetFunnel(charset);
    }
    
    public static Funnel<Integer> integerFunnel() {
        return IntegerFunnel.INSTANCE;
    }
    
    public static <E> Funnel<Iterable<? extends E>> sequentialFunnel(final Funnel<E> elementFunnel) {
        return (Funnel<Iterable<? extends E>>)new SequentialFunnel((Funnel<Object>)elementFunnel);
    }
    
    public static Funnel<Long> longFunnel() {
        return LongFunnel.INSTANCE;
    }
    
    public static OutputStream asOutputStream(final PrimitiveSink sink) {
        return new SinkAsStream(sink);
    }
    
    private enum ByteArrayFunnel implements Funnel<byte[]>
    {
        INSTANCE;
        
        @Override
        public void funnel(final byte[] from, final PrimitiveSink into) {
            into.putBytes(from);
        }
        
        @Override
        public String toString() {
            return "Funnels.byteArrayFunnel()";
        }
    }
    
    private enum UnencodedCharsFunnel implements Funnel<CharSequence>
    {
        INSTANCE;
        
        @Override
        public void funnel(final CharSequence from, final PrimitiveSink into) {
            into.putUnencodedChars(from);
        }
        
        @Override
        public String toString() {
            return "Funnels.unencodedCharsFunnel()";
        }
    }
    
    private static class StringCharsetFunnel implements Funnel<CharSequence>, Serializable
    {
        private final Charset charset;
        
        StringCharsetFunnel(final Charset charset) {
            this.charset = Preconditions.checkNotNull(charset);
        }
        
        @Override
        public void funnel(final CharSequence from, final PrimitiveSink into) {
            into.putString(from, this.charset);
        }
        
        @Override
        public String toString() {
            return "Funnels.stringFunnel(" + this.charset.name() + ")";
        }
        
        @Override
        public boolean equals(@Nullable final Object o) {
            if (o instanceof StringCharsetFunnel) {
                final StringCharsetFunnel funnel = (StringCharsetFunnel)o;
                return this.charset.equals(funnel.charset);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return StringCharsetFunnel.class.hashCode() ^ this.charset.hashCode();
        }
        
        Object writeReplace() {
            return new SerializedForm(this.charset);
        }
        
        private static class SerializedForm implements Serializable
        {
            private final String charsetCanonicalName;
            private static final long serialVersionUID = 0L;
            
            SerializedForm(final Charset charset) {
                this.charsetCanonicalName = charset.name();
            }
            
            private Object readResolve() {
                return Funnels.stringFunnel(Charset.forName(this.charsetCanonicalName));
            }
        }
    }
    
    private enum IntegerFunnel implements Funnel<Integer>
    {
        INSTANCE;
        
        @Override
        public void funnel(final Integer from, final PrimitiveSink into) {
            into.putInt(from);
        }
        
        @Override
        public String toString() {
            return "Funnels.integerFunnel()";
        }
    }
    
    private static class SequentialFunnel<E> implements Funnel<Iterable<? extends E>>, Serializable
    {
        private final Funnel<E> elementFunnel;
        
        SequentialFunnel(final Funnel<E> elementFunnel) {
            this.elementFunnel = Preconditions.checkNotNull(elementFunnel);
        }
        
        @Override
        public void funnel(final Iterable<? extends E> from, final PrimitiveSink into) {
            for (final E e : from) {
                this.elementFunnel.funnel(e, into);
            }
        }
        
        @Override
        public String toString() {
            return "Funnels.sequentialFunnel(" + this.elementFunnel + ")";
        }
        
        @Override
        public boolean equals(@Nullable final Object o) {
            if (o instanceof SequentialFunnel) {
                final SequentialFunnel<?> funnel = (SequentialFunnel<?>)o;
                return this.elementFunnel.equals(funnel.elementFunnel);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return SequentialFunnel.class.hashCode() ^ this.elementFunnel.hashCode();
        }
    }
    
    private enum LongFunnel implements Funnel<Long>
    {
        INSTANCE;
        
        @Override
        public void funnel(final Long from, final PrimitiveSink into) {
            into.putLong(from);
        }
        
        @Override
        public String toString() {
            return "Funnels.longFunnel()";
        }
    }
    
    private static class SinkAsStream extends OutputStream
    {
        final PrimitiveSink sink;
        
        SinkAsStream(final PrimitiveSink sink) {
            this.sink = Preconditions.checkNotNull(sink);
        }
        
        @Override
        public void write(final int b) {
            this.sink.putByte((byte)b);
        }
        
        @Override
        public void write(final byte[] bytes) {
            this.sink.putBytes(bytes);
        }
        
        @Override
        public void write(final byte[] bytes, final int off, final int len) {
            this.sink.putBytes(bytes, off, len);
        }
        
        @Override
        public String toString() {
            return "Funnels.asOutputStream(" + this.sink + ")";
        }
    }
}
