// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.entity;

import java.nio.charset.Charset;
import java.io.UnsupportedEncodingException;
import org.apache.http.entity.ContentType;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;
import org.apache.http.NameValuePair;
import java.util.List;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.entity.StringEntity;

@NotThreadSafe
public class UrlEncodedFormEntity extends StringEntity
{
    public UrlEncodedFormEntity(final List<? extends NameValuePair> parameters, final String charset) throws UnsupportedEncodingException {
        super(URLEncodedUtils.format(parameters, (charset != null) ? charset : HTTP.DEF_CONTENT_CHARSET.name()), ContentType.create("application/x-www-form-urlencoded", charset));
    }
    
    public UrlEncodedFormEntity(final Iterable<? extends NameValuePair> parameters, final Charset charset) {
        super(URLEncodedUtils.format(parameters, (charset != null) ? charset : HTTP.DEF_CONTENT_CHARSET), ContentType.create("application/x-www-form-urlencoded", charset));
    }
    
    public UrlEncodedFormEntity(final List<? extends NameValuePair> parameters) throws UnsupportedEncodingException {
        this(parameters, (Charset)null);
    }
    
    public UrlEncodedFormEntity(final Iterable<? extends NameValuePair> parameters) {
        this(parameters, null);
    }
}
