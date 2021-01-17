// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.utils;

import java.nio.CharBuffer;
import java.nio.ByteBuffer;
import java.util.Iterator;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.HeaderElement;
import org.apache.http.Header;
import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.http.Consts;
import org.apache.http.entity.ContentType;
import org.apache.http.HttpEntity;
import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import java.util.List;
import java.net.URI;
import java.util.BitSet;
import org.apache.http.annotation.Immutable;

@Immutable
public class URLEncodedUtils
{
    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final char QP_SEP_A = '&';
    private static final char QP_SEP_S = ';';
    private static final String NAME_VALUE_SEPARATOR = "=";
    private static final char[] QP_SEPS;
    private static final String QP_SEP_PATTERN;
    private static final BitSet UNRESERVED;
    private static final BitSet PUNCT;
    private static final BitSet USERINFO;
    private static final BitSet PATHSAFE;
    private static final BitSet URIC;
    private static final BitSet RESERVED;
    private static final BitSet URLENCODER;
    private static final int RADIX = 16;
    
    public static List<NameValuePair> parse(final URI uri, final String charset) {
        final String query = uri.getRawQuery();
        if (query != null && query.length() > 0) {
            final List<NameValuePair> result = new ArrayList<NameValuePair>();
            final Scanner scanner = new Scanner(query);
            parse(result, scanner, URLEncodedUtils.QP_SEP_PATTERN, charset);
            return result;
        }
        return Collections.emptyList();
    }
    
    public static List<NameValuePair> parse(final HttpEntity entity) throws IOException {
        final ContentType contentType = ContentType.get(entity);
        if (contentType != null && contentType.getMimeType().equalsIgnoreCase("application/x-www-form-urlencoded")) {
            final String content = EntityUtils.toString(entity, Consts.ASCII);
            if (content != null && content.length() > 0) {
                Charset charset = contentType.getCharset();
                if (charset == null) {
                    charset = HTTP.DEF_CONTENT_CHARSET;
                }
                return parse(content, charset, URLEncodedUtils.QP_SEPS);
            }
        }
        return Collections.emptyList();
    }
    
    public static boolean isEncoded(final HttpEntity entity) {
        final Header h = entity.getContentType();
        if (h != null) {
            final HeaderElement[] elems = h.getElements();
            if (elems.length > 0) {
                final String contentType = elems[0].getName();
                return contentType.equalsIgnoreCase("application/x-www-form-urlencoded");
            }
        }
        return false;
    }
    
    public static void parse(final List<NameValuePair> parameters, final Scanner scanner, final String charset) {
        parse(parameters, scanner, URLEncodedUtils.QP_SEP_PATTERN, charset);
    }
    
    public static void parse(final List<NameValuePair> parameters, final Scanner scanner, final String parameterSepartorPattern, final String charset) {
        scanner.useDelimiter(parameterSepartorPattern);
        while (scanner.hasNext()) {
            String name = null;
            String value = null;
            final String token = scanner.next();
            final int i = token.indexOf("=");
            if (i != -1) {
                name = decodeFormFields(token.substring(0, i).trim(), charset);
                value = decodeFormFields(token.substring(i + 1).trim(), charset);
            }
            else {
                name = decodeFormFields(token.trim(), charset);
            }
            parameters.add(new BasicNameValuePair(name, value));
        }
    }
    
    public static List<NameValuePair> parse(final String s, final Charset charset) {
        return parse(s, charset, URLEncodedUtils.QP_SEPS);
    }
    
    public static List<NameValuePair> parse(final String s, final Charset charset, final char... parameterSeparator) {
        if (s == null) {
            return Collections.emptyList();
        }
        final BasicHeaderValueParser parser = BasicHeaderValueParser.INSTANCE;
        final CharArrayBuffer buffer = new CharArrayBuffer(s.length());
        buffer.append(s);
        final ParserCursor cursor = new ParserCursor(0, buffer.length());
        final List<NameValuePair> list = new ArrayList<NameValuePair>();
        while (!cursor.atEnd()) {
            final NameValuePair nvp = parser.parseNameValuePair(buffer, cursor, parameterSeparator);
            if (nvp.getName().length() > 0) {
                list.add(new BasicNameValuePair(decodeFormFields(nvp.getName(), charset), decodeFormFields(nvp.getValue(), charset)));
            }
        }
        return list;
    }
    
