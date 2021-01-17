// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpVersion implements Comparable<HttpVersion>
{
    private static final Pattern VERSION_PATTERN;
    private static final String HTTP_1_0_STRING = "HTTP/1.0";
    private static final String HTTP_1_1_STRING = "HTTP/1.1";
    public static final HttpVersion HTTP_1_0;
    public static final HttpVersion HTTP_1_1;
    private final String protocolName;
    private final int majorVersion;
    private final int minorVersion;
    private final String text;
    private final boolean keepAliveDefault;
    private final byte[] bytes;
    
    public static HttpVersion valueOf(String text) {
        if (text == null) {
            throw new NullPointerException("text");
        }
        text = text.trim();
        if (text.isEmpty()) {
            throw new IllegalArgumentException("text is empty");
        }
        HttpVersion version = version0(text);
        if (version == null) {
            text = text.toUpperCase();
            version = version0(text);
            if (version == null) {
                version = new HttpVersion(text, true);
            }
        }
        return version;
    }
    
    private static HttpVersion version0(final String text) {
        if ("HTTP/1.1".equals(text)) {
            return HttpVersion.HTTP_1_1;
        }
        if ("HTTP/1.0".equals(text)) {
            return HttpVersion.HTTP_1_0;
        }
        return null;
    }
    
    public HttpVersion(String text, final boolean keepAliveDefault) {
        if (text == null) {
            throw new NullPointerException("text");
        }
        text = text.trim().toUpperCase();
        if (text.isEmpty()) {
            throw new IllegalArgumentException("empty text");
        }
        final Matcher m = HttpVersion.VERSION_PATTERN.matcher(text);
        if (!m.matches()) {
            throw new IllegalArgumentException("invalid version format: " + text);
        }
        this.protocolName = m.group(1);
        this.majorVersion = Integer.parseInt(m.group(2));
        this.minorVersion = Integer.parseInt(m.group(3));
        this.text = this.protocolName + '/' + this.majorVersion + '.' + this.minorVersion;
        this.keepAliveDefault = keepAliveDefault;
        this.bytes = null;
    }
    
    public HttpVersion(final String protocolName, final int majorVersion, final int minorVersion, final boolean keepAliveDefault) {
        this(protocolName, majorVersion, minorVersion, keepAliveDefault, false);
    }
    
    private HttpVersion(String protocolName, final int majorVersion, final int minorVersion, final boolean keepAliveDefault, final boolean bytes) {
        if (protocolName == null) {
            throw new NullPointerException("protocolName");
        }
        protocolName = protocolName.trim().toUpperCase();
        if (protocolName.isEmpty()) {
            throw new IllegalArgumentException("empty protocolName");
        }
        for (int i = 0; i < protocolName.length(); ++i) {
            if (Character.isISOControl(protocolName.charAt(i)) || Character.isWhitespace(protocolName.charAt(i))) {
                throw new IllegalArgumentException("invalid character in protocolName");
            }
        }
        if (majorVersion < 0) {
            throw new IllegalArgumentException("negative majorVersion");
        }
        if (minorVersion < 0) {
            throw new IllegalArgumentException("negative minorVersion");
        }
        this.protocolName = protocolName;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.text = protocolName + '/' + majorVersion + '.' + minorVersion;
        this.keepAliveDefault = keepAliveDefault;
        if (bytes) {
            this.bytes = this.text.getBytes(CharsetUtil.US_ASCII);
        }
        else {
            this.bytes = null;
        }
    }
    
    public String protocolName() {
        return this.protocolName;
    }
    
    public int majorVersion() {
        return this.majorVersion;
    }
    
    public int minorVersion() {
        return this.minorVersion;
    }
    
    public String text() {
        return this.text;
    }
    
    public boolean isKeepAliveDefault() {
        return this.keepAliveDefault;
    }
    
    @Override
    public String toString() {
        return this.text();
    }
    
    @Override
    public int hashCode() {
        return (this.protocolName().hashCode() * 31 + this.majorVersion()) * 31 + this.minorVersion();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof HttpVersion)) {
            return false;
        }
        final HttpVersion that = (HttpVersion)o;
        return this.minorVersion() == that.minorVersion() && this.majorVersion() == that.majorVersion() && this.protocolName().equals(that.protocolName());
    }
    
    @Override
    public int compareTo(final HttpVersion o) {
        int v = this.protocolName().compareTo(o.protocolName());
        if (v != 0) {
            return v;
        }
        v = this.majorVersion() - o.majorVersion();
        if (v != 0) {
            return v;
        }
        return this.minorVersion() - o.minorVersion();
    }
    
    void encode(final ByteBuf buf) {
        if (this.bytes == null) {
            HttpHeaders.encodeAscii0(this.text, buf);
        }
        else {
            buf.writeBytes(this.bytes);
        }
    }
    
    static {
        VERSION_PATTERN = Pattern.compile("(\\S+)/(\\d+)\\.(\\d+)");
        HTTP_1_0 = new HttpVersion("HTTP", 1, 0, false, true);
        HTTP_1_1 = new HttpVersion("HTTP", 1, 1, true, true);
    }
}
