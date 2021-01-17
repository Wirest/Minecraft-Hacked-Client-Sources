// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Structure;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public interface SetupApi extends StdCallLibrary
{
    public static final SetupApi INSTANCE = (SetupApi)Native.loadLibrary("setupapi", SetupApi.class, W32APIOptions.DEFAULT_OPTIONS);
    public static final Guid.GUID GUID_DEVINTERFACE_DISK = new Guid.GUID(new byte[] { 7, 99, -11, 83, -65, -74, -48, 17, -108, -14, 0, -96, -55, 30, -5, -117 });
    public static final int DIGCF_DEFAULT = 1;
    public static final int DIGCF_PRESENT = 2;
    public static final int DIGCF_ALLCLASSES = 4;
    public static final int DIGCF_PROFILE = 8;
    public static final int DIGCF_DEVICEINTERFACE = 16;
    public static final int SPDRP_REMOVAL_POLICY = 31;
    public static final int CM_DEVCAP_REMOVABLE = 4;
    
    WinNT.HANDLE SetupDiGetClassDevs(final Guid.GUID.ByReference p0, final Pointer p1, final Pointer p2, final int p3);
    
    boolean SetupDiDestroyDeviceInfoList(final WinNT.HANDLE p0);
    
    boolean SetupDiEnumDeviceInterfaces(final WinNT.HANDLE p0, final Pointer p1, final Guid.GUID.ByReference p2, final int p3, final SP_DEVICE_INTERFACE_DATA.ByReference p4);
    
    boolean SetupDiGetDeviceInterfaceDetail(final WinNT.HANDLE p0, final SP_DEVICE_INTERFACE_DATA.ByReference p1, final Pointer p2, final int p3, final IntByReference p4, final SP_DEVINFO_DATA.ByReference p5);
    
    boolean SetupDiGetDeviceRegistryProperty(final WinNT.HANDLE p0, final SP_DEVINFO_DATA.ByReference p1, final int p2, final IntByReference p3, final Pointer p4, final int p5, final IntByReference p6);
    
    public static class SP_DEVICE_INTERFACE_DATA extends Structure
    {
        public int cbSize;
        public Guid.GUID InterfaceClassGuid;
        public int Flags;
        public Pointer Reserved;
        
        public SP_DEVICE_INTERFACE_DATA() {
            this.cbSize = this.size();
        }
        
        public SP_DEVICE_INTERFACE_DATA(final Pointer memory) {
            super(memory);
            this.read();
        }
        
        public static class ByReference extends SP_DEVINFO_DATA implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    public static class SP_DEVINFO_DATA extends Structure
    {
        public int cbSize;
        public Guid.GUID InterfaceClassGuid;
        public int DevInst;
        public Pointer Reserved;
        
        public SP_DEVINFO_DATA() {
            this.cbSize = this.size();
        }
        
        public SP_DEVINFO_DATA(final Pointer memory) {
            super(memory);
            this.read();
        }
        
        public static class ByReference extends SP_DEVINFO_DATA implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
}
