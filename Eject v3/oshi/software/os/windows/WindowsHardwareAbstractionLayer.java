package oshi.software.os.windows;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Memory;
import oshi.hardware.Processor;
import oshi.software.os.windows.nt.CentralProcessor;
import oshi.software.os.windows.nt.GlobalMemory;

import java.util.ArrayList;

public class WindowsHardwareAbstractionLayer
        implements HardwareAbstractionLayer {
    private Processor[] _processors = null;
    private Memory _memory = null;

    public Memory getMemory() {
        if (this._memory == null) {
            this._memory = new GlobalMemory();
        }
        return this._memory;
    }

    public Processor[] getProcessors() {
        if (this._processors == null) {
            String str1 = "HARDWARE\\DESCRIPTION\\System\\CentralProcessor";
            ArrayList localArrayList = new ArrayList();
            String[] arrayOfString1 = Advapi32Util.registryGetKeys(WinReg.HKEY_LOCAL_MACHINE, "HARDWARE\\DESCRIPTION\\System\\CentralProcessor");
            for (String str2 : arrayOfString1) {
                String str3 = "HARDWARE\\DESCRIPTION\\System\\CentralProcessor\\" + str2;
                CentralProcessor localCentralProcessor = new CentralProcessor();
                localCentralProcessor.setIdentifier(Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, str3, "Identifier"));
                localCentralProcessor.setName(Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, str3, "ProcessorNameString"));
                localCentralProcessor.setVendor(Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, str3, "VendorIdentifier"));
                localArrayList.add(localCentralProcessor);
            }
            this._processors = ((Processor[]) localArrayList.toArray(new Processor[0]));
        }
        return this._processors;
    }
}




