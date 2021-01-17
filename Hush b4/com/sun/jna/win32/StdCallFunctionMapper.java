// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.win32;

import com.sun.jna.NativeMapped;
import java.lang.reflect.Method;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.NativeMappedConverter;
import com.sun.jna.FunctionMapper;

public class StdCallFunctionMapper implements FunctionMapper
{
    protected int getArgumentNativeStackSize(Class cls) {
        if (NativeMapped.class.isAssignableFrom(cls)) {
            cls = NativeMappedConverter.getInstance(cls).nativeType();
        }
        if (cls.isArray()) {
            return Pointer.SIZE;
        }
        try {
            return Native.getNativeSize(cls);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown native stack allocation size for " + cls);
        }
    }
    
    public String getFunctionName(final NativeLibrary library, final Method method) {
        String name = method.getName();
        int pop = 0;
        final Class[] argTypes = method.getParameterTypes();
        for (int i = 0; i < argTypes.length; ++i) {
            pop += this.getArgumentNativeStackSize(argTypes[i]);
        }
        final String decorated = name + "@" + pop;
        final int conv = 1;
        try {
            name = library.getFunction(decorated, conv).getName();
        }
        catch (UnsatisfiedLinkError e) {
            try {
                name = library.getFunction("_" + decorated, conv).getName();
            }
            catch (UnsatisfiedLinkError unsatisfiedLinkError) {}
        }
        return name;
    }
}
