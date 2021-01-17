// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

public interface NtDll extends StdCallLibrary
{
    public static final NtDll INSTANCE = (NtDll)Native.loadLibrary("NtDll", NtDll.class, W32APIOptions.UNICODE_OPTIONS);
    
    int ZwQueryKey(final WinNT.HANDLE p0, final int p1, final Structure p2, final int p3, final IntByReference p4);
}
