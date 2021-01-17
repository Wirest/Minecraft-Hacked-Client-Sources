// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.message;

import org.apache.http.NameValuePair;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.HeaderElement;
import org.apache.http.annotation.Immutable;

@Immutable
public class BasicHeaderValueFormatter implements HeaderValueFormatter
{
    @Deprecated
    public static final BasicHeaderValueFormatter DEFAULT;
    public static final BasicHeaderValueFormatter INSTANCE;
    public static final String SEPARATORS = " ;,:@()<>\\\"/[]?={}\t";
    public static final String UNSAFE_CHARS = "\"\\";
    
    public static String formatElements(final HeaderElement[] elems, final boolean quote, final HeaderValueFormatter formatter) {
        return ((formatter != null) ? formatter : BasicHeaderValueFormatter.INSTANCE).formatElements(null, elems, quote).toString();
    }
    
    public CharArrayBuffer formatElements(final CharArrayBuffer charBuffer, final HeaderElement[] elems, final boolean quote) {
        Args.notNull(elems, "Header element array");
        final int len = this.estimateElementsLen(elems);
        CharArrayBuffer buffer = charBuffer;
        if (buffer == null) {
            buffer = new CharArrayBuffer(len);
        }
        else {
            buffer.ensureCapacity(len);
        }
        for (int i = 0; i < elems.length; ++i) {
            if (i > 0) {
                buffer.append(", ");
            }
            this.formatHeaderElement(buffer, elems[i], quote);
        }
        return buffer;
    }
    
    protected int estimateElementsLen(final HeaderElement[] elems) {
        if (elems == null || elems.length < 1) {
            return 0;
        }
        int result = (elems.length - 1) * 2;
        for (final HeaderElement elem : elems) {
            result += this.estimateHeaderElementLen(elem);
        }
        return result;
    }
    
    public static String formatHeaderElement(final HeaderElement elem, final boolean quote, final HeaderValueFormatter formatter) {
        return ((formatter != null) ? formatter : BasicHeaderValueFormatter.INSTANCE).formatHeaderElement(null, elem, quote).toString();
    }
    
    public CharArrayBuffer formatHeaderElement(final CharArrayBuffer charBuffer, final HeaderElement elem, final boolean quote) {
        Args.notNull(elem, "Header element");
        final int len = this.estimateHeaderElementLen(elem);
        CharArrayBuffer buffer = charBuffer;
        if (buffer == null) {
            buffer = new CharArrayBuffer(len);
        }
        else {
            buffer.ensureCapacity(len);
        }
        buffer.append(elem.getName());
        final String value = elem.getValue();
        if (value != null) {
            buffer.append('=');
            this.doFormatValue(buffer, value, quote);
        }
        final int parcnt = elem.getParameterCount();
        if (parcnt > 0) {
            for (int i = 0; i < parcnt; ++i) {
                buffer.append("; ");
                this.formatNameValuePair(buffer, elem.getParameter(i), quote);
            }
        }
        return buffer;
    }
    
    protected int estimateHeaderElementLen(final HeaderElement elem) {
        if (elem == null) {
            return 0;
        }
        int result = elem.getName().length();
        final String value = elem.getValue();
        if (value != null) {
            result += 3 + value.length();
        }
        final int parcnt = elem.getParameterCount();
        if (parcnt > 0) {
            for (int i = 0; i < parcnt; ++i) {
                result += 2 + this.estimateNameValuePairLen(elem.getParameter(i));
            }
        }
        return result;
    }
    
    public static String formatParameters(final NameValuePair[] nvps, final boolean quote, final HeaderValueFormatter formatter) {
        return ((formatter != null) ? formatter : BasicHeaderValueFormatter.INSTANCE).formatParameters(null, nvps, quote).toString();
    }
    
    public CharArrayBuffer formatParameters(final CharArrayBuffer charBuffer, final NameValuePair[] nvps, final boolean quote) {
        Args.notNull(nvps, "Header parameter array");
        final int len = this.estimateParametersLen(nvps);
        CharArrayBuffer buffer = charBuffer;
        if (buffer == null) {
            buffer = new CharArrayBuffer(len);
        }
        else {
            buffer.ensureCapacity(len);
        }
        for (int i = 0; i < nvps.length; ++i) {
            if (i > 0) {
                buffer.append("; ");
            }
            this.formatNameValuePair(buffer, nvps[i], quote);
        }
        return buffer;
    }
    
    protected int estimateParametersLen(final NameValuePair[] nvps) {
        if (nvps == null || nvps.length < 1) {
            return 0;
        }
        int result = (nvps.length - 1) * 2;
        for (final NameValuePair nvp : nvps) {
            result += this.estimateNameValuePairLen(nvp);
        }
        return result;
    }
    
    public static String formatNameValuePair(final NameValuePair nvp, final boolean quote, final HeaderValueFormatter formatter) {
        return ((formatter != null) ? formatter : BasicHeaderValueFormatter.INSTANCE).formatNameValuePair(null, nvp, quote).toString();
    }
    
    public CharArrayBuffer formatNameValuePair(final CharArrayBuffer charBuffer, final NameValuePair nvp, final boolean quote) {
        Args.notNull(nvp, "Name / value pair");
        final int len = this.estimateNameValuePairLen(nvp);
        CharArrayBuffer buffer = charBuffer;
        if (buffer == null) {
            buffer = new CharArrayBuffer(len);
        }
        else {
            buffer.ensureCapacity(len);
        }
        buffer.append(nvp.getName());
        final String value = nvp.getValue();
        if (value != null) {
            buffer.append('=');
            this.doFormatValue(buffer, value, quote);
        }
        return buffer;
    }
    
    protected int estimateNameValuePairLen(final NameValuePair nvp) {
        if (nvp == null) {
            return 0;
        }
        int result = nvp.getName().length();
        final String value = nvp.getValue();
        if (value != null) {
            result += 3 + value.length();
        }
        return result;
    }
    
    protected void doFormatValue(final CharArrayBuffer buffer, final String value, final boolean quote) {
        boolean quoteFlag = quote;
        if (!quoteFlag) {
            for (int i = 0; i < value.length() && !quoteFlag; quoteFlag = this.isSeparator(value.charAt(i)), ++i) {}
        }
        if (quoteFlag) {
            buffer.append('\"');
        }
        for (int i = 0; i < value.length(); ++i) {
            final char ch = value.charAt(i);
            if (this.isUnsafe(ch)) {
                buffer.append('\\');
            }
            buffer.append(ch);
        }
        if (quoteFlag) {
            buffer.append('\"');
        }
    }
    
    protected boolean isSeparator(final char ch) {
        return " ;,:@()<>\\\"/[]?={}\t".indexOf(ch) >= 0;
    }
    
    protected boolean isUnsafe(final char ch) {
        return "\"\\".indexOf(ch) >= 0;
    }
    
    static {
        DEFAULT = new BasicHeaderValueFormatter();
        INSTANCE = new BasicHeaderValueFormatter();
    }
}
