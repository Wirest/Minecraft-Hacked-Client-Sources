// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBufHolder;
import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import io.netty.channel.ChannelException;
import java.nio.charset.Charset;

public class MemoryFileUpload extends AbstractMemoryHttpData implements FileUpload
{
    private String filename;
    private String contentType;
    private String contentTransferEncoding;
    
    public MemoryFileUpload(final String name, final String filename, final String contentType, final String contentTransferEncoding, final Charset charset, final long size) {
        super(name, charset, size);
        this.setFilename(filename);
        this.setContentType(contentType);
        this.setContentTransferEncoding(contentTransferEncoding);
    }
    
    @Override
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return InterfaceHttpData.HttpDataType.FileUpload;
    }
    
    @Override
    public String getFilename() {
        return this.filename;
    }
    
    @Override
    public void setFilename(final String filename) {
        if (filename == null) {
            throw new NullPointerException("filename");
        }
        this.filename = filename;
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
        if (!(o instanceof FileUpload)) {
            throw new ClassCastException("Cannot compare " + this.getHttpDataType() + " with " + o.getHttpDataType());
        }
        return this.compareTo((FileUpload)o);
    }
    
    public int compareTo(final FileUpload o) {
        final int v = this.getName().compareToIgnoreCase(o.getName());
        if (v != 0) {
            return v;
        }
        return v;
    }
    
    @Override
    public void setContentType(final String contentType) {
        if (contentType == null) {
            throw new NullPointerException("contentType");
        }
        this.contentType = contentType;
    }
    
    @Override
    public String getContentType() {
        return this.contentType;
    }
    
    @Override
    public String getContentTransferEncoding() {
        return this.contentTransferEncoding;
    }
    
    @Override
    public void setContentTransferEncoding(final String contentTransferEncoding) {
        this.contentTransferEncoding = contentTransferEncoding;
    }
    
    @Override
    public String toString() {
        return "Content-Disposition: form-data; name=\"" + this.getName() + "\"; " + "filename" + "=\"" + this.filename + "\"\r\n" + "Content-Type" + ": " + this.contentType + ((this.charset != null) ? ("; charset=" + this.charset + "\r\n") : "\r\n") + "Content-Length" + ": " + this.length() + "\r\n" + "Completed: " + this.isCompleted() + "\r\nIsInMemory: " + this.isInMemory();
    }
    
    @Override
    public FileUpload copy() {
        final MemoryFileUpload upload = new MemoryFileUpload(this.getName(), this.getFilename(), this.getContentType(), this.getContentTransferEncoding(), this.getCharset(), this.size);
        final ByteBuf buf = this.content();
        if (buf != null) {
            try {
                upload.setContent(buf.copy());
                return upload;
            }
            catch (IOException e) {
                throw new ChannelException(e);
            }
        }
        return upload;
    }
    
    @Override
    public FileUpload duplicate() {
        final MemoryFileUpload upload = new MemoryFileUpload(this.getName(), this.getFilename(), this.getContentType(), this.getContentTransferEncoding(), this.getCharset(), this.size);
        final ByteBuf buf = this.content();
        if (buf != null) {
            try {
                upload.setContent(buf.duplicate());
                return upload;
            }
            catch (IOException e) {
                throw new ChannelException(e);
            }
        }
        return upload;
    }
    
    @Override
    public FileUpload retain() {
        super.retain();
        return this;
    }
    
    @Override
    public FileUpload retain(final int increment) {
        super.retain(increment);
        return this;
    }
}
