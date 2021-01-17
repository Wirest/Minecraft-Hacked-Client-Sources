// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public interface Crypt32 extends StdCallLibrary
{
    public static final Crypt32 INSTANCE = (Crypt32)Native.loadLibrary("Crypt32", Crypt32.class, W32APIOptions.UNICODE_OPTIONS);
    
    boolean CryptProtectData(final WinCrypt.DATA_BLOB p0, final String p1, final WinCrypt.DATA_BLOB p2, final Pointer p3, final WinCrypt.CRYPTPROTECT_PROMPTSTRUCT p4, final int p5, final WinCrypt.DATA_BLOB p6);
    
    boolean CryptUnprotectData(final WinCrypt.DATA_BLOB p0, final PointerByReference p1, final WinCrypt.DATA_BLOB p2, final Pointer p3, final WinCrypt.CRYPTPROTECT_PROMPTSTRUCT p4, final int p5, final WinCrypt.DATA_BLOB p6);
}
