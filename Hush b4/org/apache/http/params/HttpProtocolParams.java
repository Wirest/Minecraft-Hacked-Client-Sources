// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.params;

import java.nio.charset.CodingErrorAction;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;

@Deprecated
public final class HttpProtocolParams implements CoreProtocolPNames
{
    private HttpProtocolParams() {
    }
    
    public static String getHttpElementCharset(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        String charset = (String)params.getParameter("http.protocol.element-charset");
        if (charset == null) {
            charset = HTTP.DEF_PROTOCOL_CHARSET.name();
        }
        return charset;
    }
    
    public static void setHttpElementCharset(final HttpParams params, final String charset) {
        Args.notNull(params, "HTTP parameters");
        params.setParameter("http.protocol.element-charset", charset);
    }
    
    public static String getContentCharset(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        String charset = (String)params.getParameter("http.protocol.content-charset");
        if (charset == null) {
            charset = HTTP.DEF_CONTENT_CHARSET.name();
        }
        return charset;
    }
    
    public static void setContentCharset(final HttpParams params, final String charset) {
        Args.notNull(params, "HTTP parameters");
        params.setParameter("http.protocol.content-charset", charset);
    }
    
    public static ProtocolVersion getVersion(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        final Object param = params.getParameter("http.protocol.version");
        if (param == null) {
            return HttpVersion.HTTP_1_1;
        }
        return (ProtocolVersion)param;
    }
    
    public static void setVersion(final HttpParams params, final ProtocolVersion version) {
        Args.notNull(params, "HTTP parameters");
        params.setParameter("http.protocol.version", version);
    }
    
    public static String getUserAgent(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        return (String)params.getParameter("http.useragent");
    }
    
    public static void setUserAgent(final HttpParams params, final String useragent) {
        Args.notNull(params, "HTTP parameters");
        params.setParameter("http.useragent", useragent);
    }
    
    public static boolean useExpectContinue(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        return params.getBooleanParameter("http.protocol.expect-continue", false);
    }
    
    public static void setUseExpectContinue(final HttpParams params, final boolean b) {
        Args.notNull(params, "HTTP parameters");
        params.setBooleanParameter("http.protocol.expect-continue", b);
    }
    
    public static CodingErrorAction getMalformedInputAction(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        final Object param = params.getParameter("http.malformed.input.action");
        if (param == null) {
            return CodingErrorAction.REPORT;
        }
        return (CodingErrorAction)param;
    }
    
    public static void setMalformedInputAction(final HttpParams params, final CodingErrorAction action) {
        Args.notNull(params, "HTTP parameters");
        params.setParameter("http.malformed.input.action", action);
    }
    
    public static CodingErrorAction getUnmappableInputAction(final HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        final Object param = params.getParameter("http.unmappable.input.action");
        if (param == null) {
            return CodingErrorAction.REPORT;
        }
        return (CodingErrorAction)param;
    }
    
    public static void setUnmappableInputAction(final HttpParams params, final CodingErrorAction action) {
        Args.notNull(params, "HTTP parameters");
        params.setParameter("http.unmappable.input.action", action);
    }
}