    public static String format(final List<? extends NameValuePair> parameters, final String charset) {
        return format(parameters, '&', charset);
    }
    
    public static String format(final List<? extends NameValuePair> parameters, final char parameterSeparator, final String charset) {
        final StringBuilder result = new StringBuilder();
        for (final NameValuePair parameter : parameters) {
            final String encodedName = encodeFormFields(parameter.getName(), charset);
            final String encodedValue = encodeFormFields(parameter.getValue(), charset);
            if (result.length() > 0) {
                result.append(parameterSeparator);
            }
            result.append(encodedName);
            if (encodedValue != null) {
                result.append("=");
                result.append(encodedValue);
            }
        }
        return result.toString();
    }
    
    public static String format(final Iterable<? extends NameValuePair> parameters, final Charset charset) {
        return format(parameters, '&', charset);
    }
    
    public static String format(final Iterable<? extends NameValuePair> parameters, final char parameterSeparator, final Charset charset) {
        final StringBuilder result = new StringBuilder();
        for (final NameValuePair parameter : parameters) {
            final String encodedName = encodeFormFields(parameter.getName(), charset);
            final String encodedValue = encodeFormFields(parameter.getValue(), charset);
            if (result.length() > 0) {
                result.append(parameterSeparator);
            }
            result.append(encodedName);
            if (encodedValue != null) {
                result.append("=");
                result.append(encodedValue);
            }
        }
        return result.toString();
    }
    
