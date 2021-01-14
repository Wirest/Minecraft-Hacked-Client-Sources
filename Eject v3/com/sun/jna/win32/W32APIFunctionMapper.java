package com.sun.jna.win32;

import com.sun.jna.FunctionMapper;
import com.sun.jna.NativeLibrary;

import java.lang.reflect.Method;

public class W32APIFunctionMapper
        implements FunctionMapper {
    public static final FunctionMapper UNICODE = new W32APIFunctionMapper(true);
    public static final FunctionMapper ASCII = new W32APIFunctionMapper(false);
    private final String suffix = paramBoolean ? "W" : "A";

    protected W32APIFunctionMapper(boolean paramBoolean) {
    }

    public String getFunctionName(NativeLibrary paramNativeLibrary, Method paramMethod) {
        String str = paramMethod.getName();
        if ((!str.endsWith("W")) && (!str.endsWith("A"))) {
            try {
                str = paramNativeLibrary.getFunction(str + this.suffix, 1).getName();
            } catch (UnsatisfiedLinkError localUnsatisfiedLinkError) {
            }
        }
        return str;
    }
}




