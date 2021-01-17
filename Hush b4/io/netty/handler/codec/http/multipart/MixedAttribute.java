// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBufHolder;
import io.netty.util.ReferenceCounted;
import java.io.InputStream;
import java.io.File;
import java.nio.charset.Charset;
import io.netty.buffer.ByteBuf;
import java.io.IOException;

public class MixedAttribute implements Attribute
{
    private Attribute attribute;
    private final long limitSize;
    
    public MixedAttribute(final String name, final long limitSize) {
        this.limitSize = limitSize;
        this.attribute = new MemoryAttribute(name);
    }
    
    public MixedAttribute(final String name, final String value, final long limitSize) {
        this.limitSize = limitSize;
        if (value.length() > this.limitSize) {
            try {
                this.attribute = new DiskAttribute(name, value);
            }
            catch (IOException e) {
                try {
                    this.attribute = new MemoryAttribute(name, value);
                }
                catch (IOException e2) {
                    throw new IllegalArgumentException(e);
                }
            }
        }
        else {
            try {
                this.attribute = new MemoryAttribute(name, value);
            }
            catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
    
    @Override
    public void addContent(final ByteBuf buffer, final boolean last) throws IOException {
        if (this.attribute instanceof MemoryAttribute && this.attribute.length() + buffer.readableBytes() > this.limitSize) {
            final DiskAttribute diskAttribute = new DiskAttribute(this.attribute.getName());
            if (((MemoryAttribute)this.attribute).getByteBuf() != null) {
                diskAttribute.addContent(((MemoryAttribute)this.attribute).getByteBuf(), false);
            }
            this.attribute = diskAttribute;
        }
        this.attribute.addContent(buffer, last);
    }
    
    @Override
    public void delete() {
        this.attribute.delete();
    }
    
    @Override
    public byte[] get() throws IOException {
        return this.attribute.get();
    }
    
    @Override
    public ByteBuf getByteBuf() throws IOException {
        return this.attribute.getByteBuf();
    }
    
    @Override
    public Charset getCharset() {
        return this.attribute.getCharset();
    }
    
    @Override
    public String getString() throws IOException {
        return this.attribute.getString();
    }
    
    @Override
    public String getString(final Charset encoding) throws IOException {
        return this.attribute.getString(encoding);
    }
    
    @Override
    public boolean isCompleted() {
        return this.attribute.isCompleted();
    }
    
    @Override
    public boolean isInMemory() {
        return this.attribute.isInMemory();
    }
    
    @Override
    public long length() {
        return this.attribute.length();
    }
    
    @Override
    public boolean renameTo(final File dest) throws IOException {
        return this.attribute.renameTo(dest);
    }
    
    @Override
    public void setCharset(final Charset charset) {
        this.attribute.setCharset(charset);
    }
    
    @Override
    public void setContent(final ByteBuf buffer) throws IOException {
        if (buffer.readableBytes() > this.limitSize && this.attribute instanceof MemoryAttribute) {
            this.attribute = new DiskAttribute(this.attribute.getName());
        }
        this.attribute.setContent(buffer);
    }
    
    @Override
    public void setContent(final File file) throws IOException {
        if (file.length() > this.limitSize && this.attribute instanceof MemoryAttribute) {
            this.attribute = new DiskAttribute(this.attribute.getName());
        }
        this.attribute.setContent(file);
    }
    
    @Override
    public void setContent(final InputStream inputStream) throws IOException {
        if (this.attribute instanceof MemoryAttribute) {
            this.attribute = new DiskAttribute(this.attribute.getName());
        }
        this.attribute.setContent(inputStream);
    }
    
    @Override
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return this.attribute.getHttpDataType();
    }
    
    @Override
    public String getName() {
        return this.attribute.getName();
    }
    
    @Override
    public int compareTo(final InterfaceHttpData o) {
        return this.attribute.compareTo(o);
    }
    
    @Override
    public String toString() {
        return "Mixed: " + this.attribute.toString();
    }
    
    @Override
    public String getValue() throws IOException {
        return this.attribute.getValue();
    }
    
    @Override
    public void setValue(final String value) throws IOException {
        this.attribute.setValue(value);
    }
    
    @Override
    public ByteBuf getChunk(final int length) throws IOException {
        return this.attribute.getChunk(length);
    }
    
    @Override
    public File getFile() throws IOException {
        return this.attribute.getFile();
    }
    
    @Override
    public Attribute copy() {
        return this.attribute.copy();
    }
    
    @Override
    public Attribute duplicate() {
        return this.attribute.duplicate();
    }
    
    @Override
    public ByteBuf content() {
        return this.attribute.content();
    }
    
    @Override
    public int refCnt() {
        return this.attribute.refCnt();
    }
    
    @Override
    public Attribute retain() {
        this.attribute.retain();
        return this;
    }
    
    @Override
    public Attribute retain(final int increment) {
        this.attribute.retain(increment);
        return this;
    }
    
    @Override
    public boolean release() {
        return this.attribute.release();
    }
    
    @Override
    public boolean release(final int decrement) {
        return this.attribute.release(decrement);
    }
}
