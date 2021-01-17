// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public interface Oleaut32 extends StdCallLibrary
{
    public static final Oleaut32 INSTANCE = (Oleaut32)Native.loadLibrary("Oleaut32", Oleaut32.class, W32APIOptions.UNICODE_OPTIONS);
    
    Pointer SysAllocString(final String p0);
    
    void SysFreeString(final Pointer p0);
}
