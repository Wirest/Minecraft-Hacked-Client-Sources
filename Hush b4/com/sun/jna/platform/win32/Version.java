// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface Version extends StdCallLibrary
{
    public static final Version INSTANCE = (Version)Native.loadLibrary("version", Version.class, W32APIOptions.DEFAULT_OPTIONS);
    
    int GetFileVersionInfoSize(final String p0, final IntByReference p1);
    
    boolean GetFileVersionInfo(final String p0, final int p1, final int p2, final Pointer p3);
    
    boolean VerQueryValue(final Pointer p0, final String p1, final PointerByReference p2, final IntByReference p3);
}
