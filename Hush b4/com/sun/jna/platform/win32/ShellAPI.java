// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.Structure;
import com.sun.jna.Platform;
import com.sun.jna.win32.StdCallLibrary;

public interface ShellAPI extends StdCallLibrary
{
    public static final int STRUCTURE_ALIGNMENT = !Platform.is64Bit();
    public static final int FO_MOVE = 1;
    public static final int FO_COPY = 2;
    public static final int FO_DELETE = 3;
    public static final int FO_RENAME = 4;
    public static final int FOF_MULTIDESTFILES = 1;
    public static final int FOF_CONFIRMMOUSE = 2;
    public static final int FOF_SILENT = 4;
    public static final int FOF_RENAMEONCOLLISION = 8;
    public static final int FOF_NOCONFIRMATION = 16;
    public static final int FOF_WANTMAPPINGHANDLE = 32;
    public static final int FOF_ALLOWUNDO = 64;
    public static final int FOF_FILESONLY = 128;
    public static final int FOF_SIMPLEPROGRESS = 256;
    public static final int FOF_NOCONFIRMMKDIR = 512;
    public static final int FOF_NOERRORUI = 1024;
    public static final int FOF_NOCOPYSECURITYATTRIBS = 2048;
    public static final int FOF_NORECURSION = 4096;
    public static final int FOF_NO_CONNECTED_ELEMENTS = 8192;
    public static final int FOF_WANTNUKEWARNING = 16384;
    public static final int FOF_NORECURSEREPARSE = 32768;
    public static final int FOF_NO_UI = 1556;
    public static final int PO_DELETE = 19;
    public static final int PO_RENAME = 20;
    public static final int PO_PORTCHANGE = 32;
    public static final int PO_REN_PORT = 52;
    
    public static class SHFILEOPSTRUCT extends Structure
    {
        public WinNT.HANDLE hwnd;
        public int wFunc;
        public WString pFrom;
        public WString pTo;
        public short fFlags;
        public boolean fAnyOperationsAborted;
        public Pointer pNameMappings;
        public WString lpszProgressTitle;
        
        public String encodePaths(final String[] paths) {
            String encoded = "";
            for (int i = 0; i < paths.length; ++i) {
                encoded += paths[i];
                encoded += "\u0000";
            }
            return encoded + "\u0000";
        }
    }
}
