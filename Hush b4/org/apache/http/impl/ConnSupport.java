// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl;

import java.nio.charset.CharsetEncoder;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.CharsetDecoder;
import org.apache.http.config.ConnectionConfig;

public final class ConnSupport
{
    public static CharsetDecoder createDecoder(final ConnectionConfig cconfig) {
        if (cconfig == null) {
            return null;
        }
        final Charset charset = cconfig.getCharset();
        final CodingErrorAction malformed = cconfig.getMalformedInputAction();
        final CodingErrorAction unmappable = cconfig.getUnmappableInputAction();
        if (charset != null) {
            return charset.newDecoder().onMalformedInput((malformed != null) ? malformed : CodingErrorAction.REPORT).onUnmappableCharacter((unmappable != null) ? unmappable : CodingErrorAction.REPORT);
        }
        return null;
    }
    
    public static CharsetEncoder createEncoder(final ConnectionConfig cconfig) {
        if (cconfig == null) {
            return null;
        }
        final Charset charset = cconfig.getCharset();
        if (charset != null) {
            final CodingErrorAction malformed = cconfig.getMalformedInputAction();
            final CodingErrorAction unmappable = cconfig.getUnmappableInputAction();
            return charset.newEncoder().onMalformedInput((malformed != null) ? malformed : CodingErrorAction.REPORT).onUnmappableCharacter((unmappable != null) ? unmappable : CodingErrorAction.REPORT);
        }
        return null;
    }
}
