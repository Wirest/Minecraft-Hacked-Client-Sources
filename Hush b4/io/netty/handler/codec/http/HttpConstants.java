// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.util.CharsetUtil;
import java.nio.charset.Charset;

public final class HttpConstants
{
    public static final byte SP = 32;
    public static final byte HT = 9;
    public static final byte CR = 13;
    public static final byte EQUALS = 61;
    public static final byte LF = 10;
    public static final byte COLON = 58;
    public static final byte SEMICOLON = 59;
    public static final byte COMMA = 44;
    public static final byte DOUBLE_QUOTE = 34;
    public static final Charset DEFAULT_CHARSET;
    
    private HttpConstants() {
    }
    
    static {
        DEFAULT_CHARSET = CharsetUtil.UTF_8;
    }
}
