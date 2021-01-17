// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

public interface WinCrypt extends StdCallLibrary
{
    public static final int CRYPTPROTECT_PROMPT_ON_UNPROTECT = 1;
    public static final int CRYPTPROTECT_PROMPT_ON_PROTECT = 2;
    public static final int CRYPTPROTECT_PROMPT_RESERVED = 4;
    public static final int CRYPTPROTECT_PROMPT_STRONG = 8;
    public static final int CRYPTPROTECT_PROMPT_REQUIRE_STRONG = 16;
    public static final int CRYPTPROTECT_UI_FORBIDDEN = 1;
    public static final int CRYPTPROTECT_LOCAL_MACHINE = 4;
    public static final int CRYPTPROTECT_CRED_SYNC = 8;
    public static final int CRYPTPROTECT_AUDIT = 16;
    public static final int CRYPTPROTECT_NO_RECOVERY = 32;
    public static final int CRYPTPROTECT_VERIFY_PROTECTION = 64;
    public static final int CRYPTPROTECT_CRED_REGENERATE = 128;
    
    public static class DATA_BLOB extends Structure
    {
        public int cbData;
        public Pointer pbData;
        
        public DATA_BLOB() {
        }
        
        public DATA_BLOB(final Pointer memory) {
            super(memory);
            this.read();
        }
        
        public DATA_BLOB(final byte[] data) {
            (this.pbData = new Memory(data.length)).write(0L, data, 0, data.length);
            this.cbData = data.length;
            this.allocateMemory();
        }
        
        public DATA_BLOB(final String s) {
            this(Native.toByteArray(s));
        }
        
        public byte[] getData() {
            return (byte[])((this.pbData == null) ? null : this.pbData.getByteArray(0L, this.cbData));
        }
    }
    
    public static class CRYPTPROTECT_PROMPTSTRUCT extends Structure
    {
        public int cbSize;
        public int dwPromptFlags;
        public WinDef.HWND hwndApp;
        public String szPrompt;
        
        public CRYPTPROTECT_PROMPTSTRUCT() {
        }
        
        public CRYPTPROTECT_PROMPTSTRUCT(final Pointer memory) {
            super(memory);
            this.read();
        }
    }
}
