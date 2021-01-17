// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.UnicodeUnescaper;
import org.apache.commons.lang3.text.translate.OctalUnescaper;
import org.apache.commons.lang3.text.translate.UnicodeUnpairedSurrogateRemover;
import org.apache.commons.lang3.text.translate.NumericEntityEscaper;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

public class StringEscapeUtils
{
    public static final CharSequenceTranslator ESCAPE_JAVA;
    public static final CharSequenceTranslator ESCAPE_ECMASCRIPT;
    public static final CharSequenceTranslator ESCAPE_JSON;
    @Deprecated
    public static final CharSequenceTranslator ESCAPE_XML;
    public static final CharSequenceTranslator ESCAPE_XML10;
    public static final CharSequenceTranslator ESCAPE_XML11;
    public static final CharSequenceTranslator ESCAPE_HTML3;
    public static final CharSequenceTranslator ESCAPE_HTML4;
    public static final CharSequenceTranslator ESCAPE_CSV;
    public static final CharSequenceTranslator UNESCAPE_JAVA;
    public static final CharSequenceTranslator UNESCAPE_ECMASCRIPT;
    public static final CharSequenceTranslator UNESCAPE_JSON;
    public static final CharSequenceTranslator UNESCAPE_HTML3;
    public static final CharSequenceTranslator UNESCAPE_HTML4;
    public static final CharSequenceTranslator UNESCAPE_XML;
    public static final CharSequenceTranslator UNESCAPE_CSV;
    
    public static final String escapeJava(final String input) {
        return StringEscapeUtils.ESCAPE_JAVA.translate(input);
    }
    
    public static final String escapeEcmaScript(final String input) {
        return StringEscapeUtils.ESCAPE_ECMASCRIPT.translate(input);
    }
    
    public static final String escapeJson(final String input) {
        return StringEscapeUtils.ESCAPE_JSON.translate(input);
    }
    
    public static final String unescapeJava(final String input) {
        return StringEscapeUtils.UNESCAPE_JAVA.translate(input);
    }
    
    public static final String unescapeEcmaScript(final String input) {
        return StringEscapeUtils.UNESCAPE_ECMASCRIPT.translate(input);
    }
    
    public static final String unescapeJson(final String input) {
        return StringEscapeUtils.UNESCAPE_JSON.translate(input);
    }
    
    public static final String escapeHtml4(final String input) {
        return StringEscapeUtils.ESCAPE_HTML4.translate(input);
    }
    
    public static final String escapeHtml3(final String input) {
        return StringEscapeUtils.ESCAPE_HTML3.translate(input);
    }
    
    public static final String unescapeHtml4(final String input) {
        return StringEscapeUtils.UNESCAPE_HTML4.translate(input);
    }
    
    public static final String unescapeHtml3(final String input) {
        return StringEscapeUtils.UNESCAPE_HTML3.translate(input);
    }
    
    @Deprecated
    public static final String escapeXml(final String input) {
        return StringEscapeUtils.ESCAPE_XML.translate(input);
    }
    
    public static String escapeXml10(final String input) {
        return StringEscapeUtils.ESCAPE_XML10.translate(input);
    }
    
    public static String escapeXml11(final String input) {
        return StringEscapeUtils.ESCAPE_XML11.translate(input);
    }
    
    public static final String unescapeXml(final String input) {
        return StringEscapeUtils.UNESCAPE_XML.translate(input);
    }
    
    public static final String escapeCsv(final String input) {
        return StringEscapeUtils.ESCAPE_CSV.translate(input);
    }
    
    public static final String unescapeCsv(final String input) {
        return StringEscapeUtils.UNESCAPE_CSV.translate(input);
    }
    
