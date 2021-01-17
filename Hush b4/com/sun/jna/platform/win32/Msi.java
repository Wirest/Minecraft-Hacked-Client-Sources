// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface Msi extends StdCallLibrary
{
    public static final Msi INSTANCE = (Msi)Native.loadLibrary("msi", Msi.class, W32APIOptions.UNICODE_OPTIONS);
    public static final int INSTALLSTATE_NOTUSED = -7;
    public static final int INSTALLSTATE_BADCONFIG = -6;
    public static final int INSTALLSTATE_INCOMPLETE = -5;
    public static final int INSTALLSTATE_SOURCEABSENT = -4;
    public static final int INSTALLSTATE_MOREDATA = -3;
    public static final int INSTALLSTATE_INVALIDARG = -2;
    public static final int INSTALLSTATE_UNKNOWN = -1;
    public static final int INSTALLSTATE_BROKEN = 0;
    public static final int INSTALLSTATE_ADVERTISED = 1;
    public static final int INSTALLSTATE_REMOVED = 1;
    public static final int INSTALLSTATE_ABSENT = 2;
    public static final int INSTALLSTATE_LOCAL = 3;
    public static final int INSTALLSTATE_SOURCE = 4;
    public static final int INSTALLSTATE_DEFAULT = 5;
    
    int MsiGetComponentPath(final String p0, final String p1, final char[] p2, final IntByReference p3);
    
    int MsiLocateComponent(final String p0, final char[] p1, final IntByReference p2);
    
    int MsiGetProductCode(final String p0, final char[] p1);
    
    int MsiEnumComponents(final WinDef.DWORD p0, final char[] p1);
}
