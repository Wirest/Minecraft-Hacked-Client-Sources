// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

public interface Tlhelp32 extends StdCallLibrary
{
    public static final WinDef.DWORD TH32CS_SNAPHEAPLIST = new WinDef.DWORD(1L);
    public static final WinDef.DWORD TH32CS_SNAPPROCESS = new WinDef.DWORD(2L);
    public static final WinDef.DWORD TH32CS_SNAPTHREAD = new WinDef.DWORD(4L);
    public static final WinDef.DWORD TH32CS_SNAPMODULE = new WinDef.DWORD(8L);
    public static final WinDef.DWORD TH32CS_SNAPMODULE32 = new WinDef.DWORD(16L);
    public static final WinDef.DWORD TH32CS_SNAPALL = new WinDef.DWORD((long)(Tlhelp32.TH32CS_SNAPHEAPLIST.intValue() | Tlhelp32.TH32CS_SNAPPROCESS.intValue() | Tlhelp32.TH32CS_SNAPTHREAD.intValue() | Tlhelp32.TH32CS_SNAPMODULE.intValue()));
    public static final WinDef.DWORD TH32CS_INHERIT = new WinDef.DWORD(-2147483648L);
    
    public static class PROCESSENTRY32 extends Structure
    {
        public WinDef.DWORD dwSize;
        public WinDef.DWORD cntUsage;
        public WinDef.DWORD th32ProcessID;
        public BaseTSD.ULONG_PTR th32DefaultHeapID;
        public WinDef.DWORD th32ModuleID;
        public WinDef.DWORD cntThreads;
        public WinDef.DWORD th32ParentProcessID;
        public WinDef.LONG pcPriClassBase;
        public WinDef.DWORD dwFlags;
        public char[] szExeFile;
        
        public PROCESSENTRY32() {
            this.szExeFile = new char[260];
            this.dwSize = new WinDef.DWORD((long)this.size());
        }
        
        public PROCESSENTRY32(final Pointer memory) {
            super(memory);
            this.szExeFile = new char[260];
            this.read();
        }
        
        public static class ByReference extends PROCESSENTRY32 implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
}
