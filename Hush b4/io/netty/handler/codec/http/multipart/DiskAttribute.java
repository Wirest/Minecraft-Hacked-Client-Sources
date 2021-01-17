// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBufHolder;
import io.netty.util.ReferenceCounted;
import io.netty.channel.ChannelException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import io.netty.handler.codec.http.HttpConstants;

public class DiskAttribute extends AbstractDiskHttpData implements Attribute
{
    public static String baseDirectory;
    public static boolean deleteOnExitTemporaryFile;
    public static final String prefix = "Attr_";
    public static final String postfix = ".att";
    
    public DiskAttribute(final String name) {
        super(name, HttpConstants.DEFAULT_CHARSET, 0L);
    }
    
    public DiskAttribute(final String name, final String value) throws IOException {
        super(name, HttpConstants.DEFAULT_CHARSET, 0L);
        this.setValue(value);
    }
    
    @Override
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return InterfaceHttpData.HttpDataType.Attribute;
    }
    
    @Override
    public String getValue() throws IOException {
        final byte[] bytes = this.get();
        return new String(bytes, this.charset.name());
    }
    
    @Override
    public void setValue(final String value) throws IOException {
        if (value == null) {
            throw new NullPointerException("value");
        }
        final byte[] bytes = value.getBytes(this.charset.name());
        final ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
        if (this.definedSize > 0L) {
            this.definedSize = buffer.readableBytes();
        }
        this.setContent(buffer);
    }
    
    @Override
    public void addContent(final ByteBuf buffer, final boolean last) throws IOException {
        final int localsize = buffer.readableBytes();
        if (this.definedSize > 0L && this.definedSize < this.size + localsize) {
            this.definedSize = this.size + localsize;
        }
        super.addContent(buffer, last);
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
        if (!(o instanceof Attribute)) {
            throw new ClassCastException("Cannot compare " + this.getHttpDataType() + " with " + o.getHttpDataType());
        }
        return this.compareTo((Attribute)o);
    }
    
    public int compareTo(final Attribute o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }
    
    @Override
    public String toString() {
        try {
            return this.getName() + '=' + this.getValue();
        }
        catch (IOException e) {
            return this.getName() + "=IoException";
        }
    }
    
    @Override
    protected boolean deleteOnExit() {
        return DiskAttribute.deleteOnExitTemporaryFile;
    }
    
    @Override
    protected String getBaseDirectory() {
        return DiskAttribute.baseDirectory;
    }
    
    @Override
    protected String getDiskFilename() {
        return this.getName() + ".att";
    }
    
    @Override
    protected String getPostfix() {
        return ".att";
    }
    
    @Override
    protected String getPrefix() {
        return "Attr_";
    }
    
    @Override
    public Attribute copy() {
        final DiskAttribute attr = new DiskAttribute(this.getName());
        attr.setCharset(this.getCharset());
        final ByteBuf content = this.content();
        if (content != null) {
            try {
                attr.setContent(content.copy());
            }
            catch (IOException e) {
                throw new ChannelException(e);
            }
        }
        return attr;
    }
    
    @Override
    public Attribute duplicate() {
        final DiskAttribute attr = new DiskAttribute(this.getName());
        attr.setCharset(this.getCharset());
        final ByteBuf content = this.content();
        if (content != null) {
            try {
                attr.setContent(content.duplicate());
            }
            catch (IOException e) {
                throw new ChannelException(e);
            }
        }
        return attr;
    }
    
    @Override
    public Attribute retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public Attribute retain() {
        super.retain();
        return this;
    }
    
    static {
        DiskAttribute.deleteOnExitTemporaryFile = true;
    }
}
