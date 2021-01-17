// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.windows;

import java.util.List;
import oshi.software.os.windows.nt.CentralProcessor;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import java.util.ArrayList;
import oshi.software.os.windows.nt.GlobalMemory;
import oshi.hardware.Memory;
import oshi.hardware.Processor;
import oshi.hardware.HardwareAbstractionLayer;

public class WindowsHardwareAbstractionLayer implements HardwareAbstractionLayer
{
    private Processor[] _processors;
    private Memory _memory;
    
    public WindowsHardwareAbstractionLayer() {
        this._processors = null;
        this._memory = null;
    }
    
    public Memory getMemory() {
        if (this._memory == null) {
            this._memory = new GlobalMemory();
        }
        return this._memory;
    }
    
    public Processor[] getProcessors() {
        if (this._processors == null) {
            final String cpuRegistryRoot = "HARDWARE\\DESCRIPTION\\System\\CentralProcessor";
            final List<Processor> processors = new ArrayList<Processor>();
            final String[] registryGetKeys;
            final String[] processorIds = registryGetKeys = Advapi32Util.registryGetKeys(WinReg.HKEY_LOCAL_MACHINE, "HARDWARE\\DESCRIPTION\\System\\CentralProcessor");
            for (final String processorId : registryGetKeys) {
                final String cpuRegistryPath = "HARDWARE\\DESCRIPTION\\System\\CentralProcessor\\" + processorId;
                final CentralProcessor cpu = new CentralProcessor();
                cpu.setIdentifier(Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, cpuRegistryPath, "Identifier"));
                cpu.setName(Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, cpuRegistryPath, "ProcessorNameString"));
                cpu.setVendor(Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, cpuRegistryPath, "VendorIdentifier"));
                processors.add(cpu);
            }
            this._processors = processors.toArray(new Processor[0]);
        }
        return this._processors;
    }
}
