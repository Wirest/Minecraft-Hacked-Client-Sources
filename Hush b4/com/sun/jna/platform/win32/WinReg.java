// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.ptr.ByReference;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public interface WinReg extends StdCallLibrary
{
    public static final HKEY HKEY_CLASSES_ROOT = new HKEY(Integer.MIN_VALUE);
    public static final HKEY HKEY_CURRENT_USER = new HKEY(-2147483647);
    public static final HKEY HKEY_LOCAL_MACHINE = new HKEY(-2147483646);
    public static final HKEY HKEY_USERS = new HKEY(-2147483645);
    public static final HKEY HKEY_PERFORMANCE_DATA = new HKEY(-2147483644);
    public static final HKEY HKEY_PERFORMANCE_TEXT = new HKEY(-2147483568);
    public static final HKEY HKEY_PERFORMANCE_NLSTEXT = new HKEY(-2147483552);
    public static final HKEY HKEY_CURRENT_CONFIG = new HKEY(-2147483643);
    public static final HKEY HKEY_DYN_DATA = new HKEY(-2147483642);
    
    public static class HKEY extends WinNT.HANDLE
    {
        public HKEY() {
        }
        
        public HKEY(final Pointer p) {
            super(p);
        }
        
        public HKEY(final int value) {
            super(new Pointer(value));
        }
    }
    
    public static class HKEYByReference extends ByReference
    {
        public HKEYByReference() {
            this((HKEY)null);
        }
        
        public HKEYByReference(final HKEY h) {
            super(Pointer.SIZE);
            this.setValue(h);
        }
        
        public void setValue(final HKEY h) {
            this.getPointer().setPointer(0L, (h != null) ? h.getPointer() : null);
        }
        
        public HKEY getValue() {
            final Pointer p = this.getPointer().getPointer(0L);
            if (p == null) {
                return null;
            }
            if (WinBase.INVALID_HANDLE_VALUE.getPointer().equals(p)) {
                return (HKEY)WinBase.INVALID_HANDLE_VALUE;
            }
            final HKEY h = new HKEY();
            h.setPointer(p);
            return h;
        }
    }
}
