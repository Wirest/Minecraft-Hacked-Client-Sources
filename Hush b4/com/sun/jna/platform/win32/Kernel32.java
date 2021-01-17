// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.ptr.IntByReference;
import java.nio.Buffer;
import com.sun.jna.Pointer;

public interface Kernel32 extends WinNT
{
    public static final Kernel32 INSTANCE = (Kernel32)Native.loadLibrary("kernel32", Kernel32.class, W32APIOptions.UNICODE_OPTIONS);
    
    int FormatMessage(final int p0, final Pointer p1, final int p2, final int p3, final Buffer p4, final int p5, final Pointer p6);
    
    boolean ReadFile(final HANDLE p0, final Buffer p1, final int p2, final IntByReference p3, final WinBase.OVERLAPPED p4);
}
