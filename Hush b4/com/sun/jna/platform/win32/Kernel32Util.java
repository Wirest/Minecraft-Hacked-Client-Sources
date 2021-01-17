// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.util.List;
import java.util.ArrayList;
import com.sun.jna.LastErrorException;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

public abstract class Kernel32Util implements WinDef
{
    public static String getComputerName() {
        final char[] buffer = new char[WinBase.MAX_COMPUTERNAME_LENGTH + 1];
        final IntByReference lpnSize = new IntByReference(buffer.length);
        if (!Kernel32.INSTANCE.GetComputerName(buffer, lpnSize)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return Native.toString(buffer);
    }
    
    public static String formatMessageFromHR(final WinNT.HRESULT code) {
        final PointerByReference buffer = new PointerByReference();
        if (0 == Kernel32.INSTANCE.FormatMessage(4864, null, code.intValue(), 0, buffer, 0, null)) {
            throw new LastErrorException(Kernel32.INSTANCE.GetLastError());
        }
        final String s = buffer.getValue().getString(0L, !Boolean.getBoolean("w32.ascii"));
        Kernel32.INSTANCE.LocalFree(buffer.getValue());
        return s.trim();
    }
    
    public static String formatMessageFromLastErrorCode(final int code) {
        return formatMessageFromHR(W32Errors.HRESULT_FROM_WIN32(code));
    }
    
    public static String getTempPath() {
        final DWORD nBufferLength = new DWORD(260L);
        final char[] buffer = new char[nBufferLength.intValue()];
        if (Kernel32.INSTANCE.GetTempPath(nBufferLength, buffer).intValue() == 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return Native.toString(buffer);
    }
    
    public static void deleteFile(final String filename) {
        if (!Kernel32.INSTANCE.DeleteFile(filename)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }
    
    public static String[] getLogicalDriveStrings() {
        DWORD dwSize = Kernel32.INSTANCE.GetLogicalDriveStrings(new DWORD(0L), null);
        if (dwSize.intValue() <= 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        final char[] buf = new char[dwSize.intValue()];
        dwSize = Kernel32.INSTANCE.GetLogicalDriveStrings(dwSize, buf);
        if (dwSize.intValue() <= 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        final List<String> drives = new ArrayList<String>();
        String drive = "";
        for (int i = 0; i < buf.length - 1; ++i) {
            if (buf[i] == '\0') {
                drives.add(drive);
                drive = "";
            }
            else {
                drive += buf[i];
            }
        }
        return drives.toArray(new String[0]);
    }
    
    public static int getFileAttributes(final String fileName) {
        final int fileAttributes = Kernel32.INSTANCE.GetFileAttributes(fileName);
        if (fileAttributes == -1) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return fileAttributes;
    }
}
