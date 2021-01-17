// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public interface Ole32 extends StdCallLibrary
{
    public static final Ole32 INSTANCE = (Ole32)Native.loadLibrary("Ole32", Ole32.class, W32APIOptions.UNICODE_OPTIONS);
    
    WinNT.HRESULT CoCreateGuid(final Guid.GUID.ByReference p0);
    
    int StringFromGUID2(final Guid.GUID.ByReference p0, final char[] p1, final int p2);
    
    WinNT.HRESULT IIDFromString(final String p0, final Guid.GUID.ByReference p1);
    
    WinNT.HRESULT CoInitializeEx(final Pointer p0, final int p1);
    
    void CoUninitialize();
    
    WinNT.HRESULT CoCreateInstance(final Guid.GUID p0, final Pointer p1, final int p2, final Guid.GUID p3, final PointerByReference p4);
}
