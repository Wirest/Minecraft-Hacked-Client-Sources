// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.multipart;

import java.util.Iterator;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.nio.charset.Charset;
import io.netty.buffer.ByteBuf;
import java.util.List;
import io.netty.util.AbstractReferenceCounted;

final class InternalAttribute extends AbstractReferenceCounted implements InterfaceHttpData
{
    private final List<ByteBuf> value;
    private final Charset charset;
    private int size;
    
    InternalAttribute(final Charset charset) {
        this.value = new ArrayList<ByteBuf>();
        this.charset = charset;
    }
    
    @Override
    public HttpDataType getHttpDataType() {
        return HttpDataType.InternalAttribute;
    }
    
    public void addValue(final String value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
        final ByteBuf buf = Unpooled.copiedBuffer(value, this.charset);
        this.value.add(buf);
        this.size += buf.readableBytes();
    }
    
    public void addValue(final String value, final int rank) {
        if (value == null) {
            throw new NullPointerException("value");
        }
        final ByteBuf buf = Unpooled.copiedBuffer(value, this.charset);
        this.value.add(rank, buf);
        this.size += buf.readableBytes();
    }
    
    public void setValue(final String value, final int rank) {
        if (value == null) {
            throw new NullPointerException("value");
        }
        final ByteBuf buf = Unpooled.copiedBuffer(value, this.charset);
        final ByteBuf old = this.value.set(rank, buf);
        if (old != null) {
            this.size -= old.readableBytes();
            old.release();
        }
        this.size += buf.readableBytes();
    }
    
    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Attribute)) {
            return false;
        }
        final Attribute attribute = (Attribute)o;
        return this.getName().equalsIgnoreCase(attribute.getName());
    }
    
    @Override
    public int compareTo(final InterfaceHttpData o) {
        if (!(o instanceof InternalAttribute)) {
            throw new ClassCastException("Cannot compare " + this.getHttpDataType() + " with " + o.getHttpDataType());
        }
        return this.compareTo((InternalAttribute)o);
    }
    
    public int compareTo(final InternalAttribute o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }
    
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        for (final ByteBuf elt : this.value) {
            result.append(elt.toString(this.charset));
        }
        return result.toString();
    }
    
    public int size() {
        return this.size;
    }
    
    public ByteBuf toByteBuf() {
        return Unpooled.compositeBuffer().addComponents(this.value).writerIndex(this.size()).readerIndex(0);
    }
    
    @Override
    public String getName() {
        return "InternalAttribute";
    }
    
    @Override
    protected void deallocate() {
    }
}
