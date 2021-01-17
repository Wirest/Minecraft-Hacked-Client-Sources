// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Pointer;

public abstract class Crypt32Util
{
    public static byte[] cryptProtectData(final byte[] data) {
        return cryptProtectData(data, 0);
    }
    
    public static byte[] cryptProtectData(final byte[] data, final int flags) {
        return cryptProtectData(data, null, flags, "", null);
    }
    
    public static byte[] cryptProtectData(final byte[] data, final byte[] entropy, final int flags, final String description, final WinCrypt.CRYPTPROTECT_PROMPTSTRUCT prompt) {
        final WinCrypt.DATA_BLOB pDataIn = new WinCrypt.DATA_BLOB(data);
        final WinCrypt.DATA_BLOB pDataProtected = new WinCrypt.DATA_BLOB();
        final WinCrypt.DATA_BLOB pEntropy = (entropy == null) ? null : new WinCrypt.DATA_BLOB(entropy);
        try {
            if (!Crypt32.INSTANCE.CryptProtectData(pDataIn, description, pEntropy, null, prompt, flags, pDataProtected)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            return pDataProtected.getData();
        }
        finally {
            if (pDataProtected.pbData != null) {
                Kernel32.INSTANCE.LocalFree(pDataProtected.pbData);
            }
        }
    }
    
    public static byte[] cryptUnprotectData(final byte[] data) {
        return cryptUnprotectData(data, 0);
    }
    
    public static byte[] cryptUnprotectData(final byte[] data, final int flags) {
        return cryptUnprotectData(data, null, flags, null);
    }
    
    public static byte[] cryptUnprotectData(final byte[] data, final byte[] entropy, final int flags, final WinCrypt.CRYPTPROTECT_PROMPTSTRUCT prompt) {
        final WinCrypt.DATA_BLOB pDataIn = new WinCrypt.DATA_BLOB(data);
        final WinCrypt.DATA_BLOB pDataUnprotected = new WinCrypt.DATA_BLOB();
        final WinCrypt.DATA_BLOB pEntropy = (entropy == null) ? null : new WinCrypt.DATA_BLOB(entropy);
        final PointerByReference pDescription = new PointerByReference();
        try {
            if (!Crypt32.INSTANCE.CryptUnprotectData(pDataIn, pDescription, pEntropy, null, prompt, flags, pDataUnprotected)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            return pDataUnprotected.getData();
        }
        finally {
            if (pDataUnprotected.pbData != null) {
                Kernel32.INSTANCE.LocalFree(pDataUnprotected.pbData);
            }
            if (pDescription.getValue() != null) {
                Kernel32.INSTANCE.LocalFree(pDescription.getValue());
            }
        }
    }
}