    private static String urlEncode(final String content, final Charset charset, final BitSet safechars, final boolean blankAsPlus) {
        if (content == null) {
            return null;
        }
        final StringBuilder buf = new StringBuilder();
        final ByteBuffer bb = charset.encode(content);
        while (bb.hasRemaining()) {
            final int b = bb.get() & 0xFF;
            if (safechars.get(b)) {
                buf.append((char)b);
            }
            else if (blankAsPlus && b == 32) {
                buf.append('+');
            }
            else {
                buf.append("%");
                final char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 0xF, 16));
                final char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
                buf.append(hex1);
                buf.append(hex2);
            }
        }
        return buf.toString();
    }
    
    private static String urlDecode(final String content, final Charset charset, final boolean plusAsBlank) {
        if (content == null) {
            return null;
        }
        final ByteBuffer bb = ByteBuffer.allocate(content.length());
        final CharBuffer cb = CharBuffer.wrap(content);
        while (cb.hasRemaining()) {
            final char c = cb.get();
            if (c == '%' && cb.remaining() >= 2) {
                final char uc = cb.get();
                final char lc = cb.get();
                final int u = Character.digit(uc, 16);
                final int l = Character.digit(lc, 16);
                if (u != -1 && l != -1) {
                    bb.put((byte)((u << 4) + l));
                }
                else {
                    bb.put((byte)37);
                    bb.put((byte)uc);
                    bb.put((byte)lc);
                }
            }
            else if (plusAsBlank && c == '+') {
                bb.put((byte)32);
            }
            else {
                bb.put((byte)c);
            }
        }
        bb.flip();
        return charset.decode(bb).toString();
    }
    
    private static String decodeFormFields(final String content, final String charset) {
        if (content == null) {
            return null;
        }
        return urlDecode(content, (charset != null) ? Charset.forName(charset) : Consts.UTF_8, true);
    }
    
    private static String decodeFormFields(final String content, final Charset charset) {
        if (content == null) {
            return null;
        }
        return urlDecode(content, (charset != null) ? charset : Consts.UTF_8, true);
    }
    
    private static String encodeFormFields(final String content, final String charset) {
        if (content == null) {
            return null;
        }
        return urlEncode(content, (charset != null) ? Charset.forName(charset) : Consts.UTF_8, URLEncodedUtils.URLENCODER, true);
    }
    
    private static String encodeFormFields(final String content, final Charset charset) {
        if (content == null) {
            return null;
        }
        return urlEncode(content, (charset != null) ? charset : Consts.UTF_8, URLEncodedUtils.URLENCODER, true);
    }
    
    static String encUserInfo(final String content, final Charset charset) {
        return urlEncode(content, charset, URLEncodedUtils.USERINFO, false);
    }
    
    static String encUric(final String content, final Charset charset) {
        return urlEncode(content, charset, URLEncodedUtils.URIC, false);
    }
    
    static String encPath(final String content, final Charset charset) {
        return urlEncode(content, charset, URLEncodedUtils.PATHSAFE, false);
    }
    
    static {
        QP_SEPS = new char[] { '&', ';' };
        QP_SEP_PATTERN = "[" + new String(URLEncodedUtils.QP_SEPS) + "]";
        UNRESERVED = new BitSet(256);
        PUNCT = new BitSet(256);
        USERINFO = new BitSet(256);
        PATHSAFE = new BitSet(256);
        URIC = new BitSet(256);
        RESERVED = new BitSet(256);
        URLENCODER = new BitSet(256);
        for (int i = 97; i <= 122; ++i) {
            URLEncodedUtils.UNRESERVED.set(i);
        }
        for (int i = 65; i <= 90; ++i) {
            URLEncodedUtils.UNRESERVED.set(i);
        }
        for (int i = 48; i <= 57; ++i) {
            URLEncodedUtils.UNRESERVED.set(i);
        }
        URLEncodedUtils.UNRESERVED.set(95);
        URLEncodedUtils.UNRESERVED.set(45);
        URLEncodedUtils.UNRESERVED.set(46);
        URLEncodedUtils.UNRESERVED.set(42);
        URLEncodedUtils.URLENCODER.or(URLEncodedUtils.UNRESERVED);
        URLEncodedUtils.UNRESERVED.set(33);
        URLEncodedUtils.UNRESERVED.set(126);
        URLEncodedUtils.UNRESERVED.set(39);
        URLEncodedUtils.UNRESERVED.set(40);
        URLEncodedUtils.UNRESERVED.set(41);
        URLEncodedUtils.PUNCT.set(44);
        URLEncodedUtils.PUNCT.set(59);
        URLEncodedUtils.PUNCT.set(58);
        URLEncodedUtils.PUNCT.set(36);
        URLEncodedUtils.PUNCT.set(38);
        URLEncodedUtils.PUNCT.set(43);
        URLEncodedUtils.PUNCT.set(61);
        URLEncodedUtils.USERINFO.or(URLEncodedUtils.UNRESERVED);
        URLEncodedUtils.USERINFO.or(URLEncodedUtils.PUNCT);
        URLEncodedUtils.PATHSAFE.or(URLEncodedUtils.UNRESERVED);
        URLEncodedUtils.PATHSAFE.set(47);
        URLEncodedUtils.PATHSAFE.set(59);
        URLEncodedUtils.PATHSAFE.set(58);
        URLEncodedUtils.PATHSAFE.set(64);
        URLEncodedUtils.PATHSAFE.set(38);
        URLEncodedUtils.PATHSAFE.set(61);
        URLEncodedUtils.PATHSAFE.set(43);
        URLEncodedUtils.PATHSAFE.set(36);
        URLEncodedUtils.PATHSAFE.set(44);
        URLEncodedUtils.RESERVED.set(59);
        URLEncodedUtils.RESERVED.set(47);
        URLEncodedUtils.RESERVED.set(63);
        URLEncodedUtils.RESERVED.set(58);
        URLEncodedUtils.RESERVED.set(64);
        URLEncodedUtils.RESERVED.set(38);
        URLEncodedUtils.RESERVED.set(61);
        URLEncodedUtils.RESERVED.set(43);
        URLEncodedUtils.RESERVED.set(36);
        URLEncodedUtils.RESERVED.set(44);
        URLEncodedUtils.RESERVED.set(91);
        URLEncodedUtils.RESERVED.set(93);
        URLEncodedUtils.URIC.or(URLEncodedUtils.RESERVED);
        URLEncodedUtils.URIC.or(URLEncodedUtils.UNRESERVED);
    }
}
