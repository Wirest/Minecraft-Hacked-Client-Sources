// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.util;

import java.io.Reader;
import java.io.InputStreamReader;
import org.apache.http.protocol.HTTP;
import java.nio.charset.UnsupportedCharsetException;
import java.io.UnsupportedEncodingException;
import org.apache.http.entity.ContentType;
import java.nio.charset.Charset;
import org.apache.http.ParseException;
import org.apache.http.NameValuePair;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import java.io.InputStream;
import java.io.IOException;
import org.apache.http.HttpEntity;

public final class EntityUtils
{
    private EntityUtils() {
    }
    
    public static void consumeQuietly(final HttpEntity entity) {
        try {
            consume(entity);
        }
        catch (IOException ex) {}
    }
    
    public static void consume(final HttpEntity entity) throws IOException {
        if (entity == null) {
            return;
        }
        if (entity.isStreaming()) {
            final InputStream instream = entity.getContent();
            if (instream != null) {
                instream.close();
            }
        }
    }
    
    public static void updateEntity(final HttpResponse response, final HttpEntity entity) throws IOException {
        Args.notNull(response, "Response");
        consume(response.getEntity());
        response.setEntity(entity);
    }
    
    public static byte[] toByteArray(final HttpEntity entity) throws IOException {
        Args.notNull(entity, "Entity");
        final InputStream instream = entity.getContent();
        if (instream == null) {
            return null;
        }
        try {
            Args.check(entity.getContentLength() <= 2147483647L, "HTTP entity too large to be buffered in memory");
            int i = (int)entity.getContentLength();
            if (i < 0) {
                i = 4096;
            }
            final ByteArrayBuffer buffer = new ByteArrayBuffer(i);
            final byte[] tmp = new byte[4096];
            int l;
            while ((l = instream.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
            return buffer.toByteArray();
        }
        finally {
            instream.close();
        }
    }
    
    @Deprecated
    public static String getContentCharSet(final HttpEntity entity) throws ParseException {
        Args.notNull(entity, "Entity");
        String charset = null;
        if (entity.getContentType() != null) {
            final HeaderElement[] values = entity.getContentType().getElements();
            if (values.length > 0) {
                final NameValuePair param = values[0].getParameterByName("charset");
                if (param != null) {
                    charset = param.getValue();
                }
            }
        }
        return charset;
    }
    
    @Deprecated
    public static String getContentMimeType(final HttpEntity entity) throws ParseException {
        Args.notNull(entity, "Entity");
        String mimeType = null;
        if (entity.getContentType() != null) {
            final HeaderElement[] values = entity.getContentType().getElements();
            if (values.length > 0) {
                mimeType = values[0].getName();
            }
        }
        return mimeType;
    }
    
    public static String toString(final HttpEntity entity, final Charset defaultCharset) throws IOException, ParseException {
        Args.notNull(entity, "Entity");
        final InputStream instream = entity.getContent();
        if (instream == null) {
            return null;
        }
        try {
            Args.check(entity.getContentLength() <= 2147483647L, "HTTP entity too large to be buffered in memory");
            int i = (int)entity.getContentLength();
            if (i < 0) {
                i = 4096;
            }
            Charset charset = null;
            try {
                final ContentType contentType = ContentType.get(entity);
                if (contentType != null) {
                    charset = contentType.getCharset();
                }
            }
            catch (UnsupportedCharsetException ex) {
                throw new UnsupportedEncodingException(ex.getMessage());
            }
            if (charset == null) {
                charset = defaultCharset;
            }
            if (charset == null) {
                charset = HTTP.DEF_CONTENT_CHARSET;
            }
            final Reader reader = new InputStreamReader(instream, charset);
            final CharArrayBuffer buffer = new CharArrayBuffer(i);
            final char[] tmp = new char[1024];
            int l;
            while ((l = reader.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
            return buffer.toString();
        }
        finally {
            instream.close();
        }
    }
    
    public static String toString(final HttpEntity entity, final String defaultCharset) throws IOException, ParseException {
        return toString(entity, (defaultCharset != null) ? Charset.forName(defaultCharset) : null);
    }
    
    public static String toString(final HttpEntity entity) throws IOException, ParseException {
        return toString(entity, (Charset)null);
    }
}
