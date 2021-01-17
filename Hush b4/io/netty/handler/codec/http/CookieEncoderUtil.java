// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.util.internal.InternalThreadLocalMap;

final class CookieEncoderUtil
{
    static StringBuilder stringBuilder() {
        return InternalThreadLocalMap.get().stringBuilder();
    }
    
    static String stripTrailingSeparator(final StringBuilder buf) {
        if (buf.length() > 0) {
            buf.setLength(buf.length() - 2);
        }
        return buf.toString();
    }
    
    static void add(final StringBuilder sb, final String name, final String val) {
        if (val == null) {
            addQuoted(sb, name, "");
            return;
        }
        int i = 0;
        while (i < val.length()) {
            final char c = val.charAt(i);
            switch (c) {
                case '\t':
                case ' ':
                case '\"':
                case '(':
                case ')':
                case ',':
                case '/':
                case ':':
                case ';':
                case '<':
                case '=':
                case '>':
                case '?':
                case '@':
                case '[':
                case '\\':
                case ']':
                case '{':
                case '}': {
                    addQuoted(sb, name, val);
                    return;
                }
                default: {
                    ++i;
                    continue;
                }
            }
        }
        addUnquoted(sb, name, val);
    }
    
    static void addUnquoted(final StringBuilder sb, final String name, final String val) {
        sb.append(name);
        sb.append('=');
        sb.append(val);
        sb.append(';');
        sb.append(' ');
    }
    
    static void addQuoted(final StringBuilder sb, final String name, String val) {
        if (val == null) {
            val = "";
        }
        sb.append(name);
        sb.append('=');
        sb.append('\"');
        sb.append(val.replace("\\", "\\\\").replace("\"", "\\\""));
        sb.append('\"');
        sb.append(';');
        sb.append(' ');
    }
    
    static void add(final StringBuilder sb, final String name, final long val) {
        sb.append(name);
        sb.append('=');
        sb.append(val);
        sb.append(';');
        sb.append(' ');
    }
    
    private CookieEncoderUtil() {
    }
}
