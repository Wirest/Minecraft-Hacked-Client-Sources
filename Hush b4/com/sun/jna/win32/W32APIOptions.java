// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.win32;

import java.util.HashMap;
import java.util.Map;

public interface W32APIOptions extends StdCallLibrary
{
    public static final Map UNICODE_OPTIONS = new HashMap() {
        {
            this.put("type-mapper", W32APITypeMapper.UNICODE);
            this.put("function-mapper", W32APIFunctionMapper.UNICODE);
        }
    };
    public static final Map ASCII_OPTIONS = new HashMap() {
        {
            this.put("type-mapper", W32APITypeMapper.ASCII);
            this.put("function-mapper", W32APIFunctionMapper.ASCII);
        }
    };
    public static final Map DEFAULT_OPTIONS = Boolean.getBoolean("w32.ascii") ? W32APIOptions.ASCII_OPTIONS : W32APIOptions.UNICODE_OPTIONS;
}
