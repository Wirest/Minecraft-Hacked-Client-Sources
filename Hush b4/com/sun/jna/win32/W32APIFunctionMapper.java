// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.win32;

import java.lang.reflect.Method;
import com.sun.jna.NativeLibrary;
import com.sun.jna.FunctionMapper;

public class W32APIFunctionMapper implements FunctionMapper
{
    public static final FunctionMapper UNICODE;
    public static final FunctionMapper ASCII;
    private final String suffix;
    
    protected W32APIFunctionMapper(final boolean unicode) {
        this.suffix = (unicode ? "W" : "A");
    }
    
    public String getFunctionName(final NativeLibrary library, final Method method) {
        String name = method.getName();
        if (!name.endsWith("W") && !name.endsWith("A")) {
            try {
                name = library.getFunction(name + this.suffix, 1).getName();
            }
            catch (UnsatisfiedLinkError unsatisfiedLinkError) {}
        }
        return name;
    }
    
    static {
        UNICODE = new W32APIFunctionMapper(true);
        ASCII = new W32APIFunctionMapper(false);
    }
}
