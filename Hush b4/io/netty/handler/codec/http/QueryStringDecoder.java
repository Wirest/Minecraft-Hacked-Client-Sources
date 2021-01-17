// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.nio.charset.Charset;

public class QueryStringDecoder
{
    private static final int DEFAULT_MAX_PARAMS = 1024;
    private final Charset charset;
    private final String uri;
    private final boolean hasPath;
    private final int maxParams;
    private String path;
    private Map<String, List<String>> params;
    private int nParams;
    
    public QueryStringDecoder(final String uri) {
        this(uri, HttpConstants.DEFAULT_CHARSET);
    }
    
    public QueryStringDecoder(final String uri, final boolean hasPath) {
        this(uri, HttpConstants.DEFAULT_CHARSET, hasPath);
    }
    
    public QueryStringDecoder(final String uri, final Charset charset) {
        this(uri, charset, true);
    }
    
    public QueryStringDecoder(final String uri, final Charset charset, final boolean hasPath) {
        this(uri, charset, hasPath, 1024);
    }
    
    public QueryStringDecoder(final String uri, final Charset charset, final boolean hasPath, final int maxParams) {
        if (uri == null) {
            throw new NullPointerException("getUri");
        }
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        if (maxParams <= 0) {
            throw new IllegalArgumentException("maxParams: " + maxParams + " (expected: a positive integer)");
        }
        this.uri = uri;
        this.charset = charset;
        this.maxParams = maxParams;
        this.hasPath = hasPath;
    }
    
    public QueryStringDecoder(final URI uri) {
        this(uri, HttpConstants.DEFAULT_CHARSET);
    }
    
    public QueryStringDecoder(final URI uri, final Charset charset) {
        this(uri, charset, 1024);
    }
    
    public QueryStringDecoder(final URI uri, final Charset charset, final int maxParams) {
        if (uri == null) {
            throw new NullPointerException("getUri");
        }
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        if (maxParams <= 0) {
            throw new IllegalArgumentException("maxParams: " + maxParams + " (expected: a positive integer)");
        }
        String rawPath = uri.getRawPath();
        if (rawPath != null) {
            this.hasPath = true;
        }
        else {
            rawPath = "";
            this.hasPath = false;
        }
        this.uri = rawPath + '?' + uri.getRawQuery();
        this.charset = charset;
        this.maxParams = maxParams;
    }
    
    public String path() {
        if (this.path == null) {
            if (!this.hasPath) {
                return this.path = "";
            }
            final int pathEndPos = this.uri.indexOf(63);
            if (pathEndPos >= 0) {
                return this.path = this.uri.substring(0, pathEndPos);
            }
            this.path = this.uri;
        }
        return this.path;
    }
    
    public Map<String, List<String>> parameters() {
        if (this.params == null) {
            if (this.hasPath) {
                final int pathLength = this.path().length();
                if (this.uri.length() == pathLength) {
                    return Collections.emptyMap();
                }
                this.decodeParams(this.uri.substring(pathLength + 1));
            }
            else {
                if (this.uri.isEmpty()) {
                    return Collections.emptyMap();
                }
                this.decodeParams(this.uri);
            }
        }
        return this.params;
    }
    
    private void decodeParams(final String s) {
        final LinkedHashMap<String, List<String>> params2 = new LinkedHashMap<String, List<String>>();
        this.params = params2;
        final Map<String, List<String>> params = params2;
        this.nParams = 0;
        String name = null;
        int pos = 0;
        int i;
        for (i = 0; i < s.length(); ++i) {
            final char c = s.charAt(i);
            if (c == '=' && name == null) {
                if (pos != i) {
                    name = decodeComponent(s.substring(pos, i), this.charset);
                }
                pos = i + 1;
            }
            else if (c == '&' || c == ';') {
                if (name == null && pos != i) {
                    if (!this.addParam(params, decodeComponent(s.substring(pos, i), this.charset), "")) {
                        return;
                    }
                }
                else if (name != null) {
                    if (!this.addParam(params, name, decodeComponent(s.substring(pos, i), this.charset))) {
                        return;
                    }
                    name = null;
                }
                pos = i + 1;
            }
        }
        if (pos != i) {
            if (name == null) {
                this.addParam(params, decodeComponent(s.substring(pos, i), this.charset), "");
            }
            else {
                this.addParam(params, name, decodeComponent(s.substring(pos, i), this.charset));
            }
        }
        else if (name != null) {
            this.addParam(params, name, "");
        }
    }
    
    private boolean addParam(final Map<String, List<String>> params, final String name, final String value) {
        if (this.nParams >= this.maxParams) {
            return false;
        }
        List<String> values = params.get(name);
        if (values == null) {
            values = new ArrayList<String>(1);
            params.put(name, values);
        }
        values.add(value);
        ++this.nParams;
        return true;
    }
    
    public static String decodeComponent(final String s) {
        return decodeComponent(s, HttpConstants.DEFAULT_CHARSET);
    }
    
    public static String decodeComponent(final String s, final Charset charset) {
        if (s == null) {
            return "";
        }
        final int size = s.length();
        boolean modified = false;
        for (int i = 0; i < size; ++i) {
            final char c = s.charAt(i);
            if (c == '%' || c == '+') {
                modified = true;
                break;
            }
        }
        if (!modified) {
            return s;
        }
        final byte[] buf = new byte[size];
        int pos = 0;
        for (int j = 0; j < size; ++j) {
            char c2 = s.charAt(j);
            switch (c2) {
                case '+': {
                    buf[pos++] = 32;
                    continue;
                }
                case '%': {
                    if (j == size - 1) {
                        throw new IllegalArgumentException("unterminated escape sequence at end of string: " + s);
                    }
                    c2 = s.charAt(++j);
                    if (c2 == '%') {
                        buf[pos++] = 37;
                        continue;
                    }
                    if (j == size - 1) {
                        throw new IllegalArgumentException("partial escape sequence at end of string: " + s);
                    }
                    c2 = decodeHexNibble(c2);
                    final char c3 = decodeHexNibble(s.charAt(++j));
                    if (c2 == '\uffff' || c3 == '\uffff') {
                        throw new IllegalArgumentException("invalid escape sequence `%" + s.charAt(j - 1) + s.charAt(j) + "' at index " + (j - 2) + " of: " + s);
                    }
                    c2 = (char)(c2 * '\u0010' + c3);
                    break;
                }
            }
            buf[pos++] = (byte)c2;
        }
        return new String(buf, 0, pos, charset);
    }
    
    private static char decodeHexNibble(final char c) {
        if ('0' <= c && c <= '9') {
            return (char)(c - '0');
        }
        if ('a' <= c && c <= 'f') {
            return (char)(c - 'a' + 10);
        }
        if ('A' <= c && c <= 'F') {
            return (char)(c - 'A' + 10);
        }
        return '\uffff';
    }
}
