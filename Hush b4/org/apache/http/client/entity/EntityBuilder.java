// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.entity;

import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpEntity;
import java.util.Arrays;
import org.apache.http.entity.ContentType;
import java.io.File;
import java.io.Serializable;
import org.apache.http.NameValuePair;
import java.util.List;
import java.io.InputStream;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class EntityBuilder
{
    private String text;
    private byte[] binary;
    private InputStream stream;
    private List<NameValuePair> parameters;
    private Serializable serializable;
    private File file;
    private ContentType contentType;
    private String contentEncoding;
    private boolean chunked;
    private boolean gzipCompress;
    
    EntityBuilder() {
    }
    
    public static EntityBuilder create() {
        return new EntityBuilder();
    }
    
    private void clearContent() {
        this.text = null;
        this.binary = null;
        this.stream = null;
        this.parameters = null;
        this.serializable = null;
        this.file = null;
    }
    
    public String getText() {
        return this.text;
    }
    
    public EntityBuilder setText(final String text) {
        this.clearContent();
        this.text = text;
        return this;
    }
    
    public byte[] getBinary() {
        return this.binary;
    }
    
    public EntityBuilder setBinary(final byte[] binary) {
        this.clearContent();
        this.binary = binary;
        return this;
    }
    
    public InputStream getStream() {
        return this.stream;
    }
    
    public EntityBuilder setStream(final InputStream stream) {
        this.clearContent();
        this.stream = stream;
        return this;
    }
    
    public List<NameValuePair> getParameters() {
        return this.parameters;
    }
    
    public EntityBuilder setParameters(final List<NameValuePair> parameters) {
        this.clearContent();
        this.parameters = parameters;
        return this;
    }
    
    public EntityBuilder setParameters(final NameValuePair... parameters) {
        return this.setParameters(Arrays.asList(parameters));
    }
    
    public Serializable getSerializable() {
        return this.serializable;
    }
    
    public EntityBuilder setSerializable(final Serializable serializable) {
        this.clearContent();
        this.serializable = serializable;
        return this;
    }
    
    public File getFile() {
        return this.file;
    }
    
    public EntityBuilder setFile(final File file) {
        this.clearContent();
        this.file = file;
        return this;
    }
    
    public ContentType getContentType() {
        return this.contentType;
    }
    
    public EntityBuilder setContentType(final ContentType contentType) {
        this.contentType = contentType;
        return this;
    }
    
    public String getContentEncoding() {
        return this.contentEncoding;
    }
    
    public EntityBuilder setContentEncoding(final String contentEncoding) {
        this.contentEncoding = contentEncoding;
        return this;
    }
    
    public boolean isChunked() {
        return this.chunked;
    }
    
    public EntityBuilder chunked() {
        this.chunked = true;
        return this;
    }
    
    public boolean isGzipCompress() {
        return this.gzipCompress;
    }
    
    public EntityBuilder gzipCompress() {
        this.gzipCompress = true;
        return this;
    }
    
    private ContentType getContentOrDefault(final ContentType def) {
        return (this.contentType != null) ? this.contentType : def;
    }
    
    public HttpEntity build() {
        AbstractHttpEntity e;
        if (this.text != null) {
            e = new StringEntity(this.text, this.getContentOrDefault(ContentType.DEFAULT_TEXT));
        }
        else if (this.binary != null) {
            e = new ByteArrayEntity(this.binary, this.getContentOrDefault(ContentType.DEFAULT_BINARY));
        }
        else if (this.stream != null) {
            e = new InputStreamEntity(this.stream, 1L, this.getContentOrDefault(ContentType.DEFAULT_BINARY));
        }
        else if (this.parameters != null) {
            e = new UrlEncodedFormEntity(this.parameters, (this.contentType != null) ? this.contentType.getCharset() : null);
        }
        else if (this.serializable != null) {
            e = new SerializableEntity(this.serializable);
            e.setContentType(ContentType.DEFAULT_BINARY.toString());
        }
        else if (this.file != null) {
            e = new FileEntity(this.file, this.getContentOrDefault(ContentType.DEFAULT_BINARY));
        }
        else {
            e = new BasicHttpEntity();
        }
        if (e.getContentType() != null && this.contentType != null) {
            e.setContentType(this.contentType.toString());
        }
        e.setContentEncoding(this.contentEncoding);
        e.setChunked(this.chunked);
        if (this.gzipCompress) {
            return new GzipCompressingEntity(e);
        }
        return e;
    }
}