    static {
        ESCAPE_JAVA = new LookupTranslator((CharSequence[][])new String[][] { { "\"", "\\\"" }, { "\\", "\\\\" } }).with(new LookupTranslator((CharSequence[][])EntityArrays.JAVA_CTRL_CHARS_ESCAPE())).with(JavaUnicodeEscaper.outsideOf(32, 127));
        ESCAPE_ECMASCRIPT = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator((CharSequence[][])new String[][] { { "'", "\\'" }, { "\"", "\\\"" }, { "\\", "\\\\" }, { "/", "\\/" } }), new LookupTranslator((CharSequence[][])EntityArrays.JAVA_CTRL_CHARS_ESCAPE()), JavaUnicodeEscaper.outsideOf(32, 127) });
        ESCAPE_JSON = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator((CharSequence[][])new String[][] { { "\"", "\\\"" }, { "\\", "\\\\" }, { "/", "\\/" } }), new LookupTranslator((CharSequence[][])EntityArrays.JAVA_CTRL_CHARS_ESCAPE()), JavaUnicodeEscaper.outsideOf(32, 127) });
        ESCAPE_XML = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator((CharSequence[][])EntityArrays.BASIC_ESCAPE()), new LookupTranslator((CharSequence[][])EntityArrays.APOS_ESCAPE()) });
        ESCAPE_XML10 = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator((CharSequence[][])EntityArrays.BASIC_ESCAPE()), new LookupTranslator((CharSequence[][])EntityArrays.APOS_ESCAPE()), new LookupTranslator((CharSequence[][])new String[][] { { "\u0000", "" }, { "\u0001", "" }, { "\u0002", "" }, { "\u0003", "" }, { "\u0004", "" }, { "\u0005", "" }, { "\u0006", "" }, { "\u0007", "" }, { "\b", "" }, { "\u000b", "" }, { "\f", "" }, { "\u000e", "" }, { "\u000f", "" }, { "\u0010", "" }, { "\u0011", "" }, { "\u0012", "" }, { "\u0013", "" }, { "\u0014", "" }, { "\u0015", "" }, { "\u0016", "" }, { "\u0017", "" }, { "\u0018", "" }, { "\u0019", "" }, { "\u001a", "" }, { "\u001b", "" }, { "\u001c", "" }, { "\u001d", "" }, { "\u001e", "" }, { "\u001f", "" }, { "\ufffe", "" }, { "\uffff", "" } }), NumericEntityEscaper.between(127, 132), NumericEntityEscaper.between(134, 159), new UnicodeUnpairedSurrogateRemover() });
        ESCAPE_XML11 = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator((CharSequence[][])EntityArrays.BASIC_ESCAPE()), new LookupTranslator((CharSequence[][])EntityArrays.APOS_ESCAPE()), new LookupTranslator((CharSequence[][])new String[][] { { "\u0000", "" }, { "\u000b", "&#11;" }, { "\f", "&#12;" }, { "\ufffe", "" }, { "\uffff", "" } }), NumericEntityEscaper.between(1, 8), NumericEntityEscaper.between(14, 31), NumericEntityEscaper.between(127, 132), NumericEntityEscaper.between(134, 159), new UnicodeUnpairedSurrogateRemover() });
        ESCAPE_HTML3 = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator((CharSequence[][])EntityArrays.BASIC_ESCAPE()), new LookupTranslator((CharSequence[][])EntityArrays.ISO8859_1_ESCAPE()) });
        ESCAPE_HTML4 = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator((CharSequence[][])EntityArrays.BASIC_ESCAPE()), new LookupTranslator((CharSequence[][])EntityArrays.ISO8859_1_ESCAPE()), new LookupTranslator((CharSequence[][])EntityArrays.HTML40_EXTENDED_ESCAPE()) });
        ESCAPE_CSV = new CsvEscaper();
        UNESCAPE_JAVA = new AggregateTranslator(new CharSequenceTranslator[] { new OctalUnescaper(), new UnicodeUnescaper(), new LookupTranslator((CharSequence[][])EntityArrays.JAVA_CTRL_CHARS_UNESCAPE()), new LookupTranslator((CharSequence[][])new String[][] { { "\\\\", "\\" }, { "\\\"", "\"" }, { "\\'", "'" }, { "\\", "" } }) });
        UNESCAPE_ECMASCRIPT = StringEscapeUtils.UNESCAPE_JAVA;
        UNESCAPE_JSON = StringEscapeUtils.UNESCAPE_JAVA;
        UNESCAPE_HTML3 = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator((CharSequence[][])EntityArrays.BASIC_UNESCAPE()), new LookupTranslator((CharSequence[][])EntityArrays.ISO8859_1_UNESCAPE()), new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0]) });
        UNESCAPE_HTML4 = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator((CharSequence[][])EntityArrays.BASIC_UNESCAPE()), new LookupTranslator((CharSequence[][])EntityArrays.ISO8859_1_UNESCAPE()), new LookupTranslator((CharSequence[][])EntityArrays.HTML40_EXTENDED_UNESCAPE()), new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0]) });
        UNESCAPE_XML = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator((CharSequence[][])EntityArrays.BASIC_UNESCAPE()), new LookupTranslator((CharSequence[][])EntityArrays.APOS_UNESCAPE()), new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0]) });
        UNESCAPE_CSV = new CsvUnescaper();
    }
    
    static class CsvEscaper extends CharSequenceTranslator
    {
        private static final char CSV_DELIMITER = ',';
        private static final char CSV_QUOTE = '\"';
        private static final String CSV_QUOTE_STR;
        private static final char[] CSV_SEARCH_CHARS;
        
        @Override
        public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
            if (index != 0) {
                throw new IllegalStateException("CsvEscaper should never reach the [1] index");
            }
            if (StringUtils.containsNone(input.toString(), CsvEscaper.CSV_SEARCH_CHARS)) {
                out.write(input.toString());
            }
            else {
                out.write(34);
                out.write(StringUtils.replace(input.toString(), CsvEscaper.CSV_QUOTE_STR, CsvEscaper.CSV_QUOTE_STR + CsvEscaper.CSV_QUOTE_STR));
                out.write(34);
            }
            return Character.codePointCount(input, 0, input.length());
        }
        
        static {
            CSV_QUOTE_STR = String.valueOf('\"');
            CSV_SEARCH_CHARS = new char[] { ',', '\"', '\r', '\n' };
        }
    }
    
    static class CsvUnescaper extends CharSequenceTranslator
    {
        private static final char CSV_DELIMITER = ',';
        private static final char CSV_QUOTE = '\"';
        private static final String CSV_QUOTE_STR;
        private static final char[] CSV_SEARCH_CHARS;
        
        @Override
        public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
            if (index != 0) {
                throw new IllegalStateException("CsvUnescaper should never reach the [1] index");
            }
            if (input.charAt(0) != '\"' || input.charAt(input.length() - 1) != '\"') {
                out.write(input.toString());
                return Character.codePointCount(input, 0, input.length());
            }
            final String quoteless = input.subSequence(1, input.length() - 1).toString();
            if (StringUtils.containsAny(quoteless, CsvUnescaper.CSV_SEARCH_CHARS)) {
                out.write(StringUtils.replace(quoteless, CsvUnescaper.CSV_QUOTE_STR + CsvUnescaper.CSV_QUOTE_STR, CsvUnescaper.CSV_QUOTE_STR));
            }
            else {
                out.write(input.toString());
            }
            return Character.codePointCount(input, 0, input.length());
        }
        
        static {
            CSV_QUOTE_STR = String.valueOf('\"');
            CSV_SEARCH_CHARS = new char[] { ',', '\"', '\r', '\n' };
        }
    }
}
