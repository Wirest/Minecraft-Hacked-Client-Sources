// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;
import java.net.URLEncoder;
import java.net.URISyntaxException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.Charset;

public class QueryStringEncoder
{
    private final Charset charset;
    private final String uri;
    private final List<Param> params;
    
    public QueryStringEncoder(final String uri) {
        this(uri, HttpConstants.DEFAULT_CHARSET);
    }
    
    public QueryStringEncoder(final String uri, final Charset charset) {
        this.params = new ArrayList<Param>();
        if (uri == null) {
            throw new NullPointerException("getUri");
        }
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        this.uri = uri;
        this.charset = charset;
    }
    
    public void addParam(final String name, final String value) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        this.params.add(new Param(name, value));
    }
    
    public URI toUri() throws URISyntaxException {
        return new URI(this.toString());
    }
    
    @Override
    public String toString() {
        if (this.params.isEmpty()) {
            return this.uri;
        }
        final StringBuilder sb = new StringBuilder(this.uri).append('?');
        for (int i = 0; i < this.params.size(); ++i) {
            final Param param = this.params.get(i);
            sb.append(encodeComponent(param.name, this.charset));
            if (param.value != null) {
                sb.append('=');
                sb.append(encodeComponent(param.value, this.charset));
            }
            if (i != this.params.size() - 1) {
                sb.append('&');
            }
        }
        return sb.toString();
    }
    
    private static String encodeComponent(final String s, final Charset charset) {
        try {
            return URLEncoder.encode(s, charset.name()).replace("+", "%20");
        }
        catch (UnsupportedEncodingException ignored) {
            throw new UnsupportedCharsetException(charset.name());
        }
    }
    
    private static final class Param
    {
        final String name;
        final String value;
        
        Param(final String name, final String value) {
            this.value = value;
            this.name = name;
        }
    }
}
