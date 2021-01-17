// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.helpers;

public final class Transform
{
    private static final String CDATA_START = "<![CDATA[";
    private static final String CDATA_END = "]]>";
    private static final String CDATA_PSEUDO_END = "]]&gt;";
    private static final String CDATA_EMBEDED_END = "]]>]]&gt;<![CDATA[";
    private static final int CDATA_END_LEN;
    
    private Transform() {
    }
    
    public static String escapeHtmlTags(final String input) {
        if (Strings.isEmpty(input) || (input.indexOf(34) == -1 && input.indexOf(38) == -1 && input.indexOf(60) == -1 && input.indexOf(62) == -1)) {
            return input;
        }
        final StringBuilder buf = new StringBuilder(input.length() + 6);
        char ch = ' ';
        for (int len = input.length(), i = 0; i < len; ++i) {
            ch = input.charAt(i);
            if (ch > '>') {
                buf.append(ch);
            }
            else if (ch == '<') {
                buf.append("&lt;");
            }
            else if (ch == '>') {
                buf.append("&gt;");
            }
            else if (ch == '&') {
                buf.append("&amp;");
            }
            else if (ch == '\"') {
                buf.append("&quot;");
            }
            else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }
    
    public static void appendEscapingCDATA(final StringBuilder buf, final String str) {
        if (str != null) {
            int end = str.indexOf("]]>");
            if (end < 0) {
                buf.append(str);
            }
            else {
                int start;
                for (start = 0; end > -1; end = str.indexOf("]]>", start)) {
                    buf.append(str.substring(start, end));
                    buf.append("]]>]]&gt;<![CDATA[");
                    start = end + Transform.CDATA_END_LEN;
                    if (start >= str.length()) {
                        return;
                    }
                }
                buf.append(str.substring(start));
            }
        }
    }
    
    public static String escapeJsonControlCharacters(final String input) {
        if (Strings.isEmpty(input) || (input.indexOf(34) == -1 && input.indexOf(92) == -1 && input.indexOf(47) == -1 && input.indexOf(8) == -1 && input.indexOf(12) == -1 && input.indexOf(10) == -1 && input.indexOf(13) == -1 && input.indexOf(9) == -1)) {
            return input;
        }
        final StringBuilder buf = new StringBuilder(input.length() + 6);
        for (int len = input.length(), i = 0; i < len; ++i) {
            final char ch = input.charAt(i);
            final String escBs = "\\\\";
            switch (ch) {
                case '\"': {
                    buf.append("\\\\");
                    buf.append(ch);
                    break;
                }
                case '\\': {
                    buf.append("\\\\");
                    buf.append(ch);
                    break;
                }
                case '/': {
                    buf.append("\\\\");
                    buf.append(ch);
                    break;
                }
                case '\b': {
                    buf.append("\\\\");
                    buf.append('b');
                    break;
                }
                case '\f': {
                    buf.append("\\\\");
                    buf.append('f');
                    break;
                }
                case '\n': {
                    buf.append("\\\\");
                    buf.append('n');
                    break;
                }
                case '\r': {
                    buf.append("\\\\");
                    buf.append('r');
                    break;
                }
                case '\t': {
                    buf.append("\\\\");
                    buf.append('t');
                    break;
                }
                default: {
                    buf.append(ch);
                    break;
                }
            }
        }
        return buf.toString();
    }
    
    static {
        CDATA_END_LEN = "]]>".length();
    }
}
