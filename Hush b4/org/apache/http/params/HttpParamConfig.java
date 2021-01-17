// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.params;

import java.nio.charset.CodingErrorAction;
import java.nio.charset.Charset;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.SocketConfig;

@Deprecated
public final class HttpParamConfig
{
    private HttpParamConfig() {
    }
    
    public static SocketConfig getSocketConfig(final HttpParams params) {
        return SocketConfig.custom().setSoTimeout(params.getIntParameter("http.socket.timeout", 0)).setSoReuseAddress(params.getBooleanParameter("http.socket.reuseaddr", false)).setSoKeepAlive(params.getBooleanParameter("http.socket.keepalive", false)).setSoLinger(params.getIntParameter("http.socket.linger", -1)).setTcpNoDelay(params.getBooleanParameter("http.tcp.nodelay", true)).build();
    }
    
    public static MessageConstraints getMessageConstraints(final HttpParams params) {
        return MessageConstraints.custom().setMaxHeaderCount(params.getIntParameter("http.connection.max-header-count", -1)).setMaxLineLength(params.getIntParameter("http.connection.max-line-length", -1)).build();
    }
    
    public static ConnectionConfig getConnectionConfig(final HttpParams params) {
        final MessageConstraints messageConstraints = getMessageConstraints(params);
        final String csname = (String)params.getParameter("http.protocol.element-charset");
        return ConnectionConfig.custom().setCharset((csname != null) ? Charset.forName(csname) : null).setMalformedInputAction((CodingErrorAction)params.getParameter("http.malformed.input.action")).setMalformedInputAction((CodingErrorAction)params.getParameter("http.unmappable.input.action")).setMessageConstraints(messageConstraints).build();
    }
}
