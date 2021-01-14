package io.netty.util;

import io.netty.util.internal.InternalThreadLocalMap;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.Map;

public final class CharsetUtil {
    public static final Charset UTF_16 = Charset.forName("UTF-16");
    public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
    public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset US_ASCII = Charset.forName("US-ASCII");

    public static CharsetEncoder getEncoder(Charset paramCharset) {
        if (paramCharset == null) {
            throw new NullPointerException("charset");
        }
        Map localMap = InternalThreadLocalMap.get().charsetEncoderCache();
        CharsetEncoder localCharsetEncoder = (CharsetEncoder) localMap.get(paramCharset);
        if (localCharsetEncoder != null) {
            localCharsetEncoder.reset();
            localCharsetEncoder.onMalformedInput(CodingErrorAction.REPLACE);
            localCharsetEncoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
            return localCharsetEncoder;
        }
        localCharsetEncoder = paramCharset.newEncoder();
        localCharsetEncoder.onMalformedInput(CodingErrorAction.REPLACE);
        localCharsetEncoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
        localMap.put(paramCharset, localCharsetEncoder);
        return localCharsetEncoder;
    }

    public static CharsetDecoder getDecoder(Charset paramCharset) {
        if (paramCharset == null) {
            throw new NullPointerException("charset");
        }
        Map localMap = InternalThreadLocalMap.get().charsetDecoderCache();
        CharsetDecoder localCharsetDecoder = (CharsetDecoder) localMap.get(paramCharset);
        if (localCharsetDecoder != null) {
            localCharsetDecoder.reset();
            localCharsetDecoder.onMalformedInput(CodingErrorAction.REPLACE);
            localCharsetDecoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
            return localCharsetDecoder;
        }
        localCharsetDecoder = paramCharset.newDecoder();
        localCharsetDecoder.onMalformedInput(CodingErrorAction.REPLACE);
        localCharsetDecoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
        localMap.put(paramCharset, localCharsetDecoder);
        return localCharsetDecoder;
    }
}




