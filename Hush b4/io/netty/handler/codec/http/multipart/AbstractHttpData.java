// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBufHolder;
import io.netty.util.ReferenceCounted;
import java.io.IOException;
import io.netty.channel.ChannelException;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpConstants;
import java.nio.charset.Charset;
import java.util.regex.Pattern;
import io.netty.util.AbstractReferenceCounted;

public abstract class AbstractHttpData extends AbstractReferenceCounted implements HttpData
{
    private static final Pattern STRIP_PATTERN;
    private static final Pattern REPLACE_PATTERN;
    protected final String name;
    protected long definedSize;
    protected long size;
    protected Charset charset;
    protected boolean completed;
    
    protected AbstractHttpData(String name, final Charset charset, final long size) {
        this.charset = HttpConstants.DEFAULT_CHARSET;
        if (name == null) {
            throw new NullPointerException("name");
        }
        name = AbstractHttpData.REPLACE_PATTERN.matcher(name).replaceAll(" ");
        name = AbstractHttpData.STRIP_PATTERN.matcher(name).replaceAll("");
        if (name.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        this.name = name;
        if (charset != null) {
            this.setCharset(charset);
        }
        this.definedSize = size;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public boolean isCompleted() {
        return this.completed;
    }
    
    @Override
    public Charset getCharset() {
        return this.charset;
    }
    
    @Override
    public void setCharset(final Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        this.charset = charset;
    }
    
    @Override
    public long length() {
        return this.size;
    }
    
    @Override
    public ByteBuf content() {
        try {
            return this.getByteBuf();
        }
        catch (IOException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    protected void deallocate() {
        this.delete();
    }
    
    @Override
    public HttpData retain() {
        super.retain();
        return this;
    }
    
    @Override
    public HttpData retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    static {
        STRIP_PATTERN = Pattern.compile("(?:^\\s+|\\s+$|\\n)");
        REPLACE_PATTERN = Pattern.compile("[\\r\\t]");
    }
}
