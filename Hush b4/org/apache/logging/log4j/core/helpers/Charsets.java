// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.helpers;

import org.apache.logging.log4j.status.StatusLogger;
import java.nio.charset.Charset;

public final class Charsets
{
    public static final Charset UTF_8;
    
    public static Charset getSupportedCharset(final String charsetName) {
        return getSupportedCharset(charsetName, Charset.defaultCharset());
    }
    
    public static Charset getSupportedCharset(final String charsetName, final Charset defaultCharset) {
        Charset charset = null;
        if (charsetName != null && Charset.isSupported(charsetName)) {
            charset = Charset.forName(charsetName);
        }
        if (charset == null) {
            charset = defaultCharset;
            if (charsetName != null) {
                StatusLogger.getLogger().error("Charset " + charsetName + " is not supported for layout, using " + charset.displayName());
            }
        }
        return charset;
    }
    
    private Charsets() {
    }
    
    static {
        UTF_8 = Charset.forName("UTF-8");
    }
}
