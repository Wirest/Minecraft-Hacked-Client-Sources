// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Native;

public abstract class Ole32Util
{
    public static Guid.GUID getGUIDFromString(final String guidString) {
        final Guid.GUID.ByReference lpiid = new Guid.GUID.ByReference();
        final WinNT.HRESULT hr = Ole32.INSTANCE.IIDFromString(guidString, lpiid);
        if (!hr.equals(W32Errors.S_OK)) {
            throw new RuntimeException(hr.toString());
        }
        return lpiid;
    }
    
    public static String getStringFromGUID(final Guid.GUID guid) {
        final Guid.GUID.ByReference pguid = new Guid.GUID.ByReference(guid.getPointer());
        final int max = 39;
        final char[] lpsz = new char[max];
        final int len = Ole32.INSTANCE.StringFromGUID2(pguid, lpsz, max);
        if (len == 0) {
            throw new RuntimeException("StringFromGUID2");
        }
        lpsz[len - 1] = '\0';
        return Native.toString(lpsz);
    }
    
    public static Guid.GUID generateGUID() {
        final Guid.GUID.ByReference pguid = new Guid.GUID.ByReference();
        final WinNT.HRESULT hr = Ole32.INSTANCE.CoCreateGuid(pguid);
        if (!hr.equals(W32Errors.S_OK)) {
            throw new RuntimeException(hr.toString());
        }
        return pguid;
    }
}
