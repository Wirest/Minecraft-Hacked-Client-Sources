package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByReference;
import com.sun.jna.win32.StdCallLibrary;

public abstract interface WinReg
        extends StdCallLibrary {
    public static final HKEY HKEY_CLASSES_ROOT = new HKEY(Integer.MIN_VALUE);
    public static final HKEY HKEY_CURRENT_USER = new HKEY(-2147483647);
    public static final HKEY HKEY_LOCAL_MACHINE = new HKEY(-2147483646);
    public static final HKEY HKEY_USERS = new HKEY(-2147483645);
    public static final HKEY HKEY_PERFORMANCE_DATA = new HKEY(-2147483644);
    public static final HKEY HKEY_PERFORMANCE_TEXT = new HKEY(-2147483568);
    public static final HKEY HKEY_PERFORMANCE_NLSTEXT = new HKEY(-2147483552);
    public static final HKEY HKEY_CURRENT_CONFIG = new HKEY(-2147483643);
    public static final HKEY HKEY_DYN_DATA = new HKEY(-2147483642);

    public static class HKEYByReference
            extends ByReference {
        public HKEYByReference() {
            this(null);
        }

        public HKEYByReference(WinReg.HKEY paramHKEY) {
            super();
            setValue(paramHKEY);
        }

        public WinReg.HKEY getValue() {
            Pointer localPointer = getPointer().getPointer(0L);
            if (localPointer == null) {
                return null;
            }
            if (WinBase.INVALID_HANDLE_VALUE.getPointer().equals(localPointer)) {
                return (WinReg.HKEY) WinBase.INVALID_HANDLE_VALUE;
            }
            WinReg.HKEY localHKEY = new WinReg.HKEY();
            localHKEY.setPointer(localPointer);
            return localHKEY;
        }

        public void setValue(WinReg.HKEY paramHKEY) {
            getPointer().setPointer(0L, paramHKEY != null ? paramHKEY.getPointer() : null);
        }
    }

    public static class HKEY
            extends WinNT.HANDLE {
        public HKEY() {
        }

        public HKEY(Pointer paramPointer) {
            super();
        }

        public HKEY(int paramInt) {
            super();
        }
    }
}




