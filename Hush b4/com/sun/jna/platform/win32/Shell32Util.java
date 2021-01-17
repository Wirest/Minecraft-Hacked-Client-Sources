// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Native;

public abstract class Shell32Util
{
    public static String getFolderPath(final WinDef.HWND hwnd, final int nFolder, final WinDef.DWORD dwFlags) {
        final char[] pszPath = new char[260];
        final WinNT.HRESULT hr = Shell32.INSTANCE.SHGetFolderPath(hwnd, nFolder, null, dwFlags, pszPath);
        if (!hr.equals(W32Errors.S_OK)) {
            throw new Win32Exception(hr);
        }
        return Native.toString(pszPath);
    }
    
    public static String getFolderPath(final int nFolder) {
        return getFolderPath(null, nFolder, ShlObj.SHGFP_TYPE_CURRENT);
    }
}
